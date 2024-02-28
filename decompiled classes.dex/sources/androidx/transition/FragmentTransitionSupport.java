package androidx.transition;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentTransitionImpl;
import androidx.transition.Transition;
import java.util.ArrayList;
import java.util.List;

public class FragmentTransitionSupport extends FragmentTransitionImpl {
    private static boolean hasSimpleTarget(Transition transition) {
        return !isNullOrEmpty(transition.getTargetIds()) || !isNullOrEmpty(transition.getTargetNames()) || !isNullOrEmpty(transition.getTargetTypes());
    }

    public void addTarget(Object transitionObj, View view) {
        if (transitionObj != null) {
            ((Transition) transitionObj).addTarget(view);
        }
    }

    public void addTargets(Object transitionObj, ArrayList<View> arrayList) {
        Transition transition = (Transition) transitionObj;
        if (transition != null) {
            if (transition instanceof TransitionSet) {
                TransitionSet transitionSet = (TransitionSet) transition;
                int transitionCount = transitionSet.getTransitionCount();
                for (int i = 0; i < transitionCount; i++) {
                    addTargets(transitionSet.getTransitionAt(i), arrayList);
                }
            } else if (!hasSimpleTarget(transition) && isNullOrEmpty(transition.getTargets())) {
                int size = arrayList.size();
                for (int i2 = 0; i2 < size; i2++) {
                    transition.addTarget(arrayList.get(i2));
                }
            }
        }
    }

    public void beginDelayedTransition(ViewGroup sceneRoot, Object transition) {
        TransitionManager.beginDelayedTransition(sceneRoot, (Transition) transition);
    }

    public boolean canHandle(Object transition) {
        return transition instanceof Transition;
    }

    public Object cloneTransition(Object transition) {
        if (transition != null) {
            return ((Transition) transition).clone();
        }
        return null;
    }

    public Object mergeTransitionsInSequence(Object exitTransitionObj, Object enterTransitionObj, Object sharedElementTransitionObj) {
        Transition transition = null;
        Transition transition2 = (Transition) exitTransitionObj;
        Transition transition3 = (Transition) enterTransitionObj;
        Transition transition4 = (Transition) sharedElementTransitionObj;
        if (transition2 != null && transition3 != null) {
            transition = new TransitionSet().addTransition(transition2).addTransition(transition3).setOrdering(1);
        } else if (transition2 != null) {
            transition = transition2;
        } else if (transition3 != null) {
            transition = transition3;
        }
        if (transition4 == null) {
            return transition;
        }
        TransitionSet transitionSet = new TransitionSet();
        if (transition != null) {
            transitionSet.addTransition(transition);
        }
        transitionSet.addTransition(transition4);
        return transitionSet;
    }

    public Object mergeTransitionsTogether(Object transition1, Object transition2, Object transition3) {
        TransitionSet transitionSet = new TransitionSet();
        if (transition1 != null) {
            transitionSet.addTransition((Transition) transition1);
        }
        if (transition2 != null) {
            transitionSet.addTransition((Transition) transition2);
        }
        if (transition3 != null) {
            transitionSet.addTransition((Transition) transition3);
        }
        return transitionSet;
    }

    public void removeTarget(Object transitionObj, View view) {
        if (transitionObj != null) {
            ((Transition) transitionObj).removeTarget(view);
        }
    }

