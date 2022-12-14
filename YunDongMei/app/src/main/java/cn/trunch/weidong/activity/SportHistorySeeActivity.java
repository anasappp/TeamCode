package cn.trunch.weidong.activity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.Point;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.TransportMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.util.TimeUtil;

public class SportHistorySeeActivity extends AppCompatActivity {
    private FloatingActionButton closeBtn;
    MapView mapView;
    BaiduMap baiduMap;
    public LBSTraceClient mTraceClient;
    LatLng l2;
    long serviceId = 227523;
    String uid = "Trace_Demo";
    String st = "2019-04-25 12:00:00";
    String et= "2019-04-25 12:00:00";
    int pageSize = 3000;
    int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_history_map);
       StatusBarUtil.setWindowStatusBarColor(this, R.color.colorLight);

        this.InitLocation();
        Bundle bundle = getIntent().getExtras();
        uid = bundle.getString("uid");
        st = bundle.getString("sTime");
        et = bundle.getString("eTime");
//        et=bundle.getString("eTime");
        mTraceClient = new LBSTraceClient(getApplicationContext());
        this.queryHistory();
    }

    public void InitLocation() {
        closeBtn = findViewById(R.id.sportHistoryMapClose);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // TODO ??????????????????
        mapView = findViewById(R.id.sportHistoryMapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
    }

    public void queryHistory() {

        long sTime = TimeUtil.s2l(st);
        long eTime = TimeUtil.s2l(et);
        Log.e("TAG", "queryHistory: "+st+"  "+et);
        HistoryTrackRequest hrt = new HistoryTrackRequest(1, serviceId, uid);
        hrt.setProcessed(true);
        ProcessOption processOption = new ProcessOption();//????????????
        processOption.setRadiusThreshold(50);//????????????
        processOption.setTransportMode(TransportMode.walking);//??????????????????????????????
        processOption.setNeedDenoise(true);//????????????????????????false????????????
        processOption.setNeedVacuate(true);//???????????????????????????????????????????????????????????????false
        processOption.setNeedMapMatch(true);//??????????????????????????????????????????????????????false
        hrt.setProcessOption(processOption);
        hrt.setPageIndex(pageIndex);
        hrt.setPageSize(pageSize);
        hrt.setStartTime(sTime);
        hrt.setEndTime(eTime);
        int a = 20;
        OnTrackListener onTrackListener = new OnTrackListener() {
            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                super.onHistoryTrackCallback(response);
                if (response.getSize() > 0) {//?????????????????????????????????????????????????????????
                    List<TrackPoint> tp = response.getTrackPoints();// ?????????????????????????????????
                    Point startPoint = response.getStartPoint();// ?????????????????????
                    Point endPoint = response.getEndPoint();// ?????????????????????
                    drawHistoryTrack(tp, startPoint, endPoint);// ????????????
                } else {
                }
            }

        };

        mTraceClient.queryHistoryTrack(hrt, onTrackListener);


    }

    private void drawHistoryTrack(List<TrackPoint> list_tp, Point startPoint,
                                  Point endPoint) {

        showMarker(startPoint, 0);
        showMarker(endPoint, 1);
        if (list_tp.size() <= 3)
            return;
        List<LatLng> points = new ArrayList<LatLng>();
        for (int i = 0; i < list_tp.size(); i++) {
            points.add(new LatLng(list_tp.get(i).getLocation().getLatitude(),
                    list_tp.get(i).getLocation().getLongitude()));
        }
        // ????????????
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(points);
        // ???????????????
        baiduMap.addOverlay(ooPolyline);
        MapStatusUpdate mapupdate = MapStatusUpdateFactory.newLatLngZoom(l2, 18);
        baiduMap.animateMapStatus(mapupdate);
        baiduMap.setMyLocationEnabled(false);
    }

    private void showMarker(Point point, int i) {

        // ??????Maker?????????
        LatLng latlng = new LatLng(point.getLocation().getLatitude(), point
                .getLocation().getLongitude());
        // ??????Marker??????
        BitmapDescriptor bitmap;
        if (i == 0) {
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_start);
            MyLocationData locationData = new MyLocationData.Builder()
                    .longitude(point.getLocation().getLongitude())
                    .latitude(point.getLocation().getLatitude())
                    .direction(point.getDirection())
                    .accuracy((float) point.getRadius())
                    .build();
            baiduMap.setMyLocationData(locationData);
            l2 = new LatLng(point.getLocation().getLatitude(), point.getLocation().getLongitude());
        } else if (i == 1) {
            bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_end);
            ;
        } else {
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_end);
        }

        // ??????MarkerOption???????????????????????????Marker
        OverlayOptions option = new MarkerOptions().position(latlng).icon(
                bitmap);
        // ??????????????????Marker????????????
        baiduMap.addOverlay(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("daladala", "-----------destory()---------");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("daladala", "-----------start()---------");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("daladala", "-----------pause()---------");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("daladala", "-----------resume()---------");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("daladala", "-----------stop()---------");
    }
}
