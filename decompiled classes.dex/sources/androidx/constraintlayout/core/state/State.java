package androidx.constraintlayout.core.state;

import androidx.constraintlayout.core.state.helpers.AlignHorizontallyReference;
import androidx.constraintlayout.core.state.helpers.AlignVerticallyReference;
import androidx.constraintlayout.core.state.helpers.BarrierReference;
import androidx.constraintlayout.core.state.helpers.GuidelineReference;
import androidx.constraintlayout.core.state.helpers.HorizontalChainReference;
import androidx.constraintlayout.core.state.helpers.VerticalChainReference;
import java.util.ArrayList;
import java.util.HashMap;

public class State {
    static final int CONSTRAINT_RATIO = 2;
    static final int CONSTRAINT_SPREAD = 0;
    static final int CONSTRAINT_WRAP = 1;
    public static final Integer PARENT = 0;
    static final int UNKNOWN = -1;
    protected HashMap<Object, HelperReference> mHelperReferences = new HashMap<>();
    public final ConstraintReference mParent;
    protected HashMap<Object, Reference> mReferences = new HashMap<>();
    HashMap<String, ArrayList<String>> mTags = new HashMap<>();
    private int numHelpers;

    /* renamed from: androidx.constraintlayout.core.state.State$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$core$state$State$Helper;

        static {
            int[] iArr = new int[Helper.values().length];
            $SwitchMap$androidx$constraintlayout$core$state$State$Helper = iArr;
            try {
                iArr[Helper.HORIZONTAL_CHAIN.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Helper[Helper.VERTICAL_CHAIN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Helper[Helper.ALIGN_HORIZONTALLY.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Helper[Helper.ALIGN_VERTICALLY.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$state$State$Helper[Helper.BARRIER.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public enum Chain {
        SPREAD,
        SPREAD_INSIDE,
        PACKED
    }

    public enum Constraint {
        LEFT_TO_LEFT,
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT,
        RIGHT_TO_RIGHT,
        START_TO_START,
        START_TO_END,
        END_TO_START,
        END_TO_END,
        TOP_TO_TOP,
        TOP_TO_BOTTOM,
        BOTTOM_TO_TOP,
        BOTTOM_TO_BOTTOM,
        BASELINE_TO_BASELINE,
        BASELINE_TO_TOP,
        BASELINE_TO_BOTTOM,
        CENTER_HORIZONTALLY,
        CENTER_VERTICALLY,
        CIRCULAR_CONSTRAINT
    }

    public enum Direction {
        LEFT,
        RIGHT,
        START,
        END,
        TOP,
        BOTTOM
    }

    public enum Helper {
        HORIZONTAL_CHAIN,
        VERTICAL_CHAIN,
        ALIGN_HORIZONTALLY,
        ALIGN_VERTICALLY,
        BARRIER,
        LAYER,
        FLOW
    }

    public State() {
        ConstraintReference constraintReference = new ConstraintReference(this);
        this.mParent = constraintReference;
        this.numHelpers = 0;
        this.mReferences.put(PARENT, constraintReference);
    }

    private String createHelperKey() {
        StringBuilder append = new StringBuilder().append("__HELPER_KEY_");
        int i = this.numHelpers;
        this.numHelpers = i + 1;
        return append.append(i).append("__").toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:50:0x014d, code lost:
        r3 = (androidx.constraintlayout.core.state.HelperReference) r2.getFacade();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void apply(androidx.constraintlayout.core.widgets.ConstraintWidgetContainer r12) {
        /*
            r11 = this;
            r12.removeAllChildren()
            androidx.constraintlayout.core.state.ConstraintReference r0 = r11.mParent
            androidx.constraintlayout.core.state.Dimension r0 = r0.getWidth()
            r1 = 0
            r0.apply(r11, r12, r1)
            androidx.constraintlayout.core.state.ConstraintReference r0 = r11.mParent
            androidx.constraintlayout.core.state.Dimension r0 = r0.getHeight()
            r1 = 1
            r0.apply(r11, r12, r1)
            java.util.HashMap<java.lang.Object, androidx.constraintlayout.core.state.HelperReference> r0 = r11.mHelperReferences
            java.util.Set r0 = r0.keySet()
            java.util.Iterator r0 = r0.iterator()
        L_0x0021:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x004b
            java.lang.Object r1 = r0.next()
            java.util.HashMap<java.lang.Object, androidx.constraintlayout.core.state.HelperReference> r2 = r11.mHelperReferences
            java.lang.Object r2 = r2.get(r1)
            androidx.constraintlayout.core.state.HelperReference r2 = (androidx.constraintlayout.core.state.HelperReference) r2
            androidx.constraintlayout.core.widgets.HelperWidget r3 = r2.getHelperWidget()
            if (r3 == 0) goto L_0x004a
            java.util.HashMap<java.lang.Object, androidx.constraintlayout.core.state.Reference> r4 = r11.mReferences
            java.lang.Object r4 = r4.get(r1)
            androidx.constraintlayout.core.state.Reference r4 = (androidx.constraintlayout.core.state.Reference) r4
            if (r4 != 0) goto L_0x0047
            androidx.constraintlayout.core.state.ConstraintReference r4 = r11.constraints(r1)
        L_0x0047:
            r4.setConstraintWidget(r3)
        L_0x004a:
            goto L_0x0021
        L_0x004b:
            java.util.HashMap<java.lang.Object, androidx.constraintlayout.core.state.Reference> r0 = r11.mReferences
            java.util.Set r0 = r0.keySet()
            java.util.Iterator r0 = r0.iterator()
        L_0x0055:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0091
            java.lang.Object r1 = r0.next()
            java.util.HashMap<java.lang.Object, androidx.constraintlayout.core.state.Reference> r2 = r11.mReferences
            java.lang.Object r2 = r2.get(r1)
            androidx.constraintlayout.core.state.Reference r2 = (androidx.constraintlayout.core.state.Reference) r2
            androidx.constraintlayout.core.state.ConstraintReference r3 = r11.mParent
            if (r2 == r3) goto L_0x0090
            androidx.constraintlayout.core.state.helpers.Facade r3 = r2.getFacade()
            boolean r3 = r3 instanceof androidx.constraintlayout.core.state.HelperReference
            if (r3 == 0) goto L_0x0090
            androidx.constraintlayout.core.state.helpers.Facade r3 = r2.getFacade()
            androidx.constraintlayout.core.state.HelperReference r3 = (androidx.constraintlayout.core.state.HelperReference) r3
            androidx.constraintlayout.core.widgets.HelperWidget r3 = r3.getHelperWidget()
            if (r3 == 0) goto L_0x0090
            java.util.HashMap<java.lang.Object, androidx.constraintlayout.core.state.Reference> r4 = r11.mReferences
            java.lang.Object r4 = r4.get(r1)
            androidx.constraintlayout.core.state.Reference r4 = (androidx.constraintlayout.core.state.Reference) r4
            if (r4 != 0) goto L_0x008d
            androidx.constraintlayout.core.state.ConstraintReference r4 = r11.constraints(r1)
        L_0x008d:
            r4.setConstraintWidget(r3)
        L_0x0090:
            goto L_0x0055
        L_0x0091:
            java.util.HashMap<java.lang.Object, androidx.constraintlayout.core.state.Reference> r0 = r11.mReferences
            java.util.Set r0 = r0.keySet()
            java.util.Iterator r0 = r0.iterator()
        L_0x009b:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x00d7
            java.lang.Object r1 = r0.next()
            java.util.HashMap<java.lang.Object, androidx.constraintlayout.core.state.Reference> r2 = r11.mReferences
            java.lang.Object r2 = r2.get(r1)
            androidx.constraintlayout.core.state.Reference r2 = (androidx.constraintlayout.core.state.Reference) r2
            androidx.constraintlayout.core.state.ConstraintReference r3 = r11.mParent
            if (r2 == r3) goto L_0x00d3
            androidx.constraintlayout.core.widgets.ConstraintWidget r3 = r2.getConstraintWidget()
            java.lang.Object r4 = r2.getKey()
            java.lang.String r4 = r4.toString()
            r3.setDebugName(r4)
            r4 = 0
            r3.setParent(r4)
            androidx.constraintlayout.core.state.helpers.Facade r4 = r2.getFacade()
            boolean r4 = r4 instanceof androidx.constraintlayout.core.state.helpers.GuidelineReference
            if (r4 == 0) goto L_0x00cf
            r2.apply()
        L_0x00cf:
            r12.add((androidx.constraintlayout.core.widgets.ConstraintWidget) r3)
            goto L_0x00d6
        L_0x00d3:
            r2.setConstraintWidget(r12)
        L_0x00d6:
            goto L_0x009b
        L_0x00d7:
            java.util.HashMap<java.lang.Object, androidx.constraintlayout.core.state.HelperReference> r0 = r11.mHelperReferences
            java.util.Set r0 = r0.keySet()
            java.util.Iterator r0 = r0.iterator()
        L_0x00e1:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0125
            java.lang.Object r1 = r0.next()
            java.util.HashMap<java.lang.Object, androidx.constraintlayout.core.state.HelperReference> r2 = r11.mHelperReferences
            java.lang.Object r2 = r2.get(r1)
            androidx.constraintlayout.core.state.HelperReference r2 = (androidx.constraintlayout.core.state.HelperReference) r2
            androidx.constraintlayout.core.widgets.HelperWidget r3 = r2.getHelperWidget()
            if (r3 == 0) goto L_0x0121
            java.util.ArrayList<java.lang.Object> r4 = r2.mReferences
            java.util.Iterator r4 = r4.iterator()
        L_0x00ff:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x011d
            java.lang.Object r5 = r4.next()
            java.util.HashMap<java.lang.Object, androidx.constraintlayout.core.state.Reference> r6 = r11.mReferences
            java.lang.Object r6 = r6.get(r5)
            androidx.constraintlayout.core.state.Reference r6 = (androidx.constraintlayout.core.state.Reference) r6
            androidx.constraintlayout.core.widgets.HelperWidget r7 = r2.getHelperWidget()
            androidx.constraintlayout.core.widgets.ConstraintWidget r8 = r6.getConstraintWidget()
            r7.add(r8)
            goto L_0x00ff
        L_0x011d:
            r2.apply()
            goto L_0x0124
        L_0x0121:
            r2.apply()
        L_0x0124:
            goto L_0x00e1
        L_0x0125:
            java.util.HashMap<java.lang.Object, androidx.constraintlayout.core.state.Reference> r0 = r11.mReferences
            java.util.Set r0 = r0.keySet()
            java.util.Iterator r0 = r0.iterator()
        L_0x012f:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x01a7
            java.lang.Object r1 = r0.next()
            java.util.HashMap<java.lang.Object, androidx.constraintlayout.core.state.Reference> r2 = r11.mReferences
            java.lang.Object r2 = r2.get(r1)
            androidx.constraintlayout.core.state.Reference r2 = (androidx.constraintlayout.core.state.Reference) r2
            androidx.constraintlayout.core.state.ConstraintReference r3 = r11.mParent
            if (r2 == r3) goto L_0x01a6
            androidx.constraintlayout.core.state.helpers.Facade r3 = r2.getFacade()
            boolean r3 = r3 instanceof androidx.constraintlayout.core.state.HelperReference
            if (r3 == 0) goto L_0x01a6
            androidx.constraintlayout.core.state.helpers.Facade r3 = r2.getFacade()
            androidx.constraintlayout.core.state.HelperReference r3 = (androidx.constraintlayout.core.state.HelperReference) r3
            androidx.constraintlayout.core.widgets.HelperWidget r4 = r3.getHelperWidget()
            if (r4 == 0) goto L_0x01a6
            java.util.ArrayList<java.lang.Object> r5 = r3.mReferences
            java.util.Iterator r5 = r5.iterator()
        L_0x015f:
            boolean r6 = r5.hasNext()
            if (r6 == 0) goto L_0x01a3
            java.lang.Object r6 = r5.next()
            java.util.HashMap<java.lang.Object, androidx.constraintlayout.core.state.Reference> r7 = r11.mReferences
            java.lang.Object r7 = r7.get(r6)
            androidx.constraintlayout.core.state.Reference r7 = (androidx.constraintlayout.core.state.Reference) r7
            if (r7 == 0) goto L_0x017b
            androidx.constraintlayout.core.widgets.ConstraintWidget r8 = r7.getConstraintWidget()
            r4.add(r8)
            goto L_0x01a2
        L_0x017b:
            boolean r8 = r6 instanceof androidx.constraintlayout.core.state.Reference
            if (r8 == 0) goto L_0x018a
            r8 = r6
            androidx.constraintlayout.core.state.Reference r8 = (androidx.constraintlayout.core.state.Reference) r8
            androidx.constraintlayout.core.widgets.ConstraintWidget r8 = r8.getConstraintWidget()
            r4.add(r8)
            goto L_0x01a2
        L_0x018a:
            java.io.PrintStream r8 = java.lang.System.out
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "couldn't find reference for "
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.StringBuilder r9 = r9.append(r6)
            java.lang.String r9 = r9.toString()
            r8.println(r9)
        L_0x01a2:
            goto L_0x015f
        L_0x01a3:
            r2.apply()
        L_0x01a6:
            goto L_0x012f
        L_0x01a7:
            java.util.HashMap<java.lang.Object, androidx.constraintlayout.core.state.Reference> r0 = r11.mReferences
            java.util.Set r0 = r0.keySet()
            java.util.Iterator r0 = r0.iterator()
        L_0x01b1:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x01d5
            java.lang.Object r1 = r0.next()
            java.util.HashMap<java.lang.Object, androidx.constraintlayout.core.state.Reference> r2 = r11.mReferences
            java.lang.Object r2 = r2.get(r1)
            androidx.constraintlayout.core.state.Reference r2 = (androidx.constraintlayout.core.state.Reference) r2
            r2.apply()
            androidx.constraintlayout.core.widgets.ConstraintWidget r3 = r2.getConstraintWidget()
            if (r3 == 0) goto L_0x01d4
            if (r1 == 0) goto L_0x01d4
            java.lang.String r4 = r1.toString()
            r3.stringId = r4
        L_0x01d4:
            goto L_0x01b1
        L_0x01d5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.state.State.apply(androidx.constraintlayout.core.widgets.ConstraintWidgetContainer):void");
    }

    public BarrierReference barrier(Object key, Direction direction) {
        ConstraintReference constraints = constraints(key);
        if (constraints.getFacade() == null || !(constraints.getFacade() instanceof BarrierReference)) {
            BarrierReference barrierReference = new BarrierReference(this);
            barrierReference.setBarrierDirection(direction);
            constraints.setFacade(barrierReference);
        }
        return (BarrierReference) constraints.getFacade();
    }

    public AlignHorizontallyReference centerHorizontally(Object... references) {
        AlignHorizontallyReference alignHorizontallyReference = (AlignHorizontallyReference) helper((Object) null, Helper.ALIGN_HORIZONTALLY);
        alignHorizontallyReference.add(references);
        return alignHorizontallyReference;
    }

    public AlignVerticallyReference centerVertically(Object... references) {
        AlignVerticallyReference alignVerticallyReference = (AlignVerticallyReference) helper((Object) null, Helper.ALIGN_VERTICALLY);
        alignVerticallyReference.add(references);
        return alignVerticallyReference;
    }

    public ConstraintReference constraints(Object key) {
        Reference reference = this.mReferences.get(key);
        if (reference == null) {
            reference = createConstraintReference(key);
            this.mReferences.put(key, reference);
            reference.setKey(key);
        }
        if (reference instanceof ConstraintReference) {
            return (ConstraintReference) reference;
        }
        return null;
    }

    public int convertDimension(Object value) {
        if (value instanceof Float) {
            return ((Float) value).intValue();
        }
        if (value instanceof Integer) {
            return ((Integer) value).intValue();
        }
        return 0;
    }

    public ConstraintReference createConstraintReference(Object key) {
        return new ConstraintReference(this);
    }

    public void directMapping() {
        for (Object next : this.mReferences.keySet()) {
            ConstraintReference constraints = constraints(next);
            if (constraints instanceof ConstraintReference) {
                constraints.setView(next);
            }
        }
    }

    public ArrayList<String> getIdsForTag(String tag) {
        if (this.mTags.containsKey(tag)) {
            return this.mTags.get(tag);
        }
        return null;
    }

    public GuidelineReference guideline(Object key, int orientation) {
        ConstraintReference constraints = constraints(key);
        if (constraints.getFacade() == null || !(constraints.getFacade() instanceof GuidelineReference)) {
            GuidelineReference guidelineReference = new GuidelineReference(this);
            guidelineReference.setOrientation(orientation);
            guidelineReference.setKey(key);
            constraints.setFacade(guidelineReference);
        }
        return (GuidelineReference) constraints.getFacade();
    }

    public State height(Dimension dimension) {
        return setHeight(dimension);
    }

    public HelperReference helper(Object key, Helper type) {
        if (key == null) {
            key = createHelperKey();
        }
        HelperReference helperReference = this.mHelperReferences.get(key);
        if (helperReference == null) {
            switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$state$State$Helper[type.ordinal()]) {
                case 1:
                    helperReference = new HorizontalChainReference(this);
                    break;
                case 2:
                    helperReference = new VerticalChainReference(this);
                    break;
                case 3:
                    helperReference = new AlignHorizontallyReference(this);
                    break;
                case 4:
                    helperReference = new AlignVerticallyReference(this);
                    break;
                case 5:
                    helperReference = new BarrierReference(this);
                    break;
                default:
                    helperReference = new HelperReference(this, type);
                    break;
            }
            helperReference.setKey(key);
            this.mHelperReferences.put(key, helperReference);
        }
        return helperReference;
    }

    public HorizontalChainReference horizontalChain() {
        return (HorizontalChainReference) helper((Object) null, Helper.HORIZONTAL_CHAIN);
    }

    public HorizontalChainReference horizontalChain(Object... references) {
        HorizontalChainReference horizontalChainReference = (HorizontalChainReference) helper((Object) null, Helper.HORIZONTAL_CHAIN);
        horizontalChainReference.add(references);
        return horizontalChainReference;
    }

    public GuidelineReference horizontalGuideline(Object key) {
        return guideline(key, 0);
    }

    public void map(Object key, Object view) {
        ConstraintReference constraints = constraints(key);
        if (constraints instanceof ConstraintReference) {
            constraints.setView(view);
        }
    }

    /* access modifiers changed from: package-private */
    public Reference reference(Object key) {
        return this.mReferences.get(key);
    }

