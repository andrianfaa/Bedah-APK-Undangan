package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import androidx.core.os.CancellationSignal;
import androidx.core.view.OneShotPreDrawListener;
import androidx.fragment.R;
import androidx.fragment.app.FragmentTransition;

class FragmentAnim {

    static class AnimationOrAnimator {
        public final Animation animation;
        public final Animator animator;

        AnimationOrAnimator(Animator animator2) {
            this.animation = null;
            this.animator = animator2;
            if (animator2 == null) {
                throw new IllegalStateException("Animator cannot be null");
            }
        }

        AnimationOrAnimator(Animation animation2) {
            this.animation = animation2;
            this.animator = null;
            if (animation2 == null) {
                throw new IllegalStateException("Animation cannot be null");
            }
        }
    }

    static class EndViewTransitionAnimation extends AnimationSet implements Runnable {
        private boolean mAnimating = true;
        private final View mChild;
        private boolean mEnded;
        private final ViewGroup mParent;
        private boolean mTransitionEnded;

        EndViewTransitionAnimation(Animation animation, ViewGroup parent, View child) {
            super(false);
            this.mParent = parent;
            this.mChild = child;
            addAnimation(animation);
            parent.post(this);
        }

        public boolean getTransformation(long currentTime, Transformation t) {
            this.mAnimating = true;
            if (this.mEnded) {
                return true ^ this.mTransitionEnded;
            }
            if (!super.getTransformation(currentTime, t)) {
                this.mEnded = true;
                OneShotPreDrawListener.add(this.mParent, this);
            }
            return true;
        }

        public boolean getTransformation(long currentTime, Transformation outTransformation, float scale) {
            this.mAnimating = true;
            if (this.mEnded) {
                return true ^ this.mTransitionEnded;
            }
            if (!super.getTransformation(currentTime, outTransformation, scale)) {
                this.mEnded = true;
                OneShotPreDrawListener.add(this.mParent, this);
            }
            return true;
        }

        public void run() {
            if (this.mEnded || !this.mAnimating) {
                this.mParent.endViewTransition(this.mChild);
                this.mTransitionEnded = true;
                return;
            }
            this.mAnimating = false;
            this.mParent.post(this);
        }
    }

    private FragmentAnim() {
    }

    static void animateRemoveFragment(final Fragment fragment, AnimationOrAnimator anim, final FragmentTransition.Callback callback) {
        View view = fragment.mView;
        final ViewGroup viewGroup = fragment.mContainer;
        viewGroup.startViewTransition(view);
        final CancellationSignal cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            public void onCancel() {
                if (Fragment.this.getAnimatingAway() != null) {
                    View animatingAway = Fragment.this.getAnimatingAway();
                    Fragment.this.setAnimatingAway((View) null);
                    animatingAway.clearAnimation();
                }
                Fragment.this.setAnimator((Animator) null);
            }
        });
        callback.onStart(fragment, cancellationSignal);
        if (anim.animation != null) {
            EndViewTransitionAnimation endViewTransitionAnimation = new EndViewTransitionAnimation(anim.animation, viewGroup, view);
            fragment.setAnimatingAway(fragment.mView);
            endViewTransitionAnimation.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    viewGroup.post(new Runnable() {
                        public void run() {
                            if (fragment.getAnimatingAway() != null) {
                                fragment.setAnimatingAway((View) null);
                                callback.onComplete(fragment, cancellationSignal);
                            }
                        }
                    });
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }
            });
            fragment.mView.startAnimation(endViewTransitionAnimation);
            return;
        }
        Animator animator = anim.animator;
        fragment.setAnimator(anim.animator);
        final ViewGroup viewGroup2 = viewGroup;
        final View view2 = view;
        final Fragment fragment2 = fragment;
        final FragmentTransition.Callback callback2 = callback;
        final CancellationSignal cancellationSignal2 = cancellationSignal;
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                viewGroup2.endViewTransition(view2);
                Animator animator = fragment2.getAnimator();
                fragment2.setAnimator((Animator) null);
                if (animator != null && viewGroup2.indexOfChild(view2) < 0) {
                    callback2.onComplete(fragment2, cancellationSignal2);
                }
            }
        });
        animator.setTarget(fragment.mView);
        animator.start();
    }

    private static int getNextAnim(Fragment fragment, boolean enter, boolean isPop) {
        return isPop ? enter ? fragment.getPopEnterAnim() : fragment.getPopExitAnim() : enter ? fragment.getEnterAnim() : fragment.getExitAnim();
    }

    static AnimationOrAnimator loadAnimation(Context context, Fragment fragment, boolean enter, boolean isPop) {
        int nextTransition = fragment.getNextTransition();
        int nextAnim = getNextAnim(fragment, enter, isPop);
        fragment.setAnimations(0, 0, 0, 0);
        if (!(fragment.mContainer == null || fragment.mContainer.getTag(R.id.visible_removing_fragment_view_tag) == null)) {
            fragment.mContainer.setTag(R.id.visible_removing_fragment_view_tag, (Object) null);
        }
        if (fragment.mContainer != null && fragment.mContainer.getLayoutTransition() != null) {
            return null;
        }
        Animation onCreateAnimation = fragment.onCreateAnimation(nextTransition, enter, nextAnim);
        if (onCreateAnimation != null) {
            return new AnimationOrAnimator(onCreateAnimation);
        }
        Animator onCreateAnimator = fragment.onCreateAnimator(nextTransition, enter, nextAnim);
        if (onCreateAnimator != null) {
            return new AnimationOrAnimator(onCreateAnimator);
        }
        if (nextAnim == 0 && nextTransition != 0) {
            nextAnim = transitToAnimResourceId(nextTransition, enter);
        }
        if (nextAnim != 0) {
            boolean equals = "anim".equals(context.getResources().getResourceTypeName(nextAnim));
            boolean z = false;
            if (equals) {
                try {
                    Animation loadAnimation = AnimationUtils.loadAnimation(context, nextAnim);
                    if (loadAnimation != null) {
                        return new AnimationOrAnimator(loadAnimation);
                    }
                    z = true;
                } catch (Resources.NotFoundException e) {
                    throw e;
                } catch (RuntimeException e2) {
                }
            }
            if (!z) {
                try {
                    Animator loadAnimator = AnimatorInflater.loadAnimator(context, nextAnim);
                    if (loadAnimator != null) {
                        return new AnimationOrAnimator(loadAnimator);
                    }
                } catch (RuntimeException e3) {
                    if (!equals) {
                        Animation loadAnimation2 = AnimationUtils.loadAnimation(context, nextAnim);
                        if (loadAnimation2 != null) {
                            return new AnimationOrAnimator(loadAnimation2);
                        }
                    } else {
                        throw e3;
                    }
                }
            }
        }
        return null;
    }

    private static int transitToAnimResourceId(int transit, boolean enter) {
        switch (transit) {
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN /*4097*/:
                return enter ? R.animator.fragment_open_enter : R.animator.fragment_open_exit;
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE /*4099*/:
                return enter ? R.animator.fragment_fade_enter : R.animator.fragment_fade_exit;
            case 8194:
                return enter ? R.animator.fragment_close_enter : R.animator.fragment_close_exit;
            default:
                return -1;
        }
    }
}
