package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.LinearSystem;
import androidx.constraintlayout.core.widgets.Barrier;
import androidx.constraintlayout.core.widgets.ChainHead;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.core.widgets.Guideline;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import java.util.ArrayList;
import java.util.Iterator;

public class Direct {
    private static final boolean APPLY_MATCH_PARENT = false;
    private static final boolean DEBUG = false;
    private static final boolean EARLY_TERMINATION = true;
    private static int hcount = 0;
    private static BasicMeasure.Measure measure = new BasicMeasure.Measure();
    private static int vcount = 0;

    private static boolean canMeasure(int level, ConstraintWidget layout) {
        ConstraintWidget.DimensionBehaviour horizontalDimensionBehaviour = layout.getHorizontalDimensionBehaviour();
        ConstraintWidget.DimensionBehaviour verticalDimensionBehaviour = layout.getVerticalDimensionBehaviour();
        ConstraintWidgetContainer constraintWidgetContainer = layout.getParent() != null ? (ConstraintWidgetContainer) layout.getParent() : null;
        if (constraintWidgetContainer == null || constraintWidgetContainer.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.FIXED) {
        }
        if (constraintWidgetContainer == null || constraintWidgetContainer.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.FIXED) {
        }
        boolean z = horizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED || layout.isResolvedHorizontally() || horizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || (horizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && layout.mMatchConstraintDefaultWidth == 0 && layout.mDimensionRatio == 0.0f && layout.hasDanglingDimension(0)) || (horizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && layout.mMatchConstraintDefaultWidth == 1 && layout.hasResolvedTargets(0, layout.getWidth()));
        boolean z2 = verticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED || layout.isResolvedVertically() || verticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || (verticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && layout.mMatchConstraintDefaultHeight == 0 && layout.mDimensionRatio == 0.0f && layout.hasDanglingDimension(1)) || (verticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && layout.mMatchConstraintDefaultHeight == 1 && layout.hasResolvedTargets(1, layout.getHeight()));
        if (layout.mDimensionRatio > 0.0f && (z || z2)) {
            return EARLY_TERMINATION;
        }
        if (!z || !z2) {
            return false;
        }
        return EARLY_TERMINATION;
    }