    public void reset() {
        this.mHelperReferences.clear();
        this.mTags.clear();
    }

    public boolean sameFixedHeight(int height) {
        return this.mParent.getHeight().equalsFixedValue(height);
    }

    public boolean sameFixedWidth(int width) {
        return this.mParent.getWidth().equalsFixedValue(width);
    }

    public State setHeight(Dimension dimension) {
        this.mParent.setHeight(dimension);
        return this;
    }

    public void setTag(String key, String tag) {
        ArrayList arrayList;
        ConstraintReference constraints = constraints(key);
        if (constraints instanceof ConstraintReference) {
            constraints.setTag(tag);
            if (!this.mTags.containsKey(tag)) {
                arrayList = new ArrayList();
                this.mTags.put(tag, arrayList);
            } else {
                arrayList = this.mTags.get(tag);
            }
            arrayList.add(key);
        }
    }

    public State setWidth(Dimension dimension) {
        this.mParent.setWidth(dimension);
        return this;
    }

    public VerticalChainReference verticalChain() {
        return (VerticalChainReference) helper((Object) null, Helper.VERTICAL_CHAIN);
    }

    public VerticalChainReference verticalChain(Object... references) {
        VerticalChainReference verticalChainReference = (VerticalChainReference) helper((Object) null, Helper.VERTICAL_CHAIN);
        verticalChainReference.add(references);
        return verticalChainReference;
    }

    public GuidelineReference verticalGuideline(Object key) {
        return guideline(key, 1);
    }

    public State width(Dimension dimension) {
        return setWidth(dimension);
    }
}
