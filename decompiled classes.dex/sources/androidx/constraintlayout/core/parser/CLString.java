package androidx.constraintlayout.core.parser;

public class CLString extends CLElement {
    public CLString(char[] content) {
        super(content);
    }

    public static CLElement allocate(char[] content) {
        return new CLString(content);
    }

    /* access modifiers changed from: protected */
    public String toFormattedJSON(int indent, int forceIndent) {
        StringBuilder sb = new StringBuilder();
        addIndent(sb, indent);
        sb.append("'");
        sb.append(content());
        sb.append("'");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String toJSON() {
        return "'" + content() + "'";
    }
}
