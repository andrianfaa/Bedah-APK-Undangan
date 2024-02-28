package androidx.core.content.pm;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutInfoCompatSaver;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.util.Preconditions;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/* compiled from: 003D */
public class ShortcutManagerCompat {
    static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    private static final int DEFAULT_MAX_ICON_DIMENSION_DP = 96;
    private static final int DEFAULT_MAX_ICON_DIMENSION_LOWRAM_DP = 48;
    public static final String EXTRA_SHORTCUT_ID = "android.intent.extra.shortcut.ID";
    public static final int FLAG_MATCH_CACHED = 8;
    public static final int FLAG_MATCH_DYNAMIC = 2;
    public static final int FLAG_MATCH_MANIFEST = 1;
    public static final int FLAG_MATCH_PINNED = 4;
    static final String INSTALL_SHORTCUT_PERMISSION = "com.android.launcher.permission.INSTALL_SHORTCUT";
    private static final String SHORTCUT_LISTENER_INTENT_FILTER_ACTION = "androidx.core.content.pm.SHORTCUT_LISTENER";
    private static final String SHORTCUT_LISTENER_META_DATA_KEY = "androidx.core.content.pm.shortcut_listener_impl";
    private static volatile List<ShortcutInfoChangeListener> sShortcutInfoChangeListeners = null;
    private static volatile ShortcutInfoCompatSaver<?> sShortcutInfoCompatSaver = null;

    private static class Api25Impl {
        private Api25Impl() {
        }

