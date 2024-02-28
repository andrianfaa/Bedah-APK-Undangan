package com.google.android.material.timepicker;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Arrays;
import mt.Log1F380D;

/* compiled from: 0102 */
class ChipTextInputComboView extends FrameLayout implements Checkable {
    /* access modifiers changed from: private */
    public final Chip chip;
    private final EditText editText;
    private TextView label;
    private final TextInputLayout textInputLayout;
    private TextWatcher watcher;

    /* compiled from: 0101 */
    private class TextFormatter extends TextWatcherAdapter {
        private static final String DEFAULT_TEXT = "00";

        private TextFormatter() {
        }

        public void afterTextChanged(Editable editable) {
            if (TextUtils.isEmpty(editable)) {
                Chip access$200 = ChipTextInputComboView.this.chip;
                String access$100 = ChipTextInputComboView.this.formatText(DEFAULT_TEXT);
                Log1F380D.a((Object) access$100);
                access$200.setText(access$100);
                return;
            }
            Chip access$2002 = ChipTextInputComboView.this.chip;
            String access$1002 = ChipTextInputComboView.this.formatText(editable);
            Log1F380D.a((Object) access$1002);
            access$2002.setText(access$1002);
        }
    }

    public ChipTextInputComboView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ChipTextInputComboView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChipTextInputComboView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater from = LayoutInflater.from(context);
        Chip chip2 = (Chip) from.inflate(R.layout.material_time_chip, this, false);
        this.chip = chip2;
        chip2.setAccessibilityClassName("android.view.View");
        TextInputLayout textInputLayout2 = (TextInputLayout) from.inflate(R.layout.material_time_input, this, false);
        this.textInputLayout = textInputLayout2;
        EditText editText2 = textInputLayout2.getEditText();
        this.editText = editText2;
        editText2.setVisibility(4);
        TextFormatter textFormatter = new TextFormatter();
        this.watcher = textFormatter;
        editText2.addTextChangedListener(textFormatter);
        updateHintLocales();
        addView(chip2);
        addView(textInputLayout2);
        this.label = (TextView) findViewById(R.id.material_label);
        editText2.setSaveEnabled(false);
        editText2.setLongClickable(false);
    }

    /* access modifiers changed from: private */
    public String formatText(CharSequence text) {
        String formatText = TimeModel.formatText(getResources(), text);
        Log1F380D.a((Object) formatText);
        return formatText;
    }

    private void updateHintLocales() {
        if (Build.VERSION.SDK_INT >= 24) {
            this.editText.setImeHintLocales(getContext().getResources().getConfiguration().getLocales());
        }
    }

    public void addInputFilter(InputFilter filter) {
        InputFilter[] filters = this.editText.getFilters();
        InputFilter[] inputFilterArr = (InputFilter[]) Arrays.copyOf(filters, filters.length + 1);
        inputFilterArr[filters.length] = filter;
        this.editText.setFilters(inputFilterArr);
    }

    public TextInputLayout getTextInput() {
        return this.textInputLayout;
    }

    public boolean isChecked() {
        return this.chip.isChecked();
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateHintLocales();
    }

    public void setChecked(boolean checked) {
        this.chip.setChecked(checked);
        int i = 0;
        this.editText.setVisibility(checked ? 0 : 4);
        Chip chip2 = this.chip;
        if (checked) {
            i = 8;
        }
        chip2.setVisibility(i);
        if (isChecked()) {
            ViewUtils.requestFocusAndShowKeyboard(this.editText);
            if (!TextUtils.isEmpty(this.editText.getText())) {
                EditText editText2 = this.editText;
                editText2.setSelection(editText2.getText().length());
            }
        }
    }

    public void setChipDelegate(AccessibilityDelegateCompat clickActionDelegate) {
        ViewCompat.setAccessibilityDelegate(this.chip, clickActionDelegate);
    }

    public void setCursorVisible(boolean visible) {
        this.editText.setCursorVisible(visible);
    }

    public void setHelperText(CharSequence helperText) {
        this.label.setText(helperText);
    }

    public void setOnClickListener(View.OnClickListener l) {
        this.chip.setOnClickListener(l);
    }

    public void setTag(int key, Object tag) {
        this.chip.setTag(key, tag);
    }

    public void setText(CharSequence text) {
        this.chip.setText(formatText(text));
        if (!TextUtils.isEmpty(this.editText.getText())) {
            this.editText.removeTextChangedListener(this.watcher);
            this.editText.setText((CharSequence) null);
            this.editText.addTextChangedListener(this.watcher);
        }
    }

    public void toggle() {
        this.chip.toggle();
    }
}
