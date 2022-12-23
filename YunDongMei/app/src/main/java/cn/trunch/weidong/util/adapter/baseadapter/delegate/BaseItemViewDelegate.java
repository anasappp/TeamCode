package cn.trunch.weidong.util.adapter.baseadapter.delegate;

public interface BaseItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(BaseViewHolder holder, T item, int position);

}