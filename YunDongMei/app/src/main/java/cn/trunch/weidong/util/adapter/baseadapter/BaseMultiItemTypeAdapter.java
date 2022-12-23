package cn.trunch.weidong.util.adapter.baseadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.ArrayList;
import java.util.List;

import cn.trunch.weidong.util.adapter.baseadapter.delegate.BaseItemViewDelegate;
import cn.trunch.weidong.util.adapter.baseadapter.delegate.BaseItemViewDelegateManager;
import cn.trunch.weidong.util.adapter.baseadapter.delegate.BaseViewHolder;

public class BaseMultiItemTypeAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mData;
    private BaseItemViewDelegateManager<T> mDelegateManager;
    public void addAllData(List<T> list) {
        mData.addAll(list);
        this.notifyDataSetChanged();
    }
    public void addData(T t){
        mData.add(t);
        this.notifyDataSetChanged();
    }
    public List<T> getAllData(){
        return mData;
    }
    public void clearData() {
        mData.clear();
        this.notifyDataSetChanged();
    }
    public BaseMultiItemTypeAdapter(Context mContext) {
        this.mContext = mContext;
        this.mData = new ArrayList<>(2);
        mDelegateManager = new BaseItemViewDelegateManager();
    }
    public BaseMultiItemTypeAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.mData = list;
        mDelegateManager = new BaseItemViewDelegateManager<>();
    }

    public BaseMultiItemTypeAdapter addItemViewDelegate(BaseItemViewDelegate<T> itemViewDelegate) {
        mDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    private boolean useItemViewDelegateManager() {
        return mDelegateManager.getItemViewDelegateCount() > 0;
    }

    @Override
    public int getViewTypeCount() {
        if (useItemViewDelegateManager()) {
            return mDelegateManager.getItemViewDelegateCount();
        }
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (useItemViewDelegateManager()) {
            int viewType = mDelegateManager.getItemViewType(mData.get(position), position);
            return viewType;
        }
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseItemViewDelegate itemViewDelegate = mDelegateManager.getItemViewDelegate(mData.get(position), position);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        BaseViewHolder mHolder = null;
        if (convertView == null) {
            View itemView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
            mHolder = new BaseViewHolder(mContext, itemView, parent, position);
            mHolder.setLayoutId(layoutId);
            onViewHolderCreated(mHolder, mHolder.getConvertView());
        } else {
            mHolder = (BaseViewHolder) convertView.getTag();
            mHolder.updatePosition(position);
        }
        convert(mHolder, getItem(position), position);
        return mHolder.getConvertView();
    }

    protected void convert(BaseViewHolder holder, T item, int position) {
        mDelegateManager.convert(holder, item, position);
    }

    public void onViewHolderCreated(BaseViewHolder holder, View itemView) {
    }

    /* -------------------------------------------------- */

    @Override
    public int getCount() {
        if (null == mData) {
            return 0;
        } else {
            return mData.size();
        }
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getData() {
        return mData;
    }

    /* -------------------------------------------------- */

}