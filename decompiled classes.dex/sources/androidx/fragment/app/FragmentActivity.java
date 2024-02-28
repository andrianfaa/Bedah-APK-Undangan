package androidx.fragment.app;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import androidx.activity.ComponentActivity;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.activity.contextaware.OnContextAvailableListener;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.ActivityResultRegistryOwner;
import androidx.core.app.ActivityCompat;
import androidx.core.app.SharedElementCallback;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.loader.app.LoaderManager;
import androidx.savedstate.SavedStateRegistry;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import mt.Log1F380D;

/* compiled from: 0003 */
public class FragmentActivity extends ComponentActivity implements ActivityCompat.OnRequestPermissionsResultCallback, ActivityCompat.RequestPermissionsRequestCodeValidator {
    static final String FRAGMENTS_TAG = "android:support:fragments";
    boolean mCreated;
    final LifecycleRegistry mFragmentLifecycleRegistry = new LifecycleRegistry(this);
    final FragmentController mFragments = FragmentController.createController(new HostCallbacks());
    boolean mResumed;
    boolean mStopped = true;

    class HostCallbacks extends FragmentHostCallback<FragmentActivity> implements ViewModelStoreOwner, OnBackPressedDispatcherOwner, ActivityResultRegistryOwner, FragmentOnAttachListener {
        public HostCallbacks() {
            super(FragmentActivity.this);
        }

        public ActivityResultRegistry getActivityResultRegistry() {
            return FragmentActivity.this.getActivityResultRegistry();
        }

        public Lifecycle getLifecycle() {
            return FragmentActivity.this.mFragmentLifecycleRegistry;
        }

        public OnBackPressedDispatcher getOnBackPressedDispatcher() {
            return FragmentActivity.this.getOnBackPressedDispatcher();
        }

        public ViewModelStore getViewModelStore() {
            return FragmentActivity.this.getViewModelStore();
        }

        public void onAttachFragment(FragmentManager fragmentManager, Fragment fragment) {
            FragmentActivity.this.onAttachFragment(fragment);
        }

        public void onDump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            FragmentActivity.this.dump(prefix, fd, writer, args);
        }

        public View onFindViewById(int id) {
            return FragmentActivity.this.findViewById(id);
        }

        public FragmentActivity onGetHost() {
            return FragmentActivity.this;
        }

        public LayoutInflater onGetLayoutInflater() {
            return FragmentActivity.this.getLayoutInflater().cloneInContext(FragmentActivity.this);
        }

        public int onGetWindowAnimations() {
            Window window = FragmentActivity.this.getWindow();
            if (window == null) {
                return 0;
            }
            return window.getAttributes().windowAnimations;
        }

        public boolean onHasView() {
            Window window = FragmentActivity.this.getWindow();
            return (window == null || window.peekDecorView() == null) ? false : true;
        }

        public boolean onHasWindowAnimations() {
            return FragmentActivity.this.getWindow() != null;
        }

        public boolean onShouldSaveFragmentState(Fragment fragment) {
            return !FragmentActivity.this.isFinishing();
        }

        public boolean onShouldShowRequestPermissionRationale(String permission) {
            return ActivityCompat.shouldShowRequestPermissionRationale(FragmentActivity.this, permission);
        }

