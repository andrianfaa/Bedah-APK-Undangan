package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.view.View;
import androidx.transition.Transition;

class TranslationAnimationCreator {

    private static class TransitionPositionListener extends AnimatorListenerAdapter implements Transition.TransitionListener {
        private final View mMovingView;
        private float mPausedX;
        private float mPausedY;
        private final int mStartX;
        private final int mStartY;
        private final float mTerminalX;
        private final float mTerminalY;
        private int[] mTransitionPosition;
        private final View mViewInHierarchy;

        TransitionPositionListener(View movingView, View viewInHierarchy, int startX, int startY, float terminalX, float terminalY) {
            this.mMovingView = movingView;
            this.mViewInHierarchy = viewInHierarchy;
            this.mStartX = startX - Math.round(movingView.getTranslationX());
            this.mStartY = startY - Math.round(movingView.getTranslationY());
            this.mTerminalX = terminalX;
            this.mTerminalY = terminalY;
            int[] iArr = (int[]) viewInHierarchy.getTag(R.id.transition_position);
            this.mTransitionPosition = iArr;
            if (iArr != null) {
                viewInHierarchy.setTag(R.id.transition_position, (Object) null);
            }
        }

        public void onAnimationCancel(Animator animation) {
            if (this.mTransitionPosition == null) {
                this.mTransitionPosition = new int[2];
            }
            this.mTransitionPosition[0] = Math.round(((float) this.mStartX) + this.mMovingView.getTranslationX());
            this.mTransitionPosition[1] = Math.round(((float) this.mStartY) + this.mMovingView.getTranslationY());
            this.mViewInHierarchy.setTag(R.id.transition_position, this.mTransitionPosition);
        }

        public void onAnimationPause(Animator animator) {
            this.mPausedX = this.mMovingView.getTranslationX();
            this.mPausedY = this.mMovingView.getTranslationY();
            this.mMovingView.setTranslationX(this.mTerminalX);
            this.mMovingView.setTranslationY(this.mTerminalY);
        }

        public void onAnimationResume(Animator animator) {
            this.mMovingView.setTranslationX(this.mPausedX);
            this.mMovingView.setTranslationY(this.mPausedY);
        }

        public void onTransitionCancel(Transition transition) {
        }

        public void onTransitionEnd(Transition transition) {
            this.mMovingView.setTranslationX(this.mTerminalX);
            this.mMovingView.setTranslationY(this.mTerminalY);
            transition.removeListener(this);
        }

        public void onTransitionPause(Transition transition) {
        }

        public void onTransitionResume(Transition transition) {
        }

        public void onTransitionStart(Transition transition) {
        }
    }

    private TranslationAnimationCreator() {
    }

    static Animator createAnimation(View view, TransitionValues values, int viewPosX, int viewPosY, float startX, float startY, float endX, float endY, TimeInterpolator interpolator, Transition transition) {
        float f;
        float f2;
        View view2 = view;
        TransitionValues transitionValues = values;
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        int[] iArr = (int[]) transitionValues.view.getTag(R.id.transition_position);
        if (iArr != null) {
            f2 = ((float) (iArr[0] - viewPosX)) + translationX;
            f = ((float) (iArr[1] - viewPosY)) + translationY;
        } else {
            f2 = startX;
            f = startY;
        }
        int round = viewPosX + Math.round(f2 - translationX);
        int round2 = viewPosY + Math.round(f - translationY);
        view2.setTranslationX(f2);
        view2.setTranslationY(f);
        if (f2 == endX && f == endY) {
            return null;
        }
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view2, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(View.TRANSLATION_X, new float[]{f2, endX}), PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, new float[]{f, endY})});
        TransitionPositionListener transitionPositionListener = new TransitionPositionListener(view, transitionValues.view, round, round2, translationX, translationY);
        transition.addListener(transitionPositionListener);
        ofPropertyValuesHolder.addListener(transitionPositionListener);
        AnimatorUtils.addPauseListener(ofPropertyValuesHolder, transitionPositionListener);
        ofPropertyValuesHolder.setInterpolator(interpolator);
        return ofPropertyValuesHolder;
    }
}
