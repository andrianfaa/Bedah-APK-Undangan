package kotlin.internal;

import java.lang.reflect.Method;
import java.util.regex.MatchResult;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.FallbackThreadLocalRandom;
import kotlin.random.Random;
import kotlin.text.MatchGroup;

@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\b\u0010\u0018\u00002\u00020\u0001:\u0001\u0012B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\b\u0010\b\u001a\u00020\tH\u0016J\u001a\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0016\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00060\u00112\u0006\u0010\u0007\u001a\u00020\u0006H\u0016¨\u0006\u0013"}, d2 = {"Lkotlin/internal/PlatformImplementations;", "", "()V", "addSuppressed", "", "cause", "", "exception", "defaultPlatformRandom", "Lkotlin/random/Random;", "getMatchResultNamedGroup", "Lkotlin/text/MatchGroup;", "matchResult", "Ljava/util/regex/MatchResult;", "name", "", "getSuppressed", "", "ReflectThrowable", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
/* compiled from: PlatformImplementations.kt */
public class PlatformImplementations {

    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00048\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lkotlin/internal/PlatformImplementations$ReflectThrowable;", "", "()V", "addSuppressed", "Ljava/lang/reflect/Method;", "getSuppressed", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: PlatformImplementations.kt */
    private static final class ReflectThrowable {
        public static final ReflectThrowable INSTANCE = new ReflectThrowable();
        public static final Method addSuppressed;
        public static final Method getSuppressed;

        /* JADX WARNING: Removed duplicated region for block: B:10:0x0045 A[LOOP:0: B:1:0x0017->B:10:0x0045, LOOP_END] */
        /* JADX WARNING: Removed duplicated region for block: B:20:0x0049 A[EDGE_INSN: B:20:0x0049->B:12:0x0049 ?: BREAK  , SYNTHETIC] */
        static {
            /*
                kotlin.internal.PlatformImplementations$ReflectThrowable r0 = new kotlin.internal.PlatformImplementations$ReflectThrowable
                r0.<init>()
                INSTANCE = r0
                java.lang.Class<java.lang.Throwable> r0 = java.lang.Throwable.class
                java.lang.reflect.Method[] r1 = r0.getMethods()
                java.lang.String r2 = "throwableMethods"
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r1, r2)
                int r2 = r1.length
                r3 = 0
                r4 = r3
            L_0x0017:
                r5 = 0
                if (r4 >= r2) goto L_0x0048
                r6 = r1[r4]
                r7 = r6
                r8 = 0
                java.lang.String r9 = r7.getName()
                java.lang.String r10 = "addSuppressed"
                boolean r9 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r9, (java.lang.Object) r10)
                if (r9 == 0) goto L_0x0041
                java.lang.Class[] r9 = r7.getParameterTypes()
                java.lang.String r10 = "it.parameterTypes"
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r9, r10)
                java.lang.Object[] r9 = (java.lang.Object[]) r9
                java.lang.Object r9 = kotlin.collections.ArraysKt.singleOrNull((T[]) r9)
                boolean r9 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r9, (java.lang.Object) r0)
                if (r9 == 0) goto L_0x0041
                r9 = 1
                goto L_0x0042
            L_0x0041:
                r9 = r3
            L_0x0042:
                if (r9 == 0) goto L_0x0045
                goto L_0x0049
            L_0x0045:
                int r4 = r4 + 1
                goto L_0x0017
            L_0x0048:
                r6 = r5
            L_0x0049:
                addSuppressed = r6
                int r2 = r1.length
            L_0x004c:
                if (r3 >= r2) goto L_0x0063
                r4 = r1[r3]
                r6 = r4
                r7 = 0
                java.lang.String r8 = r6.getName()
                java.lang.String r9 = "getSuppressed"
                boolean r6 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r8, (java.lang.Object) r9)
                if (r6 == 0) goto L_0x0060
                r5 = r4
                goto L_0x0063
            L_0x0060:
                int r3 = r3 + 1
                goto L_0x004c
            L_0x0063:
                getSuppressed = r5
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlin.internal.PlatformImplementations.ReflectThrowable.<clinit>():void");
        }

        private ReflectThrowable() {
        }
    }

    public void addSuppressed(Throwable cause, Throwable exception) {
        Intrinsics.checkNotNullParameter(cause, "cause");
        Intrinsics.checkNotNullParameter(exception, "exception");
        Method method = ReflectThrowable.addSuppressed;
        if (method != null) {
            method.invoke(cause, new Object[]{exception});
        }
    }

    public Random defaultPlatformRandom() {
        return new FallbackThreadLocalRandom();
    }

    public MatchGroup getMatchResultNamedGroup(MatchResult matchResult, String name) {
        Intrinsics.checkNotNullParameter(matchResult, "matchResult");
        Intrinsics.checkNotNullParameter(name, "name");
        throw new UnsupportedOperationException("Retrieving groups by name is not supported on this platform.");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0012, code lost:
        r0 = kotlin.collections.ArraysKt.asList((T[]) (java.lang.Throwable[]) (r0 = r0.invoke(r4, new java.lang.Object[0])));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<java.lang.Throwable> getSuppressed(java.lang.Throwable r4) {
        /*
            r3 = this;
            java.lang.String r0 = "exception"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r0)
            java.lang.reflect.Method r0 = kotlin.internal.PlatformImplementations.ReflectThrowable.getSuppressed
            if (r0 == 0) goto L_0x001c
            r1 = 0
            java.lang.Object[] r1 = new java.lang.Object[r1]
            java.lang.Object r0 = r0.invoke(r4, r1)
            if (r0 == 0) goto L_0x001c
            r1 = 0
            r2 = r0
            java.lang.Throwable[] r2 = (java.lang.Throwable[]) r2
            java.util.List r0 = kotlin.collections.ArraysKt.asList((T[]) r2)
            if (r0 != 0) goto L_0x0020
        L_0x001c:
            java.util.List r0 = kotlin.collections.CollectionsKt.emptyList()
        L_0x0020:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.internal.PlatformImplementations.getSuppressed(java.lang.Throwable):java.util.List");
    }
}
