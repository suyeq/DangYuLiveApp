package com.example.suyeq.dangyuliveapp.living;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.suyeq.dangyuliveapp.AppApplication;
import com.example.suyeq.dangyuliveapp.R;
import com.example.suyeq.dangyuliveapp.living.QuitLivingQuest.OnResultListener;
import com.example.suyeq.dangyuliveapp.model.ClassCategory;
import com.example.suyeq.dangyuliveapp.model.MessageInfo;
import com.example.suyeq.dangyuliveapp.view.BarrageView;
import com.example.suyeq.dangyuliveapp.view.BottomControlView;
import com.example.suyeq.dangyuliveapp.view.ChageRelayout;
import com.example.suyeq.dangyuliveapp.view.ChatView;
import com.example.suyeq.dangyuliveapp.view.MessageView;
import com.example.suyeq.dangyuliveapp.view.TitleView;
import com.example.suyeq.dangyuliveapp.watch.JoinRoomRequest;
import com.example.suyeq.dangyuliveapp.watch.WatchLivingActivity;
import com.tencent.TIMMessage;
import com.tencent.TIMUserProfile;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveConstants;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;
import com.tencent.livesdk.ILVText;

public class AnchorLivingActivity extends AppCompatActivity {
    private AVRootView mLiveView;
    private BottomControlView bottomControlView;
    private ChatView chatView;
    private ChageRelayout chageRelayout;
    private MessageView messageView;
    private BarrageView barrageView;
    private int roomId;
    private TitleView titleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living);
        findAllView();
        init();
        ILVLiveManager.getInstance().setAvVideoView(mLiveView);
        startList();
    }

    private void findAllView(){
        chageRelayout=(ChageRelayout) findViewById(R.id.chage_view);
        messageView=(MessageView) findViewById(R.id.msg_view);
        barrageView=(BarrageView) findViewById(R.id.barrage_view);
        titleView=(TitleView) findViewById(R.id.title_view);
        titleView.setHost(AppApplication.getApplication().getSelfProfile());
        chageRelayout.setOnSizeChangeListener(new ChageRelayout.OnSizeChangeListener() {
            @Override
            public void onHide() {
                chatView.setVisibility(View.INVISIBLE);
                bottomControlView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onShow() {

            }
        });
        mLiveView = (AVRootView) findViewById(R.id.live_view);
        bottomControlView=(BottomControlView) findViewById(R.id.control_view);
       // bottomControlView.setIsHost(true);
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
    //开始直播
    private void startList(){
//创建房间
        Intent intent=getIntent();
        roomId=intent.getIntExtra("roomId",-1);
        if(roomId<0){
            Toast.makeText(this, "房间号不存在！！", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

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
                } else if (cmd.getCmd() == ClassCategory.CMD_CHAT_GIFT) {
                    //界面显示礼物动画。
//                    GiftCmdInfo giftCmdInfo = new Gson().fromJson(cmd.getParam(), GiftCmdInfo.class);
//                    int giftId = giftCmdInfo.giftId;
//                    String repeatId = giftCmdInfo.repeatId;
//                    GiftInfo giftInfo = GiftInfo.getGiftById(giftId);
//                    if (giftInfo == null) {
//                        return;
//                    }
//                    if (giftInfo.type == GiftInfo.Type.ContinueGift) {
//                        giftRepeatView.showGift(giftInfo, repeatId, userProfile);
//                    } else if (giftInfo.type == GiftInfo.Type.FullScreenGift) {
//                        //全屏礼物
//                        giftFullView.showGift(giftInfo, userProfile);
//                    }
                } else if (cmd.getCmd() == ILVLiveConstants.ILVLIVE_CMD_ENTER) {
                    //用户进入直播
                    titleView.addWatcher(userProfile);
                    //addWatcher(userProfile,roomId);
//                    mVipEnterView.showVipEnter(userProfile);
                    Toast.makeText(AnchorLivingActivity.this, "观众已进入直播间", Toast.LENGTH_SHORT).show();
                } else if (cmd.getCmd() == ILVLiveConstants.ILVLIVE_CMD_LEAVE) {
                    //用户离开消息
                    //titleView.removeWatcher(userProfile);
                }else if (cmd.getCmd() == ClassCategory.CMD_CHAT_watcherleave) {//观众离开
                    titleView.removeWatcher(userProfile);
                    removeWatcher(roomId,userProfile.getIdentifier());
                    Toast.makeText(AnchorLivingActivity.this, "观众已离开", Toast.LENGTH_SHORT).show();
                    //finish();
                }

            }

            @Override
            public void onNewOtherMsg(TIMMessage message) {
                //接收到其他消息
            }
        });

        ILVLiveRoomOption hostOption = new ILVLiveRoomOption(ILiveLoginManager.getInstance().getMyUserId()).
                controlRole("LiveMaster")//角色设置
                .autoFocus(true)
                //.autoMic(hostControlState.isVoiceOn())
                .authBits(AVRoomMulti.AUTH_BITS_DEFAULT)//权限设置
                .cameraId(ILiveConstants.FRONT_CAMERA)//摄像头前置后置
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO);//是否开始半自动接收

        //创建房间
        ILVLiveManager.getInstance().createRoom(roomId, hostOption, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                //失败的情况下，退出界面。
                Toast.makeText(AnchorLivingActivity.this, "创建直播失败！", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        });
    }

    private void removeWatcher(int roomId,String userId){
        QuitLivingQuest request=new QuitLivingQuest();
        request.request(roomId,userId);
        request.setOnResultListener(new QuitLivingQuest.OnResultListener<String>() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess(String o) {

            }
        });
    }

    private void init(){
        chatView.setVisibility(View.INVISIBLE);
        bottomControlView.setVisibility(View.VISIBLE);
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
        customCmd.setCmd(ClassCategory.CMD_CHAT_hostleave);
        customCmd.setDestId(ILiveRoomManager.getInstance().getIMGroupId());
        ILVLiveManager.getInstance().sendCustomCmd(customCmd, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                ILiveRoomManager.getInstance().quitRoom(new ILiveCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        finish();
                        Toast.makeText(AnchorLivingActivity.this, "成功退出", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                       finish();
                        Toast.makeText(AnchorLivingActivity.this, "退出失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
//        QuitLivingQuest quest=new QuitLivingQuest();
//        String userId=AppApplication.getApplication().getSelfProfile().getIdentifier();
//        quest.request(roomId,userId);
//        quest.setOnResultListener(new QuitLivingQuest.OnResultListener<String>() {
//            @Override
//            public void onFail() {
//                Toast.makeText(AnchorLivingActivity.this, "删除失败！！！", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSuccess(String result) {
//                if(result.equals("1")){
//                    Toast.makeText(AnchorLivingActivity.this, "删除成功！！！", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    private void addWatcher(TIMUserProfile userProfile,int roomId) {
        JoinRoomRequest request=new JoinRoomRequest();
        String userId=AppApplication.getApplication().getSelfProfile().getIdentifier();
        request.request(userId,roomId);
        request.setOnResultListener(new JoinRoomRequest.OnResultListener<Object>() {
            @Override
            public void onFail() {
                Toast.makeText(AnchorLivingActivity.this, "加入房间失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object o) {
                Toast.makeText(AnchorLivingActivity.this, "加入房间成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
            
}


