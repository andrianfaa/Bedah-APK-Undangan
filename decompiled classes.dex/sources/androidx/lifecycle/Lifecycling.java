package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mt.Log1F380D;
import okhttp3.HttpUrl;

/* compiled from: 0083 */
public class Lifecycling {
    private static final int GENERATED_CALLBACK = 2;
    private static final int REFLECTIVE_CALLBACK = 1;
    private static Map<Class<?>, Integer> sCallbackCache = new HashMap();
    private static Map<Class<?>, List<Constructor<? extends GeneratedAdapter>>> sClassToAdapters = new HashMap();

    private Lifecycling() {
    }

    private static GeneratedAdapter createGeneratedAdapter(Constructor<? extends GeneratedAdapter> constructor, Object object) {
        try {
            return (GeneratedAdapter) constructor.newInstance(new Object[]{object});
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e2) {
            throw new RuntimeException(e2);
        } catch (InvocationTargetException e3) {
            throw new RuntimeException(e3);
        }
    }

    private static Constructor<? extends GeneratedAdapter> generatedConstructor(Class<?> cls) {
        try {
            Package packageR = cls.getPackage();
            String canonicalName = cls.getCanonicalName();
            String name = packageR != null ? packageR.getName() : HttpUrl.FRAGMENT_ENCODE_SET;
            String adapterName = getAdapterName(name.isEmpty() ? canonicalName : canonicalName.substring(name.length() + 1));
            Log1F380D.a((Object) adapterName);
            Constructor<?> declaredConstructor = Class.forName(name.isEmpty() ? adapterName : name + "." + adapterName).getDeclaredConstructor(new Class[]{cls});
            if (!declaredConstructor.isAccessible()) {
                declaredConstructor.setAccessible(true);
            }
            return declaredConstructor;
        } catch (ClassNotFoundException e) {
            return null;
        } catch (NoSuchMethodException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static String getAdapterName(String className) {
        return className.replace(".", "_") + "_LifecycleAdapter";
    }

    @Deprecated
    static GenericLifecycleObserver getCallback(Object object) {
        final LifecycleEventObserver lifecycleEventObserver = lifecycleEventObserver(object);
        return new GenericLifecycleObserver() {
            public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                LifecycleEventObserver.this.onStateChanged(source, event);
            }
        };
    }

    private static int getObserverConstructorType(Class<?> cls) {
        Integer num = sCallbackCache.get(cls);
        if (num != null) {
            return num.intValue();
        }
        int resolveObserverCallbackType = resolveObserverCallbackType(cls);
        sCallbackCache.put(cls, Integer.valueOf(resolveObserverCallbackType));
        return resolveObserverCallbackType;
    }

    private static boolean isLifecycleParent(Class<?> cls) {
        return cls != null && LifecycleObserver.class.isAssignableFrom(cls);
    }

    static LifecycleEventObserver lifecycleEventObserver(Object object) {
        boolean z = object instanceof LifecycleEventObserver;
        boolean z2 = object instanceof FullLifecycleObserver;
        if (z && z2) {
            return new FullLifecycleObserverAdapter((FullLifecycleObserver) object, (LifecycleEventObserver) object);
        }
        if (z2) {
            return new FullLifecycleObserverAdapter((FullLifecycleObserver) object, (LifecycleEventObserver) null);
        }
        if (z) {
            return (LifecycleEventObserver) object;
        }
        Class<?> cls = object.getClass();
        if (getObserverConstructorType(cls) != 2) {
            return new ReflectiveGenericLifecycleObserver(object);
        }
        List list = sClassToAdapters.get(cls);
        if (list.size() == 1) {
            return new SingleGeneratedAdapterObserver(createGeneratedAdapter((Constructor) list.get(0), object));
        }
        GeneratedAdapter[] generatedAdapterArr = new GeneratedAdapter[list.size()];
        for (int i = 0; i < list.size(); i++) {
            generatedAdapterArr[i] = createGeneratedAdapter((Constructor) list.get(i), object);
        }
        return new CompositeGeneratedAdaptersObserver(generatedAdapterArr);
    }

    private static int resolveObserverCallbackType(Class<?> cls) {
        if (cls.getCanonicalName() == null) {
            return 1;
        }
        Constructor<? extends GeneratedAdapter> generatedConstructor = generatedConstructor(cls);
        if (generatedConstructor != null) {
            sClassToAdapters.put(cls, Collections.singletonList(generatedConstructor));
            return 2;
        } else if (ClassesInfoCache.sInstance.hasLifecycleMethods(cls)) {
            return 1;
        } else {
            Class<? super Object> superclass = cls.getSuperclass();
            ArrayList arrayList = null;
            if (isLifecycleParent(superclass)) {
                if (getObserverConstructorType(superclass) == 1) {
                    return 1;
                }
                arrayList = new ArrayList(sClassToAdapters.get(superclass));
            }
            for (Class cls2 : cls.getInterfaces()) {
                if (isLifecycleParent(cls2)) {
                    if (getObserverConstructorType(cls2) == 1) {
                        return 1;
                    }
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.addAll(sClassToAdapters.get(cls2));
                }
            }
            if (arrayList == null) {
                return 1;
            }
            sClassToAdapters.put(cls, arrayList);
            return 2;
        }
    }
}
