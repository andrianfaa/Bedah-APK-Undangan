package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.core.util.Pair;
import com.google.android.material.R;
import com.google.android.material.internal.ManufacturerUtils;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.textfield.TextInputLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import mt.Log1F380D;

/* compiled from: 00F4 */
public class SingleDateSelector implements DateSelector<Long> {
    public static final Parcelable.Creator<SingleDateSelector> CREATOR = new Parcelable.Creator<SingleDateSelector>() {
        public SingleDateSelector createFromParcel(Parcel source) {
            SingleDateSelector singleDateSelector = new SingleDateSelector();
            Long unused = singleDateSelector.selectedItem = (Long) source.readValue(Long.class.getClassLoader());
            return singleDateSelector;
        }

        public SingleDateSelector[] newArray(int size) {
            return new SingleDateSelector[size];
        }
    };
    /* access modifiers changed from: private */
    public Long selectedItem;

    /* access modifiers changed from: private */
    public void clearSelection() {
        this.selectedItem = null;
    }

    public int describeContents() {
        return 0;
    }

    public int getDefaultThemeResId(Context context) {
        return MaterialAttributes.resolveOrThrow(context, R.attr.materialCalendarTheme, MaterialDatePicker.class.getCanonicalName());
    }

    public int getDefaultTitleResId() {
        return R.string.mtrl_picker_date_header_title;
    }

    public Collection<Long> getSelectedDays() {
        ArrayList arrayList = new ArrayList();
        Long l = this.selectedItem;
        if (l != null) {
            arrayList.add(l);
        }
        return arrayList;
    }

    public Collection<Pair<Long, Long>> getSelectedRanges() {
        return new ArrayList();
    }

    public Long getSelection() {
        return this.selectedItem;
    }

    public String getSelectionDisplayString(Context context) {
        Resources resources = context.getResources();
        Long l = this.selectedItem;
        if (l == null) {
            return resources.getString(R.string.mtrl_picker_date_header_unselected);
        }
        String yearMonthDay = DateStrings.getYearMonthDay(l.longValue());
        Log1F380D.a((Object) yearMonthDay);
        return resources.getString(R.string.mtrl_picker_date_header_selected, new Object[]{yearMonthDay});
    }

    public boolean isSelectionComplete() {
        return this.selectedItem != null;
    }

    public void select(long selection) {
        this.selectedItem = Long.valueOf(selection);
    }

    public void setSelection(Long selection) {
        this.selectedItem = selection == null ? null : Long.valueOf(UtcDates.canonicalYearMonthDay(selection.longValue()));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.selectedItem);
    }

    public View onCreateTextInputView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle, CalendarConstraints constraints, OnSelectionChangedListener<Long> onSelectionChangedListener) {
        View inflate = layoutInflater.inflate(R.layout.mtrl_picker_text_input_date, viewGroup, false);
        TextInputLayout textInputLayout = (TextInputLayout) inflate.findViewById(R.id.mtrl_picker_text_input_date);
        EditText editText = textInputLayout.getEditText();
        if (ManufacturerUtils.isDateInputKeyboardMissingSeparatorCharacters()) {
            editText.setInputType(17);
        }
        SimpleDateFormat textInputFormat = UtcDates.getTextInputFormat();
        String textInputHint = UtcDates.getTextInputHint(inflate.getResources(), textInputFormat);
        Log1F380D.a((Object) textInputHint);
        textInputLayout.setPlaceholderText(textInputHint);
        Long l = this.selectedItem;
        if (l != null) {
            editText.setText(textInputFormat.format(l));
        }
        final OnSelectionChangedListener<Long> onSelectionChangedListener2 = onSelectionChangedListener;
        editText.addTextChangedListener(new DateFormatTextWatcher(textInputHint, textInputFormat, textInputLayout, constraints) {
            /* access modifiers changed from: package-private */
            public void onInvalidDate() {
                onSelectionChangedListener2.onIncompleteSelectionChanged();
            }

            /* access modifiers changed from: package-private */
            public void onValidDate(Long day) {
                if (day == null) {
                    SingleDateSelector.this.clearSelection();
                } else {
                    SingleDateSelector.this.select(day.longValue());
                }
                onSelectionChangedListener2.onSelectionChanged(SingleDateSelector.this.getSelection());
            }
        });
        ViewUtils.requestFocusAndShowKeyboard(editText);
        return inflate;
    }
}
