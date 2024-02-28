package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.widgets.Barrier;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.core.widgets.Flow;
import androidx.constraintlayout.core.widgets.Guideline;
import androidx.constraintlayout.core.widgets.HelperWidget;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import java.util.ArrayList;
import java.util.Iterator;

public class Grouping {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_GROUPING = false;

    public static WidgetGroup findDependents(ConstraintWidget constraintWidget, int orientation, ArrayList<WidgetGroup> arrayList, WidgetGroup group) {
        int findGroupInDependents;
        int i = orientation == 0 ? constraintWidget.horizontalGroup : constraintWidget.verticalGroup;
        if (i != -1 && (group == null || i != group.id)) {
            int i2 = 0;
            while (true) {
                if (i2 >= arrayList.size()) {
                    break;
                }
                WidgetGroup widgetGroup = arrayList.get(i2);
                if (widgetGroup.getId() == i) {
                    if (group != null) {
                        group.moveTo(orientation, widgetGroup);
                        arrayList.remove(group);
                    }
                    group = widgetGroup;
                } else {
                    i2++;
                }
            }
        } else if (i != -1) {
            return group;
        }
        if (group == null) {
            if ((constraintWidget instanceof HelperWidget) && (findGroupInDependents = ((HelperWidget) constraintWidget).findGroupInDependents(orientation)) != -1) {
                int i3 = 0;
                while (true) {
                    if (i3 >= arrayList.size()) {
                        break;
                    }
                    WidgetGroup widgetGroup2 = arrayList.get(i3);
                    if (widgetGroup2.getId() == findGroupInDependents) {
                        group = widgetGroup2;
                        break;
                    }
                    i3++;
                }
            }
            if (group == null) {
                group = new WidgetGroup(orientation);
            }
            arrayList.add(group);
        }
        if (group.add(constraintWidget)) {
            if (constraintWidget instanceof Guideline) {
                Guideline guideline = (Guideline) constraintWidget;
                guideline.getAnchor().findDependents(guideline.getOrientation() == 0 ? 1 : 0, arrayList, group);
            }
            if (orientation == 0) {
                constraintWidget.horizontalGroup = group.getId();
                constraintWidget.mLeft.findDependents(orientation, arrayList, group);
                constraintWidget.mRight.findDependents(orientation, arrayList, group);
            } else {
                constraintWidget.verticalGroup = group.getId();
                constraintWidget.mTop.findDependents(orientation, arrayList, group);
                constraintWidget.mBaseline.findDependents(orientation, arrayList, group);
                constraintWidget.mBottom.findDependents(orientation, arrayList, group);
            }
            constraintWidget.mCenter.findDependents(orientation, arrayList, group);
        }
        return group;
    }

