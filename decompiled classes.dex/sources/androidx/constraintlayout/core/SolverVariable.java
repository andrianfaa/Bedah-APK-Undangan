package androidx.constraintlayout.core;

import java.util.Arrays;
import java.util.HashSet;
import okhttp3.HttpUrl;

public class SolverVariable implements Comparable<SolverVariable> {
    private static final boolean INTERNAL_DEBUG = false;
    static final int MAX_STRENGTH = 9;
    public static final int STRENGTH_BARRIER = 6;
    public static final int STRENGTH_CENTERING = 7;
    public static final int STRENGTH_EQUALITY = 5;
    public static final int STRENGTH_FIXED = 8;
    public static final int STRENGTH_HIGH = 3;
    public static final int STRENGTH_HIGHEST = 4;
    public static final int STRENGTH_LOW = 1;
    public static final int STRENGTH_MEDIUM = 2;
    public static final int STRENGTH_NONE = 0;
    private static final boolean VAR_USE_HASH = false;
    private static int uniqueConstantId = 1;
    private static int uniqueErrorId = 1;
    private static int uniqueId = 1;
    private static int uniqueSlackId = 1;
    private static int uniqueUnrestrictedId = 1;
    public float computedValue;
    int definitionId = -1;
    float[] goalStrengthVector = new float[9];
    public int id = -1;
    public boolean inGoal;
    HashSet<ArrayRow> inRows = null;
    public boolean isFinalValue = false;
    boolean isSynonym = false;
    ArrayRow[] mClientEquations = new ArrayRow[16];
    int mClientEquationsCount = 0;
    private String mName;
    Type mType;
    public int strength = 0;
    float[] strengthVector = new float[9];
    int synonym = -1;
    float synonymDelta = 0.0f;
    public int usageInRowCount = 0;

