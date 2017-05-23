package com.example.leo.radarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by leo on 2017/5/19.
 */

public class RadarView extends View {

    private int defTextColor = Color.BLACK;
    private int defValueColor = Color.BLUE;
    private int defPaintColor = Color.GRAY;
    float defTextSize = 20;

    private int count = 6;
    private float angle = (float) (Math.PI * 2 / count);
    private float mRadius;
    private int mCenterX;
    private int mCenterY;
    private Paint mPaint;
    private Paint mValuePaint;
    private String[] titles;
    private double[] values;
    private Paint mTextPaint;
    private float maxValue = 100;


    public RadarView(Context context) {
        this(context,null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null){
            TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.RadarView,defStyleAttr,0);
            defPaintColor = array.getColor(R.styleable.RadarView_defPaintColor,Color.GRAY);
            defTextColor = array.getColor(R.styleable.RadarView_defTextColor,Color.BLACK);
            defValueColor = array.getColor(R.styleable.RadarView_defValueColor,Color.BLUE);
            defTextSize = array.getDimensionPixelSize(R.styleable.RadarView_defTextSize,20);
            maxValue = array.getFloat(R.styleable.RadarView_maxValue,100);
            array.recycle();
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(defPaintColor);
        mPaint.setStyle(Paint.Style.STROKE);

        mValuePaint = new Paint();
        mValuePaint.setAntiAlias(true);
        mValuePaint.setColor(defValueColor);
        mValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mTextPaint = new Paint();
        mTextPaint.setColor(defTextColor);
        mTextPaint.setTextSize(defTextSize);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setAntiAlias(true);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mRadius = Math.min(w,h) / 2 * 0.9f;

        mCenterX = w / 2;
        mCenterY = h / 2;

        invalidate();

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        count = Math.min(values.length, titles.length);
        drawPloygon(canvas);
        drawLines(canvas);
        drawText(canvas);
        drawRegion(canvas);
    }

    private void drawRegion(Canvas canvas) {
        mValuePaint.setAlpha(255);
        Path path = new Path();
        for (int i = 0;i < count;i++){
            double percent = values[i] / maxValue;
            float x = (float) (mCenterX + mRadius * Math.cos(angle * i) * percent);
            float y = (float) (mCenterY + mRadius * Math.sin(angle * i) * percent);
            if (i == 0){
                path.moveTo(x,mCenterY);
            }else {
                path.lineTo(x,y);
            }
            canvas.drawCircle(x,y,10,mValuePaint);
            canvas.drawText(String.valueOf(percent),x,y,mTextPaint);
        }
        path.close();
        mValuePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path,mValuePaint);
        mValuePaint.setAlpha(127);
        mValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path,mValuePaint);
    }

    private void drawText(Canvas canvas) {
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float height = metrics.descent - metrics.ascent;
        for (int i = 0;i < count;i++){
            float x = (float) (mCenterX + (mRadius + height / 2) * Math.cos(angle * i));
            float y = (float) (mCenterY + (mRadius + height / 2) * Math.sin(angle * i));
            if (angle * i >= 0 && angle * i <= Math.PI/2){
               canvas.drawText(titles[i],x,y,mTextPaint);
            }
            if (angle * i >= 3 * Math.PI / 2 && angle * i <= Math.PI * 2){
                canvas.drawText(titles[i],x,y,mTextPaint);
            }
            if (angle * i >= Math.PI && angle * i <= 3 * Math.PI/2){
                float dis = mTextPaint.measureText(titles[i]);
                canvas.drawText(titles[i],x - dis,y,mTextPaint);
            }
            if (angle * i >= Math.PI/2 && angle * i <= Math.PI){
                float dis = mTextPaint.measureText(titles[i]);
                canvas.drawText(titles[i],x - dis,y,mTextPaint);
            }

        }


    }

    private void drawLines(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < count;i++){
            path.reset();
            path.moveTo(mCenterX,mCenterY);
            float x = (float) (mCenterX + mRadius * Math.cos(angle * i));
            float y = (float) (mCenterY + mRadius * Math.sin(angle * i));
            path.lineTo(x,y);
            canvas.drawPath(path,mPaint);
        }

    }

    private void drawPloygon(Canvas canvas) {
        Path path = new Path();
        float r = mRadius /(count - 1);
        for (int i = 1; i < count ;i++){
            path.reset();
            float curR = r * i;
            for (int j = 0; j < count;j++){
                if (j == 0){
                    path.moveTo(mCenterX + curR,mCenterY);
                }else {
                    float x = (float) (mCenterX + curR * Math.cos(angle * j));
                    float y = (float) (mCenterY + curR * Math.sin(angle * j));
                    path.lineTo(x,y);
                }

            }
            path.close();
            canvas.drawPath(path,mPaint);
        }
    }

    public void setTitles(String[] titles){
        this.titles = titles;
    }
    public void setValues(double[] values){
        this.values = values;
    }
}
