package com.zjy.js.customdialog.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 Created by 张建宇 on 2017/4/6. */

public class MyScrolling extends LinearLayout implements NestedScrollingParent {
    private NestedScrollingParentHelper ntParent;

    public MyScrolling(Context context) {
        this(context, null);
    }

    public MyScrolling(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrolling(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initScrollParent();
    }

    public void initScrollParent() {
        ntParent = new NestedScrollingParentHelper(this);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.e("zjy", "MyScrolling->onStartNestedScroll(): start==" + (child == target));
        Log.e("zjy", "MyScrolling->onStartNestedScroll(): axes==" + nestedScrollAxes);
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View child) {
        Log.e("zjy", "MyScrolling->onStopNestedScroll(): stop==");
        super.onStopNestedScroll(child);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.e("zjy", "MyScrolling->onNestedFling(): x==" + velocityX + "\t" + velocityY + "\t" + consumed);
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.e("zjy", "MyScrolling->onNestedPreFling(): px==" + velocityX + "\t" + velocityY);
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.e("zjy", "MyScrolling->onNestedPreScroll(): dx dy==" + dx + "\t" + dy);
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e("zjy", "MyScrolling->onNestedScroll(): dxc dxy==" + dxConsumed + "\t" + dxUnconsumed + "\t" + dyConsumed + "\t" + dyUnconsumed);
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        Log.e("zjy", "MyScrolling->onNestedScrollAccepted():parent-accept==" + axes);

        super.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public boolean onNestedPrePerformAccessibilityAction(View target, int action, Bundle args) {
        return super.onNestedPrePerformAccessibilityAction(target, action, args);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e("zjy", "MyScrolling->onLayout():left top right bottom==" + l + "\t" + t + "\t" + r + "\t" + b);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            int marginLeft = 0;
            int marginRight = 0;
            int marginTop = 0;
            int marginBottom = 0;
            if (params != null) {
                marginLeft = params.leftMargin;
                marginTop = params.topMargin;
                marginRight = params.rightMargin;
                marginBottom = params.bottomMargin;
            }
            child.layout(0 + marginLeft, 0 + marginTop, r, b);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        int totoalWidth = 0;
        int totoalHeight = 0;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        switch (wMode) {
            //wrap_content
            case MeasureSpec.AT_MOST:
                break;
            //match_parent or 400dp
            case MeasureSpec.EXACTLY:
                break;
        }
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.measure(0, 0);
            totoalHeight += child.getMeasuredHeight();
            Log.e("zjy", "MyScrolling->onMeasure(): child-width-height==" + child.getMeasuredWidth() + "-" + child.getMeasuredHeight());
            totoalWidth += child.getMeasuredWidth();
        }
        setMeasuredDimension(totoalWidth, totoalHeight);
        //        setMeasuredDimension(200, 200);
        //        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public int getNestedScrollAxes() {
        Log.e("zjy", "MyScrolling->getNestedScrollAxes(): parent-getAxes==");

        return super.getNestedScrollAxes();
    }

}
