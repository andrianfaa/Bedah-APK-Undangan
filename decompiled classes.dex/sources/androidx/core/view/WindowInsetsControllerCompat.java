package androidx.core.view;

import android.os.Build;
import android.os.CancellationSignal;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsAnimationControlListener;
import android.view.WindowInsetsAnimationController;
import android.view.WindowInsetsController;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import androidx.collection.SimpleArrayMap;

public final class WindowInsetsControllerCompat {
    public static final int BEHAVIOR_SHOW_BARS_BY_SWIPE = 1;
    public static final int BEHAVIOR_SHOW_BARS_BY_TOUCH = 0;
    public static final int BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE = 2;
    private final Impl mImpl;

    private static class Impl {
        Impl() {
        }

        /* access modifiers changed from: package-private */
        public void addOnControllableInsetsChangedListener(OnControllableInsetsChangedListener listener) {
        }

        /* access modifiers changed from: package-private */
        public void controlWindowInsetsAnimation(int types, long durationMillis, Interpolator interpolator, CancellationSignal cancellationSignal, WindowInsetsAnimationControlListenerCompat listener) {
        }

        /* access modifiers changed from: package-private */
        public int getSystemBarsBehavior() {
            return 0;
        }

        /* access modifiers changed from: package-private */
        public void hide(int types) {
        }

        public boolean isAppearanceLightNavigationBars() {
            return false;
        }

        public boolean isAppearanceLightStatusBars() {
            return false;
        }

        /* access modifiers changed from: package-private */
        public void removeOnControllableInsetsChangedListener(OnControllableInsetsChangedListener listener) {
        }

        public void setAppearanceLightNavigationBars(boolean isLight) {
        }

        public void setAppearanceLightStatusBars(boolean isLight) {
        }

        /* access modifiers changed from: package-private */
        public void setSystemBarsBehavior(int behavior) {
        }

        /* access modifiers changed from: package-private */
        public void show(int types) {
        }
    }

    private static class Impl20 extends Impl {
        private final View mView;
        protected final Window mWindow;

        Impl20(Window window, View view) {
            this.mWindow = window;
            this.mView = view;
        }

        private void hideForType(int type) {
            switch (type) {
                case 1:
                    setSystemUiFlag(4);
                    return;
                case 2:
                    setSystemUiFlag(2);
                    return;
                case 8:
                    ((InputMethodManager) this.mWindow.getContext().getSystemService("input_method")).hideSoftInputFromWindow(this.mWindow.getDecorView().getWindowToken(), 0);
                    return;
                default:
                    return;
            }
        }

