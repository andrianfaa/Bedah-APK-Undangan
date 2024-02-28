package androidx.constraintlayout.core.state.helpers;

import androidx.constraintlayout.core.state.ConstraintReference;
import androidx.constraintlayout.core.state.State;
import java.util.Iterator;

public class VerticalChainReference extends ChainReference {

    /* renamed from: androidx.constraintlayout.core.state.helpers.VerticalChainReference$1  reason: invalid class name */
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

    public VerticalChainReference(State state) {
        super(state, State.Helper.VERTICAL_CHAIN);
    }

    public void apply() {
        ConstraintReference constraintReference = null;
        ConstraintReference constraintReference2 = null;
        Iterator it = this.mReferences.iterator();
        while (it.hasNext()) {
            this.mState.constraints(it.next()).clearVertical();
        }
        Iterator it2 = this.mReferences.iterator();
        while (it2.hasNext()) {
            ConstraintReference constraints = this.mState.constraints(it2.next());
            if (constraintReference == null) {
                constraintReference = constraints;
                if (this.mTopToTop != null) {
                    constraintReference.topToTop(this.mTopToTop).margin(this.mMarginTop).marginGone(this.mMarginTopGone);
                } else if (this.mTopToBottom != null) {
                    constraintReference.topToBottom(this.mTopToBottom).margin(this.mMarginTop).marginGone(this.mMarginTopGone);
                } else {
                    constraintReference.topToTop(State.PARENT);
                }
            }
            if (constraintReference2 != null) {
                constraintReference2.bottomToTop(constraints.getKey());
                constraints.topToBottom(constraintReference2.getKey());
            }
            constraintReference2 = constraints;
        }
        if (constraintReference2 != null) {
            if (this.mBottomToTop != null) {
                constraintReference2.bottomToTop(this.mBottomToTop).margin(this.mMarginBottom).marginGone(this.mMarginBottomGone);
            } else if (this.mBottomToBottom != null) {
                constraintReference2.bottomToBottom(this.mBottomToBottom).margin(this.mMarginBottom).marginGone(this.mMarginBottomGone);
            } else {
                constraintReference2.bottomToBottom(State.PARENT);
            }
        }
        if (constraintReference != null) {
            if (this.mBias != 0.5f) {
                constraintReference.verticalBias(this.mBias);
            }
            switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$state$State$Chain[this.mStyle.ordinal()]) {
                case 1:
                    constraintReference.setVerticalChainStyle(0);
                    return;
                case 2:
                    constraintReference.setVerticalChainStyle(1);
                    return;
                case 3:
                    constraintReference.setVerticalChainStyle(2);
                    return;
                default:
                    return;
            }
        }
    }
}
