package androidx.core.content;

import android.content.ContentValues;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Typography;

@Metadata(d1 = {"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\b\u0002\u001a;\u0010\u0000\u001a\u00020\u00012.\u0010\u0002\u001a\u0018\u0012\u0014\b\u0001\u0012\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u00040\u0003\"\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u0004¢\u0006\u0002\u0010\u0007¨\u0006\b"}, d2 = {"contentValuesOf", "Landroid/content/ContentValues;", "pairs", "", "Lkotlin/Pair;", "", "", "([Lkotlin/Pair;)Landroid/content/ContentValues;", "core-ktx_release"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: ContentValues.kt */
public final class ContentValuesKt {
    public static final ContentValues contentValuesOf(Pair<String, ? extends Object>... pairs) {
        Intrinsics.checkNotNullParameter(pairs, "pairs");
        ContentValues contentValues = new ContentValues(pairs.length);
        ContentValues contentValues2 = contentValues;
        for (Pair<String, ? extends Object> pair : pairs) {
            String component1 = pair.component1();
            Object component2 = pair.component2();
            if (component2 == null) {
                contentValues2.putNull(component1);
            } else if (component2 instanceof String) {
                contentValues2.put(component1, (String) component2);
            } else if (component2 instanceof Integer) {
                contentValues2.put(component1, (Integer) component2);
            } else if (component2 instanceof Long) {
                contentValues2.put(component1, (Long) component2);
            } else if (component2 instanceof Boolean) {
                contentValues2.put(component1, (Boolean) component2);
            } else if (component2 instanceof Float) {
                contentValues2.put(component1, (Float) component2);
            } else if (component2 instanceof Double) {
                contentValues2.put(component1, (Double) component2);
            } else if (component2 instanceof byte[]) {
                contentValues2.put(component1, (byte[]) component2);
            } else if (component2 instanceof Byte) {
                contentValues2.put(component1, (Byte) component2);
            } else if (component2 instanceof Short) {
                contentValues2.put(component1, (Short) component2);
            } else {
                throw new IllegalArgumentException("Illegal value type " + component2.getClass().getCanonicalName() + " for key \"" + component1 + Typography.quote);
            }
        }
        return contentValues;
    }
}
