package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import java.util.Map;

public class ChangeBounds extends Transition {
    private static final Property<View, PointF> BOTTOM_RIGHT_ONLY_PROPERTY = new Property<View, PointF>(PointF.class, "bottomRight") {
        public PointF get(View view) {
            return null;
        }

        public void set(View view, PointF bottomRight) {
            ViewUtils.setLeftTopRightBottom(view, view.getLeft(), view.getTop(), Math.round(bottomRight.x), Math.round(bottomRight.y));
        }
    };
    private static final Property<ViewBounds, PointF> BOTTOM_RIGHT_PROPERTY = new Property<ViewBounds, PointF>(PointF.class, "bottomRight") {
        public PointF get(ViewBounds viewBounds) {
            return null;
        }

        public void set(ViewBounds viewBounds, PointF bottomRight) {
            viewBounds.setBottomRight(bottomRight);
        }
    };
    private static final Property<Drawable, PointF> DRAWABLE_ORIGIN_PROPERTY = new Property<Drawable, PointF>(PointF.class, "boundsOrigin") {
        private Rect mBounds = new Rect();

        public PointF get(Drawable object) {
            object.copyBounds(this.mBounds);
            return new PointF((float) this.mBounds.left, (float) this.mBounds.top);
        }

        public void set(Drawable object, PointF value) {
            object.copyBounds(this.mBounds);
            this.mBounds.offsetTo(Math.round(value.x), Math.round(value.y));
            object.setBounds(this.mBounds);
        }
    };
    private static final Property<View, PointF> POSITION_PROPERTY = new Property<View, PointF>(PointF.class, "position") {
        public PointF get(View view) {
            return null;
        }

        public void set(View view, PointF topLeft) {
            int round = Math.round(topLeft.x);
            int round2 = Math.round(topLeft.y);
            ViewUtils.setLeftTopRightBottom(view, round, round2, view.getWidth() + round, view.getHeight() + round2);
        }
    };
    private static final String PROPNAME_BOUNDS = "android:changeBounds:bounds";
    private static final String PROPNAME_CLIP = "android:changeBounds:clip";
    private static final String PROPNAME_PARENT = "android:changeBounds:parent";
    private static final String PROPNAME_WINDOW_X = "android:changeBounds:windowX";
    private static final String PROPNAME_WINDOW_Y = "android:changeBounds:windowY";
    private static final Property<View, PointF> TOP_LEFT_ONLY_PROPERTY = new Property<View, PointF>(PointF.class, "topLeft") {
        public PointF get(View view) {
            return null;
        }

        public void set(View view, PointF topLeft) {
            ViewUtils.setLeftTopRightBottom(view, Math.round(topLeft.x), Math.round(topLeft.y), view.getRight(), view.getBottom());
        }
    };
    private static final Property<ViewBounds, PointF> TOP_LEFT_PROPERTY = new Property<ViewBounds, PointF>(PointF.class, "topLeft") {
        public PointF get(ViewBounds viewBounds) {
            return null;
        }

        public void set(ViewBounds viewBounds, PointF topLeft) {
            viewBounds.setTopLeft(topLeft);
        }
    };
    private static RectEvaluator sRectEvaluator = new RectEvaluator();
    private static final String[] sTransitionProperties = {PROPNAME_BOUNDS, PROPNAME_CLIP, PROPNAME_PARENT, PROPNAME_WINDOW_X, PROPNAME_WINDOW_Y};
    private boolean mReparent = false;
    private boolean mResizeClip = false;
    private int[] mTempLocation = new int[2];

    private static class ViewBounds {
        private int mBottom;
        private int mBottomRightCalls;
        private int mLeft;
        private int mRight;
        private int mTop;
        private int mTopLeftCalls;
        private View mView;

        ViewBounds(View view) {
            this.mView = view;
        }

        private void setLeftTopRightBottom() {
            ViewUtils.setLeftTopRightBottom(this.mView, this.mLeft, this.mTop, this.mRight, this.mBottom);
            this.mTopLeftCalls = 0;
            this.mBottomRightCalls = 0;
        }

        /* access modifiers changed from: package-private */
        public void setBottomRight(PointF bottomRight) {
            this.mRight = Math.round(bottomRight.x);
            this.mBottom = Math.round(bottomRight.y);
            int i = this.mBottomRightCalls + 1;
            this.mBottomRightCalls = i;
            if (this.mTopLeftCalls == i) {
                setLeftTopRightBottom();
            }
        }

