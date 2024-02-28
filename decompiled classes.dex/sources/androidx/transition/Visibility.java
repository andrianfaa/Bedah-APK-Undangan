package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.transition.AnimatorUtils;
import androidx.transition.Transition;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class Visibility extends Transition {
    public static final int MODE_IN = 1;
    public static final int MODE_OUT = 2;
    private static final String PROPNAME_PARENT = "android:visibility:parent";
    private static final String PROPNAME_SCREEN_LOCATION = "android:visibility:screenLocation";
    static final String PROPNAME_VISIBILITY = "android:visibility:visibility";
    private static final String[] sTransitionProperties = {PROPNAME_VISIBILITY, PROPNAME_PARENT};
    private int mMode = 3;

    private static class DisappearListener extends AnimatorListenerAdapter implements Transition.TransitionListener, AnimatorUtils.AnimatorPauseListenerCompat {
        boolean mCanceled = false;
        private final int mFinalVisibility;
        private boolean mLayoutSuppressed;
        private final ViewGroup mParent;
        private final boolean mSuppressLayout;
        private final View mView;

        DisappearListener(View view, int finalVisibility, boolean suppressLayout) {
            this.mView = view;
            this.mFinalVisibility = finalVisibility;
            this.mParent = (ViewGroup) view.getParent();
            this.mSuppressLayout = suppressLayout;
            suppressLayout(true);
        }

        private void hideViewWhenNotCanceled() {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
                ViewGroup viewGroup = this.mParent;
                if (viewGroup != null) {
                    viewGroup.invalidate();
                }
            }
            suppressLayout(false);
        }

        private void suppressLayout(boolean suppress) {
            ViewGroup viewGroup;
            if (this.mSuppressLayout && this.mLayoutSuppressed != suppress && (viewGroup = this.mParent) != null) {
                this.mLayoutSuppressed = suppress;
                ViewGroupUtils.suppressLayout(viewGroup, suppress);
            }
        }

        public void onAnimationCancel(Animator animation) {
            this.mCanceled = true;
        }

        public void onAnimationEnd(Animator animation) {
            hideViewWhenNotCanceled();
        }

        public void onAnimationPause(Animator animation) {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
            }
        }

        public void onAnimationRepeat(Animator animation) {
        }

        public void onAnimationResume(Animator animation) {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, 0);
            }
        }

        public void onAnimationStart(Animator animation) {
        }

        public void onTransitionCancel(Transition transition) {
        }

        public void onTransitionEnd(Transition transition) {
            hideViewWhenNotCanceled();
            transition.removeListener(this);
        }

        public void onTransitionPause(Transition transition) {
            suppressLayout(false);
        }

        public void onTransitionResume(Transition transition) {
            suppressLayout(true);
        }

        public void onTransitionStart(Transition transition) {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    private static class VisibilityInfo {
        ViewGroup mEndParent;
        int mEndVisibility;
        boolean mFadeIn;
        ViewGroup mStartParent;
        int mStartVisibility;
        boolean mVisibilityChange;

        VisibilityInfo() {
        }
    }

    public Visibility() {
    }

    public Visibility(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, Styleable.VISIBILITY_TRANSITION);
        int namedInt = TypedArrayUtils.getNamedInt(obtainStyledAttributes, (XmlResourceParser) attrs, "transitionVisibilityMode", 0, 0);
        obtainStyledAttributes.recycle();
        if (namedInt != 0) {
            setMode(namedInt);
        }
    }

    private void captureValues(TransitionValues transitionValues) {
        transitionValues.values.put(PROPNAME_VISIBILITY, Integer.valueOf(transitionValues.view.getVisibility()));
        transitionValues.values.put(PROPNAME_PARENT, transitionValues.view.getParent());
        int[] iArr = new int[2];
        transitionValues.view.getLocationOnScreen(iArr);
        transitionValues.values.put(PROPNAME_SCREEN_LOCATION, iArr);
    }

    private VisibilityInfo getVisibilityChangeInfo(TransitionValues startValues, TransitionValues endValues) {
        VisibilityInfo visibilityInfo = new VisibilityInfo();
        visibilityInfo.mVisibilityChange = false;
        visibilityInfo.mFadeIn = false;
        if (startValues == null || !startValues.values.containsKey(PROPNAME_VISIBILITY)) {
            visibilityInfo.mStartVisibility = -1;
            visibilityInfo.mStartParent = null;
        } else {
            visibilityInfo.mStartVisibility = ((Integer) startValues.values.get(PROPNAME_VISIBILITY)).intValue();
            visibilityInfo.mStartParent = (ViewGroup) startValues.values.get(PROPNAME_PARENT);
        }
        if (endValues == null || !endValues.values.containsKey(PROPNAME_VISIBILITY)) {
            visibilityInfo.mEndVisibility = -1;
            visibilityInfo.mEndParent = null;
        } else {
            visibilityInfo.mEndVisibility = ((Integer) endValues.values.get(PROPNAME_VISIBILITY)).intValue();
            visibilityInfo.mEndParent = (ViewGroup) endValues.values.get(PROPNAME_PARENT);
        }
        if (startValues == null || endValues == null) {
            if (startValues == null && visibilityInfo.mEndVisibility == 0) {
                visibilityInfo.mFadeIn = true;
                visibilityInfo.mVisibilityChange = true;
            } else if (endValues == null && visibilityInfo.mStartVisibility == 0) {
                visibilityInfo.mFadeIn = false;
                visibilityInfo.mVisibilityChange = true;
            }
        } else if (visibilityInfo.mStartVisibility == visibilityInfo.mEndVisibility && visibilityInfo.mStartParent == visibilityInfo.mEndParent) {
            return visibilityInfo;
        } else {
            if (visibilityInfo.mStartVisibility != visibilityInfo.mEndVisibility) {
                if (visibilityInfo.mStartVisibility == 0) {
                    visibilityInfo.mFadeIn = false;
                    visibilityInfo.mVisibilityChange = true;
                } else if (visibilityInfo.mEndVisibility == 0) {
                    visibilityInfo.mFadeIn = true;
                    visibilityInfo.mVisibilityChange = true;
                }
            } else if (visibilityInfo.mEndParent == null) {
                visibilityInfo.mFadeIn = false;
                visibilityInfo.mVisibilityChange = true;
            } else if (visibilityInfo.mStartParent == null) {
                visibilityInfo.mFadeIn = true;
                visibilityInfo.mVisibilityChange = true;
            }
        }
        return visibilityInfo;
    }

    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        VisibilityInfo visibilityChangeInfo = getVisibilityChangeInfo(startValues, endValues);
        if (!visibilityChangeInfo.mVisibilityChange) {
            return null;
        }
        if (visibilityChangeInfo.mStartParent == null && visibilityChangeInfo.mEndParent == null) {
            return null;
        }
        if (visibilityChangeInfo.mFadeIn) {
            return onAppear(sceneRoot, startValues, visibilityChangeInfo.mStartVisibility, endValues, visibilityChangeInfo.mEndVisibility);
        }
        return onDisappear(sceneRoot, startValues, visibilityChangeInfo.mStartVisibility, endValues, visibilityChangeInfo.mEndVisibility);
    }

    public int getMode() {
        return this.mMode;
    }

    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    public boolean isTransitionRequired(TransitionValues startValues, TransitionValues newValues) {
        if (startValues == null && newValues == null) {
            return false;
        }
        if (startValues != null && newValues != null && newValues.values.containsKey(PROPNAME_VISIBILITY) != startValues.values.containsKey(PROPNAME_VISIBILITY)) {
            return false;
        }
        VisibilityInfo visibilityChangeInfo = getVisibilityChangeInfo(startValues, newValues);
        if (visibilityChangeInfo.mVisibilityChange) {
            return visibilityChangeInfo.mStartVisibility == 0 || visibilityChangeInfo.mEndVisibility == 0;
        }
        return false;
    }

    public boolean isVisible(TransitionValues values) {
        if (values == null) {
            return false;
        }
        return ((Integer) values.values.get(PROPNAME_VISIBILITY)).intValue() == 0 && ((View) values.values.get(PROPNAME_PARENT)) != null;
    }

    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return null;
    }

    public Animator onAppear(ViewGroup sceneRoot, TransitionValues startValues, int startVisibility, TransitionValues endValues, int endVisibility) {
        if ((this.mMode & 1) != 1 || endValues == null) {
            return null;
        }
        if (startValues == null) {
            View view = (View) endValues.view.getParent();
            if (getVisibilityChangeInfo(getMatchedTransitionValues(view, false), getTransitionValues(view, false)).mVisibilityChange) {
                return null;
            }
        }
        return onAppear(sceneRoot, endValues.view, startValues, endValues);
    }

    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return null;
    }

    public Animator onDisappear(ViewGroup sceneRoot, TransitionValues startValues, int startVisibility, TransitionValues endValues, int endVisibility) {
        ViewGroup viewGroup = sceneRoot;
        TransitionValues transitionValues = startValues;
        TransitionValues transitionValues2 = endValues;
        int i = endVisibility;
        if ((this.mMode & 2) != 2 || transitionValues == null) {
            return null;
        }
        final View view = transitionValues.view;
        View view2 = transitionValues2 != null ? transitionValues2.view : null;
        View view3 = null;
        View view4 = null;
        boolean z = false;
        View view5 = (View) view.getTag(R.id.save_overlay_view);
        if (view5 != null) {
            view3 = view5;
            z = true;
        } else {
            boolean z2 = false;
            if (view2 == null || view2.getParent() == null) {
                if (view2 != null) {
                    view3 = view2;
                } else {
                    z2 = true;
                }
            } else if (i == 4) {
                view4 = view2;
            } else if (view == view2) {
                view4 = view2;
            } else {
                z2 = true;
            }
            if (z2) {
                if (view.getParent() == null) {
                    view3 = view;
                } else if (view.getParent() instanceof View) {
                    View view6 = (View) view.getParent();
                    TransitionValues transitionValues3 = getTransitionValues(view6, true);
                    TransitionValues matchedTransitionValues = getMatchedTransitionValues(view6, true);
                    TransitionValues transitionValues4 = matchedTransitionValues;
                    if (!getVisibilityChangeInfo(transitionValues3, matchedTransitionValues).mVisibilityChange) {
                        view3 = TransitionUtils.copyViewImage(viewGroup, view, view6);
                    } else {
                        int id = view6.getId();
                        if (view6.getParent() == null) {
                            TransitionValues transitionValues5 = transitionValues3;
                            if (!(id == -1 || viewGroup.findViewById(id) == null || !this.mCanRemoveViews)) {
                                view3 = view;
                            }
                        } else {
                            TransitionValues transitionValues6 = transitionValues3;
                        }
                    }
                }
            }
        }
        if (view3 != null) {
            if (!z) {
                int[] iArr = (int[]) transitionValues.values.get(PROPNAME_SCREEN_LOCATION);
                int i2 = iArr[0];
                int i3 = iArr[1];
                int[] iArr2 = new int[2];
                viewGroup.getLocationOnScreen(iArr2);
                view3.offsetLeftAndRight((i2 - iArr2[0]) - view3.getLeft());
                view3.offsetTopAndBottom((i3 - iArr2[1]) - view3.getTop());
                ViewGroupUtils.getOverlay(sceneRoot).add(view3);
            }
            Animator onDisappear = onDisappear(viewGroup, view3, transitionValues, transitionValues2);
            if (!z) {
                if (onDisappear == null) {
                    ViewGroupUtils.getOverlay(sceneRoot).remove(view3);
                } else {
                    view.setTag(R.id.save_overlay_view, view3);
                    final View view7 = view3;
                    final ViewGroup viewGroup2 = sceneRoot;
                    addListener(new TransitionListenerAdapter() {
                        public void onTransitionEnd(Transition transition) {
                            view.setTag(R.id.save_overlay_view, (Object) null);
                            ViewGroupUtils.getOverlay(viewGroup2).remove(view7);
                            transition.removeListener(this);
                        }

                        public void onTransitionPause(Transition transition) {
                            ViewGroupUtils.getOverlay(viewGroup2).remove(view7);
                        }

                        public void onTransitionResume(Transition transition) {
                            if (view7.getParent() == null) {
                                ViewGroupUtils.getOverlay(viewGroup2).add(view7);
                            } else {
                                Visibility.this.cancel();
                            }
                        }
                    });
                }
            }
            return onDisappear;
        } else if (view4 == null) {
            return null;
        } else {
            int visibility = view4.getVisibility();
            ViewUtils.setTransitionVisibility(view4, 0);
            Animator onDisappear2 = onDisappear(viewGroup, view4, transitionValues, transitionValues2);
            if (onDisappear2 != null) {
                DisappearListener disappearListener = new DisappearListener(view4, i, true);
                onDisappear2.addListener(disappearListener);
                AnimatorUtils.addPauseListener(onDisappear2, disappearListener);
                addListener(disappearListener);
            } else {
                ViewUtils.setTransitionVisibility(view4, visibility);
            }
            return onDisappear2;
        }
    }

    public void setMode(int mode) {
        if ((mode & -4) == 0) {
            this.mMode = mode;
            return;
        }
        throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
    }
}
