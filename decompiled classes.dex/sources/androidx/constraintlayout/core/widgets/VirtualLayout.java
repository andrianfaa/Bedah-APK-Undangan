package androidx.constraintlayout.core.widgets;

import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import java.util.HashSet;

public class VirtualLayout extends HelperWidget {
    protected BasicMeasure.Measure mMeasure = new BasicMeasure.Measure();
    private int mMeasuredHeight = 0;
    private int mMeasuredWidth = 0;
    BasicMeasure.Measurer mMeasurer = null;
    private boolean mNeedsCallFromSolver = false;
    private int mPaddingBottom = 0;
    private int mPaddingEnd = 0;
    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;
    private int mPaddingStart = 0;
    private int mPaddingTop = 0;
    private int mResolvedPaddingLeft = 0;
    private int mResolvedPaddingRight = 0;

    public void applyRtl(boolean isRtl) {
        int i = this.mPaddingStart;
        if (i <= 0 && this.mPaddingEnd <= 0) {
            return;
        }
        if (isRtl) {
            this.mResolvedPaddingLeft = this.mPaddingEnd;
            this.mResolvedPaddingRight = i;
            return;
        }
        this.mResolvedPaddingLeft = i;
        this.mResolvedPaddingRight = this.mPaddingEnd;
    }

    public void captureWidgets() {
        for (int i = 0; i < this.mWidgetsCount; i++) {
            ConstraintWidget constraintWidget = this.mWidgets[i];
            if (constraintWidget != null) {
                constraintWidget.setInVirtualLayout(true);
            }
        }
    }

    public boolean contains(HashSet<ConstraintWidget> hashSet) {
        for (int i = 0; i < this.mWidgetsCount; i++) {
            if (hashSet.contains(this.mWidgets[i])) {
                return true;
            }
        }
        return false;
    }

    public int getMeasuredHeight() {
        return this.mMeasuredHeight;
    }

    public int getMeasuredWidth() {
        return this.mMeasuredWidth;
    }

    public int getPaddingBottom() {
        return this.mPaddingBottom;
    }

    public int getPaddingLeft() {
        return this.mResolvedPaddingLeft;
    }

    public int getPaddingRight() {
        return this.mResolvedPaddingRight;
    }

    public int getPaddingTop() {
        return this.mPaddingTop;
    }

    public void measure(int widthMode, int widthSize, int heightMode, int heightSize) {
    }

    /* access modifiers changed from: protected */
    public void measure(ConstraintWidget widget, ConstraintWidget.DimensionBehaviour horizontalBehavior, int horizontalDimension, ConstraintWidget.DimensionBehaviour verticalBehavior, int verticalDimension) {
        while (this.mMeasurer == null && getParent() != null) {
            this.mMeasurer = ((ConstraintWidgetContainer) getParent()).getMeasurer();
        }
        this.mMeasure.horizontalBehavior = horizontalBehavior;
        this.mMeasure.verticalBehavior = verticalBehavior;
        this.mMeasure.horizontalDimension = horizontalDimension;
        this.mMeasure.verticalDimension = verticalDimension;
        this.mMeasurer.measure(widget, this.mMeasure);
        widget.setWidth(this.mMeasure.measuredWidth);
        widget.setHeight(this.mMeasure.measuredHeight);
        widget.setHasBaseline(this.mMeasure.measuredHasBaseline);
        widget.setBaselineDistance(this.mMeasure.measuredBaseline);
    }

    /* access modifiers changed from: protected */
    public boolean measureChildren() {
        BasicMeasure.Measurer measurer = null;
        if (this.mParent != null) {
            measurer = ((ConstraintWidgetContainer) this.mParent).getMeasurer();
        }
        if (measurer == null) {
            return false;
        }
        int i = 0;
        while (true) {
            boolean z = true;
            if (i >= this.mWidgetsCount) {
                return true;
            }
            ConstraintWidget constraintWidget = this.mWidgets[i];
            if (constraintWidget != null && !(constraintWidget instanceof Guideline)) {
                ConstraintWidget.DimensionBehaviour dimensionBehaviour = constraintWidget.getDimensionBehaviour(0);
                ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = constraintWidget.getDimensionBehaviour(1);
                if (dimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget.mMatchConstraintDefaultWidth == 1 || dimensionBehaviour2 != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget.mMatchConstraintDefaultHeight == 1) {
                    z = false;
                }
                if (!z) {
                    if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    }
                    if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    }
                    this.mMeasure.horizontalBehavior = dimensionBehaviour;
                    this.mMeasure.verticalBehavior = dimensionBehaviour2;
                    this.mMeasure.horizontalDimension = constraintWidget.getWidth();
                    this.mMeasure.verticalDimension = constraintWidget.getHeight();
                    measurer.measure(constraintWidget, this.mMeasure);
                    constraintWidget.setWidth(this.mMeasure.measuredWidth);
                    constraintWidget.setHeight(this.mMeasure.measuredHeight);
                    constraintWidget.setBaselineDistance(this.mMeasure.measuredBaseline);
                }
            }
            i++;
        }
    }

    public boolean needSolverPass() {
        return this.mNeedsCallFromSolver;
    }

    /* access modifiers changed from: protected */
    public void needsCallbackFromSolver(boolean value) {
        this.mNeedsCallFromSolver = value;
    }

    public void setMeasure(int width, int height) {
        this.mMeasuredWidth = width;
        this.mMeasuredHeight = height;
    }

    public void setPadding(int value) {
        this.mPaddingLeft = value;
        this.mPaddingTop = value;
        this.mPaddingRight = value;
        this.mPaddingBottom = value;
        this.mPaddingStart = value;
        this.mPaddingEnd = value;
    }

    public void setPaddingBottom(int value) {
        this.mPaddingBottom = value;
    }

    public void setPaddingEnd(int value) {
        this.mPaddingEnd = value;
    }

    public void setPaddingLeft(int value) {
        this.mPaddingLeft = value;
        this.mResolvedPaddingLeft = value;
    }

    public void setPaddingRight(int value) {
        this.mPaddingRight = value;
        this.mResolvedPaddingRight = value;
    }

    public void setPaddingStart(int value) {
        this.mPaddingStart = value;
        this.mResolvedPaddingLeft = value;
        this.mResolvedPaddingRight = value;
    }

    public void setPaddingTop(int value) {
        this.mPaddingTop = value;
    }

    public void updateConstraints(ConstraintWidgetContainer container) {
        captureWidgets();
    }
}
