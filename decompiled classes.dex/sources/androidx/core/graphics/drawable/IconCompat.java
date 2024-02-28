package androidx.core.graphics.drawable;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import mt.Log1F380D;

/* compiled from: 0044 */
public class IconCompat extends CustomVersionedParcelable {
    private static final float ADAPTIVE_ICON_INSET_FACTOR = 0.25f;
    private static final int AMBIENT_SHADOW_ALPHA = 30;
    private static final float BLUR_FACTOR = 0.010416667f;
    static final PorterDuff.Mode DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;
    private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667f;
    static final String EXTRA_INT1 = "int1";
    static final String EXTRA_INT2 = "int2";
    static final String EXTRA_OBJ = "obj";
    static final String EXTRA_STRING1 = "string1";
    static final String EXTRA_TINT_LIST = "tint_list";
    static final String EXTRA_TINT_MODE = "tint_mode";
    static final String EXTRA_TYPE = "type";
    private static final float ICON_DIAMETER_FACTOR = 0.9166667f;
    private static final int KEY_SHADOW_ALPHA = 61;
    private static final float KEY_SHADOW_OFFSET_FACTOR = 0.020833334f;
    private static final String TAG = "IconCompat";
    public static final int TYPE_ADAPTIVE_BITMAP = 5;
    public static final int TYPE_BITMAP = 1;
    public static final int TYPE_DATA = 3;
    public static final int TYPE_RESOURCE = 2;
    public static final int TYPE_UNKNOWN = -1;
    public static final int TYPE_URI = 4;
    public static final int TYPE_URI_ADAPTIVE_BITMAP = 6;
    public byte[] mData = null;
    public int mInt1 = 0;
    public int mInt2 = 0;
    Object mObj1;
    public Parcelable mParcelable = null;
    public String mString1;
    public ColorStateList mTintList = null;
    PorterDuff.Mode mTintMode = DEFAULT_TINT_MODE;
    public String mTintModeStr = null;
    public int mType = -1;

    /* compiled from: 0043 */
    static class Api23Impl {
        private Api23Impl() {
        }

        static IconCompat createFromIcon(Context context, Icon icon) {
            switch (getType(icon)) {
                case 2:
                    String resPackage = getResPackage(icon);
                    Log1F380D.a((Object) resPackage);
                    try {
                        return IconCompat.createWithResource(IconCompat.getResources(context, resPackage), resPackage, getResId(icon));
                    } catch (Resources.NotFoundException e) {
                        throw new IllegalArgumentException("Icon resource cannot be found");
                    }
                case 4:
                    return IconCompat.createWithContentUri(getUri(icon));
                case 6:
                    return IconCompat.createWithAdaptiveBitmapContentUri(getUri(icon));
                default:
                    IconCompat iconCompat = new IconCompat(-1);
                    iconCompat.mObj1 = icon;
                    return iconCompat;
            }
        }

        static int getResId(Object icon) {
            if (Build.VERSION.SDK_INT >= 28) {
                return Api28Impl.getResId(icon);
            }
            try {
                return ((Integer) icon.getClass().getMethod("getResId", new Class[0]).invoke(icon, new Object[0])).intValue();
            } catch (IllegalAccessException e) {
                Log.e(IconCompat.TAG, "Unable to get icon resource", e);
                return 0;
            } catch (InvocationTargetException e2) {
                Log.e(IconCompat.TAG, "Unable to get icon resource", e2);
                return 0;
            } catch (NoSuchMethodException e3) {
                Log.e(IconCompat.TAG, "Unable to get icon resource", e3);
                return 0;
            }
        }

        static int getType(Object icon) {
            if (Build.VERSION.SDK_INT >= 28) {
                return Api28Impl.getType(icon);
            }
            try {
                return ((Integer) icon.getClass().getMethod("getType", new Class[0]).invoke(icon, new Object[0])).intValue();
            } catch (IllegalAccessException e) {
                Log.e(IconCompat.TAG, "Unable to get icon type " + icon, e);
                return -1;
            } catch (InvocationTargetException e2) {
                Log.e(IconCompat.TAG, "Unable to get icon type " + icon, e2);
                return -1;
            } catch (NoSuchMethodException e3) {
                Log.e(IconCompat.TAG, "Unable to get icon type " + icon, e3);
                return -1;
            }
        }

