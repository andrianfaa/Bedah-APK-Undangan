package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import org.xmlpull.v1.XmlPullParser;

public class ChangeTransform extends Transition {
    private static final Property<PathAnimatorMatrix, float[]> NON_TRANSLATIONS_PROPERTY = new Property<PathAnimatorMatrix, float[]>(float[].class, "nonTranslations") {
        public float[] get(PathAnimatorMatrix object) {
            return null;
        }

        public void set(PathAnimatorMatrix object, float[] value) {
            object.setValues(value);
        }
    };
    private static final String PROPNAME_INTERMEDIATE_MATRIX = "android:changeTransform:intermediateMatrix";
    private static final String PROPNAME_INTERMEDIATE_PARENT_MATRIX = "android:changeTransform:intermediateParentMatrix";
    private static final String PROPNAME_MATRIX = "android:changeTransform:matrix";
    private static final String PROPNAME_PARENT = "android:changeTransform:parent";
    private static final String PROPNAME_PARENT_MATRIX = "android:changeTransform:parentMatrix";
    private static final String PROPNAME_TRANSFORMS = "android:changeTransform:transforms";
    private static final boolean SUPPORTS_VIEW_REMOVAL_SUPPRESSION = (Build.VERSION.SDK_INT >= 21);
    private static final Property<PathAnimatorMatrix, PointF> TRANSLATIONS_PROPERTY = new Property<PathAnimatorMatrix, PointF>(PointF.class, "translations") {
        public PointF get(PathAnimatorMatrix object) {
            return null;
        }

        public void set(PathAnimatorMatrix object, PointF value) {
            object.setTranslation(value);
        }
    };
    private static final String[] sTransitionProperties = {PROPNAME_MATRIX, PROPNAME_TRANSFORMS, PROPNAME_PARENT_MATRIX};
    private boolean mReparent = true;
    private Matrix mTempMatrix = new Matrix();
    boolean mUseOverlay = true;

    private static class GhostListener extends TransitionListenerAdapter {
        private GhostView mGhostView;
        private View mView;

        GhostListener(View view, GhostView ghostView) {
            this.mView = view;
            this.mGhostView = ghostView;
        }

        public void onTransitionEnd(Transition transition) {
            transition.removeListener(this);
            GhostViewUtils.removeGhost(this.mView);
            this.mView.setTag(R.id.transition_transform, (Object) null);
            this.mView.setTag(R.id.parent_matrix, (Object) null);
        }

        public void onTransitionPause(Transition transition) {
            this.mGhostView.setVisibility(4);
        }

        public void onTransitionResume(Transition transition) {
            this.mGhostView.setVisibility(0);
        }
    }

    private static class PathAnimatorMatrix {
        private final Matrix mMatrix = new Matrix();
        private float mTranslationX;
        private float mTranslationY;
        private final float[] mValues;
        private final View mView;

        PathAnimatorMatrix(View view, float[] values) {
            this.mView = view;
            float[] fArr = (float[]) values.clone();
            this.mValues = fArr;
            this.mTranslationX = fArr[2];
            this.mTranslationY = fArr[5];
            setAnimationMatrix();
        }

        private void setAnimationMatrix() {
            float[] fArr = this.mValues;
            fArr[2] = this.mTranslationX;
            fArr[5] = this.mTranslationY;
            this.mMatrix.setValues(fArr);
            ViewUtils.setAnimationMatrix(this.mView, this.mMatrix);
        }

        /* access modifiers changed from: package-private */
        public Matrix getMatrix() {
            return this.mMatrix;
        }

        /* access modifiers changed from: package-private */
        public void setTranslation(PointF translation) {
            this.mTranslationX = translation.x;
            this.mTranslationY = translation.y;
            setAnimationMatrix();
        }

        /* access modifiers changed from: package-private */
        public void setValues(float[] values) {
            System.arraycopy(values, 0, this.mValues, 0, values.length);
            setAnimationMatrix();
        }
    }

    private static class Transforms {
        final float mRotationX;
        final float mRotationY;
        final float mRotationZ;
        final float mScaleX;
        final float mScaleY;
        final float mTranslationX;
        final float mTranslationY;
        final float mTranslationZ;

