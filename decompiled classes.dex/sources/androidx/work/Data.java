package androidx.work;

import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import mt.Log1F380D;

/* compiled from: 00A4 */
public final class Data {
    public static final Data EMPTY = new Builder().build();
    public static final int MAX_DATA_BYTES = 10240;
    private static final String TAG;
    Map<String, Object> mValues;

    /* compiled from: 00A3 */
    public static final class Builder {
        private Map<String, Object> mValues = new HashMap();

        public Data build() {
            Data data = new Data((Map<String, ?>) this.mValues);
            Data.toByteArrayInternal(data);
            return data;
        }

        public Builder put(String key, Object value) {
            if (value == null) {
                this.mValues.put(key, (Object) null);
            } else {
                Class<?> cls = value.getClass();
                if (cls == Boolean.class || cls == Byte.class || cls == Integer.class || cls == Long.class || cls == Float.class || cls == Double.class || cls == String.class || cls == Boolean[].class || cls == Byte[].class || cls == Integer[].class || cls == Long[].class || cls == Float[].class || cls == Double[].class || cls == String[].class) {
                    this.mValues.put(key, value);
                } else if (cls == boolean[].class) {
                    this.mValues.put(key, Data.convertPrimitiveBooleanArray((boolean[]) value));
                } else if (cls == byte[].class) {
                    this.mValues.put(key, Data.convertPrimitiveByteArray((byte[]) value));
                } else if (cls == int[].class) {
                    this.mValues.put(key, Data.convertPrimitiveIntArray((int[]) value));
                } else if (cls == long[].class) {
                    this.mValues.put(key, Data.convertPrimitiveLongArray((long[]) value));
                } else if (cls == float[].class) {
                    this.mValues.put(key, Data.convertPrimitiveFloatArray((float[]) value));
                } else if (cls == double[].class) {
                    this.mValues.put(key, Data.convertPrimitiveDoubleArray((double[]) value));
                } else {
                    String format = String.format("Key %s has invalid type %s", new Object[]{key, cls});
                    Log1F380D.a((Object) format);
                    throw new IllegalArgumentException(format);
                }
            }
            return this;
        }

        public Builder putAll(Data data) {
            putAll(data.mValues);
            return this;
        }

        public Builder putAll(Map<String, Object> map) {
            for (Map.Entry next : map.entrySet()) {
                put((String) next.getKey(), next.getValue());
            }
            return this;
        }

        public Builder putBoolean(String key, boolean value) {
            this.mValues.put(key, Boolean.valueOf(value));
            return this;
        }

        public Builder putBooleanArray(String key, boolean[] value) {
            this.mValues.put(key, Data.convertPrimitiveBooleanArray(value));
            return this;
        }

        public Builder putByte(String key, byte value) {
            this.mValues.put(key, Byte.valueOf(value));
            return this;
        }

        public Builder putByteArray(String key, byte[] value) {
            this.mValues.put(key, Data.convertPrimitiveByteArray(value));
            return this;
        }

        public Builder putDouble(String key, double value) {
            this.mValues.put(key, Double.valueOf(value));
            return this;
        }

        public Builder putDoubleArray(String key, double[] value) {
            this.mValues.put(key, Data.convertPrimitiveDoubleArray(value));
            return this;
        }

        public Builder putFloat(String key, float value) {
            this.mValues.put(key, Float.valueOf(value));
            return this;
        }

        public Builder putFloatArray(String key, float[] value) {
            this.mValues.put(key, Data.convertPrimitiveFloatArray(value));
            return this;
        }

        public Builder putInt(String key, int value) {
            this.mValues.put(key, Integer.valueOf(value));
            return this;
        }

        public Builder putIntArray(String key, int[] value) {
            this.mValues.put(key, Data.convertPrimitiveIntArray(value));
            return this;
        }

        public Builder putLong(String key, long value) {
            this.mValues.put(key, Long.valueOf(value));
            return this;
        }

        public Builder putLongArray(String key, long[] value) {
            this.mValues.put(key, Data.convertPrimitiveLongArray(value));
            return this;
        }

        public Builder putString(String key, String value) {
            this.mValues.put(key, value);
            return this;
        }

        public Builder putStringArray(String key, String[] value) {
            this.mValues.put(key, value);
            return this;
        }
    }

