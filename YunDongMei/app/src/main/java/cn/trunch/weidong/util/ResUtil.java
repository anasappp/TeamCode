package cn.trunch.weidong.util;


import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import cn.trunch.weidong.MyApplication;

/**
 * Class
 *
 * @author yangfei
 * @date 2018/9/2 14:53
 */
public class ResUtil {

    private ResUtil() {
        // no instance
    }

    @NonNull
    public static String getString(@StringRes int strRes) {
        return MyApplication.getAppContext().getString(strRes);
    }

    @ColorInt
    public static int getColor(@ColorRes int colorRes) {
        return ContextCompat.getColor(
                MyApplication.getAppContext(),
                colorRes);
    }

}