package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.core.motion.utils.Easing;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.HelperWidget;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.constraintlayout.widget.R;
import androidx.core.os.EnvironmentCompat;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import mt.Log1F380D;
import okhttp3.HttpUrl;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: 0028 */
public class ConstraintSet {
    private static final int ALPHA = 43;
    private static final int ANIMATE_CIRCLE_ANGLE_TO = 82;
    private static final int ANIMATE_RELATIVE_TO = 64;
    private static final int BARRIER_ALLOWS_GONE_WIDGETS = 75;
    private static final int BARRIER_DIRECTION = 72;
    private static final int BARRIER_MARGIN = 73;
    private static final int BARRIER_TYPE = 1;
    public static final int BASELINE = 5;
    private static final int BASELINE_MARGIN = 93;
    private static final int BASELINE_TO_BASELINE = 1;
    private static final int BASELINE_TO_BOTTOM = 92;
    private static final int BASELINE_TO_TOP = 91;
    public static final int BOTTOM = 4;
    private static final int BOTTOM_MARGIN = 2;
    private static final int BOTTOM_TO_BOTTOM = 3;
    private static final int BOTTOM_TO_TOP = 4;
    public static final int CHAIN_PACKED = 2;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    private static final int CHAIN_USE_RTL = 71;
    private static final int CIRCLE = 61;
    private static final int CIRCLE_ANGLE = 63;
    private static final int CIRCLE_RADIUS = 62;
    public static final int CIRCLE_REFERENCE = 8;
    private static final int CONSTRAINED_HEIGHT = 81;
    private static final int CONSTRAINED_WIDTH = 80;
    private static final int CONSTRAINT_REFERENCED_IDS = 74;
    private static final int CONSTRAINT_TAG = 77;
    private static final boolean DEBUG = false;
    private static final int DIMENSION_RATIO = 5;
    private static final int DRAW_PATH = 66;
    private static final int EDITOR_ABSOLUTE_X = 6;
    private static final int EDITOR_ABSOLUTE_Y = 7;
    private static final int ELEVATION = 44;
    public static final int END = 7;
    private static final int END_MARGIN = 8;
    private static final int END_TO_END = 9;
    private static final int END_TO_START = 10;
    private static final String ERROR_MESSAGE = "XML parser error must be within a Constraint ";
    public static final int GONE = 8;
    private static final int GONE_BASELINE_MARGIN = 94;
    private static final int GONE_BOTTOM_MARGIN = 11;
    private static final int GONE_END_MARGIN = 12;
    private static final int GONE_LEFT_MARGIN = 13;
    private static final int GONE_RIGHT_MARGIN = 14;
    private static final int GONE_START_MARGIN = 15;
    private static final int GONE_TOP_MARGIN = 16;
    private static final int GUIDELINE_USE_RTL = 99;
    private static final int GUIDE_BEGIN = 17;
    private static final int GUIDE_END = 18;
    private static final int GUIDE_PERCENT = 19;
    private static final int HEIGHT_DEFAULT = 55;
    private static final int HEIGHT_MAX = 57;
    private static final int HEIGHT_MIN = 59;
    private static final int HEIGHT_PERCENT = 70;
    public static final int HORIZONTAL = 0;
    private static final int HORIZONTAL_BIAS = 20;
    public static final int HORIZONTAL_GUIDELINE = 0;
    private static final int HORIZONTAL_STYLE = 41;
    private static final int HORIZONTAL_WEIGHT = 39;
    private static final int INTERNAL_MATCH_CONSTRAINT = -3;
    private static final int INTERNAL_MATCH_PARENT = -1;
    private static final int INTERNAL_WRAP_CONTENT = -2;
    private static final int INTERNAL_WRAP_CONTENT_CONSTRAINED = -4;
    public static final int INVISIBLE = 4;
    private static final String KEY_PERCENT_PARENT = "parent";
    private static final String KEY_RATIO = "ratio";
    private static final String KEY_WEIGHT = "weight";
    private static final int LAYOUT_CONSTRAINT_HEIGHT = 96;
    private static final int LAYOUT_CONSTRAINT_WIDTH = 95;
    private static final int LAYOUT_HEIGHT = 21;
    private static final int LAYOUT_VISIBILITY = 22;
    private static final int LAYOUT_WIDTH = 23;
    private static final int LAYOUT_WRAP_BEHAVIOR = 97;
    public static final int LEFT = 1;
    private static final int LEFT_MARGIN = 24;
    private static final int LEFT_TO_LEFT = 25;
    private static final int LEFT_TO_RIGHT = 26;
    public static final int MATCH_CONSTRAINT = 0;
    public static final int MATCH_CONSTRAINT_PERCENT = 2;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    private static final int MOTION_STAGGER = 79;
    private static final int MOTION_TARGET = 98;
    private static final int ORIENTATION = 27;
    public static final int PARENT_ID = 0;
    private static final int PATH_MOTION_ARC = 76;
    private static final int PROGRESS = 68;
    private static final int QUANTIZE_MOTION_INTERPOLATOR = 86;
    private static final int QUANTIZE_MOTION_INTERPOLATOR_ID = 89;
    private static final int QUANTIZE_MOTION_INTERPOLATOR_STR = 90;
    private static final int QUANTIZE_MOTION_INTERPOLATOR_TYPE = 88;
    private static final int QUANTIZE_MOTION_PHASE = 85;
    private static final int QUANTIZE_MOTION_STEPS = 84;
    public static final int RIGHT = 2;
    private static final int RIGHT_MARGIN = 28;
    private static final int RIGHT_TO_LEFT = 29;
    private static final int RIGHT_TO_RIGHT = 30;
    public static final int ROTATE_LEFT_OF_PORTRATE = 4;
    public static final int ROTATE_NONE = 0;
    public static final int ROTATE_PORTRATE_OF_LEFT = 2;
    public static final int ROTATE_PORTRATE_OF_RIGHT = 1;
    public static final int ROTATE_RIGHT_OF_PORTRATE = 3;
    private static final int ROTATION = 60;
    private static final int ROTATION_X = 45;
    private static final int ROTATION_Y = 46;
    private static final int SCALE_X = 47;
    private static final int SCALE_Y = 48;
    public static final int START = 6;
    private static final int START_MARGIN = 31;
    private static final int START_TO_END = 32;
    private static final int START_TO_START = 33;
    private static final String TAG = "ConstraintSet";
    public static final int TOP = 3;
    private static final int TOP_MARGIN = 34;
    private static final int TOP_TO_BOTTOM = 35;
    private static final int TOP_TO_TOP = 36;
    private static final int TRANSFORM_PIVOT_TARGET = 83;
    private static final int TRANSFORM_PIVOT_X = 49;
    private static final int TRANSFORM_PIVOT_Y = 50;
    private static final int TRANSITION_EASING = 65;
    private static final int TRANSITION_PATH_ROTATE = 67;
    private static final int TRANSLATION_X = 51;
    private static final int TRANSLATION_Y = 52;
    private static final int TRANSLATION_Z = 53;
    public static final int UNSET = -1;
    private static final int UNUSED = 87;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_BIAS = 37;
    public static final int VERTICAL_GUIDELINE = 1;
    private static final int VERTICAL_STYLE = 42;
    private static final int VERTICAL_WEIGHT = 40;
    private static final int VIEW_ID = 38;
    /* access modifiers changed from: private */
    public static final int[] VISIBILITY_FLAGS = {0, 4, 8};
    private static final int VISIBILITY_MODE = 78;
    public static final int VISIBILITY_MODE_IGNORE = 1;
    public static final int VISIBILITY_MODE_NORMAL = 0;
    public static final int VISIBLE = 0;
    private static final int WIDTH_DEFAULT = 54;
    private static final int WIDTH_MAX = 56;
    private static final int WIDTH_MIN = 58;
    private static final int WIDTH_PERCENT = 69;
    public static final int WRAP_CONTENT = -2;
    private static SparseIntArray mapToConstant = new SparseIntArray();
    private static SparseIntArray overrideMapToConstant = new SparseIntArray();
    public String derivedState = HttpUrl.FRAGMENT_ENCODE_SET;
    /* access modifiers changed from: private */
    public HashMap<Integer, Constraint> mConstraints = new HashMap<>();
    private boolean mForceId = true;
    public String mIdString;
    public int mRotate = 0;
    private HashMap<String, ConstraintAttribute> mSavedAttributes = new HashMap<>();
    private boolean mValidate;

    public static class Constraint {
        public final Layout layout = new Layout();
        public HashMap<String, ConstraintAttribute> mCustomConstraints = new HashMap<>();
        Delta mDelta;
        String mTargetString;
        int mViewId;
        public final Motion motion = new Motion();
        public final PropertySet propertySet = new PropertySet();
        public final Transform transform = new Transform();

        static class Delta {
            private static final int INITIAL_BOOLEAN = 4;
            private static final int INITIAL_FLOAT = 10;
            private static final int INITIAL_INT = 10;
            private static final int INITIAL_STRING = 5;
            int mCountBoolean = 0;
            int mCountFloat = 0;
            int mCountInt = 0;
            int mCountString = 0;
            int[] mTypeBoolean = new int[4];
            int[] mTypeFloat = new int[10];
            int[] mTypeInt = new int[10];
            int[] mTypeString = new int[5];
            boolean[] mValueBoolean = new boolean[4];
            float[] mValueFloat = new float[10];
            int[] mValueInt = new int[10];
            String[] mValueString = new String[5];

            Delta() {
            }

            /* access modifiers changed from: package-private */
            public void add(int type, float value) {
                int i = this.mCountFloat;
                int[] iArr = this.mTypeFloat;
                if (i >= iArr.length) {
                    this.mTypeFloat = Arrays.copyOf(iArr, iArr.length * 2);
                    float[] fArr = this.mValueFloat;
                    this.mValueFloat = Arrays.copyOf(fArr, fArr.length * 2);
                }
                int[] iArr2 = this.mTypeFloat;
                int i2 = this.mCountFloat;
                iArr2[i2] = type;
                float[] fArr2 = this.mValueFloat;
                this.mCountFloat = i2 + 1;
                fArr2[i2] = value;
            }

            /* access modifiers changed from: package-private */
            public void add(int type, int value) {
                int i = this.mCountInt;
                int[] iArr = this.mTypeInt;
                if (i >= iArr.length) {
                    this.mTypeInt = Arrays.copyOf(iArr, iArr.length * 2);
                    int[] iArr2 = this.mValueInt;
                    this.mValueInt = Arrays.copyOf(iArr2, iArr2.length * 2);
                }
                int[] iArr3 = this.mTypeInt;
                int i2 = this.mCountInt;
                iArr3[i2] = type;
                int[] iArr4 = this.mValueInt;
                this.mCountInt = i2 + 1;
                iArr4[i2] = value;
            }

            /* access modifiers changed from: package-private */
            public void add(int type, String value) {
                int i = this.mCountString;
                int[] iArr = this.mTypeString;
                if (i >= iArr.length) {
                    this.mTypeString = Arrays.copyOf(iArr, iArr.length * 2);
                    String[] strArr = this.mValueString;
                    this.mValueString = (String[]) Arrays.copyOf(strArr, strArr.length * 2);
                }
                int[] iArr2 = this.mTypeString;
                int i2 = this.mCountString;
                iArr2[i2] = type;
                String[] strArr2 = this.mValueString;
                this.mCountString = i2 + 1;
                strArr2[i2] = value;
            }

            /* access modifiers changed from: package-private */
            public void add(int type, boolean value) {
                int i = this.mCountBoolean;
                int[] iArr = this.mTypeBoolean;
                if (i >= iArr.length) {
                    this.mTypeBoolean = Arrays.copyOf(iArr, iArr.length * 2);
                    boolean[] zArr = this.mValueBoolean;
                    this.mValueBoolean = Arrays.copyOf(zArr, zArr.length * 2);
                }
                int[] iArr2 = this.mTypeBoolean;
                int i2 = this.mCountBoolean;
                iArr2[i2] = type;
                boolean[] zArr2 = this.mValueBoolean;
                this.mCountBoolean = i2 + 1;
                zArr2[i2] = value;
            }

            /* access modifiers changed from: package-private */
            public void applyDelta(Constraint c) {
                for (int i = 0; i < this.mCountInt; i++) {
                    ConstraintSet.setDeltaValue(c, this.mTypeInt[i], this.mValueInt[i]);
                }
                for (int i2 = 0; i2 < this.mCountFloat; i2++) {
                    ConstraintSet.setDeltaValue(c, this.mTypeFloat[i2], this.mValueFloat[i2]);
                }
                for (int i3 = 0; i3 < this.mCountString; i3++) {
                    ConstraintSet.setDeltaValue(c, this.mTypeString[i3], this.mValueString[i3]);
                }
                for (int i4 = 0; i4 < this.mCountBoolean; i4++) {
                    ConstraintSet.setDeltaValue(c, this.mTypeBoolean[i4], this.mValueBoolean[i4]);
                }
            }

            /* access modifiers changed from: package-private */
            public void printDelta(String tag) {
                Log.v(tag, "int");
                for (int i = 0; i < this.mCountInt; i++) {
                    Log.v(tag, this.mTypeInt[i] + " = " + this.mValueInt[i]);
                }
                Log.v(tag, TypedValues.Custom.S_FLOAT);
                for (int i2 = 0; i2 < this.mCountFloat; i2++) {
                    Log.v(tag, this.mTypeFloat[i2] + " = " + this.mValueFloat[i2]);
                }
                Log.v(tag, "strings");
                for (int i3 = 0; i3 < this.mCountString; i3++) {
                    Log.v(tag, this.mTypeString[i3] + " = " + this.mValueString[i3]);
                }
                Log.v(tag, TypedValues.Custom.S_BOOLEAN);
                for (int i4 = 0; i4 < this.mCountBoolean; i4++) {
                    Log.v(tag, this.mTypeBoolean[i4] + " = " + this.mValueBoolean[i4]);
                }
            }
        }

        /* access modifiers changed from: private */
        public void fillFrom(int viewId, ConstraintLayout.LayoutParams param) {
            this.mViewId = viewId;
            this.layout.leftToLeft = param.leftToLeft;
            this.layout.leftToRight = param.leftToRight;
            this.layout.rightToLeft = param.rightToLeft;
            this.layout.rightToRight = param.rightToRight;
            this.layout.topToTop = param.topToTop;
            this.layout.topToBottom = param.topToBottom;
            this.layout.bottomToTop = param.bottomToTop;
            this.layout.bottomToBottom = param.bottomToBottom;
            this.layout.baselineToBaseline = param.baselineToBaseline;
            this.layout.baselineToTop = param.baselineToTop;
            this.layout.baselineToBottom = param.baselineToBottom;
            this.layout.startToEnd = param.startToEnd;
            this.layout.startToStart = param.startToStart;
            this.layout.endToStart = param.endToStart;
            this.layout.endToEnd = param.endToEnd;
            this.layout.horizontalBias = param.horizontalBias;
            this.layout.verticalBias = param.verticalBias;
            this.layout.dimensionRatio = param.dimensionRatio;
            this.layout.circleConstraint = param.circleConstraint;
            this.layout.circleRadius = param.circleRadius;
            this.layout.circleAngle = param.circleAngle;
            this.layout.editorAbsoluteX = param.editorAbsoluteX;
            this.layout.editorAbsoluteY = param.editorAbsoluteY;
            this.layout.orientation = param.orientation;
            this.layout.guidePercent = param.guidePercent;
            this.layout.guideBegin = param.guideBegin;
            this.layout.guideEnd = param.guideEnd;
            this.layout.mWidth = param.width;
            this.layout.mHeight = param.height;
            this.layout.leftMargin = param.leftMargin;
            this.layout.rightMargin = param.rightMargin;
            this.layout.topMargin = param.topMargin;
            this.layout.bottomMargin = param.bottomMargin;
            this.layout.baselineMargin = param.baselineMargin;
            this.layout.verticalWeight = param.verticalWeight;
            this.layout.horizontalWeight = param.horizontalWeight;
            this.layout.verticalChainStyle = param.verticalChainStyle;
            this.layout.horizontalChainStyle = param.horizontalChainStyle;
            this.layout.constrainedWidth = param.constrainedWidth;
            this.layout.constrainedHeight = param.constrainedHeight;
            this.layout.widthDefault = param.matchConstraintDefaultWidth;
            this.layout.heightDefault = param.matchConstraintDefaultHeight;
            this.layout.widthMax = param.matchConstraintMaxWidth;
            this.layout.heightMax = param.matchConstraintMaxHeight;
            this.layout.widthMin = param.matchConstraintMinWidth;
            this.layout.heightMin = param.matchConstraintMinHeight;
            this.layout.widthPercent = param.matchConstraintPercentWidth;
            this.layout.heightPercent = param.matchConstraintPercentHeight;
            this.layout.mConstraintTag = param.constraintTag;
            this.layout.goneTopMargin = param.goneTopMargin;
            this.layout.goneBottomMargin = param.goneBottomMargin;
            this.layout.goneLeftMargin = param.goneLeftMargin;
            this.layout.goneRightMargin = param.goneRightMargin;
            this.layout.goneStartMargin = param.goneStartMargin;
            this.layout.goneEndMargin = param.goneEndMargin;
            this.layout.goneBaselineMargin = param.goneBaselineMargin;
            this.layout.mWrapBehavior = param.wrapBehaviorInParent;
            if (Build.VERSION.SDK_INT >= 17) {
                this.layout.endMargin = param.getMarginEnd();
                this.layout.startMargin = param.getMarginStart();
            }
        }

        /* access modifiers changed from: private */
        public void fillFromConstraints(int viewId, Constraints.LayoutParams param) {
            fillFrom(viewId, param);
            this.propertySet.alpha = param.alpha;
            this.transform.rotation = param.rotation;
            this.transform.rotationX = param.rotationX;
            this.transform.rotationY = param.rotationY;
            this.transform.scaleX = param.scaleX;
            this.transform.scaleY = param.scaleY;
            this.transform.transformPivotX = param.transformPivotX;
            this.transform.transformPivotY = param.transformPivotY;
            this.transform.translationX = param.translationX;
            this.transform.translationY = param.translationY;
            this.transform.translationZ = param.translationZ;
            this.transform.elevation = param.elevation;
            this.transform.applyElevation = param.applyElevation;
        }

        /* access modifiers changed from: private */
        public void fillFromConstraints(ConstraintHelper helper, int viewId, Constraints.LayoutParams param) {
            fillFromConstraints(viewId, param);
            if (helper instanceof Barrier) {
                this.layout.mHelperType = 1;
                Barrier barrier = (Barrier) helper;
                this.layout.mBarrierDirection = barrier.getType();
                this.layout.mReferenceIds = barrier.getReferencedIds();
                this.layout.mBarrierMargin = barrier.getMargin();
            }
        }

        private ConstraintAttribute get(String attributeName, ConstraintAttribute.AttributeType attributeType) {
            if (this.mCustomConstraints.containsKey(attributeName)) {
                ConstraintAttribute constraintAttribute = this.mCustomConstraints.get(attributeName);
                if (constraintAttribute.getType() == attributeType) {
                    return constraintAttribute;
                }
                throw new IllegalArgumentException("ConstraintAttribute is already a " + constraintAttribute.getType().name());
            }
            ConstraintAttribute constraintAttribute2 = new ConstraintAttribute(attributeName, attributeType);
            this.mCustomConstraints.put(attributeName, constraintAttribute2);
            return constraintAttribute2;
        }

        /* access modifiers changed from: private */
        public void setColorValue(String attributeName, int value) {
            get(attributeName, ConstraintAttribute.AttributeType.COLOR_TYPE).setColorValue(value);
        }

        /* access modifiers changed from: private */
        public void setFloatValue(String attributeName, float value) {
            get(attributeName, ConstraintAttribute.AttributeType.FLOAT_TYPE).setFloatValue(value);
        }

        /* access modifiers changed from: private */
        public void setIntValue(String attributeName, int value) {
            get(attributeName, ConstraintAttribute.AttributeType.INT_TYPE).setIntValue(value);
        }

        /* access modifiers changed from: private */
        public void setStringValue(String attributeName, String value) {
            get(attributeName, ConstraintAttribute.AttributeType.STRING_TYPE).setStringValue(value);
        }

        public void applyDelta(Constraint c) {
            Delta delta = this.mDelta;
            if (delta != null) {
                delta.applyDelta(c);
            }
        }

        public void applyTo(ConstraintLayout.LayoutParams param) {
            param.leftToLeft = this.layout.leftToLeft;
            param.leftToRight = this.layout.leftToRight;
            param.rightToLeft = this.layout.rightToLeft;
            param.rightToRight = this.layout.rightToRight;
            param.topToTop = this.layout.topToTop;
            param.topToBottom = this.layout.topToBottom;
            param.bottomToTop = this.layout.bottomToTop;
            param.bottomToBottom = this.layout.bottomToBottom;
            param.baselineToBaseline = this.layout.baselineToBaseline;
            param.baselineToTop = this.layout.baselineToTop;
            param.baselineToBottom = this.layout.baselineToBottom;
            param.startToEnd = this.layout.startToEnd;
            param.startToStart = this.layout.startToStart;
            param.endToStart = this.layout.endToStart;
            param.endToEnd = this.layout.endToEnd;
            param.leftMargin = this.layout.leftMargin;
            param.rightMargin = this.layout.rightMargin;
            param.topMargin = this.layout.topMargin;
            param.bottomMargin = this.layout.bottomMargin;
            param.goneStartMargin = this.layout.goneStartMargin;
            param.goneEndMargin = this.layout.goneEndMargin;
            param.goneTopMargin = this.layout.goneTopMargin;
            param.goneBottomMargin = this.layout.goneBottomMargin;
            param.horizontalBias = this.layout.horizontalBias;
            param.verticalBias = this.layout.verticalBias;
            param.circleConstraint = this.layout.circleConstraint;
            param.circleRadius = this.layout.circleRadius;
            param.circleAngle = this.layout.circleAngle;
            param.dimensionRatio = this.layout.dimensionRatio;
            param.editorAbsoluteX = this.layout.editorAbsoluteX;
            param.editorAbsoluteY = this.layout.editorAbsoluteY;
            param.verticalWeight = this.layout.verticalWeight;
            param.horizontalWeight = this.layout.horizontalWeight;
            param.verticalChainStyle = this.layout.verticalChainStyle;
            param.horizontalChainStyle = this.layout.horizontalChainStyle;
            param.constrainedWidth = this.layout.constrainedWidth;
            param.constrainedHeight = this.layout.constrainedHeight;
            param.matchConstraintDefaultWidth = this.layout.widthDefault;
            param.matchConstraintDefaultHeight = this.layout.heightDefault;
            param.matchConstraintMaxWidth = this.layout.widthMax;
            param.matchConstraintMaxHeight = this.layout.heightMax;
            param.matchConstraintMinWidth = this.layout.widthMin;
            param.matchConstraintMinHeight = this.layout.heightMin;
            param.matchConstraintPercentWidth = this.layout.widthPercent;
            param.matchConstraintPercentHeight = this.layout.heightPercent;
            param.orientation = this.layout.orientation;
            param.guidePercent = this.layout.guidePercent;
            param.guideBegin = this.layout.guideBegin;
            param.guideEnd = this.layout.guideEnd;
            param.width = this.layout.mWidth;
            param.height = this.layout.mHeight;
            if (this.layout.mConstraintTag != null) {
                param.constraintTag = this.layout.mConstraintTag;
            }
            param.wrapBehaviorInParent = this.layout.mWrapBehavior;
            if (Build.VERSION.SDK_INT >= 17) {
                param.setMarginStart(this.layout.startMargin);
                param.setMarginEnd(this.layout.endMargin);
            }
            param.validate();
        }

        public Constraint clone() {
            Constraint constraint = new Constraint();
            constraint.layout.copyFrom(this.layout);
            constraint.motion.copyFrom(this.motion);
            constraint.propertySet.copyFrom(this.propertySet);
            constraint.transform.copyFrom(this.transform);
            constraint.mViewId = this.mViewId;
            constraint.mDelta = this.mDelta;
            return constraint;
        }

        public void printDelta(String tag) {
            Delta delta = this.mDelta;
            if (delta != null) {
                delta.printDelta(tag);
            } else {
                Log.v(tag, "DELTA IS NULL");
            }
        }
    }

