package pers.lance.base.handler;

import android.os.Handler;
import android.os.Looper;


public class LanceHandler extends Handler {

    public LanceHandler() {
    }

    public LanceHandler(Callback callback) {
        super(callback);
    }

    public LanceHandler(Looper looper) {
        super(looper);
    }

    public LanceHandler(Callback callback, Looper looper) {
        super(looper, callback);
    }
}
