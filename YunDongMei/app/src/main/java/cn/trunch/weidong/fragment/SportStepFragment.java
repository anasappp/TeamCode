package cn.trunch.weidong.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.trunch.weidong.MyApplication;
import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.MainActivity;
import cn.trunch.weidong.activity.SportPunchActivity;
import cn.trunch.weidong.activity.WebActivity;
import cn.trunch.weidong.entity.Rank;
import cn.trunch.weidong.step.service.StepService;
import cn.trunch.weidong.step.service.UpdateUiCallBack;
import cn.trunch.weidong.step.utils.SharedPreferencesUtils;
import cn.trunch.weidong.view.SportStepView;

public class SportStepFragment extends Fragment {
    private View view;
    private Context context;
    private MainActivity activity;

    private TextView sportPunchBtn;
    private LinearLayout sportStepPanel;
    private SportStepView sportStepCount;
    private TextView sportStepCountInfo;
    private TextView sportStepRank1;
    private TextView sportStepRank2;
    private TextView sportStepRank3;
    private RelativeLayout sportStepRead;

    private SharedPreferencesUtils sp;

    private String userId ;
    private int stepNum = 0;
    private Boolean autoStepFlag = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sport_step, container, false);
        context = getActivity();
        activity = (MainActivity) getActivity();
        userId = MyApplication.getInstance().getUserEntity().getObjectId();
        init();
        initListener();
        initStepData();
        initRankData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        BmobQuery<Rank> categoryBmobQuery = new BmobQuery<>();
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(System.currentTimeMillis());
        //categoryBmobQuery.addWhereEqualTo("time", new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(System.currentTimeMillis()));
        categoryBmobQuery.findObjects(new FindListener<Rank>() {
            @Override
            public void done(List<Rank> object, BmobException e) {
                if (e == null) {
                    if (object != null){

                        List<Rank> todayList = new ArrayList<>();
                        int i = 0,flag = 0,rank = 0;
                        for (Rank r:object){
                            if (todayDate.equals(r.getTime())){
                                todayList.add(r);
                            }
                            if (!TextUtils.isEmpty(userId) && userId.equals(r.getUserId())){
                                rank = rank + r.getRank();
                            }
                        }
                        Collections.sort(todayList, (o1, o2) -> o2.getRank() - o1.getRank());
                        for (Rank r:todayList){
                            if (!TextUtils.isEmpty(userId) && userId.equals(r.getUserId())){
                                Log.e("TAg", "步数: "+r.getRank());
                                flag = i;
                            }
                            i++;
                        }
                        int finalFlag = flag;
                        int finalRank = rank;
                        getActivity().runOnUiThread(() -> {
                            sportStepRank1.setText((finalFlag +1)+"");
                            if (finalRank >= 20000){
                                sportStepRank3.setText("2");
                            }
                            if (finalRank >= 30000){
                                sportStepRank3.setText("3");
                            }
                            if (finalRank >= 40000){
                                sportStepRank3.setText("4");
                            }
                            if (finalRank >= 50000){
                                sportStepRank3.setText("5");
                            }
                            if (finalRank >= 60000){
                                sportStepRank3.setText("6");
                            }
                            if (finalRank >= 70000){
                                sportStepRank3.setText("7");
                            }
                            if (finalRank >= 80000){
                                sportStepRank3.setText("8");
                            }
                            if (finalRank >= 90000){
                                sportStepRank3.setText("9");
                            }
                            if (finalRank >= 100000){
                                sportStepRank3.setText("10");
                            }
                        });
                    }
                    //Snackbar.make(mBtnEqual, "查询成功：" + object.size(), Snackbar.LENGTH_LONG).show();
                } else {

                    //Log.e("BMOB", e.toString());
                    //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    //排名查询
    private void initRankData() {


       /* HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", ApiUtil.USER_TOKEN);
        new UniteApi(ApiUtil.RANK_MY, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi uniteApi = (UniteApi) api;
                Gson gson = new Gson();
                List<Rank> o = gson.fromJson(uniteApi.getJsonData().toString(), new TypeToken<List<Rank>>() {
                }.getType());
                Rank m=o.get(0);
                sportStepRank1.setText(String.valueOf(m.getWorldRank()));
                sportStepRank2.setText(String.valueOf(m.getSchoolRank()));
                sportStepRank3.setText(String.valueOf(m.getMyRank()));
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
            }
        });*/
    }

    private void init() {
        sportPunchBtn  = view.findViewById(R.id.sportPunchBtn);
        sportStepPanel = view.findViewById(R.id.sportStepPanel);
        sportStepCount = view.findViewById(R.id.sportStepCount);
        sportStepCountInfo = view.findViewById(R.id.sportStepCountInfo);
        sportStepRank1 = view.findViewById(R.id.sportStepRank1);
        sportStepRank2 = view.findViewById(R.id.sportStepRank2);
        sportStepRank3 = view.findViewById(R.id.sportStepRank3);
        sportStepRead = view.findViewById(R.id.sportStepRead);
    }

    private void initListener() {
        sportPunchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, SportPunchActivity.class);
                activity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
                startActivity(intent);
            }
        });
        sportStepRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, WebActivity.class);
                intent.putExtra("title", "每日阅读");
                intent.putExtra("href", "http://mp.weixin.qq.com/s?__biz=MzAwNTkyMzIzNg==&mid=2247491494&idx=1&sn=046a6b400492d2eb1369be9766595fe3&chksm=9b14176dac639e7b78195ad98b88ba67a256d12ccb818252993275620503d10633ba9714dc4e&mpshare=1&scene=23&srcid=0501a7qh7rXqrzpkbyzWwFxf#rd");
                startActivity(intent);
            }
        });
    }


    // ------------------------------------------------------计步

    private void initStepData() {
        sp = new SharedPreferencesUtils(context);
        //获取用户设置的计划锻炼步数，没有设置过的话默认7000
        String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
        //设置当前步数为0
        sportStepCount.setCurrentCount(Integer.parseInt(planWalk_QTY), 0);
        sportStepCountInfo.setText("计步中···");
        setupService();
    }

    private boolean isBind = false;

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(context, StepService.class);
        isBind = context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        context.startService(intent);
    }

    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
            sportStepCount.setCurrentCount(Integer.parseInt(planWalk_QTY), stepService.getStepCount());

            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
                    sportStepCount.setCurrentCount(Integer.parseInt(planWalk_QTY), stepCount);
                }
            });
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            context.unbindService(conn);
        }
    }
}
