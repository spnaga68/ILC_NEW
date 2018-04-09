package realmstudy.view;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import java.io.FileNotFoundException;

public class EditText extends AppCompatEditText {
    public EditText(Context context) throws FileNotFoundException {
        super(context);
        init(context, null, 0);
    }

    public EditText(Context context, AttributeSet attrs) throws FileNotFoundException {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public EditText(Context context, AttributeSet attrs, int defStyle) throws FileNotFoundException {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) throws FileNotFoundException {
//        if (!isInEditMode()) {
//            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppCompatTextView, defStyle, 0);
//            if (typedArray != null) {
//                if (typedArray.hasValue(0)) {
//                    String assetFontFileName = typedArray.getString(0);
//                    Typeface typeface = Typeface.createFromAsset(context.getAssets(), assetFontFileName);
//                    if (typeface == null) {
//                        throw new FileNotFoundException("Font file not found in asset : " + assetFontFileName);
//                    }
//                    setTypeface(typeface);
//                }
//                typedArray.recycle();
//            }
//        }
    }
}
