package androidx.lifecycle;

import java.util.HashMap;
import java.util.Map;

public class MethodCallsLogger {
    private Map<String, Integer> mCalledMethods = new HashMap();

    public boolean approveCall(String name, int type) {
        Integer num = this.mCalledMethods.get(name);
        int intValue = num != null ? num.intValue() : 0;
        boolean z = (intValue & type) != 0;
        this.mCalledMethods.put(name, Integer.valueOf(intValue | type));
        return !z;
    }
}
