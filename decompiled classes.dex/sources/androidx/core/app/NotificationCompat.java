package androidx.core.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.LocusId;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.widget.RemoteViews;
import androidx.core.R;
import androidx.core.app.Person;
import androidx.core.content.LocusIdCompat;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.text.BidiFormatter;
import androidx.core.view.ViewCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import mt.Log1F380D;
import okhttp3.HttpUrl;

/* compiled from: 002F */
public class NotificationCompat {
    public static final int BADGE_ICON_LARGE = 2;
    public static final int BADGE_ICON_NONE = 0;
    public static final int BADGE_ICON_SMALL = 1;
    public static final String CATEGORY_ALARM = "alarm";
    public static final String CATEGORY_CALL = "call";
    public static final String CATEGORY_EMAIL = "email";
    public static final String CATEGORY_ERROR = "err";
    public static final String CATEGORY_EVENT = "event";
    public static final String CATEGORY_LOCATION_SHARING = "location_sharing";
    public static final String CATEGORY_MESSAGE = "msg";
    public static final String CATEGORY_MISSED_CALL = "missed_call";
    public static final String CATEGORY_NAVIGATION = "navigation";
    public static final String CATEGORY_PROGRESS = "progress";
    public static final String CATEGORY_PROMO = "promo";
    public static final String CATEGORY_RECOMMENDATION = "recommendation";
    public static final String CATEGORY_REMINDER = "reminder";
    public static final String CATEGORY_SERVICE = "service";
    public static final String CATEGORY_SOCIAL = "social";
    public static final String CATEGORY_STATUS = "status";
    public static final String CATEGORY_STOPWATCH = "stopwatch";
    public static final String CATEGORY_SYSTEM = "sys";
    public static final String CATEGORY_TRANSPORT = "transport";
    public static final String CATEGORY_WORKOUT = "workout";
    public static final int COLOR_DEFAULT = 0;
    public static final int DEFAULT_ALL = -1;
    public static final int DEFAULT_LIGHTS = 4;
    public static final int DEFAULT_SOUND = 1;
    public static final int DEFAULT_VIBRATE = 2;
    public static final String EXTRA_AUDIO_CONTENTS_URI = "android.audioContents";
    public static final String EXTRA_BACKGROUND_IMAGE_URI = "android.backgroundImageUri";
    public static final String EXTRA_BIG_TEXT = "android.bigText";
    public static final String EXTRA_CHANNEL_GROUP_ID = "android.intent.extra.CHANNEL_GROUP_ID";
    public static final String EXTRA_CHANNEL_ID = "android.intent.extra.CHANNEL_ID";
    public static final String EXTRA_CHRONOMETER_COUNT_DOWN = "android.chronometerCountDown";
    public static final String EXTRA_COLORIZED = "android.colorized";
    public static final String EXTRA_COMPACT_ACTIONS = "android.compactActions";
    public static final String EXTRA_COMPAT_TEMPLATE = "androidx.core.app.extra.COMPAT_TEMPLATE";
    public static final String EXTRA_CONVERSATION_TITLE = "android.conversationTitle";
    public static final String EXTRA_HIDDEN_CONVERSATION_TITLE = "android.hiddenConversationTitle";
    public static final String EXTRA_HISTORIC_MESSAGES = "android.messages.historic";
    public static final String EXTRA_INFO_TEXT = "android.infoText";
    public static final String EXTRA_IS_GROUP_CONVERSATION = "android.isGroupConversation";
    public static final String EXTRA_LARGE_ICON = "android.largeIcon";
    public static final String EXTRA_LARGE_ICON_BIG = "android.largeIcon.big";
    public static final String EXTRA_MEDIA_SESSION = "android.mediaSession";
    public static final String EXTRA_MESSAGES = "android.messages";
    public static final String EXTRA_MESSAGING_STYLE_USER = "android.messagingStyleUser";
    public static final String EXTRA_NOTIFICATION_ID = "android.intent.extra.NOTIFICATION_ID";
    public static final String EXTRA_NOTIFICATION_TAG = "android.intent.extra.NOTIFICATION_TAG";
    @Deprecated
    public static final String EXTRA_PEOPLE = "android.people";
    public static final String EXTRA_PEOPLE_LIST = "android.people.list";
    public static final String EXTRA_PICTURE = "android.picture";
    public static final String EXTRA_PICTURE_CONTENT_DESCRIPTION = "android.pictureContentDescription";
    public static final String EXTRA_PROGRESS = "android.progress";
    public static final String EXTRA_PROGRESS_INDETERMINATE = "android.progressIndeterminate";
    public static final String EXTRA_PROGRESS_MAX = "android.progressMax";
    public static final String EXTRA_REMOTE_INPUT_HISTORY = "android.remoteInputHistory";
    public static final String EXTRA_SELF_DISPLAY_NAME = "android.selfDisplayName";
    public static final String EXTRA_SHOW_BIG_PICTURE_WHEN_COLLAPSED = "android.showBigPictureWhenCollapsed";
    public static final String EXTRA_SHOW_CHRONOMETER = "android.showChronometer";
    public static final String EXTRA_SHOW_WHEN = "android.showWhen";
    public static final String EXTRA_SMALL_ICON = "android.icon";
    public static final String EXTRA_SUB_TEXT = "android.subText";
    public static final String EXTRA_SUMMARY_TEXT = "android.summaryText";
    public static final String EXTRA_TEMPLATE = "android.template";
    public static final String EXTRA_TEXT = "android.text";
    public static final String EXTRA_TEXT_LINES = "android.textLines";
    public static final String EXTRA_TITLE = "android.title";
    public static final String EXTRA_TITLE_BIG = "android.title.big";
    public static final int FLAG_AUTO_CANCEL = 16;
    public static final int FLAG_BUBBLE = 4096;
    public static final int FLAG_FOREGROUND_SERVICE = 64;
    public static final int FLAG_GROUP_SUMMARY = 512;
    @Deprecated
    public static final int FLAG_HIGH_PRIORITY = 128;
    public static final int FLAG_INSISTENT = 4;
    public static final int FLAG_LOCAL_ONLY = 256;
    public static final int FLAG_NO_CLEAR = 32;
    public static final int FLAG_ONGOING_EVENT = 2;
    public static final int FLAG_ONLY_ALERT_ONCE = 8;
    public static final int FLAG_SHOW_LIGHTS = 1;
    public static final int FOREGROUND_SERVICE_DEFAULT = 0;
    public static final int FOREGROUND_SERVICE_DEFERRED = 2;
    public static final int FOREGROUND_SERVICE_IMMEDIATE = 1;
    public static final int GROUP_ALERT_ALL = 0;
    public static final int GROUP_ALERT_CHILDREN = 2;
    public static final int GROUP_ALERT_SUMMARY = 1;
    public static final String GROUP_KEY_SILENT = "silent";
    public static final String INTENT_CATEGORY_NOTIFICATION_PREFERENCES = "android.intent.category.NOTIFICATION_PREFERENCES";
    public static final int PRIORITY_DEFAULT = 0;
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_LOW = -1;
    public static final int PRIORITY_MAX = 2;
    public static final int PRIORITY_MIN = -2;
    public static final int STREAM_DEFAULT = -1;
    public static final int VISIBILITY_PRIVATE = 0;
    public static final int VISIBILITY_PUBLIC = 1;
    public static final int VISIBILITY_SECRET = -1;

    public static class Action {
        static final String EXTRA_SEMANTIC_ACTION = "android.support.action.semanticAction";
        static final String EXTRA_SHOWS_USER_INTERFACE = "android.support.action.showsUserInterface";
        public static final int SEMANTIC_ACTION_ARCHIVE = 5;
        public static final int SEMANTIC_ACTION_CALL = 10;
        public static final int SEMANTIC_ACTION_DELETE = 4;
        public static final int SEMANTIC_ACTION_MARK_AS_READ = 2;
        public static final int SEMANTIC_ACTION_MARK_AS_UNREAD = 3;
        public static final int SEMANTIC_ACTION_MUTE = 6;
        public static final int SEMANTIC_ACTION_NONE = 0;
        public static final int SEMANTIC_ACTION_REPLY = 1;
        public static final int SEMANTIC_ACTION_THUMBS_DOWN = 9;
        public static final int SEMANTIC_ACTION_THUMBS_UP = 8;
        public static final int SEMANTIC_ACTION_UNMUTE = 7;
        public PendingIntent actionIntent;
        @Deprecated
        public int icon;
        private boolean mAllowGeneratedReplies;
        private boolean mAuthenticationRequired;
        private final RemoteInput[] mDataOnlyRemoteInputs;
        final Bundle mExtras;
        private IconCompat mIcon;
        private final boolean mIsContextual;
        private final RemoteInput[] mRemoteInputs;
        private final int mSemanticAction;
        boolean mShowsUserInterface;
        public CharSequence title;

        public static final class Builder {
            private boolean mAllowGeneratedReplies;
            private boolean mAuthenticationRequired;
            private final Bundle mExtras;
            private final IconCompat mIcon;
            private final PendingIntent mIntent;
            private boolean mIsContextual;
            private ArrayList<RemoteInput> mRemoteInputs;
            private int mSemanticAction;
            private boolean mShowsUserInterface;
            private final CharSequence mTitle;

            /* JADX INFO: this call moved to the top of the method (can break code semantics) */
            public Builder(int icon, CharSequence title, PendingIntent intent) {
                this(icon != 0 ? IconCompat.createWithResource((Resources) null, HttpUrl.FRAGMENT_ENCODE_SET, icon) : null, title, intent, new Bundle(), (RemoteInput[]) null, true, 0, true, false, false);
            }

            public Builder(Action action) {
                this(action.getIconCompat(), action.title, action.actionIntent, new Bundle(action.mExtras), action.getRemoteInputs(), action.getAllowGeneratedReplies(), action.getSemanticAction(), action.mShowsUserInterface, action.isContextual(), action.isAuthenticationRequired());
            }

            public Builder(IconCompat icon, CharSequence title, PendingIntent intent) {
                this(icon, title, intent, new Bundle(), (RemoteInput[]) null, true, 0, true, false, false);
            }

            private Builder(IconCompat icon, CharSequence title, PendingIntent intent, Bundle extras, RemoteInput[] remoteInputs, boolean allowGeneratedReplies, int semanticAction, boolean showsUserInterface, boolean isContextual, boolean authRequired) {
                this.mAllowGeneratedReplies = true;
                this.mShowsUserInterface = true;
                this.mIcon = icon;
                this.mTitle = Builder.limitCharSequenceLength(title);
                this.mIntent = intent;
                this.mExtras = extras;
                this.mRemoteInputs = remoteInputs == null ? null : new ArrayList<>(Arrays.asList(remoteInputs));
                this.mAllowGeneratedReplies = allowGeneratedReplies;
                this.mSemanticAction = semanticAction;
                this.mShowsUserInterface = showsUserInterface;
                this.mIsContextual = isContextual;
                this.mAuthenticationRequired = authRequired;
            }

            private void checkContextualActionNullFields() {
                if (this.mIsContextual && this.mIntent == null) {
                    throw new NullPointerException("Contextual Actions must contain a valid PendingIntent");
                }
            }

            public static Builder fromAndroidAction(Notification.Action action) {
                RemoteInput[] remoteInputs;
                Builder builder = (Build.VERSION.SDK_INT < 23 || action.getIcon() == null) ? new Builder(action.icon, action.title, action.actionIntent) : new Builder(IconCompat.createFromIcon(action.getIcon()), action.title, action.actionIntent);
                if (!(Build.VERSION.SDK_INT < 20 || (remoteInputs = action.getRemoteInputs()) == null || remoteInputs.length == 0)) {
                    for (RemoteInput fromPlatform : remoteInputs) {
                        builder.addRemoteInput(RemoteInput.fromPlatform(fromPlatform));
                    }
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    builder.mAllowGeneratedReplies = action.getAllowGeneratedReplies();
                }
                if (Build.VERSION.SDK_INT >= 28) {
                    builder.setSemanticAction(action.getSemanticAction());
                }
                if (Build.VERSION.SDK_INT >= 29) {
                    builder.setContextual(action.isContextual());
                }
                if (Build.VERSION.SDK_INT >= 31) {
                    builder.setAuthenticationRequired(action.isAuthenticationRequired());
                }
                return builder;
            }

            public Builder addExtras(Bundle extras) {
                if (extras != null) {
                    this.mExtras.putAll(extras);
                }
                return this;
            }

            public Builder addRemoteInput(RemoteInput remoteInput) {
                if (this.mRemoteInputs == null) {
                    this.mRemoteInputs = new ArrayList<>();
                }
                if (remoteInput != null) {
                    this.mRemoteInputs.add(remoteInput);
                }
                return this;
            }

            /* JADX WARNING: type inference failed for: r3v6, types: [java.lang.Object[]] */
            /* JADX WARNING: Multi-variable type inference failed */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public androidx.core.app.NotificationCompat.Action build() {
                /*
                    r17 = this;
                    r0 = r17
                    r17.checkContextualActionNullFields()
                    java.util.ArrayList r1 = new java.util.ArrayList
                    r1.<init>()
                    java.util.ArrayList r2 = new java.util.ArrayList
                    r2.<init>()
                    java.util.ArrayList<androidx.core.app.RemoteInput> r3 = r0.mRemoteInputs
                    if (r3 == 0) goto L_0x0031
                    java.util.Iterator r3 = r3.iterator()
                L_0x0017:
                    boolean r4 = r3.hasNext()
                    if (r4 == 0) goto L_0x0031
                    java.lang.Object r4 = r3.next()
                    androidx.core.app.RemoteInput r4 = (androidx.core.app.RemoteInput) r4
                    boolean r5 = r4.isDataOnly()
                    if (r5 == 0) goto L_0x002d
                    r1.add(r4)
                    goto L_0x0030
                L_0x002d:
                    r2.add(r4)
                L_0x0030:
                    goto L_0x0017
                L_0x0031:
                    boolean r3 = r1.isEmpty()
                    r4 = 0
                    if (r3 == 0) goto L_0x003a
                    r11 = r4
                    goto L_0x0047
                L_0x003a:
                    int r3 = r1.size()
                    androidx.core.app.RemoteInput[] r3 = new androidx.core.app.RemoteInput[r3]
                    java.lang.Object[] r3 = r1.toArray(r3)
                    androidx.core.app.RemoteInput[] r3 = (androidx.core.app.RemoteInput[]) r3
                    r11 = r3
                L_0x0047:
                    boolean r3 = r2.isEmpty()
                    if (r3 == 0) goto L_0x004e
                    goto L_0x005b
                L_0x004e:
                    int r3 = r2.size()
                    androidx.core.app.RemoteInput[] r3 = new androidx.core.app.RemoteInput[r3]
                    java.lang.Object[] r3 = r2.toArray(r3)
                    r4 = r3
                    androidx.core.app.RemoteInput[] r4 = (androidx.core.app.RemoteInput[]) r4
                L_0x005b:
                    r10 = r4
                    androidx.core.app.NotificationCompat$Action r3 = new androidx.core.app.NotificationCompat$Action
                    androidx.core.graphics.drawable.IconCompat r6 = r0.mIcon
                    java.lang.CharSequence r7 = r0.mTitle
                    android.app.PendingIntent r8 = r0.mIntent
                    android.os.Bundle r9 = r0.mExtras
                    boolean r12 = r0.mAllowGeneratedReplies
                    int r13 = r0.mSemanticAction
                    boolean r14 = r0.mShowsUserInterface
                    boolean r15 = r0.mIsContextual
                    boolean r4 = r0.mAuthenticationRequired
                    r5 = r3
                    r16 = r4
                    r5.<init>((androidx.core.graphics.drawable.IconCompat) r6, (java.lang.CharSequence) r7, (android.app.PendingIntent) r8, (android.os.Bundle) r9, (androidx.core.app.RemoteInput[]) r10, (androidx.core.app.RemoteInput[]) r11, (boolean) r12, (int) r13, (boolean) r14, (boolean) r15, (boolean) r16)
                    return r3
                */
                throw new UnsupportedOperationException("Method not decompiled: androidx.core.app.NotificationCompat.Action.Builder.build():androidx.core.app.NotificationCompat$Action");
            }

            public Builder extend(Extender extender) {
                extender.extend(this);
                return this;
            }

            public Bundle getExtras() {
                return this.mExtras;
            }

            public Builder setAllowGeneratedReplies(boolean allowGeneratedReplies) {
                this.mAllowGeneratedReplies = allowGeneratedReplies;
                return this;
            }

            public Builder setAuthenticationRequired(boolean authenticationRequired) {
                this.mAuthenticationRequired = authenticationRequired;
                return this;
            }

            public Builder setContextual(boolean isContextual) {
                this.mIsContextual = isContextual;
                return this;
            }

            public Builder setSemanticAction(int semanticAction) {
                this.mSemanticAction = semanticAction;
                return this;
            }

            public Builder setShowsUserInterface(boolean showsUserInterface) {
                this.mShowsUserInterface = showsUserInterface;
                return this;
            }
        }

        public interface Extender {
            Builder extend(Builder builder);
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface SemanticAction {
        }

        public static final class WearableExtender implements Extender {
            private static final int DEFAULT_FLAGS = 1;
            private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
            private static final int FLAG_AVAILABLE_OFFLINE = 1;
            private static final int FLAG_HINT_DISPLAY_INLINE = 4;
            private static final int FLAG_HINT_LAUNCHES_ACTIVITY = 2;
            private static final String KEY_CANCEL_LABEL = "cancelLabel";
            private static final String KEY_CONFIRM_LABEL = "confirmLabel";
            private static final String KEY_FLAGS = "flags";
            private static final String KEY_IN_PROGRESS_LABEL = "inProgressLabel";
            private CharSequence mCancelLabel;
            private CharSequence mConfirmLabel;
            private int mFlags = 1;
            private CharSequence mInProgressLabel;

            public WearableExtender() {
            }

            public WearableExtender(Action action) {
                Bundle bundle = action.getExtras().getBundle(EXTRA_WEARABLE_EXTENSIONS);
                if (bundle != null) {
                    this.mFlags = bundle.getInt(KEY_FLAGS, 1);
                    this.mInProgressLabel = bundle.getCharSequence(KEY_IN_PROGRESS_LABEL);
                    this.mConfirmLabel = bundle.getCharSequence(KEY_CONFIRM_LABEL);
                    this.mCancelLabel = bundle.getCharSequence(KEY_CANCEL_LABEL);
                }
            }

            private void setFlag(int mask, boolean value) {
                if (value) {
                    this.mFlags |= mask;
                } else {
                    this.mFlags &= ~mask;
                }
            }

            public WearableExtender clone() {
                WearableExtender wearableExtender = new WearableExtender();
                wearableExtender.mFlags = this.mFlags;
                wearableExtender.mInProgressLabel = this.mInProgressLabel;
                wearableExtender.mConfirmLabel = this.mConfirmLabel;
                wearableExtender.mCancelLabel = this.mCancelLabel;
                return wearableExtender;
            }

            public Builder extend(Builder builder) {
                Bundle bundle = new Bundle();
                int i = this.mFlags;
                if (i != 1) {
                    bundle.putInt(KEY_FLAGS, i);
                }
                CharSequence charSequence = this.mInProgressLabel;
                if (charSequence != null) {
                    bundle.putCharSequence(KEY_IN_PROGRESS_LABEL, charSequence);
                }
                CharSequence charSequence2 = this.mConfirmLabel;
                if (charSequence2 != null) {
                    bundle.putCharSequence(KEY_CONFIRM_LABEL, charSequence2);
                }
                CharSequence charSequence3 = this.mCancelLabel;
                if (charSequence3 != null) {
                    bundle.putCharSequence(KEY_CANCEL_LABEL, charSequence3);
                }
                builder.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, bundle);
                return builder;
            }

