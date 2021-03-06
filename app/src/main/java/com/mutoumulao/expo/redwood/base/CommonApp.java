package com.mutoumulao.expo.redwood.base;

import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.mutoumulao.expo.redwood.util.BaseConstant;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by lzy on 2018/7/25.
 */

public class CommonApp extends MultiDexApplication {

    private static CommonApp sApp;
    private boolean mIsStarted;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        init();
    }


    public static CommonApp getApp() {
        return sApp;
    }

    public void onInit() {}

    public void onStart() {}

    public void onStop() {}

    public final void start() {
        if (!mIsStarted) {
            mIsStarted = true;
            onStart();
        }
    }

    /**
     * 程序退出时候需要调用该方法，释放一些无用资源
     * stop
     */
    public final void stop() {
        mIsStarted = false;
        onStop();
    }

    private final void init() {
        if (shouldInit()) {
            Thread.setDefaultUncaughtExceptionHandler(new ExHandler(Thread.getDefaultUncaughtExceptionHandler()));
            onInit();
        }
    }

    // 捕获程序崩溃的异常,记录log(可以考虑将异常信息发回服务器)
    private class ExHandler implements Thread.UncaughtExceptionHandler {
        private Thread.UncaughtExceptionHandler internal;

        private ExHandler(Thread.UncaughtExceptionHandler eh) {
            internal = eh;
        }

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            File file = new File(BaseConstant.LOG_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String fname = sdf.format(new Date());
            try {
                PrintStream ps = new PrintStream(file.getAbsolutePath() + "/" + fname);
                ps.println(ex.getMessage());
                ex.printStackTrace(ps);
                ps.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            internal.uncaughtException(thread, ex);
        }
    }

    // 判断是否主进程启动App
    private boolean shouldInit() {
        android.app.ActivityManager am = ((android.app.ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
