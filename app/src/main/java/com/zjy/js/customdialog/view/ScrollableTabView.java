package com.zjy.js.customdialog.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 Created by 张建宇 on 2019/7/18. */
public class ScrollableTabView extends ViewGroup {
    private List<String> titles;

    public ScrollableTabView(Context context) {
        super(context);
    }

    public ScrollableTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollableTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
        if (titles != null && titles.size() > 0) {
            removeAllViews();
            int childCount = titles.size();
            for (int i = 0; i < childCount; i++) {
                TextView tv = new TextView(getContext());
                tv.setText(titles.get(i));
                addView(tv);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int padddingLeft = getPaddingLeft();
        int padddingTop = getPaddingLeft();

        int childLeft = padddingLeft;
        int childTop = padddingTop;

        for (int i = 0; i < childCount; i++) {
            TextView tv = (TextView) getChildAt(i);
            childLeft += tv.getMeasuredWidth();
            int vWidth = tv.getMeasuredWidth();
            int vHeight = tv.getMeasuredHeight();
            tv.layout(childLeft, t, childLeft + vWidth, childTop + vHeight);
        }
    }
}
