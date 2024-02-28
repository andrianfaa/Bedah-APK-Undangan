package androidx.recyclerview.widget;

import androidx.core.util.Pools;
import androidx.recyclerview.widget.OpReorderer;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mt.Log1F380D;

class AdapterHelper implements OpReorderer.Callback {
    private static final boolean DEBUG = false;
    static final int POSITION_TYPE_INVISIBLE = 0;
    static final int POSITION_TYPE_NEW_OR_LAID_OUT = 1;
    private static final String TAG = "AHT";
    final Callback mCallback;
    final boolean mDisableRecycler;
    private int mExistingUpdateTypes;
    Runnable mOnItemProcessedCallback;
    final OpReorderer mOpReorderer;
    final ArrayList<UpdateOp> mPendingUpdates;
    final ArrayList<UpdateOp> mPostponedList;
    private Pools.Pool<UpdateOp> mUpdateOpPool;

    interface Callback {
        RecyclerView.ViewHolder findViewHolder(int i);

        void markViewHoldersUpdated(int i, int i2, Object obj);

        void offsetPositionsForAdd(int i, int i2);

        void offsetPositionsForMove(int i, int i2);

        void offsetPositionsForRemovingInvisible(int i, int i2);

        void offsetPositionsForRemovingLaidOutOrNewView(int i, int i2);

        void onDispatchFirstPass(UpdateOp updateOp);

        void onDispatchSecondPass(UpdateOp updateOp);
    }

    /* compiled from: 0088 */
    static class UpdateOp {
        static final int ADD = 1;
        static final int MOVE = 8;
        static final int POOL_SIZE = 30;
        static final int REMOVE = 2;
        static final int UPDATE = 4;
        int cmd;
        int itemCount;
        Object payload;
        int positionStart;

        UpdateOp(int cmd2, int positionStart2, int itemCount2, Object payload2) {
            this.cmd = cmd2;
            this.positionStart = positionStart2;
            this.itemCount = itemCount2;
            this.payload = payload2;
        }

        /* access modifiers changed from: package-private */
        public String cmdToString() {
            switch (this.cmd) {
                case 1:
                    return "add";
                case 2:
                    return "rm";
                case 4:
                    return "up";
                case 8:
                    return "mv";
                default:
                    return "??";
            }
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            UpdateOp updateOp = (UpdateOp) o;
            int i = this.cmd;
            if (i != updateOp.cmd) {
                return false;
            }
            if (i == 8 && Math.abs(this.itemCount - this.positionStart) == 1 && this.itemCount == updateOp.positionStart && this.positionStart == updateOp.itemCount) {
                return true;
            }
            if (this.itemCount != updateOp.itemCount || this.positionStart != updateOp.positionStart) {
                return false;
            }
            Object obj = this.payload;
            if (obj != null) {
                if (!obj.equals(updateOp.payload)) {
                    return false;
                }
            } else if (updateOp.payload != null) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (((this.cmd * 31) + this.positionStart) * 31) + this.itemCount;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            String hexString = Integer.toHexString(System.identityHashCode(this));
            Log1F380D.a((Object) hexString);
            return sb.append(hexString).append("[").append(cmdToString()).append(",s:").append(this.positionStart).append("c:").append(this.itemCount).append(",p:").append(this.payload).append("]").toString();
        }
    }

    AdapterHelper(Callback callback) {
        this(callback, false);
    }

    AdapterHelper(Callback callback, boolean disableRecycler) {
        this.mUpdateOpPool = new Pools.SimplePool(30);
        this.mPendingUpdates = new ArrayList<>();
        this.mPostponedList = new ArrayList<>();
        this.mExistingUpdateTypes = 0;
        this.mCallback = callback;
        this.mDisableRecycler = disableRecycler;
        this.mOpReorderer = new OpReorderer(this);
    }

    private void applyAdd(UpdateOp op) {
        postponeAndUpdateViewHolders(op);
    }

    private void applyMove(UpdateOp op) {
        postponeAndUpdateViewHolders(op);
    }

