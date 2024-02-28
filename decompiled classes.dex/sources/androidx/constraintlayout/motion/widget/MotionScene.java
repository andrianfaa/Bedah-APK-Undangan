package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.util.Xml;
import android.view.MotionEvent;
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
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.R;
import androidx.constraintlayout.widget.StateSet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mt.Log1F380D;
import okhttp3.HttpUrl;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: 0023 */
public class MotionScene {
    static final int ANTICIPATE = 6;
    static final int BOUNCE = 4;
    private static final String CONSTRAINTSET_TAG = "ConstraintSet";
    private static final boolean DEBUG = false;
    static final int EASE_IN = 1;
    static final int EASE_IN_OUT = 0;
    static final int EASE_OUT = 2;
    private static final String INCLUDE_TAG = "include";
    private static final String INCLUDE_TAG_UC = "Include";
    private static final int INTERPOLATOR_REFERENCE_ID = -2;
    private static final String KEYFRAMESET_TAG = "KeyFrameSet";
    public static final int LAYOUT_CALL_MEASURE = 2;
    public static final int LAYOUT_HONOR_REQUEST = 1;
    public static final int LAYOUT_IGNORE_REQUEST = 0;
    static final int LINEAR = 3;
    private static final int MIN_DURATION = 8;
    private static final String MOTIONSCENE_TAG = "MotionScene";
    private static final String ONCLICK_TAG = "OnClick";
    private static final String ONSWIPE_TAG = "OnSwipe";
    static final int OVERSHOOT = 5;
    private static final int SPLINE_STRING = -1;
    private static final String STATESET_TAG = "StateSet";
    private static final String TAG = "MotionScene";
    static final int TRANSITION_BACKWARD = 0;
    static final int TRANSITION_FORWARD = 1;
    private static final String TRANSITION_TAG = "Transition";
    public static final int UNSET = -1;
    private static final String VIEW_TRANSITION = "ViewTransition";
    private boolean DEBUG_DESKTOP = false;
    private ArrayList<Transition> mAbstractTransitionList = new ArrayList<>();
    private HashMap<String, Integer> mConstraintSetIdMap = new HashMap<>();
    /* access modifiers changed from: private */
    public SparseArray<ConstraintSet> mConstraintSetMap = new SparseArray<>();
    Transition mCurrentTransition = null;
    /* access modifiers changed from: private */
    public int mDefaultDuration = 400;
    private Transition mDefaultTransition = null;
    private SparseIntArray mDeriveMap = new SparseIntArray();
    private boolean mDisableAutoTransition = false;
    private boolean mIgnoreTouch = false;
    private MotionEvent mLastTouchDown;
    float mLastTouchX;
    float mLastTouchY;
    /* access modifiers changed from: private */
    public int mLayoutDuringTransition = 0;
    /* access modifiers changed from: private */
    public final MotionLayout mMotionLayout;
    private boolean mMotionOutsideRegion = false;
    private boolean mRtl;
    StateSet mStateSet = null;
    private ArrayList<Transition> mTransitionList = new ArrayList<>();
    private MotionLayout.MotionTracker mVelocityTracker;
    final ViewTransitionController mViewTransitionController;

    public static class Transition {
        public static final int AUTO_ANIMATE_TO_END = 4;
        public static final int AUTO_ANIMATE_TO_START = 3;
        public static final int AUTO_JUMP_TO_END = 2;
        public static final int AUTO_JUMP_TO_START = 1;
        public static final int AUTO_NONE = 0;
        public static final int INTERPOLATE_ANTICIPATE = 6;
        public static final int INTERPOLATE_BOUNCE = 4;
        public static final int INTERPOLATE_EASE_IN = 1;
        public static final int INTERPOLATE_EASE_IN_OUT = 0;
        public static final int INTERPOLATE_EASE_OUT = 2;
        public static final int INTERPOLATE_LINEAR = 3;
        public static final int INTERPOLATE_OVERSHOOT = 5;
        public static final int INTERPOLATE_REFERENCE_ID = -2;
        public static final int INTERPOLATE_SPLINE_STRING = -1;
        static final int TRANSITION_FLAG_FIRST_DRAW = 1;
        static final int TRANSITION_FLAG_INTERCEPT_TOUCH = 4;
        static final int TRANSITION_FLAG_INTRA_AUTO = 2;
        /* access modifiers changed from: private */
        public int mAutoTransition = 0;
        /* access modifiers changed from: private */
        public int mConstraintSetEnd = -1;
        /* access modifiers changed from: private */
        public int mConstraintSetStart = -1;
        /* access modifiers changed from: private */
        public int mDefaultInterpolator = 0;
        /* access modifiers changed from: private */
        public int mDefaultInterpolatorID = -1;
        /* access modifiers changed from: private */
        public String mDefaultInterpolatorString = null;
        /* access modifiers changed from: private */
        public boolean mDisable = false;
        /* access modifiers changed from: private */
        public int mDuration = 400;
        /* access modifiers changed from: private */
        public int mId = -1;
        /* access modifiers changed from: private */
        public boolean mIsAbstract = false;
        /* access modifiers changed from: private */
        public ArrayList<KeyFrames> mKeyFramesList = new ArrayList<>();
        private int mLayoutDuringTransition = 0;
        /* access modifiers changed from: private */
        public final MotionScene mMotionScene;
        /* access modifiers changed from: private */
        public ArrayList<TransitionOnClick> mOnClicks = new ArrayList<>();
        /* access modifiers changed from: private */
        public int mPathMotionArc = -1;
        /* access modifiers changed from: private */
        public float mStagger = 0.0f;
        /* access modifiers changed from: private */
        public TouchResponse mTouchResponse = null;
        private int mTransitionFlags = 0;

        public static class TransitionOnClick implements View.OnClickListener {
            public static final int ANIM_TOGGLE = 17;
            public static final int ANIM_TO_END = 1;
            public static final int ANIM_TO_START = 16;
            public static final int JUMP_TO_END = 256;
            public static final int JUMP_TO_START = 4096;
            int mMode = 17;
            int mTargetId = -1;
            private final Transition mTransition;

            public TransitionOnClick(Context context, Transition transition, XmlPullParser parser) {
                this.mTransition = transition;
                TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(parser), R.styleable.OnClick);
                int indexCount = obtainStyledAttributes.getIndexCount();
                for (int i = 0; i < indexCount; i++) {
                    int index = obtainStyledAttributes.getIndex(i);
                    if (index == R.styleable.OnClick_targetId) {
                        this.mTargetId = obtainStyledAttributes.getResourceId(index, this.mTargetId);
                    } else if (index == R.styleable.OnClick_clickAction) {
                        this.mMode = obtainStyledAttributes.getInt(index, this.mMode);
                    }
                }
                obtainStyledAttributes.recycle();
            }

            public TransitionOnClick(Transition transition, int id, int action) {
                this.mTransition = transition;
                this.mTargetId = id;
                this.mMode = action;
            }

            public void addOnClickListeners(MotionLayout motionLayout, int currentState, Transition transition) {
                int i = this.mTargetId;
                View findViewById = i == -1 ? motionLayout : motionLayout.findViewById(i);
                if (findViewById == null) {
                    Log.e(TypedValues.MotionScene.NAME, "OnClick could not find id " + this.mTargetId);
                    return;
                }
                int access$100 = transition.mConstraintSetStart;
                int access$000 = transition.mConstraintSetEnd;
                if (access$100 == -1) {
                    findViewById.setOnClickListener(this);
                    return;
                }
                int i2 = this.mMode;
                boolean z = false;
                boolean z2 = ((i2 & 1) != 0 && currentState == access$100) | ((i2 & 256) != 0 && currentState == access$100) | ((i2 & 1) != 0 && currentState == access$100) | ((i2 & 16) != 0 && currentState == access$000);
                if ((i2 & 4096) != 0 && currentState == access$000) {
                    z = true;
                }
                if (z2 || z) {
                    findViewById.setOnClickListener(this);
                }
            }

            /* access modifiers changed from: package-private */
            public boolean isTransitionViable(Transition current, MotionLayout tl) {
                Transition transition = this.mTransition;
                if (transition == current) {
                    return true;
                }
                int access$000 = transition.mConstraintSetEnd;
                int access$100 = this.mTransition.mConstraintSetStart;
                return access$100 == -1 ? tl.mCurrentState != access$000 : tl.mCurrentState == access$100 || tl.mCurrentState == access$000;
            }

