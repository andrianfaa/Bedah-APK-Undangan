package androidx.appcompat.widget;

import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.emoji2.viewsintegration.EmojiTextViewHelper;

class AppCompatEmojiTextHelper {
    private final EmojiTextViewHelper mEmojiTextViewHelper;
    private final TextView mView;

    AppCompatEmojiTextHelper(TextView view) {
        this.mView = view;
        this.mEmojiTextViewHelper = new EmojiTextViewHelper(view, false);
    }

    /* access modifiers changed from: package-private */
    public InputFilter[] getFilters(InputFilter[] filters) {
        return this.mEmojiTextViewHelper.getFilters(filters);
    }

    public boolean isEnabled() {
        return this.mEmojiTextViewHelper.isEnabled();
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: package-private */
    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        TypedArray obtainStyledAttributes = this.mView.getContext().obtainStyledAttributes(attrs, R.styleable.AppCompatTextView, defStyleAttr, 0);
        boolean z = true;
        try {
            if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextView_emojiCompatEnabled)) {
                z = obtainStyledAttributes.getBoolean(R.styleable.AppCompatTextView_emojiCompatEnabled, true);
            }
            obtainStyledAttributes.recycle();
            setEnabled(z);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    /* access modifiers changed from: package-private */
    public void setAllCaps(boolean allCaps) {
        this.mEmojiTextViewHelper.setAllCaps(allCaps);
    }

    /* access modifiers changed from: package-private */
    public void setEnabled(boolean enabled) {
        this.mEmojiTextViewHelper.setEnabled(enabled);
    }

    public TransformationMethod wrapTransformationMethod(TransformationMethod transformationMethod) {
        return this.mEmojiTextViewHelper.wrapTransformationMethod(transformationMethod);
    }
}
