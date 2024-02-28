package androidx.core.text;

import android.text.SpannableStringBuilder;
import java.util.Locale;
import kotlin.text.Typography;
import mt.Log1F380D;

/* compiled from: 0052 */
public final class BidiFormatter {
    private static final int DEFAULT_FLAGS = 2;
    static final BidiFormatter DEFAULT_LTR_INSTANCE;
    static final BidiFormatter DEFAULT_RTL_INSTANCE;
    static final TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC;
    private static final int DIR_LTR = -1;
    private static final int DIR_RTL = 1;
    private static final int DIR_UNKNOWN = 0;
    private static final String EMPTY_STRING = "";
    private static final int FLAG_STEREO_RESET = 2;
    private static final char LRE = '‪';
    private static final char LRM = '‎';
    private static final String LRM_STRING;
    private static final char PDF = '‬';
    private static final char RLE = '‫';
    private static final char RLM = '‏';
    private static final String RLM_STRING;
    private final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
    private final int mFlags;
    private final boolean mIsRtlContext;

    public static final class Builder {
        private int mFlags;
        private boolean mIsRtlContext;
        private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;

        public Builder() {
            initialize(BidiFormatter.isRtlLocale(Locale.getDefault()));
        }

        public Builder(Locale locale) {
            initialize(BidiFormatter.isRtlLocale(locale));
        }

        public Builder(boolean rtlContext) {
            initialize(rtlContext);
        }

        private static BidiFormatter getDefaultInstanceFromContext(boolean isRtlContext) {
            return isRtlContext ? BidiFormatter.DEFAULT_RTL_INSTANCE : BidiFormatter.DEFAULT_LTR_INSTANCE;
        }

        private void initialize(boolean isRtlContext) {
            this.mIsRtlContext = isRtlContext;
            this.mTextDirectionHeuristicCompat = BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC;
            this.mFlags = 2;
        }

        public BidiFormatter build() {
            return (this.mFlags == 2 && this.mTextDirectionHeuristicCompat == BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC) ? getDefaultInstanceFromContext(this.mIsRtlContext) : new BidiFormatter(this.mIsRtlContext, this.mFlags, this.mTextDirectionHeuristicCompat);
        }

        public Builder setTextDirectionHeuristic(TextDirectionHeuristicCompat heuristic) {
            this.mTextDirectionHeuristicCompat = heuristic;
            return this;
        }

        public Builder stereoReset(boolean stereoReset) {
            if (stereoReset) {
                this.mFlags |= 2;
            } else {
                this.mFlags &= -3;
            }
            return this;
        }
    }

    private static class DirectionalityEstimator {
        private static final byte[] DIR_TYPE_CACHE = new byte[DIR_TYPE_CACHE_SIZE];
        private static final int DIR_TYPE_CACHE_SIZE = 1792;
        private int charIndex;
        private final boolean isHtml;
        private char lastChar;
        private final int length;
        private final CharSequence text;

        static {
            for (int i = 0; i < DIR_TYPE_CACHE_SIZE; i++) {
                DIR_TYPE_CACHE[i] = Character.getDirectionality(i);
            }
        }

        DirectionalityEstimator(CharSequence text2, boolean isHtml2) {
            this.text = text2;
            this.isHtml = isHtml2;
            this.length = text2.length();
        }

        private static byte getCachedDirectionality(char c) {
            return c < DIR_TYPE_CACHE_SIZE ? DIR_TYPE_CACHE[c] : Character.getDirectionality(c);
        }

        private byte skipEntityBackward() {
            char charAt;
            int i = this.charIndex;
            do {
                int i2 = this.charIndex;
                if (i2 <= 0) {
                    break;
                }
                CharSequence charSequence = this.text;
                int i3 = i2 - 1;
                this.charIndex = i3;
                charAt = charSequence.charAt(i3);
                this.lastChar = charAt;
                if (charAt == '&') {
                    return 12;
                }
            } while (charAt != ';');
            this.charIndex = i;
            this.lastChar = ';';
            return 13;
        }

        private byte skipEntityForward() {
            char charAt;
            do {
                int i = this.charIndex;
                if (i >= this.length) {
                    return 12;
                }
                CharSequence charSequence = this.text;
                this.charIndex = i + 1;
                charAt = charSequence.charAt(i);
                this.lastChar = charAt;
            } while (charAt != ';');
            return 12;
        }

        private byte skipTagBackward() {
            char charAt;
            int i = this.charIndex;
            while (true) {
                int i2 = this.charIndex;
                if (i2 <= 0) {
                    break;
                }
                CharSequence charSequence = this.text;
                int i3 = i2 - 1;
                this.charIndex = i3;
                char charAt2 = charSequence.charAt(i3);
                this.lastChar = charAt2;
                if (charAt2 == '<') {
                    return 12;
                }
                if (charAt2 == '>') {
                    break;
                } else if (charAt2 == '\"' || charAt2 == '\'') {
                    char c = this.lastChar;
                    do {
                        int i4 = this.charIndex;
                        if (i4 <= 0) {
                            break;
                        }
                        CharSequence charSequence2 = this.text;
                        int i5 = i4 - 1;
                        this.charIndex = i5;
                        charAt = charSequence2.charAt(i5);
                        this.lastChar = charAt;
                    } while (charAt != c);
                }
            }
            this.charIndex = i;
            this.lastChar = Typography.greater;
            return 13;
        }

