package androidx.appcompat.widget;

import android.content.res.TypedArray;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import androidx.appcompat.R;
import androidx.emoji2.viewsintegration.EmojiEditTextHelper;

class AppCompatEmojiEditTextHelper {
    private final EmojiEditTextHelper mEmojiEditTextHelper;
    private final EditText mView;

    AppCompatEmojiEditTextHelper(EditText view) {
        this.mView = view;
        this.mEmojiEditTextHelper = new EmojiEditTextHelper(view, false);
    }

    /* access modifiers changed from: package-private */
    public KeyListener getKeyListener(KeyListener keyListener) {
        return isEmojiCapableKeyListener(keyListener) ? this.mEmojiEditTextHelper.getKeyListener(keyListener) : keyListener;
    }

    /* access modifiers changed from: package-private */
    public boolean isEmojiCapableKeyListener(KeyListener currentKeyListener) {
        return !(currentKeyListener instanceof NumberKeyListener);
    }

    /* access modifiers changed from: package-private */
    public boolean isEnabled() {
        return this.mEmojiEditTextHelper.isEnabled();
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
    public InputConnection onCreateInputConnection(InputConnection inputConnection, EditorInfo outAttrs) {
        return this.mEmojiEditTextHelper.onCreateInputConnection(inputConnection, outAttrs);
    }

    /* access modifiers changed from: package-private */
    public void setEnabled(boolean enabled) {
        this.mEmojiEditTextHelper.setEnabled(enabled);
    }
}
