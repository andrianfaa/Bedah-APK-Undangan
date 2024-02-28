package androidx.vectordrawable.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import java.io.IOException;
import mt.Log1F380D;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: 0097 */
public class AnimationUtilsCompat {
    private AnimationUtilsCompat() {
    }

    private static Interpolator createInterpolatorFromXml(Context context, Resources res, Resources.Theme theme, XmlPullParser parser) throws XmlPullParserException, IOException {
        LinearInterpolator linearInterpolator = null;
        int depth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int i = next;
            if ((next != 3 || parser.getDepth() > depth) && i != 1) {
                if (i == 2) {
                    AttributeSet asAttributeSet = Xml.asAttributeSet(parser);
                    String name = parser.getName();
                    if (name.equals("linearInterpolator")) {
                        linearInterpolator = new LinearInterpolator();
                    } else if (name.equals("accelerateInterpolator")) {
                        linearInterpolator = new AccelerateInterpolator(context, asAttributeSet);
                    } else if (name.equals("decelerateInterpolator")) {
                        linearInterpolator = new DecelerateInterpolator(context, asAttributeSet);
                    } else if (name.equals("accelerateDecelerateInterpolator")) {
                        linearInterpolator = new AccelerateDecelerateInterpolator();
                    } else if (name.equals("cycleInterpolator")) {
                        linearInterpolator = new CycleInterpolator(context, asAttributeSet);
                    } else if (name.equals("anticipateInterpolator")) {
                        linearInterpolator = new AnticipateInterpolator(context, asAttributeSet);
                    } else if (name.equals("overshootInterpolator")) {
                        linearInterpolator = new OvershootInterpolator(context, asAttributeSet);
                    } else if (name.equals("anticipateOvershootInterpolator")) {
                        linearInterpolator = new AnticipateOvershootInterpolator(context, asAttributeSet);
                    } else if (name.equals("bounceInterpolator")) {
                        linearInterpolator = new BounceInterpolator();
                    } else if (name.equals("pathInterpolator")) {
                        linearInterpolator = new PathInterpolatorCompat(context, asAttributeSet, parser);
                    } else {
                        throw new RuntimeException("Unknown interpolator name: " + parser.getName());
                    }
                }
            }
        }
        return linearInterpolator;
    }

    public static Interpolator loadInterpolator(Context context, int id) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 21) {
            return AnimationUtils.loadInterpolator(context, id);
        }
        XmlResourceParser xmlResourceParser = null;
        if (id == 17563663) {
            try {
                FastOutLinearInInterpolator fastOutLinearInInterpolator = new FastOutLinearInInterpolator();
                if (xmlResourceParser != null) {
                    xmlResourceParser.close();
                }
                return fastOutLinearInInterpolator;
            } catch (XmlPullParserException e) {
                StringBuilder append = new StringBuilder().append("Can't load animation resource ID #0x");
                String hexString = Integer.toHexString(id);
                Log1F380D.a((Object) hexString);
                Resources.NotFoundException notFoundException = new Resources.NotFoundException(append.append(hexString).toString());
                notFoundException.initCause(e);
                throw notFoundException;
            } catch (IOException e2) {
                StringBuilder append2 = new StringBuilder().append("Can't load animation resource ID #0x");
                String hexString2 = Integer.toHexString(id);
                Log1F380D.a((Object) hexString2);
                Resources.NotFoundException notFoundException2 = new Resources.NotFoundException(append2.append(hexString2).toString());
                notFoundException2.initCause(e2);
                throw notFoundException2;
            } catch (Throwable th) {
                if (xmlResourceParser != null) {
                    xmlResourceParser.close();
                }
                throw th;
            }
        } else if (id == 17563661) {
            FastOutSlowInInterpolator fastOutSlowInInterpolator = new FastOutSlowInInterpolator();
            if (xmlResourceParser != null) {
                xmlResourceParser.close();
            }
            return fastOutSlowInInterpolator;
        } else if (id == 17563662) {
            LinearOutSlowInInterpolator linearOutSlowInInterpolator = new LinearOutSlowInInterpolator();
            if (xmlResourceParser != null) {
                xmlResourceParser.close();
            }
            return linearOutSlowInInterpolator;
        } else {
            XmlResourceParser animation = context.getResources().getAnimation(id);
            Interpolator createInterpolatorFromXml = createInterpolatorFromXml(context, context.getResources(), context.getTheme(), animation);
            if (animation != null) {
                animation.close();
            }
            return createInterpolatorFromXml;
        }
    }
}
