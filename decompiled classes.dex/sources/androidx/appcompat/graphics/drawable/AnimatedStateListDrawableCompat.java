package androidx.appcompat.graphics.drawable;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.StateSet;
import androidx.appcompat.graphics.drawable.DrawableContainer;
import androidx.appcompat.graphics.drawable.StateListDrawable;
import androidx.appcompat.resources.Compatibility;
import androidx.appcompat.resources.R;
import androidx.appcompat.widget.ResourceManagerInternal;
import androidx.collection.LongSparseArray;
import androidx.collection.SparseArrayCompat;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.drawable.TintAwareDrawable;
import androidx.core.util.ObjectsCompat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedStateListDrawableCompat extends StateListDrawable implements TintAwareDrawable {
    private static final String ELEMENT_ITEM = "item";
    private static final String ELEMENT_TRANSITION = "transition";
    private static final String ITEM_MISSING_DRAWABLE_ERROR = ": <item> tag requires a 'drawable' attribute or child tag defining a drawable";
    private static final String LOGTAG = AnimatedStateListDrawableCompat.class.getSimpleName();
    private static final String TRANSITION_MISSING_DRAWABLE_ERROR = ": <transition> tag requires a 'drawable' attribute or child tag defining a drawable";
    private static final String TRANSITION_MISSING_FROM_TO_ID = ": <transition> tag requires 'fromId' & 'toId' attributes";
    private boolean mMutated;
    private AnimatedStateListState mState;
    private Transition mTransition;
    private int mTransitionFromIndex;
    private int mTransitionToIndex;

    private static class AnimatableTransition extends Transition {
        private final Animatable mA;

        AnimatableTransition(Animatable a) {
            super();
            this.mA = a;
        }

        public void start() {
            this.mA.start();
        }

        public void stop() {
            this.mA.stop();
        }
    }

    static class AnimatedStateListState extends StateListDrawable.StateListState {
        private static final long REVERSED_BIT = 4294967296L;
        private static final long REVERSIBLE_FLAG_BIT = 8589934592L;
        SparseArrayCompat<Integer> mStateIds;
        LongSparseArray<Long> mTransitions;

        AnimatedStateListState(AnimatedStateListState orig, AnimatedStateListDrawableCompat owner, Resources res) {
            super(orig, owner, res);
            if (orig != null) {
                this.mTransitions = orig.mTransitions;
                this.mStateIds = orig.mStateIds;
                return;
            }
            this.mTransitions = new LongSparseArray<>();
            this.mStateIds = new SparseArrayCompat<>();
        }

        private static long generateTransitionKey(int fromId, int toId) {
            return (((long) fromId) << 32) | ((long) toId);
        }

        /* access modifiers changed from: package-private */
        public int addStateSet(int[] stateSet, Drawable drawable, int id) {
            int addStateSet = super.addStateSet(stateSet, drawable);
            this.mStateIds.put(addStateSet, Integer.valueOf(id));
            return addStateSet;
        }

        /* access modifiers changed from: package-private */
        public int addTransition(int fromId, int toId, Drawable anim, boolean reversible) {
            int addChild = super.addChild(anim);
            long generateTransitionKey = generateTransitionKey(fromId, toId);
            long j = 0;
            if (reversible) {
                j = REVERSIBLE_FLAG_BIT;
            }
            this.mTransitions.append(generateTransitionKey, Long.valueOf(((long) addChild) | j));
            if (reversible) {
                this.mTransitions.append(generateTransitionKey(toId, fromId), Long.valueOf(((long) addChild) | REVERSED_BIT | j));
            } else {
                int i = fromId;
                int i2 = toId;
            }
            return addChild;
        }

        /* access modifiers changed from: package-private */
        public int getKeyframeIdAt(int index) {
            if (index < 0) {
                return 0;
            }
            return this.mStateIds.get(index, 0).intValue();
        }

        /* access modifiers changed from: package-private */
        public int indexOfKeyframe(int[] stateSet) {
            int indexOfStateSet = super.indexOfStateSet(stateSet);
            return indexOfStateSet >= 0 ? indexOfStateSet : super.indexOfStateSet(StateSet.WILD_CARD);
        }

        /* access modifiers changed from: package-private */
        public int indexOfTransition(int fromId, int toId) {
            return (int) this.mTransitions.get(generateTransitionKey(fromId, toId), -1L).longValue();
        }

        /* access modifiers changed from: package-private */
        public boolean isTransitionReversed(int fromId, int toId) {
            return (this.mTransitions.get(generateTransitionKey(fromId, toId), -1L).longValue() & REVERSED_BIT) != 0;
        }

        /* access modifiers changed from: package-private */
        public void mutate() {
            this.mTransitions = this.mTransitions.clone();
            this.mStateIds = this.mStateIds.clone();
        }

        public Drawable newDrawable() {
            return new AnimatedStateListDrawableCompat(this, (Resources) null);
        }

        public Drawable newDrawable(Resources res) {
            return new AnimatedStateListDrawableCompat(this, res);
        }

        /* access modifiers changed from: package-private */
        public boolean transitionHasReversibleFlag(int fromId, int toId) {
            return (this.mTransitions.get(generateTransitionKey(fromId, toId), -1L).longValue() & REVERSIBLE_FLAG_BIT) != 0;
        }
    }

    private static class AnimatedVectorDrawableTransition extends Transition {
        private final AnimatedVectorDrawableCompat mAvd;

        AnimatedVectorDrawableTransition(AnimatedVectorDrawableCompat avd) {
            super();
            this.mAvd = avd;
        }

        public void start() {
            this.mAvd.start();
        }

        public void stop() {
            this.mAvd.stop();
        }
    }

    private static class AnimationDrawableTransition extends Transition {
        private final ObjectAnimator mAnim;
        private final boolean mHasReversibleFlag;

        AnimationDrawableTransition(AnimationDrawable ad, boolean reversed, boolean hasReversibleFlag) {
            super();
            int numberOfFrames = ad.getNumberOfFrames();
            int i = reversed ? numberOfFrames - 1 : 0;
            int i2 = reversed ? 0 : numberOfFrames - 1;
            FrameInterpolator frameInterpolator = new FrameInterpolator(ad, reversed);
            ObjectAnimator ofInt = ObjectAnimator.ofInt(ad, "currentIndex", new int[]{i, i2});
            if (Build.VERSION.SDK_INT >= 18) {
                Compatibility.Api18Impl.setAutoCancel(ofInt, true);
            }
            ofInt.setDuration((long) frameInterpolator.getTotalDuration());
            ofInt.setInterpolator(frameInterpolator);
            this.mHasReversibleFlag = hasReversibleFlag;
            this.mAnim = ofInt;
        }

        public boolean canReverse() {
            return this.mHasReversibleFlag;
        }

        public void reverse() {
            this.mAnim.reverse();
        }

        public void start() {
            this.mAnim.start();
        }

        public void stop() {
            this.mAnim.cancel();
        }
    }

    private static class FrameInterpolator implements TimeInterpolator {
        private int[] mFrameTimes;
        private int mFrames;
        private int mTotalDuration;

        FrameInterpolator(AnimationDrawable d, boolean reversed) {
            updateFrames(d, reversed);
        }

        public float getInterpolation(float input) {
            int i = this.mFrames;
            int[] iArr = this.mFrameTimes;
            int i2 = (int) ((((float) this.mTotalDuration) * input) + 0.5f);
            int i3 = 0;
            while (i3 < i && i2 >= iArr[i3]) {
                i2 -= iArr[i3];
                i3++;
            }
            return (((float) i3) / ((float) i)) + (i3 < i ? ((float) i2) / ((float) this.mTotalDuration) : 0.0f);
        }

        /* access modifiers changed from: package-private */
        public int getTotalDuration() {
            return this.mTotalDuration;
        }

        /* access modifiers changed from: package-private */
        public int updateFrames(AnimationDrawable d, boolean reversed) {
            int numberOfFrames = d.getNumberOfFrames();
            this.mFrames = numberOfFrames;
            int[] iArr = this.mFrameTimes;
            if (iArr == null || iArr.length < numberOfFrames) {
                this.mFrameTimes = new int[numberOfFrames];
            }
            int[] iArr2 = this.mFrameTimes;
            int i = 0;
            for (int i2 = 0; i2 < numberOfFrames; i2++) {
                int duration = d.getDuration(reversed ? (numberOfFrames - i2) - 1 : i2);
                iArr2[i2] = duration;
                i += duration;
            }
            this.mTotalDuration = i;
            return i;
        }
    }

    private static abstract class Transition {
        private Transition() {
        }

        public boolean canReverse() {
            return false;
        }

        public void reverse() {
        }

        public abstract void start();

        public abstract void stop();
    }

    public AnimatedStateListDrawableCompat() {
        this((AnimatedStateListState) null, (Resources) null);
    }

    AnimatedStateListDrawableCompat(AnimatedStateListState state, Resources res) {
        super((StateListDrawable.StateListState) null);
        this.mTransitionToIndex = -1;
        this.mTransitionFromIndex = -1;
        setConstantState(new AnimatedStateListState(state, this, res));
        onStateChange(getState());
        jumpToCurrentState();
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0021 A[Catch:{ XmlPullParserException -> 0x0030, IOException -> 0x0029 }] */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x001c A[Catch:{ XmlPullParserException -> 0x0030, IOException -> 0x0029 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static androidx.appcompat.graphics.drawable.AnimatedStateListDrawableCompat create(android.content.Context r7, int r8, android.content.res.Resources.Theme r9) {
        /*
            java.lang.String r0 = "parser error"
            android.content.res.Resources r1 = r7.getResources()     // Catch:{ XmlPullParserException -> 0x0030, IOException -> 0x0029 }
            android.content.res.XmlResourceParser r2 = r1.getXml(r8)     // Catch:{ XmlPullParserException -> 0x0030, IOException -> 0x0029 }
            android.util.AttributeSet r3 = android.util.Xml.asAttributeSet(r2)     // Catch:{ XmlPullParserException -> 0x0030, IOException -> 0x0029 }
        L_0x000e:
            int r4 = r2.next()     // Catch:{ XmlPullParserException -> 0x0030, IOException -> 0x0029 }
            r5 = r4
            r6 = 2
            if (r4 == r6) goto L_0x001a
            r4 = 1
            if (r5 == r4) goto L_0x001a
            goto L_0x000e
        L_0x001a:
            if (r5 != r6) goto L_0x0021
            androidx.appcompat.graphics.drawable.AnimatedStateListDrawableCompat r0 = createFromXmlInner(r7, r1, r2, r3, r9)     // Catch:{ XmlPullParserException -> 0x0030, IOException -> 0x0029 }
            return r0
        L_0x0021:
            org.xmlpull.v1.XmlPullParserException r4 = new org.xmlpull.v1.XmlPullParserException     // Catch:{ XmlPullParserException -> 0x0030, IOException -> 0x0029 }
            java.lang.String r6 = "No start tag found"
            r4.<init>(r6)     // Catch:{ XmlPullParserException -> 0x0030, IOException -> 0x0029 }
            throw r4     // Catch:{ XmlPullParserException -> 0x0030, IOException -> 0x0029 }
        L_0x0029:
            r1 = move-exception
            java.lang.String r2 = LOGTAG
            android.util.Log.e(r2, r0, r1)
            goto L_0x0037
        L_0x0030:
            r1 = move-exception
            java.lang.String r2 = LOGTAG
            android.util.Log.e(r2, r0, r1)
        L_0x0037:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.graphics.drawable.AnimatedStateListDrawableCompat.create(android.content.Context, int, android.content.res.Resources$Theme):androidx.appcompat.graphics.drawable.AnimatedStateListDrawableCompat");
    }

    public static AnimatedStateListDrawableCompat createFromXmlInner(Context context, Resources resources, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws IOException, XmlPullParserException {
        String name = parser.getName();
        if (name.equals("animated-selector")) {
            AnimatedStateListDrawableCompat animatedStateListDrawableCompat = new AnimatedStateListDrawableCompat();
            animatedStateListDrawableCompat.inflate(context, resources, parser, attrs, theme);
            return animatedStateListDrawableCompat;
        }
        throw new XmlPullParserException(parser.getPositionDescription() + ": invalid animated-selector tag " + name);
    }

    private void inflateChildElements(Context context, Resources resources, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        int depth = parser.getDepth() + 1;
        while (true) {
            int next = parser.next();
            int i = next;
            if (next != 1) {
                int depth2 = parser.getDepth();
                int i2 = depth2;
                if (depth2 < depth && i == 3) {
                    return;
                }
                if (i == 2 && i2 <= depth) {
                    if (parser.getName().equals(ELEMENT_ITEM)) {
                        parseItem(context, resources, parser, attrs, theme);
                    } else if (parser.getName().equals(ELEMENT_TRANSITION)) {
                        parseTransition(context, resources, parser, attrs, theme);
                    }
                }
            } else {
                return;
            }
        }
    }

    private void init() {
        onStateChange(getState());
    }

    private int parseItem(Context context, Resources resources, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        int next;
        int i;
        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attrs, R.styleable.AnimatedStateListDrawableItem);
        int resourceId = obtainAttributes.getResourceId(R.styleable.AnimatedStateListDrawableItem_android_id, 0);
        Drawable drawable = null;
        int resourceId2 = obtainAttributes.getResourceId(R.styleable.AnimatedStateListDrawableItem_android_drawable, -1);
        if (resourceId2 > 0) {
            drawable = ResourceManagerInternal.get().getDrawable(context, resourceId2);
        }
        obtainAttributes.recycle();
        int[] extractStateSet = extractStateSet(attrs);
        if (drawable == null) {
            do {
                next = parser.next();
                i = next;
            } while (next == 4);
            if (i == 2) {
                drawable = parser.getName().equals("vector") ? VectorDrawableCompat.createFromXmlInner(resources, parser, attrs, theme) : Build.VERSION.SDK_INT >= 21 ? Compatibility.Api21Impl.createFromXmlInner(resources, parser, attrs, theme) : Drawable.createFromXmlInner(resources, parser, attrs);
            } else {
                throw new XmlPullParserException(parser.getPositionDescription() + ITEM_MISSING_DRAWABLE_ERROR);
            }
        }
        if (drawable != null) {
            return this.mState.addStateSet(extractStateSet, drawable, resourceId);
        }
        throw new XmlPullParserException(parser.getPositionDescription() + ITEM_MISSING_DRAWABLE_ERROR);
    }

    private int parseTransition(Context context, Resources resources, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        int next;
        int i;
        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attrs, R.styleable.AnimatedStateListDrawableTransition);
        int resourceId = obtainAttributes.getResourceId(R.styleable.AnimatedStateListDrawableTransition_android_fromId, -1);
        int resourceId2 = obtainAttributes.getResourceId(R.styleable.AnimatedStateListDrawableTransition_android_toId, -1);
        Drawable drawable = null;
        int resourceId3 = obtainAttributes.getResourceId(R.styleable.AnimatedStateListDrawableTransition_android_drawable, -1);
        if (resourceId3 > 0) {
            drawable = ResourceManagerInternal.get().getDrawable(context, resourceId3);
        } else {
            Context context2 = context;
        }
        boolean z = obtainAttributes.getBoolean(R.styleable.AnimatedStateListDrawableTransition_android_reversible, false);
        obtainAttributes.recycle();
        if (drawable == null) {
            do {
                next = parser.next();
                i = next;
            } while (next == 4);
            if (i == 2) {
                drawable = parser.getName().equals("animated-vector") ? AnimatedVectorDrawableCompat.createFromXmlInner(context, resources, parser, attrs, theme) : Build.VERSION.SDK_INT >= 21 ? Compatibility.Api21Impl.createFromXmlInner(resources, parser, attrs, theme) : Drawable.createFromXmlInner(resources, parser, attrs);
            } else {
                throw new XmlPullParserException(parser.getPositionDescription() + TRANSITION_MISSING_DRAWABLE_ERROR);
            }
        }
        if (drawable == null) {
            throw new XmlPullParserException(parser.getPositionDescription() + TRANSITION_MISSING_DRAWABLE_ERROR);
        } else if (resourceId != -1 && resourceId2 != -1) {
            return this.mState.addTransition(resourceId, resourceId2, drawable, z);
        } else {
            throw new XmlPullParserException(parser.getPositionDescription() + TRANSITION_MISSING_FROM_TO_ID);
        }
    }

    private boolean selectTransition(int toIndex) {
        int i;
        int indexOfTransition;
        Transition transition;
        Transition transition2 = this.mTransition;
        if (transition2 == null) {
            i = getCurrentIndex();
        } else if (toIndex == this.mTransitionToIndex) {
            return true;
        } else {
            if (toIndex != this.mTransitionFromIndex || !transition2.canReverse()) {
                i = this.mTransitionToIndex;
                transition2.stop();
            } else {
                transition2.reverse();
                this.mTransitionToIndex = this.mTransitionFromIndex;
                this.mTransitionFromIndex = toIndex;
                return true;
            }
        }
        this.mTransition = null;
        this.mTransitionFromIndex = -1;
        this.mTransitionToIndex = -1;
        AnimatedStateListState animatedStateListState = this.mState;
        int keyframeIdAt = animatedStateListState.getKeyframeIdAt(i);
        int keyframeIdAt2 = animatedStateListState.getKeyframeIdAt(toIndex);
        if (keyframeIdAt2 == 0 || keyframeIdAt == 0 || (indexOfTransition = animatedStateListState.indexOfTransition(keyframeIdAt, keyframeIdAt2)) < 0) {
            return false;
        }
        boolean transitionHasReversibleFlag = animatedStateListState.transitionHasReversibleFlag(keyframeIdAt, keyframeIdAt2);
        selectDrawable(indexOfTransition);
        Drawable current = getCurrent();
        if (current instanceof AnimationDrawable) {
            transition = new AnimationDrawableTransition((AnimationDrawable) current, animatedStateListState.isTransitionReversed(keyframeIdAt, keyframeIdAt2), transitionHasReversibleFlag);
        } else if (current instanceof AnimatedVectorDrawableCompat) {
            transition = new AnimatedVectorDrawableTransition((AnimatedVectorDrawableCompat) current);
        } else if (!(current instanceof Animatable)) {
            return false;
        } else {
            transition = new AnimatableTransition((Animatable) current);
        }
        transition.start();
        this.mTransition = transition;
        this.mTransitionFromIndex = i;
        this.mTransitionToIndex = toIndex;
        return true;
    }

    private void updateStateFromTypedArray(TypedArray a) {
        AnimatedStateListState animatedStateListState = this.mState;
        if (Build.VERSION.SDK_INT >= 21) {
            animatedStateListState.mChangingConfigurations |= Compatibility.Api21Impl.getChangingConfigurations(a);
        }
        animatedStateListState.setVariablePadding(a.getBoolean(R.styleable.AnimatedStateListDrawableCompat_android_variablePadding, animatedStateListState.mVariablePadding));
        animatedStateListState.setConstantSize(a.getBoolean(R.styleable.AnimatedStateListDrawableCompat_android_constantSize, animatedStateListState.mConstantSize));
        animatedStateListState.setEnterFadeDuration(a.getInt(R.styleable.AnimatedStateListDrawableCompat_android_enterFadeDuration, animatedStateListState.mEnterFadeDuration));
        animatedStateListState.setExitFadeDuration(a.getInt(R.styleable.AnimatedStateListDrawableCompat_android_exitFadeDuration, animatedStateListState.mExitFadeDuration));
        setDither(a.getBoolean(R.styleable.AnimatedStateListDrawableCompat_android_dither, animatedStateListState.mDither));
    }

    public /* bridge */ /* synthetic */ void addState(int[] iArr, Drawable drawable) {
        super.addState(iArr, drawable);
    }

    public void addState(int[] stateSet, Drawable drawable, int id) {
        ObjectsCompat.requireNonNull(drawable);
        this.mState.addStateSet(stateSet, drawable, id);
        onStateChange(getState());
    }

    public <T extends Drawable & Animatable> void addTransition(int fromId, int toId, T t, boolean reversible) {
        ObjectsCompat.requireNonNull(t);
        this.mState.addTransition(fromId, toId, t, reversible);
    }

    public /* bridge */ /* synthetic */ void applyTheme(Resources.Theme theme) {
        super.applyTheme(theme);
    }

    public /* bridge */ /* synthetic */ boolean canApplyTheme() {
        return super.canApplyTheme();
    }

    /* access modifiers changed from: package-private */
    public void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    /* access modifiers changed from: package-private */
    public AnimatedStateListState cloneConstantState() {
        return new AnimatedStateListState(this.mState, this, (Resources) null);
    }

    public /* bridge */ /* synthetic */ void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public /* bridge */ /* synthetic */ int getAlpha() {
        return super.getAlpha();
    }

    public /* bridge */ /* synthetic */ int getChangingConfigurations() {
        return super.getChangingConfigurations();
    }

    public /* bridge */ /* synthetic */ Drawable getCurrent() {
        return super.getCurrent();
    }

    public /* bridge */ /* synthetic */ void getHotspotBounds(Rect rect) {
        super.getHotspotBounds(rect);
    }

    public /* bridge */ /* synthetic */ int getIntrinsicHeight() {
        return super.getIntrinsicHeight();
    }

    public /* bridge */ /* synthetic */ int getIntrinsicWidth() {
        return super.getIntrinsicWidth();
    }

    public /* bridge */ /* synthetic */ int getMinimumHeight() {
        return super.getMinimumHeight();
    }

    public /* bridge */ /* synthetic */ int getMinimumWidth() {
        return super.getMinimumWidth();
    }

    public /* bridge */ /* synthetic */ int getOpacity() {
        return super.getOpacity();
    }

    public /* bridge */ /* synthetic */ void getOutline(Outline outline) {
        super.getOutline(outline);
    }

    public /* bridge */ /* synthetic */ boolean getPadding(Rect rect) {
        return super.getPadding(rect);
    }

    public void inflate(Context context, Resources resources, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attrs, R.styleable.AnimatedStateListDrawableCompat);
        setVisible(obtainAttributes.getBoolean(R.styleable.AnimatedStateListDrawableCompat_android_visible, true), true);
        updateStateFromTypedArray(obtainAttributes);
        updateDensity(resources);
        obtainAttributes.recycle();
        inflateChildElements(context, resources, parser, attrs, theme);
        init();
    }

    public /* bridge */ /* synthetic */ void invalidateDrawable(Drawable drawable) {
        super.invalidateDrawable(drawable);
    }

    public /* bridge */ /* synthetic */ boolean isAutoMirrored() {
        return super.isAutoMirrored();
    }

    public boolean isStateful() {
        return true;
    }

    public void jumpToCurrentState() {
        super.jumpToCurrentState();
        Transition transition = this.mTransition;
        if (transition != null) {
            transition.stop();
            this.mTransition = null;
            selectDrawable(this.mTransitionToIndex);
            this.mTransitionToIndex = -1;
            this.mTransitionFromIndex = -1;
        }
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mState.mutate();
            this.mMutated = true;
        }
        return this;
    }

    public /* bridge */ /* synthetic */ boolean onLayoutDirectionChanged(int i) {
        return super.onLayoutDirectionChanged(i);
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] stateSet) {
        int indexOfKeyframe = this.mState.indexOfKeyframe(stateSet);
        boolean z = indexOfKeyframe != getCurrentIndex() && (selectTransition(indexOfKeyframe) || selectDrawable(indexOfKeyframe));
        Drawable current = getCurrent();
        return current != null ? z | current.setState(stateSet) : z;
    }

    public /* bridge */ /* synthetic */ void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
        super.scheduleDrawable(drawable, runnable, j);
    }

    public /* bridge */ /* synthetic */ void setAlpha(int i) {
        super.setAlpha(i);
    }

    public /* bridge */ /* synthetic */ void setAutoMirrored(boolean z) {
        super.setAutoMirrored(z);
    }

    public /* bridge */ /* synthetic */ void setColorFilter(ColorFilter colorFilter) {
        super.setColorFilter(colorFilter);
    }

    /* access modifiers changed from: package-private */
    public void setConstantState(DrawableContainer.DrawableContainerState state) {
        super.setConstantState(state);
        if (state instanceof AnimatedStateListState) {
            this.mState = (AnimatedStateListState) state;
        }
    }

    public /* bridge */ /* synthetic */ void setDither(boolean z) {
        super.setDither(z);
    }

    public /* bridge */ /* synthetic */ void setEnterFadeDuration(int i) {
        super.setEnterFadeDuration(i);
    }

    public /* bridge */ /* synthetic */ void setExitFadeDuration(int i) {
        super.setExitFadeDuration(i);
    }

    public /* bridge */ /* synthetic */ void setHotspot(float f, float f2) {
        super.setHotspot(f, f2);
    }

    public /* bridge */ /* synthetic */ void setHotspotBounds(int i, int i2, int i3, int i4) {
        super.setHotspotBounds(i, i2, i3, i4);
    }

    public /* bridge */ /* synthetic */ void setTint(int i) {
        super.setTint(i);
    }

    public /* bridge */ /* synthetic */ void setTintList(ColorStateList colorStateList) {
        super.setTintList(colorStateList);
    }

    public /* bridge */ /* synthetic */ void setTintMode(PorterDuff.Mode mode) {
        super.setTintMode(mode);
    }

    public boolean setVisible(boolean visible, boolean restart) {
        boolean visible2 = super.setVisible(visible, restart);
        Transition transition = this.mTransition;
        if (transition != null && (visible2 || restart)) {
            if (visible) {
                transition.start();
            } else {
                jumpToCurrentState();
            }
        }
        return visible2;
    }

    public /* bridge */ /* synthetic */ void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        super.unscheduleDrawable(drawable, runnable);
    }
}
