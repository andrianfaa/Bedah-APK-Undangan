package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.R;
import androidx.core.widget.NestedScrollView;
import mt.Log1F380D;
import org.xmlpull.v1.XmlPullParser;

/* compiled from: 0024 */
class TouchResponse {
    public static final int COMPLETE_MODE_CONTINUOUS_VELOCITY = 0;
    public static final int COMPLETE_MODE_SPRING = 1;
    private static final boolean DEBUG = false;
    private static final float EPSILON = 1.0E-7f;
    static final int FLAG_DISABLE_POST_SCROLL = 1;
    static final int FLAG_DISABLE_SCROLL = 2;
    static final int FLAG_SUPPORT_SCROLL_UP = 4;
    private static final int SEC_TO_MILLISECONDS = 1000;
    private static final int SIDE_BOTTOM = 3;
    private static final int SIDE_END = 6;
    private static final int SIDE_LEFT = 1;
    private static final int SIDE_MIDDLE = 4;
    private static final int SIDE_RIGHT = 2;
    private static final int SIDE_START = 5;
    private static final int SIDE_TOP = 0;
    private static final String TAG = "TouchResponse";
    private static final float[][] TOUCH_DIRECTION = {new float[]{0.0f, -1.0f}, new float[]{0.0f, 1.0f}, new float[]{-1.0f, 0.0f}, new float[]{1.0f, 0.0f}, new float[]{-1.0f, 0.0f}, new float[]{1.0f, 0.0f}};
    private static final int TOUCH_DOWN = 1;
    private static final int TOUCH_END = 5;
    private static final int TOUCH_LEFT = 2;
    private static final int TOUCH_RIGHT = 3;
    private static final float[][] TOUCH_SIDES = {new float[]{0.5f, 0.0f}, new float[]{0.0f, 0.5f}, new float[]{1.0f, 0.5f}, new float[]{0.5f, 1.0f}, new float[]{0.5f, 0.5f}, new float[]{0.0f, 0.5f}, new float[]{1.0f, 0.5f}};
    private static final int TOUCH_START = 4;
    private static final int TOUCH_UP = 0;
    private float[] mAnchorDpDt = new float[2];
    private int mAutoCompleteMode = 0;
    private float mDragScale = 1.0f;
    private boolean mDragStarted = false;
    private float mDragThreshold = 10.0f;
    private int mFlags = 0;
    boolean mIsRotateMode = false;
    private float mLastTouchX;
    private float mLastTouchY;
    private int mLimitBoundsTo = -1;
    private float mMaxAcceleration = 1.2f;
    private float mMaxVelocity = 4.0f;
    private final MotionLayout mMotionLayout;
    private boolean mMoveWhenScrollAtTop = true;
    private int mOnTouchUp = 0;
    float mRotateCenterX = 0.5f;
    float mRotateCenterY = 0.5f;
    private int mRotationCenterId = -1;
    private int mSpringBoundary = 0;
    private float mSpringDamping = 10.0f;
    private float mSpringMass = 1.0f;
    private float mSpringStiffness = Float.NaN;
    private float mSpringStopThreshold = Float.NaN;
    private int[] mTempLoc = new int[2];
    private int mTouchAnchorId = -1;
    private int mTouchAnchorSide = 0;
    private float mTouchAnchorX = 0.5f;
    private float mTouchAnchorY = 0.5f;
    private float mTouchDirectionX = 0.0f;
    private float mTouchDirectionY = 1.0f;
    private int mTouchRegionId = -1;
    private int mTouchSide = 0;

    TouchResponse(Context context, MotionLayout layout, XmlPullParser parser) {
        this.mMotionLayout = layout;
        fillFromAttributeList(context, Xml.asAttributeSet(parser));
    }

