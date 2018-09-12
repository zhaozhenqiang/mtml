package com.mutoumulao.expo.redwood.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;


/**
 * 倒计时
 */
public class CountDown extends CountDownTimer {

    private TextView btnVerification;
    private Context mContext;

    public CountDown(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);

    }

    public CountDown(TextView button, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.btnVerification = button;
    }

    @Override
    public void onFinish() {
       // btnVerification.setBackgroundColor(Color.parseColor("#FF5533"));
        btnVerification.setText("重新获取");
        btnVerification.setClickable(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        //btnVerification.setBackgroundColor(Color.parseColor("#cdcdcd"));
        btnVerification.setClickable(false);
        btnVerification.setText(millisUntilFinished / 1000 + "秒后重新获取");

    }
}
