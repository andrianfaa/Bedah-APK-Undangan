package androidx.constraintlayout.core.widgets;

import androidx.constraintlayout.core.Cache;
import androidx.constraintlayout.core.LinearSystem;
import androidx.constraintlayout.core.SolverVariable;
import androidx.constraintlayout.core.state.WidgetFrame;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.analyzer.ChainRun;
import androidx.constraintlayout.core.widgets.analyzer.HorizontalWidgetRun;
import androidx.constraintlayout.core.widgets.analyzer.VerticalWidgetRun;
import androidx.constraintlayout.core.widgets.analyzer.WidgetRun;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import okhttp3.HttpUrl;

public class ConstraintWidget {
    public static final int ANCHOR_BASELINE = 4;
    public static final int ANCHOR_BOTTOM = 3;
    public static final int ANCHOR_LEFT = 0;
    public static final int ANCHOR_RIGHT = 1;
    public static final int ANCHOR_TOP = 2;
    private static final boolean AUTOTAG_CENTER = false;
    public static final int BOTH = 2;
    public static final int CHAIN_PACKED = 2;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    public static float DEFAULT_BIAS = 0.5f;
    static final int DIMENSION_HORIZONTAL = 0;
    static final int DIMENSION_VERTICAL = 1;
    protected static final int DIRECT = 2;
    public static final int GONE = 8;
    public static final int HORIZONTAL = 0;
    public static final int INVISIBLE = 4;
    public static final int MATCH_CONSTRAINT_PERCENT = 2;
    public static final int MATCH_CONSTRAINT_RATIO = 3;
    public static final int MATCH_CONSTRAINT_RATIO_RESOLVED = 4;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    protected static final int SOLVER = 1;
    public static final int UNKNOWN = -1;
    private static final boolean USE_WRAP_DIMENSION_FOR_SPREAD = false;
    public static final int VERTICAL = 1;
    public static final int VISIBLE = 0;
    private static final int WRAP = -2;
    public static final int WRAP_BEHAVIOR_HORIZONTAL_ONLY = 1;
    public static final int WRAP_BEHAVIOR_INCLUDED = 0;
    public static final int WRAP_BEHAVIOR_SKIPPED = 3;
    public static final int WRAP_BEHAVIOR_VERTICAL_ONLY = 2;
    private boolean OPTIMIZE_WRAP;
    private boolean OPTIMIZE_WRAP_ON_RESOLVED;
    public WidgetFrame frame;
    private boolean hasBaseline;
    public ChainRun horizontalChainRun;
    public int horizontalGroup;
    public HorizontalWidgetRun horizontalRun;
    private boolean horizontalSolvingPass;
    private boolean inPlaceholder;
    public boolean[] isTerminalWidget;
    protected ArrayList<ConstraintAnchor> mAnchors;
    private boolean mAnimated;
    public ConstraintAnchor mBaseline;
    int mBaselineDistance;
    public ConstraintAnchor mBottom;
    boolean mBottomHasCentered;
    public ConstraintAnchor mCenter;
    ConstraintAnchor mCenterX;
    ConstraintAnchor mCenterY;
    private float mCircleConstraintAngle;
    private Object mCompanionWidget;
    private int mContainerItemSkip;
    private String mDebugName;
    public float mDimensionRatio;
    protected int mDimensionRatioSide;
    int mDistToBottom;
    int mDistToLeft;
    int mDistToRight;
    int mDistToTop;
    boolean mGroupsToSolver;
    int mHeight;
    private int mHeightOverride;
    float mHorizontalBiasPercent;
    boolean mHorizontalChainFixedPosition;
    int mHorizontalChainStyle;
    ConstraintWidget mHorizontalNextWidget;
    public int mHorizontalResolution;
    boolean mHorizontalWrapVisited;
    private boolean mInVirtualLayout;
    public boolean mIsHeightWrapContent;
    private boolean[] mIsInBarrier;
    public boolean mIsWidthWrapContent;
    private int mLastHorizontalMeasureSpec;
    private int mLastVerticalMeasureSpec;
    public ConstraintAnchor mLeft;
    boolean mLeftHasCentered;
    public ConstraintAnchor[] mListAnchors;
    public DimensionBehaviour[] mListDimensionBehaviors;
    protected ConstraintWidget[] mListNextMatchConstraintsWidget;
    public int mMatchConstraintDefaultHeight;
    public int mMatchConstraintDefaultWidth;
    public int mMatchConstraintMaxHeight;
    public int mMatchConstraintMaxWidth;
    public int mMatchConstraintMinHeight;
    public int mMatchConstraintMinWidth;
    public float mMatchConstraintPercentHeight;
    public float mMatchConstraintPercentWidth;
    private int[] mMaxDimension;
    private boolean mMeasureRequested;
    protected int mMinHeight;
    protected int mMinWidth;
    protected ConstraintWidget[] mNextChainWidget;
    protected int mOffsetX;
    protected int mOffsetY;
    public ConstraintWidget mParent;
    int mRelX;
    int mRelY;
    float mResolvedDimensionRatio;
    int mResolvedDimensionRatioSide;
    boolean mResolvedHasRatio;
    public int[] mResolvedMatchConstraintDefault;
    public ConstraintAnchor mRight;
    boolean mRightHasCentered;
    public ConstraintAnchor mTop;
    boolean mTopHasCentered;
    private String mType;
    float mVerticalBiasPercent;
    boolean mVerticalChainFixedPosition;
    int mVerticalChainStyle;
    ConstraintWidget mVerticalNextWidget;
    public int mVerticalResolution;
    boolean mVerticalWrapVisited;
    private int mVisibility;
    public float[] mWeight;
    int mWidth;
    private int mWidthOverride;
    private int mWrapBehaviorInParent;
    protected int mX;
    protected int mY;
    public boolean measured;
    private boolean resolvedHorizontal;
    private boolean resolvedVertical;
    public WidgetRun[] run;
    public String stringId;
    public ChainRun verticalChainRun;
    public int verticalGroup;
    public VerticalWidgetRun verticalRun;
    private boolean verticalSolvingPass;

