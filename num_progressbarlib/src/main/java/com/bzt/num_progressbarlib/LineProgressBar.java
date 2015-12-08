package com.bzt.num_progressbarlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by SHIBW-PC on 2015/12/8.
 */
public class LineProgressBar extends View {

    private float textSize;
    private int textColor;

    private int backgroundColor;
    private int strokeWidth;

    private static final int DEFAULT_STROKE = 10;
    private static final float DEFAULT_TEXTSIZE = 14;
    private static final int MAX_PROGRESS = 100;

    private Paint mTextPaint,mLinePaint;

    private static final int FLAG_WIDTH = 0;
    private static final int FLAG_HEIGHT = 1;

    private int width,height;

    private RectF mRect;

    private int length;
    private int altitude;
    private int mCircleXY;

    private int mProgress = 0;
    private int mFinishedLine = 0;

    private ProgressListener mListener;

    public LineProgressBar(Context context) {
        this(context, null);
    }

    public LineProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
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

        mLinePaint = new Paint();
        mLinePaint.setColor(backgroundColor);
        mLinePaint.setStrokeWidth(strokeWidth);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.FILL);

    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineProgressBar);

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

        length = width;
        altitude = height;
        mCircleXY = length/2;
        mRect = new RectF(0,0,length,length);

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
        canvas.drawLine(0, height/2, mFinishedLine, height/2, mLinePaint);
        canvas.drawText(getProgressText(),0,getProgressText().length(),(float) mProgress / MAX_PROGRESS * length+getProgressText().length()*textSize/2,height/2+textSize/4,mTextPaint);
    }

    private String getProgressText() {

        return mProgress+"%";
    }

    public void setProgress(int progress){

        if (mProgress < 100){
            if (progress>100)
            {
                progress = 100;
            }
            if (mProgress <= 0) {
                setVisibility(INVISIBLE);
            }else {
                setVisibility(VISIBLE);
            }
            mProgress = progress;
            mFinishedLine = (int) ((float)mProgress/MAX_PROGRESS*length);

            if (mListener != null){
                mListener.progressChanged(mProgress,MAX_PROGRESS);
            }
            invalidate();
        }


    }

    public void setOnProgressListener(ProgressListener listener){
        mListener = listener;
    }

    public int getProgress() {
        return mProgress;
    }
}