        static Uri getUri(Object icon) {
            if (Build.VERSION.SDK_INT >= 28) {
                return Api28Impl.getUri(icon);
            }
            try {
                return (Uri) icon.getClass().getMethod("getUri", new Class[0]).invoke(icon, new Object[0]);
            } catch (IllegalAccessException e) {
                Log.e(IconCompat.TAG, "Unable to get icon uri", e);
                return null;
            } catch (InvocationTargetException e2) {
                Log.e(IconCompat.TAG, "Unable to get icon uri", e2);
                return null;
            } catch (NoSuchMethodException e3) {
                Log.e(IconCompat.TAG, "Unable to get icon uri", e3);
                return null;
            }
        }

        static Drawable loadDrawable(Icon icon, Context context) {
            return icon.loadDrawable(context);
        }

        static Icon toIcon(IconCompat iconCompat, Context context) {
            Icon icon;
            switch (iconCompat.mType) {
                case -1:
                    return (Icon) iconCompat.mObj1;
                case 1:
                    icon = Icon.createWithBitmap((Bitmap) iconCompat.mObj1);
                    break;
                case 2:
                    icon = Icon.createWithResource(iconCompat.getResPackage(), iconCompat.mInt1);
                    break;
                case 3:
                    icon = Icon.createWithData((byte[]) iconCompat.mObj1, iconCompat.mInt1, iconCompat.mInt2);
                    break;
                case 4:
                    icon = Icon.createWithContentUri((String) iconCompat.mObj1);
                    break;
                case 5:
                    if (Build.VERSION.SDK_INT < 26) {
                        icon = Icon.createWithBitmap(IconCompat.createLegacyIconFromAdaptiveIcon((Bitmap) iconCompat.mObj1, false));
                        break;
                    } else {
                        icon = Api26Impl.createWithAdaptiveBitmap((Bitmap) iconCompat.mObj1);
                        break;
                    }
                case 6:
                    if (Build.VERSION.SDK_INT >= 30) {
                        icon = Api30Impl.createWithAdaptiveBitmapContentUri(iconCompat.getUri());
                        break;
                    } else if (context != null) {
                        InputStream uriInputStream = iconCompat.getUriInputStream(context);
                        if (uriInputStream != null) {
                            if (Build.VERSION.SDK_INT < 26) {
                                icon = Icon.createWithBitmap(IconCompat.createLegacyIconFromAdaptiveIcon(BitmapFactory.decodeStream(uriInputStream), false));
                                break;
                            } else {
                                icon = Api26Impl.createWithAdaptiveBitmap(BitmapFactory.decodeStream(uriInputStream));
                                break;
                            }
                        } else {
                            throw new IllegalStateException("Cannot load adaptive icon from uri: " + iconCompat.getUri());
                        }
                    } else {
                        throw new IllegalArgumentException("Context is required to resolve the file uri of the icon: " + iconCompat.getUri());
                    }
                default:
                    throw new IllegalArgumentException("Unknown type");
            }
            if (iconCompat.mTintList != null) {
                icon.setTintList(iconCompat.mTintList);
            }
            if (iconCompat.mTintMode != IconCompat.DEFAULT_TINT_MODE) {
                icon.setTintMode(iconCompat.mTintMode);
            }
            return icon;
        }

        static IconCompat createFromIconInner(Object icon) {
            Preconditions.checkNotNull(icon);
            switch (getType(icon)) {
                case 2:
                    String resPackage = getResPackage(icon);
                    Log1F380D.a((Object) resPackage);
                    return IconCompat.createWithResource((Resources) null, resPackage, getResId(icon));
                case 4:
                    return IconCompat.createWithContentUri(getUri(icon));
                case 6:
                    return IconCompat.createWithAdaptiveBitmapContentUri(getUri(icon));
                default:
                    IconCompat iconCompat = new IconCompat(-1);
                    iconCompat.mObj1 = icon;
                    return iconCompat;
            }
        }

