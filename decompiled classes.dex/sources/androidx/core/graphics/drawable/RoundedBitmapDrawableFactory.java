package androidx.core.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import androidx.core.graphics.BitmapCompat;
import androidx.core.view.GravityCompat;
import java.io.InputStream;

public final class RoundedBitmapDrawableFactory {
    private static final String TAG = "RoundedBitmapDrawableFa";

    private static class DefaultRoundedBitmapDrawable extends RoundedBitmapDrawable {
        DefaultRoundedBitmapDrawable(Resources res, Bitmap bitmap) {
            super(res, bitmap);
        }

        /* access modifiers changed from: package-private */
        public void gravityCompatApply(int gravity, int bitmapWidth, int bitmapHeight, Rect bounds, Rect outRect) {
            GravityCompat.apply(gravity, bitmapWidth, bitmapHeight, bounds, outRect, 0);
        }

        public boolean hasMipMap() {
            return this.mBitmap != null && BitmapCompat.hasMipMap(this.mBitmap);
        }

        public void setMipMap(boolean mipMap) {
            if (this.mBitmap != null) {
                BitmapCompat.setHasMipMap(this.mBitmap, mipMap);
                invalidateSelf();
            }
        }
    }

    private RoundedBitmapDrawableFactory() {
    }

    public static RoundedBitmapDrawable create(Resources res, Bitmap bitmap) {
        return Build.VERSION.SDK_INT >= 21 ? new RoundedBitmapDrawable21(res, bitmap) : new DefaultRoundedBitmapDrawable(res, bitmap);
    }

    public static RoundedBitmapDrawable create(Resources res, InputStream is) {
        RoundedBitmapDrawable create = create(res, BitmapFactory.decodeStream(is));
        if (create.getBitmap() == null) {
            Log.w(TAG, "RoundedBitmapDrawable cannot decode " + is);
        }
        return create;
    }

    public static RoundedBitmapDrawable create(Resources res, String filepath) {
        RoundedBitmapDrawable create = create(res, BitmapFactory.decodeFile(filepath));
        if (create.getBitmap() == null) {
            Log.w(TAG, "RoundedBitmapDrawable cannot decode " + filepath);
        }
        return create;
    }
}
