package androidx.constraintlayout.core.state;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.core.state.State;
import androidx.constraintlayout.core.state.helpers.Facade;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import java.util.ArrayList;
import java.util.HashMap;

public class ConstraintReference implements Reference {
    private Object key;
    float mAlpha = Float.NaN;
    Object mBaselineToBaseline = null;
    Object mBaselineToBottom = null;
    Object mBaselineToTop = null;
    protected Object mBottomToBottom = null;
    protected Object mBottomToTop = null;
    private float mCircularAngle;
    Object mCircularConstraint = null;
    private float mCircularDistance;
    private ConstraintWidget mConstraintWidget;
    private HashMap<String, Integer> mCustomColors = new HashMap<>();
    private HashMap<String, Float> mCustomFloats = new HashMap<>();
    protected Object mEndToEnd = null;
    protected Object mEndToStart = null;
    Facade mFacade = null;
    float mHorizontalBias = 0.5f;
    int mHorizontalChainStyle = 0;
    float mHorizontalChainWeight = -1.0f;
    Dimension mHorizontalDimension = Dimension.Fixed(Dimension.WRAP_DIMENSION);
    State.Constraint mLast = null;
    protected Object mLeftToLeft = null;
    protected Object mLeftToRight = null;
    int mMarginBaseline = 0;
    int mMarginBaselineGone = 0;
    protected int mMarginBottom = 0;
    protected int mMarginBottomGone = 0;
    protected int mMarginEnd = 0;
    protected int mMarginEndGone = 0;
    protected int mMarginLeft = 0;
    protected int mMarginLeftGone = 0;
    protected int mMarginRight = 0;
    protected int mMarginRightGone = 0;
    protected int mMarginStart = 0;
    protected int mMarginStartGone = 0;
    protected int mMarginTop = 0;
    protected int mMarginTopGone = 0;
    float mPivotX = Float.NaN;
    float mPivotY = Float.NaN;
    protected Object mRightToLeft = null;
    protected Object mRightToRight = null;
    float mRotationX = Float.NaN;
    float mRotationY = Float.NaN;
    float mRotationZ = Float.NaN;
    float mScaleX = Float.NaN;
    float mScaleY = Float.NaN;
    protected Object mStartToEnd = null;
    protected Object mStartToStart = null;
    final State mState;
    String mTag = null;
    protected Object mTopToBottom = null;
    protected Object mTopToTop = null;
    float mTranslationX = Float.NaN;
    float mTranslationY = Float.NaN;
    float mTranslationZ = Float.NaN;
    float mVerticalBias = 0.5f;
    int mVerticalChainStyle = 0;
    float mVerticalChainWeight = -1.0f;
    Dimension mVerticalDimension = Dimension.Fixed(Dimension.WRAP_DIMENSION);
    private Object mView;
    int mVisibility = 0;

