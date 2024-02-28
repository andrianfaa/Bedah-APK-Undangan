package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.TextView;
import androidx.constraintlayout.core.motion.utils.KeyCache;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.core.widgets.Flow;
import androidx.constraintlayout.core.widgets.Guideline;
import androidx.constraintlayout.core.widgets.Helper;
import androidx.constraintlayout.core.widgets.HelperWidget;
import androidx.constraintlayout.core.widgets.Placeholder;
import androidx.constraintlayout.core.widgets.VirtualLayout;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.motion.utils.StopLogic;
import androidx.constraintlayout.motion.utils.ViewState;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.constraintlayout.widget.Barrier;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.constraintlayout.widget.R;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import mt.Log1F380D;
import okhttp3.HttpUrl;

/* compiled from: 0022 */
public class MotionLayout extends ConstraintLayout implements NestedScrollingParent3 {
    private static final boolean DEBUG = false;
    public static final int DEBUG_SHOW_NONE = 0;
    public static final int DEBUG_SHOW_PATH = 2;
    public static final int DEBUG_SHOW_PROGRESS = 1;
    private static final float EPSILON = 1.0E-5f;
    public static boolean IS_IN_EDIT_MODE = false;
    static final int MAX_KEY_FRAMES = 50;
    static final String TAG = "MotionLayout";
    public static final int TOUCH_UP_COMPLETE = 0;
    public static final int TOUCH_UP_COMPLETE_TO_END = 2;
    public static final int TOUCH_UP_COMPLETE_TO_START = 1;
    public static final int TOUCH_UP_DECELERATE = 4;
    public static final int TOUCH_UP_DECELERATE_AND_COMPLETE = 5;
    public static final int TOUCH_UP_NEVER_TO_END = 7;
    public static final int TOUCH_UP_NEVER_TO_START = 6;
    public static final int TOUCH_UP_STOP = 3;
    public static final int VELOCITY_LAYOUT = 1;
    public static final int VELOCITY_POST_LAYOUT = 0;
    public static final int VELOCITY_STATIC_LAYOUT = 3;
    public static final int VELOCITY_STATIC_POST_LAYOUT = 2;
    boolean firstDown = true;
    private float lastPos;
    private float lastY;
    private long mAnimationStartTime = 0;
    /* access modifiers changed from: private */
    public int mBeginState = -1;
    private RectF mBoundsCheck = new RectF();
    int mCurrentState = -1;
    int mDebugPath = 0;
    private DecelerateInterpolator mDecelerateLogic = new DecelerateInterpolator();
    private ArrayList<MotionHelper> mDecoratorsHelpers = null;
    private boolean mDelayedApply = false;
    private DesignTool mDesignTool;
    DevModeDraw mDevModeDraw;
    /* access modifiers changed from: private */
    public int mEndState = -1;
    int mEndWrapHeight;
    int mEndWrapWidth;
    HashMap<View, MotionController> mFrameArrayList = new HashMap<>();
    private int mFrames = 0;
    int mHeightMeasureMode;
    private boolean mInLayout = false;
    /* access modifiers changed from: private */
    public boolean mInRotation = false;
    boolean mInTransition = false;
    boolean mIndirectTransition = false;
    private boolean mInteractionEnabled = true;
    Interpolator mInterpolator;
    private Matrix mInverseMatrix = null;
    boolean mIsAnimating = false;
    private boolean mKeepAnimating = false;
    private KeyCache mKeyCache = new KeyCache();
    private long mLastDrawTime = -1;
    private float mLastFps = 0.0f;
    /* access modifiers changed from: private */
    public int mLastHeightMeasureSpec = 0;
    int mLastLayoutHeight;
    int mLastLayoutWidth;
    float mLastVelocity = 0.0f;
    /* access modifiers changed from: private */
    public int mLastWidthMeasureSpec = 0;
    private float mListenerPosition = 0.0f;
    private int mListenerState = 0;
    protected boolean mMeasureDuringTransition = false;
    Model mModel = new Model();
    private boolean mNeedsFireTransitionCompleted = false;
    int mOldHeight;
    int mOldWidth;
    private Runnable mOnComplete = null;
    private ArrayList<MotionHelper> mOnHideHelpers = null;
    private ArrayList<MotionHelper> mOnShowHelpers = null;
    float mPostInterpolationPosition;
    HashMap<View, ViewState> mPreRotate = new HashMap<>();
    /* access modifiers changed from: private */
    public int mPreRotateHeight;
    /* access modifiers changed from: private */
    public int mPreRotateWidth;
    private int mPreviouseRotation;
    Interpolator mProgressInterpolator = null;
    private View mRegionView = null;
    int mRotatMode = 0;
    MotionScene mScene;
    private int[] mScheduledTransitionTo = null;
    int mScheduledTransitions = 0;
    float mScrollTargetDT;
    float mScrollTargetDX;
    float mScrollTargetDY;
    long mScrollTargetTime;
    int mStartWrapHeight;
    int mStartWrapWidth;
    /* access modifiers changed from: private */
    public StateCache mStateCache;
    private StopLogic mStopLogic = new StopLogic();
    Rect mTempRect = new Rect();
    private boolean mTemporalInterpolator = false;
    ArrayList<Integer> mTransitionCompleted = new ArrayList<>();
    private float mTransitionDuration = 1.0f;
    float mTransitionGoalPosition = 0.0f;
    private boolean mTransitionInstantly;
    float mTransitionLastPosition = 0.0f;
    private long mTransitionLastTime;
    private TransitionListener mTransitionListener;
    private CopyOnWriteArrayList<TransitionListener> mTransitionListeners = null;
    float mTransitionPosition = 0.0f;
    TransitionState mTransitionState = TransitionState.UNDEFINED;
    boolean mUndergoingMotion = false;
    int mWidthMeasureMode;

    /* renamed from: androidx.constraintlayout.motion.widget.MotionLayout$5  reason: invalid class name */
    static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$motion$widget$MotionLayout$TransitionState;

