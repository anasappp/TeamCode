package cn.trunch.weidong.util.adapter.rvadapter.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import android.view.View;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import cn.trunch.weidong.R;
import cn.trunch.weidong.util.DensityUtil;

/**
 * RecyclerView-GridView的间隔线
 *
 * @author yangfei
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int mDrawableHeight;
    private int mHalfHeight;
    private Paint mPaint;

    /**
     * @param context       上下文
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public GridItemDecoration(Context context, float dividerHeight, @ColorRes int dividerColor) {
        this.mDrawableHeight = DensityUtil.dp2px(dividerHeight);
        this.mHalfHeight = mDrawableHeight / 2;
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaint.setColor(ContextCompat.getColor(context, dividerColor));
        this.mPaint.setStyle(Paint.Style.FILL);
    }

    public GridItemDecoration(Context context, @ColorRes int dividerColor) {
        this(context, 0.5f, dividerColor);
    }

    public GridItemDecoration(Context context) {
        this(context, 0.5f, R.color.gray3);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        // super.onDraw(canvas, parent, state);

        final int itemCount = parent.getAdapter().getItemCount();
        final int childCount = parent.getChildCount();
        int spanCount = getSpanCount(parent);

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
                            child.getRight() + rightMargin + mDrawableHeight,
                            top + getHeight(itemCount, spanCount, i),
                            mPaint);
                }

                // 画竖向
                final int top2 = child.getTop() - topMargin;
                final int bottom2 = child.getBottom() + bottomMargin;
                final int left2 = child.getRight() + rightMargin;
                final int right2 = left2 + mDrawableHeight;

                if (mPaint != null) {
                    canvas.drawRect(left2, top2, right2, bottom2, mPaint);
                }
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemCount = parent.getAdapter().getItemCount();
        int spanCount = getSpanCount(parent);
        int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();

        if ((pos + 1) % spanCount == 0) {
            outRect.set(mHalfHeight, 0, 0, getHeight(itemCount, spanCount, pos));
        } else {
            outRect.set(0, 0, mHalfHeight, getHeight(itemCount, spanCount, pos));
        }
    }

    /**
     * 获得列数
     */
    private int getSpanCount(RecyclerView parent) {
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    private int getHeight(int itemCount, int spanCount, int pos) {
        int line = (itemCount % spanCount == 0) ? (itemCount / spanCount) : (itemCount / spanCount + 1);
        if (line == pos / spanCount + 1) {
            return 0;
        } else {
            return mDrawableHeight;
        }
    }

}