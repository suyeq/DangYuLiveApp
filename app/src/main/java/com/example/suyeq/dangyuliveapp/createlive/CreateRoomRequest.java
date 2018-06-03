package com.example.suyeq.dangyuliveapp.createlive;

import com.example.suyeq.dangyuliveapp.model.RoomInfo;
import com.example.suyeq.dangyuliveapp.utils.BaseRequest;
import com.example.suyeq.dangyuliveapp.utils.GetService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Suyeq on 2018/5/27.
 */
/*
"roomId":29,"userId":"","userName":"","userAvatar":"","liveCover":"","liveTitle":"","watcherNums":0
 */
public class CreateRoomRequest extends BaseRequest {

    private GetService request;

    public static class CreateRoomParam {
        public String userId;
        public String userAvatar;
        public String userName;
        public String liveTitle;
        public String liveCover;
    }
    public CreateRoomRequest(){
        request=request_start();

    }

    public void createRoom(CreateRoomParam parm){

       //对发送请求进行封装
        Call<RoomInfo> call = request.getRoomInfo("create",parm.userId,parm.userName,parm.userAvatar,parm.liveCover,parm.liveTitle);
        //发送网络请求(异步)
        call.enqueue(new Callback<RoomInfo>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<RoomInfo> call, Response<RoomInfo> response) {
                l.onSuccess(response.body());

            }

            //请求失败时回调
            @Override
            public void onFailure(Call<RoomInfo> call, Throwable throwable) {
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