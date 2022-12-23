package cn.trunch.weidong.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.dou361.dialogui.DialogUIUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.trunch.weidong.MyApplication;
import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.SquareQuestionSearchActivity;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.step.config.Constant;
import cn.trunch.weidong.vo.DiaryUserVO;
import cn.trunch.weidong.activity.MainActivity;
import cn.trunch.weidong.activity.SquareQuestionAddActivity;
import cn.trunch.weidong.adapter.SquareQuestionAdapter;
import cn.trunch.weidong.entity.PageEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SquareQuestionFragment extends Fragment {
    private View view;
    private Context context;
    private MainActivity activity;

    private LinearLayout questionSearchBtn;
    private FloatingActionButton questionAddBtn;
    private XRecyclerView questionList;
    private ImageView questionHeaderImg;
    private SquareQuestionAdapter questionAdapter;

    private List<DiaryUserVO> questions = new ArrayList<>();
    private PageEntity page = new PageEntity();
    private int i;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_square_question, container, false);
        context = getActivity();
        activity = (MainActivity) getActivity();

        init();
        initListener();



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void init() {
        questionSearchBtn = view.findViewById(R.id.questionSearchBtn);
        questionList = view.findViewById(R.id.questionList);
        questionAddBtn = view.findViewById(R.id.questionAddBtn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        questionList.setLayoutManager(layoutManager);
        questionAdapter = new SquareQuestionAdapter(context);
        questionList.setAdapter(questionAdapter);
        View imgHeaderView = LayoutInflater.from(context).inflate(R.layout.view_square_question_header
                , view.findViewById(android.R.id.content), false);
        questionHeaderImg = imgHeaderView.findViewById(R.id.questionHeaderImg);
        questionList.addHeaderView(imgHeaderView);
    }

    private void initListener() {
        questionSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SquareQuestionSearchActivity.class);
                startActivity(intent);
            }
        });
        questionList.setLoadingMoreEnabled(false);
        questionList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {
                //loadData();
            }
        });
        questionAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SquareQuestionAddActivity.class);
                activity.overridePendingTransition(R.anim.bottom_in, R.anim.anim_static);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        i = 0;
        Glide.with(context)
                .load("https://img01.jituwang.com/180125/256724-1P12522510684.jpg")
                .apply(bitmapTransform(new MultiTransformation<>(
                        new CenterCrop()
                )))
                .into(questionHeaderImg);
        page.setCurrentPage(0);
        BmobQuery<DiaryUserVO> query = new BmobQuery<>();
        query.findObjects(new FindListener<DiaryUserVO>() {
            @Override
            public void done(List<DiaryUserVO> object, BmobException e) {
                if (e == null) {
                    Log.e("BMOB", "查询成功: " + object.size());
                    for (int j = 0; j < object.size(); j++) {
                        DiaryUserVO d = object.get(j);
                        if (d.getDiaryAnonymous() == 1 && !MyApplication.getInstance().getUserEntity().getObjectId().equals(d.getUserId())){
                            object.remove(j);
                            j--;
                        }
                    }
                    for (DiaryUserVO d : object) {
                        Log.e("", "diaryReadNum: "+d.getDiaryReadNum());

                        if (d.getImg() != null && d.getImg().size() > 0) {
                            for (int j = 0; j < d.getImg().size(); j++) {
                                if (!TextUtils.isEmpty(d.getImg().get(j))){
                                    d.setDiaryImgPreview(d.getImg().get(j));
                                    break;
                                }
                            }
                        }
                        if (!TextUtils.isEmpty(d.getUserId())) {
                            BmobQuery<UserEntity> categoryBmobQuery = new BmobQuery<>();
                            categoryBmobQuery.getObject(d.getUserId(), new QueryListener<UserEntity>() {
                                @Override
                                public void done(UserEntity userEntity, BmobException e) {
                                    if (e == null) {
                                        d.setUser(userEntity);
                                        i++;
                                        if (i == object.size()) {
                                            Collections.reverse(object);
                                            getActivity().runOnUiThread(() -> {
                                                questionAdapter.initData(object);
                                                questionList.refreshComplete();
                                            });
                                        }
                                        //toast("查询成功");
                                    } else {
                                        //toast("查询失败：" + e.getMessage());
                                    }
                                }
                            });
                        }
                    }


                    //Snackbar.make(mBtnEqual, "查询成功：" + object.size(), Snackbar.LENGTH_LONG).show();
                } else {
                    Log.e("BMOB", e.toString());
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogUIUtils.showToastCenter("网络出错！请重试");
                            questionList.refreshComplete();
                        }
                    });
                    //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });


       /* HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", ApiUtil.USER_TOKEN);
        hashMap.put("type", String.valueOf(ApiUtil.QUESTION_TYPE));
        hashMap.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hashMap.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.DIARY_LIST, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi uniteApi = (UniteApi) api;
                Gson gson = new Gson();
                questions = gson.fromJson(uniteApi.getJsonData().toString(), new TypeToken<List<DiaryUserVO>>() {
                }.getType());
                page = gson.fromJson(uniteApi.getJsonPage().toString(), PageEntity.class);
                questionList.post(new Runnable() {
                    @Override
                    public void run() {
                        questionAdapter.initData(questions);
                        questionList.refreshComplete();
                    }
                });
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
                questionList.refreshComplete();
            }
        });*/
    }

    private void loadData() {
        if (!page.getNextPage()) {
            questionList.setNoMore(true);
            questionList.setFootViewText("正在加载更多", "已经到底了");
            DialogUIUtils.showToastCenter("已经到底了");
            questionList.loadMoreComplete();
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", ApiUtil.USER_TOKEN);
        hashMap.put("type", String.valueOf(ApiUtil.QUESTION_TYPE));
        hashMap.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hashMap.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.DIARY_LIST, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi uniteApi = (UniteApi) api;
                Gson gson = new Gson();
                questions = gson.fromJson(uniteApi.getJsonData().toString(), new TypeToken<List<DiaryUserVO>>() {
                }.getType());
                page = gson.fromJson(uniteApi.getJsonPage().toString(), PageEntity.class);
                questionList.post(new Runnable() {
                    @Override
                    public void run() {
                        questionAdapter.loadData(questions);
                        questionList.loadMoreComplete();
                    }
                });
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
                questionList.loadMoreComplete();
            }
        });
    }
}
