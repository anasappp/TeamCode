package cn.trunch.weidong.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dou361.dialogui.DialogUIUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONException;

import java.io.Serializable;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.trunch.weidong.MyApplication;
import cn.trunch.weidong.R;
import cn.trunch.weidong.entity.DiaryEntity;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.fragment.ItemLikeFragment;
import cn.trunch.weidong.fragment.ItemReplyFragment;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.step.config.Constant;
import cn.trunch.weidong.util.InputUtil;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.util.TimeUtil;
import cn.trunch.weidong.vo.ComUserVO;
import cn.trunch.weidong.vo.DiaryUserVO;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class CircleDiarySeeActivity extends AppCompatActivity {

    private String diaryId;
    private DiaryUserVO diary;
    private Dialog dialog;

    //头部
    private ImageView circleDiarySeeBackBtn;
    private TextView circleDiarySeeTitle;
    private ImageView circleDiarySeeUHead;
    private TextView circleDiarySeeUName;
    private TextView circleDiarySeeUInfo;
    private TextView circleDiarySeeInfo;
    private WebView circleDiarySeeContent;
    private TextView circleDiarySeeField;
    //下部导航
    private String[] titles = {"留言", "点赞"};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ItemLikeFragment itemLikeFragment;
    private ItemReplyFragment itemReplyFragment;
    private SlidingTabLayout circleDiarySeeNav;
    private ViewPager circleDiarySeePager;
    // 底部操作
    private View circleDiarySeeCover;
    private LinearLayout circleDiarySeeReplyBox;
    private LikeButton circleDiarySeeLikeBtn;
    private LikeButton circleDiarySeeCollectBtn;
    private EditText circleDiarySeeReply;
    private TextView circleDiarySeeReplySendBtn;
    private DiaryEntity mDiaryEntity;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_diary_see);
        diaryId = getIntent().getStringExtra("diaryId");
        mDiaryEntity = (DiaryEntity) getIntent().getSerializableExtra("data");
        StatusBarUtil.setWindowStatusBarColor(this, R.color.colorPrimary);

        init();
        initNav();
        initListener();
        initHeadData();

