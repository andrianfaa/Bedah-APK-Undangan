package androidx.core.graphics;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import androidx.core.graphics.BlendModeUtils;

public class BlendModeColorFilterCompat {

    static class Api29Impl {
        private Api29Impl() {
        }

        static ColorFilter createBlendModeColorFilter(int color, Object mode) {
            return new BlendModeColorFilter(color, (BlendMode) mode);
        }
    }

    private BlendModeColorFilterCompat() {
    }

    public static ColorFilter createBlendModeColorFilterCompat(int color, BlendModeCompat blendModeCompat) {
        if (Build.VERSION.SDK_INT >= 29) {
            Object obtainBlendModeFromCompat = BlendModeUtils.Api29Impl.obtainBlendModeFromCompat(blendModeCompat);
            if (obtainBlendModeFromCompat != null) {
                return Api29Impl.createBlendModeColorFilter(color, obtainBlendModeFromCompat);
            }
            return null;
        }
        PorterDuff.Mode obtainPorterDuffFromCompat = BlendModeUtils.obtainPorterDuffFromCompat(blendModeCompat);
        if (obtainPorterDuffFromCompat != null) {
            return new PorterDuffColorFilter(color, obtainPorterDuffFromCompat);
        }
        return null;
    }
}
