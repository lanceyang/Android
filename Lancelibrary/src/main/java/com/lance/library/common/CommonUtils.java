package com.lance.library.common;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import java.lang.reflect.Method;

import static android.text.TextUtils.isEmpty;

/**
 * Created by ZF_Develop_PC on 2018/1/24.
 */

public class CommonUtils {

  //获取虚拟按键的高度
  public static int getNavigationBarHeight(Context context) {
    int result = 0;
    if (hasNavBar(context)) {
      Resources res = context.getResources();
      int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
      if (resourceId > 0) {
        result = res.getDimensionPixelSize(resourceId);
      }
    }
    return result;
  }

  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  public static boolean hasNavBar(Context context) {
    Resources res = context.getResources();
    int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
    if (resourceId != 0) {
      boolean hasNav = res.getBoolean(resourceId);
      // check override flag
      String sNavBarOverride = getNavBarOverride();
      if ("1".equals(sNavBarOverride)) {
        hasNav = false;
      } else if ("0".equals(sNavBarOverride)) {
        hasNav = true;
      }
      return hasNav;
    } else { // fallback
      return !ViewConfiguration.get(context).hasPermanentMenuKey();
    }
  }

  private static String getNavBarOverride() {
    String sNavBarOverride = null;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      try {
        Class c = Class.forName("android.os.SystemProperties");
        Method m = c.getDeclaredMethod("get", String.class);
        m.setAccessible(true);
        sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
      } catch (Throwable e) {
      }
    }
    return sNavBarOverride;
  }

  public static int getHasVirtualKey(WindowManager windowManager) {
    int dpi = 0;
    Display display = windowManager.getDefaultDisplay();
    DisplayMetrics dm = new DisplayMetrics();
    @SuppressWarnings("rawtypes")
    Class c;
    try {
      c = Class.forName("android.view.Display");
      @SuppressWarnings("unchecked")
      Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
      method.invoke(display, dm);
      dpi = dm.heightPixels;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dpi;
  }

  private static final String LOCAL_FILE_NAME = "local_file";

  public static void setSharedPerferencesData(Context context, String key, String value) {
    SharedPreferences sharedPreferences = context.getApplicationContext()
      .getSharedPreferences(LOCAL_FILE_NAME, Context.MODE_PRIVATE);
    sharedPreferences.edit().putString(key, value).commit();
  }

  public static void setSharedPerferencesDataBoolean(Context context, String key, boolean value) {
    SharedPreferences sharedPreferences = context.getApplicationContext()
      .getSharedPreferences(LOCAL_FILE_NAME, Context.MODE_PRIVATE);
    sharedPreferences.edit().putBoolean(key, value).commit();
  }

  public static String getSharedPerferencesString(Context context, String key) {
    SharedPreferences sharedPreferences = context.getApplicationContext()
      .getSharedPreferences(LOCAL_FILE_NAME, Context.MODE_PRIVATE);
    return sharedPreferences.getString(key, "");
  }

  public static boolean getSharedPerferencesBoolean(Context context, String key) {
    SharedPreferences sharedPreferences = context.getApplicationContext()
      .getSharedPreferences(LOCAL_FILE_NAME, Context.MODE_PRIVATE);
    return sharedPreferences.getBoolean(key, false);
  }

  public static String getDeviceId(Activity context) {
    //IMEI（imei）
    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_PHONE_STATE}, 0x10001);
      return null;
    }

    String imei = tm.getDeviceId();
    if (!isEmpty(imei)) {
      return imei;
    }
    return imei;
  }

}
