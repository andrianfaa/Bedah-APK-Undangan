package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.LinearSystem;
import androidx.constraintlayout.core.widgets.Barrier;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.core.widgets.Guideline;
import androidx.constraintlayout.core.widgets.Helper;
import androidx.constraintlayout.core.widgets.Optimizer;
import androidx.constraintlayout.core.widgets.VirtualLayout;
import java.util.ArrayList;

public class BasicMeasure {
    public static final int AT_MOST = Integer.MIN_VALUE;
    private static final boolean DEBUG = false;
    public static final int EXACTLY = 1073741824;
    public static final int FIXED = -3;
    public static final int MATCH_PARENT = -1;
    private static final int MODE_SHIFT = 30;
    public static final int UNSPECIFIED = 0;
    public static final int WRAP_CONTENT = -2;
    private ConstraintWidgetContainer constraintWidgetContainer;
    private Measure mMeasure = new Measure();
    private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets = new ArrayList<>();

    public static class Measure {
        public static int SELF_DIMENSIONS = 0;
        public static int TRY_GIVEN_DIMENSIONS = 1;
        public static int USE_GIVEN_DIMENSIONS = 2;
        public ConstraintWidget.DimensionBehaviour horizontalBehavior;
        public int horizontalDimension;
        public int measureStrategy;
        public int measuredBaseline;
        public boolean measuredHasBaseline;
        public int measuredHeight;
        public boolean measuredNeedsSolverPass;
        public int measuredWidth;
        public ConstraintWidget.DimensionBehaviour verticalBehavior;
        public int verticalDimension;
    }

    public interface Measurer {
        void didMeasures();

        void measure(ConstraintWidget constraintWidget, Measure measure);
    }

    public BasicMeasure(ConstraintWidgetContainer constraintWidgetContainer2) {
        this.constraintWidgetContainer = constraintWidgetContainer2;
    }

