package cn.trunch.weidong.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.jyn.vcview.VerificationCodeView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.trunch.weidong.R;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.util.StatusBarUtil;


public class SettingCodeActivity extends AppCompatActivity {

    private ImageView settingCodeExitBtn;
    private TextView settingCodePhone;
    private VerificationCodeView settingCodeInput;
    private TextView settingCodeResend;

    private String phone;
    private int resendTiming = 60;
    private String resendString;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_code);
        StatusBarUtil.setWindowStatusBarColor(this, R.color.colorBack);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        phone = getIntent().getStringExtra("phone");

        init();
        initListener();
        //验证码重新获取计时
        resendTiming();
    }

    private void init() {
        settingCodeExitBtn = findViewById(R.id.settingCodeExitBtn);
        settingCodePhone = findViewById(R.id.settingCodePhone);
        settingCodePhone.setText(phone);
        settingCodeInput = findViewById(R.id.settingCodeInput);
        settingCodeResend = findViewById(R.id.settingCodeResend);

        settingCodeInput.requestFocus();
        settingCodeInput.setFocusable(true);

    }

    private void initListener() {
        settingCodeExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        settingCodeInput.setOnCodeFinishListener(content -> {
            dialog = DialogUIUtils.showLoading(SettingCodeActivity.this,
                    "验证中...", false, false,
                    false, false)
                    .show();
            HashMap<String, String> hash = new HashMap<>();
            hash.put("code", content);
            hash.put("phone", phone);
            BmobQuery<UserEntity> categoryBmobQuery = new BmobQuery<>();
            categoryBmobQuery.addWhereEqualTo("uPhone", phone);
            categoryBmobQuery.findObjects(new FindListener<UserEntity>() {
                @Override
                public void done(List<UserEntity> object, BmobException e) {
                    if (e == null) {
                        Log.e("BMOB", "查询成功: " + object.size());
                        if (object.size() == 0){
                            UserEntity userEntity = new UserEntity();
                            userEntity.setuPhone(phone);
                            userEntity.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null){
                                        ApiUtil.USER_ID = userEntity.getObjectId();
                                        ApiUtil.USER_AVATAR = userEntity.getuAvatar();
                                        ApiUtil.USER_NAME = userEntity.getuNickname();
                                        SharedPreferences.Editor editor = getSharedPreferences("wd", MODE_PRIVATE).edit();
                                        editor.putString("userId", userEntity.getObjectId());
                                        editor.putString("userToken", ApiUtil.USER_TOKEN);
                                        editor.putString("userAvatar", userEntity.getuAvatar());
                                        editor.putString("userName", userEntity.getuNickname());
                                        editor.putBoolean("isPermit", true);
                                        editor.apply();
                                        runOnUiThread(() -> {
                                            DialogUIUtils.showToastCenter("注册成功");
                                            //Toast.makeText(SettingCodeActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                            toMain();
                                        });
                                    }
                                }
                            });
                        }
                        if (object.size() > 0){
                            UserEntity userEntity = object.get(0);
                            if (userEntity != null){
                                ApiUtil.USER_ID = userEntity.getObjectId();
                                ApiUtil.USER_AVATAR = userEntity.getuAvatar();
                                ApiUtil.USER_NAME = userEntity.getuNickname();
                                SharedPreferences.Editor editor = getSharedPreferences("wd", MODE_PRIVATE).edit();
                                editor.putString("userId", userEntity.getObjectId());
                                editor.putString("userToken", ApiUtil.USER_TOKEN);
                                editor.putString("userAvatar", userEntity.getuAvatar());
                                editor.putString("userName", userEntity.getuNickname());
                                editor.putBoolean("isPermit", true);
                                editor.apply();
                                runOnUiThread(() -> {
                                    DialogUIUtils.showToastCenter("登录成功");
                                    //Toast.makeText(SettingCodeActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                    toMain();
                                });
                            }

                        }
                        //Snackbar.make(mBtnEqual, "查询成功：" + object.size(), Snackbar.LENGTH_LONG).show();
                    } else {
                        Log.e("BMOB", e.toString());
                        //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            });
           /* saveUserInfo();
            dialog.dismiss();
            Intent mainIntent = new Intent(SettingCodeActivity.this, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);*/
           /* new UniteApi(ApiUtil.USER_LOGIN, hash).post(new ApiListener() {
                @Override
                public void success(Api api) {
                    UniteApi u = (UniteApi) api;
                    try {
                        ApiUtil.USER_TOKEN = ((JSONObject) u.getJsonData().get(0)).getString("token");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    saveUserInfo();
                    dialog.dismiss();
                    Intent mainIntent = new Intent(SettingCodeActivity.this, MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mainIntent);
                }

                @Override
                public void failure(Api api) {
                    UniteApi u = (UniteApi) api;
                    dialog.dismiss();
                    String jsonCode = u.getJsonCode();
                    if ("10010".equals(jsonCode)) {
                        DialogUIUtils.showToastCenter("验证码错误");
                    } else if ("10008".equals(jsonCode)) {
                        DialogUIUtils.showToastCenter("验证码超时，请重新获取");
                    } else {
                        DialogUIUtils.showToastCenter("网络异常");
                    }
                }
            });*/

        });
        settingCodeResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendTiming = 60;
                dialog = DialogUIUtils.showLoading(SettingCodeActivity.this,
                        "发送中", false, false,
                        false, false)
                        .show();
                HashMap<String, String> hash = new HashMap<>();
                hash.put("phone", phone);
               /* new UniteApi(ApiUtil.USER_SENDSMS, hash).post(new ApiListener() {
                    @Override
                    public void success(Api api) {
                        dialog.dismiss();
                        resendTiming();
                        DialogUIUtils.showToastCenter("已发送");

                    }

                    @Override
                    public void failure(Api api) {
                        dialog.dismiss();
                        UniteApi u = (UniteApi) api;
                        String jsonCode = u.getJsonCode();
                        if ("10007".equals(jsonCode)) {
                            DialogUIUtils.showToastCenter("验证码获取频繁");
                        } else {
                            DialogUIUtils.showToastCenter("网络异常");
                        }
                    }
                });*/
            }
        });
    }

    private void toMain() {
        Intent mainIntent = new Intent(SettingCodeActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
    }

    private void resendTiming() {
        settingCodeResend.setEnabled(false);
        settingCodeResend.setTextColor(getResources().getColor(R.color.colorDefaultText));
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = resendTiming; i > 0; i--) {
                    resendString = i + " 秒后可重新获取";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            settingCodeResend.setText(resendString);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        settingCodeResend.setEnabled(true);
                        settingCodeResend.setTextColor(getResources().getColor(R.color.colorTheme));
                        settingCodeResend.setText("重新获取验证码");
                    }
                });
            }
        }).start();

    }

    private void saveUserInfo() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        new UniteApi(ApiUtil.USER_INFO, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi u = (UniteApi) api;
                Gson gson = new Gson();
                try {
                    UserEntity user = gson.fromJson(u.getJsonData().get(0).toString(), UserEntity.class);
                    ApiUtil.USER_ID = user.getuId();
                    ApiUtil.USER_AVATAR = user.getuAvatar();
                    ApiUtil.USER_NAME = user.getuNickname();
                    SharedPreferences.Editor editor = getSharedPreferences("wd", MODE_PRIVATE).edit();
                    editor.putString("userId", user.getuId());
                    editor.putString("userToken", ApiUtil.USER_TOKEN);
                    editor.putString("userAvatar", user.getuAvatar());
                    editor.putString("userName", user.getuNickname());
                    editor.putBoolean("isPermit", true);
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                    DialogUIUtils.showToastCenter("数据初始化失败");
                }
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("数据初始化失败");
            }
        });
    }
}
