package cn.trunch.weidong.util.adapter.rvadapter;

import android.content.Context;
import android.view.LayoutInflater;


import java.util.List;

import cn.trunch.weidong.util.adapter.rvadapter.delegate.ItemViewDelegate;
import cn.trunch.weidong.util.adapter.rvadapter.delegate.ViewHolder;

/**
 * 公共的RecyclerView.Adapter
 *
 * @author yangfei
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {
    protected int mLayoutId;
    protected LayoutInflater mInflater;
    public CommonAdapter(Context context, List<T> list, final int layoutId) {
        super(context, list);
        super.addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
        // 解决全局刷新时图片闪烁
        //super.setHasStableIds(true);
    }
    public CommonAdapter(Context mContext, final int layoutId) {
        super(mContext);
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.mLayoutId = layoutId;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder mHolder, T t, int position) {
                CommonAdapter.this.convert(mHolder, t, position);
            }
        });
    }



    /**
     * 解决全局刷新时图片闪烁
     */
 /*   @Override
    public long getItemId(int position) {
        return position;
    }*/

    public abstract void convert(ViewHolder holder, T item, int position);

}