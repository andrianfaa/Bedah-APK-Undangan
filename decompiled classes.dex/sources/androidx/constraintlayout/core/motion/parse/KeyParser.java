package androidx.constraintlayout.core.motion.parse;

import androidx.constraintlayout.core.motion.utils.TypedBundle;
import androidx.constraintlayout.core.parser.CLElement;
import androidx.constraintlayout.core.parser.CLKey;
import androidx.constraintlayout.core.parser.CLObject;
import androidx.constraintlayout.core.parser.CLParser;
import androidx.constraintlayout.core.parser.CLParsingException;

public class KeyParser {

    private interface DataType {
        int get(int i);
    }

    private interface Ids {
        int get(String str);
    }

    public static void main(String[] args) {
        parseAttributes("{frame:22,\ntarget:'widget1',\neasing:'easeIn',\ncurveFit:'spline',\nprogress:0.3,\nalpha:0.2,\nelevation:0.7,\nrotationZ:23,\nrotationX:25.0,\nrotationY:27.0,\npivotX:15,\npivotY:17,\npivotTarget:'32',\npathRotate:23,\nscaleX:0.5,\nscaleY:0.7,\ntranslationX:5,\ntranslationY:7,\ntranslationZ:11,\n}");
    }

    private static TypedBundle parse(String str, Ids table, DataType dtype) {
        TypedBundle typedBundle = new TypedBundle();
        try {
            CLObject parse = CLParser.parse(str);
            int size = parse.size();
            for (int i = 0; i < size; i++) {
                CLKey cLKey = (CLKey) parse.get(i);
                String content = cLKey.content();
                CLElement value = cLKey.getValue();
                int i2 = table.get(content);
                if (i2 != -1) {
                    switch (dtype.get(i2)) {
                        case 1:
                            typedBundle.add(i2, parse.getBoolean(i));
                            break;
                        case 2:
                            typedBundle.add(i2, value.getInt());
                            System.out.println("parse " + content + " INT_MASK > " + value.getInt());
                            break;
                        case 4:
                            typedBundle.add(i2, value.getFloat());
                            System.out.println("parse " + content + " FLOAT_MASK > " + value.getFloat());
                            break;
                        case 8:
                            typedBundle.add(i2, value.content());
                            System.out.println("parse " + content + " STRING_MASK > " + value.content());
                            break;
                    }
                } else {
                    System.err.println("unknown type " + content);
                }
            }
        } catch (CLParsingException e) {
            e.printStackTrace();
        }
        return typedBundle;
    }

    public static TypedBundle parseAttributes(String str) {
        return parse(str, new KeyParser$$ExternalSyntheticLambda0(), new KeyParser$$ExternalSyntheticLambda1());
    }
}
