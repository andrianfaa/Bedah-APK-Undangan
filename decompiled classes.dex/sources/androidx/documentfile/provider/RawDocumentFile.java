package androidx.documentfile.provider;

import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import mt.Log1F380D;

/* compiled from: 006A */
class RawDocumentFile extends DocumentFile {
    private File mFile;

    RawDocumentFile(DocumentFile parent, File file) {
        super(parent);
        this.mFile = file;
    }

    private static boolean deleteContents(File dir) {
        File[] listFiles = dir.listFiles();
        boolean z = true;
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    z &= deleteContents(file);
                }
                if (!file.delete()) {
                    Log.w("DocumentFile", "Failed to delete " + file);
                    z = false;
                }
            }
        }
        return z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0008, code lost:
        r2 = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(r3.substring(r0 + 1).toLowerCase());
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getTypeForName(java.lang.String r3) {
        /*
            r0 = 46
            int r0 = r3.lastIndexOf(r0)
            if (r0 < 0) goto L_0x001d
            int r1 = r0 + 1
            java.lang.String r1 = r3.substring(r1)
            java.lang.String r1 = r1.toLowerCase()
            android.webkit.MimeTypeMap r2 = android.webkit.MimeTypeMap.getSingleton()
            java.lang.String r2 = r2.getMimeTypeFromExtension(r1)
            if (r2 == 0) goto L_0x001d
            return r2
        L_0x001d:
            java.lang.String r1 = "application/octet-stream"
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.documentfile.provider.RawDocumentFile.getTypeForName(java.lang.String):java.lang.String");
    }

    public boolean canRead() {
        return this.mFile.canRead();
    }

    public boolean canWrite() {
        return this.mFile.canWrite();
    }

    public DocumentFile createDirectory(String displayName) {
        File file = new File(this.mFile, displayName);
        if (file.isDirectory() || file.mkdir()) {
            return new RawDocumentFile(this, file);
        }
        return null;
    }

    public DocumentFile createFile(String mimeType, String displayName) {
        String extensionFromMimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
        if (extensionFromMimeType != null) {
            displayName = displayName + "." + extensionFromMimeType;
        }
        File file = new File(this.mFile, displayName);
        try {
            file.createNewFile();
            return new RawDocumentFile(this, file);
        } catch (IOException e) {
            Log.w("DocumentFile", "Failed to createFile: " + e);
            return null;
        }
    }

    public boolean delete() {
        deleteContents(this.mFile);
        return this.mFile.delete();
    }

    public boolean exists() {
        return this.mFile.exists();
    }

    public String getName() {
        return this.mFile.getName();
    }

    public String getType() {
        if (this.mFile.isDirectory()) {
            return null;
        }
        String typeForName = getTypeForName(this.mFile.getName());
        Log1F380D.a((Object) typeForName);
        return typeForName;
    }

    public Uri getUri() {
        return Uri.fromFile(this.mFile);
    }

    public boolean isDirectory() {
        return this.mFile.isDirectory();
    }

    public boolean isFile() {
        return this.mFile.isFile();
    }

    public boolean isVirtual() {
        return false;
    }

    public long lastModified() {
        return this.mFile.lastModified();
    }

    public long length() {
        return this.mFile.length();
    }

    public DocumentFile[] listFiles() {
        ArrayList arrayList = new ArrayList();
        File[] listFiles = this.mFile.listFiles();
        if (listFiles != null) {
            for (File rawDocumentFile : listFiles) {
                arrayList.add(new RawDocumentFile(this, rawDocumentFile));
            }
        }
        return (DocumentFile[]) arrayList.toArray(new DocumentFile[arrayList.size()]);
    }

    public boolean renameTo(String displayName) {
        File file = new File(this.mFile.getParentFile(), displayName);
        if (!this.mFile.renameTo(file)) {
            return false;
        }
        this.mFile = file;
        return true;
    }
}
