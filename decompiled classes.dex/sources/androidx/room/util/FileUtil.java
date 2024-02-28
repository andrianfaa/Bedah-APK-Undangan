package androidx.room.util;

import android.os.Build;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class FileUtil {
    private FileUtil() {
    }

    public static void copy(ReadableByteChannel input, FileChannel output) throws IOException {
        try {
            if (Build.VERSION.SDK_INT <= 23) {
                InputStream newInputStream = Channels.newInputStream(input);
                OutputStream newOutputStream = Channels.newOutputStream(output);
                byte[] bArr = new byte[4096];
                while (true) {
                    int read = newInputStream.read(bArr);
                    int i = read;
                    if (read <= 0) {
                        break;
                    }
                    newOutputStream.write(bArr, 0, i);
                }
            } else {
                output.transferFrom(input, 0, Long.MAX_VALUE);
            }
            output.force(false);
        } finally {
            input.close();
            output.close();
        }
    }
}
