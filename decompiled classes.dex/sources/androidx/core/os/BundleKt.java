package androidx.core.os;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;
import java.io.Serializable;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Typography;

@Metadata(d1 = {"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\b\u0002\u001a\u0006\u0010\u0000\u001a\u00020\u0001\u001a;\u0010\u0000\u001a\u00020\u00012.\u0010\u0002\u001a\u0018\u0012\u0014\b\u0001\u0012\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u00040\u0003\"\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u0004¢\u0006\u0002\u0010\u0007¨\u0006\b"}, d2 = {"bundleOf", "Landroid/os/Bundle;", "pairs", "", "Lkotlin/Pair;", "", "", "([Lkotlin/Pair;)Landroid/os/Bundle;", "core-ktx_release"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: Bundle.kt */
public final class BundleKt {
    public static final Bundle bundleOf() {
        return new Bundle(0);
    }

    public static final Bundle bundleOf(Pair<String, ? extends Object>... pairs) {
        Intrinsics.checkNotNullParameter(pairs, "pairs");
        Bundle bundle = new Bundle(pairs.length);
        Bundle bundle2 = bundle;
        for (Pair<String, ? extends Object> pair : pairs) {
            String component1 = pair.component1();
            Object component2 = pair.component2();
            if (component2 == null) {
                bundle2.putString(component1, (String) null);
            } else if (component2 instanceof Boolean) {
                bundle2.putBoolean(component1, ((Boolean) component2).booleanValue());
            } else if (component2 instanceof Byte) {
                bundle2.putByte(component1, ((Number) component2).byteValue());
            } else if (component2 instanceof Character) {
                bundle2.putChar(component1, ((Character) component2).charValue());
            } else if (component2 instanceof Double) {
                bundle2.putDouble(component1, ((Number) component2).doubleValue());
            } else if (component2 instanceof Float) {
                bundle2.putFloat(component1, ((Number) component2).floatValue());
            } else if (component2 instanceof Integer) {
                bundle2.putInt(component1, ((Number) component2).intValue());
            } else if (component2 instanceof Long) {
                bundle2.putLong(component1, ((Number) component2).longValue());
            } else if (component2 instanceof Short) {
                bundle2.putShort(component1, ((Number) component2).shortValue());
            } else if (component2 instanceof Bundle) {
                bundle2.putBundle(component1, (Bundle) component2);
            } else if (component2 instanceof CharSequence) {
                bundle2.putCharSequence(component1, (CharSequence) component2);
            } else if (component2 instanceof Parcelable) {
                bundle2.putParcelable(component1, (Parcelable) component2);
            } else if (component2 instanceof boolean[]) {
                bundle2.putBooleanArray(component1, (boolean[]) component2);
            } else if (component2 instanceof byte[]) {
                bundle2.putByteArray(component1, (byte[]) component2);
            } else if (component2 instanceof char[]) {
                bundle2.putCharArray(component1, (char[]) component2);
            } else if (component2 instanceof double[]) {
                bundle2.putDoubleArray(component1, (double[]) component2);
            } else if (component2 instanceof float[]) {
                bundle2.putFloatArray(component1, (float[]) component2);
            } else if (component2 instanceof int[]) {
                bundle2.putIntArray(component1, (int[]) component2);
            } else if (component2 instanceof long[]) {
                bundle2.putLongArray(component1, (long[]) component2);
            } else if (component2 instanceof short[]) {
                bundle2.putShortArray(component1, (short[]) component2);
            } else if (component2 instanceof Object[]) {
                Class<?> componentType = component2.getClass().getComponentType();
                Intrinsics.checkNotNull(componentType);
                if (Parcelable.class.isAssignableFrom(componentType)) {
                    if (component2 != null) {
                        bundle2.putParcelableArray(component1, (Parcelable[]) component2);
                    } else {
                        throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<android.os.Parcelable>");
                    }
                } else if (String.class.isAssignableFrom(componentType)) {
                    if (component2 != null) {
                        bundle2.putStringArray(component1, (String[]) component2);
                    } else {
                        throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<kotlin.String>");
                    }
                } else if (CharSequence.class.isAssignableFrom(componentType)) {
                    if (component2 != null) {
                        bundle2.putCharSequenceArray(component1, (CharSequence[]) component2);
                    } else {
                        throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<kotlin.CharSequence>");
                    }
                } else if (Serializable.class.isAssignableFrom(componentType)) {
                    bundle2.putSerializable(component1, (Serializable) component2);
                } else {
                    throw new IllegalArgumentException("Illegal value array type " + componentType.getCanonicalName() + " for key \"" + component1 + Typography.quote);
                }
            } else if (component2 instanceof Serializable) {
                bundle2.putSerializable(component1, (Serializable) component2);
            } else if (Build.VERSION.SDK_INT >= 18 && (component2 instanceof IBinder)) {
                BundleApi18ImplKt.putBinder(bundle2, component1, (IBinder) component2);
            } else if (Build.VERSION.SDK_INT >= 21 && (component2 instanceof Size)) {
                BundleApi21ImplKt.putSize(bundle2, component1, (Size) component2);
            } else if (Build.VERSION.SDK_INT < 21 || !(component2 instanceof SizeF)) {
                throw new IllegalArgumentException("Illegal value type " + component2.getClass().getCanonicalName() + " for key \"" + component1 + Typography.quote);
            } else {
                BundleApi21ImplKt.putSizeF(bundle2, component1, (SizeF) component2);
            }
        }
        return bundle;
    }
}