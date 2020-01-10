package pers.lance.base.handler;

import java.lang.ref.WeakReference;

public class HandlerWrapRunnable implements Runnable {

    private WeakReference<Runnable> backRun;

    public HandlerWrapRunnable(Runnable backRun) {
        this.backRun = new WeakReference<Runnable>(backRun);
    }

    @Override
    public void run() {
        Runnable runnable;
        if (backRun != null && (runnable = backRun.get()) != null)
            runnable.run();
    }
}