        /* access modifiers changed from: package-private */
        public void setTopLeft(PointF topLeft) {
            this.mLeft = Math.round(topLeft.x);
            this.mTop = Math.round(topLeft.y);
            int i = this.mTopLeftCalls + 1;
            this.mTopLeftCalls = i;
            if (i == this.mBottomRightCalls) {
                setLeftTopRightBottom();
            }
        }
    }

    public ChangeBounds() {
    }

    public ChangeBounds(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, Styleable.CHANGE_BOUNDS);
        boolean namedBoolean = TypedArrayUtils.getNamedBoolean(obtainStyledAttributes, (XmlResourceParser) attrs, "resizeClip", 0, false);
        obtainStyledAttributes.recycle();
        setResizeClip(namedBoolean);
    }

    private void captureValues(TransitionValues values) {
        View view = values.view;
        if (ViewCompat.isLaidOut(view) || view.getWidth() != 0 || view.getHeight() != 0) {
            values.values.put(PROPNAME_BOUNDS, new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
            values.values.put(PROPNAME_PARENT, values.view.getParent());
            if (this.mReparent) {
                values.view.getLocationInWindow(this.mTempLocation);
                values.values.put(PROPNAME_WINDOW_X, Integer.valueOf(this.mTempLocation[0]));
                values.values.put(PROPNAME_WINDOW_Y, Integer.valueOf(this.mTempLocation[1]));
            }
            if (this.mResizeClip) {
                values.values.put(PROPNAME_CLIP, ViewCompat.getClipBounds(view));
            }
        }
    }

    private boolean parentMatches(View startParent, View endParent) {
        if (!this.mReparent) {
            return true;
        }
        boolean z = true;
        TransitionValues matchedTransitionValues = getMatchedTransitionValues(startParent, true);
        if (matchedTransitionValues == null) {
            if (startParent != endParent) {
                z = false;
            }
            return z;
        }
        if (endParent != matchedTransitionValues.view) {
            z = false;
        }
        return z;
    }

    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        boolean z;
        View view;
        Animator animator;
        int i;
        int i2;
        int i3;
        ObjectAnimator objectAnimator;
        Rect rect;
        int i4;
        Rect rect2;
        TransitionValues transitionValues = startValues;
        TransitionValues transitionValues2 = endValues;
        if (transitionValues == null) {
            ViewGroup viewGroup = sceneRoot;
            TransitionValues transitionValues3 = transitionValues2;
            return null;
        } else if (transitionValues2 == null) {
            ViewGroup viewGroup2 = sceneRoot;
            TransitionValues transitionValues4 = transitionValues2;
            return null;
        } else {
            Map<String, Object> map = transitionValues.values;
            Map<String, Object> map2 = transitionValues2.values;
            ViewGroup viewGroup3 = (ViewGroup) map.get(PROPNAME_PARENT);
            ViewGroup viewGroup4 = (ViewGroup) map2.get(PROPNAME_PARENT);
            if (viewGroup3 == null) {
                ViewGroup viewGroup5 = sceneRoot;
                Map<String, Object> map3 = map;
                Map<String, Object> map4 = map2;
                ViewGroup viewGroup6 = viewGroup3;
                ViewGroup viewGroup7 = viewGroup4;
                TransitionValues transitionValues5 = transitionValues2;
                return null;
            } else if (viewGroup4 == null) {
                ViewGroup viewGroup8 = sceneRoot;
                Map<String, Object> map5 = map;
                Map<String, Object> map6 = map2;
                ViewGroup viewGroup9 = viewGroup3;
                ViewGroup viewGroup10 = viewGroup4;
                TransitionValues transitionValues6 = transitionValues2;
                return null;
            } else {
                View view2 = transitionValues2.view;
                if (parentMatches(viewGroup3, viewGroup4)) {
                    Rect rect3 = (Rect) transitionValues.values.get(PROPNAME_BOUNDS);
                    Rect rect4 = (Rect) transitionValues2.values.get(PROPNAME_BOUNDS);
                    int i5 = rect3.left;
                    int i6 = rect4.left;
                    int i7 = rect3.top;
                    int i8 = rect4.top;
                    int i9 = rect3.right;
                    Map<String, Object> map7 = map;
                    int i10 = rect4.right;
                    Map<String, Object> map8 = map2;
                    int i11 = rect3.bottom;
                    ViewGroup viewGroup11 = viewGroup3;
                    int i12 = rect4.bottom;
                    ViewGroup viewGroup12 = viewGroup4;
                    int i13 = i9 - i5;
                    Rect rect5 = rect3;
                    int i14 = i11 - i7;
                    Rect rect6 = rect4;
                    int i15 = i10 - i6;
                    int i16 = i12 - i8;
                    View view3 = view2;
                    Rect rect7 = (Rect) transitionValues.values.get(PROPNAME_CLIP);
                    Rect rect8 = (Rect) transitionValues2.values.get(PROPNAME_CLIP);
                    int i17 = 0;
                    if (!((i13 == 0 || i14 == 0) && (i15 == 0 || i16 == 0))) {
                        if (!(i5 == i6 && i7 == i8)) {
                            i17 = 0 + 1;
                        }
                        if (!(i9 == i10 && i11 == i12)) {
                            i17++;
                        }
                    }
                    if ((rect7 != null && !rect7.equals(rect8)) || (rect7 == null && rect8 != null)) {
                        i17++;
                    }
                    if (i17 > 0) {
                        Rect rect9 = rect7;
                        Rect rect10 = rect8;
                        if (!this.mResizeClip) {
                            View view4 = view3;
                            ViewUtils.setLeftTopRightBottom(view4, i5, i7, i9, i11);
                            if (i17 != 2) {
                                int i18 = i16;
                                int i19 = i15;
                                int i20 = i14;
                                int i21 = i17;
                                int i22 = i13;
                                View view5 = view4;
                                if (i5 != i6) {
                                    view = view5;
                                } else if (i7 != i8) {
                                    view = view5;
                                } else {
                                    view = view5;
                                    animator = ObjectAnimatorUtils.ofPointF(view, BOTTOM_RIGHT_ONLY_PROPERTY, getPathMotion().getPath((float) i9, (float) i11, (float) i10, (float) i12));
                                    int i23 = i6;
                                    int i24 = i9;
                                    int i25 = i8;
                                    int i26 = i10;
                                    Rect rect11 = rect9;
                                    int i27 = i18;
                                    int i28 = i20;
                                    int i29 = i19;
                                    int i30 = i22;
                                    z = true;
                                    int i31 = i7;
                                    int i32 = i5;
                                    int i33 = i11;
                                }
                                animator = ObjectAnimatorUtils.ofPointF(view, TOP_LEFT_ONLY_PROPERTY, getPathMotion().getPath((float) i5, (float) i7, (float) i6, (float) i8));
                                int i34 = i6;
                                int i35 = i9;
                                int i36 = i8;
                                int i37 = i10;
                                Rect rect12 = rect9;
                                int i38 = i18;
                                int i39 = i20;
                                int i40 = i19;
                                int i41 = i22;
                                z = true;
                                int i42 = i7;
                                int i43 = i5;
                                int i44 = i11;
                            } else if (i13 == i15 && i14 == i16) {
                                int i45 = i17;
                                int i46 = i16;
                                animator = ObjectAnimatorUtils.ofPointF(view4, POSITION_PROPERTY, getPathMotion().getPath((float) i5, (float) i7, (float) i6, (float) i8));
                                int i47 = i6;
                                int i48 = i9;
                                int i49 = i8;
                                int i50 = i10;
                                int i51 = i13;
                                view = view4;
                                Rect rect13 = rect9;
                                int i52 = i46;
                                int i53 = i14;
                                int i54 = i15;
                                z = true;
                                int i55 = i7;
                                int i56 = i5;
                                int i57 = i11;
                            } else {
                                int i58 = i16;
                                int i59 = i15;
                                int i60 = i14;
                                int i61 = i17;
                                ViewBounds viewBounds = new ViewBounds(view4);
                                Path path = getPathMotion().getPath((float) i5, (float) i7, (float) i6, (float) i8);
                                ObjectAnimator ofPointF = ObjectAnimatorUtils.ofPointF(viewBounds, TOP_LEFT_PROPERTY, path);
                                Path path2 = path;
                                View view6 = view4;
                                ObjectAnimator ofPointF2 = ObjectAnimatorUtils.ofPointF(viewBounds, BOTTOM_RIGHT_PROPERTY, getPathMotion().getPath((float) i9, (float) i11, (float) i10, (float) i12));
                                AnimatorSet animatorSet = new AnimatorSet();
                                animatorSet.playTogether(new Animator[]{ofPointF, ofPointF2});
                                animatorSet.addListener(new AnimatorListenerAdapter(viewBounds) {
                                    private ViewBounds mViewBounds;
                                    final /* synthetic */ ViewBounds val$viewBounds;

                                    {
                                        this.val$viewBounds = r2;
                                        this.mViewBounds = r2;
                                    }
                                });
                                int i62 = i9;
                                int i63 = i8;
                                int i64 = i10;
                                animator = animatorSet;
                                Rect rect14 = rect9;
                                int i65 = i58;
                                int i66 = i60;
                                int i67 = i59;
                                int i68 = i13;
                                view = view6;
                                z = true;
                                int i69 = i6;
                                int i70 = i7;
                                int i71 = i5;
                                int i72 = i11;
                            }
                        } else {
                            int i73 = i16;
                            int i74 = i15;
                            int i75 = i14;
                            view = view3;
                            int i76 = i17;
                            int i77 = i13;
                            int max = Math.max(i77, i15);
                            int i78 = i9;
                            int i79 = i11;
                            ViewUtils.setLeftTopRightBottom(view, i5, i7, i5 + max, i7 + Math.max(i14, i16));
                            if (i5 == i6 && i7 == i8) {
                                i = i6;
                                objectAnimator = null;
                                i2 = i7;
                                i3 = i5;
                            } else {
                                i3 = i5;
                                i2 = i7;
                                i = i6;
                                objectAnimator = ObjectAnimatorUtils.ofPointF(view, POSITION_PROPERTY, getPathMotion().getPath((float) i5, (float) i7, (float) i6, (float) i8));
                            }
                            int i80 = i2;
                            final Rect rect15 = rect10;
                            if (rect9 == null) {
                                i4 = 0;
                                rect = new Rect(0, 0, i77, i14);
                            } else {
                                i4 = 0;
                                rect = rect9;
                            }
                            Rect rect16 = rect10 == null ? new Rect(i4, i4, i15, i16) : rect10;
                            ObjectAnimator objectAnimator2 = null;
                            if (!rect.equals(rect16)) {
                                ViewCompat.setClipBounds(view, rect);
                                int i81 = i16;
                                int i82 = i77;
                                AnonymousClass8 r9 = r0;
                                int i83 = max;
                                Rect rect17 = rect16;
                                int i84 = i78;
                                ObjectAnimator ofObject = ObjectAnimator.ofObject(view, "clipBounds", sRectEvaluator, new Object[]{rect, rect16});
                                final View view7 = view;
                                int i85 = i3;
                                rect2 = rect;
                                final int i86 = i;
                                int i87 = i15;
                                final int i88 = i8;
                                int i89 = i14;
                                final int i90 = i10;
                                int i91 = i10;
                                z = true;
                                int i92 = i8;
                                final int i93 = i12;
                                AnonymousClass8 r0 = new AnimatorListenerAdapter() {
                                    private boolean mIsCanceled;

                                    public void onAnimationCancel(Animator animation) {
                                        this.mIsCanceled = true;
                                    }

                                    public void onAnimationEnd(Animator animation) {
                                        if (!this.mIsCanceled) {
                                            ViewCompat.setClipBounds(view7, rect15);
                                            ViewUtils.setLeftTopRightBottom(view7, i86, i88, i90, i93);
                                        }
                                    }
                                };
                                ofObject.addListener(r9);
                                objectAnimator2 = ofObject;
                            } else {
                                int i94 = i16;
                                Rect rect18 = rect16;
                                int i95 = i15;
                                int i96 = i14;
                                int i97 = i8;
                                int i98 = i77;
                                int i99 = i10;
                                int i100 = max;
                                int i101 = i78;
                                int i102 = i3;
                                z = true;
                                rect2 = rect;
                            }
                            animator = TransitionUtils.mergeAnimators(objectAnimator, objectAnimator2);
                            Rect rect19 = rect2;
                        }
                        if (view.getParent() instanceof ViewGroup) {
                            final ViewGroup viewGroup13 = (ViewGroup) view.getParent();
                            ViewGroupUtils.suppressLayout(viewGroup13, z);
                            addListener(new TransitionListenerAdapter() {
                                boolean mCanceled = false;

                                public void onTransitionCancel(Transition transition) {
                                    ViewGroupUtils.suppressLayout(viewGroup13, false);
                                    this.mCanceled = true;
                                }

                                public void onTransitionEnd(Transition transition) {
                                    if (!this.mCanceled) {
                                        ViewGroupUtils.suppressLayout(viewGroup13, false);
                                    }
                                    transition.removeListener(this);
                                }

                                public void onTransitionPause(Transition transition) {
                                    ViewGroupUtils.suppressLayout(viewGroup13, false);
                                }

                                public void onTransitionResume(Transition transition) {
                                    ViewGroupUtils.suppressLayout(viewGroup13, true);
                                }
                            });
                        }
                        return animator;
                    }
                    int i103 = i16;
                    int i104 = i6;
                    int i105 = i9;
                    int i106 = i7;
                    int i107 = i5;
                    int i108 = i15;
                    int i109 = i14;
                    int i110 = i8;
                    Rect rect20 = rect8;
                    int i111 = i10;
                    int i112 = i11;
                    int i113 = i13;
                    Rect rect21 = rect7;
                    View view8 = view3;
                    int i114 = i17;
                    TransitionValues transitionValues7 = startValues;
                    TransitionValues transitionValues8 = endValues;
                    return null;
                }
                Map<String, Object> map9 = map;
                Map<String, Object> map10 = map2;
                ViewGroup viewGroup14 = viewGroup3;
                ViewGroup viewGroup15 = viewGroup4;
                View view9 = view2;
                TransitionValues transitionValues9 = startValues;
                int intValue = ((Integer) transitionValues9.values.get(PROPNAME_WINDOW_X)).intValue();
                int intValue2 = ((Integer) transitionValues9.values.get(PROPNAME_WINDOW_Y)).intValue();
                TransitionValues transitionValues10 = endValues;
                int intValue3 = ((Integer) transitionValues10.values.get(PROPNAME_WINDOW_X)).intValue();
                int intValue4 = ((Integer) transitionValues10.values.get(PROPNAME_WINDOW_Y)).intValue();
                if (intValue == intValue3 && intValue2 == intValue4) {
                    return null;
                }
                sceneRoot.getLocationInWindow(this.mTempLocation);
                Bitmap createBitmap = Bitmap.createBitmap(view9.getWidth(), view9.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                view9.draw(canvas);
                final BitmapDrawable bitmapDrawable = new BitmapDrawable(createBitmap);
                float transitionAlpha = ViewUtils.getTransitionAlpha(view9);
                ViewUtils.setTransitionAlpha(view9, 0.0f);
                ViewUtils.getOverlay(sceneRoot).add(bitmapDrawable);
                PathMotion pathMotion = getPathMotion();
                int[] iArr = this.mTempLocation;
                int i115 = iArr[0];
                int i116 = iArr[1];
                AnonymousClass10 r6 = r0;
                final ViewGroup viewGroup16 = sceneRoot;
                BitmapDrawable bitmapDrawable2 = bitmapDrawable;
                Canvas canvas2 = canvas;
                int i117 = intValue;
                ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(bitmapDrawable, new PropertyValuesHolder[]{PropertyValuesHolderUtils.ofPointF(DRAWABLE_ORIGIN_PROPERTY, pathMotion.getPath((float) (intValue - i115), (float) (intValue2 - i116), (float) (intValue3 - i115), (float) (intValue4 - i116)))});
                final View view10 = view9;
                Bitmap bitmap = createBitmap;
                final float f = transitionAlpha;
                AnonymousClass10 r02 = new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        ViewUtils.getOverlay(viewGroup16).remove(bitmapDrawable);
                        ViewUtils.setTransitionAlpha(view10, f);
                    }
                };
                ofPropertyValuesHolder.addListener(r6);
                return ofPropertyValuesHolder;
            }
        }
    }

    public boolean getResizeClip() {
        return this.mResizeClip;
    }

    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    public void setResizeClip(boolean resizeClip) {
        this.mResizeClip = resizeClip;
    }
}
