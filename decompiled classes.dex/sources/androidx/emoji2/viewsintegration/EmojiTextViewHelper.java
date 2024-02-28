package androidx.emoji2.viewsintegration;

import android.os.Build;
import android.text.InputFilter;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.SparseArray;
import android.widget.TextView;
import androidx.core.util.Preconditions;
import androidx.emoji2.text.EmojiCompat;

public final class EmojiTextViewHelper {
    private final HelperInternal mHelper;

    static class HelperInternal {
        HelperInternal() {
        }

        /* access modifiers changed from: package-private */
        public InputFilter[] getFilters(InputFilter[] filters) {
            return filters;
        }

        public boolean isEnabled() {
            return false;
        }

        /* access modifiers changed from: package-private */
        public void setAllCaps(boolean allCaps) {
        }

        /* access modifiers changed from: package-private */
        public void setEnabled(boolean processEmoji) {
        }

        /* access modifiers changed from: package-private */
        public void updateTransformationMethod() {
        }

        /* access modifiers changed from: package-private */
        public TransformationMethod wrapTransformationMethod(TransformationMethod transformationMethod) {
            return transformationMethod;
        }
    }

    private static class HelperInternal19 extends HelperInternal {
        private final EmojiInputFilter mEmojiInputFilter;
        private boolean mEnabled = true;
        private final TextView mTextView;

        HelperInternal19(TextView textView) {
            this.mTextView = textView;
            this.mEmojiInputFilter = new EmojiInputFilter(textView);
        }

        private InputFilter[] addEmojiInputFilterIfMissing(InputFilter[] filters) {
            for (EmojiInputFilter emojiInputFilter : filters) {
                if (emojiInputFilter == this.mEmojiInputFilter) {
                    return filters;
                }
            }
            InputFilter[] inputFilterArr = new InputFilter[(filters.length + 1)];
            System.arraycopy(filters, 0, inputFilterArr, 0, r0);
            inputFilterArr[r0] = this.mEmojiInputFilter;
            return inputFilterArr;
        }

        private SparseArray<InputFilter> getEmojiInputFilterPositionArray(InputFilter[] filters) {
            SparseArray<InputFilter> sparseArray = new SparseArray<>(1);
            for (int i = 0; i < filters.length; i++) {
                if (filters[i] instanceof EmojiInputFilter) {
                    sparseArray.put(i, filters[i]);
                }
            }
            return sparseArray;
        }

        private InputFilter[] removeEmojiInputFilterIfPresent(InputFilter[] filters) {
            SparseArray<InputFilter> emojiInputFilterPositionArray = getEmojiInputFilterPositionArray(filters);
            if (emojiInputFilterPositionArray.size() == 0) {
                return filters;
            }
            int length = filters.length;
            InputFilter[] inputFilterArr = new InputFilter[(filters.length - emojiInputFilterPositionArray.size())];
            int i = 0;
            for (int i2 = 0; i2 < length; i2++) {
                if (emojiInputFilterPositionArray.indexOfKey(i2) < 0) {
                    inputFilterArr[i] = filters[i2];
                    i++;
                }
            }
            return inputFilterArr;
        }

        private TransformationMethod unwrapForDisabled(TransformationMethod transformationMethod) {
            return transformationMethod instanceof EmojiTransformationMethod ? ((EmojiTransformationMethod) transformationMethod).getOriginalTransformationMethod() : transformationMethod;
        }

        private void updateFilters() {
            this.mTextView.setFilters(getFilters(this.mTextView.getFilters()));
        }

        private TransformationMethod wrapForEnabled(TransformationMethod transformationMethod) {
            return (!(transformationMethod instanceof EmojiTransformationMethod) && !(transformationMethod instanceof PasswordTransformationMethod)) ? new EmojiTransformationMethod(transformationMethod) : transformationMethod;
        }

        /* access modifiers changed from: package-private */
        public InputFilter[] getFilters(InputFilter[] filters) {
            return !this.mEnabled ? removeEmojiInputFilterIfPresent(filters) : addEmojiInputFilterIfMissing(filters);
        }