    private boolean measure(Measurer measurer, ConstraintWidget widget, int measureStrategy) {
        this.mMeasure.horizontalBehavior = widget.getHorizontalDimensionBehaviour();
        this.mMeasure.verticalBehavior = widget.getVerticalDimensionBehaviour();
        this.mMeasure.horizontalDimension = widget.getWidth();
        this.mMeasure.verticalDimension = widget.getHeight();
        this.mMeasure.measuredNeedsSolverPass = false;
        this.mMeasure.measureStrategy = measureStrategy;
        boolean z = this.mMeasure.horizontalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        boolean z2 = this.mMeasure.verticalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        boolean z3 = z && widget.mDimensionRatio > 0.0f;
        boolean z4 = z2 && widget.mDimensionRatio > 0.0f;
        if (z3 && widget.mResolvedMatchConstraintDefault[0] == 4) {
            this.mMeasure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        if (z4 && widget.mResolvedMatchConstraintDefault[1] == 4) {
            this.mMeasure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        measurer.measure(widget, this.mMeasure);
        widget.setWidth(this.mMeasure.measuredWidth);
        widget.setHeight(this.mMeasure.measuredHeight);
        widget.setHasBaseline(this.mMeasure.measuredHasBaseline);
        widget.setBaselineDistance(this.mMeasure.measuredBaseline);
        this.mMeasure.measureStrategy = Measure.SELF_DIMENSIONS;
        return this.mMeasure.measuredNeedsSolverPass;
    }

    private void measureChildren(ConstraintWidgetContainer layout) {
        int size = layout.mChildren.size();
        boolean optimizeFor = layout.optimizeFor(64);
        Measurer measurer = layout.getMeasurer();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = (ConstraintWidget) layout.mChildren.get(i);
            if (!(constraintWidget instanceof Guideline) && !(constraintWidget instanceof Barrier) && !constraintWidget.isInVirtualLayout() && (!optimizeFor || constraintWidget.horizontalRun == null || constraintWidget.verticalRun == null || !constraintWidget.horizontalRun.dimension.resolved || !constraintWidget.verticalRun.dimension.resolved)) {
                boolean z = false;
                ConstraintWidget.DimensionBehaviour dimensionBehaviour = constraintWidget.getDimensionBehaviour(0);
                ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = constraintWidget.getDimensionBehaviour(1);
                if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultWidth != 1 && dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultHeight != 1) {
                    z = true;
                }
                if (!z && layout.optimizeFor(1) && !(constraintWidget instanceof VirtualLayout)) {
                    if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultWidth == 0 && dimensionBehaviour2 != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && !constraintWidget.isInHorizontalChain()) {
                        z = true;
                    }
                    if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultHeight == 0 && dimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && !constraintWidget.isInHorizontalChain()) {
                        z = true;
                    }
                    if ((dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) && constraintWidget.mDimensionRatio > 0.0f) {
                        z = true;
                    }
                }
                if (!z) {
                    measure(measurer, constraintWidget, Measure.SELF_DIMENSIONS);
                    if (layout.mMetrics != null) {
                        layout.mMetrics.measuredWidgets++;
                    }
                }
            }
        }
        measurer.didMeasures();
    }

    private void solveLinearSystem(ConstraintWidgetContainer layout, String reason, int pass, int w, int h) {
        int minWidth = layout.getMinWidth();
        int minHeight = layout.getMinHeight();
        layout.setMinWidth(0);
        layout.setMinHeight(0);
        layout.setWidth(w);
        layout.setHeight(h);
        layout.setMinWidth(minWidth);
        layout.setMinHeight(minHeight);
        this.constraintWidgetContainer.setPass(pass);
        this.constraintWidgetContainer.layout();
    }

    public long solverMeasure(ConstraintWidgetContainer layout, int optimizationLevel, int paddingX, int paddingY, int widthMode, int widthSize, int heightMode, int heightSize, int lastMeasureWidth, int lastMeasureHeight) {
        boolean z;
        boolean z2;
        int i;
        boolean z3;
        int i2;
        boolean z4;
        boolean z5;
        int i3;
        long j;
        int widthSize2;
        Measurer measurer;
        int i4;
        long j2;
        int i5;
        int widthSize3;
        boolean z6;
        boolean z7;
        boolean z8;
        ConstraintWidgetContainer constraintWidgetContainer2 = layout;
        int i6 = optimizationLevel;
        int i7 = widthMode;
        int i8 = heightMode;
        Measurer measurer2 = layout.getMeasurer();
        long j3 = 0;
        int size = constraintWidgetContainer2.mChildren.size();
        int width = layout.getWidth();
        int height = layout.getHeight();
        boolean enabled = Optimizer.enabled(i6, 128);
        boolean z9 = enabled || Optimizer.enabled(i6, 64);
        if (z9) {
            int i9 = 0;
            while (i9 < size) {
                ConstraintWidget constraintWidget = (ConstraintWidget) constraintWidgetContainer2.mChildren.get(i9);
                boolean z10 = constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                boolean z11 = z9;
                boolean z12 = constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                boolean z13 = z10 && z12 && constraintWidget.getDimensionRatio() > 0.0f;
                if (!constraintWidget.isInHorizontalChain() || !z13) {
                    if (constraintWidget.isInVerticalChain() && z13) {
                        z = false;
                        break;
                    }
                    boolean z14 = z12;
                    if (constraintWidget instanceof VirtualLayout) {
                        z = false;
                        break;
                    } else if (constraintWidget.isInHorizontalChain() || constraintWidget.isInVerticalChain()) {
                        z = false;
                        break;
                    } else {
                        i9++;
                        z9 = z11;
                    }
                } else {
                    z = false;
                    break;
                }
            }
            z8 = z9;
        } else {
            z8 = z9;
        }
        z = z8;
        if (z && LinearSystem.sMetrics != null) {
            LinearSystem.sMetrics.measures++;
        }
        boolean z15 = z & ((i7 == 1073741824 && i8 == 1073741824) || enabled);
        int i10 = 0;
        if (z15) {
            int min = Math.min(layout.getMaxWidth(), widthSize);
            int min2 = Math.min(layout.getMaxHeight(), heightSize);
            if (i7 == 1073741824 && layout.getWidth() != min) {
                constraintWidgetContainer2.setWidth(min);
                layout.invalidateGraph();
            }
            if (i8 == 1073741824 && layout.getHeight() != min2) {
                constraintWidgetContainer2.setHeight(min2);
                layout.invalidateGraph();
            }
            if (i7 == 1073741824 && i8 == 1073741824) {
                z7 = constraintWidgetContainer2.directMeasure(enabled);
                i10 = 2;
                widthSize3 = min;
                z6 = true;
            } else {
                z7 = constraintWidgetContainer2.directMeasureSetup(enabled);
                if (i7 == 1073741824) {
                    widthSize3 = min;
                    z7 &= constraintWidgetContainer2.directMeasureWithOrientation(enabled, 0);
                    i10 = 0 + 1;
                } else {
                    widthSize3 = min;
                }
                if (i8 == 1073741824) {
                    z6 = true;
                    z7 &= constraintWidgetContainer2.directMeasureWithOrientation(enabled, 1);
                    i10++;
                } else {
                    z6 = true;
                }
            }
            if (z7) {
                if (i7 != 1073741824) {
                    z6 = false;
                }
                constraintWidgetContainer2.updateFromRuns(z6, i8 == 1073741824);
            }
            int i11 = widthSize3;
            i = i10;
            z2 = z7;
            int i12 = min2;
            z3 = true;
        } else {
            z3 = true;
            z2 = false;
            int i13 = heightSize;
            int i14 = widthSize;
            i = 0;
        }
        if (!z2 || i != 2) {
            int optimizationLevel2 = layout.getOptimizationLevel();
            if (size > 0) {
                measureChildren(layout);
            }
            updateHierarchy(layout);
            int size2 = this.mVariableDimensionsWidgets.size();
            if (size > 0) {
                i2 = size2;
                i3 = optimizationLevel2;
                z4 = z3;
                z5 = false;
                int i15 = i;
                boolean z16 = enabled;
                solveLinearSystem(layout, "First pass", 0, width, height);
            } else {
                i2 = size2;
                i3 = optimizationLevel2;
                z4 = z3;
                int i16 = i;
                boolean z17 = enabled;
                z5 = false;
            }
            int i17 = i2;
            if (i17 > 0) {
                int i18 = 0;
                boolean z18 = layout.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT ? z4 : z5;
                boolean z19 = layout.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT ? z4 : z5;
                int max = Math.max(layout.getWidth(), this.constraintWidgetContainer.getMinWidth());
                int max2 = Math.max(layout.getHeight(), this.constraintWidgetContainer.getMinHeight());
                int i19 = 0;
                while (i19 < i17) {
                    ConstraintWidget constraintWidget2 = this.mVariableDimensionsWidgets.get(i19);
                    if (!(constraintWidget2 instanceof VirtualLayout)) {
                        j2 = j3;
                        i5 = size;
                    } else {
                        int width2 = constraintWidget2.getWidth();
                        int height2 = constraintWidget2.getHeight();
                        i5 = size;
                        int measure = i18 | measure(measurer2, constraintWidget2, Measure.TRY_GIVEN_DIMENSIONS);
                        if (constraintWidgetContainer2.mMetrics != null) {
                            j2 = j3;
                            constraintWidgetContainer2.mMetrics.measuredMatchWidgets++;
                        } else {
                            j2 = j3;
                        }
                        int width3 = constraintWidget2.getWidth();
                        int height3 = constraintWidget2.getHeight();
                        if (width3 != width2) {
                            constraintWidget2.setWidth(width3);
                            if (!z18 || constraintWidget2.getRight() <= max) {
                                int widthSize4 = measure;
                            } else {
                                int widthSize5 = measure;
                                max = Math.max(max, constraintWidget2.getRight() + constraintWidget2.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin());
                            }
                            measure = true;
                        } else {
                            int widthSize6 = measure;
                        }
                        if (height3 != height2) {
                            constraintWidget2.setHeight(height3);
                            if (!z19 || constraintWidget2.getBottom() <= max2) {
                                int widthSize7 = measure;
                            } else {
                                int widthSize8 = measure;
                                max2 = Math.max(max2, constraintWidget2.getBottom() + constraintWidget2.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin());
                            }
                            measure = true;
                        } else {
                            int widthSize9 = measure;
                        }
                        i18 = measure | ((VirtualLayout) constraintWidget2).needSolverPass();
                    }
                    i19++;
                    int i20 = widthMode;
                    int i21 = heightMode;
                    size = i5;
                    j3 = j2;
                }
                j = j3;
                int i22 = size;
                int i23 = 0;
                while (true) {
                    if (i23 >= 2) {
                        Measurer measurer3 = measurer2;
                        int i24 = i17;
                        break;
                    }
                    int i25 = 0;
                    int i26 = i18;
                    int i27 = max;
                    int i28 = max2;
                    while (i25 < i17) {
                        ConstraintWidget constraintWidget3 = this.mVariableDimensionsWidgets.get(i25);
                        if ((!(constraintWidget3 instanceof Helper) || (constraintWidget3 instanceof VirtualLayout)) && !(constraintWidget3 instanceof Guideline) && constraintWidget3.getVisibility() != 8 && ((!z15 || !constraintWidget3.horizontalRun.dimension.resolved || !constraintWidget3.verticalRun.dimension.resolved) && !(constraintWidget3 instanceof VirtualLayout))) {
                            int width4 = constraintWidget3.getWidth();
                            int height4 = constraintWidget3.getHeight();
                            int baselineDistance = constraintWidget3.getBaselineDistance();
                            widthSize2 = i17;
                            int i29 = i23 == 2 + -1 ? Measure.USE_GIVEN_DIMENSIONS : Measure.TRY_GIVEN_DIMENSIONS;
                            int measure2 = i26 | measure(measurer2, constraintWidget3, i29);
                            int heightSize2 = i29;
                            if (constraintWidgetContainer2.mMetrics != null) {
                                measurer = measurer2;
                                i4 = measure2;
                                constraintWidgetContainer2.mMetrics.measuredMatchWidgets++;
                            } else {
                                measurer = measurer2;
                                i4 = measure2;
                            }
                            int width5 = constraintWidget3.getWidth();
                            int height5 = constraintWidget3.getHeight();
                            if (width5 != width4) {
                                constraintWidget3.setWidth(width5);
                                if (!z18 || constraintWidget3.getRight() <= i27) {
                                    int i30 = width4;
                                } else {
                                    int i31 = width4;
                                    i27 = Math.max(i27, constraintWidget3.getRight() + constraintWidget3.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin());
                                }
                                i26 = 1;
                            } else {
                                int i32 = width4;
                                i26 = i4;
                            }
                            if (height5 != height4) {
                                constraintWidget3.setHeight(height5);
                                if (!z19 || constraintWidget3.getBottom() <= i28) {
                                    int i33 = height4;
                                } else {
                                    int i34 = height4;
                                    i28 = Math.max(i28, constraintWidget3.getBottom() + constraintWidget3.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin());
                                }
                                i26 = 1;
                            } else {
                                int i35 = height4;
                            }
                            if (constraintWidget3.hasBaseline() && baselineDistance != constraintWidget3.getBaselineDistance()) {
                                i26 = 1;
                            }
                        } else {
                            widthSize2 = i17;
                            measurer = measurer2;
                        }
                        i25++;
                        i17 = widthSize2;
                        measurer2 = measurer;
                    }
                    int widthSize10 = i17;
                    Measurer measurer4 = measurer2;
                    if (i26 == 0) {
                        int i36 = widthSize10;
                        break;
                    }
                    solveLinearSystem(layout, "intermediate pass", i23 + 1, width, height);
                    i18 = 0;
                    i23++;
                    i17 = widthSize10;
                    max = i27;
                    max2 = i28;
                    measurer2 = measurer4;
                }
            } else {
                Measurer measurer5 = measurer2;
                j = 0;
                int i37 = size;
                int i38 = i17;
            }
            constraintWidgetContainer2.setOptimizationLevel(i3);
            return j;
        }
        int i39 = i;
        boolean z20 = enabled;
        Measurer measurer6 = measurer2;
        int i40 = size;
        return 0;
    }

    public void updateHierarchy(ConstraintWidgetContainer layout) {
        this.mVariableDimensionsWidgets.clear();
        int size = layout.mChildren.size();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = (ConstraintWidget) layout.mChildren.get(i);
            if (constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                this.mVariableDimensionsWidgets.add(constraintWidget);
            }
        }
        layout.invalidateGraph();
    }
}
