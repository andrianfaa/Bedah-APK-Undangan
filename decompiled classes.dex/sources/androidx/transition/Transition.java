package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.collection.SimpleArrayMap;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import mt.Log1F380D;
import okhttp3.HttpUrl;

/* compiled from: 0093 */
public abstract class Transition implements Cloneable {
    static final boolean DBG = false;
    private static final int[] DEFAULT_MATCH_ORDER = {2, 1, 3, 4};
    private static final String LOG_TAG = "Transition";
    private static final int MATCH_FIRST = 1;
    public static final int MATCH_ID = 3;
    private static final String MATCH_ID_STR = "id";
    public static final int MATCH_INSTANCE = 1;
    private static final String MATCH_INSTANCE_STR = "instance";
    public static final int MATCH_ITEM_ID = 4;
    private static final String MATCH_ITEM_ID_STR = "itemId";
    private static final int MATCH_LAST = 4;
    public static final int MATCH_NAME = 2;
    private static final String MATCH_NAME_STR = "name";
    private static final PathMotion STRAIGHT_PATH_MOTION = new PathMotion() {
        public Path getPath(float startX, float startY, float endX, float endY) {
            Path path = new Path();
            path.moveTo(startX, startY);
            path.lineTo(endX, endY);
            return path;
        }
    };
    private static ThreadLocal<ArrayMap<Animator, AnimationInfo>> sRunningAnimators = new ThreadLocal<>();
    private ArrayList<Animator> mAnimators = new ArrayList<>();
    boolean mCanRemoveViews = false;
    ArrayList<Animator> mCurrentAnimators = new ArrayList<>();
    long mDuration = -1;
    private TransitionValuesMaps mEndValues = new TransitionValuesMaps();
    private ArrayList<TransitionValues> mEndValuesList;
    private boolean mEnded = false;
    private EpicenterCallback mEpicenterCallback;
    private TimeInterpolator mInterpolator = null;
    private ArrayList<TransitionListener> mListeners = null;
    private int[] mMatchOrder = DEFAULT_MATCH_ORDER;
    private String mName = getClass().getName();
    private ArrayMap<String, String> mNameOverrides;
    private int mNumInstances = 0;
    TransitionSet mParent = null;
    private PathMotion mPathMotion = STRAIGHT_PATH_MOTION;
    private boolean mPaused = false;
    TransitionPropagation mPropagation;
    private ViewGroup mSceneRoot = null;
    private long mStartDelay = -1;
    private TransitionValuesMaps mStartValues = new TransitionValuesMaps();
    private ArrayList<TransitionValues> mStartValuesList;
    private ArrayList<View> mTargetChildExcludes = null;
    private ArrayList<View> mTargetExcludes = null;
    private ArrayList<Integer> mTargetIdChildExcludes = null;
    private ArrayList<Integer> mTargetIdExcludes = null;
    ArrayList<Integer> mTargetIds = new ArrayList<>();
    private ArrayList<String> mTargetNameExcludes = null;
    private ArrayList<String> mTargetNames = null;
    private ArrayList<Class<?>> mTargetTypeChildExcludes = null;
    private ArrayList<Class<?>> mTargetTypeExcludes = null;
    private ArrayList<Class<?>> mTargetTypes = null;
    ArrayList<View> mTargets = new ArrayList<>();

    private static class AnimationInfo {
        String mName;
        Transition mTransition;
        TransitionValues mValues;
        View mView;
        WindowIdImpl mWindowId;

        AnimationInfo(View view, String name, Transition transition, WindowIdImpl windowId, TransitionValues values) {
            this.mView = view;
            this.mName = name;
            this.mValues = values;
            this.mWindowId = windowId;
            this.mTransition = transition;
        }
    }

    private static class ArrayListManager {
        private ArrayListManager() {
        }

        static <T> ArrayList<T> add(ArrayList<T> arrayList, T t) {
            if (arrayList == null) {
                arrayList = new ArrayList<>();
            }
            if (!arrayList.contains(t)) {
                arrayList.add(t);
            }
            return arrayList;
        }

        static <T> ArrayList<T> remove(ArrayList<T> arrayList, T t) {
            if (arrayList == null) {
                return arrayList;
            }
            arrayList.remove(t);
            if (arrayList.isEmpty()) {
                return null;
            }
            return arrayList;
        }
    }

    public static abstract class EpicenterCallback {
        public abstract Rect onGetEpicenter(Transition transition);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MatchOrder {
    }

    public interface TransitionListener {
        void onTransitionCancel(Transition transition);

        void onTransitionEnd(Transition transition);

        void onTransitionPause(Transition transition);

        void onTransitionResume(Transition transition);

        void onTransitionStart(Transition transition);
    }

    public Transition() {
    }

