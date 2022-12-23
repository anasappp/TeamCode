package cn.trunch.weidong.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.loader.ImageLoader;

import cn.trunch.weidong.R;


/**
 * Banner的图片加载器
 *
 * @author yangfei
 */
public class BannerImageLoader extends ImageLoader {
    private static RequestOptions normalOptions = new RequestOptions()
            .error(R.mipmap.data_null)
            .dontAnimate()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    @Override
    public void displayImage(Context mContext, Object path, ImageView mImageView) {
        Glide.with(mContext)
                .load(path)
                .apply(normalOptions)
                .into(mImageView);
    }

   /* @Override
    public NiceImageView createImageView(Context mContext) {
        NiceImageView mImageView = new NiceImageView(mContext);
        mImageView.setCornerRadius(10);
        return mImageView;
    }*/

}