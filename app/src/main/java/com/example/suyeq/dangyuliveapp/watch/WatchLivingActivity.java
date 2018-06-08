package com.example.suyeq.dangyuliveapp.watch;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.suyeq.dangyuliveapp.AppApplication;
import com.example.suyeq.dangyuliveapp.R;
import com.example.suyeq.dangyuliveapp.living.QuitLivingQuest;
import com.example.suyeq.dangyuliveapp.model.ClassCategory;
import com.example.suyeq.dangyuliveapp.model.MessageInfo;
import com.example.suyeq.dangyuliveapp.view.BarrageView;
import com.example.suyeq.dangyuliveapp.view.BottomControlView;
import com.example.suyeq.dangyuliveapp.view.ChageRelayout;
import com.example.suyeq.dangyuliveapp.view.ChatView;
import com.example.suyeq.dangyuliveapp.view.MessageView;
import com.example.suyeq.dangyuliveapp.view.TitleView;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMMessage;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveConstants;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;
import com.tencent.livesdk.ILVText;

import java.util.ArrayList;
import java.util.List;

public class WatchLivingActivity extends AppCompatActivity {
    private AVRootView mLiveView;
    private int roomId;
    private String userId;
    private BottomControlView bottomControlView;
    private ChatView chatView;
    private ChageRelayout chageRelayout;
    private MessageView messageView;
    private BarrageView barrageView;
    private TitleView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_living);
        findAllView();
        joinRoom();
    }

    private void joinRoom() {
        final String hostId=AppApplication.getApplication().getSelfProfile().getIdentifier();
        if(roomId<-1){
            Toast.makeText(this, "该房间号不存在！！", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        updateAvatar(userId);
        ILVLiveConfig liveConfig = AppApplication.getApplication().getLiveConfig();
        liveConfig.setLiveMsgListener(new ILVLiveConfig.ILVLiveMsgListener() {
            @Override
            public void onNewTextMsg(ILVText text, String SenderId, TIMUserProfile userProfile) {
                //接收到文本消息
            }

            @Override
            public void onNewCustomMsg(ILVCustomCmd cmd, String id, TIMUserProfile userProfile) {
                //接收到自定义消息
                if (cmd.getCmd() == ClassCategory.CMD_CHAT_MSG_LIST) {
                    String content = cmd.getParam();
                    MessageInfo info = MessageInfo.createListInfo(content, id, userProfile.getFaceUrl());
                    messageView.addMsgInfo(info);
                } else if (cmd.getCmd() == ClassCategory.CMD_CHAT_MSG_DANMU) {
                    String content = cmd.getParam();
                    MessageInfo info = MessageInfo.createListInfo(content, id, userProfile.getFaceUrl());
                    messageView.addMsgInfo(info);
                    String name = userProfile.getNickName();
                    if (TextUtils.isEmpty(name)) {
                        name = userProfile.getIdentifier();
                    }
                    MessageInfo danmuInfo = MessageInfo.createDanmuInfo(content, id, userProfile.getFaceUrl(), name);
                    barrageView.addMsgInfo(danmuInfo);
                } else if (cmd.getCmd() == ILVLiveConstants.ILVLIVE_CMD_LEAVE) {
                    Toast.makeText(WatchLivingActivity.this, "观众已离开", Toast.LENGTH_SHORT).show();
                } else if (cmd.getCmd() == ILVLiveConstants.ILVLIVE_CMD_ENTER) {
                    Toast.makeText(WatchLivingActivity.this, "观众111111", Toast.LENGTH_SHORT).show();
                    titleView.addWatcher(userProfile);
//                    mVipEnterView.showVipEnter(userProfile);
                }else if (cmd.getCmd() == ClassCategory.CMD_CHAT_hostleave) {//主播离开
                    Toast.makeText(WatchLivingActivity.this, "主播已下播", Toast.LENGTH_SHORT).show();
                    finish();
                }else if (cmd.getCmd() == ClassCategory.CMD_CHAT_watcherleave) {//观众离开
                    titleView.removeWatcher(userProfile);
                    //removeWatcher(roomId,userProfile.getIdentifier());
                    Toast.makeText(WatchLivingActivity.this, "观众已离开", Toast.LENGTH_SHORT).show();
                    //finish();
                }
            }

            @Override
            public void onNewOtherMsg(TIMMessage message) {
                //接收到其他消息
            }
        });

        ILVLiveRoomOption ilvLiveRoomOption=new ILVLiveRoomOption(userId)
                .autoCamera(false)//是否打开摄像头
                .controlRole("Guest")//角色设置
                .authBits(AVRoomMulti.AUTH_BITS_JOIN_ROOM | AVRoomMulti.AUTH_BITS_RECV_AUDIO | AVRoomMulti.AUTH_BITS_RECV_CAMERA_VIDEO | AVRoomMulti.AUTH_BITS_RECV_SCREEN_VIDEO)//权限设置
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO)//是否开始半自动接收
                .autoMic(false);//是否自动打开mic

        ILVLiveManager.getInstance().joinRoom(roomId, ilvLiveRoomOption, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                setEnterRoom();
                addWatcher(roomId,AppApplication.getApplication().getSelfProfile().getIdentifier());
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Toast.makeText(WatchLivingActivity.this, "主播已下播", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateAvatar(String hostId) {
        List<String> ids = new ArrayList<String>();
        ids.add(hostId);
        TIMFriendshipManager.getInstance().getUsersProfile(ids, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {
                Toast.makeText(WatchLivingActivity.this, "请求用户信息失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                titleView.setHost(timUserProfiles.get(0));
            }
        });
    }

    private void addWatcher(int roomId,String userId) {
        JoinRoomRequest request=new JoinRoomRequest();
        request.request(userId,roomId);
        request.setOnResultListener(new JoinRoomRequest.OnResultListener<String>() {
            @Override
            public void onFail() {
               // Toast.makeText(WatchLivingActivity.this, "加入房间失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String o) {
//                Toast.makeText(WatchLivingActivity.this, "加入房间成功"+o, Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void removeWatcher(int roomId,String userId){
//        QuitLivingQuest request=new QuitLivingQuest();
//        request.request(roomId,userId);
//        request.setOnResultListener(new QuitLivingQuest.OnResultListener<String>() {
//            @Override
//            public void onFail() {
//
//            }
//
//            @Override
//            public void onSuccess(String o) {
//
//            }
//        });
//    }


    private void findAllView() {
        roomId=getIntent().getIntExtra("roomId",-1);
        userId=getIntent().getStringExtra("hostId");
        chageRelayout=(ChageRelayout) findViewById(R.id.chage_view);
        barrageView=(BarrageView) findViewById(R.id.barrage_view);
        chageRelayout.setOnSizeChangeListener(new ChageRelayout.OnSizeChangeListener() {
            @Override
            public void onHide(){
                chatView.setVisibility(View.INVISIBLE);
                bottomControlView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onShow() {

            }
        });
        titleView=(TitleView) findViewById(R.id.title_view);
        mLiveView = (AVRootView) findViewById(R.id.live_view);
        ILVLiveManager.getInstance().setAvVideoView(mLiveView);
        messageView=(MessageView) findViewById(R.id.msg_view);
        bottomControlView=(BottomControlView) findViewById(R.id.control_view);
        //bottomControlView.setIsHost(true);
        bottomControlView.setOnControlListener(new BottomControlView.OnControlListener() {
            @Override
            public void onChatClick() {
                chatView.setVisibility(View.VISIBLE);
                bottomControlView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCloseClick() {
                finish();
            }

            @Override
            public void onGiftClick() {

            }

            @Override
            public void onOptionClick(View view) {

            }
        });

        chatView=(ChatView)findViewById(R.id.chat_view);
        chatView.setOnChatSendListener(new ChatView.OnChatSendListener() {
            @Override
            public void onChatSend(final ILVCustomCmd msg) {
                msg.setDestId(ILiveRoomManager.getInstance().getIMGroupId());
                ILVLiveManager.getInstance().sendCustomCmd(msg, new ILiveCallBack<TIMMessage>() {
                    @Override
                    public void onSuccess(TIMMessage data) {
                        if (msg.getCmd() == ClassCategory.CMD_CHAT_MSG_LIST) {
                            //如果是列表类型的消息，发送给列表显示
                            String chatContent = msg.getParam();
                            String userId = AppApplication.getApplication().getSelfProfile().getIdentifier();
                            String avatar = AppApplication.getApplication().getSelfProfile().getFaceUrl();
                            MessageInfo info = MessageInfo.createListInfo(chatContent, userId, avatar);
                            messageView.addMsgInfo(info);
                        } else if (msg.getCmd() == ClassCategory.CMD_CHAT_MSG_DANMU) {
                            String chatContent = msg.getParam();
                            String userId = AppApplication.getApplication().getSelfProfile().getIdentifier();
                            String avatar = AppApplication.getApplication().getSelfProfile().getFaceUrl();
                            MessageInfo info = MessageInfo.createListInfo(chatContent, userId, avatar);
                            messageView.addMsgInfo(info);

                            String name = AppApplication.getApplication().getSelfProfile().getNickName();
                            if (TextUtils.isEmpty(name)) {
                                name = userId;
                            }
                            MessageInfo danmuInfo = MessageInfo.createDanmuInfo(chatContent, userId, avatar, name);
                            barrageView.addMsgInfo(danmuInfo);
                        }
                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                    }

                });
            }
        });
    }
    private void setEnterRoom(){
        ILVCustomCmd customCmd = new ILVCustomCmd();
        customCmd.setType(ILVText.ILVTextType.eGroupMsg);
        customCmd.setCmd(ILVLiveConstants.ILVLIVE_CMD_ENTER);
        ILVLiveManager.getInstance().sendCustomCmd(customCmd, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                titleView.addWatcher(AppApplication.getApplication().getSelfProfile());
                Toast.makeText(WatchLivingActivity.this, "观众进入直播间", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        ILVLiveManager.getInstance().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ILVLiveManager.getInstance().onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        quitLive();
    }

    private void quitLive() {
        ILVCustomCmd customCmd = new ILVCustomCmd();
        customCmd.setType(ILVText.ILVTextType.eGroupMsg);
        customCmd.setCmd(ClassCategory.CMD_CHAT_watcherleave);
        customCmd.setDestId(ILiveRoomManager.getInstance().getIMGroupId());
        ILVLiveManager.getInstance().sendCustomCmd(customCmd, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                ILiveRoomManager.getInstance().quitRoom(new ILiveCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        Toast.makeText(WatchLivingActivity.this, "成功退出直播间", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {

                    }
                });
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
               // Toast.makeText(WatchLivingActivity.this, "退出直播间失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
