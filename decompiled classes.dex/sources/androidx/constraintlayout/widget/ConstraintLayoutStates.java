package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;

public class ConstraintLayoutStates {
    private static final boolean DEBUG = false;
    public static final String TAG = "ConstraintLayoutStates";
    private final ConstraintLayout mConstraintLayout;
    private SparseArray<ConstraintSet> mConstraintSetMap = new SparseArray<>();
    private ConstraintsChangedListener mConstraintsChangedListener = null;
    int mCurrentConstraintNumber = -1;
    int mCurrentStateId = -1;
    ConstraintSet mDefaultConstraintSet;
    private SparseArray<State> mStateList = new SparseArray<>();

    static class State {
        int mConstraintID = -1;
        ConstraintSet mConstraintSet;
        int mId;
        ArrayList<Variant> mVariants = new ArrayList<>();

        public State(Context context, XmlPullParser parser) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(parser), R.styleable.State);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.State_android_id) {
                    this.mId = obtainStyledAttributes.getResourceId(index, this.mId);
                } else if (index == R.styleable.State_constraints) {
                    this.mConstraintID = obtainStyledAttributes.getResourceId(index, this.mConstraintID);
                    String resourceTypeName = context.getResources().getResourceTypeName(this.mConstraintID);
                    String resourceName = context.getResources().getResourceName(this.mConstraintID);
                    if ("layout".equals(resourceTypeName)) {
                        ConstraintSet constraintSet = new ConstraintSet();
                        this.mConstraintSet = constraintSet;
                        constraintSet.clone(context, this.mConstraintID);
                    }
                }
            }
            obtainStyledAttributes.recycle();
        }

        /* access modifiers changed from: package-private */
        public void add(Variant size) {
            this.mVariants.add(size);
        }

        public int findMatch(float width, float height) {
            for (int i = 0; i < this.mVariants.size(); i++) {
                if (this.mVariants.get(i).match(width, height)) {
                    return i;
                }
            }
            return -1;
        }
    }

    static class Variant {
        int mConstraintID = -1;
        ConstraintSet mConstraintSet;
        int mId;
        float mMaxHeight = Float.NaN;
        float mMaxWidth = Float.NaN;
        float mMinHeight = Float.NaN;
        float mMinWidth = Float.NaN;

        public Variant(Context context, XmlPullParser parser) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(parser), R.styleable.Variant);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.Variant_constraints) {
                    this.mConstraintID = obtainStyledAttributes.getResourceId(index, this.mConstraintID);
                    String resourceTypeName = context.getResources().getResourceTypeName(this.mConstraintID);
                    String resourceName = context.getResources().getResourceName(this.mConstraintID);
                    if ("layout".equals(resourceTypeName)) {
                        ConstraintSet constraintSet = new ConstraintSet();
                        this.mConstraintSet = constraintSet;
                        constraintSet.clone(context, this.mConstraintID);
                    }
                } else if (index == R.styleable.Variant_region_heightLessThan) {
                    this.mMaxHeight = obtainStyledAttributes.getDimension(index, this.mMaxHeight);
                } else if (index == R.styleable.Variant_region_heightMoreThan) {
                    this.mMinHeight = obtainStyledAttributes.getDimension(index, this.mMinHeight);
                } else if (index == R.styleable.Variant_region_widthLessThan) {
                    this.mMaxWidth = obtainStyledAttributes.getDimension(index, this.mMaxWidth);
                } else if (index == R.styleable.Variant_region_widthMoreThan) {
                    this.mMinWidth = obtainStyledAttributes.getDimension(index, this.mMinWidth);
                } else {
                    Log.v("ConstraintLayoutStates", "Unknown tag");
                }
            }
            obtainStyledAttributes.recycle();
        }

        /* access modifiers changed from: package-private */
        public boolean match(float widthDp, float heightDp) {
            if (!Float.isNaN(this.mMinWidth) && widthDp < this.mMinWidth) {
                return false;
            }
            if (!Float.isNaN(this.mMinHeight) && heightDp < this.mMinHeight) {
                return false;
            }
            if (Float.isNaN(this.mMaxWidth) || widthDp <= this.mMaxWidth) {
                return Float.isNaN(this.mMaxHeight) || heightDp <= this.mMaxHeight;
            }
            return false;
        }
    }

    ConstraintLayoutStates(Context context, ConstraintLayout layout, int resourceID) {
        this.mConstraintLayout = layout;
        load(context, resourceID);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void load(android.content.Context r10, int r11) {
        /*
            r9 = this;
            android.content.res.Resources r0 = r10.getResources()
            android.content.res.XmlResourceParser r1 = r0.getXml(r11)
            r2 = 0
            r3 = 0
            r4 = 0
            int r5 = r1.getEventType()     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
        L_0x000f:
            r6 = 1
            if (r5 == r6) goto L_0x008a
            switch(r5) {
                case 0: goto L_0x007e;
                case 1: goto L_0x0015;
                case 2: goto L_0x001a;
                case 3: goto L_0x0017;
                default: goto L_0x0015;
            }     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
        L_0x0015:
            goto L_0x0084
        L_0x0017:
            r3 = 0
            goto L_0x0084
        L_0x001a:
            java.lang.String r7 = r1.getName()     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
            r3 = r7
            r7 = -1
            int r8 = r3.hashCode()     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
            switch(r8) {
                case -1349929691: goto L_0x004f;
                case 80204913: goto L_0x0045;
                case 1382829617: goto L_0x003c;
                case 1657696882: goto L_0x0032;
                case 1901439077: goto L_0x0028;
                default: goto L_0x0027;
            }     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
        L_0x0027:
            goto L_0x0059
        L_0x0028:
            java.lang.String r6 = "Variant"
            boolean r6 = r3.equals(r6)     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
            if (r6 == 0) goto L_0x0027
            r6 = 3
            goto L_0x005a
        L_0x0032:
            java.lang.String r6 = "layoutDescription"
            boolean r6 = r3.equals(r6)     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
            if (r6 == 0) goto L_0x0027
            r6 = 0
            goto L_0x005a
        L_0x003c:
            java.lang.String r8 = "StateSet"
            boolean r8 = r3.equals(r8)     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
            if (r8 == 0) goto L_0x0027
            goto L_0x005a
        L_0x0045:
            java.lang.String r6 = "State"
            boolean r6 = r3.equals(r6)     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
            if (r6 == 0) goto L_0x0027
            r6 = 2
            goto L_0x005a
        L_0x004f:
            java.lang.String r6 = "ConstraintSet"
            boolean r6 = r3.equals(r6)     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
            if (r6 == 0) goto L_0x0027
            r6 = 4
            goto L_0x005a
        L_0x0059:
            r6 = r7
        L_0x005a:
            switch(r6) {
                case 0: goto L_0x007c;
                case 1: goto L_0x007b;
                case 2: goto L_0x006d;
                case 3: goto L_0x0062;
                case 4: goto L_0x005e;
                default: goto L_0x005d;
            }     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
        L_0x005d:
            goto L_0x007d
        L_0x005e:
            r9.parseConstraintSet(r10, r1)     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
            goto L_0x007d
        L_0x0062:
            androidx.constraintlayout.widget.ConstraintLayoutStates$Variant r6 = new androidx.constraintlayout.widget.ConstraintLayoutStates$Variant     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
            r6.<init>(r10, r1)     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
            if (r4 == 0) goto L_0x007d
            r4.add(r6)     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
            goto L_0x007d
        L_0x006d:
            androidx.constraintlayout.widget.ConstraintLayoutStates$State r6 = new androidx.constraintlayout.widget.ConstraintLayoutStates$State     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
            r6.<init>(r10, r1)     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
            r4 = r6
            android.util.SparseArray<androidx.constraintlayout.widget.ConstraintLayoutStates$State> r6 = r9.mStateList     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
            int r7 = r4.mId     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
            r6.put(r7, r4)     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
            goto L_0x007d
        L_0x007b:
            goto L_0x007d
        L_0x007c:
        L_0x007d:
            goto L_0x0084
        L_0x007e:
            java.lang.String r6 = r1.getName()     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
            r2 = r6
        L_0x0084:
            int r6 = r1.next()     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008b }
            r5 = r6
            goto L_0x000f
        L_0x008a:
            goto L_0x0094
        L_0x008b:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x0095
        L_0x0090:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0094:
        L_0x0095:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.ConstraintLayoutStates.load(android.content.Context, int):void");
    }

    private void parseConstraintSet(Context context, XmlPullParser parser) {
        ConstraintSet constraintSet = new ConstraintSet();
        int attributeCount = parser.getAttributeCount();
        int i = 0;
        while (i < attributeCount) {
            String attributeName = parser.getAttributeName(i);
            String attributeValue = parser.getAttributeValue(i);
            if (attributeName == null || attributeValue == null || !"id".equals(attributeName)) {
                i++;
            } else {
                int i2 = -1;
                if (attributeValue.contains("/")) {
                    i2 = context.getResources().getIdentifier(attributeValue.substring(attributeValue.indexOf(47) + 1), "id", context.getPackageName());
                }
                if (i2 == -1) {
                    if (attributeValue.length() > 1) {
                        i2 = Integer.parseInt(attributeValue.substring(1));
                    } else {
                        Log.e("ConstraintLayoutStates", "error in parsing id");
                    }
                }
                constraintSet.load(context, parser);
                this.mConstraintSetMap.put(i2, constraintSet);
                return;
            }
        }
    }

    public boolean needsToChange(int id, float width, float height) {
        int i = this.mCurrentStateId;
        if (i != id) {
            return true;
        }
        State state = (State) (id == -1 ? this.mStateList.valueAt(0) : this.mStateList.get(i));
        return (this.mCurrentConstraintNumber == -1 || !state.mVariants.get(this.mCurrentConstraintNumber).match(width, height)) && this.mCurrentConstraintNumber != state.findMatch(width, height);
    }

    public void setOnConstraintsChanged(ConstraintsChangedListener constraintsChangedListener) {
        this.mConstraintsChangedListener = constraintsChangedListener;
    }

    public void updateConstraints(int id, float width, float height) {
        int findMatch;
        int i = this.mCurrentStateId;
        if (i == id) {
            State valueAt = id == -1 ? this.mStateList.valueAt(0) : this.mStateList.get(i);
            if ((this.mCurrentConstraintNumber == -1 || !valueAt.mVariants.get(this.mCurrentConstraintNumber).match(width, height)) && this.mCurrentConstraintNumber != (findMatch = valueAt.findMatch(width, height))) {
                ConstraintSet constraintSet = findMatch == -1 ? this.mDefaultConstraintSet : valueAt.mVariants.get(findMatch).mConstraintSet;
                int i2 = findMatch == -1 ? valueAt.mConstraintID : valueAt.mVariants.get(findMatch).mConstraintID;
                if (constraintSet != null) {
                    this.mCurrentConstraintNumber = findMatch;
                    ConstraintsChangedListener constraintsChangedListener = this.mConstraintsChangedListener;
                    if (constraintsChangedListener != null) {
                        constraintsChangedListener.preLayoutChange(-1, i2);
                    }
                    constraintSet.applyTo(this.mConstraintLayout);
                    ConstraintsChangedListener constraintsChangedListener2 = this.mConstraintsChangedListener;
                    if (constraintsChangedListener2 != null) {
                        constraintsChangedListener2.postLayoutChange(-1, i2);
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        this.mCurrentStateId = id;
        State state = this.mStateList.get(id);
        int findMatch2 = state.findMatch(width, height);
        ConstraintSet constraintSet2 = findMatch2 == -1 ? state.mConstraintSet : state.mVariants.get(findMatch2).mConstraintSet;
        int i3 = findMatch2 == -1 ? state.mConstraintID : state.mVariants.get(findMatch2).mConstraintID;
        if (constraintSet2 == null) {
            Log.v("ConstraintLayoutStates", "NO Constraint set found ! id=" + id + ", dim =" + width + ", " + height);
            return;
        }
        this.mCurrentConstraintNumber = findMatch2;
        ConstraintsChangedListener constraintsChangedListener3 = this.mConstraintsChangedListener;
        if (constraintsChangedListener3 != null) {
            constraintsChangedListener3.preLayoutChange(id, i3);
        }
        constraintSet2.applyTo(this.mConstraintLayout);
        ConstraintsChangedListener constraintsChangedListener4 = this.mConstraintsChangedListener;
        if (constraintsChangedListener4 != null) {
            constraintsChangedListener4.postLayoutChange(id, i3);
        }
    }
}
