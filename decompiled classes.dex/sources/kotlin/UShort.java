package kotlin;

import kotlin.jvm.JvmInline;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.UIntRange;
import mt.Log1F380D;
import okhttp3.internal.ws.WebSocketProtocol;

@JvmInline
@Metadata(d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\n\n\u0002\b\t\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b!\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u000e\b@\u0018\u0000 t2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001tB\u0014\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005J\u001b\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\fø\u0001\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u000eH\nø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0010J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0011H\nø\u0001\u0000¢\u0006\u0004\b\u0012\u0010\u0013J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0014H\nø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\nø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018J\u0016\u0010\u0019\u001a\u00020\u0000H\nø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u0005J\u001b\u0010\u001b\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\nø\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u0010J\u001b\u0010\u001b\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\nø\u0001\u0000¢\u0006\u0004\b\u001d\u0010\u0013J\u001b\u0010\u001b\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\nø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u001b\u0010\u001b\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\nø\u0001\u0000¢\u0006\u0004\b \u0010\u0018J\u001a\u0010!\u001a\u00020\"2\b\u0010\t\u001a\u0004\u0018\u00010#HÖ\u0003¢\u0006\u0004\b$\u0010%J\u001b\u0010&\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\bø\u0001\u0000¢\u0006\u0004\b'\u0010\u0010J\u001b\u0010&\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\bø\u0001\u0000¢\u0006\u0004\b(\u0010\u0013J\u001b\u0010&\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\bø\u0001\u0000¢\u0006\u0004\b)\u0010\u001fJ\u001b\u0010&\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\bø\u0001\u0000¢\u0006\u0004\b*\u0010\u0018J\u0010\u0010+\u001a\u00020\rHÖ\u0001¢\u0006\u0004\b,\u0010-J\u0016\u0010.\u001a\u00020\u0000H\nø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b/\u0010\u0005J\u0016\u00100\u001a\u00020\u0000H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b1\u0010\u0005J\u001b\u00102\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\nø\u0001\u0000¢\u0006\u0004\b3\u0010\u0010J\u001b\u00102\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\nø\u0001\u0000¢\u0006\u0004\b4\u0010\u0013J\u001b\u00102\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\nø\u0001\u0000¢\u0006\u0004\b5\u0010\u001fJ\u001b\u00102\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\nø\u0001\u0000¢\u0006\u0004\b6\u0010\u0018J\u001b\u00107\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\u000eH\bø\u0001\u0000¢\u0006\u0004\b8\u00109J\u001b\u00107\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\bø\u0001\u0000¢\u0006\u0004\b:\u0010\u0013J\u001b\u00107\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\bø\u0001\u0000¢\u0006\u0004\b;\u0010\u001fJ\u001b\u00107\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\bø\u0001\u0000¢\u0006\u0004\b<\u0010\u000bJ\u001b\u0010=\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\fø\u0001\u0000¢\u0006\u0004\b>\u0010\u000bJ\u001b\u0010?\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\nø\u0001\u0000¢\u0006\u0004\b@\u0010\u0010J\u001b\u0010?\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\nø\u0001\u0000¢\u0006\u0004\bA\u0010\u0013J\u001b\u0010?\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\nø\u0001\u0000¢\u0006\u0004\bB\u0010\u001fJ\u001b\u0010?\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\nø\u0001\u0000¢\u0006\u0004\bC\u0010\u0018J\u001b\u0010D\u001a\u00020E2\u0006\u0010\t\u001a\u00020\u0000H\nø\u0001\u0000¢\u0006\u0004\bF\u0010GJ\u001b\u0010H\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\nø\u0001\u0000¢\u0006\u0004\bI\u0010\u0010J\u001b\u0010H\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\nø\u0001\u0000¢\u0006\u0004\bJ\u0010\u0013J\u001b\u0010H\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\nø\u0001\u0000¢\u0006\u0004\bK\u0010\u001fJ\u001b\u0010H\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\nø\u0001\u0000¢\u0006\u0004\bL\u0010\u0018J\u001b\u0010M\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\nø\u0001\u0000¢\u0006\u0004\bN\u0010\u0010J\u001b\u0010M\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\nø\u0001\u0000¢\u0006\u0004\bO\u0010\u0013J\u001b\u0010M\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\nø\u0001\u0000¢\u0006\u0004\bP\u0010\u001fJ\u001b\u0010M\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\nø\u0001\u0000¢\u0006\u0004\bQ\u0010\u0018J\u0010\u0010R\u001a\u00020SH\b¢\u0006\u0004\bT\u0010UJ\u0010\u0010V\u001a\u00020WH\b¢\u0006\u0004\bX\u0010YJ\u0010\u0010Z\u001a\u00020[H\b¢\u0006\u0004\b\\\u0010]J\u0010\u0010^\u001a\u00020\rH\b¢\u0006\u0004\b_\u0010-J\u0010\u0010`\u001a\u00020aH\b¢\u0006\u0004\bb\u0010cJ\u0010\u0010d\u001a\u00020\u0003H\b¢\u0006\u0004\be\u0010\u0005J\u000f\u0010f\u001a\u00020gH\u0016¢\u0006\u0004\bh\u0010iJ\u0016\u0010j\u001a\u00020\u000eH\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bk\u0010UJ\u0016\u0010l\u001a\u00020\u0011H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bm\u0010-J\u0016\u0010n\u001a\u00020\u0014H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bo\u0010cJ\u0016\u0010p\u001a\u00020\u0000H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bq\u0010\u0005J\u001b\u0010r\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\fø\u0001\u0000¢\u0006\u0004\bs\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u0001\u0002\u0001\u00020\u0003ø\u0001\u0000\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u0006u"}, d2 = {"Lkotlin/UShort;", "", "data", "", "constructor-impl", "(S)S", "getData$annotations", "()V", "and", "other", "and-xj2QHRw", "(SS)S", "compareTo", "", "Lkotlin/UByte;", "compareTo-7apg3OU", "(SB)I", "Lkotlin/UInt;", "compareTo-WZ4Q5Ns", "(SI)I", "Lkotlin/ULong;", "compareTo-VKZWuLQ", "(SJ)I", "compareTo-xj2QHRw", "(SS)I", "dec", "dec-Mh2AYeg", "div", "div-7apg3OU", "div-WZ4Q5Ns", "div-VKZWuLQ", "(SJ)J", "div-xj2QHRw", "equals", "", "", "equals-impl", "(SLjava/lang/Object;)Z", "floorDiv", "floorDiv-7apg3OU", "floorDiv-WZ4Q5Ns", "floorDiv-VKZWuLQ", "floorDiv-xj2QHRw", "hashCode", "hashCode-impl", "(S)I", "inc", "inc-Mh2AYeg", "inv", "inv-Mh2AYeg", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "mod", "mod-7apg3OU", "(SB)B", "mod-WZ4Q5Ns", "mod-VKZWuLQ", "mod-xj2QHRw", "or", "or-xj2QHRw", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/UIntRange;", "rangeTo-xj2QHRw", "(SS)Lkotlin/ranges/UIntRange;", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "", "toByte-impl", "(S)B", "toDouble", "", "toDouble-impl", "(S)D", "toFloat", "", "toFloat-impl", "(S)F", "toInt", "toInt-impl", "toLong", "", "toLong-impl", "(S)J", "toShort", "toShort-impl", "toString", "", "toString-impl", "(S)Ljava/lang/String;", "toUByte", "toUByte-w2LRezQ", "toUInt", "toUInt-pVg5ArA", "toULong", "toULong-s-VKNKU", "toUShort", "toUShort-Mh2AYeg", "xor", "xor-xj2QHRw", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
/* compiled from: 011B */
public final class UShort implements Comparable<UShort> {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final short MAX_VALUE = -1;
    public static final short MIN_VALUE = 0;
    public static final int SIZE_BITS = 16;
    public static final int SIZE_BYTES = 2;
    private final short data;

    @Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\u00020\u0004XTø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\n\u0002\u0010\u0005R\u0016\u0010\u0006\u001a\u00020\u0004XTø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0007\u001a\u00020\bXT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bXT¢\u0006\u0002\n\u0000\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u0006\n"}, d2 = {"Lkotlin/UShort$Companion;", "", "()V", "MAX_VALUE", "Lkotlin/UShort;", "S", "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: UShort.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    private /* synthetic */ UShort(short data2) {
        this.data = data2;
    }

    /* renamed from: and-xj2QHRw  reason: not valid java name */
    private static final short m307andxj2QHRw(short arg0, short other) {
        return m314constructorimpl((short) (arg0 & other));
    }

    /* renamed from: box-impl  reason: not valid java name */
    public static final /* synthetic */ UShort m308boximpl(short s) {
        return new UShort(s);
    }

    /* renamed from: compareTo-7apg3OU  reason: not valid java name */
    private static final int m309compareTo7apg3OU(short arg0, byte other) {
        return Intrinsics.compare((int) 65535 & arg0, (int) other & UByte.MAX_VALUE);
    }

    /* renamed from: compareTo-VKZWuLQ  reason: not valid java name */
    private static final int m310compareToVKZWuLQ(short arg0, long other) {
        return UnsignedKt.ulongCompare(ULong.m208constructorimpl(((long) arg0) & WebSocketProtocol.PAYLOAD_SHORT_MAX), other);
    }

    /* renamed from: compareTo-WZ4Q5Ns  reason: not valid java name */
    private static final int m311compareToWZ4Q5Ns(short arg0, int other) {
        return UnsignedKt.uintCompare(UInt.m130constructorimpl(65535 & arg0), other);
    }

    /* renamed from: compareTo-xj2QHRw  reason: not valid java name */
    private int m312compareToxj2QHRw(short other) {
        return Intrinsics.compare((int) m363unboximpl() & MAX_VALUE, (int) 65535 & other);
    }

    /* renamed from: compareTo-xj2QHRw  reason: not valid java name */
    private static int m313compareToxj2QHRw(short arg0, short other) {
        return Intrinsics.compare((int) arg0 & MAX_VALUE, (int) 65535 & other);
    }

    /* renamed from: constructor-impl  reason: not valid java name */
    public static short m314constructorimpl(short s) {
        return s;
    }

    /* renamed from: dec-Mh2AYeg  reason: not valid java name */
    private static final short m315decMh2AYeg(short arg0) {
        return m314constructorimpl((short) (arg0 - 1));
    }

    /* renamed from: div-7apg3OU  reason: not valid java name */
    private static final int m316div7apg3OU(short arg0, byte other) {
        return UnsignedKt.m383uintDivideJ1ME1BU(UInt.m130constructorimpl(65535 & arg0), UInt.m130constructorimpl(other & UByte.MAX_VALUE));
    }

    /* renamed from: div-VKZWuLQ  reason: not valid java name */
    private static final long m317divVKZWuLQ(short arg0, long other) {
        return UnsignedKt.m385ulongDivideeb3DHEI(ULong.m208constructorimpl(((long) arg0) & WebSocketProtocol.PAYLOAD_SHORT_MAX), other);
    }

    /* renamed from: div-WZ4Q5Ns  reason: not valid java name */
    private static final int m318divWZ4Q5Ns(short arg0, int other) {
        return UnsignedKt.m383uintDivideJ1ME1BU(UInt.m130constructorimpl(65535 & arg0), other);
    }

    /* renamed from: div-xj2QHRw  reason: not valid java name */
    private static final int m319divxj2QHRw(short arg0, short other) {
        return UnsignedKt.m383uintDivideJ1ME1BU(UInt.m130constructorimpl(arg0 & MAX_VALUE), UInt.m130constructorimpl(65535 & other));
    }

    /* renamed from: equals-impl  reason: not valid java name */
    public static boolean m320equalsimpl(short s, Object obj) {
        return (obj instanceof UShort) && s == ((UShort) obj).m363unboximpl();
    }

    /* renamed from: equals-impl0  reason: not valid java name */
    public static final boolean m321equalsimpl0(short s, short s2) {
        return s == s2;
    }

    /* renamed from: floorDiv-7apg3OU  reason: not valid java name */
    private static final int m322floorDiv7apg3OU(short arg0, byte other) {
        return UnsignedKt.m383uintDivideJ1ME1BU(UInt.m130constructorimpl(65535 & arg0), UInt.m130constructorimpl(other & UByte.MAX_VALUE));
    }

    /* renamed from: floorDiv-VKZWuLQ  reason: not valid java name */
    private static final long m323floorDivVKZWuLQ(short arg0, long other) {
        return UnsignedKt.m385ulongDivideeb3DHEI(ULong.m208constructorimpl(((long) arg0) & WebSocketProtocol.PAYLOAD_SHORT_MAX), other);
    }

    /* renamed from: floorDiv-WZ4Q5Ns  reason: not valid java name */
    private static final int m324floorDivWZ4Q5Ns(short arg0, int other) {
        return UnsignedKt.m383uintDivideJ1ME1BU(UInt.m130constructorimpl(65535 & arg0), other);
    }

    /* renamed from: floorDiv-xj2QHRw  reason: not valid java name */
    private static final int m325floorDivxj2QHRw(short arg0, short other) {
        return UnsignedKt.m383uintDivideJ1ME1BU(UInt.m130constructorimpl(arg0 & MAX_VALUE), UInt.m130constructorimpl(65535 & other));
    }

    public static /* synthetic */ void getData$annotations() {
    }

    /* renamed from: hashCode-impl  reason: not valid java name */
    public static int m326hashCodeimpl(short s) {
        return s;
    }

    /* renamed from: inc-Mh2AYeg  reason: not valid java name */
    private static final short m327incMh2AYeg(short arg0) {
        return m314constructorimpl((short) (arg0 + 1));
    }

    /* renamed from: inv-Mh2AYeg  reason: not valid java name */
    private static final short m328invMh2AYeg(short arg0) {
        return m314constructorimpl((short) (~arg0));
    }

    /* renamed from: minus-7apg3OU  reason: not valid java name */
    private static final int m329minus7apg3OU(short arg0, byte other) {
        return UInt.m130constructorimpl(UInt.m130constructorimpl(65535 & arg0) - UInt.m130constructorimpl(other & UByte.MAX_VALUE));
    }

    /* renamed from: minus-VKZWuLQ  reason: not valid java name */
    private static final long m330minusVKZWuLQ(short arg0, long other) {
        return ULong.m208constructorimpl(ULong.m208constructorimpl(((long) arg0) & WebSocketProtocol.PAYLOAD_SHORT_MAX) - other);
    }

    /* renamed from: minus-WZ4Q5Ns  reason: not valid java name */
    private static final int m331minusWZ4Q5Ns(short arg0, int other) {
        return UInt.m130constructorimpl(UInt.m130constructorimpl(65535 & arg0) - other);
    }

    /* renamed from: minus-xj2QHRw  reason: not valid java name */
    private static final int m332minusxj2QHRw(short arg0, short other) {
        return UInt.m130constructorimpl(UInt.m130constructorimpl(arg0 & MAX_VALUE) - UInt.m130constructorimpl(65535 & other));
    }

    /* renamed from: mod-7apg3OU  reason: not valid java name */
    private static final byte m333mod7apg3OU(short arg0, byte other) {
        return UByte.m54constructorimpl((byte) UnsignedKt.m384uintRemainderJ1ME1BU(UInt.m130constructorimpl(65535 & arg0), UInt.m130constructorimpl(other & UByte.MAX_VALUE)));
    }

    /* renamed from: mod-VKZWuLQ  reason: not valid java name */
    private static final long m334modVKZWuLQ(short arg0, long other) {
        return UnsignedKt.m386ulongRemaindereb3DHEI(ULong.m208constructorimpl(((long) arg0) & WebSocketProtocol.PAYLOAD_SHORT_MAX), other);
    }

    /* renamed from: mod-WZ4Q5Ns  reason: not valid java name */
    private static final int m335modWZ4Q5Ns(short arg0, int other) {
        return UnsignedKt.m384uintRemainderJ1ME1BU(UInt.m130constructorimpl(65535 & arg0), other);
    }

    /* renamed from: mod-xj2QHRw  reason: not valid java name */
    private static final short m336modxj2QHRw(short arg0, short other) {
        return m314constructorimpl((short) UnsignedKt.m384uintRemainderJ1ME1BU(UInt.m130constructorimpl(arg0 & MAX_VALUE), UInt.m130constructorimpl(65535 & other)));
    }

    /* renamed from: or-xj2QHRw  reason: not valid java name */
    private static final short m337orxj2QHRw(short arg0, short other) {
        return m314constructorimpl((short) (arg0 | other));
    }

    /* renamed from: plus-7apg3OU  reason: not valid java name */
    private static final int m338plus7apg3OU(short arg0, byte other) {
        return UInt.m130constructorimpl(UInt.m130constructorimpl(65535 & arg0) + UInt.m130constructorimpl(other & UByte.MAX_VALUE));
    }

    /* renamed from: plus-VKZWuLQ  reason: not valid java name */
    private static final long m339plusVKZWuLQ(short arg0, long other) {
        return ULong.m208constructorimpl(ULong.m208constructorimpl(((long) arg0) & WebSocketProtocol.PAYLOAD_SHORT_MAX) + other);
    }

    /* renamed from: plus-WZ4Q5Ns  reason: not valid java name */
    private static final int m340plusWZ4Q5Ns(short arg0, int other) {
        return UInt.m130constructorimpl(UInt.m130constructorimpl(65535 & arg0) + other);
    }

    /* renamed from: plus-xj2QHRw  reason: not valid java name */
    private static final int m341plusxj2QHRw(short arg0, short other) {
        return UInt.m130constructorimpl(UInt.m130constructorimpl(arg0 & MAX_VALUE) + UInt.m130constructorimpl(65535 & other));
    }

    /* renamed from: rangeTo-xj2QHRw  reason: not valid java name */
    private static final UIntRange m342rangeToxj2QHRw(short arg0, short other) {
        return new UIntRange(UInt.m130constructorimpl(arg0 & MAX_VALUE), UInt.m130constructorimpl(65535 & other), (DefaultConstructorMarker) null);
    }

    /* renamed from: rem-7apg3OU  reason: not valid java name */
    private static final int m343rem7apg3OU(short arg0, byte other) {
        return UnsignedKt.m384uintRemainderJ1ME1BU(UInt.m130constructorimpl(65535 & arg0), UInt.m130constructorimpl(other & UByte.MAX_VALUE));
    }

    /* renamed from: rem-VKZWuLQ  reason: not valid java name */
    private static final long m344remVKZWuLQ(short arg0, long other) {
        return UnsignedKt.m386ulongRemaindereb3DHEI(ULong.m208constructorimpl(((long) arg0) & WebSocketProtocol.PAYLOAD_SHORT_MAX), other);
    }

    /* renamed from: rem-WZ4Q5Ns  reason: not valid java name */
    private static final int m345remWZ4Q5Ns(short arg0, int other) {
        return UnsignedKt.m384uintRemainderJ1ME1BU(UInt.m130constructorimpl(65535 & arg0), other);
    }

    /* renamed from: rem-xj2QHRw  reason: not valid java name */
    private static final int m346remxj2QHRw(short arg0, short other) {
        return UnsignedKt.m384uintRemainderJ1ME1BU(UInt.m130constructorimpl(arg0 & MAX_VALUE), UInt.m130constructorimpl(65535 & other));
    }

    /* renamed from: times-7apg3OU  reason: not valid java name */
    private static final int m347times7apg3OU(short arg0, byte other) {
        return UInt.m130constructorimpl(UInt.m130constructorimpl(65535 & arg0) * UInt.m130constructorimpl(other & UByte.MAX_VALUE));
    }

    /* renamed from: times-VKZWuLQ  reason: not valid java name */
    private static final long m348timesVKZWuLQ(short arg0, long other) {
        return ULong.m208constructorimpl(ULong.m208constructorimpl(((long) arg0) & WebSocketProtocol.PAYLOAD_SHORT_MAX) * other);
    }

    /* renamed from: times-WZ4Q5Ns  reason: not valid java name */
    private static final int m349timesWZ4Q5Ns(short arg0, int other) {
        return UInt.m130constructorimpl(UInt.m130constructorimpl(65535 & arg0) * other);
    }

    /* renamed from: times-xj2QHRw  reason: not valid java name */
    private static final int m350timesxj2QHRw(short arg0, short other) {
        return UInt.m130constructorimpl(UInt.m130constructorimpl(arg0 & MAX_VALUE) * UInt.m130constructorimpl(65535 & other));
    }

    /* renamed from: toByte-impl  reason: not valid java name */
    private static final byte m351toByteimpl(short arg0) {
        return (byte) arg0;
    }

    /* renamed from: toDouble-impl  reason: not valid java name */
    private static final double m352toDoubleimpl(short arg0) {
        return (double) (65535 & arg0);
    }

    /* renamed from: toFloat-impl  reason: not valid java name */
    private static final float m353toFloatimpl(short arg0) {
        return (float) (65535 & arg0);
    }

    /* renamed from: toInt-impl  reason: not valid java name */
    private static final int m354toIntimpl(short arg0) {
        return 65535 & arg0;
    }

    /* renamed from: toLong-impl  reason: not valid java name */
    private static final long m355toLongimpl(short arg0) {
        return ((long) arg0) & WebSocketProtocol.PAYLOAD_SHORT_MAX;
    }

    /* renamed from: toShort-impl  reason: not valid java name */
    private static final short m356toShortimpl(short arg0) {
        return arg0;
    }

    /* renamed from: toString-impl  reason: not valid java name */
    public static String m357toStringimpl(short arg0) {
        String valueOf = String.valueOf(65535 & arg0);
        Log1F380D.a((Object) valueOf);
        return valueOf;
    }

    /* renamed from: toUByte-w2LRezQ  reason: not valid java name */
    private static final byte m358toUBytew2LRezQ(short arg0) {
        return UByte.m54constructorimpl((byte) arg0);
    }

    /* renamed from: toUInt-pVg5ArA  reason: not valid java name */
    private static final int m359toUIntpVg5ArA(short arg0) {
        return UInt.m130constructorimpl(65535 & arg0);
    }

    /* renamed from: toULong-s-VKNKU  reason: not valid java name */
    private static final long m360toULongsVKNKU(short arg0) {
        return ULong.m208constructorimpl(((long) arg0) & WebSocketProtocol.PAYLOAD_SHORT_MAX);
    }

    /* renamed from: toUShort-Mh2AYeg  reason: not valid java name */
    private static final short m361toUShortMh2AYeg(short arg0) {
        return arg0;
    }

    /* renamed from: xor-xj2QHRw  reason: not valid java name */
    private static final short m362xorxj2QHRw(short arg0, short other) {
        return m314constructorimpl((short) (arg0 ^ other));
    }

    public /* bridge */ /* synthetic */ int compareTo(Object other) {
        return Intrinsics.compare((int) m363unboximpl() & MAX_VALUE, (int) ((UShort) other).m363unboximpl() & MAX_VALUE);
    }

    public boolean equals(Object obj) {
        return m320equalsimpl(this.data, obj);
    }

    public int hashCode() {
        return m326hashCodeimpl(this.data);
    }

    /* renamed from: unbox-impl  reason: not valid java name */
    public final /* synthetic */ short m363unboximpl() {
        return this.data;
    }

    public String toString() {
        String r0 = m357toStringimpl(this.data);
        Log1F380D.a((Object) r0);
        return r0;
    }
}
