package androidx.emoji2.text;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import androidx.collection.ArraySet;
import androidx.core.util.Preconditions;
import androidx.emoji2.text.DefaultEmojiCompatConfig;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import okhttp3.HttpUrl;

public class EmojiCompat {
    private static final Object CONFIG_LOCK = new Object();
    public static final String EDITOR_INFO_METAVERSION_KEY = "android.support.text.emoji.emojiCompat_metadataVersion";
    public static final String EDITOR_INFO_REPLACE_ALL_KEY = "android.support.text.emoji.emojiCompat_replaceAll";
    static final int EMOJI_COUNT_UNLIMITED = Integer.MAX_VALUE;
    public static final int EMOJI_FALLBACK = 2;
    public static final int EMOJI_SUPPORTED = 1;
    public static final int EMOJI_UNSUPPORTED = 0;
    private static final Object INSTANCE_LOCK = new Object();
    public static final int LOAD_STATE_DEFAULT = 3;
    public static final int LOAD_STATE_FAILED = 2;
    public static final int LOAD_STATE_LOADING = 0;
    public static final int LOAD_STATE_SUCCEEDED = 1;
    public static final int LOAD_STRATEGY_DEFAULT = 0;
    public static final int LOAD_STRATEGY_MANUAL = 1;
    private static final String NOT_INITIALIZED_ERROR_TEXT = "EmojiCompat is not initialized.\n\nYou must initialize EmojiCompat prior to referencing the EmojiCompat instance.\n\nThe most likely cause of this error is disabling the EmojiCompatInitializer\neither explicitly in AndroidManifest.xml, or by including\nandroidx.emoji2:emoji2-bundled.\n\nAutomatic initialization is typically performed by EmojiCompatInitializer. If\nyou are not expecting to initialize EmojiCompat manually in your application,\nplease check to ensure it has not been removed from your APK's manifest. You can\ndo this in Android Studio using Build > Analyze APK.\n\nIn the APK Analyzer, ensure that the startup entry for\nEmojiCompatInitializer and InitializationProvider is present in\n AndroidManifest.xml. If it is missing or contains tools:node=\"remove\", and you\nintend to use automatic configuration, verify:\n\n  1. Your application does not include emoji2-bundled\n  2. All modules do not contain an exclusion manifest rule for\n     EmojiCompatInitializer or InitializationProvider. For more information\n     about manifest exclusions see the documentation for the androidx startup\n     library.\n\nIf you intend to use emoji2-bundled, please call EmojiCompat.init. You can\nlearn more in the documentation for BundledEmojiCompatConfig.\n\nIf you intended to perform manual configuration, it is recommended that you call\nEmojiCompat.init immediately on application startup.\n\nIf you still cannot resolve this issue, please open a bug with your specific\nconfiguration to help improve error message.";
    public static final int REPLACE_STRATEGY_ALL = 1;
    public static final int REPLACE_STRATEGY_DEFAULT = 0;
    public static final int REPLACE_STRATEGY_NON_EXISTENT = 2;
    private static volatile boolean sHasDoneDefaultConfigLookup;
    private static volatile EmojiCompat sInstance;
    final int[] mEmojiAsDefaultStyleExceptions;
    private final int mEmojiSpanIndicatorColor;
    private final boolean mEmojiSpanIndicatorEnabled;
    /* access modifiers changed from: private */
    public final GlyphChecker mGlyphChecker;
    private final CompatInternal mHelper;
    private final Set<InitCallback> mInitCallbacks;
    private final ReadWriteLock mInitLock = new ReentrantReadWriteLock();
    private volatile int mLoadState = 3;
    private final Handler mMainHandler;
    private final int mMetadataLoadStrategy;
    final MetadataRepoLoader mMetadataLoader;
    final boolean mReplaceAll;
    final boolean mUseEmojiAsDefaultStyle;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CodepointSequenceMatchResult {
    }

    private static class CompatInternal {
        final EmojiCompat mEmojiCompat;

        CompatInternal(EmojiCompat emojiCompat) {
            this.mEmojiCompat = emojiCompat;
        }

