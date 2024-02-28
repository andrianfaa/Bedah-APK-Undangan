package androidx.constraintlayout.helper.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.constraintlayout.motion.widget.KeyAttributes;
import androidx.constraintlayout.motion.widget.KeyPosition;
import androidx.constraintlayout.motion.widget.MotionController;
import androidx.constraintlayout.motion.widget.MotionHelper;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.R;
import java.util.HashMap;
import mt.Log1F380D;

/* compiled from: 0017 */
public class MotionEffect extends MotionHelper {
    public static final int AUTO = -1;
    public static final int EAST = 2;
    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final String TAG = "FadeMove";
    private static final int UNSET = -1;
    public static final int WEST = 3;
    private int fadeMove = -1;
    private float motionEffectAlpha = 0.1f;
    private int motionEffectEnd = 50;
    private int motionEffectStart = 49;
    private boolean motionEffectStrictMove = true;
    private int motionEffectTranslationX = 0;
    private int motionEffectTranslationY = 0;
    private int viewTransitionId = -1;

    public MotionEffect(Context context) {
        super(context);
    }

    public MotionEffect(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MotionEffect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.MotionEffect);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.MotionEffect_motionEffect_start) {
                    int i2 = obtainStyledAttributes.getInt(index, this.motionEffectStart);
                    this.motionEffectStart = i2;
                    this.motionEffectStart = Math.max(Math.min(i2, 99), 0);
                } else if (index == R.styleable.MotionEffect_motionEffect_end) {
                    int i3 = obtainStyledAttributes.getInt(index, this.motionEffectEnd);
                    this.motionEffectEnd = i3;
                    this.motionEffectEnd = Math.max(Math.min(i3, 99), 0);
                } else if (index == R.styleable.MotionEffect_motionEffect_translationX) {
                    this.motionEffectTranslationX = obtainStyledAttributes.getDimensionPixelOffset(index, this.motionEffectTranslationX);
                } else if (index == R.styleable.MotionEffect_motionEffect_translationY) {
                    this.motionEffectTranslationY = obtainStyledAttributes.getDimensionPixelOffset(index, this.motionEffectTranslationY);
                } else if (index == R.styleable.MotionEffect_motionEffect_alpha) {
                    this.motionEffectAlpha = obtainStyledAttributes.getFloat(index, this.motionEffectAlpha);
                } else if (index == R.styleable.MotionEffect_motionEffect_move) {
                    this.fadeMove = obtainStyledAttributes.getInt(index, this.fadeMove);
                } else if (index == R.styleable.MotionEffect_motionEffect_strict) {
                    this.motionEffectStrictMove = obtainStyledAttributes.getBoolean(index, this.motionEffectStrictMove);
                } else if (index == R.styleable.MotionEffect_motionEffect_viewTransition) {
                    this.viewTransitionId = obtainStyledAttributes.getResourceId(index, this.viewTransitionId);
                }
            }
            int i4 = this.motionEffectStart;
            int i5 = this.motionEffectEnd;
            if (i4 == i5) {
                if (i4 > 0) {
                    this.motionEffectStart = i4 - 1;
                } else {
                    this.motionEffectEnd = i5 + 1;
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    public boolean isDecorator() {
        return true;
    }

    public void onPreSetup(MotionLayout motionLayout, HashMap<View, MotionController> hashMap) {
        View[] viewArr;
        HashMap<View, MotionController> hashMap2 = hashMap;
        View[] views = getViews((ConstraintLayout) getParent());
        if (views == null) {
            StringBuilder sb = new StringBuilder();
            String loc = Debug.getLoc();
            Log1F380D.a((Object) loc);
            Log.v(TAG, sb.append(loc).append(" views = null").toString());
            return;
        }
        KeyAttributes keyAttributes = new KeyAttributes();
        KeyAttributes keyAttributes2 = new KeyAttributes();
        keyAttributes.setValue("alpha", Float.valueOf(this.motionEffectAlpha));
        keyAttributes2.setValue("alpha", Float.valueOf(this.motionEffectAlpha));
        keyAttributes.setFramePosition(this.motionEffectStart);
        keyAttributes2.setFramePosition(this.motionEffectEnd);
        KeyPosition keyPosition = new KeyPosition();
        keyPosition.setFramePosition(this.motionEffectStart);
        keyPosition.setType(0);
        keyPosition.setValue("percentX", 0);
        keyPosition.setValue("percentY", 0);
        KeyPosition keyPosition2 = new KeyPosition();
        keyPosition2.setFramePosition(this.motionEffectEnd);
        keyPosition2.setType(0);
        keyPosition2.setValue("percentX", 1);
        keyPosition2.setValue("percentY", 1);
        KeyAttributes keyAttributes3 = null;
        KeyAttributes keyAttributes4 = null;
        if (this.motionEffectTranslationX > 0) {
            keyAttributes3 = new KeyAttributes();
            keyAttributes4 = new KeyAttributes();
            keyAttributes3.setValue("translationX", Integer.valueOf(this.motionEffectTranslationX));
            keyAttributes3.setFramePosition(this.motionEffectEnd);
            keyAttributes4.setValue("translationX", 0);
            keyAttributes4.setFramePosition(this.motionEffectEnd - 1);
        }
        KeyAttributes keyAttributes5 = null;
        KeyAttributes keyAttributes6 = null;
        if (this.motionEffectTranslationY > 0) {
            keyAttributes5 = new KeyAttributes();
            keyAttributes6 = new KeyAttributes();
            keyAttributes5.setValue("translationY", Integer.valueOf(this.motionEffectTranslationY));
            keyAttributes5.setFramePosition(this.motionEffectEnd);
            keyAttributes6.setValue("translationY", 0);
            keyAttributes6.setFramePosition(this.motionEffectEnd - 1);
        }
        int i = this.fadeMove;
        if (this.fadeMove == -1) {
            int[] iArr = new int[4];
            for (View view : views) {
                MotionController motionController = hashMap2.get(view);
                if (motionController != null) {
                    float finalX = motionController.getFinalX() - motionController.getStartX();
                    float finalY = motionController.getFinalY() - motionController.getStartY();
                    if (finalY < 0.0f) {
                        iArr[1] = iArr[1] + 1;
                    }
                    if (finalY > 0.0f) {
                        iArr[0] = iArr[0] + 1;
                    }
                    if (finalX > 0.0f) {
                        iArr[3] = iArr[3] + 1;
                    }
                    if (finalX < 0.0f) {
                        iArr[2] = iArr[2] + 1;
                    }
                }
            }
            int i2 = iArr[0];
            i = 0;
            for (int i3 = 1; i3 < 4; i3++) {
                if (i2 < iArr[i3]) {
                    i2 = iArr[i3];
                    i = i3;
                }
            }
        }
        int i4 = 0;
        while (i4 < views.length) {
            MotionController motionController2 = hashMap2.get(views[i4]);
            if (motionController2 == null) {
                viewArr = views;
                MotionLayout motionLayout2 = motionLayout;
            } else {
                float finalX2 = motionController2.getFinalX() - motionController2.getStartX();
                float finalY2 = motionController2.getFinalY() - motionController2.getStartY();
                boolean z = true;
                if (i == 0) {
                    if (finalY2 > 0.0f && (!this.motionEffectStrictMove || finalX2 == 0.0f)) {
                        z = false;
                    }
                } else if (i == 1) {
                    if (finalY2 < 0.0f && (!this.motionEffectStrictMove || finalX2 == 0.0f)) {
                        z = false;
                    }
                } else if (i == 2) {
                    if (finalX2 < 0.0f && (!this.motionEffectStrictMove || finalY2 == 0.0f)) {
                        z = false;
                    }
                } else if (i == 3 && finalX2 > 0.0f && (!this.motionEffectStrictMove || finalY2 == 0.0f)) {
                    z = false;
                }
                if (z) {
                    int i5 = this.viewTransitionId;
                    viewArr = views;
                    if (i5 == -1) {
                        motionController2.addKey(keyAttributes);
                        motionController2.addKey(keyAttributes2);
                        motionController2.addKey(keyPosition);
                        motionController2.addKey(keyPosition2);
                        if (this.motionEffectTranslationX > 0) {
                            motionController2.addKey(keyAttributes3);
                            motionController2.addKey(keyAttributes4);
                        }
                        if (this.motionEffectTranslationY > 0) {
                            motionController2.addKey(keyAttributes5);
                            motionController2.addKey(keyAttributes6);
                            MotionLayout motionLayout3 = motionLayout;
                        } else {
                            MotionLayout motionLayout4 = motionLayout;
                        }
                    } else {
                        motionLayout.applyViewTransition(i5, motionController2);
                    }
                } else {
                    viewArr = views;
                    MotionLayout motionLayout5 = motionLayout;
                }
            }
            i4++;
            hashMap2 = hashMap;
            views = viewArr;
        }
    }
}