    static {
        String tagWithPrefix = Logger.tagWithPrefix("Data");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    Data() {
    }

    public Data(Data other) {
        this.mValues = new HashMap(other.mValues);
    }

    public Data(Map<String, ?> map) {
        this.mValues = new HashMap(map);
    }

    public static Boolean[] convertPrimitiveBooleanArray(boolean[] value) {
        Boolean[] boolArr = new Boolean[value.length];
        for (int i = 0; i < value.length; i++) {
            boolArr[i] = Boolean.valueOf(value[i]);
        }
        return boolArr;
    }

    public static Byte[] convertPrimitiveByteArray(byte[] value) {
        Byte[] bArr = new Byte[value.length];
        for (int i = 0; i < value.length; i++) {
            bArr[i] = Byte.valueOf(value[i]);
        }
        return bArr;
    }

    public static Double[] convertPrimitiveDoubleArray(double[] value) {
        Double[] dArr = new Double[value.length];
        for (int i = 0; i < value.length; i++) {
            dArr[i] = Double.valueOf(value[i]);
        }
        return dArr;
    }

    public static Float[] convertPrimitiveFloatArray(float[] value) {
        Float[] fArr = new Float[value.length];
        for (int i = 0; i < value.length; i++) {
            fArr[i] = Float.valueOf(value[i]);
        }
        return fArr;
    }

    public static Integer[] convertPrimitiveIntArray(int[] value) {
        Integer[] numArr = new Integer[value.length];
        for (int i = 0; i < value.length; i++) {
            numArr[i] = Integer.valueOf(value[i]);
        }
        return numArr;
    }

    public static Long[] convertPrimitiveLongArray(long[] value) {
        Long[] lArr = new Long[value.length];
        for (int i = 0; i < value.length; i++) {
            lArr[i] = Long.valueOf(value[i]);
        }
        return lArr;
    }

    public static byte[] convertToPrimitiveArray(Byte[] array) {
        byte[] bArr = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            bArr[i] = array[i].byteValue();
        }
        return bArr;
    }

