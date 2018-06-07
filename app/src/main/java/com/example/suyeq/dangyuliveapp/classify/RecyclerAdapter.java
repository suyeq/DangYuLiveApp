package com.example.suyeq.dangyuliveapp.classify;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suyeq.dangyuliveapp.R;
import com.example.suyeq.dangyuliveapp.model.RoomInfo;
import com.example.suyeq.dangyuliveapp.utils.ImgUtils;
import com.example.suyeq.dangyuliveapp.watch.WatchLivingActivity;
import com.tencent.TIMUserProfile;

import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<RoomInfo> list=new ArrayList<RoomInfo>();

    public void addRoominfos(RoomInfo roominfo){
        list.add(roominfo);
        notifyDataSetChanged();
    }

    public void addRoominfos(List<RoomInfo> l){
        list.addAll(l);
        notifyDataSetChanged();
    }
    public void removeRooninfo(RoomInfo roominfo){
        if(roominfo!=null){
            list.remove(roominfo);
            notifyDataSetChanged();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context=parent.getContext();
        View childView = inflater.inflate(R.layout.item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(childView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView liveTitle;
        ImageView liveCover;
        ImageView hostAvatar;
        TextView hostName;
        TextView watchNums;
        View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            liveTitle = (TextView) itemView.findViewById(R.id.live_title);
            liveCover = (ImageView) itemView.findViewById(R.id.live_cover);
            hostName = (TextView) itemView.findViewById(R.id.host_name);
            hostAvatar = (ImageView) itemView.findViewById(R.id.host_avatar);
            watchNums = (TextView) itemView.findViewById(R.id.watch_nums);
        }
        public void bindData(final RoomInfo roomInfo) {
            String userName = roomInfo.getUserName();
            if (TextUtils.isEmpty(userName)) {
                userName = roomInfo.getUserId();
            }
            hostName.setText(userName);

            String liveTitleStr = roomInfo.getLiveTitle();
            if (TextUtils.isEmpty(liveTitleStr)) {
                this.liveTitle.setText(userName + "的直播");
            } else {
                this.liveTitle.setText(liveTitleStr);
            }
            String url = roomInfo.getLiveCover();
            if (TextUtils.isEmpty(url)) {
                ImgUtils.load(R.drawable.default_cover, liveCover);
            } else {
                ImgUtils.load(url, liveCover);
            }

            String avatar = roomInfo.getUserAvatar();
            if (TextUtils.isEmpty(avatar)) {
                ImgUtils.loadRound(R.drawable.default_avatar, hostAvatar);
            } else {
                ImgUtils.loadRound(avatar, hostAvatar);
            }

            int watchers = roomInfo.getWatcherNums();
            String watchText = watchers + "人\r\n正在看";
            watchNums.setText(watchText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(context, WatchLivingActivity.class);
                    intent.putExtra("roomId", roomInfo.getRoomId());
                    intent.putExtra("hostId", roomInfo.getUserId());
                    context.startActivity(intent);
                }
            });


        }
    }
}
