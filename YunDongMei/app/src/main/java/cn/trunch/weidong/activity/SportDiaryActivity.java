package cn.trunch.weidong.activity;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.trunch.weidong.MyApplication;
import cn.trunch.weidong.R;
import cn.trunch.weidong.entity.DiaryEntity;
import cn.trunch.weidong.fragment.SportDiaryFragment;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.view.NoScrollViewPager;

public class SportDiaryActivity extends AppCompatActivity {

    private ImageView diaryBackBtn;
    private SlidingTabLayout diaryTabLayout;
    private NoScrollViewPager diaryViewPager;
    private FloatingActionButton diaryAddBtn;
    private String[] titles = {"公开日记", "私密日记"}; //去除3：活动
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private SportDiaryFragment oneFragment,twoFragment;
    private boolean isAnonymous;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_diary);
        StatusBarUtil.setWindowStatusBarColor(this, R.color.colorTheme);

        init();
        initListener();
        //绑定TabLayout和ViewPager
        diaryTabLayout.setViewPager(diaryViewPager, titles, this, fragments);
        diaryViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (0 == position){
                    isAnonymous = true;
                }else {
                    isAnonymous = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        getData();
    }

    private void getData() {
        BmobQuery<DiaryEntity> categoryBlobQuery = new BmobQuery<>();
        categoryBlobQuery.addWhereEqualTo("diaryUid", MyApplication.getInstance().getUserEntity().getObjectId());
        categoryBlobQuery.findObjects(new FindListener<DiaryEntity>() {
            @Override
            public void done(List<DiaryEntity> object, BmobException e) {
                if (e == null) {
                    //Snackbar.make(mBtnEqual, "查询成功：" + object.size(), Snackbar.LENGTH_LONG).show();
                    List<DiaryEntity> one = new ArrayList<>();
                    List<DiaryEntity> two = new ArrayList<>();
                    if (object != null){
                        for (DiaryEntity d:object){
                            if (d.getDiaryAnonymous() == 0){
                                one.add(d);
                            }else {
                                two.add(d);
                            }
                        }
                        runOnUiThread(() -> {
                            if (oneFragment != null){
                                oneFragment.setData(object);
                            }
                            if (twoFragment != null){
                                twoFragment.setData(two);
                            }
                        });
                    }
                } else {
                    Log.e("BMOB", e.toString());
                    //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void init() {
        diaryAddBtn = findViewById(R.id.diaryAddBtn);
        diaryBackBtn = findViewById(R.id.diaryBackBtn);
        diaryTabLayout = findViewById(R.id.diaryTabLayout);
        diaryViewPager = findViewById(R.id.diaryViewPager);
        oneFragment = SportDiaryFragment.newInstance(0);
        twoFragment = SportDiaryFragment.newInstance(1);
        fragments.add(oneFragment);
        fragments.add(twoFragment);
    }

    private void initListener() {
        diaryBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        diaryAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SportDiaryActivity.this, SportDiaryAddActivity.class);
                overridePendingTransition(R.anim.bottom_in, R.anim.anim_static);
                intent.putExtra("isAnonymous",isAnonymous);
                startActivityForResult(intent,100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            getData();
        }
    }
}
