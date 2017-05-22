package realmstudy.lib.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.Switch;
import android.widget.TextView;

import realmstudy.R;


public class AutoResizeTextView extends TextView {

    private float strokeWidth;
    int strokeColor, solidColor;
    Context c;

    public AutoResizeTextView(Context context) {
        super(context);
        this.c = context;
    }

    public AutoResizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.c = context;
    }

    public AutoResizeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.c = context;
    }

//    public AutoResizeTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    @Override
    public void draw(Canvas canvas) {

        Paint circlePaint = new Paint();
        circlePaint.setColor(solidColor);
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        Paint strokePaint = new Paint();
        strokePaint.setColor(strokeColor);
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        int h = this.getHeight();
        int w = this.getWidth();

        int diameter = ((h > w) ? h : w);
        int radius = diameter / 2;

        this.setHeight(diameter);
        this.setWidth(diameter);

        canvas.drawCircle(diameter / 2, diameter / 2, radius, strokePaint);

        canvas.drawCircle(diameter / 2, diameter / 2, radius - strokeWidth, circlePaint);

        super.draw(canvas);
    }

    public void setStrokeWidth(int dp) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        strokeWidth = dp * scale;

    }

    public void setStrokeColor(String color) {
        strokeColor = Color.parseColor(color);
    }

    public void setSolidColor(String color) {
        solidColor = Color.parseColor(color);

    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.length() == 2) {
            setTextSize(10);
            colorForText(text.charAt(1));

        } else if (text.length() == 1) {
            setTextSize(10);
            colorForText(text.charAt(0));
        }

        invalidate();


    }

    private void colorForText(char text) {
        String s = String.valueOf(text);
        System.out.println("_____VV" + s);
        switch (s) {
            case "0":
                solidColor = ContextCompat.getColor(c, R.color.dots_c);
                break;
            case "1":
                solidColor = ContextCompat.getColor(c, R.color.single_c);
                break;
            case "2":
                solidColor = ContextCompat.getColor(c, R.color.double_c);
                break;
            case "3":
                solidColor = ContextCompat.getColor(c, R.color.three_c);
                break;
            case "4":
                solidColor = ContextCompat.getColor(c, R.color.four_c);
                break;
            case "6":
                solidColor = ContextCompat.getColor(c, R.color.six_c);
                break;
            case "o":
                solidColor = ContextCompat.getColor(c, R.color.wicket_c);
                break;
            default:
                solidColor = ContextCompat.getColor(c, R.color.extra_c);
                break;


        }

    }


}