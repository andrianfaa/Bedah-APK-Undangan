package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import java.util.ArrayList;
import java.util.Iterator;

public class ChainRun extends WidgetRun {
    private int chainStyle;
    ArrayList<WidgetRun> widgets = new ArrayList<>();

    public ChainRun(ConstraintWidget widget, int orientation) {
        super(widget);
        this.orientation = orientation;
        build();
    }

    private void build() {
        ConstraintWidget constraintWidget = this.widget;
        ConstraintWidget previousChainMember = constraintWidget.getPreviousChainMember(this.orientation);
        while (previousChainMember != null) {
            constraintWidget = previousChainMember;
            previousChainMember = constraintWidget.getPreviousChainMember(this.orientation);
        }
        this.widget = constraintWidget;
        this.widgets.add(constraintWidget.getRun(this.orientation));
        ConstraintWidget nextChainMember = constraintWidget.getNextChainMember(this.orientation);
        while (nextChainMember != null) {
            ConstraintWidget constraintWidget2 = nextChainMember;
            this.widgets.add(constraintWidget2.getRun(this.orientation));
            nextChainMember = constraintWidget2.getNextChainMember(this.orientation);
        }
        Iterator<WidgetRun> it = this.widgets.iterator();
        while (it.hasNext()) {
            WidgetRun next = it.next();
            if (this.orientation == 0) {
                next.widget.horizontalChainRun = this;
            } else if (this.orientation == 1) {
                next.widget.verticalChainRun = this;
            }
        }
        if ((this.orientation == 0 && ((ConstraintWidgetContainer) this.widget.getParent()).isRtl()) && this.widgets.size() > 1) {
            ArrayList<WidgetRun> arrayList = this.widgets;
            this.widget = arrayList.get(arrayList.size() - 1).widget;
        }
        this.chainStyle = this.orientation == 0 ? this.widget.getHorizontalChainStyle() : this.widget.getVerticalChainStyle();
    }

    private ConstraintWidget getFirstVisibleWidget() {
        for (int i = 0; i < this.widgets.size(); i++) {
            WidgetRun widgetRun = this.widgets.get(i);
            if (widgetRun.widget.getVisibility() != 8) {
                return widgetRun.widget;
            }
        }
        return null;
    }

