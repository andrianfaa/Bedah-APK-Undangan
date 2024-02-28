package androidx.core.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import java.lang.ref.WeakReference;

public final class ViewPropertyAnimatorCompat {
    static final int LISTENER_TAG_ID = 2113929216;
    Runnable mEndAction = null;
    int mOldLayerType = -1;
    Runnable mStartAction = null;
    private final WeakReference<View> mView;

    static class Api16Impl {
        private Api16Impl() {
        }

        static ViewPropertyAnimator withEndAction(ViewPropertyAnimator viewPropertyAnimator, Runnable runnable) {
            return viewPropertyAnimator.withEndAction(runnable);
        }

        static ViewPropertyAnimator withLayer(ViewPropertyAnimator viewPropertyAnimator) {
            return viewPropertyAnimator.withLayer();
        }

        static ViewPropertyAnimator withStartAction(ViewPropertyAnimator viewPropertyAnimator, Runnable runnable) {
            return viewPropertyAnimator.withStartAction(runnable);
        }
    }

    static class Api18Impl {
        private Api18Impl() {
        }

        static Interpolator getInterpolator(ViewPropertyAnimator viewPropertyAnimator) {
            return (Interpolator) viewPropertyAnimator.getInterpolator();
        }
    }

    static class Api19Impl {
        private Api19Impl() {
        }

        static ViewPropertyAnimator setUpdateListener(ViewPropertyAnimator viewPropertyAnimator, ValueAnimator.AnimatorUpdateListener listener) {
            return viewPropertyAnimator.setUpdateListener(listener);
        }
    }

    static class Api21Impl {
        private Api21Impl() {
        }

        static ViewPropertyAnimator translationZ(ViewPropertyAnimator viewPropertyAnimator, float value) {
            return viewPropertyAnimator.translationZ(value);
        }

        static ViewPropertyAnimator translationZBy(ViewPropertyAnimator viewPropertyAnimator, float value) {
            return viewPropertyAnimator.translationZBy(value);
        }

        static ViewPropertyAnimator z(ViewPropertyAnimator viewPropertyAnimator, float value) {
            return viewPropertyAnimator.z(value);
        }

        static ViewPropertyAnimator zBy(ViewPropertyAnimator viewPropertyAnimator, float value) {
            return viewPropertyAnimator.zBy(value);
        }
    }

    static class ViewPropertyAnimatorListenerApi14 implements ViewPropertyAnimatorListener {
        boolean mAnimEndCalled;
        ViewPropertyAnimatorCompat mVpa;

        ViewPropertyAnimatorListenerApi14(ViewPropertyAnimatorCompat vpa) {
            this.mVpa = vpa;
        }

        public void onAnimationCancel(View view) {
            Object tag = view.getTag(ViewPropertyAnimatorCompat.LISTENER_TAG_ID);
            ViewPropertyAnimatorListener viewPropertyAnimatorListener = null;
            if (tag instanceof ViewPropertyAnimatorListener) {
                viewPropertyAnimatorListener = (ViewPropertyAnimatorListener) tag;
            }
            if (viewPropertyAnimatorListener != null) {
                viewPropertyAnimatorListener.onAnimationCancel(view);
            }
        }

        public void onAnimationEnd(View view) {
            if (this.mVpa.mOldLayerType > -1) {
                view.setLayerType(this.mVpa.mOldLayerType, (Paint) null);
                this.mVpa.mOldLayerType = -1;
            }
            if (Build.VERSION.SDK_INT >= 16 || !this.mAnimEndCalled) {
                if (this.mVpa.mEndAction != null) {
                    Runnable runnable = this.mVpa.mEndAction;
                    this.mVpa.mEndAction = null;
                    runnable.run();
                }
                Object tag = view.getTag(ViewPropertyAnimatorCompat.LISTENER_TAG_ID);
                ViewPropertyAnimatorListener viewPropertyAnimatorListener = null;
                if (tag instanceof ViewPropertyAnimatorListener) {
                    viewPropertyAnimatorListener = (ViewPropertyAnimatorListener) tag;
                }
                if (viewPropertyAnimatorListener != null) {
                    viewPropertyAnimatorListener.onAnimationEnd(view);
                }
                this.mAnimEndCalled = true;
            }
        }

        public void onAnimationStart(View view) {
            this.mAnimEndCalled = false;
            if (this.mVpa.mOldLayerType > -1) {
                view.setLayerType(2, (Paint) null);
            }
            if (this.mVpa.mStartAction != null) {
                Runnable runnable = this.mVpa.mStartAction;
                this.mVpa.mStartAction = null;
                runnable.run();
            }
            Object tag = view.getTag(ViewPropertyAnimatorCompat.LISTENER_TAG_ID);
            ViewPropertyAnimatorListener viewPropertyAnimatorListener = null;
            if (tag instanceof ViewPropertyAnimatorListener) {
                viewPropertyAnimatorListener = (ViewPropertyAnimatorListener) tag;
            }
            if (viewPropertyAnimatorListener != null) {
                viewPropertyAnimatorListener.onAnimationStart(view);
            }
        }
    }