        private void showForType(int type) {
            switch (type) {
                case 1:
                    unsetSystemUiFlag(4);
                    unsetWindowFlag(1024);
                    return;
                case 2:
                    unsetSystemUiFlag(2);
                    return;
                case 8:
                    View view = this.mView;
                    if (view.isInEditMode() || view.onCheckIsTextEditor()) {
                        view.requestFocus();
                    } else {
                        view = this.mWindow.getCurrentFocus();
                    }
                    if (view == null) {
                        view = this.mWindow.findViewById(16908290);
                    }
                    if (view != null && view.hasWindowFocus()) {
                        View view2 = view;
                        view2.post(new WindowInsetsControllerCompat$Impl20$$ExternalSyntheticLambda0(view2));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        /* access modifiers changed from: package-private */
        public void addOnControllableInsetsChangedListener(OnControllableInsetsChangedListener listener) {
        }

        /* access modifiers changed from: package-private */
        public void controlWindowInsetsAnimation(int types, long durationMillis, Interpolator interpolator, CancellationSignal cancellationSignal, WindowInsetsAnimationControlListenerCompat listener) {
        }

        /* access modifiers changed from: package-private */
        public int getSystemBarsBehavior() {
            return 0;
        }

        /* access modifiers changed from: package-private */
        public void hide(int typeMask) {
            for (int i = 1; i <= 256; i <<= 1) {
                if ((typeMask & i) != 0) {
                    hideForType(i);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void removeOnControllableInsetsChangedListener(OnControllableInsetsChangedListener listener) {
        }

        /* access modifiers changed from: package-private */
        public void setSystemBarsBehavior(int behavior) {
            switch (behavior) {
                case 0:
                    unsetSystemUiFlag(6144);
                    return;
                case 1:
                    unsetSystemUiFlag(4096);
                    setSystemUiFlag(2048);
                    return;
                case 2:
                    unsetSystemUiFlag(2048);
                    setSystemUiFlag(4096);
                    return;
                default:
                    return;
            }
        }

        /* access modifiers changed from: protected */
        public void setSystemUiFlag(int systemUiFlag) {
            View decorView = this.mWindow.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | systemUiFlag);
        }

        /* access modifiers changed from: protected */
        public void setWindowFlag(int windowFlag) {
            this.mWindow.addFlags(windowFlag);
        }

        /* access modifiers changed from: package-private */
        public void show(int typeMask) {
            for (int i = 1; i <= 256; i <<= 1) {
                if ((typeMask & i) != 0) {
                    showForType(i);
                }
            }
        }

        /* access modifiers changed from: protected */
        public void unsetSystemUiFlag(int systemUiFlag) {
            View decorView = this.mWindow.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & (~systemUiFlag));
        }

        /* access modifiers changed from: protected */
        public void unsetWindowFlag(int windowFlag) {
            this.mWindow.clearFlags(windowFlag);
        }
    }

    private static class Impl23 extends Impl20 {
        Impl23(Window window, View view) {
            super(window, view);
        }

        public boolean isAppearanceLightStatusBars() {
            return (this.mWindow.getDecorView().getSystemUiVisibility() & 8192) != 0;
        }

        public void setAppearanceLightStatusBars(boolean isLight) {
            if (isLight) {
                unsetWindowFlag(67108864);
                setWindowFlag(Integer.MIN_VALUE);
                setSystemUiFlag(8192);
                return;
            }
            unsetSystemUiFlag(8192);
        }
    }

    private static class Impl26 extends Impl23 {
        Impl26(Window window, View view) {
            super(window, view);
        }

        public boolean isAppearanceLightNavigationBars() {
            return (this.mWindow.getDecorView().getSystemUiVisibility() & 16) != 0;
        }

        public void setAppearanceLightNavigationBars(boolean isLight) {
            if (isLight) {
                unsetWindowFlag(134217728);
                setWindowFlag(Integer.MIN_VALUE);
                setSystemUiFlag(16);
                return;
            }
            unsetSystemUiFlag(16);
        }
    }

    private static class Impl30 extends Impl {
        final WindowInsetsControllerCompat mCompatController;
        final WindowInsetsController mInsetsController;
        private final SimpleArrayMap<OnControllableInsetsChangedListener, WindowInsetsController.OnControllableInsetsChangedListener> mListeners;
        protected Window mWindow;

        Impl30(Window window, WindowInsetsControllerCompat compatController) {
            this(window.getInsetsController(), compatController);
            this.mWindow = window;
        }

        Impl30(WindowInsetsController insetsController, WindowInsetsControllerCompat compatController) {
            this.mListeners = new SimpleArrayMap<>();
            this.mInsetsController = insetsController;
            this.mCompatController = compatController;
        }

        /* access modifiers changed from: package-private */
        public void addOnControllableInsetsChangedListener(OnControllableInsetsChangedListener listener) {
            if (!this.mListeners.containsKey(listener)) {
                WindowInsetsControllerCompat$Impl30$$ExternalSyntheticLambda0 windowInsetsControllerCompat$Impl30$$ExternalSyntheticLambda0 = new WindowInsetsControllerCompat$Impl30$$ExternalSyntheticLambda0(this, listener);
                this.mListeners.put(listener, windowInsetsControllerCompat$Impl30$$ExternalSyntheticLambda0);
                this.mInsetsController.addOnControllableInsetsChangedListener(windowInsetsControllerCompat$Impl30$$ExternalSyntheticLambda0);
            }
        }

        /* access modifiers changed from: package-private */
        public void controlWindowInsetsAnimation(int types, long durationMillis, Interpolator interpolator, CancellationSignal cancellationSignal, final WindowInsetsAnimationControlListenerCompat listener) {
            this.mInsetsController.controlWindowInsetsAnimation(types, durationMillis, interpolator, cancellationSignal, new WindowInsetsAnimationControlListener() {
                private WindowInsetsAnimationControllerCompat mCompatAnimController = null;

                public void onCancelled(WindowInsetsAnimationController controller) {
                    listener.onCancelled(controller == null ? null : this.mCompatAnimController);
                }

                public void onFinished(WindowInsetsAnimationController controller) {
                    listener.onFinished(this.mCompatAnimController);
                }

                public void onReady(WindowInsetsAnimationController controller, int types) {
                    WindowInsetsAnimationControllerCompat windowInsetsAnimationControllerCompat = new WindowInsetsAnimationControllerCompat(controller);
                    this.mCompatAnimController = windowInsetsAnimationControllerCompat;
                    listener.onReady(windowInsetsAnimationControllerCompat, types);
                }
            });
        }

        /* access modifiers changed from: package-private */
        public int getSystemBarsBehavior() {
            return this.mInsetsController.getSystemBarsBehavior();
        }

        /* access modifiers changed from: package-private */
        public void hide(int types) {
            this.mInsetsController.hide(types);
        }

        public boolean isAppearanceLightNavigationBars() {
            return (this.mInsetsController.getSystemBarsAppearance() & 16) != 0;
        }

        public boolean isAppearanceLightStatusBars() {
            return (this.mInsetsController.getSystemBarsAppearance() & 8) != 0;
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$addOnControllableInsetsChangedListener$0$androidx-core-view-WindowInsetsControllerCompat$Impl30  reason: not valid java name */
        public /* synthetic */ void m25lambda$addOnControllableInsetsChangedListener$0$androidxcoreviewWindowInsetsControllerCompat$Impl30(OnControllableInsetsChangedListener listener, WindowInsetsController controller, int typeMask) {
            if (this.mInsetsController == controller) {
                listener.onControllableInsetsChanged(this.mCompatController, typeMask);
            }
        }

        /* access modifiers changed from: package-private */
        public void removeOnControllableInsetsChangedListener(OnControllableInsetsChangedListener listener) {
            WindowInsetsController.OnControllableInsetsChangedListener remove = this.mListeners.remove(listener);
            if (remove != null) {
                this.mInsetsController.removeOnControllableInsetsChangedListener(remove);
            }
        }

        public void setAppearanceLightNavigationBars(boolean isLight) {
            if (isLight) {
                if (this.mWindow != null) {
                    setSystemUiFlag(16);
                }
                this.mInsetsController.setSystemBarsAppearance(16, 16);
                return;
            }
            if (this.mWindow != null) {
                unsetSystemUiFlag(16);
            }
            this.mInsetsController.setSystemBarsAppearance(0, 16);
        }

        public void setAppearanceLightStatusBars(boolean isLight) {
            if (isLight) {
                if (this.mWindow != null) {
                    setSystemUiFlag(8192);
                }
                this.mInsetsController.setSystemBarsAppearance(8, 8);
                return;
            }
            if (this.mWindow != null) {
                unsetSystemUiFlag(8192);
            }
            this.mInsetsController.setSystemBarsAppearance(0, 8);
        }

        /* access modifiers changed from: package-private */
        public void setSystemBarsBehavior(int behavior) {
            this.mInsetsController.setSystemBarsBehavior(behavior);
        }

        /* access modifiers changed from: protected */
        public void setSystemUiFlag(int systemUiFlag) {
            View decorView = this.mWindow.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | systemUiFlag);
        }

        /* access modifiers changed from: package-private */
        public void show(int types) {
            if (!(this.mWindow == null || (types & 8) == 0 || Build.VERSION.SDK_INT >= 32)) {
                ((InputMethodManager) this.mWindow.getContext().getSystemService("input_method")).isActive();
            }
            this.mInsetsController.show(types);
        }

        /* access modifiers changed from: protected */
        public void unsetSystemUiFlag(int systemUiFlag) {
            View decorView = this.mWindow.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & (~systemUiFlag));
        }
    }

    public interface OnControllableInsetsChangedListener {
        void onControllableInsetsChanged(WindowInsetsControllerCompat windowInsetsControllerCompat, int i);
    }

    public WindowInsetsControllerCompat(Window window, View view) {
        if (Build.VERSION.SDK_INT >= 30) {
            this.mImpl = new Impl30(window, this);
        } else if (Build.VERSION.SDK_INT >= 26) {
            this.mImpl = new Impl26(window, view);
        } else if (Build.VERSION.SDK_INT >= 23) {
            this.mImpl = new Impl23(window, view);
        } else if (Build.VERSION.SDK_INT >= 20) {
            this.mImpl = new Impl20(window, view);
        } else {
            this.mImpl = new Impl();
        }
    }

    @Deprecated
    private WindowInsetsControllerCompat(WindowInsetsController insetsController) {
        this.mImpl = new Impl30(insetsController, this);
    }

    @Deprecated
    public static WindowInsetsControllerCompat toWindowInsetsControllerCompat(WindowInsetsController insetsController) {
        return new WindowInsetsControllerCompat(insetsController);
    }

    public void addOnControllableInsetsChangedListener(OnControllableInsetsChangedListener listener) {
        this.mImpl.addOnControllableInsetsChangedListener(listener);
    }

    public void controlWindowInsetsAnimation(int types, long durationMillis, Interpolator interpolator, CancellationSignal cancellationSignal, WindowInsetsAnimationControlListenerCompat listener) {
        this.mImpl.controlWindowInsetsAnimation(types, durationMillis, interpolator, cancellationSignal, listener);
    }

    public int getSystemBarsBehavior() {
        return this.mImpl.getSystemBarsBehavior();
    }

    public void hide(int types) {
        this.mImpl.hide(types);
    }

    public boolean isAppearanceLightNavigationBars() {
        return this.mImpl.isAppearanceLightNavigationBars();
    }

    public boolean isAppearanceLightStatusBars() {
        return this.mImpl.isAppearanceLightStatusBars();
    }

    public void removeOnControllableInsetsChangedListener(OnControllableInsetsChangedListener listener) {
        this.mImpl.removeOnControllableInsetsChangedListener(listener);
    }

    public void setAppearanceLightNavigationBars(boolean isLight) {
        this.mImpl.setAppearanceLightNavigationBars(isLight);
    }

    public void setAppearanceLightStatusBars(boolean isLight) {
        this.mImpl.setAppearanceLightStatusBars(isLight);
    }

    public void setSystemBarsBehavior(int behavior) {
        this.mImpl.setSystemBarsBehavior(behavior);
    }

    public void show(int types) {
        this.mImpl.show(types);
    }
}
