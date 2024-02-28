package androidx.constraintlayout.core;

import androidx.constraintlayout.core.ArrayRow;
import java.util.Arrays;
import okhttp3.HttpUrl;

public class ArrayLinkedVariables implements ArrayRow.ArrayRowVariables {
    private static final boolean DEBUG = false;
    private static final boolean FULL_NEW_CHECK = false;
    static final int NONE = -1;
    private static float epsilon = 0.001f;
    private int ROW_SIZE = 8;
    private SolverVariable candidate = null;
    int currentSize = 0;
    private int[] mArrayIndices = new int[8];
    private int[] mArrayNextIndices = new int[8];
    private float[] mArrayValues = new float[8];
    protected final Cache mCache;
    private boolean mDidFillOnce = false;
    private int mHead = -1;
    private int mLast = -1;
    private final ArrayRow mRow;

    ArrayLinkedVariables(ArrayRow arrayRow, Cache cache) {
        this.mRow = arrayRow;
        this.mCache = cache;
    }

    public void add(SolverVariable variable, float value, boolean removeFromDefinition) {
        float f = epsilon;
        if (value > (-f) && value < f) {
            return;
        }
        if (this.mHead == -1) {
            this.mHead = 0;
            this.mArrayValues[0] = value;
            this.mArrayIndices[0] = variable.id;
            this.mArrayNextIndices[this.mHead] = -1;
            variable.usageInRowCount++;
            variable.addToRow(this.mRow);
            this.currentSize++;
            if (!this.mDidFillOnce) {
                int i = this.mLast + 1;
                this.mLast = i;
                int[] iArr = this.mArrayIndices;
                if (i >= iArr.length) {
                    this.mDidFillOnce = true;
                    this.mLast = iArr.length - 1;
                    return;
                }
                return;
            }
            return;
        }
        int i2 = this.mHead;
        int i3 = -1;
        int i4 = 0;
        while (i2 != -1 && i4 < this.currentSize) {
            if (this.mArrayIndices[i2] == variable.id) {
                float[] fArr = this.mArrayValues;
                float f2 = fArr[i2] + value;
                float f3 = epsilon;
                if (f2 > (-f3) && f2 < f3) {
                    f2 = 0.0f;
                }
                fArr[i2] = f2;
                if (f2 == 0.0f) {
                    if (i2 == this.mHead) {
                        this.mHead = this.mArrayNextIndices[i2];
                    } else {
                        int[] iArr2 = this.mArrayNextIndices;
                        iArr2[i3] = iArr2[i2];
                    }
                    if (removeFromDefinition) {
                        variable.removeFromRow(this.mRow);
                    }
                    if (this.mDidFillOnce) {
                        this.mLast = i2;
                    }
                    variable.usageInRowCount--;
                    this.currentSize--;
                    return;
                }
                return;
            }
            if (this.mArrayIndices[i2] < variable.id) {
                i3 = i2;
            }
            i2 = this.mArrayNextIndices[i2];
            i4++;
        }
        int i5 = this.mLast;
        int i6 = i5 + 1;
        if (this.mDidFillOnce) {
            int[] iArr3 = this.mArrayIndices;
            i6 = iArr3[i5] == -1 ? this.mLast : iArr3.length;
        }
        int[] iArr4 = this.mArrayIndices;
        if (i6 >= iArr4.length && this.currentSize < iArr4.length) {
            int i7 = 0;
            while (true) {
                int[] iArr5 = this.mArrayIndices;
                if (i7 >= iArr5.length) {
                    break;
                } else if (iArr5[i7] == -1) {
                    i6 = i7;
                    break;
                } else {
                    i7++;
                }
            }
        }
        int[] iArr6 = this.mArrayIndices;
        if (i6 >= iArr6.length) {
            i6 = iArr6.length;
            int i8 = this.ROW_SIZE * 2;
            this.ROW_SIZE = i8;
            this.mDidFillOnce = false;
            this.mLast = i6 - 1;
            this.mArrayValues = Arrays.copyOf(this.mArrayValues, i8);
            this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
            this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
        }
        this.mArrayIndices[i6] = variable.id;
        this.mArrayValues[i6] = value;
        if (i3 != -1) {
            int[] iArr7 = this.mArrayNextIndices;
            iArr7[i6] = iArr7[i3];
            iArr7[i3] = i6;
        } else {
            this.mArrayNextIndices[i6] = this.mHead;
            this.mHead = i6;
        }
        variable.usageInRowCount++;
        variable.addToRow(this.mRow);
        this.currentSize++;
        if (!this.mDidFillOnce) {
            this.mLast++;
        }
        int i9 = this.mLast;
        int[] iArr8 = this.mArrayIndices;
        if (i9 >= iArr8.length) {
            this.mDidFillOnce = true;
            this.mLast = iArr8.length - 1;
        }
    }

