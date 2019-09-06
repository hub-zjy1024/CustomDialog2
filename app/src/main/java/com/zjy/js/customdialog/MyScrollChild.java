package com.zjy.js.customdialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 Created by 张建宇 on 2017/4/6. */

public class MyScrollChild extends View {

    private NestedScrollingChildHelper myNestedChildHelper;
    private int[] mConsumed = new int[2];
    private int[] mOffset = new int[2];
    private int x;
    private int y;
    Paint mPaint;
    private int radius = 30;
    public MyScrollChild(Context context) {
        super(context);
        init();
    }

    public MyScrollChild(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyScrollChild);
        radius = typedArray.getInteger(R.styleable.MyScrollChild_radius, 40);
        init();
    }

    public MyScrollChild(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyScrollChild);
        radius = typedArray.getInteger(R.styleable.MyScrollChild_radius, 40);
        init();
    }

    private void init() {
        myNestedChildHelper = new NestedScrollingChildHelper(this);
        mPaint = new Paint();
        setNestedScrollingEnabled(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = radius * 2;
        setMeasuredDimension(width, width);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 按下事件调用startNestedScroll
                x = (int) getX();
                y = (int) getY();
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动事件调用startNestedScroll
                dispatchNestedPreScroll(0, 20, mConsumed, mOffset);
                // 输出一下偏移
                mOffset[0] = x - (int) event.getX();
                mOffset[1] = y - (int) event.getY();
                Log.e("zjy", "MyScrollChild->onTouchEvent(): offsetX  offsetY==" + mOffset[0] + "\t" + mOffset[1]);
                dispatchNestedScroll(50, 50, 50, 50, mOffset);
                break;
            case MotionEvent.ACTION_UP:
                // 弹起事件调用startNestedScroll
                stopNestedScroll();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setTextSize(20);
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);
        canvas.drawText("这是我的子view", 0, 0, mPaint);
        canvas.drawCircle(radius, radius, radius, mPaint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        myNestedChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return myNestedChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return myNestedChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        myNestedChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return myNestedChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return myNestedChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return myNestedChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return myNestedChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return myNestedChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

}