    /* renamed from: androidx.constraintlayout.core.widgets.ConstraintWidget$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type;
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintWidget$DimensionBehaviour;

        static {
            int[] iArr = new int[DimensionBehaviour.values().length];
            $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintWidget$DimensionBehaviour = iArr;
            try {
                iArr[DimensionBehaviour.FIXED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintWidget$DimensionBehaviour[DimensionBehaviour.WRAP_CONTENT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintWidget$DimensionBehaviour[DimensionBehaviour.MATCH_PARENT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintWidget$DimensionBehaviour[DimensionBehaviour.MATCH_CONSTRAINT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            int[] iArr2 = new int[ConstraintAnchor.Type.values().length];
            $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type = iArr2;
            try {
                iArr2[ConstraintAnchor.Type.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.TOP.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.BOTTOM.ordinal()] = 4;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.BASELINE.ordinal()] = 5;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.CENTER.ordinal()] = 6;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.CENTER_X.ordinal()] = 7;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.CENTER_Y.ordinal()] = 8;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.NONE.ordinal()] = 9;
            } catch (NoSuchFieldError e13) {
            }
        }
    }

    public enum DimensionBehaviour {
        FIXED,
        WRAP_CONTENT,
        MATCH_CONSTRAINT,
        MATCH_PARENT
    }

    public ConstraintWidget() {
        this.measured = false;
        this.run = new WidgetRun[2];
        this.horizontalRun = null;
        this.verticalRun = null;
        this.isTerminalWidget = new boolean[]{true, true};
        this.mResolvedHasRatio = false;
        this.mMeasureRequested = true;
        this.OPTIMIZE_WRAP = false;
        this.OPTIMIZE_WRAP_ON_RESOLVED = true;
        this.mWidthOverride = -1;
        this.mHeightOverride = -1;
        this.frame = new WidgetFrame(this);
        this.resolvedHorizontal = false;
        this.resolvedVertical = false;
        this.horizontalSolvingPass = false;
        this.verticalSolvingPass = false;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mWrapBehaviorInParent = 0;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mMaxDimension = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
        this.mCircleConstraintAngle = 0.0f;
        this.hasBaseline = false;
        this.mInVirtualLayout = false;
        this.mLastHorizontalMeasureSpec = 0;
        this.mLastVerticalMeasureSpec = 0;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mCenter = constraintAnchor;
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor};
        this.mAnchors = new ArrayList<>();
        this.mIsInBarrier = new boolean[2];
        this.mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mAnimated = false;
        this.mDebugName = null;
        this.mType = null;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.horizontalGroup = -1;
        this.verticalGroup = -1;
        addAnchors();
    }

    public ConstraintWidget(int width, int height) {
        this(0, 0, width, height);
    }

    public ConstraintWidget(int x, int y, int width, int height) {
        this.measured = false;
        this.run = new WidgetRun[2];
        this.horizontalRun = null;
        this.verticalRun = null;
        this.isTerminalWidget = new boolean[]{true, true};
        this.mResolvedHasRatio = false;
        this.mMeasureRequested = true;
        this.OPTIMIZE_WRAP = false;
        this.OPTIMIZE_WRAP_ON_RESOLVED = true;
        this.mWidthOverride = -1;
        this.mHeightOverride = -1;
        this.frame = new WidgetFrame(this);
        this.resolvedHorizontal = false;
        this.resolvedVertical = false;
        this.horizontalSolvingPass = false;
        this.verticalSolvingPass = false;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mWrapBehaviorInParent = 0;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mMaxDimension = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
        this.mCircleConstraintAngle = 0.0f;
        this.hasBaseline = false;
        this.mInVirtualLayout = false;
        this.mLastHorizontalMeasureSpec = 0;
        this.mLastVerticalMeasureSpec = 0;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mCenter = constraintAnchor;
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor};
        this.mAnchors = new ArrayList<>();
        this.mIsInBarrier = new boolean[2];
        this.mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mAnimated = false;
        this.mDebugName = null;
        this.mType = null;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.horizontalGroup = -1;
        this.verticalGroup = -1;
        this.mX = x;
        this.mY = y;
        this.mWidth = width;
        this.mHeight = height;
        addAnchors();
    }

    public ConstraintWidget(String debugName) {
        this.measured = false;
        this.run = new WidgetRun[2];
        this.horizontalRun = null;
        this.verticalRun = null;
        this.isTerminalWidget = new boolean[]{true, true};
        this.mResolvedHasRatio = false;
        this.mMeasureRequested = true;
        this.OPTIMIZE_WRAP = false;
        this.OPTIMIZE_WRAP_ON_RESOLVED = true;
        this.mWidthOverride = -1;
        this.mHeightOverride = -1;
        this.frame = new WidgetFrame(this);
        this.resolvedHorizontal = false;
        this.resolvedVertical = false;
        this.horizontalSolvingPass = false;
        this.verticalSolvingPass = false;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mWrapBehaviorInParent = 0;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mMaxDimension = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
        this.mCircleConstraintAngle = 0.0f;
        this.hasBaseline = false;
        this.mInVirtualLayout = false;
        this.mLastHorizontalMeasureSpec = 0;
        this.mLastVerticalMeasureSpec = 0;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mCenter = constraintAnchor;
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor};
        this.mAnchors = new ArrayList<>();
        this.mIsInBarrier = new boolean[2];
        this.mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mAnimated = false;
        this.mDebugName = null;
        this.mType = null;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.horizontalGroup = -1;
        this.verticalGroup = -1;
        addAnchors();
        setDebugName(debugName);
    }

    public ConstraintWidget(String debugName, int width, int height) {
        this(width, height);
        setDebugName(debugName);
    }

    public ConstraintWidget(String debugName, int x, int y, int width, int height) {
        this(x, y, width, height);
        setDebugName(debugName);
    }

    private void addAnchors() {
        this.mAnchors.add(this.mLeft);
        this.mAnchors.add(this.mTop);
        this.mAnchors.add(this.mRight);
        this.mAnchors.add(this.mBottom);
        this.mAnchors.add(this.mCenterX);
        this.mAnchors.add(this.mCenterY);
        this.mAnchors.add(this.mCenter);
        this.mAnchors.add(this.mBaseline);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:263:0x060c, code lost:
        if ((r3 instanceof androidx.constraintlayout.core.widgets.Barrier) != false) goto L_0x0611;
     */
    /* JADX WARNING: Removed duplicated region for block: B:240:0x0596  */
    /* JADX WARNING: Removed duplicated region for block: B:250:0x05dd  */
    /* JADX WARNING: Removed duplicated region for block: B:255:0x05fb A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:256:0x05fc  */
    /* JADX WARNING: Removed duplicated region for block: B:331:0x06db A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void applyConstraints(androidx.constraintlayout.core.LinearSystem r38, boolean r39, boolean r40, boolean r41, boolean r42, androidx.constraintlayout.core.SolverVariable r43, androidx.constraintlayout.core.SolverVariable r44, androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour r45, boolean r46, androidx.constraintlayout.core.widgets.ConstraintAnchor r47, androidx.constraintlayout.core.widgets.ConstraintAnchor r48, int r49, int r50, int r51, int r52, float r53, boolean r54, boolean r55, boolean r56, boolean r57, boolean r58, int r59, int r60, int r61, int r62, float r63, boolean r64) {
        /*
            r37 = this;
            r0 = r37
            r10 = r38
            r11 = r43
            r12 = r44
            r13 = r47
            r14 = r48
            r15 = r51
            r9 = r52
            r8 = r60
            r1 = r61
            r2 = r62
            androidx.constraintlayout.core.SolverVariable r7 = r10.createObjectVariable(r13)
            androidx.constraintlayout.core.SolverVariable r6 = r10.createObjectVariable(r14)
            androidx.constraintlayout.core.widgets.ConstraintAnchor r3 = r47.getTarget()
            androidx.constraintlayout.core.SolverVariable r5 = r10.createObjectVariable(r3)
            androidx.constraintlayout.core.widgets.ConstraintAnchor r3 = r48.getTarget()
            androidx.constraintlayout.core.SolverVariable r4 = r10.createObjectVariable(r3)
            androidx.constraintlayout.core.Metrics r3 = androidx.constraintlayout.core.LinearSystem.getMetrics()
            if (r3 == 0) goto L_0x0040
            androidx.constraintlayout.core.Metrics r3 = androidx.constraintlayout.core.LinearSystem.getMetrics()
            long r11 = r3.nonresolvedWidgets
            r16 = 1
            long r11 = r11 + r16
            r3.nonresolvedWidgets = r11
        L_0x0040:
            boolean r11 = r47.isConnected()
            boolean r12 = r48.isConnected()
            androidx.constraintlayout.core.widgets.ConstraintAnchor r3 = r0.mCenter
            boolean r16 = r3.isConnected()
            r3 = 0
            r17 = 0
            if (r11 == 0) goto L_0x0055
            int r17 = r17 + 1
        L_0x0055:
            if (r12 == 0) goto L_0x0059
            int r17 = r17 + 1
        L_0x0059:
            if (r16 == 0) goto L_0x0060
            int r17 = r17 + 1
            r8 = r17
            goto L_0x0062
        L_0x0060:
            r8 = r17
        L_0x0062:
            if (r54 == 0) goto L_0x0069
            r17 = 3
            r14 = r17
            goto L_0x006b
        L_0x0069:
            r14 = r59
        L_0x006b:
            int[] r17 = androidx.constraintlayout.core.widgets.ConstraintWidget.AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$widgets$ConstraintWidget$DimensionBehaviour
            int r18 = r45.ordinal()
            r17 = r17[r18]
            switch(r17) {
                case 1: goto L_0x0083;
                case 2: goto L_0x0081;
                case 3: goto L_0x007f;
                case 4: goto L_0x0077;
                default: goto L_0x0076;
            }
        L_0x0076:
            goto L_0x0085
        L_0x0077:
            r13 = 4
            if (r14 == r13) goto L_0x007c
            r13 = 1
            goto L_0x007d
        L_0x007c:
            r13 = 0
        L_0x007d:
            r3 = r13
            goto L_0x0085
        L_0x007f:
            r3 = 0
            goto L_0x0085
        L_0x0081:
            r3 = 0
            goto L_0x0085
        L_0x0083:
            r3 = 0
        L_0x0085:
            int r13 = r0.mWidthOverride
            r18 = r4
            r4 = -1
            if (r13 == r4) goto L_0x0094
            if (r39 == 0) goto L_0x0094
            r3 = 0
            int r13 = r0.mWidthOverride
            r0.mWidthOverride = r4
            goto L_0x0096
        L_0x0094:
            r13 = r50
        L_0x0096:
            r50 = r3
            int r3 = r0.mHeightOverride
            if (r3 == r4) goto L_0x00a4
            if (r39 != 0) goto L_0x00a4
            r3 = 0
            int r13 = r0.mHeightOverride
            r0.mHeightOverride = r4
            goto L_0x00a6
        L_0x00a4:
            r3 = r50
        L_0x00a6:
            int r4 = r0.mVisibility
            r50 = r13
            r13 = 8
            if (r4 != r13) goto L_0x00b3
            r4 = 0
            r3 = 0
            r20 = r3
            goto L_0x00b7
        L_0x00b3:
            r4 = r50
            r20 = r3
        L_0x00b7:
            if (r64 == 0) goto L_0x00d2
            if (r11 != 0) goto L_0x00c5
            if (r12 != 0) goto L_0x00c5
            if (r16 != 0) goto L_0x00c5
            r3 = r49
            r10.addEquality(r7, r3)
            goto L_0x00d2
        L_0x00c5:
            r3 = r49
            if (r11 == 0) goto L_0x00d2
            if (r12 != 0) goto L_0x00d2
            int r3 = r47.getMargin()
            r10.addEquality(r7, r5, r3, r13)
        L_0x00d2:
            r3 = 3
            if (r20 != 0) goto L_0x0104
            if (r46 == 0) goto L_0x00ee
            r13 = 0
            r10.addEquality(r6, r7, r13, r3)
            if (r15 <= 0) goto L_0x00e3
            r3 = 8
            r10.addGreaterThan(r6, r7, r15, r3)
            goto L_0x00e5
        L_0x00e3:
            r3 = 8
        L_0x00e5:
            r13 = 2147483647(0x7fffffff, float:NaN)
            if (r9 >= r13) goto L_0x00f3
            r10.addLowerThan(r6, r7, r9, r3)
            goto L_0x00f3
        L_0x00ee:
            r3 = 8
            r10.addEquality(r6, r7, r4, r3)
        L_0x00f3:
            r13 = r42
            r26 = r2
            r19 = r4
            r30 = r5
            r27 = r8
            r9 = r18
            r18 = r1
            r8 = r6
            goto L_0x0229
        L_0x0104:
            r3 = 2
            if (r8 == r3) goto L_0x0130
            if (r54 != 0) goto L_0x0130
            r3 = 1
            if (r14 == r3) goto L_0x010e
            if (r14 != 0) goto L_0x0130
        L_0x010e:
            r20 = 0
            int r3 = java.lang.Math.max(r1, r4)
            if (r2 <= 0) goto L_0x011a
            int r3 = java.lang.Math.min(r2, r3)
        L_0x011a:
            r13 = 8
            r10.addEquality(r6, r7, r3, r13)
            r13 = r42
            r26 = r2
            r19 = r4
            r30 = r5
            r27 = r8
            r9 = r18
            r18 = r1
            r8 = r6
            goto L_0x0229
        L_0x0130:
            r3 = -2
            if (r1 != r3) goto L_0x0136
            r1 = r4
            r13 = r1
            goto L_0x0137
        L_0x0136:
            r13 = r1
        L_0x0137:
            if (r2 != r3) goto L_0x013c
            r1 = r4
            r3 = r1
            goto L_0x013d
        L_0x013c:
            r3 = r2
        L_0x013d:
            if (r4 <= 0) goto L_0x0143
            r1 = 1
            if (r14 == r1) goto L_0x0143
            r4 = 0
        L_0x0143:
            if (r13 <= 0) goto L_0x014e
            r1 = 8
            r10.addGreaterThan(r6, r7, r13, r1)
            int r4 = java.lang.Math.max(r4, r13)
        L_0x014e:
            if (r3 <= 0) goto L_0x0163
            r1 = 1
            if (r40 == 0) goto L_0x0157
            r2 = 1
            if (r14 != r2) goto L_0x0157
            r1 = 0
        L_0x0157:
            if (r1 == 0) goto L_0x015e
            r2 = 8
            r10.addLowerThan(r6, r7, r3, r2)
        L_0x015e:
            int r2 = java.lang.Math.min(r4, r3)
            r4 = r2
        L_0x0163:
            r1 = 1
            if (r14 != r1) goto L_0x0193
            if (r40 == 0) goto L_0x016f
            r1 = 8
            r10.addEquality(r6, r7, r4, r1)
            r2 = 5
            goto L_0x0182
        L_0x016f:
            r1 = 8
            if (r56 == 0) goto L_0x017b
            r2 = 5
            r10.addEquality(r6, r7, r4, r2)
            r10.addLowerThan(r6, r7, r4, r1)
            goto L_0x0182
        L_0x017b:
            r2 = 5
            r10.addEquality(r6, r7, r4, r2)
            r10.addLowerThan(r6, r7, r4, r1)
        L_0x0182:
            r26 = r3
            r19 = r4
            r30 = r5
            r27 = r8
            r9 = r18
            r8 = r6
            r18 = r13
            r13 = r42
            goto L_0x0229
        L_0x0193:
            r2 = 5
            r1 = 2
            if (r14 != r1) goto L_0x0218
            r1 = 0
            r24 = 0
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r2 = r47.getType()
            r61 = r1
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.TOP
            if (r2 == r1) goto L_0x01cc
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r1 = r47.getType()
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.BOTTOM
            if (r1 != r2) goto L_0x01ad
            goto L_0x01cc
        L_0x01ad:
            androidx.constraintlayout.core.widgets.ConstraintWidget r1 = r0.mParent
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.LEFT
            androidx.constraintlayout.core.widgets.ConstraintAnchor r1 = r1.getAnchor(r2)
            androidx.constraintlayout.core.SolverVariable r1 = r10.createObjectVariable(r1)
            androidx.constraintlayout.core.widgets.ConstraintWidget r2 = r0.mParent
            r61 = r1
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.RIGHT
            androidx.constraintlayout.core.widgets.ConstraintAnchor r1 = r2.getAnchor(r1)
            androidx.constraintlayout.core.SolverVariable r1 = r10.createObjectVariable(r1)
            r25 = r61
            r24 = r1
            goto L_0x01ea
        L_0x01cc:
            androidx.constraintlayout.core.widgets.ConstraintWidget r1 = r0.mParent
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.TOP
            androidx.constraintlayout.core.widgets.ConstraintAnchor r1 = r1.getAnchor(r2)
            androidx.constraintlayout.core.SolverVariable r1 = r10.createObjectVariable(r1)
            androidx.constraintlayout.core.widgets.ConstraintWidget r2 = r0.mParent
            r61 = r1
            androidx.constraintlayout.core.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.core.widgets.ConstraintAnchor.Type.BOTTOM
            androidx.constraintlayout.core.widgets.ConstraintAnchor r1 = r2.getAnchor(r1)
            androidx.constraintlayout.core.SolverVariable r1 = r10.createObjectVariable(r1)
            r25 = r61
            r24 = r1
        L_0x01ea:
            androidx.constraintlayout.core.ArrayRow r1 = r38.createRow()
            r26 = 5
            r2 = r6
            r27 = r8
            r8 = r26
            r26 = r3
            r3 = r7
            r9 = r18
            r18 = r4
            r4 = r24
            r30 = r5
            r5 = r25
            r8 = r6
            r6 = r63
            androidx.constraintlayout.core.ArrayRow r1 = r1.createRowDimensionRatio(r2, r3, r4, r5, r6)
            r10.addConstraint(r1)
            if (r40 == 0) goto L_0x0211
            r1 = 0
            r20 = r1
        L_0x0211:
            r19 = r18
            r18 = r13
            r13 = r42
            goto L_0x0229
        L_0x0218:
            r26 = r3
            r30 = r5
            r27 = r8
            r9 = r18
            r18 = r4
            r8 = r6
            r1 = 1
            r19 = r18
            r18 = r13
            r13 = r1
        L_0x0229:
            if (r64 == 0) goto L_0x070d
            if (r56 == 0) goto L_0x0245
            r4 = r44
            r3 = r48
            r1 = r0
            r6 = r7
            r0 = r8
            r33 = r11
            r34 = r12
            r50 = r13
            r8 = r14
            r35 = r27
            r7 = r30
            r2 = 0
            r11 = r43
            r12 = r9
            goto L_0x0723
        L_0x0245:
            r6 = 5
            if (r11 != 0) goto L_0x0260
            if (r12 != 0) goto L_0x0260
            if (r16 != 0) goto L_0x0260
            r1 = r0
            r0 = r8
            r33 = r11
            r34 = r12
            r50 = r13
            r8 = r14
            r35 = r27
            r11 = r43
            r14 = r6
            r6 = r7
            r12 = r9
            r7 = r30
            goto L_0x06d7
        L_0x0260:
            if (r11 == 0) goto L_0x0288
            if (r12 != 0) goto L_0x0288
            r5 = r47
            androidx.constraintlayout.core.widgets.ConstraintAnchor r1 = r5.mTarget
            androidx.constraintlayout.core.widgets.ConstraintWidget r1 = r1.mOwner
            if (r40 == 0) goto L_0x0272
            boolean r2 = r1 instanceof androidx.constraintlayout.core.widgets.Barrier
            if (r2 == 0) goto L_0x0272
            r6 = 8
        L_0x0272:
            r23 = r40
            r1 = r0
            r0 = r8
            r33 = r11
            r34 = r12
            r50 = r13
            r8 = r14
            r35 = r27
            r11 = r43
            r14 = r6
            r6 = r7
            r12 = r9
            r7 = r30
            goto L_0x06d9
        L_0x0288:
            r5 = r47
            r4 = 0
            if (r11 != 0) goto L_0x02f4
            if (r12 == 0) goto L_0x02f4
            int r1 = r48.getMargin()
            int r1 = -r1
            r2 = 8
            r10.addEquality(r8, r9, r1, r2)
            if (r40 == 0) goto L_0x02df
            boolean r1 = r0.OPTIMIZE_WRAP
            if (r1 == 0) goto L_0x02c6
            boolean r1 = r7.isFinalValue
            if (r1 == 0) goto L_0x02c6
            androidx.constraintlayout.core.widgets.ConstraintWidget r1 = r0.mParent
            if (r1 == 0) goto L_0x02c6
            androidx.constraintlayout.core.widgets.ConstraintWidgetContainer r1 = (androidx.constraintlayout.core.widgets.ConstraintWidgetContainer) r1
            if (r39 == 0) goto L_0x02af
            r1.addHorizontalWrapMinVariable(r5)
            goto L_0x02b2
        L_0x02af:
            r1.addVerticalWrapMinVariable(r5)
        L_0x02b2:
            r1 = r0
            r0 = r8
            r33 = r11
            r34 = r12
            r50 = r13
            r8 = r14
            r35 = r27
            r11 = r43
            r14 = r6
            r6 = r7
            r12 = r9
            r7 = r30
            goto L_0x06d7
        L_0x02c6:
            r3 = r43
            r1 = 5
            r10.addGreaterThan(r7, r3, r4, r1)
            r1 = r0
            r0 = r8
            r33 = r11
            r34 = r12
            r50 = r13
            r8 = r14
            r35 = r27
            r11 = r3
            r14 = r6
            r6 = r7
            r12 = r9
            r7 = r30
            goto L_0x06d7
        L_0x02df:
            r3 = r43
            r1 = r0
            r0 = r8
            r33 = r11
            r34 = r12
            r50 = r13
            r8 = r14
            r35 = r27
            r11 = r3
            r14 = r6
            r6 = r7
            r12 = r9
            r7 = r30
            goto L_0x06d7
        L_0x02f4:
            r3 = r43
            if (r11 == 0) goto L_0x06c6
            if (r12 == 0) goto L_0x06c6
            r23 = 1
            r1 = 0
            r24 = 0
            r2 = 0
            r25 = 5
            r31 = 4
            r32 = 6
            r25 = 5
            androidx.constraintlayout.core.widgets.ConstraintAnchor r4 = r5.mTarget
            androidx.constraintlayout.core.widgets.ConstraintWidget r4 = r4.mOwner
            r42 = r1
            r33 = r11
            r11 = r14
            r14 = r48
            androidx.constraintlayout.core.widgets.ConstraintAnchor r1 = r14.mTarget
            androidx.constraintlayout.core.widgets.ConstraintWidget r1 = r1.mOwner
            r34 = r12
            androidx.constraintlayout.core.widgets.ConstraintWidget r12 = r37.getParent()
            if (r20 == 0) goto L_0x0517
            if (r11 != 0) goto L_0x039c
            if (r26 != 0) goto L_0x0353
            if (r18 != 0) goto L_0x0353
            r24 = 1
            r17 = 8
            r22 = 8
            r50 = r2
            r15 = r30
            boolean r2 = r15.isFinalValue
            if (r2 == 0) goto L_0x034a
            boolean r2 = r9.isFinalValue
            if (r2 == 0) goto L_0x034a
            int r2 = r47.getMargin()
            r3 = 8
            r10.addEquality(r7, r15, r2, r3)
            int r2 = r48.getMargin()
            int r2 = -r2
            r10.addEquality(r8, r9, r2, r3)
            return
        L_0x034a:
            r2 = r42
            r25 = r17
            r31 = r22
            r22 = r50
            goto L_0x0363
        L_0x0353:
            r50 = r2
            r15 = r30
            r2 = 1
            r3 = 5
            r17 = 5
            r23 = 1
            r22 = 1
            r25 = r3
            r31 = r17
        L_0x0363:
            boolean r3 = r4 instanceof androidx.constraintlayout.core.widgets.Barrier
            if (r3 != 0) goto L_0x0383
            boolean r3 = r1 instanceof androidx.constraintlayout.core.widgets.Barrier
            if (r3 == 0) goto L_0x036c
            goto L_0x0383
        L_0x036c:
            r36 = r1
            r17 = r2
            r29 = r4
            r42 = r7
            r0 = r8
            r61 = r11
            r50 = r12
            r2 = r22
            r35 = r27
            r11 = r6
            r12 = r9
            r9 = r44
            goto L_0x057e
        L_0x0383:
            r31 = 4
            r36 = r1
            r17 = r2
            r29 = r4
            r42 = r7
            r0 = r8
            r61 = r11
            r50 = r12
            r2 = r22
            r35 = r27
            r11 = r6
            r12 = r9
            r9 = r44
            goto L_0x057e
        L_0x039c:
            r50 = r2
            r15 = r30
            r2 = 2
            if (r11 != r2) goto L_0x03e4
            r2 = 1
            r25 = 5
            r31 = 5
            r23 = 1
            r3 = 1
            r42 = r2
            boolean r2 = r4 instanceof androidx.constraintlayout.core.widgets.Barrier
            if (r2 != 0) goto L_0x03cc
            boolean r2 = r1 instanceof androidx.constraintlayout.core.widgets.Barrier
            if (r2 == 0) goto L_0x03b6
            goto L_0x03cc
        L_0x03b6:
            r17 = r42
            r36 = r1
            r2 = r3
            r29 = r4
            r42 = r7
            r0 = r8
            r61 = r11
            r50 = r12
            r35 = r27
            r11 = r6
            r12 = r9
            r9 = r44
            goto L_0x057e
        L_0x03cc:
            r31 = 4
            r17 = r42
            r36 = r1
            r2 = r3
            r29 = r4
            r42 = r7
            r0 = r8
            r61 = r11
            r50 = r12
            r35 = r27
            r11 = r6
            r12 = r9
            r9 = r44
            goto L_0x057e
        L_0x03e4:
            r2 = 1
            if (r11 != r2) goto L_0x0401
            r2 = 1
            r3 = 1
            r25 = 8
            r36 = r1
            r17 = r2
            r2 = r3
            r29 = r4
            r42 = r7
            r0 = r8
            r61 = r11
            r50 = r12
            r35 = r27
            r11 = r6
            r12 = r9
            r9 = r44
            goto L_0x057e
        L_0x0401:
            r3 = 3
            if (r11 != r3) goto L_0x04fe
            int r2 = r0.mResolvedDimensionRatioSide
            r3 = -1
            if (r2 != r3) goto L_0x045f
            r2 = 1
            r3 = 1
            r24 = 1
            r25 = 8
            r31 = 5
            if (r57 == 0) goto L_0x0447
            r31 = 5
            r32 = 4
            if (r40 == 0) goto L_0x0431
            r32 = 5
            r36 = r1
            r17 = r2
            r2 = r3
            r29 = r4
            r42 = r7
            r0 = r8
            r61 = r11
            r50 = r12
            r35 = r27
            r11 = r6
            r12 = r9
            r9 = r44
            goto L_0x057e
        L_0x0431:
            r36 = r1
            r17 = r2
            r2 = r3
            r29 = r4
            r42 = r7
            r0 = r8
            r61 = r11
            r50 = r12
            r35 = r27
            r11 = r6
            r12 = r9
            r9 = r44
            goto L_0x057e
        L_0x0447:
            r32 = 8
            r36 = r1
            r17 = r2
            r2 = r3
            r29 = r4
            r42 = r7
            r0 = r8
            r61 = r11
            r50 = r12
            r35 = r27
            r11 = r6
            r12 = r9
            r9 = r44
            goto L_0x057e
        L_0x045f:
            r2 = 1
            r3 = 1
            r24 = 1
            if (r54 == 0) goto L_0x0493
            r42 = r2
            r61 = r11
            r35 = r27
            r2 = 2
            r11 = r60
            if (r11 == r2) goto L_0x0477
            r2 = 1
            if (r11 != r2) goto L_0x0474
            goto L_0x0477
        L_0x0474:
            r17 = 0
            goto L_0x0479
        L_0x0477:
            r17 = 1
        L_0x0479:
            r2 = r17
            if (r2 != 0) goto L_0x0481
            r25 = 8
            r31 = 5
        L_0x0481:
            r17 = r42
            r36 = r1
            r2 = r3
            r29 = r4
            r11 = r6
            r42 = r7
            r0 = r8
            r50 = r12
            r12 = r9
            r9 = r44
            goto L_0x057e
        L_0x0493:
            r42 = r2
            r61 = r11
            r35 = r27
            r11 = r60
            r25 = 5
            if (r26 <= 0) goto L_0x04b3
            r31 = 5
            r17 = r42
            r36 = r1
            r2 = r3
            r29 = r4
            r11 = r6
            r42 = r7
            r0 = r8
            r50 = r12
            r12 = r9
            r9 = r44
            goto L_0x057e
        L_0x04b3:
            if (r26 != 0) goto L_0x04ec
            if (r18 != 0) goto L_0x04ec
            if (r57 != 0) goto L_0x04cd
            r31 = 8
            r17 = r42
            r36 = r1
            r2 = r3
            r29 = r4
            r11 = r6
            r42 = r7
            r0 = r8
            r50 = r12
            r12 = r9
            r9 = r44
            goto L_0x057e
        L_0x04cd:
            if (r4 == r12) goto L_0x04d5
            if (r1 == r12) goto L_0x04d5
            r2 = 4
            r25 = r2
            goto L_0x04d8
        L_0x04d5:
            r2 = 5
            r25 = r2
        L_0x04d8:
            r31 = 4
            r17 = r42
            r36 = r1
            r2 = r3
            r29 = r4
            r11 = r6
            r42 = r7
            r0 = r8
            r50 = r12
            r12 = r9
            r9 = r44
            goto L_0x057e
        L_0x04ec:
            r17 = r42
            r36 = r1
            r2 = r3
            r29 = r4
            r11 = r6
            r42 = r7
            r0 = r8
            r50 = r12
            r12 = r9
            r9 = r44
            goto L_0x057e
        L_0x04fe:
            r61 = r11
            r35 = r27
            r11 = r60
            r17 = r42
            r2 = r50
            r36 = r1
            r29 = r4
            r11 = r6
            r42 = r7
            r0 = r8
            r50 = r12
            r12 = r9
            r9 = r44
            goto L_0x057e
        L_0x0517:
            r50 = r2
            r61 = r11
            r35 = r27
            r15 = r30
            r11 = r60
            r17 = 1
            r22 = 1
            boolean r2 = r15.isFinalValue
            if (r2 == 0) goto L_0x056f
            boolean r2 = r9.isFinalValue
            if (r2 == 0) goto L_0x056f
            int r21 = r47.getMargin()
            int r27 = r48.getMargin()
            r28 = 8
            r3 = r1
            r1 = r38
            r2 = r7
            r11 = r43
            r36 = r3
            r3 = r15
            r0 = r4
            r4 = r21
            r5 = r53
            r11 = r6
            r6 = r9
            r42 = r7
            r7 = r8
            r29 = r0
            r0 = r8
            r8 = r27
            r50 = r12
            r12 = r9
            r9 = r28
            r1.addCentering(r2, r3, r4, r5, r6, r7, r8, r9)
            if (r40 == 0) goto L_0x056c
            if (r13 == 0) goto L_0x056c
            r1 = 0
            androidx.constraintlayout.core.widgets.ConstraintAnchor r2 = r14.mTarget
            if (r2 == 0) goto L_0x0564
            int r1 = r48.getMargin()
        L_0x0564:
            r9 = r44
            if (r12 == r9) goto L_0x056e
            r10.addGreaterThan(r9, r0, r1, r11)
            goto L_0x056e
        L_0x056c:
            r9 = r44
        L_0x056e:
            return
        L_0x056f:
            r36 = r1
            r29 = r4
            r11 = r6
            r42 = r7
            r0 = r8
            r50 = r12
            r12 = r9
            r9 = r44
            r2 = r22
        L_0x057e:
            if (r2 == 0) goto L_0x058e
            if (r15 != r12) goto L_0x058e
            r7 = r50
            r8 = r29
            if (r8 == r7) goto L_0x0592
            r2 = 0
            r23 = 0
            r22 = r2
            goto L_0x0594
        L_0x058e:
            r7 = r50
            r8 = r29
        L_0x0592:
            r22 = r2
        L_0x0594:
            if (r17 == 0) goto L_0x05dd
            if (r20 != 0) goto L_0x05b2
            if (r55 != 0) goto L_0x05b2
            if (r57 != 0) goto L_0x05b2
            r6 = r11
            r11 = r43
            if (r15 != r11) goto L_0x05b5
            if (r12 != r9) goto L_0x05b5
            r1 = 8
            r2 = 8
            r3 = 0
            r4 = 0
            r32 = r1
            r27 = r2
            r25 = r3
            r23 = r4
            goto L_0x05bb
        L_0x05b2:
            r6 = r11
            r11 = r43
        L_0x05b5:
            r27 = r25
            r25 = r23
            r23 = r40
        L_0x05bb:
            int r4 = r47.getMargin()
            int r29 = r48.getMargin()
            r1 = r38
            r2 = r42
            r5 = 3
            r3 = r15
            r14 = 0
            r14 = r5
            r5 = r53
            r14 = r6
            r6 = r12
            r50 = r13
            r13 = r7
            r7 = r0
            r30 = r8
            r8 = r29
            r9 = r32
            r1.addCentering(r2, r3, r4, r5, r6, r7, r8, r9)
            goto L_0x05eb
        L_0x05dd:
            r30 = r8
            r14 = r11
            r50 = r13
            r11 = r43
            r13 = r7
            r27 = r25
            r25 = r23
            r23 = r40
        L_0x05eb:
            r1 = r37
            r2 = r30
            int r3 = r1.mVisibility
            r4 = 8
            if (r3 != r4) goto L_0x05fc
            boolean r3 = r48.hasDependents()
            if (r3 != 0) goto L_0x05fc
            return
        L_0x05fc:
            if (r22 == 0) goto L_0x062b
            if (r23 == 0) goto L_0x0613
            if (r15 == r12) goto L_0x0613
            if (r20 != 0) goto L_0x0613
            boolean r3 = r2 instanceof androidx.constraintlayout.core.widgets.Barrier
            if (r3 != 0) goto L_0x060f
            r3 = r36
            boolean r4 = r3 instanceof androidx.constraintlayout.core.widgets.Barrier
            if (r4 == 0) goto L_0x0615
            goto L_0x0611
        L_0x060f:
            r3 = r36
        L_0x0611:
            r4 = 6
            goto L_0x0617
        L_0x0613:
            r3 = r36
        L_0x0615:
            r4 = r27
        L_0x0617:
            int r5 = r47.getMargin()
            r6 = r42
            r10.addGreaterThan(r6, r15, r5, r4)
            int r5 = r48.getMargin()
            int r5 = -r5
            r10.addLowerThan(r0, r12, r5, r4)
            r27 = r4
            goto L_0x062f
        L_0x062b:
            r6 = r42
            r3 = r36
        L_0x062f:
            if (r23 == 0) goto L_0x0648
            if (r58 == 0) goto L_0x0648
            boolean r4 = r2 instanceof androidx.constraintlayout.core.widgets.Barrier
            if (r4 != 0) goto L_0x0648
            boolean r4 = r3 instanceof androidx.constraintlayout.core.widgets.Barrier
            if (r4 != 0) goto L_0x0648
            if (r3 == r13) goto L_0x0648
            r31 = 6
            r27 = 6
            r25 = 1
            r4 = r27
            r5 = r31
            goto L_0x064c
        L_0x0648:
            r4 = r27
            r5 = r31
        L_0x064c:
            if (r25 == 0) goto L_0x0691
            if (r24 == 0) goto L_0x0673
            if (r57 == 0) goto L_0x0654
            if (r41 == 0) goto L_0x0673
        L_0x0654:
            r7 = r5
            if (r2 == r13) goto L_0x0659
            if (r3 != r13) goto L_0x065a
        L_0x0659:
            r7 = 6
        L_0x065a:
            boolean r8 = r2 instanceof androidx.constraintlayout.core.widgets.Guideline
            if (r8 != 0) goto L_0x0662
            boolean r8 = r3 instanceof androidx.constraintlayout.core.widgets.Guideline
            if (r8 == 0) goto L_0x0663
        L_0x0662:
            r7 = 5
        L_0x0663:
            boolean r8 = r2 instanceof androidx.constraintlayout.core.widgets.Barrier
            if (r8 != 0) goto L_0x066b
            boolean r8 = r3 instanceof androidx.constraintlayout.core.widgets.Barrier
            if (r8 == 0) goto L_0x066c
        L_0x066b:
            r7 = 5
        L_0x066c:
            if (r57 == 0) goto L_0x066f
            r7 = 5
        L_0x066f:
            int r5 = java.lang.Math.max(r7, r5)
        L_0x0673:
            if (r23 == 0) goto L_0x0682
            int r5 = java.lang.Math.min(r4, r5)
            if (r54 == 0) goto L_0x0682
            if (r57 != 0) goto L_0x0682
            if (r2 == r13) goto L_0x0681
            if (r3 != r13) goto L_0x0682
        L_0x0681:
            r5 = 4
        L_0x0682:
            int r7 = r47.getMargin()
            r10.addEquality(r6, r15, r7, r5)
            int r7 = r48.getMargin()
            int r7 = -r7
            r10.addEquality(r0, r12, r7, r5)
        L_0x0691:
            if (r23 == 0) goto L_0x069f
            r7 = 0
            if (r11 != r15) goto L_0x069a
            int r7 = r47.getMargin()
        L_0x069a:
            if (r15 == r11) goto L_0x069f
            r10.addGreaterThan(r6, r11, r7, r14)
        L_0x069f:
            if (r23 == 0) goto L_0x06c2
            if (r20 == 0) goto L_0x06c2
            r7 = r15
            if (r51 != 0) goto L_0x06bf
            if (r18 != 0) goto L_0x06bf
            if (r20 == 0) goto L_0x06b8
            r8 = r61
            r9 = 3
            if (r8 != r9) goto L_0x06b6
            r9 = 0
            r15 = 8
            r10.addGreaterThan(r0, r6, r9, r15)
            goto L_0x06d9
        L_0x06b6:
            r9 = 0
            goto L_0x06bb
        L_0x06b8:
            r8 = r61
            r9 = 0
        L_0x06bb:
            r10.addGreaterThan(r0, r6, r9, r14)
            goto L_0x06d9
        L_0x06bf:
            r8 = r61
            goto L_0x06d9
        L_0x06c2:
            r8 = r61
            r7 = r15
            goto L_0x06d9
        L_0x06c6:
            r1 = r0
            r0 = r8
            r33 = r11
            r34 = r12
            r50 = r13
            r8 = r14
            r35 = r27
            r11 = r3
            r14 = r6
            r6 = r7
            r12 = r9
            r7 = r30
        L_0x06d7:
            r23 = r40
        L_0x06d9:
            if (r23 == 0) goto L_0x0708
            if (r50 == 0) goto L_0x0708
            r2 = 0
            r3 = r48
            androidx.constraintlayout.core.widgets.ConstraintAnchor r4 = r3.mTarget
            if (r4 == 0) goto L_0x06e8
            int r2 = r48.getMargin()
        L_0x06e8:
            r4 = r44
            if (r12 == r4) goto L_0x070c
            boolean r5 = r1.OPTIMIZE_WRAP
            if (r5 == 0) goto L_0x0704
            boolean r5 = r0.isFinalValue
            if (r5 == 0) goto L_0x0704
            androidx.constraintlayout.core.widgets.ConstraintWidget r5 = r1.mParent
            if (r5 == 0) goto L_0x0704
            androidx.constraintlayout.core.widgets.ConstraintWidgetContainer r5 = (androidx.constraintlayout.core.widgets.ConstraintWidgetContainer) r5
            if (r39 == 0) goto L_0x0700
            r5.addHorizontalWrapMaxVariable(r3)
            goto L_0x0703
        L_0x0700:
            r5.addVerticalWrapMaxVariable(r3)
        L_0x0703:
            return
        L_0x0704:
            r10.addGreaterThan(r4, r0, r2, r14)
            goto L_0x070c
        L_0x0708:
            r4 = r44
            r3 = r48
        L_0x070c:
            return
        L_0x070d:
            r4 = r44
            r3 = r48
            r1 = r0
            r6 = r7
            r0 = r8
            r33 = r11
            r34 = r12
            r50 = r13
            r8 = r14
            r35 = r27
            r7 = r30
            r2 = 0
            r11 = r43
            r12 = r9
        L_0x0723:
            r5 = r35
            r9 = 2
            if (r5 >= r9) goto L_0x076e
            if (r40 == 0) goto L_0x076e
            if (r50 == 0) goto L_0x076e
            r9 = 8
            r10.addGreaterThan(r6, r11, r2, r9)
            if (r39 != 0) goto L_0x073c
            androidx.constraintlayout.core.widgets.ConstraintAnchor r9 = r1.mBaseline
            androidx.constraintlayout.core.widgets.ConstraintAnchor r9 = r9.mTarget
            if (r9 != 0) goto L_0x073a
            goto L_0x073c
        L_0x073a:
            r13 = r2
            goto L_0x073d
        L_0x073c:
            r13 = 1
        L_0x073d:
            r9 = r13
            if (r39 != 0) goto L_0x0767
            androidx.constraintlayout.core.widgets.ConstraintAnchor r13 = r1.mBaseline
            androidx.constraintlayout.core.widgets.ConstraintAnchor r13 = r13.mTarget
            if (r13 == 0) goto L_0x0767
            androidx.constraintlayout.core.widgets.ConstraintAnchor r13 = r1.mBaseline
            androidx.constraintlayout.core.widgets.ConstraintAnchor r13 = r13.mTarget
            androidx.constraintlayout.core.widgets.ConstraintWidget r13 = r13.mOwner
            float r14 = r13.mDimensionRatio
            r15 = 0
            int r14 = (r14 > r15 ? 1 : (r14 == r15 ? 0 : -1))
            if (r14 == 0) goto L_0x0766
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour[] r14 = r13.mListDimensionBehaviors
            r14 = r14[r2]
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r15 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r14 != r15) goto L_0x0766
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour[] r14 = r13.mListDimensionBehaviors
            r15 = 1
            r14 = r14[r15]
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r15 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r14 != r15) goto L_0x0766
            r9 = 1
            goto L_0x0767
        L_0x0766:
            r9 = 0
        L_0x0767:
            if (r9 == 0) goto L_0x076e
            r13 = 8
            r10.addGreaterThan(r4, r0, r2, r13)
        L_0x076e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.widgets.ConstraintWidget.applyConstraints(androidx.constraintlayout.core.LinearSystem, boolean, boolean, boolean, boolean, androidx.constraintlayout.core.SolverVariable, androidx.constraintlayout.core.SolverVariable, androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour, boolean, androidx.constraintlayout.core.widgets.ConstraintAnchor, androidx.constraintlayout.core.widgets.ConstraintAnchor, int, int, int, int, float, boolean, boolean, boolean, boolean, boolean, int, int, int, int, float, boolean):void");
    }

    private void getSceneString(StringBuilder ret, String type, int size, int min, int max, int override, int matchConstraintMin, int matchConstraintDefault, float MatchConstraintPercent, float weight) {
        ret.append(type);
        ret.append(" :  {\n");
        serializeAttribute(ret, "      size", size, 0);
        serializeAttribute(ret, "      min", min, 0);
        serializeAttribute(ret, "      max", max, Integer.MAX_VALUE);
        serializeAttribute(ret, "      matchMin", matchConstraintMin, 0);
        serializeAttribute(ret, "      matchDef", matchConstraintDefault, 0);
        serializeAttribute(ret, "      matchPercent", MatchConstraintPercent, 1.0f);
        ret.append("    },\n");
    }

    private void getSceneString(StringBuilder ret, String side, ConstraintAnchor a) {
        if (a.mTarget != null) {
            ret.append("    ");
            ret.append(side);
            ret.append(" : [ '");
            ret.append(a.mTarget);
            ret.append("'");
            if (!(a.mGoneMargin == Integer.MIN_VALUE && a.mMargin == 0)) {
                ret.append(",");
                ret.append(a.mMargin);
                if (a.mGoneMargin != Integer.MIN_VALUE) {
                    ret.append(",");
                    ret.append(a.mGoneMargin);
                    ret.append(",");
                }
            }
            ret.append(" ] ,\n");
        }
    }

    private boolean isChainHead(int orientation) {
        int i = orientation * 2;
        if (this.mListAnchors[i].mTarget != null) {
            ConstraintAnchor constraintAnchor = this.mListAnchors[i].mTarget.mTarget;
            ConstraintAnchor[] constraintAnchorArr = this.mListAnchors;
            return (constraintAnchor == constraintAnchorArr[i] || constraintAnchorArr[i + 1].mTarget == null || this.mListAnchors[i + 1].mTarget.mTarget != this.mListAnchors[i + 1]) ? false : true;
        }
    }

    private void serializeAnchor(StringBuilder ret, String side, ConstraintAnchor a) {
        if (a.mTarget != null) {
            ret.append(side);
            ret.append(" : [ '");
            ret.append(a.mTarget);
            ret.append("',");
            ret.append(a.mMargin);
            ret.append(",");
            ret.append(a.mGoneMargin);
            ret.append(",");
            ret.append(" ] ,\n");
        }
    }

    private void serializeAttribute(StringBuilder ret, String type, float value, float def) {
        if (value != def) {
            ret.append(type);
            ret.append(" :   ");
            ret.append(value);
            ret.append(",\n");
        }
    }

    private void serializeAttribute(StringBuilder ret, String type, int value, int def) {
        if (value != def) {
            ret.append(type);
            ret.append(" :   ");
            ret.append(value);
            ret.append(",\n");
        }
    }

    private void serializeCircle(StringBuilder ret, ConstraintAnchor a, float angle) {
        if (a.mTarget != null) {
            ret.append("circle : [ '");
            ret.append(a.mTarget);
            ret.append("',");
            ret.append(a.mMargin);
            ret.append(",");
            ret.append(angle);
            ret.append(",");
            ret.append(" ] ,\n");
        }
    }

    private void serializeDimensionRatio(StringBuilder ret, String type, float value, int whichSide) {
        if (value != 0.0f) {
            ret.append(type);
            ret.append(" :  [");
            ret.append(value);
            ret.append(",");
            ret.append(whichSide);
            ret.append(HttpUrl.FRAGMENT_ENCODE_SET);
            ret.append("],\n");
        }
    }

    private void serializeSize(StringBuilder ret, String type, int size, int min, int max, int override, int matchConstraintMin, int matchConstraintDefault, float MatchConstraintPercent, float weight) {
        ret.append(type);
        ret.append(" :  {\n");
        serializeAttribute(ret, "size", size, Integer.MIN_VALUE);
        serializeAttribute(ret, "min", min, 0);
        serializeAttribute(ret, "max", max, Integer.MAX_VALUE);
        serializeAttribute(ret, "matchMin", matchConstraintMin, 0);
        serializeAttribute(ret, "matchDef", matchConstraintDefault, 0);
        serializeAttribute(ret, "matchPercent", matchConstraintDefault, 1);
        ret.append("},\n");
    }

    public void addChildrenToSolverByDependency(ConstraintWidgetContainer container, LinearSystem system, HashSet<ConstraintWidget> hashSet, int orientation, boolean addSelf) {
        if (addSelf) {
            if (hashSet.contains(this)) {
                Optimizer.checkMatchParent(container, system, this);
                hashSet.remove(this);
                addToSolver(system, container.optimizeFor(64));
            } else {
                return;
            }
        }
        if (orientation == 0) {
            HashSet<ConstraintAnchor> dependents = this.mLeft.getDependents();
            if (dependents != null) {
                Iterator<ConstraintAnchor> it = dependents.iterator();
                while (it.hasNext()) {
                    it.next().mOwner.addChildrenToSolverByDependency(container, system, hashSet, orientation, true);
                }
            }
            HashSet<ConstraintAnchor> dependents2 = this.mRight.getDependents();
            if (dependents2 != null) {
                Iterator<ConstraintAnchor> it2 = dependents2.iterator();
                while (it2.hasNext()) {
                    it2.next().mOwner.addChildrenToSolverByDependency(container, system, hashSet, orientation, true);
                }
                return;
            }
            return;
        }
        HashSet<ConstraintAnchor> dependents3 = this.mTop.getDependents();
        if (dependents3 != null) {
            Iterator<ConstraintAnchor> it3 = dependents3.iterator();
            while (it3.hasNext()) {
                it3.next().mOwner.addChildrenToSolverByDependency(container, system, hashSet, orientation, true);
            }
        }
        HashSet<ConstraintAnchor> dependents4 = this.mBottom.getDependents();
        if (dependents4 != null) {
            Iterator<ConstraintAnchor> it4 = dependents4.iterator();
            while (it4.hasNext()) {
                it4.next().mOwner.addChildrenToSolverByDependency(container, system, hashSet, orientation, true);
            }
        }
        HashSet<ConstraintAnchor> dependents5 = this.mBaseline.getDependents();
        if (dependents5 != null) {
            Iterator<ConstraintAnchor> it5 = dependents5.iterator();
            while (it5.hasNext()) {
                it5.next().mOwner.addChildrenToSolverByDependency(container, system, hashSet, orientation, true);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean addFirst() {
        return (this instanceof VirtualLayout) || (this instanceof Guideline);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v31, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r37v0, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v32, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v34, resolved type: int} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:193:0x0350  */
    /* JADX WARNING: Removed duplicated region for block: B:194:0x0353  */
    /* JADX WARNING: Removed duplicated region for block: B:200:0x035f  */
    /* JADX WARNING: Removed duplicated region for block: B:201:0x0362  */
    /* JADX WARNING: Removed duplicated region for block: B:206:0x0371  */
    /* JADX WARNING: Removed duplicated region for block: B:207:0x0373  */
    /* JADX WARNING: Removed duplicated region for block: B:210:0x0378  */
    /* JADX WARNING: Removed duplicated region for block: B:211:0x037c  */
    /* JADX WARNING: Removed duplicated region for block: B:214:0x0387  */
    /* JADX WARNING: Removed duplicated region for block: B:215:0x038b  */
    /* JADX WARNING: Removed duplicated region for block: B:220:0x03a0  */
    /* JADX WARNING: Removed duplicated region for block: B:254:0x04f0  */
    /* JADX WARNING: Removed duplicated region for block: B:257:0x0507  */
    /* JADX WARNING: Removed duplicated region for block: B:275:0x0568  */
    /* JADX WARNING: Removed duplicated region for block: B:279:0x057b  */
    /* JADX WARNING: Removed duplicated region for block: B:280:0x057e  */
    /* JADX WARNING: Removed duplicated region for block: B:284:0x0585  */
    /* JADX WARNING: Removed duplicated region for block: B:318:0x0629  */
    /* JADX WARNING: Removed duplicated region for block: B:319:0x062c  */
    /* JADX WARNING: Removed duplicated region for block: B:321:0x0668  */
    /* JADX WARNING: Removed duplicated region for block: B:323:0x066c  */
    /* JADX WARNING: Removed duplicated region for block: B:329:0x0698  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addToSolver(androidx.constraintlayout.core.LinearSystem r74, boolean r75) {
        /*
            r73 = this;
            r15 = r73
            r14 = r74
            androidx.constraintlayout.core.widgets.ConstraintAnchor r0 = r15.mLeft
            androidx.constraintlayout.core.SolverVariable r13 = r14.createObjectVariable(r0)
            androidx.constraintlayout.core.widgets.ConstraintAnchor r0 = r15.mRight
            androidx.constraintlayout.core.SolverVariable r12 = r14.createObjectVariable(r0)
            androidx.constraintlayout.core.widgets.ConstraintAnchor r0 = r15.mTop
            androidx.constraintlayout.core.SolverVariable r11 = r14.createObjectVariable(r0)
            androidx.constraintlayout.core.widgets.ConstraintAnchor r0 = r15.mBottom
            androidx.constraintlayout.core.SolverVariable r10 = r14.createObjectVariable(r0)
            androidx.constraintlayout.core.widgets.ConstraintAnchor r0 = r15.mBaseline
            androidx.constraintlayout.core.SolverVariable r9 = r14.createObjectVariable(r0)
            r0 = 0
            r1 = 0
            androidx.constraintlayout.core.widgets.ConstraintWidget r2 = r15.mParent
            r8 = 1
            r5 = 0
            if (r2 == 0) goto L_0x005d
            if (r2 == 0) goto L_0x0036
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour[] r2 = r2.mListDimensionBehaviors
            r2 = r2[r5]
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r3 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r2 != r3) goto L_0x0036
            r2 = r8
            goto L_0x0037
        L_0x0036:
            r2 = r5
        L_0x0037:
            r0 = r2
            androidx.constraintlayout.core.widgets.ConstraintWidget r2 = r15.mParent
            if (r2 == 0) goto L_0x0046
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour[] r2 = r2.mListDimensionBehaviors
            r2 = r2[r8]
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r3 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r2 != r3) goto L_0x0046
            r2 = r8
            goto L_0x0047
        L_0x0046:
            r2 = r5
        L_0x0047:
            r1 = r2
            int r2 = r15.mWrapBehaviorInParent
            switch(r2) {
                case 1: goto L_0x0059;
                case 2: goto L_0x0055;
                case 3: goto L_0x0050;
                default: goto L_0x004d;
            }
        L_0x004d:
            r4 = r0
            r3 = r1
            goto L_0x005f
        L_0x0050:
            r0 = 0
            r1 = 0
            r4 = r0
            r3 = r1
            goto L_0x005f
        L_0x0055:
            r0 = 0
            r4 = r0
            r3 = r1
            goto L_0x005f
        L_0x0059:
            r1 = 0
            r4 = r0
            r3 = r1
            goto L_0x005f
        L_0x005d:
            r4 = r0
            r3 = r1
        L_0x005f:
            int r0 = r15.mVisibility
            r2 = 8
            if (r0 != r2) goto L_0x007a
            boolean r0 = r15.mAnimated
            if (r0 != 0) goto L_0x007a
            boolean r0 = r73.hasDependencies()
            if (r0 != 0) goto L_0x007a
            boolean[] r0 = r15.mIsInBarrier
            boolean r1 = r0[r5]
            if (r1 != 0) goto L_0x007a
            boolean r0 = r0[r8]
            if (r0 != 0) goto L_0x007a
            return
        L_0x007a:
            boolean r0 = r15.resolvedHorizontal
            if (r0 != 0) goto L_0x0082
            boolean r1 = r15.resolvedVertical
            if (r1 == 0) goto L_0x0101
        L_0x0082:
            if (r0 == 0) goto L_0x00b2
            int r0 = r15.mX
            r14.addEquality(r13, r0)
            int r0 = r15.mX
            int r1 = r15.mWidth
            int r0 = r0 + r1
            r14.addEquality(r12, r0)
            if (r4 == 0) goto L_0x00b2
            androidx.constraintlayout.core.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x00b2
            boolean r1 = r15.OPTIMIZE_WRAP_ON_RESOLVED
            if (r1 == 0) goto L_0x00a8
            androidx.constraintlayout.core.widgets.ConstraintWidgetContainer r0 = (androidx.constraintlayout.core.widgets.ConstraintWidgetContainer) r0
            androidx.constraintlayout.core.widgets.ConstraintAnchor r1 = r15.mLeft
            r0.addHorizontalWrapMinVariable(r1)
            androidx.constraintlayout.core.widgets.ConstraintAnchor r1 = r15.mRight
            r0.addHorizontalWrapMaxVariable(r1)
            goto L_0x00b2
        L_0x00a8:
            r1 = 5
            androidx.constraintlayout.core.widgets.ConstraintAnchor r0 = r0.mRight
            androidx.constraintlayout.core.SolverVariable r0 = r14.createObjectVariable(r0)
            r14.addGreaterThan(r0, r12, r5, r1)
        L_0x00b2:
            boolean r0 = r15.resolvedVertical
            if (r0 == 0) goto L_0x00f4
            int r0 = r15.mY
            r14.addEquality(r11, r0)
            int r0 = r15.mY
            int r1 = r15.mHeight
            int r0 = r0 + r1
            r14.addEquality(r10, r0)
            androidx.constraintlayout.core.widgets.ConstraintAnchor r0 = r15.mBaseline
            boolean r0 = r0.hasDependents()
            if (r0 == 0) goto L_0x00d3
            int r0 = r15.mY
            int r1 = r15.mBaselineDistance
            int r0 = r0 + r1
            r14.addEquality(r9, r0)
        L_0x00d3:
            if (r3 == 0) goto L_0x00f4
            androidx.constraintlayout.core.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x00f4
            boolean r1 = r15.OPTIMIZE_WRAP_ON_RESOLVED
            if (r1 == 0) goto L_0x00ea
            androidx.constraintlayout.core.widgets.ConstraintWidgetContainer r0 = (androidx.constraintlayout.core.widgets.ConstraintWidgetContainer) r0
            androidx.constraintlayout.core.widgets.ConstraintAnchor r1 = r15.mTop
            r0.addVerticalWrapMinVariable(r1)
            androidx.constraintlayout.core.widgets.ConstraintAnchor r1 = r15.mBottom
            r0.addVerticalWrapMaxVariable(r1)
            goto L_0x00f4
        L_0x00ea:
            r1 = 5
            androidx.constraintlayout.core.widgets.ConstraintAnchor r0 = r0.mBottom
            androidx.constraintlayout.core.SolverVariable r0 = r14.createObjectVariable(r0)
            r14.addGreaterThan(r0, r10, r5, r1)
        L_0x00f4:
            boolean r0 = r15.resolvedHorizontal
            if (r0 == 0) goto L_0x0101
            boolean r0 = r15.resolvedVertical
            if (r0 == 0) goto L_0x0101
            r15.resolvedHorizontal = r5
            r15.resolvedVertical = r5
            return
        L_0x0101:
            androidx.constraintlayout.core.Metrics r0 = androidx.constraintlayout.core.LinearSystem.sMetrics
            r6 = 1
            if (r0 == 0) goto L_0x0111
            androidx.constraintlayout.core.Metrics r0 = androidx.constraintlayout.core.LinearSystem.sMetrics
            r16 = r3
            long r2 = r0.widgets
            long r2 = r2 + r6
            r0.widgets = r2
            goto L_0x0113
        L_0x0111:
            r16 = r3
        L_0x0113:
            if (r75 == 0) goto L_0x01b2
            androidx.constraintlayout.core.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            if (r0 == 0) goto L_0x01b2
            androidx.constraintlayout.core.widgets.analyzer.VerticalWidgetRun r1 = r15.verticalRun
            if (r1 == 0) goto L_0x01b2
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r0 = r0.start
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x01b2
            androidx.constraintlayout.core.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r0 = r0.end
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x01b2
            androidx.constraintlayout.core.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r0 = r0.start
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x01b2
            androidx.constraintlayout.core.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r0 = r0.end
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x01b2
            androidx.constraintlayout.core.Metrics r0 = androidx.constraintlayout.core.LinearSystem.sMetrics
            if (r0 == 0) goto L_0x0146
            androidx.constraintlayout.core.Metrics r0 = androidx.constraintlayout.core.LinearSystem.sMetrics
            long r1 = r0.graphSolved
            long r1 = r1 + r6
            r0.graphSolved = r1
        L_0x0146:
            androidx.constraintlayout.core.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r0 = r0.start
            int r0 = r0.value
            r14.addEquality(r13, r0)
            androidx.constraintlayout.core.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r0 = r0.end
            int r0 = r0.value
            r14.addEquality(r12, r0)
            androidx.constraintlayout.core.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r0 = r0.start
            int r0 = r0.value
            r14.addEquality(r11, r0)
            androidx.constraintlayout.core.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r0 = r0.end
            int r0 = r0.value
            r14.addEquality(r10, r0)
            androidx.constraintlayout.core.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r0 = r0.baseline
            int r0 = r0.value
            r14.addEquality(r9, r0)
            androidx.constraintlayout.core.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x01ad
            if (r4 == 0) goto L_0x0192
            boolean[] r0 = r15.isTerminalWidget
            boolean r0 = r0[r5]
            if (r0 == 0) goto L_0x0192
            boolean r0 = r73.isInHorizontalChain()
            if (r0 != 0) goto L_0x0192
            androidx.constraintlayout.core.widgets.ConstraintWidget r0 = r15.mParent
            androidx.constraintlayout.core.widgets.ConstraintAnchor r0 = r0.mRight
            androidx.constraintlayout.core.SolverVariable r0 = r14.createObjectVariable(r0)
            r1 = 8
            r14.addGreaterThan(r0, r12, r5, r1)
        L_0x0192:
            if (r16 == 0) goto L_0x01ad
            boolean[] r0 = r15.isTerminalWidget
            boolean r0 = r0[r8]
            if (r0 == 0) goto L_0x01ad
            boolean r0 = r73.isInVerticalChain()
            if (r0 != 0) goto L_0x01ad
            androidx.constraintlayout.core.widgets.ConstraintWidget r0 = r15.mParent
            androidx.constraintlayout.core.widgets.ConstraintAnchor r0 = r0.mBottom
            androidx.constraintlayout.core.SolverVariable r0 = r14.createObjectVariable(r0)
            r1 = 8
            r14.addGreaterThan(r0, r10, r5, r1)
        L_0x01ad:
            r15.resolvedHorizontal = r5
            r15.resolvedVertical = r5
            return
        L_0x01b2:
            androidx.constraintlayout.core.Metrics r0 = androidx.constraintlayout.core.LinearSystem.sMetrics
            if (r0 == 0) goto L_0x01bd
            androidx.constraintlayout.core.Metrics r0 = androidx.constraintlayout.core.LinearSystem.sMetrics
            long r1 = r0.linearSolved
            long r1 = r1 + r6
            r0.linearSolved = r1
        L_0x01bd:
            r0 = 0
            r1 = 0
            androidx.constraintlayout.core.widgets.ConstraintWidget r2 = r15.mParent
            if (r2 == 0) goto L_0x0234
            boolean r2 = r15.isChainHead(r5)
            if (r2 == 0) goto L_0x01d2
            androidx.constraintlayout.core.widgets.ConstraintWidget r2 = r15.mParent
            androidx.constraintlayout.core.widgets.ConstraintWidgetContainer r2 = (androidx.constraintlayout.core.widgets.ConstraintWidgetContainer) r2
            r2.addChain(r15, r5)
            r0 = 1
            goto L_0x01d6
        L_0x01d2:
            boolean r0 = r73.isInHorizontalChain()
        L_0x01d6:
            boolean r2 = r15.isChainHead(r8)
            if (r2 == 0) goto L_0x01e5
            androidx.constraintlayout.core.widgets.ConstraintWidget r2 = r15.mParent
            androidx.constraintlayout.core.widgets.ConstraintWidgetContainer r2 = (androidx.constraintlayout.core.widgets.ConstraintWidgetContainer) r2
            r2.addChain(r15, r8)
            r1 = 1
            goto L_0x01e9
        L_0x01e5:
            boolean r1 = r73.isInVerticalChain()
        L_0x01e9:
            if (r0 != 0) goto L_0x020a
            if (r4 == 0) goto L_0x020a
            int r2 = r15.mVisibility
            r3 = 8
            if (r2 == r3) goto L_0x020a
            androidx.constraintlayout.core.widgets.ConstraintAnchor r2 = r15.mLeft
            androidx.constraintlayout.core.widgets.ConstraintAnchor r2 = r2.mTarget
            if (r2 != 0) goto L_0x020a
            androidx.constraintlayout.core.widgets.ConstraintAnchor r2 = r15.mRight
            androidx.constraintlayout.core.widgets.ConstraintAnchor r2 = r2.mTarget
            if (r2 != 0) goto L_0x020a
            androidx.constraintlayout.core.widgets.ConstraintWidget r2 = r15.mParent
            androidx.constraintlayout.core.widgets.ConstraintAnchor r2 = r2.mRight
            androidx.constraintlayout.core.SolverVariable r2 = r14.createObjectVariable(r2)
            r14.addGreaterThan(r2, r12, r5, r8)
        L_0x020a:
            if (r1 != 0) goto L_0x022f
            if (r16 == 0) goto L_0x022f
            int r2 = r15.mVisibility
            r3 = 8
            if (r2 == r3) goto L_0x022f
            androidx.constraintlayout.core.widgets.ConstraintAnchor r2 = r15.mTop
            androidx.constraintlayout.core.widgets.ConstraintAnchor r2 = r2.mTarget
            if (r2 != 0) goto L_0x022f
            androidx.constraintlayout.core.widgets.ConstraintAnchor r2 = r15.mBottom
            androidx.constraintlayout.core.widgets.ConstraintAnchor r2 = r2.mTarget
            if (r2 != 0) goto L_0x022f
            androidx.constraintlayout.core.widgets.ConstraintAnchor r2 = r15.mBaseline
            if (r2 != 0) goto L_0x022f
            androidx.constraintlayout.core.widgets.ConstraintWidget r2 = r15.mParent
            androidx.constraintlayout.core.widgets.ConstraintAnchor r2 = r2.mBottom
            androidx.constraintlayout.core.SolverVariable r2 = r14.createObjectVariable(r2)
            r14.addGreaterThan(r2, r10, r5, r8)
        L_0x022f:
            r56 = r0
            r57 = r1
            goto L_0x0238
        L_0x0234:
            r56 = r0
            r57 = r1
        L_0x0238:
            int r0 = r15.mWidth
            int r1 = r15.mMinWidth
            if (r0 >= r1) goto L_0x0240
            int r0 = r15.mMinWidth
        L_0x0240:
            int r1 = r15.mHeight
            int r2 = r15.mMinHeight
            if (r1 >= r2) goto L_0x0248
            int r1 = r15.mMinHeight
        L_0x0248:
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour[] r2 = r15.mListDimensionBehaviors
            r2 = r2[r5]
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r3 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r2 == r3) goto L_0x0252
            r2 = r8
            goto L_0x0253
        L_0x0252:
            r2 = r5
        L_0x0253:
            r3 = r2
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour[] r2 = r15.mListDimensionBehaviors
            r2 = r2[r8]
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r6 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r2 == r6) goto L_0x025e
            r2 = r8
            goto L_0x025f
        L_0x025e:
            r2 = r5
        L_0x025f:
            r6 = 0
            int r7 = r15.mDimensionRatioSide
            r15.mResolvedDimensionRatioSide = r7
            float r7 = r15.mDimensionRatio
            r15.mResolvedDimensionRatio = r7
            int r8 = r15.mMatchConstraintDefaultWidth
            int r5 = r15.mMatchConstraintDefaultHeight
            r17 = 0
            int r7 = (r7 > r17 ? 1 : (r7 == r17 ? 0 : -1))
            r17 = r0
            if (r7 <= 0) goto L_0x0330
            int r7 = r15.mVisibility
            r0 = 8
            if (r7 == r0) goto L_0x0330
            r6 = 1
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r7 = 0
            r0 = r0[r7]
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r7 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r7) goto L_0x0287
            if (r8 != 0) goto L_0x0287
            r8 = 3
        L_0x0287:
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r7 = 1
            r0 = r0[r7]
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r7 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r7) goto L_0x0293
            if (r5 != 0) goto L_0x0293
            r5 = 3
        L_0x0293:
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r7 = 0
            r0 = r0[r7]
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r7 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            r22 = r1
            r1 = 3
            if (r0 != r7) goto L_0x02b5
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r7 = 1
            r0 = r0[r7]
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r7 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r7) goto L_0x02b5
            if (r8 != r1) goto L_0x02b5
            if (r5 != r1) goto L_0x02b5
            r0 = r16
            r15.setupDimensionRatio(r4, r0, r3, r2)
            r23 = r0
            goto L_0x0334
        L_0x02b5:
            r0 = r16
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour[] r7 = r15.mListDimensionBehaviors
            r1 = 0
            r7 = r7[r1]
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r7 != r1) goto L_0x02ee
            r1 = 3
            if (r8 != r1) goto L_0x02ee
            r1 = 0
            r15.mResolvedDimensionRatioSide = r1
            float r1 = r15.mResolvedDimensionRatio
            int r7 = r15.mHeight
            float r7 = (float) r7
            float r1 = r1 * r7
            int r1 = (int) r1
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour[] r7 = r15.mListDimensionBehaviors
            r16 = 1
            r7 = r7[r16]
            r23 = r0
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r0 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r7 == r0) goto L_0x02e5
            r8 = 4
            r6 = 0
            r0 = r1
            r58 = r5
            r59 = r8
            r28 = r22
            r8 = r6
            goto L_0x033d
        L_0x02e5:
            r0 = r1
            r58 = r5
            r59 = r8
            r28 = r22
            r8 = r6
            goto L_0x033d
        L_0x02ee:
            r23 = r0
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r1 = 1
            r0 = r0[r1]
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r7 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r7) goto L_0x0334
            r0 = 3
            if (r5 != r0) goto L_0x0334
            r15.mResolvedDimensionRatioSide = r1
            int r0 = r15.mDimensionRatioSide
            r1 = -1
            if (r0 != r1) goto L_0x030a
            r0 = 1065353216(0x3f800000, float:1.0)
            float r1 = r15.mResolvedDimensionRatio
            float r0 = r0 / r1
            r15.mResolvedDimensionRatio = r0
        L_0x030a:
            float r0 = r15.mResolvedDimensionRatio
            int r1 = r15.mWidth
            float r1 = (float) r1
            float r0 = r0 * r1
            int r1 = (int) r0
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r7 = 0
            r0 = r0[r7]
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r7 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 == r7) goto L_0x0326
            r5 = 4
            r6 = 0
            r28 = r1
            r58 = r5
            r59 = r8
            r0 = r17
            r8 = r6
            goto L_0x033d
        L_0x0326:
            r28 = r1
            r58 = r5
            r59 = r8
            r0 = r17
            r8 = r6
            goto L_0x033d
        L_0x0330:
            r22 = r1
            r23 = r16
        L_0x0334:
            r58 = r5
            r59 = r8
            r0 = r17
            r28 = r22
            r8 = r6
        L_0x033d:
            int[] r1 = r15.mResolvedMatchConstraintDefault
            r5 = 0
            r1[r5] = r59
            r5 = 1
            r1[r5] = r58
            r15.mResolvedHasRatio = r8
            if (r8 == 0) goto L_0x0353
            int r1 = r15.mResolvedDimensionRatioSide
            if (r1 == 0) goto L_0x0350
            r5 = -1
            if (r1 != r5) goto L_0x0353
        L_0x0350:
            r17 = 1
            goto L_0x0355
        L_0x0353:
            r17 = 0
        L_0x0355:
            if (r8 == 0) goto L_0x0362
            int r1 = r15.mResolvedDimensionRatioSide
            r5 = 1
            if (r1 == r5) goto L_0x035f
            r5 = -1
            if (r1 != r5) goto L_0x0362
        L_0x035f:
            r45 = 1
            goto L_0x0364
        L_0x0362:
            r45 = 0
        L_0x0364:
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r15.mListDimensionBehaviors
            r5 = 0
            r1 = r1[r5]
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r5 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r1 != r5) goto L_0x0373
            boolean r1 = r15 instanceof androidx.constraintlayout.core.widgets.ConstraintWidgetContainer
            if (r1 == 0) goto L_0x0373
            r1 = 1
            goto L_0x0374
        L_0x0373:
            r1 = 0
        L_0x0374:
            r29 = r1
            if (r29 == 0) goto L_0x037c
            r0 = 0
            r60 = r0
            goto L_0x037e
        L_0x037c:
            r60 = r0
        L_0x037e:
            r0 = 1
            androidx.constraintlayout.core.widgets.ConstraintAnchor r1 = r15.mCenter
            boolean r1 = r1.isConnected()
            if (r1 == 0) goto L_0x038b
            r0 = 0
            r30 = r0
            goto L_0x038d
        L_0x038b:
            r30 = r0
        L_0x038d:
            boolean[] r0 = r15.mIsInBarrier
            r1 = 0
            boolean r61 = r0[r1]
            r1 = 1
            boolean r62 = r0[r1]
            int r0 = r15.mHorizontalResolution
            r5 = 2
            r31 = 0
            if (r0 == r5) goto L_0x04f0
            boolean r0 = r15.resolvedHorizontal
            if (r0 != 0) goto L_0x04f0
            if (r75 == 0) goto L_0x044a
            androidx.constraintlayout.core.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            if (r0 == 0) goto L_0x044a
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r0 = r0.start
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x044a
            androidx.constraintlayout.core.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r0 = r0.end
            boolean r0 = r0.resolved
            if (r0 != 0) goto L_0x03b8
            r6 = 8
            goto L_0x044c
        L_0x03b8:
            if (r75 == 0) goto L_0x0432
            androidx.constraintlayout.core.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r0 = r0.start
            int r0 = r0.value
            r14.addEquality(r13, r0)
            androidx.constraintlayout.core.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r0 = r0.end
            int r0 = r0.value
            r14.addEquality(r12, r0)
            androidx.constraintlayout.core.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x041a
            if (r4 == 0) goto L_0x0402
            boolean[] r0 = r15.isTerminalWidget
            r1 = 0
            boolean r0 = r0[r1]
            if (r0 == 0) goto L_0x0402
            boolean r0 = r73.isInHorizontalChain()
            if (r0 != 0) goto L_0x0402
            androidx.constraintlayout.core.widgets.ConstraintWidget r0 = r15.mParent
            androidx.constraintlayout.core.widgets.ConstraintAnchor r0 = r0.mRight
            androidx.constraintlayout.core.SolverVariable r0 = r14.createObjectVariable(r0)
            r6 = 8
            r14.addGreaterThan(r0, r12, r1, r6)
            r63 = r2
            r65 = r3
            r66 = r4
            r67 = r8
            r68 = r9
            r69 = r10
            r70 = r11
            r71 = r12
            r72 = r13
            r64 = r23
            goto L_0x0504
        L_0x0402:
            r6 = 8
            r63 = r2
            r65 = r3
            r66 = r4
            r67 = r8
            r68 = r9
            r69 = r10
            r70 = r11
            r71 = r12
            r72 = r13
            r64 = r23
            goto L_0x0504
        L_0x041a:
            r6 = 8
            r63 = r2
            r65 = r3
            r66 = r4
            r67 = r8
            r68 = r9
            r69 = r10
            r70 = r11
            r71 = r12
            r72 = r13
            r64 = r23
            goto L_0x0504
        L_0x0432:
            r6 = 8
            r63 = r2
            r65 = r3
            r66 = r4
            r67 = r8
            r68 = r9
            r69 = r10
            r70 = r11
            r71 = r12
            r72 = r13
            r64 = r23
            goto L_0x0504
        L_0x044a:
            r6 = 8
        L_0x044c:
            androidx.constraintlayout.core.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x0458
            androidx.constraintlayout.core.widgets.ConstraintAnchor r0 = r0.mRight
            androidx.constraintlayout.core.SolverVariable r0 = r14.createObjectVariable(r0)
            r7 = r0
            goto L_0x045a
        L_0x0458:
            r7 = r31
        L_0x045a:
            androidx.constraintlayout.core.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x0465
            androidx.constraintlayout.core.widgets.ConstraintAnchor r0 = r0.mLeft
            androidx.constraintlayout.core.SolverVariable r0 = r14.createObjectVariable(r0)
            goto L_0x0467
        L_0x0465:
            r0 = r31
        L_0x0467:
            r16 = r6
            r6 = r0
            r18 = 1
            boolean[] r0 = r15.isTerminalWidget
            r20 = 0
            boolean r21 = r0[r20]
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r22 = r0[r20]
            androidx.constraintlayout.core.widgets.ConstraintAnchor r1 = r15.mLeft
            r27 = r2
            androidx.constraintlayout.core.widgets.ConstraintAnchor r2 = r15.mRight
            r32 = r2
            int r2 = r15.mX
            r33 = r2
            int r2 = r15.mMinWidth
            int[] r5 = r15.mMaxDimension
            r35 = r5[r20]
            float r5 = r15.mHorizontalBiasPercent
            r19 = 1
            r0 = r0[r19]
            r36 = r1
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r1) goto L_0x0497
            r37 = r19
            goto L_0x0499
        L_0x0497:
            r37 = r20
        L_0x0499:
            int r0 = r15.mMatchConstraintMinWidth
            r24 = r0
            int r0 = r15.mMatchConstraintMaxWidth
            r25 = r0
            float r0 = r15.mMatchConstraintPercentWidth
            r26 = r0
            r0 = r73
            r1 = r74
            r63 = r27
            r16 = r32
            r27 = r33
            r32 = r2
            r2 = r18
            r65 = r3
            r64 = r23
            r3 = r4
            r66 = r4
            r4 = r64
            r18 = r5
            r5 = r21
            r67 = r8
            r8 = r22
            r68 = r9
            r9 = r29
            r69 = r10
            r10 = r36
            r70 = r11
            r11 = r16
            r71 = r12
            r12 = r27
            r72 = r13
            r13 = r60
            r14 = r32
            r15 = r35
            r16 = r18
            r18 = r37
            r19 = r56
            r20 = r57
            r21 = r61
            r22 = r59
            r23 = r58
            r27 = r30
            r0.applyConstraints(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27)
            goto L_0x0504
        L_0x04f0:
            r63 = r2
            r65 = r3
            r66 = r4
            r67 = r8
            r68 = r9
            r69 = r10
            r70 = r11
            r71 = r12
            r72 = r13
            r64 = r23
        L_0x0504:
            r0 = 1
            if (r75 == 0) goto L_0x0568
            r7 = r73
            androidx.constraintlayout.core.widgets.analyzer.VerticalWidgetRun r1 = r7.verticalRun
            if (r1 == 0) goto L_0x056a
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r1 = r1.start
            boolean r1 = r1.resolved
            if (r1 == 0) goto L_0x056a
            androidx.constraintlayout.core.widgets.analyzer.VerticalWidgetRun r1 = r7.verticalRun
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r1 = r1.end
            boolean r1 = r1.resolved
            if (r1 == 0) goto L_0x056a
            androidx.constraintlayout.core.widgets.analyzer.VerticalWidgetRun r1 = r7.verticalRun
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r1 = r1.start
            int r1 = r1.value
            r8 = r74
            r9 = r70
            r8.addEquality(r9, r1)
            androidx.constraintlayout.core.widgets.analyzer.VerticalWidgetRun r1 = r7.verticalRun
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r1 = r1.end
            int r1 = r1.value
            r10 = r69
            r8.addEquality(r10, r1)
            androidx.constraintlayout.core.widgets.analyzer.VerticalWidgetRun r1 = r7.verticalRun
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r1 = r1.baseline
            int r1 = r1.value
            r11 = r68
            r8.addEquality(r11, r1)
            androidx.constraintlayout.core.widgets.ConstraintWidget r1 = r7.mParent
            if (r1 == 0) goto L_0x0562
            if (r57 != 0) goto L_0x055d
            if (r64 == 0) goto L_0x055d
            boolean[] r2 = r7.isTerminalWidget
            r3 = 1
            boolean r2 = r2[r3]
            if (r2 == 0) goto L_0x055a
            androidx.constraintlayout.core.widgets.ConstraintAnchor r1 = r1.mBottom
            androidx.constraintlayout.core.SolverVariable r1 = r8.createObjectVariable(r1)
            r2 = 8
            r12 = 0
            r8.addGreaterThan(r1, r10, r12, r2)
            goto L_0x0566
        L_0x055a:
            r2 = 8
            goto L_0x0560
        L_0x055d:
            r2 = 8
            r3 = 1
        L_0x0560:
            r12 = 0
            goto L_0x0566
        L_0x0562:
            r2 = 8
            r3 = 1
            r12 = 0
        L_0x0566:
            r0 = 0
            goto L_0x0576
        L_0x0568:
            r7 = r73
        L_0x056a:
            r8 = r74
            r11 = r68
            r10 = r69
            r9 = r70
            r2 = 8
            r3 = 1
            r12 = 0
        L_0x0576:
            int r1 = r7.mVerticalResolution
            r4 = 2
            if (r1 != r4) goto L_0x057e
            r0 = 0
            r13 = r0
            goto L_0x057f
        L_0x057e:
            r13 = r0
        L_0x057f:
            if (r13 == 0) goto L_0x0668
            boolean r0 = r7.resolvedVertical
            if (r0 != 0) goto L_0x0668
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r7.mListDimensionBehaviors
            r0 = r0[r3]
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r0 != r1) goto L_0x0593
            boolean r0 = r7 instanceof androidx.constraintlayout.core.widgets.ConstraintWidgetContainer
            if (r0 == 0) goto L_0x0593
            r0 = r3
            goto L_0x0594
        L_0x0593:
            r0 = r12
        L_0x0594:
            if (r0 == 0) goto L_0x059b
            r28 = 0
            r1 = r28
            goto L_0x059d
        L_0x059b:
            r1 = r28
        L_0x059d:
            androidx.constraintlayout.core.widgets.ConstraintWidget r4 = r7.mParent
            if (r4 == 0) goto L_0x05a8
            androidx.constraintlayout.core.widgets.ConstraintAnchor r4 = r4.mBottom
            androidx.constraintlayout.core.SolverVariable r4 = r8.createObjectVariable(r4)
            goto L_0x05aa
        L_0x05a8:
            r4 = r31
        L_0x05aa:
            androidx.constraintlayout.core.widgets.ConstraintWidget r5 = r7.mParent
            if (r5 == 0) goto L_0x05b7
            androidx.constraintlayout.core.widgets.ConstraintAnchor r5 = r5.mTop
            androidx.constraintlayout.core.SolverVariable r5 = r8.createObjectVariable(r5)
            r34 = r5
            goto L_0x05b9
        L_0x05b7:
            r34 = r31
        L_0x05b9:
            int r5 = r7.mBaselineDistance
            if (r5 > 0) goto L_0x05c1
            int r5 = r7.mVisibility
            if (r5 != r2) goto L_0x0605
        L_0x05c1:
            androidx.constraintlayout.core.widgets.ConstraintAnchor r5 = r7.mBaseline
            androidx.constraintlayout.core.widgets.ConstraintAnchor r5 = r5.mTarget
            if (r5 == 0) goto L_0x05f0
            int r5 = r73.getBaselineDistance()
            r8.addEquality(r11, r9, r5, r2)
            androidx.constraintlayout.core.widgets.ConstraintAnchor r5 = r7.mBaseline
            androidx.constraintlayout.core.widgets.ConstraintAnchor r5 = r5.mTarget
            androidx.constraintlayout.core.SolverVariable r5 = r8.createObjectVariable(r5)
            androidx.constraintlayout.core.widgets.ConstraintAnchor r6 = r7.mBaseline
            int r6 = r6.getMargin()
            r8.addEquality(r11, r5, r6, r2)
            r30 = 0
            if (r64 == 0) goto L_0x05ed
            androidx.constraintlayout.core.widgets.ConstraintAnchor r2 = r7.mBottom
            androidx.constraintlayout.core.SolverVariable r2 = r8.createObjectVariable(r2)
            r14 = 5
            r8.addGreaterThan(r4, r2, r12, r14)
        L_0x05ed:
            r2 = r30
            goto L_0x0607
        L_0x05f0:
            int r5 = r7.mVisibility
            if (r5 != r2) goto L_0x05fe
            androidx.constraintlayout.core.widgets.ConstraintAnchor r5 = r7.mBaseline
            int r5 = r5.getMargin()
            r8.addEquality(r11, r9, r5, r2)
            goto L_0x0605
        L_0x05fe:
            int r5 = r73.getBaselineDistance()
            r8.addEquality(r11, r9, r5, r2)
        L_0x0605:
            r2 = r30
        L_0x0607:
            r30 = 0
            boolean[] r5 = r7.isTerminalWidget
            boolean r33 = r5[r3]
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour[] r5 = r7.mListDimensionBehaviors
            r36 = r5[r3]
            androidx.constraintlayout.core.widgets.ConstraintAnchor r6 = r7.mTop
            androidx.constraintlayout.core.widgets.ConstraintAnchor r14 = r7.mBottom
            int r15 = r7.mY
            int r12 = r7.mMinHeight
            r68 = r11
            int[] r11 = r7.mMaxDimension
            r43 = r11[r3]
            float r11 = r7.mVerticalBiasPercent
            r16 = 0
            r5 = r5[r16]
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r3 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r5 != r3) goto L_0x062c
            r46 = 1
            goto L_0x062e
        L_0x062c:
            r46 = 0
        L_0x062e:
            int r3 = r7.mMatchConstraintMinHeight
            r52 = r3
            int r3 = r7.mMatchConstraintMaxHeight
            r53 = r3
            float r3 = r7.mMatchConstraintPercentHeight
            r54 = r3
            r28 = r73
            r29 = r74
            r31 = r64
            r32 = r66
            r35 = r4
            r37 = r0
            r38 = r6
            r39 = r14
            r40 = r15
            r41 = r1
            r42 = r12
            r44 = r11
            r47 = r57
            r48 = r56
            r49 = r62
            r50 = r58
            r51 = r59
            r55 = r2
            r28.applyConstraints(r29, r30, r31, r32, r33, r34, r35, r36, r37, r38, r39, r40, r41, r42, r43, r44, r45, r46, r47, r48, r49, r50, r51, r52, r53, r54, r55)
            r29 = r0
            r28 = r1
            r30 = r2
            goto L_0x066a
        L_0x0668:
            r68 = r11
        L_0x066a:
            if (r67 == 0) goto L_0x0690
            r11 = 8
            int r0 = r7.mResolvedDimensionRatioSide
            r1 = 1
            if (r0 != r1) goto L_0x0682
            float r5 = r7.mResolvedDimensionRatio
            r0 = r74
            r1 = r10
            r2 = r9
            r3 = r71
            r4 = r72
            r6 = r11
            r0.addRatio(r1, r2, r3, r4, r5, r6)
            goto L_0x0690
        L_0x0682:
            float r5 = r7.mResolvedDimensionRatio
            r0 = r74
            r1 = r71
            r2 = r72
            r3 = r10
            r4 = r9
            r6 = r11
            r0.addRatio(r1, r2, r3, r4, r5, r6)
        L_0x0690:
            androidx.constraintlayout.core.widgets.ConstraintAnchor r0 = r7.mCenter
            boolean r0 = r0.isConnected()
            if (r0 == 0) goto L_0x06b6
            androidx.constraintlayout.core.widgets.ConstraintAnchor r0 = r7.mCenter
            androidx.constraintlayout.core.widgets.ConstraintAnchor r0 = r0.getTarget()
            androidx.constraintlayout.core.widgets.ConstraintWidget r0 = r0.getOwner()
            float r1 = r7.mCircleConstraintAngle
            r2 = 1119092736(0x42b40000, float:90.0)
            float r1 = r1 + r2
            double r1 = (double) r1
            double r1 = java.lang.Math.toRadians(r1)
            float r1 = (float) r1
            androidx.constraintlayout.core.widgets.ConstraintAnchor r2 = r7.mCenter
            int r2 = r2.getMargin()
            r8.addCenterPoint(r7, r0, r1, r2)
        L_0x06b6:
            r0 = 0
            r7.resolvedHorizontal = r0
            r7.resolvedVertical = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.widgets.ConstraintWidget.addToSolver(androidx.constraintlayout.core.LinearSystem, boolean):void");
    }

    public boolean allowedInBarrier() {
        return this.mVisibility != 8;
    }

    public void connect(ConstraintAnchor.Type constraintFrom, ConstraintWidget target, ConstraintAnchor.Type constraintTo) {
        connect(constraintFrom, target, constraintTo, 0);
    }

    public void connect(ConstraintAnchor.Type constraintFrom, ConstraintWidget target, ConstraintAnchor.Type constraintTo, int margin) {
        if (constraintFrom == ConstraintAnchor.Type.CENTER) {
            if (constraintTo == ConstraintAnchor.Type.CENTER) {
                ConstraintAnchor anchor = getAnchor(ConstraintAnchor.Type.LEFT);
                ConstraintAnchor anchor2 = getAnchor(ConstraintAnchor.Type.RIGHT);
                ConstraintAnchor anchor3 = getAnchor(ConstraintAnchor.Type.TOP);
                ConstraintAnchor anchor4 = getAnchor(ConstraintAnchor.Type.BOTTOM);
                boolean z = false;
                boolean z2 = false;
                if ((anchor == null || !anchor.isConnected()) && (anchor2 == null || !anchor2.isConnected())) {
                    connect(ConstraintAnchor.Type.LEFT, target, ConstraintAnchor.Type.LEFT, 0);
                    connect(ConstraintAnchor.Type.RIGHT, target, ConstraintAnchor.Type.RIGHT, 0);
                    z = true;
                }
                if ((anchor3 == null || !anchor3.isConnected()) && (anchor4 == null || !anchor4.isConnected())) {
                    connect(ConstraintAnchor.Type.TOP, target, ConstraintAnchor.Type.TOP, 0);
                    connect(ConstraintAnchor.Type.BOTTOM, target, ConstraintAnchor.Type.BOTTOM, 0);
                    z2 = true;
                }
                if (z && z2) {
                    getAnchor(ConstraintAnchor.Type.CENTER).connect(target.getAnchor(ConstraintAnchor.Type.CENTER), 0);
                } else if (z) {
                    getAnchor(ConstraintAnchor.Type.CENTER_X).connect(target.getAnchor(ConstraintAnchor.Type.CENTER_X), 0);
                } else if (z2) {
                    getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(target.getAnchor(ConstraintAnchor.Type.CENTER_Y), 0);
                }
            } else if (constraintTo == ConstraintAnchor.Type.LEFT || constraintTo == ConstraintAnchor.Type.RIGHT) {
                connect(ConstraintAnchor.Type.LEFT, target, constraintTo, 0);
                connect(ConstraintAnchor.Type.RIGHT, target, constraintTo, 0);
                getAnchor(ConstraintAnchor.Type.CENTER).connect(target.getAnchor(constraintTo), 0);
            } else if (constraintTo == ConstraintAnchor.Type.TOP || constraintTo == ConstraintAnchor.Type.BOTTOM) {
                connect(ConstraintAnchor.Type.TOP, target, constraintTo, 0);
                connect(ConstraintAnchor.Type.BOTTOM, target, constraintTo, 0);
                getAnchor(ConstraintAnchor.Type.CENTER).connect(target.getAnchor(constraintTo), 0);
            }
        } else if (constraintFrom == ConstraintAnchor.Type.CENTER_X && (constraintTo == ConstraintAnchor.Type.LEFT || constraintTo == ConstraintAnchor.Type.RIGHT)) {
            ConstraintAnchor anchor5 = getAnchor(ConstraintAnchor.Type.LEFT);
            ConstraintAnchor anchor6 = target.getAnchor(constraintTo);
            ConstraintAnchor anchor7 = getAnchor(ConstraintAnchor.Type.RIGHT);
            anchor5.connect(anchor6, 0);
            anchor7.connect(anchor6, 0);
            getAnchor(ConstraintAnchor.Type.CENTER_X).connect(anchor6, 0);
        } else if (constraintFrom == ConstraintAnchor.Type.CENTER_Y && (constraintTo == ConstraintAnchor.Type.TOP || constraintTo == ConstraintAnchor.Type.BOTTOM)) {
            ConstraintAnchor anchor8 = target.getAnchor(constraintTo);
            getAnchor(ConstraintAnchor.Type.TOP).connect(anchor8, 0);
            getAnchor(ConstraintAnchor.Type.BOTTOM).connect(anchor8, 0);
            getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(anchor8, 0);
        } else if (constraintFrom == ConstraintAnchor.Type.CENTER_X && constraintTo == ConstraintAnchor.Type.CENTER_X) {
            getAnchor(ConstraintAnchor.Type.LEFT).connect(target.getAnchor(ConstraintAnchor.Type.LEFT), 0);
            getAnchor(ConstraintAnchor.Type.RIGHT).connect(target.getAnchor(ConstraintAnchor.Type.RIGHT), 0);
            getAnchor(ConstraintAnchor.Type.CENTER_X).connect(target.getAnchor(constraintTo), 0);
        } else if (constraintFrom == ConstraintAnchor.Type.CENTER_Y && constraintTo == ConstraintAnchor.Type.CENTER_Y) {
            getAnchor(ConstraintAnchor.Type.TOP).connect(target.getAnchor(ConstraintAnchor.Type.TOP), 0);
            getAnchor(ConstraintAnchor.Type.BOTTOM).connect(target.getAnchor(ConstraintAnchor.Type.BOTTOM), 0);
            getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(target.getAnchor(constraintTo), 0);
        } else {
            ConstraintAnchor anchor9 = getAnchor(constraintFrom);
            ConstraintAnchor anchor10 = target.getAnchor(constraintTo);
            if (anchor9.isValidConnection(anchor10)) {
                if (constraintFrom == ConstraintAnchor.Type.BASELINE) {
                    ConstraintAnchor anchor11 = getAnchor(ConstraintAnchor.Type.TOP);
                    ConstraintAnchor anchor12 = getAnchor(ConstraintAnchor.Type.BOTTOM);
                    if (anchor11 != null) {
                        anchor11.reset();
                    }
                    if (anchor12 != null) {
                        anchor12.reset();
                    }
                } else if (constraintFrom == ConstraintAnchor.Type.TOP || constraintFrom == ConstraintAnchor.Type.BOTTOM) {
                    ConstraintAnchor anchor13 = getAnchor(ConstraintAnchor.Type.BASELINE);
                    if (anchor13 != null) {
                        anchor13.reset();
                    }
                    ConstraintAnchor anchor14 = getAnchor(ConstraintAnchor.Type.CENTER);
                    if (anchor14.getTarget() != anchor10) {
                        anchor14.reset();
                    }
                    ConstraintAnchor opposite = getAnchor(constraintFrom).getOpposite();
                    ConstraintAnchor anchor15 = getAnchor(ConstraintAnchor.Type.CENTER_Y);
                    if (anchor15.isConnected()) {
                        opposite.reset();
                        anchor15.reset();
                    }
                } else if (constraintFrom == ConstraintAnchor.Type.LEFT || constraintFrom == ConstraintAnchor.Type.RIGHT) {
                    ConstraintAnchor anchor16 = getAnchor(ConstraintAnchor.Type.CENTER);
                    if (anchor16.getTarget() != anchor10) {
                        anchor16.reset();
                    }
                    ConstraintAnchor opposite2 = getAnchor(constraintFrom).getOpposite();
                    ConstraintAnchor anchor17 = getAnchor(ConstraintAnchor.Type.CENTER_X);
                    if (anchor17.isConnected()) {
                        opposite2.reset();
                        anchor17.reset();
                    }
                }
                anchor9.connect(anchor10, margin);
            }
        }
    }

    public void connect(ConstraintAnchor from, ConstraintAnchor to, int margin) {
        if (from.getOwner() == this) {
            connect(from.getType(), to.getOwner(), to.getType(), margin);
        }
    }

    public void connectCircularConstraint(ConstraintWidget target, float angle, int radius) {
        immediateConnect(ConstraintAnchor.Type.CENTER, target, ConstraintAnchor.Type.CENTER, radius, 0);
        this.mCircleConstraintAngle = angle;
    }

    public void copy(ConstraintWidget src, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        this.mHorizontalResolution = src.mHorizontalResolution;
        this.mVerticalResolution = src.mVerticalResolution;
        this.mMatchConstraintDefaultWidth = src.mMatchConstraintDefaultWidth;
        this.mMatchConstraintDefaultHeight = src.mMatchConstraintDefaultHeight;
        int[] iArr = this.mResolvedMatchConstraintDefault;
        int[] iArr2 = src.mResolvedMatchConstraintDefault;
        iArr[0] = iArr2[0];
        iArr[1] = iArr2[1];
        this.mMatchConstraintMinWidth = src.mMatchConstraintMinWidth;
        this.mMatchConstraintMaxWidth = src.mMatchConstraintMaxWidth;
        this.mMatchConstraintMinHeight = src.mMatchConstraintMinHeight;
        this.mMatchConstraintMaxHeight = src.mMatchConstraintMaxHeight;
        this.mMatchConstraintPercentHeight = src.mMatchConstraintPercentHeight;
        this.mIsWidthWrapContent = src.mIsWidthWrapContent;
        this.mIsHeightWrapContent = src.mIsHeightWrapContent;
        this.mResolvedDimensionRatioSide = src.mResolvedDimensionRatioSide;
        this.mResolvedDimensionRatio = src.mResolvedDimensionRatio;
        int[] iArr3 = src.mMaxDimension;
        this.mMaxDimension = Arrays.copyOf(iArr3, iArr3.length);
        this.mCircleConstraintAngle = src.mCircleConstraintAngle;
        this.hasBaseline = src.hasBaseline;
        this.inPlaceholder = src.inPlaceholder;
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mListDimensionBehaviors = (DimensionBehaviour[]) Arrays.copyOf(this.mListDimensionBehaviors, 2);
        ConstraintWidget constraintWidget = null;
        this.mParent = this.mParent == null ? null : hashMap.get(src.mParent);
        this.mWidth = src.mWidth;
        this.mHeight = src.mHeight;
        this.mDimensionRatio = src.mDimensionRatio;
        this.mDimensionRatioSide = src.mDimensionRatioSide;
        this.mX = src.mX;
        this.mY = src.mY;
        this.mRelX = src.mRelX;
        this.mRelY = src.mRelY;
        this.mOffsetX = src.mOffsetX;
        this.mOffsetY = src.mOffsetY;
        this.mBaselineDistance = src.mBaselineDistance;
        this.mMinWidth = src.mMinWidth;
        this.mMinHeight = src.mMinHeight;
        this.mHorizontalBiasPercent = src.mHorizontalBiasPercent;
        this.mVerticalBiasPercent = src.mVerticalBiasPercent;
        this.mCompanionWidget = src.mCompanionWidget;
        this.mContainerItemSkip = src.mContainerItemSkip;
        this.mVisibility = src.mVisibility;
        this.mAnimated = src.mAnimated;
        this.mDebugName = src.mDebugName;
        this.mType = src.mType;
        this.mDistToTop = src.mDistToTop;
        this.mDistToLeft = src.mDistToLeft;
        this.mDistToRight = src.mDistToRight;
        this.mDistToBottom = src.mDistToBottom;
        this.mLeftHasCentered = src.mLeftHasCentered;
        this.mRightHasCentered = src.mRightHasCentered;
        this.mTopHasCentered = src.mTopHasCentered;
        this.mBottomHasCentered = src.mBottomHasCentered;
        this.mHorizontalWrapVisited = src.mHorizontalWrapVisited;
        this.mVerticalWrapVisited = src.mVerticalWrapVisited;
        this.mHorizontalChainStyle = src.mHorizontalChainStyle;
        this.mVerticalChainStyle = src.mVerticalChainStyle;
        this.mHorizontalChainFixedPosition = src.mHorizontalChainFixedPosition;
        this.mVerticalChainFixedPosition = src.mVerticalChainFixedPosition;
        float[] fArr = this.mWeight;
        float[] fArr2 = src.mWeight;
        fArr[0] = fArr2[0];
        fArr[1] = fArr2[1];
        ConstraintWidget[] constraintWidgetArr = this.mListNextMatchConstraintsWidget;
        ConstraintWidget[] constraintWidgetArr2 = src.mListNextMatchConstraintsWidget;
        constraintWidgetArr[0] = constraintWidgetArr2[0];
        constraintWidgetArr[1] = constraintWidgetArr2[1];
        ConstraintWidget[] constraintWidgetArr3 = this.mNextChainWidget;
        ConstraintWidget[] constraintWidgetArr4 = src.mNextChainWidget;
        constraintWidgetArr3[0] = constraintWidgetArr4[0];
        constraintWidgetArr3[1] = constraintWidgetArr4[1];
        ConstraintWidget constraintWidget2 = src.mHorizontalNextWidget;
        this.mHorizontalNextWidget = constraintWidget2 == null ? null : hashMap.get(constraintWidget2);
        ConstraintWidget constraintWidget3 = src.mVerticalNextWidget;
        if (constraintWidget3 != null) {
            constraintWidget = hashMap.get(constraintWidget3);
        }
        this.mVerticalNextWidget = constraintWidget;
    }

    public void createObjectVariables(LinearSystem system) {
        SolverVariable createObjectVariable = system.createObjectVariable(this.mLeft);
        SolverVariable createObjectVariable2 = system.createObjectVariable(this.mTop);
        SolverVariable createObjectVariable3 = system.createObjectVariable(this.mRight);
        SolverVariable createObjectVariable4 = system.createObjectVariable(this.mBottom);
        if (this.mBaselineDistance > 0) {
            system.createObjectVariable(this.mBaseline);
        }
    }

    public void ensureMeasureRequested() {
        this.mMeasureRequested = true;
    }

    public void ensureWidgetRuns() {
        if (this.horizontalRun == null) {
            this.horizontalRun = new HorizontalWidgetRun(this);
        }
        if (this.verticalRun == null) {
            this.verticalRun = new VerticalWidgetRun(this);
        }
    }

    public ConstraintAnchor getAnchor(ConstraintAnchor.Type anchorType) {
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[anchorType.ordinal()]) {
            case 1:
                return this.mLeft;
            case 2:
                return this.mTop;
            case 3:
                return this.mRight;
            case 4:
                return this.mBottom;
            case 5:
                return this.mBaseline;
            case 6:
                return this.mCenter;
            case 7:
                return this.mCenterX;
            case 8:
                return this.mCenterY;
            case 9:
                return null;
            default:
                throw new AssertionError(anchorType.name());
        }
    }

    public ArrayList<ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }

    public int getBaselineDistance() {
        return this.mBaselineDistance;
    }

    public float getBiasPercent(int orientation) {
        if (orientation == 0) {
            return this.mHorizontalBiasPercent;
        }
        if (orientation == 1) {
            return this.mVerticalBiasPercent;
        }
        return -1.0f;
    }

    public int getBottom() {
        return getY() + this.mHeight;
    }

    public Object getCompanionWidget() {
        return this.mCompanionWidget;
    }

    public int getContainerItemSkip() {
        return this.mContainerItemSkip;
    }

    public String getDebugName() {
        return this.mDebugName;
    }

    public DimensionBehaviour getDimensionBehaviour(int orientation) {
        if (orientation == 0) {
            return getHorizontalDimensionBehaviour();
        }
        if (orientation == 1) {
            return getVerticalDimensionBehaviour();
        }
        return null;
    }

    public float getDimensionRatio() {
        return this.mDimensionRatio;
    }

    public int getDimensionRatioSide() {
        return this.mDimensionRatioSide;
    }

    public boolean getHasBaseline() {
        return this.hasBaseline;
    }

    public int getHeight() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mHeight;
    }

    public float getHorizontalBiasPercent() {
        return this.mHorizontalBiasPercent;
    }

    public ConstraintWidget getHorizontalChainControlWidget() {
        ConstraintWidget constraintWidget = null;
        if (!isInHorizontalChain()) {
            return null;
        }
        ConstraintWidget constraintWidget2 = this;
        while (constraintWidget == null && constraintWidget2 != null) {
            ConstraintAnchor anchor = constraintWidget2.getAnchor(ConstraintAnchor.Type.LEFT);
            ConstraintAnchor constraintAnchor = null;
            ConstraintAnchor target = anchor == null ? null : anchor.getTarget();
            ConstraintWidget owner = target == null ? null : target.getOwner();
            if (owner == getParent()) {
                return constraintWidget2;
            }
            if (owner != null) {
                constraintAnchor = owner.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget();
            }
            if (constraintAnchor == null || constraintAnchor.getOwner() == constraintWidget2) {
                constraintWidget2 = owner;
            } else {
                constraintWidget = constraintWidget2;
            }
        }
        return constraintWidget;
    }

    public int getHorizontalChainStyle() {
        return this.mHorizontalChainStyle;
    }

    public DimensionBehaviour getHorizontalDimensionBehaviour() {
        return this.mListDimensionBehaviors[0];
    }

    public int getHorizontalMargin() {
        int i = 0;
        ConstraintAnchor constraintAnchor = this.mLeft;
        if (constraintAnchor != null) {
            i = 0 + constraintAnchor.mMargin;
        }
        ConstraintAnchor constraintAnchor2 = this.mRight;
        return constraintAnchor2 != null ? i + constraintAnchor2.mMargin : i;
    }

    public int getLastHorizontalMeasureSpec() {
        return this.mLastHorizontalMeasureSpec;
    }

    public int getLastVerticalMeasureSpec() {
        return this.mLastVerticalMeasureSpec;
    }

    public int getLeft() {
        return getX();
    }

    public int getLength(int orientation) {
        if (orientation == 0) {
            return getWidth();
        }
        if (orientation == 1) {
            return getHeight();
        }
        return 0;
    }

    public int getMaxHeight() {
        return this.mMaxDimension[1];
    }

    public int getMaxWidth() {
        return this.mMaxDimension[0];
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public ConstraintWidget getNextChainMember(int orientation) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (orientation == 0) {
            if (this.mRight.mTarget == null || this.mRight.mTarget.mTarget != (constraintAnchor2 = this.mRight)) {
                return null;
            }
            return constraintAnchor2.mTarget.mOwner;
        } else if (orientation == 1 && this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == (constraintAnchor = this.mBottom)) {
            return constraintAnchor.mTarget.mOwner;
        } else {
            return null;
        }
    }

    public int getOptimizerWrapHeight() {
        int i;
        int i2 = this.mHeight;
        if (this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT) {
            return i2;
        }
        if (this.mMatchConstraintDefaultHeight == 1) {
            i = Math.max(this.mMatchConstraintMinHeight, i2);
        } else if (this.mMatchConstraintMinHeight > 0) {
            i = this.mMatchConstraintMinHeight;
            this.mHeight = i;
        } else {
            i = 0;
        }
        int i3 = this.mMatchConstraintMaxHeight;
        return (i3 <= 0 || i3 >= i) ? i : this.mMatchConstraintMaxHeight;
    }

    public int getOptimizerWrapWidth() {
        int i;
        int i2 = this.mWidth;
        if (this.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT) {
            return i2;
        }
        if (this.mMatchConstraintDefaultWidth == 1) {
            i = Math.max(this.mMatchConstraintMinWidth, i2);
        } else if (this.mMatchConstraintMinWidth > 0) {
            i = this.mMatchConstraintMinWidth;
            this.mWidth = i;
        } else {
            i = 0;
        }
        int i3 = this.mMatchConstraintMaxWidth;
        return (i3 <= 0 || i3 >= i) ? i : this.mMatchConstraintMaxWidth;
    }

    public ConstraintWidget getParent() {
        return this.mParent;
    }

    public ConstraintWidget getPreviousChainMember(int orientation) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (orientation == 0) {
            if (this.mLeft.mTarget == null || this.mLeft.mTarget.mTarget != (constraintAnchor2 = this.mLeft)) {
                return null;
            }
            return constraintAnchor2.mTarget.mOwner;
        } else if (orientation == 1 && this.mTop.mTarget != null && this.mTop.mTarget.mTarget == (constraintAnchor = this.mTop)) {
            return constraintAnchor.mTarget.mOwner;
        } else {
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public int getRelativePositioning(int orientation) {
        if (orientation == 0) {
            return this.mRelX;
        }
        if (orientation == 1) {
            return this.mRelY;
        }
        return 0;
    }

    public int getRight() {
        return getX() + this.mWidth;
    }

    /* access modifiers changed from: protected */
    public int getRootX() {
        return this.mX + this.mOffsetX;
    }

