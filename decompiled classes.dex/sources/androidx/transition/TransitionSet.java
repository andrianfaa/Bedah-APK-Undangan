package androidx.transition;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.transition.Transition;
import java.util.ArrayList;
import java.util.Iterator;

public class TransitionSet extends Transition {
    private static final int FLAG_CHANGE_EPICENTER = 8;
    private static final int FLAG_CHANGE_INTERPOLATOR = 1;
    private static final int FLAG_CHANGE_PATH_MOTION = 4;
    private static final int FLAG_CHANGE_PROPAGATION = 2;
    public static final int ORDERING_SEQUENTIAL = 1;
    public static final int ORDERING_TOGETHER = 0;
    private int mChangeFlags = 0;
    int mCurrentListeners;
    private boolean mPlayTogether = true;
    boolean mStarted = false;
    private ArrayList<Transition> mTransitions = new ArrayList<>();

    static class TransitionSetListener extends TransitionListenerAdapter {
        TransitionSet mTransitionSet;

        TransitionSetListener(TransitionSet transitionSet) {
            this.mTransitionSet = transitionSet;
        }

        public void onTransitionEnd(Transition transition) {
            TransitionSet transitionSet = this.mTransitionSet;
            transitionSet.mCurrentListeners--;
            if (this.mTransitionSet.mCurrentListeners == 0) {
                this.mTransitionSet.mStarted = false;
                this.mTransitionSet.end();
            }
            transition.removeListener(this);
        }

        public void onTransitionStart(Transition transition) {
            if (!this.mTransitionSet.mStarted) {
                this.mTransitionSet.start();
                this.mTransitionSet.mStarted = true;
            }
        }
    }

    public TransitionSet() {
    }

