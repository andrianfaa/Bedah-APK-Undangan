package androidx.emoji2.viewsintegration;

import android.os.Build;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import androidx.core.util.Preconditions;

public final class EmojiEditTextHelper {
    private int mEmojiReplaceStrategy;
    private final HelperInternal mHelper;
    private int mMaxEmojiCount;

    static class HelperInternal {
        HelperInternal() {
        }

        /* access modifiers changed from: package-private */
        public KeyListener getKeyListener(KeyListener keyListener) {
            return keyListener;
        }

        /* access modifiers changed from: package-private */
        public boolean isEnabled() {
            return false;
        }

        /* access modifiers changed from: package-private */
        public InputConnection onCreateInputConnection(InputConnection inputConnection, EditorInfo outAttrs) {
            return inputConnection;
        }

        /* access modifiers changed from: package-private */
        public void setEmojiReplaceStrategy(int replaceStrategy) {
        }

        /* access modifiers changed from: package-private */
        public void setEnabled(boolean isEnabled) {
        }

        /* access modifiers changed from: package-private */
        public void setMaxEmojiCount(int maxEmojiCount) {
        }
    }

    private static class HelperInternal19 extends HelperInternal {
        private final EditText mEditText;
        private final EmojiTextWatcher mTextWatcher;

        HelperInternal19(EditText editText, boolean expectInitializedEmojiCompat) {
            this.mEditText = editText;
            EmojiTextWatcher emojiTextWatcher = new EmojiTextWatcher(editText, expectInitializedEmojiCompat);
            this.mTextWatcher = emojiTextWatcher;
            editText.addTextChangedListener(emojiTextWatcher);
            editText.setEditableFactory(EmojiEditableFactory.getInstance());
        }

        /* access modifiers changed from: package-private */
        public KeyListener getKeyListener(KeyListener keyListener) {
            if (keyListener instanceof EmojiKeyListener) {
                return keyListener;
            }
            if (keyListener == null) {
                return null;
            }
            return keyListener instanceof NumberKeyListener ? keyListener : new EmojiKeyListener(keyListener);
        }

        /* access modifiers changed from: package-private */
        public boolean isEnabled() {
            return this.mTextWatcher.isEnabled();
        }

        /* access modifiers changed from: package-private */
        public InputConnection onCreateInputConnection(InputConnection inputConnection, EditorInfo outAttrs) {
            return inputConnection instanceof EmojiInputConnection ? inputConnection : new EmojiInputConnection(this.mEditText, inputConnection, outAttrs);
        }

        /* access modifiers changed from: package-private */
        public void setEmojiReplaceStrategy(int replaceStrategy) {
            this.mTextWatcher.setEmojiReplaceStrategy(replaceStrategy);
        }

        /* access modifiers changed from: package-private */
        public void setEnabled(boolean isEnabled) {
            this.mTextWatcher.setEnabled(isEnabled);
        }

        /* access modifiers changed from: package-private */
        public void setMaxEmojiCount(int maxEmojiCount) {
            this.mTextWatcher.setMaxEmojiCount(maxEmojiCount);
        }
    }

    public EmojiEditTextHelper(EditText editText) {
        this(editText, true);
    }

    public EmojiEditTextHelper(EditText editText, boolean expectInitializedEmojiCompat) {
        this.mMaxEmojiCount = Integer.MAX_VALUE;
        this.mEmojiReplaceStrategy = 0;
        Preconditions.checkNotNull(editText, "editText cannot be null");
        if (Build.VERSION.SDK_INT < 19) {
            this.mHelper = new HelperInternal();
        } else {
            this.mHelper = new HelperInternal19(editText, expectInitializedEmojiCompat);
        }
    }

    public int getEmojiReplaceStrategy() {
        return this.mEmojiReplaceStrategy;
    }

    public KeyListener getKeyListener(KeyListener keyListener) {
        return this.mHelper.getKeyListener(keyListener);
    }

    public int getMaxEmojiCount() {
        return this.mMaxEmojiCount;
    }

    public boolean isEnabled() {
        return this.mHelper.isEnabled();
    }

    public InputConnection onCreateInputConnection(InputConnection inputConnection, EditorInfo outAttrs) {
        if (inputConnection == null) {
            return null;
        }
        return this.mHelper.onCreateInputConnection(inputConnection, outAttrs);
    }

    public void setEmojiReplaceStrategy(int replaceStrategy) {
        this.mEmojiReplaceStrategy = replaceStrategy;
        this.mHelper.setEmojiReplaceStrategy(replaceStrategy);
    }

    public void setEnabled(boolean isEnabled) {
        this.mHelper.setEnabled(isEnabled);
    }

    public void setMaxEmojiCount(int maxEmojiCount) {
        Preconditions.checkArgumentNonnegative(maxEmojiCount, "maxEmojiCount should be greater than 0");
        this.mMaxEmojiCount = maxEmojiCount;
        this.mHelper.setMaxEmojiCount(maxEmojiCount);
    }
}