        /* access modifiers changed from: package-private */
        public String getAssetSignature() {
            return HttpUrl.FRAGMENT_ENCODE_SET;
        }

        public int getEmojiMatch(CharSequence sequence, int metadataVersion) {
            return 0;
        }

        /* access modifiers changed from: package-private */
        public boolean hasEmojiGlyph(CharSequence sequence) {
            return false;
        }

        /* access modifiers changed from: package-private */
        public boolean hasEmojiGlyph(CharSequence sequence, int metadataVersion) {
            return false;
        }

        /* access modifiers changed from: package-private */
        public void loadMetadata() {
            this.mEmojiCompat.onMetadataLoadSuccess();
        }

        /* access modifiers changed from: package-private */
        public CharSequence process(CharSequence charSequence, int start, int end, int maxEmojiCount, boolean replaceAll) {
            return charSequence;
        }

        /* access modifiers changed from: package-private */
        public void updateEditorInfoAttrs(EditorInfo outAttrs) {
        }
    }

    private static final class CompatInternal19 extends CompatInternal {
        private volatile MetadataRepo mMetadataRepo;
        private volatile EmojiProcessor mProcessor;

        CompatInternal19(EmojiCompat emojiCompat) {
            super(emojiCompat);
        }

        /* access modifiers changed from: package-private */
        public String getAssetSignature() {
            String sourceSha = this.mMetadataRepo.getMetadataList().sourceSha();
            return sourceSha == null ? HttpUrl.FRAGMENT_ENCODE_SET : sourceSha;
        }

        public int getEmojiMatch(CharSequence sequence, int metadataVersion) {
            return this.mProcessor.getEmojiMatch(sequence, metadataVersion);
        }

        /* access modifiers changed from: package-private */
        public boolean hasEmojiGlyph(CharSequence sequence) {
            return this.mProcessor.getEmojiMatch(sequence) == 1;
        }

        /* access modifiers changed from: package-private */
        public boolean hasEmojiGlyph(CharSequence sequence, int metadataVersion) {
            return this.mProcessor.getEmojiMatch(sequence, metadataVersion) == 1;
        }

        /* access modifiers changed from: package-private */
        public void loadMetadata() {
            try {
                this.mEmojiCompat.mMetadataLoader.load(new MetadataRepoLoaderCallback() {
                    public void onFailed(Throwable throwable) {
                        CompatInternal19.this.mEmojiCompat.onMetadataLoadFailed(throwable);
                    }

                    public void onLoaded(MetadataRepo metadataRepo) {
                        CompatInternal19.this.onMetadataLoadSuccess(metadataRepo);
                    }
                });
            } catch (Throwable th) {
                this.mEmojiCompat.onMetadataLoadFailed(th);
            }
        }

        /* access modifiers changed from: package-private */
        public void onMetadataLoadSuccess(MetadataRepo metadataRepo) {
            if (metadataRepo == null) {
                this.mEmojiCompat.onMetadataLoadFailed(new IllegalArgumentException("metadataRepo cannot be null"));
                return;
            }
            this.mMetadataRepo = metadataRepo;
            this.mProcessor = new EmojiProcessor(this.mMetadataRepo, new SpanFactory(), this.mEmojiCompat.mGlyphChecker, this.mEmojiCompat.mUseEmojiAsDefaultStyle, this.mEmojiCompat.mEmojiAsDefaultStyleExceptions);
            this.mEmojiCompat.onMetadataLoadSuccess();
        }

        /* access modifiers changed from: package-private */
        public CharSequence process(CharSequence charSequence, int start, int end, int maxEmojiCount, boolean replaceAll) {
            return this.mProcessor.process(charSequence, start, end, maxEmojiCount, replaceAll);
        }

        /* access modifiers changed from: package-private */
        public void updateEditorInfoAttrs(EditorInfo outAttrs) {
            outAttrs.extras.putInt(EmojiCompat.EDITOR_INFO_METAVERSION_KEY, this.mMetadataRepo.getMetadataVersion());
            outAttrs.extras.putBoolean(EmojiCompat.EDITOR_INFO_REPLACE_ALL_KEY, this.mEmojiCompat.mReplaceAll);
        }
    }