    /* access modifiers changed from: protected */
    public int getRootY() {
        return this.mY + this.mOffsetY;
    }

    public WidgetRun getRun(int orientation) {
        if (orientation == 0) {
            return this.horizontalRun;
        }
        if (orientation == 1) {
            return this.verticalRun;
        }
        return null;
    }

    public void getSceneString(StringBuilder ret) {
        ret.append("  " + this.stringId + ":{\n");
        ret.append("    actualWidth:" + this.mWidth);
        ret.append("\n");
        ret.append("    actualHeight:" + this.mHeight);
        ret.append("\n");
        ret.append("    actualLeft:" + this.mX);
        ret.append("\n");
        ret.append("    actualTop:" + this.mY);
        ret.append("\n");
        getSceneString(ret, "left", this.mLeft);
        getSceneString(ret, "top", this.mTop);
        getSceneString(ret, "right", this.mRight);
        getSceneString(ret, "bottom", this.mBottom);
        getSceneString(ret, "baseline", this.mBaseline);
        getSceneString(ret, "centerX", this.mCenterX);
        getSceneString(ret, "centerY", this.mCenterY);
        getSceneString(ret, "    width", this.mWidth, this.mMinWidth, this.mMaxDimension[0], this.mWidthOverride, this.mMatchConstraintMinWidth, this.mMatchConstraintDefaultWidth, this.mMatchConstraintPercentWidth, this.mWeight[0]);
        getSceneString(ret, "    height", this.mHeight, this.mMinHeight, this.mMaxDimension[1], this.mHeightOverride, this.mMatchConstraintMinHeight, this.mMatchConstraintDefaultHeight, this.mMatchConstraintPercentHeight, this.mWeight[1]);
        serializeDimensionRatio(ret, "    dimensionRatio", this.mDimensionRatio, this.mDimensionRatioSide);
        serializeAttribute(ret, "    horizontalBias", this.mHorizontalBiasPercent, DEFAULT_BIAS);
        serializeAttribute(ret, "    verticalBias", this.mVerticalBiasPercent, DEFAULT_BIAS);
        serializeAttribute(ret, "    horizontalChainStyle", this.mHorizontalChainStyle, 0);
        serializeAttribute(ret, "    verticalChainStyle", this.mVerticalChainStyle, 0);
        ret.append("  }");
    }

