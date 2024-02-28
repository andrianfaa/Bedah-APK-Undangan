package androidx.core.graphics;

import android.graphics.Path;
import android.util.Log;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.location.LocationRequestCompat;
import java.util.ArrayList;

public class PathParser {
    private static final String LOGTAG = "PathParser";

    private static class ExtractFloatResult {
        int mEndPosition;
        boolean mEndWithNegOrDot;

        ExtractFloatResult() {
        }
    }

    public static class PathDataNode {
        public float[] mParams;
        public char mType;

        PathDataNode(char type, float[] params) {
            this.mType = type;
            this.mParams = params;
        }

        PathDataNode(PathDataNode n) {
            this.mType = n.mType;
            float[] fArr = n.mParams;
            this.mParams = PathParser.copyOfRange(fArr, 0, fArr.length);
        }

        private static void addCommand(Path path, float[] current, char previousCmd, char cmd, float[] val) {
            int i;
            int i2;
            float f;
            float f2;
            float f3;
            float f4;
            Path path2 = path;
            float[] fArr = val;
            float f5 = current[0];
            float f6 = current[1];
            float f7 = current[2];
            float f8 = current[3];
            float f9 = current[4];
            float f10 = current[5];
            switch (cmd) {
                case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_HEIGHT:
                case 'a':
                    i = 7;
                    break;
                case ConstraintLayout.LayoutParams.Table.GUIDELINE_USE_RTL:
                case 'c':
                    i = 6;
                    break;
                case 'H':
                case 'V':
                case LocationRequestCompat.QUALITY_LOW_POWER /*104*/:
                case 'v':
                    i = 1;
                    break;
                case 'L':
                case 'M':
                case 'T':
                case AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR:
                case AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY:
                case 't':
                    i = 2;
                    break;
                case 'Q':
                case 'S':
                case 'q':
                case 's':
                    i = 4;
                    break;
                case 'Z':
                case 'z':
                    path.close();
                    f5 = f9;
                    f6 = f10;
                    f7 = f9;
                    f8 = f10;
                    path2.moveTo(f5, f6);
                    i = 2;
                    break;
                default:
                    i = 2;
                    break;
            }
            char c = previousCmd;
            int i3 = 0;
            float f11 = f5;
            float f12 = f7;
            float f13 = f8;
            float f14 = f9;
            float f15 = f10;
            float f16 = f6;
            while (i3 < fArr.length) {
                switch (cmd) {
                    case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_HEIGHT:
                        i2 = i3;
                        char c2 = c;
                        float f17 = f11;
                        float f18 = f17;
                        drawArc(path, f17, f16, fArr[i2 + 5], fArr[i2 + 6], fArr[i2 + 0], fArr[i2 + 1], fArr[i2 + 2], fArr[i2 + 3] != 0.0f, fArr[i2 + 4] != 0.0f);
                        float f19 = fArr[i2 + 5];
                        float f20 = fArr[i2 + 6];
                        f11 = f19;
                        f16 = f20;
                        f12 = f19;
                        f13 = f20;
                        break;
                    case ConstraintLayout.LayoutParams.Table.GUIDELINE_USE_RTL:
                        float f21 = f16;
                        i2 = i3;
                        char c3 = c;
                        float f22 = f11;
                        path.cubicTo(fArr[i2 + 0], fArr[i2 + 1], fArr[i2 + 2], fArr[i2 + 3], fArr[i2 + 4], fArr[i2 + 5]);
                        f11 = fArr[i2 + 4];
                        f16 = fArr[i2 + 5];
                        f12 = fArr[i2 + 2];
                        f13 = fArr[i2 + 3];
                        break;
                    case 'H':
                        i2 = i3;
                        char c4 = c;
                        float f23 = f11;
                        path2.lineTo(fArr[i2 + 0], f16);
                        f11 = fArr[i2 + 0];
                        break;
                    case 'L':
                        float f24 = f16;
                        i2 = i3;
                        char c5 = c;
                        float f25 = f11;
                        path2.lineTo(fArr[i2 + 0], fArr[i2 + 1]);
                        f11 = fArr[i2 + 0];
                        f16 = fArr[i2 + 1];
                        break;
                    case 'M':
                        float f26 = f16;
                        i2 = i3;
                        char c6 = c;
                        float f27 = f11;
                        float f28 = fArr[i2 + 0];
                        float f29 = fArr[i2 + 1];
                        if (i2 <= 0) {
                            path2.moveTo(fArr[i2 + 0], fArr[i2 + 1]);
                            f11 = f28;
                            f16 = f29;
                            f14 = f28;
                            f15 = f29;
                            break;
                        } else {
                            path2.lineTo(fArr[i2 + 0], fArr[i2 + 1]);
                            f11 = f28;
                            f16 = f29;
                            break;
                        }
                    case 'Q':
                        float f30 = f16;
                        i2 = i3;
                        char c7 = c;
                        float f31 = f11;
                        path2.quadTo(fArr[i2 + 0], fArr[i2 + 1], fArr[i2 + 2], fArr[i2 + 3]);
                        f12 = fArr[i2 + 0];
                        f13 = fArr[i2 + 1];
                        f11 = fArr[i2 + 2];
                        f16 = fArr[i2 + 3];
                        break;
                    case 'S':
                        float f32 = f16;
                        i2 = i3;
                        char c8 = c;
                        float f33 = f11;
                        float f34 = f33;
                        float f35 = f32;
                        if (c8 == 'c' || c8 == 's' || c8 == 'C' || c8 == 'S') {
                            f2 = (f33 * 2.0f) - f12;
                            f = (f32 * 2.0f) - f13;
                        } else {
                            f2 = f34;
                            f = f35;
                        }
                        path.cubicTo(f2, f, fArr[i2 + 0], fArr[i2 + 1], fArr[i2 + 2], fArr[i2 + 3]);
                        f12 = fArr[i2 + 0];
                        f13 = fArr[i2 + 1];
                        f11 = fArr[i2 + 2];
                        f16 = fArr[i2 + 3];
                        break;
                    case 'T':
                        float f36 = f16;
                        i2 = i3;
                        char c9 = c;
                        float f37 = f11;
                        float f38 = f37;
                        float f39 = f36;
                        if (c9 == 'q' || c9 == 't' || c9 == 'Q' || c9 == 'T') {
                            f38 = (f37 * 2.0f) - f12;
                            f39 = (f36 * 2.0f) - f13;
                        }
                        path2.quadTo(f38, f39, fArr[i2 + 0], fArr[i2 + 1]);
                        f12 = f38;
                        f13 = f39;
                        f11 = fArr[i2 + 0];
                        f16 = fArr[i2 + 1];
                        char c10 = c9;
                        break;
                    case 'V':
                        float f40 = f16;
                        i2 = i3;
                        char c11 = c;
                        float f41 = f11;
                        path2 = path;
                        path2.lineTo(f41, fArr[i2 + 0]);
                        f16 = fArr[i2 + 0];
                        f11 = f41;
                        char c12 = c11;
                        break;
                    case 'a':
                        float f42 = f16;
                        i2 = i3;
                        drawArc(path, f11, f42, fArr[i3 + 5] + f11, fArr[i3 + 6] + f42, fArr[i3 + 0], fArr[i3 + 1], fArr[i3 + 2], fArr[i3 + 3] != 0.0f, fArr[i3 + 4] != 0.0f);
                        f11 += fArr[i2 + 5];
                        f16 = f42 + fArr[i2 + 6];
                        path2 = path;
                        f12 = f11;
                        f13 = f16;
                        char c13 = c;
                        break;
                    case 'c':
                        float f43 = f16;
                        path.rCubicTo(fArr[i3 + 0], fArr[i3 + 1], fArr[i3 + 2], fArr[i3 + 3], fArr[i3 + 4], fArr[i3 + 5]);
                        float f44 = fArr[i3 + 2] + f11;
                        float f45 = f43 + fArr[i3 + 3];
                        f11 += fArr[i3 + 4];
                        f12 = f44;
                        f13 = f45;
                        i2 = i3;
                        char c14 = c;
                        f16 = fArr[i3 + 5] + f43;
                        break;
                    case LocationRequestCompat.QUALITY_LOW_POWER /*104*/:
                        float f46 = f16;
                        path2.rLineTo(fArr[i3 + 0], 0.0f);
                        f11 += fArr[i3 + 0];
                        i2 = i3;
                        char c15 = c;
                        break;
                    case AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR:
                        path2.rLineTo(fArr[i3 + 0], fArr[i3 + 1]);
                        f11 += fArr[i3 + 0];
                        f16 += fArr[i3 + 1];
                        i2 = i3;
                        char c16 = c;
                        break;
                    case AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY:
                        f11 += fArr[i3 + 0];
                        f16 += fArr[i3 + 1];
                        if (i3 <= 0) {
                            path2.rMoveTo(fArr[i3 + 0], fArr[i3 + 1]);
                            f14 = f11;
                            f15 = f16;
                            i2 = i3;
                            char c17 = c;
                            break;
                        } else {
                            path2.rLineTo(fArr[i3 + 0], fArr[i3 + 1]);
                            i2 = i3;
                            char c18 = c;
                            break;
                        }
                    case 'q':
                        float f47 = f16;
                        path2.rQuadTo(fArr[i3 + 0], fArr[i3 + 1], fArr[i3 + 2], fArr[i3 + 3]);
                        float f48 = fArr[i3 + 0] + f11;
                        float f49 = f47 + fArr[i3 + 1];
                        f11 += fArr[i3 + 2];
                        f12 = f48;
                        f13 = f49;
                        i2 = i3;
                        char c19 = c;
                        f16 = fArr[i3 + 3] + f47;
                        break;
                    case 's':
                        if (c == 'c' || c == 's' || c == 'C' || c == 'S') {
                            f4 = f11 - f12;
                            f3 = f16 - f13;
                        } else {
                            f4 = 0.0f;
                            f3 = 0.0f;
                        }
                        float f50 = f16;
                        path.rCubicTo(f4, f3, fArr[i3 + 0], fArr[i3 + 1], fArr[i3 + 2], fArr[i3 + 3]);
                        float f51 = fArr[i3 + 0] + f11;
                        float f52 = f50 + fArr[i3 + 1];
                        f11 += fArr[i3 + 2];
                        f12 = f51;
                        f13 = f52;
                        i2 = i3;
                        char c20 = c;
                        f16 = fArr[i3 + 3] + f50;
                        break;
                    case 't':
                        float f53 = 0.0f;
                        float f54 = 0.0f;
                        if (c == 'q' || c == 't' || c == 'Q' || c == 'T') {
                            f53 = f11 - f12;
                            f54 = f16 - f13;
                        }
                        path2.rQuadTo(f53, f54, fArr[i3 + 0], fArr[i3 + 1]);
                        float f55 = f11 + f53;
                        float f56 = f16 + f54;
                        f11 += fArr[i3 + 0];
                        f16 += fArr[i3 + 1];
                        f12 = f55;
                        f13 = f56;
                        i2 = i3;
                        char c21 = c;
                        break;
                    case 'v':
                        path2.rLineTo(0.0f, fArr[i3 + 0]);
                        f16 += fArr[i3 + 0];
                        i2 = i3;
                        char c22 = c;
                        break;
                    default:
                        float f57 = f16;
                        float f58 = f11;
                        i2 = i3;
                        char c23 = c;
                        break;
                }
                c = cmd;
                i3 = i2 + i;
            }
            current[0] = f11;
            current[1] = f16;
            current[2] = f12;
            current[3] = f13;
            current[4] = f14;
            current[5] = f15;
        }

