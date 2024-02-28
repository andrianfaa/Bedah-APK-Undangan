package com.google.android.material.transition;

class FadeModeEvaluators {
    private static final FadeModeEvaluator CROSS = new FadeModeEvaluator() {
        public FadeModeResult evaluate(float progress, float fadeStartFraction, float fadeEndFraction, float threshold) {
            return FadeModeResult.startOnTop(TransitionUtils.lerp(255, 0, fadeStartFraction, fadeEndFraction, progress), TransitionUtils.lerp(0, 255, fadeStartFraction, fadeEndFraction, progress));
        }
    };
    private static final FadeModeEvaluator IN = new FadeModeEvaluator() {
        public FadeModeResult evaluate(float progress, float fadeStartFraction, float fadeEndFraction, float threshold) {
            return FadeModeResult.endOnTop(255, TransitionUtils.lerp(0, 255, fadeStartFraction, fadeEndFraction, progress));
        }
    };
    private static final FadeModeEvaluator OUT = new FadeModeEvaluator() {
        public FadeModeResult evaluate(float progress, float fadeStartFraction, float fadeEndFraction, float threshold) {
            return FadeModeResult.startOnTop(TransitionUtils.lerp(255, 0, fadeStartFraction, fadeEndFraction, progress), 255);
        }
    };
    private static final FadeModeEvaluator THROUGH = new FadeModeEvaluator() {
        public FadeModeResult evaluate(float progress, float fadeStartFraction, float fadeEndFraction, float threshold) {
            float f = ((fadeEndFraction - fadeStartFraction) * threshold) + fadeStartFraction;
            return FadeModeResult.startOnTop(TransitionUtils.lerp(255, 0, fadeStartFraction, f, progress), TransitionUtils.lerp(0, 255, f, fadeEndFraction, progress));
        }
    };

    private FadeModeEvaluators() {
    }

    static FadeModeEvaluator get(int fadeMode, boolean entering) {
        switch (fadeMode) {
            case 0:
                return entering ? IN : OUT;
            case 1:
                return entering ? OUT : IN;
            case 2:
                return CROSS;
            case 3:
                return THROUGH;
            default:
                throw new IllegalArgumentException("Invalid fade mode: " + fadeMode);
        }
    }
}
