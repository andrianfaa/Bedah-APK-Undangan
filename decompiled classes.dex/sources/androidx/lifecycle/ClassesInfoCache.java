package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
final class ClassesInfoCache {
    private static final int CALL_TYPE_NO_ARG = 0;
    private static final int CALL_TYPE_PROVIDER = 1;
    private static final int CALL_TYPE_PROVIDER_WITH_EVENT = 2;
    static ClassesInfoCache sInstance = new ClassesInfoCache();
    private final Map<Class<?>, CallbackInfo> mCallbackMap = new HashMap();
    private final Map<Class<?>, Boolean> mHasLifecycleMethods = new HashMap();

    @Deprecated
    static class CallbackInfo {
        final Map<Lifecycle.Event, List<MethodReference>> mEventToHandlers = new HashMap();
        final Map<MethodReference, Lifecycle.Event> mHandlerToEvent;

        CallbackInfo(Map<MethodReference, Lifecycle.Event> map) {
            this.mHandlerToEvent = map;
            for (Map.Entry next : map.entrySet()) {
                Lifecycle.Event event = (Lifecycle.Event) next.getValue();
                List list = this.mEventToHandlers.get(event);
                if (list == null) {
                    list = new ArrayList();
                    this.mEventToHandlers.put(event, list);
                }
                list.add((MethodReference) next.getKey());
            }
        }

        private static void invokeMethodsForEvent(List<MethodReference> list, LifecycleOwner source, Lifecycle.Event event, Object mWrapped) {
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    list.get(size).invokeCallback(source, event, mWrapped);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void invokeCallbacks(LifecycleOwner source, Lifecycle.Event event, Object target) {
            invokeMethodsForEvent(this.mEventToHandlers.get(event), source, event, target);
            invokeMethodsForEvent(this.mEventToHandlers.get(Lifecycle.Event.ON_ANY), source, event, target);
        }
    }

    @Deprecated
    static final class MethodReference {
        final int mCallType;
        final Method mMethod;

        MethodReference(int callType, Method method) {
            this.mCallType = callType;
            this.mMethod = method;
            method.setAccessible(true);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof MethodReference)) {
                return false;
            }
            MethodReference methodReference = (MethodReference) o;
            return this.mCallType == methodReference.mCallType && this.mMethod.getName().equals(methodReference.mMethod.getName());
        }

        public int hashCode() {
            return (this.mCallType * 31) + this.mMethod.getName().hashCode();
        }

        /* access modifiers changed from: package-private */
        public void invokeCallback(LifecycleOwner source, Lifecycle.Event event, Object target) {
            try {
                switch (this.mCallType) {
                    case 0:
                        this.mMethod.invoke(target, new Object[0]);
                        return;
                    case 1:
                        this.mMethod.invoke(target, new Object[]{source});
                        return;
                    case 2:
                        this.mMethod.invoke(target, new Object[]{source, event});
                        return;
                    default:
                        return;
                }
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Failed to call observer method", e.getCause());
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    ClassesInfoCache() {
    }

    private CallbackInfo createInfo(Class<?> cls, Method[] declaredMethods) {
        CallbackInfo info;
        Class<?> cls2 = cls;
        Class<? super Object> superclass = cls.getSuperclass();
        HashMap hashMap = new HashMap();
        if (!(superclass == null || (info = getInfo(superclass)) == null)) {
            hashMap.putAll(info.mHandlerToEvent);
        }
        char c = 0;
        for (Class info2 : cls.getInterfaces()) {
            for (Map.Entry next : getInfo(info2).mHandlerToEvent.entrySet()) {
                verifyAndPutHandler(hashMap, (MethodReference) next.getKey(), (Lifecycle.Event) next.getValue(), cls2);
            }
        }
        Method[] declaredMethods2 = declaredMethods != null ? declaredMethods : getDeclaredMethods(cls);
        boolean z = false;
        int length = declaredMethods2.length;
        int i = 0;
        while (i < length) {
            Method method = declaredMethods2[i];
            OnLifecycleEvent onLifecycleEvent = (OnLifecycleEvent) method.getAnnotation(OnLifecycleEvent.class);
            if (onLifecycleEvent != null) {
                z = true;
                Class[] parameterTypes = method.getParameterTypes();
                int i2 = 0;
                if (parameterTypes.length > 0) {
                    i2 = 1;
                    if (!parameterTypes[c].isAssignableFrom(LifecycleOwner.class)) {
                        throw new IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner");
                    }
                }
                Lifecycle.Event value = onLifecycleEvent.value();
                if (parameterTypes.length > 1) {
                    i2 = 2;
                    if (!parameterTypes[1].isAssignableFrom(Lifecycle.Event.class)) {
                        throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
                    } else if (value != Lifecycle.Event.ON_ANY) {
                        throw new IllegalArgumentException("Second arg is supported only for ON_ANY value");
                    }
                }
                if (parameterTypes.length <= 2) {
                    verifyAndPutHandler(hashMap, new MethodReference(i2, method), value, cls2);
                } else {
                    throw new IllegalArgumentException("cannot have more than 2 params");
                }
            }
            i++;
            c = 0;
        }
        CallbackInfo callbackInfo = new CallbackInfo(hashMap);
        this.mCallbackMap.put(cls2, callbackInfo);
        this.mHasLifecycleMethods.put(cls2, Boolean.valueOf(z));
        return callbackInfo;
    }

    private Method[] getDeclaredMethods(Class<?> cls) {
        try {
            return cls.getDeclaredMethods();
        } catch (NoClassDefFoundError e) {
            throw new IllegalArgumentException("The observer class has some methods that use newer APIs which are not available in the current OS version. Lifecycles cannot access even other methods so you should make sure that your observer classes only access framework classes that are available in your min API level OR use lifecycle:compiler annotation processor.", e);
        }
    }

    private void verifyAndPutHandler(Map<MethodReference, Lifecycle.Event> map, MethodReference newHandler, Lifecycle.Event newEvent, Class<?> cls) {
        Lifecycle.Event event = map.get(newHandler);
        if (event != null && newEvent != event) {
            throw new IllegalArgumentException("Method " + newHandler.mMethod.getName() + " in " + cls.getName() + " already declared with different @OnLifecycleEvent value: previous value " + event + ", new value " + newEvent);
        } else if (event == null) {
            map.put(newHandler, newEvent);
        }
    }

    /* access modifiers changed from: package-private */
    public CallbackInfo getInfo(Class<?> cls) {
        CallbackInfo callbackInfo = this.mCallbackMap.get(cls);
        return callbackInfo != null ? callbackInfo : createInfo(cls, (Method[]) null);
    }

    /* access modifiers changed from: package-private */
    public boolean hasLifecycleMethods(Class<?> cls) {
        Boolean bool = this.mHasLifecycleMethods.get(cls);
        if (bool != null) {
            return bool.booleanValue();
        }
        Method[] declaredMethods = getDeclaredMethods(cls);
        for (Method annotation : declaredMethods) {
            if (((OnLifecycleEvent) annotation.getAnnotation(OnLifecycleEvent.class)) != null) {
                createInfo(cls, declaredMethods);
                return true;
            }
        }
        this.mHasLifecycleMethods.put(cls, false);
        return false;
    }
}