            @Deprecated
            public CharSequence getCancelLabel() {
                return this.mCancelLabel;
            }

            @Deprecated
            public CharSequence getConfirmLabel() {
                return this.mConfirmLabel;
            }

            public boolean getHintDisplayActionInline() {
                return (this.mFlags & 4) != 0;
            }

            public boolean getHintLaunchesActivity() {
                return (this.mFlags & 2) != 0;
            }

            @Deprecated
            public CharSequence getInProgressLabel() {
                return this.mInProgressLabel;
            }

            public boolean isAvailableOffline() {
                return (this.mFlags & 1) != 0;
            }

            public WearableExtender setAvailableOffline(boolean availableOffline) {
                setFlag(1, availableOffline);
                return this;
            }

            @Deprecated
            public WearableExtender setCancelLabel(CharSequence label) {
                this.mCancelLabel = label;
                return this;
            }

            @Deprecated
            public WearableExtender setConfirmLabel(CharSequence label) {
                this.mConfirmLabel = label;
                return this;
            }

            public WearableExtender setHintDisplayActionInline(boolean hintDisplayInline) {
                setFlag(4, hintDisplayInline);
                return this;
            }

            public WearableExtender setHintLaunchesActivity(boolean hintLaunchesActivity) {
                setFlag(2, hintLaunchesActivity);
                return this;
            }

            @Deprecated
            public WearableExtender setInProgressLabel(CharSequence label) {
                this.mInProgressLabel = label;
                return this;
            }
        }

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public Action(int icon2, CharSequence title2, PendingIntent intent) {
            this(icon2 != 0 ? IconCompat.createWithResource((Resources) null, HttpUrl.FRAGMENT_ENCODE_SET, icon2) : null, title2, intent);
        }

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        Action(int icon2, CharSequence title2, PendingIntent intent, Bundle extras, RemoteInput[] remoteInputs, RemoteInput[] dataOnlyRemoteInputs, boolean allowGeneratedReplies, int semanticAction, boolean showsUserInterface, boolean isContextual, boolean requireAuth) {
            this(icon2 != 0 ? IconCompat.createWithResource((Resources) null, HttpUrl.FRAGMENT_ENCODE_SET, icon2) : null, title2, intent, extras, remoteInputs, dataOnlyRemoteInputs, allowGeneratedReplies, semanticAction, showsUserInterface, isContextual, requireAuth);
        }

        public Action(IconCompat icon2, CharSequence title2, PendingIntent intent) {
            this(icon2, title2, intent, new Bundle(), (RemoteInput[]) null, (RemoteInput[]) null, true, 0, true, false, false);
        }

        Action(IconCompat icon2, CharSequence title2, PendingIntent intent, Bundle extras, RemoteInput[] remoteInputs, RemoteInput[] dataOnlyRemoteInputs, boolean allowGeneratedReplies, int semanticAction, boolean showsUserInterface, boolean isContextual, boolean requireAuth) {
            this.mShowsUserInterface = true;
            this.mIcon = icon2;
            if (icon2 != null && icon2.getType() == 2) {
                this.icon = icon2.getResId();
            }
            this.title = Builder.limitCharSequenceLength(title2);
            this.actionIntent = intent;
            this.mExtras = extras != null ? extras : new Bundle();
            this.mRemoteInputs = remoteInputs;
            this.mDataOnlyRemoteInputs = dataOnlyRemoteInputs;
            this.mAllowGeneratedReplies = allowGeneratedReplies;
            this.mSemanticAction = semanticAction;
            this.mShowsUserInterface = showsUserInterface;
            this.mIsContextual = isContextual;
            this.mAuthenticationRequired = requireAuth;
        }

        public PendingIntent getActionIntent() {
            return this.actionIntent;
        }

        public boolean getAllowGeneratedReplies() {
            return this.mAllowGeneratedReplies;
        }

