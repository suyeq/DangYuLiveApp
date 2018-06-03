package com.example.suyeq.dangyuliveapp.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.suyeq.dangyuliveapp.R;
import com.example.suyeq.dangyuliveapp.model.ClassCategory;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVText;

public class ChatView extends LinearLayout {
    private CheckBox mSwitchChatType;
    private EditText mChatContent;
    private TextView mSend;
    public ChatView(Context context) {
        super(context);
        init();
    }
    public ChatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public ChatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_chat,this,true);
        findAllView();
    }

    private void findAllView() {
        mSwitchChatType = (CheckBox) findViewById(R.id.switch_chat_type);
        mChatContent = (EditText) findViewById(R.id.chat_content_edit);
        mSend = (TextView) findViewById(R.id.chat_send);

        mSwitchChatType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mChatContent.setHint("发送弹幕聊天消息");
                } else {
                    mChatContent.setHint("和大家聊点什么吧");

                }
            }
        });
        mSwitchChatType.setChecked(false);
        mChatContent.setHint("和大家聊点什么吧");
        mSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //发送聊天消息。
                sendChatMsg();
            }
        });
    }

    private void sendChatMsg() {
        if (mOnChatSendListener != null) {
            ILVCustomCmd customCmd = new ILVCustomCmd();
            customCmd.setType(ILVText.ILVTextType.eGroupMsg);
            boolean isDanmu = mSwitchChatType.isChecked();
            if (isDanmu) {
                customCmd.setCmd(ClassCategory.CMD_CHAT_MSG_DANMU);
            } else {
                customCmd.setCmd(ClassCategory.CMD_CHAT_MSG_LIST);
            }
            customCmd.setParam(mChatContent.getText().toString());
            mOnChatSendListener.onChatSend(customCmd);//设置消息内容
        }
    }

    private OnChatSendListener mOnChatSendListener;

    public void setOnChatSendListener(OnChatSendListener l) {
        mOnChatSendListener = l;
    }

    public interface OnChatSendListener {
        public void onChatSend(ILVCustomCmd msg);
    }
}
