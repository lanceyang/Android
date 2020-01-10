package com.lance.library.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lance.library.R;

/**
 * Created by ZF_Develop_PC on 2018/9/15.
 */

public class ShowPicActivity extends AppCompatActivity {

    public static final String DATA_PIC_URL = "dataPicUrl";

    private ImageView image;

    private ProgressBar pb;

    public static final int SYSTEM_UI_FLAG = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);

        pb = findViewById(R.id.progress);

        image = findViewById(R.id.image);
        String path = getIntent().getStringExtra(DATA_PIC_URL);

        if (TextUtils.isEmpty(path)) {
            Toast.makeText(this, "未获得到正确的图片路径！", Toast.LENGTH_SHORT).show();
            finish();
        }

        Log.e(this.getClass().getSimpleName(), path);

        Glide.with(this).asBitmap().listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                pb.setVisibility(View.GONE);
                Toast.makeText(ShowPicActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                pb.setVisibility(View.GONE);
                return false;
            }
        }).load(path).into(image);

//    Glide.with(image).asBitmap().load(path).listener(new RequestListener<Bitmap>() {
//      @Override
//      public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//        pb.setVisibility(View.GONE);
//        Toast.makeText(ShowPicActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        return true;
//      }
//
//      @Override
//      public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//        pb.setVisibility(View.GONE);
//        image.setImageBitmap(resource);
//        return true;
//      }
//    });
    }

    @Override
    protected void onStart() {
        super.onStart();

        getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    public void onRootClick(View v) {
        onBackPressed();
    }

    public static void jumpHere(Context context, String dataUrl) {
        Intent intent = new Intent(context, ShowPicActivity.class);
        intent.putExtra(ShowPicActivity.DATA_PIC_URL, dataUrl);
        context.startActivity(intent);
    }
}
