package cn.trunch.weidong.fragment;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dou361.dialogui.DialogUIUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.util.AppUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.trunch.weidong.MyApplication;
import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.MainActivity;
import cn.trunch.weidong.entity.CollectionEntity;
import cn.trunch.weidong.entity.VideoInfoEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UploadApi;
import cn.trunch.weidong.image.BannerImageLoader;
import cn.trunch.weidong.util.RvUtil;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.util.adapter.CommonOnClickListener;
import cn.trunch.weidong.util.adapter.rvadapter.CommonAdapter;
import cn.trunch.weidong.util.adapter.rvadapter.delegate.ViewHolder;
import cn.trunch.weidong.vo.ComUserVO;

public class ExploreFragment extends Fragment {
    private View view;
    private Context context;
    private MainActivity activity;

    private RecyclerView exploreRecyclerView;
    private FloatingActionButton exploreRefreshBtn;
    private FloatingActionButton exploreHomeBtn;
    Banner mBanner;
    private CommonAdapter<VideoInfoEntity> commonAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_explore, container, false);
        context = getActivity();
        activity = (MainActivity) getActivity();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initListener();

    }



    private void init() {
        exploreRecyclerView = view.findViewById(R.id.recyclerview);
        exploreRefreshBtn = view.findViewById(R.id.exploreRefreshBtn);
        exploreHomeBtn = view.findViewById(R.id.exploreHomeBtn);
        mBanner = view.findViewById(R.id.home_banner);


        commonAdapter = new CommonAdapter<VideoInfoEntity>(context, R.layout.item_explore_recy) {
            @Override
            public void convert(ViewHolder holder, VideoInfoEntity item, int position) {
                LikeButton likeButton = holder.getView(R.id.circleDiaryLikeBtn);

                holder.setText(R.id.item_video_title, item.getvTitle());
                holder.setText(R.id.item_video_time, item.getvTime());
                JzvdStd jzvdStd = holder.getView(R.id.videoplayer);
                jzvdStd.setUp(item.getvUrl(), "", Jzvd.SCREEN_NORMAL);
                Glide.with(getContext())
                        .setDefaultRequestOptions(
                                new RequestOptions()
                                        .frame(10000)
                        )
                        .load(item.getvUrl())
                        .into(jzvdStd.posterImageView);
                List<String> stringList = item.getLikes();
                if (stringList == null)
                    stringList = new ArrayList<>();
                int a = 0;
                for (int i = 0; i < stringList.size(); i++) {
                    if (stringList.get(i).equals(ApiUtil.USER_ID)) {
                        a++;
                    }
                }
                if (a == 0){
                    item.setvType(0);
                }else {
                    item.setvType(1);
                }
                likeButton.setLiked(item.getvType() == 1);
                if (item.getvType() == 1) {
                    holder.setText(R.id.circleDiaryLikeNum, "已经订阅");
                    holder.setTextColor(R.id.circleDiaryLikeNum, context.getResources().getColor(R.color.text_black));
                } else {
                    holder.setText(R.id.circleDiaryLikeNum, "立即订阅");
                    holder.setTextColor(R.id.circleDiaryLikeNum, context.getResources().getColor(R.color.bg_white));
                }
                likeButton.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        setHttpCollection(1, item);
                        item.setvType(1);
                        likeButton.setLiked(item.getvType() == 1);
                        if (item.getvType() == 1) {
                            holder.setText(R.id.circleDiaryLikeNum, "已经订阅");
                            holder.setTextColor(R.id.circleDiaryLikeNum, context.getResources().getColor(R.color.text_black));
                        } else {
                            holder.setText(R.id.circleDiaryLikeNum, "立即订阅");
                            holder.setTextColor(R.id.circleDiaryLikeNum, context.getResources().getColor(R.color.bg_white));
                        }
                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {
                        setHttpCollection(0, item);
                        item.setvType(0);
                        likeButton.setLiked(item.getvType() == 1);
                        if (item.getvType() == 1) {
                            holder.setText(R.id.circleDiaryLikeNum, "已经订阅");
                            holder.setTextColor(R.id.circleDiaryLikeNum, context.getResources().getColor(R.color.text_black));
                        } else {
                            holder.setText(R.id.circleDiaryLikeNum, "立即订阅");
                            holder.setTextColor(R.id.circleDiaryLikeNum, context.getResources().getColor(R.color.bg_white));
                        }
                    }
                });
                holder.setOnClickListener(R.id.item_lin, position, new CommonOnClickListener() {
                    @Override
                    public void clickListener(View view, int position) {
                        setHttpCollection((item.getvType() == 1 ? 0 : 1), item);
                    }
                });
            }
        };
        exploreRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        exploreRecyclerView.setHasFixedSize(true);
        exploreRecyclerView.setAdapter(commonAdapter);
        RvUtil.solveNestQuestion(exploreRecyclerView);
        exploreRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                Jzvd jzvd = view.findViewById(R.id.videoplayer);
                if (jzvd != null && Jzvd.CURRENT_JZVD != null &&
                        jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                    if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                        Jzvd.releaseAllVideos();
                    }
                }
            }
        });

        List<String> strings = new ArrayList<>();
        strings.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3053550508,3307802824&fm=15&gp=0.jpg");
        strings.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4073595050,2408397814&fm=26&gp=0.jpg");
        strings.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1372525336,3472197421&fm=26&gp=0.jpg");
        strings.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3934390074,1014297908&fm=26&gp=0.jpg");
        InitBanner(strings);

    }


    public void setHttpDataInfo() {
        BmobQuery<VideoInfoEntity> query = new BmobQuery<>();
        query.setLimit(5).setSkip(1).findObjects(new FindListener<VideoInfoEntity>() {
            @Override
            public void done(List<VideoInfoEntity> object, BmobException e) {
                Log.e("TAG", "done: "+object+" "+e);
                if (e == null) {
                    // ...
                    if (object != null) {
                        activity.runOnUiThread(() -> {
                            if (commonAdapter != null) {
                                commonAdapter.clearData();
                                //Collections.shuffle(object);
                                commonAdapter.addAllData(object);
                            }
                        });
                    } else {
                        activity.runOnUiThread(() -> {
                            DialogUIUtils.showToastCenter("网络波动，请重试");
                        });

                    }
                } else {
                    // ...
                    activity.runOnUiThread(() -> {
                        DialogUIUtils.showToastCenter("网络波动，请重试");
                    });
                }
            }
        });

       /* HashMap<String, String> hm = new HashMap<>();
        hm.put("userId", ApiUtil.USER_ID);
        new UniteApi(ApiUtil.VIDEO_ALL_LIST, hm).get(new ApiListener() {
            @Override
            public void success(Api api) {

                UniteApi u = (UniteApi) api;
                Gson g = new Gson();
                List<VideoInfoEntity> o = g.fromJson(u.getJsonData().toString(), new TypeToken<List<VideoInfoEntity>>() {}.getType());
                if (commonAdapter != null) {
                    commonAdapter.clearData();
                    commonAdapter.addAllData(o);
                }
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动，请重试");
            }
        });*/
    }

    public void setHttpCollection(int type, VideoInfoEntity did) {
        List<String> stringList = did.getLikes();
        if (1 == type) {
            if (stringList == null || stringList.size() == 0) {
                stringList = new ArrayList<>();
                stringList.add(MyApplication.getInstance().getUserEntity().getObjectId());
            } else {
                for (int i = 0; i < stringList.size(); i++) {
                    if (stringList.get(i).equals(ApiUtil.USER_ID)) {
                        stringList.remove(i);
                        i--;
                    }
                }
                stringList.add(ApiUtil.USER_ID);
            }
        } else {
            if (stringList == null || stringList.size() == 0) {
                stringList = new ArrayList<>();
            } else {
                for (int i = 0; i < stringList.size(); i++) {
                    if (stringList.get(i).equals(ApiUtil.USER_ID)) {
                        stringList.remove(i);
                        i--;
                    }
                }
            }
        }

        did.setLikes(stringList);
        did.update(did.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //toast("更新成功:"+p2.getUpdatedAt());
                } else {
                    //toast("更新失败：" + e.getMessage());
                }
            }
        });
       /* HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("cDataId", did);
        hm.put("cType", type + "");
        new UniteApi(ApiUtil.VIDEO_ADD_COLLECTION, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                setHttpDataInfo();
                DialogUIUtils.showToastCenter("成功");
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动，请重试");
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mBanner) {
            mBanner.startAutoPlay();
        }
        if (exploreRefreshBtn != null){
            exploreRefreshBtn.performClick();
        }
    }


    @Override
    public void onStop() {
        super.onStop();

        if (null != mBanner) {
            mBanner.stopAutoPlay();
        }
    }

    /**
     * 轮播图
     */
    public void InitBanner(List<String> list) {
        /*首页轮播图*/
//        mBanner.setLayoutParams(new ConstraintLayout.LayoutParams(ScreenUtil.getScreenWidth(), (ScreenUtil.getScreenWidth() / 2) * 1));//轮播图2:1
        // 设置banner样式setIndicatorGravity
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        // 设置图片加载器
        mBanner.setImageLoader(new BannerImageLoader());
        List<Integer> list1 = new ArrayList<>();
        list1.add(R.drawable.banner_1);
        list1.add(R.drawable.banner_2);
        list1.add(R.drawable.banner_3);
        list1.add(R.drawable.banner_4);
        //设置图片集合
        mBanner.setImages(list1);
        // 设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        // 设置自动轮播,默认为true
        mBanner.isAutoPlay(true);
        // 设置轮播时间
        mBanner.setDelayTime(3000);
        // 设置指示器位置(当banner模式中有指示器时)
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        // banner设置方法全部调用完毕时最后调用
        mBanner.start();
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

             /*   Bundle bundle1 = new Bundle();
                bundle1.putString("dataBean", new Gson().toJson(commonAdapter.getDataByPosition(position)));
                gotoActivity(ProjectDetailsActivity.class, bundle1);*/
            }
        });

    }

    private void initListener() {

        exploreRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //exploreWebView.reload(); //刷新
                setHttpDataInfo();
            }
        });
        exploreHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                exploreWebView.clearHistory();
//                exploreWebView.loadUrl("http://47.102.200.22/#/?token=" + ApiUtil.USER_TOKEN);
            }
        });
    }

}
