package com.example.suyeq.dangyuliveapp.createlive;

import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.suyeq.dangyuliveapp.AppApplication;
import com.example.suyeq.dangyuliveapp.R;
import com.example.suyeq.dangyuliveapp.living.AnchorLivingActivity;
import com.example.suyeq.dangyuliveapp.model.RoomInfo;
import com.example.suyeq.dangyuliveapp.utils.ImgUtils;
import com.example.suyeq.dangyuliveapp.utils.PicChooserHelper;
import com.tencent.TIMUserProfile;

public class CreateActivity extends AppCompatActivity {
    private View mSetCoverView;
    private ImageView mCoverImg;
    private TextView mCoverTipTxt;
    private EditText mTitleEt;
    private TextView mCreateRoomBtn;
    private TextView mRoomNoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        findAllViews();
        setListeners();
        setupTitlebar();
    }
    private void findAllViews() {
        mSetCoverView = findViewById(R.id.set_cover);
        mCoverImg = (ImageView) findViewById(R.id.cover);
        mCoverTipTxt = (TextView) findViewById(R.id.tv_pic_tip);
        mTitleEt = (EditText) findViewById(R.id.title);
        mCreateRoomBtn = (TextView) findViewById(R.id.create);
        //mRoomNoText = (TextView) findViewById(R.id.room_no);
    }

    private void setListeners() {
        mSetCoverView.setOnClickListener(clickListener);
        mCreateRoomBtn.setOnClickListener(clickListener);
    }

    private void setupTitlebar() {
        Toolbar titlebar = (Toolbar) findViewById(R.id.titlebar);
        titlebar.setTitle("创建直播");
        //titlebar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(titlebar);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.create) {
                //创建直播
                requestCreateRoom();
            } else if (id == R.id.set_cover) {
                //选择图片
                choosePic();
            }
        }
    };

    private void requestCreateRoom() {
        CreateRoomRequest.CreateRoomParam param = new CreateRoomRequest.CreateRoomParam();
        TIMUserProfile selfProfile = AppApplication.getApplication().getSelfProfile();
        param.userId = selfProfile.getIdentifier();
        param.userAvatar = selfProfile.getFaceUrl();
        String nickName = selfProfile.getNickName();
        param.userName = TextUtils.isEmpty(nickName) ? selfProfile.getIdentifier() : nickName;
        param.liveTitle = mTitleEt.getText().toString();
        param.liveCover = coverUrl;
        //创建房间
        CreateRoomRequest request = new CreateRoomRequest();
        request.setOnResultListener(new CreateRoomRequest.OnResultListener<RoomInfo>() {
            @Override
            public void onFail() {
                Toast.makeText(CreateActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(RoomInfo roomInfo) {
                Toast.makeText(CreateActivity.this, "请求成功：" + roomInfo.getRoomId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(CreateActivity.this, AnchorLivingActivity.class);
                intent.putExtra("roomId", roomInfo.getRoomId());
                startActivity(intent);
                finish();
            }
        });
        request.createRoom(param);
    }

    private PicChooserHelper mPicChooserHelper;

    private void choosePic() {
        if (mPicChooserHelper == null) {
            mPicChooserHelper = new PicChooserHelper(this, PicChooserHelper.PicType.Cover);
            mPicChooserHelper.setOnChooseResultListener(new PicChooserHelper.OnChooseResultListener() {
                @Override
                public void onSuccess(String url) {
                    //获取图片成功
                    updateCover(url);
                }

                @Override
                public void onFail(String msg) {
                    //获取图片失败
                    Toast.makeText(CreateActivity.this,"选择失败：" + msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
        mPicChooserHelper.showPicChooserDialog();
    }

    private String coverUrl = null;

    private void updateCover(String url) {
        //Log.e("cuowu","tttt");
        coverUrl = url;
        ImgUtils.load(url, mCoverImg);
        mCoverTipTxt.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mPicChooserHelper != null) {
            mPicChooserHelper.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onResume(){
        super.onResume();

    }
}