    public static double[] convertToPrimitiveArray(Double[] array) {
        double[] dArr = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            dArr[i] = array[i].doubleValue();
        }
        return dArr;
    }

    public static float[] convertToPrimitiveArray(Float[] array) {
        float[] fArr = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            fArr[i] = array[i].floatValue();
        }
        return fArr;
    }

    public static int[] convertToPrimitiveArray(Integer[] array) {
        int[] iArr = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            iArr[i] = array[i].intValue();
        }
        return iArr;
    }

    public static long[] convertToPrimitiveArray(Long[] array) {
        long[] jArr = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            jArr[i] = array[i].longValue();
        }
        return jArr;
    }

    public static boolean[] convertToPrimitiveArray(Boolean[] array) {
        boolean[] zArr = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            zArr[i] = array[i].booleanValue();
        }
        return zArr;
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:12:0x0037=Splitter:B:12:0x0037, B:25:0x0059=Splitter:B:25:0x0059} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static androidx.work.Data fromByteArray(byte[] r7) {
        /*
            java.lang.String r0 = "Error in Data#fromByteArray: "
            int r1 = r7.length
            r2 = 10240(0x2800, float:1.4349E-41)
            if (r1 > r2) goto L_0x007a
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
            java.io.ByteArrayInputStream r2 = new java.io.ByteArrayInputStream
            r2.<init>(r7)
            r3 = 0
            java.io.ObjectInputStream r4 = new java.io.ObjectInputStream     // Catch:{ IOException -> 0x0046, ClassNotFoundException -> 0x0044 }
            r4.<init>(r2)     // Catch:{ IOException -> 0x0046, ClassNotFoundException -> 0x0044 }
            r3 = r4
            int r4 = r3.readInt()     // Catch:{ IOException -> 0x0046, ClassNotFoundException -> 0x0044 }
        L_0x001c:
            if (r4 <= 0) goto L_0x002c
            java.lang.String r5 = r3.readUTF()     // Catch:{ IOException -> 0x0046, ClassNotFoundException -> 0x0044 }
            java.lang.Object r6 = r3.readObject()     // Catch:{ IOException -> 0x0046, ClassNotFoundException -> 0x0044 }
            r1.put(r5, r6)     // Catch:{ IOException -> 0x0046, ClassNotFoundException -> 0x0044 }
            int r4 = r4 + -1
            goto L_0x001c
        L_0x002c:
            r3.close()     // Catch:{ IOException -> 0x0031 }
            goto L_0x0037
        L_0x0031:
            r4 = move-exception
            java.lang.String r5 = TAG
            android.util.Log.e(r5, r0, r4)
        L_0x0037:
            r2.close()     // Catch:{ IOException -> 0x003b }
        L_0x003a:
            goto L_0x005d
        L_0x003b:
            r4 = move-exception
            java.lang.String r5 = TAG
            android.util.Log.e(r5, r0, r4)
            goto L_0x005d
        L_0x0042:
            r4 = move-exception
            goto L_0x0063
        L_0x0044:
            r4 = move-exception
            goto L_0x0047
        L_0x0046:
            r4 = move-exception
        L_0x0047:
            java.lang.String r5 = TAG     // Catch:{ all -> 0x0042 }
            android.util.Log.e(r5, r0, r4)     // Catch:{ all -> 0x0042 }
            if (r3 == 0) goto L_0x0059
            r3.close()     // Catch:{ IOException -> 0x0053 }
            goto L_0x0059
        L_0x0053:
            r4 = move-exception
            java.lang.String r5 = TAG
            android.util.Log.e(r5, r0, r4)
        L_0x0059:
            r2.close()     // Catch:{ IOException -> 0x003b }
            goto L_0x003a
        L_0x005d:
            androidx.work.Data r0 = new androidx.work.Data
            r0.<init>((java.util.Map<java.lang.String, ?>) r1)
            return r0
        L_0x0063:
            if (r3 == 0) goto L_0x006f
            r3.close()     // Catch:{ IOException -> 0x0069 }
            goto L_0x006f
        L_0x0069:
            r5 = move-exception
            java.lang.String r6 = TAG
            android.util.Log.e(r6, r0, r5)
        L_0x006f:
            r2.close()     // Catch:{ IOException -> 0x0073 }
            goto L_0x0079
        L_0x0073:
            r5 = move-exception
            java.lang.String r6 = TAG
            android.util.Log.e(r6, r0, r5)
        L_0x0079:
            throw r4
        L_0x007a:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "Data cannot occupy more than 10240 bytes when serialized"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.work.Data.fromByteArray(byte[]):androidx.work.Data");
    }

    public static byte[] toByteArrayInternal(Data data) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream2.writeInt(data.size());
            for (Map.Entry next : data.mValues.entrySet()) {
                objectOutputStream2.writeUTF((String) next.getKey());
                objectOutputStream2.writeObject(next.getValue());
            }
            try {
                objectOutputStream2.close();
            } catch (IOException e) {
                Log.e(TAG, "Error in Data#toByteArray: ", e);
            }
            try {
                byteArrayOutputStream.close();
            } catch (IOException e2) {
                Log.e(TAG, "Error in Data#toByteArray: ", e2);
            }
            if (byteArrayOutputStream.size() <= 10240) {
                return byteArrayOutputStream.toByteArray();
            }
            throw new IllegalStateException("Data cannot occupy more than 10240 bytes when serialized");
        } catch (IOException e3) {
            Log.e(TAG, "Error in Data#toByteArray: ", e3);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e4) {
                    Log.e(TAG, "Error in Data#toByteArray: ", e4);
                }
            }
            try {
                byteArrayOutputStream.close();
            } catch (IOException e5) {
                Log.e(TAG, "Error in Data#toByteArray: ", e5);
            }
            return byteArray;
        } catch (Throwable th) {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e6) {
                    Log.e(TAG, "Error in Data#toByteArray: ", e6);
                }
            }
            try {
                byteArrayOutputStream.close();
            } catch (IOException e7) {
                Log.e(TAG, "Error in Data#toByteArray: ", e7);
            }
            throw th;
        }
    }

    public boolean equals(Object o) {
        boolean z;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Data data = (Data) o;
        Set<String> keySet = this.mValues.keySet();
        if (!keySet.equals(data.mValues.keySet())) {
            return false;
        }
        for (String next : keySet) {
            Object obj = this.mValues.get(next);
            Object obj2 = data.mValues.get(next);
            if (obj == null || obj2 == null) {
                if (obj == obj2) {
                    z = true;
                    continue;
                } else {
                    z = false;
                    continue;
                }
            } else if (!(obj instanceof Object[]) || !(obj2 instanceof Object[])) {
                z = obj.equals(obj2);
                continue;
            } else {
                z = Arrays.deepEquals((Object[]) obj, (Object[]) obj2);
                continue;
            }
            if (!z) {
                return false;
            }
        }
        return true;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Object obj = this.mValues.get(key);
        return obj instanceof Boolean ? ((Boolean) obj).booleanValue() : defaultValue;
    }

    public boolean[] getBooleanArray(String key) {
        Object obj = this.mValues.get(key);
        if (obj instanceof Boolean[]) {
            return convertToPrimitiveArray((Boolean[]) obj);
        }
        return null;
    }

    public byte getByte(String key, byte defaultValue) {
        Object obj = this.mValues.get(key);
        return obj instanceof Byte ? ((Byte) obj).byteValue() : defaultValue;
    }

    public byte[] getByteArray(String key) {
        Object obj = this.mValues.get(key);
        if (obj instanceof Byte[]) {
            return convertToPrimitiveArray((Byte[]) obj);
        }
        return null;
    }

    public double getDouble(String key, double defaultValue) {
        Object obj = this.mValues.get(key);
        return obj instanceof Double ? ((Double) obj).doubleValue() : defaultValue;
    }

    public double[] getDoubleArray(String key) {
        Object obj = this.mValues.get(key);
        if (obj instanceof Double[]) {
            return convertToPrimitiveArray((Double[]) obj);
        }
        return null;
    }

    public float getFloat(String key, float defaultValue) {
        Object obj = this.mValues.get(key);
        return obj instanceof Float ? ((Float) obj).floatValue() : defaultValue;
    }

    public float[] getFloatArray(String key) {
        Object obj = this.mValues.get(key);
        if (obj instanceof Float[]) {
            return convertToPrimitiveArray((Float[]) obj);
        }
        return null;
    }

    public int getInt(String key, int defaultValue) {
        Object obj = this.mValues.get(key);
        return obj instanceof Integer ? ((Integer) obj).intValue() : defaultValue;
    }

    public int[] getIntArray(String key) {
        Object obj = this.mValues.get(key);
        if (obj instanceof Integer[]) {
            return convertToPrimitiveArray((Integer[]) obj);
        }
        return null;
    }

    public Map<String, Object> getKeyValueMap() {
        return Collections.unmodifiableMap(this.mValues);
    }

    public long getLong(String key, long defaultValue) {
        Object obj = this.mValues.get(key);
        return obj instanceof Long ? ((Long) obj).longValue() : defaultValue;
    }

    public long[] getLongArray(String key) {
        Object obj = this.mValues.get(key);
        if (obj instanceof Long[]) {
            return convertToPrimitiveArray((Long[]) obj);
        }
        return null;
    }

    public String getString(String key) {
        Object obj = this.mValues.get(key);
        if (obj instanceof String) {
            return (String) obj;
        }
        return null;
    }

    public String[] getStringArray(String key) {
        Object obj = this.mValues.get(key);
        if (obj instanceof String[]) {
            return (String[]) obj;
        }
        return null;
    }

    public <T> boolean hasKeyWithValueOfType(String key, Class<T> cls) {
        Object obj = this.mValues.get(key);
        return obj != null && cls.isAssignableFrom(obj.getClass());
    }

    public int hashCode() {
        return this.mValues.hashCode() * 31;
    }

    public int size() {
        return this.mValues.size();
    }

    public byte[] toByteArray() {
        return toByteArrayInternal(this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Data {");
        if (!this.mValues.isEmpty()) {
            for (String next : this.mValues.keySet()) {
                sb.append(next).append(" : ");
                Object obj = this.mValues.get(next);
                if (obj instanceof Object[]) {
                    String arrays = Arrays.toString((Object[]) obj);
                    Log1F380D.a((Object) arrays);
                    sb.append(arrays);
                } else {
                    sb.append(obj);
                }
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
