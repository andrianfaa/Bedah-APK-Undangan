package androidx.core.net;

import android.net.Uri;
import androidx.core.util.Preconditions;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import kotlin.text.Typography;
import mt.Log1F380D;

/* compiled from: 0046 */
public final class MailTo {
    private static final String BCC = "bcc";
    private static final String BODY = "body";
    private static final String CC = "cc";
    private static final String MAILTO = "mailto";
    public static final String MAILTO_SCHEME = "mailto:";
    private static final String SUBJECT = "subject";
    private static final String TO = "to";
    private HashMap<String, String> mHeaders = new HashMap<>();

    private MailTo() {
    }

    public static boolean isMailTo(Uri uri) {
        return uri != null && MAILTO.equals(uri.getScheme());
    }

    public static boolean isMailTo(String uri) {
        return uri != null && uri.startsWith(MAILTO_SCHEME);
    }

    public static MailTo parse(Uri uri) throws ParseException {
        return parse(uri.toString());
    }

    public static MailTo parse(String uri) throws ParseException {
        String str;
        String str2;
        String str3;
        Preconditions.checkNotNull(uri);
        if (isMailTo(uri)) {
            int indexOf = uri.indexOf(35);
            if (indexOf != -1) {
                uri = uri.substring(0, indexOf);
            }
            int indexOf2 = uri.indexOf(63);
            if (indexOf2 == -1) {
                str2 = Uri.decode(uri.substring(MAILTO_SCHEME.length()));
                Log1F380D.a((Object) str2);
                str = null;
            } else {
                str2 = Uri.decode(uri.substring(MAILTO_SCHEME.length(), indexOf2));
                Log1F380D.a((Object) str2);
                str = uri.substring(indexOf2 + 1);
            }
            MailTo mailTo = new MailTo();
            if (str != null) {
                for (String split : str.split("&")) {
                    String[] split2 = split.split("=", 2);
                    if (split2.length != 0) {
                        String decode = Uri.decode(split2[0]);
                        Log1F380D.a((Object) decode);
                        String lowerCase = decode.toLowerCase(Locale.ROOT);
                        if (split2.length > 1) {
                            str3 = Uri.decode(split2[1]);
                            Log1F380D.a((Object) str3);
                        } else {
                            str3 = null;
                        }
                        mailTo.mHeaders.put(lowerCase, str3);
                    }
                }
            }
            String to = mailTo.getTo();
            if (to != null) {
                str2 = str2 + ", " + to;
            }
            mailTo.mHeaders.put("to", str2);
            return mailTo;
        }
        throw new ParseException("Not a mailto scheme");
    }

    public String getBcc() {
        return this.mHeaders.get(BCC);
    }

    public String getBody() {
        return this.mHeaders.get(BODY);
    }

    public String getCc() {
        return this.mHeaders.get(CC);
    }

    public Map<String, String> getHeaders() {
        return this.mHeaders;
    }

    public String getSubject() {
        return this.mHeaders.get(SUBJECT);
    }

    public String getTo() {
        return this.mHeaders.get("to");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(MAILTO_SCHEME);
        sb.append('?');
        for (Map.Entry next : this.mHeaders.entrySet()) {
            String encode = Uri.encode((String) next.getKey());
            Log1F380D.a((Object) encode);
            sb.append(encode);
            sb.append('=');
            String encode2 = Uri.encode((String) next.getValue());
            Log1F380D.a((Object) encode2);
            sb.append(encode2);
            sb.append(Typography.amp);
        }
        return sb.toString();
    }
}
