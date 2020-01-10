package com.lance.library.custom;

import com.lance.library.BuildConfig;

/**
 * Created by Tjcx on 2017/9/27.
 */

public class Constant {

  //封装的httpClient的BaseUrl
//  public static String BASE_URL = "http://192.168.2.141:8080/";
//  public static String BASE_URL = "http://39.104.121.114:8080/yjp/";
  public static final String BASE_URL = BuildConfig.BASE_URL;
//  public static String BASE_URL = "http://192.168.2.25/yjp/";

  //本地化存储账号的key
  public static final String YOUR_ARE_MY_DISH = "you_are_my_dish";

  //本地化存储账号的key
  public static final String THE_SECRET_DISH = "dish_secret";

  //本地化存储用户ticket
  public static final String YOUR_ARE_MY_ONLY_ONE = "you_are_my_only_one";

  //本地持久化密码时加密的key
  public static final String ALL_TEARS = "Life is so charming that it brings countless heroes to the waist";

}
