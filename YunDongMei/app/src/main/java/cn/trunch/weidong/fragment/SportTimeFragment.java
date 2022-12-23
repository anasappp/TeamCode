package cn.trunch.weidong.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baidu.location.LocationClientOption;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.fence.FenceAlarmPushInfo;
import com.baidu.trace.api.track.LatestPoint;
import com.baidu.trace.api.track.LatestPointRequest;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.LatLng;
import com.baidu.trace.model.OnCustomAttributeListener;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.TransportMode;
import com.baidubce.util.DateUtils;
import com.dou361.dialogui.DialogUIUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.trunch.weidong.MyApplication;
import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.MainActivity;
import cn.trunch.weidong.activity.SettingAccountActivity;
import cn.trunch.weidong.activity.SportMapActivity;
import cn.trunch.weidong.entity.ExerciseEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.service.SportTimeService;
import cn.trunch.weidong.util.GPSUtils;
import cn.trunch.weidong.util.SPUtil;
import cn.trunch.weidong.util.TimeUtil;
import cn.trunch.weidong.view.SportTimeView;

public class SportTimeFragment extends Fragment {
    private String TAG = "SportTimeFragment";
    private View view;
    private Context context;
    private MainActivity activity;

    private TextView sportTimeH;
    private TextView sportTimeM;
    private SportTimeView sportTimeCount;
    private ImageView sportTimeMap;
    private TextView sportTimeStartBtn;
    private TextView sportTimeStartInfo;

    private boolean timeCountFlag = false;
    private long timeH = 0;
    private long timeM = 0;
    private long timeS = 0;
    private IntentFilter intentFilter;
    private SportTimeReceiver sportTimeReceiver;
    private long oTimsS = 0;

    //垃圾
    LBSTraceClient mTraceClient;
    long serviceId = 227523;
    String uid = ApiUtil.USER_ID;
    boolean isNeedObjectStorage = false;
    boolean isStart = false;

