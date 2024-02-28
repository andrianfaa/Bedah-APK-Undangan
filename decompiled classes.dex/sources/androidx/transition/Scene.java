package androidx.transition;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Scene {
    private Context mContext;
    private Runnable mEnterAction;
    private Runnable mExitAction;
    private View mLayout;
    private int mLayoutId = -1;
    private ViewGroup mSceneRoot;

    public Scene(ViewGroup sceneRoot) {
        this.mSceneRoot = sceneRoot;
    }

    private Scene(ViewGroup sceneRoot, int layoutId, Context context) {
        this.mContext = context;
        this.mSceneRoot = sceneRoot;
        this.mLayoutId = layoutId;
    }

    public Scene(ViewGroup sceneRoot, View layout) {
        this.mSceneRoot = sceneRoot;
        this.mLayout = layout;
    }

    public static Scene getCurrentScene(ViewGroup sceneRoot) {
        return (Scene) sceneRoot.getTag(R.id.transition_current_scene);
    }

    public static Scene getSceneForLayout(ViewGroup sceneRoot, int layoutId, Context context) {
        SparseArray sparseArray = (SparseArray) sceneRoot.getTag(R.id.transition_scene_layoutid_cache);
        if (sparseArray == null) {
            sparseArray = new SparseArray();
            sceneRoot.setTag(R.id.transition_scene_layoutid_cache, sparseArray);
        }
        Scene scene = (Scene) sparseArray.get(layoutId);
        if (scene != null) {
            return scene;
        }
        Scene scene2 = new Scene(sceneRoot, layoutId, context);
        sparseArray.put(layoutId, scene2);
        return scene2;
    }

    static void setCurrentScene(ViewGroup sceneRoot, Scene scene) {
        sceneRoot.setTag(R.id.transition_current_scene, scene);
    }

    public void enter() {
        if (this.mLayoutId > 0 || this.mLayout != null) {
            getSceneRoot().removeAllViews();
            if (this.mLayoutId > 0) {
                LayoutInflater.from(this.mContext).inflate(this.mLayoutId, this.mSceneRoot);
            } else {
                this.mSceneRoot.addView(this.mLayout);
            }
        }
        Runnable runnable = this.mEnterAction;
        if (runnable != null) {
            runnable.run();
        }
        setCurrentScene(this.mSceneRoot, this);
    }

    public void exit() {
        Runnable runnable;
        if (getCurrentScene(this.mSceneRoot) == this && (runnable = this.mExitAction) != null) {
            runnable.run();
        }
    }

    public ViewGroup getSceneRoot() {
        return this.mSceneRoot;
    }

    /* access modifiers changed from: package-private */
    public boolean isCreatedFromLayoutResource() {
        return this.mLayoutId > 0;
    }

    public void setEnterAction(Runnable action) {
        this.mEnterAction = action;
    }

    public void setExitAction(Runnable action) {
        this.mExitAction = action;
    }
}