        private byte skipTagForward() {
            char charAt;
            int i = this.charIndex;
            while (true) {
                int i2 = this.charIndex;
                if (i2 < this.length) {
                    CharSequence charSequence = this.text;
                    this.charIndex = i2 + 1;
                    char charAt2 = charSequence.charAt(i2);
                    this.lastChar = charAt2;
                    if (charAt2 == '>') {
                        return 12;
                    }
                    if (charAt2 == '\"' || charAt2 == '\'') {
                        char c = this.lastChar;
                        do {
                            int i3 = this.charIndex;
                            if (i3 >= this.length) {
                                break;
                            }
                            CharSequence charSequence2 = this.text;
                            this.charIndex = i3 + 1;
                            charAt = charSequence2.charAt(i3);
                            this.lastChar = charAt;
                        } while (charAt != c);
                    }
                } else {
                    this.charIndex = i;
                    this.lastChar = Typography.less;
                    return 13;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public byte dirTypeBackward() {
            char charAt = this.text.charAt(this.charIndex - 1);
            this.lastChar = charAt;
            if (Character.isLowSurrogate(charAt)) {
                int codePointBefore = Character.codePointBefore(this.text, this.charIndex);
                this.charIndex -= Character.charCount(codePointBefore);
                return Character.getDirectionality(codePointBefore);
            }
            this.charIndex--;
            byte cachedDirectionality = getCachedDirectionality(this.lastChar);
            if (!this.isHtml) {
                return cachedDirectionality;
            }
            char c = this.lastChar;
            return c == '>' ? skipTagBackward() : c == ';' ? skipEntityBackward() : cachedDirectionality;
        }

        /* access modifiers changed from: package-private */
        public byte dirTypeForward() {
            char charAt = this.text.charAt(this.charIndex);
            this.lastChar = charAt;
            if (Character.isHighSurrogate(charAt)) {
                int codePointAt = Character.codePointAt(this.text, this.charIndex);
                this.charIndex += Character.charCount(codePointAt);
                return Character.getDirectionality(codePointAt);
            }
            this.charIndex++;
            byte cachedDirectionality = getCachedDirectionality(this.lastChar);
            if (!this.isHtml) {
                return cachedDirectionality;
            }
            char c = this.lastChar;
            return c == '<' ? skipTagForward() : c == '&' ? skipEntityForward() : cachedDirectionality;
        }

        /* access modifiers changed from: package-private */
        public int getEntryDir() {
            this.charIndex = 0;
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            while (this.charIndex < this.length && i3 == 0) {
                switch (dirTypeForward()) {
                    case 0:
                        if (i != 0) {
                            i3 = i;
                            break;
                        } else {
                            return -1;
                        }
                    case 1:
                    case 2:
                        if (i != 0) {
                            i3 = i;
                            break;
                        } else {
                            return 1;
                        }
                    case 9:
                        break;
                    case 14:
                    case 15:
                        i++;
                        i2 = -1;
                        break;
                    case 16:
                    case 17:
                        i++;
                        i2 = 1;
                        break;
                    case 18:
                        i--;
                        i2 = 0;
                        break;
                    default:
                        i3 = i;
                        break;
                }
            }
            if (i3 == 0) {
                return 0;
            }
            if (i2 != 0) {
                return i2;
            }
            while (this.charIndex > 0) {
                switch (dirTypeBackward()) {
                    case 14:
                    case 15:
                        if (i3 != i) {
                            i--;
                            break;
                        } else {
                            return -1;
                        }
                    case 16:
                    case 17:
                        if (i3 != i) {
                            i--;
                            break;
                        } else {
                            return 1;
                        }
                    case 18:
                        i++;
                        break;
                }
            }
            return 0;
        }

        /* access modifiers changed from: package-private */
        public int getExitDir() {
            this.charIndex = this.length;
            int i = 0;
            int i2 = 0;
            while (this.charIndex > 0) {
                switch (dirTypeBackward()) {
                    case 0:
                        if (i != 0) {
                            if (i2 != 0) {
                                break;
                            } else {
                                i2 = i;
                                break;
                            }
                        } else {
                            return -1;
                        }
                    case 1:
                    case 2:
                        if (i != 0) {
                            if (i2 != 0) {
                                break;
                            } else {
                                i2 = i;
                                break;
                            }
                        } else {
                            return 1;
                        }
                    case 9:
                        break;
                    case 14:
                    case 15:
                        if (i2 != i) {
                            i--;
                            break;
                        } else {
                            return -1;
                        }
                    case 16:
                    case 17:
                        if (i2 != i) {
                            i--;
                            break;
                        } else {
                            return 1;
                        }
                    case 18:
                        i++;
                        break;
                    default:
                        if (i2 != 0) {
                            break;
                        } else {
                            i2 = i;
                            break;
                        }
                }
            }
            return 0;
        }
    }

    static {
        TextDirectionHeuristicCompat textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
        DEFAULT_TEXT_DIRECTION_HEURISTIC = textDirectionHeuristicCompat;
        String ch = Character.toString(LRM);
        Log1F380D.a((Object) ch);
        LRM_STRING = ch;
        String ch2 = Character.toString(RLM);
        Log1F380D.a((Object) ch2);
        RLM_STRING = ch2;
        DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, textDirectionHeuristicCompat);
        DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, textDirectionHeuristicCompat);
    }

