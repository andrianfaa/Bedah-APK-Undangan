package androidx.cursoradapter.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;
import androidx.cursoradapter.widget.CursorFilter;
import okhttp3.HttpUrl;

public abstract class CursorAdapter extends BaseAdapter implements Filterable, CursorFilter.CursorFilterClient {
    @Deprecated
    public static final int FLAG_AUTO_REQUERY = 1;
    public static final int FLAG_REGISTER_CONTENT_OBSERVER = 2;
    protected boolean mAutoRequery;
    protected ChangeObserver mChangeObserver;
    protected Context mContext;
    protected Cursor mCursor;
    protected CursorFilter mCursorFilter;
    protected DataSetObserver mDataSetObserver;
    protected boolean mDataValid;
    protected FilterQueryProvider mFilterQueryProvider;
    protected int mRowIDColumn;

    private class ChangeObserver extends ContentObserver {
        ChangeObserver() {
            super(new Handler());
        }

        public boolean deliverSelfNotifications() {
            return true;
        }

        public void onChange(boolean selfChange) {
            CursorAdapter.this.onContentChanged();
        }
    }

    private class MyDataSetObserver extends DataSetObserver {
        MyDataSetObserver() {
        }

        public void onChanged() {
            CursorAdapter.this.mDataValid = true;
            CursorAdapter.this.notifyDataSetChanged();
        }

        public void onInvalidated() {
            CursorAdapter.this.mDataValid = false;
            CursorAdapter.this.notifyDataSetInvalidated();
        }
    }

    @Deprecated
    public CursorAdapter(Context context, Cursor c) {
        init(context, c, 1);
    }

    public CursorAdapter(Context context, Cursor c, int flags) {
        init(context, c, flags);
    }

    public CursorAdapter(Context context, Cursor c, boolean autoRequery) {
        init(context, c, autoRequery ? 1 : 2);
    }

    public abstract void bindView(View view, Context context, Cursor cursor);

    public void changeCursor(Cursor cursor) {
        Cursor swapCursor = swapCursor(cursor);
        if (swapCursor != null) {
            swapCursor.close();
        }
    }

    public CharSequence convertToString(Cursor cursor) {
        return cursor == null ? HttpUrl.FRAGMENT_ENCODE_SET : cursor.toString();
    }

    public int getCount() {
        Cursor cursor;
        if (!this.mDataValid || (cursor = this.mCursor) == null) {
            return 0;
        }
        return cursor.getCount();
    }

    public Cursor getCursor() {
        return this.mCursor;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (!this.mDataValid) {
            return null;
        }
        this.mCursor.moveToPosition(position);
        View newDropDownView = convertView == null ? newDropDownView(this.mContext, this.mCursor, parent) : convertView;
        bindView(newDropDownView, this.mContext, this.mCursor);
        return newDropDownView;
    }

    public Filter getFilter() {
        if (this.mCursorFilter == null) {
            this.mCursorFilter = new CursorFilter(this);
        }
        return this.mCursorFilter;
    }

    public FilterQueryProvider getFilterQueryProvider() {
        return this.mFilterQueryProvider;
    }

    public Object getItem(int position) {
        Cursor cursor;
        if (!this.mDataValid || (cursor = this.mCursor) == null) {
            return null;
        }
        cursor.moveToPosition(position);
        return this.mCursor;
    }

    public long getItemId(int position) {
        Cursor cursor;
        if (!this.mDataValid || (cursor = this.mCursor) == null || !cursor.moveToPosition(position)) {
            return 0;
        }
        return this.mCursor.getLong(this.mRowIDColumn);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (!this.mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        } else if (this.mCursor.moveToPosition(position)) {
            View newView = convertView == null ? newView(this.mContext, this.mCursor, parent) : convertView;
            bindView(newView, this.mContext, this.mCursor);
            return newView;
        } else {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
    }

    public boolean hasStableIds() {
        return true;
    }

    /* access modifiers changed from: package-private */
    public void init(Context context, Cursor c, int flags) {
        boolean z = false;
        if ((flags & 1) == 1) {
            flags |= 2;
            this.mAutoRequery = true;
        } else {
            this.mAutoRequery = false;
        }
        if (c != null) {
            z = true;
        }
        boolean z2 = z;
        this.mCursor = c;
        this.mDataValid = z2;
        this.mContext = context;
        this.mRowIDColumn = z2 ? c.getColumnIndexOrThrow("_id") : -1;
        if ((flags & 2) == 2) {
            this.mChangeObserver = new ChangeObserver();
            this.mDataSetObserver = new MyDataSetObserver();
        } else {
            this.mChangeObserver = null;
            this.mDataSetObserver = null;
        }
        if (z2) {
            ChangeObserver changeObserver = this.mChangeObserver;
            if (changeObserver != null) {
                c.registerContentObserver(changeObserver);
            }
            DataSetObserver dataSetObserver = this.mDataSetObserver;
            if (dataSetObserver != null) {
                c.registerDataSetObserver(dataSetObserver);
            }
        }
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public void init(Context context, Cursor c, boolean autoRequery) {
        init(context, c, autoRequery ? 1 : 2);
    }

    public View newDropDownView(Context context, Cursor cursor, ViewGroup parent) {
        return newView(context, cursor, parent);
    }

    public abstract View newView(Context context, Cursor cursor, ViewGroup viewGroup);

    /* access modifiers changed from: protected */
    public void onContentChanged() {
        Cursor cursor;
        if (this.mAutoRequery && (cursor = this.mCursor) != null && !cursor.isClosed()) {
            this.mDataValid = this.mCursor.requery();
        }
    }

    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        FilterQueryProvider filterQueryProvider = this.mFilterQueryProvider;
        return filterQueryProvider != null ? filterQueryProvider.runQuery(constraint) : this.mCursor;
    }

    public void setFilterQueryProvider(FilterQueryProvider filterQueryProvider) {
        this.mFilterQueryProvider = filterQueryProvider;
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == this.mCursor) {
            return null;
        }
        Cursor cursor = this.mCursor;
        if (cursor != null) {
            ChangeObserver changeObserver = this.mChangeObserver;
            if (changeObserver != null) {
                cursor.unregisterContentObserver(changeObserver);
            }
            DataSetObserver dataSetObserver = this.mDataSetObserver;
            if (dataSetObserver != null) {
                cursor.unregisterDataSetObserver(dataSetObserver);
            }
        }
        this.mCursor = newCursor;
        if (newCursor != null) {
            ChangeObserver changeObserver2 = this.mChangeObserver;
            if (changeObserver2 != null) {
                newCursor.registerContentObserver(changeObserver2);
            }
            DataSetObserver dataSetObserver2 = this.mDataSetObserver;
            if (dataSetObserver2 != null) {
                newCursor.registerDataSetObserver(dataSetObserver2);
            }
            this.mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
            this.mDataValid = true;
            notifyDataSetChanged();
        } else {
            this.mRowIDColumn = -1;
            this.mDataValid = false;
            notifyDataSetInvalidated();
        }
        return cursor;
    }
}
