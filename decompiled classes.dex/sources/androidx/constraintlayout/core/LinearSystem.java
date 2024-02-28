package androidx.constraintlayout.core;

import androidx.constraintlayout.core.SolverVariable;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import okhttp3.HttpUrl;

public class LinearSystem {
    public static long ARRAY_ROW_CREATION = 0;
    public static final boolean DEBUG = false;
    private static final boolean DEBUG_CONSTRAINTS = false;
    public static final boolean FULL_DEBUG = false;
    public static final boolean MEASURE = false;
    public static long OPTIMIZED_ARRAY_ROW_CREATION = 0;
    public static boolean OPTIMIZED_ENGINE = false;
    private static int POOL_SIZE = 1000;
    public static boolean SIMPLIFY_SYNONYMS = true;
    public static boolean SKIP_COLUMNS = true;
    public static boolean USE_BASIC_SYNONYMS = true;
    public static boolean USE_DEPENDENCY_ORDERING = false;
    public static boolean USE_SYNONYMS = true;
    public static Metrics sMetrics;
    private int TABLE_SIZE;
    public boolean graphOptimizer;
    public boolean hasSimpleDefinition;
    private boolean[] mAlreadyTestedCandidates;
    final Cache mCache;
    private Row mGoal;
    private int mMaxColumns;
    private int mMaxRows;
    int mNumColumns;
    int mNumRows;
    private SolverVariable[] mPoolVariables;
    private int mPoolVariablesCount;
    ArrayRow[] mRows;
    private Row mTempGoal;
    private HashMap<String, SolverVariable> mVariables;
    int mVariablesID;
    public boolean newgraphOptimizer;

    interface Row {
        void addError(SolverVariable solverVariable);

        void clear();

        SolverVariable getKey();

        SolverVariable getPivotCandidate(LinearSystem linearSystem, boolean[] zArr);

        void initFromRow(Row row);

        boolean isEmpty();

        void updateFromFinalVariable(LinearSystem linearSystem, SolverVariable solverVariable, boolean z);

        void updateFromRow(LinearSystem linearSystem, ArrayRow arrayRow, boolean z);

        void updateFromSystem(LinearSystem linearSystem);
    }

    class ValuesRow extends ArrayRow {
        public ValuesRow(Cache cache) {
            this.variables = new SolverVariableValues(this, cache);
        }
    }

    public LinearSystem() {
        this.hasSimpleDefinition = false;
        this.mVariablesID = 0;
        this.mVariables = null;
        this.TABLE_SIZE = 32;
        this.mMaxColumns = 32;
        this.mRows = null;
        this.graphOptimizer = false;
        this.newgraphOptimizer = false;
        this.mAlreadyTestedCandidates = new boolean[32];
        this.mNumColumns = 1;
        this.mNumRows = 0;
        this.mMaxRows = 32;
        this.mPoolVariables = new SolverVariable[POOL_SIZE];
        this.mPoolVariablesCount = 0;
        this.mRows = new ArrayRow[32];
        releaseRows();
        Cache cache = new Cache();
        this.mCache = cache;
        this.mGoal = new PriorityGoalRow(cache);
        if (OPTIMIZED_ENGINE) {
            this.mTempGoal = new ValuesRow(cache);
        } else {
            this.mTempGoal = new ArrayRow(cache);
        }
    }

    private SolverVariable acquireSolverVariable(SolverVariable.Type type, String prefix) {
        SolverVariable acquire = this.mCache.solverVariablePool.acquire();
        if (acquire == null) {
            acquire = new SolverVariable(type, prefix);
            acquire.setType(type, prefix);
        } else {
            acquire.reset();
            acquire.setType(type, prefix);
        }
        int i = this.mPoolVariablesCount;
        int i2 = POOL_SIZE;
        if (i >= i2) {
            int i3 = i2 * 2;
            POOL_SIZE = i3;
            this.mPoolVariables = (SolverVariable[]) Arrays.copyOf(this.mPoolVariables, i3);
        }
        SolverVariable[] solverVariableArr = this.mPoolVariables;
        int i4 = this.mPoolVariablesCount;
        this.mPoolVariablesCount = i4 + 1;
        solverVariableArr[i4] = acquire;
        return acquire;
    }

    private void addError(ArrayRow row) {
        row.addError(this, 0);
    }

