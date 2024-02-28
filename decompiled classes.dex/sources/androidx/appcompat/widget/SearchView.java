package androidx.appcompat.widget;

import android.app.PendingIntent;
import android.app.SearchableInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.view.CollapsibleActionView;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.core.view.ViewCompat;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.customview.view.AbsSavedState;
import java.lang.reflect.Method;
import java.util.WeakHashMap;
import mt.Log1F380D;
import okhttp3.HttpUrl;

/* compiled from: 000A */
public class SearchView extends LinearLayoutCompat implements CollapsibleActionView {
    static final boolean DBG = false;
    private static final String IME_OPTION_NO_MICROPHONE = "nm";
    static final String LOG_TAG = "SearchView";
    static final PreQAutoCompleteTextViewReflector PRE_API_29_HIDDEN_METHOD_INVOKER = (Build.VERSION.SDK_INT < 29 ? new PreQAutoCompleteTextViewReflector() : null);
    private Bundle mAppSearchData;
    private boolean mClearingFocus;
    final ImageView mCloseButton;
    private final ImageView mCollapsedIcon;
    private int mCollapsedImeOptions;
    private final CharSequence mDefaultQueryHint;
    private final View mDropDownAnchor;
    private boolean mExpandedInActionView;
    final ImageView mGoButton;
    private boolean mIconified;
    private boolean mIconifiedByDefault;
    private int mMaxWidth;
    private CharSequence mOldQueryText;
    private final View.OnClickListener mOnClickListener;
    private OnCloseListener mOnCloseListener;
    private final TextView.OnEditorActionListener mOnEditorActionListener;
    private final AdapterView.OnItemClickListener mOnItemClickListener;
    private final AdapterView.OnItemSelectedListener mOnItemSelectedListener;
    private OnQueryTextListener mOnQueryChangeListener;
    View.OnFocusChangeListener mOnQueryTextFocusChangeListener;
    private View.OnClickListener mOnSearchClickListener;
    private OnSuggestionListener mOnSuggestionListener;
    private final WeakHashMap<String, Drawable.ConstantState> mOutsideDrawablesCache;
    private CharSequence mQueryHint;
    private boolean mQueryRefinement;
    private Runnable mReleaseCursorRunnable;
    final ImageView mSearchButton;
    private final View mSearchEditFrame;
    private final Drawable mSearchHintIcon;
    private final View mSearchPlate;
    final SearchAutoComplete mSearchSrcTextView;
    private Rect mSearchSrcTextViewBounds;
    private Rect mSearchSrtTextViewBoundsExpanded;
    SearchableInfo mSearchable;
    private final View mSubmitArea;
    private boolean mSubmitButtonEnabled;
    private final int mSuggestionCommitIconResId;
    private final int mSuggestionRowLayout;
    CursorAdapter mSuggestionsAdapter;
    private int[] mTemp;
    private int[] mTemp2;
    View.OnKeyListener mTextKeyListener;
    private TextWatcher mTextWatcher;
    private UpdatableTouchDelegate mTouchDelegate;
    private final Runnable mUpdateDrawableStateRunnable;
    private CharSequence mUserQuery;
    private final Intent mVoiceAppSearchIntent;
    final ImageView mVoiceButton;
    private boolean mVoiceButtonEnabled;
    private final Intent mVoiceWebSearchIntent;

    static class Api29Impl {
        private Api29Impl() {
        }

        static void refreshAutoCompleteResults(AutoCompleteTextView autoCompleteTextView) {
            autoCompleteTextView.refreshAutoCompleteResults();
        }

        static void setInputMethodMode(SearchAutoComplete searchAutoComplete, int mode) {
            searchAutoComplete.setInputMethodMode(mode);
        }
    }

    public interface OnCloseListener {
        boolean onClose();
    }

    public interface OnQueryTextListener {
        boolean onQueryTextChange(String str);

        boolean onQueryTextSubmit(String str);
    }

    public interface OnSuggestionListener {
        boolean onSuggestionClick(int i);

        boolean onSuggestionSelect(int i);
    }

    private static class PreQAutoCompleteTextViewReflector {
        private Method mDoAfterTextChanged = null;
        private Method mDoBeforeTextChanged = null;
        private Method mEnsureImeVisible = null;

        PreQAutoCompleteTextViewReflector() {
            preApi29Check();
            try {
                Method declaredMethod = AutoCompleteTextView.class.getDeclaredMethod("doBeforeTextChanged", new Class[0]);
                this.mDoBeforeTextChanged = declaredMethod;
                declaredMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
            }
            try {
                Method declaredMethod2 = AutoCompleteTextView.class.getDeclaredMethod("doAfterTextChanged", new Class[0]);
                this.mDoAfterTextChanged = declaredMethod2;
                declaredMethod2.setAccessible(true);
            } catch (NoSuchMethodException e2) {
            }
            Class<AutoCompleteTextView> cls = AutoCompleteTextView.class;
            try {
                Method method = cls.getMethod("ensureImeVisible", new Class[]{Boolean.TYPE});
                this.mEnsureImeVisible = method;
                method.setAccessible(true);
            } catch (NoSuchMethodException e3) {
            }
        }

        private static void preApi29Check() {
            if (Build.VERSION.SDK_INT >= 29) {
                throw new UnsupportedClassVersionError("This function can only be used for API Level < 29.");
            }
        }

