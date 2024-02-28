package androidx.documentfile.provider;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import java.io.File;
import mt.Log1F380D;

/* compiled from: 0068 */
public abstract class DocumentFile {
    static final String TAG = "DocumentFile";
    private final DocumentFile mParent;

    DocumentFile(DocumentFile parent) {
        this.mParent = parent;
    }

    public static DocumentFile fromFile(File file) {
        return new RawDocumentFile((DocumentFile) null, file);
    }

    public static DocumentFile fromSingleUri(Context context, Uri singleUri) {
        if (Build.VERSION.SDK_INT >= 19) {
            return new SingleDocumentFile((DocumentFile) null, context, singleUri);
        }
        return null;
    }

    public static DocumentFile fromTreeUri(Context context, Uri treeUri) {
        if (Build.VERSION.SDK_INT < 21) {
            return null;
        }
        String treeDocumentId = DocumentsContract.getTreeDocumentId(treeUri);
        Log1F380D.a((Object) treeDocumentId);
        return new TreeDocumentFile((DocumentFile) null, context, DocumentsContract.buildDocumentUriUsingTree(treeUri, treeDocumentId));
    }

    public static boolean isDocumentUri(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT >= 19) {
            return DocumentsContract.isDocumentUri(context, uri);
        }
        return false;
    }

    public abstract boolean canRead();

    public abstract boolean canWrite();

    public abstract DocumentFile createDirectory(String str);

    public abstract DocumentFile createFile(String str, String str2);

    public abstract boolean delete();

    public abstract boolean exists();

    public DocumentFile findFile(String displayName) {
        for (DocumentFile documentFile : listFiles()) {
            if (displayName.equals(documentFile.getName())) {
                return documentFile;
            }
        }
        return null;
    }

    public abstract String getName();

    public DocumentFile getParentFile() {
        return this.mParent;
    }

    public abstract String getType();

    public abstract Uri getUri();

    public abstract boolean isDirectory();

    public abstract boolean isFile();

    public abstract boolean isVirtual();

    public abstract long lastModified();

    public abstract long length();

    public abstract DocumentFile[] listFiles();

    public abstract boolean renameTo(String str);
}
