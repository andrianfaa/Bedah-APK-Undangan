package androidx.core.app;

import android.app.RemoteInput;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import mt.Log1F380D;

/* compiled from: 0032 */
public final class RemoteInput {
    public static final int EDIT_CHOICES_BEFORE_SENDING_AUTO = 0;
    public static final int EDIT_CHOICES_BEFORE_SENDING_DISABLED = 1;
    public static final int EDIT_CHOICES_BEFORE_SENDING_ENABLED = 2;
    private static final String EXTRA_DATA_TYPE_RESULTS_DATA = "android.remoteinput.dataTypeResultsData";
    public static final String EXTRA_RESULTS_DATA = "android.remoteinput.resultsData";
    private static final String EXTRA_RESULTS_SOURCE = "android.remoteinput.resultsSource";
    public static final String RESULTS_CLIP_LABEL = "android.remoteinput.results";
    public static final int SOURCE_CHOICE = 1;
    public static final int SOURCE_FREE_FORM_INPUT = 0;
    private final boolean mAllowFreeFormTextInput;
    private final Set<String> mAllowedDataTypes;
    private final CharSequence[] mChoices;
    private final int mEditChoicesBeforeSending;
    private final Bundle mExtras;
    private final CharSequence mLabel;
    private final String mResultKey;

    static class Api16Impl {
        private Api16Impl() {
        }

        static ClipData getClipData(Intent intent) {
            return intent.getClipData();
        }

        static void setClipData(Intent intent, ClipData clip) {
            intent.setClipData(clip);
        }
    }

    static class Api20Impl {
        private Api20Impl() {
        }

        static void addResultsToIntent(Object remoteInputs, Intent intent, Bundle results) {
            android.app.RemoteInput.addResultsToIntent((android.app.RemoteInput[]) remoteInputs, intent, results);
        }

        public static android.app.RemoteInput fromCompat(RemoteInput src) {
            Set<String> allowedDataTypes;
            RemoteInput.Builder addExtras = new RemoteInput.Builder(src.getResultKey()).setLabel(src.getLabel()).setChoices(src.getChoices()).setAllowFreeFormInput(src.getAllowFreeFormInput()).addExtras(src.getExtras());
            if (Build.VERSION.SDK_INT >= 26 && (allowedDataTypes = src.getAllowedDataTypes()) != null) {
                for (String allowDataType : allowedDataTypes) {
                    Api26Impl.setAllowDataType(addExtras, allowDataType, true);
                }
            }
            if (Build.VERSION.SDK_INT >= 29) {
                Api29Impl.setEditChoicesBeforeSending(addExtras, src.getEditChoicesBeforeSending());
            }
            return addExtras.build();
        }

        static RemoteInput fromPlatform(Object srcObj) {
            Set<String> allowedDataTypes;
            android.app.RemoteInput remoteInput = (android.app.RemoteInput) srcObj;
            Builder addExtras = new Builder(remoteInput.getResultKey()).setLabel(remoteInput.getLabel()).setChoices(remoteInput.getChoices()).setAllowFreeFormInput(remoteInput.getAllowFreeFormInput()).addExtras(remoteInput.getExtras());
            if (Build.VERSION.SDK_INT >= 26 && (allowedDataTypes = Api26Impl.getAllowedDataTypes(remoteInput)) != null) {
                for (String allowDataType : allowedDataTypes) {
                    addExtras.setAllowDataType(allowDataType, true);
                }
            }
            if (Build.VERSION.SDK_INT >= 29) {
                addExtras.setEditChoicesBeforeSending(Api29Impl.getEditChoicesBeforeSending(remoteInput));
            }
            return addExtras.build();
        }

        static Bundle getResultsFromIntent(Intent intent) {
            return android.app.RemoteInput.getResultsFromIntent(intent);
        }
    }

    static class Api26Impl {
        private Api26Impl() {
        }

        static void addDataResultToIntent(RemoteInput remoteInput, Intent intent, Map<String, Uri> map) {
            android.app.RemoteInput.addDataResultToIntent(RemoteInput.fromCompat(remoteInput), intent, map);
        }

