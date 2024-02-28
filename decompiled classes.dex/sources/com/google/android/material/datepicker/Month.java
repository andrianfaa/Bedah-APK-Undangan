package com.google.android.material.datepicker;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import mt.Log1F380D;

/* compiled from: 00F1 */
final class Month implements Comparable<Month>, Parcelable {
    public static final Parcelable.Creator<Month> CREATOR = new Parcelable.Creator<Month>() {
        public Month createFromParcel(Parcel source) {
            return Month.create(source.readInt(), source.readInt());
        }

        public Month[] newArray(int size) {
            return new Month[size];
        }
    };
    final int daysInMonth;
    final int daysInWeek;
    private final Calendar firstOfMonth;
    private String longName;
    final int month;
    final long timeInMillis;
    final int year;

    private Month(Calendar rawCalendar) {
        rawCalendar.set(5, 1);
        Calendar dayCopy = UtcDates.getDayCopy(rawCalendar);
        this.firstOfMonth = dayCopy;
        this.month = dayCopy.get(2);
        this.year = dayCopy.get(1);
        this.daysInWeek = dayCopy.getMaximum(7);
        this.daysInMonth = dayCopy.getActualMaximum(5);
        this.timeInMillis = dayCopy.getTimeInMillis();
    }

    static Month create(int year2, int month2) {
        Calendar utcCalendar = UtcDates.getUtcCalendar();
        utcCalendar.set(1, year2);
        utcCalendar.set(2, month2);
        return new Month(utcCalendar);
    }

    static Month create(long timeInMillis2) {
        Calendar utcCalendar = UtcDates.getUtcCalendar();
        utcCalendar.setTimeInMillis(timeInMillis2);
        return new Month(utcCalendar);
    }

    static Month current() {
        return new Month(UtcDates.getTodayCalendar());
    }

    public int compareTo(Month other) {
        return this.firstOfMonth.compareTo(other.firstOfMonth);
    }

    /* access modifiers changed from: package-private */
    public int daysFromStartOfWeekToFirstOfMonth() {
        int firstDayOfWeek = this.firstOfMonth.get(7) - this.firstOfMonth.getFirstDayOfWeek();
        return firstDayOfWeek < 0 ? firstDayOfWeek + this.daysInWeek : firstDayOfWeek;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Month)) {
            return false;
        }
        Month month2 = (Month) o;
        return this.month == month2.month && this.year == month2.year;
    }

    /* access modifiers changed from: package-private */
    public long getDay(int day) {
        Calendar dayCopy = UtcDates.getDayCopy(this.firstOfMonth);
        dayCopy.set(5, day);
        return dayCopy.getTimeInMillis();
    }

    /* access modifiers changed from: package-private */
    public int getDayOfMonth(long date) {
        Calendar dayCopy = UtcDates.getDayCopy(this.firstOfMonth);
        dayCopy.setTimeInMillis(date);
        return dayCopy.get(5);
    }

    /* access modifiers changed from: package-private */
    public String getLongName() {
        if (this.longName == null) {
            String yearMonth = DateStrings.getYearMonth(this.firstOfMonth.getTimeInMillis());
            Log1F380D.a((Object) yearMonth);
            this.longName = yearMonth;
        }
        return this.longName;
    }

    /* access modifiers changed from: package-private */
    public long getStableId() {
        return this.firstOfMonth.getTimeInMillis();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.month), Integer.valueOf(this.year)});
    }

    /* access modifiers changed from: package-private */
    public Month monthsLater(int months) {
        Calendar dayCopy = UtcDates.getDayCopy(this.firstOfMonth);
        dayCopy.add(2, months);
        return new Month(dayCopy);
    }

    /* access modifiers changed from: package-private */
    public int monthsUntil(Month other) {
        if (this.firstOfMonth instanceof GregorianCalendar) {
            return ((other.year - this.year) * 12) + (other.month - this.month);
        }
        throw new IllegalArgumentException("Only Gregorian calendars are supported.");
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.year);
        dest.writeInt(this.month);
    }
}