        /* access modifiers changed from: package-private */
        public void doAfterTextChanged(AutoCompleteTextView view) {
            preApi29Check();
            Method method = this.mDoAfterTextChanged;
            if (method != null) {
                try {
                    method.invoke(view, new Object[0]);
                } catch (Exception e) {
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void doBeforeTextChanged(AutoCompleteTextView view) {
            preApi29Check();
            Method method = this.mDoBeforeTextChanged;
            if (method != null) {
                try {
                    method.invoke(view, new Object[0]);
                } catch (Exception e) {
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void ensureImeVisible(AutoCompleteTextView view) {
            preApi29Check();
            Method method = this.mEnsureImeVisible;
            if (method != null) {
                try {
                    method.invoke(view, new Object[]{true});
                } catch (Exception e) {
                }
            }
        }
    }

    /* compiled from: 0009 */
    static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, (ClassLoader) null);
            }

            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        boolean isIconified;

        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            this.isIconified = ((Boolean) source.readValue((ClassLoader) null)).booleanValue();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        public String toString() {
            StringBuilder append = new StringBuilder().append("SearchView.SavedState{");
            String hexString = Integer.toHexString(System.identityHashCode(this));
            Log1F380D.a((Object) hexString);
            return append.append(hexString).append(" isIconified=").append(this.isIconified).append("}").toString();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeValue(Boolean.valueOf(this.isIconified));
        }
    }

    public static class SearchAutoComplete extends AppCompatAutoCompleteTextView {
        private boolean mHasPendingShowSoftInputRequest;
        final Runnable mRunShowSoftInputIfNecessary;
        private SearchView mSearchView;
        private int mThreshold;

        public SearchAutoComplete(Context context) {
            this(context, (AttributeSet) null);
        }

        public SearchAutoComplete(Context context, AttributeSet attrs) {
            this(context, attrs, R.attr.autoCompleteTextViewStyle);
        }

        public SearchAutoComplete(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            this.mRunShowSoftInputIfNecessary = new Runnable() {
                public void run() {
                    SearchAutoComplete.this.showSoftInputIfNecessary();
                }
            };
            this.mThreshold = getThreshold();
        }

        private int getSearchViewTextMinWidthDp() {
            Configuration configuration = getResources().getConfiguration();
            int i = configuration.screenWidthDp;
            int i2 = configuration.screenHeightDp;
            if (i >= 960 && i2 >= 720 && configuration.orientation == 2) {
                return 256;
            }
            if (i < 600) {
                return (i < 640 || i2 < 480) ? 160 : 192;
            }
            return 192;
        }

        public boolean enoughToFilter() {
            return this.mThreshold <= 0 || super.enoughToFilter();
        }

        /* access modifiers changed from: package-private */
        public void ensureImeVisible() {
            if (Build.VERSION.SDK_INT >= 29) {
                Api29Impl.setInputMethodMode(this, 1);
                if (enoughToFilter()) {
                    showDropDown();
                    return;
                }
                return;
            }
            SearchView.PRE_API_29_HIDDEN_METHOD_INVOKER.ensureImeVisible(this);
        }

        /* access modifiers changed from: package-private */
        public boolean isEmpty() {
            return TextUtils.getTrimmedLength(getText()) == 0;
        }

        public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
            InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
            if (this.mHasPendingShowSoftInputRequest) {
                removeCallbacks(this.mRunShowSoftInputIfNecessary);
                post(this.mRunShowSoftInputIfNecessary);
            }
            return onCreateInputConnection;
        }

        /* access modifiers changed from: protected */
        public void onFinishInflate() {
            super.onFinishInflate();
            setMinWidth((int) TypedValue.applyDimension(1, (float) getSearchViewTextMinWidthDp(), getResources().getDisplayMetrics()));
        }

        /* access modifiers changed from: protected */
        public void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
            this.mSearchView.onTextFocusChanged();
        }

        public boolean onKeyPreIme(int keyCode, KeyEvent event) {
            if (keyCode == 4) {
                if (event.getAction() == 0 && event.getRepeatCount() == 0) {
                    KeyEvent.DispatcherState keyDispatcherState = getKeyDispatcherState();
                    if (keyDispatcherState != null) {
                        keyDispatcherState.startTracking(event, this);
                    }
                    return true;
                } else if (event.getAction() == 1) {
                    KeyEvent.DispatcherState keyDispatcherState2 = getKeyDispatcherState();
                    if (keyDispatcherState2 != null) {
                        keyDispatcherState2.handleUpEvent(event);
                    }
                    if (event.isTracking() && !event.isCanceled()) {
                        this.mSearchView.clearFocus();
                        setImeVisibility(false);
                        return true;
                    }
                }
            }
            return super.onKeyPreIme(keyCode, event);
        }

        public void onWindowFocusChanged(boolean hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);
            if (hasWindowFocus && this.mSearchView.hasFocus() && getVisibility() == 0) {
                this.mHasPendingShowSoftInputRequest = true;
                if (SearchView.isLandscapeMode(getContext())) {
                    ensureImeVisible();
                }
            }
        }

        public void performCompletion() {
        }

        /* access modifiers changed from: protected */
        public void replaceText(CharSequence text) {
        }

        /* access modifiers changed from: package-private */
        public void setImeVisibility(boolean visible) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService("input_method");
            if (!visible) {
                this.mHasPendingShowSoftInputRequest = false;
                removeCallbacks(this.mRunShowSoftInputIfNecessary);
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            } else if (inputMethodManager.isActive(this)) {
                this.mHasPendingShowSoftInputRequest = false;
                removeCallbacks(this.mRunShowSoftInputIfNecessary);
                inputMethodManager.showSoftInput(this, 0);
            } else {
                this.mHasPendingShowSoftInputRequest = true;
            }
        }

        /* access modifiers changed from: package-private */
        public void setSearchView(SearchView searchView) {
            this.mSearchView = searchView;
        }

        public void setThreshold(int threshold) {
            super.setThreshold(threshold);
            this.mThreshold = threshold;
        }

