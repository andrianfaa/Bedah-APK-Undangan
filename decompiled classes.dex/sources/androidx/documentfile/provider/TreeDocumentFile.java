package androidx.documentfile.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.util.Log;
import java.util.ArrayList;
import mt.Log1F380D;

/* compiled from: 006C */
class TreeDocumentFile extends DocumentFile {
    private Context mContext;
    private Uri mUri;

    TreeDocumentFile(DocumentFile parent, Context context, Uri uri) {
        super(parent);
        this.mContext = context;
        this.mUri = uri;
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

    private static Uri createFile(Context context, Uri self, String mimeType, String displayName) {
        try {
            return DocumentsContract.createDocument(context.getContentResolver(), self, mimeType, displayName);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean canRead() {
        return DocumentsContractApi19.canRead(this.mContext, this.mUri);
    }

    public boolean canWrite() {
        return DocumentsContractApi19.canWrite(this.mContext, this.mUri);
    }

    public DocumentFile createDirectory(String displayName) {
        Uri createFile = createFile(this.mContext, this.mUri, "vnd.android.document/directory", displayName);
        if (createFile != null) {
            return new TreeDocumentFile(this, this.mContext, createFile);
        }
        return null;
    }

    public DocumentFile createFile(String mimeType, String displayName) {
        Uri createFile = createFile(this.mContext, this.mUri, mimeType, displayName);
        if (createFile != null) {
            return new TreeDocumentFile(this, this.mContext, createFile);
        }
        return null;
    }

    public boolean delete() {
        try {
            return DocumentsContract.deleteDocument(this.mContext.getContentResolver(), this.mUri);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean exists() {
        return DocumentsContractApi19.exists(this.mContext, this.mUri);
    }

    public String getName() {
        String name = DocumentsContractApi19.getName(this.mContext, this.mUri);
        Log1F380D.a((Object) name);
        return name;
    }

    public Uri getUri() {
        return this.mUri;
    }

    public boolean isDirectory() {
        return DocumentsContractApi19.isDirectory(this.mContext, this.mUri);
    }

    public boolean isFile() {
        return DocumentsContractApi19.isFile(this.mContext, this.mUri);
    }

    public boolean isVirtual() {
        return DocumentsContractApi19.isVirtual(this.mContext, this.mUri);
    }

    public long lastModified() {
        return DocumentsContractApi19.lastModified(this.mContext, this.mUri);
    }

    public long length() {
        return DocumentsContractApi19.length(this.mContext, this.mUri);
    }

    public boolean renameTo(String displayName) {
        try {
            Uri renameDocument = DocumentsContract.renameDocument(this.mContext.getContentResolver(), this.mUri, displayName);
            if (renameDocument == null) {
                return false;
            }
            this.mUri = renameDocument;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getType() {
        String type = DocumentsContractApi19.getType(this.mContext, this.mUri);
        Log1F380D.a((Object) type);
        return type;
    }

    public DocumentFile[] listFiles() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        Uri uri = this.mUri;
        String documentId = DocumentsContract.getDocumentId(uri);
        Log1F380D.a((Object) documentId);
        Uri buildChildDocumentsUriUsingTree = DocumentsContract.buildChildDocumentsUriUsingTree(uri, documentId);
        ArrayList arrayList = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(buildChildDocumentsUriUsingTree, new String[]{"document_id"}, (String) null, (String[]) null, (String) null);
            while (cursor.moveToNext()) {
                arrayList.add(DocumentsContract.buildDocumentUriUsingTree(this.mUri, cursor.getString(0)));
            }
        } catch (Exception e) {
            Log.w("DocumentFile", "Failed query: " + e);
        } catch (Throwable th) {
            closeQuietly((AutoCloseable) null);
            throw th;
        }
        closeQuietly(cursor);
        Uri[] uriArr = (Uri[]) arrayList.toArray(new Uri[arrayList.size()]);
        DocumentFile[] documentFileArr = new DocumentFile[uriArr.length];
        for (int i = 0; i < uriArr.length; i++) {
            documentFileArr[i] = new TreeDocumentFile(this, this.mContext, uriArr[i]);
        }
        return documentFileArr;
    }
}
