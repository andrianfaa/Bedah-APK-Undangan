package androidx.constraintlayout.widget;

import android.util.SparseIntArray;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class SharedValues {
    public static final int UNSET = -1;
    private SparseIntArray mValues = new SparseIntArray();
    private HashMap<Integer, HashSet<WeakReference<SharedValuesListener>>> mValuesListeners = new HashMap<>();

    public interface SharedValuesListener {
        void onNewValue(int i, int i2, int i3);
    }

    public void addListener(int key, SharedValuesListener listener) {
        HashSet hashSet = this.mValuesListeners.get(Integer.valueOf(key));
        if (hashSet == null) {
            hashSet = new HashSet();
            this.mValuesListeners.put(Integer.valueOf(key), hashSet);
        }
        hashSet.add(new WeakReference(listener));
    }

    public void clearListeners() {
        this.mValuesListeners.clear();
    }

    public void fireNewValue(int key, int value) {
        boolean z = false;
        int i = this.mValues.get(key, -1);
        if (i != value) {
            this.mValues.put(key, value);
            HashSet hashSet = this.mValuesListeners.get(Integer.valueOf(key));
            if (hashSet != null) {
                Iterator it = hashSet.iterator();
                while (it.hasNext()) {
                    SharedValuesListener sharedValuesListener = (SharedValuesListener) ((WeakReference) it.next()).get();
                    if (sharedValuesListener != null) {
                        sharedValuesListener.onNewValue(key, value, i);
                    } else {
                        z = true;
                    }
                }
                if (z) {
                    ArrayList arrayList = new ArrayList();
                    Iterator it2 = hashSet.iterator();
                    while (it2.hasNext()) {
                        WeakReference weakReference = (WeakReference) it2.next();
                        if (((SharedValuesListener) weakReference.get()) == null) {
                            arrayList.add(weakReference);
                        }
                    }
                    hashSet.removeAll(arrayList);
                }
            }
        }
    }

    public int getValue(int key) {
        return this.mValues.get(key, -1);
    }

    public void removeListener(int key, SharedValuesListener listener) {
        HashSet hashSet = this.mValuesListeners.get(Integer.valueOf(key));
        if (hashSet != null) {
            ArrayList arrayList = new ArrayList();
            Iterator it = hashSet.iterator();
            while (it.hasNext()) {
                WeakReference weakReference = (WeakReference) it.next();
                SharedValuesListener sharedValuesListener = (SharedValuesListener) weakReference.get();
                if (sharedValuesListener == null || sharedValuesListener == listener) {
                    arrayList.add(weakReference);
                }
            }
            hashSet.removeAll(arrayList);
        }
    }

    public void removeListener(SharedValuesListener listener) {
        for (Integer intValue : this.mValuesListeners.keySet()) {
            removeListener(intValue.intValue(), listener);
        }
    }
}