    public int getTop() {
        return getY();
    }

    public String getType() {
        return this.mType;
    }

    public float getVerticalBiasPercent() {
        return this.mVerticalBiasPercent;
    }

    public ConstraintWidget getVerticalChainControlWidget() {
        ConstraintWidget constraintWidget = null;
        if (!isInVerticalChain()) {
            return null;
        }
        ConstraintWidget constraintWidget2 = this;
        while (constraintWidget == null && constraintWidget2 != null) {
            ConstraintAnchor anchor = constraintWidget2.getAnchor(ConstraintAnchor.Type.TOP);
            ConstraintAnchor constraintAnchor = null;
            ConstraintAnchor target = anchor == null ? null : anchor.getTarget();
            ConstraintWidget owner = target == null ? null : target.getOwner();
            if (owner == getParent()) {
                return constraintWidget2;
            }
            if (owner != null) {
                constraintAnchor = owner.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget();
            }
            if (constraintAnchor == null || constraintAnchor.getOwner() == constraintWidget2) {
                constraintWidget2 = owner;
            } else {
                constraintWidget = constraintWidget2;
            }
        }
        return constraintWidget;
    }

    public int getVerticalChainStyle() {
        return this.mVerticalChainStyle;
    }

    public DimensionBehaviour getVerticalDimensionBehaviour() {
        return this.mListDimensionBehaviors[1];
    }

