package androidx.core.os;

import android.os.Build;
import android.os.PersistableBundle;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Typography;

@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\bÃ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J$\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00042\b\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u0001H\u0007¨\u0006\r"}, d2 = {"Landroidx/core/os/PersistableBundleApi21ImplKt;", "", "()V", "createPersistableBundle", "Landroid/os/PersistableBundle;", "capacity", "", "putValue", "", "persistableBundle", "key", "", "value", "core-ktx_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: PersistableBundle.kt */
final class PersistableBundleApi21ImplKt {
    public static final PersistableBundleApi21ImplKt INSTANCE = new PersistableBundleApi21ImplKt();

    private PersistableBundleApi21ImplKt() {
    }

    @JvmStatic
    public static final PersistableBundle createPersistableBundle(int capacity) {
        return new PersistableBundle(capacity);
    }

    @JvmStatic
    public static final void putValue(PersistableBundle persistableBundle, String key, Object value) {
        Intrinsics.checkNotNullParameter(persistableBundle, "persistableBundle");
        PersistableBundle persistableBundle2 = persistableBundle;
        if (value == null) {
            persistableBundle2.putString(key, (String) null);
        } else if (value instanceof Boolean) {
            if (Build.VERSION.SDK_INT >= 22) {
                PersistableBundleApi22ImplKt.putBoolean(persistableBundle2, key, ((Boolean) value).booleanValue());
                return;
            }
            throw new IllegalArgumentException("Illegal value type boolean for key \"" + key + Typography.quote);
        } else if (value instanceof Double) {
            persistableBundle2.putDouble(key, ((Number) value).doubleValue());
        } else if (value instanceof Integer) {
            persistableBundle2.putInt(key, ((Number) value).intValue());
        } else if (value instanceof Long) {
            persistableBundle2.putLong(key, ((Number) value).longValue());
        } else if (value instanceof String) {
            persistableBundle2.putString(key, (String) value);
        } else if (value instanceof boolean[]) {
            if (Build.VERSION.SDK_INT >= 22) {
                PersistableBundleApi22ImplKt.putBooleanArray(persistableBundle2, key, (boolean[]) value);
                return;
            }
            throw new IllegalArgumentException("Illegal value type boolean[] for key \"" + key + Typography.quote);
        } else if (value instanceof double[]) {
            persistableBundle2.putDoubleArray(key, (double[]) value);
        } else if (value instanceof int[]) {
            persistableBundle2.putIntArray(key, (int[]) value);
        } else if (value instanceof long[]) {
            persistableBundle2.putLongArray(key, (long[]) value);
        } else if (value instanceof Object[]) {
            Class<?> componentType = value.getClass().getComponentType();
            Intrinsics.checkNotNull(componentType);
            if (!String.class.isAssignableFrom(componentType)) {
                throw new IllegalArgumentException("Illegal value array type " + componentType.getCanonicalName() + " for key \"" + key + Typography.quote);
            } else if (value != null) {
                persistableBundle2.putStringArray(key, (String[]) value);
            } else {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<kotlin.String>");
            }
        } else {
            throw new IllegalArgumentException("Illegal value type " + value.getClass().getCanonicalName() + " for key \"" + key + Typography.quote);
        }
    }
}
