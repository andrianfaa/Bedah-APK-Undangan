package androidx.core.graphics;

import android.graphics.Path;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\f\u001a\u001c\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004*\u00020\u00012\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a\u0015\u0010\b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\n\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\f\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\n\u001a\u0015\u0010\u000b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\f¨\u0006\f"}, d2 = {"and", "Landroid/graphics/Path;", "p", "flatten", "", "Landroidx/core/graphics/PathSegment;", "error", "", "minus", "or", "plus", "xor", "core-ktx_release"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: Path.kt */
public final class PathKt {
    public static final Path and(Path $this$and, Path p) {
        Intrinsics.checkNotNullParameter($this$and, "<this>");
        Intrinsics.checkNotNullParameter(p, "p");
        Path path = new Path();
        path.op($this$and, p, Path.Op.INTERSECT);
        return path;
    }

    public static final Iterable<PathSegment> flatten(Path $this$flatten, float error) {
        Intrinsics.checkNotNullParameter($this$flatten, "<this>");
        Collection<PathSegment> flatten = PathUtils.flatten($this$flatten, error);
        Intrinsics.checkNotNullExpressionValue(flatten, "flatten(this, error)");
        return flatten;
    }

    public static /* synthetic */ Iterable flatten$default(Path path, float f, int i, Object obj) {
        if ((i & 1) != 0) {
            f = 0.5f;
        }
        return flatten(path, f);
    }

    public static final Path minus(Path $this$minus, Path p) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(p, "p");
        Path path = new Path($this$minus);
        path.op(p, Path.Op.DIFFERENCE);
        return path;
    }

    public static final Path or(Path $this$or, Path p) {
        Intrinsics.checkNotNullParameter($this$or, "<this>");
        Intrinsics.checkNotNullParameter(p, "p");
        Path path = new Path($this$or);
        path.op(p, Path.Op.UNION);
        return path;
    }

    public static final Path plus(Path $this$plus, Path p) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(p, "p");
        Path path = new Path($this$plus);
        path.op(p, Path.Op.UNION);
        return path;
    }

    public static final Path xor(Path $this$xor, Path p) {
        Intrinsics.checkNotNullParameter($this$xor, "<this>");
        Intrinsics.checkNotNullParameter(p, "p");
        Path path = new Path($this$xor);
        path.op(p, Path.Op.XOR);
        return path;
    }
}
