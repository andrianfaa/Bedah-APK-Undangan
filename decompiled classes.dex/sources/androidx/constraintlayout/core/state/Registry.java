package androidx.constraintlayout.core.state;

import java.util.HashMap;
import java.util.Set;

public class Registry {
    private static final Registry sRegistry = new Registry();
    private HashMap<String, RegistryCallback> mCallbacks = new HashMap<>();

    public static Registry getInstance() {
        return sRegistry;
    }

    public String currentContent(String name) {
        RegistryCallback registryCallback = this.mCallbacks.get(name);
        if (registryCallback != null) {
            return registryCallback.currentMotionScene();
        }
        return null;
    }

    public String currentLayoutInformation(String name) {
        RegistryCallback registryCallback = this.mCallbacks.get(name);
        if (registryCallback != null) {
            return registryCallback.currentLayoutInformation();
        }
        return null;
    }

    public long getLastModified(String name) {
        RegistryCallback registryCallback = this.mCallbacks.get(name);
        if (registryCallback != null) {
            return registryCallback.getLastModified();
        }
        return Long.MAX_VALUE;
    }

    public Set<String> getLayoutList() {
        return this.mCallbacks.keySet();
    }

    public void register(String name, RegistryCallback callback) {
        this.mCallbacks.put(name, callback);
    }

    public void setDrawDebug(String name, int debugMode) {
        RegistryCallback registryCallback = this.mCallbacks.get(name);
        if (registryCallback != null) {
            registryCallback.setDrawDebug(debugMode);
        }
    }

    public void setLayoutInformationMode(String name, int mode) {
        RegistryCallback registryCallback = this.mCallbacks.get(name);
        if (registryCallback != null) {
            registryCallback.setLayoutInformationMode(mode);
        }
    }

    public void unregister(String name, RegistryCallback callback) {
        this.mCallbacks.remove(name);
    }

    public void updateContent(String name, String content) {
        RegistryCallback registryCallback = this.mCallbacks.get(name);
        if (registryCallback != null) {
            registryCallback.onNewMotionScene(content);
        }
    }

    public void updateDimensions(String name, int width, int height) {
        RegistryCallback registryCallback = this.mCallbacks.get(name);
        if (registryCallback != null) {
            registryCallback.onDimensions(width, height);
        }
    }

    public void updateProgress(String name, float progress) {
        RegistryCallback registryCallback = this.mCallbacks.get(name);
        if (registryCallback != null) {
            registryCallback.onProgress(progress);
        }
    }
}
