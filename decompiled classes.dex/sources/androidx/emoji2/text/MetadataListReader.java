package androidx.emoji2.text;

import android.content.res.AssetManager;
import androidx.emoji2.text.flatbuffer.MetadataList;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class MetadataListReader {
    private static final int EMJI_TAG = 1164798569;
    private static final int EMJI_TAG_DEPRECATED = 1701669481;
    private static final int META_TABLE_NAME = 1835365473;

    private static class ByteBufferReader implements OpenTypeReader {
        private final ByteBuffer mByteBuffer;

        ByteBufferReader(ByteBuffer byteBuffer) {
            this.mByteBuffer = byteBuffer;
            byteBuffer.order(ByteOrder.BIG_ENDIAN);
        }

        public long getPosition() {
            return (long) this.mByteBuffer.position();
        }

        public int readTag() throws IOException {
            return this.mByteBuffer.getInt();
        }

        public long readUnsignedInt() throws IOException {
            return MetadataListReader.toUnsignedInt(this.mByteBuffer.getInt());
        }

        public int readUnsignedShort() throws IOException {
            return MetadataListReader.toUnsignedShort(this.mByteBuffer.getShort());
        }

        public void skip(int numOfBytes) throws IOException {
            ByteBuffer byteBuffer = this.mByteBuffer;
            byteBuffer.position(byteBuffer.position() + numOfBytes);
        }
    }

    private static class InputStreamOpenTypeReader implements OpenTypeReader {
        private final byte[] mByteArray;
        private final ByteBuffer mByteBuffer;
        private final InputStream mInputStream;
        private long mPosition = 0;

        InputStreamOpenTypeReader(InputStream inputStream) {
            this.mInputStream = inputStream;
            byte[] bArr = new byte[4];
            this.mByteArray = bArr;
            ByteBuffer wrap = ByteBuffer.wrap(bArr);
            this.mByteBuffer = wrap;
            wrap.order(ByteOrder.BIG_ENDIAN);
        }

        private void read(int numOfBytes) throws IOException {
            if (this.mInputStream.read(this.mByteArray, 0, numOfBytes) == numOfBytes) {
                this.mPosition += (long) numOfBytes;
                return;
            }
            throw new IOException("read failed");
        }

        public long getPosition() {
            return this.mPosition;
        }

        public int readTag() throws IOException {
            this.mByteBuffer.position(0);
            read(4);
            return this.mByteBuffer.getInt();
        }

        public long readUnsignedInt() throws IOException {
            this.mByteBuffer.position(0);
            read(4);
            return MetadataListReader.toUnsignedInt(this.mByteBuffer.getInt());
        }

        public int readUnsignedShort() throws IOException {
            this.mByteBuffer.position(0);
            read(2);
            return MetadataListReader.toUnsignedShort(this.mByteBuffer.getShort());
        }

        public void skip(int numOfBytes) throws IOException {
            while (numOfBytes > 0) {
                int skip = (int) this.mInputStream.skip((long) numOfBytes);
                if (skip >= 1) {
                    numOfBytes -= skip;
                    this.mPosition += (long) skip;
                } else {
                    throw new IOException("Skip didn't move at least 1 byte forward");
                }
            }
        }
    }

    private static class OffsetInfo {
        private final long mLength;
        private final long mStartOffset;

        OffsetInfo(long startOffset, long length) {
            this.mStartOffset = startOffset;
            this.mLength = length;
        }

        /* access modifiers changed from: package-private */
        public long getLength() {
            return this.mLength;
        }

        /* access modifiers changed from: package-private */
        public long getStartOffset() {
            return this.mStartOffset;
        }
    }

    private interface OpenTypeReader {
        public static final int UINT16_BYTE_COUNT = 2;
        public static final int UINT32_BYTE_COUNT = 4;

        long getPosition();

        int readTag() throws IOException;

        long readUnsignedInt() throws IOException;

        int readUnsignedShort() throws IOException;

        void skip(int i) throws IOException;
    }

    private MetadataListReader() {
    }

    private static OffsetInfo findOffsetInfo(OpenTypeReader reader) throws IOException {
        reader.skip(4);
        int readUnsignedShort = reader.readUnsignedShort();
        if (readUnsignedShort <= 100) {
            reader.skip(6);
            long j = -1;
            int i = 0;
            while (true) {
                if (i >= readUnsignedShort) {
                    break;
                }
                int readTag = reader.readTag();
                reader.skip(4);
                long readUnsignedInt = reader.readUnsignedInt();
                reader.skip(4);
                if (META_TABLE_NAME == readTag) {
                    j = readUnsignedInt;
                    break;
                }
                i++;
            }
            if (j != -1) {
                reader.skip((int) (j - reader.getPosition()));
                reader.skip(12);
                long readUnsignedInt2 = reader.readUnsignedInt();
                for (int i2 = 0; ((long) i2) < readUnsignedInt2; i2++) {
                    int readTag2 = reader.readTag();
                    long readUnsignedInt3 = reader.readUnsignedInt();
                    long readUnsignedInt4 = reader.readUnsignedInt();
                    if (EMJI_TAG == readTag2 || EMJI_TAG_DEPRECATED == readTag2) {
                        return new OffsetInfo(readUnsignedInt3 + j, readUnsignedInt4);
                    }
                }
            }
            throw new IOException("Cannot read metadata.");
        }
        throw new IOException("Cannot read metadata.");
    }

    static MetadataList read(AssetManager assetManager, String assetPath) throws IOException {
        InputStream open = assetManager.open(assetPath);
        try {
            MetadataList read = read(open);
            if (open != null) {
                open.close();
            }
            return read;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    static MetadataList read(InputStream inputStream) throws IOException {
        InputStreamOpenTypeReader inputStreamOpenTypeReader = new InputStreamOpenTypeReader(inputStream);
        OffsetInfo findOffsetInfo = findOffsetInfo(inputStreamOpenTypeReader);
        inputStreamOpenTypeReader.skip((int) (findOffsetInfo.getStartOffset() - inputStreamOpenTypeReader.getPosition()));
        ByteBuffer allocate = ByteBuffer.allocate((int) findOffsetInfo.getLength());
        int read = inputStream.read(allocate.array());
        if (((long) read) == findOffsetInfo.getLength()) {
            return MetadataList.getRootAsMetadataList(allocate);
        }
        throw new IOException("Needed " + findOffsetInfo.getLength() + " bytes, got " + read);
    }

    static MetadataList read(ByteBuffer byteBuffer) throws IOException {
        ByteBuffer duplicate = byteBuffer.duplicate();
        duplicate.position((int) findOffsetInfo(new ByteBufferReader(duplicate)).getStartOffset());
        return MetadataList.getRootAsMetadataList(duplicate);
    }

    static long toUnsignedInt(int value) {
        return ((long) value) & 4294967295L;
    }

    static int toUnsignedShort(short value) {
        return 65535 & value;
    }
}