            public void onClick(View view) {
                MotionLayout access$700 = this.mTransition.mMotionScene.mMotionLayout;
                if (access$700.isInteractionEnabled()) {
                    if (this.mTransition.mConstraintSetStart == -1) {
                        int currentState = access$700.getCurrentState();
                        if (currentState == -1) {
                            access$700.transitionToState(this.mTransition.mConstraintSetEnd);
                            return;
                        }
                        Transition transition = new Transition(this.mTransition.mMotionScene, this.mTransition);
                        int unused = transition.mConstraintSetStart = currentState;
                        int unused2 = transition.mConstraintSetEnd = this.mTransition.mConstraintSetEnd;
                        access$700.setTransition(transition);
                        access$700.transitionToEnd();
                        return;
                    }
                    Transition transition2 = this.mTransition.mMotionScene.mCurrentTransition;
                    int i = this.mMode;
                    boolean z = false;
                    boolean z2 = ((i & 1) == 0 && (i & 256) == 0) ? false : true;
                    boolean z3 = ((i & 16) == 0 && (i & 4096) == 0) ? false : true;
                    if (z2 && z3) {
                        z = true;
                    }
                    if (z) {
                        Transition transition3 = this.mTransition.mMotionScene.mCurrentTransition;
                        Transition transition4 = this.mTransition;
                        if (transition3 != transition4) {
                            access$700.setTransition(transition4);
                        }
                        if (access$700.getCurrentState() == access$700.getEndState() || access$700.getProgress() > 0.5f) {
                            z2 = false;
                        } else {
                            z3 = false;
                        }
                    }
                    if (!isTransitionViable(transition2, access$700)) {
                        return;
                    }
                    if (z2 && (1 & this.mMode) != 0) {
                        access$700.setTransition(this.mTransition);
                        access$700.transitionToEnd();
                    } else if (z3 && (this.mMode & 16) != 0) {
                        access$700.setTransition(this.mTransition);
                        access$700.transitionToStart();
                    } else if (z2 && (this.mMode & 256) != 0) {
                        access$700.setTransition(this.mTransition);
                        access$700.setProgress(1.0f);
                    } else if (z3 && (this.mMode & 4096) != 0) {
                        access$700.setTransition(this.mTransition);
                        access$700.setProgress(0.0f);
                    }
                }
            }

            public void removeOnClickListeners(MotionLayout motionLayout) {
                int i = this.mTargetId;
                if (i != -1) {
                    View findViewById = motionLayout.findViewById(i);
                    if (findViewById == null) {
                        Log.e(TypedValues.MotionScene.NAME, " (*)  could not find id " + this.mTargetId);
                    } else {
                        findViewById.setOnClickListener((View.OnClickListener) null);
                    }
                }
            }
        }

        public Transition(int id, MotionScene motionScene, int constraintSetStartId, int constraintSetEndId) {
            this.mId = id;
            this.mMotionScene = motionScene;
            this.mConstraintSetStart = constraintSetStartId;
            this.mConstraintSetEnd = constraintSetEndId;
            this.mDuration = motionScene.mDefaultDuration;
            this.mLayoutDuringTransition = motionScene.mLayoutDuringTransition;
        }

        Transition(MotionScene motionScene, Context context, XmlPullParser parser) {
            this.mDuration = motionScene.mDefaultDuration;
            this.mLayoutDuringTransition = motionScene.mLayoutDuringTransition;
            this.mMotionScene = motionScene;
            fillFromAttributeList(motionScene, context, Xml.asAttributeSet(parser));
        }

        Transition(MotionScene motionScene, Transition global) {
            this.mMotionScene = motionScene;
            this.mDuration = motionScene.mDefaultDuration;
            if (global != null) {
                this.mPathMotionArc = global.mPathMotionArc;
                this.mDefaultInterpolator = global.mDefaultInterpolator;
                this.mDefaultInterpolatorString = global.mDefaultInterpolatorString;
                this.mDefaultInterpolatorID = global.mDefaultInterpolatorID;
                this.mDuration = global.mDuration;
                this.mKeyFramesList = global.mKeyFramesList;
                this.mStagger = global.mStagger;
                this.mLayoutDuringTransition = global.mLayoutDuringTransition;
            }
        }

