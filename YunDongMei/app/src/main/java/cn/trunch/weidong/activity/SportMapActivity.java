package cn.trunch.weidong.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.SupplementMode;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.Point;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.TransportMode;
import com.dou361.dialogui.DialogUIUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;
import java.util.TimerTask;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.trunch.weidong.MyApplication;
import cn.trunch.weidong.R;
import cn.trunch.weidong.entity.Rank;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.service.SportTimeService;
import cn.trunch.weidong.util.GPSUtils;
import cn.trunch.weidong.util.MyTimeTask;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.util.TimeUtil;

public class SportMapActivity extends AppCompatActivity {

    //------------------------
    private TextView sportMapSpeed;
    private TextView sportMapTime;
    private TextView sportMapDistance;
    private FloatingActionButton sportMapClose;
    private TextView sportMapTimeInfo;

    private long timeH = 0;
    private long timeM = 0;
    private long timeS = 0;
    private IntentFilter intentFilter;
    private SportTimeReceiver sportTimeReceiver;
    //------------------------
    MapView mapView;
    BaiduMap baiduMap;
    LocationClient locationClient;
    LBSTraceClient mTraceClient;
    LocationClientOption option;
    long serviceId = 227523;
    long startTime = 0;
    long endTime = 0;
    String uid = MyApplication.getInstance().getUserEntity().getObjectId();
    // 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK
    // v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
    int pageSize = 5000;
    int pageIndex = 1;
    boolean queryHis = true;
    boolean isQuery = true;
    private boolean isFirstLoc = true;
    private MapStatus.Builder builder;
    private LatLng last;
    private List<LatLng> points = new ArrayList<>();
    private Polyline mPolyline;
    private double mCurrentLat;
    private double mCurrentLon;
    private MyLocationData locData;
    private boolean isTiming;
    private long time;
    private int isfirst = 0;
    private MyTimeTask timeTask;               //定时器
    int flag = 0;
    boolean isStart = false;

