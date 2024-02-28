package androidx.core.view.inputmethod;

import android.content.ClipData;
import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.ContentInfoCompat;
import androidx.core.view.ViewCompat;

public final class InputConnectionCompat {
    private static final String COMMIT_CONTENT_ACTION = "androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_CONTENT_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_DESCRIPTION_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_FLAGS_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_FLAGS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_INTEROP_ACTION = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_LINK_URI_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_LINK_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_OPTS_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_OPTS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_RESULT_INTEROP_RECEIVER_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    private static final String COMMIT_CONTENT_RESULT_RECEIVER_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    private static final String EXTRA_INPUT_CONTENT_INFO = "androidx.core.view.extra.INPUT_CONTENT_INFO";
    public static final int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;
    private static final String LOG_TAG = "InputConnectionCompat";

    static class Api25Impl {
        private Api25Impl() {
        }

        static boolean commitContent(InputConnection inputConnection, InputContentInfo inputContentInfo, int i, Bundle bundle) {
            return inputConnection.commitContent(inputContentInfo, i, bundle);
        }
    }

    public interface OnCommitContentListener {
        boolean onCommitContent(InputContentInfoCompat inputContentInfoCompat, int i, Bundle bundle);
    }

    public static boolean commitContent(InputConnection inputConnection, EditorInfo editorInfo, InputContentInfoCompat inputContentInfo, int flags, Bundle opts) {
        boolean z;
        if (Build.VERSION.SDK_INT >= 25) {
            return Api25Impl.commitContent(inputConnection, (InputContentInfo) inputContentInfo.unwrap(), flags, opts);
        }
        switch (EditorInfoCompat.getProtocol(editorInfo)) {
            case 2:
                z = true;
                break;
            case 3:
            case 4:
                z = false;
                break;
            default:
                return false;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(z ? COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY : COMMIT_CONTENT_CONTENT_URI_KEY, inputContentInfo.getContentUri());
        bundle.putParcelable(z ? COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY : COMMIT_CONTENT_DESCRIPTION_KEY, inputContentInfo.getDescription());
        bundle.putParcelable(z ? COMMIT_CONTENT_LINK_URI_INTEROP_KEY : COMMIT_CONTENT_LINK_URI_KEY, inputContentInfo.getLinkUri());
        bundle.putInt(z ? COMMIT_CONTENT_FLAGS_INTEROP_KEY : COMMIT_CONTENT_FLAGS_KEY, flags);
        bundle.putParcelable(z ? COMMIT_CONTENT_OPTS_INTEROP_KEY : COMMIT_CONTENT_OPTS_KEY, opts);
        return inputConnection.performPrivateCommand(z ? COMMIT_CONTENT_INTEROP_ACTION : COMMIT_CONTENT_ACTION, bundle);
    }

    private static OnCommitContentListener createOnCommitContentListenerUsingPerformReceiveContent(View view) {
        Preconditions.checkNotNull(view);
        return new InputConnectionCompat$$ExternalSyntheticLambda0(view);
    }

    public static InputConnection createWrapper(View view, InputConnection inputConnection, EditorInfo editorInfo) {
        return createWrapper(inputConnection, editorInfo, createOnCommitContentListenerUsingPerformReceiveContent(view));
    }

    @Deprecated
    public static InputConnection createWrapper(InputConnection inputConnection, EditorInfo editorInfo, OnCommitContentListener onCommitContentListener) {
        ObjectsCompat.requireNonNull(inputConnection, "inputConnection must be non-null");
        ObjectsCompat.requireNonNull(editorInfo, "editorInfo must be non-null");
        ObjectsCompat.requireNonNull(onCommitContentListener, "onCommitContentListener must be non-null");
        if (Build.VERSION.SDK_INT >= 25) {
            final OnCommitContentListener onCommitContentListener2 = onCommitContentListener;
            return new InputConnectionWrapper(inputConnection, false) {
                public boolean commitContent(InputContentInfo inputContentInfo, int flags, Bundle opts) {
                    if (onCommitContentListener2.onCommitContent(InputContentInfoCompat.wrap(inputContentInfo), flags, opts)) {
                        return true;
                    }
                    return super.commitContent(inputContentInfo, flags, opts);
                }
            };
        } else if (EditorInfoCompat.getContentMimeTypes(editorInfo).length == 0) {
            return inputConnection;
        } else {
            final OnCommitContentListener onCommitContentListener3 = onCommitContentListener;
            return new InputConnectionWrapper(inputConnection, false) {
                public boolean performPrivateCommand(String action, Bundle data) {
                    if (InputConnectionCompat.handlePerformPrivateCommand(action, data, onCommitContentListener3)) {
                        return true;
                    }
                    return super.performPrivateCommand(action, data);
                }
            };
        }
    }

    static boolean handlePerformPrivateCommand(String action, Bundle data, OnCommitContentListener onCommitContentListener) {
        boolean z;
        int i = 0;
        if (data == null) {
            return false;
        }
        if (TextUtils.equals(COMMIT_CONTENT_ACTION, action)) {
            z = false;
        } else if (!TextUtils.equals(COMMIT_CONTENT_INTEROP_ACTION, action)) {
            return false;
        } else {
            z = true;
        }
        ResultReceiver resultReceiver = null;
        boolean z2 = false;
        try {
            ResultReceiver resultReceiver2 = (ResultReceiver) data.getParcelable(z ? COMMIT_CONTENT_RESULT_INTEROP_RECEIVER_KEY : COMMIT_CONTENT_RESULT_RECEIVER_KEY);
            Uri uri = (Uri) data.getParcelable(z ? COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY : COMMIT_CONTENT_CONTENT_URI_KEY);
            ClipDescription clipDescription = (ClipDescription) data.getParcelable(z ? COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY : COMMIT_CONTENT_DESCRIPTION_KEY);
            Uri uri2 = (Uri) data.getParcelable(z ? COMMIT_CONTENT_LINK_URI_INTEROP_KEY : COMMIT_CONTENT_LINK_URI_KEY);
            int i2 = data.getInt(z ? COMMIT_CONTENT_FLAGS_INTEROP_KEY : COMMIT_CONTENT_FLAGS_KEY);
            Bundle bundle = (Bundle) data.getParcelable(z ? COMMIT_CONTENT_OPTS_INTEROP_KEY : COMMIT_CONTENT_OPTS_KEY);
            if (!(uri == null || clipDescription == null)) {
                z2 = onCommitContentListener.onCommitContent(new InputContentInfoCompat(uri, clipDescription, uri2), i2, bundle);
            }
            if (resultReceiver2 != null) {
                if (z2) {
                    i = 1;
                }
                resultReceiver2.send(i, (Bundle) null);
            }
            return z2;
        } catch (Throwable th) {
            if (resultReceiver != null) {
                resultReceiver.send(0, (Bundle) null);
            }
            throw th;
        }
    }

    static /* synthetic */ boolean lambda$createOnCommitContentListenerUsingPerformReceiveContent$0(View view, InputContentInfoCompat inputContentInfo, int flags, Bundle opts) {
        Bundle bundle;
        Bundle bundle2 = opts;
        if (Build.VERSION.SDK_INT >= 25 && (flags & 1) != 0) {
            try {
                inputContentInfo.requestPermission();
                InputContentInfo inputContentInfo2 = (InputContentInfo) inputContentInfo.unwrap();
                if (opts != null) {
                    bundle = new Bundle(opts);
                }
                bundle2 = bundle;
                bundle2.putParcelable(EXTRA_INPUT_CONTENT_INFO, inputContentInfo2);
            } catch (Exception e) {
                Log.w(LOG_TAG, "Can't insert content from IME; requestPermission() failed", e);
                return false;
            }
        }
        return ViewCompat.performReceiveContent(view, new ContentInfoCompat.Builder(new ClipData(inputContentInfo.getDescription(), new ClipData.Item(inputContentInfo.getContentUri())), 2).setLinkUri(inputContentInfo.getLinkUri()).setExtras(bundle2).build()) == null;
    }
}