        Transforms(View view) {
            this.mTranslationX = view.getTranslationX();
            this.mTranslationY = view.getTranslationY();
            this.mTranslationZ = ViewCompat.getTranslationZ(view);
            this.mScaleX = view.getScaleX();
            this.mScaleY = view.getScaleY();
            this.mRotationX = view.getRotationX();
            this.mRotationY = view.getRotationY();
            this.mRotationZ = view.getRotation();
        }

        public boolean equals(Object that) {
            if (!(that instanceof Transforms)) {
                return false;
            }
            Transforms transforms = (Transforms) that;
            return transforms.mTranslationX == this.mTranslationX && transforms.mTranslationY == this.mTranslationY && transforms.mTranslationZ == this.mTranslationZ && transforms.mScaleX == this.mScaleX && transforms.mScaleY == this.mScaleY && transforms.mRotationX == this.mRotationX && transforms.mRotationY == this.mRotationY && transforms.mRotationZ == this.mRotationZ;
        }

        public int hashCode() {
            float f = this.mTranslationX;
            int i = 0;
            int floatToIntBits = (f != 0.0f ? Float.floatToIntBits(f) : 0) * 31;
            float f2 = this.mTranslationY;
            int floatToIntBits2 = (floatToIntBits + (f2 != 0.0f ? Float.floatToIntBits(f2) : 0)) * 31;
            float f3 = this.mTranslationZ;
            int floatToIntBits3 = (floatToIntBits2 + (f3 != 0.0f ? Float.floatToIntBits(f3) : 0)) * 31;
            float f4 = this.mScaleX;
            int floatToIntBits4 = (floatToIntBits3 + (f4 != 0.0f ? Float.floatToIntBits(f4) : 0)) * 31;
            float f5 = this.mScaleY;
            int floatToIntBits5 = (floatToIntBits4 + (f5 != 0.0f ? Float.floatToIntBits(f5) : 0)) * 31;
            float f6 = this.mRotationX;
            int floatToIntBits6 = (floatToIntBits5 + (f6 != 0.0f ? Float.floatToIntBits(f6) : 0)) * 31;
            float f7 = this.mRotationY;
            int floatToIntBits7 = (floatToIntBits6 + (f7 != 0.0f ? Float.floatToIntBits(f7) : 0)) * 31;
            float f8 = this.mRotationZ;
            if (f8 != 0.0f) {
                i = Float.floatToIntBits(f8);
            }
            return floatToIntBits7 + i;
        }

        public void restore(View view) {
            ChangeTransform.setTransforms(view, this.mTranslationX, this.mTranslationY, this.mTranslationZ, this.mScaleX, this.mScaleY, this.mRotationX, this.mRotationY, this.mRotationZ);
        }
    }

    public ChangeTransform() {
    }