//        DialogUIUtils.init(this);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        // 头部
        circleDiarySeeBackBtn = findViewById(R.id.circleDiarySeeBackBtn);
        circleDiarySeeTitle = findViewById(R.id.circleDiarySeeTitle);
        circleDiarySeeUHead = findViewById(R.id.circleDiarySeeUHead);
        circleDiarySeeUName = findViewById(R.id.circleDiarySeeUName);
        circleDiarySeeUInfo = findViewById(R.id.circleDiarySeeUInfo);
        circleDiarySeeInfo = findViewById(R.id.circleDiarySeeInfo);
        circleDiarySeeContent = findViewById(R.id.circleDiarySeeWebView);
        WebSettings webSettings = circleDiarySeeContent.getSettings();
        webSettings.setJavaScriptEnabled(true);
        circleDiarySeeField = findViewById(R.id.circleDiarySeeField);
        // 导航
        circleDiarySeeNav = findViewById(R.id.circleDiarySeeNav);
        circleDiarySeePager = findViewById(R.id.circleDiarySeePager);
        // 底部操作
        circleDiarySeeCover = findViewById(R.id.circleDiarySeeCover);
        circleDiarySeeReplyBox = findViewById(R.id.circleDiarySeeReplyBox);
        circleDiarySeeLikeBtn = findViewById(R.id.circleDiarySeeLikeBtn);
        circleDiarySeeCollectBtn = findViewById(R.id.circleDiarySeeCollectBtn);
        circleDiarySeeReply = findViewById(R.id.circleDiarySeeReply);
        circleDiarySeeReplySendBtn = findViewById(R.id.circleDiarySeeReplySendBtn);
    }

    private void initNav() {
        itemReplyFragment = ItemReplyFragment.newInstance(diaryId,mDiaryEntity.getObjectId());
        itemLikeFragment = ItemLikeFragment.newInstance(diaryId,mDiaryEntity.getDiaryLikUserNids());
        fragments.add(itemReplyFragment);
        fragments.add(itemLikeFragment);
        //配置并绑定 Fragment + ViewPager
        //绑定TabLayout和ViewPager
        circleDiarySeeNav.setViewPager(circleDiarySeePager, titles, this, fragments);
    }

    private void initListener() {
        circleDiarySeeBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        circleDiarySeeCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleDiarySeeReply.clearFocus();
            }
        });
        circleDiarySeeReplyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拦截事件
            }
        });
        circleDiarySeeReply.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    circleDiarySeeCover.setVisibility(View.VISIBLE);
                    circleDiarySeeLikeBtn.setVisibility(View.GONE);
                    //circleDiarySeeCollectBtn.setVisibility(View.GONE);
                    circleDiarySeeReplySendBtn.setVisibility(View.VISIBLE);
                    circleDiarySeeReply.setMinLines(3);
                    circleDiarySeeReply.setMaxLines(10);
                    InputUtil.showInput(circleDiarySeeReply);
                } else {
                    circleDiarySeeCover.setVisibility(View.GONE);
                    circleDiarySeeLikeBtn.setVisibility(View.VISIBLE);
                    //circleDiarySeeCollectBtn.setVisibility(View.VISIBLE);
                    circleDiarySeeReplySendBtn.setVisibility(View.GONE);
                    circleDiarySeeReply.setMinLines(1);
                    circleDiarySeeReply.setMaxLines(1);
                    InputUtil.hideInput(CircleDiarySeeActivity.this);
                }
            }
        });
        circleDiarySeeReply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().replaceAll(" ", "")
                        .replaceAll("\n", "").length() == 0) {
                    circleDiarySeeReplySendBtn.setEnabled(false);
                    circleDiarySeeReplySendBtn.setTextColor(getResources().getColor(R.color.colorDefaultText));
                } else {
                    circleDiarySeeReplySendBtn.setEnabled(true);
                    circleDiarySeeReplySendBtn.setTextColor(getResources().getColor(R.color.colorTheme));
                }
            }
        });
        circleDiarySeeReplySendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment();
            }
        });
        circleDiarySeeLikeBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                like(0);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                like(1);
            }
        });
        circleDiarySeeCollectBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });
    }

    private void initHeadData() {
        BmobQuery<UserEntity> query = new BmobQuery<>();
        query.getObject(mDiaryEntity.getDiaryUid(), new QueryListener<UserEntity>() {
            @Override
            public void done(UserEntity userEntity, BmobException e) {
                if (e == null){
                    if (userEntity != null){
                        runOnUiThread(() -> {
                            Glide.with(circleDiarySeeUHead)
                                    .load(userEntity.getuAvatar())
                                    .apply(bitmapTransform(new CircleCrop()))
                                    .into(circleDiarySeeUHead);
                            circleDiarySeeUName.setText(userEntity.getuNickname());
                            circleDiarySeeUInfo.setText(userEntity.getuRank() + "级小白");
                        });
                    }

                }else {
                    Log.e("BMOB", e.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogUIUtils.showToastCenter("网络出错！请重试");
                        }
                    });
                }
            }
        });
        int likeNum = 0;
        if (mDiaryEntity.getDiaryLikUserNids() != null){
            likeNum = mDiaryEntity.getDiaryLikUserNids().size();
        }
        circleDiarySeeInfo.setText(mDiaryEntity.getDiaryReadNum() + "次浏览 · "
                + likeNum + "个赞同");
        circleDiarySeeTitle.setText(mDiaryEntity.getDiaryTitle());
        circleDiarySeeContent.loadDataWithBaseURL(null, URLDecoder.decode(mDiaryEntity.getDiaryContent())
                , "text/html", "utf-8", null);
        circleDiarySeeField.setText(mDiaryEntity.getCreatedAt());
        circleDiarySeeLikeBtn.setLiked(mDiaryEntity.getIsLike() == 1);
       /* HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("did", String.valueOf(diaryId));
        new UniteApi(ApiUtil.DIARY_INFO, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi u = (UniteApi) api;
                Gson gson = new Gson();
                try {
                    diary = gson.fromJson(u.getJsonData().get(0).toString(), DiaryUserVO.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                UserEntity user = diary.getUser();
                Glide.with(circleDiarySeeUHead)
                        .load(user.getuAvatar())
                        .apply(bitmapTransform(new CircleCrop()))
                        .into(circleDiarySeeUHead);
                circleDiarySeeUName.setText(user.getuNickname());
                circleDiarySeeUInfo.setText(user.getuRank() + "级小白");
                circleDiarySeeInfo.setText(diary.getDiaryReadNum() + "次浏览 · "
                        + diary.getDiaryLikeNum() + "个赞同");
                circleDiarySeeTitle.setText(diary.getDiaryTitle());
                circleDiarySeeContent.loadDataWithBaseURL(null, URLDecoder.decode(diary.getDiaryContent())
                        , "text/html", "utf-8", null);
                circleDiarySeeField.setText(diary.getDiaryTime());
                circleDiarySeeLikeBtn.setLiked(diary.getIsLike() == 1);
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
            }
        });*/

    }

    private void like(int type) {

        List<String> nidString = new ArrayList<>();
        if (mDiaryEntity != null && mDiaryEntity.getDiaryLikUserNids() != null) {
            nidString.addAll(mDiaryEntity.getDiaryLikUserNids());
        }



        if (type == 0){
            for (int i = 0; i < nidString.size(); i++) {
                if (!TextUtils.isEmpty(nidString.get(i))) {
                    if (nidString.get(i).equals(MyApplication.getInstance().getUserEntity().getObjectId())) {
                        nidString.remove(i);
                        i--;
                    }
                }
            }
            nidString.add(MyApplication.getInstance().getUserEntity().getObjectId());
        }else {
            for (int i = 0; i < nidString.size(); i++) {
                if (!TextUtils.isEmpty(nidString.get(i))) {
                    if (nidString.get(i).equals(MyApplication.getInstance().getUserEntity().getObjectId())) {
                        nidString.remove(i);
                        i--;
                    }
                }
            }
        }

        if (mDiaryEntity != null) {
            mDiaryEntity.setDiaryLikUserNids(nidString);
            mDiaryEntity.update(mDiaryEntity.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    runOnUiThread(() -> {
                        if (e == null) {
                            //toast("更新成功:"+p2.getUpdatedAt());
                            int likeNum = 0;
                            if (mDiaryEntity.getDiaryLikUserNids() != null){
                                likeNum = mDiaryEntity.getDiaryLikUserNids().size();
                            }
                            circleDiarySeeInfo.setText(mDiaryEntity.getDiaryReadNum() + "次浏览 · "
                                    + likeNum + "个赞同");
                            itemLikeFragment.setQuestionId(nidString);
                            itemLikeFragment.initData();
                        } else {
                            //toast("更新失败：" + e.getMessage());
                        }
                    });
                }
            });
        }
      /*  HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("id", diaryId);
        hm.put("type", "1");
        new UniteApi(ApiUtil.LIKE_ADD, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                itemLikeFragment.initData();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
            }
        });*/
    }

    private void comment() {
        String content = circleDiarySeeReply.getText().toString();
        if (TextUtils.isEmpty(content)){
            return;
        }
        ComUserVO comUserVO = new ComUserVO();
        comUserVO.setComBUid(mDiaryEntity.getObjectId());
        comUserVO.setComContent(content);
        comUserVO.setComUid(MyApplication.getInstance().getUserEntity().getObjectId());
        comUserVO.setComTime(TimeUtil.getCurrentDateStr());
        comUserVO.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                runOnUiThread(() -> {
                    if (e == null){
                        circleDiarySeeReply.clearFocus();
                        circleDiarySeeReply.setText("");
                        itemReplyFragment.initData();
                    }else {

                    }
                });

            }
        });
       /* HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("did", diaryId);
        hm.put("content", circleDiarySeeReply.getText().toString());
        new UniteApi(ApiUtil.COM_ADD, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                circleDiarySeeReply.clearFocus();
                circleDiarySeeReply.setText("");
                itemReplyFragment.initData();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
            }
        });*/
    }
}
