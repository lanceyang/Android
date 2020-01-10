package com.lance.library.origin.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.lance.library.origin.config.CallConfig;
import com.lance.library.origin.config.FileConfig;
import com.lance.library.origin.config.PhotoConfig;
import com.lance.library.origin.config.SmsConfig;
import com.lance.library.origin.config.VideoConfig;

import pers.lance.file.activity.FilePickerActivity;

import static com.lance.library.origin.contants.PermissionCode.REQUEST_CODE_CAMERA_PERMISSION;
import static com.lance.library.origin.contants.PermissionCode.REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION;

public class OriginActivityStart {

    public static void startVideo(Activity activity, int requestCode, VideoConfig config) {

        if (activity == null)
            return;

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
                return;
            } else {
                Toast.makeText(activity, "无权限做此操作", Toast.LENGTH_SHORT);
                return;
            }
        }

        if (config == null)
            config = new VideoConfig();
        activity.startActivityForResult(
                config.buildIntent(), requestCode);
    }

    public static void startPhoto(Activity activity, int requestCode, PhotoConfig config) {

        if (activity == null)
            return;

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
                return;
            } else {
                Toast.makeText(activity, "无权限做此操作", Toast.LENGTH_SHORT);
                return;
            }
        }

        if (config == null)
            config = new PhotoConfig();

        activity.startActivityForResult(
                config.buildIntent(), requestCode);
    }

    public static void startCall(Activity activity, CallConfig config) {

        if (activity == null)
            return;

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CAMERA_PERMISSION);
                return;
            } else {
                Toast.makeText(activity, "无权限做此操作", Toast.LENGTH_SHORT);
                return;
            }
        }

        if (config == null)
            config = new CallConfig();

        activity.startActivity(
                config.buildIntent());
    }

    public static void startSms(Activity activity, SmsConfig config) {

        if (activity == null)
            return;

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE_CAMERA_PERMISSION);
                return;
            } else {
                Toast.makeText(activity, "无权限做此操作", Toast.LENGTH_SHORT);
                return;
            }
        }

        if (config == null)
            config = new SmsConfig();


        activity.startActivity(
                config.buildIntent());
    }

    public static void startFiles(Activity activity, int requestCode, FileConfig config) {

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION);
                return;
            } else {
                Toast.makeText(activity, "无权限做此操作,请去设置中修改权限", Toast.LENGTH_SHORT);
                return;
            }
        }


        FilePickerActivity.jumpHereForResult(activity, requestCode);

//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("application/msword");//设置类型，我这里是任意类型，任意后缀的可以这样写。
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        activity.startActivityForResult(intent, 1);
    }
}