    private final void addRow(ArrayRow row) {
        int i;
        if (!SIMPLIFY_SYNONYMS || !row.isSimpleDefinition) {
            this.mRows[this.mNumRows] = row;
            row.variable.definitionId = this.mNumRows;
            this.mNumRows++;
            row.variable.updateReferencesWithNewDefinition(this, row);
        } else {
            row.variable.setFinalValue(this, row.constantValue);
        }
        if (SIMPLIFY_SYNONYMS && this.hasSimpleDefinition) {
            int i2 = 0;
            while (i2 < this.mNumRows) {
                if (this.mRows[i2] == null) {
                    System.out.println("WTF");
                }
                ArrayRow arrayRow = this.mRows[i2];
                if (arrayRow != null && arrayRow.isSimpleDefinition) {
                    ArrayRow arrayRow2 = this.mRows[i2];
                    arrayRow2.variable.setFinalValue(this, arrayRow2.constantValue);
                    if (OPTIMIZED_ENGINE) {
                        this.mCache.optimizedArrayRowPool.release(arrayRow2);
                    } else {
                        this.mCache.arrayRowPool.release(arrayRow2);
                    }
                    this.mRows[i2] = null;
                    int i3 = i2 + 1;
                    int i4 = i2 + 1;
                    while (true) {
                        i = this.mNumRows;
                        if (i4 >= i) {
                            break;
                        }
                        ArrayRow[] arrayRowArr = this.mRows;
                        arrayRowArr[i4 - 1] = arrayRowArr[i4];
                        if (arrayRowArr[i4 - 1].variable.definitionId == i4) {
                            this.mRows[i4 - 1].variable.definitionId = i4 - 1;
                        }
                        i3 = i4;
                        i4++;
                    }
                    if (i3 < i) {
                        this.mRows[i3] = null;
                    }
                    this.mNumRows = i - 1;
                    i2--;
                }
                i2++;
            }
            this.hasSimpleDefinition = false;
        }
    }

    private void addSingleError(ArrayRow row, int sign) {
        addSingleError(row, sign, 0);
    }

    private void computeValues() {
        for (int i = 0; i < this.mNumRows; i++) {
            ArrayRow arrayRow = this.mRows[i];
            arrayRow.variable.computedValue = arrayRow.constantValue;
        }
    }

    public static ArrayRow createRowDimensionPercent(LinearSystem linearSystem, SolverVariable variableA, SolverVariable variableC, float percent) {
        return linearSystem.createRow().createRowDimensionPercent(variableA, variableC, percent);
    }

    private SolverVariable createVariable(String name, SolverVariable.Type type) {
        Metrics metrics = sMetrics;
        if (metrics != null) {
            metrics.variables++;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable acquireSolverVariable = acquireSolverVariable(type, (String) null);
        acquireSolverVariable.setName(name);
        int i = this.mVariablesID + 1;
        this.mVariablesID = i;
        this.mNumColumns++;
        acquireSolverVariable.id = i;
        if (this.mVariables == null) {
            this.mVariables = new HashMap<>();
        }
        this.mVariables.put(name, acquireSolverVariable);
        this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
        return acquireSolverVariable;
    }

    private void displayRows() {
        displaySolverVariables();
        String str = HttpUrl.FRAGMENT_ENCODE_SET;
        for (int i = 0; i < this.mNumRows; i++) {
            str = (str + this.mRows[i]) + "\n";
        }
        System.out.println(str + this.mGoal + "\n");
    }

    private void displaySolverVariables() {
        System.out.println("Display Rows (" + this.mNumRows + "x" + this.mNumColumns + ")\n");
    }

    private int enforceBFS(Row goal) throws Exception {
        float f;
        boolean z;
        boolean z2;
        int i = 0;
        boolean z3 = false;
        int i2 = 0;
        while (true) {
            f = 0.0f;
            if (i2 >= this.mNumRows) {
                break;
            } else if (this.mRows[i2].variable.mType != SolverVariable.Type.UNRESTRICTED && this.mRows[i2].constantValue < 0.0f) {
                z3 = true;
                break;
            } else {
                i2++;
            }
        }
        if (z3) {
            boolean z4 = false;
            i = 0;
            while (!z4) {
                Metrics metrics = sMetrics;
                if (metrics != null) {
                    metrics.bfs++;
                }
                i++;
                float f2 = Float.MAX_VALUE;
                int i3 = 0;
                int i4 = -1;
                int i5 = -1;
                int i6 = 0;
                while (i6 < this.mNumRows) {
                    ArrayRow arrayRow = this.mRows[i6];
                    if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED) {
                        z = z3;
                    } else if (arrayRow.isSimpleDefinition) {
                        z = z3;
                    } else if (arrayRow.constantValue >= f) {
                        z = z3;
                    } else if (SKIP_COLUMNS) {
                        int currentSize = arrayRow.variables.getCurrentSize();
                        int i7 = 0;
                        while (i7 < currentSize) {
                            SolverVariable variable = arrayRow.variables.getVariable(i7);
                            float f3 = arrayRow.variables.get(variable);
                            if (f3 > f) {
                                int i8 = 0;
                                while (true) {
                                    z2 = z3;
                                    if (i8 >= 9) {
                                        break;
                                    }
                                    float f4 = variable.strengthVector[i8] / f3;
                                    if ((f4 < f2 && i8 == i3) || i8 > i3) {
                                        f2 = f4;
                                        i4 = i6;
                                        i5 = variable.id;
                                        i3 = i8;
                                    }
                                    i8++;
                                    z3 = z2;
                                }
                            } else {
                                z2 = z3;
                            }
                            i7++;
                            z3 = z2;
                            f = 0.0f;
                        }
                        z = z3;
                    } else {
                        z = z3;
                        for (int i9 = 1; i9 < this.mNumColumns; i9++) {
                            SolverVariable solverVariable = this.mCache.mIndexedVariables[i9];
                            float f5 = arrayRow.variables.get(solverVariable);
                            if (f5 > 0.0f) {
                                for (int i10 = 0; i10 < 9; i10++) {
                                    float f6 = solverVariable.strengthVector[i10] / f5;
                                    if ((f6 < f2 && i10 == i3) || i10 > i3) {
                                        f2 = f6;
                                        i4 = i6;
                                        i5 = i9;
                                        i3 = i10;
                                    }
                                }
                            }
                        }
                    }
                    i6++;
                    z3 = z;
                    f = 0.0f;
                }
                boolean z5 = z3;
                if (i4 != -1) {
                    ArrayRow arrayRow2 = this.mRows[i4];
                    arrayRow2.variable.definitionId = -1;
                    Metrics metrics2 = sMetrics;
                    if (metrics2 != null) {
                        metrics2.pivots++;
                    }
                    arrayRow2.pivot(this.mCache.mIndexedVariables[i5]);
                    arrayRow2.variable.definitionId = i4;
                    arrayRow2.variable.updateReferencesWithNewDefinition(this, arrayRow2);
                } else {
                    z4 = true;
                }
                if (i > this.mNumColumns / 2) {
                    z4 = true;
                }
                z3 = z5;
                f = 0.0f;
            }
            boolean z6 = z3;
        } else {
            boolean z7 = z3;
        }
        return i;
    }

