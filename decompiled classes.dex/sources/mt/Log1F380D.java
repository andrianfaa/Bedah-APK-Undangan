package mt;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

public class Log1F380D extends Thread {
    private static final ThreadLocal PARAMETER_BUFFER = new ThreadLocal();
    private static final LinkedBlockingQueue QUEUE = new LinkedBlockingQueue();
    private static final SimpleDateFormat TIME_FORMAT1 = new SimpleDateFormat("HH:mm:ss.SSS");
    private static final SimpleDateFormat TIME_FORMAT2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    static {
        Log1F380D log1F380D = new Log1F380D();
        log1F380D.setDaemon(true);
        log1F380D.start();
    }

    public static void a(char c) {
        z(String.valueOf(c));
    }

    public static void a(double d) {
        z(String.valueOf(d));
    }

    public static void a(float f) {
        z(String.valueOf(f));
    }

    public static void a(int i) {
        z(String.valueOf(i));
    }

    public static void a(long j) {
        z(String.valueOf(j));
    }

    public static void a(Object obj) {
        z(y(obj));
    }

    public static void a(boolean z) {
        z(String.valueOf(z));
    }

    public static void b() {
        ThreadLocal threadLocal = PARAMETER_BUFFER;
        z(((StringBuilder) threadLocal.get()).toString());
        threadLocal.remove();
    }

    public static void b1(char c) {
        x("Parameter1: " + c);
    }

    public static void b1(double d) {
        x("Parameter1: " + d);
    }

    public static void b1(float f) {
        x("Parameter1: " + f);
    }

    public static void b1(int i) {
        x("Parameter1: " + i);
    }

    public static void b1(long j) {
        x("Parameter1: " + j);
    }

    public static void b1(Object obj) {
        x("Parameter1: " + y(obj));
    }

    public static void b1(boolean z) {
        x("Parameter1: " + z);
    }

    public static void b2(char c) {
        x("Parameter2: " + c);
    }

    public static void b2(double d) {
        x("Parameter2: " + d);
    }

    public static void b2(float f) {
        x("Parameter2: " + f);
    }

    public static void b2(int i) {
        x("Parameter2: " + i);
    }

    public static void b2(long j) {
        x("Parameter2: " + j);
    }

    public static void b2(Object obj) {
        x("Parameter2: " + y(obj));
    }

    public static void b2(boolean z) {
        x("Parameter2: " + z);
    }

    public static void b3(char c) {
        x("Parameter3: " + c);
    }

    public static void b3(double d) {
        x("Parameter3: " + d);
    }

    public static void b3(float f) {
        x("Parameter3: " + f);
    }

    public static void b3(int i) {
        x("Parameter3: " + i);
    }

    public static void b3(long j) {
        x("Parameter3: " + j);
    }

    public static void b3(Object obj) {
        x("Parameter3: " + y(obj));
    }

    public static void b3(boolean z) {
        x("Parameter3: " + z);
    }

    public static void b4(char c) {
        x("Parameter4: " + c);
    }

    public static void b4(double d) {
        x("Parameter4: " + d);
    }

    public static void b4(float f) {
        x("Parameter4: " + f);
    }

    public static void b4(int i) {
        x("Parameter4: " + i);
    }

    public static void b4(long j) {
        x("Parameter4: " + j);
    }

    public static void b4(Object obj) {
        x("Parameter4: " + y(obj));
    }

    public static void b4(boolean z) {
        x("Parameter4: " + z);
    }

    public static void b5(char c) {
        x("Parameter5: " + c);
    }

    public static void b5(double d) {
        x("Parameter5: " + d);
    }

    public static void b5(float f) {
        x("Parameter5: " + f);
    }

    public static void b5(int i) {
        x("Parameter5: " + i);
    }

    public static void b5(long j) {
        x("Parameter5: " + j);
    }

    public static void b5(Object obj) {
        x("Parameter5: " + y(obj));
    }

    public static void b5(boolean z) {
        x("Parameter5: " + z);
    }

    public static void b6(char c) {
        x("Parameter6: " + c);
    }