        static String getResPackage(Object icon) {
            if (Build.VERSION.SDK_INT >= 28) {
                String resPackage = Api28Impl.getResPackage(icon);
                Log1F380D.a((Object) resPackage);
                return resPackage;
            }
            try {
                return (String) icon.getClass().getMethod("getResPackage", new Class[0]).invoke(icon, new Object[0]);
            } catch (IllegalAccessException e) {
                Log.e(IconCompat.TAG, "Unable to get icon package", e);
                return null;
            } catch (InvocationTargetException e2) {
                Log.e(IconCompat.TAG, "Unable to get icon package", e2);
                return null;
            } catch (NoSuchMethodException e3) {
                Log.e(IconCompat.TAG, "Unable to get icon package", e3);
                return null;
            }
        }
    }

    static class Api26Impl {
        private Api26Impl() {
        }

        static Drawable createAdaptiveIconDrawable(Drawable backgroundDrawable, Drawable foregroundDrawable) {
            return new AdaptiveIconDrawable(backgroundDrawable, foregroundDrawable);
        }

        static Icon createWithAdaptiveBitmap(Bitmap bits) {
            return Icon.createWithAdaptiveBitmap(bits);
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static int getResId(Object icon) {
            return ((Icon) icon).getResId();
        }

        static String getResPackage(Object icon) {
            return ((Icon) icon).getResPackage();
        }

        static int getType(Object icon) {
            return ((Icon) icon).getType();
        }

        static Uri getUri(Object icon) {
            return ((Icon) icon).getUri();
        }
    }

    static class Api30Impl {
        private Api30Impl() {
        }

