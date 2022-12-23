package cn.trunch.weidong.util.adapter.rvadapter.delegate;

public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T item, int position);

}