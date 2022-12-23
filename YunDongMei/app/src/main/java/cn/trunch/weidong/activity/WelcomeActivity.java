package cn.trunch.weidong.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.dou361.dialogui.DialogUIUtils;
import com.lzy.ninegrid.NineGridView;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.trunch.weidong.R;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.OkHttpUtil;
import cn.trunch.weidong.MyApplication;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class WelcomeActivity extends AppCompatActivity {

    private TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //隐藏标题栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); //布局延伸
        setContentView(R.layout.activity_welcome);

        //根据状态栏颜色来决定状态栏文字用黑色还是白色
        //StatusBarUtil.setWindowStatusBarColor(this, R.color.colorLight);
        //初始化九图
        NineGridView.setImageLoader(new GlideImageLoader());
        //初始化通讯
        OkHttpUtil.init();
        //初始化地图
        SDKInitializer.initialize(getApplicationContext());
        // 自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        // 包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        //初始化弹框
        DialogUIUtils.init(this);

        initListener();

       /* BmobFile bmobFile = new BmobFile(new File(Environment.getExternalStorageDirectory().getPath()+"/ChengTou/208514901-1-208.mp4"));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    //toast("上传文件成功:" + bmobFile.getFileUrl());
                    Log.e("TAG", "11212  done: "+bmobFile.getFileUrl());
                }else{
                    //toast("上传文件失败：" + e.getMessage());--
                    Log.e("TAG", "32323 done: "+e.toString() );
                }

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
                Log.e("TAG", "onProgress: "+value);
            }
        });*/
    }



    private void initListener() {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                SharedPreferences preferences = getSharedPreferences("wd", MODE_PRIVATE);
                ApiUtil.USER_ID = preferences.getString("userId", "");
                ApiUtil.USER_TOKEN = preferences.getString("userToken", "");
                ApiUtil.USER_AVATAR = preferences.getString("userAvatar", "");
                ApiUtil.USER_NAME = preferences.getString("userName", "未知用户");
                boolean isPermit = preferences.getBoolean("isPermit", false);
                if (isPermit) {
                    BmobQuery<UserEntity> categoryBmobQuery = new BmobQuery<>();
                    categoryBmobQuery.getObject(ApiUtil.USER_ID, new QueryListener<UserEntity>() {
                        @Override
                        public void done(UserEntity userEntity, BmobException e) {
                            if (e == null) {
                                if (userEntity != null){
                                    runOnUiThread(() -> {
                                        MyApplication.getInstance().setUserEntity(userEntity);
                                        startActivity(new Intent(
                                                WelcomeActivity.this, MainActivity.class));
                                        finish();
                                    });
                                }else {
                                    runOnUiThread(() -> {
                                        startActivity(new Intent(
                                                WelcomeActivity.this, SettingPhoneActivity.class));
                                        finish();
                                    });
                                }
                                //toast("查询成功");
                            } else {
                                runOnUiThread(() -> {
                                    startActivity(new Intent(
                                            WelcomeActivity.this, SettingPhoneActivity.class));
                                    finish();
                                });
                                //toast("查询失败：" + e.getMessage());
                            }
                        }
                    });
                } else {
                    startActivity(new Intent(
                            WelcomeActivity.this, SettingPhoneActivity.class));
                    finish();
                }
            });
        }).start();
    }

    /**
     * 九图 加载
     */
    private class GlideImageLoader implements NineGridView.ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context)
                    .load(url)//
                    .placeholder(R.drawable.bg_image_default)//
                    .error(R.drawable.ic_default_image)//
                    .apply(bitmapTransform(new MultiTransformation<>(
                            new CenterCrop(),
                            new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)
                    )))
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }
}
