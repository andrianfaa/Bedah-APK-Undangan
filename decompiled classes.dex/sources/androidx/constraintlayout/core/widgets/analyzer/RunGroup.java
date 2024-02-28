package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import java.util.ArrayList;
import java.util.Iterator;

class RunGroup {
    public static final int BASELINE = 2;
    public static final int END = 1;
    public static final int START = 0;
    public static int index;
    int direction;
    public boolean dual = false;
    WidgetRun firstRun = null;
    int groupIndex = 0;
    WidgetRun lastRun = null;
    public int position = 0;
    ArrayList<WidgetRun> runs = new ArrayList<>();

    public RunGroup(WidgetRun run, int dir) {
        int i = index;
        this.groupIndex = i;
        index = i + 1;
        this.firstRun = run;
        this.lastRun = run;
        this.direction = dir;
    }

    private boolean defineTerminalWidget(WidgetRun run, int orientation) {
        if (!run.widget.isTerminalWidget[orientation]) {
            return false;
        }
        for (Dependency next : run.start.dependencies) {
            if (next instanceof DependencyNode) {
                DependencyNode dependencyNode = (DependencyNode) next;
                if (dependencyNode.run != run && dependencyNode == dependencyNode.run.start) {
                    if (run instanceof ChainRun) {
                        Iterator<WidgetRun> it = ((ChainRun) run).widgets.iterator();
                        while (it.hasNext()) {
                            defineTerminalWidget(it.next(), orientation);
                        }
                    } else if (!(run instanceof HelperReferences)) {
                        run.widget.isTerminalWidget[orientation] = false;
                    }
                    defineTerminalWidget(dependencyNode.run, orientation);
                }
            }
        }
        for (Dependency next2 : run.end.dependencies) {
            if (next2 instanceof DependencyNode) {
                DependencyNode dependencyNode2 = (DependencyNode) next2;
                if (dependencyNode2.run != run && dependencyNode2 == dependencyNode2.run.start) {
                    if (run instanceof ChainRun) {
                        Iterator<WidgetRun> it2 = ((ChainRun) run).widgets.iterator();
                        while (it2.hasNext()) {
                            defineTerminalWidget(it2.next(), orientation);
                        }
                    } else if (!(run instanceof HelperReferences)) {
                        run.widget.isTerminalWidget[orientation] = false;
                    }
                    defineTerminalWidget(dependencyNode2.run, orientation);
                }
            }
        }
        return false;
    }

    private long traverseEnd(DependencyNode node, long startPosition) {
        WidgetRun widgetRun = node.run;
        if (widgetRun instanceof HelperReferences) {
            return startPosition;
        }
        long j = startPosition;
        int size = node.dependencies.size();
        for (int i = 0; i < size; i++) {
            Dependency dependency = node.dependencies.get(i);
            if (dependency instanceof DependencyNode) {
                DependencyNode dependencyNode = (DependencyNode) dependency;
                if (dependencyNode.run != widgetRun) {
                    j = Math.min(j, traverseEnd(dependencyNode, ((long) dependencyNode.margin) + startPosition));
                }
            }
        }
        if (node != widgetRun.end) {
            return j;
        }
        long wrapDimension = widgetRun.getWrapDimension();
        return Math.min(Math.min(j, traverseEnd(widgetRun.start, startPosition - wrapDimension)), (startPosition - wrapDimension) - ((long) widgetRun.start.margin));
    }

    private long traverseStart(DependencyNode node, long startPosition) {
        WidgetRun widgetRun = node.run;
        if (widgetRun instanceof HelperReferences) {
            return startPosition;
        }
        long j = startPosition;
        int size = node.dependencies.size();
        for (int i = 0; i < size; i++) {
            Dependency dependency = node.dependencies.get(i);
            if (dependency instanceof DependencyNode) {
                DependencyNode dependencyNode = (DependencyNode) dependency;
                if (dependencyNode.run != widgetRun) {
                    j = Math.max(j, traverseStart(dependencyNode, ((long) dependencyNode.margin) + startPosition));
                }
            }
        }
        if (node != widgetRun.start) {
            return j;
        }
        long wrapDimension = widgetRun.getWrapDimension();
        return Math.max(Math.max(j, traverseStart(widgetRun.end, startPosition + wrapDimension)), (startPosition + wrapDimension) - ((long) widgetRun.end.margin));
    }

