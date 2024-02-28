package androidx.constraintlayout.core;

import androidx.constraintlayout.core.LinearSystem;
import androidx.constraintlayout.core.SolverVariable;
import java.util.ArrayList;
import okhttp3.HttpUrl;

public class ArrayRow implements LinearSystem.Row {
    private static final boolean DEBUG = false;
    private static final boolean FULL_NEW_CHECK = false;
    float constantValue = 0.0f;
    boolean isSimpleDefinition = false;
    boolean used = false;
    SolverVariable variable = null;
    public ArrayRowVariables variables;
    ArrayList<SolverVariable> variablesToUpdate = new ArrayList<>();

    public interface ArrayRowVariables {
        void add(SolverVariable solverVariable, float f, boolean z);

        void clear();

        boolean contains(SolverVariable solverVariable);

        void display();

        void divideByAmount(float f);

        float get(SolverVariable solverVariable);

        int getCurrentSize();

        SolverVariable getVariable(int i);

        float getVariableValue(int i);

        int indexOf(SolverVariable solverVariable);

        void invert();

        void put(SolverVariable solverVariable, float f);

        float remove(SolverVariable solverVariable, boolean z);

        int sizeInBytes();

        float use(ArrayRow arrayRow, boolean z);
    }

    public ArrayRow() {
    }

    public ArrayRow(Cache cache) {
        this.variables = new ArrayLinkedVariables(this, cache);
    }

    private boolean isNew(SolverVariable variable2, LinearSystem system) {
        return variable2.usageInRowCount <= 1;
    }

    private SolverVariable pickPivotInVariables(boolean[] avoid, SolverVariable exclude) {
        float f = 0.0f;
        SolverVariable solverVariable = null;
        SolverVariable solverVariable2 = null;
        float f2 = 0.0f;
        int currentSize = this.variables.getCurrentSize();
        for (int i = 0; i < currentSize; i++) {
            float variableValue = this.variables.getVariableValue(i);
            if (variableValue < 0.0f) {
                SolverVariable variable2 = this.variables.getVariable(i);
                if ((avoid == null || !avoid[variable2.id]) && variable2 != exclude) {
                    if (1 != 0) {
                        if ((variable2.mType == SolverVariable.Type.SLACK || variable2.mType == SolverVariable.Type.ERROR) && variableValue < f) {
                            f = variableValue;
                            solverVariable = variable2;
                        }
                    } else if (variable2.mType == SolverVariable.Type.SLACK) {
                        if (variableValue < f2) {
                            f2 = variableValue;
                            solverVariable2 = variable2;
                        }
                    } else if (variable2.mType == SolverVariable.Type.ERROR && variableValue < f) {
                        f = variableValue;
                        solverVariable = variable2;
                    }
                }
            }
        }
        return 1 != 0 ? solverVariable : solverVariable != null ? solverVariable : solverVariable2;
    }

    public ArrayRow addError(LinearSystem system, int strength) {
        this.variables.put(system.createErrorVariable(strength, "ep"), 1.0f);
        this.variables.put(system.createErrorVariable(strength, "em"), -1.0f);
        return this;
    }

    public void addError(SolverVariable error) {
        float f = 1.0f;
        if (error.strength == 1) {
            f = 1.0f;
        } else if (error.strength == 2) {
            f = 1000.0f;
        } else if (error.strength == 3) {
            f = 1000000.0f;
        } else if (error.strength == 4) {
            f = 1.0E9f;
        } else if (error.strength == 5) {
            f = 1.0E12f;
        }
        this.variables.put(error, f);
    }

    /* access modifiers changed from: package-private */
    public ArrayRow addSingleError(SolverVariable error, int sign) {
        this.variables.put(error, (float) sign);
        return this;
    }

