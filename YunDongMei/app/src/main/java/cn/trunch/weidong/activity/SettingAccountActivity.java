package cn.trunch.weidong.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.runtime.Permission;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.trunch.weidong.MyApplication;
import cn.trunch.weidong.R;
import cn.trunch.weidong.entity.UserBody;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.http.UploadApi;
import cn.trunch.weidong.util.Glide4Engine;
import cn.trunch.weidong.util.PathUtil;
import cn.trunch.weidong.util.StatusBarUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SettingAccountActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CODE = 1;

    private ImageView settingAccountBackBtn;
    private TextView settingAccountSaveBtn;
    private ImageView settingAccountAvatar;
    private TextView settingAccountName;
    private TextView settingAccountGender;
    private TextView settingAccountIntro;
    private TextView settingAccountBirth;
    private TextView settingAccountTall;
    private TextView settingAccountWeight;
    private TextView settingAccountVital;
    private List<Uri> uriSelected;
    private String avatarName = "";
    private UserEntity user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account);
        StatusBarUtil.setWindowStatusBarColor(this, R.color.colorPrimary);

        init();
        initListener();
        initData();
        DialogUIUtils.init(this);
    }

    private void initData() {
        user = MyApplication.getInstance().getUserEntity();
        avatarName = user.getuAvatar();
        Glide.with(SettingAccountActivity.this)
                .load(avatarName)
                .apply(bitmapTransform(new CircleCrop()))
                .error(R.drawable.default_head)
                .into(settingAccountAvatar);
        settingAccountName.setText(user.getuNickname());
        settingAccountGender.setText((user.getuGender() == 1) ? ("???") : ("???"));
        settingAccountIntro.setText(user.getuSelfdes());
        settingAccountBirth.setText(user.getuBirthday());
        settingAccountTall.setText(String.valueOf(user.getuHeight()));
        settingAccountWeight.setText(String.valueOf(user.getuWeight()));
        settingAccountVital.setText(String.valueOf(user.getuVialCap()));
       /* //????????????
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        new UniteApi(ApiUtil.USER_INFO, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                Gson gson = new Gson();
                UniteApi u = (UniteApi) api;
                List<UserEntity> uSchool = gson.fromJson(u.getJsonData().toString(), new TypeToken<List<UserEntity>>() {
                }.getType());
                UserEntity user = uSchool.get(0);
                Glide.with(SettingAccountActivity.this)
                        .load(user.getuAvatar())
                        .apply(bitmapTransform(new CircleCrop()))
                        .into(settingAccountAvatar);
                settingAccountName.setText(user.getuNickname());
                settingAccountGender.setText((user.getuGender() == 1) ? ("???") : ("???"));
                settingAccountIntro.setText(user.getuSelfdes());
                settingAccountBirth.setText(user.getuBirthday());
            }

            @Override
            public void failure(Api api) {

            }
        });*/
       /* new UniteApi(ApiUtil.USER_BODY_INFO, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                Gson gson = new Gson();
                UniteApi u = (UniteApi) api;
                List<UserBody> uSchool = gson.fromJson(u.getJsonData().toString(), new TypeToken<List<UserBody>>() {
                }.getType());
                UserBody user = uSchool.get(0);
                settingAccountTall.setText(String.valueOf(user.getuHeight()));
                settingAccountWeight.setText(String.valueOf(user.getuWeight()));
                settingAccountVital.setText(String.valueOf(user.getuVialCap()));
            }

            @Override
            public void failure(Api api) {

            }
        });*/

    }

    private void init() {
        settingAccountBackBtn = findViewById(R.id.settingAccountBackBtn);
        settingAccountSaveBtn = findViewById(R.id.settingAccountSaveBtn);
        settingAccountAvatar = findViewById(R.id.settingAccountAvatar);
        settingAccountName = findViewById(R.id.settingAccountName);
        settingAccountGender = findViewById(R.id.settingAccountGender);
        settingAccountIntro = findViewById(R.id.settingAccountIntro);
        settingAccountBirth = findViewById(R.id.settingAccountBirth);
        settingAccountTall = findViewById(R.id.settingAccountTall);
        settingAccountWeight = findViewById(R.id.settingAccountWeight);
        settingAccountVital = findViewById(R.id.settingAccountVital);
    }

    private void initListener() {
        settingAccountBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        settingAccountAvatar.setOnClickListener(v -> AndPermission.with(SettingAccountActivity.this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        Matisse.from(SettingAccountActivity.this)
                                .choose(MimeType.ofImage()) // ?????? mime ?????????
                                .countable(true)
                                .maxSelectable(1) // ???????????????????????????
                                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                .thumbnailScale(0.85f) // ??????????????????
                                .imageEngine(new Glide4Engine()) // ???????????????????????????
                                .forResult(REQUEST_IMAGE_CODE); // ??????????????????????????????
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        DialogUIUtils.showToastCenter("???????????????????????????");
                    }
                })
                .start());
        settingAccountSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    private void update() {


        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("head", avatarName);
        hm.put("nickName", settingAccountName.getText().toString());
        hm.put("selfdes", settingAccountIntro.getText().toString());
        hm.put("gender", String.valueOf(((settingAccountGender.getText().toString()).equals("???") ? 0 : 1)));
        hm.put("birthday", settingAccountBirth.getText().toString());
        hm.put("height", settingAccountTall.getText().toString());
        hm.put("weight", settingAccountWeight.getText().toString());
        hm.put("vialCap", settingAccountVital.getText().toString());
        user.setuAvatar(avatarName);
        user.setuNickname(settingAccountName.getText().toString());
        user.setuSelfdes(settingAccountIntro.getText().toString());
        user.setuGender(((settingAccountGender.getText().toString()).equals("???") ? 0 : 1));
        user.setuBirthday(settingAccountBirth.getText().toString());
        if (!TextUtils.isEmpty(settingAccountTall.getText().toString()))
            user.setuHeight(String.valueOf(Integer.parseInt(settingAccountTall.getText().toString().trim())));
        if (!TextUtils.isEmpty(settingAccountWeight.getText().toString()))
            user.setuWeight(String.valueOf(Integer.parseInt(settingAccountWeight.getText().toString().trim())));
        if (!TextUtils.isEmpty(settingAccountVital.getText().toString()))
            user.setuVialCap(String.valueOf(Integer.parseInt(settingAccountVital.getText().toString().trim())));

        user.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                runOnUiThread(() -> {
                    if (e == null){
                        MyApplication.getInstance().setUserEntity(user);
                        DialogUIUtils.showToastCenter("??????????????????");
                        finish();
                    }else {
                        DialogUIUtils.showToastCenter("??????????????????????????????");
                    }
                });
            }
        });
       /* new UniteApi(ApiUtil.USER_UPDATE, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                SharedPreferences.Editor editor = getSharedPreferences("wd", MODE_PRIVATE).edit();
                ApiUtil.USER_NAME = settingAccountName.getText().toString();
                editor.putString("userName", settingAccountName.getText().toString());
                if (!"".equals(avatarName)) {
                    ApiUtil.USER_AVATAR = "http://www.two2two.xyz/werunImg/head/" + avatarName;
                    editor.putString("userAvatar", "http://www.two2two.xyz/werunImg/head/" + avatarName);
                }
                editor.apply();
                DialogUIUtils.showToastCenter("??????????????????");
                finish();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("??????????????????????????????");
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK) {
            assert data != null;
            uriSelected = Matisse.obtainResult(data);
            BmobFile bmobFile = new BmobFile(new File(PathUtil.getFilePath(this, uriSelected.get(0))));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUrl()--????????????????????????????????????
                        //toast("??????????????????:" + bmobFile.getFileUrl());
                        avatarName = bmobFile.getFileUrl();

                        DialogUIUtils.showToastCenter("??????????????????");
                    } else {
                        DialogUIUtils.showToastCenter("????????????????????????");
                    }

                }

                @Override
                public void onProgress(Integer value) {
                    // ????????????????????????????????????
                }
            });

            /*new UploadApi(ApiUtil.FILE_UPLOAD_HEAD, PathUtil.getFilePath(this, uriSelected.get(0))).upload(new ApiListener() {
                @Override
                public void success(Api api) {
                    UploadApi u = (UploadApi) api;
                    Gson gson = new Gson();
                    List<String> imgs = gson.fromJson(u.getJsonData().toString(), new TypeToken<List<String>>() {
                    }.getType());
                    avatarName = imgs.get(0);
                    DialogUIUtils.showToastCenter("??????????????????");
                }

                @Override
                public void failure(Api api) {
                    DialogUIUtils.showToastCenter("????????????????????????");
                }
            });*/
            Glide.with(this)
                    .load(uriSelected.get(0))
                    .apply(bitmapTransform(new CircleCrop()))
                    .into(settingAccountAvatar);
        }
    }
}
