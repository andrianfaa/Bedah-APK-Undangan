package androidx.core.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import mt.Log1F380D;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider extends ContentProvider {
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final String[] COLUMNS = {"_display_name", "_size"};
    private static final File DEVICE_ROOT = new File("/");
    private static final String DISPLAYNAME_FIELD = "displayName";
    private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
    private static final String TAG_CACHE_PATH = "cache-path";
    private static final String TAG_EXTERNAL = "external-path";
    private static final String TAG_EXTERNAL_CACHE = "external-cache-path";
    private static final String TAG_EXTERNAL_FILES = "external-files-path";
    private static final String TAG_EXTERNAL_MEDIA = "external-media-path";
    private static final String TAG_FILES_PATH = "files-path";
    private static final String TAG_ROOT_PATH = "root-path";
    private static final HashMap<String, PathStrategy> sCache = new HashMap<>();
    private int mResourceId;
    private PathStrategy mStrategy;

    static class Api21Impl {
        private Api21Impl() {
        }

        static File[] getExternalMediaDirs(Context context) {
            return context.getExternalMediaDirs();
        }
    }

    interface PathStrategy {
        File getFileForUri(Uri uri);

        Uri getUriForFile(File file);
    }

    public FileProvider() {
        this.mResourceId = 0;
    }

    protected FileProvider(int resourceId) {
        this.mResourceId = resourceId;
    }

    private static File buildPath(File base, String... segments) {
        File file = base;
        for (String str : segments) {
            if (str != null) {
                file = new File(file, str);
            }
        }
        return file;
    }

    private static Object[] copyOf(Object[] original, int newLength) {
        Object[] objArr = new Object[newLength];
        System.arraycopy(original, 0, objArr, 0, newLength);
        return objArr;
    }

    private static String[] copyOf(String[] original, int newLength) {
        String[] strArr = new String[newLength];
        System.arraycopy(original, 0, strArr, 0, newLength);
        return strArr;
    }

    static XmlResourceParser getFileProviderPathsMetaData(Context context, String authority, ProviderInfo info, int resourceId) {
        if (info != null) {
            if (info.metaData == null && resourceId != 0) {
                info.metaData = new Bundle(1);
                info.metaData.putInt(META_DATA_FILE_PROVIDER_PATHS, resourceId);
            }
            XmlResourceParser loadXmlMetaData = info.loadXmlMetaData(context.getPackageManager(), META_DATA_FILE_PROVIDER_PATHS);
            if (loadXmlMetaData != null) {
                return loadXmlMetaData;
            }
            throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
        }
        throw new IllegalArgumentException("Couldn't find meta-data for provider with authority " + authority);
    }

    private static PathStrategy getPathStrategy(Context context, String authority, int resourceId) {
        PathStrategy pathStrategy;
        HashMap<String, PathStrategy> hashMap = sCache;
        synchronized (hashMap) {
            pathStrategy = hashMap.get(authority);
            if (pathStrategy == null) {
                try {
                    pathStrategy = parsePathStrategy(context, authority, resourceId);
                    hashMap.put(authority, pathStrategy);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", e);
                } catch (XmlPullParserException e2) {
                    throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", e2);
                }
            }
        }
        return pathStrategy;
    }

    public static Uri getUriForFile(Context context, String authority, File file) {
        return getPathStrategy(context, authority, 0).getUriForFile(file);
    }

    public static Uri getUriForFile(Context context, String authority, File file, String displayName) {
        return getUriForFile(context, authority, file).buildUpon().appendQueryParameter(DISPLAYNAME_FIELD, displayName).build();
    }

    private static int modeToMode(String mode) {
        if ("r".equals(mode)) {
            return 268435456;
        }
        if ("w".equals(mode) || "wt".equals(mode)) {
            return 738197504;
        }
        if ("wa".equals(mode)) {
            return 704643072;
        }
        if ("rw".equals(mode)) {
            return 939524096;
        }
        if ("rwt".equals(mode)) {
            return 1006632960;
        }
        throw new IllegalArgumentException("Invalid mode: " + mode);
    }

    private static PathStrategy parsePathStrategy(Context context, String authority, int resourceId) throws IOException, XmlPullParserException {
        SimplePathStrategy simplePathStrategy = new SimplePathStrategy(authority);
        XmlResourceParser fileProviderPathsMetaData = getFileProviderPathsMetaData(context, authority, context.getPackageManager().resolveContentProvider(authority, 128), resourceId);
        while (true) {
            int next = fileProviderPathsMetaData.next();
            int i = next;
            if (next == 1) {
                return simplePathStrategy;
            }
            if (i == 2) {
                String name = fileProviderPathsMetaData.getName();
                String attributeValue = fileProviderPathsMetaData.getAttributeValue((String) null, ATTR_NAME);
                String attributeValue2 = fileProviderPathsMetaData.getAttributeValue((String) null, ATTR_PATH);
                File file = null;
                if (TAG_ROOT_PATH.equals(name)) {
                    file = DEVICE_ROOT;
                } else if (TAG_FILES_PATH.equals(name)) {
                    file = context.getFilesDir();
                } else if (TAG_CACHE_PATH.equals(name)) {
                    file = context.getCacheDir();
                } else if (TAG_EXTERNAL.equals(name)) {
                    file = Environment.getExternalStorageDirectory();
                } else if (TAG_EXTERNAL_FILES.equals(name)) {
                    File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(context, (String) null);
                    if (externalFilesDirs.length > 0) {
                        file = externalFilesDirs[0];
                    }
                } else if (TAG_EXTERNAL_CACHE.equals(name)) {
                    File[] externalCacheDirs = ContextCompat.getExternalCacheDirs(context);
                    if (externalCacheDirs.length > 0) {
                        file = externalCacheDirs[0];
                    }
                } else if (Build.VERSION.SDK_INT >= 21 && TAG_EXTERNAL_MEDIA.equals(name)) {
                    File[] externalMediaDirs = Api21Impl.getExternalMediaDirs(context);
                    if (externalMediaDirs.length > 0) {
                        file = externalMediaDirs[0];
                    }
                }
                if (file != null) {
                    simplePathStrategy.addRoot(attributeValue, buildPath(file, attributeValue2));
                }
            }
        }
    }

    public void attachInfo(Context context, ProviderInfo info) {
        super.attachInfo(context, info);
        if (info.exported) {
            throw new SecurityException("Provider must not be exported");
        } else if (info.grantUriPermissions) {
            String str = info.authority.split(";")[0];
            HashMap<String, PathStrategy> hashMap = sCache;
            synchronized (hashMap) {
                hashMap.remove(str);
            }
            this.mStrategy = getPathStrategy(context, str, this.mResourceId);
        } else {
            throw new SecurityException("Provider must grant uri permissions");
        }
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return this.mStrategy.getFileForUri(uri).delete() ? 1 : 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0012, code lost:
        r3 = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(r0.getName().substring(r1 + 1));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getType(android.net.Uri r5) {
        /*
            r4 = this;
            androidx.core.content.FileProvider$PathStrategy r0 = r4.mStrategy
            java.io.File r0 = r0.getFileForUri(r5)
            java.lang.String r1 = r0.getName()
            r2 = 46
            int r1 = r1.lastIndexOf(r2)
            if (r1 < 0) goto L_0x0027
            java.lang.String r2 = r0.getName()
            int r3 = r1 + 1
            java.lang.String r2 = r2.substring(r3)
            android.webkit.MimeTypeMap r3 = android.webkit.MimeTypeMap.getSingleton()
            java.lang.String r3 = r3.getMimeTypeFromExtension(r2)
            if (r3 == 0) goto L_0x0027
            return r3
        L_0x0027:
            java.lang.String r2 = "application/octet-stream"
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.FileProvider.getType(android.net.Uri):java.lang.String");
    }

    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("No external inserts");
    }

    public boolean onCreate() {
        return true;
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        return ParcelFileDescriptor.open(this.mStrategy.getFileForUri(uri), modeToMode(mode));
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Uri uri2 = uri;
        File fileForUri = this.mStrategy.getFileForUri(uri);
        String queryParameter = uri.getQueryParameter(DISPLAYNAME_FIELD);
        String[] strArr = projection == null ? COLUMNS : projection;
        String[] strArr2 = new String[strArr.length];
        Object[] objArr = new Object[strArr.length];
        int i = 0;
        for (String str : strArr) {
            if ("_display_name".equals(str)) {
                strArr2[i] = "_display_name";
                int i2 = i + 1;
                objArr[i] = queryParameter == null ? fileForUri.getName() : queryParameter;
                i = i2;
            } else if ("_size".equals(str)) {
                strArr2[i] = "_size";
                objArr[i] = Long.valueOf(fileForUri.length());
                i++;
            }
        }
        String[] copyOf = copyOf(strArr2, i);
        Object[] copyOf2 = copyOf(objArr, i);
        MatrixCursor matrixCursor = new MatrixCursor(copyOf, 1);
        matrixCursor.addRow(copyOf2);
        return matrixCursor;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("No external updates");
    }

    /* compiled from: 0036 */
    static class SimplePathStrategy implements PathStrategy {
        private final String mAuthority;
        private final HashMap<String, File> mRoots = new HashMap<>();

        SimplePathStrategy(String authority) {
            this.mAuthority = authority;
        }

        /* access modifiers changed from: package-private */
        public void addRoot(String name, File root) {
            if (!TextUtils.isEmpty(name)) {
                try {
                    this.mRoots.put(name, root.getCanonicalFile());
                } catch (IOException e) {
                    throw new IllegalArgumentException("Failed to resolve canonical path for " + root, e);
                }
            } else {
                throw new IllegalArgumentException("Name must not be empty");
            }
        }

        public File getFileForUri(Uri uri) {
            String encodedPath = uri.getEncodedPath();
            int indexOf = encodedPath.indexOf(47, 1);
            String decode = Uri.decode(encodedPath.substring(1, indexOf));
            Log1F380D.a((Object) decode);
            String decode2 = Uri.decode(encodedPath.substring(indexOf + 1));
            Log1F380D.a((Object) decode2);
            File file = this.mRoots.get(decode);
            if (file != null) {
                File file2 = new File(file, decode2);
                try {
                    File canonicalFile = file2.getCanonicalFile();
                    if (canonicalFile.getPath().startsWith(file.getPath())) {
                        return canonicalFile;
                    }
                    throw new SecurityException("Resolved path jumped beyond configured root");
                } catch (IOException e) {
                    throw new IllegalArgumentException("Failed to resolve canonical path for " + file2);
                }
            } else {
                throw new IllegalArgumentException("Unable to find configured root for " + uri);
            }
        }

        public Uri getUriForFile(File file) {
            try {
                String canonicalPath = file.getCanonicalPath();
                Map.Entry entry = null;
                for (Map.Entry next : this.mRoots.entrySet()) {
                    String path = ((File) next.getValue()).getPath();
                    if (canonicalPath.startsWith(path) && (entry == null || path.length() > ((File) entry.getValue()).getPath().length())) {
                        entry = next;
                    }
                }
                if (entry != null) {
                    String path2 = ((File) entry.getValue()).getPath();
                    String substring = path2.endsWith("/") ? canonicalPath.substring(path2.length()) : canonicalPath.substring(path2.length() + 1);
                    StringBuilder sb = new StringBuilder();
                    String encode = Uri.encode((String) entry.getKey());
                    Log1F380D.a((Object) encode);
                    StringBuilder append = sb.append(encode).append('/');
                    String encode2 = Uri.encode(substring, "/");
                    Log1F380D.a((Object) encode2);
                    return new Uri.Builder().scheme("content").authority(this.mAuthority).encodedPath(append.append(encode2).toString()).build();
                }
                throw new IllegalArgumentException("Failed to find configured root that contains " + canonicalPath);
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + file);
            }
        }
    }
}
