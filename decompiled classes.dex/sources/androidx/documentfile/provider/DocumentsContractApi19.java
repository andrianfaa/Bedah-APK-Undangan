package androidx.documentfile.provider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.util.Log;
import mt.Log1F380D;

/* compiled from: 0069 */
class DocumentsContractApi19 {
    private static final int FLAG_VIRTUAL_DOCUMENT = 512;
    private static final String TAG = "DocumentFile";

    private DocumentsContractApi19() {
    }

    public static boolean canRead(Context context, Uri self) {
        if (context.checkCallingOrSelfUriPermission(self, 1) != 0) {
            return false;
        }
        String rawType = getRawType(context, self);
        Log1F380D.a((Object) rawType);
        return !TextUtils.isEmpty(rawType);
    }

    private static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
            }
        }
    }

    public static boolean exists(Context context, Uri self) {
        Cursor cursor = null;
        boolean z = false;
        try {
            cursor = context.getContentResolver().query(self, new String[]{"document_id"}, (String) null, (String[]) null, (String) null);
            if (cursor.getCount() > 0) {
                z = true;
            }
            return z;
        } catch (Exception e) {
            Log.w(TAG, "Failed query: " + e);
            return false;
        } finally {
            closeQuietly(cursor);
        }
    }

    public static long getFlags(Context context, Uri self) {
        return queryForLong(context, self, "flags", 0);
    }

    public static boolean isVirtual(Context context, Uri self) {
        return DocumentsContract.isDocumentUri(context, self) && (getFlags(context, self) & 512) != 0;
    }

    public static long lastModified(Context context, Uri self) {
        return queryForLong(context, self, "last_modified", 0);
    }

    public static long length(Context context, Uri self) {
        return queryForLong(context, self, "_size", 0);
    }

    private static int queryForInt(Context context, Uri self, String column, int defaultValue) {
        return (int) queryForLong(context, self, column, (long) defaultValue);
    }

    private static long queryForLong(Context context, Uri self, String column, long defaultValue) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(self, new String[]{column}, (String) null, (String[]) null, (String) null);
            if (cursor.moveToFirst() && !cursor.isNull(0)) {
                return cursor.getLong(0);
            }
            closeQuietly(cursor);
            return defaultValue;
        } catch (Exception e) {
            Log.w(TAG, "Failed query: " + e);
            return defaultValue;
        } finally {
            closeQuietly(cursor);
        }
    }

    private static String queryForString(Context context, Uri self, String column, String defaultValue) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(self, new String[]{column}, (String) null, (String[]) null, (String) null);
            if (cursor.moveToFirst() && !cursor.isNull(0)) {
                return cursor.getString(0);
            }
            closeQuietly(cursor);
            return defaultValue;
        } catch (Exception e) {
            Log.w(TAG, "Failed query: " + e);
            return defaultValue;
        } finally {
            closeQuietly(cursor);
        }
    }

    public static boolean canWrite(Context context, Uri self) {
        if (context.checkCallingOrSelfUriPermission(self, 2) != 0) {
            return false;
        }
        String rawType = getRawType(context, self);
        Log1F380D.a((Object) rawType);
        int queryForInt = queryForInt(context, self, "flags", 0);
        if (TextUtils.isEmpty(rawType)) {
            return false;
        }
        if ((queryForInt & 4) != 0) {
            return true;
        }
        if (!"vnd.android.document/directory".equals(rawType) || (queryForInt & 8) == 0) {
            return !TextUtils.isEmpty(rawType) && (queryForInt & 2) != 0;
        }
        return true;
    }

    public static String getName(Context context, Uri self) {
        String queryForString = queryForString(context, self, "_display_name", (String) null);
        Log1F380D.a((Object) queryForString);
        return queryForString;
    }

    private static String getRawType(Context context, Uri self) {
        String queryForString = queryForString(context, self, "mime_type", (String) null);
        Log1F380D.a((Object) queryForString);
        return queryForString;
    }

    public static String getType(Context context, Uri self) {
        String rawType = getRawType(context, self);
        Log1F380D.a((Object) rawType);
        if ("vnd.android.document/directory".equals(rawType)) {
            return null;
        }
        return rawType;
    }

    public static boolean isDirectory(Context context, Uri self) {
        String rawType = getRawType(context, self);
        Log1F380D.a((Object) rawType);
        return "vnd.android.document/directory".equals(rawType);
    }

    public static boolean isFile(Context context, Uri self) {
        String rawType = getRawType(context, self);
        Log1F380D.a((Object) rawType);
        return !"vnd.android.document/directory".equals(rawType) && !TextUtils.isEmpty(rawType);
    }
}
