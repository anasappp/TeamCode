package cn.trunch.weidong.util.adapter.rvadapter;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.trunch.weidong.util.adapter.rvadapter.delegate.ItemViewDelegate;
import cn.trunch.weidong.util.adapter.rvadapter.delegate.ItemViewDelegateManager;
import cn.trunch.weidong.util.adapter.rvadapter.delegate.ViewHolder;

public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<ViewHolder> {


    public List<T> getAllData(){
        return mData;
    }
    public T getDataByPosition(int position){
        return mData.get(position);
    }
    public void addData(T t){
        mData.add(t);
        this.notifyDataSetChanged();
    }
    public void addAllData(List<T> list){
        mData.addAll(list);
        this.notifyDataSetChanged();
    }

    public void removeData(int position){
        mData.remove(position);
        this.notifyDataSetChanged();
    }

    public void clearData(){
        mData.clear();
        this.notifyDataSetChanged();
    }

    protected Context mContext;
    protected List<T> mData;
    private ItemViewDelegateManager<T> mDelegateManager;

    public MultiItemTypeAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.mData = list;
        mDelegateManager = new ItemViewDelegateManager<>();
    }
    public MultiItemTypeAdapter(Context context) {
        this.mContext = context;
        this.mData = new ArrayList<>(2);
        mDelegateManager = new ItemViewDelegateManager<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) {return super.getItemViewType(position);}
        return mDelegateManager.getItemViewType(mData.get(position), position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewDelegate itemViewDelegate = mDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder mHolder = ViewHolder.createViewHolder(mContext, parent, layoutId);
        onViewHolderCreated(mHolder, mHolder.getConvertView());
        setListener(parent, mHolder, viewType);
        return mHolder;
    }

    public void onViewHolderCreated(ViewHolder holder, View itemView) {
    }

    public void convert(ViewHolder holder, T item) {
        mDelegateManager.convert(holder, item, holder.getAdapterPosition());
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }

    protected void setListener(final ViewGroup parent, final ViewHolder mHolder, int viewType) {
        if (!isEnabled(viewType)) {return;}
        mHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickListener) {
                    int position = mHolder.getAdapterPosition();
                    onItemClickListener.onItemClick(v, mHolder, position);
                }
            }
        });
        mHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onItemLongClickListener) {
                    int position = mHolder.getAdapterPosition();
                    return onItemLongClickListener.onItemLongClick(v, mHolder, position);
                }
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert(holder, mData.get(position));
    }

    /* -------------------------------------------------- */

    @Override
    public int getItemCount() {
        if (null == mData) {
            return 0;
        } else {
            return mData.size();
        }
    }

    public List<T> getData() {
        return mData;
    }


    /* -------------------------------------------------- */

    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiItemTypeAdapter addItemViewDelegate(int viewType, ItemViewDelegate<T> itemViewDelegate) {
        mDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mDelegateManager.getItemViewDelegateCount() > 0;
    }

    /**
     * 接口回调(点击)
     */
    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder mHolder, int position);
    }

    protected OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 接口回调(长按)
     */
    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, RecyclerView.ViewHolder mHolder, int position);
    }

    protected OnItemLongClickListener onItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

}