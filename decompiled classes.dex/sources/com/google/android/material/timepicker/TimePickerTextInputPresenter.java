package com.google.android.material.timepicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.timepicker.TimePickerView;
import java.lang.reflect.Field;
import java.util.Locale;
import mt.Log1F380D;

/* compiled from: 0109 */
class TimePickerTextInputPresenter implements TimePickerView.OnSelectionChange, TimePickerPresenter {
    private final TimePickerTextInputKeyController controller;
    private final EditText hourEditText;
    private final ChipTextInputComboView hourTextInput;
    private final TextWatcher hourTextWatcher = new TextWatcherAdapter() {
        public void afterTextChanged(Editable s) {
            try {
                if (TextUtils.isEmpty(s)) {
                    TimePickerTextInputPresenter.this.time.setHour(0);
                    return;
                }
                TimePickerTextInputPresenter.this.time.setHour(Integer.parseInt(s.toString()));
            } catch (NumberFormatException e) {
            }
        }
    };
    private final EditText minuteEditText;
    private final ChipTextInputComboView minuteTextInput;
    private final TextWatcher minuteTextWatcher = new TextWatcherAdapter() {
        public void afterTextChanged(Editable s) {
            try {
                if (TextUtils.isEmpty(s)) {
                    TimePickerTextInputPresenter.this.time.setMinute(0);
                    return;
                }
                TimePickerTextInputPresenter.this.time.setMinute(Integer.parseInt(s.toString()));
            } catch (NumberFormatException e) {
            }
        }
    };
    /* access modifiers changed from: private */
    public final TimeModel time;
    private final LinearLayout timePickerView;
    private MaterialButtonToggleGroup toggle;

