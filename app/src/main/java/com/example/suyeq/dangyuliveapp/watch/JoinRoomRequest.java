package com.example.suyeq.dangyuliveapp.watch;

import com.example.suyeq.dangyuliveapp.utils.BaseRequest;
import com.example.suyeq.dangyuliveapp.utils.GetService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinRoomRequest extends BaseRequest {
    private GetService request;

    public JoinRoomRequest(){
        request=request_start();
    }

    public void request(String userId,int roomId){
        //对发送请求进行封装
        Call<Object> call = request.joinLiving("quit",userId,roomId);
        //发送网络请求(异步)
        call.enqueue(new Callback<Object>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                l.onSuccess(response.body());

            }

            //请求失败时回调
            @Override
            public void onFailure(Call<Object> call, Throwable throwable) {
                l.onFail();
            }
        });
    }

    //设置监听器
    public interface OnResultListener<T> {
        void onFail();

        void onSuccess(T t);
    }

    private OnResultListener l;

    public void setOnResultListener(OnResultListener listener){
        l=listener;
    }
}
