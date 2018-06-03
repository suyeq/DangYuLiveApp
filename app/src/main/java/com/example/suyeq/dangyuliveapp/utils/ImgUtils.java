package com.example.suyeq.dangyuliveapp.utils;

import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.suyeq.dangyuliveapp.AppApplication;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Suyeq on 2018/5/17.
 */
public class ImgUtils {

    public static void load(String url, ImageView targetView) {
        Glide.with(AppApplication.getContext())
                .load(url)
                .into(targetView);
    }

    public static void load(int resId, ImageView targetView) {
        Glide.with(AppApplication.getContext())
                .load(resId)
                .into(targetView);
    }

    public static void loadRound(String url, ImageView targetView) {
        Glide.with(AppApplication.getContext())
                .load(url)
                .bitmapTransform(new CropCircleTransformation(AppApplication.getContext()))
                .into(targetView);
    }

    public static void loadRound(int resId, ImageView targetView) {
        Glide.with(AppApplication.getContext())
                .load(resId)
                .bitmapTransform(new CropCircleTransformation(AppApplication.getContext()))
                .into(targetView);
    }

}
