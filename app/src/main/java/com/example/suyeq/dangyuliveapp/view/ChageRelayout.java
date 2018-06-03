package com.example.suyeq.dangyuliveapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ChageRelayout extends RelativeLayout {

    public ChageRelayout(Context context) {
        super(context);
    }

    public ChageRelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChageRelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mOnSizeChangeListener == null) {
            return;
        }
        if (h > oldh) {
            //画面变长，键盘隐藏
            mOnSizeChangeListener.onHide();
        } else {
            //画面变短，键盘显示
            mOnSizeChangeListener.onShow();
        }
    }

    private OnSizeChangeListener mOnSizeChangeListener;

    public void setOnSizeChangeListener(OnSizeChangeListener l) {
        mOnSizeChangeListener = l;
    }

    public interface OnSizeChangeListener {
        public void onHide();

        public void onShow();
    }
}
