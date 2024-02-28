package androidx.constraintlayout.core;

import androidx.constraintlayout.core.ArrayRow;
import java.util.Arrays;

public class SolverVariableValues implements ArrayRow.ArrayRowVariables {
    private static final boolean DEBUG = false;
    private static final boolean HASH = true;
    private static float epsilon = 0.001f;
    private int HASH_SIZE = 16;
    private final int NONE = -1;
    private int SIZE = 16;
    int head = -1;
    int[] keys = new int[16];
    protected final Cache mCache;
    int mCount = 0;
    private final ArrayRow mRow;
    int[] next = new int[16];
    int[] nextKeys = new int[16];
    int[] previous = new int[16];
    float[] values = new float[16];
    int[] variables = new int[16];

    SolverVariableValues(ArrayRow row, Cache cache) {
        this.mRow = row;
        this.mCache = cache;
        clear();
    }

    private void addToHashMap(SolverVariable variable, int index) {
        int[] iArr;
        int i = variable.id % this.HASH_SIZE;
        int[] iArr2 = this.keys;
        int i2 = iArr2[i];
        if (i2 == -1) {
            iArr2[i] = index;
        } else {
            while (true) {
                iArr = this.nextKeys;
                if (iArr[i2] == -1) {
                    break;
                }
                i2 = iArr[i2];
            }
            iArr[i2] = index;
        }
        this.nextKeys[index] = -1;
    }

    private void addVariable(int index, SolverVariable variable, float value) {
        this.variables[index] = variable.id;
        this.values[index] = value;
        this.previous[index] = -1;
        this.next[index] = -1;
        variable.addToRow(this.mRow);
        variable.usageInRowCount++;
        this.mCount++;
    }

    private void displayHash() {
        for (int i = 0; i < this.HASH_SIZE; i++) {
            if (this.keys[i] != -1) {
                String str = hashCode() + " hash [" + i + "] => ";
                int i2 = this.keys[i];
                boolean z = false;
                while (!z) {
                    str = str + " " + this.variables[i2];
                    int[] iArr = this.nextKeys;
                    if (iArr[i2] != -1) {
                        i2 = iArr[i2];
                    } else {
                        z = HASH;
                    }
                }
                System.out.println(str);
            }
        }
    }

    private int findEmptySlot() {
        for (int i = 0; i < this.SIZE; i++) {
            if (this.variables[i] == -1) {
                return i;
            }
        }
        return -1;
    }

    private void increaseSize() {
        int i = this.SIZE * 2;
        this.variables = Arrays.copyOf(this.variables, i);
        this.values = Arrays.copyOf(this.values, i);
        this.previous = Arrays.copyOf(this.previous, i);
        this.next = Arrays.copyOf(this.next, i);
        this.nextKeys = Arrays.copyOf(this.nextKeys, i);
        for (int i2 = this.SIZE; i2 < i; i2++) {
            this.variables[i2] = -1;
            this.nextKeys[i2] = -1;
        }
        this.SIZE = i;
    }

    private void insertVariable(int index, SolverVariable variable, float value) {
        int findEmptySlot = findEmptySlot();
        addVariable(findEmptySlot, variable, value);
        if (index != -1) {
            this.previous[findEmptySlot] = index;
            int[] iArr = this.next;
            iArr[findEmptySlot] = iArr[index];
            iArr[index] = findEmptySlot;
        } else {
            this.previous[findEmptySlot] = -1;
            if (this.mCount > 0) {
                this.next[findEmptySlot] = this.head;
                this.head = findEmptySlot;
            } else {
                this.next[findEmptySlot] = -1;
            }
        }
        int i = this.next[findEmptySlot];
        if (i != -1) {
            this.previous[i] = findEmptySlot;
        }
        addToHashMap(variable, findEmptySlot);
    }

