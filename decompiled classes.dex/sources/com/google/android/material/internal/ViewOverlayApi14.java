package com.google.android.material.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.core.view.ViewCompat;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

class ViewOverlayApi14 implements ViewOverlayImpl {
    protected OverlayViewGroup overlayViewGroup;

    static class OverlayViewGroup extends ViewGroup {
        static Method invalidateChildInParentFastMethod;
        private boolean disposed;
        ArrayList<Drawable> drawables = null;
        ViewGroup hostView;
        View requestingView;
        ViewOverlayApi14 viewOverlay;

        static {
            Class<ViewGroup> cls = ViewGroup.class;
            try {
                invalidateChildInParentFastMethod = cls.getDeclaredMethod("invalidateChildInParentFast", new Class[]{Integer.TYPE, Integer.TYPE, Rect.class});
            } catch (NoSuchMethodException e) {
            }
        }

        OverlayViewGroup(Context context, ViewGroup hostView2, View requestingView2, ViewOverlayApi14 viewOverlay2) {
            super(context);
            this.hostView = hostView2;
            this.requestingView = requestingView2;
            setRight(hostView2.getWidth());
            setBottom(hostView2.getHeight());
            hostView2.addView(this);
            this.viewOverlay = viewOverlay2;
        }

        private void assertNotDisposed() {
            if (this.disposed) {
                throw new IllegalStateException("This overlay was disposed already. Please use a new one via ViewGroupUtils.getOverlay()");
            }
        }

        private void disposeIfEmpty() {
            if (getChildCount() == 0) {
                ArrayList<Drawable> arrayList = this.drawables;
                if (arrayList == null || arrayList.size() == 0) {
                    this.disposed = true;
                    this.hostView.removeView(this);
                }
            }
        }

        private void getOffset(int[] offset) {
            int[] iArr = new int[2];
            int[] iArr2 = new int[2];
            this.hostView.getLocationOnScreen(iArr);
            this.requestingView.getLocationOnScreen(iArr2);
            offset[0] = iArr2[0] - iArr[0];
            offset[1] = iArr2[1] - iArr[1];
        }

        public void add(Drawable drawable) {
            assertNotDisposed();
            if (this.drawables == null) {
                this.drawables = new ArrayList<>();
            }
            if (!this.drawables.contains(drawable)) {
                this.drawables.add(drawable);
                invalidate(drawable.getBounds());
                drawable.setCallback(this);
            }
        }

        public void add(View child) {
            assertNotDisposed();
            if (child.getParent() instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) child.getParent();
                if (!(viewGroup == this.hostView || viewGroup.getParent() == null || !ViewCompat.isAttachedToWindow(viewGroup))) {
                    int[] iArr = new int[2];
                    int[] iArr2 = new int[2];
                    viewGroup.getLocationOnScreen(iArr);
                    this.hostView.getLocationOnScreen(iArr2);
                    ViewCompat.offsetLeftAndRight(child, iArr[0] - iArr2[0]);
                    ViewCompat.offsetTopAndBottom(child, iArr[1] - iArr2[1]);
                }
                viewGroup.removeView(child);
                if (child.getParent() != null) {
                    viewGroup.removeView(child);
                }
            }
            super.addView(child);
        }

        /* access modifiers changed from: protected */
        public void dispatchDraw(Canvas canvas) {
            int[] iArr = new int[2];
            int[] iArr2 = new int[2];
            this.hostView.getLocationOnScreen(iArr);
            this.requestingView.getLocationOnScreen(iArr2);
            int i = 0;
            canvas.translate((float) (iArr2[0] - iArr[0]), (float) (iArr2[1] - iArr[1]));
            canvas.clipRect(new Rect(0, 0, this.requestingView.getWidth(), this.requestingView.getHeight()));
            super.dispatchDraw(canvas);
            ArrayList<Drawable> arrayList = this.drawables;
            if (arrayList != null) {
                i = arrayList.size();
            }
            for (int i2 = 0; i2 < i; i2++) {
                this.drawables.get(i2).draw(canvas);
            }
        }

        public boolean dispatchTouchEvent(MotionEvent ev) {
            return false;
        }

        public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
            if (this.hostView == null) {
                return null;
            }
            dirty.offset(location[0], location[1]);
            if (this.hostView != null) {
                location[0] = 0;
                location[1] = 0;
                int[] iArr = new int[2];
                getOffset(iArr);
                dirty.offset(iArr[0], iArr[1]);
                return super.invalidateChildInParent(location, dirty);
            }
            invalidate(dirty);
            return null;
        }

        /* access modifiers changed from: protected */
        public ViewParent invalidateChildInParentFast(int left, int top, Rect dirty) {
            if (this.hostView == null || invalidateChildInParentFastMethod == null) {
                return null;
            }
            try {
                getOffset(new int[2]);
                invalidateChildInParentFastMethod.invoke(this.hostView, new Object[]{Integer.valueOf(left), Integer.valueOf(top), dirty});
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (InvocationTargetException e2) {
                e2.printStackTrace();
                return null;
            }
        }

        public void invalidateDrawable(Drawable drawable) {
            invalidate(drawable.getBounds());
        }

        /* access modifiers changed from: protected */
        public void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        public void remove(Drawable drawable) {
            ArrayList<Drawable> arrayList = this.drawables;
            if (arrayList != null) {
                arrayList.remove(drawable);
                invalidate(drawable.getBounds());
                drawable.setCallback((Drawable.Callback) null);
                disposeIfEmpty();
            }
        }

        public void remove(View view) {
            super.removeView(view);
            disposeIfEmpty();
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
            r0 = r1.drawables;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean verifyDrawable(android.graphics.drawable.Drawable r2) {
            /*
                r1 = this;
                boolean r0 = super.verifyDrawable(r2)
                if (r0 != 0) goto L_0x0013
                java.util.ArrayList<android.graphics.drawable.Drawable> r0 = r1.drawables
                if (r0 == 0) goto L_0x0011
                boolean r0 = r0.contains(r2)
                if (r0 == 0) goto L_0x0011
                goto L_0x0013
            L_0x0011:
                r0 = 0
                goto L_0x0014
            L_0x0013:
                r0 = 1
            L_0x0014:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.internal.ViewOverlayApi14.OverlayViewGroup.verifyDrawable(android.graphics.drawable.Drawable):boolean");
        }
    }

    ViewOverlayApi14(Context context, ViewGroup hostView, View requestingView) {
        this.overlayViewGroup = new OverlayViewGroup(context, hostView, requestingView, this);
    }

    static ViewOverlayApi14 createFrom(View view) {
        ViewGroup contentView = ViewUtils.getContentView(view);
        if (contentView == null) {
            return null;
        }
        int childCount = contentView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = contentView.getChildAt(i);
            if (childAt instanceof OverlayViewGroup) {
                return ((OverlayViewGroup) childAt).viewOverlay;
            }
        }
        return new ViewGroupOverlayApi14(contentView.getContext(), contentView, view);
    }

    public void add(Drawable drawable) {
        this.overlayViewGroup.add(drawable);
    }

    public void remove(Drawable drawable) {
        this.overlayViewGroup.remove(drawable);
    }
}
