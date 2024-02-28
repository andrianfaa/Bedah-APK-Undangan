package androidx.work.impl.background.systemjob;

import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.Context;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.PersistableBundle;
import androidx.core.os.BuildCompat;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ContentUriTriggers;
import androidx.work.Logger;
import androidx.work.NetworkType;
import androidx.work.impl.model.WorkSpec;
import mt.Log1F380D;

/* compiled from: 00C2 */
class SystemJobInfoConverter {
    static final String EXTRA_IS_PERIODIC = "EXTRA_IS_PERIODIC";
    static final String EXTRA_WORK_SPEC_ID = "EXTRA_WORK_SPEC_ID";
    private static final String TAG;
    private final ComponentName mWorkServiceComponent;

    /* renamed from: androidx.work.impl.background.systemjob.SystemJobInfoConverter$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$work$NetworkType;

        static {
            int[] iArr = new int[NetworkType.values().length];
            $SwitchMap$androidx$work$NetworkType = iArr;
            try {
                iArr[NetworkType.NOT_REQUIRED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$work$NetworkType[NetworkType.CONNECTED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$work$NetworkType[NetworkType.UNMETERED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$work$NetworkType[NetworkType.NOT_ROAMING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$androidx$work$NetworkType[NetworkType.METERED.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    static {
        String tagWithPrefix = Logger.tagWithPrefix("SystemJobInfoConverter");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    SystemJobInfoConverter(Context context) {
        this.mWorkServiceComponent = new ComponentName(context.getApplicationContext(), SystemJobService.class);
    }

    private static JobInfo.TriggerContentUri convertContentUriTrigger(ContentUriTriggers.Trigger trigger) {
        return new JobInfo.TriggerContentUri(trigger.getUri(), trigger.shouldTriggerForDescendants() ? 1 : 0);
    }

    static void setRequiredNetwork(JobInfo.Builder builder, NetworkType networkType) {
        if (Build.VERSION.SDK_INT < 30 || networkType != NetworkType.TEMPORARILY_UNMETERED) {
            builder.setRequiredNetworkType(convertNetworkType(networkType));
        } else {
            builder.setRequiredNetwork(new NetworkRequest.Builder().addCapability(25).build());
        }
    }

    /* access modifiers changed from: package-private */
    public JobInfo convert(WorkSpec workSpec, int jobId) {
        WorkSpec workSpec2 = workSpec;
        Constraints constraints = workSpec2.constraints;
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putString(EXTRA_WORK_SPEC_ID, workSpec2.id);
        persistableBundle.putBoolean(EXTRA_IS_PERIODIC, workSpec.isPeriodic());
        JobInfo.Builder extras = new JobInfo.Builder(jobId, this.mWorkServiceComponent).setRequiresCharging(constraints.requiresCharging()).setRequiresDeviceIdle(constraints.requiresDeviceIdle()).setExtras(persistableBundle);
        setRequiredNetwork(extras, constraints.getRequiredNetworkType());
        if (!constraints.requiresDeviceIdle()) {
            extras.setBackoffCriteria(workSpec2.backoffDelayDuration, workSpec2.backoffPolicy == BackoffPolicy.LINEAR ? 0 : 1);
        }
        long max = Math.max(workSpec.calculateNextRunTime() - System.currentTimeMillis(), 0);
        if (Build.VERSION.SDK_INT <= 28) {
            extras.setMinimumLatency(max);
        } else if (max > 0) {
            extras.setMinimumLatency(max);
        } else if (!workSpec2.expedited) {
            extras.setImportantWhileForeground(true);
        }
        if (Build.VERSION.SDK_INT >= 24 && constraints.hasContentUriTriggers()) {
            for (ContentUriTriggers.Trigger convertContentUriTrigger : constraints.getContentUriTriggers().getTriggers()) {
                extras.addTriggerContentUri(convertContentUriTrigger(convertContentUriTrigger));
            }
            extras.setTriggerContentUpdateDelay(constraints.getTriggerContentUpdateDelay());
            extras.setTriggerContentMaxDelay(constraints.getTriggerMaxContentDelay());
        }
        boolean z = false;
        extras.setPersisted(false);
        if (Build.VERSION.SDK_INT >= 26) {
            extras.setRequiresBatteryNotLow(constraints.requiresBatteryNotLow());
            extras.setRequiresStorageNotLow(constraints.requiresStorageNotLow());
        }
        boolean z2 = workSpec2.runAttemptCount > 0;
        if (max > 0) {
            z = true;
        }
        if (BuildCompat.isAtLeastS() && workSpec2.expedited && !z2 && !z) {
            extras.setExpedited(true);
        }
        return extras.build();
    }

    static int convertNetworkType(NetworkType networkType) {
        switch (AnonymousClass1.$SwitchMap$androidx$work$NetworkType[networkType.ordinal()]) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                if (Build.VERSION.SDK_INT >= 24) {
                    return 3;
                }
                break;
            case 5:
                if (Build.VERSION.SDK_INT >= 26) {
                    return 4;
                }
                break;
        }
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("API version too low. Cannot convert network type value %s", new Object[]{networkType});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        return 1;
    }
}
