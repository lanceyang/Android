package com.lance.library.fragment;

import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;


import com.lance.library.httpservice.HttpBaseBean;
import com.lance.library.httpservice.HttpClient;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

import io.reactivex.disposables.Disposable;

/**
 * Created by Tjcx on 2017/9/20.
 */

public abstract class BasicFragment extends Fragment {

    private Queue<Disposable> obQueue = new ArrayDeque<>(0);

    protected void doPost(String url, Map<String, Object> params, DecorateCallback callback) {
        Log.e("doPost--------", "url:" + url + "code：" + params.get("code"));
        HttpClient.getInstance().doPost(url, params, callback);
    }

    @Override
    public void onDestroyView() {
        Disposable disposable = obQueue.poll();
        while (disposable != null) {
            if (!disposable.isDisposed())
                disposable.dispose();
            disposable = obQueue.poll();
        }
        super.onDestroyView();
    }

    protected abstract class DecorateCallback implements HttpClient.Callback {

        protected abstract void onRequestStar();

        protected abstract void onReqestSuccess(HttpBaseBean result);

        protected abstract void onRequestComplete();

        protected abstract void onReqestError(Throwable throwable);

        @Override
        public void onStart(Disposable disposable) {
            obQueue.add(disposable);
            onRequestStar();
        }

        @Override
        public void onSuccess(final HttpBaseBean result) {

            Log.d("DecorateCallback", getClass().getSimpleName() + "--onSuccess");
            onReqestSuccess(result);
        }

        @Override
        public void onError(Throwable throwable) {
            Log.e(BasicFragment.this.getClass().getSimpleName(), TextUtils.isEmpty(throwable.getMessage()) ? "" : throwable.getMessage());
            onReqestError(throwable);
        }

        @Override
        public void onComplete() {
            onRequestComplete();
        }
    }
}
