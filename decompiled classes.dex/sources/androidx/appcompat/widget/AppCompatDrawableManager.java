package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.ResourceManagerInternal;
import androidx.core.graphics.ColorUtils;

public final class AppCompatDrawableManager {
    private static final boolean DEBUG = false;
    /* access modifiers changed from: private */
    public static final PorterDuff.Mode DEFAULT_MODE = PorterDuff.Mode.SRC_IN;
    private static AppCompatDrawableManager INSTANCE = null;
    private static final String TAG = "AppCompatDrawableManag";
    private ResourceManagerInternal mResourceManager;

    public static synchronized AppCompatDrawableManager get() {
        AppCompatDrawableManager appCompatDrawableManager;
        synchronized (AppCompatDrawableManager.class) {
            if (INSTANCE == null) {
                preload();
            }
            appCompatDrawableManager = INSTANCE;
        }
        return appCompatDrawableManager;
    }

    public static synchronized PorterDuffColorFilter getPorterDuffColorFilter(int color, PorterDuff.Mode mode) {
        PorterDuffColorFilter porterDuffColorFilter;
        synchronized (AppCompatDrawableManager.class) {
            porterDuffColorFilter = ResourceManagerInternal.getPorterDuffColorFilter(color, mode);
        }
        return porterDuffColorFilter;
    }

