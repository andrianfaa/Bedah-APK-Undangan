package androidx.core.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.drawable.IconCompat;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class NotificationCompatJellybean {
    static final String EXTRA_ALLOW_GENERATED_REPLIES = "android.support.allowGeneratedReplies";
    static final String EXTRA_DATA_ONLY_REMOTE_INPUTS = "android.support.dataRemoteInputs";
    private static final String KEY_ACTION_INTENT = "actionIntent";
    private static final String KEY_ALLOWED_DATA_TYPES = "allowedDataTypes";
    private static final String KEY_ALLOW_FREE_FORM_INPUT = "allowFreeFormInput";
    private static final String KEY_CHOICES = "choices";
    private static final String KEY_DATA_ONLY_REMOTE_INPUTS = "dataOnlyRemoteInputs";
    private static final String KEY_EXTRAS = "extras";
    private static final String KEY_ICON = "icon";
    private static final String KEY_LABEL = "label";
    private static final String KEY_REMOTE_INPUTS = "remoteInputs";
    private static final String KEY_RESULT_KEY = "resultKey";
    private static final String KEY_SEMANTIC_ACTION = "semanticAction";
    private static final String KEY_SHOWS_USER_INTERFACE = "showsUserInterface";
    private static final String KEY_TITLE = "title";
    public static final String TAG = "NotificationCompat";
    private static Field sActionIconField;
    private static Field sActionIntentField;
    private static Field sActionTitleField;
    private static boolean sActionsAccessFailed;
    private static Field sActionsField;
    private static final Object sActionsLock = new Object();
    private static Field sExtrasField;
    private static boolean sExtrasFieldAccessFailed;
    private static final Object sExtrasLock = new Object();

    private NotificationCompatJellybean() {
    }

    public static SparseArray<Bundle> buildActionExtrasMap(List<Bundle> list) {
        SparseArray<Bundle> sparseArray = null;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Bundle bundle = list.get(i);
            if (bundle != null) {
                if (sparseArray == null) {
                    sparseArray = new SparseArray<>();
                }
                sparseArray.put(i, bundle);
            }
        }
        return sparseArray;
    }

    private static boolean ensureActionReflectionReadyLocked() {
        if (sActionsAccessFailed) {
            return false;
        }
        try {
            if (sActionsField == null) {
                Class<?> cls = Class.forName("android.app.Notification$Action");
                sActionIconField = cls.getDeclaredField(KEY_ICON);
                sActionTitleField = cls.getDeclaredField(KEY_TITLE);
                sActionIntentField = cls.getDeclaredField(KEY_ACTION_INTENT);
                Field declaredField = Notification.class.getDeclaredField("actions");
                sActionsField = declaredField;
                declaredField.setAccessible(true);
            }
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Unable to access notification actions", e);
            sActionsAccessFailed = true;
        } catch (NoSuchFieldException e2) {
            Log.e(TAG, "Unable to access notification actions", e2);
            sActionsAccessFailed = true;
        }
        return !sActionsAccessFailed;
    }

    private static RemoteInput fromBundle(Bundle data) {
        ArrayList<String> stringArrayList = data.getStringArrayList(KEY_ALLOWED_DATA_TYPES);
        HashSet hashSet = new HashSet();
        if (stringArrayList != null) {
            Iterator<String> it = stringArrayList.iterator();
            while (it.hasNext()) {
                hashSet.add(it.next());
            }
        }
        return new RemoteInput(data.getString(KEY_RESULT_KEY), data.getCharSequence(KEY_LABEL), data.getCharSequenceArray(KEY_CHOICES), data.getBoolean(KEY_ALLOW_FREE_FORM_INPUT), 0, data.getBundle(KEY_EXTRAS), hashSet);
    }

    private static RemoteInput[] fromBundleArray(Bundle[] bundles) {
        if (bundles == null) {
            return null;
        }
        RemoteInput[] remoteInputArr = new RemoteInput[bundles.length];
        for (int i = 0; i < bundles.length; i++) {
            remoteInputArr[i] = fromBundle(bundles[i]);
        }
        return remoteInputArr;
    }

    public static NotificationCompat.Action getAction(Notification notif, int actionIndex) {
        SparseArray sparseParcelableArray;
        synchronized (sActionsLock) {
            try {
                Object[] actionObjectsLocked = getActionObjectsLocked(notif);
                if (actionObjectsLocked != null) {
                    Object obj = actionObjectsLocked[actionIndex];
                    Bundle bundle = null;
                    Bundle extras = getExtras(notif);
                    if (!(extras == null || (sparseParcelableArray = extras.getSparseParcelableArray(NotificationCompatExtras.EXTRA_ACTION_EXTRAS)) == null)) {
                        bundle = (Bundle) sparseParcelableArray.get(actionIndex);
                    }
                    NotificationCompat.Action readAction = readAction(sActionIconField.getInt(obj), (CharSequence) sActionTitleField.get(obj), (PendingIntent) sActionIntentField.get(obj), bundle);
                    return readAction;
                }
            } catch (IllegalAccessException e) {
                Log.e(TAG, "Unable to access notification actions", e);
                sActionsAccessFailed = true;
            } catch (Throwable th) {
                throw th;
            }
        }
        return null;
    }

    public static int getActionCount(Notification notif) {
        int length;
        synchronized (sActionsLock) {
            Object[] actionObjectsLocked = getActionObjectsLocked(notif);
            length = actionObjectsLocked != null ? actionObjectsLocked.length : 0;
        }
        return length;
    }

    static NotificationCompat.Action getActionFromBundle(Bundle bundle) {
        Bundle bundle2 = bundle;
        Bundle bundle3 = bundle2.getBundle(KEY_EXTRAS);
        boolean z = false;
        if (bundle3 != null) {
            z = bundle3.getBoolean(EXTRA_ALLOW_GENERATED_REPLIES, false);
        }
        return new NotificationCompat.Action(bundle2.getInt(KEY_ICON), bundle2.getCharSequence(KEY_TITLE), (PendingIntent) bundle2.getParcelable(KEY_ACTION_INTENT), bundle2.getBundle(KEY_EXTRAS), fromBundleArray(getBundleArrayFromBundle(bundle2, KEY_REMOTE_INPUTS)), fromBundleArray(getBundleArrayFromBundle(bundle2, KEY_DATA_ONLY_REMOTE_INPUTS)), z, bundle2.getInt(KEY_SEMANTIC_ACTION), bundle2.getBoolean(KEY_SHOWS_USER_INTERFACE), false, false);
    }

    private static Object[] getActionObjectsLocked(Notification notif) {
        synchronized (sActionsLock) {
            if (!ensureActionReflectionReadyLocked()) {
                return null;
            }
            try {
                Object[] objArr = (Object[]) sActionsField.get(notif);
                return objArr;
            } catch (IllegalAccessException e) {
                Log.e(TAG, "Unable to access notification actions", e);
                sActionsAccessFailed = true;
                return null;
            }
        }
    }

    private static Bundle[] getBundleArrayFromBundle(Bundle bundle, String key) {
        Parcelable[] parcelableArray = bundle.getParcelableArray(key);
        if ((parcelableArray instanceof Bundle[]) || parcelableArray == null) {
            return (Bundle[]) parcelableArray;
        }
        Bundle[] bundleArr = (Bundle[]) Arrays.copyOf(parcelableArray, parcelableArray.length, Bundle[].class);
        bundle.putParcelableArray(key, bundleArr);
        return bundleArr;
    }

    static Bundle getBundleForAction(NotificationCompat.Action action) {
        Bundle bundle = new Bundle();
        IconCompat iconCompat = action.getIconCompat();
        bundle.putInt(KEY_ICON, iconCompat != null ? iconCompat.getResId() : 0);
        bundle.putCharSequence(KEY_TITLE, action.getTitle());
        bundle.putParcelable(KEY_ACTION_INTENT, action.getActionIntent());
        Bundle bundle2 = action.getExtras() != null ? new Bundle(action.getExtras()) : new Bundle();
        bundle2.putBoolean(EXTRA_ALLOW_GENERATED_REPLIES, action.getAllowGeneratedReplies());
        bundle.putBundle(KEY_EXTRAS, bundle2);
        bundle.putParcelableArray(KEY_REMOTE_INPUTS, toBundleArray(action.getRemoteInputs()));
        bundle.putBoolean(KEY_SHOWS_USER_INTERFACE, action.getShowsUserInterface());
        bundle.putInt(KEY_SEMANTIC_ACTION, action.getSemanticAction());
        return bundle;
    }

    public static Bundle getExtras(Notification notif) {
        synchronized (sExtrasLock) {
            if (sExtrasFieldAccessFailed) {
                return null;
            }
            try {
                if (sExtrasField == null) {
                    Field declaredField = Notification.class.getDeclaredField(KEY_EXTRAS);
                    if (!Bundle.class.isAssignableFrom(declaredField.getType())) {
                        Log.e(TAG, "Notification.extras field is not of type Bundle");
                        sExtrasFieldAccessFailed = true;
                        return null;
                    }
                    declaredField.setAccessible(true);
                    sExtrasField = declaredField;
                }
                Bundle bundle = (Bundle) sExtrasField.get(notif);
                if (bundle == null) {
                    bundle = new Bundle();
                    sExtrasField.set(notif, bundle);
                }
                return bundle;
            } catch (IllegalAccessException e) {
                Log.e(TAG, "Unable to access notification extras", e);
                sExtrasFieldAccessFailed = true;
                return null;
            } catch (NoSuchFieldException e2) {
                Log.e(TAG, "Unable to access notification extras", e2);
                sExtrasFieldAccessFailed = true;
                return null;
            }
        }
    }

    public static NotificationCompat.Action readAction(int icon, CharSequence title, PendingIntent actionIntent, Bundle extras) {
        boolean z;
        RemoteInput[] remoteInputArr;
        RemoteInput[] remoteInputArr2;
        Bundle bundle = extras;
        if (bundle != null) {
            remoteInputArr2 = fromBundleArray(getBundleArrayFromBundle(bundle, NotificationCompatExtras.EXTRA_REMOTE_INPUTS));
            remoteInputArr = fromBundleArray(getBundleArrayFromBundle(bundle, EXTRA_DATA_ONLY_REMOTE_INPUTS));
            z = bundle.getBoolean(EXTRA_ALLOW_GENERATED_REPLIES);
        } else {
            remoteInputArr2 = null;
            remoteInputArr = null;
            z = false;
        }
        return new NotificationCompat.Action(icon, title, actionIntent, extras, remoteInputArr2, remoteInputArr, z, 0, true, false, false);
    }

    private static Bundle toBundle(RemoteInput remoteInput) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RESULT_KEY, remoteInput.getResultKey());
        bundle.putCharSequence(KEY_LABEL, remoteInput.getLabel());
        bundle.putCharSequenceArray(KEY_CHOICES, remoteInput.getChoices());
        bundle.putBoolean(KEY_ALLOW_FREE_FORM_INPUT, remoteInput.getAllowFreeFormInput());
        bundle.putBundle(KEY_EXTRAS, remoteInput.getExtras());
        Set<String> allowedDataTypes = remoteInput.getAllowedDataTypes();
        if (allowedDataTypes != null && !allowedDataTypes.isEmpty()) {
            ArrayList arrayList = new ArrayList(allowedDataTypes.size());
            for (String add : allowedDataTypes) {
                arrayList.add(add);
            }
            bundle.putStringArrayList(KEY_ALLOWED_DATA_TYPES, arrayList);
        }
        return bundle;
    }

    private static Bundle[] toBundleArray(RemoteInput[] remoteInputs) {
        if (remoteInputs == null) {
            return null;
        }
        Bundle[] bundleArr = new Bundle[remoteInputs.length];
        for (int i = 0; i < remoteInputs.length; i++) {
            bundleArr[i] = toBundle(remoteInputs[i]);
        }
        return bundleArr;
    }

    public static Bundle writeActionAndGetExtras(Notification.Builder builder, NotificationCompat.Action action) {
        IconCompat iconCompat = action.getIconCompat();
        builder.addAction(iconCompat != null ? iconCompat.getResId() : 0, action.getTitle(), action.getActionIntent());
        Bundle bundle = new Bundle(action.getExtras());
        if (action.getRemoteInputs() != null) {
            bundle.putParcelableArray(NotificationCompatExtras.EXTRA_REMOTE_INPUTS, toBundleArray(action.getRemoteInputs()));
        }
        if (action.getDataOnlyRemoteInputs() != null) {
            bundle.putParcelableArray(EXTRA_DATA_ONLY_REMOTE_INPUTS, toBundleArray(action.getDataOnlyRemoteInputs()));
        }
        bundle.putBoolean(EXTRA_ALLOW_GENERATED_REPLIES, action.getAllowGeneratedReplies());
        return bundle;
    }
}