        public RemoteInput[] getDataOnlyRemoteInputs() {
            return this.mDataOnlyRemoteInputs;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        @Deprecated
        public int getIcon() {
            return this.icon;
        }

        public IconCompat getIconCompat() {
            int i;
            if (this.mIcon == null && (i = this.icon) != 0) {
                this.mIcon = IconCompat.createWithResource((Resources) null, HttpUrl.FRAGMENT_ENCODE_SET, i);
            }
            return this.mIcon;
        }

        public RemoteInput[] getRemoteInputs() {
            return this.mRemoteInputs;
        }

        public int getSemanticAction() {
            return this.mSemanticAction;
        }

        public boolean getShowsUserInterface() {
            return this.mShowsUserInterface;
        }

        public CharSequence getTitle() {
            return this.title;
        }

        public boolean isAuthenticationRequired() {
            return this.mAuthenticationRequired;
        }

        public boolean isContextual() {
            return this.mIsContextual;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface BadgeIconType {
    }

    public static class BigPictureStyle extends Style {
        private static final String TEMPLATE_CLASS_NAME = "androidx.core.app.NotificationCompat$BigPictureStyle";
        private IconCompat mBigLargeIcon;
        private boolean mBigLargeIconSet;
        private Bitmap mPicture;
        private CharSequence mPictureContentDescription;
        private boolean mShowBigPictureWhenCollapsed;

        private static class Api16Impl {
            private Api16Impl() {
            }

            static void setBigLargeIcon(Notification.BigPictureStyle style, Bitmap icon) {
                style.bigLargeIcon(icon);
            }

            static void setSummaryText(Notification.BigPictureStyle style, CharSequence text) {
                style.setSummaryText(text);
            }
        }

        private static class Api23Impl {
            private Api23Impl() {
            }

            static void setBigLargeIcon(Notification.BigPictureStyle style, Icon icon) {
                style.bigLargeIcon(icon);
            }
        }

        private static class Api31Impl {
            private Api31Impl() {
            }

            static void setContentDescription(Notification.BigPictureStyle style, CharSequence contentDescription) {
                style.setContentDescription(contentDescription);
            }

            static void showBigPictureWhenCollapsed(Notification.BigPictureStyle style, boolean show) {
                style.showBigPictureWhenCollapsed(show);
            }
        }

        public BigPictureStyle() {
        }

        public BigPictureStyle(Builder builder) {
            setBuilder(builder);
        }

        private static IconCompat asIconCompat(Parcelable bitmapOrIcon) {
            if (bitmapOrIcon == null) {
                return null;
            }
            if (Build.VERSION.SDK_INT >= 23 && (bitmapOrIcon instanceof Icon)) {
                return IconCompat.createFromIcon((Icon) bitmapOrIcon);
            }
            if (bitmapOrIcon instanceof Bitmap) {
                return IconCompat.createWithBitmap((Bitmap) bitmapOrIcon);
            }
            return null;
        }

        public void apply(NotificationBuilderWithBuilderAccessor builder) {
            if (Build.VERSION.SDK_INT >= 16) {
                Notification.BigPictureStyle bigPicture = new Notification.BigPictureStyle(builder.getBuilder()).setBigContentTitle(this.mBigContentTitle).bigPicture(this.mPicture);
                if (this.mBigLargeIconSet) {
                    if (this.mBigLargeIcon == null) {
                        Api16Impl.setBigLargeIcon(bigPicture, (Bitmap) null);
                    } else if (Build.VERSION.SDK_INT >= 23) {
                        Context context = null;
                        if (builder instanceof NotificationCompatBuilder) {
                            context = ((NotificationCompatBuilder) builder).getContext();
                        }
                        Api23Impl.setBigLargeIcon(bigPicture, this.mBigLargeIcon.toIcon(context));
                    } else if (this.mBigLargeIcon.getType() == 1) {
                        Api16Impl.setBigLargeIcon(bigPicture, this.mBigLargeIcon.getBitmap());
                    } else {
                        Api16Impl.setBigLargeIcon(bigPicture, (Bitmap) null);
                    }
                }
                if (this.mSummaryTextSet) {
                    Api16Impl.setSummaryText(bigPicture, this.mSummaryText);
                }
                if (Build.VERSION.SDK_INT >= 31) {
                    Api31Impl.showBigPictureWhenCollapsed(bigPicture, this.mShowBigPictureWhenCollapsed);
                    Api31Impl.setContentDescription(bigPicture, this.mPictureContentDescription);
                }
            }
        }

        public BigPictureStyle bigLargeIcon(Bitmap b) {
            this.mBigLargeIcon = b == null ? null : IconCompat.createWithBitmap(b);
            this.mBigLargeIconSet = true;
            return this;
        }

        public BigPictureStyle bigPicture(Bitmap b) {
            this.mPicture = b;
            return this;
        }

        /* access modifiers changed from: protected */
        public void clearCompatExtraKeys(Bundle extras) {
            super.clearCompatExtraKeys(extras);
            extras.remove(NotificationCompat.EXTRA_LARGE_ICON_BIG);
            extras.remove(NotificationCompat.EXTRA_PICTURE);
            extras.remove(NotificationCompat.EXTRA_SHOW_BIG_PICTURE_WHEN_COLLAPSED);
        }

        /* access modifiers changed from: protected */
        public String getClassName() {
            return TEMPLATE_CLASS_NAME;
        }

        /* access modifiers changed from: protected */
        public void restoreFromCompatExtras(Bundle extras) {
            super.restoreFromCompatExtras(extras);
            if (extras.containsKey(NotificationCompat.EXTRA_LARGE_ICON_BIG)) {
                this.mBigLargeIcon = asIconCompat(extras.getParcelable(NotificationCompat.EXTRA_LARGE_ICON_BIG));
                this.mBigLargeIconSet = true;
            }
            this.mPicture = (Bitmap) extras.getParcelable(NotificationCompat.EXTRA_PICTURE);
            this.mShowBigPictureWhenCollapsed = extras.getBoolean(NotificationCompat.EXTRA_SHOW_BIG_PICTURE_WHEN_COLLAPSED);
        }

        public BigPictureStyle setBigContentTitle(CharSequence title) {
            this.mBigContentTitle = Builder.limitCharSequenceLength(title);
            return this;
        }

        public BigPictureStyle setContentDescription(CharSequence contentDescription) {
            this.mPictureContentDescription = contentDescription;
            return this;
        }

        public BigPictureStyle setSummaryText(CharSequence cs) {
            this.mSummaryText = Builder.limitCharSequenceLength(cs);
            this.mSummaryTextSet = true;
            return this;
        }

        public BigPictureStyle showBigPictureWhenCollapsed(boolean show) {
            this.mShowBigPictureWhenCollapsed = show;
            return this;
        }
    }

    public static class BigTextStyle extends Style {
        private static final String TEMPLATE_CLASS_NAME = "androidx.core.app.NotificationCompat$BigTextStyle";
        private CharSequence mBigText;

        public BigTextStyle() {
        }

        public BigTextStyle(Builder builder) {
            setBuilder(builder);
        }

        public void addCompatExtras(Bundle extras) {
            super.addCompatExtras(extras);
            if (Build.VERSION.SDK_INT < 21) {
                extras.putCharSequence(NotificationCompat.EXTRA_BIG_TEXT, this.mBigText);
            }
        }

        public void apply(NotificationBuilderWithBuilderAccessor builder) {
            if (Build.VERSION.SDK_INT >= 16) {
                Notification.BigTextStyle bigText = new Notification.BigTextStyle(builder.getBuilder()).setBigContentTitle(this.mBigContentTitle).bigText(this.mBigText);
                if (this.mSummaryTextSet) {
                    bigText.setSummaryText(this.mSummaryText);
                }
            }
        }

        public BigTextStyle bigText(CharSequence cs) {
            this.mBigText = Builder.limitCharSequenceLength(cs);
            return this;
        }

        /* access modifiers changed from: protected */
        public void clearCompatExtraKeys(Bundle extras) {
            super.clearCompatExtraKeys(extras);
            extras.remove(NotificationCompat.EXTRA_BIG_TEXT);
        }

        /* access modifiers changed from: protected */
        public String getClassName() {
            return TEMPLATE_CLASS_NAME;
        }

        /* access modifiers changed from: protected */
        public void restoreFromCompatExtras(Bundle extras) {
            super.restoreFromCompatExtras(extras);
            this.mBigText = extras.getCharSequence(NotificationCompat.EXTRA_BIG_TEXT);
        }

        public BigTextStyle setBigContentTitle(CharSequence title) {
            this.mBigContentTitle = Builder.limitCharSequenceLength(title);
            return this;
        }

        public BigTextStyle setSummaryText(CharSequence cs) {
            this.mSummaryText = Builder.limitCharSequenceLength(cs);
            this.mSummaryTextSet = true;
            return this;
        }
    }

    public static final class BubbleMetadata {
        private static final int FLAG_AUTO_EXPAND_BUBBLE = 1;
        private static final int FLAG_SUPPRESS_NOTIFICATION = 2;
        private PendingIntent mDeleteIntent;
        private int mDesiredHeight;
        private int mDesiredHeightResId;
        private int mFlags;
        private IconCompat mIcon;
        private PendingIntent mPendingIntent;
        private String mShortcutId;

        private static class Api29Impl {
            private Api29Impl() {
            }

            static BubbleMetadata fromPlatform(Notification.BubbleMetadata platformMetadata) {
                if (platformMetadata == null || platformMetadata.getIntent() == null) {
                    return null;
                }
                Builder suppressNotification = new Builder(platformMetadata.getIntent(), IconCompat.createFromIcon(platformMetadata.getIcon())).setAutoExpandBubble(platformMetadata.getAutoExpandBubble()).setDeleteIntent(platformMetadata.getDeleteIntent()).setSuppressNotification(platformMetadata.isNotificationSuppressed());
                if (platformMetadata.getDesiredHeight() != 0) {
                    suppressNotification.setDesiredHeight(platformMetadata.getDesiredHeight());
                }
                if (platformMetadata.getDesiredHeightResId() != 0) {
                    suppressNotification.setDesiredHeightResId(platformMetadata.getDesiredHeightResId());
                }
                return suppressNotification.build();
            }

            static Notification.BubbleMetadata toPlatform(BubbleMetadata compatMetadata) {
                if (compatMetadata == null || compatMetadata.getIntent() == null) {
                    return null;
                }
                Notification.BubbleMetadata.Builder suppressNotification = new Notification.BubbleMetadata.Builder().setIcon(compatMetadata.getIcon().toIcon()).setIntent(compatMetadata.getIntent()).setDeleteIntent(compatMetadata.getDeleteIntent()).setAutoExpandBubble(compatMetadata.getAutoExpandBubble()).setSuppressNotification(compatMetadata.isNotificationSuppressed());
                if (compatMetadata.getDesiredHeight() != 0) {
                    suppressNotification.setDesiredHeight(compatMetadata.getDesiredHeight());
                }
                if (compatMetadata.getDesiredHeightResId() != 0) {
                    suppressNotification.setDesiredHeightResId(compatMetadata.getDesiredHeightResId());
                }
                return suppressNotification.build();
            }
        }

        private static class Api30Impl {
            private Api30Impl() {
            }

            static BubbleMetadata fromPlatform(Notification.BubbleMetadata platformMetadata) {
                if (platformMetadata == null) {
                    return null;
                }
                Builder builder = platformMetadata.getShortcutId() != null ? new Builder(platformMetadata.getShortcutId()) : new Builder(platformMetadata.getIntent(), IconCompat.createFromIcon(platformMetadata.getIcon()));
                builder.setAutoExpandBubble(platformMetadata.getAutoExpandBubble()).setDeleteIntent(platformMetadata.getDeleteIntent()).setSuppressNotification(platformMetadata.isNotificationSuppressed());
                if (platformMetadata.getDesiredHeight() != 0) {
                    builder.setDesiredHeight(platformMetadata.getDesiredHeight());
                }
                if (platformMetadata.getDesiredHeightResId() != 0) {
                    builder.setDesiredHeightResId(platformMetadata.getDesiredHeightResId());
                }
                return builder.build();
            }

            static Notification.BubbleMetadata toPlatform(BubbleMetadata compatMetadata) {
                if (compatMetadata == null) {
                    return null;
                }
                Notification.BubbleMetadata.Builder builder = compatMetadata.getShortcutId() != null ? new Notification.BubbleMetadata.Builder(compatMetadata.getShortcutId()) : new Notification.BubbleMetadata.Builder(compatMetadata.getIntent(), compatMetadata.getIcon().toIcon());
                builder.setDeleteIntent(compatMetadata.getDeleteIntent()).setAutoExpandBubble(compatMetadata.getAutoExpandBubble()).setSuppressNotification(compatMetadata.isNotificationSuppressed());
                if (compatMetadata.getDesiredHeight() != 0) {
                    builder.setDesiredHeight(compatMetadata.getDesiredHeight());
                }
                if (compatMetadata.getDesiredHeightResId() != 0) {
                    builder.setDesiredHeightResId(compatMetadata.getDesiredHeightResId());
                }
                return builder.build();
            }
        }

        public static final class Builder {
            private PendingIntent mDeleteIntent;
            private int mDesiredHeight;
            private int mDesiredHeightResId;
            private int mFlags;
            private IconCompat mIcon;
            private PendingIntent mPendingIntent;
            private String mShortcutId;

            @Deprecated
            public Builder() {
            }

            public Builder(PendingIntent intent, IconCompat icon) {
                if (intent == null) {
                    throw new NullPointerException("Bubble requires non-null pending intent");
                } else if (icon != null) {
                    this.mPendingIntent = intent;
                    this.mIcon = icon;
                } else {
                    throw new NullPointerException("Bubbles require non-null icon");
                }
            }

            public Builder(String shortcutId) {
                if (!TextUtils.isEmpty(shortcutId)) {
                    this.mShortcutId = shortcutId;
                    return;
                }
                throw new NullPointerException("Bubble requires a non-null shortcut id");
            }

            private Builder setFlag(int mask, boolean value) {
                if (value) {
                    this.mFlags |= mask;
                } else {
                    this.mFlags &= ~mask;
                }
                return this;
            }

            public BubbleMetadata build() {
                String str = this.mShortcutId;
                if (str == null && this.mPendingIntent == null) {
                    throw new NullPointerException("Must supply pending intent or shortcut to bubble");
                } else if (str == null && this.mIcon == null) {
                    throw new NullPointerException("Must supply an icon or shortcut for the bubble");
                } else {
                    BubbleMetadata bubbleMetadata = new BubbleMetadata(this.mPendingIntent, this.mDeleteIntent, this.mIcon, this.mDesiredHeight, this.mDesiredHeightResId, this.mFlags, this.mShortcutId);
                    bubbleMetadata.setFlags(this.mFlags);
                    return bubbleMetadata;
                }
            }

            public Builder setAutoExpandBubble(boolean shouldExpand) {
                setFlag(1, shouldExpand);
                return this;
            }

            public Builder setDeleteIntent(PendingIntent deleteIntent) {
                this.mDeleteIntent = deleteIntent;
                return this;
            }

            public Builder setDesiredHeight(int height) {
                this.mDesiredHeight = Math.max(height, 0);
                this.mDesiredHeightResId = 0;
                return this;
            }

            public Builder setDesiredHeightResId(int heightResId) {
                this.mDesiredHeightResId = heightResId;
                this.mDesiredHeight = 0;
                return this;
            }

            public Builder setIcon(IconCompat icon) {
                if (this.mShortcutId != null) {
                    throw new IllegalStateException("Created as a shortcut bubble, cannot set an Icon. Consider using BubbleMetadata.Builder(PendingIntent,Icon) instead.");
                } else if (icon != null) {
                    this.mIcon = icon;
                    return this;
                } else {
                    throw new NullPointerException("Bubbles require non-null icon");
                }
            }

            public Builder setIntent(PendingIntent intent) {
                if (this.mShortcutId != null) {
                    throw new IllegalStateException("Created as a shortcut bubble, cannot set a PendingIntent. Consider using BubbleMetadata.Builder(PendingIntent,Icon) instead.");
                } else if (intent != null) {
                    this.mPendingIntent = intent;
                    return this;
                } else {
                    throw new NullPointerException("Bubble requires non-null pending intent");
                }
            }

            public Builder setSuppressNotification(boolean shouldSuppressNotif) {
                setFlag(2, shouldSuppressNotif);
                return this;
            }
        }

        private BubbleMetadata(PendingIntent expandIntent, PendingIntent deleteIntent, IconCompat icon, int height, int heightResId, int flags, String shortcutId) {
            this.mPendingIntent = expandIntent;
            this.mIcon = icon;
            this.mDesiredHeight = height;
            this.mDesiredHeightResId = heightResId;
            this.mDeleteIntent = deleteIntent;
            this.mFlags = flags;
            this.mShortcutId = shortcutId;
        }

        public static BubbleMetadata fromPlatform(Notification.BubbleMetadata platformMetadata) {
            if (platformMetadata == null) {
                return null;
            }
            if (Build.VERSION.SDK_INT >= 30) {
                return Api30Impl.fromPlatform(platformMetadata);
            }
            if (Build.VERSION.SDK_INT == 29) {
                return Api29Impl.fromPlatform(platformMetadata);
            }
            return null;
        }

        public static Notification.BubbleMetadata toPlatform(BubbleMetadata compatMetadata) {
            if (compatMetadata == null) {
                return null;
            }
            if (Build.VERSION.SDK_INT >= 30) {
                return Api30Impl.toPlatform(compatMetadata);
            }
            if (Build.VERSION.SDK_INT == 29) {
                return Api29Impl.toPlatform(compatMetadata);
            }
            return null;
        }

        public boolean getAutoExpandBubble() {
            return (this.mFlags & 1) != 0;
        }

        public PendingIntent getDeleteIntent() {
            return this.mDeleteIntent;
        }

        public int getDesiredHeight() {
            return this.mDesiredHeight;
        }

        public int getDesiredHeightResId() {
            return this.mDesiredHeightResId;
        }

        public IconCompat getIcon() {
            return this.mIcon;
        }

        public PendingIntent getIntent() {
            return this.mPendingIntent;
        }

        public String getShortcutId() {
            return this.mShortcutId;
        }

        public boolean isNotificationSuppressed() {
            return (this.mFlags & 2) != 0;
        }

        public void setFlags(int flags) {
            this.mFlags = flags;
        }
    }

    /* compiled from: 002E */
    public static class Builder {
        private static final int MAX_CHARSEQUENCE_LENGTH = 5120;
        public ArrayList<Action> mActions;
        boolean mAllowSystemGeneratedContextualActions;
        int mBadgeIcon;
        RemoteViews mBigContentView;
        BubbleMetadata mBubbleMetadata;
        String mCategory;
        String mChannelId;
        boolean mChronometerCountDown;
        int mColor;
        boolean mColorized;
        boolean mColorizedSet;
        CharSequence mContentInfo;
        PendingIntent mContentIntent;
        CharSequence mContentText;
        CharSequence mContentTitle;
        RemoteViews mContentView;
        public Context mContext;
        Bundle mExtras;
        int mFgsDeferBehavior;
        PendingIntent mFullScreenIntent;
        int mGroupAlertBehavior;
        String mGroupKey;
        boolean mGroupSummary;
        RemoteViews mHeadsUpContentView;
        ArrayList<Action> mInvisibleActions;
        Bitmap mLargeIcon;
        boolean mLocalOnly;
        LocusIdCompat mLocusId;
        Notification mNotification;
        int mNumber;
        @Deprecated
        public ArrayList<String> mPeople;
        public ArrayList<Person> mPersonList;
        int mPriority;
        int mProgress;
        boolean mProgressIndeterminate;
        int mProgressMax;
        Notification mPublicVersion;
        CharSequence[] mRemoteInputHistory;
        CharSequence mSettingsText;
        String mShortcutId;
        boolean mShowWhen;
        boolean mSilent;
        Icon mSmallIcon;
        String mSortKey;
        Style mStyle;
        CharSequence mSubText;
        RemoteViews mTickerView;
        long mTimeout;
        boolean mUseChronometer;
        int mVisibility;

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        @Deprecated
        public Builder(Context context) {
            this(context, (String) null);
            String str = null;
        }

        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public Builder(android.content.Context r9, android.app.Notification r10) {
            /*
                r8 = this;
                java.lang.String r0 = androidx.core.app.NotificationCompat.getChannelId(r10)
                mt.Log1F380D.a((java.lang.Object) r0)
                r8.<init>((android.content.Context) r9, (java.lang.String) r0)
                android.os.Bundle r0 = r10.extras
                androidx.core.app.NotificationCompat$Style r1 = androidx.core.app.NotificationCompat.Style.extractStyleFromNotification(r10)
                java.lang.CharSequence r2 = androidx.core.app.NotificationCompat.getContentTitle(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r8.setContentTitle(r2)
                java.lang.CharSequence r3 = androidx.core.app.NotificationCompat.getContentText(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setContentText(r3)
                java.lang.CharSequence r3 = androidx.core.app.NotificationCompat.getContentInfo(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setContentInfo(r3)
                java.lang.CharSequence r3 = androidx.core.app.NotificationCompat.getSubText(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setSubText(r3)
                java.lang.CharSequence r3 = androidx.core.app.NotificationCompat.getSettingsText(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setSettingsText(r3)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setStyle(r1)
                android.app.PendingIntent r3 = r10.contentIntent
                androidx.core.app.NotificationCompat$Builder r2 = r2.setContentIntent(r3)
                java.lang.String r3 = androidx.core.app.NotificationCompat.getGroup(r10)
                mt.Log1F380D.a((java.lang.Object) r3)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setGroup(r3)
                boolean r3 = androidx.core.app.NotificationCompat.isGroupSummary(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setGroupSummary(r3)
                androidx.core.content.LocusIdCompat r3 = androidx.core.app.NotificationCompat.getLocusId(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setLocusId(r3)
                long r3 = r10.when
                androidx.core.app.NotificationCompat$Builder r2 = r2.setWhen(r3)
                boolean r3 = androidx.core.app.NotificationCompat.getShowWhen(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setShowWhen(r3)
                boolean r3 = androidx.core.app.NotificationCompat.getUsesChronometer(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setUsesChronometer(r3)
                boolean r3 = androidx.core.app.NotificationCompat.getAutoCancel(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setAutoCancel(r3)
                boolean r3 = androidx.core.app.NotificationCompat.getOnlyAlertOnce(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setOnlyAlertOnce(r3)
                boolean r3 = androidx.core.app.NotificationCompat.getOngoing(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setOngoing(r3)
                boolean r3 = androidx.core.app.NotificationCompat.getLocalOnly(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setLocalOnly(r3)
                android.graphics.Bitmap r3 = r10.largeIcon
                androidx.core.app.NotificationCompat$Builder r2 = r2.setLargeIcon(r3)
                int r3 = androidx.core.app.NotificationCompat.getBadgeIconType(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setBadgeIconType(r3)
                java.lang.String r3 = androidx.core.app.NotificationCompat.getCategory(r10)
                mt.Log1F380D.a((java.lang.Object) r3)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setCategory(r3)
                androidx.core.app.NotificationCompat$BubbleMetadata r3 = androidx.core.app.NotificationCompat.getBubbleMetadata(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setBubbleMetadata(r3)
                int r3 = r10.number
                androidx.core.app.NotificationCompat$Builder r2 = r2.setNumber(r3)
                java.lang.CharSequence r3 = r10.tickerText
                androidx.core.app.NotificationCompat$Builder r2 = r2.setTicker(r3)
                android.app.PendingIntent r3 = r10.contentIntent
                androidx.core.app.NotificationCompat$Builder r2 = r2.setContentIntent(r3)
                android.app.PendingIntent r3 = r10.deleteIntent
                androidx.core.app.NotificationCompat$Builder r2 = r2.setDeleteIntent(r3)
                android.app.PendingIntent r3 = r10.fullScreenIntent
                boolean r4 = androidx.core.app.NotificationCompat.getHighPriority(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setFullScreenIntent(r3, r4)
                android.net.Uri r3 = r10.sound
                int r4 = r10.audioStreamType
                androidx.core.app.NotificationCompat$Builder r2 = r2.setSound(r3, r4)
                long[] r3 = r10.vibrate
                androidx.core.app.NotificationCompat$Builder r2 = r2.setVibrate(r3)
                int r3 = r10.ledARGB
                int r4 = r10.ledOnMS
                int r5 = r10.ledOffMS
                androidx.core.app.NotificationCompat$Builder r2 = r2.setLights(r3, r4, r5)
                int r3 = r10.defaults
                androidx.core.app.NotificationCompat$Builder r2 = r2.setDefaults(r3)
                int r3 = r10.priority
                androidx.core.app.NotificationCompat$Builder r2 = r2.setPriority(r3)
                int r3 = androidx.core.app.NotificationCompat.getColor(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setColor(r3)
                int r3 = androidx.core.app.NotificationCompat.getVisibility(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setVisibility(r3)
                android.app.Notification r3 = androidx.core.app.NotificationCompat.getPublicVersion(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setPublicVersion(r3)
                java.lang.String r3 = androidx.core.app.NotificationCompat.getSortKey(r10)
                mt.Log1F380D.a((java.lang.Object) r3)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setSortKey(r3)
                long r3 = androidx.core.app.NotificationCompat.getTimeoutAfter(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setTimeoutAfter(r3)
                java.lang.String r3 = androidx.core.app.NotificationCompat.getShortcutId(r10)
                mt.Log1F380D.a((java.lang.Object) r3)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setShortcutId(r3)
                java.lang.String r3 = "android.progressMax"
                int r3 = r0.getInt(r3)
                java.lang.String r4 = "android.progress"
                int r4 = r0.getInt(r4)
                java.lang.String r5 = "android.progressIndeterminate"
                boolean r5 = r0.getBoolean(r5)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setProgress(r3, r4, r5)
                boolean r3 = androidx.core.app.NotificationCompat.getAllowSystemGeneratedContextualActions(r10)
                androidx.core.app.NotificationCompat$Builder r2 = r2.setAllowSystemGeneratedContextualActions(r3)
                int r3 = r10.icon
                int r4 = r10.iconLevel
                androidx.core.app.NotificationCompat$Builder r2 = r2.setSmallIcon(r3, r4)
                android.os.Bundle r3 = getExtrasWithoutDuplicateData(r10, r1)
                r2.addExtras(r3)
                int r2 = android.os.Build.VERSION.SDK_INT
                r3 = 23
                if (r2 < r3) goto L_0x0169
                android.graphics.drawable.Icon r2 = r10.getSmallIcon()
                r8.mSmallIcon = r2
            L_0x0169:
                android.app.Notification$Action[] r2 = r10.actions
                r3 = 0
                if (r2 == 0) goto L_0x0189
                android.app.Notification$Action[] r2 = r10.actions
                int r2 = r2.length
                if (r2 == 0) goto L_0x0189
                android.app.Notification$Action[] r2 = r10.actions
                int r4 = r2.length
                r5 = r3
            L_0x0177:
                if (r5 >= r4) goto L_0x0189
                r6 = r2[r5]
                androidx.core.app.NotificationCompat$Action$Builder r7 = androidx.core.app.NotificationCompat.Action.Builder.fromAndroidAction(r6)
                androidx.core.app.NotificationCompat$Action r7 = r7.build()
                r8.addAction(r7)
                int r5 = r5 + 1
                goto L_0x0177
            L_0x0189:
                int r2 = android.os.Build.VERSION.SDK_INT
                r4 = 21
                if (r2 < r4) goto L_0x01ae
                java.util.List r2 = androidx.core.app.NotificationCompat.getInvisibleActions(r10)
                boolean r4 = r2.isEmpty()
                if (r4 != 0) goto L_0x01ae
                java.util.Iterator r4 = r2.iterator()
            L_0x019e:
                boolean r5 = r4.hasNext()
                if (r5 == 0) goto L_0x01ae
                java.lang.Object r5 = r4.next()
                androidx.core.app.NotificationCompat$Action r5 = (androidx.core.app.NotificationCompat.Action) r5
                r8.addInvisibleAction(r5)
                goto L_0x019e
            L_0x01ae:
                android.os.Bundle r2 = r10.extras
                java.lang.String r4 = "android.people"
                java.lang.String[] r2 = r2.getStringArray(r4)
                if (r2 == 0) goto L_0x01c6
                int r4 = r2.length
                if (r4 == 0) goto L_0x01c6
                int r4 = r2.length
            L_0x01bc:
                if (r3 >= r4) goto L_0x01c6
                r5 = r2[r3]
                r8.addPerson((java.lang.String) r5)
                int r3 = r3 + 1
                goto L_0x01bc
            L_0x01c6:
                int r3 = android.os.Build.VERSION.SDK_INT
                r4 = 28
                if (r3 < r4) goto L_0x01f4
                android.os.Bundle r3 = r10.extras
                java.lang.String r4 = "android.people.list"
                java.util.ArrayList r3 = r3.getParcelableArrayList(r4)
                if (r3 == 0) goto L_0x01f4
                boolean r4 = r3.isEmpty()
                if (r4 != 0) goto L_0x01f4
                java.util.Iterator r4 = r3.iterator()
            L_0x01e0:
                boolean r5 = r4.hasNext()
                if (r5 == 0) goto L_0x01f4
                java.lang.Object r5 = r4.next()
                android.app.Person r5 = (android.app.Person) r5
                androidx.core.app.Person r6 = androidx.core.app.Person.fromAndroidPerson(r5)
                r8.addPerson((androidx.core.app.Person) r6)
                goto L_0x01e0
            L_0x01f4:
                int r3 = android.os.Build.VERSION.SDK_INT
                r4 = 24
                if (r3 < r4) goto L_0x020a
                java.lang.String r3 = "android.chronometerCountDown"
                boolean r4 = r0.containsKey(r3)
                if (r4 == 0) goto L_0x020a
                boolean r3 = r0.getBoolean(r3)
                r8.setChronometerCountDown(r3)
            L_0x020a:
                int r3 = android.os.Build.VERSION.SDK_INT
                r4 = 26
                if (r3 < r4) goto L_0x021f
                java.lang.String r3 = "android.colorized"
                boolean r4 = r0.containsKey(r3)
                if (r4 == 0) goto L_0x021f
                boolean r3 = r0.getBoolean(r3)
                r8.setColorized(r3)
            L_0x021f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.core.app.NotificationCompat.Builder.<init>(android.content.Context, android.app.Notification):void");
        }

        public Builder(Context context, String channelId) {
            this.mActions = new ArrayList<>();
            this.mPersonList = new ArrayList<>();
            this.mInvisibleActions = new ArrayList<>();
            this.mShowWhen = true;
            this.mLocalOnly = false;
            this.mColor = 0;
            this.mVisibility = 0;
            this.mBadgeIcon = 0;
            this.mGroupAlertBehavior = 0;
            this.mFgsDeferBehavior = 0;
            Notification notification = new Notification();
            this.mNotification = notification;
            this.mContext = context;
            this.mChannelId = channelId;
            notification.when = System.currentTimeMillis();
            this.mNotification.audioStreamType = -1;
            this.mPriority = 0;
            this.mPeople = new ArrayList<>();
            this.mAllowSystemGeneratedContextualActions = true;
        }

        private static Bundle getExtrasWithoutDuplicateData(Notification notification, Style style) {
            if (notification.extras == null) {
                return null;
            }
            Bundle bundle = new Bundle(notification.extras);
            bundle.remove(NotificationCompat.EXTRA_TITLE);
            bundle.remove(NotificationCompat.EXTRA_TEXT);
            bundle.remove(NotificationCompat.EXTRA_INFO_TEXT);
            bundle.remove(NotificationCompat.EXTRA_SUB_TEXT);
            bundle.remove(NotificationCompat.EXTRA_CHANNEL_ID);
            bundle.remove(NotificationCompat.EXTRA_CHANNEL_GROUP_ID);
            bundle.remove(NotificationCompat.EXTRA_SHOW_WHEN);
            bundle.remove(NotificationCompat.EXTRA_PROGRESS);
            bundle.remove(NotificationCompat.EXTRA_PROGRESS_MAX);
            bundle.remove(NotificationCompat.EXTRA_PROGRESS_INDETERMINATE);
            bundle.remove(NotificationCompat.EXTRA_CHRONOMETER_COUNT_DOWN);
            bundle.remove(NotificationCompat.EXTRA_COLORIZED);
            bundle.remove(NotificationCompat.EXTRA_PEOPLE_LIST);
            bundle.remove(NotificationCompat.EXTRA_PEOPLE);
            bundle.remove(NotificationCompatExtras.EXTRA_SORT_KEY);
            bundle.remove(NotificationCompatExtras.EXTRA_GROUP_KEY);
            bundle.remove(NotificationCompatExtras.EXTRA_GROUP_SUMMARY);
            bundle.remove(NotificationCompatExtras.EXTRA_LOCAL_ONLY);
            bundle.remove(NotificationCompatExtras.EXTRA_ACTION_EXTRAS);
            Bundle bundle2 = bundle.getBundle("android.car.EXTENSIONS");
            if (bundle2 != null) {
                Bundle bundle3 = new Bundle(bundle2);
                bundle3.remove("invisible_actions");
                bundle.putBundle("android.car.EXTENSIONS", bundle3);
            }
            if (style != null) {
                style.clearCompatExtraKeys(bundle);
            }
            return bundle;
        }

        protected static CharSequence limitCharSequenceLength(CharSequence cs) {
            return (cs != null && cs.length() > MAX_CHARSEQUENCE_LENGTH) ? cs.subSequence(0, MAX_CHARSEQUENCE_LENGTH) : cs;
        }

        private Bitmap reduceLargeIconSize(Bitmap icon) {
            if (icon == null || Build.VERSION.SDK_INT >= 27) {
                return icon;
            }
            Resources resources = this.mContext.getResources();
            int dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.compat_notification_large_icon_max_width);
            int dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.compat_notification_large_icon_max_height);
            if (icon.getWidth() <= dimensionPixelSize && icon.getHeight() <= dimensionPixelSize2) {
                return icon;
            }
            double min = Math.min(((double) dimensionPixelSize) / ((double) Math.max(1, icon.getWidth())), ((double) dimensionPixelSize2) / ((double) Math.max(1, icon.getHeight())));
            return Bitmap.createScaledBitmap(icon, (int) Math.ceil(((double) icon.getWidth()) * min), (int) Math.ceil(((double) icon.getHeight()) * min), true);
        }

        private void setFlag(int mask, boolean value) {
            if (value) {
                this.mNotification.flags |= mask;
                return;
            }
            this.mNotification.flags &= ~mask;
        }

        private boolean useExistingRemoteView() {
            Style style = this.mStyle;
            return style == null || !style.displayCustomViewInline();
        }

        public Builder addAction(int icon, CharSequence title, PendingIntent intent) {
            this.mActions.add(new Action(icon, title, intent));
            return this;
        }

        public Builder addAction(Action action) {
            if (action != null) {
                this.mActions.add(action);
            }
            return this;
        }

        public Builder addExtras(Bundle extras) {
            if (extras != null) {
                Bundle bundle = this.mExtras;
                if (bundle == null) {
                    this.mExtras = new Bundle(extras);
                } else {
                    bundle.putAll(extras);
                }
            }
            return this;
        }

        public Builder addInvisibleAction(int icon, CharSequence title, PendingIntent intent) {
            this.mInvisibleActions.add(new Action(icon, title, intent));
            return this;
        }

        public Builder addInvisibleAction(Action action) {
            if (action != null) {
                this.mInvisibleActions.add(action);
            }
            return this;
        }

        public Builder addPerson(Person person) {
            if (person != null) {
                this.mPersonList.add(person);
            }
            return this;
        }

        @Deprecated
        public Builder addPerson(String uri) {
            if (uri != null && !uri.isEmpty()) {
                this.mPeople.add(uri);
            }
            return this;
        }

        public Notification build() {
            return new NotificationCompatBuilder(this).build();
        }

        public Builder clearActions() {
            this.mActions.clear();
            return this;
        }

        public Builder clearInvisibleActions() {
            this.mInvisibleActions.clear();
            Bundle bundle = this.mExtras.getBundle("android.car.EXTENSIONS");
            if (bundle != null) {
                Bundle bundle2 = new Bundle(bundle);
                bundle2.remove("invisible_actions");
                this.mExtras.putBundle("android.car.EXTENSIONS", bundle2);
            }
            return this;
        }

        public Builder clearPeople() {
            this.mPersonList.clear();
            this.mPeople.clear();
            return this;
        }

        public RemoteViews createBigContentView() {
            RemoteViews makeBigContentView;
            if (Build.VERSION.SDK_INT < 16) {
                return null;
            }
            if (this.mBigContentView != null && useExistingRemoteView()) {
                return this.mBigContentView;
            }
            NotificationCompatBuilder notificationCompatBuilder = new NotificationCompatBuilder(this);
            Style style = this.mStyle;
            if (style != null && (makeBigContentView = style.makeBigContentView(notificationCompatBuilder)) != null) {
                return makeBigContentView;
            }
            Notification build = notificationCompatBuilder.build();
            return Build.VERSION.SDK_INT >= 24 ? Notification.Builder.recoverBuilder(this.mContext, build).createBigContentView() : build.bigContentView;
        }

        public RemoteViews createContentView() {
            RemoteViews makeContentView;
            if (this.mContentView != null && useExistingRemoteView()) {
                return this.mContentView;
            }
            NotificationCompatBuilder notificationCompatBuilder = new NotificationCompatBuilder(this);
            Style style = this.mStyle;
            if (style != null && (makeContentView = style.makeContentView(notificationCompatBuilder)) != null) {
                return makeContentView;
            }
            Notification build = notificationCompatBuilder.build();
            return Build.VERSION.SDK_INT >= 24 ? Notification.Builder.recoverBuilder(this.mContext, build).createContentView() : build.contentView;
        }

        public RemoteViews createHeadsUpContentView() {
            RemoteViews makeHeadsUpContentView;
            if (Build.VERSION.SDK_INT < 21) {
                return null;
            }
            if (this.mHeadsUpContentView != null && useExistingRemoteView()) {
                return this.mHeadsUpContentView;
            }
            NotificationCompatBuilder notificationCompatBuilder = new NotificationCompatBuilder(this);
            Style style = this.mStyle;
            if (style != null && (makeHeadsUpContentView = style.makeHeadsUpContentView(notificationCompatBuilder)) != null) {
                return makeHeadsUpContentView;
            }
            Notification build = notificationCompatBuilder.build();
            return Build.VERSION.SDK_INT >= 24 ? Notification.Builder.recoverBuilder(this.mContext, build).createHeadsUpContentView() : build.headsUpContentView;
        }

        public Builder extend(Extender extender) {
            extender.extend(this);
            return this;
        }

        public RemoteViews getBigContentView() {
            return this.mBigContentView;
        }

        public BubbleMetadata getBubbleMetadata() {
            return this.mBubbleMetadata;
        }

        public int getColor() {
            return this.mColor;
        }

        public RemoteViews getContentView() {
            return this.mContentView;
        }

        public Bundle getExtras() {
            if (this.mExtras == null) {
                this.mExtras = new Bundle();
            }
            return this.mExtras;
        }

        public int getForegroundServiceBehavior() {
            return this.mFgsDeferBehavior;
        }

        public RemoteViews getHeadsUpContentView() {
            return this.mHeadsUpContentView;
        }

        @Deprecated
        public Notification getNotification() {
            return build();
        }

        public int getPriority() {
            return this.mPriority;
        }

        public long getWhenIfShowing() {
            if (this.mShowWhen) {
                return this.mNotification.when;
            }
            return 0;
        }

        public Builder setAllowSystemGeneratedContextualActions(boolean allowed) {
            this.mAllowSystemGeneratedContextualActions = allowed;
            return this;
        }

        public Builder setAutoCancel(boolean autoCancel) {
            setFlag(16, autoCancel);
            return this;
        }

        public Builder setBadgeIconType(int icon) {
            this.mBadgeIcon = icon;
            return this;
        }

        public Builder setBubbleMetadata(BubbleMetadata data) {
            this.mBubbleMetadata = data;
            return this;
        }

        public Builder setCategory(String category) {
            this.mCategory = category;
            return this;
        }

        public Builder setChannelId(String channelId) {
            this.mChannelId = channelId;
            return this;
        }

        public Builder setChronometerCountDown(boolean countsDown) {
            this.mChronometerCountDown = countsDown;
            getExtras().putBoolean(NotificationCompat.EXTRA_CHRONOMETER_COUNT_DOWN, countsDown);
            return this;
        }

        public Builder setColor(int argb) {
            this.mColor = argb;
            return this;
        }

        public Builder setColorized(boolean colorize) {
            this.mColorized = colorize;
            this.mColorizedSet = true;
            return this;
        }

        public Builder setContent(RemoteViews views) {
            this.mNotification.contentView = views;
            return this;
        }

        public Builder setContentInfo(CharSequence info) {
            this.mContentInfo = limitCharSequenceLength(info);
            return this;
        }

        public Builder setContentIntent(PendingIntent intent) {
            this.mContentIntent = intent;
            return this;
        }

        public Builder setContentText(CharSequence text) {
            this.mContentText = limitCharSequenceLength(text);
            return this;
        }

        public Builder setContentTitle(CharSequence title) {
            this.mContentTitle = limitCharSequenceLength(title);
            return this;
        }

        public Builder setCustomBigContentView(RemoteViews contentView) {
            this.mBigContentView = contentView;
            return this;
        }

        public Builder setCustomContentView(RemoteViews contentView) {
            this.mContentView = contentView;
            return this;
        }

        public Builder setCustomHeadsUpContentView(RemoteViews contentView) {
            this.mHeadsUpContentView = contentView;
            return this;
        }

        public Builder setDefaults(int defaults) {
            this.mNotification.defaults = defaults;
            if ((defaults & 4) != 0) {
                this.mNotification.flags |= 1;
            }
            return this;
        }

        public Builder setDeleteIntent(PendingIntent intent) {
            this.mNotification.deleteIntent = intent;
            return this;
        }

        public Builder setExtras(Bundle extras) {
            this.mExtras = extras;
            return this;
        }

        public Builder setForegroundServiceBehavior(int behavior) {
            this.mFgsDeferBehavior = behavior;
            return this;
        }

        public Builder setFullScreenIntent(PendingIntent intent, boolean highPriority) {
            this.mFullScreenIntent = intent;
            setFlag(128, highPriority);
            return this;
        }

        public Builder setGroup(String groupKey) {
            this.mGroupKey = groupKey;
            return this;
        }

        public Builder setGroupAlertBehavior(int groupAlertBehavior) {
            this.mGroupAlertBehavior = groupAlertBehavior;
            return this;
        }

        public Builder setGroupSummary(boolean isGroupSummary) {
            this.mGroupSummary = isGroupSummary;
            return this;
        }

        public Builder setLargeIcon(Bitmap icon) {
            this.mLargeIcon = reduceLargeIconSize(icon);
            return this;
        }

        public Builder setLights(int argb, int onMs, int offMs) {
            this.mNotification.ledARGB = argb;
            this.mNotification.ledOnMS = onMs;
            this.mNotification.ledOffMS = offMs;
            int i = 1;
            boolean z = (this.mNotification.ledOnMS == 0 || this.mNotification.ledOffMS == 0) ? false : true;
            Notification notification = this.mNotification;
            int i2 = notification.flags & -2;
            if (!z) {
                i = 0;
            }
            notification.flags = i | i2;
            return this;
        }

        public Builder setLocalOnly(boolean b) {
            this.mLocalOnly = b;
            return this;
        }

        public Builder setLocusId(LocusIdCompat locusId) {
            this.mLocusId = locusId;
            return this;
        }

        @Deprecated
        public Builder setNotificationSilent() {
            this.mSilent = true;
            return this;
        }

        public Builder setNumber(int number) {
            this.mNumber = number;
            return this;
        }

        public Builder setOngoing(boolean ongoing) {
            setFlag(2, ongoing);
            return this;
        }

        public Builder setOnlyAlertOnce(boolean onlyAlertOnce) {
            setFlag(8, onlyAlertOnce);
            return this;
        }

        public Builder setPriority(int pri) {
            this.mPriority = pri;
            return this;
        }

        public Builder setProgress(int max, int progress, boolean indeterminate) {
            this.mProgressMax = max;
            this.mProgress = progress;
            this.mProgressIndeterminate = indeterminate;
            return this;
        }

        public Builder setPublicVersion(Notification n) {
            this.mPublicVersion = n;
            return this;
        }

        public Builder setRemoteInputHistory(CharSequence[] text) {
            this.mRemoteInputHistory = text;
            return this;
        }

        public Builder setSettingsText(CharSequence text) {
            this.mSettingsText = limitCharSequenceLength(text);
            return this;
        }

        public Builder setShortcutId(String shortcutId) {
            this.mShortcutId = shortcutId;
            return this;
        }

        public Builder setShortcutInfo(ShortcutInfoCompat shortcutInfo) {
            if (shortcutInfo == null) {
                return this;
            }
            this.mShortcutId = shortcutInfo.getId();
            if (this.mLocusId == null) {
                if (shortcutInfo.getLocusId() != null) {
                    this.mLocusId = shortcutInfo.getLocusId();
                } else if (shortcutInfo.getId() != null) {
                    this.mLocusId = new LocusIdCompat(shortcutInfo.getId());
                }
            }
            if (this.mContentTitle == null) {
                setContentTitle(shortcutInfo.getShortLabel());
            }
            return this;
        }

        public Builder setShowWhen(boolean show) {
            this.mShowWhen = show;
            return this;
        }

        public Builder setSilent(boolean silent) {
            this.mSilent = silent;
            return this;
        }

        public Builder setSmallIcon(int icon) {
            this.mNotification.icon = icon;
            return this;
        }

        public Builder setSmallIcon(int icon, int level) {
            this.mNotification.icon = icon;
            this.mNotification.iconLevel = level;
            return this;
        }

        public Builder setSmallIcon(IconCompat icon) {
            this.mSmallIcon = icon.toIcon(this.mContext);
            return this;
        }

        public Builder setSortKey(String sortKey) {
            this.mSortKey = sortKey;
            return this;
        }

        public Builder setSound(Uri sound) {
            this.mNotification.sound = sound;
            this.mNotification.audioStreamType = -1;
            if (Build.VERSION.SDK_INT >= 21) {
                this.mNotification.audioAttributes = new AudioAttributes.Builder().setContentType(4).setUsage(5).build();
            }
            return this;
        }

        public Builder setSound(Uri sound, int streamType) {
            this.mNotification.sound = sound;
            this.mNotification.audioStreamType = streamType;
            if (Build.VERSION.SDK_INT >= 21) {
                this.mNotification.audioAttributes = new AudioAttributes.Builder().setContentType(4).setLegacyStreamType(streamType).build();
            }
            return this;
        }

        public Builder setStyle(Style style) {
            if (this.mStyle != style) {
                this.mStyle = style;
                if (style != null) {
                    style.setBuilder(this);
                }
            }
            return this;
        }

        public Builder setSubText(CharSequence text) {
            this.mSubText = limitCharSequenceLength(text);
            return this;
        }

        public Builder setTicker(CharSequence tickerText) {
            this.mNotification.tickerText = limitCharSequenceLength(tickerText);
            return this;
        }

        @Deprecated
        public Builder setTicker(CharSequence tickerText, RemoteViews views) {
            this.mNotification.tickerText = limitCharSequenceLength(tickerText);
            this.mTickerView = views;
            return this;
        }

        public Builder setTimeoutAfter(long durationMs) {
            this.mTimeout = durationMs;
            return this;
        }

        public Builder setUsesChronometer(boolean b) {
            this.mUseChronometer = b;
            return this;
        }

        public Builder setVibrate(long[] pattern) {
            this.mNotification.vibrate = pattern;
            return this;
        }

        public Builder setVisibility(int visibility) {
            this.mVisibility = visibility;
            return this;
        }

        public Builder setWhen(long when) {
            this.mNotification.when = when;
            return this;
        }
    }

    public static final class CarExtender implements Extender {
        static final String EXTRA_CAR_EXTENDER = "android.car.EXTENSIONS";
        private static final String EXTRA_COLOR = "app_color";
        private static final String EXTRA_CONVERSATION = "car_conversation";
        static final String EXTRA_INVISIBLE_ACTIONS = "invisible_actions";
        private static final String EXTRA_LARGE_ICON = "large_icon";
        private static final String KEY_AUTHOR = "author";
        private static final String KEY_MESSAGES = "messages";
        private static final String KEY_ON_READ = "on_read";
        private static final String KEY_ON_REPLY = "on_reply";
        private static final String KEY_PARTICIPANTS = "participants";
        private static final String KEY_REMOTE_INPUT = "remote_input";
        private static final String KEY_TEXT = "text";
        private static final String KEY_TIMESTAMP = "timestamp";
        private int mColor = 0;
        private Bitmap mLargeIcon;
        private UnreadConversation mUnreadConversation;

        @Deprecated
        public static class UnreadConversation {
            private final long mLatestTimestamp;
            private final String[] mMessages;
            private final String[] mParticipants;
            private final PendingIntent mReadPendingIntent;
            private final RemoteInput mRemoteInput;
            private final PendingIntent mReplyPendingIntent;

            public static class Builder {
                private long mLatestTimestamp;
                private final List<String> mMessages = new ArrayList();
                private final String mParticipant;
                private PendingIntent mReadPendingIntent;
                private RemoteInput mRemoteInput;
                private PendingIntent mReplyPendingIntent;

                public Builder(String name) {
                    this.mParticipant = name;
                }

                public Builder addMessage(String message) {
                    if (message != null) {
                        this.mMessages.add(message);
                    }
                    return this;
                }

                public UnreadConversation build() {
                    List<String> list = this.mMessages;
                    String[] strArr = {this.mParticipant};
                    return new UnreadConversation((String[]) list.toArray(new String[list.size()]), this.mRemoteInput, this.mReplyPendingIntent, this.mReadPendingIntent, strArr, this.mLatestTimestamp);
                }

                public Builder setLatestTimestamp(long timestamp) {
                    this.mLatestTimestamp = timestamp;
                    return this;
                }

                public Builder setReadPendingIntent(PendingIntent pendingIntent) {
                    this.mReadPendingIntent = pendingIntent;
                    return this;
                }

                public Builder setReplyAction(PendingIntent pendingIntent, RemoteInput remoteInput) {
                    this.mRemoteInput = remoteInput;
                    this.mReplyPendingIntent = pendingIntent;
                    return this;
                }
            }

            UnreadConversation(String[] messages, RemoteInput remoteInput, PendingIntent replyPendingIntent, PendingIntent readPendingIntent, String[] participants, long latestTimestamp) {
                this.mMessages = messages;
                this.mRemoteInput = remoteInput;
                this.mReadPendingIntent = readPendingIntent;
                this.mReplyPendingIntent = replyPendingIntent;
                this.mParticipants = participants;
                this.mLatestTimestamp = latestTimestamp;
            }

            public long getLatestTimestamp() {
                return this.mLatestTimestamp;
            }

            public String[] getMessages() {
                return this.mMessages;
            }

            public String getParticipant() {
                String[] strArr = this.mParticipants;
                if (strArr.length > 0) {
                    return strArr[0];
                }
                return null;
            }

            public String[] getParticipants() {
                return this.mParticipants;
            }

            public PendingIntent getReadPendingIntent() {
                return this.mReadPendingIntent;
            }

            public RemoteInput getRemoteInput() {
                return this.mRemoteInput;
            }

            public PendingIntent getReplyPendingIntent() {
                return this.mReplyPendingIntent;
            }
        }

        public CarExtender() {
        }

        public CarExtender(Notification notification) {
            if (Build.VERSION.SDK_INT >= 21) {
                Bundle bundle = NotificationCompat.getExtras(notification) == null ? null : NotificationCompat.getExtras(notification).getBundle(EXTRA_CAR_EXTENDER);
                if (bundle != null) {
                    this.mLargeIcon = (Bitmap) bundle.getParcelable(EXTRA_LARGE_ICON);
                    this.mColor = bundle.getInt(EXTRA_COLOR, 0);
                    this.mUnreadConversation = getUnreadConversationFromBundle(bundle.getBundle(EXTRA_CONVERSATION));
                }
            }
        }

        private static Bundle getBundleForUnreadConversation(UnreadConversation uc) {
            Bundle bundle = new Bundle();
            String str = null;
            if (uc.getParticipants() != null && uc.getParticipants().length > 1) {
                str = uc.getParticipants()[0];
            }
            Parcelable[] parcelableArr = new Parcelable[uc.getMessages().length];
            for (int i = 0; i < parcelableArr.length; i++) {
                Bundle bundle2 = new Bundle();
                bundle2.putString(KEY_TEXT, uc.getMessages()[i]);
                bundle2.putString(KEY_AUTHOR, str);
                parcelableArr[i] = bundle2;
            }
            bundle.putParcelableArray(KEY_MESSAGES, parcelableArr);
            RemoteInput remoteInput = uc.getRemoteInput();
            if (remoteInput != null) {
                bundle.putParcelable(KEY_REMOTE_INPUT, new RemoteInput.Builder(remoteInput.getResultKey()).setLabel(remoteInput.getLabel()).setChoices(remoteInput.getChoices()).setAllowFreeFormInput(remoteInput.getAllowFreeFormInput()).addExtras(remoteInput.getExtras()).build());
            }
            bundle.putParcelable(KEY_ON_REPLY, uc.getReplyPendingIntent());
            bundle.putParcelable(KEY_ON_READ, uc.getReadPendingIntent());
            bundle.putStringArray(KEY_PARTICIPANTS, uc.getParticipants());
            bundle.putLong(KEY_TIMESTAMP, uc.getLatestTimestamp());
            return bundle;
        }

        private static UnreadConversation getUnreadConversationFromBundle(Bundle b) {
            RemoteInput remoteInput;
            Bundle bundle = b;
            if (bundle == null) {
                return null;
            }
            Parcelable[] parcelableArray = bundle.getParcelableArray(KEY_MESSAGES);
            String[] strArr = null;
            if (parcelableArray != null) {
                String[] strArr2 = new String[parcelableArray.length];
                boolean z = true;
                int i = 0;
                while (true) {
                    if (i >= strArr2.length) {
                        break;
                    } else if (!(parcelableArray[i] instanceof Bundle)) {
                        z = false;
                        break;
                    } else {
                        strArr2[i] = ((Bundle) parcelableArray[i]).getString(KEY_TEXT);
                        if (strArr2[i] == null) {
                            z = false;
                            break;
                        }
                        i++;
                    }
                }
                if (!z) {
                    return null;
                }
                strArr = strArr2;
            }
            PendingIntent pendingIntent = (PendingIntent) bundle.getParcelable(KEY_ON_READ);
            PendingIntent pendingIntent2 = (PendingIntent) bundle.getParcelable(KEY_ON_REPLY);
            RemoteInput remoteInput2 = (RemoteInput) bundle.getParcelable(KEY_REMOTE_INPUT);
            String[] stringArray = bundle.getStringArray(KEY_PARTICIPANTS);
            if (stringArray == null || stringArray.length != 1) {
                return null;
            }
            if (remoteInput2 != null) {
                remoteInput = new RemoteInput(remoteInput2.getResultKey(), remoteInput2.getLabel(), remoteInput2.getChoices(), remoteInput2.getAllowFreeFormInput(), Build.VERSION.SDK_INT >= 29 ? remoteInput2.getEditChoicesBeforeSending() : 0, remoteInput2.getExtras(), (Set<String>) null);
            } else {
                remoteInput = null;
            }
            return new UnreadConversation(strArr, remoteInput, pendingIntent2, pendingIntent, stringArray, bundle.getLong(KEY_TIMESTAMP));
        }

        public Builder extend(Builder builder) {
            if (Build.VERSION.SDK_INT < 21) {
                return builder;
            }
            Bundle bundle = new Bundle();
            Bitmap bitmap = this.mLargeIcon;
            if (bitmap != null) {
                bundle.putParcelable(EXTRA_LARGE_ICON, bitmap);
            }
            int i = this.mColor;
            if (i != 0) {
                bundle.putInt(EXTRA_COLOR, i);
            }
            UnreadConversation unreadConversation = this.mUnreadConversation;
            if (unreadConversation != null) {
                bundle.putBundle(EXTRA_CONVERSATION, getBundleForUnreadConversation(unreadConversation));
            }
            builder.getExtras().putBundle(EXTRA_CAR_EXTENDER, bundle);
            return builder;
        }

        public int getColor() {
            return this.mColor;
        }

        public Bitmap getLargeIcon() {
            return this.mLargeIcon;
        }

        @Deprecated
        public UnreadConversation getUnreadConversation() {
            return this.mUnreadConversation;
        }

        public CarExtender setColor(int color) {
            this.mColor = color;
            return this;
        }

        public CarExtender setLargeIcon(Bitmap largeIcon) {
            this.mLargeIcon = largeIcon;
            return this;
        }

        @Deprecated
        public CarExtender setUnreadConversation(UnreadConversation unreadConversation) {
            this.mUnreadConversation = unreadConversation;
            return this;
        }
    }

    public static class DecoratedCustomViewStyle extends Style {
        private static final int MAX_ACTION_BUTTONS = 3;
        private static final String TEMPLATE_CLASS_NAME = "androidx.core.app.NotificationCompat$DecoratedCustomViewStyle";

        private RemoteViews createRemoteViews(RemoteViews innerView, boolean showActions) {
            int min;
            int i = 0;
            RemoteViews applyStandardTemplate = applyStandardTemplate(true, R.layout.notification_template_custom_big, false);
            applyStandardTemplate.removeAllViews(R.id.actions);
            boolean z = false;
            List<Action> nonContextualActions = getNonContextualActions(this.mBuilder.mActions);
            if (showActions && nonContextualActions != null && (min = Math.min(nonContextualActions.size(), 3)) > 0) {
                z = true;
                for (int i2 = 0; i2 < min; i2++) {
                    applyStandardTemplate.addView(R.id.actions, generateActionButton(nonContextualActions.get(i2)));
                }
            }
            if (!z) {
                i = 8;
            }
            applyStandardTemplate.setViewVisibility(R.id.actions, i);
            applyStandardTemplate.setViewVisibility(R.id.action_divider, i);
            buildIntoRemoteViews(applyStandardTemplate, innerView);
            return applyStandardTemplate;
        }

        private RemoteViews generateActionButton(Action action) {
            boolean z = action.actionIntent == null;
            RemoteViews remoteViews = new RemoteViews(this.mBuilder.mContext.getPackageName(), z ? R.layout.notification_action_tombstone : R.layout.notification_action);
            IconCompat iconCompat = action.getIconCompat();
            if (iconCompat != null) {
                remoteViews.setImageViewBitmap(R.id.action_image, createColoredBitmap(iconCompat, this.mBuilder.mContext.getResources().getColor(R.color.notification_action_color_filter)));
            }
            remoteViews.setTextViewText(R.id.action_text, action.title);
            if (!z) {
                remoteViews.setOnClickPendingIntent(R.id.action_container, action.actionIntent);
            }
            if (Build.VERSION.SDK_INT >= 15) {
                remoteViews.setContentDescription(R.id.action_container, action.title);
            }
            return remoteViews;
        }

        private static List<Action> getNonContextualActions(List<Action> list) {
            if (list == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (Action next : list) {
                if (!next.isContextual()) {
                    arrayList.add(next);
                }
            }
            return arrayList;
        }

        public void apply(NotificationBuilderWithBuilderAccessor builder) {
            if (Build.VERSION.SDK_INT >= 24) {
                builder.getBuilder().setStyle(new Notification.DecoratedCustomViewStyle());
            }
        }

        public boolean displayCustomViewInline() {
            return true;
        }

        /* access modifiers changed from: protected */
        public String getClassName() {
            return TEMPLATE_CLASS_NAME;
        }

        public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor builder) {
            if (Build.VERSION.SDK_INT >= 24) {
                return null;
            }
            RemoteViews bigContentView = this.mBuilder.getBigContentView();
            RemoteViews contentView = bigContentView != null ? bigContentView : this.mBuilder.getContentView();
            if (contentView == null) {
                return null;
            }
            return createRemoteViews(contentView, true);
        }

        public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor builder) {
            if (Build.VERSION.SDK_INT < 24 && this.mBuilder.getContentView() != null) {
                return createRemoteViews(this.mBuilder.getContentView(), false);
            }
            return null;
        }

        public RemoteViews makeHeadsUpContentView(NotificationBuilderWithBuilderAccessor builder) {
            if (Build.VERSION.SDK_INT >= 24) {
                return null;
            }
            RemoteViews headsUpContentView = this.mBuilder.getHeadsUpContentView();
            RemoteViews contentView = headsUpContentView != null ? headsUpContentView : this.mBuilder.getContentView();
            if (headsUpContentView == null) {
                return null;
            }
            return createRemoteViews(contentView, true);
        }
    }

    public interface Extender {
        Builder extend(Builder builder);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface GroupAlertBehavior {
    }

    public static class InboxStyle extends Style {
        private static final String TEMPLATE_CLASS_NAME = "androidx.core.app.NotificationCompat$InboxStyle";
        private ArrayList<CharSequence> mTexts = new ArrayList<>();

        public InboxStyle() {
        }

        public InboxStyle(Builder builder) {
            setBuilder(builder);
        }

        public InboxStyle addLine(CharSequence cs) {
            if (cs != null) {
                this.mTexts.add(Builder.limitCharSequenceLength(cs));
            }
            return this;
        }

        public void apply(NotificationBuilderWithBuilderAccessor builder) {
            if (Build.VERSION.SDK_INT >= 16) {
                Notification.InboxStyle bigContentTitle = new Notification.InboxStyle(builder.getBuilder()).setBigContentTitle(this.mBigContentTitle);
                if (this.mSummaryTextSet) {
                    bigContentTitle.setSummaryText(this.mSummaryText);
                }
                Iterator<CharSequence> it = this.mTexts.iterator();
                while (it.hasNext()) {
                    bigContentTitle.addLine(it.next());
                }
            }
        }

        /* access modifiers changed from: protected */
        public void clearCompatExtraKeys(Bundle extras) {
            super.clearCompatExtraKeys(extras);
            extras.remove(NotificationCompat.EXTRA_TEXT_LINES);
        }

        /* access modifiers changed from: protected */
        public String getClassName() {
            return TEMPLATE_CLASS_NAME;
        }

        /* access modifiers changed from: protected */
        public void restoreFromCompatExtras(Bundle extras) {
            super.restoreFromCompatExtras(extras);
            this.mTexts.clear();
            if (extras.containsKey(NotificationCompat.EXTRA_TEXT_LINES)) {
                Collections.addAll(this.mTexts, extras.getCharSequenceArray(NotificationCompat.EXTRA_TEXT_LINES));
            }
        }

        public InboxStyle setBigContentTitle(CharSequence title) {
            this.mBigContentTitle = Builder.limitCharSequenceLength(title);
            return this;
        }

        public InboxStyle setSummaryText(CharSequence cs) {
            this.mSummaryText = Builder.limitCharSequenceLength(cs);
            this.mSummaryTextSet = true;
            return this;
        }
    }

    public static class MessagingStyle extends Style {
        public static final int MAXIMUM_RETAINED_MESSAGES = 25;
        private static final String TEMPLATE_CLASS_NAME = "androidx.core.app.NotificationCompat$MessagingStyle";
        private CharSequence mConversationTitle;
        private final List<Message> mHistoricMessages = new ArrayList();
        private Boolean mIsGroupConversation;
        private final List<Message> mMessages = new ArrayList();
        private Person mUser;

        public static final class Message {
            static final String KEY_DATA_MIME_TYPE = "type";
            static final String KEY_DATA_URI = "uri";
            static final String KEY_EXTRAS_BUNDLE = "extras";
            static final String KEY_NOTIFICATION_PERSON = "sender_person";
            static final String KEY_PERSON = "person";
            static final String KEY_SENDER = "sender";
            static final String KEY_TEXT = "text";
            static final String KEY_TIMESTAMP = "time";
            private String mDataMimeType;
            private Uri mDataUri;
            private Bundle mExtras;
            private final Person mPerson;
            private final CharSequence mText;
            private final long mTimestamp;

            public Message(CharSequence text, long timestamp, Person person) {
                this.mExtras = new Bundle();
                this.mText = text;
                this.mTimestamp = timestamp;
                this.mPerson = person;
            }

            @Deprecated
            public Message(CharSequence text, long timestamp, CharSequence sender) {
                this(text, timestamp, new Person.Builder().setName(sender).build());
            }

            static Bundle[] getBundleArrayForMessages(List<Message> list) {
                Bundle[] bundleArr = new Bundle[list.size()];
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    bundleArr[i] = list.get(i).toBundle();
                }
                return bundleArr;
            }

            static Message getMessageFromBundle(Bundle bundle) {
                try {
                    if (bundle.containsKey(KEY_TEXT)) {
                        if (bundle.containsKey(KEY_TIMESTAMP)) {
                            Person person = null;
                            if (bundle.containsKey(KEY_PERSON)) {
                                person = Person.fromBundle(bundle.getBundle(KEY_PERSON));
                            } else if (bundle.containsKey(KEY_NOTIFICATION_PERSON) && Build.VERSION.SDK_INT >= 28) {
                                person = Person.fromAndroidPerson((android.app.Person) bundle.getParcelable(KEY_NOTIFICATION_PERSON));
                            } else if (bundle.containsKey(KEY_SENDER)) {
                                person = new Person.Builder().setName(bundle.getCharSequence(KEY_SENDER)).build();
                            }
                            Message message = new Message(bundle.getCharSequence(KEY_TEXT), bundle.getLong(KEY_TIMESTAMP), person);
                            if (bundle.containsKey(KEY_DATA_MIME_TYPE) && bundle.containsKey(KEY_DATA_URI)) {
                                message.setData(bundle.getString(KEY_DATA_MIME_TYPE), (Uri) bundle.getParcelable(KEY_DATA_URI));
                            }
                            if (bundle.containsKey(KEY_EXTRAS_BUNDLE)) {
                                message.getExtras().putAll(bundle.getBundle(KEY_EXTRAS_BUNDLE));
                            }
                            return message;
                        }
                    }
                    return null;
                } catch (ClassCastException e) {
                    return null;
                }
            }

            static List<Message> getMessagesFromBundleArray(Parcelable[] bundles) {
                Message messageFromBundle;
                ArrayList arrayList = new ArrayList(bundles.length);
                for (int i = 0; i < bundles.length; i++) {
                    if ((bundles[i] instanceof Bundle) && (messageFromBundle = getMessageFromBundle(bundles[i])) != null) {
                        arrayList.add(messageFromBundle);
                    }
                }
                return arrayList;
            }

            private Bundle toBundle() {
                Bundle bundle = new Bundle();
                CharSequence charSequence = this.mText;
                if (charSequence != null) {
                    bundle.putCharSequence(KEY_TEXT, charSequence);
                }
                bundle.putLong(KEY_TIMESTAMP, this.mTimestamp);
                Person person = this.mPerson;
                if (person != null) {
                    bundle.putCharSequence(KEY_SENDER, person.getName());
                    if (Build.VERSION.SDK_INT >= 28) {
                        bundle.putParcelable(KEY_NOTIFICATION_PERSON, this.mPerson.toAndroidPerson());
                    } else {
                        bundle.putBundle(KEY_PERSON, this.mPerson.toBundle());
                    }
                }
                String str = this.mDataMimeType;
                if (str != null) {
                    bundle.putString(KEY_DATA_MIME_TYPE, str);
                }
                Uri uri = this.mDataUri;
                if (uri != null) {
                    bundle.putParcelable(KEY_DATA_URI, uri);
                }
                Bundle bundle2 = this.mExtras;
                if (bundle2 != null) {
                    bundle.putBundle(KEY_EXTRAS_BUNDLE, bundle2);
                }
                return bundle;
            }

            public String getDataMimeType() {
                return this.mDataMimeType;
            }

            public Uri getDataUri() {
                return this.mDataUri;
            }

            public Bundle getExtras() {
                return this.mExtras;
            }

            public Person getPerson() {
                return this.mPerson;
            }

            @Deprecated
            public CharSequence getSender() {
                Person person = this.mPerson;
                if (person == null) {
                    return null;
                }
                return person.getName();
            }

            public CharSequence getText() {
                return this.mText;
            }

            public long getTimestamp() {
                return this.mTimestamp;
            }

            public Message setData(String dataMimeType, Uri dataUri) {
                this.mDataMimeType = dataMimeType;
                this.mDataUri = dataUri;
                return this;
            }

            /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v5, resolved type: android.app.Person} */
            /* JADX WARNING: type inference failed for: r2v0 */
            /* JADX WARNING: type inference failed for: r2v3, types: [java.lang.CharSequence] */
            /* JADX WARNING: type inference failed for: r2v7 */
            /* JADX WARNING: type inference failed for: r2v8 */
            /* access modifiers changed from: package-private */
            /* JADX WARNING: Multi-variable type inference failed */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public android.app.Notification.MessagingStyle.Message toAndroidMessage() {
                /*
                    r6 = this;
                    androidx.core.app.Person r0 = r6.getPerson()
                    int r1 = android.os.Build.VERSION.SDK_INT
                    r2 = 0
                    r3 = 28
                    if (r1 < r3) goto L_0x0020
                    android.app.Notification$MessagingStyle$Message r1 = new android.app.Notification$MessagingStyle$Message
                    java.lang.CharSequence r3 = r6.getText()
                    long r4 = r6.getTimestamp()
                    if (r0 != 0) goto L_0x0018
                    goto L_0x001c
                L_0x0018:
                    android.app.Person r2 = r0.toAndroidPerson()
                L_0x001c:
                    r1.<init>(r3, r4, r2)
                    goto L_0x0034
                L_0x0020:
                    android.app.Notification$MessagingStyle$Message r1 = new android.app.Notification$MessagingStyle$Message
                    java.lang.CharSequence r3 = r6.getText()
                    long r4 = r6.getTimestamp()
                    if (r0 != 0) goto L_0x002d
                    goto L_0x0031
                L_0x002d:
                    java.lang.CharSequence r2 = r0.getName()
                L_0x0031:
                    r1.<init>(r3, r4, r2)
                L_0x0034:
                    java.lang.String r2 = r6.getDataMimeType()
                    if (r2 == 0) goto L_0x0045
                    java.lang.String r2 = r6.getDataMimeType()
                    android.net.Uri r3 = r6.getDataUri()
                    r1.setData(r2, r3)
                L_0x0045:
                    return r1
                */
                throw new UnsupportedOperationException("Method not decompiled: androidx.core.app.NotificationCompat.MessagingStyle.Message.toAndroidMessage():android.app.Notification$MessagingStyle$Message");
            }
        }

        MessagingStyle() {
        }

        public MessagingStyle(Person user) {
            if (!TextUtils.isEmpty(user.getName())) {
                this.mUser = user;
                return;
            }
            throw new IllegalArgumentException("User's name must not be empty.");
        }

        @Deprecated
        public MessagingStyle(CharSequence userDisplayName) {
            this.mUser = new Person.Builder().setName(userDisplayName).build();
        }

        public static MessagingStyle extractMessagingStyleFromNotification(Notification notification) {
            Style extractStyleFromNotification = Style.extractStyleFromNotification(notification);
            if (extractStyleFromNotification instanceof MessagingStyle) {
                return (MessagingStyle) extractStyleFromNotification;
            }
            return null;
        }

        private Message findLatestIncomingMessage() {
            for (int size = this.mMessages.size() - 1; size >= 0; size--) {
                Message message = this.mMessages.get(size);
                if (message.getPerson() != null && !TextUtils.isEmpty(message.getPerson().getName())) {
                    return message;
                }
            }
            if (this.mMessages.isEmpty()) {
                return null;
            }
            List<Message> list = this.mMessages;
            return list.get(list.size() - 1);
        }

        private boolean hasMessagesWithoutSender() {
            for (int size = this.mMessages.size() - 1; size >= 0; size--) {
                Message message = this.mMessages.get(size);
                if (message.getPerson() != null && message.getPerson().getName() == null) {
                    return true;
                }
            }
            return false;
        }

        private TextAppearanceSpan makeFontColorSpan(int color) {
            return new TextAppearanceSpan((String) null, 0, 0, ColorStateList.valueOf(color), (ColorStateList) null);
        }

        private CharSequence makeMessageLine(Message message) {
            BidiFormatter instance = BidiFormatter.getInstance();
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            boolean z = Build.VERSION.SDK_INT >= 21;
            int i = z ? ViewCompat.MEASURED_STATE_MASK : -1;
            Person person = message.getPerson();
            CharSequence charSequence = HttpUrl.FRAGMENT_ENCODE_SET;
            CharSequence name = person == null ? charSequence : message.getPerson().getName();
            if (TextUtils.isEmpty(name)) {
                name = this.mUser.getName();
                i = (!z || this.mBuilder.getColor() == 0) ? i : this.mBuilder.getColor();
            }
            CharSequence unicodeWrap = instance.unicodeWrap(name);
            spannableStringBuilder.append(unicodeWrap);
            spannableStringBuilder.setSpan(makeFontColorSpan(i), spannableStringBuilder.length() - unicodeWrap.length(), spannableStringBuilder.length(), 33);
            if (message.getText() != null) {
                charSequence = message.getText();
            }
            spannableStringBuilder.append("  ").append(instance.unicodeWrap(charSequence));
            return spannableStringBuilder;
        }

        public void addCompatExtras(Bundle extras) {
            super.addCompatExtras(extras);
            extras.putCharSequence(NotificationCompat.EXTRA_SELF_DISPLAY_NAME, this.mUser.getName());
            extras.putBundle(NotificationCompat.EXTRA_MESSAGING_STYLE_USER, this.mUser.toBundle());
            extras.putCharSequence(NotificationCompat.EXTRA_HIDDEN_CONVERSATION_TITLE, this.mConversationTitle);
            if (this.mConversationTitle != null && this.mIsGroupConversation.booleanValue()) {
                extras.putCharSequence(NotificationCompat.EXTRA_CONVERSATION_TITLE, this.mConversationTitle);
            }
            if (!this.mMessages.isEmpty()) {
                extras.putParcelableArray(NotificationCompat.EXTRA_MESSAGES, Message.getBundleArrayForMessages(this.mMessages));
            }
            if (!this.mHistoricMessages.isEmpty()) {
                extras.putParcelableArray(NotificationCompat.EXTRA_HISTORIC_MESSAGES, Message.getBundleArrayForMessages(this.mHistoricMessages));
            }
            Boolean bool = this.mIsGroupConversation;
            if (bool != null) {
                extras.putBoolean(NotificationCompat.EXTRA_IS_GROUP_CONVERSATION, bool.booleanValue());
            }
        }

        public MessagingStyle addHistoricMessage(Message message) {
            if (message != null) {
                this.mHistoricMessages.add(message);
                if (this.mHistoricMessages.size() > 25) {
                    this.mHistoricMessages.remove(0);
                }
            }
            return this;
        }

        public MessagingStyle addMessage(Message message) {
            if (message != null) {
                this.mMessages.add(message);
                if (this.mMessages.size() > 25) {
                    this.mMessages.remove(0);
                }
            }
            return this;
        }

        public MessagingStyle addMessage(CharSequence text, long timestamp, Person person) {
            addMessage(new Message(text, timestamp, person));
            return this;
        }

        @Deprecated
        public MessagingStyle addMessage(CharSequence text, long timestamp, CharSequence sender) {
            this.mMessages.add(new Message(text, timestamp, new Person.Builder().setName(sender).build()));
            if (this.mMessages.size() > 25) {
                this.mMessages.remove(0);
            }
            return this;
        }

        public void apply(NotificationBuilderWithBuilderAccessor builder) {
            setGroupConversation(isGroupConversation());
            if (Build.VERSION.SDK_INT >= 24) {
                Notification.MessagingStyle messagingStyle = Build.VERSION.SDK_INT >= 28 ? new Notification.MessagingStyle(this.mUser.toAndroidPerson()) : new Notification.MessagingStyle(this.mUser.getName());
                for (Message androidMessage : this.mMessages) {
                    messagingStyle.addMessage(androidMessage.toAndroidMessage());
                }
                if (Build.VERSION.SDK_INT >= 26) {
                    for (Message androidMessage2 : this.mHistoricMessages) {
                        messagingStyle.addHistoricMessage(androidMessage2.toAndroidMessage());
                    }
                }
                if (this.mIsGroupConversation.booleanValue() || Build.VERSION.SDK_INT >= 28) {
                    messagingStyle.setConversationTitle(this.mConversationTitle);
                }
                if (Build.VERSION.SDK_INT >= 28) {
                    messagingStyle.setGroupConversation(this.mIsGroupConversation.booleanValue());
                }
                messagingStyle.setBuilder(builder.getBuilder());
                return;
            }
            Message findLatestIncomingMessage = findLatestIncomingMessage();
            if (this.mConversationTitle != null && this.mIsGroupConversation.booleanValue()) {
                builder.getBuilder().setContentTitle(this.mConversationTitle);
            } else if (findLatestIncomingMessage != null) {
                builder.getBuilder().setContentTitle(HttpUrl.FRAGMENT_ENCODE_SET);
                if (findLatestIncomingMessage.getPerson() != null) {
                    builder.getBuilder().setContentTitle(findLatestIncomingMessage.getPerson().getName());
                }
            }
            if (findLatestIncomingMessage != null) {
                builder.getBuilder().setContentText(this.mConversationTitle != null ? makeMessageLine(findLatestIncomingMessage) : findLatestIncomingMessage.getText());
            }
            if (Build.VERSION.SDK_INT >= 16) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                boolean z = this.mConversationTitle != null || hasMessagesWithoutSender();
                for (int size = this.mMessages.size() - 1; size >= 0; size--) {
                    Message message = this.mMessages.get(size);
                    CharSequence makeMessageLine = z ? makeMessageLine(message) : message.getText();
                    if (size != this.mMessages.size() - 1) {
                        spannableStringBuilder.insert(0, "\n");
                    }
                    spannableStringBuilder.insert(0, makeMessageLine);
                }
                new Notification.BigTextStyle(builder.getBuilder()).setBigContentTitle((CharSequence) null).bigText(spannableStringBuilder);
            }
        }

