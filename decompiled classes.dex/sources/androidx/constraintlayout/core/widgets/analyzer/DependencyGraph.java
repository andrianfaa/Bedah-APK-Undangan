package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.widgets.Barrier;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.core.widgets.Guideline;
import androidx.constraintlayout.core.widgets.HelperWidget;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import okhttp3.HttpUrl;

public class DependencyGraph {
    private static final boolean USE_GROUPS = true;
    private ConstraintWidgetContainer container;
    private ConstraintWidgetContainer mContainer;
    ArrayList<RunGroup> mGroups = new ArrayList<>();
    private BasicMeasure.Measure mMeasure = new BasicMeasure.Measure();
    private BasicMeasure.Measurer mMeasurer = null;
    private boolean mNeedBuildGraph = USE_GROUPS;
    private boolean mNeedRedoMeasures = USE_GROUPS;
    private ArrayList<WidgetRun> mRuns = new ArrayList<>();
    private ArrayList<RunGroup> runGroups = new ArrayList<>();

    public DependencyGraph(ConstraintWidgetContainer container2) {
        this.container = container2;
        this.mContainer = container2;
    }

    private void applyGroup(DependencyNode node, int orientation, int direction, DependencyNode end, ArrayList<RunGroup> arrayList, RunGroup group) {
        RunGroup runGroup;
        int i = orientation;
        DependencyNode dependencyNode = end;
        WidgetRun widgetRun = node.run;
        if (widgetRun.runGroup != null || widgetRun == this.container.horizontalRun) {
            int i2 = direction;
            ArrayList<RunGroup> arrayList2 = arrayList;
        } else if (widgetRun == this.container.verticalRun) {
            int i3 = direction;
            ArrayList<RunGroup> arrayList3 = arrayList;
        } else {
            if (group == null) {
                RunGroup runGroup2 = new RunGroup(widgetRun, direction);
                arrayList.add(runGroup2);
                runGroup = runGroup2;
            } else {
                int i4 = direction;
                ArrayList<RunGroup> arrayList4 = arrayList;
                runGroup = group;
            }
            widgetRun.runGroup = runGroup;
            runGroup.add(widgetRun);
            for (Dependency next : widgetRun.start.dependencies) {
                if (next instanceof DependencyNode) {
                    Dependency dependency = next;
                    applyGroup((DependencyNode) next, orientation, 0, end, arrayList, runGroup);
                } else {
                    Dependency dependency2 = next;
                }
            }
            for (Dependency next2 : widgetRun.end.dependencies) {
                if (next2 instanceof DependencyNode) {
                    Dependency dependency3 = next2;
                    applyGroup((DependencyNode) next2, orientation, 1, end, arrayList, runGroup);
                } else {
                    Dependency dependency4 = next2;
                }
            }
            if (i == 1 && (widgetRun instanceof VerticalWidgetRun)) {
                for (Dependency next3 : ((VerticalWidgetRun) widgetRun).baseline.dependencies) {
                    if (next3 instanceof DependencyNode) {
                        Dependency dependency5 = next3;
                        applyGroup((DependencyNode) next3, orientation, 2, end, arrayList, runGroup);
                    } else {
                        Dependency dependency6 = next3;
                    }
                }
            }
            for (DependencyNode next4 : widgetRun.start.targets) {
                if (next4 == dependencyNode) {
                    runGroup.dual = USE_GROUPS;
                }
                DependencyNode dependencyNode2 = next4;
                applyGroup(next4, orientation, 0, end, arrayList, runGroup);
            }
            for (DependencyNode next5 : widgetRun.end.targets) {
                if (next5 == dependencyNode) {
                    runGroup.dual = USE_GROUPS;
                }
                DependencyNode dependencyNode3 = next5;
                applyGroup(next5, orientation, 1, end, arrayList, runGroup);
            }
            if (i == 1 && (widgetRun instanceof VerticalWidgetRun)) {
                for (DependencyNode applyGroup : ((VerticalWidgetRun) widgetRun).baseline.targets) {
                    applyGroup(applyGroup, orientation, 2, end, arrayList, runGroup);
                }
            }
        }
    }