    public void replaceTargets(Object transitionObj, ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        Transition transition = (Transition) transitionObj;
        if (transition instanceof TransitionSet) {
            TransitionSet transitionSet = (TransitionSet) transition;
            int transitionCount = transitionSet.getTransitionCount();
            for (int i = 0; i < transitionCount; i++) {
                replaceTargets(transitionSet.getTransitionAt(i), arrayList, arrayList2);
            }
        } else if (!hasSimpleTarget(transition)) {
            List<View> targets = transition.getTargets();
            if (targets.size() == arrayList.size() && targets.containsAll(arrayList)) {
                int size = arrayList2 == null ? 0 : arrayList2.size();
                for (int i2 = 0; i2 < size; i2++) {
                    transition.addTarget(arrayList2.get(i2));
                }
                for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
                    transition.removeTarget(arrayList.get(size2));
                }
            }
        }
    }

    public void scheduleHideFragmentView(Object exitTransitionObj, final View fragmentView, final ArrayList<View> arrayList) {
        ((Transition) exitTransitionObj).addListener(new Transition.TransitionListener() {
            public void onTransitionCancel(Transition transition) {
            }

            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                fragmentView.setVisibility(8);
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    ((View) arrayList.get(i)).setVisibility(0);
                }
            }

            public void onTransitionPause(Transition transition) {
            }

            public void onTransitionResume(Transition transition) {
            }

            public void onTransitionStart(Transition transition) {
            }
        });
    }

    public void scheduleRemoveTargets(Object overallTransitionObj, Object enterTransition, ArrayList<View> arrayList, Object exitTransition, ArrayList<View> arrayList2, Object sharedElementTransition, ArrayList<View> arrayList3) {
        final Object obj = enterTransition;
        final ArrayList<View> arrayList4 = arrayList;
        final Object obj2 = exitTransition;
        final ArrayList<View> arrayList5 = arrayList2;
        final Object obj3 = sharedElementTransition;
        final ArrayList<View> arrayList6 = arrayList3;
        ((Transition) overallTransitionObj).addListener(new TransitionListenerAdapter() {
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
            }

            public void onTransitionStart(Transition transition) {
                Object obj = obj;
                if (obj != null) {
                    FragmentTransitionSupport.this.replaceTargets(obj, arrayList4, (ArrayList<View>) null);
                }
                Object obj2 = obj2;
                if (obj2 != null) {
                    FragmentTransitionSupport.this.replaceTargets(obj2, arrayList5, (ArrayList<View>) null);
                }
                Object obj3 = obj3;
                if (obj3 != null) {
                    FragmentTransitionSupport.this.replaceTargets(obj3, arrayList6, (ArrayList<View>) null);
                }
            }
        });
    }

    public void setEpicenter(Object transitionObj, final Rect epicenter) {
        if (transitionObj != null) {
            ((Transition) transitionObj).setEpicenterCallback(new Transition.EpicenterCallback() {
                public Rect onGetEpicenter(Transition transition) {
                    Rect rect = epicenter;
                    if (rect == null || rect.isEmpty()) {
                        return null;
                    }
                    return epicenter;
                }
            });
        }
    }

    public void setEpicenter(Object transitionObj, View view) {
        if (view != null) {
            final Rect rect = new Rect();
            getBoundsOnScreen(view, rect);
            ((Transition) transitionObj).setEpicenterCallback(new Transition.EpicenterCallback() {
                public Rect onGetEpicenter(Transition transition) {
                    return rect;
                }
            });
        }
    }

    public void setSharedElementTargets(Object transitionObj, View nonExistentView, ArrayList<View> arrayList) {
        TransitionSet transitionSet = (TransitionSet) transitionObj;
        List<View> targets = transitionSet.getTargets();
        targets.clear();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            bfsAddViewChildren(targets, arrayList.get(i));
        }
        targets.add(nonExistentView);
        arrayList.add(nonExistentView);
        addTargets(transitionSet, arrayList);
    }

    public void swapSharedElementTargets(Object sharedElementTransitionObj, ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        TransitionSet transitionSet = (TransitionSet) sharedElementTransitionObj;
        if (transitionSet != null) {
            transitionSet.getTargets().clear();
            transitionSet.getTargets().addAll(arrayList2);
            replaceTargets(transitionSet, arrayList, arrayList2);
        }
    }

    public Object wrapTransitionInSet(Object transition) {
        if (transition == null) {
            return null;
        }
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition((Transition) transition);
        return transitionSet;
    }
}
