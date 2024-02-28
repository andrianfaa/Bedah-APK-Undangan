package androidx.core.graphics;

import android.graphics.Rect;

public final class Insets {
    public static final Insets NONE = new Insets(0, 0, 0, 0);
    public final int bottom;
    public final int left;
    public final int right;
    public final int top;

    static class Api29Impl {
        private Api29Impl() {
        }

        static android.graphics.Insets of(int left, int top, int right, int bottom) {
            return android.graphics.Insets.of(left, top, right, bottom);
        }
    }

    private Insets(int left2, int top2, int right2, int bottom2) {
        this.left = left2;
        this.top = top2;
        this.right = right2;
        this.bottom = bottom2;
    }

    public static Insets add(Insets a, Insets b) {
        return of(a.left + b.left, a.top + b.top, a.right + b.right, a.bottom + b.bottom);
    }

    public static Insets max(Insets a, Insets b) {
        return of(Math.max(a.left, b.left), Math.max(a.top, b.top), Math.max(a.right, b.right), Math.max(a.bottom, b.bottom));
    }

    public static Insets min(Insets a, Insets b) {
        return of(Math.min(a.left, b.left), Math.min(a.top, b.top), Math.min(a.right, b.right), Math.min(a.bottom, b.bottom));
    }

    public static Insets of(int left2, int top2, int right2, int bottom2) {
        return (left2 == 0 && top2 == 0 && right2 == 0 && bottom2 == 0) ? NONE : new Insets(left2, top2, right2, bottom2);
    }

    public static Insets of(Rect r) {
        return of(r.left, r.top, r.right, r.bottom);
    }

    public static Insets subtract(Insets a, Insets b) {
        return of(a.left - b.left, a.top - b.top, a.right - b.right, a.bottom - b.bottom);
    }

    public static Insets toCompatInsets(android.graphics.Insets insets) {
        return of(insets.left, insets.top, insets.right, insets.bottom);
    }

    @Deprecated
    public static Insets wrap(android.graphics.Insets insets) {
        return toCompatInsets(insets);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Insets insets = (Insets) o;
        return this.bottom == insets.bottom && this.left == insets.left && this.right == insets.right && this.top == insets.top;
    }

    public int hashCode() {
        return (((((this.left * 31) + this.top) * 31) + this.right) * 31) + this.bottom;
    }

    public android.graphics.Insets toPlatformInsets() {
        return Api29Impl.of(this.left, this.top, this.right, this.bottom);
    }

    public String toString() {
        return "Insets{left=" + this.left + ", top=" + this.top + ", right=" + this.right + ", bottom=" + this.bottom + '}';
    }
}
