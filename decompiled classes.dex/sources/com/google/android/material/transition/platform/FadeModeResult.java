package com.google.android.material.transition.platform;

class FadeModeResult {
    final int endAlpha;
    final boolean endOnTop;
    final int startAlpha;

    private FadeModeResult(int startAlpha2, int endAlpha2, boolean endOnTop2) {
        this.startAlpha = startAlpha2;
        this.endAlpha = endAlpha2;
        this.endOnTop = endOnTop2;
    }

    static FadeModeResult endOnTop(int startAlpha2, int endAlpha2) {
        return new FadeModeResult(startAlpha2, endAlpha2, true);
    }

    static FadeModeResult startOnTop(int startAlpha2, int endAlpha2) {
        return new FadeModeResult(startAlpha2, endAlpha2, false);
    }
}