    private static void horizontalSolvingPass(int level, ConstraintWidget layout, BasicMeasure.Measurer measurer, boolean isRtl) {
        ConstraintWidget constraintWidget = layout;
        BasicMeasure.Measurer measurer2 = measurer;
        boolean z = isRtl;
        if (!layout.isHorizontalSolvingPassDone()) {
            hcount++;
            if (!(constraintWidget instanceof ConstraintWidgetContainer) && layout.isMeasureRequested() && canMeasure(level + 1, constraintWidget)) {
                ConstraintWidgetContainer.measure(level + 1, constraintWidget, measurer2, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
            }
            ConstraintAnchor anchor = constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT);
            ConstraintAnchor anchor2 = constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT);
            int finalValue = anchor.getFinalValue();
            int finalValue2 = anchor2.getFinalValue();
            if (anchor.getDependents() != null && anchor.hasFinalValue()) {
                Iterator<ConstraintAnchor> it = anchor.getDependents().iterator();
                while (it.hasNext()) {
                    ConstraintAnchor next = it.next();
                    ConstraintWidget constraintWidget2 = next.mOwner;
                    boolean canMeasure = canMeasure(level + 1, constraintWidget2);
                    if (constraintWidget2.isMeasureRequested() && canMeasure) {
                        ConstraintWidgetContainer.measure(level + 1, constraintWidget2, measurer2, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
                    }
                    boolean z2 = ((next != constraintWidget2.mLeft || constraintWidget2.mRight.mTarget == null || !constraintWidget2.mRight.mTarget.hasFinalValue()) && (next != constraintWidget2.mRight || constraintWidget2.mLeft.mTarget == null || !constraintWidget2.mLeft.mTarget.hasFinalValue())) ? false : EARLY_TERMINATION;
                    if (constraintWidget2.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || canMeasure) {
                        if (!constraintWidget2.isMeasureRequested()) {
                            if (next == constraintWidget2.mLeft && constraintWidget2.mRight.mTarget == null) {
                                int margin = constraintWidget2.mLeft.getMargin() + finalValue;
                                constraintWidget2.setFinalHorizontal(margin, constraintWidget2.getWidth() + margin);
                                horizontalSolvingPass(level + 1, constraintWidget2, measurer2, z);
                            } else if (next == constraintWidget2.mRight && constraintWidget2.mLeft.mTarget == null) {
                                int margin2 = finalValue - constraintWidget2.mRight.getMargin();
                                constraintWidget2.setFinalHorizontal(margin2 - constraintWidget2.getWidth(), margin2);
                                horizontalSolvingPass(level + 1, constraintWidget2, measurer2, z);
                            } else if (z2 && !constraintWidget2.isInHorizontalChain()) {
                                solveHorizontalCenterConstraints(level + 1, measurer2, constraintWidget2, z);
                            }
                        }
                    } else if (constraintWidget2.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget2.mMatchConstraintMaxWidth >= 0 && constraintWidget2.mMatchConstraintMinWidth >= 0 && ((constraintWidget2.getVisibility() == 8 || (constraintWidget2.mMatchConstraintDefaultWidth == 0 && constraintWidget2.getDimensionRatio() == 0.0f)) && !constraintWidget2.isInHorizontalChain() && !constraintWidget2.isInVirtualLayout() && z2 && !constraintWidget2.isInHorizontalChain())) {
                        solveHorizontalMatchConstraint(level + 1, constraintWidget, measurer2, constraintWidget2, z);
                    }
                }
            }
            if (!(constraintWidget instanceof Guideline)) {
                if (anchor2.getDependents() != null && anchor2.hasFinalValue()) {
                    Iterator<ConstraintAnchor> it2 = anchor2.getDependents().iterator();
                    while (it2.hasNext()) {
                        ConstraintAnchor next2 = it2.next();
                        ConstraintWidget constraintWidget3 = next2.mOwner;
                        boolean canMeasure2 = canMeasure(level + 1, constraintWidget3);
                        if (constraintWidget3.isMeasureRequested() && canMeasure2) {
                            ConstraintWidgetContainer.measure(level + 1, constraintWidget3, measurer2, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
                        }
                        boolean z3 = ((next2 != constraintWidget3.mLeft || constraintWidget3.mRight.mTarget == null || !constraintWidget3.mRight.mTarget.hasFinalValue()) && (next2 != constraintWidget3.mRight || constraintWidget3.mLeft.mTarget == null || !constraintWidget3.mLeft.mTarget.hasFinalValue())) ? false : EARLY_TERMINATION;
                        if (constraintWidget3.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                            if (!canMeasure2) {
                                if (constraintWidget3.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget3.mMatchConstraintMaxWidth >= 0 && constraintWidget3.mMatchConstraintMinWidth >= 0) {
                                    if (constraintWidget3.getVisibility() != 8) {
                                        if (constraintWidget3.mMatchConstraintDefaultWidth == 0) {
                                            if (constraintWidget3.getDimensionRatio() != 0.0f) {
                                            }
                                        }
                                    }
                                    if (!constraintWidget3.isInHorizontalChain() && !constraintWidget3.isInVirtualLayout() && z3 && !constraintWidget3.isInHorizontalChain()) {
                                        solveHorizontalMatchConstraint(level + 1, constraintWidget, measurer2, constraintWidget3, z);
                                    }
                                }
                            }
                        }
                        if (!constraintWidget3.isMeasureRequested()) {
                            if (next2 == constraintWidget3.mLeft && constraintWidget3.mRight.mTarget == null) {
                                int margin3 = constraintWidget3.mLeft.getMargin() + finalValue2;
                                constraintWidget3.setFinalHorizontal(margin3, constraintWidget3.getWidth() + margin3);
                                horizontalSolvingPass(level + 1, constraintWidget3, measurer2, z);
                            } else if (next2 == constraintWidget3.mRight && constraintWidget3.mLeft.mTarget == null) {
                                int margin4 = finalValue2 - constraintWidget3.mRight.getMargin();
                                constraintWidget3.setFinalHorizontal(margin4 - constraintWidget3.getWidth(), margin4);
                                horizontalSolvingPass(level + 1, constraintWidget3, measurer2, z);
                            } else if (z3 && !constraintWidget3.isInHorizontalChain()) {
                                solveHorizontalCenterConstraints(level + 1, measurer2, constraintWidget3, z);
                            }
                        }
                    }
                }
                layout.markHorizontalSolvingPassDone();
            }
        }
    }

    public static String ls(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("  ");
        }
        sb.append("+-(" + level + ") ");
        return sb.toString();
    }

    private static void solveBarrier(int level, Barrier barrier, BasicMeasure.Measurer measurer, int orientation, boolean isRtl) {
        if (!barrier.allSolved()) {
            return;
        }
        if (orientation == 0) {
            horizontalSolvingPass(level + 1, barrier, measurer, isRtl);
        } else {
            verticalSolvingPass(level + 1, barrier, measurer);
        }
    }

