package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import androidx.collection.LruCache;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.provider.FontsContractCompat;
import mt.Log1F380D;

/* compiled from: 0041 */
public class TypefaceCompat {
    private static final LruCache<String, Typeface> sTypefaceCache = new LruCache<>(16);
    private static final TypefaceCompatBaseImpl sTypefaceCompatImpl;

    public static class ResourcesCallbackAdapter extends FontsContractCompat.FontRequestCallback {
        private ResourcesCompat.FontCallback mFontCallback;

        public ResourcesCallbackAdapter(ResourcesCompat.FontCallback fontCallback) {
            this.mFontCallback = fontCallback;
        }

        public void onTypefaceRequestFailed(int reason) {
            ResourcesCompat.FontCallback fontCallback = this.mFontCallback;
            if (fontCallback != null) {
                fontCallback.m6lambda$callbackFailAsync$1$androidxcorecontentresResourcesCompat$FontCallback(reason);
            }
        }

        public void onTypefaceRetrieved(Typeface typeface) {
            ResourcesCompat.FontCallback fontCallback = this.mFontCallback;
            if (fontCallback != null) {
                fontCallback.m7lambda$callbackSuccessAsync$0$androidxcorecontentresResourcesCompat$FontCallback(typeface);
            }
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 29) {
            sTypefaceCompatImpl = new TypefaceCompatApi29Impl();
        } else if (Build.VERSION.SDK_INT >= 28) {
            sTypefaceCompatImpl = new TypefaceCompatApi28Impl();
        } else if (Build.VERSION.SDK_INT >= 26) {
            sTypefaceCompatImpl = new TypefaceCompatApi26Impl();
        } else if (Build.VERSION.SDK_INT >= 24 && TypefaceCompatApi24Impl.isUsable()) {
            sTypefaceCompatImpl = new TypefaceCompatApi24Impl();
        } else if (Build.VERSION.SDK_INT >= 21) {
            sTypefaceCompatImpl = new TypefaceCompatApi21Impl();
        } else {
            sTypefaceCompatImpl = new TypefaceCompatBaseImpl();
        }
    }

    private TypefaceCompat() {
    }

