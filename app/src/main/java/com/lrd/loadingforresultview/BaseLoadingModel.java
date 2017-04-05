package com.lrd.loadingforresultview;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by lierdong on 2017/4/2 下午7:51
 */

abstract class BaseLoadingModel {

    static final long DURATION = 1332;

    OnLoadingResultListener onLoadingResultListener;
    LoadingForResultView loadingForResultView;
    int color;
    ValueAnimator animator;

    BaseLoadingModel(LoadingForResultView loadingForResultView) {
        this.loadingForResultView = loadingForResultView;
    }

    public abstract void updateSizes();

    public abstract void start();

    public void stop() {
        if (animator != null) {
            animator.cancel();
            animator.removeAllListeners();
            animator.removeAllUpdateListeners();
            animator = null;
        }
    }

    public abstract void drawSelf(Canvas canvas, Paint paint);
}
