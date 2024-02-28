package androidx.core.transition;

import android.transition.Transition;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\n¸\u0006\u0000"}, d2 = {"androidx/core/transition/TransitionKt$addListener$listener$1", "Landroid/transition/Transition$TransitionListener;", "onTransitionCancel", "", "transition", "Landroid/transition/Transition;", "onTransitionEnd", "onTransitionPause", "onTransitionResume", "onTransitionStart", "core-ktx_release"}, k = 1, mv = {1, 6, 0}, xi = 176)
/* compiled from: Transition.kt */
public final class TransitionKt$doOnPause$$inlined$addListener$default$1 implements Transition.TransitionListener {
    final /* synthetic */ Function1 $onPause;

    public TransitionKt$doOnPause$$inlined$addListener$default$1(Function1 $onPause2) {
        this.$onPause = $onPause2;
    }

    public void onTransitionCancel(Transition transition) {
        Intrinsics.checkNotNullParameter(transition, "transition");
        Transition transition2 = transition;
    }

    public void onTransitionEnd(Transition transition) {
        Intrinsics.checkNotNullParameter(transition, "transition");
        Transition transition2 = transition;
    }

    public void onTransitionPause(Transition transition) {
        Intrinsics.checkNotNullParameter(transition, "transition");
        this.$onPause.invoke(transition);
    }

    public void onTransitionResume(Transition transition) {
        Intrinsics.checkNotNullParameter(transition, "transition");
        Transition transition2 = transition;
    }

    public void onTransitionStart(Transition transition) {
        Intrinsics.checkNotNullParameter(transition, "transition");
        Transition transition2 = transition;
    }
}
