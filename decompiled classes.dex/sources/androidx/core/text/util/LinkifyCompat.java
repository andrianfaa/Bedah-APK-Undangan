package androidx.core.text.util;

import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.webkit.WebView;
import android.widget.TextView;
import androidx.core.net.MailTo;
import androidx.core.util.PatternsCompat;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mt.Log1F380D;
import okhttp3.HttpUrl;

/* compiled from: 0059 */
public final class LinkifyCompat {
    private static final Comparator<LinkSpec> COMPARATOR = new LinkifyCompat$$ExternalSyntheticLambda0();
    private static final String[] EMPTY_STRING = new String[0];

    static class Api24Impl {
        private Api24Impl() {
        }

        static void addLinks(TextView text, Pattern pattern, String defaultScheme, String[] schemes, Linkify.MatchFilter matchFilter, Linkify.TransformFilter transformFilter) {
            Linkify.addLinks(text, pattern, defaultScheme, schemes, matchFilter, transformFilter);
        }

        static boolean addLinks(Spannable spannable, Pattern pattern, String defaultScheme, String[] schemes, Linkify.MatchFilter matchFilter, Linkify.TransformFilter transformFilter) {
            return Linkify.addLinks(spannable, pattern, defaultScheme, schemes, matchFilter, transformFilter);
        }
    }

    private static class LinkSpec {
        int end;
        URLSpan frameworkAddedSpan;
        int start;
        String url;

