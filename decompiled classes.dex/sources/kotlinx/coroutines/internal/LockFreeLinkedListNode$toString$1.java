package kotlinx.coroutines.internal;

import kotlin.Metadata;
import kotlin.jvm.internal.PropertyReference0Impl;
import kotlinx.coroutines.DebugStringsKt;
import mt.Log1F380D;

@Metadata(k = 3, mv = {1, 6, 0}, xi = 48)
/* compiled from: 01A0 */
/* synthetic */ class LockFreeLinkedListNode$toString$1 extends PropertyReference0Impl {
    LockFreeLinkedListNode$toString$1(Object obj) {
        super(obj, DebugStringsKt.class, "classSimpleName", "getClassSimpleName(Ljava/lang/Object;)Ljava/lang/String;", 1);
    }

    public Object get() {
        String classSimpleName = DebugStringsKt.getClassSimpleName(this.receiver);
        Log1F380D.a((Object) classSimpleName);
        return classSimpleName;
    }
}
