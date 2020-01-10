package pers.lance.base.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

public class ExecHandler implements Handler.Callback {

    private final LanceHandler mHandler;

    private final WeakReference<Handler.Callback> mCallbak;



    public ExecHandler() {
        mCallbak = null;
        this.mHandler = new LanceHandler(this);
    }

    public ExecHandler(Handler.Callback callback) {
        mCallbak = new WeakReference<>(callback);
        mHandler = new LanceHandler(this);
    }

    public ExecHandler(Handler.Callback callback, Looper looper) {
        mCallbak = new WeakReference<>(callback);
        mHandler = new LanceHandler(this);
    }

    public ExecHandler(Looper looper) {
        mCallbak = null;
        mHandler = new LanceHandler(looper);
    }


    @Override
    public boolean handleMessage(Message msg) {
        Handler.Callback callback;
        if (mCallbak != null && (callback = mCallbak.get()) != null)
            return callback.handleMessage(msg);
        return false;
    }

    public void post(Runnable runnable) {
        mHandler.post(new HandlerWrapRunnable(runnable));
    }

    public void postDelayed(Runnable runnable, long delayMillis) {
        mHandler.postDelayed(new HandlerWrapRunnable(runnable), delayMillis);
    }

    public void removeCallbacksAndMessages(Object token) {
        mHandler.removeCallbacksAndMessages(token);
    }
}
