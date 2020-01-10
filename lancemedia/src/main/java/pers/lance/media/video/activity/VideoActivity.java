package pers.lance.media.video.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import pers.lance.media.R;
import pers.lance.media.video.utils.MyMediaSystem;

public class VideoActivity extends AppCompatActivity {

    private static final String INTENT_KEY_URL = "video_url";

    public JzvdStd video;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video);
        video = findViewById(R.id.video);

        String url = getIntent().getStringExtra(INTENT_KEY_URL);

        if (TextUtils.isEmpty(url)) {
            Toast.makeText(this, "无效的播放地址", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        video.setUp(new JZDataSource(url, ""), Jzvd.SCREEN_NORMAL, MyMediaSystem.class);
        video.startButton.performClick();
    }


    @Override
    public void onBackPressed() {
        if (Jzvd.backPress())
            return;
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    public static void jumpHere(Context context, String url) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(INTENT_KEY_URL, url);
        context.startActivity(intent);
    }
}
