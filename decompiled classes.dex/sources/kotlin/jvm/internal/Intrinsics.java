package kotlin.jvm.internal;

import java.util.Arrays;
import kotlin.KotlinNullPointerException;
import kotlin.UninitializedPropertyAccessException;
import mt.Log1F380D;

/* compiled from: 013E */
public class Intrinsics {

    public static class Kotlin {
        private Kotlin() {
        }
    }

    private Intrinsics() {
    }

    public static boolean areEqual(double first, Double second) {
        return second != null && first == second.doubleValue();
    }

    public static boolean areEqual(float first, Float second) {
        return second != null && first == second.floatValue();
    }

    public static boolean areEqual(Double first, double second) {
        return first != null && first.doubleValue() == second;
    }

    public static boolean areEqual(Double first, Double second) {
        if (first == null) {
            if (second == null) {
                return true;
            }
        } else if (second != null && first.doubleValue() == second.doubleValue()) {
            return true;
        }
        return false;
    }

    public static boolean areEqual(Float first, float second) {
        return first != null && first.floatValue() == second;
    }

    public static boolean areEqual(Float first, Float second) {
        if (first == null) {
            if (second == null) {
                return true;
            }
        } else if (second != null && first.floatValue() == second.floatValue()) {
            return true;
        }
        return false;
    }

    public static boolean areEqual(Object first, Object second) {
        return first == null ? second == null : first.equals(second);
    }

    public static void checkExpressionValueIsNotNull(Object value, String expression) {
        if (value == null) {
            throw ((IllegalStateException) sanitizeStackTrace(new IllegalStateException(expression + " must not be null")));
        }
    }

    public static void checkFieldIsNotNull(Object value, String message) {
        if (value == null) {
            throw ((IllegalStateException) sanitizeStackTrace(new IllegalStateException(message)));
        }
    }

    public static void checkFieldIsNotNull(Object value, String className, String fieldName) {
        if (value == null) {
            throw ((IllegalStateException) sanitizeStackTrace(new IllegalStateException("Field specified as non-null is null: " + className + "." + fieldName)));
        }
    }

    public static void checkHasClass(String internalName) throws ClassNotFoundException {
        String replace = internalName.replace('/', '.');
        try {
            Class.forName(replace);
        } catch (ClassNotFoundException e) {
            throw ((ClassNotFoundException) sanitizeStackTrace(new ClassNotFoundException("Class " + replace + " is not found. Please update the Kotlin runtime to the latest version", e)));
        }
    }

    public static void checkHasClass(String internalName, String requiredVersion) throws ClassNotFoundException {
        String replace = internalName.replace('/', '.');
        try {
            Class.forName(replace);
        } catch (ClassNotFoundException e) {
            throw ((ClassNotFoundException) sanitizeStackTrace(new ClassNotFoundException("Class " + replace + " is not found: this code requires the Kotlin runtime of version at least " + requiredVersion, e)));
        }
    }

    public static void checkNotNull(Object object) {
        if (object == null) {
            throwJavaNpe();
        }
    }

    public static void checkNotNull(Object object, String message) {
        if (object == null) {
            throwJavaNpe(message);
        }
    }

    public static void checkNotNullExpressionValue(Object value, String expression) {
        if (value == null) {
            throw ((NullPointerException) sanitizeStackTrace(new NullPointerException(expression + " must not be null")));
        }
    }

    public static void checkNotNullParameter(Object value, String paramName) {
        if (value == null) {
            throwParameterIsNullNPE(paramName);
        }
    }

    public static void checkParameterIsNotNull(Object value, String paramName) {
        if (value == null) {
            throwParameterIsNullIAE(paramName);
        }
    }

    public static void checkReturnedValueIsNotNull(Object value, String message) {
        if (value == null) {
            throw ((IllegalStateException) sanitizeStackTrace(new IllegalStateException(message)));
        }
    }

    public static void checkReturnedValueIsNotNull(Object value, String className, String methodName) {
        if (value == null) {
            throw ((IllegalStateException) sanitizeStackTrace(new IllegalStateException("Method specified as non-null returned null: " + className + "." + methodName)));
        }
    }

    public static int compare(int thisVal, int anotherVal) {
        if (thisVal < anotherVal) {
            return -1;
        }
        return thisVal == anotherVal ? 0 : 1;
    }