        private static void arcToBezier(Path p, double cx, double cy, double a, double b, double e1x, double e1y, double theta, double start, double sweep) {
            double d = a;
            int ceil = (int) Math.ceil(Math.abs((sweep * 4.0d) / 3.141592653589793d));
            double d2 = start;
            double cos = Math.cos(theta);
            double sin = Math.sin(theta);
            double cos2 = Math.cos(d2);
            double sin2 = Math.sin(d2);
            double d3 = ((-d) * sin * sin2) + (b * cos * cos2);
            double d4 = sweep / ((double) ceil);
            double d5 = d2;
            int i = 0;
            double d6 = e1x;
            double d7 = (((-d) * cos) * sin2) - ((b * sin) * cos2);
            double d8 = e1y;
            while (i < ceil) {
                double d9 = d5 + d4;
                double sin3 = Math.sin(d9);
                double cos3 = Math.cos(d9);
                double d10 = d4;
                double d11 = (cx + ((d * cos) * cos3)) - ((b * sin) * sin3);
                double d12 = cos2;
                double d13 = sin2;
                double d14 = (((-d) * cos) * sin3) - ((b * sin) * cos3);
                double e1x2 = cy + (d * sin * cos3) + (b * cos * sin3);
                double e1x3 = ((-d) * sin * sin3) + (b * cos * cos3);
                double tan = Math.tan((d9 - d5) / 2.0d);
                double sin4 = (Math.sin(d9 - d5) * (Math.sqrt(((tan * 3.0d) * tan) + 4.0d) - 1.0d)) / 3.0d;
                double d15 = d6 + (sin4 * d7);
                int i2 = ceil;
                double e1y2 = d6;
                double d16 = d8 + (sin4 * d3);
                double d17 = d11 - (sin4 * d14);
                double d18 = e1x2;
                p.rLineTo(0.0f, 0.0f);
                double d19 = d15;
                double d20 = d16;
                double d21 = d17;
                p.cubicTo((float) d15, (float) d16, (float) d17, (float) (d18 - (sin4 * e1x3)), (float) d11, (float) d18);
                d5 = d9;
                d6 = d11;
                d8 = d18;
                d7 = d14;
                d3 = e1x3;
                i++;
                ceil = i2;
                sin2 = d13;
                d4 = d10;
                cos2 = d12;
                cos = cos;
                sin = sin;
                d = a;
            }
        }

