package androidx.room.util;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import mt.Log1F380D;
import okhttp3.HttpUrl;

/* compiled from: 0091 */
public class StringUtil {
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    private StringUtil() {
    }

    public static void appendPlaceholders(StringBuilder builder, int count) {
        for (int i = 0; i < count; i++) {
            builder.append("?");
            if (i < count - 1) {
                builder.append(",");
            }
        }
    }

    public static String joinIntoString(List<Integer> list) {
        if (list == null) {
            return null;
        }
        int size = list.size();
        if (size == 0) {
            return HttpUrl.FRAGMENT_ENCODE_SET;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            String num = Integer.toString(list.get(i).intValue());
            Log1F380D.a((Object) num);
            sb.append(num);
            if (i < size - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public static StringBuilder newStringBuilder() {
        return new StringBuilder();
    }

    public static List<Integer> splitToIntList(String input) {
        if (input == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        StringTokenizer stringTokenizer = new StringTokenizer(input, ",");
        while (stringTokenizer.hasMoreElements()) {
            try {
                arrayList.add(Integer.valueOf(Integer.parseInt(stringTokenizer.nextToken())));
            } catch (NumberFormatException e) {
                Log.e("ROOM", "Malformed integer list", e);
            }
        }
        return arrayList;
    }
}