    /* compiled from: 0027 */
    public static class Layout {
        private static final int BARRIER_ALLOWS_GONE_WIDGETS = 75;
        private static final int BARRIER_DIRECTION = 72;
        private static final int BARRIER_MARGIN = 73;
        private static final int BASELINE_MARGIN = 80;
        private static final int BASELINE_TO_BASELINE = 1;
        private static final int BASELINE_TO_BOTTOM = 78;
        private static final int BASELINE_TO_TOP = 77;
        private static final int BOTTOM_MARGIN = 2;
        private static final int BOTTOM_TO_BOTTOM = 3;
        private static final int BOTTOM_TO_TOP = 4;
        private static final int CHAIN_USE_RTL = 71;
        private static final int CIRCLE = 61;
        private static final int CIRCLE_ANGLE = 63;
        private static final int CIRCLE_RADIUS = 62;
        private static final int CONSTRAINED_HEIGHT = 88;
        private static final int CONSTRAINED_WIDTH = 87;
        private static final int CONSTRAINT_REFERENCED_IDS = 74;
        private static final int CONSTRAINT_TAG = 89;
        private static final int DIMENSION_RATIO = 5;
        private static final int EDITOR_ABSOLUTE_X = 6;
        private static final int EDITOR_ABSOLUTE_Y = 7;
        private static final int END_MARGIN = 8;
        private static final int END_TO_END = 9;
        private static final int END_TO_START = 10;
        private static final int GONE_BASELINE_MARGIN = 79;
        private static final int GONE_BOTTOM_MARGIN = 11;
        private static final int GONE_END_MARGIN = 12;
        private static final int GONE_LEFT_MARGIN = 13;
        private static final int GONE_RIGHT_MARGIN = 14;
        private static final int GONE_START_MARGIN = 15;
        private static final int GONE_TOP_MARGIN = 16;
        private static final int GUIDE_BEGIN = 17;
        private static final int GUIDE_END = 18;
        private static final int GUIDE_PERCENT = 19;
        private static final int GUIDE_USE_RTL = 90;
        private static final int HEIGHT_DEFAULT = 82;
        private static final int HEIGHT_MAX = 83;
        private static final int HEIGHT_MIN = 85;
        private static final int HEIGHT_PERCENT = 70;
        private static final int HORIZONTAL_BIAS = 20;
        private static final int HORIZONTAL_STYLE = 39;
        private static final int HORIZONTAL_WEIGHT = 37;
        private static final int LAYOUT_CONSTRAINT_HEIGHT = 42;
        private static final int LAYOUT_CONSTRAINT_WIDTH = 41;
        private static final int LAYOUT_HEIGHT = 21;
        private static final int LAYOUT_WIDTH = 22;
        private static final int LAYOUT_WRAP_BEHAVIOR = 76;
        private static final int LEFT_MARGIN = 23;
        private static final int LEFT_TO_LEFT = 24;
        private static final int LEFT_TO_RIGHT = 25;
        private static final int ORIENTATION = 26;
        private static final int RIGHT_MARGIN = 27;
        private static final int RIGHT_TO_LEFT = 28;
        private static final int RIGHT_TO_RIGHT = 29;
        private static final int START_MARGIN = 30;
        private static final int START_TO_END = 31;
        private static final int START_TO_START = 32;
        private static final int TOP_MARGIN = 33;
        private static final int TOP_TO_BOTTOM = 34;
        private static final int TOP_TO_TOP = 35;
        public static final int UNSET = -1;
        public static final int UNSET_GONE_MARGIN = Integer.MIN_VALUE;
        private static final int UNUSED = 91;
        private static final int VERTICAL_BIAS = 36;
        private static final int VERTICAL_STYLE = 40;
        private static final int VERTICAL_WEIGHT = 38;
        private static final int WIDTH_DEFAULT = 81;
        private static final int WIDTH_MAX = 84;
        private static final int WIDTH_MIN = 86;
        private static final int WIDTH_PERCENT = 69;
        private static SparseIntArray mapToConstant;
        public int baselineMargin = 0;
        public int baselineToBaseline = -1;
        public int baselineToBottom = -1;
        public int baselineToTop = -1;
        public int bottomMargin = 0;
        public int bottomToBottom = -1;
        public int bottomToTop = -1;
        public float circleAngle = 0.0f;
        public int circleConstraint = -1;
        public int circleRadius = 0;
        public boolean constrainedHeight = false;
        public boolean constrainedWidth = false;
        public String dimensionRatio = null;
        public int editorAbsoluteX = -1;
        public int editorAbsoluteY = -1;
        public int endMargin = 0;
        public int endToEnd = -1;
        public int endToStart = -1;
        public int goneBaselineMargin = Integer.MIN_VALUE;
        public int goneBottomMargin = Integer.MIN_VALUE;
        public int goneEndMargin = Integer.MIN_VALUE;
        public int goneLeftMargin = Integer.MIN_VALUE;
        public int goneRightMargin = Integer.MIN_VALUE;
        public int goneStartMargin = Integer.MIN_VALUE;
        public int goneTopMargin = Integer.MIN_VALUE;
        public int guideBegin = -1;
        public int guideEnd = -1;
        public float guidePercent = -1.0f;
        public boolean guidelineUseRtl = true;
        public int heightDefault = 0;
        public int heightMax = 0;
        public int heightMin = 0;
        public float heightPercent = 1.0f;
        public float horizontalBias = 0.5f;
        public int horizontalChainStyle = 0;
        public float horizontalWeight = -1.0f;
        public int leftMargin = 0;
        public int leftToLeft = -1;
        public int leftToRight = -1;
        public boolean mApply = false;
        public boolean mBarrierAllowsGoneWidgets = true;
        public int mBarrierDirection = -1;
        public int mBarrierMargin = 0;
        public String mConstraintTag;
        public int mHeight;
        public int mHelperType = -1;
        public boolean mIsGuideline = false;
        public boolean mOverride = false;
        public String mReferenceIdString;
        public int[] mReferenceIds;
        public int mWidth;
        public int mWrapBehavior = 0;
        public int orientation = -1;
        public int rightMargin = 0;
        public int rightToLeft = -1;
        public int rightToRight = -1;
        public int startMargin = 0;
        public int startToEnd = -1;
        public int startToStart = -1;
        public int topMargin = 0;
        public int topToBottom = -1;
        public int topToTop = -1;
        public float verticalBias = 0.5f;
        public int verticalChainStyle = 0;
        public float verticalWeight = -1.0f;
        public int widthDefault = 0;
        public int widthMax = 0;
        public int widthMin = 0;
        public float widthPercent = 1.0f;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            mapToConstant = sparseIntArray;
            sparseIntArray.append(R.styleable.Layout_layout_constraintLeft_toLeftOf, 24);
            mapToConstant.append(R.styleable.Layout_layout_constraintLeft_toRightOf, 25);
            mapToConstant.append(R.styleable.Layout_layout_constraintRight_toLeftOf, 28);
            mapToConstant.append(R.styleable.Layout_layout_constraintRight_toRightOf, 29);
            mapToConstant.append(R.styleable.Layout_layout_constraintTop_toTopOf, 35);
            mapToConstant.append(R.styleable.Layout_layout_constraintTop_toBottomOf, 34);
            mapToConstant.append(R.styleable.Layout_layout_constraintBottom_toTopOf, 4);
            mapToConstant.append(R.styleable.Layout_layout_constraintBottom_toBottomOf, 3);
            mapToConstant.append(R.styleable.Layout_layout_constraintBaseline_toBaselineOf, 1);
            mapToConstant.append(R.styleable.Layout_layout_editor_absoluteX, 6);
            mapToConstant.append(R.styleable.Layout_layout_editor_absoluteY, 7);
            mapToConstant.append(R.styleable.Layout_layout_constraintGuide_begin, 17);
            mapToConstant.append(R.styleable.Layout_layout_constraintGuide_end, 18);
            mapToConstant.append(R.styleable.Layout_layout_constraintGuide_percent, 19);
            mapToConstant.append(R.styleable.Layout_guidelineUseRtl, GUIDE_USE_RTL);
            mapToConstant.append(R.styleable.Layout_android_orientation, 26);
            mapToConstant.append(R.styleable.Layout_layout_constraintStart_toEndOf, 31);
            mapToConstant.append(R.styleable.Layout_layout_constraintStart_toStartOf, 32);
            mapToConstant.append(R.styleable.Layout_layout_constraintEnd_toStartOf, 10);
            mapToConstant.append(R.styleable.Layout_layout_constraintEnd_toEndOf, 9);
            mapToConstant.append(R.styleable.Layout_layout_goneMarginLeft, 13);
            mapToConstant.append(R.styleable.Layout_layout_goneMarginTop, 16);
            mapToConstant.append(R.styleable.Layout_layout_goneMarginRight, 14);
            mapToConstant.append(R.styleable.Layout_layout_goneMarginBottom, 11);
            mapToConstant.append(R.styleable.Layout_layout_goneMarginStart, 15);
            mapToConstant.append(R.styleable.Layout_layout_goneMarginEnd, 12);
            mapToConstant.append(R.styleable.Layout_layout_constraintVertical_weight, 38);
            mapToConstant.append(R.styleable.Layout_layout_constraintHorizontal_weight, 37);
            mapToConstant.append(R.styleable.Layout_layout_constraintHorizontal_chainStyle, 39);
            mapToConstant.append(R.styleable.Layout_layout_constraintVertical_chainStyle, 40);
            mapToConstant.append(R.styleable.Layout_layout_constraintHorizontal_bias, 20);
            mapToConstant.append(R.styleable.Layout_layout_constraintVertical_bias, 36);
            mapToConstant.append(R.styleable.Layout_layout_constraintDimensionRatio, 5);
            mapToConstant.append(R.styleable.Layout_layout_constraintLeft_creator, UNUSED);
            mapToConstant.append(R.styleable.Layout_layout_constraintTop_creator, UNUSED);
            mapToConstant.append(R.styleable.Layout_layout_constraintRight_creator, UNUSED);
            mapToConstant.append(R.styleable.Layout_layout_constraintBottom_creator, UNUSED);
            mapToConstant.append(R.styleable.Layout_layout_constraintBaseline_creator, UNUSED);
            mapToConstant.append(R.styleable.Layout_android_layout_marginLeft, 23);
            mapToConstant.append(R.styleable.Layout_android_layout_marginRight, 27);
            mapToConstant.append(R.styleable.Layout_android_layout_marginStart, 30);
            mapToConstant.append(R.styleable.Layout_android_layout_marginEnd, 8);
            mapToConstant.append(R.styleable.Layout_android_layout_marginTop, 33);
            mapToConstant.append(R.styleable.Layout_android_layout_marginBottom, 2);
            mapToConstant.append(R.styleable.Layout_android_layout_width, 22);
            mapToConstant.append(R.styleable.Layout_android_layout_height, 21);
            mapToConstant.append(R.styleable.Layout_layout_constraintWidth, 41);
            mapToConstant.append(R.styleable.Layout_layout_constraintHeight, 42);
            mapToConstant.append(R.styleable.Layout_layout_constrainedWidth, 41);
            mapToConstant.append(R.styleable.Layout_layout_constrainedHeight, 42);
            mapToConstant.append(R.styleable.Layout_layout_wrapBehaviorInParent, LAYOUT_WRAP_BEHAVIOR);
            mapToConstant.append(R.styleable.Layout_layout_constraintCircle, 61);
            mapToConstant.append(R.styleable.Layout_layout_constraintCircleRadius, CIRCLE_RADIUS);
            mapToConstant.append(R.styleable.Layout_layout_constraintCircleAngle, 63);
            mapToConstant.append(R.styleable.Layout_layout_constraintWidth_percent, WIDTH_PERCENT);
            mapToConstant.append(R.styleable.Layout_layout_constraintHeight_percent, HEIGHT_PERCENT);
            mapToConstant.append(R.styleable.Layout_chainUseRtl, CHAIN_USE_RTL);
            mapToConstant.append(R.styleable.Layout_barrierDirection, BARRIER_DIRECTION);
            mapToConstant.append(R.styleable.Layout_barrierMargin, BARRIER_MARGIN);
            mapToConstant.append(R.styleable.Layout_constraint_referenced_ids, CONSTRAINT_REFERENCED_IDS);
            mapToConstant.append(R.styleable.Layout_barrierAllowsGoneWidgets, BARRIER_ALLOWS_GONE_WIDGETS);
        }

        public void copyFrom(Layout src) {
            this.mIsGuideline = src.mIsGuideline;
            this.mWidth = src.mWidth;
            this.mApply = src.mApply;
            this.mHeight = src.mHeight;
            this.guideBegin = src.guideBegin;
            this.guideEnd = src.guideEnd;
            this.guidePercent = src.guidePercent;
            this.guidelineUseRtl = src.guidelineUseRtl;
            this.leftToLeft = src.leftToLeft;
            this.leftToRight = src.leftToRight;
            this.rightToLeft = src.rightToLeft;
            this.rightToRight = src.rightToRight;
            this.topToTop = src.topToTop;
            this.topToBottom = src.topToBottom;
            this.bottomToTop = src.bottomToTop;
            this.bottomToBottom = src.bottomToBottom;
            this.baselineToBaseline = src.baselineToBaseline;
            this.baselineToTop = src.baselineToTop;
            this.baselineToBottom = src.baselineToBottom;
            this.startToEnd = src.startToEnd;
            this.startToStart = src.startToStart;
            this.endToStart = src.endToStart;
            this.endToEnd = src.endToEnd;
            this.horizontalBias = src.horizontalBias;
            this.verticalBias = src.verticalBias;
            this.dimensionRatio = src.dimensionRatio;
            this.circleConstraint = src.circleConstraint;
            this.circleRadius = src.circleRadius;
            this.circleAngle = src.circleAngle;
            this.editorAbsoluteX = src.editorAbsoluteX;
            this.editorAbsoluteY = src.editorAbsoluteY;
            this.orientation = src.orientation;
            this.leftMargin = src.leftMargin;
            this.rightMargin = src.rightMargin;
            this.topMargin = src.topMargin;
            this.bottomMargin = src.bottomMargin;
            this.endMargin = src.endMargin;
            this.startMargin = src.startMargin;
            this.baselineMargin = src.baselineMargin;
            this.goneLeftMargin = src.goneLeftMargin;
            this.goneTopMargin = src.goneTopMargin;
            this.goneRightMargin = src.goneRightMargin;
            this.goneBottomMargin = src.goneBottomMargin;
            this.goneEndMargin = src.goneEndMargin;
            this.goneStartMargin = src.goneStartMargin;
            this.goneBaselineMargin = src.goneBaselineMargin;
            this.verticalWeight = src.verticalWeight;
            this.horizontalWeight = src.horizontalWeight;
            this.horizontalChainStyle = src.horizontalChainStyle;
            this.verticalChainStyle = src.verticalChainStyle;
            this.widthDefault = src.widthDefault;
            this.heightDefault = src.heightDefault;
            this.widthMax = src.widthMax;
            this.heightMax = src.heightMax;
            this.widthMin = src.widthMin;
            this.heightMin = src.heightMin;
            this.widthPercent = src.widthPercent;
            this.heightPercent = src.heightPercent;
            this.mBarrierDirection = src.mBarrierDirection;
            this.mBarrierMargin = src.mBarrierMargin;
            this.mHelperType = src.mHelperType;
            this.mConstraintTag = src.mConstraintTag;
            int[] iArr = src.mReferenceIds;
            if (iArr == null || src.mReferenceIdString != null) {
                this.mReferenceIds = null;
            } else {
                this.mReferenceIds = Arrays.copyOf(iArr, iArr.length);
            }
            this.mReferenceIdString = src.mReferenceIdString;
            this.constrainedWidth = src.constrainedWidth;
            this.constrainedHeight = src.constrainedHeight;
            this.mBarrierAllowsGoneWidgets = src.mBarrierAllowsGoneWidgets;
            this.mWrapBehavior = src.mWrapBehavior;
        }

