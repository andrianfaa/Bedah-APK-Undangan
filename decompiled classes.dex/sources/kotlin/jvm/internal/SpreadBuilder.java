package kotlin.jvm.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class SpreadBuilder {
    private final ArrayList<Object> list;

    public SpreadBuilder(int size) {
        this.list = new ArrayList<>(size);
    }

    public void add(Object element) {
        this.list.add(element);
    }

    public void addSpread(Object container) {
        if (container != null) {
            if (container instanceof Object[]) {
                Object[] objArr = (Object[]) container;
                if (objArr.length > 0) {
                    ArrayList<Object> arrayList = this.list;
                    arrayList.ensureCapacity(arrayList.size() + objArr.length);
                    Collections.addAll(this.list, objArr);
                }
            } else if (container instanceof Collection) {
                this.list.addAll((Collection) container);
            } else if (container instanceof Iterable) {
                for (Object add : (Iterable) container) {
                    this.list.add(add);
                }
            } else if (container instanceof Iterator) {
                Iterator it = (Iterator) container;
                while (it.hasNext()) {
                    this.list.add(it.next());
                }
            } else {
                throw new UnsupportedOperationException("Don't know how to spread " + container.getClass());
            }
        }
    }

    public int size() {
        return this.list.size();
    }

    public Object[] toArray(Object[] a) {
        return this.list.toArray(a);
    }
}
