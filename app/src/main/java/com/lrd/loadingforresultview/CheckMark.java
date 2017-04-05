package com.lrd.loadingforresultview;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by lierdong on 2017/4/3 下午8:12
 */

class CheckMark extends BaseLoadingModel {

    private RectF rectF1;
    private RectF rectF2;
    private boolean canDraw;

    /**
     * 对号宽度
     */
    private float checkMarkWidth = 7;

    CheckMark(LoadingForResultView loadingForResultView) {
        super(loadingForResultView);
        rectF1 = new RectF();
        rectF2 = new RectF();
    }

    void setCheckMarkWidth(float checkMarkWidth) {
        this.checkMarkWidth = checkMarkWidth;
        updateSizes();
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    @Override
    public void updateSizes() {
        int width = loadingForResultView.getMeasuredWidth();
        int height = loadingForResultView.getMeasuredHeight();
        int radius = Math.min(width, height) / 2;
        float cx = width / 2;
        float cy = height / 2;
        rectF1.set(cx - checkMarkWidth / 2, radius * 0.5f, cx + checkMarkWidth / 2, radius * 0.5f);
        rectF2.set(cx - checkMarkWidth / 2, cy - checkMarkWidth / 2, cx - checkMarkWidth / 2, cy + checkMarkWidth / 2);
    }
    @Override
    public void start() {
        updateSizes();
        canDraw = true;
        if (animator == null) {
            int width = loadingForResultView.getMeasuredWidth();
            int height = loadingForResultView.getMeasuredHeight();
            int radius = Math.min(width, height) / 2;
            float cx = width / 2;
            float cy = height / 2;
            PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("ok1", radius * 0.5f, radius * 0.5f, cy + checkMarkWidth / 2);
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("ok2", cx - checkMarkWidth / 2, cx - checkMarkWidth / 2, width);
            animator = ValueAnimator.ofPropertyValuesHolder(holder1, holder2);
            animator.setDuration(DURATION/2);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float ok1 = (float) animation.getAnimatedValue("ok1");
                    float ok2 = (float) animation.getAnimatedValue("ok2");
                    rectF1.bottom = ok1;
                    rectF2.right = ok2;
                    loadingForResultView.invalidate();
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (onLoadingResultListener!=null){
                        onLoadingResultListener.onResultShowStart();
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (onLoadingResultListener!=null){
                        onLoadingResultListener.onResultShowEnd();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

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
        canvas.translate(-loadingForResultView.getMeasuredWidth() / 4, loadingForResultView.getMeasuredHeight() / 8);
        canvas.drawRect(rectF1, paint);
        canvas.drawRect(rectF2, paint);
        canvas.translate(loadingForResultView.getMeasuredWidth() / 4, -loadingForResultView.getMeasuredHeight() / 8);
    }
}
