package realmstudy.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build.VERSION;
import android.renderscript.Allocation;
import android.renderscript.Allocation.MipmapControl;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.squareup.picasso.Transformation;

public class BlurTransformation implements Transformation {
    RenderScript rs;

    public BlurTransformation(Context context) {
        this.rs = RenderScript.create(context);
    }

    public Bitmap transform(Bitmap bitmap) {
        try {
            if (VERSION.SDK_INT < 17) {
                return bitmap;
            }
            Bitmap blurredBitmap = bitmap.copy(Config.ARGB_8888, true);
            Allocation input = Allocation.createFromBitmap(this.rs, blurredBitmap, MipmapControl.MIPMAP_FULL, 128);
            Allocation output = Allocation.createTyped(this.rs, input.getType());
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(this.rs, Element.U8_4(this.rs));
            script.setInput(input);
            script.setRadius(20.0f);
            script.forEach(output);
            output.copyTo(blurredBitmap);
            bitmap.recycle();
            return blurredBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public String key() {
        return "blur";
    }
}
