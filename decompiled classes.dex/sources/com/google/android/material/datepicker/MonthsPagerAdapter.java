package com.google.android.material.datepicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.R;
import com.google.android.material.datepicker.MaterialCalendar;

class MonthsPagerAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final CalendarConstraints calendarConstraints;
    private final DateSelector<?> dateSelector;
    private final int itemHeight;
    /* access modifiers changed from: private */
    public final MaterialCalendar.OnDayClickListener onDayClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final MaterialCalendarGridView monthGrid;
        final TextView monthTitle;

        ViewHolder(LinearLayout container, boolean showLabel) {
            super(container);
            TextView textView = (TextView) container.findViewById(R.id.month_title);
            this.monthTitle = textView;
            ViewCompat.setAccessibilityHeading(textView, true);
            this.monthGrid = (MaterialCalendarGridView) container.findViewById(R.id.month_grid);
            if (!showLabel) {
                textView.setVisibility(8);
            }
        }
    }

    MonthsPagerAdapter(Context context, DateSelector<?> dateSelector2, CalendarConstraints calendarConstraints2, MaterialCalendar.OnDayClickListener onDayClickListener2) {
        Month start = calendarConstraints2.getStart();
        Month end = calendarConstraints2.getEnd();
        Month openAt = calendarConstraints2.getOpenAt();
        if (start.compareTo(openAt) > 0) {
            throw new IllegalArgumentException("firstPage cannot be after currentPage");
        } else if (openAt.compareTo(end) <= 0) {
            this.itemHeight = (MonthAdapter.MAXIMUM_WEEKS * MaterialCalendar.getDayHeight(context)) + (MaterialDatePicker.isFullscreen(context) ? MaterialCalendar.getDayHeight(context) : 0);
            this.calendarConstraints = calendarConstraints2;
            this.dateSelector = dateSelector2;
            this.onDayClickListener = onDayClickListener2;
            setHasStableIds(true);
        } else {
            throw new IllegalArgumentException("currentPage cannot be after lastPage");
        }
    }

    public int getItemCount() {
        return this.calendarConstraints.getMonthSpan();
    }

    public long getItemId(int position) {
        return this.calendarConstraints.getStart().monthsLater(position).getStableId();
    }

    /* access modifiers changed from: package-private */
    public Month getPageMonth(int position) {
        return this.calendarConstraints.getStart().monthsLater(position);
    }

    /* access modifiers changed from: package-private */
    public CharSequence getPageTitle(int position) {
        return getPageMonth(position).getLongName();
    }

    /* access modifiers changed from: package-private */
    public int getPosition(Month month) {
        return this.calendarConstraints.getStart().monthsUntil(month);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Month monthsLater = this.calendarConstraints.getStart().monthsLater(position);
        viewHolder.monthTitle.setText(monthsLater.getLongName());
        final MaterialCalendarGridView materialCalendarGridView = (MaterialCalendarGridView) viewHolder.monthGrid.findViewById(R.id.month_grid);
        if (materialCalendarGridView.getAdapter() == null || !monthsLater.equals(materialCalendarGridView.getAdapter().month)) {
            MonthAdapter monthAdapter = new MonthAdapter(monthsLater, this.dateSelector, this.calendarConstraints);
            materialCalendarGridView.setNumColumns(monthsLater.daysInWeek);
            materialCalendarGridView.setAdapter((ListAdapter) monthAdapter);
        } else {
            materialCalendarGridView.invalidate();
            materialCalendarGridView.getAdapter().updateSelectedStates(materialCalendarGridView);
        }
        materialCalendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (materialCalendarGridView.getAdapter().withinMonth(position)) {
                    MonthsPagerAdapter.this.onDayClickListener.onDayClick(materialCalendarGridView.getAdapter().getItem(position).longValue());
                }
            }
        });
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mtrl_calendar_month_labeled, viewGroup, false);
        if (!MaterialDatePicker.isFullscreen(viewGroup.getContext())) {
            return new ViewHolder(linearLayout, false);
        }
        linearLayout.setLayoutParams(new RecyclerView.LayoutParams(-1, this.itemHeight));
        return new ViewHolder(linearLayout, true);
    }
}