    public ChangeTransform(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, Styleable.CHANGE_TRANSFORM);
        this.mUseOverlay = TypedArrayUtils.getNamedBoolean(obtainStyledAttributes, (XmlPullParser) attrs, "reparentWithOverlay", 1, true);
        this.mReparent = TypedArrayUtils.getNamedBoolean(obtainStyledAttributes, (XmlPullParser) attrs, "reparent", 0, true);
        obtainStyledAttributes.recycle();
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        if (view.getVisibility() != 8) {
            transitionValues.values.put(PROPNAME_PARENT, view.getParent());
            transitionValues.values.put(PROPNAME_TRANSFORMS, new Transforms(view));
            Matrix matrix = view.getMatrix();
            transitionValues.values.put(PROPNAME_MATRIX, (matrix == null || matrix.isIdentity()) ? null : new Matrix(matrix));
            if (this.mReparent) {
                Matrix matrix2 = new Matrix();
                ViewGroup viewGroup = (ViewGroup) view.getParent();
                ViewUtils.transformMatrixToGlobal(viewGroup, matrix2);
                matrix2.preTranslate((float) (-viewGroup.getScrollX()), (float) (-viewGroup.getScrollY()));
                transitionValues.values.put(PROPNAME_PARENT_MATRIX, matrix2);
                transitionValues.values.put(PROPNAME_INTERMEDIATE_MATRIX, view.getTag(R.id.transition_transform));
                transitionValues.values.put(PROPNAME_INTERMEDIATE_PARENT_MATRIX, view.getTag(R.id.parent_matrix));
            }
        }
    }

    private void createGhostView(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        View view = endValues.view;
        Matrix matrix = new Matrix((Matrix) endValues.values.get(PROPNAME_PARENT_MATRIX));
        ViewUtils.transformMatrixToLocal(sceneRoot, matrix);
        GhostView addGhost = GhostViewUtils.addGhost(view, sceneRoot, matrix);
        if (addGhost != null) {
            addGhost.reserveEndViewTransition((ViewGroup) startValues.values.get(PROPNAME_PARENT), startValues.view);
            Transition transition = this;
            while (transition.mParent != null) {
                transition = transition.mParent;
            }
            transition.addListener(new GhostListener(view, addGhost));
            if (SUPPORTS_VIEW_REMOVAL_SUPPRESSION) {
                if (startValues.view != endValues.view) {
                    ViewUtils.setTransitionAlpha(startValues.view, 0.0f);
                }
                ViewUtils.setTransitionAlpha(view, 1.0f);
            }
        }
    }

    private ObjectAnimator createTransformAnimator(TransitionValues startValues, TransitionValues endValues, boolean handleParentChange) {
        TransitionValues transitionValues = endValues;
        Matrix matrix = (Matrix) startValues.values.get(PROPNAME_MATRIX);
        Matrix matrix2 = (Matrix) transitionValues.values.get(PROPNAME_MATRIX);
        if (matrix == null) {
            matrix = MatrixUtils.IDENTITY_MATRIX;
        }
        if (matrix2 == null) {
            matrix2 = MatrixUtils.IDENTITY_MATRIX;
        }
        if (matrix.equals(matrix2)) {
            return null;
        }
        View view = transitionValues.view;
        setIdentityTransforms(view);
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        float[] fArr2 = new float[9];
        matrix2.getValues(fArr2);
        PathAnimatorMatrix pathAnimatorMatrix = new PathAnimatorMatrix(view, fArr);
        PropertyValuesHolder ofObject = PropertyValuesHolder.ofObject(NON_TRANSLATIONS_PROPERTY, new FloatArrayEvaluator(new float[9]), new float[][]{fArr, fArr2});
        Path path = getPathMotion().getPath(fArr[2], fArr[5], fArr2[2], fArr2[5]);
        final Matrix matrix3 = matrix2;
        final boolean z = handleParentChange;
        final View view2 = view;
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(pathAnimatorMatrix, new PropertyValuesHolder[]{ofObject, PropertyValuesHolderUtils.ofPointF(TRANSLATIONS_PROPERTY, path)});
        final Transforms transforms = (Transforms) transitionValues.values.get(PROPNAME_TRANSFORMS);
        Path path2 = path;
        final PathAnimatorMatrix pathAnimatorMatrix2 = pathAnimatorMatrix;
        AnonymousClass3 r4 = new AnimatorListenerAdapter() {
            private boolean mIsCanceled;
            private Matrix mTempMatrix = new Matrix();

            private void setCurrentMatrix(Matrix currentMatrix) {
                this.mTempMatrix.set(currentMatrix);
                view2.setTag(R.id.transition_transform, this.mTempMatrix);
                transforms.restore(view2);
            }

            public void onAnimationCancel(Animator animation) {
                this.mIsCanceled = true;
            }

            public void onAnimationEnd(Animator animation) {
                if (!this.mIsCanceled) {
                    if (!z || !ChangeTransform.this.mUseOverlay) {
                        view2.setTag(R.id.transition_transform, (Object) null);
                        view2.setTag(R.id.parent_matrix, (Object) null);
                    } else {
                        setCurrentMatrix(matrix3);
                    }
                }
                ViewUtils.setAnimationMatrix(view2, (Matrix) null);
                transforms.restore(view2);
            }

            public void onAnimationPause(Animator animation) {
                setCurrentMatrix(pathAnimatorMatrix2.getMatrix());
            }

            public void onAnimationResume(Animator animation) {
                ChangeTransform.setIdentityTransforms(view2);
            }
        };
        ofPropertyValuesHolder.addListener(r4);
        AnimatorUtils.addPauseListener(ofPropertyValuesHolder, r4);
        return ofPropertyValuesHolder;
    }

    private boolean parentsMatch(ViewGroup startParent, ViewGroup endParent) {
        boolean z = false;
        if (!isValidTarget(startParent) || !isValidTarget(endParent)) {
            if (startParent == endParent) {
                z = true;
            }
            return z;
        }
        TransitionValues matchedTransitionValues = getMatchedTransitionValues(startParent, true);
        if (matchedTransitionValues == null) {
            return false;
        }
        if (endParent == matchedTransitionValues.view) {
            z = true;
        }
        return z;
    }

    static void setIdentityTransforms(View view) {
        setTransforms(view, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f);
    }

    private void setMatricesForParent(TransitionValues startValues, TransitionValues endValues) {
        Matrix matrix = (Matrix) endValues.values.get(PROPNAME_PARENT_MATRIX);
        endValues.view.setTag(R.id.parent_matrix, matrix);
        Matrix matrix2 = this.mTempMatrix;
        matrix2.reset();
        matrix.invert(matrix2);
        Matrix matrix3 = (Matrix) startValues.values.get(PROPNAME_MATRIX);
        if (matrix3 == null) {
            matrix3 = new Matrix();
            startValues.values.put(PROPNAME_MATRIX, matrix3);
        }
        matrix3.postConcat((Matrix) startValues.values.get(PROPNAME_PARENT_MATRIX));
        matrix3.postConcat(matrix2);
    }

    static void setTransforms(View view, float translationX, float translationY, float translationZ, float scaleX, float scaleY, float rotationX, float rotationY, float rotationZ) {
        view.setTranslationX(translationX);
        view.setTranslationY(translationY);
        ViewCompat.setTranslationZ(view, translationZ);
        view.setScaleX(scaleX);
        view.setScaleY(scaleY);
        view.setRotationX(rotationX);
        view.setRotationY(rotationY);
        view.setRotation(rotationZ);
    }

    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
        if (!SUPPORTS_VIEW_REMOVAL_SUPPRESSION) {
            ((ViewGroup) transitionValues.view.getParent()).startViewTransition(transitionValues.view);
        }
    }

    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null || endValues == null || !startValues.values.containsKey(PROPNAME_PARENT) || !endValues.values.containsKey(PROPNAME_PARENT)) {
            return null;
        }
        ViewGroup viewGroup = (ViewGroup) startValues.values.get(PROPNAME_PARENT);
        boolean z = this.mReparent && !parentsMatch(viewGroup, (ViewGroup) endValues.values.get(PROPNAME_PARENT));
        Matrix matrix = (Matrix) startValues.values.get(PROPNAME_INTERMEDIATE_MATRIX);
        if (matrix != null) {
            startValues.values.put(PROPNAME_MATRIX, matrix);
        }
        Matrix matrix2 = (Matrix) startValues.values.get(PROPNAME_INTERMEDIATE_PARENT_MATRIX);
        if (matrix2 != null) {
            startValues.values.put(PROPNAME_PARENT_MATRIX, matrix2);
        }
        if (z) {
            setMatricesForParent(startValues, endValues);
        }
        ObjectAnimator createTransformAnimator = createTransformAnimator(startValues, endValues, z);
        if (z && createTransformAnimator != null && this.mUseOverlay) {
            createGhostView(sceneRoot, startValues, endValues);
        } else if (!SUPPORTS_VIEW_REMOVAL_SUPPRESSION) {
            viewGroup.endViewTransition(startValues.view);
        }
        return createTransformAnimator;
    }

    public boolean getReparent() {
        return this.mReparent;
    }

    public boolean getReparentWithOverlay() {
        return this.mUseOverlay;
    }

    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    public void setReparent(boolean reparent) {
        this.mReparent = reparent;
    }

    public void setReparentWithOverlay(boolean reparentWithOverlay) {
        this.mUseOverlay = reparentWithOverlay;
    }
}