    private String getDisplaySize(int n) {
        int i = ((n * 4) / 1024) / 1024;
        if (i > 0) {
            return HttpUrl.FRAGMENT_ENCODE_SET + i + " Mb";
        }
        int i2 = (n * 4) / 1024;
        return i2 > 0 ? HttpUrl.FRAGMENT_ENCODE_SET + i2 + " Kb" : HttpUrl.FRAGMENT_ENCODE_SET + (n * 4) + " bytes";
    }

    private String getDisplayStrength(int strength) {
        return strength == 1 ? "LOW" : strength == 2 ? "MEDIUM" : strength == 3 ? "HIGH" : strength == 4 ? "HIGHEST" : strength == 5 ? "EQUALITY" : strength == 8 ? "FIXED" : strength == 6 ? "BARRIER" : "NONE";
    }

    public static Metrics getMetrics() {
        return sMetrics;
    }

    private void increaseTableSize() {
        int i = this.TABLE_SIZE * 2;
        this.TABLE_SIZE = i;
        this.mRows = (ArrayRow[]) Arrays.copyOf(this.mRows, i);
        Cache cache = this.mCache;
        cache.mIndexedVariables = (SolverVariable[]) Arrays.copyOf(cache.mIndexedVariables, this.TABLE_SIZE);
        int i2 = this.TABLE_SIZE;
        this.mAlreadyTestedCandidates = new boolean[i2];
        this.mMaxColumns = i2;
        this.mMaxRows = i2;
        Metrics metrics = sMetrics;
        if (metrics != null) {
            metrics.tableSizeIncrease++;
            Metrics metrics2 = sMetrics;
            metrics2.maxTableSize = Math.max(metrics2.maxTableSize, (long) this.TABLE_SIZE);
            Metrics metrics3 = sMetrics;
            metrics3.lastTableSize = metrics3.maxTableSize;
        }
    }

