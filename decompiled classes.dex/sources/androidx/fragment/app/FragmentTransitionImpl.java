package androidx.fragment.app;

import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.core.os.CancellationSignal;
import androidx.core.view.OneShotPreDrawListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewGroupCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mt.Log1F380D;

/* compiled from: 007F */
public abstract class FragmentTransitionImpl {
    protected static void bfsAddViewChildren(List<View> list, View startView) {
        int size = list.size();
        if (!containedBeforeIndex(list, startView, size)) {
            String transitionName = ViewCompat.getTransitionName(startView);
            Log1F380D.a((Object) transitionName);
            if (transitionName != null) {
                list.add(startView);
            }
            for (int i = size; i < list.size(); i++) {
                View view = list.get(i);
                if (view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    int childCount = viewGroup.getChildCount();
                    for (int i2 = 0; i2 < childCount; i2++) {
                        View childAt = viewGroup.getChildAt(i2);
                        if (!containedBeforeIndex(list, childAt, size)) {
                            String transitionName2 = ViewCompat.getTransitionName(childAt);
                            Log1F380D.a((Object) transitionName2);
                            if (transitionName2 != null) {
                                list.add(childAt);
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean containedBeforeIndex(List<View> list, View view, int maxIndex) {
        for (int i = 0; i < maxIndex; i++) {
            if (list.get(i) == view) {
                return true;
            }
        }
        return false;
    }

    static String findKeyForValue(Map<String, String> map, String value) {
        for (Map.Entry next : map.entrySet()) {
            if (value.equals(next.getValue())) {
                return (String) next.getKey();
            }
        }
        return null;
    }

    protected static boolean isNullOrEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public abstract void addTarget(Object obj, View view);

    public abstract void addTargets(Object obj, ArrayList<View> arrayList);

    public abstract void beginDelayedTransition(ViewGroup viewGroup, Object obj);

    public abstract boolean canHandle(Object obj);

    /* access modifiers changed from: package-private */
    public void captureTransitioningViews(ArrayList<View> arrayList, View view) {
        if (view.getVisibility() != 0) {
            return;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (ViewGroupCompat.isTransitionGroup(viewGroup)) {
                arrayList.add(viewGroup);
                return;
            }
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                captureTransitioningViews(arrayList, viewGroup.getChildAt(i));
            }
            return;
        }
        arrayList.add(view);
    }

    public abstract Object cloneTransition(Object obj);

    /* access modifiers changed from: protected */
    public void getBoundsOnScreen(View view, Rect epicenter) {
        if (ViewCompat.isAttachedToWindow(view)) {
            RectF rectF = new RectF();
            rectF.set(0.0f, 0.0f, (float) view.getWidth(), (float) view.getHeight());
            view.getMatrix().mapRect(rectF);
            rectF.offset((float) view.getLeft(), (float) view.getTop());
            ViewParent parent = view.getParent();
            while (parent instanceof View) {
                View view2 = (View) parent;
                rectF.offset((float) (-view2.getScrollX()), (float) (-view2.getScrollY()));
                view2.getMatrix().mapRect(rectF);
                rectF.offset((float) view2.getLeft(), (float) view2.getTop());
                parent = view2.getParent();
            }
            int[] iArr = new int[2];
            view.getRootView().getLocationOnScreen(iArr);
            rectF.offset((float) iArr[0], (float) iArr[1]);
            epicenter.set(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
        }
    }

    public abstract Object mergeTransitionsInSequence(Object obj, Object obj2, Object obj3);

    public abstract Object mergeTransitionsTogether(Object obj, Object obj2, Object obj3);

    public abstract void removeTarget(Object obj, View view);

    public abstract void replaceTargets(Object obj, ArrayList<View> arrayList, ArrayList<View> arrayList2);

    public abstract void scheduleHideFragmentView(Object obj, View view, ArrayList<View> arrayList);

    /* access modifiers changed from: package-private */
    public void scheduleNameReset(ViewGroup sceneRoot, final ArrayList<View> arrayList, final Map<String, String> map) {
        OneShotPreDrawListener.add(sceneRoot, new Runnable() {
            public void run() {
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    View view = (View) arrayList.get(i);
                    String transitionName = ViewCompat.getTransitionName(view);
                    Log1F380D.a((Object) transitionName);
                    ViewCompat.setTransitionName(view, (String) map.get(transitionName));
                }
            }
        });
    }

    public abstract void scheduleRemoveTargets(Object obj, Object obj2, ArrayList<View> arrayList, Object obj3, ArrayList<View> arrayList2, Object obj4, ArrayList<View> arrayList3);

    public abstract void setEpicenter(Object obj, Rect rect);

    public abstract void setEpicenter(Object obj, View view);

    public void setListenerForTransitionEnd(Fragment outFragment, Object transition, CancellationSignal signal, Runnable transitionCompleteRunnable) {
        transitionCompleteRunnable.run();
    }

    /* access modifiers changed from: package-private */
    public void setNameOverridesOrdered(View sceneRoot, final ArrayList<View> arrayList, final Map<String, String> map) {
        OneShotPreDrawListener.add(sceneRoot, new Runnable() {
            public void run() {
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    View view = (View) arrayList.get(i);
                    String transitionName = ViewCompat.getTransitionName(view);
                    Log1F380D.a((Object) transitionName);
                    if (transitionName != null) {
                        String findKeyForValue = FragmentTransitionImpl.findKeyForValue(map, transitionName);
                        Log1F380D.a((Object) findKeyForValue);
                        ViewCompat.setTransitionName(view, findKeyForValue);
                    }
                }
            }
        });
    }

    public abstract void setSharedElementTargets(Object obj, View view, ArrayList<View> arrayList);

    public abstract void swapSharedElementTargets(Object obj, ArrayList<View> arrayList, ArrayList<View> arrayList2);

    public abstract Object wrapTransitionInSet(Object obj);

    /* access modifiers changed from: package-private */
    public void findNamedViews(Map<String, View> map, View view) {
        if (view.getVisibility() == 0) {
            String transitionName = ViewCompat.getTransitionName(view);
            Log1F380D.a((Object) transitionName);
            if (transitionName != null) {
                map.put(transitionName, view);
            }
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    findNamedViews(map, viewGroup.getChildAt(i));
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public ArrayList<String> prepareSetNameOverridesReordered(ArrayList<View> arrayList) {
        ArrayList<String> arrayList2 = new ArrayList<>();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            View view = arrayList.get(i);
            String transitionName = ViewCompat.getTransitionName(view);
            Log1F380D.a((Object) transitionName);
            arrayList2.add(transitionName);
            ViewCompat.setTransitionName(view, (String) null);
        }
        return arrayList2;
    }

    /* access modifiers changed from: package-private */
    public void setNameOverridesReordered(View sceneRoot, ArrayList<View> arrayList, ArrayList<View> arrayList2, ArrayList<String> arrayList3, Map<String, String> map) {
        int size = arrayList2.size();
        ArrayList arrayList4 = new ArrayList();
        for (int i = 0; i < size; i++) {
            View view = arrayList.get(i);
            String transitionName = ViewCompat.getTransitionName(view);
            Log1F380D.a((Object) transitionName);
            arrayList4.add(transitionName);
            if (transitionName != null) {
                ViewCompat.setTransitionName(view, (String) null);
                String str = map.get(transitionName);
                int i2 = 0;
                while (true) {
                    if (i2 >= size) {
                        break;
                    } else if (str.equals(arrayList3.get(i2))) {
                        ViewCompat.setTransitionName(arrayList2.get(i2), transitionName);
                        break;
                    } else {
                        i2++;
                    }
                }
            }
        }
        final int i3 = size;
        final ArrayList<View> arrayList5 = arrayList2;
        final ArrayList<String> arrayList6 = arrayList3;
        final ArrayList<View> arrayList7 = arrayList;
        final ArrayList arrayList8 = arrayList4;
        OneShotPreDrawListener.add(sceneRoot, new Runnable() {
            public void run() {
                for (int i = 0; i < i3; i++) {
                    ViewCompat.setTransitionName((View) arrayList5.get(i), (String) arrayList6.get(i));
                    ViewCompat.setTransitionName((View) arrayList7.get(i), (String) arrayList8.get(i));
                }
            }
        });
    }
}
