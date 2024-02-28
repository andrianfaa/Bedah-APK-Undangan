package kotlin.text;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ReplaceWith;
import kotlin.TuplesKt;
import kotlin.collections.ArraysKt;
import kotlin.collections.CharIterator;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import mt.Log1F380D;
import okhttp3.HttpUrl;

@Metadata(d1 = {"\u0000\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\r\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0019\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\b\n\u0002\u0010\u0011\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b!\u001a\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0006H\u0000\u001a\u001c\u0010\f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001c\u0010\u0011\u001a\u00020\r*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001f\u0010\u0012\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0002\u001a\u001f\u0010\u0012\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0002\u001a\u0015\u0010\u0012\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0016H\n\u001a\u0018\u0010\u0017\u001a\u00020\u0010*\u0004\u0018\u00010\u00022\b\u0010\u000e\u001a\u0004\u0018\u00010\u0002H\u0000\u001a\u0018\u0010\u0018\u001a\u00020\u0010*\u0004\u0018\u00010\u00022\b\u0010\u000e\u001a\u0004\u0018\u00010\u0002H\u0000\u001a\u001c\u0010\u0019\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001c\u0010\u0019\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a:\u0010\u001b\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\r\u0018\u00010\u001c*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001aE\u0010\u001b\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\r\u0018\u00010\u001c*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010 \u001a\u00020\u0010H\u0002¢\u0006\u0002\b!\u001a:\u0010\"\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\r\u0018\u00010\u001c*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u0012\u0010#\u001a\u00020\u0010*\u00020\u00022\u0006\u0010$\u001a\u00020\u0006\u001a7\u0010%\u001a\u0002H&\"\f\b\u0000\u0010'*\u00020\u0002*\u0002H&\"\u0004\b\u0001\u0010&*\u0002H'2\f\u0010(\u001a\b\u0012\u0004\u0012\u0002H&0)H\bø\u0001\u0000¢\u0006\u0002\u0010*\u001a7\u0010+\u001a\u0002H&\"\f\b\u0000\u0010'*\u00020\u0002*\u0002H&\"\u0004\b\u0001\u0010&*\u0002H'2\f\u0010(\u001a\b\u0012\u0004\u0012\u0002H&0)H\bø\u0001\u0000¢\u0006\u0002\u0010*\u001a&\u0010,\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a;\u0010,\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010 \u001a\u00020\u0010H\u0002¢\u0006\u0002\b.\u001a&\u0010,\u001a\u00020\u0006*\u00020\u00022\u0006\u0010/\u001a\u00020\r2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a&\u00100\u001a\u00020\u0006*\u00020\u00022\u0006\u00101\u001a\u0002022\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a,\u00100\u001a\u00020\u0006*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\r\u00103\u001a\u00020\u0010*\u00020\u0002H\b\u001a\r\u00104\u001a\u00020\u0010*\u00020\u0002H\b\u001a\r\u00105\u001a\u00020\u0010*\u00020\u0002H\b\u001a \u00106\u001a\u00020\u0010*\u0004\u0018\u00010\u0002H\b\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a \u00107\u001a\u00020\u0010*\u0004\u0018\u00010\u0002H\b\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a\r\u00108\u001a\u000209*\u00020\u0002H\u0002\u001a&\u0010:\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a&\u0010:\u001a\u00020\u0006*\u00020\u00022\u0006\u0010/\u001a\u00020\r2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a&\u0010;\u001a\u00020\u0006*\u00020\u00022\u0006\u00101\u001a\u0002022\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a,\u0010;\u001a\u00020\u0006*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u0010\u0010<\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u0002\u001a\u0010\u0010>\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u0002\u001a\u0015\u0010@\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0016H\f\u001a\u000f\u0010A\u001a\u00020\r*\u0004\u0018\u00010\rH\b\u001a\u001c\u0010B\u001a\u00020\u0002*\u00020\u00022\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001a\u001c\u0010B\u001a\u00020\r*\u00020\r2\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001a\u001c\u0010E\u001a\u00020\u0002*\u00020\u00022\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001a\u001c\u0010E\u001a\u00020\r*\u00020\r2\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001aG\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00010=*\u00020\u00022\u000e\u0010G\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0H2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u0002¢\u0006\u0004\bI\u0010J\u001a=\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00010=*\u00020\u00022\u0006\u0010G\u001a\u0002022\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u0002¢\u0006\u0002\bI\u001a4\u0010K\u001a\u00020\u0010*\u00020\u00022\u0006\u0010L\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010M\u001a\u00020\u00062\u0006\u0010C\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0010H\u0000\u001a\u0012\u0010N\u001a\u00020\u0002*\u00020\u00022\u0006\u0010O\u001a\u00020\u0002\u001a\u0012\u0010N\u001a\u00020\r*\u00020\r2\u0006\u0010O\u001a\u00020\u0002\u001a\u001a\u0010P\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u0006\u001a\u0012\u0010P\u001a\u00020\u0002*\u00020\u00022\u0006\u0010Q\u001a\u00020\u0001\u001a\u001d\u0010P\u001a\u00020\r*\u00020\r2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u0006H\b\u001a\u0015\u0010P\u001a\u00020\r*\u00020\r2\u0006\u0010Q\u001a\u00020\u0001H\b\u001a\u0012\u0010R\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002\u001a\u0012\u0010R\u001a\u00020\r*\u00020\r2\u0006\u0010\u001a\u001a\u00020\u0002\u001a\u0012\u0010S\u001a\u00020\u0002*\u00020\u00022\u0006\u0010T\u001a\u00020\u0002\u001a\u001a\u0010S\u001a\u00020\u0002*\u00020\u00022\u0006\u0010O\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002\u001a\u0012\u0010S\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u0002\u001a\u001a\u0010S\u001a\u00020\r*\u00020\r2\u0006\u0010O\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002\u001a.\u0010U\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0014\b\b\u0010V\u001a\u000e\u0012\u0004\u0012\u00020X\u0012\u0004\u0012\u00020\u00020WH\bø\u0001\u0000\u001a\u001d\u0010U\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010Y\u001a\u00020\rH\b\u001a$\u0010Z\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010Z\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010\\\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010\\\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010]\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010]\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010^\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010^\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001d\u0010_\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010Y\u001a\u00020\rH\b\u001a)\u0010`\u001a\u00020\r*\u00020\r2\u0012\u0010V\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00140WH\bø\u0001\u0000¢\u0006\u0002\ba\u001a)\u0010`\u001a\u00020\r*\u00020\r2\u0012\u0010V\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00020WH\bø\u0001\u0000¢\u0006\u0002\bb\u001a\"\u0010c\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u00062\u0006\u0010Y\u001a\u00020\u0002\u001a\u001a\u0010c\u001a\u00020\u0002*\u00020\u00022\u0006\u0010Q\u001a\u00020\u00012\u0006\u0010Y\u001a\u00020\u0002\u001a%\u0010c\u001a\u00020\r*\u00020\r2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u00062\u0006\u0010Y\u001a\u00020\u0002H\b\u001a\u001d\u0010c\u001a\u00020\r*\u00020\r2\u0006\u0010Q\u001a\u00020\u00012\u0006\u0010Y\u001a\u00020\u0002H\b\u001a=\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\u0012\u0010G\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0H\"\u00020\r2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006¢\u0006\u0002\u0010e\u001a0\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\n\u0010G\u001a\u000202\"\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006\u001a/\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\u0006\u0010T\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u000b\u001a\u00020\u0006H\u0002¢\u0006\u0002\bf\u001a%\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\b\u001a=\u0010g\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u00022\u0012\u0010G\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0H\"\u00020\r2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006¢\u0006\u0002\u0010h\u001a0\u0010g\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u00022\n\u0010G\u001a\u000202\"\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006\u001a%\u0010g\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\b\u001a\u001c\u0010i\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001c\u0010i\u001a\u00020\u0010*\u00020\u00022\u0006\u0010O\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a$\u0010i\u001a\u00020\u0010*\u00020\u00022\u0006\u0010O\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u0012\u0010j\u001a\u00020\u0002*\u00020\u00022\u0006\u0010Q\u001a\u00020\u0001\u001a\u001d\u0010j\u001a\u00020\u0002*\u00020\r2\u0006\u0010k\u001a\u00020\u00062\u0006\u0010l\u001a\u00020\u0006H\b\u001a\u001f\u0010m\u001a\u00020\r*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010-\u001a\u00020\u0006H\b\u001a\u0012\u0010m\u001a\u00020\r*\u00020\u00022\u0006\u0010Q\u001a\u00020\u0001\u001a\u0012\u0010m\u001a\u00020\r*\u00020\r2\u0006\u0010Q\u001a\u00020\u0001\u001a\u001c\u0010n\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010n\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010o\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010o\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010p\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010p\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010q\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010q\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\f\u0010r\u001a\u00020\u0010*\u00020\rH\u0007\u001a\u0013\u0010s\u001a\u0004\u0018\u00010\u0010*\u00020\rH\u0007¢\u0006\u0002\u0010t\u001a\n\u0010u\u001a\u00020\u0002*\u00020\u0002\u001a$\u0010u\u001a\u00020\u0002*\u00020\u00022\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\bø\u0001\u0000\u001a\u0016\u0010u\u001a\u00020\u0002*\u00020\u00022\n\u00101\u001a\u000202\"\u00020\u0014\u001a\r\u0010u\u001a\u00020\r*\u00020\rH\b\u001a$\u0010u\u001a\u00020\r*\u00020\r2\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\bø\u0001\u0000\u001a\u0016\u0010u\u001a\u00020\r*\u00020\r2\n\u00101\u001a\u000202\"\u00020\u0014\u001a\n\u0010w\u001a\u00020\u0002*\u00020\u0002\u001a$\u0010w\u001a\u00020\u0002*\u00020\u00022\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\bø\u0001\u0000\u001a\u0016\u0010w\u001a\u00020\u0002*\u00020\u00022\n\u00101\u001a\u000202\"\u00020\u0014\u001a\r\u0010w\u001a\u00020\r*\u00020\rH\b\u001a$\u0010w\u001a\u00020\r*\u00020\r2\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\bø\u0001\u0000\u001a\u0016\u0010w\u001a\u00020\r*\u00020\r2\n\u00101\u001a\u000202\"\u00020\u0014\u001a\n\u0010x\u001a\u00020\u0002*\u00020\u0002\u001a$\u0010x\u001a\u00020\u0002*\u00020\u00022\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\bø\u0001\u0000\u001a\u0016\u0010x\u001a\u00020\u0002*\u00020\u00022\n\u00101\u001a\u000202\"\u00020\u0014\u001a\r\u0010x\u001a\u00020\r*\u00020\rH\b\u001a$\u0010x\u001a\u00020\r*\u00020\r2\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\bø\u0001\u0000\u001a\u0016\u0010x\u001a\u00020\r*\u00020\r2\n\u00101\u001a\u000202\"\u00020\u0014\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b\u0002\u0007\n\u0005\b20\u0001¨\u0006y"}, d2 = {"indices", "Lkotlin/ranges/IntRange;", "", "getIndices", "(Ljava/lang/CharSequence;)Lkotlin/ranges/IntRange;", "lastIndex", "", "getLastIndex", "(Ljava/lang/CharSequence;)I", "requireNonNegativeLimit", "", "limit", "commonPrefixWith", "", "other", "ignoreCase", "", "commonSuffixWith", "contains", "char", "", "regex", "Lkotlin/text/Regex;", "contentEqualsIgnoreCaseImpl", "contentEqualsImpl", "endsWith", "suffix", "findAnyOf", "Lkotlin/Pair;", "strings", "", "startIndex", "last", "findAnyOf$StringsKt__StringsKt", "findLastAnyOf", "hasSurrogatePairAt", "index", "ifBlank", "R", "C", "defaultValue", "Lkotlin/Function0;", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "ifEmpty", "indexOf", "endIndex", "indexOf$StringsKt__StringsKt", "string", "indexOfAny", "chars", "", "isEmpty", "isNotBlank", "isNotEmpty", "isNullOrBlank", "isNullOrEmpty", "iterator", "Lkotlin/collections/CharIterator;", "lastIndexOf", "lastIndexOfAny", "lineSequence", "Lkotlin/sequences/Sequence;", "lines", "", "matches", "orEmpty", "padEnd", "length", "padChar", "padStart", "rangesDelimitedBy", "delimiters", "", "rangesDelimitedBy$StringsKt__StringsKt", "(Ljava/lang/CharSequence;[Ljava/lang/String;IZI)Lkotlin/sequences/Sequence;", "regionMatchesImpl", "thisOffset", "otherOffset", "removePrefix", "prefix", "removeRange", "range", "removeSuffix", "removeSurrounding", "delimiter", "replace", "transform", "Lkotlin/Function1;", "Lkotlin/text/MatchResult;", "replacement", "replaceAfter", "missingDelimiterValue", "replaceAfterLast", "replaceBefore", "replaceBeforeLast", "replaceFirst", "replaceFirstChar", "replaceFirstCharWithChar", "replaceFirstCharWithCharSequence", "replaceRange", "split", "(Ljava/lang/CharSequence;[Ljava/lang/String;ZI)Ljava/util/List;", "split$StringsKt__StringsKt", "splitToSequence", "(Ljava/lang/CharSequence;[Ljava/lang/String;ZI)Lkotlin/sequences/Sequence;", "startsWith", "subSequence", "start", "end", "substring", "substringAfter", "substringAfterLast", "substringBefore", "substringBeforeLast", "toBooleanStrict", "toBooleanStrictOrNull", "(Ljava/lang/String;)Ljava/lang/Boolean;", "trim", "predicate", "trimEnd", "trimStart", "kotlin-stdlib"}, k = 5, mv = {1, 7, 1}, xi = 49, xs = "kotlin/text/StringsKt")
/* compiled from: 015F */
class StringsKt__StringsKt extends StringsKt__StringsJVMKt {
    public static final String commonPrefixWith(CharSequence $this$commonPrefixWith, CharSequence other, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$commonPrefixWith, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        int min = Math.min($this$commonPrefixWith.length(), other.length());
        int i = 0;
        while (i < min && CharsKt.equals($this$commonPrefixWith.charAt(i), other.charAt(i), ignoreCase)) {
            i++;
        }
        if (StringsKt.hasSurrogatePairAt($this$commonPrefixWith, i - 1) || StringsKt.hasSurrogatePairAt(other, i - 1)) {
            i--;
        }
        return $this$commonPrefixWith.subSequence(0, i).toString();
    }