    private void applyRemove(UpdateOp op) {
        int i = op.positionStart;
        int i2 = 0;
        int i3 = op.positionStart + op.itemCount;
        char c = 65535;
        int i4 = op.positionStart;
        while (i4 < i3) {
            boolean z = false;
            if (this.mCallback.findViewHolder(i4) != null || canFindInPreLayout(i4)) {
                if (c == 0) {
                    dispatchAndUpdateViewHolders(obtainUpdateOp(2, i, i2, (Object) null));
                    z = true;
                }
                c = 1;
            } else {
                if (c == 1) {
                    postponeAndUpdateViewHolders(obtainUpdateOp(2, i, i2, (Object) null));
                    z = true;
                }
                c = 0;
            }
            if (z) {
                i4 -= i2;
                i3 -= i2;
                i2 = 1;
            } else {
                i2++;
            }
            i4++;
        }
        if (i2 != op.itemCount) {
            recycleUpdateOp(op);
            op = obtainUpdateOp(2, i, i2, (Object) null);
        }
        if (c == 0) {
            dispatchAndUpdateViewHolders(op);
        } else {
            postponeAndUpdateViewHolders(op);
        }
    }

    private void applyUpdate(UpdateOp op) {
        int i = op.positionStart;
        int i2 = 0;
        int i3 = op.positionStart + op.itemCount;
        char c = 65535;
        for (int i4 = op.positionStart; i4 < i3; i4++) {
            if (this.mCallback.findViewHolder(i4) != null || canFindInPreLayout(i4)) {
                if (c == 0) {
                    dispatchAndUpdateViewHolders(obtainUpdateOp(4, i, i2, op.payload));
                    i2 = 0;
                    i = i4;
                }
                c = 1;
            } else {
                if (c == 1) {
                    postponeAndUpdateViewHolders(obtainUpdateOp(4, i, i2, op.payload));
                    i2 = 0;
                    i = i4;
                }
                c = 0;
            }
            i2++;
        }
        if (i2 != op.itemCount) {
            Object obj = op.payload;
            recycleUpdateOp(op);
            op = obtainUpdateOp(4, i, i2, obj);
        }
        if (c == 0) {
            dispatchAndUpdateViewHolders(op);
        } else {
            postponeAndUpdateViewHolders(op);
        }
    }

    private boolean canFindInPreLayout(int position) {
        int size = this.mPostponedList.size();
        for (int i = 0; i < size; i++) {
            UpdateOp updateOp = this.mPostponedList.get(i);
            if (updateOp.cmd == 8) {
                if (findPositionOffset(updateOp.itemCount, i + 1) == position) {
                    return true;
                }
            } else if (updateOp.cmd == 1) {
                int i2 = updateOp.positionStart + updateOp.itemCount;
                for (int i3 = updateOp.positionStart; i3 < i2; i3++) {
                    if (findPositionOffset(i3, i + 1) == position) {
                        return true;
                    }
                }
                continue;
            } else {
                continue;
            }
        }
        return false;
    }

    private void dispatchAndUpdateViewHolders(UpdateOp op) {
        int i;
        if (op.cmd == 1 || op.cmd == 8) {
            throw new IllegalArgumentException("should not dispatch add or move for pre layout");
        }
        int updatePositionWithPostponed = updatePositionWithPostponed(op.positionStart, op.cmd);
        int i2 = 1;
        int i3 = op.positionStart;
        switch (op.cmd) {
            case 2:
                i = 0;
                break;
            case 4:
                i = 1;
                break;
            default:
                throw new IllegalArgumentException("op should be remove or update." + op);
        }
        for (int i4 = 1; i4 < op.itemCount; i4++) {
            int updatePositionWithPostponed2 = updatePositionWithPostponed(op.positionStart + (i * i4), op.cmd);
            boolean z = false;
            boolean z2 = false;
            switch (op.cmd) {
                case 2:
                    if (updatePositionWithPostponed2 == updatePositionWithPostponed) {
                        z2 = true;
                    }
                    z = z2;
                    break;
                case 4:
                    if (updatePositionWithPostponed2 == updatePositionWithPostponed + 1) {
                        z2 = true;
                    }
                    z = z2;
                    break;
            }
            if (z) {
                i2++;
            } else {
                UpdateOp obtainUpdateOp = obtainUpdateOp(op.cmd, updatePositionWithPostponed, i2, op.payload);
                dispatchFirstPassAndUpdateViewHolders(obtainUpdateOp, i3);
                recycleUpdateOp(obtainUpdateOp);
                if (op.cmd == 4) {
                    i3 += i2;
                }
                updatePositionWithPostponed = updatePositionWithPostponed2;
                i2 = 1;
            }
        }
        Object obj = op.payload;
        recycleUpdateOp(op);
        if (i2 > 0) {
            UpdateOp obtainUpdateOp2 = obtainUpdateOp(op.cmd, updatePositionWithPostponed, i2, obj);
            dispatchFirstPassAndUpdateViewHolders(obtainUpdateOp2, i3);
            recycleUpdateOp(obtainUpdateOp2);
        }
    }

