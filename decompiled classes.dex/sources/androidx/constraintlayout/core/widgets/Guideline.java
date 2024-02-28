package androidx.constraintlayout.core.widgets;

import androidx.constraintlayout.core.LinearSystem;
import androidx.constraintlayout.core.SolverVariable;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import java.util.HashMap;

public class Guideline extends ConstraintWidget {
    public static final int HORIZONTAL = 0;
    public static final int RELATIVE_BEGIN = 1;
    public static final int RELATIVE_END = 2;
    public static final int RELATIVE_PERCENT = 0;
    public static final int RELATIVE_UNKNOWN = -1;
    public static final int VERTICAL = 1;
    protected boolean guidelineUseRtl = true;
    private ConstraintAnchor mAnchor = this.mTop;
    private int mMinimumPosition = 0;
    private int mOrientation = 0;
    protected int mRelativeBegin = -1;
    protected int mRelativeEnd = -1;
    protected float mRelativePercent = -1.0f;
    private boolean resolved;

    /* renamed from: androidx.constraintlayout.core.widgets.Guideline$1  reason: invalid class name */
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
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.BOTTOM.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.BASELINE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.CENTER.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.CENTER_X.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.CENTER_Y.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.NONE.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
        }
    }

    public Guideline() {
        this.mAnchors.clear();
        this.mAnchors.add(this.mAnchor);
        int length = this.mListAnchors.length;
        for (int i = 0; i < length; i++) {
            this.mListAnchors[i] = this.mAnchor;
        }
    }

    public void addToSolver(LinearSystem system, boolean optimize) {
        ConstraintWidgetContainer constraintWidgetContainer = (ConstraintWidgetContainer) getParent();
        if (constraintWidgetContainer != null) {
            ConstraintAnchor anchor = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.LEFT);
            ConstraintAnchor anchor2 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.RIGHT);
            boolean z = true;
            boolean z2 = this.mParent != null && this.mParent.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            if (this.mOrientation == 0) {
                anchor = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.TOP);
                anchor2 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.BOTTOM);
                if (this.mParent == null || this.mParent.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    z = false;
                }
                z2 = z;
            }
            if (this.resolved && this.mAnchor.hasFinalValue()) {
                SolverVariable createObjectVariable = system.createObjectVariable(this.mAnchor);
                system.addEquality(createObjectVariable, this.mAnchor.getFinalValue());
                if (this.mRelativeBegin != -1) {
                    if (z2) {
                        system.addGreaterThan(system.createObjectVariable(anchor2), createObjectVariable, 0, 5);
                    }
                } else if (this.mRelativeEnd != -1 && z2) {
                    SolverVariable createObjectVariable2 = system.createObjectVariable(anchor2);
                    system.addGreaterThan(createObjectVariable, system.createObjectVariable(anchor), 0, 5);
                    system.addGreaterThan(createObjectVariable2, createObjectVariable, 0, 5);
                }
                this.resolved = false;
            } else if (this.mRelativeBegin != -1) {
                SolverVariable createObjectVariable3 = system.createObjectVariable(this.mAnchor);
                system.addEquality(createObjectVariable3, system.createObjectVariable(anchor), this.mRelativeBegin, 8);
                if (z2) {
                    system.addGreaterThan(system.createObjectVariable(anchor2), createObjectVariable3, 0, 5);
                }
            } else if (this.mRelativeEnd != -1) {
                SolverVariable createObjectVariable4 = system.createObjectVariable(this.mAnchor);
                SolverVariable createObjectVariable5 = system.createObjectVariable(anchor2);
                system.addEquality(createObjectVariable4, createObjectVariable5, -this.mRelativeEnd, 8);
                if (z2) {
                    system.addGreaterThan(createObjectVariable4, system.createObjectVariable(anchor), 0, 5);
                    system.addGreaterThan(createObjectVariable5, createObjectVariable4, 0, 5);
                }
            } else if (this.mRelativePercent != -1.0f) {
                system.addConstraint(LinearSystem.createRowDimensionPercent(system, system.createObjectVariable(this.mAnchor), system.createObjectVariable(anchor2), this.mRelativePercent));
            }
        }
    }

    public boolean allowedInBarrier() {
        return true;
    }

    public void copy(ConstraintWidget src, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        super.copy(src, hashMap);
        Guideline guideline = (Guideline) src;
        this.mRelativePercent = guideline.mRelativePercent;
        this.mRelativeBegin = guideline.mRelativeBegin;
        this.mRelativeEnd = guideline.mRelativeEnd;
        this.guidelineUseRtl = guideline.guidelineUseRtl;
        setOrientation(guideline.mOrientation);
    }

    public void cyclePosition() {
        if (this.mRelativeBegin != -1) {
            inferRelativePercentPosition();
        } else if (this.mRelativePercent != -1.0f) {
            inferRelativeEndPosition();
        } else if (this.mRelativeEnd != -1) {
            inferRelativeBeginPosition();
        }
    }

    public ConstraintAnchor getAnchor() {
        return this.mAnchor;
    }

    public ConstraintAnchor getAnchor(ConstraintAnchor.Type anchorType) {
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[anchorType.ordinal()]) {
            case 1:
            case 2:
                if (this.mOrientation == 1) {
                    return this.mAnchor;
                }
                break;
            case 3:
            case 4:
                if (this.mOrientation == 0) {
                    return this.mAnchor;
                }
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return null;
        }
        return null;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getRelativeBegin() {
        return this.mRelativeBegin;
    }

    public int getRelativeBehaviour() {
        if (this.mRelativePercent != -1.0f) {
            return 0;
        }
        if (this.mRelativeBegin != -1) {
            return 1;
        }
        return this.mRelativeEnd != -1 ? 2 : -1;
    }

    public int getRelativeEnd() {
        return this.mRelativeEnd;
    }

    public float getRelativePercent() {
        return this.mRelativePercent;
    }

    public String getType() {
        return "Guideline";
    }

    /* access modifiers changed from: package-private */
    public void inferRelativeBeginPosition() {
        int x = getX();
        if (this.mOrientation == 0) {
            x = getY();
        }
        setGuideBegin(x);
    }

    /* access modifiers changed from: package-private */
    public void inferRelativeEndPosition() {
        int width = getParent().getWidth() - getX();
        if (this.mOrientation == 0) {
            width = getParent().getHeight() - getY();
        }
        setGuideEnd(width);
    }

    /* access modifiers changed from: package-private */
    public void inferRelativePercentPosition() {
        float x = ((float) getX()) / ((float) getParent().getWidth());
        if (this.mOrientation == 0) {
            x = ((float) getY()) / ((float) getParent().getHeight());
        }
        setGuidePercent(x);
    }

    public boolean isPercent() {
        return this.mRelativePercent != -1.0f && this.mRelativeBegin == -1 && this.mRelativeEnd == -1;
    }

    public boolean isResolvedHorizontally() {
        return this.resolved;
    }

    public boolean isResolvedVertically() {
        return this.resolved;
    }

    public void setFinalValue(int position) {
        this.mAnchor.setFinalValue(position);
        this.resolved = true;
    }

    public void setGuideBegin(int value) {
        if (value > -1) {
            this.mRelativePercent = -1.0f;
            this.mRelativeBegin = value;
            this.mRelativeEnd = -1;
        }
    }

    public void setGuideEnd(int value) {
        if (value > -1) {
            this.mRelativePercent = -1.0f;
            this.mRelativeBegin = -1;
            this.mRelativeEnd = value;
        }
    }

    public void setGuidePercent(float value) {
        if (value > -1.0f) {
            this.mRelativePercent = value;
            this.mRelativeBegin = -1;
            this.mRelativeEnd = -1;
        }
    }

    public void setGuidePercent(int value) {
        setGuidePercent(((float) value) / 100.0f);
    }

    public void setMinimumPosition(int minimum) {
        this.mMinimumPosition = minimum;
    }

    public void setOrientation(int orientation) {
        if (this.mOrientation != orientation) {
            this.mOrientation = orientation;
            this.mAnchors.clear();
            if (this.mOrientation == 1) {
                this.mAnchor = this.mLeft;
            } else {
                this.mAnchor = this.mTop;
            }
            this.mAnchors.add(this.mAnchor);
            int length = this.mListAnchors.length;
            for (int i = 0; i < length; i++) {
                this.mListAnchors[i] = this.mAnchor;
            }
        }
    }

    public void updateFromSolver(LinearSystem system, boolean optimize) {
        if (getParent() != null) {
            int objectVariableValue = system.getObjectVariableValue(this.mAnchor);
            if (this.mOrientation == 1) {
                setX(objectVariableValue);
                setY(0);
                setHeight(getParent().getHeight());
                setWidth(0);
                return;
            }
            setX(0);
            setY(objectVariableValue);
            setWidth(getParent().getWidth());
            setHeight(0);
        }
    }
}