    Trace mTrace;
    final OnTraceListener traceListener = new OnTraceListener() {
        /**
         * 绑定服务回调接口
         * @param i  状态码
         * @param s 消息
         *                <p>
         *                <pre>0：成功 </pre>
         *                <pre>1：失败</pre>
         */
        @Override
        public void onBindServiceCallback(int i, String s) {
            Log.e("LBS", "onBindServiceCallback: "+i+"  "+s);
        }

        @Override
        public void onStartTraceCallback(int i, String s) {
            Log.e("LBS", "Service start"+" i:"+i+"   s:"+s);
            if (i == 0){
                mTraceClient.startGather(traceListener);
            }
        }

        @Override
        public void onStopTraceCallback(int i, String s) {
            Log.e("LBS", "Service stop"+" i:"+i+"   s:"+s);
        }

        @Override
        public void onStartGatherCallback(int i, String s) {
            Log.e("LBS", "gather start"+" i:"+i+"   s:"+s);
        }

        @Override
        public void onStopGatherCallback(int i, String s) {
            Log.e("LBS", "gather stop"+" i:"+i+"   s:"+s);
        }

        @Override
        public void onPushCallback(byte b, PushMessage pushMessage) {
            Log.e("LBS", "-----onPushCallback, messageType:%d, messageContent:%s "+ b+"  "+
                    pushMessage);
            if (b < 0x03 || b > 0x04) {

                return;
            }
            FenceAlarmPushInfo alarmPushInfo = pushMessage.getFenceAlarmPushInfo();
            if (null == alarmPushInfo) {
                Log.e("LBS", "onPushCallback, messageType:%d, messageContent:%s "+ b+"  "+
                        pushMessage);

                return;
            }
            Log.e("LBS", "onPushCallback, messageType:"+b+", messageContent:"+ pushMessage);

        }

        @Override
        public void onInitBOSCallback(int i, String s) {
            Log.e("LBS", "onInitBOSCallback: "+i+"  "+s);
        }




    };
    private String startTime,endTime;
    private long time;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sport_time, container, false);
        context = getActivity();
        activity = (MainActivity) getActivity();

        //垃圾
        mTraceClient = new LBSTraceClient(context);
        mTraceClient.setInterval(5, 10);
        mTrace = new Trace(serviceId, uid, isNeedObjectStorage);
        LatestPointRequest request = new LatestPointRequest(1, serviceId, uid);


        ProcessOption processOption = new ProcessOption();//纠偏选项
        processOption.setRadiusThreshold(50);//设置精度过滤，0为不需要；精度大于50米的位置点过滤掉
        processOption.setTransportMode(TransportMode.walking);
        processOption.setNeedDenoise(true);//去噪处理
        processOption.setNeedMapMatch(true);//绑路处理
        request.setProcessOption(processOption);//设置参数
        mTraceClient.queryLatestPoint(request, trackListener);//请求纠偏后的最新点


        isStart = false;

        init();
        initListener();

        // 广播注册
        intentFilter = new IntentFilter();
        intentFilter.addAction(SportTimeService.STEP_TIME_ACTION);
        sportTimeReceiver = new SportTimeReceiver();
        context.registerReceiver(sportTimeReceiver, intentFilter);

        DialogUIUtils.init(context);

        return view;
    }

    //轨迹监听器(用于接收纠偏后实时位置回调)
    private OnTrackListener trackListener = new OnTrackListener() {
        @Override
        public void onLatestPointCallback(LatestPointResponse response) {
            //位置点的返回间隔时间为数据打包上传的频率
            Log.e(TAG, "onLatestPointCallback: ");
            //将纠偏后实时位置显示在地图MapView上
            //这里位置点的返回间隔时间为数据打包上传的频率；数据发送到服务端，才会更新最新的纠偏位置
        }
    };

    class SportTimeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SportTimeService.STEP_TIME_ACTION.equals(intent.getAction())) {
                timeS = intent.getLongExtra("timeS", 0);
                time = intent.getLongExtra("time", 0);
               /* sportTimeCount.setCurrentCount(60, (int) (timeS % 60));
                timeH = timeS / 3600;
                timeM = (timeS % 3600) / 60;
                sportTimeH.setText(timeH == 0 ? "/" : String.valueOf(timeH));
                sportTimeM.setText(timeM == 0 ? "/" : String.valueOf(timeM));*/
                sportTimeCount.setCurrentCount(60, (int) (time % 60));
                timeH = time / 3600;
                timeM = (time % 3600) / 60;
                sportTimeH.setText(timeH == 0 ? "/" : String.valueOf(timeH));
                sportTimeM.setText(timeM == 0 ? "/" : String.valueOf(timeM));
            }
        }
    }

    private void init() {
        sportTimeH = view.findViewById(R.id.sportTimeH);
        sportTimeM = view.findViewById(R.id.sportTimeM);
        sportTimeCount = view.findViewById(R.id.sportTimeCount);
        SPUtil.init(context);
        timeS = SPUtil.getLong(TimeUtil.getCurrentDate() + "_SPORT_TIME", 0);
       /* sportTimeCount.setCurrentCount(60, (int) (timeS % 60));
        timeH = timeS / 3600;
        timeM = (timeS % 3600) / 60;
        sportTimeH.setText(timeH == 0 ? "/" : String.valueOf(timeS / 3600));
        sportTimeM.setText(timeM == 0 ? "/" : String.valueOf((timeS % 3600) / 60));*/
        sportTimeCount.setCurrentCount(60, (int) (time % 60));
        timeH = time / 3600;
        timeM = (time % 3600) / 60;
        sportTimeH.setText(timeH == 0 ? "/" : String.valueOf(time / 3600));
        sportTimeM.setText(timeM == 0 ? "/" : String.valueOf((time % 3600) / 60));
        sportTimeMap = view.findViewById(R.id.sportTimeMap);
        sportTimeStartBtn = view.findViewById(R.id.sportTimeStartBtn);
        sportTimeStartInfo = view.findViewById(R.id.sportTimeStartInfo);



        AndPermission.with(SportTimeFragment.this)
                .runtime()
                .permission(Permission.Group.LOCATION)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {

                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        DialogUIUtils.showToastCenter("请前往设置打开位置权限");
                    }
                })
                .start();
    }

    private void initListener() {
        sportTimeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SportMapActivity.class);
                intent.putExtra("isTiming", timeCountFlag);
                startActivity(intent);
            }
        });
        sportTimeStartBtn.setOnClickListener(v -> {
            timeCountFlag = !timeCountFlag;
            if (timeCountFlag) {
                mTraceClient.startTrace(mTrace, traceListener);

            } else {
                mTraceClient.stopGather(traceListener);
                mTraceClient.stopTrace(mTrace, traceListener);
            }

            if (timeCountFlag) {
                oTimsS = timeS;
                Intent intent = new Intent(activity, SportTimeService.class);
                MyApplication.getInstance().setTiming(true);
                activity.startService(intent);
                startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(System.currentTimeMillis());
                MyApplication.getInstance().setStartTime(startTime);
                sportTimeStartBtn.setSelected(true);
                sportTimeStartBtn.setTextSize(14);
                sportTimeStartBtn.setText("点击暂停");
                sportTimeStartBtn.setTextColor(getResources().getColor(R.color.colorDefaultText));
                sportTimeStartInfo.setSelected(true);
                sportTimeStartInfo.setTextColor(getResources().getColor(R.color.colorLight));
                sportTimeStartInfo.setText("正在计时");

            } else {
                Intent intent = new Intent(activity, SportTimeService.class);
                activity.stopService(intent);
                MyApplication.getInstance().setStartTime(TimeUtil.getCurrentDateStr());
                sportTimeStartBtn.setSelected(false);
                sportTimeStartBtn.setTextSize(24);
                sportTimeStartBtn.setText("GO");
                sportTimeStartBtn.setTextColor(getResources().getColor(R.color.colorLight));
                sportTimeStartInfo.setSelected(false);
                sportTimeStartInfo.setTextColor(getResources().getColor(R.color.colorDefaultText));
                sportTimeStartInfo.setText("暂停计时");
            }

            if (!timeCountFlag) {
                //获取运动时间
                if (timeS - oTimsS > ApiUtil.thresholdS) {
                    ExerciseEntity exerciseEntity = new ExerciseEntity();
                    exerciseEntity.setExEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(System.currentTimeMillis()));
                    exerciseEntity.setExStartTime(startTime);
                    exerciseEntity.setExAmount((int) GPSUtils.getDistance(MyApplication.getInstance().getStartLongitude(),
                            MyApplication.getInstance().getStartLatitude(),
                            MyApplication.getInstance().getEndLongitude(),
                            MyApplication.getInstance().getEndLatitude()));
                    Log.e(TAG, "onClick: "+exerciseEntity.getExEndTime()+"   "+exerciseEntity.getExStartTime() );
                    exerciseEntity.setuId(MyApplication.getInstance().getUserEntity().getObjectId());
                    exerciseEntity.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                                if (e == null){
                                    DialogUIUtils.showToastCenter("数据上传成功");
                                }else {
                                    DialogUIUtils.showToastCenter("数据上传失败，请检查网络设置");
                                }
                            });
                        }
                    });
                   /* HashMap<String, String> hm = new HashMap<>();
                    hm.put("token", ApiUtil.USER_TOKEN);
                    hm.put("time", String.valueOf(timeS - oTimsS));
                    new UniteApi(ApiUtil.EX_ADD, hm).post(new ApiListener() {
                        @Override
                        public void success(Api api) {
                            DialogUIUtils.showToastCenter("数据上传成功");
                        }

                        @Override
                        public void failure(Api api) {
                            DialogUIUtils.showToastCenter("数据上传失败，请检查网络设置");
                        }
                    });*/
                } else {
                    DialogUIUtils.showToastCenter("本次运动时间低于" + ApiUtil.thresholdS + "S，记录不保存");
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(activity, SportTimeService.class);
        activity.stopService(intent);
        context.unregisterReceiver(sportTimeReceiver);
    }
}
