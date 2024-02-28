package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;

public abstract class WidgetRun implements Dependency {
    DimensionDependency dimension = new DimensionDependency(this);
    protected ConstraintWidget.DimensionBehaviour dimensionBehavior;
    public DependencyNode end = new DependencyNode(this);
    protected RunType mRunType = RunType.NONE;
    public int matchConstraintsType;
    public int orientation = 0;
    boolean resolved = false;
    RunGroup runGroup;
    public DependencyNode start = new DependencyNode(this);
    ConstraintWidget widget;

    /* renamed from: androidx.constraintlayout.core.widgets.analyzer.WidgetRun$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type;

        static {
            int[] iArr = new int[ConstraintAnchor.Type.values().length];
            $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type = iArr;
            try {
                iArr[ConstraintAnchor.Type.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.TOP.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.BASELINE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.BOTTOM.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    enum RunType {
        NONE,
        START,
        END,
        CENTER
    }

    public WidgetRun(ConstraintWidget widget2) {
        this.widget = widget2;
    }

    private void resolveDimension(int orientation2, int distance) {
        switch (this.matchConstraintsType) {
            case 0:
                this.dimension.resolve(getLimitedDimension(distance, orientation2));
                return;
            case 1:
                this.dimension.resolve(Math.min(getLimitedDimension(this.dimension.wrapValue, orientation2), distance));
                return;
            case 2:
                ConstraintWidget parent = this.widget.getParent();
                if (parent != null) {
                    WidgetRun widgetRun = orientation2 == 0 ? parent.horizontalRun : parent.verticalRun;
                    if (widgetRun.dimension.resolved) {
                        ConstraintWidget constraintWidget = this.widget;
                        this.dimension.resolve(getLimitedDimension((int) ((((float) widgetRun.dimension.value) * (orientation2 == 0 ? constraintWidget.mMatchConstraintPercentWidth : constraintWidget.mMatchConstraintPercentHeight)) + 0.5f), orientation2));
                        return;
                    }
                    return;
                }
                return;
            case 3:
                if (this.widget.horizontalRun.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || this.widget.horizontalRun.matchConstraintsType != 3 || this.widget.verticalRun.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || this.widget.verticalRun.matchConstraintsType != 3) {
                    ConstraintWidget constraintWidget2 = this.widget;
                    WidgetRun widgetRun2 = orientation2 == 0 ? constraintWidget2.verticalRun : constraintWidget2.horizontalRun;
                    if (widgetRun2.dimension.resolved) {
                        float dimensionRatio = this.widget.getDimensionRatio();
                        this.dimension.resolve(orientation2 == 1 ? (int) ((((float) widgetRun2.dimension.value) / dimensionRatio) + 0.5f) : (int) ((((float) widgetRun2.dimension.value) * dimensionRatio) + 0.5f));
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public final void addTarget(DependencyNode node, DependencyNode target, int margin) {
        node.targets.add(target);
        node.margin = margin;
        target.dependencies.add(node);
    }

    /* access modifiers changed from: protected */
    public final void addTarget(DependencyNode node, DependencyNode target, int marginFactor, DimensionDependency dimensionDependency) {
        node.targets.add(target);
        node.targets.add(this.dimension);
        node.marginFactor = marginFactor;
        node.marginDependency = dimensionDependency;
        target.dependencies.add(node);
        dimensionDependency.dependencies.add(node);
    }

    /* access modifiers changed from: package-private */
    public abstract void apply();

    /* access modifiers changed from: package-private */
    public abstract void applyToWidget();

    /* access modifiers changed from: package-private */
    public abstract void clear();

    /* access modifiers changed from: protected */
    public final int getLimitedDimension(int dimension2, int orientation2) {
        if (orientation2 == 0) {
            int i = this.widget.mMatchConstraintMaxWidth;
            int dimension3 = Math.max(this.widget.mMatchConstraintMinWidth, dimension2);
            if (i > 0) {
                dimension3 = Math.min(i, dimension2);
            }
            return dimension3 != dimension2 ? dimension3 : dimension2;
        }
        int i2 = this.widget.mMatchConstraintMaxHeight;
        int dimension4 = Math.max(this.widget.mMatchConstraintMinHeight, dimension2);
        if (i2 > 0) {
            dimension4 = Math.min(i2, dimension2);
        }
        return dimension4 != dimension2 ? dimension4 : dimension2;
    }