        private static void drawArc(Path p, float x0, float y0, float x1, float y1, float a, float b, float theta, boolean isMoreThanHalf, boolean isPositiveArc) {
            double d;
            double d2;
            float f = x0;
            float f2 = y0;
            float f3 = x1;
            float f4 = y1;
            float f5 = a;
            float f6 = b;
            boolean z = isPositiveArc;
            double radians = Math.toRadians((double) theta);
            double cos = Math.cos(radians);
            double sin = Math.sin(radians);
            double d3 = ((((double) f) * cos) + (((double) f2) * sin)) / ((double) f5);
            double d4 = ((((double) (-f)) * sin) + (((double) f2) * cos)) / ((double) f6);
            double d5 = ((((double) f3) * cos) + (((double) f4) * sin)) / ((double) f5);
            double d6 = ((((double) (-f3)) * sin) + (((double) f4) * cos)) / ((double) f6);
            double d7 = d3 - d5;
            double d8 = d4 - d6;
            double d9 = (d3 + d5) / 2.0d;
            double d10 = (d4 + d6) / 2.0d;
            double d11 = (d7 * d7) + (d8 * d8);
            if (d11 == 0.0d) {
                Log.w(PathParser.LOGTAG, " Points are coincident");
                return;
            }
            double d12 = (1.0d / d11) - 0.25d;
            if (d12 < 0.0d) {
                Log.w(PathParser.LOGTAG, "Points are too far apart " + d11);
                float sqrt = (float) (Math.sqrt(d11) / 1.99999d);
                float f7 = sqrt;
                double d13 = d11;
                boolean z2 = z;
                drawArc(p, x0, y0, x1, y1, f5 * sqrt, f6 * sqrt, theta, isMoreThanHalf, isPositiveArc);
                return;
            }
            double d14 = d11;
            boolean z3 = z;
            double sqrt2 = Math.sqrt(d12);
            double d15 = sqrt2 * d7;
            double d16 = sqrt2 * d8;
            if (isMoreThanHalf == z3) {
                d2 = d9 - d16;
                d = d10 + d15;
            } else {
                d2 = d9 + d16;
                d = d10 - d15;
            }
            double d17 = sqrt2;
            double atan2 = Math.atan2(d4 - d, d3 - d2);
            double d18 = d15;
            double atan22 = Math.atan2(d6 - d, d5 - d2);
            double d19 = atan22 - atan2;
            if (z3 != (d19 >= 0.0d)) {
                d19 = d19 > 0.0d ? d19 - 6.283185307179586d : d19 + 6.283185307179586d;
            }
            double d20 = atan22;
            double d21 = d2 * ((double) f5);
            double d22 = ((double) f6) * d;
            double d23 = (d21 * sin) + (d22 * cos);
            double d24 = d23;
            arcToBezier(p, (d21 * cos) - (d22 * sin), d23, (double) f5, (double) f6, (double) f, (double) f2, radians, atan2, d19);
        }

