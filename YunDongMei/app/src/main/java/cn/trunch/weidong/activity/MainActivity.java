package cn.trunch.weidong.activity;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.next.easynavigation.view.EasyNavigationBar;


import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.trunch.weidong.R;
import cn.trunch.weidong.fragment.CircleFragment;
import cn.trunch.weidong.fragment.ExploreFragment;
import cn.trunch.weidong.fragment.MineFragment;
import cn.trunch.weidong.fragment.SportFragment;
import cn.trunch.weidong.fragment.SquareFragment;
import cn.trunch.weidong.util.StatusBarUtil;

public class MainActivity extends AppCompatActivity {

    private String[] tabText = {"运动", "发现", "社区", "我的"};
    private int[] normalIcon = {R.mipmap.sport_normal, R.mipmap.square_normal, R.mipmap.explore_normal, R.mipmap.circle_normal};
    private int[] selectIcon = {R.mipmap.sport_select, R.mipmap.square_select, R.mipmap.explore_select, R.mipmap.circle_select};
    private List<Fragment> fragments = new ArrayList<>();
    private EasyNavigationBar navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setWindowStatusBarColor(this, R.color.colorTheme);
        //底部导航
        initNav();
    }

    private void initNav() {
        fragments.add(new SportFragment());
        fragments.add(new ExploreFragment());
        fragments.add(new SquareFragment());
        fragments.add(new MineFragment());
        navigationBar = findViewById(R.id.navigationBar);
        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .iconSize(22)     //Tab图标大小
                .tabTextSize(12)   //Tab文字大小
                .normalTextColor(getResources().getColor(R.color.colorDefaultText))   //Tab未选中时字体颜色
                .selectTextColor(getResources().getColor(R.color.colorTheme))   //Tab选中时字体颜色
                .navigationBackground(getResources().getColor(R.color.bg_white))   //导航栏背景色
                .lineColor(getResources().getColor(R.color.colorTopLine)) //分割线颜色
                .smoothScroll(false)  //点击Tab  Viewpager切换是否有动画
                .canScroll(false)    //Viewpager能否左右滑动
//                .anim(Anim.ZoomIn)  //点击Tab时的动画
                .hintPointLeft(-3)  //调节提示红点的位置hintPointLeft hintPointTop（看文档说明）
                .hintPointTop(-7)
                .hintPointSize(6)    //提示红点的大小
                .msgPointLeft(-10)  //调节数字消息的位置msgPointLeft msgPointTop（看文档说明）
                .msgPointTop(-15)
                .msgPointTextSize(9)  //数字消息中字体大小
                .msgPointSize(18)    //数字消息红色背景的大小
                .onTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabClickEvent(View view, int i) {
                        if (i!=2){
                            Jzvd.releaseAllVideos();
                        }
                        if (i != 3) {
                            StatusBarUtil.setWindowStatusBarColor(MainActivity.this, R.color.colorTheme);
                        } else {
                            StatusBarUtil.setWindowStatusBarColor(MainActivity.this, R.color.colorDarkText);
                        }
                        return false;
                    }
                })
                .build();
        navigationBar.getmViewPager().setOffscreenPageLimit(4);
    }

    public void onBackPressed() {
        // 在全屏或者小窗口时按返回键要先退出全屏或小窗口，
        // 所以在Activity中onBackPress要交给NiceVideoPlayer先处理。
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
