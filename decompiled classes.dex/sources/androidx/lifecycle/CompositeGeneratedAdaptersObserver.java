package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;

class CompositeGeneratedAdaptersObserver implements LifecycleEventObserver {
    private final GeneratedAdapter[] mGeneratedAdapters;

    CompositeGeneratedAdaptersObserver(GeneratedAdapter[] generatedAdapters) {
        this.mGeneratedAdapters = generatedAdapters;
    }

    public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
        MethodCallsLogger methodCallsLogger = new MethodCallsLogger();
        for (GeneratedAdapter callMethods : this.mGeneratedAdapters) {
            callMethods.callMethods(source, event, false, methodCallsLogger);
        }
        for (GeneratedAdapter callMethods2 : this.mGeneratedAdapters) {
            callMethods2.callMethods(source, event, true, methodCallsLogger);
        }
    }
}
