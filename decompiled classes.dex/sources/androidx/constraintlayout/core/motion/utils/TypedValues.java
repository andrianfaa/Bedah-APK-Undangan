package androidx.constraintlayout.core.motion.utils;

public interface TypedValues {
    public static final int BOOLEAN_MASK = 1;
    public static final int FLOAT_MASK = 4;
    public static final int INT_MASK = 2;
    public static final int STRING_MASK = 8;
    public static final String S_CUSTOM = "CUSTOM";
    public static final int TYPE_FRAME_POSITION = 100;
    public static final int TYPE_TARGET = 101;

    public interface AttributesType {
        public static final String[] KEY_WORDS = {"curveFit", "visibility", "alpha", "translationX", "translationY", "translationZ", "elevation", "rotationX", "rotationY", "rotationZ", "scaleX", "scaleY", "pivotX", "pivotY", "progress", "pathRotate", "easing", "CUSTOM", S_FRAME, S_TARGET, S_PIVOT_TARGET};
        public static final String NAME = "KeyAttributes";
        public static final String S_ALPHA = "alpha";
        public static final String S_CURVE_FIT = "curveFit";
        public static final String S_CUSTOM = "CUSTOM";
        public static final String S_EASING = "easing";
        public static final String S_ELEVATION = "elevation";
        public static final String S_FRAME = "frame";
        public static final String S_PATH_ROTATE = "pathRotate";
        public static final String S_PIVOT_TARGET = "pivotTarget";
        public static final String S_PIVOT_X = "pivotX";
        public static final String S_PIVOT_Y = "pivotY";
        public static final String S_PROGRESS = "progress";
        public static final String S_ROTATION_X = "rotationX";
        public static final String S_ROTATION_Y = "rotationY";
        public static final String S_ROTATION_Z = "rotationZ";
        public static final String S_SCALE_X = "scaleX";
        public static final String S_SCALE_Y = "scaleY";
        public static final String S_TARGET = "target";
        public static final String S_TRANSLATION_X = "translationX";
        public static final String S_TRANSLATION_Y = "translationY";
        public static final String S_TRANSLATION_Z = "translationZ";
        public static final String S_VISIBILITY = "visibility";
        public static final int TYPE_ALPHA = 303;
        public static final int TYPE_CURVE_FIT = 301;
        public static final int TYPE_EASING = 317;
        public static final int TYPE_ELEVATION = 307;
        public static final int TYPE_PATH_ROTATE = 316;
        public static final int TYPE_PIVOT_TARGET = 318;
        public static final int TYPE_PIVOT_X = 313;
        public static final int TYPE_PIVOT_Y = 314;
        public static final int TYPE_PROGRESS = 315;
        public static final int TYPE_ROTATION_X = 308;
        public static final int TYPE_ROTATION_Y = 309;
        public static final int TYPE_ROTATION_Z = 310;
        public static final int TYPE_SCALE_X = 311;
        public static final int TYPE_SCALE_Y = 312;
        public static final int TYPE_TRANSLATION_X = 304;
        public static final int TYPE_TRANSLATION_Y = 305;
        public static final int TYPE_TRANSLATION_Z = 306;
        public static final int TYPE_VISIBILITY = 302;

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static int getId(java.lang.String r2) {
            /*
                int r0 = r2.hashCode()
                r1 = -1
                switch(r0) {
                    case -1310311125: goto L_0x00e8;
                    case -1249320806: goto L_0x00dd;
                    case -1249320805: goto L_0x00d1;
                    case -1249320804: goto L_0x00c5;
                    case -1225497657: goto L_0x00ba;
                    case -1225497656: goto L_0x00af;
                    case -1225497655: goto L_0x00a4;
                    case -1001078227: goto L_0x0099;
                    case -987906986: goto L_0x008e;
                    case -987906985: goto L_0x0082;
                    case -908189618: goto L_0x0075;
                    case -908189617: goto L_0x0068;
                    case -880905839: goto L_0x005b;
                    case -4379043: goto L_0x0050;
                    case 92909918: goto L_0x0045;
                    case 97692013: goto L_0x0039;
                    case 579057826: goto L_0x002e;
                    case 803192288: goto L_0x0022;
                    case 1167159411: goto L_0x0016;
                    case 1941332754: goto L_0x000a;
                    default: goto L_0x0008;
                }
            L_0x0008:
                goto L_0x00f3
            L_0x000a:
                java.lang.String r0 = "visibility"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 1
                goto L_0x00f4
            L_0x0016:
                java.lang.String r0 = "pivotTarget"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 19
                goto L_0x00f4
            L_0x0022:
                java.lang.String r0 = "pathRotate"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 15
                goto L_0x00f4
            L_0x002e:
                java.lang.String r0 = "curveFit"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 0
                goto L_0x00f4
            L_0x0039:
                java.lang.String r0 = "frame"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 17
                goto L_0x00f4
            L_0x0045:
                java.lang.String r0 = "alpha"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 2
                goto L_0x00f4
            L_0x0050:
                java.lang.String r0 = "elevation"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 6
                goto L_0x00f4
            L_0x005b:
                java.lang.String r0 = "target"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 18
                goto L_0x00f4
            L_0x0068:
                java.lang.String r0 = "scaleY"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 11
                goto L_0x00f4
            L_0x0075:
                java.lang.String r0 = "scaleX"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 10
                goto L_0x00f4
            L_0x0082:
                java.lang.String r0 = "pivotY"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 13
                goto L_0x00f4
            L_0x008e:
                java.lang.String r0 = "pivotX"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 12
                goto L_0x00f4
            L_0x0099:
                java.lang.String r0 = "progress"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 14
                goto L_0x00f4
            L_0x00a4:
                java.lang.String r0 = "translationZ"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 5
                goto L_0x00f4
            L_0x00af:
                java.lang.String r0 = "translationY"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 4
                goto L_0x00f4
            L_0x00ba:
                java.lang.String r0 = "translationX"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 3
                goto L_0x00f4
            L_0x00c5:
                java.lang.String r0 = "rotationZ"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 9
                goto L_0x00f4
            L_0x00d1:
                java.lang.String r0 = "rotationY"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 8
                goto L_0x00f4
            L_0x00dd:
                java.lang.String r0 = "rotationX"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 7
                goto L_0x00f4
            L_0x00e8:
                java.lang.String r0 = "easing"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 16
                goto L_0x00f4
            L_0x00f3:
                r0 = r1
            L_0x00f4:
                switch(r0) {
                    case 0: goto L_0x0131;
                    case 1: goto L_0x012e;
                    case 2: goto L_0x012b;
                    case 3: goto L_0x0128;
                    case 4: goto L_0x0125;
                    case 5: goto L_0x0122;
                    case 6: goto L_0x011f;
                    case 7: goto L_0x011c;
                    case 8: goto L_0x0119;
                    case 9: goto L_0x0116;
                    case 10: goto L_0x0113;
                    case 11: goto L_0x0110;
                    case 12: goto L_0x010d;
                    case 13: goto L_0x010a;
                    case 14: goto L_0x0107;
                    case 15: goto L_0x0104;
                    case 16: goto L_0x0101;
                    case 17: goto L_0x00fe;
                    case 18: goto L_0x00fb;
                    case 19: goto L_0x00f8;
                    default: goto L_0x00f7;
                }
            L_0x00f7:
                return r1
            L_0x00f8:
                r0 = 318(0x13e, float:4.46E-43)
                return r0
            L_0x00fb:
                r0 = 101(0x65, float:1.42E-43)
                return r0
            L_0x00fe:
                r0 = 100
                return r0
            L_0x0101:
                r0 = 317(0x13d, float:4.44E-43)
                return r0
            L_0x0104:
                r0 = 316(0x13c, float:4.43E-43)
                return r0
            L_0x0107:
                r0 = 315(0x13b, float:4.41E-43)
                return r0
            L_0x010a:
                r0 = 314(0x13a, float:4.4E-43)
                return r0
            L_0x010d:
                r0 = 313(0x139, float:4.39E-43)
                return r0
            L_0x0110:
                r0 = 312(0x138, float:4.37E-43)
                return r0
            L_0x0113:
                r0 = 311(0x137, float:4.36E-43)
                return r0
            L_0x0116:
                r0 = 310(0x136, float:4.34E-43)
                return r0
            L_0x0119:
                r0 = 309(0x135, float:4.33E-43)
                return r0
            L_0x011c:
                r0 = 308(0x134, float:4.32E-43)
                return r0
            L_0x011f:
                r0 = 307(0x133, float:4.3E-43)
                return r0
            L_0x0122:
                r0 = 306(0x132, float:4.29E-43)
                return r0
            L_0x0125:
                r0 = 305(0x131, float:4.27E-43)
                return r0
            L_0x0128:
                r0 = 304(0x130, float:4.26E-43)
                return r0
            L_0x012b:
                r0 = 303(0x12f, float:4.25E-43)
                return r0
            L_0x012e:
                r0 = 302(0x12e, float:4.23E-43)
                return r0
            L_0x0131:
                r0 = 301(0x12d, float:4.22E-43)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.motion.utils.TypedValues.AttributesType.getId(java.lang.String):int");
        }

        static int getType(int name) {
            switch (name) {
                case 100:
                case 301:
                case 302:
                    return 2;
                case TypedValues.TYPE_TARGET /*101*/:
                case TYPE_EASING /*317*/:
                case TYPE_PIVOT_TARGET /*318*/:
                    return 8;
                case 303:
                case 304:
                case 305:
                case 306:
                case 307:
                case 308:
                case 309:
                case 310:
                case 311:
                case 312:
                case 313:
                case 314:
                case 315:
                case TYPE_PATH_ROTATE /*316*/:
                    return 4;
                default:
                    return -1;
            }
        }
    }

    public interface Custom {
        public static final String[] KEY_WORDS = {S_FLOAT, S_COLOR, S_STRING, S_BOOLEAN, S_DIMENSION, S_REFERENCE};
        public static final String NAME = "Custom";
        public static final String S_BOOLEAN = "boolean";
        public static final String S_COLOR = "color";
        public static final String S_DIMENSION = "dimension";
        public static final String S_FLOAT = "float";
        public static final String S_INT = "integer";
        public static final String S_REFERENCE = "refrence";
        public static final String S_STRING = "string";
        public static final int TYPE_BOOLEAN = 904;
        public static final int TYPE_COLOR = 902;
        public static final int TYPE_DIMENSION = 905;
        public static final int TYPE_FLOAT = 901;
        public static final int TYPE_INT = 900;
        public static final int TYPE_REFERENCE = 906;
        public static final int TYPE_STRING = 903;

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static int getId(java.lang.String r2) {
            /*
                int r0 = r2.hashCode()
                r1 = -1
                switch(r0) {
                    case -1095013018: goto L_0x0046;
                    case -891985903: goto L_0x003b;
                    case -710953590: goto L_0x0031;
                    case 64711720: goto L_0x0027;
                    case 94842723: goto L_0x001d;
                    case 97526364: goto L_0x0013;
                    case 1958052158: goto L_0x0009;
                    default: goto L_0x0008;
                }
            L_0x0008:
                goto L_0x0050
            L_0x0009:
                java.lang.String r0 = "integer"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 0
                goto L_0x0051
            L_0x0013:
                java.lang.String r0 = "float"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 1
                goto L_0x0051
            L_0x001d:
                java.lang.String r0 = "color"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 2
                goto L_0x0051
            L_0x0027:
                java.lang.String r0 = "boolean"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 4
                goto L_0x0051
            L_0x0031:
                java.lang.String r0 = "refrence"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 6
                goto L_0x0051
            L_0x003b:
                java.lang.String r0 = "string"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 3
                goto L_0x0051
            L_0x0046:
                java.lang.String r0 = "dimension"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 5
                goto L_0x0051
            L_0x0050:
                r0 = r1
            L_0x0051:
                switch(r0) {
                    case 0: goto L_0x0067;
                    case 1: goto L_0x0064;
                    case 2: goto L_0x0061;
                    case 3: goto L_0x005e;
                    case 4: goto L_0x005b;
                    case 5: goto L_0x0058;
                    case 6: goto L_0x0055;
                    default: goto L_0x0054;
                }
            L_0x0054:
                return r1
            L_0x0055:
                r0 = 906(0x38a, float:1.27E-42)
                return r0
            L_0x0058:
                r0 = 905(0x389, float:1.268E-42)
                return r0
            L_0x005b:
                r0 = 904(0x388, float:1.267E-42)
                return r0
            L_0x005e:
                r0 = 903(0x387, float:1.265E-42)
                return r0
            L_0x0061:
                r0 = 902(0x386, float:1.264E-42)
                return r0
            L_0x0064:
                r0 = 901(0x385, float:1.263E-42)
                return r0
            L_0x0067:
                r0 = 900(0x384, float:1.261E-42)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.motion.utils.TypedValues.Custom.getId(java.lang.String):int");
        }
    }

    public interface CycleType {
        public static final String[] KEY_WORDS = {"curveFit", "visibility", "alpha", "translationX", "translationY", "translationZ", "elevation", "rotationX", "rotationY", "rotationZ", "scaleX", "scaleY", "pivotX", "pivotY", "progress", "pathRotate", "easing", "waveShape", S_CUSTOM_WAVE_SHAPE, S_WAVE_PERIOD, S_WAVE_OFFSET, S_WAVE_PHASE};
        public static final String NAME = "KeyCycle";
        public static final String S_ALPHA = "alpha";
        public static final String S_CURVE_FIT = "curveFit";
        public static final String S_CUSTOM_WAVE_SHAPE = "customWave";
        public static final String S_EASING = "easing";
        public static final String S_ELEVATION = "elevation";
        public static final String S_PATH_ROTATE = "pathRotate";
        public static final String S_PIVOT_X = "pivotX";
        public static final String S_PIVOT_Y = "pivotY";
        public static final String S_PROGRESS = "progress";
        public static final String S_ROTATION_X = "rotationX";
        public static final String S_ROTATION_Y = "rotationY";
        public static final String S_ROTATION_Z = "rotationZ";
        public static final String S_SCALE_X = "scaleX";
        public static final String S_SCALE_Y = "scaleY";
        public static final String S_TRANSLATION_X = "translationX";
        public static final String S_TRANSLATION_Y = "translationY";
        public static final String S_TRANSLATION_Z = "translationZ";
        public static final String S_VISIBILITY = "visibility";
        public static final String S_WAVE_OFFSET = "offset";
        public static final String S_WAVE_PERIOD = "period";
        public static final String S_WAVE_PHASE = "phase";
        public static final String S_WAVE_SHAPE = "waveShape";
        public static final int TYPE_ALPHA = 403;
        public static final int TYPE_CURVE_FIT = 401;
        public static final int TYPE_CUSTOM_WAVE_SHAPE = 422;
        public static final int TYPE_EASING = 420;
        public static final int TYPE_ELEVATION = 307;
        public static final int TYPE_PATH_ROTATE = 416;
        public static final int TYPE_PIVOT_X = 313;
        public static final int TYPE_PIVOT_Y = 314;
        public static final int TYPE_PROGRESS = 315;
        public static final int TYPE_ROTATION_X = 308;
        public static final int TYPE_ROTATION_Y = 309;
        public static final int TYPE_ROTATION_Z = 310;
        public static final int TYPE_SCALE_X = 311;
        public static final int TYPE_SCALE_Y = 312;
        public static final int TYPE_TRANSLATION_X = 304;
        public static final int TYPE_TRANSLATION_Y = 305;
        public static final int TYPE_TRANSLATION_Z = 306;
        public static final int TYPE_VISIBILITY = 402;
        public static final int TYPE_WAVE_OFFSET = 424;
        public static final int TYPE_WAVE_PERIOD = 423;
        public static final int TYPE_WAVE_PHASE = 425;
        public static final int TYPE_WAVE_SHAPE = 421;

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static int getId(java.lang.String r2) {
            /*
                int r0 = r2.hashCode()
                r1 = -1
                switch(r0) {
                    case -1310311125: goto L_0x00b6;
                    case -1249320806: goto L_0x00ab;
                    case -1249320805: goto L_0x00a0;
                    case -1249320804: goto L_0x0094;
                    case -1225497657: goto L_0x0089;
                    case -1225497656: goto L_0x007e;
                    case -1225497655: goto L_0x0073;
                    case -1001078227: goto L_0x0068;
                    case -987906986: goto L_0x005d;
                    case -987906985: goto L_0x0052;
                    case -908189618: goto L_0x0045;
                    case -908189617: goto L_0x0038;
                    case 92909918: goto L_0x002d;
                    case 579057826: goto L_0x0022;
                    case 803192288: goto L_0x0016;
                    case 1941332754: goto L_0x000a;
                    default: goto L_0x0008;
                }
            L_0x0008:
                goto L_0x00c1
            L_0x000a:
                java.lang.String r0 = "visibility"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 1
                goto L_0x00c2
            L_0x0016:
                java.lang.String r0 = "pathRotate"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 14
                goto L_0x00c2
            L_0x0022:
                java.lang.String r0 = "curveFit"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 0
                goto L_0x00c2
            L_0x002d:
                java.lang.String r0 = "alpha"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 2
                goto L_0x00c2
            L_0x0038:
                java.lang.String r0 = "scaleY"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 10
                goto L_0x00c2
            L_0x0045:
                java.lang.String r0 = "scaleX"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 9
                goto L_0x00c2
            L_0x0052:
                java.lang.String r0 = "pivotY"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 12
                goto L_0x00c2
            L_0x005d:
                java.lang.String r0 = "pivotX"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 11
                goto L_0x00c2
            L_0x0068:
                java.lang.String r0 = "progress"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 13
                goto L_0x00c2
            L_0x0073:
                java.lang.String r0 = "translationZ"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 5
                goto L_0x00c2
            L_0x007e:
                java.lang.String r0 = "translationY"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 4
                goto L_0x00c2
            L_0x0089:
                java.lang.String r0 = "translationX"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 3
                goto L_0x00c2
            L_0x0094:
                java.lang.String r0 = "rotationZ"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 8
                goto L_0x00c2
            L_0x00a0:
                java.lang.String r0 = "rotationY"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 7
                goto L_0x00c2
            L_0x00ab:
                java.lang.String r0 = "rotationX"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 6
                goto L_0x00c2
            L_0x00b6:
                java.lang.String r0 = "easing"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 15
                goto L_0x00c2
            L_0x00c1:
                r0 = r1
            L_0x00c2:
                switch(r0) {
                    case 0: goto L_0x00f3;
                    case 1: goto L_0x00f0;
                    case 2: goto L_0x00ed;
                    case 3: goto L_0x00ea;
                    case 4: goto L_0x00e7;
                    case 5: goto L_0x00e4;
                    case 6: goto L_0x00e1;
                    case 7: goto L_0x00de;
                    case 8: goto L_0x00db;
                    case 9: goto L_0x00d8;
                    case 10: goto L_0x00d5;
                    case 11: goto L_0x00d2;
                    case 12: goto L_0x00cf;
                    case 13: goto L_0x00cc;
                    case 14: goto L_0x00c9;
                    case 15: goto L_0x00c6;
                    default: goto L_0x00c5;
                }
            L_0x00c5:
                return r1
            L_0x00c6:
                r0 = 420(0x1a4, float:5.89E-43)
                return r0
            L_0x00c9:
                r0 = 416(0x1a0, float:5.83E-43)
                return r0
            L_0x00cc:
                r0 = 315(0x13b, float:4.41E-43)
                return r0
            L_0x00cf:
                r0 = 314(0x13a, float:4.4E-43)
                return r0
            L_0x00d2:
                r0 = 313(0x139, float:4.39E-43)
                return r0
            L_0x00d5:
                r0 = 312(0x138, float:4.37E-43)
                return r0
            L_0x00d8:
                r0 = 311(0x137, float:4.36E-43)
                return r0
            L_0x00db:
                r0 = 310(0x136, float:4.34E-43)
                return r0
            L_0x00de:
                r0 = 309(0x135, float:4.33E-43)
                return r0
            L_0x00e1:
                r0 = 308(0x134, float:4.32E-43)
                return r0
            L_0x00e4:
                r0 = 306(0x132, float:4.29E-43)
                return r0
            L_0x00e7:
                r0 = 305(0x131, float:4.27E-43)
                return r0
            L_0x00ea:
                r0 = 304(0x130, float:4.26E-43)
                return r0
            L_0x00ed:
                r0 = 403(0x193, float:5.65E-43)
                return r0
            L_0x00f0:
                r0 = 402(0x192, float:5.63E-43)
                return r0
            L_0x00f3:
                r0 = 401(0x191, float:5.62E-43)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.motion.utils.TypedValues.CycleType.getId(java.lang.String):int");
        }

        static int getType(int name) {
            switch (name) {
                case 100:
                case TYPE_CURVE_FIT /*401*/:
                case TYPE_VISIBILITY /*402*/:
                    return 2;
                case TypedValues.TYPE_TARGET /*101*/:
                case TYPE_EASING /*420*/:
                case 421:
                    return 8;
                case 304:
                case 305:
                case 306:
                case 307:
                case 308:
                case 309:
                case 310:
                case 311:
                case 312:
                case 313:
                case 314:
                case 315:
                case TYPE_ALPHA /*403*/:
                case TYPE_PATH_ROTATE /*416*/:
                case TYPE_WAVE_PERIOD /*423*/:
                case TYPE_WAVE_OFFSET /*424*/:
                case TYPE_WAVE_PHASE /*425*/:
                    return 4;
                default:
                    return -1;
            }
        }
    }

    public interface MotionScene {
        public static final String[] KEY_WORDS = {S_DEFAULT_DURATION, S_LAYOUT_DURING_TRANSITION};
        public static final String NAME = "MotionScene";
        public static final String S_DEFAULT_DURATION = "defaultDuration";
        public static final String S_LAYOUT_DURING_TRANSITION = "layoutDuringTransition";
        public static final int TYPE_DEFAULT_DURATION = 600;
        public static final int TYPE_LAYOUT_DURING_TRANSITION = 601;

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static int getId(java.lang.String r2) {
            /*
                int r0 = r2.hashCode()
                r1 = -1
                switch(r0) {
                    case 6076149: goto L_0x0013;
                    case 1028758976: goto L_0x0009;
                    default: goto L_0x0008;
                }
            L_0x0008:
                goto L_0x001d
            L_0x0009:
                java.lang.String r0 = "layoutDuringTransition"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 1
                goto L_0x001e
            L_0x0013:
                java.lang.String r0 = "defaultDuration"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 0
                goto L_0x001e
            L_0x001d:
                r0 = r1
            L_0x001e:
                switch(r0) {
                    case 0: goto L_0x0025;
                    case 1: goto L_0x0022;
                    default: goto L_0x0021;
                }
            L_0x0021:
                return r1
            L_0x0022:
                r0 = 601(0x259, float:8.42E-43)
                return r0
            L_0x0025:
                r0 = 600(0x258, float:8.41E-43)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.motion.utils.TypedValues.MotionScene.getId(java.lang.String):int");
        }

        static int getType(int name) {
            switch (name) {
                case 600:
                    return 2;
                case 601:
                    return 1;
                default:
                    return -1;
            }
        }
    }

    public interface MotionType {
        public static final String[] KEY_WORDS = {S_STAGGER, S_PATH_ROTATE, S_QUANTIZE_MOTION_PHASE, S_EASING, S_QUANTIZE_INTERPOLATOR, S_ANIMATE_RELATIVE_TO, S_ANIMATE_CIRCLEANGLE_TO, S_PATHMOTION_ARC, S_DRAW_PATH, S_POLAR_RELATIVETO, S_QUANTIZE_MOTIONSTEPS, S_QUANTIZE_INTERPOLATOR_TYPE, S_QUANTIZE_INTERPOLATOR_ID};
        public static final String NAME = "Motion";
        public static final String S_ANIMATE_CIRCLEANGLE_TO = "AnimateCircleAngleTo";
        public static final String S_ANIMATE_RELATIVE_TO = "AnimateRelativeTo";
        public static final String S_DRAW_PATH = "DrawPath";
        public static final String S_EASING = "TransitionEasing";
        public static final String S_PATHMOTION_ARC = "PathMotionArc";
        public static final String S_PATH_ROTATE = "PathRotate";
        public static final String S_POLAR_RELATIVETO = "PolarRelativeTo";
        public static final String S_QUANTIZE_INTERPOLATOR = "QuantizeInterpolator";
        public static final String S_QUANTIZE_INTERPOLATOR_ID = "QuantizeInterpolatorID";
        public static final String S_QUANTIZE_INTERPOLATOR_TYPE = "QuantizeInterpolatorType";
        public static final String S_QUANTIZE_MOTIONSTEPS = "QuantizeMotionSteps";
        public static final String S_QUANTIZE_MOTION_PHASE = "QuantizeMotionPhase";
        public static final String S_STAGGER = "Stagger";
        public static final int TYPE_ANIMATE_CIRCLEANGLE_TO = 606;
        public static final int TYPE_ANIMATE_RELATIVE_TO = 605;
        public static final int TYPE_DRAW_PATH = 608;
        public static final int TYPE_EASING = 603;
        public static final int TYPE_PATHMOTION_ARC = 607;
        public static final int TYPE_PATH_ROTATE = 601;
        public static final int TYPE_POLAR_RELATIVETO = 609;
        public static final int TYPE_QUANTIZE_INTERPOLATOR = 604;
        public static final int TYPE_QUANTIZE_INTERPOLATOR_ID = 612;
        public static final int TYPE_QUANTIZE_INTERPOLATOR_TYPE = 611;
        public static final int TYPE_QUANTIZE_MOTIONSTEPS = 610;
        public static final int TYPE_QUANTIZE_MOTION_PHASE = 602;
        public static final int TYPE_STAGGER = 600;

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static int getId(java.lang.String r2) {
            /*
                int r0 = r2.hashCode()
                r1 = -1
                switch(r0) {
                    case -2033446275: goto L_0x008a;
                    case -1532277420: goto L_0x0080;
                    case -1529145600: goto L_0x0075;
                    case -1498310144: goto L_0x006b;
                    case -1030753096: goto L_0x0061;
                    case -762370135: goto L_0x0056;
                    case -232872051: goto L_0x004c;
                    case 1138491429: goto L_0x0041;
                    case 1539234834: goto L_0x0036;
                    case 1583722451: goto L_0x002b;
                    case 1639368448: goto L_0x0020;
                    case 1900899336: goto L_0x0015;
                    case 2109694967: goto L_0x000a;
                    default: goto L_0x0008;
                }
            L_0x0008:
                goto L_0x0094
            L_0x000a:
                java.lang.String r0 = "PathMotionArc"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 7
                goto L_0x0095
            L_0x0015:
                java.lang.String r0 = "AnimateRelativeTo"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 5
                goto L_0x0095
            L_0x0020:
                java.lang.String r0 = "TransitionEasing"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 3
                goto L_0x0095
            L_0x002b:
                java.lang.String r0 = "QuantizeInterpolatorID"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 12
                goto L_0x0095
            L_0x0036:
                java.lang.String r0 = "QuantizeInterpolatorType"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 11
                goto L_0x0095
            L_0x0041:
                java.lang.String r0 = "PolarRelativeTo"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 9
                goto L_0x0095
            L_0x004c:
                java.lang.String r0 = "Stagger"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 0
                goto L_0x0095
            L_0x0056:
                java.lang.String r0 = "DrawPath"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 8
                goto L_0x0095
            L_0x0061:
                java.lang.String r0 = "QuantizeInterpolator"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 4
                goto L_0x0095
            L_0x006b:
                java.lang.String r0 = "PathRotate"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 1
                goto L_0x0095
            L_0x0075:
                java.lang.String r0 = "QuantizeMotionSteps"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 10
                goto L_0x0095
            L_0x0080:
                java.lang.String r0 = "QuantizeMotionPhase"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 2
                goto L_0x0095
            L_0x008a:
                java.lang.String r0 = "AnimateCircleAngleTo"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 6
                goto L_0x0095
            L_0x0094:
                r0 = r1
            L_0x0095:
                switch(r0) {
                    case 0: goto L_0x00bd;
                    case 1: goto L_0x00ba;
                    case 2: goto L_0x00b7;
                    case 3: goto L_0x00b4;
                    case 4: goto L_0x00b1;
                    case 5: goto L_0x00ae;
                    case 6: goto L_0x00ab;
                    case 7: goto L_0x00a8;
                    case 8: goto L_0x00a5;
                    case 9: goto L_0x00a2;
                    case 10: goto L_0x009f;
                    case 11: goto L_0x009c;
                    case 12: goto L_0x0099;
                    default: goto L_0x0098;
                }
            L_0x0098:
                return r1
            L_0x0099:
                r0 = 612(0x264, float:8.58E-43)
                return r0
            L_0x009c:
                r0 = 611(0x263, float:8.56E-43)
                return r0
            L_0x009f:
                r0 = 610(0x262, float:8.55E-43)
                return r0
            L_0x00a2:
                r0 = 609(0x261, float:8.53E-43)
                return r0
            L_0x00a5:
                r0 = 608(0x260, float:8.52E-43)
                return r0
            L_0x00a8:
                r0 = 607(0x25f, float:8.5E-43)
                return r0
            L_0x00ab:
                r0 = 606(0x25e, float:8.49E-43)
                return r0
            L_0x00ae:
                r0 = 605(0x25d, float:8.48E-43)
                return r0
            L_0x00b1:
                r0 = 604(0x25c, float:8.46E-43)
                return r0
            L_0x00b4:
                r0 = 603(0x25b, float:8.45E-43)
                return r0
            L_0x00b7:
                r0 = 602(0x25a, float:8.44E-43)
                return r0
            L_0x00ba:
                r0 = 601(0x259, float:8.42E-43)
                return r0
            L_0x00bd:
                r0 = 600(0x258, float:8.41E-43)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.motion.utils.TypedValues.MotionType.getId(java.lang.String):int");
        }
    }

    public interface OnSwipe {
        public static final String AUTOCOMPLETE_MODE = "autocompletemode";
        public static final String[] AUTOCOMPLETE_MODE_ENUM = {"continuousVelocity", "spring"};
        public static final String DRAG_DIRECTION = "dragdirection";
        public static final String DRAG_SCALE = "dragscale";
        public static final String DRAG_THRESHOLD = "dragthreshold";
        public static final String LIMIT_BOUNDS_TO = "limitboundsto";
        public static final String MAX_ACCELERATION = "maxacceleration";
        public static final String MAX_VELOCITY = "maxvelocity";
        public static final String MOVE_WHEN_SCROLLAT_TOP = "movewhenscrollattop";
        public static final String NESTED_SCROLL_FLAGS = "nestedscrollflags";
        public static final String[] NESTED_SCROLL_FLAGS_ENUM = {"none", "disablePostScroll", "disableScroll", "supportScrollUp"};
        public static final String ON_TOUCH_UP = "ontouchup";
        public static final String[] ON_TOUCH_UP_ENUM = {"autoComplete", "autoCompleteToStart", "autoCompleteToEnd", "stop", "decelerate", "decelerateAndComplete", "neverCompleteToStart", "neverCompleteToEnd"};
        public static final String ROTATION_CENTER_ID = "rotationcenterid";
        public static final String SPRINGS_TOP_THRESHOLD = "springstopthreshold";
        public static final String SPRING_BOUNDARY = "springboundary";
        public static final String[] SPRING_BOUNDARY_ENUM = {"overshoot", "bounceStart", "bounceEnd", "bounceBoth"};
        public static final String SPRING_DAMPING = "springdamping";
        public static final String SPRING_MASS = "springmass";
        public static final String SPRING_STIFFNESS = "springstiffness";
        public static final String TOUCH_ANCHOR_ID = "touchanchorid";
        public static final String TOUCH_ANCHOR_SIDE = "touchanchorside";
        public static final String TOUCH_REGION_ID = "touchregionid";
    }

    public interface PositionType {
        public static final String[] KEY_WORDS = {"transitionEasing", "drawPath", "percentWidth", "percentHeight", "sizePercent", "percentX", "percentY"};
        public static final String NAME = "KeyPosition";
        public static final String S_DRAWPATH = "drawPath";
        public static final String S_PERCENT_HEIGHT = "percentHeight";
        public static final String S_PERCENT_WIDTH = "percentWidth";
        public static final String S_PERCENT_X = "percentX";
        public static final String S_PERCENT_Y = "percentY";
        public static final String S_SIZE_PERCENT = "sizePercent";
        public static final String S_TRANSITION_EASING = "transitionEasing";
        public static final int TYPE_CURVE_FIT = 508;
        public static final int TYPE_DRAWPATH = 502;
        public static final int TYPE_PATH_MOTION_ARC = 509;
        public static final int TYPE_PERCENT_HEIGHT = 504;
        public static final int TYPE_PERCENT_WIDTH = 503;
        public static final int TYPE_PERCENT_X = 506;
        public static final int TYPE_PERCENT_Y = 507;
        public static final int TYPE_POSITION_TYPE = 510;
        public static final int TYPE_SIZE_PERCENT = 505;
        public static final int TYPE_TRANSITION_EASING = 501;

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static int getId(java.lang.String r2) {
            /*
                int r0 = r2.hashCode()
                r1 = -1
                switch(r0) {
                    case -1812823328: goto L_0x0046;
                    case -1127236479: goto L_0x003c;
                    case -1017587252: goto L_0x0032;
                    case -827014263: goto L_0x0028;
                    case -200259324: goto L_0x001d;
                    case 428090547: goto L_0x0013;
                    case 428090548: goto L_0x0009;
                    default: goto L_0x0008;
                }
            L_0x0008:
                goto L_0x0051
            L_0x0009:
                java.lang.String r0 = "percentY"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 6
                goto L_0x0052
            L_0x0013:
                java.lang.String r0 = "percentX"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 5
                goto L_0x0052
            L_0x001d:
                java.lang.String r0 = "sizePercent"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 4
                goto L_0x0052
            L_0x0028:
                java.lang.String r0 = "drawPath"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 1
                goto L_0x0052
            L_0x0032:
                java.lang.String r0 = "percentHeight"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 3
                goto L_0x0052
            L_0x003c:
                java.lang.String r0 = "percentWidth"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 2
                goto L_0x0052
            L_0x0046:
                java.lang.String r0 = "transitionEasing"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 0
                goto L_0x0052
            L_0x0051:
                r0 = r1
            L_0x0052:
                switch(r0) {
                    case 0: goto L_0x0068;
                    case 1: goto L_0x0065;
                    case 2: goto L_0x0062;
                    case 3: goto L_0x005f;
                    case 4: goto L_0x005c;
                    case 5: goto L_0x0059;
                    case 6: goto L_0x0056;
                    default: goto L_0x0055;
                }
            L_0x0055:
                return r1
            L_0x0056:
                r0 = 507(0x1fb, float:7.1E-43)
                return r0
            L_0x0059:
                r0 = 506(0x1fa, float:7.09E-43)
                return r0
            L_0x005c:
                r0 = 505(0x1f9, float:7.08E-43)
                return r0
            L_0x005f:
                r0 = 504(0x1f8, float:7.06E-43)
                return r0
            L_0x0062:
                r0 = 503(0x1f7, float:7.05E-43)
                return r0
            L_0x0065:
                r0 = 502(0x1f6, float:7.03E-43)
                return r0
            L_0x0068:
                r0 = 501(0x1f5, float:7.02E-43)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.motion.utils.TypedValues.PositionType.getId(java.lang.String):int");
        }

        static int getType(int name) {
            switch (name) {
                case 100:
                case TYPE_CURVE_FIT /*508*/:
                    return 2;
                case TypedValues.TYPE_TARGET /*101*/:
                case TYPE_TRANSITION_EASING /*501*/:
                case TYPE_DRAWPATH /*502*/:
                    return 8;
                case TYPE_PERCENT_WIDTH /*503*/:
                case TYPE_PERCENT_HEIGHT /*504*/:
                case TYPE_SIZE_PERCENT /*505*/:
                case TYPE_PERCENT_X /*506*/:
                case TYPE_PERCENT_Y /*507*/:
                    return 4;
                default:
                    return -1;
            }
        }
    }

    public interface TransitionType {
        public static final String[] KEY_WORDS = {S_DURATION, S_FROM, S_TO, S_PATH_MOTION_ARC, S_AUTO_TRANSITION, S_INTERPOLATOR, S_STAGGERED, S_FROM, S_TRANSITION_FLAGS};
        public static final String NAME = "Transitions";
        public static final String S_AUTO_TRANSITION = "autoTransition";
        public static final String S_DURATION = "duration";
        public static final String S_FROM = "from";
        public static final String S_INTERPOLATOR = "motionInterpolator";
        public static final String S_PATH_MOTION_ARC = "pathMotionArc";
        public static final String S_STAGGERED = "staggered";
        public static final String S_TO = "to";
        public static final String S_TRANSITION_FLAGS = "transitionFlags";
        public static final int TYPE_AUTO_TRANSITION = 704;
        public static final int TYPE_DURATION = 700;
        public static final int TYPE_FROM = 701;
        public static final int TYPE_INTERPOLATOR = 705;
        public static final int TYPE_PATH_MOTION_ARC = 509;
        public static final int TYPE_STAGGERED = 706;
        public static final int TYPE_TO = 702;
        public static final int TYPE_TRANSITION_FLAGS = 707;

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static int getId(java.lang.String r2) {
            /*
                int r0 = r2.hashCode()
                r1 = -1
                switch(r0) {
                    case -1996906958: goto L_0x0051;
                    case -1992012396: goto L_0x0047;
                    case -1357874275: goto L_0x003d;
                    case -1298065308: goto L_0x0033;
                    case 3707: goto L_0x0028;
                    case 3151786: goto L_0x001e;
                    case 1310733335: goto L_0x0014;
                    case 1839260940: goto L_0x0009;
                    default: goto L_0x0008;
                }
            L_0x0008:
                goto L_0x005c
            L_0x0009:
                java.lang.String r0 = "staggered"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 6
                goto L_0x005d
            L_0x0014:
                java.lang.String r0 = "pathMotionArc"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 3
                goto L_0x005d
            L_0x001e:
                java.lang.String r0 = "from"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 1
                goto L_0x005d
            L_0x0028:
                java.lang.String r0 = "to"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 2
                goto L_0x005d
            L_0x0033:
                java.lang.String r0 = "autoTransition"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 4
                goto L_0x005d
            L_0x003d:
                java.lang.String r0 = "motionInterpolator"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 5
                goto L_0x005d
            L_0x0047:
                java.lang.String r0 = "duration"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 0
                goto L_0x005d
            L_0x0051:
                java.lang.String r0 = "transitionFlags"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 7
                goto L_0x005d
            L_0x005c:
                r0 = r1
            L_0x005d:
                switch(r0) {
                    case 0: goto L_0x0076;
                    case 1: goto L_0x0073;
                    case 2: goto L_0x0070;
                    case 3: goto L_0x006d;
                    case 4: goto L_0x006a;
                    case 5: goto L_0x0067;
                    case 6: goto L_0x0064;
                    case 7: goto L_0x0061;
                    default: goto L_0x0060;
                }
            L_0x0060:
                return r1
            L_0x0061:
                r0 = 707(0x2c3, float:9.91E-43)
                return r0
            L_0x0064:
                r0 = 706(0x2c2, float:9.9E-43)
                return r0
            L_0x0067:
                r0 = 705(0x2c1, float:9.88E-43)
                return r0
            L_0x006a:
                r0 = 704(0x2c0, float:9.87E-43)
                return r0
            L_0x006d:
                r0 = 509(0x1fd, float:7.13E-43)
                return r0
            L_0x0070:
                r0 = 702(0x2be, float:9.84E-43)
                return r0
            L_0x0073:
                r0 = 701(0x2bd, float:9.82E-43)
                return r0
            L_0x0076:
                r0 = 700(0x2bc, float:9.81E-43)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.motion.utils.TypedValues.TransitionType.getId(java.lang.String):int");
        }

        static int getType(int name) {
            switch (name) {
                case 509:
                case TYPE_DURATION /*700*/:
                    return 2;
                case TYPE_FROM /*701*/:
                case TYPE_TO /*702*/:
                case TYPE_INTERPOLATOR /*705*/:
                case TYPE_TRANSITION_FLAGS /*707*/:
                    return 8;
                case TYPE_STAGGERED /*706*/:
                    return 4;
                default:
                    return -1;
            }
        }
    }

    public interface TriggerType {
        public static final String CROSS = "CROSS";
        public static final String[] KEY_WORDS = {"viewTransitionOnCross", "viewTransitionOnPositiveCross", "viewTransitionOnNegativeCross", "postLayout", "triggerSlack", "triggerCollisionView", "triggerCollisionId", "triggerID", "positiveCross", "negativeCross", "triggerReceiver", "CROSS"};
        public static final String NAME = "KeyTrigger";
        public static final String NEGATIVE_CROSS = "negativeCross";
        public static final String POSITIVE_CROSS = "positiveCross";
        public static final String POST_LAYOUT = "postLayout";
        public static final String TRIGGER_COLLISION_ID = "triggerCollisionId";
        public static final String TRIGGER_COLLISION_VIEW = "triggerCollisionView";
        public static final String TRIGGER_ID = "triggerID";
        public static final String TRIGGER_RECEIVER = "triggerReceiver";
        public static final String TRIGGER_SLACK = "triggerSlack";
        public static final int TYPE_CROSS = 312;
        public static final int TYPE_NEGATIVE_CROSS = 310;
        public static final int TYPE_POSITIVE_CROSS = 309;
        public static final int TYPE_POST_LAYOUT = 304;
        public static final int TYPE_TRIGGER_COLLISION_ID = 307;
        public static final int TYPE_TRIGGER_COLLISION_VIEW = 306;
        public static final int TYPE_TRIGGER_ID = 308;
        public static final int TYPE_TRIGGER_RECEIVER = 311;
        public static final int TYPE_TRIGGER_SLACK = 305;
        public static final int TYPE_VIEW_TRANSITION_ON_CROSS = 301;
        public static final int TYPE_VIEW_TRANSITION_ON_NEGATIVE_CROSS = 303;
        public static final int TYPE_VIEW_TRANSITION_ON_POSITIVE_CROSS = 302;
        public static final String VIEW_TRANSITION_ON_CROSS = "viewTransitionOnCross";
        public static final String VIEW_TRANSITION_ON_NEGATIVE_CROSS = "viewTransitionOnNegativeCross";
        public static final String VIEW_TRANSITION_ON_POSITIVE_CROSS = "viewTransitionOnPositiveCross";

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static int getId(java.lang.String r2) {
            /*
                int r0 = r2.hashCode()
                r1 = -1
                switch(r0) {
                    case -1594793529: goto L_0x0085;
                    case -966421266: goto L_0x007a;
                    case -786670827: goto L_0x006f;
                    case -648752941: goto L_0x0064;
                    case -638126837: goto L_0x0059;
                    case -76025313: goto L_0x004e;
                    case -9754574: goto L_0x0043;
                    case 64397344: goto L_0x0038;
                    case 364489912: goto L_0x002d;
                    case 1301930599: goto L_0x0022;
                    case 1401391082: goto L_0x0017;
                    case 1535404999: goto L_0x000a;
                    default: goto L_0x0008;
                }
            L_0x0008:
                goto L_0x0090
            L_0x000a:
                java.lang.String r0 = "triggerReceiver"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 10
                goto L_0x0091
            L_0x0017:
                java.lang.String r0 = "postLayout"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 3
                goto L_0x0091
            L_0x0022:
                java.lang.String r0 = "viewTransitionOnCross"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 0
                goto L_0x0091
            L_0x002d:
                java.lang.String r0 = "triggerSlack"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 4
                goto L_0x0091
            L_0x0038:
                java.lang.String r0 = "CROSS"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 11
                goto L_0x0091
            L_0x0043:
                java.lang.String r0 = "viewTransitionOnNegativeCross"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 2
                goto L_0x0091
            L_0x004e:
                java.lang.String r0 = "triggerCollisionView"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 5
                goto L_0x0091
            L_0x0059:
                java.lang.String r0 = "negativeCross"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 9
                goto L_0x0091
            L_0x0064:
                java.lang.String r0 = "triggerID"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 7
                goto L_0x0091
            L_0x006f:
                java.lang.String r0 = "triggerCollisionId"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 6
                goto L_0x0091
            L_0x007a:
                java.lang.String r0 = "viewTransitionOnPositiveCross"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 1
                goto L_0x0091
            L_0x0085:
                java.lang.String r0 = "positiveCross"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x0008
                r0 = 8
                goto L_0x0091
            L_0x0090:
                r0 = r1
            L_0x0091:
                switch(r0) {
                    case 0: goto L_0x00b6;
                    case 1: goto L_0x00b3;
                    case 2: goto L_0x00b0;
                    case 3: goto L_0x00ad;
                    case 4: goto L_0x00aa;
                    case 5: goto L_0x00a7;
                    case 6: goto L_0x00a4;
                    case 7: goto L_0x00a1;
                    case 8: goto L_0x009e;
                    case 9: goto L_0x009b;
                    case 10: goto L_0x0098;
                    case 11: goto L_0x0095;
                    default: goto L_0x0094;
                }
            L_0x0094:
                return r1
            L_0x0095:
                r0 = 312(0x138, float:4.37E-43)
                return r0
            L_0x0098:
                r0 = 311(0x137, float:4.36E-43)
                return r0
            L_0x009b:
                r0 = 310(0x136, float:4.34E-43)
                return r0
            L_0x009e:
                r0 = 309(0x135, float:4.33E-43)
                return r0
            L_0x00a1:
                r0 = 308(0x134, float:4.32E-43)
                return r0
            L_0x00a4:
                r0 = 307(0x133, float:4.3E-43)
                return r0
            L_0x00a7:
                r0 = 306(0x132, float:4.29E-43)
                return r0
            L_0x00aa:
                r0 = 305(0x131, float:4.27E-43)
                return r0
            L_0x00ad:
                r0 = 304(0x130, float:4.26E-43)
                return r0
            L_0x00b0:
                r0 = 303(0x12f, float:4.25E-43)
                return r0
            L_0x00b3:
                r0 = 302(0x12e, float:4.23E-43)
                return r0
            L_0x00b6:
                r0 = 301(0x12d, float:4.22E-43)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.motion.utils.TypedValues.TriggerType.getId(java.lang.String):int");
        }
    }

    int getId(String str);

    boolean setValue(int i, float f);

    boolean setValue(int i, int i2);

    boolean setValue(int i, String str);

    boolean setValue(int i, boolean z);
}
