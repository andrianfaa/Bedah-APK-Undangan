package androidx.constraintlayout.core.widgets;

import androidx.constraintlayout.core.LinearSystem;
import androidx.constraintlayout.core.SolverVariable;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import java.util.HashMap;

public class Barrier extends HelperWidget {
    public static final int BOTTOM = 3;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int TOP = 2;
    private static final boolean USE_RELAX_GONE = false;
    private static final boolean USE_RESOLUTION = true;
    private boolean mAllowsGoneWidget = USE_RESOLUTION;
    private int mBarrierType = 0;
    private int mMargin = 0;
    boolean resolved = false;

    public Barrier() {
    }

    public Barrier(String debugName) {
        setDebugName(debugName);
    }

    public void addToSolver(LinearSystem system, boolean optimize) {
        int i;
        LinearSystem linearSystem = system;
        this.mListAnchors[0] = this.mLeft;
        this.mListAnchors[2] = this.mTop;
        this.mListAnchors[1] = this.mRight;
        this.mListAnchors[3] = this.mBottom;
        for (int i2 = 0; i2 < this.mListAnchors.length; i2++) {
            this.mListAnchors[i2].mSolverVariable = linearSystem.createObjectVariable(this.mListAnchors[i2]);
        }
        int i3 = this.mBarrierType;
        if (i3 >= 0 && i3 < 4) {
            ConstraintAnchor constraintAnchor = this.mListAnchors[this.mBarrierType];
            if (!this.resolved) {
                allSolved();
            }
            if (this.resolved) {
                this.resolved = false;
                int i4 = this.mBarrierType;
                if (i4 == 0 || i4 == 1) {
                    linearSystem.addEquality(this.mLeft.mSolverVariable, this.mX);
                    linearSystem.addEquality(this.mRight.mSolverVariable, this.mX);
                } else if (i4 == 2 || i4 == 3) {
                    linearSystem.addEquality(this.mTop.mSolverVariable, this.mY);
                    linearSystem.addEquality(this.mBottom.mSolverVariable, this.mY);
                }
            } else {
                boolean z = false;
                int i5 = 0;
                while (true) {
                    if (i5 >= this.mWidgetsCount) {
                        break;
                    }
                    ConstraintWidget constraintWidget = this.mWidgets[i5];
                    if (this.mAllowsGoneWidget || constraintWidget.allowedInBarrier()) {
                        int i6 = this.mBarrierType;
                        if ((i6 != 0 && i6 != 1) || constraintWidget.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget.mLeft.mTarget == null || constraintWidget.mRight.mTarget == null) {
                            int i7 = this.mBarrierType;
                            if ((i7 == 2 || i7 == 3) && constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget != null) {
                                z = USE_RESOLUTION;
                                break;
                            }
                        } else {
                            z = USE_RESOLUTION;
                            break;
                        }
                    }
                    i5++;
                }
                boolean z2 = this.mLeft.hasCenteredDependents() || this.mRight.hasCenteredDependents();
                boolean z3 = this.mTop.hasCenteredDependents() || this.mBottom.hasCenteredDependents();
                int i8 = 5;
                if (!(!z && (((i = this.mBarrierType) == 0 && z2) || ((i == 2 && z3) || ((i == 1 && z2) || (i == 3 && z3)))))) {
                    i8 = 4;
                }
                for (int i9 = 0; i9 < this.mWidgetsCount; i9++) {
                    ConstraintWidget constraintWidget2 = this.mWidgets[i9];
                    if (this.mAllowsGoneWidget || constraintWidget2.allowedInBarrier()) {
                        SolverVariable createObjectVariable = linearSystem.createObjectVariable(constraintWidget2.mListAnchors[this.mBarrierType]);
                        constraintWidget2.mListAnchors[this.mBarrierType].mSolverVariable = createObjectVariable;
                        int i10 = 0;
                        if (constraintWidget2.mListAnchors[this.mBarrierType].mTarget != null && constraintWidget2.mListAnchors[this.mBarrierType].mTarget.mOwner == this) {
                            i10 = 0 + constraintWidget2.mListAnchors[this.mBarrierType].mMargin;
                        }
                        int i11 = this.mBarrierType;
                        if (i11 == 0 || i11 == 2) {
                            linearSystem.addLowerBarrier(constraintAnchor.mSolverVariable, createObjectVariable, this.mMargin - i10, z);
                        } else {
                            linearSystem.addGreaterBarrier(constraintAnchor.mSolverVariable, createObjectVariable, this.mMargin + i10, z);
                        }
                        linearSystem.addEquality(constraintAnchor.mSolverVariable, createObjectVariable, this.mMargin + i10, i8);
                    }
                }
                int i12 = this.mBarrierType;
                if (i12 == 0) {
                    linearSystem.addEquality(this.mRight.mSolverVariable, this.mLeft.mSolverVariable, 0, 8);
                    linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mRight.mSolverVariable, 0, 4);
                    linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mLeft.mSolverVariable, 0, 0);
                } else if (i12 == 1) {
                    linearSystem.addEquality(this.mLeft.mSolverVariable, this.mRight.mSolverVariable, 0, 8);
                    linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mLeft.mSolverVariable, 0, 4);
                    linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mRight.mSolverVariable, 0, 0);
                } else if (i12 == 2) {
                    linearSystem.addEquality(this.mBottom.mSolverVariable, this.mTop.mSolverVariable, 0, 8);
                    linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mBottom.mSolverVariable, 0, 4);
                    linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mTop.mSolverVariable, 0, 0);
                } else if (i12 == 3) {
                    linearSystem.addEquality(this.mTop.mSolverVariable, this.mBottom.mSolverVariable, 0, 8);
                    linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mTop.mSolverVariable, 0, 4);
                    linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mBottom.mSolverVariable, 0, 0);
                }
            }
        }
    }

    public boolean allSolved() {
        boolean z = USE_RESOLUTION;
        for (int i = 0; i < this.mWidgetsCount; i++) {
            ConstraintWidget constraintWidget = this.mWidgets[i];
            if (this.mAllowsGoneWidget || constraintWidget.allowedInBarrier()) {
                int i2 = this.mBarrierType;
                if ((i2 == 0 || i2 == 1) && !constraintWidget.isResolvedHorizontally()) {
                    z = false;
                } else {
                    int i3 = this.mBarrierType;
                    if ((i3 == 2 || i3 == 3) && !constraintWidget.isResolvedVertically()) {
                        z = false;
                    }
                }
            }
        }
        if (!z || this.mWidgetsCount <= 0) {
            return false;
        }
        int i4 = 0;
        boolean z2 = false;
        for (int i5 = 0; i5 < this.mWidgetsCount; i5++) {
            ConstraintWidget constraintWidget2 = this.mWidgets[i5];
            if (this.mAllowsGoneWidget || constraintWidget2.allowedInBarrier()) {
                if (!z2) {
                    int i6 = this.mBarrierType;
                    if (i6 == 0) {
                        i4 = constraintWidget2.getAnchor(ConstraintAnchor.Type.LEFT).getFinalValue();
                    } else if (i6 == 1) {
                        i4 = constraintWidget2.getAnchor(ConstraintAnchor.Type.RIGHT).getFinalValue();
                    } else if (i6 == 2) {
                        i4 = constraintWidget2.getAnchor(ConstraintAnchor.Type.TOP).getFinalValue();
                    } else if (i6 == 3) {
                        i4 = constraintWidget2.getAnchor(ConstraintAnchor.Type.BOTTOM).getFinalValue();
                    }
                    z2 = USE_RESOLUTION;
                }
                int i7 = this.mBarrierType;
                if (i7 == 0) {
                    i4 = Math.min(i4, constraintWidget2.getAnchor(ConstraintAnchor.Type.LEFT).getFinalValue());
                } else if (i7 == 1) {
                    i4 = Math.max(i4, constraintWidget2.getAnchor(ConstraintAnchor.Type.RIGHT).getFinalValue());
                } else if (i7 == 2) {
                    i4 = Math.min(i4, constraintWidget2.getAnchor(ConstraintAnchor.Type.TOP).getFinalValue());
                } else if (i7 == 3) {
                    i4 = Math.max(i4, constraintWidget2.getAnchor(ConstraintAnchor.Type.BOTTOM).getFinalValue());
                }
            }
        }
        int i8 = i4 + this.mMargin;
        int i9 = this.mBarrierType;
        if (i9 == 0 || i9 == 1) {
            setFinalHorizontal(i8, i8);
        } else {
            setFinalVertical(i8, i8);
        }
        this.resolved = USE_RESOLUTION;
        return USE_RESOLUTION;
    }

    public boolean allowedInBarrier() {
        return USE_RESOLUTION;
    }

    @Deprecated
    public boolean allowsGoneWidget() {
        return this.mAllowsGoneWidget;
    }

    public void copy(ConstraintWidget src, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        super.copy(src, hashMap);
        Barrier barrier = (Barrier) src;
        this.mBarrierType = barrier.mBarrierType;
        this.mAllowsGoneWidget = barrier.mAllowsGoneWidget;
        this.mMargin = barrier.mMargin;
    }

    public boolean getAllowsGoneWidget() {
        return this.mAllowsGoneWidget;
    }

    public int getBarrierType() {
        return this.mBarrierType;
    }

    public int getMargin() {
        return this.mMargin;
    }

    public int getOrientation() {
        switch (this.mBarrierType) {
            case 0:
            case 1:
                return 0;
            case 2:
            case 3:
                return 1;
            default:
                return -1;
        }
    }

    public boolean isResolvedHorizontally() {
        return this.resolved;
    }

    public boolean isResolvedVertically() {
        return this.resolved;
    }

    /* access modifiers changed from: protected */
    public void markWidgets() {
        for (int i = 0; i < this.mWidgetsCount; i++) {
            ConstraintWidget constraintWidget = this.mWidgets[i];
            if (this.mAllowsGoneWidget || constraintWidget.allowedInBarrier()) {
                int i2 = this.mBarrierType;
                if (i2 == 0 || i2 == 1) {
                    constraintWidget.setInBarrier(0, USE_RESOLUTION);
                } else if (i2 == 2 || i2 == 3) {
                    constraintWidget.setInBarrier(1, USE_RESOLUTION);
                }
            }
        }
    }

    public void setAllowsGoneWidget(boolean allowsGoneWidget) {
        this.mAllowsGoneWidget = allowsGoneWidget;
    }

    public void setBarrierType(int barrierType) {
        this.mBarrierType = barrierType;
    }

    public void setMargin(int margin) {
        this.mMargin = margin;
    }

    public String toString() {
        String str = "[Barrier] " + getDebugName() + " {";
        for (int i = 0; i < this.mWidgetsCount; i++) {
            ConstraintWidget constraintWidget = this.mWidgets[i];
            if (i > 0) {
                str = str + ", ";
            }
            str = str + constraintWidget.getDebugName();
        }
        return str + "}";
    }
}
