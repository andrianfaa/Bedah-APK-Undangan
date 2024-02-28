package com.google.android.material.internal;

import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.internal.MaterialCheckable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CheckableGroup<T extends MaterialCheckable<T>> {
    private final Map<Integer, T> checkables = new HashMap();
    private final Set<Integer> checkedIds = new HashSet();
    private OnCheckedStateChangeListener onCheckedStateChangeListener;
    /* access modifiers changed from: private */
    public boolean selectionRequired;
    private boolean singleSelection;

    public interface OnCheckedStateChangeListener {
        void onCheckedStateChanged(Set<Integer> set);
    }

    /* access modifiers changed from: private */
    public boolean checkInternal(MaterialCheckable<T> materialCheckable) {
        int id = materialCheckable.getId();
        if (this.checkedIds.contains(Integer.valueOf(id))) {
            return false;
        }
        MaterialCheckable materialCheckable2 = (MaterialCheckable) this.checkables.get(Integer.valueOf(getSingleCheckedId()));
        if (materialCheckable2 != null) {
            uncheckInternal(materialCheckable2, false);
        }
        boolean add = this.checkedIds.add(Integer.valueOf(id));
        if (!materialCheckable.isChecked()) {
            materialCheckable.setChecked(true);
        }
        return add;
    }

    /* access modifiers changed from: private */
    public void onCheckedStateChanged() {
        OnCheckedStateChangeListener onCheckedStateChangeListener2 = this.onCheckedStateChangeListener;
        if (onCheckedStateChangeListener2 != null) {
            onCheckedStateChangeListener2.onCheckedStateChanged(getCheckedIds());
        }
    }

    /* access modifiers changed from: private */
    public boolean uncheckInternal(MaterialCheckable<T> materialCheckable, boolean selectionRequired2) {
        int id = materialCheckable.getId();
        if (!this.checkedIds.contains(Integer.valueOf(id))) {
            return false;
        }
        if (!selectionRequired2 || this.checkedIds.size() != 1 || !this.checkedIds.contains(Integer.valueOf(id))) {
            boolean remove = this.checkedIds.remove(Integer.valueOf(id));
            if (materialCheckable.isChecked()) {
                materialCheckable.setChecked(false);
            }
            return remove;
        }
        materialCheckable.setChecked(true);
        return false;
    }

    public void addCheckable(T t) {
        this.checkables.put(Integer.valueOf(t.getId()), t);
        if (t.isChecked()) {
            checkInternal(t);
        }
        t.setInternalOnCheckedChangeListener(new MaterialCheckable.OnCheckedChangeListener<T>() {
            public void onCheckedChanged(T t, boolean isChecked) {
                CheckableGroup checkableGroup = CheckableGroup.this;
                if (isChecked) {
                    if (!checkableGroup.checkInternal(t)) {
                        return;
                    }
                } else if (!checkableGroup.uncheckInternal(t, checkableGroup.selectionRequired)) {
                    return;
                }
                CheckableGroup.this.onCheckedStateChanged();
            }
        });
    }

    public void check(int id) {
        MaterialCheckable materialCheckable = (MaterialCheckable) this.checkables.get(Integer.valueOf(id));
        if (materialCheckable != null && checkInternal(materialCheckable)) {
            onCheckedStateChanged();
        }
    }

    public void clearCheck() {
        boolean z = !this.checkedIds.isEmpty();
        for (T uncheckInternal : this.checkables.values()) {
            uncheckInternal(uncheckInternal, false);
        }
        if (z) {
            onCheckedStateChanged();
        }
    }

    public Set<Integer> getCheckedIds() {
        return new HashSet(this.checkedIds);
    }

    public List<Integer> getCheckedIdsSortedByChildOrder(ViewGroup parent) {
        Set<Integer> checkedIds2 = getCheckedIds();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View childAt = parent.getChildAt(i);
            if ((childAt instanceof MaterialCheckable) && checkedIds2.contains(Integer.valueOf(childAt.getId()))) {
                arrayList.add(Integer.valueOf(childAt.getId()));
            }
        }
        return arrayList;
    }

    public int getSingleCheckedId() {
        if (!this.singleSelection || this.checkedIds.isEmpty()) {
            return -1;
        }
        return this.checkedIds.iterator().next().intValue();
    }

    public boolean isSelectionRequired() {
        return this.selectionRequired;
    }

    public boolean isSingleSelection() {
        return this.singleSelection;
    }

    public void removeCheckable(T t) {
        t.setInternalOnCheckedChangeListener((MaterialCheckable.OnCheckedChangeListener) null);
        this.checkables.remove(Integer.valueOf(t.getId()));
        this.checkedIds.remove(Integer.valueOf(t.getId()));
    }

    public void setOnCheckedStateChangeListener(OnCheckedStateChangeListener listener) {
        this.onCheckedStateChangeListener = listener;
    }

    public void setSelectionRequired(boolean selectionRequired2) {
        this.selectionRequired = selectionRequired2;
    }

    public void setSingleSelection(boolean singleSelection2) {
        if (this.singleSelection != singleSelection2) {
            this.singleSelection = singleSelection2;
            clearCheck();
        }
    }

    public void uncheck(int id) {
        MaterialCheckable materialCheckable = (MaterialCheckable) this.checkables.get(Integer.valueOf(id));
        if (materialCheckable != null && uncheckInternal(materialCheckable, this.selectionRequired)) {
            onCheckedStateChanged();
        }
    }
}