        public static void nodesToPath(PathDataNode[] node, Path path) {
            float[] fArr = new float[6];
            char c = 'm';
            for (int i = 0; i < node.length; i++) {
                addCommand(path, fArr, c, node[i].mType, node[i].mParams);
                c = node[i].mType;
            }
        }

        public void interpolatePathDataNode(PathDataNode nodeFrom, PathDataNode nodeTo, float fraction) {
            this.mType = nodeFrom.mType;
            int i = 0;
            while (true) {
                float[] fArr = nodeFrom.mParams;
                if (i < fArr.length) {
                    this.mParams[i] = (fArr[i] * (1.0f - fraction)) + (nodeTo.mParams[i] * fraction);
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    private PathParser() {
    }

    private static void addNode(ArrayList<PathDataNode> arrayList, char cmd, float[] val) {
        arrayList.add(new PathDataNode(cmd, val));
    }

    public static boolean canMorph(PathDataNode[] nodesFrom, PathDataNode[] nodesTo) {
        if (nodesFrom == null || nodesTo == null || nodesFrom.length != nodesTo.length) {
            return false;
        }
        for (int i = 0; i < nodesFrom.length; i++) {
            if (nodesFrom[i].mType != nodesTo[i].mType || nodesFrom[i].mParams.length != nodesTo[i].mParams.length) {
                return false;
            }
        }
        return true;
    }

    static float[] copyOfRange(float[] original, int start, int end) {
        if (start <= end) {
            int length = original.length;
            if (start < 0 || start > length) {
                throw new ArrayIndexOutOfBoundsException();
            }
            int i = end - start;
            float[] fArr = new float[i];
            System.arraycopy(original, start, fArr, 0, Math.min(i, length - start));
            return fArr;
        }
        throw new IllegalArgumentException();
    }

    public static PathDataNode[] createNodesFromPathData(String pathData) {
        if (pathData == null) {
            return null;
        }
        int i = 0;
        int i2 = 1;
        ArrayList arrayList = new ArrayList();
        while (i2 < pathData.length()) {
            int nextStart = nextStart(pathData, i2);
            String trim = pathData.substring(i, nextStart).trim();
            if (trim.length() > 0) {
                addNode(arrayList, trim.charAt(0), getFloats(trim));
            }
            i = nextStart;
            i2 = nextStart + 1;
        }
        if (i2 - i == 1 && i < pathData.length()) {
            addNode(arrayList, pathData.charAt(i), new float[0]);
        }
        return (PathDataNode[]) arrayList.toArray(new PathDataNode[arrayList.size()]);
    }

    public static Path createPathFromPathData(String pathData) {
        Path path = new Path();
        PathDataNode[] createNodesFromPathData = createNodesFromPathData(pathData);
        if (createNodesFromPathData == null) {
            return null;
        }
        try {
            PathDataNode.nodesToPath(createNodesFromPathData, path);
            return path;
        } catch (RuntimeException e) {
            throw new RuntimeException("Error in parsing " + pathData, e);
        }
    }

    public static PathDataNode[] deepCopyNodes(PathDataNode[] source) {
        if (source == null) {
            return null;
        }
        PathDataNode[] pathDataNodeArr = new PathDataNode[source.length];
        for (int i = 0; i < source.length; i++) {
            pathDataNodeArr[i] = new PathDataNode(source[i]);
        }
        return pathDataNodeArr;
    }

    private static void extract(String s, int start, ExtractFloatResult result) {
        boolean z = false;
        result.mEndWithNegOrDot = false;
        boolean z2 = false;
        boolean z3 = false;
        for (int i = start; i < s.length(); i++) {
            boolean z4 = z3;
            z3 = false;
            switch (s.charAt(i)) {
                case ' ':
                case ',':
                    z = true;
                    break;
                case '-':
                    if (i != start && !z4) {
                        z = true;
                        result.mEndWithNegOrDot = true;
                        break;
                    }
                case '.':
                    if (z2) {
                        z = true;
                        result.mEndWithNegOrDot = true;
                        break;
                    } else {
                        z2 = true;
                        break;
                    }
                case 'E':
                case TypedValues.TYPE_TARGET:
                    z3 = true;
                    break;
            }
            if (z) {
                result.mEndPosition = i;
            }
        }
        result.mEndPosition = i;
    }

    private static float[] getFloats(String s) {
        if (s.charAt(0) == 'z' || s.charAt(0) == 'Z') {
            return new float[0];
        }
        try {
            float[] fArr = new float[s.length()];
            int i = 0;
            int i2 = 1;
            ExtractFloatResult extractFloatResult = new ExtractFloatResult();
            int length = s.length();
            while (i2 < length) {
                extract(s, i2, extractFloatResult);
                int i3 = extractFloatResult.mEndPosition;
                if (i2 < i3) {
                    fArr[i] = Float.parseFloat(s.substring(i2, i3));
                    i++;
                }
                i2 = extractFloatResult.mEndWithNegOrDot ? i3 : i3 + 1;
            }
            return copyOfRange(fArr, 0, i);
        } catch (NumberFormatException e) {
            throw new RuntimeException("error in parsing \"" + s + "\"", e);
        }
    }

    public static boolean interpolatePathDataNodes(PathDataNode[] target, PathDataNode[] from, PathDataNode[] to, float fraction) {
        if (target == null || from == null || to == null) {
            throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes cannot be null");
        } else if (target.length != from.length || from.length != to.length) {
            throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes must have the same length");
        } else if (!canMorph(from, to)) {
            return false;
        } else {
            for (int i = 0; i < target.length; i++) {
                target[i].interpolatePathDataNode(from[i], to[i], fraction);
            }
            return true;
        }
    }

    private static int nextStart(String s, int end) {
        while (end < s.length()) {
            char charAt = s.charAt(end);
            if (((charAt - 'A') * (charAt - 'Z') <= 0 || (charAt - 'a') * (charAt - 'z') <= 0) && charAt != 'e' && charAt != 'E') {
                return end;
            }
            end++;
        }
        return end;
    }

    public static void updateNodes(PathDataNode[] target, PathDataNode[] source) {
        for (int i = 0; i < source.length; i++) {
            target[i].mType = source[i].mType;
            for (int i2 = 0; i2 < source[i].mParams.length; i2++) {
                target[i].mParams[i2] = source[i].mParams[i2];
            }
        }
    }
}
