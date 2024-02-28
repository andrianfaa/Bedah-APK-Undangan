package androidx.constraintlayout.core.motion.utils;

import androidx.constraintlayout.core.motion.CustomAttribute;
import androidx.constraintlayout.core.motion.CustomVariable;
import java.io.PrintStream;
import java.util.Arrays;
import mt.Log1F380D;
import okhttp3.HttpUrl;

public class KeyFrameArray {

    /* compiled from: 0010 */
    public static class CustomArray {
        private static final int EMPTY = 999;
        int count;
        int[] keys = new int[TypedValues.TYPE_TARGET];
        CustomAttribute[] values = new CustomAttribute[TypedValues.TYPE_TARGET];

        public CustomArray() {
            clear();
        }

        public void append(int position, CustomAttribute value) {
            if (this.values[position] != null) {
                remove(position);
            }
            this.values[position] = value;
            int[] iArr = this.keys;
            int i = this.count;
            this.count = i + 1;
            iArr[i] = position;
            Arrays.sort(iArr);
        }

        public void clear() {
            Arrays.fill(this.keys, 999);
            Arrays.fill(this.values, (Object) null);
            this.count = 0;
        }

        public void dump() {
            PrintStream printStream = System.out;
            StringBuilder append = new StringBuilder().append("V: ");
            String arrays = Arrays.toString(Arrays.copyOf(this.keys, this.count));
            Log1F380D.a((Object) arrays);
            printStream.println(append.append(arrays).toString());
            System.out.print("K: [");
            int i = 0;
            while (i < this.count) {
                System.out.print((i == 0 ? HttpUrl.FRAGMENT_ENCODE_SET : ", ") + valueAt(i));
                i++;
            }
            System.out.println("]");
        }

        public int keyAt(int i) {
            return this.keys[i];
        }

        public void remove(int position) {
            this.values[position] = null;
            int i = 0;
            int i2 = 0;
            while (true) {
                int i3 = this.count;
                if (i2 < i3) {
                    int[] iArr = this.keys;
                    if (position == iArr[i2]) {
                        iArr[i2] = 999;
                        i++;
                    }
                    if (i2 != i) {
                        iArr[i2] = iArr[i];
                    }
                    i++;
                    i2++;
                } else {
                    this.count = i3 - 1;
                    return;
                }
            }
        }

        public int size() {
            return this.count;
        }

        public CustomAttribute valueAt(int i) {
            return this.values[this.keys[i]];
        }
    }

    /* compiled from: 0011 */
    public static class CustomVar {
        private static final int EMPTY = 999;
        int count;
        int[] keys = new int[TypedValues.TYPE_TARGET];
        CustomVariable[] values = new CustomVariable[TypedValues.TYPE_TARGET];

        public CustomVar() {
            clear();
        }

        public void append(int position, CustomVariable value) {
            if (this.values[position] != null) {
                remove(position);
            }
            this.values[position] = value;
            int[] iArr = this.keys;
            int i = this.count;
            this.count = i + 1;
            iArr[i] = position;
            Arrays.sort(iArr);
        }

        public void clear() {
            Arrays.fill(this.keys, 999);
            Arrays.fill(this.values, (Object) null);
            this.count = 0;
        }

        public void dump() {
            PrintStream printStream = System.out;
            StringBuilder append = new StringBuilder().append("V: ");
            String arrays = Arrays.toString(Arrays.copyOf(this.keys, this.count));
            Log1F380D.a((Object) arrays);
            printStream.println(append.append(arrays).toString());
            System.out.print("K: [");
            int i = 0;
            while (i < this.count) {
                System.out.print((i == 0 ? HttpUrl.FRAGMENT_ENCODE_SET : ", ") + valueAt(i));
                i++;
            }
            System.out.println("]");
        }

        public int keyAt(int i) {
            return this.keys[i];
        }

        public void remove(int position) {
            this.values[position] = null;
            int i = 0;
            int i2 = 0;
            while (true) {
                int i3 = this.count;
                if (i2 < i3) {
                    int[] iArr = this.keys;
                    if (position == iArr[i2]) {
                        iArr[i2] = 999;
                        i++;
                    }
                    if (i2 != i) {
                        iArr[i2] = iArr[i];
                    }
                    i++;
                    i2++;
                } else {
                    this.count = i3 - 1;
                    return;
                }
            }
        }

        public int size() {
            return this.count;
        }

        public CustomVariable valueAt(int i) {
            return this.values[this.keys[i]];
        }
    }

    /* compiled from: 0012 */
    static class FloatArray {
        private static final int EMPTY = 999;
        int count;
        int[] keys = new int[TypedValues.TYPE_TARGET];
        float[][] values = new float[TypedValues.TYPE_TARGET][];

        public FloatArray() {
            clear();
        }

        public void append(int position, float[] value) {
            if (this.values[position] != null) {
                remove(position);
            }
            this.values[position] = value;
            int[] iArr = this.keys;
            int i = this.count;
            this.count = i + 1;
            iArr[i] = position;
            Arrays.sort(iArr);
        }

        public void clear() {
            Arrays.fill(this.keys, 999);
            Arrays.fill(this.values, (Object) null);
            this.count = 0;
        }

        public void dump() {
            PrintStream printStream = System.out;
            StringBuilder append = new StringBuilder().append("V: ");
            String arrays = Arrays.toString(Arrays.copyOf(this.keys, this.count));
            Log1F380D.a((Object) arrays);
            printStream.println(append.append(arrays).toString());
            System.out.print("K: [");
            int i = 0;
            while (i < this.count) {
                PrintStream printStream2 = System.out;
                StringBuilder append2 = new StringBuilder().append(i == 0 ? HttpUrl.FRAGMENT_ENCODE_SET : ", ");
                String arrays2 = Arrays.toString(valueAt(i));
                Log1F380D.a((Object) arrays2);
                printStream2.print(append2.append(arrays2).toString());
                i++;
            }
            System.out.println("]");
        }

        public int keyAt(int i) {
            return this.keys[i];
        }

        public void remove(int position) {
            this.values[position] = null;
            int i = 0;
            int i2 = 0;
            while (true) {
                int i3 = this.count;
                if (i2 < i3) {
                    int[] iArr = this.keys;
                    if (position == iArr[i2]) {
                        iArr[i2] = 999;
                        i++;
                    }
                    if (i2 != i) {
                        iArr[i2] = iArr[i];
                    }
                    i++;
                    i2++;
                } else {
                    this.count = i3 - 1;
                    return;
                }
            }
        }

        public int size() {
            return this.count;
        }

        public float[] valueAt(int i) {
            return this.values[this.keys[i]];
        }
    }
}
