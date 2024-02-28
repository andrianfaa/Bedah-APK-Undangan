package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.TypedValue;
import android.util.Xml;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import androidx.constraintlayout.core.motion.utils.Easing;
import androidx.constraintlayout.core.motion.utils.KeyCache;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.R;
import java.util.ArrayList;
import java.util.Iterator;
import mt.Log1F380D;
import org.xmlpull.v1.XmlPullParser;

/* compiled from: 0025 */
public class ViewTransition {
    static final int ANTICIPATE = 6;
    static final int BOUNCE = 4;
    public static final String CONSTRAINT_OVERRIDE = "ConstraintOverride";
    public static final String CUSTOM_ATTRIBUTE = "CustomAttribute";
    public static final String CUSTOM_METHOD = "CustomMethod";
    static final int EASE_IN = 1;
    static final int EASE_IN_OUT = 0;
    static final int EASE_OUT = 2;
    private static final int INTERPOLATOR_REFERENCE_ID = -2;
    public static final String KEY_FRAME_SET_TAG = "KeyFrameSet";
    static final int LINEAR = 3;
    public static final int ONSTATE_ACTION_DOWN = 1;
    public static final int ONSTATE_ACTION_DOWN_UP = 3;
    public static final int ONSTATE_ACTION_UP = 2;
    public static final int ONSTATE_SHARED_VALUE_SET = 4;
    public static final int ONSTATE_SHARED_VALUE_UNSET = 5;
    static final int OVERSHOOT = 5;
    private static final int SPLINE_STRING = -1;
    private static String TAG = VIEW_TRANSITION_TAG;
    private static final int UNSET = -1;
    static final int VIEWTRANSITIONMODE_ALLSTATES = 1;
    static final int VIEWTRANSITIONMODE_CURRENTSTATE = 0;
    static final int VIEWTRANSITIONMODE_NOSTATE = 2;
    public static final String VIEW_TRANSITION_TAG = "ViewTransition";
    private int mClearsTag = -1;
    ConstraintSet.Constraint mConstraintDelta;
    Context mContext;
    private int mDefaultInterpolator = 0;
    private int mDefaultInterpolatorID = -1;
    private String mDefaultInterpolatorString = null;
    private boolean mDisabled = false;
    private int mDuration = -1;
    private int mId;
    private int mIfTagNotSet = -1;
    private int mIfTagSet = -1;
    KeyFrames mKeyFrames;
    private int mOnStateTransition = -1;
    private int mPathMotionArc = 0;
    private int mSetsTag = -1;
    private int mSharedValueCurrent = -1;
    private int mSharedValueID = -1;
    private int mSharedValueTarget = -1;
    private int mTargetId;
    private String mTargetString;
    private int mUpDuration = -1;
    int mViewTransitionMode;
    ConstraintSet set;

    static class Animate {
        boolean hold_at_100 = false;
        KeyCache mCache = new KeyCache();
        private final int mClearsTag;
        float mDpositionDt;
        int mDuration;
        Interpolator mInterpolator;
        long mLastRender;
        MotionController mMC;
        float mPosition;
        private final int mSetsTag;
        long mStart;
        Rect mTempRec = new Rect();
        int mUpDuration;
        ViewTransitionController mVtController;
        boolean reverse = false;

        Animate(ViewTransitionController controller, MotionController motionController, int duration, int upDuration, int mode, Interpolator interpolator, int setTag, int clearTag) {
            this.mVtController = controller;
            this.mMC = motionController;
            this.mDuration = duration;
            this.mUpDuration = upDuration;
            long nanoTime = System.nanoTime();
            this.mStart = nanoTime;
            this.mLastRender = nanoTime;
            this.mVtController.addAnimation(this);
            this.mInterpolator = interpolator;
            this.mSetsTag = setTag;
            this.mClearsTag = clearTag;
            if (mode == 3) {
                this.hold_at_100 = true;
            }
            this.mDpositionDt = duration == 0 ? Float.MAX_VALUE : 1.0f / ((float) duration);
            mutate();
        }

        /* access modifiers changed from: package-private */
        public void mutate() {
            if (this.reverse) {
                mutateReverse();
            } else {
                mutateForward();
            }
        }

