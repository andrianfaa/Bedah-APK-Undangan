package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class MetadataItem extends Table {

    public static final class Vector extends BaseVector {
        public Vector __assign(int _vector, int _element_size, ByteBuffer _bb) {
            __reset(_vector, _element_size, _bb);
            return this;
        }

        public MetadataItem get(int j) {
            return get(new MetadataItem(), j);
        }

        public MetadataItem get(MetadataItem obj, int j) {
            return obj.__assign(MetadataItem.__indirect(__element(j), this.bb), this.bb);
        }
    }

    public static void ValidateVersion() {
        Constants.FLATBUFFERS_1_12_0();
    }

    public static void addCodepoints(FlatBufferBuilder builder, int codepointsOffset) {
        builder.addOffset(6, codepointsOffset, 0);
    }

    public static void addCompatAdded(FlatBufferBuilder builder, short compatAdded) {
        builder.addShort(3, compatAdded, 0);
    }

    public static void addEmojiStyle(FlatBufferBuilder builder, boolean emojiStyle) {
        builder.addBoolean(1, emojiStyle, false);
    }

    public static void addHeight(FlatBufferBuilder builder, short height) {
        builder.addShort(5, height, 0);
    }

    public static void addId(FlatBufferBuilder builder, int id) {
        builder.addInt(0, id, 0);
    }

    public static void addSdkAdded(FlatBufferBuilder builder, short sdkAdded) {
        builder.addShort(2, sdkAdded, 0);
    }

    public static void addWidth(FlatBufferBuilder builder, short width) {
        builder.addShort(4, width, 0);
    }

    public static int createCodepointsVector(FlatBufferBuilder builder, int[] data) {
        builder.startVector(4, data.length, 4);
        for (int length = data.length - 1; length >= 0; length--) {
            builder.addInt(data[length]);
        }
        return builder.endVector();
    }

    public static int createMetadataItem(FlatBufferBuilder builder, int id, boolean emojiStyle, short sdkAdded, short compatAdded, short width, short height, int codepointsOffset) {
        builder.startTable(7);
        addCodepoints(builder, codepointsOffset);
        addId(builder, id);
        addHeight(builder, height);
        addWidth(builder, width);
        addCompatAdded(builder, compatAdded);
        addSdkAdded(builder, sdkAdded);
        addEmojiStyle(builder, emojiStyle);
        return endMetadataItem(builder);
    }

    public static int endMetadataItem(FlatBufferBuilder builder) {
        return builder.endTable();
    }

    public static MetadataItem getRootAsMetadataItem(ByteBuffer _bb) {
        return getRootAsMetadataItem(_bb, new MetadataItem());
    }

    public static MetadataItem getRootAsMetadataItem(ByteBuffer _bb, MetadataItem obj) {
        _bb.order(ByteOrder.LITTLE_ENDIAN);
        return obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb);
    }

    public static void startCodepointsVector(FlatBufferBuilder builder, int numElems) {
        builder.startVector(4, numElems, 4);
    }

    public static void startMetadataItem(FlatBufferBuilder builder) {
        builder.startTable(7);
    }

    public MetadataItem __assign(int _i, ByteBuffer _bb) {
        __init(_i, _bb);
        return this;
    }

    public void __init(int _i, ByteBuffer _bb) {
        __reset(_i, _bb);
    }

    public int codepoints(int j) {
        int __offset = __offset(16);
        if (__offset != 0) {
            return this.bb.getInt(__vector(__offset) + (j * 4));
        }
        return 0;
    }

    public ByteBuffer codepointsAsByteBuffer() {
        return __vector_as_bytebuffer(16, 4);
    }

    public ByteBuffer codepointsInByteBuffer(ByteBuffer _bb) {
        return __vector_in_bytebuffer(_bb, 16, 4);
    }

    public int codepointsLength() {
        int __offset = __offset(16);
        if (__offset != 0) {
            return __vector_len(__offset);
        }
        return 0;
    }

    public IntVector codepointsVector() {
        return codepointsVector(new IntVector());
    }

    public IntVector codepointsVector(IntVector obj) {
        int __offset = __offset(16);
        if (__offset != 0) {
            return obj.__assign(__vector(__offset), this.bb);
        }
        return null;
    }

    public short compatAdded() {
        int __offset = __offset(10);
        if (__offset != 0) {
            return this.bb.getShort(this.bb_pos + __offset);
        }
        return 0;
    }

    public boolean emojiStyle() {
        int __offset = __offset(6);
        return (__offset == 0 || this.bb.get(this.bb_pos + __offset) == 0) ? false : true;
    }

    public short height() {
        int __offset = __offset(14);
        if (__offset != 0) {
            return this.bb.getShort(this.bb_pos + __offset);
        }
        return 0;
    }

    public int id() {
        int __offset = __offset(4);
        if (__offset != 0) {
            return this.bb.getInt(this.bb_pos + __offset);
        }
        return 0;
    }

    public short sdkAdded() {
        int __offset = __offset(8);
        if (__offset != 0) {
            return this.bb.getShort(this.bb_pos + __offset);
        }
        return 0;
    }

    public short width() {
        int __offset = __offset(12);
        if (__offset != 0) {
            return this.bb.getShort(this.bb_pos + __offset);
        }
        return 0;
    }
}
