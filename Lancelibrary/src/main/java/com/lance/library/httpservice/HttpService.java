package com.lance.library.httpservice;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Tjcx on 2017/9/15.
 */

public interface HttpService {

  @FormUrlEncoded
  @POST
  Observable<HttpBaseBean> doPost(@Url String url, @FieldMap Map<String, Object> params);

  @POST
  @Headers({"Content-Type:application/json;charset=UTF-8"})
  Observable<HttpBaseBean> doPost(@Url String url, @Body RequestBody params);

  @POST
  @Multipart
  @Streaming
  Observable<HttpBaseBean> upLoad(@Url String url, @QueryMap Map<String, Object> customParams, @PartMap Map<String, RequestBody> multiParams);

  @GET
  @Streaming
  Observable<ResponseBody> download(@Url String url);
}