        /* access modifiers changed from: package-private */
        public void mutateForward() {
            long nanoTime = System.nanoTime();
            long j = nanoTime - this.mLastRender;
            this.mLastRender = nanoTime;
            float f = this.mPosition + (((float) (((double) j) * 1.0E-6d)) * this.mDpositionDt);
            this.mPosition = f;
            if (f >= 1.0f) {
                this.mPosition = 1.0f;
            }
            Interpolator interpolator = this.mInterpolator;
            float interpolation = interpolator == null ? this.mPosition : interpolator.getInterpolation(this.mPosition);
            MotionController motionController = this.mMC;
            boolean interpolate = motionController.interpolate(motionController.mView, interpolation, nanoTime, this.mCache);
            if (this.mPosition >= 1.0f) {
                if (this.mSetsTag != -1) {
                    this.mMC.getView().setTag(this.mSetsTag, Long.valueOf(System.nanoTime()));
                }
                if (this.mClearsTag != -1) {
                    this.mMC.getView().setTag(this.mClearsTag, (Object) null);
                }
                if (!this.hold_at_100) {
                    this.mVtController.removeAnimation(this);
                }
            }
            if (this.mPosition < 1.0f || interpolate) {
                this.mVtController.invalidate();
            }
        }

        /* access modifiers changed from: package-private */
        public void mutateReverse() {
            long nanoTime = System.nanoTime();
            long j = nanoTime - this.mLastRender;
            this.mLastRender = nanoTime;
            float f = this.mPosition - (((float) (((double) j) * 1.0E-6d)) * this.mDpositionDt);
            this.mPosition = f;
            if (f < 0.0f) {
                this.mPosition = 0.0f;
            }
            Interpolator interpolator = this.mInterpolator;
            float interpolation = interpolator == null ? this.mPosition : interpolator.getInterpolation(this.mPosition);
            MotionController motionController = this.mMC;
            boolean interpolate = motionController.interpolate(motionController.mView, interpolation, nanoTime, this.mCache);
            if (this.mPosition <= 0.0f) {
                if (this.mSetsTag != -1) {
                    this.mMC.getView().setTag(this.mSetsTag, Long.valueOf(System.nanoTime()));
                }
                if (this.mClearsTag != -1) {
                    this.mMC.getView().setTag(this.mClearsTag, (Object) null);
                }
                this.mVtController.removeAnimation(this);
            }
            if (this.mPosition > 0.0f || interpolate) {
                this.mVtController.invalidate();
            }
        }

