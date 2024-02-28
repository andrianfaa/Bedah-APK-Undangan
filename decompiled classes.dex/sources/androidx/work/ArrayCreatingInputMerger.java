package androidx.work;

import androidx.work.Data;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class ArrayCreatingInputMerger extends InputMerger {
    private Object concatenateArrayAndNonArray(Object array, Object obj) {
        int length = Array.getLength(array);
        Object newInstance = Array.newInstance(obj.getClass(), length + 1);
        System.arraycopy(array, 0, newInstance, 0, length);
        Array.set(newInstance, length, obj);
        return newInstance;
    }

    private Object concatenateArrays(Object array1, Object array2) {
        int length = Array.getLength(array1);
        int length2 = Array.getLength(array2);
        Object newInstance = Array.newInstance(array1.getClass().getComponentType(), length + length2);
        System.arraycopy(array1, 0, newInstance, 0, length);
        System.arraycopy(array2, 0, newInstance, length, length2);
        return newInstance;
    }

    private Object concatenateNonArrays(Object obj1, Object obj2) {
        Object newInstance = Array.newInstance(obj1.getClass(), 2);
        Array.set(newInstance, 0, obj1);
        Array.set(newInstance, 1, obj2);
        return newInstance;
    }

    private Object createArrayFor(Object obj) {
        Object newInstance = Array.newInstance(obj.getClass(), 1);
        Array.set(newInstance, 0, obj);
        return newInstance;
    }

    public Data merge(List<Data> list) {
        Object obj;
        Data.Builder builder = new Data.Builder();
        HashMap hashMap = new HashMap();
        loop0:
        for (Data keyValueMap : list) {
            Iterator<Map.Entry<String, Object>> it = keyValueMap.getKeyValueMap().entrySet().iterator();
            while (true) {
                if (it.hasNext()) {
                    Map.Entry next = it.next();
                    String str = (String) next.getKey();
                    Object value = next.getValue();
                    Class<?> cls = value.getClass();
                    Object obj2 = hashMap.get(str);
                    if (obj2 == null) {
                        obj = cls.isArray() ? value : createArrayFor(value);
                    } else {
                        Class<?> cls2 = obj2.getClass();
                        if (cls2.equals(cls)) {
                            obj = cls2.isArray() ? concatenateArrays(obj2, value) : concatenateNonArrays(obj2, value);
                        } else if (cls2.isArray() && cls2.getComponentType().equals(cls)) {
                            obj = concatenateArrayAndNonArray(obj2, value);
                        } else if (cls.isArray() && cls.getComponentType().equals(cls2)) {
                            obj = concatenateArrayAndNonArray(value, obj2);
                        }
                    }
                    hashMap.put(str, obj);
                }
            }
            throw new IllegalArgumentException();
        }
        builder.putAll((Map<String, Object>) hashMap);
        return builder.build();
    }
}
