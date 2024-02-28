package androidx.recyclerview.widget;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.recyclerview.widget.ThreadUtil;
import androidx.recyclerview.widget.TileList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

class MessageThreadUtil<T> implements ThreadUtil<T> {

    static class MessageQueue {
        private SyncQueueItem mRoot;

        MessageQueue() {
        }

        /* access modifiers changed from: package-private */
        public synchronized SyncQueueItem next() {
            SyncQueueItem syncQueueItem = this.mRoot;
            if (syncQueueItem == null) {
                return null;
            }
            SyncQueueItem syncQueueItem2 = syncQueueItem;
            this.mRoot = syncQueueItem.next;
            return syncQueueItem2;
        }

        /* access modifiers changed from: package-private */
        public synchronized void removeMessages(int what) {
            while (true) {
                SyncQueueItem syncQueueItem = this.mRoot;
                if (syncQueueItem == null || syncQueueItem.what != what) {
                    SyncQueueItem syncQueueItem2 = this.mRoot;
                } else {
                    SyncQueueItem syncQueueItem3 = this.mRoot;
                    this.mRoot = syncQueueItem3.next;
                    syncQueueItem3.recycle();
                }
            }
            SyncQueueItem syncQueueItem22 = this.mRoot;
            if (syncQueueItem22 != null) {
                SyncQueueItem syncQueueItem4 = syncQueueItem22.next;
                while (syncQueueItem4 != null) {
                    SyncQueueItem syncQueueItem5 = syncQueueItem4.next;
                    if (syncQueueItem4.what == what) {
                        syncQueueItem22.next = syncQueueItem5;
                        syncQueueItem4.recycle();
                    } else {
                        syncQueueItem22 = syncQueueItem4;
                    }
                    syncQueueItem4 = syncQueueItem5;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public synchronized void sendMessage(SyncQueueItem item) {
            SyncQueueItem syncQueueItem = this.mRoot;
            if (syncQueueItem == null) {
                this.mRoot = item;
                return;
            }
            while (syncQueueItem.next != null) {
                syncQueueItem = syncQueueItem.next;
            }
            syncQueueItem.next = item;
        }

        /* access modifiers changed from: package-private */
        public synchronized void sendMessageAtFrontOfQueue(SyncQueueItem item) {
            item.next = this.mRoot;
            this.mRoot = item;
        }
    }

    static class SyncQueueItem {
        private static SyncQueueItem sPool;
        private static final Object sPoolLock = new Object();
        public int arg1;
        public int arg2;
        public int arg3;
        public int arg4;
        public int arg5;
        public Object data;
        SyncQueueItem next;
        public int what;

        SyncQueueItem() {
        }

        static SyncQueueItem obtainMessage(int what2, int arg12, int arg22) {
            return obtainMessage(what2, arg12, arg22, 0, 0, 0, (Object) null);
        }

        static SyncQueueItem obtainMessage(int what2, int arg12, int arg22, int arg32, int arg42, int arg52, Object data2) {
            SyncQueueItem syncQueueItem;
            synchronized (sPoolLock) {
                SyncQueueItem syncQueueItem2 = sPool;
                if (syncQueueItem2 == null) {
                    syncQueueItem = new SyncQueueItem();
                } else {
                    SyncQueueItem syncQueueItem3 = syncQueueItem2;
                    sPool = syncQueueItem2.next;
                    syncQueueItem3.next = null;
                    syncQueueItem = syncQueueItem3;
                }
                syncQueueItem.what = what2;
                syncQueueItem.arg1 = arg12;
                syncQueueItem.arg2 = arg22;
                syncQueueItem.arg3 = arg32;
                syncQueueItem.arg4 = arg42;
                syncQueueItem.arg5 = arg52;
                syncQueueItem.data = data2;
            }
            return syncQueueItem;
        }

        static SyncQueueItem obtainMessage(int what2, int arg12, Object data2) {
            return obtainMessage(what2, arg12, 0, 0, 0, 0, data2);
        }

        /* access modifiers changed from: package-private */
        public void recycle() {
            this.next = null;
            this.arg5 = 0;
            this.arg4 = 0;
            this.arg3 = 0;
            this.arg2 = 0;
            this.arg1 = 0;
            this.what = 0;
            this.data = null;
            synchronized (sPoolLock) {
                SyncQueueItem syncQueueItem = sPool;
                if (syncQueueItem != null) {
                    this.next = syncQueueItem;
                }
                sPool = this;
            }
        }
    }

    MessageThreadUtil() {
    }

    public ThreadUtil.BackgroundCallback<T> getBackgroundProxy(final ThreadUtil.BackgroundCallback<T> backgroundCallback) {
        return new ThreadUtil.BackgroundCallback<T>() {
            static final int LOAD_TILE = 3;
            static final int RECYCLE_TILE = 4;
            static final int REFRESH = 1;
            static final int UPDATE_RANGE = 2;
            private Runnable mBackgroundRunnable = new Runnable() {
                public void run() {
                    while (true) {
                        SyncQueueItem next = AnonymousClass2.this.mQueue.next();
                        if (next != null) {
                            switch (next.what) {
                                case 1:
                                    AnonymousClass2.this.mQueue.removeMessages(1);
                                    backgroundCallback.refresh(next.arg1);
                                    break;
                                case 2:
                                    AnonymousClass2.this.mQueue.removeMessages(2);
                                    AnonymousClass2.this.mQueue.removeMessages(3);
                                    backgroundCallback.updateRange(next.arg1, next.arg2, next.arg3, next.arg4, next.arg5);
                                    break;
                                case 3:
                                    backgroundCallback.loadTile(next.arg1, next.arg2);
                                    break;
                                case 4:
                                    backgroundCallback.recycleTile((TileList.Tile) next.data);
                                    break;
                                default:
                                    Log.e("ThreadUtil", "Unsupported message, what=" + next.what);
                                    break;
                            }
                        } else {
                            AnonymousClass2.this.mBackgroundRunning.set(false);
                            return;
                        }
                    }
                }
            };
            AtomicBoolean mBackgroundRunning = new AtomicBoolean(false);
            private final Executor mExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
            final MessageQueue mQueue = new MessageQueue();

            private void maybeExecuteBackgroundRunnable() {
                if (this.mBackgroundRunning.compareAndSet(false, true)) {
                    this.mExecutor.execute(this.mBackgroundRunnable);
                }
            }

            private void sendMessage(SyncQueueItem msg) {
                this.mQueue.sendMessage(msg);
                maybeExecuteBackgroundRunnable();
            }

            private void sendMessageAtFrontOfQueue(SyncQueueItem msg) {
                this.mQueue.sendMessageAtFrontOfQueue(msg);
                maybeExecuteBackgroundRunnable();
            }

            public void loadTile(int position, int scrollHint) {
                sendMessage(SyncQueueItem.obtainMessage(3, position, scrollHint));
            }

            public void recycleTile(TileList.Tile<T> tile) {
                sendMessage(SyncQueueItem.obtainMessage(4, 0, (Object) tile));
            }

            public void refresh(int generation) {
                sendMessageAtFrontOfQueue(SyncQueueItem.obtainMessage(1, generation, (Object) null));
            }

            public void updateRange(int rangeStart, int rangeEnd, int extRangeStart, int extRangeEnd, int scrollHint) {
                sendMessageAtFrontOfQueue(SyncQueueItem.obtainMessage(2, rangeStart, rangeEnd, extRangeStart, extRangeEnd, scrollHint, (Object) null));
            }
        };
    }

    public ThreadUtil.MainThreadCallback<T> getMainThreadProxy(final ThreadUtil.MainThreadCallback<T> mainThreadCallback) {
        return new ThreadUtil.MainThreadCallback<T>() {
            static final int ADD_TILE = 2;
            static final int REMOVE_TILE = 3;
            static final int UPDATE_ITEM_COUNT = 1;
            private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
            private Runnable mMainThreadRunnable = new Runnable() {
                public void run() {
                    SyncQueueItem next = AnonymousClass1.this.mQueue.next();
                    while (next != null) {
                        switch (next.what) {
                            case 1:
                                mainThreadCallback.updateItemCount(next.arg1, next.arg2);
                                break;
                            case 2:
                                mainThreadCallback.addTile(next.arg1, (TileList.Tile) next.data);
                                break;
                            case 3:
                                mainThreadCallback.removeTile(next.arg1, next.arg2);
                                break;
                            default:
                                Log.e("ThreadUtil", "Unsupported message, what=" + next.what);
                                break;
                        }
                        next = AnonymousClass1.this.mQueue.next();
                    }
                }
            };
            final MessageQueue mQueue = new MessageQueue();

            private void sendMessage(SyncQueueItem msg) {
                this.mQueue.sendMessage(msg);
                this.mMainThreadHandler.post(this.mMainThreadRunnable);
            }

            public void addTile(int generation, TileList.Tile<T> tile) {
                sendMessage(SyncQueueItem.obtainMessage(2, generation, (Object) tile));
            }

            public void removeTile(int generation, int position) {
                sendMessage(SyncQueueItem.obtainMessage(3, generation, position));
            }

            public void updateItemCount(int generation, int itemCount) {
                sendMessage(SyncQueueItem.obtainMessage(1, generation, itemCount));
            }
        };
    }
}
