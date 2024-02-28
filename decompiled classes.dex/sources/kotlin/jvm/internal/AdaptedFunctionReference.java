package kotlin.jvm.internal;

import java.io.Serializable;
import kotlin.reflect.KDeclarationContainer;
import mt.Log1F380D;

/* compiled from: 013B */
public class AdaptedFunctionReference implements FunctionBase, Serializable {
    private final int arity;
    private final int flags;
    private final boolean isTopLevel;
    private final String name;
    private final Class owner;
    protected final Object receiver;
    private final String signature;

    public AdaptedFunctionReference(int arity2, Class owner2, String name2, String signature2, int flags2) {
        this(arity2, CallableReference.NO_RECEIVER, owner2, name2, signature2, flags2);
    }

    public AdaptedFunctionReference(int arity2, Object receiver2, Class owner2, String name2, String signature2, int flags2) {
        this.receiver = receiver2;
        this.owner = owner2;
        this.name = name2;
        this.signature = signature2;
        this.isTopLevel = (flags2 & 1) != 1 ? false : true;
        this.arity = arity2;
        this.flags = flags2 >> 1;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdaptedFunctionReference)) {
            return false;
        }
        AdaptedFunctionReference adaptedFunctionReference = (AdaptedFunctionReference) o;
        return this.isTopLevel == adaptedFunctionReference.isTopLevel && this.arity == adaptedFunctionReference.arity && this.flags == adaptedFunctionReference.flags && Intrinsics.areEqual(this.receiver, adaptedFunctionReference.receiver) && Intrinsics.areEqual((Object) this.owner, (Object) adaptedFunctionReference.owner) && this.name.equals(adaptedFunctionReference.name) && this.signature.equals(adaptedFunctionReference.signature);
    }

    public int getArity() {
        return this.arity;
    }

    public KDeclarationContainer getOwner() {
        Class cls = this.owner;
        if (cls == null) {
            return null;
        }
        return this.isTopLevel ? Reflection.getOrCreateKotlinPackage(cls) : Reflection.getOrCreateKotlinClass(cls);
    }

    public int hashCode() {
        Object obj = this.receiver;
        int i = 0;
        int hashCode = (obj != null ? obj.hashCode() : 0) * 31;
        Class cls = this.owner;
        if (cls != null) {
            i = cls.hashCode();
        }
        return ((((((((((hashCode + i) * 31) + this.name.hashCode()) * 31) + this.signature.hashCode()) * 31) + (this.isTopLevel ? 1231 : 1237)) * 31) + this.arity) * 31) + this.flags;
    }

    public String toString() {
        String renderLambdaToString = Reflection.renderLambdaToString((FunctionBase) this);
        Log1F380D.a((Object) renderLambdaToString);
        return renderLambdaToString;
    }
}
