package com.google.android.material.datepicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;
import androidx.core.util.Pair;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R;
import com.google.android.material.internal.ViewUtils;
import java.util.Calendar;
import mt.Log1F380D;

/* compiled from: 00EF */
final class MaterialCalendarGridView extends GridView {
    private final Calendar dayCompute;
    private final boolean nestedScrollable;

    public MaterialCalendarGridView(Context context) {
        this(context, (AttributeSet) null);
    }

    public MaterialCalendarGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialCalendarGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.dayCompute = UtcDates.getUtcCalendar();
        if (MaterialDatePicker.isFullscreen(getContext())) {
            setNextFocusLeftId(R.id.cancel_button);
            setNextFocusRightId(R.id.confirm_button);
        }
        this.nestedScrollable = MaterialDatePicker.isNestedScrollable(getContext());
        ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegateCompat() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                accessibilityNodeInfoCompat.setCollectionInfo((Object) null);
            }
        });
    }

    private void gainFocus(int direction, Rect previouslyFocusedRect) {
        if (direction == 33) {
            setSelection(getAdapter().lastPositionInMonth());
        } else if (direction == 130) {
            setSelection(getAdapter().firstPositionInMonth());
        } else {
            super.onFocusChanged(true, direction, previouslyFocusedRect);
        }
    }

    private View getChildAtPosition(int position) {
        return getChildAt(position - getFirstVisiblePosition());
    }

    private static int horizontalMidPoint(View view) {
        return view.getLeft() + (view.getWidth() / 2);
    }

    private static boolean skipMonth(Long firstOfMonth, Long lastOfMonth, Long startDay, Long endDay) {
        return firstOfMonth == null || lastOfMonth == null || startDay == null || endDay == null || startDay.longValue() > lastOfMonth.longValue() || endDay.longValue() < firstOfMonth.longValue();
    }

    public MonthAdapter getAdapter() {
        return (MonthAdapter) super.getAdapter();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getAdapter().notifyDataSetChanged();
    }

    /* access modifiers changed from: protected */
    public final void onDraw(Canvas canvas) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        MaterialCalendarGridView materialCalendarGridView = this;
        super.onDraw(canvas);
        MonthAdapter adapter = getAdapter();
        DateSelector<?> dateSelector = adapter.dateSelector;
        CalendarStyle calendarStyle = adapter.calendarStyle;
        int max = Math.max(adapter.firstPositionInMonth(), getFirstVisiblePosition());
        int min = Math.min(adapter.lastPositionInMonth(), getLastVisiblePosition());
        Long item = adapter.getItem(max);
        Long item2 = adapter.getItem(min);
        for (Pair next : dateSelector.getSelectedRanges()) {
            if (next.first == null) {
                MonthAdapter monthAdapter = adapter;
                DateSelector<?> dateSelector2 = dateSelector;
                int i9 = max;
                int i10 = min;
                Long l = item;
                materialCalendarGridView = this;
            } else if (next.second != null) {
                long longValue = ((Long) next.first).longValue();
                long longValue2 = ((Long) next.second).longValue();
                if (!skipMonth(item, item2, Long.valueOf(longValue), Long.valueOf(longValue2))) {
                    boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
                    DateSelector<?> dateSelector3 = dateSelector;
                    if (longValue < item.longValue()) {
                        i = max;
                        i2 = adapter.isFirstInRow(i) ? 0 : !isLayoutRtl ? materialCalendarGridView.getChildAtPosition(i - 1).getRight() : materialCalendarGridView.getChildAtPosition(i - 1).getLeft();
                    } else {
                        materialCalendarGridView.dayCompute.setTimeInMillis(longValue);
                        i = adapter.dayToPosition(materialCalendarGridView.dayCompute.get(5));
                        i2 = horizontalMidPoint(materialCalendarGridView.getChildAtPosition(i));
                    }
                    if (longValue2 > item2.longValue()) {
                        i4 = i2;
                        i6 = min;
                        i5 = adapter.isLastInRow(i6) ? getWidth() : !isLayoutRtl ? materialCalendarGridView.getChildAtPosition(i6).getRight() : materialCalendarGridView.getChildAtPosition(i6).getLeft();
                        i3 = max;
                    } else {
                        i4 = i2;
                        materialCalendarGridView.dayCompute.setTimeInMillis(longValue2);
                        i3 = max;
                        i6 = adapter.dayToPosition(materialCalendarGridView.dayCompute.get(5));
                        i5 = horizontalMidPoint(materialCalendarGridView.getChildAtPosition(i6));
                    }
                    int i11 = min;
                    int itemId = (int) adapter.getItemId(i);
                    Long l2 = item;
                    int itemId2 = (int) adapter.getItemId(i6);
                    int i12 = itemId;
                    while (i12 <= itemId2) {
                        MonthAdapter monthAdapter2 = adapter;
                        int numColumns = i12 * getNumColumns();
                        int i13 = itemId;
                        int numColumns2 = (numColumns + getNumColumns()) - 1;
                        View childAtPosition = materialCalendarGridView.getChildAtPosition(numColumns);
                        int top = childAtPosition.getTop() + calendarStyle.day.getTopInset();
                        int i14 = itemId2;
                        int bottom = childAtPosition.getBottom() - calendarStyle.day.getBottomInset();
                        if (!isLayoutRtl) {
                            i8 = numColumns > i ? 0 : i4;
                            i7 = i6 > numColumns2 ? getWidth() : i5;
                        } else {
                            i8 = i6 > numColumns2 ? 0 : i5;
                            i7 = numColumns > i ? getWidth() : i4;
                        }
                        int i15 = numColumns;
                        int i16 = i8;
                        int i17 = i6;
                        int i18 = i7;
                        int i19 = numColumns2;
                        int i20 = i16;
                        int i21 = top;
                        int i22 = i18;
                        int i23 = bottom;
                        canvas.drawRect((float) i16, (float) top, (float) i18, (float) bottom, calendarStyle.rangeFill);
                        i12++;
                        materialCalendarGridView = this;
                        adapter = monthAdapter2;
                        itemId = i13;
                        i6 = i17;
                        itemId2 = i14;
                    }
                    MonthAdapter monthAdapter3 = adapter;
                    int i24 = i6;
                    int i25 = itemId;
                    int i26 = itemId2;
                    materialCalendarGridView = this;
                    dateSelector = dateSelector3;
                    max = i3;
                    min = i11;
                    item = l2;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (gainFocus) {
            gainFocus(direction, previouslyFocusedRect);
        } else {
            super.onFocusChanged(false, direction, previouslyFocusedRect);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!super.onKeyDown(keyCode, event)) {
            return false;
        }
        if (getSelectedItemPosition() == -1 || getSelectedItemPosition() >= getAdapter().firstPositionInMonth()) {
            return true;
        }
        if (19 != keyCode) {
            return false;
        }
        setSelection(getAdapter().firstPositionInMonth());
        return true;
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.nestedScrollable) {
            super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(ViewCompat.MEASURED_SIZE_MASK, Integer.MIN_VALUE));
            getLayoutParams().height = getMeasuredHeight();
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public final void setAdapter(ListAdapter adapter) {
        if (adapter instanceof MonthAdapter) {
            super.setAdapter(adapter);
            return;
        }
        String format = String.format("%1$s must have its Adapter set to a %2$s", new Object[]{MaterialCalendarGridView.class.getCanonicalName(), MonthAdapter.class.getCanonicalName()});
        Log1F380D.a((Object) format);
        throw new IllegalArgumentException(format);
    }

    public void setSelection(int position) {
        if (position < getAdapter().firstPositionInMonth()) {
            super.setSelection(getAdapter().firstPositionInMonth());
        } else {
            super.setSelection(position);
        }
    }
}
