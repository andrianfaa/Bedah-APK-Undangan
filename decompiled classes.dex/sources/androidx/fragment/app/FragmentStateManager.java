package androidx.fragment.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.fragment.R;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.SpecialEffectsController;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelStoreOwner;

/* compiled from: 007C */
class FragmentStateManager {
    private static final String TAG = "FragmentManager";
    private static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    private static final String TARGET_STATE_TAG = "android:target_state";
    private static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    private static final String VIEW_REGISTRY_STATE_TAG = "android:view_registry_state";
    private static final String VIEW_STATE_TAG = "android:view_state";
    private final FragmentLifecycleCallbacksDispatcher mDispatcher;
    private final Fragment mFragment;
    private int mFragmentManagerState = -1;
    private final FragmentStore mFragmentStore;
    private boolean mMovingToState = false;

    /* renamed from: androidx.fragment.app.FragmentStateManager$2  reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$androidx$lifecycle$Lifecycle$State;

        static {
            int[] iArr = new int[Lifecycle.State.values().length];
            $SwitchMap$androidx$lifecycle$Lifecycle$State = iArr;
            try {
                iArr[Lifecycle.State.RESUMED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$lifecycle$Lifecycle$State[Lifecycle.State.STARTED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$lifecycle$Lifecycle$State[Lifecycle.State.CREATED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$lifecycle$Lifecycle$State[Lifecycle.State.INITIALIZED.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    FragmentStateManager(FragmentLifecycleCallbacksDispatcher dispatcher, FragmentStore fragmentStore, Fragment fragment) {
        this.mDispatcher = dispatcher;
        this.mFragmentStore = fragmentStore;
        this.mFragment = fragment;
    }

    FragmentStateManager(FragmentLifecycleCallbacksDispatcher dispatcher, FragmentStore fragmentStore, Fragment retainedFragment, FragmentState fs) {
        this.mDispatcher = dispatcher;
        this.mFragmentStore = fragmentStore;
        this.mFragment = retainedFragment;
        retainedFragment.mSavedViewState = null;
        retainedFragment.mSavedViewRegistryState = null;
        retainedFragment.mBackStackNesting = 0;
        retainedFragment.mInLayout = false;
        retainedFragment.mAdded = false;
        retainedFragment.mTargetWho = retainedFragment.mTarget != null ? retainedFragment.mTarget.mWho : null;
        retainedFragment.mTarget = null;
        if (fs.mSavedFragmentState != null) {
            retainedFragment.mSavedFragmentState = fs.mSavedFragmentState;
        } else {
            retainedFragment.mSavedFragmentState = new Bundle();
        }
    }

    FragmentStateManager(FragmentLifecycleCallbacksDispatcher dispatcher, FragmentStore fragmentStore, ClassLoader classLoader, FragmentFactory fragmentFactory, FragmentState fs) {
        this.mDispatcher = dispatcher;
        this.mFragmentStore = fragmentStore;
        Fragment instantiate = fragmentFactory.instantiate(classLoader, fs.mClassName);
        this.mFragment = instantiate;
        if (fs.mArguments != null) {
            fs.mArguments.setClassLoader(classLoader);
        }
        instantiate.setArguments(fs.mArguments);
        instantiate.mWho = fs.mWho;
        instantiate.mFromLayout = fs.mFromLayout;
        instantiate.mRestored = true;
        instantiate.mFragmentId = fs.mFragmentId;
        instantiate.mContainerId = fs.mContainerId;
        instantiate.mTag = fs.mTag;
        instantiate.mRetainInstance = fs.mRetainInstance;
        instantiate.mRemoving = fs.mRemoving;
        instantiate.mDetached = fs.mDetached;
        instantiate.mHidden = fs.mHidden;
        instantiate.mMaxState = Lifecycle.State.values()[fs.mMaxLifecycleState];
        if (fs.mSavedFragmentState != null) {
            instantiate.mSavedFragmentState = fs.mSavedFragmentState;
        } else {
            instantiate.mSavedFragmentState = new Bundle();
        }
        if (FragmentManager.isLoggingEnabled(2)) {
            Log.v(TAG, "Instantiated fragment " + instantiate);
        }
    }

    private boolean isFragmentViewChild(View view) {
        if (view == this.mFragment.mView) {
            return true;
        }
        for (ViewParent parent = view.getParent(); parent != null; parent = parent.getParent()) {
            if (parent == this.mFragment.mView) {
                return true;
            }
        }
        return false;
    }

    private Bundle saveBasicState() {
        Bundle bundle = new Bundle();
        this.mFragment.performSaveInstanceState(bundle);
        this.mDispatcher.dispatchOnFragmentSaveInstanceState(this.mFragment, bundle, false);
        if (bundle.isEmpty()) {
            bundle = null;
        }
        if (this.mFragment.mView != null) {
            saveViewState();
        }
        if (this.mFragment.mSavedViewState != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray(VIEW_STATE_TAG, this.mFragment.mSavedViewState);
        }
        if (this.mFragment.mSavedViewRegistryState != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putBundle(VIEW_REGISTRY_STATE_TAG, this.mFragment.mSavedViewRegistryState);
        }
        if (!this.mFragment.mUserVisibleHint) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putBoolean(USER_VISIBLE_HINT_TAG, this.mFragment.mUserVisibleHint);
        }
        return bundle;
    }

    /* access modifiers changed from: package-private */
    public void activityCreated() {
        if (FragmentManager.isLoggingEnabled(3)) {
            Log.d(TAG, "moveto ACTIVITY_CREATED: " + this.mFragment);
        }
        Fragment fragment = this.mFragment;
        fragment.performActivityCreated(fragment.mSavedFragmentState);
        FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.mDispatcher;
        Fragment fragment2 = this.mFragment;
        fragmentLifecycleCallbacksDispatcher.dispatchOnFragmentActivityCreated(fragment2, fragment2.mSavedFragmentState, false);
    }

