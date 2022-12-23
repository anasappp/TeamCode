package cn.trunch.weidong.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import cn.trunch.weidong.MyApplication;
import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.CollectionActivity;
import cn.trunch.weidong.activity.EmptyActivity;
import cn.trunch.weidong.activity.FollowActivity;
import cn.trunch.weidong.activity.SettingAccountActivity;
import cn.trunch.weidong.activity.SettingActivity;
import cn.trunch.weidong.activity.SettingIdentifyActivity;
import cn.trunch.weidong.activity.SettingSetupActivity;
import cn.trunch.weidong.activity.SportDiaryActivity;
import cn.trunch.weidong.activity.SportHistoryActivity;
import cn.trunch.weidong.activity.VideoActivity;
import cn.trunch.weidong.activity.WebActivity;
import cn.trunch.weidong.http.ApiUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class MineFragment extends Fragment {
    private ImageView settingBackBtn;
    private ImageView settingSetupBtn1;
    private ImageView settingAvatar;
    private TextView settingNickname;
    private TextView settingInfoTextView;
//    private RelativeLayout settingHomeGoBtn;

    private LinearLayout settingReleaseBtn;
    private LinearLayout settingRecordBtn;
    private LinearLayout settingFollowBtn;
    private LinearLayout settingCollectBtn;
    private RelativeLayout settingIdentifyBtn;
    private RelativeLayout settingAccountBtn;
    private RelativeLayout settingWalletBtn;
    private RelativeLayout settingFansBtn;
    private RelativeLayout settingSetupBtn2;
    
    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
     
        fragment.setArguments(args);
        return fragment;
    }

   

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        init(view);
        initListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(this)
                .load(MyApplication.getInstance().getUserEntity().getuAvatar())
                .apply(bitmapTransform(new CircleCrop()))
                .into(settingAvatar);
        settingNickname.setText(MyApplication.getInstance().getUserEntity().getuNickname());
        settingInfoTextView.setText(MyApplication.getInstance().getUserEntity().getuSelfdes());
    }

    private void init(View view) {
        settingBackBtn = view.findViewById(R.id.settingBackBtn);
        settingSetupBtn1 = view.findViewById(R.id.settingSetupBtn1);
        settingAvatar = view.findViewById(R.id.settingAvatar);
        settingNickname = view.findViewById(R.id.settingNickname);
//        settingHomeGoBtn = view.findViewById(R.id.settingHomeGoBtn);
        settingReleaseBtn = view.findViewById(R.id.settingReleaseBtn);
        settingRecordBtn = view.findViewById(R.id.settingRecordBtn);
        settingFollowBtn = view.findViewById(R.id.settingFollowBtn);
        settingCollectBtn = view.findViewById(R.id.settingCollectBtn);
        settingIdentifyBtn = view.findViewById(R.id.settingIdentifyBtn);
        settingAccountBtn = view.findViewById(R.id.settingAccountBtn);
        settingWalletBtn = view.findViewById(R.id.settingWalletBtn);
        settingFansBtn = view.findViewById(R.id.settingFansBtn);
        settingSetupBtn2 = view.findViewById(R.id.settingSetupBtn2);
        settingInfoTextView = view.findViewById(R.id.settingInfoTextView);
    }

    private void initListener() {
        settingBackBtn.setOnClickListener(v -> {


        });
        settingSetupBtn1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingSetupActivity.class);
            startActivity(intent);
        });
        settingAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingAccountActivity.class);
            startActivity(intent);
        });
//        settingHomeGoBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), SettingAccountActivity.class);
//                startActivity(intent);
//            }
//        });
        settingReleaseBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SportDiaryActivity.class);
            startActivity(intent);
        });
        settingRecordBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SportHistoryActivity.class);
            startActivity(intent);
        });
        settingFollowBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FollowActivity.class);
            startActivity(intent);
        });
        settingCollectBtn.setOnClickListener(v -> {

            //Intent intent = new Intent(getActivity(), VideoActivity.class);
            Intent intent = new Intent(getActivity(), CollectionActivity.class);
            startActivity(intent);
        });
        settingIdentifyBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingIdentifyActivity.class);
            startActivity(intent);
        });
        settingAccountBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingAccountActivity.class);
            startActivity(intent);
        });
        settingWalletBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("title", "我的订单");
            intent.putExtra("href", "http://47.102.200.22/#/my_order_list?token=" + ApiUtil.USER_TOKEN);
            startActivity(intent);
//                Intent intent = new Intent(getActivity(), EmptyActivity.class);
//                startActivity(intent);
        });
        settingFansBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EmptyActivity.class);
            startActivity(intent);
        });
        settingSetupBtn2.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingSetupActivity.class);
            startActivity(intent);
        });
    }
}
