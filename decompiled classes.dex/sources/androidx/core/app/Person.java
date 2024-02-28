package androidx.core.app;

import android.app.Person;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.core.graphics.drawable.IconCompat;
import okhttp3.HttpUrl;

public class Person {
    private static final String ICON_KEY = "icon";
    private static final String IS_BOT_KEY = "isBot";
    private static final String IS_IMPORTANT_KEY = "isImportant";
    private static final String KEY_KEY = "key";
    private static final String NAME_KEY = "name";
    private static final String URI_KEY = "uri";
    IconCompat mIcon;
    boolean mIsBot;
    boolean mIsImportant;
    String mKey;
    CharSequence mName;
    String mUri;

    static class Api22Impl {
        private Api22Impl() {
        }

        static Person fromPersistableBundle(PersistableBundle bundle) {
            return new Builder().setName(bundle.getString(Person.NAME_KEY)).setUri(bundle.getString(Person.URI_KEY)).setKey(bundle.getString(Person.KEY_KEY)).setBot(bundle.getBoolean(Person.IS_BOT_KEY)).setImportant(bundle.getBoolean(Person.IS_IMPORTANT_KEY)).build();
        }

        static PersistableBundle toPersistableBundle(Person person) {
            PersistableBundle persistableBundle = new PersistableBundle();
            persistableBundle.putString(Person.NAME_KEY, person.mName != null ? person.mName.toString() : null);
            persistableBundle.putString(Person.URI_KEY, person.mUri);
            persistableBundle.putString(Person.KEY_KEY, person.mKey);
            persistableBundle.putBoolean(Person.IS_BOT_KEY, person.mIsBot);
            persistableBundle.putBoolean(Person.IS_IMPORTANT_KEY, person.mIsImportant);
            return persistableBundle;
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static Person fromAndroidPerson(android.app.Person person) {
            return new Builder().setName(person.getName()).setIcon(person.getIcon() != null ? IconCompat.createFromIcon(person.getIcon()) : null).setUri(person.getUri()).setKey(person.getKey()).setBot(person.isBot()).setImportant(person.isImportant()).build();
        }

        static android.app.Person toAndroidPerson(Person person) {
            return new Person.Builder().setName(person.getName()).setIcon(person.getIcon() != null ? person.getIcon().toIcon() : null).setUri(person.getUri()).setKey(person.getKey()).setBot(person.isBot()).setImportant(person.isImportant()).build();
        }
    }

    public static class Builder {
        IconCompat mIcon;
        boolean mIsBot;
        boolean mIsImportant;
        String mKey;
        CharSequence mName;
        String mUri;

        public Builder() {
        }

        Builder(Person person) {
            this.mName = person.mName;
            this.mIcon = person.mIcon;
            this.mUri = person.mUri;
            this.mKey = person.mKey;
            this.mIsBot = person.mIsBot;
            this.mIsImportant = person.mIsImportant;
        }

        public Person build() {
            return new Person(this);
        }

        public Builder setBot(boolean bot) {
            this.mIsBot = bot;
            return this;
        }

        public Builder setIcon(IconCompat icon) {
            this.mIcon = icon;
            return this;
        }

        public Builder setImportant(boolean important) {
            this.mIsImportant = important;
            return this;
        }

        public Builder setKey(String key) {
            this.mKey = key;
            return this;
        }

        public Builder setName(CharSequence name) {
            this.mName = name;
            return this;
        }

        public Builder setUri(String uri) {
            this.mUri = uri;
            return this;
        }
    }

    Person(Builder builder) {
        this.mName = builder.mName;
        this.mIcon = builder.mIcon;
        this.mUri = builder.mUri;
        this.mKey = builder.mKey;
        this.mIsBot = builder.mIsBot;
        this.mIsImportant = builder.mIsImportant;
    }

    public static Person fromAndroidPerson(android.app.Person person) {
        return Api28Impl.fromAndroidPerson(person);
    }

    public static Person fromBundle(Bundle bundle) {
        Bundle bundle2 = bundle.getBundle(ICON_KEY);
        return new Builder().setName(bundle.getCharSequence(NAME_KEY)).setIcon(bundle2 != null ? IconCompat.createFromBundle(bundle2) : null).setUri(bundle.getString(URI_KEY)).setKey(bundle.getString(KEY_KEY)).setBot(bundle.getBoolean(IS_BOT_KEY)).setImportant(bundle.getBoolean(IS_IMPORTANT_KEY)).build();
    }

    public static Person fromPersistableBundle(PersistableBundle bundle) {
        return Api22Impl.fromPersistableBundle(bundle);
    }

    public IconCompat getIcon() {
        return this.mIcon;
    }

    public String getKey() {
        return this.mKey;
    }

    public CharSequence getName() {
        return this.mName;
    }

    public String getUri() {
        return this.mUri;
    }

    public boolean isBot() {
        return this.mIsBot;
    }

    public boolean isImportant() {
        return this.mIsImportant;
    }

    public String resolveToLegacyUri() {
        String str = this.mUri;
        return str != null ? str : this.mName != null ? "name:" + this.mName : HttpUrl.FRAGMENT_ENCODE_SET;
    }

    public android.app.Person toAndroidPerson() {
        return Api28Impl.toAndroidPerson(this);
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(NAME_KEY, this.mName);
        IconCompat iconCompat = this.mIcon;
        bundle.putBundle(ICON_KEY, iconCompat != null ? iconCompat.toBundle() : null);
        bundle.putString(URI_KEY, this.mUri);
        bundle.putString(KEY_KEY, this.mKey);
        bundle.putBoolean(IS_BOT_KEY, this.mIsBot);
        bundle.putBoolean(IS_IMPORTANT_KEY, this.mIsImportant);
        return bundle;
    }

    public PersistableBundle toPersistableBundle() {
        return Api22Impl.toPersistableBundle(this);
    }
}
