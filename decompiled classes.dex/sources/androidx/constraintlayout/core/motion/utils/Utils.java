package androidx.constraintlayout.core.motion.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import mt.Log1F380D;

/* compiled from: 0014 */
public class Utils {
    static DebugHandle ourHandle;

    public interface DebugHandle {
        void message(String str);
    }

    private static int clamp(int c) {
        int c2 = (c & (~(c >> 31))) - 255;
        return (c2 & (c2 >> 31)) + 255;
    }

    public static void log(String str) {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        String substring = (stackTraceElement.getMethodName() + "                  ").substring(0, 17);
        String num = Integer.toString(stackTraceElement.getLineNumber());
        Log1F380D.a((Object) num);
        String str2 = ".(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ")" + "    ".substring(num.length()) + substring;
        System.out.println(str2 + " " + str);
        DebugHandle debugHandle = ourHandle;
        if (debugHandle != null) {
            debugHandle.message(str2 + " " + str);
        }
    }

    public static void log(String tag, String value) {
        System.out.println(tag + " : " + value);
    }

    public static void logStack(String msg, int n) {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        String str = " ";
        int n2 = Math.min(n, stackTrace.length - 1);
        for (int i = 1; i <= n2; i++) {
            StackTraceElement stackTraceElement = stackTrace[i];
            str = str + " ";
            System.out.println(msg + str + (".(" + stackTrace[i].getFileName() + ":" + stackTrace[i].getLineNumber() + ") " + stackTrace[i].getMethodName()) + str);
        }
    }

    public static void loge(String tag, String value) {
        System.err.println(tag + " : " + value);
    }

    public static int rgbaTocColor(float r, float g, float b, float a) {
        int clamp = clamp((int) (r * 255.0f));
        int clamp2 = clamp((int) (g * 255.0f));
        return (clamp((int) (255.0f * a)) << 24) | (clamp << 16) | (clamp2 << 8) | clamp((int) (b * 255.0f));
    }

    public static void setDebugHandle(DebugHandle handle) {
        ourHandle = handle;
    }

    public static void socketSend(String str) {
        try {
            OutputStream outputStream = new Socket("127.0.0.1", 5327).getOutputStream();
            outputStream.write(str.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getInterpolatedColor(float[] value) {
        int clamp = clamp((int) (((float) Math.pow((double) value[0], 0.45454545454545453d)) * 255.0f));
        int clamp2 = clamp((int) (((float) Math.pow((double) value[1], 0.45454545454545453d)) * 255.0f));
        return (clamp((int) (value[3] * 255.0f)) << 24) | (clamp << 16) | (clamp2 << 8) | clamp((int) (((float) Math.pow((double) value[2], 0.45454545454545453d)) * 255.0f));
    }
}