        /* access modifiers changed from: package-private */
        public void showSoftInputIfNecessary() {
            if (this.mHasPendingShowSoftInputRequest) {
                ((InputMethodManager) getContext().getSystemService("input_method")).showSoftInput(this, 0);
                this.mHasPendingShowSoftInputRequest = false;
            }
        }
    }

    private static class UpdatableTouchDelegate extends TouchDelegate {
        private final Rect mActualBounds = new Rect();
        private boolean mDelegateTargeted;
        private final View mDelegateView;
        private final int mSlop;
        private final Rect mSlopBounds = new Rect();
        private final Rect mTargetBounds = new Rect();

        public UpdatableTouchDelegate(Rect targetBounds, Rect actualBounds, View delegateView) {
            super(targetBounds, delegateView);
            this.mSlop = ViewConfiguration.get(delegateView.getContext()).getScaledTouchSlop();
            setBounds(targetBounds, actualBounds);
            this.mDelegateView = delegateView;
        }

        public boolean onTouchEvent(MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            boolean z = false;
            boolean z2 = true;
            switch (event.getAction()) {
                case 0:
                    if (this.mTargetBounds.contains(x, y)) {
                        this.mDelegateTargeted = true;
                        z = true;
                        break;
                    }
                    break;
                case 1:
                case 2:
                    z = this.mDelegateTargeted;
                    if (z && !this.mSlopBounds.contains(x, y)) {
                        z2 = false;
                        break;
                    }
                case 3:
                    z = this.mDelegateTargeted;
                    this.mDelegateTargeted = false;
                    break;
            }
            if (!z) {
                return false;
            }
            if (!z2 || this.mActualBounds.contains(x, y)) {
                event.setLocation((float) (x - this.mActualBounds.left), (float) (y - this.mActualBounds.top));
            } else {
                event.setLocation((float) (this.mDelegateView.getWidth() / 2), (float) (this.mDelegateView.getHeight() / 2));
            }
            return this.mDelegateView.dispatchTouchEvent(event);
        }

        public void setBounds(Rect desiredBounds, Rect actualBounds) {
            this.mTargetBounds.set(desiredBounds);
            this.mSlopBounds.set(desiredBounds);
            Rect rect = this.mSlopBounds;
            int i = this.mSlop;
            rect.inset(-i, -i);
            this.mActualBounds.set(actualBounds);
        }
    }

    public SearchView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.searchViewStyle);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mSearchSrcTextViewBounds = new Rect();
        this.mSearchSrtTextViewBoundsExpanded = new Rect();
        this.mTemp = new int[2];
        this.mTemp2 = new int[2];
        this.mUpdateDrawableStateRunnable = new Runnable() {
            public void run() {
                SearchView.this.updateFocusedState();
            }
        };
        this.mReleaseCursorRunnable = new Runnable() {
            public void run() {
                if (SearchView.this.mSuggestionsAdapter instanceof SuggestionsAdapter) {
                    SearchView.this.mSuggestionsAdapter.changeCursor((Cursor) null);
                }
            }
        };
        this.mOutsideDrawablesCache = new WeakHashMap<>();
        AnonymousClass5 r8 = new View.OnClickListener() {
            public void onClick(View v) {
                if (v == SearchView.this.mSearchButton) {
                    SearchView.this.onSearchClicked();
                } else if (v == SearchView.this.mCloseButton) {
                    SearchView.this.onCloseClicked();
                } else if (v == SearchView.this.mGoButton) {
                    SearchView.this.onSubmitQuery();
                } else if (v == SearchView.this.mVoiceButton) {
                    SearchView.this.onVoiceClicked();
                } else if (v == SearchView.this.mSearchSrcTextView) {
                    SearchView.this.forceSuggestionQuery();
                }
            }
        };
        this.mOnClickListener = r8;
        this.mTextKeyListener = new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (SearchView.this.mSearchable == null) {
                    return false;
                }
                if (SearchView.this.mSearchSrcTextView.isPopupShowing() && SearchView.this.mSearchSrcTextView.getListSelection() != -1) {
                    return SearchView.this.onSuggestionsKey(v, keyCode, event);
                }
                if (SearchView.this.mSearchSrcTextView.isEmpty() || !event.hasNoModifiers() || event.getAction() != 1 || keyCode != 66) {
                    return false;
                }
                v.cancelLongPress();
                SearchView searchView = SearchView.this;
                searchView.launchQuerySearch(0, (String) null, searchView.mSearchSrcTextView.getText().toString());
                return true;
            }
        };
        AnonymousClass7 r9 = new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                SearchView.this.onSubmitQuery();
                return true;
            }
        };
        this.mOnEditorActionListener = r9;
        AnonymousClass8 r10 = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                SearchView.this.onItemClicked(position, 0, (String) null);
            }
        };
        this.mOnItemClickListener = r10;
        AnonymousClass9 r11 = new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                SearchView.this.onItemSelected(position);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };
        this.mOnItemSelectedListener = r11;
        this.mTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int before, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int after) {
                SearchView.this.onTextChanged(s);
            }
        };
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.SearchView, defStyleAttr, 0);
        TintTypedArray tintTypedArray = obtainStyledAttributes;
        ViewCompat.saveAttributeDataForStyleable(this, context, R.styleable.SearchView, attrs, obtainStyledAttributes.getWrappedTypeArray(), defStyleAttr, 0);
        LayoutInflater from = LayoutInflater.from(context);
        int resourceId = tintTypedArray.getResourceId(R.styleable.SearchView_layout, R.layout.abc_search_view);
        from.inflate(resourceId, this, true);
        SearchAutoComplete searchAutoComplete = (SearchAutoComplete) findViewById(R.id.search_src_text);
        this.mSearchSrcTextView = searchAutoComplete;
        searchAutoComplete.setSearchView(this);
        this.mSearchEditFrame = findViewById(R.id.search_edit_frame);
        View findViewById = findViewById(R.id.search_plate);
        this.mSearchPlate = findViewById;
        View findViewById2 = findViewById(R.id.submit_area);
        this.mSubmitArea = findViewById2;
        ImageView imageView = (ImageView) findViewById(R.id.search_button);
        this.mSearchButton = imageView;
        ImageView imageView2 = (ImageView) findViewById(R.id.search_go_btn);
        this.mGoButton = imageView2;
        LayoutInflater layoutInflater = from;
        ImageView imageView3 = (ImageView) findViewById(R.id.search_close_btn);
        this.mCloseButton = imageView3;
        int i = resourceId;
        ImageView imageView4 = (ImageView) findViewById(R.id.search_voice_btn);
        this.mVoiceButton = imageView4;
        ImageView imageView5 = (ImageView) findViewById(R.id.search_mag_icon);
        this.mCollapsedIcon = imageView5;
        ViewCompat.setBackground(findViewById, tintTypedArray.getDrawable(R.styleable.SearchView_queryBackground));
        ViewCompat.setBackground(findViewById2, tintTypedArray.getDrawable(R.styleable.SearchView_submitBackground));
        imageView.setImageDrawable(tintTypedArray.getDrawable(R.styleable.SearchView_searchIcon));
        imageView2.setImageDrawable(tintTypedArray.getDrawable(R.styleable.SearchView_goIcon));
        imageView3.setImageDrawable(tintTypedArray.getDrawable(R.styleable.SearchView_closeIcon));
        imageView4.setImageDrawable(tintTypedArray.getDrawable(R.styleable.SearchView_voiceIcon));
        imageView5.setImageDrawable(tintTypedArray.getDrawable(R.styleable.SearchView_searchIcon));
        this.mSearchHintIcon = tintTypedArray.getDrawable(R.styleable.SearchView_searchHintIcon);
        TooltipCompat.setTooltipText(imageView, getResources().getString(R.string.abc_searchview_description_search));
        this.mSuggestionRowLayout = tintTypedArray.getResourceId(R.styleable.SearchView_suggestionRowLayout, R.layout.abc_search_dropdown_item_icons_2line);
        this.mSuggestionCommitIconResId = tintTypedArray.getResourceId(R.styleable.SearchView_commitIcon, 0);
        imageView.setOnClickListener(r8);
        imageView3.setOnClickListener(r8);
        imageView2.setOnClickListener(r8);
        imageView4.setOnClickListener(r8);
        searchAutoComplete.setOnClickListener(r8);
        searchAutoComplete.addTextChangedListener(this.mTextWatcher);
        searchAutoComplete.setOnEditorActionListener(r9);
        searchAutoComplete.setOnItemClickListener(r10);
        searchAutoComplete.setOnItemSelectedListener(r11);
        searchAutoComplete.setOnKeyListener(this.mTextKeyListener);
        searchAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (SearchView.this.mOnQueryTextFocusChangeListener != null) {
                    SearchView.this.mOnQueryTextFocusChangeListener.onFocusChange(SearchView.this, hasFocus);
                }
            }
        });
        setIconifiedByDefault(tintTypedArray.getBoolean(R.styleable.SearchView_iconifiedByDefault, true));
        int dimensionPixelSize = tintTypedArray.getDimensionPixelSize(R.styleable.SearchView_android_maxWidth, -1);
        if (dimensionPixelSize != -1) {
            setMaxWidth(dimensionPixelSize);
        }
        this.mDefaultQueryHint = tintTypedArray.getText(R.styleable.SearchView_defaultQueryHint);
        this.mQueryHint = tintTypedArray.getText(R.styleable.SearchView_queryHint);
        int i2 = tintTypedArray.getInt(R.styleable.SearchView_android_imeOptions, -1);
        if (i2 != -1) {
            setImeOptions(i2);
        }
        int i3 = tintTypedArray.getInt(R.styleable.SearchView_android_inputType, -1);
        if (i3 != -1) {
            setInputType(i3);
        }
        setFocusable(tintTypedArray.getBoolean(R.styleable.SearchView_android_focusable, true));
        tintTypedArray.recycle();
        Intent intent = new Intent("android.speech.action.WEB_SEARCH");
        this.mVoiceWebSearchIntent = intent;
        intent.addFlags(268435456);
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
        Intent intent2 = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        this.mVoiceAppSearchIntent = intent2;
        intent2.addFlags(268435456);
        View findViewById3 = findViewById(searchAutoComplete.getDropDownAnchor());
        this.mDropDownAnchor = findViewById3;
        if (findViewById3 != null) {
            findViewById3.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    SearchView.this.adjustDropDownSizeAndPosition();
                }
            });
        }
        updateViewsVisibility(this.mIconifiedByDefault);
        updateQueryHint();
    }

    private Intent createIntent(String action, Uri data, String extraData, String query, int actionKey, String actionMsg) {
        Intent intent = new Intent(action);
        intent.addFlags(268435456);
        if (data != null) {
            intent.setData(data);
        }
        intent.putExtra("user_query", this.mUserQuery);
        if (query != null) {
            intent.putExtra("query", query);
        }
        if (extraData != null) {
            intent.putExtra("intent_extra_data_key", extraData);
        }
        Bundle bundle = this.mAppSearchData;
        if (bundle != null) {
            intent.putExtra("app_data", bundle);
        }
        if (actionKey != 0) {
            intent.putExtra("action_key", actionKey);
            intent.putExtra("action_msg", actionMsg);
        }
        intent.setComponent(this.mSearchable.getSearchActivity());
        return intent;
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x005e A[Catch:{ RuntimeException -> 0x0082 }] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0060 A[Catch:{ RuntimeException -> 0x0082 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.content.Intent createIntentFromSuggestion(android.database.Cursor r11, int r12, java.lang.String r13) {
        /*
            r10 = this;
            r0 = 0
            java.lang.String r1 = "suggest_intent_action"
            java.lang.String r1 = androidx.appcompat.widget.SuggestionsAdapter.getColumnString(r11, r1)     // Catch:{ RuntimeException -> 0x0082 }
            mt.Log1F380D.a((java.lang.Object) r1)
            if (r1 != 0) goto L_0x0014
            android.app.SearchableInfo r2 = r10.mSearchable     // Catch:{ RuntimeException -> 0x0082 }
            java.lang.String r2 = r2.getSuggestIntentAction()     // Catch:{ RuntimeException -> 0x0082 }
            r1 = r2
        L_0x0014:
            if (r1 != 0) goto L_0x0019
            java.lang.String r2 = "android.intent.action.SEARCH"
            r1 = r2
        L_0x0019:
            java.lang.String r2 = "suggest_intent_data"
            java.lang.String r2 = androidx.appcompat.widget.SuggestionsAdapter.getColumnString(r11, r2)     // Catch:{ RuntimeException -> 0x0082 }
            mt.Log1F380D.a((java.lang.Object) r2)
            if (r2 != 0) goto L_0x002c
            android.app.SearchableInfo r3 = r10.mSearchable     // Catch:{ RuntimeException -> 0x0082 }
            java.lang.String r3 = r3.getSuggestIntentData()     // Catch:{ RuntimeException -> 0x0082 }
            r2 = r3
        L_0x002c:
            if (r2 == 0) goto L_0x005b
            java.lang.String r3 = "suggest_intent_data_id"
            java.lang.String r3 = androidx.appcompat.widget.SuggestionsAdapter.getColumnString(r11, r3)     // Catch:{ RuntimeException -> 0x0082 }
            mt.Log1F380D.a((java.lang.Object) r3)
            if (r3 == 0) goto L_0x005b
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ RuntimeException -> 0x0082 }
            r4.<init>()     // Catch:{ RuntimeException -> 0x0082 }
            java.lang.StringBuilder r4 = r4.append(r2)     // Catch:{ RuntimeException -> 0x0082 }
            java.lang.String r5 = "/"
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ RuntimeException -> 0x0082 }
            java.lang.String r5 = android.net.Uri.encode(r3)     // Catch:{ RuntimeException -> 0x0082 }
            mt.Log1F380D.a((java.lang.Object) r5)
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ RuntimeException -> 0x0082 }
            java.lang.String r4 = r4.toString()     // Catch:{ RuntimeException -> 0x0082 }
            r2 = r4
            r9 = r2
            goto L_0x005c
        L_0x005b:
            r9 = r2
        L_0x005c:
            if (r9 != 0) goto L_0x0060
            r4 = r0
            goto L_0x0065
        L_0x0060:
            android.net.Uri r2 = android.net.Uri.parse(r9)     // Catch:{ RuntimeException -> 0x0082 }
            r4 = r2
        L_0x0065:
            java.lang.String r2 = "suggest_intent_query"
            java.lang.String r6 = androidx.appcompat.widget.SuggestionsAdapter.getColumnString(r11, r2)     // Catch:{ RuntimeException -> 0x0082 }
            mt.Log1F380D.a((java.lang.Object) r6)
            java.lang.String r2 = "suggest_intent_extra_data"
            java.lang.String r5 = androidx.appcompat.widget.SuggestionsAdapter.getColumnString(r11, r2)     // Catch:{ RuntimeException -> 0x0082 }
            mt.Log1F380D.a((java.lang.Object) r5)
            r2 = r10
            r3 = r1
            r7 = r12
            r8 = r13
            android.content.Intent r0 = r2.createIntent(r3, r4, r5, r6, r7, r8)     // Catch:{ RuntimeException -> 0x0082 }
            return r0
        L_0x0082:
            r1 = move-exception
            int r2 = r11.getPosition()     // Catch:{ RuntimeException -> 0x0088 }
            goto L_0x008b
        L_0x0088:
            r2 = move-exception
            r3 = -1
            r2 = r3
        L_0x008b:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Search suggestions cursor at row "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r2)
            java.lang.String r4 = " returned exception."
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            java.lang.String r4 = "SearchView"
            android.util.Log.w(r4, r3, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.SearchView.createIntentFromSuggestion(android.database.Cursor, int, java.lang.String):android.content.Intent");
    }

    private Intent createVoiceAppSearchIntent(Intent baseIntent, SearchableInfo searchable) {
        ComponentName searchActivity = searchable.getSearchActivity();
        Intent intent = new Intent("android.intent.action.SEARCH");
        intent.setComponent(searchActivity);
        PendingIntent activity = PendingIntent.getActivity(getContext(), 0, intent, 1107296256);
        Bundle bundle = new Bundle();
        Bundle bundle2 = this.mAppSearchData;
        if (bundle2 != null) {
            bundle.putParcelable("app_data", bundle2);
        }
        Intent intent2 = new Intent(baseIntent);
        String str = "free_form";
        String str2 = null;
        String str3 = null;
        int i = 1;
        Resources resources = getResources();
        if (searchable.getVoiceLanguageModeId() != 0) {
            str = resources.getString(searchable.getVoiceLanguageModeId());
        }
        if (searchable.getVoicePromptTextId() != 0) {
            str2 = resources.getString(searchable.getVoicePromptTextId());
        }
        if (searchable.getVoiceLanguageId() != 0) {
            str3 = resources.getString(searchable.getVoiceLanguageId());
        }
        if (searchable.getVoiceMaxResults() != 0) {
            i = searchable.getVoiceMaxResults();
        }
        intent2.putExtra("android.speech.extra.LANGUAGE_MODEL", str);
        intent2.putExtra("android.speech.extra.PROMPT", str2);
        intent2.putExtra("android.speech.extra.LANGUAGE", str3);
        intent2.putExtra("android.speech.extra.MAX_RESULTS", i);
        intent2.putExtra("calling_package", searchActivity == null ? null : searchActivity.flattenToShortString());
        intent2.putExtra("android.speech.extra.RESULTS_PENDINGINTENT", activity);
        intent2.putExtra("android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE", bundle);
        return intent2;
    }

    private Intent createVoiceWebSearchIntent(Intent baseIntent, SearchableInfo searchable) {
        Intent intent = new Intent(baseIntent);
        ComponentName searchActivity = searchable.getSearchActivity();
        intent.putExtra("calling_package", searchActivity == null ? null : searchActivity.flattenToShortString());
        return intent;
    }

    private void dismissSuggestions() {
        this.mSearchSrcTextView.dismissDropDown();
    }

    private void getChildBoundsWithinSearchView(View view, Rect rect) {
        view.getLocationInWindow(this.mTemp);
        getLocationInWindow(this.mTemp2);
        int[] iArr = this.mTemp;
        int i = iArr[1];
        int[] iArr2 = this.mTemp2;
        int i2 = i - iArr2[1];
        int i3 = iArr[0] - iArr2[0];
        rect.set(i3, i2, view.getWidth() + i3, view.getHeight() + i2);
    }

    private CharSequence getDecoratedHint(CharSequence hintText) {
        if (!this.mIconifiedByDefault || this.mSearchHintIcon == null) {
            return hintText;
        }
        int textSize = (int) (((double) this.mSearchSrcTextView.getTextSize()) * 1.25d);
        this.mSearchHintIcon.setBounds(0, 0, textSize, textSize);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("   ");
        spannableStringBuilder.setSpan(new ImageSpan(this.mSearchHintIcon), 1, 2, 33);
        spannableStringBuilder.append(hintText);
        return spannableStringBuilder;
    }

    private int getPreferredHeight() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.abc_search_view_preferred_height);
    }

    private int getPreferredWidth() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.abc_search_view_preferred_width);
    }

    private boolean hasVoiceSearch() {
        SearchableInfo searchableInfo = this.mSearchable;
        if (searchableInfo != null && searchableInfo.getVoiceSearchEnabled()) {
            Intent intent = null;
            if (this.mSearchable.getVoiceSearchLaunchWebSearch()) {
                intent = this.mVoiceWebSearchIntent;
            } else if (this.mSearchable.getVoiceSearchLaunchRecognizer()) {
                intent = this.mVoiceAppSearchIntent;
            }
            return (intent == null || getContext().getPackageManager().resolveActivity(intent, 65536) == null) ? false : true;
        }
        return false;
    }

    static boolean isLandscapeMode(Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }

    private boolean isSubmitAreaEnabled() {
        return (this.mSubmitButtonEnabled || this.mVoiceButtonEnabled) && !isIconified();
    }

    private void launchIntent(Intent intent) {
        if (intent != null) {
            try {
                getContext().startActivity(intent);
            } catch (RuntimeException e) {
                Log.e(LOG_TAG, "Failed launch activity: " + intent, e);
            }
        }
    }

    private boolean launchSuggestion(int position, int actionKey, String actionMsg) {
        Cursor cursor = this.mSuggestionsAdapter.getCursor();
        if (cursor == null || !cursor.moveToPosition(position)) {
            return false;
        }
        launchIntent(createIntentFromSuggestion(cursor, actionKey, actionMsg));
        return true;
    }

    private void postUpdateFocusedState() {
        post(this.mUpdateDrawableStateRunnable);
    }

    private void rewriteQueryFromSuggestion(int position) {
        Editable text = this.mSearchSrcTextView.getText();
        Cursor cursor = this.mSuggestionsAdapter.getCursor();
        if (cursor != null) {
            if (cursor.moveToPosition(position)) {
                CharSequence convertToString = this.mSuggestionsAdapter.convertToString(cursor);
                if (convertToString != null) {
                    setQuery(convertToString);
                } else {
                    setQuery(text);
                }
            } else {
                setQuery(text);
            }
        }
    }

    private void setQuery(CharSequence query) {
        this.mSearchSrcTextView.setText(query);
        this.mSearchSrcTextView.setSelection(TextUtils.isEmpty(query) ? 0 : query.length());
    }

    private void updateCloseButton() {
        boolean z = true;
        boolean z2 = !TextUtils.isEmpty(this.mSearchSrcTextView.getText());
        int i = 0;
        if (!z2 && (!this.mIconifiedByDefault || this.mExpandedInActionView)) {
            z = false;
        }
        ImageView imageView = this.mCloseButton;
        if (!z) {
            i = 8;
        }
        imageView.setVisibility(i);
        Drawable drawable = this.mCloseButton.getDrawable();
        if (drawable != null) {
            drawable.setState(z2 ? ENABLED_STATE_SET : EMPTY_STATE_SET);
        }
    }

    private void updateQueryHint() {
        CharSequence queryHint = getQueryHint();
        this.mSearchSrcTextView.setHint(getDecoratedHint(queryHint == null ? HttpUrl.FRAGMENT_ENCODE_SET : queryHint));
    }

    private void updateSearchAutoComplete() {
        this.mSearchSrcTextView.setThreshold(this.mSearchable.getSuggestThreshold());
        this.mSearchSrcTextView.setImeOptions(this.mSearchable.getImeOptions());
        int inputType = this.mSearchable.getInputType();
        int i = 1;
        if ((inputType & 15) == 1) {
            inputType &= -65537;
            if (this.mSearchable.getSuggestAuthority() != null) {
                inputType = inputType | 65536 | 524288;
            }
        }
        this.mSearchSrcTextView.setInputType(inputType);
        CursorAdapter cursorAdapter = this.mSuggestionsAdapter;
        if (cursorAdapter != null) {
            cursorAdapter.changeCursor((Cursor) null);
        }
        if (this.mSearchable.getSuggestAuthority() != null) {
            SuggestionsAdapter suggestionsAdapter = new SuggestionsAdapter(getContext(), this, this.mSearchable, this.mOutsideDrawablesCache);
            this.mSuggestionsAdapter = suggestionsAdapter;
            this.mSearchSrcTextView.setAdapter(suggestionsAdapter);
            SuggestionsAdapter suggestionsAdapter2 = (SuggestionsAdapter) this.mSuggestionsAdapter;
            if (this.mQueryRefinement) {
                i = 2;
            }
            suggestionsAdapter2.setQueryRefinement(i);
        }
    }

    private void updateSubmitArea() {
        int i = 8;
        if (isSubmitAreaEnabled() && (this.mGoButton.getVisibility() == 0 || this.mVoiceButton.getVisibility() == 0)) {
            i = 0;
        }
        this.mSubmitArea.setVisibility(i);
    }

    private void updateSubmitButton(boolean hasText) {
        int i = 8;
        if (this.mSubmitButtonEnabled && isSubmitAreaEnabled() && hasFocus() && (hasText || !this.mVoiceButtonEnabled)) {
            i = 0;
        }
        this.mGoButton.setVisibility(i);
    }

    private void updateViewsVisibility(boolean collapsed) {
        this.mIconified = collapsed;
        int i = 8;
        boolean z = false;
        int i2 = collapsed ? 0 : 8;
        boolean z2 = !TextUtils.isEmpty(this.mSearchSrcTextView.getText());
        this.mSearchButton.setVisibility(i2);
        updateSubmitButton(z2);
        View view = this.mSearchEditFrame;
        if (!collapsed) {
            i = 0;
        }
        view.setVisibility(i);
        this.mCollapsedIcon.setVisibility((this.mCollapsedIcon.getDrawable() == null || this.mIconifiedByDefault) ? 8 : 0);
        updateCloseButton();
        if (!z2) {
            z = true;
        }
        updateVoiceButton(z);
        updateSubmitArea();
    }

    private void updateVoiceButton(boolean empty) {
        int i = 8;
        if (this.mVoiceButtonEnabled && !isIconified() && empty) {
            i = 0;
            this.mGoButton.setVisibility(8);
        }
        this.mVoiceButton.setVisibility(i);
    }

    /* access modifiers changed from: package-private */
    public void adjustDropDownSizeAndPosition() {
        if (this.mDropDownAnchor.getWidth() > 1) {
            Resources resources = getContext().getResources();
            int paddingLeft = this.mSearchPlate.getPaddingLeft();
            Rect rect = new Rect();
            boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
            int dimensionPixelSize = this.mIconifiedByDefault ? resources.getDimensionPixelSize(R.dimen.abc_dropdownitem_icon_width) + resources.getDimensionPixelSize(R.dimen.abc_dropdownitem_text_padding_left) : 0;
            this.mSearchSrcTextView.getDropDownBackground().getPadding(rect);
            this.mSearchSrcTextView.setDropDownHorizontalOffset(isLayoutRtl ? -rect.left : paddingLeft - (rect.left + dimensionPixelSize));
            this.mSearchSrcTextView.setDropDownWidth((((this.mDropDownAnchor.getWidth() + rect.left) + rect.right) + dimensionPixelSize) - paddingLeft);
        }
    }

    public void clearFocus() {
        this.mClearingFocus = true;
        super.clearFocus();
        this.mSearchSrcTextView.clearFocus();
        this.mSearchSrcTextView.setImeVisibility(false);
        this.mClearingFocus = false;
    }

    /* access modifiers changed from: package-private */
    public void forceSuggestionQuery() {
        if (Build.VERSION.SDK_INT >= 29) {
            Api29Impl.refreshAutoCompleteResults(this.mSearchSrcTextView);
            return;
        }
        PreQAutoCompleteTextViewReflector preQAutoCompleteTextViewReflector = PRE_API_29_HIDDEN_METHOD_INVOKER;
        preQAutoCompleteTextViewReflector.doBeforeTextChanged(this.mSearchSrcTextView);
        preQAutoCompleteTextViewReflector.doAfterTextChanged(this.mSearchSrcTextView);
    }

    public int getImeOptions() {
        return this.mSearchSrcTextView.getImeOptions();
    }

    public int getInputType() {
        return this.mSearchSrcTextView.getInputType();
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    public CharSequence getQuery() {
        return this.mSearchSrcTextView.getText();
    }

    public CharSequence getQueryHint() {
        if (this.mQueryHint != null) {
            return this.mQueryHint;
        }
        SearchableInfo searchableInfo = this.mSearchable;
        return (searchableInfo == null || searchableInfo.getHintId() == 0) ? this.mDefaultQueryHint : getContext().getText(this.mSearchable.getHintId());
    }

    /* access modifiers changed from: package-private */
    public int getSuggestionCommitIconResId() {
        return this.mSuggestionCommitIconResId;
    }

    /* access modifiers changed from: package-private */
    public int getSuggestionRowLayout() {
        return this.mSuggestionRowLayout;
    }

    public CursorAdapter getSuggestionsAdapter() {
        return this.mSuggestionsAdapter;
    }

    public boolean isIconfiedByDefault() {
        return this.mIconifiedByDefault;
    }

    public boolean isIconified() {
        return this.mIconified;
    }

    public boolean isQueryRefinementEnabled() {
        return this.mQueryRefinement;
    }

    public boolean isSubmitButtonEnabled() {
        return this.mSubmitButtonEnabled;
    }

    /* access modifiers changed from: package-private */
    public void launchQuerySearch(int actionKey, String actionMsg, String query) {
        getContext().startActivity(createIntent("android.intent.action.SEARCH", (Uri) null, (String) null, query, actionKey, actionMsg));
    }

    public void onActionViewCollapsed() {
        setQuery(HttpUrl.FRAGMENT_ENCODE_SET, false);
        clearFocus();
        updateViewsVisibility(true);
        this.mSearchSrcTextView.setImeOptions(this.mCollapsedImeOptions);
        this.mExpandedInActionView = false;
    }

    public void onActionViewExpanded() {
        if (!this.mExpandedInActionView) {
            this.mExpandedInActionView = true;
            int imeOptions = this.mSearchSrcTextView.getImeOptions();
            this.mCollapsedImeOptions = imeOptions;
            this.mSearchSrcTextView.setImeOptions(imeOptions | 33554432);
            this.mSearchSrcTextView.setText(HttpUrl.FRAGMENT_ENCODE_SET);
            setIconified(false);
        }
    }

    /* access modifiers changed from: package-private */
    public void onCloseClicked() {
        if (!TextUtils.isEmpty(this.mSearchSrcTextView.getText())) {
            this.mSearchSrcTextView.setText(HttpUrl.FRAGMENT_ENCODE_SET);
            this.mSearchSrcTextView.requestFocus();
            this.mSearchSrcTextView.setImeVisibility(true);
        } else if (this.mIconifiedByDefault) {
            OnCloseListener onCloseListener = this.mOnCloseListener;
            if (onCloseListener == null || !onCloseListener.onClose()) {
                clearFocus();
                updateViewsVisibility(true);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        removeCallbacks(this.mUpdateDrawableStateRunnable);
        post(this.mReleaseCursorRunnable);
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: package-private */
    public boolean onItemClicked(int position, int actionKey, String actionMsg) {
        OnSuggestionListener onSuggestionListener = this.mOnSuggestionListener;
        if (onSuggestionListener != null && onSuggestionListener.onSuggestionClick(position)) {
            return false;
        }
        launchSuggestion(position, 0, (String) null);
        this.mSearchSrcTextView.setImeVisibility(false);
        dismissSuggestions();
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean onItemSelected(int position) {
        OnSuggestionListener onSuggestionListener = this.mOnSuggestionListener;
        if (onSuggestionListener != null && onSuggestionListener.onSuggestionSelect(position)) {
            return false;
        }
        rewriteQueryFromSuggestion(position);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            getChildBoundsWithinSearchView(this.mSearchSrcTextView, this.mSearchSrcTextViewBounds);
            this.mSearchSrtTextViewBoundsExpanded.set(this.mSearchSrcTextViewBounds.left, 0, this.mSearchSrcTextViewBounds.right, bottom - top);
            UpdatableTouchDelegate updatableTouchDelegate = this.mTouchDelegate;
            if (updatableTouchDelegate == null) {
                UpdatableTouchDelegate updatableTouchDelegate2 = new UpdatableTouchDelegate(this.mSearchSrtTextViewBoundsExpanded, this.mSearchSrcTextViewBounds, this.mSearchSrcTextView);
                this.mTouchDelegate = updatableTouchDelegate2;
                setTouchDelegate(updatableTouchDelegate2);
                return;
            }
            updatableTouchDelegate.setBounds(this.mSearchSrtTextViewBoundsExpanded, this.mSearchSrcTextViewBounds);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isIconified()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int mode = View.MeasureSpec.getMode(widthMeasureSpec);
        int size = View.MeasureSpec.getSize(widthMeasureSpec);
        switch (mode) {
            case Integer.MIN_VALUE:
                int i = this.mMaxWidth;
                if (i <= 0) {
                    size = Math.min(getPreferredWidth(), size);
                    break;
                } else {
                    size = Math.min(i, size);
                    break;
                }
            case 0:
                int i2 = this.mMaxWidth;
                if (i2 <= 0) {
                    i2 = getPreferredWidth();
                }
                size = i2;
                break;
            case BasicMeasure.EXACTLY /*1073741824*/:
                int i3 = this.mMaxWidth;
                if (i3 > 0) {
                    size = Math.min(i3, size);
                    break;
                }
                break;
        }
        int mode2 = View.MeasureSpec.getMode(heightMeasureSpec);
        int size2 = View.MeasureSpec.getSize(heightMeasureSpec);
        switch (mode2) {
            case Integer.MIN_VALUE:
                size2 = Math.min(getPreferredHeight(), size2);
                break;
            case 0:
                size2 = getPreferredHeight();
                break;
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(size, BasicMeasure.EXACTLY), View.MeasureSpec.makeMeasureSpec(size2, BasicMeasure.EXACTLY));
    }

    /* access modifiers changed from: protected */
    public void onQueryRefine(CharSequence queryText) {
        setQuery(queryText);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        updateViewsVisibility(savedState.isIconified);
        requestLayout();
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.isIconified = isIconified();
        return savedState;
    }

    /* access modifiers changed from: package-private */
    public void onSearchClicked() {
        updateViewsVisibility(false);
        this.mSearchSrcTextView.requestFocus();
        this.mSearchSrcTextView.setImeVisibility(true);
        View.OnClickListener onClickListener = this.mOnSearchClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(this);
        }
    }

    /* access modifiers changed from: package-private */
    public void onSubmitQuery() {
        Editable text = this.mSearchSrcTextView.getText();
        if (text != null && TextUtils.getTrimmedLength(text) > 0) {
            OnQueryTextListener onQueryTextListener = this.mOnQueryChangeListener;
            if (onQueryTextListener == null || !onQueryTextListener.onQueryTextSubmit(text.toString())) {
                if (this.mSearchable != null) {
                    launchQuerySearch(0, (String) null, text.toString());
                }
                this.mSearchSrcTextView.setImeVisibility(false);
                dismissSuggestions();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean onSuggestionsKey(View v, int keyCode, KeyEvent event) {
        if (this.mSearchable != null && this.mSuggestionsAdapter != null && event.getAction() == 0 && event.hasNoModifiers()) {
            if (keyCode == 66 || keyCode == 84 || keyCode == 61) {
                return onItemClicked(this.mSearchSrcTextView.getListSelection(), 0, (String) null);
            }
            if (keyCode == 21 || keyCode == 22) {
                this.mSearchSrcTextView.setSelection(keyCode == 21 ? 0 : this.mSearchSrcTextView.length());
                this.mSearchSrcTextView.setListSelection(0);
                this.mSearchSrcTextView.clearListSelection();
                this.mSearchSrcTextView.ensureImeVisible();
                return true;
            } else if (keyCode == 19) {
                this.mSearchSrcTextView.getListSelection();
                return false;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void onTextChanged(CharSequence newText) {
        Editable text = this.mSearchSrcTextView.getText();
        this.mUserQuery = text;
        boolean z = true;
        boolean z2 = !TextUtils.isEmpty(text);
        updateSubmitButton(z2);
        if (z2) {
            z = false;
        }
        updateVoiceButton(z);
        updateCloseButton();
        updateSubmitArea();
        if (this.mOnQueryChangeListener != null && !TextUtils.equals(newText, this.mOldQueryText)) {
            this.mOnQueryChangeListener.onQueryTextChange(newText.toString());
        }
        this.mOldQueryText = newText.toString();
    }

    /* access modifiers changed from: package-private */
    public void onTextFocusChanged() {
        updateViewsVisibility(isIconified());
        postUpdateFocusedState();
        if (this.mSearchSrcTextView.hasFocus()) {
            forceSuggestionQuery();
        }
    }

    /* access modifiers changed from: package-private */
    public void onVoiceClicked() {
        if (this.mSearchable != null) {
            SearchableInfo searchableInfo = this.mSearchable;
            try {
                if (searchableInfo.getVoiceSearchLaunchWebSearch()) {
                    getContext().startActivity(createVoiceWebSearchIntent(this.mVoiceWebSearchIntent, searchableInfo));
                } else if (searchableInfo.getVoiceSearchLaunchRecognizer()) {
                    getContext().startActivity(createVoiceAppSearchIntent(this.mVoiceAppSearchIntent, searchableInfo));
                }
            } catch (ActivityNotFoundException e) {
                Log.w(LOG_TAG, "Could not find voice search activity");
            }
        }
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        postUpdateFocusedState();
    }

    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (this.mClearingFocus || !isFocusable()) {
            return false;
        }
        if (isIconified()) {
            return super.requestFocus(direction, previouslyFocusedRect);
        }
        boolean requestFocus = this.mSearchSrcTextView.requestFocus(direction, previouslyFocusedRect);
        if (requestFocus) {
            updateViewsVisibility(false);
        }
        return requestFocus;
    }

    public void setAppSearchData(Bundle appSearchData) {
        this.mAppSearchData = appSearchData;
    }

    public void setIconified(boolean iconify) {
        if (iconify) {
            onCloseClicked();
        } else {
            onSearchClicked();
        }
    }

    public void setIconifiedByDefault(boolean iconified) {
        if (this.mIconifiedByDefault != iconified) {
            this.mIconifiedByDefault = iconified;
            updateViewsVisibility(iconified);
            updateQueryHint();
        }
    }

    public void setImeOptions(int imeOptions) {
        this.mSearchSrcTextView.setImeOptions(imeOptions);
    }

    public void setInputType(int inputType) {
        this.mSearchSrcTextView.setInputType(inputType);
    }

    public void setMaxWidth(int maxpixels) {
        this.mMaxWidth = maxpixels;
        requestLayout();
    }

    public void setOnCloseListener(OnCloseListener listener) {
        this.mOnCloseListener = listener;
    }

    public void setOnQueryTextFocusChangeListener(View.OnFocusChangeListener listener) {
        this.mOnQueryTextFocusChangeListener = listener;
    }

    public void setOnQueryTextListener(OnQueryTextListener listener) {
        this.mOnQueryChangeListener = listener;
    }

    public void setOnSearchClickListener(View.OnClickListener listener) {
        this.mOnSearchClickListener = listener;
    }

    public void setOnSuggestionListener(OnSuggestionListener listener) {
        this.mOnSuggestionListener = listener;
    }

    public void setQuery(CharSequence query, boolean submit) {
        this.mSearchSrcTextView.setText(query);
        if (query != null) {
            SearchAutoComplete searchAutoComplete = this.mSearchSrcTextView;
            searchAutoComplete.setSelection(searchAutoComplete.length());
            this.mUserQuery = query;
        }
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    public void setQueryHint(CharSequence hint) {
        this.mQueryHint = hint;
        updateQueryHint();
    }

    public void setQueryRefinementEnabled(boolean enable) {
        this.mQueryRefinement = enable;
        CursorAdapter cursorAdapter = this.mSuggestionsAdapter;
        if (cursorAdapter instanceof SuggestionsAdapter) {
            ((SuggestionsAdapter) cursorAdapter).setQueryRefinement(enable ? 2 : 1);
        }
    }

    public void setSearchableInfo(SearchableInfo searchable) {
        this.mSearchable = searchable;
        if (searchable != null) {
            updateSearchAutoComplete();
            updateQueryHint();
        }
        boolean hasVoiceSearch = hasVoiceSearch();
        this.mVoiceButtonEnabled = hasVoiceSearch;
        if (hasVoiceSearch) {
            this.mSearchSrcTextView.setPrivateImeOptions(IME_OPTION_NO_MICROPHONE);
        }
        updateViewsVisibility(isIconified());
    }

    public void setSubmitButtonEnabled(boolean enabled) {
        this.mSubmitButtonEnabled = enabled;
        updateViewsVisibility(isIconified());
    }

    public void setSuggestionsAdapter(CursorAdapter adapter) {
        this.mSuggestionsAdapter = adapter;
        this.mSearchSrcTextView.setAdapter(adapter);
    }

    /* access modifiers changed from: package-private */
    public void updateFocusedState() {
        int[] iArr = this.mSearchSrcTextView.hasFocus() ? FOCUSED_STATE_SET : EMPTY_STATE_SET;
        Drawable background = this.mSearchPlate.getBackground();
        if (background != null) {
            background.setState(iArr);
        }
        Drawable background2 = this.mSubmitArea.getBackground();
        if (background2 != null) {
            background2.setState(iArr);
        }
        invalidate();
    }
}
