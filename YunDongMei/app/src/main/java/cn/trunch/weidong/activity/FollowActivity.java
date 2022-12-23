package cn.trunch.weidong.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.dou361.dialogui.DialogUIUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import cn.trunch.weidong.entity.FollowInfoEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.util.RvUtil;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.util.TextUtil;
import cn.trunch.weidong.util.adapter.CommonOnClickListener;
import cn.trunch.weidong.util.adapter.rvadapter.CommonAdapter;
import cn.trunch.weidong.util.adapter.rvadapter.MultiItemTypeAdapter;
import cn.trunch.weidong.util.adapter.rvadapter.delegate.ViewHolder;
import cn.trunch.weidong.vo.DiaryUserVO;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class FollowActivity extends AppCompatActivity {
    RecyclerView exploreRecyclerView;
    private CommonAdapter<FollowInfoEntity> commonAdapter;
    private Context context;
    private FloatingActionButton exploreRefreshBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);

        context = this;
        exploreRecyclerView = findViewById(R.id.recyclerview);
        exploreRefreshBtn = findViewById(R.id.exploreRefreshBtn);
        StatusBarUtil.setWindowStatusBarColor(this, R.color.colorLight);
        ImageView emptyExitBtn = findViewById(R.id.emptyExitBtn);
        emptyExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        commonAdapter = new CommonAdapter<FollowInfoEntity>(context, R.layout.view_square_question_item) {
            @Override
            public void convert(ViewHolder holder, FollowInfoEntity item, int position) {
                LikeButton likeButton = holder.getView(R.id.circleDiaryLikeBtn);
                TextView questionTitle = holder.getView(R.id.questionTitle);
                ImageView questionPreview = holder.getView(R.id.questionPreview);
                ImageView questionUHead = holder.getView(R.id.questionUHead);
                questionTitle.setText(item.getDiaryEntity().getDiaryTitle());
                TextUtil.setBold(questionTitle);
                holder.setText(R.id.questionContent, item.getDiaryEntity().getDiaryContent());
                holder.setText(R.id.questionBounty, item.getDiaryEntity().getDiaryReward() + ".00");
                holder.setText(R.id.questionInfo, item.getUserEntity().getuNickname()
                        + "    " + item.getDiaryEntity().getDiaryReadNum() + "人浏览");

                //预览图
                questionPreview.setVisibility(View.VISIBLE);
                if (item.getDiaryEntity().getDiaryImgPreview().contains("default"))
                    questionPreview.setVisibility(View.GONE);
                else {
                    questionPreview.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(item.getDiaryEntity().getDiaryImgPreview())
                            .apply(bitmapTransform(new MultiTransformation<>(
                                    new CropSquareTransformation(),
                                    new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)
                            )))
                            .into(questionPreview);
                }
                Glide.with(context)
                        .load(item.getUserEntity().getuAvatar())
                        .apply(bitmapTransform(new CircleCrop()))
                        .into(questionUHead);
            }
        };
        exploreRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        exploreRecyclerView.setHasFixedSize(true);
        exploreRecyclerView.setAdapter(commonAdapter);
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder mHolder, int position) {
                Intent questionSeeIntent = new Intent(context, SquareQuestionSeeActivity.class);
                questionSeeIntent.putExtra("did", commonAdapter.getDataByPosition(position).getDiaryEntity().getDiaryId());
                context.startActivity(questionSeeIntent);
            }
        });
        setHttpDataInfo();

        exploreRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHttpDataInfo();
            }
        });
    }

    public void setHttpDataInfo() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("userId", ApiUtil.USER_ID);
        new UniteApi(ApiUtil.VIDEO_MY_FOLLOW_ALL_LIST, hm).get(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi u = (UniteApi) api;
                Gson g = new Gson();
                List<FollowInfoEntity> o = g.fromJson(u.getJsonData().toString(), new TypeToken<List<FollowInfoEntity>>() {
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
}
