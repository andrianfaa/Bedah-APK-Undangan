package kotlin.jvm.internal;

import kotlin.reflect.KCallable;
import kotlin.reflect.KProperty;

public abstract class PropertyReference extends CallableReference implements KProperty {
    public PropertyReference() {
    }

    public PropertyReference(Object receiver) {
        super(receiver);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public PropertyReference(Object receiver, Class owner, String name, String signature, int flags) {
        super(receiver, owner, name, signature, (flags & 1) != 1 ? false : true);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof PropertyReference) {
            PropertyReference propertyReference = (PropertyReference) obj;
            return getOwner().equals(propertyReference.getOwner()) && getName().equals(propertyReference.getName()) && getSignature().equals(propertyReference.getSignature()) && Intrinsics.areEqual(getBoundReceiver(), propertyReference.getBoundReceiver());
        } else if (obj instanceof KProperty) {
            return obj.equals(compute());
        } else {
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public KProperty getReflected() {
        return (KProperty) super.getReflected();
    }

    public int hashCode() {
        return (((getOwner().hashCode() * 31) + getName().hashCode()) * 31) + getSignature().hashCode();
    }

    public boolean isConst() {
        return getReflected().isConst();
    }

    public boolean isLateinit() {
        return getReflected().isLateinit();
    }

    public String toString() {
        KCallable compute = compute();
        return compute != this ? compute.toString() : "property " + getName() + " (Kotlin reflection is not available)";
    }
}
