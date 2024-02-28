package com.google.android.material.transition.platform;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.core.graphics.BlendModeColorFilterCompat;
import androidx.core.graphics.BlendModeCompat;
import com.google.android.material.R;
import com.google.android.material.internal.ContextUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class MaterialContainerTransformSharedElementCallback extends SharedElementCallback {
    /* access modifiers changed from: private */
    public static WeakReference<View> capturedSharedElement;
    private boolean entering = true;
    private Rect returnEndBounds;
    private ShapeProvider shapeProvider = new ShapeableViewShapeProvider();
    private boolean sharedElementReenterTransitionEnabled = false;
    private boolean transparentWindowBackgroundEnabled = true;

    public interface ShapeProvider {
        ShapeAppearanceModel provideShape(View view);
    }

    public static class ShapeableViewShapeProvider implements ShapeProvider {
        public ShapeAppearanceModel provideShape(View sharedElement) {
            if (sharedElement instanceof Shapeable) {
                return ((Shapeable) sharedElement).getShapeAppearanceModel();
            }
            return null;
        }
    }

    private static Drawable getWindowBackground(Window window) {
        return window.getDecorView().getBackground();
    }

    /* access modifiers changed from: private */
    public static void removeWindowBackground(Window window) {
        Drawable windowBackground = getWindowBackground(window);
        if (windowBackground != null) {
            windowBackground.mutate().setColorFilter(BlendModeColorFilterCompat.createBlendModeColorFilterCompat(0, BlendModeCompat.CLEAR));
        }
    }

    /* access modifiers changed from: private */
    public static void restoreWindowBackground(Window window) {
        Drawable windowBackground = getWindowBackground(window);
        if (windowBackground != null) {
            windowBackground.mutate().clearColorFilter();
        }
    }

    private void setUpEnterTransform(final Window window) {
        Transition sharedElementEnterTransition = window.getSharedElementEnterTransition();
        if (sharedElementEnterTransition instanceof MaterialContainerTransform) {
            MaterialContainerTransform materialContainerTransform = (MaterialContainerTransform) sharedElementEnterTransition;
            if (!this.sharedElementReenterTransitionEnabled) {
                window.setSharedElementReenterTransition((Transition) null);
            }
            if (this.transparentWindowBackgroundEnabled) {
                updateBackgroundFadeDuration(window, materialContainerTransform);
                materialContainerTransform.addListener(new TransitionListenerAdapter() {
                    public void onTransitionEnd(Transition transition) {
                        MaterialContainerTransformSharedElementCallback.restoreWindowBackground(window);
                    }

                    public void onTransitionStart(Transition transition) {
                        MaterialContainerTransformSharedElementCallback.removeWindowBackground(window);
                    }
                });
            }
        }
    }

    private void setUpReturnTransform(final Activity activity, final Window window) {
        Transition sharedElementReturnTransition = window.getSharedElementReturnTransition();
        if (sharedElementReturnTransition instanceof MaterialContainerTransform) {
            MaterialContainerTransform materialContainerTransform = (MaterialContainerTransform) sharedElementReturnTransition;
            materialContainerTransform.setHoldAtEndEnabled(true);
            materialContainerTransform.addListener(new TransitionListenerAdapter() {
                public void onTransitionEnd(Transition transition) {
                    View view;
                    if (!(MaterialContainerTransformSharedElementCallback.capturedSharedElement == null || (view = (View) MaterialContainerTransformSharedElementCallback.capturedSharedElement.get()) == null)) {
                        view.setAlpha(1.0f);
                        WeakReference unused = MaterialContainerTransformSharedElementCallback.capturedSharedElement = null;
                    }
                    activity.finish();
                    activity.overridePendingTransition(0, 0);
                }
            });
            if (this.transparentWindowBackgroundEnabled) {
                updateBackgroundFadeDuration(window, materialContainerTransform);
                materialContainerTransform.addListener(new TransitionListenerAdapter() {
                    public void onTransitionStart(Transition transition) {
                        MaterialContainerTransformSharedElementCallback.removeWindowBackground(window);
                    }
                });
            }
        }
    }

    private static void updateBackgroundFadeDuration(Window window, MaterialContainerTransform transform) {
        if (transform.getDuration() >= 0) {
            window.setTransitionBackgroundFadeDuration(transform.getDuration());
        }
    }

    public ShapeProvider getShapeProvider() {
        return this.shapeProvider;
    }

    public boolean isSharedElementReenterTransitionEnabled() {
        return this.sharedElementReenterTransitionEnabled;
    }

    public boolean isTransparentWindowBackgroundEnabled() {
        return this.transparentWindowBackgroundEnabled;
    }

    public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
        capturedSharedElement = new WeakReference<>(sharedElement);
        return super.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);
    }

    public View onCreateSnapshotView(Context context, Parcelable snapshot) {
        WeakReference<View> weakReference;
        View view;
        ShapeAppearanceModel provideShape;
        View onCreateSnapshotView = super.onCreateSnapshotView(context, snapshot);
        if (!(onCreateSnapshotView == null || (weakReference = capturedSharedElement) == null || this.shapeProvider == null || (view = (View) weakReference.get()) == null || (provideShape = this.shapeProvider.provideShape(view)) == null)) {
            onCreateSnapshotView.setTag(R.id.mtrl_motion_snapshot_view, provideShape);
        }
        return onCreateSnapshotView;
    }

    public void onMapSharedElements(List<String> list, Map<String, View> map) {
        View view;
        Activity activity;
        if (!list.isEmpty() && !map.isEmpty() && (view = map.get(list.get(0))) != null && (activity = ContextUtils.getActivity(view.getContext())) != null) {
            Window window = activity.getWindow();
            if (this.entering) {
                setUpEnterTransform(window);
            } else {
                setUpReturnTransform(activity, window);
            }
        }
    }

    public void onSharedElementEnd(List<String> list, List<View> list2, List<View> list3) {
        if (!list2.isEmpty() && (list2.get(0).getTag(R.id.mtrl_motion_snapshot_view) instanceof View)) {
            list2.get(0).setTag(R.id.mtrl_motion_snapshot_view, (Object) null);
        }
        if (!this.entering && !list2.isEmpty()) {
            this.returnEndBounds = TransitionUtils.getRelativeBoundsRect(list2.get(0));
        }
        this.entering = false;
    }

    public void onSharedElementStart(List<String> list, List<View> list2, List<View> list3) {
        if (!list2.isEmpty() && !list3.isEmpty()) {
            list2.get(0).setTag(R.id.mtrl_motion_snapshot_view, list3.get(0));
        }
        if (!this.entering && !list2.isEmpty() && this.returnEndBounds != null) {
            View view = list2.get(0);
            view.measure(View.MeasureSpec.makeMeasureSpec(this.returnEndBounds.width(), BasicMeasure.EXACTLY), View.MeasureSpec.makeMeasureSpec(this.returnEndBounds.height(), BasicMeasure.EXACTLY));
            view.layout(this.returnEndBounds.left, this.returnEndBounds.top, this.returnEndBounds.right, this.returnEndBounds.bottom);
        }
    }

    public void setShapeProvider(ShapeProvider shapeProvider2) {
        this.shapeProvider = shapeProvider2;
    }

    public void setSharedElementReenterTransitionEnabled(boolean sharedElementReenterTransitionEnabled2) {
        this.sharedElementReenterTransitionEnabled = sharedElementReenterTransitionEnabled2;
    }

    public void setTransparentWindowBackgroundEnabled(boolean transparentWindowBackgroundEnabled2) {
        this.transparentWindowBackgroundEnabled = transparentWindowBackgroundEnabled2;
    }
}
