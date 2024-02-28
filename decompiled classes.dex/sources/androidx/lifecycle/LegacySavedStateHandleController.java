package androidx.lifecycle;

import android.os.Bundle;
import androidx.lifecycle.Lifecycle;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;

class LegacySavedStateHandleController {
    static final String TAG_SAVED_STATE_HANDLE_CONTROLLER = "androidx.lifecycle.savedstate.vm.tag";

    static final class OnRecreation implements SavedStateRegistry.AutoRecreated {
        OnRecreation() {
        }

        public void onRecreated(SavedStateRegistryOwner owner) {
            if (owner instanceof ViewModelStoreOwner) {
                ViewModelStore viewModelStore = ((ViewModelStoreOwner) owner).getViewModelStore();
                SavedStateRegistry savedStateRegistry = owner.getSavedStateRegistry();
                for (String str : viewModelStore.keys()) {
                    LegacySavedStateHandleController.attachHandleIfNeeded(viewModelStore.get(str), savedStateRegistry, owner.getLifecycle());
                }
                if (!viewModelStore.keys().isEmpty()) {
                    savedStateRegistry.runOnNextRecreation(OnRecreation.class);
                    return;
                }
                return;
            }
            throw new IllegalStateException("Internal error: OnRecreation should be registered only on components that implement ViewModelStoreOwner");
        }
    }

    private LegacySavedStateHandleController() {
    }

    static void attachHandleIfNeeded(ViewModel viewModel, SavedStateRegistry registry, Lifecycle lifecycle) {
        SavedStateHandleController savedStateHandleController = (SavedStateHandleController) viewModel.getTag(TAG_SAVED_STATE_HANDLE_CONTROLLER);
        if (savedStateHandleController != null && !savedStateHandleController.isAttached()) {
            savedStateHandleController.attachToLifecycle(registry, lifecycle);
            tryToAddRecreator(registry, lifecycle);
        }
    }

    static SavedStateHandleController create(SavedStateRegistry registry, Lifecycle lifecycle, String key, Bundle defaultArgs) {
        SavedStateHandleController savedStateHandleController = new SavedStateHandleController(key, SavedStateHandle.createHandle(registry.consumeRestoredStateForKey(key), defaultArgs));
        savedStateHandleController.attachToLifecycle(registry, lifecycle);
        tryToAddRecreator(registry, lifecycle);
        return savedStateHandleController;
    }

    private static void tryToAddRecreator(final SavedStateRegistry registry, final Lifecycle lifecycle) {
        Lifecycle.State currentState = lifecycle.getCurrentState();
        if (currentState == Lifecycle.State.INITIALIZED || currentState.isAtLeast(Lifecycle.State.STARTED)) {
            registry.runOnNextRecreation(OnRecreation.class);
        } else {
            lifecycle.addObserver(new LifecycleEventObserver() {
                public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_START) {
                        Lifecycle.this.removeObserver(this);
                        registry.runOnNextRecreation(OnRecreation.class);
                    }
                }
            });
        }
    }
}
