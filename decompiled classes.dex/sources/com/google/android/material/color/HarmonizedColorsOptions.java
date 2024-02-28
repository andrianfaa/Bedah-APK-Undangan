package com.google.android.material.color;

import com.google.android.material.R;

public class HarmonizedColorsOptions {
    private final int colorAttributeToHarmonizeWith;
    private final HarmonizedColorAttributes colorAttributes;
    private final int[] colorResourceIds;

    public static class Builder {
        /* access modifiers changed from: private */
        public int colorAttributeToHarmonizeWith = R.attr.colorPrimary;
        /* access modifiers changed from: private */
        public HarmonizedColorAttributes colorAttributes;
        /* access modifiers changed from: private */
        public int[] colorResourceIds = new int[0];

        public HarmonizedColorsOptions build() {
            return new HarmonizedColorsOptions(this);
        }

        public Builder setColorAttributeToHarmonizeWith(int colorAttributeToHarmonizeWith2) {
            this.colorAttributeToHarmonizeWith = colorAttributeToHarmonizeWith2;
            return this;
        }

        public Builder setColorAttributes(HarmonizedColorAttributes colorAttributes2) {
            this.colorAttributes = colorAttributes2;
            return this;
        }

        public Builder setColorResourceIds(int[] colorResourceIds2) {
            this.colorResourceIds = colorResourceIds2;
            return this;
        }
    }

    private HarmonizedColorsOptions(Builder builder) {
        this.colorResourceIds = builder.colorResourceIds;
        this.colorAttributes = builder.colorAttributes;
        this.colorAttributeToHarmonizeWith = builder.colorAttributeToHarmonizeWith;
    }

    public static HarmonizedColorsOptions createMaterialDefaults() {
        return new Builder().setColorAttributes(HarmonizedColorAttributes.createMaterialDefaults()).build();
    }

    public int getColorAttributeToHarmonizeWith() {
        return this.colorAttributeToHarmonizeWith;
    }

    public HarmonizedColorAttributes getColorAttributes() {
        return this.colorAttributes;
    }

    public int[] getColorResourceIds() {
        return this.colorResourceIds;
    }

    /* access modifiers changed from: package-private */
    public int getThemeOverlayResourceId(int defaultThemeOverlay) {
        HarmonizedColorAttributes harmonizedColorAttributes = this.colorAttributes;
        return (harmonizedColorAttributes == null || harmonizedColorAttributes.getThemeOverlay() == 0) ? defaultThemeOverlay : this.colorAttributes.getThemeOverlay();
    }
}
