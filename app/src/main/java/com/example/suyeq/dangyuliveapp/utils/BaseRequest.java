package com.example.suyeq.dangyuliveapp.utils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Suyeq on 2018/5/27.
 */

public class BaseRequest {
    private Retrofit retrofit;
    private String baseUrl="http://dangyu.butterfly.mopaasapp.com/";
    public GetService request_start(){
        retrofit=new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetService request = retrofit.create(GetService.class);
        return request;
    }
}
