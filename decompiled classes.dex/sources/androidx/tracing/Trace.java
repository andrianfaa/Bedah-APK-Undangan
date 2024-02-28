package androidx.tracing;

import android.os.Build;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class Trace {
    static final String TAG = "Trace";
    private static Method sAsyncTraceBeginMethod;
    private static Method sAsyncTraceEndMethod;
    private static Method sIsTagEnabledMethod;
    private static Method sTraceCounterMethod;
    private static long sTraceTagApp;

    private Trace() {
    }

    public static void beginAsyncSection(String methodName, int cookie) {
        try {
            if (sAsyncTraceBeginMethod == null) {
                TraceApi29Impl.beginAsyncSection(methodName, cookie);
                return;
            }
        } catch (NoClassDefFoundError | NoSuchMethodError e) {
        }
        beginAsyncSectionFallback(methodName, cookie);
    }

    private static void beginAsyncSectionFallback(String methodName, int cookie) {
        if (Build.VERSION.SDK_INT >= 18) {
            try {
                if (sAsyncTraceBeginMethod == null) {
                    sAsyncTraceBeginMethod = android.os.Trace.class.getMethod("asyncTraceBegin", new Class[]{Long.TYPE, String.class, Integer.TYPE});
                }
                sAsyncTraceBeginMethod.invoke((Object) null, new Object[]{Long.valueOf(sTraceTagApp), methodName, Integer.valueOf(cookie)});
            } catch (Exception e) {
                handleException("asyncTraceBegin", e);
            }
        }
    }

    public static void beginSection(String label) {
        if (Build.VERSION.SDK_INT >= 18) {
            TraceApi18Impl.beginSection(label);
        }
    }

    public static void endAsyncSection(String methodName, int cookie) {
        try {
            if (sAsyncTraceEndMethod == null) {
                TraceApi29Impl.endAsyncSection(methodName, cookie);
                return;
            }
        } catch (NoClassDefFoundError | NoSuchMethodError e) {
        }
        endAsyncSectionFallback(methodName, cookie);
    }

    private static void endAsyncSectionFallback(String methodName, int cookie) {
        if (Build.VERSION.SDK_INT >= 18) {
            try {
                if (sAsyncTraceEndMethod == null) {
                    sAsyncTraceEndMethod = android.os.Trace.class.getMethod("asyncTraceEnd", new Class[]{Long.TYPE, String.class, Integer.TYPE});
                }
                sAsyncTraceEndMethod.invoke((Object) null, new Object[]{Long.valueOf(sTraceTagApp), methodName, Integer.valueOf(cookie)});
            } catch (Exception e) {
                handleException("asyncTraceEnd", e);
            }
        }
    }

    public static void endSection() {
        if (Build.VERSION.SDK_INT >= 18) {
            TraceApi18Impl.endSection();
        }
    }

    private static void handleException(String methodName, Exception exception) {
        if (exception instanceof InvocationTargetException) {
            Throwable cause = exception.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            throw new RuntimeException(cause);
        }
        Log.v(TAG, "Unable to call " + methodName + " via reflection", exception);
    }

    public static boolean isEnabled() {
        try {
            if (sIsTagEnabledMethod == null) {
                return android.os.Trace.isEnabled();
            }
        } catch (NoClassDefFoundError | NoSuchMethodError e) {
        }
        return isEnabledFallback();
    }

    private static boolean isEnabledFallback() {
        if (Build.VERSION.SDK_INT >= 18) {
            try {
                if (sIsTagEnabledMethod == null) {
                    sTraceTagApp = android.os.Trace.class.getField("TRACE_TAG_APP").getLong((Object) null);
                    sIsTagEnabledMethod = android.os.Trace.class.getMethod("isTagEnabled", new Class[]{Long.TYPE});
                }
                return ((Boolean) sIsTagEnabledMethod.invoke((Object) null, new Object[]{Long.valueOf(sTraceTagApp)})).booleanValue();
            } catch (Exception e) {
                handleException("isTagEnabled", e);
            }
        }
        return false;
    }

    public static void setCounter(String counterName, int counterValue) {
        try {
            if (sTraceCounterMethod == null) {
                TraceApi29Impl.setCounter(counterName, counterValue);
                return;
            }
        } catch (NoClassDefFoundError | NoSuchMethodError e) {
        }
        setCounterFallback(counterName, counterValue);
    }

    private static void setCounterFallback(String counterName, int counterValue) {
        if (Build.VERSION.SDK_INT >= 18) {
            try {
                if (sTraceCounterMethod == null) {
                    sTraceCounterMethod = android.os.Trace.class.getMethod("traceCounter", new Class[]{Long.TYPE, String.class, Integer.TYPE});
                }
                sTraceCounterMethod.invoke((Object) null, new Object[]{Long.valueOf(sTraceTagApp), counterName, Integer.valueOf(counterValue)});
            } catch (Exception e) {
                handleException("traceCounter", e);
            }
        }
    }
}
