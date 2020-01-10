package com.lance.library.httpservice;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.reflect.TypeToken;
import com.lance.library.application.BaseApplication;
import com.lance.library.common.CommonUtils;
import com.lance.library.custom.Constant;
import com.lance.library.utils.MobCookieManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tjcx on 2017/9/15.
 */

public class HttpClient {

    private Retrofit mRetrofit;

    private OkHttpClient mClient;

    public HttpClient() {

        mRetrofit = new Retrofit.Builder().client(createClient()).baseUrl(Constant.BASE_URL)
                .addConverterFactory(initGsonFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private OkHttpClient createClient() {
        mClient = new OkHttpClient.Builder().connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder requestBuilder = chain.request().newBuilder();
                        requestBuilder.addHeader("Content-Type", "application/json; charset=UTF-8");
                        String cookie = MobCookieManager.getCookie(Constant.BASE_URL);
                        String ticket = CommonUtils.getSharedPerferencesString(BaseApplication.getAppContext(), Constant.YOUR_ARE_MY_ONLY_ONE);
                        if (ticket != null) {
                            requestBuilder.addHeader("userId", ticket);
                        }
                        if (!TextUtils.isEmpty(cookie))
                            requestBuilder.addHeader("Cookie", cookie);
                        return chain.proceed(requestBuilder.build());
                    }
                }).build();
        return mClient;
    }

    private GsonConverterFactory initGsonFactory() {
        GsonBuilder gb = new GsonBuilder().enableComplexMapKeySerialization()
                .setLongSerializationPolicy(LongSerializationPolicy.DEFAULT);
        gb.registerTypeAdapter(
                new TypeToken<HashMap<String, Object>>() {
                }.getType()
                , new JsonDeserializer<HashMap<String, Object>>() {
                    @Override
                    public HashMap<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        HashMap<String, Object> treeMap = new HashMap<>();
                        JsonObject jsonObject = json.getAsJsonObject();
                        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                        for (Map.Entry<String, JsonElement> entry : entrySet) {
                            Object ot = entry.getValue();
                            if (ot instanceof JsonPrimitive) {
                                treeMap.put(entry.getKey(), ((JsonPrimitive) ot).getAsString());
                            } else {
                                treeMap.put(entry.getKey(), ot);
                            }
                        }
                        return treeMap;
                    }
                });
        return GsonConverterFactory.create(gb.create());
    }

    private Consumer<HttpBaseBean> callbackFirtStep = new Consumer<HttpBaseBean>() {
        @Override
        public void accept(HttpBaseBean httpBaseBean) throws Exception {

        }
    };

    public Observable doPost(String url, @NonNull Map<String, Object> params, Callback callback) {
        HttpService service = getInstance().mRetrofit.create(HttpService.class);
        Observable<HttpBaseBean> observable = service.doPost(url, params);
        attach(observable, callback);
        return observable;
    }

    public Observable doPost(String url, @NonNull RequestBody params, Callback callback) {
        HttpService service = getInstance().mRetrofit.create(HttpService.class);
        Observable<HttpBaseBean> observable = service.doPost(url, params);
        attach(observable, callback);
        return observable;
    }

    public Observable upLoad(String url, Map<String, Object> params, @NonNull Map<String, RequestBody> multiParams, Callback callback) {
        HttpService service = getInstance().mRetrofit.create(HttpService.class);
        Observable<HttpBaseBean> observable = service.upLoad(url, params, multiParams);
        attach(observable, callback);
        return observable;
    }

    private void attach(final Observable<HttpBaseBean> observable, final Callback callback) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(callbackFirtStep)
                .subscribe(new Observer<HttpBaseBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e("onSubscribe--------", "------------");
                        if (callback != null)
                            callback.onStart(d);
                    }

                    @Override
                    public void onNext(@NonNull HttpBaseBean httpBaseBean) {
                        Log.e("onNext--------", "------------" + httpBaseBean.toString());
                        if (callback != null && httpBaseBean.isFirstStepSuccess()) {
                            callback.onSuccess(httpBaseBean);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null && e.getMessage() != null)
                            Log.e("onError--------", e.getMessage());
                        if (callback != null) {
                            callback.onError(e);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.e("onComplete--------", "------------");
                        if (callback != null) {
                            callback.onComplete();
                        }
                    }
                });
    }

    public void downLoad(@NonNull String url, final String toPath, Callback callback) {
        HttpService service = getInstance().mRetrofit.create(HttpService.class);
        service.download(url).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).map(new Function<ResponseBody, InputStream>() {
            @Override
            public InputStream apply(ResponseBody responseBody) throws Exception {
                return responseBody.byteStream();
            }
        }).observeOn(Schedulers.computation()).doOnNext(new Consumer<InputStream>() {
            @Override
            public void accept(InputStream inputStream) throws Exception {

                writeFile(inputStream, toPath, callback);

            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<InputStream>() {
            @Override
            public void onSubscribe(Disposable d) {

                if (callback != null) {
                    callback.onStart(d);
                }
            }

            @Override
            public void onNext(InputStream inputStream) {

                if (callback != null) {
                    HttpBaseBean bean = new HttpBaseBean();
                    bean.setCode(0);
                    callback.onSuccess(bean);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (e != null && e.getMessage() != null)
                    Log.e("onError--------", e.getMessage());
                if (callback != null) {
                    callback.onError(e);
                }
            }

            @Override
            public void onComplete() {
                Log.e("onComplete--------", "------------");
                if (callback != null) {
                    callback.onComplete();
                }
            }
        });
    }

    public void downLoad(@NonNull String url, final String toPath) {
        HttpService service = getInstance().mRetrofit.create(HttpService.class);
        service.download(url).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).map(new Function<ResponseBody, InputStream>() {
            @Override
            public InputStream apply(ResponseBody responseBody) throws Exception {
                return responseBody.byteStream();
            }
        }).observeOn(Schedulers.computation()).doOnNext(new Consumer<InputStream>() {
            @Override
            public void accept(InputStream inputStream) throws Exception {

                writeFile(inputStream, toPath, null);

            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    /**
     * 将输入流写入文件
     *
     * @param inputString
     * @param filePath
     */
    private void writeFile(InputStream inputString, String filePath, Callback callback) {

        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        } else {
            file.getParentFile().mkdirs();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            byte[] b = new byte[1024];

            int len;
            while ((len = inputString.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            inputString.close();
            fos.close();

        } catch (FileNotFoundException e) {
            if (callback != null)
                callback.onError(e.getCause());
        } catch (IOException e) {
            if (callback != null)
                callback.onError(e.getCause());
        }
    }

    public static HttpClient getInstance() {
        return SingleHolder.mSingleton;
    }

    private static class SingleHolder {
        private static final HttpClient mSingleton = new HttpClient();
    }

    public interface Callback<T> {

        void onStart(Disposable disposable);

        void onSuccess(HttpBaseBean<T> result);

        void onError(Throwable throwable);

        void onComplete();
    }

}
