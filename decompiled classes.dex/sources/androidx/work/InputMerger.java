package androidx.work;

import java.util.List;
import mt.Log1F380D;

/* compiled from: 00A2 */
public abstract class InputMerger {
    private static final String TAG;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("InputMerger");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public static InputMerger fromClassName(String className) {
        try {
            return (InputMerger) Class.forName(className).newInstance();
        } catch (Exception e) {
            Logger.get().error(TAG, "Trouble instantiating + " + className, e);
            return null;
        }
    }

    public abstract Data merge(List<Data> list);
}
