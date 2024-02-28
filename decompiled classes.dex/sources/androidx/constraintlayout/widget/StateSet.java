package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;

public class StateSet {
    private static final boolean DEBUG = false;
    public static final String TAG = "ConstraintLayoutStates";
    private SparseArray<ConstraintSet> mConstraintSetMap = new SparseArray<>();
    private ConstraintsChangedListener mConstraintsChangedListener = null;
    int mCurrentConstraintNumber = -1;
    int mCurrentStateId = -1;
    ConstraintSet mDefaultConstraintSet;
    int mDefaultState = -1;
    private SparseArray<State> mStateList = new SparseArray<>();

    static class State {
        int mConstraintID = -1;
        int mId;
        boolean mIsLayout = false;
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
                        this.mIsLayout = true;
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
        int mId;
        boolean mIsLayout = false;
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
                        this.mIsLayout = true;
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

    public StateSet(Context context, XmlPullParser parser) {
        load(context, parser);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void load(android.content.Context r12, org.xmlpull.v1.XmlPullParser r13) {
        /*
            r11 = this;
            android.util.AttributeSet r0 = android.util.Xml.asAttributeSet(r13)
            int[] r1 = androidx.constraintlayout.widget.R.styleable.StateSet
            android.content.res.TypedArray r1 = r12.obtainStyledAttributes(r0, r1)
            int r2 = r1.getIndexCount()
            r3 = 0
        L_0x000f:
            if (r3 >= r2) goto L_0x0024
            int r4 = r1.getIndex(r3)
            int r5 = androidx.constraintlayout.widget.R.styleable.StateSet_defaultState
            if (r4 != r5) goto L_0x0021
            int r5 = r11.mDefaultState
            int r5 = r1.getResourceId(r4, r5)
            r11.mDefaultState = r5
        L_0x0021:
            int r3 = r3 + 1
            goto L_0x000f
        L_0x0024:
            r1.recycle()
            r3 = 0
            r4 = 0
            r5 = 0
            int r6 = r13.getEventType()     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
        L_0x002e:
            r7 = 1
            if (r6 == r7) goto L_0x00a5
            java.lang.String r8 = "StateSet"
            switch(r6) {
                case 0: goto L_0x0099;
                case 1: goto L_0x0036;
                case 2: goto L_0x0045;
                case 3: goto L_0x0038;
                default: goto L_0x0036;
            }
        L_0x0036:
            goto L_0x009f
        L_0x0038:
            java.lang.String r7 = r13.getName()     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
            boolean r7 = r8.equals(r7)     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
            if (r7 == 0) goto L_0x0043
            return
        L_0x0043:
            r3 = 0
            goto L_0x009f
        L_0x0045:
            java.lang.String r9 = r13.getName()     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
            r3 = r9
            r9 = -1
            int r10 = r3.hashCode()     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
            switch(r10) {
                case 80204913: goto L_0x006e;
                case 1301459538: goto L_0x0064;
                case 1382829617: goto L_0x005d;
                case 1901439077: goto L_0x0053;
                default: goto L_0x0052;
            }     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
        L_0x0052:
            goto L_0x0078
        L_0x0053:
            java.lang.String r7 = "Variant"
            boolean r7 = r3.equals(r7)     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
            if (r7 == 0) goto L_0x0052
            r7 = 3
            goto L_0x0079
        L_0x005d:
            boolean r8 = r3.equals(r8)     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
            if (r8 == 0) goto L_0x0052
            goto L_0x0079
        L_0x0064:
            java.lang.String r7 = "LayoutDescription"
            boolean r7 = r3.equals(r7)     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
            if (r7 == 0) goto L_0x0052
            r7 = 0
            goto L_0x0079
        L_0x006e:
            java.lang.String r7 = "State"
            boolean r7 = r3.equals(r7)     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
            if (r7 == 0) goto L_0x0052
            r7 = 2
            goto L_0x0079
        L_0x0078:
            r7 = r9
        L_0x0079:
            switch(r7) {
                case 0: goto L_0x0097;
                case 1: goto L_0x0096;
                case 2: goto L_0x0088;
                case 3: goto L_0x007d;
                default: goto L_0x007c;
            }     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
        L_0x007c:
            goto L_0x0098
        L_0x007d:
            androidx.constraintlayout.widget.StateSet$Variant r7 = new androidx.constraintlayout.widget.StateSet$Variant     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
            r7.<init>(r12, r13)     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
            if (r5 == 0) goto L_0x0098
            r5.add(r7)     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
            goto L_0x0098
        L_0x0088:
            androidx.constraintlayout.widget.StateSet$State r7 = new androidx.constraintlayout.widget.StateSet$State     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
            r7.<init>(r12, r13)     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
            r5 = r7
            android.util.SparseArray<androidx.constraintlayout.widget.StateSet$State> r7 = r11.mStateList     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
            int r8 = r5.mId     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
            r7.put(r8, r5)     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
            goto L_0x0098
        L_0x0096:
            goto L_0x0098
        L_0x0097:
        L_0x0098:
            goto L_0x009f
        L_0x0099:
            java.lang.String r7 = r13.getName()     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
            r4 = r7
        L_0x009f:
            int r7 = r13.next()     // Catch:{ XmlPullParserException -> 0x00ab, IOException -> 0x00a6 }
            r6 = r7
            goto L_0x002e
        L_0x00a5:
            goto L_0x00af
        L_0x00a6:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x00b0
        L_0x00ab:
            r4 = move-exception
            r4.printStackTrace()
        L_0x00af:
        L_0x00b0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.StateSet.load(android.content.Context, org.xmlpull.v1.XmlPullParser):void");
    }

    public int convertToConstraintSet(int currentConstrainSettId, int stateId, float width, float height) {
        State state = this.mStateList.get(stateId);
        if (state == null) {
            return stateId;
        }
        if (width != -1.0f && height != -1.0f) {
            Variant variant = null;
            Iterator<Variant> it = state.mVariants.iterator();
            while (it.hasNext()) {
                Variant next = it.next();
                if (next.match(width, height)) {
                    if (currentConstrainSettId == next.mConstraintID) {
                        return currentConstrainSettId;
                    }
                    variant = next;
                }
            }
            return variant != null ? variant.mConstraintID : state.mConstraintID;
        } else if (state.mConstraintID == currentConstrainSettId) {
            return currentConstrainSettId;
        } else {
            Iterator<Variant> it2 = state.mVariants.iterator();
            while (it2.hasNext()) {
                if (currentConstrainSettId == it2.next().mConstraintID) {
                    return currentConstrainSettId;
                }
            }
            return state.mConstraintID;
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

    public int stateGetConstraintID(int id, int width, int height) {
        return updateConstraints(-1, id, (float) width, (float) height);
    }

    public int updateConstraints(int currentId, int id, float width, float height) {
        int findMatch;
        if (currentId == id) {
            State valueAt = id == -1 ? this.mStateList.valueAt(0) : this.mStateList.get(this.mCurrentStateId);
            if (valueAt == null) {
                return -1;
            }
            return ((this.mCurrentConstraintNumber == -1 || !valueAt.mVariants.get(currentId).match(width, height)) && currentId != (findMatch = valueAt.findMatch(width, height))) ? findMatch == -1 ? valueAt.mConstraintID : valueAt.mVariants.get(findMatch).mConstraintID : currentId;
        }
        State state = this.mStateList.get(id);
        if (state == null) {
            return -1;
        }
        int findMatch2 = state.findMatch(width, height);
        return findMatch2 == -1 ? state.mConstraintID : state.mVariants.get(findMatch2).mConstraintID;
    }
}
