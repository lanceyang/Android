package com.lance.library.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lance.library.adapter.PickerAdapter;
import com.lance.library.origin.config.PhotoConfig;
import com.lance.library.origin.utils.OriginActivityStart;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pers.lance.base.handler.ExecHandler;

/**
 * Created by ZF_Develop_PC on 2017/11/7.
 */

public class PhotoPickerActivity extends AppCompatActivity implements PickerAdapter.OnItemClickListener {

    public final int REQUEST_CODE_CAMERA = 10001;

    public final int REQUEST_CODE_WRITE_PERMISSION = 10005;

    public final int REQUEST_CODE_CAMERA_PERMISSION = 10006;

    public final static String INTENT_KEY_MULTIPLE = "intent_key_multiple";

    public final static String INTENT_KEY_SELECTED_DATA = "intent_key_selected_data";

    private final String IMG_BASE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Tbh/";

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("_yyyyMMdd_HHmmss");

    private ExecHandler threadHandler;

    private RecyclerView recyclerView;

    private TextView title;

    private PickerAdapter adapter;

    private TextView rightBtn, leftBtn;

    private String currPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File baseFile = new File(IMG_BASE_PATH);
        if (!baseFile.exists())
            baseFile.mkdirs();

        int limitCount = getIntent().getIntExtra(INTENT_KEY_MULTIPLE, 1);
        adapter = new PickerAdapter(limitCount);

        setContentView(getResources().getIdentifier("activity_photo_picker", "layout", getPackageName()));
        initView();

        int drawableId = getResources().getIdentifier("btn_sure_selector", "drawable", getPackageName());
        rightBtn.setBackgroundResource(drawableId);
        rightBtn.setText("确定");
        leftBtn.setBackgroundResource(drawableId);
        leftBtn.setText("取消");

        title.setText("最多可选择" + limitCount + "张图片");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            try {
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, currPath);
                contentValues.put(MediaStore.Images.Media.DATE_ADDED, Calendar.getInstance().getTimeInMillis() / 1000);
                getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
            PickerHolder.PickerBean bean = new PickerHolder.PickerBean(currUri, currPath);
            adapter.addData(bean, 0);
        }
    }

    final List<PickerHolder.PickerBean> dataList = new ArrayList<>(0);

    private Runnable queryIPhotoRunnable = new Runnable() {
        @Override
        public void run() {

            dataList.clear();

            Cursor cursor = MediaStore.Images.Media.query(getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, MediaStore.Images.Media.DATE_ADDED);

            if (cursor != null) {
                boolean flag = cursor.moveToLast();

                while (flag) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                    Uri curUri;
                    File currFile = new File(path);
                    if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
                        curUri = FileProvider.getUriForFile(
                                PhotoPickerActivity.this, "com.zfzn.tuanbo.provider",
                                currFile);
                    } else {
                        curUri = Uri.fromFile(currFile);
                    }

                    dataList.add(new PickerHolder.PickerBean(curUri, path));
                    flag = cursor.moveToPrevious();
                }
                cursor.close();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.refreshData(dataList);
                    }
                });
            }
        }
    };

    private View.OnClickListener rightBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra(INTENT_KEY_SELECTED_DATA, adapter.getSelectedPaths());
            if (adapter.getSelectedPaths().length > 0)
                setResult(RESULT_OK, intent);
            finish();
        }
    };

    private void initWorkingThread() {
        HandlerThread thread = new HandlerThread("working");
        thread.start();
        threadHandler = new ExecHandler(thread.getLooper());
    }


    private void initView() {
        int rcvId = getResources().getIdentifier("activity_rcv", "id", getPackageName());
        recyclerView = (RecyclerView) findViewById(rcvId);

        int rightBtnId = getResources().getIdentifier("right_btn", "id", getPackageName());
        rightBtn = (TextView) findViewById(rightBtnId);
        findViewById(getResources().getIdentifier("right_btn_container", "id", getPackageName()))
                .setOnClickListener(rightBtnListener);

        int leftBtnId = getResources().getIdentifier("left_btn", "id", getPackageName());
        leftBtn = (TextView) findViewById(leftBtnId);
        findViewById(getResources().getIdentifier("left_btn_container", "id", getPackageName()))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        int titleId = getResources().getIdentifier("title", "id", getPackageName());
        title = (TextView) findViewById(titleId);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(this);

        initWorkingThread();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            threadHandler.post(queryIPhotoRunnable);
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10010);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 10010) {
            threadHandler.post(queryIPhotoRunnable);
        } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == REQUEST_CODE_WRITE_PERMISSION) {

        } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == REQUEST_CODE_CAMERA_PERMISSION) {

        }

    }

    private boolean requestWritePermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_PERMISSION);
                return true;
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        threadHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private Uri currUri;

    @Override
    public void onItemClick(View view, int position) {

        if (requestWritePermission())
            return;

        if (position == 0) {
            currPath = IMG_BASE_PATH + "IMG_" + simpleDateFormat.format(new Date()) + ".jpg";

            currUri = null;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                currUri = FileProvider.getUriForFile(
                        this, "com.zfzn.tuanbo.provider",
                        new File(currPath));
            } else {
                currUri = Uri.fromFile(new File(currPath));
            }


            OriginActivityStart.startPhoto(this,REQUEST_CODE_CAMERA,new PhotoConfig().setOutput(currUri));
        } else {

            ShowPicActivity.jumpHere(this, dataList.get(position - 1).getPath());
        }

    }

    public static void jumpHereForResult(Activity context, int maxNum, int requestCode) {
        Intent intent = new Intent(context, PhotoPickerActivity.class);
        intent.putExtra(INTENT_KEY_MULTIPLE, maxNum < 1 ? 1 : maxNum);
        context.startActivityForResult(intent, requestCode);
    }

}
