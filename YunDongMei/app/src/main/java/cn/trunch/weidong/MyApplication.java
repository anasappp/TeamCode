package cn.trunch.weidong;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.http.HandleSSL;
import cn.trunch.weidong.util.TimeUtil;

public class MyApplication extends Application {
    /*全局上下文*/
    private static Context appContext;
    private UserEntity userEntity;
    private double startLongitude;
    private double startLatitude;
    private double endLongitude;
    private double endLatitude;
    private String startTime = TimeUtil.getCurrentDateStr();
    private boolean timing;

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    private static MyApplication instance;
    public static synchronized MyApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // 全局上下文
        appContext = this;
        instance = this;

        //第一：默认初始化
        Bmob.resetDomain("http://bmobsdk3user3.15456.cn/8/");
        Bmob.initialize(this, "22acfb51f313a24420c1b8736dadf94f");
        HandleSSL handleSSL = new HandleSSL();
        handleSSL.handleSSLHandshake();
    }



    /**
     * 获取全局上下文
     */
    public static Context getAppContext() {
        return appContext;
    }

    public void setStartPoint(double longitude, double latitude) {
        this.startLongitude = longitude;
        this.startLatitude = latitude;
    }

    public void setEndPoint(double longitude, double latitude) {
        this.endLongitude = longitude;
        this.endLatitude = latitude;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setTiming(boolean b) {
        timing = b;
    }

    public boolean isTiming() {
        return timing;
    }
}
