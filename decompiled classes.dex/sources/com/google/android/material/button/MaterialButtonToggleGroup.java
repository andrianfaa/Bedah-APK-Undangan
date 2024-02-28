package com.google.android.material.button;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.shape.AbsoluteCornerSize;
import com.google.android.material.shape.CornerSize;
import com.google.android.material.shape.ShapeAppearanceModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class MaterialButtonToggleGroup extends LinearLayout {
    private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_MaterialButtonToggleGroup;
    private static final String LOG_TAG = MaterialButtonToggleGroup.class.getSimpleName();
    private Set<Integer> checkedIds;
    private Integer[] childOrder;
    private final Comparator<MaterialButton> childOrderComparator;
    private final int defaultCheckId;
    private final LinkedHashSet<OnButtonCheckedListener> onButtonCheckedListeners;
    private final List<CornerData> originalCornerData;
    private final PressedStateTracker pressedStateTracker;
    private boolean selectionRequired;
    private boolean singleSelection;
    private boolean skipCheckedStateTracker;

    private static class CornerData {
        private static final CornerSize noCorner = new AbsoluteCornerSize(0.0f);
        CornerSize bottomLeft;
        CornerSize bottomRight;
        CornerSize topLeft;
        CornerSize topRight;

        CornerData(CornerSize topLeft2, CornerSize bottomLeft2, CornerSize topRight2, CornerSize bottomRight2) {
            this.topLeft = topLeft2;
            this.topRight = topRight2;
            this.bottomRight = bottomRight2;
            this.bottomLeft = bottomLeft2;
        }

        public static CornerData bottom(CornerData orig) {
            CornerSize cornerSize = noCorner;
            return new CornerData(cornerSize, orig.bottomLeft, cornerSize, orig.bottomRight);
        }

        public static CornerData end(CornerData orig, View view) {
            return ViewUtils.isLayoutRtl(view) ? left(orig) : right(orig);
        }

        public static CornerData left(CornerData orig) {
            CornerSize cornerSize = orig.topLeft;
            CornerSize cornerSize2 = orig.bottomLeft;
            CornerSize cornerSize3 = noCorner;
            return new CornerData(cornerSize, cornerSize2, cornerSize3, cornerSize3);
        }

        public static CornerData right(CornerData orig) {
            CornerSize cornerSize = noCorner;
            return new CornerData(cornerSize, cornerSize, orig.topRight, orig.bottomRight);
        }

        public static CornerData start(CornerData orig, View view) {
            return ViewUtils.isLayoutRtl(view) ? right(orig) : left(orig);
        }

        public static CornerData top(CornerData orig) {
            CornerSize cornerSize = orig.topLeft;
            CornerSize cornerSize2 = noCorner;
            return new CornerData(cornerSize, cornerSize2, orig.topRight, cornerSize2);
        }
    }

    public interface OnButtonCheckedListener {
        void onButtonChecked(MaterialButtonToggleGroup materialButtonToggleGroup, int i, boolean z);
    }

    private class PressedStateTracker implements MaterialButton.OnPressedChangeListener {
        private PressedStateTracker() {
        }

        public void onPressedChanged(MaterialButton button, boolean isPressed) {
            MaterialButtonToggleGroup.this.invalidate();
        }
    }

    public MaterialButtonToggleGroup(Context context) {
        this(context, (AttributeSet) null);
    }

    public MaterialButtonToggleGroup(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.materialButtonToggleGroupStyle);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public MaterialButtonToggleGroup(android.content.Context r8, android.util.AttributeSet r9, int r10) {
        /*
            r7 = this;
            int r4 = DEF_STYLE_RES
            android.content.Context r0 = com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap(r8, r9, r10, r4)
            r7.<init>(r0, r9, r10)
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r7.originalCornerData = r0
            com.google.android.material.button.MaterialButtonToggleGroup$PressedStateTracker r0 = new com.google.android.material.button.MaterialButtonToggleGroup$PressedStateTracker
            r1 = 0
            r0.<init>()
            r7.pressedStateTracker = r0
            java.util.LinkedHashSet r0 = new java.util.LinkedHashSet
            r0.<init>()
            r7.onButtonCheckedListeners = r0
            com.google.android.material.button.MaterialButtonToggleGroup$1 r0 = new com.google.android.material.button.MaterialButtonToggleGroup$1
            r0.<init>()
            r7.childOrderComparator = r0
            r6 = 0
            r7.skipCheckedStateTracker = r6
            java.util.HashSet r0 = new java.util.HashSet
            r0.<init>()
            r7.checkedIds = r0
            android.content.Context r8 = r7.getContext()
            int[] r2 = com.google.android.material.R.styleable.MaterialButtonToggleGroup
            int[] r5 = new int[r6]
            r0 = r8
            r1 = r9
            r3 = r10
            android.content.res.TypedArray r0 = com.google.android.material.internal.ThemeEnforcement.obtainStyledAttributes(r0, r1, r2, r3, r4, r5)
            int r1 = com.google.android.material.R.styleable.MaterialButtonToggleGroup_singleSelection
            boolean r1 = r0.getBoolean(r1, r6)
            r7.setSingleSelection((boolean) r1)
            int r1 = com.google.android.material.R.styleable.MaterialButtonToggleGroup_checkedButton
            r2 = -1
            int r1 = r0.getResourceId(r1, r2)
            r7.defaultCheckId = r1
            int r1 = com.google.android.material.R.styleable.MaterialButtonToggleGroup_selectionRequired
            boolean r1 = r0.getBoolean(r1, r6)
            r7.selectionRequired = r1
            r1 = 1
            r7.setChildrenDrawingOrderEnabled(r1)
            r0.recycle()
            androidx.core.view.ViewCompat.setImportantForAccessibility(r7, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.button.MaterialButtonToggleGroup.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private void adjustChildMarginsAndUpdateLayout() {
        int firstVisibleChildIndex = getFirstVisibleChildIndex();
        if (firstVisibleChildIndex != -1) {
            for (int i = firstVisibleChildIndex + 1; i < getChildCount(); i++) {
                MaterialButton childButton = getChildButton(i);
                int min = Math.min(childButton.getStrokeWidth(), getChildButton(i - 1).getStrokeWidth());
                LinearLayout.LayoutParams buildLayoutParams = buildLayoutParams(childButton);
                if (getOrientation() == 0) {
                    MarginLayoutParamsCompat.setMarginEnd(buildLayoutParams, 0);
                    MarginLayoutParamsCompat.setMarginStart(buildLayoutParams, -min);
                    buildLayoutParams.topMargin = 0;
                } else {
                    buildLayoutParams.bottomMargin = 0;
                    buildLayoutParams.topMargin = -min;
                    MarginLayoutParamsCompat.setMarginStart(buildLayoutParams, 0);
                }
                childButton.setLayoutParams(buildLayoutParams);
            }
            resetChildMargins(firstVisibleChildIndex);
        }
    }

    private LinearLayout.LayoutParams buildLayoutParams(View child) {
        ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
        return layoutParams instanceof LinearLayout.LayoutParams ? (LinearLayout.LayoutParams) layoutParams : new LinearLayout.LayoutParams(layoutParams.width, layoutParams.height);
    }

    private void checkInternal(int buttonId, boolean checked) {
        if (buttonId == -1) {
            Log.e(LOG_TAG, "Button ID is not valid: " + buttonId);
            return;
        }
        HashSet hashSet = new HashSet(this.checkedIds);
        if (checked && !hashSet.contains(Integer.valueOf(buttonId))) {
            if (this.singleSelection && !hashSet.isEmpty()) {
                hashSet.clear();
            }
            hashSet.add(Integer.valueOf(buttonId));
        } else if (!checked && hashSet.contains(Integer.valueOf(buttonId))) {
            if (!this.selectionRequired || hashSet.size() > 1) {
                hashSet.remove(Integer.valueOf(buttonId));
            }
        } else {
            return;
        }
        updateCheckedIds(hashSet);
    }

    private void dispatchOnButtonChecked(int buttonId, boolean checked) {
        Iterator it = this.onButtonCheckedListeners.iterator();
        while (it.hasNext()) {
            ((OnButtonCheckedListener) it.next()).onButtonChecked(this, buttonId, checked);
        }
    }

    private MaterialButton getChildButton(int index) {
        return (MaterialButton) getChildAt(index);
    }

    private int getFirstVisibleChildIndex() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (isChildVisible(i)) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: private */
    public int getIndexWithinVisibleButtons(View child) {
        if (!(child instanceof MaterialButton)) {
            return -1;
        }
        int i = 0;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            if (getChildAt(i2) == child) {
                return i;
            }
            if ((getChildAt(i2) instanceof MaterialButton) && isChildVisible(i2)) {
                i++;
            }
        }
        return -1;
    }

    private int getLastVisibleChildIndex() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            if (isChildVisible(childCount)) {
                return childCount;
            }
        }
        return -1;
    }

    private CornerData getNewCornerData(int index, int firstVisibleChildIndex, int lastVisibleChildIndex) {
        CornerData cornerData = this.originalCornerData.get(index);
        if (firstVisibleChildIndex == lastVisibleChildIndex) {
            return cornerData;
        }
        boolean z = getOrientation() == 0;
        if (index == firstVisibleChildIndex) {
            return z ? CornerData.start(cornerData, this) : CornerData.top(cornerData);
        }
        if (index == lastVisibleChildIndex) {
            return z ? CornerData.end(cornerData, this) : CornerData.bottom(cornerData);
        }
        return null;
    }

    private int getVisibleButtonCount() {
        int i = 0;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            if ((getChildAt(i2) instanceof MaterialButton) && isChildVisible(i2)) {
                i++;
            }
        }
        return i;
    }

    private boolean isChildVisible(int i) {
        return getChildAt(i).getVisibility() != 8;
    }

    private void resetChildMargins(int childIndex) {
        if (getChildCount() != 0 && childIndex != -1) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getChildButton(childIndex).getLayoutParams();
            if (getOrientation() == 1) {
                layoutParams.topMargin = 0;
                layoutParams.bottomMargin = 0;
                return;
            }
            MarginLayoutParamsCompat.setMarginEnd(layoutParams, 0);
            MarginLayoutParamsCompat.setMarginStart(layoutParams, 0);
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View findViewById = findViewById(viewId);
        if (findViewById instanceof MaterialButton) {
            this.skipCheckedStateTracker = true;
            ((MaterialButton) findViewById).setChecked(checked);
            this.skipCheckedStateTracker = false;
        }
    }

    private void setGeneratedIdIfNeeded(MaterialButton materialButton) {
        if (materialButton.getId() == -1) {
            materialButton.setId(ViewCompat.generateViewId());
        }
    }

    private void setupButtonChild(MaterialButton buttonChild) {
        buttonChild.setMaxLines(1);
        buttonChild.setEllipsize(TextUtils.TruncateAt.END);
        buttonChild.setCheckable(true);
        buttonChild.setOnPressedChangeListenerInternal(this.pressedStateTracker);
        buttonChild.setShouldDrawSurfaceColorStroke(true);
    }

    private static void updateBuilderWithCornerData(ShapeAppearanceModel.Builder shapeAppearanceModelBuilder, CornerData cornerData) {
        if (cornerData == null) {
            shapeAppearanceModelBuilder.setAllCornerSizes(0.0f);
        } else {
            shapeAppearanceModelBuilder.setTopLeftCornerSize(cornerData.topLeft).setBottomLeftCornerSize(cornerData.bottomLeft).setTopRightCornerSize(cornerData.topRight).setBottomRightCornerSize(cornerData.bottomRight);
        }
    }

    private void updateCheckedIds(Set<Integer> set) {
        Set<Integer> set2 = this.checkedIds;
        this.checkedIds = new HashSet(set);
        for (int i = 0; i < getChildCount(); i++) {
            int id = getChildButton(i).getId();
            setCheckedStateForView(id, set.contains(Integer.valueOf(id)));
            if (set2.contains(Integer.valueOf(id)) != set.contains(Integer.valueOf(id))) {
                dispatchOnButtonChecked(id, set.contains(Integer.valueOf(id)));
            }
        }
        invalidate();
    }

    private void updateChildOrder() {
        TreeMap treeMap = new TreeMap(this.childOrderComparator);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            treeMap.put(getChildButton(i), Integer.valueOf(i));
        }
        this.childOrder = (Integer[]) treeMap.values().toArray(new Integer[0]);
    }

    public void addOnButtonCheckedListener(OnButtonCheckedListener listener) {
        this.onButtonCheckedListeners.add(listener);
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (!(child instanceof MaterialButton)) {
            Log.e(LOG_TAG, "Child views must be of type MaterialButton.");
            return;
        }
        super.addView(child, index, params);
        MaterialButton materialButton = (MaterialButton) child;
        setGeneratedIdIfNeeded(materialButton);
        setupButtonChild(materialButton);
        checkInternal(materialButton.getId(), materialButton.isChecked());
        ShapeAppearanceModel shapeAppearanceModel = materialButton.getShapeAppearanceModel();
        this.originalCornerData.add(new CornerData(shapeAppearanceModel.getTopLeftCornerSize(), shapeAppearanceModel.getBottomLeftCornerSize(), shapeAppearanceModel.getTopRightCornerSize(), shapeAppearanceModel.getBottomRightCornerSize()));
        ViewCompat.setAccessibilityDelegate(materialButton, new AccessibilityDelegateCompat() {
            public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
                super.onInitializeAccessibilityNodeInfo(host, info);
                info.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(0, 1, MaterialButtonToggleGroup.this.getIndexWithinVisibleButtons(host), 1, false, ((MaterialButton) host).isChecked()));
            }
        });
    }

    public void check(int id) {
        checkInternal(id, true);
    }

    public void clearChecked() {
        updateCheckedIds(new HashSet());
    }

    public void clearOnButtonCheckedListeners() {
        this.onButtonCheckedListeners.clear();
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        updateChildOrder();
        super.dispatchDraw(canvas);
    }

    public int getCheckedButtonId() {
        if (!this.singleSelection || this.checkedIds.isEmpty()) {
            return -1;
        }
        return this.checkedIds.iterator().next().intValue();
    }

    public List<Integer> getCheckedButtonIds() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < getChildCount(); i++) {
            int id = getChildButton(i).getId();
            if (this.checkedIds.contains(Integer.valueOf(id))) {
                arrayList.add(Integer.valueOf(id));
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public int getChildDrawingOrder(int childCount, int i) {
        Integer[] numArr = this.childOrder;
        if (numArr != null && i < numArr.length) {
            return numArr[i].intValue();
        }
        Log.w(LOG_TAG, "Child order wasn't updated");
        return i;
    }

    public boolean isSelectionRequired() {
        return this.selectionRequired;
    }

    public boolean isSingleSelection() {
        return this.singleSelection;
    }

    /* access modifiers changed from: package-private */
    public void onButtonCheckedStateChanged(MaterialButton button, boolean isChecked) {
        if (!this.skipCheckedStateTracker) {
            checkInternal(button.getId(), isChecked);
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        int i = this.defaultCheckId;
        if (i != -1) {
            updateCheckedIds(Collections.singleton(Integer.valueOf(i)));
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        AccessibilityNodeInfoCompat.wrap(info).setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(1, getVisibleButtonCount(), false, isSingleSelection() ? 1 : 2));
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        updateChildShapes();
        adjustChildMarginsAndUpdateLayout();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        if (child instanceof MaterialButton) {
            ((MaterialButton) child).setOnPressedChangeListenerInternal((MaterialButton.OnPressedChangeListener) null);
        }
        int indexOfChild = indexOfChild(child);
        if (indexOfChild >= 0) {
            this.originalCornerData.remove(indexOfChild);
        }
        updateChildShapes();
        adjustChildMarginsAndUpdateLayout();
    }

    public void removeOnButtonCheckedListener(OnButtonCheckedListener listener) {
        this.onButtonCheckedListeners.remove(listener);
    }

    public void setSelectionRequired(boolean selectionRequired2) {
        this.selectionRequired = selectionRequired2;
    }

    public void setSingleSelection(int id) {
        setSingleSelection(getResources().getBoolean(id));
    }

    public void setSingleSelection(boolean singleSelection2) {
        if (this.singleSelection != singleSelection2) {
            this.singleSelection = singleSelection2;
            clearChecked();
        }
    }

    public void uncheck(int id) {
        checkInternal(id, false);
    }

    /* access modifiers changed from: package-private */
    public void updateChildShapes() {
        int childCount = getChildCount();
        int firstVisibleChildIndex = getFirstVisibleChildIndex();
        int lastVisibleChildIndex = getLastVisibleChildIndex();
        for (int i = 0; i < childCount; i++) {
            MaterialButton childButton = getChildButton(i);
            if (childButton.getVisibility() != 8) {
                ShapeAppearanceModel.Builder builder = childButton.getShapeAppearanceModel().toBuilder();
                updateBuilderWithCornerData(builder, getNewCornerData(i, firstVisibleChildIndex, lastVisibleChildIndex));
                childButton.setShapeAppearanceModel(builder.build());
            }
        }
    }
}
