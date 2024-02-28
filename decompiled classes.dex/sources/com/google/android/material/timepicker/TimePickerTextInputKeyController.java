package com.google.android.material.timepicker;

import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputLayout;

class TimePickerTextInputKeyController implements TextView.OnEditorActionListener, View.OnKeyListener {
    private final ChipTextInputComboView hourLayoutComboView;
    private boolean keyListenerRunning = false;
    private final ChipTextInputComboView minuteLayoutComboView;
    private final TimeModel time;

    TimePickerTextInputKeyController(ChipTextInputComboView hourLayoutComboView2, ChipTextInputComboView minuteLayoutComboView2, TimeModel time2) {
        this.hourLayoutComboView = hourLayoutComboView2;
        this.minuteLayoutComboView = minuteLayoutComboView2;
        this.time = time2;
    }

    private void moveSelection(int selection) {
        boolean z = true;
        this.minuteLayoutComboView.setChecked(selection == 12);
        ChipTextInputComboView chipTextInputComboView = this.hourLayoutComboView;
        if (selection != 10) {
            z = false;
        }
        chipTextInputComboView.setChecked(z);
        this.time.selection = selection;
    }

    private boolean onHourKeyPress(int keyCode, KeyEvent event, EditText editText) {
        Editable text = editText.getText();
        if (text == null) {
            return false;
        }
        if (!(keyCode >= 7 && keyCode <= 16 && event.getAction() == 1 && editText.getSelectionStart() == 2 && text.length() == 2)) {
            return false;
        }
        moveSelection(12);
        return true;
    }

    private boolean onMinuteKeyPress(int keyCode, KeyEvent event, EditText editText) {
        if (!(keyCode == 67 && event.getAction() == 0 && TextUtils.isEmpty(editText.getText()))) {
            return false;
        }
        moveSelection(10);
        return true;
    }

    public void bind() {
        TextInputLayout textInput = this.hourLayoutComboView.getTextInput();
        TextInputLayout textInput2 = this.minuteLayoutComboView.getTextInput();
        EditText editText = textInput.getEditText();
        EditText editText2 = textInput2.getEditText();
        editText.setImeOptions(268435461);
        editText2.setImeOptions(268435462);
        editText.setOnEditorActionListener(this);
        editText.setOnKeyListener(this);
        editText2.setOnKeyListener(this);
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean z = actionId == 5;
        if (z) {
            moveSelection(12);
        }
        return z;
    }

    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (this.keyListenerRunning) {
            return false;
        }
        this.keyListenerRunning = true;
        EditText editText = (EditText) view;
        boolean onMinuteKeyPress = this.time.selection == 12 ? onMinuteKeyPress(keyCode, event, editText) : onHourKeyPress(keyCode, event, editText);
        this.keyListenerRunning = false;
        return onMinuteKeyPress;
    }
}
