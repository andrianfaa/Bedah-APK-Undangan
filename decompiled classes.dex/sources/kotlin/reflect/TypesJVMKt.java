package kotlin.reflect;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;
import mt.Log1F380D;
import okhttp3.HttpUrl;

@Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\u001a\"\u0010\n\u001a\u00020\u00012\n\u0010\u000b\u001a\u0006\u0012\u0002\b\u00030\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000eH\u0003\u001a\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0001H\u0002\u001a\u0016\u0010\u0012\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0003\"\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00028FX\u0004¢\u0006\f\u0012\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\"\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00078BX\u0004¢\u0006\f\u0012\u0004\b\u0003\u0010\b\u001a\u0004\b\u0005\u0010\t¨\u0006\u0015"}, d2 = {"javaType", "Ljava/lang/reflect/Type;", "Lkotlin/reflect/KType;", "getJavaType$annotations", "(Lkotlin/reflect/KType;)V", "getJavaType", "(Lkotlin/reflect/KType;)Ljava/lang/reflect/Type;", "Lkotlin/reflect/KTypeProjection;", "(Lkotlin/reflect/KTypeProjection;)V", "(Lkotlin/reflect/KTypeProjection;)Ljava/lang/reflect/Type;", "createPossiblyInnerType", "jClass", "Ljava/lang/Class;", "arguments", "", "typeToString", "", "type", "computeJavaType", "forceWrapper", "", "kotlin-stdlib"}, k = 2, mv = {1, 7, 1}, xi = 48)
/* compiled from: 0155 */
public final class TypesJVMKt {

    @Metadata(k = 3, mv = {1, 7, 1}, xi = 48)
    /* compiled from: TypesJVM.kt */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[KVariance.values().length];
            iArr[KVariance.IN.ordinal()] = 1;
            iArr[KVariance.INVARIANT.ordinal()] = 2;
            iArr[KVariance.OUT.ordinal()] = 3;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public static final /* synthetic */ String access$typeToString(Type type) {
        String typeToString = typeToString(type);
        Log1F380D.a((Object) typeToString);
        return typeToString;
    }

    /* access modifiers changed from: private */
    public static final Type computeJavaType(KType $this$computeJavaType, boolean forceWrapper) {
        KClassifier classifier = $this$computeJavaType.getClassifier();
        if (classifier instanceof KTypeParameter) {
            return new TypeVariableImpl((KTypeParameter) classifier);
        }
        if (classifier instanceof KClass) {
            KClass kClass = (KClass) classifier;
            Class javaObjectType = forceWrapper ? JvmClassMappingKt.getJavaObjectType(kClass) : JvmClassMappingKt.getJavaClass(kClass);
            List<KTypeProjection> arguments = $this$computeJavaType.getArguments();
            if (arguments.isEmpty()) {
                return javaObjectType;
            }
            if (!javaObjectType.isArray()) {
                return createPossiblyInnerType(javaObjectType, arguments);
            }
            if (javaObjectType.getComponentType().isPrimitive()) {
                return javaObjectType;
            }
            KTypeProjection kTypeProjection = (KTypeProjection) CollectionsKt.singleOrNull(arguments);
            if (kTypeProjection != null) {
                KVariance component1 = kTypeProjection.component1();
                KType component2 = kTypeProjection.component2();
                switch (component1 == null ? -1 : WhenMappings.$EnumSwitchMapping$0[component1.ordinal()]) {
                    case -1:
                    case 1:
                        return javaObjectType;
                    case 2:
                    case 3:
                        Intrinsics.checkNotNull(component2);
                        Type computeJavaType$default = computeJavaType$default(component2, false, 1, (Object) null);
                        return computeJavaType$default instanceof Class ? javaObjectType : new GenericArrayTypeImpl(computeJavaType$default);
                    default:
                        throw new NoWhenBranchMatchedException();
                }
            } else {
                throw new IllegalArgumentException("kotlin.Array must have exactly one type argument: " + $this$computeJavaType);
            }
        } else {
            throw new UnsupportedOperationException("Unsupported type classifier: " + $this$computeJavaType);
        }
    }

