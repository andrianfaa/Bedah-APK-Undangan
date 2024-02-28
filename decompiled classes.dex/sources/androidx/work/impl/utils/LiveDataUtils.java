package androidx.work.impl.utils;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;

public class LiveDataUtils {
    private LiveDataUtils() {
    }

    public static <In, Out> LiveData<Out> dedupedMappedLiveDataFor(LiveData<In> liveData, final Function<In, Out> function, final TaskExecutor workTaskExecutor) {
        final Object obj = new Object();
        final MediatorLiveData mediatorLiveData = new MediatorLiveData();
        mediatorLiveData.addSource(liveData, new Observer<In>() {
            Out mCurrentOutput = null;

            public void onChanged(final In in) {
                TaskExecutor.this.executeOnBackgroundThread(new Runnable() {
                    public void run() {
                        synchronized (obj) {
                            Out apply = function.apply(in);
                            if (AnonymousClass1.this.mCurrentOutput == null && apply != null) {
                                AnonymousClass1.this.mCurrentOutput = apply;
                                mediatorLiveData.postValue(apply);
                            } else if (AnonymousClass1.this.mCurrentOutput != null && !AnonymousClass1.this.mCurrentOutput.equals(apply)) {
                                AnonymousClass1.this.mCurrentOutput = apply;
                                mediatorLiveData.postValue(apply);
                            }
                        }
                    }
                });
            }
        });
        return mediatorLiveData;
    }
}
