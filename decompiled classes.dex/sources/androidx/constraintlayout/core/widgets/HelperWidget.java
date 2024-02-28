package androidx.constraintlayout.core.widgets;

import androidx.constraintlayout.core.widgets.analyzer.Grouping;
import androidx.constraintlayout.core.widgets.analyzer.WidgetGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class HelperWidget extends ConstraintWidget implements Helper {
    public ConstraintWidget[] mWidgets = new ConstraintWidget[4];
    public int mWidgetsCount = 0;

    public void add(ConstraintWidget widget) {
        if (widget != this && widget != null) {
            int i = this.mWidgetsCount + 1;
            ConstraintWidget[] constraintWidgetArr = this.mWidgets;
            if (i > constraintWidgetArr.length) {
                this.mWidgets = (ConstraintWidget[]) Arrays.copyOf(constraintWidgetArr, constraintWidgetArr.length * 2);
            }
            ConstraintWidget[] constraintWidgetArr2 = this.mWidgets;
            int i2 = this.mWidgetsCount;
            constraintWidgetArr2[i2] = widget;
            this.mWidgetsCount = i2 + 1;
        }
    }

    public void addDependents(ArrayList<WidgetGroup> arrayList, int orientation, WidgetGroup group) {
        for (int i = 0; i < this.mWidgetsCount; i++) {
            group.add(this.mWidgets[i]);
        }
        for (int i2 = 0; i2 < this.mWidgetsCount; i2++) {
            Grouping.findDependents(this.mWidgets[i2], orientation, arrayList, group);
        }
    }

    public void copy(ConstraintWidget src, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        super.copy(src, hashMap);
        HelperWidget helperWidget = (HelperWidget) src;
        this.mWidgetsCount = 0;
        int i = helperWidget.mWidgetsCount;
        for (int i2 = 0; i2 < i; i2++) {
            add(hashMap.get(helperWidget.mWidgets[i2]));
        }
    }

    public int findGroupInDependents(int orientation) {
        for (int i = 0; i < this.mWidgetsCount; i++) {
            ConstraintWidget constraintWidget = this.mWidgets[i];
            if (orientation == 0 && constraintWidget.horizontalGroup != -1) {
                return constraintWidget.horizontalGroup;
            }
            if (orientation == 1 && constraintWidget.verticalGroup != -1) {
                return constraintWidget.verticalGroup;
            }
        }
        return -1;
    }

    public void removeAllIds() {
        this.mWidgetsCount = 0;
        Arrays.fill(this.mWidgets, (Object) null);
    }

    public void updateConstraints(ConstraintWidgetContainer container) {
    }
}