        private void fill(MotionScene motionScene, Context context, TypedArray a) {
            int indexCount = a.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = a.getIndex(i);
                if (index == R.styleable.Transition_constraintSetEnd) {
                    this.mConstraintSetEnd = a.getResourceId(index, -1);
                    String resourceTypeName = context.getResources().getResourceTypeName(this.mConstraintSetEnd);
                    if ("layout".equals(resourceTypeName)) {
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.load(context, this.mConstraintSetEnd);
                        motionScene.mConstraintSetMap.append(this.mConstraintSetEnd, constraintSet);
                    } else if ("xml".equals(resourceTypeName)) {
                        this.mConstraintSetEnd = motionScene.parseInclude(context, this.mConstraintSetEnd);
                    }
                } else if (index == R.styleable.Transition_constraintSetStart) {
                    this.mConstraintSetStart = a.getResourceId(index, this.mConstraintSetStart);
                    String resourceTypeName2 = context.getResources().getResourceTypeName(this.mConstraintSetStart);
                    if ("layout".equals(resourceTypeName2)) {
                        ConstraintSet constraintSet2 = new ConstraintSet();
                        constraintSet2.load(context, this.mConstraintSetStart);
                        motionScene.mConstraintSetMap.append(this.mConstraintSetStart, constraintSet2);
                    } else if ("xml".equals(resourceTypeName2)) {
                        this.mConstraintSetStart = motionScene.parseInclude(context, this.mConstraintSetStart);
                    }
                } else if (index == R.styleable.Transition_motionInterpolator) {
                    TypedValue peekValue = a.peekValue(index);
                    if (peekValue.type == 1) {
                        int resourceId = a.getResourceId(index, -1);
                        this.mDefaultInterpolatorID = resourceId;
                        if (resourceId != -1) {
                            this.mDefaultInterpolator = -2;
                        }
                    } else if (peekValue.type == 3) {
                        String string = a.getString(index);
                        this.mDefaultInterpolatorString = string;
                        if (string != null) {
                            if (string.indexOf("/") > 0) {
                                this.mDefaultInterpolatorID = a.getResourceId(index, -1);
                                this.mDefaultInterpolator = -2;
                            } else {
                                this.mDefaultInterpolator = -1;
                            }
                        }
                    } else {
                        this.mDefaultInterpolator = a.getInteger(index, this.mDefaultInterpolator);
                    }
                } else if (index == R.styleable.Transition_duration) {
                    int i2 = a.getInt(index, this.mDuration);
                    this.mDuration = i2;
                    if (i2 < 8) {
                        this.mDuration = 8;
                    }
                } else if (index == R.styleable.Transition_staggered) {
                    this.mStagger = a.getFloat(index, this.mStagger);
                } else if (index == R.styleable.Transition_autoTransition) {
                    this.mAutoTransition = a.getInteger(index, this.mAutoTransition);
                } else if (index == R.styleable.Transition_android_id) {
                    this.mId = a.getResourceId(index, this.mId);
                } else if (index == R.styleable.Transition_transitionDisable) {
                    this.mDisable = a.getBoolean(index, this.mDisable);
                } else if (index == R.styleable.Transition_pathMotionArc) {
                    this.mPathMotionArc = a.getInteger(index, -1);
                } else if (index == R.styleable.Transition_layoutDuringTransition) {
                    this.mLayoutDuringTransition = a.getInteger(index, 0);
                } else if (index == R.styleable.Transition_transitionFlags) {
                    this.mTransitionFlags = a.getInteger(index, 0);
                }
            }
            if (this.mConstraintSetStart == -1) {
                this.mIsAbstract = true;
            }
        }

        private void fillFromAttributeList(MotionScene motionScene, Context context, AttributeSet attrs) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.Transition);
            fill(motionScene, context, obtainStyledAttributes);
            obtainStyledAttributes.recycle();
        }

        public void addKeyFrame(KeyFrames keyFrames) {
            this.mKeyFramesList.add(keyFrames);
        }

        public void addOnClick(int id, int action) {
            Iterator<TransitionOnClick> it = this.mOnClicks.iterator();
            while (it.hasNext()) {
                TransitionOnClick next = it.next();
                if (next.mTargetId == id) {
                    next.mMode = action;
                    return;
                }
            }
            this.mOnClicks.add(new TransitionOnClick(this, id, action));
        }

        public void addOnClick(Context context, XmlPullParser parser) {
            this.mOnClicks.add(new TransitionOnClick(context, this, parser));
        }

        public String debugString(Context context) {
            String resourceEntryName = this.mConstraintSetStart == -1 ? "null" : context.getResources().getResourceEntryName(this.mConstraintSetStart);
            return this.mConstraintSetEnd == -1 ? resourceEntryName + " -> null" : resourceEntryName + " -> " + context.getResources().getResourceEntryName(this.mConstraintSetEnd);
        }

        public int getAutoTransition() {
            return this.mAutoTransition;
        }

        public int getDuration() {
            return this.mDuration;
        }

        public int getEndConstraintSetId() {
            return this.mConstraintSetEnd;
        }

        public int getId() {
            return this.mId;
        }

        public List<KeyFrames> getKeyFrameList() {
            return this.mKeyFramesList;
        }

        public int getLayoutDuringTransition() {
            return this.mLayoutDuringTransition;
        }

        public List<TransitionOnClick> getOnClickList() {
            return this.mOnClicks;
        }

        public int getPathMotionArc() {
            return this.mPathMotionArc;
        }

        public float getStagger() {
            return this.mStagger;
        }

        public int getStartConstraintSetId() {
            return this.mConstraintSetStart;
        }

        public TouchResponse getTouchResponse() {
            return this.mTouchResponse;
        }

        public boolean isEnabled() {
            return !this.mDisable;
        }

        public boolean isTransitionFlag(int flag) {
            return (this.mTransitionFlags & flag) != 0;
        }

        public void removeOnClick(int id) {
            TransitionOnClick transitionOnClick = null;
            Iterator<TransitionOnClick> it = this.mOnClicks.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                TransitionOnClick next = it.next();
                if (next.mTargetId == id) {
                    transitionOnClick = next;
                    break;
                }
            }
            if (transitionOnClick != null) {
                this.mOnClicks.remove(transitionOnClick);
            }
        }

        public void setAutoTransition(int type) {
            this.mAutoTransition = type;
        }

        public void setDuration(int duration) {
            this.mDuration = Math.max(duration, 8);
        }

        public void setEnable(boolean enable) {
            setEnabled(enable);
        }

        public void setEnabled(boolean enable) {
            this.mDisable = !enable;
        }

        public void setInterpolatorInfo(int interpolator, String interpolatorString, int interpolatorID) {
            this.mDefaultInterpolator = interpolator;
            this.mDefaultInterpolatorString = interpolatorString;
            this.mDefaultInterpolatorID = interpolatorID;
        }

        public void setLayoutDuringTransition(int mode) {
            this.mLayoutDuringTransition = mode;
        }

        public void setOnSwipe(OnSwipe onSwipe) {
            this.mTouchResponse = onSwipe == null ? null : new TouchResponse(this.mMotionScene.mMotionLayout, onSwipe);
        }

        public void setOnTouchUp(int touchUpMode) {
            TouchResponse touchResponse = getTouchResponse();
            if (touchResponse != null) {
                touchResponse.setTouchUpMode(touchUpMode);
            }
        }

        public void setPathMotionArc(int arcMode) {
            this.mPathMotionArc = arcMode;
        }

        public void setStagger(float stagger) {
            this.mStagger = stagger;
        }

        public void setTransitionFlag(int flag) {
            this.mTransitionFlags = flag;
        }
    }

    MotionScene(Context context, MotionLayout layout, int resourceID) {
        this.mMotionLayout = layout;
        this.mViewTransitionController = new ViewTransitionController(layout);
        load(context, resourceID);
        this.mConstraintSetMap.put(R.id.motion_base, new ConstraintSet());
        this.mConstraintSetIdMap.put("motion_base", Integer.valueOf(R.id.motion_base));
    }

    public MotionScene(MotionLayout layout) {
        this.mMotionLayout = layout;
        this.mViewTransitionController = new ViewTransitionController(layout);
    }

    private int getId(Context context, String idString) {
        int i = -1;
        if (idString.contains("/")) {
            i = context.getResources().getIdentifier(idString.substring(idString.indexOf(47) + 1), "id", context.getPackageName());
            if (this.DEBUG_DESKTOP) {
                System.out.println("id getMap res = " + i);
            }
        }
        if (i != -1) {
            return i;
        }
        if (idString != null && idString.length() > 1) {
            return Integer.parseInt(idString.substring(1));
        }
        Log.e(TypedValues.MotionScene.NAME, "error in parsing id");
        return i;
    }

    private int getIndex(Transition transition) {
        int access$300 = transition.mId;
        if (access$300 != -1) {
            for (int i = 0; i < this.mTransitionList.size(); i++) {
                if (this.mTransitionList.get(i).mId == access$300) {
                    return i;
                }
            }
            return -1;
        }
        throw new IllegalArgumentException("The transition must have an id");
    }

    static String getLine(Context context, int resourceId, XmlPullParser pullParser) {
        StringBuilder append = new StringBuilder().append(".(");
        String name = Debug.getName(context, resourceId);
        Log1F380D.a((Object) name);
        return append.append(name).append(".xml:").append(pullParser.getLineNumber()).append(") \"").append(pullParser.getName()).append("\"").toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = r0.stateGetConstraintID(r3, -1, -1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getRealID(int r3) {
        /*
            r2 = this;
            androidx.constraintlayout.widget.StateSet r0 = r2.mStateSet
            if (r0 == 0) goto L_0x000c
            r1 = -1
            int r0 = r0.stateGetConstraintID(r3, r1, r1)
            if (r0 == r1) goto L_0x000c
            return r0
        L_0x000c:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionScene.getRealID(int):int");
    }

    private boolean hasCycleDependency(int key) {
        int i = this.mDeriveMap.get(key);
        int size = this.mDeriveMap.size();
        while (i > 0) {
            if (i == key) {
                return true;
            }
            int i2 = size - 1;
            if (size < 0) {
                return true;
            }
            i = this.mDeriveMap.get(i);
            size = i2;
        }
        return false;
    }

    private boolean isProcessingTouch() {
        return this.mVelocityTracker != null;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00ab, code lost:
        switch(r6) {
            case 0: goto L_0x0169;
            case 1: goto L_0x0127;
            case 2: goto L_0x00e6;
            case 3: goto L_0x00df;
            case 4: goto L_0x00d6;
            case 5: goto L_0x00d1;
            case 6: goto L_0x00cc;
            case 7: goto L_0x00cc;
            case 8: goto L_0x00bc;
            case 9: goto L_0x00b0;
            default: goto L_0x00ae;
        };
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00b0, code lost:
        r11.mViewTransitionController.add(new androidx.constraintlayout.motion.widget.ViewTransition(r12, r1));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00bc, code lost:
        r6 = new androidx.constraintlayout.motion.widget.KeyFrames(r12, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00c1, code lost:
        if (r4 == null) goto L_0x016d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00c3, code lost:
        androidx.constraintlayout.motion.widget.MotionScene.Transition.access$1400(r4).add(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00cc, code lost:
        parseInclude(r12, (org.xmlpull.v1.XmlPullParser) r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00d1, code lost:
        parseConstraintSet(r12, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00d6, code lost:
        r11.mStateSet = new androidx.constraintlayout.widget.StateSet(r12, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00df, code lost:
        if (r4 == null) goto L_0x016d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00e1, code lost:
        r4.addOnClick(r12, (org.xmlpull.v1.XmlPullParser) r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00e6, code lost:
        if (r4 != null) goto L_0x011a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00e8, code lost:
        android.util.Log.v(androidx.constraintlayout.core.motion.utils.TypedValues.MotionScene.NAME, " OnSwipe (" + r12.getResources().getResourceEntryName(r13) + ".xml:" + r1.getLineNumber() + ")");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x011a, code lost:
        if (r4 == null) goto L_0x016d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x011c, code lost:
        androidx.constraintlayout.motion.widget.MotionScene.Transition.access$202(r4, new androidx.constraintlayout.motion.widget.TouchResponse(r12, r11.mMotionLayout, r1));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0127, code lost:
        r6 = r11.mTransitionList;
        r7 = new androidx.constraintlayout.motion.widget.MotionScene.Transition(r11, r12, r1);
        r4 = r7;
        r6.add(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0134, code lost:
        if (r11.mCurrentTransition != null) goto L_0x014f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x013a, code lost:
        if (androidx.constraintlayout.motion.widget.MotionScene.Transition.access$1300(r4) != false) goto L_0x014f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x013c, code lost:
        r11.mCurrentTransition = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0142, code lost:
        if (androidx.constraintlayout.motion.widget.MotionScene.Transition.access$200(r4) == null) goto L_0x014f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x0144, code lost:
        androidx.constraintlayout.motion.widget.MotionScene.Transition.access$200(r11.mCurrentTransition).setRTL(r11.mRtl);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x0153, code lost:
        if (androidx.constraintlayout.motion.widget.MotionScene.Transition.access$1300(r4) == false) goto L_0x016d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0159, code lost:
        if (androidx.constraintlayout.motion.widget.MotionScene.Transition.access$000(r4) != -1) goto L_0x015e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x015b, code lost:
        r11.mDefaultTransition = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x015e, code lost:
        r11.mAbstractTransitionList.add(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0163, code lost:
        r11.mTransitionList.remove(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0169, code lost:
        parseMotionSceneTags(r12, r1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void load(android.content.Context r12, int r13) {
        /*
            r11 = this;
            android.content.res.Resources r0 = r12.getResources()
            android.content.res.XmlResourceParser r1 = r0.getXml(r13)
            r2 = 0
            r3 = 0
            r4 = 0
            int r5 = r1.getEventType()     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
        L_0x000f:
            r6 = 1
            if (r5 == r6) goto L_0x017b
            switch(r5) {
                case 0: goto L_0x016e;
                case 1: goto L_0x0015;
                case 2: goto L_0x001a;
                case 3: goto L_0x0017;
                default: goto L_0x0015;
            }     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
        L_0x0015:
            goto L_0x0174
        L_0x0017:
            r3 = 0
            goto L_0x0174
        L_0x001a:
            java.lang.String r7 = r1.getName()     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            r3 = r7
            boolean r7 = r11.DEBUG_DESKTOP     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            if (r7 == 0) goto L_0x003b
            java.io.PrintStream r7 = java.lang.System.out     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            r8.<init>()     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            java.lang.String r9 = "parsing = "
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            java.lang.StringBuilder r8 = r8.append(r3)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            java.lang.String r8 = r8.toString()     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            r7.println(r8)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
        L_0x003b:
            int r7 = r3.hashCode()     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            java.lang.String r8 = "MotionScene"
            r9 = -1
            switch(r7) {
                case -1349929691: goto L_0x00a0;
                case -1239391468: goto L_0x0095;
                case -687739768: goto L_0x008b;
                case 61998586: goto L_0x0080;
                case 269306229: goto L_0x0077;
                case 312750793: goto L_0x006d;
                case 327855227: goto L_0x0063;
                case 793277014: goto L_0x005b;
                case 1382829617: goto L_0x0051;
                case 1942574248: goto L_0x0047;
                default: goto L_0x0045;
            }
        L_0x0045:
            goto L_0x00aa
        L_0x0047:
            java.lang.String r6 = "include"
            boolean r6 = r3.equals(r6)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            if (r6 == 0) goto L_0x0045
            r6 = 6
            goto L_0x00ab
        L_0x0051:
            java.lang.String r6 = "StateSet"
            boolean r6 = r3.equals(r6)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            if (r6 == 0) goto L_0x0045
            r6 = 4
            goto L_0x00ab
        L_0x005b:
            boolean r6 = r3.equals(r8)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            if (r6 == 0) goto L_0x0045
            r6 = 0
            goto L_0x00ab
        L_0x0063:
            java.lang.String r6 = "OnSwipe"
            boolean r6 = r3.equals(r6)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            if (r6 == 0) goto L_0x0045
            r6 = 2
            goto L_0x00ab
        L_0x006d:
            java.lang.String r6 = "OnClick"
            boolean r6 = r3.equals(r6)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            if (r6 == 0) goto L_0x0045
            r6 = 3
            goto L_0x00ab
        L_0x0077:
            java.lang.String r7 = "Transition"
            boolean r7 = r3.equals(r7)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            if (r7 == 0) goto L_0x0045
            goto L_0x00ab
        L_0x0080:
            java.lang.String r6 = "ViewTransition"
            boolean r6 = r3.equals(r6)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            if (r6 == 0) goto L_0x0045
            r6 = 9
            goto L_0x00ab
        L_0x008b:
            java.lang.String r6 = "Include"
            boolean r6 = r3.equals(r6)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            if (r6 == 0) goto L_0x0045
            r6 = 7
            goto L_0x00ab
        L_0x0095:
            java.lang.String r6 = "KeyFrameSet"
            boolean r6 = r3.equals(r6)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            if (r6 == 0) goto L_0x0045
            r6 = 8
            goto L_0x00ab
        L_0x00a0:
            java.lang.String r6 = "ConstraintSet"
            boolean r6 = r3.equals(r6)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            if (r6 == 0) goto L_0x0045
            r6 = 5
            goto L_0x00ab
        L_0x00aa:
            r6 = r9
        L_0x00ab:
            switch(r6) {
                case 0: goto L_0x0169;
                case 1: goto L_0x0127;
                case 2: goto L_0x00e6;
                case 3: goto L_0x00df;
                case 4: goto L_0x00d6;
                case 5: goto L_0x00d1;
                case 6: goto L_0x00cc;
                case 7: goto L_0x00cc;
                case 8: goto L_0x00bc;
                case 9: goto L_0x00b0;
                default: goto L_0x00ae;
            }     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
        L_0x00ae:
            goto L_0x016d
        L_0x00b0:
            androidx.constraintlayout.motion.widget.ViewTransition r6 = new androidx.constraintlayout.motion.widget.ViewTransition     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            r6.<init>(r12, r1)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            androidx.constraintlayout.motion.widget.ViewTransitionController r7 = r11.mViewTransitionController     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            r7.add(r6)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            goto L_0x016d
        L_0x00bc:
            androidx.constraintlayout.motion.widget.KeyFrames r6 = new androidx.constraintlayout.motion.widget.KeyFrames     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            r6.<init>(r12, r1)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            if (r4 == 0) goto L_0x016d
            java.util.ArrayList r7 = r4.mKeyFramesList     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            r7.add(r6)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            goto L_0x016d
        L_0x00cc:
            r11.parseInclude((android.content.Context) r12, (org.xmlpull.v1.XmlPullParser) r1)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            goto L_0x016d
        L_0x00d1:
            r11.parseConstraintSet(r12, r1)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            goto L_0x016d
        L_0x00d6:
            androidx.constraintlayout.widget.StateSet r6 = new androidx.constraintlayout.widget.StateSet     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            r6.<init>(r12, r1)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            r11.mStateSet = r6     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            goto L_0x016d
        L_0x00df:
            if (r4 == 0) goto L_0x016d
            r4.addOnClick((android.content.Context) r12, (org.xmlpull.v1.XmlPullParser) r1)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            goto L_0x016d
        L_0x00e6:
            if (r4 != 0) goto L_0x011a
            android.content.res.Resources r6 = r12.getResources()     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            java.lang.String r6 = r6.getResourceEntryName(r13)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            int r7 = r1.getLineNumber()     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            r9.<init>()     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            java.lang.String r10 = " OnSwipe ("
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            java.lang.StringBuilder r9 = r9.append(r6)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            java.lang.String r10 = ".xml:"
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            java.lang.StringBuilder r9 = r9.append(r7)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            java.lang.String r10 = ")"
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            java.lang.String r9 = r9.toString()     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            android.util.Log.v(r8, r9)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
        L_0x011a:
            if (r4 == 0) goto L_0x016d
            androidx.constraintlayout.motion.widget.TouchResponse r6 = new androidx.constraintlayout.motion.widget.TouchResponse     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            androidx.constraintlayout.motion.widget.MotionLayout r7 = r11.mMotionLayout     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            r6.<init>(r12, r7, r1)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            androidx.constraintlayout.motion.widget.TouchResponse unused = r4.mTouchResponse = r6     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            goto L_0x016d
        L_0x0127:
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionScene$Transition> r6 = r11.mTransitionList     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            androidx.constraintlayout.motion.widget.MotionScene$Transition r7 = new androidx.constraintlayout.motion.widget.MotionScene$Transition     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            r7.<init>(r11, r12, r1)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            r4 = r7
            r6.add(r7)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            androidx.constraintlayout.motion.widget.MotionScene$Transition r6 = r11.mCurrentTransition     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            if (r6 != 0) goto L_0x014f
            boolean r6 = r4.mIsAbstract     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            if (r6 != 0) goto L_0x014f
            r11.mCurrentTransition = r4     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            androidx.constraintlayout.motion.widget.TouchResponse r6 = r4.mTouchResponse     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            if (r6 == 0) goto L_0x014f
            androidx.constraintlayout.motion.widget.MotionScene$Transition r6 = r11.mCurrentTransition     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            androidx.constraintlayout.motion.widget.TouchResponse r6 = r6.mTouchResponse     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            boolean r7 = r11.mRtl     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            r6.setRTL(r7)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
        L_0x014f:
            boolean r6 = r4.mIsAbstract     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            if (r6 == 0) goto L_0x016d
            int r6 = r4.mConstraintSetEnd     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            if (r6 != r9) goto L_0x015e
            r11.mDefaultTransition = r4     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            goto L_0x0163
        L_0x015e:
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionScene$Transition> r6 = r11.mAbstractTransitionList     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            r6.add(r4)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
        L_0x0163:
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionScene$Transition> r6 = r11.mTransitionList     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            r6.remove(r4)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            goto L_0x016d
        L_0x0169:
            r11.parseMotionSceneTags(r12, r1)     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
        L_0x016d:
            goto L_0x0174
        L_0x016e:
            java.lang.String r6 = r1.getName()     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            r2 = r6
        L_0x0174:
            int r6 = r1.next()     // Catch:{ XmlPullParserException -> 0x0181, IOException -> 0x017c }
            r5 = r6
            goto L_0x000f
        L_0x017b:
            goto L_0x0185
        L_0x017c:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x0186
        L_0x0181:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0185:
        L_0x0186:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionScene.load(android.content.Context, int):void");
    }

    /* access modifiers changed from: private */
    public int parseInclude(Context context, int resourceId) {
        XmlResourceParser xml = context.getResources().getXml(resourceId);
        try {
            for (int eventType = xml.getEventType(); eventType != 1; eventType = xml.next()) {
                String name = xml.getName();
                if (2 == eventType && CONSTRAINTSET_TAG.equals(name)) {
                    return parseConstraintSet(context, xml);
                }
            }
            return -1;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e2) {
            e2.printStackTrace();
            return -1;
        }
    }

    private void parseInclude(Context context, XmlPullParser mainParser) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(mainParser), R.styleable.include);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = obtainStyledAttributes.getIndex(i);
            if (index == R.styleable.include_constraintSet) {
                parseInclude(context, obtainStyledAttributes.getResourceId(index, -1));
            }
        }
        obtainStyledAttributes.recycle();
    }

    private void parseMotionSceneTags(Context context, XmlPullParser parser) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(parser), R.styleable.MotionScene);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = obtainStyledAttributes.getIndex(i);
            if (index == R.styleable.MotionScene_defaultDuration) {
                int i2 = obtainStyledAttributes.getInt(index, this.mDefaultDuration);
                this.mDefaultDuration = i2;
                if (i2 < 8) {
                    this.mDefaultDuration = 8;
                }
            } else if (index == R.styleable.MotionScene_layoutDuringTransition) {
                this.mLayoutDuringTransition = obtainStyledAttributes.getInteger(index, 0);
            }
        }
        obtainStyledAttributes.recycle();
    }

    public static String stripID(String id) {
        if (id == null) {
            return HttpUrl.FRAGMENT_ENCODE_SET;
        }
        int indexOf = id.indexOf(47);
        return indexOf < 0 ? id : id.substring(indexOf + 1);
    }

    public void addOnClickListeners(MotionLayout motionLayout, int currentState) {
        Iterator<Transition> it = this.mTransitionList.iterator();
        while (it.hasNext()) {
            Transition next = it.next();
            if (next.mOnClicks.size() > 0) {
                Iterator it2 = next.mOnClicks.iterator();
                while (it2.hasNext()) {
                    ((Transition.TransitionOnClick) it2.next()).removeOnClickListeners(motionLayout);
                }
            }
        }
        Iterator<Transition> it3 = this.mAbstractTransitionList.iterator();
        while (it3.hasNext()) {
            Transition next2 = it3.next();
            if (next2.mOnClicks.size() > 0) {
                Iterator it4 = next2.mOnClicks.iterator();
                while (it4.hasNext()) {
                    ((Transition.TransitionOnClick) it4.next()).removeOnClickListeners(motionLayout);
                }
            }
        }
        Iterator<Transition> it5 = this.mTransitionList.iterator();
        while (it5.hasNext()) {
            Transition next3 = it5.next();
            if (next3.mOnClicks.size() > 0) {
                Iterator it6 = next3.mOnClicks.iterator();
                while (it6.hasNext()) {
                    ((Transition.TransitionOnClick) it6.next()).addOnClickListeners(motionLayout, currentState, next3);
                }
            }
        }
        Iterator<Transition> it7 = this.mAbstractTransitionList.iterator();
        while (it7.hasNext()) {
            Transition next4 = it7.next();
            if (next4.mOnClicks.size() > 0) {
                Iterator it8 = next4.mOnClicks.iterator();
                while (it8.hasNext()) {
                    ((Transition.TransitionOnClick) it8.next()).addOnClickListeners(motionLayout, currentState, next4);
                }
            }
        }
    }

    public void addTransition(Transition transition) {
        int index = getIndex(transition);
        if (index == -1) {
            this.mTransitionList.add(transition);
        } else {
            this.mTransitionList.set(index, transition);
        }
    }

    public boolean applyViewTransition(int viewTransitionId, MotionController motionController) {
        return this.mViewTransitionController.applyViewTransition(viewTransitionId, motionController);
    }

    /* access modifiers changed from: package-private */
    public boolean autoTransition(MotionLayout motionLayout, int currentState) {
        Transition transition;
        if (isProcessingTouch() || this.mDisableAutoTransition) {
            return false;
        }
        Iterator<Transition> it = this.mTransitionList.iterator();
        while (it.hasNext()) {
            Transition next = it.next();
            if (next.mAutoTransition != 0 && ((transition = this.mCurrentTransition) != next || !transition.isTransitionFlag(2))) {
                if (currentState == next.mConstraintSetStart && (next.mAutoTransition == 4 || next.mAutoTransition == 2)) {
                    motionLayout.setState(MotionLayout.TransitionState.FINISHED);
                    motionLayout.setTransition(next);
                    if (next.mAutoTransition == 4) {
                        motionLayout.transitionToEnd();
                        motionLayout.setState(MotionLayout.TransitionState.SETUP);
                        motionLayout.setState(MotionLayout.TransitionState.MOVING);
                    } else {
                        motionLayout.setProgress(1.0f);
                        motionLayout.evaluate(true);
                        motionLayout.setState(MotionLayout.TransitionState.SETUP);
                        motionLayout.setState(MotionLayout.TransitionState.MOVING);
                        motionLayout.setState(MotionLayout.TransitionState.FINISHED);
                        motionLayout.onNewStateAttachHandlers();
                    }
                    return true;
                } else if (currentState == next.mConstraintSetEnd && (next.mAutoTransition == 3 || next.mAutoTransition == 1)) {
                    motionLayout.setState(MotionLayout.TransitionState.FINISHED);
                    motionLayout.setTransition(next);
                    if (next.mAutoTransition == 3) {
                        motionLayout.transitionToStart();
                        motionLayout.setState(MotionLayout.TransitionState.SETUP);
                        motionLayout.setState(MotionLayout.TransitionState.MOVING);
                    } else {
                        motionLayout.setProgress(0.0f);
                        motionLayout.evaluate(true);
                        motionLayout.setState(MotionLayout.TransitionState.SETUP);
                        motionLayout.setState(MotionLayout.TransitionState.MOVING);
                        motionLayout.setState(MotionLayout.TransitionState.FINISHED);
                        motionLayout.onNewStateAttachHandlers();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public Transition bestTransitionFor(int currentState, float dx, float dy, MotionEvent lastTouchDown) {
        Iterator<Transition> it;
        RectF rectF;
        RectF limitBoundsTo;
        float f;
        int i = currentState;
        float f2 = dx;
        float f3 = dy;
        if (i == -1) {
            return this.mCurrentTransition;
        }
        List<Transition> transitionsWithState = getTransitionsWithState(currentState);
        float f4 = 0.0f;
        Transition transition = null;
        RectF rectF2 = new RectF();
        Iterator<Transition> it2 = transitionsWithState.iterator();
        while (it2.hasNext()) {
            Transition next = it2.next();
            if (!next.mDisable) {
                if (next.mTouchResponse != null) {
                    next.mTouchResponse.setRTL(this.mRtl);
                    RectF touchRegion = next.mTouchResponse.getTouchRegion(this.mMotionLayout, rectF2);
                    if ((touchRegion == null || lastTouchDown == null || touchRegion.contains(lastTouchDown.getX(), lastTouchDown.getY())) && ((limitBoundsTo = next.mTouchResponse.getLimitBoundsTo(this.mMotionLayout, rectF2)) == null || lastTouchDown == null || limitBoundsTo.contains(lastTouchDown.getX(), lastTouchDown.getY()))) {
                        float dot = next.mTouchResponse.dot(f2, f3);
                        if (!next.mTouchResponse.mIsRotateMode || lastTouchDown == null) {
                            rectF = rectF2;
                            it = it2;
                            RectF rectF3 = limitBoundsTo;
                            f = dot;
                        } else {
                            float x = lastTouchDown.getX() - next.mTouchResponse.mRotateCenterX;
                            float y = lastTouchDown.getY() - next.mTouchResponse.mRotateCenterY;
                            rectF = rectF2;
                            it = it2;
                            RectF rectF4 = limitBoundsTo;
                            float f5 = dot;
                            f = 10.0f * ((float) (Math.atan2((double) (f3 + y), (double) (f2 + x)) - Math.atan2((double) x, (double) y)));
                        }
                        float f6 = next.mConstraintSetEnd == i ? f * -1.0f : f * 1.1f;
                        if (f6 > f4) {
                            f4 = f6;
                            transition = next;
                        }
                    }
                } else {
                    rectF = rectF2;
                    it = it2;
                }
                f2 = dx;
                f3 = dy;
                rectF2 = rectF;
                it2 = it;
            }
        }
        return transition;
    }

    public void disableAutoTransition(boolean disable) {
        this.mDisableAutoTransition = disable;
    }

    public void enableViewTransition(int id, boolean enable) {
        this.mViewTransitionController.enableViewTransition(id, enable);
    }

    public int gatPathMotionArc() {
        Transition transition = this.mCurrentTransition;
        if (transition != null) {
            return transition.mPathMotionArc;
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public int getAutoCompleteMode() {
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return 0;
        }
        return this.mCurrentTransition.mTouchResponse.getAutoCompleteMode();
    }

    /* access modifiers changed from: package-private */
    public ConstraintSet getConstraintSet(int id) {
        return getConstraintSet(id, -1, -1);
    }

    public ConstraintSet getConstraintSet(Context context, String id) {
        if (this.DEBUG_DESKTOP) {
            System.out.println("id " + id);
            System.out.println("size " + this.mConstraintSetMap.size());
        }
        for (int i = 0; i < this.mConstraintSetMap.size(); i++) {
            int keyAt = this.mConstraintSetMap.keyAt(i);
            String resourceName = context.getResources().getResourceName(keyAt);
            if (this.DEBUG_DESKTOP) {
                System.out.println("Id for <" + i + "> is <" + resourceName + "> looking for <" + id + ">");
            }
            if (id.equals(resourceName)) {
                return this.mConstraintSetMap.get(keyAt);
            }
        }
        return null;
    }

    public int[] getConstraintSetIds() {
        int[] iArr = new int[this.mConstraintSetMap.size()];
        for (int i = 0; i < iArr.length; i++) {
            iArr[i] = this.mConstraintSetMap.keyAt(i);
        }
        return iArr;
    }

    public ArrayList<Transition> getDefinedTransitions() {
        return this.mTransitionList;
    }

    public int getDuration() {
        Transition transition = this.mCurrentTransition;
        return transition != null ? transition.mDuration : this.mDefaultDuration;
    }

    /* access modifiers changed from: package-private */
    public int getEndId() {
        Transition transition = this.mCurrentTransition;
        if (transition == null) {
            return -1;
        }
        return transition.mConstraintSetEnd;
    }

    /* access modifiers changed from: package-private */
    public Key getKeyFrame(Context context, int type, int target, int position) {
        Transition transition = this.mCurrentTransition;
        if (transition == null) {
            return null;
        }
        Iterator it = transition.mKeyFramesList.iterator();
        while (it.hasNext()) {
            KeyFrames keyFrames = (KeyFrames) it.next();
            Iterator<Integer> it2 = keyFrames.getKeys().iterator();
            while (true) {
                if (it2.hasNext()) {
                    Integer next = it2.next();
                    if (target == next.intValue()) {
                        Iterator<Key> it3 = keyFrames.getKeyFramesForView(next.intValue()).iterator();
                        while (it3.hasNext()) {
                            Key next2 = it3.next();
                            if (next2.mFramePosition == position && next2.mType == type) {
                                return next2;
                            }
                        }
                        continue;
                    }
                }
            }
        }
        return null;
    }

    public void getKeyFrames(MotionController motionController) {
        Transition transition = this.mCurrentTransition;
        if (transition == null) {
            Transition transition2 = this.mDefaultTransition;
            if (transition2 != null) {
                Iterator it = transition2.mKeyFramesList.iterator();
                while (it.hasNext()) {
                    ((KeyFrames) it.next()).addFrames(motionController);
                }
                return;
            }
            return;
        }
        Iterator it2 = transition.mKeyFramesList.iterator();
        while (it2.hasNext()) {
            ((KeyFrames) it2.next()).addFrames(motionController);
        }
    }

    /* access modifiers changed from: package-private */
    public float getMaxAcceleration() {
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return 0.0f;
        }
        return this.mCurrentTransition.mTouchResponse.getMaxAcceleration();
    }

    /* access modifiers changed from: package-private */
    public float getMaxVelocity() {
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return 0.0f;
        }
        return this.mCurrentTransition.mTouchResponse.getMaxVelocity();
    }

    /* access modifiers changed from: package-private */
    public boolean getMoveWhenScrollAtTop() {
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return false;
        }
        return this.mCurrentTransition.mTouchResponse.getMoveWhenScrollAtTop();
    }

    public float getPathPercent(View view, int position) {
        return 0.0f;
    }

    /* access modifiers changed from: package-private */
    public float getProgressDirection(float dx, float dy) {
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return 0.0f;
        }
        return this.mCurrentTransition.mTouchResponse.getProgressDirection(dx, dy);
    }

    /* access modifiers changed from: package-private */
    public int getSpringBoundary() {
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return 0;
        }
        return this.mCurrentTransition.mTouchResponse.getSpringBoundary();
    }

    /* access modifiers changed from: package-private */
    public float getSpringDamping() {
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return 0.0f;
        }
        return this.mCurrentTransition.mTouchResponse.getSpringDamping();
    }

    /* access modifiers changed from: package-private */
    public float getSpringMass() {
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return 0.0f;
        }
        return this.mCurrentTransition.mTouchResponse.getSpringMass();
    }

    /* access modifiers changed from: package-private */
    public float getSpringStiffiness() {
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return 0.0f;
        }
        return this.mCurrentTransition.mTouchResponse.getSpringStiffness();
    }

    /* access modifiers changed from: package-private */
    public float getSpringStopThreshold() {
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return 0.0f;
        }
        return this.mCurrentTransition.mTouchResponse.getSpringStopThreshold();
    }

    public float getStaggered() {
        Transition transition = this.mCurrentTransition;
        if (transition != null) {
            return transition.mStagger;
        }
        return 0.0f;
    }

    /* access modifiers changed from: package-private */
    public int getStartId() {
        Transition transition = this.mCurrentTransition;
        if (transition == null) {
            return -1;
        }
        return transition.mConstraintSetStart;
    }

    public Transition getTransitionById(int id) {
        Iterator<Transition> it = this.mTransitionList.iterator();
        while (it.hasNext()) {
            Transition next = it.next();
            if (next.mId == id) {
                return next;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public int getTransitionDirection(int stateId) {
        Iterator<Transition> it = this.mTransitionList.iterator();
        while (it.hasNext()) {
            if (it.next().mConstraintSetStart == stateId) {
                return 0;
            }
        }
        return 1;
    }

    public List<Transition> getTransitionsWithState(int stateId) {
        int stateId2 = getRealID(stateId);
        ArrayList arrayList = new ArrayList();
        Iterator<Transition> it = this.mTransitionList.iterator();
        while (it.hasNext()) {
            Transition next = it.next();
            if (next.mConstraintSetStart == stateId2 || next.mConstraintSetEnd == stateId2) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public boolean hasKeyFramePosition(View view, int position) {
        Transition transition = this.mCurrentTransition;
        if (transition == null) {
            return false;
        }
        Iterator it = transition.mKeyFramesList.iterator();
        while (it.hasNext()) {
            Iterator<Key> it2 = ((KeyFrames) it.next()).getKeyFramesForView(view.getId()).iterator();
            while (true) {
                if (it2.hasNext()) {
                    if (it2.next().mFramePosition == position) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isViewTransitionEnabled(int id) {
        return this.mViewTransitionController.isViewTransitionEnabled(id);
    }

    public int lookUpConstraintId(String id) {
        Integer num = this.mConstraintSetIdMap.get(id);
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    public String lookUpConstraintName(int id) {
        for (Map.Entry next : this.mConstraintSetIdMap.entrySet()) {
            Integer num = (Integer) next.getValue();
            if (num != null && num.intValue() == id) {
                return (String) next.getKey();
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    /* access modifiers changed from: package-private */
    public void processScrollMove(float dx, float dy) {
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.scrollMove(dx, dy);
        }
    }

    /* access modifiers changed from: package-private */
    public void processScrollUp(float dx, float dy) {
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.scrollUp(dx, dy);
        }
    }

    /* access modifiers changed from: package-private */
    public void processTouchEvent(MotionEvent event, int currentState, MotionLayout motionLayout) {
        MotionLayout.MotionTracker motionTracker;
        MotionEvent motionEvent;
        RectF rectF = new RectF();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = this.mMotionLayout.obtainVelocityTracker();
        }
        this.mVelocityTracker.addMovement(event);
        if (currentState != -1) {
            boolean z = false;
            switch (event.getAction()) {
                case 0:
                    this.mLastTouchX = event.getRawX();
                    this.mLastTouchY = event.getRawY();
                    this.mLastTouchDown = event;
                    this.mIgnoreTouch = false;
                    if (this.mCurrentTransition.mTouchResponse != null) {
                        RectF limitBoundsTo = this.mCurrentTransition.mTouchResponse.getLimitBoundsTo(this.mMotionLayout, rectF);
                        if (limitBoundsTo == null || limitBoundsTo.contains(this.mLastTouchDown.getX(), this.mLastTouchDown.getY())) {
                            RectF touchRegion = this.mCurrentTransition.mTouchResponse.getTouchRegion(this.mMotionLayout, rectF);
                            if (touchRegion == null || touchRegion.contains(this.mLastTouchDown.getX(), this.mLastTouchDown.getY())) {
                                this.mMotionOutsideRegion = false;
                            } else {
                                this.mMotionOutsideRegion = true;
                            }
                            this.mCurrentTransition.mTouchResponse.setDown(this.mLastTouchX, this.mLastTouchY);
                            return;
                        }
                        this.mLastTouchDown = null;
                        this.mIgnoreTouch = true;
                        return;
                    }
                    return;
                case 2:
                    if (!this.mIgnoreTouch) {
                        float rawY = event.getRawY() - this.mLastTouchY;
                        float rawX = event.getRawX() - this.mLastTouchX;
                        if ((((double) rawX) != 0.0d || ((double) rawY) != 0.0d) && (motionEvent = this.mLastTouchDown) != null) {
                            Transition bestTransitionFor = bestTransitionFor(currentState, rawX, rawY, motionEvent);
                            if (bestTransitionFor != null) {
                                motionLayout.setTransition(bestTransitionFor);
                                RectF touchRegion2 = this.mCurrentTransition.mTouchResponse.getTouchRegion(this.mMotionLayout, rectF);
                                if (touchRegion2 != null && !touchRegion2.contains(this.mLastTouchDown.getX(), this.mLastTouchDown.getY())) {
                                    z = true;
                                }
                                this.mMotionOutsideRegion = z;
                                this.mCurrentTransition.mTouchResponse.setUpTouchEvent(this.mLastTouchX, this.mLastTouchY);
                                break;
                            }
                        } else {
                            return;
                        }
                    }
                    break;
            }
        }
        if (!this.mIgnoreTouch) {
            Transition transition = this.mCurrentTransition;
            if (!(transition == null || transition.mTouchResponse == null || this.mMotionOutsideRegion)) {
                this.mCurrentTransition.mTouchResponse.processTouchEvent(event, this.mVelocityTracker, currentState, this);
            }
            this.mLastTouchX = event.getRawX();
            this.mLastTouchY = event.getRawY();
            if (event.getAction() == 1 && (motionTracker = this.mVelocityTracker) != null) {
                motionTracker.recycle();
                this.mVelocityTracker = null;
                if (motionLayout.mCurrentState != -1) {
                    autoTransition(motionLayout, motionLayout.mCurrentState);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void readFallback(MotionLayout motionLayout) {
        int i = 0;
        while (i < this.mConstraintSetMap.size()) {
            int keyAt = this.mConstraintSetMap.keyAt(i);
            if (hasCycleDependency(keyAt)) {
                Log.e(TypedValues.MotionScene.NAME, "Cannot be derived from yourself");
                return;
            } else {
                readConstraintChain(keyAt, motionLayout);
                i++;
            }
        }
    }

    public void removeTransition(Transition transition) {
        int index = getIndex(transition);
        if (index != -1) {
            this.mTransitionList.remove(index);
        }
    }

    public void setConstraintSet(int id, ConstraintSet set) {
        this.mConstraintSetMap.put(id, set);
    }

    public void setDuration(int duration) {
        Transition transition = this.mCurrentTransition;
        if (transition != null) {
            transition.setDuration(duration);
        } else {
            this.mDefaultDuration = duration;
        }
    }

    public void setKeyframe(View view, int position, String name, Object value) {
        Transition transition = this.mCurrentTransition;
        if (transition != null) {
            Iterator it = transition.mKeyFramesList.iterator();
            while (it.hasNext()) {
                Iterator<Key> it2 = ((KeyFrames) it.next()).getKeyFramesForView(view.getId()).iterator();
                while (it2.hasNext()) {
                    if (it2.next().mFramePosition == position) {
                        float f = 0.0f;
                        if (value != null) {
                            f = ((Float) value).floatValue();
                        }
                        if (f == 0.0f) {
                        }
                        name.equalsIgnoreCase("app:PerpendicularPath_percent");
                    }
                }
            }
        }
    }

    public void setRtl(boolean rtl) {
        this.mRtl = rtl;
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.setRTL(this.mRtl);
        }
    }

    /* access modifiers changed from: package-private */
    public void setTransition(int beginId, int endId) {
        int i = beginId;
        int i2 = endId;
        StateSet stateSet = this.mStateSet;
        if (stateSet != null) {
            int stateGetConstraintID = stateSet.stateGetConstraintID(beginId, -1, -1);
            if (stateGetConstraintID != -1) {
                i = stateGetConstraintID;
            }
            int stateGetConstraintID2 = this.mStateSet.stateGetConstraintID(endId, -1, -1);
            if (stateGetConstraintID2 != -1) {
                i2 = stateGetConstraintID2;
            }
        }
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mConstraintSetEnd != endId || this.mCurrentTransition.mConstraintSetStart != beginId) {
            Iterator<Transition> it = this.mTransitionList.iterator();
            while (it.hasNext()) {
                Transition next = it.next();
                if ((next.mConstraintSetEnd == i2 && next.mConstraintSetStart == i) || (next.mConstraintSetEnd == endId && next.mConstraintSetStart == beginId)) {
                    this.mCurrentTransition = next;
                    if (next != null && next.mTouchResponse != null) {
                        this.mCurrentTransition.mTouchResponse.setRTL(this.mRtl);
                        return;
                    }
                    return;
                }
            }
            Transition transition2 = this.mDefaultTransition;
            Iterator<Transition> it2 = this.mAbstractTransitionList.iterator();
            while (it2.hasNext()) {
                Transition next2 = it2.next();
                if (next2.mConstraintSetEnd == endId) {
                    transition2 = next2;
                }
            }
            Transition transition3 = new Transition(this, transition2);
            int unused = transition3.mConstraintSetStart = i;
            int unused2 = transition3.mConstraintSetEnd = i2;
            if (i != -1) {
                this.mTransitionList.add(transition3);
            }
            this.mCurrentTransition = transition3;
        }
    }

    public void setTransition(Transition transition) {
        this.mCurrentTransition = transition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.setRTL(this.mRtl);
        }
    }

    /* access modifiers changed from: package-private */
    public void setupTouch() {
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.setupTouch();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean supportTouch() {
        Iterator<Transition> it = this.mTransitionList.iterator();
        while (it.hasNext()) {
            if (it.next().mTouchResponse != null) {
                return true;
            }
        }
        Transition transition = this.mCurrentTransition;
        return (transition == null || transition.mTouchResponse == null) ? false : true;
    }

    public boolean validateLayout(MotionLayout layout) {
        return layout == this.mMotionLayout && layout.mScene == this;
    }

    public void viewTransition(int id, View... view) {
        this.mViewTransitionController.viewTransition(id, view);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int parseConstraintSet(android.content.Context r18, org.xmlpull.v1.XmlPullParser r19) {
        /*
            r17 = this;
            r1 = r17
            r2 = r18
            r3 = r19
            androidx.constraintlayout.widget.ConstraintSet r0 = new androidx.constraintlayout.widget.ConstraintSet
            r0.<init>()
            r4 = r0
            r5 = 0
            r4.setForceId(r5)
            int r6 = r19.getAttributeCount()
            r0 = -1
            r7 = -1
            r8 = 0
            r9 = r8
            r8 = r7
            r7 = r0
        L_0x001a:
            r11 = 1
            if (r9 >= r6) goto L_0x00fe
            java.lang.String r12 = r3.getAttributeName(r9)
            java.lang.String r13 = r3.getAttributeValue(r9)
            boolean r0 = r1.DEBUG_DESKTOP
            if (r0 == 0) goto L_0x0041
            java.io.PrintStream r0 = java.lang.System.out
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = "id string = "
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.StringBuilder r14 = r14.append(r13)
            java.lang.String r14 = r14.toString()
            r0.println(r14)
        L_0x0041:
            int r0 = r12.hashCode()
            r14 = 2
            switch(r0) {
                case -1496482599: goto L_0x005e;
                case -1153153640: goto L_0x0054;
                case 3355: goto L_0x004a;
                default: goto L_0x0049;
            }
        L_0x0049:
            goto L_0x0068
        L_0x004a:
            java.lang.String r0 = "id"
            boolean r0 = r12.equals(r0)
            if (r0 == 0) goto L_0x0049
            r0 = r5
            goto L_0x0069
        L_0x0054:
            java.lang.String r0 = "constraintRotate"
            boolean r0 = r12.equals(r0)
            if (r0 == 0) goto L_0x0049
            r0 = r14
            goto L_0x0069
        L_0x005e:
            java.lang.String r0 = "deriveConstraintsFrom"
            boolean r0 = r12.equals(r0)
            if (r0 == 0) goto L_0x0049
            r0 = r11
            goto L_0x0069
        L_0x0068:
            r0 = -1
        L_0x0069:
            switch(r0) {
                case 0: goto L_0x00dc;
                case 1: goto L_0x00d6;
                case 2: goto L_0x006e;
                default: goto L_0x006c;
            }
        L_0x006c:
            goto L_0x00fa
        L_0x006e:
            int r0 = java.lang.Integer.parseInt(r13)     // Catch:{ NumberFormatException -> 0x0076 }
            r4.mRotate = r0     // Catch:{ NumberFormatException -> 0x0076 }
            goto L_0x00fa
        L_0x0076:
            r0 = move-exception
            int r15 = r13.hashCode()
            r10 = 4
            r5 = 3
            switch(r15) {
                case -768416914: goto L_0x00af;
                case 3317767: goto L_0x00a4;
                case 3387192: goto L_0x0099;
                case 108511772: goto L_0x008d;
                case 1954540437: goto L_0x0081;
                default: goto L_0x0080;
            }
        L_0x0080:
            goto L_0x00bb
        L_0x0081:
            java.lang.String r15 = "x_right"
            boolean r15 = r13.equals(r15)
            if (r15 == 0) goto L_0x0080
            r16 = r5
            goto L_0x00bd
        L_0x008d:
            java.lang.String r15 = "right"
            boolean r15 = r13.equals(r15)
            if (r15 == 0) goto L_0x0080
            r16 = r11
            goto L_0x00bd
        L_0x0099:
            java.lang.String r15 = "none"
            boolean r15 = r13.equals(r15)
            if (r15 == 0) goto L_0x0080
            r16 = 0
            goto L_0x00bd
        L_0x00a4:
            java.lang.String r15 = "left"
            boolean r15 = r13.equals(r15)
            if (r15 == 0) goto L_0x0080
            r16 = r14
            goto L_0x00bd
        L_0x00af:
            java.lang.String r15 = "x_left"
            boolean r15 = r13.equals(r15)
            if (r15 == 0) goto L_0x0080
            r16 = r10
            goto L_0x00bd
        L_0x00bb:
            r16 = -1
        L_0x00bd:
            switch(r16) {
                case 0: goto L_0x00d2;
                case 1: goto L_0x00ce;
                case 2: goto L_0x00ca;
                case 3: goto L_0x00c6;
                case 4: goto L_0x00c2;
                default: goto L_0x00c0;
            }
        L_0x00c0:
            r5 = 0
            goto L_0x00fa
        L_0x00c2:
            r4.mRotate = r10
            r5 = 0
            goto L_0x00fa
        L_0x00c6:
            r4.mRotate = r5
            r5 = 0
            goto L_0x00fa
        L_0x00ca:
            r4.mRotate = r14
            r5 = 0
            goto L_0x00fa
        L_0x00ce:
            r4.mRotate = r11
            r5 = 0
            goto L_0x00fa
        L_0x00d2:
            r5 = 0
            r4.mRotate = r5
            goto L_0x00fa
        L_0x00d6:
            int r0 = r1.getId(r2, r13)
            r8 = r0
            goto L_0x00fa
        L_0x00dc:
            int r0 = r1.getId(r2, r13)
            java.util.HashMap<java.lang.String, java.lang.Integer> r7 = r1.mConstraintSetIdMap
            java.lang.String r10 = stripID(r13)
            mt.Log1F380D.a((java.lang.Object) r10)
            java.lang.Integer r11 = java.lang.Integer.valueOf(r0)
            r7.put(r10, r11)
            java.lang.String r7 = androidx.constraintlayout.motion.widget.Debug.getName((android.content.Context) r2, (int) r0)
            mt.Log1F380D.a((java.lang.Object) r7)
            r4.mIdString = r7
            r7 = r0
        L_0x00fa:
            int r9 = r9 + 1
            goto L_0x001a
        L_0x00fe:
            r5 = -1
            if (r7 == r5) goto L_0x011a
            androidx.constraintlayout.motion.widget.MotionLayout r0 = r1.mMotionLayout
            int r0 = r0.mDebugPath
            if (r0 == 0) goto L_0x010a
            r4.setValidateOnParse(r11)
        L_0x010a:
            r4.load((android.content.Context) r2, (org.xmlpull.v1.XmlPullParser) r3)
            r5 = -1
            if (r8 == r5) goto L_0x0115
            android.util.SparseIntArray r0 = r1.mDeriveMap
            r0.put(r7, r8)
        L_0x0115:
            android.util.SparseArray<androidx.constraintlayout.widget.ConstraintSet> r0 = r1.mConstraintSetMap
            r0.put(r7, r4)
        L_0x011a:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionScene.parseConstraintSet(android.content.Context, org.xmlpull.v1.XmlPullParser):int");
    }

    private void readConstraintChain(int key, MotionLayout motionLayout) {
        ConstraintSet constraintSet = this.mConstraintSetMap.get(key);
        constraintSet.derivedState = constraintSet.mIdString;
        int i = this.mDeriveMap.get(key);
        if (i > 0) {
            readConstraintChain(i, motionLayout);
            ConstraintSet constraintSet2 = this.mConstraintSetMap.get(i);
            if (constraintSet2 == null) {
                StringBuilder append = new StringBuilder().append("ERROR! invalid deriveConstraintsFrom: @id/");
                String name = Debug.getName(this.mMotionLayout.getContext(), i);
                Log1F380D.a((Object) name);
                Log.e(TypedValues.MotionScene.NAME, append.append(name).toString());
                return;
            }
            constraintSet.derivedState += "/" + constraintSet2.derivedState;
            constraintSet.readFallback(constraintSet2);
        } else {
            constraintSet.derivedState += "  layout";
            constraintSet.readFallback((ConstraintLayout) motionLayout);
        }
        constraintSet.applyDeltaFrom(constraintSet);
    }

    /* access modifiers changed from: package-private */
    public ConstraintSet getConstraintSet(int id, int width, int height) {
        int stateGetConstraintID;
        if (this.DEBUG_DESKTOP) {
            System.out.println("id " + id);
            System.out.println("size " + this.mConstraintSetMap.size());
        }
        StateSet stateSet = this.mStateSet;
        if (!(stateSet == null || (stateGetConstraintID = stateSet.stateGetConstraintID(id, width, height)) == -1)) {
            id = stateGetConstraintID;
        }
        if (this.mConstraintSetMap.get(id) != null) {
            return this.mConstraintSetMap.get(id);
        }
        StringBuilder append = new StringBuilder().append("Warning could not find ConstraintSet id/");
        String name = Debug.getName(this.mMotionLayout.getContext(), id);
        Log1F380D.a((Object) name);
        Log.e(TypedValues.MotionScene.NAME, append.append(name).append(" In MotionScene").toString());
        SparseArray<ConstraintSet> sparseArray = this.mConstraintSetMap;
        return sparseArray.get(sparseArray.keyAt(0));
    }

    public Interpolator getInterpolator() {
        switch (this.mCurrentTransition.mDefaultInterpolator) {
            case -2:
                return AnimationUtils.loadInterpolator(this.mMotionLayout.getContext(), this.mCurrentTransition.mDefaultInterpolatorID);
            case -1:
                String access$1600 = this.mCurrentTransition.mDefaultInterpolatorString;
                Log1F380D.a((Object) access$1600);
                final Easing interpolator = Easing.getInterpolator(access$1600);
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
}
