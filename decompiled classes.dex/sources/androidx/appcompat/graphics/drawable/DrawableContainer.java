package androidx.appcompat.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.util.SparseArray;
import androidx.core.graphics.drawable.DrawableCompat;

class DrawableContainer extends Drawable implements Drawable.Callback {
    private static final boolean DEBUG = false;
    private static final boolean DEFAULT_DITHER = true;
    private static final String TAG = "DrawableContainer";
    private int mAlpha = 255;
    private Runnable mAnimationRunnable;
    private BlockInvalidateCallback mBlockInvalidateCallback;
    private int mCurIndex = -1;
    private Drawable mCurrDrawable;
    private DrawableContainerState mDrawableContainerState;
    private long mEnterAnimationEnd;
    private long mExitAnimationEnd;
    private boolean mHasAlpha;
    private Rect mHotspotBounds;
    private Drawable mLastDrawable;
    private boolean mMutated;

    private static class Api21Impl {
        private Api21Impl() {
        }

        public static boolean canApplyTheme(Drawable.ConstantState constantState) {
            return constantState.canApplyTheme();
        }

        public static void getOutline(Drawable drawable, Outline outline) {
            drawable.getOutline(outline);
        }

        public static Resources getResources(Resources.Theme theme) {
            return theme.getResources();
        }
    }

    static class BlockInvalidateCallback implements Drawable.Callback {
        private Drawable.Callback mCallback;

        BlockInvalidateCallback() {
        }

        public void invalidateDrawable(Drawable who) {
        }

        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            Drawable.Callback callback = this.mCallback;
            if (callback != null) {
                callback.scheduleDrawable(who, what, when);
            }
        }

        public void unscheduleDrawable(Drawable who, Runnable what) {
            Drawable.Callback callback = this.mCallback;
            if (callback != null) {
                callback.unscheduleDrawable(who, what);
            }
        }

        public Drawable.Callback unwrap() {
            Drawable.Callback callback = this.mCallback;
            this.mCallback = null;
            return callback;
        }

