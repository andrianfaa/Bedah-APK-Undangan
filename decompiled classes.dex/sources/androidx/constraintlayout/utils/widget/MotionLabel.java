package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.constraintlayout.motion.widget.FloatLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.R;
import androidx.core.view.GravityCompat;
import mt.Log1F380D;

/* compiled from: 0026 */
public class MotionLabel extends View implements FloatLayout {
    private static final int MONOSPACE = 3;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    static String TAG = "MotionLabel";
    private boolean mAutoSize = false;
    private int mAutoSizeTextType = 0;
    float mBackgroundPanX = Float.NaN;
    float mBackgroundPanY = Float.NaN;
    private float mBaseTextSize = Float.NaN;
    private float mDeltaLeft;
    private float mFloatHeight;
    private float mFloatWidth;
    private String mFontFamily;
    private int mGravity = 8388659;
    private Layout mLayout;
    boolean mNotBuilt = true;
    Matrix mOutlinePositionMatrix;
    private int mPaddingBottom = 1;
    private int mPaddingLeft = 1;
    private int mPaddingRight = 1;
    private int mPaddingTop = 1;
    TextPaint mPaint = new TextPaint();
    Path mPath = new Path();
    RectF mRect;
    float mRotate = Float.NaN;
    /* access modifiers changed from: private */
    public float mRound = Float.NaN;
    /* access modifiers changed from: private */
    public float mRoundPercent = 0.0f;
    private int mStyleIndex;
    Paint mTempPaint;
    Rect mTempRect;
    private String mText = "Hello World";
    private Drawable mTextBackground;
    private Bitmap mTextBackgroundBitmap;
    private Rect mTextBounds = new Rect();
    private int mTextFillColor = 65535;
    private int mTextOutlineColor = 65535;
    private float mTextOutlineThickness = 0.0f;
    private float mTextPanX = 0.0f;
    private float mTextPanY = 0.0f;
    private BitmapShader mTextShader;
    private Matrix mTextShaderMatrix;
    private float mTextSize = 48.0f;
    private int mTextureEffect = 0;
    private float mTextureHeight = Float.NaN;
    private float mTextureWidth = Float.NaN;
    private CharSequence mTransformed;
    private int mTypefaceIndex;
    private boolean mUseOutline = false;
    ViewOutlineProvider mViewOutlineProvider;
    float mZoom = Float.NaN;
    Paint paintCache = new Paint();
    float paintTextSize;

    public MotionLabel(Context context) {
        super(context);
        init(context, (AttributeSet) null);
    }

    public MotionLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MotionLabel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void adjustTexture(float l, float t, float r, float b) {
        if (this.mTextShaderMatrix != null) {
            this.mFloatWidth = r - l;
            this.mFloatHeight = b - t;
            updateShaderMatrix();
        }
    }

    private float getHorizontalOffset() {
        float f = Float.isNaN(this.mBaseTextSize) ? 1.0f : this.mTextSize / this.mBaseTextSize;
        TextPaint textPaint = this.mPaint;
        String str = this.mText;
        return (((((Float.isNaN(this.mFloatWidth) ? (float) getMeasuredWidth() : this.mFloatWidth) - ((float) getPaddingLeft())) - ((float) getPaddingRight())) - (textPaint.measureText(str, 0, str.length()) * f)) * (this.mTextPanX + 1.0f)) / 2.0f;
    }

    private float getVerticalOffset() {
        float f = Float.isNaN(this.mBaseTextSize) ? 1.0f : this.mTextSize / this.mBaseTextSize;
        Paint.FontMetrics fontMetrics = this.mPaint.getFontMetrics();
        return ((((((Float.isNaN(this.mFloatHeight) ? (float) getMeasuredHeight() : this.mFloatHeight) - ((float) getPaddingTop())) - ((float) getPaddingBottom())) - ((fontMetrics.descent - fontMetrics.ascent) * f)) * (1.0f - this.mTextPanY)) / 2.0f) - (fontMetrics.ascent * f);
    }

