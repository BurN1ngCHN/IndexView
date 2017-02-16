package com.example.indexviewdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.indexviewdemo.R;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by burning on 2017/1/22.
 */
public class IndexView extends View {

    private int textSize;
    private final int defaultTextSize = 16;
    private int singleHeight = 0;
    private final int backgroundColor = Color.GRAY;
    private boolean isTouched = false;
    private onItemTouchListener mOnItemTouchListener;
    private TextView textView;
    private Rect rect = new Rect();

    private int fingerPosition = 0;

    public static String[] INDEX_STRING = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};


    //数据源
    private List<String> datas;
    //画笔
    private Paint mPaint;
    //区域画笔
    private Paint divPaint;

    private Random random = new Random();

    public IndexView(Context context) {
        this(context, null);
    }

    public IndexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IndexView, defStyleAttr, 0);
        textSize = a.getDimensionPixelSize(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                defaultTextSize, getResources().getDisplayMetrics()));
        a.recycle();

        init();
    }

    private void init() {
        datas = Arrays.asList(INDEX_STRING);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setTextSize(textSize);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextScaleX(1.2f);
        mPaint.setFakeBoldText(true);

        setOnItemTouchListener(new onItemTouchListener() {
            @Override
            public void onViewTouched(String text) {
                if (textView != null){
                    textView.setText(text);
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onViewTouchEnd() {
                if (textView != null){
                    textView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量宽高和模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //最终宽高
        int width = 0, height = 0;
        if (rect != null) {
            rect = new Rect();
        }
        for (String item : datas){
            mPaint.getTextBounds(item, 0, item.length(), rect);
            //取出单个最大宽高
            width = Math.max(width, rect.width());
            height = Math.max(height, rect.height());
        }
        height *= datas.size();
        //宽的模式
        if (widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else if (widthMode == MeasureSpec.AT_MOST){
            width = Math.min(widthSize, width);
        }
        //高的模式
        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else if (heightMode == MeasureSpec.AT_MOST){
            height = Math.min(heightSize, height);
        }
        setMeasuredDimension(width, height);
        singleHeight = getMeasuredHeight() / datas.size();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //获取基准点
        int start = getPaddingTop();

        if (isTouched){
            int index = (fingerPosition - start) / singleHeight;
            index = index < datas.size()-1 ? index : datas.size()-1;
            if (mOnItemTouchListener != null) {
                mOnItemTouchListener.onViewTouched(datas.get(index));
            }
        }else {
            if (mOnItemTouchListener != null) {
                mOnItemTouchListener.onViewTouchEnd();
            }
        }

        int i = 0;
        for (String item : datas){
            mPaint.setColor(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            canvas.drawText(item, getWidth()/2 - mPaint.measureText(item)/2, start + singleHeight*i + singleHeight/2, mPaint);
            i++;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        if (event.getY() < getPaddingTop()){
            fingerPosition = getPaddingTop();
        } else {
            fingerPosition = (int) event.getY();
        }
        switch (action){
            case MotionEvent.ACTION_DOWN:
                isTouched = true;
                break;
            case MotionEvent.ACTION_MOVE:
                isTouched = true;
                break;
            case MotionEvent.ACTION_UP:
                isTouched = false;
                break;
        }
        invalidate();

        return true;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void setOnItemTouchListener(onItemTouchListener onItemTouchListener){
        this.mOnItemTouchListener = onItemTouchListener;
    }

    public interface onItemTouchListener{
        void onViewTouched(String text);
        void onViewTouchEnd();
    }
}
