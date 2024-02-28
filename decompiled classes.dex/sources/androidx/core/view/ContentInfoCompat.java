package androidx.core.view;

import android.content.ClipData;
import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.ContentInfo;
import androidx.core.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import mt.Log1F380D;
import okhttp3.HttpUrl;

/* compiled from: 005F */
public final class ContentInfoCompat {
    public static final int FLAG_CONVERT_TO_PLAIN_TEXT = 1;
    public static final int SOURCE_APP = 0;
    public static final int SOURCE_AUTOFILL = 4;
    public static final int SOURCE_CLIPBOARD = 1;
    public static final int SOURCE_DRAG_AND_DROP = 3;
    public static final int SOURCE_INPUT_METHOD = 2;
    public static final int SOURCE_PROCESS_TEXT = 5;
    private final Compat mCompat;

    private static final class Api31Impl {
        private Api31Impl() {
        }

        public static Pair<ContentInfo, ContentInfo> partition(ContentInfo payload, Predicate<ClipData.Item> predicate) {
            ClipData clip = payload.getClip();
            ContentInfo contentInfo = null;
            if (clip.getItemCount() == 1) {
                boolean test = predicate.test(clip.getItemAt(0));
                ContentInfo contentInfo2 = test ? payload : null;
                if (!test) {
                    contentInfo = payload;
                }
                return Pair.create(contentInfo2, contentInfo);
            }
            Objects.requireNonNull(predicate);
            Pair<ClipData, ClipData> partition = ContentInfoCompat.partition(clip, (androidx.core.util.Predicate<ClipData.Item>) new ContentInfoCompat$Api31Impl$$ExternalSyntheticLambda0(predicate));
            return partition.first == null ? Pair.create((Object) null, payload) : partition.second == null ? Pair.create(payload, (Object) null) : Pair.create(new ContentInfo.Builder(payload).setClip((ClipData) partition.first).build(), new ContentInfo.Builder(payload).setClip((ClipData) partition.second).build());
        }
    }

    public static final class Builder {
        private final BuilderCompat mBuilderCompat;

        public Builder(ClipData clip, int source) {
            if (Build.VERSION.SDK_INT >= 31) {
                this.mBuilderCompat = new BuilderCompat31Impl(clip, source);
            } else {
                this.mBuilderCompat = new BuilderCompatImpl(clip, source);
            }
        }

        public Builder(ContentInfoCompat other) {
            if (Build.VERSION.SDK_INT >= 31) {
                this.mBuilderCompat = new BuilderCompat31Impl(other);
            } else {
                this.mBuilderCompat = new BuilderCompatImpl(other);
            }
        }

        public ContentInfoCompat build() {
            return this.mBuilderCompat.build();
        }

        public Builder setClip(ClipData clip) {
            this.mBuilderCompat.setClip(clip);
            return this;
        }

        public Builder setExtras(Bundle extras) {
            this.mBuilderCompat.setExtras(extras);
            return this;
        }

        public Builder setFlags(int flags) {
            this.mBuilderCompat.setFlags(flags);
            return this;
        }

        public Builder setLinkUri(Uri linkUri) {
            this.mBuilderCompat.setLinkUri(linkUri);
            return this;
        }

        public Builder setSource(int source) {
            this.mBuilderCompat.setSource(source);
            return this;
        }
    }

    private interface BuilderCompat {
        ContentInfoCompat build();

        void setClip(ClipData clipData);

        void setExtras(Bundle bundle);

        void setFlags(int i);

        void setLinkUri(Uri uri);

        void setSource(int i);
    }

    private static final class BuilderCompat31Impl implements BuilderCompat {
        private final ContentInfo.Builder mPlatformBuilder;

        BuilderCompat31Impl(ClipData clip, int source) {
            this.mPlatformBuilder = new ContentInfo.Builder(clip, source);
        }

        BuilderCompat31Impl(ContentInfoCompat other) {
            this.mPlatformBuilder = new ContentInfo.Builder(other.toContentInfo());
        }

        public ContentInfoCompat build() {
            return new ContentInfoCompat(new Compat31Impl(this.mPlatformBuilder.build()));
        }

        public void setClip(ClipData clip) {
            this.mPlatformBuilder.setClip(clip);
        }

