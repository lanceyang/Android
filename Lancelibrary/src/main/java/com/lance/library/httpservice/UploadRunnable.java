package com.lance.library.httpservice;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by ZF_Develop_PC on 2017/11/10.
 */

public class UploadRunnable implements Runnable {

    private Listener listener;

    private Context context;

    private String[] files;

    private UploadRunnable() {
    }

    private int Id;

    private Disposable disposable;

    private int code = 0;

    private String msg = null;

    private String url = "";

    private Boolean isSingleUpload = false;

    private Map<String, Object> params;

    private void setSingleUpload(Boolean singleUpload) {
        isSingleUpload = singleUpload;
    }

    public void cancel() {
        disposable.dispose();
    }

    private void setUrl(String url) {
        this.url = url;
    }

    private void setFiles(String[] files) {
        this.files = files;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {

        Map<String, RequestBody> multiParams = new HashMap<>();

        if (isSingleUpload) {
            File file = new File(files[0]);
            Uri uri;

            RequestBody fileBody = RequestBody.create(MediaType.parse(""), file);
            try {
                multiParams.put("files\"; filename=\"" + URLEncoder.encode(file.getName(), "utf-8"), fileBody);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else
            for (String path : files) {
                File file = new File(path);
                RequestBody fileBody = RequestBody.create(MediaType.parse(""), file);
                try {
                    multiParams.put("files\"; filename=\"" + URLEncoder.encode(file.getName(), "utf-8"), fileBody);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        HttpClient.getInstance().upLoad(url, params, multiParams, new HttpClient.Callback<List<String>>() {

            private HttpBaseBean data;

            @Override
            public void onStart(Disposable disposable) {
                UploadRunnable.this.disposable = disposable;
            }

            @Override
            public void onSuccess(HttpBaseBean<List<String>> result) {
                data = result;
                code = result.getCode();
                if (code == 0)
                    msg = "上传成功";
                else
                    msg = "没有相关匹配";
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e(UploadRunnable.this.getClass().getSimpleName(), throwable.getMessage() == null ? "未知原因" : throwable.getMessage());

                code = 1;
                msg = throwable.getMessage() == null ? "未知原因" : throwable.getMessage();
            }

            @Override
            public void onComplete() {
                if (listener != null)
                    listener.onComplete(code == 200, msg, data.getData());
            }
        });
    }

    public interface Listener<T> {

        void onComplete(boolean isSuccess, String msg, T data);

    }

    public static class Builder {

        private String[] files = new String[0];

        private Boolean isSingleUpload = false;

        private String url = "";

        private Map<String, Object> params = new HashMap<>();

        public Builder setSingleUpload(Boolean singleUpload) {
            isSingleUpload = singleUpload;
            return this;
        }

        public Builder setParams(Map<String, Object> params) {
            this.params = params;

            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setFiles(String[] files) {
            this.files = files;
            return this;
        }

        public UploadRunnable create() {
            UploadRunnable runnable = new UploadRunnable();
            runnable.setFiles(files);
            runnable.setParams(params);
            runnable.setSingleUpload(isSingleUpload);
            runnable.setUrl(url);
            return runnable;
        }
    }
}
