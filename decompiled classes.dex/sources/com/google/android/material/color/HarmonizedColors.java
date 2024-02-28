package com.google.android.material.color;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.content.res.loader.ResourcesLoader;
import android.os.Build;
import android.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import com.google.android.material.R;
import java.util.HashMap;
import java.util.Map;

public class HarmonizedColors {
    private static final String TAG = HarmonizedColors.class.getSimpleName();

    private HarmonizedColors() {
    }

    private static void addHarmonizedColorAttributesToReplacementMap(Map<Integer, Integer> map, TypedArray themeAttributesTypedArray, TypedArray themeOverlayAttributesTypedArray, int colorToHarmonizeWith) {
        TypedArray typedArray = themeOverlayAttributesTypedArray != null ? themeOverlayAttributesTypedArray : themeAttributesTypedArray;
        for (int i = 0; i < themeAttributesTypedArray.getIndexCount(); i++) {
            int resourceId = typedArray.getResourceId(i, 0);
            if (resourceId != 0 && themeAttributesTypedArray.hasValue(i) && isColorResource(themeAttributesTypedArray.getType(i))) {
                map.put(Integer.valueOf(resourceId), Integer.valueOf(MaterialColors.harmonize(themeAttributesTypedArray.getColor(i, 0), colorToHarmonizeWith)));
            }
        }
    }

    private static boolean addResourcesLoaderToContext(Context context, Map<Integer, Integer> map) {
        ResourcesLoader create = ColorResourcesLoaderCreator.create(context, map);
        if (create == null) {
            return false;
        }
        context.getResources().addLoaders(new ResourcesLoader[]{create});
        return true;
    }

    public static void applyToContextIfAvailable(Context context, HarmonizedColorsOptions options) {
        if (isHarmonizedColorAvailable()) {
            Map<Integer, Integer> createHarmonizedColorReplacementMap = createHarmonizedColorReplacementMap(context, options);
            int themeOverlayResourceId = options.getThemeOverlayResourceId(0);
            if (addResourcesLoaderToContext(context, createHarmonizedColorReplacementMap) && themeOverlayResourceId != 0) {
                ThemeUtils.applyThemeOverlay(context, themeOverlayResourceId);
            }
        }
    }

    private static Map<Integer, Integer> createHarmonizedColorReplacementMap(Context originalContext, HarmonizedColorsOptions options) {
        HashMap hashMap = new HashMap();
        int color = MaterialColors.getColor(originalContext, options.getColorAttributeToHarmonizeWith(), TAG);
        for (int i : options.getColorResourceIds()) {
            hashMap.put(Integer.valueOf(i), Integer.valueOf(MaterialColors.harmonize(ContextCompat.getColor(originalContext, i), color)));
        }
        HarmonizedColorAttributes colorAttributes = options.getColorAttributes();
        if (colorAttributes != null) {
            int[] attributes = colorAttributes.getAttributes();
            if (attributes.length > 0) {
                int themeOverlay = colorAttributes.getThemeOverlay();
                TypedArray obtainStyledAttributes = originalContext.obtainStyledAttributes(attributes);
                TypedArray obtainStyledAttributes2 = themeOverlay != 0 ? new ContextThemeWrapper(originalContext, themeOverlay).obtainStyledAttributes(attributes) : null;
                addHarmonizedColorAttributesToReplacementMap(hashMap, obtainStyledAttributes, obtainStyledAttributes2, color);
                obtainStyledAttributes.recycle();
                if (obtainStyledAttributes2 != null) {
                    obtainStyledAttributes2.recycle();
                }
            }
        }
        return hashMap;
    }

    private static boolean isColorResource(int attrType) {
        return 28 <= attrType && attrType <= 31;
    }

    public static boolean isHarmonizedColorAvailable() {
        return Build.VERSION.SDK_INT >= 30;
    }

    public static Context wrapContextIfAvailable(Context context, HarmonizedColorsOptions options) {
        if (!isHarmonizedColorAvailable()) {
            return context;
        }
        Map<Integer, Integer> createHarmonizedColorReplacementMap = createHarmonizedColorReplacementMap(context, options);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, options.getThemeOverlayResourceId(R.style.ThemeOverlay_Material3_HarmonizedColors_Empty));
        contextThemeWrapper.applyOverrideConfiguration(new Configuration());
        return addResourcesLoaderToContext(contextThemeWrapper, createHarmonizedColorReplacementMap) ? contextThemeWrapper : context;
    }
}