        static String getShortcutInfoWithLowestRank(List<ShortcutInfo> list) {
            int i = -1;
            String str = null;
            for (ShortcutInfo next : list) {
                if (next.getRank() > i) {
                    str = next.getId();
                    i = next.getRank();
                }
            }
            return str;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ShortcutMatchFlags {
    }

    private ShortcutManagerCompat() {
    }

    public static boolean addDynamicShortcuts(Context context, List<ShortcutInfoCompat> list) {
        List<ShortcutInfoCompat> removeShortcutsExcludedFromSurface = removeShortcutsExcludedFromSurface(list, 1);
        if (Build.VERSION.SDK_INT <= 29) {
            convertUriIconsToBitmapIcons(context, removeShortcutsExcludedFromSurface);
        }
        if (Build.VERSION.SDK_INT >= 25) {
            ArrayList arrayList = new ArrayList();
            for (ShortcutInfoCompat shortcutInfo : removeShortcutsExcludedFromSurface) {
                arrayList.add(shortcutInfo.toShortcutInfo());
            }
            if (!((ShortcutManager) context.getSystemService(ShortcutManager.class)).addDynamicShortcuts(arrayList)) {
                return false;
            }
        }
        getShortcutInfoSaverInstance(context).addShortcuts(removeShortcutsExcludedFromSurface);
        for (ShortcutInfoChangeListener onShortcutAdded : getShortcutInfoListeners(context)) {
            onShortcutAdded.onShortcutAdded(list);
        }
        return true;
    }

    static boolean convertUriIconToBitmapIcon(Context context, ShortcutInfoCompat info) {
        Bitmap decodeStream;
        if (info.mIcon == null) {
            return false;
        }
        int i = info.mIcon.mType;
        if (i != 6 && i != 4) {
            return true;
        }
        InputStream uriInputStream = info.mIcon.getUriInputStream(context);
        if (uriInputStream == null || (decodeStream = BitmapFactory.decodeStream(uriInputStream)) == null) {
            return false;
        }
        info.mIcon = i == 6 ? IconCompat.createWithAdaptiveBitmap(decodeStream) : IconCompat.createWithBitmap(decodeStream);
        return true;
    }

    static void convertUriIconsToBitmapIcons(Context context, List<ShortcutInfoCompat> list) {
        for (ShortcutInfoCompat shortcutInfoCompat : new ArrayList(list)) {
            if (!convertUriIconToBitmapIcon(context, shortcutInfoCompat)) {
                list.remove(shortcutInfoCompat);
            }
        }
    }

    public static Intent createShortcutResultIntent(Context context, ShortcutInfoCompat shortcut) {
        Intent intent = null;
        if (Build.VERSION.SDK_INT >= 26) {
            intent = ((ShortcutManager) context.getSystemService(ShortcutManager.class)).createShortcutResultIntent(shortcut.toShortcutInfo());
        }
        if (intent == null) {
            intent = new Intent();
        }
        return shortcut.addToIntent(intent);
    }

    public static void disableShortcuts(Context context, List<String> list, CharSequence disabledMessage) {
        if (Build.VERSION.SDK_INT >= 25) {
            ((ShortcutManager) context.getSystemService(ShortcutManager.class)).disableShortcuts(list, disabledMessage);
        }
        getShortcutInfoSaverInstance(context).removeShortcuts(list);
        for (ShortcutInfoChangeListener onShortcutRemoved : getShortcutInfoListeners(context)) {
            onShortcutRemoved.onShortcutRemoved(list);
        }
    }

    public static void enableShortcuts(Context context, List<ShortcutInfoCompat> list) {
        List<ShortcutInfoCompat> removeShortcutsExcludedFromSurface = removeShortcutsExcludedFromSurface(list, 1);
        if (Build.VERSION.SDK_INT >= 25) {
            ArrayList arrayList = new ArrayList(list.size());
            for (ShortcutInfoCompat shortcutInfoCompat : removeShortcutsExcludedFromSurface) {
                arrayList.add(shortcutInfoCompat.mId);
            }
            ((ShortcutManager) context.getSystemService(ShortcutManager.class)).enableShortcuts(arrayList);
        }
        getShortcutInfoSaverInstance(context).addShortcuts(removeShortcutsExcludedFromSurface);
        for (ShortcutInfoChangeListener onShortcutAdded : getShortcutInfoListeners(context)) {
            onShortcutAdded.onShortcutAdded(list);
        }
    }

    public static List<ShortcutInfoCompat> getDynamicShortcuts(Context context) {
        if (Build.VERSION.SDK_INT >= 25) {
            List<ShortcutInfo> dynamicShortcuts = ((ShortcutManager) context.getSystemService(ShortcutManager.class)).getDynamicShortcuts();
            ArrayList arrayList = new ArrayList(dynamicShortcuts.size());
            for (ShortcutInfo builder : dynamicShortcuts) {
                arrayList.add(new ShortcutInfoCompat.Builder(context, builder).build());
            }
            return arrayList;
        }
        try {
            return getShortcutInfoSaverInstance(context).getShortcuts();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    private static int getIconDimensionInternal(Context context, boolean isHorizontal) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        int max = Math.max(1, Build.VERSION.SDK_INT < 19 || activityManager == null || activityManager.isLowRamDevice() ? 48 : DEFAULT_MAX_ICON_DIMENSION_DP);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (((float) max) * ((isHorizontal ? displayMetrics.xdpi : displayMetrics.ydpi) / 160.0f));
    }

    public static int getIconMaxHeight(Context context) {
        Preconditions.checkNotNull(context);
        return Build.VERSION.SDK_INT >= 25 ? ((ShortcutManager) context.getSystemService(ShortcutManager.class)).getIconMaxHeight() : getIconDimensionInternal(context, false);
    }

    public static int getIconMaxWidth(Context context) {
        Preconditions.checkNotNull(context);
        return Build.VERSION.SDK_INT >= 25 ? ((ShortcutManager) context.getSystemService(ShortcutManager.class)).getIconMaxWidth() : getIconDimensionInternal(context, true);
    }

    public static int getMaxShortcutCountPerActivity(Context context) {
        Preconditions.checkNotNull(context);
        if (Build.VERSION.SDK_INT >= 25) {
            return ((ShortcutManager) context.getSystemService(ShortcutManager.class)).getMaxShortcutCountPerActivity();
        }
        return 5;
    }

    static List<ShortcutInfoChangeListener> getShortcutInfoChangeListeners() {
        return sShortcutInfoChangeListeners;
    }

    private static String getShortcutInfoCompatWithLowestRank(List<ShortcutInfoCompat> list) {
        int i = -1;
        String str = null;
        for (ShortcutInfoCompat next : list) {
            if (next.getRank() > i) {
                str = next.getId();
                i = next.getRank();
            }
        }
        return str;
    }

    private static List<ShortcutInfoChangeListener> getShortcutInfoListeners(Context context) {
        Bundle bundle;
        String string;
        if (sShortcutInfoChangeListeners == null) {
            ArrayList arrayList = new ArrayList();
            if (Build.VERSION.SDK_INT >= 21) {
                PackageManager packageManager = context.getPackageManager();
                Intent intent = new Intent(SHORTCUT_LISTENER_INTENT_FILTER_ACTION);
                intent.setPackage(context.getPackageName());
                for (ResolveInfo resolveInfo : packageManager.queryIntentActivities(intent, 128)) {
                    ActivityInfo activityInfo = resolveInfo.activityInfo;
                    if (!(activityInfo == null || (bundle = activityInfo.metaData) == null || (string = bundle.getString(SHORTCUT_LISTENER_META_DATA_KEY)) == null)) {
                        try {
                            arrayList.add((ShortcutInfoChangeListener) Class.forName(string, false, ShortcutManagerCompat.class.getClassLoader()).getMethod("getInstance", new Class[]{Context.class}).invoke((Object) null, new Object[]{context}));
                        } catch (Exception e) {
                        }
                    }
                }
            }
            if (sShortcutInfoChangeListeners == null) {
                sShortcutInfoChangeListeners = arrayList;
            }
        }
        return sShortcutInfoChangeListeners;
    }

    private static ShortcutInfoCompatSaver<?> getShortcutInfoSaverInstance(Context context) {
        if (sShortcutInfoCompatSaver == null) {
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    sShortcutInfoCompatSaver = (ShortcutInfoCompatSaver) Class.forName("androidx.sharetarget.ShortcutInfoCompatSaverImpl", false, ShortcutManagerCompat.class.getClassLoader()).getMethod("getInstance", new Class[]{Context.class}).invoke((Object) null, new Object[]{context});
                } catch (Exception e) {
                }
            }
            if (sShortcutInfoCompatSaver == null) {
                sShortcutInfoCompatSaver = new ShortcutInfoCompatSaver.NoopImpl();
            }
        }
        return sShortcutInfoCompatSaver;
    }

