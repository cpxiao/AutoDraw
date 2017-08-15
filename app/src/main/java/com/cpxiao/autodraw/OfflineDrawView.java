package com.cpxiao.autodraw;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.cpxiao.gamelib.views.BaseSurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cpxiao on 2017/08/15.
 */

public class OfflineDrawView extends BaseSurfaceView {

    private List<Path> mPathList = new ArrayList<>();
    private Path mCurrentPath;

    public OfflineDrawView(Context context) {
        super(context);
    }

    public OfflineDrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initWidget() {
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void drawCache() {

        //        mCanvasCache.drawColor(Color.RED);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(0.02F * mViewWidth);

        for (Path p : mPathList) {
            mCanvasCache.drawPath(p, mPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (action == MotionEvent.ACTION_DOWN) {
            mCurrentPath = new Path();
            mCurrentPath.moveTo(x, y);
            mPathList.add(mCurrentPath);
        } else if (action == MotionEvent.ACTION_MOVE) {
            mCurrentPath.lineTo(x, y);
        } else if (action == MotionEvent.ACTION_UP) {
            mCurrentPath = null;
        }
        myDraw();
        return true;
    }
}
