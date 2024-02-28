package com.google.android.material.color;

import android.content.Context;
import android.util.Pair;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.UByte;
import mt.Log1F380D;

/* compiled from: 00E9 */
final class ColorResourcesTableCreator {
    private static final byte ANDROID_PACKAGE_ID = 1;
    private static final PackageInfo ANDROID_PACKAGE_INFO = new PackageInfo(1, "android");
    private static final byte APPLICATION_PACKAGE_ID = Byte.MAX_VALUE;
    /* access modifiers changed from: private */
    public static final Comparator<ColorResource> COLOR_RESOURCE_COMPARATOR = new Comparator<ColorResource>() {
        public int compare(ColorResource res1, ColorResource res2) {
            return res1.entryId - res2.entryId;
        }
    };
    private static final short HEADER_TYPE_PACKAGE = 512;
    private static final short HEADER_TYPE_RES_TABLE = 2;
    private static final short HEADER_TYPE_STRING_POOL = 1;
    private static final short HEADER_TYPE_TYPE = 513;
    private static final short HEADER_TYPE_TYPE_SPEC = 514;
    private static final String RESOURCE_TYPE_NAME_COLOR = "color";
    /* access modifiers changed from: private */
    public static byte typeIdColor;

    static class ColorResource {
        /* access modifiers changed from: private */
        public final short entryId;
        /* access modifiers changed from: private */
        public final String name;
        /* access modifiers changed from: private */
        public final byte packageId;
        /* access modifiers changed from: private */
        public final byte typeId;
        /* access modifiers changed from: private */
        public final int value;

        ColorResource(int id, String name2, int value2) {
            this.name = name2;
            this.value = value2;
            this.entryId = (short) (65535 & id);
            this.typeId = (byte) ((id >> 16) & 255);
            this.packageId = (byte) ((id >> 24) & 255);
        }
    }

    /* compiled from: 00E8 */
    private static class PackageChunk {
        private static final short HEADER_SIZE = 288;
        private static final int PACKAGE_NAME_MAX_LENGTH = 128;
        private final ResChunkHeader header;
        private final StringPoolChunk keyStrings;
        private final PackageInfo packageInfo;
        private final TypeSpecChunk typeSpecChunk;
        private final StringPoolChunk typeStrings = new StringPoolChunk(false, "?1", "?2", "?3", "?4", "?5", "color");

        PackageChunk(PackageInfo packageInfo2, List<ColorResource> list) {
            this.packageInfo = packageInfo2;
            String[] strArr = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                String access$100 = list.get(i).name;
                Log1F380D.a((Object) access$100);
                strArr[i] = access$100;
            }
            this.keyStrings = new StringPoolChunk(true, strArr);
            this.typeSpecChunk = new TypeSpecChunk(list);
            this.header = new ResChunkHeader(ColorResourcesTableCreator.HEADER_TYPE_PACKAGE, HEADER_SIZE, getChunkSize());
        }

        /* access modifiers changed from: package-private */
        public int getChunkSize() {
            return this.typeStrings.getChunkSize() + 288 + this.keyStrings.getChunkSize() + this.typeSpecChunk.getChunkSizeWithTypeChunk();
        }