    public static abstract class Config {
        int[] mEmojiAsDefaultStyleExceptions;
        int mEmojiSpanIndicatorColor = -16711936;
        boolean mEmojiSpanIndicatorEnabled;
        GlyphChecker mGlyphChecker = new DefaultGlyphChecker();
        Set<InitCallback> mInitCallbacks;
        int mMetadataLoadStrategy = 0;
        final MetadataRepoLoader mMetadataLoader;
        boolean mReplaceAll;
        boolean mUseEmojiAsDefaultStyle;

        protected Config(MetadataRepoLoader metadataLoader) {
            Preconditions.checkNotNull(metadataLoader, "metadataLoader cannot be null.");
            this.mMetadataLoader = metadataLoader;
        }

        /* access modifiers changed from: protected */
        public final MetadataRepoLoader getMetadataRepoLoader() {
            return this.mMetadataLoader;
        }

        public Config registerInitCallback(InitCallback initCallback) {
            Preconditions.checkNotNull(initCallback, "initCallback cannot be null");
            if (this.mInitCallbacks == null) {
                this.mInitCallbacks = new ArraySet();
            }
            this.mInitCallbacks.add(initCallback);
            return this;
        }

        public Config setEmojiSpanIndicatorColor(int color) {
            this.mEmojiSpanIndicatorColor = color;
            return this;
        }

        public Config setEmojiSpanIndicatorEnabled(boolean emojiSpanIndicatorEnabled) {
            this.mEmojiSpanIndicatorEnabled = emojiSpanIndicatorEnabled;
            return this;
        }

        public Config setGlyphChecker(GlyphChecker glyphChecker) {
            Preconditions.checkNotNull(glyphChecker, "GlyphChecker cannot be null");
            this.mGlyphChecker = glyphChecker;
            return this;
        }

        public Config setMetadataLoadStrategy(int strategy) {
            this.mMetadataLoadStrategy = strategy;
            return this;
        }

        public Config setReplaceAll(boolean replaceAll) {
            this.mReplaceAll = replaceAll;
            return this;
        }

        public Config setUseEmojiAsDefaultStyle(boolean useEmojiAsDefaultStyle) {
            return setUseEmojiAsDefaultStyle(useEmojiAsDefaultStyle, (List<Integer>) null);
        }

        public Config setUseEmojiAsDefaultStyle(boolean useEmojiAsDefaultStyle, List<Integer> list) {
            this.mUseEmojiAsDefaultStyle = useEmojiAsDefaultStyle;
            if (!useEmojiAsDefaultStyle || list == null) {
                this.mEmojiAsDefaultStyleExceptions = null;
            } else {
                this.mEmojiAsDefaultStyleExceptions = new int[list.size()];
                int i = 0;
                for (Integer intValue : list) {
                    this.mEmojiAsDefaultStyleExceptions[i] = intValue.intValue();
                    i++;
                }
                Arrays.sort(this.mEmojiAsDefaultStyleExceptions);
            }
            return this;
        }

        public Config unregisterInitCallback(InitCallback initCallback) {
            Preconditions.checkNotNull(initCallback, "initCallback cannot be null");
            Set<InitCallback> set = this.mInitCallbacks;
            if (set != null) {
                set.remove(initCallback);
            }
            return this;
        }
    }

    public interface GlyphChecker {
        boolean hasGlyph(CharSequence charSequence, int i, int i2, int i3);
    }

    public static abstract class InitCallback {
        public void onFailed(Throwable throwable) {
        }

        public void onInitialized() {
        }
    }

    private static class ListenerDispatcher implements Runnable {
        private final List<InitCallback> mInitCallbacks;
        private final int mLoadState;
        private final Throwable mThrowable;

        ListenerDispatcher(InitCallback initCallback, int loadState) {
            this(Arrays.asList(new InitCallback[]{(InitCallback) Preconditions.checkNotNull(initCallback, "initCallback cannot be null")}), loadState, (Throwable) null);
        }

        ListenerDispatcher(Collection<InitCallback> collection, int loadState) {
            this(collection, loadState, (Throwable) null);
        }

