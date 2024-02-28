package androidx.work.impl.utils.futures;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Locale;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import mt.Log1F380D;

/* compiled from: 00E2 */
public abstract class AbstractFuture<V> implements ListenableFuture<V> {
    static final AtomicHelper ATOMIC_HELPER;
    static final boolean GENERATE_CANCELLATION_CAUSES;
    private static final Object NULL = new Object();
    private static final long SPIN_THRESHOLD_NANOS = 1000;
    private static final Logger log;
    volatile Listener listeners;
    volatile Object value;
    volatile Waiter waiters;

    private static abstract class AtomicHelper {
        private AtomicHelper() {
        }

        /* access modifiers changed from: package-private */
        public abstract boolean casListeners(AbstractFuture<?> abstractFuture, Listener listener, Listener listener2);

        /* access modifiers changed from: package-private */
        public abstract boolean casValue(AbstractFuture<?> abstractFuture, Object obj, Object obj2);

        /* access modifiers changed from: package-private */
        public abstract boolean casWaiters(AbstractFuture<?> abstractFuture, Waiter waiter, Waiter waiter2);

        /* access modifiers changed from: package-private */
        public abstract void putNext(Waiter waiter, Waiter waiter2);

        /* access modifiers changed from: package-private */
        public abstract void putThread(Waiter waiter, Thread thread);
    }

    private static final class Cancellation {
        static final Cancellation CAUSELESS_CANCELLED;
        static final Cancellation CAUSELESS_INTERRUPTED;
        final Throwable cause;
        final boolean wasInterrupted;

        static {
            if (AbstractFuture.GENERATE_CANCELLATION_CAUSES) {
                CAUSELESS_CANCELLED = null;
                CAUSELESS_INTERRUPTED = null;
                return;
            }
            CAUSELESS_CANCELLED = new Cancellation(false, (Throwable) null);
            CAUSELESS_INTERRUPTED = new Cancellation(true, (Throwable) null);
        }

        Cancellation(boolean wasInterrupted2, Throwable cause2) {
            this.wasInterrupted = wasInterrupted2;
            this.cause = cause2;
        }
    }

    private static final class Failure {
        static final Failure FALLBACK_INSTANCE = new Failure(new Throwable("Failure occurred while trying to finish a future.") {
            public synchronized Throwable fillInStackTrace() {
                return this;
            }
        });
        final Throwable exception;

        Failure(Throwable exception2) {
            this.exception = (Throwable) AbstractFuture.checkNotNull(exception2);
        }
    }

    private static final class Listener {
        static final Listener TOMBSTONE = new Listener((Runnable) null, (Executor) null);
        final Executor executor;
        Listener next;
        final Runnable task;

        Listener(Runnable task2, Executor executor2) {
            this.task = task2;
            this.executor = executor2;
        }
    }

    private static final class SafeAtomicHelper extends AtomicHelper {
        final AtomicReferenceFieldUpdater<AbstractFuture, Listener> listenersUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;
        final AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater;
        final AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Waiter> waitersUpdater;

        SafeAtomicHelper(AtomicReferenceFieldUpdater<Waiter, Thread> atomicReferenceFieldUpdater, AtomicReferenceFieldUpdater<Waiter, Waiter> atomicReferenceFieldUpdater2, AtomicReferenceFieldUpdater<AbstractFuture, Waiter> atomicReferenceFieldUpdater3, AtomicReferenceFieldUpdater<AbstractFuture, Listener> atomicReferenceFieldUpdater4, AtomicReferenceFieldUpdater<AbstractFuture, Object> atomicReferenceFieldUpdater5) {
            super();
            this.waiterThreadUpdater = atomicReferenceFieldUpdater;
            this.waiterNextUpdater = atomicReferenceFieldUpdater2;
            this.waitersUpdater = atomicReferenceFieldUpdater3;
            this.listenersUpdater = atomicReferenceFieldUpdater4;
            this.valueUpdater = atomicReferenceFieldUpdater5;
        }

        /* access modifiers changed from: package-private */
        public boolean casListeners(AbstractFuture<?> abstractFuture, Listener expect, Listener update) {
            return AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(this.listenersUpdater, abstractFuture, expect, update);
        }

        /* access modifiers changed from: package-private */
        public boolean casValue(AbstractFuture<?> abstractFuture, Object expect, Object update) {
            return AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(this.valueUpdater, abstractFuture, expect, update);
        }

