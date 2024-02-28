package androidx.core.util;

import android.util.SizeF;

public final class SizeFCompat {
    private final float mHeight;
    private final float mWidth;

    private static final class Api21Impl {
        private Api21Impl() {
        }

        static SizeF toSizeF(SizeFCompat size) {
            Preconditions.checkNotNull(size);
            return new SizeF(size.getWidth(), size.getHeight());
        }

        static SizeFCompat toSizeFCompat(SizeF size) {
            Preconditions.checkNotNull(size);
            return new SizeFCompat(size.getWidth(), size.getHeight());
        }
    }

    public SizeFCompat(float width, float height) {
        this.mWidth = Preconditions.checkArgumentFinite(width, "width");
        this.mHeight = Preconditions.checkArgumentFinite(height, "height");
    }

    public static SizeFCompat toSizeFCompat(SizeF size) {
        return Api21Impl.toSizeFCompat(size);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SizeFCompat)) {
            return false;
        }
        SizeFCompat sizeFCompat = (SizeFCompat) o;
        return sizeFCompat.mWidth == this.mWidth && sizeFCompat.mHeight == this.mHeight;
    }

    public float getHeight() {
        return this.mHeight;
    }

    public float getWidth() {
        return this.mWidth;
    }

    public int hashCode() {
        return Float.floatToIntBits(this.mWidth) ^ Float.floatToIntBits(this.mHeight);
    }

    public SizeF toSizeF() {
        return Api21Impl.toSizeF(this);
    }

    public String toString() {
        return this.mWidth + "x" + this.mHeight;
    }
}