        public BlockInvalidateCallback wrap(Drawable.Callback callback) {
            this.mCallback = callback;
            return this;
        }
    }

    static abstract class DrawableContainerState extends Drawable.ConstantState {
        boolean mAutoMirrored;
        boolean mCanConstantState;
        int mChangingConfigurations;
        boolean mCheckedConstantSize;
        boolean mCheckedConstantState;
        boolean mCheckedOpacity;
        boolean mCheckedPadding;
        boolean mCheckedStateful;
        int mChildrenChangingConfigurations;
        ColorFilter mColorFilter;
        int mConstantHeight;
        int mConstantMinimumHeight;
        int mConstantMinimumWidth;
        Rect mConstantPadding;
        boolean mConstantSize = false;
        int mConstantWidth;
        int mDensity;
        boolean mDither = DrawableContainer.DEFAULT_DITHER;
        SparseArray<Drawable.ConstantState> mDrawableFutures;
        Drawable[] mDrawables;
        int mEnterFadeDuration = 0;
        int mExitFadeDuration = 0;
        boolean mHasColorFilter;
        boolean mHasTintList;
        boolean mHasTintMode;
        int mLayoutDirection;
        boolean mMutated;
        int mNumChildren;
        int mOpacity;
        final DrawableContainer mOwner;
        Resources mSourceRes;
        boolean mStateful;
        ColorStateList mTintList;
        PorterDuff.Mode mTintMode;
        boolean mVariablePadding = false;

        DrawableContainerState(DrawableContainerState orig, DrawableContainer owner, Resources res) {
            this.mOwner = owner;
            Rect rect = null;
            this.mSourceRes = res != null ? res : orig != null ? orig.mSourceRes : null;
            int resolveDensity = DrawableContainer.resolveDensity(res, orig != null ? orig.mDensity : 0);
            this.mDensity = resolveDensity;
            if (orig != null) {
                this.mChangingConfigurations = orig.mChangingConfigurations;
                this.mChildrenChangingConfigurations = orig.mChildrenChangingConfigurations;
                this.mCheckedConstantState = DrawableContainer.DEFAULT_DITHER;
                this.mCanConstantState = DrawableContainer.DEFAULT_DITHER;
                this.mVariablePadding = orig.mVariablePadding;
                this.mConstantSize = orig.mConstantSize;
                this.mDither = orig.mDither;
                this.mMutated = orig.mMutated;
                this.mLayoutDirection = orig.mLayoutDirection;
                this.mEnterFadeDuration = orig.mEnterFadeDuration;
                this.mExitFadeDuration = orig.mExitFadeDuration;
                this.mAutoMirrored = orig.mAutoMirrored;
                this.mColorFilter = orig.mColorFilter;
                this.mHasColorFilter = orig.mHasColorFilter;
                this.mTintList = orig.mTintList;
                this.mTintMode = orig.mTintMode;
                this.mHasTintList = orig.mHasTintList;
                this.mHasTintMode = orig.mHasTintMode;
                if (orig.mDensity == resolveDensity) {
                    if (orig.mCheckedPadding) {
                        this.mConstantPadding = orig.mConstantPadding != null ? new Rect(orig.mConstantPadding) : rect;
                        this.mCheckedPadding = DrawableContainer.DEFAULT_DITHER;
                    }
                    if (orig.mCheckedConstantSize) {
                        this.mConstantWidth = orig.mConstantWidth;
                        this.mConstantHeight = orig.mConstantHeight;
                        this.mConstantMinimumWidth = orig.mConstantMinimumWidth;
                        this.mConstantMinimumHeight = orig.mConstantMinimumHeight;
                        this.mCheckedConstantSize = DrawableContainer.DEFAULT_DITHER;
                    }
                }
                if (orig.mCheckedOpacity) {
                    this.mOpacity = orig.mOpacity;
                    this.mCheckedOpacity = DrawableContainer.DEFAULT_DITHER;
                }
                if (orig.mCheckedStateful) {
                    this.mStateful = orig.mStateful;
                    this.mCheckedStateful = DrawableContainer.DEFAULT_DITHER;
                }
                Drawable[] drawableArr = orig.mDrawables;
                this.mDrawables = new Drawable[drawableArr.length];
                this.mNumChildren = orig.mNumChildren;
                SparseArray<Drawable.ConstantState> sparseArray = orig.mDrawableFutures;
                if (sparseArray != null) {
                    this.mDrawableFutures = sparseArray.clone();
                } else {
                    this.mDrawableFutures = new SparseArray<>(this.mNumChildren);
                }
                int i = this.mNumChildren;
                for (int i2 = 0; i2 < i; i2++) {
                    if (drawableArr[i2] != null) {
                        Drawable.ConstantState constantState = drawableArr[i2].getConstantState();
                        if (constantState != null) {
                            this.mDrawableFutures.put(i2, constantState);
                        } else {
                            this.mDrawables[i2] = drawableArr[i2];
                        }
                    }
                }
                return;
            }
            this.mDrawables = new Drawable[10];
            this.mNumChildren = 0;
        }

        private void createAllFutures() {
            SparseArray<Drawable.ConstantState> sparseArray = this.mDrawableFutures;
            if (sparseArray != null) {
                int size = sparseArray.size();
                for (int i = 0; i < size; i++) {
                    this.mDrawables[this.mDrawableFutures.keyAt(i)] = prepareDrawable(this.mDrawableFutures.valueAt(i).newDrawable(this.mSourceRes));
                }
                this.mDrawableFutures = null;
            }
        }

        private Drawable prepareDrawable(Drawable child) {
            if (Build.VERSION.SDK_INT >= 23) {
                DrawableCompat.setLayoutDirection(child, this.mLayoutDirection);
            }
            Drawable child2 = child.mutate();
            child2.setCallback(this.mOwner);
            return child2;
        }

        public final int addChild(Drawable dr) {
            int i = this.mNumChildren;
            if (i >= this.mDrawables.length) {
                growArray(i, i + 10);
            }
            dr.mutate();
            dr.setVisible(false, DrawableContainer.DEFAULT_DITHER);
            dr.setCallback(this.mOwner);
            this.mDrawables[i] = dr;
            this.mNumChildren++;
            this.mChildrenChangingConfigurations |= dr.getChangingConfigurations();
            invalidateCache();
            this.mConstantPadding = null;
            this.mCheckedPadding = false;
            this.mCheckedConstantSize = false;
            this.mCheckedConstantState = false;
            return i;
        }

        /* access modifiers changed from: package-private */
        public final void applyTheme(Resources.Theme theme) {
            if (theme != null) {
                createAllFutures();
                int i = this.mNumChildren;
                Drawable[] drawableArr = this.mDrawables;
                for (int i2 = 0; i2 < i; i2++) {
                    if (drawableArr[i2] != null && DrawableCompat.canApplyTheme(drawableArr[i2])) {
                        DrawableCompat.applyTheme(drawableArr[i2], theme);
                        this.mChildrenChangingConfigurations |= drawableArr[i2].getChangingConfigurations();
                    }
                }
                updateDensity(Api21Impl.getResources(theme));
            }
        }

        public boolean canApplyTheme() {
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            for (int i2 = 0; i2 < i; i2++) {
                Drawable drawable = drawableArr[i2];
                if (drawable == null) {
                    Drawable.ConstantState constantState = this.mDrawableFutures.get(i2);
                    if (constantState != null && Api21Impl.canApplyTheme(constantState)) {
                        return DrawableContainer.DEFAULT_DITHER;
                    }
                } else if (DrawableCompat.canApplyTheme(drawable)) {
                    return DrawableContainer.DEFAULT_DITHER;
                }
            }
            return false;
        }

        public boolean canConstantState() {
            if (this.mCheckedConstantState) {
                return this.mCanConstantState;
            }
            createAllFutures();
            this.mCheckedConstantState = DrawableContainer.DEFAULT_DITHER;
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            for (int i2 = 0; i2 < i; i2++) {
                if (drawableArr[i2].getConstantState() == null) {
                    this.mCanConstantState = false;
                    return false;
                }
            }
            this.mCanConstantState = DrawableContainer.DEFAULT_DITHER;
            return DrawableContainer.DEFAULT_DITHER;
        }

        /* access modifiers changed from: package-private */
        public final void clearMutated() {
            this.mMutated = false;
        }

        /* access modifiers changed from: protected */
        public void computeConstantSize() {
            this.mCheckedConstantSize = DrawableContainer.DEFAULT_DITHER;
            createAllFutures();
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            this.mConstantHeight = -1;
            this.mConstantWidth = -1;
            this.mConstantMinimumHeight = 0;
            this.mConstantMinimumWidth = 0;
            for (int i2 = 0; i2 < i; i2++) {
                Drawable drawable = drawableArr[i2];
                int intrinsicWidth = drawable.getIntrinsicWidth();
                if (intrinsicWidth > this.mConstantWidth) {
                    this.mConstantWidth = intrinsicWidth;
                }
                int intrinsicHeight = drawable.getIntrinsicHeight();
                if (intrinsicHeight > this.mConstantHeight) {
                    this.mConstantHeight = intrinsicHeight;
                }
                int minimumWidth = drawable.getMinimumWidth();
                if (minimumWidth > this.mConstantMinimumWidth) {
                    this.mConstantMinimumWidth = minimumWidth;
                }
                int minimumHeight = drawable.getMinimumHeight();
                if (minimumHeight > this.mConstantMinimumHeight) {
                    this.mConstantMinimumHeight = minimumHeight;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public final int getCapacity() {
            return this.mDrawables.length;
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations | this.mChildrenChangingConfigurations;
        }

        public final Drawable getChild(int index) {
            int indexOfKey;
            Drawable drawable = this.mDrawables[index];
            if (drawable != null) {
                return drawable;
            }
            SparseArray<Drawable.ConstantState> sparseArray = this.mDrawableFutures;
            if (sparseArray == null || (indexOfKey = sparseArray.indexOfKey(index)) < 0) {
                return null;
            }
            Drawable prepareDrawable = prepareDrawable(this.mDrawableFutures.valueAt(indexOfKey).newDrawable(this.mSourceRes));
            this.mDrawables[index] = prepareDrawable;
            this.mDrawableFutures.removeAt(indexOfKey);
            if (this.mDrawableFutures.size() == 0) {
                this.mDrawableFutures = null;
            }
            return prepareDrawable;
        }

        public final int getChildCount() {
            return this.mNumChildren;
        }

        public final int getConstantHeight() {
            if (!this.mCheckedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantHeight;
        }

        public final int getConstantMinimumHeight() {
            if (!this.mCheckedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantMinimumHeight;
        }

        public final int getConstantMinimumWidth() {
            if (!this.mCheckedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantMinimumWidth;
        }

        public final Rect getConstantPadding() {
            if (this.mVariablePadding) {
                return null;
            }
            Rect rect = this.mConstantPadding;
            if (rect != null || this.mCheckedPadding) {
                return rect;
            }
            createAllFutures();
            Rect rect2 = null;
            Rect rect3 = new Rect();
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            for (int i2 = 0; i2 < i; i2++) {
                if (drawableArr[i2].getPadding(rect3)) {
                    if (rect2 == null) {
                        rect2 = new Rect(0, 0, 0, 0);
                    }
                    if (rect3.left > rect2.left) {
                        rect2.left = rect3.left;
                    }
                    if (rect3.top > rect2.top) {
                        rect2.top = rect3.top;
                    }
                    if (rect3.right > rect2.right) {
                        rect2.right = rect3.right;
                    }
                    if (rect3.bottom > rect2.bottom) {
                        rect2.bottom = rect3.bottom;
                    }
                }
            }
            this.mCheckedPadding = DrawableContainer.DEFAULT_DITHER;
            this.mConstantPadding = rect2;
            return rect2;
        }

        public final int getConstantWidth() {
            if (!this.mCheckedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantWidth;
        }

        public final int getEnterFadeDuration() {
            return this.mEnterFadeDuration;
        }

        public final int getExitFadeDuration() {
            return this.mExitFadeDuration;
        }

        public final int getOpacity() {
            if (this.mCheckedOpacity) {
                return this.mOpacity;
            }
            createAllFutures();
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            int opacity = i > 0 ? drawableArr[0].getOpacity() : -2;
            for (int i2 = 1; i2 < i; i2++) {
                opacity = Drawable.resolveOpacity(opacity, drawableArr[i2].getOpacity());
            }
            this.mOpacity = opacity;
            this.mCheckedOpacity = DrawableContainer.DEFAULT_DITHER;
            return opacity;
        }

        public void growArray(int oldSize, int newSize) {
            Drawable[] drawableArr = new Drawable[newSize];
            Drawable[] drawableArr2 = this.mDrawables;
            if (drawableArr2 != null) {
                System.arraycopy(drawableArr2, 0, drawableArr, 0, oldSize);
            }
            this.mDrawables = drawableArr;
        }

        /* access modifiers changed from: package-private */
        public void invalidateCache() {
            this.mCheckedOpacity = false;
            this.mCheckedStateful = false;
        }

        public final boolean isConstantSize() {
            return this.mConstantSize;
        }

        public final boolean isStateful() {
            if (this.mCheckedStateful) {
                return this.mStateful;
            }
            createAllFutures();
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            boolean z = false;
            int i2 = 0;
            while (true) {
                if (i2 >= i) {
                    break;
                } else if (drawableArr[i2].isStateful()) {
                    z = DrawableContainer.DEFAULT_DITHER;
                    break;
                } else {
                    i2++;
                }
            }
            this.mStateful = z;
            this.mCheckedStateful = DrawableContainer.DEFAULT_DITHER;
            return z;
        }

        /* access modifiers changed from: package-private */
        public void mutate() {
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            for (int i2 = 0; i2 < i; i2++) {
                if (drawableArr[i2] != null) {
                    drawableArr[i2].mutate();
                }
            }
            this.mMutated = DrawableContainer.DEFAULT_DITHER;
        }

        public final void setConstantSize(boolean constant) {
            this.mConstantSize = constant;
        }

        public final void setEnterFadeDuration(int duration) {
            this.mEnterFadeDuration = duration;
        }

        public final void setExitFadeDuration(int duration) {
            this.mExitFadeDuration = duration;
        }

        /* access modifiers changed from: package-private */
        public final boolean setLayoutDirection(int layoutDirection, int currentIndex) {
            boolean z = false;
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            for (int i2 = 0; i2 < i; i2++) {
                if (drawableArr[i2] != null) {
                    boolean z2 = false;
                    if (Build.VERSION.SDK_INT >= 23) {
                        z2 = DrawableCompat.setLayoutDirection(drawableArr[i2], layoutDirection);
                    }
                    if (i2 == currentIndex) {
                        z = z2;
                    }
                }
            }
            this.mLayoutDirection = layoutDirection;
            return z;
        }

        public final void setVariablePadding(boolean variable) {
            this.mVariablePadding = variable;
        }

        /* access modifiers changed from: package-private */
        public final void updateDensity(Resources res) {
            if (res != null) {
                this.mSourceRes = res;
                int resolveDensity = DrawableContainer.resolveDensity(res, this.mDensity);
                int i = this.mDensity;
                this.mDensity = resolveDensity;
                if (i != resolveDensity) {
                    this.mCheckedConstantSize = false;
                    this.mCheckedPadding = false;
                }
            }
        }
    }

    DrawableContainer() {
    }

    private void initializeDrawableForDisplay(Drawable d) {
        if (this.mBlockInvalidateCallback == null) {
            this.mBlockInvalidateCallback = new BlockInvalidateCallback();
        }
        d.setCallback(this.mBlockInvalidateCallback.wrap(d.getCallback()));
        try {
            if (this.mDrawableContainerState.mEnterFadeDuration <= 0 && this.mHasAlpha) {
                d.setAlpha(this.mAlpha);
            }
            if (this.mDrawableContainerState.mHasColorFilter) {
                d.setColorFilter(this.mDrawableContainerState.mColorFilter);
            } else {
                if (this.mDrawableContainerState.mHasTintList) {
                    DrawableCompat.setTintList(d, this.mDrawableContainerState.mTintList);
                }
                if (this.mDrawableContainerState.mHasTintMode) {
                    DrawableCompat.setTintMode(d, this.mDrawableContainerState.mTintMode);
                }
            }
            d.setVisible(isVisible(), DEFAULT_DITHER);
            d.setDither(this.mDrawableContainerState.mDither);
            d.setState(getState());
            d.setLevel(getLevel());
            d.setBounds(getBounds());
            if (Build.VERSION.SDK_INT >= 23) {
                DrawableCompat.setLayoutDirection(d, DrawableCompat.getLayoutDirection(this));
            }
            if (Build.VERSION.SDK_INT >= 19) {
                DrawableCompat.setAutoMirrored(d, this.mDrawableContainerState.mAutoMirrored);
            }
            Rect rect = this.mHotspotBounds;
            if (Build.VERSION.SDK_INT >= 21 && rect != null) {
                DrawableCompat.setHotspotBounds(d, rect.left, rect.top, rect.right, rect.bottom);
            }
        } finally {
            d.setCallback(this.mBlockInvalidateCallback.unwrap());
        }
    }

    private boolean needsMirroring() {
        if (!isAutoMirrored() || DrawableCompat.getLayoutDirection(this) != 1) {
            return false;
        }
        return DEFAULT_DITHER;
    }

    static int resolveDensity(Resources r, int parentDensity) {
        int i = r == null ? parentDensity : r.getDisplayMetrics().densityDpi;
        if (i == 0) {
            return 160;
        }
        return i;
    }

    /* access modifiers changed from: package-private */
    public void animate(boolean schedule) {
        this.mHasAlpha = DEFAULT_DITHER;
        long uptimeMillis = SystemClock.uptimeMillis();
        boolean z = false;
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            long j = this.mEnterAnimationEnd;
            if (j != 0) {
                if (j <= uptimeMillis) {
                    drawable.setAlpha(this.mAlpha);
                    this.mEnterAnimationEnd = 0;
                } else {
                    this.mCurrDrawable.setAlpha(((255 - (((int) ((j - uptimeMillis) * 255)) / this.mDrawableContainerState.mEnterFadeDuration)) * this.mAlpha) / 255);
                    z = DEFAULT_DITHER;
                }
            }
        } else {
            this.mEnterAnimationEnd = 0;
        }
        Drawable drawable2 = this.mLastDrawable;
        if (drawable2 != null) {
            long j2 = this.mExitAnimationEnd;
            if (j2 != 0) {
                if (j2 <= uptimeMillis) {
                    drawable2.setVisible(false, false);
                    this.mLastDrawable = null;
                    this.mExitAnimationEnd = 0;
                } else {
                    this.mLastDrawable.setAlpha((this.mAlpha * (((int) ((j2 - uptimeMillis) * 255)) / this.mDrawableContainerState.mExitFadeDuration)) / 255);
                    z = DEFAULT_DITHER;
                }
            }
        } else {
            this.mExitAnimationEnd = 0;
        }
        if (schedule && z) {
            scheduleSelf(this.mAnimationRunnable, 16 + uptimeMillis);
        }
    }

    public void applyTheme(Resources.Theme theme) {
        this.mDrawableContainerState.applyTheme(theme);
    }

    public boolean canApplyTheme() {
        return this.mDrawableContainerState.canApplyTheme();
    }

    /* access modifiers changed from: package-private */
    public void clearMutated() {
        this.mDrawableContainerState.clearMutated();
        this.mMutated = false;
    }

    /* access modifiers changed from: package-private */
    public DrawableContainerState cloneConstantState() {
        return this.mDrawableContainerState;
    }

    public void draw(Canvas canvas) {
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            drawable.draw(canvas);
        }
        Drawable drawable2 = this.mLastDrawable;
        if (drawable2 != null) {
            drawable2.draw(canvas);
        }
    }

    public int getAlpha() {
        return this.mAlpha;
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mDrawableContainerState.getChangingConfigurations();
    }

    public final Drawable.ConstantState getConstantState() {
        if (!this.mDrawableContainerState.canConstantState()) {
            return null;
        }
        this.mDrawableContainerState.mChangingConfigurations = getChangingConfigurations();
        return this.mDrawableContainerState;
    }

    public Drawable getCurrent() {
        return this.mCurrDrawable;
    }

    /* access modifiers changed from: package-private */
    public int getCurrentIndex() {
        return this.mCurIndex;
    }

    public void getHotspotBounds(Rect outRect) {
        Rect rect = this.mHotspotBounds;
        if (rect != null) {
            outRect.set(rect);
        } else {
            super.getHotspotBounds(outRect);
        }
    }

    public int getIntrinsicHeight() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantHeight();
        }
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            return drawable.getIntrinsicHeight();
        }
        return -1;
    }

    public int getIntrinsicWidth() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantWidth();
        }
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            return drawable.getIntrinsicWidth();
        }
        return -1;
    }

    public int getMinimumHeight() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantMinimumHeight();
        }
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            return drawable.getMinimumHeight();
        }
        return 0;
    }

    public int getMinimumWidth() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantMinimumWidth();
        }
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            return drawable.getMinimumWidth();
        }
        return 0;
    }

    public int getOpacity() {
        Drawable drawable = this.mCurrDrawable;
        if (drawable == null || !drawable.isVisible()) {
            return -2;
        }
        return this.mDrawableContainerState.getOpacity();
    }

    public void getOutline(Outline outline) {
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            Api21Impl.getOutline(drawable, outline);
        }
    }

    public boolean getPadding(Rect padding) {
        boolean z;
        Rect constantPadding = this.mDrawableContainerState.getConstantPadding();
        if (constantPadding != null) {
            padding.set(constantPadding);
            z = (((constantPadding.left | constantPadding.top) | constantPadding.bottom) | constantPadding.right) != 0 ? DEFAULT_DITHER : false;
        } else {
            Drawable drawable = this.mCurrDrawable;
            z = drawable != null ? drawable.getPadding(padding) : super.getPadding(padding);
        }
        if (needsMirroring()) {
            int i = padding.left;
            padding.left = padding.right;
            padding.right = i;
        }
        return z;
    }

    public void invalidateDrawable(Drawable who) {
        DrawableContainerState drawableContainerState = this.mDrawableContainerState;
        if (drawableContainerState != null) {
            drawableContainerState.invalidateCache();
        }
        if (who == this.mCurrDrawable && getCallback() != null) {
            getCallback().invalidateDrawable(this);
        }
    }

    public boolean isAutoMirrored() {
        return this.mDrawableContainerState.mAutoMirrored;
    }

    public boolean isStateful() {
        return this.mDrawableContainerState.isStateful();
    }

    public void jumpToCurrentState() {
        boolean z = false;
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            drawable.jumpToCurrentState();
            this.mLastDrawable = null;
            z = DEFAULT_DITHER;
        }
        Drawable drawable2 = this.mCurrDrawable;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
            if (this.mHasAlpha) {
                this.mCurrDrawable.setAlpha(this.mAlpha);
            }
        }
        if (this.mExitAnimationEnd != 0) {
            this.mExitAnimationEnd = 0;
            z = DEFAULT_DITHER;
        }
        if (this.mEnterAnimationEnd != 0) {
            this.mEnterAnimationEnd = 0;
            z = DEFAULT_DITHER;
        }
        if (z) {
            invalidateSelf();
        }
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            DrawableContainerState cloneConstantState = cloneConstantState();
            cloneConstantState.mutate();
            setConstantState(cloneConstantState);
            this.mMutated = DEFAULT_DITHER;
        }
        return this;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect bounds) {
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            drawable.setBounds(bounds);
        }
        Drawable drawable2 = this.mCurrDrawable;
        if (drawable2 != null) {
            drawable2.setBounds(bounds);
        }
    }

    public boolean onLayoutDirectionChanged(int layoutDirection) {
        return this.mDrawableContainerState.setLayoutDirection(layoutDirection, getCurrentIndex());
    }

    /* access modifiers changed from: protected */
    public boolean onLevelChange(int level) {
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            return drawable.setLevel(level);
        }
        Drawable drawable2 = this.mCurrDrawable;
        if (drawable2 != null) {
            return drawable2.setLevel(level);
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] state) {
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            return drawable.setState(state);
        }
        Drawable drawable2 = this.mCurrDrawable;
        if (drawable2 != null) {
            return drawable2.setState(state);
        }
        return false;
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        if (who == this.mCurrDrawable && getCallback() != null) {
            getCallback().scheduleDrawable(this, what, when);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean selectDrawable(int index) {
        if (index == this.mCurIndex) {
            return false;
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        if (this.mDrawableContainerState.mExitFadeDuration > 0) {
            Drawable drawable = this.mLastDrawable;
            if (drawable != null) {
                drawable.setVisible(false, false);
            }
            Drawable drawable2 = this.mCurrDrawable;
            if (drawable2 != null) {
                this.mLastDrawable = drawable2;
                this.mExitAnimationEnd = ((long) this.mDrawableContainerState.mExitFadeDuration) + uptimeMillis;
            } else {
                this.mLastDrawable = null;
                this.mExitAnimationEnd = 0;
            }
        } else {
            Drawable drawable3 = this.mCurrDrawable;
            if (drawable3 != null) {
                drawable3.setVisible(false, false);
            }
        }
        if (index < 0 || index >= this.mDrawableContainerState.mNumChildren) {
            this.mCurrDrawable = null;
            this.mCurIndex = -1;
        } else {
            Drawable child = this.mDrawableContainerState.getChild(index);
            this.mCurrDrawable = child;
            this.mCurIndex = index;
            if (child != null) {
                if (this.mDrawableContainerState.mEnterFadeDuration > 0) {
                    this.mEnterAnimationEnd = ((long) this.mDrawableContainerState.mEnterFadeDuration) + uptimeMillis;
                }
                initializeDrawableForDisplay(child);
            }
        }
        if (!(this.mEnterAnimationEnd == 0 && this.mExitAnimationEnd == 0)) {
            Runnable runnable = this.mAnimationRunnable;
            if (runnable == null) {
                this.mAnimationRunnable = new Runnable() {
                    public void run() {
                        DrawableContainer.this.animate(DrawableContainer.DEFAULT_DITHER);
                        DrawableContainer.this.invalidateSelf();
                    }
                };
            } else {
                unscheduleSelf(runnable);
            }
            animate(DEFAULT_DITHER);
        }
        invalidateSelf();
        return DEFAULT_DITHER;
    }

    public void setAlpha(int alpha) {
        if (!this.mHasAlpha || this.mAlpha != alpha) {
            this.mHasAlpha = DEFAULT_DITHER;
            this.mAlpha = alpha;
            Drawable drawable = this.mCurrDrawable;
            if (drawable == null) {
                return;
            }
            if (this.mEnterAnimationEnd == 0) {
                drawable.setAlpha(alpha);
            } else {
                animate(false);
            }
        }
    }

    public void setAutoMirrored(boolean mirrored) {
        if (this.mDrawableContainerState.mAutoMirrored != mirrored) {
            this.mDrawableContainerState.mAutoMirrored = mirrored;
            Drawable drawable = this.mCurrDrawable;
            if (drawable != null) {
                DrawableCompat.setAutoMirrored(drawable, this.mDrawableContainerState.mAutoMirrored);
            }
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mDrawableContainerState.mHasColorFilter = DEFAULT_DITHER;
        if (this.mDrawableContainerState.mColorFilter != colorFilter) {
            this.mDrawableContainerState.mColorFilter = colorFilter;
            Drawable drawable = this.mCurrDrawable;
            if (drawable != null) {
                drawable.setColorFilter(colorFilter);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setConstantState(DrawableContainerState state) {
        this.mDrawableContainerState = state;
        int i = this.mCurIndex;
        if (i >= 0) {
            Drawable child = state.getChild(i);
            this.mCurrDrawable = child;
            if (child != null) {
                initializeDrawableForDisplay(child);
            }
        }
        this.mLastDrawable = null;
    }

    /* access modifiers changed from: package-private */
    public void setCurrentIndex(int index) {
        selectDrawable(index);
    }

    public void setDither(boolean dither) {
        if (this.mDrawableContainerState.mDither != dither) {
            this.mDrawableContainerState.mDither = dither;
            Drawable drawable = this.mCurrDrawable;
            if (drawable != null) {
                drawable.setDither(this.mDrawableContainerState.mDither);
            }
        }
    }

    public void setEnterFadeDuration(int ms) {
        this.mDrawableContainerState.mEnterFadeDuration = ms;
    }

    public void setExitFadeDuration(int ms) {
        this.mDrawableContainerState.mExitFadeDuration = ms;
    }

    public void setHotspot(float x, float y) {
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            DrawableCompat.setHotspot(drawable, x, y);
        }
    }

    public void setHotspotBounds(int left, int top, int right, int bottom) {
        Rect rect = this.mHotspotBounds;
        if (rect == null) {
            this.mHotspotBounds = new Rect(left, top, right, bottom);
        } else {
            rect.set(left, top, right, bottom);
        }
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            DrawableCompat.setHotspotBounds(drawable, left, top, right, bottom);
        }
    }

    public void setTint(int tintColor) {
        setTintList(ColorStateList.valueOf(tintColor));
    }

    public void setTintList(ColorStateList tint) {
        this.mDrawableContainerState.mHasTintList = DEFAULT_DITHER;
        if (this.mDrawableContainerState.mTintList != tint) {
            this.mDrawableContainerState.mTintList = tint;
            DrawableCompat.setTintList(this.mCurrDrawable, tint);
        }
    }

    public void setTintMode(PorterDuff.Mode tintMode) {
        this.mDrawableContainerState.mHasTintMode = DEFAULT_DITHER;
        if (this.mDrawableContainerState.mTintMode != tintMode) {
            this.mDrawableContainerState.mTintMode = tintMode;
            DrawableCompat.setTintMode(this.mCurrDrawable, tintMode);
        }
    }

    public boolean setVisible(boolean visible, boolean restart) {
        boolean visible2 = super.setVisible(visible, restart);
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            drawable.setVisible(visible, restart);
        }
        Drawable drawable2 = this.mCurrDrawable;
        if (drawable2 != null) {
            drawable2.setVisible(visible, restart);
        }
        return visible2;
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        if (who == this.mCurrDrawable && getCallback() != null) {
            getCallback().unscheduleDrawable(this, what);
        }
    }

    /* access modifiers changed from: package-private */
    public final void updateDensity(Resources res) {
        this.mDrawableContainerState.updateDensity(res);
    }
}
