package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.Helper;
import androidx.constraintlayout.core.widgets.analyzer.DependencyNode;
import androidx.constraintlayout.core.widgets.analyzer.WidgetRun;

public class VerticalWidgetRun extends WidgetRun {
    public DependencyNode baseline = new DependencyNode(this);
    DimensionDependency baselineDimension = null;

    /* renamed from: androidx.constraintlayout.core.widgets.analyzer.VerticalWidgetRun$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$core$widgets$analyzer$WidgetRun$RunType;

        static {
            int[] iArr = new int[WidgetRun.RunType.values().length];
            $SwitchMap$androidx$constraintlayout$core$widgets$analyzer$WidgetRun$RunType = iArr;
            try {
                iArr[WidgetRun.RunType.START.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$analyzer$WidgetRun$RunType[WidgetRun.RunType.END.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$analyzer$WidgetRun$RunType[WidgetRun.RunType.CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public VerticalWidgetRun(ConstraintWidget widget) {
        super(widget);
        this.start.type = DependencyNode.Type.TOP;
        this.end.type = DependencyNode.Type.BOTTOM;
        this.baseline.type = DependencyNode.Type.BASELINE;
        this.orientation = 1;
    }

    /* access modifiers changed from: package-private */
    public void apply() {
        ConstraintWidget parent;
        ConstraintWidget parent2;
        if (this.widget.measured) {
            this.dimension.resolve(this.widget.getHeight());
        }
        if (!this.dimension.resolved) {
            this.dimensionBehavior = this.widget.getVerticalDimensionBehaviour();
            if (this.widget.hasBaseline()) {
                this.baselineDimension = new BaselineDimensionDependency(this);
            }
            if (this.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT && (parent2 = this.widget.getParent()) != null && parent2.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) {
                    int height = (parent2.getHeight() - this.widget.mTop.getMargin()) - this.widget.mBottom.getMargin();
                    addTarget(this.start, parent2.verticalRun.start, this.widget.mTop.getMargin());
                    addTarget(this.end, parent2.verticalRun.end, -this.widget.mBottom.getMargin());
                    this.dimension.resolve(height);
                    return;
                } else if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.FIXED) {
                    this.dimension.resolve(this.widget.getHeight());
                }
            }
        } else if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT && (parent = this.widget.getParent()) != null && parent.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) {
            addTarget(this.start, parent.verticalRun.start, this.widget.mTop.getMargin());
            addTarget(this.end, parent.verticalRun.end, -this.widget.mBottom.getMargin());
            return;
        }
        if (!this.dimension.resolved || !this.widget.measured) {
            if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                switch (this.widget.mMatchConstraintDefaultHeight) {
                    case 2:
                        ConstraintWidget parent3 = this.widget.getParent();
                        if (parent3 != null) {
                            DimensionDependency dimensionDependency = parent3.verticalRun.dimension;
                            this.dimension.targets.add(dimensionDependency);
                            dimensionDependency.dependencies.add(this.dimension);
                            this.dimension.delegateToWidgetRun = true;
                            this.dimension.dependencies.add(this.start);
                            this.dimension.dependencies.add(this.end);
                            break;
                        }
                        break;
                    case 3:
                        if (!this.widget.isInVerticalChain() && this.widget.mMatchConstraintDefaultWidth != 3) {
                            DimensionDependency dimensionDependency2 = this.widget.horizontalRun.dimension;
                            this.dimension.targets.add(dimensionDependency2);
                            dimensionDependency2.dependencies.add(this.dimension);
                            this.dimension.delegateToWidgetRun = true;
                            this.dimension.dependencies.add(this.start);
                            this.dimension.dependencies.add(this.end);
                            break;
                        }
                }
            } else {
                this.dimension.addDependency(this);
            }
            if (this.widget.mListAnchors[2].mTarget != null && this.widget.mListAnchors[3].mTarget != null) {
                if (this.widget.isInVerticalChain()) {
                    this.start.margin = this.widget.mListAnchors[2].getMargin();
                    this.end.margin = -this.widget.mListAnchors[3].getMargin();
                } else {
                    DependencyNode target = getTarget(this.widget.mListAnchors[2]);
                    DependencyNode target2 = getTarget(this.widget.mListAnchors[3]);
                    if (target != null) {
                        target.addDependency(this);
                    }
                    if (target2 != null) {
                        target2.addDependency(this);
                    }
                    this.mRunType = WidgetRun.RunType.CENTER;
                }
                if (this.widget.hasBaseline()) {
                    addTarget(this.baseline, this.start, 1, this.baselineDimension);
                }
            } else if (this.widget.mListAnchors[2].mTarget != null) {
                DependencyNode target3 = getTarget(this.widget.mListAnchors[2]);
                if (target3 != null) {
                    addTarget(this.start, target3, this.widget.mListAnchors[2].getMargin());
                    addTarget(this.end, this.start, 1, this.dimension);
                    if (this.widget.hasBaseline()) {
                        addTarget(this.baseline, this.start, 1, this.baselineDimension);
                    }
                    if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.widget.getDimensionRatio() > 0.0f && this.widget.horizontalRun.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        this.widget.horizontalRun.dimension.dependencies.add(this.dimension);
                        this.dimension.targets.add(this.widget.horizontalRun.dimension);
                        this.dimension.updateDelegate = this;
                    }
                }
            } else if (this.widget.mListAnchors[3].mTarget != null) {
                DependencyNode target4 = getTarget(this.widget.mListAnchors[3]);
                if (target4 != null) {
                    addTarget(this.end, target4, -this.widget.mListAnchors[3].getMargin());
                    addTarget(this.start, this.end, -1, this.dimension);
                    if (this.widget.hasBaseline()) {
                        addTarget(this.baseline, this.start, 1, this.baselineDimension);
                    }
                }
            } else if (this.widget.mListAnchors[4].mTarget != null) {
                DependencyNode target5 = getTarget(this.widget.mListAnchors[4]);
                if (target5 != null) {
                    addTarget(this.baseline, target5, 0);
                    addTarget(this.start, this.baseline, -1, this.baselineDimension);
                    addTarget(this.end, this.start, 1, this.dimension);
                }
            } else if (!(this.widget instanceof Helper) && this.widget.getParent() != null) {
                addTarget(this.start, this.widget.getParent().verticalRun.start, this.widget.getY());
                addTarget(this.end, this.start, 1, this.dimension);
                if (this.widget.hasBaseline()) {
                    addTarget(this.baseline, this.start, 1, this.baselineDimension);
                }
                if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.widget.getDimensionRatio() > 0.0f && this.widget.horizontalRun.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    this.widget.horizontalRun.dimension.dependencies.add(this.dimension);
                    this.dimension.targets.add(this.widget.horizontalRun.dimension);
                    this.dimension.updateDelegate = this;
                }
            }
            if (this.dimension.targets.size() == 0) {
                this.dimension.readyToSolve = true;
            }
        } else if (this.widget.mListAnchors[2].mTarget != null && this.widget.mListAnchors[3].mTarget != null) {
            if (this.widget.isInVerticalChain()) {
                this.start.margin = this.widget.mListAnchors[2].getMargin();
                this.end.margin = -this.widget.mListAnchors[3].getMargin();
            } else {
                DependencyNode target6 = getTarget(this.widget.mListAnchors[2]);
                if (target6 != null) {
                    addTarget(this.start, target6, this.widget.mListAnchors[2].getMargin());
                }
                DependencyNode target7 = getTarget(this.widget.mListAnchors[3]);
                if (target7 != null) {
                    addTarget(this.end, target7, -this.widget.mListAnchors[3].getMargin());
                }
                this.start.delegateToWidgetRun = true;
                this.end.delegateToWidgetRun = true;
            }
            if (this.widget.hasBaseline()) {
                addTarget(this.baseline, this.start, this.widget.getBaselineDistance());
            }
        } else if (this.widget.mListAnchors[2].mTarget != null) {
            DependencyNode target8 = getTarget(this.widget.mListAnchors[2]);
            if (target8 != null) {
                addTarget(this.start, target8, this.widget.mListAnchors[2].getMargin());
                addTarget(this.end, this.start, this.dimension.value);
                if (this.widget.hasBaseline()) {
                    addTarget(this.baseline, this.start, this.widget.getBaselineDistance());
                }
            }
        } else if (this.widget.mListAnchors[3].mTarget != null) {
            DependencyNode target9 = getTarget(this.widget.mListAnchors[3]);
            if (target9 != null) {
                addTarget(this.end, target9, -this.widget.mListAnchors[3].getMargin());
                addTarget(this.start, this.end, -this.dimension.value);
            }
            if (this.widget.hasBaseline()) {
                addTarget(this.baseline, this.start, this.widget.getBaselineDistance());
            }
        } else if (this.widget.mListAnchors[4].mTarget != null) {
            DependencyNode target10 = getTarget(this.widget.mListAnchors[4]);
            if (target10 != null) {
                addTarget(this.baseline, target10, 0);
                addTarget(this.start, this.baseline, -this.widget.getBaselineDistance());
                addTarget(this.end, this.start, this.dimension.value);
            }
        } else if (!(this.widget instanceof Helper) && this.widget.getParent() != null && this.widget.getAnchor(ConstraintAnchor.Type.CENTER).mTarget == null) {
            addTarget(this.start, this.widget.getParent().verticalRun.start, this.widget.getY());
            addTarget(this.end, this.start, this.dimension.value);
            if (this.widget.hasBaseline()) {
                addTarget(this.baseline, this.start, this.widget.getBaselineDistance());
            }
        }
    }

    public void applyToWidget() {
        if (this.start.resolved) {
            this.widget.setY(this.start.value);
        }
    }

    /* access modifiers changed from: package-private */
    public void clear() {
        this.runGroup = null;
        this.start.clear();
        this.end.clear();
        this.baseline.clear();
        this.dimension.clear();
        this.resolved = false;
    }

    /* access modifiers changed from: package-private */
    public void reset() {
        this.resolved = false;
        this.start.clear();
        this.start.resolved = false;
        this.end.clear();
        this.end.resolved = false;
        this.baseline.clear();
        this.baseline.resolved = false;
        this.dimension.resolved = false;
    }

    /* access modifiers changed from: package-private */
    public boolean supportsWrapComputation() {
        return this.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || this.widget.mMatchConstraintDefaultHeight == 0;
    }

    public String toString() {
        return "VerticalRun " + this.widget.getDebugName();
    }

    public void update(Dependency dependency) {
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$widgets$analyzer$WidgetRun$RunType[this.mRunType.ordinal()]) {
            case 1:
                updateRunStart(dependency);
                break;
            case 2:
                updateRunEnd(dependency);
                break;
            case 3:
                updateRunCenter(dependency, this.widget.mTop, this.widget.mBottom, 1);
                return;
        }
        if (this.dimension.readyToSolve && !this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            switch (this.widget.mMatchConstraintDefaultHeight) {
                case 2:
                    ConstraintWidget parent = this.widget.getParent();
                    if (parent != null && parent.verticalRun.dimension.resolved) {
                        float f = this.widget.mMatchConstraintPercentHeight;
                        this.dimension.resolve((int) ((((float) parent.verticalRun.dimension.value) * f) + 0.5f));
                        break;
                    }
                case 3:
                    if (this.widget.horizontalRun.dimension.resolved) {
                        int i = 0;
                        switch (this.widget.getDimensionRatioSide()) {
                            case -1:
                                i = (int) ((((float) this.widget.horizontalRun.dimension.value) / this.widget.getDimensionRatio()) + 0.5f);
                                break;
                            case 0:
                                i = (int) ((((float) this.widget.horizontalRun.dimension.value) * this.widget.getDimensionRatio()) + 0.5f);
                                break;
                            case 1:
                                i = (int) ((((float) this.widget.horizontalRun.dimension.value) / this.widget.getDimensionRatio()) + 0.5f);
                                break;
                        }
                        this.dimension.resolve(i);
                        break;
                    }
                    break;
            }
        }
        if (this.start.readyToSolve && this.end.readyToSolve) {
            if (this.start.resolved && this.end.resolved && this.dimension.resolved) {
                return;
            }
            if (this.dimension.resolved || this.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || this.widget.mMatchConstraintDefaultWidth != 0 || this.widget.isInVerticalChain()) {
                if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.matchConstraintsType == 1 && this.start.targets.size() > 0 && this.end.targets.size() > 0) {
                    int i2 = (this.end.targets.get(0).value + this.end.margin) - (this.start.targets.get(0).value + this.start.margin);
                    if (i2 < this.dimension.wrapValue) {
                        this.dimension.resolve(i2);
                    } else {
                        this.dimension.resolve(this.dimension.wrapValue);
                    }
                }
                if (this.dimension.resolved && this.start.targets.size() > 0 && this.end.targets.size() > 0) {
                    DependencyNode dependencyNode = this.start.targets.get(0);
                    DependencyNode dependencyNode2 = this.end.targets.get(0);
                    int i3 = dependencyNode.value + this.start.margin;
                    int i4 = dependencyNode2.value + this.end.margin;
                    float verticalBiasPercent = this.widget.getVerticalBiasPercent();
                    if (dependencyNode == dependencyNode2) {
                        i3 = dependencyNode.value;
                        i4 = dependencyNode2.value;
                        verticalBiasPercent = 0.5f;
                    }
                    this.start.resolve((int) (((float) i3) + 0.5f + (((float) ((i4 - i3) - this.dimension.value)) * verticalBiasPercent)));
                    this.end.resolve(this.start.value + this.dimension.value);
                    return;
                }
                return;
            }
            int i5 = this.start.targets.get(0).value + this.start.margin;
            int i6 = this.end.targets.get(0).value + this.end.margin;
            this.start.resolve(i5);
            this.end.resolve(i6);
            this.dimension.resolve(i6 - i5);
        }
    }
}