    public static void clearCache() {
        sTypefaceCache.evictAll();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0009, code lost:
        r0 = getBestFontFromFamily(r3, r4, r5);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Typeface create(android.content.Context r3, android.graphics.Typeface r4, int r5) {
        /*
            if (r3 == 0) goto L_0x0015
            r0 = 0
            int r1 = android.os.Build.VERSION.SDK_INT
            r2 = 21
            if (r1 >= r2) goto L_0x0010
            android.graphics.Typeface r0 = getBestFontFromFamily(r3, r4, r5)
            if (r0 == 0) goto L_0x0010
            return r0
        L_0x0010:
            android.graphics.Typeface r1 = android.graphics.Typeface.create(r4, r5)
            return r1
        L_0x0015:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Context cannot be null"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.graphics.TypefaceCompat.create(android.content.Context, android.graphics.Typeface, int):android.graphics.Typeface");
    }

    public static Typeface createFromFontInfo(Context context, CancellationSignal cancellationSignal, FontsContractCompat.FontInfo[] fonts, int style) {
        return sTypefaceCompatImpl.createFromFontInfo(context, cancellationSignal, fonts, style);
    }

    @Deprecated
    public static Typeface createFromResourcesFamilyXml(Context context, FontResourcesParserCompat.FamilyResourceEntry entry, Resources resources, int id, int style, ResourcesCompat.FontCallback fontCallback, Handler handler, boolean isRequestFromLayoutInflator) {
        return createFromResourcesFamilyXml(context, entry, resources, id, (String) null, 0, style, fontCallback, handler, isRequestFromLayoutInflator);
    }

    public static Typeface createFromResourcesFamilyXml(Context context, FontResourcesParserCompat.FamilyResourceEntry entry, Resources resources, int id, String path, int cookie, int style, ResourcesCompat.FontCallback fontCallback, Handler handler, boolean isRequestFromLayoutInflator) {
        Typeface typeface;
        FontResourcesParserCompat.FamilyResourceEntry familyResourceEntry = entry;
        ResourcesCompat.FontCallback fontCallback2 = fontCallback;
        Handler handler2 = handler;
        if (familyResourceEntry instanceof FontResourcesParserCompat.ProviderResourceEntry) {
            FontResourcesParserCompat.ProviderResourceEntry providerResourceEntry = (FontResourcesParserCompat.ProviderResourceEntry) familyResourceEntry;
            Typeface systemFontFamily = getSystemFontFamily(providerResourceEntry.getSystemFontFamilyName());
            if (systemFontFamily != null) {
                if (fontCallback2 != null) {
                    fontCallback2.callbackSuccessAsync(systemFontFamily, handler2);
                }
                return systemFontFamily;
            }
            typeface = FontsContractCompat.requestFont(context, providerResourceEntry.getRequest(), style, isRequestFromLayoutInflator ? providerResourceEntry.getFetchStrategy() == 0 : fontCallback2 == null, isRequestFromLayoutInflator ? providerResourceEntry.getTimeout() : -1, ResourcesCompat.FontCallback.getHandler(handler), new ResourcesCallbackAdapter(fontCallback2));
            Context context2 = context;
            Resources resources2 = resources;
            int i = style;
        } else {
            Context context3 = context;
            typeface = sTypefaceCompatImpl.createFromFontFamilyFilesResourceEntry(context, (FontResourcesParserCompat.FontFamilyFilesResourceEntry) familyResourceEntry, resources, style);
            if (fontCallback2 != null) {
                if (typeface != null) {
                    fontCallback2.callbackSuccessAsync(typeface, handler2);
                } else {
                    fontCallback2.callbackFailAsync(-3, handler2);
                }
            }
        }
        if (typeface != null) {
            LruCache<String, Typeface> lruCache = sTypefaceCache;
            String createResourceUid = createResourceUid(resources, id, path, cookie, style);
            Log1F380D.a((Object) createResourceUid);
            lruCache.put(createResourceUid, typeface);
        }
        return typeface;
    }

    @Deprecated
    public static Typeface createFromResourcesFontFile(Context context, Resources resources, int id, String path, int style) {
        return createFromResourcesFontFile(context, resources, id, path, 0, style);
    }

    private static String createResourceUid(Resources resources, int id, String path, int cookie, int style) {
        return resources.getResourcePackageName(id) + '-' + path + '-' + cookie + '-' + id + '-' + style;
    }

    @Deprecated
    public static Typeface findFromCache(Resources resources, int id, int style) {
        return findFromCache(resources, id, (String) null, 0, style);
    }

    private static Typeface getBestFontFromFamily(Context context, Typeface typeface, int style) {
        TypefaceCompatBaseImpl typefaceCompatBaseImpl = sTypefaceCompatImpl;
        FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamily = typefaceCompatBaseImpl.getFontFamily(typeface);
        if (fontFamily == null) {
            return null;
        }
        return typefaceCompatBaseImpl.createFromFontFamilyFilesResourceEntry(context, fontFamily, context.getResources(), style);
    }

    private static Typeface getSystemFontFamily(String familyName) {
        if (familyName == null || familyName.isEmpty()) {
            return null;
        }
        Typeface create = Typeface.create(familyName, 0);
        Typeface create2 = Typeface.create(Typeface.DEFAULT, 0);
        if (create == null || create.equals(create2)) {
            return null;
        }
        return create;
    }

    public static Typeface createFromResourcesFontFile(Context context, Resources resources, int id, String path, int cookie, int style) {
        Typeface createFromResourcesFontFile = sTypefaceCompatImpl.createFromResourcesFontFile(context, resources, id, path, style);
        if (createFromResourcesFontFile != null) {
            String createResourceUid = createResourceUid(resources, id, path, cookie, style);
            Log1F380D.a((Object) createResourceUid);
            sTypefaceCache.put(createResourceUid, createFromResourcesFontFile);
        }
        return createFromResourcesFontFile;
    }

    public static Typeface findFromCache(Resources resources, int id, String path, int cookie, int style) {
        LruCache<String, Typeface> lruCache = sTypefaceCache;
        String createResourceUid = createResourceUid(resources, id, path, cookie, style);
        Log1F380D.a((Object) createResourceUid);
        return lruCache.get(createResourceUid);
    }
}
