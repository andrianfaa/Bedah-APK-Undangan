package androidx.fragment.app;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryController;
import androidx.savedstate.SavedStateRegistryOwner;

class FragmentViewLifecycleOwner implements HasDefaultViewModelProviderFactory, SavedStateRegistryOwner, ViewModelStoreOwner {
    private ViewModelProvider.Factory mDefaultFactory;
    private final Fragment mFragment;
    private LifecycleRegistry mLifecycleRegistry = null;
    private SavedStateRegistryController mSavedStateRegistryController = null;
    private final ViewModelStore mViewModelStore;

    FragmentViewLifecycleOwner(Fragment fragment, ViewModelStore viewModelStore) {
        this.mFragment = fragment;
        this.mViewModelStore = viewModelStore;
    }

    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        ViewModelProvider.Factory defaultViewModelProviderFactory = this.mFragment.getDefaultViewModelProviderFactory();
        if (!defaultViewModelProviderFactory.equals(this.mFragment.mDefaultFactory)) {
            this.mDefaultFactory = defaultViewModelProviderFactory;
            return defaultViewModelProviderFactory;
        }
        if (this.mDefaultFactory == null) {
            Application application = null;
            Context context = this.mFragment.requireContext().getApplicationContext();
            while (true) {
                if (!(context instanceof ContextWrapper)) {
                    break;
                } else if (context instanceof Application) {
                    application = (Application) context;
                    break;
                } else {
                    context = ((ContextWrapper) context).getBaseContext();
                }
            }
            this.mDefaultFactory = new SavedStateViewModelFactory(application, this, this.mFragment.getArguments());
        }
        return this.mDefaultFactory;
    }

    public Lifecycle getLifecycle() {
        initialize();
        return this.mLifecycleRegistry;
    }

    public SavedStateRegistry getSavedStateRegistry() {
        initialize();
        return this.mSavedStateRegistryController.getSavedStateRegistry();
    }

    public ViewModelStore getViewModelStore() {
        initialize();
        return this.mViewModelStore;
    }

    /* access modifiers changed from: package-private */
    public void handleLifecycleEvent(Lifecycle.Event event) {
        this.mLifecycleRegistry.handleLifecycleEvent(event);
    }

    /* access modifiers changed from: package-private */
    public void initialize() {
        if (this.mLifecycleRegistry == null) {
            this.mLifecycleRegistry = new LifecycleRegistry(this);
            this.mSavedStateRegistryController = SavedStateRegistryController.create(this);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isInitialized() {
        return this.mLifecycleRegistry != null;
    }

    /* access modifiers changed from: package-private */
    public void performRestore(Bundle savedState) {
        this.mSavedStateRegistryController.performRestore(savedState);
    }

    /* access modifiers changed from: package-private */
    public void performSave(Bundle outBundle) {
        this.mSavedStateRegistryController.performSave(outBundle);
    }

    /* access modifiers changed from: package-private */
    public void setCurrentState(Lifecycle.State state) {
        this.mLifecycleRegistry.setCurrentState(state);
    }
}
