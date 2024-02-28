package androidx.core.app;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.os.Build;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationChannelGroupCompat {
    private boolean mBlocked;
    private List<NotificationChannelCompat> mChannels;
    String mDescription;
    final String mId;
    CharSequence mName;

    public static class Builder {
        final NotificationChannelGroupCompat mGroup;

        public Builder(String id) {
            this.mGroup = new NotificationChannelGroupCompat(id);
        }

        public NotificationChannelGroupCompat build() {
            return this.mGroup;
        }

        public Builder setDescription(String description) {
            this.mGroup.mDescription = description;
            return this;
        }

        public Builder setName(CharSequence name) {
            this.mGroup.mName = name;
            return this;
        }
    }

    NotificationChannelGroupCompat(NotificationChannelGroup group) {
        this(group, Collections.emptyList());
    }

    NotificationChannelGroupCompat(NotificationChannelGroup group, List<NotificationChannel> list) {
        this(group.getId());
        this.mName = group.getName();
        if (Build.VERSION.SDK_INT >= 28) {
            this.mDescription = group.getDescription();
        }
        if (Build.VERSION.SDK_INT >= 28) {
            this.mBlocked = group.isBlocked();
            this.mChannels = getChannelsCompat(group.getChannels());
            return;
        }
        this.mChannels = getChannelsCompat(list);
    }

    NotificationChannelGroupCompat(String id) {
        this.mChannels = Collections.emptyList();
        this.mId = (String) Preconditions.checkNotNull(id);
    }

    private List<NotificationChannelCompat> getChannelsCompat(List<NotificationChannel> list) {
        ArrayList arrayList = new ArrayList();
        for (NotificationChannel next : list) {
            if (this.mId.equals(next.getGroup())) {
                arrayList.add(new NotificationChannelCompat(next));
            }
        }
        return arrayList;
    }

    public List<NotificationChannelCompat> getChannels() {
        return this.mChannels;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public String getId() {
        return this.mId;
    }

    public CharSequence getName() {
        return this.mName;
    }

    /* access modifiers changed from: package-private */
    public NotificationChannelGroup getNotificationChannelGroup() {
        if (Build.VERSION.SDK_INT < 26) {
            return null;
        }
        NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup(this.mId, this.mName);
        if (Build.VERSION.SDK_INT >= 28) {
            notificationChannelGroup.setDescription(this.mDescription);
        }
        return notificationChannelGroup;
    }

    public boolean isBlocked() {
        return this.mBlocked;
    }

    public Builder toBuilder() {
        return new Builder(this.mId).setName(this.mName).setDescription(this.mDescription);
    }
}
