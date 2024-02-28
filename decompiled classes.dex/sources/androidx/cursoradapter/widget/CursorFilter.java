package androidx.cursoradapter.widget;

import android.database.Cursor;
import android.widget.Filter;

class CursorFilter extends Filter {
    CursorFilterClient mClient;

    interface CursorFilterClient {
        void changeCursor(Cursor cursor);

        CharSequence convertToString(Cursor cursor);

        Cursor getCursor();

        Cursor runQueryOnBackgroundThread(CharSequence charSequence);
    }

    CursorFilter(CursorFilterClient client) {
        this.mClient = client;
    }

    public CharSequence convertResultToString(Object resultValue) {
        return this.mClient.convertToString((Cursor) resultValue);
    }

    /* access modifiers changed from: protected */
    public Filter.FilterResults performFiltering(CharSequence constraint) {
        Cursor runQueryOnBackgroundThread = this.mClient.runQueryOnBackgroundThread(constraint);
        Filter.FilterResults filterResults = new Filter.FilterResults();
        if (runQueryOnBackgroundThread != null) {
            filterResults.count = runQueryOnBackgroundThread.getCount();
            filterResults.values = runQueryOnBackgroundThread;
        } else {
            filterResults.count = 0;
            filterResults.values = null;
        }
        return filterResults;
    }

    /* access modifiers changed from: protected */
    public void publishResults(CharSequence constraint, Filter.FilterResults results) {
        Cursor cursor = this.mClient.getCursor();
        if (results.values != null && results.values != cursor) {
            this.mClient.changeCursor((Cursor) results.values);
        }
    }
}