    private void postponeAndUpdateViewHolders(UpdateOp op) {
        this.mPostponedList.add(op);
        switch (op.cmd) {
            case 1:
                this.mCallback.offsetPositionsForAdd(op.positionStart, op.itemCount);
                return;
            case 2:
                this.mCallback.offsetPositionsForRemovingLaidOutOrNewView(op.positionStart, op.itemCount);
                return;
            case 4:
                this.mCallback.markViewHoldersUpdated(op.positionStart, op.itemCount, op.payload);
                return;
            case 8:
                this.mCallback.offsetPositionsForMove(op.positionStart, op.itemCount);
                return;
            default:
                throw new IllegalArgumentException("Unknown update op type for " + op);
        }
    }

    private int updatePositionWithPostponed(int pos, int cmd) {
        int i;
        int i2;
        for (int size = this.mPostponedList.size() - 1; size >= 0; size--) {
            UpdateOp updateOp = this.mPostponedList.get(size);
            if (updateOp.cmd == 8) {
                if (updateOp.positionStart < updateOp.itemCount) {
                    i2 = updateOp.positionStart;
                    i = updateOp.itemCount;
                } else {
                    i2 = updateOp.itemCount;
                    i = updateOp.positionStart;
                }
                if (pos < i2 || pos > i) {
                    if (pos < updateOp.positionStart) {
                        if (cmd == 1) {
                            updateOp.positionStart++;
                            updateOp.itemCount++;
                        } else if (cmd == 2) {
                            updateOp.positionStart--;
                            updateOp.itemCount--;
                        }
                    }
                } else if (i2 == updateOp.positionStart) {
                    if (cmd == 1) {
                        updateOp.itemCount++;
                    } else if (cmd == 2) {
                        updateOp.itemCount--;
                    }
                    pos++;
                } else {
                    if (cmd == 1) {
                        updateOp.positionStart++;
                    } else if (cmd == 2) {
                        updateOp.positionStart--;
                    }
                    pos--;
                }
            } else if (updateOp.positionStart <= pos) {
                if (updateOp.cmd == 1) {
                    pos -= updateOp.itemCount;
                } else if (updateOp.cmd == 2) {
                    pos += updateOp.itemCount;
                }
            } else if (cmd == 1) {
                updateOp.positionStart++;
            } else if (cmd == 2) {
                updateOp.positionStart--;
            }
        }
        for (int size2 = this.mPostponedList.size() - 1; size2 >= 0; size2--) {
            UpdateOp updateOp2 = this.mPostponedList.get(size2);
            if (updateOp2.cmd == 8) {
                if (updateOp2.itemCount == updateOp2.positionStart || updateOp2.itemCount < 0) {
                    this.mPostponedList.remove(size2);
                    recycleUpdateOp(updateOp2);
                }
            } else if (updateOp2.itemCount <= 0) {
                this.mPostponedList.remove(size2);
                recycleUpdateOp(updateOp2);
            }
        }
        return pos;
    }

    /* access modifiers changed from: package-private */
    public AdapterHelper addUpdateOp(UpdateOp... ops) {
        Collections.addAll(this.mPendingUpdates, ops);
        return this;
    }

