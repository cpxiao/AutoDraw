package com.cpxiao.autodraw.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.cpxiao.R;
import com.cpxiao.gamelib.views.BaseSurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cpxiao on 2017/08/15.
 */

public class OfflineDrawView extends BaseSurfaceView {

    private List<Path> mPathList = new ArrayList<>();
    private Path mCurrentPath;

    private CircleBtn mRBCircleBtn;
    private CircleBtn[] mCircleList;
    private boolean mCircleListShown = false;

    private static Paint mCirclePaint = new Paint();


    static {
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setAntiAlias(true);//抗锯齿,一般加这个就可以了，加另外两个可能会卡
        //        mPaint.setDither(true);//防抖动
        //        mPaint.setFilterBitmap(true);//用来对位图进行滤波处理


    }

    public OfflineDrawView(Context context) {
        super(context);
    }

    public OfflineDrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initWidget() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(0.018F * mViewWidth);


        float cX = 0.9F * mViewWidth;
        float r = 0.06F * Math.min(mViewWidth, mViewHeight);

        mRBCircleBtn = new CircleBtn();

        mRBCircleBtn.cX = cX;
        mRBCircleBtn.cY = 0.9F * mViewHeight;
        mRBCircleBtn.r = r;
        mRBCircleBtn.color = Color.BLUE;


        //        int[] colorList = {Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED};
        String[] colorList = getResources().getStringArray(R.array.colorList);
        int mColorCount = colorList.length;
        mCircleList = new CircleBtn[mColorCount];
        for (int i = 0; i < mColorCount; i++) {
            CircleBtn circleBtn = new CircleBtn();
            circleBtn.cX = cX;
            circleBtn.cY = mRBCircleBtn.cY - (i + 1) * r * 2;
            circleBtn.r = 0.8F * r;
            circleBtn.color = Color.parseColor(colorList[i]);
            mCircleList[i] = circleBtn;
        }
    }

    @Override
    public void drawCache() {

        //        mCanvasCache.drawColor(Color.RED);
        //        mPaint.setColor(Color.BLUE);
        //        mPaint.setStrokeWidth(0.02F * mViewWidth);

        mPaint.setColor(mRBCircleBtn.color);
        for (Path p : mPathList) {
            mCanvasCache.drawPath(p, mPaint);
        }

        //绘制颜色按钮
        mRBCircleBtn.draw(mCanvasCache, mCirclePaint);
        if (mCircleListShown) {
            for (CircleBtn c : mCircleList) {
                c.draw(mCanvasCache, mCirclePaint);

            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int eventX = (int) event.getX();
        int eventY = (int) event.getY();
        if (action == MotionEvent.ACTION_DOWN) {
            mCurrentPath = new Path();
            mCurrentPath.moveTo(eventX, eventY);
            mPathList.add(mCurrentPath);
        } else if (action == MotionEvent.ACTION_MOVE) {
            mCurrentPath.lineTo(eventX, eventY);
        } else if (action == MotionEvent.ACTION_UP) {
            mCurrentPath = null;
        }
        myDraw();

        if (action == MotionEvent.ACTION_DOWN) {
            if (mRBCircleBtn.isSelected(eventX, eventY)) {
                mCircleListShown = !mCircleListShown;
            } else {
                if (mCircleListShown) {
                    for (CircleBtn c : mCircleList) {
                        if (c.isSelected(eventX, eventY)) {
                            mRBCircleBtn.color = c.color;
                        }
                    }
                }
                mCircleListShown = false;
            }
        }
        return true;
    }

    private class CircleBtn {
        float cX, cY, r;
        private int color;

        boolean isSelected(float x, float y) {
            return Math.sqrt(Math.pow(x - cX, 2) + Math.pow(y - cY, 2)) <= r;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public void draw(Canvas canvas, Paint paint) {
            paint.setColor(Color.WHITE);
            canvas.drawCircle(cX, cY, r, paint);

            paint.setAlpha(100);
            paint.setColor(color);
            canvas.drawCircle(cX, cY, 0.85F * r, paint);
            paint.setAlpha(255);
            canvas.drawCircle(cX, cY, 0.76F * r, paint);
        }
    }
}