    static /* synthetic */ Type computeJavaType$default(KType kType, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        return computeJavaType(kType, z);
    }

    private static final Type createPossiblyInnerType(Class<?> jClass, List<KTypeProjection> arguments) {
        Class<?> declaringClass = jClass.getDeclaringClass();
        if (declaringClass == null) {
            Iterable<KTypeProjection> iterable = arguments;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            for (KTypeProjection javaType : iterable) {
                arrayList.add(getJavaType(javaType));
            }
            return new ParameterizedTypeImpl(jClass, (Type) null, (List) arrayList);
        } else if (Modifier.isStatic(jClass.getModifiers())) {
            Type type = declaringClass;
            Iterable<KTypeProjection> iterable2 = arguments;
            Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
            for (KTypeProjection javaType2 : iterable2) {
                arrayList2.add(getJavaType(javaType2));
            }
            return new ParameterizedTypeImpl(jClass, type, (List) arrayList2);
        } else {
            int length = jClass.getTypeParameters().length;
            Type createPossiblyInnerType = createPossiblyInnerType(declaringClass, arguments.subList(length, arguments.size()));
            Iterable<KTypeProjection> subList = arguments.subList(0, length);
            Collection arrayList3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(subList, 10));
            for (KTypeProjection javaType3 : subList) {
                arrayList3.add(getJavaType(javaType3));
            }
            return new ParameterizedTypeImpl(jClass, createPossiblyInnerType, (List) arrayList3);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0009, code lost:
        r0 = ((kotlin.jvm.internal.KTypeBase) r3).getJavaType();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final java.lang.reflect.Type getJavaType(kotlin.reflect.KType r3) {
        /*
            java.lang.String r0 = "<this>"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r0)
            boolean r0 = r3 instanceof kotlin.jvm.internal.KTypeBase
            if (r0 == 0) goto L_0x0014
            r0 = r3
            kotlin.jvm.internal.KTypeBase r0 = (kotlin.jvm.internal.KTypeBase) r0
            java.lang.reflect.Type r0 = r0.getJavaType()
            if (r0 == 0) goto L_0x0014
            r1 = 0
            return r0
        L_0x0014:
            r0 = 0
            r1 = 1
            r2 = 0
            java.lang.reflect.Type r0 = computeJavaType$default(r3, r0, r1, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.reflect.TypesJVMKt.getJavaType(kotlin.reflect.KType):java.lang.reflect.Type");
    }

    private static final Type getJavaType(KTypeProjection $this$javaType) {
        KVariance variance = $this$javaType.getVariance();
        if (variance == null) {
            return WildcardTypeImpl.Companion.getSTAR();
        }
        KType type = $this$javaType.getType();
        Intrinsics.checkNotNull(type);
        switch (WhenMappings.$EnumSwitchMapping$0[variance.ordinal()]) {
            case 1:
                return new WildcardTypeImpl((Type) null, computeJavaType(type, true));
            case 2:
                return computeJavaType(type, true);
            case 3:
                return new WildcardTypeImpl(computeJavaType(type, true), (Type) null);
            default:
                throw new NoWhenBranchMatchedException();
        }
    }

    public static /* synthetic */ void getJavaType$annotations(KType kType) {
    }

    private static /* synthetic */ void getJavaType$annotations(KTypeProjection kTypeProjection) {
    }

    private static final String typeToString(Type type) {
        String str;
        if (!(type instanceof Class)) {
            return type.toString();
        }
        if (((Class) type).isArray()) {
            Sequence generateSequence = SequencesKt.generateSequence(type, TypesJVMKt$typeToString$unwrap$1.INSTANCE);
            StringBuilder append = new StringBuilder().append(((Class) SequencesKt.last(generateSequence)).getName());
            String repeat = StringsKt.repeat(HttpUrl.PATH_SEGMENT_ENCODE_SET_URI, SequencesKt.count(generateSequence));
            Log1F380D.a((Object) repeat);
            str = append.append(repeat).toString();
        } else {
            str = ((Class) type).getName();
        }
        Intrinsics.checkNotNullExpressionValue(str, "{\n        if (type.isArr…   } else type.name\n    }");
        return str;
    }
}
