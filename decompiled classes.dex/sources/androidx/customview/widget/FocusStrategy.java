package androidx.customview.widget;

import android.graphics.Rect;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class FocusStrategy {

    public interface BoundsAdapter<T> {
        void obtainBounds(T t, Rect rect);
    }

    public interface CollectionAdapter<T, V> {
        V get(T t, int i);

        int size(T t);
    }

    private static class SequentialComparator<T> implements Comparator<T> {
        private final BoundsAdapter<T> mAdapter;
        private final boolean mIsLayoutRtl;
        private final Rect mTemp1 = new Rect();
        private final Rect mTemp2 = new Rect();

        SequentialComparator(boolean isLayoutRtl, BoundsAdapter<T> boundsAdapter) {
            this.mIsLayoutRtl = isLayoutRtl;
            this.mAdapter = boundsAdapter;
        }

        public int compare(T t, T t2) {
            Rect rect = this.mTemp1;
            Rect rect2 = this.mTemp2;
            this.mAdapter.obtainBounds(t, rect);
            this.mAdapter.obtainBounds(t2, rect2);
            if (rect.top < rect2.top) {
                return -1;
            }
            if (rect.top > rect2.top) {
                return 1;
            }
            if (rect.left < rect2.left) {
                return this.mIsLayoutRtl ? 1 : -1;
            }
            if (rect.left > rect2.left) {
                return this.mIsLayoutRtl ? -1 : 1;
            }
            if (rect.bottom < rect2.bottom) {
                return -1;
            }
            if (rect.bottom > rect2.bottom) {
                return 1;
            }
            if (rect.right < rect2.right) {
                return this.mIsLayoutRtl ? 1 : -1;
            }
            if (rect.right > rect2.right) {
                return this.mIsLayoutRtl ? -1 : 1;
            }
            return 0;
        }
    }

    private FocusStrategy() {
    }

    private static boolean beamBeats(int direction, Rect source, Rect rect1, Rect rect2) {
        boolean beamsOverlap = beamsOverlap(direction, source, rect1);
        if (beamsOverlap(direction, source, rect2) || !beamsOverlap) {
            return false;
        }
        if (!isToDirectionOf(direction, source, rect2) || direction == 17 || direction == 66) {
            return true;
        }
        return majorAxisDistance(direction, source, rect1) < majorAxisDistanceToFarEdge(direction, source, rect2);
    }

    private static boolean beamsOverlap(int direction, Rect rect1, Rect rect2) {
        switch (direction) {
            case 17:
            case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT:
                return rect2.bottom >= rect1.top && rect2.top <= rect1.bottom;
            case 33:
            case 130:
                return rect2.right >= rect1.left && rect2.left <= rect1.right;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    public static <L, T> T findNextFocusInAbsoluteDirection(L l, CollectionAdapter<L, T> collectionAdapter, BoundsAdapter<T> boundsAdapter, T t, Rect focusedRect, int direction) {
        Rect rect = new Rect(focusedRect);
        switch (direction) {
            case 17:
                rect.offset(focusedRect.width() + 1, 0);
                break;
            case 33:
                rect.offset(0, focusedRect.height() + 1);
                break;
            case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT:
                rect.offset(-(focusedRect.width() + 1), 0);
                break;
            case 130:
                rect.offset(0, -(focusedRect.height() + 1));
                break;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
        T t2 = null;
        int size = collectionAdapter.size(l);
        Rect rect2 = new Rect();
        for (int i = 0; i < size; i++) {
            T t3 = collectionAdapter.get(l, i);
            if (t3 != t) {
                boundsAdapter.obtainBounds(t3, rect2);
                if (isBetterCandidate(direction, focusedRect, rect2, rect)) {
                    rect.set(rect2);
                    t2 = t3;
                }
            }
        }
        return t2;
    }

    public static <L, T> T findNextFocusInRelativeDirection(L l, CollectionAdapter<L, T> collectionAdapter, BoundsAdapter<T> boundsAdapter, T t, int direction, boolean isLayoutRtl, boolean wrap) {
        int size = collectionAdapter.size(l);
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(collectionAdapter.get(l, i));
        }
        Collections.sort(arrayList, new SequentialComparator(isLayoutRtl, boundsAdapter));
        switch (direction) {
            case 1:
                return getPreviousFocusable(t, arrayList, wrap);
            case 2:
                return getNextFocusable(t, arrayList, wrap);
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD}.");
        }
    }

    private static <T> T getNextFocusable(T t, ArrayList<T> arrayList, boolean wrap) {
        int size = arrayList.size();
        int lastIndexOf = (t == null ? -1 : arrayList.lastIndexOf(t)) + 1;
        if (lastIndexOf < size) {
            return arrayList.get(lastIndexOf);
        }
        if (!wrap || size <= 0) {
            return null;
        }
        return arrayList.get(0);
    }

    private static <T> T getPreviousFocusable(T t, ArrayList<T> arrayList, boolean wrap) {
        int size = arrayList.size();
        int indexOf = (t == null ? size : arrayList.indexOf(t)) - 1;
        if (indexOf >= 0) {
            return arrayList.get(indexOf);
        }
        if (!wrap || size <= 0) {
            return null;
        }
        return arrayList.get(size - 1);
    }

    private static int getWeightedDistanceFor(int majorAxisDistance, int minorAxisDistance) {
        return (majorAxisDistance * 13 * majorAxisDistance) + (minorAxisDistance * minorAxisDistance);
    }

    private static boolean isBetterCandidate(int direction, Rect source, Rect candidate, Rect currentBest) {
        if (!isCandidate(source, candidate, direction)) {
            return false;
        }
        if (isCandidate(source, currentBest, direction) && !beamBeats(direction, source, candidate, currentBest)) {
            return !beamBeats(direction, source, currentBest, candidate) && getWeightedDistanceFor(majorAxisDistance(direction, source, candidate), minorAxisDistance(direction, source, candidate)) < getWeightedDistanceFor(majorAxisDistance(direction, source, currentBest), minorAxisDistance(direction, source, currentBest));
        }
        return true;
    }

    private static boolean isCandidate(Rect srcRect, Rect destRect, int direction) {
        switch (direction) {
            case 17:
                return (srcRect.right > destRect.right || srcRect.left >= destRect.right) && srcRect.left > destRect.left;
            case 33:
                return (srcRect.bottom > destRect.bottom || srcRect.top >= destRect.bottom) && srcRect.top > destRect.top;
            case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT:
                return (srcRect.left < destRect.left || srcRect.right <= destRect.left) && srcRect.right < destRect.right;
            case 130:
                return (srcRect.top < destRect.top || srcRect.bottom <= destRect.top) && srcRect.bottom < destRect.bottom;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    private static boolean isToDirectionOf(int direction, Rect src, Rect dest) {
        switch (direction) {
            case 17:
                return src.left >= dest.right;
            case 33:
                return src.top >= dest.bottom;
            case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT:
                return src.right <= dest.left;
            case 130:
                return src.bottom <= dest.top;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    private static int majorAxisDistance(int direction, Rect source, Rect dest) {
        return Math.max(0, majorAxisDistanceRaw(direction, source, dest));
    }

    private static int majorAxisDistanceRaw(int direction, Rect source, Rect dest) {
        switch (direction) {
            case 17:
                return source.left - dest.right;
            case 33:
                return source.top - dest.bottom;
            case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT:
                return dest.left - source.right;
            case 130:
                return dest.top - source.bottom;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    private static int majorAxisDistanceToFarEdge(int direction, Rect source, Rect dest) {
        return Math.max(1, majorAxisDistanceToFarEdgeRaw(direction, source, dest));
    }

    private static int majorAxisDistanceToFarEdgeRaw(int direction, Rect source, Rect dest) {
        switch (direction) {
            case 17:
                return source.left - dest.left;
            case 33:
                return source.top - dest.top;
            case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT:
                return dest.right - source.right;
            case 130:
                return dest.bottom - source.bottom;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    private static int minorAxisDistance(int direction, Rect source, Rect dest) {
        switch (direction) {
            case 17:
            case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT:
                return Math.abs((source.top + (source.height() / 2)) - (dest.top + (dest.height() / 2)));
            case 33:
            case 130:
                return Math.abs((source.left + (source.width() / 2)) - (dest.left + (dest.width() / 2)));
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }
}
