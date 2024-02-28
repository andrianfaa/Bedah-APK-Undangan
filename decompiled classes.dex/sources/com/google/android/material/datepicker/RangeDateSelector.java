package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.core.util.Pair;
import androidx.core.util.Preconditions;
import com.google.android.material.R;
import com.google.android.material.internal.ManufacturerUtils;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.textfield.TextInputLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import mt.Log1F380D;

/* compiled from: 00F3 */
public class RangeDateSelector implements DateSelector<Pair<Long, Long>> {
    public static final Parcelable.Creator<RangeDateSelector> CREATOR = new Parcelable.Creator<RangeDateSelector>() {
        public RangeDateSelector createFromParcel(Parcel source) {
            RangeDateSelector rangeDateSelector = new RangeDateSelector();
            Long unused = rangeDateSelector.selectedStartItem = (Long) source.readValue(Long.class.getClassLoader());
            Long unused2 = rangeDateSelector.selectedEndItem = (Long) source.readValue(Long.class.getClassLoader());
            return rangeDateSelector;
        }

        public RangeDateSelector[] newArray(int size) {
            return new RangeDateSelector[size];
        }
    };
    private final String invalidRangeEndError = " ";
    private String invalidRangeStartError;
    /* access modifiers changed from: private */
    public Long proposedTextEnd = null;
    /* access modifiers changed from: private */
    public Long proposedTextStart = null;
    /* access modifiers changed from: private */
    public Long selectedEndItem = null;
    /* access modifiers changed from: private */
    public Long selectedStartItem = null;

    private void clearInvalidRange(TextInputLayout start, TextInputLayout end) {
        if (start.getError() != null && this.invalidRangeStartError.contentEquals(start.getError())) {
            start.setError((CharSequence) null);
        }
        if (end.getError() != null && " ".contentEquals(end.getError())) {
            end.setError((CharSequence) null);
        }
    }

    private boolean isValidRange(long start, long end) {
        return start <= end;
    }

    private void setInvalidRange(TextInputLayout start, TextInputLayout end) {
        start.setError(this.invalidRangeStartError);
        end.setError(" ");
    }

    /* access modifiers changed from: private */
    public void updateIfValidTextProposal(TextInputLayout startTextInput, TextInputLayout endTextInput, OnSelectionChangedListener<Pair<Long, Long>> onSelectionChangedListener) {
        Long l = this.proposedTextStart;
        if (l == null || this.proposedTextEnd == null) {
            clearInvalidRange(startTextInput, endTextInput);
            onSelectionChangedListener.onIncompleteSelectionChanged();
        } else if (isValidRange(l.longValue(), this.proposedTextEnd.longValue())) {
            this.selectedStartItem = this.proposedTextStart;
            this.selectedEndItem = this.proposedTextEnd;
            onSelectionChangedListener.onSelectionChanged(getSelection());
        } else {
            setInvalidRange(startTextInput, endTextInput);
            onSelectionChangedListener.onIncompleteSelectionChanged();
        }
    }

    public int describeContents() {
        return 0;
    }

