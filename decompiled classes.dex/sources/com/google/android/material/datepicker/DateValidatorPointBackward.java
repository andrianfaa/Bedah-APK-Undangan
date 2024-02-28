package com.google.android.material.datepicker;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.material.datepicker.CalendarConstraints;
import java.util.Arrays;

public class DateValidatorPointBackward implements CalendarConstraints.DateValidator {
    public static final Parcelable.Creator<DateValidatorPointBackward> CREATOR = new Parcelable.Creator<DateValidatorPointBackward>() {
        public DateValidatorPointBackward createFromParcel(Parcel source) {
            return new DateValidatorPointBackward(source.readLong());
        }

        public DateValidatorPointBackward[] newArray(int size) {
            return new DateValidatorPointBackward[size];
        }
    };
    private final long point;

    private DateValidatorPointBackward(long point2) {
        this.point = point2;
    }

    public static DateValidatorPointBackward before(long point2) {
        return new DateValidatorPointBackward(point2);
    }

    public static DateValidatorPointBackward now() {
        return before(UtcDates.getTodayCalendar().getTimeInMillis());
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DateValidatorPointBackward)) {
            return false;
        }
        return this.point == ((DateValidatorPointBackward) o).point;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.point)});
    }

    public boolean isValid(long date) {
        return date <= this.point;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.point);
    }
}
