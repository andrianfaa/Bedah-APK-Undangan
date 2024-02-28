package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.core.Metrics;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.core.widgets.Guideline;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ConstraintLayout extends ViewGroup {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_DRAW_CONSTRAINTS = false;
    public static final int DESIGN_INFO_ID = 0;
    private static final boolean MEASURE = false;
    private static final boolean OPTIMIZE_HEIGHT_CHANGE = false;
    private static final String TAG = "ConstraintLayout";
    private static final boolean USE_CONSTRAINTS_HELPER = true;
    public static final String VERSION = "ConstraintLayout-2.1.4";
    private static SharedValues sSharedValues = null;
    SparseArray<View> mChildrenByIds = new SparseArray<>();
    /* access modifiers changed from: private */
    public ArrayList<ConstraintHelper> mConstraintHelpers = new ArrayList<>(4);
    protected ConstraintLayoutStates mConstraintLayoutSpec = null;
    private ConstraintSet mConstraintSet = null;
    private int mConstraintSetId = -1;
    private ConstraintsChangedListener mConstraintsChangedListener;
    private HashMap<String, Integer> mDesignIds = new HashMap<>();
    protected boolean mDirtyHierarchy = USE_CONSTRAINTS_HELPER;
    private int mLastMeasureHeight = -1;
    int mLastMeasureHeightMode = 0;
    int mLastMeasureHeightSize = -1;
    private int mLastMeasureWidth = -1;
    int mLastMeasureWidthMode = 0;
    int mLastMeasureWidthSize = -1;
    /* access modifiers changed from: protected */
    public ConstraintWidgetContainer mLayoutWidget = new ConstraintWidgetContainer();
    private int mMaxHeight = Integer.MAX_VALUE;
    private int mMaxWidth = Integer.MAX_VALUE;
    Measurer mMeasurer = new Measurer(this);
    private Metrics mMetrics;
    private int mMinHeight = 0;
    private int mMinWidth = 0;
    private int mOnMeasureHeightMeasureSpec = 0;
    private int mOnMeasureWidthMeasureSpec = 0;
    /* access modifiers changed from: private */
    public int mOptimizationLevel = 257;
    private SparseArray<ConstraintWidget> mTempMapIdToWidget = new SparseArray<>();

    /* renamed from: androidx.constraintlayout.widget.ConstraintLayout$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintWidget$DimensionBehaviour;

        static {
            int[] iArr = new int[ConstraintWidget.DimensionBehaviour.values().length];
            $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintWidget$DimensionBehaviour = iArr;
            try {
                iArr[ConstraintWidget.DimensionBehaviour.FIXED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintWidget$DimensionBehaviour[ConstraintWidget.DimensionBehaviour.WRAP_CONTENT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintWidget$DimensionBehaviour[ConstraintWidget.DimensionBehaviour.MATCH_PARENT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintWidget$DimensionBehaviour[ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public static final int BASELINE = 5;
        public static final int BOTTOM = 4;
        public static final int CHAIN_PACKED = 2;
        public static final int CHAIN_SPREAD = 0;
        public static final int CHAIN_SPREAD_INSIDE = 1;
        public static final int CIRCLE = 8;
        public static final int END = 7;
        public static final int GONE_UNSET = Integer.MIN_VALUE;
        public static final int HORIZONTAL = 0;
        public static final int LEFT = 1;
        public static final int MATCH_CONSTRAINT = 0;
        public static final int MATCH_CONSTRAINT_PERCENT = 2;
        public static final int MATCH_CONSTRAINT_SPREAD = 0;
        public static final int MATCH_CONSTRAINT_WRAP = 1;
        public static final int PARENT_ID = 0;
        public static final int RIGHT = 2;
        public static final int START = 6;
        public static final int TOP = 3;
        public static final int UNSET = -1;
        public static final int VERTICAL = 1;
        public static final int WRAP_BEHAVIOR_HORIZONTAL_ONLY = 1;
        public static final int WRAP_BEHAVIOR_INCLUDED = 0;
        public static final int WRAP_BEHAVIOR_SKIPPED = 3;
        public static final int WRAP_BEHAVIOR_VERTICAL_ONLY = 2;
        public int baselineMargin = 0;
        public int baselineToBaseline = -1;
        public int baselineToBottom = -1;
        public int baselineToTop = -1;
        public int bottomToBottom = -1;
        public int bottomToTop = -1;
        public float circleAngle = 0.0f;
        public int circleConstraint = -1;
        public int circleRadius = 0;
        public boolean constrainedHeight = false;
        public boolean constrainedWidth = false;
        public String constraintTag = null;
        public String dimensionRatio = null;
        int dimensionRatioSide = 1;
        float dimensionRatioValue = 0.0f;
        public int editorAbsoluteX = -1;
        public int editorAbsoluteY = -1;
        public int endToEnd = -1;
        public int endToStart = -1;
        public int goneBaselineMargin = Integer.MIN_VALUE;
        public int goneBottomMargin = Integer.MIN_VALUE;
        public int goneEndMargin = Integer.MIN_VALUE;
        public int goneLeftMargin = Integer.MIN_VALUE;
        public int goneRightMargin = Integer.MIN_VALUE;
        public int goneStartMargin = Integer.MIN_VALUE;
        public int goneTopMargin = Integer.MIN_VALUE;
        public int guideBegin = -1;
        public int guideEnd = -1;
        public float guidePercent = -1.0f;
        public boolean guidelineUseRtl = ConstraintLayout.USE_CONSTRAINTS_HELPER;
        boolean heightSet = ConstraintLayout.USE_CONSTRAINTS_HELPER;
        public boolean helped = false;
        public float horizontalBias = 0.5f;
        public int horizontalChainStyle = 0;
        boolean horizontalDimensionFixed = ConstraintLayout.USE_CONSTRAINTS_HELPER;
        public float horizontalWeight = -1.0f;
        boolean isGuideline = false;
        boolean isHelper = false;
        boolean isInPlaceholder = false;
        boolean isVirtualGroup = false;
        public int leftToLeft = -1;
        public int leftToRight = -1;
        public int matchConstraintDefaultHeight = 0;
        public int matchConstraintDefaultWidth = 0;
        public int matchConstraintMaxHeight = 0;
        public int matchConstraintMaxWidth = 0;
        public int matchConstraintMinHeight = 0;
        public int matchConstraintMinWidth = 0;
        public float matchConstraintPercentHeight = 1.0f;
        public float matchConstraintPercentWidth = 1.0f;
        boolean needsBaseline = false;
        public int orientation = -1;
        int resolveGoneLeftMargin = Integer.MIN_VALUE;
        int resolveGoneRightMargin = Integer.MIN_VALUE;
        int resolvedGuideBegin;
        int resolvedGuideEnd;
        float resolvedGuidePercent;
        float resolvedHorizontalBias = 0.5f;
        int resolvedLeftToLeft = -1;
        int resolvedLeftToRight = -1;
        int resolvedRightToLeft = -1;
        int resolvedRightToRight = -1;
        public int rightToLeft = -1;
        public int rightToRight = -1;
        public int startToEnd = -1;
        public int startToStart = -1;
        public int topToBottom = -1;
        public int topToTop = -1;
        public float verticalBias = 0.5f;
        public int verticalChainStyle = 0;
        boolean verticalDimensionFixed = ConstraintLayout.USE_CONSTRAINTS_HELPER;
        public float verticalWeight = -1.0f;
        ConstraintWidget widget = new ConstraintWidget();
        boolean widthSet = ConstraintLayout.USE_CONSTRAINTS_HELPER;
        public int wrapBehaviorInParent = 0;

        private static class Table {
            public static final int ANDROID_ORIENTATION = 1;
            public static final int GUIDELINE_USE_RTL = 67;
            public static final int LAYOUT_CONSTRAINED_HEIGHT = 28;
            public static final int LAYOUT_CONSTRAINED_WIDTH = 27;
            public static final int LAYOUT_CONSTRAINT_BASELINE_CREATOR = 43;
            public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BASELINE_OF = 16;
            public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BOTTOM_OF = 53;
            public static final int LAYOUT_CONSTRAINT_BASELINE_TO_TOP_OF = 52;
            public static final int LAYOUT_CONSTRAINT_BOTTOM_CREATOR = 42;
            public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_BOTTOM_OF = 15;
            public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_TOP_OF = 14;
            public static final int LAYOUT_CONSTRAINT_CIRCLE = 2;
            public static final int LAYOUT_CONSTRAINT_CIRCLE_ANGLE = 4;
            public static final int LAYOUT_CONSTRAINT_CIRCLE_RADIUS = 3;
            public static final int LAYOUT_CONSTRAINT_DIMENSION_RATIO = 44;
            public static final int LAYOUT_CONSTRAINT_END_TO_END_OF = 20;
            public static final int LAYOUT_CONSTRAINT_END_TO_START_OF = 19;
            public static final int LAYOUT_CONSTRAINT_GUIDE_BEGIN = 5;
            public static final int LAYOUT_CONSTRAINT_GUIDE_END = 6;
            public static final int LAYOUT_CONSTRAINT_GUIDE_PERCENT = 7;
            public static final int LAYOUT_CONSTRAINT_HEIGHT = 65;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_DEFAULT = 32;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_MAX = 37;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_MIN = 36;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_PERCENT = 38;
            public static final int LAYOUT_CONSTRAINT_HORIZONTAL_BIAS = 29;
            public static final int LAYOUT_CONSTRAINT_HORIZONTAL_CHAINSTYLE = 47;
            public static final int LAYOUT_CONSTRAINT_HORIZONTAL_WEIGHT = 45;
            public static final int LAYOUT_CONSTRAINT_LEFT_CREATOR = 39;
            public static final int LAYOUT_CONSTRAINT_LEFT_TO_LEFT_OF = 8;
            public static final int LAYOUT_CONSTRAINT_LEFT_TO_RIGHT_OF = 9;
            public static final int LAYOUT_CONSTRAINT_RIGHT_CREATOR = 41;
            public static final int LAYOUT_CONSTRAINT_RIGHT_TO_LEFT_OF = 10;
            public static final int LAYOUT_CONSTRAINT_RIGHT_TO_RIGHT_OF = 11;
            public static final int LAYOUT_CONSTRAINT_START_TO_END_OF = 17;
            public static final int LAYOUT_CONSTRAINT_START_TO_START_OF = 18;
            public static final int LAYOUT_CONSTRAINT_TAG = 51;
            public static final int LAYOUT_CONSTRAINT_TOP_CREATOR = 40;
            public static final int LAYOUT_CONSTRAINT_TOP_TO_BOTTOM_OF = 13;
            public static final int LAYOUT_CONSTRAINT_TOP_TO_TOP_OF = 12;
            public static final int LAYOUT_CONSTRAINT_VERTICAL_BIAS = 30;
            public static final int LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE = 48;
            public static final int LAYOUT_CONSTRAINT_VERTICAL_WEIGHT = 46;
            public static final int LAYOUT_CONSTRAINT_WIDTH = 64;
            public static final int LAYOUT_CONSTRAINT_WIDTH_DEFAULT = 31;
            public static final int LAYOUT_CONSTRAINT_WIDTH_MAX = 34;
            public static final int LAYOUT_CONSTRAINT_WIDTH_MIN = 33;
            public static final int LAYOUT_CONSTRAINT_WIDTH_PERCENT = 35;
            public static final int LAYOUT_EDITOR_ABSOLUTEX = 49;
            public static final int LAYOUT_EDITOR_ABSOLUTEY = 50;
            public static final int LAYOUT_GONE_MARGIN_BASELINE = 55;
            public static final int LAYOUT_GONE_MARGIN_BOTTOM = 24;
            public static final int LAYOUT_GONE_MARGIN_END = 26;
            public static final int LAYOUT_GONE_MARGIN_LEFT = 21;
            public static final int LAYOUT_GONE_MARGIN_RIGHT = 23;
            public static final int LAYOUT_GONE_MARGIN_START = 25;
            public static final int LAYOUT_GONE_MARGIN_TOP = 22;
            public static final int LAYOUT_MARGIN_BASELINE = 54;
            public static final int LAYOUT_WRAP_BEHAVIOR_IN_PARENT = 66;
            public static final int UNUSED = 0;
            public static final SparseIntArray map;

            static {
                SparseIntArray sparseIntArray = new SparseIntArray();
                map = sparseIntArray;
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth, 64);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight, 65);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toTopOf, 52);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBottomOf, 53);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_guidelineUseRtl, 67);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBaseline, 55);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_marginBaseline, 54);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintTag, 51);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_wrapBehaviorInParent, 66);
            }

            private Table() {
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray obtainStyledAttributes = c.obtainStyledAttributes(attrs, R.styleable.ConstraintLayout_Layout);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                switch (Table.map.get(index)) {
                    case 1:
                        this.orientation = obtainStyledAttributes.getInt(index, this.orientation);
                        break;
                    case 2:
                        int resourceId = obtainStyledAttributes.getResourceId(index, this.circleConstraint);
                        this.circleConstraint = resourceId;
                        if (resourceId != -1) {
                            break;
                        } else {
                            this.circleConstraint = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 3:
                        this.circleRadius = obtainStyledAttributes.getDimensionPixelSize(index, this.circleRadius);
                        break;
                    case 4:
                        float f = obtainStyledAttributes.getFloat(index, this.circleAngle) % 360.0f;
                        this.circleAngle = f;
                        if (f >= 0.0f) {
                            break;
                        } else {
                            this.circleAngle = (360.0f - f) % 360.0f;
                            break;
                        }
                    case 5:
                        this.guideBegin = obtainStyledAttributes.getDimensionPixelOffset(index, this.guideBegin);
                        break;
                    case 6:
                        this.guideEnd = obtainStyledAttributes.getDimensionPixelOffset(index, this.guideEnd);
                        break;
                    case 7:
                        this.guidePercent = obtainStyledAttributes.getFloat(index, this.guidePercent);
                        break;
                    case 8:
                        int resourceId2 = obtainStyledAttributes.getResourceId(index, this.leftToLeft);
                        this.leftToLeft = resourceId2;
                        if (resourceId2 != -1) {
                            break;
                        } else {
                            this.leftToLeft = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 9:
                        int resourceId3 = obtainStyledAttributes.getResourceId(index, this.leftToRight);
                        this.leftToRight = resourceId3;
                        if (resourceId3 != -1) {
                            break;
                        } else {
                            this.leftToRight = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 10:
                        int resourceId4 = obtainStyledAttributes.getResourceId(index, this.rightToLeft);
                        this.rightToLeft = resourceId4;
                        if (resourceId4 != -1) {
                            break;
                        } else {
                            this.rightToLeft = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 11:
                        int resourceId5 = obtainStyledAttributes.getResourceId(index, this.rightToRight);
                        this.rightToRight = resourceId5;
                        if (resourceId5 != -1) {
                            break;
                        } else {
                            this.rightToRight = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 12:
                        int resourceId6 = obtainStyledAttributes.getResourceId(index, this.topToTop);
                        this.topToTop = resourceId6;
                        if (resourceId6 != -1) {
                            break;
                        } else {
                            this.topToTop = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 13:
                        int resourceId7 = obtainStyledAttributes.getResourceId(index, this.topToBottom);
                        this.topToBottom = resourceId7;
                        if (resourceId7 != -1) {
                            break;
                        } else {
                            this.topToBottom = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 14:
                        int resourceId8 = obtainStyledAttributes.getResourceId(index, this.bottomToTop);
                        this.bottomToTop = resourceId8;
                        if (resourceId8 != -1) {
                            break;
                        } else {
                            this.bottomToTop = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 15:
                        int resourceId9 = obtainStyledAttributes.getResourceId(index, this.bottomToBottom);
                        this.bottomToBottom = resourceId9;
                        if (resourceId9 != -1) {
                            break;
                        } else {
                            this.bottomToBottom = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 16:
                        int resourceId10 = obtainStyledAttributes.getResourceId(index, this.baselineToBaseline);
                        this.baselineToBaseline = resourceId10;
                        if (resourceId10 != -1) {
                            break;
                        } else {
                            this.baselineToBaseline = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 17:
                        int resourceId11 = obtainStyledAttributes.getResourceId(index, this.startToEnd);
                        this.startToEnd = resourceId11;
                        if (resourceId11 != -1) {
                            break;
                        } else {
                            this.startToEnd = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 18:
                        int resourceId12 = obtainStyledAttributes.getResourceId(index, this.startToStart);
                        this.startToStart = resourceId12;
                        if (resourceId12 != -1) {
                            break;
                        } else {
                            this.startToStart = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 19:
                        int resourceId13 = obtainStyledAttributes.getResourceId(index, this.endToStart);
                        this.endToStart = resourceId13;
                        if (resourceId13 != -1) {
                            break;
                        } else {
                            this.endToStart = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 20:
                        int resourceId14 = obtainStyledAttributes.getResourceId(index, this.endToEnd);
                        this.endToEnd = resourceId14;
                        if (resourceId14 != -1) {
                            break;
                        } else {
                            this.endToEnd = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 21:
                        this.goneLeftMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneLeftMargin);
                        break;
                    case 22:
                        this.goneTopMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneTopMargin);
                        break;
                    case 23:
                        this.goneRightMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneRightMargin);
                        break;
                    case 24:
                        this.goneBottomMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneBottomMargin);
                        break;
                    case 25:
                        this.goneStartMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneStartMargin);
                        break;
                    case 26:
                        this.goneEndMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneEndMargin);
                        break;
                    case 27:
                        this.constrainedWidth = obtainStyledAttributes.getBoolean(index, this.constrainedWidth);
                        break;
                    case 28:
                        this.constrainedHeight = obtainStyledAttributes.getBoolean(index, this.constrainedHeight);
                        break;
                    case Table.LAYOUT_CONSTRAINT_HORIZONTAL_BIAS /*29*/:
                        this.horizontalBias = obtainStyledAttributes.getFloat(index, this.horizontalBias);
                        break;
                    case 30:
                        this.verticalBias = obtainStyledAttributes.getFloat(index, this.verticalBias);
                        break;
                    case Table.LAYOUT_CONSTRAINT_WIDTH_DEFAULT /*31*/:
                        int i2 = obtainStyledAttributes.getInt(index, 0);
                        this.matchConstraintDefaultWidth = i2;
                        if (i2 != 1) {
                            break;
                        } else {
                            Log.e(ConstraintLayout.TAG, "layout_constraintWidth_default=\"wrap\" is deprecated.\nUse layout_width=\"WRAP_CONTENT\" and layout_constrainedWidth=\"true\" instead.");
                            break;
                        }
                    case 32:
                        int i3 = obtainStyledAttributes.getInt(index, 0);
                        this.matchConstraintDefaultHeight = i3;
                        if (i3 != 1) {
                            break;
                        } else {
                            Log.e(ConstraintLayout.TAG, "layout_constraintHeight_default=\"wrap\" is deprecated.\nUse layout_height=\"WRAP_CONTENT\" and layout_constrainedHeight=\"true\" instead.");
                            break;
                        }
                    case 33:
                        try {
                            this.matchConstraintMinWidth = obtainStyledAttributes.getDimensionPixelSize(index, this.matchConstraintMinWidth);
                            break;
                        } catch (Exception e) {
                            if (obtainStyledAttributes.getInt(index, this.matchConstraintMinWidth) != -2) {
                                break;
                            } else {
                                this.matchConstraintMinWidth = -2;
                                break;
                            }
                        }
                    case 34:
                        try {
                            this.matchConstraintMaxWidth = obtainStyledAttributes.getDimensionPixelSize(index, this.matchConstraintMaxWidth);
                            break;
                        } catch (Exception e2) {
                            if (obtainStyledAttributes.getInt(index, this.matchConstraintMaxWidth) != -2) {
                                break;
                            } else {
                                this.matchConstraintMaxWidth = -2;
                                break;
                            }
                        }
                    case 35:
                        this.matchConstraintPercentWidth = Math.max(0.0f, obtainStyledAttributes.getFloat(index, this.matchConstraintPercentWidth));
                        this.matchConstraintDefaultWidth = 2;
                        break;
                    case 36:
                        try {
                            this.matchConstraintMinHeight = obtainStyledAttributes.getDimensionPixelSize(index, this.matchConstraintMinHeight);
                            break;
                        } catch (Exception e3) {
                            if (obtainStyledAttributes.getInt(index, this.matchConstraintMinHeight) != -2) {
                                break;
                            } else {
                                this.matchConstraintMinHeight = -2;
                                break;
                            }
                        }
                    case 37:
                        try {
                            this.matchConstraintMaxHeight = obtainStyledAttributes.getDimensionPixelSize(index, this.matchConstraintMaxHeight);
                            break;
                        } catch (Exception e4) {
                            if (obtainStyledAttributes.getInt(index, this.matchConstraintMaxHeight) != -2) {
                                break;
                            } else {
                                this.matchConstraintMaxHeight = -2;
                                break;
                            }
                        }
                    case 38:
                        this.matchConstraintPercentHeight = Math.max(0.0f, obtainStyledAttributes.getFloat(index, this.matchConstraintPercentHeight));
                        this.matchConstraintDefaultHeight = 2;
                        break;
                    case 44:
                        ConstraintSet.parseDimensionRatioString(this, obtainStyledAttributes.getString(index));
                        break;
                    case 45:
                        this.horizontalWeight = obtainStyledAttributes.getFloat(index, this.horizontalWeight);
                        break;
                    case 46:
                        this.verticalWeight = obtainStyledAttributes.getFloat(index, this.verticalWeight);
                        break;
                    case 47:
                        this.horizontalChainStyle = obtainStyledAttributes.getInt(index, 0);
                        break;
                    case Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE /*48*/:
                        this.verticalChainStyle = obtainStyledAttributes.getInt(index, 0);
                        break;
                    case Table.LAYOUT_EDITOR_ABSOLUTEX /*49*/:
                        this.editorAbsoluteX = obtainStyledAttributes.getDimensionPixelOffset(index, this.editorAbsoluteX);
                        break;
                    case 50:
                        this.editorAbsoluteY = obtainStyledAttributes.getDimensionPixelOffset(index, this.editorAbsoluteY);
                        break;
                    case Table.LAYOUT_CONSTRAINT_TAG /*51*/:
                        this.constraintTag = obtainStyledAttributes.getString(index);
                        break;
                    case Table.LAYOUT_CONSTRAINT_BASELINE_TO_TOP_OF /*52*/:
                        int resourceId15 = obtainStyledAttributes.getResourceId(index, this.baselineToTop);
                        this.baselineToTop = resourceId15;
                        if (resourceId15 != -1) {
                            break;
                        } else {
                            this.baselineToTop = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case Table.LAYOUT_CONSTRAINT_BASELINE_TO_BOTTOM_OF /*53*/:
                        int resourceId16 = obtainStyledAttributes.getResourceId(index, this.baselineToBottom);
                        this.baselineToBottom = resourceId16;
                        if (resourceId16 != -1) {
                            break;
                        } else {
                            this.baselineToBottom = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case Table.LAYOUT_MARGIN_BASELINE /*54*/:
                        this.baselineMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.baselineMargin);
                        break;
                    case Table.LAYOUT_GONE_MARGIN_BASELINE /*55*/:
                        this.goneBaselineMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneBaselineMargin);
                        break;
                    case 64:
                        ConstraintSet.parseDimensionConstraints(this, obtainStyledAttributes, index, 0);
                        this.widthSet = ConstraintLayout.USE_CONSTRAINTS_HELPER;
                        break;
                    case Table.LAYOUT_CONSTRAINT_HEIGHT /*65*/:
                        ConstraintSet.parseDimensionConstraints(this, obtainStyledAttributes, index, 1);
                        this.heightSet = ConstraintLayout.USE_CONSTRAINTS_HELPER;
                        break;
                    case Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT /*66*/:
                        this.wrapBehaviorInParent = obtainStyledAttributes.getInt(index, this.wrapBehaviorInParent);
                        break;
                    case Table.GUIDELINE_USE_RTL /*67*/:
                        this.guidelineUseRtl = obtainStyledAttributes.getBoolean(index, this.guidelineUseRtl);
                        break;
                }
            }
            obtainStyledAttributes.recycle();
            validate();
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super(source);
            this.guideBegin = source.guideBegin;
            this.guideEnd = source.guideEnd;
            this.guidePercent = source.guidePercent;
            this.guidelineUseRtl = source.guidelineUseRtl;
            this.leftToLeft = source.leftToLeft;
            this.leftToRight = source.leftToRight;
            this.rightToLeft = source.rightToLeft;
            this.rightToRight = source.rightToRight;
            this.topToTop = source.topToTop;
            this.topToBottom = source.topToBottom;
            this.bottomToTop = source.bottomToTop;
            this.bottomToBottom = source.bottomToBottom;
            this.baselineToBaseline = source.baselineToBaseline;
            this.baselineToTop = source.baselineToTop;
            this.baselineToBottom = source.baselineToBottom;
            this.circleConstraint = source.circleConstraint;
            this.circleRadius = source.circleRadius;
            this.circleAngle = source.circleAngle;
            this.startToEnd = source.startToEnd;
            this.startToStart = source.startToStart;
            this.endToStart = source.endToStart;
            this.endToEnd = source.endToEnd;
            this.goneLeftMargin = source.goneLeftMargin;
            this.goneTopMargin = source.goneTopMargin;
            this.goneRightMargin = source.goneRightMargin;
            this.goneBottomMargin = source.goneBottomMargin;
            this.goneStartMargin = source.goneStartMargin;
            this.goneEndMargin = source.goneEndMargin;
            this.goneBaselineMargin = source.goneBaselineMargin;
            this.baselineMargin = source.baselineMargin;
            this.horizontalBias = source.horizontalBias;
            this.verticalBias = source.verticalBias;
            this.dimensionRatio = source.dimensionRatio;
            this.dimensionRatioValue = source.dimensionRatioValue;
            this.dimensionRatioSide = source.dimensionRatioSide;
            this.horizontalWeight = source.horizontalWeight;
            this.verticalWeight = source.verticalWeight;
            this.horizontalChainStyle = source.horizontalChainStyle;
            this.verticalChainStyle = source.verticalChainStyle;
            this.constrainedWidth = source.constrainedWidth;
            this.constrainedHeight = source.constrainedHeight;
            this.matchConstraintDefaultWidth = source.matchConstraintDefaultWidth;
            this.matchConstraintDefaultHeight = source.matchConstraintDefaultHeight;
            this.matchConstraintMinWidth = source.matchConstraintMinWidth;
            this.matchConstraintMaxWidth = source.matchConstraintMaxWidth;
            this.matchConstraintMinHeight = source.matchConstraintMinHeight;
            this.matchConstraintMaxHeight = source.matchConstraintMaxHeight;
            this.matchConstraintPercentWidth = source.matchConstraintPercentWidth;
            this.matchConstraintPercentHeight = source.matchConstraintPercentHeight;
            this.editorAbsoluteX = source.editorAbsoluteX;
            this.editorAbsoluteY = source.editorAbsoluteY;
            this.orientation = source.orientation;
            this.horizontalDimensionFixed = source.horizontalDimensionFixed;
            this.verticalDimensionFixed = source.verticalDimensionFixed;
            this.needsBaseline = source.needsBaseline;
            this.isGuideline = source.isGuideline;
            this.resolvedLeftToLeft = source.resolvedLeftToLeft;
            this.resolvedLeftToRight = source.resolvedLeftToRight;
            this.resolvedRightToLeft = source.resolvedRightToLeft;
            this.resolvedRightToRight = source.resolvedRightToRight;
            this.resolveGoneLeftMargin = source.resolveGoneLeftMargin;
            this.resolveGoneRightMargin = source.resolveGoneRightMargin;
            this.resolvedHorizontalBias = source.resolvedHorizontalBias;
            this.constraintTag = source.constraintTag;
            this.wrapBehaviorInParent = source.wrapBehaviorInParent;
            this.widget = source.widget;
            this.widthSet = source.widthSet;
            this.heightSet = source.heightSet;
        }

        public String getConstraintTag() {
            return this.constraintTag;
        }

        public ConstraintWidget getConstraintWidget() {
            return this.widget;
        }

        public void reset() {
            ConstraintWidget constraintWidget = this.widget;
            if (constraintWidget != null) {
                constraintWidget.reset();
            }
        }

        public void resolveLayoutDirection(int layoutDirection) {
            int i = this.leftMargin;
            int i2 = this.rightMargin;
            boolean z = false;
            if (Build.VERSION.SDK_INT >= 17) {
                super.resolveLayoutDirection(layoutDirection);
                z = 1 == getLayoutDirection();
            }
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolveGoneLeftMargin = this.goneLeftMargin;
            this.resolveGoneRightMargin = this.goneRightMargin;
            float f = this.horizontalBias;
            this.resolvedHorizontalBias = f;
            int i3 = this.guideBegin;
            this.resolvedGuideBegin = i3;
            int i4 = this.guideEnd;
            this.resolvedGuideEnd = i4;
            float f2 = this.guidePercent;
            this.resolvedGuidePercent = f2;
            if (z) {
                boolean z2 = false;
                int i5 = this.startToEnd;
                if (i5 != -1) {
                    this.resolvedRightToLeft = i5;
                    z2 = ConstraintLayout.USE_CONSTRAINTS_HELPER;
                } else {
                    int i6 = this.startToStart;
                    if (i6 != -1) {
                        this.resolvedRightToRight = i6;
                        z2 = ConstraintLayout.USE_CONSTRAINTS_HELPER;
                    }
                }
                int i7 = this.endToStart;
                if (i7 != -1) {
                    this.resolvedLeftToRight = i7;
                    z2 = ConstraintLayout.USE_CONSTRAINTS_HELPER;
                }
                int i8 = this.endToEnd;
                if (i8 != -1) {
                    this.resolvedLeftToLeft = i8;
                    z2 = ConstraintLayout.USE_CONSTRAINTS_HELPER;
                }
                int i9 = this.goneStartMargin;
                if (i9 != Integer.MIN_VALUE) {
                    this.resolveGoneRightMargin = i9;
                }
                int i10 = this.goneEndMargin;
                if (i10 != Integer.MIN_VALUE) {
                    this.resolveGoneLeftMargin = i10;
                }
                if (z2) {
                    this.resolvedHorizontalBias = 1.0f - f;
                }
                if (this.isGuideline && this.orientation == 1 && this.guidelineUseRtl) {
                    if (f2 != -1.0f) {
                        this.resolvedGuidePercent = 1.0f - f2;
                        this.resolvedGuideBegin = -1;
                        this.resolvedGuideEnd = -1;
                    } else if (i3 != -1) {
                        this.resolvedGuideEnd = i3;
                        this.resolvedGuideBegin = -1;
                        this.resolvedGuidePercent = -1.0f;
                    } else if (i4 != -1) {
                        this.resolvedGuideBegin = i4;
                        this.resolvedGuideEnd = -1;
                        this.resolvedGuidePercent = -1.0f;
                    }
                }
            } else {
                int i11 = this.startToEnd;
                if (i11 != -1) {
                    this.resolvedLeftToRight = i11;
                }
                int i12 = this.startToStart;
                if (i12 != -1) {
                    this.resolvedLeftToLeft = i12;
                }
                int i13 = this.endToStart;
                if (i13 != -1) {
                    this.resolvedRightToLeft = i13;
                }
                int i14 = this.endToEnd;
                if (i14 != -1) {
                    this.resolvedRightToRight = i14;
                }
                int i15 = this.goneStartMargin;
                if (i15 != Integer.MIN_VALUE) {
                    this.resolveGoneLeftMargin = i15;
                }
                int i16 = this.goneEndMargin;
                if (i16 != Integer.MIN_VALUE) {
                    this.resolveGoneRightMargin = i16;
                }
            }
            if (this.endToStart == -1 && this.endToEnd == -1 && this.startToStart == -1 && this.startToEnd == -1) {
                int i17 = this.rightToLeft;
                if (i17 != -1) {
                    this.resolvedRightToLeft = i17;
                    if (this.rightMargin <= 0 && i2 > 0) {
                        this.rightMargin = i2;
                    }
                } else {
                    int i18 = this.rightToRight;
                    if (i18 != -1) {
                        this.resolvedRightToRight = i18;
                        if (this.rightMargin <= 0 && i2 > 0) {
                            this.rightMargin = i2;
                        }
                    }
                }
                int i19 = this.leftToLeft;
                if (i19 != -1) {
                    this.resolvedLeftToLeft = i19;
                    if (this.leftMargin <= 0 && i > 0) {
                        this.leftMargin = i;
                        return;
                    }
                    return;
                }
                int i20 = this.leftToRight;
                if (i20 != -1) {
                    this.resolvedLeftToRight = i20;
                    if (this.leftMargin <= 0 && i > 0) {
                        this.leftMargin = i;
                    }
                }
            }
        }

        public void setWidgetDebugName(String text) {
            this.widget.setDebugName(text);
        }

        public void validate() {
            this.isGuideline = false;
            this.horizontalDimensionFixed = ConstraintLayout.USE_CONSTRAINTS_HELPER;
            this.verticalDimensionFixed = ConstraintLayout.USE_CONSTRAINTS_HELPER;
            if (this.width == -2 && this.constrainedWidth) {
                this.horizontalDimensionFixed = false;
                if (this.matchConstraintDefaultWidth == 0) {
                    this.matchConstraintDefaultWidth = 1;
                }
            }
            if (this.height == -2 && this.constrainedHeight) {
                this.verticalDimensionFixed = false;
                if (this.matchConstraintDefaultHeight == 0) {
                    this.matchConstraintDefaultHeight = 1;
                }
            }
            if (this.width == 0 || this.width == -1) {
                this.horizontalDimensionFixed = false;
                if (this.width == 0 && this.matchConstraintDefaultWidth == 1) {
                    this.width = -2;
                    this.constrainedWidth = ConstraintLayout.USE_CONSTRAINTS_HELPER;
                }
            }
            if (this.height == 0 || this.height == -1) {
                this.verticalDimensionFixed = false;
                if (this.height == 0 && this.matchConstraintDefaultHeight == 1) {
                    this.height = -2;
                    this.constrainedHeight = ConstraintLayout.USE_CONSTRAINTS_HELPER;
                }
            }
            if (this.guidePercent != -1.0f || this.guideBegin != -1 || this.guideEnd != -1) {
                this.isGuideline = ConstraintLayout.USE_CONSTRAINTS_HELPER;
                this.horizontalDimensionFixed = ConstraintLayout.USE_CONSTRAINTS_HELPER;
                this.verticalDimensionFixed = ConstraintLayout.USE_CONSTRAINTS_HELPER;
                if (!(this.widget instanceof Guideline)) {
                    this.widget = new Guideline();
                }
                ((Guideline) this.widget).setOrientation(this.orientation);
            }
        }
    }

    class Measurer implements BasicMeasure.Measurer {
        ConstraintLayout layout;
        int layoutHeightSpec;
        int layoutWidthSpec;
        int paddingBottom;
        int paddingHeight;
        int paddingTop;
        int paddingWidth;

        public Measurer(ConstraintLayout l) {
            this.layout = l;
        }

        private boolean isSimilarSpec(int lastMeasureSpec, int spec, int widgetSize) {
            if (lastMeasureSpec == spec) {
                return ConstraintLayout.USE_CONSTRAINTS_HELPER;
            }
            int mode = View.MeasureSpec.getMode(lastMeasureSpec);
            int size = View.MeasureSpec.getSize(lastMeasureSpec);
            int mode2 = View.MeasureSpec.getMode(spec);
            int size2 = View.MeasureSpec.getSize(spec);
            if (mode2 != 1073741824) {
                return false;
            }
            if ((mode == Integer.MIN_VALUE || mode == 0) && widgetSize == size2) {
                return ConstraintLayout.USE_CONSTRAINTS_HELPER;
            }
            return false;
        }

        public void captureLayoutInfo(int widthSpec, int heightSpec, int top, int bottom, int width, int height) {
            this.paddingTop = top;
            this.paddingBottom = bottom;
            this.paddingWidth = width;
            this.paddingHeight = height;
            this.layoutWidthSpec = widthSpec;
            this.layoutHeightSpec = heightSpec;
        }

        public final void didMeasures() {
            int childCount = this.layout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.layout.getChildAt(i);
                if (childAt instanceof Placeholder) {
                    ((Placeholder) childAt).updatePostMeasure(this.layout);
                }
            }
            int size = this.layout.mConstraintHelpers.size();
            if (size > 0) {
                for (int i2 = 0; i2 < size; i2++) {
                    ((ConstraintHelper) this.layout.mConstraintHelpers.get(i2)).updatePostMeasure(this.layout);
                }
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:127:0x01f4  */
        /* JADX WARNING: Removed duplicated region for block: B:130:0x01fe  */
        /* JADX WARNING: Removed duplicated region for block: B:133:0x0203 A[RETURN] */
        /* JADX WARNING: Removed duplicated region for block: B:134:0x0204  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void measure(androidx.constraintlayout.core.widgets.ConstraintWidget r29, androidx.constraintlayout.core.widgets.analyzer.BasicMeasure.Measure r30) {
            /*
                r28 = this;
                r0 = r28
                r1 = r29
                r2 = r30
                if (r1 != 0) goto L_0x0009
                return
            L_0x0009:
                int r3 = r29.getVisibility()
                r4 = 8
                r5 = 0
                if (r3 != r4) goto L_0x001f
                boolean r3 = r29.isInPlaceholder()
                if (r3 != 0) goto L_0x001f
                r2.measuredWidth = r5
                r2.measuredHeight = r5
                r2.measuredBaseline = r5
                return
            L_0x001f:
                androidx.constraintlayout.core.widgets.ConstraintWidget r3 = r29.getParent()
                if (r3 != 0) goto L_0x0026
                return
            L_0x0026:
                androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r3 = r2.horizontalBehavior
                androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r4 = r2.verticalBehavior
                int r6 = r2.horizontalDimension
                int r7 = r2.verticalDimension
                r8 = 0
                r9 = 0
                int r10 = r0.paddingTop
                int r11 = r0.paddingBottom
                int r10 = r10 + r11
                int r11 = r0.paddingWidth
                java.lang.Object r12 = r29.getCompanionWidget()
                android.view.View r12 = (android.view.View) r12
                int[] r13 = androidx.constraintlayout.widget.ConstraintLayout.AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$widgets$ConstraintWidget$DimensionBehaviour
                int r14 = r3.ordinal()
                r13 = r13[r14]
                r15 = -2
                r14 = 1
                switch(r13) {
                    case 1: goto L_0x00b3;
                    case 2: goto L_0x00ab;
                    case 3: goto L_0x009e;
                    case 4: goto L_0x004c;
                    default: goto L_0x004a;
                }
            L_0x004a:
                goto L_0x00ba
            L_0x004c:
                int r13 = r0.layoutWidthSpec
                int r8 = android.view.ViewGroup.getChildMeasureSpec(r13, r11, r15)
                int r13 = r1.mMatchConstraintDefaultWidth
                if (r13 != r14) goto L_0x0058
                r13 = r14
                goto L_0x0059
            L_0x0058:
                r13 = 0
            L_0x0059:
                int r14 = r2.measureStrategy
                int r15 = androidx.constraintlayout.core.widgets.analyzer.BasicMeasure.Measure.TRY_GIVEN_DIMENSIONS
                if (r14 == r15) goto L_0x0065
                int r14 = r2.measureStrategy
                int r15 = androidx.constraintlayout.core.widgets.analyzer.BasicMeasure.Measure.USE_GIVEN_DIMENSIONS
                if (r14 != r15) goto L_0x00ba
            L_0x0065:
                int r14 = r12.getMeasuredHeight()
                int r15 = r29.getHeight()
                if (r14 != r15) goto L_0x0071
                r14 = 1
                goto L_0x0072
            L_0x0071:
                r14 = 0
            L_0x0072:
                int r15 = r2.measureStrategy
                int r5 = androidx.constraintlayout.core.widgets.analyzer.BasicMeasure.Measure.USE_GIVEN_DIMENSIONS
                if (r15 == r5) goto L_0x008b
                if (r13 == 0) goto L_0x008b
                if (r13 == 0) goto L_0x007e
                if (r14 != 0) goto L_0x008b
            L_0x007e:
                boolean r5 = r12 instanceof androidx.constraintlayout.widget.Placeholder
                if (r5 != 0) goto L_0x008b
                boolean r5 = r29.isResolvedHorizontally()
                if (r5 == 0) goto L_0x0089
                goto L_0x008b
            L_0x0089:
                r5 = 0
                goto L_0x008c
            L_0x008b:
                r5 = 1
            L_0x008c:
                if (r5 == 0) goto L_0x009b
                int r15 = r29.getWidth()
                r17 = r5
                r5 = 1073741824(0x40000000, float:2.0)
                int r8 = android.view.View.MeasureSpec.makeMeasureSpec(r15, r5)
                goto L_0x00ba
            L_0x009b:
                r17 = r5
                goto L_0x00ba
            L_0x009e:
                int r5 = r0.layoutWidthSpec
                int r13 = r29.getHorizontalMargin()
                int r13 = r13 + r11
                r14 = -1
                int r8 = android.view.ViewGroup.getChildMeasureSpec(r5, r13, r14)
                goto L_0x00ba
            L_0x00ab:
                int r5 = r0.layoutWidthSpec
                r13 = -2
                int r8 = android.view.ViewGroup.getChildMeasureSpec(r5, r11, r13)
                goto L_0x00ba
            L_0x00b3:
                r5 = 1073741824(0x40000000, float:2.0)
                int r8 = android.view.View.MeasureSpec.makeMeasureSpec(r6, r5)
            L_0x00ba:
                int[] r5 = androidx.constraintlayout.widget.ConstraintLayout.AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$widgets$ConstraintWidget$DimensionBehaviour
                int r13 = r4.ordinal()
                r5 = r5[r13]
                switch(r5) {
                    case 1: goto L_0x0130;
                    case 2: goto L_0x0128;
                    case 3: goto L_0x011b;
                    case 4: goto L_0x00c7;
                    default: goto L_0x00c5;
                }
            L_0x00c5:
                goto L_0x0137
            L_0x00c7:
                int r5 = r0.layoutHeightSpec
                r13 = -2
                int r9 = android.view.ViewGroup.getChildMeasureSpec(r5, r10, r13)
                int r5 = r1.mMatchConstraintDefaultHeight
                r13 = 1
                if (r5 != r13) goto L_0x00d5
                r5 = 1
                goto L_0x00d6
            L_0x00d5:
                r5 = 0
            L_0x00d6:
                int r13 = r2.measureStrategy
                int r14 = androidx.constraintlayout.core.widgets.analyzer.BasicMeasure.Measure.TRY_GIVEN_DIMENSIONS
                if (r13 == r14) goto L_0x00e2
                int r13 = r2.measureStrategy
                int r14 = androidx.constraintlayout.core.widgets.analyzer.BasicMeasure.Measure.USE_GIVEN_DIMENSIONS
                if (r13 != r14) goto L_0x0137
            L_0x00e2:
                int r13 = r12.getMeasuredWidth()
                int r14 = r29.getWidth()
                if (r13 != r14) goto L_0x00ee
                r13 = 1
                goto L_0x00ef
            L_0x00ee:
                r13 = 0
            L_0x00ef:
                int r14 = r2.measureStrategy
                int r15 = androidx.constraintlayout.core.widgets.analyzer.BasicMeasure.Measure.USE_GIVEN_DIMENSIONS
                if (r14 == r15) goto L_0x0108
                if (r5 == 0) goto L_0x0108
                if (r5 == 0) goto L_0x00fb
                if (r13 != 0) goto L_0x0108
            L_0x00fb:
                boolean r14 = r12 instanceof androidx.constraintlayout.widget.Placeholder
                if (r14 != 0) goto L_0x0108
                boolean r14 = r29.isResolvedVertically()
                if (r14 == 0) goto L_0x0106
                goto L_0x0108
            L_0x0106:
                r14 = 0
                goto L_0x0109
            L_0x0108:
                r14 = 1
            L_0x0109:
                if (r14 == 0) goto L_0x0118
                int r15 = r29.getHeight()
                r16 = r5
                r5 = 1073741824(0x40000000, float:2.0)
                int r9 = android.view.View.MeasureSpec.makeMeasureSpec(r15, r5)
                goto L_0x0137
            L_0x0118:
                r16 = r5
                goto L_0x0137
            L_0x011b:
                int r5 = r0.layoutHeightSpec
                int r13 = r29.getVerticalMargin()
                int r13 = r13 + r10
                r14 = -1
                int r9 = android.view.ViewGroup.getChildMeasureSpec(r5, r13, r14)
                goto L_0x0137
            L_0x0128:
                int r5 = r0.layoutHeightSpec
                r13 = -2
                int r9 = android.view.ViewGroup.getChildMeasureSpec(r5, r10, r13)
                goto L_0x0137
            L_0x0130:
                r5 = 1073741824(0x40000000, float:2.0)
                int r9 = android.view.View.MeasureSpec.makeMeasureSpec(r7, r5)
            L_0x0137:
                androidx.constraintlayout.core.widgets.ConstraintWidget r5 = r29.getParent()
                androidx.constraintlayout.core.widgets.ConstraintWidgetContainer r5 = (androidx.constraintlayout.core.widgets.ConstraintWidgetContainer) r5
                if (r5 == 0) goto L_0x01b9
                androidx.constraintlayout.widget.ConstraintLayout r13 = androidx.constraintlayout.widget.ConstraintLayout.this
                int r13 = r13.mOptimizationLevel
                r14 = 256(0x100, float:3.59E-43)
                boolean r13 = androidx.constraintlayout.core.widgets.Optimizer.enabled(r13, r14)
                if (r13 == 0) goto L_0x01b9
                int r13 = r12.getMeasuredWidth()
                int r14 = r29.getWidth()
                if (r13 != r14) goto L_0x01b9
                int r13 = r12.getMeasuredWidth()
                int r14 = r5.getWidth()
                if (r13 >= r14) goto L_0x01b9
                int r13 = r12.getMeasuredHeight()
                int r14 = r29.getHeight()
                if (r13 != r14) goto L_0x01b9
                int r13 = r12.getMeasuredHeight()
                int r14 = r5.getHeight()
                if (r13 >= r14) goto L_0x01b9
                int r13 = r12.getBaseline()
                int r14 = r29.getBaselineDistance()
                if (r13 != r14) goto L_0x01b9
                boolean r13 = r29.isMeasureRequested()
                if (r13 != 0) goto L_0x01b9
                int r13 = r29.getLastHorizontalMeasureSpec()
                int r14 = r29.getWidth()
                boolean r13 = r0.isSimilarSpec(r13, r8, r14)
                if (r13 == 0) goto L_0x01a3
                int r13 = r29.getLastVerticalMeasureSpec()
                int r14 = r29.getHeight()
                boolean r13 = r0.isSimilarSpec(r13, r9, r14)
                if (r13 == 0) goto L_0x01a3
                r13 = 1
                goto L_0x01a4
            L_0x01a3:
                r13 = 0
            L_0x01a4:
                if (r13 == 0) goto L_0x01b9
                int r14 = r29.getWidth()
                r2.measuredWidth = r14
                int r14 = r29.getHeight()
                r2.measuredHeight = r14
                int r14 = r29.getBaselineDistance()
                r2.measuredBaseline = r14
                return
            L_0x01b9:
                androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r13 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
                if (r3 != r13) goto L_0x01bf
                r13 = 1
                goto L_0x01c0
            L_0x01bf:
                r13 = 0
            L_0x01c0:
                androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r14 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
                if (r4 != r14) goto L_0x01c6
                r14 = 1
                goto L_0x01c7
            L_0x01c6:
                r14 = 0
            L_0x01c7:
                androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r15 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_PARENT
                if (r4 == r15) goto L_0x01d2
                androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r15 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.FIXED
                if (r4 != r15) goto L_0x01d0
                goto L_0x01d2
            L_0x01d0:
                r15 = 0
                goto L_0x01d3
            L_0x01d2:
                r15 = 1
            L_0x01d3:
                r16 = r4
                androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r4 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_PARENT
                if (r3 == r4) goto L_0x01e0
                androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r4 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.FIXED
                if (r3 != r4) goto L_0x01de
                goto L_0x01e0
            L_0x01de:
                r4 = 0
                goto L_0x01e1
            L_0x01e0:
                r4 = 1
            L_0x01e1:
                r17 = 0
                if (r13 == 0) goto L_0x01ef
                r18 = r3
                float r3 = r1.mDimensionRatio
                int r3 = (r3 > r17 ? 1 : (r3 == r17 ? 0 : -1))
                if (r3 <= 0) goto L_0x01f1
                r3 = 1
                goto L_0x01f2
            L_0x01ef:
                r18 = r3
            L_0x01f1:
                r3 = 0
            L_0x01f2:
                if (r14 == 0) goto L_0x01fe
                r19 = r5
                float r5 = r1.mDimensionRatio
                int r5 = (r5 > r17 ? 1 : (r5 == r17 ? 0 : -1))
                if (r5 <= 0) goto L_0x0200
                r5 = 1
                goto L_0x0201
            L_0x01fe:
                r19 = r5
            L_0x0200:
                r5 = 0
            L_0x0201:
                if (r12 != 0) goto L_0x0204
                return
            L_0x0204:
                android.view.ViewGroup$LayoutParams r17 = r12.getLayoutParams()
                r20 = r6
                r6 = r17
                androidx.constraintlayout.widget.ConstraintLayout$LayoutParams r6 = (androidx.constraintlayout.widget.ConstraintLayout.LayoutParams) r6
                r17 = 0
                r21 = 0
                r22 = 0
                r23 = r7
                int r7 = r2.measureStrategy
                r24 = r10
                int r10 = androidx.constraintlayout.core.widgets.analyzer.BasicMeasure.Measure.TRY_GIVEN_DIMENSIONS
                if (r7 == r10) goto L_0x023d
                int r7 = r2.measureStrategy
                int r10 = androidx.constraintlayout.core.widgets.analyzer.BasicMeasure.Measure.USE_GIVEN_DIMENSIONS
                if (r7 == r10) goto L_0x023d
                if (r13 == 0) goto L_0x023d
                int r7 = r1.mMatchConstraintDefaultWidth
                if (r7 != 0) goto L_0x023d
                if (r14 == 0) goto L_0x023d
                int r7 = r1.mMatchConstraintDefaultHeight
                if (r7 == 0) goto L_0x0231
                goto L_0x023d
            L_0x0231:
                r27 = r3
                r25 = r11
                r0 = r17
                r11 = r21
                r3 = r22
                goto L_0x0313
            L_0x023d:
                boolean r7 = r12 instanceof androidx.constraintlayout.widget.VirtualLayout
                if (r7 == 0) goto L_0x024f
                boolean r7 = r1 instanceof androidx.constraintlayout.core.widgets.VirtualLayout
                if (r7 == 0) goto L_0x024f
                r7 = r1
                androidx.constraintlayout.core.widgets.VirtualLayout r7 = (androidx.constraintlayout.core.widgets.VirtualLayout) r7
                r10 = r12
                androidx.constraintlayout.widget.VirtualLayout r10 = (androidx.constraintlayout.widget.VirtualLayout) r10
                r10.onMeasure(r7, r8, r9)
                goto L_0x0252
            L_0x024f:
                r12.measure(r8, r9)
            L_0x0252:
                r1.setLastMeasureSpec(r8, r9)
                int r7 = r12.getMeasuredWidth()
                int r10 = r12.getMeasuredHeight()
                int r22 = r12.getBaseline()
                r25 = r7
                r17 = r10
                r21 = r8
                int r8 = r1.mMatchConstraintMinWidth
                if (r8 <= 0) goto L_0x0278
                int r8 = r1.mMatchConstraintMinWidth
                r26 = r9
                r9 = r25
                int r25 = java.lang.Math.max(r8, r9)
                r9 = r25
                goto L_0x027c
            L_0x0278:
                r26 = r9
                r9 = r25
            L_0x027c:
                int r8 = r1.mMatchConstraintMaxWidth
                if (r8 <= 0) goto L_0x0286
                int r8 = r1.mMatchConstraintMaxWidth
                int r9 = java.lang.Math.min(r8, r9)
            L_0x0286:
                int r8 = r1.mMatchConstraintMinHeight
                if (r8 <= 0) goto L_0x0297
                int r8 = r1.mMatchConstraintMinHeight
                r25 = r11
                r11 = r17
                int r17 = java.lang.Math.max(r8, r11)
                r11 = r17
                goto L_0x029b
            L_0x0297:
                r25 = r11
                r11 = r17
            L_0x029b:
                int r8 = r1.mMatchConstraintMaxHeight
                if (r8 <= 0) goto L_0x02a5
                int r8 = r1.mMatchConstraintMaxHeight
                int r11 = java.lang.Math.min(r8, r11)
            L_0x02a5:
                androidx.constraintlayout.widget.ConstraintLayout r8 = androidx.constraintlayout.widget.ConstraintLayout.this
                int r8 = r8.mOptimizationLevel
                r0 = 1
                boolean r8 = androidx.constraintlayout.core.widgets.Optimizer.enabled(r8, r0)
                if (r8 != 0) goto L_0x02d4
                if (r3 == 0) goto L_0x02c3
                if (r15 == 0) goto L_0x02c3
                float r0 = r1.mDimensionRatio
                r27 = r3
                float r3 = (float) r11
                float r3 = r3 * r0
                r17 = 1056964608(0x3f000000, float:0.5)
                float r3 = r3 + r17
                int r0 = (int) r3
                r9 = r0
                goto L_0x02d6
            L_0x02c3:
                r27 = r3
                if (r5 == 0) goto L_0x02d6
                if (r4 == 0) goto L_0x02d6
                float r0 = r1.mDimensionRatio
                float r3 = (float) r9
                float r3 = r3 / r0
                r17 = 1056964608(0x3f000000, float:0.5)
                float r3 = r3 + r17
                int r3 = (int) r3
                r11 = r3
                goto L_0x02d6
            L_0x02d4:
                r27 = r3
            L_0x02d6:
                if (r7 != r9) goto L_0x02e3
                if (r10 == r11) goto L_0x02db
                goto L_0x02e3
            L_0x02db:
                r0 = r9
                r8 = r21
                r3 = r22
                r9 = r26
                goto L_0x0313
            L_0x02e3:
                if (r7 == r9) goto L_0x02ec
                r0 = 1073741824(0x40000000, float:2.0)
                int r3 = android.view.View.MeasureSpec.makeMeasureSpec(r9, r0)
                goto L_0x02f0
            L_0x02ec:
                r0 = 1073741824(0x40000000, float:2.0)
                r3 = r21
            L_0x02f0:
                if (r10 == r11) goto L_0x02f7
                int r0 = android.view.View.MeasureSpec.makeMeasureSpec(r11, r0)
                goto L_0x02f9
            L_0x02f7:
                r0 = r26
            L_0x02f9:
                r12.measure(r3, r0)
                r1.setLastMeasureSpec(r3, r0)
                int r17 = r12.getMeasuredWidth()
                int r21 = r12.getMeasuredHeight()
                int r22 = r12.getBaseline()
                r9 = r0
                r8 = r3
                r0 = r17
                r11 = r21
                r3 = r22
            L_0x0313:
                r7 = -1
                if (r3 == r7) goto L_0x0318
                r7 = 1
                goto L_0x0319
            L_0x0318:
                r7 = 0
            L_0x0319:
                int r10 = r2.horizontalDimension
                if (r0 != r10) goto L_0x0324
                int r10 = r2.verticalDimension
                if (r11 == r10) goto L_0x0322
                goto L_0x0324
            L_0x0322:
                r10 = 0
                goto L_0x0325
            L_0x0324:
                r10 = 1
            L_0x0325:
                r2.measuredNeedsSolverPass = r10
                boolean r10 = r6.needsBaseline
                if (r10 == 0) goto L_0x032c
                r7 = 1
            L_0x032c:
                if (r7 == 0) goto L_0x033a
                r10 = -1
                if (r3 == r10) goto L_0x033a
                int r10 = r29.getBaselineDistance()
                if (r10 == r3) goto L_0x033a
                r10 = 1
                r2.measuredNeedsSolverPass = r10
            L_0x033a:
                r2.measuredWidth = r0
                r2.measuredHeight = r11
                r2.measuredHasBaseline = r7
                r2.measuredBaseline = r3
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.ConstraintLayout.Measurer.measure(androidx.constraintlayout.core.widgets.ConstraintWidget, androidx.constraintlayout.core.widgets.analyzer.BasicMeasure$Measure):void");
        }
    }

    public ConstraintLayout(Context context) {
        super(context);
        init((AttributeSet) null, 0, 0);
    }

    public ConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public ConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    public ConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    private int getPaddingWidth() {
        int max = Math.max(0, getPaddingLeft()) + Math.max(0, getPaddingRight());
        int i = 0;
        if (Build.VERSION.SDK_INT >= 17) {
            i = Math.max(0, getPaddingStart()) + Math.max(0, getPaddingEnd());
        }
        return i > 0 ? i : max;
    }

    public static SharedValues getSharedValues() {
        if (sSharedValues == null) {
            sSharedValues = new SharedValues();
        }
        return sSharedValues;
    }

    private final ConstraintWidget getTargetWidget(int id) {
        if (id == 0) {
            return this.mLayoutWidget;
        }
        View view = this.mChildrenByIds.get(id);
        if (view == null && (view = findViewById(id)) != null && view != this && view.getParent() == this) {
            onViewAdded(view);
        }
        if (view == this) {
            return this.mLayoutWidget;
        }
        if (view == null) {
            return null;
        }
        return ((LayoutParams) view.getLayoutParams()).widget;
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.mLayoutWidget.setCompanionWidget(this);
        this.mLayoutWidget.setMeasurer(this.mMeasurer);
        this.mChildrenByIds.put(getId(), this);
        this.mConstraintSet = null;
        if (attrs != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.ConstraintLayout_Layout, defStyleAttr, defStyleRes);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.ConstraintLayout_Layout_android_minWidth) {
                    this.mMinWidth = obtainStyledAttributes.getDimensionPixelOffset(index, this.mMinWidth);
                } else if (index == R.styleable.ConstraintLayout_Layout_android_minHeight) {
                    this.mMinHeight = obtainStyledAttributes.getDimensionPixelOffset(index, this.mMinHeight);
                } else if (index == R.styleable.ConstraintLayout_Layout_android_maxWidth) {
                    this.mMaxWidth = obtainStyledAttributes.getDimensionPixelOffset(index, this.mMaxWidth);
                } else if (index == R.styleable.ConstraintLayout_Layout_android_maxHeight) {
                    this.mMaxHeight = obtainStyledAttributes.getDimensionPixelOffset(index, this.mMaxHeight);
                } else if (index == R.styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
                    this.mOptimizationLevel = obtainStyledAttributes.getInt(index, this.mOptimizationLevel);
                } else if (index == R.styleable.ConstraintLayout_Layout_layoutDescription) {
                    int resourceId = obtainStyledAttributes.getResourceId(index, 0);
                    if (resourceId != 0) {
                        try {
                            parseLayoutDescription(resourceId);
                        } catch (Resources.NotFoundException e) {
                            this.mConstraintLayoutSpec = null;
                        }
                    }
                } else if (index == R.styleable.ConstraintLayout_Layout_constraintSet) {
                    int resourceId2 = obtainStyledAttributes.getResourceId(index, 0);
                    try {
                        ConstraintSet constraintSet = new ConstraintSet();
                        this.mConstraintSet = constraintSet;
                        constraintSet.load(getContext(), resourceId2);
                    } catch (Resources.NotFoundException e2) {
                        this.mConstraintSet = null;
                    }
                    this.mConstraintSetId = resourceId2;
                }
            }
            obtainStyledAttributes.recycle();
        }
        this.mLayoutWidget.setOptimizationLevel(this.mOptimizationLevel);
    }

    private void markHierarchyDirty() {
        this.mDirtyHierarchy = USE_CONSTRAINTS_HELPER;
        this.mLastMeasureWidth = -1;
        this.mLastMeasureHeight = -1;
        this.mLastMeasureWidthSize = -1;
        this.mLastMeasureHeightSize = -1;
        this.mLastMeasureWidthMode = 0;
        this.mLastMeasureHeightMode = 0;
    }

    private void setChildrenConstraints() {
        boolean isInEditMode = isInEditMode();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ConstraintWidget viewWidget = getViewWidget(getChildAt(i));
            if (viewWidget != null) {
                viewWidget.reset();
            }
        }
        if (isInEditMode) {
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = getChildAt(i2);
                try {
                    String resourceName = getResources().getResourceName(childAt.getId());
                    setDesignInformation(0, resourceName, Integer.valueOf(childAt.getId()));
                    int indexOf = resourceName.indexOf(47);
                    if (indexOf != -1) {
                        resourceName = resourceName.substring(indexOf + 1);
                    }
                    getTargetWidget(childAt.getId()).setDebugName(resourceName);
                } catch (Resources.NotFoundException e) {
                }
            }
        }
        if (this.mConstraintSetId != -1) {
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt2 = getChildAt(i3);
                if (childAt2.getId() == this.mConstraintSetId && (childAt2 instanceof Constraints)) {
                    this.mConstraintSet = ((Constraints) childAt2).getConstraintSet();
                }
            }
        }
        ConstraintSet constraintSet = this.mConstraintSet;
        if (constraintSet != null) {
            constraintSet.applyToInternal(this, USE_CONSTRAINTS_HELPER);
        }
        this.mLayoutWidget.removeAllChildren();
        int size = this.mConstraintHelpers.size();
        if (size > 0) {
            for (int i4 = 0; i4 < size; i4++) {
                this.mConstraintHelpers.get(i4).updatePreLayout(this);
            }
        }
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt3 = getChildAt(i5);
            if (childAt3 instanceof Placeholder) {
                ((Placeholder) childAt3).updatePreLayout(this);
            }
        }
        this.mTempMapIdToWidget.clear();
        this.mTempMapIdToWidget.put(0, this.mLayoutWidget);
        this.mTempMapIdToWidget.put(getId(), this.mLayoutWidget);
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt4 = getChildAt(i6);
            this.mTempMapIdToWidget.put(childAt4.getId(), getViewWidget(childAt4));
        }
        for (int i7 = 0; i7 < childCount; i7++) {
            View childAt5 = getChildAt(i7);
            ConstraintWidget viewWidget2 = getViewWidget(childAt5);
            if (viewWidget2 != null) {
                this.mLayoutWidget.add(viewWidget2);
                applyConstraintsFromLayoutParams(isInEditMode, childAt5, viewWidget2, (LayoutParams) childAt5.getLayoutParams(), this.mTempMapIdToWidget);
            }
        }
    }

    private void setWidgetBaseline(ConstraintWidget widget, LayoutParams layoutParams, SparseArray<ConstraintWidget> sparseArray, int baselineTarget, ConstraintAnchor.Type type) {
        View view = this.mChildrenByIds.get(baselineTarget);
        ConstraintWidget constraintWidget = sparseArray.get(baselineTarget);
        if (constraintWidget != null && view != null && (view.getLayoutParams() instanceof LayoutParams)) {
            layoutParams.needsBaseline = USE_CONSTRAINTS_HELPER;
            if (type == ConstraintAnchor.Type.BASELINE) {
                LayoutParams layoutParams2 = (LayoutParams) view.getLayoutParams();
                layoutParams2.needsBaseline = USE_CONSTRAINTS_HELPER;
                layoutParams2.widget.setHasBaseline(USE_CONSTRAINTS_HELPER);
            }
            widget.getAnchor(ConstraintAnchor.Type.BASELINE).connect(constraintWidget.getAnchor(type), layoutParams.baselineMargin, layoutParams.goneBaselineMargin, USE_CONSTRAINTS_HELPER);
            widget.setHasBaseline(USE_CONSTRAINTS_HELPER);
            widget.getAnchor(ConstraintAnchor.Type.TOP).reset();
            widget.getAnchor(ConstraintAnchor.Type.BOTTOM).reset();
        }
    }

    private boolean updateHierarchy() {
        int childCount = getChildCount();
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= childCount) {
                break;
            } else if (getChildAt(i).isLayoutRequested()) {
                z = USE_CONSTRAINTS_HELPER;
                break;
            } else {
                i++;
            }
        }
        if (z) {
            setChildrenConstraints();
        }
        return z;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00a8  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00b4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void applyConstraintsFromLayoutParams(boolean r22, android.view.View r23, androidx.constraintlayout.core.widgets.ConstraintWidget r24, androidx.constraintlayout.widget.ConstraintLayout.LayoutParams r25, android.util.SparseArray<androidx.constraintlayout.core.widgets.ConstraintWidget> r26) {
        /*
            r21 = this;
            r0 = r23
            r7 = r24
            r8 = r25
            r9 = r26
            r25.validate()
            r10 = 0
            r8.helped = r10
            int r1 = r23.getVisibility()
            r7.setVisibility(r1)
            boolean r1 = r8.isInPlaceholder
            if (r1 == 0) goto L_0x0022
            r1 = 1
            r7.setInPlaceholder(r1)
            r1 = 8
            r7.setVisibility(r1)
        L_0x0022:
            r7.setCompanionWidget(r0)
            boolean r1 = r0 instanceof androidx.constraintlayout.widget.ConstraintHelper
            if (r1 == 0) goto L_0x0038
            r1 = r0
            androidx.constraintlayout.widget.ConstraintHelper r1 = (androidx.constraintlayout.widget.ConstraintHelper) r1
            r11 = r21
            androidx.constraintlayout.core.widgets.ConstraintWidgetContainer r2 = r11.mLayoutWidget
            boolean r2 = r2.isRtl()
            r1.resolveRtl(r7, r2)
            goto L_0x003a
        L_0x0038:
            r11 = r21
        L_0x003a:
            boolean r1 = r8.isGuideline
            r2 = 17
            r12 = -1
            if (r1 == 0) goto L_0x006b
            r1 = r7
            androidx.constraintlayout.core.widgets.Guideline r1 = (androidx.constraintlayout.core.widgets.Guideline) r1
            int r3 = r8.resolvedGuideBegin
            int r4 = r8.resolvedGuideEnd
            float r5 = r8.resolvedGuidePercent
            int r6 = android.os.Build.VERSION.SDK_INT
            if (r6 >= r2) goto L_0x0054
            int r3 = r8.guideBegin
            int r4 = r8.guideEnd
            float r5 = r8.guidePercent
        L_0x0054:
            r2 = -1082130432(0xffffffffbf800000, float:-1.0)
            int r2 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1))
            if (r2 == 0) goto L_0x005e
            r1.setGuidePercent((float) r5)
            goto L_0x0069
        L_0x005e:
            if (r3 == r12) goto L_0x0064
            r1.setGuideBegin(r3)
            goto L_0x0069
        L_0x0064:
            if (r4 == r12) goto L_0x0069
            r1.setGuideEnd(r4)
        L_0x0069:
            goto L_0x0323
        L_0x006b:
            int r1 = r8.resolvedLeftToLeft
            int r3 = r8.resolvedLeftToRight
            int r4 = r8.resolvedRightToLeft
            int r5 = r8.resolvedRightToRight
            int r6 = r8.resolveGoneLeftMargin
            int r13 = r8.resolveGoneRightMargin
            float r14 = r8.resolvedHorizontalBias
            int r15 = android.os.Build.VERSION.SDK_INT
            if (r15 >= r2) goto L_0x00ce
            int r1 = r8.leftToLeft
            int r2 = r8.leftToRight
            int r4 = r8.rightToLeft
            int r5 = r8.rightToRight
            int r6 = r8.goneLeftMargin
            int r13 = r8.goneRightMargin
            float r14 = r8.horizontalBias
            if (r1 != r12) goto L_0x009f
            if (r2 != r12) goto L_0x009f
            int r3 = r8.startToStart
            if (r3 == r12) goto L_0x0097
            int r1 = r8.startToStart
            r3 = r2
            goto L_0x00a0
        L_0x0097:
            int r3 = r8.startToEnd
            if (r3 == r12) goto L_0x009f
            int r2 = r8.startToEnd
            r3 = r2
            goto L_0x00a0
        L_0x009f:
            r3 = r2
        L_0x00a0:
            if (r4 != r12) goto L_0x00c4
            if (r5 != r12) goto L_0x00c4
            int r2 = r8.endToStart
            if (r2 == r12) goto L_0x00b4
            int r4 = r8.endToStart
            r15 = r4
            r16 = r6
            r17 = r13
            r13 = r1
            r6 = r5
            r5 = r14
            r14 = r3
            goto L_0x00d7
        L_0x00b4:
            int r2 = r8.endToEnd
            if (r2 == r12) goto L_0x00c4
            int r5 = r8.endToEnd
            r15 = r4
            r16 = r6
            r17 = r13
            r13 = r1
            r6 = r5
            r5 = r14
            r14 = r3
            goto L_0x00d7
        L_0x00c4:
            r15 = r4
            r16 = r6
            r17 = r13
            r13 = r1
            r6 = r5
            r5 = r14
            r14 = r3
            goto L_0x00d7
        L_0x00ce:
            r15 = r4
            r16 = r6
            r17 = r13
            r13 = r1
            r6 = r5
            r5 = r14
            r14 = r3
        L_0x00d7:
            int r1 = r8.circleConstraint
            if (r1 == r12) goto L_0x00f1
            int r1 = r8.circleConstraint
            java.lang.Object r1 = r9.get(r1)
            androidx.constraintlayout.core.widgets.ConstraintWidget r1 = (androidx.constraintlayout.core.widgets.ConstraintWidget) r1
            if (r1 == 0) goto L_0x00ec
            float r2 = r8.circleAngle
            int r3 = r8.circleRadius
            r7.connectCircularConstraint(r1, r2, r3)
        L_0x00ec:
            r10 = r5
            r19 = r6
            goto L_0x024b
        L_0x00f1:
            if (r13 == r12) goto L_0x0118
            java.lang.Object r1 = r9.get(r13)
            r18 = r1
            androidx.constraintlayout.core.widgets.ConstraintWidget r18 = (androidx.constraintlayout.core.widgets.ConstraintWidget) r18
            if (r18 == 0) goto L_0x0114
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.LEFT
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.LEFT
            int r3 = r8.leftMargin
            r1 = r24
            r19 = r3
            r3 = r18
            r10 = r5
            r5 = r19
            r20 = r6
            r6 = r16
            r1.immediateConnect(r2, r3, r4, r5, r6)
            goto L_0x0137
        L_0x0114:
            r10 = r5
            r20 = r6
            goto L_0x0137
        L_0x0118:
            r10 = r5
            r20 = r6
            if (r14 == r12) goto L_0x0137
            java.lang.Object r1 = r9.get(r14)
            r18 = r1
            androidx.constraintlayout.core.widgets.ConstraintWidget r18 = (androidx.constraintlayout.core.widgets.ConstraintWidget) r18
            if (r18 == 0) goto L_0x0138
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.LEFT
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.RIGHT
            int r5 = r8.leftMargin
            r1 = r24
            r3 = r18
            r6 = r16
            r1.immediateConnect(r2, r3, r4, r5, r6)
            goto L_0x0138
        L_0x0137:
        L_0x0138:
            if (r15 == r12) goto L_0x0156
            java.lang.Object r1 = r9.get(r15)
            r18 = r1
            androidx.constraintlayout.core.widgets.ConstraintWidget r18 = (androidx.constraintlayout.core.widgets.ConstraintWidget) r18
            if (r18 == 0) goto L_0x0153
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.RIGHT
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.LEFT
            int r5 = r8.rightMargin
            r1 = r24
            r3 = r18
            r6 = r17
            r1.immediateConnect(r2, r3, r4, r5, r6)
        L_0x0153:
            r19 = r20
            goto L_0x017b
        L_0x0156:
            r6 = r20
            if (r6 == r12) goto L_0x0179
            java.lang.Object r1 = r9.get(r6)
            r18 = r1
            androidx.constraintlayout.core.widgets.ConstraintWidget r18 = (androidx.constraintlayout.core.widgets.ConstraintWidget) r18
            if (r18 == 0) goto L_0x0176
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.RIGHT
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.RIGHT
            int r5 = r8.rightMargin
            r1 = r24
            r3 = r18
            r19 = r6
            r6 = r17
            r1.immediateConnect(r2, r3, r4, r5, r6)
            goto L_0x017b
        L_0x0176:
            r19 = r6
            goto L_0x017b
        L_0x0179:
            r19 = r6
        L_0x017b:
            int r1 = r8.topToTop
            if (r1 == r12) goto L_0x019b
            int r1 = r8.topToTop
            java.lang.Object r1 = r9.get(r1)
            r18 = r1
            androidx.constraintlayout.core.widgets.ConstraintWidget r18 = (androidx.constraintlayout.core.widgets.ConstraintWidget) r18
            if (r18 == 0) goto L_0x01bb
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.TOP
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.TOP
            int r5 = r8.topMargin
            int r6 = r8.goneTopMargin
            r1 = r24
            r3 = r18
            r1.immediateConnect(r2, r3, r4, r5, r6)
            goto L_0x01bb
        L_0x019b:
            int r1 = r8.topToBottom
            if (r1 == r12) goto L_0x01bb
            int r1 = r8.topToBottom
            java.lang.Object r1 = r9.get(r1)
            r18 = r1
            androidx.constraintlayout.core.widgets.ConstraintWidget r18 = (androidx.constraintlayout.core.widgets.ConstraintWidget) r18
            if (r18 == 0) goto L_0x01bc
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.TOP
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.BOTTOM
            int r5 = r8.topMargin
            int r6 = r8.goneTopMargin
            r1 = r24
            r3 = r18
            r1.immediateConnect(r2, r3, r4, r5, r6)
            goto L_0x01bc
        L_0x01bb:
        L_0x01bc:
            int r1 = r8.bottomToTop
            if (r1 == r12) goto L_0x01dc
            int r1 = r8.bottomToTop
            java.lang.Object r1 = r9.get(r1)
            r18 = r1
            androidx.constraintlayout.core.widgets.ConstraintWidget r18 = (androidx.constraintlayout.core.widgets.ConstraintWidget) r18
            if (r18 == 0) goto L_0x01fc
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.BOTTOM
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.TOP
            int r5 = r8.bottomMargin
            int r6 = r8.goneBottomMargin
            r1 = r24
            r3 = r18
            r1.immediateConnect(r2, r3, r4, r5, r6)
            goto L_0x01fc
        L_0x01dc:
            int r1 = r8.bottomToBottom
            if (r1 == r12) goto L_0x01fc
            int r1 = r8.bottomToBottom
            java.lang.Object r1 = r9.get(r1)
            r18 = r1
            androidx.constraintlayout.core.widgets.ConstraintWidget r18 = (androidx.constraintlayout.core.widgets.ConstraintWidget) r18
            if (r18 == 0) goto L_0x01fd
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.BOTTOM
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.BOTTOM
            int r5 = r8.bottomMargin
            int r6 = r8.goneBottomMargin
            r1 = r24
            r3 = r18
            r1.immediateConnect(r2, r3, r4, r5, r6)
            goto L_0x01fd
        L_0x01fc:
        L_0x01fd:
            int r1 = r8.baselineToBaseline
            if (r1 == r12) goto L_0x0211
            int r5 = r8.baselineToBaseline
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r6 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.BASELINE
            r1 = r21
            r2 = r24
            r3 = r25
            r4 = r26
            r1.setWidgetBaseline(r2, r3, r4, r5, r6)
            goto L_0x0238
        L_0x0211:
            int r1 = r8.baselineToTop
            if (r1 == r12) goto L_0x0225
            int r5 = r8.baselineToTop
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r6 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.TOP
            r1 = r21
            r2 = r24
            r3 = r25
            r4 = r26
            r1.setWidgetBaseline(r2, r3, r4, r5, r6)
            goto L_0x0238
        L_0x0225:
            int r1 = r8.baselineToBottom
            if (r1 == r12) goto L_0x0238
            int r5 = r8.baselineToBottom
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r6 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.BOTTOM
            r1 = r21
            r2 = r24
            r3 = r25
            r4 = r26
            r1.setWidgetBaseline(r2, r3, r4, r5, r6)
        L_0x0238:
            r1 = 0
            int r2 = (r10 > r1 ? 1 : (r10 == r1 ? 0 : -1))
            if (r2 < 0) goto L_0x0240
            r7.setHorizontalBiasPercent(r10)
        L_0x0240:
            float r2 = r8.verticalBias
            int r1 = (r2 > r1 ? 1 : (r2 == r1 ? 0 : -1))
            if (r1 < 0) goto L_0x024b
            float r1 = r8.verticalBias
            r7.setVerticalBiasPercent(r1)
        L_0x024b:
            if (r22 == 0) goto L_0x025c
            int r1 = r8.editorAbsoluteX
            if (r1 != r12) goto L_0x0255
            int r1 = r8.editorAbsoluteY
            if (r1 == r12) goto L_0x025c
        L_0x0255:
            int r1 = r8.editorAbsoluteX
            int r2 = r8.editorAbsoluteY
            r7.setOrigin(r1, r2)
        L_0x025c:
            boolean r1 = r8.horizontalDimensionFixed
            r2 = -2
            if (r1 != 0) goto L_0x0293
            int r1 = r8.width
            if (r1 != r12) goto L_0x0289
            boolean r1 = r8.constrainedWidth
            if (r1 == 0) goto L_0x026f
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            r7.setHorizontalDimensionBehaviour(r1)
            goto L_0x0274
        L_0x026f:
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_PARENT
            r7.setHorizontalDimensionBehaviour(r1)
        L_0x0274:
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.LEFT
            androidx.constraintlayout.core.widgets.ConstraintAnchor r1 = r7.getAnchor(r1)
            int r3 = r8.leftMargin
            r1.mMargin = r3
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.RIGHT
            androidx.constraintlayout.core.widgets.ConstraintAnchor r1 = r7.getAnchor(r1)
            int r3 = r8.rightMargin
            r1.mMargin = r3
            goto L_0x02a6
        L_0x0289:
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            r7.setHorizontalDimensionBehaviour(r1)
            r1 = 0
            r7.setWidth(r1)
            goto L_0x02a6
        L_0x0293:
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            r7.setHorizontalDimensionBehaviour(r1)
            int r1 = r8.width
            r7.setWidth(r1)
            int r1 = r8.width
            if (r1 != r2) goto L_0x02a6
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            r7.setHorizontalDimensionBehaviour(r1)
        L_0x02a6:
            boolean r1 = r8.verticalDimensionFixed
            if (r1 != 0) goto L_0x02dc
            int r1 = r8.height
            if (r1 != r12) goto L_0x02d2
            boolean r1 = r8.constrainedHeight
            if (r1 == 0) goto L_0x02b8
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            r7.setVerticalDimensionBehaviour(r1)
            goto L_0x02bd
        L_0x02b8:
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_PARENT
            r7.setVerticalDimensionBehaviour(r1)
        L_0x02bd:
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.TOP
            androidx.constraintlayout.core.widgets.ConstraintAnchor r1 = r7.getAnchor(r1)
            int r2 = r8.topMargin
            r1.mMargin = r2
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.BOTTOM
            androidx.constraintlayout.core.widgets.ConstraintAnchor r1 = r7.getAnchor(r1)
            int r2 = r8.bottomMargin
            r1.mMargin = r2
            goto L_0x02ef
        L_0x02d2:
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            r7.setVerticalDimensionBehaviour(r1)
            r1 = 0
            r7.setHeight(r1)
            goto L_0x02ef
        L_0x02dc:
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            r7.setVerticalDimensionBehaviour(r1)
            int r1 = r8.height
            r7.setHeight(r1)
            int r1 = r8.height
            if (r1 != r2) goto L_0x02ef
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            r7.setVerticalDimensionBehaviour(r1)
        L_0x02ef:
            java.lang.String r1 = r8.dimensionRatio
            r7.setDimensionRatio(r1)
            float r1 = r8.horizontalWeight
            r7.setHorizontalWeight(r1)
            float r1 = r8.verticalWeight
            r7.setVerticalWeight(r1)
            int r1 = r8.horizontalChainStyle
            r7.setHorizontalChainStyle(r1)
            int r1 = r8.verticalChainStyle
            r7.setVerticalChainStyle(r1)
            int r1 = r8.wrapBehaviorInParent
            r7.setWrapBehaviorInParent(r1)
            int r1 = r8.matchConstraintDefaultWidth
            int r2 = r8.matchConstraintMinWidth
            int r3 = r8.matchConstraintMaxWidth
            float r4 = r8.matchConstraintPercentWidth
            r7.setHorizontalMatchStyle(r1, r2, r3, r4)
            int r1 = r8.matchConstraintDefaultHeight
            int r2 = r8.matchConstraintMinHeight
            int r3 = r8.matchConstraintMaxHeight
            float r4 = r8.matchConstraintPercentHeight
            r7.setVerticalMatchStyle(r1, r2, r3, r4)
        L_0x0323:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.ConstraintLayout.applyConstraintsFromLayoutParams(boolean, android.view.View, androidx.constraintlayout.core.widgets.ConstraintWidget, androidx.constraintlayout.widget.ConstraintLayout$LayoutParams, android.util.SparseArray):void");
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        float f;
        float f2;
        float f3;
        int size;
        ConstraintLayout constraintLayout = this;
        ArrayList<ConstraintHelper> arrayList = constraintLayout.mConstraintHelpers;
        if (arrayList != null && (size = arrayList.size()) > 0) {
            for (int i = 0; i < size; i++) {
                constraintLayout.mConstraintHelpers.get(i).updatePreDraw(constraintLayout);
            }
        }
        super.dispatchDraw(canvas);
        if (isInEditMode()) {
            float width = (float) getWidth();
            float height = (float) getHeight();
            float f4 = 1080.0f;
            int childCount = getChildCount();
            int i2 = 0;
            while (i2 < childCount) {
                View childAt = constraintLayout.getChildAt(i2);
                if (childAt.getVisibility() == 8) {
                    f3 = width;
                    f2 = height;
                    f = f4;
                } else {
                    Object tag = childAt.getTag();
                    if (tag == null || !(tag instanceof String)) {
                        f3 = width;
                        f2 = height;
                        f = f4;
                    } else {
                        String[] split = ((String) tag).split(",");
                        if (split.length == 4) {
                            int parseInt = Integer.parseInt(split[0]);
                            int parseInt2 = Integer.parseInt(split[1]);
                            int i3 = (int) ((((float) parseInt) / f4) * width);
                            int i4 = (int) ((((float) parseInt2) / 1920.0f) * height);
                            int parseInt3 = (int) ((((float) Integer.parseInt(split[2])) / f4) * width);
                            int parseInt4 = (int) ((((float) Integer.parseInt(split[3])) / 1920.0f) * height);
                            Paint paint = new Paint();
                            paint.setColor(SupportMenu.CATEGORY_MASK);
                            f3 = width;
                            f2 = height;
                            f = f4;
                            Canvas canvas2 = canvas;
                            Paint paint2 = paint;
                            canvas2.drawLine((float) i3, (float) i4, (float) (i3 + parseInt3), (float) i4, paint2);
                            canvas2.drawLine((float) (i3 + parseInt3), (float) i4, (float) (i3 + parseInt3), (float) (i4 + parseInt4), paint2);
                            canvas2.drawLine((float) (i3 + parseInt3), (float) (i4 + parseInt4), (float) i3, (float) (i4 + parseInt4), paint2);
                            canvas2.drawLine((float) i3, (float) (i4 + parseInt4), (float) i3, (float) i4, paint2);
                            paint.setColor(-16711936);
                            canvas2.drawLine((float) i3, (float) i4, (float) (i3 + parseInt3), (float) (i4 + parseInt4), paint2);
                            canvas2.drawLine((float) i3, (float) (i4 + parseInt4), (float) (i3 + parseInt3), (float) i4, paint2);
                        } else {
                            f3 = width;
                            f2 = height;
                            f = f4;
                        }
                    }
                }
                i2++;
                constraintLayout = this;
                width = f3;
                height = f2;
                f4 = f;
            }
            float f5 = width;
            float f6 = height;
            float f7 = f4;
        }
    }

    public void fillMetrics(Metrics metrics) {
        this.mMetrics = metrics;
        this.mLayoutWidget.fillMetrics(metrics);
    }

    public void forceLayout() {
        markHierarchyDirty();
        super.forceLayout();
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public Object getDesignInformation(int type, Object value) {
        if (type != 0 || !(value instanceof String)) {
            return null;
        }
        String str = (String) value;
        HashMap<String, Integer> hashMap = this.mDesignIds;
        if (hashMap == null || !hashMap.containsKey(str)) {
            return null;
        }
        return this.mDesignIds.get(str);
    }

    public int getMaxHeight() {
        return this.mMaxHeight;
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public int getOptimizationLevel() {
        return this.mLayoutWidget.getOptimizationLevel();
    }

    public String getSceneString() {
        int id;
        StringBuilder sb = new StringBuilder();
        if (this.mLayoutWidget.stringId == null) {
            int id2 = getId();
            if (id2 != -1) {
                this.mLayoutWidget.stringId = getContext().getResources().getResourceEntryName(id2);
            } else {
                this.mLayoutWidget.stringId = "parent";
            }
        }
        if (this.mLayoutWidget.getDebugName() == null) {
            ConstraintWidgetContainer constraintWidgetContainer = this.mLayoutWidget;
            constraintWidgetContainer.setDebugName(constraintWidgetContainer.stringId);
            Log.v(TAG, " setDebugName " + this.mLayoutWidget.getDebugName());
        }
        Iterator<ConstraintWidget> it = this.mLayoutWidget.getChildren().iterator();
        while (it.hasNext()) {
            ConstraintWidget next = it.next();
            View view = (View) next.getCompanionWidget();
            if (view != null) {
                if (next.stringId == null && (id = view.getId()) != -1) {
                    next.stringId = getContext().getResources().getResourceEntryName(id);
                }
                if (next.getDebugName() == null) {
                    next.setDebugName(next.stringId);
                    Log.v(TAG, " setDebugName " + next.getDebugName());
                }
            }
        }
        this.mLayoutWidget.getSceneString(sb);
        return sb.toString();
    }

    public View getViewById(int id) {
        return this.mChildrenByIds.get(id);
    }

    public final ConstraintWidget getViewWidget(View view) {
        if (view == this) {
            return this.mLayoutWidget;
        }
        if (view == null) {
            return null;
        }
        if (view.getLayoutParams() instanceof LayoutParams) {
            return ((LayoutParams) view.getLayoutParams()).widget;
        }
        view.setLayoutParams(generateLayoutParams(view.getLayoutParams()));
        if (view.getLayoutParams() instanceof LayoutParams) {
            return ((LayoutParams) view.getLayoutParams()).widget;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public boolean isRtl() {
        if (Build.VERSION.SDK_INT < 17) {
            return false;
        }
        if (!((getContext().getApplicationInfo().flags & 4194304) != 0) || 1 != getLayoutDirection()) {
            return false;
        }
        return USE_CONSTRAINTS_HELPER;
    }

    public void loadLayoutDescription(int layoutDescription) {
        if (layoutDescription != 0) {
            try {
                this.mConstraintLayoutSpec = new ConstraintLayoutStates(getContext(), this, layoutDescription);
            } catch (Resources.NotFoundException e) {
                this.mConstraintLayoutSpec = null;
            }
        } else {
            this.mConstraintLayoutSpec = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        View content;
        int childCount = getChildCount();
        boolean isInEditMode = isInEditMode();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            ConstraintWidget constraintWidget = layoutParams.widget;
            if ((childAt.getVisibility() != 8 || layoutParams.isGuideline || layoutParams.isHelper || layoutParams.isVirtualGroup || isInEditMode) && !layoutParams.isInPlaceholder) {
                int x = constraintWidget.getX();
                int y = constraintWidget.getY();
                int width = constraintWidget.getWidth() + x;
                int height = constraintWidget.getHeight() + y;
                childAt.layout(x, y, width, height);
                if ((childAt instanceof Placeholder) && (content = ((Placeholder) childAt).getContent()) != null) {
                    content.setVisibility(0);
                    content.layout(x, y, width, height);
                }
            }
        }
        int size = this.mConstraintHelpers.size();
        if (size > 0) {
            for (int i2 = 0; i2 < size; i2++) {
                this.mConstraintHelpers.get(i2).updatePostLayout(this);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mOnMeasureWidthMeasureSpec == widthMeasureSpec && this.mOnMeasureHeightMeasureSpec == heightMeasureSpec) {
        }
        if (!this.mDirtyHierarchy && 0 == 0) {
            int childCount = getChildCount();
            int i = 0;
            while (true) {
                if (i >= childCount) {
                    break;
                } else if (getChildAt(i).isLayoutRequested()) {
                    this.mDirtyHierarchy = USE_CONSTRAINTS_HELPER;
                    break;
                } else {
                    i++;
                }
            }
        }
        if (this.mDirtyHierarchy || 0 == 0) {
            this.mOnMeasureWidthMeasureSpec = widthMeasureSpec;
            this.mOnMeasureHeightMeasureSpec = heightMeasureSpec;
            this.mLayoutWidget.setRtl(isRtl());
            if (this.mDirtyHierarchy) {
                this.mDirtyHierarchy = false;
                if (updateHierarchy()) {
                    this.mLayoutWidget.updateHierarchy();
                }
            }
            resolveSystem(this.mLayoutWidget, this.mOptimizationLevel, widthMeasureSpec, heightMeasureSpec);
            resolveMeasuredDimension(widthMeasureSpec, heightMeasureSpec, this.mLayoutWidget.getWidth(), this.mLayoutWidget.getHeight(), this.mLayoutWidget.isWidthMeasuredTooSmall(), this.mLayoutWidget.isHeightMeasuredTooSmall());
            return;
        }
        resolveMeasuredDimension(widthMeasureSpec, heightMeasureSpec, this.mLayoutWidget.getWidth(), this.mLayoutWidget.getHeight(), this.mLayoutWidget.isWidthMeasuredTooSmall(), this.mLayoutWidget.isHeightMeasuredTooSmall());
    }

    public void onViewAdded(View view) {
        super.onViewAdded(view);
        ConstraintWidget viewWidget = getViewWidget(view);
        if ((view instanceof Guideline) && !(viewWidget instanceof Guideline)) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            layoutParams.widget = new Guideline();
            layoutParams.isGuideline = USE_CONSTRAINTS_HELPER;
            ((Guideline) layoutParams.widget).setOrientation(layoutParams.orientation);
        }
        if (view instanceof ConstraintHelper) {
            ConstraintHelper constraintHelper = (ConstraintHelper) view;
            constraintHelper.validateParams();
            ((LayoutParams) view.getLayoutParams()).isHelper = USE_CONSTRAINTS_HELPER;
            if (!this.mConstraintHelpers.contains(constraintHelper)) {
                this.mConstraintHelpers.add(constraintHelper);
            }
        }
        this.mChildrenByIds.put(view.getId(), view);
        this.mDirtyHierarchy = USE_CONSTRAINTS_HELPER;
    }

    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        this.mChildrenByIds.remove(view.getId());
        this.mLayoutWidget.remove(getViewWidget(view));
        this.mConstraintHelpers.remove(view);
        this.mDirtyHierarchy = USE_CONSTRAINTS_HELPER;
    }

    /* access modifiers changed from: protected */
    public void parseLayoutDescription(int id) {
        this.mConstraintLayoutSpec = new ConstraintLayoutStates(getContext(), this, id);
    }

    public void requestLayout() {
        markHierarchyDirty();
        super.requestLayout();
    }

    /* access modifiers changed from: protected */
    public void resolveMeasuredDimension(int widthMeasureSpec, int heightMeasureSpec, int measuredWidth, int measuredHeight, boolean isWidthMeasuredTooSmall, boolean isHeightMeasuredTooSmall) {
        int i = this.mMeasurer.paddingHeight;
        int resolveSizeAndState = resolveSizeAndState(measuredWidth + this.mMeasurer.paddingWidth, widthMeasureSpec, 0);
        int resolveSizeAndState2 = resolveSizeAndState(measuredHeight + i, heightMeasureSpec, 0 << 16);
        int i2 = resolveSizeAndState & ViewCompat.MEASURED_SIZE_MASK;
        int i3 = resolveSizeAndState2 & ViewCompat.MEASURED_SIZE_MASK;
        int min = Math.min(this.mMaxWidth, i2);
        int min2 = Math.min(this.mMaxHeight, i3);
        if (isWidthMeasuredTooSmall) {
            min |= 16777216;
        }
        if (isHeightMeasuredTooSmall) {
            min2 |= 16777216;
        }
        setMeasuredDimension(min, min2);
        this.mLastMeasureWidth = min;
        this.mLastMeasureHeight = min2;
    }

    /* access modifiers changed from: protected */
    public void resolveSystem(ConstraintWidgetContainer layout, int optimizationLevel, int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int mode = View.MeasureSpec.getMode(widthMeasureSpec);
        int size = View.MeasureSpec.getSize(widthMeasureSpec);
        int mode2 = View.MeasureSpec.getMode(heightMeasureSpec);
        int size2 = View.MeasureSpec.getSize(heightMeasureSpec);
        int max = Math.max(0, getPaddingTop());
        int max2 = Math.max(0, getPaddingBottom());
        int i2 = max + max2;
        int paddingWidth = getPaddingWidth();
        this.mMeasurer.captureLayoutInfo(widthMeasureSpec, heightMeasureSpec, max, max2, paddingWidth, i2);
        if (Build.VERSION.SDK_INT >= 17) {
            int max3 = Math.max(0, getPaddingStart());
            int max4 = Math.max(0, getPaddingEnd());
            i = (max3 > 0 || max4 > 0) ? isRtl() ? max4 : max3 : Math.max(0, getPaddingLeft());
        } else {
            i = Math.max(0, getPaddingLeft());
        }
        int i3 = size - paddingWidth;
        int i4 = size2 - i2;
        setSelfDimensionBehaviour(layout, mode, i3, mode2, i4);
        layout.measure(optimizationLevel, mode, i3, mode2, i4, this.mLastMeasureWidth, this.mLastMeasureHeight, i, max);
    }

    public void setConstraintSet(ConstraintSet set) {
        this.mConstraintSet = set;
    }

    public void setDesignInformation(int type, Object value1, Object value2) {
        if (type == 0 && (value1 instanceof String) && (value2 instanceof Integer)) {
            if (this.mDesignIds == null) {
                this.mDesignIds = new HashMap<>();
            }
            String str = (String) value1;
            int indexOf = str.indexOf("/");
            if (indexOf != -1) {
                str = str.substring(indexOf + 1);
            }
            this.mDesignIds.put(str, Integer.valueOf(((Integer) value2).intValue()));
        }
    }

    public void setId(int id) {
        this.mChildrenByIds.remove(getId());
        super.setId(id);
        this.mChildrenByIds.put(getId(), this);
    }

    public void setMaxHeight(int value) {
        if (value != this.mMaxHeight) {
            this.mMaxHeight = value;
            requestLayout();
        }
    }

    public void setMaxWidth(int value) {
        if (value != this.mMaxWidth) {
            this.mMaxWidth = value;
            requestLayout();
        }
    }

    public void setMinHeight(int value) {
        if (value != this.mMinHeight) {
            this.mMinHeight = value;
            requestLayout();
        }
    }

    public void setMinWidth(int value) {
        if (value != this.mMinWidth) {
            this.mMinWidth = value;
            requestLayout();
        }
    }

    public void setOnConstraintsChanged(ConstraintsChangedListener constraintsChangedListener) {
        this.mConstraintsChangedListener = constraintsChangedListener;
        ConstraintLayoutStates constraintLayoutStates = this.mConstraintLayoutSpec;
        if (constraintLayoutStates != null) {
            constraintLayoutStates.setOnConstraintsChanged(constraintsChangedListener);
        }
    }

    public void setOptimizationLevel(int level) {
        this.mOptimizationLevel = level;
        this.mLayoutWidget.setOptimizationLevel(level);
    }

    /* access modifiers changed from: protected */
    public void setSelfDimensionBehaviour(ConstraintWidgetContainer layout, int widthMode, int widthSize, int heightMode, int heightSize) {
        int i = this.mMeasurer.paddingHeight;
        int i2 = this.mMeasurer.paddingWidth;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.FIXED;
        int i3 = 0;
        int i4 = 0;
        int childCount = getChildCount();
        switch (widthMode) {
            case Integer.MIN_VALUE:
                dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                i3 = widthSize;
                if (childCount == 0) {
                    i3 = Math.max(0, this.mMinWidth);
                    break;
                }
                break;
            case 0:
                dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                if (childCount == 0) {
                    i3 = Math.max(0, this.mMinWidth);
                    break;
                }
                break;
            case BasicMeasure.EXACTLY:
                i3 = Math.min(this.mMaxWidth - i2, widthSize);
                break;
        }
        switch (heightMode) {
            case Integer.MIN_VALUE:
                dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                i4 = heightSize;
                if (childCount == 0) {
                    i4 = Math.max(0, this.mMinHeight);
                    break;
                }
                break;
            case 0:
                dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                if (childCount == 0) {
                    i4 = Math.max(0, this.mMinHeight);
                    break;
                }
                break;
            case BasicMeasure.EXACTLY:
                i4 = Math.min(this.mMaxHeight - i, heightSize);
                break;
        }
        if (!(i3 == layout.getWidth() && i4 == layout.getHeight())) {
            layout.invalidateMeasures();
        }
        layout.setX(0);
        layout.setY(0);
        layout.setMaxWidth(this.mMaxWidth - i2);
        layout.setMaxHeight(this.mMaxHeight - i);
        layout.setMinWidth(0);
        layout.setMinHeight(0);
        layout.setHorizontalDimensionBehaviour(dimensionBehaviour);
        layout.setWidth(i3);
        layout.setVerticalDimensionBehaviour(dimensionBehaviour2);
        layout.setHeight(i4);
        layout.setMinWidth(this.mMinWidth - i2);
        layout.setMinHeight(this.mMinHeight - i);
    }

    public void setState(int id, int screenWidth, int screenHeight) {
        ConstraintLayoutStates constraintLayoutStates = this.mConstraintLayoutSpec;
        if (constraintLayoutStates != null) {
            constraintLayoutStates.updateConstraints(id, (float) screenWidth, (float) screenHeight);
        }
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }
}