    public static boolean solveChain(ConstraintWidgetContainer container, LinearSystem system, int orientation, int offset, ChainHead chainHead, boolean isChainSpread, boolean isChainSpreadInside, boolean isChainPacked) {
        ConstraintWidget constraintWidget;
        int i;
        ConstraintWidget constraintWidget2;
        boolean z;
        int i2;
        ConstraintAnchor constraintAnchor;
        boolean z2;
        BasicMeasure.Measure measure2;
        ConstraintWidget constraintWidget3;
        if (isChainPacked) {
            return false;
        }
        if (orientation == 0) {
            if (!container.isResolvedHorizontally()) {
                return false;
            }
        } else if (!container.isResolvedVertically()) {
            return false;
        }
        boolean isRtl = container.isRtl();
        ConstraintWidget first = chainHead.getFirst();
        ConstraintWidget last = chainHead.getLast();
        ConstraintWidget firstVisibleWidget = chainHead.getFirstVisibleWidget();
        ConstraintWidget lastVisibleWidget = chainHead.getLastVisibleWidget();
        ConstraintWidget head = chainHead.getHead();
        ConstraintWidget constraintWidget4 = first;
        boolean z3 = false;
        ConstraintAnchor constraintAnchor2 = first.mListAnchors[offset];
        ConstraintAnchor constraintAnchor3 = last.mListAnchors[offset + 1];
        if (constraintAnchor2.mTarget == null) {
            ConstraintWidget constraintWidget5 = first;
            ConstraintWidget constraintWidget6 = last;
            ConstraintWidget constraintWidget7 = head;
            ConstraintAnchor constraintAnchor4 = constraintAnchor2;
        } else if (constraintAnchor3.mTarget == null) {
            ConstraintWidget constraintWidget8 = first;
            ConstraintWidget constraintWidget9 = last;
            ConstraintWidget constraintWidget10 = head;
            ConstraintAnchor constraintAnchor5 = constraintAnchor2;
        } else {
            if (!constraintAnchor2.mTarget.hasFinalValue()) {
                ConstraintWidget constraintWidget11 = first;
                ConstraintWidget constraintWidget12 = last;
                ConstraintWidget constraintWidget13 = head;
                ConstraintAnchor constraintAnchor6 = constraintAnchor2;
            } else if (!constraintAnchor3.mTarget.hasFinalValue()) {
                ConstraintWidget constraintWidget14 = first;
                ConstraintWidget constraintWidget15 = last;
                ConstraintWidget constraintWidget16 = head;
                ConstraintAnchor constraintAnchor7 = constraintAnchor2;
            } else {
                if (firstVisibleWidget == null) {
                    ConstraintWidget constraintWidget17 = first;
                    ConstraintWidget constraintWidget18 = last;
                    ConstraintWidget constraintWidget19 = head;
                    ConstraintAnchor constraintAnchor8 = constraintAnchor2;
                } else if (lastVisibleWidget == null) {
                    ConstraintWidget constraintWidget20 = first;
                    ConstraintWidget constraintWidget21 = last;
                    ConstraintWidget constraintWidget22 = head;
                    ConstraintAnchor constraintAnchor9 = constraintAnchor2;
                } else {
                    int finalValue = constraintAnchor2.mTarget.getFinalValue() + firstVisibleWidget.mListAnchors[offset].getMargin();
                    int finalValue2 = constraintAnchor3.mTarget.getFinalValue() - lastVisibleWidget.mListAnchors[offset + 1].getMargin();
                    int i3 = finalValue2 - finalValue;
                    if (i3 <= 0) {
                        return false;
                    }
                    int i4 = 0;
                    BasicMeasure.Measure measure3 = new BasicMeasure.Measure();
                    int i5 = 0;
                    int i6 = 0;
                    while (!z3) {
                        boolean canMeasure = canMeasure(0 + 1, constraintWidget4);
                        if (!canMeasure) {
                            return false;
                        }
                        boolean z4 = canMeasure;
                        ConstraintWidget constraintWidget23 = last;
                        if (constraintWidget4.mListDimensionBehaviors[orientation] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                            return false;
                        }
                        if (constraintWidget4.isMeasureRequested()) {
                            z2 = z3;
                            constraintAnchor = constraintAnchor2;
                            measure2 = measure3;
                            ConstraintWidgetContainer.measure(0 + 1, constraintWidget4, container.getMeasurer(), measure2, BasicMeasure.Measure.SELF_DIMENSIONS);
                        } else {
                            z2 = z3;
                            constraintAnchor = constraintAnchor2;
                            measure2 = measure3;
                        }
                        int margin = i4 + constraintWidget4.mListAnchors[offset].getMargin();
                        i4 = (orientation == 0 ? margin + constraintWidget4.getWidth() : margin + constraintWidget4.getHeight()) + constraintWidget4.mListAnchors[offset + 1].getMargin();
                        i5++;
                        if (constraintWidget4.getVisibility() != 8) {
                            i6++;
                        } else {
                            int i7 = i6;
                        }
                        ConstraintAnchor constraintAnchor10 = constraintWidget4.mListAnchors[offset + 1].mTarget;
                        if (constraintAnchor10 != null) {
                            constraintWidget3 = constraintAnchor10.mOwner;
                            if (constraintWidget3.mListAnchors[offset].mTarget == null || constraintWidget3.mListAnchors[offset].mTarget.mOwner != constraintWidget4) {
                                constraintWidget3 = null;
                            }
                        } else {
                            constraintWidget3 = null;
                        }
                        if (constraintWidget3 != null) {
                            constraintWidget4 = constraintWidget3;
                            z3 = z2;
                        } else {
                            z3 = EARLY_TERMINATION;
                        }
                        measure3 = measure2;
                        last = constraintWidget23;
                        constraintAnchor2 = constraintAnchor;
                    }
                    ConstraintWidget constraintWidget24 = last;
                    boolean z5 = z3;
                    ConstraintAnchor constraintAnchor11 = constraintAnchor2;
                    BasicMeasure.Measure measure4 = measure3;
                    int i8 = i5;
                    int i9 = i6;
                    if (i9 == 0 || i9 != i8 || i3 < i4) {
                        return false;
                    }
                    int i10 = i3 - i4;
                    int i11 = i8;
                    if (isChainSpread) {
                        i10 /= i9 + 1;
                        constraintWidget = constraintWidget4;
                        i = 1;
                    } else if (!isChainSpreadInside) {
                        constraintWidget = constraintWidget4;
                        i = 1;
                    } else if (i9 > 2) {
                        constraintWidget = constraintWidget4;
                        i = 1;
                        i10 = (i10 / i9) - 1;
                    } else {
                        constraintWidget = constraintWidget4;
                        i = 1;
                    }
                    if (i9 == i) {
                        float horizontalBiasPercent = orientation == 0 ? head.getHorizontalBiasPercent() : head.getVerticalBiasPercent();
                        ConstraintWidget constraintWidget25 = head;
                        int i12 = (int) (((float) finalValue) + 0.5f + (((float) i10) * horizontalBiasPercent));
                        if (orientation == 0) {
                            firstVisibleWidget.setFinalHorizontal(i12, firstVisibleWidget.getWidth() + i12);
                        } else {
                            firstVisibleWidget.setFinalVertical(i12, firstVisibleWidget.getHeight() + i12);
                        }
                        float f = horizontalBiasPercent;
                        horizontalSolvingPass(0 + 1, firstVisibleWidget, container.getMeasurer(), isRtl);
                        return EARLY_TERMINATION;
                    }
                    ConstraintWidget constraintWidget26 = head;
                    if (isChainSpread) {
                        boolean z6 = false;
                        int i13 = finalValue + i10;
                        ConstraintWidget constraintWidget27 = first;
                        while (!z6) {
                            boolean z7 = z6;
                            ConstraintWidget constraintWidget28 = first;
                            if (constraintWidget27.getVisibility() != 8) {
                                int margin2 = i13 + constraintWidget27.mListAnchors[offset].getMargin();
                                if (orientation == 0) {
                                    constraintWidget27.setFinalHorizontal(margin2, constraintWidget27.getWidth() + margin2);
                                    horizontalSolvingPass(0 + 1, constraintWidget27, container.getMeasurer(), isRtl);
                                    i2 = margin2 + constraintWidget27.getWidth();
                                } else {
                                    constraintWidget27.setFinalVertical(margin2, constraintWidget27.getHeight() + margin2);
                                    verticalSolvingPass(0 + 1, constraintWidget27, container.getMeasurer());
                                    i2 = margin2 + constraintWidget27.getHeight();
                                }
                                i13 = i2 + constraintWidget27.mListAnchors[offset + 1].getMargin() + i10;
                            } else if (orientation == 0) {
                                constraintWidget27.setFinalHorizontal(i13, i13);
                                horizontalSolvingPass(0 + 1, constraintWidget27, container.getMeasurer(), isRtl);
                            } else {
                                constraintWidget27.setFinalVertical(i13, i13);
                                verticalSolvingPass(0 + 1, constraintWidget27, container.getMeasurer());
                            }
                            constraintWidget27.addToSolver(system, false);
                            ConstraintAnchor constraintAnchor12 = constraintWidget27.mListAnchors[offset + 1].mTarget;
                            if (constraintAnchor12 != null) {
                                constraintWidget2 = constraintAnchor12.mOwner;
                                ConstraintAnchor constraintAnchor13 = constraintAnchor12;
                                if (constraintWidget2.mListAnchors[offset].mTarget == null || constraintWidget2.mListAnchors[offset].mTarget.mOwner != constraintWidget27) {
                                    constraintWidget2 = null;
                                }
                            } else {
                                ConstraintAnchor constraintAnchor14 = constraintAnchor12;
                                constraintWidget2 = null;
                            }
                            if (constraintWidget2 != null) {
                                constraintWidget27 = constraintWidget2;
                                z = z7;
                            } else {
                                z = EARLY_TERMINATION;
                            }
                            z6 = z;
                            first = constraintWidget28;
                        }
                        boolean z8 = z6;
                        ConstraintWidget constraintWidget29 = first;
                        return EARLY_TERMINATION;
                    }
                    ConstraintWidget constraintWidget30 = first;
                    if (!isChainSpreadInside) {
                        ConstraintWidget constraintWidget31 = constraintWidget;
                        boolean z9 = z5;
                        return EARLY_TERMINATION;
                    } else if (i9 != 2) {
                        return false;
                    } else {
                        if (orientation == 0) {
                            firstVisibleWidget.setFinalHorizontal(finalValue, firstVisibleWidget.getWidth() + finalValue);
                            lastVisibleWidget.setFinalHorizontal(finalValue2 - lastVisibleWidget.getWidth(), finalValue2);
                            horizontalSolvingPass(0 + 1, firstVisibleWidget, container.getMeasurer(), isRtl);
                            horizontalSolvingPass(0 + 1, lastVisibleWidget, container.getMeasurer(), isRtl);
                            return EARLY_TERMINATION;
                        }
                        firstVisibleWidget.setFinalVertical(finalValue, firstVisibleWidget.getHeight() + finalValue);
                        lastVisibleWidget.setFinalVertical(finalValue2 - lastVisibleWidget.getHeight(), finalValue2);
                        verticalSolvingPass(0 + 1, firstVisibleWidget, container.getMeasurer());
                        verticalSolvingPass(0 + 1, lastVisibleWidget, container.getMeasurer());
                        return EARLY_TERMINATION;
                    }
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private static void solveHorizontalCenterConstraints(int level, BasicMeasure.Measurer measurer, ConstraintWidget widget, boolean isRtl) {
        float horizontalBiasPercent = widget.getHorizontalBiasPercent();
        int finalValue = widget.mLeft.mTarget.getFinalValue();
        int finalValue2 = widget.mRight.mTarget.getFinalValue();
        int margin = widget.mLeft.getMargin() + finalValue;
        int margin2 = finalValue2 - widget.mRight.getMargin();
        if (finalValue == finalValue2) {
            horizontalBiasPercent = 0.5f;
            margin = finalValue;
            margin2 = finalValue2;
        }
        int width = widget.getWidth();
        int i = (margin2 - margin) - width;
        if (margin > margin2) {
            i = (margin - margin2) - width;
        }
        int i2 = i > 0 ? (int) ((((float) i) * horizontalBiasPercent) + 0.5f) : (int) (((float) i) * horizontalBiasPercent);
        int i3 = margin + i2;
        int i4 = i3 + width;
        if (margin > margin2) {
            i3 = margin + i2;
            i4 = i3 - width;
        }
        widget.setFinalHorizontal(i3, i4);
        horizontalSolvingPass(level + 1, widget, measurer, isRtl);
    }

    private static void solveHorizontalMatchConstraint(int level, ConstraintWidget layout, BasicMeasure.Measurer measurer, ConstraintWidget widget, boolean isRtl) {
        float horizontalBiasPercent = widget.getHorizontalBiasPercent();
        int finalValue = widget.mLeft.mTarget.getFinalValue() + widget.mLeft.getMargin();
        int finalValue2 = widget.mRight.mTarget.getFinalValue() - widget.mRight.getMargin();
        if (finalValue2 >= finalValue) {
            int width = widget.getWidth();
            if (widget.getVisibility() != 8) {
                if (widget.mMatchConstraintDefaultWidth == 2) {
                    width = (int) (widget.getHorizontalBiasPercent() * 0.5f * ((float) (layout instanceof ConstraintWidgetContainer ? layout.getWidth() : layout.getParent().getWidth())));
                } else if (widget.mMatchConstraintDefaultWidth == 0) {
                    width = finalValue2 - finalValue;
                }
                width = Math.max(widget.mMatchConstraintMinWidth, width);
                if (widget.mMatchConstraintMaxWidth > 0) {
                    width = Math.min(widget.mMatchConstraintMaxWidth, width);
                }
            }
            int i = finalValue + ((int) ((((float) ((finalValue2 - finalValue) - width)) * horizontalBiasPercent) + 0.5f));
            widget.setFinalHorizontal(i, i + width);
            horizontalSolvingPass(level + 1, widget, measurer, isRtl);
        }
    }

    private static void solveVerticalCenterConstraints(int level, BasicMeasure.Measurer measurer, ConstraintWidget widget) {
        float verticalBiasPercent = widget.getVerticalBiasPercent();
        int finalValue = widget.mTop.mTarget.getFinalValue();
        int finalValue2 = widget.mBottom.mTarget.getFinalValue();
        int margin = widget.mTop.getMargin() + finalValue;
        int margin2 = finalValue2 - widget.mBottom.getMargin();
        if (finalValue == finalValue2) {
            verticalBiasPercent = 0.5f;
            margin = finalValue;
            margin2 = finalValue2;
        }
        int height = widget.getHeight();
        int i = (margin2 - margin) - height;
        if (margin > margin2) {
            i = (margin - margin2) - height;
        }
        int i2 = i > 0 ? (int) ((((float) i) * verticalBiasPercent) + 0.5f) : (int) (((float) i) * verticalBiasPercent);
        int i3 = margin + i2;
        int i4 = i3 + height;
        if (margin > margin2) {
            i3 = margin - i2;
            i4 = i3 - height;
        }
        widget.setFinalVertical(i3, i4);
        verticalSolvingPass(level + 1, widget, measurer);
    }

    private static void solveVerticalMatchConstraint(int level, ConstraintWidget layout, BasicMeasure.Measurer measurer, ConstraintWidget widget) {
        float verticalBiasPercent = widget.getVerticalBiasPercent();
        int finalValue = widget.mTop.mTarget.getFinalValue() + widget.mTop.getMargin();
        int finalValue2 = widget.mBottom.mTarget.getFinalValue() - widget.mBottom.getMargin();
        if (finalValue2 >= finalValue) {
            int height = widget.getHeight();
            if (widget.getVisibility() != 8) {
                if (widget.mMatchConstraintDefaultHeight == 2) {
                    height = (int) (verticalBiasPercent * 0.5f * ((float) (layout instanceof ConstraintWidgetContainer ? layout.getHeight() : layout.getParent().getHeight())));
                } else if (widget.mMatchConstraintDefaultHeight == 0) {
                    height = finalValue2 - finalValue;
                }
                height = Math.max(widget.mMatchConstraintMinHeight, height);
                if (widget.mMatchConstraintMaxHeight > 0) {
                    height = Math.min(widget.mMatchConstraintMaxHeight, height);
                }
            }
            int i = finalValue + ((int) ((((float) ((finalValue2 - finalValue) - height)) * verticalBiasPercent) + 0.5f));
            widget.setFinalVertical(i, i + height);
            verticalSolvingPass(level + 1, widget, measurer);
        }
    }

    public static void solvingPass(ConstraintWidgetContainer layout, BasicMeasure.Measurer measurer) {
        ConstraintWidgetContainer constraintWidgetContainer = layout;
        BasicMeasure.Measurer measurer2 = measurer;
        ConstraintWidget.DimensionBehaviour horizontalDimensionBehaviour = layout.getHorizontalDimensionBehaviour();
        ConstraintWidget.DimensionBehaviour verticalDimensionBehaviour = layout.getVerticalDimensionBehaviour();
        hcount = 0;
        vcount = 0;
        layout.resetFinalResolution();
        ArrayList<ConstraintWidget> children = layout.getChildren();
        int size = children.size();
        for (int i = 0; i < size; i++) {
            children.get(i).resetFinalResolution();
        }
        boolean isRtl = layout.isRtl();
        if (horizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED) {
            constraintWidgetContainer.setFinalHorizontal(0, layout.getWidth());
        } else {
            constraintWidgetContainer.setFinalLeft(0);
        }
        boolean z = false;
        boolean z2 = false;
        for (int i2 = 0; i2 < size; i2++) {
            ConstraintWidget constraintWidget = children.get(i2);
            if (constraintWidget instanceof Guideline) {
                Guideline guideline = (Guideline) constraintWidget;
                if (guideline.getOrientation() == 1) {
                    if (guideline.getRelativeBegin() != -1) {
                        guideline.setFinalValue(guideline.getRelativeBegin());
                    } else if (guideline.getRelativeEnd() != -1 && layout.isResolvedHorizontally()) {
                        guideline.setFinalValue(layout.getWidth() - guideline.getRelativeEnd());
                    } else if (layout.isResolvedHorizontally()) {
                        guideline.setFinalValue((int) ((guideline.getRelativePercent() * ((float) layout.getWidth())) + 0.5f));
                    }
                    z = EARLY_TERMINATION;
                }
            } else if ((constraintWidget instanceof Barrier) && ((Barrier) constraintWidget).getOrientation() == 0) {
                z2 = EARLY_TERMINATION;
            }
        }
        if (z) {
            for (int i3 = 0; i3 < size; i3++) {
                ConstraintWidget constraintWidget2 = children.get(i3);
                if (constraintWidget2 instanceof Guideline) {
                    Guideline guideline2 = (Guideline) constraintWidget2;
                    if (guideline2.getOrientation() == 1) {
                        horizontalSolvingPass(0, guideline2, measurer2, isRtl);
                    }
                }
            }
        }
        horizontalSolvingPass(0, constraintWidgetContainer, measurer2, isRtl);
        if (z2) {
            for (int i4 = 0; i4 < size; i4++) {
                ConstraintWidget constraintWidget3 = children.get(i4);
                if (constraintWidget3 instanceof Barrier) {
                    Barrier barrier = (Barrier) constraintWidget3;
                    if (barrier.getOrientation() == 0) {
                        solveBarrier(0, barrier, measurer2, 0, isRtl);
                    }
                }
            }
        }
        if (verticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED) {
            constraintWidgetContainer.setFinalVertical(0, layout.getHeight());
        } else {
            constraintWidgetContainer.setFinalTop(0);
        }
        boolean z3 = false;
        boolean z4 = false;
        for (int i5 = 0; i5 < size; i5++) {
            ConstraintWidget constraintWidget4 = children.get(i5);
            if (constraintWidget4 instanceof Guideline) {
                Guideline guideline3 = (Guideline) constraintWidget4;
                if (guideline3.getOrientation() == 0) {
                    if (guideline3.getRelativeBegin() != -1) {
                        guideline3.setFinalValue(guideline3.getRelativeBegin());
                    } else if (guideline3.getRelativeEnd() != -1 && layout.isResolvedVertically()) {
                        guideline3.setFinalValue(layout.getHeight() - guideline3.getRelativeEnd());
                    } else if (layout.isResolvedVertically()) {
                        guideline3.setFinalValue((int) ((guideline3.getRelativePercent() * ((float) layout.getHeight())) + 0.5f));
                    }
                    z3 = EARLY_TERMINATION;
                }
            } else if ((constraintWidget4 instanceof Barrier) && ((Barrier) constraintWidget4).getOrientation() == 1) {
                z4 = EARLY_TERMINATION;
            }
        }
        if (z3) {
            for (int i6 = 0; i6 < size; i6++) {
                ConstraintWidget constraintWidget5 = children.get(i6);
                if (constraintWidget5 instanceof Guideline) {
                    Guideline guideline4 = (Guideline) constraintWidget5;
                    if (guideline4.getOrientation() == 0) {
                        verticalSolvingPass(1, guideline4, measurer2);
                    }
                }
            }
        }
        verticalSolvingPass(0, constraintWidgetContainer, measurer2);
        if (z4) {
            for (int i7 = 0; i7 < size; i7++) {
                ConstraintWidget constraintWidget6 = children.get(i7);
                if (constraintWidget6 instanceof Barrier) {
                    Barrier barrier2 = (Barrier) constraintWidget6;
                    if (barrier2.getOrientation() == 1) {
                        solveBarrier(0, barrier2, measurer2, 1, isRtl);
                    }
                }
            }
        }
        for (int i8 = 0; i8 < size; i8++) {
            ConstraintWidget constraintWidget7 = children.get(i8);
            if (constraintWidget7.isMeasureRequested()) {
                if (canMeasure(0, constraintWidget7)) {
                    ConstraintWidgetContainer.measure(0, constraintWidget7, measurer2, measure, BasicMeasure.Measure.SELF_DIMENSIONS);
                    if (!(constraintWidget7 instanceof Guideline)) {
                        horizontalSolvingPass(0, constraintWidget7, measurer2, isRtl);
                        verticalSolvingPass(0, constraintWidget7, measurer2);
                    } else if (((Guideline) constraintWidget7).getOrientation() == 0) {
                        verticalSolvingPass(0, constraintWidget7, measurer2);
                    } else {
                        horizontalSolvingPass(0, constraintWidget7, measurer2, isRtl);
                    }
                }
            }
        }
    }

    private static void verticalSolvingPass(int level, ConstraintWidget layout, BasicMeasure.Measurer measurer) {
        ConstraintWidget constraintWidget = layout;
        BasicMeasure.Measurer measurer2 = measurer;
        if (!layout.isVerticalSolvingPassDone()) {
            vcount++;
            if (!(constraintWidget instanceof ConstraintWidgetContainer) && layout.isMeasureRequested() && canMeasure(level + 1, constraintWidget)) {
                ConstraintWidgetContainer.measure(level + 1, constraintWidget, measurer2, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
            }
            ConstraintAnchor anchor = constraintWidget.getAnchor(ConstraintAnchor.Type.TOP);
            ConstraintAnchor anchor2 = constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM);
            int finalValue = anchor.getFinalValue();
            int finalValue2 = anchor2.getFinalValue();
            if (anchor.getDependents() != null && anchor.hasFinalValue()) {
                Iterator<ConstraintAnchor> it = anchor.getDependents().iterator();
                while (it.hasNext()) {
                    ConstraintAnchor next = it.next();
                    ConstraintWidget constraintWidget2 = next.mOwner;
                    boolean canMeasure = canMeasure(level + 1, constraintWidget2);
                    if (constraintWidget2.isMeasureRequested() && canMeasure) {
                        ConstraintWidgetContainer.measure(level + 1, constraintWidget2, measurer2, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
                    }
                    boolean z = ((next != constraintWidget2.mTop || constraintWidget2.mBottom.mTarget == null || !constraintWidget2.mBottom.mTarget.hasFinalValue()) && (next != constraintWidget2.mBottom || constraintWidget2.mTop.mTarget == null || !constraintWidget2.mTop.mTarget.hasFinalValue())) ? false : EARLY_TERMINATION;
                    if (constraintWidget2.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || canMeasure) {
                        if (!constraintWidget2.isMeasureRequested()) {
                            if (next == constraintWidget2.mTop && constraintWidget2.mBottom.mTarget == null) {
                                int margin = constraintWidget2.mTop.getMargin() + finalValue;
                                constraintWidget2.setFinalVertical(margin, constraintWidget2.getHeight() + margin);
                                verticalSolvingPass(level + 1, constraintWidget2, measurer2);
                            } else if (next == constraintWidget2.mBottom && constraintWidget2.mTop.mTarget == null) {
                                int margin2 = finalValue - constraintWidget2.mBottom.getMargin();
                                constraintWidget2.setFinalVertical(margin2 - constraintWidget2.getHeight(), margin2);
                                verticalSolvingPass(level + 1, constraintWidget2, measurer2);
                            } else if (z && !constraintWidget2.isInVerticalChain()) {
                                solveVerticalCenterConstraints(level + 1, measurer2, constraintWidget2);
                            }
                        }
                    } else if (constraintWidget2.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget2.mMatchConstraintMaxHeight >= 0 && constraintWidget2.mMatchConstraintMinHeight >= 0 && ((constraintWidget2.getVisibility() == 8 || (constraintWidget2.mMatchConstraintDefaultHeight == 0 && constraintWidget2.getDimensionRatio() == 0.0f)) && !constraintWidget2.isInVerticalChain() && !constraintWidget2.isInVirtualLayout() && z && !constraintWidget2.isInVerticalChain())) {
                        solveVerticalMatchConstraint(level + 1, constraintWidget, measurer2, constraintWidget2);
                    }
                }
            }
            if (!(constraintWidget instanceof Guideline)) {
                if (anchor2.getDependents() != null && anchor2.hasFinalValue()) {
                    Iterator<ConstraintAnchor> it2 = anchor2.getDependents().iterator();
                    while (it2.hasNext()) {
                        ConstraintAnchor next2 = it2.next();
                        ConstraintWidget constraintWidget3 = next2.mOwner;
                        boolean canMeasure2 = canMeasure(level + 1, constraintWidget3);
                        if (constraintWidget3.isMeasureRequested() && canMeasure2) {
                            ConstraintWidgetContainer.measure(level + 1, constraintWidget3, measurer2, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
                        }
                        boolean z2 = ((next2 != constraintWidget3.mTop || constraintWidget3.mBottom.mTarget == null || !constraintWidget3.mBottom.mTarget.hasFinalValue()) && (next2 != constraintWidget3.mBottom || constraintWidget3.mTop.mTarget == null || !constraintWidget3.mTop.mTarget.hasFinalValue())) ? false : EARLY_TERMINATION;
                        if (constraintWidget3.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                            if (!canMeasure2) {
                                if (constraintWidget3.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget3.mMatchConstraintMaxHeight >= 0 && constraintWidget3.mMatchConstraintMinHeight >= 0) {
                                    if (constraintWidget3.getVisibility() != 8) {
                                        if (constraintWidget3.mMatchConstraintDefaultHeight == 0) {
                                            if (constraintWidget3.getDimensionRatio() != 0.0f) {
                                            }
                                        }
                                    }
                                    if (!constraintWidget3.isInVerticalChain() && !constraintWidget3.isInVirtualLayout() && z2 && !constraintWidget3.isInVerticalChain()) {
                                        solveVerticalMatchConstraint(level + 1, constraintWidget, measurer2, constraintWidget3);
                                    }
                                }
                            }
                        }
                        if (!constraintWidget3.isMeasureRequested()) {
                            if (next2 == constraintWidget3.mTop && constraintWidget3.mBottom.mTarget == null) {
                                int margin3 = constraintWidget3.mTop.getMargin() + finalValue2;
                                constraintWidget3.setFinalVertical(margin3, constraintWidget3.getHeight() + margin3);
                                verticalSolvingPass(level + 1, constraintWidget3, measurer2);
                            } else if (next2 == constraintWidget3.mBottom && constraintWidget3.mTop.mTarget == null) {
                                int margin4 = finalValue2 - constraintWidget3.mBottom.getMargin();
                                constraintWidget3.setFinalVertical(margin4 - constraintWidget3.getHeight(), margin4);
                                verticalSolvingPass(level + 1, constraintWidget3, measurer2);
                            } else if (z2 && !constraintWidget3.isInVerticalChain()) {
                                solveVerticalCenterConstraints(level + 1, measurer2, constraintWidget3);
                            }
                        }
                    }
                }
                ConstraintAnchor anchor3 = constraintWidget.getAnchor(ConstraintAnchor.Type.BASELINE);
                if (anchor3.getDependents() != null && anchor3.hasFinalValue()) {
                    int finalValue3 = anchor3.getFinalValue();
                    Iterator<ConstraintAnchor> it3 = anchor3.getDependents().iterator();
                    while (it3.hasNext()) {
                        ConstraintAnchor next3 = it3.next();
                        ConstraintWidget constraintWidget4 = next3.mOwner;
                        boolean canMeasure3 = canMeasure(level + 1, constraintWidget4);
                        if (constraintWidget4.isMeasureRequested() && canMeasure3) {
                            ConstraintWidgetContainer.measure(level + 1, constraintWidget4, measurer2, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
                        }
                        if ((constraintWidget4.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || canMeasure3) && !constraintWidget4.isMeasureRequested() && next3 == constraintWidget4.mBaseline) {
                            constraintWidget4.setFinalBaseline(next3.getMargin() + finalValue3);
                            verticalSolvingPass(level + 1, constraintWidget4, measurer2);
                        }
                    }
                }
                layout.markVerticalSolvingPassDone();
            }
        }
    }
}
