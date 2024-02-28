package androidx.core.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.WeakHashMap;
import mt.Log1F380D;

/* compiled from: 003F */
public final class ResourcesCompat {
    public static final int ID_NULL = 0;
    private static final String TAG = "ResourcesCompat";
    private static final Object sColorStateCacheLock = new Object();
    private static final WeakHashMap<ColorStateListCacheKey, SparseArray<ColorStateListCacheEntry>> sColorStateCaches = new WeakHashMap<>(0);
    private static final ThreadLocal<TypedValue> sTempTypedValue = new ThreadLocal<>();

    static class Api15Impl {
        private Api15Impl() {
        }

        static Drawable getDrawableForDensity(Resources resources, int id, int density) {
            return resources.getDrawableForDensity(id, density);
        }
    }

    static class Api21Impl {
        private Api21Impl() {
        }

        static Drawable getDrawable(Resources resources, int id, Resources.Theme theme) {
            return resources.getDrawable(id, theme);
        }

        static Drawable getDrawableForDensity(Resources resources, int id, int density, Resources.Theme theme) {
            return resources.getDrawableForDensity(id, density, theme);
        }
    }

    static class Api23Impl {
        private Api23Impl() {
        }

        static int getColor(Resources resources, int id, Resources.Theme theme) {
            return resources.getColor(id, theme);
        }

        static ColorStateList getColorStateList(Resources res, int id, Resources.Theme theme) {
            return res.getColorStateList(id, theme);
        }
    }

    static class Api29Impl {
        private Api29Impl() {
        }

        static float getFloat(Resources res, int id) {
            return res.getFloat(id);
        }
    }

    private static class ColorStateListCacheEntry {
        final Configuration mConfiguration;
        final int mThemeHash;
        final ColorStateList mValue;

        ColorStateListCacheEntry(ColorStateList value, Configuration configuration, Resources.Theme theme) {
            this.mValue = value;
            this.mConfiguration = configuration;
            this.mThemeHash = theme == null ? 0 : theme.hashCode();
        }
    }

    private static final class ColorStateListCacheKey {
        final Resources mResources;
        final Resources.Theme mTheme;

        ColorStateListCacheKey(Resources resources, Resources.Theme theme) {
            this.mResources = resources;
            this.mTheme = theme;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ColorStateListCacheKey colorStateListCacheKey = (ColorStateListCacheKey) o;
            return this.mResources.equals(colorStateListCacheKey.mResources) && ObjectsCompat.equals(this.mTheme, colorStateListCacheKey.mTheme);
        }

        public int hashCode() {
            return ObjectsCompat.hash(this.mResources, this.mTheme);
        }
    }

    public static abstract class FontCallback {
        public static Handler getHandler(Handler handler) {
            return handler == null ? new Handler(Looper.getMainLooper()) : handler;
        }

        public final void callbackFailAsync(int reason, Handler handler) {
            getHandler(handler).post(new ResourcesCompat$FontCallback$$ExternalSyntheticLambda1(this, reason));
        }

        public final void callbackSuccessAsync(Typeface typeface, Handler handler) {
            getHandler(handler).post(new ResourcesCompat$FontCallback$$ExternalSyntheticLambda0(this, typeface));
        }

        /* renamed from: onFontRetrievalFailed */
        public abstract void m6lambda$callbackFailAsync$1$androidxcorecontentresResourcesCompat$FontCallback(int i);

        /* renamed from: onFontRetrieved */
        public abstract void m7lambda$callbackSuccessAsync$0$androidxcorecontentresResourcesCompat$FontCallback(Typeface typeface);
    }

    public static final class ThemeCompat {

        static class Api23Impl {
            private static Method sRebaseMethod;
            private static boolean sRebaseMethodFetched;
            private static final Object sRebaseMethodLock = new Object();

            private Api23Impl() {
            }

            static void rebase(Resources.Theme theme) {
                synchronized (sRebaseMethodLock) {
                    if (!sRebaseMethodFetched) {
                        try {
                            Method declaredMethod = Resources.Theme.class.getDeclaredMethod("rebase", new Class[0]);
                            sRebaseMethod = declaredMethod;
                            declaredMethod.setAccessible(true);
                        } catch (NoSuchMethodException e) {
                            Log.i(ResourcesCompat.TAG, "Failed to retrieve rebase() method", e);
                        }
                        sRebaseMethodFetched = true;
                    }
                    Method method = sRebaseMethod;
                    if (method != null) {
                        try {
                            method.invoke(theme, new Object[0]);
                        } catch (IllegalAccessException | InvocationTargetException e2) {
                            Log.i(ResourcesCompat.TAG, "Failed to invoke rebase() method via reflection", e2);
                            sRebaseMethod = null;
                        }
                    }
                }
            }
        }

