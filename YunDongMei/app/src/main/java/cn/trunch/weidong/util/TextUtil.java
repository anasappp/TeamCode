package cn.trunch.weidong.util;


import android.text.TextPaint;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class TextUtil {
    public static void setBold(@NonNull TextView textView) {
        TextPaint paint = textView.getPaint();
        paint.setFakeBoldText(true);
    }
}