    /* access modifiers changed from: protected */
    public final DependencyNode getTarget(ConstraintAnchor anchor) {
        if (anchor.mTarget == null) {
            return null;
        }
        ConstraintWidget constraintWidget = anchor.mTarget.mOwner;
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[anchor.mTarget.mType.ordinal()]) {
            case 1:
                return constraintWidget.horizontalRun.start;
            case 2:
                return constraintWidget.horizontalRun.end;
            case 3:
                return constraintWidget.verticalRun.start;
            case 4:
                return constraintWidget.verticalRun.baseline;
            case 5:
                return constraintWidget.verticalRun.end;
            default:
                return null;
        }
    }

    /* access modifiers changed from: protected */
    public final DependencyNode getTarget(ConstraintAnchor anchor, int orientation2) {
        if (anchor.mTarget == null) {
            return null;
        }
        ConstraintWidget constraintWidget = anchor.mTarget.mOwner;
        WidgetRun widgetRun = orientation2 == 0 ? constraintWidget.horizontalRun : constraintWidget.verticalRun;
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[anchor.mTarget.mType.ordinal()]) {
            case 1:
            case 3:
                return widgetRun.start;
            case 2:
            case 5:
                return widgetRun.end;
            default:
                return null;
        }
    }

    public long getWrapDimension() {
        if (this.dimension.resolved) {
            return (long) this.dimension.value;
        }
        return 0;
    }

    public boolean isCenterConnection() {
        int i = 0;
        int size = this.start.targets.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (this.start.targets.get(i2).run != this) {
                i++;
            }
        }
        int size2 = this.end.targets.size();
        for (int i3 = 0; i3 < size2; i3++) {
            if (this.end.targets.get(i3).run != this) {
                i++;
            }
        }
        return i >= 2;
    }

    public boolean isDimensionResolved() {
        return this.dimension.resolved;
    }

    public boolean isResolved() {
        return this.resolved;
    }

    /* access modifiers changed from: package-private */
    public abstract void reset();

    /* access modifiers changed from: package-private */
    public abstract boolean supportsWrapComputation();

    public void update(Dependency dependency) {
    }

    /* access modifiers changed from: protected */
    public void updateRunCenter(Dependency dependency, ConstraintAnchor startAnchor, ConstraintAnchor endAnchor, int orientation2) {
        DependencyNode target = getTarget(startAnchor);
        DependencyNode target2 = getTarget(endAnchor);
        if (target.resolved && target2.resolved) {
            int margin = target.value + startAnchor.getMargin();
            int margin2 = target2.value - endAnchor.getMargin();
            int i = margin2 - margin;
            if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                resolveDimension(orientation2, i);
            }
            if (this.dimension.resolved) {
                if (this.dimension.value == i) {
                    this.start.resolve(margin);
                    this.end.resolve(margin2);
                    return;
                }
                ConstraintWidget constraintWidget = this.widget;
                float horizontalBiasPercent = orientation2 == 0 ? constraintWidget.getHorizontalBiasPercent() : constraintWidget.getVerticalBiasPercent();
                if (target == target2) {
                    margin = target.value;
                    margin2 = target2.value;
                    horizontalBiasPercent = 0.5f;
                }
                this.start.resolve((int) (((float) margin) + 0.5f + (((float) ((margin2 - margin) - this.dimension.value)) * horizontalBiasPercent)));
                this.end.resolve(this.start.value + this.dimension.value);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void updateRunEnd(Dependency dependency) {
    }

    /* access modifiers changed from: protected */
    public void updateRunStart(Dependency dependency) {
    }

    public long wrapSize(int direction) {
        if (!this.dimension.resolved) {
            return 0;
        }
        long j = (long) this.dimension.value;
        return isCenterConnection() ? j + ((long) (this.start.margin - this.end.margin)) : direction == 0 ? j + ((long) this.start.margin) : j - ((long) this.end.margin);
    }
}
