package androidx.emoji2.viewsintegration;

import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.widget.TextView;
import androidx.emoji2.text.EmojiCompat;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

final class EmojiInputFilter implements InputFilter {
    private EmojiCompat.InitCallback mInitCallback;
    private final TextView mTextView;

    private static class InitCallbackImpl extends EmojiCompat.InitCallback {
        private final Reference<EmojiInputFilter> mEmojiInputFilterReference;
        private final Reference<TextView> mViewRef;

        InitCallbackImpl(TextView textView, EmojiInputFilter emojiInputFilter) {
            this.mViewRef = new WeakReference(textView);
            this.mEmojiInputFilterReference = new WeakReference(emojiInputFilter);
        }

        private boolean isInputFilterCurrentlyRegisteredOnTextView(TextView textView, InputFilter myInputFilter) {
            InputFilter[] filters;
            if (myInputFilter == null || textView == null || (filters = textView.getFilters()) == null) {
                return false;
            }
            for (InputFilter inputFilter : filters) {
                if (inputFilter == myInputFilter) {
                    return true;
                }
            }
            return false;
        }

        public void onInitialized() {
            CharSequence text;
            CharSequence process;
            super.onInitialized();
            TextView textView = this.mViewRef.get();
            if (isInputFilterCurrentlyRegisteredOnTextView(textView, this.mEmojiInputFilterReference.get()) && textView.isAttachedToWindow() && (text = textView.getText()) != (process = EmojiCompat.get().process(text))) {
                int selectionStart = Selection.getSelectionStart(process);
                int selectionEnd = Selection.getSelectionEnd(process);
                textView.setText(process);
                if (process instanceof Spannable) {
                    EmojiInputFilter.updateSelection((Spannable) process, selectionStart, selectionEnd);
                }
            }
        }
    }

    EmojiInputFilter(TextView textView) {
        this.mTextView = textView;
    }

    private EmojiCompat.InitCallback getInitCallback() {
        if (this.mInitCallback == null) {
            this.mInitCallback = new InitCallbackImpl(this.mTextView, this);
        }
        return this.mInitCallback;
    }

    static void updateSelection(Spannable spannable, int start, int end) {
        if (start >= 0 && end >= 0) {
            Selection.setSelection(spannable, start, end);
        } else if (start >= 0) {
            Selection.setSelection(spannable, start);
        } else if (end >= 0) {
            Selection.setSelection(spannable, end);
        }
    }

    public CharSequence filter(CharSequence source, int sourceStart, int sourceEnd, Spanned dest, int destStart, int destEnd) {
        if (this.mTextView.isInEditMode()) {
            return source;
        }
        switch (EmojiCompat.get().getLoadState()) {
            case 0:
            case 3:
                EmojiCompat.get().registerInitCallback(getInitCallback());
                return source;
            case 1:
                boolean z = true;
                if (destEnd == 0 && destStart == 0 && dest.length() == 0 && source == this.mTextView.getText()) {
                    z = false;
                }
                if (!z || source == null) {
                    return source;
                }
                CharSequence subSequence = (sourceStart == 0 && sourceEnd == source.length()) ? source : source.subSequence(sourceStart, sourceEnd);
                return EmojiCompat.get().process(subSequence, 0, subSequence.length());
            default:
                return source;
        }
    }
}
