package com.google.android.material.color;

import android.content.Context;
import android.content.res.loader.AssetsProvider;
import android.content.res.loader.ResourcesLoader;
import android.content.res.loader.ResourcesProvider;
import android.os.ParcelFileDescriptor;
import android.system.Os;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.util.Map;

final class ColorResourcesLoaderCreator {
    private static final String TAG = ColorResourcesLoaderCreator.class.getSimpleName();

    private ColorResourcesLoaderCreator() {
    }

    static ResourcesLoader create(Context context, Map<Integer, Integer> map) {
        ParcelFileDescriptor dup;
        try {
            byte[] create = ColorResourcesTableCreator.create(context, map);
            Log.i(TAG, "Table created, length: " + create.length);
            if (create.length == 0) {
                return null;
            }
            FileDescriptor fileDescriptor = null;
            try {
                fileDescriptor = Os.memfd_create("temp.arsc", 0);
                FileOutputStream fileOutputStream = new FileOutputStream(fileDescriptor);
                try {
                    fileOutputStream.write(create);
                    dup = ParcelFileDescriptor.dup(fileDescriptor);
                    ResourcesLoader resourcesLoader = new ResourcesLoader();
                    resourcesLoader.addProvider(ResourcesProvider.loadFromTable(dup, (AssetsProvider) null));
                    if (dup != null) {
                        dup.close();
                    }
                    fileOutputStream.close();
                    if (fileDescriptor != null) {
                        Os.close(fileDescriptor);
                    }
                    return resourcesLoader;
                } catch (Throwable th) {
                    fileOutputStream.close();
                    throw th;
                }
            } catch (Throwable th2) {
                if (fileDescriptor != null) {
                    Os.close(fileDescriptor);
                }
                throw th2;
            }
            throw th;
        } catch (Exception e) {
            Log.e(TAG, "Failed to create the ColorResourcesTableCreator.", e);
            return null;
        }
    }
}
