package okhttp3.internal.cache;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import kotlin.KotlinNothingValueException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import kotlin.text.Typography;
import okhttp3.internal.Util;
import okhttp3.internal.concurrent.TaskQueue;
import okhttp3.internal.concurrent.TaskRunner;
import okhttp3.internal.io.FileSystem;
import okhttp3.internal.platform.Platform;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import okio.Source;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000y\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010)\n\u0002\b\u0007*\u0001\u0014\u0018\u0000 [2\u00020\u00012\u00020\u0002:\u0004[\\]^B7\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\b\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\b\u00108\u001a\u000209H\u0002J\b\u0010:\u001a\u000209H\u0016J!\u0010;\u001a\u0002092\n\u0010<\u001a\u00060=R\u00020\u00002\u0006\u0010>\u001a\u00020\u0010H\u0000¢\u0006\u0002\b?J\u0006\u0010@\u001a\u000209J \u0010A\u001a\b\u0018\u00010=R\u00020\u00002\u0006\u0010B\u001a\u00020(2\b\b\u0002\u0010C\u001a\u00020\u000bH\u0007J\u0006\u0010D\u001a\u000209J\b\u0010E\u001a\u000209H\u0016J\u0017\u0010F\u001a\b\u0018\u00010GR\u00020\u00002\u0006\u0010B\u001a\u00020(H\u0002J\u0006\u0010H\u001a\u000209J\u0006\u0010I\u001a\u00020\u0010J\b\u0010J\u001a\u00020\u0010H\u0002J\b\u0010K\u001a\u00020%H\u0002J\b\u0010L\u001a\u000209H\u0002J\b\u0010M\u001a\u000209H\u0002J\u0010\u0010N\u001a\u0002092\u0006\u0010O\u001a\u00020(H\u0002J\r\u0010P\u001a\u000209H\u0000¢\u0006\u0002\bQJ\u000e\u0010R\u001a\u00020\u00102\u0006\u0010B\u001a\u00020(J\u0019\u0010S\u001a\u00020\u00102\n\u0010T\u001a\u00060)R\u00020\u0000H\u0000¢\u0006\u0002\bUJ\b\u0010V\u001a\u00020\u0010H\u0002J\u0006\u00105\u001a\u00020\u000bJ\u0010\u0010W\u001a\f\u0012\b\u0012\u00060GR\u00020\u00000XJ\u0006\u0010Y\u001a\u000209J\u0010\u0010Z\u001a\u0002092\u0006\u0010B\u001a\u00020(H\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0004\n\u0002\u0010\u0015R\u001a\u0010\u0016\u001a\u00020\u0010X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0014\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u000e\u0010\u001f\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010$\u001a\u0004\u0018\u00010%X\u000e¢\u0006\u0002\n\u0000R$\u0010&\u001a\u0012\u0012\u0004\u0012\u00020(\u0012\b\u0012\u00060)R\u00020\u00000'X\u0004¢\u0006\b\n\u0000\u001a\u0004\b*\u0010+R&\u0010\n\u001a\u00020\u000b2\u0006\u0010,\u001a\u00020\u000b8F@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b-\u0010.\"\u0004\b/\u00100R\u000e\u00101\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\u000bX\u000e¢\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\u000bX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\bX\u0004¢\u0006\b\n\u0000\u001a\u0004\b6\u00107¨\u0006_"}, d2 = {"Lokhttp3/internal/cache/DiskLruCache;", "Ljava/io/Closeable;", "Ljava/io/Flushable;", "fileSystem", "Lokhttp3/internal/io/FileSystem;", "directory", "Ljava/io/File;", "appVersion", "", "valueCount", "maxSize", "", "taskRunner", "Lokhttp3/internal/concurrent/TaskRunner;", "(Lokhttp3/internal/io/FileSystem;Ljava/io/File;IIJLokhttp3/internal/concurrent/TaskRunner;)V", "civilizedFileSystem", "", "cleanupQueue", "Lokhttp3/internal/concurrent/TaskQueue;", "cleanupTask", "okhttp3/internal/cache/DiskLruCache$cleanupTask$1", "Lokhttp3/internal/cache/DiskLruCache$cleanupTask$1;", "closed", "getClosed$okhttp", "()Z", "setClosed$okhttp", "(Z)V", "getDirectory", "()Ljava/io/File;", "getFileSystem$okhttp", "()Lokhttp3/internal/io/FileSystem;", "hasJournalErrors", "initialized", "journalFile", "journalFileBackup", "journalFileTmp", "journalWriter", "Lokio/BufferedSink;", "lruEntries", "Ljava/util/LinkedHashMap;", "", "Lokhttp3/internal/cache/DiskLruCache$Entry;", "getLruEntries$okhttp", "()Ljava/util/LinkedHashMap;", "value", "getMaxSize", "()J", "setMaxSize", "(J)V", "mostRecentRebuildFailed", "mostRecentTrimFailed", "nextSequenceNumber", "redundantOpCount", "size", "getValueCount$okhttp", "()I", "checkNotClosed", "", "close", "completeEdit", "editor", "Lokhttp3/internal/cache/DiskLruCache$Editor;", "success", "completeEdit$okhttp", "delete", "edit", "key", "expectedSequenceNumber", "evictAll", "flush", "get", "Lokhttp3/internal/cache/DiskLruCache$Snapshot;", "initialize", "isClosed", "journalRebuildRequired", "newJournalWriter", "processJournal", "readJournal", "readJournalLine", "line", "rebuildJournal", "rebuildJournal$okhttp", "remove", "removeEntry", "entry", "removeEntry$okhttp", "removeOldestEntry", "snapshots", "", "trimToSize", "validateKey", "Companion", "Editor", "Entry", "Snapshot", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: 01C6 */
public final class DiskLruCache implements Closeable, Flushable {
    public static final long ANY_SEQUENCE_NUMBER = -1;
    public static final String CLEAN = "CLEAN";
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String DIRTY = "DIRTY";
    public static final String JOURNAL_FILE = "journal";
    public static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    public static final String JOURNAL_FILE_TEMP = "journal.tmp";
    public static final Regex LEGAL_KEY_PATTERN = new Regex("[a-z0-9_-]{1,120}");
    public static final String MAGIC = "libcore.io.DiskLruCache";
    public static final String READ = "READ";
    public static final String REMOVE = "REMOVE";
    public static final String VERSION_1 = "1";
    private final int appVersion;
    /* access modifiers changed from: private */
    public boolean civilizedFileSystem;
    private final TaskQueue cleanupQueue;
    private final DiskLruCache$cleanupTask$1 cleanupTask;
    private boolean closed;
    private final File directory;
    private final FileSystem fileSystem;
    /* access modifiers changed from: private */
    public boolean hasJournalErrors;
    /* access modifiers changed from: private */
    public boolean initialized;
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    /* access modifiers changed from: private */
    public BufferedSink journalWriter;
    private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap<>(0, 0.75f, true);
    private long maxSize;
    /* access modifiers changed from: private */
    public boolean mostRecentRebuildFailed;
    /* access modifiers changed from: private */
    public boolean mostRecentTrimFailed;
    private long nextSequenceNumber;
    /* access modifiers changed from: private */
    public int redundantOpCount;
    private long size;
    private final int valueCount;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u00020\f8\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lokhttp3/internal/cache/DiskLruCache$Companion;", "", "()V", "ANY_SEQUENCE_NUMBER", "", "CLEAN", "", "DIRTY", "JOURNAL_FILE", "JOURNAL_FILE_BACKUP", "JOURNAL_FILE_TEMP", "LEGAL_KEY_PATTERN", "Lkotlin/text/Regex;", "MAGIC", "READ", "REMOVE", "VERSION_1", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: DiskLruCache.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0018\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0013\b\u0000\u0012\n\u0010\u0002\u001a\u00060\u0003R\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0010\u001a\u00020\u000fJ\r\u0010\u0011\u001a\u00020\u000fH\u0000¢\u0006\u0002\b\u0012J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0015\u001a\u00020\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000R\u0018\u0010\u0002\u001a\u00060\u0003R\u00020\u0004X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0016\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u0019"}, d2 = {"Lokhttp3/internal/cache/DiskLruCache$Editor;", "", "entry", "Lokhttp3/internal/cache/DiskLruCache$Entry;", "Lokhttp3/internal/cache/DiskLruCache;", "(Lokhttp3/internal/cache/DiskLruCache;Lokhttp3/internal/cache/DiskLruCache$Entry;)V", "done", "", "getEntry$okhttp", "()Lokhttp3/internal/cache/DiskLruCache$Entry;", "written", "", "getWritten$okhttp", "()[Z", "abort", "", "commit", "detach", "detach$okhttp", "newSink", "Lokio/Sink;", "index", "", "newSource", "Lokio/Source;", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: DiskLruCache.kt */
    public final class Editor {
        private boolean done;
        private final Entry entry;
        final /* synthetic */ DiskLruCache this$0;
        private final boolean[] written;

        public Editor(DiskLruCache this$02, Entry entry2) {
            Intrinsics.checkNotNullParameter(entry2, "entry");
            this.this$0 = this$02;
            this.entry = entry2;
            this.written = entry2.getReadable$okhttp() ? null : new boolean[this$02.getValueCount$okhttp()];
        }

        public final void abort() throws IOException {
            synchronized (this.this$0) {
                if (!this.done) {
                    if (Intrinsics.areEqual((Object) this.entry.getCurrentEditor$okhttp(), (Object) this)) {
                        this.this$0.completeEdit$okhttp(this, false);
                    }
                    this.done = true;
                    Unit unit = Unit.INSTANCE;
                } else {
                    throw new IllegalStateException("Check failed.".toString());
                }
            }
        }

        public final void commit() throws IOException {
            synchronized (this.this$0) {
                if (!this.done) {
                    if (Intrinsics.areEqual((Object) this.entry.getCurrentEditor$okhttp(), (Object) this)) {
                        this.this$0.completeEdit$okhttp(this, true);
                    }
                    this.done = true;
                    Unit unit = Unit.INSTANCE;
                } else {
                    throw new IllegalStateException("Check failed.".toString());
                }
            }
        }

        public final void detach$okhttp() {
            if (!Intrinsics.areEqual((Object) this.entry.getCurrentEditor$okhttp(), (Object) this)) {
                return;
            }
            if (this.this$0.civilizedFileSystem) {
                this.this$0.completeEdit$okhttp(this, false);
            } else {
                this.entry.setZombie$okhttp(true);
            }
        }

        public final Entry getEntry$okhttp() {
            return this.entry;
        }

        public final boolean[] getWritten$okhttp() {
            return this.written;
        }

        public final Sink newSink(int index) {
            synchronized (this.this$0) {
                if (!(!this.done)) {
                    throw new IllegalStateException("Check failed.".toString());
                } else if (!Intrinsics.areEqual((Object) this.entry.getCurrentEditor$okhttp(), (Object) this)) {
                    Sink blackhole = Okio.blackhole();
                    return blackhole;
                } else {
                    if (!this.entry.getReadable$okhttp()) {
                        boolean[] zArr = this.written;
                        Intrinsics.checkNotNull(zArr);
                        zArr[index] = true;
                    }
                    try {
                        Sink faultHidingSink = new FaultHidingSink(this.this$0.getFileSystem$okhttp().sink(this.entry.getDirtyFiles$okhttp().get(index)), new DiskLruCache$Editor$newSink$$inlined$synchronized$lambda$1(this, index));
                        return faultHidingSink;
                    } catch (FileNotFoundException e) {
                        return Okio.blackhole();
                    }
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:18:0x004c, code lost:
            return null;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final okio.Source newSource(int r6) {
            /*
                r5 = this;
                okhttp3.internal.cache.DiskLruCache r0 = r5.this$0
                monitor-enter(r0)
                r1 = 0
                boolean r2 = r5.done     // Catch:{ all -> 0x005b }
                r2 = r2 ^ 1
                if (r2 == 0) goto L_0x004d
                okhttp3.internal.cache.DiskLruCache$Entry r2 = r5.entry     // Catch:{ all -> 0x005b }
                boolean r2 = r2.getReadable$okhttp()     // Catch:{ all -> 0x005b }
                r3 = 0
                if (r2 == 0) goto L_0x004a
                okhttp3.internal.cache.DiskLruCache$Entry r2 = r5.entry     // Catch:{ all -> 0x005b }
                okhttp3.internal.cache.DiskLruCache$Editor r2 = r2.getCurrentEditor$okhttp()     // Catch:{ all -> 0x005b }
                r4 = r5
                okhttp3.internal.cache.DiskLruCache$Editor r4 = (okhttp3.internal.cache.DiskLruCache.Editor) r4     // Catch:{ all -> 0x005b }
                boolean r2 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2, (java.lang.Object) r4)     // Catch:{ all -> 0x005b }
                r2 = r2 ^ 1
                if (r2 != 0) goto L_0x004a
                okhttp3.internal.cache.DiskLruCache$Entry r2 = r5.entry     // Catch:{ all -> 0x005b }
                boolean r2 = r2.getZombie$okhttp()     // Catch:{ all -> 0x005b }
                if (r2 == 0) goto L_0x002d
                goto L_0x004a
            L_0x002d:
                okhttp3.internal.cache.DiskLruCache r2 = r5.this$0     // Catch:{ FileNotFoundException -> 0x0045 }
                okhttp3.internal.io.FileSystem r2 = r2.getFileSystem$okhttp()     // Catch:{ FileNotFoundException -> 0x0045 }
                okhttp3.internal.cache.DiskLruCache$Entry r4 = r5.entry     // Catch:{ FileNotFoundException -> 0x0045 }
                java.util.List r4 = r4.getCleanFiles$okhttp()     // Catch:{ FileNotFoundException -> 0x0045 }
                java.lang.Object r4 = r4.get(r6)     // Catch:{ FileNotFoundException -> 0x0045 }
                java.io.File r4 = (java.io.File) r4     // Catch:{ FileNotFoundException -> 0x0045 }
                okio.Source r3 = r2.source(r4)     // Catch:{ FileNotFoundException -> 0x0045 }
                goto L_0x0047
            L_0x0045:
                r2 = move-exception
            L_0x0047:
                monitor-exit(r0)
                return r3
            L_0x004a:
                monitor-exit(r0)
                return r3
            L_0x004d:
                java.lang.String r2 = "Check failed."
                java.lang.IllegalStateException r3 = new java.lang.IllegalStateException     // Catch:{ all -> 0x005b }
                java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x005b }
                r3.<init>(r2)     // Catch:{ all -> 0x005b }
                java.lang.Throwable r3 = (java.lang.Throwable) r3     // Catch:{ all -> 0x005b }
                throw r3     // Catch:{ all -> 0x005b }
            L_0x005b:
                r1 = move-exception
                monitor-exit(r0)
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache.Editor.newSource(int):okio.Source");
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0016\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010.\u001a\u00020/2\f\u00100\u001a\b\u0012\u0004\u0012\u00020\u000301H\u0002J\u0010\u00102\u001a\u0002032\u0006\u00104\u001a\u00020\u001aH\u0002J\u001b\u00105\u001a\u0002062\f\u00100\u001a\b\u0012\u0004\u0012\u00020\u000301H\u0000¢\u0006\u0002\b7J\u0013\u00108\u001a\b\u0018\u000109R\u00020\fH\u0000¢\u0006\u0002\b:J\u0015\u0010;\u001a\u0002062\u0006\u0010<\u001a\u00020=H\u0000¢\u0006\u0002\b>R\u001a\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR \u0010\n\u001a\b\u0018\u00010\u000bR\u00020\fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\tR\u0014\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u001a\u0010\u0019\u001a\u00020\u001aX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001a\u0010\u001f\u001a\u00020 X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R\u001a\u0010%\u001a\u00020&X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b'\u0010(\"\u0004\b)\u0010*R\u001a\u0010+\u001a\u00020 X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b,\u0010\"\"\u0004\b-\u0010$¨\u0006?"}, d2 = {"Lokhttp3/internal/cache/DiskLruCache$Entry;", "", "key", "", "(Lokhttp3/internal/cache/DiskLruCache;Ljava/lang/String;)V", "cleanFiles", "", "Ljava/io/File;", "getCleanFiles$okhttp", "()Ljava/util/List;", "currentEditor", "Lokhttp3/internal/cache/DiskLruCache$Editor;", "Lokhttp3/internal/cache/DiskLruCache;", "getCurrentEditor$okhttp", "()Lokhttp3/internal/cache/DiskLruCache$Editor;", "setCurrentEditor$okhttp", "(Lokhttp3/internal/cache/DiskLruCache$Editor;)V", "dirtyFiles", "getDirtyFiles$okhttp", "getKey$okhttp", "()Ljava/lang/String;", "lengths", "", "getLengths$okhttp", "()[J", "lockingSourceCount", "", "getLockingSourceCount$okhttp", "()I", "setLockingSourceCount$okhttp", "(I)V", "readable", "", "getReadable$okhttp", "()Z", "setReadable$okhttp", "(Z)V", "sequenceNumber", "", "getSequenceNumber$okhttp", "()J", "setSequenceNumber$okhttp", "(J)V", "zombie", "getZombie$okhttp", "setZombie$okhttp", "invalidLengths", "", "strings", "", "newSource", "Lokio/Source;", "index", "setLengths", "", "setLengths$okhttp", "snapshot", "Lokhttp3/internal/cache/DiskLruCache$Snapshot;", "snapshot$okhttp", "writeLengths", "writer", "Lokio/BufferedSink;", "writeLengths$okhttp", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: DiskLruCache.kt */
    public final class Entry {
        private final List<File> cleanFiles = new ArrayList();
        private Editor currentEditor;
        private final List<File> dirtyFiles = new ArrayList();
        private final String key;
        private final long[] lengths;
        private int lockingSourceCount;
        private boolean readable;
        private long sequenceNumber;
        final /* synthetic */ DiskLruCache this$0;
        private boolean zombie;

        public Entry(DiskLruCache this$02, String key2) {
            Intrinsics.checkNotNullParameter(key2, "key");
            this.this$0 = this$02;
            this.key = key2;
            this.lengths = new long[this$02.getValueCount$okhttp()];
            StringBuilder append = new StringBuilder(key2).append('.');
            int length = append.length();
            int valueCount$okhttp = this$02.getValueCount$okhttp();
            for (int i = 0; i < valueCount$okhttp; i++) {
                append.append(i);
                this.cleanFiles.add(new File(this$02.getDirectory(), append.toString()));
                append.append(".tmp");
                this.dirtyFiles.add(new File(this$02.getDirectory(), append.toString()));
                append.setLength(length);
            }
        }

        private final Void invalidLengths(List<String> strings) throws IOException {
            throw new IOException("unexpected journal line: " + strings);
        }

        private final Source newSource(int index) {
            Source source = this.this$0.getFileSystem$okhttp().source(this.cleanFiles.get(index));
            if (this.this$0.civilizedFileSystem) {
                return source;
            }
            this.lockingSourceCount++;
            return new DiskLruCache$Entry$newSource$1(this, source, source);
        }

        public final List<File> getCleanFiles$okhttp() {
            return this.cleanFiles;
        }

        public final Editor getCurrentEditor$okhttp() {
            return this.currentEditor;
        }

        public final List<File> getDirtyFiles$okhttp() {
            return this.dirtyFiles;
        }

        public final String getKey$okhttp() {
            return this.key;
        }

        public final long[] getLengths$okhttp() {
            return this.lengths;
        }

        public final int getLockingSourceCount$okhttp() {
            return this.lockingSourceCount;
        }

        public final boolean getReadable$okhttp() {
            return this.readable;
        }

        public final long getSequenceNumber$okhttp() {
            return this.sequenceNumber;
        }

        public final boolean getZombie$okhttp() {
            return this.zombie;
        }

        public final void setCurrentEditor$okhttp(Editor editor) {
            this.currentEditor = editor;
        }

        public final void setLengths$okhttp(List<String> strings) throws IOException {
            Intrinsics.checkNotNullParameter(strings, "strings");
            if (strings.size() == this.this$0.getValueCount$okhttp()) {
                try {
                    int size = strings.size();
                    for (int i = 0; i < size; i++) {
                        this.lengths[i] = Long.parseLong(strings.get(i));
                    }
                } catch (NumberFormatException e) {
                    invalidLengths(strings);
                    throw new KotlinNothingValueException();
                }
            } else {
                invalidLengths(strings);
                throw new KotlinNothingValueException();
            }
        }

        public final void setLockingSourceCount$okhttp(int i) {
            this.lockingSourceCount = i;
        }

        public final void setReadable$okhttp(boolean z) {
            this.readable = z;
        }

        public final void setSequenceNumber$okhttp(long j) {
            this.sequenceNumber = j;
        }

        public final void setZombie$okhttp(boolean z) {
            this.zombie = z;
        }

        public final Snapshot snapshot$okhttp() {
            DiskLruCache diskLruCache = this.this$0;
            if (Util.assertionsEnabled && !Thread.holdsLock(diskLruCache)) {
                StringBuilder append = new StringBuilder().append("Thread ");
                Thread currentThread = Thread.currentThread();
                Intrinsics.checkNotNullExpressionValue(currentThread, "Thread.currentThread()");
                throw new AssertionError(append.append(currentThread.getName()).append(" MUST hold lock on ").append(diskLruCache).toString());
            } else if (!this.readable) {
                return null;
            } else {
                if (!this.this$0.civilizedFileSystem && (this.currentEditor != null || this.zombie)) {
                    return null;
                }
                List<Source> arrayList = new ArrayList<>();
                long[] jArr = (long[]) this.lengths.clone();
                try {
                    int valueCount$okhttp = this.this$0.getValueCount$okhttp();
                    for (int i = 0; i < valueCount$okhttp; i++) {
                        arrayList.add(newSource(i));
                    }
                    return new Snapshot(this.this$0, this.key, this.sequenceNumber, arrayList, jArr);
                } catch (FileNotFoundException e) {
                    for (Source closeQuietly : arrayList) {
                        Util.closeQuietly((Closeable) closeQuietly);
                    }
                    try {
                        this.this$0.removeEntry$okhttp(this);
                    } catch (IOException e2) {
                    }
                    return null;
                }
            }
        }

        public final void writeLengths$okhttp(BufferedSink writer) throws IOException {
            Intrinsics.checkNotNullParameter(writer, "writer");
            for (long writeDecimalLong : this.lengths) {
                writer.writeByte(32).writeDecimalLong(writeDecimalLong);
            }
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0016\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B-\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bJ\b\u0010\f\u001a\u00020\rH\u0016J\f\u0010\u000e\u001a\b\u0018\u00010\u000fR\u00020\u0010J\u000e\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010\u0002\u001a\u00020\u0003R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lokhttp3/internal/cache/DiskLruCache$Snapshot;", "Ljava/io/Closeable;", "key", "", "sequenceNumber", "", "sources", "", "Lokio/Source;", "lengths", "", "(Lokhttp3/internal/cache/DiskLruCache;Ljava/lang/String;JLjava/util/List;[J)V", "close", "", "edit", "Lokhttp3/internal/cache/DiskLruCache$Editor;", "Lokhttp3/internal/cache/DiskLruCache;", "getLength", "index", "", "getSource", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: DiskLruCache.kt */
    public final class Snapshot implements Closeable {
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;
        private final List<Source> sources;
        final /* synthetic */ DiskLruCache this$0;

        public Snapshot(DiskLruCache this$02, String key2, long sequenceNumber2, List<? extends Source> sources2, long[] lengths2) {
            Intrinsics.checkNotNullParameter(key2, "key");
            Intrinsics.checkNotNullParameter(sources2, "sources");
            Intrinsics.checkNotNullParameter(lengths2, "lengths");
            this.this$0 = this$02;
            this.key = key2;
            this.sequenceNumber = sequenceNumber2;
            this.sources = sources2;
            this.lengths = lengths2;
        }

        public void close() {
            for (Source closeQuietly : this.sources) {
                Util.closeQuietly((Closeable) closeQuietly);
            }
        }

        public final Editor edit() throws IOException {
            return this.this$0.edit(this.key, this.sequenceNumber);
        }

        public final long getLength(int index) {
            return this.lengths[index];
        }

        public final Source getSource(int index) {
            return this.sources.get(index);
        }

        public final String key() {
            return this.key;
        }
    }

    public DiskLruCache(FileSystem fileSystem2, File directory2, int appVersion2, int valueCount2, long maxSize2, TaskRunner taskRunner) {
        Intrinsics.checkNotNullParameter(fileSystem2, "fileSystem");
        Intrinsics.checkNotNullParameter(directory2, "directory");
        Intrinsics.checkNotNullParameter(taskRunner, "taskRunner");
        this.fileSystem = fileSystem2;
        this.directory = directory2;
        this.appVersion = appVersion2;
        this.valueCount = valueCount2;
        this.maxSize = maxSize2;
        boolean z = false;
        this.cleanupQueue = taskRunner.newQueue();
        this.cleanupTask = new DiskLruCache$cleanupTask$1(this, Util.okHttpName + " Cache");
        if (maxSize2 > 0) {
            if (valueCount2 > 0 ? true : z) {
                this.journalFile = new File(directory2, JOURNAL_FILE);
                this.journalFileTmp = new File(directory2, JOURNAL_FILE_TEMP);
                this.journalFileBackup = new File(directory2, JOURNAL_FILE_BACKUP);
                return;
            }
            throw new IllegalArgumentException("valueCount <= 0".toString());
        }
        throw new IllegalArgumentException("maxSize <= 0".toString());
    }

    private final synchronized void checkNotClosed() {
        if (!(!this.closed)) {
            throw new IllegalStateException("cache is closed".toString());
        }
    }

    public static /* synthetic */ Editor edit$default(DiskLruCache diskLruCache, String str, long j, int i, Object obj) throws IOException {
        if ((i & 2) != 0) {
            j = ANY_SEQUENCE_NUMBER;
        }
        return diskLruCache.edit(str, j);
    }

    /* access modifiers changed from: private */
    public final boolean journalRebuildRequired() {
        int i = this.redundantOpCount;
        return i >= 2000 && i >= this.lruEntries.size();
    }

    private final BufferedSink newJournalWriter() throws FileNotFoundException {
        return Okio.buffer((Sink) new FaultHidingSink(this.fileSystem.appendingSink(this.journalFile), new DiskLruCache$newJournalWriter$faultHidingSink$1(this)));
    }

    private final void processJournal() throws IOException {
        this.fileSystem.delete(this.journalFileTmp);
        Iterator<Entry> it = this.lruEntries.values().iterator();
        while (it.hasNext()) {
            Entry next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "i.next()");
            Entry entry = next;
            int i = 0;
            if (entry.getCurrentEditor$okhttp() == null) {
                int i2 = this.valueCount;
                while (i < i2) {
                    this.size += entry.getLengths$okhttp()[i];
                    i++;
                }
            } else {
                Editor editor = null;
                entry.setCurrentEditor$okhttp((Editor) null);
                int i3 = this.valueCount;
                while (i < i3) {
                    this.fileSystem.delete(entry.getCleanFiles$okhttp().get(i));
                    this.fileSystem.delete(entry.getDirtyFiles$okhttp().get(i));
                    i++;
                }
                it.remove();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00dd, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00de, code lost:
        kotlin.io.CloseableKt.closeFinally(r1, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00e1, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void readJournal() throws java.io.IOException {
        /*
            r12 = this;
            java.lang.String r0 = ", "
            okhttp3.internal.io.FileSystem r1 = r12.fileSystem
            java.io.File r2 = r12.journalFile
            okio.Source r1 = r1.source(r2)
            okio.BufferedSource r1 = okio.Okio.buffer((okio.Source) r1)
            java.io.Closeable r1 = (java.io.Closeable) r1
            r2 = 0
            r3 = r2
            java.lang.Throwable r3 = (java.lang.Throwable) r3
            r3 = r1
            okio.BufferedSource r3 = (okio.BufferedSource) r3     // Catch:{ all -> 0x00db }
            r4 = 0
            java.lang.String r5 = r3.readUtf8LineStrict()     // Catch:{ all -> 0x00db }
            java.lang.String r6 = r3.readUtf8LineStrict()     // Catch:{ all -> 0x00db }
            java.lang.String r7 = r3.readUtf8LineStrict()     // Catch:{ all -> 0x00db }
            java.lang.String r8 = r3.readUtf8LineStrict()     // Catch:{ all -> 0x00db }
            java.lang.String r9 = r3.readUtf8LineStrict()     // Catch:{ all -> 0x00db }
            java.lang.String r10 = MAGIC     // Catch:{ all -> 0x00db }
            boolean r10 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r10, (java.lang.Object) r5)     // Catch:{ all -> 0x00db }
            r11 = 1
            r10 = r10 ^ r11
            if (r10 != 0) goto L_0x00a1
            java.lang.String r10 = VERSION_1     // Catch:{ all -> 0x00db }
            boolean r10 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r10, (java.lang.Object) r6)     // Catch:{ all -> 0x00db }
            r10 = r10 ^ r11
            if (r10 != 0) goto L_0x00a1
            int r10 = r12.appVersion     // Catch:{ all -> 0x00db }
            java.lang.String r10 = java.lang.String.valueOf(r10)     // Catch:{ all -> 0x00db }
            mt.Log1F380D.a((java.lang.Object) r10)
            boolean r10 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r10, (java.lang.Object) r7)     // Catch:{ all -> 0x00db }
            r10 = r10 ^ r11
            if (r10 != 0) goto L_0x00a1
            int r10 = r12.valueCount     // Catch:{ all -> 0x00db }
            java.lang.String r10 = java.lang.String.valueOf(r10)     // Catch:{ all -> 0x00db }
            mt.Log1F380D.a((java.lang.Object) r10)
            boolean r10 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r10, (java.lang.Object) r8)     // Catch:{ all -> 0x00db }
            r10 = r10 ^ r11
            if (r10 != 0) goto L_0x00a1
            r10 = r9
            java.lang.CharSequence r10 = (java.lang.CharSequence) r10     // Catch:{ all -> 0x00db }
            int r10 = r10.length()     // Catch:{ all -> 0x00db }
            if (r10 <= 0) goto L_0x006d
            goto L_0x006e
        L_0x006d:
            r11 = 0
        L_0x006e:
            if (r11 != 0) goto L_0x00a1
            r0 = 0
        L_0x0071:
            java.lang.String r10 = r3.readUtf8LineStrict()     // Catch:{ EOFException -> 0x007d }
            r12.readJournalLine(r10)     // Catch:{ EOFException -> 0x007d }
            int r0 = r0 + 1
            goto L_0x0071
        L_0x007d:
            r10 = move-exception
            java.util.LinkedHashMap<java.lang.String, okhttp3.internal.cache.DiskLruCache$Entry> r10 = r12.lruEntries     // Catch:{ all -> 0x00db }
            int r10 = r10.size()     // Catch:{ all -> 0x00db }
            int r10 = r0 - r10
            r12.redundantOpCount = r10     // Catch:{ all -> 0x00db }
            boolean r10 = r3.exhausted()     // Catch:{ all -> 0x00db }
            if (r10 != 0) goto L_0x0093
            r12.rebuildJournal$okhttp()     // Catch:{ all -> 0x00db }
            goto L_0x0099
        L_0x0093:
            okio.BufferedSink r10 = r12.newJournalWriter()     // Catch:{ all -> 0x00db }
            r12.journalWriter = r10     // Catch:{ all -> 0x00db }
        L_0x0099:
            kotlin.Unit r0 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x00db }
            kotlin.io.CloseableKt.closeFinally(r1, r2)
            return
        L_0x00a1:
            java.io.IOException r2 = new java.io.IOException     // Catch:{ all -> 0x00db }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x00db }
            r10.<init>()     // Catch:{ all -> 0x00db }
            java.lang.String r11 = "unexpected journal header: ["
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x00db }
            java.lang.StringBuilder r10 = r10.append(r5)     // Catch:{ all -> 0x00db }
            java.lang.StringBuilder r10 = r10.append(r0)     // Catch:{ all -> 0x00db }
            java.lang.StringBuilder r10 = r10.append(r6)     // Catch:{ all -> 0x00db }
            java.lang.StringBuilder r10 = r10.append(r0)     // Catch:{ all -> 0x00db }
            java.lang.StringBuilder r10 = r10.append(r8)     // Catch:{ all -> 0x00db }
            java.lang.StringBuilder r0 = r10.append(r0)     // Catch:{ all -> 0x00db }
            java.lang.StringBuilder r0 = r0.append(r9)     // Catch:{ all -> 0x00db }
            r10 = 93
            java.lang.StringBuilder r0 = r0.append(r10)     // Catch:{ all -> 0x00db }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x00db }
            r2.<init>(r0)     // Catch:{ all -> 0x00db }
            java.lang.Throwable r2 = (java.lang.Throwable) r2     // Catch:{ all -> 0x00db }
            throw r2     // Catch:{ all -> 0x00db }
        L_0x00db:
            r0 = move-exception
            throw r0     // Catch:{ all -> 0x00dd }
        L_0x00dd:
            r2 = move-exception
            kotlin.io.CloseableKt.closeFinally(r1, r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache.readJournal():void");
    }

    private final void readJournalLine(String line) throws IOException {
        String str;
        String str2 = line;
        int indexOf$default = StringsKt.indexOf$default((CharSequence) str2, ' ', 0, false, 6, (Object) null);
        if (indexOf$default != -1) {
            int i = indexOf$default + 1;
            int indexOf$default2 = StringsKt.indexOf$default((CharSequence) str2, ' ', i, false, 4, (Object) null);
            if (indexOf$default2 == -1) {
                if (str2 != null) {
                    String substring = str2.substring(i);
                    Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.String).substring(startIndex)");
                    str = substring;
                    String str3 = REMOVE;
                    if (indexOf$default == str3.length() && StringsKt.startsWith$default(str2, str3, false, 2, (Object) null)) {
                        this.lruEntries.remove(str);
                        return;
                    }
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
            } else if (str2 != null) {
                String substring2 = str2.substring(i, indexOf$default2);
                Intrinsics.checkNotNullExpressionValue(substring2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                str = substring2;
            } else {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            Entry entry = this.lruEntries.get(str);
            if (entry == null) {
                entry = new Entry(this, str);
                this.lruEntries.put(str, entry);
            }
            if (indexOf$default2 != -1) {
                String str4 = CLEAN;
                if (indexOf$default == str4.length() && StringsKt.startsWith$default(str2, str4, false, 2, (Object) null)) {
                    int i2 = indexOf$default2 + 1;
                    if (str2 != null) {
                        String substring3 = str2.substring(i2);
                        Intrinsics.checkNotNullExpressionValue(substring3, "(this as java.lang.String).substring(startIndex)");
                        List split$default = StringsKt.split$default((CharSequence) substring3, new char[]{' '}, false, 0, 6, (Object) null);
                        entry.setReadable$okhttp(true);
                        Editor editor = null;
                        entry.setCurrentEditor$okhttp((Editor) null);
                        entry.setLengths$okhttp(split$default);
                        return;
                    }
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
            }
            if (indexOf$default2 == -1) {
                String str5 = DIRTY;
                if (indexOf$default == str5.length() && StringsKt.startsWith$default(str2, str5, false, 2, (Object) null)) {
                    entry.setCurrentEditor$okhttp(new Editor(this, entry));
                    return;
                }
            }
            if (indexOf$default2 == -1) {
                String str6 = READ;
                if (indexOf$default == str6.length() && StringsKt.startsWith$default(str2, str6, false, 2, (Object) null)) {
                    return;
                }
            }
            throw new IOException("unexpected journal line: " + str2);
        }
        throw new IOException("unexpected journal line: " + str2);
    }

    private final boolean removeOldestEntry() {
        for (Entry next : this.lruEntries.values()) {
            if (!next.getZombie$okhttp()) {
                Intrinsics.checkNotNullExpressionValue(next, "toEvict");
                removeEntry$okhttp(next);
                return true;
            }
        }
        return false;
    }

    private final void validateKey(String key) {
        if (!LEGAL_KEY_PATTERN.matches(key)) {
            throw new IllegalArgumentException(("keys must match regex [a-z0-9_-]{1,120}: \"" + key + Typography.quote).toString());
        }
    }

    public synchronized void close() throws IOException {
        Editor currentEditor$okhttp;
        if (this.initialized) {
            if (!this.closed) {
                Collection<Entry> values = this.lruEntries.values();
                Intrinsics.checkNotNullExpressionValue(values, "lruEntries.values");
                Object[] array = values.toArray(new Entry[0]);
                if (array != null) {
                    for (Entry entry : (Entry[]) array) {
                        if (!(entry.getCurrentEditor$okhttp() == null || (currentEditor$okhttp = entry.getCurrentEditor$okhttp()) == null)) {
                            currentEditor$okhttp.detach$okhttp();
                        }
                    }
                    trimToSize();
                    BufferedSink bufferedSink = this.journalWriter;
                    Intrinsics.checkNotNull(bufferedSink);
                    bufferedSink.close();
                    BufferedSink bufferedSink2 = null;
                    this.journalWriter = null;
                    this.closed = true;
                    return;
                }
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
            }
        }
        this.closed = true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0146, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized void completeEdit$okhttp(okhttp3.internal.cache.DiskLruCache.Editor r12, boolean r13) throws java.io.IOException {
        /*
            r11 = this;
            monitor-enter(r11)
            java.lang.String r0 = "editor"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r12, r0)     // Catch:{ all -> 0x0155 }
            okhttp3.internal.cache.DiskLruCache$Entry r0 = r12.getEntry$okhttp()     // Catch:{ all -> 0x0155 }
            okhttp3.internal.cache.DiskLruCache$Editor r1 = r0.getCurrentEditor$okhttp()     // Catch:{ all -> 0x0155 }
            boolean r1 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r1, (java.lang.Object) r12)     // Catch:{ all -> 0x0155 }
            if (r1 == 0) goto L_0x0147
            r1 = 0
            if (r13 == 0) goto L_0x0066
            boolean r2 = r0.getReadable$okhttp()     // Catch:{ all -> 0x0155 }
            if (r2 != 0) goto L_0x0066
            int r2 = r11.valueCount     // Catch:{ all -> 0x0155 }
            r3 = r1
        L_0x0020:
            if (r3 >= r2) goto L_0x0066
            boolean[] r4 = r12.getWritten$okhttp()     // Catch:{ all -> 0x0155 }
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)     // Catch:{ all -> 0x0155 }
            boolean r4 = r4[r3]     // Catch:{ all -> 0x0155 }
            if (r4 == 0) goto L_0x0048
            okhttp3.internal.io.FileSystem r4 = r11.fileSystem     // Catch:{ all -> 0x0155 }
            java.util.List r5 = r0.getDirtyFiles$okhttp()     // Catch:{ all -> 0x0155 }
            java.lang.Object r5 = r5.get(r3)     // Catch:{ all -> 0x0155 }
            java.io.File r5 = (java.io.File) r5     // Catch:{ all -> 0x0155 }
            boolean r4 = r4.exists(r5)     // Catch:{ all -> 0x0155 }
            if (r4 != 0) goto L_0x0044
            r12.abort()     // Catch:{ all -> 0x0155 }
            monitor-exit(r11)
            return
        L_0x0044:
            int r3 = r3 + 1
            goto L_0x0020
        L_0x0048:
            r12.abort()     // Catch:{ all -> 0x0155 }
            java.lang.IllegalStateException r1 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0155 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0155 }
            r2.<init>()     // Catch:{ all -> 0x0155 }
            java.lang.String r4 = "Newly created entry didn't create value for index "
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ all -> 0x0155 }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x0155 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0155 }
            r1.<init>(r2)     // Catch:{ all -> 0x0155 }
            java.lang.Throwable r1 = (java.lang.Throwable) r1     // Catch:{ all -> 0x0155 }
            throw r1     // Catch:{ all -> 0x0155 }
        L_0x0066:
            int r2 = r11.valueCount     // Catch:{ all -> 0x0155 }
        L_0x0068:
            if (r1 >= r2) goto L_0x00b7
            java.util.List r3 = r0.getDirtyFiles$okhttp()     // Catch:{ all -> 0x0155 }
            java.lang.Object r3 = r3.get(r1)     // Catch:{ all -> 0x0155 }
            java.io.File r3 = (java.io.File) r3     // Catch:{ all -> 0x0155 }
            if (r13 == 0) goto L_0x00ad
            boolean r4 = r0.getZombie$okhttp()     // Catch:{ all -> 0x0155 }
            if (r4 != 0) goto L_0x00ad
            okhttp3.internal.io.FileSystem r4 = r11.fileSystem     // Catch:{ all -> 0x0155 }
            boolean r4 = r4.exists(r3)     // Catch:{ all -> 0x0155 }
            if (r4 == 0) goto L_0x00b2
            java.util.List r4 = r0.getCleanFiles$okhttp()     // Catch:{ all -> 0x0155 }
            java.lang.Object r4 = r4.get(r1)     // Catch:{ all -> 0x0155 }
            java.io.File r4 = (java.io.File) r4     // Catch:{ all -> 0x0155 }
            okhttp3.internal.io.FileSystem r5 = r11.fileSystem     // Catch:{ all -> 0x0155 }
            r5.rename(r3, r4)     // Catch:{ all -> 0x0155 }
            long[] r5 = r0.getLengths$okhttp()     // Catch:{ all -> 0x0155 }
            r6 = r5[r1]     // Catch:{ all -> 0x0155 }
            r5 = r6
            okhttp3.internal.io.FileSystem r7 = r11.fileSystem     // Catch:{ all -> 0x0155 }
            long r7 = r7.size(r4)     // Catch:{ all -> 0x0155 }
            long[] r9 = r0.getLengths$okhttp()     // Catch:{ all -> 0x0155 }
            r9[r1] = r7     // Catch:{ all -> 0x0155 }
            long r9 = r11.size     // Catch:{ all -> 0x0155 }
            long r9 = r9 - r5
            long r9 = r9 + r7
            r11.size = r9     // Catch:{ all -> 0x0155 }
            goto L_0x00b2
        L_0x00ad:
            okhttp3.internal.io.FileSystem r4 = r11.fileSystem     // Catch:{ all -> 0x0155 }
            r4.delete(r3)     // Catch:{ all -> 0x0155 }
        L_0x00b2:
            int r1 = r1 + 1
            goto L_0x0068
        L_0x00b7:
            r1 = 0
            r2 = r1
            okhttp3.internal.cache.DiskLruCache$Editor r2 = (okhttp3.internal.cache.DiskLruCache.Editor) r2     // Catch:{ all -> 0x0155 }
            r0.setCurrentEditor$okhttp(r1)     // Catch:{ all -> 0x0155 }
            boolean r1 = r0.getZombie$okhttp()     // Catch:{ all -> 0x0155 }
            if (r1 == 0) goto L_0x00c9
            r11.removeEntry$okhttp(r0)     // Catch:{ all -> 0x0155 }
            monitor-exit(r11)
            return
        L_0x00c9:
            int r1 = r11.redundantOpCount     // Catch:{ all -> 0x0155 }
            r2 = 1
            int r1 = r1 + r2
            r11.redundantOpCount = r1     // Catch:{ all -> 0x0155 }
            okio.BufferedSink r1 = r11.journalWriter     // Catch:{ all -> 0x0155 }
            kotlin.jvm.internal.Intrinsics.checkNotNull(r1)     // Catch:{ all -> 0x0155 }
            r3 = 0
            boolean r4 = r0.getReadable$okhttp()     // Catch:{ all -> 0x0155 }
            r5 = 10
            r6 = 32
            if (r4 != 0) goto L_0x00ff
            if (r13 == 0) goto L_0x00e2
            goto L_0x00ff
        L_0x00e2:
            java.util.LinkedHashMap<java.lang.String, okhttp3.internal.cache.DiskLruCache$Entry> r2 = r11.lruEntries     // Catch:{ all -> 0x0155 }
            java.lang.String r4 = r0.getKey$okhttp()     // Catch:{ all -> 0x0155 }
            r2.remove(r4)     // Catch:{ all -> 0x0155 }
            java.lang.String r2 = REMOVE     // Catch:{ all -> 0x0155 }
            okio.BufferedSink r2 = r1.writeUtf8(r2)     // Catch:{ all -> 0x0155 }
            r2.writeByte(r6)     // Catch:{ all -> 0x0155 }
            java.lang.String r2 = r0.getKey$okhttp()     // Catch:{ all -> 0x0155 }
            r1.writeUtf8(r2)     // Catch:{ all -> 0x0155 }
            r1.writeByte(r5)     // Catch:{ all -> 0x0155 }
            goto L_0x0124
        L_0x00ff:
            r0.setReadable$okhttp(r2)     // Catch:{ all -> 0x0155 }
            java.lang.String r2 = CLEAN     // Catch:{ all -> 0x0155 }
            okio.BufferedSink r2 = r1.writeUtf8(r2)     // Catch:{ all -> 0x0155 }
            r2.writeByte(r6)     // Catch:{ all -> 0x0155 }
            java.lang.String r2 = r0.getKey$okhttp()     // Catch:{ all -> 0x0155 }
            r1.writeUtf8(r2)     // Catch:{ all -> 0x0155 }
            r0.writeLengths$okhttp(r1)     // Catch:{ all -> 0x0155 }
            r1.writeByte(r5)     // Catch:{ all -> 0x0155 }
            if (r13 == 0) goto L_0x0124
            long r4 = r11.nextSequenceNumber     // Catch:{ all -> 0x0155 }
            r6 = 1
            long r6 = r6 + r4
            r11.nextSequenceNumber = r6     // Catch:{ all -> 0x0155 }
            r0.setSequenceNumber$okhttp(r4)     // Catch:{ all -> 0x0155 }
        L_0x0124:
            r1.flush()     // Catch:{ all -> 0x0155 }
            long r1 = r11.size     // Catch:{ all -> 0x0155 }
            long r3 = r11.maxSize     // Catch:{ all -> 0x0155 }
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 > 0) goto L_0x0138
            boolean r1 = r11.journalRebuildRequired()     // Catch:{ all -> 0x0155 }
            if (r1 == 0) goto L_0x0145
        L_0x0138:
            okhttp3.internal.concurrent.TaskQueue r1 = r11.cleanupQueue     // Catch:{ all -> 0x0155 }
            okhttp3.internal.cache.DiskLruCache$cleanupTask$1 r2 = r11.cleanupTask     // Catch:{ all -> 0x0155 }
            okhttp3.internal.concurrent.Task r2 = (okhttp3.internal.concurrent.Task) r2     // Catch:{ all -> 0x0155 }
            r3 = 0
            r5 = 2
            r6 = 0
            okhttp3.internal.concurrent.TaskQueue.schedule$default(r1, r2, r3, r5, r6)     // Catch:{ all -> 0x0155 }
        L_0x0145:
            monitor-exit(r11)
            return
        L_0x0147:
            java.lang.String r1 = "Check failed."
            java.lang.IllegalStateException r2 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0155 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0155 }
            r2.<init>(r1)     // Catch:{ all -> 0x0155 }
            java.lang.Throwable r2 = (java.lang.Throwable) r2     // Catch:{ all -> 0x0155 }
            throw r2     // Catch:{ all -> 0x0155 }
        L_0x0155:
            r12 = move-exception
            monitor-exit(r11)
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache.completeEdit$okhttp(okhttp3.internal.cache.DiskLruCache$Editor, boolean):void");
    }

    public final void delete() throws IOException {
        close();
        this.fileSystem.deleteContents(this.directory);
    }

    public final Editor edit(String str) throws IOException {
        return edit$default(this, str, 0, 2, (Object) null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002a, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized okhttp3.internal.cache.DiskLruCache.Editor edit(java.lang.String r10, long r11) throws java.io.IOException {
        /*
            r9 = this;
            monitor-enter(r9)
            java.lang.String r0 = "key"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r10, r0)     // Catch:{ all -> 0x009c }
            r9.initialize()     // Catch:{ all -> 0x009c }
            r9.checkNotClosed()     // Catch:{ all -> 0x009c }
            r9.validateKey(r10)     // Catch:{ all -> 0x009c }
            java.util.LinkedHashMap<java.lang.String, okhttp3.internal.cache.DiskLruCache$Entry> r0 = r9.lruEntries     // Catch:{ all -> 0x009c }
            java.lang.Object r0 = r0.get(r10)     // Catch:{ all -> 0x009c }
            okhttp3.internal.cache.DiskLruCache$Entry r0 = (okhttp3.internal.cache.DiskLruCache.Entry) r0     // Catch:{ all -> 0x009c }
            long r1 = ANY_SEQUENCE_NUMBER     // Catch:{ all -> 0x009c }
            int r1 = (r11 > r1 ? 1 : (r11 == r1 ? 0 : -1))
            r2 = 0
            if (r1 == 0) goto L_0x002b
            if (r0 == 0) goto L_0x0029
            long r3 = r0.getSequenceNumber$okhttp()     // Catch:{ all -> 0x009c }
            int r1 = (r3 > r11 ? 1 : (r3 == r11 ? 0 : -1))
            if (r1 == 0) goto L_0x002b
        L_0x0029:
            monitor-exit(r9)
            return r2
        L_0x002b:
            if (r0 == 0) goto L_0x0032
            okhttp3.internal.cache.DiskLruCache$Editor r1 = r0.getCurrentEditor$okhttp()     // Catch:{ all -> 0x009c }
            goto L_0x0033
        L_0x0032:
            r1 = r2
        L_0x0033:
            if (r1 == 0) goto L_0x0037
            monitor-exit(r9)
            return r2
        L_0x0037:
            if (r0 == 0) goto L_0x0041
            int r1 = r0.getLockingSourceCount$okhttp()     // Catch:{ all -> 0x009c }
            if (r1 == 0) goto L_0x0041
            monitor-exit(r9)
            return r2
        L_0x0041:
            boolean r1 = r9.mostRecentTrimFailed     // Catch:{ all -> 0x009c }
            if (r1 != 0) goto L_0x008c
            boolean r1 = r9.mostRecentRebuildFailed     // Catch:{ all -> 0x009c }
            if (r1 == 0) goto L_0x004a
            goto L_0x008c
        L_0x004a:
            okio.BufferedSink r1 = r9.journalWriter     // Catch:{ all -> 0x009c }
            kotlin.jvm.internal.Intrinsics.checkNotNull(r1)     // Catch:{ all -> 0x009c }
            java.lang.String r3 = DIRTY     // Catch:{ all -> 0x009c }
            okio.BufferedSink r3 = r1.writeUtf8(r3)     // Catch:{ all -> 0x009c }
            r4 = 32
            okio.BufferedSink r3 = r3.writeByte(r4)     // Catch:{ all -> 0x009c }
            okio.BufferedSink r3 = r3.writeUtf8(r10)     // Catch:{ all -> 0x009c }
            r4 = 10
            r3.writeByte(r4)     // Catch:{ all -> 0x009c }
            r1.flush()     // Catch:{ all -> 0x009c }
            boolean r3 = r9.hasJournalErrors     // Catch:{ all -> 0x009c }
            if (r3 == 0) goto L_0x0073
            monitor-exit(r9)
            return r2
        L_0x0073:
            if (r0 != 0) goto L_0x0082
            okhttp3.internal.cache.DiskLruCache$Entry r2 = new okhttp3.internal.cache.DiskLruCache$Entry     // Catch:{ all -> 0x009c }
            r2.<init>(r9, r10)     // Catch:{ all -> 0x009c }
            r0 = r2
            java.util.LinkedHashMap<java.lang.String, okhttp3.internal.cache.DiskLruCache$Entry> r2 = r9.lruEntries     // Catch:{ all -> 0x009c }
            java.util.Map r2 = (java.util.Map) r2     // Catch:{ all -> 0x009c }
            r2.put(r10, r0)     // Catch:{ all -> 0x009c }
        L_0x0082:
            okhttp3.internal.cache.DiskLruCache$Editor r2 = new okhttp3.internal.cache.DiskLruCache$Editor     // Catch:{ all -> 0x009c }
            r2.<init>(r9, r0)     // Catch:{ all -> 0x009c }
            r0.setCurrentEditor$okhttp(r2)     // Catch:{ all -> 0x009c }
            monitor-exit(r9)
            return r2
        L_0x008c:
            okhttp3.internal.concurrent.TaskQueue r3 = r9.cleanupQueue     // Catch:{ all -> 0x009c }
            okhttp3.internal.cache.DiskLruCache$cleanupTask$1 r1 = r9.cleanupTask     // Catch:{ all -> 0x009c }
            r4 = r1
            okhttp3.internal.concurrent.Task r4 = (okhttp3.internal.concurrent.Task) r4     // Catch:{ all -> 0x009c }
            r5 = 0
            r7 = 2
            r8 = 0
            okhttp3.internal.concurrent.TaskQueue.schedule$default(r3, r4, r5, r7, r8)     // Catch:{ all -> 0x009c }
            monitor-exit(r9)
            return r2
        L_0x009c:
            r10 = move-exception
            monitor-exit(r9)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache.edit(java.lang.String, long):okhttp3.internal.cache.DiskLruCache$Editor");
    }

    public final synchronized void evictAll() throws IOException {
        initialize();
        Collection<Entry> values = this.lruEntries.values();
        Intrinsics.checkNotNullExpressionValue(values, "lruEntries.values");
        Object[] array = values.toArray(new Entry[0]);
        if (array != null) {
            for (Entry entry : (Entry[]) array) {
                Intrinsics.checkNotNullExpressionValue(entry, "entry");
                removeEntry$okhttp(entry);
            }
            this.mostRecentTrimFailed = false;
        } else {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }
    }

    public synchronized void flush() throws IOException {
        if (this.initialized) {
            checkNotClosed();
            trimToSize();
            BufferedSink bufferedSink = this.journalWriter;
            Intrinsics.checkNotNull(bufferedSink);
            bufferedSink.flush();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0061, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized okhttp3.internal.cache.DiskLruCache.Snapshot get(java.lang.String r10) throws java.io.IOException {
        /*
            r9 = this;
            monitor-enter(r9)
            java.lang.String r0 = "key"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r10, r0)     // Catch:{ all -> 0x0066 }
            r9.initialize()     // Catch:{ all -> 0x0066 }
            r9.checkNotClosed()     // Catch:{ all -> 0x0066 }
            r9.validateKey(r10)     // Catch:{ all -> 0x0066 }
            java.util.LinkedHashMap<java.lang.String, okhttp3.internal.cache.DiskLruCache$Entry> r0 = r9.lruEntries     // Catch:{ all -> 0x0066 }
            java.lang.Object r0 = r0.get(r10)     // Catch:{ all -> 0x0066 }
            okhttp3.internal.cache.DiskLruCache$Entry r0 = (okhttp3.internal.cache.DiskLruCache.Entry) r0     // Catch:{ all -> 0x0066 }
            r1 = 0
            if (r0 == 0) goto L_0x0064
            java.lang.String r2 = "lruEntries[key] ?: return null"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r2)     // Catch:{ all -> 0x0066 }
            okhttp3.internal.cache.DiskLruCache$Snapshot r2 = r0.snapshot$okhttp()     // Catch:{ all -> 0x0066 }
            if (r2 == 0) goto L_0x0062
            r1 = r2
            int r2 = r9.redundantOpCount     // Catch:{ all -> 0x0066 }
            int r2 = r2 + 1
            r9.redundantOpCount = r2     // Catch:{ all -> 0x0066 }
            okio.BufferedSink r2 = r9.journalWriter     // Catch:{ all -> 0x0066 }
            kotlin.jvm.internal.Intrinsics.checkNotNull(r2)     // Catch:{ all -> 0x0066 }
            java.lang.String r3 = READ     // Catch:{ all -> 0x0066 }
            okio.BufferedSink r2 = r2.writeUtf8(r3)     // Catch:{ all -> 0x0066 }
            r3 = 32
            okio.BufferedSink r2 = r2.writeByte(r3)     // Catch:{ all -> 0x0066 }
            okio.BufferedSink r2 = r2.writeUtf8(r10)     // Catch:{ all -> 0x0066 }
            r3 = 10
            r2.writeByte(r3)     // Catch:{ all -> 0x0066 }
            boolean r2 = r9.journalRebuildRequired()     // Catch:{ all -> 0x0066 }
            if (r2 == 0) goto L_0x0060
            okhttp3.internal.concurrent.TaskQueue r3 = r9.cleanupQueue     // Catch:{ all -> 0x0066 }
            okhttp3.internal.cache.DiskLruCache$cleanupTask$1 r2 = r9.cleanupTask     // Catch:{ all -> 0x0066 }
            r4 = r2
            okhttp3.internal.concurrent.Task r4 = (okhttp3.internal.concurrent.Task) r4     // Catch:{ all -> 0x0066 }
            r5 = 0
            r7 = 2
            r8 = 0
            okhttp3.internal.concurrent.TaskQueue.schedule$default(r3, r4, r5, r7, r8)     // Catch:{ all -> 0x0066 }
        L_0x0060:
            monitor-exit(r9)
            return r1
        L_0x0062:
            monitor-exit(r9)
            return r1
        L_0x0064:
            monitor-exit(r9)
            return r1
        L_0x0066:
            r10 = move-exception
            monitor-exit(r9)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache.get(java.lang.String):okhttp3.internal.cache.DiskLruCache$Snapshot");
    }

    public final boolean getClosed$okhttp() {
        return this.closed;
    }

    public final File getDirectory() {
        return this.directory;
    }

    public final FileSystem getFileSystem$okhttp() {
        return this.fileSystem;
    }

    public final LinkedHashMap<String, Entry> getLruEntries$okhttp() {
        return this.lruEntries;
    }

    public final synchronized long getMaxSize() {
        return this.maxSize;
    }

    public final int getValueCount$okhttp() {
        return this.valueCount;
    }

    public final synchronized void initialize() throws IOException {
        if (Util.assertionsEnabled) {
            if (!Thread.holdsLock(this)) {
                StringBuilder append = new StringBuilder().append("Thread ");
                Thread currentThread = Thread.currentThread();
                Intrinsics.checkNotNullExpressionValue(currentThread, "Thread.currentThread()");
                throw new AssertionError(append.append(currentThread.getName()).append(" MUST hold lock on ").append(this).toString());
            }
        }
        if (!this.initialized) {
            if (this.fileSystem.exists(this.journalFileBackup)) {
                if (this.fileSystem.exists(this.journalFile)) {
                    this.fileSystem.delete(this.journalFileBackup);
                } else {
                    this.fileSystem.rename(this.journalFileBackup, this.journalFile);
                }
            }
            this.civilizedFileSystem = Util.isCivilized(this.fileSystem, this.journalFileBackup);
            if (this.fileSystem.exists(this.journalFile)) {
                try {
                    readJournal();
                    processJournal();
                    this.initialized = true;
                    return;
                } catch (IOException e) {
                    Platform.Companion.get().log("DiskLruCache " + this.directory + " is corrupt: " + e.getMessage() + ", removing", 5, e);
                    delete();
                    this.closed = false;
                } catch (Throwable th) {
                    this.closed = false;
                    throw th;
                }
            }
            rebuildJournal$okhttp();
            this.initialized = true;
        }
    }

    public final synchronized boolean isClosed() {
        return this.closed;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00c9, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        kotlin.io.CloseableKt.closeFinally(r0, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00cd, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized void rebuildJournal$okhttp() throws java.io.IOException {
        /*
            r9 = this;
            monitor-enter(r9)
            okio.BufferedSink r0 = r9.journalWriter     // Catch:{ all -> 0x00ce }
            if (r0 == 0) goto L_0x0008
            r0.close()     // Catch:{ all -> 0x00ce }
        L_0x0008:
            okhttp3.internal.io.FileSystem r0 = r9.fileSystem     // Catch:{ all -> 0x00ce }
            java.io.File r1 = r9.journalFileTmp     // Catch:{ all -> 0x00ce }
            okio.Sink r0 = r0.sink(r1)     // Catch:{ all -> 0x00ce }
            okio.BufferedSink r0 = okio.Okio.buffer((okio.Sink) r0)     // Catch:{ all -> 0x00ce }
            java.io.Closeable r0 = (java.io.Closeable) r0     // Catch:{ all -> 0x00ce }
            r1 = 0
            r2 = r1
            java.lang.Throwable r2 = (java.lang.Throwable) r2     // Catch:{ all -> 0x00ce }
            r2 = r0
            okio.BufferedSink r2 = (okio.BufferedSink) r2     // Catch:{ all -> 0x00c7 }
            r3 = 0
            java.lang.String r4 = MAGIC     // Catch:{ all -> 0x00c7 }
            okio.BufferedSink r4 = r2.writeUtf8(r4)     // Catch:{ all -> 0x00c7 }
            r5 = 10
            r4.writeByte(r5)     // Catch:{ all -> 0x00c7 }
            java.lang.String r4 = VERSION_1     // Catch:{ all -> 0x00c7 }
            okio.BufferedSink r4 = r2.writeUtf8(r4)     // Catch:{ all -> 0x00c7 }
            r4.writeByte(r5)     // Catch:{ all -> 0x00c7 }
            int r4 = r9.appVersion     // Catch:{ all -> 0x00c7 }
            long r6 = (long) r4     // Catch:{ all -> 0x00c7 }
            okio.BufferedSink r4 = r2.writeDecimalLong(r6)     // Catch:{ all -> 0x00c7 }
            r4.writeByte(r5)     // Catch:{ all -> 0x00c7 }
            int r4 = r9.valueCount     // Catch:{ all -> 0x00c7 }
            long r6 = (long) r4     // Catch:{ all -> 0x00c7 }
            okio.BufferedSink r4 = r2.writeDecimalLong(r6)     // Catch:{ all -> 0x00c7 }
            r4.writeByte(r5)     // Catch:{ all -> 0x00c7 }
            r2.writeByte(r5)     // Catch:{ all -> 0x00c7 }
            java.util.LinkedHashMap<java.lang.String, okhttp3.internal.cache.DiskLruCache$Entry> r4 = r9.lruEntries     // Catch:{ all -> 0x00c7 }
            java.util.Collection r4 = r4.values()     // Catch:{ all -> 0x00c7 }
            java.util.Iterator r4 = r4.iterator()     // Catch:{ all -> 0x00c7 }
        L_0x0053:
            boolean r6 = r4.hasNext()     // Catch:{ all -> 0x00c7 }
            if (r6 == 0) goto L_0x0092
            java.lang.Object r6 = r4.next()     // Catch:{ all -> 0x00c7 }
            okhttp3.internal.cache.DiskLruCache$Entry r6 = (okhttp3.internal.cache.DiskLruCache.Entry) r6     // Catch:{ all -> 0x00c7 }
            okhttp3.internal.cache.DiskLruCache$Editor r7 = r6.getCurrentEditor$okhttp()     // Catch:{ all -> 0x00c7 }
            r8 = 32
            if (r7 == 0) goto L_0x007b
            java.lang.String r7 = DIRTY     // Catch:{ all -> 0x00c7 }
            okio.BufferedSink r7 = r2.writeUtf8(r7)     // Catch:{ all -> 0x00c7 }
            r7.writeByte(r8)     // Catch:{ all -> 0x00c7 }
            java.lang.String r7 = r6.getKey$okhttp()     // Catch:{ all -> 0x00c7 }
            r2.writeUtf8(r7)     // Catch:{ all -> 0x00c7 }
            r2.writeByte(r5)     // Catch:{ all -> 0x00c7 }
            goto L_0x0091
        L_0x007b:
            java.lang.String r7 = CLEAN     // Catch:{ all -> 0x00c7 }
            okio.BufferedSink r7 = r2.writeUtf8(r7)     // Catch:{ all -> 0x00c7 }
            r7.writeByte(r8)     // Catch:{ all -> 0x00c7 }
            java.lang.String r7 = r6.getKey$okhttp()     // Catch:{ all -> 0x00c7 }
            r2.writeUtf8(r7)     // Catch:{ all -> 0x00c7 }
            r6.writeLengths$okhttp(r2)     // Catch:{ all -> 0x00c7 }
            r2.writeByte(r5)     // Catch:{ all -> 0x00c7 }
        L_0x0091:
            goto L_0x0053
        L_0x0092:
            kotlin.Unit r2 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x00c7 }
            kotlin.io.CloseableKt.closeFinally(r0, r1)     // Catch:{ all -> 0x00ce }
            okhttp3.internal.io.FileSystem r0 = r9.fileSystem     // Catch:{ all -> 0x00ce }
            java.io.File r1 = r9.journalFile     // Catch:{ all -> 0x00ce }
            boolean r0 = r0.exists(r1)     // Catch:{ all -> 0x00ce }
            if (r0 == 0) goto L_0x00aa
            okhttp3.internal.io.FileSystem r0 = r9.fileSystem     // Catch:{ all -> 0x00ce }
            java.io.File r1 = r9.journalFile     // Catch:{ all -> 0x00ce }
            java.io.File r2 = r9.journalFileBackup     // Catch:{ all -> 0x00ce }
            r0.rename(r1, r2)     // Catch:{ all -> 0x00ce }
        L_0x00aa:
            okhttp3.internal.io.FileSystem r0 = r9.fileSystem     // Catch:{ all -> 0x00ce }
            java.io.File r1 = r9.journalFileTmp     // Catch:{ all -> 0x00ce }
            java.io.File r2 = r9.journalFile     // Catch:{ all -> 0x00ce }
            r0.rename(r1, r2)     // Catch:{ all -> 0x00ce }
            okhttp3.internal.io.FileSystem r0 = r9.fileSystem     // Catch:{ all -> 0x00ce }
            java.io.File r1 = r9.journalFileBackup     // Catch:{ all -> 0x00ce }
            r0.delete(r1)     // Catch:{ all -> 0x00ce }
            okio.BufferedSink r0 = r9.newJournalWriter()     // Catch:{ all -> 0x00ce }
            r9.journalWriter = r0     // Catch:{ all -> 0x00ce }
            r0 = 0
            r9.hasJournalErrors = r0     // Catch:{ all -> 0x00ce }
            r9.mostRecentRebuildFailed = r0     // Catch:{ all -> 0x00ce }
            monitor-exit(r9)
            return
        L_0x00c7:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x00c9 }
        L_0x00c9:
            r2 = move-exception
            kotlin.io.CloseableKt.closeFinally(r0, r1)     // Catch:{ all -> 0x00ce }
            throw r2     // Catch:{ all -> 0x00ce }
        L_0x00ce:
            r0 = move-exception
            monitor-exit(r9)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache.rebuildJournal$okhttp():void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0030, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized boolean remove(java.lang.String r8) throws java.io.IOException {
        /*
            r7 = this;
            monitor-enter(r7)
            java.lang.String r0 = "key"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r8, r0)     // Catch:{ all -> 0x0033 }
            r7.initialize()     // Catch:{ all -> 0x0033 }
            r7.checkNotClosed()     // Catch:{ all -> 0x0033 }
            r7.validateKey(r8)     // Catch:{ all -> 0x0033 }
            java.util.LinkedHashMap<java.lang.String, okhttp3.internal.cache.DiskLruCache$Entry> r0 = r7.lruEntries     // Catch:{ all -> 0x0033 }
            java.lang.Object r0 = r0.get(r8)     // Catch:{ all -> 0x0033 }
            okhttp3.internal.cache.DiskLruCache$Entry r0 = (okhttp3.internal.cache.DiskLruCache.Entry) r0     // Catch:{ all -> 0x0033 }
            r1 = 0
            if (r0 == 0) goto L_0x0031
            java.lang.String r2 = "lruEntries[key] ?: return false"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r2)     // Catch:{ all -> 0x0033 }
            boolean r2 = r7.removeEntry$okhttp(r0)     // Catch:{ all -> 0x0033 }
            if (r2 == 0) goto L_0x002f
            long r3 = r7.size     // Catch:{ all -> 0x0033 }
            long r5 = r7.maxSize     // Catch:{ all -> 0x0033 }
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 > 0) goto L_0x002f
            r7.mostRecentTrimFailed = r1     // Catch:{ all -> 0x0033 }
        L_0x002f:
            monitor-exit(r7)
            return r2
        L_0x0031:
            monitor-exit(r7)
            return r1
        L_0x0033:
            r8 = move-exception
            monitor-exit(r7)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache.remove(java.lang.String):boolean");
    }

    public final boolean removeEntry$okhttp(Entry entry) throws IOException {
        BufferedSink bufferedSink;
        Intrinsics.checkNotNullParameter(entry, "entry");
        if (!this.civilizedFileSystem) {
            if (entry.getLockingSourceCount$okhttp() > 0 && (bufferedSink = this.journalWriter) != null) {
                bufferedSink.writeUtf8(DIRTY);
                bufferedSink.writeByte(32);
                bufferedSink.writeUtf8(entry.getKey$okhttp());
                bufferedSink.writeByte(10);
                bufferedSink.flush();
            }
            if (entry.getLockingSourceCount$okhttp() > 0 || entry.getCurrentEditor$okhttp() != null) {
                entry.setZombie$okhttp(true);
                return true;
            }
        }
        Editor currentEditor$okhttp = entry.getCurrentEditor$okhttp();
        if (currentEditor$okhttp != null) {
            currentEditor$okhttp.detach$okhttp();
        }
        int i = this.valueCount;
        for (int i2 = 0; i2 < i; i2++) {
            this.fileSystem.delete(entry.getCleanFiles$okhttp().get(i2));
            this.size -= entry.getLengths$okhttp()[i2];
            entry.getLengths$okhttp()[i2] = 0;
        }
        this.redundantOpCount++;
        BufferedSink bufferedSink2 = this.journalWriter;
        if (bufferedSink2 != null) {
            bufferedSink2.writeUtf8(REMOVE);
            bufferedSink2.writeByte(32);
            bufferedSink2.writeUtf8(entry.getKey$okhttp());
            bufferedSink2.writeByte(10);
        }
        this.lruEntries.remove(entry.getKey$okhttp());
        if (journalRebuildRequired()) {
            TaskQueue.schedule$default(this.cleanupQueue, this.cleanupTask, 0, 2, (Object) null);
        }
        return true;
    }

    public final void setClosed$okhttp(boolean z) {
        this.closed = z;
    }

    public final synchronized void setMaxSize(long value) {
        this.maxSize = value;
        if (this.initialized) {
            TaskQueue.schedule$default(this.cleanupQueue, this.cleanupTask, 0, 2, (Object) null);
        }
    }

    public final synchronized long size() throws IOException {
        initialize();
        return this.size;
    }

    public final synchronized Iterator<Snapshot> snapshots() throws IOException {
        initialize();
        return new DiskLruCache$snapshots$1(this);
    }

    public final void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            if (!removeOldestEntry()) {
                return;
            }
        }
        this.mostRecentTrimFailed = false;
    }
}
