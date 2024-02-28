package androidx.core.graphics;

import android.graphics.Point;
import android.graphics.PointF;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\u0018\u0002\n\u0002\b\t\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n\u001a\r\u0010\u0000\u001a\u00020\u0003*\u00020\u0004H\n\u001a\r\u0010\u0005\u001a\u00020\u0001*\u00020\u0002H\n\u001a\r\u0010\u0005\u001a\u00020\u0003*\u00020\u0004H\n\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\n\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\b\u001a\u00020\u0001H\n\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\n\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\b\u001a\u00020\u0003H\n\u001a\u0015\u0010\t\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\n\u001a\u0015\u0010\t\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\b\u001a\u00020\u0001H\n\u001a\u0015\u0010\t\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\n\u001a\u0015\u0010\t\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\b\u001a\u00020\u0003H\n\u001a\r\u0010\n\u001a\u00020\u0002*\u00020\u0004H\b\u001a\r\u0010\u000b\u001a\u00020\u0004*\u00020\u0002H\b\u001a\r\u0010\f\u001a\u00020\u0002*\u00020\u0002H\n\u001a\r\u0010\f\u001a\u00020\u0004*\u00020\u0004H\n¨\u0006\r"}, d2 = {"component1", "", "Landroid/graphics/Point;", "", "Landroid/graphics/PointF;", "component2", "minus", "p", "xy", "plus", "toPoint", "toPointF", "unaryMinus", "core-ktx_release"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: Point.kt */
public final class PointKt {
    public static final float component1(PointF $this$component1) {
        Intrinsics.checkNotNullParameter($this$component1, "<this>");
        return $this$component1.x;
    }

    public static final int component1(Point $this$component1) {
        Intrinsics.checkNotNullParameter($this$component1, "<this>");
        return $this$component1.x;
    }

    public static final float component2(PointF $this$component2) {
        Intrinsics.checkNotNullParameter($this$component2, "<this>");
        return $this$component2.y;
    }

    public static final int component2(Point $this$component2) {
        Intrinsics.checkNotNullParameter($this$component2, "<this>");
        return $this$component2.y;
    }

    public static final Point minus(Point $this$minus, int xy) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Point point = new Point($this$minus.x, $this$minus.y);
        point.offset(-xy, -xy);
        return point;
    }

    public static final Point minus(Point $this$minus, Point p) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(p, "p");
        Point point = new Point($this$minus.x, $this$minus.y);
        point.offset(-p.x, -p.y);
        return point;
    }

    public static final PointF minus(PointF $this$minus, float xy) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        PointF pointF = new PointF($this$minus.x, $this$minus.y);
        pointF.offset(-xy, -xy);
        return pointF;
    }

    public static final PointF minus(PointF $this$minus, PointF p) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(p, "p");
        PointF pointF = new PointF($this$minus.x, $this$minus.y);
        pointF.offset(-p.x, -p.y);
        return pointF;
    }

    public static final Point plus(Point $this$plus, int xy) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Point point = new Point($this$plus.x, $this$plus.y);
        point.offset(xy, xy);
        return point;
    }

    public static final Point plus(Point $this$plus, Point p) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(p, "p");
        Point point = new Point($this$plus.x, $this$plus.y);
        point.offset(p.x, p.y);
        return point;
    }

    public static final PointF plus(PointF $this$plus, float xy) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        PointF pointF = new PointF($this$plus.x, $this$plus.y);
        pointF.offset(xy, xy);
        return pointF;
    }

    public static final PointF plus(PointF $this$plus, PointF p) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(p, "p");
        PointF pointF = new PointF($this$plus.x, $this$plus.y);
        pointF.offset(p.x, p.y);
        return pointF;
    }

    public static final Point toPoint(PointF $this$toPoint) {
        Intrinsics.checkNotNullParameter($this$toPoint, "<this>");
        return new Point((int) $this$toPoint.x, (int) $this$toPoint.y);
    }

    public static final PointF toPointF(Point $this$toPointF) {
        Intrinsics.checkNotNullParameter($this$toPointF, "<this>");
        return new PointF($this$toPointF);
    }

    public static final Point unaryMinus(Point $this$unaryMinus) {
        Intrinsics.checkNotNullParameter($this$unaryMinus, "<this>");
        return new Point(-$this$unaryMinus.x, -$this$unaryMinus.y);
    }

    public static final PointF unaryMinus(PointF $this$unaryMinus) {
        Intrinsics.checkNotNullParameter($this$unaryMinus, "<this>");
        return new PointF(-$this$unaryMinus.x, -$this$unaryMinus.y);
    }
}
