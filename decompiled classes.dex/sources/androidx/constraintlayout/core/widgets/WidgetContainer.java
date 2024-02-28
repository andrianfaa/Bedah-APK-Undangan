package androidx.constraintlayout.core.widgets;

import androidx.constraintlayout.core.Cache;
import java.util.ArrayList;

public class WidgetContainer extends ConstraintWidget {
    public ArrayList<ConstraintWidget> mChildren = new ArrayList<>();

    public WidgetContainer() {
    }

    public WidgetContainer(int width, int height) {
        super(width, height);
    }

    public WidgetContainer(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void add(ConstraintWidget widget) {
        this.mChildren.add(widget);
        if (widget.getParent() != null) {
            ((WidgetContainer) widget.getParent()).remove(widget);
        }
        widget.setParent(this);
    }

    public void add(ConstraintWidget... widgets) {
        for (ConstraintWidget add : widgets) {
            add(add);
        }
    }

    public ArrayList<ConstraintWidget> getChildren() {
        return this.mChildren;
    }

    public ConstraintWidgetContainer getRootConstraintContainer() {
        ConstraintWidget parent = getParent();
        ConstraintWidgetContainer constraintWidgetContainer = null;
        if (this instanceof ConstraintWidgetContainer) {
            constraintWidgetContainer = (ConstraintWidgetContainer) this;
        }
        while (parent != null) {
            ConstraintWidget constraintWidget = parent;
            parent = constraintWidget.getParent();
            if (constraintWidget instanceof ConstraintWidgetContainer) {
                constraintWidgetContainer = (ConstraintWidgetContainer) constraintWidget;
            }
        }
        return constraintWidgetContainer;
    }

    public void layout() {
        ArrayList<ConstraintWidget> arrayList = this.mChildren;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                ConstraintWidget constraintWidget = this.mChildren.get(i);
                if (constraintWidget instanceof WidgetContainer) {
                    ((WidgetContainer) constraintWidget).layout();
                }
            }
        }
    }

    public void remove(ConstraintWidget widget) {
        this.mChildren.remove(widget);
        widget.reset();
    }

    public void removeAllChildren() {
        this.mChildren.clear();
    }

    public void reset() {
        this.mChildren.clear();
        super.reset();
    }

    public void resetSolverVariables(Cache cache) {
        super.resetSolverVariables(cache);
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            this.mChildren.get(i).resetSolverVariables(cache);
        }
    }

    public void setOffset(int x, int y) {
        super.setOffset(x, y);
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            this.mChildren.get(i).setOffset(getRootX(), getRootY());
        }
    }
}
