package com.mutoumulao.expo.redwood.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.hyphenate.easeui.EaseUI;
import com.mutoumulao.expo.redwood.hx.DemoHelper;
import com.umeng.socialize.PlatformConfig;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by lzy on 2018/7/25.
 */

public class BaseApp extends CommonApp {

    @Override
    public void onCreate() {
        super.onCreate();
        disableChecks(this);
        handleSSLHandshake();
        initUmengSdk();
        //init demo helper
        DemoHelper.getInstance().init(this);
        EaseUI.getInstance().init(this, null);
    }

    @Override
    public void onInit() {
        super.onInit();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    public static void disableChecks(Context context) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new TrustAllTrustManager()}, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    private void initUmengSdk() {
        //UMShareAPI.get(this);
        // Config.DEBUG=true;
        //微信    wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
        PlatformConfig.setWeixin("wx84bd33fbc16c10b7", "3e3f278c205f16d84d88618d24d59b4e");
        //新浪微博
        PlatformConfig.setSinaWeibo("925069597", "7abd649d562a8e07a0356aecd338aa27", "http://www.mutoumulao.com");
        //QQZone
        PlatformConfig.setQQZone("1106820382", "C3s5u6vE2gpqwSgn");

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
