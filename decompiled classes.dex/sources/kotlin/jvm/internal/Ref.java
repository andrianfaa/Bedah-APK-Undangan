package kotlin.jvm.internal;

import java.io.Serializable;
import mt.Log1F380D;

public class Ref {

    /* compiled from: 013F */
    public static final class BooleanRef implements Serializable {
        public boolean element;

        public String toString() {
            String valueOf = String.valueOf(this.element);
            Log1F380D.a((Object) valueOf);
            return valueOf;
        }
    }

    /* compiled from: 0140 */
    public static final class ByteRef implements Serializable {
        public byte element;

        public String toString() {
            String valueOf = String.valueOf(this.element);
            Log1F380D.a((Object) valueOf);
            return valueOf;
        }
    }

    /* compiled from: 0141 */
    public static final class CharRef implements Serializable {
        public char element;

        public String toString() {
            String valueOf = String.valueOf(this.element);
            Log1F380D.a((Object) valueOf);
            return valueOf;
        }
    }

    /* compiled from: 0142 */
    public static final class DoubleRef implements Serializable {
        public double element;

        public String toString() {
            String valueOf = String.valueOf(this.element);
            Log1F380D.a((Object) valueOf);
            return valueOf;
        }
    }

    /* compiled from: 0143 */
    public static final class FloatRef implements Serializable {
        public float element;

        public String toString() {
            String valueOf = String.valueOf(this.element);
            Log1F380D.a((Object) valueOf);
            return valueOf;
        }
    }

    /* compiled from: 0144 */
    public static final class IntRef implements Serializable {
        public int element;

        public String toString() {
            String valueOf = String.valueOf(this.element);
            Log1F380D.a((Object) valueOf);
            return valueOf;
        }
    }

    /* compiled from: 0145 */
    public static final class LongRef implements Serializable {
        public long element;

        public String toString() {
            String valueOf = String.valueOf(this.element);
            Log1F380D.a((Object) valueOf);
            return valueOf;
        }
    }

    /* compiled from: 0146 */
    public static final class ObjectRef<T> implements Serializable {
        public T element;

        public String toString() {
            String valueOf = String.valueOf(this.element);
            Log1F380D.a((Object) valueOf);
            return valueOf;
        }
    }

    /* compiled from: 0147 */
    public static final class ShortRef implements Serializable {
        public short element;

        public String toString() {
            String valueOf = String.valueOf(this.element);
            Log1F380D.a((Object) valueOf);
            return valueOf;
        }
    }

    private Ref() {
    }
}