        /* access modifiers changed from: package-private */
        public void writeTo(ByteArrayOutputStream outputStream) throws IOException {
            this.header.writeTo(outputStream);
            outputStream.write(ColorResourcesTableCreator.intToByteArray(this.packageInfo.id));
            String access$1100 = this.packageInfo.name;
            Log1F380D.a((Object) access$1100);
            char[] charArray = access$1100.toCharArray();
            for (int i = 0; i < 128; i++) {
                if (i < charArray.length) {
                    outputStream.write(ColorResourcesTableCreator.charToByteArray(charArray[i]));
                } else {
                    outputStream.write(ColorResourcesTableCreator.charToByteArray(0));
                }
            }
            outputStream.write(ColorResourcesTableCreator.intToByteArray(288));
            outputStream.write(ColorResourcesTableCreator.intToByteArray(0));
            outputStream.write(ColorResourcesTableCreator.intToByteArray(this.typeStrings.getChunkSize() + 288));
            outputStream.write(ColorResourcesTableCreator.intToByteArray(0));
            outputStream.write(ColorResourcesTableCreator.intToByteArray(0));
            this.typeStrings.writeTo(outputStream);
            this.keyStrings.writeTo(outputStream);
            this.typeSpecChunk.writeTo(outputStream);
        }
    }

    static class PackageInfo {
        /* access modifiers changed from: private */
        public final int id;
        /* access modifiers changed from: private */
        public final String name;

        PackageInfo(int id2, String name2) {
            this.id = id2;
            this.name = name2;
        }
    }

    private static class ResChunkHeader {
        private final int chunkSize;
        private final short headerSize;
        private final short type;

        ResChunkHeader(short type2, short headerSize2, int chunkSize2) {
            this.type = type2;
            this.headerSize = headerSize2;
            this.chunkSize = chunkSize2;
        }

        /* access modifiers changed from: package-private */
        public void writeTo(ByteArrayOutputStream outputStream) throws IOException {
            outputStream.write(ColorResourcesTableCreator.shortToByteArray(this.type));
            outputStream.write(ColorResourcesTableCreator.shortToByteArray(this.headerSize));
            outputStream.write(ColorResourcesTableCreator.intToByteArray(this.chunkSize));
        }
    }

    private static class ResEntry {
        private static final byte DATA_TYPE_AARRGGBB = 28;
        private static final short ENTRY_SIZE = 8;
        private static final short FLAG_PUBLIC = 2;
        private static final int SIZE = 16;
        private static final short VALUE_SIZE = 8;
        private final int data;
        private final int keyStringIndex;

        ResEntry(int keyStringIndex2, int data2) {
            this.keyStringIndex = keyStringIndex2;
            this.data = data2;
        }

        /* access modifiers changed from: package-private */
        public void writeTo(ByteArrayOutputStream outputStream) throws IOException {
            outputStream.write(ColorResourcesTableCreator.shortToByteArray(8));
            outputStream.write(ColorResourcesTableCreator.shortToByteArray(FLAG_PUBLIC));
            outputStream.write(ColorResourcesTableCreator.intToByteArray(this.keyStringIndex));
            outputStream.write(ColorResourcesTableCreator.shortToByteArray(8));
            outputStream.write(new byte[]{0, DATA_TYPE_AARRGGBB});
            outputStream.write(ColorResourcesTableCreator.intToByteArray(this.data));
        }
    }

    private static class ResTable {
        private static final short HEADER_SIZE = 12;
        private final ResChunkHeader header;
        private final List<PackageChunk> packageChunks = new ArrayList();
        private final int packageCount;
        private final StringPoolChunk stringPool;

        ResTable(Map<PackageInfo, List<ColorResource>> map) {
            this.packageCount = map.size();
            this.stringPool = new StringPoolChunk(new String[0]);
            for (Map.Entry next : map.entrySet()) {
                List list = (List) next.getValue();
                Collections.sort(list, ColorResourcesTableCreator.COLOR_RESOURCE_COMPARATOR);
                this.packageChunks.add(new PackageChunk((PackageInfo) next.getKey(), list));
            }
            this.header = new ResChunkHeader(ColorResourcesTableCreator.HEADER_TYPE_RES_TABLE, HEADER_SIZE, getOverallSize());
        }

        private int getOverallSize() {
            int i = 0;
            for (PackageChunk chunkSize : this.packageChunks) {
                i += chunkSize.getChunkSize();
            }
            return this.stringPool.getChunkSize() + 12 + i;
        }

        /* access modifiers changed from: package-private */
        public void writeTo(ByteArrayOutputStream outputStream) throws IOException {
            this.header.writeTo(outputStream);
            outputStream.write(ColorResourcesTableCreator.intToByteArray(this.packageCount));
            this.stringPool.writeTo(outputStream);
            for (PackageChunk writeTo : this.packageChunks) {
                writeTo.writeTo(outputStream);
            }
        }
    }

    private static class StringPoolChunk {
        private static final int FLAG_UTF8 = 256;
        private static final short HEADER_SIZE = 28;
        private static final int STYLED_SPAN_LIST_END = -1;
        private final int chunkSize;
        private final ResChunkHeader header;
        private final int stringCount;
        private final List<Integer> stringIndex;
        private final List<byte[]> strings;
        private final int stringsPaddingSize;
        private final int stringsStart;
        private final int styledSpanCount;
        private final List<Integer> styledSpanIndex;
        private final List<List<StringStyledSpan>> styledSpans;
        private final int styledSpansStart;
        private final boolean utf8Encode;

        StringPoolChunk(boolean utf8, String... rawStrings) {
            this.stringIndex = new ArrayList();
            this.styledSpanIndex = new ArrayList();
            this.strings = new ArrayList();
            this.styledSpans = new ArrayList();
            this.utf8Encode = utf8;
            int i = 0;
            int i2 = 0;
            for (String processString : rawStrings) {
                Pair<byte[], List<StringStyledSpan>> processString2 = processString(processString);
                this.stringIndex.add(Integer.valueOf(i));
                i += ((byte[]) processString2.first).length;
                this.strings.add(processString2.first);
                this.styledSpans.add(processString2.second);
            }
            int i3 = 0;
            for (List<StringStyledSpan> next : this.styledSpans) {
                for (StringStyledSpan stringStyledSpan : next) {
                    this.stringIndex.add(Integer.valueOf(i));
                    i += stringStyledSpan.styleString.length;
                    this.strings.add(stringStyledSpan.styleString);
                }
                this.styledSpanIndex.add(Integer.valueOf(i3));
                i3 += (next.size() * 12) + 4;
            }
            int i4 = i % 4;
            int i5 = i4 == 0 ? 0 : 4 - i4;
            this.stringsPaddingSize = i5;
            int size = this.strings.size();
            this.stringCount = size;
            this.styledSpanCount = this.strings.size() - rawStrings.length;
            boolean z = this.strings.size() - rawStrings.length > 0;
            if (!z) {
                this.styledSpanIndex.clear();
                this.styledSpans.clear();
            }
            int size2 = (size * 4) + 28 + (this.styledSpanIndex.size() * 4);
            this.stringsStart = size2;
            int i6 = i5 + i;
            this.styledSpansStart = z ? size2 + i6 : 0;
            int i7 = size2 + i6 + (z ? i3 : i2);
            this.chunkSize = i7;
            this.header = new ResChunkHeader(ColorResourcesTableCreator.HEADER_TYPE_STRING_POOL, HEADER_SIZE, i7);
        }

        StringPoolChunk(String... rawStrings) {
            this(false, rawStrings);
        }

        private Pair<byte[], List<StringStyledSpan>> processString(String rawString) {
            return new Pair<>(this.utf8Encode ? ColorResourcesTableCreator.stringToByteArrayUtf8(rawString) : ColorResourcesTableCreator.stringToByteArray(rawString), Collections.emptyList());
        }

        /* access modifiers changed from: package-private */
        public int getChunkSize() {
            return this.chunkSize;
        }

        /* access modifiers changed from: package-private */
        public void writeTo(ByteArrayOutputStream outputStream) throws IOException {
            this.header.writeTo(outputStream);
            outputStream.write(ColorResourcesTableCreator.intToByteArray(this.stringCount));
            outputStream.write(ColorResourcesTableCreator.intToByteArray(this.styledSpanCount));
            outputStream.write(ColorResourcesTableCreator.intToByteArray(this.utf8Encode ? 256 : 0));
            outputStream.write(ColorResourcesTableCreator.intToByteArray(this.stringsStart));
            outputStream.write(ColorResourcesTableCreator.intToByteArray(this.styledSpansStart));
            for (Integer intValue : this.stringIndex) {
                outputStream.write(ColorResourcesTableCreator.intToByteArray(intValue.intValue()));
            }
            for (Integer intValue2 : this.styledSpanIndex) {
                outputStream.write(ColorResourcesTableCreator.intToByteArray(intValue2.intValue()));
            }
            for (byte[] write : this.strings) {
                outputStream.write(write);
            }
            int i = this.stringsPaddingSize;
            if (i > 0) {
                outputStream.write(new byte[i]);
            }
            for (List<StringStyledSpan> it : this.styledSpans) {
                for (StringStyledSpan writeTo : it) {
                    writeTo.writeTo(outputStream);
                }
                outputStream.write(ColorResourcesTableCreator.intToByteArray(-1));
            }
        }
    }

    private static class StringStyledSpan {
        private int firstCharacterIndex;
        private int lastCharacterIndex;
        private int nameReference;
        /* access modifiers changed from: private */
        public byte[] styleString;

        private StringStyledSpan() {
        }

        /* access modifiers changed from: package-private */
        public void writeTo(ByteArrayOutputStream outputStream) throws IOException {
            outputStream.write(ColorResourcesTableCreator.intToByteArray(this.nameReference));
            outputStream.write(ColorResourcesTableCreator.intToByteArray(this.firstCharacterIndex));
            outputStream.write(ColorResourcesTableCreator.intToByteArray(this.lastCharacterIndex));
        }
    }

    private static class TypeChunk {
        private static final byte CONFIG_SIZE = 64;
        private static final short HEADER_SIZE = 84;
        private static final int OFFSET_NO_ENTRY = -1;
        private final byte[] config;
        private final int entryCount;
        private final ResChunkHeader header;
        private final int[] offsetTable;
        private final ResEntry[] resEntries;

        TypeChunk(List<ColorResource> list, Set<Short> set, int entryCount2) {
            byte[] bArr = new byte[64];
            this.config = bArr;
            this.entryCount = entryCount2;
            bArr[0] = CONFIG_SIZE;
            this.resEntries = new ResEntry[list.size()];
            for (int i = 0; i < list.size(); i++) {
                this.resEntries[i] = new ResEntry(i, list.get(i).value);
            }
            this.offsetTable = new int[entryCount2];
            int i2 = 0;
            for (short s = 0; s < entryCount2; s = (short) (s + ColorResourcesTableCreator.HEADER_TYPE_STRING_POOL)) {
                if (set.contains(Short.valueOf(s))) {
                    this.offsetTable[s] = i2;
                    i2 += 16;
                } else {
                    this.offsetTable[s] = -1;
                }
            }
            this.header = new ResChunkHeader(ColorResourcesTableCreator.HEADER_TYPE_TYPE, HEADER_SIZE, getChunkSize());
        }

        private int getEntryStart() {
            return getOffsetTableSize() + 84;
        }

        private int getOffsetTableSize() {
            return this.offsetTable.length * 4;
        }

        /* access modifiers changed from: package-private */
        public int getChunkSize() {
            return getEntryStart() + (this.resEntries.length * 16);
        }

        /* access modifiers changed from: package-private */
        public void writeTo(ByteArrayOutputStream outputStream) throws IOException {
            this.header.writeTo(outputStream);
            outputStream.write(new byte[]{ColorResourcesTableCreator.typeIdColor, 0, 0, 0});
            outputStream.write(ColorResourcesTableCreator.intToByteArray(this.entryCount));
            outputStream.write(ColorResourcesTableCreator.intToByteArray(getEntryStart()));
            outputStream.write(this.config);
            for (int access$500 : this.offsetTable) {
                outputStream.write(ColorResourcesTableCreator.intToByteArray(access$500));
            }
            for (ResEntry writeTo : this.resEntries) {
                writeTo.writeTo(outputStream);
            }
        }
    }

    private static class TypeSpecChunk {
        private static final short HEADER_SIZE = 16;
        private static final int SPEC_PUBLIC = 1073741824;
        private final int entryCount;
        private final int[] entryFlags;
        private final ResChunkHeader header;
        private final TypeChunk typeChunk;

        TypeSpecChunk(List<ColorResource> list) {
            this.entryCount = list.get(list.size() - 1).entryId + ColorResourcesTableCreator.HEADER_TYPE_STRING_POOL;
            HashSet hashSet = new HashSet();
            for (ColorResource access$000 : list) {
                hashSet.add(Short.valueOf(access$000.entryId));
            }
            this.entryFlags = new int[this.entryCount];
            for (short s = 0; s < this.entryCount; s = (short) (s + ColorResourcesTableCreator.HEADER_TYPE_STRING_POOL)) {
                if (hashSet.contains(Short.valueOf(s))) {
                    this.entryFlags[s] = 1073741824;
                }
            }
            this.header = new ResChunkHeader(ColorResourcesTableCreator.HEADER_TYPE_TYPE_SPEC, HEADER_SIZE, getChunkSize());
            this.typeChunk = new TypeChunk(list, hashSet, this.entryCount);
        }

        private int getChunkSize() {
            return (this.entryCount * 4) + 16;
        }

        /* access modifiers changed from: package-private */
        public int getChunkSizeWithTypeChunk() {
            return getChunkSize() + this.typeChunk.getChunkSize();
        }

        /* access modifiers changed from: package-private */
        public void writeTo(ByteArrayOutputStream outputStream) throws IOException {
            this.header.writeTo(outputStream);
            outputStream.write(new byte[]{ColorResourcesTableCreator.typeIdColor, 0, 0, 0});
            outputStream.write(ColorResourcesTableCreator.intToByteArray(this.entryCount));
            for (int access$500 : this.entryFlags) {
                outputStream.write(ColorResourcesTableCreator.intToByteArray(access$500));
            }
            this.typeChunk.writeTo(outputStream);
        }
    }

    private ColorResourcesTableCreator() {
    }

    /* access modifiers changed from: private */
    public static byte[] charToByteArray(char value) {
        return new byte[]{(byte) (value & 255), (byte) ((value >> 8) & 255)};
    }

    static byte[] create(Context context, Map<Integer, Integer> map) throws IOException {
        PackageInfo packageInfo;
        if (!map.entrySet().isEmpty()) {
            PackageInfo packageInfo2 = new PackageInfo(127, context.getPackageName());
            HashMap hashMap = new HashMap();
            ColorResource colorResource = null;
            for (Map.Entry next : map.entrySet()) {
                colorResource = new ColorResource(((Integer) next.getKey()).intValue(), context.getResources().getResourceName(((Integer) next.getKey()).intValue()), ((Integer) next.getValue()).intValue());
                if (context.getResources().getResourceTypeName(((Integer) next.getKey()).intValue()).equals("color")) {
                    if (colorResource.packageId == 1) {
                        packageInfo = ANDROID_PACKAGE_INFO;
                    } else if (colorResource.packageId == Byte.MAX_VALUE) {
                        packageInfo = packageInfo2;
                    } else {
                        throw new IllegalArgumentException("Not supported with unknown package id: " + colorResource.packageId);
                    }
                    if (!hashMap.containsKey(packageInfo)) {
                        hashMap.put(packageInfo, new ArrayList());
                    }
                    ((List) hashMap.get(packageInfo)).add(colorResource);
                } else {
                    StringBuilder append = new StringBuilder().append("Non color resource found: name=");
                    String access$100 = colorResource.name;
                    Log1F380D.a((Object) access$100);
                    StringBuilder append2 = append.append(access$100).append(", typeId=");
                    String hexString = Integer.toHexString(colorResource.typeId & UByte.MAX_VALUE);
                    Log1F380D.a((Object) hexString);
                    throw new IllegalArgumentException(append2.append(hexString).toString());
                }
            }
            byte access$200 = colorResource.typeId;
            typeIdColor = access$200;
            if (access$200 != 0) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                new ResTable(hashMap).writeTo(byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            }
            throw new IllegalArgumentException("No color resources found for harmonization.");
        }
        throw new IllegalArgumentException("No color resources provided for harmonization.");
    }

    /* access modifiers changed from: private */
    public static byte[] intToByteArray(int value) {
        return new byte[]{(byte) (value & 255), (byte) ((value >> 8) & 255), (byte) ((value >> 16) & 255), (byte) ((value >> 24) & 255)};
    }

    /* access modifiers changed from: private */
    public static byte[] shortToByteArray(short value) {
        return new byte[]{(byte) (value & 255), (byte) ((value >> 8) & 255)};
    }

    /* access modifiers changed from: private */
    public static byte[] stringToByteArray(String value) {
        char[] charArray = value.toCharArray();
        byte[] bArr = new byte[((charArray.length * 2) + 4)];
        byte[] shortToByteArray = shortToByteArray((short) charArray.length);
        bArr[0] = shortToByteArray[0];
        bArr[1] = shortToByteArray[1];
        for (int i = 0; i < charArray.length; i++) {
            byte[] charToByteArray = charToByteArray(charArray[i]);
            bArr[(i * 2) + 2] = charToByteArray[0];
            bArr[(i * 2) + 3] = charToByteArray[1];
        }
        bArr[bArr.length - 2] = 0;
        bArr[bArr.length - 1] = 0;
        return bArr;
    }

    /* access modifiers changed from: private */
    public static byte[] stringToByteArrayUtf8(String value) {
        byte[] bytes = value.getBytes(Charset.forName("UTF-8"));
        byte length = (byte) bytes.length;
        byte[] bArr = new byte[(bytes.length + 3)];
        System.arraycopy(bytes, 0, bArr, 2, length);
        bArr[1] = length;
        bArr[0] = length;
        bArr[bArr.length - 1] = 0;
        return bArr;
    }
}
