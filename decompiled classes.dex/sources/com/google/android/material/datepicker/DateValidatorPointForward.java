package com.google.android.material.datepicker;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.material.datepicker.CalendarConstraints;
import java.util.Arrays;

public class DateValidatorPointForward implements CalendarConstraints.DateValidator {
    public static final Parcelable.Creator<DateValidatorPointForward> CREATOR = new Parcelable.Creator<DateValidatorPointForward>() {
        public DateValidatorPointForward createFromParcel(Parcel source) {
            return new DateValidatorPointForward(source.readLong());
        }

        public DateValidatorPointForward[] newArray(int size) {
            return new DateValidatorPointForward[size];
        }
    };
    private final long point;

    private DateValidatorPointForward(long point2) {
        this.point = point2;
    }

    public static DateValidatorPointForward from(long point2) {
        return new DateValidatorPointForward(point2);
    }

    public static DateValidatorPointForward now() {
        return from(UtcDates.getTodayCalendar().getTimeInMillis());
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DateValidatorPointForward)) {
            return false;
        }
        return this.point == ((DateValidatorPointForward) o).point;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.point)});
    }

    public boolean isValid(long date) {
        return date >= this.point;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.point);
    }
}
