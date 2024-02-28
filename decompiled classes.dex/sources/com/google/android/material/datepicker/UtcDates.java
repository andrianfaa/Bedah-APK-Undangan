package com.google.android.material.datepicker;

import android.content.res.Resources;
import android.icu.text.DateFormat;
import com.google.android.material.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;
import mt.Log1F380D;
import okhttp3.HttpUrl;

/* compiled from: 00F5 */
class UtcDates {
    static final String UTC = "UTC";
    static AtomicReference<TimeSource> timeSourceRef = new AtomicReference<>();

    private UtcDates() {
    }

    static long canonicalYearMonthDay(long rawDate) {
        Calendar utcCalendar = getUtcCalendar();
        utcCalendar.setTimeInMillis(rawDate);
        return getDayCopy(utcCalendar).getTimeInMillis();
    }

    private static int findCharactersInDateFormatPattern(String pattern, String characterSequence, int increment, int initialPosition) {
        int i;
        int i2 = initialPosition;
        while (i >= 0 && i < pattern.length() && characterSequence.indexOf(pattern.charAt(i)) == -1) {
            if (pattern.charAt(i) == '\'') {
                i += increment;
                while (i >= 0 && i < pattern.length() && pattern.charAt(i) != '\'') {
                    i += increment;
                }
            }
            i2 = i + increment;
        }
        return i;
    }

    static DateFormat getAbbrMonthDayFormat(Locale locale) {
        return getAndroidFormat("MMMd", locale);
    }

    static DateFormat getAbbrMonthWeekdayDayFormat(Locale locale) {
        return getAndroidFormat("MMMEd", locale);
    }

    private static DateFormat getAndroidFormat(String pattern, Locale locale) {
        DateFormat instanceForSkeleton = DateFormat.getInstanceForSkeleton(pattern, locale);
        instanceForSkeleton.setTimeZone(getUtcAndroidTimeZone());
        return instanceForSkeleton;
    }

    static Calendar getDayCopy(Calendar rawCalendar) {
        Calendar utcCalendarOf = getUtcCalendarOf(rawCalendar);
        Calendar utcCalendar = getUtcCalendar();
        utcCalendar.set(utcCalendarOf.get(1), utcCalendarOf.get(2), utcCalendarOf.get(5));
        return utcCalendar;
    }

    private static java.text.DateFormat getFormat(int style, Locale locale) {
        java.text.DateFormat dateInstance = java.text.DateFormat.getDateInstance(style, locale);
        dateInstance.setTimeZone(getTimeZone());
        return dateInstance;
    }

    static java.text.DateFormat getFullFormat() {
        return getFullFormat(Locale.getDefault());
    }

    static java.text.DateFormat getFullFormat(Locale locale) {
        return getFormat(0, locale);
    }

    static java.text.DateFormat getMediumFormat() {
        return getMediumFormat(Locale.getDefault());
    }

    static java.text.DateFormat getMediumFormat(Locale locale) {
        return getFormat(2, locale);
    }

    static java.text.DateFormat getMediumNoYear() {
        return getMediumNoYear(Locale.getDefault());
    }

    static java.text.DateFormat getMediumNoYear(Locale locale) {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) getMediumFormat(locale);
        String removeYearFromDateFormatPattern = removeYearFromDateFormatPattern(simpleDateFormat.toPattern());
        Log1F380D.a((Object) removeYearFromDateFormatPattern);
        simpleDateFormat.applyPattern(removeYearFromDateFormatPattern);
        return simpleDateFormat;
    }

    static SimpleDateFormat getSimpleFormat(String pattern) {
        return getSimpleFormat(pattern, Locale.getDefault());
    }

    private static SimpleDateFormat getSimpleFormat(String pattern, Locale locale) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
        simpleDateFormat.setTimeZone(getTimeZone());
        return simpleDateFormat;
    }

    static SimpleDateFormat getTextInputFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(((SimpleDateFormat) java.text.DateFormat.getDateInstance(3, Locale.getDefault())).toPattern().replaceAll("\\s+", HttpUrl.FRAGMENT_ENCODE_SET), Locale.getDefault());
        simpleDateFormat.setTimeZone(getTimeZone());
        simpleDateFormat.setLenient(false);
        return simpleDateFormat;
    }

    static String getTextInputHint(Resources res, SimpleDateFormat format) {
        String pattern = format.toPattern();
        String string = res.getString(R.string.mtrl_picker_text_input_year_abbr);
        String string2 = res.getString(R.string.mtrl_picker_text_input_month_abbr);
        String string3 = res.getString(R.string.mtrl_picker_text_input_day_abbr);
        if (pattern.replaceAll("[^y]", HttpUrl.FRAGMENT_ENCODE_SET).length() == 1) {
            pattern = pattern.replace("y", "yyyy");
        }
        return pattern.replace("d", string3).replace("M", string2).replace("y", string);
    }

    static TimeSource getTimeSource() {
        TimeSource timeSource = timeSourceRef.get();
        return timeSource == null ? TimeSource.system() : timeSource;
    }

    private static TimeZone getTimeZone() {
        return TimeZone.getTimeZone(UTC);
    }

    static Calendar getTodayCalendar() {
        Calendar now = getTimeSource().now();
        now.set(11, 0);
        now.set(12, 0);
        now.set(13, 0);
        now.set(14, 0);
        now.setTimeZone(getTimeZone());
        return now;
    }

    private static android.icu.util.TimeZone getUtcAndroidTimeZone() {
        return android.icu.util.TimeZone.getTimeZone(UTC);
    }

    static Calendar getUtcCalendar() {
        return getUtcCalendarOf((Calendar) null);
    }

    static Calendar getUtcCalendarOf(Calendar rawCalendar) {
        Calendar instance = Calendar.getInstance(getTimeZone());
        if (rawCalendar == null) {
            instance.clear();
        } else {
            instance.setTimeInMillis(rawCalendar.getTimeInMillis());
        }
        return instance;
    }

    static DateFormat getYearAbbrMonthDayFormat(Locale locale) {
        return getAndroidFormat("yMMMd", locale);
    }

    static DateFormat getYearAbbrMonthWeekdayDayFormat(Locale locale) {
        return getAndroidFormat("yMMMEd", locale);
    }

    private static String removeYearFromDateFormatPattern(String pattern) {
        int findCharactersInDateFormatPattern = findCharactersInDateFormatPattern(pattern, "yY", 1, 0);
        if (findCharactersInDateFormatPattern >= pattern.length()) {
            return pattern;
        }
        String str = "EMd";
        int findCharactersInDateFormatPattern2 = findCharactersInDateFormatPattern(pattern, str, 1, findCharactersInDateFormatPattern);
        if (findCharactersInDateFormatPattern2 < pattern.length()) {
            str = str + ",";
        }
        return pattern.replace(pattern.substring(findCharactersInDateFormatPattern(pattern, str, -1, findCharactersInDateFormatPattern) + 1, findCharactersInDateFormatPattern2), " ").trim();
    }

    static void setTimeSource(TimeSource timeSource) {
        timeSourceRef.set(timeSource);
    }
}