        static Set<String> getAllowedDataTypes(Object remoteInput) {
            return ((android.app.RemoteInput) remoteInput).getAllowedDataTypes();
        }

        static Map<String, Uri> getDataResultsFromIntent(Intent intent, String remoteInputResultKey) {
            return android.app.RemoteInput.getDataResultsFromIntent(intent, remoteInputResultKey);
        }

        static RemoteInput.Builder setAllowDataType(RemoteInput.Builder builder, String mimeType, boolean doAllow) {
            return builder.setAllowDataType(mimeType, doAllow);
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static int getResultsSource(Intent intent) {
            return android.app.RemoteInput.getResultsSource(intent);
        }

        static void setResultsSource(Intent intent, int source) {
            android.app.RemoteInput.setResultsSource(intent, source);
        }
    }

    static class Api29Impl {
        private Api29Impl() {
        }

        static int getEditChoicesBeforeSending(Object remoteInput) {
            return ((android.app.RemoteInput) remoteInput).getEditChoicesBeforeSending();
        }

        static RemoteInput.Builder setEditChoicesBeforeSending(RemoteInput.Builder builder, int editChoicesBeforeSending) {
            return builder.setEditChoicesBeforeSending(editChoicesBeforeSending);
        }
    }

    public static final class Builder {
        private boolean mAllowFreeFormTextInput = true;
        private final Set<String> mAllowedDataTypes = new HashSet();
        private CharSequence[] mChoices;
        private int mEditChoicesBeforeSending = 0;
        private final Bundle mExtras = new Bundle();
        private CharSequence mLabel;
        private final String mResultKey;

        public Builder(String resultKey) {
            if (resultKey != null) {
                this.mResultKey = resultKey;
                return;
            }
            throw new IllegalArgumentException("Result key can't be null");
        }

        public Builder addExtras(Bundle extras) {
            if (extras != null) {
                this.mExtras.putAll(extras);
            }
            return this;
        }