    private void removeFromHashMap(SolverVariable variable) {
        int[] iArr;
        int i = variable.id % this.HASH_SIZE;
        int i2 = this.keys[i];
        if (i2 != -1) {
            int i3 = variable.id;
            if (this.variables[i2] == i3) {
                int[] iArr2 = this.keys;
                int[] iArr3 = this.nextKeys;
                iArr2[i] = iArr3[i2];
                iArr3[i2] = -1;
                return;
            }
            while (true) {
                iArr = this.nextKeys;
                int i4 = iArr[i2];
                if (i4 == -1 || this.variables[i4] == i3) {
                    int i5 = iArr[i2];
                } else {
                    i2 = iArr[i2];
                }
            }
            int i52 = iArr[i2];
            if (i52 != -1 && this.variables[i52] == i3) {
                iArr[i2] = iArr[i52];
                iArr[i52] = -1;
            }
        }
    }

    public void add(SolverVariable v, float value, boolean removeFromDefinition) {
        float f = epsilon;
        if (value <= (-f) || value >= f) {
            int indexOf = indexOf(v);
            if (indexOf == -1) {
                put(v, value);
                return;
            }
            float[] fArr = this.values;
            float f2 = fArr[indexOf] + value;
            fArr[indexOf] = f2;
            float f3 = epsilon;
            if (f2 > (-f3) && f2 < f3) {
                fArr[indexOf] = 0.0f;
                remove(v, removeFromDefinition);
            }
        }
    }

    public void clear() {
        int i = this.mCount;
        for (int i2 = 0; i2 < i; i2++) {
            SolverVariable variable = getVariable(i2);
            if (variable != null) {
                variable.removeFromRow(this.mRow);
            }
        }
        for (int i3 = 0; i3 < this.SIZE; i3++) {
            this.variables[i3] = -1;
            this.nextKeys[i3] = -1;
        }
        for (int i4 = 0; i4 < this.HASH_SIZE; i4++) {
            this.keys[i4] = -1;
        }
        this.mCount = 0;
        this.head = -1;
    }

    public boolean contains(SolverVariable variable) {
        if (indexOf(variable) != -1) {
            return HASH;
        }
        return false;
    }

    public void display() {
        int i = this.mCount;
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
        int i = this.mCount;
        int i2 = this.head;
        int i3 = 0;
        while (i3 < i) {
            float[] fArr = this.values;
            fArr[i2] = fArr[i2] / amount;
            i2 = this.next[i2];
            if (i2 != -1) {
                i3++;
            } else {
                return;
            }
        }
    }

    public float get(SolverVariable variable) {
        int indexOf = indexOf(variable);
        if (indexOf != -1) {
            return this.values[indexOf];
        }
        return 0.0f;
    }

    public int getCurrentSize() {
        return this.mCount;
    }

    public SolverVariable getVariable(int index) {
        int i = this.mCount;
        if (i == 0) {
            return null;
        }
        int i2 = this.head;
        for (int i3 = 0; i3 < i; i3++) {
            if (i3 == index && i2 != -1) {
                return this.mCache.mIndexedVariables[this.variables[i2]];
            }
            i2 = this.next[i2];
            if (i2 == -1) {
                break;
            }
        }
        return null;
    }

