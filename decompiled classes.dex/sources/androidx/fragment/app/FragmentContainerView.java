package androidx.fragment.app;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.R;
import java.util.ArrayList;
import okhttp3.HttpUrl;

public final class FragmentContainerView extends FrameLayout {
    private View.OnApplyWindowInsetsListener mApplyWindowInsetsListener;
    private ArrayList<View> mDisappearingFragmentChildren;
    private boolean mDrawDisappearingViewsFirst;
    private ArrayList<View> mTransitioningFragmentViews;

    public FragmentContainerView(Context context) {
        super(context);
        this.mDrawDisappearingViewsFirst = true;
    }

    public FragmentContainerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FragmentContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mDrawDisappearingViewsFirst = true;
        if (attrs != null) {
            String classAttribute = attrs.getClassAttribute();
            String str = "class";
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.FragmentContainerView);
            if (classAttribute == null) {
                classAttribute = obtainStyledAttributes.getString(R.styleable.FragmentContainerView_android_name);
                str = "android:name";
            }
            obtainStyledAttributes.recycle();
            if (classAttribute != null && !isInEditMode()) {
                throw new UnsupportedOperationException("FragmentContainerView must be within a FragmentActivity to use " + str + "=\"" + classAttribute + "\"");
            }
        }
    }

    FragmentContainerView(Context context, AttributeSet attrs, FragmentManager fm) {
        super(context, attrs);
        this.mDrawDisappearingViewsFirst = true;
        String classAttribute = attrs.getClassAttribute();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.FragmentContainerView);
        classAttribute = classAttribute == null ? obtainStyledAttributes.getString(R.styleable.FragmentContainerView_android_name) : classAttribute;
        String string = obtainStyledAttributes.getString(R.styleable.FragmentContainerView_android_tag);
        obtainStyledAttributes.recycle();
        int id = getId();
        Fragment findFragmentById = fm.findFragmentById(id);
        if (classAttribute != null && findFragmentById == null) {
            if (id <= 0) {
                throw new IllegalStateException("FragmentContainerView must have an android:id to add Fragment " + classAttribute + (string != null ? " with tag " + string : HttpUrl.FRAGMENT_ENCODE_SET));
            }
            Fragment instantiate = fm.getFragmentFactory().instantiate(context.getClassLoader(), classAttribute);
            instantiate.onInflate(context, attrs, (Bundle) null);
            fm.beginTransaction().setReorderingAllowed(true).add((ViewGroup) this, instantiate, string).commitNowAllowingStateLoss();
        }
        fm.onContainerAvailable(this);
    }

    private void addDisappearingFragmentView(View v) {
        ArrayList<View> arrayList = this.mTransitioningFragmentViews;
        if (arrayList != null && arrayList.contains(v)) {
            if (this.mDisappearingFragmentChildren == null) {
                this.mDisappearingFragmentChildren = new ArrayList<>();
            }
            this.mDisappearingFragmentChildren.add(v);
        }
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (FragmentManager.getViewFragment(child) != null) {
            super.addView(child, index, params);
            return;
        }
        throw new IllegalStateException("Views added to a FragmentContainerView must be associated with a Fragment. View " + child + " is not associated with a Fragment.");
    }

    /* access modifiers changed from: protected */
    public boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        if (FragmentManager.getViewFragment(child) != null) {
            return super.addViewInLayout(child, index, params, preventRequestLayout);
        }
        throw new IllegalStateException("Views added to a FragmentContainerView must be associated with a Fragment. View " + child + " is not associated with a Fragment.");
    }

    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        WindowInsetsCompat windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(insets);
        View.OnApplyWindowInsetsListener onApplyWindowInsetsListener = this.mApplyWindowInsetsListener;
        WindowInsetsCompat windowInsetsCompat2 = onApplyWindowInsetsListener != null ? WindowInsetsCompat.toWindowInsetsCompat(onApplyWindowInsetsListener.onApplyWindowInsets(this, insets)) : ViewCompat.onApplyWindowInsets(this, windowInsetsCompat);
        if (!windowInsetsCompat2.isConsumed()) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                ViewCompat.dispatchApplyWindowInsets(getChildAt(i), windowInsetsCompat2);
            }
        }
        return insets;
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        if (this.mDrawDisappearingViewsFirst && this.mDisappearingFragmentChildren != null) {
            for (int i = 0; i < this.mDisappearingFragmentChildren.size(); i++) {
                super.drawChild(canvas, this.mDisappearingFragmentChildren.get(i), getDrawingTime());
            }
        }
        super.dispatchDraw(canvas);
    }

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        ArrayList<View> arrayList;
        if (!this.mDrawDisappearingViewsFirst || (arrayList = this.mDisappearingFragmentChildren) == null || arrayList.size() <= 0 || !this.mDisappearingFragmentChildren.contains(child)) {
            return super.drawChild(canvas, child, drawingTime);
        }
        return false;
    }

    public void endViewTransition(View view) {
        ArrayList<View> arrayList = this.mTransitioningFragmentViews;
        if (arrayList != null) {
            arrayList.remove(view);
            ArrayList<View> arrayList2 = this.mDisappearingFragmentChildren;
            if (arrayList2 != null && arrayList2.remove(view)) {
                this.mDrawDisappearingViewsFirst = true;
            }
        }
        super.endViewTransition(view);
    }

    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        return insets;
    }

    public void removeAllViewsInLayout() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            addDisappearingFragmentView(getChildAt(childCount));
        }
        super.removeAllViewsInLayout();
    }

    /* access modifiers changed from: protected */
    public void removeDetachedView(View child, boolean animate) {
        if (animate) {
            addDisappearingFragmentView(child);
        }
        super.removeDetachedView(child, animate);
    }

    public void removeView(View view) {
        addDisappearingFragmentView(view);
        super.removeView(view);
    }

    public void removeViewAt(int index) {
        addDisappearingFragmentView(getChildAt(index));
        super.removeViewAt(index);
    }

    public void removeViewInLayout(View view) {
        addDisappearingFragmentView(view);
        super.removeViewInLayout(view);
    }

    public void removeViews(int start, int count) {
        for (int i = start; i < start + count; i++) {
            addDisappearingFragmentView(getChildAt(i));
        }
        super.removeViews(start, count);
    }

    public void removeViewsInLayout(int start, int count) {
        for (int i = start; i < start + count; i++) {
            addDisappearingFragmentView(getChildAt(i));
        }
        super.removeViewsInLayout(start, count);
    }

    /* access modifiers changed from: package-private */
    public void setDrawDisappearingViewsLast(boolean drawDisappearingViewsFirst) {
        this.mDrawDisappearingViewsFirst = drawDisappearingViewsFirst;
    }

    public void setLayoutTransition(LayoutTransition transition) {
        if (Build.VERSION.SDK_INT < 18) {
            super.setLayoutTransition(transition);
            return;
        }
        throw new UnsupportedOperationException("FragmentContainerView does not support Layout Transitions or animateLayoutChanges=\"true\".");
    }

    public void setOnApplyWindowInsetsListener(View.OnApplyWindowInsetsListener listener) {
        this.mApplyWindowInsetsListener = listener;
    }

    public void startViewTransition(View view) {
        if (view.getParent() == this) {
            if (this.mTransitioningFragmentViews == null) {
                this.mTransitioningFragmentViews = new ArrayList<>();
            }
            this.mTransitioningFragmentViews.add(view);
        }
        super.startViewTransition(view);
    }
}