    public int applyPendingUpdatesToPosition(int position) {
        int size = this.mPendingUpdates.size();
        for (int i = 0; i < size; i++) {
            UpdateOp updateOp = this.mPendingUpdates.get(i);
            switch (updateOp.cmd) {
                case 1:
                    if (updateOp.positionStart > position) {
                        break;
                    } else {
                        position += updateOp.itemCount;
                        break;
                    }
                case 2:
                    if (updateOp.positionStart <= position) {
                        if (updateOp.positionStart + updateOp.itemCount <= position) {
                            position -= updateOp.itemCount;
                            break;
                        } else {
                            return -1;
                        }
                    } else {
                        continue;
                    }
                case 8:
                    if (updateOp.positionStart != position) {
                        if (updateOp.positionStart < position) {
                            position--;
                        }
                        if (updateOp.itemCount > position) {
                            break;
                        } else {
                            position++;
                            break;
                        }
                    } else {
                        position = updateOp.itemCount;
                        break;
                    }
            }
        }
        return position;
    }

    /* access modifiers changed from: package-private */
    public void consumePostponedUpdates() {
        int size = this.mPostponedList.size();
        for (int i = 0; i < size; i++) {
            this.mCallback.onDispatchSecondPass(this.mPostponedList.get(i));
        }
        recycleUpdateOpsAndClearList(this.mPostponedList);
        this.mExistingUpdateTypes = 0;
    }

    /* access modifiers changed from: package-private */
    public void consumeUpdatesInOnePass() {
        consumePostponedUpdates();
        int size = this.mPendingUpdates.size();
        for (int i = 0; i < size; i++) {
            UpdateOp updateOp = this.mPendingUpdates.get(i);
            switch (updateOp.cmd) {
                case 1:
                    this.mCallback.onDispatchSecondPass(updateOp);
                    this.mCallback.offsetPositionsForAdd(updateOp.positionStart, updateOp.itemCount);
                    break;
                case 2:
                    this.mCallback.onDispatchSecondPass(updateOp);
                    this.mCallback.offsetPositionsForRemovingInvisible(updateOp.positionStart, updateOp.itemCount);
                    break;
                case 4:
                    this.mCallback.onDispatchSecondPass(updateOp);
                    this.mCallback.markViewHoldersUpdated(updateOp.positionStart, updateOp.itemCount, updateOp.payload);
                    break;
                case 8:
                    this.mCallback.onDispatchSecondPass(updateOp);
                    this.mCallback.offsetPositionsForMove(updateOp.positionStart, updateOp.itemCount);
                    break;
            }
            Runnable runnable = this.mOnItemProcessedCallback;
            if (runnable != null) {
                runnable.run();
            }
        }
        recycleUpdateOpsAndClearList(this.mPendingUpdates);
        this.mExistingUpdateTypes = 0;
    }

    /* access modifiers changed from: package-private */
    public void dispatchFirstPassAndUpdateViewHolders(UpdateOp op, int offsetStart) {
        this.mCallback.onDispatchFirstPass(op);
        switch (op.cmd) {
            case 2:
                this.mCallback.offsetPositionsForRemovingInvisible(offsetStart, op.itemCount);
                return;
            case 4:
                this.mCallback.markViewHoldersUpdated(offsetStart, op.itemCount, op.payload);
                return;
            default:
                throw new IllegalArgumentException("only remove and update ops can be dispatched in first pass");
        }
    }

    /* access modifiers changed from: package-private */
    public int findPositionOffset(int position) {
        return findPositionOffset(position, 0);
    }

    /* access modifiers changed from: package-private */
    public int findPositionOffset(int position, int firstPostponedItem) {
        int size = this.mPostponedList.size();
        for (int i = firstPostponedItem; i < size; i++) {
            UpdateOp updateOp = this.mPostponedList.get(i);
            if (updateOp.cmd == 8) {
                if (updateOp.positionStart == position) {
                    position = updateOp.itemCount;
                } else {
                    if (updateOp.positionStart < position) {
                        position--;
                    }
                    if (updateOp.itemCount <= position) {
                        position++;
                    }
                }
            } else if (updateOp.positionStart > position) {
                continue;
            } else if (updateOp.cmd == 2) {
                if (position < updateOp.positionStart + updateOp.itemCount) {
                    return -1;
                }
                position -= updateOp.itemCount;
            } else if (updateOp.cmd == 1) {
                position += updateOp.itemCount;
            }
        }
        return position;
    }

