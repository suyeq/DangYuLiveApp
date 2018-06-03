package com.example.suyeq.dangyuliveapp.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.suyeq.dangyuliveapp.R;
import com.example.suyeq.dangyuliveapp.model.MessageInfo;

import java.util.LinkedList;
import java.util.List;




public class BarrageView extends LinearLayout {
    private BarrageItemView  item0;
    private BarrageItemView  item1;
    private BarrageItemView  item2;
    private BarrageItemView  item3;
    private List<MessageInfo> msgInfoList = new LinkedList<MessageInfo>();

    public BarrageView(Context context) {
        super(context);
        init();
    }

    public BarrageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarrageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_danmu,this,true);
        findAllView();
    }

    private void findAllView() {
        item0=(BarrageItemView) findViewById(R.id.danmu0);
        item1=(BarrageItemView) findViewById(R.id.danmu1);
        item2=(BarrageItemView) findViewById(R.id.danmu2);
        item3=(BarrageItemView) findViewById(R.id.danmu3);
        item0.setVisibility(INVISIBLE);
        item1.setVisibility(INVISIBLE);
        item2.setVisibility(INVISIBLE);
        item3.setVisibility(INVISIBLE);

        item0.setOnAvaliableListener(avaliableListener);
        item1.setOnAvaliableListener(avaliableListener);
        item2.setOnAvaliableListener(avaliableListener);
        item3.setOnAvaliableListener(avaliableListener);
    }

    private  BarrageItemView.OnAvaliableListener avaliableListener = new BarrageItemView.OnAvaliableListener() {
        @Override
        public void onAvaliable() {
            //有可用的itemview
            //从msgList中获取之前缓存下来的消息，然后显示出来。
            if(msgInfoList.size() > 0) {
                MessageInfo chatMsgInfo = msgInfoList.remove(0);
                addMsgInfo(chatMsgInfo);
            }
        }
    };


    public void addMsgInfo(MessageInfo danmuInfo) {
        BarrageItemView avaliableItemView = getAvaliableItemView();
        if (avaliableItemView == null) {
            //说明没有可用的itemView
            msgInfoList.add(danmuInfo);
        } else {
            //说明有可用的itemView
            avaliableItemView.showMsgInfo(danmuInfo);
        }
    }

    private BarrageItemView getAvaliableItemView() {
        //获取可用的item view
        if (item0.getVisibility() != VISIBLE) {
            return item0;
        }
        if (item1.getVisibility() != VISIBLE) {
            return item1;
        }
        if (item2.getVisibility() != VISIBLE) {
            return item2;
        }
        if (item3.getVisibility() != VISIBLE) {
            return item3;
        }
        return null;
    }
}
