package com.google.android.material.datepicker;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.Collection;

/* compiled from: 00F2 */
class MonthAdapter extends BaseAdapter {
    static final int MAXIMUM_WEEKS = UtcDates.getUtcCalendar().getMaximum(4);
    final CalendarConstraints calendarConstraints;
    CalendarStyle calendarStyle;
    final DateSelector<?> dateSelector;
    final Month month;
    private Collection<Long> previouslySelectedDates;

    MonthAdapter(Month month2, DateSelector<?> dateSelector2, CalendarConstraints calendarConstraints2) {
        this.month = month2;
        this.dateSelector = dateSelector2;
        this.calendarConstraints = calendarConstraints2;
        this.previouslySelectedDates = dateSelector2.getSelectedDays();
    }

    private void initializeStyles(Context context) {
        if (this.calendarStyle == null) {
            this.calendarStyle = new CalendarStyle(context);
        }
    }

    private boolean isSelected(long date) {
        for (Long longValue : this.dateSelector.getSelectedDays()) {
            if (UtcDates.canonicalYearMonthDay(date) == UtcDates.canonicalYearMonthDay(longValue.longValue())) {
                return true;
            }
        }
        return false;
    }

    private void updateSelectedState(TextView day, long date) {
        CalendarItemStyle calendarItemStyle;
        if (day != null) {
            if (this.calendarConstraints.getDateValidator().isValid(date)) {
                day.setEnabled(true);
                calendarItemStyle = isSelected(date) ? this.calendarStyle.selectedDay : UtcDates.getTodayCalendar().getTimeInMillis() == date ? this.calendarStyle.todayDay : this.calendarStyle.day;
            } else {
                day.setEnabled(false);
                calendarItemStyle = this.calendarStyle.invalidDay;
            }
            calendarItemStyle.styleItem(day);
        }
    }

    private void updateSelectedStateForDate(MaterialCalendarGridView monthGrid, long date) {
        if (Month.create(date).equals(this.month)) {
            updateSelectedState((TextView) monthGrid.getChildAt(monthGrid.getAdapter().dayToPosition(this.month.getDayOfMonth(date)) - monthGrid.getFirstVisiblePosition()), date);
        }
    }

    /* access modifiers changed from: package-private */
    public int dayToPosition(int day) {
        return firstPositionInMonth() + (day - 1);
    }

    /* access modifiers changed from: package-private */
    public int firstPositionInMonth() {
        return this.month.daysFromStartOfWeekToFirstOfMonth();
    }

    public int getCount() {
        return this.month.daysInMonth + firstPositionInMonth();
    }

    public Long getItem(int position) {
        if (position < this.month.daysFromStartOfWeekToFirstOfMonth() || position > lastPositionInMonth()) {
            return null;
        }
        return Long.valueOf(this.month.getDay(positionToDay(position)));
    }

    public long getItemId(int position) {
        return (long) (position / this.month.daysInWeek);
    }

    /* JADX WARNING: type inference failed for: r3v6, types: [android.view.View] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.widget.TextView getView(int r11, android.view.View r12, android.view.ViewGroup r13) {
        /*
            r10 = this;
            android.content.Context r0 = r13.getContext()
            r10.initializeStyles(r0)
            r0 = r12
            android.widget.TextView r0 = (android.widget.TextView) r0
            r1 = 0
            if (r12 != 0) goto L_0x001e
            android.content.Context r2 = r13.getContext()
            android.view.LayoutInflater r2 = android.view.LayoutInflater.from(r2)
            int r3 = com.google.android.material.R.layout.mtrl_calendar_day
            android.view.View r3 = r2.inflate(r3, r13, r1)
            r0 = r3
            android.widget.TextView r0 = (android.widget.TextView) r0
        L_0x001e:
            int r2 = r10.firstPositionInMonth()
            int r2 = r11 - r2
            if (r2 < 0) goto L_0x0081
            com.google.android.material.datepicker.Month r3 = r10.month
            int r3 = r3.daysInMonth
            if (r2 < r3) goto L_0x002d
            goto L_0x0081
        L_0x002d:
            int r3 = r2 + 1
            com.google.android.material.datepicker.Month r4 = r10.month
            r0.setTag(r4)
            android.content.res.Resources r4 = r0.getResources()
            android.content.res.Configuration r4 = r4.getConfiguration()
            java.util.Locale r4 = r4.locale
            r5 = 1
            java.lang.Object[] r6 = new java.lang.Object[r5]
            java.lang.Integer r7 = java.lang.Integer.valueOf(r3)
            r6[r1] = r7
            java.lang.String r7 = "%d"
            java.lang.String r6 = java.lang.String.format(r4, r7, r6)
            mt.Log1F380D.a((java.lang.Object) r6)
            r0.setText(r6)
            com.google.android.material.datepicker.Month r6 = r10.month
            long r6 = r6.getDay(r3)
            com.google.android.material.datepicker.Month r8 = r10.month
            int r8 = r8.year
            com.google.android.material.datepicker.Month r9 = com.google.android.material.datepicker.Month.current()
            int r9 = r9.year
            if (r8 != r9) goto L_0x0070
            java.lang.String r8 = com.google.android.material.datepicker.DateStrings.getMonthDayOfWeekDay(r6)
            mt.Log1F380D.a((java.lang.Object) r8)
            r0.setContentDescription(r8)
            goto L_0x007a
        L_0x0070:
            java.lang.String r8 = com.google.android.material.datepicker.DateStrings.getYearMonthDayOfWeekDay(r6)
            mt.Log1F380D.a((java.lang.Object) r8)
            r0.setContentDescription(r8)
        L_0x007a:
            r0.setVisibility(r1)
            r0.setEnabled(r5)
            goto L_0x0089
        L_0x0081:
            r3 = 8
            r0.setVisibility(r3)
            r0.setEnabled(r1)
        L_0x0089:
            java.lang.Long r1 = r10.getItem((int) r11)
            if (r1 != 0) goto L_0x0090
            return r0
        L_0x0090:
            long r3 = r1.longValue()
            r10.updateSelectedState(r0, r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.datepicker.MonthAdapter.getView(int, android.view.View, android.view.ViewGroup):android.widget.TextView");
    }

    public boolean hasStableIds() {
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean isFirstInRow(int position) {
        return position % this.month.daysInWeek == 0;
    }

    /* access modifiers changed from: package-private */
    public boolean isLastInRow(int position) {
        return (position + 1) % this.month.daysInWeek == 0;
    }

    /* access modifiers changed from: package-private */
    public int lastPositionInMonth() {
        return (this.month.daysFromStartOfWeekToFirstOfMonth() + this.month.daysInMonth) - 1;
    }

    /* access modifiers changed from: package-private */
    public int positionToDay(int position) {
        return (position - this.month.daysFromStartOfWeekToFirstOfMonth()) + 1;
    }

    public void updateSelectedStates(MaterialCalendarGridView monthGrid) {
        for (Long longValue : this.previouslySelectedDates) {
            updateSelectedStateForDate(monthGrid, longValue.longValue());
        }
        DateSelector<?> dateSelector2 = this.dateSelector;
        if (dateSelector2 != null) {
            for (Long longValue2 : dateSelector2.getSelectedDays()) {
                updateSelectedStateForDate(monthGrid, longValue2.longValue());
            }
            this.previouslySelectedDates = this.dateSelector.getSelectedDays();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean withinMonth(int position) {
        return position >= firstPositionInMonth() && position <= lastPositionInMonth();
    }
}
