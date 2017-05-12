package realmstudy.lib.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;


public class AutoResizeTextView extends TextView {

    private float strokeWidth;
    int strokeColor,solidColor;
    public AutoResizeTextView(Context context) {
        super(context);
    }

    public AutoResizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoResizeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AutoResizeTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void draw(Canvas canvas) {

        Paint circlePaint = new Paint();
        circlePaint.setColor(solidColor);
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        Paint strokePaint = new Paint();
        strokePaint.setColor(strokeColor);
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        int  h = this.getHeight();
        int  w = this.getWidth();

        int diameter = ((h > w) ? h : w);
        int radius = diameter/2;

        this.setHeight(diameter);
        this.setWidth(diameter);

        canvas.drawCircle(diameter / 2 , diameter / 2, radius, strokePaint);

        canvas.drawCircle(diameter / 2, diameter / 2, radius-strokeWidth, circlePaint);

        super.draw(canvas);
    }

    public void setStrokeWidth(int dp)
    {
        float  scale = getContext().getResources().getDisplayMetrics().density;
        strokeWidth = dp*scale;

    }

    public void setStrokeColor(String color)
    {
        strokeColor = Color.parseColor(color);
    }

    public void setSolidColor(String color)
    {
        solidColor = Color.parseColor(color);

    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if(text.length()==2){
            setTextSize(10);


        }
        else if(text.length()==1)
            setTextSize(10);


    }




}