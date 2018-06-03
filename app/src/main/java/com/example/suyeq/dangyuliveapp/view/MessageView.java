package com.example.suyeq.dangyuliveapp.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.suyeq.dangyuliveapp.R;
import com.example.suyeq.dangyuliveapp.model.MessageInfo;
import com.example.suyeq.dangyuliveapp.utils.ImgUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageView extends RelativeLayout {
    private ListView list;
    private ChatMsgAdapter mChatMsgAdapter;
    public MessageView(Context context) {
        super(context);
        init();
    }
    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_chat_msg_list,this,true);
        list=(ListView) findViewById(R.id.chat_msg_list);
        mChatMsgAdapter=new ChatMsgAdapter();
        list.setAdapter(mChatMsgAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageInfo msgInfo = mChatMsgAdapter.getItem(position);
                //showUserInfoDialog(msgInfo.getSenderId());
            }
        });
    }

    public void addMsgInfo(MessageInfo info) {
        if (info != null) {
            mChatMsgAdapter.addMsgInfo(info);
            list.smoothScrollToPosition(mChatMsgAdapter.getCount());
        }
    }

    public void addMsgInfos(List<MessageInfo> infos) {
        if (infos != null) {
            mChatMsgAdapter.addMsgInfos(infos);
            list.smoothScrollToPosition(mChatMsgAdapter.getCount());
        }
    }
    private class ChatMsgAdapter extends BaseAdapter {

        private List<MessageInfo> mChatMsgInfos = new ArrayList<MessageInfo>();

        public void addMsgInfo(MessageInfo info) {
            if (info != null) {
                mChatMsgInfos.add(info);
                notifyDataSetChanged();
            }
        }

        public void addMsgInfos(List<MessageInfo> infos) {
            if (infos != null) {
                mChatMsgInfos.addAll(infos);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return mChatMsgInfos.size();
        }

        @Override
        public MessageInfo getItem(int i) {
            return mChatMsgInfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ChatMsgHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.view_chat_msg_list_item, null);
                holder = new ChatMsgHolder(view);
                view.setTag(holder);
            } else {
                holder = (ChatMsgHolder) view.getTag();
            }

            holder.bindData(mChatMsgInfos.get(i));

            return view;
        }
    }

    private class ChatMsgHolder {

        private ImageView avatar;
        private TextView content;

        private MessageInfo chatMsgInfo;

        public ChatMsgHolder(View itemView) {
            avatar = (ImageView) itemView.findViewById(R.id.sender_avatar);
            content = (TextView) itemView.findViewById(R.id.chat_content);
        }

        public void bindData(MessageInfo info) {
            chatMsgInfo = info;

            String avatarUrl = info.getAvatar();
            if (TextUtils.isEmpty(avatarUrl)) {
                ImgUtils.loadRound(R.drawable.default_avatar, avatar);
            } else {
                ImgUtils.loadRound(avatarUrl, avatar);
            }
            content.setText(info.getContent());
        }
    }

}
