package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CompletableDeferred;
import kotlinx.coroutines.CompletableDeferredKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.channels.BufferOverflow;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.flow.internal.ChannelFlow;

@Metadata(d1 = {"\u0000j\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003\u001a\u001c\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006\u001a+\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002¢\u0006\u0002\b\f\u001aM\u0010\r\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002*\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00020\t2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u0002H\u0002H\u0002¢\u0006\u0004\b\u0017\u0010\u0018\u001aA\u0010\u0019\u001a\u00020\u001a\"\u0004\b\u0000\u0010\u0002*\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00020\t2\u0012\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00050\u001cH\u0002¢\u0006\u0002\b\u001d\u001aS\u0010\u001e\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012-\u0010\u001f\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020!\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\"\u0012\u0006\u0012\u0004\u0018\u00010#0 ¢\u0006\u0002\b$ø\u0001\u0000¢\u0006\u0002\u0010%\u001a6\u0010&\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\t2\u0006\u0010'\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\n\u001a\u00020\u000b\u001a/\u0010(\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\t2\u0006\u0010'\u001a\u00020\u000fH@ø\u0001\u0000¢\u0006\u0002\u0010)\u001a9\u0010(\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\t2\u0006\u0010'\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u0002H\u0002¢\u0006\u0002\u0010*\u0002\u0004\n\u0002\b\u0019¨\u0006+"}, d2 = {"asSharedFlow", "Lkotlinx/coroutines/flow/SharedFlow;", "T", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "asStateFlow", "Lkotlinx/coroutines/flow/StateFlow;", "Lkotlinx/coroutines/flow/MutableStateFlow;", "configureSharing", "Lkotlinx/coroutines/flow/SharingConfig;", "Lkotlinx/coroutines/flow/Flow;", "replay", "", "configureSharing$FlowKt__ShareKt", "launchSharing", "Lkotlinx/coroutines/Job;", "Lkotlinx/coroutines/CoroutineScope;", "context", "Lkotlin/coroutines/CoroutineContext;", "upstream", "shared", "started", "Lkotlinx/coroutines/flow/SharingStarted;", "initialValue", "launchSharing$FlowKt__ShareKt", "(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/CoroutineContext;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/MutableSharedFlow;Lkotlinx/coroutines/flow/SharingStarted;Ljava/lang/Object;)Lkotlinx/coroutines/Job;", "launchSharingDeferred", "", "result", "Lkotlinx/coroutines/CompletableDeferred;", "launchSharingDeferred$FlowKt__ShareKt", "onSubscription", "action", "Lkotlin/Function2;", "Lkotlinx/coroutines/flow/FlowCollector;", "Lkotlin/coroutines/Continuation;", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/flow/SharedFlow;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/flow/SharedFlow;", "shareIn", "scope", "stateIn", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/CoroutineScope;Lkotlinx/coroutines/flow/SharingStarted;Ljava/lang/Object;)Lkotlinx/coroutines/flow/StateFlow;", "kotlinx-coroutines-core"}, k = 5, mv = {1, 6, 0}, xi = 48, xs = "kotlinx/coroutines/flow/FlowKt")
/* compiled from: Share.kt */
final /* synthetic */ class FlowKt__ShareKt {
    public static final <T> SharedFlow<T> asSharedFlow(MutableSharedFlow<T> $this$asSharedFlow) {
        return new ReadonlySharedFlow<>($this$asSharedFlow, (Job) null);
    }

    public static final <T> StateFlow<T> asStateFlow(MutableStateFlow<T> $this$asStateFlow) {
        return new ReadonlyStateFlow<>($this$asStateFlow, (Job) null);
    }

    private static final <T> SharingConfig<T> configureSharing$FlowKt__ShareKt(Flow<? extends T> $this$configureSharing, int replay) {
        Flow dropChannelOperators;
        int i = 1;
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(replay >= 0)) {
                throw new AssertionError();
            }
        }
        int coerceAtLeast = RangesKt.coerceAtLeast(replay, Channel.Factory.getCHANNEL_DEFAULT_CAPACITY$kotlinx_coroutines_core()) - replay;
        if (!($this$configureSharing instanceof ChannelFlow) || (dropChannelOperators = ((ChannelFlow) $this$configureSharing).dropChannelOperators()) == null) {
            return new SharingConfig<>($this$configureSharing, coerceAtLeast, BufferOverflow.SUSPEND, EmptyCoroutineContext.INSTANCE);
        }
        switch (((ChannelFlow) $this$configureSharing).capacity) {
            case -3:
            case -2:
            case 0:
                if (((ChannelFlow) $this$configureSharing).onBufferOverflow != BufferOverflow.SUSPEND) {
                    if (replay != 0) {
                        i = 0;
                        break;
                    }
                } else if (((ChannelFlow) $this$configureSharing).capacity != 0) {
                    i = coerceAtLeast;
                    break;
                } else {
                    i = 0;
                    break;
                }
                break;
            default:
                i = ((ChannelFlow) $this$configureSharing).capacity;
                break;
        }
        return new SharingConfig<>(dropChannelOperators, i, ((ChannelFlow) $this$configureSharing).onBufferOverflow, ((ChannelFlow) $this$configureSharing).context);
    }

    private static final <T> Job launchSharing$FlowKt__ShareKt(CoroutineScope $this$launchSharing, CoroutineContext context, Flow<? extends T> upstream, MutableSharedFlow<T> shared, SharingStarted started, T initialValue) {
        return BuildersKt.launch($this$launchSharing, context, Intrinsics.areEqual((Object) started, (Object) SharingStarted.Companion.getEagerly()) ? CoroutineStart.DEFAULT : CoroutineStart.UNDISPATCHED, new FlowKt__ShareKt$launchSharing$1(started, upstream, shared, initialValue, (Continuation<? super FlowKt__ShareKt$launchSharing$1>) null));
    }

    private static final <T> void launchSharingDeferred$FlowKt__ShareKt(CoroutineScope $this$launchSharingDeferred, CoroutineContext context, Flow<? extends T> upstream, CompletableDeferred<StateFlow<T>> result) {
        Job unused = BuildersKt__Builders_commonKt.launch$default($this$launchSharingDeferred, context, (CoroutineStart) null, new FlowKt__ShareKt$launchSharingDeferred$1(upstream, result, (Continuation<? super FlowKt__ShareKt$launchSharingDeferred$1>) null), 2, (Object) null);
    }

    public static final <T> SharedFlow<T> onSubscription(SharedFlow<? extends T> $this$onSubscription, Function2<? super FlowCollector<? super T>, ? super Continuation<? super Unit>, ? extends Object> action) {
        return new SubscribedSharedFlow<>($this$onSubscription, action);
    }

    public static final <T> SharedFlow<T> shareIn(Flow<? extends T> $this$shareIn, CoroutineScope scope, SharingStarted started, int replay) {
        SharingConfig<? extends T> configureSharing$FlowKt__ShareKt = configureSharing$FlowKt__ShareKt($this$shareIn, replay);
        MutableSharedFlow MutableSharedFlow = SharedFlowKt.MutableSharedFlow(replay, configureSharing$FlowKt__ShareKt.extraBufferCapacity, configureSharing$FlowKt__ShareKt.onBufferOverflow);
        return new ReadonlySharedFlow<>(MutableSharedFlow, launchSharing$FlowKt__ShareKt(scope, configureSharing$FlowKt__ShareKt.context, configureSharing$FlowKt__ShareKt.upstream, MutableSharedFlow, started, SharedFlowKt.NO_VALUE));
    }

    public static /* synthetic */ SharedFlow shareIn$default(Flow flow, CoroutineScope coroutineScope, SharingStarted sharingStarted, int i, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return FlowKt.shareIn(flow, coroutineScope, sharingStarted, i);
    }

    public static final <T> Object stateIn(Flow<? extends T> $this$stateIn, CoroutineScope scope, Continuation<? super StateFlow<? extends T>> $completion) {
        SharingConfig<? extends T> configureSharing$FlowKt__ShareKt = configureSharing$FlowKt__ShareKt($this$stateIn, 1);
        CompletableDeferred CompletableDeferred$default = CompletableDeferredKt.CompletableDeferred$default((Job) null, 1, (Object) null);
        launchSharingDeferred$FlowKt__ShareKt(scope, configureSharing$FlowKt__ShareKt.context, configureSharing$FlowKt__ShareKt.upstream, CompletableDeferred$default);
        return CompletableDeferred$default.await($completion);
    }

    public static final <T> StateFlow<T> stateIn(Flow<? extends T> $this$stateIn, CoroutineScope scope, SharingStarted started, T initialValue) {
        SharingConfig<? extends T> configureSharing$FlowKt__ShareKt = configureSharing$FlowKt__ShareKt($this$stateIn, 1);
        MutableStateFlow MutableStateFlow = StateFlowKt.MutableStateFlow(initialValue);
        return new ReadonlyStateFlow<>(MutableStateFlow, launchSharing$FlowKt__ShareKt(scope, configureSharing$FlowKt__ShareKt.context, configureSharing$FlowKt__ShareKt.upstream, MutableStateFlow, started, initialValue));
    }
}
