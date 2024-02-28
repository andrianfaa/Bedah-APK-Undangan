package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import mt.Log1F380D;
import okhttp3.HttpUrl;

/* compiled from: 0019 */
public class Debug {
    public static void dumpLayoutParams(ViewGroup.LayoutParams param, String str) {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        String str2 = ".(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ") " + str + "  ";
        System.out.println(" >>>>>>>>>>>>>>>>>>. dump " + str2 + "  " + param.getClass().getName());
        Field[] fields = param.getClass().getFields();
        for (Field field : fields) {
            try {
                Object obj = field.get(param);
                String name = field.getName();
                if (name.contains("To")) {
                    if (!obj.toString().equals("-1")) {
                        System.out.println(str2 + "       " + name + " " + obj);
                    }
                }
            } catch (IllegalAccessException e) {
            }
        }
        System.out.println(" <<<<<<<<<<<<<<<<< dump " + str2);
    }

    public static void dumpLayoutParams(ViewGroup layout, String str) {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        String str2 = ".(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ") " + str + "  ";
        int childCount = layout.getChildCount();
        System.out.println(str + " children " + childCount);
        for (int i = 0; i < childCount; i++) {
            View childAt = layout.getChildAt(i);
            PrintStream printStream = System.out;
            StringBuilder append = new StringBuilder().append(str2).append("     ");
            String name = getName(childAt);
            Log1F380D.a((Object) name);
            printStream.println(append.append(name).toString());
            ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
            Field[] fields = layoutParams.getClass().getFields();
            for (Field field : fields) {
                try {
                    Object obj = field.get(layoutParams);
                    if (field.getName().contains("To")) {
                        if (!obj.toString().equals("-1")) {
                            System.out.println(str2 + "       " + field.getName() + " " + obj);
                        }
                    }
                } catch (IllegalAccessException e) {
                }
            }
        }
    }

    public static void dumpPoc(Object obj) {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        String str = ".(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ")";
        Class<?> cls = obj.getClass();
        System.out.println(str + "------------- " + cls.getName() + " --------------------");
        Field[] fields = cls.getFields();
        for (Field field : fields) {
            try {
                Object obj2 = field.get(obj);
                if (field.getName().startsWith("layout_constraint")) {
                    if (!(obj2 instanceof Integer) || !obj2.toString().equals("-1")) {
                        if (!(obj2 instanceof Integer) || !obj2.toString().equals("0")) {
                            if (!(obj2 instanceof Float) || !obj2.toString().equals("1.0")) {
                                if (!(obj2 instanceof Float) || !obj2.toString().equals("0.5")) {
                                    System.out.println(str + "    " + field.getName() + " " + obj2);
                                }
                            }
                        }
                    }
                }
            } catch (IllegalAccessException e) {
            }
        }
        System.out.println(str + "------------- " + cls.getSimpleName() + " --------------------");
    }

    public static String getActionType(MotionEvent event) {
        int action = event.getAction();
        Field[] fields = MotionEvent.class.getFields();
        for (Field field : fields) {
            try {
                if (Modifier.isStatic(field.getModifiers()) && field.getType().equals(Integer.TYPE) && field.getInt((Object) null) == action) {
                    return field.getName();
                }
            } catch (IllegalAccessException e) {
            }
        }
        return "---";
    }

    public static String getCallFrom(int n) {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[n + 2];
        return ".(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ")";
    }

    public static String getLoc() {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        return ".(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ") " + stackTraceElement.getMethodName() + "()";
    }

    public static String getLocation() {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        return ".(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ")";
    }

    public static String getLocation2() {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[2];
        return ".(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ")";
    }

    public static String getName(Context context, int id) {
        if (id == -1) {
            return "UNKNOWN";
        }
        try {
            return context.getResources().getResourceEntryName(id);
        } catch (Exception e) {
            return "?" + id;
        }
    }

    public static String getName(Context context, int[] id) {
        String str;
        try {
            String str2 = id.length + "[";
            int i = 0;
            while (i < id.length) {
                String str3 = str2 + (i == 0 ? HttpUrl.FRAGMENT_ENCODE_SET : " ");
                try {
                    str = context.getResources().getResourceEntryName(id[i]);
                } catch (Resources.NotFoundException e) {
                    str = "? " + id[i] + " ";
                }
                str2 = str3 + str;
                i++;
            }
            return str2 + "]";
        } catch (Exception e2) {
            Log.v("DEBUG", e2.toString());
            return "UNKNOWN";
        }
    }

    public static String getName(View view) {
        try {
            return view.getContext().getResources().getResourceEntryName(view.getId());
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0028, code lost:
        r0 = r2.replaceAll("[^_]", okhttp3.HttpUrl.FRAGMENT_ENCODE_SET).length();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getState(androidx.constraintlayout.motion.widget.MotionLayout r8, int r9, int r10) {
        /*
            r0 = -1
            if (r9 != r0) goto L_0x0006
            java.lang.String r0 = "UNDEFINED"
            return r0
        L_0x0006:
            android.content.Context r1 = r8.getContext()
            android.content.res.Resources r2 = r1.getResources()
            java.lang.String r2 = r2.getResourceEntryName(r9)
            if (r10 == r0) goto L_0x0062
            int r0 = r2.length()
            if (r0 <= r10) goto L_0x0022
            java.lang.String r0 = "([^_])[aeiou]+"
            java.lang.String r3 = "$1"
            java.lang.String r2 = r2.replaceAll(r0, r3)
        L_0x0022:
            int r0 = r2.length()
            if (r0 <= r10) goto L_0x0062
            java.lang.String r0 = "[^_]"
            java.lang.String r3 = ""
            java.lang.String r0 = r2.replaceAll(r0, r3)
            int r0 = r0.length()
            if (r0 <= 0) goto L_0x0062
            int r3 = r2.length()
            int r3 = r3 - r10
            int r3 = r3 / r0
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.nio.CharBuffer r5 = java.nio.CharBuffer.allocate(r3)
            java.lang.String r5 = r5.toString()
            r6 = 0
            r7 = 46
            java.lang.String r5 = r5.replace(r6, r7)
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = "_"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            java.lang.String r2 = r2.replaceAll(r4, r5)
        L_0x0062:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.Debug.getState(androidx.constraintlayout.motion.widget.MotionLayout, int, int):java.lang.String");
    }

    public static void logStack(String tag, String msg, int n) {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        String str = " ";
        int n2 = Math.min(n, stackTrace.length - 1);
        for (int i = 1; i <= n2; i++) {
            StackTraceElement stackTraceElement = stackTrace[i];
            str = str + " ";
            Log.v(tag, msg + str + (".(" + stackTrace[i].getFileName() + ":" + stackTrace[i].getLineNumber() + ") " + stackTrace[i].getMethodName()) + str);
        }
    }

    public static void printStack(String msg, int n) {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        String str = " ";
        int n2 = Math.min(n, stackTrace.length - 1);
        for (int i = 1; i <= n2; i++) {
            StackTraceElement stackTraceElement = stackTrace[i];
            str = str + " ";
            System.out.println(msg + str + (".(" + stackTrace[i].getFileName() + ":" + stackTrace[i].getLineNumber() + ") ") + str);
        }
    }

    public static String getState(MotionLayout layout, int stateId) {
        String state = getState(layout, stateId, -1);
        Log1F380D.a((Object) state);
        return state;
    }
}
