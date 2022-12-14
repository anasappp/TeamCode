package cn.trunch.weidong.util.adapter.rvadapter.divider;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import android.view.View;

import androidx.annotation.ColorRes;
import androidx.recyclerview.widget.RecyclerView;

import cn.trunch.weidong.util.DensityUtil;
import cn.trunch.weidong.util.ResUtil;

/**

 */
public class ListDivider extends RecyclerView.ItemDecoration {

    public static ListDivider get(@ColorRes int colorRes) {
        return new ListDivider(0.5f, colorRes, false);
    }

    public static ListDivider get(float height, @ColorRes int colorRes) {
        return new ListDivider(height, colorRes, false);
    }

    public static ListDivider getLast(@ColorRes int colorRes) {
        return new ListDivider(0.5f, colorRes, true);
    }

    public static ListDivider getLast(float height, @ColorRes int colorRes) {
        return new ListDivider(height, colorRes, true);
    }

    private int mHeight;
    private Paint mPaint;
    private boolean mIsLast;

    public ListDivider(float height, @ColorRes int colorRes, boolean isLast) {
        this.mHeight = DensityUtil.dp2px(height);
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(ResUtil.getColor(colorRes));
        this.mIsLast = isLast;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // super.getItemOffsets(outRect, view, parent, state);

        int itemPos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        int lastPos = parent.getAdapter().getItemCount() - 1;

        if (itemPos == lastPos && !mIsLast) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(0, 0, 0, mHeight);
        }
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        // super.onDraw(canvas, parent, state);

        final int childCount = parent.getChildCount();
        int lastPos = parent.getAdapter().getItemCount() - 1;

        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            final int top = child.getBottom() + params.bottomMargin;
            if (i == lastPos && !mIsLast) {
                canvas.drawRect(left, top, right, top, mPaint);
            } else {
                canvas.drawRect(left, top, right, top + mHeight, mPaint);
            }
        }
    }

}