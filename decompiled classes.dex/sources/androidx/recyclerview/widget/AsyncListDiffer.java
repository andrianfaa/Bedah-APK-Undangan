package androidx.recyclerview.widget;

import android.os.Handler;
import android.os.Looper;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

public class AsyncListDiffer<T> {
    private static final Executor sMainThreadExecutor = new MainThreadExecutor();
    final AsyncDifferConfig<T> mConfig;
    private List<T> mList;
    private final List<ListListener<T>> mListeners;
    Executor mMainThreadExecutor;
    int mMaxScheduledGeneration;
    private List<T> mReadOnlyList;
    private final ListUpdateCallback mUpdateCallback;

    public interface ListListener<T> {
        void onCurrentListChanged(List<T> list, List<T> list2);
    }

    private static class MainThreadExecutor implements Executor {
        final Handler mHandler = new Handler(Looper.getMainLooper());

        MainThreadExecutor() {
        }

        public void execute(Runnable command) {
            this.mHandler.post(command);
        }
    }

    public AsyncListDiffer(ListUpdateCallback listUpdateCallback, AsyncDifferConfig<T> asyncDifferConfig) {
        this.mListeners = new CopyOnWriteArrayList();
        this.mReadOnlyList = Collections.emptyList();
        this.mUpdateCallback = listUpdateCallback;
        this.mConfig = asyncDifferConfig;
        if (asyncDifferConfig.getMainThreadExecutor() != null) {
            this.mMainThreadExecutor = asyncDifferConfig.getMainThreadExecutor();
        } else {
            this.mMainThreadExecutor = sMainThreadExecutor;
        }
    }

    public AsyncListDiffer(RecyclerView.Adapter adapter, DiffUtil.ItemCallback<T> itemCallback) {
        this((ListUpdateCallback) new AdapterListUpdateCallback(adapter), new AsyncDifferConfig.Builder(itemCallback).build());
    }

    private void onCurrentListChanged(List<T> list, Runnable commitCallback) {
        for (ListListener<T> onCurrentListChanged : this.mListeners) {
            onCurrentListChanged.onCurrentListChanged(list, this.mReadOnlyList);
        }
        if (commitCallback != null) {
            commitCallback.run();
        }
    }

    public void addListListener(ListListener<T> listListener) {
        this.mListeners.add(listListener);
    }

    public List<T> getCurrentList() {
        return this.mReadOnlyList;
    }

    /* access modifiers changed from: package-private */
    public void latchList(List<T> list, DiffUtil.DiffResult diffResult, Runnable commitCallback) {
        List<T> list2 = this.mReadOnlyList;
        this.mList = list;
        this.mReadOnlyList = Collections.unmodifiableList(list);
        diffResult.dispatchUpdatesTo(this.mUpdateCallback);
        onCurrentListChanged(list2, commitCallback);
    }

    public void removeListListener(ListListener<T> listListener) {
        this.mListeners.remove(listListener);
    }

    public void submitList(List<T> list) {
        submitList(list, (Runnable) null);
    }

    public void submitList(List<T> list, Runnable commitCallback) {
        final int i = this.mMaxScheduledGeneration + 1;
        this.mMaxScheduledGeneration = i;
        List<T> list2 = this.mList;
        if (list != list2) {
            List<T> list3 = this.mReadOnlyList;
            if (list == null) {
                int size = list2.size();
                this.mList = null;
                this.mReadOnlyList = Collections.emptyList();
                this.mUpdateCallback.onRemoved(0, size);
                onCurrentListChanged(list3, commitCallback);
            } else if (list2 == null) {
                this.mList = list;
                this.mReadOnlyList = Collections.unmodifiableList(list);
                this.mUpdateCallback.onInserted(0, list.size());
                onCurrentListChanged(list3, commitCallback);
            } else {
                final List<T> list4 = this.mList;
                final List<T> list5 = list;
                final Runnable runnable = commitCallback;
                this.mConfig.getBackgroundThreadExecutor().execute(new Runnable() {
                    public void run() {
                        final DiffUtil.DiffResult calculateDiff = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                                Object obj = list4.get(oldItemPosition);
                                Object obj2 = list5.get(newItemPosition);
                                if (obj != null && obj2 != null) {
                                    return AsyncListDiffer.this.mConfig.getDiffCallback().areContentsTheSame(obj, obj2);
                                }
                                if (obj == null && obj2 == null) {
                                    return true;
                                }
                                throw new AssertionError();
                            }

                            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                                Object obj = list4.get(oldItemPosition);
                                Object obj2 = list5.get(newItemPosition);
                                return (obj == null || obj2 == null) ? obj == null && obj2 == null : AsyncListDiffer.this.mConfig.getDiffCallback().areItemsTheSame(obj, obj2);
                            }

                            public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                                Object obj = list4.get(oldItemPosition);
                                Object obj2 = list5.get(newItemPosition);
                                if (obj != null && obj2 != null) {
                                    return AsyncListDiffer.this.mConfig.getDiffCallback().getChangePayload(obj, obj2);
                                }
                                throw new AssertionError();
                            }

                            public int getNewListSize() {
                                return list5.size();
                            }

                            public int getOldListSize() {
                                return list4.size();
                            }
                        });
                        AsyncListDiffer.this.mMainThreadExecutor.execute(new Runnable() {
                            public void run() {
                                if (AsyncListDiffer.this.mMaxScheduledGeneration == i) {
                                    AsyncListDiffer.this.latchList(list5, calculateDiff, runnable);
                                }
                            }
                        });
                    }
                });
            }
        } else if (commitCallback != null) {
            commitCallback.run();
        }
    }
}
