package androidx.constraintlayout.core.widgets;

import androidx.constraintlayout.core.LinearSystem;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Flow extends VirtualLayout {
    public static final int HORIZONTAL_ALIGN_CENTER = 2;
    public static final int HORIZONTAL_ALIGN_END = 1;
    public static final int HORIZONTAL_ALIGN_START = 0;
    public static final int VERTICAL_ALIGN_BASELINE = 3;
    public static final int VERTICAL_ALIGN_BOTTOM = 1;
    public static final int VERTICAL_ALIGN_CENTER = 2;
    public static final int VERTICAL_ALIGN_TOP = 0;
    public static final int WRAP_ALIGNED = 2;
    public static final int WRAP_CHAIN = 1;
    public static final int WRAP_CHAIN_NEW = 3;
    public static final int WRAP_NONE = 0;
    private ConstraintWidget[] mAlignedBiggestElementsInCols = null;
    private ConstraintWidget[] mAlignedBiggestElementsInRows = null;
    private int[] mAlignedDimensions = null;
    private ArrayList<WidgetsList> mChainList = new ArrayList<>();
    /* access modifiers changed from: private */
    public ConstraintWidget[] mDisplayedWidgets;
    /* access modifiers changed from: private */
    public int mDisplayedWidgetsCount = 0;
    /* access modifiers changed from: private */
    public float mFirstHorizontalBias = 0.5f;
    /* access modifiers changed from: private */
    public int mFirstHorizontalStyle = -1;
    /* access modifiers changed from: private */
    public float mFirstVerticalBias = 0.5f;
    /* access modifiers changed from: private */
    public int mFirstVerticalStyle = -1;
    /* access modifiers changed from: private */
    public int mHorizontalAlign = 2;
    /* access modifiers changed from: private */
    public float mHorizontalBias = 0.5f;
    /* access modifiers changed from: private */
    public int mHorizontalGap = 0;
    /* access modifiers changed from: private */
    public int mHorizontalStyle = -1;
    /* access modifiers changed from: private */
    public float mLastHorizontalBias = 0.5f;
    /* access modifiers changed from: private */
    public int mLastHorizontalStyle = -1;
    /* access modifiers changed from: private */
    public float mLastVerticalBias = 0.5f;
    /* access modifiers changed from: private */
    public int mLastVerticalStyle = -1;
    private int mMaxElementsWrap = -1;
    private int mOrientation = 0;
    /* access modifiers changed from: private */
    public int mVerticalAlign = 2;
    /* access modifiers changed from: private */
    public float mVerticalBias = 0.5f;
    /* access modifiers changed from: private */
    public int mVerticalGap = 0;
    /* access modifiers changed from: private */
    public int mVerticalStyle = -1;
    private int mWrapMode = 0;

    private class WidgetsList {
        /* access modifiers changed from: private */
        public ConstraintWidget biggest = null;
        int biggestDimension = 0;
        private ConstraintAnchor mBottom;
        private int mCount = 0;
        private int mHeight = 0;
        private ConstraintAnchor mLeft;
        private int mMax = 0;
        private int mNbMatchConstraintsWidgets = 0;
        private int mOrientation = 0;
        private int mPaddingBottom = 0;
        private int mPaddingLeft = 0;
        private int mPaddingRight = 0;
        private int mPaddingTop = 0;
        private ConstraintAnchor mRight;
        private int mStartIndex = 0;
        private ConstraintAnchor mTop;
        private int mWidth = 0;

        public WidgetsList(int orientation, ConstraintAnchor left, ConstraintAnchor top, ConstraintAnchor right, ConstraintAnchor bottom, int max) {
            this.mOrientation = orientation;
            this.mLeft = left;
            this.mTop = top;
            this.mRight = right;
            this.mBottom = bottom;
            this.mPaddingLeft = Flow.this.getPaddingLeft();
            this.mPaddingTop = Flow.this.getPaddingTop();
            this.mPaddingRight = Flow.this.getPaddingRight();
            this.mPaddingBottom = Flow.this.getPaddingBottom();
            this.mMax = max;
        }

        private void recomputeDimensions() {
            this.mWidth = 0;
            this.mHeight = 0;
            this.biggest = null;
            this.biggestDimension = 0;
            int i = this.mCount;
            int i2 = 0;
            while (i2 < i && this.mStartIndex + i2 < Flow.this.mDisplayedWidgetsCount) {
                ConstraintWidget constraintWidget = Flow.this.mDisplayedWidgets[this.mStartIndex + i2];
                if (this.mOrientation == 0) {
                    int width = constraintWidget.getWidth();
                    int access$000 = Flow.this.mHorizontalGap;
                    if (constraintWidget.getVisibility() == 8) {
                        access$000 = 0;
                    }
                    this.mWidth += width + access$000;
                    int access$300 = Flow.this.getWidgetHeight(constraintWidget, this.mMax);
                    if (this.biggest == null || this.biggestDimension < access$300) {
                        this.biggest = constraintWidget;
                        this.biggestDimension = access$300;
                        this.mHeight = access$300;
                    }
                } else {
                    int access$200 = Flow.this.getWidgetWidth(constraintWidget, this.mMax);
                    int access$3002 = Flow.this.getWidgetHeight(constraintWidget, this.mMax);
                    int access$100 = Flow.this.mVerticalGap;
                    if (constraintWidget.getVisibility() == 8) {
                        access$100 = 0;
                    }
                    this.mHeight += access$3002 + access$100;
                    if (this.biggest == null || this.biggestDimension < access$200) {
                        this.biggest = constraintWidget;
                        this.biggestDimension = access$200;
                        this.mWidth = access$200;
                    }
                }
                i2++;
            }
        }

        public void add(ConstraintWidget widget) {
            if (this.mOrientation == 0) {
                int access$200 = Flow.this.getWidgetWidth(widget, this.mMax);
                if (widget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    this.mNbMatchConstraintsWidgets++;
                    access$200 = 0;
                }
                int access$000 = Flow.this.mHorizontalGap;
                if (widget.getVisibility() == 8) {
                    access$000 = 0;
                }
                this.mWidth += access$200 + access$000;
                int access$300 = Flow.this.getWidgetHeight(widget, this.mMax);
                if (this.biggest == null || this.biggestDimension < access$300) {
                    this.biggest = widget;
                    this.biggestDimension = access$300;
                    this.mHeight = access$300;
                }
            } else {
                int access$2002 = Flow.this.getWidgetWidth(widget, this.mMax);
                int access$3002 = Flow.this.getWidgetHeight(widget, this.mMax);
                if (widget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    this.mNbMatchConstraintsWidgets++;
                    access$3002 = 0;
                }
                int access$100 = Flow.this.mVerticalGap;
                if (widget.getVisibility() == 8) {
                    access$100 = 0;
                }
                this.mHeight += access$3002 + access$100;
                if (this.biggest == null || this.biggestDimension < access$2002) {
                    this.biggest = widget;
                    this.biggestDimension = access$2002;
                    this.mWidth = access$2002;
                }
            }
            this.mCount++;
        }

        public void clear() {
            this.biggestDimension = 0;
            this.biggest = null;
            this.mWidth = 0;
            this.mHeight = 0;
            this.mStartIndex = 0;
            this.mCount = 0;
            this.mNbMatchConstraintsWidgets = 0;
        }

        public void createConstraints(boolean isInRtl, int chainIndex, boolean isLastChain) {
            int i = this.mCount;
            int i2 = 0;
            while (i2 < i && this.mStartIndex + i2 < Flow.this.mDisplayedWidgetsCount) {
                ConstraintWidget constraintWidget = Flow.this.mDisplayedWidgets[this.mStartIndex + i2];
                if (constraintWidget != null) {
                    constraintWidget.resetAnchors();
                }
                i2++;
            }
            if (i != 0 && this.biggest != null) {
                boolean z = isLastChain && chainIndex == 0;
                int i3 = -1;
                int i4 = -1;
                for (int i5 = 0; i5 < i; i5++) {
                    int i6 = i5;
                    if (isInRtl) {
                        i6 = (i - 1) - i5;
                    }
                    if (this.mStartIndex + i6 >= Flow.this.mDisplayedWidgetsCount) {
                        break;
                    }
                    ConstraintWidget constraintWidget2 = Flow.this.mDisplayedWidgets[this.mStartIndex + i6];
                    if (constraintWidget2 != null && constraintWidget2.getVisibility() == 0) {
                        if (i3 == -1) {
                            i3 = i5;
                        }
                        i4 = i5;
                    }
                }
                ConstraintWidget constraintWidget3 = null;
                if (this.mOrientation == 0) {
                    ConstraintWidget constraintWidget4 = this.biggest;
                    constraintWidget4.setVerticalChainStyle(Flow.this.mVerticalStyle);
                    int i7 = this.mPaddingTop;
                    if (chainIndex > 0) {
                        i7 += Flow.this.mVerticalGap;
                    }
                    constraintWidget4.mTop.connect(this.mTop, i7);
                    if (isLastChain) {
                        constraintWidget4.mBottom.connect(this.mBottom, this.mPaddingBottom);
                    }
                    if (chainIndex > 0) {
                        this.mTop.mOwner.mBottom.connect(constraintWidget4.mTop, 0);
                    }
                    ConstraintWidget constraintWidget5 = constraintWidget4;
                    if (Flow.this.mVerticalAlign == 3 && !constraintWidget4.hasBaseline()) {
                        int i8 = 0;
                        while (true) {
                            if (i8 >= i) {
                                break;
                            }
                            int i9 = i8;
                            if (isInRtl) {
                                i9 = (i - 1) - i8;
                            }
                            if (this.mStartIndex + i9 >= Flow.this.mDisplayedWidgetsCount) {
                                break;
                            }
                            ConstraintWidget constraintWidget6 = Flow.this.mDisplayedWidgets[this.mStartIndex + i9];
                            if (constraintWidget6.hasBaseline()) {
                                constraintWidget5 = constraintWidget6;
                                break;
                            }
                            i8++;
                        }
                    }
                    int i10 = 0;
                    while (i10 < i) {
                        int i11 = i10;
                        if (isInRtl) {
                            i11 = (i - 1) - i10;
                        }
                        if (this.mStartIndex + i11 < Flow.this.mDisplayedWidgetsCount) {
                            ConstraintWidget constraintWidget7 = Flow.this.mDisplayedWidgets[this.mStartIndex + i11];
                            if (constraintWidget7 != null) {
                                if (i10 == 0) {
                                    constraintWidget7.connect(constraintWidget7.mLeft, this.mLeft, this.mPaddingLeft);
                                }
                                if (i11 == 0) {
                                    int access$800 = Flow.this.mHorizontalStyle;
                                    float access$900 = Flow.this.mHorizontalBias;
                                    if (isInRtl) {
                                        access$900 = 1.0f - access$900;
                                    }
                                    if (this.mStartIndex == 0 && Flow.this.mFirstHorizontalStyle != -1) {
                                        access$800 = Flow.this.mFirstHorizontalStyle;
                                        float access$1100 = Flow.this.mFirstHorizontalBias;
                                        if (isInRtl) {
                                            access$1100 = 1.0f - access$1100;
                                        }
                                        access$900 = access$1100;
                                    } else if (isLastChain && Flow.this.mLastHorizontalStyle != -1) {
                                        access$800 = Flow.this.mLastHorizontalStyle;
                                        float access$1300 = Flow.this.mLastHorizontalBias;
                                        if (isInRtl) {
                                            access$1300 = 1.0f - access$1300;
                                        }
                                        access$900 = access$1300;
                                    }
                                    constraintWidget7.setHorizontalChainStyle(access$800);
                                    constraintWidget7.setHorizontalBiasPercent(access$900);
                                }
                                if (i10 == i - 1) {
                                    constraintWidget7.connect(constraintWidget7.mRight, this.mRight, this.mPaddingRight);
                                }
                                if (constraintWidget3 != null) {
                                    constraintWidget7.mLeft.connect(constraintWidget3.mRight, Flow.this.mHorizontalGap);
                                    if (i10 == i3) {
                                        constraintWidget7.mLeft.setGoneMargin(this.mPaddingLeft);
                                    }
                                    constraintWidget3.mRight.connect(constraintWidget7.mLeft, 0);
                                    if (i10 == i4 + 1) {
                                        constraintWidget3.mRight.setGoneMargin(this.mPaddingRight);
                                    }
                                }
                                if (constraintWidget7 != constraintWidget4) {
                                    if (Flow.this.mVerticalAlign != 3 || !constraintWidget5.hasBaseline() || constraintWidget7 == constraintWidget5 || !constraintWidget7.hasBaseline()) {
                                        switch (Flow.this.mVerticalAlign) {
                                            case 0:
                                                constraintWidget7.mTop.connect(constraintWidget4.mTop, 0);
                                                break;
                                            case 1:
                                                constraintWidget7.mBottom.connect(constraintWidget4.mBottom, 0);
                                                break;
                                            default:
                                                if (!z) {
                                                    constraintWidget7.mTop.connect(constraintWidget4.mTop, 0);
                                                    constraintWidget7.mBottom.connect(constraintWidget4.mBottom, 0);
                                                    break;
                                                } else {
                                                    constraintWidget7.mTop.connect(this.mTop, this.mPaddingTop);
                                                    constraintWidget7.mBottom.connect(this.mBottom, this.mPaddingBottom);
                                                    break;
                                                }
                                        }
                                    } else {
                                        constraintWidget7.mBaseline.connect(constraintWidget5.mBaseline, 0);
                                    }
                                }
                                constraintWidget3 = constraintWidget7;
                            }
                            i10++;
                        } else {
                            return;
                        }
                    }
                    return;
                }
                ConstraintWidget constraintWidget8 = this.biggest;
                constraintWidget8.setHorizontalChainStyle(Flow.this.mHorizontalStyle);
                int i12 = this.mPaddingLeft;
                if (chainIndex > 0) {
                    i12 += Flow.this.mHorizontalGap;
                }
                if (isInRtl) {
                    constraintWidget8.mRight.connect(this.mRight, i12);
                    if (isLastChain) {
                        constraintWidget8.mLeft.connect(this.mLeft, this.mPaddingRight);
                    }
                    if (chainIndex > 0) {
                        this.mRight.mOwner.mLeft.connect(constraintWidget8.mRight, 0);
                    }
                } else {
                    constraintWidget8.mLeft.connect(this.mLeft, i12);
                    if (isLastChain) {
                        constraintWidget8.mRight.connect(this.mRight, this.mPaddingRight);
                    }
                    if (chainIndex > 0) {
                        this.mLeft.mOwner.mRight.connect(constraintWidget8.mLeft, 0);
                    }
                }
                int i13 = 0;
                while (i13 < i && this.mStartIndex + i13 < Flow.this.mDisplayedWidgetsCount) {
                    ConstraintWidget constraintWidget9 = Flow.this.mDisplayedWidgets[this.mStartIndex + i13];
                    if (constraintWidget9 != null) {
                        if (i13 == 0) {
                            constraintWidget9.connect(constraintWidget9.mTop, this.mTop, this.mPaddingTop);
                            int access$600 = Flow.this.mVerticalStyle;
                            float access$1400 = Flow.this.mVerticalBias;
                            if (this.mStartIndex == 0 && Flow.this.mFirstVerticalStyle != -1) {
                                access$600 = Flow.this.mFirstVerticalStyle;
                                access$1400 = Flow.this.mFirstVerticalBias;
                            } else if (isLastChain && Flow.this.mLastVerticalStyle != -1) {
                                access$600 = Flow.this.mLastVerticalStyle;
                                access$1400 = Flow.this.mLastVerticalBias;
                            }
                            constraintWidget9.setVerticalChainStyle(access$600);
                            constraintWidget9.setVerticalBiasPercent(access$1400);
                        }
                        if (i13 == i - 1) {
                            constraintWidget9.connect(constraintWidget9.mBottom, this.mBottom, this.mPaddingBottom);
                        }
                        if (constraintWidget3 != null) {
                            constraintWidget9.mTop.connect(constraintWidget3.mBottom, Flow.this.mVerticalGap);
                            if (i13 == i3) {
                                constraintWidget9.mTop.setGoneMargin(this.mPaddingTop);
                            }
                            constraintWidget3.mBottom.connect(constraintWidget9.mTop, 0);
                            if (i13 == i4 + 1) {
                                constraintWidget3.mBottom.setGoneMargin(this.mPaddingBottom);
                            }
                        }
                        if (constraintWidget9 != constraintWidget8) {
                            if (!isInRtl) {
                                switch (Flow.this.mHorizontalAlign) {
                                    case 0:
                                        constraintWidget9.mLeft.connect(constraintWidget8.mLeft, 0);
                                        break;
                                    case 1:
                                        constraintWidget9.mRight.connect(constraintWidget8.mRight, 0);
                                        break;
                                    case 2:
                                        if (!z) {
                                            constraintWidget9.mLeft.connect(constraintWidget8.mLeft, 0);
                                            constraintWidget9.mRight.connect(constraintWidget8.mRight, 0);
                                            break;
                                        } else {
                                            constraintWidget9.mLeft.connect(this.mLeft, this.mPaddingLeft);
                                            constraintWidget9.mRight.connect(this.mRight, this.mPaddingRight);
                                            break;
                                        }
                                }
                            } else {
                                switch (Flow.this.mHorizontalAlign) {
                                    case 0:
                                        constraintWidget9.mRight.connect(constraintWidget8.mRight, 0);
                                        break;
                                    case 1:
                                        constraintWidget9.mLeft.connect(constraintWidget8.mLeft, 0);
                                        break;
                                    case 2:
                                        constraintWidget9.mLeft.connect(constraintWidget8.mLeft, 0);
                                        constraintWidget9.mRight.connect(constraintWidget8.mRight, 0);
                                        break;
                                }
                            }
                        }
                        constraintWidget3 = constraintWidget9;
                    }
                    i13++;
                }
            }
        }

        public int getHeight() {
            return this.mOrientation == 1 ? this.mHeight - Flow.this.mVerticalGap : this.mHeight;
        }

        public int getWidth() {
            return this.mOrientation == 0 ? this.mWidth - Flow.this.mHorizontalGap : this.mWidth;
        }

        public void measureMatchConstraints(int availableSpace) {
            int i = this.mNbMatchConstraintsWidgets;
            if (i != 0) {
                int i2 = this.mCount;
                int i3 = availableSpace / i;
                int i4 = 0;
                while (i4 < i2 && this.mStartIndex + i4 < Flow.this.mDisplayedWidgetsCount) {
                    ConstraintWidget constraintWidget = Flow.this.mDisplayedWidgets[this.mStartIndex + i4];
                    if (this.mOrientation == 0) {
                        if (constraintWidget != null && constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultWidth == 0) {
                            Flow.this.measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, i3, constraintWidget.getVerticalDimensionBehaviour(), constraintWidget.getHeight());
                        }
                    } else if (constraintWidget != null && constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultHeight == 0) {
                        Flow.this.measure(constraintWidget, constraintWidget.getHorizontalDimensionBehaviour(), constraintWidget.getWidth(), ConstraintWidget.DimensionBehaviour.FIXED, i3);
                    }
                    i4++;
                }
                recomputeDimensions();
            }
        }

        public void setStartIndex(int value) {
            this.mStartIndex = value;
        }

        public void setup(int orientation, ConstraintAnchor left, ConstraintAnchor top, ConstraintAnchor right, ConstraintAnchor bottom, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom, int max) {
            this.mOrientation = orientation;
            this.mLeft = left;
            this.mTop = top;
            this.mRight = right;
            this.mBottom = bottom;
            this.mPaddingLeft = paddingLeft;
            this.mPaddingTop = paddingTop;
            this.mPaddingRight = paddingRight;
            this.mPaddingBottom = paddingBottom;
            this.mMax = max;
        }
    }

    private void createAlignedConstraints(boolean isInRtl) {
        ConstraintWidget constraintWidget;
        if (this.mAlignedDimensions != null && this.mAlignedBiggestElementsInCols != null && this.mAlignedBiggestElementsInRows != null) {
            for (int i = 0; i < this.mDisplayedWidgetsCount; i++) {
                this.mDisplayedWidgets[i].resetAnchors();
            }
            int[] iArr = this.mAlignedDimensions;
            int i2 = iArr[0];
            int i3 = iArr[1];
            ConstraintWidget constraintWidget2 = null;
            float f = this.mHorizontalBias;
            for (int i4 = 0; i4 < i2; i4++) {
                int i5 = i4;
                if (isInRtl) {
                    i5 = (i2 - i4) - 1;
                    f = 1.0f - this.mHorizontalBias;
                }
                ConstraintWidget constraintWidget3 = this.mAlignedBiggestElementsInCols[i5];
                if (!(constraintWidget3 == null || constraintWidget3.getVisibility() == 8)) {
                    if (i4 == 0) {
                        constraintWidget3.connect(constraintWidget3.mLeft, this.mLeft, getPaddingLeft());
                        constraintWidget3.setHorizontalChainStyle(this.mHorizontalStyle);
                        constraintWidget3.setHorizontalBiasPercent(f);
                    }
                    if (i4 == i2 - 1) {
                        constraintWidget3.connect(constraintWidget3.mRight, this.mRight, getPaddingRight());
                    }
                    if (i4 > 0 && constraintWidget2 != null) {
                        constraintWidget3.connect(constraintWidget3.mLeft, constraintWidget2.mRight, this.mHorizontalGap);
                        constraintWidget2.connect(constraintWidget2.mRight, constraintWidget3.mLeft, 0);
                    }
                    constraintWidget2 = constraintWidget3;
                }
            }
            for (int i6 = 0; i6 < i3; i6++) {
                ConstraintWidget constraintWidget4 = this.mAlignedBiggestElementsInRows[i6];
                if (!(constraintWidget4 == null || constraintWidget4.getVisibility() == 8)) {
                    if (i6 == 0) {
                        constraintWidget4.connect(constraintWidget4.mTop, this.mTop, getPaddingTop());
                        constraintWidget4.setVerticalChainStyle(this.mVerticalStyle);
                        constraintWidget4.setVerticalBiasPercent(this.mVerticalBias);
                    }
                    if (i6 == i3 - 1) {
                        constraintWidget4.connect(constraintWidget4.mBottom, this.mBottom, getPaddingBottom());
                    }
                    if (i6 > 0 && constraintWidget2 != null) {
                        constraintWidget4.connect(constraintWidget4.mTop, constraintWidget2.mBottom, this.mVerticalGap);
                        constraintWidget2.connect(constraintWidget2.mBottom, constraintWidget4.mTop, 0);
                    }
                    constraintWidget2 = constraintWidget4;
                }
            }
            for (int i7 = 0; i7 < i2; i7++) {
                for (int i8 = 0; i8 < i3; i8++) {
                    int i9 = (i8 * i2) + i7;
                    if (this.mOrientation == 1) {
                        i9 = (i7 * i3) + i8;
                    }
                    ConstraintWidget[] constraintWidgetArr = this.mDisplayedWidgets;
                    if (!(i9 >= constraintWidgetArr.length || (constraintWidget = constraintWidgetArr[i9]) == null || constraintWidget.getVisibility() == 8)) {
                        ConstraintWidget constraintWidget5 = this.mAlignedBiggestElementsInCols[i7];
                        ConstraintWidget constraintWidget6 = this.mAlignedBiggestElementsInRows[i8];
                        if (constraintWidget != constraintWidget5) {
                            constraintWidget.connect(constraintWidget.mLeft, constraintWidget5.mLeft, 0);
                            constraintWidget.connect(constraintWidget.mRight, constraintWidget5.mRight, 0);
                        }
                        if (constraintWidget != constraintWidget6) {
                            constraintWidget.connect(constraintWidget.mTop, constraintWidget6.mTop, 0);
                            constraintWidget.connect(constraintWidget.mBottom, constraintWidget6.mBottom, 0);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public final int getWidgetHeight(ConstraintWidget widget, int max) {
        if (widget == null) {
            return 0;
        }
        if (widget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            if (widget.mMatchConstraintDefaultHeight == 0) {
                return 0;
            }
            if (widget.mMatchConstraintDefaultHeight == 2) {
                int i = (int) (widget.mMatchConstraintPercentHeight * ((float) max));
                if (i != widget.getHeight()) {
                    widget.setMeasureRequested(true);
                    measure(widget, widget.getHorizontalDimensionBehaviour(), widget.getWidth(), ConstraintWidget.DimensionBehaviour.FIXED, i);
                }
                return i;
            } else if (widget.mMatchConstraintDefaultHeight == 1) {
                return widget.getHeight();
            } else {
                if (widget.mMatchConstraintDefaultHeight == 3) {
                    return (int) ((((float) widget.getWidth()) * widget.mDimensionRatio) + 0.5f);
                }
            }
        }
        return widget.getHeight();
    }

    /* access modifiers changed from: private */
    public final int getWidgetWidth(ConstraintWidget widget, int max) {
        if (widget == null) {
            return 0;
        }
        if (widget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            if (widget.mMatchConstraintDefaultWidth == 0) {
                return 0;
            }
            if (widget.mMatchConstraintDefaultWidth == 2) {
                int i = (int) (widget.mMatchConstraintPercentWidth * ((float) max));
                if (i != widget.getWidth()) {
                    widget.setMeasureRequested(true);
                    measure(widget, ConstraintWidget.DimensionBehaviour.FIXED, i, widget.getVerticalDimensionBehaviour(), widget.getHeight());
                }
                return i;
            } else if (widget.mMatchConstraintDefaultWidth == 1) {
                return widget.getWidth();
            } else {
                if (widget.mMatchConstraintDefaultWidth == 3) {
                    return (int) ((((float) widget.getHeight()) * widget.mDimensionRatio) + 0.5f);
                }
            }
        }
        return widget.getWidth();
    }

    private void measureAligned(ConstraintWidget[] widgets, int count, int orientation, int max, int[] measured) {
        ConstraintWidget constraintWidget;
        ConstraintWidget[] constraintWidgetArr = widgets;
        int i = count;
        int i2 = orientation;
        int i3 = max;
        boolean z = false;
        int i4 = 0;
        int i5 = 0;
        if (i2 == 0) {
            i5 = this.mMaxElementsWrap;
            if (i5 <= 0) {
                int i6 = 0;
                i5 = 0;
                for (int i7 = 0; i7 < i; i7++) {
                    if (i7 > 0) {
                        i6 += this.mHorizontalGap;
                    }
                    ConstraintWidget constraintWidget2 = constraintWidgetArr[i7];
                    if (constraintWidget2 != null) {
                        i6 += getWidgetWidth(constraintWidget2, i3);
                        if (i6 > i3) {
                            break;
                        }
                        i5++;
                    }
                }
            }
        } else {
            i4 = this.mMaxElementsWrap;
            if (i4 <= 0) {
                int i8 = 0;
                i4 = 0;
                for (int i9 = 0; i9 < i; i9++) {
                    if (i9 > 0) {
                        i8 += this.mVerticalGap;
                    }
                    ConstraintWidget constraintWidget3 = constraintWidgetArr[i9];
                    if (constraintWidget3 != null) {
                        i8 += getWidgetHeight(constraintWidget3, i3);
                        if (i8 > i3) {
                            break;
                        }
                        i4++;
                    }
                }
            }
        }
        if (this.mAlignedDimensions == null) {
            this.mAlignedDimensions = new int[2];
        }
        int i10 = 1;
        if ((i4 == 0 && i2 == 1) || (i5 == 0 && i2 == 0)) {
            z = true;
        }
        while (!z) {
            if (i2 == 0) {
                i4 = (int) Math.ceil((double) (((float) i) / ((float) i5)));
            } else {
                i5 = (int) Math.ceil((double) (((float) i) / ((float) i4)));
            }
            ConstraintWidget[] constraintWidgetArr2 = this.mAlignedBiggestElementsInCols;
            if (constraintWidgetArr2 == null || constraintWidgetArr2.length < i5) {
                this.mAlignedBiggestElementsInCols = new ConstraintWidget[i5];
            } else {
                Arrays.fill(constraintWidgetArr2, (Object) null);
            }
            ConstraintWidget[] constraintWidgetArr3 = this.mAlignedBiggestElementsInRows;
            if (constraintWidgetArr3 == null || constraintWidgetArr3.length < i4) {
                this.mAlignedBiggestElementsInRows = new ConstraintWidget[i4];
            } else {
                Arrays.fill(constraintWidgetArr3, (Object) null);
            }
            int i11 = 0;
            while (i11 < i5) {
                int i12 = 0;
                while (i12 < i4) {
                    int i13 = (i12 * i5) + i11;
                    if (i2 == i10) {
                        i13 = (i11 * i4) + i12;
                    }
                    if (i13 < constraintWidgetArr.length && (constraintWidget = constraintWidgetArr[i13]) != null) {
                        int widgetWidth = getWidgetWidth(constraintWidget, i3);
                        ConstraintWidget constraintWidget4 = this.mAlignedBiggestElementsInCols[i11];
                        if (constraintWidget4 == null || constraintWidget4.getWidth() < widgetWidth) {
                            this.mAlignedBiggestElementsInCols[i11] = constraintWidget;
                        }
                        int widgetHeight = getWidgetHeight(constraintWidget, i3);
                        ConstraintWidget constraintWidget5 = this.mAlignedBiggestElementsInRows[i12];
                        if (constraintWidget5 == null || constraintWidget5.getHeight() < widgetHeight) {
                            this.mAlignedBiggestElementsInRows[i12] = constraintWidget;
                        }
                    }
                    i12++;
                    i10 = 1;
                }
                i11++;
                i10 = 1;
            }
            int i14 = 0;
            for (int i15 = 0; i15 < i5; i15++) {
                ConstraintWidget constraintWidget6 = this.mAlignedBiggestElementsInCols[i15];
                if (constraintWidget6 != null) {
                    if (i15 > 0) {
                        i14 += this.mHorizontalGap;
                    }
                    i14 += getWidgetWidth(constraintWidget6, i3);
                }
            }
            int i16 = 0;
            for (int i17 = 0; i17 < i4; i17++) {
                ConstraintWidget constraintWidget7 = this.mAlignedBiggestElementsInRows[i17];
                if (constraintWidget7 != null) {
                    if (i17 > 0) {
                        i16 += this.mVerticalGap;
                    }
                    i16 += getWidgetHeight(constraintWidget7, i3);
                }
            }
            measured[0] = i14;
            measured[1] = i16;
            if (i2 == 0) {
                if (i14 <= i3) {
                    z = true;
                } else if (i5 > 1) {
                    i5--;
                } else {
                    z = true;
                }
            } else if (i16 <= i3) {
                z = true;
            } else if (i4 > 1) {
                i4--;
            } else {
                z = true;
            }
            i10 = 1;
        }
        int[] iArr = this.mAlignedDimensions;
        iArr[0] = i5;
        iArr[1] = i4;
    }

    private void measureChainWrap(ConstraintWidget[] widgets, int count, int orientation, int max, int[] measured) {
        WidgetsList widgetsList;
        int i;
        int i2;
        int i3;
        int i4 = count;
        int i5 = max;
        if (i4 != 0) {
            this.mChainList.clear();
            WidgetsList widgetsList2 = new WidgetsList(orientation, this.mLeft, this.mTop, this.mRight, this.mBottom, max);
            this.mChainList.add(widgetsList2);
            int i6 = 0;
            if (orientation == 0) {
                int i7 = 0;
                WidgetsList widgetsList3 = widgetsList2;
                int i8 = 0;
                while (i8 < i4) {
                    ConstraintWidget constraintWidget = widgets[i8];
                    int widgetWidth = getWidgetWidth(constraintWidget, i5);
                    int i9 = constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ? i6 + 1 : i6;
                    boolean z = (i7 == i5 || (this.mHorizontalGap + i7) + widgetWidth > i5) && widgetsList3.biggest != null;
                    if ((z || i8 <= 0 || (i3 = this.mMaxElementsWrap) <= 0 || i8 % i3 != 0) ? z : true) {
                        WidgetsList widgetsList4 = new WidgetsList(orientation, this.mLeft, this.mTop, this.mRight, this.mBottom, max);
                        widgetsList4.setStartIndex(i8);
                        this.mChainList.add(widgetsList4);
                        widgetsList3 = widgetsList4;
                        i7 = widgetWidth;
                    } else {
                        i7 = i8 > 0 ? i7 + this.mHorizontalGap + widgetWidth : widgetWidth;
                    }
                    widgetsList3.add(constraintWidget);
                    i8++;
                    i6 = i9;
                }
                widgetsList = widgetsList3;
            } else {
                int i10 = 0;
                WidgetsList widgetsList5 = widgetsList2;
                int i11 = 0;
                while (i11 < i4) {
                    ConstraintWidget constraintWidget2 = widgets[i11];
                    int widgetHeight = getWidgetHeight(constraintWidget2, i5);
                    int i12 = constraintWidget2.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ? i6 + 1 : i6;
                    boolean z2 = (i10 == i5 || (this.mVerticalGap + i10) + widgetHeight > i5) && widgetsList5.biggest != null;
                    if ((z2 || i11 <= 0 || (i2 = this.mMaxElementsWrap) <= 0 || i11 % i2 != 0) ? z2 : true) {
                        WidgetsList widgetsList6 = new WidgetsList(orientation, this.mLeft, this.mTop, this.mRight, this.mBottom, max);
                        widgetsList6.setStartIndex(i11);
                        this.mChainList.add(widgetsList6);
                        widgetsList5 = widgetsList6;
                        i10 = widgetHeight;
                    } else {
                        i10 = i11 > 0 ? i10 + this.mVerticalGap + widgetHeight : widgetHeight;
                    }
                    widgetsList5.add(constraintWidget2);
                    i11++;
                    i6 = i12;
                }
                widgetsList = widgetsList5;
            }
            int size = this.mChainList.size();
            ConstraintAnchor constraintAnchor = this.mLeft;
            ConstraintAnchor constraintAnchor2 = this.mTop;
            ConstraintAnchor constraintAnchor3 = this.mRight;
            ConstraintAnchor constraintAnchor4 = this.mBottom;
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int paddingRight = getPaddingRight();
            int paddingBottom = getPaddingBottom();
            WidgetsList widgetsList7 = widgetsList;
            ConstraintAnchor constraintAnchor5 = constraintAnchor;
            boolean z3 = getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            if (i6 <= 0 || !z3) {
                boolean z4 = z3;
                int i13 = i6;
            } else {
                int i14 = 0;
                while (i14 < size) {
                    boolean z5 = z3;
                    WidgetsList widgetsList8 = this.mChainList.get(i14);
                    if (orientation == 0) {
                        i = i6;
                        widgetsList8.measureMatchConstraints(i5 - widgetsList8.getWidth());
                    } else {
                        i = i6;
                        widgetsList8.measureMatchConstraints(i5 - widgetsList8.getHeight());
                    }
                    i14++;
                    z3 = z5;
                    i6 = i;
                }
                boolean z6 = z3;
                int i15 = i6;
            }
            int i16 = 0;
            int i17 = paddingTop;
            int i18 = paddingRight;
            int i19 = paddingBottom;
            ConstraintAnchor constraintAnchor6 = constraintAnchor5;
            int i20 = 0;
            int i21 = 0;
            while (i16 < size) {
                WidgetsList widgetsList9 = this.mChainList.get(i16);
                if (orientation == 0) {
                    if (i16 < size - 1) {
                        constraintAnchor4 = this.mChainList.get(i16 + 1).biggest.mTop;
                        i19 = 0;
                    } else {
                        constraintAnchor4 = this.mBottom;
                        i19 = getPaddingBottom();
                    }
                    ConstraintAnchor constraintAnchor7 = constraintAnchor2;
                    widgetsList9.setup(orientation, constraintAnchor6, constraintAnchor2, constraintAnchor3, constraintAnchor4, paddingLeft, i17, i18, i19, max);
                    ConstraintAnchor constraintAnchor8 = widgetsList9.biggest.mBottom;
                    i17 = 0;
                    int max2 = Math.max(i20, widgetsList9.getWidth());
                    i21 += widgetsList9.getHeight();
                    if (i16 > 0) {
                        i21 += this.mVerticalGap;
                    }
                    i20 = max2;
                    constraintAnchor2 = constraintAnchor8;
                } else {
                    ConstraintAnchor constraintAnchor9 = constraintAnchor2;
                    int i22 = i21;
                    int i23 = i20;
                    if (i16 < size - 1) {
                        constraintAnchor3 = this.mChainList.get(i16 + 1).biggest.mLeft;
                        i18 = 0;
                    } else {
                        constraintAnchor3 = this.mRight;
                        i18 = getPaddingRight();
                    }
                    widgetsList9.setup(orientation, constraintAnchor6, constraintAnchor9, constraintAnchor3, constraintAnchor4, paddingLeft, i17, i18, i19, max);
                    constraintAnchor6 = widgetsList9.biggest.mRight;
                    paddingLeft = 0;
                    i20 = i23 + widgetsList9.getWidth();
                    int max3 = Math.max(i22, widgetsList9.getHeight());
                    if (i16 > 0) {
                        i20 += this.mHorizontalGap;
                        i21 = max3;
                        constraintAnchor2 = constraintAnchor9;
                    } else {
                        i21 = max3;
                        constraintAnchor2 = constraintAnchor9;
                    }
                }
                i16++;
                int i24 = count;
                int i25 = max;
            }
            ConstraintAnchor constraintAnchor10 = constraintAnchor2;
            measured[0] = i20;
            measured[1] = i21;
        }
    }

    private void measureChainWrap_new(ConstraintWidget[] widgets, int count, int orientation, int max, int[] measured) {
        WidgetsList widgetsList;
        int i;
        int i2;
        int i3;
        int i4 = count;
        int i5 = max;
        if (i4 != 0) {
            this.mChainList.clear();
            WidgetsList widgetsList2 = new WidgetsList(orientation, this.mLeft, this.mTop, this.mRight, this.mBottom, max);
            this.mChainList.add(widgetsList2);
            int i6 = 0;
            if (orientation == 0) {
                int i7 = 0;
                int i8 = 0;
                WidgetsList widgetsList3 = widgetsList2;
                int i9 = 0;
                while (i9 < i4) {
                    int i10 = i8 + 1;
                    ConstraintWidget constraintWidget = widgets[i9];
                    int widgetWidth = getWidgetWidth(constraintWidget, i5);
                    int i11 = constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ? i6 + 1 : i6;
                    boolean z = (i7 == i5 || (this.mHorizontalGap + i7) + widgetWidth > i5) && widgetsList3.biggest != null;
                    if ((z || i9 <= 0 || (i3 = this.mMaxElementsWrap) <= 0 || i10 <= i3) ? z : true) {
                        WidgetsList widgetsList4 = new WidgetsList(orientation, this.mLeft, this.mTop, this.mRight, this.mBottom, max);
                        widgetsList4.setStartIndex(i9);
                        this.mChainList.add(widgetsList4);
                        widgetsList3 = widgetsList4;
                        i8 = i10;
                        i7 = widgetWidth;
                    } else if (i9 > 0) {
                        i7 += this.mHorizontalGap + widgetWidth;
                        i8 = 0;
                    } else {
                        i8 = 0;
                        i7 = widgetWidth;
                    }
                    widgetsList3.add(constraintWidget);
                    i9++;
                    i6 = i11;
                }
                widgetsList = widgetsList3;
            } else {
                int i12 = 0;
                WidgetsList widgetsList5 = widgetsList2;
                int i13 = 0;
                int i14 = 0;
                while (i14 < i4) {
                    ConstraintWidget constraintWidget2 = widgets[i14];
                    int widgetHeight = getWidgetHeight(constraintWidget2, i5);
                    int i15 = constraintWidget2.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ? i6 + 1 : i6;
                    boolean z2 = (i12 == i5 || (this.mVerticalGap + i12) + widgetHeight > i5) && widgetsList5.biggest != null;
                    if ((z2 || i14 <= 0 || (i2 = this.mMaxElementsWrap) <= 0 || i13 <= i2) ? z2 : true) {
                        WidgetsList widgetsList6 = new WidgetsList(orientation, this.mLeft, this.mTop, this.mRight, this.mBottom, max);
                        widgetsList6.setStartIndex(i14);
                        this.mChainList.add(widgetsList6);
                        widgetsList5 = widgetsList6;
                        i12 = widgetHeight;
                    } else if (i14 > 0) {
                        i12 += this.mVerticalGap + widgetHeight;
                        i13 = 0;
                    } else {
                        i13 = 0;
                        i12 = widgetHeight;
                    }
                    widgetsList5.add(constraintWidget2);
                    i14++;
                    i6 = i15;
                }
                widgetsList = widgetsList5;
            }
            int size = this.mChainList.size();
            ConstraintAnchor constraintAnchor = this.mLeft;
            ConstraintAnchor constraintAnchor2 = this.mTop;
            ConstraintAnchor constraintAnchor3 = this.mRight;
            ConstraintAnchor constraintAnchor4 = this.mBottom;
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int paddingRight = getPaddingRight();
            int paddingBottom = getPaddingBottom();
            WidgetsList widgetsList7 = widgetsList;
            ConstraintAnchor constraintAnchor5 = constraintAnchor;
            boolean z3 = getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            if (i6 <= 0 || !z3) {
                boolean z4 = z3;
                int i16 = i6;
            } else {
                int i17 = 0;
                while (i17 < size) {
                    boolean z5 = z3;
                    WidgetsList widgetsList8 = this.mChainList.get(i17);
                    if (orientation == 0) {
                        i = i6;
                        widgetsList8.measureMatchConstraints(i5 - widgetsList8.getWidth());
                    } else {
                        i = i6;
                        widgetsList8.measureMatchConstraints(i5 - widgetsList8.getHeight());
                    }
                    i17++;
                    z3 = z5;
                    i6 = i;
                }
                boolean z6 = z3;
                int i18 = i6;
            }
            int i19 = 0;
            int i20 = paddingTop;
            int i21 = paddingRight;
            int i22 = paddingBottom;
            ConstraintAnchor constraintAnchor6 = constraintAnchor5;
            int i23 = 0;
            int i24 = 0;
            while (i19 < size) {
                WidgetsList widgetsList9 = this.mChainList.get(i19);
                if (orientation == 0) {
                    if (i19 < size - 1) {
                        constraintAnchor4 = this.mChainList.get(i19 + 1).biggest.mTop;
                        i22 = 0;
                    } else {
                        constraintAnchor4 = this.mBottom;
                        i22 = getPaddingBottom();
                    }
                    ConstraintAnchor constraintAnchor7 = constraintAnchor2;
                    widgetsList9.setup(orientation, constraintAnchor6, constraintAnchor2, constraintAnchor3, constraintAnchor4, paddingLeft, i20, i21, i22, max);
                    ConstraintAnchor constraintAnchor8 = widgetsList9.biggest.mBottom;
                    i20 = 0;
                    int max2 = Math.max(i23, widgetsList9.getWidth());
                    i24 += widgetsList9.getHeight();
                    if (i19 > 0) {
                        i24 += this.mVerticalGap;
                    }
                    i23 = max2;
                    constraintAnchor2 = constraintAnchor8;
                } else {
                    ConstraintAnchor constraintAnchor9 = constraintAnchor2;
                    int i25 = i24;
                    int i26 = i23;
                    if (i19 < size - 1) {
                        constraintAnchor3 = this.mChainList.get(i19 + 1).biggest.mLeft;
                        i21 = 0;
                    } else {
                        constraintAnchor3 = this.mRight;
                        i21 = getPaddingRight();
                    }
                    widgetsList9.setup(orientation, constraintAnchor6, constraintAnchor9, constraintAnchor3, constraintAnchor4, paddingLeft, i20, i21, i22, max);
                    constraintAnchor6 = widgetsList9.biggest.mRight;
                    paddingLeft = 0;
                    i23 = i26 + widgetsList9.getWidth();
                    int max3 = Math.max(i25, widgetsList9.getHeight());
                    if (i19 > 0) {
                        i23 += this.mHorizontalGap;
                        i24 = max3;
                        constraintAnchor2 = constraintAnchor9;
                    } else {
                        i24 = max3;
                        constraintAnchor2 = constraintAnchor9;
                    }
                }
                i19++;
                int i27 = count;
                int i28 = max;
            }
            ConstraintAnchor constraintAnchor10 = constraintAnchor2;
            measured[0] = i23;
            measured[1] = i24;
        }
    }

    private void measureNoWrap(ConstraintWidget[] widgets, int count, int orientation, int max, int[] measured) {
        WidgetsList widgetsList;
        int i = count;
        if (i != 0) {
            if (this.mChainList.size() == 0) {
                widgetsList = new WidgetsList(orientation, this.mLeft, this.mTop, this.mRight, this.mBottom, max);
                this.mChainList.add(widgetsList);
            } else {
                widgetsList = this.mChainList.get(0);
                widgetsList.clear();
                ConstraintAnchor constraintAnchor = this.mLeft;
                ConstraintAnchor constraintAnchor2 = this.mTop;
                ConstraintAnchor constraintAnchor3 = this.mRight;
                widgetsList.setup(orientation, constraintAnchor, constraintAnchor2, constraintAnchor3, this.mBottom, getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom(), max);
            }
            for (int i2 = 0; i2 < i; i2++) {
                widgetsList.add(widgets[i2]);
            }
            measured[0] = widgetsList.getWidth();
            measured[1] = widgetsList.getHeight();
        }
    }

    public void addToSolver(LinearSystem system, boolean optimize) {
        super.addToSolver(system, optimize);
        boolean z = getParent() != null && ((ConstraintWidgetContainer) getParent()).isRtl();
        switch (this.mWrapMode) {
            case 0:
                if (this.mChainList.size() > 0) {
                    this.mChainList.get(0).createConstraints(z, 0, true);
                    break;
                }
                break;
            case 1:
                int size = this.mChainList.size();
                int i = 0;
                while (i < size) {
                    this.mChainList.get(i).createConstraints(z, i, i == size + -1);
                    i++;
                }
                break;
            case 2:
                createAlignedConstraints(z);
                break;
            case 3:
                int size2 = this.mChainList.size();
                int i2 = 0;
                while (i2 < size2) {
                    this.mChainList.get(i2).createConstraints(z, i2, i2 == size2 + -1);
                    i2++;
                }
                break;
        }
        needsCallbackFromSolver(false);
    }

    public void copy(ConstraintWidget src, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        super.copy(src, hashMap);
        Flow flow = (Flow) src;
        this.mHorizontalStyle = flow.mHorizontalStyle;
        this.mVerticalStyle = flow.mVerticalStyle;
        this.mFirstHorizontalStyle = flow.mFirstHorizontalStyle;
        this.mFirstVerticalStyle = flow.mFirstVerticalStyle;
        this.mLastHorizontalStyle = flow.mLastHorizontalStyle;
        this.mLastVerticalStyle = flow.mLastVerticalStyle;
        this.mHorizontalBias = flow.mHorizontalBias;
        this.mVerticalBias = flow.mVerticalBias;
        this.mFirstHorizontalBias = flow.mFirstHorizontalBias;
        this.mFirstVerticalBias = flow.mFirstVerticalBias;
        this.mLastHorizontalBias = flow.mLastHorizontalBias;
        this.mLastVerticalBias = flow.mLastVerticalBias;
        this.mHorizontalGap = flow.mHorizontalGap;
        this.mVerticalGap = flow.mVerticalGap;
        this.mHorizontalAlign = flow.mHorizontalAlign;
        this.mVerticalAlign = flow.mVerticalAlign;
        this.mWrapMode = flow.mWrapMode;
        this.mMaxElementsWrap = flow.mMaxElementsWrap;
        this.mOrientation = flow.mOrientation;
    }

    public float getMaxElementsWrap() {
        return (float) this.mMaxElementsWrap;
    }

    /* JADX WARNING: type inference failed for: r20v0 */
    /* JADX WARNING: type inference failed for: r20v1 */
    /* JADX WARNING: type inference failed for: r20v2 */
    /* JADX WARNING: type inference failed for: r20v3 */
    /* JADX WARNING: type inference failed for: r20v4 */
    /* JADX WARNING: type inference failed for: r20v5 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void measure(int r24, int r25, int r26, int r27) {
        /*
            r23 = this;
            r6 = r23
            r7 = r24
            r8 = r25
            r9 = r26
            r10 = r27
            int r0 = r6.mWidgetsCount
            r11 = 0
            if (r0 <= 0) goto L_0x001c
            boolean r0 = r23.measureChildren()
            if (r0 != 0) goto L_0x001c
            r6.setMeasure(r11, r11)
            r6.needsCallbackFromSolver(r11)
            return
        L_0x001c:
            r12 = 0
            r13 = 0
            int r14 = r23.getPaddingLeft()
            int r15 = r23.getPaddingRight()
            int r16 = r23.getPaddingTop()
            int r17 = r23.getPaddingBottom()
            r0 = 2
            int[] r5 = new int[r0]
            int r0 = r8 - r14
            int r0 = r0 - r15
            int r1 = r6.mOrientation
            r4 = 1
            if (r1 != r4) goto L_0x0040
            int r2 = r10 - r16
            int r0 = r2 - r17
            r18 = r0
            goto L_0x0042
        L_0x0040:
            r18 = r0
        L_0x0042:
            r0 = -1
            if (r1 != 0) goto L_0x0052
            int r1 = r6.mHorizontalStyle
            if (r1 != r0) goto L_0x004b
            r6.mHorizontalStyle = r11
        L_0x004b:
            int r1 = r6.mVerticalStyle
            if (r1 != r0) goto L_0x005e
            r6.mVerticalStyle = r11
            goto L_0x005e
        L_0x0052:
            int r1 = r6.mHorizontalStyle
            if (r1 != r0) goto L_0x0058
            r6.mHorizontalStyle = r11
        L_0x0058:
            int r1 = r6.mVerticalStyle
            if (r1 != r0) goto L_0x005e
            r6.mVerticalStyle = r11
        L_0x005e:
            androidx.constraintlayout.core.widgets.ConstraintWidget[] r0 = r6.mWidgets
            r1 = 0
            r2 = 0
            r19 = r1
        L_0x0064:
            int r1 = r6.mWidgetsCount
            r3 = 8
            if (r2 >= r1) goto L_0x007a
            androidx.constraintlayout.core.widgets.ConstraintWidget[] r1 = r6.mWidgets
            r1 = r1[r2]
            int r4 = r1.getVisibility()
            if (r4 != r3) goto L_0x0076
            int r19 = r19 + 1
        L_0x0076:
            int r2 = r2 + 1
            r4 = 1
            goto L_0x0064
        L_0x007a:
            int r1 = r6.mWidgetsCount
            if (r19 <= 0) goto L_0x00a6
            int r2 = r6.mWidgetsCount
            int r2 = r2 - r19
            androidx.constraintlayout.core.widgets.ConstraintWidget[] r0 = new androidx.constraintlayout.core.widgets.ConstraintWidget[r2]
            r2 = 0
            r4 = 0
        L_0x0086:
            int r11 = r6.mWidgetsCount
            if (r4 >= r11) goto L_0x00a0
            androidx.constraintlayout.core.widgets.ConstraintWidget[] r11 = r6.mWidgets
            r11 = r11[r4]
            r21 = r1
            int r1 = r11.getVisibility()
            if (r1 == r3) goto L_0x009a
            r0[r2] = r11
            int r2 = r2 + 1
        L_0x009a:
            int r4 = r4 + 1
            r1 = r21
            r11 = 0
            goto L_0x0086
        L_0x00a0:
            r21 = r1
            r1 = r2
            r11 = r0
            r4 = r1
            goto L_0x00ab
        L_0x00a6:
            r21 = r1
            r11 = r0
            r4 = r21
        L_0x00ab:
            r6.mDisplayedWidgets = r11
            r6.mDisplayedWidgetsCount = r4
            int r0 = r6.mWrapMode
            switch(r0) {
                case 0: goto L_0x00f3;
                case 1: goto L_0x00e0;
                case 2: goto L_0x00cd;
                case 3: goto L_0x00bb;
                default: goto L_0x00b4;
            }
        L_0x00b4:
            r21 = r4
            r22 = r5
            r20 = 1
            goto L_0x0106
        L_0x00bb:
            int r3 = r6.mOrientation
            r0 = r23
            r1 = r11
            r2 = r4
            r21 = r4
            r20 = 1
            r4 = r18
            r22 = r5
            r0.measureChainWrap_new(r1, r2, r3, r4, r5)
            goto L_0x0106
        L_0x00cd:
            r21 = r4
            r22 = r5
            r20 = 1
            int r3 = r6.mOrientation
            r0 = r23
            r1 = r11
            r2 = r21
            r4 = r18
            r0.measureAligned(r1, r2, r3, r4, r5)
            goto L_0x0106
        L_0x00e0:
            r21 = r4
            r22 = r5
            r20 = 1
            int r3 = r6.mOrientation
            r0 = r23
            r1 = r11
            r2 = r21
            r4 = r18
            r0.measureChainWrap(r1, r2, r3, r4, r5)
            goto L_0x0106
        L_0x00f3:
            r21 = r4
            r22 = r5
            r20 = 1
            int r3 = r6.mOrientation
            r0 = r23
            r1 = r11
            r2 = r21
            r4 = r18
            r0.measureNoWrap(r1, r2, r3, r4, r5)
        L_0x0106:
            r0 = 0
            r1 = r22[r0]
            int r1 = r1 + r14
            int r1 = r1 + r15
            r2 = r22[r20]
            int r2 = r2 + r16
            int r2 = r2 + r17
            r3 = 0
            r4 = 0
            r5 = -2147483648(0xffffffff80000000, float:-0.0)
            r12 = 1073741824(0x40000000, float:2.0)
            if (r7 != r12) goto L_0x011c
            r3 = r25
            goto L_0x0126
        L_0x011c:
            if (r7 != r5) goto L_0x0123
            int r3 = java.lang.Math.min(r1, r8)
            goto L_0x0126
        L_0x0123:
            if (r7 != 0) goto L_0x0126
            r3 = r1
        L_0x0126:
            if (r9 != r12) goto L_0x012b
            r4 = r27
            goto L_0x0135
        L_0x012b:
            if (r9 != r5) goto L_0x0132
            int r4 = java.lang.Math.min(r2, r10)
            goto L_0x0135
        L_0x0132:
            if (r9 != 0) goto L_0x0135
            r4 = r2
        L_0x0135:
            r6.setMeasure(r3, r4)
            r6.setWidth(r3)
            r6.setHeight(r4)
            int r5 = r6.mWidgetsCount
            if (r5 <= 0) goto L_0x0144
            r0 = r20
        L_0x0144:
            r6.needsCallbackFromSolver(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.widgets.Flow.measure(int, int, int, int):void");
    }

    public void setFirstHorizontalBias(float value) {
        this.mFirstHorizontalBias = value;
    }

    public void setFirstHorizontalStyle(int value) {
        this.mFirstHorizontalStyle = value;
    }

    public void setFirstVerticalBias(float value) {
        this.mFirstVerticalBias = value;
    }

    public void setFirstVerticalStyle(int value) {
        this.mFirstVerticalStyle = value;
    }

    public void setHorizontalAlign(int value) {
        this.mHorizontalAlign = value;
    }

    public void setHorizontalBias(float value) {
        this.mHorizontalBias = value;
    }

    public void setHorizontalGap(int value) {
        this.mHorizontalGap = value;
    }

    public void setHorizontalStyle(int value) {
        this.mHorizontalStyle = value;
    }

    public void setLastHorizontalBias(float value) {
        this.mLastHorizontalBias = value;
    }

    public void setLastHorizontalStyle(int value) {
        this.mLastHorizontalStyle = value;
    }

    public void setLastVerticalBias(float value) {
        this.mLastVerticalBias = value;
    }

    public void setLastVerticalStyle(int value) {
        this.mLastVerticalStyle = value;
    }

    public void setMaxElementsWrap(int value) {
        this.mMaxElementsWrap = value;
    }

    public void setOrientation(int value) {
        this.mOrientation = value;
    }

    public void setVerticalAlign(int value) {
        this.mVerticalAlign = value;
    }

    public void setVerticalBias(float value) {
        this.mVerticalBias = value;
    }

    public void setVerticalGap(int value) {
        this.mVerticalGap = value;
    }

    public void setVerticalStyle(int value) {
        this.mVerticalStyle = value;
    }

    public void setWrapMode(int value) {
        this.mWrapMode = value;
    }
}
