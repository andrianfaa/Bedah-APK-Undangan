package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.constraintlayout.widget.R;

public class ImageFilterButton extends AppCompatImageButton {
    private Drawable mAltDrawable = null;
    private float mCrossfade = 0.0f;
    private Drawable mDrawable = null;
    private ImageFilterView.ImageMatrix mImageMatrix = new ImageFilterView.ImageMatrix();
    LayerDrawable mLayer;
    Drawable[] mLayers = new Drawable[2];
    private boolean mOverlay = true;
    private float mPanX = Float.NaN;
    private float mPanY = Float.NaN;
    private Path mPath;
    RectF mRect;
    private float mRotate = Float.NaN;
    /* access modifiers changed from: private */
    public float mRound = Float.NaN;
    /* access modifiers changed from: private */
    public float mRoundPercent = 0.0f;
    ViewOutlineProvider mViewOutlineProvider;
    private float mZoom = Float.NaN;

    public ImageFilterButton(Context context) {
        super(context);
        init(context, (AttributeSet) null);
    }

    public ImageFilterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ImageFilterButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setPadding(0, 0, 0, 0);
        if (attrs != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.ImageFilterView);
            int indexCount = obtainStyledAttributes.getIndexCount();
            this.mAltDrawable = obtainStyledAttributes.getDrawable(R.styleable.ImageFilterView_altSrc);
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.ImageFilterView_crossfade) {
                    this.mCrossfade = obtainStyledAttributes.getFloat(index, 0.0f);
                } else if (index == R.styleable.ImageFilterView_warmth) {
                    setWarmth(obtainStyledAttributes.getFloat(index, 0.0f));
                } else if (index == R.styleable.ImageFilterView_saturation) {
                    setSaturation(obtainStyledAttributes.getFloat(index, 0.0f));
                } else if (index == R.styleable.ImageFilterView_contrast) {
                    setContrast(obtainStyledAttributes.getFloat(index, 0.0f));
                } else if (index == R.styleable.ImageFilterView_round) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        setRound(obtainStyledAttributes.getDimension(index, 0.0f));
                    }
                } else if (index == R.styleable.ImageFilterView_roundPercent) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        setRoundPercent(obtainStyledAttributes.getFloat(index, 0.0f));
                    }
                } else if (index == R.styleable.ImageFilterView_overlay) {
                    setOverlay(obtainStyledAttributes.getBoolean(index, this.mOverlay));
                } else if (index == R.styleable.ImageFilterView_imagePanX) {
                    setImagePanX(obtainStyledAttributes.getFloat(index, this.mPanX));
                } else if (index == R.styleable.ImageFilterView_imagePanY) {
                    setImagePanY(obtainStyledAttributes.getFloat(index, this.mPanY));
                } else if (index == R.styleable.ImageFilterView_imageRotate) {
                    setImageRotate(obtainStyledAttributes.getFloat(index, this.mRotate));
                } else if (index == R.styleable.ImageFilterView_imageZoom) {
                    setImageZoom(obtainStyledAttributes.getFloat(index, this.mZoom));
                }
            }
            obtainStyledAttributes.recycle();
            Drawable drawable = getDrawable();
            this.mDrawable = drawable;
            if (this.mAltDrawable == null || drawable == null) {
                Drawable drawable2 = getDrawable();
                this.mDrawable = drawable2;
                if (drawable2 != null) {
                    Drawable[] drawableArr = this.mLayers;
                    Drawable mutate = drawable2.mutate();
                    this.mDrawable = mutate;
                    drawableArr[0] = mutate;
                    return;
                }
                return;
            }
            Drawable[] drawableArr2 = this.mLayers;
            Drawable mutate2 = getDrawable().mutate();
            this.mDrawable = mutate2;
            drawableArr2[0] = mutate2;
            this.mLayers[1] = this.mAltDrawable.mutate();
            LayerDrawable layerDrawable = new LayerDrawable(this.mLayers);
            this.mLayer = layerDrawable;
            layerDrawable.getDrawable(1).setAlpha((int) (this.mCrossfade * 255.0f));
            if (!this.mOverlay) {
                this.mLayer.getDrawable(0).setAlpha((int) ((1.0f - this.mCrossfade) * 255.0f));
            }
            super.setImageDrawable(this.mLayer);
        }
    }

    private void setMatrix() {
        if (!Float.isNaN(this.mPanX) || !Float.isNaN(this.mPanY) || !Float.isNaN(this.mZoom) || !Float.isNaN(this.mRotate)) {
            float f = 0.0f;
            float f2 = Float.isNaN(this.mPanX) ? 0.0f : this.mPanX;
            float f3 = Float.isNaN(this.mPanY) ? 0.0f : this.mPanY;
            float f4 = Float.isNaN(this.mZoom) ? 1.0f : this.mZoom;
            if (!Float.isNaN(this.mRotate)) {
                f = this.mRotate;
            }
            Matrix matrix = new Matrix();
            matrix.reset();
            float intrinsicWidth = (float) getDrawable().getIntrinsicWidth();
            float intrinsicHeight = (float) getDrawable().getIntrinsicHeight();
            float width = (float) getWidth();
            float height = (float) getHeight();
            float f5 = (intrinsicWidth * height < intrinsicHeight * width ? width / intrinsicWidth : height / intrinsicHeight) * f4;
            matrix.postScale(f5, f5);
            matrix.postTranslate(((((width - (f5 * intrinsicWidth)) * f2) + width) - (f5 * intrinsicWidth)) * 0.5f, ((((height - (f5 * intrinsicHeight)) * f3) + height) - (f5 * intrinsicHeight)) * 0.5f);
            matrix.postRotate(f, width / 2.0f, height / 2.0f);
            setImageMatrix(matrix);
            setScaleType(ImageView.ScaleType.MATRIX);
        }
    }

    private void setOverlay(boolean overlay) {
        this.mOverlay = overlay;
    }

    private void updateViewMatrix() {
        if (!Float.isNaN(this.mPanX) || !Float.isNaN(this.mPanY) || !Float.isNaN(this.mZoom) || !Float.isNaN(this.mRotate)) {
            setMatrix();
        } else {
            setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    public void draw(Canvas canvas) {
        boolean z = false;
        if (!(Build.VERSION.SDK_INT >= 21 || this.mRound == 0.0f || this.mPath == null)) {
            z = true;
            canvas.save();
            canvas.clipPath(this.mPath);
        }
        super.draw(canvas);
        if (z) {
            canvas.restore();
        }
    }

    public float getContrast() {
        return this.mImageMatrix.mContrast;
    }

    public float getCrossfade() {
        return this.mCrossfade;
    }

    public float getImagePanX() {
        return this.mPanX;
    }

    public float getImagePanY() {
        return this.mPanY;
    }

    public float getImageRotate() {
        return this.mRotate;
    }

    public float getImageZoom() {
        return this.mZoom;
    }

    public float getRound() {
        return this.mRound;
    }

    public float getRoundPercent() {
        return this.mRoundPercent;
    }

    public float getSaturation() {
        return this.mImageMatrix.mSaturation;
    }

    public float getWarmth() {
        return this.mImageMatrix.mWarmth;
    }

    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        setMatrix();
    }

    public void setAltImageResource(int resId) {
        Drawable mutate = AppCompatResources.getDrawable(getContext(), resId).mutate();
        this.mAltDrawable = mutate;
        Drawable[] drawableArr = this.mLayers;
        drawableArr[0] = this.mDrawable;
        drawableArr[1] = mutate;
        LayerDrawable layerDrawable = new LayerDrawable(this.mLayers);
        this.mLayer = layerDrawable;
        super.setImageDrawable(layerDrawable);
        setCrossfade(this.mCrossfade);
    }

    public void setBrightness(float brightness) {
        this.mImageMatrix.mBrightness = brightness;
        this.mImageMatrix.updateMatrix(this);
    }

    public void setContrast(float contrast) {
        this.mImageMatrix.mContrast = contrast;
        this.mImageMatrix.updateMatrix(this);
    }

    public void setCrossfade(float crossfade) {
        this.mCrossfade = crossfade;
        if (this.mLayers != null) {
            if (!this.mOverlay) {
                this.mLayer.getDrawable(0).setAlpha((int) ((1.0f - this.mCrossfade) * 255.0f));
            }
            this.mLayer.getDrawable(1).setAlpha((int) (this.mCrossfade * 255.0f));
            super.setImageDrawable(this.mLayer);
        }
    }

    public void setImageDrawable(Drawable drawable) {
        if (this.mAltDrawable == null || drawable == null) {
            super.setImageDrawable(drawable);
            return;
        }
        Drawable mutate = drawable.mutate();
        this.mDrawable = mutate;
        Drawable[] drawableArr = this.mLayers;
        drawableArr[0] = mutate;
        drawableArr[1] = this.mAltDrawable;
        LayerDrawable layerDrawable = new LayerDrawable(this.mLayers);
        this.mLayer = layerDrawable;
        super.setImageDrawable(layerDrawable);
        setCrossfade(this.mCrossfade);
    }

    public void setImagePanX(float pan) {
        this.mPanX = pan;
        updateViewMatrix();
    }

    public void setImagePanY(float pan) {
        this.mPanY = pan;
        updateViewMatrix();
    }

    public void setImageResource(int resId) {
        if (this.mAltDrawable != null) {
            Drawable mutate = AppCompatResources.getDrawable(getContext(), resId).mutate();
            this.mDrawable = mutate;
            Drawable[] drawableArr = this.mLayers;
            drawableArr[0] = mutate;
            drawableArr[1] = this.mAltDrawable;
            LayerDrawable layerDrawable = new LayerDrawable(this.mLayers);
            this.mLayer = layerDrawable;
            super.setImageDrawable(layerDrawable);
            setCrossfade(this.mCrossfade);
            return;
        }
        super.setImageResource(resId);
    }

    public void setImageRotate(float rotation) {
        this.mRotate = rotation;
        updateViewMatrix();
    }

    public void setImageZoom(float zoom) {
        this.mZoom = zoom;
        updateViewMatrix();
    }

    public void setRound(float round) {
        if (Float.isNaN(round)) {
            this.mRound = round;
            float f = this.mRoundPercent;
            this.mRoundPercent = -1.0f;
            setRoundPercent(f);
            return;
        }
        boolean z = this.mRound != round;
        this.mRound = round;
        if (round != 0.0f) {
            if (this.mPath == null) {
                this.mPath = new Path();
            }
            if (this.mRect == null) {
                this.mRect = new RectF();
            }
            if (Build.VERSION.SDK_INT >= 21) {
                if (this.mViewOutlineProvider == null) {
                    AnonymousClass2 r2 = new ViewOutlineProvider() {
                        public void getOutline(View view, Outline outline) {
                            outline.setRoundRect(0, 0, ImageFilterButton.this.getWidth(), ImageFilterButton.this.getHeight(), ImageFilterButton.this.mRound);
                        }
                    };
                    this.mViewOutlineProvider = r2;
                    setOutlineProvider(r2);
                }
                setClipToOutline(true);
            }
            this.mRect.set(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
            this.mPath.reset();
            Path path = this.mPath;
            RectF rectF = this.mRect;
            float f2 = this.mRound;
            path.addRoundRect(rectF, f2, f2, Path.Direction.CW);
        } else if (Build.VERSION.SDK_INT >= 21) {
            setClipToOutline(false);
        }
        if (z && Build.VERSION.SDK_INT >= 21) {
            invalidateOutline();
        }
    }

    public void setRoundPercent(float round) {
        boolean z = this.mRoundPercent != round;
        this.mRoundPercent = round;
        if (round != 0.0f) {
            if (this.mPath == null) {
                this.mPath = new Path();
            }
            if (this.mRect == null) {
                this.mRect = new RectF();
            }
            if (Build.VERSION.SDK_INT >= 21) {
                if (this.mViewOutlineProvider == null) {
                    AnonymousClass1 r2 = new ViewOutlineProvider() {
                        public void getOutline(View view, Outline outline) {
                            int width = ImageFilterButton.this.getWidth();
                            int height = ImageFilterButton.this.getHeight();
                            outline.setRoundRect(0, 0, width, height, (((float) Math.min(width, height)) * ImageFilterButton.this.mRoundPercent) / 2.0f);
                        }
                    };
                    this.mViewOutlineProvider = r2;
                    setOutlineProvider(r2);
                }
                setClipToOutline(true);
            }
            int width = getWidth();
            int height = getHeight();
            float min = (((float) Math.min(width, height)) * this.mRoundPercent) / 2.0f;
            this.mRect.set(0.0f, 0.0f, (float) width, (float) height);
            this.mPath.reset();
            this.mPath.addRoundRect(this.mRect, min, min, Path.Direction.CW);
        } else if (Build.VERSION.SDK_INT >= 21) {
            setClipToOutline(false);
        }
        if (z && Build.VERSION.SDK_INT >= 21) {
            invalidateOutline();
        }
    }

    public void setSaturation(float saturation) {
        this.mImageMatrix.mSaturation = saturation;
        this.mImageMatrix.updateMatrix(this);
    }

    public void setWarmth(float warmth) {
        this.mImageMatrix.mWarmth = warmth;
        this.mImageMatrix.updateMatrix(this);
    }
}
