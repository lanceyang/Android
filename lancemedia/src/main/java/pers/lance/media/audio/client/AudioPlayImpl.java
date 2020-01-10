package pers.lance.media.audio.client;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;

import pers.lance.base.handler.ExecHandler;
import pers.lance.media.audio.base.AudioPlayState;

public class AudioPlayImpl implements AudioPlay, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private final String TAG = getClass().getSimpleName();

    private MediaPlayer mediaPlayer;

    private WeakReference<Context> context;

    private ExecHandler handler;

    private static AudioPlayImpl instance;

    private AudioManager audioManager;

    private AudioPlayState playState = AudioPlayState.PREPARE;

    private EndListener endListener;

    private AudioPlayImpl(Context context) {

        this.context = new WeakReference<>(context);
        HandlerThread thread = new HandlerThread("AudioPlay");
        thread.start();
        handler = new ExecHandler(thread.getLooper());
        audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    }

    public static AudioPlayImpl getInstance(Context context) {
        if (instance == null) {
            instance = new AudioPlayImpl(context);
        } else {
            instance.setContext(context);
        }
        return instance;
    }

    private void setContext(Context context) {
        this.context = new WeakReference<>(context);
    }

    public EndListener getEndListener() {
        return endListener;
    }

    public AudioPlayImpl setEndListener(EndListener endListener) {
        this.endListener = endListener;
        return this;
    }

    public void start(String url) {

        if (TextUtils.isEmpty(url)) {
            Toast.makeText(context.get(), "播放路径错误！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (playState == AudioPlayState.START)
            stop();

        audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        handler.post(new Runnable() {
            @Override
            public void run() {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnCompletionListener(AudioPlayImpl.this);
                mediaPlayer.setOnPreparedListener(AudioPlayImpl.this);

                try {
                    mediaPlayer.setDataSource(context.get(), Uri.parse(url));
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context.get(), "播放录音失败，请重试", Toast.LENGTH_SHORT).show();
                }

                try {
                    mediaPlayer.prepare();
                    playState = AudioPlayState.PREPARE;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void stop() {
        handler.removeCallbacksAndMessages(null);

        if (mediaPlayer != null && playState == AudioPlayState.START) {
            mediaPlayer.stop();
            mediaPlayer.release();
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
        playState = AudioPlayState.REALEASE;

        if (endListener != null) {
            endListener.onEnd();
            endListener = null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mediaPlayer.release();
        audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        playState = AudioPlayState.REALEASE;
        if (endListener != null) {
            endListener.onEnd();
            endListener = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playState = AudioPlayState.START;
        mp.start();
    }

    public AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {//是否新建个class，代码更规矩，并且变量的位置也很尴尬
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    stop();
                    Log.d(AudioPlayImpl.this.TAG, "AUDIOFOCUS_LOSS [" + this.hashCode() + "]");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    stop();
                    Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT [" + this.hashCode() + "]");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    break;
            }
        }
    };

    public interface EndListener {

        void onEnd();

    }
}
