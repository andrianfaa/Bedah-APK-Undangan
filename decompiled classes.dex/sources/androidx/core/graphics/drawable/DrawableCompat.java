package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import java.io.IOException;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class DrawableCompat {
    private static final String TAG = "DrawableCompat";
    private static Method sGetLayoutDirectionMethod;
    private static boolean sGetLayoutDirectionMethodFetched;
    private static Method sSetLayoutDirectionMethod;
    private static boolean sSetLayoutDirectionMethodFetched;

    static class Api19Impl {
        private Api19Impl() {
        }

        static int getAlpha(Drawable drawable) {
            return drawable.getAlpha();
        }

        static Drawable getChild(DrawableContainer.DrawableContainerState drawableContainerState, int index) {
            return drawableContainerState.getChild(index);
        }

        static Drawable getDrawable(InsetDrawable drawable) {
            return drawable.getDrawable();
        }

        static boolean isAutoMirrored(Drawable drawable) {
            return drawable.isAutoMirrored();
        }

        static void setAutoMirrored(Drawable drawable, boolean mirrored) {
            drawable.setAutoMirrored(mirrored);
        }
    }

    static class Api21Impl {
        private Api21Impl() {
        }

        static void applyTheme(Drawable drawable, Resources.Theme t) {
            drawable.applyTheme(t);
        }

        static boolean canApplyTheme(Drawable drawable) {
            return drawable.canApplyTheme();
        }

        static ColorFilter getColorFilter(Drawable drawable) {
            return drawable.getColorFilter();
        }

        static void inflate(Drawable drawable, Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
            drawable.inflate(r, parser, attrs, theme);
        }

        static void setHotspot(Drawable drawable, float x, float y) {
            drawable.setHotspot(x, y);
        }

        static void setHotspotBounds(Drawable drawable, int left, int top, int right, int bottom) {
            drawable.setHotspotBounds(left, top, right, bottom);
        }

        static void setTint(Drawable drawable, int tintColor) {
            drawable.setTint(tintColor);
        }

        static void setTintList(Drawable drawable, ColorStateList tint) {
            drawable.setTintList(tint);
        }

        static void setTintMode(Drawable drawable, PorterDuff.Mode tintMode) {
            drawable.setTintMode(tintMode);
        }
    }

    static class Api23Impl {
        private Api23Impl() {
        }

        static int getLayoutDirection(Drawable drawable) {
            return drawable.getLayoutDirection();
        }

        static boolean setLayoutDirection(Drawable drawable, int layoutDirection) {
            return drawable.setLayoutDirection(layoutDirection);
        }
    }

    private DrawableCompat() {
    }

    public static void applyTheme(Drawable drawable, Resources.Theme theme) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.applyTheme(drawable, theme);
        }
    }

    public static boolean canApplyTheme(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.canApplyTheme(drawable);
        }
        return false;
    }

    public static void clearColorFilter(Drawable drawable) {
        DrawableContainer.DrawableContainerState drawableContainerState;
        if (Build.VERSION.SDK_INT >= 23) {
            drawable.clearColorFilter();
        } else if (Build.VERSION.SDK_INT >= 21) {
            drawable.clearColorFilter();
            if (drawable instanceof InsetDrawable) {
                clearColorFilter(Api19Impl.getDrawable((InsetDrawable) drawable));
            } else if (drawable instanceof WrappedDrawable) {
                clearColorFilter(((WrappedDrawable) drawable).getWrappedDrawable());
            } else if ((drawable instanceof DrawableContainer) && (drawableContainerState = (DrawableContainer.DrawableContainerState) ((DrawableContainer) drawable).getConstantState()) != null) {
                int childCount = drawableContainerState.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    Drawable child = Api19Impl.getChild(drawableContainerState, i);
                    if (child != null) {
                        clearColorFilter(child);
                    }
                }
            }
        } else {
            drawable.clearColorFilter();
        }
    }

    public static int getAlpha(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Api19Impl.getAlpha(drawable);
        }
        return 0;
    }

    public static ColorFilter getColorFilter(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getColorFilter(drawable);
        }
        return null;
    }

    public static int getLayoutDirection(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.getLayoutDirection(drawable);
        }
        if (Build.VERSION.SDK_INT < 17) {
            return 0;
        }
        if (!sGetLayoutDirectionMethodFetched) {
            try {
                Method declaredMethod = Drawable.class.getDeclaredMethod("getLayoutDirection", new Class[0]);
                sGetLayoutDirectionMethod = declaredMethod;
                declaredMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                Log.i(TAG, "Failed to retrieve getLayoutDirection() method", e);
            }
            sGetLayoutDirectionMethodFetched = true;
        }
        Method method = sGetLayoutDirectionMethod;
        if (method != null) {
            try {
                return ((Integer) method.invoke(drawable, new Object[0])).intValue();
            } catch (Exception e2) {
                Log.i(TAG, "Failed to invoke getLayoutDirection() via reflection", e2);
                sGetLayoutDirectionMethod = null;
            }
        }
        return 0;
    }

    public static void inflate(Drawable drawable, Resources res, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.inflate(drawable, res, parser, attrs, theme);
        } else {
            drawable.inflate(res, parser, attrs);
        }
    }

    public static boolean isAutoMirrored(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Api19Impl.isAutoMirrored(drawable);
        }
        return false;
    }

    @Deprecated
    public static void jumpToCurrentState(Drawable drawable) {
        drawable.jumpToCurrentState();
    }

    public static void setAutoMirrored(Drawable drawable, boolean mirrored) {
        if (Build.VERSION.SDK_INT >= 19) {
            Api19Impl.setAutoMirrored(drawable, mirrored);
        }
    }

    public static void setHotspot(Drawable drawable, float x, float y) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setHotspot(drawable, x, y);
        }
    }

    public static void setHotspotBounds(Drawable drawable, int left, int top, int right, int bottom) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setHotspotBounds(drawable, left, top, right, bottom);
        }
    }

    public static boolean setLayoutDirection(Drawable drawable, int layoutDirection) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.setLayoutDirection(drawable, layoutDirection);
        }
        if (Build.VERSION.SDK_INT < 17) {
            return false;
        }
        if (!sSetLayoutDirectionMethodFetched) {
            Class<Drawable> cls = Drawable.class;
            try {
                Method declaredMethod = cls.getDeclaredMethod("setLayoutDirection", new Class[]{Integer.TYPE});
                sSetLayoutDirectionMethod = declaredMethod;
                declaredMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                Log.i(TAG, "Failed to retrieve setLayoutDirection(int) method", e);
            }
            sSetLayoutDirectionMethodFetched = true;
        }
        Method method = sSetLayoutDirectionMethod;
        if (method != null) {
            try {
                method.invoke(drawable, new Object[]{Integer.valueOf(layoutDirection)});
                return true;
            } catch (Exception e2) {
                Log.i(TAG, "Failed to invoke setLayoutDirection(int) via reflection", e2);
                sSetLayoutDirectionMethod = null;
            }
        }
        return false;
    }

    public static void setTint(Drawable drawable, int tint) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setTint(drawable, tint);
        } else if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable) drawable).setTint(tint);
        }
    }

    public static void setTintList(Drawable drawable, ColorStateList tint) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setTintList(drawable, tint);
        } else if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable) drawable).setTintList(tint);
        }
    }

    public static void setTintMode(Drawable drawable, PorterDuff.Mode tintMode) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setTintMode(drawable, tintMode);
        } else if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable) drawable).setTintMode(tintMode);
        }
    }

    public static <T extends Drawable> T unwrap(Drawable drawable) {
        return drawable instanceof WrappedDrawable ? ((WrappedDrawable) drawable).getWrappedDrawable() : drawable;
    }

    public static Drawable wrap(Drawable drawable) {
        return Build.VERSION.SDK_INT >= 23 ? drawable : Build.VERSION.SDK_INT >= 21 ? !(drawable instanceof TintAwareDrawable) ? new WrappedDrawableApi21(drawable) : drawable : !(drawable instanceof TintAwareDrawable) ? new WrappedDrawableApi14(drawable) : drawable;
    }
}
