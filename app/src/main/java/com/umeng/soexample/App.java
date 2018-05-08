package com.umeng.soexample;

import android.app.Application;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.init(this
                , "59892f08310c9307b60023d0"
                , "Umeng"
                , UMConfigure.DEVICE_TYPE_PHONE
                , "");

        UMConfigure.setLogEnabled(true);
        UMShareAPI.get(this);

        //需要去微信开放平台注册，拿到 App key 和 App Secret
        PlatformConfig.setWeixin("wxdc1e388c3822c80b"
                , "3baf1193c85774b3fd9d18447d76cab0");

        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad"
                , "http://sns.whalecloud.com");//需要去新浪开发平台注册拿到 App key 和 App Secret

        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");//目前暂时不用（建议也去单独整一个）
    }
}
