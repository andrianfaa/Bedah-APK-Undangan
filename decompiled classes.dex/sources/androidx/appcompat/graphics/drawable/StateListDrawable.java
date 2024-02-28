package androidx.appcompat.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.StateSet;
import androidx.appcompat.graphics.drawable.DrawableContainer;
import androidx.appcompat.resources.Compatibility;
import androidx.appcompat.resources.R;
import androidx.appcompat.widget.ResourceManagerInternal;
import androidx.core.content.res.TypedArrayUtils;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class StateListDrawable extends DrawableContainer {
    private static final boolean DEBUG = false;
    private static final String TAG = "StateListDrawable";
    private boolean mMutated;
    private StateListState mStateListState;

    static class StateListState extends DrawableContainer.DrawableContainerState {
        int[][] mStateSets;

        StateListState(StateListState orig, StateListDrawable owner, Resources res) {
            super(orig, owner, res);
            if (orig != null) {
                this.mStateSets = orig.mStateSets;
            } else {
                this.mStateSets = new int[getCapacity()][];
            }
        }

        /* access modifiers changed from: package-private */
        public int addStateSet(int[] stateSet, Drawable drawable) {
            int addChild = addChild(drawable);
            this.mStateSets[addChild] = stateSet;
            return addChild;
        }

        public void growArray(int oldSize, int newSize) {
            super.growArray(oldSize, newSize);
            int[][] iArr = new int[newSize][];
            System.arraycopy(this.mStateSets, 0, iArr, 0, oldSize);
            this.mStateSets = iArr;
        }

        /* access modifiers changed from: package-private */
        public int indexOfStateSet(int[] stateSet) {
            int[][] iArr = this.mStateSets;
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (StateSet.stateSetMatches(iArr[i], stateSet)) {
                    return i;
                }
            }
            return -1;
        }

        /* access modifiers changed from: package-private */
        public void mutate() {
            int[][] iArr = this.mStateSets;
            int[][] iArr2 = new int[iArr.length][];
            for (int length = iArr.length - 1; length >= 0; length--) {
                int[] iArr3 = this.mStateSets[length];
                iArr2[length] = iArr3 != null ? (int[]) iArr3.clone() : null;
            }
            this.mStateSets = iArr2;
        }

        public Drawable newDrawable() {
            return new StateListDrawable(this, (Resources) null);
        }

        public Drawable newDrawable(Resources res) {
            return new StateListDrawable(this, res);
        }
    }

    StateListDrawable() {
        this((StateListState) null, (Resources) null);
    }

    StateListDrawable(StateListState state) {
        if (state != null) {
            setConstantState(state);
        }
    }

    StateListDrawable(StateListState state, Resources res) {
        setConstantState(new StateListState(state, this, res));
        onStateChange(getState());
    }

    private void inflateChildElements(Context context, Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        int i;
        AttributeSet attributeSet = attrs;
        StateListState stateListState = this.mStateListState;
        int i2 = 1;
        int depth = parser.getDepth() + 1;
        while (true) {
            int next = parser.next();
            int i3 = next;
            if (next != i2) {
                int depth2 = parser.getDepth();
                int i4 = depth2;
                if (depth2 < depth && i3 == 3) {
                    Context context2 = context;
                    Resources resources = r;
                    Resources.Theme theme2 = theme;
                    return;
                } else if (i3 == 2) {
                    if (i4 > depth) {
                        Context context3 = context;
                        Resources resources2 = r;
                        Resources.Theme theme3 = theme;
                        i2 = 1;
                    } else if (!parser.getName().equals("item")) {
                        continue;
                    } else {
                        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(r, theme, attributeSet, R.styleable.StateListDrawableItem);
                        Drawable drawable = null;
                        int resourceId = obtainAttributes.getResourceId(R.styleable.StateListDrawableItem_android_drawable, -1);
                        if (resourceId > 0) {
                            drawable = ResourceManagerInternal.get().getDrawable(context, resourceId);
                        } else {
                            Context context4 = context;
                        }
                        obtainAttributes.recycle();
                        int[] extractStateSet = extractStateSet(attributeSet);
                        if (drawable == null) {
                            while (true) {
                                int next2 = parser.next();
                                i = next2;
                                if (next2 != 4) {
                                    break;
                                }
                            }
                            if (i == 2) {
                                drawable = Build.VERSION.SDK_INT >= 21 ? Compatibility.Api21Impl.createFromXmlInner(r, parser, attrs, theme) : Drawable.createFromXmlInner(r, parser, attrs);
                            } else {
                                throw new XmlPullParserException(parser.getPositionDescription() + ": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
                            }
                        }
                        stateListState.addStateSet(extractStateSet, drawable);
                        i2 = 1;
                    }
                }
            } else {
                Context context5 = context;
                Resources resources3 = r;
                Resources.Theme theme4 = theme;
                return;
            }
        }
    }

    private void updateStateFromTypedArray(TypedArray a) {
        StateListState stateListState = this.mStateListState;
        if (Build.VERSION.SDK_INT >= 21) {
            stateListState.mChangingConfigurations |= Compatibility.Api21Impl.getChangingConfigurations(a);
        }
        stateListState.mVariablePadding = a.getBoolean(R.styleable.StateListDrawable_android_variablePadding, stateListState.mVariablePadding);
        stateListState.mConstantSize = a.getBoolean(R.styleable.StateListDrawable_android_constantSize, stateListState.mConstantSize);
        stateListState.mEnterFadeDuration = a.getInt(R.styleable.StateListDrawable_android_enterFadeDuration, stateListState.mEnterFadeDuration);
        stateListState.mExitFadeDuration = a.getInt(R.styleable.StateListDrawable_android_exitFadeDuration, stateListState.mExitFadeDuration);
        stateListState.mDither = a.getBoolean(R.styleable.StateListDrawable_android_dither, stateListState.mDither);
    }

    public void addState(int[] stateSet, Drawable drawable) {
        if (drawable != null) {
            this.mStateListState.addStateSet(stateSet, drawable);
            onStateChange(getState());
        }
    }

    public void applyTheme(Resources.Theme theme) {
        super.applyTheme(theme);
        onStateChange(getState());
    }

    /* access modifiers changed from: package-private */
    public void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    /* access modifiers changed from: package-private */
    public StateListState cloneConstantState() {
        return new StateListState(this.mStateListState, this, (Resources) null);
    }

    /* access modifiers changed from: package-private */
    public int[] extractStateSet(AttributeSet attrs) {
        int i = 0;
        int attributeCount = attrs.getAttributeCount();
        int[] iArr = new int[attributeCount];
        for (int i2 = 0; i2 < attributeCount; i2++) {
            int attributeNameResource = attrs.getAttributeNameResource(i2);
            switch (attributeNameResource) {
                case 0:
                case 16842960:
                case 16843161:
                    break;
                default:
                    int i3 = i + 1;
                    iArr[i] = attrs.getAttributeBooleanValue(i2, false) ? attributeNameResource : -attributeNameResource;
                    i = i3;
                    break;
            }
        }
        return StateSet.trimStateSet(iArr, i);
    }

    /* access modifiers changed from: package-private */
    public int getStateCount() {
        return this.mStateListState.getChildCount();
    }

    /* access modifiers changed from: package-private */
    public Drawable getStateDrawable(int index) {
        return this.mStateListState.getChild(index);
    }

    /* access modifiers changed from: package-private */
    public int getStateDrawableIndex(int[] stateSet) {
        return this.mStateListState.indexOfStateSet(stateSet);
    }

    /* access modifiers changed from: package-private */
    public StateListState getStateListState() {
        return this.mStateListState;
    }

    /* access modifiers changed from: package-private */
    public int[] getStateSet(int index) {
        return this.mStateListState.mStateSets[index];
    }

    public void inflate(Context context, Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(r, theme, attrs, R.styleable.StateListDrawable);
        setVisible(obtainAttributes.getBoolean(R.styleable.StateListDrawable_android_visible, true), true);
        updateStateFromTypedArray(obtainAttributes);
        updateDensity(r);
        obtainAttributes.recycle();
        inflateChildElements(context, r, parser, attrs, theme);
        onStateChange(getState());
    }

    public boolean isStateful() {
        return true;
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mStateListState.mutate();
            this.mMutated = true;
        }
        return this;
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] stateSet) {
        boolean onStateChange = super.onStateChange(stateSet);
        int indexOfStateSet = this.mStateListState.indexOfStateSet(stateSet);
        if (indexOfStateSet < 0) {
            indexOfStateSet = this.mStateListState.indexOfStateSet(StateSet.WILD_CARD);
        }
        return selectDrawable(indexOfStateSet) || onStateChange;
    }

    /* access modifiers changed from: package-private */
    public void setConstantState(DrawableContainer.DrawableContainerState state) {
        super.setConstantState(state);
        if (state instanceof StateListState) {
            this.mStateListState = (StateListState) state;
        }
    }
}
