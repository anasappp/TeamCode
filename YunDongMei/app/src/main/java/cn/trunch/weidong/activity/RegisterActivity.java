package cn.trunch.weidong.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.trunch.weidong.MyApplication;
import cn.trunch.weidong.R;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.step.config.Constant;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.settingCodeExitBtn)
    ImageView settingCodeExitBtn;
    @BindView(R.id.settingPhoneGoBtn)
    Button settingPhoneGoBtn;
    @BindView(R.id.pwd_edit)
    EditText pwdEdit;
    @BindView(R.id.check_edit)
    EditText checkEdit;
    @BindView(R.id.phone_edit)
    EditText phoneEdit;
    @BindView(R.id.center_title)
    TextView centerTitle;

    private String phone;
    private int type;
    private UserEntity userEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type",0);
        if (type == 1){
            centerTitle.setText("修改密码");
            userEntity = MyApplication.getInstance().getUserEntity();
            phoneEdit.setText(userEntity.getuPhone());
            phoneEdit.setEnabled(false);
            pwdEdit.setText(userEntity.getuPwd());
            checkEdit.setHint("请再次输入新密码");
            settingPhoneGoBtn.setText("确定修改");
        }


    }

    @OnClick({R.id.settingCodeExitBtn, R.id.settingPhoneGoBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.settingCodeExitBtn:
                finish();
                break;
            case R.id.settingPhoneGoBtn:
                String pwd = pwdEdit.getText().toString();
                String checkPwd = checkEdit.getText().toString();
                phone = phoneEdit.getText().toString();

                if (TextUtils.isEmpty(phone)) {
                    DialogUIUtils.showToastCenter("请输入手机号");
                    return;
                }
                if (!isMobile(phone)) {
                    DialogUIUtils.showToastCenter("请输入正确手机号");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    DialogUIUtils.showToastCenter("请输入密码");
                    return;
                }
                if (pwd.length() < 6 || pwd.length() > 18) {
                    DialogUIUtils.showToastCenter("请输入6~18位密码");
                    return;
                }
                if (type == 1){
                    if (pwd.equals(userEntity.getuPwd())){
                        DialogUIUtils.showToastCenter("新密码不能与旧密码一致");
                        return;
                    }
                }
                if (TextUtils.isEmpty(checkPwd)) {
                    DialogUIUtils.showToastCenter("请再次输入密码");
                    return;
                }
                if (!pwd.equals(checkPwd)) {
                    DialogUIUtils.showToastCenter("两次输入的密码不一致！");
                    return;
                }
                if (type == 1){
                    new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("确定要修改密码吗？")
                            .setNegativeButton("确定", (dialogInterface, i) -> {
                                userEntity.setuPwd(pwd);
                                userEntity.update(userEntity.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        runOnUiThread(() -> {
                                            if (e == null){
                                                DialogUIUtils.showToastCenter("密码修改成功，请重新登录！");
                                                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getSharedPreferences("wd", MODE_PRIVATE).edit();
                                                editor.putBoolean("isPermit", false);
                                                startActivity(new Intent(MyApplication.getInstance().getApplicationContext(),
                                                        SettingPhoneActivity.class)
                                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK |
                                                                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                                            }
                                        });
                                    }
                                });
                            })
                            .setPositiveButton("取消", (dialogInterface, i) -> {

                            }).show();


                }else {
                    BmobQuery<UserEntity> query = new BmobQuery<>();
                    query.addWhereEqualTo("uPhone", phone);
                    query.findObjects(new FindListener<UserEntity>() {
                        @Override
                        public void done(List<UserEntity> object, BmobException e) {
                            if (e == null) {
                                Log.e("BMOB", "查询成功: " + object.size());
                                if (object.size() == 0) {
                                    UserEntity userEntity = new UserEntity();
                                    userEntity.setuPhone(phone);
                                    userEntity.setuPwd(pwd);
                                    userEntity.setuNickname("用户" + phone);
                                    userEntity.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                runOnUiThread(() -> {
                                                    DialogUIUtils.showToastCenter("注册成功！请登录");
                                                    Intent intent = new Intent();
                                                    intent.putExtra(Constant.PHONE, phone);
                                                    setResult(RESULT_OK, intent);
                                                    finish();
                                                });
                                            } else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        DialogUIUtils.showToastCenter("注册失败！");
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                                if (object.size() > 0) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            DialogUIUtils.showToastCenter("此手机已被注册！");
                                        }
                                    });

                                }
                                //Snackbar.make(mBtnEqual, "查询成功：" + object.size(), Snackbar.LENGTH_LONG).show();
                            } else {
                                Log.e("BMOB", e.toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DialogUIUtils.showToastCenter("网络出错！请重试");
                                    }
                                });
                                //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                }



                break;
        }
    }

    public static boolean isMobile(String str) {
        Pattern p;
        Matcher m;
        boolean b = false;
        String s2 = "^[1](([3|5|8][\\d])|([4][5,6,7,8,9])|([6][5,6])|([7][3,4,5,6,7,8])|([9][8,9]))[\\d]{8}$";// 验证手机号
        p = Pattern.compile(s2);
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
