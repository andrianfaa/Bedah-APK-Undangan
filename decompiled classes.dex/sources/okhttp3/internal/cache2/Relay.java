package okhttp3.internal.cache2;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.ByteString;
import okio.Source;
import okio.Timeout;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\t\u0018\u0000 :2\u00020\u0001:\u0002:;B3\b\u0002\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u0007¢\u0006\u0002\u0010\u000bJ\u000e\u00102\u001a\u0002032\u0006\u00104\u001a\u00020\u0007J\u0006\u0010\b\u001a\u00020\tJ\b\u00105\u001a\u0004\u0018\u00010\u0005J \u00106\u001a\u0002032\u0006\u00107\u001a\u00020\t2\u0006\u00104\u001a\u00020\u00072\u0006\u00108\u001a\u00020\u0007H\u0002J\u0010\u00109\u001a\u0002032\u0006\u00104\u001a\u00020\u0007H\u0002R\u0011\u0010\f\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\n\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0013X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u0011\u0010\u001c\u001a\u00020\u00138F¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u0015R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001d\u001a\u00020\u001eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u0011\u0010'\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b(\u0010\u000fR\u001a\u0010\u0006\u001a\u00020\u0007X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u0011\"\u0004\b*\u0010+R\u001c\u0010,\u001a\u0004\u0018\u00010-X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010/\"\u0004\b0\u00101¨\u0006<"}, d2 = {"Lokhttp3/internal/cache2/Relay;", "", "file", "Ljava/io/RandomAccessFile;", "upstream", "Lokio/Source;", "upstreamPos", "", "metadata", "Lokio/ByteString;", "bufferMaxSize", "(Ljava/io/RandomAccessFile;Lokio/Source;JLokio/ByteString;J)V", "buffer", "Lokio/Buffer;", "getBuffer", "()Lokio/Buffer;", "getBufferMaxSize", "()J", "complete", "", "getComplete", "()Z", "setComplete", "(Z)V", "getFile", "()Ljava/io/RandomAccessFile;", "setFile", "(Ljava/io/RandomAccessFile;)V", "isClosed", "sourceCount", "", "getSourceCount", "()I", "setSourceCount", "(I)V", "getUpstream", "()Lokio/Source;", "setUpstream", "(Lokio/Source;)V", "upstreamBuffer", "getUpstreamBuffer", "getUpstreamPos", "setUpstreamPos", "(J)V", "upstreamReader", "Ljava/lang/Thread;", "getUpstreamReader", "()Ljava/lang/Thread;", "setUpstreamReader", "(Ljava/lang/Thread;)V", "commit", "", "upstreamSize", "newSource", "writeHeader", "prefix", "metadataSize", "writeMetadata", "Companion", "RelaySource", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: Relay.kt */
public final class Relay {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final long FILE_HEADER_SIZE = 32;
    public static final ByteString PREFIX_CLEAN = ByteString.Companion.encodeUtf8("OkHttp cache v1\n");
    public static final ByteString PREFIX_DIRTY = ByteString.Companion.encodeUtf8("OkHttp DIRTY :(\n");
    private static final int SOURCE_FILE = 2;
    private static final int SOURCE_UPSTREAM = 1;
    private final Buffer buffer;
    private final long bufferMaxSize;
    private boolean complete;
    private RandomAccessFile file;
    private final ByteString metadata;
    private int sourceCount;
    private Source upstream;
    private final Buffer upstreamBuffer;
    private long upstreamPos;
    private Thread upstreamReader;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J&\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004J\u000e\u0010\u0013\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eR\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\u00068\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tXT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tXT¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lokhttp3/internal/cache2/Relay$Companion;", "", "()V", "FILE_HEADER_SIZE", "", "PREFIX_CLEAN", "Lokio/ByteString;", "PREFIX_DIRTY", "SOURCE_FILE", "", "SOURCE_UPSTREAM", "edit", "Lokhttp3/internal/cache2/Relay;", "file", "Ljava/io/File;", "upstream", "Lokio/Source;", "metadata", "bufferMaxSize", "read", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: Relay.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        public final Relay edit(File file, Source upstream, ByteString metadata, long bufferMaxSize) throws IOException {
            File file2 = file;
            Intrinsics.checkNotNullParameter(file2, "file");
            Intrinsics.checkNotNullParameter(upstream, "upstream");
            Intrinsics.checkNotNullParameter(metadata, "metadata");
            RandomAccessFile randomAccessFile = new RandomAccessFile(file2, "rw");
            Relay relay = new Relay(randomAccessFile, upstream, 0, metadata, bufferMaxSize, (DefaultConstructorMarker) null);
            randomAccessFile.setLength(0);
            relay.writeHeader(Relay.PREFIX_DIRTY, -1, -1);
            return relay;
        }

        public final Relay read(File file) throws IOException {
            File file2 = file;
            Intrinsics.checkNotNullParameter(file2, "file");
            RandomAccessFile randomAccessFile = new RandomAccessFile(file2, "rw");
            FileChannel channel = randomAccessFile.getChannel();
            Intrinsics.checkNotNullExpressionValue(channel, "randomAccessFile.channel");
            FileOperator fileOperator = new FileOperator(channel);
            Buffer buffer = new Buffer();
            fileOperator.read(0, buffer, Relay.FILE_HEADER_SIZE);
            if (!(!Intrinsics.areEqual((Object) buffer.readByteString((long) Relay.PREFIX_CLEAN.size()), (Object) Relay.PREFIX_CLEAN))) {
                long readLong = buffer.readLong();
                long readLong2 = buffer.readLong();
                Buffer buffer2 = new Buffer();
                fileOperator.read(readLong + Relay.FILE_HEADER_SIZE, buffer2, readLong2);
                return new Relay(randomAccessFile, (Source) null, readLong, buffer2.readByteString(), 0, (DefaultConstructorMarker) null);
            }
            throw new IOException("unreadable cache file");
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\bH\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lokhttp3/internal/cache2/Relay$RelaySource;", "Lokio/Source;", "(Lokhttp3/internal/cache2/Relay;)V", "fileOperator", "Lokhttp3/internal/cache2/FileOperator;", "sourcePos", "", "timeout", "Lokio/Timeout;", "close", "", "read", "sink", "Lokio/Buffer;", "byteCount", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: Relay.kt */
    public final class RelaySource implements Source {
        private FileOperator fileOperator;
        private long sourcePos;
        private final Timeout timeout = new Timeout();

        public RelaySource() {
            RandomAccessFile file = Relay.this.getFile();
            Intrinsics.checkNotNull(file);
            FileChannel channel = file.getChannel();
            Intrinsics.checkNotNullExpressionValue(channel, "file!!.channel");
            this.fileOperator = new FileOperator(channel);
        }

        public void close() throws IOException {
            if (this.fileOperator != null) {
                FileOperator fileOperator2 = null;
                this.fileOperator = null;
                RandomAccessFile randomAccessFile = null;
                synchronized (Relay.this) {
                    Relay relay = Relay.this;
                    relay.setSourceCount(relay.getSourceCount() - 1);
                    if (Relay.this.getSourceCount() == 0) {
                        randomAccessFile = Relay.this.getFile();
                        RandomAccessFile randomAccessFile2 = null;
                        Relay.this.setFile((RandomAccessFile) null);
                    }
                    Unit unit = Unit.INSTANCE;
                }
                if (randomAccessFile != null) {
                    Util.closeQuietly((Closeable) randomAccessFile);
                }
            }
        }

        /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
            java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
            	at java.util.ArrayList.rangeCheck(ArrayList.java:659)
            	at java.util.ArrayList.get(ArrayList.java:435)
            	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
            	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
            	at jadx.core.dex.visitors.regions.RegionMaker.processExcHandler(RegionMaker.java:1043)
            	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:975)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
            */
        public long read(okio.Buffer r25, long r26) throws java.io.IOException {
            /*
                r24 = this;
                r1 = r24
                r2 = r26
                java.lang.String r0 = "sink"
                r10 = r25
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r10, r0)
                okhttp3.internal.cache2.FileOperator r0 = r1.fileOperator
                r4 = 1
                if (r0 == 0) goto L_0x0013
                r0 = r4
                goto L_0x0014
            L_0x0013:
                r0 = 0
            L_0x0014:
                if (r0 == 0) goto L_0x01f4
                okhttp3.internal.cache2.Relay r11 = okhttp3.internal.cache2.Relay.this
                monitor-enter(r11)
                r0 = 0
            L_0x001a:
                okhttp3.internal.cache2.Relay r5 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01f1 }
                long r5 = r5.getUpstreamPos()     // Catch:{ all -> 0x01f1 }
                long r7 = r1.sourcePos     // Catch:{ all -> 0x01f1 }
                int r7 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
                r8 = 2
                r12 = -1
                if (r7 == 0) goto L_0x006a
                okhttp3.internal.cache2.Relay r4 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01f1 }
                long r4 = r4.getUpstreamPos()     // Catch:{ all -> 0x01f1 }
                okhttp3.internal.cache2.Relay r6 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01f1 }
                okio.Buffer r6 = r6.getBuffer()     // Catch:{ all -> 0x01f1 }
                long r6 = r6.size()     // Catch:{ all -> 0x01f1 }
                long r14 = r4 - r6
                long r4 = r1.sourcePos     // Catch:{ all -> 0x01f1 }
                int r4 = (r4 > r14 ? 1 : (r4 == r14 ? 0 : -1))
                if (r4 >= 0) goto L_0x0044
                r4 = r8
                goto L_0x008e
            L_0x0044:
                okhttp3.internal.cache2.Relay r4 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01f1 }
                long r4 = r4.getUpstreamPos()     // Catch:{ all -> 0x01f1 }
                long r6 = r1.sourcePos     // Catch:{ all -> 0x01f1 }
                long r4 = r4 - r6
                long r4 = java.lang.Math.min(r2, r4)     // Catch:{ all -> 0x01f1 }
                r12 = r4
                okhttp3.internal.cache2.Relay r4 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01f1 }
                okio.Buffer r4 = r4.getBuffer()     // Catch:{ all -> 0x01f1 }
                long r5 = r1.sourcePos     // Catch:{ all -> 0x01f1 }
                long r6 = r5 - r14
                r5 = r25
                r8 = r12
                r4.copyTo((okio.Buffer) r5, (long) r6, (long) r8)     // Catch:{ all -> 0x01f1 }
                long r4 = r1.sourcePos     // Catch:{ all -> 0x01f1 }
                long r4 = r4 + r12
                r1.sourcePos = r4     // Catch:{ all -> 0x01f1 }
                monitor-exit(r11)
                return r12
            L_0x006a:
                okhttp3.internal.cache2.Relay r7 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01f1 }
                boolean r7 = r7.getComplete()     // Catch:{ all -> 0x01f1 }
                if (r7 == 0) goto L_0x0074
                monitor-exit(r11)
                return r12
            L_0x0074:
                okhttp3.internal.cache2.Relay r7 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01f1 }
                java.lang.Thread r7 = r7.getUpstreamReader()     // Catch:{ all -> 0x01f1 }
                if (r7 == 0) goto L_0x0084
                okio.Timeout r7 = r1.timeout     // Catch:{ all -> 0x01f1 }
                okhttp3.internal.cache2.Relay r8 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01f1 }
                r7.waitUntilNotified(r8)     // Catch:{ all -> 0x01f1 }
                goto L_0x001a
            L_0x0084:
                okhttp3.internal.cache2.Relay r7 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01f1 }
                java.lang.Thread r9 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x01f1 }
                r7.setUpstreamReader(r9)     // Catch:{ all -> 0x01f1 }
            L_0x008e:
                monitor-exit(r11)
                r11 = r4
                r14 = 32
                if (r11 != r8) goto L_0x00b5
                okhttp3.internal.cache2.Relay r0 = okhttp3.internal.cache2.Relay.this
                long r4 = r0.getUpstreamPos()
                long r6 = r1.sourcePos
                long r4 = r4 - r6
                long r12 = java.lang.Math.min(r2, r4)
                okhttp3.internal.cache2.FileOperator r4 = r1.fileOperator
                kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
                long r5 = r1.sourcePos
                long r5 = r5 + r14
                r7 = r25
                r8 = r12
                r4.read(r5, r7, r8)
                long r4 = r1.sourcePos
                long r4 = r4 + r12
                r1.sourcePos = r4
                return r12
            L_0x00b5:
                r8 = 0
                okhttp3.internal.cache2.Relay r0 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01c8 }
                okio.Source r0 = r0.getUpstream()     // Catch:{ all -> 0x01c8 }
                kotlin.jvm.internal.Intrinsics.checkNotNull(r0)     // Catch:{ all -> 0x01c8 }
                okhttp3.internal.cache2.Relay r4 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01c8 }
                okio.Buffer r4 = r4.getUpstreamBuffer()     // Catch:{ all -> 0x01c8 }
                okhttp3.internal.cache2.Relay r5 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01c8 }
                long r5 = r5.getBufferMaxSize()     // Catch:{ all -> 0x01c8 }
                long r4 = r0.read(r4, r5)     // Catch:{ all -> 0x01c8 }
                r6 = r4
                int r0 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1))
                if (r0 != 0) goto L_0x0106
                okhttp3.internal.cache2.Relay r0 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01c8 }
                long r4 = r0.getUpstreamPos()     // Catch:{ all -> 0x01c8 }
                r0.commit(r4)     // Catch:{ all -> 0x01c8 }
                okhttp3.internal.cache2.Relay r4 = okhttp3.internal.cache2.Relay.this
                monitor-enter(r4)
                r0 = 0
                okhttp3.internal.cache2.Relay r5 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x0103 }
                r9 = r8
                java.lang.Thread r9 = (java.lang.Thread) r9     // Catch:{ all -> 0x0103 }
                r5.setUpstreamReader(r8)     // Catch:{ all -> 0x0103 }
                okhttp3.internal.cache2.Relay r5 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x0103 }
                r8 = 0
                if (r5 == 0) goto L_0x00fb
                r9 = r5
                java.lang.Object r9 = (java.lang.Object) r9     // Catch:{ all -> 0x0103 }
                r9.notifyAll()     // Catch:{ all -> 0x0103 }
                kotlin.Unit r0 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x0103 }
                monitor-exit(r4)
                return r12
            L_0x00fb:
                java.lang.NullPointerException r9 = new java.lang.NullPointerException     // Catch:{ all -> 0x0103 }
                java.lang.String r12 = "null cannot be cast to non-null type java.lang.Object"
                r9.<init>(r12)     // Catch:{ all -> 0x0103 }
                throw r9     // Catch:{ all -> 0x0103 }
            L_0x0103:
                r0 = move-exception
                monitor-exit(r4)
                throw r0
            L_0x0106:
                long r4 = java.lang.Math.min(r6, r2)     // Catch:{ all -> 0x01c8 }
                r12 = r4
                okhttp3.internal.cache2.Relay r0 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01c8 }
                okio.Buffer r4 = r0.getUpstreamBuffer()     // Catch:{ all -> 0x01c8 }
                r16 = 0
                r5 = r25
                r22 = r6
                r6 = r16
                r8 = r12
                r4.copyTo((okio.Buffer) r5, (long) r6, (long) r8)     // Catch:{ all -> 0x01c5 }
                long r4 = r1.sourcePos     // Catch:{ all -> 0x01c5 }
                long r4 = r4 + r12
                r1.sourcePos = r4     // Catch:{ all -> 0x01c5 }
                okhttp3.internal.cache2.FileOperator r0 = r1.fileOperator     // Catch:{ all -> 0x01c5 }
                kotlin.jvm.internal.Intrinsics.checkNotNull(r0)     // Catch:{ all -> 0x01c5 }
                okhttp3.internal.cache2.Relay r4 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01c5 }
                long r4 = r4.getUpstreamPos()     // Catch:{ all -> 0x01c5 }
                long r17 = r4 + r14
                okhttp3.internal.cache2.Relay r4 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01c5 }
                okio.Buffer r4 = r4.getUpstreamBuffer()     // Catch:{ all -> 0x01c5 }
                okio.Buffer r19 = r4.clone()     // Catch:{ all -> 0x01c5 }
                r16 = r0
                r20 = r22
                r16.write(r17, r19, r20)     // Catch:{ all -> 0x01c5 }
                okhttp3.internal.cache2.Relay r4 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01c5 }
                monitor-enter(r4)     // Catch:{ all -> 0x01c5 }
                r0 = 0
                okhttp3.internal.cache2.Relay r5 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01bd }
                okio.Buffer r5 = r5.getBuffer()     // Catch:{ all -> 0x01bd }
                okhttp3.internal.cache2.Relay r6 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01bd }
                okio.Buffer r6 = r6.getUpstreamBuffer()     // Catch:{ all -> 0x01bd }
                r7 = r22
                r5.write((okio.Buffer) r6, (long) r7)     // Catch:{ all -> 0x01ba }
                okhttp3.internal.cache2.Relay r5 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01ba }
                okio.Buffer r5 = r5.getBuffer()     // Catch:{ all -> 0x01ba }
                long r5 = r5.size()     // Catch:{ all -> 0x01ba }
                okhttp3.internal.cache2.Relay r9 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01ba }
                long r14 = r9.getBufferMaxSize()     // Catch:{ all -> 0x01ba }
                int r5 = (r5 > r14 ? 1 : (r5 == r14 ? 0 : -1))
                if (r5 <= 0) goto L_0x0184
                okhttp3.internal.cache2.Relay r5 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01ba }
                okio.Buffer r5 = r5.getBuffer()     // Catch:{ all -> 0x01ba }
                okhttp3.internal.cache2.Relay r6 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01ba }
                okio.Buffer r6 = r6.getBuffer()     // Catch:{ all -> 0x01ba }
                long r14 = r6.size()     // Catch:{ all -> 0x01ba }
                okhttp3.internal.cache2.Relay r6 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01ba }
                long r16 = r6.getBufferMaxSize()     // Catch:{ all -> 0x01ba }
                long r14 = r14 - r16
                r5.skip(r14)     // Catch:{ all -> 0x01ba }
            L_0x0184:
                okhttp3.internal.cache2.Relay r5 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01ba }
                long r14 = r5.getUpstreamPos()     // Catch:{ all -> 0x01ba }
                long r14 = r14 + r7
                r5.setUpstreamPos(r14)     // Catch:{ all -> 0x01ba }
                kotlin.Unit r0 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x01ba }
                monitor-exit(r4)     // Catch:{ all -> 0x01c5 }
                okhttp3.internal.cache2.Relay r4 = okhttp3.internal.cache2.Relay.this
                monitor-enter(r4)
                r0 = 0
                okhttp3.internal.cache2.Relay r5 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01b7 }
                r6 = 0
                r9 = r6
                java.lang.Thread r9 = (java.lang.Thread) r9     // Catch:{ all -> 0x01b7 }
                r5.setUpstreamReader(r6)     // Catch:{ all -> 0x01b7 }
                okhttp3.internal.cache2.Relay r5 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01b7 }
                r6 = 0
                if (r5 == 0) goto L_0x01af
                r9 = r5
                java.lang.Object r9 = (java.lang.Object) r9     // Catch:{ all -> 0x01b7 }
                r9.notifyAll()     // Catch:{ all -> 0x01b7 }
                kotlin.Unit r0 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x01b7 }
                monitor-exit(r4)
                return r12
            L_0x01af:
                java.lang.NullPointerException r9 = new java.lang.NullPointerException     // Catch:{ all -> 0x01b7 }
                java.lang.String r14 = "null cannot be cast to non-null type java.lang.Object"
                r9.<init>(r14)     // Catch:{ all -> 0x01b7 }
                throw r9     // Catch:{ all -> 0x01b7 }
            L_0x01b7:
                r0 = move-exception
                monitor-exit(r4)
                throw r0
            L_0x01ba:
                r0 = move-exception
                r6 = 0
                goto L_0x01c1
            L_0x01bd:
                r0 = move-exception
                r7 = r22
                r6 = 0
            L_0x01c1:
                monitor-exit(r4)     // Catch:{ all -> 0x01c3 }
                throw r0     // Catch:{ all -> 0x01c3 }
            L_0x01c3:
                r0 = move-exception
                goto L_0x01ca
            L_0x01c5:
                r0 = move-exception
                r6 = 0
                goto L_0x01ca
            L_0x01c8:
                r0 = move-exception
                r6 = r8
            L_0x01ca:
                okhttp3.internal.cache2.Relay r4 = okhttp3.internal.cache2.Relay.this
                monitor-enter(r4)
                r5 = 0
                okhttp3.internal.cache2.Relay r7 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01ee }
                r8 = r6
                java.lang.Thread r8 = (java.lang.Thread) r8     // Catch:{ all -> 0x01ee }
                r7.setUpstreamReader(r6)     // Catch:{ all -> 0x01ee }
                okhttp3.internal.cache2.Relay r6 = okhttp3.internal.cache2.Relay.this     // Catch:{ all -> 0x01ee }
                r7 = 0
                if (r6 != 0) goto L_0x01e3
                java.lang.NullPointerException r0 = new java.lang.NullPointerException     // Catch:{ all -> 0x01ee }
                java.lang.String r8 = "null cannot be cast to non-null type java.lang.Object"
                r0.<init>(r8)     // Catch:{ all -> 0x01ee }
                throw r0     // Catch:{ all -> 0x01ee }
            L_0x01e3:
                r8 = r6
                java.lang.Object r8 = (java.lang.Object) r8     // Catch:{ all -> 0x01ee }
                r8.notifyAll()     // Catch:{ all -> 0x01ee }
                kotlin.Unit r5 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x01ee }
                monitor-exit(r4)
                throw r0
            L_0x01ee:
                r0 = move-exception
                monitor-exit(r4)
                throw r0
            L_0x01f1:
                r0 = move-exception
                monitor-exit(r11)
                throw r0
            L_0x01f4:
                java.lang.String r0 = "Check failed."
                java.lang.IllegalStateException r4 = new java.lang.IllegalStateException
                java.lang.String r0 = r0.toString()
                r4.<init>(r0)
                java.lang.Throwable r4 = (java.lang.Throwable) r4
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache2.Relay.RelaySource.read(okio.Buffer, long):long");
        }

        public Timeout timeout() {
            return this.timeout;
        }
    }

    private Relay(RandomAccessFile file2, Source upstream2, long upstreamPos2, ByteString metadata2, long bufferMaxSize2) {
        this.file = file2;
        this.upstream = upstream2;
        this.upstreamPos = upstreamPos2;
        this.metadata = metadata2;
        this.bufferMaxSize = bufferMaxSize2;
        this.upstreamBuffer = new Buffer();
        this.complete = this.upstream == null;
        this.buffer = new Buffer();
    }

    public /* synthetic */ Relay(RandomAccessFile file2, Source upstream2, long upstreamPos2, ByteString metadata2, long bufferMaxSize2, DefaultConstructorMarker $constructor_marker) {
        this(file2, upstream2, upstreamPos2, metadata2, bufferMaxSize2);
    }

    /* access modifiers changed from: private */
    public final void writeHeader(ByteString prefix, long upstreamSize, long metadataSize) throws IOException {
        Buffer buffer2 = new Buffer();
        Buffer buffer3 = buffer2;
        buffer3.write(prefix);
        buffer3.writeLong(upstreamSize);
        buffer3.writeLong(metadataSize);
        if (buffer3.size() == FILE_HEADER_SIZE) {
            RandomAccessFile randomAccessFile = this.file;
            Intrinsics.checkNotNull(randomAccessFile);
            FileChannel channel = randomAccessFile.getChannel();
            Intrinsics.checkNotNullExpressionValue(channel, "file!!.channel");
            new FileOperator(channel).write(0, buffer2, FILE_HEADER_SIZE);
            return;
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }

    private final void writeMetadata(long upstreamSize) throws IOException {
        Buffer buffer2 = new Buffer();
        buffer2.write(this.metadata);
        RandomAccessFile randomAccessFile = this.file;
        Intrinsics.checkNotNull(randomAccessFile);
        FileChannel channel = randomAccessFile.getChannel();
        Intrinsics.checkNotNullExpressionValue(channel, "file!!.channel");
        new FileOperator(channel).write(FILE_HEADER_SIZE + upstreamSize, buffer2, (long) this.metadata.size());
    }

    public final void commit(long upstreamSize) throws IOException {
        writeMetadata(upstreamSize);
        RandomAccessFile randomAccessFile = this.file;
        Intrinsics.checkNotNull(randomAccessFile);
        randomAccessFile.getChannel().force(false);
        writeHeader(PREFIX_CLEAN, upstreamSize, (long) this.metadata.size());
        RandomAccessFile randomAccessFile2 = this.file;
        Intrinsics.checkNotNull(randomAccessFile2);
        randomAccessFile2.getChannel().force(false);
        synchronized (this) {
            this.complete = true;
            Unit unit = Unit.INSTANCE;
        }
        Source source = this.upstream;
        if (source != null) {
            Util.closeQuietly((Closeable) source);
        }
        Source source2 = null;
        this.upstream = null;
    }

    public final Buffer getBuffer() {
        return this.buffer;
    }

    public final long getBufferMaxSize() {
        return this.bufferMaxSize;
    }

    public final boolean getComplete() {
        return this.complete;
    }

    public final RandomAccessFile getFile() {
        return this.file;
    }

    public final int getSourceCount() {
        return this.sourceCount;
    }

    public final Source getUpstream() {
        return this.upstream;
    }

    public final Buffer getUpstreamBuffer() {
        return this.upstreamBuffer;
    }

    public final long getUpstreamPos() {
        return this.upstreamPos;
    }

    public final Thread getUpstreamReader() {
        return this.upstreamReader;
    }

    public final boolean isClosed() {
        return this.file == null;
    }

    public final ByteString metadata() {
        return this.metadata;
    }

    public final Source newSource() {
        synchronized (this) {
            if (this.file == null) {
                return null;
            }
            this.sourceCount++;
            return new RelaySource();
        }
    }

    public final void setComplete(boolean z) {
        this.complete = z;
    }

    public final void setFile(RandomAccessFile randomAccessFile) {
        this.file = randomAccessFile;
    }

    public final void setSourceCount(int i) {
        this.sourceCount = i;
    }

    public final void setUpstream(Source source) {
        this.upstream = source;
    }

    public final void setUpstreamPos(long j) {
        this.upstreamPos = j;
    }

    public final void setUpstreamReader(Thread thread) {
        this.upstreamReader = thread;
    }
}
