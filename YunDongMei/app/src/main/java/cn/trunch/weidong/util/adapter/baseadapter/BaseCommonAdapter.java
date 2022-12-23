package cn.trunch.weidong.util.adapter.baseadapter;

import android.content.Context;


import java.util.List;

import cn.trunch.weidong.util.adapter.baseadapter.delegate.BaseItemViewDelegate;
import cn.trunch.weidong.util.adapter.baseadapter.delegate.BaseViewHolder;

/**
 * 公共的BaseAdapter
 *
 * @author yangfei
 */
public abstract class BaseCommonAdapter<T> extends BaseMultiItemTypeAdapter<T> {
    private int mCurPosition = 0;

    public int getCurPosition() {
        return mCurPosition;
    }

    public void setCurPosition(int mCurPosition) {
        this.mCurPosition = mCurPosition;
    }
    public BaseCommonAdapter(Context context, List<T> list, final int layoutId) {
        super(context, list);

        super.addItemViewDelegate(new BaseItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(BaseViewHolder holder, T t, int position) {
                BaseCommonAdapter.this.convert(holder, t, position);
            }
        });
    }
    public BaseCommonAdapter(Context mContext, final int layoutId) {
        super(mContext);
        addItemViewDelegate(new BaseItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(BaseViewHolder mHolder, T t, int position) {
                BaseCommonAdapter.this.convert(mHolder, t, position);
            }
        });
    }
    @Override
    public abstract void convert(BaseViewHolder holder, T item, int position);

}