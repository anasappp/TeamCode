package cn.trunch.weidong.fragment;

import android.content.Context;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.trunch.weidong.R;
import cn.trunch.weidong.adapter.ItemReplyAdapter;
import cn.trunch.weidong.entity.PageEntity;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.vo.ComUserVO;

public class ItemReplyFragment extends Fragment {
    private View view;
    private Context context;
    private String did;
    private XRecyclerView itemReplyList;
    private ItemReplyAdapter itemReplyAdapter;
    private PageEntity page;
    private String questionId;
    int i = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null){
            did = bundle.getString("did");
            questionId = bundle.getString("questionId");
        }
        view = inflater.inflate(R.layout.fragment_item_reply, container, false);
        context = getActivity();

        init();
        initListener();
        initData();

        return view;
    }

    private void init() {
        itemReplyList = view.findViewById(R.id.replyList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        itemReplyList.setLayoutManager(linearLayoutManager);
        itemReplyAdapter = new ItemReplyAdapter(context);
        itemReplyList.setAdapter(itemReplyAdapter);
        itemReplyList.setLoadingMoreEnabled(false);
        page = new PageEntity();
    }

    private void initListener() {
        itemReplyList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {
                //loadData();
            }
        });
    }

    public void initData() {
        page.setCurrentPage(0);
        i = 0;
        BmobQuery<ComUserVO> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("comBUid",questionId);
        bmobQuery.findObjects(new FindListener<ComUserVO>() {
            @Override
            public void done(List<ComUserVO> list, BmobException e) {
                    if (e == null){
                        if (list != null){
                            for (ComUserVO c:list){
                                if (!TextUtils.isEmpty(c.getComUid())){
                                    BmobQuery<UserEntity> bmobQuery1 = new BmobQuery<>();
                                    bmobQuery1.getObject(c.getComUid(), new QueryListener<UserEntity>() {
                                        @Override
                                        public void done(UserEntity object,BmobException e) {
                                            if(e==null){
                                                //toast("查询成功");
                                                c.setUser(object);
                                                i++;
                                                if (i == list.size()){
                                                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                                                        itemReplyAdapter.initData(list);
                                                        itemReplyList.refreshComplete();
                                                    });
                                                }
                                            }else{
                                                //toast("查询失败：" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }

                        }
                    }else {
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DialogUIUtils.showToastCenter("网络波动，请重试");
                                itemReplyList.refreshComplete();
                            }
                        });
                    }
            }
        });
        /*HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("did", did);
        hm.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hm.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.COM_LIST, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi u = (UniteApi) api;
                Gson g = new Gson();
                List<ComUserVO> o = g.fromJson(u.getJsonData().toString(), new TypeToken<List<ComUserVO>>() {
                }.getType());
                page = g.fromJson(u.getJsonPage().toString(), PageEntity.class);
                itemReplyAdapter.initData(o);
                itemReplyList.refreshComplete();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动，请重试");
                itemReplyList.refreshComplete();
            }
        });*/
    }

    private void loadData() {
        if (!page.getNextPage()) {
            itemReplyList.setNoMore(true);
            itemReplyList.setFootViewText("正在加载更多", "已经到底了");
            DialogUIUtils.showToastCenter("已经到底了");
            itemReplyList.loadMoreComplete();
            return;
        }
        page.setCurrentPage(0);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("did", did);
        hm.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hm.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.COM_LIST, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi u = (UniteApi) api;
                Gson g = new Gson();
                List<ComUserVO> o = g.fromJson(u.getJsonData().toString(), new TypeToken<List<ComUserVO>>() {
                }.getType());
                page = g.fromJson(u.getJsonPage().toString(), PageEntity.class);
                itemReplyAdapter.loadData(o);
                itemReplyList.loadMoreComplete();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动，请重试");
                itemReplyList.loadMoreComplete();
            }
        });
    }

    public static ItemReplyFragment newInstance(String itemId,String questionId) {
        ItemReplyFragment fragment = new ItemReplyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("did", itemId);
        bundle.putString("questionId", questionId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