    private boolean basicMeasureWidgets(ConstraintWidgetContainer constraintWidgetContainer) {
        ConstraintWidgetContainer constraintWidgetContainer2 = constraintWidgetContainer;
        Iterator it = constraintWidgetContainer2.mChildren.iterator();
        while (it.hasNext()) {
            ConstraintWidget constraintWidget = (ConstraintWidget) it.next();
            ConstraintWidget.DimensionBehaviour dimensionBehaviour = constraintWidget.mListDimensionBehaviors[0];
            ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = constraintWidget.mListDimensionBehaviors[1];
            if (constraintWidget.getVisibility() == 8) {
                constraintWidget.measured = USE_GROUPS;
            } else {
                if (constraintWidget.mMatchConstraintPercentWidth < 1.0f && dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    constraintWidget.mMatchConstraintDefaultWidth = 2;
                }
                if (constraintWidget.mMatchConstraintPercentHeight < 1.0f && dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    constraintWidget.mMatchConstraintDefaultHeight = 2;
                }
                if (constraintWidget.getDimensionRatio() > 0.0f) {
                    if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.FIXED)) {
                        constraintWidget.mMatchConstraintDefaultWidth = 3;
                    } else if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED)) {
                        constraintWidget.mMatchConstraintDefaultHeight = 3;
                    } else if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        if (constraintWidget.mMatchConstraintDefaultWidth == 0) {
                            constraintWidget.mMatchConstraintDefaultWidth = 3;
                        }
                        if (constraintWidget.mMatchConstraintDefaultHeight == 0) {
                            constraintWidget.mMatchConstraintDefaultHeight = 3;
                        }
                    }
                }
                ConstraintWidget.DimensionBehaviour dimensionBehaviour3 = (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultWidth == 1 && (constraintWidget.mLeft.mTarget == null || constraintWidget.mRight.mTarget == null)) ? ConstraintWidget.DimensionBehaviour.WRAP_CONTENT : dimensionBehaviour;
                ConstraintWidget.DimensionBehaviour dimensionBehaviour4 = (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultHeight == 1 && (constraintWidget.mTop.mTarget == null || constraintWidget.mBottom.mTarget == null)) ? ConstraintWidget.DimensionBehaviour.WRAP_CONTENT : dimensionBehaviour2;
                constraintWidget.horizontalRun.dimensionBehavior = dimensionBehaviour3;
                constraintWidget.horizontalRun.matchConstraintsType = constraintWidget.mMatchConstraintDefaultWidth;
                constraintWidget.verticalRun.dimensionBehavior = dimensionBehaviour4;
                constraintWidget.verticalRun.matchConstraintsType = constraintWidget.mMatchConstraintDefaultHeight;
                if ((dimensionBehaviour3 == ConstraintWidget.DimensionBehaviour.MATCH_PARENT || dimensionBehaviour3 == ConstraintWidget.DimensionBehaviour.FIXED || dimensionBehaviour3 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) && (dimensionBehaviour4 == ConstraintWidget.DimensionBehaviour.MATCH_PARENT || dimensionBehaviour4 == ConstraintWidget.DimensionBehaviour.FIXED || dimensionBehaviour4 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)) {
                    int width = constraintWidget.getWidth();
                    if (dimensionBehaviour3 == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                        width = (constraintWidgetContainer.getWidth() - constraintWidget.mLeft.mMargin) - constraintWidget.mRight.mMargin;
                        dimensionBehaviour3 = ConstraintWidget.DimensionBehaviour.FIXED;
                    }
                    int height = constraintWidget.getHeight();
                    if (dimensionBehaviour4 == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                        height = (constraintWidgetContainer.getHeight() - constraintWidget.mTop.mMargin) - constraintWidget.mBottom.mMargin;
                        dimensionBehaviour4 = ConstraintWidget.DimensionBehaviour.FIXED;
                    }
                    measure(constraintWidget, dimensionBehaviour3, width, dimensionBehaviour4, height);
                    constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
                    constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
                    constraintWidget.measured = USE_GROUPS;
                } else {
                    if (dimensionBehaviour3 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (dimensionBehaviour4 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour4 == ConstraintWidget.DimensionBehaviour.FIXED)) {
                        if (constraintWidget.mMatchConstraintDefaultWidth == 3) {
                            if (dimensionBehaviour4 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                                measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0);
                            }
                            int height2 = constraintWidget.getHeight();
                            measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, (int) ((((float) height2) * constraintWidget.mDimensionRatio) + 0.5f), ConstraintWidget.DimensionBehaviour.FIXED, height2);
                            constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
                            constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
                            constraintWidget.measured = USE_GROUPS;
                        } else if (constraintWidget.mMatchConstraintDefaultWidth == 1) {
                            measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, dimensionBehaviour4, 0);
                            constraintWidget.horizontalRun.dimension.wrapValue = constraintWidget.getWidth();
                        } else if (constraintWidget.mMatchConstraintDefaultWidth == 2) {
                            if (constraintWidgetContainer2.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED || constraintWidgetContainer2.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                                measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, (int) ((((float) constraintWidgetContainer.getWidth()) * constraintWidget.mMatchConstraintPercentWidth) + 0.5f), dimensionBehaviour4, constraintWidget.getHeight());
                                constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
                                constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
                                constraintWidget.measured = USE_GROUPS;
                            }
                        } else if (constraintWidget.mListAnchors[0].mTarget == null || constraintWidget.mListAnchors[1].mTarget == null) {
                            measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, dimensionBehaviour4, 0);
                            constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
                            constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
                            constraintWidget.measured = USE_GROUPS;
                        }
                    }
                    if (dimensionBehaviour4 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (dimensionBehaviour3 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour3 == ConstraintWidget.DimensionBehaviour.FIXED)) {
                        if (constraintWidget.mMatchConstraintDefaultHeight == 3) {
                            if (dimensionBehaviour3 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                                measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0);
                            }
                            int width2 = constraintWidget.getWidth();
                            float f = constraintWidget.mDimensionRatio;
                            if (constraintWidget.getDimensionRatioSide() == -1) {
                                f = 1.0f / f;
                            }
                            measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, width2, ConstraintWidget.DimensionBehaviour.FIXED, (int) ((((float) width2) * f) + 0.5f));
                            constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
                            constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
                            constraintWidget.measured = USE_GROUPS;
                        } else if (constraintWidget.mMatchConstraintDefaultHeight == 1) {
                            measure(constraintWidget, dimensionBehaviour3, 0, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0);
                            constraintWidget.verticalRun.dimension.wrapValue = constraintWidget.getHeight();
                        } else if (constraintWidget.mMatchConstraintDefaultHeight == 2) {
                            if (constraintWidgetContainer2.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED || constraintWidgetContainer2.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                                measure(constraintWidget, dimensionBehaviour3, constraintWidget.getWidth(), ConstraintWidget.DimensionBehaviour.FIXED, (int) ((((float) constraintWidgetContainer.getHeight()) * constraintWidget.mMatchConstraintPercentHeight) + 0.5f));
                                constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
                                constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
                                constraintWidget.measured = USE_GROUPS;
                            }
                        } else if (constraintWidget.mListAnchors[2].mTarget == null || constraintWidget.mListAnchors[3].mTarget == null) {
                            measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, dimensionBehaviour4, 0);
                            constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
                            constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
                            constraintWidget.measured = USE_GROUPS;
                        }
                    }
                    if (dimensionBehaviour3 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && dimensionBehaviour4 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        if (constraintWidget.mMatchConstraintDefaultWidth == 1 || constraintWidget.mMatchConstraintDefaultHeight == 1) {
                            measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0);
                            constraintWidget.horizontalRun.dimension.wrapValue = constraintWidget.getWidth();
                            constraintWidget.verticalRun.dimension.wrapValue = constraintWidget.getHeight();
                        } else if (constraintWidget.mMatchConstraintDefaultHeight == 2 && constraintWidget.mMatchConstraintDefaultWidth == 2 && constraintWidgetContainer2.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED && constraintWidgetContainer2.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED) {
                            measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, (int) ((((float) constraintWidgetContainer.getWidth()) * constraintWidget.mMatchConstraintPercentWidth) + 0.5f), ConstraintWidget.DimensionBehaviour.FIXED, (int) ((((float) constraintWidgetContainer.getHeight()) * constraintWidget.mMatchConstraintPercentHeight) + 0.5f));
                            constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
                            constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
                            constraintWidget.measured = USE_GROUPS;
                        }
                    }
                }
            }
        }
        return false;
    }

    private int computeWrap(ConstraintWidgetContainer container2, int orientation) {
        int size = this.mGroups.size();
        long j = 0;
        for (int i = 0; i < size; i++) {
            j = Math.max(j, this.mGroups.get(i).computeWrapSize(container2, orientation));
        }
        return (int) j;
    }

    private void displayGraph() {
        String str = "digraph {\n";
        Iterator<WidgetRun> it = this.mRuns.iterator();
        while (it.hasNext()) {
            str = generateDisplayGraph(it.next(), str);
        }
        System.out.println("content:<<\n" + (str + "\n}\n") + "\n>>");
    }

    private void findGroup(WidgetRun run, int orientation, ArrayList<RunGroup> arrayList) {
        for (Dependency next : run.start.dependencies) {
            if (next instanceof DependencyNode) {
                applyGroup((DependencyNode) next, orientation, 0, run.end, arrayList, (RunGroup) null);
            } else if (next instanceof WidgetRun) {
                applyGroup(((WidgetRun) next).start, orientation, 0, run.end, arrayList, (RunGroup) null);
            }
        }
        for (Dependency next2 : run.end.dependencies) {
            if (next2 instanceof DependencyNode) {
                applyGroup((DependencyNode) next2, orientation, 1, run.start, arrayList, (RunGroup) null);
            } else if (next2 instanceof WidgetRun) {
                applyGroup(((WidgetRun) next2).end, orientation, 1, run.start, arrayList, (RunGroup) null);
            }
        }
        if (orientation == 1) {
            for (Dependency next3 : ((VerticalWidgetRun) run).baseline.dependencies) {
                if (next3 instanceof DependencyNode) {
                    applyGroup((DependencyNode) next3, orientation, 2, (DependencyNode) null, arrayList, (RunGroup) null);
                }
            }
        }
    }

    private String generateChainDisplayGraph(ChainRun chain, String content) {
        int i = chain.orientation;
        StringBuilder sb = new StringBuilder("subgraph ");
        sb.append("cluster_");
        sb.append(chain.widget.getDebugName());
        if (i == 0) {
            sb.append("_h");
        } else {
            sb.append("_v");
        }
        sb.append(" {\n");
        String str = HttpUrl.FRAGMENT_ENCODE_SET;
        Iterator<WidgetRun> it = chain.widgets.iterator();
        while (it.hasNext()) {
            WidgetRun next = it.next();
            sb.append(next.widget.getDebugName());
            if (i == 0) {
                sb.append("_HORIZONTAL");
            } else {
                sb.append("_VERTICAL");
            }
            sb.append(";\n");
            str = generateDisplayGraph(next, str);
        }
        sb.append("}\n");
        return content + str + sb;
    }

    private String generateDisplayGraph(WidgetRun root, String content) {
        DependencyNode dependencyNode = root.start;
        DependencyNode dependencyNode2 = root.end;
        StringBuilder sb = new StringBuilder(content);
        if (!(root instanceof HelperReferences) && dependencyNode.dependencies.isEmpty() && (dependencyNode2.dependencies.isEmpty() && dependencyNode.targets.isEmpty()) && dependencyNode2.targets.isEmpty()) {
            return content;
        }
        sb.append(nodeDefinition(root));
        boolean isCenteredConnection = isCenteredConnection(dependencyNode, dependencyNode2);
        String content2 = generateDisplayNode(dependencyNode2, isCenteredConnection, generateDisplayNode(dependencyNode, isCenteredConnection, content));
        if (root instanceof VerticalWidgetRun) {
            content2 = generateDisplayNode(((VerticalWidgetRun) root).baseline, isCenteredConnection, content2);
        }
        if ((root instanceof HorizontalWidgetRun) || ((root instanceof ChainRun) && ((ChainRun) root).orientation == 0)) {
            ConstraintWidget.DimensionBehaviour horizontalDimensionBehaviour = root.widget.getHorizontalDimensionBehaviour();
            if (horizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED || horizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                if (!dependencyNode.targets.isEmpty() && dependencyNode2.targets.isEmpty()) {
                    sb.append("\n");
                    sb.append(dependencyNode2.name());
                    sb.append(" -> ");
                    sb.append(dependencyNode.name());
                    sb.append("\n");
                } else if (dependencyNode.targets.isEmpty() && !dependencyNode2.targets.isEmpty()) {
                    sb.append("\n");
                    sb.append(dependencyNode.name());
                    sb.append(" -> ");
                    sb.append(dependencyNode2.name());
                    sb.append("\n");
                }
            } else if (horizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && root.widget.getDimensionRatio() > 0.0f) {
                sb.append("\n");
                sb.append(root.widget.getDebugName());
                sb.append("_HORIZONTAL -> ");
                sb.append(root.widget.getDebugName());
                sb.append("_VERTICAL;\n");
            }
        } else if ((root instanceof VerticalWidgetRun) || ((root instanceof ChainRun) && ((ChainRun) root).orientation == 1)) {
            ConstraintWidget.DimensionBehaviour verticalDimensionBehaviour = root.widget.getVerticalDimensionBehaviour();
            if (verticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED || verticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                if (!dependencyNode.targets.isEmpty() && dependencyNode2.targets.isEmpty()) {
                    sb.append("\n");
                    sb.append(dependencyNode2.name());
                    sb.append(" -> ");
                    sb.append(dependencyNode.name());
                    sb.append("\n");
                } else if (dependencyNode.targets.isEmpty() && !dependencyNode2.targets.isEmpty()) {
                    sb.append("\n");
                    sb.append(dependencyNode.name());
                    sb.append(" -> ");
                    sb.append(dependencyNode2.name());
                    sb.append("\n");
                }
            } else if (verticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && root.widget.getDimensionRatio() > 0.0f) {
                sb.append("\n");
                sb.append(root.widget.getDebugName());
                sb.append("_VERTICAL -> ");
                sb.append(root.widget.getDebugName());
                sb.append("_HORIZONTAL;\n");
            }
        }
        return root instanceof ChainRun ? generateChainDisplayGraph((ChainRun) root, content2) : sb.toString();
    }

    private String generateDisplayNode(DependencyNode node, boolean centeredConnection, String content) {
        StringBuilder sb = new StringBuilder(content);
        for (DependencyNode name : node.targets) {
            String str = ("\n" + node.name()) + " -> " + name.name();
            if (node.margin > 0 || centeredConnection || (node.run instanceof HelperReferences)) {
                String str2 = str + "[";
                if (node.margin > 0) {
                    str2 = str2 + "label=\"" + node.margin + "\"";
                    if (centeredConnection) {
                        str2 = str2 + ",";
                    }
                }
                if (centeredConnection) {
                    str2 = str2 + " style=dashed ";
                }
                if (node.run instanceof HelperReferences) {
                    str2 = str2 + " style=bold,color=gray ";
                }
                str = str2 + "]";
            }
            sb.append(str + "\n");
        }
        return sb.toString();
    }

    private boolean isCenteredConnection(DependencyNode start, DependencyNode end) {
        int i = 0;
        int i2 = 0;
        for (DependencyNode dependencyNode : start.targets) {
            if (dependencyNode != end) {
                i++;
            }
        }
        for (DependencyNode dependencyNode2 : end.targets) {
            if (dependencyNode2 != start) {
                i2++;
            }
        }
        if (i <= 0 || i2 <= 0) {
            return false;
        }
        return USE_GROUPS;
    }

    private void measure(ConstraintWidget widget, ConstraintWidget.DimensionBehaviour horizontalBehavior, int horizontalDimension, ConstraintWidget.DimensionBehaviour verticalBehavior, int verticalDimension) {
        this.mMeasure.horizontalBehavior = horizontalBehavior;
        this.mMeasure.verticalBehavior = verticalBehavior;
        this.mMeasure.horizontalDimension = horizontalDimension;
        this.mMeasure.verticalDimension = verticalDimension;
        this.mMeasurer.measure(widget, this.mMeasure);
        widget.setWidth(this.mMeasure.measuredWidth);
        widget.setHeight(this.mMeasure.measuredHeight);
        widget.setHasBaseline(this.mMeasure.measuredHasBaseline);
        widget.setBaselineDistance(this.mMeasure.measuredBaseline);
    }

    private String nodeDefinition(WidgetRun run) {
        boolean z = run instanceof VerticalWidgetRun;
        String debugName = run.widget.getDebugName();
        StringBuilder sb = new StringBuilder(debugName);
        ConstraintWidget constraintWidget = run.widget;
        ConstraintWidget.DimensionBehaviour horizontalDimensionBehaviour = !z ? constraintWidget.getHorizontalDimensionBehaviour() : constraintWidget.getVerticalDimensionBehaviour();
        RunGroup runGroup = run.runGroup;
        if (!z) {
            sb.append("_HORIZONTAL");
        } else {
            sb.append("_VERTICAL");
        }
        sb.append(" [shape=none, label=<");
        sb.append("<TABLE BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"2\">");
        sb.append("  <TR>");
        if (!z) {
            sb.append("    <TD ");
            if (run.start.resolved) {
                sb.append(" BGCOLOR=\"green\"");
            }
            sb.append(" PORT=\"LEFT\" BORDER=\"1\">L</TD>");
        } else {
            sb.append("    <TD ");
            if (run.start.resolved) {
                sb.append(" BGCOLOR=\"green\"");
            }
            sb.append(" PORT=\"TOP\" BORDER=\"1\">T</TD>");
        }
        sb.append("    <TD BORDER=\"1\" ");
        if (run.dimension.resolved && !run.widget.measured) {
            sb.append(" BGCOLOR=\"green\" ");
        } else if (run.dimension.resolved) {
            sb.append(" BGCOLOR=\"lightgray\" ");
        } else if (run.widget.measured) {
            sb.append(" BGCOLOR=\"yellow\" ");
        }
        if (horizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            sb.append("style=\"dashed\"");
        }
        sb.append(">");
        sb.append(debugName);
        if (runGroup != null) {
            sb.append(" [");
            sb.append(runGroup.groupIndex + 1);
            sb.append("/");
            sb.append(RunGroup.index);
            sb.append("]");
        }
        sb.append(" </TD>");
        if (!z) {
            sb.append("    <TD ");
            if (run.end.resolved) {
                sb.append(" BGCOLOR=\"green\"");
            }
            sb.append(" PORT=\"RIGHT\" BORDER=\"1\">R</TD>");
        } else {
            sb.append("    <TD ");
            if (((VerticalWidgetRun) run).baseline.resolved) {
                sb.append(" BGCOLOR=\"green\"");
            }
            sb.append(" PORT=\"BASELINE\" BORDER=\"1\">b</TD>");
            sb.append("    <TD ");
            if (run.end.resolved) {
                sb.append(" BGCOLOR=\"green\"");
            }
            sb.append(" PORT=\"BOTTOM\" BORDER=\"1\">B</TD>");
        }
        sb.append("  </TR></TABLE>");
        sb.append(">];\n");
        return sb.toString();
    }

    public void buildGraph() {
        buildGraph(this.mRuns);
        this.mGroups.clear();
        RunGroup.index = 0;
        findGroup(this.container.horizontalRun, 0, this.mGroups);
        findGroup(this.container.verticalRun, 1, this.mGroups);
        this.mNeedBuildGraph = false;
    }

    public void buildGraph(ArrayList<WidgetRun> arrayList) {
        arrayList.clear();
        this.mContainer.horizontalRun.clear();
        this.mContainer.verticalRun.clear();
        arrayList.add(this.mContainer.horizontalRun);
        arrayList.add(this.mContainer.verticalRun);
        HashSet hashSet = null;
        Iterator it = this.mContainer.mChildren.iterator();
        while (it.hasNext()) {
            ConstraintWidget constraintWidget = (ConstraintWidget) it.next();
            if (constraintWidget instanceof Guideline) {
                arrayList.add(new GuidelineReference(constraintWidget));
            } else {
                if (constraintWidget.isInHorizontalChain()) {
                    if (constraintWidget.horizontalChainRun == null) {
                        constraintWidget.horizontalChainRun = new ChainRun(constraintWidget, 0);
                    }
                    if (hashSet == null) {
                        hashSet = new HashSet();
                    }
                    hashSet.add(constraintWidget.horizontalChainRun);
                } else {
                    arrayList.add(constraintWidget.horizontalRun);
                }
                if (constraintWidget.isInVerticalChain()) {
                    if (constraintWidget.verticalChainRun == null) {
                        constraintWidget.verticalChainRun = new ChainRun(constraintWidget, 1);
                    }
                    if (hashSet == null) {
                        hashSet = new HashSet();
                    }
                    hashSet.add(constraintWidget.verticalChainRun);
                } else {
                    arrayList.add(constraintWidget.verticalRun);
                }
                if (constraintWidget instanceof HelperWidget) {
                    arrayList.add(new HelperReferences(constraintWidget));
                }
            }
        }
        if (hashSet != null) {
            arrayList.addAll(hashSet);
        }
        Iterator<WidgetRun> it2 = arrayList.iterator();
        while (it2.hasNext()) {
            it2.next().clear();
        }
        Iterator<WidgetRun> it3 = arrayList.iterator();
        while (it3.hasNext()) {
            WidgetRun next = it3.next();
            if (next.widget != this.mContainer) {
                next.apply();
            }
        }
    }

    public void defineTerminalWidgets(ConstraintWidget.DimensionBehaviour horizontalBehavior, ConstraintWidget.DimensionBehaviour verticalBehavior) {
        if (this.mNeedBuildGraph) {
            buildGraph();
            boolean z = false;
            Iterator it = this.container.mChildren.iterator();
            while (it.hasNext()) {
                ConstraintWidget constraintWidget = (ConstraintWidget) it.next();
                constraintWidget.isTerminalWidget[0] = USE_GROUPS;
                constraintWidget.isTerminalWidget[1] = USE_GROUPS;
                if (constraintWidget instanceof Barrier) {
                    z = USE_GROUPS;
                }
            }
            if (!z) {
                Iterator<RunGroup> it2 = this.mGroups.iterator();
                while (it2.hasNext()) {
                    it2.next().defineTerminalWidgets(horizontalBehavior == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, verticalBehavior == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                }
            }
        }
    }

    public boolean directMeasure(boolean optimizeWrap) {
        boolean optimizeWrap2 = optimizeWrap & USE_GROUPS;
        if (this.mNeedBuildGraph || this.mNeedRedoMeasures) {
            Iterator it = this.container.mChildren.iterator();
            while (it.hasNext()) {
                ConstraintWidget constraintWidget = (ConstraintWidget) it.next();
                constraintWidget.ensureWidgetRuns();
                constraintWidget.measured = false;
                constraintWidget.horizontalRun.reset();
                constraintWidget.verticalRun.reset();
            }
            this.container.ensureWidgetRuns();
            this.container.measured = false;
            this.container.horizontalRun.reset();
            this.container.verticalRun.reset();
            this.mNeedRedoMeasures = false;
        }
        if (basicMeasureWidgets(this.mContainer)) {
            return false;
        }
        this.container.setX(0);
        this.container.setY(0);
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = this.container.getDimensionBehaviour(0);
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = this.container.getDimensionBehaviour(1);
        if (this.mNeedBuildGraph) {
            buildGraph();
        }
        int x = this.container.getX();
        int y = this.container.getY();
        this.container.horizontalRun.start.resolve(x);
        this.container.verticalRun.start.resolve(y);
        measureWidgets();
        if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            if (optimizeWrap2) {
                Iterator<WidgetRun> it2 = this.mRuns.iterator();
                while (true) {
                    if (it2.hasNext()) {
                        if (!it2.next().supportsWrapComputation()) {
                            optimizeWrap2 = false;
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            if (optimizeWrap2 && dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                this.container.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                ConstraintWidgetContainer constraintWidgetContainer = this.container;
                constraintWidgetContainer.setWidth(computeWrap(constraintWidgetContainer, 0));
                this.container.horizontalRun.dimension.resolve(this.container.getWidth());
            }
            if (optimizeWrap2 && dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                this.container.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                ConstraintWidgetContainer constraintWidgetContainer2 = this.container;
                constraintWidgetContainer2.setHeight(computeWrap(constraintWidgetContainer2, 1));
                this.container.verticalRun.dimension.resolve(this.container.getHeight());
            }
        }
        boolean z = false;
        if (this.container.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED || this.container.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            int width = this.container.getWidth() + x;
            this.container.horizontalRun.end.resolve(width);
            this.container.horizontalRun.dimension.resolve(width - x);
            measureWidgets();
            if (this.container.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED || this.container.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                int height = this.container.getHeight() + y;
                this.container.verticalRun.end.resolve(height);
                this.container.verticalRun.dimension.resolve(height - y);
            }
            measureWidgets();
            z = USE_GROUPS;
        }
        Iterator<WidgetRun> it3 = this.mRuns.iterator();
        while (it3.hasNext()) {
            WidgetRun next = it3.next();
            if (next.widget != this.container || next.resolved) {
                next.applyToWidget();
            }
        }
        boolean z2 = USE_GROUPS;
        Iterator<WidgetRun> it4 = this.mRuns.iterator();
        while (true) {
            if (!it4.hasNext()) {
                break;
            }
            WidgetRun next2 = it4.next();
            if (z || next2.widget != this.container) {
                if (next2.start.resolved) {
                    if (next2.end.resolved || (next2 instanceof GuidelineReference)) {
                        if (!next2.dimension.resolved && !(next2 instanceof ChainRun) && !(next2 instanceof GuidelineReference)) {
                            z2 = false;
                            break;
                        }
                    } else {
                        z2 = false;
                        break;
                    }
                } else {
                    z2 = false;
                    break;
                }
            }
        }
        this.container.setHorizontalDimensionBehaviour(dimensionBehaviour);
        this.container.setVerticalDimensionBehaviour(dimensionBehaviour2);
        return z2;
    }

    public boolean directMeasureSetup(boolean optimizeWrap) {
        if (this.mNeedBuildGraph) {
            Iterator it = this.container.mChildren.iterator();
            while (it.hasNext()) {
                ConstraintWidget constraintWidget = (ConstraintWidget) it.next();
                constraintWidget.ensureWidgetRuns();
                constraintWidget.measured = false;
                constraintWidget.horizontalRun.dimension.resolved = false;
                constraintWidget.horizontalRun.resolved = false;
                constraintWidget.horizontalRun.reset();
                constraintWidget.verticalRun.dimension.resolved = false;
                constraintWidget.verticalRun.resolved = false;
                constraintWidget.verticalRun.reset();
            }
            this.container.ensureWidgetRuns();
            this.container.measured = false;
            this.container.horizontalRun.dimension.resolved = false;
            this.container.horizontalRun.resolved = false;
            this.container.horizontalRun.reset();
            this.container.verticalRun.dimension.resolved = false;
            this.container.verticalRun.resolved = false;
            this.container.verticalRun.reset();
            buildGraph();
        }
        if (basicMeasureWidgets(this.mContainer)) {
            return false;
        }
        this.container.setX(0);
        this.container.setY(0);
        this.container.horizontalRun.start.resolve(0);
        this.container.verticalRun.start.resolve(0);
        return USE_GROUPS;
    }

    public boolean directMeasureWithOrientation(boolean optimizeWrap, int orientation) {
        boolean optimizeWrap2 = optimizeWrap & USE_GROUPS;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = this.container.getDimensionBehaviour(0);
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = this.container.getDimensionBehaviour(1);
        int x = this.container.getX();
        int y = this.container.getY();
        if (optimizeWrap2 && (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)) {
            Iterator<WidgetRun> it = this.mRuns.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                WidgetRun next = it.next();
                if (next.orientation == orientation && !next.supportsWrapComputation()) {
                    optimizeWrap2 = false;
                    break;
                }
            }
            if (orientation == 0) {
                if (optimizeWrap2 && dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    this.container.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                    ConstraintWidgetContainer constraintWidgetContainer = this.container;
                    constraintWidgetContainer.setWidth(computeWrap(constraintWidgetContainer, 0));
                    this.container.horizontalRun.dimension.resolve(this.container.getWidth());
                }
            } else if (optimizeWrap2 && dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                this.container.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                ConstraintWidgetContainer constraintWidgetContainer2 = this.container;
                constraintWidgetContainer2.setHeight(computeWrap(constraintWidgetContainer2, 1));
                this.container.verticalRun.dimension.resolve(this.container.getHeight());
            }
        }
        boolean z = false;
        if (orientation == 0) {
            if (this.container.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED || this.container.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                int width = this.container.getWidth() + x;
                this.container.horizontalRun.end.resolve(width);
                this.container.horizontalRun.dimension.resolve(width - x);
                z = USE_GROUPS;
            }
        } else if (this.container.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED || this.container.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            int height = this.container.getHeight() + y;
            this.container.verticalRun.end.resolve(height);
            this.container.verticalRun.dimension.resolve(height - y);
            z = USE_GROUPS;
        }
        measureWidgets();
        Iterator<WidgetRun> it2 = this.mRuns.iterator();
        while (it2.hasNext()) {
            WidgetRun next2 = it2.next();
            if (next2.orientation == orientation && (next2.widget != this.container || next2.resolved)) {
                next2.applyToWidget();
            }
        }
        boolean z2 = USE_GROUPS;
        Iterator<WidgetRun> it3 = this.mRuns.iterator();
        while (true) {
            if (!it3.hasNext()) {
                break;
            }
            WidgetRun next3 = it3.next();
            if (next3.orientation == orientation && (z || next3.widget != this.container)) {
                if (next3.start.resolved) {
                    if (next3.end.resolved) {
                        if (!(next3 instanceof ChainRun) && !next3.dimension.resolved) {
                            z2 = false;
                            break;
                        }
                    } else {
                        z2 = false;
                        break;
                    }
                } else {
                    z2 = false;
                    break;
                }
            }
        }
        this.container.setHorizontalDimensionBehaviour(dimensionBehaviour);
        this.container.setVerticalDimensionBehaviour(dimensionBehaviour2);
        return z2;
    }

    public void invalidateGraph() {
        this.mNeedBuildGraph = USE_GROUPS;
    }

    public void invalidateMeasures() {
        this.mNeedRedoMeasures = USE_GROUPS;
    }

    public void measureWidgets() {
        Iterator it = this.container.mChildren.iterator();
        while (it.hasNext()) {
            ConstraintWidget constraintWidget = (ConstraintWidget) it.next();
            if (!constraintWidget.measured) {
                boolean z = false;
                ConstraintWidget.DimensionBehaviour dimensionBehaviour = constraintWidget.mListDimensionBehaviors[0];
                ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = constraintWidget.mListDimensionBehaviors[1];
                int i = constraintWidget.mMatchConstraintDefaultWidth;
                int i2 = constraintWidget.mMatchConstraintDefaultHeight;
                boolean z2 = dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && i == 1);
                if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && i2 == 1)) {
                    z = true;
                }
                boolean z3 = z;
                boolean z4 = constraintWidget.horizontalRun.dimension.resolved;
                boolean z5 = constraintWidget.verticalRun.dimension.resolved;
                if (!z4 || !z5) {
                    boolean z6 = z5;
                    if (z4 && z3) {
                        measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, constraintWidget.horizontalRun.dimension.value, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, constraintWidget.verticalRun.dimension.value);
                        if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                            constraintWidget.verticalRun.dimension.wrapValue = constraintWidget.getHeight();
                        } else {
                            constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
                            constraintWidget.measured = USE_GROUPS;
                        }
                    } else if (z6 && z2) {
                        measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, constraintWidget.horizontalRun.dimension.value, ConstraintWidget.DimensionBehaviour.FIXED, constraintWidget.verticalRun.dimension.value);
                        if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                            constraintWidget.horizontalRun.dimension.wrapValue = constraintWidget.getWidth();
                        } else {
                            constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
                            constraintWidget.measured = USE_GROUPS;
                        }
                    }
                } else {
                    boolean z7 = z5;
                    boolean z8 = z4;
                    measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, constraintWidget.horizontalRun.dimension.value, ConstraintWidget.DimensionBehaviour.FIXED, constraintWidget.verticalRun.dimension.value);
                    constraintWidget.measured = USE_GROUPS;
                }
                if (constraintWidget.measured && constraintWidget.verticalRun.baselineDimension != null) {
                    constraintWidget.verticalRun.baselineDimension.resolve(constraintWidget.getBaselineDistance());
                }
            }
        }
    }

    public void setMeasurer(BasicMeasure.Measurer measurer) {
        this.mMeasurer = measurer;
    }
}
