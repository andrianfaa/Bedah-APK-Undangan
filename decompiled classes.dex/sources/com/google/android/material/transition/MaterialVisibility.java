package com.google.android.material.transition;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.transition.TransitionValues;
import androidx.transition.Visibility;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.transition.VisibilityAnimatorProvider;
import java.util.ArrayList;
import java.util.List;

abstract class MaterialVisibility<P extends VisibilityAnimatorProvider> extends Visibility {
    private final List<VisibilityAnimatorProvider> additionalAnimatorProviders = new ArrayList();
    private final P primaryAnimatorProvider;
    private VisibilityAnimatorProvider secondaryAnimatorProvider;

    protected MaterialVisibility(P p, VisibilityAnimatorProvider secondaryAnimatorProvider2) {
        this.primaryAnimatorProvider = p;
        this.secondaryAnimatorProvider = secondaryAnimatorProvider2;
    }

    private static void addAnimatorIfNeeded(List<Animator> list, VisibilityAnimatorProvider animatorProvider, ViewGroup sceneRoot, View view, boolean appearing) {
        if (animatorProvider != null) {
            Animator createAppear = appearing ? animatorProvider.createAppear(sceneRoot, view) : animatorProvider.createDisappear(sceneRoot, view);
            if (createAppear != null) {
                list.add(createAppear);
            }
        }
    }

    private Animator createAnimator(ViewGroup sceneRoot, View view, boolean appearing) {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        addAnimatorIfNeeded(arrayList, this.primaryAnimatorProvider, sceneRoot, view, appearing);
        addAnimatorIfNeeded(arrayList, this.secondaryAnimatorProvider, sceneRoot, view, appearing);
        for (VisibilityAnimatorProvider addAnimatorIfNeeded : this.additionalAnimatorProviders) {
            addAnimatorIfNeeded(arrayList, addAnimatorIfNeeded, sceneRoot, view, appearing);
        }
        maybeApplyThemeValues(sceneRoot.getContext(), appearing);
        AnimatorSetCompat.playTogether(animatorSet, arrayList);
        return animatorSet;
    }

    private void maybeApplyThemeValues(Context context, boolean appearing) {
        TransitionUtils.maybeApplyThemeDuration(this, context, getDurationThemeAttrResId(appearing));
        TransitionUtils.maybeApplyThemeInterpolator(this, context, getEasingThemeAttrResId(appearing), getDefaultEasingInterpolator(appearing));
    }

    public void addAdditionalAnimatorProvider(VisibilityAnimatorProvider additionalAnimatorProvider) {
        this.additionalAnimatorProviders.add(additionalAnimatorProvider);
    }

    public void clearAdditionalAnimatorProvider() {
        this.additionalAnimatorProviders.clear();
    }

    /* access modifiers changed from: package-private */
    public TimeInterpolator getDefaultEasingInterpolator(boolean appearing) {
        return AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR;
    }

    /* access modifiers changed from: package-private */
    public int getDurationThemeAttrResId(boolean appearing) {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int getEasingThemeAttrResId(boolean appearing) {
        return 0;
    }

    public P getPrimaryAnimatorProvider() {
        return this.primaryAnimatorProvider;
    }

    public VisibilityAnimatorProvider getSecondaryAnimatorProvider() {
        return this.secondaryAnimatorProvider;
    }

    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return createAnimator(sceneRoot, view, true);
    }

    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return createAnimator(sceneRoot, view, false);
    }

    public boolean removeAdditionalAnimatorProvider(VisibilityAnimatorProvider additionalAnimatorProvider) {
        return this.additionalAnimatorProviders.remove(additionalAnimatorProvider);
    }

    public void setSecondaryAnimatorProvider(VisibilityAnimatorProvider secondaryAnimatorProvider2) {
        this.secondaryAnimatorProvider = secondaryAnimatorProvider2;
    }
}
