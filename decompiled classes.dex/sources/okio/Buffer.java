package okio;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.work.WorkRequest;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.jvm.internal.CharCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.Charsets;
import kotlin.text.Typography;
import mt.Log1F380D;
import okhttp3.HttpUrl;
import okhttp3.internal.connection.RealConnection;
import okio.internal.BufferKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000ª\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u001a\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0005\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0017\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004:\u0002\u0001B\u0005¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0000H\u0016J\u0006\u0010\u0011\u001a\u00020\u0012J\b\u0010\u0013\u001a\u00020\u0000H\u0016J\b\u0010\u0014\u001a\u00020\u0012H\u0016J\u0006\u0010\u0015\u001a\u00020\fJ\u0006\u0010\u0016\u001a\u00020\u0000J$\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u00192\b\b\u0002\u0010\u001a\u001a\u00020\f2\b\b\u0002\u0010\u001b\u001a\u00020\fH\u0007J\u0018\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u001a\u001a\u00020\fJ \u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u001a\u001a\u00020\f2\u0006\u0010\u001b\u001a\u00020\fJ\u0010\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\b\u0010 \u001a\u00020\u0000H\u0016J\b\u0010!\u001a\u00020\u0000H\u0016J\u0013\u0010\"\u001a\u00020#2\b\u0010$\u001a\u0004\u0018\u00010%H\u0002J\b\u0010&\u001a\u00020#H\u0016J\b\u0010'\u001a\u00020\u0012H\u0016J\u0016\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020\fH\u0002¢\u0006\u0002\b+J\u0015\u0010+\u001a\u00020)2\u0006\u0010,\u001a\u00020\fH\u0007¢\u0006\u0002\b-J\b\u0010.\u001a\u00020/H\u0016J\u0018\u00100\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u00101\u001a\u00020\u001dH\u0002J\u000e\u00102\u001a\u00020\u001d2\u0006\u00101\u001a\u00020\u001dJ\u000e\u00103\u001a\u00020\u001d2\u0006\u00101\u001a\u00020\u001dJ\u000e\u00104\u001a\u00020\u001d2\u0006\u00101\u001a\u00020\u001dJ\u0010\u00105\u001a\u00020\f2\u0006\u00106\u001a\u00020)H\u0016J\u0018\u00105\u001a\u00020\f2\u0006\u00106\u001a\u00020)2\u0006\u00107\u001a\u00020\fH\u0016J \u00105\u001a\u00020\f2\u0006\u00106\u001a\u00020)2\u0006\u00107\u001a\u00020\f2\u0006\u00108\u001a\u00020\fH\u0016J\u0010\u00105\u001a\u00020\f2\u0006\u00109\u001a\u00020\u001dH\u0016J\u0018\u00105\u001a\u00020\f2\u0006\u00109\u001a\u00020\u001d2\u0006\u00107\u001a\u00020\fH\u0016J\u0010\u0010:\u001a\u00020\f2\u0006\u0010;\u001a\u00020\u001dH\u0016J\u0018\u0010:\u001a\u00020\f2\u0006\u0010;\u001a\u00020\u001d2\u0006\u00107\u001a\u00020\fH\u0016J\b\u0010<\u001a\u00020=H\u0016J\b\u0010>\u001a\u00020#H\u0016J\u0006\u0010?\u001a\u00020\u001dJ\b\u0010@\u001a\u00020\u0019H\u0016J\b\u0010A\u001a\u00020\u0001H\u0016J\u0018\u0010B\u001a\u00020#2\u0006\u0010\u001a\u001a\u00020\f2\u0006\u00109\u001a\u00020\u001dH\u0016J(\u0010B\u001a\u00020#2\u0006\u0010\u001a\u001a\u00020\f2\u0006\u00109\u001a\u00020\u001d2\u0006\u0010C\u001a\u00020/2\u0006\u0010\u001b\u001a\u00020/H\u0016J\u0010\u0010D\u001a\u00020/2\u0006\u0010E\u001a\u00020FH\u0016J\u0010\u0010D\u001a\u00020/2\u0006\u0010E\u001a\u00020GH\u0016J \u0010D\u001a\u00020/2\u0006\u0010E\u001a\u00020G2\u0006\u0010\u001a\u001a\u00020/2\u0006\u0010\u001b\u001a\u00020/H\u0016J\u0018\u0010D\u001a\u00020\f2\u0006\u0010E\u001a\u00020\u00002\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u0010\u0010H\u001a\u00020\f2\u0006\u0010E\u001a\u00020IH\u0016J\u0012\u0010J\u001a\u00020K2\b\b\u0002\u0010L\u001a\u00020KH\u0007J\b\u0010M\u001a\u00020)H\u0016J\b\u0010N\u001a\u00020GH\u0016J\u0010\u0010N\u001a\u00020G2\u0006\u0010\u001b\u001a\u00020\fH\u0016J\b\u0010O\u001a\u00020\u001dH\u0016J\u0010\u0010O\u001a\u00020\u001d2\u0006\u0010\u001b\u001a\u00020\fH\u0016J\b\u0010P\u001a\u00020\fH\u0016J\u000e\u0010Q\u001a\u00020\u00002\u0006\u0010R\u001a\u00020=J\u0016\u0010Q\u001a\u00020\u00002\u0006\u0010R\u001a\u00020=2\u0006\u0010\u001b\u001a\u00020\fJ \u0010Q\u001a\u00020\u00122\u0006\u0010R\u001a\u00020=2\u0006\u0010\u001b\u001a\u00020\f2\u0006\u0010S\u001a\u00020#H\u0002J\u0010\u0010T\u001a\u00020\u00122\u0006\u0010E\u001a\u00020GH\u0016J\u0018\u0010T\u001a\u00020\u00122\u0006\u0010E\u001a\u00020\u00002\u0006\u0010\u001b\u001a\u00020\fH\u0016J\b\u0010U\u001a\u00020\fH\u0016J\b\u0010V\u001a\u00020/H\u0016J\b\u0010W\u001a\u00020/H\u0016J\b\u0010X\u001a\u00020\fH\u0016J\b\u0010Y\u001a\u00020\fH\u0016J\b\u0010Z\u001a\u00020[H\u0016J\b\u0010\\\u001a\u00020[H\u0016J\u0010\u0010]\u001a\u00020\u001f2\u0006\u0010^\u001a\u00020_H\u0016J\u0018\u0010]\u001a\u00020\u001f2\u0006\u0010\u001b\u001a\u00020\f2\u0006\u0010^\u001a\u00020_H\u0016J\u0012\u0010`\u001a\u00020K2\b\b\u0002\u0010L\u001a\u00020KH\u0007J\b\u0010a\u001a\u00020\u001fH\u0016J\u0010\u0010a\u001a\u00020\u001f2\u0006\u0010\u001b\u001a\u00020\fH\u0016J\b\u0010b\u001a\u00020/H\u0016J\n\u0010c\u001a\u0004\u0018\u00010\u001fH\u0016J\b\u0010d\u001a\u00020\u001fH\u0016J\u0010\u0010d\u001a\u00020\u001f2\u0006\u0010e\u001a\u00020\fH\u0016J\u0010\u0010f\u001a\u00020#2\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u0010\u0010g\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u0010\u0010h\u001a\u00020/2\u0006\u0010i\u001a\u00020jH\u0016J\u0006\u0010k\u001a\u00020\u001dJ\u0006\u0010l\u001a\u00020\u001dJ\u0006\u0010m\u001a\u00020\u001dJ\r\u0010\r\u001a\u00020\fH\u0007¢\u0006\u0002\bnJ\u0010\u0010o\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u0006\u0010p\u001a\u00020\u001dJ\u000e\u0010p\u001a\u00020\u001d2\u0006\u0010\u001b\u001a\u00020/J\b\u0010q\u001a\u00020rH\u0016J\b\u0010s\u001a\u00020\u001fH\u0016J\u0015\u0010t\u001a\u00020\n2\u0006\u0010u\u001a\u00020/H\u0000¢\u0006\u0002\bvJ\u0010\u0010w\u001a\u00020/2\u0006\u0010x\u001a\u00020FH\u0016J\u0010\u0010w\u001a\u00020\u00002\u0006\u0010x\u001a\u00020GH\u0016J \u0010w\u001a\u00020\u00002\u0006\u0010x\u001a\u00020G2\u0006\u0010\u001a\u001a\u00020/2\u0006\u0010\u001b\u001a\u00020/H\u0016J\u0018\u0010w\u001a\u00020\u00122\u0006\u0010x\u001a\u00020\u00002\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u0010\u0010w\u001a\u00020\u00002\u0006\u0010y\u001a\u00020\u001dH\u0016J \u0010w\u001a\u00020\u00002\u0006\u0010y\u001a\u00020\u001d2\u0006\u0010\u001a\u001a\u00020/2\u0006\u0010\u001b\u001a\u00020/H\u0016J\u0018\u0010w\u001a\u00020\u00002\u0006\u0010x\u001a\u00020z2\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u0010\u0010{\u001a\u00020\f2\u0006\u0010x\u001a\u00020zH\u0016J\u0010\u0010|\u001a\u00020\u00002\u0006\u00106\u001a\u00020/H\u0016J\u0010\u0010}\u001a\u00020\u00002\u0006\u0010~\u001a\u00020\fH\u0016J\u0010\u0010\u001a\u00020\u00002\u0006\u0010~\u001a\u00020\fH\u0016J\u0012\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020/H\u0016J\u0012\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020/H\u0016J\u0011\u0010\u0001\u001a\u00020\u00002\u0006\u0010~\u001a\u00020\fH\u0016J\u0011\u0010\u0001\u001a\u00020\u00002\u0006\u0010~\u001a\u00020\fH\u0016J\u0012\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020/H\u0016J\u0012\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020/H\u0016J\u001a\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020\u001f2\u0006\u0010^\u001a\u00020_H\u0016J,\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020\u001f2\u0007\u0010\u0001\u001a\u00020/2\u0007\u0010\u0001\u001a\u00020/2\u0006\u0010^\u001a\u00020_H\u0016J\u001b\u0010\u0001\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u00192\b\b\u0002\u0010\u001b\u001a\u00020\fH\u0007J\u0012\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020\u001fH\u0016J$\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020\u001f2\u0007\u0010\u0001\u001a\u00020/2\u0007\u0010\u0001\u001a\u00020/H\u0016J\u0012\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020/H\u0016R\u0014\u0010\u0006\u001a\u00020\u00008VX\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u0004\u0018\u00010\n8\u0000@\u0000X\u000e¢\u0006\u0002\n\u0000R&\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f8G@@X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010¨\u0006\u0001"}, d2 = {"Lokio/Buffer;", "Lokio/BufferedSource;", "Lokio/BufferedSink;", "", "Ljava/nio/channels/ByteChannel;", "()V", "buffer", "getBuffer", "()Lokio/Buffer;", "head", "Lokio/Segment;", "<set-?>", "", "size", "()J", "setSize$okio", "(J)V", "clear", "", "clone", "close", "completeSegmentByteCount", "copy", "copyTo", "out", "Ljava/io/OutputStream;", "offset", "byteCount", "digest", "Lokio/ByteString;", "algorithm", "", "emit", "emitCompleteSegments", "equals", "", "other", "", "exhausted", "flush", "get", "", "pos", "getByte", "index", "-deprecated_getByte", "hashCode", "", "hmac", "key", "hmacSha1", "hmacSha256", "hmacSha512", "indexOf", "b", "fromIndex", "toIndex", "bytes", "indexOfElement", "targetBytes", "inputStream", "Ljava/io/InputStream;", "isOpen", "md5", "outputStream", "peek", "rangeEquals", "bytesOffset", "read", "sink", "Ljava/nio/ByteBuffer;", "", "readAll", "Lokio/Sink;", "readAndWriteUnsafe", "Lokio/Buffer$UnsafeCursor;", "unsafeCursor", "readByte", "readByteArray", "readByteString", "readDecimalLong", "readFrom", "input", "forever", "readFully", "readHexadecimalUnsignedLong", "readInt", "readIntLe", "readLong", "readLongLe", "readShort", "", "readShortLe", "readString", "charset", "Ljava/nio/charset/Charset;", "readUnsafe", "readUtf8", "readUtf8CodePoint", "readUtf8Line", "readUtf8LineStrict", "limit", "request", "require", "select", "options", "Lokio/Options;", "sha1", "sha256", "sha512", "-deprecated_size", "skip", "snapshot", "timeout", "Lokio/Timeout;", "toString", "writableSegment", "minimumCapacity", "writableSegment$okio", "write", "source", "byteString", "Lokio/Source;", "writeAll", "writeByte", "writeDecimalLong", "v", "writeHexadecimalUnsignedLong", "writeInt", "i", "writeIntLe", "writeLong", "writeLongLe", "writeShort", "s", "writeShortLe", "writeString", "string", "beginIndex", "endIndex", "writeTo", "writeUtf8", "writeUtf8CodePoint", "codePoint", "UnsafeCursor", "okio"}, k = 1, mv = {1, 4, 0})
/* compiled from: 01EC */
public final class Buffer implements BufferedSource, BufferedSink, Cloneable, ByteChannel {
    public Segment head;
    private long size;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u000e\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\bJ\u0006\u0010\u0014\u001a\u00020\bJ\u000e\u0010\u0015\u001a\u00020\n2\u0006\u0010\u0016\u001a\u00020\nJ\u000e\u0010\u0017\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0007\u001a\u00020\b8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\t\u001a\u00020\n8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000b\u001a\u00020\f8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000f\u001a\u00020\b8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lokio/Buffer$UnsafeCursor;", "Ljava/io/Closeable;", "()V", "buffer", "Lokio/Buffer;", "data", "", "end", "", "offset", "", "readWrite", "", "segment", "Lokio/Segment;", "start", "close", "", "expandBuffer", "minByteCount", "next", "resizeBuffer", "newSize", "seek", "okio"}, k = 1, mv = {1, 4, 0})
    /* compiled from: 01EB */
    public static final class UnsafeCursor implements Closeable {
        public Buffer buffer;
        public byte[] data;
        public int end = -1;
        public long offset = -1;
        public boolean readWrite;
        private Segment segment;
        public int start = -1;