    /* access modifiers changed from: package-private */
    public boolean chooseSubject(LinearSystem system) {
        boolean z = false;
        SolverVariable chooseSubjectInVariables = chooseSubjectInVariables(system);
        if (chooseSubjectInVariables == null) {
            z = true;
        } else {
            pivot(chooseSubjectInVariables);
        }
        if (this.variables.getCurrentSize() == 0) {
            this.isSimpleDefinition = true;
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public SolverVariable chooseSubjectInVariables(LinearSystem system) {
        SolverVariable solverVariable = null;
        SolverVariable solverVariable2 = null;
        float f = 0.0f;
        float f2 = 0.0f;
        boolean z = false;
        boolean z2 = false;
        int currentSize = this.variables.getCurrentSize();
        for (int i = 0; i < currentSize; i++) {
            float variableValue = this.variables.getVariableValue(i);
            SolverVariable variable2 = this.variables.getVariable(i);
            if (variable2.mType == SolverVariable.Type.UNRESTRICTED) {
                if (solverVariable2 == null) {
                    solverVariable2 = variable2;
                    f = variableValue;
                    z = isNew(variable2, system);
                } else if (f > variableValue) {
                    solverVariable2 = variable2;
                    f = variableValue;
                    z = isNew(variable2, system);
                } else if (!z && isNew(variable2, system)) {
                    solverVariable2 = variable2;
                    f = variableValue;
                    z = true;
                }
            } else if (solverVariable2 == null && variableValue < 0.0f) {
                if (solverVariable == null) {
                    solverVariable = variable2;
                    f2 = variableValue;
                    z2 = isNew(variable2, system);
                } else if (f2 > variableValue) {
                    solverVariable = variable2;
                    f2 = variableValue;
                    z2 = isNew(variable2, system);
                } else if (!z2 && isNew(variable2, system)) {
                    solverVariable = variable2;
                    f2 = variableValue;
                    z2 = true;
                }
            }
        }
        return solverVariable2 != null ? solverVariable2 : solverVariable;
    }

    public void clear() {
        this.variables.clear();
        this.variable = null;
        this.constantValue = 0.0f;
    }

    /* access modifiers changed from: package-private */
    public ArrayRow createRowCentering(SolverVariable variableA, SolverVariable variableB, int marginA, float bias, SolverVariable variableC, SolverVariable variableD, int marginB) {
        if (variableB == variableC) {
            this.variables.put(variableA, 1.0f);
            this.variables.put(variableD, 1.0f);
            this.variables.put(variableB, -2.0f);
            return this;
        }
        if (bias == 0.5f) {
            this.variables.put(variableA, 1.0f);
            this.variables.put(variableB, -1.0f);
            this.variables.put(variableC, -1.0f);
            this.variables.put(variableD, 1.0f);
            if (marginA > 0 || marginB > 0) {
                this.constantValue = (float) ((-marginA) + marginB);
            }
        } else if (bias <= 0.0f) {
            this.variables.put(variableA, -1.0f);
            this.variables.put(variableB, 1.0f);
            this.constantValue = (float) marginA;
        } else if (bias >= 1.0f) {
            this.variables.put(variableD, -1.0f);
            this.variables.put(variableC, 1.0f);
            this.constantValue = (float) (-marginB);
        } else {
            this.variables.put(variableA, (1.0f - bias) * 1.0f);
            this.variables.put(variableB, (1.0f - bias) * -1.0f);
            this.variables.put(variableC, -1.0f * bias);
            this.variables.put(variableD, bias * 1.0f);
            if (marginA > 0 || marginB > 0) {
                this.constantValue = (((float) (-marginA)) * (1.0f - bias)) + (((float) marginB) * bias);
            }
        }
        return this;
    }

    /* access modifiers changed from: package-private */
    public ArrayRow createRowDefinition(SolverVariable variable2, int value) {
        this.variable = variable2;
        variable2.computedValue = (float) value;
        this.constantValue = (float) value;
        this.isSimpleDefinition = true;
        return this;
    }

    /* access modifiers changed from: package-private */
    public ArrayRow createRowDimensionPercent(SolverVariable variableA, SolverVariable variableC, float percent) {
        this.variables.put(variableA, -1.0f);
        this.variables.put(variableC, percent);
        return this;
    }

    public ArrayRow createRowDimensionRatio(SolverVariable variableA, SolverVariable variableB, SolverVariable variableC, SolverVariable variableD, float ratio) {
        this.variables.put(variableA, -1.0f);
        this.variables.put(variableB, 1.0f);
        this.variables.put(variableC, ratio);
        this.variables.put(variableD, -ratio);
        return this;
    }

    public ArrayRow createRowEqualDimension(float currentWeight, float totalWeights, float nextWeight, SolverVariable variableStartA, int marginStartA, SolverVariable variableEndA, int marginEndA, SolverVariable variableStartB, int marginStartB, SolverVariable variableEndB, int marginEndB) {
        SolverVariable solverVariable = variableStartA;
        int i = marginStartA;
        SolverVariable solverVariable2 = variableEndA;
        SolverVariable solverVariable3 = variableStartB;
        int i2 = marginStartB;
        SolverVariable solverVariable4 = variableEndB;
        int i3 = marginEndB;
        if (totalWeights == 0.0f || currentWeight == nextWeight) {
            this.constantValue = (float) (((-i) - marginEndA) + i2 + i3);
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable4, 1.0f);
            this.variables.put(solverVariable3, -1.0f);
        } else {
            float f = (currentWeight / totalWeights) / (nextWeight / totalWeights);
            this.constantValue = ((float) ((-i) - marginEndA)) + (((float) i2) * f) + (((float) i3) * f);
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable4, f);
            this.variables.put(solverVariable3, -f);
        }
        return this;
    }

