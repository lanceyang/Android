package com.lance.library.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lance.library.httpservice.HttpBaseBean;
import com.lance.library.httpservice.HttpClient;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

/**
 * Created by Tjcx on 2017/8/29.
 */

public class BasicActivity extends AppCompatActivity {

    /**
     * 用于存储一开始的网络请求的Disposable对象集合
     */
    private Queue<Disposable> obQueue = new ArrayDeque<>(0);

    /**
     * 发送post请求
     *
     * @param url      请求地址
     * @param params   参数集合
     * @param callback 回调对象
     */
    protected void doPost(String url, Map<String, Object> params, DecorateCallback callback) {
        HttpClient.getInstance().doPost(url, params, callback);
    }

    /**
     * 发送post请求
     *
     * @param url      请求地址
     * @param params   参数集合
     * @param callback 回调对象
     */
    protected void doPost(String url, RequestBody params, DecorateCallback callback) {
        HttpClient.getInstance().doPost(url, params, callback);
    }

    /**
     * 将所有未完成的请求取消
     */
    @Override
    protected void onDestroy() {
        Disposable disposable = obQueue.poll();
        while (disposable != null) {
            if (!disposable.isDisposed())
                disposable.dispose();
            disposable = obQueue.poll();
        }
        setContentView(new View(this));
        super.onDestroy();
    }

    /**
     * 用于监控activity是否被释放掉
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.d(getClass().getSimpleName(), "------finalize------");
    }

    protected void initStatusBar() {
        Window mWindow = getWindow();
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//		mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams mWindowAttributes = mWindow.getAttributes();
        mWindowAttributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowAttributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        mWindow.setAttributes(mWindowAttributes);
    }

    /**
     * 网络请求的代理类
     */
    protected abstract class DecorateCallback implements HttpClient.Callback {

        protected abstract void onRequestStar();

        protected abstract void onReqestSuccess(HttpBaseBean result);

        protected abstract void onRequestComplete();

        protected abstract void onReqestError(Throwable throwable);

        /**
         * 请求开始回调
         *
         * @param disposable 用于取消该请求
         */
        @Override
        public void onStart(Disposable disposable) {
            obQueue.add(disposable);
            onRequestStar();
        }

        /**
         * 请求成功
         *
         * @param result 请求结果
         */
        @Override
        public void onSuccess(final HttpBaseBean result) {

            Log.d("DecorateCallback", getClass().getSimpleName() + "--onSuccess");
            onReqestSuccess(result);
        }

        /**
         * 请求发生错误
         *
         * @param throwable 错误
         */
        @Override
        public void onError(Throwable throwable) {
            if (throwable.getMessage() == null)
                return;
            Log.e(BasicActivity.this.getClass().getSimpleName(), throwable.getMessage());
            onReqestError(throwable);
        }

        /**
         * 请求完成
         */
        @Override
        public void onComplete() {
            onRequestComplete();
        }
    }
}
