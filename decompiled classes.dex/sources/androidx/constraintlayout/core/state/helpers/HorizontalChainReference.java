package androidx.constraintlayout.core.state.helpers;

import androidx.constraintlayout.core.state.ConstraintReference;
import androidx.constraintlayout.core.state.State;
import java.util.Iterator;

public class HorizontalChainReference extends ChainReference {

    /* renamed from: androidx.constraintlayout.core.state.helpers.HorizontalChainReference$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$core$state$State$Chain;

        static {
            int[] iArr = new int[State.Chain.values().length];
            $SwitchMap$androidx$constraintlayout$core$state$State$Chain = iArr;
            try {
                iArr[State.Chain.SPREAD.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Chain[State.Chain.SPREAD_INSIDE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Chain[State.Chain.PACKED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public HorizontalChainReference(State state) {
        super(state, State.Helper.HORIZONTAL_CHAIN);
    }

    public void apply() {
        ConstraintReference constraintReference = null;
        ConstraintReference constraintReference2 = null;
        Iterator it = this.mReferences.iterator();
        while (it.hasNext()) {
            this.mState.constraints(it.next()).clearHorizontal();
        }
        Iterator it2 = this.mReferences.iterator();
        while (it2.hasNext()) {
            ConstraintReference constraints = this.mState.constraints(it2.next());
            if (constraintReference == null) {
                constraintReference = constraints;
                if (this.mStartToStart != null) {
                    constraintReference.startToStart(this.mStartToStart).margin(this.mMarginStart).marginGone(this.mMarginStartGone);
                } else if (this.mStartToEnd != null) {
                    constraintReference.startToEnd(this.mStartToEnd).margin(this.mMarginStart).marginGone(this.mMarginStartGone);
                } else if (this.mLeftToLeft != null) {
                    constraintReference.startToStart(this.mLeftToLeft).margin(this.mMarginLeft).marginGone(this.mMarginLeftGone);
                } else if (this.mLeftToRight != null) {
                    constraintReference.startToEnd(this.mLeftToRight).margin(this.mMarginLeft).marginGone(this.mMarginLeftGone);
                } else {
                    constraintReference.startToStart(State.PARENT);
                }
            }
            if (constraintReference2 != null) {
                constraintReference2.endToStart(constraints.getKey());
                constraints.startToEnd(constraintReference2.getKey());
            }
            constraintReference2 = constraints;
        }
        if (constraintReference2 != null) {
            if (this.mEndToStart != null) {
                constraintReference2.endToStart(this.mEndToStart).margin(this.mMarginEnd).marginGone(this.mMarginEndGone);
            } else if (this.mEndToEnd != null) {
                constraintReference2.endToEnd(this.mEndToEnd).margin(this.mMarginEnd).marginGone(this.mMarginEndGone);
            } else if (this.mRightToLeft != null) {
                constraintReference2.endToStart(this.mRightToLeft).margin(this.mMarginRight).marginGone(this.mMarginRightGone);
            } else if (this.mRightToRight != null) {
                constraintReference2.endToEnd(this.mRightToRight).margin(this.mMarginRight).marginGone(this.mMarginRightGone);
            } else {
                constraintReference2.endToEnd(State.PARENT);
            }
        }
        if (constraintReference != null) {
            if (this.mBias != 0.5f) {
                constraintReference.horizontalBias(this.mBias);
            }
            switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$state$State$Chain[this.mStyle.ordinal()]) {
                case 1:
                    constraintReference.setHorizontalChainStyle(0);
                    return;
                case 2:
                    constraintReference.setHorizontalChainStyle(1);
                    return;
                case 3:
                    constraintReference.setHorizontalChainStyle(2);
                    return;
                default:
                    return;
            }
        }
    }
}