    ViewPropertyAnimatorCompat(View view) {
        this.mView = new WeakReference<>(view);
    }

    private void setListenerInternal(final View view, final ViewPropertyAnimatorListener listener) {
        if (listener != null) {
            view.animate().setListener(new AnimatorListenerAdapter() {
                public void onAnimationCancel(Animator animation) {
                    listener.onAnimationCancel(view);
                }

                public void onAnimationEnd(Animator animation) {
                    listener.onAnimationEnd(view);
                }

                public void onAnimationStart(Animator animation) {
                    listener.onAnimationStart(view);
                }
            });
        } else {
            view.animate().setListener((Animator.AnimatorListener) null);
        }
    }

    public ViewPropertyAnimatorCompat alpha(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().alpha(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat alphaBy(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().alphaBy(value);
        }
        return this;
    }

    public void cancel() {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().cancel();
        }
    }

    public long getDuration() {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            return view2.animate().getDuration();
        }
        return 0;
    }

    public Interpolator getInterpolator() {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view == null || Build.VERSION.SDK_INT < 18) {
            return null;
        }
        return Api18Impl.getInterpolator(view2.animate());
    }

    public long getStartDelay() {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            return view2.animate().getStartDelay();
        }
        return 0;
    }

    public ViewPropertyAnimatorCompat rotation(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().rotation(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationBy(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().rotationBy(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationX(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().rotationX(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationXBy(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().rotationXBy(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationY(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().rotationY(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationYBy(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().rotationYBy(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat scaleX(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().scaleX(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat scaleXBy(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().scaleXBy(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat scaleY(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().scaleY(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat scaleYBy(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().scaleYBy(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat setDuration(long value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().setDuration(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat setInterpolator(Interpolator value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().setInterpolator(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat setListener(ViewPropertyAnimatorListener listener) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            if (Build.VERSION.SDK_INT >= 16) {
                setListenerInternal(view2, listener);
            } else {
                view2.setTag(LISTENER_TAG_ID, listener);
                setListenerInternal(view2, new ViewPropertyAnimatorListenerApi14(this));
            }
        }
        return this;
    }

    public ViewPropertyAnimatorCompat setStartDelay(long value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().setStartDelay(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat setUpdateListener(ViewPropertyAnimatorUpdateListener listener) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null && Build.VERSION.SDK_INT >= 19) {
            ViewPropertyAnimatorCompat$$ExternalSyntheticLambda0 viewPropertyAnimatorCompat$$ExternalSyntheticLambda0 = null;
            if (listener != null) {
                viewPropertyAnimatorCompat$$ExternalSyntheticLambda0 = new ViewPropertyAnimatorCompat$$ExternalSyntheticLambda0(listener, view2);
            }
            Api19Impl.setUpdateListener(view2.animate(), viewPropertyAnimatorCompat$$ExternalSyntheticLambda0);
        }
        return this;
    }

    public void start() {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().start();
        }
    }

    public ViewPropertyAnimatorCompat translationX(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().translationX(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationXBy(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().translationXBy(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationY(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().translationY(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationYBy(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().translationYBy(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationZ(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null && Build.VERSION.SDK_INT >= 21) {
            Api21Impl.translationZ(view2.animate(), value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationZBy(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null && Build.VERSION.SDK_INT >= 21) {
            Api21Impl.translationZBy(view2.animate(), value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat withEndAction(Runnable runnable) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            if (Build.VERSION.SDK_INT >= 16) {
                Api16Impl.withEndAction(view2.animate(), runnable);
            } else {
                setListenerInternal(view2, new ViewPropertyAnimatorListenerApi14(this));
                this.mEndAction = runnable;
            }
        }
        return this;
    }

    public ViewPropertyAnimatorCompat withLayer() {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            if (Build.VERSION.SDK_INT >= 16) {
                Api16Impl.withLayer(view2.animate());
            } else {
                this.mOldLayerType = view2.getLayerType();
                setListenerInternal(view2, new ViewPropertyAnimatorListenerApi14(this));
            }
        }
        return this;
    }

    public ViewPropertyAnimatorCompat withStartAction(Runnable runnable) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            if (Build.VERSION.SDK_INT >= 16) {
                Api16Impl.withStartAction(view2.animate(), runnable);
            } else {
                setListenerInternal(view2, new ViewPropertyAnimatorListenerApi14(this));
                this.mStartAction = runnable;
            }
        }
        return this;
    }

    public ViewPropertyAnimatorCompat x(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().x(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat xBy(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().xBy(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat y(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().y(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat yBy(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null) {
            view2.animate().yBy(value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat z(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null && Build.VERSION.SDK_INT >= 21) {
            Api21Impl.z(view2.animate(), value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat zBy(float value) {
        View view = (View) this.mView.get();
        View view2 = view;
        if (view != null && Build.VERSION.SDK_INT >= 21) {
            Api21Impl.zBy(view2.animate(), value);
        }
        return this;
    }
}
