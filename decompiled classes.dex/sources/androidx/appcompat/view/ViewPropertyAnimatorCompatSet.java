package androidx.appcompat.view;

import android.view.View;
import android.view.animation.Interpolator;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import java.util.ArrayList;
import java.util.Iterator;

public class ViewPropertyAnimatorCompatSet {
    final ArrayList<ViewPropertyAnimatorCompat> mAnimators = new ArrayList<>();
    private long mDuration = -1;
    private Interpolator mInterpolator;
    private boolean mIsStarted;
    ViewPropertyAnimatorListener mListener;
    private final ViewPropertyAnimatorListenerAdapter mProxyListener = new ViewPropertyAnimatorListenerAdapter() {
        private int mProxyEndCount = 0;
        private boolean mProxyStarted = false;

        public void onAnimationEnd(View view) {
            int i = this.mProxyEndCount + 1;
            this.mProxyEndCount = i;
            if (i == ViewPropertyAnimatorCompatSet.this.mAnimators.size()) {
                if (ViewPropertyAnimatorCompatSet.this.mListener != null) {
                    ViewPropertyAnimatorCompatSet.this.mListener.onAnimationEnd((View) null);
                }
                onEnd();
            }
        }

        public void onAnimationStart(View view) {
            if (!this.mProxyStarted) {
                this.mProxyStarted = true;
                if (ViewPropertyAnimatorCompatSet.this.mListener != null) {
                    ViewPropertyAnimatorCompatSet.this.mListener.onAnimationStart((View) null);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void onEnd() {
            this.mProxyEndCount = 0;
            this.mProxyStarted = false;
            ViewPropertyAnimatorCompatSet.this.onAnimationsEnded();
        }
    };

    public void cancel() {
        if (this.mIsStarted) {
            Iterator<ViewPropertyAnimatorCompat> it = this.mAnimators.iterator();
            while (it.hasNext()) {
                it.next().cancel();
            }
            this.mIsStarted = false;
        }
    }

    /* access modifiers changed from: package-private */
    public void onAnimationsEnded() {
        this.mIsStarted = false;
    }

    public ViewPropertyAnimatorCompatSet play(ViewPropertyAnimatorCompat animator) {
        if (!this.mIsStarted) {
            this.mAnimators.add(animator);
        }
        return this;
    }

    public ViewPropertyAnimatorCompatSet playSequentially(ViewPropertyAnimatorCompat anim1, ViewPropertyAnimatorCompat anim2) {
        this.mAnimators.add(anim1);
        anim2.setStartDelay(anim1.getDuration());
        this.mAnimators.add(anim2);
        return this;
    }

    public ViewPropertyAnimatorCompatSet setDuration(long duration) {
        if (!this.mIsStarted) {
            this.mDuration = duration;
        }
        return this;
    }

    public ViewPropertyAnimatorCompatSet setInterpolator(Interpolator interpolator) {
        if (!this.mIsStarted) {
            this.mInterpolator = interpolator;
        }
        return this;
    }

    public ViewPropertyAnimatorCompatSet setListener(ViewPropertyAnimatorListener listener) {
        if (!this.mIsStarted) {
            this.mListener = listener;
        }
        return this;
    }

    public void start() {
        if (!this.mIsStarted) {
            Iterator<ViewPropertyAnimatorCompat> it = this.mAnimators.iterator();
            while (it.hasNext()) {
                ViewPropertyAnimatorCompat next = it.next();
                long j = this.mDuration;
                if (j >= 0) {
                    next.setDuration(j);
                }
                Interpolator interpolator = this.mInterpolator;
                if (interpolator != null) {
                    next.setInterpolator(interpolator);
                }
                if (this.mListener != null) {
                    next.setListener(this.mProxyListener);
                }
                next.start();
            }
            this.mIsStarted = true;
        }
    }
}
