package androidx.core.view.inputmethod;

import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import androidx.core.util.Preconditions;
import mt.Log1F380D;

/* compiled from: 0066 */
public final class EditorInfoCompat {
    private static final String CONTENT_MIME_TYPES_INTEROP_KEY = "android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES";
    private static final String CONTENT_MIME_TYPES_KEY = "androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES";
    private static final String CONTENT_SELECTION_END_KEY = "androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SELECTION_END";
    private static final String CONTENT_SELECTION_HEAD_KEY = "androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SELECTION_HEAD";
    private static final String CONTENT_SURROUNDING_TEXT_KEY = "androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SURROUNDING_TEXT";
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final int IME_FLAG_FORCE_ASCII = Integer.MIN_VALUE;
    public static final int IME_FLAG_NO_PERSONALIZED_LEARNING = 16777216;
    static final int MAX_INITIAL_SELECTION_LENGTH = 1024;
    static final int MEMORY_EFFICIENT_TEXT_LENGTH = 2048;

    private static class Api30Impl {
        private Api30Impl() {
        }

        static CharSequence getInitialSelectedText(EditorInfo editorInfo, int flags) {
            return editorInfo.getInitialSelectedText(flags);
        }

        static CharSequence getInitialTextAfterCursor(EditorInfo editorInfo, int length, int flags) {
            return editorInfo.getInitialTextAfterCursor(length, flags);
        }

        static CharSequence getInitialTextBeforeCursor(EditorInfo editorInfo, int length, int flags) {
            return editorInfo.getInitialTextBeforeCursor(length, flags);
        }

        static void setInitialSurroundingSubText(EditorInfo editorInfo, CharSequence sourceText, int subTextStart) {
            editorInfo.setInitialSurroundingSubText(sourceText, subTextStart);
        }
    }

    public static String[] getContentMimeTypes(EditorInfo editorInfo) {
        if (Build.VERSION.SDK_INT >= 25) {
            String[] strArr = editorInfo.contentMimeTypes;
            return strArr != null ? strArr : EMPTY_STRING_ARRAY;
        } else if (editorInfo.extras == null) {
            return EMPTY_STRING_ARRAY;
        } else {
            String[] stringArray = editorInfo.extras.getStringArray(CONTENT_MIME_TYPES_KEY);
            if (stringArray == null) {
                stringArray = editorInfo.extras.getStringArray(CONTENT_MIME_TYPES_INTEROP_KEY);
            }
            return stringArray != null ? stringArray : EMPTY_STRING_ARRAY;
        }
    }

    public static CharSequence getInitialSelectedText(EditorInfo editorInfo, int flags) {
        CharSequence charSequence;
        if (Build.VERSION.SDK_INT >= 30) {
            return Api30Impl.getInitialSelectedText(editorInfo, flags);
        }
        if (editorInfo.extras == null) {
            return null;
        }
        int min = Math.min(editorInfo.initialSelStart, editorInfo.initialSelEnd);
        int max = Math.max(editorInfo.initialSelStart, editorInfo.initialSelEnd);
        int i = editorInfo.extras.getInt(CONTENT_SELECTION_HEAD_KEY);
        int i2 = editorInfo.extras.getInt(CONTENT_SELECTION_END_KEY);
        int i3 = max - min;
        if (editorInfo.initialSelStart < 0 || editorInfo.initialSelEnd < 0 || i2 - i != i3 || (charSequence = editorInfo.extras.getCharSequence(CONTENT_SURROUNDING_TEXT_KEY)) == null) {
            return null;
        }
        if ((flags & 1) != 0) {
            return charSequence.subSequence(i, i2);
        }
        String substring = TextUtils.substring(charSequence, i, i2);
        Log1F380D.a((Object) substring);
        return substring;
    }

    static int getProtocol(EditorInfo editorInfo) {
        if (Build.VERSION.SDK_INT >= 25) {
            return 1;
        }
        if (editorInfo.extras == null) {
            return 0;
        }
        boolean containsKey = editorInfo.extras.containsKey(CONTENT_MIME_TYPES_KEY);
        boolean containsKey2 = editorInfo.extras.containsKey(CONTENT_MIME_TYPES_INTEROP_KEY);
        if (containsKey && containsKey2) {
            return 4;
        }
        if (containsKey) {
            return 3;
        }
        return containsKey2 ? 2 : 0;
    }

    private static boolean isCutOnSurrogate(CharSequence sourceText, int cutPosition, int policy) {
        switch (policy) {
            case 0:
                return Character.isLowSurrogate(sourceText.charAt(cutPosition));
            case 1:
                return Character.isHighSurrogate(sourceText.charAt(cutPosition));
            default:
                return false;
        }
    }

    private static boolean isPasswordInputType(int inputType) {
        int i = inputType & 4095;
        return i == 129 || i == 225 || i == 18;
    }

    public static void setContentMimeTypes(EditorInfo editorInfo, String[] contentMimeTypes) {
        if (Build.VERSION.SDK_INT >= 25) {
            editorInfo.contentMimeTypes = contentMimeTypes;
            return;
        }
        if (editorInfo.extras == null) {
            editorInfo.extras = new Bundle();
        }
        editorInfo.extras.putStringArray(CONTENT_MIME_TYPES_KEY, contentMimeTypes);
        editorInfo.extras.putStringArray(CONTENT_MIME_TYPES_INTEROP_KEY, contentMimeTypes);
    }

