package com.zjy.js.customdialog.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;


/**
 Created by 张建宇 on 2018/8/14. */
public class MeasureFontHeightTv extends TextView {
    private float lablewidth;

    public MeasureFontHeightTv(Context context) {
        this(context, null, 0);
    }

    private Paint mPaint = new Paint();
    private Rect rect = new Rect();

    public MeasureFontHeightTv(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeasureFontHeightTv(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String s = getText().toString();
        float textSize = getTextSize();
        mPaint.setTextSize(textSize);
        mPaint.getTextBounds(s, 0, s.length(), rect);
        int w = rect.width();
        int h = rect.height();
//        float v = mPaint.measureText(s);
        float v = w;
        int marginText = 30;
        String alertSize = "字号：" + textSize;
        mPaint.getTextBounds(alertSize, 0, alertSize.length(), rect);
        int labelWidth = rect.width();
        canvas.drawText(s, v + marginText, h, mPaint);
        lablewidth = v + labelWidth + marginText;
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("zjy", "MeasureFontHeightTv->onMeasure(): Width==" + lablewidth);
    }
}
