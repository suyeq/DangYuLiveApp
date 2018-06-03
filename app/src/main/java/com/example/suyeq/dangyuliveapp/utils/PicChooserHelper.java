package com.example.suyeq.dangyuliveapp.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.example.suyeq.dangyuliveapp.AppApplication;
import com.example.suyeq.dangyuliveapp.editprofile.EditProFileActivity;
import com.example.suyeq.dangyuliveapp.widget.PicChooseDialog;
import com.qiniu.android.http.ResponseInfo;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;

import java.io.File;
import java.io.IOException;




public class PicChooserHelper {

    private Activity mActivity;
    private Fragment mFragment;
    private static final int FROM_CAMERA = 2;
    private static final int FROM_ALBUM = 1;
    private static final int CROP = 0;
    private TIMUserProfile mUserProfile;

    private Uri mCameraFileUri;
    private PicType mPicType;

    public static enum PicType {
        Avatar, Cover
    }

    public PicChooserHelper(Activity activity,PicType picType) {
        mActivity = activity;
        mPicType = picType;
        mUserProfile = AppApplication.getApplication().getSelfProfile();
    }

   public PicChooserHelper(Fragment fragment,PicType picType) {
        mFragment = fragment;
        mActivity = fragment.getActivity();
        mPicType = picType;
        mUserProfile = AppApplication.getApplication().getSelfProfile();
    }

    public void showPicChooserDialog() {
        PicChooseDialog dialog = new PicChooseDialog(mActivity);
        dialog.setOnDialogClickListener(new PicChooseDialog.OnDialogClickListener() {
            @Override
            public void onCamera() {
                //拍照
                takePicFromCamera();
            }

            @Override
            public void onAlbum() {
                //相册
                takePicFromAlbum();
            }
        });
        dialog.show();
    }

    private void takePicFromAlbum() {
        Intent picIntent = new Intent("android.intent.action.GET_CONTENT");
        picIntent.addCategory(Intent.CATEGORY_OPENABLE);
        picIntent.setType("image/*");
        if (mFragment == null) {
            mActivity.startActivityForResult(picIntent, FROM_ALBUM);
       } else {
           mFragment.startActivityForResult(picIntent, FROM_ALBUM);
        }
    }

