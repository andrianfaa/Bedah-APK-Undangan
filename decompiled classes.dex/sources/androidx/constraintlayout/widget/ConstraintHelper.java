package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.core.widgets.Helper;
import androidx.constraintlayout.core.widgets.HelperWidget;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import java.util.Arrays;
import java.util.HashMap;

public abstract class ConstraintHelper extends View {
    protected int mCount;
    protected Helper mHelperWidget;
    protected int[] mIds = new int[32];
    protected HashMap<Integer, String> mMap = new HashMap<>();
    protected String mReferenceIds;
    protected String mReferenceTags;
    protected boolean mUseViewMeasure = false;
    private View[] mViews = null;
    protected Context myContext;

    public ConstraintHelper(Context context) {
        super(context);
        this.myContext = context;
        init((AttributeSet) null);
    }

    public ConstraintHelper(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.myContext = context;
        init(attrs);
    }

    public ConstraintHelper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.myContext = context;
        init(attrs);
    }

    private void addID(String idString) {
        if (idString != null && idString.length() != 0 && this.myContext != null) {
            String idString2 = idString.trim();
            if (getParent() instanceof ConstraintLayout) {
                ConstraintLayout constraintLayout = (ConstraintLayout) getParent();
            }
            int findId = findId(idString2);
            if (findId != 0) {
                this.mMap.put(Integer.valueOf(findId), idString2);
                addRscID(findId);
                return;
            }
            Log.w("ConstraintHelper", "Could not find id of \"" + idString2 + "\"");
        }
    }

    private void addRscID(int id) {
        if (id != getId()) {
            int i = this.mCount + 1;
            int[] iArr = this.mIds;
            if (i > iArr.length) {
                this.mIds = Arrays.copyOf(iArr, iArr.length * 2);
            }
            int[] iArr2 = this.mIds;
            int i2 = this.mCount;
            iArr2[i2] = id;
            this.mCount = i2 + 1;
        }
    }

    /* JADX WARNING: type inference failed for: r1v3, types: [android.view.ViewParent] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addTag(java.lang.String r10) {
        /*
            r9 = this;
            if (r10 == 0) goto L_0x0083
            int r0 = r10.length()
            if (r0 != 0) goto L_0x000a
            goto L_0x0083
        L_0x000a:
            android.content.Context r0 = r9.myContext
            if (r0 != 0) goto L_0x000f
            return
        L_0x000f:
            java.lang.String r10 = r10.trim()
            r0 = 0
            android.view.ViewParent r1 = r9.getParent()
            boolean r1 = r1 instanceof androidx.constraintlayout.widget.ConstraintLayout
            if (r1 == 0) goto L_0x0023
            android.view.ViewParent r1 = r9.getParent()
            r0 = r1
            androidx.constraintlayout.widget.ConstraintLayout r0 = (androidx.constraintlayout.widget.ConstraintLayout) r0
        L_0x0023:
            java.lang.String r1 = "ConstraintHelper"
            if (r0 != 0) goto L_0x002d
            java.lang.String r2 = "Parent not a ConstraintLayout"
            android.util.Log.w(r1, r2)
            return
        L_0x002d:
            int r2 = r0.getChildCount()
            r3 = 0
        L_0x0032:
            if (r3 >= r2) goto L_0x0082
            android.view.View r4 = r0.getChildAt(r3)
            android.view.ViewGroup$LayoutParams r5 = r4.getLayoutParams()
            boolean r6 = r5 instanceof androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
            if (r6 == 0) goto L_0x007f
            r6 = r5
            androidx.constraintlayout.widget.ConstraintLayout$LayoutParams r6 = (androidx.constraintlayout.widget.ConstraintLayout.LayoutParams) r6
            java.lang.String r7 = r6.constraintTag
            boolean r7 = r10.equals(r7)
            if (r7 == 0) goto L_0x007f
            int r7 = r4.getId()
            r8 = -1
            if (r7 != r8) goto L_0x0078
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "to use ConstraintTag view "
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.Class r8 = r4.getClass()
            java.lang.String r8 = r8.getSimpleName()
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r8 = " must have an ID"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r7 = r7.toString()
            android.util.Log.w(r1, r7)
            goto L_0x007f
        L_0x0078:
            int r7 = r4.getId()
            r9.addRscID(r7)
        L_0x007f:
            int r3 = r3 + 1
            goto L_0x0032
        L_0x0082:
            return
        L_0x0083:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.ConstraintHelper.addTag(java.lang.String):void");
    }

    private int[] convertReferenceString(View view, String referenceIdString) {
        String[] split = referenceIdString.split(",");
        Context context = view.getContext();
        int[] iArr = new int[split.length];
        int i = 0;
        for (String trim : split) {
            int findId = findId(trim.trim());
            if (findId != 0) {
                iArr[i] = findId;
                i++;
            }
        }
        return i != split.length ? Arrays.copyOf(iArr, i) : iArr;
    }

    private int findId(ConstraintLayout container, String idString) {
        Resources resources;
        if (idString == null || container == null || (resources = this.myContext.getResources()) == null) {
            return 0;
        }
        int childCount = container.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = container.getChildAt(i);
            if (childAt.getId() != -1) {
                String str = null;
                try {
                    str = resources.getResourceEntryName(childAt.getId());
                } catch (Resources.NotFoundException e) {
                }
                if (idString.equals(str)) {
                    return childAt.getId();
                }
            }
        }
        return 0;
    }

    /* JADX WARNING: type inference failed for: r1v10, types: [android.view.ViewParent] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int findId(java.lang.String r6) {
        /*
            r5 = this;
            r0 = 0
            android.view.ViewParent r1 = r5.getParent()
            boolean r1 = r1 instanceof androidx.constraintlayout.widget.ConstraintLayout
            if (r1 == 0) goto L_0x0010
            android.view.ViewParent r1 = r5.getParent()
            r0 = r1
            androidx.constraintlayout.widget.ConstraintLayout r0 = (androidx.constraintlayout.widget.ConstraintLayout) r0
        L_0x0010:
            r1 = 0
            boolean r2 = r5.isInEditMode()
            if (r2 == 0) goto L_0x0029
            if (r0 == 0) goto L_0x0029
            r2 = 0
            java.lang.Object r2 = r0.getDesignInformation(r2, r6)
            boolean r3 = r2 instanceof java.lang.Integer
            if (r3 == 0) goto L_0x0029
            r3 = r2
            java.lang.Integer r3 = (java.lang.Integer) r3
            int r1 = r3.intValue()
        L_0x0029:
            if (r1 != 0) goto L_0x0031
            if (r0 == 0) goto L_0x0031
            int r1 = r5.findId(r0, r6)
        L_0x0031:
            if (r1 != 0) goto L_0x0041
            java.lang.Class<androidx.constraintlayout.widget.R$id> r2 = androidx.constraintlayout.widget.R.id.class
            java.lang.reflect.Field r3 = r2.getField(r6)     // Catch:{ Exception -> 0x0040 }
            r4 = 0
            int r4 = r3.getInt(r4)     // Catch:{ Exception -> 0x0040 }
            r1 = r4
            goto L_0x0041
        L_0x0040:
            r2 = move-exception
        L_0x0041:
            if (r1 != 0) goto L_0x0055
            android.content.Context r2 = r5.myContext
            android.content.res.Resources r2 = r2.getResources()
            android.content.Context r3 = r5.myContext
            java.lang.String r3 = r3.getPackageName()
            java.lang.String r4 = "id"
            int r1 = r2.getIdentifier(r6, r4, r3)
        L_0x0055:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.ConstraintHelper.findId(java.lang.String):int");
    }

    public void addView(View view) {
        if (view != this) {
            if (view.getId() == -1) {
                Log.e("ConstraintHelper", "Views added to a ConstraintHelper need to have an id");
            } else if (view.getParent() == null) {
                Log.e("ConstraintHelper", "Views added to a ConstraintHelper need to have a parent");
            } else {
                this.mReferenceIds = null;
                addRscID(view.getId());
                requestLayout();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void applyLayoutFeatures() {
        ViewParent parent = getParent();
        if (parent != null && (parent instanceof ConstraintLayout)) {
            applyLayoutFeatures((ConstraintLayout) parent);
        }
    }

    /* access modifiers changed from: protected */
    public void applyLayoutFeatures(ConstraintLayout container) {
        int visibility = getVisibility();
        float f = 0.0f;
        if (Build.VERSION.SDK_INT >= 21) {
            f = getElevation();
        }
        for (int i = 0; i < this.mCount; i++) {
            View viewById = container.getViewById(this.mIds[i]);
            if (viewById != null) {
                viewById.setVisibility(visibility);
                if (f > 0.0f && Build.VERSION.SDK_INT >= 21) {
                    viewById.setTranslationZ(viewById.getTranslationZ() + f);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void applyLayoutFeaturesInConstraintSet(ConstraintLayout container) {
    }

    public boolean containsId(int id) {
        for (int i : this.mIds) {
            if (i == id) {
                return true;
            }
        }
        return false;
    }

    public int[] getReferencedIds() {
        return Arrays.copyOf(this.mIds, this.mCount);
    }

    /* access modifiers changed from: protected */
    public View[] getViews(ConstraintLayout layout) {
        View[] viewArr = this.mViews;
        if (viewArr == null || viewArr.length != this.mCount) {
            this.mViews = new View[this.mCount];
        }
        for (int i = 0; i < this.mCount; i++) {
            this.mViews[i] = layout.getViewById(this.mIds[i]);
        }
        return this.mViews;
    }

    public int indexFromId(int id) {
        int i = -1;
        for (int i2 : this.mIds) {
            i++;
            if (i2 == id) {
                return i;
            }
        }
        return i;
    }

    /* access modifiers changed from: protected */
    public void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.ConstraintLayout_Layout);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.ConstraintLayout_Layout_constraint_referenced_ids) {
                    String string = obtainStyledAttributes.getString(index);
                    this.mReferenceIds = string;
                    setIds(string);
                } else if (index == R.styleable.ConstraintLayout_Layout_constraint_referenced_tags) {
                    String string2 = obtainStyledAttributes.getString(index);
                    this.mReferenceTags = string2;
                    setReferenceTags(string2);
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    public void loadParameters(ConstraintSet.Constraint constraint, HelperWidget child, ConstraintLayout.LayoutParams layoutParams, SparseArray<ConstraintWidget> sparseArray) {
        if (constraint.layout.mReferenceIds != null) {
            setReferencedIds(constraint.layout.mReferenceIds);
        } else if (constraint.layout.mReferenceIdString != null) {
            if (constraint.layout.mReferenceIdString.length() > 0) {
                constraint.layout.mReferenceIds = convertReferenceString(this, constraint.layout.mReferenceIdString);
            } else {
                constraint.layout.mReferenceIds = null;
            }
        }
        if (child != null) {
            child.removeAllIds();
            if (constraint.layout.mReferenceIds != null) {
                for (int i : constraint.layout.mReferenceIds) {
                    ConstraintWidget constraintWidget = sparseArray.get(i);
                    if (constraintWidget != null) {
                        child.add(constraintWidget);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        String str = this.mReferenceIds;
        if (str != null) {
            setIds(str);
        }
        String str2 = this.mReferenceTags;
        if (str2 != null) {
            setReferenceTags(str2);
        }
    }

    public void onDraw(Canvas canvas) {
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mUseViewMeasure) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            setMeasuredDimension(0, 0);
        }
    }

    public int removeView(View view) {
        int i;
        int i2 = -1;
        int id = view.getId();
        if (id == -1) {
            return -1;
        }
        this.mReferenceIds = null;
        int i3 = 0;
        while (true) {
            if (i3 >= this.mCount) {
                break;
            } else if (this.mIds[i3] == id) {
                i2 = i3;
                int i4 = i3;
                while (true) {
                    i = this.mCount;
                    if (i4 >= i - 1) {
                        break;
                    }
                    int[] iArr = this.mIds;
                    iArr[i4] = iArr[i4 + 1];
                    i4++;
                }
                this.mIds[i - 1] = 0;
                this.mCount = i - 1;
            } else {
                i3++;
            }
        }
        requestLayout();
        return i2;
    }

    public void resolveRtl(ConstraintWidget widget, boolean isRtl) {
    }

    /* access modifiers changed from: protected */
    public void setIds(String idList) {
        this.mReferenceIds = idList;
        if (idList != null) {
            int i = 0;
            this.mCount = 0;
            while (true) {
                int indexOf = idList.indexOf(44, i);
                if (indexOf == -1) {
                    addID(idList.substring(i));
                    return;
                } else {
                    addID(idList.substring(i, indexOf));
                    i = indexOf + 1;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setReferenceTags(String tagList) {
        this.mReferenceTags = tagList;
        if (tagList != null) {
            int i = 0;
            this.mCount = 0;
            while (true) {
                int indexOf = tagList.indexOf(44, i);
                if (indexOf == -1) {
                    addTag(tagList.substring(i));
                    return;
                } else {
                    addTag(tagList.substring(i, indexOf));
                    i = indexOf + 1;
                }
            }
        }
    }

    public void setReferencedIds(int[] ids) {
        this.mReferenceIds = null;
        this.mCount = 0;
        for (int addRscID : ids) {
            addRscID(addRscID);
        }
    }

    public void setTag(int key, Object tag) {
        super.setTag(key, tag);
        if (tag == null && this.mReferenceIds == null) {
            addRscID(key);
        }
    }

    public void updatePostConstraints(ConstraintLayout container) {
    }

    public void updatePostLayout(ConstraintLayout container) {
    }

    public void updatePostMeasure(ConstraintLayout container) {
    }

    public void updatePreDraw(ConstraintLayout container) {
    }

    public void updatePreLayout(ConstraintWidgetContainer container, Helper helper, SparseArray<ConstraintWidget> sparseArray) {
        helper.removeAllIds();
        for (int i = 0; i < this.mCount; i++) {
            helper.add(sparseArray.get(this.mIds[i]));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0022, code lost:
        r3 = r7.mMap.get(java.lang.Integer.valueOf(r1));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updatePreLayout(androidx.constraintlayout.widget.ConstraintLayout r8) {
        /*
            r7 = this;
            boolean r0 = r7.isInEditMode()
            if (r0 == 0) goto L_0x000b
            java.lang.String r0 = r7.mReferenceIds
            r7.setIds(r0)
        L_0x000b:
            androidx.constraintlayout.core.widgets.Helper r0 = r7.mHelperWidget
            if (r0 != 0) goto L_0x0010
            return
        L_0x0010:
            r0.removeAllIds()
            r0 = 0
        L_0x0014:
            int r1 = r7.mCount
            if (r0 >= r1) goto L_0x0053
            int[] r1 = r7.mIds
            r1 = r1[r0]
            android.view.View r2 = r8.getViewById(r1)
            if (r2 != 0) goto L_0x0045
            java.util.HashMap<java.lang.Integer, java.lang.String> r3 = r7.mMap
            java.lang.Integer r4 = java.lang.Integer.valueOf(r1)
            java.lang.Object r3 = r3.get(r4)
            java.lang.String r3 = (java.lang.String) r3
            int r4 = r7.findId(r8, r3)
            if (r4 == 0) goto L_0x0045
            int[] r5 = r7.mIds
            r5[r0] = r4
            java.util.HashMap<java.lang.Integer, java.lang.String> r5 = r7.mMap
            java.lang.Integer r6 = java.lang.Integer.valueOf(r4)
            r5.put(r6, r3)
            android.view.View r2 = r8.getViewById(r4)
        L_0x0045:
            if (r2 == 0) goto L_0x0050
            androidx.constraintlayout.core.widgets.Helper r3 = r7.mHelperWidget
            androidx.constraintlayout.core.widgets.ConstraintWidget r4 = r8.getViewWidget(r2)
            r3.add(r4)
        L_0x0050:
            int r0 = r0 + 1
            goto L_0x0014
        L_0x0053:
            androidx.constraintlayout.core.widgets.Helper r0 = r7.mHelperWidget
            androidx.constraintlayout.core.widgets.ConstraintWidgetContainer r1 = r8.mLayoutWidget
            r0.updateConstraints(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.ConstraintHelper.updatePreLayout(androidx.constraintlayout.widget.ConstraintLayout):void");
    }

    public void validateParams() {
        if (this.mHelperWidget != null) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (layoutParams instanceof ConstraintLayout.LayoutParams) {
                ((ConstraintLayout.LayoutParams) layoutParams).widget = (ConstraintWidget) this.mHelperWidget;
            }
        }
    }
}
