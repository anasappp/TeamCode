package cn.trunch.weidong.activity;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.HashMap;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.trunch.weidong.R;
import cn.trunch.weidong.entity.CollectionEntity;
import cn.trunch.weidong.entity.VideoInfoEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.util.RvUtil;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.util.adapter.CommonOnClickListener;
import cn.trunch.weidong.util.adapter.rvadapter.CommonAdapter;
import cn.trunch.weidong.util.adapter.rvadapter.delegate.ViewHolder;

public class VideoActivity extends AppCompatActivity {

    RecyclerView exploreRecyclerView;
    private CommonAdapter<CollectionEntity> commonAdapter;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        context = this;
        exploreRecyclerView = findViewById(R.id.recyclerview);
        StatusBarUtil.setWindowStatusBarColor(this, R.color.colorLight);
        ImageView emptyExitBtn = findViewById(R.id.emptyExitBtn);
        emptyExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        commonAdapter = new CommonAdapter<CollectionEntity>(context, R.layout.item_explore_recy) {
            @Override
            public void convert(ViewHolder holder, CollectionEntity item, int position) {
                LikeButton likeButton = holder.getView(R.id.circleDiaryLikeBtn);

                holder.setText(R.id.item_video_title, item.getVideoInfoEntity().getvTitle());
                holder.setText(R.id.item_video_time, item.getVideoInfoEntity().getvTime());
                JzvdStd jzvdStd = holder.getView(R.id.videoplayer);
                jzvdStd.setUp(item.getVideoInfoEntity().getvUrl(), "", Jzvd.SCREEN_NORMAL);
                Glide.with(context)
                        .setDefaultRequestOptions(
                                new RequestOptions()
                                        .frame(10000)
                        )
                        .load(item.getVideoInfoEntity().getvUrl())
                        .into(jzvdStd.posterImageView);

                likeButton.setLiked(item.getVideoInfoEntity().getvType() == 1);
                if (item.getVideoInfoEntity().getvType() == 1) {
                    holder.setText(R.id.circleDiaryLikeNum, "取消订阅");
                    holder.setTextColor(R.id.circleDiaryLikeNum, context.getResources().getColor(R.color.text_black));
                } else {
                    holder.setText(R.id.circleDiaryLikeNum, "立即订阅");
                    holder.setTextColor(R.id.circleDiaryLikeNum, context.getResources().getColor(R.color.bg_white));
                }
                likeButton.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        setHttpCollection(1, item.getVideoInfoEntity().getVideoId());
                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {
                        setHttpCollection(0, item.getVideoInfoEntity().getVideoId());
                    }
                });
                holder.setOnClickListener(R.id.item_lin, position, new CommonOnClickListener() {
                    @Override
                    public void clickListener(View view, int position) {
                        setHttpCollection((item.getVideoInfoEntity().getvType() == 1 ? 0 : 1), item.getVideoInfoEntity().getVideoId());
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
        setHttpDataInfo();
    }

    public void setHttpDataInfo() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("userId", ApiUtil.USER_ID);
        new UniteApi(ApiUtil.VIDEO_MY_ALL_LIST, hm).get(new ApiListener() {
            @Override
            public void success(Api api) {

                UniteApi u = (UniteApi) api;
                Gson g = new Gson();
                List<CollectionEntity> o = g.fromJson(u.getJsonData().toString(), new TypeToken<List<CollectionEntity>>() {
                }.getType());
                if (o.size() <= 0) {
                    findViewById(R.id.layout_empty).setVisibility(View.VISIBLE);
                    exploreRecyclerView.setVisibility(View.GONE);
                } else {
                    findViewById(R.id.layout_empty).setVisibility(View.GONE);
                    exploreRecyclerView.setVisibility(View.VISIBLE);
                }
                if (commonAdapter != null) {
                    commonAdapter.clearData();
                    commonAdapter.addAllData(o);
                }
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动，请重试");
            }
        });
    }


    public void setHttpCollection(int type, String did) {
        HashMap<String, String> hm = new HashMap<>();
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
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    public void onBackPressed() {
        // 在全屏或者小窗口时按返回键要先退出全屏或小窗口，
        // 所以在Activity中onBackPress要交给NiceVideoPlayer先处理。
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