    public static /* synthetic */ String commonPrefixWith$default(CharSequence charSequence, CharSequence charSequence2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        String commonPrefixWith = StringsKt.commonPrefixWith(charSequence, charSequence2, z);
        Log1F380D.a((Object) commonPrefixWith);
        return commonPrefixWith;
    }

    public static final String commonSuffixWith(CharSequence $this$commonSuffixWith, CharSequence other, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$commonSuffixWith, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        int length = $this$commonSuffixWith.length();
        int length2 = other.length();
        int min = Math.min(length, length2);
        int i = 0;
        while (i < min && CharsKt.equals($this$commonSuffixWith.charAt((length - i) - 1), other.charAt((length2 - i) - 1), ignoreCase)) {
            i++;
        }
        if (StringsKt.hasSurrogatePairAt($this$commonSuffixWith, (length - i) - 1) || StringsKt.hasSurrogatePairAt(other, (length2 - i) - 1)) {
            i--;
        }
        return $this$commonSuffixWith.subSequence(length - i, length).toString();
    }

    public static final boolean contains(CharSequence $this$contains, char c, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return StringsKt.indexOf$default($this$contains, c, 0, ignoreCase, 2, (Object) null) >= 0;
    }

    public static final boolean contains(CharSequence $this$contains, CharSequence other, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$contains, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        if (other instanceof String) {
            return StringsKt.indexOf$default($this$contains, (String) other, 0, ignoreCase, 2, (Object) null) >= 0;
        }
        return indexOf$StringsKt__StringsKt$default($this$contains, other, 0, $this$contains.length(), ignoreCase, false, 16, (Object) null) >= 0;
    }