        public void setExtras(Bundle extras) {
            this.mPlatformBuilder.setExtras(extras);
        }

        public void setFlags(int flags) {
            this.mPlatformBuilder.setFlags(flags);
        }

        public void setLinkUri(Uri linkUri) {
            this.mPlatformBuilder.setLinkUri(linkUri);
        }

        public void setSource(int source) {
            this.mPlatformBuilder.setSource(source);
        }
    }

    private static final class BuilderCompatImpl implements BuilderCompat {
        ClipData mClip;
        Bundle mExtras;
        int mFlags;
        Uri mLinkUri;
        int mSource;

        BuilderCompatImpl(ClipData clip, int source) {
            this.mClip = clip;
            this.mSource = source;
        }

        BuilderCompatImpl(ContentInfoCompat other) {
            this.mClip = other.getClip();
            this.mSource = other.getSource();
            this.mFlags = other.getFlags();
            this.mLinkUri = other.getLinkUri();
            this.mExtras = other.getExtras();
        }

        public ContentInfoCompat build() {
            return new ContentInfoCompat(new CompatImpl(this));
        }

        public void setClip(ClipData clip) {
            this.mClip = clip;
        }

        public void setExtras(Bundle extras) {
            this.mExtras = extras;
        }

        public void setFlags(int flags) {
            this.mFlags = flags;
        }

        public void setLinkUri(Uri linkUri) {
            this.mLinkUri = linkUri;
        }

        public void setSource(int source) {
            this.mSource = source;
        }
    }

    private interface Compat {
        ClipData getClip();

        Bundle getExtras();

        int getFlags();

        Uri getLinkUri();

        int getSource();

        ContentInfo getWrapped();
    }

    private static final class Compat31Impl implements Compat {
        private final ContentInfo mWrapped;

        Compat31Impl(ContentInfo wrapped) {
            this.mWrapped = (ContentInfo) Preconditions.checkNotNull(wrapped);
        }

        public ClipData getClip() {
            return this.mWrapped.getClip();
        }

        public Bundle getExtras() {
            return this.mWrapped.getExtras();
        }

        public int getFlags() {
            return this.mWrapped.getFlags();
        }

        public Uri getLinkUri() {
            return this.mWrapped.getLinkUri();
        }

        public int getSource() {
            return this.mWrapped.getSource();
        }

        public ContentInfo getWrapped() {
            return this.mWrapped;
        }

        public String toString() {
            return "ContentInfoCompat{" + this.mWrapped + "}";
        }
    }

    /* compiled from: 005E */
    private static final class CompatImpl implements Compat {
        private final ClipData mClip;
        private final Bundle mExtras;
        private final int mFlags;
        private final Uri mLinkUri;
        private final int mSource;

        CompatImpl(BuilderCompatImpl b) {
            this.mClip = (ClipData) Preconditions.checkNotNull(b.mClip);
            this.mSource = Preconditions.checkArgumentInRange(b.mSource, 0, 5, "source");
            this.mFlags = Preconditions.checkFlagsArgument(b.mFlags, 1);
            this.mLinkUri = b.mLinkUri;
            this.mExtras = b.mExtras;
        }

