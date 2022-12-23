package cn.trunch.weidong.util.adapter.rvadapter.divider;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import android.view.View;

import androidx.annotation.ColorRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import cn.trunch.weidong.util.DensityUtil;
import cn.trunch.weidong.util.ResUtil;


/**
 * Class
 *
 * @author yangfei
 * @date 2018/9/1 16:35
 */
public class GridDivider extends RecyclerView.ItemDecoration {

    public static GridDivider get(@ColorRes int colorRes) {
        return new GridDivider(0.5f, colorRes);
    }

    public static GridDivider get(float height, @ColorRes int colorRes) {
        return new GridDivider(height, colorRes);
    }

    private int mHeight;
    private int mHalfHeight;
    private Paint mPaint;

    public GridDivider(float height, @ColorRes int colorRes) {
        this.mHeight = DensityUtil.dp2px(height);
        this.mHalfHeight = this.mHeight / 2;
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(ResUtil.getColor(colorRes));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // super.getItemOffsets(outRect, view, parent, state);

        int itemCount = parent.getAdapter().getItemCount();
        int spanCount = getSpanCount(parent);
        int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();

        if ((pos + 1) % spanCount == 0) {
            outRect.set(mHalfHeight, 0, 0, getHeight(itemCount, spanCount, pos));
        } else {
            outRect.set(0, 0, mHalfHeight, getHeight(itemCount, spanCount, pos));
        }
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        // super.onDraw(canvas, parent, state);

        final int itemCount = parent.getAdapter().getItemCount();
        int spanCount = getSpanCount(parent);
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int leftMargin = params.leftMargin;
            int topMargin = params.topMargin;
            int rightMargin = params.rightMargin;
            int bottomMargin = params.bottomMargin;

            int itemPosition = i + 1;
            if (itemPosition % spanCount == 0) {

                // 画横向
                final int top = child.getBottom() + bottomMargin;
                if (mPaint != null) {
                    canvas.drawRect(
                            child.getLeft() - leftMargin,
                            top,
                            child.getRight() + rightMargin,
                            top + getHeight(itemCount, spanCount, i),
                            mPaint);
                }

            } else {

                // 画横向
                final int top = child.getBottom() + bottomMargin;
                if (mPaint != null) {
                    canvas.drawRect(
                            child.getLeft() - leftMargin,
                            top,
                            child.getRight() + rightMargin + mHeight,
                            top + getHeight(itemCount, spanCount, i),
                            mPaint);
                }

                // 画竖向
                final int top2 = child.getTop() - topMargin;
                final int bottom2 = child.getBottom() + bottomMargin;
                final int left2 = child.getRight() + rightMargin;
                final int right2 = left2 + mHeight;

                if (mPaint != null) {
                    canvas.drawRect(left2, top2, right2, bottom2, mPaint);
                }
            }
        }
    }

    /**
     * 获得列数
     */
    private int getSpanCount(RecyclerView parent) {
        int spanCount = -1;
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) manager).getSpanCount();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) manager).getSpanCount();
        }
        return spanCount;
    }

    private int getHeight(int itemCount, int spanCount, int pos) {
        int line = (itemCount % spanCount == 0) ? (itemCount / spanCount) : (itemCount / spanCount + 1);
        if (line == pos / spanCount + 1) {
            return 0;
        } else {
            return mHeight;
        }
    }

}