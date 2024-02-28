package androidx.core.graphics;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RegionIterator;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u00004\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010(\n\u0002\b\u0007\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\f\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\f\u001a\u0015\u0010\u0004\u001a\u00020\u0005*\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0007H\n\u001a3\u0010\b\u001a\u00020\t*\u00020\u00012!\u0010\n\u001a\u001d\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u00020\t0\u000bH\bø\u0001\u0000\u001a\u0013\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00030\u0010*\u00020\u0001H\u0002\u001a\u0015\u0010\u0011\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n\u001a\u0015\u0010\u0011\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\n\u001a\r\u0010\u0012\u001a\u00020\u0001*\u00020\u0001H\n\u001a\u0015\u0010\u0013\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\f\u001a\u0015\u0010\u0013\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\f\u001a\u0015\u0010\u0014\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n\u001a\u0015\u0010\u0014\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\n\u001a\r\u0010\u0015\u001a\u00020\u0001*\u00020\u0001H\n\u001a\u0015\u0010\u0016\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\f\u001a\u0015\u0010\u0016\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\f\u0002\u0007\n\u0005\b20\u0001¨\u0006\u0017"}, d2 = {"and", "Landroid/graphics/Region;", "r", "Landroid/graphics/Rect;", "contains", "", "p", "Landroid/graphics/Point;", "forEach", "", "action", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "rect", "iterator", "", "minus", "not", "or", "plus", "unaryMinus", "xor", "core-ktx_release"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: Region.kt */
public final class RegionKt {
    public static final Region and(Region $this$and, Rect r) {
        Intrinsics.checkNotNullParameter($this$and, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        Region region = new Region($this$and);
        region.op(r, Region.Op.INTERSECT);
        return region;
    }

    public static final Region and(Region $this$and, Region r) {
        Intrinsics.checkNotNullParameter($this$and, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        Region region = new Region($this$and);
        region.op(r, Region.Op.INTERSECT);
        return region;
    }

    public static final boolean contains(Region $this$contains, Point p) {
        Intrinsics.checkNotNullParameter($this$contains, "<this>");
        Intrinsics.checkNotNullParameter(p, "p");
        return $this$contains.contains(p.x, p.y);
    }

    public static final void forEach(Region $this$forEach, Function1<? super Rect, Unit> action) {
        Intrinsics.checkNotNullParameter($this$forEach, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        RegionIterator regionIterator = new RegionIterator($this$forEach);
        while (true) {
            Rect rect = new Rect();
            if (regionIterator.next(rect)) {
                action.invoke(rect);
            } else {
                return;
            }
        }
    }

    public static final Iterator<Rect> iterator(Region $this$iterator) {
        Intrinsics.checkNotNullParameter($this$iterator, "<this>");
        return new RegionKt$iterator$1($this$iterator);
    }

    public static final Region minus(Region $this$minus, Rect r) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        Region region = new Region($this$minus);
        region.op(r, Region.Op.DIFFERENCE);
        return region;
    }

    public static final Region minus(Region $this$minus, Region r) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        Region region = new Region($this$minus);
        region.op(r, Region.Op.DIFFERENCE);
        return region;
    }

    public static final Region not(Region $this$not) {
        Intrinsics.checkNotNullParameter($this$not, "<this>");
        Region region = $this$not;
        Region region2 = new Region(region.getBounds());
        region2.op(region, Region.Op.DIFFERENCE);
        return region2;
    }

    public static final Region or(Region $this$or, Rect r) {
        Intrinsics.checkNotNullParameter($this$or, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        Region region = new Region($this$or);
        region.union(r);
        return region;
    }

    public static final Region or(Region $this$or, Region r) {
        Intrinsics.checkNotNullParameter($this$or, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        Region region = new Region($this$or);
        region.op(r, Region.Op.UNION);
        return region;
    }

    public static final Region plus(Region $this$plus, Rect r) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        Region region = new Region($this$plus);
        region.union(r);
        return region;
    }

    public static final Region plus(Region $this$plus, Region r) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        Region region = new Region($this$plus);
        region.op(r, Region.Op.UNION);
        return region;
    }

    public static final Region unaryMinus(Region $this$unaryMinus) {
        Intrinsics.checkNotNullParameter($this$unaryMinus, "<this>");
        Region region = new Region($this$unaryMinus.getBounds());
        region.op($this$unaryMinus, Region.Op.DIFFERENCE);
        return region;
    }

    public static final Region xor(Region $this$xor, Rect r) {
        Intrinsics.checkNotNullParameter($this$xor, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        Region region = new Region($this$xor);
        region.op(r, Region.Op.XOR);
        return region;
    }

    public static final Region xor(Region $this$xor, Region r) {
        Intrinsics.checkNotNullParameter($this$xor, "<this>");
        Intrinsics.checkNotNullParameter(r, "r");
        Region region = new Region($this$xor);
        region.op(r, Region.Op.XOR);
        return region;
    }
}
