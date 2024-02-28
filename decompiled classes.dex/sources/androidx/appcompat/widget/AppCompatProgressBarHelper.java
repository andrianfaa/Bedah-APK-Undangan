package androidx.appcompat.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import androidx.core.graphics.drawable.WrappedDrawable;

class AppCompatProgressBarHelper {
    private static final int[] TINT_ATTRS = {16843067, 16843068};
    private Bitmap mSampleTile;
    private final ProgressBar mView;

    private static class Api23Impl {
        private Api23Impl() {
        }

        public static void transferLayerProperties(LayerDrawable src, LayerDrawable dst, int i) {
            dst.setLayerGravity(i, src.getLayerGravity(i));
            dst.setLayerWidth(i, src.getLayerWidth(i));
            dst.setLayerHeight(i, src.getLayerHeight(i));
            dst.setLayerInsetLeft(i, src.getLayerInsetLeft(i));
            dst.setLayerInsetRight(i, src.getLayerInsetRight(i));
            dst.setLayerInsetTop(i, src.getLayerInsetTop(i));
            dst.setLayerInsetBottom(i, src.getLayerInsetBottom(i));
            dst.setLayerInsetStart(i, src.getLayerInsetStart(i));
            dst.setLayerInsetEnd(i, src.getLayerInsetEnd(i));
        }
    }

    AppCompatProgressBarHelper(ProgressBar view) {
        this.mView = view;
    }

    private Shape getDrawableShape() {
        return new RoundRectShape(new float[]{5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f}, (RectF) null, (float[]) null);
    }

    private Drawable tileifyIndeterminate(Drawable drawable) {
        if (!(drawable instanceof AnimationDrawable)) {
            return drawable;
        }
        AnimationDrawable animationDrawable = (AnimationDrawable) drawable;
        int numberOfFrames = animationDrawable.getNumberOfFrames();
        AnimationDrawable drawable2 = new AnimationDrawable();
        drawable2.setOneShot(animationDrawable.isOneShot());
        for (int i = 0; i < numberOfFrames; i++) {
            Drawable tileify = tileify(animationDrawable.getFrame(i), true);
            tileify.setLevel(10000);
            drawable2.addFrame(tileify, animationDrawable.getDuration(i));
        }
        drawable2.setLevel(10000);
        return drawable2;
    }

    /* access modifiers changed from: package-private */
    public Bitmap getSampleTile() {
        return this.mSampleTile;
    }

    /* access modifiers changed from: package-private */
    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), attrs, TINT_ATTRS, defStyleAttr, 0);
        Drawable drawableIfKnown = obtainStyledAttributes.getDrawableIfKnown(0);
        if (drawableIfKnown != null) {
            this.mView.setIndeterminateDrawable(tileifyIndeterminate(drawableIfKnown));
        }
        Drawable drawableIfKnown2 = obtainStyledAttributes.getDrawableIfKnown(1);
        if (drawableIfKnown2 != null) {
            this.mView.setProgressDrawable(tileify(drawableIfKnown2, false));
        }
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: package-private */
    public Drawable tileify(Drawable drawable, boolean clip) {
        if (drawable instanceof WrappedDrawable) {
            Drawable wrappedDrawable = ((WrappedDrawable) drawable).getWrappedDrawable();
            if (wrappedDrawable != null) {
                ((WrappedDrawable) drawable).setWrappedDrawable(tileify(wrappedDrawable, clip));
            }
        } else if (drawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            int numberOfLayers = layerDrawable.getNumberOfLayers();
            Drawable[] drawableArr = new Drawable[numberOfLayers];
            for (int i = 0; i < numberOfLayers; i++) {
                int id = layerDrawable.getId(i);
                drawableArr[i] = tileify(layerDrawable.getDrawable(i), id == 16908301 || id == 16908303);
            }
            LayerDrawable layerDrawable2 = new LayerDrawable(drawableArr);
            for (int i2 = 0; i2 < numberOfLayers; i2++) {
                layerDrawable2.setId(i2, layerDrawable.getId(i2));
                if (Build.VERSION.SDK_INT >= 23) {
                    Api23Impl.transferLayerProperties(layerDrawable, layerDrawable2, i2);
                }
            }
            return layerDrawable2;
        } else if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (this.mSampleTile == null) {
                this.mSampleTile = bitmap;
            }
            ShapeDrawable shapeDrawable = new ShapeDrawable(getDrawableShape());
            shapeDrawable.getPaint().setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP));
            shapeDrawable.getPaint().setColorFilter(bitmapDrawable.getPaint().getColorFilter());
            return clip ? new ClipDrawable(shapeDrawable, 3, 1) : shapeDrawable;
        }
        return drawable;
    }
}
