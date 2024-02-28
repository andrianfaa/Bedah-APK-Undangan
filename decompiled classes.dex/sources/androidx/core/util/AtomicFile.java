package androidx.core.util;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AtomicFile {
    private static final String LOG_TAG = "AtomicFile";
    private final File mBaseName;
    private final File mLegacyBackupName;
    private final File mNewName;

    public AtomicFile(File baseName) {
        this.mBaseName = baseName;
        this.mNewName = new File(baseName.getPath() + ".new");
        this.mLegacyBackupName = new File(baseName.getPath() + ".bak");
    }

    private static void rename(File source, File target) {
        if (target.isDirectory() && !target.delete()) {
            Log.e(LOG_TAG, "Failed to delete file which is a directory " + target);
        }
        if (!source.renameTo(target)) {
            Log.e(LOG_TAG, "Failed to rename " + source + " to " + target);
        }
    }

    private static boolean sync(FileOutputStream stream) {
        try {
            stream.getFD().sync();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void delete() {
        this.mBaseName.delete();
        this.mNewName.delete();
        this.mLegacyBackupName.delete();
    }

    public void failWrite(FileOutputStream str) {
        if (str != null) {
            if (!sync(str)) {
                Log.e(LOG_TAG, "Failed to sync file output stream");
            }
            try {
                str.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Failed to close file output stream", e);
            }
            if (!this.mNewName.delete()) {
                Log.e(LOG_TAG, "Failed to delete new file " + this.mNewName);
            }
        }
    }

    public void finishWrite(FileOutputStream str) {
        if (str != null) {
            if (!sync(str)) {
                Log.e(LOG_TAG, "Failed to sync file output stream");
            }
            try {
                str.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Failed to close file output stream", e);
            }
            rename(this.mNewName, this.mBaseName);
        }
    }

    public File getBaseFile() {
        return this.mBaseName;
    }

    public FileInputStream openRead() throws FileNotFoundException {
        if (this.mLegacyBackupName.exists()) {
            rename(this.mLegacyBackupName, this.mBaseName);
        }
        if (this.mNewName.exists() && this.mBaseName.exists() && !this.mNewName.delete()) {
            Log.e(LOG_TAG, "Failed to delete outdated new file " + this.mNewName);
        }
        return new FileInputStream(this.mBaseName);
    }

    public byte[] readFully() throws IOException {
        FileInputStream openRead = openRead();
        int i = 0;
        try {
            byte[] bArr = new byte[openRead.available()];
            while (true) {
                int read = openRead.read(bArr, i, bArr.length - i);
                if (read <= 0) {
                    return bArr;
                }
                i += read;
                int available = openRead.available();
                if (available > bArr.length - i) {
                    byte[] bArr2 = new byte[(i + available)];
                    System.arraycopy(bArr, 0, bArr2, 0, i);
                    bArr = bArr2;
                }
            }
        } finally {
            openRead.close();
        }
    }

    public FileOutputStream startWrite() throws IOException {
        if (this.mLegacyBackupName.exists()) {
            rename(this.mLegacyBackupName, this.mBaseName);
        }
        try {
            return new FileOutputStream(this.mNewName);
        } catch (FileNotFoundException e) {
            if (this.mNewName.getParentFile().mkdirs()) {
                try {
                    return new FileOutputStream(this.mNewName);
                } catch (FileNotFoundException e2) {
                    throw new IOException("Failed to create new file " + this.mNewName, e2);
                }
            } else {
                throw new IOException("Failed to create directory for " + this.mNewName);
            }
        }
    }
}
