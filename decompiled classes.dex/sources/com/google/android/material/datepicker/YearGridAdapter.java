package com.google.android.material.datepicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.R;
import com.google.android.material.datepicker.MaterialCalendar;
import java.util.Calendar;
import java.util.Locale;
import mt.Log1F380D;

/* compiled from: 00F6 */
class YearGridAdapter extends RecyclerView.Adapter<ViewHolder> {
    /* access modifiers changed from: private */
    public final MaterialCalendar<?> materialCalendar;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        ViewHolder(TextView view) {
            super(view);
            this.textView = view;
        }
    }

    YearGridAdapter(MaterialCalendar<?> materialCalendar2) {
        this.materialCalendar = materialCalendar2;
    }

    private View.OnClickListener createYearClickListener(final int year) {
        return new View.OnClickListener() {
            public void onClick(View view) {
                YearGridAdapter.this.materialCalendar.setCurrentMonth(YearGridAdapter.this.materialCalendar.getCalendarConstraints().clamp(Month.create(year, YearGridAdapter.this.materialCalendar.getCurrentMonth().month)));
                YearGridAdapter.this.materialCalendar.setSelector(MaterialCalendar.CalendarSelector.DAY);
            }
        };
    }

    public int getItemCount() {
        return this.materialCalendar.getCalendarConstraints().getYearSpan();
    }

    /* access modifiers changed from: package-private */
    public int getPositionForYear(int year) {
        return year - this.materialCalendar.getCalendarConstraints().getStart().year;
    }

    /* access modifiers changed from: package-private */
    public int getYearForPosition(int position) {
        return this.materialCalendar.getCalendarConstraints().getStart().year + position;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        int yearForPosition = getYearForPosition(position);
        String string = viewHolder.textView.getContext().getString(R.string.mtrl_picker_navigate_to_year_description);
        TextView textView = viewHolder.textView;
        String format = String.format(Locale.getDefault(), TimeModel.NUMBER_FORMAT, new Object[]{Integer.valueOf(yearForPosition)});
        Log1F380D.a((Object) format);
        textView.setText(format);
        TextView textView2 = viewHolder.textView;
        String format2 = String.format(string, new Object[]{Integer.valueOf(yearForPosition)});
        Log1F380D.a((Object) format2);
        textView2.setContentDescription(format2);
        CalendarStyle calendarStyle = this.materialCalendar.getCalendarStyle();
        Calendar todayCalendar = UtcDates.getTodayCalendar();
        CalendarItemStyle calendarItemStyle = todayCalendar.get(1) == yearForPosition ? calendarStyle.todayYear : calendarStyle.year;
        for (Long longValue : this.materialCalendar.getDateSelector().getSelectedDays()) {
            todayCalendar.setTimeInMillis(longValue.longValue());
            if (todayCalendar.get(1) == yearForPosition) {
                calendarItemStyle = calendarStyle.selectedYear;
            }
        }
        calendarItemStyle.styleItem(viewHolder.textView);
        viewHolder.textView.setOnClickListener(createYearClickListener(yearForPosition));
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder((TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mtrl_calendar_year, viewGroup, false));
    }
}
