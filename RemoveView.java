package com.example.glaring;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;


/**
 * -号
 */
public class RemoveView extends AddView {

    public RemoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //水平的横线，减号不需要垂直的横线了
        canvas.drawLine(HstartX, HstartY, HendX, HendY, paint);
    }
}