        public boolean isEnabled() {
            return this.mEnabled;
        }

        /* access modifiers changed from: package-private */
        public void setAllCaps(boolean allCaps) {
            if (allCaps) {
                updateTransformationMethod();
            }
        }

        /* access modifiers changed from: package-private */
        public void setEnabled(boolean enabled) {
            this.mEnabled = enabled;
            updateTransformationMethod();
            updateFilters();
        }

        /* access modifiers changed from: package-private */
        public void setEnabledUnsafe(boolean processEmoji) {
            this.mEnabled = processEmoji;
        }

        /* access modifiers changed from: package-private */
        public void updateTransformationMethod() {
            this.mTextView.setTransformationMethod(wrapTransformationMethod(this.mTextView.getTransformationMethod()));
        }

        /* access modifiers changed from: package-private */
        public TransformationMethod wrapTransformationMethod(TransformationMethod transformationMethod) {
            return this.mEnabled ? wrapForEnabled(transformationMethod) : unwrapForDisabled(transformationMethod);
        }
    }

    private static class SkippingHelper19 extends HelperInternal {
        private final HelperInternal19 mHelperDelegate;

        SkippingHelper19(TextView textView) {
            this.mHelperDelegate = new HelperInternal19(textView);
        }

        private boolean skipBecauseEmojiCompatNotInitialized() {
            return !EmojiCompat.isConfigured();
        }

        /* access modifiers changed from: package-private */
        public InputFilter[] getFilters(InputFilter[] filters) {
            return skipBecauseEmojiCompatNotInitialized() ? filters : this.mHelperDelegate.getFilters(filters);
        }

        public boolean isEnabled() {
            return this.mHelperDelegate.isEnabled();
        }

        /* access modifiers changed from: package-private */
        public void setAllCaps(boolean allCaps) {
            if (!skipBecauseEmojiCompatNotInitialized()) {
                this.mHelperDelegate.setAllCaps(allCaps);
            }
        }

        /* access modifiers changed from: package-private */
        public void setEnabled(boolean processEmoji) {
            if (skipBecauseEmojiCompatNotInitialized()) {
                this.mHelperDelegate.setEnabledUnsafe(processEmoji);
            } else {
                this.mHelperDelegate.setEnabled(processEmoji);
            }
        }

        /* access modifiers changed from: package-private */
        public void updateTransformationMethod() {
            if (!skipBecauseEmojiCompatNotInitialized()) {
                this.mHelperDelegate.updateTransformationMethod();
            }
        }

        /* access modifiers changed from: package-private */
        public TransformationMethod wrapTransformationMethod(TransformationMethod transformationMethod) {
            return skipBecauseEmojiCompatNotInitialized() ? transformationMethod : this.mHelperDelegate.wrapTransformationMethod(transformationMethod);
        }
    }

    public EmojiTextViewHelper(TextView textView) {
        this(textView, true);
    }

    public EmojiTextViewHelper(TextView textView, boolean expectInitializedEmojiCompat) {
        Preconditions.checkNotNull(textView, "textView cannot be null");
        if (Build.VERSION.SDK_INT < 19) {
            this.mHelper = new HelperInternal();
        } else if (!expectInitializedEmojiCompat) {
            this.mHelper = new SkippingHelper19(textView);
        } else {
            this.mHelper = new HelperInternal19(textView);
        }
    }

    public InputFilter[] getFilters(InputFilter[] filters) {
        return this.mHelper.getFilters(filters);
    }

    public boolean isEnabled() {
        return this.mHelper.isEnabled();
    }

    public void setAllCaps(boolean allCaps) {
        this.mHelper.setAllCaps(allCaps);
    }

    public void setEnabled(boolean enabled) {
        this.mHelper.setEnabled(enabled);
    }

    public void updateTransformationMethod() {
        this.mHelper.updateTransformationMethod();
    }

    public TransformationMethod wrapTransformationMethod(TransformationMethod transformationMethod) {
        return this.mHelper.wrapTransformationMethod(transformationMethod);
    }
}
