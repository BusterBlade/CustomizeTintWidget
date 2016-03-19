package tinttextview.com.wyn.customizetintwidget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.TintableBackgroundView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LruCache;
import android.widget.TextView;
/**
 * Created by nancy on 16-3-19.
 */
public class CustomizeTintTextView extends TextView implements TintableBackgroundView {

    private TintInfo mTintInfo;
    private static final PorterDuff.Mode DEFAULT_MODE = PorterDuff.Mode.SRC_IN;
    private static ColorFilterLruCache COLOR_FILTER_CACHE = new ColorFilterLruCache(6);
    public CustomizeTintTextView(Context context) {
        this(context, null);
    }

    public CustomizeTintTextView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public CustomizeTintTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setSupportBackgroundTintList(ColorStateList tintColorList) {
       if(mTintInfo == null){
           mTintInfo = new TintInfo();
       }
        mTintInfo.mTintList = tintColorList;
        mTintInfo.mHasTintList  = true;
        applySupportBackgroundTint();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Log.d("Test","drawableStateChanged");
        applySupportBackgroundTint();
    }

    @Nullable
    @Override
    public ColorStateList getSupportBackgroundTintList() {
        return mTintInfo.mTintList == null ? null:mTintInfo.mTintList;
    }

    @Override
    public void setSupportBackgroundTintMode(@Nullable PorterDuff.Mode tintMode) {
        if(mTintInfo == null){
            mTintInfo = new TintInfo();
        }
        mTintInfo.mTintMode = tintMode;
        mTintInfo.mHasTintMode  = true;
        applySupportBackgroundTint();
    }

    @Nullable
    @Override
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        return mTintInfo.mTintMode == null ? null:mTintInfo.mTintMode;
    }

    private void applySupportBackgroundTint() {
        Drawable background = getBackground();
        if(background != null){
            if(mTintInfo !=null && (mTintInfo.mHasTintList || mTintInfo.mHasTintMode)){
                background.setColorFilter(createColotFilter(
                        mTintInfo.mHasTintList ? mTintInfo.mTintList:null,
                        mTintInfo.mHasTintMode ? mTintInfo.mTintMode:DEFAULT_MODE,
                        getDrawableState()));
            }else{
                background.clearColorFilter();
            }
            if(Build.VERSION.SDK_INT <=10){
                // On Gingerbread, GradientDrawable does not invalidate itself when it's
                // ColorFilter has changed, so we need to force an invalidation
                background.invalidateSelf();
            }
            }
        }

    private static PorterDuffColorFilter createColotFilter(ColorStateList tint,
                                                          PorterDuff.Mode tintMode, final int[] state) {
        if (tint == null) {
            return null;
        }
        final int color = tint.getColorForState(state, Color.TRANSPARENT);
        if(color == Color.TRANSPARENT ){
            return null;
        }
        return getPorterDuffColorFilter(color, tintMode);
    }

    private static PorterDuffColorFilter getPorterDuffColorFilter(int color, PorterDuff.Mode tintMode) {
        if(tintMode == null) {
            return null;
        }
        PorterDuffColorFilter colorFilter = COLOR_FILTER_CACHE.get(color, tintMode);
        if(colorFilter == null){
            colorFilter = new PorterDuffColorFilter(color, tintMode);
            COLOR_FILTER_CACHE.put(color, tintMode,colorFilter);
        }
        return colorFilter;
    }

    private static class ColorFilterLruCache extends LruCache<Integer,PorterDuffColorFilter> {
        /**
         * @param maxSize for caches that do not override {@link #sizeOf}, this is
         *                the maximum number of entries in the cache. For all other caches,
         *                this is the maximum sum of the sizes of the entries in this cache.
         */
        public ColorFilterLruCache(int maxSize) {
            super(maxSize);
        }
        PorterDuffColorFilter get(int color,PorterDuff.Mode tintMode){
          int key = generateCacheKey(color,tintMode);
           return get(key);
        }
        void put(int color,PorterDuff.Mode tintMode,PorterDuffColorFilter colorFilter){
            int key = generateCacheKey(color,tintMode);
            put(key,colorFilter);
        }
        private int generateCacheKey(int color,PorterDuff.Mode tintMode){
            int hashCode = 1;
            hashCode = hashCode * 31 + color;
            hashCode = hashCode * 31 + tintMode.hashCode();
            return hashCode;
        }
    }
}
