package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.Parcelable;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParcelUtils {
    private static final String INNER_BUNDLE_KEY = "a";

    private ParcelUtils() {
    }

    public static <T extends VersionedParcelable> T fromInputStream(InputStream input) {
        return new VersionedParcelStream(input, (OutputStream) null).readVersionedParcelable();
    }

    public static <T extends VersionedParcelable> T fromParcelable(Parcelable p) {
        if (p instanceof ParcelImpl) {
            return ((ParcelImpl) p).getVersionedParcel();
        }
        throw new IllegalArgumentException("Invalid parcel");
    }

    public static <T extends VersionedParcelable> T getVersionedParcelable(Bundle bundle, String key) {
        try {
            Bundle bundle2 = (Bundle) bundle.getParcelable(key);
            if (bundle2 == null) {
                return null;
            }
            bundle2.setClassLoader(ParcelUtils.class.getClassLoader());
            return fromParcelable(bundle2.getParcelable(INNER_BUNDLE_KEY));
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static <T extends VersionedParcelable> List<T> getVersionedParcelableList(Bundle bundle, String key) {
        ArrayList arrayList = new ArrayList();
        try {
            Bundle bundle2 = (Bundle) bundle.getParcelable(key);
            bundle2.setClassLoader(ParcelUtils.class.getClassLoader());
            Iterator it = bundle2.getParcelableArrayList(INNER_BUNDLE_KEY).iterator();
            while (it.hasNext()) {
                arrayList.add(fromParcelable((Parcelable) it.next()));
            }
            return arrayList;
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static void putVersionedParcelable(Bundle b, String key, VersionedParcelable obj) {
        if (obj != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(INNER_BUNDLE_KEY, toParcelable(obj));
            b.putParcelable(key, bundle);
        }
    }

    public static void putVersionedParcelableList(Bundle b, String key, List<? extends VersionedParcelable> list) {
        Bundle bundle = new Bundle();
        ArrayList arrayList = new ArrayList();
        for (VersionedParcelable parcelable : list) {
            arrayList.add(toParcelable(parcelable));
        }
        bundle.putParcelableArrayList(INNER_BUNDLE_KEY, arrayList);
        b.putParcelable(key, bundle);
    }

    public static void toOutputStream(VersionedParcelable obj, OutputStream output) {
        VersionedParcelStream versionedParcelStream = new VersionedParcelStream((InputStream) null, output);
        versionedParcelStream.writeVersionedParcelable(obj);
        versionedParcelStream.closeField();
    }

    public static Parcelable toParcelable(VersionedParcelable obj) {
        return new ParcelImpl(obj);
    }
}
