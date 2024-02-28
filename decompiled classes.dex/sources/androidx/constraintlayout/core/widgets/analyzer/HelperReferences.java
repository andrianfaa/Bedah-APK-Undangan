package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.widgets.Barrier;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.analyzer.DependencyNode;

class HelperReferences extends WidgetRun {
    public HelperReferences(ConstraintWidget widget) {
        super(widget);
    }

    private void addDependency(DependencyNode node) {
        this.start.dependencies.add(node);
        node.targets.add(this.start);
    }

    /* access modifiers changed from: package-private */
    public void apply() {
        if (this.widget instanceof Barrier) {
            this.start.delegateToWidgetRun = true;
            Barrier barrier = (Barrier) this.widget;
            int barrierType = barrier.getBarrierType();
            boolean allowsGoneWidget = barrier.getAllowsGoneWidget();
            switch (barrierType) {
                case 0:
                    this.start.type = DependencyNode.Type.LEFT;
                    for (int i = 0; i < barrier.mWidgetsCount; i++) {
                        ConstraintWidget constraintWidget = barrier.mWidgets[i];
                        if (allowsGoneWidget || constraintWidget.getVisibility() != 8) {
                            DependencyNode dependencyNode = constraintWidget.horizontalRun.start;
                            dependencyNode.dependencies.add(this.start);
                            this.start.targets.add(dependencyNode);
                        }
                    }
                    addDependency(this.widget.horizontalRun.start);
                    addDependency(this.widget.horizontalRun.end);
                    return;
                case 1:
                    this.start.type = DependencyNode.Type.RIGHT;
                    for (int i2 = 0; i2 < barrier.mWidgetsCount; i2++) {
                        ConstraintWidget constraintWidget2 = barrier.mWidgets[i2];
                        if (allowsGoneWidget || constraintWidget2.getVisibility() != 8) {
                            DependencyNode dependencyNode2 = constraintWidget2.horizontalRun.end;
                            dependencyNode2.dependencies.add(this.start);
                            this.start.targets.add(dependencyNode2);
                        }
                    }
                    addDependency(this.widget.horizontalRun.start);
                    addDependency(this.widget.horizontalRun.end);
                    return;
                case 2:
                    this.start.type = DependencyNode.Type.TOP;
                    for (int i3 = 0; i3 < barrier.mWidgetsCount; i3++) {
                        ConstraintWidget constraintWidget3 = barrier.mWidgets[i3];
                        if (allowsGoneWidget || constraintWidget3.getVisibility() != 8) {
                            DependencyNode dependencyNode3 = constraintWidget3.verticalRun.start;
                            dependencyNode3.dependencies.add(this.start);
                            this.start.targets.add(dependencyNode3);
                        }
                    }
                    addDependency(this.widget.verticalRun.start);
                    addDependency(this.widget.verticalRun.end);
                    return;
                case 3:
                    this.start.type = DependencyNode.Type.BOTTOM;
                    for (int i4 = 0; i4 < barrier.mWidgetsCount; i4++) {
                        ConstraintWidget constraintWidget4 = barrier.mWidgets[i4];
                        if (allowsGoneWidget || constraintWidget4.getVisibility() != 8) {
                            DependencyNode dependencyNode4 = constraintWidget4.verticalRun.end;
                            dependencyNode4.dependencies.add(this.start);
                            this.start.targets.add(dependencyNode4);
                        }
                    }
                    addDependency(this.widget.verticalRun.start);
                    addDependency(this.widget.verticalRun.end);
                    return;
                default:
                    return;
            }
        }
    }

    public void applyToWidget() {
        if (this.widget instanceof Barrier) {
            int barrierType = ((Barrier) this.widget).getBarrierType();
            if (barrierType == 0 || barrierType == 1) {
                this.widget.setX(this.start.value);
            } else {
                this.widget.setY(this.start.value);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void clear() {
        this.runGroup = null;
        this.start.clear();
    }

    /* access modifiers changed from: package-private */
    public void reset() {
        this.start.resolved = false;
    }

    /* access modifiers changed from: package-private */
    public boolean supportsWrapComputation() {
        return false;
    }

    public void update(Dependency dependency) {
        Barrier barrier = (Barrier) this.widget;
        int barrierType = barrier.getBarrierType();
        int i = -1;
        int i2 = 0;
        for (DependencyNode dependencyNode : this.start.targets) {
            int i3 = dependencyNode.value;
            if (i == -1 || i3 < i) {
                i = i3;
            }
            if (i2 < i3) {
                i2 = i3;
            }
        }
        if (barrierType == 0 || barrierType == 2) {
            this.start.resolve(barrier.getMargin() + i);
        } else {
            this.start.resolve(barrier.getMargin() + i2);
        }
    }
}
