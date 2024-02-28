package androidx.work;

import android.net.Uri;
import java.util.HashSet;
import java.util.Set;

public final class ContentUriTriggers {
    private final Set<Trigger> mTriggers = new HashSet();

    public static final class Trigger {
        private final boolean mTriggerForDescendants;
        private final Uri mUri;

        Trigger(Uri uri, boolean triggerForDescendants) {
            this.mUri = uri;
            this.mTriggerForDescendants = triggerForDescendants;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Trigger trigger = (Trigger) o;
            return this.mTriggerForDescendants == trigger.mTriggerForDescendants && this.mUri.equals(trigger.mUri);
        }

        public Uri getUri() {
            return this.mUri;
        }

        public int hashCode() {
            return (this.mUri.hashCode() * 31) + (this.mTriggerForDescendants ? 1 : 0);
        }

        public boolean shouldTriggerForDescendants() {
            return this.mTriggerForDescendants;
        }
    }

    public void add(Uri uri, boolean triggerForDescendants) {
        this.mTriggers.add(new Trigger(uri, triggerForDescendants));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return this.mTriggers.equals(((ContentUriTriggers) o).mTriggers);
    }

    public Set<Trigger> getTriggers() {
        return this.mTriggers;
    }

    public int hashCode() {
        return this.mTriggers.hashCode();
    }

    public int size() {
        return this.mTriggers.size();
    }
}
