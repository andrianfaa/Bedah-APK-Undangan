package androidx.recyclerview.widget;

import androidx.collection.LongSparseArray;
import androidx.collection.SimpleArrayMap;
import androidx.core.util.Pools;
import androidx.recyclerview.widget.RecyclerView;

class ViewInfoStore {
    private static final boolean DEBUG = false;
    final SimpleArrayMap<RecyclerView.ViewHolder, InfoRecord> mLayoutHolderMap = new SimpleArrayMap<>();
    final LongSparseArray<RecyclerView.ViewHolder> mOldChangedHolders = new LongSparseArray<>();

    static class InfoRecord {
        static final int FLAG_APPEAR = 2;
        static final int FLAG_APPEAR_AND_DISAPPEAR = 3;
        static final int FLAG_APPEAR_PRE_AND_POST = 14;
        static final int FLAG_DISAPPEARED = 1;
        static final int FLAG_POST = 8;
        static final int FLAG_PRE = 4;
        static final int FLAG_PRE_AND_POST = 12;
        static Pools.Pool<InfoRecord> sPool = new Pools.SimplePool(20);
        int flags;
        RecyclerView.ItemAnimator.ItemHolderInfo postInfo;
        RecyclerView.ItemAnimator.ItemHolderInfo preInfo;

        private InfoRecord() {
        }

        static void drainCache() {
            do {
            } while (sPool.acquire() != null);
        }

        static InfoRecord obtain() {
            InfoRecord acquire = sPool.acquire();
            return acquire == null ? new InfoRecord() : acquire;
        }

        static void recycle(InfoRecord record) {
            record.flags = 0;
            record.preInfo = null;
            record.postInfo = null;
            sPool.release(record);
        }
    }

    interface ProcessCallback {
        void processAppeared(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo2);

        void processDisappeared(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo2);

        void processPersistent(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo2);

        void unused(RecyclerView.ViewHolder viewHolder);
    }

    ViewInfoStore() {
    }

    private RecyclerView.ItemAnimator.ItemHolderInfo popFromLayoutStep(RecyclerView.ViewHolder vh, int flag) {
        InfoRecord valueAt;
        RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo;
        int indexOfKey = this.mLayoutHolderMap.indexOfKey(vh);
        if (indexOfKey < 0 || (valueAt = this.mLayoutHolderMap.valueAt(indexOfKey)) == null || (valueAt.flags & flag) == 0) {
            return null;
        }
        valueAt.flags &= ~flag;
        if (flag == 4) {
            itemHolderInfo = valueAt.preInfo;
        } else if (flag == 8) {
            itemHolderInfo = valueAt.postInfo;
        } else {
            throw new IllegalArgumentException("Must provide flag PRE or POST");
        }
        if ((valueAt.flags & 12) == 0) {
            this.mLayoutHolderMap.removeAt(indexOfKey);
            InfoRecord.recycle(valueAt);
        }
        return itemHolderInfo;
    }

