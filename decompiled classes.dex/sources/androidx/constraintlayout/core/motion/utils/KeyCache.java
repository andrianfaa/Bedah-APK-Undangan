package androidx.constraintlayout.core.motion.utils;

import java.util.Arrays;
import java.util.HashMap;

public class KeyCache {
    HashMap<Object, HashMap<String, float[]>> map = new HashMap<>();

    public float getFloatValue(Object view, String type, int element) {
        HashMap hashMap;
        float[] fArr;
        if (this.map.containsKey(view) && (hashMap = this.map.get(view)) != null && hashMap.containsKey(type) && (fArr = (float[]) hashMap.get(type)) != null && fArr.length > element) {
            return fArr[element];
        }
        return Float.NaN;
    }

    public void setFloatValue(Object view, String type, int element, float value) {
        if (!this.map.containsKey(view)) {
            HashMap hashMap = new HashMap();
            float[] fArr = new float[(element + 1)];
            fArr[element] = value;
            hashMap.put(type, fArr);
            this.map.put(view, hashMap);
            return;
        }
        HashMap hashMap2 = this.map.get(view);
        if (hashMap2 == null) {
            hashMap2 = new HashMap();
        }
        if (!hashMap2.containsKey(type)) {
            float[] fArr2 = new float[(element + 1)];
            fArr2[element] = value;
            hashMap2.put(type, fArr2);
            this.map.put(view, hashMap2);
            return;
        }
        float[] fArr3 = (float[]) hashMap2.get(type);
        if (fArr3 == null) {
            fArr3 = new float[0];
        }
        if (fArr3.length <= element) {
            fArr3 = Arrays.copyOf(fArr3, element + 1);
        }
        fArr3[element] = value;
        hashMap2.put(type, fArr3);
    }
}
