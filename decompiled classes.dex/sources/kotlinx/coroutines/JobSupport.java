package kotlinx.coroutines;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.OpDescriptor;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlinx.coroutines.intrinsics.CancellableKt;
import kotlinx.coroutines.intrinsics.UndispatchedKt;
import kotlinx.coroutines.selects.SelectClause0;
import kotlinx.coroutines.selects.SelectInstance;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000è\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0001\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0012\b\u0017\u0018\u00002\u00020X2\u00020\u00172\u000202\u00030Ã\u0001:\u0006Ò\u0001Ó\u0001Ô\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0001¢\u0006\u0004\b\u0003\u0010\u0004J'\u0010\u000b\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\tH\u0002¢\u0006\u0004\b\u000b\u0010\fJ%\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u000e\u001a\u00020\r2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\r0\u000fH\u0002¢\u0006\u0004\b\u0012\u0010\u0013J\u0019\u0010\u0015\u001a\u00020\u00112\b\u0010\u0014\u001a\u0004\u0018\u00010\u0005H\u0014¢\u0006\u0004\b\u0015\u0010\u0016J\u0015\u0010\u001a\u001a\u00020\u00192\u0006\u0010\u0018\u001a\u00020\u0017¢\u0006\u0004\b\u001a\u0010\u001bJ\u0015\u0010\u001e\u001a\u0004\u0018\u00010\u0005H@ø\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u001dJ\u0015\u0010\u001f\u001a\u0004\u0018\u00010\u0005H@ø\u0001\u0000¢\u0006\u0004\b\u001f\u0010\u001dJ\u0019\u0010!\u001a\u00020\u00012\b\u0010 \u001a\u0004\u0018\u00010\rH\u0017¢\u0006\u0004\b!\u0010\"J\u001f\u0010!\u001a\u00020\u00112\u000e\u0010 \u001a\n\u0018\u00010#j\u0004\u0018\u0001`$H\u0016¢\u0006\u0004\b!\u0010%J\u0017\u0010&\u001a\u00020\u00012\b\u0010 \u001a\u0004\u0018\u00010\r¢\u0006\u0004\b&\u0010\"J\u0019\u0010)\u001a\u00020\u00012\b\u0010 \u001a\u0004\u0018\u00010\u0005H\u0000¢\u0006\u0004\b'\u0010(J\u0017\u0010*\u001a\u00020\u00112\u0006\u0010 \u001a\u00020\rH\u0016¢\u0006\u0004\b*\u0010+J\u001b\u0010,\u001a\u0004\u0018\u00010\u00052\b\u0010 \u001a\u0004\u0018\u00010\u0005H\u0002¢\u0006\u0004\b,\u0010-J\u0017\u0010.\u001a\u00020\u00012\u0006\u0010 \u001a\u00020\rH\u0002¢\u0006\u0004\b.\u0010\"J\u000f\u00100\u001a\u00020/H\u0014¢\u0006\u0004\b0\u00101J\u0017\u00102\u001a\u00020\u00012\u0006\u0010 \u001a\u00020\rH\u0016¢\u0006\u0004\b2\u0010\"J!\u00105\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u0002032\b\u00104\u001a\u0004\u0018\u00010\u0005H\u0002¢\u0006\u0004\b5\u00106J)\u0010;\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u0002072\u0006\u00109\u001a\u0002082\b\u0010:\u001a\u0004\u0018\u00010\u0005H\u0002¢\u0006\u0004\b;\u0010<J\u0019\u0010=\u001a\u00020\r2\b\u0010 \u001a\u0004\u0018\u00010\u0005H\u0002¢\u0006\u0004\b=\u0010>J(\u0010C\u001a\u00020@2\n\b\u0002\u0010?\u001a\u0004\u0018\u00010/2\n\b\u0002\u0010 \u001a\u0004\u0018\u00010\rH\b¢\u0006\u0004\bA\u0010BJ#\u0010D\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0014\u001a\u0002072\b\u0010:\u001a\u0004\u0018\u00010\u0005H\u0002¢\u0006\u0004\bD\u0010EJ\u0019\u0010F\u001a\u0004\u0018\u0001082\u0006\u0010\u0014\u001a\u000203H\u0002¢\u0006\u0004\bF\u0010GJ\u0011\u0010H\u001a\u00060#j\u0002`$¢\u0006\u0004\bH\u0010IJ\u0013\u0010J\u001a\u00060#j\u0002`$H\u0016¢\u0006\u0004\bJ\u0010IJ\u0011\u0010M\u001a\u0004\u0018\u00010\u0005H\u0000¢\u0006\u0004\bK\u0010LJ\u000f\u0010N\u001a\u0004\u0018\u00010\r¢\u0006\u0004\bN\u0010OJ'\u0010P\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0014\u001a\u0002072\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\r0\u000fH\u0002¢\u0006\u0004\bP\u0010QJ\u0019\u0010R\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0014\u001a\u000203H\u0002¢\u0006\u0004\bR\u0010SJ\u0017\u0010U\u001a\u00020\u00012\u0006\u0010T\u001a\u00020\rH\u0014¢\u0006\u0004\bU\u0010\"J\u0017\u0010W\u001a\u00020\u00112\u0006\u0010T\u001a\u00020\rH\u0010¢\u0006\u0004\bV\u0010+J\u0019\u0010Z\u001a\u00020\u00112\b\u0010Y\u001a\u0004\u0018\u00010XH\u0004¢\u0006\u0004\bZ\u0010[JF\u0010d\u001a\u00020c2\u0006\u0010\\\u001a\u00020\u00012\u0006\u0010]\u001a\u00020\u00012'\u0010b\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\r¢\u0006\f\b_\u0012\b\b`\u0012\u0004\b\b( \u0012\u0004\u0012\u00020\u00110^j\u0002`a¢\u0006\u0004\bd\u0010eJ6\u0010d\u001a\u00020c2'\u0010b\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\r¢\u0006\f\b_\u0012\b\b`\u0012\u0004\b\b( \u0012\u0004\u0012\u00020\u00110^j\u0002`a¢\u0006\u0004\bd\u0010fJ\u0013\u0010g\u001a\u00020\u0011H@ø\u0001\u0000¢\u0006\u0004\bg\u0010\u001dJ\u000f\u0010h\u001a\u00020\u0001H\u0002¢\u0006\u0004\bh\u0010iJ\u0013\u0010j\u001a\u00020\u0011H@ø\u0001\u0000¢\u0006\u0004\bj\u0010\u001dJ&\u0010m\u001a\u00020l2\u0014\u0010k\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0005\u0012\u0004\u0012\u00020\u00110^H\b¢\u0006\u0004\bm\u0010nJ\u001b\u0010o\u001a\u0004\u0018\u00010\u00052\b\u0010 \u001a\u0004\u0018\u00010\u0005H\u0002¢\u0006\u0004\bo\u0010-J\u0019\u0010q\u001a\u00020\u00012\b\u0010:\u001a\u0004\u0018\u00010\u0005H\u0000¢\u0006\u0004\bp\u0010(J\u001b\u0010s\u001a\u0004\u0018\u00010\u00052\b\u0010:\u001a\u0004\u0018\u00010\u0005H\u0000¢\u0006\u0004\br\u0010-J@\u0010t\u001a\u00020\t2'\u0010b\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\r¢\u0006\f\b_\u0012\b\b`\u0012\u0004\b\b( \u0012\u0004\u0012\u00020\u00110^j\u0002`a2\u0006\u0010\\\u001a\u00020\u0001H\u0002¢\u0006\u0004\bt\u0010uJ\u000f\u0010w\u001a\u00020/H\u0010¢\u0006\u0004\bv\u00101J\u001f\u0010x\u001a\u00020\u00112\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010 \u001a\u00020\rH\u0002¢\u0006\u0004\bx\u0010yJ.\u0010{\u001a\u00020\u0011\"\n\b\u0000\u0010z\u0018\u0001*\u00020\t2\u0006\u0010\b\u001a\u00020\u00072\b\u0010 \u001a\u0004\u0018\u00010\rH\b¢\u0006\u0004\b{\u0010yJ\u0019\u0010\\\u001a\u00020\u00112\b\u0010 \u001a\u0004\u0018\u00010\rH\u0014¢\u0006\u0004\b\\\u0010+J\u0019\u0010|\u001a\u00020\u00112\b\u0010\u0014\u001a\u0004\u0018\u00010\u0005H\u0014¢\u0006\u0004\b|\u0010\u0016J\u000f\u0010}\u001a\u00020\u0011H\u0014¢\u0006\u0004\b}\u0010~J\u0019\u0010\u0001\u001a\u00020\u00112\u0007\u0010\u0001\u001a\u00020¢\u0006\u0006\b\u0001\u0010\u0001J\u001b\u0010\u0001\u001a\u00020\u00112\u0007\u0010\u0014\u001a\u00030\u0001H\u0002¢\u0006\u0006\b\u0001\u0010\u0001J\u001a\u0010\u0001\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\tH\u0002¢\u0006\u0006\b\u0001\u0010\u0001JI\u0010\u0001\u001a\u00020\u0011\"\u0005\b\u0000\u0010\u00012\u000e\u0010\u0001\u001a\t\u0012\u0004\u0012\u00028\u00000\u00012\u001d\u0010k\u001a\u0019\b\u0001\u0012\u000b\u0012\t\u0012\u0004\u0012\u00028\u00000\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00050^ø\u0001\u0000¢\u0006\u0006\b\u0001\u0010\u0001JX\u0010\u0001\u001a\u00020\u0011\"\u0004\b\u0000\u0010z\"\u0005\b\u0001\u0010\u00012\u000e\u0010\u0001\u001a\t\u0012\u0004\u0012\u00028\u00010\u00012$\u0010k\u001a \b\u0001\u0012\u0004\u0012\u00028\u0000\u0012\u000b\u0012\t\u0012\u0004\u0012\u00028\u00010\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0001H\u0000ø\u0001\u0000¢\u0006\u0006\b\u0001\u0010\u0001J\u001a\u0010\u0001\u001a\u00020\u00112\u0006\u0010\n\u001a\u00020\tH\u0000¢\u0006\u0006\b\u0001\u0010\u0001JX\u0010\u0001\u001a\u00020\u0011\"\u0004\b\u0000\u0010z\"\u0005\b\u0001\u0010\u00012\u000e\u0010\u0001\u001a\t\u0012\u0004\u0012\u00028\u00010\u00012$\u0010k\u001a \b\u0001\u0012\u0004\u0012\u00028\u0000\u0012\u000b\u0012\t\u0012\u0004\u0012\u00028\u00010\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0001H\u0000ø\u0001\u0000¢\u0006\u0006\b\u0001\u0010\u0001J\u000f\u0010\u0001\u001a\u00020\u0001¢\u0006\u0005\b\u0001\u0010iJ\u001d\u0010\u0001\u001a\u00030\u00012\b\u0010\u0014\u001a\u0004\u0018\u00010\u0005H\u0002¢\u0006\u0006\b\u0001\u0010\u0001J\u001c\u0010\u0001\u001a\u00020/2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0005H\u0002¢\u0006\u0006\b\u0001\u0010\u0001J\u0011\u0010\u0001\u001a\u00020/H\u0007¢\u0006\u0005\b\u0001\u00101J\u0011\u0010\u0001\u001a\u00020/H\u0016¢\u0006\u0005\b\u0001\u00101J$\u0010\u0001\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u0002032\b\u00104\u001a\u0004\u0018\u00010\u0005H\u0002¢\u0006\u0006\b\u0001\u0010\u0001J\"\u0010 \u0001\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u0002032\u0006\u0010\u000e\u001a\u00020\rH\u0002¢\u0006\u0006\b \u0001\u0010¡\u0001J(\u0010¢\u0001\u001a\u0004\u0018\u00010\u00052\b\u0010\u0014\u001a\u0004\u0018\u00010\u00052\b\u0010:\u001a\u0004\u0018\u00010\u0005H\u0002¢\u0006\u0006\b¢\u0001\u0010£\u0001J&\u0010¤\u0001\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0014\u001a\u0002032\b\u0010:\u001a\u0004\u0018\u00010\u0005H\u0002¢\u0006\u0006\b¤\u0001\u0010¥\u0001J-\u0010¦\u0001\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u0002072\u0006\u0010\u0018\u001a\u0002082\b\u0010:\u001a\u0004\u0018\u00010\u0005H\u0010¢\u0006\u0006\b¦\u0001\u0010§\u0001J\u0019\u0010©\u0001\u001a\u0004\u0018\u000108*\u00030¨\u0001H\u0002¢\u0006\u0006\b©\u0001\u0010ª\u0001J\u001f\u0010«\u0001\u001a\u00020\u0011*\u00020\u00072\b\u0010 \u001a\u0004\u0018\u00010\rH\u0002¢\u0006\u0005\b«\u0001\u0010yJ&\u0010¬\u0001\u001a\u00060#j\u0002`$*\u00020\r2\n\b\u0002\u0010?\u001a\u0004\u0018\u00010/H\u0004¢\u0006\u0006\b¬\u0001\u0010­\u0001R\u001b\u0010±\u0001\u001a\t\u0012\u0004\u0012\u00020X0®\u00018F¢\u0006\b\u001a\u0006\b¯\u0001\u0010°\u0001R\u0018\u0010³\u0001\u001a\u0004\u0018\u00010\r8DX\u0004¢\u0006\u0007\u001a\u0005\b²\u0001\u0010OR\u0016\u0010µ\u0001\u001a\u00020\u00018DX\u0004¢\u0006\u0007\u001a\u0005\b´\u0001\u0010iR\u0016\u0010·\u0001\u001a\u00020\u00018PX\u0004¢\u0006\u0007\u001a\u0005\b¶\u0001\u0010iR\u0016\u0010¸\u0001\u001a\u00020\u00018VX\u0004¢\u0006\u0007\u001a\u0005\b¸\u0001\u0010iR\u0013\u0010¹\u0001\u001a\u00020\u00018F¢\u0006\u0007\u001a\u0005\b¹\u0001\u0010iR\u0013\u0010º\u0001\u001a\u00020\u00018F¢\u0006\u0007\u001a\u0005\bº\u0001\u0010iR\u0013\u0010»\u0001\u001a\u00020\u00018F¢\u0006\u0007\u001a\u0005\b»\u0001\u0010iR\u0016\u0010¼\u0001\u001a\u00020\u00018TX\u0004¢\u0006\u0007\u001a\u0005\b¼\u0001\u0010iR\u0019\u0010À\u0001\u001a\u0007\u0012\u0002\b\u00030½\u00018F¢\u0006\b\u001a\u0006\b¾\u0001\u0010¿\u0001R\u0016\u0010Â\u0001\u001a\u00020\u00018PX\u0004¢\u0006\u0007\u001a\u0005\bÁ\u0001\u0010iR\u0015\u0010Æ\u0001\u001a\u00030Ã\u00018F¢\u0006\b\u001a\u0006\bÄ\u0001\u0010Å\u0001R.\u0010Ì\u0001\u001a\u0004\u0018\u00010\u00192\t\u0010Ç\u0001\u001a\u0004\u0018\u00010\u00198@@@X\u000e¢\u0006\u0010\u001a\u0006\bÈ\u0001\u0010É\u0001\"\u0006\bÊ\u0001\u0010Ë\u0001R\u0017\u0010\u0014\u001a\u0004\u0018\u00010\u00058@X\u0004¢\u0006\u0007\u001a\u0005\bÍ\u0001\u0010LR\u001e\u0010Ï\u0001\u001a\u0004\u0018\u00010\r*\u0004\u0018\u00010\u00058BX\u0004¢\u0006\u0007\u001a\u0005\bÎ\u0001\u0010>R\u001b\u0010Ð\u0001\u001a\u00020\u0001*\u0002038BX\u0004¢\u0006\b\u001a\u0006\bÐ\u0001\u0010Ñ\u0001\u0002\u0004\n\u0002\b\u0019¨\u0006Õ\u0001"}, d2 = {"Lkotlinx/coroutines/JobSupport;", "", "active", "<init>", "(Z)V", "", "expect", "Lkotlinx/coroutines/NodeList;", "list", "Lkotlinx/coroutines/JobNode;", "node", "addLastAtomic", "(Ljava/lang/Object;Lkotlinx/coroutines/NodeList;Lkotlinx/coroutines/JobNode;)Z", "", "rootCause", "", "exceptions", "", "addSuppressedExceptions", "(Ljava/lang/Throwable;Ljava/util/List;)V", "state", "afterCompletion", "(Ljava/lang/Object;)V", "Lkotlinx/coroutines/ChildJob;", "child", "Lkotlinx/coroutines/ChildHandle;", "attachChild", "(Lkotlinx/coroutines/ChildJob;)Lkotlinx/coroutines/ChildHandle;", "awaitInternal$kotlinx_coroutines_core", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "awaitInternal", "awaitSuspend", "cause", "cancel", "(Ljava/lang/Throwable;)Z", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "(Ljava/util/concurrent/CancellationException;)V", "cancelCoroutine", "cancelImpl$kotlinx_coroutines_core", "(Ljava/lang/Object;)Z", "cancelImpl", "cancelInternal", "(Ljava/lang/Throwable;)V", "cancelMakeCompleting", "(Ljava/lang/Object;)Ljava/lang/Object;", "cancelParent", "", "cancellationExceptionMessage", "()Ljava/lang/String;", "childCancelled", "Lkotlinx/coroutines/Incomplete;", "update", "completeStateFinalization", "(Lkotlinx/coroutines/Incomplete;Ljava/lang/Object;)V", "Lkotlinx/coroutines/JobSupport$Finishing;", "Lkotlinx/coroutines/ChildHandleNode;", "lastChild", "proposedUpdate", "continueCompleting", "(Lkotlinx/coroutines/JobSupport$Finishing;Lkotlinx/coroutines/ChildHandleNode;Ljava/lang/Object;)V", "createCauseException", "(Ljava/lang/Object;)Ljava/lang/Throwable;", "message", "Lkotlinx/coroutines/JobCancellationException;", "defaultCancellationException$kotlinx_coroutines_core", "(Ljava/lang/String;Ljava/lang/Throwable;)Lkotlinx/coroutines/JobCancellationException;", "defaultCancellationException", "finalizeFinishingState", "(Lkotlinx/coroutines/JobSupport$Finishing;Ljava/lang/Object;)Ljava/lang/Object;", "firstChild", "(Lkotlinx/coroutines/Incomplete;)Lkotlinx/coroutines/ChildHandleNode;", "getCancellationException", "()Ljava/util/concurrent/CancellationException;", "getChildJobCancellationCause", "getCompletedInternal$kotlinx_coroutines_core", "()Ljava/lang/Object;", "getCompletedInternal", "getCompletionExceptionOrNull", "()Ljava/lang/Throwable;", "getFinalRootCause", "(Lkotlinx/coroutines/JobSupport$Finishing;Ljava/util/List;)Ljava/lang/Throwable;", "getOrPromoteCancellingList", "(Lkotlinx/coroutines/Incomplete;)Lkotlinx/coroutines/NodeList;", "exception", "handleJobException", "handleOnCompletionException$kotlinx_coroutines_core", "handleOnCompletionException", "Lkotlinx/coroutines/Job;", "parent", "initParentJob", "(Lkotlinx/coroutines/Job;)V", "onCancelling", "invokeImmediately", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "Lkotlinx/coroutines/CompletionHandler;", "handler", "Lkotlinx/coroutines/DisposableHandle;", "invokeOnCompletion", "(ZZLkotlin/jvm/functions/Function1;)Lkotlinx/coroutines/DisposableHandle;", "(Lkotlin/jvm/functions/Function1;)Lkotlinx/coroutines/DisposableHandle;", "join", "joinInternal", "()Z", "joinSuspend", "block", "", "loopOnState", "(Lkotlin/jvm/functions/Function1;)Ljava/lang/Void;", "makeCancelling", "makeCompleting$kotlinx_coroutines_core", "makeCompleting", "makeCompletingOnce$kotlinx_coroutines_core", "makeCompletingOnce", "makeNode", "(Lkotlin/jvm/functions/Function1;Z)Lkotlinx/coroutines/JobNode;", "nameString$kotlinx_coroutines_core", "nameString", "notifyCancelling", "(Lkotlinx/coroutines/NodeList;Ljava/lang/Throwable;)V", "T", "notifyHandlers", "onCompletionInternal", "onStart", "()V", "Lkotlinx/coroutines/ParentJob;", "parentJob", "parentCancelled", "(Lkotlinx/coroutines/ParentJob;)V", "Lkotlinx/coroutines/Empty;", "promoteEmptyToNodeList", "(Lkotlinx/coroutines/Empty;)V", "promoteSingleToNodeList", "(Lkotlinx/coroutines/JobNode;)V", "R", "Lkotlinx/coroutines/selects/SelectInstance;", "select", "Lkotlin/coroutines/Continuation;", "registerSelectClause0", "(Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function1;)V", "Lkotlin/Function2;", "registerSelectClause1Internal$kotlinx_coroutines_core", "(Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "registerSelectClause1Internal", "removeNode$kotlinx_coroutines_core", "removeNode", "selectAwaitCompletion$kotlinx_coroutines_core", "selectAwaitCompletion", "start", "", "startInternal", "(Ljava/lang/Object;)I", "stateString", "(Ljava/lang/Object;)Ljava/lang/String;", "toDebugString", "toString", "tryFinalizeSimpleState", "(Lkotlinx/coroutines/Incomplete;Ljava/lang/Object;)Z", "tryMakeCancelling", "(Lkotlinx/coroutines/Incomplete;Ljava/lang/Throwable;)Z", "tryMakeCompleting", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "tryMakeCompletingSlowPath", "(Lkotlinx/coroutines/Incomplete;Ljava/lang/Object;)Ljava/lang/Object;", "tryWaitForChild", "(Lkotlinx/coroutines/JobSupport$Finishing;Lkotlinx/coroutines/ChildHandleNode;Ljava/lang/Object;)Z", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "nextChild", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;)Lkotlinx/coroutines/ChildHandleNode;", "notifyCompletion", "toCancellationException", "(Ljava/lang/Throwable;Ljava/lang/String;)Ljava/util/concurrent/CancellationException;", "Lkotlin/sequences/Sequence;", "getChildren", "()Lkotlin/sequences/Sequence;", "children", "getCompletionCause", "completionCause", "getCompletionCauseHandled", "completionCauseHandled", "getHandlesException$kotlinx_coroutines_core", "handlesException", "isActive", "isCancelled", "isCompleted", "isCompletedExceptionally", "isScopedCoroutine", "Lkotlin/coroutines/CoroutineContext$Key;", "getKey", "()Lkotlin/coroutines/CoroutineContext$Key;", "key", "getOnCancelComplete$kotlinx_coroutines_core", "onCancelComplete", "Lkotlinx/coroutines/selects/SelectClause0;", "getOnJoin", "()Lkotlinx/coroutines/selects/SelectClause0;", "onJoin", "value", "getParentHandle$kotlinx_coroutines_core", "()Lkotlinx/coroutines/ChildHandle;", "setParentHandle$kotlinx_coroutines_core", "(Lkotlinx/coroutines/ChildHandle;)V", "parentHandle", "getState$kotlinx_coroutines_core", "getExceptionOrNull", "exceptionOrNull", "isCancelling", "(Lkotlinx/coroutines/Incomplete;)Z", "AwaitContinuation", "ChildCompletion", "Finishing", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
@Deprecated(level = DeprecationLevel.ERROR, message = "This is internal API and may be removed in the future releases")
/* compiled from: 016B */
public class JobSupport implements Job, ChildJob, ParentJob, SelectClause0 {
    private static final /* synthetic */ AtomicReferenceFieldUpdater _state$FU = AtomicReferenceFieldUpdater.newUpdater(JobSupport.class, Object.class, "_state");
    private volatile /* synthetic */ Object _parentHandle;
    private volatile /* synthetic */ Object _state;

    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001b\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH\u0014R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/JobSupport$AwaitContinuation;", "T", "Lkotlinx/coroutines/CancellableContinuationImpl;", "delegate", "Lkotlin/coroutines/Continuation;", "job", "Lkotlinx/coroutines/JobSupport;", "(Lkotlin/coroutines/Continuation;Lkotlinx/coroutines/JobSupport;)V", "getContinuationCancellationCause", "", "parent", "Lkotlinx/coroutines/Job;", "nameString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: JobSupport.kt */
    private static final class AwaitContinuation<T> extends CancellableContinuationImpl<T> {
        private final JobSupport job;

        public AwaitContinuation(Continuation<? super T> delegate, JobSupport job2) {
            super(delegate, 1);
            this.job = job2;
        }

        public Throwable getContinuationCancellationCause(Job parent) {
            Throwable rootCause;
            Object state$kotlinx_coroutines_core = this.job.getState$kotlinx_coroutines_core();
            return (!(state$kotlinx_coroutines_core instanceof Finishing) || (rootCause = ((Finishing) state$kotlinx_coroutines_core).getRootCause()) == null) ? state$kotlinx_coroutines_core instanceof CompletedExceptionally ? ((CompletedExceptionally) state$kotlinx_coroutines_core).cause : parent.getCancellationException() : rootCause;
        }

        /* access modifiers changed from: protected */
        public String nameString() {
            return "AwaitContinuation";
        }
    }

    @Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\b\u0002\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\u0002\u0010\nJ\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lkotlinx/coroutines/JobSupport$ChildCompletion;", "Lkotlinx/coroutines/JobNode;", "parent", "Lkotlinx/coroutines/JobSupport;", "state", "Lkotlinx/coroutines/JobSupport$Finishing;", "child", "Lkotlinx/coroutines/ChildHandleNode;", "proposedUpdate", "", "(Lkotlinx/coroutines/JobSupport;Lkotlinx/coroutines/JobSupport$Finishing;Lkotlinx/coroutines/ChildHandleNode;Ljava/lang/Object;)V", "invoke", "", "cause", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: JobSupport.kt */
    private static final class ChildCompletion extends JobNode {
        private final ChildHandleNode child;
        private final JobSupport parent;
        private final Object proposedUpdate;
        private final Finishing state;

        public ChildCompletion(JobSupport parent2, Finishing state2, ChildHandleNode child2, Object proposedUpdate2) {
            this.parent = parent2;
            this.state = state2;
            this.child = child2;
            this.proposedUpdate = proposedUpdate2;
        }

        public /* bridge */ /* synthetic */ Object invoke(Object p1) {
            invoke((Throwable) p1);
            return Unit.INSTANCE;
        }

        public void invoke(Throwable cause) {
            this.parent.continueCompleting(this.state, this.child, this.proposedUpdate);
        }
    }

    @Metadata(d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\u0018\u0002\b\u0002\u0018\u00002\u00060\u0018j\u0002`,2\u00020-B!\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0007\u0010\bJ\u0015\u0010\u000b\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\u0005¢\u0006\u0004\b\u000b\u0010\fJ\u001f\u0010\u000f\u001a\u0012\u0012\u0004\u0012\u00020\u00050\rj\b\u0012\u0004\u0012\u00020\u0005`\u000eH\u0002¢\u0006\u0004\b\u000f\u0010\u0010J\u001d\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00050\u00122\b\u0010\u0011\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0013\u0010\u0014J\u000f\u0010\u0016\u001a\u00020\u0015H\u0016¢\u0006\u0004\b\u0016\u0010\u0017R(\u0010\u001e\u001a\u0004\u0018\u00010\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u00188B@BX\u000e¢\u0006\f\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u0014\u0010\u001f\u001a\u00020\u00038VX\u0004¢\u0006\u0006\u001a\u0004\b\u001f\u0010 R\u0011\u0010!\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b!\u0010 R$\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u00038F@FX\u000e¢\u0006\f\u001a\u0004\b\u0004\u0010 \"\u0004\b\"\u0010#R\u0011\u0010$\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b$\u0010 R\u001a\u0010\u0002\u001a\u00020\u00018\u0016X\u0004¢\u0006\f\n\u0004\b\u0002\u0010%\u001a\u0004\b&\u0010'R(\u0010\u0006\u001a\u0004\u0018\u00010\u00052\b\u0010\u0019\u001a\u0004\u0018\u00010\u00058F@FX\u000e¢\u0006\f\u001a\u0004\b(\u0010)\"\u0004\b*\u0010\f¨\u0006+"}, d2 = {"Lkotlinx/coroutines/JobSupport$Finishing;", "Lkotlinx/coroutines/NodeList;", "list", "", "isCompleting", "", "rootCause", "<init>", "(Lkotlinx/coroutines/NodeList;ZLjava/lang/Throwable;)V", "exception", "", "addExceptionLocked", "(Ljava/lang/Throwable;)V", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "allocateList", "()Ljava/util/ArrayList;", "proposedException", "", "sealLocked", "(Ljava/lang/Throwable;)Ljava/util/List;", "", "toString", "()Ljava/lang/String;", "", "value", "getExceptionsHolder", "()Ljava/lang/Object;", "setExceptionsHolder", "(Ljava/lang/Object;)V", "exceptionsHolder", "isActive", "()Z", "isCancelling", "setCompleting", "(Z)V", "isSealed", "Lkotlinx/coroutines/NodeList;", "getList", "()Lkotlinx/coroutines/NodeList;", "getRootCause", "()Ljava/lang/Throwable;", "setRootCause", "kotlinx-coroutines-core", "Lkotlinx/coroutines/internal/SynchronizedObject;", "Lkotlinx/coroutines/Incomplete;"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: 0179 */
    private static final class Finishing implements Incomplete {
        private volatile /* synthetic */ Object _exceptionsHolder = null;
        private volatile /* synthetic */ int _isCompleting;
        private volatile /* synthetic */ Object _rootCause;
        private final NodeList list;

        public Finishing(NodeList list2, boolean isCompleting, Throwable rootCause) {
            this.list = list2;
            this._isCompleting = isCompleting;
            this._rootCause = rootCause;
        }

        private final ArrayList<Throwable> allocateList() {
            return new ArrayList<>(4);
        }

        private final Object getExceptionsHolder() {
            return this._exceptionsHolder;
        }

        private final void setExceptionsHolder(Object value) {
            this._exceptionsHolder = value;
        }

        public final void addExceptionLocked(Throwable exception) {
            Throwable rootCause = getRootCause();
            if (rootCause == null) {
                setRootCause(exception);
            } else if (exception != rootCause) {
                Object exceptionsHolder = getExceptionsHolder();
                if (exceptionsHolder == null) {
                    setExceptionsHolder(exception);
                } else if (exceptionsHolder instanceof Throwable) {
                    if (exception != exceptionsHolder) {
                        ArrayList<Throwable> allocateList = allocateList();
                        ArrayList<Throwable> arrayList = allocateList;
                        arrayList.add(exceptionsHolder);
                        arrayList.add(exception);
                        setExceptionsHolder(allocateList);
                    }
                } else if (exceptionsHolder instanceof ArrayList) {
                    ((ArrayList) exceptionsHolder).add(exception);
                } else {
                    String stringPlus = Intrinsics.stringPlus("State is ", exceptionsHolder);
                    Log1F380D.a((Object) stringPlus);
                    throw new IllegalStateException(stringPlus.toString());
                }
            }
        }

        public NodeList getList() {
            return this.list;
        }

        public final Throwable getRootCause() {
            return (Throwable) this._rootCause;
        }

        public boolean isActive() {
            return getRootCause() == null;
        }

        public final boolean isCancelling() {
            return getRootCause() != null;
        }

        /* JADX WARNING: type inference failed for: r0v0, types: [int, boolean] */
        public final boolean isCompleting() {
            return this._isCompleting;
        }

        public final boolean isSealed() {
            return getExceptionsHolder() == JobSupportKt.SEALED;
        }

        public final void setCompleting(boolean value) {
            this._isCompleting = value;
        }

        public final void setRootCause(Throwable value) {
            this._rootCause = value;
        }

        public String toString() {
            return "Finishing[cancelling=" + isCancelling() + ", completing=" + isCompleting() + ", rootCause=" + getRootCause() + ", exceptions=" + getExceptionsHolder() + ", list=" + getList() + ']';
        }

        public final List<Throwable> sealLocked(Throwable proposedException) {
            ArrayList<Throwable> arrayList;
            Object exceptionsHolder = getExceptionsHolder();
            if (exceptionsHolder == null) {
                arrayList = allocateList();
            } else if (exceptionsHolder instanceof Throwable) {
                arrayList = allocateList();
                arrayList.add(exceptionsHolder);
            } else if (exceptionsHolder instanceof ArrayList) {
                arrayList = (ArrayList) exceptionsHolder;
            } else {
                String stringPlus = Intrinsics.stringPlus("State is ", exceptionsHolder);
                Log1F380D.a((Object) stringPlus);
                throw new IllegalStateException(stringPlus.toString());
            }
            ArrayList<Throwable> arrayList2 = arrayList;
            Throwable rootCause = getRootCause();
            if (rootCause != null) {
                arrayList2.add(0, rootCause);
            }
            if (proposedException != null && !Intrinsics.areEqual((Object) proposedException, (Object) rootCause)) {
                arrayList2.add(proposedException);
            }
            setExceptionsHolder(JobSupportKt.SEALED);
            return arrayList2;
        }
    }

    public JobSupport(boolean active) {
        this._state = active ? JobSupportKt.EMPTY_ACTIVE : JobSupportKt.EMPTY_NEW;
        this._parentHandle = null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:1:0x0012 A[LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean addLastAtomic(java.lang.Object r7, kotlinx.coroutines.NodeList r8, kotlinx.coroutines.JobNode r9) {
        /*
            r6 = this;
            r0 = r8
            kotlinx.coroutines.internal.LockFreeLinkedListNode r0 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r0
            r1 = 0
            r2 = r0
            r3 = 0
            kotlinx.coroutines.JobSupport$addLastAtomic$$inlined$addLastIf$1 r4 = new kotlinx.coroutines.JobSupport$addLastAtomic$$inlined$addLastIf$1
            r5 = r9
            kotlinx.coroutines.internal.LockFreeLinkedListNode r5 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r5
            r4.<init>(r5, r6, r7)
            kotlinx.coroutines.internal.LockFreeLinkedListNode$CondAddOp r4 = (kotlinx.coroutines.internal.LockFreeLinkedListNode.CondAddOp) r4
            r2 = r4
        L_0x0012:
            kotlinx.coroutines.internal.LockFreeLinkedListNode r3 = r0.getPrevNode()
            r4 = r9
            kotlinx.coroutines.internal.LockFreeLinkedListNode r4 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r4
            int r4 = r3.tryCondAddNext(r4, r0, r2)
            switch(r4) {
                case 1: goto L_0x0024;
                case 2: goto L_0x0022;
                default: goto L_0x0021;
            }
        L_0x0021:
            goto L_0x0012
        L_0x0022:
            r4 = 0
            goto L_0x0025
        L_0x0024:
            r4 = 1
        L_0x0025:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.JobSupport.addLastAtomic(java.lang.Object, kotlinx.coroutines.NodeList, kotlinx.coroutines.JobNode):boolean");
    }

    private final void addSuppressedExceptions(Throwable rootCause, List<? extends Throwable> exceptions) {
        if (exceptions.size() > 1) {
            Set newSetFromMap = Collections.newSetFromMap(new IdentityHashMap(exceptions.size()));
            Throwable unwrapImpl = !DebugKt.getRECOVER_STACK_TRACES() ? rootCause : StackTraceRecoveryKt.unwrapImpl(rootCause);
            for (Throwable th : exceptions) {
                Throwable unwrapImpl2 = !DebugKt.getRECOVER_STACK_TRACES() ? th : StackTraceRecoveryKt.unwrapImpl(th);
                if (unwrapImpl2 != rootCause && unwrapImpl2 != unwrapImpl && !(unwrapImpl2 instanceof CancellationException) && newSetFromMap.add(unwrapImpl2)) {
                    ExceptionsKt.addSuppressed(rootCause, unwrapImpl2);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public final Object awaitSuspend(Continuation<Object> $completion) {
        AwaitContinuation awaitContinuation = new AwaitContinuation(IntrinsicsKt.intercepted($completion), this);
        awaitContinuation.initCancellability();
        CancellableContinuationKt.disposeOnCancellation(awaitContinuation, invokeOnCompletion(new ResumeAwaitOnCompletion(awaitContinuation)));
        Object result = awaitContinuation.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended($completion);
        }
        return result;
    }

    private final Object cancelMakeCompleting(Object cause) {
        Object tryMakeCompleting;
        do {
            Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof Incomplete) || ((state$kotlinx_coroutines_core instanceof Finishing) && ((Finishing) state$kotlinx_coroutines_core).isCompleting())) {
                return JobSupportKt.COMPLETING_ALREADY;
            }
            tryMakeCompleting = tryMakeCompleting(state$kotlinx_coroutines_core, new CompletedExceptionally(createCauseException(cause), false, 2, (DefaultConstructorMarker) null));
        } while (tryMakeCompleting == JobSupportKt.COMPLETING_RETRY);
        return tryMakeCompleting;
    }

    private final boolean cancelParent(Throwable cause) {
        if (isScopedCoroutine()) {
            return true;
        }
        boolean z = cause instanceof CancellationException;
        ChildHandle parentHandle$kotlinx_coroutines_core = getParentHandle$kotlinx_coroutines_core();
        return (parentHandle$kotlinx_coroutines_core == null || parentHandle$kotlinx_coroutines_core == NonDisposableHandle.INSTANCE) ? z : parentHandle$kotlinx_coroutines_core.childCancelled(cause) || z;
    }

    private final void completeStateFinalization(Incomplete state, Object update) {
        ChildHandle parentHandle$kotlinx_coroutines_core = getParentHandle$kotlinx_coroutines_core();
        if (parentHandle$kotlinx_coroutines_core != null) {
            parentHandle$kotlinx_coroutines_core.dispose();
            setParentHandle$kotlinx_coroutines_core(NonDisposableHandle.INSTANCE);
        }
        Throwable th = null;
        CompletedExceptionally completedExceptionally = update instanceof CompletedExceptionally ? (CompletedExceptionally) update : null;
        if (completedExceptionally != null) {
            th = completedExceptionally.cause;
        }
        Throwable th2 = th;
        if (state instanceof JobNode) {
            try {
                ((JobNode) state).invoke(th2);
            } catch (Throwable th3) {
                handleOnCompletionException$kotlinx_coroutines_core(new CompletionHandlerException("Exception in completion handler " + state + " for " + this, th3));
            }
        } else {
            NodeList list = state.getList();
            if (list != null) {
                notifyCompletion(list, th2);
            }
        }
    }

    /* access modifiers changed from: private */
    public final void continueCompleting(Finishing state, ChildHandleNode lastChild, Object proposedUpdate) {
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(getState$kotlinx_coroutines_core() == state)) {
                throw new AssertionError();
            }
        }
        ChildHandleNode nextChild = nextChild(lastChild);
        if (nextChild == null || !tryWaitForChild(state, nextChild, proposedUpdate)) {
            afterCompletion(finalizeFinishingState(state, proposedUpdate));
        }
    }

    private final Throwable createCauseException(Object cause) {
        if (cause == null ? true : cause instanceof Throwable) {
            Throwable th = (Throwable) cause;
            if (th != null) {
                return th;
            }
            String access$cancellationExceptionMessage = cancellationExceptionMessage();
            Log1F380D.a((Object) access$cancellationExceptionMessage);
            return new JobCancellationException(access$cancellationExceptionMessage, (Throwable) null, this);
        } else if (cause != null) {
            return ((ParentJob) cause).getChildJobCancellationCause();
        } else {
            throw new NullPointerException("null cannot be cast to non-null type kotlinx.coroutines.ParentJob");
        }
    }

    private final Object finalizeFinishingState(Finishing state, Object proposedUpdate) {
        boolean isCancelling;
        Throwable finalRootCause;
        boolean z = true;
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(getState$kotlinx_coroutines_core() == state)) {
                throw new AssertionError();
            }
        }
        if (DebugKt.getASSERTIONS_ENABLED() && !(!state.isSealed())) {
            throw new AssertionError();
        } else if (!DebugKt.getASSERTIONS_ENABLED() || state.isCompleting()) {
            CompletedExceptionally completedExceptionally = proposedUpdate instanceof CompletedExceptionally ? (CompletedExceptionally) proposedUpdate : null;
            Throwable th = completedExceptionally == null ? null : completedExceptionally.cause;
            synchronized (state) {
                isCancelling = state.isCancelling();
                List<Throwable> sealLocked = state.sealLocked(th);
                finalRootCause = getFinalRootCause(state, sealLocked);
                if (finalRootCause != null) {
                    addSuppressedExceptions(finalRootCause, sealLocked);
                }
            }
            Throwable th2 = finalRootCause;
            Object completedExceptionally2 = (th2 == null || th2 == th) ? proposedUpdate : new CompletedExceptionally(th2, false, 2, (DefaultConstructorMarker) null);
            if (th2 != null) {
                if (!cancelParent(th2) && !handleJobException(th2)) {
                    z = false;
                }
                if (z) {
                    if (completedExceptionally2 != null) {
                        ((CompletedExceptionally) completedExceptionally2).makeHandled();
                    } else {
                        throw new NullPointerException("null cannot be cast to non-null type kotlinx.coroutines.CompletedExceptionally");
                    }
                }
            }
            if (!isCancelling) {
                onCancelling(th2);
            }
            onCompletionInternal(completedExceptionally2);
            boolean m = AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, state, JobSupportKt.boxIncomplete(completedExceptionally2));
            if (!DebugKt.getASSERTIONS_ENABLED() || m) {
                completeStateFinalization(state, completedExceptionally2);
                return completedExceptionally2;
            }
            throw new AssertionError();
        } else {
            throw new AssertionError();
        }
    }

    private final ChildHandleNode firstChild(Incomplete state) {
        ChildHandleNode childHandleNode = state instanceof ChildHandleNode ? (ChildHandleNode) state : null;
        if (childHandleNode != null) {
            return childHandleNode;
        }
        NodeList list = state.getList();
        if (list == null) {
            return null;
        }
        return nextChild(list);
    }

    private final Throwable getExceptionOrNull(Object $this$exceptionOrNull) {
        CompletedExceptionally completedExceptionally = $this$exceptionOrNull instanceof CompletedExceptionally ? (CompletedExceptionally) $this$exceptionOrNull : null;
        if (completedExceptionally == null) {
            return null;
        }
        return completedExceptionally.cause;
    }

    private final boolean isCancelling(Incomplete $this$isCancelling) {
        return ($this$isCancelling instanceof Finishing) && ((Finishing) $this$isCancelling).isCancelling();
    }

    private final boolean joinInternal() {
        Object state$kotlinx_coroutines_core;
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                return false;
            }
        } while (startInternal(state$kotlinx_coroutines_core) < 0);
        return true;
    }

    /* access modifiers changed from: private */
    public final Object joinSuspend(Continuation<? super Unit> $completion) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted($completion), 1);
        cancellableContinuationImpl.initCancellability();
        CancellableContinuation cancellableContinuation = cancellableContinuationImpl;
        CancellableContinuationKt.disposeOnCancellation(cancellableContinuation, invokeOnCompletion(new ResumeOnCompletion(cancellableContinuation)));
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended($completion);
        }
        return result == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? result : Unit.INSTANCE;
    }

    private final Void loopOnState(Function1<Object, Unit> block) {
        while (true) {
            block.invoke(getState$kotlinx_coroutines_core());
        }
    }

    private final JobNode makeNode(Function1<? super Throwable, Unit> handler, boolean onCancelling) {
        JobNode jobNode = null;
        if (onCancelling) {
            if (handler instanceof JobCancellingNode) {
                jobNode = (JobCancellingNode) handler;
            }
            if (jobNode == null) {
                jobNode = new InvokeOnCancelling(handler);
            }
            jobNode = jobNode;
        } else {
            JobNode jobNode2 = handler instanceof JobNode ? (JobNode) handler : null;
            if (jobNode2 != null) {
                JobNode jobNode3 = jobNode2;
                if (!DebugKt.getASSERTIONS_ENABLED() || (!(jobNode3 instanceof JobCancellingNode))) {
                    jobNode = jobNode2;
                } else {
                    throw new AssertionError();
                }
            }
            if (jobNode == null) {
                jobNode = new InvokeOnCompletion(handler);
            }
        }
        jobNode.setJob(this);
        return jobNode;
    }

    private final ChildHandleNode nextChild(LockFreeLinkedListNode $this$nextChild) {
        LockFreeLinkedListNode lockFreeLinkedListNode = $this$nextChild;
        while (lockFreeLinkedListNode.isRemoved()) {
            lockFreeLinkedListNode = lockFreeLinkedListNode.getPrevNode();
        }
        while (true) {
            lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode();
            if (!lockFreeLinkedListNode.isRemoved()) {
                if (lockFreeLinkedListNode instanceof ChildHandleNode) {
                    return (ChildHandleNode) lockFreeLinkedListNode;
                }
                if (lockFreeLinkedListNode instanceof NodeList) {
                    return null;
                }
            }
        }
    }

    private final void notifyCancelling(NodeList list, Throwable cause) {
        Throwable th = cause;
        onCancelling(th);
        LockFreeLinkedListHead lockFreeLinkedListHead = list;
        CompletionHandlerException completionHandlerException = null;
        for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) lockFreeLinkedListHead.getNext(); !Intrinsics.areEqual((Object) lockFreeLinkedListNode, (Object) lockFreeLinkedListHead); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
            if (lockFreeLinkedListNode instanceof JobCancellingNode) {
                JobNode jobNode = (JobNode) lockFreeLinkedListNode;
                try {
                    jobNode.invoke(th);
                } catch (Throwable th2) {
                    Throwable th3 = th2;
                    Throwable th4 = completionHandlerException;
                    if (th4 == null) {
                        th4 = null;
                    } else {
                        ExceptionsKt.addSuppressed(th4, th3);
                    }
                    if (th4 == null) {
                        completionHandlerException = new CompletionHandlerException("Exception in completion handler " + jobNode + " for " + this, th3);
                    }
                }
            }
        }
        Throwable th5 = completionHandlerException;
        if (th5 != null) {
            handleOnCompletionException$kotlinx_coroutines_core(th5);
        }
        cancelParent(th);
    }

    private final void notifyCompletion(NodeList $this$notifyCompletion, Throwable cause) {
        LockFreeLinkedListHead lockFreeLinkedListHead = $this$notifyCompletion;
        CompletionHandlerException completionHandlerException = null;
        for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) lockFreeLinkedListHead.getNext(); !Intrinsics.areEqual((Object) lockFreeLinkedListNode, (Object) lockFreeLinkedListHead); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
            if (lockFreeLinkedListNode instanceof JobNode) {
                JobNode jobNode = (JobNode) lockFreeLinkedListNode;
                try {
                    jobNode.invoke(cause);
                } catch (Throwable th) {
                    Throwable th2 = th;
                    Throwable th3 = completionHandlerException;
                    if (th3 == null) {
                        th3 = null;
                    } else {
                        ExceptionsKt.addSuppressed(th3, th2);
                    }
                    if (th3 == null) {
                        completionHandlerException = new CompletionHandlerException("Exception in completion handler " + jobNode + " for " + this, th2);
                    }
                }
            } else {
                Throwable th4 = cause;
            }
        }
        Throwable th5 = cause;
        Throwable th6 = completionHandlerException;
        if (th6 != null) {
            handleOnCompletionException$kotlinx_coroutines_core(th6);
        }
    }

    private final /* synthetic */ <T extends JobNode> void notifyHandlers(NodeList list, Throwable cause) {
        CompletionHandlerException completionHandlerException = null;
        LockFreeLinkedListHead lockFreeLinkedListHead = list;
        for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) lockFreeLinkedListHead.getNext(); !Intrinsics.areEqual((Object) lockFreeLinkedListNode, (Object) lockFreeLinkedListHead); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
            Intrinsics.reifiedOperationMarker(3, "T");
            if (lockFreeLinkedListNode instanceof LockFreeLinkedListNode) {
                JobNode jobNode = (JobNode) lockFreeLinkedListNode;
                try {
                    jobNode.invoke(cause);
                } catch (Throwable th) {
                    Throwable th2 = completionHandlerException;
                    if (th2 == null) {
                        th2 = null;
                    } else {
                        ExceptionsKt.addSuppressed(th2, th);
                        Throwable th3 = th2;
                    }
                    if (th2 == null) {
                        completionHandlerException = new CompletionHandlerException("Exception in completion handler " + jobNode + " for " + this, th);
                    }
                }
            }
        }
        Throwable th4 = completionHandlerException;
        if (th4 != null) {
            handleOnCompletionException$kotlinx_coroutines_core(th4);
        }
    }

    private final void promoteEmptyToNodeList(Empty state) {
        NodeList nodeList = new NodeList();
        AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, state, state.isActive() ? nodeList : new InactiveNodeList(nodeList));
    }

    private final void promoteSingleToNodeList(JobNode state) {
        state.addOneIfEmpty(new NodeList());
        AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, state, state.getNextNode());
    }

    private final int startInternal(Object state) {
        if (state instanceof Empty) {
            if (((Empty) state).isActive()) {
                return 0;
            }
            if (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, state, JobSupportKt.EMPTY_ACTIVE)) {
                return -1;
            }
            onStart();
            return 1;
        } else if (!(state instanceof InactiveNodeList)) {
            return 0;
        } else {
            if (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, state, ((InactiveNodeList) state).getList())) {
                return -1;
            }
            onStart();
            return 1;
        }
    }

    private final String stateString(Object state) {
        return state instanceof Finishing ? ((Finishing) state).isCancelling() ? "Cancelling" : ((Finishing) state).isCompleting() ? "Completing" : "Active" : state instanceof Incomplete ? ((Incomplete) state).isActive() ? "Active" : "New" : state instanceof CompletedExceptionally ? "Cancelled" : "Completed";
    }

    public static /* synthetic */ CancellationException toCancellationException$default(JobSupport jobSupport, Throwable th, String str, int i, Object obj) {
        if (obj == null) {
            if ((i & 1) != 0) {
                str = null;
            }
            return jobSupport.toCancellationException(th, str);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: toCancellationException");
    }

    private final boolean tryFinalizeSimpleState(Incomplete state, Object update) {
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!((state instanceof Empty) || (state instanceof JobNode))) {
                throw new AssertionError();
            }
        }
        if (DebugKt.getASSERTIONS_ENABLED() && !(!(update instanceof CompletedExceptionally))) {
            throw new AssertionError();
        } else if (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, state, JobSupportKt.boxIncomplete(update))) {
            return false;
        } else {
            onCancelling((Throwable) null);
            onCompletionInternal(update);
            completeStateFinalization(state, update);
            return true;
        }
    }

    private final boolean tryMakeCancelling(Incomplete state, Throwable rootCause) {
        if (DebugKt.getASSERTIONS_ENABLED() && !(!(state instanceof Finishing))) {
            throw new AssertionError();
        } else if (!DebugKt.getASSERTIONS_ENABLED() || state.isActive()) {
            NodeList orPromoteCancellingList = getOrPromoteCancellingList(state);
            if (orPromoteCancellingList == null) {
                return false;
            }
            if (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, state, new Finishing(orPromoteCancellingList, false, rootCause))) {
                return false;
            }
            notifyCancelling(orPromoteCancellingList, rootCause);
            return true;
        } else {
            throw new AssertionError();
        }
    }

    private final Object tryMakeCompleting(Object state, Object proposedUpdate) {
        return !(state instanceof Incomplete) ? JobSupportKt.COMPLETING_ALREADY : (((state instanceof Empty) || (state instanceof JobNode)) && !(state instanceof ChildHandleNode) && !(proposedUpdate instanceof CompletedExceptionally)) ? tryFinalizeSimpleState((Incomplete) state, proposedUpdate) ? proposedUpdate : JobSupportKt.COMPLETING_RETRY : tryMakeCompletingSlowPath((Incomplete) state, proposedUpdate);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:51:0x007f, code lost:
        if (r4 != null) goto L_0x0082;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0082, code lost:
        notifyCancelling(r0, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0087, code lost:
        r2 = firstChild(r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x008b, code lost:
        if (r2 == null) goto L_0x0096;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0091, code lost:
        if (tryWaitForChild(r1, r2, r14) == false) goto L_0x0096;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0095, code lost:
        return kotlinx.coroutines.JobSupportKt.COMPLETING_WAITING_CHILDREN;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x009a, code lost:
        return finalizeFinishingState(r1, r14);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final java.lang.Object tryMakeCompletingSlowPath(kotlinx.coroutines.Incomplete r13, java.lang.Object r14) {
        /*
            r12 = this;
            kotlinx.coroutines.NodeList r0 = r12.getOrPromoteCancellingList(r13)
            if (r0 != 0) goto L_0x000b
            kotlinx.coroutines.internal.Symbol r0 = kotlinx.coroutines.JobSupportKt.COMPLETING_RETRY
            return r0
        L_0x000b:
            boolean r1 = r13 instanceof kotlinx.coroutines.JobSupport.Finishing
            r2 = 0
            if (r1 == 0) goto L_0x0014
            r1 = r13
            kotlinx.coroutines.JobSupport$Finishing r1 = (kotlinx.coroutines.JobSupport.Finishing) r1
            goto L_0x0015
        L_0x0014:
            r1 = r2
        L_0x0015:
            r3 = 0
            if (r1 != 0) goto L_0x001d
            kotlinx.coroutines.JobSupport$Finishing r1 = new kotlinx.coroutines.JobSupport$Finishing
            r1.<init>(r0, r3, r2)
        L_0x001d:
            r4 = 0
            r5 = 0
            monitor-enter(r1)
            r6 = 0
            boolean r7 = r1.isCompleting()     // Catch:{ all -> 0x009b }
            if (r7 == 0) goto L_0x002d
            kotlinx.coroutines.internal.Symbol r2 = kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY     // Catch:{ all -> 0x009b }
            monitor-exit(r1)
            return r2
        L_0x002d:
            r7 = 1
            r1.setCompleting(r7)     // Catch:{ all -> 0x009b }
            if (r1 == r13) goto L_0x0041
            java.util.concurrent.atomic.AtomicReferenceFieldUpdater r8 = _state$FU     // Catch:{ all -> 0x009b }
            boolean r8 = androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(r8, r12, r13, r1)     // Catch:{ all -> 0x009b }
            if (r8 != 0) goto L_0x0041
            kotlinx.coroutines.internal.Symbol r2 = kotlinx.coroutines.JobSupportKt.COMPLETING_RETRY     // Catch:{ all -> 0x009b }
            monitor-exit(r1)
            return r2
        L_0x0041:
            boolean r8 = kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()     // Catch:{ all -> 0x009b }
            if (r8 == 0) goto L_0x0057
            r8 = 0
            boolean r9 = r1.isSealed()     // Catch:{ all -> 0x009b }
            r8 = r9 ^ 1
            if (r8 == 0) goto L_0x0051
            goto L_0x0057
        L_0x0051:
            java.lang.AssertionError r2 = new java.lang.AssertionError     // Catch:{ all -> 0x009b }
            r2.<init>()     // Catch:{ all -> 0x009b }
            throw r2     // Catch:{ all -> 0x009b }
        L_0x0057:
            boolean r8 = r1.isCancelling()     // Catch:{ all -> 0x009b }
            boolean r9 = r14 instanceof kotlinx.coroutines.CompletedExceptionally     // Catch:{ all -> 0x009b }
            if (r9 == 0) goto L_0x0063
            r9 = r14
            kotlinx.coroutines.CompletedExceptionally r9 = (kotlinx.coroutines.CompletedExceptionally) r9     // Catch:{ all -> 0x009b }
            goto L_0x0064
        L_0x0063:
            r9 = r2
        L_0x0064:
            if (r9 != 0) goto L_0x0067
            goto L_0x006d
        L_0x0067:
            r10 = 0
            java.lang.Throwable r11 = r9.cause     // Catch:{ all -> 0x009b }
            r1.addExceptionLocked(r11)     // Catch:{ all -> 0x009b }
        L_0x006d:
            java.lang.Throwable r9 = r1.getRootCause()     // Catch:{ all -> 0x009b }
            r10 = r9
            r11 = 0
            if (r8 != 0) goto L_0x0076
            r3 = r7
        L_0x0076:
            if (r3 == 0) goto L_0x0079
            r2 = r9
        L_0x0079:
            r4 = r2
            kotlin.Unit r2 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x009b }
            monitor-exit(r1)
            if (r4 != 0) goto L_0x0082
            goto L_0x0087
        L_0x0082:
            r2 = r4
            r3 = 0
            r12.notifyCancelling(r0, r2)
        L_0x0087:
            kotlinx.coroutines.ChildHandleNode r2 = r12.firstChild(r13)
            if (r2 == 0) goto L_0x0096
            boolean r3 = r12.tryWaitForChild(r1, r2, r14)
            if (r3 == 0) goto L_0x0096
            kotlinx.coroutines.internal.Symbol r3 = kotlinx.coroutines.JobSupportKt.COMPLETING_WAITING_CHILDREN
            return r3
        L_0x0096:
            java.lang.Object r3 = r12.finalizeFinishingState(r1, r14)
            return r3
        L_0x009b:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.JobSupport.tryMakeCompletingSlowPath(kotlinx.coroutines.Incomplete, java.lang.Object):java.lang.Object");
    }

    private final boolean tryWaitForChild(Finishing state, ChildHandleNode child, Object proposedUpdate) {
        ChildHandleNode childHandleNode = child;
        while (Job.DefaultImpls.invokeOnCompletion$default(childHandleNode.childJob, false, false, new ChildCompletion(this, state, childHandleNode, proposedUpdate), 1, (Object) null) == NonDisposableHandle.INSTANCE) {
            childHandleNode = nextChild(childHandleNode);
            if (childHandleNode == null) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void afterCompletion(Object state) {
    }

    public final ChildHandle attachChild(ChildJob child) {
        return (ChildHandle) Job.DefaultImpls.invokeOnCompletion$default(this, true, false, new ChildHandleNode(child), 2, (Object) null);
    }

    public final Object awaitInternal$kotlinx_coroutines_core(Continuation<Object> $completion) {
        Object state$kotlinx_coroutines_core;
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                if (!(state$kotlinx_coroutines_core instanceof CompletedExceptionally)) {
                    return JobSupportKt.unboxState(state$kotlinx_coroutines_core);
                }
                Throwable th = ((CompletedExceptionally) state$kotlinx_coroutines_core).cause;
                if (DebugKt.getRECOVER_STACK_TRACES()) {
                    Continuation<Object> continuation = $completion;
                    if (!(continuation instanceof CoroutineStackFrame)) {
                        throw th;
                    }
                    throw StackTraceRecoveryKt.recoverFromStackFrame(th, (CoroutineStackFrame) continuation);
                }
                throw th;
            }
        } while (startInternal(state$kotlinx_coroutines_core) < 0);
        return awaitSuspend($completion);
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Since 1.2.0, binary compatibility with versions <= 1.1.x")
    public /* synthetic */ void cancel() {
        cancel((CancellationException) null);
    }

    public final boolean cancelCoroutine(Throwable cause) {
        return cancelImpl$kotlinx_coroutines_core(cause);
    }

    public final boolean cancelImpl$kotlinx_coroutines_core(Object cause) {
        Object access$getCOMPLETING_ALREADY$p = JobSupportKt.COMPLETING_ALREADY;
        if (getOnCancelComplete$kotlinx_coroutines_core() && (access$getCOMPLETING_ALREADY$p = cancelMakeCompleting(cause)) == JobSupportKt.COMPLETING_WAITING_CHILDREN) {
            return true;
        }
        if (access$getCOMPLETING_ALREADY$p == JobSupportKt.COMPLETING_ALREADY) {
            access$getCOMPLETING_ALREADY$p = makeCancelling(cause);
        }
        if (access$getCOMPLETING_ALREADY$p == JobSupportKt.COMPLETING_ALREADY || access$getCOMPLETING_ALREADY$p == JobSupportKt.COMPLETING_WAITING_CHILDREN) {
            return true;
        }
        if (access$getCOMPLETING_ALREADY$p == JobSupportKt.TOO_LATE_TO_CANCEL) {
            return false;
        }
        afterCompletion(access$getCOMPLETING_ALREADY$p);
        return true;
    }

    public void cancelInternal(Throwable cause) {
        cancelImpl$kotlinx_coroutines_core(cause);
    }

    /* access modifiers changed from: protected */
    public String cancellationExceptionMessage() {
        return "Job was cancelled";
    }

    public boolean childCancelled(Throwable cause) {
        if (cause instanceof CancellationException) {
            return true;
        }
        return cancelImpl$kotlinx_coroutines_core(cause) && getHandlesException$kotlinx_coroutines_core();
    }

    public <R> R fold(R initial, Function2<? super R, ? super CoroutineContext.Element, ? extends R> operation) {
        return Job.DefaultImpls.fold(this, initial, operation);
    }

    public <E extends CoroutineContext.Element> E get(CoroutineContext.Key<E> key) {
        return Job.DefaultImpls.get(this, key);
    }

    public final Sequence<Job> getChildren() {
        return SequencesKt.sequence(new JobSupport$children$1(this, (Continuation<? super JobSupport$children$1>) null));
    }

    public final Object getCompletedInternal$kotlinx_coroutines_core() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (!(!(state$kotlinx_coroutines_core instanceof Incomplete))) {
            throw new IllegalStateException("This job has not completed yet".toString());
        } else if (!(state$kotlinx_coroutines_core instanceof CompletedExceptionally)) {
            return JobSupportKt.unboxState(state$kotlinx_coroutines_core);
        } else {
            throw ((CompletedExceptionally) state$kotlinx_coroutines_core).cause;
        }
    }

    /* access modifiers changed from: protected */
    public final boolean getCompletionCauseHandled() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        return (state$kotlinx_coroutines_core instanceof CompletedExceptionally) && ((CompletedExceptionally) state$kotlinx_coroutines_core).getHandled();
    }

    public final Throwable getCompletionExceptionOrNull() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
            return getExceptionOrNull(state$kotlinx_coroutines_core);
        }
        throw new IllegalStateException("This job has not completed yet".toString());
    }

    public boolean getHandlesException$kotlinx_coroutines_core() {
        return true;
    }

    public final CoroutineContext.Key<?> getKey() {
        return Job.Key;
    }

    public boolean getOnCancelComplete$kotlinx_coroutines_core() {
        return false;
    }

    public final SelectClause0 getOnJoin() {
        return this;
    }

    public final ChildHandle getParentHandle$kotlinx_coroutines_core() {
        return (ChildHandle) this._parentHandle;
    }

    public final Object getState$kotlinx_coroutines_core() {
        while (true) {
            Object obj = this._state;
            if (!(obj instanceof OpDescriptor)) {
                return obj;
            }
            ((OpDescriptor) obj).perform(this);
        }
    }

    /* access modifiers changed from: protected */
    public boolean handleJobException(Throwable exception) {
        return false;
    }

    public void handleOnCompletionException$kotlinx_coroutines_core(Throwable exception) {
        throw exception;
    }

    /* access modifiers changed from: protected */
    public final void initParentJob(Job parent) {
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(getParentHandle$kotlinx_coroutines_core() == null)) {
                throw new AssertionError();
            }
        }
        if (parent == null) {
            setParentHandle$kotlinx_coroutines_core(NonDisposableHandle.INSTANCE);
            return;
        }
        parent.start();
        ChildHandle attachChild = parent.attachChild(this);
        setParentHandle$kotlinx_coroutines_core(attachChild);
        if (isCompleted()) {
            attachChild.dispose();
            setParentHandle$kotlinx_coroutines_core(NonDisposableHandle.INSTANCE);
        }
    }

    public final DisposableHandle invokeOnCompletion(Function1<? super Throwable, Unit> handler) {
        return invokeOnCompletion(false, true, handler);
    }

    public final DisposableHandle invokeOnCompletion(boolean onCancelling, boolean invokeImmediately, Function1<? super Throwable, Unit> handler) {
        boolean z = onCancelling;
        JobNode makeNode = makeNode(handler, z);
        while (true) {
            Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (state$kotlinx_coroutines_core instanceof Empty) {
                if (!((Empty) state$kotlinx_coroutines_core).isActive()) {
                    promoteEmptyToNodeList((Empty) state$kotlinx_coroutines_core);
                } else if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, state$kotlinx_coroutines_core, makeNode)) {
                    return makeNode;
                }
            } else if (state$kotlinx_coroutines_core instanceof Incomplete) {
                NodeList list = ((Incomplete) state$kotlinx_coroutines_core).getList();
                if (list != null) {
                    Throwable th = null;
                    DisposableHandle disposableHandle = NonDisposableHandle.INSTANCE;
                    if (z && (state$kotlinx_coroutines_core instanceof Finishing)) {
                        synchronized (state$kotlinx_coroutines_core) {
                            th = ((Finishing) state$kotlinx_coroutines_core).getRootCause();
                            if (th == null || ((handler instanceof ChildHandleNode) && !((Finishing) state$kotlinx_coroutines_core).isCompleting())) {
                                if (addLastAtomic(state$kotlinx_coroutines_core, list, makeNode)) {
                                    if (th == null) {
                                        DisposableHandle disposableHandle2 = makeNode;
                                        return disposableHandle2;
                                    }
                                    disposableHandle = makeNode;
                                }
                            }
                            Unit unit = Unit.INSTANCE;
                        }
                    }
                    if (th != null) {
                        if (invokeImmediately) {
                            handler.invoke(th);
                        }
                        return disposableHandle;
                    } else if (addLastAtomic(state$kotlinx_coroutines_core, list, makeNode)) {
                        return makeNode;
                    }
                } else if (state$kotlinx_coroutines_core != null) {
                    promoteSingleToNodeList((JobNode) state$kotlinx_coroutines_core);
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type kotlinx.coroutines.JobNode");
                }
            } else {
                if (invokeImmediately) {
                    Throwable th2 = null;
                    CompletedExceptionally completedExceptionally = state$kotlinx_coroutines_core instanceof CompletedExceptionally ? (CompletedExceptionally) state$kotlinx_coroutines_core : null;
                    if (completedExceptionally != null) {
                        th2 = completedExceptionally.cause;
                    }
                    handler.invoke(th2);
                }
                return NonDisposableHandle.INSTANCE;
            }
        }
    }

    public boolean isActive() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        return (state$kotlinx_coroutines_core instanceof Incomplete) && ((Incomplete) state$kotlinx_coroutines_core).isActive();
    }

    public final boolean isCancelled() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        return (state$kotlinx_coroutines_core instanceof CompletedExceptionally) || ((state$kotlinx_coroutines_core instanceof Finishing) && ((Finishing) state$kotlinx_coroutines_core).isCancelling());
    }

    public final boolean isCompleted() {
        return !(getState$kotlinx_coroutines_core() instanceof Incomplete);
    }

    public final boolean isCompletedExceptionally() {
        return getState$kotlinx_coroutines_core() instanceof CompletedExceptionally;
    }

    /* access modifiers changed from: protected */
    public boolean isScopedCoroutine() {
        return false;
    }

    public final Object join(Continuation<? super Unit> $completion) {
        if (!joinInternal()) {
            JobKt.ensureActive($completion.getContext());
            return Unit.INSTANCE;
        }
        Object joinSuspend = joinSuspend($completion);
        return joinSuspend == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? joinSuspend : Unit.INSTANCE;
    }

    public final boolean makeCompleting$kotlinx_coroutines_core(Object proposedUpdate) {
        Object tryMakeCompleting;
        do {
            tryMakeCompleting = tryMakeCompleting(getState$kotlinx_coroutines_core(), proposedUpdate);
            if (tryMakeCompleting == JobSupportKt.COMPLETING_ALREADY) {
                return false;
            }
            if (tryMakeCompleting == JobSupportKt.COMPLETING_WAITING_CHILDREN) {
                return true;
            }
        } while (tryMakeCompleting == JobSupportKt.COMPLETING_RETRY);
        afterCompletion(tryMakeCompleting);
        return true;
    }

    public final Object makeCompletingOnce$kotlinx_coroutines_core(Object proposedUpdate) {
        Object tryMakeCompleting;
        do {
            tryMakeCompleting = tryMakeCompleting(getState$kotlinx_coroutines_core(), proposedUpdate);
            if (tryMakeCompleting == JobSupportKt.COMPLETING_ALREADY) {
                throw new IllegalStateException("Job " + this + " is already complete or completing, but is being completed with " + proposedUpdate, getExceptionOrNull(proposedUpdate));
            }
        } while (tryMakeCompleting == JobSupportKt.COMPLETING_RETRY);
        return tryMakeCompleting;
    }

    public CoroutineContext minusKey(CoroutineContext.Key<?> key) {
        return Job.DefaultImpls.minusKey(this, key);
    }

    /* access modifiers changed from: protected */
    public void onCancelling(Throwable cause) {
    }

    /* access modifiers changed from: protected */
    public void onCompletionInternal(Object state) {
    }

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    public final void parentCancelled(ParentJob parentJob) {
        cancelImpl$kotlinx_coroutines_core(parentJob);
    }

    public CoroutineContext plus(CoroutineContext context) {
        return Job.DefaultImpls.plus((Job) this, context);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Operator '+' on two Job objects is meaningless. Job is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The job to the right of `+` just replaces the job the left of `+`.")
    public Job plus(Job other) {
        return Job.DefaultImpls.plus((Job) this, other);
    }

    public final <R> void registerSelectClause0(SelectInstance<? super R> select, Function1<? super Continuation<? super R>, ? extends Object> block) {
        Object state$kotlinx_coroutines_core;
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!select.isSelected()) {
                if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                    if (select.trySelect()) {
                        UndispatchedKt.startCoroutineUnintercepted(block, select.getCompletion());
                        return;
                    }
                    return;
                }
            } else {
                return;
            }
        } while (startInternal(state$kotlinx_coroutines_core) != 0);
        select.disposeOnSelect(invokeOnCompletion(new SelectJoinOnCompletion(select, block)));
    }

    public final <T, R> void registerSelectClause1Internal$kotlinx_coroutines_core(SelectInstance<? super R> select, Function2<? super T, ? super Continuation<? super R>, ? extends Object> block) {
        Object state$kotlinx_coroutines_core;
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!select.isSelected()) {
                if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                    if (!select.trySelect()) {
                        return;
                    }
                    if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
                        select.resumeSelectWithException(((CompletedExceptionally) state$kotlinx_coroutines_core).cause);
                        return;
                    } else {
                        UndispatchedKt.startCoroutineUnintercepted(block, JobSupportKt.unboxState(state$kotlinx_coroutines_core), select.getCompletion());
                        return;
                    }
                }
            } else {
                return;
            }
        } while (startInternal(state$kotlinx_coroutines_core) != 0);
        select.disposeOnSelect(invokeOnCompletion(new SelectAwaitOnCompletion(select, block)));
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x001f A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:3:0x000d A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void removeNode$kotlinx_coroutines_core(kotlinx.coroutines.JobNode r7) {
        /*
            r6 = this;
            r0 = r6
            r1 = 0
        L_0x0002:
            java.lang.Object r2 = r0.getState$kotlinx_coroutines_core()
            r3 = 0
            boolean r4 = r2 instanceof kotlinx.coroutines.JobNode
            if (r4 == 0) goto L_0x001f
            if (r2 == r7) goto L_0x0010
            return
        L_0x0010:
            java.util.concurrent.atomic.AtomicReferenceFieldUpdater r4 = _state$FU
            kotlinx.coroutines.Empty r5 = kotlinx.coroutines.JobSupportKt.EMPTY_ACTIVE
            boolean r4 = androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(r4, r6, r2, r5)
            if (r4 == 0) goto L_0x001d
            return
        L_0x001d:
            goto L_0x0002
        L_0x001f:
            boolean r4 = r2 instanceof kotlinx.coroutines.Incomplete
            if (r4 == 0) goto L_0x0030
            r4 = r2
            kotlinx.coroutines.Incomplete r4 = (kotlinx.coroutines.Incomplete) r4
            kotlinx.coroutines.NodeList r4 = r4.getList()
            if (r4 == 0) goto L_0x002f
            r7.remove()
        L_0x002f:
            return
        L_0x0030:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.JobSupport.removeNode$kotlinx_coroutines_core(kotlinx.coroutines.JobNode):void");
    }

    public final <T, R> void selectAwaitCompletion$kotlinx_coroutines_core(SelectInstance<? super R> select, Function2<? super T, ? super Continuation<? super R>, ? extends Object> block) {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            select.resumeSelectWithException(((CompletedExceptionally) state$kotlinx_coroutines_core).cause);
            return;
        }
        CancellableKt.startCoroutineCancellable$default(block, JobSupportKt.unboxState(state$kotlinx_coroutines_core), select.getCompletion(), (Function1) null, 4, (Object) null);
    }

    public final void setParentHandle$kotlinx_coroutines_core(ChildHandle value) {
        this._parentHandle = value;
    }

    /* JADX WARNING: Removed duplicated region for block: B:1:0x0002 A[LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean start() {
        /*
            r5 = this;
            r0 = r5
            r1 = 0
        L_0x0002:
            java.lang.Object r2 = r0.getState$kotlinx_coroutines_core()
            r3 = 0
            int r4 = r5.startInternal(r2)
            switch(r4) {
                case 0: goto L_0x0013;
                case 1: goto L_0x0011;
                default: goto L_0x000f;
            }
        L_0x000f:
            goto L_0x0002
        L_0x0011:
            r4 = 1
            return r4
        L_0x0013:
            r4 = 0
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.JobSupport.start():boolean");
    }

    public final String toDebugString() {
        return nameString$kotlinx_coroutines_core() + '{' + stateString(getState$kotlinx_coroutines_core()) + '}';
    }

    public static /* synthetic */ JobCancellationException defaultCancellationException$kotlinx_coroutines_core$default(JobSupport jobSupport, String message, Throwable cause, int i, Object obj) {
        String str;
        if (obj == null) {
            if ((i & 1) != 0) {
                message = null;
            }
            if ((i & 2) != 0) {
                cause = null;
            }
            if (message == null) {
                str = jobSupport.cancellationExceptionMessage();
                Log1F380D.a((Object) str);
            } else {
                str = message;
            }
            return new JobCancellationException(str, cause, jobSupport);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: defaultCancellationException");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: java.lang.Throwable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.lang.Throwable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v1, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v0, resolved type: java.lang.Throwable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.lang.Throwable} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final java.lang.Throwable getFinalRootCause(kotlinx.coroutines.JobSupport.Finishing r13, java.util.List<? extends java.lang.Throwable> r14) {
        /*
            r12 = this;
            boolean r0 = r14.isEmpty()
            r1 = 0
            if (r0 == 0) goto L_0x0023
            boolean r0 = r13.isCancelling()
            if (r0 == 0) goto L_0x0022
            r0 = 0
            r1 = 0
            r2 = 0
            kotlinx.coroutines.JobCancellationException r3 = new kotlinx.coroutines.JobCancellationException
            java.lang.String r4 = r12.cancellationExceptionMessage()
            mt.Log1F380D.a((java.lang.Object) r4)
            r5 = r12
            kotlinx.coroutines.Job r5 = (kotlinx.coroutines.Job) r5
            r3.<init>(r4, r1, r5)
            java.lang.Throwable r3 = (java.lang.Throwable) r3
            return r3
        L_0x0022:
            return r1
        L_0x0023:
            r0 = r14
            java.lang.Iterable r0 = (java.lang.Iterable) r0
            r2 = 0
            java.util.Iterator r3 = r0.iterator()
        L_0x002b:
            boolean r4 = r3.hasNext()
            r5 = 1
            if (r4 == 0) goto L_0x0041
            java.lang.Object r4 = r3.next()
            r6 = r4
            java.lang.Throwable r6 = (java.lang.Throwable) r6
            r7 = 0
            boolean r8 = r6 instanceof java.util.concurrent.CancellationException
            r6 = r8 ^ 1
            if (r6 == 0) goto L_0x002b
            goto L_0x0042
        L_0x0041:
            r4 = r1
        L_0x0042:
            r0 = r4
            java.lang.Throwable r0 = (java.lang.Throwable) r0
            if (r0 == 0) goto L_0x0048
            return r0
        L_0x0048:
            r2 = 0
            java.lang.Object r3 = r14.get(r2)
            java.lang.Throwable r3 = (java.lang.Throwable) r3
            boolean r4 = r3 instanceof kotlinx.coroutines.TimeoutCancellationException
            if (r4 == 0) goto L_0x007c
            r4 = r14
            java.lang.Iterable r4 = (java.lang.Iterable) r4
            r6 = 0
            java.util.Iterator r7 = r4.iterator()
        L_0x005b:
            boolean r8 = r7.hasNext()
            if (r8 == 0) goto L_0x0076
            java.lang.Object r8 = r7.next()
            r9 = r8
            java.lang.Throwable r9 = (java.lang.Throwable) r9
            r10 = 0
            if (r9 == r3) goto L_0x0071
            boolean r11 = r9 instanceof kotlinx.coroutines.TimeoutCancellationException
            if (r11 == 0) goto L_0x0071
            r9 = r5
            goto L_0x0072
        L_0x0071:
            r9 = r2
        L_0x0072:
            if (r9 == 0) goto L_0x005b
            r1 = r8
            goto L_0x0077
        L_0x0076:
        L_0x0077:
            java.lang.Throwable r1 = (java.lang.Throwable) r1
            if (r1 == 0) goto L_0x007c
            return r1
        L_0x007c:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.JobSupport.getFinalRootCause(kotlinx.coroutines.JobSupport$Finishing, java.util.List):java.lang.Throwable");
    }

    private final NodeList getOrPromoteCancellingList(Incomplete state) {
        NodeList list = state.getList();
        if (list != null) {
            return list;
        }
        if (state instanceof Empty) {
            return new NodeList();
        }
        if (state instanceof JobNode) {
            promoteSingleToNodeList((JobNode) state);
            NodeList nodeList = null;
            return null;
        }
        String stringPlus = Intrinsics.stringPlus("State should have list: ", state);
        Log1F380D.a((Object) stringPlus);
        throw new IllegalStateException(stringPlus.toString());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0052, code lost:
        r5 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0054, code lost:
        if (r5 != null) goto L_0x0057;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0057, code lost:
        notifyCancelling(((kotlinx.coroutines.JobSupport.Finishing) r3).getList(), r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0067, code lost:
        return kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final java.lang.Object makeCancelling(java.lang.Object r15) {
        /*
            r14 = this;
            r0 = 0
            r1 = r14
            r2 = 0
        L_0x0003:
            java.lang.Object r3 = r1.getState$kotlinx_coroutines_core()
            r4 = 0
            boolean r5 = r3 instanceof kotlinx.coroutines.JobSupport.Finishing
            r6 = 0
            r7 = 0
            if (r5 == 0) goto L_0x006e
            r5 = 0
            monitor-enter(r3)
            r8 = 0
            r9 = r3
            kotlinx.coroutines.JobSupport$Finishing r9 = (kotlinx.coroutines.JobSupport.Finishing) r9     // Catch:{ all -> 0x006b }
            boolean r9 = r9.isSealed()     // Catch:{ all -> 0x006b }
            if (r9 == 0) goto L_0x0022
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.JobSupportKt.TOO_LATE_TO_CANCEL     // Catch:{ all -> 0x006b }
            monitor-exit(r3)
            return r6
        L_0x0022:
            r9 = r3
            kotlinx.coroutines.JobSupport$Finishing r9 = (kotlinx.coroutines.JobSupport.Finishing) r9     // Catch:{ all -> 0x006b }
            boolean r9 = r9.isCancelling()     // Catch:{ all -> 0x006b }
            if (r15 != 0) goto L_0x002d
            if (r9 != 0) goto L_0x0042
        L_0x002d:
            if (r0 != 0) goto L_0x003a
            java.lang.Throwable r10 = r14.createCauseException(r15)     // Catch:{ all -> 0x006b }
            r11 = r10
            r12 = 0
            r0 = r11
            r13 = r10
            r10 = r0
            r0 = r13
            goto L_0x003b
        L_0x003a:
            r10 = r0
        L_0x003b:
            r11 = r3
            kotlinx.coroutines.JobSupport$Finishing r11 = (kotlinx.coroutines.JobSupport.Finishing) r11     // Catch:{ all -> 0x0068 }
            r11.addExceptionLocked(r0)     // Catch:{ all -> 0x0068 }
            r0 = r10
        L_0x0042:
            r10 = r3
            kotlinx.coroutines.JobSupport$Finishing r10 = (kotlinx.coroutines.JobSupport.Finishing) r10     // Catch:{ all -> 0x006b }
            java.lang.Throwable r10 = r10.getRootCause()     // Catch:{ all -> 0x006b }
            r11 = r10
            r12 = 0
            if (r9 != 0) goto L_0x004e
            r7 = 1
        L_0x004e:
            if (r7 == 0) goto L_0x0051
            r6 = r10
        L_0x0051:
            monitor-exit(r3)
            r5 = r6
            if (r5 != 0) goto L_0x0057
            goto L_0x0063
        L_0x0057:
            r6 = r5
            r7 = 0
            r8 = r3
            kotlinx.coroutines.JobSupport$Finishing r8 = (kotlinx.coroutines.JobSupport.Finishing) r8
            kotlinx.coroutines.NodeList r8 = r8.getList()
            r14.notifyCancelling(r8, r6)
        L_0x0063:
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY
            return r6
        L_0x0068:
            r6 = move-exception
            r0 = r10
            goto L_0x006c
        L_0x006b:
            r6 = move-exception
        L_0x006c:
            monitor-exit(r3)
            throw r6
        L_0x006e:
            boolean r5 = r3 instanceof kotlinx.coroutines.Incomplete
            if (r5 == 0) goto L_0x00c7
            if (r0 != 0) goto L_0x007f
            java.lang.Throwable r5 = r14.createCauseException(r15)
            r8 = r5
            r9 = 0
            r0 = r8
            r13 = r5
            r5 = r0
            r0 = r13
            goto L_0x0080
        L_0x007f:
            r5 = r0
        L_0x0080:
            r8 = r3
            kotlinx.coroutines.Incomplete r8 = (kotlinx.coroutines.Incomplete) r8
            boolean r8 = r8.isActive()
            if (r8 == 0) goto L_0x0099
            r6 = r3
            kotlinx.coroutines.Incomplete r6 = (kotlinx.coroutines.Incomplete) r6
            boolean r6 = r14.tryMakeCancelling(r6, r0)
            if (r6 == 0) goto L_0x0097
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY
            return r6
        L_0x0097:
            goto L_0x00b0
        L_0x0099:
            kotlinx.coroutines.CompletedExceptionally r8 = new kotlinx.coroutines.CompletedExceptionally
            r9 = 2
            r8.<init>(r0, r7, r9, r6)
            java.lang.Object r6 = r14.tryMakeCompleting(r3, r8)
            kotlinx.coroutines.internal.Symbol r7 = kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY
            if (r6 == r7) goto L_0x00b4
            kotlinx.coroutines.internal.Symbol r7 = kotlinx.coroutines.JobSupportKt.COMPLETING_RETRY
            if (r6 != r7) goto L_0x00b3
        L_0x00b0:
            r0 = r5
            goto L_0x0003
        L_0x00b3:
            return r6
        L_0x00b4:
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
            java.lang.String r8 = "Cannot happen in "
            java.lang.String r8 = kotlin.jvm.internal.Intrinsics.stringPlus(r8, r3)
            mt.Log1F380D.a((java.lang.Object) r8)
            java.lang.String r8 = r8.toString()
            r7.<init>(r8)
            throw r7
        L_0x00c7:
            kotlinx.coroutines.internal.Symbol r5 = kotlinx.coroutines.JobSupportKt.TOO_LATE_TO_CANCEL
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.JobSupport.makeCancelling(java.lang.Object):java.lang.Object");
    }

    public void cancel(CancellationException cause) {
        CancellationException cancellationException;
        if (cause == null) {
            String access$cancellationExceptionMessage = cancellationExceptionMessage();
            Log1F380D.a((Object) access$cancellationExceptionMessage);
            cancellationException = new JobCancellationException(access$cancellationExceptionMessage, (Throwable) null, this);
        } else {
            cancellationException = cause;
        }
        cancelInternal(cancellationException);
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Added since 1.2.0 for binary compatibility with versions <= 1.1.x")
    public /* synthetic */ boolean cancel(Throwable cause) {
        CancellationException cancellationException = null;
        if (cause != null) {
            cancellationException = toCancellationException$default(this, cause, (String) null, 1, (Object) null);
        }
        if (cancellationException == null) {
            String access$cancellationExceptionMessage = cancellationExceptionMessage();
            Log1F380D.a((Object) access$cancellationExceptionMessage);
            cancellationException = new JobCancellationException(access$cancellationExceptionMessage, (Throwable) null, this);
        }
        cancelInternal(cancellationException);
        return true;
    }

    public final JobCancellationException defaultCancellationException$kotlinx_coroutines_core(String message, Throwable cause) {
        String str;
        if (message == null) {
            str = cancellationExceptionMessage();
            Log1F380D.a((Object) str);
        } else {
            str = message;
        }
        return new JobCancellationException(str, cause, this);
    }

    public final CancellationException getCancellationException() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        CancellationException cancellationException = null;
        if (state$kotlinx_coroutines_core instanceof Finishing) {
            Throwable rootCause = ((Finishing) state$kotlinx_coroutines_core).getRootCause();
            if (rootCause != null) {
                String classSimpleName = DebugStringsKt.getClassSimpleName(this);
                Log1F380D.a((Object) classSimpleName);
                String stringPlus = Intrinsics.stringPlus(classSimpleName, " is cancelling");
                Log1F380D.a((Object) stringPlus);
                cancellationException = toCancellationException(rootCause, stringPlus);
            }
            if (cancellationException != null) {
                return cancellationException;
            }
            String stringPlus2 = Intrinsics.stringPlus("Job is still new or active: ", this);
            Log1F380D.a((Object) stringPlus2);
            throw new IllegalStateException(stringPlus2.toString());
        } else if (state$kotlinx_coroutines_core instanceof Incomplete) {
            String stringPlus3 = Intrinsics.stringPlus("Job is still new or active: ", this);
            Log1F380D.a((Object) stringPlus3);
            throw new IllegalStateException(stringPlus3.toString());
        } else if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            return toCancellationException$default(this, ((CompletedExceptionally) state$kotlinx_coroutines_core).cause, (String) null, 1, (Object) null);
        } else {
            String classSimpleName2 = DebugStringsKt.getClassSimpleName(this);
            Log1F380D.a((Object) classSimpleName2);
            String stringPlus4 = Intrinsics.stringPlus(classSimpleName2, " has completed normally");
            Log1F380D.a((Object) stringPlus4);
            return new JobCancellationException(stringPlus4, (Throwable) null, this);
        }
    }

    public CancellationException getChildJobCancellationCause() {
        Throwable th;
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        CancellationException cancellationException = null;
        if (state$kotlinx_coroutines_core instanceof Finishing) {
            th = ((Finishing) state$kotlinx_coroutines_core).getRootCause();
        } else if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            th = ((CompletedExceptionally) state$kotlinx_coroutines_core).cause;
        } else if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
            th = null;
        } else {
            String stringPlus = Intrinsics.stringPlus("Cannot be cancelling child in this state: ", state$kotlinx_coroutines_core);
            Log1F380D.a((Object) stringPlus);
            throw new IllegalStateException(stringPlus.toString());
        }
        if (th instanceof CancellationException) {
            cancellationException = (CancellationException) th;
        }
        if (cancellationException != null) {
            return cancellationException;
        }
        String stringPlus2 = Intrinsics.stringPlus("Parent job is ", stateString(state$kotlinx_coroutines_core));
        Log1F380D.a((Object) stringPlus2);
        return new JobCancellationException(stringPlus2, th, this);
    }

    /* access modifiers changed from: protected */
    public final Throwable getCompletionCause() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (state$kotlinx_coroutines_core instanceof Finishing) {
            Throwable rootCause = ((Finishing) state$kotlinx_coroutines_core).getRootCause();
            if (rootCause != null) {
                return rootCause;
            }
            String stringPlus = Intrinsics.stringPlus("Job is still new or active: ", this);
            Log1F380D.a((Object) stringPlus);
            throw new IllegalStateException(stringPlus.toString());
        } else if (state$kotlinx_coroutines_core instanceof Incomplete) {
            String stringPlus2 = Intrinsics.stringPlus("Job is still new or active: ", this);
            Log1F380D.a((Object) stringPlus2);
            throw new IllegalStateException(stringPlus2.toString());
        } else if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            return ((CompletedExceptionally) state$kotlinx_coroutines_core).cause;
        } else {
            return null;
        }
    }

    public String nameString$kotlinx_coroutines_core() {
        String classSimpleName = DebugStringsKt.getClassSimpleName(this);
        Log1F380D.a((Object) classSimpleName);
        return classSimpleName;
    }

    /* access modifiers changed from: protected */
    public final CancellationException toCancellationException(Throwable $this$toCancellationException, String message) {
        String str;
        CancellationException cancellationException = $this$toCancellationException instanceof CancellationException ? (CancellationException) $this$toCancellationException : null;
        if (cancellationException != null) {
            return cancellationException;
        }
        if (message == null) {
            str = cancellationExceptionMessage();
            Log1F380D.a((Object) str);
        } else {
            str = message;
        }
        return new JobCancellationException(str, $this$toCancellationException, this);
    }

    public String toString() {
        StringBuilder append = new StringBuilder().append(toDebugString()).append('@');
        String hexAddress = DebugStringsKt.getHexAddress(this);
        Log1F380D.a((Object) hexAddress);
        return append.append(hexAddress).toString();
    }
}
