package androidx.core.app;

import android.app.PendingIntent;
import android.app.RemoteAction;
import android.graphics.drawable.Icon;
import android.os.Build;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.util.Preconditions;
import androidx.versionedparcelable.VersionedParcelable;

public final class RemoteActionCompat implements VersionedParcelable {
    public PendingIntent mActionIntent;
    public CharSequence mContentDescription;
    public boolean mEnabled;
    public IconCompat mIcon;
    public boolean mShouldShowIcon;
    public CharSequence mTitle;

    static class Api26Impl {
        private Api26Impl() {
        }

        static RemoteAction createRemoteAction(Icon icon, CharSequence title, CharSequence contentDescription, PendingIntent intent) {
            return new RemoteAction(icon, title, contentDescription, intent);
        }

        static PendingIntent getActionIntent(RemoteAction remoteAction) {
            return remoteAction.getActionIntent();
        }

        static CharSequence getContentDescription(RemoteAction remoteAction) {
            return remoteAction.getContentDescription();
        }

        static Icon getIcon(RemoteAction remoteAction) {
            return remoteAction.getIcon();
        }

        static CharSequence getTitle(RemoteAction remoteAction) {
            return remoteAction.getTitle();
        }

        static boolean isEnabled(RemoteAction remoteAction) {
            return remoteAction.isEnabled();
        }

        static void setEnabled(RemoteAction remoteAction, boolean enabled) {
            remoteAction.setEnabled(enabled);
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static void setShouldShowIcon(RemoteAction remoteAction, boolean shouldShowIcon) {
            remoteAction.setShouldShowIcon(shouldShowIcon);
        }

        static boolean shouldShowIcon(RemoteAction remoteAction) {
            return remoteAction.shouldShowIcon();
        }
    }

    public RemoteActionCompat() {
    }

    public RemoteActionCompat(RemoteActionCompat other) {
        Preconditions.checkNotNull(other);
        this.mIcon = other.mIcon;
        this.mTitle = other.mTitle;
        this.mContentDescription = other.mContentDescription;
        this.mActionIntent = other.mActionIntent;
        this.mEnabled = other.mEnabled;
        this.mShouldShowIcon = other.mShouldShowIcon;
    }

    public RemoteActionCompat(IconCompat icon, CharSequence title, CharSequence contentDescription, PendingIntent intent) {
        this.mIcon = (IconCompat) Preconditions.checkNotNull(icon);
        this.mTitle = (CharSequence) Preconditions.checkNotNull(title);
        this.mContentDescription = (CharSequence) Preconditions.checkNotNull(contentDescription);
        this.mActionIntent = (PendingIntent) Preconditions.checkNotNull(intent);
        this.mEnabled = true;
        this.mShouldShowIcon = true;
    }

    public static RemoteActionCompat createFromRemoteAction(RemoteAction remoteAction) {
        Preconditions.checkNotNull(remoteAction);
        RemoteActionCompat remoteActionCompat = new RemoteActionCompat(IconCompat.createFromIcon(Api26Impl.getIcon(remoteAction)), Api26Impl.getTitle(remoteAction), Api26Impl.getContentDescription(remoteAction), Api26Impl.getActionIntent(remoteAction));
        remoteActionCompat.setEnabled(Api26Impl.isEnabled(remoteAction));
        if (Build.VERSION.SDK_INT >= 28) {
            remoteActionCompat.setShouldShowIcon(Api28Impl.shouldShowIcon(remoteAction));
        }
        return remoteActionCompat;
    }

    public PendingIntent getActionIntent() {
        return this.mActionIntent;
    }

    public CharSequence getContentDescription() {
        return this.mContentDescription;
    }

    public IconCompat getIcon() {
        return this.mIcon;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    public void setShouldShowIcon(boolean shouldShowIcon) {
        this.mShouldShowIcon = shouldShowIcon;
    }

    public boolean shouldShowIcon() {
        return this.mShouldShowIcon;
    }

    public RemoteAction toRemoteAction() {
        RemoteAction createRemoteAction = Api26Impl.createRemoteAction(this.mIcon.toIcon(), this.mTitle, this.mContentDescription, this.mActionIntent);
        Api26Impl.setEnabled(createRemoteAction, isEnabled());
        if (Build.VERSION.SDK_INT >= 28) {
            Api28Impl.setShouldShowIcon(createRemoteAction, shouldShowIcon());
        }
        return createRemoteAction;
    }
}