    public static List<ShortcutInfoCompat> getShortcuts(Context context, int matchFlags) {
        if (Build.VERSION.SDK_INT >= 30) {
            return ShortcutInfoCompat.fromShortcuts(context, ((ShortcutManager) context.getSystemService(ShortcutManager.class)).getShortcuts(matchFlags));
        }
        if (Build.VERSION.SDK_INT >= 25) {
            ShortcutManager shortcutManager = (ShortcutManager) context.getSystemService(ShortcutManager.class);
            ArrayList arrayList = new ArrayList();
            if ((matchFlags & 1) != 0) {
                arrayList.addAll(shortcutManager.getManifestShortcuts());
            }
            if ((matchFlags & 2) != 0) {
                arrayList.addAll(shortcutManager.getDynamicShortcuts());
            }
            if ((matchFlags & 4) != 0) {
                arrayList.addAll(shortcutManager.getPinnedShortcuts());
            }
            return ShortcutInfoCompat.fromShortcuts(context, arrayList);
        }
        if ((matchFlags & 2) != 0) {
            try {
                return getShortcutInfoSaverInstance(context).getShortcuts();
            } catch (Exception e) {
            }
        }
        return Collections.emptyList();
    }

    public static boolean isRateLimitingActive(Context context) {
        Preconditions.checkNotNull(context);
        return Build.VERSION.SDK_INT >= 25 ? ((ShortcutManager) context.getSystemService(ShortcutManager.class)).isRateLimitingActive() : getShortcuts(context, 3).size() == getMaxShortcutCountPerActivity(context);
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0036  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isRequestPinShortcutSupported(android.content.Context r6) {
        /*
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 26
            if (r0 < r1) goto L_0x0013
            java.lang.Class<android.content.pm.ShortcutManager> r0 = android.content.pm.ShortcutManager.class
            java.lang.Object r0 = r6.getSystemService(r0)
            android.content.pm.ShortcutManager r0 = (android.content.pm.ShortcutManager) r0
            boolean r0 = r0.isRequestPinShortcutSupported()
            return r0
        L_0x0013:
            java.lang.String r0 = "com.android.launcher.permission.INSTALL_SHORTCUT"
            int r1 = androidx.core.content.ContextCompat.checkSelfPermission(r6, r0)
            r2 = 0
            if (r1 == 0) goto L_0x001d
            return r2
        L_0x001d:
            android.content.pm.PackageManager r1 = r6.getPackageManager()
            android.content.Intent r3 = new android.content.Intent
            java.lang.String r4 = "com.android.launcher.action.INSTALL_SHORTCUT"
            r3.<init>(r4)
            java.util.List r1 = r1.queryBroadcastReceivers(r3, r2)
            java.util.Iterator r1 = r1.iterator()
        L_0x0030:
            boolean r3 = r1.hasNext()
            if (r3 == 0) goto L_0x0050
            java.lang.Object r3 = r1.next()
            android.content.pm.ResolveInfo r3 = (android.content.pm.ResolveInfo) r3
            android.content.pm.ActivityInfo r4 = r3.activityInfo
            java.lang.String r4 = r4.permission
            boolean r5 = android.text.TextUtils.isEmpty(r4)
            if (r5 != 0) goto L_0x004e
            boolean r5 = r0.equals(r4)
            if (r5 == 0) goto L_0x004d
            goto L_0x004e
        L_0x004d:
            goto L_0x0030
        L_0x004e:
            r0 = 1
            return r0
        L_0x0050:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.pm.ShortcutManagerCompat.isRequestPinShortcutSupported(android.content.Context):boolean");
    }

    /*  JADX ERROR: StackOverflow in pass: MarkFinallyVisitor
        jadx.core.utils.exceptions.JadxOverflowException: 
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    public static boolean pushDynamicShortcut(android.content.Context r7, androidx.core.content.pm.ShortcutInfoCompat r8) {
        /*
            androidx.core.util.Preconditions.checkNotNull(r7)
            androidx.core.util.Preconditions.checkNotNull(r8)
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 1
            r2 = 31
            if (r0 > r2) goto L_0x0030
            boolean r0 = r8.isExcludedFromSurfaces(r1)
            if (r0 == 0) goto L_0x0030
            java.util.List r0 = getShortcutInfoListeners(r7)
            java.util.Iterator r0 = r0.iterator()
        L_0x001b:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x002f
            java.lang.Object r2 = r0.next()
            androidx.core.content.pm.ShortcutInfoChangeListener r2 = (androidx.core.content.pm.ShortcutInfoChangeListener) r2
            java.util.List r3 = java.util.Collections.singletonList(r8)
            r2.onShortcutAdded(r3)
            goto L_0x001b
        L_0x002f:
            return r1
        L_0x0030:
            int r0 = getMaxShortcutCountPerActivity(r7)
            r2 = 0
            if (r0 != 0) goto L_0x0038
            return r2
        L_0x0038:
            int r3 = android.os.Build.VERSION.SDK_INT
            r4 = 29
            if (r3 > r4) goto L_0x0041
            convertUriIconToBitmapIcon(r7, r8)
        L_0x0041:
            int r3 = android.os.Build.VERSION.SDK_INT
            r4 = 30
            if (r3 < r4) goto L_0x0057
            java.lang.Class<android.content.pm.ShortcutManager> r3 = android.content.pm.ShortcutManager.class
            java.lang.Object r3 = r7.getSystemService(r3)
            android.content.pm.ShortcutManager r3 = (android.content.pm.ShortcutManager) r3
            android.content.pm.ShortcutInfo r4 = r8.toShortcutInfo()
            r3.pushDynamicShortcut(r4)
            goto L_0x0097
        L_0x0057:
            int r3 = android.os.Build.VERSION.SDK_INT
            r4 = 25
            if (r3 < r4) goto L_0x0097
            java.lang.Class<android.content.pm.ShortcutManager> r3 = android.content.pm.ShortcutManager.class
            java.lang.Object r3 = r7.getSystemService(r3)
            android.content.pm.ShortcutManager r3 = (android.content.pm.ShortcutManager) r3
            boolean r4 = r3.isRateLimitingActive()
            if (r4 == 0) goto L_0x006c
            return r2
        L_0x006c:
            java.util.List r4 = r3.getDynamicShortcuts()
            int r5 = r4.size()
            if (r5 < r0) goto L_0x0088
            java.lang.String[] r5 = new java.lang.String[r1]
            java.lang.String r6 = androidx.core.content.pm.ShortcutManagerCompat.Api25Impl.getShortcutInfoWithLowestRank(r4)
            mt.Log1F380D.a((java.lang.Object) r6)
            r5[r2] = r6
            java.util.List r5 = java.util.Arrays.asList(r5)
            r3.removeDynamicShortcuts(r5)
        L_0x0088:
            android.content.pm.ShortcutInfo[] r5 = new android.content.pm.ShortcutInfo[r1]
            android.content.pm.ShortcutInfo r6 = r8.toShortcutInfo()
            r5[r2] = r6
            java.util.List r5 = java.util.Arrays.asList(r5)
            r3.addDynamicShortcuts(r5)
        L_0x0097:
            androidx.core.content.pm.ShortcutInfoCompatSaver r3 = getShortcutInfoSaverInstance(r7)
            java.util.List r4 = r3.getShortcuts()     // Catch:{ Exception -> 0x010c, all -> 0x00e7 }
            int r5 = r4.size()     // Catch:{ Exception -> 0x010c, all -> 0x00e7 }
            if (r5 < r0) goto L_0x00b7
            java.lang.String[] r5 = new java.lang.String[r1]     // Catch:{ Exception -> 0x010c, all -> 0x00e7 }
            java.lang.String r6 = getShortcutInfoCompatWithLowestRank(r4)     // Catch:{ Exception -> 0x010c, all -> 0x00e7 }
            mt.Log1F380D.a((java.lang.Object) r6)
            r5[r2] = r6     // Catch:{ Exception -> 0x010c, all -> 0x00e7 }
            java.util.List r5 = java.util.Arrays.asList(r5)     // Catch:{ Exception -> 0x010c, all -> 0x00e7 }
            r3.removeShortcuts(r5)     // Catch:{ Exception -> 0x010c, all -> 0x00e7 }
        L_0x00b7:
            androidx.core.content.pm.ShortcutInfoCompat[] r5 = new androidx.core.content.pm.ShortcutInfoCompat[r1]     // Catch:{ Exception -> 0x010c, all -> 0x00e7 }
            r5[r2] = r8     // Catch:{ Exception -> 0x010c, all -> 0x00e7 }
            java.util.List r5 = java.util.Arrays.asList(r5)     // Catch:{ Exception -> 0x010c, all -> 0x00e7 }
            r3.addShortcuts(r5)     // Catch:{ Exception -> 0x010c, all -> 0x00e7 }
            java.util.List r2 = getShortcutInfoListeners(r7)
            java.util.Iterator r2 = r2.iterator()
        L_0x00cb:
            boolean r5 = r2.hasNext()
            if (r5 == 0) goto L_0x00df
            java.lang.Object r5 = r2.next()
            androidx.core.content.pm.ShortcutInfoChangeListener r5 = (androidx.core.content.pm.ShortcutInfoChangeListener) r5
            java.util.List r6 = java.util.Collections.singletonList(r8)
            r5.onShortcutAdded(r6)
            goto L_0x00cb
        L_0x00df:
            java.lang.String r2 = r8.getId()
            reportShortcutUsed(r7, r2)
            return r1
        L_0x00e7:
            r1 = move-exception
            java.util.List r2 = getShortcutInfoListeners(r7)
            java.util.Iterator r2 = r2.iterator()
        L_0x00f0:
            boolean r4 = r2.hasNext()
            if (r4 == 0) goto L_0x0104
            java.lang.Object r4 = r2.next()
            androidx.core.content.pm.ShortcutInfoChangeListener r4 = (androidx.core.content.pm.ShortcutInfoChangeListener) r4
            java.util.List r5 = java.util.Collections.singletonList(r8)
            r4.onShortcutAdded(r5)
            goto L_0x00f0
        L_0x0104:
            java.lang.String r2 = r8.getId()
            reportShortcutUsed(r7, r2)
            throw r1
        L_0x010c:
            r1 = move-exception
            java.util.List r1 = getShortcutInfoListeners(r7)
            java.util.Iterator r1 = r1.iterator()
        L_0x0115:
            boolean r4 = r1.hasNext()
            if (r4 == 0) goto L_0x0129
            java.lang.Object r4 = r1.next()
            androidx.core.content.pm.ShortcutInfoChangeListener r4 = (androidx.core.content.pm.ShortcutInfoChangeListener) r4
            java.util.List r5 = java.util.Collections.singletonList(r8)
            r4.onShortcutAdded(r5)
            goto L_0x0115
        L_0x0129:
            java.lang.String r1 = r8.getId()
            reportShortcutUsed(r7, r1)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.pm.ShortcutManagerCompat.pushDynamicShortcut(android.content.Context, androidx.core.content.pm.ShortcutInfoCompat):boolean");
    }

    public static void removeAllDynamicShortcuts(Context context) {
        if (Build.VERSION.SDK_INT >= 25) {
            ((ShortcutManager) context.getSystemService(ShortcutManager.class)).removeAllDynamicShortcuts();
        }
        getShortcutInfoSaverInstance(context).removeAllShortcuts();
        for (ShortcutInfoChangeListener onAllShortcutsRemoved : getShortcutInfoListeners(context)) {
            onAllShortcutsRemoved.onAllShortcutsRemoved();
        }
    }

    public static void removeDynamicShortcuts(Context context, List<String> list) {
        if (Build.VERSION.SDK_INT >= 25) {
            ((ShortcutManager) context.getSystemService(ShortcutManager.class)).removeDynamicShortcuts(list);
        }
        getShortcutInfoSaverInstance(context).removeShortcuts(list);
        for (ShortcutInfoChangeListener onShortcutRemoved : getShortcutInfoListeners(context)) {
            onShortcutRemoved.onShortcutRemoved(list);
        }
    }

    public static void removeLongLivedShortcuts(Context context, List<String> list) {
        if (Build.VERSION.SDK_INT < 30) {
            removeDynamicShortcuts(context, list);
            return;
        }
        ((ShortcutManager) context.getSystemService(ShortcutManager.class)).removeLongLivedShortcuts(list);
        getShortcutInfoSaverInstance(context).removeShortcuts(list);
        for (ShortcutInfoChangeListener onShortcutRemoved : getShortcutInfoListeners(context)) {
            onShortcutRemoved.onShortcutRemoved(list);
        }
    }

    private static List<ShortcutInfoCompat> removeShortcutsExcludedFromSurface(List<ShortcutInfoCompat> list, int surfaces) {
        Objects.requireNonNull(list);
        if (Build.VERSION.SDK_INT > 31) {
            return list;
        }
        ArrayList arrayList = new ArrayList(list);
        for (ShortcutInfoCompat next : list) {
            if (next.isExcludedFromSurfaces(surfaces)) {
                arrayList.remove(next);
            }
        }
        return arrayList;
    }

    public static void reportShortcutUsed(Context context, String shortcutId) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(shortcutId);
        if (Build.VERSION.SDK_INT >= 25) {
            ((ShortcutManager) context.getSystemService(ShortcutManager.class)).reportShortcutUsed(shortcutId);
        }
        for (ShortcutInfoChangeListener onShortcutUsageReported : getShortcutInfoListeners(context)) {
            onShortcutUsageReported.onShortcutUsageReported(Collections.singletonList(shortcutId));
        }
    }

    public static boolean requestPinShortcut(Context context, ShortcutInfoCompat shortcut, final IntentSender callback) {
        if (Build.VERSION.SDK_INT <= 31 && shortcut.isExcludedFromSurfaces(1)) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 26) {
            return ((ShortcutManager) context.getSystemService(ShortcutManager.class)).requestPinShortcut(shortcut.toShortcutInfo(), callback);
        }
        if (!isRequestPinShortcutSupported(context)) {
            return false;
        }
        Intent addToIntent = shortcut.addToIntent(new Intent(ACTION_INSTALL_SHORTCUT));
        if (callback == null) {
            context.sendBroadcast(addToIntent);
            return true;
        }
        context.sendOrderedBroadcast(addToIntent, (String) null, new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                try {
                    callback.sendIntent(context, 0, (Intent) null, (IntentSender.OnFinished) null, (Handler) null);
                } catch (IntentSender.SendIntentException e) {
                }
            }
        }, (Handler) null, -1, (String) null, (Bundle) null);
        return true;
    }

    public static boolean setDynamicShortcuts(Context context, List<ShortcutInfoCompat> list) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(list);
        List<ShortcutInfoCompat> removeShortcutsExcludedFromSurface = removeShortcutsExcludedFromSurface(list, 1);
        if (Build.VERSION.SDK_INT >= 25) {
            ArrayList arrayList = new ArrayList(removeShortcutsExcludedFromSurface.size());
            for (ShortcutInfoCompat shortcutInfo : removeShortcutsExcludedFromSurface) {
                arrayList.add(shortcutInfo.toShortcutInfo());
            }
            if (!((ShortcutManager) context.getSystemService(ShortcutManager.class)).setDynamicShortcuts(arrayList)) {
                return false;
            }
        }
        getShortcutInfoSaverInstance(context).removeAllShortcuts();
        getShortcutInfoSaverInstance(context).addShortcuts(removeShortcutsExcludedFromSurface);
        for (ShortcutInfoChangeListener next : getShortcutInfoListeners(context)) {
            next.onAllShortcutsRemoved();
            next.onShortcutAdded(list);
        }
        return true;
    }

    static void setShortcutInfoChangeListeners(List<ShortcutInfoChangeListener> list) {
        sShortcutInfoChangeListeners = list;
    }

    static void setShortcutInfoCompatSaver(ShortcutInfoCompatSaver<Void> shortcutInfoCompatSaver) {
        sShortcutInfoCompatSaver = shortcutInfoCompatSaver;
    }

    public static boolean updateShortcuts(Context context, List<ShortcutInfoCompat> list) {
        List<ShortcutInfoCompat> removeShortcutsExcludedFromSurface = removeShortcutsExcludedFromSurface(list, 1);
        if (Build.VERSION.SDK_INT <= 29) {
            convertUriIconsToBitmapIcons(context, removeShortcutsExcludedFromSurface);
        }
        if (Build.VERSION.SDK_INT >= 25) {
            ArrayList arrayList = new ArrayList();
            for (ShortcutInfoCompat shortcutInfo : removeShortcutsExcludedFromSurface) {
                arrayList.add(shortcutInfo.toShortcutInfo());
            }
            if (!((ShortcutManager) context.getSystemService(ShortcutManager.class)).updateShortcuts(arrayList)) {
                return false;
            }
        }
        getShortcutInfoSaverInstance(context).addShortcuts(removeShortcutsExcludedFromSurface);
        for (ShortcutInfoChangeListener onShortcutUpdated : getShortcutInfoListeners(context)) {
            onShortcutUpdated.onShortcutUpdated(list);
        }
        return true;
    }
}