    public ArrayRow createRowEqualMatchDimensions(float currentWeight, float totalWeights, float nextWeight, SolverVariable variableStartA, SolverVariable variableEndA, SolverVariable variableStartB, SolverVariable variableEndB) {
        this.constantValue = 0.0f;
        if (totalWeights == 0.0f || currentWeight == nextWeight) {
            this.variables.put(variableStartA, 1.0f);
            this.variables.put(variableEndA, -1.0f);
            this.variables.put(variableEndB, 1.0f);
            this.variables.put(variableStartB, -1.0f);
        } else if (currentWeight == 0.0f) {
            this.variables.put(variableStartA, 1.0f);
            this.variables.put(variableEndA, -1.0f);
        } else if (nextWeight == 0.0f) {
            this.variables.put(variableStartB, 1.0f);
            this.variables.put(variableEndB, -1.0f);
        } else {
            float f = (currentWeight / totalWeights) / (nextWeight / totalWeights);
            this.variables.put(variableStartA, 1.0f);
            this.variables.put(variableEndA, -1.0f);
            this.variables.put(variableEndB, f);
            this.variables.put(variableStartB, -f);
        }
        return this;
    }

    public ArrayRow createRowEquals(SolverVariable variable2, int value) {
        if (value < 0) {
            this.constantValue = (float) (value * -1);
            this.variables.put(variable2, 1.0f);
        } else {
            this.constantValue = (float) value;
            this.variables.put(variable2, -1.0f);
        }
        return this;
    }

    public ArrayRow createRowEquals(SolverVariable variableA, SolverVariable variableB, int margin) {
        boolean z = false;
        if (margin != 0) {
            int i = margin;
            if (i < 0) {
                i *= -1;
                z = true;
            }
            this.constantValue = (float) i;
        }
        if (!z) {
            this.variables.put(variableA, -1.0f);
            this.variables.put(variableB, 1.0f);
        } else {
            this.variables.put(variableA, 1.0f);
            this.variables.put(variableB, -1.0f);
        }
        return this;
    }

    public ArrayRow createRowGreaterThan(SolverVariable a, int b, SolverVariable slack) {
        this.constantValue = (float) b;
        this.variables.put(a, -1.0f);
        return this;
    }

    public ArrayRow createRowGreaterThan(SolverVariable variableA, SolverVariable variableB, SolverVariable slack, int margin) {
        boolean z = false;
        if (margin != 0) {
            int i = margin;
            if (i < 0) {
                i *= -1;
                z = true;
            }
            this.constantValue = (float) i;
        }
        if (!z) {
            this.variables.put(variableA, -1.0f);
            this.variables.put(variableB, 1.0f);
            this.variables.put(slack, 1.0f);
        } else {
            this.variables.put(variableA, 1.0f);
            this.variables.put(variableB, -1.0f);
            this.variables.put(slack, -1.0f);
        }
        return this;
    }

