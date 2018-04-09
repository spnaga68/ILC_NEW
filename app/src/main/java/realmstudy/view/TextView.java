package realmstudy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import java.io.FileNotFoundException;
import java.util.WeakHashMap;

import realmstudy.R;


public class TextView extends AppCompatTextView {
    private static final int HTML_STYLE_STRIKE_THROUGH = 0;
    private static WeakHashMap<String, Typeface> fontMap = new WeakHashMap();

    public TextView(Context context) throws FileNotFoundException {
        super(context);
        init(context, null, 0);
    }

    public TextView(Context context, AttributeSet attrs) throws FileNotFoundException {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TextView(Context context, AttributeSet attrs, int defStyle) throws FileNotFoundException {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) throws FileNotFoundException {
//      if (!isInEditMode()) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.asset_font_file_names, defStyle, 0);
        if (typedArray != null) {
            if (typedArray.hasValue(0)) {
                String assetFontFileName = typedArray.getString(R.styleable.asset_font_file_names_asset_font_file_name);
                if (!fontMap.containsKey(assetFontFileName) || fontMap.get(assetFontFileName) == null) {
                    Typeface typeface = Typeface.createFromAsset(context.getAssets(), assetFontFileName);
                    if (typeface == null) {
                        throw new FileNotFoundException("Font file not found in asset : " + assetFontFileName);
                    }
                    fontMap.put(assetFontFileName, typeface);
                    setTypeface(typeface);
                } else {
                    setTypeface((Typeface) fontMap.get(assetFontFileName));
                }
            }
//                if (typedArray.hasValue(1)) {
//                    int style = typedArray.getInt(1, -1);
//                    if (style != -1) {
//                        switch (style) {
//                            case 0:
//                                setPaintFlags(getPaintFlags() | 16);
//                                break;
//                        }
//                    }
//                }
            typedArray.recycle();
        } else {
            System.out.println("customCame9");
        }
    }
}
