package com.google.android.material.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class SlideDistanceProvider implements VisibilityAnimatorProvider {
    private static final int DEFAULT_DISTANCE = -1;
    private int slideDistance = -1;
    private int slideEdge;

    @Retention(RetentionPolicy.SOURCE)
    public @interface GravityFlag {
    }

    public SlideDistanceProvider(int slideEdge2) {
        this.slideEdge = slideEdge2;
    }

    private static Animator createTranslationAppearAnimator(View sceneRoot, View view, int slideEdge2, int slideDistance2) {
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        switch (slideEdge2) {
            case 3:
                return createTranslationXAnimator(view, ((float) slideDistance2) + translationX, translationX, translationX);
            case 5:
                return createTranslationXAnimator(view, translationX - ((float) slideDistance2), translationX, translationX);
            case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE:
                return createTranslationYAnimator(view, translationY - ((float) slideDistance2), translationY, translationY);
            case 80:
                return createTranslationYAnimator(view, ((float) slideDistance2) + translationY, translationY, translationY);
            case GravityCompat.START:
                return createTranslationXAnimator(view, isRtl(sceneRoot) ? ((float) slideDistance2) + translationX : translationX - ((float) slideDistance2), translationX, translationX);
            case GravityCompat.END:
                return createTranslationXAnimator(view, isRtl(sceneRoot) ? translationX - ((float) slideDistance2) : ((float) slideDistance2) + translationX, translationX, translationX);
            default:
                throw new IllegalArgumentException("Invalid slide direction: " + slideEdge2);
        }
    }

    private static Animator createTranslationDisappearAnimator(View sceneRoot, View view, int slideEdge2, int slideDistance2) {
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        switch (slideEdge2) {
            case 3:
                return createTranslationXAnimator(view, translationX, translationX - ((float) slideDistance2), translationX);
            case 5:
                return createTranslationXAnimator(view, translationX, ((float) slideDistance2) + translationX, translationX);
            case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE:
                return createTranslationYAnimator(view, translationY, ((float) slideDistance2) + translationY, translationY);
            case 80:
                return createTranslationYAnimator(view, translationY, translationY - ((float) slideDistance2), translationY);
            case GravityCompat.START:
                return createTranslationXAnimator(view, translationX, isRtl(sceneRoot) ? translationX - ((float) slideDistance2) : ((float) slideDistance2) + translationX, translationX);
            case GravityCompat.END:
                return createTranslationXAnimator(view, translationX, isRtl(sceneRoot) ? ((float) slideDistance2) + translationX : translationX - ((float) slideDistance2), translationX);
            default:
                throw new IllegalArgumentException("Invalid slide direction: " + slideEdge2);
        }
    }

    private static Animator createTranslationXAnimator(final View view, float startTranslation, float endTranslation, final float originalTranslation) {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(View.TRANSLATION_X, new float[]{startTranslation, endTranslation})});
        ofPropertyValuesHolder.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                view.setTranslationX(originalTranslation);
            }
        });
        return ofPropertyValuesHolder;
    }

    private static Animator createTranslationYAnimator(final View view, float startTranslation, float endTranslation, final float originalTranslation) {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, new float[]{startTranslation, endTranslation})});
        ofPropertyValuesHolder.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                view.setTranslationY(originalTranslation);
            }
        });
        return ofPropertyValuesHolder;
    }

    private int getSlideDistanceOrDefault(Context context) {
        int i = this.slideDistance;
        return i != -1 ? i : context.getResources().getDimensionPixelSize(R.dimen.mtrl_transition_shared_axis_slide_distance);
    }

    private static boolean isRtl(View view) {
        return ViewCompat.getLayoutDirection(view) == 1;
    }

    public Animator createAppear(ViewGroup sceneRoot, View view) {
        return createTranslationAppearAnimator(sceneRoot, view, this.slideEdge, getSlideDistanceOrDefault(view.getContext()));
    }

    public Animator createDisappear(ViewGroup sceneRoot, View view) {
        return createTranslationDisappearAnimator(sceneRoot, view, this.slideEdge, getSlideDistanceOrDefault(view.getContext()));
    }

    public int getSlideDistance() {
        return this.slideDistance;
    }

    public int getSlideEdge() {
        return this.slideEdge;
    }

    public void setSlideDistance(int slideDistance2) {
        if (slideDistance2 >= 0) {
            this.slideDistance = slideDistance2;
            return;
        }
        throw new IllegalArgumentException("Slide distance must be positive. If attempting to reverse the direction of the slide, use setSlideEdge(int) instead.");
    }

    public void setSlideEdge(int slideEdge2) {
        this.slideEdge = slideEdge2;
    }
}
