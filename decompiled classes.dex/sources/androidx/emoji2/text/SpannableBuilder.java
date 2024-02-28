package androidx.emoji2.text;

import android.os.Build;
import android.text.Editable;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import androidx.core.util.Preconditions;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class SpannableBuilder extends SpannableStringBuilder {
    private final Class<?> mWatcherClass;
    private final List<WatcherWrapper> mWatchers = new ArrayList();

    private static class WatcherWrapper implements TextWatcher, SpanWatcher {
        private final AtomicInteger mBlockCalls = new AtomicInteger(0);
        final Object mObject;

        WatcherWrapper(Object object) {
            this.mObject = object;
        }

        private boolean isEmojiSpan(Object span) {
            return span instanceof EmojiSpan;
        }

        public void afterTextChanged(Editable s) {
            ((TextWatcher) this.mObject).afterTextChanged(s);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            ((TextWatcher) this.mObject).beforeTextChanged(s, start, count, after);
        }

        /* access modifiers changed from: package-private */
        public final void blockCalls() {
            this.mBlockCalls.incrementAndGet();
        }

        public void onSpanAdded(Spannable text, Object what, int start, int end) {
            if (this.mBlockCalls.get() <= 0 || !isEmojiSpan(what)) {
                ((SpanWatcher) this.mObject).onSpanAdded(text, what, start, end);
            }
        }

        public void onSpanChanged(Spannable text, Object what, int ostart, int oend, int nstart, int nend) {
            if (this.mBlockCalls.get() <= 0 || !isEmojiSpan(what)) {
                if (Build.VERSION.SDK_INT < 28) {
                    if (ostart > oend) {
                        ostart = 0;
                    }
                    if (nstart > nend) {
                        nstart = 0;
                    }
                }
                ((SpanWatcher) this.mObject).onSpanChanged(text, what, ostart, oend, nstart, nend);
            }
        }

        public void onSpanRemoved(Spannable text, Object what, int start, int end) {
            if (this.mBlockCalls.get() <= 0 || !isEmojiSpan(what)) {
                ((SpanWatcher) this.mObject).onSpanRemoved(text, what, start, end);
            }
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            ((TextWatcher) this.mObject).onTextChanged(s, start, before, count);
        }

        /* access modifiers changed from: package-private */
        public final void unblockCalls() {
            this.mBlockCalls.decrementAndGet();
        }
    }

    SpannableBuilder(Class<?> cls) {
        Preconditions.checkNotNull(cls, "watcherClass cannot be null");
        this.mWatcherClass = cls;
    }

    SpannableBuilder(Class<?> cls, CharSequence text) {
        super(text);
        Preconditions.checkNotNull(cls, "watcherClass cannot be null");
        this.mWatcherClass = cls;
    }

    SpannableBuilder(Class<?> cls, CharSequence text, int start, int end) {
        super(text, start, end);
        Preconditions.checkNotNull(cls, "watcherClass cannot be null");
        this.mWatcherClass = cls;
    }

    private void blockWatchers() {
        for (int i = 0; i < this.mWatchers.size(); i++) {
            this.mWatchers.get(i).blockCalls();
        }
    }

    public static SpannableBuilder create(Class<?> cls, CharSequence text) {
        return new SpannableBuilder(cls, text);
    }

    private void fireWatchers() {
        for (int i = 0; i < this.mWatchers.size(); i++) {
            this.mWatchers.get(i).onTextChanged(this, 0, length(), length());
        }
    }

    private WatcherWrapper getWatcherFor(Object object) {
        for (int i = 0; i < this.mWatchers.size(); i++) {
            WatcherWrapper watcherWrapper = this.mWatchers.get(i);
            if (watcherWrapper.mObject == object) {
                return watcherWrapper;
            }
        }
        return null;
    }

    private boolean isWatcher(Class<?> cls) {
        return this.mWatcherClass == cls;
    }

    private boolean isWatcher(Object object) {
        return object != null && isWatcher(object.getClass());
    }

    private void unblockwatchers() {
        for (int i = 0; i < this.mWatchers.size(); i++) {
            this.mWatchers.get(i).unblockCalls();
        }
    }

    public SpannableStringBuilder append(char text) {
        super.append(text);
        return this;
    }

    public SpannableStringBuilder append(CharSequence text) {
        super.append(text);
        return this;
    }

    public SpannableStringBuilder append(CharSequence text, int start, int end) {
        super.append(text, start, end);
        return this;
    }

    public SpannableStringBuilder append(CharSequence text, Object what, int flags) {
        super.append(text, what, flags);
        return this;
    }

    public void beginBatchEdit() {
        blockWatchers();
    }

    public SpannableStringBuilder delete(int start, int end) {
        super.delete(start, end);
        return this;
    }

    public void endBatchEdit() {
        unblockwatchers();
        fireWatchers();
    }

    public int getSpanEnd(Object tag) {
        WatcherWrapper watcherFor;
        if (isWatcher(tag) && (watcherFor = getWatcherFor(tag)) != null) {
            tag = watcherFor;
        }
        return super.getSpanEnd(tag);
    }

    public int getSpanFlags(Object tag) {
        WatcherWrapper watcherFor;
        if (isWatcher(tag) && (watcherFor = getWatcherFor(tag)) != null) {
            tag = watcherFor;
        }
        return super.getSpanFlags(tag);
    }

    public int getSpanStart(Object tag) {
        WatcherWrapper watcherFor;
        if (isWatcher(tag) && (watcherFor = getWatcherFor(tag)) != null) {
            tag = watcherFor;
        }
        return super.getSpanStart(tag);
    }

    public <T> T[] getSpans(int queryStart, int queryEnd, Class<T> cls) {
        if (!isWatcher((Class<?>) cls)) {
            return super.getSpans(queryStart, queryEnd, cls);
        }
        WatcherWrapper[] watcherWrapperArr = (WatcherWrapper[]) super.getSpans(queryStart, queryEnd, WatcherWrapper.class);
        T[] tArr = (Object[]) Array.newInstance(cls, watcherWrapperArr.length);
        for (int i = 0; i < watcherWrapperArr.length; i++) {
            tArr[i] = watcherWrapperArr[i].mObject;
        }
        return tArr;
    }

    public SpannableStringBuilder insert(int where, CharSequence tb) {
        super.insert(where, tb);
        return this;
    }

    public SpannableStringBuilder insert(int where, CharSequence tb, int start, int end) {
        super.insert(where, tb, start, end);
        return this;
    }

    public int nextSpanTransition(int start, int limit, Class type) {
        if (type == null || isWatcher((Class<?>) type)) {
            type = WatcherWrapper.class;
        }
        return super.nextSpanTransition(start, limit, type);
    }

    public void removeSpan(Object what) {
        WatcherWrapper watcherWrapper;
        if (isWatcher(what)) {
            watcherWrapper = getWatcherFor(what);
            if (watcherWrapper != null) {
                what = watcherWrapper;
            }
        } else {
            watcherWrapper = null;
        }
        super.removeSpan(what);
        if (watcherWrapper != null) {
            this.mWatchers.remove(watcherWrapper);
        }
    }

    public SpannableStringBuilder replace(int start, int end, CharSequence tb) {
        blockWatchers();
        super.replace(start, end, tb);
        unblockwatchers();
        return this;
    }

    public SpannableStringBuilder replace(int start, int end, CharSequence tb, int tbstart, int tbend) {
        blockWatchers();
        super.replace(start, end, tb, tbstart, tbend);
        unblockwatchers();
        return this;
    }

    public void setSpan(Object what, int start, int end, int flags) {
        if (isWatcher(what)) {
            WatcherWrapper watcherWrapper = new WatcherWrapper(what);
            this.mWatchers.add(watcherWrapper);
            what = watcherWrapper;
        }
        super.setSpan(what, start, end, flags);
    }

    public CharSequence subSequence(int start, int end) {
        return new SpannableBuilder(this.mWatcherClass, this, start, end);
    }
}
