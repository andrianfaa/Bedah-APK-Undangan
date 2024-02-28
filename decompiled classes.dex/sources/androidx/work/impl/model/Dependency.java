package androidx.work.impl.model;

public class Dependency {
    public final String prerequisiteId;
    public final String workSpecId;

    public Dependency(String workSpecId2, String prerequisiteId2) {
        this.workSpecId = workSpecId2;
        this.prerequisiteId = prerequisiteId2;
    }
}
