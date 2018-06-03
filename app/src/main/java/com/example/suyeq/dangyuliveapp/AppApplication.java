package com.example.suyeq.dangyuliveapp;

import android.app.Application;
import android.content.Context;

import com.example.suyeq.dangyuliveapp.editprofile.CustomProfile;
import com.example.suyeq.dangyuliveapp.utils.QnUploadHelper;
import com.tencent.TIMManager;
import com.tencent.TIMUserProfile;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Suyeq on 2018/5/16.
 */

public class AppApplication extends Application {
    private static AppApplication app;
    private static Context appContext;
    private ILVLiveConfig mLiveConfig;

    private TIMUserProfile mSelfProfile;

    @Override
    public void onCreate(){
        super.onCreate();
        //initLiveSdk();
        app = this;
        appContext = getApplicationContext();
        ILiveSDK.getInstance().initSdk(getApplicationContext(), 1400093531, 27005);
        List<String> customInfos = new ArrayList<String>();
       // customInfos.add(CustomProfile.CUSTOM_GET);
        //customInfos.add(CustomProfile.CUSTOM_SEND);
       // customInfos.add(CustomProfile.CUSTOM_LEVEL);
       // customInfos.add(CustomProfile.CUSTOM_RENZHENG);
        //初始化直播SDK
        TIMManager.getInstance().initFriendshipSettings(CustomProfile.allBaseInfo, customInfos);
        mLiveConfig = new ILVLiveConfig();
        ILVLiveManager.getInstance().init(mLiveConfig);
        QnUploadHelper.init("gLPPLNjvsOvHSuT0R3n5d449kRBpswWbPcJMqKKL",
                "OwhXxj1-a72WZoVanyqCjH0Zs9AtmiJW2nKIceLr",
                "http://p989c84ev.bkt.clouddn.com/",
                "suyeq");

       // LeakCanary.install(this);
    }

    //private void initLiveSdk() {

   // }
    public static AppApplication getApplication() {
        return app;
    }
    public static Context getContext() {
        return appContext;
    }
    public void setSelfProfile(TIMUserProfile userProfile) {
        mSelfProfile = userProfile;
    }

    public TIMUserProfile getSelfProfile() {
        return mSelfProfile;
    }

    public ILVLiveConfig getLiveConfig() {
        return mLiveConfig;
    }
}
