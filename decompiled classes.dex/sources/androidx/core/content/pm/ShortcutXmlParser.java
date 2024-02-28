package androidx.core.content.pm;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mt.Log1F380D;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: 003E */
public class ShortcutXmlParser {
    private static final String ATTR_SHORTCUT_ID = "shortcutId";
    private static final Object GET_INSTANCE_LOCK = new Object();
    private static final String META_DATA_APP_SHORTCUTS = "android.app.shortcuts";
    private static final String TAG = "ShortcutXmlParser";
    private static final String TAG_SHORTCUT = "shortcut";
    private static volatile ArrayList<String> sShortcutIds;

    private ShortcutXmlParser() {
    }

    private static String getAttributeValue(XmlPullParser parser, String attribute) {
        String attributeValue = parser.getAttributeValue("http://schemas.android.com/apk/res/android", attribute);
        return attributeValue == null ? parser.getAttributeValue((String) null, attribute) : attributeValue;
    }

    public static List<String> getShortcutIds(Context context) {
        if (sShortcutIds == null) {
            synchronized (GET_INSTANCE_LOCK) {
                if (sShortcutIds == null) {
                    sShortcutIds = new ArrayList<>();
                    sShortcutIds.addAll(parseShortcutIds(context));
                }
            }
        }
        return sShortcutIds;
    }

    private static XmlResourceParser getXmlResourceParser(Context context, ActivityInfo info) {
        XmlResourceParser loadXmlMetaData = info.loadXmlMetaData(context.getPackageManager(), META_DATA_APP_SHORTCUTS);
        if (loadXmlMetaData != null) {
            return loadXmlMetaData;
        }
        throw new IllegalArgumentException("Failed to open android.app.shortcuts meta-data resource of " + info.name);
    }

    public static List<String> parseShortcutIds(XmlPullParser parser) throws IOException, XmlPullParserException {
        ArrayList arrayList = new ArrayList(1);
        while (true) {
            int next = parser.next();
            int i = next;
            if (next == 1 || (i == 3 && parser.getDepth() <= 0)) {
                return arrayList;
            }
            int depth = parser.getDepth();
            String name = parser.getName();
            if (i == 2 && depth == 2 && TAG_SHORTCUT.equals(name)) {
                String attributeValue = getAttributeValue(parser, ATTR_SHORTCUT_ID);
                Log1F380D.a((Object) attributeValue);
                if (attributeValue != null) {
                    arrayList.add(attributeValue);
                }
            }
        }
        return arrayList;
    }

    private static Set<String> parseShortcutIds(Context context) {
        XmlResourceParser xmlResourceParser;
        HashSet hashSet = new HashSet();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setPackage(context.getPackageName());
        List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 128);
        if (queryIntentActivities == null || queryIntentActivities.size() == 0) {
            return hashSet;
        }
        try {
            for (ResolveInfo resolveInfo : queryIntentActivities) {
                ActivityInfo activityInfo = resolveInfo.activityInfo;
                Bundle bundle = activityInfo.metaData;
                if (bundle != null && bundle.containsKey(META_DATA_APP_SHORTCUTS)) {
                    xmlResourceParser = getXmlResourceParser(context, activityInfo);
                    hashSet.addAll(parseShortcutIds((XmlPullParser) xmlResourceParser));
                    if (xmlResourceParser != null) {
                        xmlResourceParser.close();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse the Xml resource: ", e);
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        return hashSet;
        throw th;
    }
}
