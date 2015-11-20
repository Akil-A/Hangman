package com.example.android.s181142;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by akil on 20.08.15.
 */
public class HangmanView extends View {

    private Paint mPaintLine = new Paint();
    private Paint mPaintHangMan = new Paint();
    private Paint mRopeLine = new Paint();
    private Paint mPaintCircle = new Paint();
    private int mAttempts = 0;
    Context mContext;

    //Need this constructor when the View is used in the layout file
    public HangmanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mPaintLine.setColor(Color.WHITE);
        mPaintLine.setStrokeWidth(12f);

        mPaintHangMan.setColor(Color.WHITE);
        mPaintHangMan.setStrokeWidth(6f);

        mRopeLine.setColor(Color.WHITE);
        mRopeLine.setStrokeWidth(9f);

        mPaintCircle.setColor(Color.WHITE);
        mPaintCircle.setStyle(Paint.Style.STROKE);
        mPaintCircle.setStrokeWidth(6f);
        mPaintCircle.setStrokeCap(Paint.Cap.ROUND);
    }

    public HangmanView(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = getWidth();
        int height = getHeight();
        //if phone is in horizontal position use different variables
        if(width>height)
            width*=0.75;
        int startHeight = (int)(height*0.1);
        int leftPadding = (int)(width*0.1);
        int topHorizontalLine = (int)(width*0.7);
        if(width>height)
            topHorizontalLine = (int)(width*0.5);
        int startBottomHorizontalLine = (int)(width*0.02);
        int endBottomHorizontalLine = (int)(width*0.3);
        if(width>height)
            endBottomHorizontalLine = (int)(width*0.25);
        int ropeLine = (int)(height*0.3);
        int radius = (int)(height*0.05);
        if(width>height)
            radius = (int)(height*0.08);
        int adjustRope = (int) (leftPadding * 1.90);
        int adjustHangMan = (int)(topHorizontalLine*0.97);
        int startHead = ropeLine+radius;
        int endHead = startHead+radius;
        int bodyLine = (int)(height*0.6);
        if(width>height)
            bodyLine = (int)(height*0.65);
        int startArm = (int)(endHead*1.25);
        int endArmY = (int)(height*0.44);
        if(width>height)
            endArmY = (int)(height*0.5);
        int endLeftArmX = (int)(width*0.48);
        if(width>height)
            endLeftArmX = (int)(width*0.35);
        int endRightArmX = (int)(width*0.9);
        if(width>height)
            endRightArmX = (int)(width*0.62);
        int endLegY = (int)(height*0.7);
        if(width>height)
            endLegY = (int)(height*0.80);
        int endLeftLegX = (int)(width*0.47);
        if(width>height)
            endLeftLegX = (int)(width*0.36);
        int endRightLegX = (int)(width*0.9);
        if(width>height)
            endRightLegX = (int)(width*0.62);
        int diagonalLine = (int)(height*0.25);

        switch (mAttempts) {

           case 6:
               canvas.drawLine(adjustHangMan, bodyLine, endRightLegX, endLegY, mPaintHangMan); //right leg
           case 5:
               canvas.drawLine(adjustHangMan, bodyLine, endLeftLegX, endLegY, mPaintHangMan); //left leg
           case 4:
               canvas.drawLine(adjustHangMan,startArm, endRightArmX, endArmY, mPaintHangMan); //right arm
           case 3:
                canvas.drawLine(adjustHangMan,startArm,endLeftArmX, endArmY, mPaintHangMan);  //left arm
           case 2:
               canvas.drawLine(adjustHangMan, endHead, adjustHangMan, bodyLine, mPaintHangMan);  //body
           case 1:
               canvas.drawCircle(adjustHangMan, startHead, radius, mPaintCircle);  //head
           case 0:
               canvas.drawLine(leftPadding,startHeight,leftPadding,height,mPaintLine);  //vertical line
               canvas.drawLine(leftPadding, startHeight, topHorizontalLine, startHeight, mPaintLine);  //horizontal line
               canvas.drawLine(startBottomHorizontalLine, height, endBottomHorizontalLine, height, mPaintLine); //bottom line
               canvas.drawLine(leftPadding, diagonalLine, (int)(adjustRope*1.6), startHeight, mPaintLine); //diagonal line
               canvas.drawLine(adjustHangMan, startHeight, adjustHangMan, ropeLine, mRopeLine);  //rope line
        }
        super.onDraw(canvas);
    }

    public void increaseAttempts() {
        mAttempts++;
        invalidate();
    }

    public void updateView(int attempts) {
        mAttempts = attempts;
        invalidate();
    }
}