    /* renamed from: androidx.constraintlayout.core.state.ConstraintReference$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$core$state$State$Constraint;

        static {
            int[] iArr = new int[State.Constraint.values().length];
            $SwitchMap$androidx$constraintlayout$core$state$State$Constraint = iArr;
            try {
                iArr[State.Constraint.LEFT_TO_LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Constraint[State.Constraint.LEFT_TO_RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Constraint[State.Constraint.RIGHT_TO_LEFT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Constraint[State.Constraint.RIGHT_TO_RIGHT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Constraint[State.Constraint.START_TO_START.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Constraint[State.Constraint.START_TO_END.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Constraint[State.Constraint.END_TO_START.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Constraint[State.Constraint.END_TO_END.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Constraint[State.Constraint.TOP_TO_TOP.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Constraint[State.Constraint.TOP_TO_BOTTOM.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Constraint[State.Constraint.BOTTOM_TO_TOP.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Constraint[State.Constraint.BOTTOM_TO_BOTTOM.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Constraint[State.Constraint.BASELINE_TO_BOTTOM.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Constraint[State.Constraint.BASELINE_TO_TOP.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Constraint[State.Constraint.BASELINE_TO_BASELINE.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Constraint[State.Constraint.CIRCULAR_CONSTRAINT.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Constraint[State.Constraint.CENTER_HORIZONTALLY.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Constraint[State.Constraint.CENTER_VERTICALLY.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
        }
    }

    public interface ConstraintReferenceFactory {
        ConstraintReference create(State state);
    }

    static class IncorrectConstraintException extends Exception {
        private final ArrayList<String> mErrors;

        public IncorrectConstraintException(ArrayList<String> arrayList) {
            this.mErrors = arrayList;
        }

        public ArrayList<String> getErrors() {
            return this.mErrors;
        }

        public String toString() {
            return "IncorrectConstraintException: " + this.mErrors.toString();
        }
    }

    public ConstraintReference(State state) {
        this.mState = state;
    }

    private void applyConnection(ConstraintWidget widget, Object opaqueTarget, State.Constraint type) {
        ConstraintWidget target = getTarget(opaqueTarget);
        if (target != null) {
            int i = AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$state$State$Constraint[type.ordinal()];
            switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$state$State$Constraint[type.ordinal()]) {
                case 1:
                    widget.getAnchor(ConstraintAnchor.Type.LEFT).connect(target.getAnchor(ConstraintAnchor.Type.LEFT), this.mMarginLeft, this.mMarginLeftGone, false);
                    return;
                case 2:
                    widget.getAnchor(ConstraintAnchor.Type.LEFT).connect(target.getAnchor(ConstraintAnchor.Type.RIGHT), this.mMarginLeft, this.mMarginLeftGone, false);
                    return;
                case 3:
                    widget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(target.getAnchor(ConstraintAnchor.Type.LEFT), this.mMarginRight, this.mMarginRightGone, false);
                    return;
                case 4:
                    widget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(target.getAnchor(ConstraintAnchor.Type.RIGHT), this.mMarginRight, this.mMarginRightGone, false);
                    return;
                case 5:
                    widget.getAnchor(ConstraintAnchor.Type.LEFT).connect(target.getAnchor(ConstraintAnchor.Type.LEFT), this.mMarginStart, this.mMarginStartGone, false);
                    return;
                case 6:
                    widget.getAnchor(ConstraintAnchor.Type.LEFT).connect(target.getAnchor(ConstraintAnchor.Type.RIGHT), this.mMarginStart, this.mMarginStartGone, false);
                    return;
                case 7:
                    widget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(target.getAnchor(ConstraintAnchor.Type.LEFT), this.mMarginEnd, this.mMarginEndGone, false);
                    return;
                case 8:
                    widget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(target.getAnchor(ConstraintAnchor.Type.RIGHT), this.mMarginEnd, this.mMarginEndGone, false);
                    return;
                case 9:
                    widget.getAnchor(ConstraintAnchor.Type.TOP).connect(target.getAnchor(ConstraintAnchor.Type.TOP), this.mMarginTop, this.mMarginTopGone, false);
                    return;
                case 10:
                    widget.getAnchor(ConstraintAnchor.Type.TOP).connect(target.getAnchor(ConstraintAnchor.Type.BOTTOM), this.mMarginTop, this.mMarginTopGone, false);
                    return;
                case 11:
                    widget.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(target.getAnchor(ConstraintAnchor.Type.TOP), this.mMarginBottom, this.mMarginBottomGone, false);
                    return;
                case 12:
                    widget.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(target.getAnchor(ConstraintAnchor.Type.BOTTOM), this.mMarginBottom, this.mMarginBottomGone, false);
                    return;
                case 13:
                    widget.immediateConnect(ConstraintAnchor.Type.BASELINE, target, ConstraintAnchor.Type.BOTTOM, this.mMarginBaseline, this.mMarginBaselineGone);
                    return;
                case 14:
                    widget.immediateConnect(ConstraintAnchor.Type.BASELINE, target, ConstraintAnchor.Type.TOP, this.mMarginBaseline, this.mMarginBaselineGone);
                    return;
                case 15:
                    widget.immediateConnect(ConstraintAnchor.Type.BASELINE, target, ConstraintAnchor.Type.BASELINE, this.mMarginBaseline, this.mMarginBaselineGone);
                    return;
                case 16:
                    widget.connectCircularConstraint(target, this.mCircularAngle, (int) this.mCircularDistance);
                    return;
                default:
                    return;
            }
        }
    }

    private void dereference() {
        this.mLeftToLeft = get(this.mLeftToLeft);
        this.mLeftToRight = get(this.mLeftToRight);
        this.mRightToLeft = get(this.mRightToLeft);
        this.mRightToRight = get(this.mRightToRight);
        this.mStartToStart = get(this.mStartToStart);
        this.mStartToEnd = get(this.mStartToEnd);
        this.mEndToStart = get(this.mEndToStart);
        this.mEndToEnd = get(this.mEndToEnd);
        this.mTopToTop = get(this.mTopToTop);
        this.mTopToBottom = get(this.mTopToBottom);
        this.mBottomToTop = get(this.mBottomToTop);
        this.mBottomToBottom = get(this.mBottomToBottom);
        this.mBaselineToBaseline = get(this.mBaselineToBaseline);
        this.mBaselineToTop = get(this.mBaselineToTop);
        this.mBaselineToBottom = get(this.mBaselineToBottom);
    }

    private Object get(Object reference) {
        if (reference == null) {
            return null;
        }
        return !(reference instanceof ConstraintReference) ? this.mState.reference(reference) : reference;
    }

    private ConstraintWidget getTarget(Object target) {
        if (target instanceof Reference) {
            return ((Reference) target).getConstraintWidget();
        }
        return null;
    }

    public void addCustomColor(String name, int color) {
        this.mCustomColors.put(name, Integer.valueOf(color));
    }

    public void addCustomFloat(String name, float value) {
        if (this.mCustomFloats == null) {
            this.mCustomFloats = new HashMap<>();
        }
        this.mCustomFloats.put(name, Float.valueOf(value));
    }

    public ConstraintReference alpha(float alpha) {
        this.mAlpha = alpha;
        return this;
    }

    public void apply() {
        if (this.mConstraintWidget != null) {
            Facade facade = this.mFacade;
            if (facade != null) {
                facade.apply();
            }
            this.mHorizontalDimension.apply(this.mState, this.mConstraintWidget, 0);
            this.mVerticalDimension.apply(this.mState, this.mConstraintWidget, 1);
            dereference();
            applyConnection(this.mConstraintWidget, this.mLeftToLeft, State.Constraint.LEFT_TO_LEFT);
            applyConnection(this.mConstraintWidget, this.mLeftToRight, State.Constraint.LEFT_TO_RIGHT);
            applyConnection(this.mConstraintWidget, this.mRightToLeft, State.Constraint.RIGHT_TO_LEFT);
            applyConnection(this.mConstraintWidget, this.mRightToRight, State.Constraint.RIGHT_TO_RIGHT);
            applyConnection(this.mConstraintWidget, this.mStartToStart, State.Constraint.START_TO_START);
            applyConnection(this.mConstraintWidget, this.mStartToEnd, State.Constraint.START_TO_END);
            applyConnection(this.mConstraintWidget, this.mEndToStart, State.Constraint.END_TO_START);
            applyConnection(this.mConstraintWidget, this.mEndToEnd, State.Constraint.END_TO_END);
            applyConnection(this.mConstraintWidget, this.mTopToTop, State.Constraint.TOP_TO_TOP);
            applyConnection(this.mConstraintWidget, this.mTopToBottom, State.Constraint.TOP_TO_BOTTOM);
            applyConnection(this.mConstraintWidget, this.mBottomToTop, State.Constraint.BOTTOM_TO_TOP);
            applyConnection(this.mConstraintWidget, this.mBottomToBottom, State.Constraint.BOTTOM_TO_BOTTOM);
            applyConnection(this.mConstraintWidget, this.mBaselineToBaseline, State.Constraint.BASELINE_TO_BASELINE);
            applyConnection(this.mConstraintWidget, this.mBaselineToTop, State.Constraint.BASELINE_TO_TOP);
            applyConnection(this.mConstraintWidget, this.mBaselineToBottom, State.Constraint.BASELINE_TO_BOTTOM);
            applyConnection(this.mConstraintWidget, this.mCircularConstraint, State.Constraint.CIRCULAR_CONSTRAINT);
            int i = this.mHorizontalChainStyle;
            if (i != 0) {
                this.mConstraintWidget.setHorizontalChainStyle(i);
            }
            int i2 = this.mVerticalChainStyle;
            if (i2 != 0) {
                this.mConstraintWidget.setVerticalChainStyle(i2);
            }
            float f = this.mHorizontalChainWeight;
            if (f != -1.0f) {
                this.mConstraintWidget.setHorizontalWeight(f);
            }
            float f2 = this.mVerticalChainWeight;
            if (f2 != -1.0f) {
                this.mConstraintWidget.setVerticalWeight(f2);
            }
            this.mConstraintWidget.setHorizontalBiasPercent(this.mHorizontalBias);
            this.mConstraintWidget.setVerticalBiasPercent(this.mVerticalBias);
            this.mConstraintWidget.frame.pivotX = this.mPivotX;
            this.mConstraintWidget.frame.pivotY = this.mPivotY;
            this.mConstraintWidget.frame.rotationX = this.mRotationX;
            this.mConstraintWidget.frame.rotationY = this.mRotationY;
            this.mConstraintWidget.frame.rotationZ = this.mRotationZ;
            this.mConstraintWidget.frame.translationX = this.mTranslationX;
            this.mConstraintWidget.frame.translationY = this.mTranslationY;
            this.mConstraintWidget.frame.translationZ = this.mTranslationZ;
            this.mConstraintWidget.frame.scaleX = this.mScaleX;
            this.mConstraintWidget.frame.scaleY = this.mScaleY;
            this.mConstraintWidget.frame.alpha = this.mAlpha;
            this.mConstraintWidget.frame.visibility = this.mVisibility;
            this.mConstraintWidget.setVisibility(this.mVisibility);
            HashMap<String, Integer> hashMap = this.mCustomColors;
            if (hashMap != null) {
                for (String next : hashMap.keySet()) {
                    this.mConstraintWidget.frame.setCustomAttribute(next, (int) TypedValues.Custom.TYPE_COLOR, this.mCustomColors.get(next).intValue());
                }
            }
            HashMap<String, Float> hashMap2 = this.mCustomFloats;
            if (hashMap2 != null) {
                for (String next2 : hashMap2.keySet()) {
                    this.mConstraintWidget.frame.setCustomAttribute(next2, (int) TypedValues.Custom.TYPE_FLOAT, this.mCustomFloats.get(next2).floatValue());
                }
            }
        }
    }

    public ConstraintReference baseline() {
        this.mLast = State.Constraint.BASELINE_TO_BASELINE;
        return this;
    }

    public ConstraintReference baselineToBaseline(Object reference) {
        this.mLast = State.Constraint.BASELINE_TO_BASELINE;
        this.mBaselineToBaseline = reference;
        return this;
    }

    public ConstraintReference baselineToBottom(Object reference) {
        this.mLast = State.Constraint.BASELINE_TO_BOTTOM;
        this.mBaselineToBottom = reference;
        return this;
    }

    public ConstraintReference baselineToTop(Object reference) {
        this.mLast = State.Constraint.BASELINE_TO_TOP;
        this.mBaselineToTop = reference;
        return this;
    }

    public ConstraintReference bias(float value) {
        if (this.mLast == null) {
            return this;
        }
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$state$State$Constraint[this.mLast.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 17:
                this.mHorizontalBias = value;
                break;
            case 9:
            case 10:
            case 11:
            case 12:
            case 18:
                this.mVerticalBias = value;
                break;
        }
        return this;
    }

    public ConstraintReference bottom() {
        if (this.mBottomToTop != null) {
            this.mLast = State.Constraint.BOTTOM_TO_TOP;
        } else {
            this.mLast = State.Constraint.BOTTOM_TO_BOTTOM;
        }
        return this;
    }

    public ConstraintReference bottomToBottom(Object reference) {
        this.mLast = State.Constraint.BOTTOM_TO_BOTTOM;
        this.mBottomToBottom = reference;
        return this;
    }

    public ConstraintReference bottomToTop(Object reference) {
        this.mLast = State.Constraint.BOTTOM_TO_TOP;
        this.mBottomToTop = reference;
        return this;
    }

    public ConstraintReference centerHorizontally(Object reference) {
        Object obj = get(reference);
        this.mStartToStart = obj;
        this.mEndToEnd = obj;
        this.mLast = State.Constraint.CENTER_HORIZONTALLY;
        this.mHorizontalBias = 0.5f;
        return this;
    }

    public ConstraintReference centerVertically(Object reference) {
        Object obj = get(reference);
        this.mTopToTop = obj;
        this.mBottomToBottom = obj;
        this.mLast = State.Constraint.CENTER_VERTICALLY;
        this.mVerticalBias = 0.5f;
        return this;
    }

    public ConstraintReference circularConstraint(Object reference, float angle, float distance) {
        this.mCircularConstraint = get(reference);
        this.mCircularAngle = angle;
        this.mCircularDistance = distance;
        this.mLast = State.Constraint.CIRCULAR_CONSTRAINT;
        return this;
    }

    public ConstraintReference clear() {
        if (this.mLast != null) {
            switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$state$State$Constraint[this.mLast.ordinal()]) {
                case 1:
                case 2:
                    this.mLeftToLeft = null;
                    this.mLeftToRight = null;
                    this.mMarginLeft = 0;
                    this.mMarginLeftGone = 0;
                    break;
                case 3:
                case 4:
                    this.mRightToLeft = null;
                    this.mRightToRight = null;
                    this.mMarginRight = 0;
                    this.mMarginRightGone = 0;
                    break;
                case 5:
                case 6:
                    this.mStartToStart = null;
                    this.mStartToEnd = null;
                    this.mMarginStart = 0;
                    this.mMarginStartGone = 0;
                    break;
                case 7:
                case 8:
                    this.mEndToStart = null;
                    this.mEndToEnd = null;
                    this.mMarginEnd = 0;
                    this.mMarginEndGone = 0;
                    break;
                case 9:
                case 10:
                    this.mTopToTop = null;
                    this.mTopToBottom = null;
                    this.mMarginTop = 0;
                    this.mMarginTopGone = 0;
                    break;
                case 11:
                case 12:
                    this.mBottomToTop = null;
                    this.mBottomToBottom = null;
                    this.mMarginBottom = 0;
                    this.mMarginBottomGone = 0;
                    break;
                case 15:
                    this.mBaselineToBaseline = null;
                    break;
                case 16:
                    this.mCircularConstraint = null;
                    break;
            }
        } else {
            this.mLeftToLeft = null;
            this.mLeftToRight = null;
            this.mMarginLeft = 0;
            this.mRightToLeft = null;
            this.mRightToRight = null;
            this.mMarginRight = 0;
            this.mStartToStart = null;
            this.mStartToEnd = null;
            this.mMarginStart = 0;
            this.mEndToStart = null;
            this.mEndToEnd = null;
            this.mMarginEnd = 0;
            this.mTopToTop = null;
            this.mTopToBottom = null;
            this.mMarginTop = 0;
            this.mBottomToTop = null;
            this.mBottomToBottom = null;
            this.mMarginBottom = 0;
            this.mBaselineToBaseline = null;
            this.mCircularConstraint = null;
            this.mHorizontalBias = 0.5f;
            this.mVerticalBias = 0.5f;
            this.mMarginLeftGone = 0;
            this.mMarginRightGone = 0;
            this.mMarginStartGone = 0;
            this.mMarginEndGone = 0;
            this.mMarginTopGone = 0;
            this.mMarginBottomGone = 0;
        }
        return this;
    }

    public ConstraintReference clearHorizontal() {
        start().clear();
        end().clear();
        left().clear();
        right().clear();
        return this;
    }

    public ConstraintReference clearVertical() {
        top().clear();
        baseline().clear();
        bottom().clear();
        return this;
    }

    public ConstraintWidget createConstraintWidget() {
        return new ConstraintWidget(getWidth().getValue(), getHeight().getValue());
    }

    public ConstraintReference end() {
        if (this.mEndToStart != null) {
            this.mLast = State.Constraint.END_TO_START;
        } else {
            this.mLast = State.Constraint.END_TO_END;
        }
        return this;
    }

    public ConstraintReference endToEnd(Object reference) {
        this.mLast = State.Constraint.END_TO_END;
        this.mEndToEnd = reference;
        return this;
    }

    public ConstraintReference endToStart(Object reference) {
        this.mLast = State.Constraint.END_TO_START;
        this.mEndToStart = reference;
        return this;
    }

    public float getAlpha() {
        return this.mAlpha;
    }

    public ConstraintWidget getConstraintWidget() {
        if (this.mConstraintWidget == null) {
            ConstraintWidget createConstraintWidget = createConstraintWidget();
            this.mConstraintWidget = createConstraintWidget;
            createConstraintWidget.setCompanionWidget(this.mView);
        }
        return this.mConstraintWidget;
    }

    public Facade getFacade() {
        return this.mFacade;
    }

    public Dimension getHeight() {
        return this.mVerticalDimension;
    }

    public int getHorizontalChainStyle() {
        return this.mHorizontalChainStyle;
    }

    public float getHorizontalChainWeight() {
        return this.mHorizontalChainWeight;
    }

    public Object getKey() {
        return this.key;
    }

    public float getPivotX() {
        return this.mPivotX;
    }

    public float getPivotY() {
        return this.mPivotY;
    }

    public float getRotationX() {
        return this.mRotationX;
    }

    public float getRotationY() {
        return this.mRotationY;
    }

    public float getRotationZ() {
        return this.mRotationZ;
    }

    public float getScaleX() {
        return this.mScaleX;
    }

    public float getScaleY() {
        return this.mScaleY;
    }

    public String getTag() {
        return this.mTag;
    }

    public float getTranslationX() {
        return this.mTranslationX;
    }

    public float getTranslationY() {
        return this.mTranslationY;
    }

    public float getTranslationZ() {
        return this.mTranslationZ;
    }

    public int getVerticalChainStyle(int chainStyle) {
        return this.mVerticalChainStyle;
    }

    public float getVerticalChainWeight() {
        return this.mVerticalChainWeight;
    }

    public Object getView() {
        return this.mView;
    }

    public Dimension getWidth() {
        return this.mHorizontalDimension;
    }

    public ConstraintReference height(Dimension dimension) {
        return setHeight(dimension);
    }

    public ConstraintReference horizontalBias(float value) {
        this.mHorizontalBias = value;
        return this;
    }

    public ConstraintReference left() {
        if (this.mLeftToLeft != null) {
            this.mLast = State.Constraint.LEFT_TO_LEFT;
        } else {
            this.mLast = State.Constraint.LEFT_TO_RIGHT;
        }
        return this;
    }

    public ConstraintReference leftToLeft(Object reference) {
        this.mLast = State.Constraint.LEFT_TO_LEFT;
        this.mLeftToLeft = reference;
        return this;
    }

    public ConstraintReference leftToRight(Object reference) {
        this.mLast = State.Constraint.LEFT_TO_RIGHT;
        this.mLeftToRight = reference;
        return this;
    }

    public ConstraintReference margin(int value) {
        if (this.mLast != null) {
            switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$state$State$Constraint[this.mLast.ordinal()]) {
                case 1:
                case 2:
                    this.mMarginLeft = value;
                    break;
                case 3:
                case 4:
                    this.mMarginRight = value;
                    break;
                case 5:
                case 6:
                    this.mMarginStart = value;
                    break;
                case 7:
                case 8:
                    this.mMarginEnd = value;
                    break;
                case 9:
                case 10:
                    this.mMarginTop = value;
                    break;
                case 11:
                case 12:
                    this.mMarginBottom = value;
                    break;
                case 13:
                case 14:
                case 15:
                    this.mMarginBaseline = value;
                    break;
                case 16:
                    this.mCircularDistance = (float) value;
                    break;
            }
        } else {
            this.mMarginLeft = value;
            this.mMarginRight = value;
            this.mMarginStart = value;
            this.mMarginEnd = value;
            this.mMarginTop = value;
            this.mMarginBottom = value;
        }
        return this;
    }

    public ConstraintReference margin(Object marginValue) {
        return margin(this.mState.convertDimension(marginValue));
    }

    public ConstraintReference marginGone(int value) {
        if (this.mLast != null) {
            switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$state$State$Constraint[this.mLast.ordinal()]) {
                case 1:
                case 2:
                    this.mMarginLeftGone = value;
                    break;
                case 3:
                case 4:
                    this.mMarginRightGone = value;
                    break;
                case 5:
                case 6:
                    this.mMarginStartGone = value;
                    break;
                case 7:
                case 8:
                    this.mMarginEndGone = value;
                    break;
                case 9:
                case 10:
                    this.mMarginTopGone = value;
                    break;
                case 11:
                case 12:
                    this.mMarginBottomGone = value;
                    break;
                case 13:
                case 14:
                case 15:
                    this.mMarginBaselineGone = value;
                    break;
            }
        } else {
            this.mMarginLeftGone = value;
            this.mMarginRightGone = value;
            this.mMarginStartGone = value;
            this.mMarginEndGone = value;
            this.mMarginTopGone = value;
            this.mMarginBottomGone = value;
        }
        return this;
    }

    public ConstraintReference marginGone(Object marginGoneValue) {
        return marginGone(this.mState.convertDimension(marginGoneValue));
    }

    public ConstraintReference pivotX(float x) {
        this.mPivotX = x;
        return this;
    }

    public ConstraintReference pivotY(float y) {
        this.mPivotY = y;
        return this;
    }

    public ConstraintReference right() {
        if (this.mRightToLeft != null) {
            this.mLast = State.Constraint.RIGHT_TO_LEFT;
        } else {
            this.mLast = State.Constraint.RIGHT_TO_RIGHT;
        }
        return this;
    }

    public ConstraintReference rightToLeft(Object reference) {
        this.mLast = State.Constraint.RIGHT_TO_LEFT;
        this.mRightToLeft = reference;
        return this;
    }

    public ConstraintReference rightToRight(Object reference) {
        this.mLast = State.Constraint.RIGHT_TO_RIGHT;
        this.mRightToRight = reference;
        return this;
    }

    public ConstraintReference rotationX(float x) {
        this.mRotationX = x;
        return this;
    }

    public ConstraintReference rotationY(float y) {
        this.mRotationY = y;
        return this;
    }

    public ConstraintReference rotationZ(float z) {
        this.mRotationZ = z;
        return this;
    }

    public ConstraintReference scaleX(float x) {
        this.mScaleX = x;
        return this;
    }

    public ConstraintReference scaleY(float y) {
        this.mScaleY = y;
        return this;
    }

    public void setConstraintWidget(ConstraintWidget widget) {
        if (widget != null) {
            this.mConstraintWidget = widget;
            widget.setCompanionWidget(this.mView);
        }
    }

    public void setFacade(Facade facade) {
        this.mFacade = facade;
        if (facade != null) {
            setConstraintWidget(facade.getConstraintWidget());
        }
    }

    public ConstraintReference setHeight(Dimension dimension) {
        this.mVerticalDimension = dimension;
        return this;
    }

    public void setHorizontalChainStyle(int chainStyle) {
        this.mHorizontalChainStyle = chainStyle;
    }

    public void setHorizontalChainWeight(float weight) {
        this.mHorizontalChainWeight = weight;
    }

    public void setKey(Object key2) {
        this.key = key2;
    }

    public void setTag(String tag) {
        this.mTag = tag;
    }

    public void setVerticalChainStyle(int chainStyle) {
        this.mVerticalChainStyle = chainStyle;
    }

    public void setVerticalChainWeight(float weight) {
        this.mVerticalChainWeight = weight;
    }

    public void setView(Object view) {
        this.mView = view;
        ConstraintWidget constraintWidget = this.mConstraintWidget;
        if (constraintWidget != null) {
            constraintWidget.setCompanionWidget(view);
        }
    }

    public ConstraintReference setWidth(Dimension dimension) {
        this.mHorizontalDimension = dimension;
        return this;
    }

    public ConstraintReference start() {
        if (this.mStartToStart != null) {
            this.mLast = State.Constraint.START_TO_START;
        } else {
            this.mLast = State.Constraint.START_TO_END;
        }
        return this;
    }

    public ConstraintReference startToEnd(Object reference) {
        this.mLast = State.Constraint.START_TO_END;
        this.mStartToEnd = reference;
        return this;
    }

    public ConstraintReference startToStart(Object reference) {
        this.mLast = State.Constraint.START_TO_START;
        this.mStartToStart = reference;
        return this;
    }

    public ConstraintReference top() {
        if (this.mTopToTop != null) {
            this.mLast = State.Constraint.TOP_TO_TOP;
        } else {
            this.mLast = State.Constraint.TOP_TO_BOTTOM;
        }
        return this;
    }

    public ConstraintReference topToBottom(Object reference) {
        this.mLast = State.Constraint.TOP_TO_BOTTOM;
        this.mTopToBottom = reference;
        return this;
    }

    public ConstraintReference topToTop(Object reference) {
        this.mLast = State.Constraint.TOP_TO_TOP;
        this.mTopToTop = reference;
        return this;
    }

    public ConstraintReference translationX(float x) {
        this.mTranslationX = x;
        return this;
    }

    public ConstraintReference translationY(float y) {
        this.mTranslationY = y;
        return this;
    }

    public ConstraintReference translationZ(float z) {
        this.mTranslationZ = z;
        return this;
    }

    public void validate() throws IncorrectConstraintException {
        ArrayList arrayList = new ArrayList();
        if (!(this.mLeftToLeft == null || this.mLeftToRight == null)) {
            arrayList.add("LeftToLeft and LeftToRight both defined");
        }
        if (!(this.mRightToLeft == null || this.mRightToRight == null)) {
            arrayList.add("RightToLeft and RightToRight both defined");
        }
        if (!(this.mStartToStart == null || this.mStartToEnd == null)) {
            arrayList.add("StartToStart and StartToEnd both defined");
        }
        if (!(this.mEndToStart == null || this.mEndToEnd == null)) {
            arrayList.add("EndToStart and EndToEnd both defined");
        }
        if (!((this.mLeftToLeft == null && this.mLeftToRight == null && this.mRightToLeft == null && this.mRightToRight == null) || (this.mStartToStart == null && this.mStartToEnd == null && this.mEndToStart == null && this.mEndToEnd == null))) {
            arrayList.add("Both left/right and start/end constraints defined");
        }
        if (arrayList.size() > 0) {
            throw new IncorrectConstraintException(arrayList);
        }
    }

    public ConstraintReference verticalBias(float value) {
        this.mVerticalBias = value;
        return this;
    }

    public ConstraintReference visibility(int visibility) {
        this.mVisibility = visibility;
        return this;
    }

    public ConstraintReference width(Dimension dimension) {
        return setWidth(dimension);
    }
}
