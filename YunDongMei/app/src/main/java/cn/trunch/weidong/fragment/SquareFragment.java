package cn.trunch.weidong.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import cn.trunch.weidong.MyApplication;
import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.MainActivity;
import cn.trunch.weidong.activity.SettingActivity;
import cn.trunch.weidong.http.ApiUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SquareFragment extends Fragment {
    private View view;
    private Context context;
    private MainActivity activity;

    private RelativeLayout squareSettingGo;
    private ImageView squareAvatar;
    private SlidingTabLayout squareTabLayout;
    private ViewPager squareViewPager;
    private String[] titles = {"讨论"/*, "咨询", "小组"*/}; //去除3：活动
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_square, container, false);
        context = getActivity();
        activity = (MainActivity) getActivity();

        init();
        initListener();
        //绑定TabLayout和ViewPager
        squareTabLayout.setViewPager(squareViewPager, titles, activity, fragments);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(this)
                .load(MyApplication.getInstance().getUserEntity().getuAvatar())
                .apply(bitmapTransform(new CircleCrop()))
                .into(squareAvatar);
    }

    private void init() {
        squareSettingGo = view.findViewById(R.id.squareSettingGo);
        squareAvatar = view.findViewById(R.id.squareAvatar);
        squareTabLayout = view.findViewById(R.id.squareTabLayout);
        squareViewPager = view.findViewById(R.id.squareViewPager);
        fragments.add(new SquareQuestionFragment());
       // fragments.add(new SquareConsultFragment());
//        fragments.add(new SquareMatchFragment());
     //   fragments.add(new SquareGroupFragment());
    }

    private void initListener() {
        squareSettingGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SettingActivity.class);
                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });
    }
}
