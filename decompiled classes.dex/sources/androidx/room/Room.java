package androidx.room;

import android.content.Context;
import androidx.room.RoomDatabase;

public class Room {
    private static final String CURSOR_CONV_SUFFIX = "_CursorConverter";
    static final String LOG_TAG = "ROOM";
    public static final String MASTER_TABLE_NAME = "room_master_table";

    public static <T extends RoomDatabase> RoomDatabase.Builder<T> databaseBuilder(Context context, Class<T> cls, String name) {
        if (name != null && name.trim().length() != 0) {
            return new RoomDatabase.Builder<>(context, cls, name);
        }
        throw new IllegalArgumentException("Cannot build a database with null or empty name. If you are trying to create an in memory database, use Room.inMemoryDatabaseBuilder");
    }

    static <T, C> T getGeneratedImplementation(Class<C> cls, String suffix) {
        String name = cls.getPackage().getName();
        String canonicalName = cls.getCanonicalName();
        String str = (name.isEmpty() ? canonicalName : canonicalName.substring(name.length() + 1)).replace('.', '_') + suffix;
        try {
            return Class.forName(name.isEmpty() ? str : name + "." + str).newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("cannot find implementation for " + cls.getCanonicalName() + ". " + str + " does not exist");
        } catch (IllegalAccessException e2) {
            throw new RuntimeException("Cannot access the constructor" + cls.getCanonicalName());
        } catch (InstantiationException e3) {
            throw new RuntimeException("Failed to create an instance of " + cls.getCanonicalName());
        }
    }

    public static <T extends RoomDatabase> RoomDatabase.Builder<T> inMemoryDatabaseBuilder(Context context, Class<T> cls) {
        return new RoomDatabase.Builder<>(context, cls, (String) null);
    }
}
