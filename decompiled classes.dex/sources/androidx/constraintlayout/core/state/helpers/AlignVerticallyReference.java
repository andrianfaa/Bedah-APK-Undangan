package androidx.constraintlayout.core.state.helpers;

import androidx.constraintlayout.core.state.ConstraintReference;
import androidx.constraintlayout.core.state.HelperReference;
import androidx.constraintlayout.core.state.State;
import java.util.Iterator;

public class AlignVerticallyReference extends HelperReference {
    private float mBias = 0.5f;

    public AlignVerticallyReference(State state) {
        super(state, State.Helper.ALIGN_VERTICALLY);
    }

    public void apply() {
        Iterator it = this.mReferences.iterator();
        while (it.hasNext()) {
            ConstraintReference constraints = this.mState.constraints(it.next());
            constraints.clearVertical();
            if (this.mTopToTop != null) {
                constraints.topToTop(this.mTopToTop);
            } else if (this.mTopToBottom != null) {
                constraints.topToBottom(this.mTopToBottom);
            } else {
                constraints.topToTop(State.PARENT);
            }
            if (this.mBottomToTop != null) {
                constraints.bottomToTop(this.mBottomToTop);
            } else if (this.mBottomToBottom != null) {
                constraints.bottomToBottom(this.mBottomToBottom);
            } else {
                constraints.bottomToBottom(State.PARENT);
            }
            float f = this.mBias;
            if (f != 0.5f) {
                constraints.verticalBias(f);
            }
        }
    }
}