    private static WidgetGroup findGroup(ArrayList<WidgetGroup> arrayList, int groupId) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            WidgetGroup widgetGroup = arrayList.get(i);
            if (groupId == widgetGroup.id) {
                return widgetGroup;
            }
        }
        return null;
    }

    public static boolean simpleSolvingPass(ConstraintWidgetContainer layout, BasicMeasure.Measurer measurer) {
        boolean z;
        ConstraintWidgetContainer constraintWidgetContainer = layout;
        ArrayList<ConstraintWidget> children = layout.getChildren();
        int size = children.size();
        ArrayList arrayList = null;
        ArrayList arrayList2 = null;
        ArrayList arrayList3 = null;
        ArrayList arrayList4 = null;
        ArrayList arrayList5 = null;
        ArrayList arrayList6 = null;
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = children.get(i);
            if (!validInGroup(layout.getHorizontalDimensionBehaviour(), layout.getVerticalDimensionBehaviour(), constraintWidget.getHorizontalDimensionBehaviour(), constraintWidget.getVerticalDimensionBehaviour()) || (constraintWidget instanceof Flow)) {
                return false;
            }
        }
        if (constraintWidgetContainer.mMetrics != null) {
            constraintWidgetContainer.mMetrics.grouping++;
        }
        for (int i2 = 0; i2 < size; i2++) {
            ConstraintWidget constraintWidget2 = children.get(i2);
            if (!validInGroup(layout.getHorizontalDimensionBehaviour(), layout.getVerticalDimensionBehaviour(), constraintWidget2.getHorizontalDimensionBehaviour(), constraintWidget2.getVerticalDimensionBehaviour())) {
                ConstraintWidgetContainer.measure(0, constraintWidget2, measurer, constraintWidgetContainer.mMeasure, BasicMeasure.Measure.SELF_DIMENSIONS);
            } else {
                BasicMeasure.Measurer measurer2 = measurer;
            }
            if (constraintWidget2 instanceof Guideline) {
                Guideline guideline = (Guideline) constraintWidget2;
                if (guideline.getOrientation() == 0) {
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList();
                    }
                    arrayList2.add(guideline);
                }
                if (guideline.getOrientation() == 1) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(guideline);
                }
            }
            if (constraintWidget2 instanceof HelperWidget) {
                if (constraintWidget2 instanceof Barrier) {
                    Barrier barrier = (Barrier) constraintWidget2;
                    if (barrier.getOrientation() == 0) {
                        if (arrayList3 == null) {
                            arrayList3 = new ArrayList();
                        }
                        arrayList3.add(barrier);
                    }
                    if (barrier.getOrientation() == 1) {
                        if (arrayList4 == null) {
                            arrayList4 = new ArrayList();
                        }
                        arrayList4.add(barrier);
                    }
                } else {
                    HelperWidget helperWidget = (HelperWidget) constraintWidget2;
                    if (arrayList3 == null) {
                        arrayList3 = new ArrayList();
                    }
                    arrayList3.add(helperWidget);
                    if (arrayList4 == null) {
                        arrayList4 = new ArrayList();
                    }
                    arrayList4.add(helperWidget);
                }
            }
            if (constraintWidget2.mLeft.mTarget == null && constraintWidget2.mRight.mTarget == null && !(constraintWidget2 instanceof Guideline) && !(constraintWidget2 instanceof Barrier)) {
                if (arrayList5 == null) {
                    arrayList5 = new ArrayList();
                }
                arrayList5.add(constraintWidget2);
            }
            if (constraintWidget2.mTop.mTarget == null && constraintWidget2.mBottom.mTarget == null && constraintWidget2.mBaseline.mTarget == null && !(constraintWidget2 instanceof Guideline) && !(constraintWidget2 instanceof Barrier)) {
                if (arrayList6 == null) {
                    arrayList6 = new ArrayList();
                }
                arrayList6.add(constraintWidget2);
            }
        }
        BasicMeasure.Measurer measurer3 = measurer;
        ArrayList arrayList7 = new ArrayList();
        ArrayList arrayList8 = arrayList7;
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                findDependents((Guideline) it.next(), 0, arrayList8, (WidgetGroup) null);
            }
        }
        if (arrayList3 != null) {
            Iterator it2 = arrayList3.iterator();
            while (it2.hasNext()) {
                HelperWidget helperWidget2 = (HelperWidget) it2.next();
                ArrayList arrayList9 = arrayList;
                WidgetGroup findDependents = findDependents(helperWidget2, 0, arrayList8, (WidgetGroup) null);
                helperWidget2.addDependents(arrayList8, 0, findDependents);
                findDependents.cleanup(arrayList8);
                arrayList = arrayList9;
            }
            ArrayList arrayList10 = arrayList;
        } else {
            ArrayList arrayList11 = arrayList;
        }
        ConstraintAnchor anchor = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.LEFT);
        if (anchor.getDependents() != null) {
            Iterator<ConstraintAnchor> it3 = anchor.getDependents().iterator();
            while (it3.hasNext()) {
                findDependents(it3.next().mOwner, 0, arrayList8, (WidgetGroup) null);
                anchor = anchor;
            }
            ConstraintAnchor constraintAnchor = anchor;
        } else {
            ConstraintAnchor constraintAnchor2 = anchor;
        }
        ConstraintAnchor anchor2 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.RIGHT);
        if (anchor2.getDependents() != null) {
            Iterator<ConstraintAnchor> it4 = anchor2.getDependents().iterator();
            while (it4.hasNext()) {
                findDependents(it4.next().mOwner, 0, arrayList8, (WidgetGroup) null);
                anchor2 = anchor2;
            }
            ConstraintAnchor constraintAnchor3 = anchor2;
        } else {
            ConstraintAnchor constraintAnchor4 = anchor2;
        }
        ConstraintAnchor anchor3 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.CENTER);
        if (anchor3.getDependents() != null) {
            Iterator<ConstraintAnchor> it5 = anchor3.getDependents().iterator();
            while (it5.hasNext()) {
                findDependents(it5.next().mOwner, 0, arrayList8, (WidgetGroup) null);
                anchor3 = anchor3;
            }
            ConstraintAnchor constraintAnchor5 = anchor3;
        } else {
            ConstraintAnchor constraintAnchor6 = anchor3;
        }
        if (arrayList5 != null) {
            Iterator it6 = arrayList5.iterator();
            while (it6.hasNext()) {
                findDependents((ConstraintWidget) it6.next(), 0, arrayList8, (WidgetGroup) null);
            }
        }
        ArrayList arrayList12 = arrayList7;
        if (arrayList2 != null) {
            Iterator it7 = arrayList2.iterator();
            while (it7.hasNext()) {
                findDependents((Guideline) it7.next(), 1, arrayList12, (WidgetGroup) null);
            }
        }
        if (arrayList4 != null) {
            Iterator it8 = arrayList4.iterator();
            while (it8.hasNext()) {
                HelperWidget helperWidget3 = (HelperWidget) it8.next();
                WidgetGroup findDependents2 = findDependents(helperWidget3, 1, arrayList12, (WidgetGroup) null);
                helperWidget3.addDependents(arrayList12, 1, findDependents2);
                findDependents2.cleanup(arrayList12);
            }
        }
        ConstraintAnchor anchor4 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.TOP);
        if (anchor4.getDependents() != null) {
            Iterator<ConstraintAnchor> it9 = anchor4.getDependents().iterator();
            while (it9.hasNext()) {
                findDependents(it9.next().mOwner, 1, arrayList12, (WidgetGroup) null);
                arrayList2 = arrayList2;
            }
            ArrayList arrayList13 = arrayList2;
        } else {
            ArrayList arrayList14 = arrayList2;
        }
        ConstraintAnchor anchor5 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.BASELINE);
        if (anchor5.getDependents() != null) {
            Iterator<ConstraintAnchor> it10 = anchor5.getDependents().iterator();
            while (it10.hasNext()) {
                findDependents(it10.next().mOwner, 1, arrayList12, (WidgetGroup) null);
                anchor5 = anchor5;
            }
            ConstraintAnchor constraintAnchor7 = anchor5;
        } else {
            ConstraintAnchor constraintAnchor8 = anchor5;
        }
        ConstraintAnchor anchor6 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.BOTTOM);
        if (anchor6.getDependents() != null) {
            Iterator<ConstraintAnchor> it11 = anchor6.getDependents().iterator();
            while (it11.hasNext()) {
                findDependents(it11.next().mOwner, 1, arrayList12, (WidgetGroup) null);
                anchor6 = anchor6;
            }
            ConstraintAnchor constraintAnchor9 = anchor6;
        } else {
            ConstraintAnchor constraintAnchor10 = anchor6;
        }
        ConstraintAnchor anchor7 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.CENTER);
        if (anchor7.getDependents() != null) {
            Iterator<ConstraintAnchor> it12 = anchor7.getDependents().iterator();
            while (it12.hasNext()) {
                findDependents(it12.next().mOwner, 1, arrayList12, (WidgetGroup) null);
                anchor7 = anchor7;
            }
            ConstraintAnchor constraintAnchor11 = anchor7;
        } else {
            ConstraintAnchor constraintAnchor12 = anchor7;
        }
        if (arrayList6 != null) {
            Iterator it13 = arrayList6.iterator();
            while (it13.hasNext()) {
                findDependents((ConstraintWidget) it13.next(), 1, arrayList12, (WidgetGroup) null);
            }
        }
        for (int i3 = 0; i3 < size; i3++) {
            ConstraintWidget constraintWidget3 = children.get(i3);
            if (constraintWidget3.oppositeDimensionsTied()) {
                WidgetGroup findGroup = findGroup(arrayList7, constraintWidget3.horizontalGroup);
                WidgetGroup findGroup2 = findGroup(arrayList7, constraintWidget3.verticalGroup);
                if (!(findGroup == null || findGroup2 == null)) {
                    findGroup.moveTo(0, findGroup2);
                    findGroup2.setOrientation(2);
                    arrayList7.remove(findGroup);
                }
            }
        }
        if (arrayList7.size() <= 1) {
            return false;
        }
        WidgetGroup widgetGroup = null;
        WidgetGroup widgetGroup2 = null;
        if (layout.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            int i4 = 0;
            WidgetGroup widgetGroup3 = null;
            Iterator it14 = arrayList7.iterator();
            while (it14.hasNext()) {
                WidgetGroup widgetGroup4 = (WidgetGroup) it14.next();
                ArrayList<ConstraintWidget> arrayList15 = children;
                if (widgetGroup4.getOrientation() == 1) {
                    children = arrayList15;
                } else {
                    widgetGroup4.setAuthoritative(false);
                    int measureWrap = widgetGroup4.measureWrap(layout.getSystem(), 0);
                    if (measureWrap > i4) {
                        i4 = measureWrap;
                        widgetGroup3 = widgetGroup4;
                    }
                    children = arrayList15;
                }
            }
            ArrayList<ConstraintWidget> arrayList16 = children;
            if (widgetGroup3 != null) {
                constraintWidgetContainer.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                constraintWidgetContainer.setWidth(i4);
                widgetGroup3.setAuthoritative(true);
                widgetGroup = widgetGroup3;
            }
        } else {
            ArrayList<ConstraintWidget> arrayList17 = children;
        }
        if (layout.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            int i5 = 0;
            WidgetGroup widgetGroup5 = null;
            Iterator it15 = arrayList7.iterator();
            while (it15.hasNext()) {
                WidgetGroup widgetGroup6 = (WidgetGroup) it15.next();
                if (widgetGroup6.getOrientation() != 0) {
                    widgetGroup6.setAuthoritative(false);
                    int measureWrap2 = widgetGroup6.measureWrap(layout.getSystem(), 1);
                    if (measureWrap2 > i5) {
                        widgetGroup5 = widgetGroup6;
                        i5 = measureWrap2;
                    }
                }
            }
            if (widgetGroup5 != null) {
                constraintWidgetContainer.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                constraintWidgetContainer.setHeight(i5);
                z = true;
                widgetGroup5.setAuthoritative(true);
                widgetGroup2 = widgetGroup5;
            } else {
                z = true;
            }
        } else {
            z = true;
        }
        if (widgetGroup == null && widgetGroup2 == null) {
            return false;
        }
        return z;
    }

    public static boolean validInGroup(ConstraintWidget.DimensionBehaviour layoutHorizontal, ConstraintWidget.DimensionBehaviour layoutVertical, ConstraintWidget.DimensionBehaviour widgetHorizontal, ConstraintWidget.DimensionBehaviour widgetVertical) {
        return (widgetHorizontal == ConstraintWidget.DimensionBehaviour.FIXED || widgetHorizontal == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || (widgetHorizontal == ConstraintWidget.DimensionBehaviour.MATCH_PARENT && layoutHorizontal != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)) || (widgetVertical == ConstraintWidget.DimensionBehaviour.FIXED || widgetVertical == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || (widgetVertical == ConstraintWidget.DimensionBehaviour.MATCH_PARENT && layoutVertical != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT));
    }
}
