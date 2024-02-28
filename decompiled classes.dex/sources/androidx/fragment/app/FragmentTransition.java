package androidx.fragment.app;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.ArrayMap;
import androidx.core.app.SharedElementCallback;
import androidx.core.os.CancellationSignal;
import androidx.core.view.OneShotPreDrawListener;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import mt.Log1F380D;

/* compiled from: 007E */
class FragmentTransition {
    private static final int[] INVERSE_OPS = {0, 3, 0, 1, 5, 4, 7, 6, 9, 8, 10};
    static final FragmentTransitionImpl PLATFORM_IMPL = (Build.VERSION.SDK_INT >= 21 ? new FragmentTransitionCompat21() : null);
    static final FragmentTransitionImpl SUPPORT_IMPL = resolveSupportImpl();

    interface Callback {
        void onComplete(Fragment fragment, CancellationSignal cancellationSignal);

        void onStart(Fragment fragment, CancellationSignal cancellationSignal);
    }

    static class FragmentContainerTransition {
        public Fragment firstOut;
        public boolean firstOutIsPop;
        public BackStackRecord firstOutTransaction;
        public Fragment lastIn;
        public boolean lastInIsPop;
        public BackStackRecord lastInTransaction;

        FragmentContainerTransition() {
        }
    }

    private FragmentTransition() {
    }

    private static void addSharedElementsWithMatchingNames(ArrayList<View> arrayList, ArrayMap<String, View> arrayMap, Collection<String> collection) {
        for (int size = arrayMap.size() - 1; size >= 0; size--) {
            View valueAt = arrayMap.valueAt(size);
            String transitionName = ViewCompat.getTransitionName(valueAt);
            Log1F380D.a((Object) transitionName);
            if (collection.contains(transitionName)) {
                arrayList.add(valueAt);
            }
        }
    }

    private static void addToFirstInLastOut(BackStackRecord transaction, FragmentTransaction.Op op, SparseArray<FragmentContainerTransition> sparseArray, boolean isPop, boolean isReorderedTransaction) {
        int i;
        boolean z;
        boolean z2;
        BackStackRecord backStackRecord = transaction;
        FragmentTransaction.Op op2 = op;
        SparseArray<FragmentContainerTransition> sparseArray2 = sparseArray;
        boolean z3 = isPop;
        Fragment fragment = op2.mFragment;
        if (fragment != null && (i = fragment.mContainerId) != 0) {
            boolean z4 = false;
            boolean z5 = false;
            boolean z6 = false;
            boolean z7 = false;
            boolean z8 = true;
            switch (z3 ? INVERSE_OPS[op2.mCmd] : op2.mCmd) {
                case 1:
                case 7:
                    if (isReorderedTransaction) {
                        z = fragment.mIsNewlyAdded;
                    } else {
                        if (fragment.mAdded || fragment.mHidden) {
                            z8 = false;
                        }
                        z = z8;
                    }
                    z7 = true;
                    break;
                case 3:
                case 6:
                    if (isReorderedTransaction) {
                        if (fragment.mAdded || fragment.mView == null || fragment.mView.getVisibility() != 0 || fragment.mPostponedAlpha < 0.0f) {
                            z8 = false;
                        }
                        z2 = z8;
                    } else {
                        if (!fragment.mAdded || fragment.mHidden) {
                            z8 = false;
                        }
                        z2 = z8;
                    }
                    z5 = true;
                    break;
                case 4:
                    if (isReorderedTransaction) {
                        if (!fragment.mHiddenChanged || !fragment.mAdded || !fragment.mHidden) {
                            z8 = false;
                        }
                        z6 = z8;
                    } else {
                        if (!fragment.mAdded || fragment.mHidden) {
                            z8 = false;
                        }
                        z6 = z8;
                    }
                    z5 = true;
                    break;
                case 5:
                    if (isReorderedTransaction) {
                        if (!fragment.mHiddenChanged || fragment.mHidden || !fragment.mAdded) {
                            z8 = false;
                        }
                        z4 = z8;
                    } else {
                        z4 = fragment.mHidden;
                    }
                    z7 = true;
                    break;
            }
            FragmentContainerTransition fragmentContainerTransition = sparseArray2.get(i);
            if (z4) {
                fragmentContainerTransition = ensureContainer(fragmentContainerTransition, sparseArray2, i);
                fragmentContainerTransition.lastIn = fragment;
                fragmentContainerTransition.lastInIsPop = z3;
                fragmentContainerTransition.lastInTransaction = backStackRecord;
            }
            if (!isReorderedTransaction && z7) {
                if (fragmentContainerTransition != null && fragmentContainerTransition.firstOut == fragment) {
                    fragmentContainerTransition.firstOut = null;
                }
                if (!backStackRecord.mReorderingAllowed) {
                    FragmentManager fragmentManager = backStackRecord.mManager;
                    fragmentManager.getFragmentStore().makeActive(fragmentManager.createOrGetFragmentStateManager(fragment));
                    fragmentManager.moveToState(fragment);
                }
            }
            if (z6 && (fragmentContainerTransition == null || fragmentContainerTransition.firstOut == null)) {
                fragmentContainerTransition = ensureContainer(fragmentContainerTransition, sparseArray2, i);
                fragmentContainerTransition.firstOut = fragment;
                fragmentContainerTransition.firstOutIsPop = z3;
                fragmentContainerTransition.firstOutTransaction = backStackRecord;
            }
            if (!isReorderedTransaction && z5 && fragmentContainerTransition != null && fragmentContainerTransition.lastIn == fragment) {
                fragmentContainerTransition.lastIn = null;
            }
        }
    }

