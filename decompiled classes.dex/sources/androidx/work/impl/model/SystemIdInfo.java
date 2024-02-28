package androidx.work.impl.model;

public class SystemIdInfo {
    public final int systemId;
    public final String workSpecId;

    public SystemIdInfo(String workSpecId2, int systemId2) {
        this.workSpecId = workSpecId2;
        this.systemId = systemId2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemIdInfo)) {
            return false;
        }
        SystemIdInfo systemIdInfo = (SystemIdInfo) o;
        if (this.systemId != systemIdInfo.systemId) {
            return false;
        }
        return this.workSpecId.equals(systemIdInfo.workSpecId);
    }

    public int hashCode() {
        return (this.workSpecId.hashCode() * 31) + this.systemId;
    }
}
