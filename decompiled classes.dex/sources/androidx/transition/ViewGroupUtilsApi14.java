package androidx.transition;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.util.Log;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewGroupUtilsApi14 {
    private static final int LAYOUT_TRANSITION_CHANGING = 4;
    private static final String TAG = "ViewGroupUtilsApi14";
    private static Method sCancelMethod;
    private static boolean sCancelMethodFetched;
    private static LayoutTransition sEmptyLayoutTransition;
    private static Field sLayoutSuppressedField;
    private static boolean sLayoutSuppressedFieldFetched;

    private ViewGroupUtilsApi14() {
    }

    private static void cancelLayoutTransition(LayoutTransition t) {
        if (!sCancelMethodFetched) {
            try {
                Method declaredMethod = LayoutTransition.class.getDeclaredMethod("cancel", new Class[0]);
                sCancelMethod = declaredMethod;
                declaredMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                Log.i(TAG, "Failed to access cancel method by reflection");
            }
            sCancelMethodFetched = true;
        }
        Method method = sCancelMethod;
        if (method != null) {
            try {
                method.invoke(t, new Object[0]);
            } catch (IllegalAccessException e2) {
                Log.i(TAG, "Failed to access cancel method by reflection");
            } catch (InvocationTargetException e3) {
                Log.i(TAG, "Failed to invoke cancel method by reflection");
            }
        }
    }

    static void suppressLayout(ViewGroup group, boolean suppress) {
        if (sEmptyLayoutTransition == null) {
            AnonymousClass1 r0 = new LayoutTransition() {
                public boolean isChangingLayout() {
                    return true;
                }
            };
            sEmptyLayoutTransition = r0;
            r0.setAnimator(2, (Animator) null);
            sEmptyLayoutTransition.setAnimator(0, (Animator) null);
            sEmptyLayoutTransition.setAnimator(1, (Animator) null);
            sEmptyLayoutTransition.setAnimator(3, (Animator) null);
            sEmptyLayoutTransition.setAnimator(4, (Animator) null);
        }
        if (suppress) {
            LayoutTransition layoutTransition = group.getLayoutTransition();
            if (layoutTransition != null) {
                if (layoutTransition.isRunning()) {
                    cancelLayoutTransition(layoutTransition);
                }
                if (layoutTransition != sEmptyLayoutTransition) {
                    group.setTag(R.id.transition_layout_save, layoutTransition);
                }
            }
            group.setLayoutTransition(sEmptyLayoutTransition);
            return;
        }
        group.setLayoutTransition((LayoutTransition) null);
        if (!sLayoutSuppressedFieldFetched) {
            try {
                Field declaredField = ViewGroup.class.getDeclaredField("mLayoutSuppressed");
                sLayoutSuppressedField = declaredField;
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                Log.i(TAG, "Failed to access mLayoutSuppressed field by reflection");
            }
            sLayoutSuppressedFieldFetched = true;
        }
        boolean z = false;
        Field field = sLayoutSuppressedField;
        if (field != null) {
            try {
                z = field.getBoolean(group);
                if (z) {
                    sLayoutSuppressedField.setBoolean(group, false);
                }
            } catch (IllegalAccessException e2) {
                Log.i(TAG, "Failed to get mLayoutSuppressed field by reflection");
            }
        }
        if (z) {
            group.requestLayout();
        }
        LayoutTransition layoutTransition2 = (LayoutTransition) group.getTag(R.id.transition_layout_save);
        if (layoutTransition2 != null) {
            group.setTag(R.id.transition_layout_save, (Object) null);
            group.setLayoutTransition(layoutTransition2);
        }
    }
}
