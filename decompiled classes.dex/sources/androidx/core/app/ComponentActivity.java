package androidx.core.app;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import androidx.collection.SimpleArrayMap;
import androidx.core.view.KeyEventDispatcher;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ReportFragment;

public class ComponentActivity extends Activity implements LifecycleOwner, KeyEventDispatcher.Component {
    private SimpleArrayMap<Class<? extends ExtraData>, ExtraData> mExtraDataMap = new SimpleArrayMap<>();
    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    @Deprecated
    public static class ExtraData {
    }

    private static boolean shouldSkipDump(String[] args) {
        if (args != null && args.length > 0) {
            String str = args[0];
            char c = 65535;
            switch (str.hashCode()) {
                case -645125871:
                    if (str.equals("--translation")) {
                        c = 2;
                        break;
                    }
                    break;
                case 1159329357:
                    if (str.equals("--contentcapture")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1455016274:
                    if (str.equals("--autofill")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    return Build.VERSION.SDK_INT >= 26;
                case 1:
                    return Build.VERSION.SDK_INT >= 29;
                case 2:
                    return Build.VERSION.SDK_INT >= 31;
            }
        }
        return false;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        View decorView = getWindow().getDecorView();
        if (decorView == null || !KeyEventDispatcher.dispatchBeforeHierarchy(decorView, event)) {
            return KeyEventDispatcher.dispatchKeyEvent(this, decorView, this, event);
        }
        return true;
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        View decorView = getWindow().getDecorView();
        if (decorView == null || !KeyEventDispatcher.dispatchBeforeHierarchy(decorView, event)) {
            return super.dispatchKeyShortcutEvent(event);
        }
        return true;
    }

    @Deprecated
    public <T extends ExtraData> T getExtraData(Class<T> cls) {
        return (ExtraData) this.mExtraDataMap.get(cls);
    }

    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReportFragment.injectIfNeededIn(this);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
        this.mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        super.onSaveInstanceState(outState);
    }

    @Deprecated
    public void putExtraData(ExtraData extraData) {
        this.mExtraDataMap.put(extraData.getClass(), extraData);
    }

    /* access modifiers changed from: protected */
    public final boolean shouldDumpInternalState(String[] args) {
        return !shouldSkipDump(args);
    }

    public boolean superDispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }
}
