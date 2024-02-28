package androidx.core.content;

import android.content.LocusId;
import android.os.Build;
import androidx.core.util.Preconditions;
import mt.Log1F380D;

/* compiled from: 0038 */
public final class LocusIdCompat {
    private final String mId;
    private final LocusId mWrapped;

    private static class Api29Impl {
        private Api29Impl() {
        }

        static LocusId create(String id) {
            return new LocusId(id);
        }

        static String getId(LocusId obj) {
            return obj.getId();
        }
    }

    public LocusIdCompat(String id) {
        this.mId = (String) Preconditions.checkStringNotEmpty(id, "id cannot be empty");
        if (Build.VERSION.SDK_INT >= 29) {
            this.mWrapped = Api29Impl.create(id);
        } else {
            this.mWrapped = null;
        }
    }

    private String getSanitizedId() {
        return this.mId.length() + "_chars";
    }

    public static LocusIdCompat toLocusIdCompat(LocusId locusId) {
        Preconditions.checkNotNull(locusId, "locusId cannot be null");
        String id = Api29Impl.getId(locusId);
        Log1F380D.a((Object) id);
        return new LocusIdCompat((String) Preconditions.checkStringNotEmpty(id, "id cannot be empty"));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LocusIdCompat locusIdCompat = (LocusIdCompat) obj;
        String str = this.mId;
        return str == null ? locusIdCompat.mId == null : str.equals(locusIdCompat.mId);
    }

    public String getId() {
        return this.mId;
    }

    public int hashCode() {
        int i = 1 * 31;
        String str = this.mId;
        return i + (str == null ? 0 : str.hashCode());
    }

    public LocusId toLocusId() {
        return this.mWrapped;
    }

    public String toString() {
        return "LocusIdCompat[" + getSanitizedId() + "]";
    }
}
