package com.google.android.material.transition.platform;

import android.graphics.RectF;

class FitModeEvaluators {
    private static final FitModeEvaluator HEIGHT = new FitModeEvaluator() {
        public void applyMask(RectF maskBounds, float maskMultiplier, FitModeResult fitModeResult) {
            float abs = Math.abs(fitModeResult.currentEndWidth - fitModeResult.currentStartWidth);
            maskBounds.left += (abs / 2.0f) * maskMultiplier;
            maskBounds.right -= (abs / 2.0f) * maskMultiplier;
        }

        public FitModeResult evaluate(float progress, float scaleStartFraction, float scaleEndFraction, float startWidth, float startHeight, float endWidth, float endHeight) {
            float lerp = TransitionUtils.lerp(startHeight, endHeight, scaleStartFraction, scaleEndFraction, progress, true);
            float f = lerp / startHeight;
            float f2 = lerp / endHeight;
            return new FitModeResult(f, f2, startWidth * f, lerp, endWidth * f2, lerp);
        }

        public boolean shouldMaskStartBounds(FitModeResult fitModeResult) {
            return fitModeResult.currentStartWidth > fitModeResult.currentEndWidth;
        }
    };
    private static final FitModeEvaluator WIDTH = new FitModeEvaluator() {
        public void applyMask(RectF maskBounds, float maskMultiplier, FitModeResult fitModeResult) {
            maskBounds.bottom -= Math.abs(fitModeResult.currentEndHeight - fitModeResult.currentStartHeight) * maskMultiplier;
        }

        public FitModeResult evaluate(float progress, float scaleStartFraction, float scaleEndFraction, float startWidth, float startHeight, float endWidth, float endHeight) {
            float lerp = TransitionUtils.lerp(startWidth, endWidth, scaleStartFraction, scaleEndFraction, progress, true);
            float f = lerp / startWidth;
            float f2 = lerp / endWidth;
            return new FitModeResult(f, f2, lerp, startHeight * f, lerp, endHeight * f2);
        }

        public boolean shouldMaskStartBounds(FitModeResult fitModeResult) {
            return fitModeResult.currentStartHeight > fitModeResult.currentEndHeight;
        }
    };

    private FitModeEvaluators() {
    }

    static FitModeEvaluator get(int fitMode, boolean entering, RectF startBounds, RectF endBounds) {
        switch (fitMode) {
            case 0:
                return shouldAutoFitToWidth(entering, startBounds, endBounds) ? WIDTH : HEIGHT;
            case 1:
                return WIDTH;
            case 2:
                return HEIGHT;
            default:
                throw new IllegalArgumentException("Invalid fit mode: " + fitMode);
        }
    }

    private static boolean shouldAutoFitToWidth(boolean entering, RectF startBounds, RectF endBounds) {
        float width = startBounds.width();
        float height = startBounds.height();
        float width2 = endBounds.width();
        float height2 = endBounds.height();
        float f = (height2 * width) / width2;
        float f2 = (height * width2) / width;
        if (entering) {
            if (f >= height) {
                return true;
            }
        } else if (f2 >= height2) {
            return true;
        }
        return false;
    }
}
