package androidx.core.graphics;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u0000<\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\f\u001a\u0015\u0010\u0000\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\f\u001a\r\u0010\u0004\u001a\u00020\u0005*\u00020\u0001H\n\u001a\r\u0010\u0004\u001a\u00020\u0006*\u00020\u0003H\n\u001a\r\u0010\u0007\u001a\u00020\u0005*\u00020\u0001H\n\u001a\r\u0010\u0007\u001a\u00020\u0006*\u00020\u0003H\n\u001a\r\u0010\b\u001a\u00020\u0005*\u00020\u0001H\n\u001a\r\u0010\b\u001a\u00020\u0006*\u00020\u0003H\n\u001a\r\u0010\t\u001a\u00020\u0005*\u00020\u0001H\n\u001a\r\u0010\t\u001a\u00020\u0006*\u00020\u0003H\n\u001a\u0015\u0010\n\u001a\u00020\u000b*\u00020\u00012\u0006\u0010\f\u001a\u00020\rH\n\u001a\u0015\u0010\n\u001a\u00020\u000b*\u00020\u00032\u0006\u0010\f\u001a\u00020\u000eH\n\u001a\u0015\u0010\u000f\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\rH\n\u001a\u0015\u0010\u000f\u001a\u00020\u0011*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\n\u001a\u0015\u0010\u000f\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0005H\n\u001a\u0015\u0010\u000f\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u000eH\n\u001a\u0015\u0010\u000f\u001a\u00020\u0011*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\n\u001a\u0015\u0010\u000f\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u0006H\n\u001a\u0015\u0010\u0012\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\f\u001a\u0015\u0010\u0012\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\f\u001a\u0015\u0010\u0013\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\rH\n\u001a\u0015\u0010\u0013\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\n\u001a\u0015\u0010\u0013\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0005H\n\u001a\u0015\u0010\u0013\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u000eH\n\u001a\u0015\u0010\u0013\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\n\u001a\u0015\u0010\u0013\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u0006H\n\u001a\u0015\u0010\u0014\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u0005H\n\u001a\u0015\u0010\u0014\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0006H\n\u001a\u0015\u0010\u0014\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0005H\n\u001a\r\u0010\u0016\u001a\u00020\u0001*\u00020\u0003H\b\u001a\r\u0010\u0017\u001a\u00020\u0003*\u00020\u0001H\b\u001a\r\u0010\u0018\u001a\u00020\u0011*\u00020\u0001H\b\u001a\r\u0010\u0018\u001a\u00020\u0011*\u00020\u0003H\b\u001a\u0015\u0010\u0019\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u001bH\b\u001a\u0015\u0010\u001c\u001a\u00020\u0011*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\f\u001a\u0015\u0010\u001c\u001a\u00020\u0011*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\f¨\u0006\u001d"}, d2 = {"and", "Landroid/graphics/Rect;", "r", "Landroid/graphics/RectF;", "component1", "", "", "component2", "component3", "component4", "contains", "", "p", "Landroid/graphics/Point;", "Landroid/graphics/PointF;", "minus", "xy", "Landroid/graphics/Region;", "or", "plus", "times", "factor", "toRect", "toRectF", "toRegion", "transform", "m", "Landroid/graphics/Matrix;", "xor", "core-ktx_release"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: Rect.kt */
public final class RectKt {
    public static final Rect and(Rect $this$and, Rect r) {
        Intrinsics.checkNotNullParameter($this$and, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        Rect rect = new Rect($this$and);
        rect.intersect(r);
        return rect;
    }

    public static final RectF and(RectF $this$and, RectF r) {
        Intrinsics.checkNotNullParameter($this$and, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        RectF rectF = new RectF($this$and);
        rectF.intersect(r);
        return rectF;
    }

    public static final float component1(RectF $this$component1) {
        Intrinsics.checkNotNullParameter($this$component1, "<this>");
        return $this$component1.left;
    }

    public static final int component1(Rect $this$component1) {
        Intrinsics.checkNotNullParameter($this$component1, "<this>");
        return $this$component1.left;
    }

    public static final float component2(RectF $this$component2) {
        Intrinsics.checkNotNullParameter($this$component2, "<this>");
        return $this$component2.top;
    }

    public static final int component2(Rect $this$component2) {
        Intrinsics.checkNotNullParameter($this$component2, "<this>");
        return $this$component2.top;
    }

    public static final float component3(RectF $this$component3) {
        Intrinsics.checkNotNullParameter($this$component3, "<this>");
        return $this$component3.right;
    }

    public static final int component3(Rect $this$component3) {
        Intrinsics.checkNotNullParameter($this$component3, "<this>");
        return $this$component3.right;
    }

    public static final float component4(RectF $this$component4) {
        Intrinsics.checkNotNullParameter($this$component4, "<this>");
        return $this$component4.bottom;
    }

    public static final int component4(Rect $this$component4) {
        Intrinsics.checkNotNullParameter($this$component4, "<this>");
        return $this$component4.bottom;
    }

    public static final boolean contains(Rect $this$contains, Point p) {
        Intrinsics.checkNotNullParameter($this$contains, "<this>");
        Intrinsics.checkNotNullParameter(p, "p");
        return $this$contains.contains(p.x, p.y);
    }

    public static final boolean contains(RectF $this$contains, PointF p) {
        Intrinsics.checkNotNullParameter($this$contains, "<this>");
        Intrinsics.checkNotNullParameter(p, "p");
        return $this$contains.contains(p.x, p.y);
    }

    public static final Rect minus(Rect $this$minus, int xy) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Rect rect = new Rect($this$minus);
        rect.offset(-xy, -xy);
        return rect;
    }

    public static final Rect minus(Rect $this$minus, Point xy) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(xy, "xy");
        Rect rect = new Rect($this$minus);
        rect.offset(-xy.x, -xy.y);
        return rect;
    }