    public int getVerticalMargin() {
        int i = 0;
        if (this.mLeft != null) {
            i = 0 + this.mTop.mMargin;
        }
        return this.mRight != null ? i + this.mBottom.mMargin : i;
    }

    public int getVisibility() {
        return this.mVisibility;
    }

    public int getWidth() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mWidth;
    }

    public int getWrapBehaviorInParent() {
        return this.mWrapBehaviorInParent;
    }

    public int getX() {
        ConstraintWidget constraintWidget = this.mParent;
        return (constraintWidget == null || !(constraintWidget instanceof ConstraintWidgetContainer)) ? this.mX : ((ConstraintWidgetContainer) constraintWidget).mPaddingLeft + this.mX;
    }

    public int getY() {
        ConstraintWidget constraintWidget = this.mParent;
        return (constraintWidget == null || !(constraintWidget instanceof ConstraintWidgetContainer)) ? this.mY : ((ConstraintWidgetContainer) constraintWidget).mPaddingTop + this.mY;
    }

    public boolean hasBaseline() {
        return this.hasBaseline;
    }

    public boolean hasDanglingDimension(int orientation) {
        if (orientation == 0) {
            return (this.mLeft.mTarget != null ? 1 : 0) + (this.mRight.mTarget != null ? 1 : 0) < 2;
        }
        return ((this.mTop.mTarget != null ? 1 : 0) + (this.mBottom.mTarget != null ? 1 : 0)) + (this.mBaseline.mTarget != null ? 1 : 0) < 2;
    }

    public boolean hasDependencies() {
        int size = this.mAnchors.size();
        for (int i = 0; i < size; i++) {
            if (this.mAnchors.get(i).hasDependents()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasDimensionOverride() {
        return (this.mWidthOverride == -1 && this.mHeightOverride == -1) ? false : true;
    }

    public boolean hasResolvedTargets(int orientation, int size) {
        if (orientation == 0) {
            if (this.mLeft.mTarget != null && this.mLeft.mTarget.hasFinalValue() && this.mRight.mTarget != null && this.mRight.mTarget.hasFinalValue()) {
                return (this.mRight.mTarget.getFinalValue() - this.mRight.getMargin()) - (this.mLeft.mTarget.getFinalValue() + this.mLeft.getMargin()) >= size;
            }
        } else if (this.mTop.mTarget != null && this.mTop.mTarget.hasFinalValue() && this.mBottom.mTarget != null && this.mBottom.mTarget.hasFinalValue()) {
            return (this.mBottom.mTarget.getFinalValue() - this.mBottom.getMargin()) - (this.mTop.mTarget.getFinalValue() + this.mTop.getMargin()) >= size;
        }
        return false;
    }

    public void immediateConnect(ConstraintAnchor.Type startType, ConstraintWidget target, ConstraintAnchor.Type endType, int margin, int goneMargin) {
        getAnchor(startType).connect(target.getAnchor(endType), margin, goneMargin, true);
    }

    public boolean isAnimated() {
        return this.mAnimated;
    }

    public boolean isHeightWrapContent() {
        return this.mIsHeightWrapContent;
    }

    public boolean isHorizontalSolvingPassDone() {
        return this.horizontalSolvingPass;
    }

    public boolean isInBarrier(int orientation) {
        return this.mIsInBarrier[orientation];
    }

    public boolean isInHorizontalChain() {
        if (this.mLeft.mTarget == null || this.mLeft.mTarget.mTarget != this.mLeft) {
            return this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight;
        }
        return true;
    }

    public boolean isInPlaceholder() {
        return this.inPlaceholder;
    }

    public boolean isInVerticalChain() {
        if (this.mTop.mTarget == null || this.mTop.mTarget.mTarget != this.mTop) {
            return this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom;
        }
        return true;
    }

    public boolean isInVirtualLayout() {
        return this.mInVirtualLayout;
    }

    public boolean isMeasureRequested() {
        return this.mMeasureRequested && this.mVisibility != 8;
    }

    public boolean isResolvedHorizontally() {
        return this.resolvedHorizontal || (this.mLeft.hasFinalValue() && this.mRight.hasFinalValue());
    }

    public boolean isResolvedVertically() {
        return this.resolvedVertical || (this.mTop.hasFinalValue() && this.mBottom.hasFinalValue());
    }

    public boolean isRoot() {
        return this.mParent == null;
    }

    public boolean isSpreadHeight() {
        return this.mMatchConstraintDefaultHeight == 0 && this.mDimensionRatio == 0.0f && this.mMatchConstraintMinHeight == 0 && this.mMatchConstraintMaxHeight == 0 && this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT;
    }

    public boolean isSpreadWidth() {
        return this.mMatchConstraintDefaultWidth == 0 && this.mDimensionRatio == 0.0f && this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMaxWidth == 0 && this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT;
    }

    public boolean isVerticalSolvingPassDone() {
        return this.verticalSolvingPass;
    }

    public boolean isWidthWrapContent() {
        return this.mIsWidthWrapContent;
    }

    public void markHorizontalSolvingPassDone() {
        this.horizontalSolvingPass = true;
    }

    public void markVerticalSolvingPassDone() {
        this.verticalSolvingPass = true;
    }

    public boolean oppositeDimensionDependsOn(int orientation) {
        char c = orientation == 0 ? (char) 1 : 0;
        DimensionBehaviour[] dimensionBehaviourArr = this.mListDimensionBehaviors;
        return dimensionBehaviourArr[orientation] == DimensionBehaviour.MATCH_CONSTRAINT && dimensionBehaviourArr[c] == DimensionBehaviour.MATCH_CONSTRAINT;
    }

    public boolean oppositeDimensionsTied() {
        return this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT;
    }

    public void reset() {
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mParent = null;
        this.mCircleConstraintAngle = 0.0f;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
        this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
        this.mCompanionWidget = null;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mType = null;
        this.mHorizontalWrapVisited = false;
        this.mVerticalWrapVisited = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalChainFixedPosition = false;
        this.mVerticalChainFixedPosition = false;
        float[] fArr = this.mWeight;
        fArr[0] = -1.0f;
        fArr[1] = -1.0f;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        int[] iArr = this.mMaxDimension;
        iArr[0] = Integer.MAX_VALUE;
        iArr[1] = Integer.MAX_VALUE;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mMatchConstraintMaxWidth = Integer.MAX_VALUE;
        this.mMatchConstraintMaxHeight = Integer.MAX_VALUE;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mResolvedHasRatio = false;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mGroupsToSolver = false;
        boolean[] zArr = this.isTerminalWidget;
        zArr[0] = true;
        zArr[1] = true;
        this.mInVirtualLayout = false;
        boolean[] zArr2 = this.mIsInBarrier;
        zArr2[0] = false;
        zArr2[1] = false;
        this.mMeasureRequested = true;
        int[] iArr2 = this.mResolvedMatchConstraintDefault;
        iArr2[0] = 0;
        iArr2[1] = 0;
        this.mWidthOverride = -1;
        this.mHeightOverride = -1;
    }

    public void resetAllConstraints() {
        resetAnchors();
        setVerticalBiasPercent(DEFAULT_BIAS);
        setHorizontalBiasPercent(DEFAULT_BIAS);
    }

    public void resetAnchor(ConstraintAnchor anchor) {
        if (getParent() == null || !(getParent() instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            ConstraintAnchor anchor2 = getAnchor(ConstraintAnchor.Type.LEFT);
            ConstraintAnchor anchor3 = getAnchor(ConstraintAnchor.Type.RIGHT);
            ConstraintAnchor anchor4 = getAnchor(ConstraintAnchor.Type.TOP);
            ConstraintAnchor anchor5 = getAnchor(ConstraintAnchor.Type.BOTTOM);
            ConstraintAnchor anchor6 = getAnchor(ConstraintAnchor.Type.CENTER);
            ConstraintAnchor anchor7 = getAnchor(ConstraintAnchor.Type.CENTER_X);
            ConstraintAnchor anchor8 = getAnchor(ConstraintAnchor.Type.CENTER_Y);
            if (anchor == anchor6) {
                if (anchor2.isConnected() && anchor3.isConnected() && anchor2.getTarget() == anchor3.getTarget()) {
                    anchor2.reset();
                    anchor3.reset();
                }
                if (anchor4.isConnected() && anchor5.isConnected() && anchor4.getTarget() == anchor5.getTarget()) {
                    anchor4.reset();
                    anchor5.reset();
                }
                this.mHorizontalBiasPercent = 0.5f;
                this.mVerticalBiasPercent = 0.5f;
            } else if (anchor == anchor7) {
                if (anchor2.isConnected() && anchor3.isConnected() && anchor2.getTarget().getOwner() == anchor3.getTarget().getOwner()) {
                    anchor2.reset();
                    anchor3.reset();
                }
                this.mHorizontalBiasPercent = 0.5f;
            } else if (anchor == anchor8) {
                if (anchor4.isConnected() && anchor5.isConnected() && anchor4.getTarget().getOwner() == anchor5.getTarget().getOwner()) {
                    anchor4.reset();
                    anchor5.reset();
                }
                this.mVerticalBiasPercent = 0.5f;
            } else if (anchor == anchor2 || anchor == anchor3) {
                if (anchor2.isConnected() && anchor2.getTarget() == anchor3.getTarget()) {
                    anchor6.reset();
                }
            } else if ((anchor == anchor4 || anchor == anchor5) && anchor4.isConnected() && anchor4.getTarget() == anchor5.getTarget()) {
                anchor6.reset();
            }
            anchor.reset();
        }
    }

    public void resetAnchors() {
        ConstraintWidget parent = getParent();
        if (parent == null || !(parent instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            int size = this.mAnchors.size();
            for (int i = 0; i < size; i++) {
                this.mAnchors.get(i).reset();
            }
        }
    }

    public void resetFinalResolution() {
        this.resolvedHorizontal = false;
        this.resolvedVertical = false;
        this.horizontalSolvingPass = false;
        this.verticalSolvingPass = false;
        int size = this.mAnchors.size();
        for (int i = 0; i < size; i++) {
            this.mAnchors.get(i).resetFinalResolution();
        }
    }

    public void resetSolverVariables(Cache cache) {
        this.mLeft.resetSolverVariable(cache);
        this.mTop.resetSolverVariable(cache);
        this.mRight.resetSolverVariable(cache);
        this.mBottom.resetSolverVariable(cache);
        this.mBaseline.resetSolverVariable(cache);
        this.mCenter.resetSolverVariable(cache);
        this.mCenterX.resetSolverVariable(cache);
        this.mCenterY.resetSolverVariable(cache);
    }

    public void resetSolvingPassFlag() {
        this.horizontalSolvingPass = false;
        this.verticalSolvingPass = false;
    }

    public StringBuilder serialize(StringBuilder ret) {
        ret.append("{\n");
        serializeAnchor(ret, "left", this.mLeft);
        serializeAnchor(ret, "top", this.mTop);
        serializeAnchor(ret, "right", this.mRight);
        serializeAnchor(ret, "bottom", this.mBottom);
        serializeAnchor(ret, "baseline", this.mBaseline);
        serializeAnchor(ret, "centerX", this.mCenterX);
        serializeAnchor(ret, "centerY", this.mCenterY);
        serializeCircle(ret, this.mCenter, this.mCircleConstraintAngle);
        serializeSize(ret, "width", this.mWidth, this.mMinWidth, this.mMaxDimension[0], this.mWidthOverride, this.mMatchConstraintMinWidth, this.mMatchConstraintDefaultWidth, this.mMatchConstraintPercentWidth, this.mWeight[0]);
        serializeSize(ret, "height", this.mHeight, this.mMinHeight, this.mMaxDimension[1], this.mHeightOverride, this.mMatchConstraintMinHeight, this.mMatchConstraintDefaultHeight, this.mMatchConstraintPercentHeight, this.mWeight[1]);
        serializeDimensionRatio(ret, "dimensionRatio", this.mDimensionRatio, this.mDimensionRatioSide);
        serializeAttribute(ret, "horizontalBias", this.mHorizontalBiasPercent, DEFAULT_BIAS);
        serializeAttribute(ret, "verticalBias", this.mVerticalBiasPercent, DEFAULT_BIAS);
        ret.append("}\n");
        return ret;
    }

    public void setAnimated(boolean animated) {
        this.mAnimated = animated;
    }

    public void setBaselineDistance(int baseline) {
        this.mBaselineDistance = baseline;
        this.hasBaseline = baseline > 0;
    }

    public void setCompanionWidget(Object companion) {
        this.mCompanionWidget = companion;
    }

    public void setContainerItemSkip(int skip) {
        if (skip >= 0) {
            this.mContainerItemSkip = skip;
        } else {
            this.mContainerItemSkip = 0;
        }
    }

    public void setDebugName(String name) {
        this.mDebugName = name;
    }

    public void setDebugSolverName(LinearSystem system, String name) {
        this.mDebugName = name;
        SolverVariable createObjectVariable = system.createObjectVariable(this.mLeft);
        SolverVariable createObjectVariable2 = system.createObjectVariable(this.mTop);
        SolverVariable createObjectVariable3 = system.createObjectVariable(this.mRight);
        SolverVariable createObjectVariable4 = system.createObjectVariable(this.mBottom);
        createObjectVariable.setName(name + ".left");
        createObjectVariable2.setName(name + ".top");
        createObjectVariable3.setName(name + ".right");
        createObjectVariable4.setName(name + ".bottom");
        system.createObjectVariable(this.mBaseline).setName(name + ".baseline");
    }

    public void setDimension(int w, int h) {
        this.mWidth = w;
        int i = this.mMinWidth;
        if (w < i) {
            this.mWidth = i;
        }
        this.mHeight = h;
        int i2 = this.mMinHeight;
        if (h < i2) {
            this.mHeight = i2;
        }
    }

    public void setDimensionRatio(float ratio, int dimensionRatioSide) {
        this.mDimensionRatio = ratio;
        this.mDimensionRatioSide = dimensionRatioSide;
    }

    public void setDimensionRatio(String ratio) {
        int i;
        if (ratio == null || ratio.length() == 0) {
            this.mDimensionRatio = 0.0f;
            return;
        }
        int i2 = -1;
        float f = 0.0f;
        int length = ratio.length();
        int indexOf = ratio.indexOf(44);
        if (indexOf <= 0 || indexOf >= length - 1) {
            i = 0;
        } else {
            String substring = ratio.substring(0, indexOf);
            if (substring.equalsIgnoreCase("W")) {
                i2 = 0;
            } else if (substring.equalsIgnoreCase("H")) {
                i2 = 1;
            }
            i = indexOf + 1;
        }
        int indexOf2 = ratio.indexOf(58);
        if (indexOf2 < 0 || indexOf2 >= length - 1) {
            String substring2 = ratio.substring(i);
            if (substring2.length() > 0) {
                try {
                    f = Float.parseFloat(substring2);
                } catch (NumberFormatException e) {
                }
            }
        } else {
            String substring3 = ratio.substring(i, indexOf2);
            String substring4 = ratio.substring(indexOf2 + 1);
            if (substring3.length() > 0 && substring4.length() > 0) {
                try {
                    float parseFloat = Float.parseFloat(substring3);
                    float parseFloat2 = Float.parseFloat(substring4);
                    if (parseFloat > 0.0f && parseFloat2 > 0.0f) {
                        f = i2 == 1 ? Math.abs(parseFloat2 / parseFloat) : Math.abs(parseFloat / parseFloat2);
                    }
                } catch (NumberFormatException e2) {
                }
            }
        }
        if (f > 0.0f) {
            this.mDimensionRatio = f;
            this.mDimensionRatioSide = i2;
        }
    }

    public void setFinalBaseline(int baselineValue) {
        if (this.hasBaseline) {
            int i = baselineValue - this.mBaselineDistance;
            this.mY = i;
            this.mTop.setFinalValue(i);
            this.mBottom.setFinalValue(this.mHeight + i);
            this.mBaseline.setFinalValue(baselineValue);
            this.resolvedVertical = true;
        }
    }

    public void setFinalFrame(int left, int top, int right, int bottom, int baseline, int orientation) {
        setFrame(left, top, right, bottom);
        setBaselineDistance(baseline);
        if (orientation == 0) {
            this.resolvedHorizontal = true;
            this.resolvedVertical = false;
        } else if (orientation == 1) {
            this.resolvedHorizontal = false;
            this.resolvedVertical = true;
        } else if (orientation == 2) {
            this.resolvedHorizontal = true;
            this.resolvedVertical = true;
        } else {
            this.resolvedHorizontal = false;
            this.resolvedVertical = false;
        }
    }

    public void setFinalHorizontal(int x1, int x2) {
        if (!this.resolvedHorizontal) {
            this.mLeft.setFinalValue(x1);
            this.mRight.setFinalValue(x2);
            this.mX = x1;
            this.mWidth = x2 - x1;
            this.resolvedHorizontal = true;
        }
    }

    public void setFinalLeft(int x1) {
        this.mLeft.setFinalValue(x1);
        this.mX = x1;
    }

    public void setFinalTop(int y1) {
        this.mTop.setFinalValue(y1);
        this.mY = y1;
    }

    public void setFinalVertical(int y1, int y2) {
        if (!this.resolvedVertical) {
            this.mTop.setFinalValue(y1);
            this.mBottom.setFinalValue(y2);
            this.mY = y1;
            this.mHeight = y2 - y1;
            if (this.hasBaseline) {
                this.mBaseline.setFinalValue(this.mBaselineDistance + y1);
            }
            this.resolvedVertical = true;
        }
    }

    public void setFrame(int start, int end, int orientation) {
        if (orientation == 0) {
            setHorizontalDimension(start, end);
        } else if (orientation == 1) {
            setVerticalDimension(start, end);
        }
    }

    public void setFrame(int left, int top, int right, int bottom) {
        int i = right - left;
        int i2 = bottom - top;
        this.mX = left;
        this.mY = top;
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED && i < this.mWidth) {
            i = this.mWidth;
        }
        if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED && i2 < this.mHeight) {
            i2 = this.mHeight;
        }
        this.mWidth = i;
        this.mHeight = i2;
        int i3 = this.mMinHeight;
        if (i2 < i3) {
            this.mHeight = i3;
        }
        int i4 = this.mMinWidth;
        if (i < i4) {
            this.mWidth = i4;
        }
        if (this.mMatchConstraintMaxWidth > 0 && this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT) {
            this.mWidth = Math.min(this.mWidth, this.mMatchConstraintMaxWidth);
        }
        if (this.mMatchConstraintMaxHeight > 0 && this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT) {
            this.mHeight = Math.min(this.mHeight, this.mMatchConstraintMaxHeight);
        }
        int i5 = this.mWidth;
        if (i != i5) {
            this.mWidthOverride = i5;
        }
        int i6 = this.mHeight;
        if (i2 != i6) {
            this.mHeightOverride = i6;
        }
    }

    public void setGoneMargin(ConstraintAnchor.Type type, int goneMargin) {
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[type.ordinal()]) {
            case 1:
                this.mLeft.mGoneMargin = goneMargin;
                return;
            case 2:
                this.mTop.mGoneMargin = goneMargin;
                return;
            case 3:
                this.mRight.mGoneMargin = goneMargin;
                return;
            case 4:
                this.mBottom.mGoneMargin = goneMargin;
                return;
            case 5:
                this.mBaseline.mGoneMargin = goneMargin;
                return;
            default:
                return;
        }
    }

    public void setHasBaseline(boolean hasBaseline2) {
        this.hasBaseline = hasBaseline2;
    }

    public void setHeight(int h) {
        this.mHeight = h;
        int i = this.mMinHeight;
        if (h < i) {
            this.mHeight = i;
        }
    }

    public void setHeightWrapContent(boolean heightWrapContent) {
        this.mIsHeightWrapContent = heightWrapContent;
    }

    public void setHorizontalBiasPercent(float horizontalBiasPercent) {
        this.mHorizontalBiasPercent = horizontalBiasPercent;
    }

    public void setHorizontalChainStyle(int horizontalChainStyle) {
        this.mHorizontalChainStyle = horizontalChainStyle;
    }

    public void setHorizontalDimension(int left, int right) {
        this.mX = left;
        int i = right - left;
        this.mWidth = i;
        int i2 = this.mMinWidth;
        if (i < i2) {
            this.mWidth = i2;
        }
    }

    public void setHorizontalDimensionBehaviour(DimensionBehaviour behaviour) {
        this.mListDimensionBehaviors[0] = behaviour;
    }

    public void setHorizontalMatchStyle(int horizontalMatchStyle, int min, int max, float percent) {
        this.mMatchConstraintDefaultWidth = horizontalMatchStyle;
        this.mMatchConstraintMinWidth = min;
        this.mMatchConstraintMaxWidth = max == Integer.MAX_VALUE ? 0 : max;
        this.mMatchConstraintPercentWidth = percent;
        if (percent > 0.0f && percent < 1.0f && horizontalMatchStyle == 0) {
            this.mMatchConstraintDefaultWidth = 2;
        }
    }

    public void setHorizontalWeight(float horizontalWeight) {
        this.mWeight[0] = horizontalWeight;
    }

    /* access modifiers changed from: protected */
    public void setInBarrier(int orientation, boolean value) {
        this.mIsInBarrier[orientation] = value;
    }

    public void setInPlaceholder(boolean inPlaceholder2) {
        this.inPlaceholder = inPlaceholder2;
    }

    public void setInVirtualLayout(boolean inVirtualLayout) {
        this.mInVirtualLayout = inVirtualLayout;
    }

    public void setLastMeasureSpec(int horizontal, int vertical) {
        this.mLastHorizontalMeasureSpec = horizontal;
        this.mLastVerticalMeasureSpec = vertical;
        setMeasureRequested(false);
    }

    public void setLength(int length, int orientation) {
        if (orientation == 0) {
            setWidth(length);
        } else if (orientation == 1) {
            setHeight(length);
        }
    }

    public void setMaxHeight(int maxHeight) {
        this.mMaxDimension[1] = maxHeight;
    }

    public void setMaxWidth(int maxWidth) {
        this.mMaxDimension[0] = maxWidth;
    }

    public void setMeasureRequested(boolean measureRequested) {
        this.mMeasureRequested = measureRequested;
    }

    public void setMinHeight(int h) {
        if (h < 0) {
            this.mMinHeight = 0;
        } else {
            this.mMinHeight = h;
        }
    }

    public void setMinWidth(int w) {
        if (w < 0) {
            this.mMinWidth = 0;
        } else {
            this.mMinWidth = w;
        }
    }

    public void setOffset(int x, int y) {
        this.mOffsetX = x;
        this.mOffsetY = y;
    }

    public void setOrigin(int x, int y) {
        this.mX = x;
        this.mY = y;
    }

    public void setParent(ConstraintWidget widget) {
        this.mParent = widget;
    }

    /* access modifiers changed from: package-private */
    public void setRelativePositioning(int offset, int orientation) {
        if (orientation == 0) {
            this.mRelX = offset;
        } else if (orientation == 1) {
            this.mRelY = offset;
        }
    }

    public void setType(String type) {
        this.mType = type;
    }

    public void setVerticalBiasPercent(float verticalBiasPercent) {
        this.mVerticalBiasPercent = verticalBiasPercent;
    }

    public void setVerticalChainStyle(int verticalChainStyle) {
        this.mVerticalChainStyle = verticalChainStyle;
    }

    public void setVerticalDimension(int top, int bottom) {
        this.mY = top;
        int i = bottom - top;
        this.mHeight = i;
        int i2 = this.mMinHeight;
        if (i < i2) {
            this.mHeight = i2;
        }
    }

    public void setVerticalDimensionBehaviour(DimensionBehaviour behaviour) {
        this.mListDimensionBehaviors[1] = behaviour;
    }

    public void setVerticalMatchStyle(int verticalMatchStyle, int min, int max, float percent) {
        this.mMatchConstraintDefaultHeight = verticalMatchStyle;
        this.mMatchConstraintMinHeight = min;
        this.mMatchConstraintMaxHeight = max == Integer.MAX_VALUE ? 0 : max;
        this.mMatchConstraintPercentHeight = percent;
        if (percent > 0.0f && percent < 1.0f && verticalMatchStyle == 0) {
            this.mMatchConstraintDefaultHeight = 2;
        }
    }

    public void setVerticalWeight(float verticalWeight) {
        this.mWeight[1] = verticalWeight;
    }

    public void setVisibility(int visibility) {
        this.mVisibility = visibility;
    }

    public void setWidth(int w) {
        this.mWidth = w;
        int i = this.mMinWidth;
        if (w < i) {
            this.mWidth = i;
        }
    }

    public void setWidthWrapContent(boolean widthWrapContent) {
        this.mIsWidthWrapContent = widthWrapContent;
    }

    public void setWrapBehaviorInParent(int behavior) {
        if (behavior >= 0 && behavior <= 3) {
            this.mWrapBehaviorInParent = behavior;
        }
    }

    public void setX(int x) {
        this.mX = x;
    }

    public void setY(int y) {
        this.mY = y;
    }

    public void setupDimensionRatio(boolean hParentWrapContent, boolean vParentWrapContent, boolean horizontalDimensionFixed, boolean verticalDimensionFixed) {
        if (this.mResolvedDimensionRatioSide == -1) {
            if (horizontalDimensionFixed && !verticalDimensionFixed) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (!horizontalDimensionFixed && verticalDimensionFixed) {
                this.mResolvedDimensionRatioSide = 1;
                if (this.mDimensionRatioSide == -1) {
                    this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                }
            }
        }
        if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
            this.mResolvedDimensionRatioSide = 1;
        } else if (this.mResolvedDimensionRatioSide == 1 && (!this.mLeft.isConnected() || !this.mRight.isConnected())) {
            this.mResolvedDimensionRatioSide = 0;
        }
        if (this.mResolvedDimensionRatioSide == -1 && (!this.mTop.isConnected() || !this.mBottom.isConnected() || !this.mLeft.isConnected() || !this.mRight.isConnected())) {
            if (this.mTop.isConnected() && this.mBottom.isConnected()) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            int i = this.mMatchConstraintMinWidth;
            if (i > 0 && this.mMatchConstraintMinHeight == 0) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (i == 0 && this.mMatchConstraintMinHeight > 0) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String str = this.mType;
        String str2 = HttpUrl.FRAGMENT_ENCODE_SET;
        StringBuilder append = sb.append(str != null ? "type: " + this.mType + " " : str2);
        if (this.mDebugName != null) {
            str2 = "id: " + this.mDebugName + " ";
        }
        return append.append(str2).append("(").append(this.mX).append(", ").append(this.mY).append(") - (").append(this.mWidth).append(" x ").append(this.mHeight).append(")").toString();
    }

    public void updateFromRuns(boolean updateHorizontal, boolean updateVertical) {
        boolean updateHorizontal2 = updateHorizontal & this.horizontalRun.isResolved();
        boolean updateVertical2 = updateVertical & this.verticalRun.isResolved();
        int i = this.horizontalRun.start.value;
        int i2 = this.verticalRun.start.value;
        int i3 = this.horizontalRun.end.value;
        int i4 = this.verticalRun.end.value;
        int i5 = i4 - i2;
        if (i3 - i < 0 || i5 < 0 || i == Integer.MIN_VALUE || i == Integer.MAX_VALUE || i2 == Integer.MIN_VALUE || i2 == Integer.MAX_VALUE || i3 == Integer.MIN_VALUE || i3 == Integer.MAX_VALUE || i4 == Integer.MIN_VALUE || i4 == Integer.MAX_VALUE) {
            i = 0;
            i2 = 0;
            i3 = 0;
            i4 = 0;
        }
        int i6 = i3 - i;
        int i7 = i4 - i2;
        if (updateHorizontal2) {
            this.mX = i;
        }
        if (updateVertical2) {
            this.mY = i2;
        }
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        if (updateHorizontal2) {
            if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED && i6 < this.mWidth) {
                i6 = this.mWidth;
            }
            this.mWidth = i6;
            int i8 = this.mMinWidth;
            if (i6 < i8) {
                this.mWidth = i8;
            }
        }
        if (updateVertical2) {
            if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED && i7 < this.mHeight) {
                i7 = this.mHeight;
            }
            this.mHeight = i7;
            int i9 = this.mMinHeight;
            if (i7 < i9) {
                this.mHeight = i9;
            }
        }
    }

    public void updateFromSolver(LinearSystem system, boolean optimize) {
        VerticalWidgetRun verticalWidgetRun;
        HorizontalWidgetRun horizontalWidgetRun;
        int objectVariableValue = system.getObjectVariableValue(this.mLeft);
        int objectVariableValue2 = system.getObjectVariableValue(this.mTop);
        int objectVariableValue3 = system.getObjectVariableValue(this.mRight);
        int objectVariableValue4 = system.getObjectVariableValue(this.mBottom);
        if (optimize && (horizontalWidgetRun = this.horizontalRun) != null && horizontalWidgetRun.start.resolved && this.horizontalRun.end.resolved) {
            objectVariableValue = this.horizontalRun.start.value;
            objectVariableValue3 = this.horizontalRun.end.value;
        }
        if (optimize && (verticalWidgetRun = this.verticalRun) != null && verticalWidgetRun.start.resolved && this.verticalRun.end.resolved) {
            objectVariableValue2 = this.verticalRun.start.value;
            objectVariableValue4 = this.verticalRun.end.value;
        }
        int i = objectVariableValue4 - objectVariableValue2;
        if (objectVariableValue3 - objectVariableValue < 0 || i < 0 || objectVariableValue == Integer.MIN_VALUE || objectVariableValue == Integer.MAX_VALUE || objectVariableValue2 == Integer.MIN_VALUE || objectVariableValue2 == Integer.MAX_VALUE || objectVariableValue3 == Integer.MIN_VALUE || objectVariableValue3 == Integer.MAX_VALUE || objectVariableValue4 == Integer.MIN_VALUE || objectVariableValue4 == Integer.MAX_VALUE) {
            objectVariableValue = 0;
            objectVariableValue2 = 0;
            objectVariableValue3 = 0;
            objectVariableValue4 = 0;
        }
        setFrame(objectVariableValue, objectVariableValue2, objectVariableValue3, objectVariableValue4);
    }
}
