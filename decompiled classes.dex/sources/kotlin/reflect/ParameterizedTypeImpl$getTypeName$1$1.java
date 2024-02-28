package kotlin.reflect;

import java.lang.reflect.Type;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import mt.Log1F380D;

@Metadata(k = 3, mv = {1, 7, 1}, xi = 48)
/* compiled from: 0153 */
/* synthetic */ class ParameterizedTypeImpl$getTypeName$1$1 extends FunctionReferenceImpl implements Function1<Type, String> {
    public static final ParameterizedTypeImpl$getTypeName$1$1 INSTANCE = new ParameterizedTypeImpl$getTypeName$1$1();

    ParameterizedTypeImpl$getTypeName$1$1() {
        super(1, TypesJVMKt.class, "typeToString", "typeToString(Ljava/lang/reflect/Type;)Ljava/lang/String;", 1);
    }

    public final String invoke(Type p0) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        String access$typeToString = TypesJVMKt.access$typeToString(p0);
        Log1F380D.a((Object) access$typeToString);
        return access$typeToString;
    }
}
