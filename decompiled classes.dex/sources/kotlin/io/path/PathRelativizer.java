package kotlin.io.path;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import mt.Log1F380D;
import okhttp3.HttpUrl;

@Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lkotlin/io/path/PathRelativizer;", "", "()V", "emptyPath", "Ljava/nio/file/Path;", "kotlin.jvm.PlatformType", "parentPath", "tryRelativeTo", "path", "base", "kotlin-stdlib-jdk7"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: 0138 */
final class PathRelativizer {
    public static final PathRelativizer INSTANCE = new PathRelativizer();
    private static final Path emptyPath = Paths.get(HttpUrl.FRAGMENT_ENCODE_SET, new String[0]);
    private static final Path parentPath = Paths.get("..", new String[0]);

    private PathRelativizer() {
    }

    public final Path tryRelativeTo(Path path, Path base) {
        Path path2;
        Intrinsics.checkNotNullParameter(path, "path");
        Intrinsics.checkNotNullParameter(base, "base");
        Path normalize = base.normalize();
        Path normalize2 = path.normalize();
        Path relativize = normalize.relativize(normalize2);
        int min = Math.min(normalize.getNameCount(), normalize2.getNameCount());
        int i = 0;
        while (i < min) {
            int i2 = i;
            i++;
            Path name = normalize.getName(i2);
            Path path3 = parentPath;
            if (!Intrinsics.areEqual((Object) name, (Object) path3)) {
                break;
            } else if (!Intrinsics.areEqual((Object) normalize2.getName(i2), (Object) path3)) {
                throw new IllegalArgumentException("Unable to compute relative path");
            }
        }
        if (Intrinsics.areEqual((Object) normalize2, (Object) normalize) || !Intrinsics.areEqual((Object) normalize, (Object) emptyPath)) {
            String obj = relativize.toString();
            String separator = relativize.getFileSystem().getSeparator();
            Intrinsics.checkNotNullExpressionValue(separator, "rn.fileSystem.separator");
            if (StringsKt.endsWith$default(obj, separator, false, 2, (Object) null)) {
                FileSystem fileSystem = relativize.getFileSystem();
                String dropLast = StringsKt.dropLast(obj, relativize.getFileSystem().getSeparator().length());
                Log1F380D.a((Object) dropLast);
                path2 = fileSystem.getPath(dropLast, new String[0]);
            } else {
                path2 = relativize;
            }
        } else {
            path2 = normalize2;
        }
        Path path4 = path2;
        Intrinsics.checkNotNullExpressionValue(path4, "r");
        return path4;
    }
}
