package com.example.suyeq.dangyuliveapp.livelist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suyeq.dangyuliveapp.R;
import com.example.suyeq.dangyuliveapp.classify.ClassifyActivity;
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
    private ViewPager mViewPager;
    private List<ImageView> mImageViewList;
    private int[] images={R.drawable.xiaoling,R.drawable.xuexia,R.drawable.saber,R.drawable.yasina,R.drawable.xiaomai};
    private int currentPosition=1;
    private int dotPosition=0;
    private int prePosition=0;
    private LinearLayout mLinearLayoutDot;
    private List<ImageView> mImageViewDotList;
    private boolean threadstatus=true;
    private LinearLayout button_game;
    private LinearLayout button_yule;
    private LinearLayout button_huwai;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                mViewPager.setCurrentItem(currentPosition,false);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_live_list, container, false);
        findAllViews(view);
        initData();
        setDot();
        setViewPager();
        Log.e("jinlaile",threadstatus+"");
        autoPlay();
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
        threadstatus=true;
//        Toolbar titlebar = (Toolbar) view.findViewById(R.id.titlebar);
//        titlebar.setTitle("热播列表");
//      //  titlebar.setTitleTextColor(Color.bla);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(titlebar);
        mViewPager= (ViewPager) view.findViewById(R.id.vp_main);
        mLinearLayoutDot= (LinearLayout) view.findViewById(R.id.ll_main_dot);
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
        button_game=(LinearLayout) view.findViewById(R.id.button_game);
        button_yule=(LinearLayout) view.findViewById(R.id.button_yule);
        button_huwai=(LinearLayout) view.findViewById(R.id.button_huwai);
        button_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getContext(), ClassifyActivity.class);
                startActivity(intent);
            }
        });
        button_huwai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getContext(), ClassifyActivity.class);
                startActivity(intent);
            }
        });
        button_yule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getContext(), ClassifyActivity.class);
                startActivity(intent);
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

    private void initData() {
        mImageViewList=new ArrayList<>();
        mImageViewDotList=new ArrayList();
        ImageView imageView;
        for(int i=0;i<images.length+2;i++){
            if(i==0){   //判断当i=0为该处的ImageView设置最后一张图片作为背景
                imageView=new ImageView(getContext());
                imageView.setBackgroundResource(images[images.length-1]);
                mImageViewList.add(imageView);
            }else if(i==images.length+1){   //判断当i=images.length+1时为该处的ImageView设置第一张图片作为背景
                imageView=new ImageView(getContext());
                imageView.setBackgroundResource(images[0]);
                mImageViewList.add(imageView);
            }else{  //其他情况则为ImageView设置images[i-1]的图片作为背景
                imageView=new ImageView(getContext());
                imageView.setBackgroundResource(images[i-1]);
                mImageViewList.add(imageView);
            }
        }
    }

    //  设置轮播小圆点
    private void setDot() {
        //  设置LinearLayout的子控件的宽高，这里单位是像素。
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(15,15);
        params.rightMargin=20;
        //  for循环创建images.length个ImageView（小圆点）
        for(int i=0;i<images.length;i++){
            ImageView  imageViewDot=new ImageView(getContext());
            imageViewDot.setLayoutParams(params);
            //  设置小圆点的背景为暗红图片
            imageViewDot.setBackgroundResource(R.drawable.red_dot_night);
            mLinearLayoutDot.addView(imageViewDot);
            mImageViewDotList.add(imageViewDot);
        }
        //设置第一个小圆点图片背景为红色
        mImageViewDotList.get(dotPosition).setBackgroundResource(R.drawable.red_dot);
    }

    private void setViewPager() {
        MyPagerAdapter adapter=new MyPagerAdapter(mImageViewList);

        mViewPager.setAdapter(adapter);

        mViewPager.setCurrentItem(currentPosition);
        //页面改变监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){    //判断当切换到第0个页面时把currentPosition设置为images.length,即倒数第二个位置，小圆点位置为length-1
                    currentPosition=images.length;
                    dotPosition=images.length-1;
                }else if(position==images.length+1){    //当切换到最后一个页面时currentPosition设置为第一个位置，小圆点位置为0
                    currentPosition=1;
                    dotPosition=0;
                }else{
                    currentPosition=position;
                    dotPosition=position-1;
                }
                //  把之前的小圆点设置背景为暗红，当前小圆点设置为红色
                mImageViewDotList.get(prePosition).setBackgroundResource(R.drawable.red_dot_night);
                mImageViewDotList.get(dotPosition).setBackgroundResource(R.drawable.red_dot);
                prePosition=dotPosition;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //当state为SCROLL_STATE_IDLE即没有滑动的状态时切换页面
                if(state==ViewPager.SCROLL_STATE_IDLE){
                    mViewPager.setCurrentItem(currentPosition,false);
                }
            }
        });
    }
    //  设置自动播放
    private  void autoPlay() {
        new Thread(){
            @Override
            public void run() {
                super.run();

                while(threadstatus){
                    SystemClock.sleep(5000);
                    currentPosition++;
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }
    @Override
    public void onDestroyView(){
        Log.e("jinlaile","1");
        super.onDestroyView();
        threadstatus=false;
    }
}