    public void add(WidgetRun run) {
        this.runs.add(run);
        this.lastRun = run;
    }

    public long computeWrapSize(ConstraintWidgetContainer container, int orientation) {
        ConstraintWidgetContainer constraintWidgetContainer = container;
        int i = orientation;
        WidgetRun widgetRun = this.firstRun;
        if (widgetRun instanceof ChainRun) {
            if (((ChainRun) widgetRun).orientation != i) {
                return 0;
            }
        } else if (i == 0) {
            if (!(widgetRun instanceof HorizontalWidgetRun)) {
                return 0;
            }
        } else if (!(widgetRun instanceof VerticalWidgetRun)) {
            return 0;
        }
        DependencyNode dependencyNode = i == 0 ? constraintWidgetContainer.horizontalRun.start : constraintWidgetContainer.verticalRun.start;
        DependencyNode dependencyNode2 = i == 0 ? constraintWidgetContainer.horizontalRun.end : constraintWidgetContainer.verticalRun.end;
        boolean contains = this.firstRun.start.targets.contains(dependencyNode);
        boolean contains2 = this.firstRun.end.targets.contains(dependencyNode2);
        long wrapDimension = this.firstRun.getWrapDimension();
        if (!contains || !contains2) {
            DependencyNode dependencyNode3 = dependencyNode;
            DependencyNode dependencyNode4 = dependencyNode2;
            if (contains) {
                return Math.max(traverseStart(this.firstRun.start, (long) this.firstRun.start.margin), ((long) this.firstRun.start.margin) + wrapDimension);
            }
            if (!contains2) {
                return (((long) this.firstRun.start.margin) + this.firstRun.getWrapDimension()) - ((long) this.firstRun.end.margin);
            }
            return Math.max(-traverseEnd(this.firstRun.end, (long) this.firstRun.end.margin), ((long) (-this.firstRun.end.margin)) + wrapDimension);
        }
        long traverseStart = traverseStart(this.firstRun.start, 0);
        long traverseEnd = traverseEnd(this.firstRun.end, 0);
        long j = traverseStart - wrapDimension;
        DependencyNode dependencyNode5 = dependencyNode;
        DependencyNode dependencyNode6 = dependencyNode2;
        if (j >= ((long) (-this.firstRun.end.margin))) {
            j += (long) this.firstRun.end.margin;
        }
        long j2 = traverseEnd;
        long j3 = ((-traverseEnd) - wrapDimension) - ((long) this.firstRun.start.margin);
        if (j3 >= ((long) this.firstRun.start.margin)) {
            j3 -= (long) this.firstRun.start.margin;
        }
        float biasPercent = this.firstRun.widget.getBiasPercent(i);
        long j4 = biasPercent > 0.0f ? (long) ((((float) j3) / biasPercent) + (((float) j) / (1.0f - biasPercent))) : 0;
        long j5 = (long) ((((float) j4) * biasPercent) + 0.5f);
        long j6 = j4;
        long j7 = j5;
        return (((long) this.firstRun.start.margin) + ((j5 + wrapDimension) + ((long) ((((float) j4) * (1.0f - biasPercent)) + 0.5f)))) - ((long) this.firstRun.end.margin);
    }

    public void defineTerminalWidgets(boolean horizontalCheck, boolean verticalCheck) {
        if (horizontalCheck) {
            WidgetRun widgetRun = this.firstRun;
            if (widgetRun instanceof HorizontalWidgetRun) {
                defineTerminalWidget(widgetRun, 0);
            }
        }
        if (verticalCheck) {
            WidgetRun widgetRun2 = this.firstRun;
            if (widgetRun2 instanceof VerticalWidgetRun) {
                defineTerminalWidget(widgetRun2, 1);
            }
        }
    }
}
