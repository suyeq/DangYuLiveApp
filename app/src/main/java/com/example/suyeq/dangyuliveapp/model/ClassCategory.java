package com.example.suyeq.dangyuliveapp.model;

import com.tencent.livesdk.ILVLiveConstants;

/**
 * Created by Suye.
 */

public class ClassCategory {

    //自定义发送列表聊天
    public static final int CMD_CHAT_MSG_LIST = ILVLiveConstants.ILVLIVE_CMD_CUSTOM_LOW_LIMIT + 1;
    //自定义发送弹幕聊天
    public static final int CMD_CHAT_MSG_DANMU = ILVLiveConstants.ILVLIVE_CMD_CUSTOM_LOW_LIMIT + 2;

    //自定义发送礼物
    public static final int CMD_CHAT_GIFT = ILVLiveConstants.ILVLIVE_CMD_CUSTOM_LOW_LIMIT + 3;

    public static final int CMD_CHAT_hostleave = ILVLiveConstants.ILVLIVE_CMD_CUSTOM_LOW_LIMIT + 4;

    public static final int CMD_CHAT_watcherleave = ILVLiveConstants.ILVLIVE_CMD_CUSTOM_LOW_LIMIT + 5;


}
