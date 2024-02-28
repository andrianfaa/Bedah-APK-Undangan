package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import androidx.collection.ArrayMap;
import androidx.core.os.CancellationSignal;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewGroupCompat;
import androidx.fragment.app.FragmentAnim;
import androidx.fragment.app.SpecialEffectsController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mt.Log1F380D;

/* compiled from: 0076 */
class DefaultSpecialEffectsController extends SpecialEffectsController {

    /* renamed from: androidx.fragment.app.DefaultSpecialEffectsController$10  reason: invalid class name */
    static /* synthetic */ class AnonymousClass10 {
        static final /* synthetic */ int[] $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State;

        static {
            int[] iArr = new int[SpecialEffectsController.Operation.State.values().length];
            $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State = iArr;
            try {
                iArr[SpecialEffectsController.Operation.State.GONE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State[SpecialEffectsController.Operation.State.INVISIBLE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State[SpecialEffectsController.Operation.State.REMOVED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State[SpecialEffectsController.Operation.State.VISIBLE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private static class AnimationInfo extends SpecialEffectsInfo {
        private FragmentAnim.AnimationOrAnimator mAnimation;
        private boolean mIsPop;
        private boolean mLoadedAnim = false;

        AnimationInfo(SpecialEffectsController.Operation operation, CancellationSignal signal, boolean isPop) {
            super(operation, signal);
            this.mIsPop = isPop;
        }

        /* access modifiers changed from: package-private */
        public FragmentAnim.AnimationOrAnimator getAnimation(Context context) {
            if (this.mLoadedAnim) {
                return this.mAnimation;
            }
            FragmentAnim.AnimationOrAnimator loadAnimation = FragmentAnim.loadAnimation(context, getOperation().getFragment(), getOperation().getFinalState() == SpecialEffectsController.Operation.State.VISIBLE, this.mIsPop);
            this.mAnimation = loadAnimation;
            this.mLoadedAnim = true;
            return loadAnimation;
        }
    }

    private static class SpecialEffectsInfo {
        private final SpecialEffectsController.Operation mOperation;
        private final CancellationSignal mSignal;

        SpecialEffectsInfo(SpecialEffectsController.Operation operation, CancellationSignal signal) {
            this.mOperation = operation;
            this.mSignal = signal;
        }

        /* access modifiers changed from: package-private */
        public void completeSpecialEffect() {
            this.mOperation.completeSpecialEffect(this.mSignal);
        }

        /* access modifiers changed from: package-private */
        public SpecialEffectsController.Operation getOperation() {
            return this.mOperation;
        }

        /* access modifiers changed from: package-private */
        public CancellationSignal getSignal() {
            return this.mSignal;
        }

        /* access modifiers changed from: package-private */
        public boolean isVisibilityUnchanged() {
            SpecialEffectsController.Operation.State from = SpecialEffectsController.Operation.State.from(this.mOperation.getFragment().mView);
            SpecialEffectsController.Operation.State finalState = this.mOperation.getFinalState();
            return from == finalState || !(from == SpecialEffectsController.Operation.State.VISIBLE || finalState == SpecialEffectsController.Operation.State.VISIBLE);
        }
    }

    private static class TransitionInfo extends SpecialEffectsInfo {
        private final boolean mOverlapAllowed;
        private final Object mSharedElementTransition;
        private final Object mTransition;

        TransitionInfo(SpecialEffectsController.Operation operation, CancellationSignal signal, boolean isPop, boolean providesSharedElementTransition) {
            super(operation, signal);
            if (operation.getFinalState() == SpecialEffectsController.Operation.State.VISIBLE) {
                this.mTransition = isPop ? operation.getFragment().getReenterTransition() : operation.getFragment().getEnterTransition();
                this.mOverlapAllowed = isPop ? operation.getFragment().getAllowReturnTransitionOverlap() : operation.getFragment().getAllowEnterTransitionOverlap();
            } else {
                this.mTransition = isPop ? operation.getFragment().getReturnTransition() : operation.getFragment().getExitTransition();
                this.mOverlapAllowed = true;
            }
            if (!providesSharedElementTransition) {
                this.mSharedElementTransition = null;
            } else if (isPop) {
                this.mSharedElementTransition = operation.getFragment().getSharedElementReturnTransition();
            } else {
                this.mSharedElementTransition = operation.getFragment().getSharedElementEnterTransition();
            }
        }

        private FragmentTransitionImpl getHandlingImpl(Object transition) {
            if (transition == null) {
                return null;
            }
            if (FragmentTransition.PLATFORM_IMPL != null && FragmentTransition.PLATFORM_IMPL.canHandle(transition)) {
                return FragmentTransition.PLATFORM_IMPL;
            }
            if (FragmentTransition.SUPPORT_IMPL != null && FragmentTransition.SUPPORT_IMPL.canHandle(transition)) {
                return FragmentTransition.SUPPORT_IMPL;
            }
            throw new IllegalArgumentException("Transition " + transition + " for fragment " + getOperation().getFragment() + " is not a valid framework Transition or AndroidX Transition");
        }

        /* access modifiers changed from: package-private */
        public FragmentTransitionImpl getHandlingImpl() {
            FragmentTransitionImpl handlingImpl = getHandlingImpl(this.mTransition);
            FragmentTransitionImpl handlingImpl2 = getHandlingImpl(this.mSharedElementTransition);
            if (handlingImpl == null || handlingImpl2 == null || handlingImpl == handlingImpl2) {
                return handlingImpl != null ? handlingImpl : handlingImpl2;
            }
            throw new IllegalArgumentException("Mixing framework transitions and AndroidX transitions is not allowed. Fragment " + getOperation().getFragment() + " returned Transition " + this.mTransition + " which uses a different Transition  type than its shared element transition " + this.mSharedElementTransition);
        }

        public Object getSharedElementTransition() {
            return this.mSharedElementTransition;
        }

        /* access modifiers changed from: package-private */
        public Object getTransition() {
            return this.mTransition;
        }

        public boolean hasSharedElementTransition() {
            return this.mSharedElementTransition != null;
        }

        /* access modifiers changed from: package-private */
        public boolean isOverlapAllowed() {
            return this.mOverlapAllowed;
        }
    }

    DefaultSpecialEffectsController(ViewGroup container) {
        super(container);
    }

    private void startAnimations(List<AnimationInfo> list, List<SpecialEffectsController.Operation> list2, boolean startedAnyTransition, Map<SpecialEffectsController.Operation, Boolean> map) {
        final ViewGroup container = getContainer();
        Context context = container.getContext();
        ArrayList arrayList = new ArrayList();
        boolean z = false;
        Iterator<AnimationInfo> it = list.iterator();
        while (it.hasNext()) {
            AnimationInfo next = it.next();
            if (next.isVisibilityUnchanged()) {
                next.completeSpecialEffect();
                Map<SpecialEffectsController.Operation, Boolean> map2 = map;
            } else {
                FragmentAnim.AnimationOrAnimator animation = next.getAnimation(context);
                if (animation == null) {
                    next.completeSpecialEffect();
                    Map<SpecialEffectsController.Operation, Boolean> map3 = map;
                } else {
                    final Animator animator = animation.animator;
                    if (animator == null) {
                        arrayList.add(next);
                        Map<SpecialEffectsController.Operation, Boolean> map4 = map;
                    } else {
                        SpecialEffectsController.Operation operation = next.getOperation();
                        Fragment fragment = operation.getFragment();
                        if (Boolean.TRUE.equals(map.get(operation))) {
                            if (FragmentManager.isLoggingEnabled(2)) {
                                Log.v("FragmentManager", "Ignoring Animator set on " + fragment + " as this Fragment was involved in a Transition.");
                            }
                            next.completeSpecialEffect();
                        } else {
                            boolean z2 = operation.getFinalState() == SpecialEffectsController.Operation.State.GONE;
                            if (z2) {
                                list2.remove(operation);
                            } else {
                                List<SpecialEffectsController.Operation> list3 = list2;
                            }
                            final View view = fragment.mView;
                            container.startViewTransition(view);
                            Iterator<AnimationInfo> it2 = it;
                            AnonymousClass2 r11 = r0;
                            final ViewGroup viewGroup = container;
                            final boolean z3 = z2;
                            final SpecialEffectsController.Operation operation2 = operation;
                            Fragment fragment2 = fragment;
                            final AnimationInfo animationInfo = next;
                            AnonymousClass2 r0 = new AnimatorListenerAdapter() {
                                public void onAnimationEnd(Animator anim) {
                                    viewGroup.endViewTransition(view);
                                    if (z3) {
                                        operation2.getFinalState().applyState(view);
                                    }
                                    animationInfo.completeSpecialEffect();
                                }
                            };
                            animator.addListener(r11);
                            animator.setTarget(view);
                            animator.start();
                            next.getSignal().setOnCancelListener(new CancellationSignal.OnCancelListener() {
                                public void onCancel() {
                                    animator.end();
                                }
                            });
                            z = true;
                            it = it2;
                        }
                    }
                }
            }
        }
        Iterator it3 = arrayList.iterator();
        while (it3.hasNext()) {
            final AnimationInfo animationInfo2 = (AnimationInfo) it3.next();
            SpecialEffectsController.Operation operation3 = animationInfo2.getOperation();
            Fragment fragment3 = operation3.getFragment();
            if (startedAnyTransition) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    Log.v("FragmentManager", "Ignoring Animation set on " + fragment3 + " as Animations cannot run alongside Transitions.");
                }
                animationInfo2.completeSpecialEffect();
            } else if (z) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    Log.v("FragmentManager", "Ignoring Animation set on " + fragment3 + " as Animations cannot run alongside Animators.");
                }
                animationInfo2.completeSpecialEffect();
            } else {
                final View view2 = fragment3.mView;
                Animation animation2 = (Animation) Preconditions.checkNotNull(((FragmentAnim.AnimationOrAnimator) Preconditions.checkNotNull(animationInfo2.getAnimation(context))).animation);
                if (operation3.getFinalState() != SpecialEffectsController.Operation.State.REMOVED) {
                    view2.startAnimation(animation2);
                    animationInfo2.completeSpecialEffect();
                } else {
                    container.startViewTransition(view2);
                    FragmentAnim.EndViewTransitionAnimation endViewTransitionAnimation = new FragmentAnim.EndViewTransitionAnimation(animation2, container, view2);
                    endViewTransitionAnimation.setAnimationListener(new Animation.AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            container.post(new Runnable() {
                                public void run() {
                                    container.endViewTransition(view2);
                                    animationInfo2.completeSpecialEffect();
                                }
                            });
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {
                        }
                    });
                    view2.startAnimation(endViewTransitionAnimation);
                }
                animationInfo2.getSignal().setOnCancelListener(new CancellationSignal.OnCancelListener() {
                    public void onCancel() {
                        view2.clearAnimation();
                        container.endViewTransition(view2);
                        animationInfo2.completeSpecialEffect();
                    }
                });
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v22, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v20, resolved type: android.view.View} */
    /* JADX WARNING: Code restructure failed: missing block: B:141:0x0526, code lost:
        if (r11 == r43) goto L_0x052d;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:151:0x0548  */
    /* JADX WARNING: Removed duplicated region for block: B:156:0x057f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.Map<androidx.fragment.app.SpecialEffectsController.Operation, java.lang.Boolean> startTransitions(java.util.List<androidx.fragment.app.DefaultSpecialEffectsController.TransitionInfo> r39, java.util.List<androidx.fragment.app.SpecialEffectsController.Operation> r40, boolean r41, androidx.fragment.app.SpecialEffectsController.Operation r42, androidx.fragment.app.SpecialEffectsController.Operation r43) {
        /*
            r38 = this;
            r6 = r38
            r7 = r41
            r8 = r42
            r9 = r43
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            r10 = r0
            r0 = 0
            java.util.Iterator r1 = r39.iterator()
            r15 = r0
        L_0x0014:
            boolean r0 = r1.hasNext()
            if (r0 == 0) goto L_0x006b
            java.lang.Object r0 = r1.next()
            androidx.fragment.app.DefaultSpecialEffectsController$TransitionInfo r0 = (androidx.fragment.app.DefaultSpecialEffectsController.TransitionInfo) r0
            boolean r2 = r0.isVisibilityUnchanged()
            if (r2 == 0) goto L_0x0027
            goto L_0x0014
        L_0x0027:
            androidx.fragment.app.FragmentTransitionImpl r2 = r0.getHandlingImpl()
            if (r15 != 0) goto L_0x0030
            r3 = r2
            r15 = r3
            goto L_0x006a
        L_0x0030:
            if (r2 == 0) goto L_0x006a
            if (r15 != r2) goto L_0x0035
            goto L_0x006a
        L_0x0035:
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Mixing framework transitions and AndroidX transitions is not allowed. Fragment "
            java.lang.StringBuilder r3 = r3.append(r4)
            androidx.fragment.app.SpecialEffectsController$Operation r4 = r0.getOperation()
            androidx.fragment.app.Fragment r4 = r4.getFragment()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = " returned Transition "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.Object r4 = r0.getTransition()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = " which uses a different Transition  type than other Fragments."
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            r1.<init>(r3)
            throw r1
        L_0x006a:
            goto L_0x0014
        L_0x006b:
            r14 = 0
            if (r15 != 0) goto L_0x008e
            java.util.Iterator r0 = r39.iterator()
        L_0x0072:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x008d
            java.lang.Object r1 = r0.next()
            androidx.fragment.app.DefaultSpecialEffectsController$TransitionInfo r1 = (androidx.fragment.app.DefaultSpecialEffectsController.TransitionInfo) r1
            androidx.fragment.app.SpecialEffectsController$Operation r2 = r1.getOperation()
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r14)
            r10.put(r2, r3)
            r1.completeSpecialEffect()
            goto L_0x0072
        L_0x008d:
            return r10
        L_0x008e:
            android.view.View r0 = new android.view.View
            android.view.ViewGroup r1 = r38.getContainer()
            android.content.Context r1 = r1.getContext()
            r0.<init>(r1)
            r13 = r0
            r0 = 0
            r1 = 0
            r2 = 0
            android.graphics.Rect r3 = new android.graphics.Rect
            r3.<init>()
            r12 = r3
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r11 = r3
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r5 = r3
            androidx.collection.ArrayMap r3 = new androidx.collection.ArrayMap
            r3.<init>()
            r4 = r3
            java.util.Iterator r19 = r39.iterator()
            r3 = r1
            r20 = r2
        L_0x00be:
            boolean r1 = r19.hasNext()
            if (r1 == 0) goto L_0x0384
            java.lang.Object r1 = r19.next()
            r21 = r1
            androidx.fragment.app.DefaultSpecialEffectsController$TransitionInfo r21 = (androidx.fragment.app.DefaultSpecialEffectsController.TransitionInfo) r21
            boolean r22 = r21.hasSharedElementTransition()
            if (r22 == 0) goto L_0x0360
            if (r8 == 0) goto L_0x0360
            if (r9 == 0) goto L_0x0360
            java.lang.Object r1 = r21.getSharedElementTransition()
            java.lang.Object r1 = r15.cloneTransition(r1)
            java.lang.Object r1 = r15.wrapTransitionInSet(r1)
            androidx.fragment.app.Fragment r0 = r43.getFragment()
            java.util.ArrayList r0 = r0.getSharedElementSourceNames()
            androidx.fragment.app.Fragment r16 = r42.getFragment()
            java.util.ArrayList r14 = r16.getSharedElementSourceNames()
            androidx.fragment.app.Fragment r16 = r42.getFragment()
            java.util.ArrayList r2 = r16.getSharedElementTargetNames()
            r16 = 0
            r23 = r1
            r1 = r16
        L_0x0101:
            r16 = r3
            int r3 = r2.size()
            if (r1 >= r3) goto L_0x0124
            java.lang.Object r3 = r2.get(r1)
            int r3 = r0.indexOf(r3)
            r24 = r2
            r2 = -1
            if (r3 == r2) goto L_0x011d
            java.lang.Object r2 = r14.get(r1)
            r0.set(r3, r2)
        L_0x011d:
            int r1 = r1 + 1
            r3 = r16
            r2 = r24
            goto L_0x0101
        L_0x0124:
            r24 = r2
            androidx.fragment.app.Fragment r1 = r43.getFragment()
            java.util.ArrayList r3 = r1.getSharedElementTargetNames()
            if (r7 != 0) goto L_0x0146
            androidx.fragment.app.Fragment r1 = r42.getFragment()
            androidx.core.app.SharedElementCallback r1 = r1.getExitTransitionCallback()
            androidx.fragment.app.Fragment r2 = r43.getFragment()
            androidx.core.app.SharedElementCallback r2 = r2.getEnterTransitionCallback()
            r37 = r2
            r2 = r1
            r1 = r37
            goto L_0x015b
        L_0x0146:
            androidx.fragment.app.Fragment r1 = r42.getFragment()
            androidx.core.app.SharedElementCallback r1 = r1.getEnterTransitionCallback()
            androidx.fragment.app.Fragment r2 = r43.getFragment()
            androidx.core.app.SharedElementCallback r2 = r2.getExitTransitionCallback()
            r37 = r2
            r2 = r1
            r1 = r37
        L_0x015b:
            r25 = r14
            int r14 = r0.size()
            r26 = 0
            r9 = r26
        L_0x0165:
            if (r9 >= r14) goto L_0x0183
            java.lang.Object r26 = r0.get(r9)
            r27 = r14
            r14 = r26
            java.lang.String r14 = (java.lang.String) r14
            java.lang.Object r26 = r3.get(r9)
            r8 = r26
            java.lang.String r8 = (java.lang.String) r8
            r4.put(r14, r8)
            int r9 = r9 + 1
            r8 = r42
            r14 = r27
            goto L_0x0165
        L_0x0183:
            r27 = r14
            androidx.collection.ArrayMap r8 = new androidx.collection.ArrayMap
            r8.<init>()
            androidx.fragment.app.Fragment r9 = r42.getFragment()
            android.view.View r9 = r9.mView
            r6.findNamedViews(r8, r9)
            r8.retainAll(r0)
            if (r2 == 0) goto L_0x01e9
            r2.onMapSharedElements(r0, r8)
            int r9 = r0.size()
            r14 = 1
            int r9 = r9 - r14
        L_0x01a1:
            if (r9 < 0) goto L_0x01e4
            java.lang.Object r14 = r0.get(r9)
            java.lang.String r14 = (java.lang.String) r14
            java.lang.Object r26 = r8.get(r14)
            android.view.View r26 = (android.view.View) r26
            if (r26 != 0) goto L_0x01b9
            r4.remove(r14)
            r28 = r0
            r29 = r2
            goto L_0x01dd
        L_0x01b9:
            r28 = r0
            java.lang.String r0 = androidx.core.view.ViewCompat.getTransitionName(r26)
            mt.Log1F380D.a((java.lang.Object) r0)
            boolean r0 = r14.equals(r0)
            if (r0 != 0) goto L_0x01db
            java.lang.Object r0 = r4.remove(r14)
            java.lang.String r0 = (java.lang.String) r0
            r29 = r2
            java.lang.String r2 = androidx.core.view.ViewCompat.getTransitionName(r26)
            mt.Log1F380D.a((java.lang.Object) r2)
            r4.put(r2, r0)
            goto L_0x01dd
        L_0x01db:
            r29 = r2
        L_0x01dd:
            int r9 = r9 + -1
            r0 = r28
            r2 = r29
            goto L_0x01a1
        L_0x01e4:
            r28 = r0
            r29 = r2
            goto L_0x01f4
        L_0x01e9:
            r28 = r0
            r29 = r2
            java.util.Set r0 = r8.keySet()
            r4.retainAll(r0)
        L_0x01f4:
            androidx.collection.ArrayMap r0 = new androidx.collection.ArrayMap
            r0.<init>()
            r9 = r0
            androidx.fragment.app.Fragment r0 = r43.getFragment()
            android.view.View r0 = r0.mView
            r6.findNamedViews(r9, r0)
            r9.retainAll(r3)
            java.util.Collection r0 = r4.values()
            r9.retainAll(r0)
            if (r1 == 0) goto L_0x026a
            r1.onMapSharedElements(r3, r9)
            int r0 = r3.size()
            r2 = 1
            int r0 = r0 - r2
        L_0x0218:
            if (r0 < 0) goto L_0x0267
            java.lang.Object r2 = r3.get(r0)
            java.lang.String r2 = (java.lang.String) r2
            java.lang.Object r14 = r9.get(r2)
            android.view.View r14 = (android.view.View) r14
            if (r14 != 0) goto L_0x0237
            r26 = r1
            java.lang.String r1 = androidx.fragment.app.FragmentTransition.findKeyForValue(r4, r2)
            mt.Log1F380D.a((java.lang.Object) r1)
            if (r1 == 0) goto L_0x0236
            r4.remove(r1)
        L_0x0236:
            goto L_0x0262
        L_0x0237:
            r26 = r1
            java.lang.String r1 = androidx.core.view.ViewCompat.getTransitionName(r14)
            mt.Log1F380D.a((java.lang.Object) r1)
            boolean r1 = r2.equals(r1)
            if (r1 != 0) goto L_0x0260
            java.lang.String r1 = androidx.fragment.app.FragmentTransition.findKeyForValue(r4, r2)
            mt.Log1F380D.a((java.lang.Object) r1)
            if (r1 == 0) goto L_0x025d
            r30 = r2
            java.lang.String r2 = androidx.core.view.ViewCompat.getTransitionName(r14)
            mt.Log1F380D.a((java.lang.Object) r2)
            r4.put(r1, r2)
            goto L_0x0262
        L_0x025d:
            r30 = r2
            goto L_0x0262
        L_0x0260:
            r30 = r2
        L_0x0262:
            int r0 = r0 + -1
            r1 = r26
            goto L_0x0218
        L_0x0267:
            r26 = r1
            goto L_0x026f
        L_0x026a:
            r26 = r1
            androidx.fragment.app.FragmentTransition.retainValues(r4, r9)
        L_0x026f:
            java.util.Set r0 = r4.keySet()
            r6.retainMatchingViews(r8, r0)
            java.util.Collection r0 = r4.values()
            r6.retainMatchingViews(r9, r0)
            boolean r0 = r4.isEmpty()
            if (r0 == 0) goto L_0x029e
            r0 = 0
            r11.clear()
            r5.clear()
            r33 = r4
            r30 = r5
            r14 = r10
            r10 = r11
            r34 = r12
            r2 = r13
            r35 = r15
            r3 = r16
            r4 = 0
            r15 = r42
            r13 = r43
            goto L_0x0372
        L_0x029e:
            androidx.fragment.app.Fragment r0 = r43.getFragment()
            androidx.fragment.app.Fragment r1 = r42.getFragment()
            r2 = 1
            androidx.fragment.app.FragmentTransition.callSharedElementStartEnd(r0, r1, r7, r8, r2)
            android.view.ViewGroup r14 = r38.getContainer()
            androidx.fragment.app.DefaultSpecialEffectsController$6 r1 = new androidx.fragment.app.DefaultSpecialEffectsController$6
            r30 = r28
            r0 = r1
            r7 = r23
            r23 = r26
            r26 = r10
            r10 = r1
            r1 = r38
            r28 = r29
            r29 = r2
            r2 = r43
            r32 = r3
            r31 = r16
            r3 = r42
            r33 = r4
            r4 = r41
            r16 = r13
            r13 = r5
            r5 = r9
            r0.<init>(r2, r3, r4, r5)
            androidx.core.view.OneShotPreDrawListener.add(r14, r10)
            java.util.Collection r0 = r8.values()
            r11.addAll(r0)
            boolean r0 = r30.isEmpty()
            if (r0 != 0) goto L_0x02f9
            r0 = r30
            r1 = 0
            java.lang.Object r2 = r0.get(r1)
            r1 = r2
            java.lang.String r1 = (java.lang.String) r1
            java.lang.Object r2 = r8.get(r1)
            r3 = r2
            android.view.View r3 = (android.view.View) r3
            r15.setEpicenter((java.lang.Object) r7, (android.view.View) r3)
            goto L_0x02fd
        L_0x02f9:
            r0 = r30
            r3 = r31
        L_0x02fd:
            java.util.Collection r1 = r9.values()
            r13.addAll(r1)
            boolean r1 = r32.isEmpty()
            if (r1 != 0) goto L_0x032b
            r1 = r32
            r2 = 0
            java.lang.Object r4 = r1.get(r2)
            java.lang.String r4 = (java.lang.String) r4
            java.lang.Object r5 = r9.get(r4)
            android.view.View r5 = (android.view.View) r5
            if (r5 == 0) goto L_0x032d
            r20 = 1
            r10 = r15
            android.view.ViewGroup r14 = r38.getContainer()
            androidx.fragment.app.DefaultSpecialEffectsController$7 r2 = new androidx.fragment.app.DefaultSpecialEffectsController$7
            r2.<init>(r10, r5, r12)
            androidx.core.view.OneShotPreDrawListener.add(r14, r2)
            goto L_0x032d
        L_0x032b:
            r1 = r32
        L_0x032d:
            r2 = r16
            r15.setSharedElementTargets(r7, r2, r11)
            r4 = 0
            r14 = 0
            r5 = 0
            r16 = 0
            r10 = r11
            r11 = r15
            r34 = r12
            r12 = r7
            r30 = r13
            r13 = r4
            r4 = 0
            r35 = r15
            r15 = r5
            r17 = r7
            r18 = r30
            r11.scheduleRemoveTargets(r12, r13, r14, r15, r16, r17, r18)
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r29)
            r15 = r42
            r14 = r26
            r14.put(r15, r5)
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r29)
            r13 = r43
            r14.put(r13, r5)
            r0 = r7
            goto L_0x0372
        L_0x0360:
            r31 = r3
            r33 = r4
            r30 = r5
            r34 = r12
            r2 = r13
            r4 = r14
            r35 = r15
            r15 = r8
            r13 = r9
            r14 = r10
            r10 = r11
            r3 = r31
        L_0x0372:
            r7 = r41
            r11 = r10
            r9 = r13
            r10 = r14
            r8 = r15
            r5 = r30
            r12 = r34
            r15 = r35
            r13 = r2
            r14 = r4
            r4 = r33
            goto L_0x00be
        L_0x0384:
            r31 = r3
            r33 = r4
            r30 = r5
            r34 = r12
            r2 = r13
            r4 = r14
            r35 = r15
            r29 = 1
            r15 = r8
            r13 = r9
            r14 = r10
            r10 = r11
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            r3 = 0
            r5 = 0
            java.util.Iterator r7 = r39.iterator()
        L_0x03a1:
            boolean r8 = r7.hasNext()
            if (r8 == 0) goto L_0x04eb
            java.lang.Object r8 = r7.next()
            androidx.fragment.app.DefaultSpecialEffectsController$TransitionInfo r8 = (androidx.fragment.app.DefaultSpecialEffectsController.TransitionInfo) r8
            boolean r9 = r8.isVisibilityUnchanged()
            if (r9 == 0) goto L_0x03c2
            androidx.fragment.app.SpecialEffectsController$Operation r9 = r8.getOperation()
            java.lang.Boolean r11 = java.lang.Boolean.valueOf(r4)
            r14.put(r9, r11)
            r8.completeSpecialEffect()
            goto L_0x03a1
        L_0x03c2:
            java.lang.Object r9 = r8.getTransition()
            r12 = r35
            java.lang.Object r9 = r12.cloneTransition(r9)
            androidx.fragment.app.SpecialEffectsController$Operation r11 = r8.getOperation()
            if (r0 == 0) goto L_0x03d9
            if (r11 == r15) goto L_0x03d6
            if (r11 != r13) goto L_0x03d9
        L_0x03d6:
            r16 = r29
            goto L_0x03db
        L_0x03d9:
            r16 = r4
        L_0x03db:
            r19 = r16
            if (r9 != 0) goto L_0x0401
            if (r19 != 0) goto L_0x03ee
            r21 = r7
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r4)
            r14.put(r11, r7)
            r8.completeSpecialEffect()
            goto L_0x03f0
        L_0x03ee:
            r21 = r7
        L_0x03f0:
            r25 = r2
            r26 = r10
            r2 = r14
            r10 = r15
            r4 = r30
            r13 = r34
            r15 = r40
            r14 = r12
            r12 = r31
            goto L_0x04d6
        L_0x0401:
            r21 = r7
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>()
            androidx.fragment.app.Fragment r4 = r11.getFragment()
            android.view.View r4 = r4.mView
            r6.captureTransitioningViews(r7, r4)
            if (r19 == 0) goto L_0x0422
            if (r11 != r15) goto L_0x041c
            r7.removeAll(r10)
            r4 = r30
            goto L_0x0424
        L_0x041c:
            r4 = r30
            r7.removeAll(r4)
            goto L_0x0424
        L_0x0422:
            r4 = r30
        L_0x0424:
            boolean r16 = r7.isEmpty()
            if (r16 == 0) goto L_0x0438
            r12.addTarget(r9, r2)
            r25 = r2
            r26 = r10
            r2 = r14
            r10 = r15
            r15 = r40
            r14 = r12
            goto L_0x049a
        L_0x0438:
            r12.addTargets(r9, r7)
            r16 = 0
            r17 = 0
            r18 = 0
            r23 = 0
            r24 = r11
            r11 = r12
            r36 = r12
            r12 = r9
            r13 = r9
            r25 = r2
            r2 = r14
            r14 = r7
            r26 = r10
            r10 = r15
            r15 = r16
            r16 = r17
            r17 = r18
            r18 = r23
            r11.scheduleRemoveTargets(r12, r13, r14, r15, r16, r17, r18)
            androidx.fragment.app.SpecialEffectsController$Operation$State r11 = r24.getFinalState()
            androidx.fragment.app.SpecialEffectsController$Operation$State r12 = androidx.fragment.app.SpecialEffectsController.Operation.State.GONE
            if (r11 != r12) goto L_0x0494
            r15 = r40
            r11 = r24
            r15.remove(r11)
            java.util.ArrayList r12 = new java.util.ArrayList
            r12.<init>(r7)
            androidx.fragment.app.Fragment r13 = r11.getFragment()
            android.view.View r13 = r13.mView
            r12.remove(r13)
            androidx.fragment.app.Fragment r13 = r11.getFragment()
            android.view.View r13 = r13.mView
            r14 = r36
            r14.scheduleHideFragmentView(r9, r13, r12)
            android.view.ViewGroup r13 = r38.getContainer()
            r16 = r12
            androidx.fragment.app.DefaultSpecialEffectsController$8 r12 = new androidx.fragment.app.DefaultSpecialEffectsController$8
            r12.<init>(r7)
            androidx.core.view.OneShotPreDrawListener.add(r13, r12)
            goto L_0x049a
        L_0x0494:
            r15 = r40
            r11 = r24
            r14 = r36
        L_0x049a:
            androidx.fragment.app.SpecialEffectsController$Operation$State r12 = r11.getFinalState()
            androidx.fragment.app.SpecialEffectsController$Operation$State r13 = androidx.fragment.app.SpecialEffectsController.Operation.State.VISIBLE
            if (r12 != r13) goto L_0x04b4
            r1.addAll(r7)
            if (r20 == 0) goto L_0x04af
            r13 = r34
            r14.setEpicenter((java.lang.Object) r9, (android.graphics.Rect) r13)
            r12 = r31
            goto L_0x04bb
        L_0x04af:
            r13 = r34
            r12 = r31
            goto L_0x04bb
        L_0x04b4:
            r13 = r34
            r12 = r31
            r14.setEpicenter((java.lang.Object) r9, (android.view.View) r12)
        L_0x04bb:
            r16 = r7
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r29)
            r2.put(r11, r7)
            boolean r7 = r8.isOverlapAllowed()
            r17 = r8
            r8 = 0
            if (r7 == 0) goto L_0x04d2
            java.lang.Object r3 = r14.mergeTransitionsTogether(r3, r9, r8)
            goto L_0x04d6
        L_0x04d2:
            java.lang.Object r5 = r14.mergeTransitionsTogether(r5, r9, r8)
        L_0x04d6:
            r30 = r4
            r15 = r10
            r31 = r12
            r34 = r13
            r35 = r14
            r7 = r21
            r10 = r26
            r4 = 0
            r13 = r43
            r14 = r2
            r2 = r25
            goto L_0x03a1
        L_0x04eb:
            r25 = r2
            r26 = r10
            r2 = r14
            r10 = r15
            r4 = r30
            r12 = r31
            r13 = r34
            r14 = r35
            r15 = r40
            java.lang.Object r3 = r14.mergeTransitionsInSequence(r3, r5, r0)
            java.util.Iterator r7 = r39.iterator()
        L_0x0503:
            boolean r8 = r7.hasNext()
            if (r8 == 0) goto L_0x059d
            java.lang.Object r8 = r7.next()
            androidx.fragment.app.DefaultSpecialEffectsController$TransitionInfo r8 = (androidx.fragment.app.DefaultSpecialEffectsController.TransitionInfo) r8
            boolean r9 = r8.isVisibilityUnchanged()
            if (r9 == 0) goto L_0x0516
            goto L_0x0503
        L_0x0516:
            java.lang.Object r9 = r8.getTransition()
            androidx.fragment.app.SpecialEffectsController$Operation r11 = r8.getOperation()
            if (r0 == 0) goto L_0x0530
            if (r11 == r10) goto L_0x0529
            r17 = r5
            r5 = r43
            if (r11 != r5) goto L_0x0534
            goto L_0x052d
        L_0x0529:
            r17 = r5
            r5 = r43
        L_0x052d:
            r16 = r29
            goto L_0x0536
        L_0x0530:
            r17 = r5
            r5 = r43
        L_0x0534:
            r16 = 0
        L_0x0536:
            if (r9 != 0) goto L_0x053e
            if (r16 == 0) goto L_0x053b
            goto L_0x053e
        L_0x053b:
            r18 = r7
            goto L_0x0597
        L_0x053e:
            android.view.ViewGroup r18 = r38.getContainer()
            boolean r18 = androidx.core.view.ViewCompat.isLaidOut(r18)
            if (r18 != 0) goto L_0x057f
            r18 = 2
            boolean r18 = androidx.fragment.app.FragmentManager.isLoggingEnabled(r18)
            if (r18 == 0) goto L_0x0579
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r18 = r7
            java.lang.String r7 = "SpecialEffectsController: Container "
            java.lang.StringBuilder r5 = r5.append(r7)
            android.view.ViewGroup r7 = r38.getContainer()
            java.lang.StringBuilder r5 = r5.append(r7)
            java.lang.String r7 = " has not been laid out. Completing operation "
            java.lang.StringBuilder r5 = r5.append(r7)
            java.lang.StringBuilder r5 = r5.append(r11)
            java.lang.String r5 = r5.toString()
            java.lang.String r7 = "FragmentManager"
            android.util.Log.v(r7, r5)
            goto L_0x057b
        L_0x0579:
            r18 = r7
        L_0x057b:
            r8.completeSpecialEffect()
            goto L_0x0597
        L_0x057f:
            r18 = r7
            androidx.fragment.app.SpecialEffectsController$Operation r5 = r8.getOperation()
            androidx.fragment.app.Fragment r5 = r5.getFragment()
            androidx.core.os.CancellationSignal r7 = r8.getSignal()
            r19 = r9
            androidx.fragment.app.DefaultSpecialEffectsController$9 r9 = new androidx.fragment.app.DefaultSpecialEffectsController$9
            r9.<init>(r8)
            r14.setListenerForTransitionEnd(r5, r3, r7, r9)
        L_0x0597:
            r5 = r17
            r7 = r18
            goto L_0x0503
        L_0x059d:
            r17 = r5
            android.view.ViewGroup r5 = r38.getContainer()
            boolean r5 = androidx.core.view.ViewCompat.isLaidOut(r5)
            if (r5 != 0) goto L_0x05aa
            return r2
        L_0x05aa:
            r5 = 4
            androidx.fragment.app.FragmentTransition.setViewVisibility(r1, r5)
            java.util.ArrayList r5 = r14.prepareSetNameOverridesReordered(r4)
            android.view.ViewGroup r7 = r38.getContainer()
            r14.beginDelayedTransition(r7, r3)
            android.view.ViewGroup r7 = r38.getContainer()
            r11 = r14
            r8 = r12
            r12 = r7
            r7 = r13
            r13 = r26
            r9 = r14
            r14 = r4
            r15 = r5
            r16 = r33
            r11.setNameOverridesReordered(r12, r13, r14, r15, r16)
            r11 = 0
            androidx.fragment.app.FragmentTransition.setViewVisibility(r1, r11)
            r11 = r26
            r9.swapSharedElementTargets(r0, r11, r4)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.DefaultSpecialEffectsController.startTransitions(java.util.List, java.util.List, boolean, androidx.fragment.app.SpecialEffectsController$Operation, androidx.fragment.app.SpecialEffectsController$Operation):java.util.Map");
    }

    /* access modifiers changed from: package-private */
    public void applyContainerChanges(SpecialEffectsController.Operation operation) {
        operation.getFinalState().applyState(operation.getFragment().mView);
    }

    /* access modifiers changed from: package-private */
    public void captureTransitioningViews(ArrayList<View> arrayList, View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (!ViewGroupCompat.isTransitionGroup(viewGroup)) {
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childAt = viewGroup.getChildAt(i);
                    if (childAt.getVisibility() == 0) {
                        captureTransitioningViews(arrayList, childAt);
                    }
                }
            } else if (!arrayList.contains(view)) {
                arrayList.add(viewGroup);
            }
        } else if (!arrayList.contains(view)) {
            arrayList.add(view);
        }
    }

    /* access modifiers changed from: package-private */
    public void executeOperations(List<SpecialEffectsController.Operation> list, boolean isPop) {
        SpecialEffectsController.Operation operation = null;
        SpecialEffectsController.Operation operation2 = null;
        for (SpecialEffectsController.Operation next : list) {
            SpecialEffectsController.Operation.State from = SpecialEffectsController.Operation.State.from(next.getFragment().mView);
            switch (AnonymousClass10.$SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State[next.getFinalState().ordinal()]) {
                case 1:
                case 2:
                case 3:
                    if (from == SpecialEffectsController.Operation.State.VISIBLE && operation == null) {
                        operation = next;
                        break;
                    }
                case 4:
                    if (from == SpecialEffectsController.Operation.State.VISIBLE) {
                        break;
                    } else {
                        operation2 = next;
                        break;
                    }
            }
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        final ArrayList<SpecialEffectsController.Operation> arrayList3 = new ArrayList<>(list);
        Iterator<SpecialEffectsController.Operation> it = list.iterator();
        while (true) {
            boolean z = true;
            if (it.hasNext()) {
                final SpecialEffectsController.Operation next2 = it.next();
                CancellationSignal cancellationSignal = new CancellationSignal();
                next2.markStartedSpecialEffect(cancellationSignal);
                arrayList.add(new AnimationInfo(next2, cancellationSignal, isPop));
                CancellationSignal cancellationSignal2 = new CancellationSignal();
                next2.markStartedSpecialEffect(cancellationSignal2);
                if (isPop) {
                    if (next2 == operation) {
                        arrayList2.add(new TransitionInfo(next2, cancellationSignal2, isPop, z));
                        next2.addCompletionListener(new Runnable() {
                            public void run() {
                                if (arrayList3.contains(next2)) {
                                    arrayList3.remove(next2);
                                    DefaultSpecialEffectsController.this.applyContainerChanges(next2);
                                }
                            }
                        });
                    }
                } else if (next2 == operation2) {
                    arrayList2.add(new TransitionInfo(next2, cancellationSignal2, isPop, z));
                    next2.addCompletionListener(new Runnable() {
                        public void run() {
                            if (arrayList3.contains(next2)) {
                                arrayList3.remove(next2);
                                DefaultSpecialEffectsController.this.applyContainerChanges(next2);
                            }
                        }
                    });
                }
                z = false;
                arrayList2.add(new TransitionInfo(next2, cancellationSignal2, isPop, z));
                next2.addCompletionListener(new Runnable() {
                    public void run() {
                        if (arrayList3.contains(next2)) {
                            arrayList3.remove(next2);
                            DefaultSpecialEffectsController.this.applyContainerChanges(next2);
                        }
                    }
                });
            } else {
                Map<SpecialEffectsController.Operation, Boolean> startTransitions = startTransitions(arrayList2, arrayList3, isPop, operation, operation2);
                startAnimations(arrayList, arrayList3, startTransitions.containsValue(true), startTransitions);
                for (SpecialEffectsController.Operation applyContainerChanges : arrayList3) {
                    applyContainerChanges(applyContainerChanges);
                }
                arrayList3.clear();
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void findNamedViews(Map<String, View> map, View view) {
        String transitionName = ViewCompat.getTransitionName(view);
        Log1F380D.a((Object) transitionName);
        if (transitionName != null) {
            map.put(transitionName, view);
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = viewGroup.getChildAt(i);
                if (childAt.getVisibility() == 0) {
                    findNamedViews(map, childAt);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void retainMatchingViews(ArrayMap<String, View> arrayMap, Collection<String> collection) {
        Iterator<Map.Entry<String, View>> it = arrayMap.entrySet().iterator();
        while (it.hasNext()) {
            String transitionName = ViewCompat.getTransitionName((View) it.next().getValue());
            Log1F380D.a((Object) transitionName);
            if (!collection.contains(transitionName)) {
                it.remove();
            }
        }
    }
}
