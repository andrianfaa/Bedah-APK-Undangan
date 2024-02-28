package androidx.fragment.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.R;
import mt.Log1F380D;

/* compiled from: 0077 */
class FragmentLayoutInflaterFactory implements LayoutInflater.Factory2 {
    private static final String TAG = "FragmentManager";
    final FragmentManager mFragmentManager;

    FragmentLayoutInflaterFactory(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        final FragmentStateManager fragmentStateManager;
        String str = name;
        Context context2 = context;
        AttributeSet attributeSet = attrs;
        if (FragmentContainerView.class.getName().equals(str)) {
            return new FragmentContainerView(context2, attributeSet, this.mFragmentManager);
        }
        Fragment fragment = null;
        if (!"fragment".equals(str)) {
            return null;
        }
        String attributeValue = attributeSet.getAttributeValue((String) null, "class");
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, R.styleable.Fragment);
        if (attributeValue == null) {
            attributeValue = obtainStyledAttributes.getString(R.styleable.Fragment_android_name);
        }
        int resourceId = obtainStyledAttributes.getResourceId(R.styleable.Fragment_android_id, -1);
        String string = obtainStyledAttributes.getString(R.styleable.Fragment_android_tag);
        obtainStyledAttributes.recycle();
        if (attributeValue == null || !FragmentFactory.isFragmentClass(context.getClassLoader(), attributeValue)) {
            return null;
        }
        int id = parent != null ? parent.getId() : 0;
        if (id == -1 && resourceId == -1 && string == null) {
            throw new IllegalArgumentException(attrs.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + attributeValue);
        }
        if (resourceId != -1) {
            fragment = this.mFragmentManager.findFragmentById(resourceId);
        }
        if (fragment == null && string != null) {
            fragment = this.mFragmentManager.findFragmentByTag(string);
        }
        if (fragment == null && id != -1) {
            fragment = this.mFragmentManager.findFragmentById(id);
        }
        if (fragment == null) {
            fragment = this.mFragmentManager.getFragmentFactory().instantiate(context.getClassLoader(), attributeValue);
            fragment.mFromLayout = true;
            fragment.mFragmentId = resourceId != 0 ? resourceId : id;
            fragment.mContainerId = id;
            fragment.mTag = string;
            fragment.mInLayout = true;
            fragment.mFragmentManager = this.mFragmentManager;
            fragment.mHost = this.mFragmentManager.getHost();
            fragment.onInflate(this.mFragmentManager.getHost().getContext(), attributeSet, fragment.mSavedFragmentState);
            fragmentStateManager = this.mFragmentManager.addFragment(fragment);
            if (FragmentManager.isLoggingEnabled(2)) {
                StringBuilder append = new StringBuilder().append("Fragment ").append(fragment).append(" has been inflated via the <fragment> tag: id=0x");
                String hexString = Integer.toHexString(resourceId);
                Log1F380D.a((Object) hexString);
                Log.v(TAG, append.append(hexString).toString());
            }
        } else if (!fragment.mInLayout) {
            fragment.mInLayout = true;
            fragment.mFragmentManager = this.mFragmentManager;
            fragment.mHost = this.mFragmentManager.getHost();
            fragment.onInflate(this.mFragmentManager.getHost().getContext(), attributeSet, fragment.mSavedFragmentState);
            fragmentStateManager = this.mFragmentManager.createOrGetFragmentStateManager(fragment);
            if (FragmentManager.isLoggingEnabled(2)) {
                StringBuilder append2 = new StringBuilder().append("Retained Fragment ").append(fragment).append(" has been re-attached via the <fragment> tag: id=0x");
                String hexString2 = Integer.toHexString(resourceId);
                Log1F380D.a((Object) hexString2);
                Log.v(TAG, append2.append(hexString2).toString());
            }
        } else {
            StringBuilder append3 = new StringBuilder().append(attrs.getPositionDescription()).append(": Duplicate id 0x");
            String hexString3 = Integer.toHexString(resourceId);
            Log1F380D.a((Object) hexString3);
            StringBuilder append4 = append3.append(hexString3).append(", tag ").append(string).append(", or parent id 0x");
            String hexString4 = Integer.toHexString(id);
            Log1F380D.a((Object) hexString4);
            throw new IllegalArgumentException(append4.append(hexString4).append(" with another fragment for ").append(attributeValue).toString());
        }
        fragment.mContainer = (ViewGroup) parent;
        fragmentStateManager.moveToExpectedState();
        fragmentStateManager.ensureInflatedView();
        if (fragment.mView != null) {
            if (resourceId != 0) {
                fragment.mView.setId(resourceId);
            }
            if (fragment.mView.getTag() == null) {
                fragment.mView.setTag(string);
            }
            fragment.mView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                public void onViewAttachedToWindow(View v) {
                    Fragment fragment = fragmentStateManager.getFragment();
                    fragmentStateManager.moveToExpectedState();
                    SpecialEffectsController.getOrCreateController((ViewGroup) fragment.mView.getParent(), FragmentLayoutInflaterFactory.this.mFragmentManager).forceCompleteAllOperations();
                }

                public void onViewDetachedFromWindow(View v) {
                }
            });
            return fragment.mView;
        }
        throw new IllegalStateException("Fragment " + attributeValue + " did not create a view.");
    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return onCreateView((View) null, name, context, attrs);
    }
}
