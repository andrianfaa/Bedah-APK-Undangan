package androidx.customview.widget;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import androidx.core.view.ViewCompat;
import java.util.Arrays;

public class ViewDragHelper {
    private static final int BASE_SETTLE_DURATION = 256;
    public static final int DIRECTION_ALL = 3;
    public static final int DIRECTION_HORIZONTAL = 1;
    public static final int DIRECTION_VERTICAL = 2;
    public static final int EDGE_ALL = 15;
    public static final int EDGE_BOTTOM = 8;
    public static final int EDGE_LEFT = 1;
    public static final int EDGE_RIGHT = 2;
    private static final int EDGE_SIZE = 20;
    public static final int EDGE_TOP = 4;
    public static final int INVALID_POINTER = -1;
    private static final int MAX_SETTLE_DURATION = 600;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "ViewDragHelper";
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            float t2 = t - 1.0f;
            return (t2 * t2 * t2 * t2 * t2) + 1.0f;
        }
    };
    private int mActivePointerId = -1;
    private final Callback mCallback;
    private View mCapturedView;
    private final int mDefaultEdgeSize;
    private int mDragState;
    private int[] mEdgeDragsInProgress;
    private int[] mEdgeDragsLocked;
    private int mEdgeSize;
    private int[] mInitialEdgesTouched;
    private float[] mInitialMotionX;
    private float[] mInitialMotionY;
    private float[] mLastMotionX;
    private float[] mLastMotionY;
    private float mMaxVelocity;
    private float mMinVelocity;
    private final ViewGroup mParentView;
    private int mPointersDown;
    private boolean mReleaseInProgress;
    private OverScroller mScroller;
    private final Runnable mSetIdleRunnable = new Runnable() {
        public void run() {
            ViewDragHelper.this.setDragState(0);
        }
    };
    private int mTouchSlop;
    private int mTrackingEdges;
    private VelocityTracker mVelocityTracker;

    public static abstract class Callback {
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return 0;
        }

        public int clampViewPositionVertical(View child, int top, int dy) {
            return 0;
        }

        public int getOrderedChildIndex(int index) {
            return index;
        }

        public int getViewHorizontalDragRange(View child) {
            return 0;
        }

        public int getViewVerticalDragRange(View child) {
            return 0;
        }

        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
        }

        public boolean onEdgeLock(int edgeFlags) {
            return false;
        }

        public void onEdgeTouched(int edgeFlags, int pointerId) {
        }

        public void onViewCaptured(View capturedChild, int activePointerId) {
        }

        public void onViewDragStateChanged(int state) {
        }

        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
        }

        public void onViewReleased(View releasedChild, float xvel, float yvel) {
        }

        public abstract boolean tryCaptureView(View view, int i);
    }

    private ViewDragHelper(Context context, ViewGroup forParent, Callback cb) {
        if (forParent == null) {
            throw new IllegalArgumentException("Parent view may not be null");
        } else if (cb != null) {
            this.mParentView = forParent;
            this.mCallback = cb;
            ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
            int i = (int) ((20.0f * context.getResources().getDisplayMetrics().density) + 0.5f);
            this.mDefaultEdgeSize = i;
            this.mEdgeSize = i;
            this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
            this.mMaxVelocity = (float) viewConfiguration.getScaledMaximumFlingVelocity();
            this.mMinVelocity = (float) viewConfiguration.getScaledMinimumFlingVelocity();
            this.mScroller = new OverScroller(context, sInterpolator);
        } else {
            throw new IllegalArgumentException("Callback may not be null");
        }
    }

    private boolean checkNewEdgeDrag(float delta, float odelta, int pointerId, int edge) {
        float abs = Math.abs(delta);
        float abs2 = Math.abs(odelta);
        if (!((this.mInitialEdgesTouched[pointerId] & edge) != edge || (this.mTrackingEdges & edge) == 0 || (this.mEdgeDragsLocked[pointerId] & edge) == edge || (this.mEdgeDragsInProgress[pointerId] & edge) == edge)) {
            int i = this.mTouchSlop;
            if (abs > ((float) i) || abs2 > ((float) i)) {
                if (abs >= 0.5f * abs2 || !this.mCallback.onEdgeLock(edge)) {
                    return (this.mEdgeDragsInProgress[pointerId] & edge) == 0 && abs > ((float) this.mTouchSlop);
                }
                int[] iArr = this.mEdgeDragsLocked;
                iArr[pointerId] = iArr[pointerId] | edge;
                return false;
            }
        }
        return false;
    }

    private boolean checkTouchSlop(View child, float dx, float dy) {
        if (child == null) {
            return false;
        }
        boolean z = this.mCallback.getViewHorizontalDragRange(child) > 0;
        boolean z2 = this.mCallback.getViewVerticalDragRange(child) > 0;
        if (!z || !z2) {
            return z ? Math.abs(dx) > ((float) this.mTouchSlop) : z2 && Math.abs(dy) > ((float) this.mTouchSlop);
        }
        float f = (dx * dx) + (dy * dy);
        int i = this.mTouchSlop;
        return f > ((float) (i * i));
    }

    private float clampMag(float value, float absMin, float absMax) {
        float abs = Math.abs(value);
        if (abs < absMin) {
            return 0.0f;
        }
        return abs > absMax ? value > 0.0f ? absMax : -absMax : value;
    }

    private int clampMag(int value, int absMin, int absMax) {
        int abs = Math.abs(value);
        if (abs < absMin) {
            return 0;
        }
        return abs > absMax ? value > 0 ? absMax : -absMax : value;
    }

    private void clearMotionHistory() {
        float[] fArr = this.mInitialMotionX;
        if (fArr != null) {
            Arrays.fill(fArr, 0.0f);
            Arrays.fill(this.mInitialMotionY, 0.0f);
            Arrays.fill(this.mLastMotionX, 0.0f);
            Arrays.fill(this.mLastMotionY, 0.0f);
            Arrays.fill(this.mInitialEdgesTouched, 0);
            Arrays.fill(this.mEdgeDragsInProgress, 0);
            Arrays.fill(this.mEdgeDragsLocked, 0);
            this.mPointersDown = 0;
        }
    }

    private void clearMotionHistory(int pointerId) {
        if (this.mInitialMotionX != null && isPointerDown(pointerId)) {
            this.mInitialMotionX[pointerId] = 0.0f;
            this.mInitialMotionY[pointerId] = 0.0f;
            this.mLastMotionX[pointerId] = 0.0f;
            this.mLastMotionY[pointerId] = 0.0f;
            this.mInitialEdgesTouched[pointerId] = 0;
            this.mEdgeDragsInProgress[pointerId] = 0;
            this.mEdgeDragsLocked[pointerId] = 0;
            this.mPointersDown &= ~(1 << pointerId);
        }
    }

    private int computeAxisDuration(int delta, int velocity, int motionRange) {
        if (delta == 0) {
            return 0;
        }
        int width = this.mParentView.getWidth();
        int i = width / 2;
        float distanceInfluenceForSnapDuration = ((float) i) + (((float) i) * distanceInfluenceForSnapDuration(Math.min(1.0f, ((float) Math.abs(delta)) / ((float) width))));
        int velocity2 = Math.abs(velocity);
        return Math.min(velocity2 > 0 ? Math.round(Math.abs(distanceInfluenceForSnapDuration / ((float) velocity2)) * 1000.0f) * 4 : (int) ((1.0f + (((float) Math.abs(delta)) / ((float) motionRange))) * 256.0f), 600);
    }

    private int computeSettleDuration(View child, int dx, int dy, int xvel, int yvel) {
        View view = child;
        int clampMag = clampMag(xvel, (int) this.mMinVelocity, (int) this.mMaxVelocity);
        int clampMag2 = clampMag(yvel, (int) this.mMinVelocity, (int) this.mMaxVelocity);
        int abs = Math.abs(dx);
        int abs2 = Math.abs(dy);
        int abs3 = Math.abs(clampMag);
        int abs4 = Math.abs(clampMag2);
        int i = abs3 + abs4;
        int i2 = abs + abs2;
        return (int) ((((float) computeAxisDuration(dx, clampMag, this.mCallback.getViewHorizontalDragRange(view))) * (clampMag != 0 ? ((float) abs3) / ((float) i) : ((float) abs) / ((float) i2))) + (((float) computeAxisDuration(dy, clampMag2, this.mCallback.getViewVerticalDragRange(view))) * (clampMag2 != 0 ? ((float) abs4) / ((float) i) : ((float) abs2) / ((float) i2))));
    }

    public static ViewDragHelper create(ViewGroup forParent, float sensitivity, Callback cb) {
        ViewDragHelper create = create(forParent, cb);
        create.mTouchSlop = (int) (((float) create.mTouchSlop) * (1.0f / sensitivity));
        return create;
    }

    public static ViewDragHelper create(ViewGroup forParent, Callback cb) {
        return new ViewDragHelper(forParent.getContext(), forParent, cb);
    }

    private void dispatchViewReleased(float xvel, float yvel) {
        this.mReleaseInProgress = true;
        this.mCallback.onViewReleased(this.mCapturedView, xvel, yvel);
        this.mReleaseInProgress = false;
        if (this.mDragState == 1) {
            setDragState(0);
        }
    }

    private float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((double) ((f - 0.5f) * 0.47123894f));
    }

    private void dragTo(int left, int top, int dx, int dy) {
        int i = dx;
        int i2 = dy;
        int i3 = left;
        int i4 = top;
        int left2 = this.mCapturedView.getLeft();
        int top2 = this.mCapturedView.getTop();
        if (i != 0) {
            i3 = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, left, i);
            ViewCompat.offsetLeftAndRight(this.mCapturedView, i3 - left2);
        } else {
            int i5 = left;
        }
        if (i2 != 0) {
            i4 = this.mCallback.clampViewPositionVertical(this.mCapturedView, top, i2);
            ViewCompat.offsetTopAndBottom(this.mCapturedView, i4 - top2);
        } else {
            int i6 = top;
        }
        if (i != 0 || i2 != 0) {
            this.mCallback.onViewPositionChanged(this.mCapturedView, i3, i4, i3 - left2, i4 - top2);
        }
    }

    private void ensureMotionHistorySizeForId(int pointerId) {
        float[] fArr = this.mInitialMotionX;
        if (fArr == null || fArr.length <= pointerId) {
            float[] fArr2 = new float[(pointerId + 1)];
            float[] fArr3 = new float[(pointerId + 1)];
            float[] fArr4 = new float[(pointerId + 1)];
            float[] fArr5 = new float[(pointerId + 1)];
            int[] iArr = new int[(pointerId + 1)];
            int[] iArr2 = new int[(pointerId + 1)];
            int[] iArr3 = new int[(pointerId + 1)];
            if (fArr != null) {
                System.arraycopy(fArr, 0, fArr2, 0, fArr.length);
                float[] fArr6 = this.mInitialMotionY;
                System.arraycopy(fArr6, 0, fArr3, 0, fArr6.length);
                float[] fArr7 = this.mLastMotionX;
                System.arraycopy(fArr7, 0, fArr4, 0, fArr7.length);
                float[] fArr8 = this.mLastMotionY;
                System.arraycopy(fArr8, 0, fArr5, 0, fArr8.length);
                int[] iArr4 = this.mInitialEdgesTouched;
                System.arraycopy(iArr4, 0, iArr, 0, iArr4.length);
                int[] iArr5 = this.mEdgeDragsInProgress;
                System.arraycopy(iArr5, 0, iArr2, 0, iArr5.length);
                int[] iArr6 = this.mEdgeDragsLocked;
                System.arraycopy(iArr6, 0, iArr3, 0, iArr6.length);
            }
            this.mInitialMotionX = fArr2;
            this.mInitialMotionY = fArr3;
            this.mLastMotionX = fArr4;
            this.mLastMotionY = fArr5;
            this.mInitialEdgesTouched = iArr;
            this.mEdgeDragsInProgress = iArr2;
            this.mEdgeDragsLocked = iArr3;
        }
    }

    private boolean forceSettleCapturedViewAt(int finalLeft, int finalTop, int xvel, int yvel) {
        int left = this.mCapturedView.getLeft();
        int top = this.mCapturedView.getTop();
        int i = finalLeft - left;
        int i2 = finalTop - top;
        if (i == 0 && i2 == 0) {
            this.mScroller.abortAnimation();
            setDragState(0);
            return false;
        }
        this.mScroller.startScroll(left, top, i, i2, computeSettleDuration(this.mCapturedView, i, i2, xvel, yvel));
        setDragState(2);
        return true;
    }

    private int getEdgesTouched(int x, int y) {
        int i = 0;
        if (x < this.mParentView.getLeft() + this.mEdgeSize) {
            i = 0 | 1;
        }
        if (y < this.mParentView.getTop() + this.mEdgeSize) {
            i |= 4;
        }
        if (x > this.mParentView.getRight() - this.mEdgeSize) {
            i |= 2;
        }
        return y > this.mParentView.getBottom() - this.mEdgeSize ? i | 8 : i;
    }

    private boolean isValidPointerForActionMove(int pointerId) {
        if (isPointerDown(pointerId)) {
            return true;
        }
        Log.e(TAG, "Ignoring pointerId=" + pointerId + " because ACTION_DOWN was not received for this pointer before ACTION_MOVE. It likely happened because  ViewDragHelper did not receive all the events in the event stream.");
        return false;
    }

    private void releaseViewForPointerUp() {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
        dispatchViewReleased(clampMag(this.mVelocityTracker.getXVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), clampMag(this.mVelocityTracker.getYVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
    }

    private void reportNewEdgeDrags(float dx, float dy, int pointerId) {
        int i = 0;
        if (checkNewEdgeDrag(dx, dy, pointerId, 1)) {
            i = 0 | 1;
        }
        if (checkNewEdgeDrag(dy, dx, pointerId, 4)) {
            i |= 4;
        }
        if (checkNewEdgeDrag(dx, dy, pointerId, 2)) {
            i |= 2;
        }
        if (checkNewEdgeDrag(dy, dx, pointerId, 8)) {
            i |= 8;
        }
        if (i != 0) {
            int[] iArr = this.mEdgeDragsInProgress;
            iArr[pointerId] = iArr[pointerId] | i;
            this.mCallback.onEdgeDragStarted(i, pointerId);
        }
    }

    private void saveInitialMotion(float x, float y, int pointerId) {
        ensureMotionHistorySizeForId(pointerId);
        float[] fArr = this.mInitialMotionX;
        this.mLastMotionX[pointerId] = x;
        fArr[pointerId] = x;
        float[] fArr2 = this.mInitialMotionY;
        this.mLastMotionY[pointerId] = y;
        fArr2[pointerId] = y;
        this.mInitialEdgesTouched[pointerId] = getEdgesTouched((int) x, (int) y);
        this.mPointersDown |= 1 << pointerId;
    }

    private void saveLastMotion(MotionEvent ev) {
        int pointerCount = ev.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            int pointerId = ev.getPointerId(i);
            if (isValidPointerForActionMove(pointerId)) {
                float x = ev.getX(i);
                float y = ev.getY(i);
                this.mLastMotionX[pointerId] = x;
                this.mLastMotionY[pointerId] = y;
            }
        }
    }

    public void abort() {
        cancel();
        if (this.mDragState == 2) {
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            this.mScroller.abortAnimation();
            int currX2 = this.mScroller.getCurrX();
            int currY2 = this.mScroller.getCurrY();
            this.mCallback.onViewPositionChanged(this.mCapturedView, currX2, currY2, currX2 - currX, currY2 - currY);
        }
        setDragState(0);
    }

    /* access modifiers changed from: protected */
    public boolean canScroll(View v, boolean checkV, int dx, int dy, int x, int y) {
        View view = v;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = viewGroup.getChildAt(childCount);
                if (x + scrollX >= childAt.getLeft() && x + scrollX < childAt.getRight() && y + scrollY >= childAt.getTop() && y + scrollY < childAt.getBottom()) {
                    if (canScroll(childAt, true, dx, dy, (x + scrollX) - childAt.getLeft(), (y + scrollY) - childAt.getTop())) {
                        return true;
                    }
                }
            }
        }
        if (!checkV) {
            int i = dx;
            int i2 = dy;
        } else if (view.canScrollHorizontally(-dx)) {
            int i3 = dy;
            return true;
        } else if (view.canScrollVertically(-dy)) {
            return true;
        }
        return false;
    }

    public void cancel() {
        this.mActivePointerId = -1;
        clearMotionHistory();
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void captureChildView(View childView, int activePointerId) {
        if (childView.getParent() == this.mParentView) {
            this.mCapturedView = childView;
            this.mActivePointerId = activePointerId;
            this.mCallback.onViewCaptured(childView, activePointerId);
            setDragState(1);
            return;
        }
        throw new IllegalArgumentException("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (" + this.mParentView + ")");
    }

    public boolean checkTouchSlop(int directions) {
        int length = this.mInitialMotionX.length;
        for (int i = 0; i < length; i++) {
            if (checkTouchSlop(directions, i)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkTouchSlop(int directions, int pointerId) {
        if (!isPointerDown(pointerId)) {
            return false;
        }
        boolean z = (directions & 1) == 1;
        boolean z2 = (directions & 2) == 2;
        float f = this.mLastMotionX[pointerId] - this.mInitialMotionX[pointerId];
        float f2 = this.mLastMotionY[pointerId] - this.mInitialMotionY[pointerId];
        if (!z || !z2) {
            return z ? Math.abs(f) > ((float) this.mTouchSlop) : z2 && Math.abs(f2) > ((float) this.mTouchSlop);
        }
        float f3 = (f * f) + (f2 * f2);
        int i = this.mTouchSlop;
        return f3 > ((float) (i * i));
    }

    public boolean continueSettling(boolean deferCallbacks) {
        if (this.mDragState == 2) {
            boolean computeScrollOffset = this.mScroller.computeScrollOffset();
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            int left = currX - this.mCapturedView.getLeft();
            int top = currY - this.mCapturedView.getTop();
            if (left != 0) {
                ViewCompat.offsetLeftAndRight(this.mCapturedView, left);
            }
            if (top != 0) {
                ViewCompat.offsetTopAndBottom(this.mCapturedView, top);
            }
            if (!(left == 0 && top == 0)) {
                this.mCallback.onViewPositionChanged(this.mCapturedView, currX, currY, left, top);
            }
            if (computeScrollOffset && currX == this.mScroller.getFinalX() && currY == this.mScroller.getFinalY()) {
                this.mScroller.abortAnimation();
                computeScrollOffset = false;
            }
            if (!computeScrollOffset) {
                if (deferCallbacks) {
                    this.mParentView.post(this.mSetIdleRunnable);
                } else {
                    setDragState(0);
                }
            }
        }
        return this.mDragState == 2;
    }

    public View findTopChildUnder(int x, int y) {
        for (int childCount = this.mParentView.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(childCount));
            if (x >= childAt.getLeft() && x < childAt.getRight() && y >= childAt.getTop() && y < childAt.getBottom()) {
                return childAt;
            }
        }
        return null;
    }

    public void flingCapturedView(int minLeft, int minTop, int maxLeft, int maxTop) {
        if (this.mReleaseInProgress) {
            this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int) this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int) this.mVelocityTracker.getYVelocity(this.mActivePointerId), minLeft, maxLeft, minTop, maxTop);
            setDragState(2);
            return;
        }
        throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
    }

    public int getActivePointerId() {
        return this.mActivePointerId;
    }

    public View getCapturedView() {
        return this.mCapturedView;
    }

    public int getDefaultEdgeSize() {
        return this.mDefaultEdgeSize;
    }

    public int getEdgeSize() {
        return this.mEdgeSize;
    }

    public float getMinVelocity() {
        return this.mMinVelocity;
    }

    public int getTouchSlop() {
        return this.mTouchSlop;
    }

    public int getViewDragState() {
        return this.mDragState;
    }

    public boolean isCapturedViewUnder(int x, int y) {
        return isViewUnder(this.mCapturedView, x, y);
    }

    public boolean isEdgeTouched(int edges) {
        int length = this.mInitialEdgesTouched.length;
        for (int i = 0; i < length; i++) {
            if (isEdgeTouched(edges, i)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEdgeTouched(int edges, int pointerId) {
        return isPointerDown(pointerId) && (this.mInitialEdgesTouched[pointerId] & edges) != 0;
    }

    public boolean isPointerDown(int pointerId) {
        return (this.mPointersDown & (1 << pointerId)) != 0;
    }

    public boolean isViewUnder(View view, int x, int y) {
        return view != null && x >= view.getLeft() && x < view.getRight() && y >= view.getTop() && y < view.getBottom();
    }

    public void processTouchEvent(MotionEvent ev) {
        int actionMasked = ev.getActionMasked();
        int actionIndex = ev.getActionIndex();
        if (actionMasked == 0) {
            cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(ev);
        switch (actionMasked) {
            case 0:
                float x = ev.getX();
                float y = ev.getY();
                int pointerId = ev.getPointerId(0);
                View findTopChildUnder = findTopChildUnder((int) x, (int) y);
                saveInitialMotion(x, y, pointerId);
                tryCaptureViewForDrag(findTopChildUnder, pointerId);
                int i = this.mInitialEdgesTouched[pointerId];
                int i2 = this.mTrackingEdges;
                if ((i & i2) != 0) {
                    this.mCallback.onEdgeTouched(i2 & i, pointerId);
                    return;
                }
                return;
            case 1:
                if (this.mDragState == 1) {
                    releaseViewForPointerUp();
                }
                cancel();
                return;
            case 2:
                if (this.mDragState != 1) {
                    int pointerCount = ev.getPointerCount();
                    for (int i3 = 0; i3 < pointerCount; i3++) {
                        int pointerId2 = ev.getPointerId(i3);
                        if (isValidPointerForActionMove(pointerId2)) {
                            float x2 = ev.getX(i3);
                            float y2 = ev.getY(i3);
                            float f = x2 - this.mInitialMotionX[pointerId2];
                            float f2 = y2 - this.mInitialMotionY[pointerId2];
                            reportNewEdgeDrags(f, f2, pointerId2);
                            if (this.mDragState != 1) {
                                View findTopChildUnder2 = findTopChildUnder((int) x2, (int) y2);
                                if (checkTouchSlop(findTopChildUnder2, f, f2) && tryCaptureViewForDrag(findTopChildUnder2, pointerId2)) {
                                }
                            }
                            saveLastMotion(ev);
                            return;
                        }
                    }
                    saveLastMotion(ev);
                    return;
                } else if (isValidPointerForActionMove(this.mActivePointerId)) {
                    int findPointerIndex = ev.findPointerIndex(this.mActivePointerId);
                    float x3 = ev.getX(findPointerIndex);
                    float y3 = ev.getY(findPointerIndex);
                    float[] fArr = this.mLastMotionX;
                    int i4 = this.mActivePointerId;
                    int i5 = (int) (x3 - fArr[i4]);
                    int i6 = (int) (y3 - this.mLastMotionY[i4]);
                    dragTo(this.mCapturedView.getLeft() + i5, this.mCapturedView.getTop() + i6, i5, i6);
                    saveLastMotion(ev);
                    return;
                } else {
                    return;
                }
            case 3:
                if (this.mDragState == 1) {
                    dispatchViewReleased(0.0f, 0.0f);
                }
                cancel();
                return;
            case 5:
                int pointerId3 = ev.getPointerId(actionIndex);
                float x4 = ev.getX(actionIndex);
                float y4 = ev.getY(actionIndex);
                saveInitialMotion(x4, y4, pointerId3);
                if (this.mDragState == 0) {
                    tryCaptureViewForDrag(findTopChildUnder((int) x4, (int) y4), pointerId3);
                    int i7 = this.mInitialEdgesTouched[pointerId3];
                    int i8 = this.mTrackingEdges;
                    if ((i7 & i8) != 0) {
                        this.mCallback.onEdgeTouched(i8 & i7, pointerId3);
                        return;
                    }
                    return;
                } else if (isCapturedViewUnder((int) x4, (int) y4)) {
                    tryCaptureViewForDrag(this.mCapturedView, pointerId3);
                    return;
                } else {
                    return;
                }
            case 6:
                int pointerId4 = ev.getPointerId(actionIndex);
                if (this.mDragState == 1 && pointerId4 == this.mActivePointerId) {
                    int i9 = -1;
                    int pointerCount2 = ev.getPointerCount();
                    int i10 = 0;
                    while (true) {
                        if (i10 < pointerCount2) {
                            int pointerId5 = ev.getPointerId(i10);
                            if (pointerId5 != this.mActivePointerId) {
                                View findTopChildUnder3 = findTopChildUnder((int) ev.getX(i10), (int) ev.getY(i10));
                                View view = this.mCapturedView;
                                if (findTopChildUnder3 == view && tryCaptureViewForDrag(view, pointerId5)) {
                                    i9 = this.mActivePointerId;
                                }
                            }
                            i10++;
                        }
                    }
                    if (i9 == -1) {
                        releaseViewForPointerUp();
                    }
                }
                clearMotionHistory(pointerId4);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: package-private */
    public void setDragState(int state) {
        this.mParentView.removeCallbacks(this.mSetIdleRunnable);
        if (this.mDragState != state) {
            this.mDragState = state;
            this.mCallback.onViewDragStateChanged(state);
            if (this.mDragState == 0) {
                this.mCapturedView = null;
            }
        }
    }

    public void setEdgeSize(int size) {
        this.mEdgeSize = size;
    }

    public void setEdgeTrackingEnabled(int edgeFlags) {
        this.mTrackingEdges = edgeFlags;
    }

    public void setMinVelocity(float minVel) {
        this.mMinVelocity = minVel;
    }

    public boolean settleCapturedViewAt(int finalLeft, int finalTop) {
        if (this.mReleaseInProgress) {
            return forceSettleCapturedViewAt(finalLeft, finalTop, (int) this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int) this.mVelocityTracker.getYVelocity(this.mActivePointerId));
        }
        throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00fd, code lost:
        if (r2 != r15) goto L_0x010c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean shouldInterceptTouchEvent(android.view.MotionEvent r22) {
        /*
            r21 = this;
            r0 = r21
            r1 = r22
            int r2 = r22.getActionMasked()
            int r3 = r22.getActionIndex()
            if (r2 != 0) goto L_0x0011
            r21.cancel()
        L_0x0011:
            android.view.VelocityTracker r4 = r0.mVelocityTracker
            if (r4 != 0) goto L_0x001b
            android.view.VelocityTracker r4 = android.view.VelocityTracker.obtain()
            r0.mVelocityTracker = r4
        L_0x001b:
            android.view.VelocityTracker r4 = r0.mVelocityTracker
            r4.addMovement(r1)
            r4 = 2
            switch(r2) {
                case 0: goto L_0x0142;
                case 1: goto L_0x0139;
                case 2: goto L_0x007a;
                case 3: goto L_0x0139;
                case 4: goto L_0x0024;
                case 5: goto L_0x0039;
                case 6: goto L_0x002b;
                default: goto L_0x0024;
            }
        L_0x0024:
            r16 = r2
            r17 = r3
            r5 = 0
            goto L_0x0177
        L_0x002b:
            int r4 = r1.getPointerId(r3)
            r0.clearMotionHistory(r4)
            r16 = r2
            r17 = r3
            r5 = 0
            goto L_0x0177
        L_0x0039:
            int r7 = r1.getPointerId(r3)
            float r8 = r1.getX(r3)
            float r9 = r1.getY(r3)
            r0.saveInitialMotion(r8, r9, r7)
            int r10 = r0.mDragState
            if (r10 != 0) goto L_0x005d
            int[] r4 = r0.mInitialEdgesTouched
            r4 = r4[r7]
            int r10 = r0.mTrackingEdges
            r11 = r4 & r10
            if (r11 == 0) goto L_0x0073
            androidx.customview.widget.ViewDragHelper$Callback r11 = r0.mCallback
            r10 = r10 & r4
            r11.onEdgeTouched(r10, r7)
            goto L_0x0073
        L_0x005d:
            if (r10 != r4) goto L_0x0073
            int r4 = (int) r8
            int r10 = (int) r9
            android.view.View r4 = r0.findTopChildUnder(r4, r10)
            android.view.View r10 = r0.mCapturedView
            if (r4 != r10) goto L_0x006c
            r0.tryCaptureViewForDrag(r4, r7)
        L_0x006c:
            r16 = r2
            r17 = r3
            r5 = 0
            goto L_0x0177
        L_0x0073:
            r16 = r2
            r17 = r3
            r5 = 0
            goto L_0x0177
        L_0x007a:
            float[] r4 = r0.mInitialMotionX
            if (r4 == 0) goto L_0x0133
            float[] r4 = r0.mInitialMotionY
            if (r4 != 0) goto L_0x0089
            r16 = r2
            r17 = r3
            r5 = 0
            goto L_0x0177
        L_0x0089:
            int r4 = r22.getPointerCount()
            r7 = 0
        L_0x008e:
            if (r7 >= r4) goto L_0x0128
            int r8 = r1.getPointerId(r7)
            boolean r9 = r0.isValidPointerForActionMove(r8)
            if (r9 != 0) goto L_0x00a2
            r16 = r2
            r17 = r3
            r18 = r4
            goto L_0x011e
        L_0x00a2:
            float r9 = r1.getX(r7)
            float r10 = r1.getY(r7)
            float[] r11 = r0.mInitialMotionX
            r11 = r11[r8]
            float r11 = r9 - r11
            float[] r12 = r0.mInitialMotionY
            r12 = r12[r8]
            float r12 = r10 - r12
            int r13 = (int) r9
            int r14 = (int) r10
            android.view.View r13 = r0.findTopChildUnder(r13, r14)
            if (r13 == 0) goto L_0x00c6
            boolean r14 = r0.checkTouchSlop(r13, r11, r12)
            if (r14 == 0) goto L_0x00c6
            r14 = 1
            goto L_0x00c7
        L_0x00c6:
            r14 = 0
        L_0x00c7:
            if (r14 == 0) goto L_0x0106
            int r15 = r13.getLeft()
            int r5 = (int) r11
            int r5 = r5 + r15
            androidx.customview.widget.ViewDragHelper$Callback r6 = r0.mCallback
            r16 = r2
            int r2 = (int) r11
            int r2 = r6.clampViewPositionHorizontal(r13, r5, r2)
            int r6 = r13.getTop()
            r17 = r3
            int r3 = (int) r12
            int r3 = r3 + r6
            r18 = r4
            androidx.customview.widget.ViewDragHelper$Callback r4 = r0.mCallback
            r19 = r5
            int r5 = (int) r12
            int r4 = r4.clampViewPositionVertical(r13, r3, r5)
            androidx.customview.widget.ViewDragHelper$Callback r5 = r0.mCallback
            int r5 = r5.getViewHorizontalDragRange(r13)
            r20 = r3
            androidx.customview.widget.ViewDragHelper$Callback r3 = r0.mCallback
            int r3 = r3.getViewVerticalDragRange(r13)
            if (r5 == 0) goto L_0x00ff
            if (r5 <= 0) goto L_0x010c
            if (r2 != r15) goto L_0x010c
        L_0x00ff:
            if (r3 == 0) goto L_0x012e
            if (r3 <= 0) goto L_0x010c
            if (r4 != r6) goto L_0x010c
            goto L_0x012e
        L_0x0106:
            r16 = r2
            r17 = r3
            r18 = r4
        L_0x010c:
            r0.reportNewEdgeDrags(r11, r12, r8)
            int r2 = r0.mDragState
            r3 = 1
            if (r2 != r3) goto L_0x0115
            goto L_0x012e
        L_0x0115:
            if (r14 == 0) goto L_0x011e
            boolean r2 = r0.tryCaptureViewForDrag(r13, r8)
            if (r2 == 0) goto L_0x011e
            goto L_0x012e
        L_0x011e:
            int r7 = r7 + 1
            r2 = r16
            r3 = r17
            r4 = r18
            goto L_0x008e
        L_0x0128:
            r16 = r2
            r17 = r3
            r18 = r4
        L_0x012e:
            r21.saveLastMotion(r22)
            r5 = 0
            goto L_0x0177
        L_0x0133:
            r16 = r2
            r17 = r3
            r5 = 0
            goto L_0x0177
        L_0x0139:
            r16 = r2
            r17 = r3
            r21.cancel()
            r5 = 0
            goto L_0x0177
        L_0x0142:
            r16 = r2
            r17 = r3
            float r2 = r22.getX()
            float r3 = r22.getY()
            r5 = 0
            int r6 = r1.getPointerId(r5)
            r0.saveInitialMotion(r2, r3, r6)
            int r7 = (int) r2
            int r8 = (int) r3
            android.view.View r7 = r0.findTopChildUnder(r7, r8)
            android.view.View r8 = r0.mCapturedView
            if (r7 != r8) goto L_0x0167
            int r8 = r0.mDragState
            if (r8 != r4) goto L_0x0167
            r0.tryCaptureViewForDrag(r7, r6)
        L_0x0167:
            int[] r4 = r0.mInitialEdgesTouched
            r4 = r4[r6]
            int r8 = r0.mTrackingEdges
            r9 = r4 & r8
            if (r9 == 0) goto L_0x0177
            androidx.customview.widget.ViewDragHelper$Callback r9 = r0.mCallback
            r8 = r8 & r4
            r9.onEdgeTouched(r8, r6)
        L_0x0177:
            int r2 = r0.mDragState
            r3 = 1
            if (r2 != r3) goto L_0x017d
            r5 = r3
        L_0x017d:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.customview.widget.ViewDragHelper.shouldInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    public boolean smoothSlideViewTo(View child, int finalLeft, int finalTop) {
        this.mCapturedView = child;
        this.mActivePointerId = -1;
        boolean forceSettleCapturedViewAt = forceSettleCapturedViewAt(finalLeft, finalTop, 0, 0);
        if (!forceSettleCapturedViewAt && this.mDragState == 0 && this.mCapturedView != null) {
            this.mCapturedView = null;
        }
        return forceSettleCapturedViewAt;
    }

    /* access modifiers changed from: package-private */
    public boolean tryCaptureViewForDrag(View toCapture, int pointerId) {
        if (toCapture == this.mCapturedView && this.mActivePointerId == pointerId) {
            return true;
        }
        if (toCapture == null || !this.mCallback.tryCaptureView(toCapture, pointerId)) {
            return false;
        }
        this.mActivePointerId = pointerId;
        captureChildView(toCapture, pointerId);
        return true;
    }
}
