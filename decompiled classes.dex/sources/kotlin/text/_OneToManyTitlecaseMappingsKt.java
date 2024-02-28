package kotlin.text;

import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u000e\n\u0002\u0010\f\n\u0000\u001a\f\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0000¨\u0006\u0003"}, d2 = {"titlecaseImpl", "", "", "kotlin-stdlib"}, k = 2, mv = {1, 7, 1}, xi = 48)
/* compiled from: 0165 */
public final class _OneToManyTitlecaseMappingsKt {
    public static final String titlecaseImpl(char $this$titlecaseImpl) {
        String valueOf = String.valueOf($this$titlecaseImpl);
        Log1F380D.a((Object) valueOf);
        Intrinsics.checkNotNull(valueOf, "null cannot be cast to non-null type java.lang.String");
        String upperCase = valueOf.toUpperCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(upperCase, "this as java.lang.String).toUpperCase(Locale.ROOT)");
        if (upperCase.length() <= 1) {
            String valueOf2 = String.valueOf(Character.toTitleCase($this$titlecaseImpl));
            Log1F380D.a((Object) valueOf2);
            return valueOf2;
        } else if ($this$titlecaseImpl == 329) {
            return upperCase;
        } else {
            char charAt = upperCase.charAt(0);
            Intrinsics.checkNotNull(upperCase, "null cannot be cast to non-null type java.lang.String");
            String substring = upperCase.substring(1);
            Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String).substring(startIndex)");
            Intrinsics.checkNotNull(substring, "null cannot be cast to non-null type java.lang.String");
            String lowerCase = substring.toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "this as java.lang.String).toLowerCase(Locale.ROOT)");
            return charAt + lowerCase;
        }
    }
}
