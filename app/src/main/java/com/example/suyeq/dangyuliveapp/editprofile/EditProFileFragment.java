package com.example.suyeq.dangyuliveapp.editprofile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.suyeq.dangyuliveapp.AppApplication;
import com.example.suyeq.dangyuliveapp.MainActivity;
import com.example.suyeq.dangyuliveapp.R;
import com.example.suyeq.dangyuliveapp.utils.ImgUtils;
import com.example.suyeq.dangyuliveapp.utils.PicChooserHelper;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendGenderType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;

import java.util.Map;

/**
 * Created by Suyeq on 2018/5/22.
 */

public class EditProFileFragment extends Fragment {
    private Toolbar mTitlebar;
    private View mAvatarView;
    private ImageView mAvatarImg;
    private ProfileEdit mNickNameEdt;
    private ProfileEdit mGenderEdt;
    private ProfileEdit mSignEdt;
    private ProfileEdit mRenzhengEdt;
    private ProfileEdit mLocationEdt;

    private ProfileTextView mIdView;
    private ProfileTextView mLevelView;
    private ProfileTextView mGetNumsView;
    private ProfileTextView mSendNumsView;
    private Button mCompleteBtn;
    private TIMUserProfile userProfile;
    private PicChooserHelper mPicChooserHelper;
    private Activity activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.fragment_edit_pro_file,container,false);
        activity=getActivity();
        findAllViews(view);
        setListeners();
        setTitleBar();
        setIconKey();//设置字段和icon
        //updateView(userProfile);
        getSelfInfo();
        /*SharedPreferences sp=getContext().getSharedPreferences("FirstLogin", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean("firstLogin",false);
        editor.commit();*/
        return  view;
    }
    private void updateView(TIMUserProfile userProfile) {
        String faceUrl = userProfile.getFaceUrl();
        Log.e("tupian",faceUrl+"");
        if (TextUtils.isEmpty(faceUrl)) {
            ImgUtils.loadRound(R.drawable.dog, mAvatarImg);
        } else {
            ImgUtils.loadRound(faceUrl, mAvatarImg);
        }
        mNickNameEdt.updateValue(userProfile.getNickName());
        long genderValue = userProfile.getGender().getValue();
        String genderStr = genderValue == 1 ? "男" : "女";
        mGenderEdt.updateValue(genderStr);
        mSignEdt.updateValue(userProfile.getSelfSignature());
        mLocationEdt.updateValue(userProfile.getLocation());
        mIdView.updateValue(userProfile.getIdentifier());

        /*Map<String, byte[]> customInfo = userProfile.getCustomInfo();
        mRenzhengEdt.updateValue(getValue(customInfo, CustomProfile.CUSTOM_RENZHENG, "未知"));
        mLevelView.updateValue(getValue(customInfo, CustomProfile.CUSTOM_LEVEL, "0"));
        mGetNumsView.updateValue(getValue(customInfo, CustomProfile.CUSTOM_GET, "0"));
        mSendNumsView.updateValue(getValue(customInfo, CustomProfile.CUSTOM_SEND, "0"));*/
    }

    private void setIconKey() {
        ImgUtils.loadRound(R.drawable.dog, mAvatarImg);
        mNickNameEdt.set(R.drawable.ic_info_nickname, "昵称", "溯夜");
        mGenderEdt.set(R.drawable.ic_info_gender, "性别", "男");
        mSignEdt.set(R.drawable.ic_info_sign, "签名", "无");
        mRenzhengEdt.set(R.drawable.ic_info_renzhen, "认证", "无");
        mLocationEdt.set(R.drawable.ic_info_location, "地区", "无");
        mIdView.set(R.drawable.ic_info_id, "ID", "0");
        mLevelView.set(R.drawable.ic_info_level, "等级", "0");
        mGetNumsView.set(R.drawable.ic_info_get, "获得票数", "0");
        mSendNumsView.set(R.drawable.ic_info_send, "送出票数", "0");
    }

    private void findAllViews(View view) {
        mTitlebar = (Toolbar) view.findViewById(R.id.title_bar);

        mAvatarView = view.findViewById(R.id.avatar);
        mAvatarImg = (ImageView) view.findViewById(R.id.avatar_img);
        mNickNameEdt = (ProfileEdit) view.findViewById(R.id.nick_name);
        mGenderEdt = (ProfileEdit) view.findViewById(R.id.gender);
        mSignEdt = (ProfileEdit) view.findViewById(R.id.sign);
        mRenzhengEdt = (ProfileEdit) view.findViewById(R.id.renzheng);
        mLocationEdt = (ProfileEdit) view.findViewById(R.id.location);

        mIdView = (ProfileTextView) view.findViewById(R.id.id);
        mLevelView = (ProfileTextView) view.findViewById(R.id.level);
        mGetNumsView = (ProfileTextView) view.findViewById(R.id.get_nums);
        mSendNumsView = (ProfileTextView) view.findViewById(R.id.send_nums);

        mCompleteBtn = (Button) view.findViewById(R.id.complete);
    }

    private void setListeners() {
        mAvatarView.setOnClickListener(clickListener);
        mNickNameEdt.setOnClickListener(clickListener);
        mGenderEdt.setOnClickListener(clickListener);
        mSignEdt.setOnClickListener(clickListener);
        mRenzhengEdt.setOnClickListener(clickListener);
        mLocationEdt.setOnClickListener(clickListener);
        mCompleteBtn.setOnClickListener(clickListener);
    }

    private void setTitleBar() {
        mTitlebar.setTitle("编辑个人信息");
        mTitlebar.setTitleTextColor(Color.WHITE);
        Activity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).setSupportActionBar(mTitlebar);
        }
    }
    private String getValue(Map<String, byte[]> customInfo, String key, String defaultValue) {
        if (customInfo != null) {
            byte[] valueBytes = customInfo.get(key);
            if (valueBytes != null) {
                return new String(valueBytes);
            }
        }
        return defaultValue;
    }
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.avatar) {
                //修改头像
                choosePic();
            } else if (id == R.id.nick_name) {
                //修改昵称
                showEditNickNameDialog(activity);
            } else if (id == R.id.gender) {
                //修改性别
                showEditGenderDialog(activity);
            } else if (id == R.id.sign) {
                //修改签名
                showEditSignDialog(activity);
            } else if (id == R.id.renzheng) {
                //修改认证
                showEditRenzhengDialog(activity);
            } else if (id == R.id.location) {
                //修改位置
                showEditLocationDialog(activity);
            } else if (id == R.id.complete) {
                //完成，点击跳转到主界面
                Intent intent = new Intent();
                intent.setClass(getContext(), MainActivity.class);
                startActivity(intent);

            }
        }
    };

    private void showEditLocationDialog(Activity activity) {
        EditStrProfileDialog dialog = new EditStrProfileDialog(activity);
        dialog.setOnOKListener(new EditStrProfileDialog.OnOKListener() {
            @Override
            public void onOk(String title, final String content) {
                TIMFriendshipManager.getInstance().setLocation(content, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(getContext(), "更新地区失败：" + s, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onSuccess() {
                        //更新成功
                        getSelfInfo();
                    }
                });
            }
        });
        dialog.show("地区", R.drawable.ic_info_location, mLocationEdt.getValue());
    }

    private void getSelfInfo() {
        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {
                Toast.makeText(getContext(), "获取信息失败：" + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(TIMUserProfile timUserProfile) {
                //获取自己信息成功
                userProfile = timUserProfile;
                updateView(timUserProfile);
                AppApplication.getApplication().setSelfProfile(timUserProfile);
            }
        });
    }
    private void showEditSignDialog(Activity activity) {
        EditStrProfileDialog dialog = new EditStrProfileDialog(activity);
        dialog.setOnOKListener(new EditStrProfileDialog.OnOKListener() {
            @Override
            public void onOk(String title, final String content) {
                TIMFriendshipManager.getInstance().setSelfSignature(content, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(getContext(), "更新签名失败：" + s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess() {
                        //更新成功
                        getSelfInfo();
                    }
                });
            }
        });
        dialog.show("签名", R.drawable.ic_info_sign, mSignEdt.getValue());
    }

    private void showEditGenderDialog(Activity activity) {
        EditGenderDialog dialog = new EditGenderDialog(activity);
        dialog.setOnChangeGenderListener(new EditGenderDialog.OnChangeGenderListener() {
            @Override
            public void onChangeGender(boolean isMale) {
                TIMFriendGenderType gender = isMale ? TIMFriendGenderType.Male : TIMFriendGenderType.Female;
                TIMFriendshipManager.getInstance().setGender(gender, new TIMCallBack() {

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(getContext(), "更新性别失败：" + s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess() {
                        //更新成功
                        getSelfInfo();
                    }
                });
            }
        });
        dialog.show(mGenderEdt.getValue().equals("男"));
    }

    private void showEditRenzhengDialog(Activity activity) {
        EditStrProfileDialog dialog = new EditStrProfileDialog(activity);
        dialog.setOnOKListener(new EditStrProfileDialog.OnOKListener() {
            @Override
            public void onOk(String title, final String content) {
                TIMFriendshipManager.getInstance().setCustomInfo(CustomProfile.CUSTOM_RENZHENG, content.getBytes(), new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(getContext(), "更新认证失败：" + s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess() {
                        //更新成功
                        getSelfInfo();
                    }
                });
            }
        });
        dialog.show("认证", R.drawable.ic_info_renzhen, mRenzhengEdt.getValue());
    }

    private void showEditNickNameDialog(Activity activity) {
        EditStrProfileDialog dialog = new EditStrProfileDialog(activity);
        dialog.setOnOKListener(new EditStrProfileDialog.OnOKListener() {
            @Override
            public void onOk(String title, final String content) {
                TIMFriendshipManager.getInstance().setNickName(content, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(getContext(), "更新昵称失败：" + s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess() {
                        //更新成功
                        getSelfInfo();
                    }
                });
            }
        });
        dialog.show("昵称", R.drawable.ic_info_nickname, mNickNameEdt.getValue());
    }
    private void choosePic() {
        if (mPicChooserHelper == null) {
            mPicChooserHelper = new PicChooserHelper(this, PicChooserHelper.PicType.Avatar);
            mPicChooserHelper.setOnChooseResultListener(new PicChooserHelper.OnChooseResultListener() {
                @Override
                public void onSuccess(String url) {
                    updateAvatar(url);

                }

                @Override
                public void onFail(String msg) {
                    Toast.makeText(getContext(), "选择失败：" + msg, Toast.LENGTH_SHORT).show();
                }
            });
        }

        mPicChooserHelper.showPicChooserDialog();
    }
    private void updateAvatar(String url) {
        TIMFriendshipManager.getInstance().setFaceUrl(url, new TIMCallBack() {

            @Override
            public void onError(int i, String s) {
                Toast.makeText(getContext(), "头像更新失败：" + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                //更新头像成功
                getSelfInfo();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mPicChooserHelper != null) {
            mPicChooserHelper.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
    @Override
    public void onResume(){
        super.onResume();
        getSelfInfo();
    }

}