    public static int compare(long thisVal, long anotherVal) {
        if (thisVal < anotherVal) {
            return -1;
        }
        return thisVal == anotherVal ? 0 : 1;
    }

    private static String createParameterIsNullExceptionMessage(String paramName) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        String className = stackTraceElement.getClassName();
        return "Parameter specified as non-null is null: method " + className + "." + stackTraceElement.getMethodName() + ", parameter " + paramName;
    }

    public static void needClassReification() {
        throwUndefinedForReified();
    }

    public static void needClassReification(String message) {
        throwUndefinedForReified(message);
    }

    public static void reifiedOperationMarker(int id, String typeParameterIdentifier) {
        throwUndefinedForReified();
    }

    public static void reifiedOperationMarker(int id, String typeParameterIdentifier, String message) {
        throwUndefinedForReified(message);
    }

    private static <T extends Throwable> T sanitizeStackTrace(T t) {
        return sanitizeStackTrace(t, Intrinsics.class.getName());
    }

    static <T extends Throwable> T sanitizeStackTrace(T t, String classNameToDrop) {
        StackTraceElement[] stackTrace = t.getStackTrace();
        int length = stackTrace.length;
        int i = -1;
        for (int i2 = 0; i2 < length; i2++) {
            if (classNameToDrop.equals(stackTrace[i2].getClassName())) {
                i = i2;
            }
        }
        t.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(stackTrace, i + 1, length));
        return t;
    }

    public static String stringPlus(String self, Object other) {
        return self + other;
    }

    public static void throwAssert() {
        throw ((AssertionError) sanitizeStackTrace(new AssertionError()));
    }

    public static void throwAssert(String message) {
        throw ((AssertionError) sanitizeStackTrace(new AssertionError(message)));
    }

    public static void throwIllegalArgument() {
        throw ((IllegalArgumentException) sanitizeStackTrace(new IllegalArgumentException()));
    }

    public static void throwIllegalArgument(String message) {
        throw ((IllegalArgumentException) sanitizeStackTrace(new IllegalArgumentException(message)));
    }

    public static void throwIllegalState() {
        throw ((IllegalStateException) sanitizeStackTrace(new IllegalStateException()));
    }

    public static void throwIllegalState(String message) {
        throw ((IllegalStateException) sanitizeStackTrace(new IllegalStateException(message)));
    }

    public static void throwJavaNpe() {
        throw ((NullPointerException) sanitizeStackTrace(new NullPointerException()));
    }

    public static void throwJavaNpe(String message) {
        throw ((NullPointerException) sanitizeStackTrace(new NullPointerException(message)));
    }

    public static void throwNpe() {
        throw ((KotlinNullPointerException) sanitizeStackTrace(new KotlinNullPointerException()));
    }

    public static void throwNpe(String message) {
        throw ((KotlinNullPointerException) sanitizeStackTrace(new KotlinNullPointerException(message)));
    }

    private static void throwParameterIsNullIAE(String paramName) {
        String createParameterIsNullExceptionMessage = createParameterIsNullExceptionMessage(paramName);
        Log1F380D.a((Object) createParameterIsNullExceptionMessage);
        throw ((IllegalArgumentException) sanitizeStackTrace(new IllegalArgumentException(createParameterIsNullExceptionMessage)));
    }

    public static void throwUndefinedForReified() {
        throwUndefinedForReified("This function has a reified type parameter and thus can only be inlined at compilation time, not called directly.");
    }

    public static void throwUndefinedForReified(String message) {
        throw new UnsupportedOperationException(message);
    }

    public static void throwUninitializedProperty(String message) {
        throw ((UninitializedPropertyAccessException) sanitizeStackTrace(new UninitializedPropertyAccessException(message)));
    }

    public static void throwUninitializedPropertyAccessException(String propertyName) {
        throwUninitializedProperty("lateinit property " + propertyName + " has not been initialized");
    }

    private static void throwParameterIsNullNPE(String paramName) {
        String createParameterIsNullExceptionMessage = createParameterIsNullExceptionMessage(paramName);
        Log1F380D.a((Object) createParameterIsNullExceptionMessage);
        throw ((NullPointerException) sanitizeStackTrace(new NullPointerException(createParameterIsNullExceptionMessage)));
    }
}
