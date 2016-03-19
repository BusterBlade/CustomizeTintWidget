package tinttextview.com.wyn.customizetintwidget;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import java.lang.ref.WeakReference;

/**
 * Created by nancy on 16-3-19.
 */
public class CustomizeImageSpan extends ImageSpan {
    private WeakReference<Drawable> wr;
    public CustomizeImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }


    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        Drawable drawable = getCacheDrawable();
        if(drawable !=null && fm!=null){
            Rect rect = drawable.getBounds();
            /**
             *The core is that you need recalculate the descent and ascent of the text,
             * using the middle of bitmap as new baseline of text
             * the value of fm.ascent is negative because it is above the baseline
             * the value of fm.descent is positive because it's below the baseline
             * so the height of text = fm.descent + fm.ascent
             */
            int margin = rect.bottom - (fm.descent - fm.ascent);
            if(margin > 0){
                //equals:rect.bottom /2 - (fm.descent - fm.ascent)/2
                // to get how many distances you need move from old baseline to new baseline
                fm.descent = fm.descent +margin/2;//move the descent according the new baseline
                fm.bottom = fm.descent;
                fm.ascent = -(rect.bottom - fm.descent);
                fm.top = fm.ascent;
            }
            return rect.right;
        }
        return super.getSize(paint, text, start, end, fm);
    }

    private Drawable getCacheDrawable(){

        Drawable drawable = null;
        if(wr!=null){
            drawable = wr.get();
        }
        if(drawable == null){
            drawable = getDrawable();
            wr = new WeakReference<Drawable>(drawable);
        }
        return drawable;
    }
}
