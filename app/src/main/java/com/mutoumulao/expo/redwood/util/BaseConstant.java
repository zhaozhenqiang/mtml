package com.mutoumulao.expo.redwood.util;

import android.os.Environment;


import com.mutoumulao.expo.redwood.BuildConfig;

import java.io.File;

public class BaseConstant {
    /**
     * 日志的tag前缀
     */
    public static final String LOG_PREFIX = "lzy->";

    public static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    /**
     * 本地文件保存的根目录
     */
    public static final String BASE_LOCAL_PATH = Environment.getExternalStorageDirectory().getPath() + "/" + BuildConfig.APPLICATION_ID;
    /**
     * 设备DeviceID存储文件
     */
    public static final String DEVICE_ID_PATH = BASE_LOCAL_PATH + "/.id";
    /**
     * 保存日志文件的目录
     */
    public static final String LOG_PATH = BASE_LOCAL_PATH + "/log";
    /**
     * 保存临时文件的目录
     */
    public static final String TEMP_PATH = BASE_LOCAL_PATH + "/temp";
    /**
     * 下载的文件保存路径
     */
    public static final String SAVE_PATH = BASE_LOCAL_PATH + "/save";

    /**
     * 缓存文件目录
     */
    public static final String CACHE_PATH = BASE_LOCAL_PATH + "/cache";

    /**
     * Http加载操作类型
     */
    public static final int TYPE_INIT = 0;
    public static final int TYPE_RELOAD = 1;
    public static final int TYPE_NEXT = 2;

    public static final int REQUEST_CODE_PICK = 3001;
    public static final int REQUEST_CODE_TAKE_PHOTO = 3002;
    public static final int REQUEST_CODE_CROP = 3003;

    public static final String ACTION_PICK_PHOTO = ".PICK_PHOTO";

    public static final String KEY_RESPONSE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALqFImjo8C8C3FpHFDmoJzpdQvfR9pcAyy5vg+BctpNwmIM8SbfkRGFxxTx/hJfmJ5auMFfsKkd8j4srA3NCgUGGf6eB8TySYKmV6mMvWlMrpjgVGB0rElV3W7PJr0brsoHwrlBDhSPeUzDEt7P9IN2A+aPLtfazbea2W22FA5ItAgMBAAECgYEAhwYoAdBXR4EHqab5AkAznbGz8BkkLO5bKBN8YWhcl2GUVrTHHQN3aR9mTER35Uqs8AzLXGrPtI58j5+k0MSdMmadoCXG9U7wcnCaik5mTf16I5390QGcAAdB3mdY7oITLIKsz4pGMSOpk0CgvkXglJ49LtRzm8iRcboggxAsWXUCQQD2L0Ua9eYF/y7fYU+D3l6LyFf2/CDvDhUkzrL82ybqDb25SD/zzOo8zV9VL7ZAd8T8FRzvbNw8n0/a+EWq6I57AkEAwfTi9WWAvse/KQ3E46s+Wbq97v609mMitUaPk2RvYBn2nCfPOvwEgNMnPxCYdQDMgeIASZmHWndbVipbXQfVdwJAHXRAX15mO/dxAzbgTZWwWCcLJzi5NADKVNIKJiiOOliUh3N2e1Pb/pRPwKBpvMLXpZVdFeQ/YV1qL3ee1jjmuwJAQTx31eglDIYswscx0Q248/8+gRNElJa1htlL01x1pZI2A0HUjtdTQG1FBw4y6S+ymYEFbbvo7cG1g97NShYncwJBALWxgscXRzCumuh1LxAmQBpyRM/Nqf0+YEB3kWDILgGsxL/t8jiFS16dDehpmXmtnvCmkJCiekGr1V7SFlW/0XQ=";

}