    BidiFormatter(boolean isRtlContext, int flags, TextDirectionHeuristicCompat heuristic) {
        this.mIsRtlContext = isRtlContext;
        this.mFlags = flags;
        this.mDefaultTextDirectionHeuristicCompat = heuristic;
    }

    private static int getEntryDir(CharSequence str) {
        return new DirectionalityEstimator(str, false).getEntryDir();
    }

    private static int getExitDir(CharSequence str) {
        return new DirectionalityEstimator(str, false).getExitDir();
    }

    public static BidiFormatter getInstance() {
        return new Builder().build();
    }

    public static BidiFormatter getInstance(Locale locale) {
        return new Builder(locale).build();
    }

    public static BidiFormatter getInstance(boolean rtlContext) {
        return new Builder(rtlContext).build();
    }

    static boolean isRtlLocale(Locale locale) {
        return TextUtilsCompat.getLayoutDirectionFromLocale(locale) == 1;
    }

    private String markAfter(CharSequence str, TextDirectionHeuristicCompat heuristic) {
        boolean isRtl = heuristic.isRtl(str, 0, str.length());
        return (this.mIsRtlContext || (!isRtl && getExitDir(str) != 1)) ? this.mIsRtlContext ? (!isRtl || getExitDir(str) == -1) ? RLM_STRING : "" : "" : LRM_STRING;
    }

    private String markBefore(CharSequence str, TextDirectionHeuristicCompat heuristic) {
        boolean isRtl = heuristic.isRtl(str, 0, str.length());
        return (this.mIsRtlContext || (!isRtl && getEntryDir(str) != 1)) ? this.mIsRtlContext ? (!isRtl || getEntryDir(str) == -1) ? RLM_STRING : "" : "" : LRM_STRING;
    }

    public boolean getStereoReset() {
        return (this.mFlags & 2) != 0;
    }

    public boolean isRtl(CharSequence str) {
        return this.mDefaultTextDirectionHeuristicCompat.isRtl(str, 0, str.length());
    }

    public boolean isRtl(String str) {
        return isRtl((CharSequence) str);
    }

    public boolean isRtlContext() {
        return this.mIsRtlContext;
    }

    public CharSequence unicodeWrap(CharSequence str) {
        return unicodeWrap(str, this.mDefaultTextDirectionHeuristicCompat, true);
    }

    public CharSequence unicodeWrap(CharSequence str, TextDirectionHeuristicCompat heuristic) {
        return unicodeWrap(str, heuristic, true);
    }

    public CharSequence unicodeWrap(CharSequence str, TextDirectionHeuristicCompat heuristic, boolean isolate) {
        if (str == null) {
            return null;
        }
        boolean isRtl = heuristic.isRtl(str, 0, str.length());
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (getStereoReset() && isolate) {
            spannableStringBuilder.append(markBefore(str, isRtl ? TextDirectionHeuristicsCompat.RTL : TextDirectionHeuristicsCompat.LTR));
        }
        if (isRtl != this.mIsRtlContext) {
            spannableStringBuilder.append(isRtl ? RLE : LRE);
            spannableStringBuilder.append(str);
            spannableStringBuilder.append(PDF);
        } else {
            spannableStringBuilder.append(str);
        }
        if (isolate) {
            spannableStringBuilder.append(markAfter(str, isRtl ? TextDirectionHeuristicsCompat.RTL : TextDirectionHeuristicsCompat.LTR));
        }
        return spannableStringBuilder;
    }

    public CharSequence unicodeWrap(CharSequence str, boolean isolate) {
        return unicodeWrap(str, this.mDefaultTextDirectionHeuristicCompat, isolate);
    }

    public String unicodeWrap(String str) {
        return unicodeWrap(str, this.mDefaultTextDirectionHeuristicCompat, true);
    }

    public String unicodeWrap(String str, TextDirectionHeuristicCompat heuristic) {
        return unicodeWrap(str, heuristic, true);
    }

    public String unicodeWrap(String str, TextDirectionHeuristicCompat heuristic, boolean isolate) {
        if (str == null) {
            return null;
        }
        return unicodeWrap((CharSequence) str, heuristic, isolate).toString();
    }

    public String unicodeWrap(String str, boolean isolate) {
        return unicodeWrap(str, this.mDefaultTextDirectionHeuristicCompat, isolate);
    }
}
