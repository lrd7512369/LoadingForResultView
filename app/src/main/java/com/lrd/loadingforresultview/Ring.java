package com.lrd.loadingforresultview;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by lierdong on 2017/4/2 下午7:50
 */

class Ring extends BaseLoadingModel {

    private static final float SWIPE_ANGLE = 120;

    /**
     * loading时候的颜色
     */
    private int loadingColor;

    /**
     * 圆环变成圈时候的颜色
     */
    private int finishColor;

    /**
     * 圆环宽度
     */
    private float strokeWidth;

    /**
     * 容器大小
     */
    private RectF root;

    /**
     * 绘制控制的动画
     */
    private ValueAnimator animator;

    /**
     * canvas旋转角度
     */
    private float rotation;

    /**
     * 下一次动画开始的时候起始角度
     */
    private float startRotateAngle;

    /**
     * 扇形起点角度
     */
    private float startAngle;

    /**
     * 扇形终点角度
     */
    private float endAngle;

    /**
     * 是否在下一次动画完成后结束
     */
    private boolean finish;

    private BaseLoadingModel finishModel;

    Ring(LoadingForResultView loadingForResultView) {
        super(loadingForResultView);
        root = new RectF();
    }

    void setFinishModel(BaseLoadingModel finishModel) {
        this.finishModel = finishModel;
    }

    void setLoadingColor(int loadingColor) {
        this.loadingColor = loadingColor;
        color = loadingColor;
    }

    void setFinishColor(int finishColor) {
        this.finishColor = finishColor;
    }

    void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        updateSizes();
    }

    void setFinish(boolean finish) {
        this.finish = finish;
    }

    @Override
    public void updateSizes() {
        int width = loadingForResultView.getMeasuredWidth();
        int height = loadingForResultView.getMeasuredHeight();
        float radius = Math.min(width, height) / 2;
        root.set(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius);
        root.inset(strokeWidth / 2, strokeWidth / 2);
    }

    public void start() {
        finish = false;
        color = loadingColor;
        updateSizes();
        if (animator == null) {
            PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("startAngle", 0, 0, SWIPE_ANGLE);
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("endAngle", 0, SWIPE_ANGLE, SWIPE_ANGLE);
            PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("rotation", 0, 360);
            animator = ValueAnimator.ofPropertyValuesHolder(holder1, holder2, holder3);
            animator.setStartDelay(300);
            animator.setDuration(DURATION);
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    startAngle = (float) animation.getAnimatedValue("startAngle");
                    startAngle = fixAngle(startAngle);
                    endAngle = (float) animation.getAnimatedValue("endAngle");
                    endAngle = fixAngle(endAngle);
                    rotation = (float) animation.getAnimatedValue("rotation");
                    loadingForResultView.invalidate();
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(final Animator animation) {
                    Log.d("aa", "repeat");
                    startRotateAngle = fixAngle(startRotateAngle + SWIPE_ANGLE);
                    if (finish) {
                        finish();
                    }
                }
            });
        }
        if (animator.isRunning()) {
            return;
        }
        animator.start();
    }

    public void stop() {
        if (animator != null) {
            animator.cancel();
        }
    }

    /**
     * 补充完整圆环
     */
    private void finish() {
        color = finishColor;
        animator.cancel();
        animator.removeAllListeners();
        animator.removeAllUpdateListeners();
        animator = null;
        PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("startAngle", startRotateAngle, startRotateAngle);
        PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("endAngle", startRotateAngle, startRotateAngle + 360);
        PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("rotation", 0);
        startRotateAngle = 0;
        rotation = 0;
        animator = ValueAnimator.ofPropertyValuesHolder(holder1, holder2, holder3);
        animator.setDuration(DURATION / 2);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startAngle = (float) animation.getAnimatedValue("startAngle");
//                startAngle = fixAngle(startAngle);
                endAngle = (float) animation.getAnimatedValue("endAngle");
//                endAngle = fixAngle(endAngle);
                rotation = (float) animation.getAnimatedValue("rotation");
                loadingForResultView.invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                finishModel.start();
                if (onLoadingResultListener != null) {
                    onLoadingResultListener.onFillRingStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animator = null;
                finish = false;
                if (onLoadingResultListener != null) {
                    onLoadingResultListener.onFillRingEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    @Override
    public void drawSelf(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
        canvas.rotate(fixAngle(rotation + startRotateAngle), root.centerX(), root.centerY());
//        Log.d("aa", startAngle + ":" + endAngle);
        if (endAngle - startAngle > 1) {
            canvas.drawArc(root, startAngle, endAngle - startAngle, false, paint);
        }
    }

    private float fixAngle(float angle) {
        return angle % 360;
    }

}
