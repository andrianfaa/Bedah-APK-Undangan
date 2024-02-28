package com.google.android.material.timepicker;

import android.text.InputFilter;
import android.text.Spanned;
import okhttp3.HttpUrl;

class MaxInputValidator implements InputFilter {
    private int max;

    public MaxInputValidator(int max2) {
        this.max = max2;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            StringBuilder sb = new StringBuilder(dest);
            sb.replace(dstart, dend, source.subSequence(start, end).toString());
            if (Integer.parseInt(sb.toString()) <= this.max) {
                return null;
            }
            return HttpUrl.FRAGMENT_ENCODE_SET;
        } catch (NumberFormatException e) {
            return HttpUrl.FRAGMENT_ENCODE_SET;
        }
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(int max2) {
        this.max = max2;
    }
}
