package com.umeng.soexample;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            System.out.println("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            System.out.println("分享出错:" + throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            System.out.println("分享取消！");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mButton = findViewById(R.id.share);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMyPermission();
            }
        });
    }

    private void setUMShare() {
        System.out.println("准备分享");
        UMImage image = new UMImage(MainActivity.this, R.drawable.logo);
        //UMImage thumb = new UMImage(MainActivity.this,R.mipmap.ic_launcher);//略缩图
        //image.setThumb(thumb);
        new ShareAction(MainActivity.this)
                .setDisplayList(SHARE_MEDIA.QQ
                        , SHARE_MEDIA.SINA
                        , SHARE_MEDIA.QZONE
                        , SHARE_MEDIA.WEIXIN
                        , SHARE_MEDIA.WEIXIN_CIRCLE)
                .withText("hello")
                .withMedias(image)
                .setCallback(umShareListener)
                .open();
    }

    /**
     * 检查权限
     */
    @AfterPermissionGranted(123)
    private void checkMyPermission() {
        String[] perms = new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(MainActivity.this, perms)) {
            setUMShare();//调用分享
        } else {
            EasyPermissions.requestPermissions(MainActivity.this
                    , "需要相应的权限", 123, perms);
        }
    }

    /**
     * 检查权限后的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode
            , @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);//分享到QQ和新浪回调处理，必须
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            Toast.makeText(this, "用户从系统权限界面返回", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        System.out.println("权限通过");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        System.out.println("权限被拒绝");
        //引导用户跳到系统权限授权界面
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
