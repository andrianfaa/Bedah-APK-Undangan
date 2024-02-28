package androidx.constraintlayout.core.parser;

import okhttp3.HttpUrl;

public class CLNumber extends CLElement {
    float value = Float.NaN;

    public CLNumber(float value2) {
        super((char[]) null);
        this.value = value2;
    }

    public CLNumber(char[] content) {
        super(content);
    }

    public static CLElement allocate(char[] content) {
        return new CLNumber(content);
    }

    public float getFloat() {
        if (Float.isNaN(this.value)) {
            this.value = Float.parseFloat(content());
        }
        return this.value;
    }

    public int getInt() {
        if (Float.isNaN(this.value)) {
            this.value = (float) Integer.parseInt(content());
        }
        return (int) this.value;
    }

    public boolean isInt() {
        float f = getFloat();
        return ((float) ((int) f)) == f;
    }

    public void putValue(float value2) {
        this.value = value2;
    }

    /* access modifiers changed from: protected */
    public String toFormattedJSON(int indent, int forceIndent) {
        StringBuilder sb = new StringBuilder();
        addIndent(sb, indent);
        float f = getFloat();
        int i = (int) f;
        if (((float) i) == f) {
            sb.append(i);
        } else {
            sb.append(f);
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String toJSON() {
        float f = getFloat();
        int i = (int) f;
        return ((float) i) == f ? HttpUrl.FRAGMENT_ENCODE_SET + i : HttpUrl.FRAGMENT_ENCODE_SET + f;
    }
}