        public void reactTo(int action, float x, float y) {
            switch (action) {
                case 1:
                    if (!this.reverse) {
                        reverse(true);
                        return;
                    }
                    return;
                case 2:
                    this.mMC.getView().getHitRect(this.mTempRec);
                    if (!this.mTempRec.contains((int) x, (int) y) && !this.reverse) {
                        reverse(true);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        /* access modifiers changed from: package-private */
        public void reverse(boolean dir) {
            int i;
            this.reverse = dir;
            if (dir && (i = this.mUpDuration) != -1) {
                this.mDpositionDt = i == 0 ? Float.MAX_VALUE : 1.0f / ((float) i);
            }
            this.mVtController.invalidate();
            this.mLastRender = System.nanoTime();
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    ViewTransition(android.content.Context r9, org.xmlpull.v1.XmlPullParser r10) {
        /*
            r8 = this;
            r8.<init>()
            r0 = -1
            r8.mOnStateTransition = r0
            r1 = 0
            r8.mDisabled = r1
            r8.mPathMotionArc = r1
            r8.mDuration = r0
            r8.mUpDuration = r0
            r8.mDefaultInterpolator = r1
            r2 = 0
            r8.mDefaultInterpolatorString = r2
            r8.mDefaultInterpolatorID = r0
            r8.mSetsTag = r0
            r8.mClearsTag = r0
            r8.mIfTagSet = r0
            r8.mIfTagNotSet = r0
            r8.mSharedValueTarget = r0
            r8.mSharedValueID = r0
            r8.mSharedValueCurrent = r0
            r8.mContext = r9
            r2 = 0
            r3 = 0
            int r4 = r10.getEventType()     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
        L_0x002c:
            r5 = 1
            if (r4 == r5) goto L_0x00e5
            java.lang.String r6 = "ViewTransition"
            switch(r4) {
                case 0: goto L_0x00dd;
                case 1: goto L_0x0034;
                case 2: goto L_0x0041;
                case 3: goto L_0x0036;
                default: goto L_0x0034;
            }
        L_0x0034:
            goto L_0x00de
        L_0x0036:
            java.lang.String r5 = r10.getName()     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            boolean r5 = r6.equals(r5)     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            if (r5 == 0) goto L_0x00de
            return
        L_0x0041:
            java.lang.String r7 = r10.getName()     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            r2 = r7
            int r7 = r2.hashCode()     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            switch(r7) {
                case -1962203927: goto L_0x0073;
                case -1239391468: goto L_0x006a;
                case 61998586: goto L_0x0062;
                case 366511058: goto L_0x0058;
                case 1791837707: goto L_0x004e;
                default: goto L_0x004d;
            }     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
        L_0x004d:
            goto L_0x007d
        L_0x004e:
            java.lang.String r5 = "CustomAttribute"
            boolean r5 = r2.equals(r5)     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            if (r5 == 0) goto L_0x004d
            r5 = 3
            goto L_0x007e
        L_0x0058:
            java.lang.String r5 = "CustomMethod"
            boolean r5 = r2.equals(r5)     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            if (r5 == 0) goto L_0x004d
            r5 = 4
            goto L_0x007e
        L_0x0062:
            boolean r5 = r2.equals(r6)     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            if (r5 == 0) goto L_0x004d
            r5 = r1
            goto L_0x007e
        L_0x006a:
            java.lang.String r6 = "KeyFrameSet"
            boolean r6 = r2.equals(r6)     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            if (r6 == 0) goto L_0x004d
            goto L_0x007e
        L_0x0073:
            java.lang.String r5 = "ConstraintOverride"
            boolean r5 = r2.equals(r5)     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            if (r5 == 0) goto L_0x004d
            r5 = 2
            goto L_0x007e
        L_0x007d:
            r5 = r0
        L_0x007e:
            switch(r5) {
                case 0: goto L_0x009b;
                case 1: goto L_0x0093;
                case 2: goto L_0x008c;
                case 3: goto L_0x0084;
                case 4: goto L_0x0084;
                default: goto L_0x0081;
            }     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
        L_0x0081:
            java.lang.String r5 = TAG     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            goto L_0x009f
        L_0x0084:
            androidx.constraintlayout.widget.ConstraintSet$Constraint r5 = r8.mConstraintDelta     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            java.util.HashMap<java.lang.String, androidx.constraintlayout.widget.ConstraintAttribute> r5 = r5.mCustomConstraints     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            androidx.constraintlayout.widget.ConstraintAttribute.parse(r9, r10, r5)     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            goto L_0x00dc
        L_0x008c:
            androidx.constraintlayout.widget.ConstraintSet$Constraint r5 = androidx.constraintlayout.widget.ConstraintSet.buildDelta(r9, r10)     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            r8.mConstraintDelta = r5     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            goto L_0x00dc
        L_0x0093:
            androidx.constraintlayout.motion.widget.KeyFrames r5 = new androidx.constraintlayout.motion.widget.KeyFrames     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            r5.<init>(r9, r10)     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            r8.mKeyFrames = r5     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            goto L_0x00dc
        L_0x009b:
            r8.parseViewTransitionTags(r9, r10)     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            goto L_0x00dc
        L_0x009f:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            r6.<init>()     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            java.lang.String r7 = androidx.constraintlayout.motion.widget.Debug.getLoc()     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            mt.Log1F380D.a((java.lang.Object) r7)
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            java.lang.String r7 = " unknown tag "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            java.lang.StringBuilder r6 = r6.append(r2)     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            java.lang.String r6 = r6.toString()     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            android.util.Log.e(r5, r6)     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            java.lang.String r5 = TAG     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            r6.<init>()     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            java.lang.String r7 = ".xml:"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            int r7 = r10.getLineNumber()     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            java.lang.String r6 = r6.toString()     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            android.util.Log.e(r5, r6)     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
        L_0x00dc:
            goto L_0x00de
        L_0x00dd:
        L_0x00de:
            int r5 = r10.next()     // Catch:{ XmlPullParserException -> 0x00eb, IOException -> 0x00e6 }
            r4 = r5
            goto L_0x002c
        L_0x00e5:
            goto L_0x00ef
        L_0x00e6:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x00f0
        L_0x00eb:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00ef:
        L_0x00f0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.ViewTransition.<init>(android.content.Context, org.xmlpull.v1.XmlPullParser):void");
    }

    private void parseViewTransitionTags(Context context, XmlPullParser parser) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(parser), R.styleable.ViewTransition);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = obtainStyledAttributes.getIndex(i);
            if (index == R.styleable.ViewTransition_android_id) {
                this.mId = obtainStyledAttributes.getResourceId(index, this.mId);
            } else if (index == R.styleable.ViewTransition_motionTarget) {
                if (MotionLayout.IS_IN_EDIT_MODE) {
                    int resourceId = obtainStyledAttributes.getResourceId(index, this.mTargetId);
                    this.mTargetId = resourceId;
                    if (resourceId == -1) {
                        this.mTargetString = obtainStyledAttributes.getString(index);
                    }
                } else if (obtainStyledAttributes.peekValue(index).type == 3) {
                    this.mTargetString = obtainStyledAttributes.getString(index);
                } else {
                    this.mTargetId = obtainStyledAttributes.getResourceId(index, this.mTargetId);
                }
            } else if (index == R.styleable.ViewTransition_onStateTransition) {
                this.mOnStateTransition = obtainStyledAttributes.getInt(index, this.mOnStateTransition);
            } else if (index == R.styleable.ViewTransition_transitionDisable) {
                this.mDisabled = obtainStyledAttributes.getBoolean(index, this.mDisabled);
            } else if (index == R.styleable.ViewTransition_pathMotionArc) {
                this.mPathMotionArc = obtainStyledAttributes.getInt(index, this.mPathMotionArc);
            } else if (index == R.styleable.ViewTransition_duration) {
                this.mDuration = obtainStyledAttributes.getInt(index, this.mDuration);
            } else if (index == R.styleable.ViewTransition_upDuration) {
                this.mUpDuration = obtainStyledAttributes.getInt(index, this.mUpDuration);
            } else if (index == R.styleable.ViewTransition_viewTransitionMode) {
                this.mViewTransitionMode = obtainStyledAttributes.getInt(index, this.mViewTransitionMode);
            } else if (index == R.styleable.ViewTransition_motionInterpolator) {
                TypedValue peekValue = obtainStyledAttributes.peekValue(index);
                if (peekValue.type == 1) {
                    int resourceId2 = obtainStyledAttributes.getResourceId(index, -1);
                    this.mDefaultInterpolatorID = resourceId2;
                    if (resourceId2 != -1) {
                        this.mDefaultInterpolator = -2;
                    }
                } else if (peekValue.type == 3) {
                    String string = obtainStyledAttributes.getString(index);
                    this.mDefaultInterpolatorString = string;
                    if (string == null || string.indexOf("/") <= 0) {
                        this.mDefaultInterpolator = -1;
                    } else {
                        this.mDefaultInterpolatorID = obtainStyledAttributes.getResourceId(index, -1);
                        this.mDefaultInterpolator = -2;
                    }
                } else {
                    this.mDefaultInterpolator = obtainStyledAttributes.getInteger(index, this.mDefaultInterpolator);
                }
            } else if (index == R.styleable.ViewTransition_setsTag) {
                this.mSetsTag = obtainStyledAttributes.getResourceId(index, this.mSetsTag);
            } else if (index == R.styleable.ViewTransition_clearsTag) {
                this.mClearsTag = obtainStyledAttributes.getResourceId(index, this.mClearsTag);
            } else if (index == R.styleable.ViewTransition_ifTagSet) {
                this.mIfTagSet = obtainStyledAttributes.getResourceId(index, this.mIfTagSet);
            } else if (index == R.styleable.ViewTransition_ifTagNotSet) {
                this.mIfTagNotSet = obtainStyledAttributes.getResourceId(index, this.mIfTagNotSet);
            } else if (index == R.styleable.ViewTransition_SharedValueId) {
                this.mSharedValueID = obtainStyledAttributes.getResourceId(index, this.mSharedValueID);
            } else if (index == R.styleable.ViewTransition_SharedValue) {
                this.mSharedValueTarget = obtainStyledAttributes.getInteger(index, this.mSharedValueTarget);
            }
        }
        obtainStyledAttributes.recycle();
    }

    private void updateTransition(MotionScene.Transition transition, View view) {
        int i = this.mDuration;
        if (i != -1) {
            transition.setDuration(i);
        }
        transition.setPathMotionArc(this.mPathMotionArc);
        transition.setInterpolatorInfo(this.mDefaultInterpolator, this.mDefaultInterpolatorString, this.mDefaultInterpolatorID);
        int id = view.getId();
        KeyFrames keyFrames = this.mKeyFrames;
        if (keyFrames != null) {
            ArrayList<Key> keyFramesForView = keyFrames.getKeyFramesForView(-1);
            KeyFrames keyFrames2 = new KeyFrames();
            Iterator<Key> it = keyFramesForView.iterator();
            while (it.hasNext()) {
                keyFrames2.addKey(it.next().clone().setViewId(id));
            }
            transition.addKeyFrame(keyFrames2);
        }
    }

    /* access modifiers changed from: package-private */
    public void applyIndependentTransition(ViewTransitionController controller, MotionLayout motionLayout, View view) {
        MotionController motionController = new MotionController(view);
        motionController.setBothStates(view);
        this.mKeyFrames.addAllFrames(motionController);
        motionController.setup(motionLayout.getWidth(), motionLayout.getHeight(), (float) this.mDuration, System.nanoTime());
        new Animate(controller, motionController, this.mDuration, this.mUpDuration, this.mOnStateTransition, getInterpolator(motionLayout.getContext()), this.mSetsTag, this.mClearsTag);
    }

    /* access modifiers changed from: package-private */
    public void applyTransition(ViewTransitionController controller, MotionLayout layout, int fromId, ConstraintSet current, View... views) {
        MotionLayout motionLayout = layout;
        int i = fromId;
        ConstraintSet constraintSet = current;
        View[] viewArr = views;
        if (!this.mDisabled) {
            int i2 = this.mViewTransitionMode;
            int i3 = 0;
            if (i2 == 2) {
                applyIndependentTransition(controller, motionLayout, viewArr[0]);
                return;
            }
            ViewTransitionController viewTransitionController = controller;
            if (i2 == 1) {
                int[] constraintSetIds = layout.getConstraintSetIds();
                int i4 = 0;
                while (i4 < constraintSetIds.length) {
                    int i5 = constraintSetIds[i4];
                    if (i5 != i) {
                        ConstraintSet constraintSet2 = motionLayout.getConstraintSet(i5);
                        int length = viewArr.length;
                        for (int i6 = i3; i6 < length; i6++) {
                            ConstraintSet.Constraint constraint = constraintSet2.getConstraint(viewArr[i6].getId());
                            ConstraintSet.Constraint constraint2 = this.mConstraintDelta;
                            if (constraint2 != null) {
                                constraint2.applyDelta(constraint);
                                constraint.mCustomConstraints.putAll(this.mConstraintDelta.mCustomConstraints);
                            }
                        }
                    }
                    i4++;
                    i3 = 0;
                }
            }
            ConstraintSet constraintSet3 = new ConstraintSet();
            constraintSet3.clone(constraintSet);
            for (View id : viewArr) {
                ConstraintSet.Constraint constraint3 = constraintSet3.getConstraint(id.getId());
                ConstraintSet.Constraint constraint4 = this.mConstraintDelta;
                if (constraint4 != null) {
                    constraint4.applyDelta(constraint3);
                    constraint3.mCustomConstraints.putAll(this.mConstraintDelta.mCustomConstraints);
                }
            }
            motionLayout.updateState(i, constraintSet3);
            motionLayout.updateState(R.id.view_transition, constraintSet);
            motionLayout.setState(R.id.view_transition, -1, -1);
            MotionScene.Transition transition = new MotionScene.Transition(-1, motionLayout.mScene, R.id.view_transition, i);
            for (View updateTransition : viewArr) {
                updateTransition(transition, updateTransition);
            }
            motionLayout.setTransition(transition);
            motionLayout.transitionToEnd(new ViewTransition$$ExternalSyntheticLambda0(this, viewArr));
        }
    }

    /* access modifiers changed from: package-private */
    public boolean checkTags(View view) {
        int i = this.mIfTagSet;
        boolean z = i == -1 || view.getTag(i) != null;
        int i2 = this.mIfTagNotSet;
        return z && (i2 == -1 || view.getTag(i2) == null);
    }

    /* access modifiers changed from: package-private */
    public int getId() {
        return this.mId;
    }

    /* access modifiers changed from: package-private */
    public Interpolator getInterpolator(Context context) {
        switch (this.mDefaultInterpolator) {
            case -2:
                return AnimationUtils.loadInterpolator(context, this.mDefaultInterpolatorID);
            case -1:
                final Easing interpolator = Easing.getInterpolator(this.mDefaultInterpolatorString);
                return new Interpolator(this) {
                    public float getInterpolation(float v) {
                        return (float) interpolator.get((double) v);
                    }
                };
            case 0:
                return new AccelerateDecelerateInterpolator();
            case 1:
                return new AccelerateInterpolator();
            case 2:
                return new DecelerateInterpolator();
            case 3:
                return null;
            case 4:
                return new BounceInterpolator();
            case 5:
                return new OvershootInterpolator();
            case 6:
                return new AnticipateInterpolator();
            default:
                return null;
        }
    }

    public int getSharedValue() {
        return this.mSharedValueTarget;
    }

    public int getSharedValueCurrent() {
        return this.mSharedValueCurrent;
    }

    public int getSharedValueID() {
        return this.mSharedValueID;
    }

    public int getStateTransition() {
        return this.mOnStateTransition;
    }

    /* access modifiers changed from: package-private */
    public boolean isEnabled() {
        return !this.mDisabled;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$applyTransition$0$androidx-constraintlayout-motion-widget-ViewTransition  reason: not valid java name */
    public /* synthetic */ void m5lambda$applyTransition$0$androidxconstraintlayoutmotionwidgetViewTransition(View[] views) {
        if (this.mSetsTag != -1) {
            for (View tag : views) {
                tag.setTag(this.mSetsTag, Long.valueOf(System.nanoTime()));
            }
        }
        if (this.mClearsTag != -1) {
            for (View tag2 : views) {
                tag2.setTag(this.mClearsTag, (Object) null);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002c, code lost:
        r2 = ((androidx.constraintlayout.widget.ConstraintLayout.LayoutParams) r6.getLayoutParams()).constraintTag;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean matchesView(android.view.View r6) {
        /*
            r5 = this;
            r0 = 0
            if (r6 != 0) goto L_0x0004
            return r0
        L_0x0004:
            int r1 = r5.mTargetId
            r2 = -1
            if (r1 != r2) goto L_0x000e
            java.lang.String r1 = r5.mTargetString
            if (r1 != 0) goto L_0x000e
            return r0
        L_0x000e:
            boolean r1 = r5.checkTags(r6)
            if (r1 != 0) goto L_0x0015
            return r0
        L_0x0015:
            int r1 = r6.getId()
            int r2 = r5.mTargetId
            r3 = 1
            if (r1 != r2) goto L_0x001f
            return r3
        L_0x001f:
            java.lang.String r1 = r5.mTargetString
            if (r1 != 0) goto L_0x0024
            return r0
        L_0x0024:
            android.view.ViewGroup$LayoutParams r1 = r6.getLayoutParams()
            boolean r2 = r1 instanceof androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
            if (r2 == 0) goto L_0x003f
            android.view.ViewGroup$LayoutParams r2 = r6.getLayoutParams()
            androidx.constraintlayout.widget.ConstraintLayout$LayoutParams r2 = (androidx.constraintlayout.widget.ConstraintLayout.LayoutParams) r2
            java.lang.String r2 = r2.constraintTag
            if (r2 == 0) goto L_0x003f
            java.lang.String r4 = r5.mTargetString
            boolean r4 = r2.matches(r4)
            if (r4 == 0) goto L_0x003f
            return r3
        L_0x003f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.ViewTransition.matchesView(android.view.View):boolean");
    }

    /* access modifiers changed from: package-private */
    public void setEnabled(boolean enable) {
        this.mDisabled = !enable;
    }

    /* access modifiers changed from: package-private */
    public void setId(int id) {
        this.mId = id;
    }

    public void setSharedValue(int sharedValue) {
        this.mSharedValueTarget = sharedValue;
    }

    public void setSharedValueCurrent(int sharedValueCurrent) {
        this.mSharedValueCurrent = sharedValueCurrent;
    }

    public void setSharedValueID(int sharedValueID) {
        this.mSharedValueID = sharedValueID;
    }

    public void setStateTransition(int stateTransition) {
        this.mOnStateTransition = stateTransition;
    }

    /* access modifiers changed from: package-private */
    public boolean supports(int action) {
        int i = this.mOnStateTransition;
        return i == 1 ? action == 0 : i == 2 ? action == 1 : i == 3 && action == 0;
    }

    public String toString() {
        StringBuilder append = new StringBuilder().append("ViewTransition(");
        String name = Debug.getName(this.mContext, this.mId);
        Log1F380D.a((Object) name);
        return append.append(name).append(")").toString();
    }
}