        LinkSpec() {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LinkifyMask {
    }

    private LinkifyCompat() {
    }

    private static void addLinkMovementMethod(TextView t) {
        if (!(t.getMovementMethod() instanceof LinkMovementMethod) && t.getLinksClickable()) {
            t.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public static void addLinks(TextView text, Pattern pattern, String scheme) {
        if (shouldAddLinksFallbackToFramework()) {
            Linkify.addLinks(text, pattern, scheme);
        } else {
            addLinks(text, pattern, scheme, (String[]) null, (Linkify.MatchFilter) null, (Linkify.TransformFilter) null);
        }
    }

    public static void addLinks(TextView text, Pattern pattern, String scheme, Linkify.MatchFilter matchFilter, Linkify.TransformFilter transformFilter) {
        if (shouldAddLinksFallbackToFramework()) {
            Linkify.addLinks(text, pattern, scheme, matchFilter, transformFilter);
        } else {
            addLinks(text, pattern, scheme, (String[]) null, matchFilter, transformFilter);
        }
    }

    public static void addLinks(TextView text, Pattern pattern, String defaultScheme, String[] schemes, Linkify.MatchFilter matchFilter, Linkify.TransformFilter transformFilter) {
        if (shouldAddLinksFallbackToFramework()) {
            Api24Impl.addLinks(text, pattern, defaultScheme, schemes, matchFilter, transformFilter);
            return;
        }
        SpannableString valueOf = SpannableString.valueOf(text.getText());
        if (addLinks((Spannable) valueOf, pattern, defaultScheme, schemes, matchFilter, transformFilter)) {
            text.setText(valueOf);
            addLinkMovementMethod(text);
        }
    }

    public static boolean addLinks(Spannable text, int mask) {
        if (shouldAddLinksFallbackToFramework()) {
            return Linkify.addLinks(text, mask);
        }
        if (mask == 0) {
            return false;
        }
        URLSpan[] uRLSpanArr = (URLSpan[]) text.getSpans(0, text.length(), URLSpan.class);
        for (int length = uRLSpanArr.length - 1; length >= 0; length--) {
            text.removeSpan(uRLSpanArr[length]);
        }
        if ((mask & 4) != 0) {
            Linkify.addLinks(text, 4);
        }
        ArrayList arrayList = new ArrayList();
        if ((mask & 1) != 0) {
            gatherLinks(arrayList, text, PatternsCompat.AUTOLINK_WEB_URL, new String[]{"http://", "https://", "rtsp://"}, Linkify.sUrlMatchFilter, (Linkify.TransformFilter) null);
        }
        if ((mask & 2) != 0) {
            gatherLinks(arrayList, text, PatternsCompat.AUTOLINK_EMAIL_ADDRESS, new String[]{MailTo.MAILTO_SCHEME}, (Linkify.MatchFilter) null, (Linkify.TransformFilter) null);
        }
        if ((mask & 8) != 0) {
            gatherMapLinks(arrayList, text);
        }
        pruneOverlaps(arrayList, text);
        if (arrayList.size() == 0) {
            return false;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            LinkSpec linkSpec = (LinkSpec) it.next();
            if (linkSpec.frameworkAddedSpan == null) {
                applyLink(linkSpec.url, linkSpec.start, linkSpec.end, text);
            }
        }
        return true;
    }

    public static boolean addLinks(Spannable text, Pattern pattern, String scheme) {
        return shouldAddLinksFallbackToFramework() ? Linkify.addLinks(text, pattern, scheme) : addLinks(text, pattern, scheme, (String[]) null, (Linkify.MatchFilter) null, (Linkify.TransformFilter) null);
    }

    public static boolean addLinks(Spannable spannable, Pattern pattern, String scheme, Linkify.MatchFilter matchFilter, Linkify.TransformFilter transformFilter) {
        return shouldAddLinksFallbackToFramework() ? Linkify.addLinks(spannable, pattern, scheme, matchFilter, transformFilter) : addLinks(spannable, pattern, scheme, (String[]) null, matchFilter, transformFilter);
    }

    public static boolean addLinks(Spannable spannable, Pattern pattern, String defaultScheme, String[] schemes, Linkify.MatchFilter matchFilter, Linkify.TransformFilter transformFilter) {
        if (shouldAddLinksFallbackToFramework()) {
            return Api24Impl.addLinks(spannable, pattern, defaultScheme, schemes, matchFilter, transformFilter);
        }
        if (defaultScheme == null) {
            defaultScheme = HttpUrl.FRAGMENT_ENCODE_SET;
        }
        if (schemes == null || schemes.length < 1) {
            schemes = EMPTY_STRING;
        }
        String[] strArr = new String[(schemes.length + 1)];
        strArr[0] = defaultScheme.toLowerCase(Locale.ROOT);
        for (int i = 0; i < schemes.length; i++) {
            String str = schemes[i];
            strArr[i + 1] = str == null ? HttpUrl.FRAGMENT_ENCODE_SET : str.toLowerCase(Locale.ROOT);
        }
        boolean z = false;
        Matcher matcher = pattern.matcher(spannable);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String group = matcher.group(0);
            boolean z2 = true;
            if (matchFilter != null) {
                z2 = matchFilter.acceptMatch(spannable, start, end);
            }
            if (z2 && group != null) {
                String makeUrl = makeUrl(group, strArr, matcher, transformFilter);
                Log1F380D.a((Object) makeUrl);
                applyLink(makeUrl, start, end, spannable);
                z = true;
            }
        }
        return z;
    }

    public static boolean addLinks(TextView text, int mask) {
        if (shouldAddLinksFallbackToFramework()) {
            return Linkify.addLinks(text, mask);
        }
        if (mask == 0) {
            return false;
        }
        CharSequence text2 = text.getText();
        if (!(text2 instanceof Spannable)) {
            SpannableString valueOf = SpannableString.valueOf(text2);
            if (addLinks((Spannable) valueOf, mask)) {
                addLinkMovementMethod(text);
                text.setText(valueOf);
                return true;
            }
        } else if (addLinks((Spannable) text2, mask)) {
            addLinkMovementMethod(text);
            return true;
        }
        return false;
    }

    private static void applyLink(String url, int start, int end, Spannable text) {
        text.setSpan(new URLSpan(url), start, end, 33);
    }

    static /* synthetic */ int lambda$static$0(LinkSpec a, LinkSpec b) {
        if (a.start < b.start) {
            return -1;
        }
        if (a.start > b.start) {
            return 1;
        }
        return Integer.compare(b.end, a.end);
    }

    private static String makeUrl(String url, String[] prefixes, Matcher matcher, Linkify.TransformFilter filter) {
        if (filter != null) {
            url = filter.transformUrl(matcher, url);
        }
        boolean z = false;
        int length = prefixes.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            String str = prefixes[i];
            if (url.regionMatches(true, 0, str, 0, str.length())) {
                z = true;
                if (!url.regionMatches(false, 0, str, 0, str.length())) {
                    url = str + url.substring(str.length());
                }
            } else {
                i++;
            }
        }
        return (z || prefixes.length <= 0) ? url : prefixes[0] + url;
    }

    private static void pruneOverlaps(ArrayList<LinkSpec> arrayList, Spannable text) {
        for (URLSpan uRLSpan : (URLSpan[]) text.getSpans(0, text.length(), URLSpan.class)) {
            LinkSpec linkSpec = new LinkSpec();
            linkSpec.frameworkAddedSpan = uRLSpan;
            linkSpec.start = text.getSpanStart(uRLSpan);
            linkSpec.end = text.getSpanEnd(uRLSpan);
            arrayList.add(linkSpec);
        }
        Collections.sort(arrayList, COMPARATOR);
        int size = arrayList.size();
        int i = 0;
        while (i < size - 1) {
            LinkSpec linkSpec2 = arrayList.get(i);
            LinkSpec linkSpec3 = arrayList.get(i + 1);
            int i2 = -1;
            if (linkSpec2.start <= linkSpec3.start && linkSpec2.end > linkSpec3.start) {
                if (linkSpec3.end <= linkSpec2.end) {
                    i2 = i + 1;
                } else if (linkSpec2.end - linkSpec2.start > linkSpec3.end - linkSpec3.start) {
                    i2 = i + 1;
                } else if (linkSpec2.end - linkSpec2.start < linkSpec3.end - linkSpec3.start) {
                    i2 = i;
                }
                if (i2 != -1) {
                    URLSpan uRLSpan2 = arrayList.get(i2).frameworkAddedSpan;
                    if (uRLSpan2 != null) {
                        text.removeSpan(uRLSpan2);
                    }
                    arrayList.remove(i2);
                    size--;
                }
            }
            i++;
        }
    }

    private static boolean shouldAddLinksFallbackToFramework() {
        return Build.VERSION.SDK_INT >= 28;
    }

    private static String findAddress(String addr) {
        if (Build.VERSION.SDK_INT >= 28) {
            String findAddress = WebView.findAddress(addr);
            Log1F380D.a((Object) findAddress);
            return findAddress;
        }
        String findAddress2 = FindAddress.findAddress(addr);
        Log1F380D.a((Object) findAddress2);
        return findAddress2;
    }

    private static void gatherLinks(ArrayList<LinkSpec> arrayList, Spannable s, Pattern pattern, String[] schemes, Linkify.MatchFilter matchFilter, Linkify.TransformFilter transformFilter) {
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String group = matcher.group(0);
            if ((matchFilter == null || matchFilter.acceptMatch(s, start, end)) && group != null) {
                LinkSpec linkSpec = new LinkSpec();
                String makeUrl = makeUrl(group, schemes, matcher, transformFilter);
                Log1F380D.a((Object) makeUrl);
                linkSpec.url = makeUrl;
                linkSpec.start = start;
                linkSpec.end = end;
                arrayList.add(linkSpec);
            }
        }
    }

    private static void gatherMapLinks(ArrayList<LinkSpec> arrayList, Spannable s) {
        String obj = s.toString();
        int i = 0;
        while (true) {
            try {
                String findAddress = findAddress(obj);
                Log1F380D.a((Object) findAddress);
                String str = findAddress;
                if (findAddress != null) {
                    int indexOf = obj.indexOf(str);
                    if (indexOf >= 0) {
                        LinkSpec linkSpec = new LinkSpec();
                        int length = indexOf + str.length();
                        linkSpec.start = i + indexOf;
                        linkSpec.end = i + length;
                        obj = obj.substring(length);
                        i += length;
                        try {
                            String encode = URLEncoder.encode(str, "UTF-8");
                            Log1F380D.a((Object) encode);
                            linkSpec.url = "geo:0,0?q=" + encode;
                            arrayList.add(linkSpec);
                        } catch (UnsupportedEncodingException e) {
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } catch (UnsupportedOperationException e2) {
                return;
            }
        }
    }
}
