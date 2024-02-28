package androidx.work;

import androidx.lifecycle.LiveData;
import com.google.common.util.concurrent.ListenableFuture;
import mt.Log1F380D;

public interface Operation {
    public static final State.IN_PROGRESS IN_PROGRESS = new State.IN_PROGRESS();
    public static final State.SUCCESS SUCCESS = new State.SUCCESS();

    public static abstract class State {

        /* compiled from: 00A7 */
        public static final class FAILURE extends State {
            private final Throwable mThrowable;

            public FAILURE(Throwable exception) {
                this.mThrowable = exception;
            }

            public Throwable getThrowable() {
                return this.mThrowable;
            }

            public String toString() {
                String format = String.format("FAILURE (%s)", new Object[]{this.mThrowable.getMessage()});
                Log1F380D.a((Object) format);
                return format;
            }
        }

        public static final class IN_PROGRESS extends State {
            private IN_PROGRESS() {
            }

            public String toString() {
                return "IN_PROGRESS";
            }
        }

        public static final class SUCCESS extends State {
            private SUCCESS() {
            }

            public String toString() {
                return "SUCCESS";
            }
        }

        State() {
        }
    }

    ListenableFuture<State.SUCCESS> getResult();

    LiveData<State> getState();
}