    /* access modifiers changed from: package-private */
    public void addViewToContainer() {
        this.mFragment.mContainer.addView(this.mFragment.mView, this.mFragmentStore.findFragmentIndexInContainer(this.mFragment));
    }

    /* access modifiers changed from: package-private */
    public void attach() {
        FragmentStateManager fragmentStateManager;
        if (FragmentManager.isLoggingEnabled(3)) {
            Log.d(TAG, "moveto ATTACHED: " + this.mFragment);
        }
        if (this.mFragment.mTarget != null) {
            fragmentStateManager = this.mFragmentStore.getFragmentStateManager(this.mFragment.mTarget.mWho);
            if (fragmentStateManager != null) {
                Fragment fragment = this.mFragment;
                fragment.mTargetWho = fragment.mTarget.mWho;
                this.mFragment.mTarget = null;
            } else {
                throw new IllegalStateException("Fragment " + this.mFragment + " declared target fragment " + this.mFragment.mTarget + " that does not belong to this FragmentManager!");
            }
        } else if (this.mFragment.mTargetWho != null) {
            fragmentStateManager = this.mFragmentStore.getFragmentStateManager(this.mFragment.mTargetWho);
            if (fragmentStateManager == null) {
                throw new IllegalStateException("Fragment " + this.mFragment + " declared target fragment " + this.mFragment.mTargetWho + " that does not belong to this FragmentManager!");
            }
        } else {
            fragmentStateManager = null;
        }
        if (fragmentStateManager != null && (FragmentManager.USE_STATE_MANAGER || fragmentStateManager.getFragment().mState < 1)) {
            fragmentStateManager.moveToExpectedState();
        }
        Fragment fragment2 = this.mFragment;
        fragment2.mHost = fragment2.mFragmentManager.getHost();
        Fragment fragment3 = this.mFragment;
        fragment3.mParentFragment = fragment3.mFragmentManager.getParent();
        this.mDispatcher.dispatchOnFragmentPreAttached(this.mFragment, false);
        this.mFragment.performAttach();
        this.mDispatcher.dispatchOnFragmentAttached(this.mFragment, false);
    }