        public void onSupportInvalidateOptionsMenu() {
            FragmentActivity.this.supportInvalidateOptionsMenu();
        }
    }

    public FragmentActivity() {
        init();
    }

    public FragmentActivity(int contentLayoutId) {
        super(contentLayoutId);
        init();
    }

    private void init() {
        getSavedStateRegistry().registerSavedStateProvider(FRAGMENTS_TAG, new SavedStateRegistry.SavedStateProvider() {
            public Bundle saveState() {
                Bundle bundle = new Bundle();
                FragmentActivity.this.markFragmentsCreated();
                FragmentActivity.this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
                Parcelable saveAllState = FragmentActivity.this.mFragments.saveAllState();
                if (saveAllState != null) {
                    bundle.putParcelable(FragmentActivity.FRAGMENTS_TAG, saveAllState);
                }
                return bundle;
            }
        });
        addOnContextAvailableListener(new OnContextAvailableListener() {
            public void onContextAvailable(Context context) {
                FragmentActivity.this.mFragments.attachHost((Fragment) null);
                Bundle consumeRestoredStateForKey = FragmentActivity.this.getSavedStateRegistry().consumeRestoredStateForKey(FragmentActivity.FRAGMENTS_TAG);
                if (consumeRestoredStateForKey != null) {
                    FragmentActivity.this.mFragments.restoreSaveState(consumeRestoredStateForKey.getParcelable(FragmentActivity.FRAGMENTS_TAG));
                }
            }
        });
    }

    private static boolean markState(FragmentManager manager, Lifecycle.State state) {
        boolean z = false;
        for (Fragment next : manager.getFragments()) {
            if (next != null) {
                if (next.getHost() != null) {
                    z |= markState(next.getChildFragmentManager(), state);
                }
                if (next.mViewLifecycleOwner != null && next.mViewLifecycleOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    next.mViewLifecycleOwner.setCurrentState(state);
                    z = true;
                }
                if (next.mLifecycleRegistry.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    next.mLifecycleRegistry.setCurrentState(state);
                    z = true;
                }
            }
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public final View dispatchFragmentsOnCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return this.mFragments.onCreateView(parent, name, context, attrs);
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
        writer.print(prefix);
        writer.print("Local FragmentActivity ");
        String hexString = Integer.toHexString(System.identityHashCode(this));
        Log1F380D.a((Object) hexString);
        writer.print(hexString);
        writer.println(" State:");
        String str = prefix + "  ";
        writer.print(str);
        writer.print("mCreated=");
        writer.print(this.mCreated);
        writer.print(" mResumed=");
        writer.print(this.mResumed);
        writer.print(" mStopped=");
        writer.print(this.mStopped);
        if (getApplication() != null) {
            LoaderManager.getInstance(this).dump(str, fd, writer, args);
        }
        this.mFragments.getSupportFragmentManager().dump(prefix, fd, writer, args);
    }

    public FragmentManager getSupportFragmentManager() {
        return this.mFragments.getSupportFragmentManager();
    }

    @Deprecated
    public LoaderManager getSupportLoaderManager() {
        return LoaderManager.getInstance(this);
    }

    /* access modifiers changed from: package-private */
    public void markFragmentsCreated() {
        do {
        } while (markState(getSupportFragmentManager(), Lifecycle.State.CREATED));
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mFragments.noteStateNotSaved();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Deprecated
    public void onAttachFragment(Fragment fragment) {
    }

    public void onConfigurationChanged(Configuration newConfig) {
        this.mFragments.noteStateNotSaved();
        super.onConfigurationChanged(newConfig);
        this.mFragments.dispatchConfigurationChanged(newConfig);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        this.mFragments.dispatchCreate();
    }

    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        return featureId == 0 ? super.onCreatePanelMenu(featureId, menu) | this.mFragments.dispatchCreateOptionsMenu(menu, getMenuInflater()) : super.onCreatePanelMenu(featureId, menu);
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View dispatchFragmentsOnCreateView = dispatchFragmentsOnCreateView(parent, name, context, attrs);
        return dispatchFragmentsOnCreateView == null ? super.onCreateView(parent, name, context, attrs) : dispatchFragmentsOnCreateView;
    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View dispatchFragmentsOnCreateView = dispatchFragmentsOnCreateView((View) null, name, context, attrs);
        return dispatchFragmentsOnCreateView == null ? super.onCreateView(name, context, attrs) : dispatchFragmentsOnCreateView;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.mFragments.dispatchDestroy();
        this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    public void onLowMemory() {
        super.onLowMemory();
        this.mFragments.dispatchLowMemory();
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (super.onMenuItemSelected(featureId, item)) {
            return true;
        }
        switch (featureId) {
            case 0:
                return this.mFragments.dispatchOptionsItemSelected(item);
            case 6:
                return this.mFragments.dispatchContextItemSelected(item);
            default:
                return false;
        }
    }

    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        this.mFragments.dispatchMultiWindowModeChanged(isInMultiWindowMode);
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        this.mFragments.noteStateNotSaved();
        super.onNewIntent(intent);
    }

    public void onPanelClosed(int featureId, Menu menu) {
        if (featureId == 0) {
            this.mFragments.dispatchOptionsMenuClosed(menu);
        }
        super.onPanelClosed(featureId, menu);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mResumed = false;
        this.mFragments.dispatchPause();
        this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    }

    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        this.mFragments.dispatchPictureInPictureModeChanged(isInPictureInPictureMode);
    }

    /* access modifiers changed from: protected */
    public void onPostResume() {
        super.onPostResume();
        onResumeFragments();
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public boolean onPrepareOptionsPanel(View view, Menu menu) {
        return super.onPreparePanel(0, view, menu);
    }

    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        return featureId == 0 ? onPrepareOptionsPanel(view, menu) | this.mFragments.dispatchPrepareOptionsMenu(menu) : super.onPreparePanel(featureId, view, menu);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        this.mFragments.noteStateNotSaved();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        this.mFragments.noteStateNotSaved();
        super.onResume();
        this.mResumed = true;
        this.mFragments.execPendingActions();
    }

    /* access modifiers changed from: protected */
    public void onResumeFragments() {
        this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        this.mFragments.dispatchResume();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        this.mFragments.noteStateNotSaved();
        super.onStart();
        this.mStopped = false;
        if (!this.mCreated) {
            this.mCreated = true;
            this.mFragments.dispatchActivityCreated();
        }
        this.mFragments.execPendingActions();
        this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
        this.mFragments.dispatchStart();
    }

    public void onStateNotSaved() {
        this.mFragments.noteStateNotSaved();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.mStopped = true;
        markFragmentsCreated();
        this.mFragments.dispatchStop();
        this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    public void setEnterSharedElementCallback(SharedElementCallback callback) {
        ActivityCompat.setEnterSharedElementCallback(this, callback);
    }

    public void setExitSharedElementCallback(SharedElementCallback listener) {
        ActivityCompat.setExitSharedElementCallback(this, listener);
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        startActivityFromFragment(fragment, intent, requestCode, (Bundle) null);
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode, Bundle options) {
        if (requestCode == -1) {
            ActivityCompat.startActivityForResult(this, intent, -1, options);
        } else {
            fragment.startActivityForResult(intent, requestCode, options);
        }
    }

    @Deprecated
    public void startIntentSenderFromFragment(Fragment fragment, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        if (requestCode == -1) {
            ActivityCompat.startIntentSenderForResult(this, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
        } else {
            fragment.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
        }
    }

    public void supportFinishAfterTransition() {
        ActivityCompat.finishAfterTransition(this);
    }

    @Deprecated
    public void supportInvalidateOptionsMenu() {
        invalidateOptionsMenu();
    }

    public void supportPostponeEnterTransition() {
        ActivityCompat.postponeEnterTransition(this);
    }

    public void supportStartPostponedEnterTransition() {
        ActivityCompat.startPostponedEnterTransition(this);
    }

    @Deprecated
    public final void validateRequestPermissionsRequestCode(int requestCode) {
    }
}
