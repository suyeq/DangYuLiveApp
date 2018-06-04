package com.example.suyeq.dangyuliveapp.utils;

import com.example.suyeq.dangyuliveapp.model.RoomInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Suyeq on 2018/5/27.
 */
public interface GetService {
    @POST("roomServlet")
    Call<RoomInfo> getRoomInfo(@Query("action") String action,
                               @Query("userId") String userId,
                               @Query("userName") String userName,
                               @Query("userAvatar") String userAvatar,
                               @Query("liveCover") String liveCover,
                               @Query("liveTitle") String liveTitle);

    @POST("roomServlet")
    Call<List<RoomInfo>> getRoomInfoList(@Query("action") String action,
                                         @Query("pageIndex") int pageIndex);

    @POST("roomServlet")
    Call<String> quitLiving(@Query("action") String action,
                            @Query("roomId") int roomId,
                            @Query("userId") String userId);

    @POST("roomServlet")
    Call<String> joinLiving(@Query("action") String action,
                            @Query("userId") String userId,
                            @Query("roomId") int roomId);
}
