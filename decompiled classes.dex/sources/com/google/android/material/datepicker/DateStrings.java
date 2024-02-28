package com.google.android.material.datepicker;

import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;
import androidx.core.util.Pair;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import mt.Log1F380D;

/* compiled from: 00ED */
class DateStrings {
    private DateStrings() {
    }

    static Pair<String, String> getDateRangeString(Long start, Long end) {
        return getDateRangeString(start, end, (SimpleDateFormat) null);
    }

    static Pair<String, String> getDateRangeString(Long start, Long end, SimpleDateFormat userDefinedDateFormat) {
        if (start == null && end == null) {
            return Pair.create(null, null);
        }
        if (start == null) {
            String dateString = getDateString(end.longValue(), userDefinedDateFormat);
            Log1F380D.a((Object) dateString);
            return Pair.create(null, dateString);
        } else if (end == null) {
            String dateString2 = getDateString(start.longValue(), userDefinedDateFormat);
            Log1F380D.a((Object) dateString2);
            return Pair.create(dateString2, null);
        } else {
            Calendar todayCalendar = UtcDates.getTodayCalendar();
            Calendar utcCalendar = UtcDates.getUtcCalendar();
            utcCalendar.setTimeInMillis(start.longValue());
            Calendar utcCalendar2 = UtcDates.getUtcCalendar();
            utcCalendar2.setTimeInMillis(end.longValue());
            if (userDefinedDateFormat != null) {
                return Pair.create(userDefinedDateFormat.format(new Date(start.longValue())), userDefinedDateFormat.format(new Date(end.longValue())));
            } else if (utcCalendar.get(1) != utcCalendar2.get(1)) {
                String yearMonthDay = getYearMonthDay(start.longValue(), Locale.getDefault());
                Log1F380D.a((Object) yearMonthDay);
                String yearMonthDay2 = getYearMonthDay(end.longValue(), Locale.getDefault());
                Log1F380D.a((Object) yearMonthDay2);
                return Pair.create(yearMonthDay, yearMonthDay2);
            } else if (utcCalendar.get(1) == todayCalendar.get(1)) {
                String monthDay = getMonthDay(start.longValue(), Locale.getDefault());
                Log1F380D.a((Object) monthDay);
                String monthDay2 = getMonthDay(end.longValue(), Locale.getDefault());
                Log1F380D.a((Object) monthDay2);
                return Pair.create(monthDay, monthDay2);
            } else {
                String monthDay3 = getMonthDay(start.longValue(), Locale.getDefault());
                Log1F380D.a((Object) monthDay3);
                String yearMonthDay3 = getYearMonthDay(end.longValue(), Locale.getDefault());
                Log1F380D.a((Object) yearMonthDay3);
                return Pair.create(monthDay3, yearMonthDay3);
            }
        }
    }

    static String getMonthDay(long timeInMillis, Locale locale) {
        return Build.VERSION.SDK_INT >= 24 ? UtcDates.getAbbrMonthDayFormat(locale).format(new Date(timeInMillis)) : UtcDates.getMediumNoYear(locale).format(new Date(timeInMillis));
    }

    static String getMonthDayOfWeekDay(long timeInMillis, Locale locale) {
        return Build.VERSION.SDK_INT >= 24 ? UtcDates.getAbbrMonthWeekdayDayFormat(locale).format(new Date(timeInMillis)) : UtcDates.getFullFormat(locale).format(new Date(timeInMillis));
    }

    static String getYearMonthDay(long timeInMillis, Locale locale) {
        return Build.VERSION.SDK_INT >= 24 ? UtcDates.getYearAbbrMonthDayFormat(locale).format(new Date(timeInMillis)) : UtcDates.getMediumFormat(locale).format(new Date(timeInMillis));
    }

    static String getYearMonthDayOfWeekDay(long timeInMillis, Locale locale) {
        return Build.VERSION.SDK_INT >= 24 ? UtcDates.getYearAbbrMonthWeekdayDayFormat(locale).format(new Date(timeInMillis)) : UtcDates.getFullFormat(locale).format(new Date(timeInMillis));
    }

    static String getDateString(long timeInMillis) {
        String dateString = getDateString(timeInMillis, (SimpleDateFormat) null);
        Log1F380D.a((Object) dateString);
        return dateString;
    }

    static String getDateString(long timeInMillis, SimpleDateFormat userDefinedDateFormat) {
        Calendar todayCalendar = UtcDates.getTodayCalendar();
        Calendar utcCalendar = UtcDates.getUtcCalendar();
        utcCalendar.setTimeInMillis(timeInMillis);
        if (userDefinedDateFormat != null) {
            return userDefinedDateFormat.format(new Date(timeInMillis));
        }
        if (todayCalendar.get(1) == utcCalendar.get(1)) {
            String monthDay = getMonthDay(timeInMillis);
            Log1F380D.a((Object) monthDay);
            return monthDay;
        }
        String yearMonthDay = getYearMonthDay(timeInMillis);
        Log1F380D.a((Object) yearMonthDay);
        return yearMonthDay;
    }

    static String getMonthDay(long timeInMillis) {
        String monthDay = getMonthDay(timeInMillis, Locale.getDefault());
        Log1F380D.a((Object) monthDay);
        return monthDay;
    }

    static String getMonthDayOfWeekDay(long timeInMillis) {
        String monthDayOfWeekDay = getMonthDayOfWeekDay(timeInMillis, Locale.getDefault());
        Log1F380D.a((Object) monthDayOfWeekDay);
        return monthDayOfWeekDay;
    }

    static String getYearMonth(long timeInMillis) {
        String formatDateTime = DateUtils.formatDateTime((Context) null, timeInMillis, 8228);
        Log1F380D.a((Object) formatDateTime);
        return formatDateTime;
    }

    static String getYearMonthDay(long timeInMillis) {
        String yearMonthDay = getYearMonthDay(timeInMillis, Locale.getDefault());
        Log1F380D.a((Object) yearMonthDay);
        return yearMonthDay;
    }

    static String getYearMonthDayOfWeekDay(long timeInMillis) {
        String yearMonthDayOfWeekDay = getYearMonthDayOfWeekDay(timeInMillis, Locale.getDefault());
        Log1F380D.a((Object) yearMonthDayOfWeekDay);
        return yearMonthDayOfWeekDay;
    }
}