        static Icon createWithAdaptiveBitmapContentUri(Uri uri) {
            return Icon.createWithAdaptiveBitmapContentUri(uri);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface IconType {
    }

    public IconCompat() {
    }

    IconCompat(int mType2) {
        this.mType = mType2;
    }

    public static IconCompat createFromBundle(Bundle bundle) {
        int i = bundle.getInt(EXTRA_TYPE);
        IconCompat iconCompat = new IconCompat(i);
        iconCompat.mInt1 = bundle.getInt(EXTRA_INT1);
        iconCompat.mInt2 = bundle.getInt(EXTRA_INT2);
        iconCompat.mString1 = bundle.getString(EXTRA_STRING1);
        if (bundle.containsKey(EXTRA_TINT_LIST)) {
            iconCompat.mTintList = (ColorStateList) bundle.getParcelable(EXTRA_TINT_LIST);
        }
        if (bundle.containsKey(EXTRA_TINT_MODE)) {
            iconCompat.mTintMode = PorterDuff.Mode.valueOf(bundle.getString(EXTRA_TINT_MODE));
        }
        switch (i) {
            case -1:
            case 1:
            case 5:
                iconCompat.mObj1 = bundle.getParcelable(EXTRA_OBJ);
                break;
            case 2:
            case 4:
            case 6:
                iconCompat.mObj1 = bundle.getString(EXTRA_OBJ);
                break;
            case 3:
                iconCompat.mObj1 = bundle.getByteArray(EXTRA_OBJ);
                break;
            default:
                Log.w(TAG, "Unknown type " + i);
                return null;
        }
        return iconCompat;
    }

    public static IconCompat createFromIcon(Context context, Icon icon) {
        Preconditions.checkNotNull(icon);
        return Api23Impl.createFromIcon(context, icon);
    }

    public static IconCompat createFromIcon(Icon icon) {
        return Api23Impl.createFromIconInner(icon);
    }

    public static IconCompat createFromIconOrNullIfZeroResId(Icon icon) {
        if (Api23Impl.getType(icon) == 2 && Api23Impl.getResId(icon) == 0) {
            return null;
        }
        return Api23Impl.createFromIconInner(icon);
    }

    static Bitmap createLegacyIconFromAdaptiveIcon(Bitmap adaptiveIconBitmap, boolean addShadow) {
        int min = (int) (((float) Math.min(adaptiveIconBitmap.getWidth(), adaptiveIconBitmap.getHeight())) * DEFAULT_VIEW_PORT_SCALE);
        Bitmap createBitmap = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(3);
        float f = ((float) min) * 0.5f;
        float f2 = ICON_DIAMETER_FACTOR * f;
        if (addShadow) {
            float f3 = ((float) min) * BLUR_FACTOR;
            paint.setColor(0);
            paint.setShadowLayer(f3, 0.0f, ((float) min) * KEY_SHADOW_OFFSET_FACTOR, 1023410176);
            canvas.drawCircle(f, f, f2, paint);
            paint.setShadowLayer(f3, 0.0f, 0.0f, 503316480);
            canvas.drawCircle(f, f, f2, paint);
            paint.clearShadowLayer();
        }
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        BitmapShader bitmapShader = new BitmapShader(adaptiveIconBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        matrix.setTranslate(((float) (-(adaptiveIconBitmap.getWidth() - min))) / 2.0f, ((float) (-(adaptiveIconBitmap.getHeight() - min))) / 2.0f);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        canvas.drawCircle(f, f, f2, paint);
        canvas.setBitmap((Bitmap) null);
        return createBitmap;
    }

    public static IconCompat createWithAdaptiveBitmap(Bitmap bits) {
        ObjectsCompat.requireNonNull(bits);
        IconCompat iconCompat = new IconCompat(5);
        iconCompat.mObj1 = bits;
        return iconCompat;
    }

    public static IconCompat createWithAdaptiveBitmapContentUri(Uri uri) {
        ObjectsCompat.requireNonNull(uri);
        return createWithAdaptiveBitmapContentUri(uri.toString());
    }

    public static IconCompat createWithAdaptiveBitmapContentUri(String uri) {
        ObjectsCompat.requireNonNull(uri);
        IconCompat iconCompat = new IconCompat(6);
        iconCompat.mObj1 = uri;
        return iconCompat;
    }

    public static IconCompat createWithBitmap(Bitmap bits) {
        ObjectsCompat.requireNonNull(bits);
        IconCompat iconCompat = new IconCompat(1);
        iconCompat.mObj1 = bits;
        return iconCompat;
    }

    public static IconCompat createWithContentUri(Uri uri) {
        ObjectsCompat.requireNonNull(uri);
        return createWithContentUri(uri.toString());
    }

    public static IconCompat createWithContentUri(String uri) {
        ObjectsCompat.requireNonNull(uri);
        IconCompat iconCompat = new IconCompat(4);
        iconCompat.mObj1 = uri;
        return iconCompat;
    }

    public static IconCompat createWithData(byte[] data, int offset, int length) {
        ObjectsCompat.requireNonNull(data);
        IconCompat iconCompat = new IconCompat(3);
        iconCompat.mObj1 = data;
        iconCompat.mInt1 = offset;
        iconCompat.mInt2 = length;
        return iconCompat;
    }

    public static IconCompat createWithResource(Context context, int resId) {
        ObjectsCompat.requireNonNull(context);
        return createWithResource(context.getResources(), context.getPackageName(), resId);
    }

    public static IconCompat createWithResource(Resources r, String pkg, int resId) {
        ObjectsCompat.requireNonNull(pkg);
        if (resId != 0) {
            IconCompat iconCompat = new IconCompat(2);
            iconCompat.mInt1 = resId;
            if (r != null) {
                try {
                    iconCompat.mObj1 = r.getResourceName(resId);
                } catch (Resources.NotFoundException e) {
                    throw new IllegalArgumentException("Icon resource cannot be found");
                }
            } else {
                iconCompat.mObj1 = pkg;
            }
            iconCompat.mString1 = pkg;
            return iconCompat;
        }
        throw new IllegalArgumentException("Drawable resource ID must not be 0");
    }

    static Resources getResources(Context context, String resPackage) {
        if ("android".equals(resPackage)) {
            return Resources.getSystem();
        }
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(resPackage, 8192);
            if (applicationInfo != null) {
                return packageManager.getResourcesForApplication(applicationInfo);
            }
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            String format = String.format("Unable to find pkg=%s for icon", new Object[]{resPackage});
            Log1F380D.a((Object) format);
            Log.e(TAG, format, e);
            return null;
        }
    }

    private static String typeToString(int x) {
        switch (x) {
            case 1:
                return "BITMAP";
            case 2:
                return "RESOURCE";
            case 3:
                return "DATA";
            case 4:
                return "URI";
            case 5:
                return "BITMAP_MASKABLE";
            case 6:
                return "URI_MASKABLE";
            default:
                return "UNKNOWN";
        }
    }

    public void addToShortcutIntent(Intent outIntent, Drawable badge, Context c) {
        Bitmap bitmap;
        Bitmap bitmap2;
        checkResource(c);
        switch (this.mType) {
            case 1:
                bitmap = (Bitmap) this.mObj1;
                if (badge != null) {
                    bitmap = bitmap.copy(bitmap.getConfig(), true);
                    break;
                }
                break;
            case 2:
                try {
                    Context createPackageContext = c.createPackageContext(getResPackage(), 0);
                    if (badge == null) {
                        outIntent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(createPackageContext, this.mInt1));
                        return;
                    }
                    Drawable drawable = ContextCompat.getDrawable(createPackageContext, this.mInt1);
                    if (drawable.getIntrinsicWidth() > 0) {
                        if (drawable.getIntrinsicHeight() > 0) {
                            bitmap2 = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                            drawable.setBounds(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
                            drawable.draw(new Canvas(bitmap2));
                            bitmap = bitmap2;
                            break;
                        }
                    }
                    int launcherLargeIconSize = ((ActivityManager) createPackageContext.getSystemService("activity")).getLauncherLargeIconSize();
                    bitmap2 = Bitmap.createBitmap(launcherLargeIconSize, launcherLargeIconSize, Bitmap.Config.ARGB_8888);
                    drawable.setBounds(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
                    drawable.draw(new Canvas(bitmap2));
                    bitmap = bitmap2;
                } catch (PackageManager.NameNotFoundException e) {
                    throw new IllegalArgumentException("Can't find package " + this.mObj1, e);
                }
            case 5:
                bitmap = createLegacyIconFromAdaptiveIcon((Bitmap) this.mObj1, true);
                break;
            default:
                throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
        }
        if (badge != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            badge.setBounds(width / 2, height / 2, width, height);
            badge.draw(new Canvas(bitmap));
        }
        outIntent.putExtra("android.intent.extra.shortcut.ICON", bitmap);
    }

    public void checkResource(Context context) {
        Object obj;
        if (this.mType == 2 && (obj = this.mObj1) != null) {
            String str = (String) obj;
            if (str.contains(":")) {
                String str2 = str.split(":", -1)[1];
                String str3 = str2.split("/", -1)[0];
                String str4 = str2.split("/", -1)[1];
                String str5 = str.split(":", -1)[0];
                if ("0_resource_name_obfuscated".equals(str4)) {
                    Log.i(TAG, "Found obfuscated resource, not trying to update resource id for it");
                    return;
                }
                String resPackage = getResPackage();
                int identifier = getResources(context, resPackage).getIdentifier(str4, str3, str5);
                if (this.mInt1 != identifier) {
                    Log.i(TAG, "Id has changed for " + resPackage + " " + str);
                    this.mInt1 = identifier;
                }
            }
        }
    }

    public Bitmap getBitmap() {
        if (this.mType != -1 || Build.VERSION.SDK_INT < 23) {
            int i = this.mType;
            if (i == 1) {
                return (Bitmap) this.mObj1;
            }
            if (i == 5) {
                return createLegacyIconFromAdaptiveIcon((Bitmap) this.mObj1, true);
            }
            throw new IllegalStateException("called getBitmap() on " + this);
        }
        Object obj = this.mObj1;
        if (obj instanceof Bitmap) {
            return (Bitmap) obj;
        }
        return null;
    }

    public int getResId() {
        if (this.mType == -1 && Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.getResId(this.mObj1);
        }
        if (this.mType == 2) {
            return this.mInt1;
        }
        throw new IllegalStateException("called getResId() on " + this);
    }

    public int getType() {
        return (this.mType != -1 || Build.VERSION.SDK_INT < 23) ? this.mType : Api23Impl.getType(this.mObj1);
    }

    public Uri getUri() {
        if (this.mType == -1 && Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.getUri(this.mObj1);
        }
        int i = this.mType;
        if (i == 4 || i == 6) {
            return Uri.parse((String) this.mObj1);
        }
        throw new IllegalStateException("called getUri() on " + this);
    }

    public InputStream getUriInputStream(Context context) {
        Uri uri = getUri();
        String scheme = uri.getScheme();
        if ("content".equals(scheme) || "file".equals(scheme)) {
            try {
                return context.getContentResolver().openInputStream(uri);
            } catch (Exception e) {
                Log.w(TAG, "Unable to load image from URI: " + uri, e);
                return null;
            }
        } else {
            try {
                return new FileInputStream(new File((String) this.mObj1));
            } catch (FileNotFoundException e2) {
                Log.w(TAG, "Unable to load image from path: " + uri, e2);
                return null;
            }
        }
    }

    public Drawable loadDrawable(Context context) {
        checkResource(context);
        if (Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.loadDrawable(toIcon(context), context);
        }
        Drawable loadDrawableInner = loadDrawableInner(context);
        if (!(loadDrawableInner == null || (this.mTintList == null && this.mTintMode == DEFAULT_TINT_MODE))) {
            loadDrawableInner.mutate();
            DrawableCompat.setTintList(loadDrawableInner, this.mTintList);
            DrawableCompat.setTintMode(loadDrawableInner, this.mTintMode);
        }
        return loadDrawableInner;
    }

    public void onPreParceling(boolean isStream) {
        this.mTintModeStr = this.mTintMode.name();
        switch (this.mType) {
            case -1:
                if (!isStream) {
                    this.mParcelable = (Parcelable) this.mObj1;
                    return;
                }
                throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
            case 1:
            case 5:
                if (isStream) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ((Bitmap) this.mObj1).compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
                    this.mData = byteArrayOutputStream.toByteArray();
                    return;
                }
                this.mParcelable = (Parcelable) this.mObj1;
                return;
            case 2:
                this.mData = ((String) this.mObj1).getBytes(Charset.forName("UTF-16"));
                return;
            case 3:
                this.mData = (byte[]) this.mObj1;
                return;
            case 4:
            case 6:
                this.mData = this.mObj1.toString().getBytes(Charset.forName("UTF-16"));
                return;
            default:
                return;
        }
    }

    public IconCompat setTint(int tint) {
        return setTintList(ColorStateList.valueOf(tint));
    }

    public IconCompat setTintList(ColorStateList tintList) {
        this.mTintList = tintList;
        return this;
    }

    public IconCompat setTintMode(PorterDuff.Mode mode) {
        this.mTintMode = mode;
        return this;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        switch (this.mType) {
            case -1:
                bundle.putParcelable(EXTRA_OBJ, (Parcelable) this.mObj1);
                break;
            case 1:
            case 5:
                bundle.putParcelable(EXTRA_OBJ, (Bitmap) this.mObj1);
                break;
            case 2:
            case 4:
            case 6:
                bundle.putString(EXTRA_OBJ, (String) this.mObj1);
                break;
            case 3:
                bundle.putByteArray(EXTRA_OBJ, (byte[]) this.mObj1);
                break;
            default:
                throw new IllegalArgumentException("Invalid icon");
        }
        bundle.putInt(EXTRA_TYPE, this.mType);
        bundle.putInt(EXTRA_INT1, this.mInt1);
        bundle.putInt(EXTRA_INT2, this.mInt2);
        bundle.putString(EXTRA_STRING1, this.mString1);
        ColorStateList colorStateList = this.mTintList;
        if (colorStateList != null) {
            bundle.putParcelable(EXTRA_TINT_LIST, colorStateList);
        }
        PorterDuff.Mode mode = this.mTintMode;
        if (mode != DEFAULT_TINT_MODE) {
            bundle.putString(EXTRA_TINT_MODE, mode.name());
        }
        return bundle;
    }

    @Deprecated
    public Icon toIcon() {
        return toIcon((Context) null);
    }

    public Icon toIcon(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.toIcon(this, context);
        }
        throw new UnsupportedOperationException("This method is only supported on API level 23+");
    }

