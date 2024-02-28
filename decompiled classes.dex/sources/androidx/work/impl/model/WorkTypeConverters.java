package androidx.work.impl.model;

import android.os.Build;
import androidx.work.BackoffPolicy;
import androidx.work.NetworkType;
import androidx.work.OutOfQuotaPolicy;
import androidx.work.WorkInfo;

public class WorkTypeConverters {

    /* renamed from: androidx.work.impl.model.WorkTypeConverters$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$work$BackoffPolicy;
        static final /* synthetic */ int[] $SwitchMap$androidx$work$NetworkType;
        static final /* synthetic */ int[] $SwitchMap$androidx$work$OutOfQuotaPolicy;
        static final /* synthetic */ int[] $SwitchMap$androidx$work$WorkInfo$State;

        static {
            int[] iArr = new int[OutOfQuotaPolicy.values().length];
            $SwitchMap$androidx$work$OutOfQuotaPolicy = iArr;
            try {
                iArr[OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$work$OutOfQuotaPolicy[OutOfQuotaPolicy.DROP_WORK_REQUEST.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            int[] iArr2 = new int[NetworkType.values().length];
            $SwitchMap$androidx$work$NetworkType = iArr2;
            try {
                iArr2[NetworkType.NOT_REQUIRED.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$work$NetworkType[NetworkType.CONNECTED.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$androidx$work$NetworkType[NetworkType.UNMETERED.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$androidx$work$NetworkType[NetworkType.NOT_ROAMING.ordinal()] = 4;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$androidx$work$NetworkType[NetworkType.METERED.ordinal()] = 5;
            } catch (NoSuchFieldError e7) {
            }
            int[] iArr3 = new int[BackoffPolicy.values().length];
            $SwitchMap$androidx$work$BackoffPolicy = iArr3;
            try {
                iArr3[BackoffPolicy.EXPONENTIAL.ordinal()] = 1;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$androidx$work$BackoffPolicy[BackoffPolicy.LINEAR.ordinal()] = 2;
            } catch (NoSuchFieldError e9) {
            }
            int[] iArr4 = new int[WorkInfo.State.values().length];
            $SwitchMap$androidx$work$WorkInfo$State = iArr4;
            try {
                iArr4[WorkInfo.State.ENQUEUED.ordinal()] = 1;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$androidx$work$WorkInfo$State[WorkInfo.State.RUNNING.ordinal()] = 2;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$androidx$work$WorkInfo$State[WorkInfo.State.SUCCEEDED.ordinal()] = 3;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$androidx$work$WorkInfo$State[WorkInfo.State.FAILED.ordinal()] = 4;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$androidx$work$WorkInfo$State[WorkInfo.State.BLOCKED.ordinal()] = 5;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$androidx$work$WorkInfo$State[WorkInfo.State.CANCELLED.ordinal()] = 6;
            } catch (NoSuchFieldError e15) {
            }
        }
    }

    public interface BackoffPolicyIds {
        public static final int EXPONENTIAL = 0;
        public static final int LINEAR = 1;
    }

    public interface NetworkTypeIds {
        public static final int CONNECTED = 1;
        public static final int METERED = 4;
        public static final int NOT_REQUIRED = 0;
        public static final int NOT_ROAMING = 3;
        public static final int TEMPORARILY_UNMETERED = 5;
        public static final int UNMETERED = 2;
    }

    public interface OutOfPolicyIds {
        public static final int DROP_WORK_REQUEST = 1;
        public static final int RUN_AS_NON_EXPEDITED_WORK_REQUEST = 0;
    }

    public interface StateIds {
        public static final int BLOCKED = 4;
        public static final int CANCELLED = 5;
        public static final String COMPLETED_STATES = "(2, 3, 5)";
        public static final int ENQUEUED = 0;
        public static final int FAILED = 3;
        public static final int RUNNING = 1;
        public static final int SUCCEEDED = 2;
    }

    private WorkTypeConverters() {
    }

    public static int backoffPolicyToInt(BackoffPolicy backoffPolicy) {
        switch (AnonymousClass1.$SwitchMap$androidx$work$BackoffPolicy[backoffPolicy.ordinal()]) {
            case 1:
                return 0;
            case 2:
                return 1;
            default:
                throw new IllegalArgumentException("Could not convert " + backoffPolicy + " to int");
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:13:0x0035=Splitter:B:13:0x0035, B:26:0x004e=Splitter:B:26:0x004e} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static androidx.work.ContentUriTriggers byteArrayToContentUriTriggers(byte[] r6) {
        /*
            androidx.work.ContentUriTriggers r0 = new androidx.work.ContentUriTriggers
            r0.<init>()
            if (r6 != 0) goto L_0x0008
            return r0
        L_0x0008:
            java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream
            r1.<init>(r6)
            r2 = 0
            java.io.ObjectInputStream r3 = new java.io.ObjectInputStream     // Catch:{ IOException -> 0x0040 }
            r3.<init>(r1)     // Catch:{ IOException -> 0x0040 }
            r2 = r3
            int r3 = r2.readInt()     // Catch:{ IOException -> 0x0040 }
        L_0x0018:
            if (r3 <= 0) goto L_0x002c
            java.lang.String r4 = r2.readUTF()     // Catch:{ IOException -> 0x0040 }
            android.net.Uri r4 = android.net.Uri.parse(r4)     // Catch:{ IOException -> 0x0040 }
            boolean r5 = r2.readBoolean()     // Catch:{ IOException -> 0x0040 }
            r0.add(r4, r5)     // Catch:{ IOException -> 0x0040 }
            int r3 = r3 + -1
            goto L_0x0018
        L_0x002c:
            r2.close()     // Catch:{ IOException -> 0x0031 }
            goto L_0x0035
        L_0x0031:
            r3 = move-exception
            r3.printStackTrace()
        L_0x0035:
            r1.close()     // Catch:{ IOException -> 0x0039 }
        L_0x0038:
            goto L_0x0052
        L_0x0039:
            r3 = move-exception
            r3.printStackTrace()
            goto L_0x0052
        L_0x003e:
            r3 = move-exception
            goto L_0x0053
        L_0x0040:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x003e }
            if (r2 == 0) goto L_0x004e
            r2.close()     // Catch:{ IOException -> 0x004a }
            goto L_0x004e
        L_0x004a:
            r3 = move-exception
            r3.printStackTrace()
        L_0x004e:
            r1.close()     // Catch:{ IOException -> 0x0039 }
            goto L_0x0038
        L_0x0052:
            return r0
        L_0x0053:
            if (r2 == 0) goto L_0x005d
            r2.close()     // Catch:{ IOException -> 0x0059 }
            goto L_0x005d
        L_0x0059:
            r4 = move-exception
            r4.printStackTrace()
        L_0x005d:
            r1.close()     // Catch:{ IOException -> 0x0061 }
            goto L_0x0065
        L_0x0061:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0065:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.work.impl.model.WorkTypeConverters.byteArrayToContentUriTriggers(byte[]):androidx.work.ContentUriTriggers");
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:26:0x0064=Splitter:B:26:0x0064, B:13:0x004b=Splitter:B:13:0x004b} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] contentUriTriggersToByteArray(androidx.work.ContentUriTriggers r5) {
        /*
            int r0 = r5.size()
            if (r0 != 0) goto L_0x0008
            r0 = 0
            return r0
        L_0x0008:
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r0.<init>()
            r1 = 0
            java.io.ObjectOutputStream r2 = new java.io.ObjectOutputStream     // Catch:{ IOException -> 0x0056 }
            r2.<init>(r0)     // Catch:{ IOException -> 0x0056 }
            r1 = r2
            int r2 = r5.size()     // Catch:{ IOException -> 0x0056 }
            r1.writeInt(r2)     // Catch:{ IOException -> 0x0056 }
            java.util.Set r2 = r5.getTriggers()     // Catch:{ IOException -> 0x0056 }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ IOException -> 0x0056 }
        L_0x0023:
            boolean r3 = r2.hasNext()     // Catch:{ IOException -> 0x0056 }
            if (r3 == 0) goto L_0x0042
            java.lang.Object r3 = r2.next()     // Catch:{ IOException -> 0x0056 }
            androidx.work.ContentUriTriggers$Trigger r3 = (androidx.work.ContentUriTriggers.Trigger) r3     // Catch:{ IOException -> 0x0056 }
            android.net.Uri r4 = r3.getUri()     // Catch:{ IOException -> 0x0056 }
            java.lang.String r4 = r4.toString()     // Catch:{ IOException -> 0x0056 }
            r1.writeUTF(r4)     // Catch:{ IOException -> 0x0056 }
            boolean r4 = r3.shouldTriggerForDescendants()     // Catch:{ IOException -> 0x0056 }
            r1.writeBoolean(r4)     // Catch:{ IOException -> 0x0056 }
            goto L_0x0023
        L_0x0042:
            r1.close()     // Catch:{ IOException -> 0x0047 }
            goto L_0x004b
        L_0x0047:
            r2 = move-exception
            r2.printStackTrace()
        L_0x004b:
            r0.close()     // Catch:{ IOException -> 0x004f }
        L_0x004e:
            goto L_0x0068
        L_0x004f:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0068
        L_0x0054:
            r2 = move-exception
            goto L_0x006d
        L_0x0056:
            r2 = move-exception
            r2.printStackTrace()     // Catch:{ all -> 0x0054 }
            if (r1 == 0) goto L_0x0064
            r1.close()     // Catch:{ IOException -> 0x0060 }
            goto L_0x0064
        L_0x0060:
            r2 = move-exception
            r2.printStackTrace()
        L_0x0064:
            r0.close()     // Catch:{ IOException -> 0x004f }
            goto L_0x004e
        L_0x0068:
            byte[] r2 = r0.toByteArray()
            return r2
        L_0x006d:
            if (r1 == 0) goto L_0x0077
            r1.close()     // Catch:{ IOException -> 0x0073 }
            goto L_0x0077
        L_0x0073:
            r3 = move-exception
            r3.printStackTrace()
        L_0x0077:
            r0.close()     // Catch:{ IOException -> 0x007b }
            goto L_0x007f
        L_0x007b:
            r3 = move-exception
            r3.printStackTrace()
        L_0x007f:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.work.impl.model.WorkTypeConverters.contentUriTriggersToByteArray(androidx.work.ContentUriTriggers):byte[]");
    }

    public static BackoffPolicy intToBackoffPolicy(int value) {
        switch (value) {
            case 0:
                return BackoffPolicy.EXPONENTIAL;
            case 1:
                return BackoffPolicy.LINEAR;
            default:
                throw new IllegalArgumentException("Could not convert " + value + " to BackoffPolicy");
        }
    }

    public static NetworkType intToNetworkType(int value) {
        switch (value) {
            case 0:
                return NetworkType.NOT_REQUIRED;
            case 1:
                return NetworkType.CONNECTED;
            case 2:
                return NetworkType.UNMETERED;
            case 3:
                return NetworkType.NOT_ROAMING;
            case 4:
                return NetworkType.METERED;
            default:
                if (Build.VERSION.SDK_INT >= 30 && value == 5) {
                    return NetworkType.TEMPORARILY_UNMETERED;
                }
                throw new IllegalArgumentException("Could not convert " + value + " to NetworkType");
        }
    }

    public static OutOfQuotaPolicy intToOutOfQuotaPolicy(int value) {
        switch (value) {
            case 0:
                return OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST;
            case 1:
                return OutOfQuotaPolicy.DROP_WORK_REQUEST;
            default:
                throw new IllegalArgumentException("Could not convert " + value + " to OutOfQuotaPolicy");
        }
    }

    public static WorkInfo.State intToState(int value) {
        switch (value) {
            case 0:
                return WorkInfo.State.ENQUEUED;
            case 1:
                return WorkInfo.State.RUNNING;
            case 2:
                return WorkInfo.State.SUCCEEDED;
            case 3:
                return WorkInfo.State.FAILED;
            case 4:
                return WorkInfo.State.BLOCKED;
            case 5:
                return WorkInfo.State.CANCELLED;
            default:
                throw new IllegalArgumentException("Could not convert " + value + " to State");
        }
    }

    public static int networkTypeToInt(NetworkType networkType) {
        switch (AnonymousClass1.$SwitchMap$androidx$work$NetworkType[networkType.ordinal()]) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            default:
                if (Build.VERSION.SDK_INT >= 30 && networkType == NetworkType.TEMPORARILY_UNMETERED) {
                    return 5;
                }
                throw new IllegalArgumentException("Could not convert " + networkType + " to int");
        }
    }

    public static int outOfQuotaPolicyToInt(OutOfQuotaPolicy policy) {
        switch (AnonymousClass1.$SwitchMap$androidx$work$OutOfQuotaPolicy[policy.ordinal()]) {
            case 1:
                return 0;
            case 2:
                return 1;
            default:
                throw new IllegalArgumentException("Could not convert " + policy + " to int");
        }
    }

    public static int stateToInt(WorkInfo.State state) {
        switch (AnonymousClass1.$SwitchMap$androidx$work$WorkInfo$State[state.ordinal()]) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            default:
                throw new IllegalArgumentException("Could not convert " + state + " to int");
        }
    }
}
