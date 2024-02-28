package okhttp3.internal.concurrent;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.time.DurationKt;
import mt.Log1F380D;
import okhttp3.internal.http2.Http2Connection;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u000e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003\u001a \u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0001H\u0002\u001a5\u0010\u000b\u001a\u0002H\f\"\u0004\b\u0000\u0010\f2\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\f\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\f0\u000eH\bø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a*\u0010\u0010\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u000eH\bø\u0001\u0000\u0002\u0007\n\u0005\b20\u0001¨\u0006\u0012"}, d2 = {"formatDuration", "", "ns", "", "log", "", "task", "Lokhttp3/internal/concurrent/Task;", "queue", "Lokhttp3/internal/concurrent/TaskQueue;", "message", "logElapsed", "T", "block", "Lkotlin/Function0;", "(Lokhttp3/internal/concurrent/Task;Lokhttp3/internal/concurrent/TaskQueue;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "taskLog", "messageBlock", "okhttp"}, k = 2, mv = {1, 4, 0})
/* compiled from: 01C7 */
public final class TaskLoggerKt {
    public static final String formatDuration(long ns) {
        String str = ns <= ((long) -999500000) ? ((ns - ((long) 500000000)) / ((long) Http2Connection.DEGRADED_PONG_TIMEOUT_NS)) + " s " : ns <= ((long) -999500) ? ((ns - ((long) 500000)) / ((long) DurationKt.NANOS_IN_MILLIS)) + " ms" : ns <= 0 ? ((ns - ((long) 500)) / ((long) 1000)) + " µs" : ns < ((long) 999500) ? ((((long) 500) + ns) / ((long) 1000)) + " µs" : ns < ((long) 999500000) ? ((((long) 500000) + ns) / ((long) DurationKt.NANOS_IN_MILLIS)) + " ms" : ((((long) 500000000) + ns) / ((long) Http2Connection.DEGRADED_PONG_TIMEOUT_NS)) + " s ";
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        String format = String.format("%6s", Arrays.copyOf(new Object[]{str}, 1));
        Log1F380D.a((Object) format);
        Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
        return format;
    }

    public static final void taskLog(Task task, TaskQueue queue, Function0<String> messageBlock) {
        Intrinsics.checkNotNullParameter(task, "task");
        Intrinsics.checkNotNullParameter(queue, "queue");
        Intrinsics.checkNotNullParameter(messageBlock, "messageBlock");
        if (TaskRunner.Companion.getLogger().isLoggable(Level.FINE)) {
            log(task, queue, messageBlock.invoke());
        }
    }

    /* access modifiers changed from: private */
    public static final void log(Task task, TaskQueue queue, String message) {
        Logger logger = TaskRunner.Companion.getLogger();
        StringBuilder append = new StringBuilder().append(queue.getName$okhttp()).append(' ');
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        String format = String.format("%-22s", Arrays.copyOf(new Object[]{message}, 1));
        Log1F380D.a((Object) format);
        Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
        logger.fine(append.append(format).append(": ").append(task.getName()).toString());
    }

    public static final <T> T logElapsed(Task task, TaskQueue queue, Function0<? extends T> block) {
        Intrinsics.checkNotNullParameter(task, "task");
        Intrinsics.checkNotNullParameter(queue, "queue");
        Intrinsics.checkNotNullParameter(block, "block");
        long j = -1;
        boolean isLoggable = TaskRunner.Companion.getLogger().isLoggable(Level.FINE);
        if (isLoggable) {
            j = queue.getTaskRunner$okhttp().getBackend().nanoTime();
            log(task, queue, "starting");
        }
        try {
            T invoke = block.invoke();
            InlineMarker.finallyStart(1);
            if (isLoggable) {
                StringBuilder append = new StringBuilder().append("finished run in ");
                String formatDuration = formatDuration(queue.getTaskRunner$okhttp().getBackend().nanoTime() - j);
                Log1F380D.a((Object) formatDuration);
                log(task, queue, append.append(formatDuration).toString());
            }
            InlineMarker.finallyEnd(1);
            return invoke;
        } catch (Throwable th) {
            InlineMarker.finallyStart(1);
            if (isLoggable) {
                long nanoTime = queue.getTaskRunner$okhttp().getBackend().nanoTime() - j;
                if (0 != 0) {
                    StringBuilder append2 = new StringBuilder().append("finished run in ");
                    String formatDuration2 = formatDuration(nanoTime);
                    Log1F380D.a((Object) formatDuration2);
                    log(task, queue, append2.append(formatDuration2).toString());
                } else {
                    StringBuilder append3 = new StringBuilder().append("failed a run in ");
                    String formatDuration3 = formatDuration(nanoTime);
                    Log1F380D.a((Object) formatDuration3);
                    log(task, queue, append3.append(formatDuration3).toString());
                }
            }
            InlineMarker.finallyEnd(1);
            throw th;
        }
    }
}