    private ConstraintWidget getLastVisibleWidget() {
        for (int size = this.widgets.size() - 1; size >= 0; size--) {
            WidgetRun widgetRun = this.widgets.get(size);
            if (widgetRun.widget.getVisibility() != 8) {
                return widgetRun.widget;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void apply() {
        Iterator<WidgetRun> it = this.widgets.iterator();
        while (it.hasNext()) {
            it.next().apply();
        }
        int size = this.widgets.size();
        if (size >= 1) {
            ConstraintWidget constraintWidget = this.widgets.get(0).widget;
            ConstraintWidget constraintWidget2 = this.widgets.get(size - 1).widget;
            if (this.orientation == 0) {
                ConstraintAnchor constraintAnchor = constraintWidget.mLeft;
                ConstraintAnchor constraintAnchor2 = constraintWidget2.mRight;
                DependencyNode target = getTarget(constraintAnchor, 0);
                int margin = constraintAnchor.getMargin();
                ConstraintWidget firstVisibleWidget = getFirstVisibleWidget();
                if (firstVisibleWidget != null) {
                    margin = firstVisibleWidget.mLeft.getMargin();
                }
                if (target != null) {
                    addTarget(this.start, target, margin);
                }
                DependencyNode target2 = getTarget(constraintAnchor2, 0);
                int margin2 = constraintAnchor2.getMargin();
                ConstraintWidget lastVisibleWidget = getLastVisibleWidget();
                if (lastVisibleWidget != null) {
                    margin2 = lastVisibleWidget.mRight.getMargin();
                }
                if (target2 != null) {
                    addTarget(this.end, target2, -margin2);
                }
            } else {
                ConstraintAnchor constraintAnchor3 = constraintWidget.mTop;
                ConstraintAnchor constraintAnchor4 = constraintWidget2.mBottom;
                DependencyNode target3 = getTarget(constraintAnchor3, 1);
                int margin3 = constraintAnchor3.getMargin();
                ConstraintWidget firstVisibleWidget2 = getFirstVisibleWidget();
                if (firstVisibleWidget2 != null) {
                    margin3 = firstVisibleWidget2.mTop.getMargin();
                }
                if (target3 != null) {
                    addTarget(this.start, target3, margin3);
                }
                DependencyNode target4 = getTarget(constraintAnchor4, 1);
                int margin4 = constraintAnchor4.getMargin();
                ConstraintWidget lastVisibleWidget2 = getLastVisibleWidget();
                if (lastVisibleWidget2 != null) {
                    margin4 = lastVisibleWidget2.mBottom.getMargin();
                }
                if (target4 != null) {
                    addTarget(this.end, target4, -margin4);
                }
            }
            this.start.updateDelegate = this;
            this.end.updateDelegate = this;
        }
    }

    public void applyToWidget() {
        for (int i = 0; i < this.widgets.size(); i++) {
            this.widgets.get(i).applyToWidget();
        }
    }

    /* access modifiers changed from: package-private */
    public void clear() {
        this.runGroup = null;
        Iterator<WidgetRun> it = this.widgets.iterator();
        while (it.hasNext()) {
            it.next().clear();
        }
    }

    public long getWrapDimension() {
        int size = this.widgets.size();
        long j = 0;
        for (int i = 0; i < size; i++) {
            WidgetRun widgetRun = this.widgets.get(i);
            j = j + ((long) widgetRun.start.margin) + widgetRun.getWrapDimension() + ((long) widgetRun.end.margin);
        }
        return j;
    }

    /* access modifiers changed from: package-private */
    public void reset() {
        this.start.resolved = false;
        this.end.resolved = false;
    }

    /* access modifiers changed from: package-private */
    public boolean supportsWrapComputation() {
        int size = this.widgets.size();
        for (int i = 0; i < size; i++) {
            if (!this.widgets.get(i).supportsWrapComputation()) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ChainRun ");
        sb.append(this.orientation == 0 ? "horizontal : " : "vertical : ");
        Iterator<WidgetRun> it = this.widgets.iterator();
        while (it.hasNext()) {
            sb.append("<");
            sb.append(it.next());
            sb.append("> ");
        }
        return sb.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:56:0x00e2  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x00f5  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void update(androidx.constraintlayout.core.widgets.analyzer.Dependency r28) {
        /*
            r27 = this;
            r0 = r27
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r1 = r0.start
            boolean r1 = r1.resolved
            if (r1 == 0) goto L_0x0486
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r1 = r0.end
            boolean r1 = r1.resolved
            if (r1 != 0) goto L_0x0010
            goto L_0x0486
        L_0x0010:
            androidx.constraintlayout.core.widgets.ConstraintWidget r1 = r0.widget
            androidx.constraintlayout.core.widgets.ConstraintWidget r1 = r1.getParent()
            r2 = 0
            boolean r3 = r1 instanceof androidx.constraintlayout.core.widgets.ConstraintWidgetContainer
            if (r3 == 0) goto L_0x0022
            r3 = r1
            androidx.constraintlayout.core.widgets.ConstraintWidgetContainer r3 = (androidx.constraintlayout.core.widgets.ConstraintWidgetContainer) r3
            boolean r2 = r3.isRtl()
        L_0x0022:
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r3 = r0.end
            int r3 = r3.value
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r4 = r0.start
            int r4 = r4.value
            int r3 = r3 - r4
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            java.util.ArrayList<androidx.constraintlayout.core.widgets.analyzer.WidgetRun> r8 = r0.widgets
            int r8 = r8.size()
            r9 = -1
            r10 = 0
        L_0x0037:
            r11 = 8
            if (r10 >= r8) goto L_0x0050
            java.util.ArrayList<androidx.constraintlayout.core.widgets.analyzer.WidgetRun> r12 = r0.widgets
            java.lang.Object r12 = r12.get(r10)
            androidx.constraintlayout.core.widgets.analyzer.WidgetRun r12 = (androidx.constraintlayout.core.widgets.analyzer.WidgetRun) r12
            androidx.constraintlayout.core.widgets.ConstraintWidget r13 = r12.widget
            int r13 = r13.getVisibility()
            if (r13 != r11) goto L_0x004f
            int r10 = r10 + 1
            goto L_0x0037
        L_0x004f:
            r9 = r10
        L_0x0050:
            r10 = -1
            int r12 = r8 + -1
        L_0x0053:
            if (r12 < 0) goto L_0x006a
            java.util.ArrayList<androidx.constraintlayout.core.widgets.analyzer.WidgetRun> r13 = r0.widgets
            java.lang.Object r13 = r13.get(r12)
            androidx.constraintlayout.core.widgets.analyzer.WidgetRun r13 = (androidx.constraintlayout.core.widgets.analyzer.WidgetRun) r13
            androidx.constraintlayout.core.widgets.ConstraintWidget r14 = r13.widget
            int r14 = r14.getVisibility()
            if (r14 != r11) goto L_0x0069
            int r12 = r12 + -1
            goto L_0x0053
        L_0x0069:
            r10 = r12
        L_0x006a:
            r12 = 0
        L_0x006b:
            r15 = 2
            if (r12 >= r15) goto L_0x011f
            r17 = 0
            r15 = r17
        L_0x0072:
            if (r15 >= r8) goto L_0x010c
            java.util.ArrayList<androidx.constraintlayout.core.widgets.analyzer.WidgetRun> r13 = r0.widgets
            java.lang.Object r13 = r13.get(r15)
            androidx.constraintlayout.core.widgets.analyzer.WidgetRun r13 = (androidx.constraintlayout.core.widgets.analyzer.WidgetRun) r13
            androidx.constraintlayout.core.widgets.ConstraintWidget r14 = r13.widget
            int r14 = r14.getVisibility()
            if (r14 != r11) goto L_0x0088
            r19 = r1
            goto L_0x0104
        L_0x0088:
            int r7 = r7 + 1
            if (r15 <= 0) goto L_0x0093
            if (r15 < r9) goto L_0x0093
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r14 = r13.start
            int r14 = r14.margin
            int r4 = r4 + r14
        L_0x0093:
            androidx.constraintlayout.core.widgets.analyzer.DimensionDependency r14 = r13.dimension
            int r14 = r14.value
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r11 = r13.dimensionBehavior
            r19 = r1
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r11 == r1) goto L_0x00a1
            r1 = 1
            goto L_0x00a2
        L_0x00a1:
            r1 = 0
        L_0x00a2:
            if (r1 == 0) goto L_0x00c5
            int r11 = r0.orientation
            if (r11 != 0) goto L_0x00b3
            androidx.constraintlayout.core.widgets.ConstraintWidget r11 = r13.widget
            androidx.constraintlayout.core.widgets.analyzer.HorizontalWidgetRun r11 = r11.horizontalRun
            androidx.constraintlayout.core.widgets.analyzer.DimensionDependency r11 = r11.dimension
            boolean r11 = r11.resolved
            if (r11 != 0) goto L_0x00b3
            return
        L_0x00b3:
            int r11 = r0.orientation
            r20 = r1
            r1 = 1
            if (r11 != r1) goto L_0x00de
            androidx.constraintlayout.core.widgets.ConstraintWidget r1 = r13.widget
            androidx.constraintlayout.core.widgets.analyzer.VerticalWidgetRun r1 = r1.verticalRun
            androidx.constraintlayout.core.widgets.analyzer.DimensionDependency r1 = r1.dimension
            boolean r1 = r1.resolved
            if (r1 != 0) goto L_0x00de
            return
        L_0x00c5:
            r20 = r1
            int r1 = r13.matchConstraintsType
            r11 = 1
            if (r1 != r11) goto L_0x00d6
            if (r12 != 0) goto L_0x00d6
            r1 = 1
            androidx.constraintlayout.core.widgets.analyzer.DimensionDependency r11 = r13.dimension
            int r14 = r11.wrapValue
            int r5 = r5 + 1
            goto L_0x00e0
        L_0x00d6:
            androidx.constraintlayout.core.widgets.analyzer.DimensionDependency r1 = r13.dimension
            boolean r1 = r1.resolved
            if (r1 == 0) goto L_0x00de
            r1 = 1
            goto L_0x00e0
        L_0x00de:
            r1 = r20
        L_0x00e0:
            if (r1 != 0) goto L_0x00f5
            int r5 = r5 + 1
            androidx.constraintlayout.core.widgets.ConstraintWidget r11 = r13.widget
            float[] r11 = r11.mWeight
            r20 = r1
            int r1 = r0.orientation
            r1 = r11[r1]
            r11 = 0
            int r21 = (r1 > r11 ? 1 : (r1 == r11 ? 0 : -1))
            if (r21 < 0) goto L_0x00f4
            float r6 = r6 + r1
        L_0x00f4:
            goto L_0x00f8
        L_0x00f5:
            r20 = r1
            int r4 = r4 + r14
        L_0x00f8:
            int r1 = r8 + -1
            if (r15 >= r1) goto L_0x0104
            if (r15 >= r10) goto L_0x0104
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r1 = r13.end
            int r1 = r1.margin
            int r1 = -r1
            int r4 = r4 + r1
        L_0x0104:
            int r15 = r15 + 1
            r1 = r19
            r11 = 8
            goto L_0x0072
        L_0x010c:
            r19 = r1
            if (r4 < r3) goto L_0x0121
            if (r5 != 0) goto L_0x0113
            goto L_0x0121
        L_0x0113:
            r7 = 0
            r5 = 0
            r4 = 0
            r6 = 0
            int r12 = r12 + 1
            r1 = r19
            r11 = 8
            goto L_0x006b
        L_0x011f:
            r19 = r1
        L_0x0121:
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r1 = r0.start
            int r1 = r1.value
            if (r2 == 0) goto L_0x012b
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r11 = r0.end
            int r1 = r11.value
        L_0x012b:
            r11 = 1056964608(0x3f000000, float:0.5)
            if (r4 <= r3) goto L_0x0142
            r12 = 1073741824(0x40000000, float:2.0)
            if (r2 == 0) goto L_0x013b
            int r13 = r4 - r3
            float r13 = (float) r13
            float r13 = r13 / r12
            float r13 = r13 + r11
            int r12 = (int) r13
            int r1 = r1 + r12
            goto L_0x0142
        L_0x013b:
            int r13 = r4 - r3
            float r13 = (float) r13
            float r13 = r13 / r12
            float r13 = r13 + r11
            int r12 = (int) r13
            int r1 = r1 - r12
        L_0x0142:
            r12 = 0
            if (r5 <= 0) goto L_0x0252
            int r13 = r3 - r4
            float r13 = (float) r13
            float r14 = (float) r5
            float r13 = r13 / r14
            float r13 = r13 + r11
            int r12 = (int) r13
            r13 = 0
            r14 = 0
        L_0x014e:
            if (r14 >= r8) goto L_0x0200
            java.util.ArrayList<androidx.constraintlayout.core.widgets.analyzer.WidgetRun> r15 = r0.widgets
            java.lang.Object r15 = r15.get(r14)
            androidx.constraintlayout.core.widgets.analyzer.WidgetRun r15 = (androidx.constraintlayout.core.widgets.analyzer.WidgetRun) r15
            androidx.constraintlayout.core.widgets.ConstraintWidget r11 = r15.widget
            int r11 = r11.getVisibility()
            r21 = r1
            r1 = 8
            if (r11 != r1) goto L_0x016e
            r25 = r2
            r22 = r4
            r23 = r6
            r24 = r12
            goto L_0x01f0
        L_0x016e:
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r1 = r15.dimensionBehavior
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r11 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r1 != r11) goto L_0x01e8
            androidx.constraintlayout.core.widgets.analyzer.DimensionDependency r1 = r15.dimension
            boolean r1 = r1.resolved
            if (r1 != 0) goto L_0x01e8
            r1 = r12
            r11 = 0
            int r18 = (r6 > r11 ? 1 : (r6 == r11 ? 0 : -1))
            if (r18 <= 0) goto L_0x0196
            androidx.constraintlayout.core.widgets.ConstraintWidget r11 = r15.widget
            float[] r11 = r11.mWeight
            r22 = r1
            int r1 = r0.orientation
            r1 = r11[r1]
            int r11 = r3 - r4
            float r11 = (float) r11
            float r11 = r11 * r1
            float r11 = r11 / r6
            r20 = 1056964608(0x3f000000, float:0.5)
            float r11 = r11 + r20
            int r11 = (int) r11
            r1 = r11
            goto L_0x0198
        L_0x0196:
            r22 = r1
        L_0x0198:
            r11 = r1
            r22 = r4
            int r4 = r0.orientation
            if (r4 != 0) goto L_0x01b1
            androidx.constraintlayout.core.widgets.ConstraintWidget r4 = r15.widget
            int r4 = r4.mMatchConstraintMaxWidth
            r23 = r4
            androidx.constraintlayout.core.widgets.ConstraintWidget r4 = r15.widget
            int r4 = r4.mMatchConstraintMinWidth
            r26 = r6
            r6 = r4
            r4 = r23
            r23 = r26
            goto L_0x01c2
        L_0x01b1:
            androidx.constraintlayout.core.widgets.ConstraintWidget r4 = r15.widget
            int r4 = r4.mMatchConstraintMaxHeight
            r23 = r4
            androidx.constraintlayout.core.widgets.ConstraintWidget r4 = r15.widget
            int r4 = r4.mMatchConstraintMinHeight
            r26 = r6
            r6 = r4
            r4 = r23
            r23 = r26
        L_0x01c2:
            r24 = r12
            int r12 = r15.matchConstraintsType
            r25 = r2
            r2 = 1
            if (r12 != r2) goto L_0x01d3
            androidx.constraintlayout.core.widgets.analyzer.DimensionDependency r2 = r15.dimension
            int r2 = r2.wrapValue
            int r11 = java.lang.Math.min(r11, r2)
        L_0x01d3:
            int r2 = java.lang.Math.max(r6, r11)
            if (r4 <= 0) goto L_0x01dd
            int r2 = java.lang.Math.min(r4, r2)
        L_0x01dd:
            if (r2 == r1) goto L_0x01e2
            int r13 = r13 + 1
            r1 = r2
        L_0x01e2:
            androidx.constraintlayout.core.widgets.analyzer.DimensionDependency r11 = r15.dimension
            r11.resolve(r1)
            goto L_0x01f0
        L_0x01e8:
            r25 = r2
            r22 = r4
            r23 = r6
            r24 = r12
        L_0x01f0:
            int r14 = r14 + 1
            r1 = r21
            r4 = r22
            r6 = r23
            r12 = r24
            r2 = r25
            r11 = 1056964608(0x3f000000, float:0.5)
            goto L_0x014e
        L_0x0200:
            r21 = r1
            r25 = r2
            r22 = r4
            r23 = r6
            r24 = r12
            if (r13 <= 0) goto L_0x0243
            int r5 = r5 - r13
            r1 = 0
            r2 = 0
        L_0x020f:
            if (r2 >= r8) goto L_0x0241
            java.util.ArrayList<androidx.constraintlayout.core.widgets.analyzer.WidgetRun> r4 = r0.widgets
            java.lang.Object r4 = r4.get(r2)
            androidx.constraintlayout.core.widgets.analyzer.WidgetRun r4 = (androidx.constraintlayout.core.widgets.analyzer.WidgetRun) r4
            androidx.constraintlayout.core.widgets.ConstraintWidget r6 = r4.widget
            int r6 = r6.getVisibility()
            r11 = 8
            if (r6 != r11) goto L_0x0224
            goto L_0x023e
        L_0x0224:
            if (r2 <= 0) goto L_0x022d
            if (r2 < r9) goto L_0x022d
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r6 = r4.start
            int r6 = r6.margin
            int r1 = r1 + r6
        L_0x022d:
            androidx.constraintlayout.core.widgets.analyzer.DimensionDependency r6 = r4.dimension
            int r6 = r6.value
            int r1 = r1 + r6
            int r6 = r8 + -1
            if (r2 >= r6) goto L_0x023e
            if (r2 >= r10) goto L_0x023e
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r6 = r4.end
            int r6 = r6.margin
            int r6 = -r6
            int r1 = r1 + r6
        L_0x023e:
            int r2 = r2 + 1
            goto L_0x020f
        L_0x0241:
            r4 = r1
            goto L_0x0245
        L_0x0243:
            r4 = r22
        L_0x0245:
            int r1 = r0.chainStyle
            r2 = 2
            if (r1 != r2) goto L_0x024f
            if (r13 != 0) goto L_0x024f
            r1 = 0
            r0.chainStyle = r1
        L_0x024f:
            r12 = r24
            goto L_0x025a
        L_0x0252:
            r21 = r1
            r25 = r2
            r22 = r4
            r23 = r6
        L_0x025a:
            if (r4 <= r3) goto L_0x0260
            r1 = 2
            r0.chainStyle = r1
            goto L_0x0261
        L_0x0260:
            r1 = 2
        L_0x0261:
            if (r7 <= 0) goto L_0x0269
            if (r5 != 0) goto L_0x0269
            if (r9 != r10) goto L_0x0269
            r0.chainStyle = r1
        L_0x0269:
            int r1 = r0.chainStyle
            r2 = 1
            if (r1 != r2) goto L_0x031e
            r1 = 0
            if (r7 <= r2) goto L_0x0278
            int r6 = r3 - r4
            int r11 = r7 + -1
            int r1 = r6 / r11
            goto L_0x027f
        L_0x0278:
            if (r7 != r2) goto L_0x027f
            int r2 = r3 - r4
            r6 = 2
            int r1 = r2 / 2
        L_0x027f:
            if (r5 <= 0) goto L_0x0282
            r1 = 0
        L_0x0282:
            r2 = 0
            r6 = r2
            r2 = r21
        L_0x0286:
            if (r6 >= r8) goto L_0x0319
            r11 = r6
            if (r25 == 0) goto L_0x028f
            int r13 = r6 + 1
            int r11 = r8 - r13
        L_0x028f:
            java.util.ArrayList<androidx.constraintlayout.core.widgets.analyzer.WidgetRun> r13 = r0.widgets
            java.lang.Object r13 = r13.get(r11)
            androidx.constraintlayout.core.widgets.analyzer.WidgetRun r13 = (androidx.constraintlayout.core.widgets.analyzer.WidgetRun) r13
            androidx.constraintlayout.core.widgets.ConstraintWidget r14 = r13.widget
            int r14 = r14.getVisibility()
            r15 = 8
            if (r14 != r15) goto L_0x02af
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r14 = r13.start
            r14.resolve(r2)
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r14 = r13.end
            r14.resolve(r2)
            r16 = r1
            goto L_0x0313
        L_0x02af:
            if (r6 <= 0) goto L_0x02b6
            if (r25 == 0) goto L_0x02b5
            int r2 = r2 - r1
            goto L_0x02b6
        L_0x02b5:
            int r2 = r2 + r1
        L_0x02b6:
            if (r6 <= 0) goto L_0x02c7
            if (r6 < r9) goto L_0x02c7
            if (r25 == 0) goto L_0x02c2
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r14 = r13.start
            int r14 = r14.margin
            int r2 = r2 - r14
            goto L_0x02c7
        L_0x02c2:
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r14 = r13.start
            int r14 = r14.margin
            int r2 = r2 + r14
        L_0x02c7:
            if (r25 == 0) goto L_0x02cf
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r14 = r13.end
            r14.resolve(r2)
            goto L_0x02d4
        L_0x02cf:
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r14 = r13.start
            r14.resolve(r2)
        L_0x02d4:
            androidx.constraintlayout.core.widgets.analyzer.DimensionDependency r14 = r13.dimension
            int r14 = r14.value
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r15 = r13.dimensionBehavior
            r16 = r1
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r15 != r1) goto L_0x02e9
            int r1 = r13.matchConstraintsType
            r15 = 1
            if (r1 != r15) goto L_0x02e9
            androidx.constraintlayout.core.widgets.analyzer.DimensionDependency r1 = r13.dimension
            int r14 = r1.wrapValue
        L_0x02e9:
            if (r25 == 0) goto L_0x02ed
            int r2 = r2 - r14
            goto L_0x02ee
        L_0x02ed:
            int r2 = r2 + r14
        L_0x02ee:
            if (r25 == 0) goto L_0x02f6
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r1 = r13.start
            r1.resolve(r2)
            goto L_0x02fb
        L_0x02f6:
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r1 = r13.end
            r1.resolve(r2)
        L_0x02fb:
            r1 = 1
            r13.resolved = r1
            int r1 = r8 + -1
            if (r6 >= r1) goto L_0x0313
            if (r6 >= r10) goto L_0x0313
            if (r25 == 0) goto L_0x030d
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r1 = r13.end
            int r1 = r1.margin
            int r1 = -r1
            int r2 = r2 - r1
            goto L_0x0313
        L_0x030d:
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r1 = r13.end
            int r1 = r1.margin
            int r1 = -r1
            int r2 = r2 + r1
        L_0x0313:
            int r6 = r6 + 1
            r1 = r16
            goto L_0x0286
        L_0x0319:
            r16 = r1
            r1 = r2
            goto L_0x0485
        L_0x031e:
            if (r1 != 0) goto L_0x03c2
            int r1 = r3 - r4
            int r2 = r7 + 1
            int r1 = r1 / r2
            if (r5 <= 0) goto L_0x0328
            r1 = 0
        L_0x0328:
            r2 = 0
            r6 = r2
            r2 = r21
        L_0x032c:
            if (r6 >= r8) goto L_0x03bd
            r11 = r6
            if (r25 == 0) goto L_0x0335
            int r13 = r6 + 1
            int r11 = r8 - r13
        L_0x0335:
            java.util.ArrayList<androidx.constraintlayout.core.widgets.analyzer.WidgetRun> r13 = r0.widgets
            java.lang.Object r13 = r13.get(r11)
            androidx.constraintlayout.core.widgets.analyzer.WidgetRun r13 = (androidx.constraintlayout.core.widgets.analyzer.WidgetRun) r13
            androidx.constraintlayout.core.widgets.ConstraintWidget r14 = r13.widget
            int r14 = r14.getVisibility()
            r15 = 8
            if (r14 != r15) goto L_0x0354
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r14 = r13.start
            r14.resolve(r2)
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r14 = r13.end
            r14.resolve(r2)
            r16 = r1
            goto L_0x03b7
        L_0x0354:
            if (r25 == 0) goto L_0x0358
            int r2 = r2 - r1
            goto L_0x0359
        L_0x0358:
            int r2 = r2 + r1
        L_0x0359:
            if (r6 <= 0) goto L_0x036a
            if (r6 < r9) goto L_0x036a
            if (r25 == 0) goto L_0x0365
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r14 = r13.start
            int r14 = r14.margin
            int r2 = r2 - r14
            goto L_0x036a
        L_0x0365:
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r14 = r13.start
            int r14 = r14.margin
            int r2 = r2 + r14
        L_0x036a:
            if (r25 == 0) goto L_0x0372
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r14 = r13.end
            r14.resolve(r2)
            goto L_0x0377
        L_0x0372:
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r14 = r13.start
            r14.resolve(r2)
        L_0x0377:
            androidx.constraintlayout.core.widgets.analyzer.DimensionDependency r14 = r13.dimension
            int r14 = r14.value
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r15 = r13.dimensionBehavior
            r16 = r1
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r15 != r1) goto L_0x0390
            int r1 = r13.matchConstraintsType
            r15 = 1
            if (r1 != r15) goto L_0x0390
            androidx.constraintlayout.core.widgets.analyzer.DimensionDependency r1 = r13.dimension
            int r1 = r1.wrapValue
            int r14 = java.lang.Math.min(r14, r1)
        L_0x0390:
            if (r25 == 0) goto L_0x0394
            int r2 = r2 - r14
            goto L_0x0395
        L_0x0394:
            int r2 = r2 + r14
        L_0x0395:
            if (r25 == 0) goto L_0x039d
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r1 = r13.start
            r1.resolve(r2)
            goto L_0x03a2
        L_0x039d:
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r1 = r13.end
            r1.resolve(r2)
        L_0x03a2:
            int r1 = r8 + -1
            if (r6 >= r1) goto L_0x03b7
            if (r6 >= r10) goto L_0x03b7
            if (r25 == 0) goto L_0x03b1
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r1 = r13.end
            int r1 = r1.margin
            int r1 = -r1
            int r2 = r2 - r1
            goto L_0x03b7
        L_0x03b1:
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r1 = r13.end
            int r1 = r1.margin
            int r1 = -r1
            int r2 = r2 + r1
        L_0x03b7:
            int r6 = r6 + 1
            r1 = r16
            goto L_0x032c
        L_0x03bd:
            r16 = r1
            r1 = r2
            goto L_0x0485
        L_0x03c2:
            r2 = 2
            if (r1 != r2) goto L_0x0483
            int r1 = r0.orientation
            if (r1 != 0) goto L_0x03d0
            androidx.constraintlayout.core.widgets.ConstraintWidget r1 = r0.widget
            float r1 = r1.getHorizontalBiasPercent()
            goto L_0x03d6
        L_0x03d0:
            androidx.constraintlayout.core.widgets.ConstraintWidget r1 = r0.widget
            float r1 = r1.getVerticalBiasPercent()
        L_0x03d6:
            if (r25 == 0) goto L_0x03dd
            r2 = 1065353216(0x3f800000, float:1.0)
            float r1 = r2 - r1
        L_0x03dd:
            int r2 = r3 - r4
            float r2 = (float) r2
            float r2 = r2 * r1
            r6 = 1056964608(0x3f000000, float:0.5)
            float r2 = r2 + r6
            int r2 = (int) r2
            if (r2 < 0) goto L_0x03e9
            if (r5 <= 0) goto L_0x03ea
        L_0x03e9:
            r2 = 0
        L_0x03ea:
            if (r25 == 0) goto L_0x03ef
            int r6 = r21 - r2
            goto L_0x03f1
        L_0x03ef:
            int r6 = r21 + r2
        L_0x03f1:
            r11 = 0
        L_0x03f2:
            if (r11 >= r8) goto L_0x047f
            r13 = r11
            if (r25 == 0) goto L_0x03fb
            int r14 = r11 + 1
            int r13 = r8 - r14
        L_0x03fb:
            java.util.ArrayList<androidx.constraintlayout.core.widgets.analyzer.WidgetRun> r14 = r0.widgets
            java.lang.Object r14 = r14.get(r13)
            androidx.constraintlayout.core.widgets.analyzer.WidgetRun r14 = (androidx.constraintlayout.core.widgets.analyzer.WidgetRun) r14
            androidx.constraintlayout.core.widgets.ConstraintWidget r15 = r14.widget
            int r15 = r15.getVisibility()
            r0 = 8
            if (r15 != r0) goto L_0x041b
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r15 = r14.start
            r15.resolve(r6)
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r15 = r14.end
            r15.resolve(r6)
            r16 = r1
            r1 = 1
            goto L_0x0477
        L_0x041b:
            if (r11 <= 0) goto L_0x042c
            if (r11 < r9) goto L_0x042c
            if (r25 == 0) goto L_0x0427
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r15 = r14.start
            int r15 = r15.margin
            int r6 = r6 - r15
            goto L_0x042c
        L_0x0427:
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r15 = r14.start
            int r15 = r15.margin
            int r6 = r6 + r15
        L_0x042c:
            if (r25 == 0) goto L_0x0434
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r15 = r14.end
            r15.resolve(r6)
            goto L_0x0439
        L_0x0434:
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r15 = r14.start
            r15.resolve(r6)
        L_0x0439:
            androidx.constraintlayout.core.widgets.analyzer.DimensionDependency r15 = r14.dimension
            int r15 = r15.value
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r0 = r14.dimensionBehavior
            r16 = r1
            androidx.constraintlayout.core.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.core.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r1) goto L_0x044f
            int r0 = r14.matchConstraintsType
            r1 = 1
            if (r0 != r1) goto L_0x0450
            androidx.constraintlayout.core.widgets.analyzer.DimensionDependency r0 = r14.dimension
            int r15 = r0.wrapValue
            goto L_0x0450
        L_0x044f:
            r1 = 1
        L_0x0450:
            if (r25 == 0) goto L_0x0454
            int r6 = r6 - r15
            goto L_0x0455
        L_0x0454:
            int r6 = r6 + r15
        L_0x0455:
            if (r25 == 0) goto L_0x045d
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r0 = r14.start
            r0.resolve(r6)
            goto L_0x0462
        L_0x045d:
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r0 = r14.end
            r0.resolve(r6)
        L_0x0462:
            int r0 = r8 + -1
            if (r11 >= r0) goto L_0x0477
            if (r11 >= r10) goto L_0x0477
            if (r25 == 0) goto L_0x0471
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r0 = r14.end
            int r0 = r0.margin
            int r0 = -r0
            int r6 = r6 - r0
            goto L_0x0477
        L_0x0471:
            androidx.constraintlayout.core.widgets.analyzer.DependencyNode r0 = r14.end
            int r0 = r0.margin
            int r0 = -r0
            int r6 = r6 + r0
        L_0x0477:
            int r11 = r11 + 1
            r0 = r27
            r1 = r16
            goto L_0x03f2
        L_0x047f:
            r16 = r1
            r1 = r6
            goto L_0x0485
        L_0x0483:
            r1 = r21
        L_0x0485:
            return
        L_0x0486:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.widgets.analyzer.ChainRun.update(androidx.constraintlayout.core.widgets.analyzer.Dependency):void");
    }
}
