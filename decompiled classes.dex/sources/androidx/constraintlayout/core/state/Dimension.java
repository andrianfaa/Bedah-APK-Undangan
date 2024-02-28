package androidx.constraintlayout.core.state;

import androidx.constraintlayout.core.widgets.ConstraintWidget;

public class Dimension {
    public static final Object FIXED_DIMENSION = new Object();
    public static final Object PARENT_DIMENSION = new Object();
    public static final Object PERCENT_DIMENSION = new Object();
    public static final Object RATIO_DIMENSION = new Object();
    public static final Object SPREAD_DIMENSION = new Object();
    public static final Object WRAP_DIMENSION = new Object();
    private final int WRAP_CONTENT = -2;
    Object mInitialValue = WRAP_DIMENSION;
    boolean mIsSuggested = false;
    int mMax = Integer.MAX_VALUE;
    int mMin = 0;
    float mPercent = 1.0f;
    String mRatioString = null;
    int mValue = 0;

    public enum Type {
        FIXED,
        WRAP,
        MATCH_PARENT,
        MATCH_CONSTRAINT
    }

    private Dimension() {
    }

    private Dimension(Object type) {
        this.mInitialValue = type;
    }

    public static Dimension Fixed(int value) {
        Dimension dimension = new Dimension(FIXED_DIMENSION);
        dimension.fixed(value);
        return dimension;
    }

    public static Dimension Fixed(Object value) {
        Dimension dimension = new Dimension(FIXED_DIMENSION);
        dimension.fixed(value);
        return dimension;
    }

    public static Dimension Parent() {
        return new Dimension(PARENT_DIMENSION);
    }

    public static Dimension Percent(Object key, float value) {
        Dimension dimension = new Dimension(PERCENT_DIMENSION);
        dimension.percent(key, value);
        return dimension;
    }

    public static Dimension Ratio(String ratio) {
        Dimension dimension = new Dimension(RATIO_DIMENSION);
        dimension.ratio(ratio);
        return dimension;
    }

    public static Dimension Spread() {
        return new Dimension(SPREAD_DIMENSION);
    }

    public static Dimension Suggested(int value) {
        Dimension dimension = new Dimension();
        dimension.suggested(value);
        return dimension;
    }

    public static Dimension Suggested(Object startValue) {
        Dimension dimension = new Dimension();
        dimension.suggested(startValue);
        return dimension;
    }

    public static Dimension Wrap() {
        return new Dimension(WRAP_DIMENSION);
    }

    public void apply(State state, ConstraintWidget constraintWidget, int orientation) {
        String str = this.mRatioString;
        if (str != null) {
            constraintWidget.setDimensionRatio(str);
        }
        if (orientation == 0) {
            if (this.mIsSuggested) {
                constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                int i = 0;
                Object obj = this.mInitialValue;
                if (obj == WRAP_DIMENSION) {
                    i = 1;
                } else if (obj == PERCENT_DIMENSION) {
                    i = 2;
                }
                constraintWidget.setHorizontalMatchStyle(i, this.mMin, this.mMax, this.mPercent);
                return;
            }
            int i2 = this.mMin;
            if (i2 > 0) {
                constraintWidget.setMinWidth(i2);
            }
            int i3 = this.mMax;
            if (i3 < Integer.MAX_VALUE) {
                constraintWidget.setMaxWidth(i3);
            }
            Object obj2 = this.mInitialValue;
            if (obj2 == WRAP_DIMENSION) {
                constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
            } else if (obj2 == PARENT_DIMENSION) {
                constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
            } else if (obj2 == null) {
                constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                constraintWidget.setWidth(this.mValue);
            }
        } else if (this.mIsSuggested) {
            constraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
            int i4 = 0;
            Object obj3 = this.mInitialValue;
            if (obj3 == WRAP_DIMENSION) {
                i4 = 1;
            } else if (obj3 == PERCENT_DIMENSION) {
                i4 = 2;
            }
            constraintWidget.setVerticalMatchStyle(i4, this.mMin, this.mMax, this.mPercent);
        } else {
            int i5 = this.mMin;
            if (i5 > 0) {
                constraintWidget.setMinHeight(i5);
            }
            int i6 = this.mMax;
            if (i6 < Integer.MAX_VALUE) {
                constraintWidget.setMaxHeight(i6);
            }
            Object obj4 = this.mInitialValue;
            if (obj4 == WRAP_DIMENSION) {
                constraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
            } else if (obj4 == PARENT_DIMENSION) {
                constraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
            } else if (obj4 == null) {
                constraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                constraintWidget.setHeight(this.mValue);
            }
        }
    }

    public boolean equalsFixedValue(int value) {
        return this.mInitialValue == null && this.mValue == value;
    }

    public Dimension fixed(int value) {
        this.mInitialValue = null;
        this.mValue = value;
        return this;
    }

    public Dimension fixed(Object value) {
        this.mInitialValue = value;
        if (value instanceof Integer) {
            this.mValue = ((Integer) value).intValue();
            this.mInitialValue = null;
        }
        return this;
    }

    /* access modifiers changed from: package-private */
    public int getValue() {
        return this.mValue;
    }

    public Dimension max(int value) {
        if (this.mMax >= 0) {
            this.mMax = value;
        }
        return this;
    }

    public Dimension max(Object value) {
        Object obj = WRAP_DIMENSION;
        if (value == obj && this.mIsSuggested) {
            this.mInitialValue = obj;
            this.mMax = Integer.MAX_VALUE;
        }
        return this;
    }

    public Dimension min(int value) {
        if (value >= 0) {
            this.mMin = value;
        }
        return this;
    }

    public Dimension min(Object value) {
        if (value == WRAP_DIMENSION) {
            this.mMin = -2;
        }
        return this;
    }

    public Dimension percent(Object key, float value) {
        this.mPercent = value;
        return this;
    }

    public Dimension ratio(String ratio) {
        this.mRatioString = ratio;
        return this;
    }

    /* access modifiers changed from: package-private */
    public void setValue(int value) {
        this.mIsSuggested = false;
        this.mInitialValue = null;
        this.mValue = value;
    }

    public Dimension suggested(int value) {
        this.mIsSuggested = true;
        if (value >= 0) {
            this.mMax = value;
        }
        return this;
    }

    public Dimension suggested(Object value) {
        this.mInitialValue = value;
        this.mIsSuggested = true;
        return this;
    }
}
