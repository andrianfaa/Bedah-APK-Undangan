package androidx.constraintlayout.motion.widget;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.constraintlayout.motion.widget.ViewTransition;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.SharedValues;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class ViewTransitionController {
    private String TAG = "ViewTransitionController";
    ArrayList<ViewTransition.Animate> animations;
    /* access modifiers changed from: private */
    public final MotionLayout mMotionLayout;
    private HashSet<View> mRelatedViews;
    ArrayList<ViewTransition.Animate> removeList = new ArrayList<>();
    private ArrayList<ViewTransition> viewTransitions = new ArrayList<>();

    public ViewTransitionController(MotionLayout layout) {
        this.mMotionLayout = layout;
    }

    private void listenForSharedVariable(ViewTransition viewTransition, boolean isSet) {
        int sharedValueID = viewTransition.getSharedValueID();
        final ViewTransition viewTransition2 = viewTransition;
        final int i = sharedValueID;
        final boolean z = isSet;
        final int sharedValue = viewTransition.getSharedValue();
        ConstraintLayout.getSharedValues().addListener(viewTransition.getSharedValueID(), new SharedValues.SharedValuesListener() {
            public void onNewValue(int id, int value, int oldValue) {
                int i = value;
                int sharedValueCurrent = viewTransition2.getSharedValueCurrent();
                viewTransition2.setSharedValueCurrent(i);
                if (i == id && sharedValueCurrent != i) {
                    if (z) {
                        if (sharedValue == i) {
                            int childCount = ViewTransitionController.this.mMotionLayout.getChildCount();
                            for (int i2 = 0; i2 < childCount; i2++) {
                                View childAt = ViewTransitionController.this.mMotionLayout.getChildAt(i2);
                                if (viewTransition2.matchesView(childAt)) {
                                    int currentState = ViewTransitionController.this.mMotionLayout.getCurrentState();
                                    ConstraintSet constraintSet = ViewTransitionController.this.mMotionLayout.getConstraintSet(currentState);
                                    ViewTransition viewTransition = viewTransition2;
                                    ViewTransitionController viewTransitionController = ViewTransitionController.this;
                                    viewTransition.applyTransition(viewTransitionController, viewTransitionController.mMotionLayout, currentState, constraintSet, childAt);
                                }
                            }
                        }
                    } else if (sharedValue != i) {
                        int childCount2 = ViewTransitionController.this.mMotionLayout.getChildCount();
                        for (int i3 = 0; i3 < childCount2; i3++) {
                            View childAt2 = ViewTransitionController.this.mMotionLayout.getChildAt(i3);
                            if (viewTransition2.matchesView(childAt2)) {
                                int currentState2 = ViewTransitionController.this.mMotionLayout.getCurrentState();
                                ConstraintSet constraintSet2 = ViewTransitionController.this.mMotionLayout.getConstraintSet(currentState2);
                                ViewTransition viewTransition2 = viewTransition2;
                                ViewTransitionController viewTransitionController2 = ViewTransitionController.this;
                                viewTransition2.applyTransition(viewTransitionController2, viewTransitionController2.mMotionLayout, currentState2, constraintSet2, childAt2);
                            }
                        }
                    }
                }
            }
        });
    }

    private void viewTransition(ViewTransition vt, View... view) {
        int currentState = this.mMotionLayout.getCurrentState();
        if (vt.mViewTransitionMode == 2) {
            vt.applyTransition(this, this.mMotionLayout, currentState, (ConstraintSet) null, view);
        } else if (currentState == -1) {
            Log.w(this.TAG, "No support for ViewTransition within transition yet. Currently: " + this.mMotionLayout.toString());
        } else {
            ConstraintSet constraintSet = this.mMotionLayout.getConstraintSet(currentState);
            if (constraintSet != null) {
                vt.applyTransition(this, this.mMotionLayout, currentState, constraintSet, view);
            }
        }
    }

    public void add(ViewTransition viewTransition) {
        this.viewTransitions.add(viewTransition);
        this.mRelatedViews = null;
        if (viewTransition.getStateTransition() == 4) {
            listenForSharedVariable(viewTransition, true);
        } else if (viewTransition.getStateTransition() == 5) {
            listenForSharedVariable(viewTransition, false);
        }
    }

    /* access modifiers changed from: package-private */
    public void addAnimation(ViewTransition.Animate animation) {
        if (this.animations == null) {
            this.animations = new ArrayList<>();
        }
        this.animations.add(animation);
    }

    /* access modifiers changed from: package-private */
    public void animate() {
        ArrayList<ViewTransition.Animate> arrayList = this.animations;
        if (arrayList != null) {
            Iterator<ViewTransition.Animate> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().mutate();
            }
            this.animations.removeAll(this.removeList);
            this.removeList.clear();
            if (this.animations.isEmpty()) {
                this.animations = null;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean applyViewTransition(int viewTransitionId, MotionController motionController) {
        Iterator<ViewTransition> it = this.viewTransitions.iterator();
        while (it.hasNext()) {
            ViewTransition next = it.next();
            if (next.getId() == viewTransitionId) {
                next.mKeyFrames.addAllFrames(motionController);
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void enableViewTransition(int id, boolean enable) {
        Iterator<ViewTransition> it = this.viewTransitions.iterator();
        while (it.hasNext()) {
            ViewTransition next = it.next();
            if (next.getId() == id) {
                next.setEnabled(enable);
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void invalidate() {
        this.mMotionLayout.invalidate();
    }

    /* access modifiers changed from: package-private */
    public boolean isViewTransitionEnabled(int id) {
        Iterator<ViewTransition> it = this.viewTransitions.iterator();
        while (it.hasNext()) {
            ViewTransition next = it.next();
            if (next.getId() == id) {
                return next.isEnabled();
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void remove(int id) {
        ViewTransition viewTransition = null;
        Iterator<ViewTransition> it = this.viewTransitions.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ViewTransition next = it.next();
            if (next.getId() == id) {
                viewTransition = next;
                break;
            }
        }
        if (viewTransition != null) {
            this.mRelatedViews = null;
            this.viewTransitions.remove(viewTransition);
        }
    }

    /* access modifiers changed from: package-private */
    public void removeAnimation(ViewTransition.Animate animation) {
        this.removeList.add(animation);
    }

    /* access modifiers changed from: package-private */
    public void touchEvent(MotionEvent event) {
        int currentState = this.mMotionLayout.getCurrentState();
        if (currentState != -1) {
            if (this.mRelatedViews == null) {
                this.mRelatedViews = new HashSet<>();
                Iterator<ViewTransition> it = this.viewTransitions.iterator();
                while (it.hasNext()) {
                    ViewTransition next = it.next();
                    int childCount = this.mMotionLayout.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View childAt = this.mMotionLayout.getChildAt(i);
                        if (next.matchesView(childAt)) {
                            int id = childAt.getId();
                            this.mRelatedViews.add(childAt);
                        }
                    }
                }
            }
            float x = event.getX();
            float y = event.getY();
            Rect rect = new Rect();
            int action = event.getAction();
            ArrayList<ViewTransition.Animate> arrayList = this.animations;
            if (arrayList != null && !arrayList.isEmpty()) {
                Iterator<ViewTransition.Animate> it2 = this.animations.iterator();
                while (it2.hasNext()) {
                    it2.next().reactTo(action, x, y);
                }
            }
            switch (action) {
                case 0:
                case 1:
                    ConstraintSet constraintSet = this.mMotionLayout.getConstraintSet(currentState);
                    Iterator<ViewTransition> it3 = this.viewTransitions.iterator();
                    while (it3.hasNext()) {
                        ViewTransition next2 = it3.next();
                        if (next2.supports(action)) {
                            Iterator<View> it4 = this.mRelatedViews.iterator();
                            while (it4.hasNext()) {
                                View next3 = it4.next();
                                if (next2.matchesView(next3)) {
                                    next3.getHitRect(rect);
                                    if (rect.contains((int) x, (int) y)) {
                                        View view = next3;
                                        next2.applyTransition(this, this.mMotionLayout, currentState, constraintSet, next3);
                                    } else {
                                        View view2 = next3;
                                    }
                                }
                            }
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void viewTransition(int id, View... views) {
        ViewTransition viewTransition = null;
        ArrayList arrayList = new ArrayList();
        Iterator<ViewTransition> it = this.viewTransitions.iterator();
        while (it.hasNext()) {
            ViewTransition next = it.next();
            if (next.getId() == id) {
                viewTransition = next;
                for (View view : views) {
                    if (next.checkTags(view)) {
                        arrayList.add(view);
                    }
                }
                if (!arrayList.isEmpty()) {
                    viewTransition(viewTransition, (View[]) arrayList.toArray(new View[0]));
                    arrayList.clear();
                }
            }
        }
        if (viewTransition == null) {
            Log.e(this.TAG, " Could not find ViewTransition");
        }
    }
}