    private void init(Context context, AttributeSet attrs) {
        setUpTheme(context, attrs);
        if (attrs != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.MotionLabel);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.MotionLabel_android_text) {
                    setText(obtainStyledAttributes.getText(index));
                } else if (index == R.styleable.MotionLabel_android_fontFamily) {
                    this.mFontFamily = obtainStyledAttributes.getString(index);
                } else if (index == R.styleable.MotionLabel_scaleFromTextSize) {
                    this.mBaseTextSize = (float) obtainStyledAttributes.getDimensionPixelSize(index, (int) this.mBaseTextSize);
                } else if (index == R.styleable.MotionLabel_android_textSize) {
                    this.mTextSize = (float) obtainStyledAttributes.getDimensionPixelSize(index, (int) this.mTextSize);
                } else if (index == R.styleable.MotionLabel_android_textStyle) {
                    this.mStyleIndex = obtainStyledAttributes.getInt(index, this.mStyleIndex);
                } else if (index == R.styleable.MotionLabel_android_typeface) {
                    this.mTypefaceIndex = obtainStyledAttributes.getInt(index, this.mTypefaceIndex);
                } else if (index == R.styleable.MotionLabel_android_textColor) {
                    this.mTextFillColor = obtainStyledAttributes.getColor(index, this.mTextFillColor);
                } else if (index == R.styleable.MotionLabel_borderRound) {
                    this.mRound = obtainStyledAttributes.getDimension(index, this.mRound);
                    if (Build.VERSION.SDK_INT >= 21) {
                        setRound(this.mRound);
                    }
                } else if (index == R.styleable.MotionLabel_borderRoundPercent) {
                    this.mRoundPercent = obtainStyledAttributes.getFloat(index, this.mRoundPercent);
                    if (Build.VERSION.SDK_INT >= 21) {
                        setRoundPercent(this.mRoundPercent);
                    }
                } else if (index == R.styleable.MotionLabel_android_gravity) {
                    setGravity(obtainStyledAttributes.getInt(index, -1));
                } else if (index == R.styleable.MotionLabel_android_autoSizeTextType) {
                    this.mAutoSizeTextType = obtainStyledAttributes.getInt(index, 0);
                } else if (index == R.styleable.MotionLabel_textOutlineColor) {
                    this.mTextOutlineColor = obtainStyledAttributes.getInt(index, this.mTextOutlineColor);
                    this.mUseOutline = true;
                } else if (index == R.styleable.MotionLabel_textOutlineThickness) {
                    this.mTextOutlineThickness = obtainStyledAttributes.getDimension(index, this.mTextOutlineThickness);
                    this.mUseOutline = true;
                } else if (index == R.styleable.MotionLabel_textBackground) {
                    this.mTextBackground = obtainStyledAttributes.getDrawable(index);
                    this.mUseOutline = true;
                } else if (index == R.styleable.MotionLabel_textBackgroundPanX) {
                    this.mBackgroundPanX = obtainStyledAttributes.getFloat(index, this.mBackgroundPanX);
                } else if (index == R.styleable.MotionLabel_textBackgroundPanY) {
                    this.mBackgroundPanY = obtainStyledAttributes.getFloat(index, this.mBackgroundPanY);
                } else if (index == R.styleable.MotionLabel_textPanX) {
                    this.mTextPanX = obtainStyledAttributes.getFloat(index, this.mTextPanX);
                } else if (index == R.styleable.MotionLabel_textPanY) {
                    this.mTextPanY = obtainStyledAttributes.getFloat(index, this.mTextPanY);
                } else if (index == R.styleable.MotionLabel_textBackgroundRotate) {
                    this.mRotate = obtainStyledAttributes.getFloat(index, this.mRotate);
                } else if (index == R.styleable.MotionLabel_textBackgroundZoom) {
                    this.mZoom = obtainStyledAttributes.getFloat(index, this.mZoom);
                } else if (index == R.styleable.MotionLabel_textureHeight) {
                    this.mTextureHeight = obtainStyledAttributes.getDimension(index, this.mTextureHeight);
                } else if (index == R.styleable.MotionLabel_textureWidth) {
                    this.mTextureWidth = obtainStyledAttributes.getDimension(index, this.mTextureWidth);
                } else if (index == R.styleable.MotionLabel_textureEffect) {
                    this.mTextureEffect = obtainStyledAttributes.getInt(index, this.mTextureEffect);
                }
            }
            obtainStyledAttributes.recycle();
        }
        setupTexture();
        setupPath();
    }

    private void setTypefaceFromAttrs(String familyName, int typefaceIndex, int styleIndex) {
        Typeface typeface = null;
        if (familyName == null || (typeface = Typeface.create(familyName, styleIndex)) == null) {
            switch (typefaceIndex) {
                case 1:
                    typeface = Typeface.SANS_SERIF;
                    break;
                case 2:
                    typeface = Typeface.SERIF;
                    break;
                case 3:
                    typeface = Typeface.MONOSPACE;
                    break;
            }
            float f = 0.0f;
            boolean z = false;
            if (styleIndex > 0) {
                Typeface defaultFromStyle = typeface == null ? Typeface.defaultFromStyle(styleIndex) : Typeface.create(typeface, styleIndex);
                setTypeface(defaultFromStyle);
                int i = (~(defaultFromStyle != null ? defaultFromStyle.getStyle() : 0)) & styleIndex;
                TextPaint textPaint = this.mPaint;
                if ((i & 1) != 0) {
                    z = true;
                }
                textPaint.setFakeBoldText(z);
                TextPaint textPaint2 = this.mPaint;
                if ((i & 2) != 0) {
                    f = -0.25f;
                }
                textPaint2.setTextSkewX(f);
                return;
            }
            this.mPaint.setFakeBoldText(false);
            this.mPaint.setTextSkewX(0.0f);
            setTypeface(typeface);
            return;
        }
        setTypeface(typeface);
    }

    private void setUpTheme(Context context, AttributeSet attrs) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
        TextPaint textPaint = this.mPaint;
        int i = typedValue.data;
        this.mTextFillColor = i;
        textPaint.setColor(i);
    }

    private void setupTexture() {
        if (this.mTextBackground != null) {
            this.mTextShaderMatrix = new Matrix();
            int intrinsicWidth = this.mTextBackground.getIntrinsicWidth();
            int intrinsicHeight = this.mTextBackground.getIntrinsicHeight();
            int i = 128;
            if (intrinsicWidth <= 0) {
                int width = getWidth();
                if (width == 0) {
                    width = Float.isNaN(this.mTextureWidth) ? 128 : (int) this.mTextureWidth;
                }
                intrinsicWidth = width;
            }
            if (intrinsicHeight <= 0) {
                int height = getHeight();
                if (height == 0) {
                    if (!Float.isNaN(this.mTextureHeight)) {
                        i = (int) this.mTextureHeight;
                    }
                    height = i;
                }
                intrinsicHeight = height;
            }
            if (this.mTextureEffect != 0) {
                intrinsicWidth /= 2;
                intrinsicHeight /= 2;
            }
            this.mTextBackgroundBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(this.mTextBackgroundBitmap);
            this.mTextBackground.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            this.mTextBackground.setFilterBitmap(true);
            this.mTextBackground.draw(canvas);
            if (this.mTextureEffect != 0) {
                this.mTextBackgroundBitmap = blur(this.mTextBackgroundBitmap, 4);
            }
            this.mTextShader = new BitmapShader(this.mTextBackgroundBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        }
    }

    private void updateShaderMatrix() {
        float f = 0.0f;
        float f2 = Float.isNaN(this.mBackgroundPanX) ? 0.0f : this.mBackgroundPanX;
        float f3 = Float.isNaN(this.mBackgroundPanY) ? 0.0f : this.mBackgroundPanY;
        float f4 = Float.isNaN(this.mZoom) ? 1.0f : this.mZoom;
        if (!Float.isNaN(this.mRotate)) {
            f = this.mRotate;
        }
        this.mTextShaderMatrix.reset();
        float width = (float) this.mTextBackgroundBitmap.getWidth();
        float height = (float) this.mTextBackgroundBitmap.getHeight();
        float f5 = Float.isNaN(this.mTextureWidth) ? this.mFloatWidth : this.mTextureWidth;
        float f6 = Float.isNaN(this.mTextureHeight) ? this.mFloatHeight : this.mTextureHeight;
        float f7 = (width * f6 < height * f5 ? f5 / width : f6 / height) * f4;
        this.mTextShaderMatrix.postScale(f7, f7);
        float f8 = f5 - (f7 * width);
        float f9 = f6 - (f7 * height);
        if (!Float.isNaN(this.mTextureHeight)) {
            f9 = this.mTextureHeight / 2.0f;
        }
        if (!Float.isNaN(this.mTextureWidth)) {
            f8 = this.mTextureWidth / 2.0f;
        }
        this.mTextShaderMatrix.postTranslate((((f2 * f8) + f5) - (f7 * width)) * 0.5f, (((f3 * f9) + f6) - (f7 * height)) * 0.5f);
        float f10 = f2;
        this.mTextShaderMatrix.postRotate(f, f5 / 2.0f, f6 / 2.0f);
        this.mTextShader.setLocalMatrix(this.mTextShaderMatrix);
    }

    /* access modifiers changed from: package-private */
    public Bitmap blur(Bitmap bitmapOriginal, int factor) {
        Long valueOf = Long.valueOf(System.nanoTime());
        int width = bitmapOriginal.getWidth() / 2;
        int height = bitmapOriginal.getHeight() / 2;
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmapOriginal, width, height, true);
        for (int i = 0; i < factor && width >= 32 && height >= 32; i++) {
            width /= 2;
            height /= 2;
            createScaledBitmap = Bitmap.createScaledBitmap(createScaledBitmap, width, height, true);
        }
        return createScaledBitmap;
    }

    /* access modifiers changed from: package-private */
    public void buildShape(float scale) {
        if (this.mUseOutline || scale != 1.0f) {
            this.mPath.reset();
            String str = this.mText;
            int length = str.length();
            this.mPaint.getTextBounds(str, 0, length, this.mTextBounds);
            this.mPaint.getTextPath(str, 0, length, 0.0f, 0.0f, this.mPath);
            if (scale != 1.0f) {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                String loc = Debug.getLoc();
                Log1F380D.a((Object) loc);
                Log.v(str2, sb.append(loc).append(" scale ").append(scale).toString());
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                this.mPath.transform(matrix);
            }
            Rect rect = this.mTextBounds;
            rect.right--;
            this.mTextBounds.left++;
            this.mTextBounds.bottom++;
            Rect rect2 = this.mTextBounds;
            rect2.top--;
            RectF rectF = new RectF();
            rectF.bottom = (float) getHeight();
            rectF.right = (float) getWidth();
            this.mNotBuilt = false;
        }
    }

    public float getRound() {
        return this.mRound;
    }

    public float getRoundPercent() {
        return this.mRoundPercent;
    }

    public float getScaleFromTextSize() {
        return this.mBaseTextSize;
    }

    public float getTextBackgroundPanX() {
        return this.mBackgroundPanX;
    }

    public float getTextBackgroundPanY() {
        return this.mBackgroundPanY;
    }

    public float getTextBackgroundRotate() {
        return this.mRotate;
    }

    public float getTextBackgroundZoom() {
        return this.mZoom;
    }

    public int getTextOutlineColor() {
        return this.mTextOutlineColor;
    }

    public float getTextPanX() {
        return this.mTextPanX;
    }

    public float getTextPanY() {
        return this.mTextPanY;
    }

    public float getTextureHeight() {
        return this.mTextureHeight;
    }

    public float getTextureWidth() {
        return this.mTextureWidth;
    }

    public Typeface getTypeface() {
        return this.mPaint.getTypeface();
    }

    public void layout(float l, float t, float r, float b) {
        this.mDeltaLeft = l - ((float) ((int) (l + 0.5f)));
        int i = ((int) (r + 0.5f)) - ((int) (l + 0.5f));
        int i2 = ((int) (b + 0.5f)) - ((int) (t + 0.5f));
        this.mFloatWidth = r - l;
        this.mFloatHeight = b - t;
        adjustTexture(l, t, r, b);
        if (getMeasuredHeight() == i2 && getMeasuredWidth() == i) {
            super.layout((int) (l + 0.5f), (int) (t + 0.5f), (int) (r + 0.5f), (int) (0.5f + b));
        } else {
            measure(View.MeasureSpec.makeMeasureSpec(i, BasicMeasure.EXACTLY), View.MeasureSpec.makeMeasureSpec(i2, BasicMeasure.EXACTLY));
            super.layout((int) (l + 0.5f), (int) (t + 0.5f), (int) (r + 0.5f), (int) (0.5f + b));
        }
        if (this.mAutoSize) {
            if (this.mTempRect == null) {
                this.mTempPaint = new Paint();
                this.mTempRect = new Rect();
                this.mTempPaint.set(this.mPaint);
                this.paintTextSize = this.mTempPaint.getTextSize();
            }
            this.mFloatWidth = r - l;
            this.mFloatHeight = b - t;
            Paint paint = this.mTempPaint;
            String str = this.mText;
            paint.getTextBounds(str, 0, str.length(), this.mTempRect);
            int width = this.mTempRect.width();
            float height = ((float) this.mTempRect.height()) * 1.3f;
            float f = ((r - l) - ((float) this.mPaddingRight)) - ((float) this.mPaddingLeft);
            float f2 = ((b - t) - ((float) this.mPaddingBottom)) - ((float) this.mPaddingTop);
            if (((float) width) * f2 > height * f) {
                this.mPaint.setTextSize((this.paintTextSize * f) / ((float) width));
            } else {
                this.mPaint.setTextSize((this.paintTextSize * f2) / height);
            }
            if (this.mUseOutline || !Float.isNaN(this.mBaseTextSize)) {
                buildShape(Float.isNaN(this.mBaseTextSize) ? 1.0f : this.mTextSize / this.mBaseTextSize);
            }
        }
    }

    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        boolean isNaN = Float.isNaN(this.mBaseTextSize);
        float f = isNaN ? 1.0f : this.mTextSize / this.mBaseTextSize;
        this.mFloatWidth = (float) (r - l);
        this.mFloatHeight = (float) (b - t);
        if (this.mAutoSize) {
            if (this.mTempRect == null) {
                this.mTempPaint = new Paint();
                this.mTempRect = new Rect();
                this.mTempPaint.set(this.mPaint);
                this.paintTextSize = this.mTempPaint.getTextSize();
            }
            Paint paint = this.mTempPaint;
            String str = this.mText;
            paint.getTextBounds(str, 0, str.length(), this.mTempRect);
            int width = this.mTempRect.width();
            int height = (int) (((float) this.mTempRect.height()) * 1.3f);
            float f2 = (this.mFloatWidth - ((float) this.mPaddingRight)) - ((float) this.mPaddingLeft);
            float f3 = (this.mFloatHeight - ((float) this.mPaddingBottom)) - ((float) this.mPaddingTop);
            if (!isNaN) {
                f = ((float) width) * f3 > ((float) height) * f2 ? f2 / ((float) width) : f3 / ((float) height);
            } else if (((float) width) * f3 > ((float) height) * f2) {
                this.mPaint.setTextSize((this.paintTextSize * f2) / ((float) width));
            } else {
                this.mPaint.setTextSize((this.paintTextSize * f3) / ((float) height));
            }
        }
        if (this.mUseOutline || !isNaN) {
            adjustTexture((float) l, (float) t, (float) r, (float) b);
            buildShape(f);
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        float f = Float.isNaN(this.mBaseTextSize) ? 1.0f : this.mTextSize / this.mBaseTextSize;
        super.onDraw(canvas);
        if (this.mUseOutline || f != 1.0f) {
            if (this.mNotBuilt) {
                buildShape(f);
            }
            if (this.mOutlinePositionMatrix == null) {
                this.mOutlinePositionMatrix = new Matrix();
            }
            if (this.mUseOutline) {
                this.paintCache.set(this.mPaint);
                this.mOutlinePositionMatrix.reset();
                float horizontalOffset = ((float) this.mPaddingLeft) + getHorizontalOffset();
                float verticalOffset = ((float) this.mPaddingTop) + getVerticalOffset();
                this.mOutlinePositionMatrix.postTranslate(horizontalOffset, verticalOffset);
                this.mOutlinePositionMatrix.preScale(f, f);
                this.mPath.transform(this.mOutlinePositionMatrix);
                if (this.mTextShader != null) {
                    this.mPaint.setFilterBitmap(true);
                    this.mPaint.setShader(this.mTextShader);
                } else {
                    this.mPaint.setColor(this.mTextFillColor);
                }
                this.mPaint.setStyle(Paint.Style.FILL);
                this.mPaint.setStrokeWidth(this.mTextOutlineThickness);
                canvas.drawPath(this.mPath, this.mPaint);
                if (this.mTextShader != null) {
                    this.mPaint.setShader((Shader) null);
                }
                this.mPaint.setColor(this.mTextOutlineColor);
                this.mPaint.setStyle(Paint.Style.STROKE);
                this.mPaint.setStrokeWidth(this.mTextOutlineThickness);
                canvas.drawPath(this.mPath, this.mPaint);
                this.mOutlinePositionMatrix.reset();
                this.mOutlinePositionMatrix.postTranslate(-horizontalOffset, -verticalOffset);
                this.mPath.transform(this.mOutlinePositionMatrix);
                this.mPaint.set(this.paintCache);
                return;
            }
            float horizontalOffset2 = ((float) this.mPaddingLeft) + getHorizontalOffset();
            float verticalOffset2 = ((float) this.mPaddingTop) + getVerticalOffset();
            this.mOutlinePositionMatrix.reset();
            this.mOutlinePositionMatrix.preTranslate(horizontalOffset2, verticalOffset2);
            this.mPath.transform(this.mOutlinePositionMatrix);
            this.mPaint.setColor(this.mTextFillColor);
            this.mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.mPaint.setStrokeWidth(this.mTextOutlineThickness);
            canvas.drawPath(this.mPath, this.mPaint);
            this.mOutlinePositionMatrix.reset();
            this.mOutlinePositionMatrix.preTranslate(-horizontalOffset2, -verticalOffset2);
            this.mPath.transform(this.mOutlinePositionMatrix);
            return;
        }
        canvas.drawText(this.mText, this.mDeltaLeft + ((float) this.mPaddingLeft) + getHorizontalOffset(), ((float) this.mPaddingTop) + getVerticalOffset(), this.mPaint);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = View.MeasureSpec.getMode(widthMeasureSpec);
        int mode2 = View.MeasureSpec.getMode(heightMeasureSpec);
        int size = View.MeasureSpec.getSize(widthMeasureSpec);
        int size2 = View.MeasureSpec.getSize(heightMeasureSpec);
        this.mAutoSize = false;
        this.mPaddingLeft = getPaddingLeft();
        this.mPaddingRight = getPaddingRight();
        this.mPaddingTop = getPaddingTop();
        this.mPaddingBottom = getPaddingBottom();
        if (mode != 1073741824 || mode2 != 1073741824) {
            TextPaint textPaint = this.mPaint;
            String str = this.mText;
            textPaint.getTextBounds(str, 0, str.length(), this.mTextBounds);
            if (mode != 1073741824) {
                size = (int) (((float) this.mTextBounds.width()) + 0.99999f);
            }
            size += this.mPaddingLeft + this.mPaddingRight;
            if (mode2 != 1073741824) {
                int fontMetricsInt = (int) (((float) this.mPaint.getFontMetricsInt((Paint.FontMetricsInt) null)) + 0.99999f);
                size2 = (mode2 == Integer.MIN_VALUE ? Math.min(size2, fontMetricsInt) : fontMetricsInt) + this.mPaddingTop + this.mPaddingBottom;
            }
        } else if (this.mAutoSizeTextType != 0) {
            this.mAutoSize = true;
        }
        setMeasuredDimension(size, size2);
    }

    public void setGravity(int gravity) {
        if ((gravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 0) {
            gravity |= GravityCompat.START;
        }
        if ((gravity & 112) == 0) {
            gravity |= 48;
        }
        int i = gravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        int i2 = this.mGravity;
        if (i != (i2 & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK)) {
        }
        if (gravity != i2) {
            invalidate();
        }
        this.mGravity = gravity;
        switch (gravity & 112) {
            case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE /*48*/:
                this.mTextPanY = -1.0f;
                break;
            case 80:
                this.mTextPanY = 1.0f;
                break;
            default:
                this.mTextPanY = 0.0f;
                break;
        }
        switch (8388615 & gravity) {
            case 3:
            case GravityCompat.START /*8388611*/:
                this.mTextPanX = -1.0f;
                return;
            case 5:
            case GravityCompat.END /*8388613*/:
                this.mTextPanX = 1.0f;
                return;
            default:
                this.mTextPanX = 0.0f;
                return;
        }
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
                            outline.setRoundRect(0, 0, MotionLabel.this.getWidth(), MotionLabel.this.getHeight(), MotionLabel.this.mRound);
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
                            int width = MotionLabel.this.getWidth();
                            int height = MotionLabel.this.getHeight();
                            outline.setRoundRect(0, 0, width, height, (((float) Math.min(width, height)) * MotionLabel.this.mRoundPercent) / 2.0f);
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

    public void setScaleFromTextSize(float size) {
        this.mBaseTextSize = size;
    }

    public void setText(CharSequence text) {
        this.mText = text.toString();
        invalidate();
    }

    public void setTextBackgroundPanX(float pan) {
        this.mBackgroundPanX = pan;
        updateShaderMatrix();
        invalidate();
    }

    public void setTextBackgroundPanY(float pan) {
        this.mBackgroundPanY = pan;
        updateShaderMatrix();
        invalidate();
    }

    public void setTextBackgroundRotate(float rotation) {
        this.mRotate = rotation;
        updateShaderMatrix();
        invalidate();
    }

    public void setTextBackgroundZoom(float zoom) {
        this.mZoom = zoom;
        updateShaderMatrix();
        invalidate();
    }

    public void setTextFillColor(int color) {
        this.mTextFillColor = color;
        invalidate();
    }

    public void setTextOutlineColor(int color) {
        this.mTextOutlineColor = color;
        this.mUseOutline = true;
        invalidate();
    }

    public void setTextOutlineThickness(float width) {
        this.mTextOutlineThickness = width;
        this.mUseOutline = true;
        if (Float.isNaN(width)) {
            this.mTextOutlineThickness = 1.0f;
            this.mUseOutline = false;
        }
        invalidate();
    }

    public void setTextPanX(float textPanX) {
        this.mTextPanX = textPanX;
        invalidate();
    }

    public void setTextPanY(float textPanY) {
        this.mTextPanY = textPanY;
        invalidate();
    }

    public void setTextureHeight(float mTextureHeight2) {
        this.mTextureHeight = mTextureHeight2;
        updateShaderMatrix();
        invalidate();
    }

    public void setTextureWidth(float mTextureWidth2) {
        this.mTextureWidth = mTextureWidth2;
        updateShaderMatrix();
        invalidate();
    }

    public void setTypeface(Typeface tf) {
        if (this.mPaint.getTypeface() != tf) {
            this.mPaint.setTypeface(tf);
            if (this.mLayout != null) {
                this.mLayout = null;
                requestLayout();
                invalidate();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setupPath() {
        this.mPaddingLeft = getPaddingLeft();
        this.mPaddingRight = getPaddingRight();
        this.mPaddingTop = getPaddingTop();
        this.mPaddingBottom = getPaddingBottom();
        setTypefaceFromAttrs(this.mFontFamily, this.mTypefaceIndex, this.mStyleIndex);
        this.mPaint.setColor(this.mTextFillColor);
        this.mPaint.setStrokeWidth(this.mTextOutlineThickness);
        this.mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.mPaint.setFlags(128);
        setTextSize(this.mTextSize);
        this.mPaint.setAntiAlias(true);
    }

    public void setTextSize(float size) {
        this.mTextSize = size;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        String loc = Debug.getLoc();
        Log1F380D.a((Object) loc);
        Log.v(str, sb.append(loc).append("  ").append(size).append(" / ").append(this.mBaseTextSize).toString());
        this.mPaint.setTextSize(Float.isNaN(this.mBaseTextSize) ? size : this.mBaseTextSize);
        buildShape(Float.isNaN(this.mBaseTextSize) ? 1.0f : this.mTextSize / this.mBaseTextSize);
        requestLayout();
        invalidate();
    }
}