        /* access modifiers changed from: package-private */
        public boolean casWaiters(AbstractFuture<?> abstractFuture, Waiter expect, Waiter update) {
            return AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(this.waitersUpdater, abstractFuture, expect, update);
        }

        /* access modifiers changed from: package-private */
        public void putNext(Waiter waiter, Waiter newValue) {
            this.waiterNextUpdater.lazySet(waiter, newValue);
        }

        /* access modifiers changed from: package-private */
        public void putThread(Waiter waiter, Thread newValue) {
            this.waiterThreadUpdater.lazySet(waiter, newValue);
        }
    }

    private static final class SetFuture<V> implements Runnable {
        final ListenableFuture<? extends V> future;
        final AbstractFuture<V> owner;

        SetFuture(AbstractFuture<V> abstractFuture, ListenableFuture<? extends V> listenableFuture) {
            this.owner = abstractFuture;
            this.future = listenableFuture;
        }

        public void run() {
            if (this.owner.value == this) {
                if (AbstractFuture.ATOMIC_HELPER.casValue(this.owner, this, AbstractFuture.getFutureValue(this.future))) {
                    AbstractFuture.complete(this.owner);
                }
            }
        }
    }

    private static final class SynchronizedHelper extends AtomicHelper {
        SynchronizedHelper() {
            super();
        }

        /* access modifiers changed from: package-private */
        public boolean casListeners(AbstractFuture<?> abstractFuture, Listener expect, Listener update) {
            synchronized (abstractFuture) {
                if (abstractFuture.listeners != expect) {
                    return false;
                }
                abstractFuture.listeners = update;
                return true;
            }
        }

        /* access modifiers changed from: package-private */
        public boolean casValue(AbstractFuture<?> abstractFuture, Object expect, Object update) {
            synchronized (abstractFuture) {
                if (abstractFuture.value != expect) {
                    return false;
                }
                abstractFuture.value = update;
                return true;
            }
        }

        /* access modifiers changed from: package-private */
        public boolean casWaiters(AbstractFuture<?> abstractFuture, Waiter expect, Waiter update) {
            synchronized (abstractFuture) {
                if (abstractFuture.waiters != expect) {
                    return false;
                }
                abstractFuture.waiters = update;
                return true;
            }
        }

        /* access modifiers changed from: package-private */
        public void putNext(Waiter waiter, Waiter newValue) {
            waiter.next = newValue;
        }

        /* access modifiers changed from: package-private */
        public void putThread(Waiter waiter, Thread newValue) {
            waiter.thread = newValue;
        }
    }

    private static final class Waiter {
        static final Waiter TOMBSTONE = new Waiter(false);
        volatile Waiter next;
        volatile Thread thread;

        Waiter() {
            AbstractFuture.ATOMIC_HELPER.putThread(this, Thread.currentThread());
        }

        Waiter(boolean unused) {
        }

        /* access modifiers changed from: package-private */
        public void setNext(Waiter next2) {
            AbstractFuture.ATOMIC_HELPER.putNext(this, next2);
        }