    private void takePicFromCamera() {
        mCameraFileUri = createAlbumUri();


       Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < 24) {
            //小于7.0的版本
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, mCameraFileUri);
            if (mFragment == null) {
                mActivity.startActivityForResult(intentCamera, FROM_CAMERA);
            } else {
                mFragment.startActivityForResult(intentCamera, FROM_CAMERA);
            }
       } else {
            //大于7.0的版本
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, mCameraFileUri.getPath());
            Uri uri = getImageContentUri(mCameraFileUri);
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            if (mFragment == null) {
                mActivity.startActivityForResult(intentCamera, FROM_CAMERA);
            } else {
               mFragment.startActivityForResult(intentCamera, FROM_CAMERA);
           }
       }

    }

    private Uri createAlbumUri() {
        String dirPath = Environment.getExternalStorageDirectory() + "/" + mActivity.getApplication().getApplicationInfo().packageName;
        File dir = new File(dirPath);
        if (!dir.exists()||dir.isFile()) {
            dir.mkdirs();
        }
        String id = "";
        if (mUserProfile != null) {
            id = mUserProfile.getIdentifier();
        }
        String fileName = id + ".jpg";
        File picFile = new File(dirPath, fileName);
        if (picFile.exists()) {
            picFile.delete();
        }

        return Uri.fromFile(picFile);

    }


    /**
     * 转换 content:// uri
     */
    public Uri getImageContentUri(Uri uri) {
        String filePath = uri.getPath();
        Cursor cursor = mActivity.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, filePath);
            return mActivity.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FROM_CAMERA) {
            //从相册选择返回。
            if (resultCode == Activity.RESULT_OK) {
                startCrop(mCameraFileUri);
            }
        } else if (requestCode == FROM_ALBUM) {
            //从相册选择返回。
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                /*TIMFriendshipManager.getInstance().setFaceUrl(uri.getPath(),new TIMCallBack(){
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(mActivity, "修改头像失败", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onSuccess() {
                        Toast.makeText(mActivity, "修改头像成功", Toast.LENGTH_SHORT).show();
                    }
                });*/
                startCrop(uri);
            }
        } else if (requestCode == CROP) {
            //裁剪结束
            if (resultCode == Activity.RESULT_OK) {
                //上传到服务器保存起来
                //七牛上传
                uploadTo7Niu(cropUri.getPath());
                /*TIMFriendshipManager.getInstance().setFaceUrl(cropUri.getPath(),new TIMCallBack(){
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(mActivity, "修改头像失败", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onSuccess() {
                        Toast.makeText(mActivity, "修改头像成功", Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        }
    }

    private Uri cropUri = null;


    private void startCrop(Uri uri) {//裁剪
        cropUri = createCropUri();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.putExtra("crop", "true");//是否裁剪
        Log.e("cuowu","kkkk");
        if(mPicType == PicType.Avatar) {
            intent.putExtra("aspectX", 300);
            intent.putExtra("aspectY", 300);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
        }else if(mPicType == PicType.Cover){
            intent.putExtra("aspectX", 500);
           intent.putExtra("aspectY", 300);
            intent.putExtra("outputX", 500);
            intent.putExtra("outputY", 300);
        }
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
       if (currentapiVersion < 24) {
            //小于7.0的版本
            intent.setDataAndType(uri, "image/*");
           // intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
       }else {
            //大于7.0的版本
               String scheme = uri.getScheme();
                if (scheme.equals("content")) {
                    Uri contentUri = uri;
                    intent.setDataAndType(contentUri, "image/*");
                } else {
                    Uri contentUri = getImageContentUri(uri);
                    intent.setDataAndType(contentUri, "image/*");
                }
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
            if (mFragment == null) {
                mActivity.startActivityForResult(intent, CROP);
            } else {
                mFragment.startActivityForResult(intent, CROP);
            }
    }

    private Uri createCropUri() {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + mActivity.getApplication().getApplicationInfo().packageName;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            boolean l=dir.mkdirs() ;
            Log.e("asdf",l+"");
        }
        Log.e("asdf",dir.getAbsolutePath());
        String id = "";
        if (mUserProfile != null) {
            id = mUserProfile.getIdentifier();
        }
        String fileName = id +System.currentTimeMillis()+ "_crop.jpg";
        File picFile = new File(dirPath, fileName);
        Log.e("asdf",picFile.getAbsolutePath());
        if (picFile.exists()) {
            Log.e("wenjian","jinlaile");
            picFile.delete();
        }
        try {
            if(picFile.createNewFile()){
                Log.e("wenjian","1");
            }else{
                Log.e("wenjian","2");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Uri.fromFile(picFile);
    }

    private void uploadTo7Niu(String path) {
        String id = "";
        if (mUserProfile != null) {
            id = mUserProfile.getIdentifier();
        }
        String name = id + "_" + System.currentTimeMillis() + "_avatar";
        QnUploadHelper.uploadPic(path, name, new QnUploadHelper.UploadCallBack() {

            @Override
            public void success(String url) {
                //上传成功
                if (mOnChooserResultListener != null) {
                    mOnChooserResultListener.onSuccess(url);
                }
            }

            @Override
            public void fail(String key, ResponseInfo info) {
                //上传失败！
                if (mOnChooserResultListener != null) {
                    mOnChooserResultListener.onFail(info.error);
                }
            }
        });
    }

    public interface OnChooseResultListener {
        void onSuccess(String url);

        void onFail(String msg);
    }

    private OnChooseResultListener mOnChooserResultListener;

    public void setOnChooseResultListener(OnChooseResultListener l) {
        mOnChooserResultListener = l;
    }
}