    public TransitionSet(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, Styleable.TRANSITION_SET);
        setOrdering(TypedArrayUtils.getNamedInt(obtainStyledAttributes, (XmlResourceParser) attrs, "transitionOrdering", 0, 0));
        obtainStyledAttributes.recycle();
    }

    private void addTransitionInternal(Transition transition) {
        this.mTransitions.add(transition);
        transition.mParent = this;
    }

    private void setupStartEndListeners() {
        TransitionSetListener transitionSetListener = new TransitionSetListener(this);
        Iterator<Transition> it = this.mTransitions.iterator();
        while (it.hasNext()) {
            it.next().addListener(transitionSetListener);
        }
        this.mCurrentListeners = this.mTransitions.size();
    }

    public TransitionSet addListener(Transition.TransitionListener listener) {
        return (TransitionSet) super.addListener(listener);
    }

    public TransitionSet addTarget(int targetId) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).addTarget(targetId);
        }
        return (TransitionSet) super.addTarget(targetId);
    }

    public TransitionSet addTarget(View target) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).addTarget(target);
        }
        return (TransitionSet) super.addTarget(target);
    }

    public TransitionSet addTarget(Class<?> cls) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).addTarget(cls);
        }
        return (TransitionSet) super.addTarget(cls);
    }

    public TransitionSet addTarget(String targetName) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).addTarget(targetName);
        }
        return (TransitionSet) super.addTarget(targetName);
    }

    public TransitionSet addTransition(Transition transition) {
        addTransitionInternal(transition);
        if (this.mDuration >= 0) {
            transition.setDuration(this.mDuration);
        }
        if ((this.mChangeFlags & 1) != 0) {
            transition.setInterpolator(getInterpolator());
        }
        if ((this.mChangeFlags & 2) != 0) {
            transition.setPropagation(getPropagation());
        }
        if ((this.mChangeFlags & 4) != 0) {
            transition.setPathMotion(getPathMotion());
        }
        if ((this.mChangeFlags & 8) != 0) {
            transition.setEpicenterCallback(getEpicenterCallback());
        }
        return this;
    }

    /* access modifiers changed from: protected */
    public void cancel() {
        super.cancel();
        int size = this.mTransitions.size();
        for (int i = 0; i < size; i++) {
            this.mTransitions.get(i).cancel();
        }
    }

    public void captureEndValues(TransitionValues transitionValues) {
        if (isValidTarget(transitionValues.view)) {
            Iterator<Transition> it = this.mTransitions.iterator();
            while (it.hasNext()) {
                Transition next = it.next();
                if (next.isValidTarget(transitionValues.view)) {
                    next.captureEndValues(transitionValues);
                    transitionValues.mTargetedTransitions.add(next);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void capturePropagationValues(TransitionValues transitionValues) {
        super.capturePropagationValues(transitionValues);
        int size = this.mTransitions.size();
        for (int i = 0; i < size; i++) {
            this.mTransitions.get(i).capturePropagationValues(transitionValues);
        }
    }

    public void captureStartValues(TransitionValues transitionValues) {
        if (isValidTarget(transitionValues.view)) {
            Iterator<Transition> it = this.mTransitions.iterator();
            while (it.hasNext()) {
                Transition next = it.next();
                if (next.isValidTarget(transitionValues.view)) {
                    next.captureStartValues(transitionValues);
                    transitionValues.mTargetedTransitions.add(next);
                }
            }
        }
    }

    public Transition clone() {
        TransitionSet transitionSet = (TransitionSet) super.clone();
        transitionSet.mTransitions = new ArrayList<>();
        int size = this.mTransitions.size();
        for (int i = 0; i < size; i++) {
            transitionSet.addTransitionInternal(this.mTransitions.get(i).clone());
        }
        return transitionSet;
    }

    /* access modifiers changed from: protected */
    public void createAnimators(ViewGroup sceneRoot, TransitionValuesMaps startValues, TransitionValuesMaps endValues, ArrayList<TransitionValues> arrayList, ArrayList<TransitionValues> arrayList2) {
        long startDelay = getStartDelay();
        int size = this.mTransitions.size();
        for (int i = 0; i < size; i++) {
            Transition transition = this.mTransitions.get(i);
            if (startDelay > 0 && (this.mPlayTogether || i == 0)) {
                long startDelay2 = transition.getStartDelay();
                if (startDelay2 > 0) {
                    transition.setStartDelay(startDelay + startDelay2);
                } else {
                    transition.setStartDelay(startDelay);
                }
            }
            transition.createAnimators(sceneRoot, startValues, endValues, arrayList, arrayList2);
        }
    }

    public Transition excludeTarget(int targetId, boolean exclude) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).excludeTarget(targetId, exclude);
        }
        return super.excludeTarget(targetId, exclude);
    }

    public Transition excludeTarget(View target, boolean exclude) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).excludeTarget(target, exclude);
        }
        return super.excludeTarget(target, exclude);
    }

    public Transition excludeTarget(Class<?> cls, boolean exclude) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).excludeTarget(cls, exclude);
        }
        return super.excludeTarget(cls, exclude);
    }

    public Transition excludeTarget(String targetName, boolean exclude) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).excludeTarget(targetName, exclude);
        }
        return super.excludeTarget(targetName, exclude);
    }

    /* access modifiers changed from: package-private */
    public void forceToEnd(ViewGroup sceneRoot) {
        super.forceToEnd(sceneRoot);
        int size = this.mTransitions.size();
        for (int i = 0; i < size; i++) {
            this.mTransitions.get(i).forceToEnd(sceneRoot);
        }
    }

    public int getOrdering() {
        return this.mPlayTogether ^ true ? 1 : 0;
    }

    public Transition getTransitionAt(int index) {
        if (index < 0 || index >= this.mTransitions.size()) {
            return null;
        }
        return this.mTransitions.get(index);
    }

    public int getTransitionCount() {
        return this.mTransitions.size();
    }

    public void pause(View sceneRoot) {
        super.pause(sceneRoot);
        int size = this.mTransitions.size();
        for (int i = 0; i < size; i++) {
            this.mTransitions.get(i).pause(sceneRoot);
        }
    }

    public TransitionSet removeListener(Transition.TransitionListener listener) {
        return (TransitionSet) super.removeListener(listener);
    }

    public TransitionSet removeTarget(int targetId) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).removeTarget(targetId);
        }
        return (TransitionSet) super.removeTarget(targetId);
    }

    public TransitionSet removeTarget(View target) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).removeTarget(target);
        }
        return (TransitionSet) super.removeTarget(target);
    }

    public TransitionSet removeTarget(Class<?> cls) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).removeTarget(cls);
        }
        return (TransitionSet) super.removeTarget(cls);
    }

    public TransitionSet removeTarget(String target) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).removeTarget(target);
        }
        return (TransitionSet) super.removeTarget(target);
    }

    public TransitionSet removeTransition(Transition transition) {
        this.mTransitions.remove(transition);
        transition.mParent = null;
        return this;
    }

    public void resume(View sceneRoot) {
        super.resume(sceneRoot);
        int size = this.mTransitions.size();
        for (int i = 0; i < size; i++) {
            this.mTransitions.get(i).resume(sceneRoot);
        }
    }

    /* access modifiers changed from: protected */
    public void runAnimators() {
        if (this.mTransitions.isEmpty()) {
            start();
            end();
            return;
        }
        setupStartEndListeners();
        if (!this.mPlayTogether) {
            for (int i = 1; i < this.mTransitions.size(); i++) {
                final Transition transition = this.mTransitions.get(i);
                this.mTransitions.get(i - 1).addListener(new TransitionListenerAdapter() {
                    public void onTransitionEnd(Transition transition) {
                        transition.runAnimators();
                        transition.removeListener(this);
                    }
                });
            }
            Transition transition2 = this.mTransitions.get(0);
            if (transition2 != null) {
                transition2.runAnimators();
                return;
            }
            return;
        }
        Iterator<Transition> it = this.mTransitions.iterator();
        while (it.hasNext()) {
            it.next().runAnimators();
        }
    }

    /* access modifiers changed from: package-private */
    public void setCanRemoveViews(boolean canRemoveViews) {
        super.setCanRemoveViews(canRemoveViews);
        int size = this.mTransitions.size();
        for (int i = 0; i < size; i++) {
            this.mTransitions.get(i).setCanRemoveViews(canRemoveViews);
        }
    }

    public TransitionSet setDuration(long duration) {
        ArrayList<Transition> arrayList;
        super.setDuration(duration);
        if (this.mDuration >= 0 && (arrayList = this.mTransitions) != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                this.mTransitions.get(i).setDuration(duration);
            }
        }
        return this;
    }

    public void setEpicenterCallback(Transition.EpicenterCallback epicenterCallback) {
        super.setEpicenterCallback(epicenterCallback);
        this.mChangeFlags |= 8;
        int size = this.mTransitions.size();
        for (int i = 0; i < size; i++) {
            this.mTransitions.get(i).setEpicenterCallback(epicenterCallback);
        }
    }

    public TransitionSet setInterpolator(TimeInterpolator interpolator) {
        this.mChangeFlags |= 1;
        ArrayList<Transition> arrayList = this.mTransitions;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                this.mTransitions.get(i).setInterpolator(interpolator);
            }
        }
        return (TransitionSet) super.setInterpolator(interpolator);
    }

    public TransitionSet setOrdering(int ordering) {
        switch (ordering) {
            case 0:
                this.mPlayTogether = true;
                break;
            case 1:
                this.mPlayTogether = false;
                break;
            default:
                throw new AndroidRuntimeException("Invalid parameter for TransitionSet ordering: " + ordering);
        }
        return this;
    }

    public void setPathMotion(PathMotion pathMotion) {
        super.setPathMotion(pathMotion);
        this.mChangeFlags |= 4;
        if (this.mTransitions != null) {
            for (int i = 0; i < this.mTransitions.size(); i++) {
                this.mTransitions.get(i).setPathMotion(pathMotion);
            }
        }
    }

    public void setPropagation(TransitionPropagation propagation) {
        super.setPropagation(propagation);
        this.mChangeFlags |= 2;
        int size = this.mTransitions.size();
        for (int i = 0; i < size; i++) {
            this.mTransitions.get(i).setPropagation(propagation);
        }
    }

    /* access modifiers changed from: package-private */
    public TransitionSet setSceneRoot(ViewGroup sceneRoot) {
        super.setSceneRoot(sceneRoot);
        int size = this.mTransitions.size();
        for (int i = 0; i < size; i++) {
            this.mTransitions.get(i).setSceneRoot(sceneRoot);
        }
        return this;
    }

    public TransitionSet setStartDelay(long startDelay) {
        return (TransitionSet) super.setStartDelay(startDelay);
    }

    /* access modifiers changed from: package-private */
    public String toString(String indent) {
        String transition = super.toString(indent);
        for (int i = 0; i < this.mTransitions.size(); i++) {
            transition = transition + "\n" + this.mTransitions.get(i).toString(indent + "  ");
        }
        return transition;
    }
}
