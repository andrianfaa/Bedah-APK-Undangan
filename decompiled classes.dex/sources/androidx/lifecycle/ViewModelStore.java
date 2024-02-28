package androidx.lifecycle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ViewModelStore {
    private final HashMap<String, ViewModel> mMap = new HashMap<>();

    public final void clear() {
        for (ViewModel clear : this.mMap.values()) {
            clear.clear();
        }
        this.mMap.clear();
    }

    /* access modifiers changed from: package-private */
    public final ViewModel get(String key) {
        return this.mMap.get(key);
    }

    /* access modifiers changed from: package-private */
    public Set<String> keys() {
        return new HashSet(this.mMap.keySet());
    }

    /* access modifiers changed from: package-private */
    public final void put(String key, ViewModel viewModel) {
        ViewModel put = this.mMap.put(key, viewModel);
        if (put != null) {
            put.onCleared();
        }
    }
}
