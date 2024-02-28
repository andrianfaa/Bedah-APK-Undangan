package androidx.appcompat.widget;

import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.ResourceCursorAdapter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.WeakHashMap;
import mt.Log1F380D;
import okhttp3.HttpUrl;

/* compiled from: 000B */
class SuggestionsAdapter extends ResourceCursorAdapter implements View.OnClickListener {
    private static final boolean DBG = false;
    static final int INVALID_INDEX = -1;
    private static final String LOG_TAG = "SuggestionsAdapter";
    private static final int QUERY_LIMIT = 50;
    static final int REFINE_ALL = 2;
    static final int REFINE_BY_ENTRY = 1;
    static final int REFINE_NONE = 0;
    private boolean mClosed = false;
    private final int mCommitIconResId;
    private int mFlagsCol = -1;
    private int mIconName1Col = -1;
    private int mIconName2Col = -1;
    private final WeakHashMap<String, Drawable.ConstantState> mOutsideDrawablesCache;
    private final Context mProviderContext;
    private int mQueryRefinement = 1;
    private final SearchView mSearchView;
    private final SearchableInfo mSearchable;
    private int mText1Col = -1;
    private int mText2Col = -1;
    private int mText2UrlCol = -1;
    private ColorStateList mUrlColor;

    private static final class ChildViewCache {
        public final ImageView mIcon1;
        public final ImageView mIcon2;
        public final ImageView mIconRefine;
        public final TextView mText1;
        public final TextView mText2;

        public ChildViewCache(View v) {
            this.mText1 = (TextView) v.findViewById(16908308);
            this.mText2 = (TextView) v.findViewById(16908309);
            this.mIcon1 = (ImageView) v.findViewById(16908295);
            this.mIcon2 = (ImageView) v.findViewById(16908296);
            this.mIconRefine = (ImageView) v.findViewById(R.id.edit_query);
        }
    }

    public SuggestionsAdapter(Context context, SearchView searchView, SearchableInfo searchable, WeakHashMap<String, Drawable.ConstantState> weakHashMap) {
        super(context, searchView.getSuggestionRowLayout(), (Cursor) null, true);
        this.mSearchView = searchView;
        this.mSearchable = searchable;
        this.mCommitIconResId = searchView.getSuggestionCommitIconResId();
        this.mProviderContext = context;
        this.mOutsideDrawablesCache = weakHashMap;
    }

    private Drawable checkIconCache(String resourceUri) {
        Drawable.ConstantState constantState = this.mOutsideDrawablesCache.get(resourceUri);
        if (constantState == null) {
            return null;
        }
        return constantState.newDrawable();
    }

    private CharSequence formatUrl(CharSequence url) {
        if (this.mUrlColor == null) {
            TypedValue typedValue = new TypedValue();
            this.mProviderContext.getTheme().resolveAttribute(R.attr.textColorSearchUrl, typedValue, true);
            this.mUrlColor = this.mProviderContext.getResources().getColorStateList(typedValue.resourceId);
        }
        SpannableString spannableString = new SpannableString(url);
        spannableString.setSpan(new TextAppearanceSpan((String) null, 0, 0, this.mUrlColor, (ColorStateList) null), 0, url.length(), 33);
        return spannableString;
    }

    private Drawable getActivityIcon(ComponentName component) {
        PackageManager packageManager = this.mProviderContext.getPackageManager();
        try {
            ActivityInfo activityInfo = packageManager.getActivityInfo(component, 128);
            int iconResource = activityInfo.getIconResource();
            if (iconResource == 0) {
                return null;
            }
            Drawable drawable = packageManager.getDrawable(component.getPackageName(), iconResource, activityInfo.applicationInfo);
            if (drawable != null) {
                return drawable;
            }
            Log.w(LOG_TAG, "Invalid icon resource " + iconResource + " for " + component.flattenToShortString());
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(LOG_TAG, e.toString());
            return null;
        }
    }

    private Drawable getActivityIconWithCache(ComponentName component) {
        String flattenToShortString = component.flattenToShortString();
        Drawable.ConstantState constantState = null;
        if (this.mOutsideDrawablesCache.containsKey(flattenToShortString)) {
            Drawable.ConstantState constantState2 = this.mOutsideDrawablesCache.get(flattenToShortString);
            if (constantState2 == null) {
                return null;
            }
            return constantState2.newDrawable(this.mProviderContext.getResources());
        }
        Drawable activityIcon = getActivityIcon(component);
        if (activityIcon != null) {
            constantState = activityIcon.getConstantState();
        }
        this.mOutsideDrawablesCache.put(flattenToShortString, constantState);
        return activityIcon;
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        String stringOrNull = getStringOrNull(cursor, cursor.getColumnIndex(columnName));
        Log1F380D.a((Object) stringOrNull);
        return stringOrNull;
    }

