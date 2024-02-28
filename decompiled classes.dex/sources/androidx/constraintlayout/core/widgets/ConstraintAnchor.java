package androidx.constraintlayout.core.widgets;

import androidx.constraintlayout.core.Cache;
import androidx.constraintlayout.core.SolverVariable;
import androidx.constraintlayout.core.widgets.analyzer.Grouping;
import androidx.constraintlayout.core.widgets.analyzer.WidgetGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ConstraintAnchor {
    private static final boolean ALLOW_BINARY = false;
    private static final int UNSET_GONE_MARGIN = Integer.MIN_VALUE;
    private HashSet<ConstraintAnchor> mDependents = null;
    private int mFinalValue;
    int mGoneMargin = Integer.MIN_VALUE;
    private boolean mHasFinalValue;
    public int mMargin = 0;
    public final ConstraintWidget mOwner;
    SolverVariable mSolverVariable;
    public ConstraintAnchor mTarget;
    public final Type mType;

    /* renamed from: androidx.constraintlayout.core.widgets.ConstraintAnchor$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type;

        static {
            int[] iArr = new int[Type.values().length];
            $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type = iArr;
            try {
                iArr[Type.CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[Type.LEFT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[Type.RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[Type.TOP.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[Type.BOTTOM.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[Type.BASELINE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[Type.CENTER_X.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[Type.CENTER_Y.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[Type.NONE.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
        }
    }

    public enum Type {
        NONE,
        LEFT,
        TOP,
        RIGHT,
        BOTTOM,
        BASELINE,
        CENTER,
        CENTER_X,
        CENTER_Y
    }

    public ConstraintAnchor(ConstraintWidget owner, Type type) {
        this.mOwner = owner;
        this.mType = type;
    }

    private boolean isConnectionToMe(ConstraintWidget target, HashSet<ConstraintWidget> hashSet) {
        if (hashSet.contains(target)) {
            return false;
        }
        hashSet.add(target);
        if (target == getOwner()) {
            return true;
        }
        ArrayList<ConstraintAnchor> anchors = target.getAnchors();
        int size = anchors.size();
        for (int i = 0; i < size; i++) {
            ConstraintAnchor constraintAnchor = anchors.get(i);
            if (constraintAnchor.isSimilarDimensionConnection(this) && constraintAnchor.isConnected() && isConnectionToMe(constraintAnchor.getTarget().getOwner(), hashSet)) {
                return true;
            }
        }
        return false;
    }

    public boolean connect(ConstraintAnchor toAnchor, int margin) {
        return connect(toAnchor, margin, Integer.MIN_VALUE, false);
    }

    public boolean connect(ConstraintAnchor toAnchor, int margin, int goneMargin, boolean forceConnection) {
        if (toAnchor == null) {
            reset();
            return true;
        } else if (!forceConnection && !isValidConnection(toAnchor)) {
            return false;
        } else {
            this.mTarget = toAnchor;
            if (toAnchor.mDependents == null) {
                toAnchor.mDependents = new HashSet<>();
            }
            HashSet<ConstraintAnchor> hashSet = this.mTarget.mDependents;
            if (hashSet != null) {
                hashSet.add(this);
            }
            this.mMargin = margin;
            this.mGoneMargin = goneMargin;
            return true;
        }
    }

    public void copyFrom(ConstraintAnchor source, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        HashSet<ConstraintAnchor> hashSet;
        ConstraintAnchor constraintAnchor = this.mTarget;
        if (!(constraintAnchor == null || (hashSet = constraintAnchor.mDependents) == null)) {
            hashSet.remove(this);
        }
        ConstraintAnchor constraintAnchor2 = source.mTarget;
        if (constraintAnchor2 != null) {
            this.mTarget = hashMap.get(source.mTarget.mOwner).getAnchor(constraintAnchor2.getType());
        } else {
            this.mTarget = null;
        }
        ConstraintAnchor constraintAnchor3 = this.mTarget;
        if (constraintAnchor3 != null) {
            if (constraintAnchor3.mDependents == null) {
                constraintAnchor3.mDependents = new HashSet<>();
            }
            this.mTarget.mDependents.add(this);
        }
        this.mMargin = source.mMargin;
        this.mGoneMargin = source.mGoneMargin;
    }

    public void findDependents(int orientation, ArrayList<WidgetGroup> arrayList, WidgetGroup group) {
        HashSet<ConstraintAnchor> hashSet = this.mDependents;
        if (hashSet != null) {
            Iterator<ConstraintAnchor> it = hashSet.iterator();
            while (it.hasNext()) {
                Grouping.findDependents(it.next().mOwner, orientation, arrayList, group);
            }
        }
    }

    public HashSet<ConstraintAnchor> getDependents() {
        return this.mDependents;
    }

    public int getFinalValue() {
        if (!this.mHasFinalValue) {
            return 0;
        }
        return this.mFinalValue;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0012, code lost:
        r0 = r3.mTarget;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getMargin() {
        /*
            r3 = this;
            androidx.constraintlayout.core.widgets.ConstraintWidget r0 = r3.mOwner
            int r0 = r0.getVisibility()
            r1 = 8
            if (r0 != r1) goto L_0x000c
            r0 = 0
            return r0
        L_0x000c:
            int r0 = r3.mGoneMargin
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r0 == r2) goto L_0x0021
            androidx.constraintlayout.core.widgets.ConstraintAnchor r0 = r3.mTarget
            if (r0 == 0) goto L_0x0021
            androidx.constraintlayout.core.widgets.ConstraintWidget r0 = r0.mOwner
            int r0 = r0.getVisibility()
            if (r0 != r1) goto L_0x0021
            int r0 = r3.mGoneMargin
            return r0
        L_0x0021:
            int r0 = r3.mMargin
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.widgets.ConstraintAnchor.getMargin():int");
    }

    public final ConstraintAnchor getOpposite() {
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            case 1:
            case 6:
            case 7:
            case 8:
            case 9:
                return null;
            case 2:
                return this.mOwner.mRight;
            case 3:
                return this.mOwner.mLeft;
            case 4:
                return this.mOwner.mBottom;
            case 5:
                return this.mOwner.mTop;
            default:
                throw new AssertionError(this.mType.name());
        }
    }

    public ConstraintWidget getOwner() {
        return this.mOwner;
    }

    public SolverVariable getSolverVariable() {
        return this.mSolverVariable;
    }

    public ConstraintAnchor getTarget() {
        return this.mTarget;
    }

    public Type getType() {
        return this.mType;
    }

    public boolean hasCenteredDependents() {
        HashSet<ConstraintAnchor> hashSet = this.mDependents;
        if (hashSet == null) {
            return false;
        }
        Iterator<ConstraintAnchor> it = hashSet.iterator();
        while (it.hasNext()) {
            if (it.next().getOpposite().isConnected()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasDependents() {
        HashSet<ConstraintAnchor> hashSet = this.mDependents;
        return hashSet != null && hashSet.size() > 0;
    }

    public boolean hasFinalValue() {
        return this.mHasFinalValue;
    }

    public boolean isConnected() {
        return this.mTarget != null;
    }

    public boolean isConnectionAllowed(ConstraintWidget target) {
        if (isConnectionToMe(target, new HashSet())) {
            return false;
        }
        ConstraintWidget parent = getOwner().getParent();
        return parent == target || target.getParent() == parent;
    }

    public boolean isConnectionAllowed(ConstraintWidget target, ConstraintAnchor anchor) {
        return isConnectionAllowed(target);
    }

    public boolean isSideAnchor() {
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            case 1:
            case 6:
            case 7:
            case 8:
            case 9:
                return false;
            case 2:
            case 3:
            case 4:
            case 5:
                return true;
            default:
                throw new AssertionError(this.mType.name());
        }
    }

    public boolean isSimilarDimensionConnection(ConstraintAnchor anchor) {
        Type type = anchor.getType();
        if (type == this.mType) {
            return true;
        }
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            case 1:
                return type != Type.BASELINE;
            case 2:
            case 3:
            case 7:
                return type == Type.LEFT || type == Type.RIGHT || type == Type.CENTER_X;
            case 4:
            case 5:
            case 6:
            case 8:
                return type == Type.TOP || type == Type.BOTTOM || type == Type.CENTER_Y || type == Type.BASELINE;
            case 9:
                return false;
            default:
                throw new AssertionError(this.mType.name());
        }
    }

    public boolean isValidConnection(ConstraintAnchor anchor) {
        boolean z = false;
        if (anchor == null) {
            return false;
        }
        Type type = anchor.getType();
        Type type2 = this.mType;
        if (type == type2) {
            return type2 != Type.BASELINE || (anchor.getOwner().hasBaseline() && getOwner().hasBaseline());
        }
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            case 1:
                return (type == Type.BASELINE || type == Type.CENTER_X || type == Type.CENTER_Y) ? false : true;
            case 2:
            case 3:
                boolean z2 = type == Type.LEFT || type == Type.RIGHT;
                if (!(anchor.getOwner() instanceof Guideline)) {
                    return z2;
                }
                if (z2 || type == Type.CENTER_X) {
                    z = true;
                }
                return z;
            case 4:
            case 5:
                boolean z3 = type == Type.TOP || type == Type.BOTTOM;
                if (!(anchor.getOwner() instanceof Guideline)) {
                    return z3;
                }
                if (z3 || type == Type.CENTER_Y) {
                    z = true;
                }
                return z;
            case 6:
                return (type == Type.LEFT || type == Type.RIGHT) ? false : true;
            case 7:
            case 8:
            case 9:
                return false;
            default:
                throw new AssertionError(this.mType.name());
        }
    }

    public boolean isVerticalAnchor() {
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 7:
                return false;
            case 4:
            case 5:
            case 6:
            case 8:
            case 9:
                return true;
            default:
                throw new AssertionError(this.mType.name());
        }
    }

    public void reset() {
        HashSet<ConstraintAnchor> hashSet;
        ConstraintAnchor constraintAnchor = this.mTarget;
        if (!(constraintAnchor == null || (hashSet = constraintAnchor.mDependents) == null)) {
            hashSet.remove(this);
            if (this.mTarget.mDependents.size() == 0) {
                this.mTarget.mDependents = null;
            }
        }
        this.mDependents = null;
        this.mTarget = null;
        this.mMargin = 0;
        this.mGoneMargin = Integer.MIN_VALUE;
        this.mHasFinalValue = false;
        this.mFinalValue = 0;
    }

    public void resetFinalResolution() {
        this.mHasFinalValue = false;
        this.mFinalValue = 0;
    }

    public void resetSolverVariable(Cache cache) {
        SolverVariable solverVariable = this.mSolverVariable;
        if (solverVariable == null) {
            this.mSolverVariable = new SolverVariable(SolverVariable.Type.UNRESTRICTED, (String) null);
        } else {
            solverVariable.reset();
        }
    }

    public void setFinalValue(int finalValue) {
        this.mFinalValue = finalValue;
        this.mHasFinalValue = true;
    }

    public void setGoneMargin(int margin) {
        if (isConnected()) {
            this.mGoneMargin = margin;
        }
    }

    public void setMargin(int margin) {
        if (isConnected()) {
            this.mMargin = margin;
        }
    }

    public String toString() {
        return this.mOwner.getDebugName() + ":" + this.mType.toString();
    }
}