        /* access modifiers changed from: protected */
        public void clearCompatExtraKeys(Bundle extras) {
            super.clearCompatExtraKeys(extras);
            extras.remove(NotificationCompat.EXTRA_MESSAGING_STYLE_USER);
            extras.remove(NotificationCompat.EXTRA_SELF_DISPLAY_NAME);
            extras.remove(NotificationCompat.EXTRA_CONVERSATION_TITLE);
            extras.remove(NotificationCompat.EXTRA_HIDDEN_CONVERSATION_TITLE);
            extras.remove(NotificationCompat.EXTRA_MESSAGES);
            extras.remove(NotificationCompat.EXTRA_HISTORIC_MESSAGES);
            extras.remove(NotificationCompat.EXTRA_IS_GROUP_CONVERSATION);
        }

        /* access modifiers changed from: protected */
        public String getClassName() {
            return TEMPLATE_CLASS_NAME;
        }

        public CharSequence getConversationTitle() {
            return this.mConversationTitle;
        }

        public List<Message> getHistoricMessages() {
            return this.mHistoricMessages;
        }

        public List<Message> getMessages() {
            return this.mMessages;
        }

        public Person getUser() {
            return this.mUser;
        }

        @Deprecated
        public CharSequence getUserDisplayName() {
            return this.mUser.getName();
        }

