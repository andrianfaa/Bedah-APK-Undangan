package androidx.transition;

import android.view.View;

public abstract class VisibilityPropagation extends TransitionPropagation {
    private static final String PROPNAME_VIEW_CENTER = "android:visibilityPropagation:center";
    private static final String PROPNAME_VISIBILITY = "android:visibilityPropagation:visibility";
    private static final String[] VISIBILITY_PROPAGATION_VALUES = {PROPNAME_VISIBILITY, PROPNAME_VIEW_CENTER};

    private static int getViewCoordinate(TransitionValues values, int coordinateIndex) {
        int[] iArr;
        if (values == null || (iArr = (int[]) values.values.get(PROPNAME_VIEW_CENTER)) == null) {
            return -1;
        }
        return iArr[coordinateIndex];
    }

    public void captureValues(TransitionValues values) {
        View view = values.view;
        Integer num = (Integer) values.values.get("android:visibility:visibility");
        if (num == null) {
            num = Integer.valueOf(view.getVisibility());
        }
        values.values.put(PROPNAME_VISIBILITY, num);
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        iArr[0] = iArr[0] + Math.round(view.getTranslationX());
        iArr[0] = iArr[0] + (view.getWidth() / 2);
        iArr[1] = iArr[1] + Math.round(view.getTranslationY());
        iArr[1] = iArr[1] + (view.getHeight() / 2);
        values.values.put(PROPNAME_VIEW_CENTER, iArr);
    }

    public String[] getPropagationProperties() {
        return VISIBILITY_PROPAGATION_VALUES;
    }

    public int getViewVisibility(TransitionValues values) {
        Integer num;
        if (values == null || (num = (Integer) values.values.get(PROPNAME_VISIBILITY)) == null) {
            return 8;
        }
        return num.intValue();
    }

    public int getViewX(TransitionValues values) {
        return getViewCoordinate(values, 0);
    }

    public int getViewY(TransitionValues values) {
        return getViewCoordinate(values, 1);
    }
}
