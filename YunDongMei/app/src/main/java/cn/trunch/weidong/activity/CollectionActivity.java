package cn.trunch.weidong.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dou361.dialogui.DialogUIUtils;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.trunch.weidong.MyApplication;
import cn.trunch.weidong.R;
import cn.trunch.weidong.entity.VideoInfoEntity;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.util.RvUtil;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.util.adapter.CommonOnClickListener;
import cn.trunch.weidong.util.adapter.rvadapter.CommonAdapter;
import cn.trunch.weidong.util.adapter.rvadapter.delegate.ViewHolder;

import static com.baidu.mapapi.BMapManager.getContext;

public class CollectionActivity extends AppCompatActivity {
    private RecyclerView exploreRecyclerView;
    private CommonAdapter<VideoInfoEntity> commonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        StatusBarUtil.setWindowStatusBarColor(this, R.color.colorPrimary);
        ImageView emptyExitBtn = findViewById(R.id.emptyExitBtn);
        emptyExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        exploreRecyclerView = findViewById(R.id.recyclerview);
        commonAdapter = new CommonAdapter<VideoInfoEntity>(this, R.layout.item_explore_recy) {
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
                    holder.setText(R.id.circleDiaryLikeNum, "????????????");
                    holder.setTextColor(R.id.circleDiaryLikeNum, CollectionActivity.this.getResources().getColor(R.color.text_black));
                } else {
                    holder.setText(R.id.circleDiaryLikeNum, "????????????");
                    holder.setTextColor(R.id.circleDiaryLikeNum, CollectionActivity.this.getResources().getColor(R.color.bg_white));
                }
                likeButton.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        setHttpCollection(1, item,position);
                        item.setvType(1);
                        likeButton.setLiked(item.getvType() == 1);
                        if (item.getvType() == 1) {
                            holder.setText(R.id.circleDiaryLikeNum, "????????????");
                            holder.setTextColor(R.id.circleDiaryLikeNum, CollectionActivity.this.getResources().getColor(R.color.text_black));
                        } else {
                            holder.setText(R.id.circleDiaryLikeNum, "????????????");
                            holder.setTextColor(R.id.circleDiaryLikeNum, CollectionActivity.this.getResources().getColor(R.color.bg_white));
                        }
                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {
                        setHttpCollection(0, item,position);
                        item.setvType(0);
                        likeButton.setLiked(item.getvType() == 1);
                        if (item.getvType() == 1) {
                            holder.setText(R.id.circleDiaryLikeNum, "????????????");
                            holder.setTextColor(R.id.circleDiaryLikeNum, CollectionActivity.this.getResources().getColor(R.color.text_black));
                        } else {
                            holder.setText(R.id.circleDiaryLikeNum, "????????????");
                            holder.setTextColor(R.id.circleDiaryLikeNum, CollectionActivity.this.getResources().getColor(R.color.bg_white));
                        }
                    }
                });
                holder.setOnClickListener(R.id.item_lin, position, new CommonOnClickListener() {
                    @Override
                    public void clickListener(View view, int position) {
                        setHttpCollection((item.getvType() == 1 ? 0 : 1), item,position);
                    }
                });
            }
        };
        exploreRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        BmobQuery<VideoInfoEntity> query = new BmobQuery<>();
        query.setLimit(5).setSkip(1).findObjects(new FindListener<VideoInfoEntity>() {
            @Override
            public void done(List<VideoInfoEntity> object, BmobException e) {
                Log.e("TAG", "done: "+object+" "+e);
                List<VideoInfoEntity> videoInfoEntityList = new ArrayList<>();
                Set<String> stringSet = new HashSet<>();
                if (e == null) {
                    // ...
                    if (object != null) {
                        String udi = MyApplication.getInstance().getUserEntity().getObjectId();
                        for(VideoInfoEntity v:object){
                            if (v.getLikes() != null){
                                for (String str:v.getLikes()){
                                    if (!TextUtils.isEmpty(str)){
                                        if (str.equals(udi)){
                                            if (stringSet.add(v.getObjectId())){
                                                videoInfoEntityList.add(v);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        CollectionActivity.this.runOnUiThread(() -> {
                            if (commonAdapter != null) {
                                commonAdapter.clearData();
                                //Collections.shuffle(object);
                                commonAdapter.addAllData(videoInfoEntityList);
                            }
                        });
                    } else {
                        CollectionActivity.this.runOnUiThread(() -> {
                            DialogUIUtils.showToastCenter("????????????????????????");
                        });

                    }
                } else {
                    // ...
                    CollectionActivity.this.runOnUiThread(() -> {
                        DialogUIUtils.showToastCenter("????????????????????????");
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
                DialogUIUtils.showToastCenter("????????????????????????");
            }
        });*/
    }



    public void setHttpCollection(int type, VideoInfoEntity did,int position) {
        List<String> stringList = did.getLikes();
        if (1 == type) {
            if (stringList == null || stringList.size() == 0) {
                stringList = new ArrayList<>();
                stringList.add(ApiUtil.USER_ID);
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    commonAdapter.removeData(position);
                }
            });

        }

        did.setLikes(stringList);
        did.update(did.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //toast("????????????:"+p2.getUpdatedAt());
                } else {
                    //toast("???????????????" + e.getMessage());
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
                DialogUIUtils.showToastCenter("??????");
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("????????????????????????");
            }
        });*/
    }


    public void onBackPressed() {
        // ????????????????????????????????????????????????????????????????????????
        // ?????????Activity???onBackPress?????????NiceVideoPlayer????????????
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