        public boolean isGroupConversation() {
            if (this.mBuilder != null && this.mBuilder.mContext.getApplicationInfo().targetSdkVersion < 28 && this.mIsGroupConversation == null) {
                return this.mConversationTitle != null;
            }
            Boolean bool = this.mIsGroupConversation;
            if (bool != null) {
                return bool.booleanValue();
            }
            return false;
        }

        /* access modifiers changed from: protected */
        public void restoreFromCompatExtras(Bundle extras) {
            super.restoreFromCompatExtras(extras);
            this.mMessages.clear();
            if (extras.containsKey(NotificationCompat.EXTRA_MESSAGING_STYLE_USER)) {
                this.mUser = Person.fromBundle(extras.getBundle(NotificationCompat.EXTRA_MESSAGING_STYLE_USER));
            } else {
                this.mUser = new Person.Builder().setName(extras.getString(NotificationCompat.EXTRA_SELF_DISPLAY_NAME)).build();
            }
            CharSequence charSequence = extras.getCharSequence(NotificationCompat.EXTRA_CONVERSATION_TITLE);
            this.mConversationTitle = charSequence;
            if (charSequence == null) {
                this.mConversationTitle = extras.getCharSequence(NotificationCompat.EXTRA_HIDDEN_CONVERSATION_TITLE);
            }
            Parcelable[] parcelableArray = extras.getParcelableArray(NotificationCompat.EXTRA_MESSAGES);
            if (parcelableArray != null) {
                this.mMessages.addAll(Message.getMessagesFromBundleArray(parcelableArray));
            }
            Parcelable[] parcelableArray2 = extras.getParcelableArray(NotificationCompat.EXTRA_HISTORIC_MESSAGES);
            if (parcelableArray2 != null) {
                this.mHistoricMessages.addAll(Message.getMessagesFromBundleArray(parcelableArray2));
            }
            if (extras.containsKey(NotificationCompat.EXTRA_IS_GROUP_CONVERSATION)) {
                this.mIsGroupConversation = Boolean.valueOf(extras.getBoolean(NotificationCompat.EXTRA_IS_GROUP_CONVERSATION));
            }
        }

