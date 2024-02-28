package androidx.lifecycle;

import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;

public abstract class AbstractSavedStateViewModelFactory extends ViewModelProvider.OnRequeryFactory implements ViewModelProvider.Factory {
    static final String TAG_SAVED_STATE_HANDLE_CONTROLLER = "androidx.lifecycle.savedstate.vm.tag";
    private Bundle mDefaultArgs;
    private Lifecycle mLifecycle;
    private SavedStateRegistry mSavedStateRegistry;

    public AbstractSavedStateViewModelFactory() {
    }

    public AbstractSavedStateViewModelFactory(SavedStateRegistryOwner owner, Bundle defaultArgs) {
        this.mSavedStateRegistry = owner.getSavedStateRegistry();
        this.mLifecycle = owner.getLifecycle();
        this.mDefaultArgs = defaultArgs;
    }

    private <T extends ViewModel> T create(String key, Class<T> cls) {
        SavedStateHandleController create = LegacySavedStateHandleController.create(this.mSavedStateRegistry, this.mLifecycle, key, this.mDefaultArgs);
        T create2 = create(key, cls, create.getHandle());
        create2.setTagIfAbsent(TAG_SAVED_STATE_HANDLE_CONTROLLER, create);
        return create2;
    }

    public final <T extends ViewModel> T create(Class<T> cls) {
        String canonicalName = cls.getCanonicalName();
        if (canonicalName == null) {
            throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
        } else if (this.mLifecycle != null) {
            return create(canonicalName, cls);
        } else {
            throw new UnsupportedOperationException("AbstractSavedStateViewModelFactory constructed with empty constructor supports only calls to create(modelClass: Class<T>, extras: CreationExtras).");
        }
    }

    public final <T extends ViewModel> T create(Class<T> cls, CreationExtras extras) {
        String str = (String) extras.get(ViewModelProvider.NewInstanceFactory.VIEW_MODEL_KEY);
        if (str != null) {
            return this.mSavedStateRegistry != null ? create(str, cls) : create(str, cls, SavedStateHandleSupport.createSavedStateHandle(extras));
        }
        throw new IllegalStateException("VIEW_MODEL_KEY must always be provided by ViewModelProvider");
    }

    /* access modifiers changed from: protected */
    public abstract <T extends ViewModel> T create(String str, Class<T> cls, SavedStateHandle savedStateHandle);

    public void onRequery(ViewModel viewModel) {
        SavedStateRegistry savedStateRegistry = this.mSavedStateRegistry;
        if (savedStateRegistry != null) {
            LegacySavedStateHandleController.attachHandleIfNeeded(viewModel, savedStateRegistry, this.mLifecycle);
        }
    }
}