    public final void clear() {
        int i = this.mHead;
        int i2 = 0;
        while (i != -1 && i2 < this.currentSize) {
            SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[i]];
            if (solverVariable != null) {
                solverVariable.removeFromRow(this.mRow);
            }
            i = this.mArrayNextIndices[i];
            i2++;
        }
        this.mHead = -1;
        this.mLast = -1;
        this.mDidFillOnce = false;
        this.currentSize = 0;
    }

    public boolean contains(SolverVariable variable) {
        if (this.mHead == -1) {
            return false;
        }
        int i = this.mHead;
        int i2 = 0;
        while (i != -1 && i2 < this.currentSize) {
            if (this.mArrayIndices[i] == variable.id) {
                return true;
            }
            i = this.mArrayNextIndices[i];
            i2++;
        }
        return false;
    }

    public void display() {
        int i = this.currentSize;
        System.out.print("{ ");
        for (int i2 = 0; i2 < i; i2++) {
            SolverVariable variable = getVariable(i2);
            if (variable != null) {
                System.out.print(variable + " = " + getVariableValue(i2) + " ");
            }
        }
        System.out.println(" }");
    }

    public void divideByAmount(float amount) {
        int i = this.mHead;
        int i2 = 0;
        while (i != -1 && i2 < this.currentSize) {
            float[] fArr = this.mArrayValues;
            fArr[i] = fArr[i] / amount;
            i = this.mArrayNextIndices[i];
            i2++;
        }
    }

    public final float get(SolverVariable v) {
        int i = this.mHead;
        int i2 = 0;
        while (i != -1 && i2 < this.currentSize) {
            if (this.mArrayIndices[i] == v.id) {
                return this.mArrayValues[i];
            }
            i = this.mArrayNextIndices[i];
            i2++;
        }
        return 0.0f;
    }

    public int getCurrentSize() {
        return this.currentSize;
    }

    public int getHead() {
        return this.mHead;
    }

    public final int getId(int index) {
        return this.mArrayIndices[index];
    }

    public final int getNextIndice(int index) {
        return this.mArrayNextIndices[index];
    }

    /* access modifiers changed from: package-private */
    public SolverVariable getPivotCandidate() {
        SolverVariable solverVariable = this.candidate;
        if (solverVariable != null) {
            return solverVariable;
        }
        int i = this.mHead;
        int i2 = 0;
        SolverVariable solverVariable2 = null;
        while (i != -1 && i2 < this.currentSize) {
            if (this.mArrayValues[i] < 0.0f) {
                SolverVariable solverVariable3 = this.mCache.mIndexedVariables[this.mArrayIndices[i]];
                if (solverVariable2 == null || solverVariable2.strength < solverVariable3.strength) {
                    solverVariable2 = solverVariable3;
                }
            }
            i = this.mArrayNextIndices[i];
            i2++;
        }
        return solverVariable2;
    }

    public final float getValue(int index) {
        return this.mArrayValues[index];
    }

    public SolverVariable getVariable(int index) {
        int i = this.mHead;
        int i2 = 0;
        while (i != -1 && i2 < this.currentSize) {
            if (i2 == index) {
                return this.mCache.mIndexedVariables[this.mArrayIndices[i]];
            }
            i = this.mArrayNextIndices[i];
            i2++;
        }
        return null;
    }

    public float getVariableValue(int index) {
        int i = this.mHead;
        int i2 = 0;
        while (i != -1 && i2 < this.currentSize) {
            if (i2 == index) {
                return this.mArrayValues[i];
            }
            i = this.mArrayNextIndices[i];
            i2++;
        }
        return 0.0f;
    }

    /* access modifiers changed from: package-private */
    public boolean hasAtLeastOnePositiveVariable() {
        int i = this.mHead;
        int i2 = 0;
        while (i != -1 && i2 < this.currentSize) {
            if (this.mArrayValues[i] > 0.0f) {
                return true;
            }
            i = this.mArrayNextIndices[i];
            i2++;
        }
        return false;
    }

    public int indexOf(SolverVariable variable) {
        if (this.mHead == -1) {
            return -1;
        }
        int i = this.mHead;
        int i2 = 0;
        while (i != -1 && i2 < this.currentSize) {
            if (this.mArrayIndices[i] == variable.id) {
                return i;
            }
            i = this.mArrayNextIndices[i];
            i2++;
        }
        return -1;
    }

    public void invert() {
        int i = this.mHead;
        int i2 = 0;
        while (i != -1 && i2 < this.currentSize) {
            float[] fArr = this.mArrayValues;
            fArr[i] = fArr[i] * -1.0f;
            i = this.mArrayNextIndices[i];
            i2++;
        }
    }

    public final void put(SolverVariable variable, float value) {
        if (value == 0.0f) {
            remove(variable, true);
        } else if (this.mHead == -1) {
            this.mHead = 0;
            this.mArrayValues[0] = value;
            this.mArrayIndices[0] = variable.id;
            this.mArrayNextIndices[this.mHead] = -1;
            variable.usageInRowCount++;
            variable.addToRow(this.mRow);
            this.currentSize++;
            if (!this.mDidFillOnce) {
                int i = this.mLast + 1;
                this.mLast = i;
                int[] iArr = this.mArrayIndices;
                if (i >= iArr.length) {
                    this.mDidFillOnce = true;
                    this.mLast = iArr.length - 1;
                }
            }
        } else {
            int i2 = this.mHead;
            int i3 = -1;
            int i4 = 0;
            while (i2 != -1 && i4 < this.currentSize) {
                if (this.mArrayIndices[i2] == variable.id) {
                    this.mArrayValues[i2] = value;
                    return;
                }
                if (this.mArrayIndices[i2] < variable.id) {
                    i3 = i2;
                }
                i2 = this.mArrayNextIndices[i2];
                i4++;
            }
            int i5 = this.mLast;
            int i6 = i5 + 1;
            if (this.mDidFillOnce) {
                int[] iArr2 = this.mArrayIndices;
                i6 = iArr2[i5] == -1 ? this.mLast : iArr2.length;
            }
            int[] iArr3 = this.mArrayIndices;
            if (i6 >= iArr3.length && this.currentSize < iArr3.length) {
                int i7 = 0;
                while (true) {
                    int[] iArr4 = this.mArrayIndices;
                    if (i7 >= iArr4.length) {
                        break;
                    } else if (iArr4[i7] == -1) {
                        i6 = i7;
                        break;
                    } else {
                        i7++;
                    }
                }
            }
            int[] iArr5 = this.mArrayIndices;
            if (i6 >= iArr5.length) {
                i6 = iArr5.length;
                int i8 = this.ROW_SIZE * 2;
                this.ROW_SIZE = i8;
                this.mDidFillOnce = false;
                this.mLast = i6 - 1;
                this.mArrayValues = Arrays.copyOf(this.mArrayValues, i8);
                this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
                this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
            }
            this.mArrayIndices[i6] = variable.id;
            this.mArrayValues[i6] = value;
            if (i3 != -1) {
                int[] iArr6 = this.mArrayNextIndices;
                iArr6[i6] = iArr6[i3];
                iArr6[i3] = i6;
            } else {
                this.mArrayNextIndices[i6] = this.mHead;
                this.mHead = i6;
            }
            variable.usageInRowCount++;
            variable.addToRow(this.mRow);
            int i9 = this.currentSize + 1;
            this.currentSize = i9;
            if (!this.mDidFillOnce) {
                this.mLast++;
            }
            int[] iArr7 = this.mArrayIndices;
            if (i9 >= iArr7.length) {
                this.mDidFillOnce = true;
            }
            if (this.mLast >= iArr7.length) {
                this.mDidFillOnce = true;
                this.mLast = iArr7.length - 1;
            }
        }
    }

    public final float remove(SolverVariable variable, boolean removeFromDefinition) {
        if (this.candidate == variable) {
            this.candidate = null;
        }
        if (this.mHead == -1) {
            return 0.0f;
        }
        int i = this.mHead;
        int i2 = -1;
        int i3 = 0;
        while (i != -1 && i3 < this.currentSize) {
            if (this.mArrayIndices[i] == variable.id) {
                if (i == this.mHead) {
                    this.mHead = this.mArrayNextIndices[i];
                } else {
                    int[] iArr = this.mArrayNextIndices;
                    iArr[i2] = iArr[i];
                }
                if (removeFromDefinition) {
                    variable.removeFromRow(this.mRow);
                }
                variable.usageInRowCount--;
                this.currentSize--;
                this.mArrayIndices[i] = -1;
                if (this.mDidFillOnce) {
                    this.mLast = i;
                }
                return this.mArrayValues[i];
            }
            i2 = i;
            i = this.mArrayNextIndices[i];
            i3++;
        }
        return 0.0f;
    }

    public int sizeInBytes() {
        return 0 + (this.mArrayIndices.length * 4 * 3) + 36;
    }

    public String toString() {
        String str = HttpUrl.FRAGMENT_ENCODE_SET;
        int i = this.mHead;
        int i2 = 0;
        while (i != -1 && i2 < this.currentSize) {
            str = ((str + " -> ") + this.mArrayValues[i] + " : ") + this.mCache.mIndexedVariables[this.mArrayIndices[i]];
            i = this.mArrayNextIndices[i];
            i2++;
        }
        return str;
    }

    public float use(ArrayRow definition, boolean removeFromDefinition) {
        float f = get(definition.variable);
        remove(definition.variable, removeFromDefinition);
        ArrayRow.ArrayRowVariables arrayRowVariables = definition.variables;
        int currentSize2 = arrayRowVariables.getCurrentSize();
        for (int i = 0; i < currentSize2; i++) {
            SolverVariable variable = arrayRowVariables.getVariable(i);
            add(variable, arrayRowVariables.get(variable) * f, removeFromDefinition);
        }
        return f;
    }
}
