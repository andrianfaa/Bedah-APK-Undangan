package androidx.constraintlayout.core.parser;

import mt.Log1F380D;
import okhttp3.HttpUrl;

/* compiled from: 0015 */
public class CLElement {
    protected static int BASE_INDENT = 2;
    protected static int MAX_LINE = 80;
    protected long end = Long.MAX_VALUE;
    private int line;
    protected CLContainer mContainer;
    private final char[] mContent;
    protected long start = -1;

    public CLElement(char[] content) {
        this.mContent = content;
    }

    /* access modifiers changed from: protected */
    public void addIndent(StringBuilder builder, int indent) {
        for (int i = 0; i < indent; i++) {
            builder.append(' ');
        }
    }

    public String content() {
        String str = new String(this.mContent);
        Log1F380D.a((Object) str);
        long j = this.end;
        if (j != Long.MAX_VALUE) {
            long j2 = this.start;
            if (j >= j2) {
                return str.substring((int) j2, ((int) j) + 1);
            }
        }
        long j3 = this.start;
        return str.substring((int) j3, ((int) j3) + 1);
    }

    public CLElement getContainer() {
        return this.mContainer;
    }

    /* access modifiers changed from: protected */
    public String getDebugName() {
        return CLParser.DEBUG ? getStrClass() + " -> " : HttpUrl.FRAGMENT_ENCODE_SET;
    }

    public long getEnd() {
        return this.end;
    }

    public float getFloat() {
        if (this instanceof CLNumber) {
            return ((CLNumber) this).getFloat();
        }
        return Float.NaN;
    }

    public int getInt() {
        if (this instanceof CLNumber) {
            return ((CLNumber) this).getInt();
        }
        return 0;
    }

    public int getLine() {
        return this.line;
    }

    public long getStart() {
        return this.start;
    }

    /* access modifiers changed from: protected */
    public String getStrClass() {
        String cls = getClass().toString();
        return cls.substring(cls.lastIndexOf(46) + 1);
    }

    public boolean isDone() {
        return this.end != Long.MAX_VALUE;
    }

    public boolean isStarted() {
        return this.start > -1;
    }

    public boolean notStarted() {
        return this.start == -1;
    }

    public void setContainer(CLContainer element) {
        this.mContainer = element;
    }

    public void setEnd(long end2) {
        if (this.end == Long.MAX_VALUE) {
            this.end = end2;
            if (CLParser.DEBUG) {
                System.out.println("closing " + hashCode() + " -> " + this);
            }
            CLContainer cLContainer = this.mContainer;
            if (cLContainer != null) {
                cLContainer.add(this);
            }
        }
    }

    public void setLine(int line2) {
        this.line = line2;
    }

    public void setStart(long start2) {
        this.start = start2;
    }

    /* access modifiers changed from: protected */
    public String toFormattedJSON(int indent, int forceIndent) {
        return HttpUrl.FRAGMENT_ENCODE_SET;
    }

    /* access modifiers changed from: protected */
    public String toJSON() {
        return HttpUrl.FRAGMENT_ENCODE_SET;
    }

    public String toString() {
        long j = this.start;
        long j2 = this.end;
        if (j > j2 || j2 == Long.MAX_VALUE) {
            return getClass() + " (INVALID, " + this.start + "-" + this.end + ")";
        }
        String str = new String(this.mContent);
        Log1F380D.a((Object) str);
        return getStrClass() + " (" + this.start + " : " + this.end + ") <<" + str.substring((int) this.start, ((int) this.end) + 1) + ">>";
    }
}
