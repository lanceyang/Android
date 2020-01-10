package com.lance.library.push;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

public class AliPushUtils {


  public static void bindAccount(String account) {

    PushServiceFactory.getCloudPushService().bindAccount(account, new CommonCallback() {
      @Override
      public void onSuccess(String s) {
        Log.d(getClass().getSimpleName(),"bindAccount-onSuccess");
      }

      @Override
      public void onFailed(String s, String s1) {
        Log.d(getClass().getSimpleName(),"bindAccount-onFailed");
      }
    });
  }

  public static void unBindAccount() {
    PushServiceFactory.getCloudPushService().unbindAccount(new CommonCallback() {
      @Override
      public void onSuccess(String s) {
        Log.d(getClass().getSimpleName(),"unBindAccount-onSuccess");
      }

      @Override
      public void onFailed(String s, String s1) {
        Log.d(getClass().getSimpleName(),"unBindAccount-onFailed");
      }
    });
  }


  public static String getDeviceId(Context context) {
    return PushServiceFactory.getCloudPushService().getDeviceId();
  }
}
