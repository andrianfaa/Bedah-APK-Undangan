package androidx.recyclerview.widget;

import android.util.SparseArray;
import java.lang.reflect.Array;

class TileList<T> {
    Tile<T> mLastAccessedTile;
    final int mTileSize;
    private final SparseArray<Tile<T>> mTiles = new SparseArray<>(10);

    public static class Tile<T> {
        public int mItemCount;
        public final T[] mItems;
        Tile<T> mNext;
        public int mStartPosition;

        public Tile(Class<T> cls, int size) {
            this.mItems = (Object[]) Array.newInstance(cls, size);
        }

        /* access modifiers changed from: package-private */
        public boolean containsPosition(int pos) {
            int i = this.mStartPosition;
            return i <= pos && pos < i + this.mItemCount;
        }

        /* access modifiers changed from: package-private */
        public T getByPosition(int pos) {
            return this.mItems[pos - this.mStartPosition];
        }
    }

    public TileList(int tileSize) {
        this.mTileSize = tileSize;
    }

    public Tile<T> addOrReplace(Tile<T> tile) {
        int indexOfKey = this.mTiles.indexOfKey(tile.mStartPosition);
        if (indexOfKey < 0) {
            this.mTiles.put(tile.mStartPosition, tile);
            return null;
        }
        Tile<T> valueAt = this.mTiles.valueAt(indexOfKey);
        this.mTiles.setValueAt(indexOfKey, tile);
        if (this.mLastAccessedTile == valueAt) {
            this.mLastAccessedTile = tile;
        }
        return valueAt;
    }

    public void clear() {
        this.mTiles.clear();
    }

    public Tile<T> getAtIndex(int index) {
        return this.mTiles.valueAt(index);
    }

    public T getItemAt(int pos) {
        Tile<T> tile = this.mLastAccessedTile;
        if (tile == null || !tile.containsPosition(pos)) {
            int indexOfKey = this.mTiles.indexOfKey(pos - (pos % this.mTileSize));
            if (indexOfKey < 0) {
                return null;
            }
            this.mLastAccessedTile = this.mTiles.valueAt(indexOfKey);
        }
        return this.mLastAccessedTile.getByPosition(pos);
    }

    public Tile<T> removeAtPos(int startPosition) {
        Tile<T> tile = this.mTiles.get(startPosition);
        if (this.mLastAccessedTile == tile) {
            this.mLastAccessedTile = null;
        }
        this.mTiles.delete(startPosition);
        return tile;
    }

    public int size() {
        return this.mTiles.size();
    }
}