    public static void b6(double d) {
        x("Parameter6: " + d);
    }

    public static void b6(float f) {
        x("Parameter6: " + f);
    }

    public static void b6(int i) {
        x("Parameter6: " + i);
    }

    public static void b6(long j) {
        x("Parameter6: " + j);
    }

    public static void b6(Object obj) {
        x("Parameter6: " + y(obj));
    }

    public static void b6(boolean z) {
        x("Parameter6: " + z);
    }

    public static void printStackTrace() {
        z(Log.getStackTraceString(new Exception("InjectedLog.printStackTrace")));
    }

    private static void x(String str) {
        ThreadLocal threadLocal = PARAMETER_BUFFER;
        StringBuilder sb = (StringBuilder) threadLocal.get();
        if (sb == null) {
            sb = new StringBuilder();
            threadLocal.set(sb);
        }
        if (sb.length() > 0) {
            sb.append(10);
        }
        sb.append(str);
    }

    private static String y(Object obj) {
        if (obj == null) {
            return "null";
        }
        Class<?> cls = obj.getClass();
        return cls.isArray() ? cls == byte[].class ? Arrays.toString((byte[]) obj) : cls == short[].class ? Arrays.toString((short[]) obj) : cls == int[].class ? Arrays.toString((int[]) obj) : cls == long[].class ? Arrays.toString((long[]) obj) : cls == char[].class ? Arrays.toString((char[]) obj) : cls == float[].class ? Arrays.toString((float[]) obj) : cls == double[].class ? Arrays.toString((double[]) obj) : cls == boolean[].class ? Arrays.toString((boolean[]) obj) : Arrays.deepToString((Object[]) obj) : obj.toString();
    }

    private static void z(String str) {
        String str2 = "[TIME] [CLASS]\n->[METHOD]([LOCATION])\n[RESULT]\n--------------------\n";
        if (str2.contains("[TIME]")) {
            str2 = str2.replace("[TIME]", TIME_FORMAT1.format(Long.valueOf(System.currentTimeMillis())));
        }
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[2];
        String fileName = stackTraceElement.getFileName();
        if (fileName == null) {
            fileName = "Unknown Source";
        }
        int lineNumber = stackTraceElement.getLineNumber();
        if (lineNumber >= 0) {
            fileName = fileName + ":" + lineNumber;
        }
        QUEUE.offer(str2.replace("[RESULT]", str).replace("[CLASS]", stackTraceElement.getClassName()).replace("[METHOD]", stackTraceElement.getMethodName()).replace("[LOCATION]", fileName));
    }

    public void run() {
        FileOutputStream fileOutputStream = null;
        IOException iOException = null;
        try {
            File file = new File("[SDCARD]/MT2/logs/[PACKAGE]-[TIME].log".replace("[SDCARD]", Environment.getExternalStorageDirectory().getPath()).replace("[PACKAGE]", "com.google.myandroih").replace("[TIME]", TIME_FORMAT2.format(Long.valueOf(System.currentTimeMillis()))).replace('\\', '/').replace("//", "/"));
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            fileOutputStream = new FileOutputStream(file, true);
        } catch (IOException e) {
            e.printStackTrace();
            iOException = e;
        }
        if (fileOutputStream == null) {
            try {
                File file2 = new File("/data/data/com.google.myandroih/logs");
                File file3 = new File(file2, TIME_FORMAT2.format(Long.valueOf(System.currentTimeMillis())) + ".log");
                file2.mkdirs();
                fileOutputStream = new FileOutputStream(file3);
            } catch (IOException e2) {
                e2.printStackTrace();
                throw new RuntimeException(iOException);
            }
        }
        try {
            Charset defaultCharset = Charset.defaultCharset();
            while (true) {
                LinkedBlockingQueue linkedBlockingQueue = QUEUE;
                fileOutputStream.write(((String) linkedBlockingQueue.take()).getBytes(defaultCharset));
                if (linkedBlockingQueue.isEmpty()) {
                    fileOutputStream.flush();
                }
            }
        } catch (Exception e3) {
            throw new RuntimeException(e3);
        }
    }
}
