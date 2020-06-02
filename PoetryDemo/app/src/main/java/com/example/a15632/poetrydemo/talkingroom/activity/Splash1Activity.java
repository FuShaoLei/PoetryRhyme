package com.example.a15632.poetrydemo.talkingroom.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.a15632.poetrydemo.R;
import com.example.a15632.poetrydemo.talkingroom.util.LogUtil;
import com.example.a15632.poetrydemo.talkingroom.widget.SetPermissionDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/*import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.a15632.poetrydemo.R;
import com.example.a15632.poetrydemo.talkingroom.util.LogUtil;
import com.example.a15632.poetrydemo.talkingroom.widget.SetPermissionDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;*/
public class Splash1Activity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//去掉顶部标题栏
        setContentView(R.layout.talkingroom_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestPermisson();
            }
        }, 100);
        LogUtil.d(new String(Character.toChars(0x1F60E)));
    }


    private void requestPermisson(){
        RxPermissions rxPermission = new RxPermissions(this);

        rxPermission
                .request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储权限
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                )
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            startActivity(new Intent(Splash1Activity.this,Chat1Activity.class));
                            finish();
                         } else {

                            SetPermissionDialog mSetPermissionDialog = new SetPermissionDialog(Splash1Activity.this);
                            mSetPermissionDialog.show();
                            mSetPermissionDialog.setConfirmCancelListener(new SetPermissionDialog.OnConfirmCancelClickListener() {
                                @Override
                                public void onLeftClick() {

                                    finish();
                                }

                                @Override
                                public void onRightClick() {

                                     finish();
                                }
                            });
                        }
                    }
                });
    }

}
