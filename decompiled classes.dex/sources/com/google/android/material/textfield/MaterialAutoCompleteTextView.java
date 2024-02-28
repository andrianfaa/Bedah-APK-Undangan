package com.google.android.material.textfield;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.ListPopupWindow;
import com.google.android.material.R;
import com.google.android.material.internal.ManufacturerUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import okhttp3.HttpUrl;

public class MaterialAutoCompleteTextView extends AppCompatAutoCompleteTextView {
    private static final int MAX_ITEMS_MEASURED = 15;
    private final AccessibilityManager accessibilityManager;
    /* access modifiers changed from: private */
    public final ListPopupWindow modalListPopup;
    private final int simpleItemLayout;
    private final Rect tempRect;

    public MaterialAutoCompleteTextView(Context context) {
        this(context, (AttributeSet) null);
    }

    public MaterialAutoCompleteTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.autoCompleteTextViewStyle);
    }

    public MaterialAutoCompleteTextView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, defStyleAttr, 0), attributeSet, defStyleAttr);
        this.tempRect = new Rect();
        Context context2 = getContext();
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R.styleable.MaterialAutoCompleteTextView, defStyleAttr, R.style.Widget_AppCompat_AutoCompleteTextView, new int[0]);
        if (obtainStyledAttributes.hasValue(R.styleable.MaterialAutoCompleteTextView_android_inputType) && obtainStyledAttributes.getInt(R.styleable.MaterialAutoCompleteTextView_android_inputType, 0) == 0) {
            setKeyListener((KeyListener) null);
        }
        this.simpleItemLayout = obtainStyledAttributes.getResourceId(R.styleable.MaterialAutoCompleteTextView_simpleItemLayout, R.layout.mtrl_auto_complete_simple_item);
        this.accessibilityManager = (AccessibilityManager) context2.getSystemService("accessibility");
        ListPopupWindow listPopupWindow = new ListPopupWindow(context2);
        this.modalListPopup = listPopupWindow;
        listPopupWindow.setModal(true);
        listPopupWindow.setAnchorView(this);
        listPopupWindow.setInputMethodMode(2);
        listPopupWindow.setAdapter(getAdapter());
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View selectedView, int position, long id) {
                MaterialAutoCompleteTextView materialAutoCompleteTextView = MaterialAutoCompleteTextView.this;
                MaterialAutoCompleteTextView.this.updateText(position < 0 ? materialAutoCompleteTextView.modalListPopup.getSelectedItem() : materialAutoCompleteTextView.getAdapter().getItem(position));
                AdapterView.OnItemClickListener onItemClickListener = MaterialAutoCompleteTextView.this.getOnItemClickListener();
                if (onItemClickListener != null) {
                    if (selectedView == null || position < 0) {
                        selectedView = MaterialAutoCompleteTextView.this.modalListPopup.getSelectedView();
                        position = MaterialAutoCompleteTextView.this.modalListPopup.getSelectedItemPosition();
                        id = MaterialAutoCompleteTextView.this.modalListPopup.getSelectedItemId();
                    }
                    onItemClickListener.onItemClick(MaterialAutoCompleteTextView.this.modalListPopup.getListView(), selectedView, position, id);
                }
                MaterialAutoCompleteTextView.this.modalListPopup.dismiss();
            }
        });
        if (obtainStyledAttributes.hasValue(R.styleable.MaterialAutoCompleteTextView_simpleItems)) {
            setSimpleItems(obtainStyledAttributes.getResourceId(R.styleable.MaterialAutoCompleteTextView_simpleItems, 0));
        }
        obtainStyledAttributes.recycle();
    }

    private TextInputLayout findTextInputLayoutAncestor() {
        for (ViewParent parent = getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof TextInputLayout) {
                return (TextInputLayout) parent;
            }
        }
        return null;
    }

    private int measureContentWidth() {
        ListAdapter adapter = getAdapter();
        TextInputLayout findTextInputLayoutAncestor = findTextInputLayoutAncestor();
        if (adapter == null || findTextInputLayoutAncestor == null) {
            return 0;
        }
        int i = 0;
        View view = null;
        int i2 = 0;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 0);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0);
        int min = Math.min(adapter.getCount(), Math.max(0, this.modalListPopup.getSelectedItemPosition()) + 15);
        for (int max = Math.max(0, min - 15); max < min; max++) {
            int itemViewType = adapter.getItemViewType(max);
            if (itemViewType != i2) {
                i2 = itemViewType;
                view = null;
            }
            view = adapter.getView(max, view, findTextInputLayoutAncestor);
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
            }
            view.measure(makeMeasureSpec, makeMeasureSpec2);
            i = Math.max(i, view.getMeasuredWidth());
        }
        Drawable background = this.modalListPopup.getBackground();
        if (background != null) {
            background.getPadding(this.tempRect);
            i += this.tempRect.left + this.tempRect.right;
        }
        return i + findTextInputLayoutAncestor.getEndIconView().getMeasuredWidth();
    }

    /* access modifiers changed from: private */
    public <T extends ListAdapter & Filterable> void updateText(Object selectedItem) {
        if (Build.VERSION.SDK_INT >= 17) {
            setText(convertSelectionToString(selectedItem), false);
            return;
        }
        ListAdapter adapter = getAdapter();
        setAdapter((ListAdapter) null);
        setText(convertSelectionToString(selectedItem));
        setAdapter(adapter);
    }

    public CharSequence getHint() {
        TextInputLayout findTextInputLayoutAncestor = findTextInputLayoutAncestor();
        return (findTextInputLayoutAncestor == null || !findTextInputLayoutAncestor.isProvidingHint()) ? super.getHint() : findTextInputLayoutAncestor.getHint();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        TextInputLayout findTextInputLayoutAncestor = findTextInputLayoutAncestor();
        if (findTextInputLayoutAncestor != null && findTextInputLayoutAncestor.isProvidingHint() && super.getHint() == null && ManufacturerUtils.isMeizuDevice()) {
            setHint(HttpUrl.FRAGMENT_ENCODE_SET);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (View.MeasureSpec.getMode(widthMeasureSpec) == Integer.MIN_VALUE) {
            setMeasuredDimension(Math.min(Math.max(getMeasuredWidth(), measureContentWidth()), View.MeasureSpec.getSize(widthMeasureSpec)), getMeasuredHeight());
        }
    }

    public <T extends ListAdapter & Filterable> void setAdapter(T t) {
        super.setAdapter(t);
        this.modalListPopup.setAdapter(getAdapter());
    }

    public void setSimpleItems(int stringArrayResId) {
        setSimpleItems(getResources().getStringArray(stringArrayResId));
    }

    public void setSimpleItems(String[] stringArray) {
        setAdapter(new ArrayAdapter(getContext(), this.simpleItemLayout, stringArray));
    }

    public void showDropDown() {
        AccessibilityManager accessibilityManager2 = this.accessibilityManager;
        if (accessibilityManager2 == null || !accessibilityManager2.isTouchExplorationEnabled()) {
            super.showDropDown();
        } else {
            this.modalListPopup.show();
        }
    }
}
