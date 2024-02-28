package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.collection.ArraySet;
import androidx.core.os.CancellationSignal;
import androidx.fragment.R;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentAnim;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentTransition;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import mt.Log1F380D;

/* compiled from: 0078 */
public abstract class FragmentManager implements FragmentResultOwner {
    private static boolean DEBUG = false;
    private static final String EXTRA_CREATED_FILLIN_INTENT = "androidx.fragment.extra.ACTIVITY_OPTIONS_BUNDLE";
    public static final int POP_BACK_STACK_INCLUSIVE = 1;
    static final String TAG = "FragmentManager";
    static boolean USE_STATE_MANAGER = true;
    ArrayList<BackStackRecord> mBackStack;
    private ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
    private final AtomicInteger mBackStackIndex = new AtomicInteger();
    private FragmentContainer mContainer;
    private ArrayList<Fragment> mCreatedMenus;
    int mCurState = -1;
    private SpecialEffectsControllerFactory mDefaultSpecialEffectsControllerFactory = new SpecialEffectsControllerFactory() {
        public SpecialEffectsController createController(ViewGroup container) {
            return new DefaultSpecialEffectsController(container);
        }
    };
    private boolean mDestroyed;
    private Runnable mExecCommit = new Runnable() {
        public void run() {
            FragmentManager.this.execPendingActions(true);
        }
    };
    private boolean mExecutingActions;
    private Map<Fragment, HashSet<CancellationSignal>> mExitAnimationCancellationSignals = Collections.synchronizedMap(new HashMap());
    private FragmentFactory mFragmentFactory = null;
    /* access modifiers changed from: private */
    public final FragmentStore mFragmentStore = new FragmentStore();
    private final FragmentTransition.Callback mFragmentTransitionCallback = new FragmentTransition.Callback() {
        public void onComplete(Fragment f, CancellationSignal signal) {
            if (!signal.isCanceled()) {
                FragmentManager.this.removeCancellationSignal(f, signal);
            }
        }

        public void onStart(Fragment fragment, CancellationSignal signal) {
            FragmentManager.this.addCancellationSignal(fragment, signal);
        }
    };
    private boolean mHavePendingDeferredStart;
    private FragmentHostCallback<?> mHost;
    private FragmentFactory mHostFragmentFactory = new FragmentFactory() {
        public Fragment instantiate(ClassLoader classLoader, String className) {
            return FragmentManager.this.getHost().instantiate(FragmentManager.this.getHost().getContext(), className, (Bundle) null);
        }
    };
    ArrayDeque<LaunchedFragmentInfo> mLaunchedFragments = new ArrayDeque<>();
    private final FragmentLayoutInflaterFactory mLayoutInflaterFactory = new FragmentLayoutInflaterFactory(this);
    private final FragmentLifecycleCallbacksDispatcher mLifecycleCallbacksDispatcher = new FragmentLifecycleCallbacksDispatcher(this);
    private boolean mNeedMenuInvalidate;
    private FragmentManagerViewModel mNonConfig;
    private final CopyOnWriteArrayList<FragmentOnAttachListener> mOnAttachListeners = new CopyOnWriteArrayList<>();
    private final OnBackPressedCallback mOnBackPressedCallback = new OnBackPressedCallback(false) {
        public void handleOnBackPressed() {
            FragmentManager.this.handleOnBackPressed();
        }
    };
    private OnBackPressedDispatcher mOnBackPressedDispatcher;
    private Fragment mParent;
    private final ArrayList<OpGenerator> mPendingActions = new ArrayList<>();
    private ArrayList<StartEnterTransitionListener> mPostponedTransactions;
    Fragment mPrimaryNav;
    private ActivityResultLauncher<String[]> mRequestPermissions;
    /* access modifiers changed from: private */
    public final Map<String, LifecycleAwareResultListener> mResultListeners = Collections.synchronizedMap(new HashMap());
    /* access modifiers changed from: private */
    public final Map<String, Bundle> mResults = Collections.synchronizedMap(new HashMap());
    private SpecialEffectsControllerFactory mSpecialEffectsControllerFactory = null;
    private ActivityResultLauncher<Intent> mStartActivityForResult;
    private ActivityResultLauncher<IntentSenderRequest> mStartIntentSenderForResult;
    private boolean mStateSaved;
    private boolean mStopped;
    private ArrayList<Fragment> mTmpAddedFragments;
    private ArrayList<Boolean> mTmpIsPop;
    private ArrayList<BackStackRecord> mTmpRecords;

    public interface BackStackEntry {
        @Deprecated
        CharSequence getBreadCrumbShortTitle();

        @Deprecated
        int getBreadCrumbShortTitleRes();

        @Deprecated
        CharSequence getBreadCrumbTitle();

        @Deprecated
        int getBreadCrumbTitleRes();

        int getId();

        String getName();
    }

    static class FragmentIntentSenderContract extends ActivityResultContract<IntentSenderRequest, ActivityResult> {
        FragmentIntentSenderContract() {
        }

        public Intent createIntent(Context context, IntentSenderRequest input) {
            Bundle bundleExtra;
            Intent intent = new Intent(ActivityResultContracts.StartIntentSenderForResult.ACTION_INTENT_SENDER_REQUEST);
            Intent fillInIntent = input.getFillInIntent();
            if (!(fillInIntent == null || (bundleExtra = fillInIntent.getBundleExtra(ActivityResultContracts.StartActivityForResult.EXTRA_ACTIVITY_OPTIONS_BUNDLE)) == null)) {
                intent.putExtra(ActivityResultContracts.StartActivityForResult.EXTRA_ACTIVITY_OPTIONS_BUNDLE, bundleExtra);
                fillInIntent.removeExtra(ActivityResultContracts.StartActivityForResult.EXTRA_ACTIVITY_OPTIONS_BUNDLE);
                if (fillInIntent.getBooleanExtra(FragmentManager.EXTRA_CREATED_FILLIN_INTENT, false)) {
                    input = new IntentSenderRequest.Builder(input.getIntentSender()).setFillInIntent((Intent) null).setFlags(input.getFlagsValues(), input.getFlagsMask()).build();
                }
            }
            intent.putExtra(ActivityResultContracts.StartIntentSenderForResult.EXTRA_INTENT_SENDER_REQUEST, input);
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v(FragmentManager.TAG, "CreateIntent created the following intent: " + intent);
            }
            return intent;
        }