    /* access modifiers changed from: package-private */
    public int computeExpectedState() {
        if (this.mFragment.mFragmentManager == null) {
            return this.mFragment.mState;
        }
        int i = this.mFragmentManagerState;
        switch (AnonymousClass2.$SwitchMap$androidx$lifecycle$Lifecycle$State[this.mFragment.mMaxState.ordinal()]) {
            case 1:
                break;
            case 2:
                i = Math.min(i, 5);
                break;
            case 3:
                i = Math.min(i, 1);
                break;
            case 4:
                i = Math.min(i, 0);
                break;
            default:
                i = Math.min(i, -1);
                break;
        }
        if (this.mFragment.mFromLayout) {
            if (this.mFragment.mInLayout) {
                i = Math.max(this.mFragmentManagerState, 2);
                if (this.mFragment.mView != null && this.mFragment.mView.getParent() == null) {
                    i = Math.min(i, 2);
                }
            } else {
                i = this.mFragmentManagerState < 4 ? Math.min(i, this.mFragment.mState) : Math.min(i, 1);
            }
        }
        if (!this.mFragment.mAdded) {
            i = Math.min(i, 1);
        }
        SpecialEffectsController.Operation.LifecycleImpact lifecycleImpact = null;
        if (FragmentManager.USE_STATE_MANAGER && this.mFragment.mContainer != null) {
            lifecycleImpact = SpecialEffectsController.getOrCreateController(this.mFragment.mContainer, this.mFragment.getParentFragmentManager()).getAwaitingCompletionLifecycleImpact(this);
        }
        if (lifecycleImpact == SpecialEffectsController.Operation.LifecycleImpact.ADDING) {
            i = Math.min(i, 6);
        } else if (lifecycleImpact == SpecialEffectsController.Operation.LifecycleImpact.REMOVING) {
            i = Math.max(i, 3);
        } else if (this.mFragment.mRemoving) {
            i = this.mFragment.isInBackStack() ? Math.min(i, 1) : Math.min(i, -1);
        }
        if (this.mFragment.mDeferStart && this.mFragment.mState < 5) {
            i = Math.min(i, 4);
        }
        if (FragmentManager.isLoggingEnabled(2)) {
            Log.v(TAG, "computeExpectedState() of " + i + " for " + this.mFragment);
        }
        return i;
    }

    /* access modifiers changed from: package-private */
    public void create() {
        if (FragmentManager.isLoggingEnabled(3)) {
            Log.d(TAG, "moveto CREATED: " + this.mFragment);
        }
        if (!this.mFragment.mIsCreated) {
            FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.mDispatcher;
            Fragment fragment = this.mFragment;
            fragmentLifecycleCallbacksDispatcher.dispatchOnFragmentPreCreated(fragment, fragment.mSavedFragmentState, false);
            Fragment fragment2 = this.mFragment;
            fragment2.performCreate(fragment2.mSavedFragmentState);
            FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher2 = this.mDispatcher;
            Fragment fragment3 = this.mFragment;
            fragmentLifecycleCallbacksDispatcher2.dispatchOnFragmentCreated(fragment3, fragment3.mSavedFragmentState, false);
            return;
        }
        Fragment fragment4 = this.mFragment;
        fragment4.restoreChildFragmentState(fragment4.mSavedFragmentState);
        this.mFragment.mState = 1;
    }

