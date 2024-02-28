package androidx.constraintlayout.core.state.helpers;

import androidx.constraintlayout.core.state.ConstraintReference;
import androidx.constraintlayout.core.state.HelperReference;
import androidx.constraintlayout.core.state.State;
import java.util.Iterator;

public class AlignHorizontallyReference extends HelperReference {
    private float mBias = 0.5f;

    public AlignHorizontallyReference(State state) {
        super(state, State.Helper.ALIGN_VERTICALLY);
    }

    public void apply() {
        Iterator it = this.mReferences.iterator();
        while (it.hasNext()) {
            ConstraintReference constraints = this.mState.constraints(it.next());
            constraints.clearHorizontal();
            if (this.mStartToStart != null) {
                constraints.startToStart(this.mStartToStart);
            } else if (this.mStartToEnd != null) {
                constraints.startToEnd(this.mStartToEnd);
            } else {
                constraints.startToStart(State.PARENT);
            }
            if (this.mEndToStart != null) {
                constraints.endToStart(this.mEndToStart);
            } else if (this.mEndToEnd != null) {
                constraints.endToEnd(this.mEndToEnd);
            } else {
                constraints.endToEnd(State.PARENT);
            }
            float f = this.mBias;
            if (f != 0.5f) {
                constraints.horizontalBias(f);
            }
        }
    }
}
