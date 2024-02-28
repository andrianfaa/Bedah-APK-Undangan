package androidx.emoji2.text;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.os.Build;
import android.util.Log;
import androidx.core.provider.FontRequest;
import androidx.core.util.Preconditions;
import androidx.emoji2.text.EmojiCompat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DefaultEmojiCompatConfig {

    public static class DefaultEmojiCompatConfigFactory {
        private static final String DEFAULT_EMOJI_QUERY = "emojicompat-emoji-font";
        private static final String INTENT_LOAD_EMOJI_FONT = "androidx.content.action.LOAD_EMOJI_FONT";
        private static final String TAG = "emoji2.text.DefaultEmojiConfig";
        private final DefaultEmojiCompatConfigHelper mHelper;

        public DefaultEmojiCompatConfigFactory(DefaultEmojiCompatConfigHelper helper) {
            this.mHelper = helper != null ? helper : getHelperForApi();
        }

        private EmojiCompat.Config configOrNull(Context context, FontRequest fontRequest) {
            if (fontRequest == null) {
                return null;
            }
            return new FontRequestEmojiCompatConfig(context, fontRequest);
        }

        private List<List<byte[]>> convertToByteArray(Signature[] signatures) {
            ArrayList arrayList = new ArrayList();
            for (Signature byteArray : signatures) {
                arrayList.add(byteArray.toByteArray());
            }
            return Collections.singletonList(arrayList);
        }

        private FontRequest generateFontRequestFrom(ProviderInfo providerInfo, PackageManager packageManager) throws PackageManager.NameNotFoundException {
            String str = providerInfo.authority;
            String str2 = providerInfo.packageName;
            return new FontRequest(str, str2, DEFAULT_EMOJI_QUERY, convertToByteArray(this.mHelper.getSigningSignatures(packageManager, str2)));
        }

        private static DefaultEmojiCompatConfigHelper getHelperForApi() {
            return Build.VERSION.SDK_INT >= 28 ? new DefaultEmojiCompatConfigHelper_API28() : Build.VERSION.SDK_INT >= 19 ? new DefaultEmojiCompatConfigHelper_API19() : new DefaultEmojiCompatConfigHelper();
        }

        private boolean hasFlagSystem(ProviderInfo providerInfo) {
            return (providerInfo == null || providerInfo.applicationInfo == null || (providerInfo.applicationInfo.flags & 1) != 1) ? false : true;
        }

        private ProviderInfo queryDefaultInstalledContentProvider(PackageManager packageManager) {
            for (ResolveInfo providerInfo : this.mHelper.queryIntentContentProviders(packageManager, new Intent(INTENT_LOAD_EMOJI_FONT), 0)) {
                ProviderInfo providerInfo2 = this.mHelper.getProviderInfo(providerInfo);
                if (hasFlagSystem(providerInfo2)) {
                    return providerInfo2;
                }
            }
            return null;
        }

        public EmojiCompat.Config create(Context context) {
            return configOrNull(context, queryForDefaultFontRequest(context));
        }

        /* access modifiers changed from: package-private */
        public FontRequest queryForDefaultFontRequest(Context context) {
            PackageManager packageManager = context.getPackageManager();
            Preconditions.checkNotNull(packageManager, "Package manager required to locate emoji font provider");
            ProviderInfo queryDefaultInstalledContentProvider = queryDefaultInstalledContentProvider(packageManager);
            if (queryDefaultInstalledContentProvider == null) {
                return null;
            }
            try {
                return generateFontRequestFrom(queryDefaultInstalledContentProvider, packageManager);
            } catch (PackageManager.NameNotFoundException e) {
                Log.wtf(TAG, e);
                return null;
            }
        }
    }

    public static class DefaultEmojiCompatConfigHelper {
        public ProviderInfo getProviderInfo(ResolveInfo resolveInfo) {
            throw new IllegalStateException("Unable to get provider info prior to API 19");
        }

        public Signature[] getSigningSignatures(PackageManager packageManager, String providerPackage) throws PackageManager.NameNotFoundException {
            return packageManager.getPackageInfo(providerPackage, 64).signatures;
        }

        public List<ResolveInfo> queryIntentContentProviders(PackageManager packageManager, Intent intent, int flags) {
            return Collections.emptyList();
        }
    }

    public static class DefaultEmojiCompatConfigHelper_API19 extends DefaultEmojiCompatConfigHelper {
        public ProviderInfo getProviderInfo(ResolveInfo resolveInfo) {
            return resolveInfo.providerInfo;
        }

        public List<ResolveInfo> queryIntentContentProviders(PackageManager packageManager, Intent intent, int flags) {
            return packageManager.queryIntentContentProviders(intent, flags);
        }
    }

    public static class DefaultEmojiCompatConfigHelper_API28 extends DefaultEmojiCompatConfigHelper_API19 {
        public Signature[] getSigningSignatures(PackageManager packageManager, String providerPackage) throws PackageManager.NameNotFoundException {
            return packageManager.getPackageInfo(providerPackage, 64).signatures;
        }
    }

    private DefaultEmojiCompatConfig() {
    }

    public static FontRequestEmojiCompatConfig create(Context context) {
        return (FontRequestEmojiCompatConfig) new DefaultEmojiCompatConfigFactory((DefaultEmojiCompatConfigHelper) null).create(context);
    }
}
