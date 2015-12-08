package com.bzt.num_progressbarlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by SHIBW-PC on 2015/12/8.
 */
public class CircleProgressBar extends View {

    private float textSize;
    private int textColor;

    private int backgroundColor;
    private int strokeWidth;

    private static final int DEFAULT_STROKE = 5;
    private static final float DEFAULT_TEXTSIZE = 14;
    private static final int MAX_PROGRESS = 100;
    private static final int MAX_ANGLE = 360;

    private Paint mTextPaint,mCirclePaint;

    private static final int FLAG_WIDTH = 0;
    private static final int FLAG_HEIGHT = 1;

    private int width,height;

    private RectF mRect;

    private int startAngle = -90;
    private int sweepAngle =0;

    private int length;
    private int mCircleXY;

    private int mProgress = 0;

    private ProgressListener mListener;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);

        init();
    }

    private void init() {

        mTextPaint = new Paint();
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mCirclePaint = new Paint();
        mCirclePaint.setColor(backgroundColor);
        mCirclePaint.setStrokeWidth(strokeWidth);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);

    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);

        textColor = ta.getColor(R.styleable.CircleProgressBar_circle_textColor,context.getResources().getColor(R.color.default_text_color));
        backgroundColor = ta.getColor(R.styleable.CircleProgressBar_circle_backgroundColor, context.getResources().getColor(R.color.default_background_color));
        strokeWidth = ta.getInt(R.styleable.CircleProgressBar_circle_strokeWidth, DEFAULT_STROKE);
        textSize = ta.getDimension(R.styleable.CircleProgressBar_circle_textSize,DisplayUtil.sp2px(context,DEFAULT_TEXTSIZE));

        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measureUtils(widthMode, widthSize, FLAG_WIDTH), measureUtils(heightMode, heightSize, FLAG_HEIGHT));

        initDimesion();
    }

    private void initDimesion() {

        length = Math.min(width,height);
        mCircleXY = length/2;
        mRect = new RectF(length*0.1f,length*0.1f,length*0.9f,length*0.9f);

    }

    private int measureUtils(int mode, int size,int flag) {
        int result = 0;

        if (mode == MeasureSpec.EXACTLY){
            //表示match_parent和精确值
            setValue(flag,size);
            return size;
        }else {
            result = 200;
            if (mode == MeasureSpec.AT_MOST){
                //精确值
                setValue(flag,Math.min(result,size));
                return Math.min(result,size);
            }
            setValue(flag,Math.min(result,size));
        }
        return result;
    }

    private void setValue(int flag,int size) {
        switch (flag){
            case FLAG_WIDTH:
                width = size;break;
            case FLAG_HEIGHT:
                height = size;break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //先画圆
        canvas.drawArc(mRect,startAngle,sweepAngle,false,mCirclePaint);
        //画文字
        canvas.drawText(getProgressText(),0,getProgressText().length(),mCircleXY,mCircleXY+(textSize/4),mTextPaint);
    }

    public String getProgressText() {
        return mProgress + "%";
    }

    public int getProgress(){
        return mProgress;
    }

    public void setProgress(int progress){

        if (mProgress<100){
            if (progress>100){
                progress = 100;
            }
            if (mProgress <= 0) {
                setVisibility(INVISIBLE);
            }else {
                setVisibility(VISIBLE);
            }
            mProgress = progress;
            sweepAngle = (int) ((float)mProgress/MAX_PROGRESS*MAX_ANGLE);

            if (mListener != null){
                mListener.progressChanged(mProgress,MAX_PROGRESS);
            }

            invalidate();
        }

    }

    public void setOnProgressListener(ProgressListener listener){
        mListener = listener;
    }

    public void clearProgress(){
        setProgress(0);
    }
}
