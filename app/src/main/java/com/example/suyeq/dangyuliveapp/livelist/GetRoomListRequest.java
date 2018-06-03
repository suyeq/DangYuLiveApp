package com.example.suyeq.dangyuliveapp.livelist;

import com.example.suyeq.dangyuliveapp.createlive.CreateRoomRequest;
import com.example.suyeq.dangyuliveapp.model.RoomInfo;
import com.example.suyeq.dangyuliveapp.utils.BaseRequest;
import com.example.suyeq.dangyuliveapp.utils.GetService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suyeq on 2018/5/28.
 */

public class GetRoomListRequest extends BaseRequest {
    private GetService request;

    public GetRoomListRequest(){
        request=request_start();

    }

    public void getRoomListRequest(int indexPage){
        Call<List<RoomInfo>> call = request.getRoomInfoList("getList",indexPage);
        //发送网络请求(异步)
        call.enqueue(new Callback<List<RoomInfo>>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<List<RoomInfo>> call, Response<List<RoomInfo>> response) {
                l.onSuccess(response.body());
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<List<RoomInfo>> call, Throwable throwable) {
                l.onFail();
            }
        });
    }

    //设置监听器
    public interface OnResultListener<T> {
        void onFail();

        void onSuccess(T t);
    }

    private GetRoomListRequest.OnResultListener l;

    public void setOnResultListener(GetRoomListRequest.OnResultListener listener){
        l=listener;
    }
}
