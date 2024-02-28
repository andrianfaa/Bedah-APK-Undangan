package androidx.transition;

import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import mt.Log1F380D;

/* compiled from: 0096 */
public class TransitionValues {
    final ArrayList<Transition> mTargetedTransitions = new ArrayList<>();
    public final Map<String, Object> values = new HashMap();
    public View view;

    @Deprecated
    public TransitionValues() {
    }

    public TransitionValues(View view2) {
        this.view = view2;
    }

    public boolean equals(Object other) {
        return (other instanceof TransitionValues) && this.view == ((TransitionValues) other).view && this.values.equals(((TransitionValues) other).values);
    }

    public int hashCode() {
        return (this.view.hashCode() * 31) + this.values.hashCode();
    }

    public String toString() {
        StringBuilder append = new StringBuilder().append("TransitionValues@");
        String hexString = Integer.toHexString(hashCode());
        Log1F380D.a((Object) hexString);
        String str = (append.append(hexString).append(":\n").toString() + "    view = " + this.view + "\n") + "    values:";
        for (String next : this.values.keySet()) {
            str = str + "    " + next + ": " + this.values.get(next) + "\n";
        }
        return str;
    }
}
