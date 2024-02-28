package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import androidx.core.util.Pair;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.R;
import com.google.android.material.button.MaterialButton;
import java.util.Calendar;
import java.util.Iterator;

public final class MaterialCalendar<S> extends PickerFragment<S> {
    private static final String CALENDAR_CONSTRAINTS_KEY = "CALENDAR_CONSTRAINTS_KEY";
    private static final String CURRENT_MONTH_KEY = "CURRENT_MONTH_KEY";
    private static final String GRID_SELECTOR_KEY = "GRID_SELECTOR_KEY";
    static final Object MONTHS_VIEW_GROUP_TAG = "MONTHS_VIEW_GROUP_TAG";
    static final Object NAVIGATION_NEXT_TAG = "NAVIGATION_NEXT_TAG";
    static final Object NAVIGATION_PREV_TAG = "NAVIGATION_PREV_TAG";
    static final Object SELECTOR_TOGGLE_TAG = "SELECTOR_TOGGLE_TAG";
    private static final int SMOOTH_SCROLL_MAX = 3;
    private static final String THEME_RES_ID_KEY = "THEME_RES_ID_KEY";
    /* access modifiers changed from: private */
    public CalendarConstraints calendarConstraints;
    private CalendarSelector calendarSelector;
    /* access modifiers changed from: private */
    public CalendarStyle calendarStyle;
    /* access modifiers changed from: private */
    public Month current;
    /* access modifiers changed from: private */
    public DateSelector<S> dateSelector;
    /* access modifiers changed from: private */
    public View dayFrame;
    /* access modifiers changed from: private */
    public RecyclerView recyclerView;
    private int themeResId;
    private View yearFrame;
    /* access modifiers changed from: private */
    public RecyclerView yearSelector;

    enum CalendarSelector {
        DAY,
        YEAR
    }

    interface OnDayClickListener {
        void onDayClick(long j);
    }

