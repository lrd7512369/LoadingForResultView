package com.lrd.loadingforresultview;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by lierdong on 2017/4/5 上午11:18
 */

public class Error extends BaseLoadingModel {

    private float errorStrokeWidth;
    private RectF rectF1;
    private RectF rectF2;

    private boolean canDraw;

    Error(LoadingForResultView loadingForResultView) {
        super(loadingForResultView);
        rectF1 = new RectF();
        rectF2 = new RectF();
    }

    public void setErrorStrokeWidth(float errorStrokeWidth) {
        this.errorStrokeWidth = errorStrokeWidth;
        updateSizes();
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    @Override
    public void updateSizes() {
        int width = loadingForResultView.getMeasuredWidth();
        int height = loadingForResultView.getMeasuredHeight();
        int cx = width / 2;
        int radius = Math.min(width, height) / 2;
        rectF1.set(cx - errorStrokeWidth / 2, radius / 2, cx + errorStrokeWidth / 2, radius);
        rectF2.set(rectF1);
    }

    @Override
    public void start() {
        canDraw = true;
        updateSizes();
        if (animator == null) {
            int width = loadingForResultView.getMeasuredWidth();
            final int height = loadingForResultView.getMeasuredHeight();
            int radius = Math.min(width, height) / 2;
            animator = ValueAnimator.ofFloat(radius / 2, height - radius / 2);
            animator.setDuration(DURATION/2);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    rectF1.bottom = value;
                    rectF2.bottom = value;
                    loadingForResultView.invalidate();
                }
            });
        } else {
            if (animator.isRunning()) {
                return;
            }
        }
        animator.start();
    }

    @Override
    public void drawSelf(Canvas canvas, Paint paint) {
        if (!canDraw) {
            return;
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        canvas.rotate(-45, loadingForResultView.getMeasuredWidth() / 2, loadingForResultView.getMeasuredHeight() / 2);
        canvas.drawRect(rectF1, paint);
        canvas.rotate(90, loadingForResultView.getMeasuredWidth() / 2, loadingForResultView.getMeasuredHeight() / 2);
        canvas.drawRect(rectF2, paint);
    }
}