    /* access modifiers changed from: package-private */
    public boolean hasAnyUpdateTypes(int updateTypes) {
        return (this.mExistingUpdateTypes & updateTypes) != 0;
    }

    /* access modifiers changed from: package-private */
    public boolean hasPendingUpdates() {
        return this.mPendingUpdates.size() > 0;
    }

    /* access modifiers changed from: package-private */
    public boolean hasUpdates() {
        return !this.mPostponedList.isEmpty() && !this.mPendingUpdates.isEmpty();
    }

    public UpdateOp obtainUpdateOp(int cmd, int positionStart, int itemCount, Object payload) {
        UpdateOp acquire = this.mUpdateOpPool.acquire();
        if (acquire == null) {
            return new UpdateOp(cmd, positionStart, itemCount, payload);
        }
        acquire.cmd = cmd;
        acquire.positionStart = positionStart;
        acquire.itemCount = itemCount;
        acquire.payload = payload;
        return acquire;
    }

    /* access modifiers changed from: package-private */
    public boolean onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        if (itemCount < 1) {
            return false;
        }
        this.mPendingUpdates.add(obtainUpdateOp(4, positionStart, itemCount, payload));
        this.mExistingUpdateTypes |= 4;
        return this.mPendingUpdates.size() == 1;
    }

    /* access modifiers changed from: package-private */
    public boolean onItemRangeInserted(int positionStart, int itemCount) {
        if (itemCount < 1) {
            return false;
        }
        this.mPendingUpdates.add(obtainUpdateOp(1, positionStart, itemCount, (Object) null));
        this.mExistingUpdateTypes |= 1;
        return this.mPendingUpdates.size() == 1;
    }

    /* access modifiers changed from: package-private */
    public boolean onItemRangeMoved(int from, int to, int itemCount) {
        if (from == to) {
            return false;
        }
        if (itemCount == 1) {
            this.mPendingUpdates.add(obtainUpdateOp(8, from, to, (Object) null));
            this.mExistingUpdateTypes |= 8;
            return this.mPendingUpdates.size() == 1;
        }
        throw new IllegalArgumentException("Moving more than 1 item is not supported yet");
    }

    /* access modifiers changed from: package-private */
    public boolean onItemRangeRemoved(int positionStart, int itemCount) {
        if (itemCount < 1) {
            return false;
        }
        this.mPendingUpdates.add(obtainUpdateOp(2, positionStart, itemCount, (Object) null));
        this.mExistingUpdateTypes |= 2;
        return this.mPendingUpdates.size() == 1;
    }

    /* access modifiers changed from: package-private */
    public void preProcess() {
        this.mOpReorderer.reorderOps(this.mPendingUpdates);
        int size = this.mPendingUpdates.size();
        for (int i = 0; i < size; i++) {
            UpdateOp updateOp = this.mPendingUpdates.get(i);
            switch (updateOp.cmd) {
                case 1:
                    applyAdd(updateOp);
                    break;
                case 2:
                    applyRemove(updateOp);
                    break;
                case 4:
                    applyUpdate(updateOp);
                    break;
                case 8:
                    applyMove(updateOp);
                    break;
            }
            Runnable runnable = this.mOnItemProcessedCallback;
            if (runnable != null) {
                runnable.run();
            }
        }
        this.mPendingUpdates.clear();
    }

    public void recycleUpdateOp(UpdateOp op) {
        if (!this.mDisableRecycler) {
            op.payload = null;
            this.mUpdateOpPool.release(op);
        }
    }

    /* access modifiers changed from: package-private */
    public void recycleUpdateOpsAndClearList(List<UpdateOp> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            recycleUpdateOp(list.get(i));
        }
        list.clear();
    }

    /* access modifiers changed from: package-private */
    public void reset() {
        recycleUpdateOpsAndClearList(this.mPendingUpdates);
        recycleUpdateOpsAndClearList(this.mPostponedList);
        this.mExistingUpdateTypes = 0;
    }
}