        /* access modifiers changed from: package-private */
        public void unpark() {
            Thread thread2 = this.thread;
            if (thread2 != null) {
                this.thread = null;
                LockSupport.unpark(thread2);
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: androidx.work.impl.utils.futures.AbstractFuture$SynchronizedHelper} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: androidx.work.impl.utils.futures.AbstractFuture$SynchronizedHelper} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v8, resolved type: androidx.work.impl.utils.futures.AbstractFuture$SafeAtomicHelper} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: androidx.work.impl.utils.futures.AbstractFuture$SynchronizedHelper} */
    /* JADX WARNING: Multi-variable type inference failed */
    static {
        /*
            java.lang.Class<androidx.work.impl.utils.futures.AbstractFuture> r0 = androidx.work.impl.utils.futures.AbstractFuture.class
            java.lang.String r1 = "guava.concurrent.generate_cancellation_cause"
            java.lang.String r2 = "false"
            java.lang.String r1 = java.lang.System.getProperty(r1, r2)
            mt.Log1F380D.a((java.lang.Object) r1)
            boolean r1 = java.lang.Boolean.parseBoolean(r1)
            GENERATE_CANCELLATION_CAUSES = r1
            java.lang.String r1 = r0.getName()
            java.util.logging.Logger r1 = java.util.logging.Logger.getLogger(r1)
            log = r1
            r1 = 0
            androidx.work.impl.utils.futures.AbstractFuture$SafeAtomicHelper r8 = new androidx.work.impl.utils.futures.AbstractFuture$SafeAtomicHelper     // Catch:{ all -> 0x0055 }
            java.lang.Class<androidx.work.impl.utils.futures.AbstractFuture$Waiter> r2 = androidx.work.impl.utils.futures.AbstractFuture.Waiter.class
            java.lang.Class<java.lang.Thread> r3 = java.lang.Thread.class
            java.lang.String r4 = "thread"
            java.util.concurrent.atomic.AtomicReferenceFieldUpdater r3 = java.util.concurrent.atomic.AtomicReferenceFieldUpdater.newUpdater(r2, r3, r4)     // Catch:{ all -> 0x0055 }
            java.lang.Class<androidx.work.impl.utils.futures.AbstractFuture$Waiter> r2 = androidx.work.impl.utils.futures.AbstractFuture.Waiter.class
            java.lang.Class<androidx.work.impl.utils.futures.AbstractFuture$Waiter> r4 = androidx.work.impl.utils.futures.AbstractFuture.Waiter.class
            java.lang.String r5 = "next"
            java.util.concurrent.atomic.AtomicReferenceFieldUpdater r4 = java.util.concurrent.atomic.AtomicReferenceFieldUpdater.newUpdater(r2, r4, r5)     // Catch:{ all -> 0x0055 }
            java.lang.Class<androidx.work.impl.utils.futures.AbstractFuture$Waiter> r2 = androidx.work.impl.utils.futures.AbstractFuture.Waiter.class
            java.lang.String r5 = "waiters"
            java.util.concurrent.atomic.AtomicReferenceFieldUpdater r5 = java.util.concurrent.atomic.AtomicReferenceFieldUpdater.newUpdater(r0, r2, r5)     // Catch:{ all -> 0x0055 }
            java.lang.Class<androidx.work.impl.utils.futures.AbstractFuture$Listener> r2 = androidx.work.impl.utils.futures.AbstractFuture.Listener.class
            java.lang.String r6 = "listeners"
            java.util.concurrent.atomic.AtomicReferenceFieldUpdater r6 = java.util.concurrent.atomic.AtomicReferenceFieldUpdater.newUpdater(r0, r2, r6)     // Catch:{ all -> 0x0055 }
            java.lang.Class<java.lang.Object> r2 = java.lang.Object.class
            java.lang.String r7 = "value"
            java.util.concurrent.atomic.AtomicReferenceFieldUpdater r7 = java.util.concurrent.atomic.AtomicReferenceFieldUpdater.newUpdater(r0, r2, r7)     // Catch:{ all -> 0x0055 }
            r2 = r8
            r2.<init>(r3, r4, r5, r6, r7)     // Catch:{ all -> 0x0055 }
            r0 = r8
            goto L_0x005d
        L_0x0055:
            r0 = move-exception
            r1 = r0
            androidx.work.impl.utils.futures.AbstractFuture$SynchronizedHelper r2 = new androidx.work.impl.utils.futures.AbstractFuture$SynchronizedHelper
            r2.<init>()
            r0 = r2
        L_0x005d:
            ATOMIC_HELPER = r0
            java.lang.Class<java.util.concurrent.locks.LockSupport> r2 = java.util.concurrent.locks.LockSupport.class
            if (r1 == 0) goto L_0x006c
            java.util.logging.Logger r3 = log
            java.util.logging.Level r4 = java.util.logging.Level.SEVERE
            java.lang.String r5 = "SafeAtomicHelper is broken!"
            r3.log(r4, r5, r1)
        L_0x006c:
            java.lang.Object r0 = new java.lang.Object
            r0.<init>()
            NULL = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.work.impl.utils.futures.AbstractFuture.<clinit>():void");
    }

    protected AbstractFuture() {
    }

    private void addDoneString(StringBuilder builder) {
        try {
            builder.append("SUCCESS, result=[").append(userObjectToString(getUninterruptibly(this))).append("]");
        } catch (ExecutionException e) {
            builder.append("FAILURE, cause=[").append(e.getCause()).append("]");
        } catch (CancellationException e2) {
            builder.append("CANCELLED");
        } catch (RuntimeException e3) {
            builder.append("UNKNOWN, cause=[").append(e3.getClass()).append(" thrown from get()]");
        }
    }

    private static CancellationException cancellationExceptionWithCause(String message, Throwable cause) {
        CancellationException cancellationException = new CancellationException(message);
        cancellationException.initCause(cause);
        return cancellationException;
    }

    static <T> T checkNotNull(T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException();
    }

    private Listener clearListeners(Listener onto) {
        Listener listener;
        do {
            listener = this.listeners;
        } while (!ATOMIC_HELPER.casListeners(this, listener, Listener.TOMBSTONE));
        Listener listener2 = onto;
        while (listener != null) {
            Listener listener3 = listener;
            listener = listener.next;
            listener3.next = listener2;
            listener2 = listener3;
        }
        return listener2;
    }

    static void complete(AbstractFuture<?> abstractFuture) {
        Listener listener = null;
        AbstractFuture<V> abstractFuture2 = abstractFuture;
        while (true) {
            abstractFuture2.releaseWaiters();
            abstractFuture2.afterDone();
            listener = abstractFuture2.clearListeners(listener);
            while (true) {
                if (listener != null) {
                    Listener listener2 = listener;
                    listener = listener.next;
                    Runnable runnable = listener2.task;
                    if (runnable instanceof SetFuture) {
                        SetFuture setFuture = (SetFuture) runnable;
                        AbstractFuture<V> abstractFuture3 = setFuture.owner;
                        if (abstractFuture3.value == setFuture) {
                            if (ATOMIC_HELPER.casValue(abstractFuture3, setFuture, getFutureValue(setFuture.future))) {
                                abstractFuture2 = abstractFuture3;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        executeListener(runnable, listener2.executor);
                    }
                } else {
                    return;
                }
            }
        }
    }

    private static void executeListener(Runnable runnable, Executor executor) {
        try {
            executor.execute(runnable);
        } catch (RuntimeException e) {
            log.log(Level.SEVERE, "RuntimeException while executing runnable " + runnable + " with executor " + executor, e);
        }
    }

    private V getDoneValue(Object obj) throws ExecutionException {
        if (obj instanceof Cancellation) {
            throw cancellationExceptionWithCause("Task was cancelled.", ((Cancellation) obj).cause);
        } else if (obj instanceof Failure) {
            throw new ExecutionException(((Failure) obj).exception);
        } else if (obj == NULL) {
            return null;
        } else {
            return obj;
        }
    }

    static Object getFutureValue(ListenableFuture<?> listenableFuture) {
        if (listenableFuture instanceof AbstractFuture) {
            Object obj = ((AbstractFuture) listenableFuture).value;
            if (!(obj instanceof Cancellation)) {
                return obj;
            }
            Cancellation cancellation = (Cancellation) obj;
            if (!cancellation.wasInterrupted) {
                return obj;
            }
            return cancellation.cause != null ? new Cancellation(false, cancellation.cause) : Cancellation.CAUSELESS_CANCELLED;
        }
        boolean isCancelled = listenableFuture.isCancelled();
        if ((!GENERATE_CANCELLATION_CAUSES) && isCancelled) {
            return Cancellation.CAUSELESS_CANCELLED;
        }
        try {
            Object uninterruptibly = getUninterruptibly(listenableFuture);
            return uninterruptibly == null ? NULL : uninterruptibly;
        } catch (ExecutionException e) {
            return new Failure(e.getCause());
        } catch (CancellationException e2) {
            return !isCancelled ? new Failure(new IllegalArgumentException("get() threw CancellationException, despite reporting isCancelled() == false: " + listenableFuture, e2)) : new Cancellation(false, e2);
        } catch (Throwable th) {
            return new Failure(th);
        }
    }

    private static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
        V v;
        boolean z = false;
        while (true) {
            try {
                v = future.get();
                break;
            } catch (InterruptedException e) {
                z = true;
            } catch (Throwable th) {
                if (z) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (z) {
            Thread.currentThread().interrupt();
        }
        return v;
    }

    private void releaseWaiters() {
        Waiter waiter;
        do {
            waiter = this.waiters;
        } while (!ATOMIC_HELPER.casWaiters(this, waiter, Waiter.TOMBSTONE));
        for (Waiter waiter2 = waiter; waiter2 != null; waiter2 = waiter2.next) {
            waiter2.unpark();
        }
    }

    private void removeWaiter(Waiter node) {
        node.thread = null;
        while (true) {
            Waiter waiter = null;
            Waiter waiter2 = this.waiters;
            if (waiter2 != Waiter.TOMBSTONE) {
                while (waiter2 != null) {
                    Waiter waiter3 = waiter2.next;
                    if (waiter2.thread != null) {
                        waiter = waiter2;
                    } else if (waiter != null) {
                        waiter.next = waiter3;
                        if (waiter.thread == null) {
                        }
                    } else if (!ATOMIC_HELPER.casWaiters(this, waiter2, waiter3)) {
                    }
                    waiter2 = waiter3;
                }
                return;
            }
            return;
        }
    }

    public final void addListener(Runnable listener, Executor executor) {
        checkNotNull(listener);
        checkNotNull(executor);
        Listener listener2 = this.listeners;
        if (listener2 != Listener.TOMBSTONE) {
            Listener listener3 = new Listener(listener, executor);
            do {
                listener3.next = listener2;
                if (!ATOMIC_HELPER.casListeners(this, listener2, listener3)) {
                    listener2 = this.listeners;
                } else {
                    return;
                }
            } while (listener2 != Listener.TOMBSTONE);
        }
        executeListener(listener, executor);
    }

    /* access modifiers changed from: protected */
    public void afterDone() {
    }

    /* JADX WARNING: type inference failed for: r6v6, types: [com.google.common.util.concurrent.ListenableFuture, com.google.common.util.concurrent.ListenableFuture<? extends V>] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean cancel(boolean r11) {
        /*
            r10 = this;
            java.lang.Object r0 = r10.value
            r1 = 0
            r2 = 1
            r3 = 0
            if (r0 != 0) goto L_0x0009
            r4 = r2
            goto L_0x000a
        L_0x0009:
            r4 = r3
        L_0x000a:
            boolean r5 = r0 instanceof androidx.work.impl.utils.futures.AbstractFuture.SetFuture
            r4 = r4 | r5
            if (r4 == 0) goto L_0x0063
            boolean r4 = GENERATE_CANCELLATION_CAUSES
            if (r4 == 0) goto L_0x0020
            androidx.work.impl.utils.futures.AbstractFuture$Cancellation r4 = new androidx.work.impl.utils.futures.AbstractFuture$Cancellation
            java.util.concurrent.CancellationException r5 = new java.util.concurrent.CancellationException
            java.lang.String r6 = "Future.cancel() was called."
            r5.<init>(r6)
            r4.<init>(r11, r5)
            goto L_0x0027
        L_0x0020:
            if (r11 == 0) goto L_0x0025
            androidx.work.impl.utils.futures.AbstractFuture$Cancellation r4 = androidx.work.impl.utils.futures.AbstractFuture.Cancellation.CAUSELESS_INTERRUPTED
            goto L_0x0027
        L_0x0025:
            androidx.work.impl.utils.futures.AbstractFuture$Cancellation r4 = androidx.work.impl.utils.futures.AbstractFuture.Cancellation.CAUSELESS_CANCELLED
        L_0x0027:
            r5 = r10
        L_0x0029:
            androidx.work.impl.utils.futures.AbstractFuture$AtomicHelper r6 = ATOMIC_HELPER
            boolean r6 = r6.casValue(r5, r0, r4)
            if (r6 == 0) goto L_0x005d
            r1 = 1
            if (r11 == 0) goto L_0x0037
            r5.interruptTask()
        L_0x0037:
            complete(r5)
            boolean r6 = r0 instanceof androidx.work.impl.utils.futures.AbstractFuture.SetFuture
            if (r6 == 0) goto L_0x0063
            r6 = r0
            androidx.work.impl.utils.futures.AbstractFuture$SetFuture r6 = (androidx.work.impl.utils.futures.AbstractFuture.SetFuture) r6
            com.google.common.util.concurrent.ListenableFuture<? extends V> r6 = r6.future
            boolean r7 = r6 instanceof androidx.work.impl.utils.futures.AbstractFuture
            if (r7 == 0) goto L_0x0059
            r7 = r6
            androidx.work.impl.utils.futures.AbstractFuture r7 = (androidx.work.impl.utils.futures.AbstractFuture) r7
            java.lang.Object r0 = r7.value
            if (r0 != 0) goto L_0x0050
            r8 = r2
            goto L_0x0051
        L_0x0050:
            r8 = r3
        L_0x0051:
            boolean r9 = r0 instanceof androidx.work.impl.utils.futures.AbstractFuture.SetFuture
            r8 = r8 | r9
            if (r8 == 0) goto L_0x0058
            r5 = r7
            goto L_0x0029
        L_0x0058:
            goto L_0x005c
        L_0x0059:
            r6.cancel(r11)
        L_0x005c:
            goto L_0x0063
        L_0x005d:
            java.lang.Object r0 = r5.value
            boolean r6 = r0 instanceof androidx.work.impl.utils.futures.AbstractFuture.SetFuture
            if (r6 != 0) goto L_0x0029
        L_0x0063:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.work.impl.utils.futures.AbstractFuture.cancel(boolean):boolean");
    }

    public final V get() throws InterruptedException, ExecutionException {
        Object obj;
        if (!Thread.interrupted()) {
            Object obj2 = this.value;
            if ((obj2 != null) && (!(obj2 instanceof SetFuture))) {
                return getDoneValue(obj2);
            }
            Waiter waiter = this.waiters;
            if (waiter != Waiter.TOMBSTONE) {
                Waiter waiter2 = new Waiter();
                do {
                    waiter2.setNext(waiter);
                    if (ATOMIC_HELPER.casWaiters(this, waiter, waiter2)) {
                        do {
                            LockSupport.park(this);
                            if (!Thread.interrupted()) {
                                obj = this.value;
                            } else {
                                removeWaiter(waiter2);
                                throw new InterruptedException();
                            }
                        } while (!((obj != null) & (!(obj instanceof SetFuture))));
                        return getDoneValue(obj);
                    }
                    waiter = this.waiters;
                } while (waiter != Waiter.TOMBSTONE);
            }
            return getDoneValue(this.value);
        }
        throw new InterruptedException();
    }

    public final V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
        long j = timeout;
        TimeUnit timeUnit = unit;
        long nanos = timeUnit.toNanos(j);
        long j2 = nanos;
        if (!Thread.interrupted()) {
            Object obj = this.value;
            if ((obj != null) && (!(obj instanceof SetFuture))) {
                return getDoneValue(obj);
            }
            long nanoTime = j2 > 0 ? System.nanoTime() + j2 : 0;
            if (j2 >= SPIN_THRESHOLD_NANOS) {
                Waiter waiter = this.waiters;
                if (waiter != Waiter.TOMBSTONE) {
                    Waiter waiter2 = new Waiter();
                    while (true) {
                        waiter2.setNext(waiter);
                        if (ATOMIC_HELPER.casWaiters(this, waiter, waiter2)) {
                            while (true) {
                                LockSupport.parkNanos(this, j2);
                                if (!Thread.interrupted()) {
                                    Object obj2 = this.value;
                                    if ((obj2 != null) && (!(obj2 instanceof SetFuture))) {
                                        return getDoneValue(obj2);
                                    }
                                    j2 = nanoTime - System.nanoTime();
                                    if (j2 < SPIN_THRESHOLD_NANOS) {
                                        removeWaiter(waiter2);
                                        break;
                                    }
                                } else {
                                    removeWaiter(waiter2);
                                    throw new InterruptedException();
                                }
                            }
                        } else {
                            waiter = this.waiters;
                            if (waiter == Waiter.TOMBSTONE) {
                                break;
                            }
                        }
                    }
                }
                return getDoneValue(this.value);
            }
            while (j2 > 0) {
                Object obj3 = this.value;
                if ((obj3 != null) && (!(obj3 instanceof SetFuture))) {
                    return getDoneValue(obj3);
                }
                if (!Thread.interrupted()) {
                    j2 = nanoTime - System.nanoTime();
                } else {
                    throw new InterruptedException();
                }
            }
            String abstractFuture = toString();
            String lowerCase = unit.toString().toLowerCase(Locale.ROOT);
            String str = "Waited " + j + " " + unit.toString().toLowerCase(Locale.ROOT);
            if (j2 + SPIN_THRESHOLD_NANOS < 0) {
                String str2 = str + " (plus ";
                long j3 = -j2;
                long j4 = nanos;
                long convert = timeUnit.convert(j3, TimeUnit.NANOSECONDS);
                long j5 = j2;
                long nanos2 = j3 - timeUnit.toNanos(convert);
                boolean z = convert == 0 || nanos2 > SPIN_THRESHOLD_NANOS;
                if (convert > 0) {
                    String str3 = str2 + convert + " " + lowerCase;
                    if (z) {
                        String str4 = str3;
                        str3 = str3 + ",";
                    } else {
                        String str5 = str3;
                    }
                    str2 = str3 + " ";
                }
                if (z) {
                    str2 = str2 + nanos2 + " nanoseconds ";
                }
                str = str2 + "delay)";
            } else {
                long j6 = nanos;
                long j7 = j2;
            }
            if (isDone()) {
                throw new TimeoutException(str + " but future completed as timeout expired");
            }
            throw new TimeoutException(str + " for " + abstractFuture);
        }
        throw new InterruptedException();
    }

    /* access modifiers changed from: protected */
    public void interruptTask() {
    }

    public final boolean isCancelled() {
        return this.value instanceof Cancellation;
    }

    public final boolean isDone() {
        Object obj = this.value;
        return (true ^ (obj instanceof SetFuture)) & (obj != null);
    }

    /* access modifiers changed from: package-private */
    public final void maybePropagateCancellationTo(Future<?> future) {
        if ((future != null) && isCancelled()) {
            future.cancel(wasInterrupted());
        }
    }

    /* access modifiers changed from: protected */
    public String pendingToString() {
        Object obj = this.value;
        if (obj instanceof SetFuture) {
            return "setFuture=[" + userObjectToString(((SetFuture) obj).future) + "]";
        }
        if (this instanceof ScheduledFuture) {
            return "remaining delay=[" + ((ScheduledFuture) this).getDelay(TimeUnit.MILLISECONDS) + " ms]";
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public boolean set(V v) {
        if (!ATOMIC_HELPER.casValue(this, (Object) null, v == null ? NULL : v)) {
            return false;
        }
        complete(this);
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean setException(Throwable throwable) {
        if (!ATOMIC_HELPER.casValue(this, (Object) null, new Failure((Throwable) checkNotNull(throwable)))) {
            return false;
        }
        complete(this);
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean setFuture(ListenableFuture<? extends V> listenableFuture) {
        SetFuture setFuture;
        Failure failure;
        checkNotNull(listenableFuture);
        Object obj = this.value;
        if (obj == null) {
            if (listenableFuture.isDone()) {
                if (!ATOMIC_HELPER.casValue(this, (Object) null, getFutureValue(listenableFuture))) {
                    return false;
                }
                complete(this);
                return true;
            }
            setFuture = new SetFuture(this, listenableFuture);
            if (ATOMIC_HELPER.casValue(this, (Object) null, setFuture)) {
                try {
                    listenableFuture.addListener(setFuture, DirectExecutor.INSTANCE);
                } catch (Throwable th) {
                    failure = Failure.FALLBACK_INSTANCE;
                }
                return true;
            }
            obj = this.value;
        }
        if (obj instanceof Cancellation) {
            listenableFuture.cancel(((Cancellation) obj).wasInterrupted);
        }
        return false;
        ATOMIC_HELPER.casValue(this, setFuture, failure);
        return true;
    }

    public String toString() {
        String str;
        StringBuilder append = new StringBuilder().append(super.toString()).append("[status=");
        if (isCancelled()) {
            append.append("CANCELLED");
        } else if (isDone()) {
            addDoneString(append);
        } else {
            try {
                str = pendingToString();
            } catch (RuntimeException e) {
                str = "Exception thrown from implementation: " + e.getClass();
            }
            if (str != null && !str.isEmpty()) {
                append.append("PENDING, info=[").append(str).append("]");
            } else if (isDone()) {
                addDoneString(append);
            } else {
                append.append("PENDING");
            }
        }
        return append.append("]").toString();
    }

    /* access modifiers changed from: protected */
    public final boolean wasInterrupted() {
        Object obj = this.value;
        return (obj instanceof Cancellation) && ((Cancellation) obj).wasInterrupted;
    }

    private String userObjectToString(Object o) {
        if (o == this) {
            return "this future";
        }
        String valueOf = String.valueOf(o);
        Log1F380D.a((Object) valueOf);
        return valueOf;
    }
}