    public TouchResponse(MotionLayout layout, OnSwipe onSwipe) {
        this.mMotionLayout = layout;
        this.mTouchAnchorId = onSwipe.getTouchAnchorId();
        int touchAnchorSide = onSwipe.getTouchAnchorSide();
        this.mTouchAnchorSide = touchAnchorSide;
        if (touchAnchorSide != -1) {
            float[] fArr = TOUCH_SIDES[touchAnchorSide];
            this.mTouchAnchorX = fArr[0];
            this.mTouchAnchorY = fArr[1];
        }
        int dragDirection = onSwipe.getDragDirection();
        this.mTouchSide = dragDirection;
        float[][] fArr2 = TOUCH_DIRECTION;
        if (dragDirection < fArr2.length) {
            float[] fArr3 = fArr2[dragDirection];
            this.mTouchDirectionX = fArr3[0];
            this.mTouchDirectionY = fArr3[1];
        } else {
            this.mTouchDirectionY = Float.NaN;
            this.mTouchDirectionX = Float.NaN;
            this.mIsRotateMode = true;
        }
        this.mMaxVelocity = onSwipe.getMaxVelocity();
        this.mMaxAcceleration = onSwipe.getMaxAcceleration();
        this.mMoveWhenScrollAtTop = onSwipe.getMoveWhenScrollAtTop();
        this.mDragScale = onSwipe.getDragScale();
        this.mDragThreshold = onSwipe.getDragThreshold();
        this.mTouchRegionId = onSwipe.getTouchRegionId();
        this.mOnTouchUp = onSwipe.getOnTouchUp();
        this.mFlags = onSwipe.getNestedScrollFlags();
        this.mLimitBoundsTo = onSwipe.getLimitBoundsTo();
        this.mRotationCenterId = onSwipe.getRotationCenterId();
        this.mSpringBoundary = onSwipe.getSpringBoundary();
        this.mSpringDamping = onSwipe.getSpringDamping();
        this.mSpringMass = onSwipe.getSpringMass();
        this.mSpringStiffness = onSwipe.getSpringStiffness();
        this.mSpringStopThreshold = onSwipe.getSpringStopThreshold();
        this.mAutoCompleteMode = onSwipe.getAutoCompleteMode();
    }