    public static void calculateFragments(BackStackRecord transaction, SparseArray<FragmentContainerTransition> sparseArray, boolean isReordered) {
        int size = transaction.mOps.size();
        for (int i = 0; i < size; i++) {
            addToFirstInLastOut(transaction, (FragmentTransaction.Op) transaction.mOps.get(i), sparseArray, false, isReordered);
        }
    }

    private static ArrayMap<String, String> calculateNameOverrides(int containerId, ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int startIndex, int endIndex) {
        ArrayList arrayList3;
        ArrayList arrayList4;
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        for (int i = endIndex - 1; i >= startIndex; i--) {
            BackStackRecord backStackRecord = arrayList.get(i);
            if (backStackRecord.interactsWith(containerId)) {
                boolean booleanValue = arrayList2.get(i).booleanValue();
                if (backStackRecord.mSharedElementSourceNames != null) {
                    int size = backStackRecord.mSharedElementSourceNames.size();
                    if (booleanValue) {
                        arrayList4 = backStackRecord.mSharedElementSourceNames;
                        arrayList3 = backStackRecord.mSharedElementTargetNames;
                    } else {
                        arrayList3 = backStackRecord.mSharedElementSourceNames;
                        arrayList4 = backStackRecord.mSharedElementTargetNames;
                    }
                    for (int i2 = 0; i2 < size; i2++) {
                        String str = (String) arrayList3.get(i2);
                        String str2 = (String) arrayList4.get(i2);
                        String remove = arrayMap.remove(str2);
                        if (remove != null) {
                            arrayMap.put(str, remove);
                        } else {
                            arrayMap.put(str, str2);
                        }
                    }
                }
            }
        }
        return arrayMap;
    }

    public static void calculatePopFragments(BackStackRecord transaction, SparseArray<FragmentContainerTransition> sparseArray, boolean isReordered) {
        if (transaction.mManager.getContainer().onHasView()) {
            for (int size = transaction.mOps.size() - 1; size >= 0; size--) {
                addToFirstInLastOut(transaction, (FragmentTransaction.Op) transaction.mOps.get(size), sparseArray, true, isReordered);
            }
        }
    }

