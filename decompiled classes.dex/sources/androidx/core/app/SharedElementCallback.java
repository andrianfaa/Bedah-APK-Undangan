package androidx.core.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import java.util.List;
import java.util.Map;

public abstract class SharedElementCallback {
    private static final String BUNDLE_SNAPSHOT_BITMAP = "sharedElement:snapshot:bitmap";
    private static final String BUNDLE_SNAPSHOT_IMAGE_MATRIX = "sharedElement:snapshot:imageMatrix";
    private static final String BUNDLE_SNAPSHOT_IMAGE_SCALETYPE = "sharedElement:snapshot:imageScaleType";
    private static final int MAX_IMAGE_SIZE = 1048576;
    private Matrix mTempMatrix;

    public interface OnSharedElementsReadyListener {
        void onSharedElementsReady();
    }

    private static Bitmap createDrawableBitmap(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
            return null;
        }
        float min = Math.min(1.0f, 1048576.0f / ((float) (intrinsicWidth * intrinsicHeight)));
        if ((drawable instanceof BitmapDrawable) && min == 1.0f) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int i = (int) (((float) intrinsicWidth) * min);
        int i2 = (int) (((float) intrinsicHeight) * min);
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Rect bounds = drawable.getBounds();
        int i3 = bounds.left;
        int i4 = bounds.top;
        int i5 = bounds.right;
        int i6 = bounds.bottom;
        drawable.setBounds(0, 0, i, i2);
        drawable.draw(canvas);
        drawable.setBounds(i3, i4, i5, i6);
        return createBitmap;
    }

    public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
        Bitmap createDrawableBitmap;
        if (sharedElement instanceof ImageView) {
            ImageView imageView = (ImageView) sharedElement;
            Drawable drawable = imageView.getDrawable();
            Drawable background = imageView.getBackground();
            if (!(drawable == null || background != null || (createDrawableBitmap = createDrawableBitmap(drawable)) == null)) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(BUNDLE_SNAPSHOT_BITMAP, createDrawableBitmap);
                bundle.putString(BUNDLE_SNAPSHOT_IMAGE_SCALETYPE, imageView.getScaleType().toString());
                if (imageView.getScaleType() == ImageView.ScaleType.MATRIX) {
                    float[] fArr = new float[9];
                    imageView.getImageMatrix().getValues(fArr);
                    bundle.putFloatArray(BUNDLE_SNAPSHOT_IMAGE_MATRIX, fArr);
                }
                return bundle;
            }
        }
        int round = Math.round(screenBounds.width());
        int round2 = Math.round(screenBounds.height());
        if (round <= 0 || round2 <= 0) {
            return null;
        }
        float min = Math.min(1.0f, 1048576.0f / ((float) (round * round2)));
        int i = (int) (((float) round) * min);
        int i2 = (int) (((float) round2) * min);
        if (this.mTempMatrix == null) {
            this.mTempMatrix = new Matrix();
        }
        this.mTempMatrix.set(viewToGlobalMatrix);
        this.mTempMatrix.postTranslate(-screenBounds.left, -screenBounds.top);
        this.mTempMatrix.postScale(min, min);
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.concat(this.mTempMatrix);
        sharedElement.draw(canvas);
        return createBitmap;
    }

    public View onCreateSnapshotView(Context context, Parcelable snapshot) {
        if (snapshot instanceof Bundle) {
            Bundle bundle = (Bundle) snapshot;
            Bitmap bitmap = (Bitmap) bundle.getParcelable(BUNDLE_SNAPSHOT_BITMAP);
            if (bitmap == null) {
                return null;
            }
            ImageView imageView = new ImageView(context);
            ImageView imageView2 = imageView;
            imageView.setImageBitmap(bitmap);
            imageView.setScaleType(ImageView.ScaleType.valueOf(bundle.getString(BUNDLE_SNAPSHOT_IMAGE_SCALETYPE)));
            if (imageView.getScaleType() != ImageView.ScaleType.MATRIX) {
                return imageView2;
            }
            float[] floatArray = bundle.getFloatArray(BUNDLE_SNAPSHOT_IMAGE_MATRIX);
            Matrix matrix = new Matrix();
            matrix.setValues(floatArray);
            imageView.setImageMatrix(matrix);
            return imageView2;
        } else if (!(snapshot instanceof Bitmap)) {
            return null;
        } else {
            ImageView imageView3 = new ImageView(context);
            imageView3.setImageBitmap((Bitmap) snapshot);
            return imageView3;
        }
    }

    public void onMapSharedElements(List<String> list, Map<String, View> map) {
    }

    public void onRejectSharedElements(List<View> list) {
    }

    public void onSharedElementEnd(List<String> list, List<View> list2, List<View> list3) {
    }

    public void onSharedElementStart(List<String> list, List<View> list2, List<View> list3) {
    }

    public void onSharedElementsArrived(List<String> list, List<View> list2, OnSharedElementsReadyListener listener) {
        listener.onSharedElementsReady();
    }
}
