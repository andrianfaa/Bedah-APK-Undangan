package androidx.constraintlayout.motion.widget;

public class OnSwipe {
    public static final int COMPLETE_MODE_CONTINUOUS_VELOCITY = 0;
    public static final int COMPLETE_MODE_SPRING = 1;
    public static final int DRAG_ANTICLOCKWISE = 7;
    public static final int DRAG_CLOCKWISE = 6;
    public static final int DRAG_DOWN = 1;
    public static final int DRAG_END = 5;
    public static final int DRAG_LEFT = 2;
    public static final int DRAG_RIGHT = 3;
    public static final int DRAG_START = 4;
    public static final int DRAG_UP = 0;
    public static final int FLAG_DISABLE_POST_SCROLL = 1;
    public static final int FLAG_DISABLE_SCROLL = 2;
    public static final int ON_UP_AUTOCOMPLETE = 0;
    public static final int ON_UP_AUTOCOMPLETE_TO_END = 2;
    public static final int ON_UP_AUTOCOMPLETE_TO_START = 1;
    public static final int ON_UP_DECELERATE = 4;
    public static final int ON_UP_DECELERATE_AND_COMPLETE = 5;
    public static final int ON_UP_NEVER_TO_END = 7;
    public static final int ON_UP_NEVER_TO_START = 6;
    public static final int ON_UP_STOP = 3;
    public static final int SIDE_BOTTOM = 3;
    public static final int SIDE_END = 6;
    public static final int SIDE_LEFT = 1;
    public static final int SIDE_MIDDLE = 4;
    public static final int SIDE_RIGHT = 2;
    public static final int SIDE_START = 5;
    public static final int SIDE_TOP = 0;
    public static final int SPRING_BOUNDARY_BOUNCEBOTH = 3;
    public static final int SPRING_BOUNDARY_BOUNCEEND = 2;
    public static final int SPRING_BOUNDARY_BOUNCESTART = 1;
    public static final int SPRING_BOUNDARY_OVERSHOOT = 0;
    private int mAutoCompleteMode = 0;
    private int mDragDirection = 0;
    private float mDragScale = 1.0f;
    private float mDragThreshold = 10.0f;
    private int mFlags = 0;
    private int mLimitBoundsTo = -1;
    private float mMaxAcceleration = 1.2f;
    private float mMaxVelocity = 4.0f;
    private boolean mMoveWhenScrollAtTop = true;
    private int mOnTouchUp = 0;
    private int mRotationCenterId = -1;
    private int mSpringBoundary = 0;
    private float mSpringDamping = Float.NaN;
    private float mSpringMass = 1.0f;
    private float mSpringStiffness = Float.NaN;
    private float mSpringStopThreshold = Float.NaN;
    private int mTouchAnchorId = -1;
    private int mTouchAnchorSide = 0;
    private int mTouchRegionId = -1;

    public int getAutoCompleteMode() {
        return this.mAutoCompleteMode;
    }

    public int getDragDirection() {
        return this.mDragDirection;
    }

    public float getDragScale() {
        return this.mDragScale;
    }

    public float getDragThreshold() {
        return this.mDragThreshold;
    }

    public int getLimitBoundsTo() {
        return this.mLimitBoundsTo;
    }

    public float getMaxAcceleration() {
        return this.mMaxAcceleration;
    }

    public float getMaxVelocity() {
        return this.mMaxVelocity;
    }

    public boolean getMoveWhenScrollAtTop() {
        return this.mMoveWhenScrollAtTop;
    }

    public int getNestedScrollFlags() {
        return this.mFlags;
    }

    public int getOnTouchUp() {
        return this.mOnTouchUp;
    }

    public int getRotationCenterId() {
        return this.mRotationCenterId;
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

    public int getTouchAnchorId() {
        return this.mTouchAnchorId;
    }

    public int getTouchAnchorSide() {
        return this.mTouchAnchorSide;
    }

    public int getTouchRegionId() {
        return this.mTouchRegionId;
    }

    public void setAutoCompleteMode(int autoCompleteMode) {
        this.mAutoCompleteMode = autoCompleteMode;
    }

    public OnSwipe setDragDirection(int dragDirection) {
        this.mDragDirection = dragDirection;
        return this;
    }

    public OnSwipe setDragScale(int dragScale) {
        this.mDragScale = (float) dragScale;
        return this;
    }

    public OnSwipe setDragThreshold(int dragThreshold) {
        this.mDragThreshold = (float) dragThreshold;
        return this;
    }

    public OnSwipe setLimitBoundsTo(int id) {
        this.mLimitBoundsTo = id;
        return this;
    }

    public OnSwipe setMaxAcceleration(int maxAcceleration) {
        this.mMaxAcceleration = (float) maxAcceleration;
        return this;
    }

    public OnSwipe setMaxVelocity(int maxVelocity) {
        this.mMaxVelocity = (float) maxVelocity;
        return this;
    }

    public OnSwipe setMoveWhenScrollAtTop(boolean moveWhenScrollAtTop) {
        this.mMoveWhenScrollAtTop = moveWhenScrollAtTop;
        return this;
    }

    public OnSwipe setNestedScrollFlags(int flags) {
        this.mFlags = flags;
        return this;
    }

    public OnSwipe setOnTouchUp(int mode) {
        this.mOnTouchUp = mode;
        return this;
    }

    public OnSwipe setRotateCenter(int rotationCenterId) {
        this.mRotationCenterId = rotationCenterId;
        return this;
    }

    public OnSwipe setSpringBoundary(int springBoundary) {
        this.mSpringBoundary = springBoundary;
        return this;
    }

    public OnSwipe setSpringDamping(float springDamping) {
        this.mSpringDamping = springDamping;
        return this;
    }

    public OnSwipe setSpringMass(float springMass) {
        this.mSpringMass = springMass;
        return this;
    }

    public OnSwipe setSpringStiffness(float springStiffness) {
        this.mSpringStiffness = springStiffness;
        return this;
    }

    public OnSwipe setSpringStopThreshold(float springStopThreshold) {
        this.mSpringStopThreshold = springStopThreshold;
        return this;
    }

    public OnSwipe setTouchAnchorId(int side) {
        this.mTouchAnchorId = side;
        return this;
    }

    public OnSwipe setTouchAnchorSide(int side) {
        this.mTouchAnchorSide = side;
        return this;
    }

    public OnSwipe setTouchRegionId(int side) {
        this.mTouchRegionId = side;
        return this;
    }
}
