package com.google.android.material.datepicker;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.util.ObjectsCompat;
import java.util.Arrays;

public final class CalendarConstraints implements Parcelable {
    public static final Parcelable.Creator<CalendarConstraints> CREATOR = new Parcelable.Creator<CalendarConstraints>() {
        public CalendarConstraints createFromParcel(Parcel source) {
            return new CalendarConstraints((Month) source.readParcelable(Month.class.getClassLoader()), (Month) source.readParcelable(Month.class.getClassLoader()), (DateValidator) source.readParcelable(DateValidator.class.getClassLoader()), (Month) source.readParcelable(Month.class.getClassLoader()));
        }

        public CalendarConstraints[] newArray(int size) {
            return new CalendarConstraints[size];
        }
    };
    /* access modifiers changed from: private */
    public final Month end;
    private final int monthSpan;
    /* access modifiers changed from: private */
    public Month openAt;
    /* access modifiers changed from: private */
    public final Month start;
    /* access modifiers changed from: private */
    public final DateValidator validator;
    private final int yearSpan;

    public static final class Builder {
        private static final String DEEP_COPY_VALIDATOR_KEY = "DEEP_COPY_VALIDATOR_KEY";
        static final long DEFAULT_END = UtcDates.canonicalYearMonthDay(Month.create(2100, 11).timeInMillis);
        static final long DEFAULT_START = UtcDates.canonicalYearMonthDay(Month.create(1900, 0).timeInMillis);
        private long end = DEFAULT_END;
        private Long openAt;
        private long start = DEFAULT_START;
        private DateValidator validator = DateValidatorPointForward.from(Long.MIN_VALUE);

        public Builder() {
        }

        Builder(CalendarConstraints clone) {
            this.start = clone.start.timeInMillis;
            this.end = clone.end.timeInMillis;
            this.openAt = Long.valueOf(clone.openAt.timeInMillis);
            this.validator = clone.validator;
        }

        public CalendarConstraints build() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(DEEP_COPY_VALIDATOR_KEY, this.validator);
            Month create = Month.create(this.start);
            Month create2 = Month.create(this.end);
            DateValidator dateValidator = (DateValidator) bundle.getParcelable(DEEP_COPY_VALIDATOR_KEY);
            Long l = this.openAt;
            return new CalendarConstraints(create, create2, dateValidator, l == null ? null : Month.create(l.longValue()));
        }

        public Builder setEnd(long month) {
            this.end = month;
            return this;
        }

        public Builder setOpenAt(long month) {
            this.openAt = Long.valueOf(month);
            return this;
        }

        public Builder setStart(long month) {
            this.start = month;
            return this;
        }

        public Builder setValidator(DateValidator validator2) {
            this.validator = validator2;
            return this;
        }
    }

    public interface DateValidator extends Parcelable {
        boolean isValid(long j);
    }

    private CalendarConstraints(Month start2, Month end2, DateValidator validator2, Month openAt2) {
        this.start = start2;
        this.end = end2;
        this.openAt = openAt2;
        this.validator = validator2;
        if (openAt2 != null && start2.compareTo(openAt2) > 0) {
            throw new IllegalArgumentException("start Month cannot be after current Month");
        } else if (openAt2 == null || openAt2.compareTo(end2) <= 0) {
            this.monthSpan = start2.monthsUntil(end2) + 1;
            this.yearSpan = (end2.year - start2.year) + 1;
        } else {
            throw new IllegalArgumentException("current Month cannot be after end Month");
        }
    }

    /* access modifiers changed from: package-private */
    public Month clamp(Month month) {
        return month.compareTo(this.start) < 0 ? this.start : month.compareTo(this.end) > 0 ? this.end : month;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CalendarConstraints)) {
            return false;
        }
        CalendarConstraints calendarConstraints = (CalendarConstraints) o;
        return this.start.equals(calendarConstraints.start) && this.end.equals(calendarConstraints.end) && ObjectsCompat.equals(this.openAt, calendarConstraints.openAt) && this.validator.equals(calendarConstraints.validator);
    }

    public DateValidator getDateValidator() {
        return this.validator;
    }

    /* access modifiers changed from: package-private */
    public Month getEnd() {
        return this.end;
    }

    /* access modifiers changed from: package-private */
    public int getMonthSpan() {
        return this.monthSpan;
    }

    /* access modifiers changed from: package-private */
    public Month getOpenAt() {
        return this.openAt;
    }

    /* access modifiers changed from: package-private */
    public Month getStart() {
        return this.start;
    }

    /* access modifiers changed from: package-private */
    public int getYearSpan() {
        return this.yearSpan;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.start, this.end, this.openAt, this.validator});
    }

    /* access modifiers changed from: package-private */
    public boolean isWithinBounds(long date) {
        if (this.start.getDay(1) <= date) {
            Month month = this.end;
            if (date <= month.getDay(month.daysInMonth)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void setOpenAt(Month openAt2) {
        this.openAt = openAt2;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.start, 0);
        dest.writeParcelable(this.end, 0);
        dest.writeParcelable(this.openAt, 0);
        dest.writeParcelable(this.validator, 0);
    }
}
