package com.google.android.material.transformation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.animation.MotionTiming;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class FabTransformationScrimBehavior extends ExpandableTransformationBehavior {
    public static final long COLLAPSE_DELAY = 0;
    public static final long COLLAPSE_DURATION = 150;
    public static final long EXPAND_DELAY = 75;
    public static final long EXPAND_DURATION = 150;
    private final MotionTiming collapseTiming = new MotionTiming(0, 150);
    private final MotionTiming expandTiming = new MotionTiming(75, 150);

    public FabTransformationScrimBehavior() {
    }

    public FabTransformationScrimBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void createScrimAnimation(View child, boolean expanded, boolean currentlyAnimating, List<Animator> list, List<Animator.AnimatorListener> list2) {
        ObjectAnimator objectAnimator;
        MotionTiming motionTiming = expanded ? this.expandTiming : this.collapseTiming;
        if (expanded) {
            if (!currentlyAnimating) {
                child.setAlpha(0.0f);
            }
            objectAnimator = ObjectAnimator.ofFloat(child, View.ALPHA, new float[]{1.0f});
        } else {
            objectAnimator = ObjectAnimator.ofFloat(child, View.ALPHA, new float[]{0.0f});
        }
        motionTiming.apply(objectAnimator);
        list.add(objectAnimator);
    }

    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof FloatingActionButton;
    }

    /* access modifiers changed from: protected */
    public AnimatorSet onCreateExpandedStateChangeAnimation(View dependency, final View child, final boolean expanded, boolean isAnimating) {
        ArrayList arrayList = new ArrayList();
        createScrimAnimation(child, expanded, isAnimating, arrayList, new ArrayList());
        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSetCompat.playTogether(animatorSet, arrayList);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if (!expanded) {
                    child.setVisibility(4);
                }
            }

            public void onAnimationStart(Animator animation) {
                if (expanded) {
                    child.setVisibility(0);
                }
            }
        });
        return animatorSet;
    }

    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        return super.onTouchEvent(parent, child, ev);
    }
}