    private Drawable getDefaultIcon1() {
        Drawable activityIconWithCache = getActivityIconWithCache(this.mSearchable.getSearchActivity());
        return activityIconWithCache != null ? activityIconWithCache : this.mProviderContext.getPackageManager().getDefaultActivityIcon();
    }

    private Drawable getDrawable(Uri uri) {
        InputStream openInputStream;
        try {
            if ("android.resource".equals(uri.getScheme())) {
                return getDrawableFromResourceUri(uri);
            }
            openInputStream = this.mProviderContext.getContentResolver().openInputStream(uri);
            if (openInputStream != null) {
                Drawable createFromStream = Drawable.createFromStream(openInputStream, (String) null);
                try {
                    openInputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing icon stream for " + uri, e);
                }
                return createFromStream;
            }
            throw new FileNotFoundException("Failed to open " + uri);
        } catch (Resources.NotFoundException e2) {
            throw new FileNotFoundException("Resource does not exist: " + uri);
        } catch (FileNotFoundException e3) {
            Log.w(LOG_TAG, "Icon not found: " + uri + ", " + e3.getMessage());
            return null;
        } catch (Throwable th) {
            try {
                openInputStream.close();
            } catch (IOException e4) {
                Log.e(LOG_TAG, "Error closing icon stream for " + uri, e4);
            }
            throw th;
        }
    }

    private Drawable getDrawableFromResourceValue(String drawableId) {
        if (drawableId == null || drawableId.isEmpty() || "0".equals(drawableId)) {
            return null;
        }
        try {
            int parseInt = Integer.parseInt(drawableId);
            String str = "android.resource://" + this.mProviderContext.getPackageName() + "/" + parseInt;
            Drawable checkIconCache = checkIconCache(str);
            if (checkIconCache != null) {
                return checkIconCache;
            }
            Drawable drawable = ContextCompat.getDrawable(this.mProviderContext, parseInt);
            storeInIconCache(str, drawable);
            return drawable;
        } catch (NumberFormatException e) {
            Drawable checkIconCache2 = checkIconCache(drawableId);
            if (checkIconCache2 != null) {
                return checkIconCache2;
            }
            Drawable drawable2 = getDrawable(Uri.parse(drawableId));
            storeInIconCache(drawableId, drawable2);
            return drawable2;
        } catch (Resources.NotFoundException e2) {
            Log.w(LOG_TAG, "Icon resource not found: " + drawableId);
            return null;
        }
    }

    private Drawable getIcon1(Cursor cursor) {
        int i = this.mIconName1Col;
        if (i == -1) {
            return null;
        }
        Drawable drawableFromResourceValue = getDrawableFromResourceValue(cursor.getString(i));
        return drawableFromResourceValue != null ? drawableFromResourceValue : getDefaultIcon1();
    }

    private Drawable getIcon2(Cursor cursor) {
        int i = this.mIconName2Col;
        if (i == -1) {
            return null;
        }
        return getDrawableFromResourceValue(cursor.getString(i));
    }

    private static String getStringOrNull(Cursor cursor, int col) {
        if (col == -1) {
            return null;
        }
        try {
            return cursor.getString(col);
        } catch (Exception e) {
            Log.e(LOG_TAG, "unexpected error retrieving valid column from cursor, did the remote process die?", e);
            return null;
        }
    }

    private void setViewDrawable(ImageView v, Drawable drawable, int nullVisibility) {
        v.setImageDrawable(drawable);
        if (drawable == null) {
            v.setVisibility(nullVisibility);
            return;
        }
        v.setVisibility(0);
        drawable.setVisible(false, false);
        drawable.setVisible(true, false);
    }

    private void setViewText(TextView v, CharSequence text) {
        v.setText(text);
        if (TextUtils.isEmpty(text)) {
            v.setVisibility(8);
        } else {
            v.setVisibility(0);
        }
    }