    private static final boolean contains(CharSequence $this$contains, Regex regex) {
        Intrinsics.checkNotNullParameter($this$contains, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        return regex.containsMatchIn($this$contains);
    }

    public static /* synthetic */ boolean contains$default(CharSequence charSequence, char c, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.contains(charSequence, c, z);
    }

    public static /* synthetic */ boolean contains$default(CharSequence charSequence, CharSequence charSequence2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.contains(charSequence, charSequence2, z);
    }

    public static final boolean contentEqualsIgnoreCaseImpl(CharSequence $this$contentEqualsIgnoreCaseImpl, CharSequence other) {
        if (($this$contentEqualsIgnoreCaseImpl instanceof String) && (other instanceof String)) {
            return StringsKt.equals((String) $this$contentEqualsIgnoreCaseImpl, (String) other, true);
        }
        if ($this$contentEqualsIgnoreCaseImpl == other) {
            return true;
        }
        if ($this$contentEqualsIgnoreCaseImpl == null || other == null || $this$contentEqualsIgnoreCaseImpl.length() != other.length()) {
            return false;
        }
        int length = $this$contentEqualsIgnoreCaseImpl.length();
        for (int i = 0; i < length; i++) {
            if (!CharsKt.equals($this$contentEqualsIgnoreCaseImpl.charAt(i), other.charAt(i), true)) {
                return false;
            }
        }
        return true;
    }

    public static final boolean contentEqualsImpl(CharSequence $this$contentEqualsImpl, CharSequence other) {
        if (($this$contentEqualsImpl instanceof String) && (other instanceof String)) {
            return Intrinsics.areEqual((Object) $this$contentEqualsImpl, (Object) other);
        }
        if ($this$contentEqualsImpl == other) {
            return true;
        }
        if ($this$contentEqualsImpl == null || other == null || $this$contentEqualsImpl.length() != other.length()) {
            return false;
        }
        int length = $this$contentEqualsImpl.length();
        for (int i = 0; i < length; i++) {
            if ($this$contentEqualsImpl.charAt(i) != other.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static final boolean endsWith(CharSequence $this$endsWith, char c, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$endsWith, "<this>");
        return $this$endsWith.length() > 0 && CharsKt.equals($this$endsWith.charAt(StringsKt.getLastIndex($this$endsWith)), c, ignoreCase);
    }

    public static final boolean endsWith(CharSequence $this$endsWith, CharSequence suffix, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$endsWith, "<this>");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        if (!ignoreCase && ($this$endsWith instanceof String) && (suffix instanceof String)) {
            return StringsKt.endsWith$default((String) $this$endsWith, (String) suffix, false, 2, (Object) null);
        }
        return StringsKt.regionMatchesImpl($this$endsWith, $this$endsWith.length() - suffix.length(), suffix, 0, suffix.length(), ignoreCase);
    }

    public static /* synthetic */ boolean endsWith$default(CharSequence charSequence, char c, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.endsWith(charSequence, c, z);
    }

    public static /* synthetic */ boolean endsWith$default(CharSequence charSequence, CharSequence charSequence2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.endsWith(charSequence, charSequence2, z);
    }

    public static final Pair<Integer, String> findAnyOf(CharSequence $this$findAnyOf, Collection<String> strings, int startIndex, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$findAnyOf, "<this>");
        Intrinsics.checkNotNullParameter(strings, "strings");
        return findAnyOf$StringsKt__StringsKt($this$findAnyOf, strings, startIndex, ignoreCase, false);
    }

    /* access modifiers changed from: private */
    public static final Pair<Integer, String> findAnyOf$StringsKt__StringsKt(CharSequence $this$findAnyOf, Collection<String> strings, int startIndex, boolean ignoreCase, boolean last) {
        Object obj;
        Object obj2;
        CharSequence charSequence = $this$findAnyOf;
        int i = startIndex;
        if (ignoreCase || strings.size() != 1) {
            IntProgression intRange = !last ? new IntRange(RangesKt.coerceAtLeast(i, 0), $this$findAnyOf.length()) : RangesKt.downTo(RangesKt.coerceAtMost(i, StringsKt.getLastIndex($this$findAnyOf)), 0);
            if (charSequence instanceof String) {
                int first = intRange.getFirst();
                int last2 = intRange.getLast();
                int step = intRange.getStep();
                if ((step > 0 && first <= last2) || (step < 0 && last2 <= first)) {
                    int i2 = first;
                    while (true) {
                        Iterator it = strings.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                obj2 = null;
                                break;
                            }
                            obj2 = it.next();
                            String str = (String) obj2;
                            if (StringsKt.regionMatches(str, 0, (String) charSequence, i2, str.length(), ignoreCase)) {
                                break;
                            }
                        }
                        String str2 = (String) obj2;
                        if (str2 == null) {
                            if (i2 == last2) {
                                break;
                            }
                            i2 += step;
                        } else {
                            return TuplesKt.to(Integer.valueOf(i2), str2);
                        }
                    }
                }
            } else {
                int first2 = intRange.getFirst();
                int last3 = intRange.getLast();
                int step2 = intRange.getStep();
                if ((step2 > 0 && first2 <= last3) || (step2 < 0 && last3 <= first2)) {
                    int i3 = first2;
                    while (true) {
                        Iterator it2 = strings.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                obj = null;
                                break;
                            }
                            obj = it2.next();
                            String str3 = (String) obj;
                            if (StringsKt.regionMatchesImpl(str3, 0, $this$findAnyOf, i3, str3.length(), ignoreCase)) {
                                break;
                            }
                        }
                        String str4 = (String) obj;
                        if (str4 == null) {
                            if (i3 == last3) {
                                break;
                            }
                            i3 += step2;
                        } else {
                            return TuplesKt.to(Integer.valueOf(i3), str4);
                        }
                    }
                }
            }
            return null;
        }
        String str5 = (String) CollectionsKt.single(strings);
        CharSequence charSequence2 = $this$findAnyOf;
        String str6 = str5;
        int i4 = startIndex;
        int indexOf$default = !last ? StringsKt.indexOf$default(charSequence2, str6, i4, false, 4, (Object) null) : StringsKt.lastIndexOf$default(charSequence2, str6, i4, false, 4, (Object) null);
        if (indexOf$default < 0) {
            return null;
        }
        return TuplesKt.to(Integer.valueOf(indexOf$default), str5);
    }

    public static /* synthetic */ Pair findAnyOf$default(CharSequence charSequence, Collection collection, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.findAnyOf(charSequence, collection, i, z);
    }

    public static final Pair<Integer, String> findLastAnyOf(CharSequence $this$findLastAnyOf, Collection<String> strings, int startIndex, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$findLastAnyOf, "<this>");
        Intrinsics.checkNotNullParameter(strings, "strings");
        return findAnyOf$StringsKt__StringsKt($this$findLastAnyOf, strings, startIndex, ignoreCase, true);
    }

    public static /* synthetic */ Pair findLastAnyOf$default(CharSequence charSequence, Collection collection, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.findLastAnyOf(charSequence, collection, i, z);
    }

    public static final IntRange getIndices(CharSequence $this$indices) {
        Intrinsics.checkNotNullParameter($this$indices, "<this>");
        return new IntRange(0, $this$indices.length() - 1);
    }

    public static final int getLastIndex(CharSequence $this$lastIndex) {
        Intrinsics.checkNotNullParameter($this$lastIndex, "<this>");
        return $this$lastIndex.length() - 1;
    }

    public static final boolean hasSurrogatePairAt(CharSequence $this$hasSurrogatePairAt, int index) {
        Intrinsics.checkNotNullParameter($this$hasSurrogatePairAt, "<this>");
        return new IntRange(0, $this$hasSurrogatePairAt.length() + -2).contains(index) && Character.isHighSurrogate($this$hasSurrogatePairAt.charAt(index)) && Character.isLowSurrogate($this$hasSurrogatePairAt.charAt(index + 1));
    }

    private static final <C extends CharSequence & R, R> R ifBlank(C $this$ifBlank, Function0<? extends R> defaultValue) {
        Intrinsics.checkNotNullParameter(defaultValue, "defaultValue");
        return StringsKt.isBlank($this$ifBlank) ? defaultValue.invoke() : $this$ifBlank;
    }

    private static final <C extends CharSequence & R, R> R ifEmpty(C $this$ifEmpty, Function0<? extends R> defaultValue) {
        Intrinsics.checkNotNullParameter(defaultValue, "defaultValue");
        return $this$ifEmpty.length() == 0 ? defaultValue.invoke() : $this$ifEmpty;
    }

    public static final int indexOf(CharSequence $this$indexOf, char c, int startIndex, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$indexOf, "<this>");
        if (!ignoreCase && ($this$indexOf instanceof String)) {
            return ((String) $this$indexOf).indexOf(c, startIndex);
        }
        return StringsKt.indexOfAny($this$indexOf, new char[]{c}, startIndex, ignoreCase);
    }

    public static final int indexOf(CharSequence $this$indexOf, String string, int startIndex, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$indexOf, "<this>");
        Intrinsics.checkNotNullParameter(string, TypedValues.Custom.S_STRING);
        if (!ignoreCase && ($this$indexOf instanceof String)) {
            return ((String) $this$indexOf).indexOf(string, startIndex);
        }
        return indexOf$StringsKt__StringsKt$default($this$indexOf, string, startIndex, $this$indexOf.length(), ignoreCase, false, 16, (Object) null);
    }

    private static final int indexOf$StringsKt__StringsKt(CharSequence $this$indexOf, CharSequence other, int startIndex, int endIndex, boolean ignoreCase, boolean last) {
        IntProgression intRange = !last ? new IntRange(RangesKt.coerceAtLeast(startIndex, 0), RangesKt.coerceAtMost(endIndex, $this$indexOf.length())) : RangesKt.downTo(RangesKt.coerceAtMost(startIndex, StringsKt.getLastIndex($this$indexOf)), RangesKt.coerceAtLeast(endIndex, 0));
        if (!($this$indexOf instanceof String) || !(other instanceof String)) {
            int first = intRange.getFirst();
            int last2 = intRange.getLast();
            int step = intRange.getStep();
            if ((step <= 0 || first > last2) && (step >= 0 || last2 > first)) {
                return -1;
            }
            while (true) {
                if (StringsKt.regionMatchesImpl(other, 0, $this$indexOf, first, other.length(), ignoreCase)) {
                    return first;
                }
                if (first == last2) {
                    return -1;
                }
                first += step;
            }
        } else {
            int first2 = intRange.getFirst();
            int last3 = intRange.getLast();
            int step2 = intRange.getStep();
            if ((step2 <= 0 || first2 > last3) && (step2 >= 0 || last3 > first2)) {
                return -1;
            }
            while (true) {
                if (StringsKt.regionMatches((String) other, 0, (String) $this$indexOf, first2, other.length(), ignoreCase)) {
                    return first2;
                }
                if (first2 == last3) {
                    return -1;
                }
                first2 += step2;
            }
        }
    }

    static /* synthetic */ int indexOf$StringsKt__StringsKt$default(CharSequence charSequence, CharSequence charSequence2, int i, int i2, boolean z, boolean z2, int i3, Object obj) {
        if ((i3 & 16) != 0) {
            z2 = false;
        }
        return indexOf$StringsKt__StringsKt(charSequence, charSequence2, i, i2, z, z2);
    }

    public static /* synthetic */ int indexOf$default(CharSequence charSequence, char c, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.indexOf(charSequence, c, i, z);
    }

    public static /* synthetic */ int indexOf$default(CharSequence charSequence, String str, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.indexOf(charSequence, str, i, z);
    }

    public static final int indexOfAny(CharSequence $this$indexOfAny, Collection<String> strings, int startIndex, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$indexOfAny, "<this>");
        Intrinsics.checkNotNullParameter(strings, "strings");
        Pair<Integer, String> findAnyOf$StringsKt__StringsKt = findAnyOf$StringsKt__StringsKt($this$indexOfAny, strings, startIndex, ignoreCase, false);
        if (findAnyOf$StringsKt__StringsKt != null) {
            return findAnyOf$StringsKt__StringsKt.getFirst().intValue();
        }
        return -1;
    }

    public static final int indexOfAny(CharSequence $this$indexOfAny, char[] chars, int startIndex, boolean ignoreCase) {
        boolean z;
        Intrinsics.checkNotNullParameter($this$indexOfAny, "<this>");
        Intrinsics.checkNotNullParameter(chars, "chars");
        if (ignoreCase || chars.length != 1 || !($this$indexOfAny instanceof String)) {
            IntIterator it = new IntRange(RangesKt.coerceAtLeast(startIndex, 0), StringsKt.getLastIndex($this$indexOfAny)).iterator();
            while (it.hasNext()) {
                int nextInt = it.nextInt();
                char charAt = $this$indexOfAny.charAt(nextInt);
                char[] cArr = chars;
                int length = cArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        z = false;
                        continue;
                        break;
                    } else if (CharsKt.equals(cArr[i], charAt, ignoreCase)) {
                        z = true;
                        continue;
                        break;
                    } else {
                        i++;
                    }
                }
                if (z) {
                    return nextInt;
                }
            }
            return -1;
        }
        return ((String) $this$indexOfAny).indexOf(ArraysKt.single(chars), startIndex);
    }

    public static /* synthetic */ int indexOfAny$default(CharSequence charSequence, Collection collection, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.indexOfAny(charSequence, (Collection<String>) collection, i, z);
    }

    public static /* synthetic */ int indexOfAny$default(CharSequence charSequence, char[] cArr, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.indexOfAny(charSequence, cArr, i, z);
    }

    private static final boolean isEmpty(CharSequence $this$isEmpty) {
        Intrinsics.checkNotNullParameter($this$isEmpty, "<this>");
        return $this$isEmpty.length() == 0;
    }

    private static final boolean isNotBlank(CharSequence $this$isNotBlank) {
        Intrinsics.checkNotNullParameter($this$isNotBlank, "<this>");
        return !StringsKt.isBlank($this$isNotBlank);
    }

    private static final boolean isNotEmpty(CharSequence $this$isNotEmpty) {
        Intrinsics.checkNotNullParameter($this$isNotEmpty, "<this>");
        return $this$isNotEmpty.length() > 0;
    }

    private static final boolean isNullOrBlank(CharSequence $this$isNullOrBlank) {
        return $this$isNullOrBlank == null || StringsKt.isBlank($this$isNullOrBlank);
    }

    private static final boolean isNullOrEmpty(CharSequence $this$isNullOrEmpty) {
        return $this$isNullOrEmpty == null || $this$isNullOrEmpty.length() == 0;
    }

    public static final CharIterator iterator(CharSequence $this$iterator) {
        Intrinsics.checkNotNullParameter($this$iterator, "<this>");
        return new StringsKt__StringsKt$iterator$1($this$iterator);
    }

    public static final int lastIndexOf(CharSequence $this$lastIndexOf, char c, int startIndex, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$lastIndexOf, "<this>");
        if (!ignoreCase && ($this$lastIndexOf instanceof String)) {
            return ((String) $this$lastIndexOf).lastIndexOf(c, startIndex);
        }
        return StringsKt.lastIndexOfAny($this$lastIndexOf, new char[]{c}, startIndex, ignoreCase);
    }

    public static final int lastIndexOf(CharSequence $this$lastIndexOf, String string, int startIndex, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$lastIndexOf, "<this>");
        Intrinsics.checkNotNullParameter(string, TypedValues.Custom.S_STRING);
        if (!ignoreCase && ($this$lastIndexOf instanceof String)) {
            return ((String) $this$lastIndexOf).lastIndexOf(string, startIndex);
        }
        return indexOf$StringsKt__StringsKt($this$lastIndexOf, string, startIndex, 0, ignoreCase, true);
    }

    public static /* synthetic */ int lastIndexOf$default(CharSequence charSequence, char c, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.lastIndexOf(charSequence, c, i, z);
    }

    public static /* synthetic */ int lastIndexOf$default(CharSequence charSequence, String str, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.lastIndexOf(charSequence, str, i, z);
    }

    public static final int lastIndexOfAny(CharSequence $this$lastIndexOfAny, Collection<String> strings, int startIndex, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$lastIndexOfAny, "<this>");
        Intrinsics.checkNotNullParameter(strings, "strings");
        Pair<Integer, String> findAnyOf$StringsKt__StringsKt = findAnyOf$StringsKt__StringsKt($this$lastIndexOfAny, strings, startIndex, ignoreCase, true);
        if (findAnyOf$StringsKt__StringsKt != null) {
            return findAnyOf$StringsKt__StringsKt.getFirst().intValue();
        }
        return -1;
    }

    public static final int lastIndexOfAny(CharSequence $this$lastIndexOfAny, char[] chars, int startIndex, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$lastIndexOfAny, "<this>");
        Intrinsics.checkNotNullParameter(chars, "chars");
        if (ignoreCase || chars.length != 1 || !($this$lastIndexOfAny instanceof String)) {
            for (int coerceAtMost = RangesKt.coerceAtMost(startIndex, StringsKt.getLastIndex($this$lastIndexOfAny)); -1 < coerceAtMost; coerceAtMost--) {
                char charAt = $this$lastIndexOfAny.charAt(coerceAtMost);
                char[] cArr = chars;
                int length = cArr.length;
                boolean z = false;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    } else if (CharsKt.equals(cArr[i], charAt, ignoreCase)) {
                        z = true;
                        break;
                    } else {
                        i++;
                    }
                }
                if (z) {
                    return coerceAtMost;
                }
            }
            return -1;
        }
        return ((String) $this$lastIndexOfAny).lastIndexOf(ArraysKt.single(chars), startIndex);
    }

    public static /* synthetic */ int lastIndexOfAny$default(CharSequence charSequence, Collection collection, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.lastIndexOfAny(charSequence, (Collection<String>) collection, i, z);
    }

    public static /* synthetic */ int lastIndexOfAny$default(CharSequence charSequence, char[] cArr, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.lastIndexOfAny(charSequence, cArr, i, z);
    }

    public static final Sequence<String> lineSequence(CharSequence $this$lineSequence) {
        Intrinsics.checkNotNullParameter($this$lineSequence, "<this>");
        return StringsKt.splitToSequence$default($this$lineSequence, new String[]{"\r\n", "\n", "\r"}, false, 0, 6, (Object) null);
    }

    public static final List<String> lines(CharSequence $this$lines) {
        Intrinsics.checkNotNullParameter($this$lines, "<this>");
        return SequencesKt.toList(StringsKt.lineSequence($this$lines));
    }

    private static final boolean matches(CharSequence $this$matches, Regex regex) {
        Intrinsics.checkNotNullParameter($this$matches, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        return regex.matches($this$matches);
    }

    private static final String orEmpty(String $this$orEmpty) {
        return $this$orEmpty == null ? HttpUrl.FRAGMENT_ENCODE_SET : $this$orEmpty;
    }

    public static final CharSequence padEnd(CharSequence $this$padEnd, int length, char padChar) {
        Intrinsics.checkNotNullParameter($this$padEnd, "<this>");
        if (length < 0) {
            throw new IllegalArgumentException("Desired length " + length + " is less than zero.");
        } else if (length <= $this$padEnd.length()) {
            return $this$padEnd.subSequence(0, $this$padEnd.length());
        } else {
            StringBuilder sb = new StringBuilder(length);
            sb.append($this$padEnd);
            IntIterator it = new IntRange(1, length - $this$padEnd.length()).iterator();
            while (it.hasNext()) {
                int nextInt = it.nextInt();
                sb.append(padChar);
            }
            return sb;
        }
    }

    public static final String padEnd(String $this$padEnd, int length, char padChar) {
        Intrinsics.checkNotNullParameter($this$padEnd, "<this>");
        return StringsKt.padEnd((CharSequence) $this$padEnd, length, padChar).toString();
    }

    public static /* synthetic */ CharSequence padEnd$default(CharSequence charSequence, int i, char c, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            c = ' ';
        }
        return StringsKt.padEnd(charSequence, i, c);
    }

    public static final CharSequence padStart(CharSequence $this$padStart, int length, char padChar) {
        Intrinsics.checkNotNullParameter($this$padStart, "<this>");
        if (length < 0) {
            throw new IllegalArgumentException("Desired length " + length + " is less than zero.");
        } else if (length <= $this$padStart.length()) {
            return $this$padStart.subSequence(0, $this$padStart.length());
        } else {
            StringBuilder sb = new StringBuilder(length);
            IntIterator it = new IntRange(1, length - $this$padStart.length()).iterator();
            while (it.hasNext()) {
                int nextInt = it.nextInt();
                sb.append(padChar);
            }
            sb.append($this$padStart);
            return sb;
        }
    }

    public static final String padStart(String $this$padStart, int length, char padChar) {
        Intrinsics.checkNotNullParameter($this$padStart, "<this>");
        return StringsKt.padStart((CharSequence) $this$padStart, length, padChar).toString();
    }

    public static /* synthetic */ CharSequence padStart$default(CharSequence charSequence, int i, char c, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            c = ' ';
        }
        return StringsKt.padStart(charSequence, i, c);
    }

    private static final Sequence<IntRange> rangesDelimitedBy$StringsKt__StringsKt(CharSequence $this$rangesDelimitedBy, char[] delimiters, int startIndex, boolean ignoreCase, int limit) {
        StringsKt.requireNonNegativeLimit(limit);
        return new DelimitedRangesSequence($this$rangesDelimitedBy, startIndex, limit, new StringsKt__StringsKt$rangesDelimitedBy$1(delimiters, ignoreCase));
    }

    private static final Sequence<IntRange> rangesDelimitedBy$StringsKt__StringsKt(CharSequence $this$rangesDelimitedBy, String[] delimiters, int startIndex, boolean ignoreCase, int limit) {
        StringsKt.requireNonNegativeLimit(limit);
        return new DelimitedRangesSequence($this$rangesDelimitedBy, startIndex, limit, new StringsKt__StringsKt$rangesDelimitedBy$2(ArraysKt.asList((T[]) delimiters), ignoreCase));
    }

    static /* synthetic */ Sequence rangesDelimitedBy$StringsKt__StringsKt$default(CharSequence charSequence, char[] cArr, int i, boolean z, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        if ((i3 & 8) != 0) {
            i2 = 0;
        }
        return rangesDelimitedBy$StringsKt__StringsKt(charSequence, cArr, i, z, i2);
    }

    static /* synthetic */ Sequence rangesDelimitedBy$StringsKt__StringsKt$default(CharSequence charSequence, String[] strArr, int i, boolean z, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        if ((i3 & 8) != 0) {
            i2 = 0;
        }
        return rangesDelimitedBy$StringsKt__StringsKt(charSequence, strArr, i, z, i2);
    }

    public static final boolean regionMatchesImpl(CharSequence $this$regionMatchesImpl, int thisOffset, CharSequence other, int otherOffset, int length, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$regionMatchesImpl, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        if (otherOffset < 0 || thisOffset < 0 || thisOffset > $this$regionMatchesImpl.length() - length || otherOffset > other.length() - length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (!CharsKt.equals($this$regionMatchesImpl.charAt(thisOffset + i), other.charAt(otherOffset + i), ignoreCase)) {
                return false;
            }
        }
        return true;
    }

    public static final CharSequence removePrefix(CharSequence $this$removePrefix, CharSequence prefix) {
        Intrinsics.checkNotNullParameter($this$removePrefix, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        return StringsKt.startsWith$default($this$removePrefix, prefix, false, 2, (Object) null) ? $this$removePrefix.subSequence(prefix.length(), $this$removePrefix.length()) : $this$removePrefix.subSequence(0, $this$removePrefix.length());
    }

    public static final String removePrefix(String $this$removePrefix, CharSequence prefix) {
        Intrinsics.checkNotNullParameter($this$removePrefix, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        if (!StringsKt.startsWith$default((CharSequence) $this$removePrefix, prefix, false, 2, (Object) null)) {
            return $this$removePrefix;
        }
        String substring = $this$removePrefix.substring(prefix.length());
        Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String).substring(startIndex)");
        return substring;
    }

    public static final CharSequence removeRange(CharSequence $this$removeRange, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$removeRange, "<this>");
        if (endIndex < startIndex) {
            throw new IndexOutOfBoundsException("End index (" + endIndex + ") is less than start index (" + startIndex + ").");
        } else if (endIndex == startIndex) {
            return $this$removeRange.subSequence(0, $this$removeRange.length());
        } else {
            StringBuilder sb = new StringBuilder($this$removeRange.length() - (endIndex - startIndex));
            Intrinsics.checkNotNullExpressionValue(sb.append($this$removeRange, 0, startIndex), "this.append(value, startIndex, endIndex)");
            Intrinsics.checkNotNullExpressionValue(sb.append($this$removeRange, endIndex, $this$removeRange.length()), "this.append(value, startIndex, endIndex)");
            return sb;
        }
    }

    public static final CharSequence removeRange(CharSequence $this$removeRange, IntRange range) {
        Intrinsics.checkNotNullParameter($this$removeRange, "<this>");
        Intrinsics.checkNotNullParameter(range, "range");
        return StringsKt.removeRange($this$removeRange, range.getStart().intValue(), range.getEndInclusive().intValue() + 1);
    }

    private static final String removeRange(String $this$removeRange, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$removeRange, "<this>");
        return StringsKt.removeRange((CharSequence) $this$removeRange, startIndex, endIndex).toString();
    }

    private static final String removeRange(String $this$removeRange, IntRange range) {
        Intrinsics.checkNotNullParameter($this$removeRange, "<this>");
        Intrinsics.checkNotNullParameter(range, "range");
        return StringsKt.removeRange((CharSequence) $this$removeRange, range).toString();
    }

    public static final CharSequence removeSuffix(CharSequence $this$removeSuffix, CharSequence suffix) {
        Intrinsics.checkNotNullParameter($this$removeSuffix, "<this>");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        return StringsKt.endsWith$default($this$removeSuffix, suffix, false, 2, (Object) null) ? $this$removeSuffix.subSequence(0, $this$removeSuffix.length() - suffix.length()) : $this$removeSuffix.subSequence(0, $this$removeSuffix.length());
    }

    public static final String removeSuffix(String $this$removeSuffix, CharSequence suffix) {
        Intrinsics.checkNotNullParameter($this$removeSuffix, "<this>");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        if (!StringsKt.endsWith$default((CharSequence) $this$removeSuffix, suffix, false, 2, (Object) null)) {
            return $this$removeSuffix;
        }
        String substring = $this$removeSuffix.substring(0, $this$removeSuffix.length() - suffix.length());
        Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public static final CharSequence removeSurrounding(CharSequence $this$removeSurrounding, CharSequence delimiter) {
        Intrinsics.checkNotNullParameter($this$removeSurrounding, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        return StringsKt.removeSurrounding($this$removeSurrounding, delimiter, delimiter);
    }

    public static final CharSequence removeSurrounding(CharSequence $this$removeSurrounding, CharSequence prefix, CharSequence suffix) {
        Intrinsics.checkNotNullParameter($this$removeSurrounding, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        return ($this$removeSurrounding.length() < prefix.length() + suffix.length() || !StringsKt.startsWith$default($this$removeSurrounding, prefix, false, 2, (Object) null) || !StringsKt.endsWith$default($this$removeSurrounding, suffix, false, 2, (Object) null)) ? $this$removeSurrounding.subSequence(0, $this$removeSurrounding.length()) : $this$removeSurrounding.subSequence(prefix.length(), $this$removeSurrounding.length() - suffix.length());
    }

    public static final String removeSurrounding(String $this$removeSurrounding, CharSequence prefix, CharSequence suffix) {
        Intrinsics.checkNotNullParameter($this$removeSurrounding, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        if ($this$removeSurrounding.length() < prefix.length() + suffix.length() || !StringsKt.startsWith$default((CharSequence) $this$removeSurrounding, prefix, false, 2, (Object) null) || !StringsKt.endsWith$default((CharSequence) $this$removeSurrounding, suffix, false, 2, (Object) null)) {
            return $this$removeSurrounding;
        }
        String substring = $this$removeSurrounding.substring(prefix.length(), $this$removeSurrounding.length() - suffix.length());
        Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    private static final String replace(CharSequence $this$replace, Regex regex, String replacement) {
        Intrinsics.checkNotNullParameter($this$replace, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        return regex.replace($this$replace, replacement);
    }

    private static final String replace(CharSequence $this$replace, Regex regex, Function1<? super MatchResult, ? extends CharSequence> transform) {
        Intrinsics.checkNotNullParameter($this$replace, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        Intrinsics.checkNotNullParameter(transform, "transform");
        return regex.replace($this$replace, transform);
    }

    public static final String replaceAfter(String $this$replaceAfter, char delimiter, String replacement, String missingDelimiterValue) {
        Intrinsics.checkNotNullParameter($this$replaceAfter, "<this>");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int indexOf$default = StringsKt.indexOf$default((CharSequence) $this$replaceAfter, delimiter, 0, false, 6, (Object) null);
        if (indexOf$default == -1) {
            return missingDelimiterValue;
        }
        return StringsKt.replaceRange((CharSequence) $this$replaceAfter, indexOf$default + 1, $this$replaceAfter.length(), (CharSequence) replacement).toString();
    }

    public static final String replaceAfter(String $this$replaceAfter, String delimiter, String replacement, String missingDelimiterValue) {
        Intrinsics.checkNotNullParameter($this$replaceAfter, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int indexOf$default = StringsKt.indexOf$default((CharSequence) $this$replaceAfter, delimiter, 0, false, 6, (Object) null);
        if (indexOf$default == -1) {
            return missingDelimiterValue;
        }
        return StringsKt.replaceRange((CharSequence) $this$replaceAfter, delimiter.length() + indexOf$default, $this$replaceAfter.length(), (CharSequence) replacement).toString();
    }

    public static final String replaceAfterLast(String $this$replaceAfterLast, char delimiter, String replacement, String missingDelimiterValue) {
        Intrinsics.checkNotNullParameter($this$replaceAfterLast, "<this>");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int lastIndexOf$default = StringsKt.lastIndexOf$default((CharSequence) $this$replaceAfterLast, delimiter, 0, false, 6, (Object) null);
        if (lastIndexOf$default == -1) {
            return missingDelimiterValue;
        }
        return StringsKt.replaceRange((CharSequence) $this$replaceAfterLast, lastIndexOf$default + 1, $this$replaceAfterLast.length(), (CharSequence) replacement).toString();
    }

    public static final String replaceAfterLast(String $this$replaceAfterLast, String delimiter, String replacement, String missingDelimiterValue) {
        Intrinsics.checkNotNullParameter($this$replaceAfterLast, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int lastIndexOf$default = StringsKt.lastIndexOf$default((CharSequence) $this$replaceAfterLast, delimiter, 0, false, 6, (Object) null);
        if (lastIndexOf$default == -1) {
            return missingDelimiterValue;
        }
        return StringsKt.replaceRange((CharSequence) $this$replaceAfterLast, delimiter.length() + lastIndexOf$default, $this$replaceAfterLast.length(), (CharSequence) replacement).toString();
    }

    public static final String replaceBefore(String $this$replaceBefore, char delimiter, String replacement, String missingDelimiterValue) {
        Intrinsics.checkNotNullParameter($this$replaceBefore, "<this>");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int indexOf$default = StringsKt.indexOf$default((CharSequence) $this$replaceBefore, delimiter, 0, false, 6, (Object) null);
        return indexOf$default == -1 ? missingDelimiterValue : StringsKt.replaceRange((CharSequence) $this$replaceBefore, 0, indexOf$default, (CharSequence) replacement).toString();
    }

    public static final String replaceBefore(String $this$replaceBefore, String delimiter, String replacement, String missingDelimiterValue) {
        Intrinsics.checkNotNullParameter($this$replaceBefore, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int indexOf$default = StringsKt.indexOf$default((CharSequence) $this$replaceBefore, delimiter, 0, false, 6, (Object) null);
        return indexOf$default == -1 ? missingDelimiterValue : StringsKt.replaceRange((CharSequence) $this$replaceBefore, 0, indexOf$default, (CharSequence) replacement).toString();
    }

    public static final String replaceBeforeLast(String $this$replaceBeforeLast, char delimiter, String replacement, String missingDelimiterValue) {
        Intrinsics.checkNotNullParameter($this$replaceBeforeLast, "<this>");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int lastIndexOf$default = StringsKt.lastIndexOf$default((CharSequence) $this$replaceBeforeLast, delimiter, 0, false, 6, (Object) null);
        return lastIndexOf$default == -1 ? missingDelimiterValue : StringsKt.replaceRange((CharSequence) $this$replaceBeforeLast, 0, lastIndexOf$default, (CharSequence) replacement).toString();
    }

    public static final String replaceBeforeLast(String $this$replaceBeforeLast, String delimiter, String replacement, String missingDelimiterValue) {
        Intrinsics.checkNotNullParameter($this$replaceBeforeLast, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int lastIndexOf$default = StringsKt.lastIndexOf$default((CharSequence) $this$replaceBeforeLast, delimiter, 0, false, 6, (Object) null);
        return lastIndexOf$default == -1 ? missingDelimiterValue : StringsKt.replaceRange((CharSequence) $this$replaceBeforeLast, 0, lastIndexOf$default, (CharSequence) replacement).toString();
    }

    private static final String replaceFirst(CharSequence $this$replaceFirst, Regex regex, String replacement) {
        Intrinsics.checkNotNullParameter($this$replaceFirst, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        return regex.replaceFirst($this$replaceFirst, replacement);
    }

    private static final String replaceFirstCharWithChar(String $this$replaceFirstChar, Function1<? super Character, Character> transform) {
        Intrinsics.checkNotNullParameter($this$replaceFirstChar, "<this>");
        Intrinsics.checkNotNullParameter(transform, "transform");
        if (!($this$replaceFirstChar.length() > 0)) {
            return $this$replaceFirstChar;
        }
        char charValue = transform.invoke(Character.valueOf($this$replaceFirstChar.charAt(0))).charValue();
        String substring = $this$replaceFirstChar.substring(1);
        Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String).substring(startIndex)");
        return charValue + substring;
    }

    private static final String replaceFirstCharWithCharSequence(String $this$replaceFirstChar, Function1<? super Character, ? extends CharSequence> transform) {
        Intrinsics.checkNotNullParameter($this$replaceFirstChar, "<this>");
        Intrinsics.checkNotNullParameter(transform, "transform");
        if (!($this$replaceFirstChar.length() > 0)) {
            return $this$replaceFirstChar;
        }
        StringBuilder append = new StringBuilder().append(transform.invoke(Character.valueOf($this$replaceFirstChar.charAt(0))));
        String substring = $this$replaceFirstChar.substring(1);
        Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String).substring(startIndex)");
        return append.append(substring).toString();
    }

    public static final CharSequence replaceRange(CharSequence $this$replaceRange, int startIndex, int endIndex, CharSequence replacement) {
        Intrinsics.checkNotNullParameter($this$replaceRange, "<this>");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        if (endIndex >= startIndex) {
            StringBuilder sb = new StringBuilder();
            Intrinsics.checkNotNullExpressionValue(sb.append($this$replaceRange, 0, startIndex), "this.append(value, startIndex, endIndex)");
            sb.append(replacement);
            Intrinsics.checkNotNullExpressionValue(sb.append($this$replaceRange, endIndex, $this$replaceRange.length()), "this.append(value, startIndex, endIndex)");
            return sb;
        }
        throw new IndexOutOfBoundsException("End index (" + endIndex + ") is less than start index (" + startIndex + ").");
    }

    public static final CharSequence replaceRange(CharSequence $this$replaceRange, IntRange range, CharSequence replacement) {
        Intrinsics.checkNotNullParameter($this$replaceRange, "<this>");
        Intrinsics.checkNotNullParameter(range, "range");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        return StringsKt.replaceRange($this$replaceRange, range.getStart().intValue(), range.getEndInclusive().intValue() + 1, replacement);
    }

    private static final String replaceRange(String $this$replaceRange, int startIndex, int endIndex, CharSequence replacement) {
        Intrinsics.checkNotNullParameter($this$replaceRange, "<this>");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        return StringsKt.replaceRange((CharSequence) $this$replaceRange, startIndex, endIndex, replacement).toString();
    }

    private static final String replaceRange(String $this$replaceRange, IntRange range, CharSequence replacement) {
        Intrinsics.checkNotNullParameter($this$replaceRange, "<this>");
        Intrinsics.checkNotNullParameter(range, "range");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        return StringsKt.replaceRange((CharSequence) $this$replaceRange, range, replacement).toString();
    }

    public static final void requireNonNegativeLimit(int limit) {
        if (!(limit >= 0)) {
            throw new IllegalArgumentException(("Limit must be non-negative, but was " + limit).toString());
        }
    }

    private static final List<String> split(CharSequence $this$split, Regex regex, int limit) {
        Intrinsics.checkNotNullParameter($this$split, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        return regex.split($this$split, limit);
    }

    private static final List<String> split$StringsKt__StringsKt(CharSequence $this$split, String delimiter, boolean ignoreCase, int limit) {
        StringsKt.requireNonNegativeLimit(limit);
        int i = 0;
        int indexOf = StringsKt.indexOf($this$split, delimiter, 0, ignoreCase);
        if (indexOf != -1) {
            boolean z = true;
            if (limit != 1) {
                if (limit <= 0) {
                    z = false;
                }
                int i2 = 10;
                if (z) {
                    i2 = RangesKt.coerceAtMost(limit, 10);
                }
                ArrayList arrayList = new ArrayList(i2);
                do {
                    arrayList.add($this$split.subSequence(i, indexOf).toString());
                    i = indexOf + delimiter.length();
                    if ((z && arrayList.size() == limit - 1) || (indexOf = StringsKt.indexOf($this$split, delimiter, i, ignoreCase)) == -1) {
                        arrayList.add($this$split.subSequence(i, $this$split.length()).toString());
                    }
                    arrayList.add($this$split.subSequence(i, indexOf).toString());
                    i = indexOf + delimiter.length();
                    break;
                } while ((indexOf = StringsKt.indexOf($this$split, delimiter, i, ignoreCase)) == -1);
                arrayList.add($this$split.subSequence(i, $this$split.length()).toString());
                return arrayList;
            }
        }
        return CollectionsKt.listOf($this$split.toString());
    }

    static /* synthetic */ List split$default(CharSequence $this$split_u24default, Regex regex, int limit, int i, Object obj) {
        if ((i & 2) != 0) {
            limit = 0;
        }
        Intrinsics.checkNotNullParameter($this$split_u24default, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        return regex.split($this$split_u24default, limit);
    }

    public static /* synthetic */ List split$default(CharSequence charSequence, char[] cArr, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return StringsKt.split(charSequence, cArr, z, i);
    }

    public static /* synthetic */ List split$default(CharSequence charSequence, String[] strArr, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return StringsKt.split(charSequence, strArr, z, i);
    }

    private static final Sequence<String> splitToSequence(CharSequence $this$splitToSequence, Regex regex, int limit) {
        Intrinsics.checkNotNullParameter($this$splitToSequence, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        return regex.splitToSequence($this$splitToSequence, limit);
    }

    public static final Sequence<String> splitToSequence(CharSequence $this$splitToSequence, char[] delimiters, boolean ignoreCase, int limit) {
        Intrinsics.checkNotNullParameter($this$splitToSequence, "<this>");
        Intrinsics.checkNotNullParameter(delimiters, "delimiters");
        return SequencesKt.map(rangesDelimitedBy$StringsKt__StringsKt$default($this$splitToSequence, delimiters, 0, ignoreCase, limit, 2, (Object) null), new StringsKt__StringsKt$splitToSequence$2($this$splitToSequence));
    }

    public static final Sequence<String> splitToSequence(CharSequence $this$splitToSequence, String[] delimiters, boolean ignoreCase, int limit) {
        Intrinsics.checkNotNullParameter($this$splitToSequence, "<this>");
        Intrinsics.checkNotNullParameter(delimiters, "delimiters");
        return SequencesKt.map(rangesDelimitedBy$StringsKt__StringsKt$default($this$splitToSequence, delimiters, 0, ignoreCase, limit, 2, (Object) null), new StringsKt__StringsKt$splitToSequence$1($this$splitToSequence));
    }

    static /* synthetic */ Sequence splitToSequence$default(CharSequence $this$splitToSequence_u24default, Regex regex, int limit, int i, Object obj) {
        if ((i & 2) != 0) {
            limit = 0;
        }
        Intrinsics.checkNotNullParameter($this$splitToSequence_u24default, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        return regex.splitToSequence($this$splitToSequence_u24default, limit);
    }

    public static /* synthetic */ Sequence splitToSequence$default(CharSequence charSequence, char[] cArr, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return StringsKt.splitToSequence(charSequence, cArr, z, i);
    }

    public static /* synthetic */ Sequence splitToSequence$default(CharSequence charSequence, String[] strArr, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return StringsKt.splitToSequence(charSequence, strArr, z, i);
    }

    public static final boolean startsWith(CharSequence $this$startsWith, char c, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$startsWith, "<this>");
        return $this$startsWith.length() > 0 && CharsKt.equals($this$startsWith.charAt(0), c, ignoreCase);
    }

    public static final boolean startsWith(CharSequence $this$startsWith, CharSequence prefix, int startIndex, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$startsWith, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        if (!ignoreCase && ($this$startsWith instanceof String) && (prefix instanceof String)) {
            return StringsKt.startsWith$default((String) $this$startsWith, (String) prefix, startIndex, false, 4, (Object) null);
        }
        return StringsKt.regionMatchesImpl($this$startsWith, startIndex, prefix, 0, prefix.length(), ignoreCase);
    }

    public static final boolean startsWith(CharSequence $this$startsWith, CharSequence prefix, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$startsWith, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        if (!ignoreCase && ($this$startsWith instanceof String) && (prefix instanceof String)) {
            return StringsKt.startsWith$default((String) $this$startsWith, (String) prefix, false, 2, (Object) null);
        }
        return StringsKt.regionMatchesImpl($this$startsWith, 0, prefix, 0, prefix.length(), ignoreCase);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence charSequence, char c, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.startsWith(charSequence, c, z);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence charSequence, CharSequence charSequence2, int i, boolean z, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.startsWith(charSequence, charSequence2, i, z);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence charSequence, CharSequence charSequence2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.startsWith(charSequence, charSequence2, z);
    }

    public static final CharSequence subSequence(CharSequence $this$subSequence, IntRange range) {
        Intrinsics.checkNotNullParameter($this$subSequence, "<this>");
        Intrinsics.checkNotNullParameter(range, "range");
        return $this$subSequence.subSequence(range.getStart().intValue(), range.getEndInclusive().intValue() + 1);
    }

    @Deprecated(message = "Use parameters named startIndex and endIndex.", replaceWith = @ReplaceWith(expression = "subSequence(startIndex = start, endIndex = end)", imports = {}))
    private static final CharSequence subSequence(String $this$subSequence, int start, int end) {
        Intrinsics.checkNotNullParameter($this$subSequence, "<this>");
        return $this$subSequence.subSequence(start, end);
    }

    private static final String substring(CharSequence $this$substring, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$substring, "<this>");
        return $this$substring.subSequence(startIndex, endIndex).toString();
    }

    public static final String substring(CharSequence $this$substring, IntRange range) {
        Intrinsics.checkNotNullParameter($this$substring, "<this>");
        Intrinsics.checkNotNullParameter(range, "range");
        return $this$substring.subSequence(range.getStart().intValue(), range.getEndInclusive().intValue() + 1).toString();
    }

    public static final String substring(String $this$substring, IntRange range) {
        Intrinsics.checkNotNullParameter($this$substring, "<this>");
        Intrinsics.checkNotNullParameter(range, "range");
        String substring = $this$substring.substring(range.getStart().intValue(), range.getEndInclusive().intValue() + 1);
        Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    static /* synthetic */ String substring$default(CharSequence $this$substring_u24default, int startIndex, int endIndex, int i, Object obj) {
        if ((i & 2) != 0) {
            endIndex = $this$substring_u24default.length();
        }
        Intrinsics.checkNotNullParameter($this$substring_u24default, "<this>");
        return $this$substring_u24default.subSequence(startIndex, endIndex).toString();
    }

    public static final String substringAfter(String $this$substringAfter, char delimiter, String missingDelimiterValue) {
        Intrinsics.checkNotNullParameter($this$substringAfter, "<this>");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int indexOf$default = StringsKt.indexOf$default((CharSequence) $this$substringAfter, delimiter, 0, false, 6, (Object) null);
        if (indexOf$default == -1) {
            return missingDelimiterValue;
        }
        String substring = $this$substringAfter.substring(indexOf$default + 1, $this$substringAfter.length());
        Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public static final String substringAfter(String $this$substringAfter, String delimiter, String missingDelimiterValue) {
        Intrinsics.checkNotNullParameter($this$substringAfter, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int indexOf$default = StringsKt.indexOf$default((CharSequence) $this$substringAfter, delimiter, 0, false, 6, (Object) null);
        if (indexOf$default == -1) {
            return missingDelimiterValue;
        }
        String substring = $this$substringAfter.substring(delimiter.length() + indexOf$default, $this$substringAfter.length());
        Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public static final String substringAfterLast(String $this$substringAfterLast, char delimiter, String missingDelimiterValue) {
        Intrinsics.checkNotNullParameter($this$substringAfterLast, "<this>");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int lastIndexOf$default = StringsKt.lastIndexOf$default((CharSequence) $this$substringAfterLast, delimiter, 0, false, 6, (Object) null);
        if (lastIndexOf$default == -1) {
            return missingDelimiterValue;
        }
        String substring = $this$substringAfterLast.substring(lastIndexOf$default + 1, $this$substringAfterLast.length());
        Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public static final String substringAfterLast(String $this$substringAfterLast, String delimiter, String missingDelimiterValue) {
        Intrinsics.checkNotNullParameter($this$substringAfterLast, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int lastIndexOf$default = StringsKt.lastIndexOf$default((CharSequence) $this$substringAfterLast, delimiter, 0, false, 6, (Object) null);
        if (lastIndexOf$default == -1) {
            return missingDelimiterValue;
        }
        String substring = $this$substringAfterLast.substring(delimiter.length() + lastIndexOf$default, $this$substringAfterLast.length());
        Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public static final String substringBefore(String $this$substringBefore, char delimiter, String missingDelimiterValue) {
        Intrinsics.checkNotNullParameter($this$substringBefore, "<this>");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int indexOf$default = StringsKt.indexOf$default((CharSequence) $this$substringBefore, delimiter, 0, false, 6, (Object) null);
        if (indexOf$default == -1) {
            return missingDelimiterValue;
        }
        String substring = $this$substringBefore.substring(0, indexOf$default);
        Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public static final String substringBefore(String $this$substringBefore, String delimiter, String missingDelimiterValue) {
        Intrinsics.checkNotNullParameter($this$substringBefore, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int indexOf$default = StringsKt.indexOf$default((CharSequence) $this$substringBefore, delimiter, 0, false, 6, (Object) null);
        if (indexOf$default == -1) {
            return missingDelimiterValue;
        }
        String substring = $this$substringBefore.substring(0, indexOf$default);
        Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public static final String substringBeforeLast(String $this$substringBeforeLast, char delimiter, String missingDelimiterValue) {
        Intrinsics.checkNotNullParameter($this$substringBeforeLast, "<this>");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int lastIndexOf$default = StringsKt.lastIndexOf$default((CharSequence) $this$substringBeforeLast, delimiter, 0, false, 6, (Object) null);
        if (lastIndexOf$default == -1) {
            return missingDelimiterValue;
        }
        String substring = $this$substringBeforeLast.substring(0, lastIndexOf$default);
        Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public static final String substringBeforeLast(String $this$substringBeforeLast, String delimiter, String missingDelimiterValue) {
        Intrinsics.checkNotNullParameter($this$substringBeforeLast, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int lastIndexOf$default = StringsKt.lastIndexOf$default((CharSequence) $this$substringBeforeLast, delimiter, 0, false, 6, (Object) null);
        if (lastIndexOf$default == -1) {
            return missingDelimiterValue;
        }
        String substring = $this$substringBeforeLast.substring(0, lastIndexOf$default);
        Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public static final boolean toBooleanStrict(String $this$toBooleanStrict) {
        Intrinsics.checkNotNullParameter($this$toBooleanStrict, "<this>");
        if (Intrinsics.areEqual((Object) $this$toBooleanStrict, (Object) "true")) {
            return true;
        }
        if (Intrinsics.areEqual((Object) $this$toBooleanStrict, (Object) "false")) {
            return false;
        }
        throw new IllegalArgumentException("The string doesn't represent a boolean value: " + $this$toBooleanStrict);
    }

    public static final Boolean toBooleanStrictOrNull(String $this$toBooleanStrictOrNull) {
        Intrinsics.checkNotNullParameter($this$toBooleanStrictOrNull, "<this>");
        if (Intrinsics.areEqual((Object) $this$toBooleanStrictOrNull, (Object) "true")) {
            return true;
        }
        return Intrinsics.areEqual((Object) $this$toBooleanStrictOrNull, (Object) "false") ? false : null;
    }

    public static final CharSequence trim(CharSequence $this$trim) {
        Intrinsics.checkNotNullParameter($this$trim, "<this>");
        CharSequence charSequence = $this$trim;
        int i = 0;
        int length = charSequence.length() - 1;
        boolean z = false;
        while (i <= length) {
            boolean isWhitespace = CharsKt.isWhitespace(charSequence.charAt(!z ? i : length));
            if (!z) {
                if (!isWhitespace) {
                    z = true;
                } else {
                    i++;
                }
            } else if (!isWhitespace) {
                break;
            } else {
                length--;
            }
        }
        return charSequence.subSequence(i, length + 1);
    }

    public static final CharSequence trim(CharSequence $this$trim, Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkNotNullParameter($this$trim, "<this>");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        int i = 0;
        int length = $this$trim.length() - 1;
        boolean z = false;
        while (i <= length) {
            boolean booleanValue = predicate.invoke(Character.valueOf($this$trim.charAt(!z ? i : length))).booleanValue();
            if (!z) {
                if (!booleanValue) {
                    z = true;
                } else {
                    i++;
                }
            } else if (!booleanValue) {
                break;
            } else {
                length--;
            }
        }
        return $this$trim.subSequence(i, length + 1);
    }

    public static final CharSequence trim(CharSequence $this$trim, char... chars) {
        Intrinsics.checkNotNullParameter($this$trim, "<this>");
        Intrinsics.checkNotNullParameter(chars, "chars");
        CharSequence charSequence = $this$trim;
        int i = 0;
        int length = charSequence.length() - 1;
        boolean z = false;
        while (i <= length) {
            boolean contains = ArraysKt.contains(chars, charSequence.charAt(!z ? i : length));
            if (!z) {
                if (!contains) {
                    z = true;
                } else {
                    i++;
                }
            } else if (!contains) {
                break;
            } else {
                length--;
            }
        }
        return charSequence.subSequence(i, length + 1);
    }

    private static final String trim(String $this$trim) {
        Intrinsics.checkNotNullParameter($this$trim, "<this>");
        return StringsKt.trim((CharSequence) $this$trim).toString();
    }

    public static final String trim(String $this$trim, Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkNotNullParameter($this$trim, "<this>");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        CharSequence charSequence = $this$trim;
        int i = 0;
        int length = charSequence.length() - 1;
        boolean z = false;
        while (i <= length) {
            boolean booleanValue = predicate.invoke(Character.valueOf(charSequence.charAt(!z ? i : length))).booleanValue();
            if (!z) {
                if (!booleanValue) {
                    z = true;
                } else {
                    i++;
                }
            } else if (!booleanValue) {
                break;
            } else {
                length--;
            }
        }
        return charSequence.subSequence(i, length + 1).toString();
    }

    public static final String trim(String $this$trim, char... chars) {
        Intrinsics.checkNotNullParameter($this$trim, "<this>");
        Intrinsics.checkNotNullParameter(chars, "chars");
        CharSequence charSequence = $this$trim;
        int i = 0;
        int length = charSequence.length() - 1;
        boolean z = false;
        while (i <= length) {
            boolean contains = ArraysKt.contains(chars, charSequence.charAt(!z ? i : length));
            if (!z) {
                if (!contains) {
                    z = true;
                } else {
                    i++;
                }
            } else if (!contains) {
                break;
            } else {
                length--;
            }
        }
        return charSequence.subSequence(i, length + 1).toString();
    }

    public static final CharSequence trimEnd(CharSequence $this$trimEnd) {
        Intrinsics.checkNotNullParameter($this$trimEnd, "<this>");
        CharSequence charSequence = $this$trimEnd;
        int length = charSequence.length() - 1;
        if (length >= 0) {
            do {
                int i = length;
                length--;
                if (!CharsKt.isWhitespace(charSequence.charAt(i))) {
                    return charSequence.subSequence(0, i + 1);
                }
            } while (length >= 0);
        }
        return HttpUrl.FRAGMENT_ENCODE_SET;
    }

    public static final CharSequence trimEnd(CharSequence $this$trimEnd, Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkNotNullParameter($this$trimEnd, "<this>");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        int length = $this$trimEnd.length() - 1;
        if (length >= 0) {
            do {
                int i = length;
                length--;
                if (!predicate.invoke(Character.valueOf($this$trimEnd.charAt(i))).booleanValue()) {
                    return $this$trimEnd.subSequence(0, i + 1);
                }
            } while (length >= 0);
        }
        return HttpUrl.FRAGMENT_ENCODE_SET;
    }

    public static final CharSequence trimEnd(CharSequence $this$trimEnd, char... chars) {
        Intrinsics.checkNotNullParameter($this$trimEnd, "<this>");
        Intrinsics.checkNotNullParameter(chars, "chars");
        CharSequence charSequence = $this$trimEnd;
        int length = charSequence.length() - 1;
        if (length >= 0) {
            do {
                int i = length;
                length--;
                if (!ArraysKt.contains(chars, charSequence.charAt(i))) {
                    return charSequence.subSequence(0, i + 1);
                }
            } while (length >= 0);
        }
        return HttpUrl.FRAGMENT_ENCODE_SET;
    }

    private static final String trimEnd(String $this$trimEnd) {
        Intrinsics.checkNotNullParameter($this$trimEnd, "<this>");
        return StringsKt.trimEnd((CharSequence) $this$trimEnd).toString();
    }

    public static final String trimEnd(String $this$trimEnd, Function1<? super Character, Boolean> predicate) {
        CharSequence charSequence;
        Intrinsics.checkNotNullParameter($this$trimEnd, "<this>");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        CharSequence charSequence2 = $this$trimEnd;
        int length = charSequence2.length() - 1;
        if (length >= 0) {
            while (true) {
                int i = length;
                length--;
                if (predicate.invoke(Character.valueOf(charSequence2.charAt(i))).booleanValue()) {
                    if (length < 0) {
                        break;
                    }
                } else {
                    charSequence = charSequence2.subSequence(0, i + 1);
                    break;
                }
            }
        }
        return charSequence.toString();
    }

    public static final String trimEnd(String $this$trimEnd, char... chars) {
        CharSequence charSequence;
        Intrinsics.checkNotNullParameter($this$trimEnd, "<this>");
        Intrinsics.checkNotNullParameter(chars, "chars");
        CharSequence charSequence2 = $this$trimEnd;
        int length = charSequence2.length() - 1;
        if (length >= 0) {
            while (true) {
                int i = length;
                length--;
                if (ArraysKt.contains(chars, charSequence2.charAt(i))) {
                    if (length < 0) {
                        break;
                    }
                } else {
                    charSequence = charSequence2.subSequence(0, i + 1);
                    break;
                }
            }
        }
        return charSequence.toString();
    }

    public static final CharSequence trimStart(CharSequence $this$trimStart) {
        Intrinsics.checkNotNullParameter($this$trimStart, "<this>");
        CharSequence charSequence = $this$trimStart;
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (!CharsKt.isWhitespace(charSequence.charAt(i))) {
                return charSequence.subSequence(i, charSequence.length());
            }
        }
        return HttpUrl.FRAGMENT_ENCODE_SET;
    }

    public static final CharSequence trimStart(CharSequence $this$trimStart, Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkNotNullParameter($this$trimStart, "<this>");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        int length = $this$trimStart.length();
        for (int i = 0; i < length; i++) {
            if (!predicate.invoke(Character.valueOf($this$trimStart.charAt(i))).booleanValue()) {
                return $this$trimStart.subSequence(i, $this$trimStart.length());
            }
        }
        return HttpUrl.FRAGMENT_ENCODE_SET;
    }

    public static final CharSequence trimStart(CharSequence $this$trimStart, char... chars) {
        Intrinsics.checkNotNullParameter($this$trimStart, "<this>");
        Intrinsics.checkNotNullParameter(chars, "chars");
        CharSequence charSequence = $this$trimStart;
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (!ArraysKt.contains(chars, charSequence.charAt(i))) {
                return charSequence.subSequence(i, charSequence.length());
            }
        }
        return HttpUrl.FRAGMENT_ENCODE_SET;
    }

    private static final String trimStart(String $this$trimStart) {
        Intrinsics.checkNotNullParameter($this$trimStart, "<this>");
        return StringsKt.trimStart((CharSequence) $this$trimStart).toString();
    }

    public static final String trimStart(String $this$trimStart, Function1<? super Character, Boolean> predicate) {
        CharSequence charSequence;
        Intrinsics.checkNotNullParameter($this$trimStart, "<this>");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        CharSequence charSequence2 = $this$trimStart;
        int i = 0;
        int length = charSequence2.length();
        while (true) {
            if (i >= length) {
                charSequence = HttpUrl.FRAGMENT_ENCODE_SET;
                break;
            } else if (!predicate.invoke(Character.valueOf(charSequence2.charAt(i))).booleanValue()) {
                charSequence = charSequence2.subSequence(i, charSequence2.length());
                break;
            } else {
                i++;
            }
        }
        return charSequence.toString();
    }

    public static final String trimStart(String $this$trimStart, char... chars) {
        CharSequence charSequence;
        Intrinsics.checkNotNullParameter($this$trimStart, "<this>");
        Intrinsics.checkNotNullParameter(chars, "chars");
        CharSequence charSequence2 = $this$trimStart;
        int i = 0;
        int length = charSequence2.length();
        while (true) {
            if (i >= length) {
                charSequence = HttpUrl.FRAGMENT_ENCODE_SET;
                break;
            } else if (!ArraysKt.contains(chars, charSequence2.charAt(i))) {
                charSequence = charSequence2.subSequence(i, charSequence2.length());
                break;
            } else {
                i++;
            }
        }
        return charSequence.toString();
    }

    public static /* synthetic */ String commonSuffixWith$default(CharSequence charSequence, CharSequence charSequence2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        String commonSuffixWith = StringsKt.commonSuffixWith(charSequence, charSequence2, z);
        Log1F380D.a((Object) commonSuffixWith);
        return commonSuffixWith;
    }

    public static /* synthetic */ String padEnd$default(String str, int i, char c, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            c = ' ';
        }
        String padEnd = StringsKt.padEnd(str, i, c);
        Log1F380D.a((Object) padEnd);
        return padEnd;
    }

    public static /* synthetic */ String padStart$default(String str, int i, char c, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            c = ' ';
        }
        String padStart = StringsKt.padStart(str, i, c);
        Log1F380D.a((Object) padStart);
        return padStart;
    }

    public static final String removeSurrounding(String $this$removeSurrounding, CharSequence delimiter) {
        Intrinsics.checkNotNullParameter($this$removeSurrounding, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        String removeSurrounding = StringsKt.removeSurrounding($this$removeSurrounding, delimiter, delimiter);
        Log1F380D.a((Object) removeSurrounding);
        return removeSurrounding;
    }

    public static /* synthetic */ String replaceAfter$default(String str, char c, String str2, String str3, int i, Object obj) {
        if ((i & 4) != 0) {
            str3 = str;
        }
        String replaceAfter = StringsKt.replaceAfter(str, c, str2, str3);
        Log1F380D.a((Object) replaceAfter);
        return replaceAfter;
    }

    public static /* synthetic */ String replaceAfter$default(String str, String str2, String str3, String str4, int i, Object obj) {
        if ((i & 4) != 0) {
            str4 = str;
        }
        String replaceAfter = StringsKt.replaceAfter(str, str2, str3, str4);
        Log1F380D.a((Object) replaceAfter);
        return replaceAfter;
    }

    public static /* synthetic */ String replaceAfterLast$default(String str, char c, String str2, String str3, int i, Object obj) {
        if ((i & 4) != 0) {
            str3 = str;
        }
        String replaceAfterLast = StringsKt.replaceAfterLast(str, c, str2, str3);
        Log1F380D.a((Object) replaceAfterLast);
        return replaceAfterLast;
    }

    public static /* synthetic */ String replaceAfterLast$default(String str, String str2, String str3, String str4, int i, Object obj) {
        if ((i & 4) != 0) {
            str4 = str;
        }
        String replaceAfterLast = StringsKt.replaceAfterLast(str, str2, str3, str4);
        Log1F380D.a((Object) replaceAfterLast);
        return replaceAfterLast;
    }

    public static /* synthetic */ String replaceBefore$default(String str, char c, String str2, String str3, int i, Object obj) {
        if ((i & 4) != 0) {
            str3 = str;
        }
        String replaceBefore = StringsKt.replaceBefore(str, c, str2, str3);
        Log1F380D.a((Object) replaceBefore);
        return replaceBefore;
    }

    public static /* synthetic */ String replaceBefore$default(String str, String str2, String str3, String str4, int i, Object obj) {
        if ((i & 4) != 0) {
            str4 = str;
        }
        String replaceBefore = StringsKt.replaceBefore(str, str2, str3, str4);
        Log1F380D.a((Object) replaceBefore);
        return replaceBefore;
    }

    public static /* synthetic */ String replaceBeforeLast$default(String str, char c, String str2, String str3, int i, Object obj) {
        if ((i & 4) != 0) {
            str3 = str;
        }
        String replaceBeforeLast = StringsKt.replaceBeforeLast(str, c, str2, str3);
        Log1F380D.a((Object) replaceBeforeLast);
        return replaceBeforeLast;
    }

    public static /* synthetic */ String replaceBeforeLast$default(String str, String str2, String str3, String str4, int i, Object obj) {
        if ((i & 4) != 0) {
            str4 = str;
        }
        String replaceBeforeLast = StringsKt.replaceBeforeLast(str, str2, str3, str4);
        Log1F380D.a((Object) replaceBeforeLast);
        return replaceBeforeLast;
    }

    public static final List<String> split(CharSequence $this$split, char[] delimiters, boolean ignoreCase, int limit) {
        Intrinsics.checkNotNullParameter($this$split, "<this>");
        Intrinsics.checkNotNullParameter(delimiters, "delimiters");
        if (delimiters.length == 1) {
            String valueOf = String.valueOf(delimiters[0]);
            Log1F380D.a((Object) valueOf);
            return split$StringsKt__StringsKt($this$split, valueOf, ignoreCase, limit);
        }
        Iterable<IntRange> asIterable = SequencesKt.asIterable(rangesDelimitedBy$StringsKt__StringsKt$default($this$split, delimiters, 0, ignoreCase, limit, 2, (Object) null));
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(asIterable, 10));
        for (IntRange substring : asIterable) {
            String substring2 = StringsKt.substring($this$split, substring);
            Log1F380D.a((Object) substring2);
            arrayList.add(substring2);
        }
        return (List) arrayList;
    }

    public static final List<String> split(CharSequence $this$split, String[] delimiters, boolean ignoreCase, int limit) {
        Intrinsics.checkNotNullParameter($this$split, "<this>");
        Intrinsics.checkNotNullParameter(delimiters, "delimiters");
        boolean z = true;
        if (delimiters.length == 1) {
            String str = delimiters[0];
            if (str.length() != 0) {
                z = false;
            }
            if (!z) {
                return split$StringsKt__StringsKt($this$split, str, ignoreCase, limit);
            }
        }
        Iterable<IntRange> asIterable = SequencesKt.asIterable(rangesDelimitedBy$StringsKt__StringsKt$default($this$split, delimiters, 0, ignoreCase, limit, 2, (Object) null));
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(asIterable, 10));
        for (IntRange substring : asIterable) {
            String substring2 = StringsKt.substring($this$split, substring);
            Log1F380D.a((Object) substring2);
            arrayList.add(substring2);
        }
        return (List) arrayList;
    }

    public static /* synthetic */ String substringAfter$default(String str, char c, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = str;
        }
        String substringAfter = StringsKt.substringAfter(str, c, str2);
        Log1F380D.a((Object) substringAfter);
        return substringAfter;
    }

    public static /* synthetic */ String substringAfter$default(String str, String str2, String str3, int i, Object obj) {
        if ((i & 2) != 0) {
            str3 = str;
        }
        String substringAfter = StringsKt.substringAfter(str, str2, str3);
        Log1F380D.a((Object) substringAfter);
        return substringAfter;
    }

    public static /* synthetic */ String substringAfterLast$default(String str, char c, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = str;
        }
        String substringAfterLast = StringsKt.substringAfterLast(str, c, str2);
        Log1F380D.a((Object) substringAfterLast);
        return substringAfterLast;
    }

    public static /* synthetic */ String substringAfterLast$default(String str, String str2, String str3, int i, Object obj) {
        if ((i & 2) != 0) {
            str3 = str;
        }
        String substringAfterLast = StringsKt.substringAfterLast(str, str2, str3);
        Log1F380D.a((Object) substringAfterLast);
        return substringAfterLast;
    }

    public static /* synthetic */ String substringBefore$default(String str, char c, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = str;
        }
        String substringBefore = StringsKt.substringBefore(str, c, str2);
        Log1F380D.a((Object) substringBefore);
        return substringBefore;
    }

    public static /* synthetic */ String substringBefore$default(String str, String str2, String str3, int i, Object obj) {
        if ((i & 2) != 0) {
            str3 = str;
        }
        String substringBefore = StringsKt.substringBefore(str, str2, str3);
        Log1F380D.a((Object) substringBefore);
        return substringBefore;
    }

    public static /* synthetic */ String substringBeforeLast$default(String str, char c, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = str;
        }
        String substringBeforeLast = StringsKt.substringBeforeLast(str, c, str2);
        Log1F380D.a((Object) substringBeforeLast);
        return substringBeforeLast;
    }

    public static /* synthetic */ String substringBeforeLast$default(String str, String str2, String str3, int i, Object obj) {
        if ((i & 2) != 0) {
            str3 = str;
        }
        String substringBeforeLast = StringsKt.substringBeforeLast(str, str2, str3);
        Log1F380D.a((Object) substringBeforeLast);
        return substringBeforeLast;
    }
}
