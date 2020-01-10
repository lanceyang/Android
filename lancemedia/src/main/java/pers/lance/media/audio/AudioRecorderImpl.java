package pers.lance.media.audio;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import pers.lance.base.handler.ExecHandler;
import pers.lance.media.audio.base.AudioRecordState;
import pers.lance.media.audio.client.AudioRecorder;
import pers.lance.media.base.ResourcePo;
import pers.lance.media.base.ResourceType;

import static android.media.MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED;

public class AudioRecorderImpl implements AudioRecorder, MediaRecorder.OnInfoListener, MediaRecorder.OnErrorListener {

    private MediaRecorder record;

    private ExecHandler handler;

    private final WeakReference<Context> context;

    private int duration = 30 * 1000;

    private int mSampleRateInHz = 44100;

    private Callback callback;

    private AudioRecordState status;

    public AudioRecorderImpl(Context context) {

        status = AudioRecordState.PREPARE;
        this.context = new WeakReference<>(context);
        HandlerThread thread = new HandlerThread("audio");
        thread.start();
        handler = new ExecHandler(thread.getLooper());

    }

    private void init() {
        record = new MediaRecorder();
        record.setAudioChannels(1);
        record.setAudioSource(MediaRecorder.AudioSource.MIC);
        record.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        record.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        record.setAudioSamplingRate(mSampleRateInHz);
        record.setAudioEncodingBitRate(16 * mSampleRateInHz * 1 / 8);
        record.setMaxDuration(duration);
        record.setOnInfoListener(this);
        record.setOnErrorListener(this);
    }

    Uri currUri;
    File currFile;

    public void start() {

        if (ContextCompat.checkSelfPermission(context.get(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context.get(), new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            return;
        }

        if (ContextCompat.checkSelfPermission(context.get(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context.get(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }


        if (ContextCompat.checkSelfPermission(context.get(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context.get(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }

        handler.post(new RecordRunnable());
    }

    public void stop() {
        if (status == AudioRecordState.START) {
            status = AudioRecordState.FINISH;
            record.stop();
            record.release();

            finish();
        }

    }

    private void finish() {
        if (callback != null) {
            ResourcePo resourcePo = new ResourcePo();
            resourcePo.setLocalPath(currFile.getAbsolutePath());
            resourcePo.setContent(currUri == null ? "" : currUri.toString());
            resourcePo.setType(ResourceType.AUDIO);
            resourcePo.setName(currFile.getName());
            callback.onSuccess(resourcePo);
        }
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        if (what == MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
            status = AudioRecordState.FINISH;
            finish();
        }
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        Log.e("onRecorderError-------", what + "--------" + extra);
        status = AudioRecordState.ERROR;
        mr.stop();
        mr.release();
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void onSuccess(ResourcePo resource);

        void onError();
    }

    private class RecordRunnable implements Runnable {

        @Override
        public void run() {
            if (record == null) {
                init();
            }

            if (status == AudioRecordState.START) {
                Toast.makeText(context.get(), "该录音机已处于录音状态", Toast.LENGTH_SHORT).show();
                return;
            }

            String currPath = Environment.getExternalStoragePublicDirectory("tb/audio").getAbsolutePath() + "/audio_" + System.currentTimeMillis() + ".acc";
            currUri = null;
            currFile = new File(currPath);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                record.setOutputFile(currFile);
            } else {
                record.setOutputFile(currPath);
            }

            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
                currUri = FileProvider.getUriForFile(
                        context.get(), "com.zfzn.tuanbo.provider",
                        currFile);
            } else {
                currUri = Uri.fromFile(currFile);
            }

            if (!currFile.getParentFile().exists()) {
                currFile.getParentFile().mkdirs();
            }

            try {
                currFile.createNewFile();
            } catch (IOException e) {
                status = AudioRecordState.ERROR;
                e.printStackTrace();
                Toast.makeText(context.get(), "创建音频文件失败", Toast.LENGTH_SHORT).show();
                if (callback != null)
                    callback.onError();
                return;
            }

            try {
                record.prepare();
            } catch (IOException e) {
                status = AudioRecordState.ERROR;
                e.printStackTrace();
                Toast.makeText(context.get(), "初始化失败", Toast.LENGTH_SHORT).show();
                record = null;
                if (callback != null)
                    callback.onError();
                return;
            }

            if (status == AudioRecordState.PREPARE) {
                record.start();
                status = AudioRecordState.START;
            }
        }
    }
}