    /* access modifiers changed from: package-private */
    public void addToAppearedInPreLayoutHolders(RecyclerView.ViewHolder holder, RecyclerView.ItemAnimator.ItemHolderInfo info) {
        InfoRecord infoRecord = this.mLayoutHolderMap.get(holder);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put(holder, infoRecord);
        }
        infoRecord.flags |= 2;
        infoRecord.preInfo = info;
    }

    /* access modifiers changed from: package-private */
    public void addToDisappearedInLayout(RecyclerView.ViewHolder holder) {
        InfoRecord infoRecord = this.mLayoutHolderMap.get(holder);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put(holder, infoRecord);
        }
        infoRecord.flags |= 1;
    }

    /* access modifiers changed from: package-private */
    public void addToOldChangeHolders(long key, RecyclerView.ViewHolder holder) {
        this.mOldChangedHolders.put(key, holder);
    }

    /* access modifiers changed from: package-private */
    public void addToPostLayout(RecyclerView.ViewHolder holder, RecyclerView.ItemAnimator.ItemHolderInfo info) {
        InfoRecord infoRecord = this.mLayoutHolderMap.get(holder);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put(holder, infoRecord);
        }
        infoRecord.postInfo = info;
        infoRecord.flags |= 8;
    }

    /* access modifiers changed from: package-private */
    public void addToPreLayout(RecyclerView.ViewHolder holder, RecyclerView.ItemAnimator.ItemHolderInfo info) {
        InfoRecord infoRecord = this.mLayoutHolderMap.get(holder);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put(holder, infoRecord);
        }
        infoRecord.preInfo = info;
        infoRecord.flags |= 4;
    }

    /* access modifiers changed from: package-private */
    public void clear() {
        this.mLayoutHolderMap.clear();
        this.mOldChangedHolders.clear();
    }

    /* access modifiers changed from: package-private */
    public RecyclerView.ViewHolder getFromOldChangeHolders(long key) {
        return this.mOldChangedHolders.get(key);
    }

    /* access modifiers changed from: package-private */
    public boolean isDisappearing(RecyclerView.ViewHolder holder) {
        InfoRecord infoRecord = this.mLayoutHolderMap.get(holder);
        return (infoRecord == null || (infoRecord.flags & 1) == 0) ? false : true;
    }

    /* access modifiers changed from: package-private */
    public boolean isInPreLayout(RecyclerView.ViewHolder viewHolder) {
        InfoRecord infoRecord = this.mLayoutHolderMap.get(viewHolder);
        return (infoRecord == null || (infoRecord.flags & 4) == 0) ? false : true;
    }

    /* access modifiers changed from: package-private */
    public void onDetach() {
        InfoRecord.drainCache();
    }

    public void onViewDetached(RecyclerView.ViewHolder viewHolder) {
        removeFromDisappearedInLayout(viewHolder);
    }

    /* access modifiers changed from: package-private */
    public RecyclerView.ItemAnimator.ItemHolderInfo popFromPostLayout(RecyclerView.ViewHolder vh) {
        return popFromLayoutStep(vh, 8);
    }

    /* access modifiers changed from: package-private */
    public RecyclerView.ItemAnimator.ItemHolderInfo popFromPreLayout(RecyclerView.ViewHolder vh) {
        return popFromLayoutStep(vh, 4);
    }

    /* access modifiers changed from: package-private */
    public void process(ProcessCallback callback) {
        for (int size = this.mLayoutHolderMap.size() - 1; size >= 0; size--) {
            RecyclerView.ViewHolder keyAt = this.mLayoutHolderMap.keyAt(size);
            InfoRecord removeAt = this.mLayoutHolderMap.removeAt(size);
            if ((removeAt.flags & 3) == 3) {
                callback.unused(keyAt);
            } else if ((removeAt.flags & 1) != 0) {
                if (removeAt.preInfo == null) {
                    callback.unused(keyAt);
                } else {
                    callback.processDisappeared(keyAt, removeAt.preInfo, removeAt.postInfo);
                }
            } else if ((removeAt.flags & 14) == 14) {
                callback.processAppeared(keyAt, removeAt.preInfo, removeAt.postInfo);
            } else if ((removeAt.flags & 12) == 12) {
                callback.processPersistent(keyAt, removeAt.preInfo, removeAt.postInfo);
            } else if ((removeAt.flags & 4) != 0) {
                callback.processDisappeared(keyAt, removeAt.preInfo, (RecyclerView.ItemAnimator.ItemHolderInfo) null);
            } else if ((removeAt.flags & 8) != 0) {
                callback.processAppeared(keyAt, removeAt.preInfo, removeAt.postInfo);
            } else {
                int i = removeAt.flags;
            }
            InfoRecord.recycle(removeAt);
        }
    }

    /* access modifiers changed from: package-private */
    public void removeFromDisappearedInLayout(RecyclerView.ViewHolder holder) {
        InfoRecord infoRecord = this.mLayoutHolderMap.get(holder);
        if (infoRecord != null) {
            infoRecord.flags &= -2;
        }
    }

    /* access modifiers changed from: package-private */
    public void removeViewHolder(RecyclerView.ViewHolder holder) {
        int size = this.mOldChangedHolders.size() - 1;
        while (true) {
            if (size < 0) {
                break;
            } else if (holder == this.mOldChangedHolders.valueAt(size)) {
                this.mOldChangedHolders.removeAt(size);
                break;
            } else {
                size--;
            }
        }
        InfoRecord remove = this.mLayoutHolderMap.remove(holder);
        if (remove != null) {
            InfoRecord.recycle(remove);
        }
    }
}
