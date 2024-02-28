package androidx.work.impl.model;

public class Preference {
    public String mKey;
    public Long mValue;

    public Preference(String key, long value) {
        this.mKey = key;
        this.mValue = Long.valueOf(value);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public Preference(String key, boolean value) {
        this(key, value ? 1 : 0);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Preference)) {
            return false;
        }
        Preference preference = (Preference) o;
        if (!this.mKey.equals(preference.mKey)) {
            return false;
        }
        Long l = this.mValue;
        return l != null ? l.equals(preference.mValue) : preference.mValue == null;
    }

    public int hashCode() {
        int hashCode = this.mKey.hashCode() * 31;
        Long l = this.mValue;
        return hashCode + (l != null ? l.hashCode() : 0);
    }
}
