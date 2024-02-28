package androidx.constraintlayout.core.parser;

import java.util.Iterator;

public class CLObject extends CLContainer implements Iterable<CLKey> {

    private class CLObjectIterator implements Iterator {
        int index = 0;
        CLObject myObject;

        public CLObjectIterator(CLObject clObject) {
            this.myObject = clObject;
        }

        public boolean hasNext() {
            return this.index < this.myObject.size();
        }

        public Object next() {
            CLKey cLKey = (CLKey) this.myObject.mElements.get(this.index);
            this.index++;
            return cLKey;
        }
    }

    public CLObject(char[] content) {
        super(content);
    }

    public static CLObject allocate(char[] content) {
        return new CLObject(content);
    }

    public Iterator iterator() {
        return new CLObjectIterator(this);
    }

    public String toFormattedJSON() {
        return toFormattedJSON(0, 0);
    }

    public String toFormattedJSON(int indent, int forceIndent) {
        StringBuilder sb = new StringBuilder(getDebugName());
        sb.append("{\n");
        boolean z = true;
        Iterator it = this.mElements.iterator();
        while (it.hasNext()) {
            CLElement cLElement = (CLElement) it.next();
            if (!z) {
                sb.append(",\n");
            } else {
                z = false;
            }
            sb.append(cLElement.toFormattedJSON(BASE_INDENT + indent, forceIndent - 1));
        }
        sb.append("\n");
        addIndent(sb, indent);
        sb.append("}");
        return sb.toString();
    }

    public String toJSON() {
        StringBuilder sb = new StringBuilder(getDebugName() + "{ ");
        boolean z = true;
        Iterator it = this.mElements.iterator();
        while (it.hasNext()) {
            CLElement cLElement = (CLElement) it.next();
            if (!z) {
                sb.append(", ");
            } else {
                z = false;
            }
            sb.append(cLElement.toJSON());
        }
        sb.append(" }");
        return sb.toString();
    }
}
