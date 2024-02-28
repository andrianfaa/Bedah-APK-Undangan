package androidx.core.provider;

import android.os.Handler;
import android.os.Process;
import androidx.core.util.Consumer;
import androidx.core.util.Preconditions;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class RequestExecutor {

    private static class DefaultThreadFactory implements ThreadFactory {
        private int mPriority;
        private String mThreadName;

        private static class ProcessPriorityThread extends Thread {
            private final int mPriority;

            ProcessPriorityThread(Runnable target, String name, int priority) {
                super(target, name);
                this.mPriority = priority;
            }

            public void run() {
                Process.setThreadPriority(this.mPriority);
                super.run();
            }
        }

        DefaultThreadFactory(String threadName, int priority) {
            this.mThreadName = threadName;
            this.mPriority = priority;
        }

        public Thread newThread(Runnable runnable) {
            return new ProcessPriorityThread(runnable, this.mThreadName, this.mPriority);
        }
    }

    private static class HandlerExecutor implements Executor {
        private final Handler mHandler;

        HandlerExecutor(Handler handler) {
            this.mHandler = (Handler) Preconditions.checkNotNull(handler);
        }

        public void execute(Runnable command) {
            if (!this.mHandler.post((Runnable) Preconditions.checkNotNull(command))) {
                throw new RejectedExecutionException(this.mHandler + " is shutting down");
            }
        }
    }

    private static class ReplyRunnable<T> implements Runnable {
        private Callable<T> mCallable;
        private Consumer<T> mConsumer;
        private Handler mHandler;

        ReplyRunnable(Handler handler, Callable<T> callable, Consumer<T> consumer) {
            this.mCallable = callable;
            this.mConsumer = consumer;
            this.mHandler = handler;
        }

        public void run() {
            T t;
            try {
                t = this.mCallable.call();
            } catch (Exception e) {
                t = null;
            }
            final T t2 = t;
            final Consumer<T> consumer = this.mConsumer;
            this.mHandler.post(new Runnable() {
                public void run() {
                    consumer.accept(t2);
                }
            });
        }
    }

    private RequestExecutor() {
    }

    static ThreadPoolExecutor createDefaultExecutor(String threadName, int threadPriority, int keepAliveTimeInMillis) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, (long) keepAliveTimeInMillis, TimeUnit.MILLISECONDS, new LinkedBlockingDeque(), new DefaultThreadFactory(threadName, threadPriority));
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }

    static Executor createHandlerExecutor(Handler handler) {
        return new HandlerExecutor(handler);
    }

    static <T> void execute(Executor executor, Callable<T> callable, Consumer<T> consumer) {
        executor.execute(new ReplyRunnable(CalleeHandler.create(), callable, consumer));
    }

    static <T> T submit(ExecutorService executor, Callable<T> callable, int timeoutMillis) throws InterruptedException {
        try {
            return executor.submit(callable).get((long) timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e2) {
            throw e2;
        } catch (TimeoutException e3) {
            throw new InterruptedException("timeout");
        }
    }
}
