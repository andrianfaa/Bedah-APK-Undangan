package kotlinx.coroutines.internal;

import kotlin.Metadata;
import mt.Log1F380D;

@Metadata(d1 = {"kotlinx/coroutines/internal/SystemPropsKt__SystemPropsKt", "kotlinx/coroutines/internal/SystemPropsKt__SystemProps_commonKt"}, k = 4, mv = {1, 6, 0}, xi = 48)
/* compiled from: 01A6 */
public final class SystemPropsKt {
    public static final int getAVAILABLE_PROCESSORS() {
        return SystemPropsKt__SystemPropsKt.getAVAILABLE_PROCESSORS();
    }

    public static final int systemProp(String propertyName, int defaultValue, int minValue, int maxValue) {
        return SystemPropsKt__SystemProps_commonKt.systemProp(propertyName, defaultValue, minValue, maxValue);
    }

    public static final long systemProp(String propertyName, long defaultValue, long minValue, long maxValue) {
        return SystemPropsKt__SystemProps_commonKt.systemProp(propertyName, defaultValue, minValue, maxValue);
    }

    public static final String systemProp(String propertyName) {
        String systemProp = SystemPropsKt__SystemPropsKt.systemProp(propertyName);
        Log1F380D.a((Object) systemProp);
        return systemProp;
    }

    public static final boolean systemProp(String propertyName, boolean defaultValue) {
        return SystemPropsKt__SystemProps_commonKt.systemProp(propertyName, defaultValue);
    }
}
