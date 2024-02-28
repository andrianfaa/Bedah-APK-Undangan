package androidx.appcompat.resources;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class Compatibility {

    public static class Api15Impl {
        private Api15Impl() {
        }

        public static void getValueForDensity(Resources resources, int id, int density, TypedValue outValue, boolean resolveRefs) {
            resources.getValueForDensity(id, density, outValue, resolveRefs);
        }
    }

    public static class Api18Impl {
        private Api18Impl() {
        }

        public static void setAutoCancel(ObjectAnimator objectAnimator, boolean cancel) {
            objectAnimator.setAutoCancel(cancel);
        }
    }

    public static class Api21Impl {
        private Api21Impl() {
        }

        public static Drawable createFromXmlInner(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws IOException, XmlPullParserException {
            return Drawable.createFromXmlInner(r, parser, attrs, theme);
        }

        public static int getChangingConfigurations(TypedArray typedArray) {
            return typedArray.getChangingConfigurations();
        }

        public static void inflate(Drawable drawable, Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws IOException, XmlPullParserException {
            drawable.inflate(r, parser, attrs, theme);
        }
    }

    private Compatibility() {
    }
}