        static {
            int[] iArr = new int[TransitionState.values().length];
            $SwitchMap$androidx$constraintlayout$motion$widget$MotionLayout$TransitionState = iArr;
            try {
                iArr[TransitionState.UNDEFINED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$motion$widget$MotionLayout$TransitionState[TransitionState.SETUP.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$motion$widget$MotionLayout$TransitionState[TransitionState.MOVING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$motion$widget$MotionLayout$TransitionState[TransitionState.FINISHED.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    class DecelerateInterpolator extends MotionInterpolator {
        float currentP = 0.0f;
        float initalV = 0.0f;
        float maxA;

        DecelerateInterpolator() {
        }

        public void config(float velocity, float position, float maxAcceleration) {
            this.initalV = velocity;
            this.currentP = position;
            this.maxA = maxAcceleration;
        }

        public float getInterpolation(float time) {
            float f = this.initalV;
            if (f > 0.0f) {
                float f2 = this.maxA;
                if (f / f2 < time) {
                    time = f / f2;
                }
                MotionLayout.this.mLastVelocity = f - (f2 * time);
                return this.currentP + ((this.initalV * time) - (((this.maxA * time) * time) / 2.0f));
            }
            float f3 = this.maxA;
            if ((-f) / f3 < time) {
                time = (-f) / f3;
            }
            MotionLayout.this.mLastVelocity = f + (f3 * time);
            return this.currentP + (this.initalV * time) + (((this.maxA * time) * time) / 2.0f);
        }

        public float getVelocity() {
            return MotionLayout.this.mLastVelocity;
        }
    }

    private class DevModeDraw {
        private static final int DEBUG_PATH_TICKS_PER_MS = 16;
        final int DIAMOND_SIZE = 10;
        final int GRAPH_COLOR = -13391360;
        final int KEYFRAME_COLOR = -2067046;
        final int RED_COLOR = -21965;
        final int SHADOW_COLOR = 1996488704;
        Rect mBounds = new Rect();
        DashPathEffect mDashPathEffect;
        Paint mFillPaint;
        int mKeyFrameCount;
        float[] mKeyFramePoints;
        Paint mPaint;
        Paint mPaintGraph;
        Paint mPaintKeyframes;
        Path mPath;
        int[] mPathMode;
        float[] mPoints;
        boolean mPresentationMode = false;
        private float[] mRectangle;
        int mShadowTranslate = 1;
        Paint mTextPaint;

        public DevModeDraw() {
            Paint paint = new Paint();
            this.mPaint = paint;
            paint.setAntiAlias(true);
            this.mPaint.setColor(-21965);
            this.mPaint.setStrokeWidth(2.0f);
            this.mPaint.setStyle(Paint.Style.STROKE);
            Paint paint2 = new Paint();
            this.mPaintKeyframes = paint2;
            paint2.setAntiAlias(true);
            this.mPaintKeyframes.setColor(-2067046);
            this.mPaintKeyframes.setStrokeWidth(2.0f);
            this.mPaintKeyframes.setStyle(Paint.Style.STROKE);
            Paint paint3 = new Paint();
            this.mPaintGraph = paint3;
            paint3.setAntiAlias(true);
            this.mPaintGraph.setColor(-13391360);
            this.mPaintGraph.setStrokeWidth(2.0f);
            this.mPaintGraph.setStyle(Paint.Style.STROKE);
            Paint paint4 = new Paint();
            this.mTextPaint = paint4;
            paint4.setAntiAlias(true);
            this.mTextPaint.setColor(-13391360);
            this.mTextPaint.setTextSize(MotionLayout.this.getContext().getResources().getDisplayMetrics().density * 12.0f);
            this.mRectangle = new float[8];
            Paint paint5 = new Paint();
            this.mFillPaint = paint5;
            paint5.setAntiAlias(true);
            DashPathEffect dashPathEffect = new DashPathEffect(new float[]{4.0f, 8.0f}, 0.0f);
            this.mDashPathEffect = dashPathEffect;
            this.mPaintGraph.setPathEffect(dashPathEffect);
            this.mKeyFramePoints = new float[100];
            this.mPathMode = new int[50];
            if (this.mPresentationMode) {
                this.mPaint.setStrokeWidth(8.0f);
                this.mFillPaint.setStrokeWidth(8.0f);
                this.mPaintKeyframes.setStrokeWidth(8.0f);
                this.mShadowTranslate = 4;
            }
        }

        private void drawBasicPath(Canvas canvas) {
            canvas.drawLines(this.mPoints, this.mPaint);
        }

        private void drawPathAsConfigured(Canvas canvas) {
            boolean z = false;
            boolean z2 = false;
            for (int i = 0; i < this.mKeyFrameCount; i++) {
                int i2 = this.mPathMode[i];
                if (i2 == 1) {
                    z = true;
                }
                if (i2 == 0) {
                    z2 = true;
                }
            }
            if (z) {
                drawPathRelative(canvas);
            }
            if (z2) {
                drawPathCartesian(canvas);
            }
        }

        private void drawPathCartesian(Canvas canvas) {
            float[] fArr = this.mPoints;
            float f = fArr[0];
            float f2 = fArr[1];
            float f3 = fArr[fArr.length - 2];
            float f4 = fArr[fArr.length - 1];
            canvas.drawLine(Math.min(f, f3), Math.max(f2, f4), Math.max(f, f3), Math.max(f2, f4), this.mPaintGraph);
            canvas.drawLine(Math.min(f, f3), Math.min(f2, f4), Math.min(f, f3), Math.max(f2, f4), this.mPaintGraph);
        }

        private void drawPathCartesianTicks(Canvas canvas, float x, float y) {
            Canvas canvas2 = canvas;
            float[] fArr = this.mPoints;
            float f = fArr[0];
            float f2 = fArr[1];
            float f3 = fArr[fArr.length - 2];
            float f4 = fArr[fArr.length - 1];
            float min = Math.min(f, f3);
            float max = Math.max(f2, f4);
            float min2 = x - Math.min(f, f3);
            float max2 = Math.max(f2, f4) - y;
            String str = HttpUrl.FRAGMENT_ENCODE_SET + (((float) ((int) (((double) ((min2 * 100.0f) / Math.abs(f3 - f))) + 0.5d))) / 100.0f);
            getTextBounds(str, this.mTextPaint);
            canvas2.drawText(str, ((min2 / 2.0f) - ((float) (this.mBounds.width() / 2))) + min, y - 20.0f, this.mTextPaint);
            float min3 = Math.min(f, f3);
            Paint paint = this.mPaintGraph;
            String str2 = str;
            float f5 = f;
            String str3 = HttpUrl.FRAGMENT_ENCODE_SET;
            canvas.drawLine(x, y, min3, y, paint);
            String str4 = str3 + (((float) ((int) (((double) ((max2 * 100.0f) / Math.abs(f4 - f2))) + 0.5d))) / 100.0f);
            getTextBounds(str4, this.mTextPaint);
            canvas2.drawText(str4, x + 5.0f, max - ((max2 / 2.0f) - ((float) (this.mBounds.height() / 2))), this.mTextPaint);
            canvas.drawLine(x, y, x, Math.max(f2, f4), this.mPaintGraph);
        }

        private void drawPathRelative(Canvas canvas) {
            float[] fArr = this.mPoints;
            canvas.drawLine(fArr[0], fArr[1], fArr[fArr.length - 2], fArr[fArr.length - 1], this.mPaintGraph);
        }

        private void drawPathRelativeTicks(Canvas canvas, float x, float y) {
            float f = x;
            float f2 = y;
            float[] fArr = this.mPoints;
            float f3 = fArr[0];
            float f4 = fArr[1];
            float f5 = fArr[fArr.length - 2];
            float f6 = fArr[fArr.length - 1];
            float hypot = (float) Math.hypot((double) (f3 - f5), (double) (f4 - f6));
            float f7 = (((f - f3) * (f5 - f3)) + ((f2 - f4) * (f6 - f4))) / (hypot * hypot);
            float f8 = f3 + ((f5 - f3) * f7);
            float f9 = f4 + ((f6 - f4) * f7);
            Path path = new Path();
            path.moveTo(f, f2);
            path.lineTo(f8, f9);
            float hypot2 = (float) Math.hypot((double) (f8 - f), (double) (f9 - f2));
            String str = HttpUrl.FRAGMENT_ENCODE_SET + (((float) ((int) ((hypot2 * 100.0f) / hypot))) / 100.0f);
            getTextBounds(str, this.mTextPaint);
            canvas.drawTextOnPath(str, path, (hypot2 / 2.0f) - ((float) (this.mBounds.width() / 2)), -20.0f, this.mTextPaint);
            float f10 = hypot2;
            Path path2 = path;
            float f11 = f9;
            canvas.drawLine(x, y, f8, f9, this.mPaintGraph);
        }

        private void drawPathScreenTicks(Canvas canvas, float x, float y, int viewWidth, int viewHeight) {
            Canvas canvas2 = canvas;
            float f = x;
            float f2 = y;
            String str = HttpUrl.FRAGMENT_ENCODE_SET + (((float) ((int) (((double) (((f - ((float) (viewWidth / 2))) * 100.0f) / ((float) (MotionLayout.this.getWidth() - viewWidth)))) + 0.5d))) / 100.0f);
            getTextBounds(str, this.mTextPaint);
            canvas2.drawText(str, ((f / 2.0f) - ((float) (this.mBounds.width() / 2))) + 0.0f, y - 20.0f, this.mTextPaint);
            float min = Math.min(0.0f, 1.0f);
            Paint paint = this.mPaintGraph;
            String str2 = str;
            String str3 = HttpUrl.FRAGMENT_ENCODE_SET;
            canvas.drawLine(x, y, min, y, paint);
            String str4 = str3 + (((float) ((int) (((double) (((f2 - ((float) (viewHeight / 2))) * 100.0f) / ((float) (MotionLayout.this.getHeight() - viewHeight)))) + 0.5d))) / 100.0f);
            getTextBounds(str4, this.mTextPaint);
            canvas2.drawText(str4, x + 5.0f, 0.0f - ((f2 / 2.0f) - ((float) (this.mBounds.height() / 2))), this.mTextPaint);
            canvas.drawLine(x, y, x, Math.max(0.0f, 1.0f), this.mPaintGraph);
        }

        private void drawRectangle(Canvas canvas, MotionController motionController) {
            this.mPath.reset();
            for (int i = 0; i <= 50; i++) {
                motionController.buildRect(((float) i) / ((float) 50), this.mRectangle, 0);
                Path path = this.mPath;
                float[] fArr = this.mRectangle;
                path.moveTo(fArr[0], fArr[1]);
                Path path2 = this.mPath;
                float[] fArr2 = this.mRectangle;
                path2.lineTo(fArr2[2], fArr2[3]);
                Path path3 = this.mPath;
                float[] fArr3 = this.mRectangle;
                path3.lineTo(fArr3[4], fArr3[5]);
                Path path4 = this.mPath;
                float[] fArr4 = this.mRectangle;
                path4.lineTo(fArr4[6], fArr4[7]);
                this.mPath.close();
            }
            this.mPaint.setColor(1140850688);
            canvas.translate(2.0f, 2.0f);
            canvas.drawPath(this.mPath, this.mPaint);
            canvas.translate(-2.0f, -2.0f);
            this.mPaint.setColor(SupportMenu.CATEGORY_MASK);
            canvas.drawPath(this.mPath, this.mPaint);
        }

        private void drawTicks(Canvas canvas, int mode, int keyFrames, MotionController motionController) {
            int i;
            int i2;
            Canvas canvas2 = canvas;
            int i3 = mode;
            MotionController motionController2 = motionController;
            if (motionController2.mView != null) {
                i2 = motionController2.mView.getWidth();
                i = motionController2.mView.getHeight();
            } else {
                i2 = 0;
                i = 0;
            }
            for (int i4 = 1; i4 < keyFrames - 1; i4++) {
                if (i3 != 4 || this.mPathMode[i4 - 1] != 0) {
                    float[] fArr = this.mKeyFramePoints;
                    float f = fArr[i4 * 2];
                    float f2 = fArr[(i4 * 2) + 1];
                    this.mPath.reset();
                    this.mPath.moveTo(f, f2 + 10.0f);
                    this.mPath.lineTo(f + 10.0f, f2);
                    this.mPath.lineTo(f, f2 - 10.0f);
                    this.mPath.lineTo(f - 10.0f, f2);
                    this.mPath.close();
                    MotionPaths keyFrame = motionController2.getKeyFrame(i4 - 1);
                    if (i3 == 4) {
                        int[] iArr = this.mPathMode;
                        if (iArr[i4 - 1] == 1) {
                            drawPathRelativeTicks(canvas2, f - 0.0f, f2 - 0.0f);
                        } else if (iArr[i4 - 1] == 0) {
                            drawPathCartesianTicks(canvas2, f - 0.0f, f2 - 0.0f);
                        } else if (iArr[i4 - 1] == 2) {
                            drawPathScreenTicks(canvas, f - 0.0f, f2 - 0.0f, i2, i);
                        }
                        canvas2.drawPath(this.mPath, this.mFillPaint);
                    }
                    if (i3 == 2) {
                        drawPathRelativeTicks(canvas2, f - 0.0f, f2 - 0.0f);
                    }
                    if (i3 == 3) {
                        drawPathCartesianTicks(canvas2, f - 0.0f, f2 - 0.0f);
                    }
                    if (i3 == 6) {
                        drawPathScreenTicks(canvas, f - 0.0f, f2 - 0.0f, i2, i);
                    }
                    if (0.0f == 0.0f && 0.0f == 0.0f) {
                        canvas2.drawPath(this.mPath, this.mFillPaint);
                    } else {
                        drawTranslation(canvas, f - 0.0f, f2 - 0.0f, f, f2);
                    }
                }
            }
            float[] fArr2 = this.mPoints;
            if (fArr2.length > 1) {
                canvas2.drawCircle(fArr2[0], fArr2[1], 8.0f, this.mPaintKeyframes);
                float[] fArr3 = this.mPoints;
                canvas2.drawCircle(fArr3[fArr3.length - 2], fArr3[fArr3.length - 1], 8.0f, this.mPaintKeyframes);
            }
        }

        private void drawTranslation(Canvas canvas, float x1, float y1, float x2, float y2) {
            canvas.drawRect(x1, y1, x2, y2, this.mPaintGraph);
            canvas.drawLine(x1, y1, x2, y2, this.mPaintGraph);
        }

        public void draw(Canvas canvas, HashMap<View, MotionController> hashMap, int duration, int debugPath) {
            if (hashMap != null && hashMap.size() != 0) {
                canvas.save();
                if (!MotionLayout.this.isInEditMode() && (debugPath & 1) == 2) {
                    String str = MotionLayout.this.getContext().getResources().getResourceName(MotionLayout.this.mEndState) + ":" + MotionLayout.this.getProgress();
                    canvas.drawText(str, 10.0f, (float) (MotionLayout.this.getHeight() - 30), this.mTextPaint);
                    canvas.drawText(str, 11.0f, (float) (MotionLayout.this.getHeight() - 29), this.mPaint);
                }
                for (MotionController next : hashMap.values()) {
                    int drawPath = next.getDrawPath();
                    if (debugPath > 0 && drawPath == 0) {
                        drawPath = 1;
                    }
                    if (drawPath != 0) {
                        this.mKeyFrameCount = next.buildKeyFrames(this.mKeyFramePoints, this.mPathMode);
                        if (drawPath >= 1) {
                            int i = duration / 16;
                            float[] fArr = this.mPoints;
                            if (fArr == null || fArr.length != i * 2) {
                                this.mPoints = new float[(i * 2)];
                                this.mPath = new Path();
                            }
                            int i2 = this.mShadowTranslate;
                            canvas.translate((float) i2, (float) i2);
                            this.mPaint.setColor(1996488704);
                            this.mFillPaint.setColor(1996488704);
                            this.mPaintKeyframes.setColor(1996488704);
                            this.mPaintGraph.setColor(1996488704);
                            next.buildPath(this.mPoints, i);
                            drawAll(canvas, drawPath, this.mKeyFrameCount, next);
                            this.mPaint.setColor(-21965);
                            this.mPaintKeyframes.setColor(-2067046);
                            this.mFillPaint.setColor(-2067046);
                            this.mPaintGraph.setColor(-13391360);
                            int i3 = this.mShadowTranslate;
                            canvas.translate((float) (-i3), (float) (-i3));
                            drawAll(canvas, drawPath, this.mKeyFrameCount, next);
                            if (drawPath == 5) {
                                drawRectangle(canvas, next);
                            }
                        }
                    }
                }
                canvas.restore();
            }
        }

        public void drawAll(Canvas canvas, int mode, int keyFrames, MotionController motionController) {
            if (mode == 4) {
                drawPathAsConfigured(canvas);
            }
            if (mode == 2) {
                drawPathRelative(canvas);
            }
            if (mode == 3) {
                drawPathCartesian(canvas);
            }
            drawBasicPath(canvas);
            drawTicks(canvas, mode, keyFrames, motionController);
        }

        /* access modifiers changed from: package-private */
        public void getTextBounds(String text, Paint paint) {
            paint.getTextBounds(text, 0, text.length(), this.mBounds);
        }
    }

    protected interface MotionTracker {
        void addMovement(MotionEvent motionEvent);

        void clear();

        void computeCurrentVelocity(int i);

        void computeCurrentVelocity(int i, float f);

        float getXVelocity();

        float getXVelocity(int i);

        float getYVelocity();

        float getYVelocity(int i);

        void recycle();
    }

    private static class MyTracker implements MotionTracker {
        private static MyTracker me = new MyTracker();
        VelocityTracker tracker;

        private MyTracker() {
        }

        public static MyTracker obtain() {
            me.tracker = VelocityTracker.obtain();
            return me;
        }

        public void addMovement(MotionEvent event) {
            VelocityTracker velocityTracker = this.tracker;
            if (velocityTracker != null) {
                velocityTracker.addMovement(event);
            }
        }

        public void clear() {
            VelocityTracker velocityTracker = this.tracker;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }

        public void computeCurrentVelocity(int units) {
            VelocityTracker velocityTracker = this.tracker;
            if (velocityTracker != null) {
                velocityTracker.computeCurrentVelocity(units);
            }
        }

        public void computeCurrentVelocity(int units, float maxVelocity) {
            VelocityTracker velocityTracker = this.tracker;
            if (velocityTracker != null) {
                velocityTracker.computeCurrentVelocity(units, maxVelocity);
            }
        }

        public float getXVelocity() {
            VelocityTracker velocityTracker = this.tracker;
            if (velocityTracker != null) {
                return velocityTracker.getXVelocity();
            }
            return 0.0f;
        }

        public float getXVelocity(int id) {
            VelocityTracker velocityTracker = this.tracker;
            if (velocityTracker != null) {
                return velocityTracker.getXVelocity(id);
            }
            return 0.0f;
        }

        public float getYVelocity() {
            VelocityTracker velocityTracker = this.tracker;
            if (velocityTracker != null) {
                return velocityTracker.getYVelocity();
            }
            return 0.0f;
        }

        public float getYVelocity(int id) {
            if (this.tracker != null) {
                return getYVelocity(id);
            }
            return 0.0f;
        }

        public void recycle() {
            VelocityTracker velocityTracker = this.tracker;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                this.tracker = null;
            }
        }
    }

    class StateCache {
        final String KeyEndState = "motion.EndState";
        final String KeyProgress = "motion.progress";
        final String KeyStartState = "motion.StartState";
        final String KeyVelocity = "motion.velocity";
        int endState = -1;
        float mProgress = Float.NaN;
        float mVelocity = Float.NaN;
        int startState = -1;

        StateCache() {
        }

        /* access modifiers changed from: package-private */
        public void apply() {
            int i = this.startState;
            if (!(i == -1 && this.endState == -1)) {
                if (i == -1) {
                    MotionLayout.this.transitionToState(this.endState);
                } else {
                    int i2 = this.endState;
                    if (i2 == -1) {
                        MotionLayout.this.setState(i, -1, -1);
                    } else {
                        MotionLayout.this.setTransition(i, i2);
                    }
                }
                MotionLayout.this.setState(TransitionState.SETUP);
            }
            if (!Float.isNaN(this.mVelocity)) {
                MotionLayout.this.setProgress(this.mProgress, this.mVelocity);
                this.mProgress = Float.NaN;
                this.mVelocity = Float.NaN;
                this.startState = -1;
                this.endState = -1;
            } else if (!Float.isNaN(this.mProgress)) {
                MotionLayout.this.setProgress(this.mProgress);
            }
        }

        public Bundle getTransitionState() {
            Bundle bundle = new Bundle();
            bundle.putFloat("motion.progress", this.mProgress);
            bundle.putFloat("motion.velocity", this.mVelocity);
            bundle.putInt("motion.StartState", this.startState);
            bundle.putInt("motion.EndState", this.endState);
            return bundle;
        }

        public void recordState() {
            this.endState = MotionLayout.this.mEndState;
            this.startState = MotionLayout.this.mBeginState;
            this.mVelocity = MotionLayout.this.getVelocity();
            this.mProgress = MotionLayout.this.getProgress();
        }

        public void setEndState(int endState2) {
            this.endState = endState2;
        }

        public void setProgress(float progress) {
            this.mProgress = progress;
        }

        public void setStartState(int startState2) {
            this.startState = startState2;
        }

        public void setTransitionState(Bundle bundle) {
            this.mProgress = bundle.getFloat("motion.progress");
            this.mVelocity = bundle.getFloat("motion.velocity");
            this.startState = bundle.getInt("motion.StartState");
            this.endState = bundle.getInt("motion.EndState");
        }

        public void setVelocity(float mVelocity2) {
            this.mVelocity = mVelocity2;
        }
    }

    public interface TransitionListener {
        void onTransitionChange(MotionLayout motionLayout, int i, int i2, float f);

        void onTransitionCompleted(MotionLayout motionLayout, int i);

        void onTransitionStarted(MotionLayout motionLayout, int i, int i2);

        void onTransitionTrigger(MotionLayout motionLayout, int i, boolean z, float f);
    }

    enum TransitionState {
        UNDEFINED,
        SETUP,
        MOVING,
        FINISHED
    }

    public MotionLayout(Context context) {
        super(context);
        init((AttributeSet) null);
    }

    public MotionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MotionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private boolean callTransformedTouchEvent(View view, MotionEvent event, float offsetX, float offsetY) {
        Matrix matrix = view.getMatrix();
        if (matrix.isIdentity()) {
            event.offsetLocation(offsetX, offsetY);
            boolean onTouchEvent = view.onTouchEvent(event);
            event.offsetLocation(-offsetX, -offsetY);
            return onTouchEvent;
        }
        MotionEvent obtain = MotionEvent.obtain(event);
        obtain.offsetLocation(offsetX, offsetY);
        if (this.mInverseMatrix == null) {
            this.mInverseMatrix = new Matrix();
        }
        matrix.invert(this.mInverseMatrix);
        obtain.transform(this.mInverseMatrix);
        boolean onTouchEvent2 = view.onTouchEvent(obtain);
        obtain.recycle();
        return onTouchEvent2;
    }

    private void checkStructure() {
        MotionScene motionScene = this.mScene;
        if (motionScene == null) {
            Log.e(TAG, "CHECK: motion scene not set! set \"app:layoutDescription=\"@xml/file\"");
            return;
        }
        int startId = motionScene.getStartId();
        MotionScene motionScene2 = this.mScene;
        checkStructure(startId, motionScene2.getConstraintSet(motionScene2.getStartId()));
        SparseIntArray sparseIntArray = new SparseIntArray();
        SparseIntArray sparseIntArray2 = new SparseIntArray();
        Iterator<MotionScene.Transition> it = this.mScene.getDefinedTransitions().iterator();
        while (it.hasNext()) {
            MotionScene.Transition next = it.next();
            if (next == this.mScene.mCurrentTransition) {
                Log.v(TAG, "CHECK: CURRENT");
            }
            checkStructure(next);
            int startConstraintSetId = next.getStartConstraintSetId();
            int endConstraintSetId = next.getEndConstraintSetId();
            String name = Debug.getName(getContext(), startConstraintSetId);
            Log1F380D.a((Object) name);
            String name2 = Debug.getName(getContext(), endConstraintSetId);
            Log1F380D.a((Object) name2);
            if (sparseIntArray.get(startConstraintSetId) == endConstraintSetId) {
                Log.e(TAG, "CHECK: two transitions with the same start and end " + name + "->" + name2);
            }
            if (sparseIntArray2.get(endConstraintSetId) == startConstraintSetId) {
                Log.e(TAG, "CHECK: you can't have reverse transitions" + name + "->" + name2);
            }
            sparseIntArray.put(startConstraintSetId, endConstraintSetId);
            sparseIntArray2.put(endConstraintSetId, startConstraintSetId);
            if (this.mScene.getConstraintSet(startConstraintSetId) == null) {
                Log.e(TAG, " no such constraintSetStart " + name);
            }
            if (this.mScene.getConstraintSet(endConstraintSetId) == null) {
                Log.e(TAG, " no such constraintSetEnd " + name);
            }
        }
    }

    private void checkStructure(MotionScene.Transition transition) {
        if (transition.getStartConstraintSetId() == transition.getEndConstraintSetId()) {
            Log.e(TAG, "CHECK: start and end constraint set should not be the same!");
        }
    }

    private void computeCurrentPositions() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            MotionController motionController = this.mFrameArrayList.get(childAt);
            if (motionController != null) {
                motionController.setStartCurrentState(childAt);
            }
        }
    }

    private void evaluateLayout() {
        float signum = Math.signum(this.mTransitionGoalPosition - this.mTransitionLastPosition);
        long nanoTime = getNanoTime();
        float f = 0.0f;
        Interpolator interpolator = this.mInterpolator;
        if (!(interpolator instanceof StopLogic)) {
            f = ((((float) (nanoTime - this.mTransitionLastTime)) * signum) * 1.0E-9f) / this.mTransitionDuration;
        }
        float f2 = this.mTransitionLastPosition + f;
        boolean z = false;
        if (this.mTransitionInstantly) {
            f2 = this.mTransitionGoalPosition;
        }
        if ((signum > 0.0f && f2 >= this.mTransitionGoalPosition) || (signum <= 0.0f && f2 <= this.mTransitionGoalPosition)) {
            f2 = this.mTransitionGoalPosition;
            z = true;
        }
        if (interpolator != null && !z) {
            f2 = this.mTemporalInterpolator ? interpolator.getInterpolation(((float) (nanoTime - this.mAnimationStartTime)) * 1.0E-9f) : interpolator.getInterpolation(f2);
        }
        if ((signum > 0.0f && f2 >= this.mTransitionGoalPosition) || (signum <= 0.0f && f2 <= this.mTransitionGoalPosition)) {
            f2 = this.mTransitionGoalPosition;
        }
        this.mPostInterpolationPosition = f2;
        int childCount = getChildCount();
        long nanoTime2 = getNanoTime();
        Interpolator interpolator2 = this.mProgressInterpolator;
        float interpolation = interpolator2 == null ? f2 : interpolator2.getInterpolation(f2);
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            MotionController motionController = this.mFrameArrayList.get(childAt);
            if (motionController != null) {
                View view = childAt;
                motionController.interpolate(childAt, interpolation, nanoTime2, this.mKeyCache);
            } else {
                View view2 = childAt;
            }
        }
        if (this.mMeasureDuringTransition) {
            requestLayout();
        }
    }

    private void fireTransitionChange() {
        CopyOnWriteArrayList<TransitionListener> copyOnWriteArrayList;
        if ((this.mTransitionListener != null || ((copyOnWriteArrayList = this.mTransitionListeners) != null && !copyOnWriteArrayList.isEmpty())) && this.mListenerPosition != this.mTransitionPosition) {
            if (this.mListenerState != -1) {
                TransitionListener transitionListener = this.mTransitionListener;
                if (transitionListener != null) {
                    transitionListener.onTransitionStarted(this, this.mBeginState, this.mEndState);
                }
                CopyOnWriteArrayList<TransitionListener> copyOnWriteArrayList2 = this.mTransitionListeners;
                if (copyOnWriteArrayList2 != null) {
                    Iterator<TransitionListener> it = copyOnWriteArrayList2.iterator();
                    while (it.hasNext()) {
                        it.next().onTransitionStarted(this, this.mBeginState, this.mEndState);
                    }
                }
                this.mIsAnimating = true;
            }
            this.mListenerState = -1;
            float f = this.mTransitionPosition;
            this.mListenerPosition = f;
            TransitionListener transitionListener2 = this.mTransitionListener;
            if (transitionListener2 != null) {
                transitionListener2.onTransitionChange(this, this.mBeginState, this.mEndState, f);
            }
            CopyOnWriteArrayList<TransitionListener> copyOnWriteArrayList3 = this.mTransitionListeners;
            if (copyOnWriteArrayList3 != null) {
                Iterator<TransitionListener> it2 = copyOnWriteArrayList3.iterator();
                while (it2.hasNext()) {
                    it2.next().onTransitionChange(this, this.mBeginState, this.mEndState, this.mTransitionPosition);
                }
            }
            this.mIsAnimating = true;
        }
    }

    private void fireTransitionStarted(MotionLayout motionLayout, int mBeginState2, int mEndState2) {
        TransitionListener transitionListener = this.mTransitionListener;
        if (transitionListener != null) {
            transitionListener.onTransitionStarted(this, mBeginState2, mEndState2);
        }
        CopyOnWriteArrayList<TransitionListener> copyOnWriteArrayList = this.mTransitionListeners;
        if (copyOnWriteArrayList != null) {
            Iterator<TransitionListener> it = copyOnWriteArrayList.iterator();
            while (it.hasNext()) {
                it.next().onTransitionStarted(motionLayout, mBeginState2, mEndState2);
            }
        }
    }

    private boolean handlesTouchEvent(float x, float y, View view, MotionEvent event) {
        boolean z = false;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount() - 1;
            while (true) {
                if (childCount < 0) {
                    break;
                }
                View childAt = viewGroup.getChildAt(childCount);
                if (handlesTouchEvent((((float) childAt.getLeft()) + x) - ((float) view.getScrollX()), (((float) childAt.getTop()) + y) - ((float) view.getScrollY()), childAt, event)) {
                    z = true;
                    break;
                }
                childCount--;
            }
        }
        if (z) {
            return z;
        }
        this.mBoundsCheck.set(x, y, (((float) view.getRight()) + x) - ((float) view.getLeft()), (((float) view.getBottom()) + y) - ((float) view.getTop()));
        if ((event.getAction() != 0 || this.mBoundsCheck.contains(event.getX(), event.getY())) && callTransformedTouchEvent(view, event, -x, -y)) {
            return true;
        }
        return z;
    }

    private void init(AttributeSet attrs) {
        MotionScene motionScene;
        IS_IN_EDIT_MODE = isInEditMode();
        if (attrs != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.MotionLayout);
            int indexCount = obtainStyledAttributes.getIndexCount();
            boolean z = true;
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.MotionLayout_layoutDescription) {
                    this.mScene = new MotionScene(getContext(), this, obtainStyledAttributes.getResourceId(index, -1));
                } else if (index == R.styleable.MotionLayout_currentState) {
                    this.mCurrentState = obtainStyledAttributes.getResourceId(index, -1);
                } else if (index == R.styleable.MotionLayout_motionProgress) {
                    this.mTransitionGoalPosition = obtainStyledAttributes.getFloat(index, 0.0f);
                    this.mInTransition = true;
                } else if (index == R.styleable.MotionLayout_applyMotionScene) {
                    z = obtainStyledAttributes.getBoolean(index, z);
                } else {
                    int i2 = 0;
                    if (index == R.styleable.MotionLayout_showPaths) {
                        if (this.mDebugPath == 0) {
                            if (obtainStyledAttributes.getBoolean(index, false)) {
                                i2 = 2;
                            }
                            this.mDebugPath = i2;
                        }
                    } else if (index == R.styleable.MotionLayout_motionDebug) {
                        this.mDebugPath = obtainStyledAttributes.getInt(index, 0);
                    }
                }
            }
            obtainStyledAttributes.recycle();
            if (this.mScene == null) {
                Log.e(TAG, "WARNING NO app:layoutDescription tag");
            }
            if (!z) {
                this.mScene = null;
            }
        }
        if (this.mDebugPath != 0) {
            checkStructure();
        }
        if (this.mCurrentState == -1 && (motionScene = this.mScene) != null) {
            this.mCurrentState = motionScene.getStartId();
            this.mBeginState = this.mScene.getStartId();
            this.mEndState = this.mScene.getEndId();
        }
    }

    private void processTransitionCompleted() {
        CopyOnWriteArrayList<TransitionListener> copyOnWriteArrayList;
        if (this.mTransitionListener != null || ((copyOnWriteArrayList = this.mTransitionListeners) != null && !copyOnWriteArrayList.isEmpty())) {
            this.mIsAnimating = false;
            Iterator<Integer> it = this.mTransitionCompleted.iterator();
            while (it.hasNext()) {
                Integer next = it.next();
                TransitionListener transitionListener = this.mTransitionListener;
                if (transitionListener != null) {
                    transitionListener.onTransitionCompleted(this, next.intValue());
                }
                CopyOnWriteArrayList<TransitionListener> copyOnWriteArrayList2 = this.mTransitionListeners;
                if (copyOnWriteArrayList2 != null) {
                    Iterator<TransitionListener> it2 = copyOnWriteArrayList2.iterator();
                    while (it2.hasNext()) {
                        it2.next().onTransitionCompleted(this, next.intValue());
                    }
                }
            }
            this.mTransitionCompleted.clear();
        }
    }

    /* access modifiers changed from: private */
    public void setupMotionViews() {
        int i;
        int i2;
        int i3;
        int i4;
        MotionLayout motionLayout = this;
        int childCount = getChildCount();
        motionLayout.mModel.build();
        boolean z = true;
        motionLayout.mInTransition = true;
        SparseArray sparseArray = new SparseArray();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = motionLayout.getChildAt(i5);
            sparseArray.put(childAt.getId(), motionLayout.mFrameArrayList.get(childAt));
        }
        int width = getWidth();
        int height = getHeight();
        int gatPathMotionArc = motionLayout.mScene.gatPathMotionArc();
        if (gatPathMotionArc != -1) {
            for (int i6 = 0; i6 < childCount; i6++) {
                MotionController motionController = motionLayout.mFrameArrayList.get(motionLayout.getChildAt(i6));
                if (motionController != null) {
                    motionController.setPathMotionArc(gatPathMotionArc);
                }
            }
        }
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
        int[] iArr = new int[motionLayout.mFrameArrayList.size()];
        int i7 = 0;
        for (int i8 = 0; i8 < childCount; i8++) {
            MotionController motionController2 = motionLayout.mFrameArrayList.get(motionLayout.getChildAt(i8));
            if (motionController2.getAnimateRelativeTo() != -1) {
                sparseBooleanArray.put(motionController2.getAnimateRelativeTo(), true);
                iArr[i7] = motionController2.getAnimateRelativeTo();
                i7++;
            }
        }
        if (motionLayout.mDecoratorsHelpers != null) {
            for (int i9 = 0; i9 < i7; i9++) {
                MotionController motionController3 = motionLayout.mFrameArrayList.get(motionLayout.findViewById(iArr[i9]));
                if (motionController3 != null) {
                    motionLayout.mScene.getKeyFrames(motionController3);
                }
            }
            Iterator<MotionHelper> it = motionLayout.mDecoratorsHelpers.iterator();
            while (it.hasNext()) {
                it.next().onPreSetup(motionLayout, motionLayout.mFrameArrayList);
            }
            int i10 = 0;
            while (i10 < i7) {
                MotionController motionController4 = motionLayout.mFrameArrayList.get(motionLayout.findViewById(iArr[i10]));
                if (motionController4 == null) {
                    i4 = i10;
                } else {
                    i4 = i10;
                    motionController4.setup(width, height, motionLayout.mTransitionDuration, getNanoTime());
                }
                i10 = i4 + 1;
            }
            int i11 = i10;
        } else {
            int i12 = 0;
            while (i12 < i7) {
                MotionController motionController5 = motionLayout.mFrameArrayList.get(motionLayout.findViewById(iArr[i12]));
                if (motionController5 == null) {
                    i3 = i12;
                } else {
                    motionLayout.mScene.getKeyFrames(motionController5);
                    i3 = i12;
                    MotionController motionController6 = motionController5;
                    motionController5.setup(width, height, motionLayout.mTransitionDuration, getNanoTime());
                }
                i12 = i3 + 1;
            }
            int i13 = i12;
        }
        int i14 = 0;
        while (i14 < childCount) {
            View childAt2 = motionLayout.getChildAt(i14);
            MotionController motionController7 = motionLayout.mFrameArrayList.get(childAt2);
            if (sparseBooleanArray.get(childAt2.getId())) {
                i2 = i14;
            } else if (motionController7 != null) {
                motionLayout.mScene.getKeyFrames(motionController7);
                MotionController motionController8 = motionController7;
                i2 = i14;
                View view = childAt2;
                motionController7.setup(width, height, motionLayout.mTransitionDuration, getNanoTime());
            } else {
                MotionController motionController9 = motionController7;
                i2 = i14;
                View view2 = childAt2;
            }
            i14 = i2 + 1;
        }
        int i15 = i14;
        float staggered = motionLayout.mScene.getStaggered();
        if (staggered != 0.0f) {
            if (((double) staggered) >= 0.0d) {
                z = false;
            }
            boolean z2 = false;
            float abs = Math.abs(staggered);
            float f = Float.MAX_VALUE;
            float f2 = -3.4028235E38f;
            int i16 = 0;
            while (true) {
                if (i16 >= childCount) {
                    SparseArray sparseArray2 = sparseArray;
                    break;
                }
                SparseArray sparseArray3 = sparseArray;
                MotionController motionController10 = motionLayout.mFrameArrayList.get(motionLayout.getChildAt(i16));
                if (!Float.isNaN(motionController10.mMotionStagger)) {
                    z2 = true;
                    break;
                }
                float finalX = motionController10.getFinalX();
                float finalY = motionController10.getFinalY();
                MotionController motionController11 = motionController10;
                float f3 = z ? finalY - finalX : finalY + finalX;
                f = Math.min(f, f3);
                f2 = Math.max(f2, f3);
                i16++;
                sparseArray = sparseArray3;
            }
            if (z2) {
                float f4 = Float.MAX_VALUE;
                float f5 = -3.4028235E38f;
                for (int i17 = 0; i17 < childCount; i17++) {
                    MotionController motionController12 = motionLayout.mFrameArrayList.get(motionLayout.getChildAt(i17));
                    if (!Float.isNaN(motionController12.mMotionStagger)) {
                        f4 = Math.min(f4, motionController12.mMotionStagger);
                        f5 = Math.max(f5, motionController12.mMotionStagger);
                    }
                }
                int i18 = 0;
                while (i18 < childCount) {
                    MotionController motionController13 = motionLayout.mFrameArrayList.get(motionLayout.getChildAt(i18));
                    if (!Float.isNaN(motionController13.mMotionStagger)) {
                        i = width;
                        motionController13.mStaggerScale = 1.0f / (1.0f - abs);
                        if (z) {
                            motionController13.mStaggerOffset = abs - (((f5 - motionController13.mMotionStagger) / (f5 - f4)) * abs);
                        } else {
                            motionController13.mStaggerOffset = abs - (((motionController13.mMotionStagger - f4) * abs) / (f5 - f4));
                        }
                    } else {
                        i = width;
                    }
                    i18++;
                    width = i;
                }
                int i19 = width;
                return;
            }
            int i20 = width;
            int i21 = 0;
            while (i21 < childCount) {
                MotionController motionController14 = motionLayout.mFrameArrayList.get(motionLayout.getChildAt(i21));
                float finalX2 = motionController14.getFinalX();
                float finalY2 = motionController14.getFinalY();
                float f6 = z ? finalY2 - finalX2 : finalY2 + finalX2;
                motionController14.mStaggerScale = 1.0f / (1.0f - abs);
                motionController14.mStaggerOffset = abs - (((f6 - f) * abs) / (f2 - f));
                i21++;
                motionLayout = this;
            }
            return;
        }
        SparseArray sparseArray4 = sparseArray;
        int i22 = width;
    }

    /* access modifiers changed from: private */
    public Rect toRect(ConstraintWidget cw) {
        this.mTempRect.top = cw.getY();
        this.mTempRect.left = cw.getX();
        this.mTempRect.right = cw.getWidth() + this.mTempRect.left;
        this.mTempRect.bottom = cw.getHeight() + this.mTempRect.top;
        return this.mTempRect;
    }

    private static boolean willJump(float velocity, float position, float maxAcceleration) {
        if (velocity > 0.0f) {
            float f = velocity / maxAcceleration;
            return position + ((velocity * f) - (((maxAcceleration * f) * f) / 2.0f)) > 1.0f;
        }
        float f2 = (-velocity) / maxAcceleration;
        return position + ((velocity * f2) + (((maxAcceleration * f2) * f2) / 2.0f)) < 0.0f;
    }

    public void addTransitionListener(TransitionListener listener) {
        if (this.mTransitionListeners == null) {
            this.mTransitionListeners = new CopyOnWriteArrayList<>();
        }
        this.mTransitionListeners.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void animateTo(float position) {
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            float f = this.mTransitionLastPosition;
            float f2 = this.mTransitionPosition;
            if (f != f2 && this.mTransitionInstantly) {
                this.mTransitionLastPosition = f2;
            }
            if (this.mTransitionLastPosition != position) {
                this.mTemporalInterpolator = false;
                float f3 = this.mTransitionLastPosition;
                this.mTransitionGoalPosition = position;
                this.mTransitionDuration = ((float) motionScene.getDuration()) / 1000.0f;
                setProgress(this.mTransitionGoalPosition);
                this.mInterpolator = null;
                this.mProgressInterpolator = this.mScene.getInterpolator();
                this.mTransitionInstantly = false;
                this.mAnimationStartTime = getNanoTime();
                this.mInTransition = true;
                this.mTransitionPosition = f3;
                this.mTransitionLastPosition = f3;
                invalidate();
            }
        }
    }

    public boolean applyViewTransition(int viewTransitionId, MotionController motionController) {
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            return motionScene.applyViewTransition(viewTransitionId, motionController);
        }
        return false;
    }

    public ConstraintSet cloneConstraintSet(int id) {
        MotionScene motionScene = this.mScene;
        if (motionScene == null) {
            return null;
        }
        ConstraintSet constraintSet = motionScene.getConstraintSet(id);
        ConstraintSet constraintSet2 = new ConstraintSet();
        constraintSet2.clone(constraintSet);
        return constraintSet2;
    }

    /* access modifiers changed from: package-private */
    public void disableAutoTransition(boolean disable) {
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            motionScene.disableAutoTransition(disable);
        }
    }

    public void enableTransition(int transitionID, boolean enable) {
        MotionScene.Transition transition = getTransition(transitionID);
        if (enable) {
            transition.setEnabled(true);
            return;
        }
        if (transition == this.mScene.mCurrentTransition) {
            Iterator<MotionScene.Transition> it = this.mScene.getTransitionsWithState(this.mCurrentState).iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                MotionScene.Transition next = it.next();
                if (next.isEnabled()) {
                    this.mScene.mCurrentTransition = next;
                    break;
                }
            }
        }
        transition.setEnabled(false);
    }

    public void enableViewTransition(int viewTransitionId, boolean enable) {
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            motionScene.enableViewTransition(viewTransitionId, enable);
        }
    }

    /* access modifiers changed from: package-private */
    public void endTrigger(boolean start) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            MotionController motionController = this.mFrameArrayList.get(getChildAt(i));
            if (motionController != null) {
                motionController.endTrigger(start);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void evaluate(boolean force) {
        boolean z;
        boolean z2;
        int i;
        int i2;
        boolean z3;
        if (this.mTransitionLastTime == -1) {
            this.mTransitionLastTime = getNanoTime();
        }
        float f = this.mTransitionLastPosition;
        if (f > 0.0f && f < 1.0f) {
            this.mCurrentState = -1;
        }
        boolean z4 = false;
        if (this.mKeepAnimating || (this.mInTransition && (force || this.mTransitionGoalPosition != f))) {
            float signum = Math.signum(this.mTransitionGoalPosition - f);
            long nanoTime = getNanoTime();
            float f2 = 0.0f;
            Interpolator interpolator = this.mInterpolator;
            if (!(interpolator instanceof MotionInterpolator)) {
                f2 = ((((float) (nanoTime - this.mTransitionLastTime)) * signum) * 1.0E-9f) / this.mTransitionDuration;
            }
            float f3 = this.mTransitionLastPosition + f2;
            boolean z5 = false;
            if (this.mTransitionInstantly) {
                f3 = this.mTransitionGoalPosition;
            }
            if ((signum > 0.0f && f3 >= this.mTransitionGoalPosition) || (signum <= 0.0f && f3 <= this.mTransitionGoalPosition)) {
                f3 = this.mTransitionGoalPosition;
                this.mInTransition = false;
                z5 = true;
            }
            this.mTransitionLastPosition = f3;
            this.mTransitionPosition = f3;
            this.mTransitionLastTime = nanoTime;
            if (interpolator == null || z5) {
                z = false;
                this.mLastVelocity = f2;
                z2 = false;
            } else if (this.mTemporalInterpolator) {
                z = false;
                float interpolation = interpolator.getInterpolation(((float) (nanoTime - this.mAnimationStartTime)) * 1.0E-9f);
                Interpolator interpolator2 = this.mInterpolator;
                StopLogic stopLogic = this.mStopLogic;
                boolean z6 = interpolator2 == stopLogic ? stopLogic.isStopped() ? true : true : false;
                this.mTransitionLastPosition = interpolation;
                this.mTransitionLastTime = nanoTime;
                Interpolator interpolator3 = this.mInterpolator;
                if (interpolator3 instanceof MotionInterpolator) {
                    float velocity = ((MotionInterpolator) interpolator3).getVelocity();
                    this.mLastVelocity = velocity;
                    if (Math.abs(velocity) * this.mTransitionDuration <= EPSILON && z6) {
                        this.mInTransition = false;
                    }
                    if (velocity > 0.0f && interpolation >= 1.0f) {
                        interpolation = 1.0f;
                        this.mTransitionLastPosition = 1.0f;
                        this.mInTransition = false;
                    }
                    if (velocity < 0.0f && interpolation <= 0.0f) {
                        interpolation = 0.0f;
                        this.mTransitionLastPosition = 0.0f;
                        this.mInTransition = false;
                    }
                }
                z2 = z6;
                f3 = interpolation;
            } else {
                z = false;
                float f4 = f3;
                f3 = interpolator.getInterpolation(f3);
                Interpolator interpolator4 = this.mInterpolator;
                if (interpolator4 instanceof MotionInterpolator) {
                    this.mLastVelocity = ((MotionInterpolator) interpolator4).getVelocity();
                } else {
                    this.mLastVelocity = ((interpolator4.getInterpolation(f4 + f2) - f3) * signum) / f2;
                }
                z2 = false;
            }
            if (Math.abs(this.mLastVelocity) > EPSILON) {
                setState(TransitionState.MOVING);
            }
            if (!z2) {
                if ((signum > 0.0f && f3 >= this.mTransitionGoalPosition) || (signum <= 0.0f && f3 <= this.mTransitionGoalPosition)) {
                    f3 = this.mTransitionGoalPosition;
                    this.mInTransition = false;
                }
                if (f3 >= 1.0f || f3 <= 0.0f) {
                    this.mInTransition = false;
                    setState(TransitionState.FINISHED);
                }
            }
            int childCount = getChildCount();
            this.mKeepAnimating = false;
            long nanoTime2 = getNanoTime();
            this.mPostInterpolationPosition = f3;
            Interpolator interpolator5 = this.mProgressInterpolator;
            float interpolation2 = interpolator5 == null ? f3 : interpolator5.getInterpolation(f3);
            Interpolator interpolator6 = this.mProgressInterpolator;
            if (interpolator6 != null) {
                float interpolation3 = interpolator6.getInterpolation((signum / this.mTransitionDuration) + f3);
                this.mLastVelocity = interpolation3;
                this.mLastVelocity = interpolation3 - this.mProgressInterpolator.getInterpolation(f3);
            }
            int i3 = 0;
            while (i3 < childCount) {
                View childAt = getChildAt(i3);
                MotionController motionController = this.mFrameArrayList.get(childAt);
                if (motionController != null) {
                    z3 = z2;
                    this.mKeepAnimating = motionController.interpolate(childAt, interpolation2, nanoTime2, this.mKeyCache) | this.mKeepAnimating;
                } else {
                    z3 = z2;
                }
                i3++;
                z2 = z3;
            }
            boolean z7 = z2;
            boolean z8 = (signum > 0.0f && f3 >= this.mTransitionGoalPosition) || (signum <= 0.0f && f3 <= this.mTransitionGoalPosition);
            if (!this.mKeepAnimating && !this.mInTransition && z8) {
                setState(TransitionState.FINISHED);
            }
            if (this.mMeasureDuringTransition) {
                requestLayout();
            }
            this.mKeepAnimating |= !z8;
            if (!(f3 > 0.0f || (i2 = this.mBeginState) == -1 || this.mCurrentState == i2)) {
                this.mCurrentState = i2;
                this.mScene.getConstraintSet(i2).applyCustomAttributes(this);
                setState(TransitionState.FINISHED);
                z = true;
            }
            long j = nanoTime;
            if (((double) f3) >= 1.0d && this.mCurrentState != (i = this.mEndState)) {
                this.mCurrentState = i;
                this.mScene.getConstraintSet(i).applyCustomAttributes(this);
                setState(TransitionState.FINISHED);
                z = true;
            }
            if (this.mKeepAnimating || this.mInTransition) {
                invalidate();
            } else if ((signum > 0.0f && f3 == 1.0f) || (signum < 0.0f && f3 == 0.0f)) {
                setState(TransitionState.FINISHED);
            }
            if (!this.mKeepAnimating && !this.mInTransition && ((signum > 0.0f && f3 == 1.0f) || (signum < 0.0f && f3 == 0.0f))) {
                onNewStateAttachHandlers();
            }
            z4 = z;
        }
        float f5 = this.mTransitionLastPosition;
        if (f5 >= 1.0f) {
            int i4 = this.mCurrentState;
            int i5 = this.mEndState;
            if (i4 != i5) {
                z4 = true;
            }
            this.mCurrentState = i5;
        } else if (f5 <= 0.0f) {
            int i6 = this.mCurrentState;
            int i7 = this.mBeginState;
            if (i6 != i7) {
                z4 = true;
            }
            this.mCurrentState = i7;
        }
        this.mNeedsFireTransitionCompleted |= z4;
        if (z4 && !this.mInLayout) {
            requestLayout();
        }
        this.mTransitionPosition = this.mTransitionLastPosition;
    }

    /* access modifiers changed from: protected */
    public void fireTransitionCompleted() {
        CopyOnWriteArrayList<TransitionListener> copyOnWriteArrayList;
        if ((this.mTransitionListener != null || ((copyOnWriteArrayList = this.mTransitionListeners) != null && !copyOnWriteArrayList.isEmpty())) && this.mListenerState == -1) {
            this.mListenerState = this.mCurrentState;
            int i = -1;
            if (!this.mTransitionCompleted.isEmpty()) {
                ArrayList<Integer> arrayList = this.mTransitionCompleted;
                i = arrayList.get(arrayList.size() - 1).intValue();
            }
            int i2 = this.mCurrentState;
            if (!(i == i2 || i2 == -1)) {
                this.mTransitionCompleted.add(Integer.valueOf(i2));
            }
        }
        processTransitionCompleted();
        Runnable runnable = this.mOnComplete;
        if (runnable != null) {
            runnable.run();
        }
        int[] iArr = this.mScheduledTransitionTo;
        if (iArr != null && this.mScheduledTransitions > 0) {
            transitionToState(iArr[0]);
            int[] iArr2 = this.mScheduledTransitionTo;
            System.arraycopy(iArr2, 1, iArr2, 0, iArr2.length - 1);
            this.mScheduledTransitions--;
        }
    }

    public void fireTrigger(int triggerId, boolean positive, float progress) {
        TransitionListener transitionListener = this.mTransitionListener;
        if (transitionListener != null) {
            transitionListener.onTransitionTrigger(this, triggerId, positive, progress);
        }
        CopyOnWriteArrayList<TransitionListener> copyOnWriteArrayList = this.mTransitionListeners;
        if (copyOnWriteArrayList != null) {
            Iterator<TransitionListener> it = copyOnWriteArrayList.iterator();
            while (it.hasNext()) {
                it.next().onTransitionTrigger(this, triggerId, positive, progress);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void getAnchorDpDt(int mTouchAnchorId, float pos, float locationX, float locationY, float[] mAnchorDpDt) {
        HashMap<View, MotionController> hashMap = this.mFrameArrayList;
        View viewById = getViewById(mTouchAnchorId);
        View view = viewById;
        MotionController motionController = hashMap.get(viewById);
        if (motionController != null) {
            motionController.getDpDt(pos, locationX, locationY, mAnchorDpDt);
            float y = view.getY();
            float f = pos - this.lastPos;
            float f2 = y - this.lastY;
            if (f != 0.0f) {
                float f3 = f2 / f;
            }
            this.lastPos = pos;
            this.lastY = y;
            return;
        }
        Log.w(TAG, "WARNING could not find view id " + (view == null ? HttpUrl.FRAGMENT_ENCODE_SET + mTouchAnchorId : view.getContext().getResources().getResourceName(mTouchAnchorId)));
    }

    public ConstraintSet getConstraintSet(int id) {
        MotionScene motionScene = this.mScene;
        if (motionScene == null) {
            return null;
        }
        return motionScene.getConstraintSet(id);
    }

    public int[] getConstraintSetIds() {
        MotionScene motionScene = this.mScene;
        if (motionScene == null) {
            return null;
        }
        return motionScene.getConstraintSetIds();
    }

    /* access modifiers changed from: package-private */
    public String getConstraintSetNames(int id) {
        MotionScene motionScene = this.mScene;
        if (motionScene == null) {
            return null;
        }
        return motionScene.lookUpConstraintName(id);
    }

    public int getCurrentState() {
        return this.mCurrentState;
    }

    public void getDebugMode(boolean showPaths) {
        this.mDebugPath = showPaths ? 2 : 1;
        invalidate();
    }

    public ArrayList<MotionScene.Transition> getDefinedTransitions() {
        MotionScene motionScene = this.mScene;
        if (motionScene == null) {
            return null;
        }
        return motionScene.getDefinedTransitions();
    }

    public DesignTool getDesignTool() {
        if (this.mDesignTool == null) {
            this.mDesignTool = new DesignTool(this);
        }
        return this.mDesignTool;
    }

    public int getEndState() {
        return this.mEndState;
    }

    /* access modifiers changed from: package-private */
    public MotionController getMotionController(int mTouchAnchorId) {
        return this.mFrameArrayList.get(findViewById(mTouchAnchorId));
    }

    /* access modifiers changed from: protected */
    public long getNanoTime() {
        return System.nanoTime();
    }

    public float getProgress() {
        return this.mTransitionLastPosition;
    }

    public MotionScene getScene() {
        return this.mScene;
    }

    public int getStartState() {
        return this.mBeginState;
    }

    public float getTargetPosition() {
        return this.mTransitionGoalPosition;
    }

    public MotionScene.Transition getTransition(int id) {
        return this.mScene.getTransitionById(id);
    }

    public Bundle getTransitionState() {
        if (this.mStateCache == null) {
            this.mStateCache = new StateCache();
        }
        this.mStateCache.recordState();
        return this.mStateCache.getTransitionState();
    }

    public long getTransitionTimeMs() {
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            this.mTransitionDuration = ((float) motionScene.getDuration()) / 1000.0f;
        }
        return (long) (this.mTransitionDuration * 1000.0f);
    }

    public float getVelocity() {
        return this.mLastVelocity;
    }

    public void getViewVelocity(View view, float posOnViewX, float posOnViewY, float[] returnVelocity, int type) {
        float f;
        float f2 = this.mLastVelocity;
        float f3 = this.mTransitionLastPosition;
        if (this.mInterpolator != null) {
            float signum = Math.signum(this.mTransitionGoalPosition - this.mTransitionLastPosition);
            float interpolation = this.mInterpolator.getInterpolation(this.mTransitionLastPosition + EPSILON);
            float interpolation2 = this.mInterpolator.getInterpolation(this.mTransitionLastPosition);
            f2 = (signum * ((interpolation - interpolation2) / EPSILON)) / this.mTransitionDuration;
            f = interpolation2;
        } else {
            f = f3;
        }
        Interpolator interpolator = this.mInterpolator;
        if (interpolator instanceof MotionInterpolator) {
            f2 = ((MotionInterpolator) interpolator).getVelocity();
        }
        MotionController motionController = this.mFrameArrayList.get(view);
        if ((type & 1) == 0) {
            motionController.getPostLayoutDvDp(f, view.getWidth(), view.getHeight(), posOnViewX, posOnViewY, returnVelocity);
        } else {
            motionController.getDpDt(f, posOnViewX, posOnViewY, returnVelocity);
        }
        if (type < 2) {
            returnVelocity[0] = returnVelocity[0] * f2;
            returnVelocity[1] = returnVelocity[1] * f2;
        }
    }

    public boolean isAttachedToWindow() {
        return Build.VERSION.SDK_INT >= 19 ? super.isAttachedToWindow() : getWindowToken() != null;
    }

    public boolean isDelayedApplicationOfInitialState() {
        return this.mDelayedApply;
    }

    public boolean isInRotation() {
        return this.mInRotation;
    }

    public boolean isInteractionEnabled() {
        return this.mInteractionEnabled;
    }

    public boolean isViewTransitionEnabled(int viewTransitionId) {
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            return motionScene.isViewTransitionEnabled(viewTransitionId);
        }
        return false;
    }

    public void jumpToState(int id) {
        if (!isAttachedToWindow()) {
            this.mCurrentState = id;
        }
        if (this.mBeginState == id) {
            setProgress(0.0f);
        } else if (this.mEndState == id) {
            setProgress(1.0f);
        } else {
            setTransition(id, id);
        }
    }

    public void loadLayoutDescription(int motionScene) {
        if (motionScene != 0) {
            try {
                MotionScene motionScene2 = new MotionScene(getContext(), this, motionScene);
                this.mScene = motionScene2;
                if (this.mCurrentState == -1) {
                    this.mCurrentState = motionScene2.getStartId();
                    this.mBeginState = this.mScene.getStartId();
                    this.mEndState = this.mScene.getEndId();
                }
                if (Build.VERSION.SDK_INT >= 19) {
                    if (!isAttachedToWindow()) {
                        this.mScene = null;
                        return;
                    }
                }
                if (Build.VERSION.SDK_INT >= 17) {
                    Display display = getDisplay();
                    this.mPreviouseRotation = display == null ? 0 : display.getRotation();
                }
                MotionScene motionScene3 = this.mScene;
                if (motionScene3 != null) {
                    ConstraintSet constraintSet = motionScene3.getConstraintSet(this.mCurrentState);
                    this.mScene.readFallback(this);
                    ArrayList<MotionHelper> arrayList = this.mDecoratorsHelpers;
                    if (arrayList != null) {
                        Iterator<MotionHelper> it = arrayList.iterator();
                        while (it.hasNext()) {
                            it.next().onFinishedMotionScene(this);
                        }
                    }
                    if (constraintSet != null) {
                        constraintSet.applyTo(this);
                    }
                    this.mBeginState = this.mCurrentState;
                }
                onNewStateAttachHandlers();
                StateCache stateCache = this.mStateCache;
                if (stateCache == null) {
                    MotionScene motionScene4 = this.mScene;
                    if (motionScene4 != null && motionScene4.mCurrentTransition != null && this.mScene.mCurrentTransition.getAutoTransition() == 4) {
                        transitionToEnd();
                        setState(TransitionState.SETUP);
                        setState(TransitionState.MOVING);
                    }
                } else if (this.mDelayedApply) {
                    post(new Runnable() {
                        public void run() {
                            MotionLayout.this.mStateCache.apply();
                        }
                    });
                } else {
                    stateCache.apply();
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("unable to parse MotionScene file", e);
            } catch (Exception e2) {
                throw new IllegalArgumentException("unable to parse MotionScene file", e2);
            }
        } else {
            this.mScene = null;
        }
    }

    /* access modifiers changed from: package-private */
    public int lookUpConstraintId(String id) {
        MotionScene motionScene = this.mScene;
        if (motionScene == null) {
            return 0;
        }
        return motionScene.lookUpConstraintId(id);
    }

    /* access modifiers changed from: protected */
    public MotionTracker obtainVelocityTracker() {
        return MyTracker.obtain();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        int i;
        Display display;
        super.onAttachedToWindow();
        if (Build.VERSION.SDK_INT >= 17 && (display = getDisplay()) != null) {
            this.mPreviouseRotation = display.getRotation();
        }
        MotionScene motionScene = this.mScene;
        if (!(motionScene == null || (i = this.mCurrentState) == -1)) {
            ConstraintSet constraintSet = motionScene.getConstraintSet(i);
            this.mScene.readFallback(this);
            ArrayList<MotionHelper> arrayList = this.mDecoratorsHelpers;
            if (arrayList != null) {
                Iterator<MotionHelper> it = arrayList.iterator();
                while (it.hasNext()) {
                    it.next().onFinishedMotionScene(this);
                }
            }
            if (constraintSet != null) {
                constraintSet.applyTo(this);
            }
            this.mBeginState = this.mCurrentState;
        }
        onNewStateAttachHandlers();
        StateCache stateCache = this.mStateCache;
        if (stateCache == null) {
            MotionScene motionScene2 = this.mScene;
            if (motionScene2 != null && motionScene2.mCurrentTransition != null && this.mScene.mCurrentTransition.getAutoTransition() == 4) {
                transitionToEnd();
                setState(TransitionState.SETUP);
                setState(TransitionState.MOVING);
            }
        } else if (this.mDelayedApply) {
            post(new Runnable() {
                public void run() {
                    MotionLayout.this.mStateCache.apply();
                }
            });
        } else {
            stateCache.apply();
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        TouchResponse touchResponse;
        int touchRegionId;
        RectF touchRegion;
        MotionScene motionScene = this.mScene;
        if (motionScene == null || !this.mInteractionEnabled) {
            return false;
        }
        if (motionScene.mViewTransitionController != null) {
            this.mScene.mViewTransitionController.touchEvent(event);
        }
        MotionScene.Transition transition = this.mScene.mCurrentTransition;
        if (transition != null && transition.isEnabled() && (touchResponse = transition.getTouchResponse()) != null && ((event.getAction() != 0 || (touchRegion = touchResponse.getTouchRegion(this, new RectF())) == null || touchRegion.contains(event.getX(), event.getY())) && (touchRegionId = touchResponse.getTouchRegionId()) != -1)) {
            View view = this.mRegionView;
            if (view == null || view.getId() != touchRegionId) {
                this.mRegionView = findViewById(touchRegionId);
            }
            View view2 = this.mRegionView;
            if (view2 != null) {
                this.mBoundsCheck.set((float) view2.getLeft(), (float) this.mRegionView.getTop(), (float) this.mRegionView.getRight(), (float) this.mRegionView.getBottom());
                if (this.mBoundsCheck.contains(event.getX(), event.getY()) && !handlesTouchEvent((float) this.mRegionView.getLeft(), (float) this.mRegionView.getTop(), this.mRegionView, event)) {
                    return onTouchEvent(event);
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.mInLayout = true;
        try {
            if (this.mScene == null) {
                super.onLayout(changed, left, top, right, bottom);
                return;
            }
            int i = right - left;
            int i2 = bottom - top;
            if (!(this.mLastLayoutWidth == i && this.mLastLayoutHeight == i2)) {
                rebuildScene();
                evaluate(true);
            }
            this.mLastLayoutWidth = i;
            this.mLastLayoutHeight = i2;
            this.mOldWidth = i;
            this.mOldHeight = i2;
            this.mInLayout = false;
        } finally {
            this.mInLayout = false;
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mScene == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        boolean z = (this.mLastWidthMeasureSpec == widthMeasureSpec && this.mLastHeightMeasureSpec == heightMeasureSpec) ? false : true;
        if (this.mNeedsFireTransitionCompleted) {
            this.mNeedsFireTransitionCompleted = false;
            onNewStateAttachHandlers();
            processTransitionCompleted();
            z = true;
        }
        if (this.mDirtyHierarchy) {
            z = true;
        }
        this.mLastWidthMeasureSpec = widthMeasureSpec;
        this.mLastHeightMeasureSpec = heightMeasureSpec;
        int startId = this.mScene.getStartId();
        int endId = this.mScene.getEndId();
        boolean z2 = true;
        if ((z || this.mModel.isNotConfiguredWith(startId, endId)) && this.mBeginState != -1) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(startId), this.mScene.getConstraintSet(endId));
            this.mModel.reEvaluateState();
            this.mModel.setMeasuredId(startId, endId);
            z2 = false;
        } else if (z) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        if (this.mMeasureDuringTransition || z2) {
            int paddingTop = getPaddingTop() + getPaddingBottom();
            int width = this.mLayoutWidget.getWidth() + getPaddingLeft() + getPaddingRight();
            int height = this.mLayoutWidget.getHeight() + paddingTop;
            int i = this.mWidthMeasureMode;
            if (i == Integer.MIN_VALUE || i == 0) {
                int i2 = this.mStartWrapWidth;
                width = (int) (((float) i2) + (this.mPostInterpolationPosition * ((float) (this.mEndWrapWidth - i2))));
                requestLayout();
            }
            int i3 = this.mHeightMeasureMode;
            if (i3 == Integer.MIN_VALUE || i3 == 0) {
                int i4 = this.mStartWrapHeight;
                height = (int) (((float) i4) + (this.mPostInterpolationPosition * ((float) (this.mEndWrapHeight - i4))));
                requestLayout();
            }
            setMeasuredDimension(width, height);
        }
        evaluateLayout();
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed, int type) {
        MotionScene.Transition transition;
        TouchResponse touchResponse;
        int touchRegionId;
        final View view = target;
        int i = dx;
        int i2 = dy;
        MotionScene motionScene = this.mScene;
        if (motionScene != null && (transition = motionScene.mCurrentTransition) != null && transition.isEnabled()) {
            if (!transition.isEnabled() || (touchResponse = transition.getTouchResponse()) == null || (touchRegionId = touchResponse.getTouchRegionId()) == -1 || target.getId() == touchRegionId) {
                if (motionScene.getMoveWhenScrollAtTop()) {
                    TouchResponse touchResponse2 = transition.getTouchResponse();
                    int i3 = -1;
                    if (!(touchResponse2 == null || (touchResponse2.getFlags() & 4) == 0)) {
                        i3 = dy;
                    }
                    float f = this.mTransitionPosition;
                    if ((f == 1.0f || f == 0.0f) && view.canScrollVertically(i3)) {
                        return;
                    }
                }
                if (!(transition.getTouchResponse() == null || (transition.getTouchResponse().getFlags() & 1) == 0)) {
                    float progressDirection = motionScene.getProgressDirection((float) i, (float) i2);
                    float f2 = this.mTransitionLastPosition;
                    if ((f2 <= 0.0f && progressDirection < 0.0f) || (f2 >= 1.0f && progressDirection > 0.0f)) {
                        if (Build.VERSION.SDK_INT >= 21) {
                            view.setNestedScrollingEnabled(false);
                            view.post(new Runnable(this) {
                                public void run() {
                                    view.setNestedScrollingEnabled(true);
                                }
                            });
                            return;
                        }
                        return;
                    }
                }
                float f3 = this.mTransitionPosition;
                long nanoTime = getNanoTime();
                this.mScrollTargetDX = (float) i;
                this.mScrollTargetDY = (float) i2;
                this.mScrollTargetDT = (float) (((double) (nanoTime - this.mScrollTargetTime)) * 1.0E-9d);
                this.mScrollTargetTime = nanoTime;
                motionScene.processScrollMove((float) i, (float) i2);
                if (f3 != this.mTransitionPosition) {
                    consumed[0] = i;
                    consumed[1] = i2;
                }
                evaluate(false);
                if (consumed[0] != 0 || consumed[1] != 0) {
                    this.mUndergoingMotion = true;
                }
            }
        }
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, int[] consumed) {
        if (!(!this.mUndergoingMotion && dxConsumed == 0 && dyConsumed == 0)) {
            consumed[0] = consumed[0] + dxUnconsumed;
            consumed[1] = consumed[1] + dyUnconsumed;
        }
        this.mUndergoingMotion = false;
    }

    public void onNestedScrollAccepted(View child, View target, int axes, int type) {
        this.mScrollTargetTime = getNanoTime();
        this.mScrollTargetDT = 0.0f;
        this.mScrollTargetDX = 0.0f;
        this.mScrollTargetDY = 0.0f;
    }

    /* access modifiers changed from: package-private */
    public void onNewStateAttachHandlers() {
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            if (motionScene.autoTransition(this, this.mCurrentState)) {
                requestLayout();
                return;
            }
            int i = this.mCurrentState;
            if (i != -1) {
                this.mScene.addOnClickListeners(this, i);
            }
            if (this.mScene.supportTouch()) {
                this.mScene.setupTouch();
            }
        }
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            motionScene.setRtl(isRtl());
        }
    }

    public boolean onStartNestedScroll(View child, View target, int axes, int type) {
        MotionScene motionScene = this.mScene;
        return (motionScene == null || motionScene.mCurrentTransition == null || this.mScene.mCurrentTransition.getTouchResponse() == null || (this.mScene.mCurrentTransition.getTouchResponse().getFlags() & 2) != 0) ? false : true;
    }

    public void onStopNestedScroll(View target, int type) {
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            float f = this.mScrollTargetDT;
            if (f != 0.0f) {
                motionScene.processScrollUp(this.mScrollTargetDX / f, this.mScrollTargetDY / f);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        MotionScene motionScene = this.mScene;
        if (motionScene == null || !this.mInteractionEnabled || !motionScene.supportTouch()) {
            return super.onTouchEvent(event);
        }
        MotionScene.Transition transition = this.mScene.mCurrentTransition;
        if (transition != null && !transition.isEnabled()) {
            return super.onTouchEvent(event);
        }
        this.mScene.processTouchEvent(event, getCurrentState(), this);
        if (this.mScene.mCurrentTransition.isTransitionFlag(4)) {
            return this.mScene.mCurrentTransition.getTouchResponse().isDragStarted();
        }
        return true;
    }

    public void onViewAdded(View view) {
        super.onViewAdded(view);
        if (view instanceof MotionHelper) {
            MotionHelper motionHelper = (MotionHelper) view;
            if (this.mTransitionListeners == null) {
                this.mTransitionListeners = new CopyOnWriteArrayList<>();
            }
            this.mTransitionListeners.add(motionHelper);
            if (motionHelper.isUsedOnShow()) {
                if (this.mOnShowHelpers == null) {
                    this.mOnShowHelpers = new ArrayList<>();
                }
                this.mOnShowHelpers.add(motionHelper);
            }
            if (motionHelper.isUseOnHide()) {
                if (this.mOnHideHelpers == null) {
                    this.mOnHideHelpers = new ArrayList<>();
                }
                this.mOnHideHelpers.add(motionHelper);
            }
            if (motionHelper.isDecorator()) {
                if (this.mDecoratorsHelpers == null) {
                    this.mDecoratorsHelpers = new ArrayList<>();
                }
                this.mDecoratorsHelpers.add(motionHelper);
            }
        }
    }

    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        ArrayList<MotionHelper> arrayList = this.mOnShowHelpers;
        if (arrayList != null) {
            arrayList.remove(view);
        }
        ArrayList<MotionHelper> arrayList2 = this.mOnHideHelpers;
        if (arrayList2 != null) {
            arrayList2.remove(view);
        }
    }

    /* access modifiers changed from: protected */
    public void parseLayoutDescription(int id) {
        this.mConstraintLayoutSpec = null;
    }

    @Deprecated
    public void rebuildMotion() {
        Log.e(TAG, "This method is deprecated. Please call rebuildScene() instead.");
        rebuildScene();
    }

    public void rebuildScene() {
        this.mModel.reEvaluateState();
        invalidate();
    }

    public boolean removeTransitionListener(TransitionListener listener) {
        CopyOnWriteArrayList<TransitionListener> copyOnWriteArrayList = this.mTransitionListeners;
        if (copyOnWriteArrayList == null) {
            return false;
        }
        return copyOnWriteArrayList.remove(listener);
    }

    public void requestLayout() {
        MotionScene motionScene;
        if (!this.mMeasureDuringTransition && this.mCurrentState == -1 && (motionScene = this.mScene) != null && motionScene.mCurrentTransition != null) {
            int layoutDuringTransition = this.mScene.mCurrentTransition.getLayoutDuringTransition();
            if (layoutDuringTransition != 0) {
                if (layoutDuringTransition == 2) {
                    int childCount = getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        this.mFrameArrayList.get(getChildAt(i)).remeasure();
                    }
                    return;
                }
            } else {
                return;
            }
        }
        super.requestLayout();
    }

    public void rotateTo(int id, int duration) {
        int i = 1;
        this.mInRotation = true;
        this.mPreRotateWidth = getWidth();
        this.mPreRotateHeight = getHeight();
        int rotation = getDisplay().getRotation();
        if ((rotation + 1) % 4 <= (this.mPreviouseRotation + 1) % 4) {
            i = 2;
        }
        this.mRotatMode = i;
        this.mPreviouseRotation = rotation;
        int childCount = getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            ViewState viewState = this.mPreRotate.get(childAt);
            if (viewState == null) {
                viewState = new ViewState();
                this.mPreRotate.put(childAt, viewState);
            }
            viewState.getState(childAt);
        }
        this.mBeginState = -1;
        this.mEndState = id;
        this.mScene.setTransition(-1, id);
        this.mModel.initFrom(this.mLayoutWidget, (ConstraintSet) null, this.mScene.getConstraintSet(this.mEndState));
        this.mTransitionPosition = 0.0f;
        this.mTransitionLastPosition = 0.0f;
        invalidate();
        transitionToEnd(new Runnable() {
            public void run() {
                boolean unused = MotionLayout.this.mInRotation = false;
            }
        });
        if (duration > 0) {
            this.mTransitionDuration = ((float) duration) / 1000.0f;
        }
    }

    public void scheduleTransitionTo(int id) {
        if (getCurrentState() == -1) {
            transitionToState(id);
            return;
        }
        int[] iArr = this.mScheduledTransitionTo;
        if (iArr == null) {
            this.mScheduledTransitionTo = new int[4];
        } else if (iArr.length <= this.mScheduledTransitions) {
            this.mScheduledTransitionTo = Arrays.copyOf(iArr, iArr.length * 2);
        }
        int[] iArr2 = this.mScheduledTransitionTo;
        int i = this.mScheduledTransitions;
        this.mScheduledTransitions = i + 1;
        iArr2[i] = id;
    }

    public void setDebugMode(int debugMode) {
        this.mDebugPath = debugMode;
        invalidate();
    }

    public void setDelayedApplicationOfInitialState(boolean delayedApply) {
        this.mDelayedApply = delayedApply;
    }

    public void setInteractionEnabled(boolean enabled) {
        this.mInteractionEnabled = enabled;
    }

    public void setInterpolatedProgress(float pos) {
        if (this.mScene != null) {
            setState(TransitionState.MOVING);
            Interpolator interpolator = this.mScene.getInterpolator();
            if (interpolator != null) {
                setProgress(interpolator.getInterpolation(pos));
                return;
            }
        }
        setProgress(pos);
    }

    public void setOnHide(float progress) {
        ArrayList<MotionHelper> arrayList = this.mOnHideHelpers;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                this.mOnHideHelpers.get(i).setProgress(progress);
            }
        }
    }

    public void setOnShow(float progress) {
        ArrayList<MotionHelper> arrayList = this.mOnShowHelpers;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                this.mOnShowHelpers.get(i).setProgress(progress);
            }
        }
    }

    public void setProgress(float pos) {
        if (pos < 0.0f || pos > 1.0f) {
            Log.w(TAG, "Warning! Progress is defined for values between 0.0 and 1.0 inclusive");
        }
        if (!isAttachedToWindow()) {
            if (this.mStateCache == null) {
                this.mStateCache = new StateCache();
            }
            this.mStateCache.setProgress(pos);
            return;
        }
        if (pos <= 0.0f) {
            if (this.mTransitionLastPosition == 1.0f && this.mCurrentState == this.mEndState) {
                setState(TransitionState.MOVING);
            }
            this.mCurrentState = this.mBeginState;
            if (this.mTransitionLastPosition == 0.0f) {
                setState(TransitionState.FINISHED);
            }
        } else if (pos >= 1.0f) {
            if (this.mTransitionLastPosition == 0.0f && this.mCurrentState == this.mBeginState) {
                setState(TransitionState.MOVING);
            }
            this.mCurrentState = this.mEndState;
            if (this.mTransitionLastPosition == 1.0f) {
                setState(TransitionState.FINISHED);
            }
        } else {
            this.mCurrentState = -1;
            setState(TransitionState.MOVING);
        }
        if (this.mScene != null) {
            this.mTransitionInstantly = true;
            this.mTransitionGoalPosition = pos;
            this.mTransitionPosition = pos;
            this.mTransitionLastTime = -1;
            this.mAnimationStartTime = -1;
            this.mInterpolator = null;
            this.mInTransition = true;
            invalidate();
        }
    }

    public void setProgress(float pos, float velocity) {
        if (!isAttachedToWindow()) {
            if (this.mStateCache == null) {
                this.mStateCache = new StateCache();
            }
            this.mStateCache.setProgress(pos);
            this.mStateCache.setVelocity(velocity);
            return;
        }
        setProgress(pos);
        setState(TransitionState.MOVING);
        this.mLastVelocity = velocity;
        float f = 0.0f;
        if (velocity != 0.0f) {
            if (velocity > 0.0f) {
                f = 1.0f;
            }
            animateTo(f);
        } else if (pos != 0.0f && pos != 1.0f) {
            if (pos > 0.5f) {
                f = 1.0f;
            }
            animateTo(f);
        }
    }

    public void setScene(MotionScene scene) {
        this.mScene = scene;
        scene.setRtl(isRtl());
        rebuildScene();
    }

    /* access modifiers changed from: package-private */
    public void setStartState(int beginId) {
        if (!isAttachedToWindow()) {
            if (this.mStateCache == null) {
                this.mStateCache = new StateCache();
            }
            this.mStateCache.setStartState(beginId);
            this.mStateCache.setEndState(beginId);
            return;
        }
        this.mCurrentState = beginId;
    }

    public void setState(int id, int screenWidth, int screenHeight) {
        setState(TransitionState.SETUP);
        this.mCurrentState = id;
        this.mBeginState = -1;
        this.mEndState = -1;
        if (this.mConstraintLayoutSpec != null) {
            this.mConstraintLayoutSpec.updateConstraints(id, (float) screenWidth, (float) screenHeight);
            return;
        }
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            motionScene.getConstraintSet(id).applyTo(this);
        }
    }

    /* access modifiers changed from: package-private */
    public void setState(TransitionState newState) {
        if (newState != TransitionState.FINISHED || this.mCurrentState != -1) {
            TransitionState transitionState = this.mTransitionState;
            this.mTransitionState = newState;
            if (transitionState == TransitionState.MOVING && newState == TransitionState.MOVING) {
                fireTransitionChange();
            }
            switch (AnonymousClass5.$SwitchMap$androidx$constraintlayout$motion$widget$MotionLayout$TransitionState[transitionState.ordinal()]) {
                case 1:
                case 2:
                    if (newState == TransitionState.MOVING) {
                        fireTransitionChange();
                    }
                    if (newState == TransitionState.FINISHED) {
                        fireTransitionCompleted();
                        return;
                    }
                    return;
                case 3:
                    if (newState == TransitionState.FINISHED) {
                        fireTransitionCompleted();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void setTransition(int beginId, int endId) {
        if (!isAttachedToWindow()) {
            if (this.mStateCache == null) {
                this.mStateCache = new StateCache();
            }
            this.mStateCache.setStartState(beginId);
            this.mStateCache.setEndState(endId);
            return;
        }
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            this.mBeginState = beginId;
            this.mEndState = endId;
            motionScene.setTransition(beginId, endId);
            this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(beginId), this.mScene.getConstraintSet(endId));
            rebuildScene();
            this.mTransitionLastPosition = 0.0f;
            transitionToStart();
        }
    }

    /* access modifiers changed from: protected */
    public void setTransition(MotionScene.Transition transition) {
        this.mScene.setTransition(transition);
        setState(TransitionState.SETUP);
        if (this.mCurrentState == this.mScene.getEndId()) {
            this.mTransitionLastPosition = 1.0f;
            this.mTransitionPosition = 1.0f;
            this.mTransitionGoalPosition = 1.0f;
        } else {
            this.mTransitionLastPosition = 0.0f;
            this.mTransitionPosition = 0.0f;
            this.mTransitionGoalPosition = 0.0f;
        }
        this.mTransitionLastTime = transition.isTransitionFlag(1) ? -1 : getNanoTime();
        int startId = this.mScene.getStartId();
        int endId = this.mScene.getEndId();
        if (startId != this.mBeginState || endId != this.mEndState) {
            this.mBeginState = startId;
            this.mEndState = endId;
            this.mScene.setTransition(startId, endId);
            this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(this.mBeginState), this.mScene.getConstraintSet(this.mEndState));
            this.mModel.setMeasuredId(this.mBeginState, this.mEndState);
            this.mModel.reEvaluateState();
            rebuildScene();
        }
    }

    public void setTransitionDuration(int milliseconds) {
        MotionScene motionScene = this.mScene;
        if (motionScene == null) {
            Log.e(TAG, "MotionScene not defined");
        } else {
            motionScene.setDuration(milliseconds);
        }
    }

    public void setTransitionListener(TransitionListener listener) {
        this.mTransitionListener = listener;
    }

    public void setTransitionState(Bundle bundle) {
        if (this.mStateCache == null) {
            this.mStateCache = new StateCache();
        }
        this.mStateCache.setTransitionState(bundle);
        if (isAttachedToWindow()) {
            this.mStateCache.apply();
        }
    }

    public void touchAnimateTo(int touchUpMode, float position, float currentVelocity) {
        if (this.mScene != null && this.mTransitionLastPosition != position) {
            this.mTemporalInterpolator = true;
            this.mAnimationStartTime = getNanoTime();
            this.mTransitionDuration = ((float) this.mScene.getDuration()) / 1000.0f;
            this.mTransitionGoalPosition = position;
            this.mInTransition = true;
            switch (touchUpMode) {
                case 0:
                case 1:
                case 2:
                case 6:
                case 7:
                    if (touchUpMode == 1 || touchUpMode == 7) {
                        position = 0.0f;
                    } else if (touchUpMode == 2 || touchUpMode == 6) {
                        position = 1.0f;
                    }
                    if (this.mScene.getAutoCompleteMode() == 0) {
                        this.mStopLogic.config(this.mTransitionLastPosition, position, currentVelocity, this.mTransitionDuration, this.mScene.getMaxAcceleration(), this.mScene.getMaxVelocity());
                    } else {
                        this.mStopLogic.springConfig(this.mTransitionLastPosition, position, currentVelocity, this.mScene.getSpringMass(), this.mScene.getSpringStiffiness(), this.mScene.getSpringDamping(), this.mScene.getSpringStopThreshold(), this.mScene.getSpringBoundary());
                    }
                    int i = this.mCurrentState;
                    this.mTransitionGoalPosition = position;
                    this.mCurrentState = i;
                    this.mInterpolator = this.mStopLogic;
                    break;
                case 4:
                    this.mDecelerateLogic.config(currentVelocity, this.mTransitionLastPosition, this.mScene.getMaxAcceleration());
                    this.mInterpolator = this.mDecelerateLogic;
                    break;
                case 5:
                    if (!willJump(currentVelocity, this.mTransitionLastPosition, this.mScene.getMaxAcceleration())) {
                        this.mStopLogic.config(this.mTransitionLastPosition, position, currentVelocity, this.mTransitionDuration, this.mScene.getMaxAcceleration(), this.mScene.getMaxVelocity());
                        this.mLastVelocity = 0.0f;
                        int i2 = this.mCurrentState;
                        this.mTransitionGoalPosition = position;
                        this.mCurrentState = i2;
                        this.mInterpolator = this.mStopLogic;
                        break;
                    } else {
                        this.mDecelerateLogic.config(currentVelocity, this.mTransitionLastPosition, this.mScene.getMaxAcceleration());
                        this.mInterpolator = this.mDecelerateLogic;
                        break;
                    }
            }
            this.mTransitionInstantly = false;
            this.mAnimationStartTime = getNanoTime();
            invalidate();
        }
    }

    public void touchSpringTo(float position, float currentVelocity) {
        if (this.mScene != null && this.mTransitionLastPosition != position) {
            this.mTemporalInterpolator = true;
            this.mAnimationStartTime = getNanoTime();
            this.mTransitionDuration = ((float) this.mScene.getDuration()) / 1000.0f;
            this.mTransitionGoalPosition = position;
            this.mInTransition = true;
            this.mStopLogic.springConfig(this.mTransitionLastPosition, position, currentVelocity, this.mScene.getSpringMass(), this.mScene.getSpringStiffiness(), this.mScene.getSpringDamping(), this.mScene.getSpringStopThreshold(), this.mScene.getSpringBoundary());
            int i = this.mCurrentState;
            this.mTransitionGoalPosition = position;
            this.mCurrentState = i;
            this.mInterpolator = this.mStopLogic;
            this.mTransitionInstantly = false;
            this.mAnimationStartTime = getNanoTime();
            invalidate();
        }
    }

    public void transitionToEnd() {
        animateTo(1.0f);
        this.mOnComplete = null;
    }

    public void transitionToEnd(Runnable onComplete) {
        animateTo(1.0f);
        this.mOnComplete = onComplete;
    }

    public void transitionToStart() {
        animateTo(0.0f);
    }

    public void transitionToState(int id) {
        if (!isAttachedToWindow()) {
            if (this.mStateCache == null) {
                this.mStateCache = new StateCache();
            }
            this.mStateCache.setEndState(id);
            return;
        }
        transitionToState(id, -1, -1);
    }

    public void transitionToState(int id, int duration) {
        if (!isAttachedToWindow()) {
            if (this.mStateCache == null) {
                this.mStateCache = new StateCache();
            }
            this.mStateCache.setEndState(id);
            return;
        }
        transitionToState(id, -1, -1, duration);
    }

    public void transitionToState(int id, int screenWidth, int screenHeight) {
        transitionToState(id, screenWidth, screenHeight, -1);
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x002e A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x002f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void transitionToState(int r23, int r24, int r25, int r26) {
        /*
            r22 = this;
            r0 = r22
            r1 = r26
            androidx.constraintlayout.motion.widget.MotionScene r2 = r0.mScene
            r3 = -1
            if (r2 == 0) goto L_0x0023
            androidx.constraintlayout.widget.StateSet r2 = r2.mStateSet
            if (r2 == 0) goto L_0x0023
            androidx.constraintlayout.motion.widget.MotionScene r2 = r0.mScene
            androidx.constraintlayout.widget.StateSet r2 = r2.mStateSet
            int r4 = r0.mCurrentState
            r5 = r24
            float r6 = (float) r5
            r7 = r25
            float r8 = (float) r7
            r9 = r23
            int r2 = r2.convertToConstraintSet(r4, r9, r6, r8)
            if (r2 == r3) goto L_0x0029
            r4 = r2
            goto L_0x002a
        L_0x0023:
            r9 = r23
            r5 = r24
            r7 = r25
        L_0x0029:
            r4 = r9
        L_0x002a:
            int r2 = r0.mCurrentState
            if (r2 != r4) goto L_0x002f
            return
        L_0x002f:
            int r6 = r0.mBeginState
            r8 = 1148846080(0x447a0000, float:1000.0)
            r9 = 0
            if (r6 != r4) goto L_0x0040
            r0.animateTo(r9)
            if (r1 <= 0) goto L_0x003f
            float r2 = (float) r1
            float r2 = r2 / r8
            r0.mTransitionDuration = r2
        L_0x003f:
            return
        L_0x0040:
            int r6 = r0.mEndState
            r10 = 1065353216(0x3f800000, float:1.0)
            if (r6 != r4) goto L_0x0050
            r0.animateTo(r10)
            if (r1 <= 0) goto L_0x004f
            float r2 = (float) r1
            float r2 = r2 / r8
            r0.mTransitionDuration = r2
        L_0x004f:
            return
        L_0x0050:
            r0.mEndState = r4
            if (r2 == r3) goto L_0x0066
            r0.setTransition(r2, r4)
            r0.animateTo(r10)
            r0.mTransitionLastPosition = r9
            r22.transitionToEnd()
            if (r1 <= 0) goto L_0x0065
            float r2 = (float) r1
            float r2 = r2 / r8
            r0.mTransitionDuration = r2
        L_0x0065:
            return
        L_0x0066:
            r2 = 0
            r0.mTemporalInterpolator = r2
            r0.mTransitionGoalPosition = r10
            r0.mTransitionPosition = r9
            r0.mTransitionLastPosition = r9
            long r11 = r22.getNanoTime()
            r0.mTransitionLastTime = r11
            long r11 = r22.getNanoTime()
            r0.mAnimationStartTime = r11
            r0.mTransitionInstantly = r2
            r2 = 0
            r0.mInterpolator = r2
            if (r1 != r3) goto L_0x008c
            androidx.constraintlayout.motion.widget.MotionScene r6 = r0.mScene
            int r6 = r6.getDuration()
            float r6 = (float) r6
            float r6 = r6 / r8
            r0.mTransitionDuration = r6
        L_0x008c:
            r0.mBeginState = r3
            androidx.constraintlayout.motion.widget.MotionScene r6 = r0.mScene
            int r11 = r0.mEndState
            r6.setTransition(r3, r11)
            android.util.SparseArray r3 = new android.util.SparseArray
            r3.<init>()
            if (r1 != 0) goto L_0x00a7
            androidx.constraintlayout.motion.widget.MotionScene r6 = r0.mScene
            int r6 = r6.getDuration()
            float r6 = (float) r6
            float r6 = r6 / r8
            r0.mTransitionDuration = r6
            goto L_0x00ad
        L_0x00a7:
            if (r1 <= 0) goto L_0x00ad
            float r6 = (float) r1
            float r6 = r6 / r8
            r0.mTransitionDuration = r6
        L_0x00ad:
            int r6 = r22.getChildCount()
            java.util.HashMap<android.view.View, androidx.constraintlayout.motion.widget.MotionController> r8 = r0.mFrameArrayList
            r8.clear()
            r8 = 0
        L_0x00b7:
            if (r8 >= r6) goto L_0x00d9
            android.view.View r11 = r0.getChildAt(r8)
            androidx.constraintlayout.motion.widget.MotionController r12 = new androidx.constraintlayout.motion.widget.MotionController
            r12.<init>(r11)
            java.util.HashMap<android.view.View, androidx.constraintlayout.motion.widget.MotionController> r13 = r0.mFrameArrayList
            r13.put(r11, r12)
            int r13 = r11.getId()
            java.util.HashMap<android.view.View, androidx.constraintlayout.motion.widget.MotionController> r14 = r0.mFrameArrayList
            java.lang.Object r14 = r14.get(r11)
            androidx.constraintlayout.motion.widget.MotionController r14 = (androidx.constraintlayout.motion.widget.MotionController) r14
            r3.put(r13, r14)
            int r8 = r8 + 1
            goto L_0x00b7
        L_0x00d9:
            r8 = 1
            r0.mInTransition = r8
            androidx.constraintlayout.motion.widget.MotionLayout$Model r11 = r0.mModel
            androidx.constraintlayout.core.widgets.ConstraintWidgetContainer r12 = r0.mLayoutWidget
            androidx.constraintlayout.motion.widget.MotionScene r13 = r0.mScene
            androidx.constraintlayout.widget.ConstraintSet r13 = r13.getConstraintSet(r4)
            r11.initFrom(r12, r2, r13)
            r22.rebuildScene()
            androidx.constraintlayout.motion.widget.MotionLayout$Model r2 = r0.mModel
            r2.build()
            r22.computeCurrentPositions()
            int r2 = r22.getWidth()
            int r17 = r22.getHeight()
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionHelper> r11 = r0.mDecoratorsHelpers
            if (r11 == 0) goto L_0x0161
            r11 = 0
        L_0x0101:
            if (r11 >= r6) goto L_0x011a
            java.util.HashMap<android.view.View, androidx.constraintlayout.motion.widget.MotionController> r12 = r0.mFrameArrayList
            android.view.View r13 = r0.getChildAt(r11)
            java.lang.Object r12 = r12.get(r13)
            androidx.constraintlayout.motion.widget.MotionController r12 = (androidx.constraintlayout.motion.widget.MotionController) r12
            if (r12 != 0) goto L_0x0112
            goto L_0x0117
        L_0x0112:
            androidx.constraintlayout.motion.widget.MotionScene r13 = r0.mScene
            r13.getKeyFrames(r12)
        L_0x0117:
            int r11 = r11 + 1
            goto L_0x0101
        L_0x011a:
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionHelper> r11 = r0.mDecoratorsHelpers
            java.util.Iterator r11 = r11.iterator()
        L_0x0120:
            boolean r12 = r11.hasNext()
            if (r12 == 0) goto L_0x0132
            java.lang.Object r12 = r11.next()
            androidx.constraintlayout.motion.widget.MotionHelper r12 = (androidx.constraintlayout.motion.widget.MotionHelper) r12
            java.util.HashMap<android.view.View, androidx.constraintlayout.motion.widget.MotionController> r13 = r0.mFrameArrayList
            r12.onPreSetup(r0, r13)
            goto L_0x0120
        L_0x0132:
            r11 = 0
            r15 = r11
        L_0x0134:
            if (r15 >= r6) goto L_0x015e
            java.util.HashMap<android.view.View, androidx.constraintlayout.motion.widget.MotionController> r11 = r0.mFrameArrayList
            android.view.View r12 = r0.getChildAt(r15)
            java.lang.Object r11 = r11.get(r12)
            r18 = r11
            androidx.constraintlayout.motion.widget.MotionController r18 = (androidx.constraintlayout.motion.widget.MotionController) r18
            if (r18 != 0) goto L_0x0149
            r21 = r15
            goto L_0x015b
        L_0x0149:
            float r14 = r0.mTransitionDuration
            long r19 = r22.getNanoTime()
            r11 = r18
            r12 = r2
            r13 = r17
            r21 = r15
            r15 = r19
            r11.setup(r12, r13, r14, r15)
        L_0x015b:
            int r15 = r21 + 1
            goto L_0x0134
        L_0x015e:
            r21 = r15
            goto L_0x0198
        L_0x0161:
            r11 = 0
            r15 = r11
        L_0x0163:
            if (r15 >= r6) goto L_0x0196
            java.util.HashMap<android.view.View, androidx.constraintlayout.motion.widget.MotionController> r11 = r0.mFrameArrayList
            android.view.View r12 = r0.getChildAt(r15)
            java.lang.Object r11 = r11.get(r12)
            r14 = r11
            androidx.constraintlayout.motion.widget.MotionController r14 = (androidx.constraintlayout.motion.widget.MotionController) r14
            if (r14 != 0) goto L_0x0177
            r21 = r15
            goto L_0x0193
        L_0x0177:
            androidx.constraintlayout.motion.widget.MotionScene r11 = r0.mScene
            r11.getKeyFrames(r14)
            float r13 = r0.mTransitionDuration
            long r18 = r22.getNanoTime()
            r11 = r14
            r12 = r2
            r16 = r13
            r13 = r17
            r20 = r14
            r14 = r16
            r21 = r15
            r15 = r18
            r11.setup(r12, r13, r14, r15)
        L_0x0193:
            int r15 = r21 + 1
            goto L_0x0163
        L_0x0196:
            r21 = r15
        L_0x0198:
            androidx.constraintlayout.motion.widget.MotionScene r11 = r0.mScene
            float r11 = r11.getStaggered()
            int r12 = (r11 > r9 ? 1 : (r11 == r9 ? 0 : -1))
            if (r12 == 0) goto L_0x01fe
            r12 = 2139095039(0x7f7fffff, float:3.4028235E38)
            r13 = -8388609(0xffffffffff7fffff, float:-3.4028235E38)
            r14 = 0
        L_0x01a9:
            if (r14 >= r6) goto L_0x01d0
            java.util.HashMap<android.view.View, androidx.constraintlayout.motion.widget.MotionController> r15 = r0.mFrameArrayList
            android.view.View r8 = r0.getChildAt(r14)
            java.lang.Object r8 = r15.get(r8)
            androidx.constraintlayout.motion.widget.MotionController r8 = (androidx.constraintlayout.motion.widget.MotionController) r8
            float r15 = r8.getFinalX()
            float r16 = r8.getFinalY()
            float r9 = r16 + r15
            float r12 = java.lang.Math.min(r12, r9)
            float r9 = r16 + r15
            float r13 = java.lang.Math.max(r13, r9)
            int r14 = r14 + 1
            r8 = 1
            r9 = 0
            goto L_0x01a9
        L_0x01d0:
            r8 = 0
        L_0x01d1:
            if (r8 >= r6) goto L_0x01fe
            java.util.HashMap<android.view.View, androidx.constraintlayout.motion.widget.MotionController> r9 = r0.mFrameArrayList
            android.view.View r14 = r0.getChildAt(r8)
            java.lang.Object r9 = r9.get(r14)
            androidx.constraintlayout.motion.widget.MotionController r9 = (androidx.constraintlayout.motion.widget.MotionController) r9
            float r14 = r9.getFinalX()
            float r15 = r9.getFinalY()
            float r16 = r10 - r11
            float r1 = r10 / r16
            r9.mStaggerScale = r1
            float r1 = r14 + r15
            float r1 = r1 - r12
            float r1 = r1 * r11
            float r16 = r13 - r12
            float r1 = r1 / r16
            float r1 = r11 - r1
            r9.mStaggerOffset = r1
            int r8 = r8 + 1
            r1 = r26
            goto L_0x01d1
        L_0x01fe:
            r1 = 0
            r0.mTransitionPosition = r1
            r0.mTransitionLastPosition = r1
            r1 = 1
            r0.mInTransition = r1
            r22.invalidate()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionLayout.transitionToState(int, int, int, int):void");
    }

    public void updateState() {
        this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(this.mBeginState), this.mScene.getConstraintSet(this.mEndState));
        rebuildScene();
    }

    public void updateState(int stateId, ConstraintSet set) {
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            motionScene.setConstraintSet(stateId, set);
        }
        updateState();
        if (this.mCurrentState == stateId) {
            set.applyTo(this);
        }
    }

    public void updateStateAnimate(int stateId, ConstraintSet set, int duration) {
        if (this.mScene != null && this.mCurrentState == stateId) {
            updateState(R.id.view_transition, getConstraintSet(stateId));
            setState(R.id.view_transition, -1, -1);
            updateState(stateId, set);
            MotionScene.Transition transition = new MotionScene.Transition(-1, this.mScene, R.id.view_transition, stateId);
            transition.setDuration(duration);
            setTransition(transition);
            transitionToEnd();
        }
    }

    public void viewTransition(int viewTransitionId, View... view) {
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            motionScene.viewTransition(viewTransitionId, view);
        } else {
            Log.e(TAG, " no motionScene");
        }
    }

    /* compiled from: 0021 */
    class Model {
        ConstraintSet mEnd = null;
        int mEndId;
        ConstraintWidgetContainer mLayoutEnd = new ConstraintWidgetContainer();
        ConstraintWidgetContainer mLayoutStart = new ConstraintWidgetContainer();
        ConstraintSet mStart = null;
        int mStartId;

        Model() {
        }

        private void computeStartEndSize(int widthMeasureSpec, int heightMeasureSpec) {
            int optimizationLevel = MotionLayout.this.getOptimizationLevel();
            if (MotionLayout.this.mCurrentState == MotionLayout.this.getStartState()) {
                MotionLayout motionLayout = MotionLayout.this;
                ConstraintWidgetContainer constraintWidgetContainer = this.mLayoutEnd;
                ConstraintSet constraintSet = this.mEnd;
                int i = (constraintSet == null || constraintSet.mRotate == 0) ? widthMeasureSpec : heightMeasureSpec;
                ConstraintSet constraintSet2 = this.mEnd;
                motionLayout.resolveSystem(constraintWidgetContainer, optimizationLevel, i, (constraintSet2 == null || constraintSet2.mRotate == 0) ? heightMeasureSpec : widthMeasureSpec);
                ConstraintSet constraintSet3 = this.mStart;
                if (constraintSet3 != null) {
                    MotionLayout.this.resolveSystem(this.mLayoutStart, optimizationLevel, constraintSet3.mRotate == 0 ? widthMeasureSpec : heightMeasureSpec, this.mStart.mRotate == 0 ? heightMeasureSpec : widthMeasureSpec);
                    return;
                }
                return;
            }
            ConstraintSet constraintSet4 = this.mStart;
            if (constraintSet4 != null) {
                MotionLayout.this.resolveSystem(this.mLayoutStart, optimizationLevel, constraintSet4.mRotate == 0 ? widthMeasureSpec : heightMeasureSpec, this.mStart.mRotate == 0 ? heightMeasureSpec : widthMeasureSpec);
            }
            MotionLayout motionLayout2 = MotionLayout.this;
            ConstraintWidgetContainer constraintWidgetContainer2 = this.mLayoutEnd;
            ConstraintSet constraintSet5 = this.mEnd;
            int i2 = (constraintSet5 == null || constraintSet5.mRotate == 0) ? widthMeasureSpec : heightMeasureSpec;
            ConstraintSet constraintSet6 = this.mEnd;
            motionLayout2.resolveSystem(constraintWidgetContainer2, optimizationLevel, i2, (constraintSet6 == null || constraintSet6.mRotate == 0) ? heightMeasureSpec : widthMeasureSpec);
        }

        private void debugLayout(String title, ConstraintWidgetContainer c) {
            StringBuilder append = new StringBuilder().append(title).append(" ");
            String name = Debug.getName((View) c.getCompanionWidget());
            Log1F380D.a((Object) name);
            String sb = append.append(name).toString();
            Log.v(MotionLayout.TAG, sb + "  ========= " + c);
            int size = c.getChildren().size();
            for (int i = 0; i < size; i++) {
                String str = sb + "[" + i + "] ";
                ConstraintWidget constraintWidget = c.getChildren().get(i);
                String str2 = "_";
                StringBuilder append2 = new StringBuilder().append(((HttpUrl.FRAGMENT_ENCODE_SET + (constraintWidget.mTop.mTarget != null ? "T" : str2)) + (constraintWidget.mBottom.mTarget != null ? "B" : str2)) + (constraintWidget.mLeft.mTarget != null ? "L" : str2));
                if (constraintWidget.mRight.mTarget != null) {
                    str2 = "R";
                }
                String sb2 = append2.append(str2).toString();
                View view = (View) constraintWidget.getCompanionWidget();
                String name2 = Debug.getName(view);
                Log1F380D.a((Object) name2);
                if (view instanceof TextView) {
                    name2 = name2 + "(" + ((TextView) view).getText() + ")";
                }
                Log.v(MotionLayout.TAG, str + "  " + name2 + " " + constraintWidget + " " + sb2);
            }
            Log.v(MotionLayout.TAG, sb + " done. ");
        }

        private void debugLayoutParam(String str, ConstraintLayout.LayoutParams params) {
            String str2 = "|__";
            StringBuilder append = new StringBuilder().append(((((((((((" " + (params.startToStart != -1 ? "SS" : "__")) + (params.startToEnd != -1 ? "|SE" : str2)) + (params.endToStart != -1 ? "|ES" : str2)) + (params.endToEnd != -1 ? "|EE" : str2)) + (params.leftToLeft != -1 ? "|LL" : str2)) + (params.leftToRight != -1 ? "|LR" : str2)) + (params.rightToLeft != -1 ? "|RL" : str2)) + (params.rightToRight != -1 ? "|RR" : str2)) + (params.topToTop != -1 ? "|TT" : str2)) + (params.topToBottom != -1 ? "|TB" : str2)) + (params.bottomToTop != -1 ? "|BT" : str2));
            if (params.bottomToBottom != -1) {
                str2 = "|BB";
            }
            Log.v(MotionLayout.TAG, str + append.append(str2).toString());
        }

        private void debugWidget(String str, ConstraintWidget child) {
            String str2;
            String str3;
            String str4;
            StringBuilder append = new StringBuilder().append(" ");
            String str5 = "B";
            String str6 = "__";
            if (child.mTop.mTarget != null) {
                str2 = "T" + (child.mTop.mTarget.mType == ConstraintAnchor.Type.TOP ? "T" : str5);
            } else {
                str2 = str6;
            }
            StringBuilder append2 = new StringBuilder().append(append.append(str2).toString());
            if (child.mBottom.mTarget != null) {
                StringBuilder append3 = new StringBuilder().append(str5);
                if (child.mBottom.mTarget.mType == ConstraintAnchor.Type.TOP) {
                    str5 = "T";
                }
                str3 = append3.append(str5).toString();
            } else {
                str3 = str6;
            }
            StringBuilder append4 = new StringBuilder().append(append2.append(str3).toString());
            String str7 = "R";
            if (child.mLeft.mTarget != null) {
                str4 = "L" + (child.mLeft.mTarget.mType == ConstraintAnchor.Type.LEFT ? "L" : str7);
            } else {
                str4 = str6;
            }
            StringBuilder append5 = new StringBuilder().append(append4.append(str4).toString());
            if (child.mRight.mTarget != null) {
                StringBuilder append6 = new StringBuilder().append(str7);
                if (child.mRight.mTarget.mType == ConstraintAnchor.Type.LEFT) {
                    str7 = "L";
                }
                str6 = append6.append(str7).toString();
            }
            Log.v(MotionLayout.TAG, str + append5.append(str6).toString() + " ---  " + child);
        }

        private void setupConstraintWidget(ConstraintWidgetContainer base, ConstraintSet cSet) {
            SparseArray sparseArray = new SparseArray();
            Constraints.LayoutParams layoutParams = new Constraints.LayoutParams(-2, -2);
            sparseArray.clear();
            sparseArray.put(0, base);
            sparseArray.put(MotionLayout.this.getId(), base);
            if (!(cSet == null || cSet.mRotate == 0)) {
                MotionLayout motionLayout = MotionLayout.this;
                motionLayout.resolveSystem(this.mLayoutEnd, motionLayout.getOptimizationLevel(), View.MeasureSpec.makeMeasureSpec(MotionLayout.this.getHeight(), BasicMeasure.EXACTLY), View.MeasureSpec.makeMeasureSpec(MotionLayout.this.getWidth(), BasicMeasure.EXACTLY));
            }
            Iterator<ConstraintWidget> it = base.getChildren().iterator();
            while (it.hasNext()) {
                ConstraintWidget next = it.next();
                next.setAnimated(true);
                sparseArray.put(((View) next.getCompanionWidget()).getId(), next);
            }
            Iterator<ConstraintWidget> it2 = base.getChildren().iterator();
            while (it2.hasNext()) {
                ConstraintWidget next2 = it2.next();
                View view = (View) next2.getCompanionWidget();
                cSet.applyToLayoutParams(view.getId(), layoutParams);
                next2.setWidth(cSet.getWidth(view.getId()));
                next2.setHeight(cSet.getHeight(view.getId()));
                if (view instanceof ConstraintHelper) {
                    cSet.applyToHelper((ConstraintHelper) view, next2, layoutParams, sparseArray);
                    if (view instanceof Barrier) {
                        ((Barrier) view).validateParams();
                    }
                }
                if (Build.VERSION.SDK_INT >= 17) {
                    layoutParams.resolveLayoutDirection(MotionLayout.this.getLayoutDirection());
                } else {
                    layoutParams.resolveLayoutDirection(0);
                }
                MotionLayout.this.applyConstraintsFromLayoutParams(false, view, next2, layoutParams, sparseArray);
                if (cSet.getVisibilityMode(view.getId()) == 1) {
                    next2.setVisibility(view.getVisibility());
                } else {
                    next2.setVisibility(cSet.getVisibility(view.getId()));
                }
            }
            Iterator<ConstraintWidget> it3 = base.getChildren().iterator();
            while (it3.hasNext()) {
                ConstraintWidget next3 = it3.next();
                if (next3 instanceof VirtualLayout) {
                    Helper helper = (Helper) next3;
                    ((ConstraintHelper) next3.getCompanionWidget()).updatePreLayout(base, helper, sparseArray);
                    ((VirtualLayout) helper).captureWidgets();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void copy(ConstraintWidgetContainer src, ConstraintWidgetContainer dest) {
            ArrayList<ConstraintWidget> children = src.getChildren();
            HashMap hashMap = new HashMap();
            hashMap.put(src, dest);
            dest.getChildren().clear();
            dest.copy(src, hashMap);
            Iterator<ConstraintWidget> it = children.iterator();
            while (it.hasNext()) {
                ConstraintWidget next = it.next();
                ConstraintWidget barrier = next instanceof androidx.constraintlayout.core.widgets.Barrier ? new androidx.constraintlayout.core.widgets.Barrier() : next instanceof Guideline ? new Guideline() : next instanceof Flow ? new Flow() : next instanceof Placeholder ? new Placeholder() : next instanceof Helper ? new HelperWidget() : new ConstraintWidget();
                dest.add(barrier);
                hashMap.put(next, barrier);
            }
            Iterator<ConstraintWidget> it2 = children.iterator();
            while (it2.hasNext()) {
                ConstraintWidget next2 = it2.next();
                ((ConstraintWidget) hashMap.get(next2)).copy(next2, hashMap);
            }
        }

        /* access modifiers changed from: package-private */
        public ConstraintWidget getWidget(ConstraintWidgetContainer container, View view) {
            if (container.getCompanionWidget() == view) {
                return container;
            }
            ArrayList<ConstraintWidget> children = container.getChildren();
            int size = children.size();
            for (int i = 0; i < size; i++) {
                ConstraintWidget constraintWidget = children.get(i);
                if (constraintWidget.getCompanionWidget() == view) {
                    return constraintWidget;
                }
            }
            return null;
        }

        /* access modifiers changed from: package-private */
        public void initFrom(ConstraintWidgetContainer baseLayout, ConstraintSet start, ConstraintSet end) {
            this.mStart = start;
            this.mEnd = end;
            this.mLayoutStart = new ConstraintWidgetContainer();
            this.mLayoutEnd = new ConstraintWidgetContainer();
            this.mLayoutStart.setMeasurer(MotionLayout.this.mLayoutWidget.getMeasurer());
            this.mLayoutEnd.setMeasurer(MotionLayout.this.mLayoutWidget.getMeasurer());
            this.mLayoutStart.removeAllChildren();
            this.mLayoutEnd.removeAllChildren();
            copy(MotionLayout.this.mLayoutWidget, this.mLayoutStart);
            copy(MotionLayout.this.mLayoutWidget, this.mLayoutEnd);
            if (((double) MotionLayout.this.mTransitionLastPosition) > 0.5d) {
                if (start != null) {
                    setupConstraintWidget(this.mLayoutStart, start);
                }
                setupConstraintWidget(this.mLayoutEnd, end);
            } else {
                setupConstraintWidget(this.mLayoutEnd, end);
                if (start != null) {
                    setupConstraintWidget(this.mLayoutStart, start);
                }
            }
            this.mLayoutStart.setRtl(MotionLayout.this.isRtl());
            this.mLayoutStart.updateHierarchy();
            this.mLayoutEnd.setRtl(MotionLayout.this.isRtl());
            this.mLayoutEnd.updateHierarchy();
            ViewGroup.LayoutParams layoutParams = MotionLayout.this.getLayoutParams();
            if (layoutParams != null) {
                if (layoutParams.width == -2) {
                    this.mLayoutStart.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                    this.mLayoutEnd.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                }
                if (layoutParams.height == -2) {
                    this.mLayoutStart.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                    this.mLayoutEnd.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                }
            }
        }

        public boolean isNotConfiguredWith(int startId, int endId) {
            return (startId == this.mStartId && endId == this.mEndId) ? false : true;
        }

        public void measure(int widthMeasureSpec, int heightMeasureSpec) {
            int mode = View.MeasureSpec.getMode(widthMeasureSpec);
            int mode2 = View.MeasureSpec.getMode(heightMeasureSpec);
            MotionLayout.this.mWidthMeasureMode = mode;
            MotionLayout.this.mHeightMeasureMode = mode2;
            int optimizationLevel = MotionLayout.this.getOptimizationLevel();
            computeStartEndSize(widthMeasureSpec, heightMeasureSpec);
            boolean z = true;
            if ((MotionLayout.this.getParent() instanceof MotionLayout) && mode == 1073741824 && mode2 == 1073741824) {
                z = false;
            }
            if (z) {
                computeStartEndSize(widthMeasureSpec, heightMeasureSpec);
                MotionLayout.this.mStartWrapWidth = this.mLayoutStart.getWidth();
                MotionLayout.this.mStartWrapHeight = this.mLayoutStart.getHeight();
                MotionLayout.this.mEndWrapWidth = this.mLayoutEnd.getWidth();
                MotionLayout.this.mEndWrapHeight = this.mLayoutEnd.getHeight();
                MotionLayout motionLayout = MotionLayout.this;
                motionLayout.mMeasureDuringTransition = (motionLayout.mStartWrapWidth == MotionLayout.this.mEndWrapWidth && MotionLayout.this.mStartWrapHeight == MotionLayout.this.mEndWrapHeight) ? false : true;
            }
            int i = MotionLayout.this.mStartWrapWidth;
            int i2 = MotionLayout.this.mStartWrapHeight;
            if (MotionLayout.this.mWidthMeasureMode == Integer.MIN_VALUE || MotionLayout.this.mWidthMeasureMode == 0) {
                i = (int) (((float) MotionLayout.this.mStartWrapWidth) + (MotionLayout.this.mPostInterpolationPosition * ((float) (MotionLayout.this.mEndWrapWidth - MotionLayout.this.mStartWrapWidth))));
            }
            if (MotionLayout.this.mHeightMeasureMode == Integer.MIN_VALUE || MotionLayout.this.mHeightMeasureMode == 0) {
                i2 = (int) (((float) MotionLayout.this.mStartWrapHeight) + (MotionLayout.this.mPostInterpolationPosition * ((float) (MotionLayout.this.mEndWrapHeight - MotionLayout.this.mStartWrapHeight))));
            }
            MotionLayout.this.resolveMeasuredDimension(widthMeasureSpec, heightMeasureSpec, i, i2, this.mLayoutStart.isWidthMeasuredTooSmall() || this.mLayoutEnd.isWidthMeasuredTooSmall(), this.mLayoutStart.isHeightMeasuredTooSmall() || this.mLayoutEnd.isHeightMeasuredTooSmall());
        }

        public void reEvaluateState() {
            measure(MotionLayout.this.mLastWidthMeasureSpec, MotionLayout.this.mLastHeightMeasureSpec);
            MotionLayout.this.setupMotionViews();
        }

        public void setMeasuredId(int startId, int endId) {
            this.mStartId = startId;
            this.mEndId = endId;
        }

        public void build() {
            SparseArray sparseArray;
            String str;
            int childCount = MotionLayout.this.getChildCount();
            MotionLayout.this.mFrameArrayList.clear();
            SparseArray sparseArray2 = new SparseArray();
            int[] iArr = new int[childCount];
            for (int i = 0; i < childCount; i++) {
                View childAt = MotionLayout.this.getChildAt(i);
                MotionController motionController = new MotionController(childAt);
                int id = childAt.getId();
                iArr[i] = id;
                sparseArray2.put(id, motionController);
                MotionLayout.this.mFrameArrayList.put(childAt, motionController);
            }
            int i2 = 0;
            while (i2 < childCount) {
                View childAt2 = MotionLayout.this.getChildAt(i2);
                MotionController motionController2 = MotionLayout.this.mFrameArrayList.get(childAt2);
                if (motionController2 == null) {
                    sparseArray = sparseArray2;
                } else {
                    if (this.mStart != null) {
                        ConstraintWidget widget = getWidget(this.mLayoutStart, childAt2);
                        if (widget != null) {
                            motionController2.setStartState(MotionLayout.this.toRect(widget), this.mStart, MotionLayout.this.getWidth(), MotionLayout.this.getHeight());
                        } else if (MotionLayout.this.mDebugPath != 0) {
                            StringBuilder sb = new StringBuilder();
                            String location = Debug.getLocation();
                            Log1F380D.a((Object) location);
                            StringBuilder append = sb.append(location).append("no widget for  ");
                            String name = Debug.getName(childAt2);
                            Log1F380D.a((Object) name);
                            Log.e(MotionLayout.TAG, append.append(name).append(" (").append(childAt2.getClass().getName()).append(")").toString());
                        }
                        sparseArray = sparseArray2;
                        str = MotionLayout.TAG;
                    } else if (MotionLayout.this.mInRotation) {
                        int i3 = MotionLayout.this.mRotatMode;
                        int access$2100 = MotionLayout.this.mPreRotateWidth;
                        int access$2200 = MotionLayout.this.mPreRotateHeight;
                        sparseArray = sparseArray2;
                        str = MotionLayout.TAG;
                        motionController2.setStartState(MotionLayout.this.mPreRotate.get(childAt2), childAt2, i3, access$2100, access$2200);
                    } else {
                        sparseArray = sparseArray2;
                        str = MotionLayout.TAG;
                    }
                    if (this.mEnd != null) {
                        ConstraintWidget widget2 = getWidget(this.mLayoutEnd, childAt2);
                        if (widget2 != null) {
                            motionController2.setEndState(MotionLayout.this.toRect(widget2), this.mEnd, MotionLayout.this.getWidth(), MotionLayout.this.getHeight());
                        } else if (MotionLayout.this.mDebugPath != 0) {
                            StringBuilder sb2 = new StringBuilder();
                            String location2 = Debug.getLocation();
                            Log1F380D.a((Object) location2);
                            StringBuilder append2 = sb2.append(location2).append("no widget for  ");
                            String name2 = Debug.getName(childAt2);
                            Log1F380D.a((Object) name2);
                            Log.e(str, append2.append(name2).append(" (").append(childAt2.getClass().getName()).append(")").toString());
                        }
                    }
                }
                i2++;
                sparseArray2 = sparseArray;
            }
            SparseArray sparseArray3 = sparseArray2;
            int i4 = 0;
            while (i4 < childCount) {
                SparseArray sparseArray4 = sparseArray3;
                MotionController motionController3 = (MotionController) sparseArray4.get(iArr[i4]);
                int animateRelativeTo = motionController3.getAnimateRelativeTo();
                if (animateRelativeTo != -1) {
                    motionController3.setupRelative((MotionController) sparseArray4.get(animateRelativeTo));
                }
                i4++;
                sparseArray3 = sparseArray4;
            }
        }
    }

    private void checkStructure(int csetId, ConstraintSet set) {
        String name = Debug.getName(getContext(), csetId);
        Log1F380D.a((Object) name);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            int id = childAt.getId();
            if (id == -1) {
                Log.w(TAG, "CHECK: " + name + " ALL VIEWS SHOULD HAVE ID's " + childAt.getClass().getName() + " does not!");
            }
            if (set.getConstraint(id) == null) {
                StringBuilder append = new StringBuilder().append("CHECK: ").append(name).append(" NO CONSTRAINTS for ");
                String name2 = Debug.getName(childAt);
                Log1F380D.a((Object) name2);
                Log.w(TAG, append.append(name2).toString());
            }
        }
        int[] knownIds = set.getKnownIds();
        for (int i2 = 0; i2 < knownIds.length; i2++) {
            int i3 = knownIds[i2];
            String name3 = Debug.getName(getContext(), i3);
            Log1F380D.a((Object) name3);
            if (findViewById(knownIds[i2]) == null) {
                Log.w(TAG, "CHECK: " + name + " NO View matches id " + name3);
            }
            if (set.getHeight(i3) == -1) {
                Log.w(TAG, "CHECK: " + name + "(" + name3 + ") no LAYOUT_HEIGHT");
            }
            if (set.getWidth(i3) == -1) {
                Log.w(TAG, "CHECK: " + name + "(" + name3 + ") no LAYOUT_HEIGHT");
            }
        }
    }

    private void debugPos() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            StringBuilder append = new StringBuilder().append(" ");
            String location = Debug.getLocation();
            Log1F380D.a((Object) location);
            StringBuilder append2 = append.append(location).append(" ");
            String name = Debug.getName(this);
            Log1F380D.a((Object) name);
            StringBuilder append3 = append2.append(name).append(" ");
            String name2 = Debug.getName(getContext(), this.mCurrentState);
            Log1F380D.a((Object) name2);
            StringBuilder append4 = append3.append(name2).append(" ");
            String name3 = Debug.getName(childAt);
            Log1F380D.a((Object) name3);
            Log.v(TAG, append4.append(name3).append(childAt.getLeft()).append(" ").append(childAt.getTop()).toString());
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        String str;
        ArrayList<MotionHelper> arrayList = this.mDecoratorsHelpers;
        if (arrayList != null) {
            Iterator<MotionHelper> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().onPreDraw(canvas);
            }
        }
        evaluate(false);
        MotionScene motionScene = this.mScene;
        if (!(motionScene == null || motionScene.mViewTransitionController == null)) {
            this.mScene.mViewTransitionController.animate();
        }
        super.dispatchDraw(canvas);
        if (this.mScene != null) {
            if ((this.mDebugPath & 1) == 1 && !isInEditMode()) {
                this.mFrames++;
                long nanoTime = getNanoTime();
                long j = this.mLastDrawTime;
                if (j != -1) {
                    long j2 = nanoTime - j;
                    if (j2 > 200000000) {
                        this.mLastFps = ((float) ((int) ((((float) this.mFrames) / (((float) j2) * 1.0E-9f)) * 100.0f))) / 100.0f;
                        this.mFrames = 0;
                        this.mLastDrawTime = nanoTime;
                    }
                } else {
                    this.mLastDrawTime = nanoTime;
                }
                Paint paint = new Paint();
                paint.setTextSize(42.0f);
                StringBuilder append = new StringBuilder().append(this.mLastFps).append(" fps ");
                String state = Debug.getState(this, this.mBeginState);
                Log1F380D.a((Object) state);
                StringBuilder append2 = new StringBuilder().append(append.append(state).append(" -> ").toString());
                String state2 = Debug.getState(this, this.mEndState);
                Log1F380D.a((Object) state2);
                StringBuilder append3 = append2.append(state2).append(" (progress: ").append(((float) ((int) (getProgress() * 1000.0f))) / 10.0f).append(" ) state=");
                int i = this.mCurrentState;
                if (i == -1) {
                    str = "undefined";
                } else {
                    str = Debug.getState(this, i);
                    Log1F380D.a((Object) str);
                }
                String sb = append3.append(str).toString();
                paint.setColor(ViewCompat.MEASURED_STATE_MASK);
                canvas.drawText(sb, 11.0f, (float) (getHeight() - 29), paint);
                paint.setColor(-7864184);
                canvas.drawText(sb, 10.0f, (float) (getHeight() - 30), paint);
            }
            if (this.mDebugPath > 1) {
                if (this.mDevModeDraw == null) {
                    this.mDevModeDraw = new DevModeDraw();
                }
                this.mDevModeDraw.draw(canvas, this.mFrameArrayList, this.mScene.getDuration(), this.mDebugPath);
            }
            ArrayList<MotionHelper> arrayList2 = this.mDecoratorsHelpers;
            if (arrayList2 != null) {
                Iterator<MotionHelper> it2 = arrayList2.iterator();
                while (it2.hasNext()) {
                    it2.next().onPostDraw(canvas);
                }
            }
        }
    }

    public void setTransition(int transitionId) {
        if (this.mScene != null) {
            MotionScene.Transition transition = getTransition(transitionId);
            int i = this.mCurrentState;
            this.mBeginState = transition.getStartConstraintSetId();
            this.mEndState = transition.getEndConstraintSetId();
            if (!isAttachedToWindow()) {
                if (this.mStateCache == null) {
                    this.mStateCache = new StateCache();
                }
                this.mStateCache.setStartState(this.mBeginState);
                this.mStateCache.setEndState(this.mEndState);
                return;
            }
            float f = Float.NaN;
            int i2 = this.mCurrentState;
            if (i2 == this.mBeginState) {
                f = 0.0f;
            } else if (i2 == this.mEndState) {
                f = 1.0f;
            }
            this.mScene.setTransition(transition);
            this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(this.mBeginState), this.mScene.getConstraintSet(this.mEndState));
            rebuildScene();
            float f2 = 0.0f;
            if (this.mTransitionLastPosition != f) {
                if (f == 0.0f) {
                    endTrigger(true);
                    this.mScene.getConstraintSet(this.mBeginState).applyTo(this);
                } else if (f == 1.0f) {
                    endTrigger(false);
                    this.mScene.getConstraintSet(this.mEndState).applyTo(this);
                }
            }
            if (!Float.isNaN(f)) {
                f2 = f;
            }
            this.mTransitionLastPosition = f2;
            if (Float.isNaN(f)) {
                StringBuilder sb = new StringBuilder();
                String location = Debug.getLocation();
                Log1F380D.a((Object) location);
                Log.v(TAG, sb.append(location).append(" transitionToStart ").toString());
                transitionToStart();
                return;
            }
            setProgress(f);
        }
    }

    public String toString() {
        Context context = getContext();
        StringBuilder sb = new StringBuilder();
        String name = Debug.getName(context, this.mBeginState);
        Log1F380D.a((Object) name);
        StringBuilder append = sb.append(name).append("->");
        String name2 = Debug.getName(context, this.mEndState);
        Log1F380D.a((Object) name2);
        return append.append(name2).append(" (pos:").append(this.mTransitionLastPosition).append(" Dpos/Dt:").append(this.mLastVelocity).toString();
    }
}
