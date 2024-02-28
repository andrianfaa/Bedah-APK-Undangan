package androidx.lifecycle;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

public interface HasDefaultViewModelProviderFactory {
    CreationExtras getDefaultViewModelCreationExtras() {
        return CreationExtras.Empty.INSTANCE;
    }

    ViewModelProvider.Factory getDefaultViewModelProviderFactory();
}
