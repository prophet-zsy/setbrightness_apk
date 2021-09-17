package com.example.glaring;

import android.content.Context;
import android.view.View;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * +号
 */
public class AddView extends View {

    protected Paint paint;
    protected int HstartX, HstartY, HendX, HendY;//水平的线
    protected int SstartX, SstartY, SsendX, SsendY;//垂直的线
    protected int paintWidth = 2;//初始化加号的粗细为10
    protected int paintColor = Color.BLACK;//画笔颜色黑色
    protected int padding = 3;//默认3的padding

    public int getPadding() {
        return padding;
    }

    //让外界调用，修改padding的大小
    public void setPadding(int padding) {
        SsendY = HendX = padding;
        SstartY = HstartX = padding;
    }

    //让外界调用，修改加号颜色
    public void setPaintColor(int paintColor) {
        paint.setColor(paintColor);
    }

    //让外界调用，修改加号粗细
    public void setPaintWidth(int paintWidth) {
        paint.setStrokeWidth(paintWidth);
    }

    public AddView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setColor(paintColor);
        paint.setStrokeWidth(paintWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width;
        if (widthMode == MeasureSpec.EXACTLY) {
            //  MeasureSpec.EXACTLY表示该view设置的确切的数值
            width = widthSize;
        } else {
            width = 60;//默认值
        }
        SstartX = SsendX = HstartY = HendY = width / 2;
        SsendY = HendX = width - getPadding();
        SstartY = HstartX = getPadding();
        //这样做是因为加号宽高是相等的，手动设置宽高
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //水平的横线
        canvas.drawLine(HstartX, HstartY, HendX, HendY, paint);
        //垂直的横线
        canvas.drawLine(SstartX, SstartY, SsendX, SsendY, paint);
    }
}