        public RemoteInput build() {
            return new RemoteInput(this.mResultKey, this.mLabel, this.mChoices, this.mAllowFreeFormTextInput, this.mEditChoicesBeforeSending, this.mExtras, this.mAllowedDataTypes);
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public Builder setAllowDataType(String mimeType, boolean doAllow) {
            if (doAllow) {
                this.mAllowedDataTypes.add(mimeType);
            } else {
                this.mAllowedDataTypes.remove(mimeType);
            }
            return this;
        }

        public Builder setAllowFreeFormInput(boolean allowFreeFormTextInput) {
            this.mAllowFreeFormTextInput = allowFreeFormTextInput;
            return this;
        }

        public Builder setChoices(CharSequence[] choices) {
            this.mChoices = choices;
            return this;
        }

        public Builder setEditChoicesBeforeSending(int editChoicesBeforeSending) {
            this.mEditChoicesBeforeSending = editChoicesBeforeSending;
            return this;
        }

        public Builder setLabel(CharSequence label) {
            this.mLabel = label;
            return this;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EditChoicesBeforeSending {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Source {
    }

    RemoteInput(String resultKey, CharSequence label, CharSequence[] choices, boolean allowFreeFormTextInput, int editChoicesBeforeSending, Bundle extras, Set<String> set) {
        this.mResultKey = resultKey;
        this.mLabel = label;
        this.mChoices = choices;
        this.mAllowFreeFormTextInput = allowFreeFormTextInput;
        this.mEditChoicesBeforeSending = editChoicesBeforeSending;
        this.mExtras = extras;
        this.mAllowedDataTypes = set;
        if (getEditChoicesBeforeSending() == 2 && !getAllowFreeFormInput()) {
            throw new IllegalArgumentException("setEditChoicesBeforeSending requires setAllowFreeFormInput");
        }
    }

    public static void addDataResultToIntent(RemoteInput remoteInput, Intent intent, Map<String, Uri> map) {
        if (Build.VERSION.SDK_INT >= 26) {
            Api26Impl.addDataResultToIntent(remoteInput, intent, map);
        } else if (Build.VERSION.SDK_INT >= 16) {
            Intent clipDataIntentFromIntent = getClipDataIntentFromIntent(intent);
            if (clipDataIntentFromIntent == null) {
                clipDataIntentFromIntent = new Intent();
            }
            for (Map.Entry next : map.entrySet()) {
                String str = (String) next.getKey();
                Uri uri = (Uri) next.getValue();
                if (str != null) {
                    String extraResultsKeyForData = getExtraResultsKeyForData(str);
                    Log1F380D.a((Object) extraResultsKeyForData);
                    Bundle bundleExtra = clipDataIntentFromIntent.getBundleExtra(extraResultsKeyForData);
                    if (bundleExtra == null) {
                        bundleExtra = new Bundle();
                    }
                    bundleExtra.putString(remoteInput.getResultKey(), uri.toString());
                    String extraResultsKeyForData2 = getExtraResultsKeyForData(str);
                    Log1F380D.a((Object) extraResultsKeyForData2);
                    clipDataIntentFromIntent.putExtra(extraResultsKeyForData2, bundleExtra);
                }
            }
            Api16Impl.setClipData(intent, ClipData.newIntent(RESULTS_CLIP_LABEL, clipDataIntentFromIntent));
        }
    }

    public static void addResultsToIntent(RemoteInput[] remoteInputs, Intent intent, Bundle results) {
        if (Build.VERSION.SDK_INT >= 26) {
            Api20Impl.addResultsToIntent(fromCompat(remoteInputs), intent, results);
            return;
        }
        if (Build.VERSION.SDK_INT >= 20) {
            Bundle resultsFromIntent = getResultsFromIntent(intent);
            int resultsSource = getResultsSource(intent);
            if (resultsFromIntent == null) {
                resultsFromIntent = results;
            } else {
                resultsFromIntent.putAll(results);
            }
            for (RemoteInput remoteInput : remoteInputs) {
                Map<String, Uri> dataResultsFromIntent = getDataResultsFromIntent(intent, remoteInput.getResultKey());
                Api20Impl.addResultsToIntent(fromCompat(new RemoteInput[]{remoteInput}), intent, resultsFromIntent);
                if (dataResultsFromIntent != null) {
                    addDataResultToIntent(remoteInput, intent, dataResultsFromIntent);
                }
            }
            setResultsSource(intent, resultsSource);
        } else if (Build.VERSION.SDK_INT >= 16) {
            Intent clipDataIntentFromIntent = getClipDataIntentFromIntent(intent);
            if (clipDataIntentFromIntent == null) {
                clipDataIntentFromIntent = new Intent();
            }
            Bundle bundleExtra = clipDataIntentFromIntent.getBundleExtra(EXTRA_RESULTS_DATA);
            if (bundleExtra == null) {
                bundleExtra = new Bundle();
            }
            for (RemoteInput remoteInput2 : remoteInputs) {
                Object obj = results.get(remoteInput2.getResultKey());
                if (obj instanceof CharSequence) {
                    bundleExtra.putCharSequence(remoteInput2.getResultKey(), (CharSequence) obj);
                }
            }
            clipDataIntentFromIntent.putExtra(EXTRA_RESULTS_DATA, bundleExtra);
            Api16Impl.setClipData(intent, ClipData.newIntent(RESULTS_CLIP_LABEL, clipDataIntentFromIntent));
        }
    }

    static android.app.RemoteInput fromCompat(RemoteInput src) {
        return Api20Impl.fromCompat(src);
    }

    static android.app.RemoteInput[] fromCompat(RemoteInput[] srcArray) {
        if (srcArray == null) {
            return null;
        }
        android.app.RemoteInput[] remoteInputArr = new android.app.RemoteInput[srcArray.length];
        for (int i = 0; i < srcArray.length; i++) {
            remoteInputArr[i] = fromCompat(srcArray[i]);
        }
        return remoteInputArr;
    }

    static RemoteInput fromPlatform(android.app.RemoteInput src) {
        return Api20Impl.fromPlatform(src);
    }

    private static Intent getClipDataIntentFromIntent(Intent intent) {
        ClipData clipData = Api16Impl.getClipData(intent);
        if (clipData == null) {
            return null;
        }
        ClipDescription description = clipData.getDescription();
        if (description.hasMimeType("text/vnd.android.intent") && description.getLabel().toString().contentEquals(RESULTS_CLIP_LABEL)) {
            return clipData.getItemAt(0).getIntent();
        }
        return null;
    }

    public static Map<String, Uri> getDataResultsFromIntent(Intent intent, String remoteInputResultKey) {
        Intent clipDataIntentFromIntent;
        String string;
        if (Build.VERSION.SDK_INT >= 26) {
            return Api26Impl.getDataResultsFromIntent(intent, remoteInputResultKey);
        }
        if (Build.VERSION.SDK_INT < 16 || (clipDataIntentFromIntent = getClipDataIntentFromIntent(intent)) == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        for (String str : clipDataIntentFromIntent.getExtras().keySet()) {
            if (str.startsWith(EXTRA_DATA_TYPE_RESULTS_DATA)) {
                String substring = str.substring(EXTRA_DATA_TYPE_RESULTS_DATA.length());
                if (!substring.isEmpty() && (string = clipDataIntentFromIntent.getBundleExtra(str).getString(remoteInputResultKey)) != null && !string.isEmpty()) {
                    hashMap.put(substring, Uri.parse(string));
                }
            }
        }
        if (hashMap.isEmpty()) {
            return null;
        }
        return hashMap;
    }

    private static String getExtraResultsKeyForData(String mimeType) {
        return EXTRA_DATA_TYPE_RESULTS_DATA + mimeType;
    }

    public static Bundle getResultsFromIntent(Intent intent) {
        Intent clipDataIntentFromIntent;
        if (Build.VERSION.SDK_INT >= 20) {
            return Api20Impl.getResultsFromIntent(intent);
        }
        if (Build.VERSION.SDK_INT < 16 || (clipDataIntentFromIntent = getClipDataIntentFromIntent(intent)) == null) {
            return null;
        }
        return (Bundle) clipDataIntentFromIntent.getExtras().getParcelable(EXTRA_RESULTS_DATA);
    }

    public static int getResultsSource(Intent intent) {
        Intent clipDataIntentFromIntent;
        if (Build.VERSION.SDK_INT >= 28) {
            return Api28Impl.getResultsSource(intent);
        }
        if (Build.VERSION.SDK_INT < 16 || (clipDataIntentFromIntent = getClipDataIntentFromIntent(intent)) == null) {
            return 0;
        }
        return clipDataIntentFromIntent.getExtras().getInt(EXTRA_RESULTS_SOURCE, 0);
    }

    public static void setResultsSource(Intent intent, int source) {
        if (Build.VERSION.SDK_INT >= 28) {
            Api28Impl.setResultsSource(intent, source);
        } else if (Build.VERSION.SDK_INT >= 16) {
            Intent clipDataIntentFromIntent = getClipDataIntentFromIntent(intent);
            if (clipDataIntentFromIntent == null) {
                clipDataIntentFromIntent = new Intent();
            }
            clipDataIntentFromIntent.putExtra(EXTRA_RESULTS_SOURCE, source);
            Api16Impl.setClipData(intent, ClipData.newIntent(RESULTS_CLIP_LABEL, clipDataIntentFromIntent));
        }
    }

    public boolean getAllowFreeFormInput() {
        return this.mAllowFreeFormTextInput;
    }

    public Set<String> getAllowedDataTypes() {
        return this.mAllowedDataTypes;
    }

    public CharSequence[] getChoices() {
        return this.mChoices;
    }

    public int getEditChoicesBeforeSending() {
        return this.mEditChoicesBeforeSending;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public CharSequence getLabel() {
        return this.mLabel;
    }

    public String getResultKey() {
        return this.mResultKey;
    }

    public boolean isDataOnly() {
        return !getAllowFreeFormInput() && (getChoices() == null || getChoices().length == 0) && getAllowedDataTypes() != null && !getAllowedDataTypes().isEmpty();
    }
}