        public MessagingStyle setConversationTitle(CharSequence conversationTitle) {
            this.mConversationTitle = conversationTitle;
            return this;
        }

        public MessagingStyle setGroupConversation(boolean isGroupConversation) {
            this.mIsGroupConversation = Boolean.valueOf(isGroupConversation);
            return this;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NotificationVisibility {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ServiceNotificationBehavior {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface StreamType {
    }

    public static abstract class Style {
        CharSequence mBigContentTitle;
        protected Builder mBuilder;
        CharSequence mSummaryText;
        boolean mSummaryTextSet = false;

        private int calculateTopPadding() {
            Resources resources = this.mBuilder.mContext.getResources();
            int dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.notification_top_pad);
            int dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.notification_top_pad_large_text);
            float constrain = (constrain(resources.getConfiguration().fontScale, 1.0f, 1.3f) - 1.0f) / 0.29999995f;
            return Math.round(((1.0f - constrain) * ((float) dimensionPixelSize)) + (((float) dimensionPixelSize2) * constrain));
        }

        private static float constrain(float amount, float low, float high) {
            return amount < low ? low : amount > high ? high : amount;
        }

        static Style constructCompatStyleByName(String templateClass) {
            if (templateClass == null) {
                return null;
            }
            char c = 65535;
            switch (templateClass.hashCode()) {
                case -716705180:
                    if (templateClass.equals("androidx.core.app.NotificationCompat$DecoratedCustomViewStyle")) {
                        c = 3;
                        break;
                    }
                    break;
                case -171946061:
                    if (templateClass.equals("androidx.core.app.NotificationCompat$BigPictureStyle")) {
                        c = 1;
                        break;
                    }
                    break;
                case 912942987:
                    if (templateClass.equals("androidx.core.app.NotificationCompat$InboxStyle")) {
                        c = 2;
                        break;
                    }
                    break;
                case 919595044:
                    if (templateClass.equals("androidx.core.app.NotificationCompat$BigTextStyle")) {
                        c = 0;
                        break;
                    }
                    break;
                case 2090799565:
                    if (templateClass.equals("androidx.core.app.NotificationCompat$MessagingStyle")) {
                        c = 4;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    return new BigTextStyle();
                case 1:
                    return new BigPictureStyle();
                case 2:
                    return new InboxStyle();
                case 3:
                    return new DecoratedCustomViewStyle();
                case 4:
                    return new MessagingStyle();
                default:
                    return null;
            }
        }

        private static Style constructCompatStyleByPlatformName(String platformTemplateClass) {
            if (platformTemplateClass != null && Build.VERSION.SDK_INT >= 16) {
                if (platformTemplateClass.equals(Notification.BigPictureStyle.class.getName())) {
                    return new BigPictureStyle();
                }
                if (platformTemplateClass.equals(Notification.BigTextStyle.class.getName())) {
                    return new BigTextStyle();
                }
                if (platformTemplateClass.equals(Notification.InboxStyle.class.getName())) {
                    return new InboxStyle();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    if (platformTemplateClass.equals(Notification.MessagingStyle.class.getName())) {
                        return new MessagingStyle();
                    }
                    if (platformTemplateClass.equals(Notification.DecoratedCustomViewStyle.class.getName())) {
                        return new DecoratedCustomViewStyle();
                    }
                }
            }
            return null;
        }

        static Style constructCompatStyleForBundle(Bundle extras) {
            Style constructCompatStyleByName = constructCompatStyleByName(extras.getString(NotificationCompat.EXTRA_COMPAT_TEMPLATE));
            return constructCompatStyleByName != null ? constructCompatStyleByName : (extras.containsKey(NotificationCompat.EXTRA_SELF_DISPLAY_NAME) || extras.containsKey(NotificationCompat.EXTRA_MESSAGING_STYLE_USER)) ? new MessagingStyle() : extras.containsKey(NotificationCompat.EXTRA_PICTURE) ? new BigPictureStyle() : extras.containsKey(NotificationCompat.EXTRA_BIG_TEXT) ? new BigTextStyle() : extras.containsKey(NotificationCompat.EXTRA_TEXT_LINES) ? new InboxStyle() : constructCompatStyleByPlatformName(extras.getString(NotificationCompat.EXTRA_TEMPLATE));
        }

        static Style constructStyleForExtras(Bundle extras) {
            Style constructCompatStyleForBundle = constructCompatStyleForBundle(extras);
            if (constructCompatStyleForBundle == null) {
                return null;
            }
            try {
                constructCompatStyleForBundle.restoreFromCompatExtras(extras);
                return constructCompatStyleForBundle;
            } catch (ClassCastException e) {
                return null;
            }
        }

        private Bitmap createColoredBitmap(int iconId, int color, int size) {
            return createColoredBitmap(IconCompat.createWithResource(this.mBuilder.mContext, iconId), color, size);
        }

        private Bitmap createColoredBitmap(IconCompat icon, int color, int size) {
            Drawable loadDrawable = icon.loadDrawable(this.mBuilder.mContext);
            int intrinsicWidth = size == 0 ? loadDrawable.getIntrinsicWidth() : size;
            int intrinsicHeight = size == 0 ? loadDrawable.getIntrinsicHeight() : size;
            Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888);
            loadDrawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
            if (color != 0) {
                loadDrawable.mutate().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
            loadDrawable.draw(new Canvas(createBitmap));
            return createBitmap;
        }

        private Bitmap createIconWithBackground(int iconId, int size, int iconSize, int color) {
            Bitmap createColoredBitmap = createColoredBitmap(R.drawable.notification_icon_background, color == 0 ? 0 : color, size);
            Canvas canvas = new Canvas(createColoredBitmap);
            Drawable mutate = this.mBuilder.mContext.getResources().getDrawable(iconId).mutate();
            mutate.setFilterBitmap(true);
            int i = (size - iconSize) / 2;
            mutate.setBounds(i, i, iconSize + i, iconSize + i);
            mutate.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_ATOP));
            mutate.draw(canvas);
            return createColoredBitmap;
        }

        public static Style extractStyleFromNotification(Notification notification) {
            Bundle extras = NotificationCompat.getExtras(notification);
            if (extras == null) {
                return null;
            }
            return constructStyleForExtras(extras);
        }

        private void hideNormalContent(RemoteViews outerView) {
            outerView.setViewVisibility(R.id.title, 8);
            outerView.setViewVisibility(R.id.text2, 8);
            outerView.setViewVisibility(R.id.text, 8);
        }

        public void addCompatExtras(Bundle extras) {
            if (this.mSummaryTextSet) {
                extras.putCharSequence(NotificationCompat.EXTRA_SUMMARY_TEXT, this.mSummaryText);
            }
            CharSequence charSequence = this.mBigContentTitle;
            if (charSequence != null) {
                extras.putCharSequence(NotificationCompat.EXTRA_TITLE_BIG, charSequence);
            }
            String className = getClassName();
            if (className != null) {
                extras.putString(NotificationCompat.EXTRA_COMPAT_TEMPLATE, className);
            }
        }

        public void apply(NotificationBuilderWithBuilderAccessor builder) {
        }

        /* JADX WARNING: Removed duplicated region for block: B:69:0x01c8  */
        /* JADX WARNING: Removed duplicated region for block: B:73:0x01ea  */
        /* JADX WARNING: Removed duplicated region for block: B:86:0x0249  */
        /* JADX WARNING: Removed duplicated region for block: B:87:0x024b  */
        /* JADX WARNING: Removed duplicated region for block: B:90:0x0254  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public android.widget.RemoteViews applyStandardTemplate(boolean r20, int r21, boolean r22) {
            /*
                r19 = this;
                r0 = r19
                androidx.core.app.NotificationCompat$Builder r1 = r0.mBuilder
                android.content.Context r1 = r1.mContext
                android.content.res.Resources r1 = r1.getResources()
                android.widget.RemoteViews r2 = new android.widget.RemoteViews
                androidx.core.app.NotificationCompat$Builder r3 = r0.mBuilder
                android.content.Context r3 = r3.mContext
                java.lang.String r3 = r3.getPackageName()
                r4 = r21
                r2.<init>(r3, r4)
                r3 = 0
                r5 = 0
                androidx.core.app.NotificationCompat$Builder r6 = r0.mBuilder
                int r6 = r6.getPriority()
                r7 = -1
                r12 = 0
                if (r6 >= r7) goto L_0x0027
                r6 = 1
                goto L_0x0028
            L_0x0027:
                r6 = r12
            L_0x0028:
                r13 = r6
                int r6 = android.os.Build.VERSION.SDK_INT
                r8 = 21
                r14 = 16
                if (r6 < r14) goto L_0x0057
                int r6 = android.os.Build.VERSION.SDK_INT
                if (r6 >= r8) goto L_0x0057
                java.lang.String r6 = "setBackgroundResource"
                if (r13 == 0) goto L_0x0049
                int r9 = androidx.core.R.id.notification_background
                int r10 = androidx.core.R.drawable.notification_bg_low
                r2.setInt(r9, r6, r10)
                int r9 = androidx.core.R.id.icon
                int r10 = androidx.core.R.drawable.notification_template_icon_low_bg
                r2.setInt(r9, r6, r10)
                goto L_0x0057
            L_0x0049:
                int r9 = androidx.core.R.id.notification_background
                int r10 = androidx.core.R.drawable.notification_bg
                r2.setInt(r9, r6, r10)
                int r9 = androidx.core.R.id.icon
                int r10 = androidx.core.R.drawable.notification_template_icon_bg
                r2.setInt(r9, r6, r10)
            L_0x0057:
                androidx.core.app.NotificationCompat$Builder r6 = r0.mBuilder
                android.graphics.Bitmap r6 = r6.mLargeIcon
                r15 = 8
                if (r6 == 0) goto L_0x00c0
                int r6 = android.os.Build.VERSION.SDK_INT
                if (r6 < r14) goto L_0x0072
                int r6 = androidx.core.R.id.icon
                r2.setViewVisibility(r6, r12)
                int r6 = androidx.core.R.id.icon
                androidx.core.app.NotificationCompat$Builder r9 = r0.mBuilder
                android.graphics.Bitmap r9 = r9.mLargeIcon
                r2.setImageViewBitmap(r6, r9)
                goto L_0x0077
            L_0x0072:
                int r6 = androidx.core.R.id.icon
                r2.setViewVisibility(r6, r15)
            L_0x0077:
                if (r20 == 0) goto L_0x010b
                androidx.core.app.NotificationCompat$Builder r6 = r0.mBuilder
                android.app.Notification r6 = r6.mNotification
                int r6 = r6.icon
                if (r6 == 0) goto L_0x010b
                int r6 = androidx.core.R.dimen.notification_right_icon_size
                int r6 = r1.getDimensionPixelSize(r6)
                int r9 = androidx.core.R.dimen.notification_small_icon_background_padding
                int r9 = r1.getDimensionPixelSize(r9)
                int r9 = r9 * 2
                int r9 = r6 - r9
                int r10 = android.os.Build.VERSION.SDK_INT
                if (r10 < r8) goto L_0x00ab
                androidx.core.app.NotificationCompat$Builder r7 = r0.mBuilder
                android.app.Notification r7 = r7.mNotification
                int r7 = r7.icon
                androidx.core.app.NotificationCompat$Builder r10 = r0.mBuilder
                int r10 = r10.getColor()
                android.graphics.Bitmap r7 = r0.createIconWithBackground(r7, r6, r9, r10)
                int r10 = androidx.core.R.id.right_icon
                r2.setImageViewBitmap(r10, r7)
                goto L_0x00ba
            L_0x00ab:
                int r10 = androidx.core.R.id.right_icon
                androidx.core.app.NotificationCompat$Builder r11 = r0.mBuilder
                android.app.Notification r11 = r11.mNotification
                int r11 = r11.icon
                android.graphics.Bitmap r7 = r0.createColoredBitmap((int) r11, (int) r7)
                r2.setImageViewBitmap(r10, r7)
            L_0x00ba:
                int r7 = androidx.core.R.id.right_icon
                r2.setViewVisibility(r7, r12)
                goto L_0x010b
            L_0x00c0:
                if (r20 == 0) goto L_0x010b
                androidx.core.app.NotificationCompat$Builder r6 = r0.mBuilder
                android.app.Notification r6 = r6.mNotification
                int r6 = r6.icon
                if (r6 == 0) goto L_0x010b
                int r6 = androidx.core.R.id.icon
                r2.setViewVisibility(r6, r12)
                int r6 = android.os.Build.VERSION.SDK_INT
                if (r6 < r8) goto L_0x00fc
                int r6 = androidx.core.R.dimen.notification_large_icon_width
                int r6 = r1.getDimensionPixelSize(r6)
                int r7 = androidx.core.R.dimen.notification_big_circle_margin
                int r7 = r1.getDimensionPixelSize(r7)
                int r6 = r6 - r7
                int r7 = androidx.core.R.dimen.notification_small_icon_size_as_large
                int r7 = r1.getDimensionPixelSize(r7)
                androidx.core.app.NotificationCompat$Builder r9 = r0.mBuilder
                android.app.Notification r9 = r9.mNotification
                int r9 = r9.icon
                androidx.core.app.NotificationCompat$Builder r10 = r0.mBuilder
                int r10 = r10.getColor()
                android.graphics.Bitmap r9 = r0.createIconWithBackground(r9, r6, r7, r10)
                int r10 = androidx.core.R.id.icon
                r2.setImageViewBitmap(r10, r9)
                goto L_0x010b
            L_0x00fc:
                int r6 = androidx.core.R.id.icon
                androidx.core.app.NotificationCompat$Builder r9 = r0.mBuilder
                android.app.Notification r9 = r9.mNotification
                int r9 = r9.icon
                android.graphics.Bitmap r7 = r0.createColoredBitmap((int) r9, (int) r7)
                r2.setImageViewBitmap(r6, r7)
            L_0x010b:
                androidx.core.app.NotificationCompat$Builder r6 = r0.mBuilder
                java.lang.CharSequence r6 = r6.mContentTitle
                if (r6 == 0) goto L_0x011a
                int r6 = androidx.core.R.id.title
                androidx.core.app.NotificationCompat$Builder r7 = r0.mBuilder
                java.lang.CharSequence r7 = r7.mContentTitle
                r2.setTextViewText(r6, r7)
            L_0x011a:
                androidx.core.app.NotificationCompat$Builder r6 = r0.mBuilder
                java.lang.CharSequence r6 = r6.mContentText
                if (r6 == 0) goto L_0x012a
                int r6 = androidx.core.R.id.text
                androidx.core.app.NotificationCompat$Builder r7 = r0.mBuilder
                java.lang.CharSequence r7 = r7.mContentText
                r2.setTextViewText(r6, r7)
                r3 = 1
            L_0x012a:
                int r6 = android.os.Build.VERSION.SDK_INT
                if (r6 >= r8) goto L_0x0136
                androidx.core.app.NotificationCompat$Builder r6 = r0.mBuilder
                android.graphics.Bitmap r6 = r6.mLargeIcon
                if (r6 == 0) goto L_0x0136
                r6 = 1
                goto L_0x0137
            L_0x0136:
                r6 = r12
            L_0x0137:
                androidx.core.app.NotificationCompat$Builder r7 = r0.mBuilder
                java.lang.CharSequence r7 = r7.mContentInfo
                if (r7 == 0) goto L_0x014f
                int r7 = androidx.core.R.id.info
                androidx.core.app.NotificationCompat$Builder r8 = r0.mBuilder
                java.lang.CharSequence r8 = r8.mContentInfo
                r2.setTextViewText(r7, r8)
                int r7 = androidx.core.R.id.info
                r2.setViewVisibility(r7, r12)
                r3 = 1
                r6 = 1
                r11 = r6
                goto L_0x018e
            L_0x014f:
                androidx.core.app.NotificationCompat$Builder r7 = r0.mBuilder
                int r7 = r7.mNumber
                if (r7 <= 0) goto L_0x0188
                int r7 = androidx.core.R.integer.status_bar_notification_info_maxnum
                int r7 = r1.getInteger(r7)
                androidx.core.app.NotificationCompat$Builder r8 = r0.mBuilder
                int r8 = r8.mNumber
                if (r8 <= r7) goto L_0x016d
                int r8 = androidx.core.R.id.info
                int r9 = androidx.core.R.string.status_bar_notification_info_overflow
                java.lang.String r9 = r1.getString(r9)
                r2.setTextViewText(r8, r9)
                goto L_0x017f
            L_0x016d:
                java.text.NumberFormat r8 = java.text.NumberFormat.getIntegerInstance()
                int r9 = androidx.core.R.id.info
                androidx.core.app.NotificationCompat$Builder r10 = r0.mBuilder
                int r10 = r10.mNumber
                long r10 = (long) r10
                java.lang.String r10 = r8.format(r10)
                r2.setTextViewText(r9, r10)
            L_0x017f:
                int r8 = androidx.core.R.id.info
                r2.setViewVisibility(r8, r12)
                r3 = 1
                r6 = 1
                r11 = r6
                goto L_0x018e
            L_0x0188:
                int r7 = androidx.core.R.id.info
                r2.setViewVisibility(r7, r15)
                r11 = r6
            L_0x018e:
                androidx.core.app.NotificationCompat$Builder r6 = r0.mBuilder
                java.lang.CharSequence r6 = r6.mSubText
                if (r6 == 0) goto L_0x01be
                int r6 = android.os.Build.VERSION.SDK_INT
                if (r6 < r14) goto L_0x01be
                int r6 = androidx.core.R.id.text
                androidx.core.app.NotificationCompat$Builder r7 = r0.mBuilder
                java.lang.CharSequence r7 = r7.mSubText
                r2.setTextViewText(r6, r7)
                androidx.core.app.NotificationCompat$Builder r6 = r0.mBuilder
                java.lang.CharSequence r6 = r6.mContentText
                if (r6 == 0) goto L_0x01b9
                int r6 = androidx.core.R.id.text2
                androidx.core.app.NotificationCompat$Builder r7 = r0.mBuilder
                java.lang.CharSequence r7 = r7.mContentText
                r2.setTextViewText(r6, r7)
                int r6 = androidx.core.R.id.text2
                r2.setViewVisibility(r6, r12)
                r5 = 1
                r16 = r5
                goto L_0x01c0
            L_0x01b9:
                int r6 = androidx.core.R.id.text2
                r2.setViewVisibility(r6, r15)
            L_0x01be:
                r16 = r5
            L_0x01c0:
                if (r16 == 0) goto L_0x01de
                int r5 = android.os.Build.VERSION.SDK_INT
                if (r5 < r14) goto L_0x01de
                if (r22 == 0) goto L_0x01d4
                int r5 = androidx.core.R.dimen.notification_subtext_size
                int r5 = r1.getDimensionPixelSize(r5)
                float r5 = (float) r5
                int r6 = androidx.core.R.id.text
                r2.setTextViewTextSize(r6, r12, r5)
            L_0x01d4:
                int r6 = androidx.core.R.id.line1
                r7 = 0
                r8 = 0
                r9 = 0
                r10 = 0
                r5 = r2
                r5.setViewPadding(r6, r7, r8, r9, r10)
            L_0x01de:
                androidx.core.app.NotificationCompat$Builder r5 = r0.mBuilder
                long r5 = r5.getWhenIfShowing()
                r7 = 0
                int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
                if (r5 == 0) goto L_0x0245
                androidx.core.app.NotificationCompat$Builder r5 = r0.mBuilder
                boolean r5 = r5.mUseChronometer
                if (r5 == 0) goto L_0x0231
                int r5 = android.os.Build.VERSION.SDK_INT
                if (r5 < r14) goto L_0x0231
                int r5 = androidx.core.R.id.chronometer
                r2.setViewVisibility(r5, r12)
                int r5 = androidx.core.R.id.chronometer
                androidx.core.app.NotificationCompat$Builder r6 = r0.mBuilder
                long r6 = r6.getWhenIfShowing()
                long r8 = android.os.SystemClock.elapsedRealtime()
                long r17 = java.lang.System.currentTimeMillis()
                long r8 = r8 - r17
                long r6 = r6 + r8
                java.lang.String r8 = "setBase"
                r2.setLong(r5, r8, r6)
                int r5 = androidx.core.R.id.chronometer
                java.lang.String r6 = "setStarted"
                r7 = 1
                r2.setBoolean(r5, r6, r7)
                androidx.core.app.NotificationCompat$Builder r5 = r0.mBuilder
                boolean r5 = r5.mChronometerCountDown
                if (r5 == 0) goto L_0x0244
                int r5 = android.os.Build.VERSION.SDK_INT
                r6 = 24
                if (r5 < r6) goto L_0x0244
                int r5 = androidx.core.R.id.chronometer
                androidx.core.app.NotificationCompat$Builder r6 = r0.mBuilder
                boolean r6 = r6.mChronometerCountDown
                r2.setChronometerCountDown(r5, r6)
                goto L_0x0244
            L_0x0231:
                int r5 = androidx.core.R.id.time
                r2.setViewVisibility(r5, r12)
                int r5 = androidx.core.R.id.time
                androidx.core.app.NotificationCompat$Builder r6 = r0.mBuilder
                long r6 = r6.getWhenIfShowing()
                java.lang.String r8 = "setTime"
                r2.setLong(r5, r8, r6)
            L_0x0244:
                r11 = 1
            L_0x0245:
                int r5 = androidx.core.R.id.right_side
                if (r11 == 0) goto L_0x024b
                r6 = r12
                goto L_0x024c
            L_0x024b:
                r6 = r15
            L_0x024c:
                r2.setViewVisibility(r5, r6)
                int r5 = androidx.core.R.id.line3
                if (r3 == 0) goto L_0x0254
                goto L_0x0255
            L_0x0254:
                r12 = r15
            L_0x0255:
                r2.setViewVisibility(r5, r12)
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.core.app.NotificationCompat.Style.applyStandardTemplate(boolean, int, boolean):android.widget.RemoteViews");
        }

        public Notification build() {
            Builder builder = this.mBuilder;
            if (builder != null) {
                return builder.build();
            }
            return null;
        }

        public void buildIntoRemoteViews(RemoteViews outerView, RemoteViews innerView) {
            hideNormalContent(outerView);
            outerView.removeAllViews(R.id.notification_main_column);
            outerView.addView(R.id.notification_main_column, innerView.clone());
            outerView.setViewVisibility(R.id.notification_main_column, 0);
            if (Build.VERSION.SDK_INT >= 21) {
                outerView.setViewPadding(R.id.notification_main_column_container, 0, calculateTopPadding(), 0, 0);
            }
        }

        /* access modifiers changed from: protected */
        public void clearCompatExtraKeys(Bundle extras) {
            extras.remove(NotificationCompat.EXTRA_SUMMARY_TEXT);
            extras.remove(NotificationCompat.EXTRA_TITLE_BIG);
            extras.remove(NotificationCompat.EXTRA_COMPAT_TEMPLATE);
        }

        public Bitmap createColoredBitmap(int iconId, int color) {
            return createColoredBitmap(iconId, color, 0);
        }

        /* access modifiers changed from: package-private */
        public Bitmap createColoredBitmap(IconCompat icon, int color) {
            return createColoredBitmap(icon, color, 0);
        }

        public boolean displayCustomViewInline() {
            return false;
        }

        /* access modifiers changed from: protected */
        public String getClassName() {
            return null;
        }

        public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor builder) {
            return null;
        }

