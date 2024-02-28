package androidx.activity.result;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public abstract class ActivityResultRegistry {
    private static final int INITIAL_REQUEST_CODE_VALUE = 65536;
    private static final String KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS = "KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS";
    private static final String KEY_COMPONENT_ACTIVITY_PENDING_RESULTS = "KEY_COMPONENT_ACTIVITY_PENDING_RESULT";
    private static final String KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT = "KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT";
    private static final String KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS = "KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS";
    private static final String KEY_COMPONENT_ACTIVITY_REGISTERED_RCS = "KEY_COMPONENT_ACTIVITY_REGISTERED_RCS";
    private static final String LOG_TAG = "ActivityResultRegistry";
    final transient Map<String, CallbackAndContract<?>> mKeyToCallback = new HashMap();
    private final Map<String, LifecycleContainer> mKeyToLifecycleContainers = new HashMap();
    final Map<String, Integer> mKeyToRc = new HashMap();
    ArrayList<String> mLaunchedKeys = new ArrayList<>();
    final Map<String, Object> mParsedPendingResults = new HashMap();
    final Bundle mPendingResults = new Bundle();
    private Random mRandom = new Random();
    private final Map<Integer, String> mRcToKey = new HashMap();

    private static class CallbackAndContract<O> {
        final ActivityResultCallback<O> mCallback;
        final ActivityResultContract<?, O> mContract;

        CallbackAndContract(ActivityResultCallback<O> activityResultCallback, ActivityResultContract<?, O> activityResultContract) {
            this.mCallback = activityResultCallback;
            this.mContract = activityResultContract;
        }
    }

    private static class LifecycleContainer {
        final Lifecycle mLifecycle;
        private final ArrayList<LifecycleEventObserver> mObservers = new ArrayList<>();

        LifecycleContainer(Lifecycle lifecycle) {
            this.mLifecycle = lifecycle;
        }

        /* access modifiers changed from: package-private */
        public void addObserver(LifecycleEventObserver observer) {
            this.mLifecycle.addObserver(observer);
            this.mObservers.add(observer);
        }

        /* access modifiers changed from: package-private */
        public void clearObservers() {
            Iterator<LifecycleEventObserver> it = this.mObservers.iterator();
            while (it.hasNext()) {
                this.mLifecycle.removeObserver(it.next());
            }
            this.mObservers.clear();
        }
    }

    private void bindRcKey(int rc, String key) {
        this.mRcToKey.put(Integer.valueOf(rc), key);
        this.mKeyToRc.put(key, Integer.valueOf(rc));
    }

    private <O> void doDispatch(String key, int resultCode, Intent data, CallbackAndContract<O> callbackAndContract) {
        if (callbackAndContract == null || callbackAndContract.mCallback == null || !this.mLaunchedKeys.contains(key)) {
            this.mParsedPendingResults.remove(key);
            this.mPendingResults.putParcelable(key, new ActivityResult(resultCode, data));
            return;
        }
        callbackAndContract.mCallback.onActivityResult(callbackAndContract.mContract.parseResult(resultCode, data));
        this.mLaunchedKeys.remove(key);
    }

    private int generateRandomNumber() {
        int nextInt = this.mRandom.nextInt(2147418112) + 65536;
        while (this.mRcToKey.containsKey(Integer.valueOf(nextInt))) {
            nextInt = this.mRandom.nextInt(2147418112) + 65536;
        }
        return nextInt;
    }

    private void registerKey(String key) {
        if (this.mKeyToRc.get(key) == null) {
            bindRcKey(generateRandomNumber(), key);
        }
    }

    public final boolean dispatchResult(int requestCode, int resultCode, Intent data) {
        String str = this.mRcToKey.get(Integer.valueOf(requestCode));
        if (str == null) {
            return false;
        }
        doDispatch(str, resultCode, data, this.mKeyToCallback.get(str));
        return true;
    }

    public final <O> boolean dispatchResult(int requestCode, O o) {
        String str = this.mRcToKey.get(Integer.valueOf(requestCode));
        if (str == null) {
            return false;
        }
        CallbackAndContract callbackAndContract = this.mKeyToCallback.get(str);
        if (callbackAndContract == null || callbackAndContract.mCallback == null) {
            this.mPendingResults.remove(str);
            this.mParsedPendingResults.put(str, o);
            return true;
        }
        ActivityResultCallback<O> activityResultCallback = callbackAndContract.mCallback;
        if (!this.mLaunchedKeys.remove(str)) {
            return true;
        }
        activityResultCallback.onActivityResult(o);
        return true;
    }

    public abstract <I, O> void onLaunch(int i, ActivityResultContract<I, O> activityResultContract, I i2, ActivityOptionsCompat activityOptionsCompat);

    public final void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            ArrayList<Integer> integerArrayList = savedInstanceState.getIntegerArrayList(KEY_COMPONENT_ACTIVITY_REGISTERED_RCS);
            ArrayList<String> stringArrayList = savedInstanceState.getStringArrayList(KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS);
            if (stringArrayList != null && integerArrayList != null) {
                this.mLaunchedKeys = savedInstanceState.getStringArrayList(KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS);
                this.mRandom = (Random) savedInstanceState.getSerializable(KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT);
                this.mPendingResults.putAll(savedInstanceState.getBundle(KEY_COMPONENT_ACTIVITY_PENDING_RESULTS));
                for (int i = 0; i < stringArrayList.size(); i++) {
                    String str = stringArrayList.get(i);
                    if (this.mKeyToRc.containsKey(str)) {
                        Integer remove = this.mKeyToRc.remove(str);
                        if (!this.mPendingResults.containsKey(str)) {
                            this.mRcToKey.remove(remove);
                        }
                    }
                    bindRcKey(integerArrayList.get(i).intValue(), stringArrayList.get(i));
                }
            }
        }
    }

    public final void onSaveInstanceState(Bundle outState) {
        outState.putIntegerArrayList(KEY_COMPONENT_ACTIVITY_REGISTERED_RCS, new ArrayList(this.mKeyToRc.values()));
        outState.putStringArrayList(KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS, new ArrayList(this.mKeyToRc.keySet()));
        outState.putStringArrayList(KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS, new ArrayList(this.mLaunchedKeys));
        outState.putBundle(KEY_COMPONENT_ACTIVITY_PENDING_RESULTS, (Bundle) this.mPendingResults.clone());
        outState.putSerializable(KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT, this.mRandom);
    }

    public final <I, O> ActivityResultLauncher<I> register(final String key, final ActivityResultContract<I, O> activityResultContract, ActivityResultCallback<O> activityResultCallback) {
        registerKey(key);
        this.mKeyToCallback.put(key, new CallbackAndContract(activityResultCallback, activityResultContract));
        if (this.mParsedPendingResults.containsKey(key)) {
            Object obj = this.mParsedPendingResults.get(key);
            this.mParsedPendingResults.remove(key);
            activityResultCallback.onActivityResult(obj);
        }
        ActivityResult activityResult = (ActivityResult) this.mPendingResults.getParcelable(key);
        if (activityResult != null) {
            this.mPendingResults.remove(key);
            activityResultCallback.onActivityResult(activityResultContract.parseResult(activityResult.getResultCode(), activityResult.getData()));
        }
        return new ActivityResultLauncher<I>() {
            public ActivityResultContract<I, ?> getContract() {
                return activityResultContract;
            }

            public void launch(I i, ActivityOptionsCompat options) {
                Integer num = ActivityResultRegistry.this.mKeyToRc.get(key);
                if (num != null) {
                    ActivityResultRegistry.this.mLaunchedKeys.add(key);
                    try {
                        ActivityResultRegistry.this.onLaunch(num.intValue(), activityResultContract, i, options);
                    } catch (Exception e) {
                        ActivityResultRegistry.this.mLaunchedKeys.remove(key);
                        throw e;
                    }
                } else {
                    throw new IllegalStateException("Attempting to launch an unregistered ActivityResultLauncher with contract " + activityResultContract + " and input " + i + ". You must ensure the ActivityResultLauncher is registered before calling launch().");
                }
            }

            public void unregister() {
                ActivityResultRegistry.this.unregister(key);
            }
        };
    }

    public final <I, O> ActivityResultLauncher<I> register(final String key, LifecycleOwner lifecycleOwner, final ActivityResultContract<I, O> activityResultContract, final ActivityResultCallback<O> activityResultCallback) {
        Lifecycle lifecycle = lifecycleOwner.getLifecycle();
        if (!lifecycle.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            registerKey(key);
            LifecycleContainer lifecycleContainer = this.mKeyToLifecycleContainers.get(key);
            if (lifecycleContainer == null) {
                lifecycleContainer = new LifecycleContainer(lifecycle);
            }
            lifecycleContainer.addObserver(new LifecycleEventObserver() {
                public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                    if (Lifecycle.Event.ON_START.equals(event)) {
                        ActivityResultRegistry.this.mKeyToCallback.put(key, new CallbackAndContract(activityResultCallback, activityResultContract));
                        if (ActivityResultRegistry.this.mParsedPendingResults.containsKey(key)) {
                            Object obj = ActivityResultRegistry.this.mParsedPendingResults.get(key);
                            ActivityResultRegistry.this.mParsedPendingResults.remove(key);
                            activityResultCallback.onActivityResult(obj);
                        }
                        ActivityResult activityResult = (ActivityResult) ActivityResultRegistry.this.mPendingResults.getParcelable(key);
                        if (activityResult != null) {
                            ActivityResultRegistry.this.mPendingResults.remove(key);
                            activityResultCallback.onActivityResult(activityResultContract.parseResult(activityResult.getResultCode(), activityResult.getData()));
                        }
                    } else if (Lifecycle.Event.ON_STOP.equals(event)) {
                        ActivityResultRegistry.this.mKeyToCallback.remove(key);
                    } else if (Lifecycle.Event.ON_DESTROY.equals(event)) {
                        ActivityResultRegistry.this.unregister(key);
                    }
                }
            });
            this.mKeyToLifecycleContainers.put(key, lifecycleContainer);
            return new ActivityResultLauncher<I>() {
                public ActivityResultContract<I, ?> getContract() {
                    return activityResultContract;
                }

                public void launch(I i, ActivityOptionsCompat options) {
                    Integer num = ActivityResultRegistry.this.mKeyToRc.get(key);
                    if (num != null) {
                        ActivityResultRegistry.this.mLaunchedKeys.add(key);
                        try {
                            ActivityResultRegistry.this.onLaunch(num.intValue(), activityResultContract, i, options);
                        } catch (Exception e) {
                            ActivityResultRegistry.this.mLaunchedKeys.remove(key);
                            throw e;
                        }
                    } else {
                        throw new IllegalStateException("Attempting to launch an unregistered ActivityResultLauncher with contract " + activityResultContract + " and input " + i + ". You must ensure the ActivityResultLauncher is registered before calling launch().");
                    }
                }

                public void unregister() {
                    ActivityResultRegistry.this.unregister(key);
                }
            };
        }
        throw new IllegalStateException("LifecycleOwner " + lifecycleOwner + " is attempting to register while current state is " + lifecycle.getCurrentState() + ". LifecycleOwners must call register before they are STARTED.");
    }

    /* access modifiers changed from: package-private */
    public final void unregister(String key) {
        Integer remove;
        if (!this.mLaunchedKeys.contains(key) && (remove = this.mKeyToRc.remove(key)) != null) {
            this.mRcToKey.remove(remove);
        }
        this.mKeyToCallback.remove(key);
        if (this.mParsedPendingResults.containsKey(key)) {
            Log.w(LOG_TAG, "Dropping pending result for request " + key + ": " + this.mParsedPendingResults.get(key));
            this.mParsedPendingResults.remove(key);
        }
        if (this.mPendingResults.containsKey(key)) {
            Log.w(LOG_TAG, "Dropping pending result for request " + key + ": " + this.mPendingResults.getParcelable(key));
            this.mPendingResults.remove(key);
        }
        LifecycleContainer lifecycleContainer = this.mKeyToLifecycleContainers.get(key);
        if (lifecycleContainer != null) {
            lifecycleContainer.clearObservers();
            this.mKeyToLifecycleContainers.remove(key);
        }
    }
}