        public void dump(MotionScene scene, StringBuilder stringBuilder) {
            Field[] declaredFields = getClass().getDeclaredFields();
            stringBuilder.append("\n");
            for (Field field : declaredFields) {
                String name = field.getName();
                if (!Modifier.isStatic(field.getModifiers())) {
                    try {
                        Object obj = field.get(this);
                        Class<?> type = field.getType();
                        if (type == Integer.TYPE) {
                            Integer num = (Integer) obj;
                            if (num.intValue() != -1) {
                                Object lookUpConstraintName = scene.lookUpConstraintName(num.intValue());
                                stringBuilder.append("    ");
                                stringBuilder.append(name);
                                stringBuilder.append(" = \"");
                                stringBuilder.append(lookUpConstraintName == null ? num : lookUpConstraintName);
                                stringBuilder.append("\"\n");
                            }
                        } else if (type == Float.TYPE) {
                            Float f = (Float) obj;
                            if (f.floatValue() != -1.0f) {
                                stringBuilder.append("    ");
                                stringBuilder.append(name);
                                stringBuilder.append(" = \"");
                                stringBuilder.append(f);
                                stringBuilder.append("\"\n");
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void fillFromAttributeList(Context context, AttributeSet attrs) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.Layout);
            this.mApply = true;
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                switch (mapToConstant.get(index)) {
                    case 1:
                        this.baselineToBaseline = ConstraintSet.lookupID(obtainStyledAttributes, index, this.baselineToBaseline);
                        break;
                    case 2:
                        this.bottomMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.bottomMargin);
                        break;
                    case 3:
                        this.bottomToBottom = ConstraintSet.lookupID(obtainStyledAttributes, index, this.bottomToBottom);
                        break;
                    case 4:
                        this.bottomToTop = ConstraintSet.lookupID(obtainStyledAttributes, index, this.bottomToTop);
                        break;
                    case 5:
                        this.dimensionRatio = obtainStyledAttributes.getString(index);
                        break;
                    case 6:
                        this.editorAbsoluteX = obtainStyledAttributes.getDimensionPixelOffset(index, this.editorAbsoluteX);
                        break;
                    case 7:
                        this.editorAbsoluteY = obtainStyledAttributes.getDimensionPixelOffset(index, this.editorAbsoluteY);
                        break;
                    case 8:
                        if (Build.VERSION.SDK_INT < 17) {
                            break;
                        } else {
                            this.endMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.endMargin);
                            break;
                        }
                    case 9:
                        this.endToEnd = ConstraintSet.lookupID(obtainStyledAttributes, index, this.endToEnd);
                        break;
                    case 10:
                        this.endToStart = ConstraintSet.lookupID(obtainStyledAttributes, index, this.endToStart);
                        break;
                    case 11:
                        this.goneBottomMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneBottomMargin);
                        break;
                    case 12:
                        this.goneEndMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneEndMargin);
                        break;
                    case 13:
                        this.goneLeftMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneLeftMargin);
                        break;
                    case 14:
                        this.goneRightMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneRightMargin);
                        break;
                    case 15:
                        this.goneStartMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneStartMargin);
                        break;
                    case 16:
                        this.goneTopMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneTopMargin);
                        break;
                    case 17:
                        this.guideBegin = obtainStyledAttributes.getDimensionPixelOffset(index, this.guideBegin);
                        break;
                    case 18:
                        this.guideEnd = obtainStyledAttributes.getDimensionPixelOffset(index, this.guideEnd);
                        break;
                    case 19:
                        this.guidePercent = obtainStyledAttributes.getFloat(index, this.guidePercent);
                        break;
                    case 20:
                        this.horizontalBias = obtainStyledAttributes.getFloat(index, this.horizontalBias);
                        break;
                    case 21:
                        this.mHeight = obtainStyledAttributes.getLayoutDimension(index, this.mHeight);
                        break;
                    case 22:
                        this.mWidth = obtainStyledAttributes.getLayoutDimension(index, this.mWidth);
                        break;
                    case 23:
                        this.leftMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.leftMargin);
                        break;
                    case 24:
                        this.leftToLeft = ConstraintSet.lookupID(obtainStyledAttributes, index, this.leftToLeft);
                        break;
                    case 25:
                        this.leftToRight = ConstraintSet.lookupID(obtainStyledAttributes, index, this.leftToRight);
                        break;
                    case 26:
                        this.orientation = obtainStyledAttributes.getInt(index, this.orientation);
                        break;
                    case 27:
                        this.rightMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.rightMargin);
                        break;
                    case 28:
                        this.rightToLeft = ConstraintSet.lookupID(obtainStyledAttributes, index, this.rightToLeft);
                        break;
                    case 29:
                        this.rightToRight = ConstraintSet.lookupID(obtainStyledAttributes, index, this.rightToRight);
                        break;
                    case 30:
                        if (Build.VERSION.SDK_INT < 17) {
                            break;
                        } else {
                            this.startMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.startMargin);
                            break;
                        }
                    case 31:
                        this.startToEnd = ConstraintSet.lookupID(obtainStyledAttributes, index, this.startToEnd);
                        break;
                    case 32:
                        this.startToStart = ConstraintSet.lookupID(obtainStyledAttributes, index, this.startToStart);
                        break;
                    case 33:
                        this.topMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.topMargin);
                        break;
                    case 34:
                        this.topToBottom = ConstraintSet.lookupID(obtainStyledAttributes, index, this.topToBottom);
                        break;
                    case 35:
                        this.topToTop = ConstraintSet.lookupID(obtainStyledAttributes, index, this.topToTop);
                        break;
                    case 36:
                        this.verticalBias = obtainStyledAttributes.getFloat(index, this.verticalBias);
                        break;
                    case 37:
                        this.horizontalWeight = obtainStyledAttributes.getFloat(index, this.horizontalWeight);
                        break;
                    case 38:
                        this.verticalWeight = obtainStyledAttributes.getFloat(index, this.verticalWeight);
                        break;
                    case 39:
                        this.horizontalChainStyle = obtainStyledAttributes.getInt(index, this.horizontalChainStyle);
                        break;
                    case 40:
                        this.verticalChainStyle = obtainStyledAttributes.getInt(index, this.verticalChainStyle);
                        break;
                    case 41:
                        ConstraintSet.parseDimensionConstraints(this, obtainStyledAttributes, index, 0);
                        break;
                    case 42:
                        ConstraintSet.parseDimensionConstraints(this, obtainStyledAttributes, index, 1);
                        break;
                    case 61:
                        this.circleConstraint = ConstraintSet.lookupID(obtainStyledAttributes, index, this.circleConstraint);
                        break;
                    case CIRCLE_RADIUS /*62*/:
                        this.circleRadius = obtainStyledAttributes.getDimensionPixelSize(index, this.circleRadius);
                        break;
                    case 63:
                        this.circleAngle = obtainStyledAttributes.getFloat(index, this.circleAngle);
                        break;
                    case WIDTH_PERCENT /*69*/:
                        this.widthPercent = obtainStyledAttributes.getFloat(index, 1.0f);
                        break;
                    case HEIGHT_PERCENT /*70*/:
                        this.heightPercent = obtainStyledAttributes.getFloat(index, 1.0f);
                        break;
                    case CHAIN_USE_RTL /*71*/:
                        Log.e(ConstraintSet.TAG, "CURRENTLY UNSUPPORTED");
                        break;
                    case BARRIER_DIRECTION /*72*/:
                        this.mBarrierDirection = obtainStyledAttributes.getInt(index, this.mBarrierDirection);
                        break;
                    case BARRIER_MARGIN /*73*/:
                        this.mBarrierMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.mBarrierMargin);
                        break;
                    case CONSTRAINT_REFERENCED_IDS /*74*/:
                        this.mReferenceIdString = obtainStyledAttributes.getString(index);
                        break;
                    case BARRIER_ALLOWS_GONE_WIDGETS /*75*/:
                        this.mBarrierAllowsGoneWidgets = obtainStyledAttributes.getBoolean(index, this.mBarrierAllowsGoneWidgets);
                        break;
                    case LAYOUT_WRAP_BEHAVIOR /*76*/:
                        this.mWrapBehavior = obtainStyledAttributes.getInt(index, this.mWrapBehavior);
                        break;
                    case BASELINE_TO_TOP /*77*/:
                        this.baselineToTop = ConstraintSet.lookupID(obtainStyledAttributes, index, this.baselineToTop);
                        break;
                    case BASELINE_TO_BOTTOM /*78*/:
                        this.baselineToBottom = ConstraintSet.lookupID(obtainStyledAttributes, index, this.baselineToBottom);
                        break;
                    case GONE_BASELINE_MARGIN /*79*/:
                        this.goneBaselineMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneBaselineMargin);
                        break;
                    case BASELINE_MARGIN /*80*/:
                        this.baselineMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.baselineMargin);
                        break;
                    case WIDTH_DEFAULT /*81*/:
                        this.widthDefault = obtainStyledAttributes.getInt(index, this.widthDefault);
                        break;
                    case HEIGHT_DEFAULT /*82*/:
                        this.heightDefault = obtainStyledAttributes.getInt(index, this.heightDefault);
                        break;
                    case HEIGHT_MAX /*83*/:
                        this.heightMax = obtainStyledAttributes.getDimensionPixelSize(index, this.heightMax);
                        break;
                    case WIDTH_MAX /*84*/:
                        this.widthMax = obtainStyledAttributes.getDimensionPixelSize(index, this.widthMax);
                        break;
                    case HEIGHT_MIN /*85*/:
                        this.heightMin = obtainStyledAttributes.getDimensionPixelSize(index, this.heightMin);
                        break;
                    case WIDTH_MIN /*86*/:
                        this.widthMin = obtainStyledAttributes.getDimensionPixelSize(index, this.widthMin);
                        break;
                    case CONSTRAINED_WIDTH /*87*/:
                        this.constrainedWidth = obtainStyledAttributes.getBoolean(index, this.constrainedWidth);
                        break;
                    case CONSTRAINED_HEIGHT /*88*/:
                        this.constrainedHeight = obtainStyledAttributes.getBoolean(index, this.constrainedHeight);
                        break;
                    case CONSTRAINT_TAG /*89*/:
                        this.mConstraintTag = obtainStyledAttributes.getString(index);
                        break;
                    case GUIDE_USE_RTL /*90*/:
                        this.guidelineUseRtl = obtainStyledAttributes.getBoolean(index, this.guidelineUseRtl);
                        break;
                    case UNUSED /*91*/:
                        StringBuilder append = new StringBuilder().append("unused attribute 0x");
                        String hexString = Integer.toHexString(index);
                        Log1F380D.a((Object) hexString);
                        Log.w(ConstraintSet.TAG, append.append(hexString).append("   ").append(mapToConstant.get(index)).toString());
                        break;
                    default:
                        StringBuilder append2 = new StringBuilder().append("Unknown attribute 0x");
                        String hexString2 = Integer.toHexString(index);
                        Log1F380D.a((Object) hexString2);
                        Log.w(ConstraintSet.TAG, append2.append(hexString2).append("   ").append(mapToConstant.get(index)).toString());
                        break;
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    public static class Motion {
        private static final int ANIMATE_CIRCLE_ANGLE_TO = 6;
        private static final int ANIMATE_RELATIVE_TO = 5;
        private static final int INTERPOLATOR_REFERENCE_ID = -2;
        private static final int INTERPOLATOR_UNDEFINED = -3;
        private static final int MOTION_DRAW_PATH = 4;
        private static final int MOTION_STAGGER = 7;
        private static final int PATH_MOTION_ARC = 2;
        private static final int QUANTIZE_MOTION_INTERPOLATOR = 10;
        private static final int QUANTIZE_MOTION_PHASE = 9;
        private static final int QUANTIZE_MOTION_STEPS = 8;
        private static final int SPLINE_STRING = -1;
        private static final int TRANSITION_EASING = 3;
        private static final int TRANSITION_PATH_ROTATE = 1;
        private static SparseIntArray mapToConstant;
        public int mAnimateCircleAngleTo = 0;
        public int mAnimateRelativeTo = -1;
        public boolean mApply = false;
        public int mDrawPath = 0;
        public float mMotionStagger = Float.NaN;
        public int mPathMotionArc = -1;
        public float mPathRotate = Float.NaN;
        public int mPolarRelativeTo = -1;
        public int mQuantizeInterpolatorID = -1;
        public String mQuantizeInterpolatorString = null;
        public int mQuantizeInterpolatorType = -3;
        public float mQuantizeMotionPhase = Float.NaN;
        public int mQuantizeMotionSteps = -1;
        public String mTransitionEasing = null;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            mapToConstant = sparseIntArray;
            sparseIntArray.append(R.styleable.Motion_motionPathRotate, 1);
            mapToConstant.append(R.styleable.Motion_pathMotionArc, 2);
            mapToConstant.append(R.styleable.Motion_transitionEasing, 3);
            mapToConstant.append(R.styleable.Motion_drawPath, 4);
            mapToConstant.append(R.styleable.Motion_animateRelativeTo, 5);
            mapToConstant.append(R.styleable.Motion_animateCircleAngleTo, 6);
            mapToConstant.append(R.styleable.Motion_motionStagger, 7);
            mapToConstant.append(R.styleable.Motion_quantizeMotionSteps, 8);
            mapToConstant.append(R.styleable.Motion_quantizeMotionPhase, 9);
            mapToConstant.append(R.styleable.Motion_quantizeMotionInterpolator, 10);
        }

        public void copyFrom(Motion src) {
            this.mApply = src.mApply;
            this.mAnimateRelativeTo = src.mAnimateRelativeTo;
            this.mTransitionEasing = src.mTransitionEasing;
            this.mPathMotionArc = src.mPathMotionArc;
            this.mDrawPath = src.mDrawPath;
            this.mPathRotate = src.mPathRotate;
            this.mMotionStagger = src.mMotionStagger;
            this.mPolarRelativeTo = src.mPolarRelativeTo;
        }

        /* access modifiers changed from: package-private */
        public void fillFromAttributeList(Context context, AttributeSet attrs) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.Motion);
            this.mApply = true;
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                switch (mapToConstant.get(index)) {
                    case 1:
                        this.mPathRotate = obtainStyledAttributes.getFloat(index, this.mPathRotate);
                        break;
                    case 2:
                        this.mPathMotionArc = obtainStyledAttributes.getInt(index, this.mPathMotionArc);
                        break;
                    case 3:
                        if (obtainStyledAttributes.peekValue(index).type != 3) {
                            this.mTransitionEasing = Easing.NAMED_EASING[obtainStyledAttributes.getInteger(index, 0)];
                            break;
                        } else {
                            this.mTransitionEasing = obtainStyledAttributes.getString(index);
                            break;
                        }
                    case 4:
                        this.mDrawPath = obtainStyledAttributes.getInt(index, 0);
                        break;
                    case 5:
                        this.mAnimateRelativeTo = ConstraintSet.lookupID(obtainStyledAttributes, index, this.mAnimateRelativeTo);
                        break;
                    case 6:
                        this.mAnimateCircleAngleTo = obtainStyledAttributes.getInteger(index, this.mAnimateCircleAngleTo);
                        break;
                    case 7:
                        this.mMotionStagger = obtainStyledAttributes.getFloat(index, this.mMotionStagger);
                        break;
                    case 8:
                        this.mQuantizeMotionSteps = obtainStyledAttributes.getInteger(index, this.mQuantizeMotionSteps);
                        break;
                    case 9:
                        this.mQuantizeMotionPhase = obtainStyledAttributes.getFloat(index, this.mQuantizeMotionPhase);
                        break;
                    case 10:
                        TypedValue peekValue = obtainStyledAttributes.peekValue(index);
                        if (peekValue.type != 1) {
                            if (peekValue.type != 3) {
                                this.mQuantizeInterpolatorType = obtainStyledAttributes.getInteger(index, this.mQuantizeInterpolatorID);
                                break;
                            } else {
                                String string = obtainStyledAttributes.getString(index);
                                this.mQuantizeInterpolatorString = string;
                                if (string.indexOf("/") <= 0) {
                                    this.mQuantizeInterpolatorType = -1;
                                    break;
                                } else {
                                    this.mQuantizeInterpolatorID = obtainStyledAttributes.getResourceId(index, -1);
                                    this.mQuantizeInterpolatorType = -2;
                                    break;
                                }
                            }
                        } else {
                            int resourceId = obtainStyledAttributes.getResourceId(index, -1);
                            this.mQuantizeInterpolatorID = resourceId;
                            if (resourceId == -1) {
                                break;
                            } else {
                                this.mQuantizeInterpolatorType = -2;
                                break;
                            }
                        }
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    public static class PropertySet {
        public float alpha = 1.0f;
        public boolean mApply = false;
        public float mProgress = Float.NaN;
        public int mVisibilityMode = 0;
        public int visibility = 0;

        public void copyFrom(PropertySet src) {
            this.mApply = src.mApply;
            this.visibility = src.visibility;
            this.alpha = src.alpha;
            this.mProgress = src.mProgress;
            this.mVisibilityMode = src.mVisibilityMode;
        }

        /* access modifiers changed from: package-private */
        public void fillFromAttributeList(Context context, AttributeSet attrs) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.PropertySet);
            this.mApply = true;
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.PropertySet_android_alpha) {
                    this.alpha = obtainStyledAttributes.getFloat(index, this.alpha);
                } else if (index == R.styleable.PropertySet_android_visibility) {
                    this.visibility = obtainStyledAttributes.getInt(index, this.visibility);
                    this.visibility = ConstraintSet.VISIBILITY_FLAGS[this.visibility];
                } else if (index == R.styleable.PropertySet_visibilityMode) {
                    this.mVisibilityMode = obtainStyledAttributes.getInt(index, this.mVisibilityMode);
                } else if (index == R.styleable.PropertySet_motionProgress) {
                    this.mProgress = obtainStyledAttributes.getFloat(index, this.mProgress);
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    public static class Transform {
        private static final int ELEVATION = 11;
        private static final int ROTATION = 1;
        private static final int ROTATION_X = 2;
        private static final int ROTATION_Y = 3;
        private static final int SCALE_X = 4;
        private static final int SCALE_Y = 5;
        private static final int TRANSFORM_PIVOT_TARGET = 12;
        private static final int TRANSFORM_PIVOT_X = 6;
        private static final int TRANSFORM_PIVOT_Y = 7;
        private static final int TRANSLATION_X = 8;
        private static final int TRANSLATION_Y = 9;
        private static final int TRANSLATION_Z = 10;
        private static SparseIntArray mapToConstant;
        public boolean applyElevation = false;
        public float elevation = 0.0f;
        public boolean mApply = false;
        public float rotation = 0.0f;
        public float rotationX = 0.0f;
        public float rotationY = 0.0f;
        public float scaleX = 1.0f;
        public float scaleY = 1.0f;
        public int transformPivotTarget = -1;
        public float transformPivotX = Float.NaN;
        public float transformPivotY = Float.NaN;
        public float translationX = 0.0f;
        public float translationY = 0.0f;
        public float translationZ = 0.0f;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            mapToConstant = sparseIntArray;
            sparseIntArray.append(R.styleable.Transform_android_rotation, 1);
            mapToConstant.append(R.styleable.Transform_android_rotationX, 2);
            mapToConstant.append(R.styleable.Transform_android_rotationY, 3);
            mapToConstant.append(R.styleable.Transform_android_scaleX, 4);
            mapToConstant.append(R.styleable.Transform_android_scaleY, 5);
            mapToConstant.append(R.styleable.Transform_android_transformPivotX, 6);
            mapToConstant.append(R.styleable.Transform_android_transformPivotY, 7);
            mapToConstant.append(R.styleable.Transform_android_translationX, 8);
            mapToConstant.append(R.styleable.Transform_android_translationY, 9);
            mapToConstant.append(R.styleable.Transform_android_translationZ, 10);
            mapToConstant.append(R.styleable.Transform_android_elevation, 11);
            mapToConstant.append(R.styleable.Transform_transformPivotTarget, 12);
        }

        public void copyFrom(Transform src) {
            this.mApply = src.mApply;
            this.rotation = src.rotation;
            this.rotationX = src.rotationX;
            this.rotationY = src.rotationY;
            this.scaleX = src.scaleX;
            this.scaleY = src.scaleY;
            this.transformPivotX = src.transformPivotX;
            this.transformPivotY = src.transformPivotY;
            this.transformPivotTarget = src.transformPivotTarget;
            this.translationX = src.translationX;
            this.translationY = src.translationY;
            this.translationZ = src.translationZ;
            this.applyElevation = src.applyElevation;
            this.elevation = src.elevation;
        }

        /* access modifiers changed from: package-private */
        public void fillFromAttributeList(Context context, AttributeSet attrs) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.Transform);
            this.mApply = true;
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                switch (mapToConstant.get(index)) {
                    case 1:
                        this.rotation = obtainStyledAttributes.getFloat(index, this.rotation);
                        break;
                    case 2:
                        this.rotationX = obtainStyledAttributes.getFloat(index, this.rotationX);
                        break;
                    case 3:
                        this.rotationY = obtainStyledAttributes.getFloat(index, this.rotationY);
                        break;
                    case 4:
                        this.scaleX = obtainStyledAttributes.getFloat(index, this.scaleX);
                        break;
                    case 5:
                        this.scaleY = obtainStyledAttributes.getFloat(index, this.scaleY);
                        break;
                    case 6:
                        this.transformPivotX = obtainStyledAttributes.getDimension(index, this.transformPivotX);
                        break;
                    case 7:
                        this.transformPivotY = obtainStyledAttributes.getDimension(index, this.transformPivotY);
                        break;
                    case 8:
                        this.translationX = obtainStyledAttributes.getDimension(index, this.translationX);
                        break;
                    case 9:
                        this.translationY = obtainStyledAttributes.getDimension(index, this.translationY);
                        break;
                    case 10:
                        if (Build.VERSION.SDK_INT < 21) {
                            break;
                        } else {
                            this.translationZ = obtainStyledAttributes.getDimension(index, this.translationZ);
                            break;
                        }
                    case 11:
                        if (Build.VERSION.SDK_INT < 21) {
                            break;
                        } else {
                            this.applyElevation = true;
                            this.elevation = obtainStyledAttributes.getDimension(index, this.elevation);
                            break;
                        }
                    case 12:
                        this.transformPivotTarget = ConstraintSet.lookupID(obtainStyledAttributes, index, this.transformPivotTarget);
                        break;
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    class WriteJsonEngine {
        private static final String SPACE = "       ";
        final String BASELINE = "'baseline'";
        final String BOTTOM = "'bottom'";
        final String END = "'end'";
        final String LEFT = "'left'";
        final String RIGHT = "'right'";
        final String START = "'start'";
        final String TOP = "'top'";
        Context context;
        int flags;
        HashMap<Integer, String> idMap = new HashMap<>();
        ConstraintLayout layout;
        int unknownCount = 0;
        Writer writer;

        WriteJsonEngine(Writer writer2, ConstraintLayout layout2, int flags2) throws IOException {
            this.writer = writer2;
            this.layout = layout2;
            this.context = layout2.getContext();
            this.flags = flags2;
        }

        private void writeDimension(String dimString, int dim, int dimDefault, float dimPercent, int dimMin, int dimMax, boolean constrainedDim) throws IOException {
            if (dim == 0) {
                if (dimMax == -1 && dimMin == -1) {
                    switch (dimDefault) {
                        case 1:
                            this.writer.write(SPACE + dimString + ": '???????????',\n");
                            return;
                        case 2:
                            this.writer.write(SPACE + dimString + ": '" + dimPercent + "%',\n");
                            return;
                        default:
                            return;
                    }
                } else {
                    switch (dimDefault) {
                        case 0:
                            this.writer.write(SPACE + dimString + ": {'spread' ," + dimMin + ", " + dimMax + "}\n");
                            return;
                        case 1:
                            this.writer.write(SPACE + dimString + ": {'wrap' ," + dimMin + ", " + dimMax + "}\n");
                            return;
                        case 2:
                            this.writer.write(SPACE + dimString + ": {'" + dimPercent + "'% ," + dimMin + ", " + dimMax + "}\n");
                            return;
                        default:
                            return;
                    }
                }
            } else if (dim == -2) {
                this.writer.write(SPACE + dimString + ": 'wrap'\n");
            } else if (dim == -1) {
                this.writer.write(SPACE + dimString + ": 'parent'\n");
            } else {
                this.writer.write(SPACE + dimString + ": " + dim + ",\n");
            }
        }

        private void writeGuideline(int orientation, int guideBegin, int guideEnd, float guidePercent) {
        }

        /* access modifiers changed from: package-private */
        public String getName(int id) {
            if (this.idMap.containsKey(Integer.valueOf(id))) {
                return "'" + this.idMap.get(Integer.valueOf(id)) + "'";
            }
            if (id == 0) {
                return "'parent'";
            }
            String lookup = lookup(id);
            this.idMap.put(Integer.valueOf(id), lookup);
            return "'" + lookup + "'";
        }

        /* access modifiers changed from: package-private */
        public String lookup(int id) {
            if (id != -1) {
                try {
                    return this.context.getResources().getResourceEntryName(id);
                } catch (Exception e) {
                    StringBuilder append = new StringBuilder().append(EnvironmentCompat.MEDIA_UNKNOWN);
                    int i = this.unknownCount + 1;
                    this.unknownCount = i;
                    return append.append(i).toString();
                }
            } else {
                StringBuilder append2 = new StringBuilder().append(EnvironmentCompat.MEDIA_UNKNOWN);
                int i2 = this.unknownCount + 1;
                this.unknownCount = i2;
                return append2.append(i2).toString();
            }
        }

        /* access modifiers changed from: package-private */
        public void writeCircle(int circleConstraint, float circleAngle, int circleRadius) throws IOException {
            if (circleConstraint != -1) {
                this.writer.write("       circle");
                this.writer.write(":[");
                this.writer.write(getName(circleConstraint));
                this.writer.write(", " + circleAngle);
                this.writer.write(circleRadius + "]");
            }
        }

        /* access modifiers changed from: package-private */
        public void writeConstraint(String my, int leftToLeft, String other, int margin, int goneMargin) throws IOException {
            if (leftToLeft != -1) {
                this.writer.write(SPACE + my);
                this.writer.write(":[");
                this.writer.write(getName(leftToLeft));
                this.writer.write(" , ");
                this.writer.write(other);
                if (margin != 0) {
                    this.writer.write(" , " + margin);
                }
                this.writer.write("],\n");
            }
        }

        /* access modifiers changed from: package-private */
        public void writeLayout() throws IOException {
            this.writer.write("\n'ConstraintSet':{\n");
            for (Integer num : ConstraintSet.this.mConstraints.keySet()) {
                this.writer.write(getName(num.intValue()) + ":{\n");
                Layout layout2 = ((Constraint) ConstraintSet.this.mConstraints.get(num)).layout;
                writeDimension("height", layout2.mHeight, layout2.heightDefault, layout2.heightPercent, layout2.heightMin, layout2.heightMax, layout2.constrainedHeight);
                writeDimension("width", layout2.mWidth, layout2.widthDefault, layout2.widthPercent, layout2.widthMin, layout2.widthMax, layout2.constrainedWidth);
                writeConstraint("'left'", layout2.leftToLeft, "'left'", layout2.leftMargin, layout2.goneLeftMargin);
                writeConstraint("'left'", layout2.leftToRight, "'right'", layout2.leftMargin, layout2.goneLeftMargin);
                writeConstraint("'right'", layout2.rightToLeft, "'left'", layout2.rightMargin, layout2.goneRightMargin);
                writeConstraint("'right'", layout2.rightToRight, "'right'", layout2.rightMargin, layout2.goneRightMargin);
                writeConstraint("'baseline'", layout2.baselineToBaseline, "'baseline'", -1, layout2.goneBaselineMargin);
                writeConstraint("'baseline'", layout2.baselineToTop, "'top'", -1, layout2.goneBaselineMargin);
                writeConstraint("'baseline'", layout2.baselineToBottom, "'bottom'", -1, layout2.goneBaselineMargin);
                writeConstraint("'top'", layout2.topToBottom, "'bottom'", layout2.topMargin, layout2.goneTopMargin);
                writeConstraint("'top'", layout2.topToTop, "'top'", layout2.topMargin, layout2.goneTopMargin);
                writeConstraint("'bottom'", layout2.bottomToBottom, "'bottom'", layout2.bottomMargin, layout2.goneBottomMargin);
                writeConstraint("'bottom'", layout2.bottomToTop, "'top'", layout2.bottomMargin, layout2.goneBottomMargin);
                writeConstraint("'start'", layout2.startToStart, "'start'", layout2.startMargin, layout2.goneStartMargin);
                writeConstraint("'start'", layout2.startToEnd, "'end'", layout2.startMargin, layout2.goneStartMargin);
                writeConstraint("'end'", layout2.endToStart, "'start'", layout2.endMargin, layout2.goneEndMargin);
                writeConstraint("'end'", layout2.endToEnd, "'end'", layout2.endMargin, layout2.goneEndMargin);
                writeVariable("'horizontalBias'", layout2.horizontalBias, 0.5f);
                writeVariable("'verticalBias'", layout2.verticalBias, 0.5f);
                writeCircle(layout2.circleConstraint, layout2.circleAngle, layout2.circleRadius);
                writeGuideline(layout2.orientation, layout2.guideBegin, layout2.guideEnd, layout2.guidePercent);
                writeVariable("'dimensionRatio'", layout2.dimensionRatio);
                writeVariable("'barrierMargin'", layout2.mBarrierMargin);
                writeVariable("'type'", layout2.mHelperType);
                writeVariable("'ReferenceId'", layout2.mReferenceIdString);
                writeVariable("'mBarrierAllowsGoneWidgets'", layout2.mBarrierAllowsGoneWidgets, true);
                writeVariable("'WrapBehavior'", layout2.mWrapBehavior);
                writeVariable("'verticalWeight'", layout2.verticalWeight);
                writeVariable("'horizontalWeight'", layout2.horizontalWeight);
                writeVariable("'horizontalChainStyle'", layout2.horizontalChainStyle);
                writeVariable("'verticalChainStyle'", layout2.verticalChainStyle);
                writeVariable("'barrierDirection'", layout2.mBarrierDirection);
                if (layout2.mReferenceIds != null) {
                    writeVariable("'ReferenceIds'", layout2.mReferenceIds);
                }
                this.writer.write("}\n");
            }
            this.writer.write("}\n");
        }

        /* access modifiers changed from: package-private */
        public void writeVariable(String name, float value) throws IOException {
            if (value != -1.0f) {
                this.writer.write(SPACE + name);
                this.writer.write(": " + value);
                this.writer.write(",\n");
            }
        }

        /* access modifiers changed from: package-private */
        public void writeVariable(String name, float value, float def) throws IOException {
            if (value != def) {
                this.writer.write(SPACE + name);
                this.writer.write(": " + value);
                this.writer.write(",\n");
            }
        }

        /* access modifiers changed from: package-private */
        public void writeVariable(String name, int value) throws IOException {
            if (value != 0 && value != -1) {
                this.writer.write(SPACE + name);
                this.writer.write(":");
                this.writer.write(", " + value);
                this.writer.write("\n");
            }
        }

        /* access modifiers changed from: package-private */
        public void writeVariable(String name, String value) throws IOException {
            if (value != null) {
                this.writer.write(SPACE + name);
                this.writer.write(":");
                this.writer.write(", " + value);
                this.writer.write("\n");
            }
        }

        /* access modifiers changed from: package-private */
        public void writeVariable(String name, boolean value) throws IOException {
            if (value) {
                this.writer.write(SPACE + name);
                this.writer.write(": " + value);
                this.writer.write(",\n");
            }
        }

        /* access modifiers changed from: package-private */
        public void writeVariable(String name, boolean value, boolean def) throws IOException {
            if (value != def) {
                this.writer.write(SPACE + name);
                this.writer.write(": " + value);
                this.writer.write(",\n");
            }
        }

        /* access modifiers changed from: package-private */
        public void writeVariable(String name, int[] value) throws IOException {
            if (value != null) {
                this.writer.write(SPACE + name);
                this.writer.write(": ");
                int i = 0;
                while (i < value.length) {
                    this.writer.write((i == 0 ? "[" : ", ") + getName(value[i]));
                    i++;
                }
                this.writer.write("],\n");
            }
        }
    }

    class WriteXmlEngine {
        private static final String SPACE = "\n       ";
        final String BASELINE = "'baseline'";
        final String BOTTOM = "'bottom'";
        final String END = "'end'";
        final String LEFT = "'left'";
        final String RIGHT = "'right'";
        final String START = "'start'";
        final String TOP = "'top'";
        Context context;
        int flags;
        HashMap<Integer, String> idMap = new HashMap<>();
        ConstraintLayout layout;
        int unknownCount = 0;
        Writer writer;

        WriteXmlEngine(Writer writer2, ConstraintLayout layout2, int flags2) throws IOException {
            this.writer = writer2;
            this.layout = layout2;
            this.context = layout2.getContext();
            this.flags = flags2;
        }

        private void writeBaseDimension(String dimString, int dim, int def) throws IOException {
            if (dim == def) {
                return;
            }
            if (dim == -2) {
                this.writer.write(SPACE + dimString + "=\"wrap_content\"");
            } else if (dim == -1) {
                this.writer.write(SPACE + dimString + "=\"match_parent\"");
            } else {
                this.writer.write(SPACE + dimString + "=\"" + dim + "dp\"");
            }
        }

        private void writeBoolen(String dimString, boolean val, boolean def) throws IOException {
            if (val != def) {
                this.writer.write(SPACE + dimString + "=\"" + val + "dp\"");
            }
        }

        private void writeDimension(String dimString, int dim, int def) throws IOException {
            if (dim != def) {
                this.writer.write(SPACE + dimString + "=\"" + dim + "dp\"");
            }
        }

        private void writeEnum(String dimString, int val, String[] types, int def) throws IOException {
            if (val != def) {
                this.writer.write(SPACE + dimString + "=\"" + types[val] + "\"");
            }
        }

        /* access modifiers changed from: package-private */
        public String getName(int id) {
            if (this.idMap.containsKey(Integer.valueOf(id))) {
                return "@+id/" + this.idMap.get(Integer.valueOf(id)) + HttpUrl.FRAGMENT_ENCODE_SET;
            }
            if (id == 0) {
                return ConstraintSet.KEY_PERCENT_PARENT;
            }
            String lookup = lookup(id);
            this.idMap.put(Integer.valueOf(id), lookup);
            return "@+id/" + lookup + HttpUrl.FRAGMENT_ENCODE_SET;
        }

        /* access modifiers changed from: package-private */
        public String lookup(int id) {
            if (id != -1) {
                try {
                    return this.context.getResources().getResourceEntryName(id);
                } catch (Exception e) {
                    StringBuilder append = new StringBuilder().append(EnvironmentCompat.MEDIA_UNKNOWN);
                    int i = this.unknownCount + 1;
                    this.unknownCount = i;
                    return append.append(i).toString();
                }
            } else {
                StringBuilder append2 = new StringBuilder().append(EnvironmentCompat.MEDIA_UNKNOWN);
                int i2 = this.unknownCount + 1;
                this.unknownCount = i2;
                return append2.append(i2).toString();
            }
        }

        /* access modifiers changed from: package-private */
        public void writeCircle(int circleConstraint, float circleAngle, int circleRadius) throws IOException {
            if (circleConstraint != -1) {
                this.writer.write("circle");
                this.writer.write(":[");
                this.writer.write(getName(circleConstraint));
                this.writer.write(", " + circleAngle);
                this.writer.write(circleRadius + "]");
            }
        }

        /* access modifiers changed from: package-private */
        public void writeConstraint(String my, int leftToLeft, String other, int margin, int goneMargin) throws IOException {
            if (leftToLeft != -1) {
                this.writer.write(SPACE + my);
                this.writer.write(":[");
                this.writer.write(getName(leftToLeft));
                this.writer.write(" , ");
                this.writer.write(other);
                if (margin != 0) {
                    this.writer.write(" , " + margin);
                }
                this.writer.write("],\n");
            }
        }

        /* access modifiers changed from: package-private */
        public void writeLayout() throws IOException {
            this.writer.write("\n<ConstraintSet>\n");
            for (Integer num : ConstraintSet.this.mConstraints.keySet()) {
                String name = getName(num.intValue());
                this.writer.write("  <Constraint");
                this.writer.write("\n       android:id=\"" + name + "\"");
                Layout layout2 = ((Constraint) ConstraintSet.this.mConstraints.get(num)).layout;
                writeBaseDimension("android:layout_width", layout2.mWidth, -5);
                writeBaseDimension("android:layout_height", layout2.mHeight, -5);
                writeVariable("app:layout_constraintGuide_begin", (float) layout2.guideBegin, -1.0f);
                writeVariable("app:layout_constraintGuide_end", (float) layout2.guideEnd, -1.0f);
                writeVariable("app:layout_constraintGuide_percent", layout2.guidePercent, -1.0f);
                writeVariable("app:layout_constraintHorizontal_bias", layout2.horizontalBias, 0.5f);
                writeVariable("app:layout_constraintVertical_bias", layout2.verticalBias, 0.5f);
                writeVariable("app:layout_constraintDimensionRatio", layout2.dimensionRatio, (String) null);
                writeXmlConstraint("app:layout_constraintCircle", layout2.circleConstraint);
                writeVariable("app:layout_constraintCircleRadius", (float) layout2.circleRadius, 0.0f);
                writeVariable("app:layout_constraintCircleAngle", layout2.circleAngle, 0.0f);
                writeVariable("android:orientation", (float) layout2.orientation, -1.0f);
                writeVariable("app:layout_constraintVertical_weight", layout2.verticalWeight, -1.0f);
                writeVariable("app:layout_constraintHorizontal_weight", layout2.horizontalWeight, -1.0f);
                writeVariable("app:layout_constraintHorizontal_chainStyle", (float) layout2.horizontalChainStyle, 0.0f);
                writeVariable("app:layout_constraintVertical_chainStyle", (float) layout2.verticalChainStyle, 0.0f);
                writeVariable("app:barrierDirection", (float) layout2.mBarrierDirection, -1.0f);
                writeVariable("app:barrierMargin", (float) layout2.mBarrierMargin, 0.0f);
                writeDimension("app:layout_marginLeft", layout2.leftMargin, 0);
                writeDimension("app:layout_goneMarginLeft", layout2.goneLeftMargin, Integer.MIN_VALUE);
                writeDimension("app:layout_marginRight", layout2.rightMargin, 0);
                writeDimension("app:layout_goneMarginRight", layout2.goneRightMargin, Integer.MIN_VALUE);
                writeDimension("app:layout_marginStart", layout2.startMargin, 0);
                writeDimension("app:layout_goneMarginStart", layout2.goneStartMargin, Integer.MIN_VALUE);
                writeDimension("app:layout_marginEnd", layout2.endMargin, 0);
                writeDimension("app:layout_goneMarginEnd", layout2.goneEndMargin, Integer.MIN_VALUE);
                writeDimension("app:layout_marginTop", layout2.topMargin, 0);
                writeDimension("app:layout_goneMarginTop", layout2.goneTopMargin, Integer.MIN_VALUE);
                writeDimension("app:layout_marginBottom", layout2.bottomMargin, 0);
                writeDimension("app:layout_goneMarginBottom", layout2.goneBottomMargin, Integer.MIN_VALUE);
                writeDimension("app:goneBaselineMargin", layout2.goneBaselineMargin, Integer.MIN_VALUE);
                writeDimension("app:baselineMargin", layout2.baselineMargin, 0);
                writeBoolen("app:layout_constrainedWidth", layout2.constrainedWidth, false);
                writeBoolen("app:layout_constrainedHeight", layout2.constrainedHeight, false);
                writeBoolen("app:barrierAllowsGoneWidgets", layout2.mBarrierAllowsGoneWidgets, true);
                writeVariable("app:layout_wrapBehaviorInParent", (float) layout2.mWrapBehavior, 0.0f);
                writeXmlConstraint("app:baselineToBaseline", layout2.baselineToBaseline);
                writeXmlConstraint("app:baselineToBottom", layout2.baselineToBottom);
                writeXmlConstraint("app:baselineToTop", layout2.baselineToTop);
                writeXmlConstraint("app:layout_constraintBottom_toBottomOf", layout2.bottomToBottom);
                writeXmlConstraint("app:layout_constraintBottom_toTopOf", layout2.bottomToTop);
                writeXmlConstraint("app:layout_constraintEnd_toEndOf", layout2.endToEnd);
                writeXmlConstraint("app:layout_constraintEnd_toStartOf", layout2.endToStart);
                writeXmlConstraint("app:layout_constraintLeft_toLeftOf", layout2.leftToLeft);
                writeXmlConstraint("app:layout_constraintLeft_toRightOf", layout2.leftToRight);
                writeXmlConstraint("app:layout_constraintRight_toLeftOf", layout2.rightToLeft);
                writeXmlConstraint("app:layout_constraintRight_toRightOf", layout2.rightToRight);
                writeXmlConstraint("app:layout_constraintStart_toEndOf", layout2.startToEnd);
                writeXmlConstraint("app:layout_constraintStart_toStartOf", layout2.startToStart);
                writeXmlConstraint("app:layout_constraintTop_toBottomOf", layout2.topToBottom);
                writeXmlConstraint("app:layout_constraintTop_toTopOf", layout2.topToTop);
                String[] strArr = {"spread", "wrap", "percent"};
                writeEnum("app:layout_constraintHeight_default", layout2.heightDefault, strArr, 0);
                writeVariable("app:layout_constraintHeight_percent", layout2.heightPercent, 1.0f);
                writeDimension("app:layout_constraintHeight_min", layout2.heightMin, 0);
                writeDimension("app:layout_constraintHeight_max", layout2.heightMax, 0);
                writeBoolen("android:layout_constrainedHeight", layout2.constrainedHeight, false);
                writeEnum("app:layout_constraintWidth_default", layout2.widthDefault, strArr, 0);
                writeVariable("app:layout_constraintWidth_percent", layout2.widthPercent, 1.0f);
                writeDimension("app:layout_constraintWidth_min", layout2.widthMin, 0);
                writeDimension("app:layout_constraintWidth_max", layout2.widthMax, 0);
                writeBoolen("android:layout_constrainedWidth", layout2.constrainedWidth, false);
                writeVariable("app:layout_constraintVertical_weight", layout2.verticalWeight, -1.0f);
                writeVariable("app:layout_constraintHorizontal_weight", layout2.horizontalWeight, -1.0f);
                writeVariable("app:layout_constraintHorizontal_chainStyle", layout2.horizontalChainStyle);
                writeVariable("app:layout_constraintVertical_chainStyle", layout2.verticalChainStyle);
                writeEnum("app:barrierDirection", layout2.mBarrierDirection, new String[]{"left", "right", "top", "bottom", "start", "end"}, -1);
                writeVariable("app:layout_constraintTag", layout2.mConstraintTag, (String) null);
                if (layout2.mReferenceIds != null) {
                    writeVariable("'ReferenceIds'", layout2.mReferenceIds);
                }
                this.writer.write(" />\n");
            }
            this.writer.write("</ConstraintSet>\n");
        }

        /* access modifiers changed from: package-private */
        public void writeVariable(String name, float value, float def) throws IOException {
            if (value != def) {
                this.writer.write(SPACE + name);
                this.writer.write("=\"" + value + "\"");
            }
        }

        /* access modifiers changed from: package-private */
        public void writeVariable(String name, int value) throws IOException {
            if (value != 0 && value != -1) {
                this.writer.write(SPACE + name + "=\"" + value + "\"\n");
            }
        }

        /* access modifiers changed from: package-private */
        public void writeVariable(String name, String value) throws IOException {
            if (value != null) {
                this.writer.write(name);
                this.writer.write(":");
                this.writer.write(", " + value);
                this.writer.write("\n");
            }
        }

        /* access modifiers changed from: package-private */
        public void writeVariable(String name, String value, String def) throws IOException {
            if (value != null && !value.equals(def)) {
                this.writer.write(SPACE + name);
                this.writer.write("=\"" + value + "\"");
            }
        }

        /* access modifiers changed from: package-private */
        public void writeVariable(String name, int[] value) throws IOException {
            if (value != null) {
                this.writer.write(SPACE + name);
                this.writer.write(":");
                int i = 0;
                while (i < value.length) {
                    this.writer.write((i == 0 ? "[" : ", ") + getName(value[i]));
                    i++;
                }
                this.writer.write("],\n");
            }
        }

        /* access modifiers changed from: package-private */
        public void writeXmlConstraint(String str, int leftToLeft) throws IOException {
            if (leftToLeft != -1) {
                this.writer.write(SPACE + str);
                this.writer.write("=\"" + getName(leftToLeft) + "\"");
            }
        }
    }

    static {
        mapToConstant.append(R.styleable.Constraint_layout_constraintLeft_toLeftOf, 25);
        mapToConstant.append(R.styleable.Constraint_layout_constraintLeft_toRightOf, 26);
        mapToConstant.append(R.styleable.Constraint_layout_constraintRight_toLeftOf, 29);
        mapToConstant.append(R.styleable.Constraint_layout_constraintRight_toRightOf, 30);
        mapToConstant.append(R.styleable.Constraint_layout_constraintTop_toTopOf, 36);
        mapToConstant.append(R.styleable.Constraint_layout_constraintTop_toBottomOf, 35);
        mapToConstant.append(R.styleable.Constraint_layout_constraintBottom_toTopOf, 4);
        mapToConstant.append(R.styleable.Constraint_layout_constraintBottom_toBottomOf, 3);
        mapToConstant.append(R.styleable.Constraint_layout_constraintBaseline_toBaselineOf, 1);
        mapToConstant.append(R.styleable.Constraint_layout_constraintBaseline_toTopOf, BASELINE_TO_TOP);
        mapToConstant.append(R.styleable.Constraint_layout_constraintBaseline_toBottomOf, BASELINE_TO_BOTTOM);
        mapToConstant.append(R.styleable.Constraint_layout_editor_absoluteX, 6);
        mapToConstant.append(R.styleable.Constraint_layout_editor_absoluteY, 7);
        mapToConstant.append(R.styleable.Constraint_layout_constraintGuide_begin, 17);
        mapToConstant.append(R.styleable.Constraint_layout_constraintGuide_end, 18);
        mapToConstant.append(R.styleable.Constraint_layout_constraintGuide_percent, 19);
        mapToConstant.append(R.styleable.Constraint_guidelineUseRtl, GUIDELINE_USE_RTL);
        mapToConstant.append(R.styleable.Constraint_android_orientation, 27);
        mapToConstant.append(R.styleable.Constraint_layout_constraintStart_toEndOf, 32);
        mapToConstant.append(R.styleable.Constraint_layout_constraintStart_toStartOf, 33);
        mapToConstant.append(R.styleable.Constraint_layout_constraintEnd_toStartOf, 10);
        mapToConstant.append(R.styleable.Constraint_layout_constraintEnd_toEndOf, 9);
        mapToConstant.append(R.styleable.Constraint_layout_goneMarginLeft, 13);
        mapToConstant.append(R.styleable.Constraint_layout_goneMarginTop, 16);
        mapToConstant.append(R.styleable.Constraint_layout_goneMarginRight, 14);
        mapToConstant.append(R.styleable.Constraint_layout_goneMarginBottom, 11);
        mapToConstant.append(R.styleable.Constraint_layout_goneMarginStart, 15);
        mapToConstant.append(R.styleable.Constraint_layout_goneMarginEnd, 12);
        mapToConstant.append(R.styleable.Constraint_layout_constraintVertical_weight, 40);
        mapToConstant.append(R.styleable.Constraint_layout_constraintHorizontal_weight, 39);
        mapToConstant.append(R.styleable.Constraint_layout_constraintHorizontal_chainStyle, 41);
        mapToConstant.append(R.styleable.Constraint_layout_constraintVertical_chainStyle, 42);
        mapToConstant.append(R.styleable.Constraint_layout_constraintHorizontal_bias, 20);
        mapToConstant.append(R.styleable.Constraint_layout_constraintVertical_bias, 37);
        mapToConstant.append(R.styleable.Constraint_layout_constraintDimensionRatio, 5);
        mapToConstant.append(R.styleable.Constraint_layout_constraintLeft_creator, UNUSED);
        mapToConstant.append(R.styleable.Constraint_layout_constraintTop_creator, UNUSED);
        mapToConstant.append(R.styleable.Constraint_layout_constraintRight_creator, UNUSED);
        mapToConstant.append(R.styleable.Constraint_layout_constraintBottom_creator, UNUSED);
        mapToConstant.append(R.styleable.Constraint_layout_constraintBaseline_creator, UNUSED);
        mapToConstant.append(R.styleable.Constraint_android_layout_marginLeft, 24);
        mapToConstant.append(R.styleable.Constraint_android_layout_marginRight, 28);
        mapToConstant.append(R.styleable.Constraint_android_layout_marginStart, 31);
        mapToConstant.append(R.styleable.Constraint_android_layout_marginEnd, 8);
        mapToConstant.append(R.styleable.Constraint_android_layout_marginTop, 34);
        mapToConstant.append(R.styleable.Constraint_android_layout_marginBottom, 2);
        mapToConstant.append(R.styleable.Constraint_android_layout_width, 23);
        mapToConstant.append(R.styleable.Constraint_android_layout_height, 21);
        mapToConstant.append(R.styleable.Constraint_layout_constraintWidth, LAYOUT_CONSTRAINT_WIDTH);
        mapToConstant.append(R.styleable.Constraint_layout_constraintHeight, LAYOUT_CONSTRAINT_HEIGHT);
        mapToConstant.append(R.styleable.Constraint_android_visibility, 22);
        mapToConstant.append(R.styleable.Constraint_android_alpha, 43);
        mapToConstant.append(R.styleable.Constraint_android_elevation, 44);
        mapToConstant.append(R.styleable.Constraint_android_rotationX, 45);
        mapToConstant.append(R.styleable.Constraint_android_rotationY, 46);
        mapToConstant.append(R.styleable.Constraint_android_rotation, 60);
        mapToConstant.append(R.styleable.Constraint_android_scaleX, 47);
        mapToConstant.append(R.styleable.Constraint_android_scaleY, 48);
        mapToConstant.append(R.styleable.Constraint_android_transformPivotX, 49);
        mapToConstant.append(R.styleable.Constraint_android_transformPivotY, 50);
        mapToConstant.append(R.styleable.Constraint_android_translationX, 51);
        mapToConstant.append(R.styleable.Constraint_android_translationY, 52);
        mapToConstant.append(R.styleable.Constraint_android_translationZ, 53);
        mapToConstant.append(R.styleable.Constraint_layout_constraintWidth_default, 54);
        mapToConstant.append(R.styleable.Constraint_layout_constraintHeight_default, 55);
        mapToConstant.append(R.styleable.Constraint_layout_constraintWidth_max, WIDTH_MAX);
        mapToConstant.append(R.styleable.Constraint_layout_constraintHeight_max, HEIGHT_MAX);
        mapToConstant.append(R.styleable.Constraint_layout_constraintWidth_min, WIDTH_MIN);
        mapToConstant.append(R.styleable.Constraint_layout_constraintHeight_min, HEIGHT_MIN);
        mapToConstant.append(R.styleable.Constraint_layout_constraintCircle, 61);
        mapToConstant.append(R.styleable.Constraint_layout_constraintCircleRadius, CIRCLE_RADIUS);
        mapToConstant.append(R.styleable.Constraint_layout_constraintCircleAngle, 63);
        mapToConstant.append(R.styleable.Constraint_animateRelativeTo, 64);
        mapToConstant.append(R.styleable.Constraint_transitionEasing, 65);
        mapToConstant.append(R.styleable.Constraint_drawPath, 66);
        mapToConstant.append(R.styleable.Constraint_transitionPathRotate, 67);
        mapToConstant.append(R.styleable.Constraint_motionStagger, MOTION_STAGGER);
        mapToConstant.append(R.styleable.Constraint_android_id, 38);
        mapToConstant.append(R.styleable.Constraint_motionProgress, PROGRESS);
        mapToConstant.append(R.styleable.Constraint_layout_constraintWidth_percent, WIDTH_PERCENT);
        mapToConstant.append(R.styleable.Constraint_layout_constraintHeight_percent, HEIGHT_PERCENT);
        mapToConstant.append(R.styleable.Constraint_layout_wrapBehaviorInParent, LAYOUT_WRAP_BEHAVIOR);
        mapToConstant.append(R.styleable.Constraint_chainUseRtl, CHAIN_USE_RTL);
        mapToConstant.append(R.styleable.Constraint_barrierDirection, BARRIER_DIRECTION);
        mapToConstant.append(R.styleable.Constraint_barrierMargin, BARRIER_MARGIN);
        mapToConstant.append(R.styleable.Constraint_constraint_referenced_ids, CONSTRAINT_REFERENCED_IDS);
        mapToConstant.append(R.styleable.Constraint_barrierAllowsGoneWidgets, BARRIER_ALLOWS_GONE_WIDGETS);
        mapToConstant.append(R.styleable.Constraint_pathMotionArc, PATH_MOTION_ARC);
        mapToConstant.append(R.styleable.Constraint_layout_constraintTag, CONSTRAINT_TAG);
        mapToConstant.append(R.styleable.Constraint_visibilityMode, VISIBILITY_MODE);
        mapToConstant.append(R.styleable.Constraint_layout_constrainedWidth, CONSTRAINED_WIDTH);
        mapToConstant.append(R.styleable.Constraint_layout_constrainedHeight, CONSTRAINED_HEIGHT);
        mapToConstant.append(R.styleable.Constraint_polarRelativeTo, ANIMATE_CIRCLE_ANGLE_TO);
        mapToConstant.append(R.styleable.Constraint_transformPivotTarget, TRANSFORM_PIVOT_TARGET);
        mapToConstant.append(R.styleable.Constraint_quantizeMotionSteps, QUANTIZE_MOTION_STEPS);
        mapToConstant.append(R.styleable.Constraint_quantizeMotionPhase, QUANTIZE_MOTION_PHASE);
        mapToConstant.append(R.styleable.Constraint_quantizeMotionInterpolator, QUANTIZE_MOTION_INTERPOLATOR);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_editor_absoluteY, 6);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_editor_absoluteY, 7);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_orientation, 27);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_goneMarginLeft, 13);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_goneMarginTop, 16);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_goneMarginRight, 14);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_goneMarginBottom, 11);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_goneMarginStart, 15);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_goneMarginEnd, 12);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintVertical_weight, 40);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintHorizontal_weight, 39);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintHorizontal_chainStyle, 41);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintVertical_chainStyle, 42);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintHorizontal_bias, 20);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintVertical_bias, 37);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintDimensionRatio, 5);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintLeft_creator, UNUSED);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintTop_creator, UNUSED);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintRight_creator, UNUSED);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintBottom_creator, UNUSED);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintBaseline_creator, UNUSED);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_layout_marginLeft, 24);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_layout_marginRight, 28);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_layout_marginStart, 31);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_layout_marginEnd, 8);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_layout_marginTop, 34);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_layout_marginBottom, 2);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_layout_width, 23);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_layout_height, 21);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintWidth, LAYOUT_CONSTRAINT_WIDTH);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintHeight, LAYOUT_CONSTRAINT_HEIGHT);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_visibility, 22);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_alpha, 43);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_elevation, 44);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_rotationX, 45);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_rotationY, 46);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_rotation, 60);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_scaleX, 47);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_scaleY, 48);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_transformPivotX, 49);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_transformPivotY, 50);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_translationX, 51);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_translationY, 52);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_translationZ, 53);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintWidth_default, 54);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintHeight_default, 55);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintWidth_max, WIDTH_MAX);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintHeight_max, HEIGHT_MAX);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintWidth_min, WIDTH_MIN);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintHeight_min, HEIGHT_MIN);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintCircleRadius, CIRCLE_RADIUS);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintCircleAngle, 63);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_animateRelativeTo, 64);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_transitionEasing, 65);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_drawPath, 66);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_transitionPathRotate, 67);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_motionStagger, MOTION_STAGGER);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_android_id, 38);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_motionTarget, MOTION_TARGET);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_motionProgress, PROGRESS);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintWidth_percent, WIDTH_PERCENT);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintHeight_percent, HEIGHT_PERCENT);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_chainUseRtl, CHAIN_USE_RTL);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_barrierDirection, BARRIER_DIRECTION);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_barrierMargin, BARRIER_MARGIN);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_constraint_referenced_ids, CONSTRAINT_REFERENCED_IDS);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_barrierAllowsGoneWidgets, BARRIER_ALLOWS_GONE_WIDGETS);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_pathMotionArc, PATH_MOTION_ARC);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintTag, CONSTRAINT_TAG);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_visibilityMode, VISIBILITY_MODE);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constrainedWidth, CONSTRAINED_WIDTH);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constrainedHeight, CONSTRAINED_HEIGHT);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_polarRelativeTo, ANIMATE_CIRCLE_ANGLE_TO);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_transformPivotTarget, TRANSFORM_PIVOT_TARGET);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_quantizeMotionSteps, QUANTIZE_MOTION_STEPS);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_quantizeMotionPhase, QUANTIZE_MOTION_PHASE);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_quantizeMotionInterpolator, QUANTIZE_MOTION_INTERPOLATOR);
        overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_wrapBehaviorInParent, LAYOUT_WRAP_BEHAVIOR);
    }

    private void addAttributes(ConstraintAttribute.AttributeType attributeType, String... attributeName) {
        for (int i = 0; i < attributeName.length; i++) {
            if (this.mSavedAttributes.containsKey(attributeName[i])) {
                ConstraintAttribute constraintAttribute = this.mSavedAttributes.get(attributeName[i]);
                if (!(constraintAttribute == null || constraintAttribute.getType() == attributeType)) {
                    throw new IllegalArgumentException("ConstraintAttribute is already a " + constraintAttribute.getType().name());
                }
            } else {
                this.mSavedAttributes.put(attributeName[i], new ConstraintAttribute(attributeName[i], attributeType));
            }
        }
    }

    public static Constraint buildDelta(Context context, XmlPullParser parser) {
        AttributeSet asAttributeSet = Xml.asAttributeSet(parser);
        Constraint constraint = new Constraint();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(asAttributeSet, R.styleable.ConstraintOverride);
        populateOverride(context, constraint, obtainStyledAttributes);
        obtainStyledAttributes.recycle();
        return constraint;
    }

    private int[] convertReferenceString(View view, String referenceIdString) {
        Object designInformation;
        String[] split = referenceIdString.split(",");
        Context context = view.getContext();
        int[] iArr = new int[split.length];
        int i = 0;
        int i2 = 0;
        while (i2 < split.length) {
            String trim = split[i2].trim();
            int i3 = 0;
            try {
                i3 = R.id.class.getField(trim).getInt((Object) null);
            } catch (Exception e) {
            }
            if (i3 == 0) {
                i3 = context.getResources().getIdentifier(trim, "id", context.getPackageName());
            }
            if (i3 == 0 && view.isInEditMode() && (view.getParent() instanceof ConstraintLayout) && (designInformation = ((ConstraintLayout) view.getParent()).getDesignInformation(0, trim)) != null && (designInformation instanceof Integer)) {
                i3 = ((Integer) designInformation).intValue();
            }
            iArr[i] = i3;
            i2++;
            i++;
        }
        return i != split.length ? Arrays.copyOf(iArr, i) : iArr;
    }

    private void createHorizontalChain(int leftId, int leftSide, int rightId, int rightSide, int[] chainIds, float[] weights, int style, int left, int right) {
        int[] iArr = chainIds;
        float[] fArr = weights;
        if (iArr.length < 2) {
            int i = style;
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        } else if (fArr == null || fArr.length == iArr.length) {
            if (fArr != null) {
                get(iArr[0]).layout.horizontalWeight = fArr[0];
            }
            get(iArr[0]).layout.horizontalChainStyle = style;
            connect(iArr[0], left, leftId, leftSide, -1);
            for (int i2 = 1; i2 < iArr.length; i2++) {
                int i3 = iArr[i2];
                connect(iArr[i2], left, iArr[i2 - 1], right, -1);
                connect(iArr[i2 - 1], right, iArr[i2], left, -1);
                if (fArr != null) {
                    get(iArr[i2]).layout.horizontalWeight = fArr[i2];
                }
            }
            connect(iArr[iArr.length - 1], right, rightId, rightSide, -1);
        } else {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        }
    }

    private Constraint fillFromAttributeList(Context context, AttributeSet attrs, boolean override) {
        Constraint constraint = new Constraint();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, override ? R.styleable.ConstraintOverride : R.styleable.Constraint);
        populateConstraint(context, constraint, obtainStyledAttributes, override);
        obtainStyledAttributes.recycle();
        return constraint;
    }

    private Constraint get(int id) {
        if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
            this.mConstraints.put(Integer.valueOf(id), new Constraint());
        }
        return this.mConstraints.get(Integer.valueOf(id));
    }

    static String getDebugName(int v) {
        for (Field field : ConstraintSet.class.getDeclaredFields()) {
            if (field.getName().contains("_") && field.getType() == Integer.TYPE && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
                try {
                    if (field.getInt((Object) null) == v) {
                        return field.getName();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return "UNKNOWN";
    }

    static String getLine(Context context, int resourceId, XmlPullParser pullParser) {
        StringBuilder append = new StringBuilder().append(".(");
        String name = Debug.getName(context, resourceId);
        Log1F380D.a((Object) name);
        return append.append(name).append(".xml:").append(pullParser.getLineNumber()).append(") \"").append(pullParser.getName()).append("\"").toString();
    }

    /* access modifiers changed from: private */
    public static int lookupID(TypedArray a, int index, int def) {
        int resourceId = a.getResourceId(index, def);
        return resourceId == -1 ? a.getInt(index, -1) : resourceId;
    }

    static void parseDimensionConstraints(Object data, TypedArray a, int attr, int orientation) {
        if (data != null) {
            int i = 0;
            boolean z = false;
            switch (a.peekValue(attr).type) {
                case 3:
                    parseDimensionConstraintsString(data, a.getString(attr), orientation);
                    return;
                case 5:
                    i = a.getDimensionPixelSize(attr, 0);
                    break;
                default:
                    int i2 = a.getInt(attr, 0);
                    switch (i2) {
                        case -4:
                            i = -2;
                            z = true;
                            break;
                        case -3:
                            i = 0;
                            break;
                        case -2:
                        case -1:
                            i = i2;
                            break;
                    }
            }
            if (data instanceof ConstraintLayout.LayoutParams) {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) data;
                if (orientation == 0) {
                    layoutParams.width = i;
                    layoutParams.constrainedWidth = z;
                    return;
                }
                layoutParams.height = i;
                layoutParams.constrainedHeight = z;
            } else if (data instanceof Layout) {
                Layout layout = (Layout) data;
                if (orientation == 0) {
                    layout.mWidth = i;
                    layout.constrainedWidth = z;
                    return;
                }
                layout.mHeight = i;
                layout.constrainedHeight = z;
            } else if (data instanceof Constraint.Delta) {
                Constraint.Delta delta = (Constraint.Delta) data;
                if (orientation == 0) {
                    delta.add(23, i);
                    delta.add((int) CONSTRAINED_WIDTH, z);
                    return;
                }
                delta.add(21, i);
                delta.add((int) CONSTRAINED_HEIGHT, z);
            }
        }
    }

    static void parseDimensionConstraintsString(Object data, String value, int orientation) {
        if (value != null) {
            int indexOf = value.indexOf(61);
            int length = value.length();
            if (indexOf > 0 && indexOf < length - 1) {
                String substring = value.substring(0, indexOf);
                String substring2 = value.substring(indexOf + 1);
                if (substring2.length() > 0) {
                    String trim = substring.trim();
                    String trim2 = substring2.trim();
                    if (KEY_RATIO.equalsIgnoreCase(trim)) {
                        if (data instanceof ConstraintLayout.LayoutParams) {
                            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) data;
                            if (orientation == 0) {
                                layoutParams.width = 0;
                            } else {
                                layoutParams.height = 0;
                            }
                            parseDimensionRatioString(layoutParams, trim2);
                        } else if (data instanceof Layout) {
                            ((Layout) data).dimensionRatio = trim2;
                        } else if (data instanceof Constraint.Delta) {
                            ((Constraint.Delta) data).add(5, trim2);
                        }
                    } else if (KEY_WEIGHT.equalsIgnoreCase(trim)) {
                        try {
                            float parseFloat = Float.parseFloat(trim2);
                            if (data instanceof ConstraintLayout.LayoutParams) {
                                ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) data;
                                if (orientation == 0) {
                                    layoutParams2.width = 0;
                                    layoutParams2.horizontalWeight = parseFloat;
                                } else {
                                    layoutParams2.height = 0;
                                    layoutParams2.verticalWeight = parseFloat;
                                }
                            } else if (data instanceof Layout) {
                                Layout layout = (Layout) data;
                                if (orientation == 0) {
                                    layout.mWidth = 0;
                                    layout.horizontalWeight = parseFloat;
                                    return;
                                }
                                layout.mHeight = 0;
                                layout.verticalWeight = parseFloat;
                            } else if (data instanceof Constraint.Delta) {
                                Constraint.Delta delta = (Constraint.Delta) data;
                                if (orientation == 0) {
                                    delta.add(23, 0);
                                    delta.add(39, parseFloat);
                                    return;
                                }
                                delta.add(21, 0);
                                delta.add(40, parseFloat);
                            }
                        } catch (NumberFormatException e) {
                        }
                    } else if (KEY_PERCENT_PARENT.equalsIgnoreCase(trim)) {
                        try {
                            float max = Math.max(0.0f, Math.min(1.0f, Float.parseFloat(trim2)));
                            if (data instanceof ConstraintLayout.LayoutParams) {
                                ConstraintLayout.LayoutParams layoutParams3 = (ConstraintLayout.LayoutParams) data;
                                if (orientation == 0) {
                                    layoutParams3.width = 0;
                                    layoutParams3.matchConstraintPercentWidth = max;
                                    layoutParams3.matchConstraintDefaultWidth = 2;
                                } else {
                                    layoutParams3.height = 0;
                                    layoutParams3.matchConstraintPercentHeight = max;
                                    layoutParams3.matchConstraintDefaultHeight = 2;
                                }
                            } else if (data instanceof Layout) {
                                Layout layout2 = (Layout) data;
                                if (orientation == 0) {
                                    layout2.mWidth = 0;
                                    layout2.widthPercent = max;
                                    layout2.widthDefault = 2;
                                    return;
                                }
                                layout2.mHeight = 0;
                                layout2.heightPercent = max;
                                layout2.heightDefault = 2;
                            } else if (data instanceof Constraint.Delta) {
                                Constraint.Delta delta2 = (Constraint.Delta) data;
                                if (orientation == 0) {
                                    delta2.add(23, 0);
                                    delta2.add(54, 2);
                                    return;
                                }
                                delta2.add(21, 0);
                                delta2.add(55, 2);
                            }
                        } catch (NumberFormatException e2) {
                        }
                    }
                }
            }
        }
    }

    static void parseDimensionRatioString(ConstraintLayout.LayoutParams params, String value) {
        int i;
        String str = value;
        float f = Float.NaN;
        int i2 = -1;
        if (str != null) {
            int length = str.length();
            int indexOf = str.indexOf(44);
            if (indexOf <= 0 || indexOf >= length - 1) {
                i = 0;
            } else {
                String substring = str.substring(0, indexOf);
                if (substring.equalsIgnoreCase("W")) {
                    i2 = 0;
                } else if (substring.equalsIgnoreCase("H")) {
                    i2 = 1;
                }
                i = indexOf + 1;
            }
            int indexOf2 = str.indexOf(WIDTH_MIN);
            if (indexOf2 < 0 || indexOf2 >= length - 1) {
                String substring2 = str.substring(i);
                if (substring2.length() > 0) {
                    try {
                        f = Float.parseFloat(substring2);
                    } catch (NumberFormatException e) {
                    }
                }
            } else {
                String substring3 = str.substring(i, indexOf2);
                String substring4 = str.substring(indexOf2 + 1);
                if (substring3.length() > 0 && substring4.length() > 0) {
                    try {
                        float parseFloat = Float.parseFloat(substring3);
                        float parseFloat2 = Float.parseFloat(substring4);
                        if (parseFloat > 0.0f && parseFloat2 > 0.0f) {
                            f = i2 == 1 ? Math.abs(parseFloat2 / parseFloat) : Math.abs(parseFloat / parseFloat2);
                        }
                    } catch (NumberFormatException e2) {
                    }
                }
            }
        }
        params.dimensionRatio = str;
        params.dimensionRatioValue = f;
        params.dimensionRatioSide = i2;
    }

    /* access modifiers changed from: private */
    public static void setDeltaValue(Constraint c, int type, float value) {
        switch (type) {
            case 19:
                c.layout.guidePercent = value;
                return;
            case 20:
                c.layout.horizontalBias = value;
                return;
            case 37:
                c.layout.verticalBias = value;
                return;
            case 39:
                c.layout.horizontalWeight = value;
                return;
            case 40:
                c.layout.verticalWeight = value;
                return;
            case 43:
                c.propertySet.alpha = value;
                return;
            case 44:
                c.transform.elevation = value;
                c.transform.applyElevation = true;
                return;
            case 45:
                c.transform.rotationX = value;
                return;
            case 46:
                c.transform.rotationY = value;
                return;
            case 47:
                c.transform.scaleX = value;
                return;
            case 48:
                c.transform.scaleY = value;
                return;
            case 49:
                c.transform.transformPivotX = value;
                return;
            case 50:
                c.transform.transformPivotY = value;
                return;
            case 51:
                c.transform.translationX = value;
                return;
            case 52:
                c.transform.translationY = value;
                return;
            case 53:
                c.transform.translationZ = value;
                return;
            case 60:
                c.transform.rotation = value;
                return;
            case 63:
                c.layout.circleAngle = value;
                return;
            case 67:
                c.motion.mPathRotate = value;
                return;
            case PROGRESS /*68*/:
                c.propertySet.mProgress = value;
                return;
            case WIDTH_PERCENT /*69*/:
                c.layout.widthPercent = value;
                return;
            case HEIGHT_PERCENT /*70*/:
                c.layout.heightPercent = value;
                return;
            case MOTION_STAGGER /*79*/:
                c.motion.mMotionStagger = value;
                return;
            case QUANTIZE_MOTION_PHASE /*85*/:
                c.motion.mQuantizeMotionPhase = value;
                return;
            case UNUSED /*87*/:
                return;
            default:
                Log.w(TAG, "Unknown attribute 0x");
                return;
        }
    }

    /* access modifiers changed from: private */
    public static void setDeltaValue(Constraint c, int type, int value) {
        switch (type) {
            case 2:
                c.layout.bottomMargin = value;
                return;
            case 6:
                c.layout.editorAbsoluteX = value;
                return;
            case 7:
                c.layout.editorAbsoluteY = value;
                return;
            case 8:
                c.layout.endMargin = value;
                return;
            case 11:
                c.layout.goneBottomMargin = value;
                return;
            case 12:
                c.layout.goneEndMargin = value;
                return;
            case 13:
                c.layout.goneLeftMargin = value;
                return;
            case 14:
                c.layout.goneRightMargin = value;
                return;
            case 15:
                c.layout.goneStartMargin = value;
                return;
            case 16:
                c.layout.goneTopMargin = value;
                return;
            case 17:
                c.layout.guideBegin = value;
                return;
            case 18:
                c.layout.guideEnd = value;
                return;
            case 21:
                c.layout.mHeight = value;
                return;
            case 22:
                c.propertySet.visibility = value;
                return;
            case 23:
                c.layout.mWidth = value;
                return;
            case 24:
                c.layout.leftMargin = value;
                return;
            case 27:
                c.layout.orientation = value;
                return;
            case 28:
                c.layout.rightMargin = value;
                return;
            case 31:
                c.layout.startMargin = value;
                return;
            case 34:
                c.layout.topMargin = value;
                return;
            case 38:
                c.mViewId = value;
                return;
            case 41:
                c.layout.horizontalChainStyle = value;
                return;
            case 42:
                c.layout.verticalChainStyle = value;
                return;
            case 54:
                c.layout.widthDefault = value;
                return;
            case 55:
                c.layout.heightDefault = value;
                return;
            case WIDTH_MAX /*56*/:
                c.layout.widthMax = value;
                return;
            case HEIGHT_MAX /*57*/:
                c.layout.heightMax = value;
                return;
            case WIDTH_MIN /*58*/:
                c.layout.widthMin = value;
                return;
            case HEIGHT_MIN /*59*/:
                c.layout.heightMin = value;
                return;
            case 61:
                c.layout.circleConstraint = value;
                return;
            case CIRCLE_RADIUS /*62*/:
                c.layout.circleRadius = value;
                return;
            case 64:
                c.motion.mAnimateRelativeTo = value;
                return;
            case 66:
                c.motion.mDrawPath = value;
                return;
            case BARRIER_DIRECTION /*72*/:
                c.layout.mBarrierDirection = value;
                return;
            case BARRIER_MARGIN /*73*/:
                c.layout.mBarrierMargin = value;
                return;
            case PATH_MOTION_ARC /*76*/:
                c.motion.mPathMotionArc = value;
                return;
            case VISIBILITY_MODE /*78*/:
                c.propertySet.mVisibilityMode = value;
                return;
            case ANIMATE_CIRCLE_ANGLE_TO /*82*/:
                c.motion.mAnimateCircleAngleTo = value;
                return;
            case TRANSFORM_PIVOT_TARGET /*83*/:
                c.transform.transformPivotTarget = value;
                return;
            case QUANTIZE_MOTION_STEPS /*84*/:
                c.motion.mQuantizeMotionSteps = value;
                return;
            case UNUSED /*87*/:
                return;
            case QUANTIZE_MOTION_INTERPOLATOR_TYPE /*88*/:
                c.motion.mQuantizeInterpolatorType = value;
                return;
            case QUANTIZE_MOTION_INTERPOLATOR_ID /*89*/:
                c.motion.mQuantizeInterpolatorID = value;
                return;
            case BASELINE_MARGIN /*93*/:
                c.layout.baselineMargin = value;
                return;
            case GONE_BASELINE_MARGIN /*94*/:
                c.layout.goneBaselineMargin = value;
                return;
            case LAYOUT_WRAP_BEHAVIOR /*97*/:
                c.layout.mWrapBehavior = value;
                return;
            default:
                Log.w(TAG, "Unknown attribute 0x");
                return;
        }
    }

    /* access modifiers changed from: private */
    public static void setDeltaValue(Constraint c, int type, String value) {
        switch (type) {
            case 5:
                c.layout.dimensionRatio = value;
                return;
            case 65:
                c.motion.mTransitionEasing = value;
                return;
            case CONSTRAINT_REFERENCED_IDS /*74*/:
                c.layout.mReferenceIdString = value;
                c.layout.mReferenceIds = null;
                return;
            case CONSTRAINT_TAG /*77*/:
                c.layout.mConstraintTag = value;
                return;
            case UNUSED /*87*/:
                return;
            case QUANTIZE_MOTION_INTERPOLATOR_STR /*90*/:
                c.motion.mQuantizeInterpolatorString = value;
                return;
            default:
                Log.w(TAG, "Unknown attribute 0x");
                return;
        }
    }

    /* access modifiers changed from: private */
    public static void setDeltaValue(Constraint c, int type, boolean value) {
        switch (type) {
            case 44:
                c.transform.applyElevation = value;
                return;
            case BARRIER_ALLOWS_GONE_WIDGETS /*75*/:
                c.layout.mBarrierAllowsGoneWidgets = value;
                return;
            case CONSTRAINED_WIDTH /*80*/:
                c.layout.constrainedWidth = value;
                return;
            case CONSTRAINED_HEIGHT /*81*/:
                c.layout.constrainedHeight = value;
                return;
            case UNUSED /*87*/:
                return;
            default:
                Log.w(TAG, "Unknown attribute 0x");
                return;
        }
    }

    private String sideToString(int side) {
        switch (side) {
            case 1:
                return "left";
            case 2:
                return "right";
            case 3:
                return "top";
            case 4:
                return "bottom";
            case 5:
                return "baseline";
            case 6:
                return "start";
            case 7:
                return "end";
            default:
                return "undefined";
        }
    }

    public void addColorAttributes(String... attributeName) {
        addAttributes(ConstraintAttribute.AttributeType.COLOR_TYPE, attributeName);
    }

    public void addFloatAttributes(String... attributeName) {
        addAttributes(ConstraintAttribute.AttributeType.FLOAT_TYPE, attributeName);
    }

    public void addIntAttributes(String... attributeName) {
        addAttributes(ConstraintAttribute.AttributeType.INT_TYPE, attributeName);
    }

    public void addStringAttributes(String... attributeName) {
        addAttributes(ConstraintAttribute.AttributeType.STRING_TYPE, attributeName);
    }

    public void addToHorizontalChain(int viewId, int leftId, int rightId) {
        connect(viewId, 1, leftId, leftId == 0 ? 1 : 2, 0);
        connect(viewId, 2, rightId, rightId == 0 ? 2 : 1, 0);
        if (leftId != 0) {
            connect(leftId, 2, viewId, 1, 0);
        }
        if (rightId != 0) {
            connect(rightId, 1, viewId, 2, 0);
        }
    }

    public void addToHorizontalChainRTL(int viewId, int leftId, int rightId) {
        connect(viewId, 6, leftId, leftId == 0 ? 6 : 7, 0);
        connect(viewId, 7, rightId, rightId == 0 ? 7 : 6, 0);
        if (leftId != 0) {
            connect(leftId, 7, viewId, 6, 0);
        }
        if (rightId != 0) {
            connect(rightId, 6, viewId, 7, 0);
        }
    }

    public void addToVerticalChain(int viewId, int topId, int bottomId) {
        connect(viewId, 3, topId, topId == 0 ? 3 : 4, 0);
        connect(viewId, 4, bottomId, bottomId == 0 ? 4 : 3, 0);
        if (topId != 0) {
            connect(topId, 4, viewId, 3, 0);
        }
        if (bottomId != 0) {
            connect(bottomId, 3, viewId, 4, 0);
        }
    }

    public void applyDeltaFrom(ConstraintSet cs) {
        for (Constraint next : cs.mConstraints.values()) {
            if (next.mDelta != null) {
                if (next.mTargetString != null) {
                    for (Integer intValue : this.mConstraints.keySet()) {
                        Constraint constraint = getConstraint(intValue.intValue());
                        if (constraint.layout.mConstraintTag != null && next.mTargetString.matches(constraint.layout.mConstraintTag)) {
                            next.mDelta.applyDelta(constraint);
                            constraint.mCustomConstraints.putAll((HashMap) next.mCustomConstraints.clone());
                        }
                    }
                } else {
                    next.mDelta.applyDelta(getConstraint(next.mViewId));
                }
            }
        }
    }

    public void applyTo(ConstraintLayout constraintLayout) {
        applyToInternal(constraintLayout, true);
        constraintLayout.setConstraintSet((ConstraintSet) null);
        constraintLayout.requestLayout();
    }

    public void applyToHelper(ConstraintHelper helper, ConstraintWidget child, ConstraintLayout.LayoutParams layoutParams, SparseArray<ConstraintWidget> sparseArray) {
        Constraint constraint;
        int id = helper.getId();
        if (this.mConstraints.containsKey(Integer.valueOf(id)) && (constraint = this.mConstraints.get(Integer.valueOf(id))) != null && (child instanceof HelperWidget)) {
            helper.loadParameters(constraint, (HelperWidget) child, layoutParams, sparseArray);
        }
    }

    public void applyToLayoutParams(int id, ConstraintLayout.LayoutParams layoutParams) {
        Constraint constraint;
        if (this.mConstraints.containsKey(Integer.valueOf(id)) && (constraint = this.mConstraints.get(Integer.valueOf(id))) != null) {
            constraint.applyTo(layoutParams);
        }
    }

    public void applyToWithoutCustom(ConstraintLayout constraintLayout) {
        applyToInternal(constraintLayout, false);
        constraintLayout.setConstraintSet((ConstraintSet) null);
    }

    public void center(int centerID, int firstID, int firstSide, int firstMargin, int secondId, int secondSide, int secondMargin, float bias) {
        int i = firstSide;
        float f = bias;
        if (firstMargin < 0) {
            throw new IllegalArgumentException("margin must be > 0");
        } else if (secondMargin < 0) {
            throw new IllegalArgumentException("margin must be > 0");
        } else if (f <= 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("bias must be between 0 and 1 inclusive");
        } else if (i == 1 || i == 2) {
            int i2 = centerID;
            connect(i2, 1, firstID, firstSide, firstMargin);
            connect(i2, 2, secondId, secondSide, secondMargin);
            Constraint constraint = this.mConstraints.get(Integer.valueOf(centerID));
            if (constraint != null) {
                constraint.layout.horizontalBias = f;
            }
        } else if (i == 6 || i == 7) {
            int i3 = centerID;
            connect(i3, 6, firstID, firstSide, firstMargin);
            connect(i3, 7, secondId, secondSide, secondMargin);
            Constraint constraint2 = this.mConstraints.get(Integer.valueOf(centerID));
            if (constraint2 != null) {
                constraint2.layout.horizontalBias = f;
            }
        } else {
            int i4 = centerID;
            connect(i4, 3, firstID, firstSide, firstMargin);
            connect(i4, 4, secondId, secondSide, secondMargin);
            Constraint constraint3 = this.mConstraints.get(Integer.valueOf(centerID));
            if (constraint3 != null) {
                constraint3.layout.verticalBias = f;
            }
        }
    }

    public void centerHorizontally(int viewId, int toView) {
        if (toView == 0) {
            center(viewId, 0, 1, 0, 0, 2, 0, 0.5f);
        } else {
            center(viewId, toView, 2, 0, toView, 1, 0, 0.5f);
        }
    }

    public void centerHorizontally(int centerID, int leftId, int leftSide, int leftMargin, int rightId, int rightSide, int rightMargin, float bias) {
        connect(centerID, 1, leftId, leftSide, leftMargin);
        connect(centerID, 2, rightId, rightSide, rightMargin);
        Constraint constraint = this.mConstraints.get(Integer.valueOf(centerID));
        if (constraint != null) {
            constraint.layout.horizontalBias = bias;
            return;
        }
        float f = bias;
    }

    public void centerHorizontallyRtl(int viewId, int toView) {
        if (toView == 0) {
            center(viewId, 0, 6, 0, 0, 7, 0, 0.5f);
        } else {
            center(viewId, toView, 7, 0, toView, 6, 0, 0.5f);
        }
    }

    public void centerHorizontallyRtl(int centerID, int startId, int startSide, int startMargin, int endId, int endSide, int endMargin, float bias) {
        connect(centerID, 6, startId, startSide, startMargin);
        connect(centerID, 7, endId, endSide, endMargin);
        Constraint constraint = this.mConstraints.get(Integer.valueOf(centerID));
        if (constraint != null) {
            constraint.layout.horizontalBias = bias;
            return;
        }
        float f = bias;
    }

    public void centerVertically(int viewId, int toView) {
        if (toView == 0) {
            center(viewId, 0, 3, 0, 0, 4, 0, 0.5f);
        } else {
            center(viewId, toView, 4, 0, toView, 3, 0, 0.5f);
        }
    }

    public void centerVertically(int centerID, int topId, int topSide, int topMargin, int bottomId, int bottomSide, int bottomMargin, float bias) {
        connect(centerID, 3, topId, topSide, topMargin);
        connect(centerID, 4, bottomId, bottomSide, bottomMargin);
        Constraint constraint = this.mConstraints.get(Integer.valueOf(centerID));
        if (constraint != null) {
            constraint.layout.verticalBias = bias;
            return;
        }
        float f = bias;
    }

    public void clear(int viewId) {
        this.mConstraints.remove(Integer.valueOf(viewId));
    }

    public void clear(int viewId, int anchor) {
        Constraint constraint;
        if (this.mConstraints.containsKey(Integer.valueOf(viewId)) && (constraint = this.mConstraints.get(Integer.valueOf(viewId))) != null) {
            switch (anchor) {
                case 1:
                    constraint.layout.leftToRight = -1;
                    constraint.layout.leftToLeft = -1;
                    constraint.layout.leftMargin = -1;
                    constraint.layout.goneLeftMargin = Integer.MIN_VALUE;
                    return;
                case 2:
                    constraint.layout.rightToRight = -1;
                    constraint.layout.rightToLeft = -1;
                    constraint.layout.rightMargin = -1;
                    constraint.layout.goneRightMargin = Integer.MIN_VALUE;
                    return;
                case 3:
                    constraint.layout.topToBottom = -1;
                    constraint.layout.topToTop = -1;
                    constraint.layout.topMargin = 0;
                    constraint.layout.goneTopMargin = Integer.MIN_VALUE;
                    return;
                case 4:
                    constraint.layout.bottomToTop = -1;
                    constraint.layout.bottomToBottom = -1;
                    constraint.layout.bottomMargin = 0;
                    constraint.layout.goneBottomMargin = Integer.MIN_VALUE;
                    return;
                case 5:
                    constraint.layout.baselineToBaseline = -1;
                    constraint.layout.baselineToTop = -1;
                    constraint.layout.baselineToBottom = -1;
                    constraint.layout.baselineMargin = 0;
                    constraint.layout.goneBaselineMargin = Integer.MIN_VALUE;
                    return;
                case 6:
                    constraint.layout.startToEnd = -1;
                    constraint.layout.startToStart = -1;
                    constraint.layout.startMargin = 0;
                    constraint.layout.goneStartMargin = Integer.MIN_VALUE;
                    return;
                case 7:
                    constraint.layout.endToStart = -1;
                    constraint.layout.endToEnd = -1;
                    constraint.layout.endMargin = 0;
                    constraint.layout.goneEndMargin = Integer.MIN_VALUE;
                    return;
                case 8:
                    constraint.layout.circleAngle = -1.0f;
                    constraint.layout.circleRadius = -1;
                    constraint.layout.circleConstraint = -1;
                    return;
                default:
                    throw new IllegalArgumentException("unknown constraint");
            }
        }
    }

    public void clone(Context context, int constraintLayoutId) {
        clone((ConstraintLayout) LayoutInflater.from(context).inflate(constraintLayoutId, (ViewGroup) null));
    }

    public void clone(ConstraintLayout constraintLayout) {
        int childCount = constraintLayout.getChildCount();
        this.mConstraints.clear();
        int i = 0;
        while (i < childCount) {
            View childAt = constraintLayout.getChildAt(i);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) childAt.getLayoutParams();
            int id = childAt.getId();
            if (!this.mForceId || id != -1) {
                if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
                    this.mConstraints.put(Integer.valueOf(id), new Constraint());
                }
                Constraint constraint = this.mConstraints.get(Integer.valueOf(id));
                if (constraint != null) {
                    constraint.mCustomConstraints = ConstraintAttribute.extractAttributes(this.mSavedAttributes, childAt);
                    constraint.fillFrom(id, layoutParams);
                    constraint.propertySet.visibility = childAt.getVisibility();
                    if (Build.VERSION.SDK_INT >= 17) {
                        constraint.propertySet.alpha = childAt.getAlpha();
                        constraint.transform.rotation = childAt.getRotation();
                        constraint.transform.rotationX = childAt.getRotationX();
                        constraint.transform.rotationY = childAt.getRotationY();
                        constraint.transform.scaleX = childAt.getScaleX();
                        constraint.transform.scaleY = childAt.getScaleY();
                        float pivotX = childAt.getPivotX();
                        float pivotY = childAt.getPivotY();
                        if (!(((double) pivotX) == 0.0d && ((double) pivotY) == 0.0d)) {
                            constraint.transform.transformPivotX = pivotX;
                            constraint.transform.transformPivotY = pivotY;
                        }
                        constraint.transform.translationX = childAt.getTranslationX();
                        constraint.transform.translationY = childAt.getTranslationY();
                        if (Build.VERSION.SDK_INT >= 21) {
                            constraint.transform.translationZ = childAt.getTranslationZ();
                            if (constraint.transform.applyElevation) {
                                constraint.transform.elevation = childAt.getElevation();
                            }
                        }
                    }
                    if (childAt instanceof Barrier) {
                        Barrier barrier = (Barrier) childAt;
                        constraint.layout.mBarrierAllowsGoneWidgets = barrier.getAllowsGoneWidget();
                        constraint.layout.mReferenceIds = barrier.getReferencedIds();
                        constraint.layout.mBarrierDirection = barrier.getType();
                        constraint.layout.mBarrierMargin = barrier.getMargin();
                    }
                }
                i++;
            } else {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
        }
    }

    public void clone(ConstraintSet set) {
        this.mConstraints.clear();
        for (Integer next : set.mConstraints.keySet()) {
            Constraint constraint = set.mConstraints.get(next);
            if (constraint != null) {
                this.mConstraints.put(next, constraint.clone());
            }
        }
    }

    public void clone(Constraints constraints) {
        int childCount = constraints.getChildCount();
        this.mConstraints.clear();
        int i = 0;
        while (i < childCount) {
            View childAt = constraints.getChildAt(i);
            Constraints.LayoutParams layoutParams = (Constraints.LayoutParams) childAt.getLayoutParams();
            int id = childAt.getId();
            if (!this.mForceId || id != -1) {
                if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
                    this.mConstraints.put(Integer.valueOf(id), new Constraint());
                }
                Constraint constraint = this.mConstraints.get(Integer.valueOf(id));
                if (constraint != null) {
                    if (childAt instanceof ConstraintHelper) {
                        constraint.fillFromConstraints((ConstraintHelper) childAt, id, layoutParams);
                    }
                    constraint.fillFromConstraints(id, layoutParams);
                }
                i++;
            } else {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
        }
    }

    public void connect(int startID, int startSide, int endID, int endSide) {
        if (!this.mConstraints.containsKey(Integer.valueOf(startID))) {
            this.mConstraints.put(Integer.valueOf(startID), new Constraint());
        }
        Constraint constraint = this.mConstraints.get(Integer.valueOf(startID));
        if (constraint != null) {
            switch (startSide) {
                case 1:
                    if (endSide == 1) {
                        constraint.layout.leftToLeft = endID;
                        constraint.layout.leftToRight = -1;
                        return;
                    } else if (endSide == 2) {
                        constraint.layout.leftToRight = endID;
                        constraint.layout.leftToLeft = -1;
                        return;
                    } else {
                        throw new IllegalArgumentException("left to " + sideToString(endSide) + " undefined");
                    }
                case 2:
                    if (endSide == 1) {
                        constraint.layout.rightToLeft = endID;
                        constraint.layout.rightToRight = -1;
                        return;
                    } else if (endSide == 2) {
                        constraint.layout.rightToRight = endID;
                        constraint.layout.rightToLeft = -1;
                        return;
                    } else {
                        throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                    }
                case 3:
                    if (endSide == 3) {
                        constraint.layout.topToTop = endID;
                        constraint.layout.topToBottom = -1;
                        constraint.layout.baselineToBaseline = -1;
                        constraint.layout.baselineToTop = -1;
                        constraint.layout.baselineToBottom = -1;
                        return;
                    } else if (endSide == 4) {
                        constraint.layout.topToBottom = endID;
                        constraint.layout.topToTop = -1;
                        constraint.layout.baselineToBaseline = -1;
                        constraint.layout.baselineToTop = -1;
                        constraint.layout.baselineToBottom = -1;
                        return;
                    } else {
                        throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                    }
                case 4:
                    if (endSide == 4) {
                        constraint.layout.bottomToBottom = endID;
                        constraint.layout.bottomToTop = -1;
                        constraint.layout.baselineToBaseline = -1;
                        constraint.layout.baselineToTop = -1;
                        constraint.layout.baselineToBottom = -1;
                        return;
                    } else if (endSide == 3) {
                        constraint.layout.bottomToTop = endID;
                        constraint.layout.bottomToBottom = -1;
                        constraint.layout.baselineToBaseline = -1;
                        constraint.layout.baselineToTop = -1;
                        constraint.layout.baselineToBottom = -1;
                        return;
                    } else {
                        throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                    }
                case 5:
                    if (endSide == 5) {
                        constraint.layout.baselineToBaseline = endID;
                        constraint.layout.bottomToBottom = -1;
                        constraint.layout.bottomToTop = -1;
                        constraint.layout.topToTop = -1;
                        constraint.layout.topToBottom = -1;
                        return;
                    } else if (endSide == 3) {
                        constraint.layout.baselineToTop = endID;
                        constraint.layout.bottomToBottom = -1;
                        constraint.layout.bottomToTop = -1;
                        constraint.layout.topToTop = -1;
                        constraint.layout.topToBottom = -1;
                        return;
                    } else if (endSide == 4) {
                        constraint.layout.baselineToBottom = endID;
                        constraint.layout.bottomToBottom = -1;
                        constraint.layout.bottomToTop = -1;
                        constraint.layout.topToTop = -1;
                        constraint.layout.topToBottom = -1;
                        return;
                    } else {
                        throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                    }
                case 6:
                    if (endSide == 6) {
                        constraint.layout.startToStart = endID;
                        constraint.layout.startToEnd = -1;
                        return;
                    } else if (endSide == 7) {
                        constraint.layout.startToEnd = endID;
                        constraint.layout.startToStart = -1;
                        return;
                    } else {
                        throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                    }
                case 7:
                    if (endSide == 7) {
                        constraint.layout.endToEnd = endID;
                        constraint.layout.endToStart = -1;
                        return;
                    } else if (endSide == 6) {
                        constraint.layout.endToStart = endID;
                        constraint.layout.endToEnd = -1;
                        return;
                    } else {
                        throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                    }
                default:
                    throw new IllegalArgumentException(sideToString(startSide) + " to " + sideToString(endSide) + " unknown");
            }
        }
    }

    public void connect(int startID, int startSide, int endID, int endSide, int margin) {
        if (!this.mConstraints.containsKey(Integer.valueOf(startID))) {
            this.mConstraints.put(Integer.valueOf(startID), new Constraint());
        }
        Constraint constraint = this.mConstraints.get(Integer.valueOf(startID));
        if (constraint != null) {
            switch (startSide) {
                case 1:
                    if (endSide == 1) {
                        constraint.layout.leftToLeft = endID;
                        constraint.layout.leftToRight = -1;
                    } else if (endSide == 2) {
                        constraint.layout.leftToRight = endID;
                        constraint.layout.leftToLeft = -1;
                    } else {
                        throw new IllegalArgumentException("Left to " + sideToString(endSide) + " undefined");
                    }
                    constraint.layout.leftMargin = margin;
                    return;
                case 2:
                    if (endSide == 1) {
                        constraint.layout.rightToLeft = endID;
                        constraint.layout.rightToRight = -1;
                    } else if (endSide == 2) {
                        constraint.layout.rightToRight = endID;
                        constraint.layout.rightToLeft = -1;
                    } else {
                        throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                    }
                    constraint.layout.rightMargin = margin;
                    return;
                case 3:
                    if (endSide == 3) {
                        constraint.layout.topToTop = endID;
                        constraint.layout.topToBottom = -1;
                        constraint.layout.baselineToBaseline = -1;
                        constraint.layout.baselineToTop = -1;
                        constraint.layout.baselineToBottom = -1;
                    } else if (endSide == 4) {
                        constraint.layout.topToBottom = endID;
                        constraint.layout.topToTop = -1;
                        constraint.layout.baselineToBaseline = -1;
                        constraint.layout.baselineToTop = -1;
                        constraint.layout.baselineToBottom = -1;
                    } else {
                        throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                    }
                    constraint.layout.topMargin = margin;
                    return;
                case 4:
                    if (endSide == 4) {
                        constraint.layout.bottomToBottom = endID;
                        constraint.layout.bottomToTop = -1;
                        constraint.layout.baselineToBaseline = -1;
                        constraint.layout.baselineToTop = -1;
                        constraint.layout.baselineToBottom = -1;
                    } else if (endSide == 3) {
                        constraint.layout.bottomToTop = endID;
                        constraint.layout.bottomToBottom = -1;
                        constraint.layout.baselineToBaseline = -1;
                        constraint.layout.baselineToTop = -1;
                        constraint.layout.baselineToBottom = -1;
                    } else {
                        throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                    }
                    constraint.layout.bottomMargin = margin;
                    return;
                case 5:
                    if (endSide == 5) {
                        constraint.layout.baselineToBaseline = endID;
                        constraint.layout.bottomToBottom = -1;
                        constraint.layout.bottomToTop = -1;
                        constraint.layout.topToTop = -1;
                        constraint.layout.topToBottom = -1;
                        return;
                    } else if (endSide == 3) {
                        constraint.layout.baselineToTop = endID;
                        constraint.layout.bottomToBottom = -1;
                        constraint.layout.bottomToTop = -1;
                        constraint.layout.topToTop = -1;
                        constraint.layout.topToBottom = -1;
                        return;
                    } else if (endSide == 4) {
                        constraint.layout.baselineToBottom = endID;
                        constraint.layout.bottomToBottom = -1;
                        constraint.layout.bottomToTop = -1;
                        constraint.layout.topToTop = -1;
                        constraint.layout.topToBottom = -1;
                        return;
                    } else {
                        throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                    }
                case 6:
                    if (endSide == 6) {
                        constraint.layout.startToStart = endID;
                        constraint.layout.startToEnd = -1;
                    } else if (endSide == 7) {
                        constraint.layout.startToEnd = endID;
                        constraint.layout.startToStart = -1;
                    } else {
                        throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                    }
                    constraint.layout.startMargin = margin;
                    return;
                case 7:
                    if (endSide == 7) {
                        constraint.layout.endToEnd = endID;
                        constraint.layout.endToStart = -1;
                    } else if (endSide == 6) {
                        constraint.layout.endToStart = endID;
                        constraint.layout.endToEnd = -1;
                    } else {
                        throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                    }
                    constraint.layout.endMargin = margin;
                    return;
                default:
                    throw new IllegalArgumentException(sideToString(startSide) + " to " + sideToString(endSide) + " unknown");
            }
        }
    }

    public void constrainCircle(int viewId, int id, int radius, float angle) {
        Constraint constraint = get(viewId);
        constraint.layout.circleConstraint = id;
        constraint.layout.circleRadius = radius;
        constraint.layout.circleAngle = angle;
    }

    public void constrainDefaultHeight(int viewId, int height) {
        get(viewId).layout.heightDefault = height;
    }

    public void constrainDefaultWidth(int viewId, int width) {
        get(viewId).layout.widthDefault = width;
    }

    public void constrainHeight(int viewId, int height) {
        get(viewId).layout.mHeight = height;
    }

    public void constrainMaxHeight(int viewId, int height) {
        get(viewId).layout.heightMax = height;
    }

    public void constrainMaxWidth(int viewId, int width) {
        get(viewId).layout.widthMax = width;
    }

    public void constrainMinHeight(int viewId, int height) {
        get(viewId).layout.heightMin = height;
    }

    public void constrainMinWidth(int viewId, int width) {
        get(viewId).layout.widthMin = width;
    }

    public void constrainPercentHeight(int viewId, float percent) {
        get(viewId).layout.heightPercent = percent;
    }

    public void constrainPercentWidth(int viewId, float percent) {
        get(viewId).layout.widthPercent = percent;
    }

    public void constrainWidth(int viewId, int width) {
        get(viewId).layout.mWidth = width;
    }

    public void constrainedHeight(int viewId, boolean constrained) {
        get(viewId).layout.constrainedHeight = constrained;
    }

    public void constrainedWidth(int viewId, boolean constrained) {
        get(viewId).layout.constrainedWidth = constrained;
    }

    public void create(int guidelineID, int orientation) {
        Constraint constraint = get(guidelineID);
        constraint.layout.mIsGuideline = true;
        constraint.layout.orientation = orientation;
    }

    public void createBarrier(int id, int direction, int margin, int... referenced) {
        Constraint constraint = get(id);
        constraint.layout.mHelperType = 1;
        constraint.layout.mBarrierDirection = direction;
        constraint.layout.mBarrierMargin = margin;
        constraint.layout.mIsGuideline = false;
        constraint.layout.mReferenceIds = referenced;
    }

    public void createHorizontalChain(int leftId, int leftSide, int rightId, int rightSide, int[] chainIds, float[] weights, int style) {
        createHorizontalChain(leftId, leftSide, rightId, rightSide, chainIds, weights, style, 1, 2);
    }

    public void createHorizontalChainRtl(int startId, int startSide, int endId, int endSide, int[] chainIds, float[] weights, int style) {
        createHorizontalChain(startId, startSide, endId, endSide, chainIds, weights, style, 6, 7);
    }

    public void createVerticalChain(int topId, int topSide, int bottomId, int bottomSide, int[] chainIds, float[] weights, int style) {
        int[] iArr = chainIds;
        float[] fArr = weights;
        if (iArr.length < 2) {
            int i = style;
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        } else if (fArr == null || fArr.length == iArr.length) {
            if (fArr != null) {
                get(iArr[0]).layout.verticalWeight = fArr[0];
            }
            get(iArr[0]).layout.verticalChainStyle = style;
            connect(iArr[0], 3, topId, topSide, 0);
            for (int i2 = 1; i2 < iArr.length; i2++) {
                int i3 = iArr[i2];
                connect(iArr[i2], 3, iArr[i2 - 1], 4, 0);
                connect(iArr[i2 - 1], 4, iArr[i2], 3, 0);
                if (fArr != null) {
                    get(iArr[i2]).layout.verticalWeight = fArr[i2];
                }
            }
            connect(iArr[iArr.length - 1], 4, bottomId, bottomSide, 0);
        } else {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        }
    }

    public void dump(MotionScene scene, int... ids) {
        HashSet hashSet;
        Set<Integer> keySet = this.mConstraints.keySet();
        if (ids.length != 0) {
            hashSet = new HashSet();
            for (int valueOf : ids) {
                hashSet.add(Integer.valueOf(valueOf));
            }
        } else {
            hashSet = new HashSet(keySet);
        }
        System.out.println(hashSet.size() + " constraints");
        StringBuilder sb = new StringBuilder();
        for (Integer num : (Integer[]) hashSet.toArray(new Integer[0])) {
            Constraint constraint = this.mConstraints.get(num);
            if (constraint != null) {
                sb.append("<Constraint id=");
                sb.append(num);
                sb.append(" \n");
                constraint.layout.dump(scene, sb);
                sb.append("/>\n");
            }
        }
        System.out.println(sb.toString());
    }

    public boolean getApplyElevation(int viewId) {
        return get(viewId).transform.applyElevation;
    }

    public Constraint getConstraint(int id) {
        if (this.mConstraints.containsKey(Integer.valueOf(id))) {
            return this.mConstraints.get(Integer.valueOf(id));
        }
        return null;
    }

    public HashMap<String, ConstraintAttribute> getCustomAttributeSet() {
        return this.mSavedAttributes;
    }

    public int getHeight(int viewId) {
        return get(viewId).layout.mHeight;
    }

    public int[] getKnownIds() {
        Integer[] numArr = (Integer[]) this.mConstraints.keySet().toArray(new Integer[0]);
        int[] iArr = new int[numArr.length];
        for (int i = 0; i < iArr.length; i++) {
            iArr[i] = numArr[i].intValue();
        }
        return iArr;
    }

    public Constraint getParameters(int mId) {
        return get(mId);
    }

    public int[] getReferencedIds(int id) {
        Constraint constraint = get(id);
        return constraint.layout.mReferenceIds == null ? new int[0] : Arrays.copyOf(constraint.layout.mReferenceIds, constraint.layout.mReferenceIds.length);
    }

    public int getVisibility(int viewId) {
        return get(viewId).propertySet.visibility;
    }

    public int getVisibilityMode(int viewId) {
        return get(viewId).propertySet.mVisibilityMode;
    }

    public int getWidth(int viewId) {
        return get(viewId).layout.mWidth;
    }

    public boolean isForceId() {
        return this.mForceId;
    }

    public void load(Context context, int resourceId) {
        XmlResourceParser xml = context.getResources().getXml(resourceId);
        try {
            for (int eventType = xml.getEventType(); eventType != 1; eventType = xml.next()) {
                switch (eventType) {
                    case 0:
                        String name = xml.getName();
                        break;
                    case 2:
                        String name2 = xml.getName();
                        Constraint fillFromAttributeList = fillFromAttributeList(context, Xml.asAttributeSet(xml), false);
                        if (name2.equalsIgnoreCase("Guideline")) {
                            fillFromAttributeList.layout.mIsGuideline = true;
                        }
                        this.mConstraints.put(Integer.valueOf(fillFromAttributeList.mViewId), fillFromAttributeList);
                        break;
                    case 3:
                        break;
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void load(android.content.Context r11, org.xmlpull.v1.XmlPullParser r12) {
        /*
            r10 = this;
            r0 = 0
            r1 = 0
            int r2 = r12.getEventType()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
        L_0x0006:
            r3 = 1
            if (r2 == r3) goto L_0x01e2
            r4 = 3
            r5 = 2
            r6 = -1
            r7 = 0
            switch(r2) {
                case 0: goto L_0x01d6;
                case 1: goto L_0x0010;
                case 2: goto L_0x0062;
                case 3: goto L_0x0012;
                default: goto L_0x0010;
            }     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
        L_0x0010:
            goto L_0x01db
        L_0x0012:
            java.lang.String r8 = r12.getName()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r0 = r8
            java.util.Locale r8 = java.util.Locale.ROOT     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.String r8 = r0.toLowerCase(r8)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            int r9 = r8.hashCode()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            switch(r9) {
                case -2075718416: goto L_0x0042;
                case -190376483: goto L_0x0039;
                case 426575017: goto L_0x002f;
                case 2146106725: goto L_0x0025;
                default: goto L_0x0024;
            }     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
        L_0x0024:
            goto L_0x004c
        L_0x0025:
            java.lang.String r3 = "constraintset"
            boolean r3 = r8.equals(r3)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            if (r3 == 0) goto L_0x0024
            r3 = r7
            goto L_0x004d
        L_0x002f:
            java.lang.String r3 = "constraintoverride"
            boolean r3 = r8.equals(r3)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            if (r3 == 0) goto L_0x0024
            r3 = r5
            goto L_0x004d
        L_0x0039:
            java.lang.String r4 = "constraint"
            boolean r4 = r8.equals(r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            if (r4 == 0) goto L_0x0024
            goto L_0x004d
        L_0x0042:
            java.lang.String r3 = "guideline"
            boolean r3 = r8.equals(r3)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            if (r3 == 0) goto L_0x0024
            r3 = r4
            goto L_0x004d
        L_0x004c:
            r3 = r6
        L_0x004d:
            switch(r3) {
                case 0: goto L_0x005e;
                case 1: goto L_0x0051;
                case 2: goto L_0x0051;
                case 3: goto L_0x0051;
                default: goto L_0x0050;
            }     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
        L_0x0050:
            goto L_0x005f
        L_0x0051:
            java.util.HashMap<java.lang.Integer, androidx.constraintlayout.widget.ConstraintSet$Constraint> r3 = r10.mConstraints     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            int r4 = r1.mViewId     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r3.put(r4, r1)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r1 = 0
            goto L_0x005f
        L_0x005e:
            return
        L_0x005f:
            r0 = 0
            goto L_0x01db
        L_0x0062:
            java.lang.String r8 = r12.getName()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r0 = r8
            int r8 = r0.hashCode()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            switch(r8) {
                case -2025855158: goto L_0x00cc;
                case -1984451626: goto L_0x00c2;
                case -1962203927: goto L_0x00b8;
                case -1269513683: goto L_0x00ae;
                case -1238332596: goto L_0x00a4;
                case -71750448: goto L_0x009a;
                case 366511058: goto L_0x008f;
                case 1331510167: goto L_0x0086;
                case 1791837707: goto L_0x007b;
                case 1803088381: goto L_0x0070;
                default: goto L_0x006e;
            }     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
        L_0x006e:
            goto L_0x00d6
        L_0x0070:
            java.lang.String r4 = "Constraint"
            boolean r4 = r0.equals(r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            if (r4 == 0) goto L_0x006e
            r4 = r7
            goto L_0x00d7
        L_0x007b:
            java.lang.String r4 = "CustomAttribute"
            boolean r4 = r0.equals(r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            if (r4 == 0) goto L_0x006e
            r4 = 8
            goto L_0x00d7
        L_0x0086:
            java.lang.String r5 = "Barrier"
            boolean r5 = r0.equals(r5)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            if (r5 == 0) goto L_0x006e
            goto L_0x00d7
        L_0x008f:
            java.lang.String r4 = "CustomMethod"
            boolean r4 = r0.equals(r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            if (r4 == 0) goto L_0x006e
            r4 = 9
            goto L_0x00d7
        L_0x009a:
            java.lang.String r4 = "Guideline"
            boolean r4 = r0.equals(r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            if (r4 == 0) goto L_0x006e
            r4 = r5
            goto L_0x00d7
        L_0x00a4:
            java.lang.String r4 = "Transform"
            boolean r4 = r0.equals(r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            if (r4 == 0) goto L_0x006e
            r4 = 5
            goto L_0x00d7
        L_0x00ae:
            java.lang.String r4 = "PropertySet"
            boolean r4 = r0.equals(r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            if (r4 == 0) goto L_0x006e
            r4 = 4
            goto L_0x00d7
        L_0x00b8:
            java.lang.String r4 = "ConstraintOverride"
            boolean r4 = r0.equals(r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            if (r4 == 0) goto L_0x006e
            r4 = r3
            goto L_0x00d7
        L_0x00c2:
            java.lang.String r4 = "Motion"
            boolean r4 = r0.equals(r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            if (r4 == 0) goto L_0x006e
            r4 = 7
            goto L_0x00d7
        L_0x00cc:
            java.lang.String r4 = "Layout"
            boolean r4 = r0.equals(r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            if (r4 == 0) goto L_0x006e
            r4 = 6
            goto L_0x00d7
        L_0x00d6:
            r4 = r6
        L_0x00d7:
            java.lang.String r5 = "XML parser error must be within a Constraint "
            switch(r4) {
                case 0: goto L_0x01cb;
                case 1: goto L_0x01c1;
                case 2: goto L_0x01af;
                case 3: goto L_0x01a1;
                case 4: goto L_0x017a;
                case 5: goto L_0x0152;
                case 6: goto L_0x012a;
                case 7: goto L_0x0102;
                case 8: goto L_0x00de;
                case 9: goto L_0x00de;
                default: goto L_0x00dc;
            }
        L_0x00dc:
            goto L_0x01d5
        L_0x00de:
            if (r1 == 0) goto L_0x00e7
            java.util.HashMap<java.lang.String, androidx.constraintlayout.widget.ConstraintAttribute> r3 = r1.mCustomConstraints     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            androidx.constraintlayout.widget.ConstraintAttribute.parse(r11, r12, r3)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            goto L_0x01d5
        L_0x00e7:
            java.lang.RuntimeException r3 = new java.lang.RuntimeException     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r4.<init>()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            int r5 = r12.getLineNumber()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.String r4 = r4.toString()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r3.<init>(r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            throw r3     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
        L_0x0102:
            if (r1 == 0) goto L_0x010f
            androidx.constraintlayout.widget.ConstraintSet$Motion r3 = r1.motion     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            android.util.AttributeSet r4 = android.util.Xml.asAttributeSet(r12)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r3.fillFromAttributeList(r11, r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            goto L_0x01d5
        L_0x010f:
            java.lang.RuntimeException r3 = new java.lang.RuntimeException     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r4.<init>()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            int r5 = r12.getLineNumber()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.String r4 = r4.toString()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r3.<init>(r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            throw r3     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
        L_0x012a:
            if (r1 == 0) goto L_0x0137
            androidx.constraintlayout.widget.ConstraintSet$Layout r3 = r1.layout     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            android.util.AttributeSet r4 = android.util.Xml.asAttributeSet(r12)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r3.fillFromAttributeList(r11, r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            goto L_0x01d5
        L_0x0137:
            java.lang.RuntimeException r3 = new java.lang.RuntimeException     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r4.<init>()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            int r5 = r12.getLineNumber()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.String r4 = r4.toString()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r3.<init>(r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            throw r3     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
        L_0x0152:
            if (r1 == 0) goto L_0x015f
            androidx.constraintlayout.widget.ConstraintSet$Transform r3 = r1.transform     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            android.util.AttributeSet r4 = android.util.Xml.asAttributeSet(r12)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r3.fillFromAttributeList(r11, r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            goto L_0x01d5
        L_0x015f:
            java.lang.RuntimeException r3 = new java.lang.RuntimeException     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r4.<init>()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            int r5 = r12.getLineNumber()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.String r4 = r4.toString()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r3.<init>(r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            throw r3     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
        L_0x017a:
            if (r1 == 0) goto L_0x0186
            androidx.constraintlayout.widget.ConstraintSet$PropertySet r3 = r1.propertySet     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            android.util.AttributeSet r4 = android.util.Xml.asAttributeSet(r12)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r3.fillFromAttributeList(r11, r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            goto L_0x01d5
        L_0x0186:
            java.lang.RuntimeException r3 = new java.lang.RuntimeException     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r4.<init>()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            int r5 = r12.getLineNumber()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            java.lang.String r4 = r4.toString()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r3.<init>(r4)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            throw r3     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
        L_0x01a1:
            android.util.AttributeSet r4 = android.util.Xml.asAttributeSet(r12)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            androidx.constraintlayout.widget.ConstraintSet$Constraint r4 = r10.fillFromAttributeList(r11, r4, r7)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r1 = r4
            androidx.constraintlayout.widget.ConstraintSet$Layout r4 = r1.layout     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r4.mHelperType = r3     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            goto L_0x01d5
        L_0x01af:
            android.util.AttributeSet r4 = android.util.Xml.asAttributeSet(r12)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            androidx.constraintlayout.widget.ConstraintSet$Constraint r4 = r10.fillFromAttributeList(r11, r4, r7)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r1 = r4
            androidx.constraintlayout.widget.ConstraintSet$Layout r4 = r1.layout     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r4.mIsGuideline = r3     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            androidx.constraintlayout.widget.ConstraintSet$Layout r4 = r1.layout     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r4.mApply = r3     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            goto L_0x01d5
        L_0x01c1:
            android.util.AttributeSet r4 = android.util.Xml.asAttributeSet(r12)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            androidx.constraintlayout.widget.ConstraintSet$Constraint r3 = r10.fillFromAttributeList(r11, r4, r3)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r1 = r3
            goto L_0x01d5
        L_0x01cb:
            android.util.AttributeSet r3 = android.util.Xml.asAttributeSet(r12)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            androidx.constraintlayout.widget.ConstraintSet$Constraint r3 = r10.fillFromAttributeList(r11, r3, r7)     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r1 = r3
        L_0x01d5:
            goto L_0x01db
        L_0x01d6:
            java.lang.String r3 = r12.getName()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
        L_0x01db:
            int r3 = r12.next()     // Catch:{ XmlPullParserException -> 0x01e8, IOException -> 0x01e3 }
            r2 = r3
            goto L_0x0006
        L_0x01e2:
            goto L_0x01ec
        L_0x01e3:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x01ed
        L_0x01e8:
            r1 = move-exception
            r1.printStackTrace()
        L_0x01ec:
        L_0x01ed:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.ConstraintSet.load(android.content.Context, org.xmlpull.v1.XmlPullParser):void");
    }

    public void parseColorAttributes(Constraint set, String attributes) {
        String[] split = attributes.split(",");
        for (int i = 0; i < split.length; i++) {
            String[] split2 = split[i].split("=");
            if (split2.length != 2) {
                Log.w(TAG, " Unable to parse " + split[i]);
            } else {
                set.setColorValue(split2[0], Color.parseColor(split2[1]));
            }
        }
    }

    public void parseFloatAttributes(Constraint set, String attributes) {
        String[] split = attributes.split(",");
        for (int i = 0; i < split.length; i++) {
            String[] split2 = split[i].split("=");
            if (split2.length != 2) {
                Log.w(TAG, " Unable to parse " + split[i]);
            } else {
                set.setFloatValue(split2[0], Float.parseFloat(split2[1]));
            }
        }
    }

    public void parseIntAttributes(Constraint set, String attributes) {
        String[] split = attributes.split(",");
        for (int i = 0; i < split.length; i++) {
            String[] split2 = split[i].split("=");
            if (split2.length != 2) {
                Log.w(TAG, " Unable to parse " + split[i]);
            } else {
                set.setFloatValue(split2[0], (float) Integer.decode(split2[1]).intValue());
            }
        }
    }

    public void parseStringAttributes(Constraint set, String attributes) {
        String[] splitString = splitString(attributes);
        for (int i = 0; i < splitString.length; i++) {
            String[] split = splitString[i].split("=");
            Log.w(TAG, " Unable to parse " + splitString[i]);
            set.setStringValue(split[0], split[1]);
        }
    }

    public void readFallback(ConstraintLayout constraintLayout) {
        int childCount = constraintLayout.getChildCount();
        int i = 0;
        while (i < childCount) {
            View childAt = constraintLayout.getChildAt(i);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) childAt.getLayoutParams();
            int id = childAt.getId();
            if (!this.mForceId || id != -1) {
                if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
                    this.mConstraints.put(Integer.valueOf(id), new Constraint());
                }
                Constraint constraint = this.mConstraints.get(Integer.valueOf(id));
                if (constraint != null) {
                    if (!constraint.layout.mApply) {
                        constraint.fillFrom(id, layoutParams);
                        if (childAt instanceof ConstraintHelper) {
                            constraint.layout.mReferenceIds = ((ConstraintHelper) childAt).getReferencedIds();
                            if (childAt instanceof Barrier) {
                                Barrier barrier = (Barrier) childAt;
                                constraint.layout.mBarrierAllowsGoneWidgets = barrier.getAllowsGoneWidget();
                                constraint.layout.mBarrierDirection = barrier.getType();
                                constraint.layout.mBarrierMargin = barrier.getMargin();
                            }
                        }
                        constraint.layout.mApply = true;
                    }
                    if (!constraint.propertySet.mApply) {
                        constraint.propertySet.visibility = childAt.getVisibility();
                        constraint.propertySet.alpha = childAt.getAlpha();
                        constraint.propertySet.mApply = true;
                    }
                    if (Build.VERSION.SDK_INT >= 17 && !constraint.transform.mApply) {
                        constraint.transform.mApply = true;
                        constraint.transform.rotation = childAt.getRotation();
                        constraint.transform.rotationX = childAt.getRotationX();
                        constraint.transform.rotationY = childAt.getRotationY();
                        constraint.transform.scaleX = childAt.getScaleX();
                        constraint.transform.scaleY = childAt.getScaleY();
                        float pivotX = childAt.getPivotX();
                        float pivotY = childAt.getPivotY();
                        if (!(((double) pivotX) == 0.0d && ((double) pivotY) == 0.0d)) {
                            constraint.transform.transformPivotX = pivotX;
                            constraint.transform.transformPivotY = pivotY;
                        }
                        constraint.transform.translationX = childAt.getTranslationX();
                        constraint.transform.translationY = childAt.getTranslationY();
                        if (Build.VERSION.SDK_INT >= 21) {
                            constraint.transform.translationZ = childAt.getTranslationZ();
                            if (constraint.transform.applyElevation) {
                                constraint.transform.elevation = childAt.getElevation();
                            }
                        }
                    }
                }
                i++;
            } else {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
        }
    }

    public void readFallback(ConstraintSet set) {
        for (Integer next : set.mConstraints.keySet()) {
            int intValue = next.intValue();
            Constraint constraint = set.mConstraints.get(next);
            if (!this.mConstraints.containsKey(Integer.valueOf(intValue))) {
                this.mConstraints.put(Integer.valueOf(intValue), new Constraint());
            }
            Constraint constraint2 = this.mConstraints.get(Integer.valueOf(intValue));
            if (constraint2 != null) {
                if (!constraint2.layout.mApply) {
                    constraint2.layout.copyFrom(constraint.layout);
                }
                if (!constraint2.propertySet.mApply) {
                    constraint2.propertySet.copyFrom(constraint.propertySet);
                }
                if (!constraint2.transform.mApply) {
                    constraint2.transform.copyFrom(constraint.transform);
                }
                if (!constraint2.motion.mApply) {
                    constraint2.motion.copyFrom(constraint.motion);
                }
                for (String next2 : constraint.mCustomConstraints.keySet()) {
                    if (!constraint2.mCustomConstraints.containsKey(next2)) {
                        constraint2.mCustomConstraints.put(next2, constraint.mCustomConstraints.get(next2));
                    }
                }
            }
        }
    }

    public void removeAttribute(String attributeName) {
        this.mSavedAttributes.remove(attributeName);
    }

    public void removeFromHorizontalChain(int viewId) {
        Constraint constraint;
        if (this.mConstraints.containsKey(Integer.valueOf(viewId)) && (constraint = this.mConstraints.get(Integer.valueOf(viewId))) != null) {
            int i = constraint.layout.leftToRight;
            int i2 = constraint.layout.rightToLeft;
            if (i == -1 && i2 == -1) {
                int i3 = constraint.layout.startToEnd;
                int i4 = constraint.layout.endToStart;
                if (!(i3 == -1 && i4 == -1)) {
                    if (i3 != -1 && i4 != -1) {
                        connect(i3, 7, i4, 6, 0);
                        connect(i4, 6, i, 7, 0);
                    } else if (i4 != -1) {
                        if (constraint.layout.rightToRight != -1) {
                            connect(i, 7, constraint.layout.rightToRight, 7, 0);
                        } else if (constraint.layout.leftToLeft != -1) {
                            connect(i4, 6, constraint.layout.leftToLeft, 6, 0);
                        }
                    }
                }
                clear(viewId, 6);
                clear(viewId, 7);
                return;
            }
            if (i != -1 && i2 != -1) {
                connect(i, 2, i2, 1, 0);
                connect(i2, 1, i, 2, 0);
            } else if (constraint.layout.rightToRight != -1) {
                connect(i, 2, constraint.layout.rightToRight, 2, 0);
            } else if (constraint.layout.leftToLeft != -1) {
                connect(i2, 1, constraint.layout.leftToLeft, 1, 0);
            }
            clear(viewId, 1);
            clear(viewId, 2);
        }
    }

    public void removeFromVerticalChain(int viewId) {
        if (this.mConstraints.containsKey(Integer.valueOf(viewId))) {
            Constraint constraint = this.mConstraints.get(Integer.valueOf(viewId));
            if (constraint != null) {
                int i = constraint.layout.topToBottom;
                int i2 = constraint.layout.bottomToTop;
                if (!(i == -1 && i2 == -1)) {
                    if (i != -1 && i2 != -1) {
                        connect(i, 4, i2, 3, 0);
                        connect(i2, 3, i, 4, 0);
                    } else if (constraint.layout.bottomToBottom != -1) {
                        connect(i, 4, constraint.layout.bottomToBottom, 4, 0);
                    } else if (constraint.layout.topToTop != -1) {
                        connect(i2, 3, constraint.layout.topToTop, 3, 0);
                    }
                }
            } else {
                return;
            }
        }
        clear(viewId, 3);
        clear(viewId, 4);
    }

    public void setAlpha(int viewId, float alpha) {
        get(viewId).propertySet.alpha = alpha;
    }

    public void setApplyElevation(int viewId, boolean apply) {
        if (Build.VERSION.SDK_INT >= 21) {
            get(viewId).transform.applyElevation = apply;
        }
    }

    public void setBarrierType(int id, int type) {
        get(id).layout.mHelperType = type;
    }

    public void setColorValue(int viewId, String attributeName, int value) {
        get(viewId).setColorValue(attributeName, value);
    }

    public void setDimensionRatio(int viewId, String ratio) {
        get(viewId).layout.dimensionRatio = ratio;
    }

    public void setEditorAbsoluteX(int viewId, int position) {
        get(viewId).layout.editorAbsoluteX = position;
    }

    public void setEditorAbsoluteY(int viewId, int position) {
        get(viewId).layout.editorAbsoluteY = position;
    }

    public void setElevation(int viewId, float elevation) {
        if (Build.VERSION.SDK_INT >= 21) {
            get(viewId).transform.elevation = elevation;
            get(viewId).transform.applyElevation = true;
        }
    }

    public void setFloatValue(int viewId, String attributeName, float value) {
        get(viewId).setFloatValue(attributeName, value);
    }

    public void setForceId(boolean forceId) {
        this.mForceId = forceId;
    }

    public void setGoneMargin(int viewId, int anchor, int value) {
        Constraint constraint = get(viewId);
        switch (anchor) {
            case 1:
                constraint.layout.goneLeftMargin = value;
                return;
            case 2:
                constraint.layout.goneRightMargin = value;
                return;
            case 3:
                constraint.layout.goneTopMargin = value;
                return;
            case 4:
                constraint.layout.goneBottomMargin = value;
                return;
            case 5:
                constraint.layout.goneBaselineMargin = value;
                return;
            case 6:
                constraint.layout.goneStartMargin = value;
                return;
            case 7:
                constraint.layout.goneEndMargin = value;
                return;
            default:
                throw new IllegalArgumentException("unknown constraint");
        }
    }

    public void setGuidelineBegin(int guidelineID, int margin) {
        get(guidelineID).layout.guideBegin = margin;
        get(guidelineID).layout.guideEnd = -1;
        get(guidelineID).layout.guidePercent = -1.0f;
    }

    public void setGuidelineEnd(int guidelineID, int margin) {
        get(guidelineID).layout.guideEnd = margin;
        get(guidelineID).layout.guideBegin = -1;
        get(guidelineID).layout.guidePercent = -1.0f;
    }

    public void setGuidelinePercent(int guidelineID, float ratio) {
        get(guidelineID).layout.guidePercent = ratio;
        get(guidelineID).layout.guideEnd = -1;
        get(guidelineID).layout.guideBegin = -1;
    }

    public void setHorizontalBias(int viewId, float bias) {
        get(viewId).layout.horizontalBias = bias;
    }

    public void setHorizontalChainStyle(int viewId, int chainStyle) {
        get(viewId).layout.horizontalChainStyle = chainStyle;
    }

    public void setHorizontalWeight(int viewId, float weight) {
        get(viewId).layout.horizontalWeight = weight;
    }

    public void setIntValue(int viewId, String attributeName, int value) {
        get(viewId).setIntValue(attributeName, value);
    }

    public void setLayoutWrapBehavior(int viewId, int behavior) {
        if (behavior >= 0 && behavior <= 3) {
            get(viewId).layout.mWrapBehavior = behavior;
        }
    }

    public void setMargin(int viewId, int anchor, int value) {
        Constraint constraint = get(viewId);
        switch (anchor) {
            case 1:
                constraint.layout.leftMargin = value;
                return;
            case 2:
                constraint.layout.rightMargin = value;
                return;
            case 3:
                constraint.layout.topMargin = value;
                return;
            case 4:
                constraint.layout.bottomMargin = value;
                return;
            case 5:
                constraint.layout.baselineMargin = value;
                return;
            case 6:
                constraint.layout.startMargin = value;
                return;
            case 7:
                constraint.layout.endMargin = value;
                return;
            default:
                throw new IllegalArgumentException("unknown constraint");
        }
    }

    public void setReferencedIds(int id, int... referenced) {
        get(id).layout.mReferenceIds = referenced;
    }

    public void setRotation(int viewId, float rotation) {
        get(viewId).transform.rotation = rotation;
    }

    public void setRotationX(int viewId, float rotationX) {
        get(viewId).transform.rotationX = rotationX;
    }

    public void setRotationY(int viewId, float rotationY) {
        get(viewId).transform.rotationY = rotationY;
    }

    public void setScaleX(int viewId, float scaleX) {
        get(viewId).transform.scaleX = scaleX;
    }

    public void setScaleY(int viewId, float scaleY) {
        get(viewId).transform.scaleY = scaleY;
    }

    public void setStringValue(int viewId, String attributeName, String value) {
        get(viewId).setStringValue(attributeName, value);
    }

    public void setTransformPivot(int viewId, float transformPivotX, float transformPivotY) {
        Constraint constraint = get(viewId);
        constraint.transform.transformPivotY = transformPivotY;
        constraint.transform.transformPivotX = transformPivotX;
    }

    public void setTransformPivotX(int viewId, float transformPivotX) {
        get(viewId).transform.transformPivotX = transformPivotX;
    }

    public void setTransformPivotY(int viewId, float transformPivotY) {
        get(viewId).transform.transformPivotY = transformPivotY;
    }

    public void setTranslation(int viewId, float translationX, float translationY) {
        Constraint constraint = get(viewId);
        constraint.transform.translationX = translationX;
        constraint.transform.translationY = translationY;
    }

    public void setTranslationX(int viewId, float translationX) {
        get(viewId).transform.translationX = translationX;
    }

    public void setTranslationY(int viewId, float translationY) {
        get(viewId).transform.translationY = translationY;
    }

    public void setTranslationZ(int viewId, float translationZ) {
        if (Build.VERSION.SDK_INT >= 21) {
            get(viewId).transform.translationZ = translationZ;
        }
    }

    public void setValidateOnParse(boolean validate) {
        this.mValidate = validate;
    }

    public void setVerticalBias(int viewId, float bias) {
        get(viewId).layout.verticalBias = bias;
    }

    public void setVerticalChainStyle(int viewId, int chainStyle) {
        get(viewId).layout.verticalChainStyle = chainStyle;
    }

    public void setVerticalWeight(int viewId, float weight) {
        get(viewId).layout.verticalWeight = weight;
    }

    public void setVisibility(int viewId, int visibility) {
        get(viewId).propertySet.visibility = visibility;
    }

    public void setVisibilityMode(int viewId, int visibilityMode) {
        get(viewId).propertySet.mVisibilityMode = visibilityMode;
    }

    public void writeState(Writer writer, ConstraintLayout layout, int flags) throws IOException {
        writer.write("\n---------------------------------------------\n");
        if ((flags & 1) == 1) {
            new WriteXmlEngine(writer, layout, flags).writeLayout();
        } else {
            new WriteJsonEngine(writer, layout, flags).writeLayout();
        }
        writer.write("\n---------------------------------------------\n");
    }

    private void populateConstraint(Context ctx, Constraint c, TypedArray a, boolean override) {
        Constraint constraint = c;
        TypedArray typedArray = a;
        if (override) {
            populateOverride(ctx, c, a);
            return;
        }
        int indexCount = a.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            if (!(index == R.styleable.Constraint_android_id || R.styleable.Constraint_android_layout_marginStart == index || R.styleable.Constraint_android_layout_marginEnd == index)) {
                constraint.motion.mApply = true;
                constraint.layout.mApply = true;
                constraint.propertySet.mApply = true;
                constraint.transform.mApply = true;
            }
            switch (mapToConstant.get(index)) {
                case 1:
                    constraint.layout.baselineToBaseline = lookupID(typedArray, index, constraint.layout.baselineToBaseline);
                    break;
                case 2:
                    constraint.layout.bottomMargin = typedArray.getDimensionPixelSize(index, constraint.layout.bottomMargin);
                    break;
                case 3:
                    constraint.layout.bottomToBottom = lookupID(typedArray, index, constraint.layout.bottomToBottom);
                    break;
                case 4:
                    constraint.layout.bottomToTop = lookupID(typedArray, index, constraint.layout.bottomToTop);
                    break;
                case 5:
                    constraint.layout.dimensionRatio = typedArray.getString(index);
                    break;
                case 6:
                    constraint.layout.editorAbsoluteX = typedArray.getDimensionPixelOffset(index, constraint.layout.editorAbsoluteX);
                    break;
                case 7:
                    constraint.layout.editorAbsoluteY = typedArray.getDimensionPixelOffset(index, constraint.layout.editorAbsoluteY);
                    break;
                case 8:
                    if (Build.VERSION.SDK_INT < 17) {
                        break;
                    } else {
                        constraint.layout.endMargin = typedArray.getDimensionPixelSize(index, constraint.layout.endMargin);
                        break;
                    }
                case 9:
                    constraint.layout.endToEnd = lookupID(typedArray, index, constraint.layout.endToEnd);
                    break;
                case 10:
                    constraint.layout.endToStart = lookupID(typedArray, index, constraint.layout.endToStart);
                    break;
                case 11:
                    constraint.layout.goneBottomMargin = typedArray.getDimensionPixelSize(index, constraint.layout.goneBottomMargin);
                    break;
                case 12:
                    constraint.layout.goneEndMargin = typedArray.getDimensionPixelSize(index, constraint.layout.goneEndMargin);
                    break;
                case 13:
                    constraint.layout.goneLeftMargin = typedArray.getDimensionPixelSize(index, constraint.layout.goneLeftMargin);
                    break;
                case 14:
                    constraint.layout.goneRightMargin = typedArray.getDimensionPixelSize(index, constraint.layout.goneRightMargin);
                    break;
                case 15:
                    constraint.layout.goneStartMargin = typedArray.getDimensionPixelSize(index, constraint.layout.goneStartMargin);
                    break;
                case 16:
                    constraint.layout.goneTopMargin = typedArray.getDimensionPixelSize(index, constraint.layout.goneTopMargin);
                    break;
                case 17:
                    constraint.layout.guideBegin = typedArray.getDimensionPixelOffset(index, constraint.layout.guideBegin);
                    break;
                case 18:
                    constraint.layout.guideEnd = typedArray.getDimensionPixelOffset(index, constraint.layout.guideEnd);
                    break;
                case 19:
                    constraint.layout.guidePercent = typedArray.getFloat(index, constraint.layout.guidePercent);
                    break;
                case 20:
                    constraint.layout.horizontalBias = typedArray.getFloat(index, constraint.layout.horizontalBias);
                    break;
                case 21:
                    constraint.layout.mHeight = typedArray.getLayoutDimension(index, constraint.layout.mHeight);
                    break;
                case 22:
                    constraint.propertySet.visibility = typedArray.getInt(index, constraint.propertySet.visibility);
                    constraint.propertySet.visibility = VISIBILITY_FLAGS[constraint.propertySet.visibility];
                    break;
                case 23:
                    constraint.layout.mWidth = typedArray.getLayoutDimension(index, constraint.layout.mWidth);
                    break;
                case 24:
                    constraint.layout.leftMargin = typedArray.getDimensionPixelSize(index, constraint.layout.leftMargin);
                    break;
                case 25:
                    constraint.layout.leftToLeft = lookupID(typedArray, index, constraint.layout.leftToLeft);
                    break;
                case 26:
                    constraint.layout.leftToRight = lookupID(typedArray, index, constraint.layout.leftToRight);
                    break;
                case 27:
                    constraint.layout.orientation = typedArray.getInt(index, constraint.layout.orientation);
                    break;
                case 28:
                    constraint.layout.rightMargin = typedArray.getDimensionPixelSize(index, constraint.layout.rightMargin);
                    break;
                case 29:
                    constraint.layout.rightToLeft = lookupID(typedArray, index, constraint.layout.rightToLeft);
                    break;
                case 30:
                    constraint.layout.rightToRight = lookupID(typedArray, index, constraint.layout.rightToRight);
                    break;
                case 31:
                    if (Build.VERSION.SDK_INT < 17) {
                        break;
                    } else {
                        constraint.layout.startMargin = typedArray.getDimensionPixelSize(index, constraint.layout.startMargin);
                        break;
                    }
                case 32:
                    constraint.layout.startToEnd = lookupID(typedArray, index, constraint.layout.startToEnd);
                    break;
                case 33:
                    constraint.layout.startToStart = lookupID(typedArray, index, constraint.layout.startToStart);
                    break;
                case 34:
                    constraint.layout.topMargin = typedArray.getDimensionPixelSize(index, constraint.layout.topMargin);
                    break;
                case 35:
                    constraint.layout.topToBottom = lookupID(typedArray, index, constraint.layout.topToBottom);
                    break;
                case 36:
                    constraint.layout.topToTop = lookupID(typedArray, index, constraint.layout.topToTop);
                    break;
                case 37:
                    constraint.layout.verticalBias = typedArray.getFloat(index, constraint.layout.verticalBias);
                    break;
                case 38:
                    constraint.mViewId = typedArray.getResourceId(index, constraint.mViewId);
                    break;
                case 39:
                    constraint.layout.horizontalWeight = typedArray.getFloat(index, constraint.layout.horizontalWeight);
                    break;
                case 40:
                    constraint.layout.verticalWeight = typedArray.getFloat(index, constraint.layout.verticalWeight);
                    break;
                case 41:
                    constraint.layout.horizontalChainStyle = typedArray.getInt(index, constraint.layout.horizontalChainStyle);
                    break;
                case 42:
                    constraint.layout.verticalChainStyle = typedArray.getInt(index, constraint.layout.verticalChainStyle);
                    break;
                case 43:
                    constraint.propertySet.alpha = typedArray.getFloat(index, constraint.propertySet.alpha);
                    break;
                case 44:
                    if (Build.VERSION.SDK_INT < 21) {
                        break;
                    } else {
                        constraint.transform.applyElevation = true;
                        constraint.transform.elevation = typedArray.getDimension(index, constraint.transform.elevation);
                        break;
                    }
                case 45:
                    constraint.transform.rotationX = typedArray.getFloat(index, constraint.transform.rotationX);
                    break;
                case 46:
                    constraint.transform.rotationY = typedArray.getFloat(index, constraint.transform.rotationY);
                    break;
                case 47:
                    constraint.transform.scaleX = typedArray.getFloat(index, constraint.transform.scaleX);
                    break;
                case 48:
                    constraint.transform.scaleY = typedArray.getFloat(index, constraint.transform.scaleY);
                    break;
                case 49:
                    constraint.transform.transformPivotX = typedArray.getDimension(index, constraint.transform.transformPivotX);
                    break;
                case 50:
                    constraint.transform.transformPivotY = typedArray.getDimension(index, constraint.transform.transformPivotY);
                    break;
                case 51:
                    constraint.transform.translationX = typedArray.getDimension(index, constraint.transform.translationX);
                    break;
                case 52:
                    constraint.transform.translationY = typedArray.getDimension(index, constraint.transform.translationY);
                    break;
                case 53:
                    if (Build.VERSION.SDK_INT < 21) {
                        break;
                    } else {
                        constraint.transform.translationZ = typedArray.getDimension(index, constraint.transform.translationZ);
                        break;
                    }
                case 54:
                    constraint.layout.widthDefault = typedArray.getInt(index, constraint.layout.widthDefault);
                    break;
                case 55:
                    constraint.layout.heightDefault = typedArray.getInt(index, constraint.layout.heightDefault);
                    break;
                case WIDTH_MAX /*56*/:
                    constraint.layout.widthMax = typedArray.getDimensionPixelSize(index, constraint.layout.widthMax);
                    break;
                case HEIGHT_MAX /*57*/:
                    constraint.layout.heightMax = typedArray.getDimensionPixelSize(index, constraint.layout.heightMax);
                    break;
                case WIDTH_MIN /*58*/:
                    constraint.layout.widthMin = typedArray.getDimensionPixelSize(index, constraint.layout.widthMin);
                    break;
                case HEIGHT_MIN /*59*/:
                    constraint.layout.heightMin = typedArray.getDimensionPixelSize(index, constraint.layout.heightMin);
                    break;
                case 60:
                    constraint.transform.rotation = typedArray.getFloat(index, constraint.transform.rotation);
                    break;
                case 61:
                    constraint.layout.circleConstraint = lookupID(typedArray, index, constraint.layout.circleConstraint);
                    break;
                case CIRCLE_RADIUS /*62*/:
                    constraint.layout.circleRadius = typedArray.getDimensionPixelSize(index, constraint.layout.circleRadius);
                    break;
                case 63:
                    constraint.layout.circleAngle = typedArray.getFloat(index, constraint.layout.circleAngle);
                    break;
                case 64:
                    constraint.motion.mAnimateRelativeTo = lookupID(typedArray, index, constraint.motion.mAnimateRelativeTo);
                    break;
                case 65:
                    if (typedArray.peekValue(index).type != 3) {
                        constraint.motion.mTransitionEasing = Easing.NAMED_EASING[typedArray.getInteger(index, 0)];
                        break;
                    } else {
                        constraint.motion.mTransitionEasing = typedArray.getString(index);
                        break;
                    }
                case 66:
                    constraint.motion.mDrawPath = typedArray.getInt(index, 0);
                    break;
                case 67:
                    constraint.motion.mPathRotate = typedArray.getFloat(index, constraint.motion.mPathRotate);
                    break;
                case PROGRESS /*68*/:
                    constraint.propertySet.mProgress = typedArray.getFloat(index, constraint.propertySet.mProgress);
                    break;
                case WIDTH_PERCENT /*69*/:
                    constraint.layout.widthPercent = typedArray.getFloat(index, 1.0f);
                    break;
                case HEIGHT_PERCENT /*70*/:
                    constraint.layout.heightPercent = typedArray.getFloat(index, 1.0f);
                    break;
                case CHAIN_USE_RTL /*71*/:
                    Log.e(TAG, "CURRENTLY UNSUPPORTED");
                    break;
                case BARRIER_DIRECTION /*72*/:
                    constraint.layout.mBarrierDirection = typedArray.getInt(index, constraint.layout.mBarrierDirection);
                    break;
                case BARRIER_MARGIN /*73*/:
                    constraint.layout.mBarrierMargin = typedArray.getDimensionPixelSize(index, constraint.layout.mBarrierMargin);
                    break;
                case CONSTRAINT_REFERENCED_IDS /*74*/:
                    constraint.layout.mReferenceIdString = typedArray.getString(index);
                    break;
                case BARRIER_ALLOWS_GONE_WIDGETS /*75*/:
                    constraint.layout.mBarrierAllowsGoneWidgets = typedArray.getBoolean(index, constraint.layout.mBarrierAllowsGoneWidgets);
                    break;
                case PATH_MOTION_ARC /*76*/:
                    constraint.motion.mPathMotionArc = typedArray.getInt(index, constraint.motion.mPathMotionArc);
                    break;
                case CONSTRAINT_TAG /*77*/:
                    constraint.layout.mConstraintTag = typedArray.getString(index);
                    break;
                case VISIBILITY_MODE /*78*/:
                    constraint.propertySet.mVisibilityMode = typedArray.getInt(index, constraint.propertySet.mVisibilityMode);
                    break;
                case MOTION_STAGGER /*79*/:
                    constraint.motion.mMotionStagger = typedArray.getFloat(index, constraint.motion.mMotionStagger);
                    break;
                case CONSTRAINED_WIDTH /*80*/:
                    constraint.layout.constrainedWidth = typedArray.getBoolean(index, constraint.layout.constrainedWidth);
                    break;
                case CONSTRAINED_HEIGHT /*81*/:
                    constraint.layout.constrainedHeight = typedArray.getBoolean(index, constraint.layout.constrainedHeight);
                    break;
                case ANIMATE_CIRCLE_ANGLE_TO /*82*/:
                    constraint.motion.mAnimateCircleAngleTo = typedArray.getInteger(index, constraint.motion.mAnimateCircleAngleTo);
                    break;
                case TRANSFORM_PIVOT_TARGET /*83*/:
                    constraint.transform.transformPivotTarget = lookupID(typedArray, index, constraint.transform.transformPivotTarget);
                    break;
                case QUANTIZE_MOTION_STEPS /*84*/:
                    constraint.motion.mQuantizeMotionSteps = typedArray.getInteger(index, constraint.motion.mQuantizeMotionSteps);
                    break;
                case QUANTIZE_MOTION_PHASE /*85*/:
                    constraint.motion.mQuantizeMotionPhase = typedArray.getFloat(index, constraint.motion.mQuantizeMotionPhase);
                    break;
                case QUANTIZE_MOTION_INTERPOLATOR /*86*/:
                    TypedValue peekValue = typedArray.peekValue(index);
                    if (peekValue.type != 1) {
                        if (peekValue.type != 3) {
                            constraint.motion.mQuantizeInterpolatorType = typedArray.getInteger(index, constraint.motion.mQuantizeInterpolatorID);
                            break;
                        } else {
                            constraint.motion.mQuantizeInterpolatorString = typedArray.getString(index);
                            if (constraint.motion.mQuantizeInterpolatorString.indexOf("/") <= 0) {
                                constraint.motion.mQuantizeInterpolatorType = -1;
                                break;
                            } else {
                                constraint.motion.mQuantizeInterpolatorID = typedArray.getResourceId(index, -1);
                                constraint.motion.mQuantizeInterpolatorType = -2;
                                break;
                            }
                        }
                    } else {
                        constraint.motion.mQuantizeInterpolatorID = typedArray.getResourceId(index, -1);
                        if (constraint.motion.mQuantizeInterpolatorID == -1) {
                            break;
                        } else {
                            constraint.motion.mQuantizeInterpolatorType = -2;
                            break;
                        }
                    }
                case UNUSED /*87*/:
                    StringBuilder append = new StringBuilder().append("unused attribute 0x");
                    String hexString = Integer.toHexString(index);
                    Log1F380D.a((Object) hexString);
                    Log.w(TAG, append.append(hexString).append("   ").append(mapToConstant.get(index)).toString());
                    break;
                case BASELINE_TO_TOP /*91*/:
                    constraint.layout.baselineToTop = lookupID(typedArray, index, constraint.layout.baselineToTop);
                    break;
                case BASELINE_TO_BOTTOM /*92*/:
                    constraint.layout.baselineToBottom = lookupID(typedArray, index, constraint.layout.baselineToBottom);
                    break;
                case BASELINE_MARGIN /*93*/:
                    constraint.layout.baselineMargin = typedArray.getDimensionPixelSize(index, constraint.layout.baselineMargin);
                    break;
                case GONE_BASELINE_MARGIN /*94*/:
                    constraint.layout.goneBaselineMargin = typedArray.getDimensionPixelSize(index, constraint.layout.goneBaselineMargin);
                    break;
                case LAYOUT_CONSTRAINT_WIDTH /*95*/:
                    parseDimensionConstraints(constraint.layout, typedArray, index, 0);
                    break;
                case LAYOUT_CONSTRAINT_HEIGHT /*96*/:
                    parseDimensionConstraints(constraint.layout, typedArray, index, 1);
                    break;
                case LAYOUT_WRAP_BEHAVIOR /*97*/:
                    constraint.layout.mWrapBehavior = typedArray.getInt(index, constraint.layout.mWrapBehavior);
                    break;
                default:
                    StringBuilder append2 = new StringBuilder().append("Unknown attribute 0x");
                    String hexString2 = Integer.toHexString(index);
                    Log1F380D.a((Object) hexString2);
                    Log.w(TAG, append2.append(hexString2).append("   ").append(mapToConstant.get(index)).toString());
                    break;
            }
        }
        if (constraint.layout.mReferenceIdString != null) {
            constraint.layout.mReferenceIds = null;
        }
    }

    private static void populateOverride(Context ctx, Constraint c, TypedArray a) {
        Constraint constraint = c;
        TypedArray typedArray = a;
        int indexCount = a.getIndexCount();
        Constraint.Delta delta = new Constraint.Delta();
        constraint.mDelta = delta;
        constraint.motion.mApply = false;
        constraint.layout.mApply = false;
        constraint.propertySet.mApply = false;
        constraint.transform.mApply = false;
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            switch (overrideMapToConstant.get(index)) {
                case 2:
                    delta.add(2, typedArray.getDimensionPixelSize(index, constraint.layout.bottomMargin));
                    break;
                case 5:
                    delta.add(5, typedArray.getString(index));
                    break;
                case 6:
                    delta.add(6, typedArray.getDimensionPixelOffset(index, constraint.layout.editorAbsoluteX));
                    break;
                case 7:
                    delta.add(7, typedArray.getDimensionPixelOffset(index, constraint.layout.editorAbsoluteY));
                    break;
                case 8:
                    if (Build.VERSION.SDK_INT < 17) {
                        break;
                    } else {
                        delta.add(8, typedArray.getDimensionPixelSize(index, constraint.layout.endMargin));
                        break;
                    }
                case 11:
                    delta.add(11, typedArray.getDimensionPixelSize(index, constraint.layout.goneBottomMargin));
                    break;
                case 12:
                    delta.add(12, typedArray.getDimensionPixelSize(index, constraint.layout.goneEndMargin));
                    break;
                case 13:
                    delta.add(13, typedArray.getDimensionPixelSize(index, constraint.layout.goneLeftMargin));
                    break;
                case 14:
                    delta.add(14, typedArray.getDimensionPixelSize(index, constraint.layout.goneRightMargin));
                    break;
                case 15:
                    delta.add(15, typedArray.getDimensionPixelSize(index, constraint.layout.goneStartMargin));
                    break;
                case 16:
                    delta.add(16, typedArray.getDimensionPixelSize(index, constraint.layout.goneTopMargin));
                    break;
                case 17:
                    delta.add(17, typedArray.getDimensionPixelOffset(index, constraint.layout.guideBegin));
                    break;
                case 18:
                    delta.add(18, typedArray.getDimensionPixelOffset(index, constraint.layout.guideEnd));
                    break;
                case 19:
                    delta.add(19, typedArray.getFloat(index, constraint.layout.guidePercent));
                    break;
                case 20:
                    delta.add(20, typedArray.getFloat(index, constraint.layout.horizontalBias));
                    break;
                case 21:
                    delta.add(21, typedArray.getLayoutDimension(index, constraint.layout.mHeight));
                    break;
                case 22:
                    delta.add(22, VISIBILITY_FLAGS[typedArray.getInt(index, constraint.propertySet.visibility)]);
                    break;
                case 23:
                    delta.add(23, typedArray.getLayoutDimension(index, constraint.layout.mWidth));
                    break;
                case 24:
                    delta.add(24, typedArray.getDimensionPixelSize(index, constraint.layout.leftMargin));
                    break;
                case 27:
                    delta.add(27, typedArray.getInt(index, constraint.layout.orientation));
                    break;
                case 28:
                    delta.add(28, typedArray.getDimensionPixelSize(index, constraint.layout.rightMargin));
                    break;
                case 31:
                    if (Build.VERSION.SDK_INT < 17) {
                        break;
                    } else {
                        delta.add(31, typedArray.getDimensionPixelSize(index, constraint.layout.startMargin));
                        break;
                    }
                case 34:
                    delta.add(34, typedArray.getDimensionPixelSize(index, constraint.layout.topMargin));
                    break;
                case 37:
                    delta.add(37, typedArray.getFloat(index, constraint.layout.verticalBias));
                    break;
                case 38:
                    constraint.mViewId = typedArray.getResourceId(index, constraint.mViewId);
                    delta.add(38, constraint.mViewId);
                    break;
                case 39:
                    delta.add(39, typedArray.getFloat(index, constraint.layout.horizontalWeight));
                    break;
                case 40:
                    delta.add(40, typedArray.getFloat(index, constraint.layout.verticalWeight));
                    break;
                case 41:
                    delta.add(41, typedArray.getInt(index, constraint.layout.horizontalChainStyle));
                    break;
                case 42:
                    delta.add(42, typedArray.getInt(index, constraint.layout.verticalChainStyle));
                    break;
                case 43:
                    delta.add(43, typedArray.getFloat(index, constraint.propertySet.alpha));
                    break;
                case 44:
                    if (Build.VERSION.SDK_INT < 21) {
                        break;
                    } else {
                        delta.add(44, true);
                        delta.add(44, typedArray.getDimension(index, constraint.transform.elevation));
                        break;
                    }
                case 45:
                    delta.add(45, typedArray.getFloat(index, constraint.transform.rotationX));
                    break;
                case 46:
                    delta.add(46, typedArray.getFloat(index, constraint.transform.rotationY));
                    break;
                case 47:
                    delta.add(47, typedArray.getFloat(index, constraint.transform.scaleX));
                    break;
                case 48:
                    delta.add(48, typedArray.getFloat(index, constraint.transform.scaleY));
                    break;
                case 49:
                    delta.add(49, typedArray.getDimension(index, constraint.transform.transformPivotX));
                    break;
                case 50:
                    delta.add(50, typedArray.getDimension(index, constraint.transform.transformPivotY));
                    break;
                case 51:
                    delta.add(51, typedArray.getDimension(index, constraint.transform.translationX));
                    break;
                case 52:
                    delta.add(52, typedArray.getDimension(index, constraint.transform.translationY));
                    break;
                case 53:
                    if (Build.VERSION.SDK_INT < 21) {
                        break;
                    } else {
                        delta.add(53, typedArray.getDimension(index, constraint.transform.translationZ));
                        break;
                    }
                case 54:
                    delta.add(54, typedArray.getInt(index, constraint.layout.widthDefault));
                    break;
                case 55:
                    delta.add(55, typedArray.getInt(index, constraint.layout.heightDefault));
                    break;
                case WIDTH_MAX /*56*/:
                    delta.add((int) WIDTH_MAX, typedArray.getDimensionPixelSize(index, constraint.layout.widthMax));
                    break;
                case HEIGHT_MAX /*57*/:
                    delta.add((int) HEIGHT_MAX, typedArray.getDimensionPixelSize(index, constraint.layout.heightMax));
                    break;
                case WIDTH_MIN /*58*/:
                    delta.add((int) WIDTH_MIN, typedArray.getDimensionPixelSize(index, constraint.layout.widthMin));
                    break;
                case HEIGHT_MIN /*59*/:
                    delta.add((int) HEIGHT_MIN, typedArray.getDimensionPixelSize(index, constraint.layout.heightMin));
                    break;
                case 60:
                    delta.add(60, typedArray.getFloat(index, constraint.transform.rotation));
                    break;
                case CIRCLE_RADIUS /*62*/:
                    delta.add((int) CIRCLE_RADIUS, typedArray.getDimensionPixelSize(index, constraint.layout.circleRadius));
                    break;
                case 63:
                    delta.add(63, typedArray.getFloat(index, constraint.layout.circleAngle));
                    break;
                case 64:
                    delta.add(64, lookupID(typedArray, index, constraint.motion.mAnimateRelativeTo));
                    break;
                case 65:
                    if (typedArray.peekValue(index).type != 3) {
                        delta.add(65, Easing.NAMED_EASING[typedArray.getInteger(index, 0)]);
                        break;
                    } else {
                        delta.add(65, typedArray.getString(index));
                        break;
                    }
                case 66:
                    delta.add(66, typedArray.getInt(index, 0));
                    break;
                case 67:
                    delta.add(67, typedArray.getFloat(index, constraint.motion.mPathRotate));
                    break;
                case PROGRESS /*68*/:
                    delta.add((int) PROGRESS, typedArray.getFloat(index, constraint.propertySet.mProgress));
                    break;
                case WIDTH_PERCENT /*69*/:
                    delta.add((int) WIDTH_PERCENT, typedArray.getFloat(index, 1.0f));
                    break;
                case HEIGHT_PERCENT /*70*/:
                    delta.add((int) HEIGHT_PERCENT, typedArray.getFloat(index, 1.0f));
                    break;
                case CHAIN_USE_RTL /*71*/:
                    Log.e(TAG, "CURRENTLY UNSUPPORTED");
                    break;
                case BARRIER_DIRECTION /*72*/:
                    delta.add((int) BARRIER_DIRECTION, typedArray.getInt(index, constraint.layout.mBarrierDirection));
                    break;
                case BARRIER_MARGIN /*73*/:
                    delta.add((int) BARRIER_MARGIN, typedArray.getDimensionPixelSize(index, constraint.layout.mBarrierMargin));
                    break;
                case CONSTRAINT_REFERENCED_IDS /*74*/:
                    delta.add((int) CONSTRAINT_REFERENCED_IDS, typedArray.getString(index));
                    break;
                case BARRIER_ALLOWS_GONE_WIDGETS /*75*/:
                    delta.add((int) BARRIER_ALLOWS_GONE_WIDGETS, typedArray.getBoolean(index, constraint.layout.mBarrierAllowsGoneWidgets));
                    break;
                case PATH_MOTION_ARC /*76*/:
                    delta.add((int) PATH_MOTION_ARC, typedArray.getInt(index, constraint.motion.mPathMotionArc));
                    break;
                case CONSTRAINT_TAG /*77*/:
                    delta.add((int) CONSTRAINT_TAG, typedArray.getString(index));
                    break;
                case VISIBILITY_MODE /*78*/:
                    delta.add((int) VISIBILITY_MODE, typedArray.getInt(index, constraint.propertySet.mVisibilityMode));
                    break;
                case MOTION_STAGGER /*79*/:
                    delta.add((int) MOTION_STAGGER, typedArray.getFloat(index, constraint.motion.mMotionStagger));
                    break;
                case CONSTRAINED_WIDTH /*80*/:
                    delta.add((int) CONSTRAINED_WIDTH, typedArray.getBoolean(index, constraint.layout.constrainedWidth));
                    break;
                case CONSTRAINED_HEIGHT /*81*/:
                    delta.add((int) CONSTRAINED_HEIGHT, typedArray.getBoolean(index, constraint.layout.constrainedHeight));
                    break;
                case ANIMATE_CIRCLE_ANGLE_TO /*82*/:
                    delta.add((int) ANIMATE_CIRCLE_ANGLE_TO, typedArray.getInteger(index, constraint.motion.mAnimateCircleAngleTo));
                    break;
                case TRANSFORM_PIVOT_TARGET /*83*/:
                    delta.add((int) TRANSFORM_PIVOT_TARGET, lookupID(typedArray, index, constraint.transform.transformPivotTarget));
                    break;
                case QUANTIZE_MOTION_STEPS /*84*/:
                    delta.add((int) QUANTIZE_MOTION_STEPS, typedArray.getInteger(index, constraint.motion.mQuantizeMotionSteps));
                    break;
                case QUANTIZE_MOTION_PHASE /*85*/:
                    delta.add((int) QUANTIZE_MOTION_PHASE, typedArray.getFloat(index, constraint.motion.mQuantizeMotionPhase));
                    break;
                case QUANTIZE_MOTION_INTERPOLATOR /*86*/:
                    TypedValue peekValue = typedArray.peekValue(index);
                    if (peekValue.type != 1) {
                        if (peekValue.type != 3) {
                            constraint.motion.mQuantizeInterpolatorType = typedArray.getInteger(index, constraint.motion.mQuantizeInterpolatorID);
                            delta.add((int) QUANTIZE_MOTION_INTERPOLATOR_TYPE, constraint.motion.mQuantizeInterpolatorType);
                            break;
                        } else {
                            constraint.motion.mQuantizeInterpolatorString = typedArray.getString(index);
                            delta.add((int) QUANTIZE_MOTION_INTERPOLATOR_STR, constraint.motion.mQuantizeInterpolatorString);
                            if (constraint.motion.mQuantizeInterpolatorString.indexOf("/") <= 0) {
                                constraint.motion.mQuantizeInterpolatorType = -1;
                                delta.add((int) QUANTIZE_MOTION_INTERPOLATOR_TYPE, constraint.motion.mQuantizeInterpolatorType);
                                break;
                            } else {
                                constraint.motion.mQuantizeInterpolatorID = typedArray.getResourceId(index, -1);
                                delta.add((int) QUANTIZE_MOTION_INTERPOLATOR_ID, constraint.motion.mQuantizeInterpolatorID);
                                constraint.motion.mQuantizeInterpolatorType = -2;
                                delta.add((int) QUANTIZE_MOTION_INTERPOLATOR_TYPE, constraint.motion.mQuantizeInterpolatorType);
                                break;
                            }
                        }
                    } else {
                        constraint.motion.mQuantizeInterpolatorID = typedArray.getResourceId(index, -1);
                        delta.add((int) QUANTIZE_MOTION_INTERPOLATOR_ID, constraint.motion.mQuantizeInterpolatorID);
                        if (constraint.motion.mQuantizeInterpolatorID == -1) {
                            break;
                        } else {
                            constraint.motion.mQuantizeInterpolatorType = -2;
                            delta.add((int) QUANTIZE_MOTION_INTERPOLATOR_TYPE, constraint.motion.mQuantizeInterpolatorType);
                            break;
                        }
                    }
                case UNUSED /*87*/:
                    StringBuilder append = new StringBuilder().append("unused attribute 0x");
                    String hexString = Integer.toHexString(index);
                    Log1F380D.a((Object) hexString);
                    Log.w(TAG, append.append(hexString).append("   ").append(mapToConstant.get(index)).toString());
                    break;
                case BASELINE_MARGIN /*93*/:
                    delta.add((int) BASELINE_MARGIN, typedArray.getDimensionPixelSize(index, constraint.layout.baselineMargin));
                    break;
                case GONE_BASELINE_MARGIN /*94*/:
                    delta.add((int) GONE_BASELINE_MARGIN, typedArray.getDimensionPixelSize(index, constraint.layout.goneBaselineMargin));
                    break;
                case LAYOUT_CONSTRAINT_WIDTH /*95*/:
                    parseDimensionConstraints(delta, typedArray, index, 0);
                    break;
                case LAYOUT_CONSTRAINT_HEIGHT /*96*/:
                    parseDimensionConstraints(delta, typedArray, index, 1);
                    break;
                case LAYOUT_WRAP_BEHAVIOR /*97*/:
                    delta.add((int) LAYOUT_WRAP_BEHAVIOR, typedArray.getInt(index, constraint.layout.mWrapBehavior));
                    break;
                case MOTION_TARGET /*98*/:
                    if (!MotionLayout.IS_IN_EDIT_MODE) {
                        if (typedArray.peekValue(index).type != 3) {
                            constraint.mViewId = typedArray.getResourceId(index, constraint.mViewId);
                            break;
                        } else {
                            constraint.mTargetString = typedArray.getString(index);
                            break;
                        }
                    } else {
                        constraint.mViewId = typedArray.getResourceId(index, constraint.mViewId);
                        if (constraint.mViewId != -1) {
                            break;
                        } else {
                            constraint.mTargetString = typedArray.getString(index);
                            break;
                        }
                    }
                case GUIDELINE_USE_RTL /*99*/:
                    delta.add((int) GUIDELINE_USE_RTL, typedArray.getBoolean(index, constraint.layout.guidelineUseRtl));
                    break;
                default:
                    StringBuilder append2 = new StringBuilder().append("Unknown attribute 0x");
                    String hexString2 = Integer.toHexString(index);
                    Log1F380D.a((Object) hexString2);
                    Log.w(TAG, append2.append(hexString2).append("   ").append(mapToConstant.get(index)).toString());
                    break;
            }
        }
    }

    private static String[] splitString(String str) {
        char[] charArray = str.toCharArray();
        ArrayList arrayList = new ArrayList();
        boolean z = false;
        int i = 0;
        for (int i2 = 0; i2 < charArray.length; i2++) {
            if (charArray[i2] == ',' && !z) {
                String str2 = new String(charArray, i, i2 - i);
                Log1F380D.a((Object) str2);
                arrayList.add(str2);
                i = i2 + 1;
            } else if (charArray[i2] == '\"') {
                z = !z;
            }
        }
        String str3 = new String(charArray, i, charArray.length - i);
        Log1F380D.a((Object) str3);
        arrayList.add(str3);
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public void applyCustomAttributes(ConstraintLayout constraintLayout) {
        Constraint constraint;
        int childCount = constraintLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = constraintLayout.getChildAt(i);
            int id = childAt.getId();
            if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
                StringBuilder append = new StringBuilder().append("id unknown ");
                String name = Debug.getName(childAt);
                Log1F380D.a((Object) name);
                Log.w(TAG, append.append(name).toString());
            } else if (this.mForceId && id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            } else if (this.mConstraints.containsKey(Integer.valueOf(id)) && (constraint = this.mConstraints.get(Integer.valueOf(id))) != null) {
                ConstraintAttribute.setAttributes(childAt, constraint.mCustomConstraints);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void applyToInternal(ConstraintLayout constraintLayout, boolean applyPostLayout) {
        int childCount = constraintLayout.getChildCount();
        HashSet hashSet = new HashSet(this.mConstraints.keySet());
        for (int i = 0; i < childCount; i++) {
            View childAt = constraintLayout.getChildAt(i);
            int id = childAt.getId();
            if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
                StringBuilder append = new StringBuilder().append("id unknown ");
                String name = Debug.getName(childAt);
                Log1F380D.a((Object) name);
                Log.w(TAG, append.append(name).toString());
            } else if (this.mForceId && id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            } else if (id != -1) {
                if (this.mConstraints.containsKey(Integer.valueOf(id))) {
                    hashSet.remove(Integer.valueOf(id));
                    Constraint constraint = this.mConstraints.get(Integer.valueOf(id));
                    if (constraint != null) {
                        if (childAt instanceof Barrier) {
                            constraint.layout.mHelperType = 1;
                            Barrier barrier = (Barrier) childAt;
                            barrier.setId(id);
                            barrier.setType(constraint.layout.mBarrierDirection);
                            barrier.setMargin(constraint.layout.mBarrierMargin);
                            barrier.setAllowsGoneWidget(constraint.layout.mBarrierAllowsGoneWidgets);
                            if (constraint.layout.mReferenceIds != null) {
                                barrier.setReferencedIds(constraint.layout.mReferenceIds);
                            } else if (constraint.layout.mReferenceIdString != null) {
                                constraint.layout.mReferenceIds = convertReferenceString(barrier, constraint.layout.mReferenceIdString);
                                barrier.setReferencedIds(constraint.layout.mReferenceIds);
                            }
                        }
                        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) childAt.getLayoutParams();
                        layoutParams.validate();
                        constraint.applyTo(layoutParams);
                        if (applyPostLayout) {
                            ConstraintAttribute.setAttributes(childAt, constraint.mCustomConstraints);
                        }
                        childAt.setLayoutParams(layoutParams);
                        if (constraint.propertySet.mVisibilityMode == 0) {
                            childAt.setVisibility(constraint.propertySet.visibility);
                        }
                        if (Build.VERSION.SDK_INT >= 17) {
                            childAt.setAlpha(constraint.propertySet.alpha);
                            childAt.setRotation(constraint.transform.rotation);
                            childAt.setRotationX(constraint.transform.rotationX);
                            childAt.setRotationY(constraint.transform.rotationY);
                            childAt.setScaleX(constraint.transform.scaleX);
                            childAt.setScaleY(constraint.transform.scaleY);
                            if (constraint.transform.transformPivotTarget != -1) {
                                View findViewById = ((View) childAt.getParent()).findViewById(constraint.transform.transformPivotTarget);
                                if (findViewById != null) {
                                    float top = ((float) (findViewById.getTop() + findViewById.getBottom())) / 2.0f;
                                    float left = ((float) (findViewById.getLeft() + findViewById.getRight())) / 2.0f;
                                    if (childAt.getRight() - childAt.getLeft() > 0 && childAt.getBottom() - childAt.getTop() > 0) {
                                        childAt.setPivotX(left - ((float) childAt.getLeft()));
                                        childAt.setPivotY(top - ((float) childAt.getTop()));
                                    }
                                }
                            } else {
                                if (!Float.isNaN(constraint.transform.transformPivotX)) {
                                    childAt.setPivotX(constraint.transform.transformPivotX);
                                }
                                if (!Float.isNaN(constraint.transform.transformPivotY)) {
                                    childAt.setPivotY(constraint.transform.transformPivotY);
                                }
                            }
                            childAt.setTranslationX(constraint.transform.translationX);
                            childAt.setTranslationY(constraint.transform.translationY);
                            if (Build.VERSION.SDK_INT >= 21) {
                                childAt.setTranslationZ(constraint.transform.translationZ);
                                if (constraint.transform.applyElevation) {
                                    childAt.setElevation(constraint.transform.elevation);
                                }
                            }
                        }
                    }
                } else {
                    Log.v(TAG, "WARNING NO CONSTRAINTS for view " + id);
                }
            }
        }
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            Integer num = (Integer) it.next();
            Constraint constraint2 = this.mConstraints.get(num);
            if (constraint2 != null) {
                if (constraint2.layout.mHelperType == 1) {
                    Barrier barrier2 = new Barrier(constraintLayout.getContext());
                    barrier2.setId(num.intValue());
                    if (constraint2.layout.mReferenceIds != null) {
                        barrier2.setReferencedIds(constraint2.layout.mReferenceIds);
                    } else if (constraint2.layout.mReferenceIdString != null) {
                        constraint2.layout.mReferenceIds = convertReferenceString(barrier2, constraint2.layout.mReferenceIdString);
                        barrier2.setReferencedIds(constraint2.layout.mReferenceIds);
                    }
                    barrier2.setType(constraint2.layout.mBarrierDirection);
                    barrier2.setMargin(constraint2.layout.mBarrierMargin);
                    ConstraintLayout.LayoutParams generateDefaultLayoutParams = constraintLayout.generateDefaultLayoutParams();
                    barrier2.validateParams();
                    constraint2.applyTo(generateDefaultLayoutParams);
                    constraintLayout.addView(barrier2, generateDefaultLayoutParams);
                }
                if (constraint2.layout.mIsGuideline) {
                    Guideline guideline = new Guideline(constraintLayout.getContext());
                    guideline.setId(num.intValue());
                    ConstraintLayout.LayoutParams generateDefaultLayoutParams2 = constraintLayout.generateDefaultLayoutParams();
                    constraint2.applyTo(generateDefaultLayoutParams2);
                    constraintLayout.addView(guideline, generateDefaultLayoutParams2);
                }
            }
        }
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt2 = constraintLayout.getChildAt(i2);
            if (childAt2 instanceof ConstraintHelper) {
                ((ConstraintHelper) childAt2).applyLayoutFeaturesInConstraintSet(constraintLayout);
            }
        }
    }
}
