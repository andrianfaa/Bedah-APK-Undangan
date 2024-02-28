package androidx.constraintlayout.core.parser;

public class CLToken extends CLElement {
    int index = 0;
    char[] tokenFalse = "false".toCharArray();
    char[] tokenNull = "null".toCharArray();
    char[] tokenTrue = "true".toCharArray();
    Type type = Type.UNKNOWN;

    /* renamed from: androidx.constraintlayout.core.parser.CLToken$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$core$parser$CLToken$Type;

        static {
            int[] iArr = new int[Type.values().length];
            $SwitchMap$androidx$constraintlayout$core$parser$CLToken$Type = iArr;
            try {
                iArr[Type.TRUE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$parser$CLToken$Type[Type.FALSE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$parser$CLToken$Type[Type.NULL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$parser$CLToken$Type[Type.UNKNOWN.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    enum Type {
        UNKNOWN,
        TRUE,
        FALSE,
        NULL
    }

    public CLToken(char[] content) {
        super(content);
    }

    public static CLElement allocate(char[] content) {
        return new CLToken(content);
    }

    public boolean getBoolean() throws CLParsingException {
        if (this.type == Type.TRUE) {
            return true;
        }
        if (this.type == Type.FALSE) {
            return false;
        }
        throw new CLParsingException("this token is not a boolean: <" + content() + ">", this);
    }

    public Type getType() {
        return this.type;
    }

    public boolean isNull() throws CLParsingException {
        if (this.type == Type.NULL) {
            return true;
        }
        throw new CLParsingException("this token is not a null: <" + content() + ">", this);
    }

    /* access modifiers changed from: protected */
    public String toFormattedJSON(int indent, int forceIndent) {
        StringBuilder sb = new StringBuilder();
        addIndent(sb, indent);
        sb.append(content());
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String toJSON() {
        return CLParser.DEBUG ? "<" + content() + ">" : content();
    }

    public boolean validate(char c, long position) {
        boolean z = false;
        boolean z2 = false;
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$parser$CLToken$Type[this.type.ordinal()]) {
            case 1:
                char[] cArr = this.tokenTrue;
                int i = this.index;
                if (cArr[i] == c) {
                    z2 = true;
                }
                z = z2;
                if (z && i + 1 == cArr.length) {
                    setEnd(position);
                    break;
                }
            case 2:
                char[] cArr2 = this.tokenFalse;
                int i2 = this.index;
                if (cArr2[i2] == c) {
                    z2 = true;
                }
                z = z2;
                if (z && i2 + 1 == cArr2.length) {
                    setEnd(position);
                    break;
                }
            case 3:
                char[] cArr3 = this.tokenNull;
                int i3 = this.index;
                if (cArr3[i3] == c) {
                    z2 = true;
                }
                z = z2;
                if (z && i3 + 1 == cArr3.length) {
                    setEnd(position);
                    break;
                }
            case 4:
                char[] cArr4 = this.tokenTrue;
                int i4 = this.index;
                if (cArr4[i4] != c) {
                    if (this.tokenFalse[i4] != c) {
                        if (this.tokenNull[i4] == c) {
                            this.type = Type.NULL;
                            z = true;
                            break;
                        }
                    } else {
                        this.type = Type.FALSE;
                        z = true;
                        break;
                    }
                } else {
                    this.type = Type.TRUE;
                    z = true;
                    break;
                }
                break;
        }
        this.index++;
        return z;
    }
}