    private final int optimize(Row goal, boolean b) {
        Metrics metrics = sMetrics;
        if (metrics != null) {
            metrics.optimize++;
        }
        boolean z = false;
        int i = 0;
        for (int i2 = 0; i2 < this.mNumColumns; i2++) {
            this.mAlreadyTestedCandidates[i2] = false;
        }
        while (!z) {
            Metrics metrics2 = sMetrics;
            if (metrics2 != null) {
                metrics2.iterations++;
            }
            i++;
            if (i >= this.mNumColumns * 2) {
                return i;
            }
            if (goal.getKey() != null) {
                this.mAlreadyTestedCandidates[goal.getKey().id] = true;
            }
            SolverVariable pivotCandidate = goal.getPivotCandidate(this, this.mAlreadyTestedCandidates);
            if (pivotCandidate != null) {
                if (this.mAlreadyTestedCandidates[pivotCandidate.id]) {
                    return i;
                }
                this.mAlreadyTestedCandidates[pivotCandidate.id] = true;
            }
            if (pivotCandidate != null) {
                float f = Float.MAX_VALUE;
                int i3 = -1;
                for (int i4 = 0; i4 < this.mNumRows; i4++) {
                    ArrayRow arrayRow = this.mRows[i4];
                    if (arrayRow.variable.mType != SolverVariable.Type.UNRESTRICTED && !arrayRow.isSimpleDefinition && arrayRow.hasVariable(pivotCandidate)) {
                        float f2 = arrayRow.variables.get(pivotCandidate);
                        if (f2 < 0.0f) {
                            float f3 = (-arrayRow.constantValue) / f2;
                            if (f3 < f) {
                                f = f3;
                                i3 = i4;
                            }
                        }
                    }
                }
                if (i3 > -1) {
                    ArrayRow arrayRow2 = this.mRows[i3];
                    arrayRow2.variable.definitionId = -1;
                    Metrics metrics3 = sMetrics;
                    if (metrics3 != null) {
                        metrics3.pivots++;
                    }
                    arrayRow2.pivot(pivotCandidate);
                    arrayRow2.variable.definitionId = i3;
                    arrayRow2.variable.updateReferencesWithNewDefinition(this, arrayRow2);
                }
            } else {
                z = true;
            }
        }
        return i;
    }

    private void releaseRows() {
        if (OPTIMIZED_ENGINE) {
            for (int i = 0; i < this.mNumRows; i++) {
                ArrayRow arrayRow = this.mRows[i];
                if (arrayRow != null) {
                    this.mCache.optimizedArrayRowPool.release(arrayRow);
                }
                this.mRows[i] = null;
            }
            return;
        }
        for (int i2 = 0; i2 < this.mNumRows; i2++) {
            ArrayRow arrayRow2 = this.mRows[i2];
            if (arrayRow2 != null) {
                this.mCache.arrayRowPool.release(arrayRow2);
            }
            this.mRows[i2] = null;
        }
    }

