package com.lance.library.application;

import androidx.multidex.MultiDexApplication;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.lance.library.R;
import com.lance.library.common.CommonUtils;
import com.lance.library.custom.Constant;
import com.lance.library.push.AliPushUtils;
import com.lance.library.push.NotificationManager;

public class BaseApplication extends MultiDexApplication {

    private final String TAG = getClass().getSimpleName();

    protected CloudPushService pushService;

    private static Context appContext;

    public BaseApplication() {
        if (appContext == null)
            appContext = this;
    }

    protected void initCloudChannel() {

        NotificationManager.getInstance(this);
        PushServiceFactory.init(this);
        pushService = PushServiceFactory.getCloudPushService();
        pushService.setNotificationSmallIcon(R.mipmap.small_icon);

        pushService.register(this, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                String account = CommonUtils.getSharedPerferencesString(BaseApplication.this, Constant.YOUR_ARE_MY_ONLY_ONE);
                if (!TextUtils.isEmpty(account)) {
                    AliPushUtils.bindAccount(account);
                }
                Log.d(TAG, "init cloudchannel success");
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }


    public String getDeviceId() {
        return pushService.getDeviceId();
    }


    public static Context getAppContext() {
        return appContext;
    }
}
