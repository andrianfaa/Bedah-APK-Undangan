package com.google.android.material.datepicker;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.util.Preconditions;
import com.google.android.material.datepicker.CalendarConstraints;
import java.util.ArrayList;
import java.util.List;

public final class CompositeDateValidator implements CalendarConstraints.DateValidator {
    /* access modifiers changed from: private */
    public static final Operator ALL_OPERATOR = new Operator() {
        public int getId() {
            return 2;
        }

        public boolean isValid(List<CalendarConstraints.DateValidator> list, long date) {
            for (CalendarConstraints.DateValidator next : list) {
                if (next != null && !next.isValid(date)) {
                    return false;
                }
            }
            return true;
        }
    };
    /* access modifiers changed from: private */
    public static final Operator ANY_OPERATOR = new Operator() {
        public int getId() {
            return 1;
        }

        public boolean isValid(List<CalendarConstraints.DateValidator> list, long date) {
            for (CalendarConstraints.DateValidator next : list) {
                if (next != null && next.isValid(date)) {
                    return true;
                }
            }
            return false;
        }
    };
    private static final int COMPARATOR_ALL_ID = 2;
    private static final int COMPARATOR_ANY_ID = 1;
    public static final Parcelable.Creator<CompositeDateValidator> CREATOR = new Parcelable.Creator<CompositeDateValidator>() {
        public CompositeDateValidator createFromParcel(Parcel source) {
            ArrayList readArrayList = source.readArrayList(CalendarConstraints.DateValidator.class.getClassLoader());
            int readInt = source.readInt();
            return new CompositeDateValidator((List) Preconditions.checkNotNull(readArrayList), readInt == 2 ? CompositeDateValidator.ALL_OPERATOR : readInt == 1 ? CompositeDateValidator.ANY_OPERATOR : CompositeDateValidator.ALL_OPERATOR);
        }

        public CompositeDateValidator[] newArray(int size) {
            return new CompositeDateValidator[size];
        }
    };
    private final Operator operator;
    private final List<CalendarConstraints.DateValidator> validators;

    private interface Operator {
        int getId();

        boolean isValid(List<CalendarConstraints.DateValidator> list, long j);
    }

    private CompositeDateValidator(List<CalendarConstraints.DateValidator> list, Operator operator2) {
        this.validators = list;
        this.operator = operator2;
    }

    public static CalendarConstraints.DateValidator allOf(List<CalendarConstraints.DateValidator> list) {
        return new CompositeDateValidator(list, ALL_OPERATOR);
    }

    public static CalendarConstraints.DateValidator anyOf(List<CalendarConstraints.DateValidator> list) {
        return new CompositeDateValidator(list, ANY_OPERATOR);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompositeDateValidator)) {
            return false;
        }
        CompositeDateValidator compositeDateValidator = (CompositeDateValidator) o;
        return this.validators.equals(compositeDateValidator.validators) && this.operator.getId() == compositeDateValidator.operator.getId();
    }

    public int hashCode() {
        return this.validators.hashCode();
    }

    public boolean isValid(long date) {
        return this.operator.isValid(this.validators, date);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.validators);
        dest.writeInt(this.operator.getId());
    }
}
