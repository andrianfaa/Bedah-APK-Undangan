package kotlin.jvm.internal;

import java.util.Arrays;
import java.util.Collections;
import kotlin.collections.ArraysKt;
import kotlin.reflect.KClass;
import kotlin.reflect.KClassifier;
import kotlin.reflect.KDeclarationContainer;
import kotlin.reflect.KFunction;
import kotlin.reflect.KMutableProperty0;
import kotlin.reflect.KMutableProperty1;
import kotlin.reflect.KMutableProperty2;
import kotlin.reflect.KProperty0;
import kotlin.reflect.KProperty1;
import kotlin.reflect.KProperty2;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeParameter;
import kotlin.reflect.KTypeProjection;
import kotlin.reflect.KVariance;
import okhttp3.HttpUrl;

public class Reflection {
    private static final KClass[] EMPTY_K_CLASS_ARRAY = new KClass[0];
    static final String REFLECTION_NOT_AVAILABLE = " (Kotlin reflection is not available)";
    private static final ReflectionFactory factory;

    static {
        ReflectionFactory reflectionFactory;
        try {
            reflectionFactory = (ReflectionFactory) Class.forName("kotlin.reflect.jvm.internal.ReflectionFactoryImpl").newInstance();
        } catch (ClassCastException e) {
            reflectionFactory = null;
        } catch (ClassNotFoundException e2) {
            reflectionFactory = null;
        } catch (InstantiationException e3) {
            reflectionFactory = null;
        } catch (IllegalAccessException e4) {
            reflectionFactory = null;
        }
        factory = reflectionFactory != null ? reflectionFactory : new ReflectionFactory();
    }

    public static KClass createKotlinClass(Class javaClass) {
        return factory.createKotlinClass(javaClass);
    }

    public static KClass createKotlinClass(Class javaClass, String internalName) {
        return factory.createKotlinClass(javaClass, internalName);
    }

    public static KFunction function(FunctionReference f) {
        return factory.function(f);
    }

    public static KClass getOrCreateKotlinClass(Class javaClass) {
        return factory.getOrCreateKotlinClass(javaClass);
    }

    public static KClass getOrCreateKotlinClass(Class javaClass, String internalName) {
        return factory.getOrCreateKotlinClass(javaClass, internalName);
    }

    public static KClass[] getOrCreateKotlinClasses(Class[] javaClasses) {
        int length = javaClasses.length;
        if (length == 0) {
            return EMPTY_K_CLASS_ARRAY;
        }
        KClass[] kClassArr = new KClass[length];
        for (int i = 0; i < length; i++) {
            kClassArr[i] = getOrCreateKotlinClass(javaClasses[i]);
        }
        return kClassArr;
    }

    public static KDeclarationContainer getOrCreateKotlinPackage(Class javaClass) {
        return factory.getOrCreateKotlinPackage(javaClass, HttpUrl.FRAGMENT_ENCODE_SET);
    }

    public static KDeclarationContainer getOrCreateKotlinPackage(Class javaClass, String moduleName) {
        return factory.getOrCreateKotlinPackage(javaClass, moduleName);
    }

    public static KType mutableCollectionType(KType type) {
        return factory.mutableCollectionType(type);
    }

    public static KMutableProperty0 mutableProperty0(MutablePropertyReference0 p) {
        return factory.mutableProperty0(p);
    }

    public static KMutableProperty1 mutableProperty1(MutablePropertyReference1 p) {
        return factory.mutableProperty1(p);
    }

    public static KMutableProperty2 mutableProperty2(MutablePropertyReference2 p) {
        return factory.mutableProperty2(p);
    }

    public static KType nothingType(KType type) {
        return factory.nothingType(type);
    }

    public static KType nullableTypeOf(Class klass) {
        return factory.typeOf(getOrCreateKotlinClass(klass), Collections.emptyList(), true);
    }

    public static KType nullableTypeOf(Class klass, KTypeProjection arg1) {
        return factory.typeOf(getOrCreateKotlinClass(klass), Collections.singletonList(arg1), true);
    }

    public static KType nullableTypeOf(Class klass, KTypeProjection arg1, KTypeProjection arg2) {
        return factory.typeOf(getOrCreateKotlinClass(klass), Arrays.asList(new KTypeProjection[]{arg1, arg2}), true);
    }

    public static KType nullableTypeOf(Class klass, KTypeProjection... arguments) {
        return factory.typeOf(getOrCreateKotlinClass(klass), ArraysKt.toList((T[]) arguments), true);
    }

    public static KType nullableTypeOf(KClassifier classifier) {
        return factory.typeOf(classifier, Collections.emptyList(), true);
    }

    public static KType platformType(KType lowerBound, KType upperBound) {
        return factory.platformType(lowerBound, upperBound);
    }

    public static KProperty0 property0(PropertyReference0 p) {
        return factory.property0(p);
    }

    public static KProperty1 property1(PropertyReference1 p) {
        return factory.property1(p);
    }

    public static KProperty2 property2(PropertyReference2 p) {
        return factory.property2(p);
    }

    public static String renderLambdaToString(FunctionBase lambda) {
        return factory.renderLambdaToString(lambda);
    }

    public static String renderLambdaToString(Lambda lambda) {
        return factory.renderLambdaToString(lambda);
    }

    public static void setUpperBounds(KTypeParameter typeParameter, KType bound) {
        factory.setUpperBounds(typeParameter, Collections.singletonList(bound));
    }

    public static void setUpperBounds(KTypeParameter typeParameter, KType... bounds) {
        factory.setUpperBounds(typeParameter, ArraysKt.toList((T[]) bounds));
    }

    public static KType typeOf(Class klass) {
        return factory.typeOf(getOrCreateKotlinClass(klass), Collections.emptyList(), false);
    }

    public static KType typeOf(Class klass, KTypeProjection arg1) {
        return factory.typeOf(getOrCreateKotlinClass(klass), Collections.singletonList(arg1), false);
    }

    public static KType typeOf(Class klass, KTypeProjection arg1, KTypeProjection arg2) {
        return factory.typeOf(getOrCreateKotlinClass(klass), Arrays.asList(new KTypeProjection[]{arg1, arg2}), false);
    }

    public static KType typeOf(Class klass, KTypeProjection... arguments) {
        return factory.typeOf(getOrCreateKotlinClass(klass), ArraysKt.toList((T[]) arguments), false);
    }

    public static KType typeOf(KClassifier classifier) {
        return factory.typeOf(classifier, Collections.emptyList(), false);
    }

    public static KTypeParameter typeParameter(Object container, String name, KVariance variance, boolean isReified) {
        return factory.typeParameter(container, name, variance, isReified);
    }
}