    public float getVariableValue(int index) {
        int i = this.mCount;
        int i2 = this.head;
        for (int i3 = 0; i3 < i; i3++) {
            if (i3 == index) {
                return this.values[i2];
            }
            i2 = this.next[i2];
            if (i2 == -1) {
                return 0.0f;
            }
        }
        return 0.0f;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0034 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0035 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int indexOf(androidx.constraintlayout.core.SolverVariable r7) {
        /*
            r6 = this;
            int r0 = r6.mCount
            r1 = -1
            if (r0 == 0) goto L_0x0036
            if (r7 != 0) goto L_0x0008
            goto L_0x0036
        L_0x0008:
            int r0 = r7.id
            int r2 = r6.HASH_SIZE
            int r2 = r0 % r2
            int[] r3 = r6.keys
            r2 = r3[r2]
            if (r2 != r1) goto L_0x0015
            return r1
        L_0x0015:
            int[] r3 = r6.variables
            r3 = r3[r2]
            if (r3 != r0) goto L_0x001c
            return r2
        L_0x001c:
            int[] r3 = r6.nextKeys
            r4 = r3[r2]
            if (r4 == r1) goto L_0x002b
            int[] r5 = r6.variables
            r5 = r5[r4]
            if (r5 == r0) goto L_0x002b
            r2 = r3[r2]
            goto L_0x001c
        L_0x002b:
            if (r4 != r1) goto L_0x002e
            return r1
        L_0x002e:
            int[] r3 = r6.variables
            r3 = r3[r4]
            if (r3 != r0) goto L_0x0035
            return r4
        L_0x0035:
            return r1
        L_0x0036:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.SolverVariableValues.indexOf(androidx.constraintlayout.core.SolverVariable):int");
    }

    public void invert() {
        int i = this.mCount;
        int i2 = this.head;
        int i3 = 0;
        while (i3 < i) {
            float[] fArr = this.values;
            fArr[i2] = fArr[i2] * -1.0f;
            i2 = this.next[i2];
            if (i2 != -1) {
                i3++;
            } else {
                return;
            }
        }
    }

    public void put(SolverVariable variable, float value) {
        float f = epsilon;
        if (value > (-f) && value < f) {
            remove(variable, HASH);
        } else if (this.mCount == 0) {
            addVariable(0, variable, value);
            addToHashMap(variable, 0);
            this.head = 0;
        } else {
            int indexOf = indexOf(variable);
            if (indexOf != -1) {
                this.values[indexOf] = value;
                return;
            }
            if (this.mCount + 1 >= this.SIZE) {
                increaseSize();
            }
            int i = this.mCount;
            int i2 = -1;
            int i3 = this.head;
            for (int i4 = 0; i4 < i; i4++) {
                if (this.variables[i3] == variable.id) {
                    this.values[i3] = value;
                    return;
                }
                if (this.variables[i3] < variable.id) {
                    i2 = i3;
                }
                i3 = this.next[i3];
                if (i3 == -1) {
                    break;
                }
            }
            insertVariable(i2, variable, value);
        }
    }

    public float remove(SolverVariable v, boolean removeFromDefinition) {
        int indexOf = indexOf(v);
        if (indexOf == -1) {
            return 0.0f;
        }
        removeFromHashMap(v);
        float f = this.values[indexOf];
        if (this.head == indexOf) {
            this.head = this.next[indexOf];
        }
        this.variables[indexOf] = -1;
        int[] iArr = this.previous;
        int i = iArr[indexOf];
        if (i != -1) {
            int[] iArr2 = this.next;
            iArr2[i] = iArr2[indexOf];
        }
        int i2 = this.next[indexOf];
        if (i2 != -1) {
            iArr[i2] = iArr[indexOf];
        }
        this.mCount--;
        v.usageInRowCount--;
        if (removeFromDefinition) {
            v.removeFromRow(this.mRow);
        }
        return f;
    }

    public int sizeInBytes() {
        return 0;
    }

    public String toString() {
        String str = hashCode() + " { ";
        int i = this.mCount;
        for (int i2 = 0; i2 < i; i2++) {
            SolverVariable variable = getVariable(i2);
            if (variable != null) {
                int indexOf = indexOf(variable);
                String str2 = (str + variable + " = " + getVariableValue(i2) + " ") + "[p: ";
                String str3 = (this.previous[indexOf] != -1 ? str2 + this.mCache.mIndexedVariables[this.variables[this.previous[indexOf]]] : str2 + "none") + ", n: ";
                str = (this.next[indexOf] != -1 ? str3 + this.mCache.mIndexedVariables[this.variables[this.next[indexOf]]] : str3 + "none") + "]";
            }
        }
        return str + " }";
    }

    public float use(ArrayRow def, boolean removeFromDefinition) {
        float f = get(def.variable);
        remove(def.variable, removeFromDefinition);
        SolverVariableValues solverVariableValues = (SolverVariableValues) def.variables;
        int currentSize = solverVariableValues.getCurrentSize();
        int i = solverVariableValues.head;
        int i2 = 0;
        int i3 = 0;
        while (i2 < currentSize) {
            if (solverVariableValues.variables[i3] != -1) {
                add(this.mCache.mIndexedVariables[solverVariableValues.variables[i3]], solverVariableValues.values[i3] * f, removeFromDefinition);
                i2++;
            }
            i3++;
        }
        return f;
    }
}