        ListenerDispatcher(Collection<InitCallback> collection, int loadState, Throwable throwable) {
            Preconditions.checkNotNull(collection, "initCallbacks cannot be null");
            this.mInitCallbacks = new ArrayList(collection);
            this.mLoadState = loadState;
            this.mThrowable = throwable;
        }

        public void run() {
            int size = this.mInitCallbacks.size();
            switch (this.mLoadState) {
                case 1:
                    for (int i = 0; i < size; i++) {
                        this.mInitCallbacks.get(i).onInitialized();
                    }
                    return;
                default:
                    for (int i2 = 0; i2 < size; i2++) {
                        this.mInitCallbacks.get(i2).onFailed(this.mThrowable);
                    }
                    return;
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LoadStrategy {
    }

    public interface MetadataRepoLoader {
        void load(MetadataRepoLoaderCallback metadataRepoLoaderCallback);
    }

    public static abstract class MetadataRepoLoaderCallback {
        public abstract void onFailed(Throwable th);

        public abstract void onLoaded(MetadataRepo metadataRepo);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ReplaceStrategy {
    }

    static class SpanFactory {
        SpanFactory() {
        }

        /* access modifiers changed from: package-private */
        public EmojiSpan createSpan(EmojiMetadata metadata) {
            return new TypefaceEmojiSpan(metadata);
        }
    }

    private EmojiCompat(Config config) {
        this.mReplaceAll = config.mReplaceAll;
        this.mUseEmojiAsDefaultStyle = config.mUseEmojiAsDefaultStyle;
        this.mEmojiAsDefaultStyleExceptions = config.mEmojiAsDefaultStyleExceptions;
        this.mEmojiSpanIndicatorEnabled = config.mEmojiSpanIndicatorEnabled;
        this.mEmojiSpanIndicatorColor = config.mEmojiSpanIndicatorColor;
        this.mMetadataLoader = config.mMetadataLoader;
        this.mMetadataLoadStrategy = config.mMetadataLoadStrategy;
        this.mGlyphChecker = config.mGlyphChecker;
        this.mMainHandler = new Handler(Looper.getMainLooper());
        ArraySet arraySet = new ArraySet();
        this.mInitCallbacks = arraySet;
        if (config.mInitCallbacks != null && !config.mInitCallbacks.isEmpty()) {
            arraySet.addAll(config.mInitCallbacks);
        }
        this.mHelper = Build.VERSION.SDK_INT < 19 ? new CompatInternal(this) : new CompatInternal19(this);
        loadMetadata();
    }

    public static EmojiCompat get() {
        EmojiCompat emojiCompat;
        synchronized (INSTANCE_LOCK) {
            emojiCompat = sInstance;
            Preconditions.checkState(emojiCompat != null, NOT_INITIALIZED_ERROR_TEXT);
        }
        return emojiCompat;
    }

    public static boolean handleDeleteSurroundingText(InputConnection inputConnection, Editable editable, int beforeLength, int afterLength, boolean inCodePoints) {
        if (Build.VERSION.SDK_INT >= 19) {
            return EmojiProcessor.handleDeleteSurroundingText(inputConnection, editable, beforeLength, afterLength, inCodePoints);
        }
        return false;
    }

    public static boolean handleOnKeyDown(Editable editable, int keyCode, KeyEvent event) {
        if (Build.VERSION.SDK_INT >= 19) {
            return EmojiProcessor.handleOnKeyDown(editable, keyCode, event);
        }
        return false;
    }

    public static EmojiCompat init(Context context) {
        return init(context, (DefaultEmojiCompatConfig.DefaultEmojiCompatConfigFactory) null);
    }

    public static EmojiCompat init(Context context, DefaultEmojiCompatConfig.DefaultEmojiCompatConfigFactory defaultFactory) {
        EmojiCompat emojiCompat;
        if (sHasDoneDefaultConfigLookup) {
            return sInstance;
        }
        Config create = (defaultFactory != null ? defaultFactory : new DefaultEmojiCompatConfig.DefaultEmojiCompatConfigFactory((DefaultEmojiCompatConfig.DefaultEmojiCompatConfigHelper) null)).create(context);
        synchronized (CONFIG_LOCK) {
            if (!sHasDoneDefaultConfigLookup) {
                if (create != null) {
                    init(create);
                }
                sHasDoneDefaultConfigLookup = true;
            }
            emojiCompat = sInstance;
        }
        return emojiCompat;
    }

    public static EmojiCompat init(Config config) {
        EmojiCompat emojiCompat = sInstance;
        if (emojiCompat == null) {
            synchronized (INSTANCE_LOCK) {
                emojiCompat = sInstance;
                if (emojiCompat == null) {
                    emojiCompat = new EmojiCompat(config);
                    sInstance = emojiCompat;
                }
            }
        }
        return emojiCompat;
    }

    public static boolean isConfigured() {
        return sInstance != null;
    }

    private boolean isInitialized() {
        return getLoadState() == 1;
    }

    /* JADX INFO: finally extract failed */
    private void loadMetadata() {
        this.mInitLock.writeLock().lock();
        try {
            if (this.mMetadataLoadStrategy == 0) {
                this.mLoadState = 0;
            }
            this.mInitLock.writeLock().unlock();
            if (getLoadState() == 0) {
                this.mHelper.loadMetadata();
            }
        } catch (Throwable th) {
            this.mInitLock.writeLock().unlock();
            throw th;
        }
    }

    public static EmojiCompat reset(Config config) {
        EmojiCompat emojiCompat;
        synchronized (INSTANCE_LOCK) {
            emojiCompat = new EmojiCompat(config);
            sInstance = emojiCompat;
        }
        return emojiCompat;
    }

    public static EmojiCompat reset(EmojiCompat emojiCompat) {
        EmojiCompat emojiCompat2;
        synchronized (INSTANCE_LOCK) {
            sInstance = emojiCompat;
            emojiCompat2 = sInstance;
        }
        return emojiCompat2;
    }

    public static void skipDefaultConfigurationLookup(boolean shouldSkip) {
        synchronized (CONFIG_LOCK) {
            sHasDoneDefaultConfigLookup = shouldSkip;
        }
    }

    public String getAssetSignature() {
        Preconditions.checkState(isInitialized(), "Not initialized yet");
        return this.mHelper.getAssetSignature();
    }

    public int getEmojiMatch(CharSequence sequence, int metadataVersion) {
        Preconditions.checkState(isInitialized(), "Not initialized yet");
        Preconditions.checkNotNull(sequence, "sequence cannot be null");
        return this.mHelper.getEmojiMatch(sequence, metadataVersion);
    }

    public int getEmojiSpanIndicatorColor() {
        return this.mEmojiSpanIndicatorColor;
    }

    public int getLoadState() {
        this.mInitLock.readLock().lock();
        try {
            return this.mLoadState;
        } finally {
            this.mInitLock.readLock().unlock();
        }
    }

    @Deprecated
    public boolean hasEmojiGlyph(CharSequence sequence) {
        Preconditions.checkState(isInitialized(), "Not initialized yet");
        Preconditions.checkNotNull(sequence, "sequence cannot be null");
        return this.mHelper.hasEmojiGlyph(sequence);
    }

    @Deprecated
    public boolean hasEmojiGlyph(CharSequence sequence, int metadataVersion) {
        Preconditions.checkState(isInitialized(), "Not initialized yet");
        Preconditions.checkNotNull(sequence, "sequence cannot be null");
        return this.mHelper.hasEmojiGlyph(sequence, metadataVersion);
    }

    public boolean isEmojiSpanIndicatorEnabled() {
        return this.mEmojiSpanIndicatorEnabled;
    }

    public void load() {
        boolean z = true;
        if (this.mMetadataLoadStrategy != 1) {
            z = false;
        }
        Preconditions.checkState(z, "Set metadataLoadStrategy to LOAD_STRATEGY_MANUAL to execute manual loading");
        if (!isInitialized()) {
            this.mInitLock.writeLock().lock();
            try {
                if (this.mLoadState != 0) {
                    this.mLoadState = 0;
                    this.mInitLock.writeLock().unlock();
                    this.mHelper.loadMetadata();
                }
            } finally {
                this.mInitLock.writeLock().unlock();
            }
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: package-private */
    public void onMetadataLoadFailed(Throwable throwable) {
        ArrayList arrayList = new ArrayList();
        this.mInitLock.writeLock().lock();
        try {
            this.mLoadState = 2;
            arrayList.addAll(this.mInitCallbacks);
            this.mInitCallbacks.clear();
            this.mInitLock.writeLock().unlock();
            this.mMainHandler.post(new ListenerDispatcher(arrayList, this.mLoadState, throwable));
        } catch (Throwable th) {
            this.mInitLock.writeLock().unlock();
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: package-private */
    public void onMetadataLoadSuccess() {
        ArrayList arrayList = new ArrayList();
        this.mInitLock.writeLock().lock();
        try {
            this.mLoadState = 1;
            arrayList.addAll(this.mInitCallbacks);
            this.mInitCallbacks.clear();
            this.mInitLock.writeLock().unlock();
            this.mMainHandler.post(new ListenerDispatcher((Collection<InitCallback>) arrayList, this.mLoadState));
        } catch (Throwable th) {
            this.mInitLock.writeLock().unlock();
            throw th;
        }
    }

    public CharSequence process(CharSequence charSequence) {
        return process(charSequence, 0, charSequence == null ? 0 : charSequence.length());
    }

    public CharSequence process(CharSequence charSequence, int start, int end) {
        return process(charSequence, start, end, Integer.MAX_VALUE);
    }

    public CharSequence process(CharSequence charSequence, int start, int end, int maxEmojiCount) {
        return process(charSequence, start, end, maxEmojiCount, 0);
    }

    public CharSequence process(CharSequence charSequence, int start, int end, int maxEmojiCount, int replaceStrategy) {
        boolean z;
        Preconditions.checkState(isInitialized(), "Not initialized yet");
        Preconditions.checkArgumentNonnegative(start, "start cannot be negative");
        Preconditions.checkArgumentNonnegative(end, "end cannot be negative");
        Preconditions.checkArgumentNonnegative(maxEmojiCount, "maxEmojiCount cannot be negative");
        boolean z2 = true;
        Preconditions.checkArgument(start <= end, "start should be <= than end");
        if (charSequence == null) {
            return null;
        }
        Preconditions.checkArgument(start <= charSequence.length(), "start should be < than charSequence length");
        if (end > charSequence.length()) {
            z2 = false;
        }
        Preconditions.checkArgument(z2, "end should be < than charSequence length");
        if (charSequence.length() == 0 || start == end) {
            return charSequence;
        }
        switch (replaceStrategy) {
            case 1:
                z = true;
                break;
            case 2:
                z = false;
                break;
            default:
                z = this.mReplaceAll;
                break;
        }
        return this.mHelper.process(charSequence, start, end, maxEmojiCount, z);
    }

    public void registerInitCallback(InitCallback initCallback) {
        Preconditions.checkNotNull(initCallback, "initCallback cannot be null");
        this.mInitLock.writeLock().lock();
        try {
            if (this.mLoadState != 1) {
                if (this.mLoadState != 2) {
                    this.mInitCallbacks.add(initCallback);
                }
            }
            this.mMainHandler.post(new ListenerDispatcher(initCallback, this.mLoadState));
        } finally {
            this.mInitLock.writeLock().unlock();
        }
    }

    public void unregisterInitCallback(InitCallback initCallback) {
        Preconditions.checkNotNull(initCallback, "initCallback cannot be null");
        this.mInitLock.writeLock().lock();
        try {
            this.mInitCallbacks.remove(initCallback);
        } finally {
            this.mInitLock.writeLock().unlock();
        }
    }

    public void updateEditorInfo(EditorInfo outAttrs) {
        if (isInitialized() && outAttrs != null) {
            if (outAttrs.extras == null) {
                outAttrs.extras = new Bundle();
            }
            this.mHelper.updateEditorInfoAttrs(outAttrs);
        }
    }
}