    /* renamed from: androidx.constraintlayout.core.SolverVariable$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$core$SolverVariable$Type;

        static {
            int[] iArr = new int[Type.values().length];
            $SwitchMap$androidx$constraintlayout$core$SolverVariable$Type = iArr;
            try {
                iArr[Type.UNRESTRICTED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$SolverVariable$Type[Type.CONSTANT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$SolverVariable$Type[Type.SLACK.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$SolverVariable$Type[Type.ERROR.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$SolverVariable$Type[Type.UNKNOWN.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public enum Type {
        UNRESTRICTED,
        CONSTANT,
        SLACK,
        ERROR,
        UNKNOWN
    }

    public SolverVariable(Type type, String prefix) {
        this.mType = type;
    }

    public SolverVariable(String name, Type type) {
        this.mName = name;
        this.mType = type;
    }

    private static String getUniqueName(Type type, String prefix) {
        if (prefix != null) {
            return prefix + uniqueErrorId;
        }
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$SolverVariable$Type[type.ordinal()]) {
            case 1:
                StringBuilder append = new StringBuilder().append("U");
                int i = uniqueUnrestrictedId + 1;
                uniqueUnrestrictedId = i;
                return append.append(i).toString();
            case 2:
                StringBuilder append2 = new StringBuilder().append("C");
                int i2 = uniqueConstantId + 1;
                uniqueConstantId = i2;
                return append2.append(i2).toString();
            case 3:
                StringBuilder append3 = new StringBuilder().append("S");
                int i3 = uniqueSlackId + 1;
                uniqueSlackId = i3;
                return append3.append(i3).toString();
            case 4:
                StringBuilder append4 = new StringBuilder().append("e");
                int i4 = uniqueErrorId + 1;
                uniqueErrorId = i4;
                return append4.append(i4).toString();
            case 5:
                StringBuilder append5 = new StringBuilder().append("V");
                int i5 = uniqueId + 1;
                uniqueId = i5;
                return append5.append(i5).toString();
            default:
                throw new AssertionError(type.name());
        }
    }

    static void increaseErrorId() {
        uniqueErrorId++;
    }

    public final void addToRow(ArrayRow row) {
        int i = 0;
        while (true) {
            int i2 = this.mClientEquationsCount;
            if (i >= i2) {
                ArrayRow[] arrayRowArr = this.mClientEquations;
                if (i2 >= arrayRowArr.length) {
                    this.mClientEquations = (ArrayRow[]) Arrays.copyOf(arrayRowArr, arrayRowArr.length * 2);
                }
                ArrayRow[] arrayRowArr2 = this.mClientEquations;
                int i3 = this.mClientEquationsCount;
                arrayRowArr2[i3] = row;
                this.mClientEquationsCount = i3 + 1;
                return;
            } else if (this.mClientEquations[i] != row) {
                i++;
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void clearStrengths() {
        for (int i = 0; i < 9; i++) {
            this.strengthVector[i] = 0.0f;
        }
    }

    public int compareTo(SolverVariable v) {
        return this.id - v.id;
    }

    public String getName() {
        return this.mName;
    }

    public final void removeFromRow(ArrayRow row) {
        int i = this.mClientEquationsCount;
        for (int i2 = 0; i2 < i; i2++) {
            if (this.mClientEquations[i2] == row) {
                for (int i3 = i2; i3 < i - 1; i3++) {
                    ArrayRow[] arrayRowArr = this.mClientEquations;
                    arrayRowArr[i3] = arrayRowArr[i3 + 1];
                }
                this.mClientEquationsCount--;
                return;
            }
        }
    }

    public void reset() {
        this.mName = null;
        this.mType = Type.UNKNOWN;
        this.strength = 0;
        this.id = -1;
        this.definitionId = -1;
        this.computedValue = 0.0f;
        this.isFinalValue = false;
        this.isSynonym = false;
        this.synonym = -1;
        this.synonymDelta = 0.0f;
        int i = this.mClientEquationsCount;
        for (int i2 = 0; i2 < i; i2++) {
            this.mClientEquations[i2] = null;
        }
        this.mClientEquationsCount = 0;
        this.usageInRowCount = 0;
        this.inGoal = false;
        Arrays.fill(this.goalStrengthVector, 0.0f);
    }

    public void setFinalValue(LinearSystem system, float value) {
        this.computedValue = value;
        this.isFinalValue = true;
        this.isSynonym = false;
        this.synonym = -1;
        this.synonymDelta = 0.0f;
        int i = this.mClientEquationsCount;
        this.definitionId = -1;
        for (int i2 = 0; i2 < i; i2++) {
            this.mClientEquations[i2].updateFromFinalVariable(system, this, false);
        }
        this.mClientEquationsCount = 0;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setSynonym(LinearSystem system, SolverVariable synonymVariable, float value) {
        this.isSynonym = true;
        this.synonym = synonymVariable.id;
        this.synonymDelta = value;
        int i = this.mClientEquationsCount;
        this.definitionId = -1;
        for (int i2 = 0; i2 < i; i2++) {
            this.mClientEquations[i2].updateFromSynonymVariable(system, this, false);
        }
        this.mClientEquationsCount = 0;
        system.displayReadableRows();
    }

    public void setType(Type type, String prefix) {
        this.mType = type;
    }

    /* access modifiers changed from: package-private */
    public String strengthsToString() {
        String str = this + "[";
        boolean z = false;
        boolean z2 = true;
        int i = 0;
        while (i < this.strengthVector.length) {
            String str2 = str + this.strengthVector[i];
            float[] fArr = this.strengthVector;
            float f = fArr[i];
            if (f > 0.0f) {
                z = false;
            } else if (f < 0.0f) {
                z = true;
            }
            if (f != 0.0f) {
                z2 = false;
            }
            str = i < fArr.length + -1 ? str2 + ", " : str2 + "] ";
            i++;
        }
        if (z) {
            str = str + " (-)";
        }
        return z2 ? str + " (*)" : str;
    }

    public String toString() {
        return this.mName != null ? HttpUrl.FRAGMENT_ENCODE_SET + this.mName : HttpUrl.FRAGMENT_ENCODE_SET + this.id;
    }

    public final void updateReferencesWithNewDefinition(LinearSystem system, ArrayRow definition) {
        int i = this.mClientEquationsCount;
        for (int i2 = 0; i2 < i; i2++) {
            this.mClientEquations[i2].updateFromRow(system, definition, false);
        }
        this.mClientEquationsCount = 0;
    }
}
