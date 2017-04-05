package com.lrd.loadingforresultview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lierdong on 2017/4/3 下午6:55
 */

public class LoadingForResultView extends View {

    /**
     * 显示成功结果的颜色
     */
    private int successColor;

    /**
     * 显示失败结果的颜色
     */
    private int errorColor;

    /**
     * 画笔
     */
    private Paint paint;

    /**
     * 圆环对象
     */
    private Ring ring;

    /**
     * 成功结果对象
     */
    private CheckMark checkMark;

    /**
     * 失败结果对象
     */
    private Error error;
    private OnLoadingResultListener onLoadingResultListener;

    public LoadingForResultView(Context context) {
        this(context, null, 0);
    }

    public LoadingForResultView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingForResultView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnLoadingResultListener(OnLoadingResultListener onLoadingResultListener) {
        this.onLoadingResultListener = onLoadingResultListener;
        ring.onLoadingResultListener = onLoadingResultListener;
        checkMark.onLoadingResultListener = onLoadingResultListener;
        error.onLoadingResultListener = onLoadingResultListener;
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);

        ring = new Ring(this);
        checkMark = new CheckMark(this);
        error = new Error(this);
    }

    public void setLoadingColor(int loadingColor) {
        ring.setLoadingColor(loadingColor);
    }

    public void setSuccessColor(int successColor) {
        this.successColor = successColor;
    }

    public void setErrorColor(int errorColor) {
        this.errorColor = errorColor;
    }

    public void setRingStrokeWidth(int ringStrokeWidth) {
        ring.setStrokeWidth(ringStrokeWidth);
    }

    public void setCheckMarkWidth(int checkMarkWidth) {
        checkMark.setCheckMarkWidth(checkMarkWidth);
    }

    public void setErrorStrokeWidth(int errorStrokeWidth) {
        error.setErrorStrokeWidth(errorStrokeWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        ring.updateSizes();
        checkMark.updateSizes();
        error.updateSizes();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        checkMark.drawSelf(canvas, paint);
        error.drawSelf(canvas, paint);
        ring.drawSelf(canvas, paint);
    }


    public float dp2px(int dp) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return dp * scale;
    }

    /**
     * 开启loading
     */
    public void start() {
        checkMark.setCanDraw(false);
        error.setCanDraw(false);
        ring.start();
    }

    /**
     * 释放资源
     */
    public void stop() {
        ring.stop();
        checkMark.stop();
        error.stop();
    }

    public void showSuccessResult(int successColor) {
        this.successColor = successColor;
        showSuccessResult();
    }

    public void showSuccessResult() {
        ring.setFinishColor(successColor);
        checkMark.color = successColor;
        ring.setFinishModel(checkMark);
        ring.setFinish(true);
    }

    public void showErrorResult(int errorColor) {
        this.errorColor = errorColor;
        showErrorResult();
    }

    public void showErrorResult() {
        ring.setFinishColor(errorColor);
        error.color = errorColor;
        ring.setFinishModel(error);
        ring.setFinish(true);
    }

}