        public ClipData getClip() {
            return this.mClip;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public int getFlags() {
            return this.mFlags;
        }

        public Uri getLinkUri() {
            return this.mLinkUri;
        }

        public int getSource() {
            return this.mSource;
        }

        public ContentInfo getWrapped() {
            return null;
        }

        public String toString() {
            StringBuilder append = new StringBuilder().append("ContentInfoCompat{clip=").append(this.mClip.getDescription()).append(", source=");
            String sourceToString = ContentInfoCompat.sourceToString(this.mSource);
            Log1F380D.a((Object) sourceToString);
            StringBuilder append2 = append.append(sourceToString).append(", flags=");
            String flagsToString = ContentInfoCompat.flagsToString(this.mFlags);
            Log1F380D.a((Object) flagsToString);
            StringBuilder append3 = append2.append(flagsToString);
            Uri uri = this.mLinkUri;
            String str = HttpUrl.FRAGMENT_ENCODE_SET;
            StringBuilder append4 = append3.append(uri == null ? str : ", hasLinkUri(" + this.mLinkUri.toString().length() + ")");
            if (this.mExtras != null) {
                str = ", hasExtras";
            }
            return append4.append(str).append("}").toString();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Source {
    }

    ContentInfoCompat(Compat compat) {
        this.mCompat = compat;
    }

    static ClipData buildClipData(ClipDescription description, List<ClipData.Item> list) {
        ClipData clipData = new ClipData(new ClipDescription(description), list.get(0));
        for (int i = 1; i < list.size(); i++) {
            clipData.addItem(list.get(i));
        }
        return clipData;
    }

    static String flagsToString(int flags) {
        if ((flags & 1) != 0) {
            return "FLAG_CONVERT_TO_PLAIN_TEXT";
        }
        String valueOf = String.valueOf(flags);
        Log1F380D.a((Object) valueOf);
        return valueOf;
    }

    static Pair<ClipData, ClipData> partition(ClipData clip, androidx.core.util.Predicate<ClipData.Item> predicate) {
        ArrayList arrayList = null;
        ArrayList arrayList2 = null;
        for (int i = 0; i < clip.getItemCount(); i++) {
            ClipData.Item itemAt = clip.getItemAt(i);
            if (predicate.test(itemAt)) {
                arrayList = arrayList == null ? new ArrayList() : arrayList;
                arrayList.add(itemAt);
            } else {
                arrayList2 = arrayList2 == null ? new ArrayList() : arrayList2;
                arrayList2.add(itemAt);
            }
        }
        return arrayList == null ? Pair.create((Object) null, clip) : arrayList2 == null ? Pair.create(clip, (Object) null) : Pair.create(buildClipData(clip.getDescription(), arrayList), buildClipData(clip.getDescription(), arrayList2));
    }

    public static Pair<ContentInfo, ContentInfo> partition(ContentInfo payload, Predicate<ClipData.Item> predicate) {
        return Api31Impl.partition(payload, predicate);
    }

    public static ContentInfoCompat toContentInfoCompat(ContentInfo platContentInfo) {
        return new ContentInfoCompat(new Compat31Impl(platContentInfo));
    }

    public ClipData getClip() {
        return this.mCompat.getClip();
    }

    public Bundle getExtras() {
        return this.mCompat.getExtras();
    }

    public int getFlags() {
        return this.mCompat.getFlags();
    }

    public Uri getLinkUri() {
        return this.mCompat.getLinkUri();
    }

    public int getSource() {
        return this.mCompat.getSource();
    }

    public Pair<ContentInfoCompat, ContentInfoCompat> partition(androidx.core.util.Predicate<ClipData.Item> predicate) {
        ClipData clip = this.mCompat.getClip();
        ContentInfoCompat contentInfoCompat = null;
        if (clip.getItemCount() == 1) {
            boolean test = predicate.test(clip.getItemAt(0));
            ContentInfoCompat contentInfoCompat2 = test ? this : null;
            if (!test) {
                contentInfoCompat = this;
            }
            return Pair.create(contentInfoCompat2, contentInfoCompat);
        }
        Pair<ClipData, ClipData> partition = partition(clip, predicate);
        return partition.first == null ? Pair.create((Object) null, this) : partition.second == null ? Pair.create(this, (Object) null) : Pair.create(new Builder(this).setClip((ClipData) partition.first).build(), new Builder(this).setClip((ClipData) partition.second).build());
    }

    public ContentInfo toContentInfo() {
        return (ContentInfo) Objects.requireNonNull(this.mCompat.getWrapped());
    }

    public String toString() {
        return this.mCompat.toString();
    }

    static String sourceToString(int source) {
        switch (source) {
            case 0:
                return "SOURCE_APP";
            case 1:
                return "SOURCE_CLIPBOARD";
            case 2:
                return "SOURCE_INPUT_METHOD";
            case 3:
                return "SOURCE_DRAG_AND_DROP";
            case 4:
                return "SOURCE_AUTOFILL";
            case 5:
                return "SOURCE_PROCESS_TEXT";
            default:
                String valueOf = String.valueOf(source);
                Log1F380D.a((Object) valueOf);
                return valueOf;
        }
    }
}
