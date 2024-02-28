package androidx.core.view;

import android.view.WindowInsetsAnimationController;
import androidx.core.graphics.Insets;

public final class WindowInsetsAnimationControllerCompat {
    private final Impl mImpl;

    private static class Impl {
        Impl() {
        }

        /* access modifiers changed from: package-private */
        public void finish(boolean shown) {
        }

        public float getCurrentAlpha() {
            return 0.0f;
        }

        public float getCurrentFraction() {
            return 0.0f;
        }

        public Insets getCurrentInsets() {
            return Insets.NONE;
        }

        public Insets getHiddenStateInsets() {
            return Insets.NONE;
        }

        public Insets getShownStateInsets() {
            return Insets.NONE;
        }

        public int getTypes() {
            return 0;
        }

        /* access modifiers changed from: package-private */
        public boolean isCancelled() {
            return true;
        }

        /* access modifiers changed from: package-private */
        public boolean isFinished() {
            return false;
        }

        public boolean isReady() {
            return false;
        }

        public void setInsetsAndAlpha(Insets insets, float alpha, float fraction) {
        }
    }

    private static class Impl30 extends Impl {
        private final WindowInsetsAnimationController mController;

        Impl30(WindowInsetsAnimationController controller) {
            this.mController = controller;
        }

        /* access modifiers changed from: package-private */
        public void finish(boolean shown) {
            this.mController.finish(shown);
        }

        public float getCurrentAlpha() {
            return this.mController.getCurrentAlpha();
        }

        public float getCurrentFraction() {
            return this.mController.getCurrentFraction();
        }

        public Insets getCurrentInsets() {
            return Insets.toCompatInsets(this.mController.getCurrentInsets());
        }

        public Insets getHiddenStateInsets() {
            return Insets.toCompatInsets(this.mController.getHiddenStateInsets());
        }

        public Insets getShownStateInsets() {
            return Insets.toCompatInsets(this.mController.getShownStateInsets());
        }

        public int getTypes() {
            return this.mController.getTypes();
        }

        /* access modifiers changed from: package-private */
        public boolean isCancelled() {
            return this.mController.isCancelled();
        }

        /* access modifiers changed from: package-private */
        public boolean isFinished() {
            return this.mController.isFinished();
        }

        public boolean isReady() {
            return this.mController.isReady();
        }

        public void setInsetsAndAlpha(Insets insets, float alpha, float fraction) {
            this.mController.setInsetsAndAlpha(insets == null ? null : insets.toPlatformInsets(), alpha, fraction);
        }
    }

    WindowInsetsAnimationControllerCompat(WindowInsetsAnimationController controller) {
        this.mImpl = new Impl30(controller);
    }

    public void finish(boolean shown) {
        this.mImpl.finish(shown);
    }

    public float getCurrentAlpha() {
        return this.mImpl.getCurrentAlpha();
    }

    public float getCurrentFraction() {
        return this.mImpl.getCurrentFraction();
    }

    public Insets getCurrentInsets() {
        return this.mImpl.getCurrentInsets();
    }

    public Insets getHiddenStateInsets() {
        return this.mImpl.getHiddenStateInsets();
    }

    public Insets getShownStateInsets() {
        return this.mImpl.getShownStateInsets();
    }

    public int getTypes() {
        return this.mImpl.getTypes();
    }

    public boolean isCancelled() {
        return this.mImpl.isCancelled();
    }

    public boolean isFinished() {
        return this.mImpl.isFinished();
    }

    public boolean isReady() {
        return !isFinished() && !isCancelled();
    }

    public void setInsetsAndAlpha(Insets insets, float alpha, float fraction) {
        this.mImpl.setInsetsAndAlpha(insets, alpha, fraction);
    }
}
