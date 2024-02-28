package androidx.core.content;

import android.content.SharedPreferences;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u0000 \n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a3\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\bH\bø\u0001\u0000\u0002\u0007\n\u0005\b20\u0001¨\u0006\t"}, d2 = {"edit", "", "Landroid/content/SharedPreferences;", "commit", "", "action", "Lkotlin/Function1;", "Landroid/content/SharedPreferences$Editor;", "Lkotlin/ExtensionFunctionType;", "core-ktx_release"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: SharedPreferences.kt */
public final class SharedPreferencesKt {
    public static final void edit(SharedPreferences $this$edit, boolean commit, Function1<? super SharedPreferences.Editor, Unit> action) {
        Intrinsics.checkNotNullParameter($this$edit, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        SharedPreferences.Editor edit = $this$edit.edit();
        Intrinsics.checkNotNullExpressionValue(edit, "editor");
        action.invoke(edit);
        if (commit) {
            edit.commit();
        } else {
            edit.apply();
        }
    }

    public static /* synthetic */ void edit$default(SharedPreferences $this$edit_u24default, boolean commit, Function1 action, int i, Object obj) {
        if ((i & 1) != 0) {
            commit = false;
        }
        Intrinsics.checkNotNullParameter($this$edit_u24default, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        SharedPreferences.Editor edit = $this$edit_u24default.edit();
        Intrinsics.checkNotNullExpressionValue(edit, "editor");
        action.invoke(edit);
        if (commit) {
            edit.commit();
        } else {
            edit.apply();
        }
    }
}