    private void storeInIconCache(String resourceUri, Drawable drawable) {
        if (drawable != null) {
            this.mOutsideDrawablesCache.put(resourceUri, drawable.getConstantState());
        }
    }

    private void updateSpinnerState(Cursor cursor) {
        Bundle extras = cursor != null ? cursor.getExtras() : null;
        if (extras != null) {
            extras.getBoolean("in_progress");
        }
    }

    public void changeCursor(Cursor c) {
        if (this.mClosed) {
            Log.w(LOG_TAG, "Tried to change cursor after adapter was closed.");
            if (c != null) {
                c.close();
                return;
            }
            return;
        }
        try {
            super.changeCursor(c);
            if (c != null) {
                this.mText1Col = c.getColumnIndex("suggest_text_1");
                this.mText2Col = c.getColumnIndex("suggest_text_2");
                this.mText2UrlCol = c.getColumnIndex("suggest_text_2_url");
                this.mIconName1Col = c.getColumnIndex("suggest_icon_1");
                this.mIconName2Col = c.getColumnIndex("suggest_icon_2");
                this.mFlagsCol = c.getColumnIndex("suggest_flags");
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "error changing cursor and caching columns", e);
        }
    }

    public void close() {
        changeCursor((Cursor) null);
        this.mClosed = true;
    }

    /* access modifiers changed from: package-private */
    public Drawable getDrawableFromResourceUri(Uri uri) throws FileNotFoundException {
        int i;
        String authority = uri.getAuthority();
        if (!TextUtils.isEmpty(authority)) {
            try {
                Resources resourcesForApplication = this.mProviderContext.getPackageManager().getResourcesForApplication(authority);
                List<String> pathSegments = uri.getPathSegments();
                if (pathSegments != null) {
                    int size = pathSegments.size();
                    if (size == 1) {
                        try {
                            i = Integer.parseInt(pathSegments.get(0));
                        } catch (NumberFormatException e) {
                            throw new FileNotFoundException("Single path segment is not a resource ID: " + uri);
                        }
                    } else if (size == 2) {
                        i = resourcesForApplication.getIdentifier(pathSegments.get(1), pathSegments.get(0), authority);
                    } else {
                        throw new FileNotFoundException("More than two path segments: " + uri);
                    }
                    if (i != 0) {
                        return resourcesForApplication.getDrawable(i);
                    }
                    throw new FileNotFoundException("No resource found for: " + uri);
                }
                throw new FileNotFoundException("No path: " + uri);
            } catch (PackageManager.NameNotFoundException e2) {
                throw new FileNotFoundException("No package found for authority: " + uri);
            }
        } else {
            throw new FileNotFoundException("No authority: " + uri);
        }
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        try {
            return super.getDropDownView(position, convertView, parent);
        } catch (RuntimeException e) {
            Log.w(LOG_TAG, "Search suggestions cursor threw exception.", e);
            View newDropDownView = newDropDownView(this.mProviderContext, getCursor(), parent);
            if (newDropDownView != null) {
                ((ChildViewCache) newDropDownView.getTag()).mText1.setText(e.toString());
            }
            return newDropDownView;
        }
    }

