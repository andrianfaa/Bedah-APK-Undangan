package com.google.android.material.drawable;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import java.lang.reflect.InvocationTargetException;

/* compiled from: 00F7 */
public final class DrawableUtils {
    private DrawableUtils() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0042 A[Catch:{ IOException | XmlPullParserException -> 0x004a }] */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0014 A[Catch:{ IOException | XmlPullParserException -> 0x004a }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.util.AttributeSet parseDrawableXml(android.content.Context r5, int r6, java.lang.CharSequence r7) {
        /*
            android.content.res.Resources r0 = r5.getResources()     // Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x004a }
            android.content.res.XmlResourceParser r0 = r0.getXml(r6)     // Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x004a }
        L_0x0008:
            int r1 = r0.next()     // Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x004a }
            r2 = 2
            if (r1 == r2) goto L_0x0012
            r3 = 1
            if (r1 != r3) goto L_0x0008
        L_0x0012:
            if (r1 != r2) goto L_0x0042
            java.lang.String r2 = r0.getName()     // Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x004a }
            boolean r2 = android.text.TextUtils.equals(r2, r7)     // Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x004a }
            if (r2 == 0) goto L_0x0023
            android.util.AttributeSet r2 = android.util.Xml.asAttributeSet(r0)     // Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x004a }
            return r2
        L_0x0023:
            org.xmlpull.v1.XmlPullParserException r2 = new org.xmlpull.v1.XmlPullParserException     // Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x004a }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x004a }
            r3.<init>()     // Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x004a }
            java.lang.String r4 = "Must have a <"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x004a }
            java.lang.StringBuilder r3 = r3.append(r7)     // Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x004a }
            java.lang.String r4 = "> start tag"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x004a }
            java.lang.String r3 = r3.toString()     // Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x004a }
            r2.<init>(r3)     // Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x004a }
            throw r2     // Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x004a }
        L_0x0042:
            org.xmlpull.v1.XmlPullParserException r2 = new org.xmlpull.v1.XmlPullParserException     // Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x004a }
            java.lang.String r3 = "No start tag found"
            r2.<init>(r3)     // Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x004a }
            throw r2     // Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x004a }
        L_0x004a:
            r0 = move-exception
            goto L_0x004d
        L_0x004c:
            r0 = move-exception
        L_0x004d:
            android.content.res.Resources$NotFoundException r1 = new android.content.res.Resources$NotFoundException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Can't load badge resource ID #0x"
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = java.lang.Integer.toHexString(r6)
            mt.Log1F380D.a((java.lang.Object) r3)
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            r1.initCause(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.drawable.DrawableUtils.parseDrawableXml(android.content.Context, int, java.lang.CharSequence):android.util.AttributeSet");
    }

    public static void setRippleDrawableRadius(RippleDrawable drawable, int radius) {
        if (Build.VERSION.SDK_INT >= 23) {
            drawable.setRadius(radius);
            return;
        }
        Class<RippleDrawable> cls = RippleDrawable.class;
        try {
            cls.getDeclaredMethod("setMaxRadius", new Class[]{Integer.TYPE}).invoke(drawable, new Object[]{Integer.valueOf(radius)});
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException("Couldn't set RippleDrawable radius", e);
        }
    }

    public static PorterDuffColorFilter updateTintFilter(Drawable drawable, ColorStateList tint, PorterDuff.Mode tintMode) {
        if (tint == null || tintMode == null) {
            return null;
        }
        return new PorterDuffColorFilter(tint.getColorForState(drawable.getState(), 0), tintMode);
    }
}