    public static final RectF minus(RectF $this$minus, float xy) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        RectF rectF = new RectF($this$minus);
        rectF.offset(-xy, -xy);
        return rectF;
    }

    public static final RectF minus(RectF $this$minus, PointF xy) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(xy, "xy");
        RectF rectF = new RectF($this$minus);
        rectF.offset(-xy.x, -xy.y);
        return rectF;
    }

    public static final Region minus(Rect $this$minus, Rect r) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        Region region = new Region($this$minus);
        region.op(r, Region.Op.DIFFERENCE);
        return region;
    }

    public static final Region minus(RectF $this$minus, RectF r) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        Rect rect = new Rect();
        $this$minus.roundOut(rect);
        Region region = new Region(rect);
        Rect rect2 = new Rect();
        r.roundOut(rect2);
        region.op(rect2, Region.Op.DIFFERENCE);
        return region;
    }

    public static final Rect or(Rect $this$or, Rect r) {
        Intrinsics.checkNotNullParameter($this$or, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        Rect rect = new Rect($this$or);
        rect.union(r);
        return rect;
    }

    public static final RectF or(RectF $this$or, RectF r) {
        Intrinsics.checkNotNullParameter($this$or, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        RectF rectF = new RectF($this$or);
        rectF.union(r);
        return rectF;
    }

    public static final Rect plus(Rect $this$plus, int xy) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Rect rect = new Rect($this$plus);
        rect.offset(xy, xy);
        return rect;
    }

    public static final Rect plus(Rect $this$plus, Point xy) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(xy, "xy");
        Rect rect = new Rect($this$plus);
        rect.offset(xy.x, xy.y);
        return rect;
    }

    public static final Rect plus(Rect $this$plus, Rect r) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        Rect rect = new Rect($this$plus);
        rect.union(r);
        return rect;
    }

    public static final RectF plus(RectF $this$plus, float xy) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        RectF rectF = new RectF($this$plus);
        rectF.offset(xy, xy);
        return rectF;
    }

    public static final RectF plus(RectF $this$plus, PointF xy) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(xy, "xy");
        RectF rectF = new RectF($this$plus);
        rectF.offset(xy.x, xy.y);
        return rectF;
    }

    public static final RectF plus(RectF $this$plus, RectF r) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        RectF rectF = new RectF($this$plus);
        rectF.union(r);
        return rectF;
    }

    public static final Rect times(Rect $this$times, int factor) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Rect rect = new Rect($this$times);
        Rect rect2 = rect;
        rect2.top *= factor;
        rect2.left *= factor;
        rect2.right *= factor;
        rect2.bottom *= factor;
        return rect;
    }

    public static final RectF times(RectF $this$times, float factor) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        RectF rectF = new RectF($this$times);
        RectF rectF2 = rectF;
        rectF2.top *= factor;
        rectF2.left *= factor;
        rectF2.right *= factor;
        rectF2.bottom *= factor;
        return rectF;
    }

    public static final RectF times(RectF $this$times, int factor) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        float f = (float) factor;
        RectF rectF = new RectF($this$times);
        RectF rectF2 = rectF;
        rectF2.top *= f;
        rectF2.left *= f;
        rectF2.right *= f;
        rectF2.bottom *= f;
        return rectF;
    }

    public static final Rect toRect(RectF $this$toRect) {
        Intrinsics.checkNotNullParameter($this$toRect, "<this>");
        Rect rect = new Rect();
        $this$toRect.roundOut(rect);
        return rect;
    }

    public static final RectF toRectF(Rect $this$toRectF) {
        Intrinsics.checkNotNullParameter($this$toRectF, "<this>");
        return new RectF($this$toRectF);
    }

    public static final Region toRegion(Rect $this$toRegion) {
        Intrinsics.checkNotNullParameter($this$toRegion, "<this>");
        return new Region($this$toRegion);
    }

    public static final Region toRegion(RectF $this$toRegion) {
        Intrinsics.checkNotNullParameter($this$toRegion, "<this>");
        Rect rect = new Rect();
        $this$toRegion.roundOut(rect);
        return new Region(rect);
    }

    public static final RectF transform(RectF $this$transform, Matrix m) {
        Intrinsics.checkNotNullParameter($this$transform, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        RectF rectF = $this$transform;
        m.mapRect($this$transform);
        return $this$transform;
    }

    public static final Region xor(Rect $this$xor, Rect r) {
        Intrinsics.checkNotNullParameter($this$xor, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        Region region = new Region($this$xor);
        region.op(r, Region.Op.XOR);
        return region;
    }

    public static final Region xor(RectF $this$xor, RectF r) {
        Intrinsics.checkNotNullParameter($this$xor, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        Rect rect = new Rect();
        $this$xor.roundOut(rect);
        Region region = new Region(rect);
        Rect rect2 = new Rect();
        r.roundOut(rect2);
        region.op(rect2, Region.Op.XOR);
        return region;
    }
}
