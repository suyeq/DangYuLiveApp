package com.example.suyeq.dangyuliveapp.livelist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suyeq.dangyuliveapp.R;
import com.example.suyeq.dangyuliveapp.model.ClassCategory;
import com.example.suyeq.dangyuliveapp.model.RoomInfo;
import com.example.suyeq.dangyuliveapp.utils.BaseRequest;
import com.example.suyeq.dangyuliveapp.utils.ImgUtils;
import com.example.suyeq.dangyuliveapp.watch.WatchLivingActivity;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveConstants;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suyeq on 2018/5/22.
 */

public class LiveListFragment extends Fragment {
    private ListView mLiveListView;
    private LiveListAdapter mLiveListAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_live_list, container, false);
        findAllViews(view);
        getListRequest();
        return view;
    }

    private void getListRequest(){
        GetRoomListRequest roomListRequest=new GetRoomListRequest();
        roomListRequest.setOnResultListener(new GetRoomListRequest.OnResultListener<List<RoomInfo>>() {
        @Override
        public void onFail() {
            Toast.makeText(getActivity(), "刷新失败", Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onSuccess(List<RoomInfo> roomInfos) {
            mLiveListAdapter.removeAllRoomInfos();//下拉刷新，先移除掉之前的room信息
            mLiveListAdapter.addRoomInfos(roomInfos);//再添加新的信息
            mSwipeRefreshLayout.setRefreshing(false);
        }
    });
        int indexPage=0;
        roomListRequest.getRoomListRequest(indexPage);
}


    private void findAllViews(View view) {

        Toolbar titlebar = (Toolbar) view.findViewById(R.id.titlebar);
        titlebar.setTitle("热播列表");
        titlebar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(titlebar);

        mLiveListView = (ListView) view.findViewById(R.id.live_list);
        mLiveListAdapter = new LiveListAdapter(getContext());
        mLiveListView.setAdapter(mLiveListAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_list);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //请求服务器，获取直播列表
                getListRequest();
            }
        });
    }

    private class LiveListAdapter extends BaseAdapter {

        private Context mContext;
        private List<RoomInfo> liveRooms = new ArrayList<RoomInfo>();

        public LiveListAdapter(Context context) {
            this.mContext = context;
        }

        public void removeAllRoomInfos() {
            liveRooms.clear();
        }

        public void addRoomInfos(List<RoomInfo> roomInfos) {
            if (roomInfos != null) {
                liveRooms.clear();
                liveRooms.addAll(roomInfos);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return liveRooms.size();
        }

        @Override
        public RoomInfo getItem(int position) {
            return liveRooms.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RoomHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_live_list, null);
                holder = new RoomHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (RoomHolder) convertView.getTag();
            }

            holder.bindData(liveRooms.get(position));

            return convertView;
        }


        private class RoomHolder {

            View itemView;
            TextView liveTitle;
            ImageView liveCover;
            ImageView hostAvatar;
            TextView hostName;
            TextView watchNums;

            public RoomHolder(View view) {
                itemView = view;
                liveTitle = (TextView) view.findViewById(R.id.live_title);
                liveCover = (ImageView) view.findViewById(R.id.live_cover);
                hostName = (TextView) view.findViewById(R.id.host_name);
                hostAvatar = (ImageView) view.findViewById(R.id.host_avatar);
                watchNums = (TextView) view.findViewById(R.id.watch_nums);
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
                        intent.setClass(mContext, WatchLivingActivity.class);
                        intent.putExtra("roomId", roomInfo.getRoomId());
                        intent.putExtra("hostId", roomInfo.getUserId());
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