    private void addActionsToMonthNavigation(View root, final MonthsPagerAdapter monthsPagerAdapter) {
        final MaterialButton materialButton = (MaterialButton) root.findViewById(R.id.month_navigation_fragment_toggle);
        materialButton.setTag(SELECTOR_TOGGLE_TAG);
        ViewCompat.setAccessibilityDelegate(materialButton, new AccessibilityDelegateCompat() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                accessibilityNodeInfoCompat.setHintText(MaterialCalendar.this.dayFrame.getVisibility() == 0 ? MaterialCalendar.this.getString(R.string.mtrl_picker_toggle_to_year_selection) : MaterialCalendar.this.getString(R.string.mtrl_picker_toggle_to_day_selection));
            }
        });
        MaterialButton materialButton2 = (MaterialButton) root.findViewById(R.id.month_navigation_previous);
        materialButton2.setTag(NAVIGATION_PREV_TAG);
        MaterialButton materialButton3 = (MaterialButton) root.findViewById(R.id.month_navigation_next);
        materialButton3.setTag(NAVIGATION_NEXT_TAG);
        this.yearFrame = root.findViewById(R.id.mtrl_calendar_year_selector_frame);
        this.dayFrame = root.findViewById(R.id.mtrl_calendar_day_selector_frame);
        setSelector(CalendarSelector.DAY);
        materialButton.setText(this.current.getLongName());
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == 0) {
                    CharSequence text = materialButton.getText();
                    if (Build.VERSION.SDK_INT >= 16) {
                        recyclerView.announceForAccessibility(text);
                    } else {
                        recyclerView.sendAccessibilityEvent(2048);
                    }
                }
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int findFirstVisibleItemPosition = dx < 0 ? MaterialCalendar.this.getLayoutManager().findFirstVisibleItemPosition() : MaterialCalendar.this.getLayoutManager().findLastVisibleItemPosition();
                Month unused = MaterialCalendar.this.current = monthsPagerAdapter.getPageMonth(findFirstVisibleItemPosition);
                materialButton.setText(monthsPagerAdapter.getPageTitle(findFirstVisibleItemPosition));
            }
        });
        materialButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MaterialCalendar.this.toggleVisibleSelector();
            }
        });
        materialButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int findFirstVisibleItemPosition = MaterialCalendar.this.getLayoutManager().findFirstVisibleItemPosition();
                if (findFirstVisibleItemPosition + 1 < MaterialCalendar.this.recyclerView.getAdapter().getItemCount()) {
                    MaterialCalendar.this.setCurrentMonth(monthsPagerAdapter.getPageMonth(findFirstVisibleItemPosition + 1));
                }
            }
        });
        materialButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int findLastVisibleItemPosition = MaterialCalendar.this.getLayoutManager().findLastVisibleItemPosition();
                if (findLastVisibleItemPosition - 1 >= 0) {
                    MaterialCalendar.this.setCurrentMonth(monthsPagerAdapter.getPageMonth(findLastVisibleItemPosition - 1));
                }
            }
        });
    }

    private RecyclerView.ItemDecoration createItemDecoration() {
        return new RecyclerView.ItemDecoration() {
            private final Calendar endItem = UtcDates.getUtcCalendar();
            private final Calendar startItem = UtcDates.getUtcCalendar();

            public void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
                int i;
                Iterator<Pair<Long, Long>> it;
                Pair pair;
                GridLayoutManager gridLayoutManager;
                YearGridAdapter yearGridAdapter;
                if ((recyclerView.getAdapter() instanceof YearGridAdapter) && (recyclerView.getLayoutManager() instanceof GridLayoutManager)) {
                    YearGridAdapter yearGridAdapter2 = (YearGridAdapter) recyclerView.getAdapter();
                    GridLayoutManager gridLayoutManager2 = (GridLayoutManager) recyclerView.getLayoutManager();
                    Iterator<Pair<Long, Long>> it2 = MaterialCalendar.this.dateSelector.getSelectedRanges().iterator();
                    while (it2.hasNext()) {
                        Pair next = it2.next();
                        if (next.first == null) {
                            YearGridAdapter yearGridAdapter3 = yearGridAdapter2;
                            GridLayoutManager gridLayoutManager3 = gridLayoutManager2;
                            Iterator<Pair<Long, Long>> it3 = it2;
                            Pair pair2 = next;
                        } else if (next.second != null) {
                            this.startItem.setTimeInMillis(((Long) next.first).longValue());
                            this.endItem.setTimeInMillis(((Long) next.second).longValue());
                            int positionForYear = yearGridAdapter2.getPositionForYear(this.startItem.get(1));
                            int positionForYear2 = yearGridAdapter2.getPositionForYear(this.endItem.get(1));
                            View findViewByPosition = gridLayoutManager2.findViewByPosition(positionForYear);
                            View findViewByPosition2 = gridLayoutManager2.findViewByPosition(positionForYear2);
                            int spanCount = positionForYear / gridLayoutManager2.getSpanCount();
                            int spanCount2 = positionForYear2 / gridLayoutManager2.getSpanCount();
                            int i2 = spanCount;
                            while (i2 <= spanCount2) {
                                View findViewByPosition3 = gridLayoutManager2.findViewByPosition(gridLayoutManager2.getSpanCount() * i2);
                                if (findViewByPosition3 == null) {
                                    yearGridAdapter = yearGridAdapter2;
                                    gridLayoutManager = gridLayoutManager2;
                                    it = it2;
                                    pair = next;
                                    i = positionForYear;
                                } else {
                                    int top = findViewByPosition3.getTop() + MaterialCalendar.this.calendarStyle.year.getTopInset();
                                    yearGridAdapter = yearGridAdapter2;
                                    int bottom = findViewByPosition3.getBottom() - MaterialCalendar.this.calendarStyle.year.getBottomInset();
                                    int left = i2 == spanCount ? findViewByPosition.getLeft() + (findViewByPosition.getWidth() / 2) : 0;
                                    gridLayoutManager = gridLayoutManager2;
                                    int i3 = left;
                                    it = it2;
                                    int left2 = i2 == spanCount2 ? findViewByPosition2.getLeft() + (findViewByPosition2.getWidth() / 2) : recyclerView.getWidth();
                                    pair = next;
                                    int i4 = left2;
                                    i = positionForYear;
                                    canvas.drawRect((float) left, (float) top, (float) left2, (float) bottom, MaterialCalendar.this.calendarStyle.rangeFill);
                                }
                                i2++;
                                yearGridAdapter2 = yearGridAdapter;
                                gridLayoutManager2 = gridLayoutManager;
                                next = pair;
                                it2 = it;
                                positionForYear = i;
                            }
                            YearGridAdapter yearGridAdapter4 = yearGridAdapter2;
                            GridLayoutManager gridLayoutManager4 = gridLayoutManager2;
                            Iterator<Pair<Long, Long>> it4 = it2;
                            Pair pair3 = next;
                            int i5 = positionForYear;
                        }
                    }
                }
            }
        };
    }

    static int getDayHeight(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.mtrl_calendar_day_height);
    }

    private static int getDialogPickerHeight(Context context) {
        Resources resources = context.getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.mtrl_calendar_navigation_height) + resources.getDimensionPixelOffset(R.dimen.mtrl_calendar_navigation_top_padding) + resources.getDimensionPixelOffset(R.dimen.mtrl_calendar_navigation_bottom_padding);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.mtrl_calendar_days_of_week_height);
        return dimensionPixelSize + dimensionPixelSize2 + (MonthAdapter.MAXIMUM_WEEKS * resources.getDimensionPixelSize(R.dimen.mtrl_calendar_day_height)) + ((MonthAdapter.MAXIMUM_WEEKS - 1) * resources.getDimensionPixelOffset(R.dimen.mtrl_calendar_month_vertical_padding)) + resources.getDimensionPixelOffset(R.dimen.mtrl_calendar_bottom_padding);
    }

    public static <T> MaterialCalendar<T> newInstance(DateSelector<T> dateSelector2, int themeResId2, CalendarConstraints calendarConstraints2) {
        MaterialCalendar<T> materialCalendar = new MaterialCalendar<>();
        Bundle bundle = new Bundle();
        bundle.putInt(THEME_RES_ID_KEY, themeResId2);
        bundle.putParcelable(GRID_SELECTOR_KEY, dateSelector2);
        bundle.putParcelable(CALENDAR_CONSTRAINTS_KEY, calendarConstraints2);
        bundle.putParcelable(CURRENT_MONTH_KEY, calendarConstraints2.getOpenAt());
        materialCalendar.setArguments(bundle);
        return materialCalendar;
    }

    private void postSmoothRecyclerViewScroll(final int position) {
        this.recyclerView.post(new Runnable() {
            public void run() {
                MaterialCalendar.this.recyclerView.smoothScrollToPosition(position);
            }
        });
    }

    public boolean addOnSelectionChangedListener(OnSelectionChangedListener<S> onSelectionChangedListener) {
        return super.addOnSelectionChangedListener(onSelectionChangedListener);
    }

    /* access modifiers changed from: package-private */
    public CalendarConstraints getCalendarConstraints() {
        return this.calendarConstraints;
    }

    /* access modifiers changed from: package-private */
    public CalendarStyle getCalendarStyle() {
        return this.calendarStyle;
    }

    /* access modifiers changed from: package-private */
    public Month getCurrentMonth() {
        return this.current;
    }

    public DateSelector<S> getDateSelector() {
        return this.dateSelector;
    }

    /* access modifiers changed from: package-private */
    public LinearLayoutManager getLayoutManager() {
        return (LinearLayoutManager) this.recyclerView.getLayoutManager();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = bundle == null ? getArguments() : bundle;
        this.themeResId = arguments.getInt(THEME_RES_ID_KEY);
        this.dateSelector = (DateSelector) arguments.getParcelable(GRID_SELECTOR_KEY);
        this.calendarConstraints = (CalendarConstraints) arguments.getParcelable(CALENDAR_CONSTRAINTS_KEY);
        this.current = (Month) arguments.getParcelable(CURRENT_MONTH_KEY);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        int i;
        int i2;
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(), this.themeResId);
        this.calendarStyle = new CalendarStyle(contextThemeWrapper);
        LayoutInflater cloneInContext = layoutInflater.cloneInContext(contextThemeWrapper);
        Month start = this.calendarConstraints.getStart();
        if (MaterialDatePicker.isFullscreen(contextThemeWrapper)) {
            i2 = R.layout.mtrl_calendar_vertical;
            i = 1;
        } else {
            i2 = R.layout.mtrl_calendar_horizontal;
            i = 0;
        }
        View inflate = cloneInContext.inflate(i2, viewGroup, false);
        inflate.setMinimumHeight(getDialogPickerHeight(requireContext()));
        GridView gridView = (GridView) inflate.findViewById(R.id.mtrl_calendar_days_of_week);
        ViewCompat.setAccessibilityDelegate(gridView, new AccessibilityDelegateCompat() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                accessibilityNodeInfoCompat.setCollectionInfo((Object) null);
            }
        });
        gridView.setAdapter(new DaysOfWeekAdapter());
        gridView.setNumColumns(start.daysInWeek);
        gridView.setEnabled(false);
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.mtrl_calendar_months);
        GridView gridView2 = gridView;
        final int i3 = i;
        this.recyclerView.setLayoutManager(new SmoothCalendarLayoutManager(getContext(), i, false) {
            /* access modifiers changed from: protected */
            public void calculateExtraLayoutSpace(RecyclerView.State state, int[] ints) {
                if (i3 == 0) {
                    ints[0] = MaterialCalendar.this.recyclerView.getWidth();
                    ints[1] = MaterialCalendar.this.recyclerView.getWidth();
                    return;
                }
                ints[0] = MaterialCalendar.this.recyclerView.getHeight();
                ints[1] = MaterialCalendar.this.recyclerView.getHeight();
            }
        });
        this.recyclerView.setTag(MONTHS_VIEW_GROUP_TAG);
        MonthsPagerAdapter monthsPagerAdapter = new MonthsPagerAdapter(contextThemeWrapper, this.dateSelector, this.calendarConstraints, new OnDayClickListener() {
            public void onDayClick(long day) {
                if (MaterialCalendar.this.calendarConstraints.getDateValidator().isValid(day)) {
                    MaterialCalendar.this.dateSelector.select(day);
                    Iterator it = MaterialCalendar.this.onSelectionChangedListeners.iterator();
                    while (it.hasNext()) {
                        ((OnSelectionChangedListener) it.next()).onSelectionChanged(MaterialCalendar.this.dateSelector.getSelection());
                    }
                    MaterialCalendar.this.recyclerView.getAdapter().notifyDataSetChanged();
                    if (MaterialCalendar.this.yearSelector != null) {
                        MaterialCalendar.this.yearSelector.getAdapter().notifyDataSetChanged();
                    }
                }
            }
        });
        this.recyclerView.setAdapter(monthsPagerAdapter);
        int integer = contextThemeWrapper.getResources().getInteger(R.integer.mtrl_calendar_year_selector_span);
        RecyclerView recyclerView2 = (RecyclerView) inflate.findViewById(R.id.mtrl_calendar_year_selector_frame);
        this.yearSelector = recyclerView2;
        if (recyclerView2 != null) {
            recyclerView2.setHasFixedSize(true);
            this.yearSelector.setLayoutManager(new GridLayoutManager((Context) contextThemeWrapper, integer, 1, false));
            this.yearSelector.setAdapter(new YearGridAdapter(this));
            this.yearSelector.addItemDecoration(createItemDecoration());
        }
        if (inflate.findViewById(R.id.month_navigation_fragment_toggle) != null) {
            addActionsToMonthNavigation(inflate, monthsPagerAdapter);
        }
        if (!MaterialDatePicker.isFullscreen(contextThemeWrapper)) {
            new PagerSnapHelper().attachToRecyclerView(this.recyclerView);
        }
        this.recyclerView.scrollToPosition(monthsPagerAdapter.getPosition(this.current));
        return inflate;
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(THEME_RES_ID_KEY, this.themeResId);
        bundle.putParcelable(GRID_SELECTOR_KEY, this.dateSelector);
        bundle.putParcelable(CALENDAR_CONSTRAINTS_KEY, this.calendarConstraints);
        bundle.putParcelable(CURRENT_MONTH_KEY, this.current);
    }

    /* access modifiers changed from: package-private */
    public void setCurrentMonth(Month moveTo) {
        MonthsPagerAdapter monthsPagerAdapter = (MonthsPagerAdapter) this.recyclerView.getAdapter();
        int position = monthsPagerAdapter.getPosition(moveTo);
        int position2 = position - monthsPagerAdapter.getPosition(this.current);
        boolean z = true;
        boolean z2 = Math.abs(position2) > 3;
        if (position2 <= 0) {
            z = false;
        }
        this.current = moveTo;
        if (z2 && z) {
            this.recyclerView.scrollToPosition(position - 3);
            postSmoothRecyclerViewScroll(position);
        } else if (z2) {
            this.recyclerView.scrollToPosition(position + 3);
            postSmoothRecyclerViewScroll(position);
        } else {
            postSmoothRecyclerViewScroll(position);
        }
    }

    /* access modifiers changed from: package-private */
    public void setSelector(CalendarSelector selector) {
        this.calendarSelector = selector;
        if (selector == CalendarSelector.YEAR) {
            this.yearSelector.getLayoutManager().scrollToPosition(((YearGridAdapter) this.yearSelector.getAdapter()).getPositionForYear(this.current.year));
            this.yearFrame.setVisibility(0);
            this.dayFrame.setVisibility(8);
        } else if (selector == CalendarSelector.DAY) {
            this.yearFrame.setVisibility(8);
            this.dayFrame.setVisibility(0);
            setCurrentMonth(this.current);
        }
    }

    /* access modifiers changed from: package-private */
    public void toggleVisibleSelector() {
        if (this.calendarSelector == CalendarSelector.YEAR) {
            setSelector(CalendarSelector.DAY);
        } else if (this.calendarSelector == CalendarSelector.DAY) {
            setSelector(CalendarSelector.YEAR);
        }
    }
}
