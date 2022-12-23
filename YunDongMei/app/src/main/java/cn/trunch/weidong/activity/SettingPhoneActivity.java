package cn.trunch.weidong.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dou361.dialogui.DialogUIUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.trunch.weidong.R;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.MyApplication;
import cn.trunch.weidong.step.config.Constant;
import cn.trunch.weidong.util.StatusBarUtil;


public class SettingPhoneActivity extends AppCompatActivity {

    @BindView(R.id.settingPwdInput)
    EditText settingPwdInput;
    //    private ImageView settingPhoneExitBtn;
    private EditText settingPhoneInput;
    private Button settingPhoneGoBtn;

    private LinearLayout settingPhoneQQBtn;
    private LinearLayout settingPhoneWeChatBtn;
    private LinearLayout settingPhoneLinkedInBtn;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_phone);
        ButterKnife.bind(this);
        StatusBarUtil.setWindowStatusBarColor(this, R.color.colorBack);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        init();
        initListener();
        DialogUIUtils.init(this);

    }

    private void init() {
//        settingPhoneExitBtn = findViewById(R.id.settingPhoneExitBtn);
        settingPhoneInput = findViewById(R.id.settingPhoneInput);
        settingPhoneGoBtn = findViewById(R.id.settingPhoneGoBtn);
        settingPhoneGoBtn.setEnabled(false);
        settingPhoneQQBtn = findViewById(R.id.settingPhoneQQBtn);
        settingPhoneWeChatBtn = findViewById(R.id.settingPhoneWeChatBtn);
        settingPhoneLinkedInBtn = findViewById(R.id.settingPhoneLinkedInBtn);
    }

    private void initListener() {
//        settingPhoneExitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        settingPhoneGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*dialog = DialogUIUtils.showLoading(SettingPhoneActivity.this,
                        "验证中...", false, false,
                        false, false)
                        .show();
                HashMap<String, String> hash = new HashMap<>();
                hash.put("phone", settingPhoneInput.getText().toString());
                Intent settingCodeIntent = new Intent(SettingPhoneActivity.this, SettingCodeActivity.class);
                settingCodeIntent.putExtra("phone", settingPhoneInput.getText().toString());
                startActivity(settingCodeIntent);*/
                String phone = settingPhoneInput.getText().toString();
                String pwd = settingPwdInput.getText().toString();

                if (TextUtils.isEmpty(phone)) {
                    DialogUIUtils.showToastCenter("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    DialogUIUtils.showToastCenter("请输入密码");
                    return;
                }
                BmobQuery<UserEntity> categoryBmobQuery = new BmobQuery<>();
                categoryBmobQuery.addWhereEqualTo("uPhone", phone);
                categoryBmobQuery.findObjects(new FindListener<UserEntity>() {
                    @Override
                    public void done(List<UserEntity> object, BmobException e) {
                        if (e == null) {
                            Log.e("BMOB", "查询成功: " + object.size());
                            if (object.size() == 0){
                               /* UserEntity userEntity = new UserEntity();
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

                                        }
                                    }
                                });*/
                                runOnUiThread(() -> {
                                    DialogUIUtils.showToastCenter("此手机号未注册，请先注册");
                                    //Toast.makeText(SettingCodeActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                });
                            }
                            if (object.size() > 0){
                                UserEntity userEntity = object.get(0);
                                if (userEntity != null){
                                    if (pwd.equals(userEntity.getuPwd())){
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
                                        MyApplication.getInstance().setUserEntity(userEntity);
                                        runOnUiThread(() -> {
                                            DialogUIUtils.showToastCenter("登录成功");
                                            //Toast.makeText(SettingCodeActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                            toMain();
                                        });
                                    }else {
                                        runOnUiThread(() -> {
                                            DialogUIUtils.showToastCenter("密码输入错误！");
                                            //Toast.makeText(SettingCodeActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                        });
                                    }
                                }

                            }
                            //Snackbar.make(mBtnEqual, "查询成功：" + object.size(), Snackbar.LENGTH_LONG).show();
                        } else {
                            Log.e("BMOB", e.toString());
                            //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
        settingPhoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isMobile(s.toString())) {
                    settingPhoneGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_circle));
                    settingPhoneGoBtn.setEnabled(true);
                } else {
                    settingPhoneGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_pale_circle));
                    settingPhoneGoBtn.setEnabled(false);
                }
            }
        });
        settingPhoneQQBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.showToastCenter("请使用手机免登录进行登录");
            }
        });
        settingPhoneWeChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.showToastCenter("请使用手机免登录进行登录");
            }
        });
        settingPhoneLinkedInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.showToastCenter("请使用手机免登录进行登录");
            }
        });
    }


    private void toMain() {
        Intent mainIntent = new Intent(SettingPhoneActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
    }

    public static boolean isMobile(String str) {
        Pattern p;
        Matcher m;
        boolean b;
        String s2 = "^[1](([3|5|8][\\d])|([4][5,6,7,8,9])|([6][5,6])|([7][3,4,5,6,7,8])|([9][8,9]))[\\d]{8}$";// 验证手机号
        p = Pattern.compile(s2);
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    @OnClick(R.id.register_tv)
    public void onViewClicked() {
        startActivityForResult(new Intent(SettingPhoneActivity.this,RegisterActivity.class),100);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100){
            if (data != null){
                settingPhoneInput.setText(data.getStringExtra(Constant.PHONE));
            }
        }
    }
}
