package androidx.startup;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.tracing.Trace;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mt.Log1F380D;

/* compiled from: 0092 */
public final class AppInitializer {
    private static final String SECTION_NAME = "Startup";
    private static volatile AppInitializer sInstance;
    private static final Object sLock = new Object();
    final Context mContext;
    final Set<Class<? extends Initializer<?>>> mDiscovered = new HashSet();
    final Map<Class<?>, Object> mInitialized = new HashMap();

    AppInitializer(Context context) {
        this.mContext = context.getApplicationContext();
    }

    private <T> T doInitialize(Class<? extends Initializer<?>> cls, Set<Class<?>> set) {
        T t;
        if (Trace.isEnabled()) {
            try {
                Trace.beginSection(cls.getSimpleName());
            } catch (Throwable th) {
                Trace.endSection();
                throw th;
            }
        }
        if (!set.contains(cls)) {
            if (!this.mInitialized.containsKey(cls)) {
                set.add(cls);
                Initializer initializer = (Initializer) cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                List<Class<? extends Initializer<?>>> dependencies = initializer.dependencies();
                if (!dependencies.isEmpty()) {
                    for (Class next : dependencies) {
                        if (!this.mInitialized.containsKey(next)) {
                            doInitialize(next, set);
                        }
                    }
                }
                t = initializer.create(this.mContext);
                set.remove(cls);
                this.mInitialized.put(cls, t);
            } else {
                t = this.mInitialized.get(cls);
            }
            Trace.endSection();
            return t;
        }
        String format = String.format("Cannot initialize %s. Cycle detected.", new Object[]{cls.getName()});
        Log1F380D.a((Object) format);
        throw new IllegalStateException(format);
    }

    public static AppInitializer getInstance(Context context) {
        if (sInstance == null) {
            synchronized (sLock) {
                if (sInstance == null) {
                    sInstance = new AppInitializer(context);
                }
            }
        }
        return sInstance;
    }

    static void setDelegate(AppInitializer delegate) {
        synchronized (sLock) {
            sInstance = delegate;
        }
    }

    /* access modifiers changed from: package-private */
    public void discoverAndInitialize() {
        try {
            Trace.beginSection(SECTION_NAME);
            discoverAndInitialize(this.mContext.getPackageManager().getProviderInfo(new ComponentName(this.mContext.getPackageName(), InitializationProvider.class.getName()), 128).metaData);
            Trace.endSection();
        } catch (PackageManager.NameNotFoundException e) {
            throw new StartupException((Throwable) e);
        } catch (Throwable th) {
            Trace.endSection();
            throw th;
        }
    }

    /* access modifiers changed from: package-private */
    public void discoverAndInitialize(Bundle metadata) {
        String string = this.mContext.getString(R.string.androidx_startup);
        if (metadata != null) {
            try {
                HashSet hashSet = new HashSet();
                for (String str : metadata.keySet()) {
                    if (string.equals(metadata.getString(str, (String) null))) {
                        Class<?> cls = Class.forName(str);
                        if (Initializer.class.isAssignableFrom(cls)) {
                            this.mDiscovered.add(cls);
                        }
                    }
                }
                for (Class<? extends Initializer<?>> doInitialize : this.mDiscovered) {
                    doInitialize(doInitialize, hashSet);
                }
            } catch (ClassNotFoundException e) {
                throw new StartupException((Throwable) e);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public <T> T doInitialize(Class<? extends Initializer<?>> cls) {
        T t;
        synchronized (sLock) {
            t = this.mInitialized.get(cls);
            if (t == null) {
                t = doInitialize(cls, new HashSet());
            }
        }
        return t;
    }

    public <T> T initializeComponent(Class<? extends Initializer<T>> cls) {
        return doInitialize(cls);
    }

    public boolean isEagerlyInitialized(Class<? extends Initializer<?>> cls) {
        return this.mDiscovered.contains(cls);
    }
}
