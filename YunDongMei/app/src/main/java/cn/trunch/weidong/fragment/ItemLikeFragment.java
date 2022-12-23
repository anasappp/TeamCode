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
import cn.bmob.v3.listener.QueryListener;
import cn.trunch.weidong.R;
import cn.trunch.weidong.adapter.ItemLikeAdapter;
import cn.trunch.weidong.adapter.SquareConsultAdapter;
import cn.trunch.weidong.entity.DiaryEntity;
import cn.trunch.weidong.entity.PageEntity;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.vo.ComUserVO;

public class ItemLikeFragment extends Fragment {
    private View view;
    private Context context;
    private String did;
    private XRecyclerView itemLikeList;
    private ItemLikeAdapter itemLikeAdapter;
    private PageEntity page;
    List<String> questionId;
    int i = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            did = bundle.getString("did");
        }

        view = inflater.inflate(R.layout.fragment_item_like, container, false);
        context = getActivity();

        init();
        initListener();
        initData();

        return view;
    }

    private void init() {
        itemLikeList = view.findViewById(R.id.likeList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        itemLikeList.setLayoutManager(linearLayoutManager);
        itemLikeAdapter = new ItemLikeAdapter(context);
        itemLikeList.setAdapter(itemLikeAdapter);
        itemLikeList.setLoadingMoreEnabled(false);
        page = new PageEntity();
    }

    private void initListener() {
        itemLikeList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {
                loadData();
            }
        });
    }

    public void initData() {
        page.setCurrentPage(0);
        i = 0;
        List<UserEntity> userEntities = new ArrayList<>();
        if (questionId != null && questionId.size() > 0) {

            for (String c : questionId) {
                if (!TextUtils.isEmpty(c)) {
                    BmobQuery<UserEntity> bmobQuery1 = new BmobQuery<>();
                    bmobQuery1.getObject(c, new QueryListener<UserEntity>() {
                        @Override
                        public void done(UserEntity object, BmobException e) {
                            if (e == null) {
                                //toast("查询成功");
                                if (object != null){
                                    userEntities.add(object);
                                    i++;
                                    if (i == questionId.size()) {
                                        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                                            itemLikeAdapter.initData(userEntities);
                                            itemLikeList.refreshComplete();
                                        });
                                    }
                                }
                            } else {
                                Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                                    DialogUIUtils.showToastCenter("网络波动，请重试");
                                    itemLikeList.refreshComplete();
                                });
                                //toast("查询失败：" + e.getMessage());
                            }
                        }
                    });
                }
            }
        }else {
            itemLikeAdapter.initData(userEntities);
            itemLikeList.refreshComplete();
        }
       /* HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("id", did);
        hm.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hm.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.LIKE_LIST, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi u = (UniteApi) api;
                Gson g = new Gson();
                List<UserEntity> o = g.fromJson(u.getJsonData().toString(), new TypeToken<List<UserEntity>>() {}.getType());
                page = g.fromJson(u.getJsonPage().toString(), PageEntity.class);
                itemLikeAdapter.initData(o);
                itemLikeList.refreshComplete();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动，请重试");
                itemLikeList.refreshComplete();
            }
        });*/
    }

    public void setQuestionId(List<String> questionId) {
        this.questionId = questionId;
    }

    private void loadData() {
       /* if (!page.getNextPage()) {
            itemLikeList.setNoMore(true);
            itemLikeList.setFootViewText("正在加载更多", "已经到底了");
            DialogUIUtils.showToastCenter("已经到底了");
            itemLikeList.loadMoreComplete();
            return;
        }*/
        page.setCurrentPage(0);
        /*HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("id", did);
        hm.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hm.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.LIKE_LIST, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi u = (UniteApi) api;
                Gson g = new Gson();
                List<UserEntity> o = g.fromJson(u.getJsonData().toString(), new TypeToken<List<UserEntity>>() {
                }.getType());
                page = g.fromJson(u.getJsonPage().toString(), PageEntity.class);
                itemLikeAdapter.loadData(o);
                itemLikeList.loadMoreComplete();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动，请重试");
                itemLikeList.loadMoreComplete();
            }
        });*/
    }


    public static ItemLikeFragment newInstance(String itemId, List<String> questionId) {
        ItemLikeFragment fragment = new ItemLikeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("did", itemId);
        fragment.questionId = questionId;
        fragment.setArguments(bundle);
        return fragment;
    }
}
