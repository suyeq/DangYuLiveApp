package com.example.suyeq.dangyuliveapp.model;

/**
 * Created by Suyeq on 2018/5/27.
 */

public class RoomInfo {
    private int roomId;
    private String userId;
    private String userName;
    private String userAvatar;
    private String liveCover;
    private String liveTitle;
    private int watcherNums;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getLiveCover() {
        return liveCover;
    }

    public void setLiveCover(String liveCover) {
        this.liveCover = liveCover;
    }

    public String getLiveTitle() {
        return liveTitle;
    }

    public void setLiveTitle(String liveTitle) {
        this.liveTitle = liveTitle;
    }

    public int getWatcherNums() {
        return watcherNums;
    }

    public void setWatcherNums(int watcherNums) {
        this.watcherNums = watcherNums;
    }

    public String toString(){
        return "roomid="+roomId+
               "userroom="+ userId+
               "username="+userName+
                "useravatar="+ userAvatar+
                "livecover="+ liveCover+
                "livetitle="+ liveTitle+
                "watchernums="+ watcherNums;
    }
}