    public static void setInitialSurroundingSubText(EditorInfo editorInfo, CharSequence subText, int subTextStart) {
        Preconditions.checkNotNull(subText);
        if (Build.VERSION.SDK_INT >= 30) {
            Api30Impl.setInitialSurroundingSubText(editorInfo, subText, subTextStart);
            return;
        }
        int i = editorInfo.initialSelStart > editorInfo.initialSelEnd ? editorInfo.initialSelEnd - subTextStart : editorInfo.initialSelStart - subTextStart;
        int i2 = editorInfo.initialSelStart > editorInfo.initialSelEnd ? editorInfo.initialSelStart - subTextStart : editorInfo.initialSelEnd - subTextStart;
        int length = subText.length();
        if (subTextStart < 0 || i < 0 || i2 > length) {
            setSurroundingText(editorInfo, (CharSequence) null, 0, 0);
        } else if (isPasswordInputType(editorInfo.inputType)) {
            setSurroundingText(editorInfo, (CharSequence) null, 0, 0);
        } else if (length <= 2048) {
            setSurroundingText(editorInfo, subText, i, i2);
        } else {
            trimLongSurroundingText(editorInfo, subText, i, i2);
        }
    }

    public static void setInitialSurroundingText(EditorInfo editorInfo, CharSequence sourceText) {
        if (Build.VERSION.SDK_INT >= 30) {
            Api30Impl.setInitialSurroundingSubText(editorInfo, sourceText, 0);
        } else {
            setInitialSurroundingSubText(editorInfo, sourceText, 0);
        }
    }

    private static void setSurroundingText(EditorInfo editorInfo, CharSequence surroundingText, int selectionHead, int selectionEnd) {
        if (editorInfo.extras == null) {
            editorInfo.extras = new Bundle();
        }
        editorInfo.extras.putCharSequence(CONTENT_SURROUNDING_TEXT_KEY, surroundingText != null ? new SpannableStringBuilder(surroundingText) : null);
        editorInfo.extras.putInt(CONTENT_SELECTION_HEAD_KEY, selectionHead);
        editorInfo.extras.putInt(CONTENT_SELECTION_END_KEY, selectionEnd);
    }

    private static void trimLongSurroundingText(EditorInfo editorInfo, CharSequence subText, int selStart, int selEnd) {
        CharSequence charSequence = subText;
        int i = selStart;
        int i2 = selEnd;
        int i3 = i2 - i;
        int i4 = i3 > 1024 ? 0 : i3;
        int i5 = 2048 - i4;
        int min = Math.min(subText.length() - i2, i5 - Math.min(i, (int) (((double) i5) * 0.8d)));
        int min2 = Math.min(i, i5 - min);
        int i6 = i - min2;
        if (isCutOnSurrogate(charSequence, i - min2, 0)) {
            i6++;
            min2--;
        }
        if (isCutOnSurrogate(charSequence, (i2 + min) - 1, 1)) {
            min--;
        }
        int i7 = 0 + min2;
        setSurroundingText(editorInfo, i4 != i3 ? TextUtils.concat(new CharSequence[]{charSequence.subSequence(i6, i6 + min2), charSequence.subSequence(i2, i2 + min)}) : charSequence.subSequence(i6, i6 + min2 + i4 + min), i7, i7 + i4);
    }

    public static CharSequence getInitialTextAfterCursor(EditorInfo editorInfo, int length, int flags) {
        CharSequence charSequence;
        if (Build.VERSION.SDK_INT >= 30) {
            return Api30Impl.getInitialTextAfterCursor(editorInfo, length, flags);
        }
        if (editorInfo.extras == null || (charSequence = editorInfo.extras.getCharSequence(CONTENT_SURROUNDING_TEXT_KEY)) == null) {
            return null;
        }
        int i = editorInfo.extras.getInt(CONTENT_SELECTION_END_KEY);
        int min = Math.min(length, charSequence.length() - i);
        if ((flags & 1) != 0) {
            return charSequence.subSequence(i, i + min);
        }
        String substring = TextUtils.substring(charSequence, i, i + min);
        Log1F380D.a((Object) substring);
        return substring;
    }

    public static CharSequence getInitialTextBeforeCursor(EditorInfo editorInfo, int length, int flags) {
        CharSequence charSequence;
        if (Build.VERSION.SDK_INT >= 30) {
            return Api30Impl.getInitialTextBeforeCursor(editorInfo, length, flags);
        }
        if (editorInfo.extras == null || (charSequence = editorInfo.extras.getCharSequence(CONTENT_SURROUNDING_TEXT_KEY)) == null) {
            return null;
        }
        int i = editorInfo.extras.getInt(CONTENT_SELECTION_HEAD_KEY);
        int min = Math.min(length, i);
        if ((flags & 1) != 0) {
            return charSequence.subSequence(i - min, i);
        }
        String substring = TextUtils.substring(charSequence, i - min, i);
        Log1F380D.a((Object) substring);
        return substring;
    }
}