    public static synchronized void preload() {
        synchronized (AppCompatDrawableManager.class) {
            if (INSTANCE == null) {
                AppCompatDrawableManager appCompatDrawableManager = new AppCompatDrawableManager();
                INSTANCE = appCompatDrawableManager;
                appCompatDrawableManager.mResourceManager = ResourceManagerInternal.get();
                INSTANCE.mResourceManager.setHooks(new ResourceManagerInternal.ResourceManagerHooks() {
                    private final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY = {R.drawable.abc_popup_background_mtrl_mult, R.drawable.abc_cab_background_internal_bg, R.drawable.abc_menu_hardkey_panel_mtrl_mult};
                    private final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED = {R.drawable.abc_textfield_activated_mtrl_alpha, R.drawable.abc_textfield_search_activated_mtrl_alpha, R.drawable.abc_cab_background_top_mtrl_alpha, R.drawable.abc_text_cursor_material, R.drawable.abc_text_select_handle_left_mtrl, R.drawable.abc_text_select_handle_middle_mtrl, R.drawable.abc_text_select_handle_right_mtrl};
                    private final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL = {R.drawable.abc_textfield_search_default_mtrl_alpha, R.drawable.abc_textfield_default_mtrl_alpha, R.drawable.abc_ab_share_pack_mtrl_alpha};
                    private final int[] TINT_CHECKABLE_BUTTON_LIST = {R.drawable.abc_btn_check_material, R.drawable.abc_btn_radio_material, R.drawable.abc_btn_check_material_anim, R.drawable.abc_btn_radio_material_anim};
                    private final int[] TINT_COLOR_CONTROL_NORMAL = {R.drawable.abc_ic_commit_search_api_mtrl_alpha, R.drawable.abc_seekbar_tick_mark_material, R.drawable.abc_ic_menu_share_mtrl_alpha, R.drawable.abc_ic_menu_copy_mtrl_am_alpha, R.drawable.abc_ic_menu_cut_mtrl_alpha, R.drawable.abc_ic_menu_selectall_mtrl_alpha, R.drawable.abc_ic_menu_paste_mtrl_am_alpha};
                    private final int[] TINT_COLOR_CONTROL_STATE_LIST = {R.drawable.abc_tab_indicator_material, R.drawable.abc_textfield_search_material};

                    private boolean arrayContains(int[] array, int value) {
                        for (int i : array) {
                            if (i == value) {
                                return true;
                            }
                        }
                        return false;
                    }

                    private ColorStateList createBorderlessButtonColorStateList(Context context) {
                        return createButtonColorStateList(context, 0);
                    }

                    private ColorStateList createButtonColorStateList(Context context, int baseColor) {
                        int[][] iArr = new int[4][];
                        int[] iArr2 = new int[4];
                        int themeAttrColor = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlHighlight);
                        int disabledThemeAttrColor = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorButtonNormal);
                        iArr[0] = ThemeUtils.DISABLED_STATE_SET;
                        iArr2[0] = disabledThemeAttrColor;
                        int i = 0 + 1;
                        iArr[i] = ThemeUtils.PRESSED_STATE_SET;
                        iArr2[i] = ColorUtils.compositeColors(themeAttrColor, baseColor);
                        int i2 = i + 1;
                        iArr[i2] = ThemeUtils.FOCUSED_STATE_SET;
                        iArr2[i2] = ColorUtils.compositeColors(themeAttrColor, baseColor);
                        int i3 = i2 + 1;
                        iArr[i3] = ThemeUtils.EMPTY_STATE_SET;
                        iArr2[i3] = baseColor;
                        int i4 = i3 + 1;
                        return new ColorStateList(iArr, iArr2);
                    }

                    private ColorStateList createColoredButtonColorStateList(Context context) {
                        return createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, R.attr.colorAccent));
                    }

                    private ColorStateList createDefaultButtonColorStateList(Context context) {
                        return createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, R.attr.colorButtonNormal));
                    }

                    private ColorStateList createSwitchThumbColorStateList(Context context) {
                        int[][] iArr = new int[3][];
                        int[] iArr2 = new int[3];
                        ColorStateList themeAttrColorStateList = ThemeUtils.getThemeAttrColorStateList(context, R.attr.colorSwitchThumbNormal);
                        if (themeAttrColorStateList == null || !themeAttrColorStateList.isStateful()) {
                            iArr[0] = ThemeUtils.DISABLED_STATE_SET;
                            iArr2[0] = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorSwitchThumbNormal);
                            int i = 0 + 1;
                            iArr[i] = ThemeUtils.CHECKED_STATE_SET;
                            iArr2[i] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
                            int i2 = i + 1;
                            iArr[i2] = ThemeUtils.EMPTY_STATE_SET;
                            iArr2[i2] = ThemeUtils.getThemeAttrColor(context, R.attr.colorSwitchThumbNormal);
                            int i3 = i2 + 1;
                        } else {
                            iArr[0] = ThemeUtils.DISABLED_STATE_SET;
                            iArr2[0] = themeAttrColorStateList.getColorForState(iArr[0], 0);
                            int i4 = 0 + 1;
                            iArr[i4] = ThemeUtils.CHECKED_STATE_SET;
                            iArr2[i4] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
                            int i5 = i4 + 1;
                            iArr[i5] = ThemeUtils.EMPTY_STATE_SET;
                            iArr2[i5] = themeAttrColorStateList.getDefaultColor();
                            int i6 = i5 + 1;
                        }
                        return new ColorStateList(iArr, iArr2);
                    }

                    private LayerDrawable getRatingBarLayerDrawable(ResourceManagerInternal resourceManager, Context context, int dimenResId) {
                        BitmapDrawable bitmapDrawable;
                        BitmapDrawable bitmapDrawable2;
                        BitmapDrawable bitmapDrawable3;
                        int dimensionPixelSize = context.getResources().getDimensionPixelSize(dimenResId);
                        Drawable drawable = resourceManager.getDrawable(context, R.drawable.abc_star_black_48dp);
                        Drawable drawable2 = resourceManager.getDrawable(context, R.drawable.abc_star_half_black_48dp);
                        if ((drawable instanceof BitmapDrawable) && drawable.getIntrinsicWidth() == dimensionPixelSize && drawable.getIntrinsicHeight() == dimensionPixelSize) {
                            bitmapDrawable2 = (BitmapDrawable) drawable;
                            bitmapDrawable = new BitmapDrawable(bitmapDrawable2.getBitmap());
                        } else {
                            Bitmap createBitmap = Bitmap.createBitmap(dimensionPixelSize, dimensionPixelSize, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(createBitmap);
                            drawable.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
                            drawable.draw(canvas);
                            BitmapDrawable bitmapDrawable4 = new BitmapDrawable(createBitmap);
                            BitmapDrawable bitmapDrawable5 = new BitmapDrawable(createBitmap);
                            bitmapDrawable2 = bitmapDrawable4;
                            bitmapDrawable = bitmapDrawable5;
                        }
                        bitmapDrawable.setTileModeX(Shader.TileMode.REPEAT);
                        if ((drawable2 instanceof BitmapDrawable) && drawable2.getIntrinsicWidth() == dimensionPixelSize && drawable2.getIntrinsicHeight() == dimensionPixelSize) {
                            bitmapDrawable3 = (BitmapDrawable) drawable2;
                        } else {
                            Bitmap createBitmap2 = Bitmap.createBitmap(dimensionPixelSize, dimensionPixelSize, Bitmap.Config.ARGB_8888);
                            Canvas canvas2 = new Canvas(createBitmap2);
                            drawable2.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
                            drawable2.draw(canvas2);
                            bitmapDrawable3 = new BitmapDrawable(createBitmap2);
                        }
                        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{bitmapDrawable2, bitmapDrawable3, bitmapDrawable});
                        layerDrawable.setId(0, 16908288);
                        layerDrawable.setId(1, 16908303);
                        layerDrawable.setId(2, 16908301);
                        return layerDrawable;
                    }

                    private void setPorterDuffColorFilter(Drawable d, int color, PorterDuff.Mode mode) {
                        if (DrawableUtils.canSafelyMutateDrawable(d)) {
                            d = d.mutate();
                        }
                        d.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(color, mode == null ? AppCompatDrawableManager.DEFAULT_MODE : mode));
                    }

                    public Drawable createDrawableFor(ResourceManagerInternal resourceManager, Context context, int resId) {
                        if (resId == R.drawable.abc_cab_background_top_material) {
                            return new LayerDrawable(new Drawable[]{resourceManager.getDrawable(context, R.drawable.abc_cab_background_internal_bg), resourceManager.getDrawable(context, R.drawable.abc_cab_background_top_mtrl_alpha)});
                        } else if (resId == R.drawable.abc_ratingbar_material) {
                            return getRatingBarLayerDrawable(resourceManager, context, R.dimen.abc_star_big);
                        } else {
                            if (resId == R.drawable.abc_ratingbar_indicator_material) {
                                return getRatingBarLayerDrawable(resourceManager, context, R.dimen.abc_star_medium);
                            }
                            if (resId == R.drawable.abc_ratingbar_small_material) {
                                return getRatingBarLayerDrawable(resourceManager, context, R.dimen.abc_star_small);
                            }
                            return null;
                        }
                    }

                    public ColorStateList getTintListForDrawableRes(Context context, int resId) {
                        if (resId == R.drawable.abc_edit_text_material) {
                            return AppCompatResources.getColorStateList(context, R.color.abc_tint_edittext);
                        }
                        if (resId == R.drawable.abc_switch_track_mtrl_alpha) {
                            return AppCompatResources.getColorStateList(context, R.color.abc_tint_switch_track);
                        }
                        if (resId == R.drawable.abc_switch_thumb_material) {
                            return createSwitchThumbColorStateList(context);
                        }
                        if (resId == R.drawable.abc_btn_default_mtrl_shape) {
                            return createDefaultButtonColorStateList(context);
                        }
                        if (resId == R.drawable.abc_btn_borderless_material) {
                            return createBorderlessButtonColorStateList(context);
                        }
                        if (resId == R.drawable.abc_btn_colored_material) {
                            return createColoredButtonColorStateList(context);
                        }
                        if (resId == R.drawable.abc_spinner_mtrl_am_alpha || resId == R.drawable.abc_spinner_textfield_background_material) {
                            return AppCompatResources.getColorStateList(context, R.color.abc_tint_spinner);
                        }
                        if (arrayContains(this.TINT_COLOR_CONTROL_NORMAL, resId)) {
                            return ThemeUtils.getThemeAttrColorStateList(context, R.attr.colorControlNormal);
                        }
                        if (arrayContains(this.TINT_COLOR_CONTROL_STATE_LIST, resId)) {
                            return AppCompatResources.getColorStateList(context, R.color.abc_tint_default);
                        }
                        if (arrayContains(this.TINT_CHECKABLE_BUTTON_LIST, resId)) {
                            return AppCompatResources.getColorStateList(context, R.color.abc_tint_btn_checkable);
                        }
                        if (resId == R.drawable.abc_seekbar_thumb_material) {
                            return AppCompatResources.getColorStateList(context, R.color.abc_tint_seek_thumb);
                        }
                        return null;
                    }

                    public PorterDuff.Mode getTintModeForDrawableRes(int resId) {
                        if (resId == R.drawable.abc_switch_thumb_material) {
                            return PorterDuff.Mode.MULTIPLY;
                        }
                        return null;
                    }

                    public boolean tintDrawable(Context context, int resId, Drawable drawable) {
                        if (resId == R.drawable.abc_seekbar_track_material) {
                            LayerDrawable layerDrawable = (LayerDrawable) drawable;
                            setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908288), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), AppCompatDrawableManager.DEFAULT_MODE);
                            setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), AppCompatDrawableManager.DEFAULT_MODE);
                            setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), AppCompatDrawableManager.DEFAULT_MODE);
                            return true;
                        } else if (resId != R.drawable.abc_ratingbar_material && resId != R.drawable.abc_ratingbar_indicator_material && resId != R.drawable.abc_ratingbar_small_material) {
                            return false;
                        } else {
                            LayerDrawable layerDrawable2 = (LayerDrawable) drawable;
                            setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(16908288), ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorControlNormal), AppCompatDrawableManager.DEFAULT_MODE);
                            setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), AppCompatDrawableManager.DEFAULT_MODE);
                            setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), AppCompatDrawableManager.DEFAULT_MODE);
                            return true;
                        }
                    }

                    public boolean tintDrawableUsingColorFilter(Context context, int resId, Drawable drawable) {
                        PorterDuff.Mode access$000 = AppCompatDrawableManager.DEFAULT_MODE;
                        boolean z = false;
                        int i = 0;
                        int i2 = -1;
                        if (arrayContains(this.COLORFILTER_TINT_COLOR_CONTROL_NORMAL, resId)) {
                            i = R.attr.colorControlNormal;
                            z = true;
                        } else if (arrayContains(this.COLORFILTER_COLOR_CONTROL_ACTIVATED, resId)) {
                            i = R.attr.colorControlActivated;
                            z = true;
                        } else if (arrayContains(this.COLORFILTER_COLOR_BACKGROUND_MULTIPLY, resId)) {
                            i = 16842801;
                            z = true;
                            access$000 = PorterDuff.Mode.MULTIPLY;
                        } else if (resId == R.drawable.abc_list_divider_mtrl_alpha) {
                            i = 16842800;
                            z = true;
                            i2 = Math.round(40.8f);
                        } else if (resId == R.drawable.abc_dialog_material_background) {
                            i = 16842801;
                            z = true;
                        }
                        if (!z) {
                            return false;
                        }
                        if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
                            drawable = drawable.mutate();
                        }
                        drawable.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(ThemeUtils.getThemeAttrColor(context, i), access$000));
                        if (i2 == -1) {
                            return true;
                        }
                        drawable.setAlpha(i2);
                        return true;
                    }
                });
            }
        }
    }

    static void tintDrawable(Drawable drawable, TintInfo tint, int[] state) {
        ResourceManagerInternal.tintDrawable(drawable, tint, state);
    }

    public synchronized Drawable getDrawable(Context context, int resId) {
        return this.mResourceManager.getDrawable(context, resId);
    }

    /* access modifiers changed from: package-private */
    public synchronized Drawable getDrawable(Context context, int resId, boolean failIfNotKnown) {
        return this.mResourceManager.getDrawable(context, resId, failIfNotKnown);
    }

    /* access modifiers changed from: package-private */
    public synchronized ColorStateList getTintList(Context context, int resId) {
        return this.mResourceManager.getTintList(context, resId);
    }

    public synchronized void onConfigurationChanged(Context context) {
        this.mResourceManager.onConfigurationChanged(context);
    }

    /* access modifiers changed from: package-private */
    public synchronized Drawable onDrawableLoadedFromResources(Context context, VectorEnabledTintResources resources, int resId) {
        return this.mResourceManager.onDrawableLoadedFromResources(context, resources, resId);
    }

    /* access modifiers changed from: package-private */
    public boolean tintDrawableUsingColorFilter(Context context, int resId, Drawable drawable) {
        return this.mResourceManager.tintDrawableUsingColorFilter(context, resId, drawable);
    }
}
