package androidx.core.app;

import android.content.res.Configuration;

public final class MultiWindowModeChangedInfo {
    private final boolean mIsInMultiWindowMode;
    private final Configuration mNewConfig;

    public MultiWindowModeChangedInfo(boolean isInMultiWindowMode) {
        this.mIsInMultiWindowMode = isInMultiWindowMode;
        this.mNewConfig = null;
    }

    public MultiWindowModeChangedInfo(boolean isInMultiWindowMode, Configuration newConfig) {
        this.mIsInMultiWindowMode = isInMultiWindowMode;
        this.mNewConfig = newConfig;
    }

    public Configuration getNewConfig() {
        Configuration configuration = this.mNewConfig;
        if (configuration != null) {
            return configuration;
        }
        throw new IllegalStateException("MultiWindowModeChangedInfo must be constructed with the constructor that takes a Configuration to call getNewConfig(). Are you running on an API 26 or higher device that makes this information available?");
    }

    public boolean isInMultiWindowMode() {
        return this.mIsInMultiWindowMode;
    }
}