    public Transition(Context context, AttributeSet attrs) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, Styleable.TRANSITION);
        XmlResourceParser xmlResourceParser = (XmlResourceParser) attrs;
        long namedInt = (long) TypedArrayUtils.getNamedInt(obtainStyledAttributes, xmlResourceParser, TypedValues.TransitionType.S_DURATION, 1, -1);
        if (namedInt >= 0) {
            setDuration(namedInt);
        }
        long namedInt2 = (long) TypedArrayUtils.getNamedInt(obtainStyledAttributes, xmlResourceParser, "startDelay", 2, -1);
        if (namedInt2 > 0) {
            setStartDelay(namedInt2);
        }
        int namedResourceId = TypedArrayUtils.getNamedResourceId(obtainStyledAttributes, xmlResourceParser, "interpolator", 0, 0);
        if (namedResourceId > 0) {
            setInterpolator(AnimationUtils.loadInterpolator(context, namedResourceId));
        }
        String namedString = TypedArrayUtils.getNamedString(obtainStyledAttributes, xmlResourceParser, "matchOrder", 3);
        Log1F380D.a((Object) namedString);
        if (namedString != null) {
            setMatchOrder(parseMatchOrder(namedString));
        }
        obtainStyledAttributes.recycle();
    }

    private void addUnmatched(ArrayMap<View, TransitionValues> arrayMap, ArrayMap<View, TransitionValues> arrayMap2) {
        for (int i = 0; i < arrayMap.size(); i++) {
            TransitionValues valueAt = arrayMap.valueAt(i);
            if (isValidTarget(valueAt.view)) {
                this.mStartValuesList.add(valueAt);
                this.mEndValuesList.add((Object) null);
            }
        }
        for (int i2 = 0; i2 < arrayMap2.size(); i2++) {
            TransitionValues valueAt2 = arrayMap2.valueAt(i2);
            if (isValidTarget(valueAt2.view)) {
                this.mEndValuesList.add(valueAt2);
                this.mStartValuesList.add((Object) null);
            }
        }
    }

    private static boolean alreadyContains(int[] array, int searchIndex) {
        int i = array[searchIndex];
        for (int i2 = 0; i2 < searchIndex; i2++) {
            if (array[i2] == i) {
                return true;
            }
        }
        return false;
    }

    private void captureHierarchy(View view, boolean start) {
        if (view != null) {
            int id = view.getId();
            ArrayList<Integer> arrayList = this.mTargetIdExcludes;
            if (arrayList == null || !arrayList.contains(Integer.valueOf(id))) {
                ArrayList<View> arrayList2 = this.mTargetExcludes;
                if (arrayList2 == null || !arrayList2.contains(view)) {
                    ArrayList<Class<?>> arrayList3 = this.mTargetTypeExcludes;
                    if (arrayList3 != null) {
                        int size = arrayList3.size();
                        int i = 0;
                        while (i < size) {
                            if (!this.mTargetTypeExcludes.get(i).isInstance(view)) {
                                i++;
                            } else {
                                return;
                            }
                        }
                    }
                    if (view.getParent() instanceof ViewGroup) {
                        TransitionValues transitionValues = new TransitionValues(view);
                        if (start) {
                            captureStartValues(transitionValues);
                        } else {
                            captureEndValues(transitionValues);
                        }
                        transitionValues.mTargetedTransitions.add(this);
                        capturePropagationValues(transitionValues);
                        if (start) {
                            addViewValues(this.mStartValues, view, transitionValues);
                        } else {
                            addViewValues(this.mEndValues, view, transitionValues);
                        }
                    }
                    if (view instanceof ViewGroup) {
                        ArrayList<Integer> arrayList4 = this.mTargetIdChildExcludes;
                        if (arrayList4 == null || !arrayList4.contains(Integer.valueOf(id))) {
                            ArrayList<View> arrayList5 = this.mTargetChildExcludes;
                            if (arrayList5 == null || !arrayList5.contains(view)) {
                                ArrayList<Class<?>> arrayList6 = this.mTargetTypeChildExcludes;
                                if (arrayList6 != null) {
                                    int size2 = arrayList6.size();
                                    int i2 = 0;
                                    while (i2 < size2) {
                                        if (!this.mTargetTypeChildExcludes.get(i2).isInstance(view)) {
                                            i2++;
                                        } else {
                                            return;
                                        }
                                    }
                                }
                                ViewGroup viewGroup = (ViewGroup) view;
                                for (int i3 = 0; i3 < viewGroup.getChildCount(); i3++) {
                                    captureHierarchy(viewGroup.getChildAt(i3), start);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private ArrayList<Integer> excludeId(ArrayList<Integer> arrayList, int targetId, boolean exclude) {
        return targetId > 0 ? exclude ? ArrayListManager.add(arrayList, Integer.valueOf(targetId)) : ArrayListManager.remove(arrayList, Integer.valueOf(targetId)) : arrayList;
    }

    private static <T> ArrayList<T> excludeObject(ArrayList<T> arrayList, T t, boolean exclude) {
        return t != null ? exclude ? ArrayListManager.add(arrayList, t) : ArrayListManager.remove(arrayList, t) : arrayList;
    }

    private ArrayList<Class<?>> excludeType(ArrayList<Class<?>> arrayList, Class<?> cls, boolean exclude) {
        return cls != null ? exclude ? ArrayListManager.add(arrayList, cls) : ArrayListManager.remove(arrayList, cls) : arrayList;
    }

    private ArrayList<View> excludeView(ArrayList<View> arrayList, View target, boolean exclude) {
        return target != null ? exclude ? ArrayListManager.add(arrayList, target) : ArrayListManager.remove(arrayList, target) : arrayList;
    }

    private static ArrayMap<Animator, AnimationInfo> getRunningAnimators() {
        ArrayMap<Animator, AnimationInfo> arrayMap = sRunningAnimators.get();
        if (arrayMap != null) {
            return arrayMap;
        }
        ArrayMap<Animator, AnimationInfo> arrayMap2 = new ArrayMap<>();
        sRunningAnimators.set(arrayMap2);
        return arrayMap2;
    }

    private static boolean isValidMatch(int match) {
        return match >= 1 && match <= 4;
    }

    private static boolean isValueChanged(TransitionValues oldValues, TransitionValues newValues, String key) {
        Object obj = oldValues.values.get(key);
        Object obj2 = newValues.values.get(key);
        if (obj == null && obj2 == null) {
            return false;
        }
        if (obj == null || obj2 == null) {
            return true;
        }
        return !obj.equals(obj2);
    }

    private void matchIds(ArrayMap<View, TransitionValues> arrayMap, ArrayMap<View, TransitionValues> arrayMap2, SparseArray<View> sparseArray, SparseArray<View> sparseArray2) {
        View view;
        int size = sparseArray.size();
        for (int i = 0; i < size; i++) {
            View valueAt = sparseArray.valueAt(i);
            if (valueAt != null && isValidTarget(valueAt) && (view = sparseArray2.get(sparseArray.keyAt(i))) != null && isValidTarget(view)) {
                TransitionValues transitionValues = arrayMap.get(valueAt);
                TransitionValues transitionValues2 = arrayMap2.get(view);
                if (!(transitionValues == null || transitionValues2 == null)) {
                    this.mStartValuesList.add(transitionValues);
                    this.mEndValuesList.add(transitionValues2);
                    arrayMap.remove(valueAt);
                    arrayMap2.remove(view);
                }
            }
        }
    }

    private void matchInstances(ArrayMap<View, TransitionValues> arrayMap, ArrayMap<View, TransitionValues> arrayMap2) {
        TransitionValues remove;
        for (int size = arrayMap.size() - 1; size >= 0; size--) {
            View keyAt = arrayMap.keyAt(size);
            if (keyAt != null && isValidTarget(keyAt) && (remove = arrayMap2.remove(keyAt)) != null && isValidTarget(remove.view)) {
                this.mStartValuesList.add(arrayMap.removeAt(size));
                this.mEndValuesList.add(remove);
            }
        }
    }

    private void matchItemIds(ArrayMap<View, TransitionValues> arrayMap, ArrayMap<View, TransitionValues> arrayMap2, LongSparseArray<View> longSparseArray, LongSparseArray<View> longSparseArray2) {
        View view;
        int size = longSparseArray.size();
        for (int i = 0; i < size; i++) {
            View valueAt = longSparseArray.valueAt(i);
            if (valueAt != null && isValidTarget(valueAt) && (view = longSparseArray2.get(longSparseArray.keyAt(i))) != null && isValidTarget(view)) {
                TransitionValues transitionValues = arrayMap.get(valueAt);
                TransitionValues transitionValues2 = arrayMap2.get(view);
                if (!(transitionValues == null || transitionValues2 == null)) {
                    this.mStartValuesList.add(transitionValues);
                    this.mEndValuesList.add(transitionValues2);
                    arrayMap.remove(valueAt);
                    arrayMap2.remove(view);
                }
            }
        }
    }

    private void matchNames(ArrayMap<View, TransitionValues> arrayMap, ArrayMap<View, TransitionValues> arrayMap2, ArrayMap<String, View> arrayMap3, ArrayMap<String, View> arrayMap4) {
        View view;
        int size = arrayMap3.size();
        for (int i = 0; i < size; i++) {
            View valueAt = arrayMap3.valueAt(i);
            if (valueAt != null && isValidTarget(valueAt) && (view = arrayMap4.get(arrayMap3.keyAt(i))) != null && isValidTarget(view)) {
                TransitionValues transitionValues = arrayMap.get(valueAt);
                TransitionValues transitionValues2 = arrayMap2.get(view);
                if (!(transitionValues == null || transitionValues2 == null)) {
                    this.mStartValuesList.add(transitionValues);
                    this.mEndValuesList.add(transitionValues2);
                    arrayMap.remove(valueAt);
                    arrayMap2.remove(view);
                }
            }
        }
    }

    private void matchStartAndEnd(TransitionValuesMaps startValues, TransitionValuesMaps endValues) {
        ArrayMap arrayMap = new ArrayMap((SimpleArrayMap) startValues.mViewValues);
        ArrayMap arrayMap2 = new ArrayMap((SimpleArrayMap) endValues.mViewValues);
        int i = 0;
        while (true) {
            int[] iArr = this.mMatchOrder;
            if (i < iArr.length) {
                switch (iArr[i]) {
                    case 1:
                        matchInstances(arrayMap, arrayMap2);
                        break;
                    case 2:
                        matchNames(arrayMap, arrayMap2, startValues.mNameValues, endValues.mNameValues);
                        break;
                    case 3:
                        matchIds(arrayMap, arrayMap2, startValues.mIdValues, endValues.mIdValues);
                        break;
                    case 4:
                        matchItemIds(arrayMap, arrayMap2, startValues.mItemIdValues, endValues.mItemIdValues);
                        break;
                }
                i++;
            } else {
                addUnmatched(arrayMap, arrayMap2);
                return;
            }
        }
    }

    private static int[] parseMatchOrder(String matchOrderString) {
        StringTokenizer stringTokenizer = new StringTokenizer(matchOrderString, ",");
        int[] iArr = new int[stringTokenizer.countTokens()];
        int i = 0;
        while (stringTokenizer.hasMoreTokens()) {
            String trim = stringTokenizer.nextToken().trim();
            if (MATCH_ID_STR.equalsIgnoreCase(trim)) {
                iArr[i] = 3;
            } else if (MATCH_INSTANCE_STR.equalsIgnoreCase(trim)) {
                iArr[i] = 1;
            } else if (MATCH_NAME_STR.equalsIgnoreCase(trim)) {
                iArr[i] = 2;
            } else if (MATCH_ITEM_ID_STR.equalsIgnoreCase(trim)) {
                iArr[i] = 4;
            } else if (trim.isEmpty()) {
                int[] iArr2 = new int[(iArr.length - 1)];
                System.arraycopy(iArr, 0, iArr2, 0, i);
                iArr = iArr2;
                i--;
            } else {
                throw new InflateException("Unknown match type in matchOrder: '" + trim + "'");
            }
            i++;
        }
        return iArr;
    }

    private void runAnimator(Animator animator, final ArrayMap<Animator, AnimationInfo> arrayMap) {
        if (animator != null) {
            animator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    arrayMap.remove(animation);
                    Transition.this.mCurrentAnimators.remove(animation);
                }

                public void onAnimationStart(Animator animation) {
                    Transition.this.mCurrentAnimators.add(animation);
                }
            });
            animate(animator);
        }
    }

    public Transition addListener(TransitionListener listener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<>();
        }
        this.mListeners.add(listener);
        return this;
    }

    public Transition addTarget(int targetId) {
        if (targetId != 0) {
            this.mTargetIds.add(Integer.valueOf(targetId));
        }
        return this;
    }

    public Transition addTarget(View target) {
        this.mTargets.add(target);
        return this;
    }

    public Transition addTarget(Class<?> cls) {
        if (this.mTargetTypes == null) {
            this.mTargetTypes = new ArrayList<>();
        }
        this.mTargetTypes.add(cls);
        return this;
    }

    public Transition addTarget(String targetName) {
        if (this.mTargetNames == null) {
            this.mTargetNames = new ArrayList<>();
        }
        this.mTargetNames.add(targetName);
        return this;
    }

    /* access modifiers changed from: protected */
    public void animate(Animator animator) {
        if (animator == null) {
            end();
            return;
        }
        if (getDuration() >= 0) {
            animator.setDuration(getDuration());
        }
        if (getStartDelay() >= 0) {
            animator.setStartDelay(getStartDelay() + animator.getStartDelay());
        }
        if (getInterpolator() != null) {
            animator.setInterpolator(getInterpolator());
        }
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                Transition.this.end();
                animation.removeListener(this);
            }
        });
        animator.start();
    }

    /* access modifiers changed from: protected */
    public void cancel() {
        for (int size = this.mCurrentAnimators.size() - 1; size >= 0; size--) {
            this.mCurrentAnimators.get(size).cancel();
        }
        ArrayList<TransitionListener> arrayList = this.mListeners;
        if (arrayList != null && arrayList.size() > 0) {
            ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
            int size2 = arrayList2.size();
            for (int i = 0; i < size2; i++) {
                ((TransitionListener) arrayList2.get(i)).onTransitionCancel(this);
            }
        }
    }

    public abstract void captureEndValues(TransitionValues transitionValues);

    /* access modifiers changed from: package-private */
    public void capturePropagationValues(TransitionValues transitionValues) {
        String[] propagationProperties;
        if (this.mPropagation != null && !transitionValues.values.isEmpty() && (propagationProperties = this.mPropagation.getPropagationProperties()) != null) {
            boolean z = true;
            int i = 0;
            while (true) {
                if (i >= propagationProperties.length) {
                    break;
                } else if (!transitionValues.values.containsKey(propagationProperties[i])) {
                    z = false;
                    break;
                } else {
                    i++;
                }
            }
            if (!z) {
                this.mPropagation.captureValues(transitionValues);
            }
        }
    }

    public abstract void captureStartValues(TransitionValues transitionValues);

    /* access modifiers changed from: package-private */
    public void captureValues(ViewGroup sceneRoot, boolean start) {
        ArrayMap<String, String> arrayMap;
        ArrayList<String> arrayList;
        ArrayList<Class<?>> arrayList2;
        clearValues(start);
        if ((this.mTargetIds.size() > 0 || this.mTargets.size() > 0) && (((arrayList = this.mTargetNames) == null || arrayList.isEmpty()) && ((arrayList2 = this.mTargetTypes) == null || arrayList2.isEmpty()))) {
            for (int i = 0; i < this.mTargetIds.size(); i++) {
                View findViewById = sceneRoot.findViewById(this.mTargetIds.get(i).intValue());
                if (findViewById != null) {
                    TransitionValues transitionValues = new TransitionValues(findViewById);
                    if (start) {
                        captureStartValues(transitionValues);
                    } else {
                        captureEndValues(transitionValues);
                    }
                    transitionValues.mTargetedTransitions.add(this);
                    capturePropagationValues(transitionValues);
                    if (start) {
                        addViewValues(this.mStartValues, findViewById, transitionValues);
                    } else {
                        addViewValues(this.mEndValues, findViewById, transitionValues);
                    }
                }
            }
            for (int i2 = 0; i2 < this.mTargets.size(); i2++) {
                View view = this.mTargets.get(i2);
                TransitionValues transitionValues2 = new TransitionValues(view);
                if (start) {
                    captureStartValues(transitionValues2);
                } else {
                    captureEndValues(transitionValues2);
                }
                transitionValues2.mTargetedTransitions.add(this);
                capturePropagationValues(transitionValues2);
                if (start) {
                    addViewValues(this.mStartValues, view, transitionValues2);
                } else {
                    addViewValues(this.mEndValues, view, transitionValues2);
                }
            }
        } else {
            captureHierarchy(sceneRoot, start);
        }
        if (!start && (arrayMap = this.mNameOverrides) != null) {
            int size = arrayMap.size();
            ArrayList arrayList3 = new ArrayList(size);
            for (int i3 = 0; i3 < size; i3++) {
                arrayList3.add(this.mStartValues.mNameValues.remove(this.mNameOverrides.keyAt(i3)));
            }
            for (int i4 = 0; i4 < size; i4++) {
                View view2 = (View) arrayList3.get(i4);
                if (view2 != null) {
                    this.mStartValues.mNameValues.put(this.mNameOverrides.valueAt(i4), view2);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void clearValues(boolean start) {
        if (start) {
            this.mStartValues.mViewValues.clear();
            this.mStartValues.mIdValues.clear();
            this.mStartValues.mItemIdValues.clear();
            return;
        }
        this.mEndValues.mViewValues.clear();
        this.mEndValues.mIdValues.clear();
        this.mEndValues.mItemIdValues.clear();
    }

    public Transition clone() {
        try {
            Transition transition = (Transition) super.clone();
            transition.mAnimators = new ArrayList<>();
            transition.mStartValues = new TransitionValuesMaps();
            transition.mEndValues = new TransitionValuesMaps();
            transition.mStartValuesList = null;
            transition.mEndValuesList = null;
            return transition;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        return null;
    }

    /* access modifiers changed from: protected */
    public void createAnimators(ViewGroup sceneRoot, TransitionValuesMaps startValues, TransitionValuesMaps endValues, ArrayList<TransitionValues> arrayList, ArrayList<TransitionValues> arrayList2) {
        int i;
        int i2;
        View view;
        TransitionValues transitionValues;
        Animator animator;
        long j;
        Animator animator2;
        Animator animator3;
        int i3;
        int i4;
        ViewGroup viewGroup = sceneRoot;
        ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        long j2 = Long.MAX_VALUE;
        SparseIntArray sparseIntArray = new SparseIntArray();
        int size = arrayList.size();
        int i5 = 0;
        while (i5 < size) {
            TransitionValues transitionValues2 = arrayList.get(i5);
            TransitionValues transitionValues3 = arrayList2.get(i5);
            TransitionValues transitionValues4 = (transitionValues2 == null || transitionValues2.mTargetedTransitions.contains(this)) ? transitionValues2 : null;
            TransitionValues transitionValues5 = (transitionValues3 == null || transitionValues3.mTargetedTransitions.contains(this)) ? transitionValues3 : null;
            if (transitionValues4 == null && transitionValues5 == null) {
                i2 = size;
                i = i5;
            } else {
                if (transitionValues4 == null || transitionValues5 == null || isTransitionRequired(transitionValues4, transitionValues5)) {
                    Animator createAnimator = createAnimator(viewGroup, transitionValues4, transitionValues5);
                    if (createAnimator != null) {
                        TransitionValues transitionValues6 = null;
                        if (transitionValues5 != null) {
                            View view2 = transitionValues5.view;
                            String[] transitionProperties = getTransitionProperties();
                            if (transitionProperties != null) {
                                animator3 = createAnimator;
                                if (transitionProperties.length > 0) {
                                    TransitionValues transitionValues7 = new TransitionValues(view2);
                                    i2 = size;
                                    TransitionValues transitionValues8 = endValues.mViewValues.get(view2);
                                    if (transitionValues8 != null) {
                                        int i6 = 0;
                                        while (i6 < transitionProperties.length) {
                                            transitionValues7.values.put(transitionProperties[i6], transitionValues8.values.get(transitionProperties[i6]));
                                            i6++;
                                            ArrayList<TransitionValues> arrayList3 = arrayList;
                                            ArrayList<TransitionValues> arrayList4 = arrayList2;
                                            i5 = i5;
                                            transitionValues8 = transitionValues8;
                                        }
                                        TransitionValues transitionValues9 = transitionValues8;
                                        i = i5;
                                    } else {
                                        TransitionValues transitionValues10 = transitionValues8;
                                        i = i5;
                                    }
                                    int size2 = runningAnimators.size();
                                    int i7 = 0;
                                    while (true) {
                                        if (i7 >= size2) {
                                            int i8 = size2;
                                            transitionValues6 = transitionValues7;
                                            animator2 = animator3;
                                            break;
                                        }
                                        AnimationInfo animationInfo = runningAnimators.get(runningAnimators.keyAt(i7));
                                        if (animationInfo.mValues != null && animationInfo.mView == view2) {
                                            i4 = size2;
                                            if (animationInfo.mName.equals(getName()) && animationInfo.mValues.equals(transitionValues7)) {
                                                transitionValues6 = transitionValues7;
                                                animator2 = null;
                                                break;
                                            }
                                        } else {
                                            i4 = size2;
                                        }
                                        i7++;
                                        size2 = i4;
                                    }
                                    animator = animator2;
                                    transitionValues = transitionValues6;
                                    view = view2;
                                } else {
                                    i2 = size;
                                    i3 = i5;
                                }
                            } else {
                                animator3 = createAnimator;
                                i2 = size;
                                i3 = i5;
                            }
                            animator2 = animator3;
                            animator = animator2;
                            transitionValues = transitionValues6;
                            view = view2;
                        } else {
                            i2 = size;
                            i = i5;
                            transitionValues = null;
                            view = transitionValues4.view;
                            animator = createAnimator;
                        }
                        if (animator != null) {
                            TransitionPropagation transitionPropagation = this.mPropagation;
                            if (transitionPropagation != null) {
                                long startDelay = transitionPropagation.getStartDelay(viewGroup, this, transitionValues4, transitionValues5);
                                sparseIntArray.put(this.mAnimators.size(), (int) startDelay);
                                j = Math.min(startDelay, j2);
                            } else {
                                j = j2;
                            }
                            runningAnimators.put(animator, new AnimationInfo(view, getName(), this, ViewUtils.getWindowId(sceneRoot), transitionValues));
                            this.mAnimators.add(animator);
                            j2 = j;
                        }
                    } else {
                        Animator animator4 = createAnimator;
                        i2 = size;
                        i = i5;
                    }
                } else {
                    i2 = size;
                    i = i5;
                }
            }
            i5 = i + 1;
            size = i2;
        }
        int i9 = size;
        int i10 = i5;
        if (sparseIntArray.size() != 0) {
            for (int i11 = 0; i11 < sparseIntArray.size(); i11++) {
                Animator animator5 = this.mAnimators.get(sparseIntArray.keyAt(i11));
                animator5.setStartDelay((((long) sparseIntArray.valueAt(i11)) - j2) + animator5.getStartDelay());
            }
        }
    }

    /* access modifiers changed from: protected */
    public void end() {
        int i = this.mNumInstances - 1;
        this.mNumInstances = i;
        if (i == 0) {
            ArrayList<TransitionListener> arrayList = this.mListeners;
            if (arrayList != null && arrayList.size() > 0) {
                ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
                int size = arrayList2.size();
                for (int i2 = 0; i2 < size; i2++) {
                    ((TransitionListener) arrayList2.get(i2)).onTransitionEnd(this);
                }
            }
            for (int i3 = 0; i3 < this.mStartValues.mItemIdValues.size(); i3++) {
                View valueAt = this.mStartValues.mItemIdValues.valueAt(i3);
                if (valueAt != null) {
                    ViewCompat.setHasTransientState(valueAt, false);
                }
            }
            for (int i4 = 0; i4 < this.mEndValues.mItemIdValues.size(); i4++) {
                View valueAt2 = this.mEndValues.mItemIdValues.valueAt(i4);
                if (valueAt2 != null) {
                    ViewCompat.setHasTransientState(valueAt2, false);
                }
            }
            this.mEnded = true;
        }
    }

    public Transition excludeChildren(int targetId, boolean exclude) {
        this.mTargetIdChildExcludes = excludeId(this.mTargetIdChildExcludes, targetId, exclude);
        return this;
    }

    public Transition excludeChildren(View target, boolean exclude) {
        this.mTargetChildExcludes = excludeView(this.mTargetChildExcludes, target, exclude);
        return this;
    }

    public Transition excludeChildren(Class<?> cls, boolean exclude) {
        this.mTargetTypeChildExcludes = excludeType(this.mTargetTypeChildExcludes, cls, exclude);
        return this;
    }

    public Transition excludeTarget(int targetId, boolean exclude) {
        this.mTargetIdExcludes = excludeId(this.mTargetIdExcludes, targetId, exclude);
        return this;
    }

    public Transition excludeTarget(View target, boolean exclude) {
        this.mTargetExcludes = excludeView(this.mTargetExcludes, target, exclude);
        return this;
    }

    public Transition excludeTarget(Class<?> cls, boolean exclude) {
        this.mTargetTypeExcludes = excludeType(this.mTargetTypeExcludes, cls, exclude);
        return this;
    }

    public Transition excludeTarget(String targetName, boolean exclude) {
        this.mTargetNameExcludes = excludeObject(this.mTargetNameExcludes, targetName, exclude);
        return this;
    }

    /* access modifiers changed from: package-private */
    public void forceToEnd(ViewGroup sceneRoot) {
        ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        int size = runningAnimators.size();
        if (sceneRoot != null && size != 0) {
            WindowIdImpl windowId = ViewUtils.getWindowId(sceneRoot);
            ArrayMap arrayMap = new ArrayMap((SimpleArrayMap) runningAnimators);
            runningAnimators.clear();
            for (int i = size - 1; i >= 0; i--) {
                AnimationInfo animationInfo = (AnimationInfo) arrayMap.valueAt(i);
                if (!(animationInfo.mView == null || windowId == null || !windowId.equals(animationInfo.mWindowId))) {
                    ((Animator) arrayMap.keyAt(i)).end();
                }
            }
        }
    }

    public long getDuration() {
        return this.mDuration;
    }

    public Rect getEpicenter() {
        EpicenterCallback epicenterCallback = this.mEpicenterCallback;
        if (epicenterCallback == null) {
            return null;
        }
        return epicenterCallback.onGetEpicenter(this);
    }

    public EpicenterCallback getEpicenterCallback() {
        return this.mEpicenterCallback;
    }

    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }

    /* access modifiers changed from: package-private */
    public TransitionValues getMatchedTransitionValues(View view, boolean viewInStart) {
        TransitionSet transitionSet = this.mParent;
        if (transitionSet != null) {
            return transitionSet.getMatchedTransitionValues(view, viewInStart);
        }
        ArrayList<TransitionValues> arrayList = viewInStart ? this.mStartValuesList : this.mEndValuesList;
        if (arrayList == null) {
            return null;
        }
        int size = arrayList.size();
        int i = -1;
        int i2 = 0;
        while (true) {
            if (i2 >= size) {
                break;
            }
            TransitionValues transitionValues = arrayList.get(i2);
            if (transitionValues == null) {
                return null;
            }
            if (transitionValues.view == view) {
                i = i2;
                break;
            }
            i2++;
        }
        if (i < 0) {
            return null;
        }
        return (viewInStart ? this.mEndValuesList : this.mStartValuesList).get(i);
    }

    public String getName() {
        return this.mName;
    }

    public PathMotion getPathMotion() {
        return this.mPathMotion;
    }

    public TransitionPropagation getPropagation() {
        return this.mPropagation;
    }

    public long getStartDelay() {
        return this.mStartDelay;
    }

    public List<Integer> getTargetIds() {
        return this.mTargetIds;
    }

    public List<String> getTargetNames() {
        return this.mTargetNames;
    }

    public List<Class<?>> getTargetTypes() {
        return this.mTargetTypes;
    }

    public List<View> getTargets() {
        return this.mTargets;
    }

    public String[] getTransitionProperties() {
        return null;
    }

    public TransitionValues getTransitionValues(View view, boolean start) {
        TransitionSet transitionSet = this.mParent;
        if (transitionSet != null) {
            return transitionSet.getTransitionValues(view, start);
        }
        return (start ? this.mStartValues : this.mEndValues).mViewValues.get(view);
    }

    public boolean isTransitionRequired(TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return false;
        }
        String[] transitionProperties = getTransitionProperties();
        if (transitionProperties != null) {
            for (String isValueChanged : transitionProperties) {
                if (isValueChanged(startValues, endValues, isValueChanged)) {
                    return true;
                }
            }
            return false;
        }
        for (String isValueChanged2 : startValues.values.keySet()) {
            if (isValueChanged(startValues, endValues, isValueChanged2)) {
                return true;
            }
        }
        return false;
    }

    public void pause(View sceneRoot) {
        if (!this.mEnded) {
            ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
            int size = runningAnimators.size();
            WindowIdImpl windowId = ViewUtils.getWindowId(sceneRoot);
            for (int i = size - 1; i >= 0; i--) {
                AnimationInfo valueAt = runningAnimators.valueAt(i);
                if (valueAt.mView != null && windowId.equals(valueAt.mWindowId)) {
                    AnimatorUtils.pause(runningAnimators.keyAt(i));
                }
            }
            ArrayList<TransitionListener> arrayList = this.mListeners;
            if (arrayList != null && arrayList.size() > 0) {
                ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
                int size2 = arrayList2.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    ((TransitionListener) arrayList2.get(i2)).onTransitionPause(this);
                }
            }
            this.mPaused = true;
        }
    }

    /* access modifiers changed from: package-private */
    public void playTransition(ViewGroup sceneRoot) {
        AnimationInfo animationInfo;
        this.mStartValuesList = new ArrayList<>();
        this.mEndValuesList = new ArrayList<>();
        matchStartAndEnd(this.mStartValues, this.mEndValues);
        ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        int size = runningAnimators.size();
        WindowIdImpl windowId = ViewUtils.getWindowId(sceneRoot);
        for (int i = size - 1; i >= 0; i--) {
            Animator keyAt = runningAnimators.keyAt(i);
            if (!(keyAt == null || (animationInfo = runningAnimators.get(keyAt)) == null || animationInfo.mView == null || !windowId.equals(animationInfo.mWindowId))) {
                TransitionValues transitionValues = animationInfo.mValues;
                View view = animationInfo.mView;
                boolean z = true;
                TransitionValues transitionValues2 = getTransitionValues(view, true);
                TransitionValues matchedTransitionValues = getMatchedTransitionValues(view, true);
                if (transitionValues2 == null && matchedTransitionValues == null) {
                    matchedTransitionValues = this.mEndValues.mViewValues.get(view);
                }
                if ((transitionValues2 == null && matchedTransitionValues == null) || !animationInfo.mTransition.isTransitionRequired(transitionValues, matchedTransitionValues)) {
                    z = false;
                }
                if (z) {
                    if (keyAt.isRunning() || keyAt.isStarted()) {
                        keyAt.cancel();
                    } else {
                        runningAnimators.remove(keyAt);
                    }
                }
            }
        }
        createAnimators(sceneRoot, this.mStartValues, this.mEndValues, this.mStartValuesList, this.mEndValuesList);
        runAnimators();
    }

    public Transition removeListener(TransitionListener listener) {
        ArrayList<TransitionListener> arrayList = this.mListeners;
        if (arrayList == null) {
            return this;
        }
        arrayList.remove(listener);
        if (this.mListeners.size() == 0) {
            this.mListeners = null;
        }
        return this;
    }

    public Transition removeTarget(int targetId) {
        if (targetId != 0) {
            this.mTargetIds.remove(Integer.valueOf(targetId));
        }
        return this;
    }

    public Transition removeTarget(View target) {
        this.mTargets.remove(target);
        return this;
    }

    public Transition removeTarget(Class<?> cls) {
        ArrayList<Class<?>> arrayList = this.mTargetTypes;
        if (arrayList != null) {
            arrayList.remove(cls);
        }
        return this;
    }

    public Transition removeTarget(String targetName) {
        ArrayList<String> arrayList = this.mTargetNames;
        if (arrayList != null) {
            arrayList.remove(targetName);
        }
        return this;
    }

    public void resume(View sceneRoot) {
        if (this.mPaused) {
            if (!this.mEnded) {
                ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
                int size = runningAnimators.size();
                WindowIdImpl windowId = ViewUtils.getWindowId(sceneRoot);
                for (int i = size - 1; i >= 0; i--) {
                    AnimationInfo valueAt = runningAnimators.valueAt(i);
                    if (valueAt.mView != null && windowId.equals(valueAt.mWindowId)) {
                        AnimatorUtils.resume(runningAnimators.keyAt(i));
                    }
                }
                ArrayList<TransitionListener> arrayList = this.mListeners;
                if (arrayList != null && arrayList.size() > 0) {
                    ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
                    int size2 = arrayList2.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        ((TransitionListener) arrayList2.get(i2)).onTransitionResume(this);
                    }
                }
            }
            this.mPaused = false;
        }
    }

    /* access modifiers changed from: protected */
    public void runAnimators() {
        start();
        ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        Iterator<Animator> it = this.mAnimators.iterator();
        while (it.hasNext()) {
            Animator next = it.next();
            if (runningAnimators.containsKey(next)) {
                start();
                runAnimator(next, runningAnimators);
            }
        }
        this.mAnimators.clear();
        end();
    }

    /* access modifiers changed from: package-private */
    public void setCanRemoveViews(boolean canRemoveViews) {
        this.mCanRemoveViews = canRemoveViews;
    }

    public Transition setDuration(long duration) {
        this.mDuration = duration;
        return this;
    }

    public void setEpicenterCallback(EpicenterCallback epicenterCallback) {
        this.mEpicenterCallback = epicenterCallback;
    }

    public Transition setInterpolator(TimeInterpolator interpolator) {
        this.mInterpolator = interpolator;
        return this;
    }

    public void setMatchOrder(int... matches) {
        if (matches == null || matches.length == 0) {
            this.mMatchOrder = DEFAULT_MATCH_ORDER;
            return;
        }
        int i = 0;
        while (i < matches.length) {
            if (!isValidMatch(matches[i])) {
                throw new IllegalArgumentException("matches contains invalid value");
            } else if (!alreadyContains(matches, i)) {
                i++;
            } else {
                throw new IllegalArgumentException("matches contains a duplicate value");
            }
        }
        this.mMatchOrder = (int[]) matches.clone();
    }

    public void setPathMotion(PathMotion pathMotion) {
        if (pathMotion == null) {
            this.mPathMotion = STRAIGHT_PATH_MOTION;
        } else {
            this.mPathMotion = pathMotion;
        }
    }

    public void setPropagation(TransitionPropagation transitionPropagation) {
        this.mPropagation = transitionPropagation;
    }

    /* access modifiers changed from: package-private */
    public Transition setSceneRoot(ViewGroup sceneRoot) {
        this.mSceneRoot = sceneRoot;
        return this;
    }

    public Transition setStartDelay(long startDelay) {
        this.mStartDelay = startDelay;
        return this;
    }

    /* access modifiers changed from: protected */
    public void start() {
        if (this.mNumInstances == 0) {
            ArrayList<TransitionListener> arrayList = this.mListeners;
            if (arrayList != null && arrayList.size() > 0) {
                ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
                int size = arrayList2.size();
                for (int i = 0; i < size; i++) {
                    ((TransitionListener) arrayList2.get(i)).onTransitionStart(this);
                }
            }
            this.mEnded = false;
        }
        this.mNumInstances++;
    }

    public String toString() {
        return toString(HttpUrl.FRAGMENT_ENCODE_SET);
    }

    private static void addViewValues(TransitionValuesMaps transitionValuesMaps, View view, TransitionValues transitionValues) {
        transitionValuesMaps.mViewValues.put(view, transitionValues);
        int id = view.getId();
        if (id >= 0) {
            if (transitionValuesMaps.mIdValues.indexOfKey(id) >= 0) {
                transitionValuesMaps.mIdValues.put(id, (Object) null);
            } else {
                transitionValuesMaps.mIdValues.put(id, view);
            }
        }
        String transitionName = ViewCompat.getTransitionName(view);
        Log1F380D.a((Object) transitionName);
        if (transitionName != null) {
            if (transitionValuesMaps.mNameValues.containsKey(transitionName)) {
                transitionValuesMaps.mNameValues.put(transitionName, null);
            } else {
                transitionValuesMaps.mNameValues.put(transitionName, view);
            }
        }
        if (view.getParent() instanceof ListView) {
            ListView listView = (ListView) view.getParent();
            if (listView.getAdapter().hasStableIds()) {
                long itemIdAtPosition = listView.getItemIdAtPosition(listView.getPositionForView(view));
                if (transitionValuesMaps.mItemIdValues.indexOfKey(itemIdAtPosition) >= 0) {
                    View view2 = transitionValuesMaps.mItemIdValues.get(itemIdAtPosition);
                    if (view2 != null) {
                        ViewCompat.setHasTransientState(view2, false);
                        transitionValuesMaps.mItemIdValues.put(itemIdAtPosition, null);
                        return;
                    }
                    return;
                }
                ViewCompat.setHasTransientState(view, true);
                transitionValuesMaps.mItemIdValues.put(itemIdAtPosition, view);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isValidTarget(View target) {
        ArrayList<Class<?>> arrayList;
        ArrayList<String> arrayList2;
        int id = target.getId();
        ArrayList<Integer> arrayList3 = this.mTargetIdExcludes;
        if (arrayList3 != null && arrayList3.contains(Integer.valueOf(id))) {
            return false;
        }
        ArrayList<View> arrayList4 = this.mTargetExcludes;
        if (arrayList4 != null && arrayList4.contains(target)) {
            return false;
        }
        ArrayList<Class<?>> arrayList5 = this.mTargetTypeExcludes;
        if (arrayList5 != null) {
            int size = arrayList5.size();
            for (int i = 0; i < size; i++) {
                if (this.mTargetTypeExcludes.get(i).isInstance(target)) {
                    return false;
                }
            }
        }
        if (this.mTargetNameExcludes != null) {
            String transitionName = ViewCompat.getTransitionName(target);
            Log1F380D.a((Object) transitionName);
            if (transitionName != null) {
                ArrayList<String> arrayList6 = this.mTargetNameExcludes;
                String transitionName2 = ViewCompat.getTransitionName(target);
                Log1F380D.a((Object) transitionName2);
                if (arrayList6.contains(transitionName2)) {
                    return false;
                }
            }
        }
        if ((this.mTargetIds.size() == 0 && this.mTargets.size() == 0 && (((arrayList = this.mTargetTypes) == null || arrayList.isEmpty()) && ((arrayList2 = this.mTargetNames) == null || arrayList2.isEmpty()))) || this.mTargetIds.contains(Integer.valueOf(id)) || this.mTargets.contains(target)) {
            return true;
        }
        ArrayList<String> arrayList7 = this.mTargetNames;
        if (arrayList7 != null) {
            String transitionName3 = ViewCompat.getTransitionName(target);
            Log1F380D.a((Object) transitionName3);
            if (arrayList7.contains(transitionName3)) {
                return true;
            }
        }
        if (this.mTargetTypes != null) {
            for (int i2 = 0; i2 < this.mTargetTypes.size(); i2++) {
                if (this.mTargetTypes.get(i2).isInstance(target)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public String toString(String indent) {
        StringBuilder append = new StringBuilder().append(indent).append(getClass().getSimpleName()).append("@");
        String hexString = Integer.toHexString(hashCode());
        Log1F380D.a((Object) hexString);
        String sb = append.append(hexString).append(": ").toString();
        if (this.mDuration != -1) {
            sb = sb + "dur(" + this.mDuration + ") ";
        }
        if (this.mStartDelay != -1) {
            sb = sb + "dly(" + this.mStartDelay + ") ";
        }
        if (this.mInterpolator != null) {
            sb = sb + "interp(" + this.mInterpolator + ") ";
        }
        if (this.mTargetIds.size() <= 0 && this.mTargets.size() <= 0) {
            return sb;
        }
        String str = sb + "tgts(";
        if (this.mTargetIds.size() > 0) {
            for (int i = 0; i < this.mTargetIds.size(); i++) {
                if (i > 0) {
                    str = str + ", ";
                }
                str = str + this.mTargetIds.get(i);
            }
        }
        if (this.mTargets.size() > 0) {
            for (int i2 = 0; i2 < this.mTargets.size(); i2++) {
                if (i2 > 0) {
                    str = str + ", ";
                }
                str = str + this.mTargets.get(i2);
            }
        }
        return str + ")";
    }
}
