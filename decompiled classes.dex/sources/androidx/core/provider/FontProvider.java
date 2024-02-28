package androidx.core.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class FontProvider {
    private static final Comparator<byte[]> sByteArrayComparator = new FontProvider$$ExternalSyntheticLambda0();

    static class Api16Impl {
        private Api16Impl() {
        }

        static Cursor query(ContentResolver contentResolver, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, Object cancellationSignal) {
            return contentResolver.query(uri, projection, selection, selectionArgs, sortOrder, (CancellationSignal) cancellationSignal);
        }
    }

    private FontProvider() {
    }

    private static List<byte[]> convertToByteArrayList(Signature[] signatures) {
        ArrayList arrayList = new ArrayList();
        for (Signature byteArray : signatures) {
            arrayList.add(byteArray.toByteArray());
        }
        return arrayList;
    }

    private static boolean equalsByteArrayList(List<byte[]> list, List<byte[]> list2) {
        if (list.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (!Arrays.equals(list.get(i), list2.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static List<List<byte[]>> getCertificates(FontRequest request, Resources resources) {
        return request.getCertificates() != null ? request.getCertificates() : FontResourcesParserCompat.readCerts(resources, request.getCertificatesArrayResId());
    }

    static FontsContractCompat.FontFamilyResult getFontFamilyResult(Context context, FontRequest request, CancellationSignal cancellationSignal) throws PackageManager.NameNotFoundException {
        ProviderInfo provider = getProvider(context.getPackageManager(), request, context.getResources());
        return provider == null ? FontsContractCompat.FontFamilyResult.create(1, (FontsContractCompat.FontInfo[]) null) : FontsContractCompat.FontFamilyResult.create(0, query(context, request, provider.authority, cancellationSignal));
    }

    static ProviderInfo getProvider(PackageManager packageManager, FontRequest request, Resources resources) throws PackageManager.NameNotFoundException {
        String providerAuthority = request.getProviderAuthority();
        ProviderInfo resolveContentProvider = packageManager.resolveContentProvider(providerAuthority, 0);
        if (resolveContentProvider == null) {
            throw new PackageManager.NameNotFoundException("No package found for authority: " + providerAuthority);
        } else if (resolveContentProvider.packageName.equals(request.getProviderPackage())) {
            List<byte[]> convertToByteArrayList = convertToByteArrayList(packageManager.getPackageInfo(resolveContentProvider.packageName, 64).signatures);
            Collections.sort(convertToByteArrayList, sByteArrayComparator);
            List<List<byte[]>> certificates = getCertificates(request, resources);
            for (int i = 0; i < certificates.size(); i++) {
                ArrayList arrayList = new ArrayList(certificates.get(i));
                Collections.sort(arrayList, sByteArrayComparator);
                if (equalsByteArrayList(convertToByteArrayList, arrayList)) {
                    return resolveContentProvider;
                }
            }
            return null;
        } else {
            throw new PackageManager.NameNotFoundException("Found content provider " + providerAuthority + ", but package was not " + request.getProviderPackage());
        }
    }

    static /* synthetic */ int lambda$static$0(byte[] l, byte[] r) {
        if (l.length != r.length) {
            return l.length - r.length;
        }
        for (int i = 0; i < l.length; i++) {
            if (l[i] != r[i]) {
                return l[i] - r[i];
            }
        }
        return 0;
    }

    static FontsContractCompat.FontInfo[] query(Context context, FontRequest request, String authority, CancellationSignal cancellationSignal) {
        int i;
        String[] strArr;
        Uri uri;
        Cursor query;
        String str = authority;
        ArrayList arrayList = new ArrayList();
        Uri build = new Uri.Builder().scheme("content").authority(str).build();
        Uri build2 = new Uri.Builder().scheme("content").authority(str).appendPath("file").build();
        Cursor cursor = null;
        try {
            String[] strArr2 = {"_id", FontsContractCompat.Columns.FILE_ID, FontsContractCompat.Columns.TTC_INDEX, FontsContractCompat.Columns.VARIATION_SETTINGS, FontsContractCompat.Columns.WEIGHT, FontsContractCompat.Columns.ITALIC, FontsContractCompat.Columns.RESULT_CODE};
            ContentResolver contentResolver = context.getContentResolver();
            boolean z = true;
            if (Build.VERSION.SDK_INT > 16) {
                query = Api16Impl.query(contentResolver, build, strArr2, "query = ?", new String[]{request.getQuery()}, (String) null, cancellationSignal);
                i = 0;
            } else {
                i = 0;
                query = contentResolver.query(build, strArr2, "query = ?", new String[]{request.getQuery()}, (String) null);
            }
            if (cursor == null || cursor.getCount() <= 0) {
                String[] strArr3 = strArr2;
            } else {
                int columnIndex = cursor.getColumnIndex(FontsContractCompat.Columns.RESULT_CODE);
                arrayList = new ArrayList();
                int columnIndex2 = cursor.getColumnIndex("_id");
                int columnIndex3 = cursor.getColumnIndex(FontsContractCompat.Columns.FILE_ID);
                int columnIndex4 = cursor.getColumnIndex(FontsContractCompat.Columns.TTC_INDEX);
                int columnIndex5 = cursor.getColumnIndex(FontsContractCompat.Columns.WEIGHT);
                int columnIndex6 = cursor.getColumnIndex(FontsContractCompat.Columns.ITALIC);
                while (cursor.moveToNext()) {
                    int i2 = columnIndex != -1 ? cursor.getInt(columnIndex) : i;
                    int i3 = columnIndex4 != -1 ? cursor.getInt(columnIndex4) : i;
                    if (columnIndex3 == -1) {
                        strArr = strArr2;
                        uri = ContentUris.withAppendedId(build, cursor.getLong(columnIndex2));
                    } else {
                        strArr = strArr2;
                        uri = ContentUris.withAppendedId(build2, cursor.getLong(columnIndex3));
                    }
                    int i4 = columnIndex5 != -1 ? cursor.getInt(columnIndex5) : 400;
                    boolean z2 = (columnIndex6 == -1 || cursor.getInt(columnIndex6) != z) ? false : z;
                    int i5 = i2;
                    int i6 = columnIndex;
                    arrayList.add(FontsContractCompat.FontInfo.create(uri, i3, i4, z2, i5));
                    String str2 = authority;
                    strArr2 = strArr;
                    columnIndex = i6;
                    i = 0;
                    z = true;
                }
                int i7 = columnIndex;
                String[] strArr4 = strArr2;
            }
            return (FontsContractCompat.FontInfo[]) arrayList.toArray(new FontsContractCompat.FontInfo[0]);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
