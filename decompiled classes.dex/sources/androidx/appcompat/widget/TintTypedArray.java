package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.core.content.res.ResourcesCompat;

public class TintTypedArray {
    private final Context mContext;
    private TypedValue mTypedValue;
    private final TypedArray mWrapped;

    static class Api21Impl {
        private Api21Impl() {
        }

        static int getChangingConfigurations(TypedArray typedArray) {
            return typedArray.getChangingConfigurations();
        }

        static int getType(TypedArray typedArray, int index) {
            return typedArray.getType(index);
        }
    }

    private TintTypedArray(Context context, TypedArray array) {
        this.mContext = context;
        this.mWrapped = array;
    }

    public static TintTypedArray obtainStyledAttributes(Context context, int resid, int[] attrs) {
        return new TintTypedArray(context, context.obtainStyledAttributes(resid, attrs));
    }

    public static TintTypedArray obtainStyledAttributes(Context context, AttributeSet set, int[] attrs) {
        return new TintTypedArray(context, context.obtainStyledAttributes(set, attrs));
    }

    public static TintTypedArray obtainStyledAttributes(Context context, AttributeSet set, int[] attrs, int defStyleAttr, int defStyleRes) {
        return new TintTypedArray(context, context.obtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes));
    }

    public boolean getBoolean(int index, boolean defValue) {
        return this.mWrapped.getBoolean(index, defValue);
    }

    public int getChangingConfigurations() {
        return Api21Impl.getChangingConfigurations(this.mWrapped);
    }

    public int getColor(int index, int defValue) {
        return this.mWrapped.getColor(index, defValue);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0011, code lost:
        r1 = androidx.appcompat.content.res.AppCompatResources.getColorStateList(r2.mContext, (r0 = r2.mWrapped.getResourceId(r3, 0)));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.content.res.ColorStateList getColorStateList(int r3) {
        /*
            r2 = this;
            android.content.res.TypedArray r0 = r2.mWrapped
            boolean r0 = r0.hasValue(r3)
            if (r0 == 0) goto L_0x001a
            android.content.res.TypedArray r0 = r2.mWrapped
            r1 = 0
            int r0 = r0.getResourceId(r3, r1)
            if (r0 == 0) goto L_0x001a
            android.content.Context r1 = r2.mContext
            android.content.res.ColorStateList r1 = androidx.appcompat.content.res.AppCompatResources.getColorStateList(r1, r0)
            if (r1 == 0) goto L_0x001a
            return r1
        L_0x001a:
            android.content.res.TypedArray r0 = r2.mWrapped
            android.content.res.ColorStateList r0 = r0.getColorStateList(r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.TintTypedArray.getColorStateList(int):android.content.res.ColorStateList");
    }

    public float getDimension(int index, float defValue) {
        return this.mWrapped.getDimension(index, defValue);
    }

    public int getDimensionPixelOffset(int index, int defValue) {
        return this.mWrapped.getDimensionPixelOffset(index, defValue);
    }

    public int getDimensionPixelSize(int index, int defValue) {
        return this.mWrapped.getDimensionPixelSize(index, defValue);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0008, code lost:
        r0 = r2.mWrapped.getResourceId(r3, 0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.drawable.Drawable getDrawable(int r3) {
        /*
            r2 = this;
            android.content.res.TypedArray r0 = r2.mWrapped
            boolean r0 = r0.hasValue(r3)
            if (r0 == 0) goto L_0x0018
            android.content.res.TypedArray r0 = r2.mWrapped
            r1 = 0
            int r0 = r0.getResourceId(r3, r1)
            if (r0 == 0) goto L_0x0018
            android.content.Context r1 = r2.mContext
            android.graphics.drawable.Drawable r1 = androidx.appcompat.content.res.AppCompatResources.getDrawable(r1, r0)
            return r1
        L_0x0018:
            android.content.res.TypedArray r0 = r2.mWrapped
            android.graphics.drawable.Drawable r0 = r0.getDrawable(r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.TintTypedArray.getDrawable(int):android.graphics.drawable.Drawable");
    }

    public Drawable getDrawableIfKnown(int index) {
        int resourceId;
        if (!this.mWrapped.hasValue(index) || (resourceId = this.mWrapped.getResourceId(index, 0)) == 0) {
            return null;
        }
        return AppCompatDrawableManager.get().getDrawable(this.mContext, resourceId, true);
    }

    public float getFloat(int index, float defValue) {
        return this.mWrapped.getFloat(index, defValue);
    }

    public Typeface getFont(int index, int style, ResourcesCompat.FontCallback fontCallback) {
        int resourceId = this.mWrapped.getResourceId(index, 0);
        if (resourceId == 0) {
            return null;
        }
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        return ResourcesCompat.getFont(this.mContext, resourceId, this.mTypedValue, style, fontCallback);
    }

    public float getFraction(int index, int base, int pbase, float defValue) {
        return this.mWrapped.getFraction(index, base, pbase, defValue);
    }

    public int getIndex(int at) {
        return this.mWrapped.getIndex(at);
    }

    public int getIndexCount() {
        return this.mWrapped.getIndexCount();
    }

    public int getInt(int index, int defValue) {
        return this.mWrapped.getInt(index, defValue);
    }

    public int getInteger(int index, int defValue) {
        return this.mWrapped.getInteger(index, defValue);
    }

    public int getLayoutDimension(int index, int defValue) {
        return this.mWrapped.getLayoutDimension(index, defValue);
    }

    public int getLayoutDimension(int index, String name) {
        return this.mWrapped.getLayoutDimension(index, name);
    }

    public String getNonResourceString(int index) {
        return this.mWrapped.getNonResourceString(index);
    }

    public String getPositionDescription() {
        return this.mWrapped.getPositionDescription();
    }

    public int getResourceId(int index, int defValue) {
        return this.mWrapped.getResourceId(index, defValue);
    }

    public Resources getResources() {
        return this.mWrapped.getResources();
    }

    public String getString(int index) {
        return this.mWrapped.getString(index);
    }

    public CharSequence getText(int index) {
        return this.mWrapped.getText(index);
    }

    public CharSequence[] getTextArray(int index) {
        return this.mWrapped.getTextArray(index);
    }

    public int getType(int index) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getType(this.mWrapped, index);
        }
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        this.mWrapped.getValue(index, this.mTypedValue);
        return this.mTypedValue.type;
    }

    public boolean getValue(int index, TypedValue outValue) {
        return this.mWrapped.getValue(index, outValue);
    }

    public TypedArray getWrappedTypeArray() {
        return this.mWrapped;
    }

    public boolean hasValue(int index) {
        return this.mWrapped.hasValue(index);
    }

    public int length() {
        return this.mWrapped.length();
    }

    public TypedValue peekValue(int index) {
        return this.mWrapped.peekValue(index);
    }

    public void recycle() {
        this.mWrapped.recycle();
    }
}
