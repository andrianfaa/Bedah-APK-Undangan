package androidx.work.impl.model;

import java.util.List;

public interface SystemIdInfoDao {
    SystemIdInfo getSystemIdInfo(String str);

    List<String> getWorkSpecIds();

    void insertSystemIdInfo(SystemIdInfo systemIdInfo);

    void removeSystemIdInfo(String str);
}