    public void addCenterPoint(ConstraintWidget widget, ConstraintWidget target, float angle, int radius) {
        ConstraintWidget constraintWidget = widget;
        ConstraintWidget constraintWidget2 = target;
        float f = angle;
        int i = radius;
        SolverVariable createObjectVariable = createObjectVariable(constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT));
        SolverVariable createObjectVariable2 = createObjectVariable(constraintWidget.getAnchor(ConstraintAnchor.Type.TOP));
        SolverVariable createObjectVariable3 = createObjectVariable(constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT));
        SolverVariable createObjectVariable4 = createObjectVariable(constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM));
        SolverVariable createObjectVariable5 = createObjectVariable(constraintWidget2.getAnchor(ConstraintAnchor.Type.LEFT));
        SolverVariable createObjectVariable6 = createObjectVariable(constraintWidget2.getAnchor(ConstraintAnchor.Type.TOP));
        SolverVariable createObjectVariable7 = createObjectVariable(constraintWidget2.getAnchor(ConstraintAnchor.Type.RIGHT));
        SolverVariable createObjectVariable8 = createObjectVariable(constraintWidget2.getAnchor(ConstraintAnchor.Type.BOTTOM));
        ArrayRow createRow = createRow();
        float sin = (float) (Math.sin((double) f) * ((double) i));
        float f2 = sin;
        createRow.createRowWithAngle(createObjectVariable2, createObjectVariable4, createObjectVariable6, createObjectVariable8, sin);
        addConstraint(createRow);
        ArrayRow createRow2 = createRow();
        float cos = (float) (Math.cos((double) f) * ((double) i));
        float f3 = cos;
        createRow2.createRowWithAngle(createObjectVariable, createObjectVariable3, createObjectVariable5, createObjectVariable7, cos);
        addConstraint(createRow2);
    }

    public void addCentering(SolverVariable a, SolverVariable b, int m1, float bias, SolverVariable c, SolverVariable d, int m2, int strength) {
        int i = strength;
        ArrayRow createRow = createRow();
        createRow.createRowCentering(a, b, m1, bias, c, d, m2);
        if (i != 8) {
            createRow.addError(this, i);
        }
        addConstraint(createRow);
    }

    public void addConstraint(ArrayRow row) {
        SolverVariable pickPivot;
        if (row != null) {
            Metrics metrics = sMetrics;
            if (metrics != null) {
                metrics.constraints++;
                if (row.isSimpleDefinition) {
                    sMetrics.simpleconstraints++;
                }
            }
            if (this.mNumRows + 1 >= this.mMaxRows || this.mNumColumns + 1 >= this.mMaxColumns) {
                increaseTableSize();
            }
            boolean z = false;
            if (!row.isSimpleDefinition) {
                row.updateFromSystem(this);
                if (!row.isEmpty()) {
                    row.ensurePositiveConstant();
                    if (row.chooseSubject(this)) {
                        SolverVariable createExtraVariable = createExtraVariable();
                        row.variable = createExtraVariable;
                        int i = this.mNumRows;
                        addRow(row);
                        if (this.mNumRows == i + 1) {
                            z = true;
                            this.mTempGoal.initFromRow(row);
                            optimize(this.mTempGoal, true);
                            if (createExtraVariable.definitionId == -1) {
                                if (row.variable == createExtraVariable && (pickPivot = row.pickPivot(createExtraVariable)) != null) {
                                    Metrics metrics2 = sMetrics;
                                    if (metrics2 != null) {
                                        metrics2.pivots++;
                                    }
                                    row.pivot(pickPivot);
                                }
                                if (!row.isSimpleDefinition) {
                                    row.variable.updateReferencesWithNewDefinition(this, row);
                                }
                                if (OPTIMIZED_ENGINE) {
                                    this.mCache.optimizedArrayRowPool.release(row);
                                } else {
                                    this.mCache.arrayRowPool.release(row);
                                }
                                this.mNumRows--;
                            }
                        }
                    }
                    if (!row.hasKeyVariable()) {
                        return;
                    }
                } else {
                    return;
                }
            }
            if (!z) {
                addRow(row);
            }
        }
    }

    public ArrayRow addEquality(SolverVariable a, SolverVariable b, int margin, int strength) {
        if (!USE_BASIC_SYNONYMS || strength != 8 || !b.isFinalValue || a.definitionId != -1) {
            ArrayRow createRow = createRow();
            createRow.createRowEquals(a, b, margin);
            if (strength != 8) {
                createRow.addError(this, strength);
            }
            addConstraint(createRow);
            return createRow;
        }
        a.setFinalValue(this, b.computedValue + ((float) margin));
        return null;
    }

    public void addEquality(SolverVariable a, int value) {
        if (!USE_BASIC_SYNONYMS || a.definitionId != -1) {
            int i = a.definitionId;
            if (a.definitionId != -1) {
                ArrayRow arrayRow = this.mRows[i];
                if (arrayRow.isSimpleDefinition) {
                    arrayRow.constantValue = (float) value;
                } else if (arrayRow.variables.getCurrentSize() == 0) {
                    arrayRow.isSimpleDefinition = true;
                    arrayRow.constantValue = (float) value;
                } else {
                    ArrayRow createRow = createRow();
                    createRow.createRowEquals(a, value);
                    addConstraint(createRow);
                }
            } else {
                ArrayRow createRow2 = createRow();
                createRow2.createRowDefinition(a, value);
                addConstraint(createRow2);
            }
        } else {
            a.setFinalValue(this, (float) value);
            for (int i2 = 0; i2 < this.mVariablesID + 1; i2++) {
                SolverVariable solverVariable = this.mCache.mIndexedVariables[i2];
                if (solverVariable != null && solverVariable.isSynonym && solverVariable.synonym == a.id) {
                    solverVariable.setFinalValue(this, ((float) value) + solverVariable.synonymDelta);
                }
            }
        }
    }

    public void addGreaterBarrier(SolverVariable a, SolverVariable b, int margin, boolean hasMatchConstraintWidgets) {
        ArrayRow createRow = createRow();
        SolverVariable createSlackVariable = createSlackVariable();
        createSlackVariable.strength = 0;
        createRow.createRowGreaterThan(a, b, createSlackVariable, margin);
        addConstraint(createRow);
    }

    public void addGreaterThan(SolverVariable a, SolverVariable b, int margin, int strength) {
        ArrayRow createRow = createRow();
        SolverVariable createSlackVariable = createSlackVariable();
        createSlackVariable.strength = 0;
        createRow.createRowGreaterThan(a, b, createSlackVariable, margin);
        if (strength != 8) {
            addSingleError(createRow, (int) (-1.0f * createRow.variables.get(createSlackVariable)), strength);
        }
        addConstraint(createRow);
    }

    public void addLowerBarrier(SolverVariable a, SolverVariable b, int margin, boolean hasMatchConstraintWidgets) {
        ArrayRow createRow = createRow();
        SolverVariable createSlackVariable = createSlackVariable();
        createSlackVariable.strength = 0;
        createRow.createRowLowerThan(a, b, createSlackVariable, margin);
        addConstraint(createRow);
    }

    public void addLowerThan(SolverVariable a, SolverVariable b, int margin, int strength) {
        ArrayRow createRow = createRow();
        SolverVariable createSlackVariable = createSlackVariable();
        createSlackVariable.strength = 0;
        createRow.createRowLowerThan(a, b, createSlackVariable, margin);
        if (strength != 8) {
            addSingleError(createRow, (int) (-1.0f * createRow.variables.get(createSlackVariable)), strength);
        }
        addConstraint(createRow);
    }

    public void addRatio(SolverVariable a, SolverVariable b, SolverVariable c, SolverVariable d, float ratio, int strength) {
        ArrayRow createRow = createRow();
        createRow.createRowDimensionRatio(a, b, c, d, ratio);
        if (strength != 8) {
            createRow.addError(this, strength);
        }
        addConstraint(createRow);
    }

    /* access modifiers changed from: package-private */
    public void addSingleError(ArrayRow row, int sign, int strength) {
        row.addSingleError(createErrorVariable(strength, (String) null), sign);
    }

    public void addSynonym(SolverVariable a, SolverVariable b, int margin) {
        if (a.definitionId == -1 && margin == 0) {
            if (b.isSynonym) {
                margin = (int) (((float) margin) + b.synonymDelta);
                b = this.mCache.mIndexedVariables[b.synonym];
            }
            if (a.isSynonym) {
                int margin2 = (int) (((float) margin) - a.synonymDelta);
                SolverVariable a2 = this.mCache.mIndexedVariables[a.synonym];
                return;
            }
            a.setSynonym(this, b, 0.0f);
            return;
        }
        addEquality(a, b, margin, 8);
    }

    /* access modifiers changed from: package-private */
    public final void cleanupRows() {
        int i;
        int i2 = 0;
        while (i2 < this.mNumRows) {
            ArrayRow arrayRow = this.mRows[i2];
            if (arrayRow.variables.getCurrentSize() == 0) {
                arrayRow.isSimpleDefinition = true;
            }
            if (arrayRow.isSimpleDefinition) {
                arrayRow.variable.computedValue = arrayRow.constantValue;
                arrayRow.variable.removeFromRow(arrayRow);
                int i3 = i2;
                while (true) {
                    i = this.mNumRows;
                    if (i3 >= i - 1) {
                        break;
                    }
                    ArrayRow[] arrayRowArr = this.mRows;
                    arrayRowArr[i3] = arrayRowArr[i3 + 1];
                    i3++;
                }
                this.mRows[i - 1] = null;
                this.mNumRows = i - 1;
                i2--;
                if (OPTIMIZED_ENGINE) {
                    this.mCache.optimizedArrayRowPool.release(arrayRow);
                } else {
                    this.mCache.arrayRowPool.release(arrayRow);
                }
            }
            i2++;
        }
    }

    public SolverVariable createErrorVariable(int strength, String prefix) {
        Metrics metrics = sMetrics;
        if (metrics != null) {
            metrics.errors++;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable acquireSolverVariable = acquireSolverVariable(SolverVariable.Type.ERROR, prefix);
        int i = this.mVariablesID + 1;
        this.mVariablesID = i;
        this.mNumColumns++;
        acquireSolverVariable.id = i;
        acquireSolverVariable.strength = strength;
        this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
        this.mGoal.addError(acquireSolverVariable);
        return acquireSolverVariable;
    }

    public SolverVariable createExtraVariable() {
        Metrics metrics = sMetrics;
        if (metrics != null) {
            metrics.extravariables++;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable acquireSolverVariable = acquireSolverVariable(SolverVariable.Type.SLACK, (String) null);
        int i = this.mVariablesID + 1;
        this.mVariablesID = i;
        this.mNumColumns++;
        acquireSolverVariable.id = i;
        this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
        return acquireSolverVariable;
    }

    public SolverVariable createObjectVariable(Object anchor) {
        if (anchor == null) {
            return null;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable solverVariable = null;
        if (anchor instanceof ConstraintAnchor) {
            solverVariable = ((ConstraintAnchor) anchor).getSolverVariable();
            if (solverVariable == null) {
                ((ConstraintAnchor) anchor).resetSolverVariable(this.mCache);
                solverVariable = ((ConstraintAnchor) anchor).getSolverVariable();
            }
            if (solverVariable.id == -1 || solverVariable.id > this.mVariablesID || this.mCache.mIndexedVariables[solverVariable.id] == null) {
                if (solverVariable.id != -1) {
                    solverVariable.reset();
                }
                int i = this.mVariablesID + 1;
                this.mVariablesID = i;
                this.mNumColumns++;
                solverVariable.id = i;
                solverVariable.mType = SolverVariable.Type.UNRESTRICTED;
                this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
            }
        }
        return solverVariable;
    }

    public ArrayRow createRow() {
        ArrayRow arrayRow;
        if (OPTIMIZED_ENGINE) {
            arrayRow = this.mCache.optimizedArrayRowPool.acquire();
            if (arrayRow == null) {
                arrayRow = new ValuesRow(this.mCache);
                OPTIMIZED_ARRAY_ROW_CREATION++;
            } else {
                arrayRow.reset();
            }
        } else {
            arrayRow = this.mCache.arrayRowPool.acquire();
            if (arrayRow == null) {
                arrayRow = new ArrayRow(this.mCache);
                ARRAY_ROW_CREATION++;
            } else {
                arrayRow.reset();
            }
        }
        SolverVariable.increaseErrorId();
        return arrayRow;
    }

    public SolverVariable createSlackVariable() {
        Metrics metrics = sMetrics;
        if (metrics != null) {
            metrics.slackvariables++;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable acquireSolverVariable = acquireSolverVariable(SolverVariable.Type.SLACK, (String) null);
        int i = this.mVariablesID + 1;
        this.mVariablesID = i;
        this.mNumColumns++;
        acquireSolverVariable.id = i;
        this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
        return acquireSolverVariable;
    }

    public void displayReadableRows() {
        displaySolverVariables();
        String str = " num vars " + this.mVariablesID + "\n";
        for (int i = 0; i < this.mVariablesID + 1; i++) {
            SolverVariable solverVariable = this.mCache.mIndexedVariables[i];
            if (solverVariable != null && solverVariable.isFinalValue) {
                str = str + " $[" + i + "] => " + solverVariable + " = " + solverVariable.computedValue + "\n";
            }
        }
        String str2 = str + "\n";
        for (int i2 = 0; i2 < this.mVariablesID + 1; i2++) {
            SolverVariable solverVariable2 = this.mCache.mIndexedVariables[i2];
            if (solverVariable2 != null && solverVariable2.isSynonym) {
                str2 = str2 + " ~[" + i2 + "] => " + solverVariable2 + " = " + this.mCache.mIndexedVariables[solverVariable2.synonym] + " + " + solverVariable2.synonymDelta + "\n";
            }
        }
        String str3 = str2 + "\n\n #  ";
        for (int i3 = 0; i3 < this.mNumRows; i3++) {
            str3 = (str3 + this.mRows[i3].toReadableString()) + "\n #  ";
        }
        if (this.mGoal != null) {
            str3 = str3 + "Goal: " + this.mGoal + "\n";
        }
        System.out.println(str3);
    }

    /* access modifiers changed from: package-private */
    public void displaySystemInformation() {
        int i = 0;
        for (int i2 = 0; i2 < this.TABLE_SIZE; i2++) {
            ArrayRow arrayRow = this.mRows[i2];
            if (arrayRow != null) {
                i += arrayRow.sizeInBytes();
            }
        }
        int i3 = 0;
        for (int i4 = 0; i4 < this.mNumRows; i4++) {
            ArrayRow arrayRow2 = this.mRows[i4];
            if (arrayRow2 != null) {
                i3 += arrayRow2.sizeInBytes();
            }
        }
        PrintStream printStream = System.out;
        StringBuilder append = new StringBuilder().append("Linear System -> Table size: ").append(this.TABLE_SIZE).append(" (");
        int i5 = this.TABLE_SIZE;
        printStream.println(append.append(getDisplaySize(i5 * i5)).append(") -- row sizes: ").append(getDisplaySize(i)).append(", actual size: ").append(getDisplaySize(i3)).append(" rows: ").append(this.mNumRows).append("/").append(this.mMaxRows).append(" cols: ").append(this.mNumColumns).append("/").append(this.mMaxColumns).append(" ").append(0).append(" occupied cells, ").append(getDisplaySize(0)).toString());
    }

    public void displayVariablesReadableRows() {
        displaySolverVariables();
        String str = HttpUrl.FRAGMENT_ENCODE_SET;
        for (int i = 0; i < this.mNumRows; i++) {
            if (this.mRows[i].variable.mType == SolverVariable.Type.UNRESTRICTED) {
                str = (str + this.mRows[i].toReadableString()) + "\n";
            }
        }
        System.out.println(str + this.mGoal + "\n");
    }

    public void fillMetrics(Metrics metrics) {
        sMetrics = metrics;
    }

    public Cache getCache() {
        return this.mCache;
    }

    /* access modifiers changed from: package-private */
    public Row getGoal() {
        return this.mGoal;
    }

    public int getMemoryUsed() {
        int i = 0;
        for (int i2 = 0; i2 < this.mNumRows; i2++) {
            ArrayRow arrayRow = this.mRows[i2];
            if (arrayRow != null) {
                i += arrayRow.sizeInBytes();
            }
        }
        return i;
    }

    public int getNumEquations() {
        return this.mNumRows;
    }

    public int getNumVariables() {
        return this.mVariablesID;
    }

    public int getObjectVariableValue(Object object) {
        SolverVariable solverVariable = ((ConstraintAnchor) object).getSolverVariable();
        if (solverVariable != null) {
            return (int) (solverVariable.computedValue + 0.5f);
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    public ArrayRow getRow(int n) {
        return this.mRows[n];
    }

    /* access modifiers changed from: package-private */
    public float getValueFor(String name) {
        SolverVariable variable = getVariable(name, SolverVariable.Type.UNRESTRICTED);
        if (variable == null) {
            return 0.0f;
        }
        return variable.computedValue;
    }

    /* access modifiers changed from: package-private */
    public SolverVariable getVariable(String name, SolverVariable.Type type) {
        if (this.mVariables == null) {
            this.mVariables = new HashMap<>();
        }
        SolverVariable solverVariable = this.mVariables.get(name);
        return solverVariable == null ? createVariable(name, type) : solverVariable;
    }

    public void minimize() throws Exception {
        Metrics metrics = sMetrics;
        if (metrics != null) {
            metrics.minimize++;
        }
        if (this.mGoal.isEmpty()) {
            computeValues();
        } else if (this.graphOptimizer || this.newgraphOptimizer) {
            Metrics metrics2 = sMetrics;
            if (metrics2 != null) {
                metrics2.graphOptimizer++;
            }
            boolean z = true;
            int i = 0;
            while (true) {
                if (i >= this.mNumRows) {
                    break;
                } else if (!this.mRows[i].isSimpleDefinition) {
                    z = false;
                    break;
                } else {
                    i++;
                }
            }
            if (!z) {
                minimizeGoal(this.mGoal);
                return;
            }
            Metrics metrics3 = sMetrics;
            if (metrics3 != null) {
                metrics3.fullySolved++;
            }
            computeValues();
        } else {
            minimizeGoal(this.mGoal);
        }
    }

    /* access modifiers changed from: package-private */
    public void minimizeGoal(Row goal) throws Exception {
        Metrics metrics = sMetrics;
        if (metrics != null) {
            metrics.minimizeGoal++;
            Metrics metrics2 = sMetrics;
            metrics2.maxVariables = Math.max(metrics2.maxVariables, (long) this.mNumColumns);
            Metrics metrics3 = sMetrics;
            metrics3.maxRows = Math.max(metrics3.maxRows, (long) this.mNumRows);
        }
        enforceBFS(goal);
        optimize(goal, false);
        computeValues();
    }

    public void removeRow(ArrayRow row) {
        int i;
        if (row.isSimpleDefinition && row.variable != null) {
            if (row.variable.definitionId != -1) {
                int i2 = row.variable.definitionId;
                while (true) {
                    i = this.mNumRows;
                    if (i2 >= i - 1) {
                        break;
                    }
                    SolverVariable solverVariable = this.mRows[i2 + 1].variable;
                    if (solverVariable.definitionId == i2 + 1) {
                        solverVariable.definitionId = i2;
                    }
                    ArrayRow[] arrayRowArr = this.mRows;
                    arrayRowArr[i2] = arrayRowArr[i2 + 1];
                    i2++;
                }
                this.mNumRows = i - 1;
            }
            if (!row.variable.isFinalValue) {
                row.variable.setFinalValue(this, row.constantValue);
            }
            if (OPTIMIZED_ENGINE) {
                this.mCache.optimizedArrayRowPool.release(row);
            } else {
                this.mCache.arrayRowPool.release(row);
            }
        }
    }

    public void reset() {
        for (SolverVariable solverVariable : this.mCache.mIndexedVariables) {
            if (solverVariable != null) {
                solverVariable.reset();
            }
        }
        this.mCache.solverVariablePool.releaseAll(this.mPoolVariables, this.mPoolVariablesCount);
        this.mPoolVariablesCount = 0;
        Arrays.fill(this.mCache.mIndexedVariables, (Object) null);
        HashMap<String, SolverVariable> hashMap = this.mVariables;
        if (hashMap != null) {
            hashMap.clear();
        }
        this.mVariablesID = 0;
        this.mGoal.clear();
        this.mNumColumns = 1;
        for (int i = 0; i < this.mNumRows; i++) {
            ArrayRow arrayRow = this.mRows[i];
            if (arrayRow != null) {
                arrayRow.used = false;
            }
        }
        releaseRows();
        this.mNumRows = 0;
        if (OPTIMIZED_ENGINE) {
            this.mTempGoal = new ValuesRow(this.mCache);
        } else {
            this.mTempGoal = new ArrayRow(this.mCache);
        }
    }
}
