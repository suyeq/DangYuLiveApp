package com.example.suyeq.dangyuliveapp.living;

import com.example.suyeq.dangyuliveapp.createlive.CreateRoomRequest;
import com.example.suyeq.dangyuliveapp.model.RoomInfo;
import com.example.suyeq.dangyuliveapp.utils.BaseRequest;
import com.example.suyeq.dangyuliveapp.utils.GetService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuitLivingQuest extends BaseRequest {
    private GetService request;

    public QuitLivingQuest(){
        request=request_start();
    }

    public void request(int roomId,String userId){
        //对发送请求进行封装
        Call<String> call = request.quitLiving("quit",roomId,userId);
        //发送网络请求(异步)
        call.enqueue(new Callback<String>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                l.onSuccess(response.body());

            }

            //请求失败时回调
            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
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
