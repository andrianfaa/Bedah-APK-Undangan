package androidx.appcompat.app;

import android.util.AttributeSet;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Deque;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class LayoutIncludeDetector {
    private final Deque<WeakReference<XmlPullParser>> mXmlParserStack = new ArrayDeque();

    LayoutIncludeDetector() {
    }

    private static boolean isParserOutdated(XmlPullParser parser) {
        if (parser == null) {
            return true;
        }
        try {
            return parser.getEventType() == 3 || parser.getEventType() == 1;
        } catch (XmlPullParserException e) {
            return true;
        }
    }

    private static XmlPullParser popOutdatedAttrHolders(Deque<WeakReference<XmlPullParser>> deque) {
        while (!deque.isEmpty()) {
            XmlPullParser xmlPullParser = (XmlPullParser) deque.peek().get();
            if (!isParserOutdated(xmlPullParser)) {
                return xmlPullParser;
            }
            deque.pop();
        }
        return null;
    }

    private static boolean shouldInheritContext(XmlPullParser parser, XmlPullParser previousParser) {
        if (previousParser == null || parser == previousParser) {
            return false;
        }
        try {
            if (previousParser.getEventType() == 2) {
                return "include".equals(previousParser.getName());
            }
            return false;
        } catch (XmlPullParserException e) {
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean detect(AttributeSet attrs) {
        if (!(attrs instanceof XmlPullParser)) {
            return false;
        }
        XmlPullParser xmlPullParser = (XmlPullParser) attrs;
        if (xmlPullParser.getDepth() != 1) {
            return false;
        }
        XmlPullParser popOutdatedAttrHolders = popOutdatedAttrHolders(this.mXmlParserStack);
        this.mXmlParserStack.push(new WeakReference(xmlPullParser));
        return shouldInheritContext(xmlPullParser, popOutdatedAttrHolders);
    }
}