        public void close() {
            if (this.buffer != null) {
                Buffer buffer2 = null;
                this.buffer = null;
                Segment segment2 = null;
                this.segment = null;
                this.offset = -1;
                byte[] bArr = null;
                this.data = null;
                this.start = -1;
                this.end = -1;
                return;
            }
            throw new IllegalStateException("not attached to a buffer".toString());
        }

        public final long expandBuffer(int minByteCount) {
            boolean z = true;
            if (minByteCount > 0) {
                if (minByteCount > 8192) {
                    z = false;
                }
                if (z) {
                    Buffer buffer2 = this.buffer;
                    if (buffer2 == null) {
                        throw new IllegalStateException("not attached to a buffer".toString());
                    } else if (this.readWrite) {
                        long size = buffer2.size();
                        Segment writableSegment$okio = buffer2.writableSegment$okio(minByteCount);
                        int i = 8192 - writableSegment$okio.limit;
                        writableSegment$okio.limit = 8192;
                        buffer2.setSize$okio(((long) i) + size);
                        this.segment = writableSegment$okio;
                        this.offset = size;
                        this.data = writableSegment$okio.data;
                        this.start = 8192 - i;
                        this.end = 8192;
                        return (long) i;
                    } else {
                        throw new IllegalStateException("expandBuffer() only permitted for read/write buffers".toString());
                    }
                } else {
                    throw new IllegalArgumentException(("minByteCount > Segment.SIZE: " + minByteCount).toString());
                }
            } else {
                throw new IllegalArgumentException(("minByteCount <= 0: " + minByteCount).toString());
            }
        }

        public final int next() {
            long j = this.offset;
            Buffer buffer2 = this.buffer;
            Intrinsics.checkNotNull(buffer2);
            if (j != buffer2.size()) {
                long j2 = this.offset;
                return seek(j2 == -1 ? 0 : j2 + ((long) (this.end - this.start)));
            }
            throw new IllegalStateException("no more bytes".toString());
        }

        public final long resizeBuffer(long newSize) {
            long j = newSize;
            Buffer buffer2 = this.buffer;
            if (buffer2 == null) {
                throw new IllegalStateException("not attached to a buffer".toString());
            } else if (this.readWrite) {
                long size = buffer2.size();
                int i = 1;
                if (j <= size) {
                    if (j < 0) {
                        i = 0;
                    }
                    if (i != 0) {
                        long j2 = size - j;
                        while (true) {
                            if (j2 <= 0) {
                                break;
                            }
                            Segment segment2 = buffer2.head;
                            Intrinsics.checkNotNull(segment2);
                            Segment segment3 = segment2.prev;
                            Intrinsics.checkNotNull(segment3);
                            int i2 = segment3.limit - segment3.pos;
                            if (((long) i2) > j2) {
                                segment3.limit -= (int) j2;
                                break;
                            }
                            buffer2.head = segment3.pop();
                            SegmentPool.recycle(segment3);
                            j2 -= (long) i2;
                        }
                        Segment segment4 = null;
                        this.segment = null;
                        this.offset = j;
                        byte[] bArr = null;
                        this.data = null;
                        this.start = -1;
                        this.end = -1;
                    } else {
                        throw new IllegalArgumentException(("newSize < 0: " + j).toString());
                    }
                } else if (j > size) {
                    boolean z = true;
                    long j3 = j - size;
                    for (long j4 = 0; j3 > j4; j4 = 0) {
                        Segment writableSegment$okio = buffer2.writableSegment$okio(i);
                        int min = (int) Math.min(j3, (long) (8192 - writableSegment$okio.limit));
                        writableSegment$okio.limit += min;
                        j3 -= (long) min;
                        if (z) {
                            this.segment = writableSegment$okio;
                            this.offset = size;
                            this.data = writableSegment$okio.data;
                            this.start = writableSegment$okio.limit - min;
                            this.end = writableSegment$okio.limit;
                            z = false;
                        }
                        i = 1;
                    }
                }
                buffer2.setSize$okio(j);
                return size;
            } else {
                throw new IllegalStateException("resizeBuffer() only permitted for read/write buffers".toString());
            }
        }

        public final int seek(long offset2) {
            long j;
            Segment segment2;
            long j2 = offset2;
            Buffer buffer2 = this.buffer;
            if (buffer2 == null) {
                throw new IllegalStateException("not attached to a buffer".toString());
            } else if (j2 < ((long) -1) || j2 > buffer2.size()) {
                StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
                String format = String.format("offset=%s > size=%s", Arrays.copyOf(new Object[]{Long.valueOf(offset2), Long.valueOf(buffer2.size())}, 2));
                Log1F380D.a((Object) format);
                Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
                throw new ArrayIndexOutOfBoundsException(format);
            } else if (j2 == -1 || j2 == buffer2.size()) {
                Segment segment3 = null;
                this.segment = null;
                this.offset = j2;
                byte[] bArr = null;
                this.data = null;
                this.start = -1;
                this.end = -1;
                return -1;
            } else {
                long j3 = 0;
                long size = buffer2.size();
                Segment segment4 = buffer2.head;
                Segment segment5 = buffer2.head;
                Segment segment6 = this.segment;
                if (segment6 != null) {
                    long j4 = this.offset;
                    int i = this.start;
                    Intrinsics.checkNotNull(segment6);
                    long j5 = j4 - ((long) (i - segment6.pos));
                    if (j5 > j2) {
                        size = j5;
                        segment5 = this.segment;
                    } else {
                        j3 = j5;
                        segment4 = this.segment;
                    }
                }
                if (size - j2 > j2 - j3) {
                    segment2 = segment4;
                    j = j3;
                    while (true) {
                        Intrinsics.checkNotNull(segment2);
                        if (j2 < ((long) (segment2.limit - segment2.pos)) + j) {
                            break;
                        }
                        j += (long) (segment2.limit - segment2.pos);
                        segment2 = segment2.next;
                    }
                } else {
                    Segment segment7 = segment5;
                    long j6 = size;
                    while (j > j2) {
                        Intrinsics.checkNotNull(segment2);
                        segment7 = segment2.prev;
                        Intrinsics.checkNotNull(segment7);
                        j6 = j - ((long) (segment7.limit - segment7.pos));
                    }
                }
                if (this.readWrite) {
                    Intrinsics.checkNotNull(segment2);
                    if (segment2.shared) {
                        Segment unsharedCopy = segment2.unsharedCopy();
                        if (buffer2.head == segment2) {
                            buffer2.head = unsharedCopy;
                        }
                        segment2 = segment2.push(unsharedCopy);
                        Segment segment8 = segment2.prev;
                        Intrinsics.checkNotNull(segment8);
                        segment8.pop();
                    }
                }
                this.segment = segment2;
                this.offset = j2;
                Intrinsics.checkNotNull(segment2);
                this.data = segment2.data;
                this.start = segment2.pos + ((int) (j2 - j));
                int i2 = segment2.limit;
                this.end = i2;
                return i2 - this.start;
            }
        }
    }

    public static /* synthetic */ Buffer copyTo$default(Buffer buffer, OutputStream outputStream, long j, long j2, int i, Object obj) throws IOException {
        long j3 = (i & 2) != 0 ? 0 : j;
        return buffer.copyTo(outputStream, j3, (i & 4) != 0 ? buffer.size - j3 : j2);
    }

    public static /* synthetic */ Buffer copyTo$default(Buffer buffer, Buffer buffer2, long j, int i, Object obj) {
        if ((i & 2) != 0) {
            j = 0;
        }
        return buffer.copyTo(buffer2, j);
    }

    public static /* synthetic */ Buffer copyTo$default(Buffer buffer, Buffer buffer2, long j, long j2, int i, Object obj) {
        if ((i & 2) != 0) {
            j = 0;
        }
        return buffer.copyTo(buffer2, j, j2);
    }

    private final ByteString digest(String algorithm) {
        MessageDigest instance = MessageDigest.getInstance(algorithm);
        Segment segment = this.head;
        if (segment != null) {
            instance.update(segment.data, segment.pos, segment.limit - segment.pos);
            Segment segment2 = segment.next;
            Intrinsics.checkNotNull(segment2);
            while (segment2 != segment) {
                instance.update(segment2.data, segment2.pos, segment2.limit - segment2.pos);
                Segment segment3 = segment2.next;
                Intrinsics.checkNotNull(segment3);
                segment2 = segment3;
            }
        }
        byte[] digest = instance.digest();
        Intrinsics.checkNotNullExpressionValue(digest, "messageDigest.digest()");
        return new ByteString(digest);
    }

