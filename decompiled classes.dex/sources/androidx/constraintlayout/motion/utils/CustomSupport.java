package androidx.constraintlayout.motion.utils;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.constraintlayout.widget.ConstraintAttribute;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import mt.Log1F380D;

/* compiled from: 0018 */
public class CustomSupport {
    private static final String TAG = "CustomSupport";

    /* renamed from: androidx.constraintlayout.motion.utils.CustomSupport$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType;

        static {
            int[] iArr = new int[ConstraintAttribute.AttributeType.values().length];
            $SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType = iArr;
            try {
                iArr[ConstraintAttribute.AttributeType.INT_TYPE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[ConstraintAttribute.AttributeType.FLOAT_TYPE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[ConstraintAttribute.AttributeType.COLOR_DRAWABLE_TYPE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[ConstraintAttribute.AttributeType.COLOR_TYPE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[ConstraintAttribute.AttributeType.STRING_TYPE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[ConstraintAttribute.AttributeType.BOOLEAN_TYPE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[ConstraintAttribute.AttributeType.DIMENSION_TYPE.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    private static int clamp(int c) {
        int c2 = (c & (~(c >> 31))) - 255;
        return (c2 & (c2 >> 31)) + 255;
    }

    public static void setInterpolatedValue(ConstraintAttribute att, View view, float[] value) {
        View view2 = view;
        Class<?> cls = view.getClass();
        String str = "set" + att.getName();
        try {
            boolean z = true;
            switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[att.getType().ordinal()]) {
                case 1:
                    cls.getMethod(str, new Class[]{Integer.TYPE}).invoke(view2, new Object[]{Integer.valueOf((int) value[0])});
                    return;
                case 2:
                    cls.getMethod(str, new Class[]{Float.TYPE}).invoke(view2, new Object[]{Float.valueOf(value[0])});
                    return;
                case 3:
                    Method method = cls.getMethod(str, new Class[]{Drawable.class});
                    int clamp = clamp((int) (((float) Math.pow((double) value[0], 0.45454545454545453d)) * 255.0f));
                    int clamp2 = clamp((int) (((float) Math.pow((double) value[1], 0.45454545454545453d)) * 255.0f));
                    int clamp3 = clamp((int) (((float) Math.pow((double) value[2], 0.45454545454545453d)) * 255.0f));
                    ColorDrawable colorDrawable = new ColorDrawable();
                    colorDrawable.setColor((clamp((int) (value[3] * 255.0f)) << 24) | (clamp << 16) | (clamp2 << 8) | clamp3);
                    method.invoke(view2, new Object[]{colorDrawable});
                    return;
                case 4:
                    cls.getMethod(str, new Class[]{Integer.TYPE}).invoke(view2, new Object[]{Integer.valueOf((clamp((int) (value[3] * 255.0f)) << 24) | (clamp((int) (((float) Math.pow((double) value[0], 0.45454545454545453d)) * 255.0f)) << 16) | (clamp((int) (((float) Math.pow((double) value[1], 0.45454545454545453d)) * 255.0f)) << 8) | clamp((int) (((float) Math.pow((double) value[2], 0.45454545454545453d)) * 255.0f)))});
                    return;
                case 5:
                    throw new RuntimeException("unable to interpolate strings " + att.getName());
                case 6:
                    Method method2 = cls.getMethod(str, new Class[]{Boolean.TYPE});
                    Object[] objArr = new Object[1];
                    if (value[0] <= 0.5f) {
                        z = false;
                    }
                    objArr[0] = Boolean.valueOf(z);
                    method2.invoke(view2, objArr);
                    return;
                case 7:
                    cls.getMethod(str, new Class[]{Float.TYPE}).invoke(view2, new Object[]{Float.valueOf(value[0])});
                    return;
                default:
                    return;
            }
        } catch (NoSuchMethodException e) {
            StringBuilder append = new StringBuilder().append("no method ").append(str).append(" on View \"");
            String name = Debug.getName(view);
            Log1F380D.a((Object) name);
            Log.e(TAG, append.append(name).append("\"").toString());
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            StringBuilder append2 = new StringBuilder().append("cannot access method ").append(str).append(" on View \"");
            String name2 = Debug.getName(view);
            Log1F380D.a((Object) name2);
            Log.e(TAG, append2.append(name2).append("\"").toString());
            e2.printStackTrace();
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
        }
    }
}
