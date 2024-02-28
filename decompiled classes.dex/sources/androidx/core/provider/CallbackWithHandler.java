package androidx.core.provider;

import android.graphics.Typeface;
import android.os.Handler;
import androidx.core.provider.FontRequestWorker;
import androidx.core.provider.FontsContractCompat;

class CallbackWithHandler {
    private final FontsContractCompat.FontRequestCallback mCallback;
    private final Handler mCallbackHandler;

    CallbackWithHandler(FontsContractCompat.FontRequestCallback callback) {
        this.mCallback = callback;
        this.mCallbackHandler = CalleeHandler.create();
    }

    CallbackWithHandler(FontsContractCompat.FontRequestCallback callback, Handler callbackHandler) {
        this.mCallback = callback;
        this.mCallbackHandler = callbackHandler;
    }

    private void onTypefaceRequestFailed(final int reason) {
        final FontsContractCompat.FontRequestCallback fontRequestCallback = this.mCallback;
        this.mCallbackHandler.post(new Runnable() {
            public void run() {
                fontRequestCallback.onTypefaceRequestFailed(reason);
            }
        });
    }

    private void onTypefaceRetrieved(final Typeface typeface) {
        final FontsContractCompat.FontRequestCallback fontRequestCallback = this.mCallback;
        this.mCallbackHandler.post(new Runnable() {
            public void run() {
                fontRequestCallback.onTypefaceRetrieved(typeface);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void onTypefaceResult(FontRequestWorker.TypefaceResult typefaceResult) {
        if (typefaceResult.isSuccess()) {
            onTypefaceRetrieved(typefaceResult.mTypeface);
        } else {
            onTypefaceRequestFailed(typefaceResult.mResult);
        }
    }
}
