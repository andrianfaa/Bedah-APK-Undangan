package androidx.viewpager2.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import androidx.collection.ArraySet;
import androidx.collection.LongSparseArray;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import mt.Log1F380D;

/* compiled from: 009F */
public abstract class FragmentStateAdapter extends RecyclerView.Adapter<FragmentViewHolder> implements StatefulAdapter {
    private static final long GRACE_WINDOW_TIME_MS = 10000;
    private static final String KEY_PREFIX_FRAGMENT = "f#";
    private static final String KEY_PREFIX_STATE = "s#";
    final FragmentManager mFragmentManager;
    private FragmentMaxLifecycleEnforcer mFragmentMaxLifecycleEnforcer;
    final LongSparseArray<Fragment> mFragments;
    private boolean mHasStaleFragments;
    boolean mIsInGracePeriod;
    private final LongSparseArray<Integer> mItemIdToViewHolder;
    final Lifecycle mLifecycle;
    private final LongSparseArray<Fragment.SavedState> mSavedStates;

    private static abstract class DataSetChangeObserver extends RecyclerView.AdapterDataObserver {
        private DataSetChangeObserver() {
        }

        public abstract void onChanged();

        public final void onItemRangeChanged(int positionStart, int itemCount) {
            onChanged();
        }

        public final void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            onChanged();
        }

        public final void onItemRangeInserted(int positionStart, int itemCount) {
            onChanged();
        }

