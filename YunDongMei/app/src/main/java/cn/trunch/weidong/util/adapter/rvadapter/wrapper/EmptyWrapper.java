package cn.trunch.weidong.util.adapter.rvadapter.wrapper;


import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.trunch.weidong.util.adapter.rvadapter.delegate.ViewHolder;
import cn.trunch.weidong.util.adapter.rvadapter.utils.WrapperUtils;


public class EmptyWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_TYPE_EMPTY = Integer.MAX_VALUE - 1;

    private RecyclerView.Adapter mInnerAdapter;
    private View mEmptyView;
    private int mEmptyLayoutId;

    public EmptyWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    private boolean isEmpty() {
        return (mEmptyView != null || mEmptyLayoutId != 0) && mInnerAdapter.getItemCount() == 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isEmpty()) {
            ViewHolder mHolder;
            if (mEmptyView != null) {
                mHolder = ViewHolder.createViewHolder(parent.getContext(), mEmptyView);
            } else {
                mHolder = ViewHolder.createViewHolder(parent.getContext(), parent, mEmptyLayoutId);
            }
            return mHolder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, (layoutManager, oldLookup, position) -> {
            if (isEmpty()) {
                return layoutManager.getSpanCount();
            }
            if (oldLookup != null) {
                return oldLookup.getSpanSize(position);
            }
            return 1;
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder mHolder) {
        mInnerAdapter.onViewAttachedToWindow(mHolder);
        if (isEmpty()) {
            WrapperUtils.setFullSpan(mHolder);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isEmpty()) {
            return ITEM_TYPE_EMPTY;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        if (isEmpty()) {
            return;
        }
        mInnerAdapter.onBindViewHolder(mHolder, position);
    }

    @Override
    public int getItemCount() {
        if (isEmpty()) return 1;
        return mInnerAdapter.getItemCount();
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public void setEmptyView(int layoutId) {
        mEmptyLayoutId = layoutId;
    }

}