        static class Api29Impl {
            private Api29Impl() {
            }

            static void rebase(Resources.Theme theme) {
                theme.rebase();
            }
        }

        private ThemeCompat() {
        }

        public static void rebase(Resources.Theme theme) {
            if (Build.VERSION.SDK_INT >= 29) {
                Api29Impl.rebase(theme);
            } else if (Build.VERSION.SDK_INT >= 23) {
                Api23Impl.rebase(theme);
            }
        }
    }

    private ResourcesCompat() {
    }

    private static void addColorStateListToCache(ColorStateListCacheKey key, int resId, ColorStateList value, Resources.Theme theme) {
        synchronized (sColorStateCacheLock) {
            WeakHashMap<ColorStateListCacheKey, SparseArray<ColorStateListCacheEntry>> weakHashMap = sColorStateCaches;
            SparseArray sparseArray = weakHashMap.get(key);
            if (sparseArray == null) {
                sparseArray = new SparseArray();
                weakHashMap.put(key, sparseArray);
            }
            sparseArray.append(resId, new ColorStateListCacheEntry(value, key.mResources.getConfiguration(), theme));
        }
    }

    public static void clearCachesForTheme(Resources.Theme theme) {
        synchronized (sColorStateCacheLock) {
            Iterator<ColorStateListCacheKey> it = sColorStateCaches.keySet().iterator();
            while (it.hasNext()) {
                ColorStateListCacheKey next = it.next();
                if (next != null && theme.equals(next.mTheme)) {
                    it.remove();
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0047, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.content.res.ColorStateList getCachedColorStateList(androidx.core.content.res.ResourcesCompat.ColorStateListCacheKey r5, int r6) {
        /*
            java.lang.Object r0 = sColorStateCacheLock
            monitor-enter(r0)
            java.util.WeakHashMap<androidx.core.content.res.ResourcesCompat$ColorStateListCacheKey, android.util.SparseArray<androidx.core.content.res.ResourcesCompat$ColorStateListCacheEntry>> r1 = sColorStateCaches     // Catch:{ all -> 0x0049 }
            java.lang.Object r1 = r1.get(r5)     // Catch:{ all -> 0x0049 }
            android.util.SparseArray r1 = (android.util.SparseArray) r1     // Catch:{ all -> 0x0049 }
            if (r1 == 0) goto L_0x0046
            int r2 = r1.size()     // Catch:{ all -> 0x0049 }
            if (r2 <= 0) goto L_0x0046
            java.lang.Object r2 = r1.get(r6)     // Catch:{ all -> 0x0049 }
            androidx.core.content.res.ResourcesCompat$ColorStateListCacheEntry r2 = (androidx.core.content.res.ResourcesCompat.ColorStateListCacheEntry) r2     // Catch:{ all -> 0x0049 }
            if (r2 == 0) goto L_0x0046
            android.content.res.Configuration r3 = r2.mConfiguration     // Catch:{ all -> 0x0049 }
            android.content.res.Resources r4 = r5.mResources     // Catch:{ all -> 0x0049 }
            android.content.res.Configuration r4 = r4.getConfiguration()     // Catch:{ all -> 0x0049 }
            boolean r3 = r3.equals(r4)     // Catch:{ all -> 0x0049 }
            if (r3 == 0) goto L_0x0043
            android.content.res.Resources$Theme r3 = r5.mTheme     // Catch:{ all -> 0x0049 }
            if (r3 != 0) goto L_0x0031
            int r3 = r2.mThemeHash     // Catch:{ all -> 0x0049 }
            if (r3 == 0) goto L_0x003f
        L_0x0031:
            android.content.res.Resources$Theme r3 = r5.mTheme     // Catch:{ all -> 0x0049 }
            if (r3 == 0) goto L_0x0043
            int r3 = r2.mThemeHash     // Catch:{ all -> 0x0049 }
            android.content.res.Resources$Theme r4 = r5.mTheme     // Catch:{ all -> 0x0049 }
            int r4 = r4.hashCode()     // Catch:{ all -> 0x0049 }
            if (r3 != r4) goto L_0x0043
        L_0x003f:
            android.content.res.ColorStateList r3 = r2.mValue     // Catch:{ all -> 0x0049 }
            monitor-exit(r0)     // Catch:{ all -> 0x0049 }
            return r3
        L_0x0043:
            r1.remove(r6)     // Catch:{ all -> 0x0049 }
        L_0x0046:
            monitor-exit(r0)     // Catch:{ all -> 0x0049 }
            r0 = 0
            return r0
        L_0x0049:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0049 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.res.ResourcesCompat.getCachedColorStateList(androidx.core.content.res.ResourcesCompat$ColorStateListCacheKey, int):android.content.res.ColorStateList");
    }

    public static Typeface getCachedFont(Context context, int id) throws Resources.NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, id, new TypedValue(), 0, (FontCallback) null, (Handler) null, false, true);
    }

    public static int getColor(Resources res, int id, Resources.Theme theme) throws Resources.NotFoundException {
        return Build.VERSION.SDK_INT >= 23 ? Api23Impl.getColor(res, id, theme) : res.getColor(id);
    }

    public static ColorStateList getColorStateList(Resources res, int id, Resources.Theme theme) throws Resources.NotFoundException {
        ColorStateListCacheKey colorStateListCacheKey = new ColorStateListCacheKey(res, theme);
        ColorStateList cachedColorStateList = getCachedColorStateList(colorStateListCacheKey, id);
        if (cachedColorStateList != null) {
            return cachedColorStateList;
        }
        ColorStateList inflateColorStateList = inflateColorStateList(res, id, theme);
        if (inflateColorStateList == null) {
            return Build.VERSION.SDK_INT >= 23 ? Api23Impl.getColorStateList(res, id, theme) : res.getColorStateList(id);
        }
        addColorStateListToCache(colorStateListCacheKey, id, inflateColorStateList, theme);
        return inflateColorStateList;
    }

    public static Drawable getDrawable(Resources res, int id, Resources.Theme theme) throws Resources.NotFoundException {
        return Build.VERSION.SDK_INT >= 21 ? Api21Impl.getDrawable(res, id, theme) : res.getDrawable(id);
    }

    public static Drawable getDrawableForDensity(Resources res, int id, int density, Resources.Theme theme) throws Resources.NotFoundException {
        return Build.VERSION.SDK_INT >= 21 ? Api21Impl.getDrawableForDensity(res, id, density, theme) : Build.VERSION.SDK_INT >= 15 ? Api15Impl.getDrawableForDensity(res, id, density) : res.getDrawable(id);
    }

    public static float getFloat(Resources res, int id) {
        if (Build.VERSION.SDK_INT >= 29) {
            return Api29Impl.getFloat(res, id);
        }
        TypedValue typedValue = getTypedValue();
        res.getValue(id, typedValue, true);
        if (typedValue.type == 4) {
            return typedValue.getFloat();
        }
        StringBuilder append = new StringBuilder().append("Resource ID #0x");
        String hexString = Integer.toHexString(id);
        Log1F380D.a((Object) hexString);
        StringBuilder append2 = append.append(hexString).append(" type #0x");
        String hexString2 = Integer.toHexString(typedValue.type);
        Log1F380D.a((Object) hexString2);
        throw new Resources.NotFoundException(append2.append(hexString2).append(" is not valid").toString());
    }

    public static Typeface getFont(Context context, int id) throws Resources.NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, id, new TypedValue(), 0, (FontCallback) null, (Handler) null, false, false);
    }

    public static Typeface getFont(Context context, int id, TypedValue value, int style, FontCallback fontCallback) throws Resources.NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, id, value, style, fontCallback, (Handler) null, true, false);
    }

    public static void getFont(Context context, int id, FontCallback fontCallback, Handler handler) throws Resources.NotFoundException {
        Preconditions.checkNotNull(fontCallback);
        if (context.isRestricted()) {
            fontCallback.callbackFailAsync(-4, handler);
            return;
        }
        loadFont(context, id, new TypedValue(), 0, fontCallback, handler, false, false);
    }

    private static TypedValue getTypedValue() {
        ThreadLocal<TypedValue> threadLocal = sTempTypedValue;
        TypedValue typedValue = threadLocal.get();
        if (typedValue != null) {
            return typedValue;
        }
        TypedValue typedValue2 = new TypedValue();
        threadLocal.set(typedValue2);
        return typedValue2;
    }

    private static ColorStateList inflateColorStateList(Resources resources, int resId, Resources.Theme theme) {
        if (isColorInt(resources, resId)) {
            return null;
        }
        try {
            return ColorStateListInflaterCompat.createFromXml(resources, resources.getXml(resId), theme);
        } catch (Exception e) {
            Log.w(TAG, "Failed to inflate ColorStateList, leaving it to the framework", e);
            return null;
        }
    }

    private static boolean isColorInt(Resources resources, int resId) {
        TypedValue typedValue = getTypedValue();
        resources.getValue(resId, typedValue, true);
        return typedValue.type >= 28 && typedValue.type <= 31;
    }

    private static Typeface loadFont(Context context, int id, TypedValue value, int style, FontCallback fontCallback, Handler handler, boolean isRequestFromLayoutInflator, boolean isCachedOnly) {
        Resources resources = context.getResources();
        int i = id;
        TypedValue typedValue = value;
        resources.getValue(id, value, true);
        Typeface loadFont = loadFont(context, resources, value, id, style, fontCallback, handler, isRequestFromLayoutInflator, isCachedOnly);
        if (loadFont != null || fontCallback != null || isCachedOnly) {
            return loadFont;
        }
        StringBuilder append = new StringBuilder().append("Font resource ID #0x");
        String hexString = Integer.toHexString(id);
        Log1F380D.a((Object) hexString);
        throw new Resources.NotFoundException(append.append(hexString).append(" could not be retrieved.").toString());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00c9, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00ca, code lost:
        r19 = r6;
        r11 = -3;
        r2 = r9;
        r3 = TAG;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00d0, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00d1, code lost:
        r19 = r6;
        r11 = -3;
        r18 = r9;
        r20 = TAG;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00d0 A[ExcHandler: IOException (e java.io.IOException), Splitter:B:14:0x003b] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0112  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.graphics.Typeface loadFont(android.content.Context r21, android.content.res.Resources r22, android.util.TypedValue r23, int r24, int r25, androidx.core.content.res.ResourcesCompat.FontCallback r26, android.os.Handler r27, boolean r28, boolean r29) {
        /*
            r11 = r22
            r12 = r23
            r13 = r24
            r14 = r26
            r15 = r27
            java.lang.String r10 = "ResourcesCompat"
            java.lang.CharSequence r0 = r12.string
            if (r0 == 0) goto L_0x0116
            java.lang.CharSequence r0 = r12.string
            java.lang.String r9 = r0.toString()
            java.lang.String r0 = "res/"
            boolean r0 = r9.startsWith(r0)
            r8 = -3
            r16 = 0
            if (r0 != 0) goto L_0x0028
            if (r14 == 0) goto L_0x0027
            r14.callbackFailAsync(r8, r15)
        L_0x0027:
            return r16
        L_0x0028:
            int r0 = r12.assetCookie
            r7 = r25
            android.graphics.Typeface r6 = androidx.core.graphics.TypefaceCompat.findFromCache(r11, r13, r9, r0, r7)
            if (r6 == 0) goto L_0x0038
            if (r14 == 0) goto L_0x0037
            r14.callbackSuccessAsync(r6, r15)
        L_0x0037:
            return r6
        L_0x0038:
            if (r29 == 0) goto L_0x003b
            return r16
        L_0x003b:
            java.lang.String r0 = r9.toLowerCase()     // Catch:{ XmlPullParserException -> 0x00f3, IOException -> 0x00d0 }
            java.lang.String r1 = ".xml"
            boolean r0 = r0.endsWith(r1)     // Catch:{ XmlPullParserException -> 0x00c9, IOException -> 0x00d0 }
            if (r0 == 0) goto L_0x0090
            android.content.res.XmlResourceParser r0 = r11.getXml(r13)     // Catch:{ XmlPullParserException -> 0x00c9, IOException -> 0x00d0 }
            androidx.core.content.res.FontResourcesParserCompat$FamilyResourceEntry r1 = androidx.core.content.res.FontResourcesParserCompat.parse(r0, r11)     // Catch:{ XmlPullParserException -> 0x00c9, IOException -> 0x00d0 }
            r17 = r1
            if (r17 != 0) goto L_0x006d
            java.lang.String r1 = "Failed to find font-family tag"
            android.util.Log.e(r10, r1)     // Catch:{ XmlPullParserException -> 0x0067, IOException -> 0x005f }
            if (r14 == 0) goto L_0x005e
            r14.callbackFailAsync(r8, r15)     // Catch:{ XmlPullParserException -> 0x0067, IOException -> 0x005f }
        L_0x005e:
            return r16
        L_0x005f:
            r0 = move-exception
            r11 = r8
            r18 = r9
            r20 = r10
            goto L_0x00d8
        L_0x0067:
            r0 = move-exception
            r11 = r8
            r2 = r9
            r3 = r10
            goto L_0x00f9
        L_0x006d:
            int r5 = r12.assetCookie     // Catch:{ XmlPullParserException -> 0x00c9, IOException -> 0x00d0 }
            r1 = r21
            r2 = r17
            r3 = r22
            r4 = r24
            r18 = r5
            r5 = r9
            r19 = r6
            r6 = r18
            r7 = r25
            r11 = r8
            r8 = r26
            r18 = r9
            r9 = r27
            r20 = r10
            r10 = r28
            android.graphics.Typeface r1 = androidx.core.graphics.TypefaceCompat.createFromResourcesFamilyXml(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)     // Catch:{ XmlPullParserException -> 0x00c1, IOException -> 0x00bd }
            return r1
        L_0x0090:
            r19 = r6
            r11 = r8
            r18 = r9
            r20 = r10
            int r5 = r12.assetCookie     // Catch:{ XmlPullParserException -> 0x00c1, IOException -> 0x00bd }
            r1 = r21
            r2 = r22
            r3 = r24
            r4 = r18
            r6 = r25
            android.graphics.Typeface r0 = androidx.core.graphics.TypefaceCompat.createFromResourcesFontFile(r1, r2, r3, r4, r5, r6)     // Catch:{ XmlPullParserException -> 0x00c1, IOException -> 0x00bd }
            r6 = r0
            if (r14 == 0) goto L_0x00bc
            if (r6 == 0) goto L_0x00b0
            r14.callbackSuccessAsync(r6, r15)     // Catch:{ XmlPullParserException -> 0x00b6, IOException -> 0x00b4 }
            goto L_0x00bc
        L_0x00b0:
            r14.callbackFailAsync(r11, r15)     // Catch:{ XmlPullParserException -> 0x00b6, IOException -> 0x00b4 }
            goto L_0x00bc
        L_0x00b4:
            r0 = move-exception
            goto L_0x00d8
        L_0x00b6:
            r0 = move-exception
            r2 = r18
            r3 = r20
            goto L_0x00f9
        L_0x00bc:
            return r6
        L_0x00bd:
            r0 = move-exception
            r6 = r19
            goto L_0x00d8
        L_0x00c1:
            r0 = move-exception
            r2 = r18
            r6 = r19
            r3 = r20
            goto L_0x00f9
        L_0x00c9:
            r0 = move-exception
            r19 = r6
            r11 = r8
            r2 = r9
            r3 = r10
            goto L_0x00f9
        L_0x00d0:
            r0 = move-exception
            r19 = r6
            r11 = r8
            r18 = r9
            r20 = r10
        L_0x00d8:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Failed to read xml resource "
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = r18
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r3 = r20
            android.util.Log.e(r3, r1, r0)
            goto L_0x0110
        L_0x00f3:
            r0 = move-exception
            r19 = r6
            r11 = r8
            r2 = r9
            r3 = r10
        L_0x00f9:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r4 = "Failed to parse xml resource "
            java.lang.StringBuilder r1 = r1.append(r4)
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.e(r3, r1, r0)
        L_0x0110:
            if (r14 == 0) goto L_0x0115
            r14.callbackFailAsync(r11, r15)
        L_0x0115:
            return r16
        L_0x0116:
            android.content.res.Resources$NotFoundException r0 = new android.content.res.Resources$NotFoundException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Resource \""
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = r22
            java.lang.String r3 = r2.getResourceName(r13)
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.String r3 = "\" ("
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.String r3 = java.lang.Integer.toHexString(r24)
            mt.Log1F380D.a((java.lang.Object) r3)
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.String r3 = ") is not a Font: "
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.StringBuilder r1 = r1.append(r12)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.res.ResourcesCompat.loadFont(android.content.Context, android.content.res.Resources, android.util.TypedValue, int, int, androidx.core.content.res.ResourcesCompat$FontCallback, android.os.Handler, boolean, boolean):android.graphics.Typeface");
    }
}
