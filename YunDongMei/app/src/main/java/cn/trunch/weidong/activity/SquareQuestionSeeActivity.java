package cn.trunch.weidong.activity;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
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
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.SimpleFormatter;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.trunch.weidong.MyApplication;
import cn.trunch.weidong.R;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.util.TimeUtil;
import cn.trunch.weidong.vo.ComUserVO;
import cn.trunch.weidong.vo.DiaryUserVO;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.fragment.ItemLikeFragment;
import cn.trunch.weidong.fragment.ItemReplyFragment;
import cn.trunch.weidong.util.InputUtil;
import cn.trunch.weidong.util.TextUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SquareQuestionSeeActivity extends AppCompatActivity {

    private String did;
    private String HeUserId;
    private DiaryUserVO question;
    // 头部
    private ImageView questionSeeBackBtn;
    private ImageView questionSeeUHead;
    private TextView questionSeeUName;
    private TextView questionSeeUInfo;
    private TextView questionSeeInfo;
    private TextView questionSeeTime;
    private TextView questionSeeTitle;
    private TextView questionSeeContent;
    private TextView questionSeeFollowBtn;
    private NineGridView questionSeeNine;
    private TextView questionSeeField;
    private TextView questionSeeBounty;
    // 下部导航
    private String[] titles = {"回答", "点赞"};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private SlidingTabLayout questionSeeNav;
    private ViewPager questionSeePager;
    private ItemReplyFragment itemReplyFragment;
    private ItemLikeFragment itemLikeFragment;
    // 底部操作
    private View questionSeeCover;
    private LinearLayout questionSeeReplyBox;
    private LikeButton questionSeeLikeBtn;
    private LikeButton questionSeeCollectBtn;
    private EditText questionSeeReply;
    private TextView questionSeeReplySendBtn;
    private UserEntity user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_question_see);
        StatusBarUtil.setWindowStatusBarColor(this, R.color.colorLight);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        did = getIntent().getStringExtra("did");
        question = (DiaryUserVO) getIntent().getSerializableExtra("data");
        if (question != null){
            if (!TextUtils.isEmpty(question.getUserId()) && !question.getUserId().equals(MyApplication.getInstance().getUserEntity().getObjectId())){
                question.setDiaryReadNum(question.getDiaryReadNum()+1);
                question.update(question.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            Log.e("TAG", "浏览量: "+question.getDiaryReadNum());
                        }
                    }
                });
            }
        }
        init();
        initNav();
        initListener();
        initHeadData();

    }

    private void init() {
        // 顶部
        questionSeeBackBtn = findViewById(R.id.questionSeeBackBtn);
        questionSeeUHead = findViewById(R.id.questionSeeUHead);
        questionSeeUName = findViewById(R.id.questionSeeUName);
        questionSeeUInfo = findViewById(R.id.questionSeeUInfo);
        questionSeeTime = findViewById(R.id.questionSeeTime);
        questionSeeInfo = findViewById(R.id.questionSeeInfo);
        questionSeeTitle = findViewById(R.id.questionSeeTitle);
        TextUtil.setBold(questionSeeTitle);
        questionSeeContent = findViewById(R.id.questionSeeContent);
        questionSeeFollowBtn = findViewById(R.id.questionSeeFollowBtn);
        questionSeeNine = findViewById(R.id.questionSeeNine);
        questionSeeField = findViewById(R.id.questionSeeField);
        questionSeeBounty = findViewById(R.id.questionSeeBounty);
        // 下部导航
        questionSeeNav = findViewById(R.id.questionSeeNav);
        questionSeePager = findViewById(R.id.questionSeePager);
        // 底部操作
        questionSeeCover = findViewById(R.id.questionSeeCover);
        questionSeeReplyBox = findViewById(R.id.questionSeeReplyBox);
        questionSeeLikeBtn = findViewById(R.id.questionSeeLikeBtn);
        questionSeeCollectBtn = findViewById(R.id.questionSeeCollectBtn);
        questionSeeReply = findViewById(R.id.questionSeeReply);
        questionSeeReplySendBtn = findViewById(R.id.questionSeeReplySendBtn);
    }

    private void initNav() {
        itemReplyFragment = ItemReplyFragment.newInstance(did,question.getObjectId());
        itemLikeFragment = ItemLikeFragment.newInstance(did,question.getDiaryLikUserNids());
        fragments.add(itemReplyFragment);
        fragments.add(itemLikeFragment);
        //绑定TabLayout和ViewPager
        questionSeeNav.setViewPager(questionSeePager, titles, this, fragments);
    }

    private void initListener() {
        questionSeeBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        questionSeeCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionSeeReply.clearFocus();
            }
        });
        questionSeeReplyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拦截事件
            }
        });
        questionSeeReply.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == R.id.questionSeeReply && hasFocus) {
                    questionSeeCover.setVisibility(View.VISIBLE);
                    questionSeeLikeBtn.setVisibility(View.GONE);
                    // questionSeeCollectBtn.setVisibility(View.GONE);
                    questionSeeReplySendBtn.setVisibility(View.VISIBLE);
                    questionSeeReply.setMinLines(3);
                    questionSeeReply.setMaxLines(10);
                    InputUtil.showInput(questionSeeReply);
                } else {
                    questionSeeCover.setVisibility(View.GONE);
                    questionSeeLikeBtn.setVisibility(View.VISIBLE);
                    //questionSeeCollectBtn.setVisibility(View.VISIBLE);
                    questionSeeReplySendBtn.setVisibility(View.GONE);
                    questionSeeReply.setMinLines(1);
                    questionSeeReply.setMaxLines(1);
                    InputUtil.hideInput(SquareQuestionSeeActivity.this);
                }
            }
        });
        questionSeeReply.addTextChangedListener(new TextWatcher() {
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
                    questionSeeReplySendBtn.setEnabled(false);
                    questionSeeReplySendBtn.setTextColor(getResources().getColor(R.color.colorDefaultText));
                } else {
                    questionSeeReplySendBtn.setEnabled(true);
                    questionSeeReplySendBtn.setTextColor(getResources().getColor(R.color.colorTheme));
                }
            }
        });
        questionSeeReplySendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment();
            }
        });
        questionSeeLikeBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                like(0);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                like(1);
            }
        });
        questionSeeCollectBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });
        questionSeeFollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow();
            }
        });
    }

    private void initHeadData() {
        //question = new DiaryUserVO();
        question.setRepositity(3);
        setData();

       /* HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("did", did);
        new UniteApi(ApiUtil.DIARY_INFO, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                Gson gson = new Gson();
                UniteApi u = (UniteApi) api;
                try {
                    question = gson.fromJson(u.getJsonData().get(0).toString(), DiaryUserVO.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setData();
                //end
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
            }
        });*/
    }

    private void setData() {
        //显示数据
        user = question.getUser();
        HeUserId = user.getObjectId();
        questionSeeFollowBtn.setText(question.getIsFollow() == 1 ? "已关注" : "关注");

        Glide.with(questionSeeUHead)
                .load(user.getuAvatar())
                .apply(bitmapTransform(new CircleCrop()))
                .into(questionSeeUHead);
        questionSeeUName.setText(user.getuNickname());
        questionSeeUInfo.setText(user.getuRank() + "级达人");
        int likeNum = 0;
        if (question.getDiaryLikUserNids() != null){
            likeNum = question.getDiaryLikUserNids().size();
        }
        questionSeeInfo.setText(question.getDiaryCommentNum() + "个评论 " +
                (question.getDiaryReadNum()) + "次浏览 "
                + likeNum + "个人喜欢");
        questionSeeTime.setText(question.getDiaryTime());
        questionSeeTitle.setText(question.getDiaryTitle());
        questionSeeContent.setText(question.getDiaryContent());
        if (question.getDiaryAnonymous() == 0){
            questionSeeBounty.setVisibility(View.INVISIBLE);
        }else {
            questionSeeBounty.setVisibility(View.VISIBLE);
        }
        questionSeeLikeBtn.setLiked(question.getIsLike() == 1);
        questionSeeField.setText(question.getDiaryLable());
        //显示结束
        //图片开始
        List<ImageInfo> imageInfos = new ArrayList<>();
        try {
            if (question.getImg() != null)
                for (String imageURL : question.getImg()) {
                    ImageInfo imageInfo = new ImageInfo();
                    imageInfo.setThumbnailUrl(imageURL);
                    imageInfo.setBigImageUrl(imageURL);
                    imageInfos.add(imageInfo);
                }
        } catch (Exception e) {
            e.printStackTrace();
            DialogUIUtils.showToastCenter("未知错误 ＞.＜");
        }
        questionSeeNine.setAdapter(new NineGridViewClickAdapter(SquareQuestionSeeActivity.this, imageInfos));
    }

    private void like(int type) {
        //更新Person表里面id为6b6c11c537的数据，address内容更新为“北京朝阳”
        DiaryUserVO p2 = new DiaryUserVO();
        List<String> nidString = new ArrayList<>();
        if (question != null && question.getDiaryLikUserNids() != null) {
            nidString.addAll(question.getDiaryLikUserNids());
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

        if (question != null) {
            question.setDiaryLikUserNids(nidString);
            question.update(question.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    runOnUiThread(() -> {
                        if (e == null) {
                            //toast("更新成功:"+p2.getUpdatedAt());
                            int likeNum = 0;
                            if (question.getDiaryLikUserNids() != null){
                                likeNum = question.getDiaryLikUserNids().size();
                            }
                            questionSeeInfo.setText(question.getDiaryCommentNum() + "个评论 " +
                                    (question.getDiaryReadNum()) + "次浏览 "
                                    + likeNum + "个人喜欢");
                            itemLikeFragment.setQuestionId(nidString);
                            itemLikeFragment.initData();
                        } else {
                            //toast("更新失败：" + e.getMessage());
                        }
                    });
                }
            });
        }


       /* HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("id", did);
        hm.put("type", "1");
        new UniteApi(ApiUtil.LIKE_ADD, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {

            }

            @Override
            public void failure(Api api) {
            }
        });*/
    }

    private void comment() {
        String content = questionSeeReply.getText().toString();
        if (TextUtils.isEmpty(content)){
            return;
        }
        ComUserVO comUserVO = new ComUserVO();
        comUserVO.setComBUid(question.getObjectId());
        comUserVO.setComContent(questionSeeReply.getText().toString());
        comUserVO.setComUid(MyApplication.getInstance().getUserEntity().getObjectId());
        comUserVO.setComTime(TimeUtil.getCurrentDateStr());
        comUserVO.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                runOnUiThread(() -> {
                    if (e == null){
                        questionSeeReply.clearFocus();
                        questionSeeReply.setText("");
                        itemReplyFragment.initData();
                    }else {

                    }
                });

            }
        });
       /* HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("did", did);
        hm.put("content", questionSeeReply.getText().toString());
        new UniteApi(ApiUtil.COM_ADD, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {

            }

            @Override
            public void failure(Api api) {
            }
        });*/
    }

    private void follow() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("HeUserId", HeUserId);
        hm.put("did", did);
        hm.put("type", (question.getIsFollow() == 1 ? 0 : 1) + "");
        new UniteApi(ApiUtil.VIDEO_ADD_FOLLOW_INFO, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                questionSeeFollowBtn.setText(question.getIsFollow() == 1 ? "关注" : "已关注");
            }

            @Override
            public void failure(Api api) {

            }
        });
    }

}