    public ArrayRow createRowLowerThan(SolverVariable variableA, SolverVariable variableB, SolverVariable slack, int margin) {
        boolean z = false;
        if (margin != 0) {
            int i = margin;
            if (i < 0) {
                i *= -1;
                z = true;
            }
            this.constantValue = (float) i;
        }
        if (!z) {
            this.variables.put(variableA, -1.0f);
            this.variables.put(variableB, 1.0f);
            this.variables.put(slack, -1.0f);
        } else {
            this.variables.put(variableA, 1.0f);
            this.variables.put(variableB, -1.0f);
            this.variables.put(slack, 1.0f);
        }
        return this;
    }

    public ArrayRow createRowWithAngle(SolverVariable at, SolverVariable ab, SolverVariable bt, SolverVariable bb, float angleComponent) {
        this.variables.put(bt, 0.5f);
        this.variables.put(bb, 0.5f);
        this.variables.put(at, -0.5f);
        this.variables.put(ab, -0.5f);
        this.constantValue = -angleComponent;
        return this;
    }

    /* access modifiers changed from: package-private */
    public void ensurePositiveConstant() {
        float f = this.constantValue;
        if (f < 0.0f) {
            this.constantValue = f * -1.0f;
            this.variables.invert();
        }
    }

    public SolverVariable getKey() {
        return this.variable;
    }

    public SolverVariable getPivotCandidate(LinearSystem system, boolean[] avoid) {
        return pickPivotInVariables(avoid, (SolverVariable) null);
    }

    /* access modifiers changed from: package-private */
    public boolean hasKeyVariable() {
        SolverVariable solverVariable = this.variable;
        return solverVariable != null && (solverVariable.mType == SolverVariable.Type.UNRESTRICTED || this.constantValue >= 0.0f);
    }

    /* access modifiers changed from: package-private */
    public boolean hasVariable(SolverVariable v) {
        return this.variables.contains(v);
    }

    public void initFromRow(LinearSystem.Row row) {
        if (row instanceof ArrayRow) {
            ArrayRow arrayRow = (ArrayRow) row;
            this.variable = null;
            this.variables.clear();
            for (int i = 0; i < arrayRow.variables.getCurrentSize(); i++) {
                this.variables.add(arrayRow.variables.getVariable(i), arrayRow.variables.getVariableValue(i), true);
            }
        }
    }

    public boolean isEmpty() {
        return this.variable == null && this.constantValue == 0.0f && this.variables.getCurrentSize() == 0;
    }

    public SolverVariable pickPivot(SolverVariable exclude) {
        return pickPivotInVariables((boolean[]) null, exclude);
    }

    /* access modifiers changed from: package-private */
    public void pivot(SolverVariable v) {
        SolverVariable solverVariable = this.variable;
        if (solverVariable != null) {
            this.variables.put(solverVariable, -1.0f);
            this.variable.definitionId = -1;
            this.variable = null;
        }
        float remove = this.variables.remove(v, true) * -1.0f;
        this.variable = v;
        if (remove != 1.0f) {
            this.constantValue /= remove;
            this.variables.divideByAmount(remove);
        }
    }

    public void reset() {
        this.variable = null;
        this.variables.clear();
        this.constantValue = 0.0f;
        this.isSimpleDefinition = false;
    }

    /* access modifiers changed from: package-private */
    public int sizeInBytes() {
        int i = 0;
        if (this.variable != null) {
            i = 0 + 4;
        }
        return i + 4 + 4 + this.variables.sizeInBytes();
    }

