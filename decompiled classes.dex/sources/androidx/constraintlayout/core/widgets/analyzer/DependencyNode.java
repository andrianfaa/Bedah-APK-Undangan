package androidx.constraintlayout.core.widgets.analyzer;

import java.util.ArrayList;
import java.util.List;

public class DependencyNode implements Dependency {
    public boolean delegateToWidgetRun = false;
    List<Dependency> dependencies = new ArrayList();
    int margin;
    DimensionDependency marginDependency = null;
    int marginFactor = 1;
    public boolean readyToSolve = false;
    public boolean resolved = false;
    WidgetRun run;
    List<DependencyNode> targets = new ArrayList();
    Type type = Type.UNKNOWN;
    public Dependency updateDelegate = null;
    public int value;

    enum Type {
        UNKNOWN,
        HORIZONTAL_DIMENSION,
        VERTICAL_DIMENSION,
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        BASELINE
    }

    public DependencyNode(WidgetRun run2) {
        this.run = run2;
    }

    public void addDependency(Dependency dependency) {
        this.dependencies.add(dependency);
        if (this.resolved) {
            dependency.update(dependency);
        }
    }

    public void clear() {
        this.targets.clear();
        this.dependencies.clear();
        this.resolved = false;
        this.value = 0;
        this.readyToSolve = false;
        this.delegateToWidgetRun = false;
    }

    public String name() {
        String debugName = this.run.widget.getDebugName();
        return ((this.type == Type.LEFT || this.type == Type.RIGHT) ? debugName + "_HORIZONTAL" : debugName + "_VERTICAL") + ":" + this.type.name();
    }

    public void resolve(int value2) {
        if (!this.resolved) {
            this.resolved = true;
            this.value = value2;
            for (Dependency next : this.dependencies) {
                next.update(next);
            }
        }
    }

    public String toString() {
        return this.run.widget.getDebugName() + ":" + this.type + "(" + (this.resolved ? Integer.valueOf(this.value) : "unresolved") + ") <t=" + this.targets.size() + ":d=" + this.dependencies.size() + ">";
    }

    public void update(Dependency node) {
        for (DependencyNode dependencyNode : this.targets) {
            if (!dependencyNode.resolved) {
                return;
            }
        }
        this.readyToSolve = true;
        Dependency dependency = this.updateDelegate;
        if (dependency != null) {
            dependency.update(this);
        }
        if (this.delegateToWidgetRun) {
            this.run.update(this);
            return;
        }
        DependencyNode dependencyNode2 = null;
        int i = 0;
        for (DependencyNode next : this.targets) {
            if (!(next instanceof DimensionDependency)) {
                dependencyNode2 = next;
                i++;
            }
        }
        if (dependencyNode2 != null && i == 1 && dependencyNode2.resolved) {
            DimensionDependency dimensionDependency = this.marginDependency;
            if (dimensionDependency != null) {
                if (dimensionDependency.resolved) {
                    this.margin = this.marginFactor * this.marginDependency.value;
                } else {
                    return;
                }
            }
            resolve(dependencyNode2.value + this.margin);
        }
        Dependency dependency2 = this.updateDelegate;
        if (dependency2 != null) {
            dependency2.update(this);
        }
    }
}
