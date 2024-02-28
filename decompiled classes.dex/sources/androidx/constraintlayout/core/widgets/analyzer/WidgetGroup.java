package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.LinearSystem;
import androidx.constraintlayout.core.widgets.Chain;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

public class WidgetGroup {
    private static final boolean DEBUG = false;
    static int count = 0;
    boolean authoritative = false;
    int id = -1;
    private int moveTo = -1;
    int orientation = 0;
    ArrayList<MeasureResult> results = null;
    ArrayList<ConstraintWidget> widgets = new ArrayList<>();

    class MeasureResult {
        int baseline;
        int bottom;
        int left;
        int orientation;
        int right;
        int top;
        WeakReference<ConstraintWidget> widgetRef;

        public MeasureResult(ConstraintWidget widget, LinearSystem system, int orientation2) {
            this.widgetRef = new WeakReference<>(widget);
            this.left = system.getObjectVariableValue(widget.mLeft);
            this.top = system.getObjectVariableValue(widget.mTop);
            this.right = system.getObjectVariableValue(widget.mRight);
            this.bottom = system.getObjectVariableValue(widget.mBottom);
            this.baseline = system.getObjectVariableValue(widget.mBaseline);
            this.orientation = orientation2;
        }

        public void apply() {
            ConstraintWidget constraintWidget = (ConstraintWidget) this.widgetRef.get();
            if (constraintWidget != null) {
                constraintWidget.setFinalFrame(this.left, this.top, this.right, this.bottom, this.baseline, this.orientation);
            }
        }
    }

    public WidgetGroup(int orientation2) {
        int i = count;
        count = i + 1;
        this.id = i;
        this.orientation = orientation2;
    }

    private boolean contains(ConstraintWidget widget) {
        return this.widgets.contains(widget);
    }

    private String getOrientationString() {
        int i = this.orientation;
        return i == 0 ? "Horizontal" : i == 1 ? "Vertical" : i == 2 ? "Both" : "Unknown";
    }

    private int measureWrap(int orientation2, ConstraintWidget widget) {
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = widget.getDimensionBehaviour(orientation2);
        if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT || dimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED) {
            return orientation2 == 0 ? widget.getWidth() : widget.getHeight();
        }
        return -1;
    }

    private int solverMeasure(LinearSystem system, ArrayList<ConstraintWidget> arrayList, int orientation2) {
        ConstraintWidgetContainer constraintWidgetContainer = (ConstraintWidgetContainer) arrayList.get(0).getParent();
        system.reset();
        constraintWidgetContainer.addToSolver(system, false);
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).addToSolver(system, false);
        }
        if (orientation2 == 0 && constraintWidgetContainer.mHorizontalChainsSize > 0) {
            Chain.applyChainConstraints(constraintWidgetContainer, system, arrayList, 0);
        }
        if (orientation2 == 1 && constraintWidgetContainer.mVerticalChainsSize > 0) {
            Chain.applyChainConstraints(constraintWidgetContainer, system, arrayList, 1);
        }
        try {
            system.minimize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.results = new ArrayList<>();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            this.results.add(new MeasureResult(arrayList.get(i2), system, orientation2));
        }
        if (orientation2 == 0) {
            int objectVariableValue = system.getObjectVariableValue(constraintWidgetContainer.mLeft);
            int objectVariableValue2 = system.getObjectVariableValue(constraintWidgetContainer.mRight);
            system.reset();
            return objectVariableValue2 - objectVariableValue;
        }
        int objectVariableValue3 = system.getObjectVariableValue(constraintWidgetContainer.mTop);
        int objectVariableValue4 = system.getObjectVariableValue(constraintWidgetContainer.mBottom);
        system.reset();
        return objectVariableValue4 - objectVariableValue3;
    }

    public boolean add(ConstraintWidget widget) {
        if (this.widgets.contains(widget)) {
            return false;
        }
        this.widgets.add(widget);
        return true;
    }

    public void apply() {
        if (this.results != null && this.authoritative) {
            for (int i = 0; i < this.results.size(); i++) {
                this.results.get(i).apply();
            }
        }
    }

    public void cleanup(ArrayList<WidgetGroup> arrayList) {
        int size = this.widgets.size();
        if (this.moveTo != -1 && size > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                WidgetGroup widgetGroup = arrayList.get(i);
                if (this.moveTo == widgetGroup.id) {
                    moveTo(this.orientation, widgetGroup);
                }
            }
        }
        if (size == 0) {
            arrayList.remove(this);
        }
    }

    public void clear() {
        this.widgets.clear();
    }

    public int getId() {
        return this.id;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public boolean intersectWith(WidgetGroup group) {
        for (int i = 0; i < this.widgets.size(); i++) {
            if (group.contains(this.widgets.get(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean isAuthoritative() {
        return this.authoritative;
    }

    public int measureWrap(LinearSystem system, int orientation2) {
        if (this.widgets.size() == 0) {
            return 0;
        }
        return solverMeasure(system, this.widgets, orientation2);
    }

    public void moveTo(int orientation2, WidgetGroup widgetGroup) {
        Iterator<ConstraintWidget> it = this.widgets.iterator();
        while (it.hasNext()) {
            ConstraintWidget next = it.next();
            widgetGroup.add(next);
            if (orientation2 == 0) {
                next.horizontalGroup = widgetGroup.getId();
            } else {
                next.verticalGroup = widgetGroup.getId();
            }
        }
        this.moveTo = widgetGroup.id;
    }

    public void setAuthoritative(boolean isAuthoritative) {
        this.authoritative = isAuthoritative;
    }

    public void setOrientation(int orientation2) {
        this.orientation = orientation2;
    }

    public int size() {
        return this.widgets.size();
    }

    public String toString() {
        String str = getOrientationString() + " [" + this.id + "] <";
        Iterator<ConstraintWidget> it = this.widgets.iterator();
        while (it.hasNext()) {
            str = str + " " + it.next().getDebugName();
        }
        return str + " >";
    }
}