        public final void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            onChanged();
        }

        public final void onItemRangeRemoved(int positionStart, int itemCount) {
            onChanged();
        }
    }

    class FragmentMaxLifecycleEnforcer {
        private RecyclerView.AdapterDataObserver mDataObserver;
        private LifecycleEventObserver mLifecycleObserver;
        private ViewPager2.OnPageChangeCallback mPageChangeCallback;
        private long mPrimaryItemId = -1;
        private ViewPager2 mViewPager;

        FragmentMaxLifecycleEnforcer() {
        }

        private ViewPager2 inferViewPager(RecyclerView recyclerView) {
            ViewParent parent = recyclerView.getParent();
            if (parent instanceof ViewPager2) {
                return (ViewPager2) parent;
            }
            throw new IllegalStateException("Expected ViewPager2 instance. Got: " + parent);
        }

        /* access modifiers changed from: package-private */
        public void register(RecyclerView recyclerView) {
            this.mViewPager = inferViewPager(recyclerView);
            AnonymousClass1 r0 = new ViewPager2.OnPageChangeCallback() {
                public void onPageScrollStateChanged(int state) {
                    FragmentMaxLifecycleEnforcer.this.updateFragmentMaxLifecycle(false);
                }

                public void onPageSelected(int position) {
                    FragmentMaxLifecycleEnforcer.this.updateFragmentMaxLifecycle(false);
                }
            };
            this.mPageChangeCallback = r0;
            this.mViewPager.registerOnPageChangeCallback(r0);
            AnonymousClass2 r02 = new DataSetChangeObserver() {
                public void onChanged() {
                    FragmentMaxLifecycleEnforcer.this.updateFragmentMaxLifecycle(true);
                }
            };
            this.mDataObserver = r02;
            FragmentStateAdapter.this.registerAdapterDataObserver(r02);
            this.mLifecycleObserver = new LifecycleEventObserver() {
                public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                    FragmentMaxLifecycleEnforcer.this.updateFragmentMaxLifecycle(false);
                }
            };
            FragmentStateAdapter.this.mLifecycle.addObserver(this.mLifecycleObserver);
        }

        /* access modifiers changed from: package-private */
        public void unregister(RecyclerView recyclerView) {
            inferViewPager(recyclerView).unregisterOnPageChangeCallback(this.mPageChangeCallback);
            FragmentStateAdapter.this.unregisterAdapterDataObserver(this.mDataObserver);
            FragmentStateAdapter.this.mLifecycle.removeObserver(this.mLifecycleObserver);
            this.mViewPager = null;
        }

        /* access modifiers changed from: package-private */
        public void updateFragmentMaxLifecycle(boolean dataSetChanged) {
            int currentItem;
            Fragment fragment;
            if (!FragmentStateAdapter.this.shouldDelayFragmentTransactions() && this.mViewPager.getScrollState() == 0 && !FragmentStateAdapter.this.mFragments.isEmpty() && FragmentStateAdapter.this.getItemCount() != 0 && (currentItem = this.mViewPager.getCurrentItem()) < FragmentStateAdapter.this.getItemCount()) {
                long itemId = FragmentStateAdapter.this.getItemId(currentItem);
                if ((itemId != this.mPrimaryItemId || dataSetChanged) && (fragment = FragmentStateAdapter.this.mFragments.get(itemId)) != null && fragment.isAdded()) {
                    this.mPrimaryItemId = itemId;
                    FragmentTransaction beginTransaction = FragmentStateAdapter.this.mFragmentManager.beginTransaction();
                    Fragment fragment2 = null;
                    for (int i = 0; i < FragmentStateAdapter.this.mFragments.size(); i++) {
                        long keyAt = FragmentStateAdapter.this.mFragments.keyAt(i);
                        Fragment valueAt = FragmentStateAdapter.this.mFragments.valueAt(i);
                        if (valueAt.isAdded()) {
                            if (keyAt != this.mPrimaryItemId) {
                                beginTransaction.setMaxLifecycle(valueAt, Lifecycle.State.STARTED);
                            } else {
                                fragment2 = valueAt;
                            }
                            valueAt.setMenuVisibility(keyAt == this.mPrimaryItemId);
                        }
                    }
                    if (fragment2 != null) {
                        beginTransaction.setMaxLifecycle(fragment2, Lifecycle.State.RESUMED);
                    }
                    if (!beginTransaction.isEmpty()) {
                        beginTransaction.commitNow();
                    }
                }
            }
        }
    }

    public FragmentStateAdapter(Fragment fragment) {
        this(fragment.getChildFragmentManager(), fragment.getLifecycle());
    }

    public FragmentStateAdapter(FragmentActivity fragmentActivity) {
        this(fragmentActivity.getSupportFragmentManager(), fragmentActivity.getLifecycle());
    }

    public FragmentStateAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        this.mFragments = new LongSparseArray<>();
        this.mSavedStates = new LongSparseArray<>();
        this.mItemIdToViewHolder = new LongSparseArray<>();
        this.mIsInGracePeriod = false;
        this.mHasStaleFragments = false;
        this.mFragmentManager = fragmentManager;
        this.mLifecycle = lifecycle;
        super.setHasStableIds(true);
    }

    private static String createKey(String prefix, long id) {
        return prefix + id;
    }

    private void ensureFragment(int position) {
        long itemId = getItemId(position);
        if (!this.mFragments.containsKey(itemId)) {
            Fragment createFragment = createFragment(position);
            createFragment.setInitialSavedState(this.mSavedStates.get(itemId));
            this.mFragments.put(itemId, createFragment);
        }
    }

    private boolean isFragmentViewBound(long itemId) {
        View view;
        if (this.mItemIdToViewHolder.containsKey(itemId)) {
            return true;
        }
        Fragment fragment = this.mFragments.get(itemId);
        if (fragment == null || (view = fragment.getView()) == null) {
            return false;
        }
        return view.getParent() != null;
    }

    private static boolean isValidKey(String key, String prefix) {
        return key.startsWith(prefix) && key.length() > prefix.length();
    }

    private Long itemForViewHolder(int viewHolderId) {
        Long l = null;
        for (int i = 0; i < this.mItemIdToViewHolder.size(); i++) {
            if (this.mItemIdToViewHolder.valueAt(i).intValue() == viewHolderId) {
                if (l == null) {
                    l = Long.valueOf(this.mItemIdToViewHolder.keyAt(i));
                } else {
                    throw new IllegalStateException("Design assumption violated: a ViewHolder can only be bound to one item at a time.");
                }
            }
        }
        return l;
    }

    private static long parseIdFromKey(String key, String prefix) {
        return Long.parseLong(key.substring(prefix.length()));
    }

    private void removeFragment(long itemId) {
        ViewParent parent;
        Fragment fragment = this.mFragments.get(itemId);
        if (fragment != null) {
            if (!(fragment.getView() == null || (parent = fragment.getView().getParent()) == null)) {
                ((FrameLayout) parent).removeAllViews();
            }
            if (!containsItem(itemId)) {
                this.mSavedStates.remove(itemId);
            }
            if (!fragment.isAdded()) {
                this.mFragments.remove(itemId);
            } else if (shouldDelayFragmentTransactions()) {
                this.mHasStaleFragments = true;
            } else {
                if (fragment.isAdded() && containsItem(itemId)) {
                    this.mSavedStates.put(itemId, this.mFragmentManager.saveFragmentInstanceState(fragment));
                }
                this.mFragmentManager.beginTransaction().remove(fragment).commitNow();
                this.mFragments.remove(itemId);
            }
        }
    }

    private void scheduleGracePeriodEnd() {
        final Handler handler = new Handler(Looper.getMainLooper());
        final AnonymousClass4 r1 = new Runnable() {
            public void run() {
                FragmentStateAdapter.this.mIsInGracePeriod = false;
                FragmentStateAdapter.this.gcFragments();
            }
        };
        this.mLifecycle.addObserver(new LifecycleEventObserver() {
            public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    handler.removeCallbacks(r1);
                    source.getLifecycle().removeObserver(this);
                }
            }
        });
        handler.postDelayed(r1, 10000);
    }

    private void scheduleViewAttach(final Fragment fragment, final FrameLayout container) {
        this.mFragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
                if (f == fragment) {
                    fm.unregisterFragmentLifecycleCallbacks(this);
                    FragmentStateAdapter.this.addViewToContainer(v, container);
                }
            }
        }, false);
    }

    /* access modifiers changed from: package-private */
    public void addViewToContainer(View v, FrameLayout container) {
        if (container.getChildCount() > 1) {
            throw new IllegalStateException("Design assumption violated.");
        } else if (v.getParent() != container) {
            if (container.getChildCount() > 0) {
                container.removeAllViews();
            }
            if (v.getParent() != null) {
                ((ViewGroup) v.getParent()).removeView(v);
            }
            container.addView(v);
        }
    }

    public boolean containsItem(long itemId) {
        return itemId >= 0 && itemId < ((long) getItemCount());
    }

    public abstract Fragment createFragment(int i);

    /* access modifiers changed from: package-private */
    public void gcFragments() {
        if (this.mHasStaleFragments && !shouldDelayFragmentTransactions()) {
            ArraySet<Long> arraySet = new ArraySet<>();
            for (int i = 0; i < this.mFragments.size(); i++) {
                long keyAt = this.mFragments.keyAt(i);
                if (!containsItem(keyAt)) {
                    arraySet.add(Long.valueOf(keyAt));
                    this.mItemIdToViewHolder.remove(keyAt);
                }
            }
            if (!this.mIsInGracePeriod) {
                this.mHasStaleFragments = false;
                for (int i2 = 0; i2 < this.mFragments.size(); i2++) {
                    long keyAt2 = this.mFragments.keyAt(i2);
                    if (!isFragmentViewBound(keyAt2)) {
                        arraySet.add(Long.valueOf(keyAt2));
                    }
                }
            }
            for (Long longValue : arraySet) {
                removeFragment(longValue.longValue());
            }
        }
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        Preconditions.checkArgument(this.mFragmentMaxLifecycleEnforcer == null);
        FragmentMaxLifecycleEnforcer fragmentMaxLifecycleEnforcer = new FragmentMaxLifecycleEnforcer();
        this.mFragmentMaxLifecycleEnforcer = fragmentMaxLifecycleEnforcer;
        fragmentMaxLifecycleEnforcer.register(recyclerView);
    }

    public final void onBindViewHolder(final FragmentViewHolder holder, int position) {
        long itemId = holder.getItemId();
        int id = holder.getContainer().getId();
        Long itemForViewHolder = itemForViewHolder(id);
        if (!(itemForViewHolder == null || itemForViewHolder.longValue() == itemId)) {
            removeFragment(itemForViewHolder.longValue());
            this.mItemIdToViewHolder.remove(itemForViewHolder.longValue());
        }
        this.mItemIdToViewHolder.put(itemId, Integer.valueOf(id));
        ensureFragment(position);
        final FrameLayout container = holder.getContainer();
        if (ViewCompat.isAttachedToWindow(container)) {
            if (container.getParent() == null) {
                container.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        if (container.getParent() != null) {
                            container.removeOnLayoutChangeListener(this);
                            FragmentStateAdapter.this.placeFragmentInViewHolder(holder);
                        }
                    }
                });
            } else {
                throw new IllegalStateException("Design assumption violated.");
            }
        }
        gcFragments();
    }

    public final FragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return FragmentViewHolder.create(parent);
    }

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.mFragmentMaxLifecycleEnforcer.unregister(recyclerView);
        this.mFragmentMaxLifecycleEnforcer = null;
    }

    public final boolean onFailedToRecycleView(FragmentViewHolder holder) {
        return true;
    }

    public final void onViewAttachedToWindow(FragmentViewHolder holder) {
        placeFragmentInViewHolder(holder);
        gcFragments();
    }

    public final void onViewRecycled(FragmentViewHolder holder) {
        Long itemForViewHolder = itemForViewHolder(holder.getContainer().getId());
        if (itemForViewHolder != null) {
            removeFragment(itemForViewHolder.longValue());
            this.mItemIdToViewHolder.remove(itemForViewHolder.longValue());
        }
    }

    /* access modifiers changed from: package-private */
    public void placeFragmentInViewHolder(final FragmentViewHolder holder) {
        Fragment fragment = this.mFragments.get(holder.getItemId());
        if (fragment != null) {
            FrameLayout container = holder.getContainer();
            View view = fragment.getView();
            if (!fragment.isAdded() && view != null) {
                throw new IllegalStateException("Design assumption violated.");
            } else if (fragment.isAdded() && view == null) {
                scheduleViewAttach(fragment, container);
            } else if (!fragment.isAdded() || view.getParent() == null) {
                if (fragment.isAdded()) {
                    addViewToContainer(view, container);
                } else if (!shouldDelayFragmentTransactions()) {
                    scheduleViewAttach(fragment, container);
                    this.mFragmentManager.beginTransaction().add(fragment, "f" + holder.getItemId()).setMaxLifecycle(fragment, Lifecycle.State.STARTED).commitNow();
                    this.mFragmentMaxLifecycleEnforcer.updateFragmentMaxLifecycle(false);
                } else if (!this.mFragmentManager.isDestroyed()) {
                    this.mLifecycle.addObserver(new LifecycleEventObserver() {
                        public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                            if (!FragmentStateAdapter.this.shouldDelayFragmentTransactions()) {
                                source.getLifecycle().removeObserver(this);
                                if (ViewCompat.isAttachedToWindow(holder.getContainer())) {
                                    FragmentStateAdapter.this.placeFragmentInViewHolder(holder);
                                }
                            }
                        }
                    });
                }
            } else if (view.getParent() != container) {
                addViewToContainer(view, container);
            }
        } else {
            throw new IllegalStateException("Design assumption violated.");
        }
    }

    public final void restoreState(Parcelable savedState) {
        if (!this.mSavedStates.isEmpty() || !this.mFragments.isEmpty()) {
            throw new IllegalStateException("Expected the adapter to be 'fresh' while restoring state.");
        }
        Bundle bundle = (Bundle) savedState;
        if (bundle.getClassLoader() == null) {
            bundle.setClassLoader(getClass().getClassLoader());
        }
        for (String str : bundle.keySet()) {
            if (isValidKey(str, KEY_PREFIX_FRAGMENT)) {
                this.mFragments.put(parseIdFromKey(str, KEY_PREFIX_FRAGMENT), this.mFragmentManager.getFragment(bundle, str));
            } else if (isValidKey(str, KEY_PREFIX_STATE)) {
                long parseIdFromKey = parseIdFromKey(str, KEY_PREFIX_STATE);
                Fragment.SavedState savedState2 = (Fragment.SavedState) bundle.getParcelable(str);
                if (containsItem(parseIdFromKey)) {
                    this.mSavedStates.put(parseIdFromKey, savedState2);
                }
            } else {
                throw new IllegalArgumentException("Unexpected key in savedState: " + str);
            }
        }
        if (!this.mFragments.isEmpty()) {
            this.mHasStaleFragments = true;
            this.mIsInGracePeriod = true;
            gcFragments();
            scheduleGracePeriodEnd();
        }
    }

    public final Parcelable saveState() {
        Bundle bundle = new Bundle(this.mFragments.size() + this.mSavedStates.size());
        for (int i = 0; i < this.mFragments.size(); i++) {
            long keyAt = this.mFragments.keyAt(i);
            Fragment fragment = this.mFragments.get(keyAt);
            if (fragment != null && fragment.isAdded()) {
                String createKey = createKey(KEY_PREFIX_FRAGMENT, keyAt);
                Log1F380D.a((Object) createKey);
                this.mFragmentManager.putFragment(bundle, createKey, fragment);
            }
        }
        for (int i2 = 0; i2 < this.mSavedStates.size(); i2++) {
            long keyAt2 = this.mSavedStates.keyAt(i2);
            if (containsItem(keyAt2)) {
                String createKey2 = createKey(KEY_PREFIX_STATE, keyAt2);
                Log1F380D.a((Object) createKey2);
                bundle.putParcelable(createKey2, this.mSavedStates.get(keyAt2));
            }
        }
        return bundle;
    }

    public final void setHasStableIds(boolean hasStableIds) {
        throw new UnsupportedOperationException("Stable Ids are required for the adapter to function properly, and the adapter takes care of setting the flag.");
    }

    /* access modifiers changed from: package-private */
    public boolean shouldDelayFragmentTransactions() {
        return this.mFragmentManager.isStateSaved();
    }
}