        public ActivityResult parseResult(int resultCode, Intent intent) {
            return new ActivityResult(resultCode, intent);
        }
    }

    public static abstract class FragmentLifecycleCallbacks {
        @Deprecated
        public void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        }

        public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
        }

        public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        }

        public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
        }

        public void onFragmentDetached(FragmentManager fm, Fragment f) {
        }

        public void onFragmentPaused(FragmentManager fm, Fragment f) {
        }

        public void onFragmentPreAttached(FragmentManager fm, Fragment f, Context context) {
        }

        public void onFragmentPreCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        }

        public void onFragmentResumed(FragmentManager fm, Fragment f) {
        }

        public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
        }

        public void onFragmentStarted(FragmentManager fm, Fragment f) {
        }

        public void onFragmentStopped(FragmentManager fm, Fragment f) {
        }

        public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        }

        public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
        }
    }

    static class LaunchedFragmentInfo implements Parcelable {
        public static final Parcelable.Creator<LaunchedFragmentInfo> CREATOR = new Parcelable.Creator<LaunchedFragmentInfo>() {
            public LaunchedFragmentInfo createFromParcel(Parcel in) {
                return new LaunchedFragmentInfo(in);
            }

            public LaunchedFragmentInfo[] newArray(int size) {
                return new LaunchedFragmentInfo[size];
            }
        };
        int mRequestCode;
        String mWho;

        LaunchedFragmentInfo(Parcel in) {
            this.mWho = in.readString();
            this.mRequestCode = in.readInt();
        }

        LaunchedFragmentInfo(String who, int requestCode) {
            this.mWho = who;
            this.mRequestCode = requestCode;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mWho);
            dest.writeInt(this.mRequestCode);
        }
    }

    private static class LifecycleAwareResultListener implements FragmentResultListener {
        private final Lifecycle mLifecycle;
        private final FragmentResultListener mListener;
        private final LifecycleEventObserver mObserver;

        LifecycleAwareResultListener(Lifecycle lifecycle, FragmentResultListener listener, LifecycleEventObserver observer) {
            this.mLifecycle = lifecycle;
            this.mListener = listener;
            this.mObserver = observer;
        }

        public boolean isAtLeast(Lifecycle.State state) {
            return this.mLifecycle.getCurrentState().isAtLeast(state);
        }

        public void onFragmentResult(String requestKey, Bundle result) {
            this.mListener.onFragmentResult(requestKey, result);
        }

        public void removeObserver() {
            this.mLifecycle.removeObserver(this.mObserver);
        }
    }

    public interface OnBackStackChangedListener {
        void onBackStackChanged();
    }

    interface OpGenerator {
        boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2);
    }

    private class PopBackStackState implements OpGenerator {
        final int mFlags;
        final int mId;
        final String mName;

        PopBackStackState(String name, int id, int flags) {
            this.mName = name;
            this.mId = id;
            this.mFlags = flags;
        }

        public boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
            if (FragmentManager.this.mPrimaryNav != null && this.mId < 0 && this.mName == null && FragmentManager.this.mPrimaryNav.getChildFragmentManager().popBackStackImmediate()) {
                return false;
            }
            return FragmentManager.this.popBackStackState(arrayList, arrayList2, this.mName, this.mId, this.mFlags);
        }
    }

    static class StartEnterTransitionListener implements Fragment.OnStartEnterTransitionListener {
        final boolean mIsBack;
        private int mNumPostponed;
        final BackStackRecord mRecord;

        StartEnterTransitionListener(BackStackRecord record, boolean isBack) {
            this.mIsBack = isBack;
            this.mRecord = record;
        }

        /* access modifiers changed from: package-private */
        public void cancelTransaction() {
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
        }

        /* access modifiers changed from: package-private */
        public void completeTransaction() {
            boolean z = false;
            boolean z2 = this.mNumPostponed > 0;
            for (Fragment next : this.mRecord.mManager.getFragments()) {
                next.setOnStartEnterTransitionListener((Fragment.OnStartEnterTransitionListener) null);
                if (z2 && next.isPostponed()) {
                    next.startPostponedEnterTransition();
                }
            }
            FragmentManager fragmentManager = this.mRecord.mManager;
            BackStackRecord backStackRecord = this.mRecord;
            boolean z3 = this.mIsBack;
            if (!z2) {
                z = true;
            }
            fragmentManager.completeExecute(backStackRecord, z3, z, true);
        }

        public boolean isReady() {
            return this.mNumPostponed == 0;
        }

        public void onStartEnterTransition() {
            int i = this.mNumPostponed - 1;
            this.mNumPostponed = i;
            if (i == 0) {
                this.mRecord.mManager.scheduleCommit();
            }
        }

        public void startListening() {
            this.mNumPostponed++;
        }
    }

    private void addAddedFragments(ArraySet<Fragment> arraySet) {
        int i = this.mCurState;
        if (i >= 1) {
            int min = Math.min(i, 5);
            for (Fragment next : this.mFragmentStore.getFragments()) {
                if (next.mState < min) {
                    moveToState(next, min);
                    if (next.mView != null && !next.mHidden && next.mIsNewlyAdded) {
                        arraySet.add(next);
                    }
                }
            }
        }
    }

    private void cancelExitAnimation(Fragment f) {
        HashSet hashSet = this.mExitAnimationCancellationSignals.get(f);
        if (hashSet != null) {
            Iterator it = hashSet.iterator();
            while (it.hasNext()) {
                ((CancellationSignal) it.next()).cancel();
            }
            hashSet.clear();
            destroyFragmentView(f);
            this.mExitAnimationCancellationSignals.remove(f);
        }
    }

    private void checkStateLoss() {
        if (isStateSaved()) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        }
    }

    private void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }

    private Set<SpecialEffectsController> collectAllSpecialEffectsController() {
        HashSet hashSet = new HashSet();
        for (FragmentStateManager fragment : this.mFragmentStore.getActiveFragmentStateManagers()) {
            ViewGroup viewGroup = fragment.getFragment().mContainer;
            if (viewGroup != null) {
                hashSet.add(SpecialEffectsController.getOrCreateController(viewGroup, getSpecialEffectsControllerFactory()));
            }
        }
        return hashSet;
    }

    private Set<SpecialEffectsController> collectChangedControllers(ArrayList<BackStackRecord> arrayList, int startIndex, int endIndex) {
        ViewGroup viewGroup;
        HashSet hashSet = new HashSet();
        for (int i = startIndex; i < endIndex; i++) {
            Iterator it = arrayList.get(i).mOps.iterator();
            while (it.hasNext()) {
                Fragment fragment = ((FragmentTransaction.Op) it.next()).mFragment;
                if (!(fragment == null || (viewGroup = fragment.mContainer) == null)) {
                    hashSet.add(SpecialEffectsController.getOrCreateController(viewGroup, this));
                }
            }
        }
        return hashSet;
    }

    private void completeShowHideFragment(final Fragment fragment) {
        if (fragment.mView != null) {
            FragmentAnim.AnimationOrAnimator loadAnimation = FragmentAnim.loadAnimation(this.mHost.getContext(), fragment, !fragment.mHidden, fragment.getPopDirection());
            if (loadAnimation == null || loadAnimation.animator == null) {
                if (loadAnimation != null) {
                    fragment.mView.startAnimation(loadAnimation.animation);
                    loadAnimation.animation.start();
                }
                fragment.mView.setVisibility((!fragment.mHidden || fragment.isHideReplaced()) ? 0 : 8);
                if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                }
            } else {
                loadAnimation.animator.setTarget(fragment.mView);
                if (!fragment.mHidden) {
                    fragment.mView.setVisibility(0);
                } else if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                } else {
                    final ViewGroup viewGroup = fragment.mContainer;
                    final View view = fragment.mView;
                    viewGroup.startViewTransition(view);
                    loadAnimation.animator.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            viewGroup.endViewTransition(view);
                            animation.removeListener(this);
                            if (fragment.mView != null && fragment.mHidden) {
                                fragment.mView.setVisibility(8);
                            }
                        }
                    });
                }
                loadAnimation.animator.start();
            }
        }
        invalidateMenuForFragment(fragment);
        fragment.mHiddenChanged = false;
        fragment.onHiddenChanged(fragment.mHidden);
    }

    private void destroyFragmentView(Fragment fragment) {
        fragment.performDestroyView();
        this.mLifecycleCallbacksDispatcher.dispatchOnFragmentViewDestroyed(fragment, false);
        fragment.mContainer = null;
        fragment.mView = null;
        fragment.mViewLifecycleOwner = null;
        fragment.mViewLifecycleOwnerLiveData.setValue(null);
        fragment.mInLayout = false;
    }

    private void dispatchParentPrimaryNavigationFragmentChanged(Fragment f) {
        if (f != null && f.equals(findActiveFragment(f.mWho))) {
            f.performPrimaryNavigationFragmentChanged();
        }
    }

    /* JADX INFO: finally extract failed */
    private void dispatchStateChange(int nextState) {
        try {
            this.mExecutingActions = true;
            this.mFragmentStore.dispatchStateChange(nextState);
            moveToState(nextState, false);
            if (USE_STATE_MANAGER) {
                for (SpecialEffectsController forceCompleteAllOperations : collectAllSpecialEffectsController()) {
                    forceCompleteAllOperations.forceCompleteAllOperations();
                }
            }
            this.mExecutingActions = false;
            execPendingActions(true);
        } catch (Throwable th) {
            this.mExecutingActions = false;
            throw th;
        }
    }

    private void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            this.mHavePendingDeferredStart = false;
            startPendingDeferredFragments();
        }
    }

    @Deprecated
    public static void enableDebugLogging(boolean enabled) {
        DEBUG = enabled;
    }

    public static void enableNewStateManager(boolean enabled) {
        USE_STATE_MANAGER = enabled;
    }

    private void endAnimatingAwayFragments() {
        if (USE_STATE_MANAGER) {
            for (SpecialEffectsController forceCompleteAllOperations : collectAllSpecialEffectsController()) {
                forceCompleteAllOperations.forceCompleteAllOperations();
            }
        } else if (!this.mExitAnimationCancellationSignals.isEmpty()) {
            for (Fragment next : this.mExitAnimationCancellationSignals.keySet()) {
                cancelExitAnimation(next);
                moveToState(next);
            }
        }
    }

    private void ensureExecReady(boolean allowStateLoss) {
        if (this.mExecutingActions) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        } else if (this.mHost == null) {
            if (this.mDestroyed) {
                throw new IllegalStateException("FragmentManager has been destroyed");
            }
            throw new IllegalStateException("FragmentManager has not been attached to a host.");
        } else if (Looper.myLooper() == this.mHost.getHandler().getLooper()) {
            if (!allowStateLoss) {
                checkStateLoss();
            }
            if (this.mTmpRecords == null) {
                this.mTmpRecords = new ArrayList<>();
                this.mTmpIsPop = new ArrayList<>();
            }
            this.mExecutingActions = true;
            try {
                executePostponedTransaction((ArrayList<BackStackRecord>) null, (ArrayList<Boolean>) null);
            } finally {
                this.mExecutingActions = false;
            }
        } else {
            throw new IllegalStateException("Must be called from main thread of fragment host");
        }
    }

    private static void executeOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            BackStackRecord backStackRecord = arrayList.get(i);
            boolean z = true;
            if (arrayList2.get(i).booleanValue()) {
                backStackRecord.bumpBackStackNesting(-1);
                if (i != endIndex - 1) {
                    z = false;
                }
                backStackRecord.executePopOps(z);
            } else {
                backStackRecord.bumpBackStackNesting(1);
                backStackRecord.executeOps();
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v0, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v8, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v11, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v2, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v3, resolved type: int} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void executeOpsTogether(java.util.ArrayList<androidx.fragment.app.BackStackRecord> r20, java.util.ArrayList<java.lang.Boolean> r21, int r22, int r23) {
        /*
            r19 = this;
            r6 = r19
            r15 = r20
            r5 = r21
            r4 = r22
            r3 = r23
            java.lang.Object r0 = r15.get(r4)
            androidx.fragment.app.BackStackRecord r0 = (androidx.fragment.app.BackStackRecord) r0
            boolean r2 = r0.mReorderingAllowed
            r0 = 0
            java.util.ArrayList<androidx.fragment.app.Fragment> r1 = r6.mTmpAddedFragments
            if (r1 != 0) goto L_0x001f
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            r6.mTmpAddedFragments = r1
            goto L_0x0022
        L_0x001f:
            r1.clear()
        L_0x0022:
            java.util.ArrayList<androidx.fragment.app.Fragment> r1 = r6.mTmpAddedFragments
            androidx.fragment.app.FragmentStore r7 = r6.mFragmentStore
            java.util.List r7 = r7.getFragments()
            r1.addAll(r7)
            androidx.fragment.app.Fragment r1 = r19.getPrimaryNavigationFragment()
            r7 = r22
            r16 = r0
        L_0x0035:
            r0 = 1
            if (r7 >= r3) goto L_0x0064
            java.lang.Object r8 = r15.get(r7)
            androidx.fragment.app.BackStackRecord r8 = (androidx.fragment.app.BackStackRecord) r8
            java.lang.Object r9 = r5.get(r7)
            java.lang.Boolean r9 = (java.lang.Boolean) r9
            boolean r9 = r9.booleanValue()
            if (r9 != 0) goto L_0x0051
            java.util.ArrayList<androidx.fragment.app.Fragment> r10 = r6.mTmpAddedFragments
            androidx.fragment.app.Fragment r1 = r8.expandOps(r10, r1)
            goto L_0x0057
        L_0x0051:
            java.util.ArrayList<androidx.fragment.app.Fragment> r10 = r6.mTmpAddedFragments
            androidx.fragment.app.Fragment r1 = r8.trackAddedFragmentsInPop(r10, r1)
        L_0x0057:
            if (r16 != 0) goto L_0x005f
            boolean r10 = r8.mAddToBackStack
            if (r10 == 0) goto L_0x005e
            goto L_0x005f
        L_0x005e:
            r0 = 0
        L_0x005f:
            r16 = r0
            int r7 = r7 + 1
            goto L_0x0035
        L_0x0064:
            java.util.ArrayList<androidx.fragment.app.Fragment> r7 = r6.mTmpAddedFragments
            r7.clear()
            if (r2 != 0) goto L_0x00bc
            int r7 = r6.mCurState
            if (r7 < r0) goto L_0x00bc
            boolean r7 = USE_STATE_MANAGER
            if (r7 == 0) goto L_0x00a6
            r7 = r22
        L_0x0075:
            if (r7 >= r3) goto L_0x00a5
            java.lang.Object r8 = r15.get(r7)
            androidx.fragment.app.BackStackRecord r8 = (androidx.fragment.app.BackStackRecord) r8
            java.util.ArrayList r9 = r8.mOps
            java.util.Iterator r9 = r9.iterator()
        L_0x0083:
            boolean r10 = r9.hasNext()
            if (r10 == 0) goto L_0x00a2
            java.lang.Object r10 = r9.next()
            androidx.fragment.app.FragmentTransaction$Op r10 = (androidx.fragment.app.FragmentTransaction.Op) r10
            androidx.fragment.app.Fragment r11 = r10.mFragment
            if (r11 == 0) goto L_0x00a1
            androidx.fragment.app.FragmentManager r12 = r11.mFragmentManager
            if (r12 == 0) goto L_0x00a1
            androidx.fragment.app.FragmentStateManager r12 = r6.createOrGetFragmentStateManager(r11)
            androidx.fragment.app.FragmentStore r13 = r6.mFragmentStore
            r13.makeActive(r12)
        L_0x00a1:
            goto L_0x0083
        L_0x00a2:
            int r7 = r7 + 1
            goto L_0x0075
        L_0x00a5:
            goto L_0x00bc
        L_0x00a6:
            androidx.fragment.app.FragmentHostCallback<?> r7 = r6.mHost
            android.content.Context r7 = r7.getContext()
            androidx.fragment.app.FragmentContainer r8 = r6.mContainer
            r13 = 0
            androidx.fragment.app.FragmentTransition$Callback r14 = r6.mFragmentTransitionCallback
            r9 = r20
            r10 = r21
            r11 = r22
            r12 = r23
            androidx.fragment.app.FragmentTransition.startTransitions(r7, r8, r9, r10, r11, r12, r13, r14)
        L_0x00bc:
            executeOps(r20, r21, r22, r23)
            boolean r7 = USE_STATE_MANAGER
            if (r7 == 0) goto L_0x0149
            int r7 = r3 + -1
            java.lang.Object r7 = r5.get(r7)
            java.lang.Boolean r7 = (java.lang.Boolean) r7
            boolean r7 = r7.booleanValue()
            r8 = r22
        L_0x00d1:
            if (r8 >= r3) goto L_0x011e
            java.lang.Object r9 = r15.get(r8)
            androidx.fragment.app.BackStackRecord r9 = (androidx.fragment.app.BackStackRecord) r9
            if (r7 == 0) goto L_0x00fc
            java.util.ArrayList r10 = r9.mOps
            int r10 = r10.size()
            int r10 = r10 - r0
        L_0x00e2:
            if (r10 < 0) goto L_0x00fb
            java.util.ArrayList r11 = r9.mOps
            java.lang.Object r11 = r11.get(r10)
            androidx.fragment.app.FragmentTransaction$Op r11 = (androidx.fragment.app.FragmentTransaction.Op) r11
            androidx.fragment.app.Fragment r12 = r11.mFragment
            if (r12 == 0) goto L_0x00f8
            androidx.fragment.app.FragmentStateManager r13 = r6.createOrGetFragmentStateManager(r12)
            r13.moveToExpectedState()
        L_0x00f8:
            int r10 = r10 + -1
            goto L_0x00e2
        L_0x00fb:
            goto L_0x011b
        L_0x00fc:
            java.util.ArrayList r10 = r9.mOps
            java.util.Iterator r10 = r10.iterator()
        L_0x0102:
            boolean r11 = r10.hasNext()
            if (r11 == 0) goto L_0x011b
            java.lang.Object r11 = r10.next()
            androidx.fragment.app.FragmentTransaction$Op r11 = (androidx.fragment.app.FragmentTransaction.Op) r11
            androidx.fragment.app.Fragment r12 = r11.mFragment
            if (r12 == 0) goto L_0x011a
            androidx.fragment.app.FragmentStateManager r13 = r6.createOrGetFragmentStateManager(r12)
            r13.moveToExpectedState()
        L_0x011a:
            goto L_0x0102
        L_0x011b:
            int r8 = r8 + 1
            goto L_0x00d1
        L_0x011e:
            int r8 = r6.mCurState
            r6.moveToState((int) r8, (boolean) r0)
            java.util.Set r0 = r6.collectChangedControllers(r15, r4, r3)
            java.util.Iterator r8 = r0.iterator()
        L_0x012b:
            boolean r9 = r8.hasNext()
            if (r9 == 0) goto L_0x0141
            java.lang.Object r9 = r8.next()
            androidx.fragment.app.SpecialEffectsController r9 = (androidx.fragment.app.SpecialEffectsController) r9
            r9.updateOperationDirection(r7)
            r9.markPostponedState()
            r9.executePendingOperations()
            goto L_0x012b
        L_0x0141:
            r17 = r1
            r18 = r2
            r4 = r3
            r3 = r5
            goto L_0x01a8
        L_0x0149:
            r7 = r23
            if (r2 == 0) goto L_0x0171
            androidx.collection.ArraySet r8 = new androidx.collection.ArraySet
            r8.<init>()
            r6.addAddedFragments(r8)
            r14 = r0
            r0 = r19
            r17 = r1
            r1 = r20
            r18 = r2
            r2 = r21
            r13 = r3
            r3 = r22
            r12 = r4
            r4 = r23
            r11 = r5
            r5 = r8
            int r7 = r0.postponePostponableTransactions(r1, r2, r3, r4, r5)
            r6.makeRemovedFragmentsInvisible(r8)
            r0 = r7
            goto L_0x017a
        L_0x0171:
            r14 = r0
            r17 = r1
            r18 = r2
            r13 = r3
            r12 = r4
            r11 = r5
            r0 = r7
        L_0x017a:
            if (r0 == r12) goto L_0x01a6
            if (r18 == 0) goto L_0x01a6
            int r1 = r6.mCurState
            if (r1 < r14) goto L_0x019d
            androidx.fragment.app.FragmentHostCallback<?> r1 = r6.mHost
            android.content.Context r7 = r1.getContext()
            androidx.fragment.app.FragmentContainer r8 = r6.mContainer
            r1 = 1
            androidx.fragment.app.FragmentTransition$Callback r2 = r6.mFragmentTransitionCallback
            r9 = r20
            r10 = r21
            r3 = r11
            r11 = r22
            r12 = r0
            r4 = r13
            r13 = r1
            r1 = r14
            r14 = r2
            androidx.fragment.app.FragmentTransition.startTransitions(r7, r8, r9, r10, r11, r12, r13, r14)
            goto L_0x01a0
        L_0x019d:
            r3 = r11
            r4 = r13
            r1 = r14
        L_0x01a0:
            int r2 = r6.mCurState
            r6.moveToState((int) r2, (boolean) r1)
            goto L_0x01a8
        L_0x01a6:
            r3 = r11
            r4 = r13
        L_0x01a8:
            r0 = r22
        L_0x01aa:
            if (r0 >= r4) goto L_0x01cb
            java.lang.Object r1 = r15.get(r0)
            androidx.fragment.app.BackStackRecord r1 = (androidx.fragment.app.BackStackRecord) r1
            java.lang.Object r2 = r3.get(r0)
            java.lang.Boolean r2 = (java.lang.Boolean) r2
            boolean r2 = r2.booleanValue()
            if (r2 == 0) goto L_0x01c5
            int r5 = r1.mIndex
            if (r5 < 0) goto L_0x01c5
            r5 = -1
            r1.mIndex = r5
        L_0x01c5:
            r1.runOnCommitRunnables()
            int r0 = r0 + 1
            goto L_0x01aa
        L_0x01cb:
            if (r16 == 0) goto L_0x01d0
            r19.reportBackStackChanged()
        L_0x01d0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentManager.executeOpsTogether(java.util.ArrayList, java.util.ArrayList, int, int):void");
    }

    private void executePostponedTransaction(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        int indexOf;
        ArrayList<StartEnterTransitionListener> arrayList3 = this.mPostponedTransactions;
        int size = arrayList3 == null ? 0 : arrayList3.size();
        int i = 0;
        while (i < size) {
            StartEnterTransitionListener startEnterTransitionListener = this.mPostponedTransactions.get(i);
            if (arrayList != null && !startEnterTransitionListener.mIsBack && (indexOf = arrayList.indexOf(startEnterTransitionListener.mRecord)) != -1 && arrayList2 != null && arrayList2.get(indexOf).booleanValue()) {
                this.mPostponedTransactions.remove(i);
                i--;
                size--;
                startEnterTransitionListener.cancelTransaction();
            } else if (startEnterTransitionListener.isReady() || (arrayList != null && startEnterTransitionListener.mRecord.interactsWith(arrayList, 0, arrayList.size()))) {
                this.mPostponedTransactions.remove(i);
                i--;
                size--;
                if (arrayList != null && !startEnterTransitionListener.mIsBack) {
                    int indexOf2 = arrayList.indexOf(startEnterTransitionListener.mRecord);
                    int i2 = indexOf2;
                    if (!(indexOf2 == -1 || arrayList2 == null || !arrayList2.get(i2).booleanValue())) {
                        startEnterTransitionListener.cancelTransaction();
                    }
                }
                startEnterTransitionListener.completeTransaction();
            }
            i++;
        }
    }

    public static <F extends Fragment> F findFragment(View view) {
        F findViewFragment = findViewFragment(view);
        if (findViewFragment != null) {
            return findViewFragment;
        }
        throw new IllegalStateException("View " + view + " does not have a Fragment set");
    }

    static FragmentManager findFragmentManager(View view) {
        Fragment findViewFragment = findViewFragment(view);
        if (findViewFragment == null) {
            Context context = view.getContext();
            FragmentActivity fragmentActivity = null;
            while (true) {
                if (!(context instanceof ContextWrapper)) {
                    break;
                } else if (context instanceof FragmentActivity) {
                    fragmentActivity = (FragmentActivity) context;
                    break;
                } else {
                    context = ((ContextWrapper) context).getBaseContext();
                }
            }
            if (fragmentActivity != null) {
                return fragmentActivity.getSupportFragmentManager();
            }
            throw new IllegalStateException("View " + view + " is not within a subclass of FragmentActivity.");
        } else if (findViewFragment.isAdded()) {
            return findViewFragment.getChildFragmentManager();
        } else {
            throw new IllegalStateException("The Fragment " + findViewFragment + " that owns View " + view + " has already been destroyed. Nested fragments should always use the child FragmentManager.");
        }
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [android.view.ViewParent] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static androidx.fragment.app.Fragment findViewFragment(android.view.View r4) {
        /*
        L_0x0000:
            r0 = 0
            if (r4 == 0) goto L_0x0017
            androidx.fragment.app.Fragment r1 = getViewFragment(r4)
            if (r1 == 0) goto L_0x000a
            return r1
        L_0x000a:
            android.view.ViewParent r2 = r4.getParent()
            boolean r3 = r2 instanceof android.view.View
            if (r3 == 0) goto L_0x0015
            r0 = r2
            android.view.View r0 = (android.view.View) r0
        L_0x0015:
            r4 = r0
            goto L_0x0000
        L_0x0017:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentManager.findViewFragment(android.view.View):androidx.fragment.app.Fragment");
    }

    private void forcePostponedTransactions() {
        if (USE_STATE_MANAGER) {
            for (SpecialEffectsController forcePostponedExecutePendingOperations : collectAllSpecialEffectsController()) {
                forcePostponedExecutePendingOperations.forcePostponedExecutePendingOperations();
            }
        } else if (this.mPostponedTransactions != null) {
            while (!this.mPostponedTransactions.isEmpty()) {
                this.mPostponedTransactions.remove(0).completeTransaction();
            }
        }
    }

    private boolean generateOpsForPendingActions(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        boolean z = false;
        synchronized (this.mPendingActions) {
            if (this.mPendingActions.isEmpty()) {
                return false;
            }
            int size = this.mPendingActions.size();
            for (int i = 0; i < size; i++) {
                z |= this.mPendingActions.get(i).generateOps(arrayList, arrayList2);
            }
            this.mPendingActions.clear();
            this.mHost.getHandler().removeCallbacks(this.mExecCommit);
            return z;
        }
    }

    private FragmentManagerViewModel getChildNonConfig(Fragment f) {
        return this.mNonConfig.getChildNonConfig(f);
    }

    private ViewGroup getFragmentContainer(Fragment f) {
        if (f.mContainer != null) {
            return f.mContainer;
        }
        if (f.mContainerId > 0 && this.mContainer.onHasView()) {
            View onFindViewById = this.mContainer.onFindViewById(f.mContainerId);
            if (onFindViewById instanceof ViewGroup) {
                return (ViewGroup) onFindViewById;
            }
        }
        return null;
    }

    static Fragment getViewFragment(View view) {
        Object tag = view.getTag(R.id.fragment_container_view_tag);
        if (tag instanceof Fragment) {
            return (Fragment) tag;
        }
        return null;
    }

    static boolean isLoggingEnabled(int level) {
        return DEBUG || Log.isLoggable(TAG, level);
    }

    private boolean isMenuAvailable(Fragment f) {
        return (f.mHasMenu && f.mMenuVisible) || f.mChildFragmentManager.checkForMenus();
    }

    private void makeRemovedFragmentsInvisible(ArraySet<Fragment> arraySet) {
        int size = arraySet.size();
        for (int i = 0; i < size; i++) {
            Fragment valueAt = arraySet.valueAt(i);
            if (!valueAt.mAdded) {
                View requireView = valueAt.requireView();
                valueAt.mPostponedAlpha = requireView.getAlpha();
                requireView.setAlpha(0.0f);
            }
        }
    }

    private boolean popBackStackImmediate(String name, int id, int flags) {
        execPendingActions(false);
        ensureExecReady(true);
        Fragment fragment = this.mPrimaryNav;
        if (fragment != null && id < 0 && name == null && fragment.getChildFragmentManager().popBackStackImmediate()) {
            return true;
        }
        boolean popBackStackState = popBackStackState(this.mTmpRecords, this.mTmpIsPop, name, id, flags);
        if (popBackStackState) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            } finally {
                cleanupExec();
            }
        }
        updateOnBackPressedCallbackEnabled();
        doPendingDeferredStart();
        this.mFragmentStore.burpActive();
        return popBackStackState;
    }

    private int postponePostponableTransactions(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int startIndex, int endIndex, ArraySet<Fragment> arraySet) {
        int i = endIndex;
        for (int i2 = endIndex - 1; i2 >= startIndex; i2--) {
            BackStackRecord backStackRecord = arrayList.get(i2);
            boolean booleanValue = arrayList2.get(i2).booleanValue();
            if (backStackRecord.isPostponed() && !backStackRecord.interactsWith(arrayList, i2 + 1, endIndex)) {
                if (this.mPostponedTransactions == null) {
                    this.mPostponedTransactions = new ArrayList<>();
                }
                StartEnterTransitionListener startEnterTransitionListener = new StartEnterTransitionListener(backStackRecord, booleanValue);
                this.mPostponedTransactions.add(startEnterTransitionListener);
                backStackRecord.setOnStartPostponedListener(startEnterTransitionListener);
                if (booleanValue) {
                    backStackRecord.executeOps();
                } else {
                    backStackRecord.executePopOps(false);
                }
                i--;
                if (i2 != i) {
                    arrayList.remove(i2);
                    arrayList.add(i, backStackRecord);
                }
                addAddedFragments(arraySet);
            }
        }
        return i;
    }

    private void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        if (!arrayList.isEmpty()) {
            if (arrayList.size() == arrayList2.size()) {
                executePostponedTransaction(arrayList, arrayList2);
                int size = arrayList.size();
                int i = 0;
                int i2 = 0;
                while (i2 < size) {
                    if (!arrayList.get(i2).mReorderingAllowed) {
                        if (i != i2) {
                            executeOpsTogether(arrayList, arrayList2, i, i2);
                        }
                        int i3 = i2 + 1;
                        if (arrayList2.get(i2).booleanValue()) {
                            while (i3 < size && arrayList2.get(i3).booleanValue() && !arrayList.get(i3).mReorderingAllowed) {
                                i3++;
                            }
                        }
                        executeOpsTogether(arrayList, arrayList2, i2, i3);
                        i = i3;
                        i2 = i3 - 1;
                    }
                    i2++;
                }
                if (i != size) {
                    executeOpsTogether(arrayList, arrayList2, i, size);
                    return;
                }
                return;
            }
            throw new IllegalStateException("Internal error with the back stack records");
        }
    }

    private void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); i++) {
                this.mBackStackChangeListeners.get(i).onBackStackChanged();
            }
        }
    }

    static int reverseTransit(int transit) {
        switch (transit) {
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN:
                return 8194;
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE:
                return FragmentTransaction.TRANSIT_FRAGMENT_FADE;
            case 8194:
                return FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
            default:
                return 0;
        }
    }

    private void setVisibleRemovingFragment(Fragment f) {
        ViewGroup fragmentContainer = getFragmentContainer(f);
        if (fragmentContainer != null && f.getEnterAnim() + f.getExitAnim() + f.getPopEnterAnim() + f.getPopExitAnim() > 0) {
            if (fragmentContainer.getTag(R.id.visible_removing_fragment_view_tag) == null) {
                fragmentContainer.setTag(R.id.visible_removing_fragment_view_tag, f);
            }
            ((Fragment) fragmentContainer.getTag(R.id.visible_removing_fragment_view_tag)).setPopDirection(f.getPopDirection());
        }
    }

    private void startPendingDeferredFragments() {
        for (FragmentStateManager performPendingDeferredStart : this.mFragmentStore.getActiveFragmentStateManagers()) {
            performPendingDeferredStart(performPendingDeferredStart);
        }
    }

    private void throwException(RuntimeException ex) {
        Log.e(TAG, ex.getMessage());
        Log.e(TAG, "Activity state:");
        PrintWriter printWriter = new PrintWriter(new LogWriter(TAG));
        FragmentHostCallback<?> fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            try {
                fragmentHostCallback.onDump("  ", (FileDescriptor) null, printWriter, new String[0]);
            } catch (Exception e) {
                Log.e(TAG, "Failed dumping state", e);
            }
        } else {
            try {
                dump("  ", (FileDescriptor) null, printWriter, new String[0]);
            } catch (Exception e2) {
                Log.e(TAG, "Failed dumping state", e2);
            }
        }
        throw ex;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001a, code lost:
        if (getBackStackEntryCount() <= 0) goto L_0x0025;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0022, code lost:
        if (isPrimaryNavigation(r3.mParent) == false) goto L_0x0025;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0025, code lost:
        r2 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0026, code lost:
        r0.setEnabled(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0029, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0014, code lost:
        r0 = r3.mOnBackPressedCallback;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateOnBackPressedCallbackEnabled() {
        /*
            r3 = this;
            java.util.ArrayList<androidx.fragment.app.FragmentManager$OpGenerator> r0 = r3.mPendingActions
            monitor-enter(r0)
            java.util.ArrayList<androidx.fragment.app.FragmentManager$OpGenerator> r1 = r3.mPendingActions     // Catch:{ all -> 0x002a }
            boolean r1 = r1.isEmpty()     // Catch:{ all -> 0x002a }
            r2 = 1
            if (r1 != 0) goto L_0x0013
            androidx.activity.OnBackPressedCallback r1 = r3.mOnBackPressedCallback     // Catch:{ all -> 0x002a }
            r1.setEnabled(r2)     // Catch:{ all -> 0x002a }
            monitor-exit(r0)     // Catch:{ all -> 0x002a }
            return
        L_0x0013:
            monitor-exit(r0)     // Catch:{ all -> 0x002a }
            androidx.activity.OnBackPressedCallback r0 = r3.mOnBackPressedCallback
            int r1 = r3.getBackStackEntryCount()
            if (r1 <= 0) goto L_0x0025
            androidx.fragment.app.Fragment r1 = r3.mParent
            boolean r1 = r3.isPrimaryNavigation(r1)
            if (r1 == 0) goto L_0x0025
            goto L_0x0026
        L_0x0025:
            r2 = 0
        L_0x0026:
            r0.setEnabled(r2)
            return
        L_0x002a:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002a }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentManager.updateOnBackPressedCallbackEnabled():void");
    }

    /* access modifiers changed from: package-private */
    public void addBackStackState(BackStackRecord state) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList<>();
        }
        this.mBackStack.add(state);
    }

    /* access modifiers changed from: package-private */
    public void addCancellationSignal(Fragment f, CancellationSignal signal) {
        if (this.mExitAnimationCancellationSignals.get(f) == null) {
            this.mExitAnimationCancellationSignals.put(f, new HashSet());
        }
        this.mExitAnimationCancellationSignals.get(f).add(signal);
    }

    /* access modifiers changed from: package-private */
    public FragmentStateManager addFragment(Fragment fragment) {
        if (isLoggingEnabled(2)) {
            Log.v(TAG, "add: " + fragment);
        }
        FragmentStateManager createOrGetFragmentStateManager = createOrGetFragmentStateManager(fragment);
        fragment.mFragmentManager = this;
        this.mFragmentStore.makeActive(createOrGetFragmentStateManager);
        if (!fragment.mDetached) {
            this.mFragmentStore.addFragment(fragment);
            fragment.mRemoving = false;
            if (fragment.mView == null) {
                fragment.mHiddenChanged = false;
            }
            if (isMenuAvailable(fragment)) {
                this.mNeedMenuInvalidate = true;
            }
        }
        return createOrGetFragmentStateManager;
    }

    public void addFragmentOnAttachListener(FragmentOnAttachListener listener) {
        this.mOnAttachListeners.add(listener);
    }

    public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList<>();
        }
        this.mBackStackChangeListeners.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void addRetainedFragment(Fragment f) {
        this.mNonConfig.addRetainedFragment(f);
    }

    /* access modifiers changed from: package-private */
    public int allocBackStackIndex() {
        return this.mBackStackIndex.getAndIncrement();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v17, resolved type: androidx.activity.OnBackPressedDispatcherOwner} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v6, resolved type: androidx.fragment.app.Fragment} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v7, resolved type: androidx.fragment.app.Fragment} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v8, resolved type: androidx.fragment.app.Fragment} */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void attachController(androidx.fragment.app.FragmentHostCallback<?> r7, androidx.fragment.app.FragmentContainer r8, final androidx.fragment.app.Fragment r9) {
        /*
            r6 = this;
            androidx.fragment.app.FragmentHostCallback<?> r0 = r6.mHost
            if (r0 != 0) goto L_0x0115
            r6.mHost = r7
            r6.mContainer = r8
            r6.mParent = r9
            if (r9 == 0) goto L_0x0015
            androidx.fragment.app.FragmentManager$8 r0 = new androidx.fragment.app.FragmentManager$8
            r0.<init>(r9)
            r6.addFragmentOnAttachListener(r0)
            goto L_0x001f
        L_0x0015:
            boolean r0 = r7 instanceof androidx.fragment.app.FragmentOnAttachListener
            if (r0 == 0) goto L_0x001f
            r0 = r7
            androidx.fragment.app.FragmentOnAttachListener r0 = (androidx.fragment.app.FragmentOnAttachListener) r0
            r6.addFragmentOnAttachListener(r0)
        L_0x001f:
            androidx.fragment.app.Fragment r0 = r6.mParent
            if (r0 == 0) goto L_0x0026
            r6.updateOnBackPressedCallbackEnabled()
        L_0x0026:
            boolean r0 = r7 instanceof androidx.activity.OnBackPressedDispatcherOwner
            if (r0 == 0) goto L_0x003d
            r0 = r7
            androidx.activity.OnBackPressedDispatcherOwner r0 = (androidx.activity.OnBackPressedDispatcherOwner) r0
            androidx.activity.OnBackPressedDispatcher r1 = r0.getOnBackPressedDispatcher()
            r6.mOnBackPressedDispatcher = r1
            if (r9 == 0) goto L_0x0037
            r2 = r9
            goto L_0x0038
        L_0x0037:
            r2 = r0
        L_0x0038:
            androidx.activity.OnBackPressedCallback r3 = r6.mOnBackPressedCallback
            r1.addCallback(r2, r3)
        L_0x003d:
            if (r9 == 0) goto L_0x0048
            androidx.fragment.app.FragmentManager r0 = r9.mFragmentManager
            androidx.fragment.app.FragmentManagerViewModel r0 = r0.getChildNonConfig(r9)
            r6.mNonConfig = r0
            goto L_0x0062
        L_0x0048:
            boolean r0 = r7 instanceof androidx.lifecycle.ViewModelStoreOwner
            if (r0 == 0) goto L_0x005a
            r0 = r7
            androidx.lifecycle.ViewModelStoreOwner r0 = (androidx.lifecycle.ViewModelStoreOwner) r0
            androidx.lifecycle.ViewModelStore r0 = r0.getViewModelStore()
            androidx.fragment.app.FragmentManagerViewModel r1 = androidx.fragment.app.FragmentManagerViewModel.getInstance(r0)
            r6.mNonConfig = r1
            goto L_0x0062
        L_0x005a:
            androidx.fragment.app.FragmentManagerViewModel r0 = new androidx.fragment.app.FragmentManagerViewModel
            r1 = 0
            r0.<init>(r1)
            r6.mNonConfig = r0
        L_0x0062:
            androidx.fragment.app.FragmentManagerViewModel r0 = r6.mNonConfig
            boolean r1 = r6.isStateSaved()
            r0.setIsStateSaved(r1)
            androidx.fragment.app.FragmentStore r0 = r6.mFragmentStore
            androidx.fragment.app.FragmentManagerViewModel r1 = r6.mNonConfig
            r0.setNonConfig(r1)
            androidx.fragment.app.FragmentHostCallback<?> r0 = r6.mHost
            boolean r1 = r0 instanceof androidx.activity.result.ActivityResultRegistryOwner
            if (r1 == 0) goto L_0x0114
            androidx.activity.result.ActivityResultRegistryOwner r0 = (androidx.activity.result.ActivityResultRegistryOwner) r0
            androidx.activity.result.ActivityResultRegistry r0 = r0.getActivityResultRegistry()
            if (r9 == 0) goto L_0x0096
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = r9.mWho
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = ":"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            goto L_0x0098
        L_0x0096:
            java.lang.String r1 = ""
        L_0x0098:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "FragmentManager:"
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r2 = r2.append(r1)
            java.lang.String r2 = r2.toString()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.StringBuilder r3 = r3.append(r2)
            java.lang.String r4 = "StartActivityForResult"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            androidx.activity.result.contract.ActivityResultContracts$StartActivityForResult r4 = new androidx.activity.result.contract.ActivityResultContracts$StartActivityForResult
            r4.<init>()
            androidx.fragment.app.FragmentManager$9 r5 = new androidx.fragment.app.FragmentManager$9
            r5.<init>()
            androidx.activity.result.ActivityResultLauncher r3 = r0.register(r3, r4, r5)
            r6.mStartActivityForResult = r3
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.StringBuilder r3 = r3.append(r2)
            java.lang.String r4 = "StartIntentSenderForResult"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            androidx.fragment.app.FragmentManager$FragmentIntentSenderContract r4 = new androidx.fragment.app.FragmentManager$FragmentIntentSenderContract
            r4.<init>()
            androidx.fragment.app.FragmentManager$10 r5 = new androidx.fragment.app.FragmentManager$10
            r5.<init>()
            androidx.activity.result.ActivityResultLauncher r3 = r0.register(r3, r4, r5)
            r6.mStartIntentSenderForResult = r3
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.StringBuilder r3 = r3.append(r2)
            java.lang.String r4 = "RequestPermissions"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            androidx.activity.result.contract.ActivityResultContracts$RequestMultiplePermissions r4 = new androidx.activity.result.contract.ActivityResultContracts$RequestMultiplePermissions
            r4.<init>()
            androidx.fragment.app.FragmentManager$11 r5 = new androidx.fragment.app.FragmentManager$11
            r5.<init>()
            androidx.activity.result.ActivityResultLauncher r3 = r0.register(r3, r4, r5)
            r6.mRequestPermissions = r3
        L_0x0114:
            return
        L_0x0115:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "Already attached"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentManager.attachController(androidx.fragment.app.FragmentHostCallback, androidx.fragment.app.FragmentContainer, androidx.fragment.app.Fragment):void");
    }

    /* access modifiers changed from: package-private */
    public void attachFragment(Fragment fragment) {
        if (isLoggingEnabled(2)) {
            Log.v(TAG, "attach: " + fragment);
        }
        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (!fragment.mAdded) {
                this.mFragmentStore.addFragment(fragment);
                if (isLoggingEnabled(2)) {
                    Log.v(TAG, "add from attach: " + fragment);
                }
                if (isMenuAvailable(fragment)) {
                    this.mNeedMenuInvalidate = true;
                }
            }
        }
    }

    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    /* access modifiers changed from: package-private */
    public boolean checkForMenus() {
        boolean z = false;
        for (Fragment next : this.mFragmentStore.getActiveFragments()) {
            if (next != null) {
                z = isMenuAvailable(next);
                continue;
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    public final void clearFragmentResult(String requestKey) {
        this.mResults.remove(requestKey);
    }

    public final void clearFragmentResultListener(String requestKey) {
        LifecycleAwareResultListener remove = this.mResultListeners.remove(requestKey);
        if (remove != null) {
            remove.removeObserver();
        }
    }

    /* access modifiers changed from: package-private */
    public void completeExecute(BackStackRecord record, boolean isPop, boolean runTransitions, boolean moveToState) {
        if (isPop) {
            record.executePopOps(moveToState);
        } else {
            record.executeOps();
        }
        ArrayList arrayList = new ArrayList(1);
        ArrayList arrayList2 = new ArrayList(1);
        arrayList.add(record);
        arrayList2.add(Boolean.valueOf(isPop));
        if (runTransitions && this.mCurState >= 1) {
            FragmentTransition.startTransitions(this.mHost.getContext(), this.mContainer, arrayList, arrayList2, 0, 1, true, this.mFragmentTransitionCallback);
        }
        if (moveToState) {
            moveToState(this.mCurState, true);
        }
        for (Fragment next : this.mFragmentStore.getActiveFragments()) {
            if (next != null && next.mView != null && next.mIsNewlyAdded && record.interactsWith(next.mContainerId)) {
                if (next.mPostponedAlpha > 0.0f) {
                    next.mView.setAlpha(next.mPostponedAlpha);
                }
                if (moveToState) {
                    next.mPostponedAlpha = 0.0f;
                } else {
                    next.mPostponedAlpha = -1.0f;
                    next.mIsNewlyAdded = false;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public FragmentStateManager createOrGetFragmentStateManager(Fragment f) {
        FragmentStateManager fragmentStateManager = this.mFragmentStore.getFragmentStateManager(f.mWho);
        if (fragmentStateManager != null) {
            return fragmentStateManager;
        }
        FragmentStateManager fragmentStateManager2 = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, f);
        fragmentStateManager2.restoreState(this.mHost.getContext().getClassLoader());
        fragmentStateManager2.setFragmentManagerState(this.mCurState);
        return fragmentStateManager2;
    }

    /* access modifiers changed from: package-private */
    public void detachFragment(Fragment fragment) {
        if (isLoggingEnabled(2)) {
            Log.v(TAG, "detach: " + fragment);
        }
        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                if (isLoggingEnabled(2)) {
                    Log.v(TAG, "remove from detach: " + fragment);
                }
                this.mFragmentStore.removeFragment(fragment);
                if (isMenuAvailable(fragment)) {
                    this.mNeedMenuInvalidate = true;
                }
                setVisibleRemovingFragment(fragment);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchActivityCreated() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mNonConfig.setIsStateSaved(false);
        dispatchStateChange(4);
    }

    /* access modifiers changed from: package-private */
    public void dispatchAttach() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mNonConfig.setIsStateSaved(false);
        dispatchStateChange(0);
    }

    /* access modifiers changed from: package-private */
    public void dispatchConfigurationChanged(Configuration newConfig) {
        for (Fragment next : this.mFragmentStore.getFragments()) {
            if (next != null) {
                next.performConfigurationChanged(newConfig);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean dispatchContextItemSelected(MenuItem item) {
        if (this.mCurState < 1) {
            return false;
        }
        for (Fragment next : this.mFragmentStore.getFragments()) {
            if (next != null && next.performContextItemSelected(item)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void dispatchCreate() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mNonConfig.setIsStateSaved(false);
        dispatchStateChange(1);
    }

    /* access modifiers changed from: package-private */
    public boolean dispatchCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (this.mCurState < 1) {
            return false;
        }
        boolean z = false;
        ArrayList<Fragment> arrayList = null;
        for (Fragment next : this.mFragmentStore.getFragments()) {
            if (next != null && isParentMenuVisible(next) && next.performCreateOptionsMenu(menu, inflater)) {
                z = true;
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                }
                arrayList.add(next);
            }
        }
        if (this.mCreatedMenus != null) {
            for (int i = 0; i < this.mCreatedMenus.size(); i++) {
                Fragment fragment = this.mCreatedMenus.get(i);
                if (arrayList == null || !arrayList.contains(fragment)) {
                    fragment.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = arrayList;
        return z;
    }

    /* access modifiers changed from: package-private */
    public void dispatchDestroy() {
        this.mDestroyed = true;
        execPendingActions(true);
        endAnimatingAwayFragments();
        dispatchStateChange(-1);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
        if (this.mOnBackPressedDispatcher != null) {
            this.mOnBackPressedCallback.remove();
            this.mOnBackPressedDispatcher = null;
        }
        ActivityResultLauncher<Intent> activityResultLauncher = this.mStartActivityForResult;
        if (activityResultLauncher != null) {
            activityResultLauncher.unregister();
            this.mStartIntentSenderForResult.unregister();
            this.mRequestPermissions.unregister();
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchDestroyView() {
        dispatchStateChange(1);
    }

    /* access modifiers changed from: package-private */
    public void dispatchLowMemory() {
        for (Fragment next : this.mFragmentStore.getFragments()) {
            if (next != null) {
                next.performLowMemory();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchMultiWindowModeChanged(boolean isInMultiWindowMode) {
        for (Fragment next : this.mFragmentStore.getFragments()) {
            if (next != null) {
                next.performMultiWindowModeChanged(isInMultiWindowMode);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnAttachFragment(Fragment fragment) {
        Iterator<FragmentOnAttachListener> it = this.mOnAttachListeners.iterator();
        while (it.hasNext()) {
            it.next().onAttachFragment(this, fragment);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean dispatchOptionsItemSelected(MenuItem item) {
        if (this.mCurState < 1) {
            return false;
        }
        for (Fragment next : this.mFragmentStore.getFragments()) {
            if (next != null && next.performOptionsItemSelected(item)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void dispatchOptionsMenuClosed(Menu menu) {
        if (this.mCurState >= 1) {
            for (Fragment next : this.mFragmentStore.getFragments()) {
                if (next != null) {
                    next.performOptionsMenuClosed(menu);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchPause() {
        dispatchStateChange(5);
    }

    /* access modifiers changed from: package-private */
    public void dispatchPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        for (Fragment next : this.mFragmentStore.getFragments()) {
            if (next != null) {
                next.performPictureInPictureModeChanged(isInPictureInPictureMode);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean dispatchPrepareOptionsMenu(Menu menu) {
        if (this.mCurState < 1) {
            return false;
        }
        boolean z = false;
        for (Fragment next : this.mFragmentStore.getFragments()) {
            if (next != null && isParentMenuVisible(next) && next.performPrepareOptionsMenu(menu)) {
                z = true;
            }
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public void dispatchPrimaryNavigationFragmentChanged() {
        updateOnBackPressedCallbackEnabled();
        dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
    }

    /* access modifiers changed from: package-private */
    public void dispatchResume() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mNonConfig.setIsStateSaved(false);
        dispatchStateChange(7);
    }

    /* access modifiers changed from: package-private */
    public void dispatchStart() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mNonConfig.setIsStateSaved(false);
        dispatchStateChange(5);
    }

    /* access modifiers changed from: package-private */
    public void dispatchStop() {
        this.mStopped = true;
        this.mNonConfig.setIsStateSaved(true);
        dispatchStateChange(4);
    }

    /* access modifiers changed from: package-private */
    public void dispatchViewCreated() {
        dispatchStateChange(2);
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        int size;
        int size2;
        String str = prefix + "    ";
        this.mFragmentStore.dump(prefix, fd, writer, args);
        ArrayList<Fragment> arrayList = this.mCreatedMenus;
        if (arrayList != null && (size2 = arrayList.size()) > 0) {
            writer.print(prefix);
            writer.println("Fragments Created Menus:");
            for (int i = 0; i < size2; i++) {
                writer.print(prefix);
                writer.print("  #");
                writer.print(i);
                writer.print(": ");
                writer.println(this.mCreatedMenus.get(i).toString());
            }
        }
        ArrayList<BackStackRecord> arrayList2 = this.mBackStack;
        if (arrayList2 != null && (size = arrayList2.size()) > 0) {
            writer.print(prefix);
            writer.println("Back Stack:");
            for (int i2 = 0; i2 < size; i2++) {
                BackStackRecord backStackRecord = this.mBackStack.get(i2);
                writer.print(prefix);
                writer.print("  #");
                writer.print(i2);
                writer.print(": ");
                writer.println(backStackRecord.toString());
                backStackRecord.dump(str, writer);
            }
        }
        writer.print(prefix);
        writer.println("Back Stack Index: " + this.mBackStackIndex.get());
        synchronized (this.mPendingActions) {
            int size3 = this.mPendingActions.size();
            if (size3 > 0) {
                writer.print(prefix);
                writer.println("Pending Actions:");
                for (int i3 = 0; i3 < size3; i3++) {
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i3);
                    writer.print(": ");
                    writer.println(this.mPendingActions.get(i3));
                }
            }
        }
        writer.print(prefix);
        writer.println("FragmentManager misc state:");
        writer.print(prefix);
        writer.print("  mHost=");
        writer.println(this.mHost);
        writer.print(prefix);
        writer.print("  mContainer=");
        writer.println(this.mContainer);
        if (this.mParent != null) {
            writer.print(prefix);
            writer.print("  mParent=");
            writer.println(this.mParent);
        }
        writer.print(prefix);
        writer.print("  mCurState=");
        writer.print(this.mCurState);
        writer.print(" mStateSaved=");
        writer.print(this.mStateSaved);
        writer.print(" mStopped=");
        writer.print(this.mStopped);
        writer.print(" mDestroyed=");
        writer.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            writer.print(prefix);
            writer.print("  mNeedMenuInvalidate=");
            writer.println(this.mNeedMenuInvalidate);
        }
    }

    /* access modifiers changed from: package-private */
    public void enqueueAction(OpGenerator action, boolean allowStateLoss) {
        if (!allowStateLoss) {
            if (this.mHost != null) {
                checkStateLoss();
            } else if (this.mDestroyed) {
                throw new IllegalStateException("FragmentManager has been destroyed");
            } else {
                throw new IllegalStateException("FragmentManager has not been attached to a host.");
            }
        }
        synchronized (this.mPendingActions) {
            if (this.mHost != null) {
                this.mPendingActions.add(action);
                scheduleCommit();
            } else if (!allowStateLoss) {
                throw new IllegalStateException("Activity has been destroyed");
            }
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: package-private */
    public boolean execPendingActions(boolean allowStateLoss) {
        ensureExecReady(allowStateLoss);
        boolean z = false;
        while (generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                cleanupExec();
                z = true;
            } catch (Throwable th) {
                cleanupExec();
                throw th;
            }
        }
        updateOnBackPressedCallbackEnabled();
        doPendingDeferredStart();
        this.mFragmentStore.burpActive();
        return z;
    }

    /* access modifiers changed from: package-private */
    public void execSingleAction(OpGenerator action, boolean allowStateLoss) {
        if (!allowStateLoss || (this.mHost != null && !this.mDestroyed)) {
            ensureExecReady(allowStateLoss);
            if (action.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
                this.mExecutingActions = true;
                try {
                    removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                } finally {
                    cleanupExec();
                }
            }
            updateOnBackPressedCallbackEnabled();
            doPendingDeferredStart();
            this.mFragmentStore.burpActive();
        }
    }

    public boolean executePendingTransactions() {
        boolean execPendingActions = execPendingActions(true);
        forcePostponedTransactions();
        return execPendingActions;
    }

    /* access modifiers changed from: package-private */
    public Fragment findActiveFragment(String who) {
        return this.mFragmentStore.findActiveFragment(who);
    }

    public Fragment findFragmentById(int id) {
        return this.mFragmentStore.findFragmentById(id);
    }

    public Fragment findFragmentByTag(String tag) {
        return this.mFragmentStore.findFragmentByTag(tag);
    }

    /* access modifiers changed from: package-private */
    public Fragment findFragmentByWho(String who) {
        return this.mFragmentStore.findFragmentByWho(who);
    }

    /* access modifiers changed from: package-private */
    public int getActiveFragmentCount() {
        return this.mFragmentStore.getActiveFragmentCount();
    }

    /* access modifiers changed from: package-private */
    public List<Fragment> getActiveFragments() {
        return this.mFragmentStore.getActiveFragments();
    }

    public BackStackEntry getBackStackEntryAt(int index) {
        return this.mBackStack.get(index);
    }

    public int getBackStackEntryCount() {
        ArrayList<BackStackRecord> arrayList = this.mBackStack;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    public FragmentContainer getContainer() {
        return this.mContainer;
    }

    public Fragment getFragment(Bundle bundle, String key) {
        String string = bundle.getString(key);
        if (string == null) {
            return null;
        }
        Fragment findActiveFragment = findActiveFragment(string);
        if (findActiveFragment == null) {
            throwException(new IllegalStateException("Fragment no longer exists for key " + key + ": unique id " + string));
        }
        return findActiveFragment;
    }

    public FragmentFactory getFragmentFactory() {
        FragmentFactory fragmentFactory = this.mFragmentFactory;
        if (fragmentFactory != null) {
            return fragmentFactory;
        }
        Fragment fragment = this.mParent;
        return fragment != null ? fragment.mFragmentManager.getFragmentFactory() : this.mHostFragmentFactory;
    }

    /* access modifiers changed from: package-private */
    public FragmentStore getFragmentStore() {
        return this.mFragmentStore;
    }

    public List<Fragment> getFragments() {
        return this.mFragmentStore.getFragments();
    }

    /* access modifiers changed from: package-private */
    public FragmentHostCallback<?> getHost() {
        return this.mHost;
    }

    /* access modifiers changed from: package-private */
    public LayoutInflater.Factory2 getLayoutInflaterFactory() {
        return this.mLayoutInflaterFactory;
    }

    /* access modifiers changed from: package-private */
    public FragmentLifecycleCallbacksDispatcher getLifecycleCallbacksDispatcher() {
        return this.mLifecycleCallbacksDispatcher;
    }

    /* access modifiers changed from: package-private */
    public Fragment getParent() {
        return this.mParent;
    }

    public Fragment getPrimaryNavigationFragment() {
        return this.mPrimaryNav;
    }

    /* access modifiers changed from: package-private */
    public SpecialEffectsControllerFactory getSpecialEffectsControllerFactory() {
        SpecialEffectsControllerFactory specialEffectsControllerFactory = this.mSpecialEffectsControllerFactory;
        if (specialEffectsControllerFactory != null) {
            return specialEffectsControllerFactory;
        }
        Fragment fragment = this.mParent;
        return fragment != null ? fragment.mFragmentManager.getSpecialEffectsControllerFactory() : this.mDefaultSpecialEffectsControllerFactory;
    }

    /* access modifiers changed from: package-private */
    public ViewModelStore getViewModelStore(Fragment f) {
        return this.mNonConfig.getViewModelStore(f);
    }

    /* access modifiers changed from: package-private */
    public void handleOnBackPressed() {
        execPendingActions(true);
        if (this.mOnBackPressedCallback.isEnabled()) {
            popBackStackImmediate();
        } else {
            this.mOnBackPressedDispatcher.onBackPressed();
        }
    }

    /* access modifiers changed from: package-private */
    public void hideFragment(Fragment fragment) {
        if (isLoggingEnabled(2)) {
            Log.v(TAG, "hide: " + fragment);
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            fragment.mHiddenChanged = true ^ fragment.mHiddenChanged;
            setVisibleRemovingFragment(fragment);
        }
    }

    /* access modifiers changed from: package-private */
    public void invalidateMenuForFragment(Fragment f) {
        if (f.mAdded && isMenuAvailable(f)) {
            this.mNeedMenuInvalidate = true;
        }
    }

    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    /* access modifiers changed from: package-private */
    public boolean isParentMenuVisible(Fragment parent) {
        if (parent == null) {
            return true;
        }
        return parent.isMenuVisible();
    }

    /* access modifiers changed from: package-private */
    public boolean isPrimaryNavigation(Fragment parent) {
        if (parent == null) {
            return true;
        }
        FragmentManager fragmentManager = parent.mFragmentManager;
        return parent.equals(fragmentManager.getPrimaryNavigationFragment()) && isPrimaryNavigation(fragmentManager.mParent);
    }

    /* access modifiers changed from: package-private */
    public boolean isStateAtLeast(int state) {
        return this.mCurState >= state;
    }

    public boolean isStateSaved() {
        return this.mStateSaved || this.mStopped;
    }

    /* access modifiers changed from: package-private */
    public void launchRequestPermissions(Fragment f, String[] permissions, int requestCode) {
        if (this.mRequestPermissions != null) {
            this.mLaunchedFragments.addLast(new LaunchedFragmentInfo(f.mWho, requestCode));
            this.mRequestPermissions.launch(permissions);
            return;
        }
        this.mHost.onRequestPermissionsFromFragment(f, permissions, requestCode);
    }

    /* access modifiers changed from: package-private */
    public void launchStartActivityForResult(Fragment f, Intent intent, int requestCode, Bundle options) {
        if (this.mStartActivityForResult != null) {
            this.mLaunchedFragments.addLast(new LaunchedFragmentInfo(f.mWho, requestCode));
            if (!(intent == null || options == null)) {
                intent.putExtra(ActivityResultContracts.StartActivityForResult.EXTRA_ACTIVITY_OPTIONS_BUNDLE, options);
            }
            this.mStartActivityForResult.launch(intent);
            return;
        }
        this.mHost.onStartActivityFromFragment(f, intent, requestCode, options);
    }

    /* access modifiers changed from: package-private */
    public void launchStartIntentSenderForResult(Fragment f, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        Intent intent2;
        Fragment fragment = f;
        Bundle bundle = options;
        if (this.mStartIntentSenderForResult != null) {
            if (bundle != null) {
                if (fillInIntent == null) {
                    intent2 = new Intent();
                    intent2.putExtra(EXTRA_CREATED_FILLIN_INTENT, true);
                } else {
                    intent2 = fillInIntent;
                }
                if (isLoggingEnabled(2)) {
                    Log.v(TAG, "ActivityOptions " + bundle + " were added to fillInIntent " + intent2 + " for fragment " + fragment);
                }
                intent2.putExtra(ActivityResultContracts.StartActivityForResult.EXTRA_ACTIVITY_OPTIONS_BUNDLE, bundle);
            } else {
                intent2 = fillInIntent;
            }
            IntentSenderRequest build = new IntentSenderRequest.Builder(intent).setFillInIntent(intent2).setFlags(flagsValues, flagsMask).build();
            this.mLaunchedFragments.addLast(new LaunchedFragmentInfo(fragment.mWho, requestCode));
            if (isLoggingEnabled(2)) {
                Log.v(TAG, "Fragment " + fragment + "is launching an IntentSender for result ");
            }
            this.mStartIntentSenderForResult.launch(build);
            return;
        }
        IntentSender intentSender = intent;
        int i = requestCode;
        int i2 = flagsMask;
        int i3 = flagsValues;
        this.mHost.onStartIntentSenderFromFragment(f, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
        Intent intent3 = fillInIntent;
    }

    /* access modifiers changed from: package-private */
    public void moveFragmentToExpectedState(Fragment f) {
        if (this.mFragmentStore.containsActiveFragment(f.mWho)) {
            moveToState(f);
            if (!(f.mView == null || !f.mIsNewlyAdded || f.mContainer == null)) {
                if (f.mPostponedAlpha > 0.0f) {
                    f.mView.setAlpha(f.mPostponedAlpha);
                }
                f.mPostponedAlpha = 0.0f;
                f.mIsNewlyAdded = false;
                FragmentAnim.AnimationOrAnimator loadAnimation = FragmentAnim.loadAnimation(this.mHost.getContext(), f, true, f.getPopDirection());
                if (loadAnimation != null) {
                    if (loadAnimation.animation != null) {
                        f.mView.startAnimation(loadAnimation.animation);
                    } else {
                        loadAnimation.animator.setTarget(f.mView);
                        loadAnimation.animator.start();
                    }
                }
            }
            if (f.mHiddenChanged) {
                completeShowHideFragment(f);
            }
        } else if (isLoggingEnabled(3)) {
            Log.d(TAG, "Ignoring moving " + f + " to state " + this.mCurState + "since it is not added to " + this);
        }
    }

    /* access modifiers changed from: package-private */
    public void moveToState(int newState, boolean always) {
        FragmentHostCallback<?> fragmentHostCallback;
        if (this.mHost == null && newState != -1) {
            throw new IllegalStateException("No activity");
        } else if (always || newState != this.mCurState) {
            this.mCurState = newState;
            if (USE_STATE_MANAGER) {
                this.mFragmentStore.moveToExpectedState();
            } else {
                for (Fragment moveFragmentToExpectedState : this.mFragmentStore.getFragments()) {
                    moveFragmentToExpectedState(moveFragmentToExpectedState);
                }
                for (FragmentStateManager next : this.mFragmentStore.getActiveFragmentStateManagers()) {
                    Fragment fragment = next.getFragment();
                    if (!fragment.mIsNewlyAdded) {
                        moveFragmentToExpectedState(fragment);
                    }
                    if (fragment.mRemoving && !fragment.isInBackStack()) {
                        this.mFragmentStore.makeInactive(next);
                    }
                }
            }
            startPendingDeferredFragments();
            if (this.mNeedMenuInvalidate && (fragmentHostCallback = this.mHost) != null && this.mCurState == 7) {
                fragmentHostCallback.onSupportInvalidateOptionsMenu();
                this.mNeedMenuInvalidate = false;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void moveToState(Fragment f) {
        moveToState(f, this.mCurState);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0055, code lost:
        if (r12 <= 0) goto L_0x005a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0057, code lost:
        r0.create();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x005a, code lost:
        if (r12 <= -1) goto L_0x005f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x005c, code lost:
        r0.ensureInflatedView();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x005f, code lost:
        if (r12 <= 1) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0061, code lost:
        r0.createView();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0064, code lost:
        if (r12 <= 2) goto L_0x0069;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0066, code lost:
        r0.activityCreated();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0069, code lost:
        if (r12 <= 4) goto L_0x006e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x006b, code lost:
        r0.start();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x006e, code lost:
        if (r12 <= 5) goto L_0x0163;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0070, code lost:
        r0.resume();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0086, code lost:
        if (r12 >= 5) goto L_0x008b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0088, code lost:
        r0.stop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x008b, code lost:
        if (r12 >= 4) goto L_0x00bc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0091, code lost:
        if (isLoggingEnabled(3) == false) goto L_0x00a9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0093, code lost:
        android.util.Log.d(TAG, "movefrom ACTIVITY_CREATED: " + r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00ab, code lost:
        if (r11.mView == null) goto L_0x00bc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00b3, code lost:
        if (r10.mHost.onShouldSaveFragmentState(r11) == false) goto L_0x00bc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00b7, code lost:
        if (r11.mSavedViewState != null) goto L_0x00bc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00b9, code lost:
        r0.saveViewState();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00bc, code lost:
        if (r12 >= 2) goto L_0x014f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00be, code lost:
        r2 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00c1, code lost:
        if (r11.mView == null) goto L_0x0144;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00c5, code lost:
        if (r11.mContainer == null) goto L_0x0144;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00c7, code lost:
        r11.mContainer.endViewTransition(r11.mView);
        r11.mView.clearAnimation();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00d7, code lost:
        if (r11.isRemovingParent() != false) goto L_0x0144;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00dc, code lost:
        if (r10.mCurState <= -1) goto L_0x00ff;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00e0, code lost:
        if (r10.mDestroyed != false) goto L_0x00ff;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00e8, code lost:
        if (r11.mView.getVisibility() != 0) goto L_0x00ff;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00ee, code lost:
        if (r11.mPostponedAlpha < 0.0f) goto L_0x00ff;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x00f0, code lost:
        r2 = androidx.fragment.app.FragmentAnim.loadAnimation(r10.mHost.getContext(), r11, false, r11.getPopDirection());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x00ff, code lost:
        r11.mPostponedAlpha = 0.0f;
        r4 = r11.mContainer;
        r5 = r11.mView;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x0105, code lost:
        if (r2 == null) goto L_0x010c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x0107, code lost:
        androidx.fragment.app.FragmentAnim.animateRemoveFragment(r11, r2, r10.mFragmentTransitionCallback);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x010c, code lost:
        r4.removeView(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0113, code lost:
        if (isLoggingEnabled(2) == false) goto L_0x013f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0115, code lost:
        android.util.Log.v(TAG, "Removing view " + r5 + " for fragment " + r11 + " from container " + r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0141, code lost:
        if (r4 == r11.mContainer) goto L_0x0144;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x0143, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x014a, code lost:
        if (r10.mExitAnimationCancellationSignals.get(r11) != null) goto L_0x014f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x014c, code lost:
        r0.destroyFragmentView();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x014f, code lost:
        if (r12 >= 1) goto L_0x015e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0157, code lost:
        if (r10.mExitAnimationCancellationSignals.get(r11) == null) goto L_0x015b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x0159, code lost:
        r12 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x015b, code lost:
        r0.destroy();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x015e, code lost:
        if (r12 >= 0) goto L_0x0163;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x0160, code lost:
        r0.detach();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void moveToState(androidx.fragment.app.Fragment r11, int r12) {
        /*
            r10 = this;
            androidx.fragment.app.FragmentStore r0 = r10.mFragmentStore
            java.lang.String r1 = r11.mWho
            androidx.fragment.app.FragmentStateManager r0 = r0.getFragmentStateManager(r1)
            r1 = 1
            if (r0 != 0) goto L_0x0018
            androidx.fragment.app.FragmentStateManager r2 = new androidx.fragment.app.FragmentStateManager
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r3 = r10.mLifecycleCallbacksDispatcher
            androidx.fragment.app.FragmentStore r4 = r10.mFragmentStore
            r2.<init>(r3, r4, r11)
            r0 = r2
            r0.setFragmentManagerState(r1)
        L_0x0018:
            boolean r2 = r11.mFromLayout
            r3 = 2
            if (r2 == 0) goto L_0x0029
            boolean r2 = r11.mInLayout
            if (r2 == 0) goto L_0x0029
            int r2 = r11.mState
            if (r2 != r3) goto L_0x0029
            int r12 = java.lang.Math.max(r12, r3)
        L_0x0029:
            int r2 = r0.computeExpectedState()
            int r12 = java.lang.Math.min(r12, r2)
            int r2 = r11.mState
            r4 = 5
            r5 = 4
            r6 = 3
            java.lang.String r7 = "FragmentManager"
            r8 = -1
            if (r2 > r12) goto L_0x0075
            int r2 = r11.mState
            if (r2 >= r12) goto L_0x004a
            java.util.Map<androidx.fragment.app.Fragment, java.util.HashSet<androidx.core.os.CancellationSignal>> r2 = r10.mExitAnimationCancellationSignals
            boolean r2 = r2.isEmpty()
            if (r2 != 0) goto L_0x004a
            r10.cancelExitAnimation(r11)
        L_0x004a:
            int r2 = r11.mState
            switch(r2) {
                case -1: goto L_0x0050;
                case 0: goto L_0x0055;
                case 1: goto L_0x005a;
                case 2: goto L_0x0064;
                case 3: goto L_0x004f;
                case 4: goto L_0x0069;
                case 5: goto L_0x006e;
                default: goto L_0x004f;
            }
        L_0x004f:
            goto L_0x0073
        L_0x0050:
            if (r12 <= r8) goto L_0x0055
            r0.attach()
        L_0x0055:
            if (r12 <= 0) goto L_0x005a
            r0.create()
        L_0x005a:
            if (r12 <= r8) goto L_0x005f
            r0.ensureInflatedView()
        L_0x005f:
            if (r12 <= r1) goto L_0x0064
            r0.createView()
        L_0x0064:
            if (r12 <= r3) goto L_0x0069
            r0.activityCreated()
        L_0x0069:
            if (r12 <= r5) goto L_0x006e
            r0.start()
        L_0x006e:
            if (r12 <= r4) goto L_0x0073
            r0.resume()
        L_0x0073:
            goto L_0x0163
        L_0x0075:
            int r2 = r11.mState
            if (r2 <= r12) goto L_0x0163
            int r2 = r11.mState
            switch(r2) {
                case 0: goto L_0x015e;
                case 1: goto L_0x014f;
                case 2: goto L_0x00bc;
                case 3: goto L_0x007e;
                case 4: goto L_0x008b;
                case 5: goto L_0x0086;
                case 6: goto L_0x007e;
                case 7: goto L_0x0080;
                default: goto L_0x007e;
            }
        L_0x007e:
            goto L_0x0163
        L_0x0080:
            r2 = 7
            if (r12 >= r2) goto L_0x0086
            r0.pause()
        L_0x0086:
            if (r12 >= r4) goto L_0x008b
            r0.stop()
        L_0x008b:
            if (r12 >= r5) goto L_0x00bc
            boolean r2 = isLoggingEnabled(r6)
            if (r2 == 0) goto L_0x00a9
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r4 = "movefrom ACTIVITY_CREATED: "
            java.lang.StringBuilder r2 = r2.append(r4)
            java.lang.StringBuilder r2 = r2.append(r11)
            java.lang.String r2 = r2.toString()
            android.util.Log.d(r7, r2)
        L_0x00a9:
            android.view.View r2 = r11.mView
            if (r2 == 0) goto L_0x00bc
            androidx.fragment.app.FragmentHostCallback<?> r2 = r10.mHost
            boolean r2 = r2.onShouldSaveFragmentState(r11)
            if (r2 == 0) goto L_0x00bc
            android.util.SparseArray<android.os.Parcelable> r2 = r11.mSavedViewState
            if (r2 != 0) goto L_0x00bc
            r0.saveViewState()
        L_0x00bc:
            if (r12 >= r3) goto L_0x014f
            r2 = 0
            android.view.View r4 = r11.mView
            if (r4 == 0) goto L_0x0144
            android.view.ViewGroup r4 = r11.mContainer
            if (r4 == 0) goto L_0x0144
            android.view.ViewGroup r4 = r11.mContainer
            android.view.View r5 = r11.mView
            r4.endViewTransition(r5)
            android.view.View r4 = r11.mView
            r4.clearAnimation()
            boolean r4 = r11.isRemovingParent()
            if (r4 != 0) goto L_0x0144
            int r4 = r10.mCurState
            r5 = 0
            if (r4 <= r8) goto L_0x00ff
            boolean r4 = r10.mDestroyed
            if (r4 != 0) goto L_0x00ff
            android.view.View r4 = r11.mView
            int r4 = r4.getVisibility()
            if (r4 != 0) goto L_0x00ff
            float r4 = r11.mPostponedAlpha
            int r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r4 < 0) goto L_0x00ff
            androidx.fragment.app.FragmentHostCallback<?> r4 = r10.mHost
            android.content.Context r4 = r4.getContext()
            r8 = 0
            boolean r9 = r11.getPopDirection()
            androidx.fragment.app.FragmentAnim$AnimationOrAnimator r2 = androidx.fragment.app.FragmentAnim.loadAnimation(r4, r11, r8, r9)
        L_0x00ff:
            r11.mPostponedAlpha = r5
            android.view.ViewGroup r4 = r11.mContainer
            android.view.View r5 = r11.mView
            if (r2 == 0) goto L_0x010c
            androidx.fragment.app.FragmentTransition$Callback r8 = r10.mFragmentTransitionCallback
            androidx.fragment.app.FragmentAnim.animateRemoveFragment(r11, r2, r8)
        L_0x010c:
            r4.removeView(r5)
            boolean r3 = isLoggingEnabled(r3)
            if (r3 == 0) goto L_0x013f
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r8 = "Removing view "
            java.lang.StringBuilder r3 = r3.append(r8)
            java.lang.StringBuilder r3 = r3.append(r5)
            java.lang.String r8 = " for fragment "
            java.lang.StringBuilder r3 = r3.append(r8)
            java.lang.StringBuilder r3 = r3.append(r11)
            java.lang.String r8 = " from container "
            java.lang.StringBuilder r3 = r3.append(r8)
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            android.util.Log.v(r7, r3)
        L_0x013f:
            android.view.ViewGroup r3 = r11.mContainer
            if (r4 == r3) goto L_0x0144
            return
        L_0x0144:
            java.util.Map<androidx.fragment.app.Fragment, java.util.HashSet<androidx.core.os.CancellationSignal>> r3 = r10.mExitAnimationCancellationSignals
            java.lang.Object r3 = r3.get(r11)
            if (r3 != 0) goto L_0x014f
            r0.destroyFragmentView()
        L_0x014f:
            if (r12 >= r1) goto L_0x015e
            java.util.Map<androidx.fragment.app.Fragment, java.util.HashSet<androidx.core.os.CancellationSignal>> r1 = r10.mExitAnimationCancellationSignals
            java.lang.Object r1 = r1.get(r11)
            if (r1 == 0) goto L_0x015b
            r12 = 1
            goto L_0x015e
        L_0x015b:
            r0.destroy()
        L_0x015e:
            if (r12 >= 0) goto L_0x0163
            r0.detach()
        L_0x0163:
            int r1 = r11.mState
            if (r1 == r12) goto L_0x019b
            boolean r1 = isLoggingEnabled(r6)
            if (r1 == 0) goto L_0x0199
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "moveToState: Fragment state for "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r11)
            java.lang.String r2 = " not updated inline; expected state "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r12)
            java.lang.String r2 = " found "
            java.lang.StringBuilder r1 = r1.append(r2)
            int r2 = r11.mState
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r7, r1)
        L_0x0199:
            r11.mState = r12
        L_0x019b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentManager.moveToState(androidx.fragment.app.Fragment, int):void");
    }

    /* access modifiers changed from: package-private */
    public void noteStateNotSaved() {
        if (this.mHost != null) {
            this.mStateSaved = false;
            this.mStopped = false;
            this.mNonConfig.setIsStateSaved(false);
            for (Fragment next : this.mFragmentStore.getFragments()) {
                if (next != null) {
                    next.noteStateNotSaved();
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onContainerAvailable(FragmentContainerView container) {
        for (FragmentStateManager next : this.mFragmentStore.getActiveFragmentStateManagers()) {
            Fragment fragment = next.getFragment();
            if (fragment.mContainerId == container.getId() && fragment.mView != null && fragment.mView.getParent() == null) {
                fragment.mContainer = container;
                next.addViewToContainer();
            }
        }
    }

    @Deprecated
    public FragmentTransaction openTransaction() {
        return beginTransaction();
    }

    /* access modifiers changed from: package-private */
    public void performPendingDeferredStart(FragmentStateManager fragmentStateManager) {
        Fragment fragment = fragmentStateManager.getFragment();
        if (!fragment.mDeferStart) {
            return;
        }
        if (this.mExecutingActions) {
            this.mHavePendingDeferredStart = true;
            return;
        }
        fragment.mDeferStart = false;
        if (USE_STATE_MANAGER) {
            fragmentStateManager.moveToExpectedState();
        } else {
            moveToState(fragment);
        }
    }

    public void popBackStack() {
        enqueueAction(new PopBackStackState((String) null, -1, 0), false);
    }

    public void popBackStack(int id, int flags) {
        if (id >= 0) {
            enqueueAction(new PopBackStackState((String) null, id, flags), false);
            return;
        }
        throw new IllegalArgumentException("Bad id: " + id);
    }

    public void popBackStack(String name, int flags) {
        enqueueAction(new PopBackStackState(name, -1, flags), false);
    }

    public boolean popBackStackImmediate() {
        return popBackStackImmediate((String) null, -1, 0);
    }

    public boolean popBackStackImmediate(int id, int flags) {
        if (id >= 0) {
            return popBackStackImmediate((String) null, id, flags);
        }
        throw new IllegalArgumentException("Bad id: " + id);
    }

    public boolean popBackStackImmediate(String name, int flags) {
        return popBackStackImmediate(name, -1, flags);
    }

    /* access modifiers changed from: package-private */
    public boolean popBackStackState(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, String name, int id, int flags) {
        ArrayList<BackStackRecord> arrayList3 = this.mBackStack;
        if (arrayList3 == null) {
            return false;
        }
        if (name == null && id < 0 && (flags & 1) == 0) {
            int size = arrayList3.size() - 1;
            if (size < 0) {
                return false;
            }
            arrayList.add(this.mBackStack.remove(size));
            arrayList2.add(true);
        } else {
            int i = -1;
            if (name != null || id >= 0) {
                int size2 = arrayList3.size() - 1;
                while (i >= 0) {
                    BackStackRecord backStackRecord = this.mBackStack.get(i);
                    if ((name != null && name.equals(backStackRecord.getName())) || (id >= 0 && id == backStackRecord.mIndex)) {
                        break;
                    }
                    size2 = i - 1;
                }
                if (i < 0) {
                    return false;
                }
                if ((flags & 1) != 0) {
                    i--;
                    while (i >= 0) {
                        BackStackRecord backStackRecord2 = this.mBackStack.get(i);
                        if ((name == null || !name.equals(backStackRecord2.getName())) && (id < 0 || id != backStackRecord2.mIndex)) {
                            break;
                        }
                        i--;
                    }
                }
            }
            if (i == this.mBackStack.size() - 1) {
                return false;
            }
            for (int size3 = this.mBackStack.size() - 1; size3 > i; size3--) {
                arrayList.add(this.mBackStack.remove(size3));
                arrayList2.add(true);
            }
        }
        return true;
    }

    public void putFragment(Bundle bundle, String key, Fragment fragment) {
        if (fragment.mFragmentManager != this) {
            throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        bundle.putString(key, fragment.mWho);
    }

    public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb, boolean recursive) {
        this.mLifecycleCallbacksDispatcher.registerFragmentLifecycleCallbacks(cb, recursive);
    }

    /* access modifiers changed from: package-private */
    public void removeCancellationSignal(Fragment f, CancellationSignal signal) {
        HashSet hashSet = this.mExitAnimationCancellationSignals.get(f);
        if (hashSet != null && hashSet.remove(signal) && hashSet.isEmpty()) {
            this.mExitAnimationCancellationSignals.remove(f);
            if (f.mState < 5) {
                destroyFragmentView(f);
                moveToState(f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void removeFragment(Fragment fragment) {
        if (isLoggingEnabled(2)) {
            Log.v(TAG, "remove: " + fragment + " nesting=" + fragment.mBackStackNesting);
        }
        boolean z = !fragment.isInBackStack();
        if (!fragment.mDetached || z) {
            this.mFragmentStore.removeFragment(fragment);
            if (isMenuAvailable(fragment)) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mRemoving = true;
            setVisibleRemovingFragment(fragment);
        }
    }

    public void removeFragmentOnAttachListener(FragmentOnAttachListener listener) {
        this.mOnAttachListeners.remove(listener);
    }

    public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {
        ArrayList<OnBackStackChangedListener> arrayList = this.mBackStackChangeListeners;
        if (arrayList != null) {
            arrayList.remove(listener);
        }
    }

    /* access modifiers changed from: package-private */
    public void removeRetainedFragment(Fragment f) {
        this.mNonConfig.removeRetainedFragment(f);
    }

    /* access modifiers changed from: package-private */
    public void restoreAllState(Parcelable state, FragmentManagerNonConfig nonConfig) {
        if (this.mHost instanceof ViewModelStoreOwner) {
            throwException(new IllegalStateException("You must use restoreSaveState when your FragmentHostCallback implements ViewModelStoreOwner"));
        }
        this.mNonConfig.restoreFromSnapshot(nonConfig);
        restoreSaveState(state);
    }

    /* access modifiers changed from: package-private */
    public void restoreSaveState(Parcelable state) {
        FragmentStateManager fragmentStateManager;
        if (state != null) {
            FragmentManagerState fragmentManagerState = (FragmentManagerState) state;
            if (fragmentManagerState.mActive != null) {
                this.mFragmentStore.resetActiveFragments();
                Iterator<FragmentState> it = fragmentManagerState.mActive.iterator();
                while (it.hasNext()) {
                    FragmentState next = it.next();
                    if (next != null) {
                        Fragment findRetainedFragmentByWho = this.mNonConfig.findRetainedFragmentByWho(next.mWho);
                        if (findRetainedFragmentByWho != null) {
                            if (isLoggingEnabled(2)) {
                                Log.v(TAG, "restoreSaveState: re-attaching retained " + findRetainedFragmentByWho);
                            }
                            fragmentStateManager = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, findRetainedFragmentByWho, next);
                        } else {
                            fragmentStateManager = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, this.mHost.getContext().getClassLoader(), getFragmentFactory(), next);
                        }
                        Fragment fragment = fragmentStateManager.getFragment();
                        fragment.mFragmentManager = this;
                        if (isLoggingEnabled(2)) {
                            Log.v(TAG, "restoreSaveState: active (" + fragment.mWho + "): " + fragment);
                        }
                        fragmentStateManager.restoreState(this.mHost.getContext().getClassLoader());
                        this.mFragmentStore.makeActive(fragmentStateManager);
                        fragmentStateManager.setFragmentManagerState(this.mCurState);
                    }
                }
                for (Fragment next2 : this.mNonConfig.getRetainedFragments()) {
                    if (!this.mFragmentStore.containsActiveFragment(next2.mWho)) {
                        if (isLoggingEnabled(2)) {
                            Log.v(TAG, "Discarding retained Fragment " + next2 + " that was not found in the set of active Fragments " + fragmentManagerState.mActive);
                        }
                        this.mNonConfig.removeRetainedFragment(next2);
                        next2.mFragmentManager = this;
                        FragmentStateManager fragmentStateManager2 = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, next2);
                        fragmentStateManager2.setFragmentManagerState(1);
                        fragmentStateManager2.moveToExpectedState();
                        next2.mRemoving = true;
                        fragmentStateManager2.moveToExpectedState();
                    }
                }
                this.mFragmentStore.restoreAddedFragments(fragmentManagerState.mAdded);
                if (fragmentManagerState.mBackStack != null) {
                    this.mBackStack = new ArrayList<>(fragmentManagerState.mBackStack.length);
                    for (int i = 0; i < fragmentManagerState.mBackStack.length; i++) {
                        BackStackRecord instantiate = fragmentManagerState.mBackStack[i].instantiate(this);
                        if (isLoggingEnabled(2)) {
                            Log.v(TAG, "restoreAllState: back stack #" + i + " (index " + instantiate.mIndex + "): " + instantiate);
                            PrintWriter printWriter = new PrintWriter(new LogWriter(TAG));
                            instantiate.dump("  ", printWriter, false);
                            printWriter.close();
                        }
                        this.mBackStack.add(instantiate);
                    }
                } else {
                    this.mBackStack = null;
                }
                this.mBackStackIndex.set(fragmentManagerState.mBackStackIndex);
                if (fragmentManagerState.mPrimaryNavActiveWho != null) {
                    Fragment findActiveFragment = findActiveFragment(fragmentManagerState.mPrimaryNavActiveWho);
                    this.mPrimaryNav = findActiveFragment;
                    dispatchParentPrimaryNavigationFragmentChanged(findActiveFragment);
                }
                ArrayList<String> arrayList = fragmentManagerState.mResultKeys;
                if (arrayList != null) {
                    for (int i2 = 0; i2 < arrayList.size(); i2++) {
                        Bundle bundle = fragmentManagerState.mResults.get(i2);
                        bundle.setClassLoader(this.mHost.getContext().getClassLoader());
                        this.mResults.put(arrayList.get(i2), bundle);
                    }
                }
                this.mLaunchedFragments = new ArrayDeque<>(fragmentManagerState.mLaunchedFragments);
            }
        }
    }

    /* access modifiers changed from: package-private */
    @Deprecated
    public FragmentManagerNonConfig retainNonConfig() {
        if (this.mHost instanceof ViewModelStoreOwner) {
            throwException(new IllegalStateException("You cannot use retainNonConfig when your FragmentHostCallback implements ViewModelStoreOwner."));
        }
        return this.mNonConfig.getSnapshot();
    }

    /* access modifiers changed from: package-private */
    public Parcelable saveAllState() {
        int size;
        forcePostponedTransactions();
        endAnimatingAwayFragments();
        execPendingActions(true);
        this.mStateSaved = true;
        this.mNonConfig.setIsStateSaved(true);
        ArrayList<FragmentState> saveActiveFragments = this.mFragmentStore.saveActiveFragments();
        if (!saveActiveFragments.isEmpty()) {
            ArrayList<String> saveAddedFragments = this.mFragmentStore.saveAddedFragments();
            BackStackState[] backStackStateArr = null;
            ArrayList<BackStackRecord> arrayList = this.mBackStack;
            if (arrayList != null && (size = arrayList.size()) > 0) {
                backStackStateArr = new BackStackState[size];
                for (int i = 0; i < size; i++) {
                    backStackStateArr[i] = new BackStackState(this.mBackStack.get(i));
                    if (isLoggingEnabled(2)) {
                        Log.v(TAG, "saveAllState: adding back stack #" + i + ": " + this.mBackStack.get(i));
                    }
                }
            }
            FragmentManagerState fragmentManagerState = new FragmentManagerState();
            fragmentManagerState.mActive = saveActiveFragments;
            fragmentManagerState.mAdded = saveAddedFragments;
            fragmentManagerState.mBackStack = backStackStateArr;
            fragmentManagerState.mBackStackIndex = this.mBackStackIndex.get();
            Fragment fragment = this.mPrimaryNav;
            if (fragment != null) {
                fragmentManagerState.mPrimaryNavActiveWho = fragment.mWho;
            }
            fragmentManagerState.mResultKeys.addAll(this.mResults.keySet());
            fragmentManagerState.mResults.addAll(this.mResults.values());
            fragmentManagerState.mLaunchedFragments = new ArrayList<>(this.mLaunchedFragments);
            return fragmentManagerState;
        } else if (!isLoggingEnabled(2)) {
            return null;
        } else {
            Log.v(TAG, "saveAllState: no fragments!");
            return null;
        }
    }

    public Fragment.SavedState saveFragmentInstanceState(Fragment fragment) {
        FragmentStateManager fragmentStateManager = this.mFragmentStore.getFragmentStateManager(fragment.mWho);
        if (fragmentStateManager == null || !fragmentStateManager.getFragment().equals(fragment)) {
            throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        return fragmentStateManager.saveInstanceState();
    }

    /* access modifiers changed from: package-private */
    public void scheduleCommit() {
        synchronized (this.mPendingActions) {
            ArrayList<StartEnterTransitionListener> arrayList = this.mPostponedTransactions;
            boolean z = false;
            boolean z2 = arrayList != null && !arrayList.isEmpty();
            if (this.mPendingActions.size() == 1) {
                z = true;
            }
            if (z2 || z) {
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                this.mHost.getHandler().post(this.mExecCommit);
                updateOnBackPressedCallbackEnabled();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setExitAnimationOrder(Fragment f, boolean isPop) {
        ViewGroup fragmentContainer = getFragmentContainer(f);
        if (fragmentContainer != null && (fragmentContainer instanceof FragmentContainerView)) {
            ((FragmentContainerView) fragmentContainer).setDrawDisappearingViewsLast(!isPop);
        }
    }

    public void setFragmentFactory(FragmentFactory fragmentFactory) {
        this.mFragmentFactory = fragmentFactory;
    }

    public final void setFragmentResult(String requestKey, Bundle result) {
        LifecycleAwareResultListener lifecycleAwareResultListener = this.mResultListeners.get(requestKey);
        if (lifecycleAwareResultListener == null || !lifecycleAwareResultListener.isAtLeast(Lifecycle.State.STARTED)) {
            this.mResults.put(requestKey, result);
        } else {
            lifecycleAwareResultListener.onFragmentResult(requestKey, result);
        }
    }

    public final void setFragmentResultListener(final String requestKey, LifecycleOwner lifecycleOwner, final FragmentResultListener listener) {
        final Lifecycle lifecycle = lifecycleOwner.getLifecycle();
        if (lifecycle.getCurrentState() != Lifecycle.State.DESTROYED) {
            AnonymousClass6 r1 = new LifecycleEventObserver() {
                public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                    Bundle bundle;
                    if (event == Lifecycle.Event.ON_START && (bundle = (Bundle) FragmentManager.this.mResults.get(requestKey)) != null) {
                        listener.onFragmentResult(requestKey, bundle);
                        FragmentManager.this.clearFragmentResult(requestKey);
                    }
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        lifecycle.removeObserver(this);
                        FragmentManager.this.mResultListeners.remove(requestKey);
                    }
                }
            };
            lifecycle.addObserver(r1);
            LifecycleAwareResultListener put = this.mResultListeners.put(requestKey, new LifecycleAwareResultListener(lifecycle, listener, r1));
            if (put != null) {
                put.removeObserver();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setMaxLifecycle(Fragment f, Lifecycle.State state) {
        if (!f.equals(findActiveFragment(f.mWho)) || !(f.mHost == null || f.mFragmentManager == this)) {
            throw new IllegalArgumentException("Fragment " + f + " is not an active fragment of FragmentManager " + this);
        }
        f.mMaxState = state;
    }

    /* access modifiers changed from: package-private */
    public void setPrimaryNavigationFragment(Fragment f) {
        if (f == null || (f.equals(findActiveFragment(f.mWho)) && (f.mHost == null || f.mFragmentManager == this))) {
            Fragment fragment = this.mPrimaryNav;
            this.mPrimaryNav = f;
            dispatchParentPrimaryNavigationFragmentChanged(fragment);
            dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
            return;
        }
        throw new IllegalArgumentException("Fragment " + f + " is not an active fragment of FragmentManager " + this);
    }

    /* access modifiers changed from: package-private */
    public void setSpecialEffectsControllerFactory(SpecialEffectsControllerFactory specialEffectsControllerFactory) {
        this.mSpecialEffectsControllerFactory = specialEffectsControllerFactory;
    }

    /* access modifiers changed from: package-private */
    public void showFragment(Fragment fragment) {
        if (isLoggingEnabled(2)) {
            Log.v(TAG, "show: " + fragment);
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            fragment.mHiddenChanged = !fragment.mHiddenChanged;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        String hexString = Integer.toHexString(System.identityHashCode(this));
        Log1F380D.a((Object) hexString);
        sb.append(hexString);
        sb.append(" in ");
        Fragment fragment = this.mParent;
        if (fragment != null) {
            sb.append(fragment.getClass().getSimpleName());
            sb.append("{");
            String hexString2 = Integer.toHexString(System.identityHashCode(this.mParent));
            Log1F380D.a((Object) hexString2);
            sb.append(hexString2);
            sb.append("}");
        } else {
            FragmentHostCallback<?> fragmentHostCallback = this.mHost;
            if (fragmentHostCallback != null) {
                sb.append(fragmentHostCallback.getClass().getSimpleName());
                sb.append("{");
                String hexString3 = Integer.toHexString(System.identityHashCode(this.mHost));
                Log1F380D.a((Object) hexString3);
                sb.append(hexString3);
                sb.append("}");
            } else {
                sb.append("null");
            }
        }
        sb.append("}}");
        return sb.toString();
    }

    public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb) {
        this.mLifecycleCallbacksDispatcher.unregisterFragmentLifecycleCallbacks(cb);
    }
}