    private Drawable loadDrawableInner(Context context) {
        switch (this.mType) {
            case 1:
                return new BitmapDrawable(context.getResources(), (Bitmap) this.mObj1);
            case 2:
                String resPackage = getResPackage();
                if (TextUtils.isEmpty(resPackage)) {
                    resPackage = context.getPackageName();
                }
                try {
                    return ResourcesCompat.getDrawable(getResources(context, resPackage), this.mInt1, context.getTheme());
                } catch (RuntimeException e) {
                    String format = String.format("Unable to load resource 0x%08x from pkg=%s", new Object[]{Integer.valueOf(this.mInt1), this.mObj1});
                    Log1F380D.a((Object) format);
                    Log.e(TAG, format, e);
                    break;
                }
            case 3:
                return new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray((byte[]) this.mObj1, this.mInt1, this.mInt2));
            case 4:
                InputStream uriInputStream = getUriInputStream(context);
                if (uriInputStream != null) {
                    return new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(uriInputStream));
                }
                break;
            case 5:
                return new BitmapDrawable(context.getResources(), createLegacyIconFromAdaptiveIcon((Bitmap) this.mObj1, false));
            case 6:
                InputStream uriInputStream2 = getUriInputStream(context);
                if (uriInputStream2 != null) {
                    return Build.VERSION.SDK_INT >= 26 ? Api26Impl.createAdaptiveIconDrawable((Drawable) null, new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(uriInputStream2))) : new BitmapDrawable(context.getResources(), createLegacyIconFromAdaptiveIcon(BitmapFactory.decodeStream(uriInputStream2), false));
                }
                break;
        }
        return null;
    }

    public String getResPackage() {
        if (this.mType == -1 && Build.VERSION.SDK_INT >= 23) {
            String resPackage = Api23Impl.getResPackage(this.mObj1);
            Log1F380D.a((Object) resPackage);
            return resPackage;
        } else if (this.mType == 2) {
            String str = this.mString1;
            return (str == null || TextUtils.isEmpty(str)) ? ((String) this.mObj1).split(":", -1)[0] : this.mString1;
        } else {
            throw new IllegalStateException("called getResPackage() on " + this);
        }
    }

    public void onPostParceling() {
        this.mTintMode = PorterDuff.Mode.valueOf(this.mTintModeStr);
        switch (this.mType) {
            case -1:
                Parcelable parcelable = this.mParcelable;
                if (parcelable != null) {
                    this.mObj1 = parcelable;
                    return;
                }
                throw new IllegalArgumentException("Invalid icon");
            case 1:
            case 5:
                Parcelable parcelable2 = this.mParcelable;
                if (parcelable2 != null) {
                    this.mObj1 = parcelable2;
                    return;
                }
                byte[] bArr = this.mData;
                this.mObj1 = bArr;
                this.mType = 3;
                this.mInt1 = 0;
                this.mInt2 = bArr.length;
                return;
            case 2:
            case 4:
            case 6:
                String str = new String(this.mData, Charset.forName("UTF-16"));
                Log1F380D.a((Object) str);
                this.mObj1 = str;
                if (this.mType == 2 && this.mString1 == null) {
                    String str2 = str;
                    this.mString1 = str.split(":", -1)[0];
                    return;
                }
                return;
            case 3:
                this.mObj1 = this.mData;
                return;
            default:
                return;
        }
    }

    public String toString() {
        if (this.mType == -1) {
            String valueOf = String.valueOf(this.mObj1);
            Log1F380D.a((Object) valueOf);
            return valueOf;
        }
        StringBuilder sb = new StringBuilder("Icon(typ=");
        String typeToString = typeToString(this.mType);
        Log1F380D.a((Object) typeToString);
        StringBuilder append = sb.append(typeToString);
        switch (this.mType) {
            case 1:
            case 5:
                append.append(" size=").append(((Bitmap) this.mObj1).getWidth()).append("x").append(((Bitmap) this.mObj1).getHeight());
                break;
            case 2:
                StringBuilder append2 = append.append(" pkg=").append(this.mString1).append(" id=");
                String format = String.format("0x%08x", new Object[]{Integer.valueOf(getResId())});
                Log1F380D.a((Object) format);
                append2.append(format);
                break;
            case 3:
                append.append(" len=").append(this.mInt1);
                if (this.mInt2 != 0) {
                    append.append(" off=").append(this.mInt2);
                    break;
                }
                break;
            case 4:
            case 6:
                append.append(" uri=").append(this.mObj1);
                break;
        }
        if (this.mTintList != null) {
            append.append(" tint=");
            append.append(this.mTintList);
        }
        if (this.mTintMode != DEFAULT_TINT_MODE) {
            append.append(" mode=").append(this.mTintMode);
        }
        append.append(")");
        return append.toString();
    }
}