        public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor builder) {
            return null;
        }

        public RemoteViews makeHeadsUpContentView(NotificationBuilderWithBuilderAccessor builder) {
            return null;
        }

        /* access modifiers changed from: protected */
        public void restoreFromCompatExtras(Bundle extras) {
            if (extras.containsKey(NotificationCompat.EXTRA_SUMMARY_TEXT)) {
                this.mSummaryText = extras.getCharSequence(NotificationCompat.EXTRA_SUMMARY_TEXT);
                this.mSummaryTextSet = true;
            }
            this.mBigContentTitle = extras.getCharSequence(NotificationCompat.EXTRA_TITLE_BIG);
        }

        public void setBuilder(Builder builder) {
            if (this.mBuilder != builder) {
                this.mBuilder = builder;
                if (builder != null) {
                    builder.setStyle(this);
                }
            }
        }
    }

    public static final class WearableExtender implements Extender {
        private static final int DEFAULT_CONTENT_ICON_GRAVITY = 8388613;
        private static final int DEFAULT_FLAGS = 1;
        private static final int DEFAULT_GRAVITY = 80;
        private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
        private static final int FLAG_BIG_PICTURE_AMBIENT = 32;
        private static final int FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE = 1;
        private static final int FLAG_HINT_AVOID_BACKGROUND_CLIPPING = 16;
        private static final int FLAG_HINT_CONTENT_INTENT_LAUNCHES_ACTIVITY = 64;
        private static final int FLAG_HINT_HIDE_ICON = 2;
        private static final int FLAG_HINT_SHOW_BACKGROUND_ONLY = 4;
        private static final int FLAG_START_SCROLL_BOTTOM = 8;
        private static final String KEY_ACTIONS = "actions";
        private static final String KEY_BACKGROUND = "background";
        private static final String KEY_BRIDGE_TAG = "bridgeTag";
        private static final String KEY_CONTENT_ACTION_INDEX = "contentActionIndex";
        private static final String KEY_CONTENT_ICON = "contentIcon";
        private static final String KEY_CONTENT_ICON_GRAVITY = "contentIconGravity";
        private static final String KEY_CUSTOM_CONTENT_HEIGHT = "customContentHeight";
        private static final String KEY_CUSTOM_SIZE_PRESET = "customSizePreset";
        private static final String KEY_DISMISSAL_ID = "dismissalId";
        private static final String KEY_DISPLAY_INTENT = "displayIntent";
        private static final String KEY_FLAGS = "flags";
        private static final String KEY_GRAVITY = "gravity";
        private static final String KEY_HINT_SCREEN_TIMEOUT = "hintScreenTimeout";
        private static final String KEY_PAGES = "pages";
        @Deprecated
        public static final int SCREEN_TIMEOUT_LONG = -1;
        @Deprecated
        public static final int SCREEN_TIMEOUT_SHORT = 0;
        @Deprecated
        public static final int SIZE_DEFAULT = 0;
        @Deprecated
        public static final int SIZE_FULL_SCREEN = 5;
        @Deprecated
        public static final int SIZE_LARGE = 4;
        @Deprecated
        public static final int SIZE_MEDIUM = 3;
        @Deprecated
        public static final int SIZE_SMALL = 2;
        @Deprecated
        public static final int SIZE_XSMALL = 1;
        public static final int UNSET_ACTION_INDEX = -1;
        private ArrayList<Action> mActions = new ArrayList<>();
        private Bitmap mBackground;
        private String mBridgeTag;
        private int mContentActionIndex = -1;
        private int mContentIcon;
        private int mContentIconGravity = 8388613;
        private int mCustomContentHeight;
        private int mCustomSizePreset = 0;
        private String mDismissalId;
        private PendingIntent mDisplayIntent;
        private int mFlags = 1;
        private int mGravity = DEFAULT_GRAVITY;
        private int mHintScreenTimeout;
        private ArrayList<Notification> mPages = new ArrayList<>();

        public WearableExtender() {
        }

        public WearableExtender(Notification notification) {
            Bundle extras = NotificationCompat.getExtras(notification);
            Bundle bundle = extras != null ? extras.getBundle(EXTRA_WEARABLE_EXTENSIONS) : null;
            if (bundle != null) {
                ArrayList parcelableArrayList = bundle.getParcelableArrayList(KEY_ACTIONS);
                if (Build.VERSION.SDK_INT >= 16 && parcelableArrayList != null) {
                    Action[] actionArr = new Action[parcelableArrayList.size()];
                    for (int i = 0; i < actionArr.length; i++) {
                        if (Build.VERSION.SDK_INT >= 20) {
                            actionArr[i] = NotificationCompat.getActionCompatFromAction((Notification.Action) parcelableArrayList.get(i));
                        } else if (Build.VERSION.SDK_INT >= 16) {
                            actionArr[i] = NotificationCompatJellybean.getActionFromBundle((Bundle) parcelableArrayList.get(i));
                        }
                    }
                    Collections.addAll(this.mActions, actionArr);
                }
                this.mFlags = bundle.getInt(KEY_FLAGS, 1);
                this.mDisplayIntent = (PendingIntent) bundle.getParcelable(KEY_DISPLAY_INTENT);
                Notification[] notificationArrayFromBundle = NotificationCompat.getNotificationArrayFromBundle(bundle, KEY_PAGES);
                if (notificationArrayFromBundle != null) {
                    Collections.addAll(this.mPages, notificationArrayFromBundle);
                }
                this.mBackground = (Bitmap) bundle.getParcelable(KEY_BACKGROUND);
                this.mContentIcon = bundle.getInt(KEY_CONTENT_ICON);
                this.mContentIconGravity = bundle.getInt(KEY_CONTENT_ICON_GRAVITY, 8388613);
                this.mContentActionIndex = bundle.getInt(KEY_CONTENT_ACTION_INDEX, -1);
                this.mCustomSizePreset = bundle.getInt(KEY_CUSTOM_SIZE_PRESET, 0);
                this.mCustomContentHeight = bundle.getInt(KEY_CUSTOM_CONTENT_HEIGHT);
                this.mGravity = bundle.getInt(KEY_GRAVITY, DEFAULT_GRAVITY);
                this.mHintScreenTimeout = bundle.getInt(KEY_HINT_SCREEN_TIMEOUT);
                this.mDismissalId = bundle.getString(KEY_DISMISSAL_ID);
                this.mBridgeTag = bundle.getString(KEY_BRIDGE_TAG);
            }
        }

        private static Notification.Action getActionFromActionCompat(Action actionCompat) {
            Notification.Action.Builder builder;
            if (Build.VERSION.SDK_INT >= 23) {
                IconCompat iconCompat = actionCompat.getIconCompat();
                builder = new Notification.Action.Builder(iconCompat == null ? null : iconCompat.toIcon(), actionCompat.getTitle(), actionCompat.getActionIntent());
            } else {
                IconCompat iconCompat2 = actionCompat.getIconCompat();
                int i = 0;
                if (iconCompat2 != null && iconCompat2.getType() == 2) {
                    i = iconCompat2.getResId();
                }
                builder = new Notification.Action.Builder(i, actionCompat.getTitle(), actionCompat.getActionIntent());
            }
            Bundle bundle = actionCompat.getExtras() != null ? new Bundle(actionCompat.getExtras()) : new Bundle();
            bundle.putBoolean("android.support.allowGeneratedReplies", actionCompat.getAllowGeneratedReplies());
            if (Build.VERSION.SDK_INT >= 24) {
                builder.setAllowGeneratedReplies(actionCompat.getAllowGeneratedReplies());
            }
            if (Build.VERSION.SDK_INT >= 31) {
                builder.setAuthenticationRequired(actionCompat.isAuthenticationRequired());
            }
            builder.addExtras(bundle);
            RemoteInput[] remoteInputs = actionCompat.getRemoteInputs();
            if (remoteInputs != null) {
                for (RemoteInput addRemoteInput : RemoteInput.fromCompat(remoteInputs)) {
                    builder.addRemoteInput(addRemoteInput);
                }
            }
            return builder.build();
        }

        private void setFlag(int mask, boolean value) {
            if (value) {
                this.mFlags |= mask;
            } else {
                this.mFlags &= ~mask;
            }
        }

        public WearableExtender addAction(Action action) {
            this.mActions.add(action);
            return this;
        }

        public WearableExtender addActions(List<Action> list) {
            this.mActions.addAll(list);
            return this;
        }

        @Deprecated
        public WearableExtender addPage(Notification page) {
            this.mPages.add(page);
            return this;
        }

        @Deprecated
        public WearableExtender addPages(List<Notification> list) {
            this.mPages.addAll(list);
            return this;
        }

        public WearableExtender clearActions() {
            this.mActions.clear();
            return this;
        }

        @Deprecated
        public WearableExtender clearPages() {
            this.mPages.clear();
            return this;
        }

        public WearableExtender clone() {
            WearableExtender wearableExtender = new WearableExtender();
            wearableExtender.mActions = new ArrayList<>(this.mActions);
            wearableExtender.mFlags = this.mFlags;
            wearableExtender.mDisplayIntent = this.mDisplayIntent;
            wearableExtender.mPages = new ArrayList<>(this.mPages);
            wearableExtender.mBackground = this.mBackground;
            wearableExtender.mContentIcon = this.mContentIcon;
            wearableExtender.mContentIconGravity = this.mContentIconGravity;
            wearableExtender.mContentActionIndex = this.mContentActionIndex;
            wearableExtender.mCustomSizePreset = this.mCustomSizePreset;
            wearableExtender.mCustomContentHeight = this.mCustomContentHeight;
            wearableExtender.mGravity = this.mGravity;
            wearableExtender.mHintScreenTimeout = this.mHintScreenTimeout;
            wearableExtender.mDismissalId = this.mDismissalId;
            wearableExtender.mBridgeTag = this.mBridgeTag;
            return wearableExtender;
        }

        public Builder extend(Builder builder) {
            Bundle bundle = new Bundle();
            if (!this.mActions.isEmpty()) {
                if (Build.VERSION.SDK_INT >= 16) {
                    ArrayList arrayList = new ArrayList(this.mActions.size());
                    Iterator<Action> it = this.mActions.iterator();
                    while (it.hasNext()) {
                        Action next = it.next();
                        if (Build.VERSION.SDK_INT >= 20) {
                            arrayList.add(getActionFromActionCompat(next));
                        } else if (Build.VERSION.SDK_INT >= 16) {
                            arrayList.add(NotificationCompatJellybean.getBundleForAction(next));
                        }
                    }
                    bundle.putParcelableArrayList(KEY_ACTIONS, arrayList);
                } else {
                    bundle.putParcelableArrayList(KEY_ACTIONS, (ArrayList) null);
                }
            }
            int i = this.mFlags;
            if (i != 1) {
                bundle.putInt(KEY_FLAGS, i);
            }
            PendingIntent pendingIntent = this.mDisplayIntent;
            if (pendingIntent != null) {
                bundle.putParcelable(KEY_DISPLAY_INTENT, pendingIntent);
            }
            if (!this.mPages.isEmpty()) {
                ArrayList<Notification> arrayList2 = this.mPages;
                bundle.putParcelableArray(KEY_PAGES, (Parcelable[]) arrayList2.toArray(new Notification[arrayList2.size()]));
            }
            Bitmap bitmap = this.mBackground;
            if (bitmap != null) {
                bundle.putParcelable(KEY_BACKGROUND, bitmap);
            }
            int i2 = this.mContentIcon;
            if (i2 != 0) {
                bundle.putInt(KEY_CONTENT_ICON, i2);
            }
            int i3 = this.mContentIconGravity;
            if (i3 != 8388613) {
                bundle.putInt(KEY_CONTENT_ICON_GRAVITY, i3);
            }
            int i4 = this.mContentActionIndex;
            if (i4 != -1) {
                bundle.putInt(KEY_CONTENT_ACTION_INDEX, i4);
            }
            int i5 = this.mCustomSizePreset;
            if (i5 != 0) {
                bundle.putInt(KEY_CUSTOM_SIZE_PRESET, i5);
            }
            int i6 = this.mCustomContentHeight;
            if (i6 != 0) {
                bundle.putInt(KEY_CUSTOM_CONTENT_HEIGHT, i6);
            }
            int i7 = this.mGravity;
            if (i7 != DEFAULT_GRAVITY) {
                bundle.putInt(KEY_GRAVITY, i7);
            }
            int i8 = this.mHintScreenTimeout;
            if (i8 != 0) {
                bundle.putInt(KEY_HINT_SCREEN_TIMEOUT, i8);
            }
            String str = this.mDismissalId;
            if (str != null) {
                bundle.putString(KEY_DISMISSAL_ID, str);
            }
            String str2 = this.mBridgeTag;
            if (str2 != null) {
                bundle.putString(KEY_BRIDGE_TAG, str2);
            }
            builder.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, bundle);
            return builder;
        }

        public List<Action> getActions() {
            return this.mActions;
        }

        @Deprecated
        public Bitmap getBackground() {
            return this.mBackground;
        }

        public String getBridgeTag() {
            return this.mBridgeTag;
        }

        public int getContentAction() {
            return this.mContentActionIndex;
        }

        @Deprecated
        public int getContentIcon() {
            return this.mContentIcon;
        }

        @Deprecated
        public int getContentIconGravity() {
            return this.mContentIconGravity;
        }

        public boolean getContentIntentAvailableOffline() {
            return (this.mFlags & 1) != 0;
        }

        @Deprecated
        public int getCustomContentHeight() {
            return this.mCustomContentHeight;
        }

        @Deprecated
        public int getCustomSizePreset() {
            return this.mCustomSizePreset;
        }

        public String getDismissalId() {
            return this.mDismissalId;
        }

        @Deprecated
        public PendingIntent getDisplayIntent() {
            return this.mDisplayIntent;
        }

        @Deprecated
        public int getGravity() {
            return this.mGravity;
        }

        @Deprecated
        public boolean getHintAmbientBigPicture() {
            return (this.mFlags & 32) != 0;
        }

        @Deprecated
        public boolean getHintAvoidBackgroundClipping() {
            return (this.mFlags & 16) != 0;
        }

        public boolean getHintContentIntentLaunchesActivity() {
            return (this.mFlags & 64) != 0;
        }

        @Deprecated
        public boolean getHintHideIcon() {
            return (this.mFlags & 2) != 0;
        }

        @Deprecated
        public int getHintScreenTimeout() {
            return this.mHintScreenTimeout;
        }

        @Deprecated
        public boolean getHintShowBackgroundOnly() {
            return (this.mFlags & 4) != 0;
        }

        @Deprecated
        public List<Notification> getPages() {
            return this.mPages;
        }

        public boolean getStartScrollBottom() {
            return (this.mFlags & 8) != 0;
        }

        @Deprecated
        public WearableExtender setBackground(Bitmap background) {
            this.mBackground = background;
            return this;
        }

        public WearableExtender setBridgeTag(String bridgeTag) {
            this.mBridgeTag = bridgeTag;
            return this;
        }

        public WearableExtender setContentAction(int actionIndex) {
            this.mContentActionIndex = actionIndex;
            return this;
        }

        @Deprecated
        public WearableExtender setContentIcon(int icon) {
            this.mContentIcon = icon;
            return this;
        }

        @Deprecated
        public WearableExtender setContentIconGravity(int contentIconGravity) {
            this.mContentIconGravity = contentIconGravity;
            return this;
        }

        public WearableExtender setContentIntentAvailableOffline(boolean contentIntentAvailableOffline) {
            setFlag(1, contentIntentAvailableOffline);
            return this;
        }

        @Deprecated
        public WearableExtender setCustomContentHeight(int height) {
            this.mCustomContentHeight = height;
            return this;
        }

        @Deprecated
        public WearableExtender setCustomSizePreset(int sizePreset) {
            this.mCustomSizePreset = sizePreset;
            return this;
        }

        public WearableExtender setDismissalId(String dismissalId) {
            this.mDismissalId = dismissalId;
            return this;
        }

        @Deprecated
        public WearableExtender setDisplayIntent(PendingIntent intent) {
            this.mDisplayIntent = intent;
            return this;
        }

        @Deprecated
        public WearableExtender setGravity(int gravity) {
            this.mGravity = gravity;
            return this;
        }

        @Deprecated
        public WearableExtender setHintAmbientBigPicture(boolean hintAmbientBigPicture) {
            setFlag(32, hintAmbientBigPicture);
            return this;
        }

        @Deprecated
        public WearableExtender setHintAvoidBackgroundClipping(boolean hintAvoidBackgroundClipping) {
            setFlag(16, hintAvoidBackgroundClipping);
            return this;
        }

        public WearableExtender setHintContentIntentLaunchesActivity(boolean hintContentIntentLaunchesActivity) {
            setFlag(64, hintContentIntentLaunchesActivity);
            return this;
        }

        @Deprecated
        public WearableExtender setHintHideIcon(boolean hintHideIcon) {
            setFlag(2, hintHideIcon);
            return this;
        }

        @Deprecated
        public WearableExtender setHintScreenTimeout(int timeout) {
            this.mHintScreenTimeout = timeout;
            return this;
        }

        @Deprecated
        public WearableExtender setHintShowBackgroundOnly(boolean hintShowBackgroundOnly) {
            setFlag(4, hintShowBackgroundOnly);
            return this;
        }

        public WearableExtender setStartScrollBottom(boolean startScrollBottom) {
            setFlag(8, startScrollBottom);
            return this;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v3, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: android.os.Bundle} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static androidx.core.app.NotificationCompat.Action getAction(android.app.Notification r6, int r7) {
        /*
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 20
            if (r0 < r1) goto L_0x000f
            android.app.Notification$Action[] r0 = r6.actions
            r0 = r0[r7]
            androidx.core.app.NotificationCompat$Action r0 = getActionCompatFromAction(r0)
            return r0
        L_0x000f:
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 19
            if (r0 < r1) goto L_0x0036
            android.app.Notification$Action[] r0 = r6.actions
            r0 = r0[r7]
            r1 = 0
            android.os.Bundle r2 = r6.extras
            java.lang.String r3 = "android.support.actionExtras"
            android.util.SparseArray r2 = r2.getSparseParcelableArray(r3)
            if (r2 == 0) goto L_0x002b
            java.lang.Object r3 = r2.get(r7)
            r1 = r3
            android.os.Bundle r1 = (android.os.Bundle) r1
        L_0x002b:
            int r3 = r0.icon
            java.lang.CharSequence r4 = r0.title
            android.app.PendingIntent r5 = r0.actionIntent
            androidx.core.app.NotificationCompat$Action r3 = androidx.core.app.NotificationCompatJellybean.readAction(r3, r4, r5, r1)
            return r3
        L_0x0036:
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 16
            if (r0 < r1) goto L_0x0041
            androidx.core.app.NotificationCompat$Action r0 = androidx.core.app.NotificationCompatJellybean.getAction(r6, r7)
            return r0
        L_0x0041:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.app.NotificationCompat.getAction(android.app.Notification, int):androidx.core.app.NotificationCompat$Action");
    }

    static Action getActionCompatFromAction(Notification.Action action) {
        RemoteInput[] remoteInputArr;
        Notification.Action action2 = action;
        RemoteInput[] remoteInputs = action.getRemoteInputs();
        boolean z = false;
        if (remoteInputs == null) {
            remoteInputArr = null;
        } else {
            remoteInputArr = new RemoteInput[remoteInputs.length];
            for (int i = 0; i < remoteInputs.length; i++) {
                RemoteInput remoteInput = remoteInputs[i];
                remoteInputArr[i] = new RemoteInput(remoteInput.getResultKey(), remoteInput.getLabel(), remoteInput.getChoices(), remoteInput.getAllowFreeFormInput(), Build.VERSION.SDK_INT >= 29 ? remoteInput.getEditChoicesBeforeSending() : 0, remoteInput.getExtras(), (Set<String>) null);
            }
        }
        boolean z2 = Build.VERSION.SDK_INT >= 24 ? action.getExtras().getBoolean("android.support.allowGeneratedReplies") || action.getAllowGeneratedReplies() : action.getExtras().getBoolean("android.support.allowGeneratedReplies");
        boolean z3 = action.getExtras().getBoolean("android.support.action.showsUserInterface", true);
        int semanticAction = Build.VERSION.SDK_INT >= 28 ? action.getSemanticAction() : action.getExtras().getInt("android.support.action.semanticAction", 0);
        boolean isContextual = Build.VERSION.SDK_INT >= 29 ? action.isContextual() : false;
        if (Build.VERSION.SDK_INT >= 31) {
            z = action.isAuthenticationRequired();
        }
        boolean z4 = z;
        if (Build.VERSION.SDK_INT < 23) {
            return new Action(action2.icon, action2.title, action2.actionIntent, action.getExtras(), remoteInputArr, (RemoteInput[]) null, z2, semanticAction, z3, isContextual, z4);
        } else if (action.getIcon() == null && action2.icon != 0) {
            return new Action(action2.icon, action2.title, action2.actionIntent, action.getExtras(), remoteInputArr, (RemoteInput[]) null, z2, semanticAction, z3, isContextual, z4);
        } else {
            return new Action(action.getIcon() == null ? null : IconCompat.createFromIconOrNullIfZeroResId(action.getIcon()), action2.title, action2.actionIntent, action.getExtras(), remoteInputArr, (RemoteInput[]) null, z2, semanticAction, z3, isContextual, z4);
        }
    }

    public static int getActionCount(Notification notification) {
        if (Build.VERSION.SDK_INT >= 19) {
            if (notification.actions != null) {
                return notification.actions.length;
            }
            return 0;
        } else if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getActionCount(notification);
        } else {
            return 0;
        }
    }

    public static boolean getAllowSystemGeneratedContextualActions(Notification notification) {
        if (Build.VERSION.SDK_INT >= 29) {
            return notification.getAllowSystemGeneratedContextualActions();
        }
        return false;
    }

    public static boolean getAutoCancel(Notification notification) {
        return (notification.flags & 16) != 0;
    }

    public static int getBadgeIconType(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getBadgeIconType();
        }
        return 0;
    }

    public static BubbleMetadata getBubbleMetadata(Notification notification) {
        if (Build.VERSION.SDK_INT >= 29) {
            return BubbleMetadata.fromPlatform(notification.getBubbleMetadata());
        }
        return null;
    }

    public static String getCategory(Notification notification) {
        if (Build.VERSION.SDK_INT >= 21) {
            return notification.category;
        }
        return null;
    }

    public static String getChannelId(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getChannelId();
        }
        return null;
    }

    public static int getColor(Notification notification) {
        if (Build.VERSION.SDK_INT >= 21) {
            return notification.color;
        }
        return 0;
    }

    public static CharSequence getContentInfo(Notification notification) {
        return notification.extras.getCharSequence(EXTRA_INFO_TEXT);
    }

    public static CharSequence getContentText(Notification notification) {
        return notification.extras.getCharSequence(EXTRA_TEXT);
    }

    public static CharSequence getContentTitle(Notification notification) {
        return notification.extras.getCharSequence(EXTRA_TITLE);
    }

    public static Bundle getExtras(Notification notification) {
        if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification);
        }
        return null;
    }

    public static String getGroup(Notification notification) {
        if (Build.VERSION.SDK_INT >= 20) {
            return notification.getGroup();
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras.getString(NotificationCompatExtras.EXTRA_GROUP_KEY);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification).getString(NotificationCompatExtras.EXTRA_GROUP_KEY);
        }
        return null;
    }

    public static int getGroupAlertBehavior(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getGroupAlertBehavior();
        }
        return 0;
    }

    static boolean getHighPriority(Notification notification) {
        return (notification.flags & 128) != 0;
    }

    public static List<Action> getInvisibleActions(Notification notification) {
        Bundle bundle;
        Bundle bundle2;
        ArrayList arrayList = new ArrayList();
        if (!(Build.VERSION.SDK_INT < 19 || (bundle = notification.extras.getBundle("android.car.EXTENSIONS")) == null || (bundle2 = bundle.getBundle("invisible_actions")) == null)) {
            for (int i = 0; i < bundle2.size(); i++) {
                String num = Integer.toString(i);
                Log1F380D.a((Object) num);
                arrayList.add(NotificationCompatJellybean.getActionFromBundle(bundle2.getBundle(num)));
            }
        }
        return arrayList;
    }

    public static boolean getLocalOnly(Notification notification) {
        if (Build.VERSION.SDK_INT >= 20) {
            return (notification.flags & 256) != 0;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras.getBoolean(NotificationCompatExtras.EXTRA_LOCAL_ONLY);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification).getBoolean(NotificationCompatExtras.EXTRA_LOCAL_ONLY);
        }
        return false;
    }

    public static LocusIdCompat getLocusId(Notification notification) {
        LocusId locusId;
        if (Build.VERSION.SDK_INT < 29 || (locusId = notification.getLocusId()) == null) {
            return null;
        }
        return LocusIdCompat.toLocusIdCompat(locusId);
    }

    static Notification[] getNotificationArrayFromBundle(Bundle bundle, String key) {
        Parcelable[] parcelableArray = bundle.getParcelableArray(key);
        if ((parcelableArray instanceof Notification[]) || parcelableArray == null) {
            return (Notification[]) parcelableArray;
        }
        Notification[] notificationArr = new Notification[parcelableArray.length];
        for (int i = 0; i < parcelableArray.length; i++) {
            notificationArr[i] = (Notification) parcelableArray[i];
        }
        bundle.putParcelableArray(key, notificationArr);
        return notificationArr;
    }

    public static boolean getOngoing(Notification notification) {
        return (notification.flags & 2) != 0;
    }

    public static boolean getOnlyAlertOnce(Notification notification) {
        return (notification.flags & 8) != 0;
    }

    public static List<Person> getPeople(Notification notification) {
        String[] stringArray;
        ArrayList arrayList = new ArrayList();
        if (Build.VERSION.SDK_INT >= 28) {
            ArrayList parcelableArrayList = notification.extras.getParcelableArrayList(EXTRA_PEOPLE_LIST);
            if (parcelableArrayList != null && !parcelableArrayList.isEmpty()) {
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    arrayList.add(Person.fromAndroidPerson((android.app.Person) it.next()));
                }
            }
        } else if (!(Build.VERSION.SDK_INT < 19 || (stringArray = notification.extras.getStringArray(EXTRA_PEOPLE)) == null || stringArray.length == 0)) {
            for (String uri : stringArray) {
                arrayList.add(new Person.Builder().setUri(uri).build());
            }
        }
        return arrayList;
    }

    public static Notification getPublicVersion(Notification notification) {
        if (Build.VERSION.SDK_INT >= 21) {
            return notification.publicVersion;
        }
        return null;
    }

    public static CharSequence getSettingsText(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getSettingsText();
        }
        return null;
    }

    public static String getShortcutId(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getShortcutId();
        }
        return null;
    }

    public static boolean getShowWhen(Notification notification) {
        return notification.extras.getBoolean(EXTRA_SHOW_WHEN);
    }

    public static String getSortKey(Notification notification) {
        if (Build.VERSION.SDK_INT >= 20) {
            return notification.getSortKey();
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras.getString(NotificationCompatExtras.EXTRA_SORT_KEY);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification).getString(NotificationCompatExtras.EXTRA_SORT_KEY);
        }
        return null;
    }

    public static CharSequence getSubText(Notification notification) {
        return notification.extras.getCharSequence(EXTRA_SUB_TEXT);
    }

    public static long getTimeoutAfter(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getTimeoutAfter();
        }
        return 0;
    }

    public static boolean getUsesChronometer(Notification notification) {
        return notification.extras.getBoolean(EXTRA_SHOW_CHRONOMETER);
    }

    public static int getVisibility(Notification notification) {
        if (Build.VERSION.SDK_INT >= 21) {
            return notification.visibility;
        }
        return 0;
    }

    public static boolean isGroupSummary(Notification notification) {
        if (Build.VERSION.SDK_INT >= 20) {
            return (notification.flags & 512) != 0;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras.getBoolean(NotificationCompatExtras.EXTRA_GROUP_SUMMARY);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification).getBoolean(NotificationCompatExtras.EXTRA_GROUP_SUMMARY);
        }
        return false;
    }
}
