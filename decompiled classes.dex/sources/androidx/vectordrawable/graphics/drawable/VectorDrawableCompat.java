package androidx.vectordrawable.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import androidx.collection.ArrayMap;
import androidx.constraintlayout.motion.widget.Key;
import androidx.core.content.res.ComplexColorCompat;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.PathParser;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import okhttp3.HttpUrl;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class VectorDrawableCompat extends VectorDrawableCommon {
    private static final boolean DBG_VECTOR_DRAWABLE = false;
    static final PorterDuff.Mode DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;
    private static final int LINECAP_BUTT = 0;
    private static final int LINECAP_ROUND = 1;
    private static final int LINECAP_SQUARE = 2;
    private static final int LINEJOIN_BEVEL = 2;
    private static final int LINEJOIN_MITER = 0;
    private static final int LINEJOIN_ROUND = 1;
    static final String LOGTAG = "VectorDrawableCompat";
    private static final int MAX_CACHED_BITMAP_SIZE = 2048;
    private static final String SHAPE_CLIP_PATH = "clip-path";
    private static final String SHAPE_GROUP = "group";
    private static final String SHAPE_PATH = "path";
    private static final String SHAPE_VECTOR = "vector";
    private boolean mAllowCaching;
    private Drawable.ConstantState mCachedConstantStateDelegate;
    private ColorFilter mColorFilter;
    private boolean mMutated;
    private PorterDuffColorFilter mTintFilter;
    private final Rect mTmpBounds;
    private final float[] mTmpFloats;
    private final Matrix mTmpMatrix;
    private VectorDrawableCompatState mVectorState;

    private static class VClipPath extends VPath {
        VClipPath() {
        }

        VClipPath(VClipPath copy) {
            super(copy);
        }

        private void updateStateFromTypedArray(TypedArray a, XmlPullParser parser) {
            String string = a.getString(0);
            if (string != null) {
                this.mPathName = string;
            }
            String string2 = a.getString(1);
            if (string2 != null) {
                this.mNodes = PathParser.createNodesFromPathData(string2);
            }
            this.mFillRule = TypedArrayUtils.getNamedInt(a, parser, "fillType", 2, 0);
        }

        public void inflate(Resources r, AttributeSet attrs, Resources.Theme theme, XmlPullParser parser) {
            if (TypedArrayUtils.hasAttribute(parser, "pathData")) {
                TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(r, theme, attrs, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_CLIP_PATH);
                updateStateFromTypedArray(obtainAttributes, parser);
                obtainAttributes.recycle();
            }
        }

        public boolean isClipPath() {
            return true;
        }
    }

    private static class VFullPath extends VPath {
        float mFillAlpha = 1.0f;
        ComplexColorCompat mFillColor;
        float mStrokeAlpha = 1.0f;
        ComplexColorCompat mStrokeColor;
        Paint.Cap mStrokeLineCap = Paint.Cap.BUTT;
        Paint.Join mStrokeLineJoin = Paint.Join.MITER;
        float mStrokeMiterlimit = 4.0f;
        float mStrokeWidth = 0.0f;
        private int[] mThemeAttrs;
        float mTrimPathEnd = 1.0f;
        float mTrimPathOffset = 0.0f;
        float mTrimPathStart = 0.0f;

        VFullPath() {
        }

        VFullPath(VFullPath copy) {
            super(copy);
            this.mThemeAttrs = copy.mThemeAttrs;
            this.mStrokeColor = copy.mStrokeColor;
            this.mStrokeWidth = copy.mStrokeWidth;
            this.mStrokeAlpha = copy.mStrokeAlpha;
            this.mFillColor = copy.mFillColor;
            this.mFillRule = copy.mFillRule;
            this.mFillAlpha = copy.mFillAlpha;
            this.mTrimPathStart = copy.mTrimPathStart;
            this.mTrimPathEnd = copy.mTrimPathEnd;
            this.mTrimPathOffset = copy.mTrimPathOffset;
            this.mStrokeLineCap = copy.mStrokeLineCap;
            this.mStrokeLineJoin = copy.mStrokeLineJoin;
            this.mStrokeMiterlimit = copy.mStrokeMiterlimit;
        }

        private Paint.Cap getStrokeLineCap(int id, Paint.Cap defValue) {
            switch (id) {
                case 0:
                    return Paint.Cap.BUTT;
                case 1:
                    return Paint.Cap.ROUND;
                case 2:
                    return Paint.Cap.SQUARE;
                default:
                    return defValue;
            }
        }

        private Paint.Join getStrokeLineJoin(int id, Paint.Join defValue) {
            switch (id) {
                case 0:
                    return Paint.Join.MITER;
                case 1:
                    return Paint.Join.ROUND;
                case 2:
                    return Paint.Join.BEVEL;
                default:
                    return defValue;
            }
        }

        private void updateStateFromTypedArray(TypedArray a, XmlPullParser parser, Resources.Theme theme) {
            this.mThemeAttrs = null;
            if (TypedArrayUtils.hasAttribute(parser, "pathData")) {
                String string = a.getString(0);
                if (string != null) {
                    this.mPathName = string;
                }
                String string2 = a.getString(2);
                if (string2 != null) {
                    this.mNodes = PathParser.createNodesFromPathData(string2);
                }
                this.mFillColor = TypedArrayUtils.getNamedComplexColor(a, parser, theme, "fillColor", 1, 0);
                this.mFillAlpha = TypedArrayUtils.getNamedFloat(a, parser, "fillAlpha", 12, this.mFillAlpha);
                this.mStrokeLineCap = getStrokeLineCap(TypedArrayUtils.getNamedInt(a, parser, "strokeLineCap", 8, -1), this.mStrokeLineCap);
                this.mStrokeLineJoin = getStrokeLineJoin(TypedArrayUtils.getNamedInt(a, parser, "strokeLineJoin", 9, -1), this.mStrokeLineJoin);
                this.mStrokeMiterlimit = TypedArrayUtils.getNamedFloat(a, parser, "strokeMiterLimit", 10, this.mStrokeMiterlimit);
                this.mStrokeColor = TypedArrayUtils.getNamedComplexColor(a, parser, theme, "strokeColor", 3, 0);
                this.mStrokeAlpha = TypedArrayUtils.getNamedFloat(a, parser, "strokeAlpha", 11, this.mStrokeAlpha);
                this.mStrokeWidth = TypedArrayUtils.getNamedFloat(a, parser, "strokeWidth", 4, this.mStrokeWidth);
                this.mTrimPathEnd = TypedArrayUtils.getNamedFloat(a, parser, "trimPathEnd", 6, this.mTrimPathEnd);
                this.mTrimPathOffset = TypedArrayUtils.getNamedFloat(a, parser, "trimPathOffset", 7, this.mTrimPathOffset);
                this.mTrimPathStart = TypedArrayUtils.getNamedFloat(a, parser, "trimPathStart", 5, this.mTrimPathStart);
                this.mFillRule = TypedArrayUtils.getNamedInt(a, parser, "fillType", 13, this.mFillRule);
            }
        }

        public void applyTheme(Resources.Theme t) {
        }

        public boolean canApplyTheme() {
            return this.mThemeAttrs != null;
        }

        /* access modifiers changed from: package-private */
        public float getFillAlpha() {
            return this.mFillAlpha;
        }

        /* access modifiers changed from: package-private */
        public int getFillColor() {
            return this.mFillColor.getColor();
        }

        /* access modifiers changed from: package-private */
        public float getStrokeAlpha() {
            return this.mStrokeAlpha;
        }

        /* access modifiers changed from: package-private */
        public int getStrokeColor() {
            return this.mStrokeColor.getColor();
        }

        /* access modifiers changed from: package-private */
        public float getStrokeWidth() {
            return this.mStrokeWidth;
        }

        /* access modifiers changed from: package-private */
        public float getTrimPathEnd() {
            return this.mTrimPathEnd;
        }

        /* access modifiers changed from: package-private */
        public float getTrimPathOffset() {
            return this.mTrimPathOffset;
        }

        /* access modifiers changed from: package-private */
        public float getTrimPathStart() {
            return this.mTrimPathStart;
        }

        public void inflate(Resources r, AttributeSet attrs, Resources.Theme theme, XmlPullParser parser) {
            TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(r, theme, attrs, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_PATH);
            updateStateFromTypedArray(obtainAttributes, parser, theme);
            obtainAttributes.recycle();
        }

        public boolean isStateful() {
            return this.mFillColor.isStateful() || this.mStrokeColor.isStateful();
        }

        public boolean onStateChanged(int[] stateSet) {
            return this.mFillColor.onStateChanged(stateSet) | this.mStrokeColor.onStateChanged(stateSet);
        }

        /* access modifiers changed from: package-private */
        public void setFillAlpha(float fillAlpha) {
            this.mFillAlpha = fillAlpha;
        }

        /* access modifiers changed from: package-private */
        public void setFillColor(int fillColor) {
            this.mFillColor.setColor(fillColor);
        }

        /* access modifiers changed from: package-private */
        public void setStrokeAlpha(float strokeAlpha) {
            this.mStrokeAlpha = strokeAlpha;
        }

        /* access modifiers changed from: package-private */
        public void setStrokeColor(int strokeColor) {
            this.mStrokeColor.setColor(strokeColor);
        }

        /* access modifiers changed from: package-private */
        public void setStrokeWidth(float strokeWidth) {
            this.mStrokeWidth = strokeWidth;
        }

        /* access modifiers changed from: package-private */
        public void setTrimPathEnd(float trimPathEnd) {
            this.mTrimPathEnd = trimPathEnd;
        }

        /* access modifiers changed from: package-private */
        public void setTrimPathOffset(float trimPathOffset) {
            this.mTrimPathOffset = trimPathOffset;
        }

        /* access modifiers changed from: package-private */
        public void setTrimPathStart(float trimPathStart) {
            this.mTrimPathStart = trimPathStart;
        }
    }

    private static class VGroup extends VObject {
        int mChangingConfigurations;
        final ArrayList<VObject> mChildren;
        private String mGroupName;
        final Matrix mLocalMatrix;
        private float mPivotX;
        private float mPivotY;
        float mRotate;
        private float mScaleX;
        private float mScaleY;
        final Matrix mStackedMatrix;
        private int[] mThemeAttrs;
        private float mTranslateX;
        private float mTranslateY;

        public VGroup() {
            super();
            this.mStackedMatrix = new Matrix();
            this.mChildren = new ArrayList<>();
            this.mRotate = 0.0f;
            this.mPivotX = 0.0f;
            this.mPivotY = 0.0f;
            this.mScaleX = 1.0f;
            this.mScaleY = 1.0f;
            this.mTranslateX = 0.0f;
            this.mTranslateY = 0.0f;
            this.mLocalMatrix = new Matrix();
            this.mGroupName = null;
        }

        public VGroup(VGroup copy, ArrayMap<String, Object> arrayMap) {
            super();
            VPath vPath;
            this.mStackedMatrix = new Matrix();
            this.mChildren = new ArrayList<>();
            this.mRotate = 0.0f;
            this.mPivotX = 0.0f;
            this.mPivotY = 0.0f;
            this.mScaleX = 1.0f;
            this.mScaleY = 1.0f;
            this.mTranslateX = 0.0f;
            this.mTranslateY = 0.0f;
            Matrix matrix = new Matrix();
            this.mLocalMatrix = matrix;
            this.mGroupName = null;
            this.mRotate = copy.mRotate;
            this.mPivotX = copy.mPivotX;
            this.mPivotY = copy.mPivotY;
            this.mScaleX = copy.mScaleX;
            this.mScaleY = copy.mScaleY;
            this.mTranslateX = copy.mTranslateX;
            this.mTranslateY = copy.mTranslateY;
            this.mThemeAttrs = copy.mThemeAttrs;
            String str = copy.mGroupName;
            this.mGroupName = str;
            this.mChangingConfigurations = copy.mChangingConfigurations;
            if (str != null) {
                arrayMap.put(str, this);
            }
            matrix.set(copy.mLocalMatrix);
            ArrayList<VObject> arrayList = copy.mChildren;
            for (int i = 0; i < arrayList.size(); i++) {
                VObject vObject = arrayList.get(i);
                if (vObject instanceof VGroup) {
                    this.mChildren.add(new VGroup((VGroup) vObject, arrayMap));
                } else {
                    if (vObject instanceof VFullPath) {
                        vPath = new VFullPath((VFullPath) vObject);
                    } else if (vObject instanceof VClipPath) {
                        vPath = new VClipPath((VClipPath) vObject);
                    } else {
                        throw new IllegalStateException("Unknown object in the tree!");
                    }
                    this.mChildren.add(vPath);
                    if (vPath.mPathName != null) {
                        arrayMap.put(vPath.mPathName, vPath);
                    }
                }
            }
        }

        private void updateLocalMatrix() {
            this.mLocalMatrix.reset();
            this.mLocalMatrix.postTranslate(-this.mPivotX, -this.mPivotY);
            this.mLocalMatrix.postScale(this.mScaleX, this.mScaleY);
            this.mLocalMatrix.postRotate(this.mRotate, 0.0f, 0.0f);
            this.mLocalMatrix.postTranslate(this.mTranslateX + this.mPivotX, this.mTranslateY + this.mPivotY);
        }

        private void updateStateFromTypedArray(TypedArray a, XmlPullParser parser) {
            this.mThemeAttrs = null;
            this.mRotate = TypedArrayUtils.getNamedFloat(a, parser, Key.ROTATION, 5, this.mRotate);
            this.mPivotX = a.getFloat(1, this.mPivotX);
            this.mPivotY = a.getFloat(2, this.mPivotY);
            this.mScaleX = TypedArrayUtils.getNamedFloat(a, parser, "scaleX", 3, this.mScaleX);
            this.mScaleY = TypedArrayUtils.getNamedFloat(a, parser, "scaleY", 4, this.mScaleY);
            this.mTranslateX = TypedArrayUtils.getNamedFloat(a, parser, "translateX", 6, this.mTranslateX);
            this.mTranslateY = TypedArrayUtils.getNamedFloat(a, parser, "translateY", 7, this.mTranslateY);
            String string = a.getString(0);
            if (string != null) {
                this.mGroupName = string;
            }
            updateLocalMatrix();
        }

        public String getGroupName() {
            return this.mGroupName;
        }

        public Matrix getLocalMatrix() {
            return this.mLocalMatrix;
        }

        public float getPivotX() {
            return this.mPivotX;
        }

        public float getPivotY() {
            return this.mPivotY;
        }

        public float getRotation() {
            return this.mRotate;
        }

        public float getScaleX() {
            return this.mScaleX;
        }

        public float getScaleY() {
            return this.mScaleY;
        }

        public float getTranslateX() {
            return this.mTranslateX;
        }

        public float getTranslateY() {
            return this.mTranslateY;
        }

        public void inflate(Resources res, AttributeSet attrs, Resources.Theme theme, XmlPullParser parser) {
            TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_GROUP);
            updateStateFromTypedArray(obtainAttributes, parser);
            obtainAttributes.recycle();
        }

        public boolean isStateful() {
            for (int i = 0; i < this.mChildren.size(); i++) {
                if (this.mChildren.get(i).isStateful()) {
                    return true;
                }
            }
            return false;
        }

        public boolean onStateChanged(int[] stateSet) {
            boolean z = false;
            for (int i = 0; i < this.mChildren.size(); i++) {
                z |= this.mChildren.get(i).onStateChanged(stateSet);
            }
            return z;
        }

        public void setPivotX(float pivotX) {
            if (pivotX != this.mPivotX) {
                this.mPivotX = pivotX;
                updateLocalMatrix();
            }
        }

        public void setPivotY(float pivotY) {
            if (pivotY != this.mPivotY) {
                this.mPivotY = pivotY;
                updateLocalMatrix();
            }
        }

        public void setRotation(float rotation) {
            if (rotation != this.mRotate) {
                this.mRotate = rotation;
                updateLocalMatrix();
            }
        }

        public void setScaleX(float scaleX) {
            if (scaleX != this.mScaleX) {
                this.mScaleX = scaleX;
                updateLocalMatrix();
            }
        }

        public void setScaleY(float scaleY) {
            if (scaleY != this.mScaleY) {
                this.mScaleY = scaleY;
                updateLocalMatrix();
            }
        }

        public void setTranslateX(float translateX) {
            if (translateX != this.mTranslateX) {
                this.mTranslateX = translateX;
                updateLocalMatrix();
            }
        }

        public void setTranslateY(float translateY) {
            if (translateY != this.mTranslateY) {
                this.mTranslateY = translateY;
                updateLocalMatrix();
            }
        }
    }

    private static abstract class VObject {
        private VObject() {
        }

        public boolean isStateful() {
            return false;
        }

        public boolean onStateChanged(int[] stateSet) {
            return false;
        }
    }

    private static abstract class VPath extends VObject {
        protected static final int FILL_TYPE_WINDING = 0;
        int mChangingConfigurations;
        int mFillRule = 0;
        protected PathParser.PathDataNode[] mNodes = null;
        String mPathName;

        public VPath() {
            super();
        }

        public VPath(VPath copy) {
            super();
            this.mPathName = copy.mPathName;
            this.mChangingConfigurations = copy.mChangingConfigurations;
            this.mNodes = PathParser.deepCopyNodes(copy.mNodes);
        }

        public void applyTheme(Resources.Theme t) {
        }

        public boolean canApplyTheme() {
            return false;
        }

        public PathParser.PathDataNode[] getPathData() {
            return this.mNodes;
        }

        public String getPathName() {
            return this.mPathName;
        }

        public boolean isClipPath() {
            return false;
        }

        public String nodesToString(PathParser.PathDataNode[] nodes) {
            String str = " ";
            for (int i = 0; i < nodes.length; i++) {
                str = str + nodes[i].mType + ":";
                float[] fArr = nodes[i].mParams;
                for (int i2 = 0; i2 < fArr.length; i2++) {
                    str = str + fArr[i2] + ",";
                }
            }
            return str;
        }

        public void printVPath(int level) {
            String str = HttpUrl.FRAGMENT_ENCODE_SET;
            for (int i = 0; i < level; i++) {
                str = str + "    ";
            }
            Log.v(VectorDrawableCompat.LOGTAG, str + "current path is :" + this.mPathName + " pathData is " + nodesToString(this.mNodes));
        }

        public void setPathData(PathParser.PathDataNode[] nodes) {
            if (!PathParser.canMorph(this.mNodes, nodes)) {
                this.mNodes = PathParser.deepCopyNodes(nodes);
            } else {
                PathParser.updateNodes(this.mNodes, nodes);
            }
        }

        public void toPath(Path path) {
            path.reset();
            PathParser.PathDataNode[] pathDataNodeArr = this.mNodes;
            if (pathDataNodeArr != null) {
                PathParser.PathDataNode.nodesToPath(pathDataNodeArr, path);
            }
        }
    }

    private static class VPathRenderer {
        private static final Matrix IDENTITY_MATRIX = new Matrix();
        float mBaseHeight;
        float mBaseWidth;
        private int mChangingConfigurations;
        Paint mFillPaint;
        private final Matrix mFinalPathMatrix;
        Boolean mIsStateful;
        private final Path mPath;
        private PathMeasure mPathMeasure;
        private final Path mRenderPath;
        int mRootAlpha;
        final VGroup mRootGroup;
        String mRootName;
        Paint mStrokePaint;
        final ArrayMap<String, Object> mVGTargetsMap;
        float mViewportHeight;
        float mViewportWidth;

        public VPathRenderer() {
            this.mFinalPathMatrix = new Matrix();
            this.mBaseWidth = 0.0f;
            this.mBaseHeight = 0.0f;
            this.mViewportWidth = 0.0f;
            this.mViewportHeight = 0.0f;
            this.mRootAlpha = 255;
            this.mRootName = null;
            this.mIsStateful = null;
            this.mVGTargetsMap = new ArrayMap<>();
            this.mRootGroup = new VGroup();
            this.mPath = new Path();
            this.mRenderPath = new Path();
        }

        public VPathRenderer(VPathRenderer copy) {
            this.mFinalPathMatrix = new Matrix();
            this.mBaseWidth = 0.0f;
            this.mBaseHeight = 0.0f;
            this.mViewportWidth = 0.0f;
            this.mViewportHeight = 0.0f;
            this.mRootAlpha = 255;
            this.mRootName = null;
            this.mIsStateful = null;
            ArrayMap<String, Object> arrayMap = new ArrayMap<>();
            this.mVGTargetsMap = arrayMap;
            this.mRootGroup = new VGroup(copy.mRootGroup, arrayMap);
            this.mPath = new Path(copy.mPath);
            this.mRenderPath = new Path(copy.mRenderPath);
            this.mBaseWidth = copy.mBaseWidth;
            this.mBaseHeight = copy.mBaseHeight;
            this.mViewportWidth = copy.mViewportWidth;
            this.mViewportHeight = copy.mViewportHeight;
            this.mChangingConfigurations = copy.mChangingConfigurations;
            this.mRootAlpha = copy.mRootAlpha;
            this.mRootName = copy.mRootName;
            String str = copy.mRootName;
            if (str != null) {
                arrayMap.put(str, this);
            }
            this.mIsStateful = copy.mIsStateful;
        }

        private static float cross(float v1x, float v1y, float v2x, float v2y) {
            return (v1x * v2y) - (v1y * v2x);
        }

        private void drawGroupTree(VGroup currentGroup, Matrix currentMatrix, Canvas canvas, int w, int h, ColorFilter filter) {
            VGroup vGroup = currentGroup;
            vGroup.mStackedMatrix.set(currentMatrix);
            vGroup.mStackedMatrix.preConcat(vGroup.mLocalMatrix);
            canvas.save();
            for (int i = 0; i < vGroup.mChildren.size(); i++) {
                VObject vObject = vGroup.mChildren.get(i);
                if (vObject instanceof VGroup) {
                    drawGroupTree((VGroup) vObject, vGroup.mStackedMatrix, canvas, w, h, filter);
                } else if (vObject instanceof VPath) {
                    drawPath(currentGroup, (VPath) vObject, canvas, w, h, filter);
                }
            }
            canvas.restore();
        }

        private void drawPath(VGroup vGroup, VPath vPath, Canvas canvas, int w, int h, ColorFilter filter) {
            float f;
            VPath vPath2 = vPath;
            Canvas canvas2 = canvas;
            ColorFilter colorFilter = filter;
            float f2 = ((float) w) / this.mViewportWidth;
            float f3 = ((float) h) / this.mViewportHeight;
            float min = Math.min(f2, f3);
            Matrix matrix = vGroup.mStackedMatrix;
            this.mFinalPathMatrix.set(matrix);
            this.mFinalPathMatrix.postScale(f2, f3);
            float matrixScale = getMatrixScale(matrix);
            if (matrixScale != 0.0f) {
                vPath2.toPath(this.mPath);
                Path path = this.mPath;
                this.mRenderPath.reset();
                if (vPath.isClipPath()) {
                    this.mRenderPath.setFillType(vPath2.mFillRule == 0 ? Path.FillType.WINDING : Path.FillType.EVEN_ODD);
                    this.mRenderPath.addPath(path, this.mFinalPathMatrix);
                    canvas2.clipPath(this.mRenderPath);
                    float f4 = f2;
                    return;
                }
                VFullPath vFullPath = (VFullPath) vPath2;
                if (vFullPath.mTrimPathStart == 0.0f && vFullPath.mTrimPathEnd == 1.0f) {
                    float f5 = f2;
                } else {
                    float f6 = (vFullPath.mTrimPathStart + vFullPath.mTrimPathOffset) % 1.0f;
                    float f7 = (vFullPath.mTrimPathEnd + vFullPath.mTrimPathOffset) % 1.0f;
                    if (this.mPathMeasure == null) {
                        this.mPathMeasure = new PathMeasure();
                    }
                    float f8 = f2;
                    this.mPathMeasure.setPath(this.mPath, false);
                    float length = this.mPathMeasure.getLength();
                    float f9 = f6 * length;
                    float f10 = f7 * length;
                    path.reset();
                    if (f9 > f10) {
                        this.mPathMeasure.getSegment(f9, length, path, true);
                        float f11 = length;
                        f = 0.0f;
                        this.mPathMeasure.getSegment(0.0f, f10, path, true);
                    } else {
                        float f12 = length;
                        f = 0.0f;
                        this.mPathMeasure.getSegment(f9, f10, path, true);
                    }
                    path.rLineTo(f, f);
                }
                this.mRenderPath.addPath(path, this.mFinalPathMatrix);
                if (vFullPath.mFillColor.willDraw()) {
                    ComplexColorCompat complexColorCompat = vFullPath.mFillColor;
                    if (this.mFillPaint == null) {
                        Paint paint = new Paint(1);
                        this.mFillPaint = paint;
                        paint.setStyle(Paint.Style.FILL);
                    }
                    Paint paint2 = this.mFillPaint;
                    if (complexColorCompat.isGradient()) {
                        Shader shader = complexColorCompat.getShader();
                        shader.setLocalMatrix(this.mFinalPathMatrix);
                        paint2.setShader(shader);
                        paint2.setAlpha(Math.round(vFullPath.mFillAlpha * 255.0f));
                    } else {
                        paint2.setShader((Shader) null);
                        paint2.setAlpha(255);
                        paint2.setColor(VectorDrawableCompat.applyAlpha(complexColorCompat.getColor(), vFullPath.mFillAlpha));
                    }
                    paint2.setColorFilter(colorFilter);
                    this.mRenderPath.setFillType(vFullPath.mFillRule == 0 ? Path.FillType.WINDING : Path.FillType.EVEN_ODD);
                    canvas2.drawPath(this.mRenderPath, paint2);
                }
                if (vFullPath.mStrokeColor.willDraw()) {
                    ComplexColorCompat complexColorCompat2 = vFullPath.mStrokeColor;
                    if (this.mStrokePaint == null) {
                        Paint paint3 = new Paint(1);
                        this.mStrokePaint = paint3;
                        paint3.setStyle(Paint.Style.STROKE);
                    }
                    Paint paint4 = this.mStrokePaint;
                    if (vFullPath.mStrokeLineJoin != null) {
                        paint4.setStrokeJoin(vFullPath.mStrokeLineJoin);
                    }
                    if (vFullPath.mStrokeLineCap != null) {
                        paint4.setStrokeCap(vFullPath.mStrokeLineCap);
                    }
                    paint4.setStrokeMiter(vFullPath.mStrokeMiterlimit);
                    if (complexColorCompat2.isGradient()) {
                        Shader shader2 = complexColorCompat2.getShader();
                        shader2.setLocalMatrix(this.mFinalPathMatrix);
                        paint4.setShader(shader2);
                        paint4.setAlpha(Math.round(vFullPath.mStrokeAlpha * 255.0f));
                    } else {
                        paint4.setShader((Shader) null);
                        paint4.setAlpha(255);
                        paint4.setColor(VectorDrawableCompat.applyAlpha(complexColorCompat2.getColor(), vFullPath.mStrokeAlpha));
                    }
                    paint4.setColorFilter(colorFilter);
                    paint4.setStrokeWidth(vFullPath.mStrokeWidth * min * matrixScale);
                    canvas2.drawPath(this.mRenderPath, paint4);
                }
            }
        }

        private float getMatrixScale(Matrix groupStackedMatrix) {
            float[] fArr = {0.0f, 1.0f, 1.0f, 0.0f};
            groupStackedMatrix.mapVectors(fArr);
            float cross = cross(fArr[0], fArr[1], fArr[2], fArr[3]);
            float max = Math.max((float) Math.hypot((double) fArr[0], (double) fArr[1]), (float) Math.hypot((double) fArr[2], (double) fArr[3]));
            if (max > 0.0f) {
                return Math.abs(cross) / max;
            }
            return 0.0f;
        }

        public void draw(Canvas canvas, int w, int h, ColorFilter filter) {
            drawGroupTree(this.mRootGroup, IDENTITY_MATRIX, canvas, w, h, filter);
        }

        public float getAlpha() {
            return ((float) getRootAlpha()) / 255.0f;
        }

        public int getRootAlpha() {
            return this.mRootAlpha;
        }

        public boolean isStateful() {
            if (this.mIsStateful == null) {
                this.mIsStateful = Boolean.valueOf(this.mRootGroup.isStateful());
            }
            return this.mIsStateful.booleanValue();
        }

        public boolean onStateChanged(int[] stateSet) {
            return this.mRootGroup.onStateChanged(stateSet);
        }

        public void setAlpha(float alpha) {
            setRootAlpha((int) (255.0f * alpha));
        }

        public void setRootAlpha(int alpha) {
            this.mRootAlpha = alpha;
        }
    }

    private static class VectorDrawableCompatState extends Drawable.ConstantState {
        boolean mAutoMirrored;
        boolean mCacheDirty;
        boolean mCachedAutoMirrored;
        Bitmap mCachedBitmap;
        int mCachedRootAlpha;
        int[] mCachedThemeAttrs;
        ColorStateList mCachedTint;
        PorterDuff.Mode mCachedTintMode;
        int mChangingConfigurations;
        Paint mTempPaint;
        ColorStateList mTint;
        PorterDuff.Mode mTintMode;
        VPathRenderer mVPathRenderer;

        public VectorDrawableCompatState() {
            this.mTint = null;
            this.mTintMode = VectorDrawableCompat.DEFAULT_TINT_MODE;
            this.mVPathRenderer = new VPathRenderer();
        }

        public VectorDrawableCompatState(VectorDrawableCompatState copy) {
            this.mTint = null;
            this.mTintMode = VectorDrawableCompat.DEFAULT_TINT_MODE;
            if (copy != null) {
                this.mChangingConfigurations = copy.mChangingConfigurations;
                this.mVPathRenderer = new VPathRenderer(copy.mVPathRenderer);
                if (copy.mVPathRenderer.mFillPaint != null) {
                    this.mVPathRenderer.mFillPaint = new Paint(copy.mVPathRenderer.mFillPaint);
                }
                if (copy.mVPathRenderer.mStrokePaint != null) {
                    this.mVPathRenderer.mStrokePaint = new Paint(copy.mVPathRenderer.mStrokePaint);
                }
                this.mTint = copy.mTint;
                this.mTintMode = copy.mTintMode;
                this.mAutoMirrored = copy.mAutoMirrored;
            }
        }

        public boolean canReuseBitmap(int width, int height) {
            return width == this.mCachedBitmap.getWidth() && height == this.mCachedBitmap.getHeight();
        }

        public boolean canReuseCache() {
            return !this.mCacheDirty && this.mCachedTint == this.mTint && this.mCachedTintMode == this.mTintMode && this.mCachedAutoMirrored == this.mAutoMirrored && this.mCachedRootAlpha == this.mVPathRenderer.getRootAlpha();
        }

        public void createCachedBitmapIfNeeded(int width, int height) {
            if (this.mCachedBitmap == null || !canReuseBitmap(width, height)) {
                this.mCachedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                this.mCacheDirty = true;
            }
        }

        public void drawCachedBitmapWithRootAlpha(Canvas canvas, ColorFilter filter, Rect originalBounds) {
            canvas.drawBitmap(this.mCachedBitmap, (Rect) null, originalBounds, getPaint(filter));
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations;
        }

        public Paint getPaint(ColorFilter filter) {
            if (!hasTranslucentRoot() && filter == null) {
                return null;
            }
            if (this.mTempPaint == null) {
                Paint paint = new Paint();
                this.mTempPaint = paint;
                paint.setFilterBitmap(true);
            }
            this.mTempPaint.setAlpha(this.mVPathRenderer.getRootAlpha());
            this.mTempPaint.setColorFilter(filter);
            return this.mTempPaint;
        }

        public boolean hasTranslucentRoot() {
            return this.mVPathRenderer.getRootAlpha() < 255;
        }

        public boolean isStateful() {
            return this.mVPathRenderer.isStateful();
        }

        public Drawable newDrawable() {
            return new VectorDrawableCompat(this);
        }

        public Drawable newDrawable(Resources res) {
            return new VectorDrawableCompat(this);
        }

        public boolean onStateChanged(int[] stateSet) {
            boolean onStateChanged = this.mVPathRenderer.onStateChanged(stateSet);
            this.mCacheDirty |= onStateChanged;
            return onStateChanged;
        }

        public void updateCacheStates() {
            this.mCachedTint = this.mTint;
            this.mCachedTintMode = this.mTintMode;
            this.mCachedRootAlpha = this.mVPathRenderer.getRootAlpha();
            this.mCachedAutoMirrored = this.mAutoMirrored;
            this.mCacheDirty = false;
        }

        public void updateCachedBitmap(int width, int height) {
            this.mCachedBitmap.eraseColor(0);
            this.mVPathRenderer.draw(new Canvas(this.mCachedBitmap), width, height, (ColorFilter) null);
        }
    }

    private static class VectorDrawableDelegateState extends Drawable.ConstantState {
        private final Drawable.ConstantState mDelegateState;

        public VectorDrawableDelegateState(Drawable.ConstantState state) {
            this.mDelegateState = state;
        }

        public boolean canApplyTheme() {
            return this.mDelegateState.canApplyTheme();
        }

        public int getChangingConfigurations() {
            return this.mDelegateState.getChangingConfigurations();
        }

        public Drawable newDrawable() {
            VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
            vectorDrawableCompat.mDelegateDrawable = (VectorDrawable) this.mDelegateState.newDrawable();
            return vectorDrawableCompat;
        }

        public Drawable newDrawable(Resources res) {
            VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
            vectorDrawableCompat.mDelegateDrawable = (VectorDrawable) this.mDelegateState.newDrawable(res);
            return vectorDrawableCompat;
        }

        public Drawable newDrawable(Resources res, Resources.Theme theme) {
            VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
            vectorDrawableCompat.mDelegateDrawable = (VectorDrawable) this.mDelegateState.newDrawable(res, theme);
            return vectorDrawableCompat;
        }
    }

    VectorDrawableCompat() {
        this.mAllowCaching = true;
        this.mTmpFloats = new float[9];
        this.mTmpMatrix = new Matrix();
        this.mTmpBounds = new Rect();
        this.mVectorState = new VectorDrawableCompatState();
    }

    VectorDrawableCompat(VectorDrawableCompatState state) {
        this.mAllowCaching = true;
        this.mTmpFloats = new float[9];
        this.mTmpMatrix = new Matrix();
        this.mTmpBounds = new Rect();
        this.mVectorState = state;
        this.mTintFilter = updateTintFilter(this.mTintFilter, state.mTint, state.mTintMode);
    }

    static int applyAlpha(int color, float alpha) {
        return (color & ViewCompat.MEASURED_SIZE_MASK) | (((int) (((float) Color.alpha(color)) * alpha)) << 24);
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0039 A[Catch:{ XmlPullParserException -> 0x004b, IOException -> 0x0046 }] */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x003e A[Catch:{ XmlPullParserException -> 0x004b, IOException -> 0x0046 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static androidx.vectordrawable.graphics.drawable.VectorDrawableCompat create(android.content.res.Resources r7, int r8, android.content.res.Resources.Theme r9) {
        /*
            java.lang.String r0 = "parser error"
            java.lang.String r1 = "VectorDrawableCompat"
            int r2 = android.os.Build.VERSION.SDK_INT
            r3 = 24
            if (r2 < r3) goto L_0x0023
            androidx.vectordrawable.graphics.drawable.VectorDrawableCompat r0 = new androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
            r0.<init>()
            android.graphics.drawable.Drawable r1 = androidx.core.content.res.ResourcesCompat.getDrawable(r7, r8, r9)
            r0.mDelegateDrawable = r1
            androidx.vectordrawable.graphics.drawable.VectorDrawableCompat$VectorDrawableDelegateState r1 = new androidx.vectordrawable.graphics.drawable.VectorDrawableCompat$VectorDrawableDelegateState
            android.graphics.drawable.Drawable r2 = r0.mDelegateDrawable
            android.graphics.drawable.Drawable$ConstantState r2 = r2.getConstantState()
            r1.<init>(r2)
            r0.mCachedConstantStateDelegate = r1
            return r0
        L_0x0023:
            android.content.res.XmlResourceParser r2 = r7.getXml(r8)     // Catch:{ XmlPullParserException -> 0x004b, IOException -> 0x0046 }
            android.util.AttributeSet r3 = android.util.Xml.asAttributeSet(r2)     // Catch:{ XmlPullParserException -> 0x004b, IOException -> 0x0046 }
        L_0x002b:
            int r4 = r2.next()     // Catch:{ XmlPullParserException -> 0x004b, IOException -> 0x0046 }
            r5 = r4
            r6 = 2
            if (r4 == r6) goto L_0x0037
            r4 = 1
            if (r5 == r4) goto L_0x0037
            goto L_0x002b
        L_0x0037:
            if (r5 != r6) goto L_0x003e
            androidx.vectordrawable.graphics.drawable.VectorDrawableCompat r0 = createFromXmlInner(r7, r2, r3, r9)     // Catch:{ XmlPullParserException -> 0x004b, IOException -> 0x0046 }
            return r0
        L_0x003e:
            org.xmlpull.v1.XmlPullParserException r4 = new org.xmlpull.v1.XmlPullParserException     // Catch:{ XmlPullParserException -> 0x004b, IOException -> 0x0046 }
            java.lang.String r6 = "No start tag found"
            r4.<init>(r6)     // Catch:{ XmlPullParserException -> 0x004b, IOException -> 0x0046 }
            throw r4     // Catch:{ XmlPullParserException -> 0x004b, IOException -> 0x0046 }
        L_0x0046:
            r2 = move-exception
            android.util.Log.e(r1, r0, r2)
            goto L_0x0050
        L_0x004b:
            r2 = move-exception
            android.util.Log.e(r1, r0, r2)
        L_0x0050:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.vectordrawable.graphics.drawable.VectorDrawableCompat.create(android.content.res.Resources, int, android.content.res.Resources$Theme):androidx.vectordrawable.graphics.drawable.VectorDrawableCompat");
    }

    public static VectorDrawableCompat createFromXmlInner(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
        vectorDrawableCompat.inflate(r, parser, attrs, theme);
        return vectorDrawableCompat;
    }

    private void inflateInternal(Resources res, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        Resources resources = res;
        XmlPullParser xmlPullParser = parser;
        AttributeSet attributeSet = attrs;
        Resources.Theme theme2 = theme;
        VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
        VPathRenderer vPathRenderer = vectorDrawableCompatState.mVPathRenderer;
        boolean z = true;
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.push(vPathRenderer.mRootGroup);
        int eventType = parser.getEventType();
        int depth = parser.getDepth() + 1;
        for (int i = 1; eventType != i && (parser.getDepth() >= depth || eventType != 3); i = 1) {
            if (eventType == 2) {
                String name = parser.getName();
                VGroup vGroup = (VGroup) arrayDeque.peek();
                if (SHAPE_PATH.equals(name)) {
                    VFullPath vFullPath = new VFullPath();
                    vFullPath.inflate(resources, attributeSet, theme2, xmlPullParser);
                    vGroup.mChildren.add(vFullPath);
                    if (vFullPath.getPathName() != null) {
                        vPathRenderer.mVGTargetsMap.put(vFullPath.getPathName(), vFullPath);
                    }
                    z = false;
                    vectorDrawableCompatState.mChangingConfigurations |= vFullPath.mChangingConfigurations;
                } else if (SHAPE_CLIP_PATH.equals(name)) {
                    VClipPath vClipPath = new VClipPath();
                    vClipPath.inflate(resources, attributeSet, theme2, xmlPullParser);
                    vGroup.mChildren.add(vClipPath);
                    if (vClipPath.getPathName() != null) {
                        vPathRenderer.mVGTargetsMap.put(vClipPath.getPathName(), vClipPath);
                    }
                    vectorDrawableCompatState.mChangingConfigurations |= vClipPath.mChangingConfigurations;
                } else if (SHAPE_GROUP.equals(name)) {
                    VGroup vGroup2 = new VGroup();
                    vGroup2.inflate(resources, attributeSet, theme2, xmlPullParser);
                    vGroup.mChildren.add(vGroup2);
                    arrayDeque.push(vGroup2);
                    if (vGroup2.getGroupName() != null) {
                        vPathRenderer.mVGTargetsMap.put(vGroup2.getGroupName(), vGroup2);
                    }
                    vectorDrawableCompatState.mChangingConfigurations |= vGroup2.mChangingConfigurations;
                }
            } else if (eventType == 3 && SHAPE_GROUP.equals(parser.getName())) {
                arrayDeque.pop();
            }
            eventType = parser.next();
        }
        if (z) {
            throw new XmlPullParserException("no path defined");
        }
    }

    private boolean needMirroring() {
        return Build.VERSION.SDK_INT >= 17 && isAutoMirrored() && DrawableCompat.getLayoutDirection(this) == 1;
    }

    private static PorterDuff.Mode parseTintModeCompat(int value, PorterDuff.Mode defaultMode) {
        switch (value) {
            case 3:
                return PorterDuff.Mode.SRC_OVER;
            case 5:
                return PorterDuff.Mode.SRC_IN;
            case 9:
                return PorterDuff.Mode.SRC_ATOP;
            case 14:
                return PorterDuff.Mode.MULTIPLY;
            case 15:
                return PorterDuff.Mode.SCREEN;
            case 16:
                return PorterDuff.Mode.ADD;
            default:
                return defaultMode;
        }
    }

    private void printGroupTree(VGroup currentGroup, int level) {
        String str = HttpUrl.FRAGMENT_ENCODE_SET;
        for (int i = 0; i < level; i++) {
            str = str + "    ";
        }
        Log.v(LOGTAG, str + "current group is :" + currentGroup.getGroupName() + " rotation is " + currentGroup.mRotate);
        Log.v(LOGTAG, str + "matrix is :" + currentGroup.getLocalMatrix().toString());
        for (int i2 = 0; i2 < currentGroup.mChildren.size(); i2++) {
            VObject vObject = currentGroup.mChildren.get(i2);
            if (vObject instanceof VGroup) {
                printGroupTree((VGroup) vObject, level + 1);
            } else {
                ((VPath) vObject).printVPath(level + 1);
            }
        }
    }

    private void updateStateFromTypedArray(TypedArray a, XmlPullParser parser, Resources.Theme theme) throws XmlPullParserException {
        VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
        VPathRenderer vPathRenderer = vectorDrawableCompatState.mVPathRenderer;
        vectorDrawableCompatState.mTintMode = parseTintModeCompat(TypedArrayUtils.getNamedInt(a, parser, "tintMode", 6, -1), PorterDuff.Mode.SRC_IN);
        ColorStateList namedColorStateList = TypedArrayUtils.getNamedColorStateList(a, parser, theme, "tint", 1);
        if (namedColorStateList != null) {
            vectorDrawableCompatState.mTint = namedColorStateList;
        }
        vectorDrawableCompatState.mAutoMirrored = TypedArrayUtils.getNamedBoolean(a, parser, "autoMirrored", 5, vectorDrawableCompatState.mAutoMirrored);
        vPathRenderer.mViewportWidth = TypedArrayUtils.getNamedFloat(a, parser, "viewportWidth", 7, vPathRenderer.mViewportWidth);
        vPathRenderer.mViewportHeight = TypedArrayUtils.getNamedFloat(a, parser, "viewportHeight", 8, vPathRenderer.mViewportHeight);
        if (vPathRenderer.mViewportWidth <= 0.0f) {
            throw new XmlPullParserException(a.getPositionDescription() + "<vector> tag requires viewportWidth > 0");
        } else if (vPathRenderer.mViewportHeight > 0.0f) {
            vPathRenderer.mBaseWidth = a.getDimension(3, vPathRenderer.mBaseWidth);
            vPathRenderer.mBaseHeight = a.getDimension(2, vPathRenderer.mBaseHeight);
            if (vPathRenderer.mBaseWidth <= 0.0f) {
                throw new XmlPullParserException(a.getPositionDescription() + "<vector> tag requires width > 0");
            } else if (vPathRenderer.mBaseHeight > 0.0f) {
                vPathRenderer.setAlpha(TypedArrayUtils.getNamedFloat(a, parser, "alpha", 4, vPathRenderer.getAlpha()));
                String string = a.getString(0);
                if (string != null) {
                    vPathRenderer.mRootName = string;
                    vPathRenderer.mVGTargetsMap.put(string, vPathRenderer);
                }
            } else {
                throw new XmlPullParserException(a.getPositionDescription() + "<vector> tag requires height > 0");
            }
        } else {
            throw new XmlPullParserException(a.getPositionDescription() + "<vector> tag requires viewportHeight > 0");
        }
    }

    public /* bridge */ /* synthetic */ void applyTheme(Resources.Theme theme) {
        super.applyTheme(theme);
    }

    public boolean canApplyTheme() {
        if (this.mDelegateDrawable == null) {
            return false;
        }
        DrawableCompat.canApplyTheme(this.mDelegateDrawable);
        return false;
    }

    public /* bridge */ /* synthetic */ void clearColorFilter() {
        super.clearColorFilter();
    }

    public void draw(Canvas canvas) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.draw(canvas);
            return;
        }
        copyBounds(this.mTmpBounds);
        if (this.mTmpBounds.width() > 0 && this.mTmpBounds.height() > 0) {
            ColorFilter colorFilter = this.mColorFilter;
            if (colorFilter == null) {
                colorFilter = this.mTintFilter;
            }
            canvas.getMatrix(this.mTmpMatrix);
            this.mTmpMatrix.getValues(this.mTmpFloats);
            float abs = Math.abs(this.mTmpFloats[0]);
            float abs2 = Math.abs(this.mTmpFloats[4]);
            float abs3 = Math.abs(this.mTmpFloats[1]);
            float abs4 = Math.abs(this.mTmpFloats[3]);
            if (!(abs3 == 0.0f && abs4 == 0.0f)) {
                abs = 1.0f;
                abs2 = 1.0f;
            }
            int min = Math.min(2048, (int) (((float) this.mTmpBounds.width()) * abs));
            int min2 = Math.min(2048, (int) (((float) this.mTmpBounds.height()) * abs2));
            if (min > 0 && min2 > 0) {
                int save = canvas.save();
                canvas.translate((float) this.mTmpBounds.left, (float) this.mTmpBounds.top);
                if (needMirroring()) {
                    canvas.translate((float) this.mTmpBounds.width(), 0.0f);
                    canvas.scale(-1.0f, 1.0f);
                }
                this.mTmpBounds.offsetTo(0, 0);
                this.mVectorState.createCachedBitmapIfNeeded(min, min2);
                if (!this.mAllowCaching) {
                    this.mVectorState.updateCachedBitmap(min, min2);
                } else if (!this.mVectorState.canReuseCache()) {
                    this.mVectorState.updateCachedBitmap(min, min2);
                    this.mVectorState.updateCacheStates();
                }
                this.mVectorState.drawCachedBitmapWithRootAlpha(canvas, colorFilter, this.mTmpBounds);
                canvas.restoreToCount(save);
            }
        }
    }

    public int getAlpha() {
        return this.mDelegateDrawable != null ? DrawableCompat.getAlpha(this.mDelegateDrawable) : this.mVectorState.mVPathRenderer.getRootAlpha();
    }

    public int getChangingConfigurations() {
        return this.mDelegateDrawable != null ? this.mDelegateDrawable.getChangingConfigurations() : super.getChangingConfigurations() | this.mVectorState.getChangingConfigurations();
    }

    public ColorFilter getColorFilter() {
        return this.mDelegateDrawable != null ? DrawableCompat.getColorFilter(this.mDelegateDrawable) : this.mColorFilter;
    }

    public Drawable.ConstantState getConstantState() {
        if (this.mDelegateDrawable != null && Build.VERSION.SDK_INT >= 24) {
            return new VectorDrawableDelegateState(this.mDelegateDrawable.getConstantState());
        }
        this.mVectorState.mChangingConfigurations = getChangingConfigurations();
        return this.mVectorState;
    }

    public /* bridge */ /* synthetic */ Drawable getCurrent() {
        return super.getCurrent();
    }

    public int getIntrinsicHeight() {
        return this.mDelegateDrawable != null ? this.mDelegateDrawable.getIntrinsicHeight() : (int) this.mVectorState.mVPathRenderer.mBaseHeight;
    }

    public int getIntrinsicWidth() {
        return this.mDelegateDrawable != null ? this.mDelegateDrawable.getIntrinsicWidth() : (int) this.mVectorState.mVPathRenderer.mBaseWidth;
    }

    public /* bridge */ /* synthetic */ int getMinimumHeight() {
        return super.getMinimumHeight();
    }

    public /* bridge */ /* synthetic */ int getMinimumWidth() {
        return super.getMinimumWidth();
    }

    public int getOpacity() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getOpacity();
        }
        return -3;
    }

    public /* bridge */ /* synthetic */ boolean getPadding(Rect rect) {
        return super.getPadding(rect);
    }

    public float getPixelSize() {
        VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
        if (vectorDrawableCompatState == null || vectorDrawableCompatState.mVPathRenderer == null || this.mVectorState.mVPathRenderer.mBaseWidth == 0.0f || this.mVectorState.mVPathRenderer.mBaseHeight == 0.0f || this.mVectorState.mVPathRenderer.mViewportHeight == 0.0f || this.mVectorState.mVPathRenderer.mViewportWidth == 0.0f) {
            return 1.0f;
        }
        float f = this.mVectorState.mVPathRenderer.mBaseWidth;
        float f2 = this.mVectorState.mVPathRenderer.mBaseHeight;
        return Math.min(this.mVectorState.mVPathRenderer.mViewportWidth / f, this.mVectorState.mVPathRenderer.mViewportHeight / f2);
    }

    public /* bridge */ /* synthetic */ int[] getState() {
        return super.getState();
    }

    /* access modifiers changed from: package-private */
    public Object getTargetByName(String name) {
        return this.mVectorState.mVPathRenderer.mVGTargetsMap.get(name);
    }

    public /* bridge */ /* synthetic */ Region getTransparentRegion() {
        return super.getTransparentRegion();
    }

    public void inflate(Resources res, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.inflate(res, parser, attrs);
        } else {
            inflate(res, parser, attrs, (Resources.Theme) null);
        }
    }

    public void inflate(Resources res, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.inflate(this.mDelegateDrawable, res, parser, attrs, theme);
            return;
        }
        VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
        vectorDrawableCompatState.mVPathRenderer = new VPathRenderer();
        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_TYPE_ARRAY);
        updateStateFromTypedArray(obtainAttributes, parser, theme);
        obtainAttributes.recycle();
        vectorDrawableCompatState.mChangingConfigurations = getChangingConfigurations();
        vectorDrawableCompatState.mCacheDirty = true;
        inflateInternal(res, parser, attrs, theme);
        this.mTintFilter = updateTintFilter(this.mTintFilter, vectorDrawableCompatState.mTint, vectorDrawableCompatState.mTintMode);
    }

    public void invalidateSelf() {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.invalidateSelf();
        } else {
            super.invalidateSelf();
        }
    }

    public boolean isAutoMirrored() {
        return this.mDelegateDrawable != null ? DrawableCompat.isAutoMirrored(this.mDelegateDrawable) : this.mVectorState.mAutoMirrored;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0011, code lost:
        r0 = r1.mVectorState;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isStateful() {
        /*
            r1 = this;
            android.graphics.drawable.Drawable r0 = r1.mDelegateDrawable
            if (r0 == 0) goto L_0x000b
            android.graphics.drawable.Drawable r0 = r1.mDelegateDrawable
            boolean r0 = r0.isStateful()
            return r0
        L_0x000b:
            boolean r0 = super.isStateful()
            if (r0 != 0) goto L_0x002e
            androidx.vectordrawable.graphics.drawable.VectorDrawableCompat$VectorDrawableCompatState r0 = r1.mVectorState
            if (r0 == 0) goto L_0x002c
            boolean r0 = r0.isStateful()
            if (r0 != 0) goto L_0x002e
            androidx.vectordrawable.graphics.drawable.VectorDrawableCompat$VectorDrawableCompatState r0 = r1.mVectorState
            android.content.res.ColorStateList r0 = r0.mTint
            if (r0 == 0) goto L_0x002c
            androidx.vectordrawable.graphics.drawable.VectorDrawableCompat$VectorDrawableCompatState r0 = r1.mVectorState
            android.content.res.ColorStateList r0 = r0.mTint
            boolean r0 = r0.isStateful()
            if (r0 == 0) goto L_0x002c
            goto L_0x002e
        L_0x002c:
            r0 = 0
            goto L_0x002f
        L_0x002e:
            r0 = 1
        L_0x002f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.vectordrawable.graphics.drawable.VectorDrawableCompat.isStateful():boolean");
    }

    public /* bridge */ /* synthetic */ void jumpToCurrentState() {
        super.jumpToCurrentState();
    }

    public Drawable mutate() {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.mutate();
            return this;
        }
        if (!this.mMutated && super.mutate() == this) {
            this.mVectorState = new VectorDrawableCompatState(this.mVectorState);
            this.mMutated = true;
        }
        return this;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect bounds) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setBounds(bounds);
        }
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] stateSet) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setState(stateSet);
        }
        boolean z = false;
        VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
        if (!(vectorDrawableCompatState.mTint == null || vectorDrawableCompatState.mTintMode == null)) {
            this.mTintFilter = updateTintFilter(this.mTintFilter, vectorDrawableCompatState.mTint, vectorDrawableCompatState.mTintMode);
            invalidateSelf();
            z = true;
        }
        if (!vectorDrawableCompatState.isStateful() || !vectorDrawableCompatState.onStateChanged(stateSet)) {
            return z;
        }
        invalidateSelf();
        return true;
    }

    public void scheduleSelf(Runnable what, long when) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.scheduleSelf(what, when);
        } else {
            super.scheduleSelf(what, when);
        }
    }

    /* access modifiers changed from: package-private */
    public void setAllowCaching(boolean allowCaching) {
        this.mAllowCaching = allowCaching;
    }

    public void setAlpha(int alpha) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setAlpha(alpha);
        } else if (this.mVectorState.mVPathRenderer.getRootAlpha() != alpha) {
            this.mVectorState.mVPathRenderer.setRootAlpha(alpha);
            invalidateSelf();
        }
    }

    public void setAutoMirrored(boolean mirrored) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setAutoMirrored(this.mDelegateDrawable, mirrored);
        } else {
            this.mVectorState.mAutoMirrored = mirrored;
        }
    }

    public /* bridge */ /* synthetic */ void setChangingConfigurations(int i) {
        super.setChangingConfigurations(i);
    }

    public /* bridge */ /* synthetic */ void setColorFilter(int i, PorterDuff.Mode mode) {
        super.setColorFilter(i, mode);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setColorFilter(colorFilter);
            return;
        }
        this.mColorFilter = colorFilter;
        invalidateSelf();
    }

    public /* bridge */ /* synthetic */ void setFilterBitmap(boolean z) {
        super.setFilterBitmap(z);
    }

    public /* bridge */ /* synthetic */ void setHotspot(float f, float f2) {
        super.setHotspot(f, f2);
    }

    public /* bridge */ /* synthetic */ void setHotspotBounds(int i, int i2, int i3, int i4) {
        super.setHotspotBounds(i, i2, i3, i4);
    }

    public /* bridge */ /* synthetic */ boolean setState(int[] iArr) {
        return super.setState(iArr);
    }

    public void setTint(int tint) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTint(this.mDelegateDrawable, tint);
        } else {
            setTintList(ColorStateList.valueOf(tint));
        }
    }

    public void setTintList(ColorStateList tint) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTintList(this.mDelegateDrawable, tint);
            return;
        }
        VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
        if (vectorDrawableCompatState.mTint != tint) {
            vectorDrawableCompatState.mTint = tint;
            this.mTintFilter = updateTintFilter(this.mTintFilter, tint, vectorDrawableCompatState.mTintMode);
            invalidateSelf();
        }
    }

    public void setTintMode(PorterDuff.Mode tintMode) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTintMode(this.mDelegateDrawable, tintMode);
            return;
        }
        VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
        if (vectorDrawableCompatState.mTintMode != tintMode) {
            vectorDrawableCompatState.mTintMode = tintMode;
            this.mTintFilter = updateTintFilter(this.mTintFilter, vectorDrawableCompatState.mTint, tintMode);
            invalidateSelf();
        }
    }

    public boolean setVisible(boolean visible, boolean restart) {
        return this.mDelegateDrawable != null ? this.mDelegateDrawable.setVisible(visible, restart) : super.setVisible(visible, restart);
    }

    public void unscheduleSelf(Runnable what) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.unscheduleSelf(what);
        } else {
            super.unscheduleSelf(what);
        }
    }

    /* access modifiers changed from: package-private */
    public PorterDuffColorFilter updateTintFilter(PorterDuffColorFilter tintFilter, ColorStateList tint, PorterDuff.Mode tintMode) {
        if (tint == null || tintMode == null) {
            return null;
        }
        return new PorterDuffColorFilter(tint.getColorForState(getState(), 0), tintMode);
    }
}