    public TimePickerTextInputPresenter(LinearLayout timePickerView2, final TimeModel time2) {
        this.timePickerView = timePickerView2;
        this.time = time2;
        Resources resources = timePickerView2.getResources();
        ChipTextInputComboView chipTextInputComboView = (ChipTextInputComboView) timePickerView2.findViewById(R.id.material_minute_text_input);
        this.minuteTextInput = chipTextInputComboView;
        ChipTextInputComboView chipTextInputComboView2 = (ChipTextInputComboView) timePickerView2.findViewById(R.id.material_hour_text_input);
        this.hourTextInput = chipTextInputComboView2;
        ((TextView) chipTextInputComboView.findViewById(R.id.material_label)).setText(resources.getString(R.string.material_timepicker_minute));
        ((TextView) chipTextInputComboView2.findViewById(R.id.material_label)).setText(resources.getString(R.string.material_timepicker_hour));
        chipTextInputComboView.setTag(R.id.selection_type, 12);
        chipTextInputComboView2.setTag(R.id.selection_type, 10);
        if (time2.format == 0) {
            setupPeriodToggle();
        }
        AnonymousClass3 r5 = new View.OnClickListener() {
            public void onClick(View v) {
                TimePickerTextInputPresenter.this.onSelectionChanged(((Integer) v.getTag(R.id.selection_type)).intValue());
            }
        };
        chipTextInputComboView2.setOnClickListener(r5);
        chipTextInputComboView.setOnClickListener(r5);
        chipTextInputComboView2.addInputFilter(time2.getHourInputValidator());
        chipTextInputComboView.addInputFilter(time2.getMinuteInputValidator());
        EditText editText = chipTextInputComboView2.getTextInput().getEditText();
        this.hourEditText = editText;
        EditText editText2 = chipTextInputComboView.getTextInput().getEditText();
        this.minuteEditText = editText2;
        if (Build.VERSION.SDK_INT < 21) {
            int color = MaterialColors.getColor(timePickerView2, R.attr.colorPrimary);
            setCursorDrawableColor(editText, color);
            setCursorDrawableColor(editText2, color);
        }
        this.controller = new TimePickerTextInputKeyController(chipTextInputComboView2, chipTextInputComboView, time2);
        chipTextInputComboView2.setChipDelegate(new ClickActionDelegate(timePickerView2.getContext(), R.string.material_hour_selection) {
            public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
                super.onInitializeAccessibilityNodeInfo(host, info);
                Resources resources = host.getResources();
                int i = R.string.material_hour_suffix;
                String valueOf = String.valueOf(time2.getHourForDisplay());
                Log1F380D.a((Object) valueOf);
                info.setContentDescription(resources.getString(i, new Object[]{valueOf}));
            }
        });
        chipTextInputComboView.setChipDelegate(new ClickActionDelegate(timePickerView2.getContext(), R.string.material_minute_selection) {
            public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
                super.onInitializeAccessibilityNodeInfo(host, info);
                Resources resources = host.getResources();
                int i = R.string.material_minute_suffix;
                String valueOf = String.valueOf(time2.minute);
                Log1F380D.a((Object) valueOf);
                info.setContentDescription(resources.getString(i, new Object[]{valueOf}));
            }
        });
        initialize();
    }

    private void addTextWatchers() {
        this.hourEditText.addTextChangedListener(this.hourTextWatcher);
        this.minuteEditText.addTextChangedListener(this.minuteTextWatcher);
    }

    private void removeTextWatchers() {
        this.hourEditText.removeTextChangedListener(this.hourTextWatcher);
        this.minuteEditText.removeTextChangedListener(this.minuteTextWatcher);
    }

    private static void setCursorDrawableColor(EditText view, int color) {
        try {
            Context context = view.getContext();
            Field declaredField = TextView.class.getDeclaredField("mCursorDrawableRes");
            declaredField.setAccessible(true);
            int i = declaredField.getInt(view);
            Field declaredField2 = TextView.class.getDeclaredField("mEditor");
            declaredField2.setAccessible(true);
            Object obj = declaredField2.get(view);
            Field declaredField3 = obj.getClass().getDeclaredField("mCursorDrawable");
            declaredField3.setAccessible(true);
            Drawable drawable = AppCompatResources.getDrawable(context, i);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            declaredField3.set(obj, new Drawable[]{drawable, drawable});
        } catch (Throwable th) {
        }
    }

    private void setTime(TimeModel time2) {
        removeTextWatchers();
        Locale locale = this.timePickerView.getResources().getConfiguration().locale;
        String format = String.format(locale, TimeModel.ZERO_LEADING_NUMBER_FORMAT, new Object[]{Integer.valueOf(time2.minute)});
        Log1F380D.a((Object) format);
        String format2 = String.format(locale, TimeModel.ZERO_LEADING_NUMBER_FORMAT, new Object[]{Integer.valueOf(time2.getHourForDisplay())});
        Log1F380D.a((Object) format2);
        this.minuteTextInput.setText(format);
        this.hourTextInput.setText(format2);
        addTextWatchers();
        updateSelection();
    }

    private void setupPeriodToggle() {
        MaterialButtonToggleGroup materialButtonToggleGroup = (MaterialButtonToggleGroup) this.timePickerView.findViewById(R.id.material_clock_period_toggle);
        this.toggle = materialButtonToggleGroup;
        materialButtonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                TimePickerTextInputPresenter.this.time.setPeriod(checkedId == R.id.material_clock_period_pm_button ? 1 : 0);
            }
        });
        this.toggle.setVisibility(0);
        updateSelection();
    }

    private void updateSelection() {
        MaterialButtonToggleGroup materialButtonToggleGroup = this.toggle;
        if (materialButtonToggleGroup != null) {
            materialButtonToggleGroup.check(this.time.period == 0 ? R.id.material_clock_period_am_button : R.id.material_clock_period_pm_button);
        }
    }

    public void clearCheck() {
        this.minuteTextInput.setChecked(false);
        this.hourTextInput.setChecked(false);
    }

    public void hide() {
        View focusedChild = this.timePickerView.getFocusedChild();
        if (focusedChild == null) {
            this.timePickerView.setVisibility(8);
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) ContextCompat.getSystemService(this.timePickerView.getContext(), InputMethodManager.class);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(focusedChild.getWindowToken(), 0);
        }
        this.timePickerView.setVisibility(8);
    }

    public void initialize() {
        addTextWatchers();
        setTime(this.time);
        this.controller.bind();
    }

    public void invalidate() {
        setTime(this.time);
    }

    public void onSelectionChanged(int selection) {
        this.time.selection = selection;
        boolean z = true;
        this.minuteTextInput.setChecked(selection == 12);
        ChipTextInputComboView chipTextInputComboView = this.hourTextInput;
        if (selection != 10) {
            z = false;
        }
        chipTextInputComboView.setChecked(z);
        updateSelection();
    }

    public void resetChecked() {
        boolean z = true;
        this.minuteTextInput.setChecked(this.time.selection == 12);
        ChipTextInputComboView chipTextInputComboView = this.hourTextInput;
        if (this.time.selection != 10) {
            z = false;
        }
        chipTextInputComboView.setChecked(z);
    }

    public void show() {
        this.timePickerView.setVisibility(0);
    }
}