    static void callSharedElementStartEnd(Fragment inFragment, Fragment outFragment, boolean isPop, ArrayMap<String, View> arrayMap, boolean isStart) {
        SharedElementCallback enterTransitionCallback = isPop ? outFragment.getEnterTransitionCallback() : inFragment.getEnterTransitionCallback();
        if (enterTransitionCallback != null) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            int size = arrayMap == null ? 0 : arrayMap.size();
            for (int i = 0; i < size; i++) {
                arrayList2.add(arrayMap.keyAt(i));
                arrayList.add(arrayMap.valueAt(i));
            }
            if (isStart) {
                enterTransitionCallback.onSharedElementStart(arrayList2, arrayList, (List<View>) null);
            } else {
                enterTransitionCallback.onSharedElementEnd(arrayList2, arrayList, (List<View>) null);
            }
        }
    }

    private static boolean canHandleAll(FragmentTransitionImpl impl, List<Object> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (!impl.canHandle(list.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static FragmentTransitionImpl chooseImpl(Fragment outFragment, Fragment inFragment) {
        ArrayList arrayList = new ArrayList();
        if (outFragment != null) {
            Object exitTransition = outFragment.getExitTransition();
            if (exitTransition != null) {
                arrayList.add(exitTransition);
            }
            Object returnTransition = outFragment.getReturnTransition();
            if (returnTransition != null) {
                arrayList.add(returnTransition);
            }
            Object sharedElementReturnTransition = outFragment.getSharedElementReturnTransition();
            if (sharedElementReturnTransition != null) {
                arrayList.add(sharedElementReturnTransition);
            }
        }
        if (inFragment != null) {
            Object enterTransition = inFragment.getEnterTransition();
            if (enterTransition != null) {
                arrayList.add(enterTransition);
            }
            Object reenterTransition = inFragment.getReenterTransition();
            if (reenterTransition != null) {
                arrayList.add(reenterTransition);
            }
            Object sharedElementEnterTransition = inFragment.getSharedElementEnterTransition();
            if (sharedElementEnterTransition != null) {
                arrayList.add(sharedElementEnterTransition);
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        FragmentTransitionImpl fragmentTransitionImpl = PLATFORM_IMPL;
        if (fragmentTransitionImpl != null && canHandleAll(fragmentTransitionImpl, arrayList)) {
            return fragmentTransitionImpl;
        }
        FragmentTransitionImpl fragmentTransitionImpl2 = SUPPORT_IMPL;
        if (fragmentTransitionImpl2 != null && canHandleAll(fragmentTransitionImpl2, arrayList)) {
            return fragmentTransitionImpl2;
        }
        if (fragmentTransitionImpl == null && fragmentTransitionImpl2 == null) {
            return null;
        }
        throw new IllegalArgumentException("Invalid Transition types");
    }

    static ArrayList<View> configureEnteringExitingViews(FragmentTransitionImpl impl, Object transition, Fragment fragment, ArrayList<View> arrayList, View nonExistentView) {
        ArrayList<View> arrayList2 = null;
        if (transition != null) {
            arrayList2 = new ArrayList<>();
            View view = fragment.getView();
            if (view != null) {
                impl.captureTransitioningViews(arrayList2, view);
            }
            if (arrayList != null) {
                arrayList2.removeAll(arrayList);
            }
            if (!arrayList2.isEmpty()) {
                arrayList2.add(nonExistentView);
                impl.addTargets(transition, arrayList2);
            }
        }
        return arrayList2;
    }

    private static Object configureSharedElementsOrdered(FragmentTransitionImpl impl, ViewGroup sceneRoot, View nonExistentView, ArrayMap<String, String> arrayMap, FragmentContainerTransition fragments, ArrayList<View> arrayList, ArrayList<View> arrayList2, Object enterTransition, Object exitTransition) {
        Object obj;
        Rect rect;
        FragmentTransitionImpl fragmentTransitionImpl = impl;
        FragmentContainerTransition fragmentContainerTransition = fragments;
        ArrayList<View> arrayList3 = arrayList;
        Object obj2 = enterTransition;
        Fragment fragment = fragmentContainerTransition.lastIn;
        Fragment fragment2 = fragmentContainerTransition.firstOut;
        if (fragment == null) {
            ViewGroup viewGroup = sceneRoot;
            Fragment fragment3 = fragment2;
            Fragment fragment4 = fragment;
        } else if (fragment2 == null) {
            ViewGroup viewGroup2 = sceneRoot;
            Fragment fragment5 = fragment2;
            Fragment fragment6 = fragment;
        } else {
            final boolean z = fragmentContainerTransition.lastInIsPop;
            Object sharedElementTransition = arrayMap.isEmpty() ? null : getSharedElementTransition(fragmentTransitionImpl, fragment, fragment2, z);
            ArrayMap<String, View> captureOutSharedElements = captureOutSharedElements(fragmentTransitionImpl, arrayMap, sharedElementTransition, fragmentContainerTransition);
            if (arrayMap.isEmpty()) {
                obj = null;
            } else {
                arrayList3.addAll(captureOutSharedElements.values());
                obj = sharedElementTransition;
            }
            if (obj2 == null && exitTransition == null && obj == null) {
                return null;
            }
            callSharedElementStartEnd(fragment, fragment2, z, captureOutSharedElements, true);
            if (obj != null) {
                Rect rect2 = new Rect();
                fragmentTransitionImpl.setSharedElementTargets(obj, nonExistentView, arrayList3);
                ArrayMap<String, View> arrayMap2 = captureOutSharedElements;
                Rect rect3 = rect2;
                setOutEpicenter(impl, obj, exitTransition, captureOutSharedElements, fragmentContainerTransition.firstOutIsPop, fragmentContainerTransition.firstOutTransaction);
                if (obj2 != null) {
                    fragmentTransitionImpl.setEpicenter(obj2, rect3);
                }
                rect = rect3;
            } else {
                ArrayMap<String, View> arrayMap3 = captureOutSharedElements;
                rect = null;
            }
            final Object obj3 = obj;
            final FragmentTransitionImpl fragmentTransitionImpl2 = impl;
            final ArrayMap<String, String> arrayMap4 = arrayMap;
            final FragmentContainerTransition fragmentContainerTransition2 = fragments;
            final ArrayList<View> arrayList4 = arrayList2;
            Object obj4 = obj;
            final View view = nonExistentView;
            AnonymousClass6 r13 = r0;
            final Fragment fragment7 = fragment;
            final Fragment fragment8 = fragment2;
            boolean z2 = z;
            Fragment fragment9 = fragment2;
            final ArrayList<View> arrayList5 = arrayList;
            Fragment fragment10 = fragment;
            final Object obj5 = enterTransition;
            final Rect rect4 = rect;
            AnonymousClass6 r0 = new Runnable() {
                public void run() {
                    ArrayMap<String, View> captureInSharedElements = FragmentTransition.captureInSharedElements(FragmentTransitionImpl.this, arrayMap4, obj3, fragmentContainerTransition2);
                    if (captureInSharedElements != null) {
                        arrayList4.addAll(captureInSharedElements.values());
                        arrayList4.add(view);
                    }
                    FragmentTransition.callSharedElementStartEnd(fragment7, fragment8, z, captureInSharedElements, false);
                    Object obj = obj3;
                    if (obj != null) {
                        FragmentTransitionImpl.this.swapSharedElementTargets(obj, arrayList5, arrayList4);
                        View inEpicenterView = FragmentTransition.getInEpicenterView(captureInSharedElements, fragmentContainerTransition2, obj5, z);
                        if (inEpicenterView != null) {
                            FragmentTransitionImpl.this.getBoundsOnScreen(inEpicenterView, rect4);
                        }
                    }
                }
            };
            OneShotPreDrawListener.add(sceneRoot, r13);
            return obj4;
        }
        return null;
    }

    private static Object configureSharedElementsReordered(FragmentTransitionImpl impl, ViewGroup sceneRoot, View nonExistentView, ArrayMap<String, String> arrayMap, FragmentContainerTransition fragments, ArrayList<View> arrayList, ArrayList<View> arrayList2, Object enterTransition, Object exitTransition) {
        Object obj;
        Object obj2;
        View view;
        Rect rect;
        ArrayMap<String, View> arrayMap2;
        FragmentTransitionImpl fragmentTransitionImpl = impl;
        View view2 = nonExistentView;
        ArrayMap<String, String> arrayMap3 = arrayMap;
        FragmentContainerTransition fragmentContainerTransition = fragments;
        ArrayList<View> arrayList3 = arrayList;
        ArrayList<View> arrayList4 = arrayList2;
        Object obj3 = enterTransition;
        Fragment fragment = fragmentContainerTransition.lastIn;
        Fragment fragment2 = fragmentContainerTransition.firstOut;
        if (fragment != null) {
            fragment.requireView().setVisibility(0);
        }
        if (fragment == null) {
            ViewGroup viewGroup = sceneRoot;
            Fragment fragment3 = fragment2;
        } else if (fragment2 == null) {
            ViewGroup viewGroup2 = sceneRoot;
            Fragment fragment4 = fragment2;
        } else {
            boolean z = fragmentContainerTransition.lastInIsPop;
            Object sharedElementTransition = arrayMap.isEmpty() ? null : getSharedElementTransition(fragmentTransitionImpl, fragment, fragment2, z);
            ArrayMap<String, View> captureOutSharedElements = captureOutSharedElements(fragmentTransitionImpl, arrayMap3, sharedElementTransition, fragmentContainerTransition);
            ArrayMap<String, View> captureInSharedElements = captureInSharedElements(fragmentTransitionImpl, arrayMap3, sharedElementTransition, fragmentContainerTransition);
            if (arrayMap.isEmpty()) {
                if (captureOutSharedElements != null) {
                    captureOutSharedElements.clear();
                }
                if (captureInSharedElements != null) {
                    captureInSharedElements.clear();
                }
                obj = null;
            } else {
                addSharedElementsWithMatchingNames(arrayList3, captureOutSharedElements, arrayMap.keySet());
                addSharedElementsWithMatchingNames(arrayList4, captureInSharedElements, arrayMap.values());
                obj = sharedElementTransition;
            }
            if (obj3 == null && exitTransition == null && obj == null) {
                return null;
            }
            callSharedElementStartEnd(fragment, fragment2, z, captureOutSharedElements, true);
            if (obj != null) {
                arrayList4.add(view2);
                fragmentTransitionImpl.setSharedElementTargets(obj, view2, arrayList3);
                obj2 = obj;
                arrayMap2 = captureInSharedElements;
                ArrayMap<String, View> arrayMap4 = captureOutSharedElements;
                setOutEpicenter(impl, obj, exitTransition, captureOutSharedElements, fragmentContainerTransition.firstOutIsPop, fragmentContainerTransition.firstOutTransaction);
                Rect rect2 = new Rect();
                View inEpicenterView = getInEpicenterView(arrayMap2, fragmentContainerTransition, obj3, z);
                if (inEpicenterView != null) {
                    fragmentTransitionImpl.setEpicenter(obj3, rect2);
                }
                rect = rect2;
                view = inEpicenterView;
            } else {
                obj2 = obj;
                arrayMap2 = captureInSharedElements;
                ArrayMap<String, View> arrayMap5 = captureOutSharedElements;
                rect = null;
                view = null;
            }
            final Fragment fragment5 = fragment;
            final Fragment fragment6 = fragment2;
            final boolean z2 = z;
            final ArrayMap<String, View> arrayMap6 = arrayMap2;
            AnonymousClass5 r8 = r0;
            final View view3 = view;
            boolean z3 = z;
            final FragmentTransitionImpl fragmentTransitionImpl2 = impl;
            Fragment fragment7 = fragment2;
            final Rect rect3 = rect;
            AnonymousClass5 r0 = new Runnable() {
                public void run() {
                    FragmentTransition.callSharedElementStartEnd(Fragment.this, fragment6, z2, arrayMap6, false);
                    View view = view3;
                    if (view != null) {
                        fragmentTransitionImpl2.getBoundsOnScreen(view, rect3);
                    }
                }
            };
            OneShotPreDrawListener.add(sceneRoot, r8);
            return obj2;
        }
        return null;
    }

    private static void configureTransitionsOrdered(ViewGroup container, FragmentContainerTransition fragments, View nonExistentView, ArrayMap<String, String> arrayMap, Callback callback) {
        Object obj;
        ViewGroup viewGroup = container;
        FragmentContainerTransition fragmentContainerTransition = fragments;
        View view = nonExistentView;
        ArrayMap<String, String> arrayMap2 = arrayMap;
        final Callback callback2 = callback;
        Fragment fragment = fragmentContainerTransition.lastIn;
        final Fragment fragment2 = fragmentContainerTransition.firstOut;
        FragmentTransitionImpl chooseImpl = chooseImpl(fragment2, fragment);
        if (chooseImpl != null) {
            boolean z = fragmentContainerTransition.lastInIsPop;
            boolean z2 = fragmentContainerTransition.firstOutIsPop;
            Object enterTransition = getEnterTransition(chooseImpl, fragment, z);
            Object exitTransition = getExitTransition(chooseImpl, fragment2, z2);
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = arrayList;
            Object obj2 = exitTransition;
            Object obj3 = enterTransition;
            boolean z3 = z2;
            boolean z4 = z;
            FragmentTransitionImpl fragmentTransitionImpl = chooseImpl;
            Object configureSharedElementsOrdered = configureSharedElementsOrdered(chooseImpl, container, nonExistentView, arrayMap, fragments, arrayList3, arrayList2, obj3, obj2);
            Object obj4 = obj3;
            if (obj4 == null && configureSharedElementsOrdered == null) {
                obj = obj2;
                if (obj == null) {
                    return;
                }
            } else {
                obj = obj2;
            }
            ArrayList arrayList4 = arrayList3;
            ArrayList<View> configureEnteringExitingViews = configureEnteringExitingViews(fragmentTransitionImpl, obj, fragment2, arrayList4, view);
            Object obj5 = (configureEnteringExitingViews == null || configureEnteringExitingViews.isEmpty()) ? null : obj;
            fragmentTransitionImpl.addTarget(obj4, view);
            Object mergeTransitions = mergeTransitions(fragmentTransitionImpl, obj4, obj5, configureSharedElementsOrdered, fragment, fragmentContainerTransition.lastInIsPop);
            if (!(fragment2 == null || configureEnteringExitingViews == null || (configureEnteringExitingViews.size() <= 0 && arrayList4.size() <= 0))) {
                final CancellationSignal cancellationSignal = new CancellationSignal();
                callback2.onStart(fragment2, cancellationSignal);
                fragmentTransitionImpl.setListenerForTransitionEnd(fragment2, mergeTransitions, cancellationSignal, new Runnable() {
                    public void run() {
                        Callback.this.onComplete(fragment2, cancellationSignal);
                    }
                });
            }
            if (mergeTransitions != null) {
                ArrayList arrayList5 = new ArrayList();
                FragmentTransitionImpl fragmentTransitionImpl2 = fragmentTransitionImpl;
                fragmentTransitionImpl2.scheduleRemoveTargets(mergeTransitions, obj4, arrayList5, obj5, configureEnteringExitingViews, configureSharedElementsOrdered, arrayList2);
                ArrayList arrayList6 = arrayList4;
                scheduleTargetChange(fragmentTransitionImpl, container, fragment, nonExistentView, arrayList2, obj4, arrayList5, obj5, configureEnteringExitingViews);
                ViewGroup viewGroup2 = container;
                FragmentTransitionImpl fragmentTransitionImpl3 = fragmentTransitionImpl2;
                ArrayList arrayList7 = arrayList2;
                fragmentTransitionImpl3.setNameOverridesOrdered(viewGroup2, arrayList7, arrayMap2);
                fragmentTransitionImpl3.beginDelayedTransition(viewGroup2, mergeTransitions);
                fragmentTransitionImpl3.scheduleNameReset(viewGroup2, arrayList7, arrayMap2);
                return;
            }
            ViewGroup viewGroup3 = container;
            ArrayList arrayList8 = arrayList4;
            Object obj6 = obj4;
            FragmentTransitionImpl fragmentTransitionImpl4 = fragmentTransitionImpl;
            ArrayList arrayList9 = arrayList2;
            Object obj7 = mergeTransitions;
        }
    }

    private static void configureTransitionsReordered(ViewGroup container, FragmentContainerTransition fragments, View nonExistentView, ArrayMap<String, String> arrayMap, Callback callback) {
        Object obj;
        ArrayList arrayList;
        FragmentContainerTransition fragmentContainerTransition = fragments;
        View view = nonExistentView;
        Callback callback2 = callback;
        Fragment fragment = fragmentContainerTransition.lastIn;
        final Fragment fragment2 = fragmentContainerTransition.firstOut;
        FragmentTransitionImpl chooseImpl = chooseImpl(fragment2, fragment);
        if (chooseImpl != null) {
            boolean z = fragmentContainerTransition.lastInIsPop;
            boolean z2 = fragmentContainerTransition.firstOutIsPop;
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            Object enterTransition = getEnterTransition(chooseImpl, fragment, z);
            Object exitTransition = getExitTransition(chooseImpl, fragment2, z2);
            Object obj2 = enterTransition;
            ArrayList arrayList4 = arrayList3;
            ArrayList arrayList5 = arrayList3;
            ArrayList arrayList6 = arrayList2;
            ArrayList arrayList7 = arrayList2;
            Object obj3 = obj2;
            boolean z3 = z2;
            Object configureSharedElementsReordered = configureSharedElementsReordered(chooseImpl, container, nonExistentView, arrayMap, fragments, arrayList4, arrayList6, obj3, exitTransition);
            if (obj3 == null && configureSharedElementsReordered == null) {
                obj = exitTransition;
                if (obj == null) {
                    return;
                }
            } else {
                obj = exitTransition;
            }
            ArrayList<View> configureEnteringExitingViews = configureEnteringExitingViews(chooseImpl, obj, fragment2, arrayList5, view);
            ArrayList<View> configureEnteringExitingViews2 = configureEnteringExitingViews(chooseImpl, obj3, fragment, arrayList7, view);
            setViewVisibility(configureEnteringExitingViews2, 4);
            ArrayList<View> arrayList8 = configureEnteringExitingViews2;
            ArrayList<View> arrayList9 = configureEnteringExitingViews;
            Object mergeTransitions = mergeTransitions(chooseImpl, obj3, obj, configureSharedElementsReordered, fragment, z);
            if (fragment2 == null || arrayList9 == null) {
                arrayList = arrayList7;
                Callback callback3 = callback;
            } else if (arrayList9.size() > 0 || arrayList5.size() > 0) {
                final CancellationSignal cancellationSignal = new CancellationSignal();
                arrayList = arrayList7;
                final Callback callback4 = callback;
                callback4.onStart(fragment2, cancellationSignal);
                chooseImpl.setListenerForTransitionEnd(fragment2, mergeTransitions, cancellationSignal, new Runnable() {
                    public void run() {
                        Callback.this.onComplete(fragment2, cancellationSignal);
                    }
                });
            } else {
                arrayList = arrayList7;
                Callback callback5 = callback;
            }
            if (mergeTransitions != null) {
                replaceHide(chooseImpl, obj, fragment2, arrayList9);
                ArrayList<String> prepareSetNameOverridesReordered = chooseImpl.prepareSetNameOverridesReordered(arrayList);
                FragmentTransitionImpl fragmentTransitionImpl = chooseImpl;
                ArrayList arrayList10 = arrayList;
                Object obj4 = obj;
                Object obj5 = obj3;
                fragmentTransitionImpl.scheduleRemoveTargets(mergeTransitions, obj3, arrayList8, obj, arrayList9, configureSharedElementsReordered, arrayList10);
                chooseImpl.beginDelayedTransition(container, mergeTransitions);
                fragmentTransitionImpl.setNameOverridesReordered(container, arrayList5, arrayList10, prepareSetNameOverridesReordered, arrayMap);
                setViewVisibility(arrayList8, 0);
                chooseImpl.swapSharedElementTargets(configureSharedElementsReordered, arrayList5, arrayList10);
                return;
            }
            ArrayList arrayList11 = arrayList;
            Object obj6 = mergeTransitions;
            Object obj7 = obj;
            Object obj8 = obj3;
            ArrayList<View> arrayList12 = arrayList8;
            ViewGroup viewGroup = container;
        }
    }

    private static FragmentContainerTransition ensureContainer(FragmentContainerTransition containerTransition, SparseArray<FragmentContainerTransition> sparseArray, int containerId) {
        if (containerTransition != null) {
            return containerTransition;
        }
        FragmentContainerTransition containerTransition2 = new FragmentContainerTransition();
        sparseArray.put(containerId, containerTransition2);
        return containerTransition2;
    }

    static String findKeyForValue(ArrayMap<String, String> arrayMap, String value) {
        int size = arrayMap.size();
        for (int i = 0; i < size; i++) {
            if (value.equals(arrayMap.valueAt(i))) {
                return arrayMap.keyAt(i);
            }
        }
        return null;
    }

    private static Object getEnterTransition(FragmentTransitionImpl impl, Fragment inFragment, boolean isPop) {
        if (inFragment == null) {
            return null;
        }
        return impl.cloneTransition(isPop ? inFragment.getReenterTransition() : inFragment.getEnterTransition());
    }

    private static Object getExitTransition(FragmentTransitionImpl impl, Fragment outFragment, boolean isPop) {
        if (outFragment == null) {
            return null;
        }
        return impl.cloneTransition(isPop ? outFragment.getReturnTransition() : outFragment.getExitTransition());
    }

    static View getInEpicenterView(ArrayMap<String, View> arrayMap, FragmentContainerTransition fragments, Object enterTransition, boolean inIsPop) {
        BackStackRecord backStackRecord = fragments.lastInTransaction;
        if (enterTransition == null || arrayMap == null || backStackRecord.mSharedElementSourceNames == null || backStackRecord.mSharedElementSourceNames.isEmpty()) {
            return null;
        }
        return arrayMap.get(inIsPop ? (String) backStackRecord.mSharedElementSourceNames.get(0) : (String) backStackRecord.mSharedElementTargetNames.get(0));
    }

    private static Object getSharedElementTransition(FragmentTransitionImpl impl, Fragment inFragment, Fragment outFragment, boolean isPop) {
        if (inFragment == null || outFragment == null) {
            return null;
        }
        return impl.wrapTransitionInSet(impl.cloneTransition(isPop ? outFragment.getSharedElementReturnTransition() : inFragment.getSharedElementEnterTransition()));
    }

    private static Object mergeTransitions(FragmentTransitionImpl impl, Object enterTransition, Object exitTransition, Object sharedElementTransition, Fragment inFragment, boolean isPop) {
        boolean z = true;
        if (!(enterTransition == null || exitTransition == null || inFragment == null)) {
            z = isPop ? inFragment.getAllowReturnTransitionOverlap() : inFragment.getAllowEnterTransitionOverlap();
        }
        return z ? impl.mergeTransitionsTogether(exitTransition, enterTransition, sharedElementTransition) : impl.mergeTransitionsInSequence(exitTransition, enterTransition, sharedElementTransition);
    }

    private static void replaceHide(FragmentTransitionImpl impl, Object exitTransition, Fragment exitingFragment, final ArrayList<View> arrayList) {
        if (exitingFragment != null && exitTransition != null && exitingFragment.mAdded && exitingFragment.mHidden && exitingFragment.mHiddenChanged) {
            exitingFragment.setHideReplaced(true);
            impl.scheduleHideFragmentView(exitTransition, exitingFragment.getView(), arrayList);
            OneShotPreDrawListener.add(exitingFragment.mContainer, new Runnable() {
                public void run() {
                    FragmentTransition.setViewVisibility(arrayList, 4);
                }
            });
        }
    }

    private static FragmentTransitionImpl resolveSupportImpl() {
        try {
            return (FragmentTransitionImpl) Class.forName("androidx.transition.FragmentTransitionSupport").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception e) {
            return null;
        }
    }

    static void retainValues(ArrayMap<String, String> arrayMap, ArrayMap<String, View> arrayMap2) {
        for (int size = arrayMap.size() - 1; size >= 0; size--) {
            if (!arrayMap2.containsKey(arrayMap.valueAt(size))) {
                arrayMap.removeAt(size);
            }
        }
    }

    private static void scheduleTargetChange(FragmentTransitionImpl impl, ViewGroup sceneRoot, Fragment inFragment, View nonExistentView, ArrayList<View> arrayList, Object enterTransition, ArrayList<View> arrayList2, Object exitTransition, ArrayList<View> arrayList3) {
        final Object obj = enterTransition;
        final FragmentTransitionImpl fragmentTransitionImpl = impl;
        final View view = nonExistentView;
        final Fragment fragment = inFragment;
        final ArrayList<View> arrayList4 = arrayList;
        final ArrayList<View> arrayList5 = arrayList2;
        final ArrayList<View> arrayList6 = arrayList3;
        final Object obj2 = exitTransition;
        ViewGroup viewGroup = sceneRoot;
        OneShotPreDrawListener.add(sceneRoot, new Runnable() {
            public void run() {
                Object obj = obj;
                if (obj != null) {
                    fragmentTransitionImpl.removeTarget(obj, view);
                    arrayList5.addAll(FragmentTransition.configureEnteringExitingViews(fragmentTransitionImpl, obj, fragment, arrayList4, view));
                }
                if (arrayList6 != null) {
                    if (obj2 != null) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(view);
                        fragmentTransitionImpl.replaceTargets(obj2, arrayList6, arrayList);
                    }
                    arrayList6.clear();
                    arrayList6.add(view);
                }
            }
        });
    }

    private static void setOutEpicenter(FragmentTransitionImpl impl, Object sharedElementTransition, Object exitTransition, ArrayMap<String, View> arrayMap, boolean outIsPop, BackStackRecord outTransaction) {
        if (outTransaction.mSharedElementSourceNames != null && !outTransaction.mSharedElementSourceNames.isEmpty()) {
            View view = arrayMap.get(outIsPop ? (String) outTransaction.mSharedElementTargetNames.get(0) : (String) outTransaction.mSharedElementSourceNames.get(0));
            impl.setEpicenter(sharedElementTransition, view);
            if (exitTransition != null) {
                impl.setEpicenter(exitTransition, view);
            }
        }
    }

    static void setViewVisibility(ArrayList<View> arrayList, int visibility) {
        if (arrayList != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                arrayList.get(size).setVisibility(visibility);
            }
        }
    }

    static void startTransitions(Context context, FragmentContainer fragmentContainer, ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int startIndex, int endIndex, boolean isReordered, Callback callback) {
        ViewGroup viewGroup;
        SparseArray sparseArray = new SparseArray();
        for (int i = startIndex; i < endIndex; i++) {
            BackStackRecord backStackRecord = arrayList.get(i);
            if (arrayList2.get(i).booleanValue()) {
                calculatePopFragments(backStackRecord, sparseArray, isReordered);
            } else {
                calculateFragments(backStackRecord, sparseArray, isReordered);
            }
        }
        if (sparseArray.size() != 0) {
            View view = new View(context);
            int size = sparseArray.size();
            for (int i2 = 0; i2 < size; i2++) {
                int keyAt = sparseArray.keyAt(i2);
                ArrayMap<String, String> calculateNameOverrides = calculateNameOverrides(keyAt, arrayList, arrayList2, startIndex, endIndex);
                FragmentContainerTransition fragmentContainerTransition = (FragmentContainerTransition) sparseArray.valueAt(i2);
                if (fragmentContainer.onHasView() && (viewGroup = (ViewGroup) fragmentContainer.onFindViewById(keyAt)) != null) {
                    if (isReordered) {
                        configureTransitionsReordered(viewGroup, fragmentContainerTransition, view, calculateNameOverrides, callback);
                    } else {
                        configureTransitionsOrdered(viewGroup, fragmentContainerTransition, view, calculateNameOverrides, callback);
                    }
                }
            }
        }
    }

    static boolean supportsTransition() {
        return (PLATFORM_IMPL == null && SUPPORT_IMPL == null) ? false : true;
    }

    static ArrayMap<String, View> captureInSharedElements(FragmentTransitionImpl impl, ArrayMap<String, String> arrayMap, Object sharedElementTransition, FragmentContainerTransition fragments) {
        ArrayList arrayList;
        SharedElementCallback sharedElementCallback;
        Fragment fragment = fragments.lastIn;
        View view = fragment.getView();
        if (arrayMap.isEmpty() || sharedElementTransition == null || view == null) {
            arrayMap.clear();
            return null;
        }
        ArrayMap<String, View> arrayMap2 = new ArrayMap<>();
        impl.findNamedViews(arrayMap2, view);
        BackStackRecord backStackRecord = fragments.lastInTransaction;
        if (fragments.lastInIsPop) {
            sharedElementCallback = fragment.getExitTransitionCallback();
            arrayList = backStackRecord.mSharedElementSourceNames;
        } else {
            sharedElementCallback = fragment.getEnterTransitionCallback();
            arrayList = backStackRecord.mSharedElementTargetNames;
        }
        if (arrayList != null) {
            arrayMap2.retainAll(arrayList);
            arrayMap2.retainAll(arrayMap.values());
        }
        if (sharedElementCallback != null) {
            sharedElementCallback.onMapSharedElements(arrayList, arrayMap2);
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                String str = (String) arrayList.get(size);
                View view2 = arrayMap2.get(str);
                if (view2 == null) {
                    String findKeyForValue = findKeyForValue(arrayMap, str);
                    Log1F380D.a((Object) findKeyForValue);
                    if (findKeyForValue != null) {
                        arrayMap.remove(findKeyForValue);
                    }
                } else {
                    String transitionName = ViewCompat.getTransitionName(view2);
                    Log1F380D.a((Object) transitionName);
                    if (!str.equals(transitionName)) {
                        String findKeyForValue2 = findKeyForValue(arrayMap, str);
                        Log1F380D.a((Object) findKeyForValue2);
                        if (findKeyForValue2 != null) {
                            String transitionName2 = ViewCompat.getTransitionName(view2);
                            Log1F380D.a((Object) transitionName2);
                            arrayMap.put(findKeyForValue2, transitionName2);
                        }
                    }
                }
            }
        } else {
            retainValues(arrayMap, arrayMap2);
        }
        return arrayMap2;
    }

    private static ArrayMap<String, View> captureOutSharedElements(FragmentTransitionImpl impl, ArrayMap<String, String> arrayMap, Object sharedElementTransition, FragmentContainerTransition fragments) {
        ArrayList arrayList;
        SharedElementCallback sharedElementCallback;
        if (arrayMap.isEmpty() || sharedElementTransition == null) {
            arrayMap.clear();
            return null;
        }
        Fragment fragment = fragments.firstOut;
        ArrayMap<String, View> arrayMap2 = new ArrayMap<>();
        impl.findNamedViews(arrayMap2, fragment.requireView());
        BackStackRecord backStackRecord = fragments.firstOutTransaction;
        if (fragments.firstOutIsPop) {
            sharedElementCallback = fragment.getEnterTransitionCallback();
            arrayList = backStackRecord.mSharedElementTargetNames;
        } else {
            sharedElementCallback = fragment.getExitTransitionCallback();
            arrayList = backStackRecord.mSharedElementSourceNames;
        }
        if (arrayList != null) {
            arrayMap2.retainAll(arrayList);
        }
        if (sharedElementCallback != null) {
            sharedElementCallback.onMapSharedElements(arrayList, arrayMap2);
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                String str = (String) arrayList.get(size);
                View view = arrayMap2.get(str);
                if (view == null) {
                    arrayMap.remove(str);
                } else {
                    String transitionName = ViewCompat.getTransitionName(view);
                    Log1F380D.a((Object) transitionName);
                    if (!str.equals(transitionName)) {
                        String transitionName2 = ViewCompat.getTransitionName(view);
                        Log1F380D.a((Object) transitionName2);
                        arrayMap.put(transitionName2, arrayMap.remove(str));
                    }
                }
            }
        } else {
            arrayMap.retainAll(arrayMap2.keySet());
        }
        return arrayMap2;
    }
}