    /* access modifiers changed from: package-private */
    public String toReadableString() {
        String str;
        String str2 = (this.variable == null ? HttpUrl.FRAGMENT_ENCODE_SET + "0" : HttpUrl.FRAGMENT_ENCODE_SET + this.variable) + " = ";
        boolean z = false;
        if (this.constantValue != 0.0f) {
            str2 = str2 + this.constantValue;
            z = true;
        }
        int currentSize = this.variables.getCurrentSize();
        for (int i = 0; i < currentSize; i++) {
            SolverVariable variable2 = this.variables.getVariable(i);
            if (variable2 != null) {
                float variableValue = this.variables.getVariableValue(i);
                if (variableValue != 0.0f) {
                    String solverVariable = variable2.toString();
                    if (!z) {
                        if (variableValue < 0.0f) {
                            str = str + "- ";
                            variableValue *= -1.0f;
                        }
                    } else if (variableValue > 0.0f) {
                        str = str + " + ";
                    } else {
                        str = str + " - ";
                        variableValue *= -1.0f;
                    }
                    str = variableValue == 1.0f ? str + solverVariable : str + variableValue + " " + solverVariable;
                    z = true;
                }
            }
        }
        return !z ? str + "0.0" : str;
    }

    public String toString() {
        return toReadableString();
    }

    public void updateFromFinalVariable(LinearSystem system, SolverVariable variable2, boolean removeFromDefinition) {
        if (variable2 != null && variable2.isFinalValue) {
            this.constantValue += variable2.computedValue * this.variables.get(variable2);
            this.variables.remove(variable2, removeFromDefinition);
            if (removeFromDefinition) {
                variable2.removeFromRow(this);
            }
            if (LinearSystem.SIMPLIFY_SYNONYMS && this.variables.getCurrentSize() == 0) {
                this.isSimpleDefinition = true;
                system.hasSimpleDefinition = true;
            }
        }
    }

    public void updateFromRow(LinearSystem system, ArrayRow definition, boolean removeFromDefinition) {
        this.constantValue += definition.constantValue * this.variables.use(definition, removeFromDefinition);
        if (removeFromDefinition) {
            definition.variable.removeFromRow(this);
        }
        if (LinearSystem.SIMPLIFY_SYNONYMS && this.variable != null && this.variables.getCurrentSize() == 0) {
            this.isSimpleDefinition = true;
            system.hasSimpleDefinition = true;
        }
    }

    public void updateFromSynonymVariable(LinearSystem system, SolverVariable variable2, boolean removeFromDefinition) {
        if (variable2 != null && variable2.isSynonym) {
            float f = this.variables.get(variable2);
            this.constantValue += variable2.synonymDelta * f;
            this.variables.remove(variable2, removeFromDefinition);
            if (removeFromDefinition) {
                variable2.removeFromRow(this);
            }
            this.variables.add(system.mCache.mIndexedVariables[variable2.synonym], f, removeFromDefinition);
            if (LinearSystem.SIMPLIFY_SYNONYMS && this.variables.getCurrentSize() == 0) {
                this.isSimpleDefinition = true;
                system.hasSimpleDefinition = true;
            }
        }
    }

    public void updateFromSystem(LinearSystem system) {
        if (system.mRows.length != 0) {
            boolean z = false;
            while (!z) {
                int currentSize = this.variables.getCurrentSize();
                for (int i = 0; i < currentSize; i++) {
                    SolverVariable variable2 = this.variables.getVariable(i);
                    if (variable2.definitionId != -1 || variable2.isFinalValue || variable2.isSynonym) {
                        this.variablesToUpdate.add(variable2);
                    }
                }
                int size = this.variablesToUpdate.size();
                if (size > 0) {
                    for (int i2 = 0; i2 < size; i2++) {
                        SolverVariable solverVariable = this.variablesToUpdate.get(i2);
                        if (solverVariable.isFinalValue) {
                            updateFromFinalVariable(system, solverVariable, true);
                        } else if (solverVariable.isSynonym) {
                            updateFromSynonymVariable(system, solverVariable, true);
                        } else {
                            updateFromRow(system, system.mRows[solverVariable.definitionId], true);
                        }
                    }
                    this.variablesToUpdate.clear();
                } else {
                    z = true;
                }
            }
            if (LinearSystem.SIMPLIFY_SYNONYMS && this.variable != null && this.variables.getCurrentSize() == 0) {
                this.isSimpleDefinition = true;
                system.hasSimpleDefinition = true;
            }
        }
    }
}