    private void startTask() {
        timeTask = new MyTimeTask(new TimerTask() {
            @Override
            public void run() {
                if (flag >= 5) {
                    //isStart = true;
                }
                flag++;
            }
        });
        timeTask.startTask(1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_map);
        StatusBarUtil.setWindowStatusBarColor(this, R.color.colorLight);

        this.InitLocation();
        mTraceClient = new LBSTraceClient(getApplicationContext());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String format = sdf.format(new Date());
        DateFormat df = sdf;
        Date parse;
        try {
            parse = df.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
            parse = new Date();
        }
        startTime = TimeUtil.getTimestamp(MyApplication.getInstance().getStartTime());//parse.getTime();
        // 控件操作
        sportMapSpeed = findViewById(R.id.sportMapSpeed);
        sportMapTime = findViewById(R.id.sportMapTime);
        sportMapDistance = findViewById(R.id.sportMapDistance);
        sportMapClose = findViewById(R.id.sportMapClose);
        sportMapTimeInfo = findViewById(R.id.sportMapTimeInfo);
        isTiming = getIntent().getBooleanExtra("isTiming", false);
        if (isTiming) {
            startTask();
            sportMapTimeInfo.setSelected(true);
            sportMapTimeInfo.setTextColor(getResources().getColor(R.color.colorLight));
            sportMapTimeInfo.setText("正在计时");
        } else {
            sportMapTimeInfo.setSelected(false);
            sportMapTimeInfo.setTextColor(getResources().getColor(R.color.colorDefaultText));
            sportMapTimeInfo.setText("暂停计时");
        }
        sportMapClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 广播注册
        intentFilter = new IntentFilter();
        intentFilter.addAction(SportTimeService.STEP_TIME_ACTION);
        sportTimeReceiver = new SportTimeReceiver();
        registerReceiver(sportTimeReceiver, intentFilter);

        LiveEventBus.get("network", Integer.class).observe(this, integer -> {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SportMapActivity.this, "" + integer, Toast.LENGTH_SHORT).show();
                }
            });
            if (1 == integer) {
                flag = 0;
                isStart = false;
            }
        });
    }

    public void InitLocation() {
        // TODO 获取地图控件
        mapView = findViewById(R.id.sportMapView);
        baiduMap = mapView.getMap();
        // TODO 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        locationClient = new LocationClient(getApplicationContext());
        // TODO 注册监听
        this.setLocationOption();
        locationClient.registerLocationListener(myListener);
        locationClient.start();
    }

    public BDAbstractLocationListener myListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mapView == null) {
                return;
            }
            MyLocationData locationData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locationData);

            if (location.getSpeed() >= 0)
                sportMapSpeed.setText(location.getSpeed() + "");
            LatLng l2;
            l2 = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate mapupdate = MapStatusUpdateFactory.newLatLngZoom(l2, 18);
            baiduMap.animateMapStatus(mapupdate);

            last = l2;
            if (!isTiming) {
                return;
            }
            if (isFirstLoc) {
                isFirstLoc = false;
                return;
            }


            LatLng pointsss = l2;
            if (GPSUtils.getDistance(last.longitude, last.latitude, pointsss.longitude, pointsss.latitude) < 5) {
                return;
            }
            if (pointsss.longitude > 0 && pointsss.latitude > 0)
                points.add(pointsss);
            if (points.size() >= 2) {
                OverlayOptions mOverlay = new PolylineOptions()
                        .width(10)
                        .color(0xAAFF0000)
                        .points(points);
                Overlay mPolyline = baiduMap.addOverlay(mOverlay);
                mPolyline.setZIndex(3);
            }


            Log.e("TAG", "onReceiveLocation: " + location.getSpeed() + "  " + location.getLongitude() + "  " + location.getLatitude());
            /* if (isFirstLoc) {//首次定位
             *//**第一个点很重要，决定了轨迹的效果，gps刚接收到信号时返回的一些点精度不高，
             * 尽量选一个精度相对较高的起始点，这个过程大概从gps刚接收到信号后5-10秒就可以完成，不影响效果。
             * 注：gps接收卫星信号少则十几秒钟，多则几分钟，
             * 如果长时间手机收不到gps，退出，重启手机再试，这是硬件的原因
             *//*
                    LatLng ll;
                    //选一个精度相对较高的起始点
                    ll = getMostAccuracyLocation(location);
                    if(ll == null){
                        return;
                    }
                    isFirstLoc = false;
                    points.add(ll);//加入集合
                    last = ll;

                    //显示当前定位点，缩放地图
                    locateAndZoom(location, ll);

                    //标记起点图层位置
                    MarkerOptions oStart = new MarkerOptions();// 地图标记覆盖物参数配置类
                    oStart.position(points.get(0));// 覆盖物位置点，第一个点为起点
                    oStart.icon(startBD);// 设置覆盖物图片
                    baiduMap.addOverlay(oStart); // 在地图上添加此图层
                    return;//画轨迹最少得2个点，首地定位到这里就可以返回了
                }

                //从第二个点开始
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                //sdk回调gps位置的频率是1秒1个，位置点太近动态画在图上不是很明显，可以设置点之间距离大于为5米才添加到集合中
                if (GPSUtils.getDistance(last.longitude,last.latitude, ll.longitude,ll.latitude) < 5) {
                    return;
                }

                points.add(ll);//如果要运动完成后画整个轨迹，位置点都在这个集合中

                last = ll;

                //显示当前定位点，缩放地图
                locateAndZoom(location, ll);

                //清除上一次轨迹，避免重叠绘画
                baiduMap.clear();

                //起始点图层也会被清除，重新绘画
                MarkerOptions oStart = new MarkerOptions();
                oStart.position(points.get(0));
                oStart.icon(startBD);
                baiduMap.addOverlay(oStart);

                //将points集合中的点绘制轨迹线条图层，显示在地图上
                OverlayOptions ooPolyline = new PolylineOptions().width(13).color(0xAAFF0000).points(points);
                mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline);*/
        }


    };


    //首次定位很重要，选一个精度相对较高的起始点
    private LatLng getMostAccuracyLocation(final BDLocation location) {

        if (location.getRadius() > 25) {//gps位置精度大于25米的点直接弃用
            return null;
        }

        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

        if (GPSUtils.getDistance(last.longitude, last.latitude, ll.longitude, ll.latitude) > 5) {
            last = ll;
            points.clear();//有两点位置大于5，重新来过
            return null;
        }
        points.add(ll);
        last = ll;
        //有5个连续的点之间的距离小于5，认为gps已稳定，以最新的点为起始点
        if (points.size() >= 5) {
            points.clear();
            return ll;
        }
        return null;
    }


    //显示当前定位点，缩放地图
    private void locateAndZoom(BDLocation location, LatLng ll) {
        /**
         * 记录当前经纬度，当位置不变，手机转动，取得方向传感器的方向，
         给地图重新设置位置参数，在跟随模式下可使地图箭头随手机转动而转动
         */
        mCurrentLat = location.getLatitude();
        mCurrentLon = location.getLongitude();
        locData = new MyLocationData.Builder().accuracy(0)//去掉精度圈
                //此mCurrentDirection为自己获取到的手机传感器方向信息，顺时针0-360
                //.direction(mCurrentDirection).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        baiduMap.setMyLocationData(locData);//显示当前定位位置点

        //给地图设置缩放中心点，和缩放比例值
        builder = new MapStatus.Builder();
        builder.target(ll).zoom(18);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }


    private void setLocationOption() {
        option = new LocationClientOption();
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
      /*  option.setCoorType("bd09ll");
        option.setScanSpan(2000);
        option.setIsNeedAddress(false);
        option.setLocationNotify(true);
        option.setNeedDeviceDirect(false);
        option.setOpenAutoNotifyMode();*/
        // 如果设置为0，则代表单次定位，即仅定位一次，默认为0
        // 如果设置非0，需设置1000ms以上才有效
        option.setScanSpan(3000);
        option.setCoorType("bd09ll"); // 设置坐标类型


        locationClient.setLocOption(option);
    }

    public void queryHistory() {
        endTime = (new Date()).getTime();
        Log.e("TAG", "queryHistory: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(endTime) + "  " +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(startTime) + "  " + startTime + "  " + endTime);
        HistoryTrackRequest hrt = new HistoryTrackRequest(1, serviceId, uid);

       /* hrt.setProcessed(true);
        // 创建纠偏选项实例
        ProcessOption processOption = new ProcessOption();
        // 设置需要去噪
        processOption.setNeedDenoise(true);
        // 设置需要抽稀
        processOption.setNeedVacuate(true);
        // 设置需要绑路
        processOption.setNeedMapMatch(true);
        // 设置精度过滤值(定位精度大于100米的过滤掉)
        processOption.setRadiusThreshold(100);
        // 设置交通方式为驾车
        processOption.setTransportMode(TransportMode.walking);
        // 设置纠偏选项
        hrt.setProcessOption(processOption);
        // 设置里程填充方式为驾车
        hrt.setSupplementMode(SupplementMode.walking);*/
        hrt.setProcessed(true);
        ProcessOption processOption = new ProcessOption();//纠偏选项
        processOption.setRadiusThreshold(50);//精度过滤
        processOption.setTransportMode(TransportMode.walking);//交通方式，默认为驾车
        processOption.setNeedDenoise(true);//去噪处理，默认为false，不处理
        processOption.setNeedVacuate(true);//设置抽稀，仅在查询历史轨迹时有效，默认需要false
        processOption.setNeedMapMatch(true);//绑路处理，将点移到路径上，默认不需要false
        hrt.setProcessOption(processOption);
        hrt.setPageIndex(pageIndex);
        hrt.setPageSize(pageSize);
        hrt.setStartTime(TimeUtil.s2l(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(startTime)));
        hrt.setEndTime(TimeUtil.s2l(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(endTime)));
        mTraceClient.queryHistoryTrack(hrt, new OnTrackListener() {
            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                super.onHistoryTrackCallback(response);
                Log.e("TAG", "onHistoryTrackCallback: " + response);
                if (response.getSize() > 0) {//如果当前日期范围内有数据点，则准备绘制
                    List<TrackPoint> tp = response.getTrackPoints();// 所有点的坐标信息数据集
                    Date start = new Date();
                    start.setTime(startTime);
                    Date end = new Date();
                    end.setTime(endTime);
                   /* for (int i = 0; i < tp.size(); i++) {
                        TrackPoint t = tp.get(i);
                        Date now = new Date();
                        now.setTime(TimeUtil.getTimestamp(t.getCreateTime()));
                        if (TimeUtil.isEffectiveDate(now, start,end)){
                            tp.remove(i);
                            i--;
                        }
                    }*/
                    Log.e("TAG", "onHistoryTrackCallback: " + tp.size());
                    Point startPoint = response.getStartPoint();// 起点的坐标信息
                    Point endPoint = response.getEndPoint();// 终点的坐标信息
                    MyApplication.getInstance().setStartPoint(startPoint.getLocation().getLongitude(), startPoint.getLocation().getLatitude());
                    MyApplication.getInstance().setEndPoint(endPoint.getLocation().getLongitude(), endPoint.getLocation().getLatitude());
                    drawHistoryTrack(tp, startPoint, endPoint);// 绘制折线
                }
            }
        });

    }

    private void drawHistoryTrack(List<TrackPoint> list_tp, Point startPoint,
                                  Point endPoint) {
        BDLocation location = new BDLocation();
        location.setLatitude(endPoint.getLocation().getLatitude());
        location.setLongitude(endPoint.getLocation().getLongitude());
        location.setRadius((float) endPoint.getRadius());
        myListener.onReceiveLocation(location);
        runOnUiThread(() -> {
            double dist = GPSUtils.getDistance(startPoint.getLocation().longitude, startPoint.getLocation().latitude,
                    endPoint.getLocation().getLongitude(), endPoint.getLocation().getLatitude());
            //sportMapDistance.setText((Math.round(dist/100d)/10d)+"");
            sportMapDistance.setText(dist + "");
        });
        if (isfirst == 0){
            showMarker(startPoint, 0);
        }
        showMarker(endPoint, 1);
        if (list_tp.size() <= 3)
            return;
        List<LatLng> points = new ArrayList<LatLng>();
        for (int i = 0; i < list_tp.size(); i++) {
            points.add(new LatLng(list_tp.get(i).getLocation().getLatitude(),
                    list_tp.get(i).getLocation().getLongitude()));
        }
        // 构造对象
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(points);
        // 添加到地图
        baiduMap.addOverlay(ooPolyline);
    }

    private void showMarker(Point point, int i) {
        // 定义Maker坐标点
        LatLng latlng = new LatLng(point.getLocation().getLatitude(), point
                .getLocation().getLongitude());
        // 构建Marker图标
        BitmapDescriptor bitmap;
        if (i == 0) {
            bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_start);
        } else if (i == 1) {
            bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_road);
        } else {
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_road);
        }

        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(latlng).icon(
                bitmap);
        // 在地图上添加Marker，并显示
        baiduMap.addOverlay(option);
        if (i == 0){
            isfirst ++;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        queryHis = false;
        isQuery = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        isQuery = true;
        startQ();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        queryHis = false;
        isQuery = false;
        //注销广播
        unregisterReceiver(sportTimeReceiver);
    }


    public void startQ() {
        new Thread(() -> {
            while (isQuery && !isStart) {
                try {
                    queryHistory();
                    Thread.sleep(10000);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }).start();
    }

    class SportTimeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SportTimeService.STEP_TIME_ACTION.equals(intent.getAction())) {
                timeS = intent.getLongExtra("timeS", 0);
                time = intent.getLongExtra("time", 0);
               /* timeH = timeS / 3600;
                timeM = (timeS % 3600) / 60;
                sportMapTime.setText(getFormatDate());*/
                timeH = time / 3600;
                timeM = (time % 3600) / 60;
                sportMapTime.setText(getFormatDate());
            }
        }
    }

    private String getFormatDate() {
       /* String timeStringS;
        String timeStringM;
        String timeStringH;
        if (timeM < 10) {
            timeStringM = "0" + String.valueOf(timeM);
        } else {
            timeStringM = String.valueOf(timeM);
        }
        if (timeS % 60 < 10) {
            timeStringS = "0" + String.valueOf(timeS % 60);
        } else {
            timeStringS = String.valueOf(timeS % 60);
        }
        if (timeH > 0 && timeH < 10) {
            timeStringH = "0" + String.valueOf(timeH);
            return timeStringH + ":" + timeStringM + ":" + timeStringS;
        } else if (timeH >= 10) {
            timeStringH = String.valueOf(timeH);
            return timeStringH + ":" + timeStringM + ":" + timeStringS;
        } else {
            return timeStringM + ":" + timeStringS;
        }*/
        String timeStringS;
        String timeStringM;
        String timeStringH;
        if (timeM < 10) {
            timeStringM = "0" + String.valueOf(timeM);
        } else {
            timeStringM = String.valueOf(timeM);
        }
        if (time % 60 < 10) {
            timeStringS = "0" + String.valueOf(time % 60);
        } else {
            timeStringS = String.valueOf(time % 60);
        }
        if (timeH > 0 && timeH < 10) {
            timeStringH = "0" + String.valueOf(timeH);
            return timeStringH + ":" + timeStringM + ":" + timeStringS;
        } else if (timeH >= 10) {
            timeStringH = String.valueOf(timeH);
            return timeStringH + ":" + timeStringM + ":" + timeStringS;
        } else {
            return timeStringM + ":" + timeStringS;
        }
    }
}