    private void fill(TypedArray a) {
        int indexCount = a.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = a.getIndex(i);
            if (index == R.styleable.OnSwipe_touchAnchorId) {
                this.mTouchAnchorId = a.getResourceId(index, this.mTouchAnchorId);
            } else if (index == R.styleable.OnSwipe_touchAnchorSide) {
                int i2 = a.getInt(index, this.mTouchAnchorSide);
                this.mTouchAnchorSide = i2;
                float[] fArr = TOUCH_SIDES[i2];
                this.mTouchAnchorX = fArr[0];
                this.mTouchAnchorY = fArr[1];
            } else if (index == R.styleable.OnSwipe_dragDirection) {
                int i3 = a.getInt(index, this.mTouchSide);
                this.mTouchSide = i3;
                float[][] fArr2 = TOUCH_DIRECTION;
                if (i3 < fArr2.length) {
                    float[] fArr3 = fArr2[i3];
                    this.mTouchDirectionX = fArr3[0];
                    this.mTouchDirectionY = fArr3[1];
                } else {
                    this.mTouchDirectionY = Float.NaN;
                    this.mTouchDirectionX = Float.NaN;
                    this.mIsRotateMode = true;
                }
            } else if (index == R.styleable.OnSwipe_maxVelocity) {
                this.mMaxVelocity = a.getFloat(index, this.mMaxVelocity);
            } else if (index == R.styleable.OnSwipe_maxAcceleration) {
                this.mMaxAcceleration = a.getFloat(index, this.mMaxAcceleration);
            } else if (index == R.styleable.OnSwipe_moveWhenScrollAtTop) {
                this.mMoveWhenScrollAtTop = a.getBoolean(index, this.mMoveWhenScrollAtTop);
            } else if (index == R.styleable.OnSwipe_dragScale) {
                this.mDragScale = a.getFloat(index, this.mDragScale);
            } else if (index == R.styleable.OnSwipe_dragThreshold) {
                this.mDragThreshold = a.getFloat(index, this.mDragThreshold);
            } else if (index == R.styleable.OnSwipe_touchRegionId) {
                this.mTouchRegionId = a.getResourceId(index, this.mTouchRegionId);
            } else if (index == R.styleable.OnSwipe_onTouchUp) {
                this.mOnTouchUp = a.getInt(index, this.mOnTouchUp);
            } else if (index == R.styleable.OnSwipe_nestedScrollFlags) {
                this.mFlags = a.getInteger(index, 0);
            } else if (index == R.styleable.OnSwipe_limitBoundsTo) {
                this.mLimitBoundsTo = a.getResourceId(index, 0);
            } else if (index == R.styleable.OnSwipe_rotationCenterId) {
                this.mRotationCenterId = a.getResourceId(index, this.mRotationCenterId);
            } else if (index == R.styleable.OnSwipe_springDamping) {
                this.mSpringDamping = a.getFloat(index, this.mSpringDamping);
            } else if (index == R.styleable.OnSwipe_springMass) {
                this.mSpringMass = a.getFloat(index, this.mSpringMass);
            } else if (index == R.styleable.OnSwipe_springStiffness) {
                this.mSpringStiffness = a.getFloat(index, this.mSpringStiffness);
            } else if (index == R.styleable.OnSwipe_springStopThreshold) {
                this.mSpringStopThreshold = a.getFloat(index, this.mSpringStopThreshold);
            } else if (index == R.styleable.OnSwipe_springBoundary) {
                this.mSpringBoundary = a.getInt(index, this.mSpringBoundary);
            } else if (index == R.styleable.OnSwipe_autoCompleteMode) {
                this.mAutoCompleteMode = a.getInt(index, this.mAutoCompleteMode);
            }
        }
    }

    private void fillFromAttributeList(Context context, AttributeSet attrs) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.OnSwipe);
        fill(obtainStyledAttributes);
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: package-private */
    public float dot(float dx, float dy) {
        return (this.mTouchDirectionX * dx) + (this.mTouchDirectionY * dy);
    }

    public int getAnchorId() {
        return this.mTouchAnchorId;
    }

    public int getAutoCompleteMode() {
        return this.mAutoCompleteMode;
    }

    public int getFlags() {
        return this.mFlags;
    }

    /* access modifiers changed from: package-private */
    public RectF getLimitBoundsTo(ViewGroup layout, RectF rect) {
        View findViewById;
        int i = this.mLimitBoundsTo;
        if (i == -1 || (findViewById = layout.findViewById(i)) == null) {
            return null;
        }
        rect.set((float) findViewById.getLeft(), (float) findViewById.getTop(), (float) findViewById.getRight(), (float) findViewById.getBottom());
        return rect;
    }

    /* access modifiers changed from: package-private */
    public int getLimitBoundsToId() {
        return this.mLimitBoundsTo;
    }

    /* access modifiers changed from: package-private */
    public float getMaxAcceleration() {
        return this.mMaxAcceleration;
    }

    public float getMaxVelocity() {
        return this.mMaxVelocity;
    }

    /* access modifiers changed from: package-private */
    public boolean getMoveWhenScrollAtTop() {
        return this.mMoveWhenScrollAtTop;
    }

    /* access modifiers changed from: package-private */
    public float getProgressDirection(float dx, float dy) {
        this.mMotionLayout.getAnchorDpDt(this.mTouchAnchorId, this.mMotionLayout.getProgress(), this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
        float f = this.mTouchDirectionX;
        if (f != 0.0f) {
            float[] fArr = this.mAnchorDpDt;
            if (fArr[0] == 0.0f) {
                fArr[0] = 1.0E-7f;
            }
            return (f * dx) / fArr[0];
        }
        float[] fArr2 = this.mAnchorDpDt;
        if (fArr2[1] == 0.0f) {
            fArr2[1] = 1.0E-7f;
        }
        return (this.mTouchDirectionY * dy) / fArr2[1];
    }

    public int getSpringBoundary() {
        return this.mSpringBoundary;
    }

    public float getSpringDamping() {
        return this.mSpringDamping;
    }

    public float getSpringMass() {
        return this.mSpringMass;
    }

    public float getSpringStiffness() {
        return this.mSpringStiffness;
    }

    public float getSpringStopThreshold() {
        return this.mSpringStopThreshold;
    }

    /* access modifiers changed from: package-private */
    public RectF getTouchRegion(ViewGroup layout, RectF rect) {
        View findViewById;
        int i = this.mTouchRegionId;
        if (i == -1 || (findViewById = layout.findViewById(i)) == null) {
            return null;
        }
        rect.set((float) findViewById.getLeft(), (float) findViewById.getTop(), (float) findViewById.getRight(), (float) findViewById.getBottom());
        return rect;
    }

    /* access modifiers changed from: package-private */
    public int getTouchRegionId() {
        return this.mTouchRegionId;
    }

    /* access modifiers changed from: package-private */
    public boolean isDragStarted() {
        return this.mDragStarted;
    }

    /* access modifiers changed from: package-private */
    public void processTouchEvent(MotionEvent event, MotionLayout.MotionTracker velocityTracker, int currentState, MotionScene motionScene) {
        int i;
        MotionLayout.MotionTracker motionTracker = velocityTracker;
        if (this.mIsRotateMode) {
            processTouchRotateEvent(event, velocityTracker, currentState, motionScene);
            return;
        }
        motionTracker.addMovement(event);
        switch (event.getAction()) {
            case 0:
                this.mLastTouchX = event.getRawX();
                this.mLastTouchY = event.getRawY();
                this.mDragStarted = false;
                return;
            case 1:
                this.mDragStarted = false;
                motionTracker.computeCurrentVelocity(1000);
                float xVelocity = velocityTracker.getXVelocity();
                float yVelocity = velocityTracker.getYVelocity();
                float progress = this.mMotionLayout.getProgress();
                float f = progress;
                int i2 = this.mTouchAnchorId;
                if (i2 != -1) {
                    this.mMotionLayout.getAnchorDpDt(i2, f, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
                } else {
                    float min = (float) Math.min(this.mMotionLayout.getWidth(), this.mMotionLayout.getHeight());
                    float[] fArr = this.mAnchorDpDt;
                    fArr[1] = this.mTouchDirectionY * min;
                    fArr[0] = this.mTouchDirectionX * min;
                }
                float f2 = this.mTouchDirectionX;
                float[] fArr2 = this.mAnchorDpDt;
                float f3 = fArr2[0];
                float f4 = this.mTouchDirectionY;
                float f5 = fArr2[1];
                float f6 = (f2 * f3) + (f4 * f5);
                float f7 = f2 != 0.0f ? xVelocity / f3 : yVelocity / f5;
                if (!Float.isNaN(f7)) {
                    f += f7 / 3.0f;
                }
                if (f != 0.0f && f != 1.0f && (i = this.mOnTouchUp) != 3) {
                    float f8 = ((double) f) < 0.5d ? 0.0f : 1.0f;
                    if (i == 6) {
                        if (progress + f7 < 0.0f) {
                            f7 = Math.abs(f7);
                        }
                        f8 = 1.0f;
                    }
                    if (this.mOnTouchUp == 7) {
                        if (progress + f7 > 1.0f) {
                            f7 = -Math.abs(f7);
                        }
                        f8 = 0.0f;
                    }
                    this.mMotionLayout.touchAnimateTo(this.mOnTouchUp, f8, f7);
                    if (0.0f >= progress || 1.0f <= progress) {
                        this.mMotionLayout.setState(MotionLayout.TransitionState.FINISHED);
                        return;
                    }
                    return;
                } else if (0.0f >= f || 1.0f <= f) {
                    this.mMotionLayout.setState(MotionLayout.TransitionState.FINISHED);
                    return;
                } else {
                    return;
                }
            case 2:
                float rawY = event.getRawY() - this.mLastTouchY;
                float rawX = event.getRawX() - this.mLastTouchX;
                if (Math.abs((this.mTouchDirectionX * rawX) + (this.mTouchDirectionY * rawY)) > this.mDragThreshold || this.mDragStarted) {
                    float progress2 = this.mMotionLayout.getProgress();
                    if (!this.mDragStarted) {
                        this.mDragStarted = true;
                        this.mMotionLayout.setProgress(progress2);
                    }
                    int i3 = this.mTouchAnchorId;
                    if (i3 != -1) {
                        this.mMotionLayout.getAnchorDpDt(i3, progress2, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
                    } else {
                        float min2 = (float) Math.min(this.mMotionLayout.getWidth(), this.mMotionLayout.getHeight());
                        float[] fArr3 = this.mAnchorDpDt;
                        fArr3[1] = this.mTouchDirectionY * min2;
                        fArr3[0] = this.mTouchDirectionX * min2;
                    }
                    float f9 = this.mTouchDirectionX;
                    float[] fArr4 = this.mAnchorDpDt;
                    if (((double) Math.abs(((f9 * fArr4[0]) + (this.mTouchDirectionY * fArr4[1])) * this.mDragScale)) < 0.01d) {
                        float[] fArr5 = this.mAnchorDpDt;
                        fArr5[0] = 0.01f;
                        fArr5[1] = 0.01f;
                    }
                    float max = Math.max(Math.min(progress2 + (this.mTouchDirectionX != 0.0f ? rawX / this.mAnchorDpDt[0] : rawY / this.mAnchorDpDt[1]), 1.0f), 0.0f);
                    if (this.mOnTouchUp == 6) {
                        max = Math.max(max, 0.01f);
                    }
                    if (this.mOnTouchUp == 7) {
                        max = Math.min(max, 0.99f);
                    }
                    float progress3 = this.mMotionLayout.getProgress();
                    if (max != progress3) {
                        if (progress3 == 0.0f || progress3 == 1.0f) {
                            this.mMotionLayout.endTrigger(progress3 == 0.0f);
                        }
                        this.mMotionLayout.setProgress(max);
                        motionTracker.computeCurrentVelocity(1000);
                        this.mMotionLayout.mLastVelocity = this.mTouchDirectionX != 0.0f ? velocityTracker.getXVelocity() / this.mAnchorDpDt[0] : velocityTracker.getYVelocity() / this.mAnchorDpDt[1];
                    } else {
                        this.mMotionLayout.mLastVelocity = 0.0f;
                    }
                    this.mLastTouchX = event.getRawX();
                    this.mLastTouchY = event.getRawY();
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: package-private */
    public void processTouchRotateEvent(MotionEvent event, MotionLayout.MotionTracker velocityTracker, int currentState, MotionScene motionScene) {
        int i;
        char c;
        MotionLayout.MotionTracker motionTracker = velocityTracker;
        motionTracker.addMovement(event);
        switch (event.getAction()) {
            case 0:
                this.mLastTouchX = event.getRawX();
                this.mLastTouchY = event.getRawY();
                this.mDragStarted = false;
                return;
            case 1:
                this.mDragStarted = false;
                motionTracker.computeCurrentVelocity(16);
                float xVelocity = velocityTracker.getXVelocity();
                float yVelocity = velocityTracker.getYVelocity();
                float progress = this.mMotionLayout.getProgress();
                float f = progress;
                float width = ((float) this.mMotionLayout.getWidth()) / 2.0f;
                float height = ((float) this.mMotionLayout.getHeight()) / 2.0f;
                int i2 = this.mRotationCenterId;
                if (i2 != -1) {
                    View findViewById = this.mMotionLayout.findViewById(i2);
                    this.mMotionLayout.getLocationOnScreen(this.mTempLoc);
                    width = ((float) this.mTempLoc[0]) + (((float) (findViewById.getLeft() + findViewById.getRight())) / 2.0f);
                    height = ((float) this.mTempLoc[1]) + (((float) (findViewById.getTop() + findViewById.getBottom())) / 2.0f);
                } else {
                    int i3 = this.mTouchAnchorId;
                    if (i3 != -1) {
                        View findViewById2 = this.mMotionLayout.findViewById(this.mMotionLayout.getMotionController(i3).getAnimateRelativeTo());
                        this.mMotionLayout.getLocationOnScreen(this.mTempLoc);
                        width = ((float) this.mTempLoc[0]) + (((float) (findViewById2.getLeft() + findViewById2.getRight())) / 2.0f);
                        height = ((float) this.mTempLoc[1]) + (((float) (findViewById2.getTop() + findViewById2.getBottom())) / 2.0f);
                    }
                }
                float rawX = event.getRawX() - width;
                float rawY = event.getRawY() - height;
                double degrees = Math.toDegrees(Math.atan2((double) rawY, (double) rawX));
                int i4 = this.mTouchAnchorId;
                if (i4 != -1) {
                    this.mMotionLayout.getAnchorDpDt(i4, f, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
                    float[] fArr = this.mAnchorDpDt;
                    fArr[1] = (float) Math.toDegrees((double) fArr[1]);
                } else {
                    this.mAnchorDpDt[1] = 360.0f;
                }
                float degrees2 = ((float) (Math.toDegrees(Math.atan2((double) (yVelocity + rawY), (double) (xVelocity + rawX))) - degrees)) * 62.5f;
                if (!Float.isNaN(degrees2)) {
                    f += ((degrees2 * 3.0f) * this.mDragScale) / this.mAnchorDpDt[1];
                }
                if (f == 0.0f || f == 1.0f || (i = this.mOnTouchUp) == 3) {
                    float f2 = xVelocity;
                    float f3 = yVelocity;
                    if (0.0f >= f || 1.0f <= f) {
                        this.mMotionLayout.setState(MotionLayout.TransitionState.FINISHED);
                        return;
                    }
                    return;
                }
                float f4 = xVelocity;
                float f5 = (this.mDragScale * degrees2) / this.mAnchorDpDt[1];
                float f6 = yVelocity;
                float f7 = ((double) f) < 0.5d ? 0.0f : 1.0f;
                if (i == 6) {
                    if (progress + f5 < 0.0f) {
                        f5 = Math.abs(f5);
                    }
                    f7 = 1.0f;
                }
                if (this.mOnTouchUp == 7) {
                    if (progress + f5 > 1.0f) {
                        f5 = -Math.abs(f5);
                    }
                    f7 = 0.0f;
                }
                this.mMotionLayout.touchAnimateTo(this.mOnTouchUp, f7, f5 * 3.0f);
                if (0.0f >= progress || 1.0f <= progress) {
                    this.mMotionLayout.setState(MotionLayout.TransitionState.FINISHED);
                    return;
                }
                return;
            case 2:
                float rawY2 = event.getRawY() - this.mLastTouchY;
                float rawX2 = event.getRawX() - this.mLastTouchX;
                float width2 = ((float) this.mMotionLayout.getWidth()) / 2.0f;
                float height2 = ((float) this.mMotionLayout.getHeight()) / 2.0f;
                int i5 = this.mRotationCenterId;
                if (i5 != -1) {
                    View findViewById3 = this.mMotionLayout.findViewById(i5);
                    this.mMotionLayout.getLocationOnScreen(this.mTempLoc);
                    width2 = ((float) this.mTempLoc[0]) + (((float) (findViewById3.getLeft() + findViewById3.getRight())) / 2.0f);
                    height2 = ((float) this.mTempLoc[1]) + (((float) (findViewById3.getTop() + findViewById3.getBottom())) / 2.0f);
                } else {
                    int i6 = this.mTouchAnchorId;
                    if (i6 != -1) {
                        View findViewById4 = this.mMotionLayout.findViewById(this.mMotionLayout.getMotionController(i6).getAnimateRelativeTo());
                        if (findViewById4 == null) {
                            Log.e(TAG, "could not find view to animate to");
                        } else {
                            this.mMotionLayout.getLocationOnScreen(this.mTempLoc);
                            width2 = ((float) this.mTempLoc[0]) + (((float) (findViewById4.getLeft() + findViewById4.getRight())) / 2.0f);
                            height2 = ((float) this.mTempLoc[1]) + (((float) (findViewById4.getTop() + findViewById4.getBottom())) / 2.0f);
                        }
                    }
                }
                float rawY3 = event.getRawY() - height2;
                double atan2 = Math.atan2((double) (event.getRawY() - height2), (double) (event.getRawX() - width2));
                float rawX3 = event.getRawX() - width2;
                double atan22 = Math.atan2((double) (this.mLastTouchY - height2), (double) (this.mLastTouchX - width2));
                float f8 = (float) (((atan2 - atan22) * 180.0d) / 3.141592653589793d);
                if (f8 > 330.0f) {
                    f8 -= 360.0f;
                } else if (f8 < -330.0f) {
                    f8 += 360.0f;
                }
                if (((double) Math.abs(f8)) > 0.01d || this.mDragStarted) {
                    float progress2 = this.mMotionLayout.getProgress();
                    if (!this.mDragStarted) {
                        this.mDragStarted = true;
                        this.mMotionLayout.setProgress(progress2);
                    }
                    int i7 = this.mTouchAnchorId;
                    if (i7 != -1) {
                        float f9 = rawY2;
                        this.mMotionLayout.getAnchorDpDt(i7, progress2, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
                        float[] fArr2 = this.mAnchorDpDt;
                        c = 1;
                        fArr2[1] = (float) Math.toDegrees((double) fArr2[1]);
                    } else {
                        float f10 = rawY2;
                        c = 1;
                        this.mAnchorDpDt[1] = 360.0f;
                    }
                    float f11 = (this.mDragScale * f8) / this.mAnchorDpDt[c];
                    float max = Math.max(Math.min(progress2 + f11, 1.0f), 0.0f);
                    float progress3 = this.mMotionLayout.getProgress();
                    if (max != progress3) {
                        if (progress3 == 0.0f || progress3 == 1.0f) {
                            this.mMotionLayout.endTrigger(progress3 == 0.0f);
                        }
                        this.mMotionLayout.setProgress(max);
                        motionTracker.computeCurrentVelocity(1000);
                        float xVelocity2 = velocityTracker.getXVelocity();
                        float yVelocity2 = velocityTracker.getYVelocity();
                        float f12 = f11;
                        float f13 = max;
                        double d = atan22;
                        float f14 = f8;
                        float f15 = progress3;
                        double d2 = atan2;
                        this.mMotionLayout.mLastVelocity = (float) Math.toDegrees((double) ((float) ((Math.hypot((double) yVelocity2, (double) xVelocity2) * Math.sin(Math.atan2((double) yVelocity2, (double) xVelocity2) - atan2)) / Math.hypot((double) rawX3, (double) rawY3))));
                    } else {
                        float f16 = f11;
                        float f17 = max;
                        double d3 = atan22;
                        double d4 = atan2;
                        float f18 = f8;
                        float f19 = progress3;
                        float f20 = rawY3;
                        this.mMotionLayout.mLastVelocity = 0.0f;
                    }
                    this.mLastTouchX = event.getRawX();
                    this.mLastTouchY = event.getRawY();
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: package-private */
    public void scrollMove(float dx, float dy) {
        float f = (this.mTouchDirectionX * dx) + (this.mTouchDirectionY * dy);
        float progress = this.mMotionLayout.getProgress();
        if (!this.mDragStarted) {
            this.mDragStarted = true;
            this.mMotionLayout.setProgress(progress);
        }
        this.mMotionLayout.getAnchorDpDt(this.mTouchAnchorId, progress, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
        float f2 = this.mTouchDirectionX;
        float[] fArr = this.mAnchorDpDt;
        if (((double) Math.abs((f2 * fArr[0]) + (this.mTouchDirectionY * fArr[1]))) < 0.01d) {
            float[] fArr2 = this.mAnchorDpDt;
            fArr2[0] = 0.01f;
            fArr2[1] = 0.01f;
        }
        float f3 = this.mTouchDirectionX;
        float max = Math.max(Math.min(progress + (f3 != 0.0f ? (f3 * dx) / this.mAnchorDpDt[0] : (this.mTouchDirectionY * dy) / this.mAnchorDpDt[1]), 1.0f), 0.0f);
        if (max != this.mMotionLayout.getProgress()) {
            this.mMotionLayout.setProgress(max);
        }
    }

    /* access modifiers changed from: package-private */
    public void scrollUp(float dx, float dy) {
        boolean z = false;
        this.mDragStarted = false;
        float progress = this.mMotionLayout.getProgress();
        this.mMotionLayout.getAnchorDpDt(this.mTouchAnchorId, progress, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
        float f = this.mTouchDirectionX;
        float[] fArr = this.mAnchorDpDt;
        float f2 = fArr[0];
        float f3 = this.mTouchDirectionY;
        float f4 = fArr[1];
        float f5 = (f * f2) + (f3 * f4);
        float f6 = 0.0f;
        float f7 = f != 0.0f ? (f * dx) / f2 : (f3 * dy) / f4;
        if (!Float.isNaN(f7)) {
            progress += f7 / 3.0f;
        }
        if (progress != 0.0f) {
            boolean z2 = progress != 1.0f;
            int i = this.mOnTouchUp;
            if (i != 3) {
                z = true;
            }
            if (z && z2) {
                MotionLayout motionLayout = this.mMotionLayout;
                if (((double) progress) >= 0.5d) {
                    f6 = 1.0f;
                }
                motionLayout.touchAnimateTo(i, f6, f7);
            }
        }
    }

    public void setAnchorId(int id) {
        this.mTouchAnchorId = id;
    }

    /* access modifiers changed from: package-private */
    public void setAutoCompleteMode(int autoCompleteMode) {
        this.mAutoCompleteMode = autoCompleteMode;
    }

    /* access modifiers changed from: package-private */
    public void setDown(float lastTouchX, float lastTouchY) {
        this.mLastTouchX = lastTouchX;
        this.mLastTouchY = lastTouchY;
    }

    public void setMaxAcceleration(float acceleration) {
        this.mMaxAcceleration = acceleration;
    }

    public void setMaxVelocity(float velocity) {
        this.mMaxVelocity = velocity;
    }

    public void setRTL(boolean rtl) {
        if (rtl) {
            float[][] fArr = TOUCH_DIRECTION;
            fArr[4] = fArr[3];
            fArr[5] = fArr[2];
            float[][] fArr2 = TOUCH_SIDES;
            fArr2[5] = fArr2[2];
            fArr2[6] = fArr2[1];
        } else {
            float[][] fArr3 = TOUCH_DIRECTION;
            fArr3[4] = fArr3[2];
            fArr3[5] = fArr3[3];
            float[][] fArr4 = TOUCH_SIDES;
            fArr4[5] = fArr4[1];
            fArr4[6] = fArr4[2];
        }
        float[] fArr5 = TOUCH_SIDES[this.mTouchAnchorSide];
        this.mTouchAnchorX = fArr5[0];
        this.mTouchAnchorY = fArr5[1];
        int i = this.mTouchSide;
        float[][] fArr6 = TOUCH_DIRECTION;
        if (i < fArr6.length) {
            float[] fArr7 = fArr6[i];
            this.mTouchDirectionX = fArr7[0];
            this.mTouchDirectionY = fArr7[1];
        }
    }

    public void setTouchAnchorLocation(float x, float y) {
        this.mTouchAnchorX = x;
        this.mTouchAnchorY = y;
    }

    public void setTouchUpMode(int touchUpMode) {
        this.mOnTouchUp = touchUpMode;
    }

    /* access modifiers changed from: package-private */
    public void setUpTouchEvent(float lastTouchX, float lastTouchY) {
        this.mLastTouchX = lastTouchX;
        this.mLastTouchY = lastTouchY;
        this.mDragStarted = false;
    }

    /* access modifiers changed from: package-private */
    public void setupTouch() {
        View view = null;
        int i = this.mTouchAnchorId;
        if (i != -1 && (view = this.mMotionLayout.findViewById(i)) == null) {
            StringBuilder append = new StringBuilder().append("cannot find TouchAnchorId @id/");
            String name = Debug.getName(this.mMotionLayout.getContext(), this.mTouchAnchorId);
            Log1F380D.a((Object) name);
            Log.e(TAG, append.append(name).toString());
        }
        if (view instanceof NestedScrollView) {
            NestedScrollView nestedScrollView = (NestedScrollView) view;
            nestedScrollView.setOnTouchListener(new View.OnTouchListener(this) {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(this) {
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                }
            });
        }
    }

    public String toString() {
        return Float.isNaN(this.mTouchDirectionX) ? Key.ROTATION : this.mTouchDirectionX + " , " + this.mTouchDirectionY;
    }
}