    private final ByteString hmac(String algorithm, ByteString key) {
        try {
            Mac instance = Mac.getInstance(algorithm);
            instance.init(new SecretKeySpec(key.internalArray$okio(), algorithm));
            Segment segment = this.head;
            if (segment != null) {
                instance.update(segment.data, segment.pos, segment.limit - segment.pos);
                Segment segment2 = segment.next;
                Intrinsics.checkNotNull(segment2);
                while (segment2 != segment) {
                    instance.update(segment2.data, segment2.pos, segment2.limit - segment2.pos);
                    Segment segment3 = segment2.next;
                    Intrinsics.checkNotNull(segment3);
                    segment2 = segment3;
                }
            }
            byte[] doFinal = instance.doFinal();
            Intrinsics.checkNotNullExpressionValue(doFinal, "mac.doFinal()");
            return new ByteString(doFinal);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static /* synthetic */ UnsafeCursor readAndWriteUnsafe$default(Buffer buffer, UnsafeCursor unsafeCursor, int i, Object obj) {
        if ((i & 1) != 0) {
            unsafeCursor = new UnsafeCursor();
        }
        return buffer.readAndWriteUnsafe(unsafeCursor);
    }

    private final void readFrom(InputStream input, long byteCount, boolean forever) throws IOException {
        long j = byteCount;
        while (true) {
            if (j > 0 || forever) {
                Segment writableSegment$okio = writableSegment$okio(1);
                int read = input.read(writableSegment$okio.data, writableSegment$okio.limit, (int) Math.min(j, (long) (8192 - writableSegment$okio.limit)));
                if (read == -1) {
                    if (writableSegment$okio.pos == writableSegment$okio.limit) {
                        this.head = writableSegment$okio.pop();
                        SegmentPool.recycle(writableSegment$okio);
                    }
                    if (!forever) {
                        throw new EOFException();
                    }
                    return;
                }
                writableSegment$okio.limit += read;
                this.size += (long) read;
                j -= (long) read;
            } else {
                return;
            }
        }
    }

    public static /* synthetic */ UnsafeCursor readUnsafe$default(Buffer buffer, UnsafeCursor unsafeCursor, int i, Object obj) {
        if ((i & 1) != 0) {
            unsafeCursor = new UnsafeCursor();
        }
        return buffer.readUnsafe(unsafeCursor);
    }

    public static /* synthetic */ Buffer writeTo$default(Buffer buffer, OutputStream outputStream, long j, int i, Object obj) throws IOException {
        if ((i & 2) != 0) {
            j = buffer.size;
        }
        return buffer.writeTo(outputStream, j);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to operator function", replaceWith = @ReplaceWith(expression = "this[index]", imports = {}))
    /* renamed from: -deprecated_getByte  reason: not valid java name */
    public final byte m1725deprecated_getByte(long index) {
        return getByte(index);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "size", imports = {}))
    /* renamed from: -deprecated_size  reason: not valid java name */
    public final long m1726deprecated_size() {
        return this.size;
    }

    public Buffer buffer() {
        return this;
    }

    public final void clear() {
        skip(size());
    }

    public Buffer clone() {
        return copy();
    }

    public void close() {
    }

    public final long completeSegmentByteCount() {
        long size2 = size();
        if (size2 == 0) {
            return 0;
        }
        Segment segment = this.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment.prev;
        Intrinsics.checkNotNull(segment2);
        if (segment2.limit < 8192 && segment2.owner) {
            size2 -= (long) (segment2.limit - segment2.pos);
        }
        return size2;
    }

    public final Buffer copy() {
        Buffer buffer = new Buffer();
        if (size() != 0) {
            Segment segment = this.head;
            Intrinsics.checkNotNull(segment);
            Segment sharedCopy = segment.sharedCopy();
            buffer.head = sharedCopy;
            sharedCopy.prev = sharedCopy;
            sharedCopy.next = sharedCopy.prev;
            for (Segment segment2 = segment.next; segment2 != segment; segment2 = segment2.next) {
                Segment segment3 = sharedCopy.prev;
                Intrinsics.checkNotNull(segment3);
                Intrinsics.checkNotNull(segment2);
                segment3.push(segment2.sharedCopy());
            }
            buffer.setSize$okio(size());
        }
        return buffer;
    }

    public final Buffer copyTo(OutputStream outputStream) throws IOException {
        return copyTo$default(this, outputStream, 0, 0, 6, (Object) null);
    }

    public final Buffer copyTo(OutputStream outputStream, long j) throws IOException {
        return copyTo$default(this, outputStream, j, 0, 4, (Object) null);
    }

    public final Buffer copyTo(OutputStream out, long offset, long byteCount) throws IOException {
        OutputStream outputStream = out;
        Intrinsics.checkNotNullParameter(out, "out");
        long j = offset;
        long j2 = byteCount;
        Util.checkOffsetAndCount(this.size, j, j2);
        if (j2 == 0) {
            return this;
        }
        Segment segment = this.head;
        while (true) {
            Intrinsics.checkNotNull(segment);
            if (j < ((long) (segment.limit - segment.pos))) {
                break;
            }
            j -= (long) (segment.limit - segment.pos);
            segment = segment.next;
        }
        while (j2 > 0) {
            Intrinsics.checkNotNull(segment);
            int i = (int) (((long) segment.pos) + j);
            int min = (int) Math.min((long) (segment.limit - i), j2);
            out.write(segment.data, i, min);
            j2 -= (long) min;
            j = 0;
            segment = segment.next;
        }
        return this;
    }

    public final Buffer copyTo(Buffer out, long offset) {
        Intrinsics.checkNotNullParameter(out, "out");
        return copyTo(out, offset, this.size - offset);
    }

    public final Buffer copyTo(Buffer out, long offset, long byteCount) {
        Buffer buffer = out;
        Intrinsics.checkNotNullParameter(out, "out");
        long j = offset;
        long j2 = byteCount;
        Util.checkOffsetAndCount(size(), j, j2);
        if (j2 != 0) {
            out.setSize$okio(out.size() + j2);
            Segment segment = this.head;
            while (true) {
                Intrinsics.checkNotNull(segment);
                if (j < ((long) (segment.limit - segment.pos))) {
                    break;
                }
                j -= (long) (segment.limit - segment.pos);
                segment = segment.next;
            }
            while (j2 > 0) {
                Intrinsics.checkNotNull(segment);
                Segment sharedCopy = segment.sharedCopy();
                sharedCopy.pos += (int) j;
                sharedCopy.limit = Math.min(sharedCopy.pos + ((int) j2), sharedCopy.limit);
                Segment segment2 = buffer.head;
                if (segment2 == null) {
                    sharedCopy.prev = sharedCopy;
                    sharedCopy.next = sharedCopy.prev;
                    buffer.head = sharedCopy.next;
                } else {
                    Intrinsics.checkNotNull(segment2);
                    Segment segment3 = segment2.prev;
                    Intrinsics.checkNotNull(segment3);
                    segment3.push(sharedCopy);
                }
                j2 -= (long) (sharedCopy.limit - sharedCopy.pos);
                j = 0;
                segment = segment.next;
            }
        }
        return this;
    }

    public Buffer emit() {
        return this;
    }

    public Buffer emitCompleteSegments() {
        return this;
    }

    /* JADX WARNING: type inference failed for: r21v0, types: [java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r21) {
        /*
            r20 = this;
            r0 = r21
            r1 = r20
            r2 = 0
            r4 = 1
            if (r1 != r0) goto L_0x000b
            r3 = r4
            goto L_0x0096
        L_0x000b:
            boolean r5 = r0 instanceof okio.Buffer
            if (r5 != 0) goto L_0x0012
            r3 = 0
            goto L_0x0096
        L_0x0012:
            long r5 = r1.size()
            r7 = r0
            okio.Buffer r7 = (okio.Buffer) r7
            long r7 = r7.size()
            int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r5 == 0) goto L_0x0024
            r3 = 0
            goto L_0x0096
        L_0x0024:
            long r5 = r1.size()
            r7 = 0
            int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r5 != 0) goto L_0x0031
            r3 = r4
            goto L_0x0096
        L_0x0031:
            okio.Segment r5 = r1.head
            kotlin.jvm.internal.Intrinsics.checkNotNull(r5)
            r6 = r0
            okio.Buffer r6 = (okio.Buffer) r6
            okio.Segment r6 = r6.head
            kotlin.jvm.internal.Intrinsics.checkNotNull(r6)
            int r9 = r5.pos
            int r10 = r6.pos
            r11 = 0
            r13 = 0
        L_0x0046:
            long r15 = r1.size()
            int r15 = (r11 > r15 ? 1 : (r11 == r15 ? 0 : -1))
            if (r15 >= 0) goto L_0x0095
            int r15 = r5.limit
            int r15 = r15 - r9
            int r3 = r6.limit
            int r3 = r3 - r10
            int r3 = java.lang.Math.min(r15, r3)
            long r13 = (long) r3
            r17 = r7
        L_0x005b:
            int r3 = (r17 > r13 ? 1 : (r17 == r13 ? 0 : -1))
            if (r3 >= 0) goto L_0x0079
            byte[] r3 = r5.data
            int r15 = r9 + 1
            byte r3 = r3[r9]
            byte[] r9 = r6.data
            int r19 = r10 + 1
            byte r9 = r9[r10]
            if (r3 == r9) goto L_0x006f
            r3 = 0
            goto L_0x0096
        L_0x006f:
            r9 = 1
            long r9 = r17 + r9
            r17 = r9
            r9 = r15
            r10 = r19
            goto L_0x005b
        L_0x0079:
            int r3 = r5.limit
            if (r9 != r3) goto L_0x0086
            okio.Segment r3 = r5.next
            kotlin.jvm.internal.Intrinsics.checkNotNull(r3)
            int r5 = r3.pos
            r9 = r5
            r5 = r3
        L_0x0086:
            int r3 = r6.limit
            if (r10 != r3) goto L_0x0093
            okio.Segment r3 = r6.next
            kotlin.jvm.internal.Intrinsics.checkNotNull(r3)
            int r6 = r3.pos
            r10 = r6
            r6 = r3
        L_0x0093:
            long r11 = r11 + r13
            goto L_0x0046
        L_0x0095:
            r3 = r4
        L_0x0096:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Buffer.equals(java.lang.Object):boolean");
    }

    public boolean exhausted() {
        return this.size == 0;
    }

    public void flush() {
    }

    public Buffer getBuffer() {
        return this;
    }

    public final byte getByte(long pos) {
        Util.checkOffsetAndCount(size(), pos, 1);
        Segment segment = this.head;
        if (segment == null) {
            Segment segment2 = null;
            Intrinsics.checkNotNull(segment2);
            return segment2.data[(int) ((((long) segment2.pos) + pos) - -1)];
        } else if (size() - pos < pos) {
            long size2 = size();
            while (size2 > pos) {
                Segment segment3 = segment.prev;
                Intrinsics.checkNotNull(segment3);
                segment = segment3;
                size2 -= (long) (segment.limit - segment.pos);
            }
            Segment segment4 = segment;
            Intrinsics.checkNotNull(segment4);
            return segment4.data[(int) ((((long) segment4.pos) + pos) - size2)];
        } else {
            long j = 0;
            while (true) {
                long j2 = ((long) (segment.limit - segment.pos)) + j;
                if (j2 > pos) {
                    Segment segment5 = segment;
                    Intrinsics.checkNotNull(segment5);
                    return segment5.data[(int) ((((long) segment5.pos) + pos) - j)];
                }
                Segment segment6 = segment.next;
                Intrinsics.checkNotNull(segment6);
                segment = segment6;
                j = j2;
            }
        }
    }

    public int hashCode() {
        Segment segment = this.head;
        if (segment == null) {
            return 0;
        }
        int i = 1;
        do {
            int i2 = segment.limit;
            for (int i3 = segment.pos; i3 < i2; i3++) {
                i = (i * 31) + segment.data[i3];
            }
            Segment segment2 = segment.next;
            Intrinsics.checkNotNull(segment2);
            segment = segment2;
        } while (segment != this.head);
        return i;
    }

    public final ByteString hmacSha1(ByteString key) {
        Intrinsics.checkNotNullParameter(key, "key");
        return hmac("HmacSHA1", key);
    }

    public final ByteString hmacSha256(ByteString key) {
        Intrinsics.checkNotNullParameter(key, "key");
        return hmac("HmacSHA256", key);
    }

    public final ByteString hmacSha512(ByteString key) {
        Intrinsics.checkNotNullParameter(key, "key");
        return hmac("HmacSHA512", key);
    }

    public long indexOf(byte b) {
        return indexOf(b, 0, Long.MAX_VALUE);
    }

    public long indexOf(byte b, long fromIndex) {
        return indexOf(b, fromIndex, Long.MAX_VALUE);
    }

    public long indexOf(byte b, long fromIndex, long toIndex) {
        byte b2 = b;
        long j = fromIndex;
        long j2 = toIndex;
        if (0 <= j && j2 >= j) {
            if (j2 > size()) {
                j2 = size();
            }
            if (j == j2) {
                return -1;
            }
            long j3 = j;
            Buffer buffer = this;
            boolean z = false;
            Segment segment = buffer.head;
            if (segment == null) {
                Buffer buffer2 = buffer;
                long j4 = j3;
                Segment segment2 = null;
                return -1;
            } else if (buffer.size() - j3 < j3) {
                long size2 = buffer.size();
                while (size2 > j3) {
                    Segment segment3 = segment.prev;
                    Intrinsics.checkNotNull(segment3);
                    segment = segment3;
                    size2 -= (long) (segment.limit - segment.pos);
                }
                Segment segment4 = segment;
                long j5 = size2;
                boolean z2 = false;
                if (segment4 != null) {
                    long j6 = j5;
                    Segment segment5 = segment4;
                    while (j6 < j2) {
                        Buffer buffer3 = buffer;
                        byte[] bArr = segment5.data;
                        Segment segment6 = segment4;
                        boolean z3 = z2;
                        boolean z4 = z;
                        Segment segment7 = segment;
                        int min = (int) Math.min((long) segment5.limit, (((long) segment5.pos) + j2) - j6);
                        for (int i = (int) ((((long) segment5.pos) + j) - j6); i < min; i++) {
                            if (bArr[i] == b2) {
                                return ((long) (i - segment5.pos)) + j6;
                            }
                        }
                        j6 += (long) (segment5.limit - segment5.pos);
                        j = j6;
                        Segment segment8 = segment5.next;
                        Intrinsics.checkNotNull(segment8);
                        segment5 = segment8;
                        buffer = buffer3;
                        segment4 = segment6;
                        z2 = z3;
                        z = z4;
                        segment = segment7;
                    }
                    Buffer buffer4 = buffer;
                    Segment segment9 = segment4;
                    boolean z5 = z2;
                    boolean z6 = z;
                    Segment segment10 = segment;
                    return -1;
                }
                Buffer buffer5 = buffer;
                Segment segment11 = segment4;
                Segment segment12 = segment;
                return -1;
            } else {
                Buffer buffer6 = buffer;
                long j7 = 0;
                while (true) {
                    long j8 = ((long) (segment.limit - segment.pos)) + j7;
                    if (j8 > j3) {
                        break;
                    }
                    long j9 = j7;
                    long j10 = j3;
                    Segment segment13 = segment.next;
                    Intrinsics.checkNotNull(segment13);
                    segment = segment13;
                    j7 = j8;
                }
                Segment segment14 = segment;
                long j11 = j7;
                boolean z7 = false;
                if (segment14 != null) {
                    Segment segment15 = segment14;
                    long j12 = j11;
                    while (j12 < j2) {
                        Segment segment16 = segment14;
                        byte[] bArr2 = segment15.data;
                        long j13 = j7;
                        boolean z8 = z7;
                        long j14 = j3;
                        int min2 = (int) Math.min((long) segment15.limit, (((long) segment15.pos) + j2) - j12);
                        for (int i2 = (int) ((((long) segment15.pos) + j) - j12); i2 < min2; i2++) {
                            if (bArr2[i2] == b2) {
                                return ((long) (i2 - segment15.pos)) + j12;
                            }
                        }
                        j12 += (long) (segment15.limit - segment15.pos);
                        j = j12;
                        Segment segment17 = segment15.next;
                        Intrinsics.checkNotNull(segment17);
                        segment15 = segment17;
                        segment14 = segment16;
                        z7 = z8;
                        j7 = j13;
                        j3 = j14;
                    }
                    Segment segment18 = segment14;
                    long j15 = j7;
                    boolean z9 = z7;
                    long j16 = j3;
                    return -1;
                }
                Segment segment19 = segment14;
                long j17 = j7;
                long j18 = j3;
                return -1;
            }
        } else {
            throw new IllegalArgumentException(("size=" + size() + " fromIndex=" + j + " toIndex=" + j2).toString());
        }
    }

    public long indexOf(ByteString bytes) throws IOException {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return indexOf(bytes, 0);
    }

    public long indexOf(ByteString bytes, long fromIndex) throws IOException {
        byte b;
        byte[] bArr;
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        Buffer buffer = this;
        long j = fromIndex;
        if (bytes.size() > 0) {
            if (j >= 0) {
                long j2 = j;
                Buffer buffer2 = buffer;
                boolean z = false;
                Segment segment = buffer2.head;
                if (segment == null) {
                    Buffer buffer3 = buffer;
                    Buffer buffer4 = buffer2;
                    long j3 = j2;
                    Segment segment2 = null;
                    return -1;
                } else if (buffer2.size() - j2 < j2) {
                    long size2 = buffer2.size();
                    while (size2 > j2) {
                        Segment segment3 = segment.prev;
                        Intrinsics.checkNotNull(segment3);
                        segment = segment3;
                        size2 -= (long) (segment.limit - segment.pos);
                    }
                    Segment segment4 = segment;
                    long j4 = size2;
                    if (segment4 != null) {
                        long j5 = j4;
                        byte[] internalArray$okio = bytes.internalArray$okio();
                        byte b2 = internalArray$okio[0];
                        int size3 = bytes.size();
                        long size4 = (buffer.size() - ((long) size3)) + 1;
                        Segment segment5 = segment4;
                        while (j5 < size4) {
                            byte[] bArr2 = segment5.data;
                            Buffer buffer5 = buffer2;
                            boolean z2 = z;
                            Segment segment6 = segment;
                            Segment segment7 = segment4;
                            long j6 = j4;
                            int min = (int) Math.min((long) segment5.limit, (((long) segment5.pos) + size4) - j5);
                            for (int i = (int) ((((long) segment5.pos) + j) - j5); i < min; i++) {
                                if (bArr2[i] == b2 && BufferKt.rangeEquals(segment5, i + 1, internalArray$okio, 1, size3)) {
                                    return ((long) (i - segment5.pos)) + j5;
                                }
                            }
                            j5 += (long) (segment5.limit - segment5.pos);
                            j = j5;
                            Segment segment8 = segment5.next;
                            Intrinsics.checkNotNull(segment8);
                            segment5 = segment8;
                            buffer2 = buffer5;
                            segment = segment6;
                            z = z2;
                            segment4 = segment7;
                            j4 = j6;
                        }
                        Buffer buffer6 = buffer2;
                        boolean z3 = z;
                        Segment segment9 = segment;
                        Segment segment10 = segment4;
                        long j7 = j4;
                        return -1;
                    }
                    Buffer buffer7 = buffer2;
                    Segment segment11 = segment;
                    Segment segment12 = segment4;
                    long j8 = j4;
                    return -1;
                } else {
                    Buffer buffer8 = buffer2;
                    long j9 = 0;
                    while (true) {
                        long j10 = ((long) (segment.limit - segment.pos)) + j9;
                        if (j10 > j2) {
                            break;
                        }
                        long j11 = j9;
                        Segment segment13 = segment.next;
                        Intrinsics.checkNotNull(segment13);
                        segment = segment13;
                        j9 = j10;
                        buffer = buffer;
                        j2 = j2;
                    }
                    Segment segment14 = segment;
                    long j12 = j9;
                    if (segment14 != null) {
                        Segment segment15 = segment14;
                        long j13 = j12;
                        long j14 = j9;
                        byte[] internalArray$okio2 = bytes.internalArray$okio();
                        byte b3 = internalArray$okio2[0];
                        int size5 = bytes.size();
                        long j15 = j2;
                        long size6 = (buffer.size() - ((long) size5)) + 1;
                        while (j13 < size6) {
                            byte[] bArr3 = segment15.data;
                            Buffer buffer9 = buffer;
                            long j16 = j12;
                            byte[] bArr4 = internalArray$okio2;
                            int min2 = (int) Math.min((long) segment15.limit, (((long) segment15.pos) + size6) - j13);
                            int i2 = (int) ((((long) segment15.pos) + j) - j13);
                            while (i2 < min2) {
                                if (bArr3[i2] == b3) {
                                    bArr = bArr4;
                                    if (BufferKt.rangeEquals(segment15, i2 + 1, bArr, 1, size5)) {
                                        byte b4 = b3;
                                        long j17 = j;
                                        return ((long) (i2 - segment15.pos)) + j13;
                                    }
                                    b = b3;
                                } else {
                                    b = b3;
                                    bArr = bArr4;
                                }
                                i2++;
                                b3 = b;
                                j = j;
                                bArr4 = bArr;
                            }
                            byte[] bArr5 = bArr4;
                            long j18 = j;
                            j13 += (long) (segment15.limit - segment15.pos);
                            j = j13;
                            Segment segment16 = segment15.next;
                            Intrinsics.checkNotNull(segment16);
                            segment15 = segment16;
                            internalArray$okio2 = bArr5;
                            buffer = buffer9;
                            b3 = b3;
                            j12 = j16;
                        }
                        Buffer buffer10 = buffer;
                        byte b5 = b3;
                        long j19 = j;
                        long j20 = j12;
                        byte[] bArr6 = internalArray$okio2;
                        return -1;
                    }
                    Buffer buffer11 = buffer;
                    long j21 = j9;
                    long j22 = j2;
                    long j23 = j12;
                    return -1;
                }
            } else {
                Buffer buffer12 = buffer;
                throw new IllegalArgumentException(("fromIndex < 0: " + j).toString());
            }
        } else {
            Buffer buffer13 = buffer;
            throw new IllegalArgumentException("bytes is empty".toString());
        }
    }

    public long indexOfElement(ByteString targetBytes) {
        Intrinsics.checkNotNullParameter(targetBytes, "targetBytes");
        return indexOfElement(targetBytes, 0);
    }

    public long indexOfElement(ByteString targetBytes, long fromIndex) {
        ByteString byteString = targetBytes;
        Intrinsics.checkNotNullParameter(byteString, "targetBytes");
        Buffer buffer = this;
        boolean z = false;
        long j = fromIndex;
        if (j >= 0) {
            long j2 = j;
            Buffer buffer2 = buffer;
            boolean z2 = false;
            Segment segment = buffer2.head;
            if (segment == null) {
                Buffer buffer3 = buffer;
                Buffer buffer4 = buffer2;
                Segment segment2 = null;
                return -1;
            } else if (buffer2.size() - j2 < j2) {
                long size2 = buffer2.size();
                while (size2 > j2) {
                    Segment segment3 = segment.prev;
                    Intrinsics.checkNotNull(segment3);
                    segment = segment3;
                    size2 -= (long) (segment.limit - segment.pos);
                }
                Segment segment4 = segment;
                long j3 = size2;
                if (segment4 != null) {
                    Segment segment5 = segment4;
                    long j4 = j3;
                    if (targetBytes.size() == 2) {
                        byte b = byteString.getByte(0);
                        byte b2 = byteString.getByte(1);
                        Segment segment6 = segment5;
                        while (j4 < buffer.size()) {
                            boolean z3 = z;
                            byte[] bArr = segment6.data;
                            Buffer buffer5 = buffer2;
                            boolean z4 = z2;
                            Segment segment7 = segment;
                            int i = segment6.limit;
                            for (int i2 = (int) ((((long) segment6.pos) + j) - j4); i2 < i; i2++) {
                                byte b3 = bArr[i2];
                                if (b3 == b || b3 == b2) {
                                    byte[] bArr2 = bArr;
                                    long j5 = j;
                                    return ((long) (i2 - segment6.pos)) + j4;
                                }
                            }
                            byte[] bArr3 = bArr;
                            long j6 = j;
                            j4 += (long) (segment6.limit - segment6.pos);
                            j = j4;
                            Segment segment8 = segment6.next;
                            Intrinsics.checkNotNull(segment8);
                            segment6 = segment8;
                            segment = segment7;
                            z = z3;
                            buffer2 = buffer5;
                            z2 = z4;
                        }
                        boolean z5 = z;
                        long j7 = j;
                        Buffer buffer6 = buffer2;
                        boolean z6 = z2;
                        Segment segment9 = segment;
                    } else {
                        Buffer buffer7 = buffer2;
                        Segment segment10 = segment;
                        byte[] internalArray$okio = targetBytes.internalArray$okio();
                        Segment segment11 = segment5;
                        while (j4 < buffer.size()) {
                            byte[] bArr4 = segment11.data;
                            int i3 = (int) ((((long) segment11.pos) + j) - j4);
                            int i4 = segment11.limit;
                            while (i3 < i4) {
                                byte b4 = bArr4[i3];
                                long j8 = j;
                                for (byte b5 : internalArray$okio) {
                                    if (b4 == b5) {
                                        byte[] bArr5 = internalArray$okio;
                                        return ((long) (i3 - segment11.pos)) + j4;
                                    }
                                    byte[] bArr6 = internalArray$okio;
                                }
                                byte[] bArr7 = internalArray$okio;
                                i3++;
                                j = j8;
                            }
                            byte[] bArr8 = internalArray$okio;
                            long j9 = j;
                            j4 += (long) (segment11.limit - segment11.pos);
                            j = j4;
                            Segment segment12 = segment11.next;
                            Intrinsics.checkNotNull(segment12);
                            segment11 = segment12;
                            internalArray$okio = bArr8;
                        }
                        byte[] bArr9 = internalArray$okio;
                        long j10 = j;
                        Segment segment13 = segment11;
                    }
                    return -1;
                }
                Buffer buffer8 = buffer2;
                Segment segment14 = segment;
                return -1;
            } else {
                Buffer buffer9 = buffer2;
                long j11 = 0;
                while (true) {
                    long j12 = ((long) (segment.limit - segment.pos)) + j11;
                    if (j12 > j2) {
                        break;
                    }
                    Buffer buffer10 = buffer;
                    long j13 = j11;
                    Segment segment15 = segment.next;
                    Intrinsics.checkNotNull(segment15);
                    segment = segment15;
                    j11 = j12;
                    byteString = targetBytes;
                    buffer = buffer10;
                }
                Segment segment16 = segment;
                long j14 = j11;
                if (segment16 != null) {
                    Segment segment17 = segment16;
                    long j15 = j14;
                    if (targetBytes.size() == 2) {
                        byte b6 = byteString.getByte(0);
                        byte b7 = byteString.getByte(1);
                        while (j15 < buffer.size()) {
                            byte[] bArr10 = segment17.data;
                            Segment segment18 = segment16;
                            long j16 = j11;
                            int i5 = (int) ((((long) segment17.pos) + j) - j15);
                            int i6 = segment17.limit;
                            while (i5 < i6) {
                                byte b8 = bArr10[i5];
                                if (b8 == b6 || b8 == b7) {
                                    byte[] bArr11 = bArr10;
                                    long j17 = j;
                                    int i7 = i5;
                                    return ((long) (i5 - segment17.pos)) + j15;
                                }
                                i5++;
                            }
                            byte[] bArr12 = bArr10;
                            long j18 = j;
                            int i8 = i5;
                            j15 += (long) (segment17.limit - segment17.pos);
                            Segment segment19 = segment17.next;
                            Intrinsics.checkNotNull(segment19);
                            segment17 = segment19;
                            ByteString byteString2 = targetBytes;
                            j = j15;
                            segment16 = segment18;
                            j11 = j16;
                        }
                        Segment segment20 = segment16;
                        long j19 = j;
                        long j20 = j11;
                        Buffer buffer11 = buffer;
                    } else {
                        Segment segment21 = segment16;
                        long j21 = j11;
                        byte[] internalArray$okio2 = targetBytes.internalArray$okio();
                        while (j15 < buffer.size()) {
                            byte[] bArr13 = segment17.data;
                            int i9 = (int) ((((long) segment17.pos) + j) - j15);
                            int i10 = segment17.limit;
                            while (i9 < i10) {
                                byte b9 = bArr13[i9];
                                int length = internalArray$okio2.length;
                                Buffer buffer12 = buffer;
                                int i11 = 0;
                                while (i11 < length) {
                                    byte[] bArr14 = bArr13;
                                    if (b9 == internalArray$okio2[i11]) {
                                        byte[] bArr15 = internalArray$okio2;
                                        return ((long) (i9 - segment17.pos)) + j15;
                                    }
                                    byte[] bArr16 = internalArray$okio2;
                                    i11++;
                                    bArr13 = bArr14;
                                }
                                byte[] bArr17 = internalArray$okio2;
                                byte[] bArr18 = bArr13;
                                i9++;
                                buffer = buffer12;
                            }
                            byte[] bArr19 = internalArray$okio2;
                            byte[] bArr20 = bArr13;
                            j15 += (long) (segment17.limit - segment17.pos);
                            j = j15;
                            Segment segment22 = segment17.next;
                            Intrinsics.checkNotNull(segment22);
                            segment17 = segment22;
                            buffer = buffer;
                            internalArray$okio2 = bArr19;
                        }
                        byte[] bArr21 = internalArray$okio2;
                        Buffer buffer13 = buffer;
                    }
                    return -1;
                }
                Buffer buffer14 = buffer;
                Segment segment23 = segment16;
                long j22 = j11;
                return -1;
            }
        } else {
            Buffer buffer15 = buffer;
            throw new IllegalArgumentException(("fromIndex < 0: " + j).toString());
        }
    }

    public InputStream inputStream() {
        return new Buffer$inputStream$1(this);
    }

    public boolean isOpen() {
        return true;
    }

    public final ByteString md5() {
        return digest("MD5");
    }

    public OutputStream outputStream() {
        return new Buffer$outputStream$1(this);
    }

    public BufferedSource peek() {
        return Okio.buffer((Source) new PeekSource(this));
    }

    public boolean rangeEquals(long offset, ByteString bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return rangeEquals(offset, bytes, 0, bytes.size());
    }

    public boolean rangeEquals(long offset, ByteString bytes, int bytesOffset, int byteCount) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        if (offset < 0 || bytesOffset < 0 || byteCount < 0 || size() - offset < ((long) byteCount) || bytes.size() - bytesOffset < byteCount) {
            return false;
        }
        for (int i = 0; i < byteCount; i++) {
            if (getByte(((long) i) + offset) != bytes.getByte(bytesOffset + i)) {
                return false;
            }
        }
        return true;
    }

    public int read(ByteBuffer sink) throws IOException {
        Intrinsics.checkNotNullParameter(sink, "sink");
        Segment segment = this.head;
        if (segment == null) {
            return -1;
        }
        int min = Math.min(sink.remaining(), segment.limit - segment.pos);
        sink.put(segment.data, segment.pos, min);
        segment.pos += min;
        this.size -= (long) min;
        if (segment.pos == segment.limit) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
        }
        return min;
    }

    public int read(byte[] sink) {
        Intrinsics.checkNotNullParameter(sink, "sink");
        return read(sink, 0, sink.length);
    }

    public int read(byte[] sink, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter(sink, "sink");
        Util.checkOffsetAndCount((long) sink.length, (long) offset, (long) byteCount);
        Segment segment = this.head;
        if (segment == null) {
            return -1;
        }
        int min = Math.min(byteCount, segment.limit - segment.pos);
        ArraysKt.copyInto(segment.data, sink, offset, segment.pos, segment.pos + min);
        segment.pos += min;
        setSize$okio(size() - ((long) min));
        if (segment.pos != segment.limit) {
            return min;
        }
        this.head = segment.pop();
        SegmentPool.recycle(segment);
        return min;
    }

    public long read(Buffer sink, long byteCount) {
        Intrinsics.checkNotNullParameter(sink, "sink");
        long j = byteCount;
        if (!(j >= 0)) {
            throw new IllegalArgumentException(("byteCount < 0: " + j).toString());
        } else if (size() == 0) {
            return -1;
        } else {
            if (j > size()) {
                j = size();
            }
            sink.write(this, j);
            return j;
        }
    }

    public long readAll(Sink sink) throws IOException {
        Intrinsics.checkNotNullParameter(sink, "sink");
        long size2 = size();
        if (size2 > 0) {
            sink.write(this, size2);
        }
        return size2;
    }

    public final UnsafeCursor readAndWriteUnsafe() {
        return readAndWriteUnsafe$default(this, (UnsafeCursor) null, 1, (Object) null);
    }

    public final UnsafeCursor readAndWriteUnsafe(UnsafeCursor unsafeCursor) {
        Intrinsics.checkNotNullParameter(unsafeCursor, "unsafeCursor");
        if (unsafeCursor.buffer == null) {
            unsafeCursor.buffer = this;
            unsafeCursor.readWrite = true;
            return unsafeCursor;
        }
        throw new IllegalStateException("already attached to a buffer".toString());
    }

    public byte readByte() throws EOFException {
        if (size() != 0) {
            Segment segment = this.head;
            Intrinsics.checkNotNull(segment);
            int i = segment.pos;
            int i2 = segment.limit;
            int i3 = i + 1;
            byte b = segment.data[i];
            setSize$okio(size() - 1);
            if (i3 == i2) {
                this.head = segment.pop();
                SegmentPool.recycle(segment);
            } else {
                segment.pos = i3;
            }
            return b;
        }
        throw new EOFException();
    }

    public byte[] readByteArray() {
        return readByteArray(size());
    }

    public byte[] readByteArray(long byteCount) throws EOFException {
        if (!(byteCount >= 0 && byteCount <= ((long) Integer.MAX_VALUE))) {
            throw new IllegalArgumentException(("byteCount: " + byteCount).toString());
        } else if (size() >= byteCount) {
            byte[] bArr = new byte[((int) byteCount)];
            readFully(bArr);
            return bArr;
        } else {
            throw new EOFException();
        }
    }

    public ByteString readByteString() {
        return readByteString(size());
    }

    public ByteString readByteString(long byteCount) throws EOFException {
        if (!(byteCount >= 0 && byteCount <= ((long) Integer.MAX_VALUE))) {
            throw new IllegalArgumentException(("byteCount: " + byteCount).toString());
        } else if (size() < byteCount) {
            throw new EOFException();
        } else if (byteCount < ((long) 4096)) {
            return new ByteString(readByteArray(byteCount));
        } else {
            ByteString snapshot = snapshot((int) byteCount);
            ByteString byteString = snapshot;
            skip(byteCount);
            return snapshot;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00d8, code lost:
        if (r11 != r12) goto L_0x00e5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00da, code lost:
        r1 = r15;
        r1.head = r9.pop();
        okio.SegmentPool.recycle(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00e5, code lost:
        r1 = r15;
        r9.pos = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00e9, code lost:
        if (r6 != false) goto L_0x00f5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00ed, code lost:
        if (r1.head != null) goto L_0x00f0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00f5, code lost:
        r1.setSize$okio(r1.size() - ((long) r4));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00fe, code lost:
        if (r5 == false) goto L_0x0101;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:?, code lost:
        return -r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:?, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long readDecimalLong() throws java.io.EOFException {
        /*
            r19 = this;
            r0 = r19
            r1 = 0
            long r2 = r0.size()
            r4 = 0
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 == 0) goto L_0x0104
            r2 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = -7
        L_0x0014:
            okio.Segment r9 = r0.head
            kotlin.jvm.internal.Intrinsics.checkNotNull(r9)
            byte[] r10 = r9.data
            int r11 = r9.pos
            int r12 = r9.limit
        L_0x0020:
            if (r11 >= r12) goto L_0x00d1
            byte r13 = r10[r11]
            r14 = 48
            byte r14 = (byte) r14
            if (r13 < r14) goto L_0x008a
            r15 = 57
            byte r15 = (byte) r15
            if (r13 > r15) goto L_0x008a
            int r14 = r14 - r13
            r15 = -922337203685477580(0xf333333333333334, double:-8.390303882365713E246)
            int r17 = (r2 > r15 ? 1 : (r2 == r15 ? 0 : -1))
            if (r17 < 0) goto L_0x0052
            int r15 = (r2 > r15 ? 1 : (r2 == r15 ? 0 : -1))
            if (r15 != 0) goto L_0x0045
            r15 = r0
            r16 = r1
            long r0 = (long) r14
            int r0 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1))
            if (r0 >= 0) goto L_0x0048
            goto L_0x0055
        L_0x0045:
            r15 = r0
            r16 = r1
        L_0x0048:
            r0 = 10
            long r2 = r2 * r0
            long r0 = (long) r14
            long r2 = r2 + r0
            r17 = r6
            r18 = r10
            goto L_0x009c
        L_0x0052:
            r15 = r0
            r16 = r1
        L_0x0055:
            okio.Buffer r0 = new okio.Buffer
            r0.<init>()
            okio.Buffer r0 = r0.writeDecimalLong((long) r2)
            okio.Buffer r0 = r0.writeByte((int) r13)
            if (r5 != 0) goto L_0x0067
            r0.readByte()
        L_0x0067:
            java.lang.NumberFormatException r1 = new java.lang.NumberFormatException
            r17 = r6
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            r18 = r10
            java.lang.String r10 = "Number too large: "
            java.lang.StringBuilder r6 = r6.append(r10)
            java.lang.String r10 = r0.readUtf8()
            java.lang.StringBuilder r6 = r6.append(r10)
            java.lang.String r6 = r6.toString()
            r1.<init>(r6)
            java.lang.Throwable r1 = (java.lang.Throwable) r1
            throw r1
        L_0x008a:
            r15 = r0
            r16 = r1
            r17 = r6
            r18 = r10
            r0 = 45
            byte r0 = (byte) r0
            if (r13 != r0) goto L_0x00aa
            if (r4 != 0) goto L_0x00aa
            r5 = 1
            r0 = 1
            long r7 = r7 - r0
        L_0x009c:
            int r11 = r11 + 1
            int r4 = r4 + 1
            r0 = r15
            r1 = r16
            r6 = r17
            r10 = r18
            goto L_0x0020
        L_0x00aa:
            if (r4 == 0) goto L_0x00af
            r0 = 1
            r6 = r0
            goto L_0x00d8
        L_0x00af:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r6 = "Expected leading [0-9] or '-' character but was 0x"
            java.lang.StringBuilder r1 = r1.append(r6)
            java.lang.String r6 = okio.Util.toHexString((byte) r13)
            mt.Log1F380D.a((java.lang.Object) r6)
            java.lang.StringBuilder r1 = r1.append(r6)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            java.lang.Throwable r0 = (java.lang.Throwable) r0
            throw r0
        L_0x00d1:
            r15 = r0
            r16 = r1
            r17 = r6
            r18 = r10
        L_0x00d8:
            if (r11 != r12) goto L_0x00e5
            okio.Segment r0 = r9.pop()
            r1 = r15
            r1.head = r0
            okio.SegmentPool.recycle(r9)
            goto L_0x00e8
        L_0x00e5:
            r1 = r15
            r9.pos = r11
        L_0x00e8:
            if (r6 != 0) goto L_0x00f5
            okio.Segment r0 = r1.head
            if (r0 != 0) goto L_0x00f0
            goto L_0x00f5
        L_0x00f0:
            r0 = r1
            r1 = r16
            goto L_0x0014
        L_0x00f5:
            long r9 = r1.size()
            long r11 = (long) r4
            long r9 = r9 - r11
            r1.setSize$okio(r9)
            if (r5 == 0) goto L_0x0101
            goto L_0x0103
        L_0x0101:
            long r9 = -r2
            r2 = r9
        L_0x0103:
            return r2
        L_0x0104:
            r16 = r1
            r1 = r0
            java.io.EOFException r0 = new java.io.EOFException
            r0.<init>()
            java.lang.Throwable r0 = (java.lang.Throwable) r0
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Buffer.readDecimalLong():long");
    }

    public final Buffer readFrom(InputStream input) throws IOException {
        Intrinsics.checkNotNullParameter(input, "input");
        readFrom(input, Long.MAX_VALUE, true);
        return this;
    }

    public final Buffer readFrom(InputStream input, long byteCount) throws IOException {
        Intrinsics.checkNotNullParameter(input, "input");
        if (byteCount >= 0) {
            readFrom(input, byteCount, false);
            return this;
        }
        throw new IllegalArgumentException(("byteCount < 0: " + byteCount).toString());
    }

    public void readFully(Buffer sink, long byteCount) throws EOFException {
        Intrinsics.checkNotNullParameter(sink, "sink");
        if (size() >= byteCount) {
            sink.write(this, byteCount);
        } else {
            sink.write(this, size());
            throw new EOFException();
        }
    }

    public void readFully(byte[] sink) throws EOFException {
        Intrinsics.checkNotNullParameter(sink, "sink");
        int i = 0;
        while (i < sink.length) {
            int read = read(sink, i, sink.length - i);
            if (read != -1) {
                i += read;
            } else {
                throw new EOFException();
            }
        }
    }

    public int readInt() throws EOFException {
        if (size() >= 4) {
            Segment segment = this.head;
            Intrinsics.checkNotNull(segment);
            int i = segment.pos;
            int i2 = segment.limit;
            if (((long) (i2 - i)) < 4) {
                return ((readByte() & UByte.MAX_VALUE) << 24) | ((readByte() & UByte.MAX_VALUE) << 16) | ((readByte() & UByte.MAX_VALUE) << 8) | (readByte() & UByte.MAX_VALUE);
            }
            byte[] bArr = segment.data;
            int i3 = i + 1;
            int i4 = i3 + 1;
            byte b = ((bArr[i] & UByte.MAX_VALUE) << 24) | ((bArr[i3] & UByte.MAX_VALUE) << 16);
            int i5 = i4 + 1;
            byte b2 = b | ((bArr[i4] & UByte.MAX_VALUE) << 8);
            int i6 = i5 + 1;
            byte b3 = b2 | (bArr[i5] & UByte.MAX_VALUE);
            setSize$okio(size() - 4);
            if (i6 == i2) {
                this.head = segment.pop();
                SegmentPool.recycle(segment);
            } else {
                segment.pos = i6;
            }
            return b3;
        }
        throw new EOFException();
    }

    public int readIntLe() throws EOFException {
        return Util.reverseBytes(readInt());
    }

    public long readLong() throws EOFException {
        if (size() >= 8) {
            Segment segment = this.head;
            Intrinsics.checkNotNull(segment);
            int i = segment.pos;
            int i2 = segment.limit;
            if (((long) (i2 - i)) < 8) {
                return ((((long) readInt()) & 4294967295L) << 32) | (((long) readInt()) & 4294967295L);
            }
            byte[] bArr = segment.data;
            int i3 = i + 1;
            int i4 = i3 + 1;
            long j = (((long) bArr[i3]) & 255) << 48;
            int i5 = i4 + 1;
            int i6 = i5 + 1;
            int i7 = i6 + 1;
            int i8 = i7 + 1;
            long j2 = j | ((255 & ((long) bArr[i])) << 56) | ((255 & ((long) bArr[i4])) << 40) | ((((long) bArr[i5]) & 255) << 32) | ((255 & ((long) bArr[i6])) << 24) | ((((long) bArr[i7]) & 255) << 16);
            int i9 = i8 + 1;
            int i10 = i9 + 1;
            long j3 = j2 | ((255 & ((long) bArr[i8])) << 8) | (((long) bArr[i9]) & 255);
            setSize$okio(size() - 8);
            if (i10 == i2) {
                this.head = segment.pop();
                SegmentPool.recycle(segment);
            } else {
                segment.pos = i10;
            }
            return j3;
        }
        throw new EOFException();
    }

    public long readLongLe() throws EOFException {
        return Util.reverseBytes(readLong());
    }

    public short readShort() throws EOFException {
        if (size() >= 2) {
            Segment segment = this.head;
            Intrinsics.checkNotNull(segment);
            int i = segment.pos;
            int i2 = segment.limit;
            if (i2 - i < 2) {
                return (short) (((readByte() & UByte.MAX_VALUE) << 8) | (readByte() & UByte.MAX_VALUE));
            }
            byte[] bArr = segment.data;
            int i3 = i + 1;
            int i4 = i3 + 1;
            byte b = ((bArr[i] & UByte.MAX_VALUE) << 8) | (bArr[i3] & UByte.MAX_VALUE);
            setSize$okio(size() - 2);
            if (i4 == i2) {
                this.head = segment.pop();
                SegmentPool.recycle(segment);
            } else {
                segment.pos = i4;
            }
            return (short) b;
        }
        throw new EOFException();
    }

    public short readShortLe() throws EOFException {
        return Util.reverseBytes(readShort());
    }

    public String readString(Charset charset) {
        Intrinsics.checkNotNullParameter(charset, "charset");
        return readString(this.size, charset);
    }

    public final UnsafeCursor readUnsafe() {
        return readUnsafe$default(this, (UnsafeCursor) null, 1, (Object) null);
    }

    public final UnsafeCursor readUnsafe(UnsafeCursor unsafeCursor) {
        Intrinsics.checkNotNullParameter(unsafeCursor, "unsafeCursor");
        if (unsafeCursor.buffer == null) {
            unsafeCursor.buffer = this;
            unsafeCursor.readWrite = false;
            return unsafeCursor;
        }
        throw new IllegalStateException("already attached to a buffer".toString());
    }

    public String readUtf8() {
        return readString(this.size, Charsets.UTF_8);
    }

    public String readUtf8(long byteCount) throws EOFException {
        return readString(byteCount, Charsets.UTF_8);
    }

    public String readUtf8LineStrict() throws EOFException {
        return readUtf8LineStrict(Long.MAX_VALUE);
    }

    public boolean request(long byteCount) {
        return this.size >= byteCount;
    }

    public void require(long byteCount) throws EOFException {
        if (this.size < byteCount) {
            throw new EOFException();
        }
    }

    public int select(Options options) {
        Intrinsics.checkNotNullParameter(options, "options");
        int selectPrefix$default = BufferKt.selectPrefix$default(this, options, false, 2, (Object) null);
        if (selectPrefix$default == -1) {
            return -1;
        }
        skip((long) options.getByteStrings$okio()[selectPrefix$default].size());
        return selectPrefix$default;
    }

    public final void setSize$okio(long j) {
        this.size = j;
    }

    public final ByteString sha1() {
        return digest("SHA-1");
    }

    public final ByteString sha256() {
        return digest("SHA-256");
    }

    public final ByteString sha512() {
        return digest("SHA-512");
    }

    public final long size() {
        return this.size;
    }

    public void skip(long byteCount) throws EOFException {
        long j = byteCount;
        while (j > 0) {
            Segment segment = this.head;
            if (segment != null) {
                int min = (int) Math.min(j, (long) (segment.limit - segment.pos));
                setSize$okio(size() - ((long) min));
                j -= (long) min;
                segment.pos += min;
                if (segment.pos == segment.limit) {
                    this.head = segment.pop();
                    SegmentPool.recycle(segment);
                }
            } else {
                throw new EOFException();
            }
        }
    }

    public final ByteString snapshot() {
        if (size() <= ((long) Integer.MAX_VALUE)) {
            return snapshot((int) size());
        }
        throw new IllegalStateException(("size > Int.MAX_VALUE: " + size()).toString());
    }

    public final ByteString snapshot(int byteCount) {
        if (byteCount == 0) {
            return ByteString.EMPTY;
        }
        Util.checkOffsetAndCount(size(), 0, (long) byteCount);
        int i = 0;
        int i2 = 0;
        Segment segment = this.head;
        while (i < byteCount) {
            Intrinsics.checkNotNull(segment);
            if (segment.limit != segment.pos) {
                i += segment.limit - segment.pos;
                i2++;
                segment = segment.next;
            } else {
                throw new AssertionError("s.limit == s.pos");
            }
        }
        byte[][] bArr = new byte[i2][];
        int[] iArr = new int[(i2 * 2)];
        int i3 = 0;
        int i4 = 0;
        Segment segment2 = this.head;
        while (i3 < byteCount) {
            Intrinsics.checkNotNull(segment2);
            bArr[i4] = segment2.data;
            i3 += segment2.limit - segment2.pos;
            iArr[i4] = Math.min(i3, byteCount);
            iArr[((Object[]) bArr).length + i4] = segment2.pos;
            segment2.shared = true;
            i4++;
            segment2 = segment2.next;
        }
        return new SegmentedByteString(bArr, iArr);
    }

    public Timeout timeout() {
        return Timeout.NONE;
    }

    public String toString() {
        return snapshot().toString();
    }

    public final Segment writableSegment$okio(int minimumCapacity) {
        boolean z = true;
        if (minimumCapacity < 1 || minimumCapacity > 8192) {
            z = false;
        }
        if (z) {
            Segment segment = this.head;
            if (segment == null) {
                Segment take = SegmentPool.take();
                this.head = take;
                take.prev = take;
                take.next = take;
                return take;
            }
            Intrinsics.checkNotNull(segment);
            Segment segment2 = segment.prev;
            Intrinsics.checkNotNull(segment2);
            return (segment2.limit + minimumCapacity > 8192 || !segment2.owner) ? segment2.push(SegmentPool.take()) : segment2;
        }
        throw new IllegalArgumentException("unexpected capacity".toString());
    }

    public int write(ByteBuffer source) throws IOException {
        Intrinsics.checkNotNullParameter(source, "source");
        int remaining = source.remaining();
        int i = remaining;
        while (i > 0) {
            Segment writableSegment$okio = writableSegment$okio(1);
            int min = Math.min(i, 8192 - writableSegment$okio.limit);
            source.get(writableSegment$okio.data, writableSegment$okio.limit, min);
            i -= min;
            writableSegment$okio.limit += min;
        }
        this.size += (long) remaining;
        return remaining;
    }

    public Buffer write(ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "byteString");
        byteString.write$okio(this, 0, byteString.size());
        return this;
    }

    public Buffer write(ByteString byteString, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter(byteString, "byteString");
        byteString.write$okio(this, offset, byteCount);
        return this;
    }

    public Buffer write(Source source, long byteCount) throws IOException {
        Intrinsics.checkNotNullParameter(source, "source");
        long j = byteCount;
        while (j > 0) {
            long read = source.read(this, j);
            if (read != -1) {
                j -= read;
            } else {
                throw new EOFException();
            }
        }
        return this;
    }

    public Buffer write(byte[] source) {
        Intrinsics.checkNotNullParameter(source, "source");
        return write(source, 0, source.length);
    }

    public Buffer write(byte[] source, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter(source, "source");
        int i = offset;
        Util.checkOffsetAndCount((long) source.length, (long) i, (long) byteCount);
        int i2 = i + byteCount;
        while (i < i2) {
            Segment writableSegment$okio = writableSegment$okio(1);
            int min = Math.min(i2 - i, 8192 - writableSegment$okio.limit);
            ArraysKt.copyInto(source, writableSegment$okio.data, writableSegment$okio.limit, i, i + min);
            i += min;
            writableSegment$okio.limit += min;
        }
        setSize$okio(size() + ((long) byteCount));
        return this;
    }

    public void write(Buffer source, long byteCount) {
        Segment segment;
        Intrinsics.checkNotNullParameter(source, "source");
        long j = byteCount;
        if (source != this) {
            Util.checkOffsetAndCount(source.size(), 0, j);
            while (j > 0) {
                Segment segment2 = source.head;
                Intrinsics.checkNotNull(segment2);
                int i = segment2.limit;
                Segment segment3 = source.head;
                Intrinsics.checkNotNull(segment3);
                if (j < ((long) (i - segment3.pos))) {
                    Segment segment4 = this.head;
                    if (segment4 != null) {
                        Intrinsics.checkNotNull(segment4);
                        segment = segment4.prev;
                    } else {
                        segment = null;
                    }
                    if (segment != null && segment.owner) {
                        if ((((long) segment.limit) + j) - ((long) (segment.shared ? 0 : segment.pos)) <= ((long) 8192)) {
                            Segment segment5 = source.head;
                            Intrinsics.checkNotNull(segment5);
                            segment5.writeTo(segment, (int) j);
                            source.setSize$okio(source.size() - j);
                            setSize$okio(size() + j);
                            return;
                        }
                    }
                    Segment segment6 = source.head;
                    Intrinsics.checkNotNull(segment6);
                    source.head = segment6.split((int) j);
                }
                Segment segment7 = source.head;
                Intrinsics.checkNotNull(segment7);
                long j2 = (long) (segment7.limit - segment7.pos);
                source.head = segment7.pop();
                Segment segment8 = this.head;
                if (segment8 == null) {
                    this.head = segment7;
                    segment7.prev = segment7;
                    segment7.next = segment7.prev;
                } else {
                    Intrinsics.checkNotNull(segment8);
                    Segment segment9 = segment8.prev;
                    Intrinsics.checkNotNull(segment9);
                    segment9.push(segment7).compact();
                }
                source.setSize$okio(source.size() - j2);
                setSize$okio(size() + j2);
                j -= j2;
            }
            return;
        }
        throw new IllegalArgumentException("source == this".toString());
    }

    public long writeAll(Source source) throws IOException {
        Intrinsics.checkNotNullParameter(source, "source");
        long j = 0;
        while (true) {
            long read = source.read(this, (long) 8192);
            if (read == -1) {
                return j;
            }
            j += read;
        }
    }

    public Buffer writeByte(int b) {
        Segment writableSegment$okio = writableSegment$okio(1);
        byte[] bArr = writableSegment$okio.data;
        int i = writableSegment$okio.limit;
        writableSegment$okio.limit = i + 1;
        bArr[i] = (byte) b;
        setSize$okio(size() + 1);
        return this;
    }

    public Buffer writeDecimalLong(long v) {
        long j = v;
        if (j == 0) {
            return writeByte(48);
        }
        boolean z = false;
        if (j < 0) {
            j = -j;
            if (j < 0) {
                return writeUtf8("-9223372036854775808");
            }
            z = true;
        }
        int i = j < 100000000 ? j < WorkRequest.MIN_BACKOFF_MILLIS ? j < 100 ? j < 10 ? 1 : 2 : j < 1000 ? 3 : 4 : j < 1000000 ? j < 100000 ? 5 : 6 : j < 10000000 ? 7 : 8 : j < 1000000000000L ? j < RealConnection.IDLE_CONNECTION_HEALTHY_NS ? j < 1000000000 ? 9 : 10 : j < 100000000000L ? 11 : 12 : j < 1000000000000000L ? j < 10000000000000L ? 13 : j < 100000000000000L ? 14 : 15 : j < 100000000000000000L ? j < 10000000000000000L ? 16 : 17 : j < 1000000000000000000L ? 18 : 19;
        if (z) {
            i++;
        }
        Segment writableSegment$okio = writableSegment$okio(i);
        byte[] bArr = writableSegment$okio.data;
        int i2 = writableSegment$okio.limit + i;
        while (j != 0) {
            long j2 = (long) 10;
            i2--;
            bArr[i2] = BufferKt.getHEX_DIGIT_BYTES()[(int) (j % j2)];
            j /= j2;
        }
        if (z) {
            bArr[i2 - 1] = (byte) 45;
        }
        writableSegment$okio.limit += i;
        setSize$okio(size() + ((long) i));
        return this;
    }

    public Buffer writeHexadecimalUnsignedLong(long v) {
        long j = v;
        if (j == 0) {
            return writeByte(48);
        }
        long j2 = j;
        long j3 = j2 | (j2 >>> 1);
        long j4 = j3 | (j3 >>> 2);
        long j5 = j4 | (j4 >>> 4);
        long j6 = j5 | (j5 >>> 8);
        long j7 = j6 | (j6 >>> 16);
        long j8 = j7 | (j7 >>> 32);
        long j9 = j8 - ((j8 >>> 1) & 6148914691236517205L);
        long j10 = ((j9 >>> 2) & 3689348814741910323L) + (3689348814741910323L & j9);
        long j11 = ((j10 >>> 4) + j10) & 1085102592571150095L;
        long j12 = j11 + (j11 >>> 8);
        long j13 = j12 + (j12 >>> 16);
        int i = (int) ((((long) 3) + ((j13 & 63) + (63 & (j13 >>> 32)))) / ((long) 4));
        Segment writableSegment$okio = writableSegment$okio(i);
        byte[] bArr = writableSegment$okio.data;
        int i2 = writableSegment$okio.limit;
        for (int i3 = (writableSegment$okio.limit + i) - 1; i3 >= i2; i3--) {
            bArr[i3] = BufferKt.getHEX_DIGIT_BYTES()[(int) (15 & j)];
            j >>>= 4;
        }
        writableSegment$okio.limit += i;
        setSize$okio(size() + ((long) i));
        return this;
    }

    public Buffer writeInt(int i) {
        Segment writableSegment$okio = writableSegment$okio(4);
        byte[] bArr = writableSegment$okio.data;
        int i2 = writableSegment$okio.limit;
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((i >>> 24) & 255);
        int i4 = i3 + 1;
        bArr[i3] = (byte) ((i >>> 16) & 255);
        int i5 = i4 + 1;
        bArr[i4] = (byte) ((i >>> 8) & 255);
        bArr[i5] = (byte) (i & 255);
        writableSegment$okio.limit = i5 + 1;
        setSize$okio(size() + 4);
        return this;
    }

    public Buffer writeIntLe(int i) {
        return writeInt(Util.reverseBytes(i));
    }

    public Buffer writeLong(long v) {
        Segment writableSegment$okio = writableSegment$okio(8);
        byte[] bArr = writableSegment$okio.data;
        int i = writableSegment$okio.limit;
        int i2 = i + 1;
        bArr[i] = (byte) ((int) ((v >>> 56) & 255));
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((int) ((v >>> 48) & 255));
        int i4 = i3 + 1;
        bArr[i3] = (byte) ((int) ((v >>> 40) & 255));
        int i5 = i4 + 1;
        bArr[i4] = (byte) ((int) ((v >>> 32) & 255));
        int i6 = i5 + 1;
        bArr[i5] = (byte) ((int) ((v >>> 24) & 255));
        int i7 = i6 + 1;
        bArr[i6] = (byte) ((int) ((v >>> 16) & 255));
        int i8 = i7 + 1;
        bArr[i7] = (byte) ((int) ((v >>> 8) & 255));
        bArr[i8] = (byte) ((int) (v & 255));
        writableSegment$okio.limit = i8 + 1;
        setSize$okio(size() + 8);
        return this;
    }

    public Buffer writeLongLe(long v) {
        return writeLong(Util.reverseBytes(v));
    }

    public Buffer writeShort(int s) {
        Segment writableSegment$okio = writableSegment$okio(2);
        byte[] bArr = writableSegment$okio.data;
        int i = writableSegment$okio.limit;
        int i2 = i + 1;
        bArr[i] = (byte) ((s >>> 8) & 255);
        bArr[i2] = (byte) (s & 255);
        writableSegment$okio.limit = i2 + 1;
        setSize$okio(size() + 2);
        return this;
    }

    public Buffer writeShortLe(int s) {
        return writeShort((int) Util.reverseBytes((short) s));
    }

    public Buffer writeString(String string, int beginIndex, int endIndex, Charset charset) {
        Intrinsics.checkNotNullParameter(string, TypedValues.Custom.S_STRING);
        Intrinsics.checkNotNullParameter(charset, "charset");
        boolean z = true;
        if (beginIndex >= 0) {
            if (endIndex >= beginIndex) {
                if (endIndex > string.length()) {
                    z = false;
                }
                if (!z) {
                    throw new IllegalArgumentException(("endIndex > string.length: " + endIndex + " > " + string.length()).toString());
                } else if (Intrinsics.areEqual((Object) charset, (Object) Charsets.UTF_8)) {
                    return writeUtf8(string, beginIndex, endIndex);
                } else {
                    String substring = string.substring(beginIndex, endIndex);
                    Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    if (substring != null) {
                        byte[] bytes = substring.getBytes(charset);
                        Intrinsics.checkNotNullExpressionValue(bytes, "(this as java.lang.String).getBytes(charset)");
                        return write(bytes, 0, bytes.length);
                    }
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
            } else {
                throw new IllegalArgumentException(("endIndex < beginIndex: " + endIndex + " < " + beginIndex).toString());
            }
        } else {
            throw new IllegalArgumentException(("beginIndex < 0: " + beginIndex).toString());
        }
    }

    public Buffer writeString(String string, Charset charset) {
        Intrinsics.checkNotNullParameter(string, TypedValues.Custom.S_STRING);
        Intrinsics.checkNotNullParameter(charset, "charset");
        return writeString(string, 0, string.length(), charset);
    }

    public final Buffer writeTo(OutputStream outputStream) throws IOException {
        return writeTo$default(this, outputStream, 0, 2, (Object) null);
    }

    public final Buffer writeTo(OutputStream out, long byteCount) throws IOException {
        Intrinsics.checkNotNullParameter(out, "out");
        long j = byteCount;
        Util.checkOffsetAndCount(this.size, 0, j);
        Segment segment = this.head;
        while (j > 0) {
            Intrinsics.checkNotNull(segment);
            int min = (int) Math.min(j, (long) (segment.limit - segment.pos));
            out.write(segment.data, segment.pos, min);
            segment.pos += min;
            this.size -= (long) min;
            j -= (long) min;
            if (segment.pos == segment.limit) {
                Segment segment2 = segment;
                segment = segment2.pop();
                this.head = segment;
                SegmentPool.recycle(segment2);
            }
        }
        return this;
    }

    public Buffer writeUtf8(String string) {
        Intrinsics.checkNotNullParameter(string, TypedValues.Custom.S_STRING);
        return writeUtf8(string, 0, string.length());
    }

    public Buffer writeUtf8(String string, int beginIndex, int endIndex) {
        int i;
        String str = string;
        int i2 = beginIndex;
        int i3 = endIndex;
        Intrinsics.checkNotNullParameter(str, TypedValues.Custom.S_STRING);
        int i4 = 1;
        if (i2 >= 0) {
            if (i3 >= i2) {
                if (i3 <= string.length()) {
                    int i5 = beginIndex;
                    while (i5 < i3) {
                        char charAt = str.charAt(i5);
                        if (charAt < 128) {
                            Segment writableSegment$okio = writableSegment$okio(i4);
                            byte[] bArr = writableSegment$okio.data;
                            int i6 = writableSegment$okio.limit - i5;
                            int min = Math.min(i3, 8192 - i6);
                            int i7 = i5 + 1;
                            bArr[i5 + i6] = (byte) charAt;
                            while (i7 < min) {
                                char charAt2 = str.charAt(i7);
                                if (charAt2 >= 128) {
                                    break;
                                }
                                bArr[i7 + i6] = (byte) charAt2;
                                i7++;
                            }
                            int i8 = (i7 + i6) - writableSegment$okio.limit;
                            writableSegment$okio.limit += i8;
                            setSize$okio(((long) i8) + size());
                            i5 = i7;
                            i = 1;
                        } else if (charAt < 2048) {
                            Segment writableSegment$okio2 = writableSegment$okio(2);
                            writableSegment$okio2.data[writableSegment$okio2.limit] = (byte) ((charAt >> 6) | 192);
                            writableSegment$okio2.data[writableSegment$okio2.limit + 1] = (byte) (128 | (charAt & '?'));
                            writableSegment$okio2.limit += 2;
                            setSize$okio(size() + 2);
                            i5++;
                            i = 1;
                        } else if (charAt < 55296 || charAt > 57343) {
                            Segment writableSegment$okio3 = writableSegment$okio(3);
                            writableSegment$okio3.data[writableSegment$okio3.limit] = (byte) ((charAt >> 12) | 224);
                            i = 1;
                            writableSegment$okio3.data[writableSegment$okio3.limit + 1] = (byte) ((63 & (charAt >> 6)) | 128);
                            writableSegment$okio3.data[writableSegment$okio3.limit + 2] = (byte) ((charAt & '?') | 128);
                            writableSegment$okio3.limit += 3;
                            setSize$okio(size() + 3);
                            i5++;
                        } else {
                            char charAt3 = i5 + 1 < i3 ? str.charAt(i5 + 1) : 0;
                            if (charAt > 56319 || 56320 > charAt3 || 57343 < charAt3) {
                                writeByte(63);
                                i5++;
                                i = 1;
                            } else {
                                int i9 = (((charAt & 1023) << 10) | (charAt3 & 1023)) + CharCompanionObject.MIN_VALUE;
                                Segment writableSegment$okio4 = writableSegment$okio(4);
                                writableSegment$okio4.data[writableSegment$okio4.limit] = (byte) ((i9 >> 18) | 240);
                                writableSegment$okio4.data[writableSegment$okio4.limit + 1] = (byte) (((i9 >> 12) & 63) | 128);
                                writableSegment$okio4.data[writableSegment$okio4.limit + 2] = (byte) (((i9 >> 6) & 63) | 128);
                                writableSegment$okio4.data[writableSegment$okio4.limit + 3] = (byte) (128 | (i9 & 63));
                                writableSegment$okio4.limit += 4;
                                setSize$okio(size() + 4);
                                i5 += 2;
                                i = 1;
                            }
                        }
                        i4 = i;
                    }
                    return this;
                }
                throw new IllegalArgumentException(("endIndex > string.length: " + i3 + " > " + string.length()).toString());
            }
            throw new IllegalArgumentException(("endIndex < beginIndex: " + i3 + " < " + i2).toString());
        }
        throw new IllegalArgumentException(("beginIndex < 0: " + i2).toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x00b1  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00bb  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00af A[EDGE_INSN: B:45:0x00af->B:29:0x00af ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:5:0x001f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long readHexadecimalUnsignedLong() throws java.io.EOFException {
        /*
            r16 = this;
            r0 = r16
            r1 = 0
            long r2 = r0.size()
            r4 = 0
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 == 0) goto L_0x00cf
            r2 = 0
            r6 = 0
            r7 = 0
        L_0x0011:
            okio.Segment r8 = r0.head
            kotlin.jvm.internal.Intrinsics.checkNotNull(r8)
            byte[] r9 = r8.data
            int r10 = r8.pos
            int r11 = r8.limit
        L_0x001d:
            if (r10 >= r11) goto L_0x00af
            r12 = 0
            byte r13 = r9[r10]
            r14 = 48
            byte r14 = (byte) r14
            if (r13 < r14) goto L_0x002f
            r15 = 57
            byte r15 = (byte) r15
            if (r13 > r15) goto L_0x002f
            int r12 = r13 - r14
            goto L_0x004c
        L_0x002f:
            r14 = 97
            byte r14 = (byte) r14
            if (r13 < r14) goto L_0x003e
            r15 = 102(0x66, float:1.43E-43)
            byte r15 = (byte) r15
            if (r13 > r15) goto L_0x003e
            int r14 = r13 - r14
            int r12 = r14 + 10
            goto L_0x004c
        L_0x003e:
            r14 = 65
            byte r14 = (byte) r14
            if (r13 < r14) goto L_0x0089
            r15 = 70
            byte r15 = (byte) r15
            if (r13 > r15) goto L_0x0089
            int r14 = r13 - r14
            int r12 = r14 + 10
        L_0x004c:
            r14 = -1152921504606846976(0xf000000000000000, double:-3.105036184601418E231)
            long r14 = r14 & r2
            int r14 = (r14 > r4 ? 1 : (r14 == r4 ? 0 : -1))
            if (r14 != 0) goto L_0x005d
            r14 = 4
            long r2 = r2 << r14
            long r14 = (long) r12
            long r2 = r2 | r14
            int r10 = r10 + 1
            int r6 = r6 + 1
            goto L_0x001d
        L_0x005d:
            okio.Buffer r4 = new okio.Buffer
            r4.<init>()
            okio.Buffer r4 = r4.writeHexadecimalUnsignedLong((long) r2)
            okio.Buffer r4 = r4.writeByte((int) r13)
            java.lang.NumberFormatException r5 = new java.lang.NumberFormatException
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = "Number too large: "
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.String r15 = r4.readUtf8()
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.String r14 = r14.toString()
            r5.<init>(r14)
            java.lang.Throwable r5 = (java.lang.Throwable) r5
            throw r5
        L_0x0089:
            if (r6 == 0) goto L_0x008d
            r7 = 1
            goto L_0x00af
        L_0x008d:
            java.lang.NumberFormatException r4 = new java.lang.NumberFormatException
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r14 = "Expected leading [0-9a-fA-F] character but was 0x"
            java.lang.StringBuilder r5 = r5.append(r14)
            java.lang.String r14 = okio.Util.toHexString((byte) r13)
            mt.Log1F380D.a((java.lang.Object) r14)
            java.lang.StringBuilder r5 = r5.append(r14)
            java.lang.String r5 = r5.toString()
            r4.<init>(r5)
            java.lang.Throwable r4 = (java.lang.Throwable) r4
            throw r4
        L_0x00af:
            if (r10 != r11) goto L_0x00bb
            okio.Segment r12 = r8.pop()
            r0.head = r12
            okio.SegmentPool.recycle(r8)
            goto L_0x00bd
        L_0x00bb:
            r8.pos = r10
        L_0x00bd:
            if (r7 != 0) goto L_0x00c4
            okio.Segment r12 = r0.head
            if (r12 != 0) goto L_0x0011
        L_0x00c4:
            long r4 = r0.size()
            long r8 = (long) r6
            long r4 = r4 - r8
            r0.setSize$okio(r4)
            return r2
        L_0x00cf:
            java.io.EOFException r2 = new java.io.EOFException
            r2.<init>()
            java.lang.Throwable r2 = (java.lang.Throwable) r2
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Buffer.readHexadecimalUnsignedLong():long");
    }

    public String readString(long byteCount, Charset charset) throws EOFException {
        Intrinsics.checkNotNullParameter(charset, "charset");
        if (!(byteCount >= 0 && byteCount <= ((long) Integer.MAX_VALUE))) {
            throw new IllegalArgumentException(("byteCount: " + byteCount).toString());
        } else if (this.size < byteCount) {
            throw new EOFException();
        } else if (byteCount == 0) {
            return HttpUrl.FRAGMENT_ENCODE_SET;
        } else {
            Segment segment = this.head;
            Intrinsics.checkNotNull(segment);
            if (((long) segment.pos) + byteCount > ((long) segment.limit)) {
                String str = new String(readByteArray(byteCount), charset);
                Log1F380D.a((Object) str);
                return str;
            }
            String str2 = new String(segment.data, segment.pos, (int) byteCount, charset);
            Log1F380D.a((Object) str2);
            String str3 = str2;
            segment.pos += (int) byteCount;
            this.size -= byteCount;
            if (segment.pos == segment.limit) {
                this.head = segment.pop();
                SegmentPool.recycle(segment);
            }
            return str3;
        }
    }

    public int readUtf8CodePoint() throws EOFException {
        byte b;
        int i;
        byte b2;
        if (size() != 0) {
            byte b3 = getByte(0);
            if ((128 & b3) == 0) {
                b2 = b3 & ByteCompanionObject.MAX_VALUE;
                i = 1;
                b = 0;
            } else if ((224 & b3) == 192) {
                b2 = b3 & 31;
                i = 2;
                b = ByteCompanionObject.MIN_VALUE;
            } else if ((240 & b3) == 224) {
                b2 = b3 & 15;
                i = 3;
                b = UByte.MIN_VALUE;
            } else if ((248 & b3) == 240) {
                b2 = b3 & 7;
                i = 4;
                b = UByte.MIN_VALUE;
            } else {
                skip(1);
                return Utf8.REPLACEMENT_CODE_POINT;
            }
            if (size() >= ((long) i)) {
                int i2 = 1;
                while (i2 < i) {
                    byte b4 = getByte((long) i2);
                    if ((192 & b4) == 128) {
                        b2 = (b2 << 6) | (63 & b4);
                        i2++;
                    } else {
                        skip((long) i2);
                        return Utf8.REPLACEMENT_CODE_POINT;
                    }
                }
                skip((long) i);
                if (b2 > 1114111) {
                    return Utf8.REPLACEMENT_CODE_POINT;
                }
                return ((55296 <= b2 && 57343 >= b2) || b2 < b) ? Utf8.REPLACEMENT_CODE_POINT : b2;
            }
            StringBuilder append = new StringBuilder().append("size < ").append(i).append(": ").append(size()).append(" (to read code point prefixed 0x");
            String hexString = Util.toHexString(b3);
            Log1F380D.a((Object) hexString);
            throw new EOFException(append.append(hexString).append(')').toString());
        }
        throw new EOFException();
    }

    public String readUtf8Line() throws EOFException {
        long indexOf = indexOf((byte) 10);
        if (indexOf != -1) {
            String readUtf8Line = BufferKt.readUtf8Line(this, indexOf);
            Log1F380D.a((Object) readUtf8Line);
            return readUtf8Line;
        } else if (size() != 0) {
            return readUtf8(size());
        } else {
            return null;
        }
    }

    public String readUtf8LineStrict(long limit) throws EOFException {
        long j = limit;
        if (j >= 0) {
            long j2 = Long.MAX_VALUE;
            if (j != Long.MAX_VALUE) {
                j2 = j + 1;
            }
            long j3 = j2;
            byte b = (byte) 10;
            long indexOf = indexOf(b, 0, j3);
            if (indexOf != -1) {
                String readUtf8Line = BufferKt.readUtf8Line(this, indexOf);
                Log1F380D.a((Object) readUtf8Line);
                return readUtf8Line;
            } else if (j3 < size() && getByte(j3 - 1) == ((byte) 13) && getByte(j3) == b) {
                String readUtf8Line2 = BufferKt.readUtf8Line(this, j3);
                Log1F380D.a((Object) readUtf8Line2);
                return readUtf8Line2;
            } else {
                Buffer buffer = new Buffer();
                long j4 = indexOf;
                copyTo(buffer, 0, Math.min((long) 32, size()));
                throw new EOFException("\\n not found: limit=" + Math.min(size(), j) + " content=" + buffer.readByteString().hex() + Typography.ellipsis);
            }
        } else {
            throw new IllegalArgumentException(("limit < 0: " + j).toString());
        }
    }

    public Buffer writeUtf8CodePoint(int codePoint) {
        if (codePoint < 128) {
            writeByte(codePoint);
        } else if (codePoint < 2048) {
            Segment writableSegment$okio = writableSegment$okio(2);
            writableSegment$okio.data[writableSegment$okio.limit] = (byte) ((codePoint >> 6) | 192);
            writableSegment$okio.data[writableSegment$okio.limit + 1] = (byte) (128 | (codePoint & 63));
            writableSegment$okio.limit += 2;
            setSize$okio(size() + 2);
        } else if (55296 <= codePoint && 57343 >= codePoint) {
            writeByte(63);
        } else if (codePoint < 65536) {
            Segment writableSegment$okio2 = writableSegment$okio(3);
            writableSegment$okio2.data[writableSegment$okio2.limit] = (byte) ((codePoint >> 12) | 224);
            writableSegment$okio2.data[writableSegment$okio2.limit + 1] = (byte) ((63 & (codePoint >> 6)) | 128);
            writableSegment$okio2.data[writableSegment$okio2.limit + 2] = (byte) (128 | (codePoint & 63));
            writableSegment$okio2.limit += 3;
            setSize$okio(size() + 3);
        } else if (codePoint <= 1114111) {
            Segment writableSegment$okio3 = writableSegment$okio(4);
            writableSegment$okio3.data[writableSegment$okio3.limit] = (byte) ((codePoint >> 18) | 240);
            writableSegment$okio3.data[writableSegment$okio3.limit + 1] = (byte) (((codePoint >> 12) & 63) | 128);
            writableSegment$okio3.data[writableSegment$okio3.limit + 2] = (byte) (((codePoint >> 6) & 63) | 128);
            writableSegment$okio3.data[writableSegment$okio3.limit + 3] = (byte) (128 | (codePoint & 63));
            writableSegment$okio3.limit += 4;
            setSize$okio(size() + 4);
        } else {
            StringBuilder append = new StringBuilder().append("Unexpected code point: 0x");
            String hexString = Util.toHexString(codePoint);
            Log1F380D.a((Object) hexString);
            throw new IllegalArgumentException(append.append(hexString).toString());
        }
        return this;
    }
}