    /* JADX WARNING: type inference failed for: r4v8, types: [android.view.View] */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void createView() {
        /*
            r9 = this;
            androidx.fragment.app.Fragment r0 = r9.mFragment
            boolean r0 = r0.mFromLayout
            if (r0 == 0) goto L_0x0007
            return
        L_0x0007:
            r0 = 3
            boolean r0 = androidx.fragment.app.FragmentManager.isLoggingEnabled(r0)
            java.lang.String r1 = "FragmentManager"
            if (r0 == 0) goto L_0x0028
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "moveto CREATE_VIEW: "
            java.lang.StringBuilder r0 = r0.append(r2)
            androidx.fragment.app.Fragment r2 = r9.mFragment
            java.lang.StringBuilder r0 = r0.append(r2)
            java.lang.String r0 = r0.toString()
            android.util.Log.d(r1, r0)
        L_0x0028:
            androidx.fragment.app.Fragment r0 = r9.mFragment
            android.os.Bundle r2 = r0.mSavedFragmentState
            android.view.LayoutInflater r0 = r0.performGetLayoutInflater(r2)
            r2 = 0
            androidx.fragment.app.Fragment r3 = r9.mFragment
            android.view.ViewGroup r3 = r3.mContainer
            if (r3 == 0) goto L_0x003d
            androidx.fragment.app.Fragment r3 = r9.mFragment
            android.view.ViewGroup r2 = r3.mContainer
            goto L_0x00d5
        L_0x003d:
            androidx.fragment.app.Fragment r3 = r9.mFragment
            int r3 = r3.mContainerId
            if (r3 == 0) goto L_0x00d5
            androidx.fragment.app.Fragment r3 = r9.mFragment
            int r3 = r3.mContainerId
            r4 = -1
            if (r3 == r4) goto L_0x00b4
            androidx.fragment.app.Fragment r3 = r9.mFragment
            androidx.fragment.app.FragmentManager r3 = r3.mFragmentManager
            androidx.fragment.app.FragmentContainer r3 = r3.getContainer()
            androidx.fragment.app.Fragment r4 = r9.mFragment
            int r4 = r4.mContainerId
            android.view.View r4 = r3.onFindViewById(r4)
            r2 = r4
            android.view.ViewGroup r2 = (android.view.ViewGroup) r2
            if (r2 != 0) goto L_0x00d5
            androidx.fragment.app.Fragment r4 = r9.mFragment
            boolean r4 = r4.mRestored
            if (r4 == 0) goto L_0x0066
            goto L_0x00d5
        L_0x0066:
            androidx.fragment.app.Fragment r1 = r9.mFragment     // Catch:{ NotFoundException -> 0x0075 }
            android.content.res.Resources r1 = r1.getResources()     // Catch:{ NotFoundException -> 0x0075 }
            androidx.fragment.app.Fragment r4 = r9.mFragment     // Catch:{ NotFoundException -> 0x0075 }
            int r4 = r4.mContainerId     // Catch:{ NotFoundException -> 0x0075 }
            java.lang.String r1 = r1.getResourceName(r4)     // Catch:{ NotFoundException -> 0x0075 }
            goto L_0x007a
        L_0x0075:
            r1 = move-exception
            java.lang.String r4 = "unknown"
            r1 = r4
        L_0x007a:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "No view found for id 0x"
            java.lang.StringBuilder r5 = r5.append(r6)
            androidx.fragment.app.Fragment r6 = r9.mFragment
            int r6 = r6.mContainerId
            java.lang.String r6 = java.lang.Integer.toHexString(r6)
            mt.Log1F380D.a((java.lang.Object) r6)
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = " ("
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.StringBuilder r5 = r5.append(r1)
            java.lang.String r6 = ") for fragment "
            java.lang.StringBuilder r5 = r5.append(r6)
            androidx.fragment.app.Fragment r6 = r9.mFragment
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r5 = r5.toString()
            r4.<init>(r5)
            throw r4
        L_0x00b4:
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Cannot create fragment "
            java.lang.StringBuilder r3 = r3.append(r4)
            androidx.fragment.app.Fragment r4 = r9.mFragment
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = " for a container view with no id"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            r1.<init>(r3)
            throw r1
        L_0x00d5:
            androidx.fragment.app.Fragment r3 = r9.mFragment
            r3.mContainer = r2
            androidx.fragment.app.Fragment r3 = r9.mFragment
            android.os.Bundle r4 = r3.mSavedFragmentState
            r3.performCreateView(r0, r2, r4)
            androidx.fragment.app.Fragment r3 = r9.mFragment
            android.view.View r3 = r3.mView
            r4 = 2
            if (r3 == 0) goto L_0x01ab
            androidx.fragment.app.Fragment r3 = r9.mFragment
            android.view.View r3 = r3.mView
            r5 = 0
            r3.setSaveFromParentEnabled(r5)
            androidx.fragment.app.Fragment r3 = r9.mFragment
            android.view.View r3 = r3.mView
            int r6 = androidx.fragment.R.id.fragment_container_view_tag
            androidx.fragment.app.Fragment r7 = r9.mFragment
            r3.setTag(r6, r7)
            if (r2 == 0) goto L_0x00ff
            r9.addViewToContainer()
        L_0x00ff:
            androidx.fragment.app.Fragment r3 = r9.mFragment
            boolean r3 = r3.mHidden
            if (r3 == 0) goto L_0x010e
            androidx.fragment.app.Fragment r3 = r9.mFragment
            android.view.View r3 = r3.mView
            r6 = 8
            r3.setVisibility(r6)
        L_0x010e:
            androidx.fragment.app.Fragment r3 = r9.mFragment
            android.view.View r3 = r3.mView
            boolean r3 = androidx.core.view.ViewCompat.isAttachedToWindow(r3)
            if (r3 == 0) goto L_0x0120
            androidx.fragment.app.Fragment r3 = r9.mFragment
            android.view.View r3 = r3.mView
            androidx.core.view.ViewCompat.requestApplyInsets(r3)
            goto L_0x012c
        L_0x0120:
            androidx.fragment.app.Fragment r3 = r9.mFragment
            android.view.View r3 = r3.mView
            androidx.fragment.app.FragmentStateManager$1 r6 = new androidx.fragment.app.FragmentStateManager$1
            r6.<init>(r3)
            r3.addOnAttachStateChangeListener(r6)
        L_0x012c:
            androidx.fragment.app.Fragment r3 = r9.mFragment
            r3.performViewCreated()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r3 = r9.mDispatcher
            androidx.fragment.app.Fragment r6 = r9.mFragment
            android.view.View r7 = r6.mView
            androidx.fragment.app.Fragment r8 = r9.mFragment
            android.os.Bundle r8 = r8.mSavedFragmentState
            r3.dispatchOnFragmentViewCreated(r6, r7, r8, r5)
            androidx.fragment.app.Fragment r3 = r9.mFragment
            android.view.View r3 = r3.mView
            int r3 = r3.getVisibility()
            androidx.fragment.app.Fragment r6 = r9.mFragment
            android.view.View r6 = r6.mView
            float r6 = r6.getAlpha()
            boolean r7 = androidx.fragment.app.FragmentManager.USE_STATE_MANAGER
            if (r7 == 0) goto L_0x01a0
            androidx.fragment.app.Fragment r5 = r9.mFragment
            r5.setPostOnViewCreatedAlpha(r6)
            androidx.fragment.app.Fragment r5 = r9.mFragment
            android.view.ViewGroup r5 = r5.mContainer
            if (r5 == 0) goto L_0x01ab
            if (r3 != 0) goto L_0x01ab
            androidx.fragment.app.Fragment r5 = r9.mFragment
            android.view.View r5 = r5.mView
            android.view.View r5 = r5.findFocus()
            if (r5 == 0) goto L_0x0197
            androidx.fragment.app.Fragment r7 = r9.mFragment
            r7.setFocusedView(r5)
            boolean r7 = androidx.fragment.app.FragmentManager.isLoggingEnabled(r4)
            if (r7 == 0) goto L_0x0197
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "requestFocus: Saved focused view "
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r5)
            java.lang.String r8 = " for Fragment "
            java.lang.StringBuilder r7 = r7.append(r8)
            androidx.fragment.app.Fragment r8 = r9.mFragment
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r7 = r7.toString()
            android.util.Log.v(r1, r7)
        L_0x0197:
            androidx.fragment.app.Fragment r1 = r9.mFragment
            android.view.View r1 = r1.mView
            r7 = 0
            r1.setAlpha(r7)
            goto L_0x01ab
        L_0x01a0:
            androidx.fragment.app.Fragment r1 = r9.mFragment
            if (r3 != 0) goto L_0x01a9
            android.view.ViewGroup r7 = r1.mContainer
            if (r7 == 0) goto L_0x01a9
            r5 = 1
        L_0x01a9:
            r1.mIsNewlyAdded = r5
        L_0x01ab:
            androidx.fragment.app.Fragment r1 = r9.mFragment
            r1.mState = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentStateManager.createView():void");
    }

    /* access modifiers changed from: package-private */
    public void destroy() {
        Fragment findActiveFragment;
        if (FragmentManager.isLoggingEnabled(3)) {
            Log.d(TAG, "movefrom CREATED: " + this.mFragment);
        }
        boolean z = this.mFragment.mRemoving && !this.mFragment.isInBackStack();
        if (z || this.mFragmentStore.getNonConfig().shouldDestroy(this.mFragment)) {
            FragmentHostCallback<?> fragmentHostCallback = this.mFragment.mHost;
            boolean isCleared = fragmentHostCallback instanceof ViewModelStoreOwner ? this.mFragmentStore.getNonConfig().isCleared() : fragmentHostCallback.getContext() instanceof Activity ? true ^ ((Activity) fragmentHostCallback.getContext()).isChangingConfigurations() : true;
            if (z || isCleared) {
                this.mFragmentStore.getNonConfig().clearNonConfigState(this.mFragment);
            }
            this.mFragment.performDestroy();
            this.mDispatcher.dispatchOnFragmentDestroyed(this.mFragment, false);
            for (FragmentStateManager next : this.mFragmentStore.getActiveFragmentStateManagers()) {
                if (next != null) {
                    Fragment fragment = next.getFragment();
                    if (this.mFragment.mWho.equals(fragment.mTargetWho)) {
                        fragment.mTarget = this.mFragment;
                        fragment.mTargetWho = null;
                    }
                }
            }
            if (this.mFragment.mTargetWho != null) {
                Fragment fragment2 = this.mFragment;
                fragment2.mTarget = this.mFragmentStore.findActiveFragment(fragment2.mTargetWho);
            }
            this.mFragmentStore.makeInactive(this);
            return;
        }
        if (!(this.mFragment.mTargetWho == null || (findActiveFragment = this.mFragmentStore.findActiveFragment(this.mFragment.mTargetWho)) == null || !findActiveFragment.mRetainInstance)) {
            this.mFragment.mTarget = findActiveFragment;
        }
        this.mFragment.mState = 0;
    }

    /* access modifiers changed from: package-private */
    public void destroyFragmentView() {
        if (FragmentManager.isLoggingEnabled(3)) {
            Log.d(TAG, "movefrom CREATE_VIEW: " + this.mFragment);
        }
        if (!(this.mFragment.mContainer == null || this.mFragment.mView == null)) {
            this.mFragment.mContainer.removeView(this.mFragment.mView);
        }
        this.mFragment.performDestroyView();
        this.mDispatcher.dispatchOnFragmentViewDestroyed(this.mFragment, false);
        this.mFragment.mContainer = null;
        this.mFragment.mView = null;
        this.mFragment.mViewLifecycleOwner = null;
        this.mFragment.mViewLifecycleOwnerLiveData.setValue(null);
        this.mFragment.mInLayout = false;
    }

    /* access modifiers changed from: package-private */
    public void detach() {
        if (FragmentManager.isLoggingEnabled(3)) {
            Log.d(TAG, "movefrom ATTACHED: " + this.mFragment);
        }
        this.mFragment.performDetach();
        boolean z = false;
        this.mDispatcher.dispatchOnFragmentDetached(this.mFragment, false);
        this.mFragment.mState = -1;
        this.mFragment.mHost = null;
        this.mFragment.mParentFragment = null;
        this.mFragment.mFragmentManager = null;
        if (this.mFragment.mRemoving && !this.mFragment.isInBackStack()) {
            z = true;
        }
        if (z || this.mFragmentStore.getNonConfig().shouldDestroy(this.mFragment)) {
            if (FragmentManager.isLoggingEnabled(3)) {
                Log.d(TAG, "initState called for fragment: " + this.mFragment);
            }
            this.mFragment.initState();
        }
    }

    /* access modifiers changed from: package-private */
    public void ensureInflatedView() {
        if (this.mFragment.mFromLayout && this.mFragment.mInLayout && !this.mFragment.mPerformedCreateView) {
            if (FragmentManager.isLoggingEnabled(3)) {
                Log.d(TAG, "moveto CREATE_VIEW: " + this.mFragment);
            }
            Fragment fragment = this.mFragment;
            fragment.performCreateView(fragment.performGetLayoutInflater(fragment.mSavedFragmentState), (ViewGroup) null, this.mFragment.mSavedFragmentState);
            if (this.mFragment.mView != null) {
                this.mFragment.mView.setSaveFromParentEnabled(false);
                this.mFragment.mView.setTag(R.id.fragment_container_view_tag, this.mFragment);
                if (this.mFragment.mHidden) {
                    this.mFragment.mView.setVisibility(8);
                }
                this.mFragment.performViewCreated();
                FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.mDispatcher;
                Fragment fragment2 = this.mFragment;
                fragmentLifecycleCallbacksDispatcher.dispatchOnFragmentViewCreated(fragment2, fragment2.mView, this.mFragment.mSavedFragmentState, false);
                this.mFragment.mState = 2;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Fragment getFragment() {
        return this.mFragment;
    }

    /* access modifiers changed from: package-private */
    public void moveToExpectedState() {
        if (!this.mMovingToState) {
            boolean z = false;
            z = true;
            try {
                while (true) {
                    int computeExpectedState = computeExpectedState();
                    int i = computeExpectedState;
                    if (computeExpectedState != this.mFragment.mState) {
                        if (i <= this.mFragment.mState) {
                            switch (this.mFragment.mState - (z ? 1 : 0)) {
                                case -1:
                                    detach();
                                    break;
                                case 0:
                                    destroy();
                                    break;
                                case 1:
                                    destroyFragmentView();
                                    this.mFragment.mState = z;
                                    break;
                                case 2:
                                    this.mFragment.mInLayout = z;
                                    this.mFragment.mState = 2;
                                    break;
                                case 3:
                                    if (FragmentManager.isLoggingEnabled(3)) {
                                        Log.d(TAG, "movefrom ACTIVITY_CREATED: " + this.mFragment);
                                    }
                                    if (this.mFragment.mView != null && this.mFragment.mSavedViewState == null) {
                                        saveViewState();
                                    }
                                    if (!(this.mFragment.mView == null || this.mFragment.mContainer == null)) {
                                        SpecialEffectsController.getOrCreateController(this.mFragment.mContainer, this.mFragment.getParentFragmentManager()).enqueueRemove(this);
                                    }
                                    this.mFragment.mState = 3;
                                    break;
                                case 4:
                                    stop();
                                    break;
                                case 5:
                                    this.mFragment.mState = 5;
                                    break;
                                case 6:
                                    pause();
                                    break;
                            }
                        } else {
                            switch (this.mFragment.mState + z) {
                                case 0:
                                    attach();
                                    break;
                                case 1:
                                    create();
                                    break;
                                case 2:
                                    ensureInflatedView();
                                    createView();
                                    break;
                                case 3:
                                    activityCreated();
                                    break;
                                case 4:
                                    if (!(this.mFragment.mView == null || this.mFragment.mContainer == null)) {
                                        SpecialEffectsController.getOrCreateController(this.mFragment.mContainer, this.mFragment.getParentFragmentManager()).enqueueAdd(SpecialEffectsController.Operation.State.from(this.mFragment.mView.getVisibility()), this);
                                    }
                                    this.mFragment.mState = 4;
                                    break;
                                case 5:
                                    start();
                                    break;
                                case 6:
                                    this.mFragment.mState = 6;
                                    break;
                                case 7:
                                    resume();
                                    break;
                            }
                        }
                    } else {
                        if (FragmentManager.USE_STATE_MANAGER && this.mFragment.mHiddenChanged) {
                            if (!(this.mFragment.mView == null || this.mFragment.mContainer == null)) {
                                SpecialEffectsController orCreateController = SpecialEffectsController.getOrCreateController(this.mFragment.mContainer, this.mFragment.getParentFragmentManager());
                                if (this.mFragment.mHidden) {
                                    orCreateController.enqueueHide(this);
                                } else {
                                    orCreateController.enqueueShow(this);
                                }
                            }
                            if (this.mFragment.mFragmentManager != null) {
                                this.mFragment.mFragmentManager.invalidateMenuForFragment(this.mFragment);
                            }
                            this.mFragment.mHiddenChanged = z;
                            Fragment fragment = this.mFragment;
                            fragment.onHiddenChanged(fragment.mHidden);
                        }
                        this.mMovingToState = z;
                        return;
                    }
                }
            } finally {
                this.mMovingToState = z;
            }
        } else if (FragmentManager.isLoggingEnabled(2)) {
            Log.v(TAG, "Ignoring re-entrant call to moveToExpectedState() for " + getFragment());
        }
    }

    /* access modifiers changed from: package-private */
    public void pause() {
        if (FragmentManager.isLoggingEnabled(3)) {
            Log.d(TAG, "movefrom RESUMED: " + this.mFragment);
        }
        this.mFragment.performPause();
        this.mDispatcher.dispatchOnFragmentPaused(this.mFragment, false);
    }

    /* access modifiers changed from: package-private */
    public void restoreState(ClassLoader classLoader) {
        if (this.mFragment.mSavedFragmentState != null) {
            this.mFragment.mSavedFragmentState.setClassLoader(classLoader);
            Fragment fragment = this.mFragment;
            fragment.mSavedViewState = fragment.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
            Fragment fragment2 = this.mFragment;
            fragment2.mSavedViewRegistryState = fragment2.mSavedFragmentState.getBundle(VIEW_REGISTRY_STATE_TAG);
            Fragment fragment3 = this.mFragment;
            fragment3.mTargetWho = fragment3.mSavedFragmentState.getString(TARGET_STATE_TAG);
            if (this.mFragment.mTargetWho != null) {
                Fragment fragment4 = this.mFragment;
                fragment4.mTargetRequestCode = fragment4.mSavedFragmentState.getInt(TARGET_REQUEST_CODE_STATE_TAG, 0);
            }
            if (this.mFragment.mSavedUserVisibleHint != null) {
                Fragment fragment5 = this.mFragment;
                fragment5.mUserVisibleHint = fragment5.mSavedUserVisibleHint.booleanValue();
                this.mFragment.mSavedUserVisibleHint = null;
            } else {
                Fragment fragment6 = this.mFragment;
                fragment6.mUserVisibleHint = fragment6.mSavedFragmentState.getBoolean(USER_VISIBLE_HINT_TAG, true);
            }
            if (!this.mFragment.mUserVisibleHint) {
                this.mFragment.mDeferStart = true;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void resume() {
        if (FragmentManager.isLoggingEnabled(3)) {
            Log.d(TAG, "moveto RESUMED: " + this.mFragment);
        }
        View focusedView = this.mFragment.getFocusedView();
        if (focusedView != null && isFragmentViewChild(focusedView)) {
            boolean requestFocus = focusedView.requestFocus();
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v(TAG, "requestFocus: Restoring focused view " + focusedView + " " + (requestFocus ? "succeeded" : "failed") + " on Fragment " + this.mFragment + " resulting in focused view " + this.mFragment.mView.findFocus());
            }
        }
        this.mFragment.setFocusedView((View) null);
        this.mFragment.performResume();
        this.mDispatcher.dispatchOnFragmentResumed(this.mFragment, false);
        this.mFragment.mSavedFragmentState = null;
        this.mFragment.mSavedViewState = null;
        this.mFragment.mSavedViewRegistryState = null;
    }

    /* access modifiers changed from: package-private */
    public Fragment.SavedState saveInstanceState() {
        Bundle saveBasicState;
        if (this.mFragment.mState <= -1 || (saveBasicState = saveBasicState()) == null) {
            return null;
        }
        return new Fragment.SavedState(saveBasicState);
    }

    /* access modifiers changed from: package-private */
    public FragmentState saveState() {
        FragmentState fragmentState = new FragmentState(this.mFragment);
        if (this.mFragment.mState <= -1 || fragmentState.mSavedFragmentState != null) {
            fragmentState.mSavedFragmentState = this.mFragment.mSavedFragmentState;
        } else {
            fragmentState.mSavedFragmentState = saveBasicState();
            if (this.mFragment.mTargetWho != null) {
                if (fragmentState.mSavedFragmentState == null) {
                    fragmentState.mSavedFragmentState = new Bundle();
                }
                fragmentState.mSavedFragmentState.putString(TARGET_STATE_TAG, this.mFragment.mTargetWho);
                if (this.mFragment.mTargetRequestCode != 0) {
                    fragmentState.mSavedFragmentState.putInt(TARGET_REQUEST_CODE_STATE_TAG, this.mFragment.mTargetRequestCode);
                }
            }
        }
        return fragmentState;
    }

    /* access modifiers changed from: package-private */
    public void saveViewState() {
        if (this.mFragment.mView != null) {
            SparseArray<Parcelable> sparseArray = new SparseArray<>();
            this.mFragment.mView.saveHierarchyState(sparseArray);
            if (sparseArray.size() > 0) {
                this.mFragment.mSavedViewState = sparseArray;
            }
            Bundle bundle = new Bundle();
            this.mFragment.mViewLifecycleOwner.performSave(bundle);
            if (!bundle.isEmpty()) {
                this.mFragment.mSavedViewRegistryState = bundle;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setFragmentManagerState(int state) {
        this.mFragmentManagerState = state;
    }

    /* access modifiers changed from: package-private */
    public void start() {
        if (FragmentManager.isLoggingEnabled(3)) {
            Log.d(TAG, "moveto STARTED: " + this.mFragment);
        }
        this.mFragment.performStart();
        this.mDispatcher.dispatchOnFragmentStarted(this.mFragment, false);
    }

    /* access modifiers changed from: package-private */
    public void stop() {
        if (FragmentManager.isLoggingEnabled(3)) {
            Log.d(TAG, "movefrom STARTED: " + this.mFragment);
        }
        this.mFragment.performStop();
        this.mDispatcher.dispatchOnFragmentStopped(this.mFragment, false);
    }
}
