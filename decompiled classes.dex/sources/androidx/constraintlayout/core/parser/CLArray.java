package androidx.constraintlayout.core.parser;

import java.util.Iterator;

public class CLArray extends CLContainer {
    public CLArray(char[] content) {
        super(content);
    }

    public static CLElement allocate(char[] content) {
        return new CLArray(content);
    }

    /* access modifiers changed from: protected */
    public String toFormattedJSON(int indent, int forceIndent) {
        StringBuilder sb = new StringBuilder();
        String json = toJSON();
        if (forceIndent > 0 || json.length() + indent >= MAX_LINE) {
            sb.append("[\n");
            boolean z = true;
            Iterator it = this.mElements.iterator();
            while (it.hasNext()) {
                CLElement cLElement = (CLElement) it.next();
                if (!z) {
                    sb.append(",\n");
                } else {
                    z = false;
                }
                addIndent(sb, BASE_INDENT + indent);
                sb.append(cLElement.toFormattedJSON(BASE_INDENT + indent, forceIndent - 1));
            }
            sb.append("\n");
            addIndent(sb, indent);
            sb.append("]");
        } else {
            sb.append(json);
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String toJSON() {
        StringBuilder sb = new StringBuilder(getDebugName() + "[");
        boolean z = true;
        for (int i = 0; i < this.mElements.size(); i++) {
            if (!z) {
                sb.append(", ");
            } else {
                z = false;
            }
            sb.append(((CLElement) this.mElements.get(i)).toJSON());
        }
        return sb + "]";
    }
}