    public int getDefaultThemeResId(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return MaterialAttributes.resolveOrThrow(context, Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels) > resources.getDimensionPixelSize(R.dimen.mtrl_calendar_maximum_default_fullscreen_minor_axis) ? R.attr.materialCalendarTheme : R.attr.materialCalendarFullscreenTheme, MaterialDatePicker.class.getCanonicalName());
    }

    public int getDefaultTitleResId() {
        return R.string.mtrl_picker_range_header_title;
    }

    public Collection<Long> getSelectedDays() {
        ArrayList arrayList = new ArrayList();
        Long l = this.selectedStartItem;
        if (l != null) {
            arrayList.add(l);
        }
        Long l2 = this.selectedEndItem;
        if (l2 != null) {
            arrayList.add(l2);
        }
        return arrayList;
    }

    public Collection<Pair<Long, Long>> getSelectedRanges() {
        if (this.selectedStartItem == null || this.selectedEndItem == null) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new Pair(this.selectedStartItem, this.selectedEndItem));
        return arrayList;
    }

    public Pair<Long, Long> getSelection() {
        return new Pair<>(this.selectedStartItem, this.selectedEndItem);
    }

    public String getSelectionDisplayString(Context context) {
        Resources resources = context.getResources();
        Long l = this.selectedStartItem;
        if (l == null && this.selectedEndItem == null) {
            return resources.getString(R.string.mtrl_picker_range_header_unselected);
        }
        Long l2 = this.selectedEndItem;
        if (l2 == null) {
            int i = R.string.mtrl_picker_range_header_only_start_selected;
            String dateString = DateStrings.getDateString(this.selectedStartItem.longValue());
            Log1F380D.a((Object) dateString);
            return resources.getString(i, new Object[]{dateString});
        } else if (l == null) {
            int i2 = R.string.mtrl_picker_range_header_only_end_selected;
            String dateString2 = DateStrings.getDateString(this.selectedEndItem.longValue());
            Log1F380D.a((Object) dateString2);
            return resources.getString(i2, new Object[]{dateString2});
        } else {
            Pair<String, String> dateRangeString = DateStrings.getDateRangeString(l, l2);
            return resources.getString(R.string.mtrl_picker_range_header_selected, new Object[]{dateRangeString.first, dateRangeString.second});
        }
    }

    public boolean isSelectionComplete() {
        Long l = this.selectedStartItem;
        return (l == null || this.selectedEndItem == null || !isValidRange(l.longValue(), this.selectedEndItem.longValue())) ? false : true;
    }

    public void select(long selection) {
        Long l = this.selectedStartItem;
        if (l == null) {
            this.selectedStartItem = Long.valueOf(selection);
        } else if (this.selectedEndItem != null || !isValidRange(l.longValue(), selection)) {
            this.selectedEndItem = null;
            this.selectedStartItem = Long.valueOf(selection);
        } else {
            this.selectedEndItem = Long.valueOf(selection);
        }
    }

    public void setSelection(Pair<Long, Long> pair) {
        if (!(pair.first == null || pair.second == null)) {
            Preconditions.checkArgument(isValidRange(((Long) pair.first).longValue(), ((Long) pair.second).longValue()));
        }
        Long l = null;
        this.selectedStartItem = pair.first == null ? null : Long.valueOf(UtcDates.canonicalYearMonthDay(((Long) pair.first).longValue()));
        if (pair.second != null) {
            l = Long.valueOf(UtcDates.canonicalYearMonthDay(((Long) pair.second).longValue()));
        }
        this.selectedEndItem = l;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.selectedStartItem);
        dest.writeValue(this.selectedEndItem);
    }

    public View onCreateTextInputView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle, CalendarConstraints constraints, OnSelectionChangedListener<Pair<Long, Long>> onSelectionChangedListener) {
        View inflate = layoutInflater.inflate(R.layout.mtrl_picker_text_input_date_range, viewGroup, false);
        TextInputLayout textInputLayout = (TextInputLayout) inflate.findViewById(R.id.mtrl_picker_text_input_range_start);
        TextInputLayout textInputLayout2 = (TextInputLayout) inflate.findViewById(R.id.mtrl_picker_text_input_range_end);
        EditText editText = textInputLayout.getEditText();
        EditText editText2 = textInputLayout2.getEditText();
        if (ManufacturerUtils.isDateInputKeyboardMissingSeparatorCharacters()) {
            editText.setInputType(17);
            editText2.setInputType(17);
        }
        this.invalidRangeStartError = inflate.getResources().getString(R.string.mtrl_picker_invalid_range);
        SimpleDateFormat textInputFormat = UtcDates.getTextInputFormat();
        Long l = this.selectedStartItem;
        if (l != null) {
            editText.setText(textInputFormat.format(l));
            this.proposedTextStart = this.selectedStartItem;
        }
        Long l2 = this.selectedEndItem;
        if (l2 != null) {
            editText2.setText(textInputFormat.format(l2));
            this.proposedTextEnd = this.selectedEndItem;
        }
        String textInputHint = UtcDates.getTextInputHint(inflate.getResources(), textInputFormat);
        Log1F380D.a((Object) textInputHint);
        textInputLayout.setPlaceholderText(textInputHint);
        textInputLayout2.setPlaceholderText(textInputHint);
        AnonymousClass1 r9 = r0;
        CalendarConstraints calendarConstraints = constraints;
        String str = textInputHint;
        final TextInputLayout textInputLayout3 = textInputLayout;
        SimpleDateFormat simpleDateFormat = textInputFormat;
        final TextInputLayout textInputLayout4 = textInputLayout2;
        EditText editText3 = editText2;
        final OnSelectionChangedListener<Pair<Long, Long>> onSelectionChangedListener2 = onSelectionChangedListener;
        AnonymousClass1 r0 = new DateFormatTextWatcher(textInputHint, textInputFormat, textInputLayout, calendarConstraints) {
            /* access modifiers changed from: package-private */
            public void onInvalidDate() {
                Long unused = RangeDateSelector.this.proposedTextStart = null;
                RangeDateSelector.this.updateIfValidTextProposal(textInputLayout3, textInputLayout4, onSelectionChangedListener2);
            }

            /* access modifiers changed from: package-private */
            public void onValidDate(Long day) {
                Long unused = RangeDateSelector.this.proposedTextStart = day;
                RangeDateSelector.this.updateIfValidTextProposal(textInputLayout3, textInputLayout4, onSelectionChangedListener2);
            }
        };
        editText.addTextChangedListener(r9);
        editText3.addTextChangedListener(new DateFormatTextWatcher(str, simpleDateFormat, textInputLayout2, calendarConstraints) {
            /* access modifiers changed from: package-private */
            public void onInvalidDate() {
                Long unused = RangeDateSelector.this.proposedTextEnd = null;
                RangeDateSelector.this.updateIfValidTextProposal(textInputLayout3, textInputLayout4, onSelectionChangedListener2);
            }

            /* access modifiers changed from: package-private */
            public void onValidDate(Long day) {
                Long unused = RangeDateSelector.this.proposedTextEnd = day;
                RangeDateSelector.this.updateIfValidTextProposal(textInputLayout3, textInputLayout4, onSelectionChangedListener2);
            }
        });
        ViewUtils.requestFocusAndShowKeyboard(editText);
        return inflate;
    }
}