    public int getQueryRefinement() {
        return this.mQueryRefinement;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            return super.getView(position, convertView, parent);
        } catch (RuntimeException e) {
            Log.w(LOG_TAG, "Search suggestions cursor threw exception.", e);
            View newView = newView(this.mProviderContext, getCursor(), parent);
            if (newView != null) {
                ((ChildViewCache) newView.getTag()).mText1.setText(e.toString());
            }
            return newView;
        }
    }

    public boolean hasStableIds() {
        return false;
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View newView = super.newView(context, cursor, parent);
        newView.setTag(new ChildViewCache(newView));
        ((ImageView) newView.findViewById(R.id.edit_query)).setImageResource(this.mCommitIconResId);
        return newView;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        updateSpinnerState(getCursor());
    }

    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
        updateSpinnerState(getCursor());
    }

    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag instanceof CharSequence) {
            this.mSearchView.onQueryRefine((CharSequence) tag);
        }
    }

    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        String obj = constraint == null ? HttpUrl.FRAGMENT_ENCODE_SET : constraint.toString();
        if (this.mSearchView.getVisibility() != 0 || this.mSearchView.getWindowVisibility() != 0) {
            return null;
        }
        try {
            Cursor searchManagerSuggestions = getSearchManagerSuggestions(this.mSearchable, obj, 50);
            if (searchManagerSuggestions != null) {
                searchManagerSuggestions.getCount();
                return searchManagerSuggestions;
            }
        } catch (RuntimeException e) {
            Log.w(LOG_TAG, "Search suggestions query threw an exception.", e);
        }
        return null;
    }

    public void setQueryRefinement(int refineWhat) {
        this.mQueryRefinement = refineWhat;
    }

    public void bindView(View view, Context context, Cursor cursor) {
        CharSequence charSequence;
        ChildViewCache childViewCache = (ChildViewCache) view.getTag();
        int i = 0;
        int i2 = this.mFlagsCol;
        if (i2 != -1) {
            i = cursor.getInt(i2);
        }
        if (childViewCache.mText1 != null) {
            String stringOrNull = getStringOrNull(cursor, this.mText1Col);
            Log1F380D.a((Object) stringOrNull);
            setViewText(childViewCache.mText1, stringOrNull);
        }
        if (childViewCache.mText2 != null) {
            String stringOrNull2 = getStringOrNull(cursor, this.mText2UrlCol);
            Log1F380D.a((Object) stringOrNull2);
            if (stringOrNull2 != null) {
                charSequence = formatUrl(stringOrNull2);
            } else {
                charSequence = getStringOrNull(cursor, this.mText2Col);
                Log1F380D.a((Object) charSequence);
            }
            if (TextUtils.isEmpty(charSequence)) {
                if (childViewCache.mText1 != null) {
                    childViewCache.mText1.setSingleLine(false);
                    childViewCache.mText1.setMaxLines(2);
                }
            } else if (childViewCache.mText1 != null) {
                childViewCache.mText1.setSingleLine(true);
                childViewCache.mText1.setMaxLines(1);
            }
            setViewText(childViewCache.mText2, charSequence);
        }
        if (childViewCache.mIcon1 != null) {
            setViewDrawable(childViewCache.mIcon1, getIcon1(cursor), 4);
        }
        if (childViewCache.mIcon2 != null) {
            setViewDrawable(childViewCache.mIcon2, getIcon2(cursor), 8);
        }
        int i3 = this.mQueryRefinement;
        if (i3 == 2 || (i3 == 1 && (i & 1) != 0)) {
            childViewCache.mIconRefine.setVisibility(0);
            childViewCache.mIconRefine.setTag(childViewCache.mText1.getText());
            childViewCache.mIconRefine.setOnClickListener(this);
            return;
        }
        childViewCache.mIconRefine.setVisibility(8);
    }

    public CharSequence convertToString(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        String columnString = getColumnString(cursor, "suggest_intent_query");
        Log1F380D.a((Object) columnString);
        if (columnString != null) {
            return columnString;
        }
        if (this.mSearchable.shouldRewriteQueryFromData()) {
            String columnString2 = getColumnString(cursor, "suggest_intent_data");
            Log1F380D.a((Object) columnString2);
            if (columnString2 != null) {
                return columnString2;
            }
        }
        if (this.mSearchable.shouldRewriteQueryFromText()) {
            String columnString3 = getColumnString(cursor, "suggest_text_1");
            Log1F380D.a((Object) columnString3);
            if (columnString3 != null) {
                return columnString3;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public Cursor getSearchManagerSuggestions(SearchableInfo searchable, String query, int limit) {
        String suggestAuthority;
        String[] strArr;
        if (searchable == null || (suggestAuthority = searchable.getSuggestAuthority()) == null) {
            return null;
        }
        Uri.Builder fragment = new Uri.Builder().scheme("content").authority(suggestAuthority).query(HttpUrl.FRAGMENT_ENCODE_SET).fragment(HttpUrl.FRAGMENT_ENCODE_SET);
        String suggestPath = searchable.getSuggestPath();
        if (suggestPath != null) {
            fragment.appendEncodedPath(suggestPath);
        }
        fragment.appendPath("search_suggest_query");
        String suggestSelection = searchable.getSuggestSelection();
        if (suggestSelection != null) {
            strArr = new String[]{query};
        } else {
            fragment.appendPath(query);
            strArr = null;
        }
        if (limit > 0) {
            String valueOf = String.valueOf(limit);
            Log1F380D.a((Object) valueOf);
            fragment.appendQueryParameter("limit", valueOf);
        }
        return this.mProviderContext.getContentResolver().query(fragment.build(), (String[]) null, suggestSelection, strArr, (String) null);
    }
}
