package androidx.core.database;

import android.text.TextUtils;

@Deprecated
public final class DatabaseUtilsCompat {
    private DatabaseUtilsCompat() {
    }

    @Deprecated
    public static String[] appendSelectionArgs(String[] originalValues, String[] newValues) {
        if (originalValues == null || originalValues.length == 0) {
            return newValues;
        }
        String[] strArr = new String[(originalValues.length + newValues.length)];
        System.arraycopy(originalValues, 0, strArr, 0, originalValues.length);
        System.arraycopy(newValues, 0, strArr, originalValues.length, newValues.length);
        return strArr;
    }

    @Deprecated
    public static String concatenateWhere(String a, String b) {
        return TextUtils.isEmpty(a) ? b : TextUtils.isEmpty(b) ? a : "(" + a + ") AND (" + b + ")";
    }
}
