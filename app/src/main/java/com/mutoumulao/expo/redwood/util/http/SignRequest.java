package com.mutoumulao.expo.redwood.util.http;

import android.content.Context;

import com.mutoumulao.expo.redwood.base.BaseApp;
import com.mutoumulao.expo.redwood.util.SystemUtil;

import java.util.ArrayList;
import java.util.Locale;

import okhttp3.Headers;
import okhttp3.Request;


/**
 * 类描述：对Request进行签名，添加公共参数
 * 创建人：chenbo
 * 创建时间：2015/6/25 17:28
 */
public class SignRequest extends RequestParams {
    public static final String API_VERSION = "1.0.0";


    // 需要加入签名的header
    private final ArrayList<BasicNameValuePair> signHeaders = new ArrayList<>();

    private String mRequestTime;
    private String mRequestId;


    public SignRequest() {
        setDefaultPostType(TYPE_URL_ENCODE);
        Context context = BaseApp.getApp();
        addHeader("version", API_VERSION);
        signHeaders.add(new BasicNameValuePair("version", API_VERSION));

/*
        addHeader("app-version", SystemUtil.getVersionName(context));
        signHeaders.add(new BasicNameValuePair("app-version", SystemUtil.getVersionName(context)));
*/

        addHeader("device", SystemUtil.getIMEI(context));
        signHeaders.add(new BasicNameValuePair("device", SystemUtil.getIMEI(context)));
/*
        addHeader("screen-width", SystemUtil.getScreenWidth() + "");
        signHeaders.add(new BasicNameValuePair("screen-width", SystemUtil.getScreenWidth() + ""));

        addHeader("screen-height", SystemUtil.getScreenHeight() + "");
        signHeaders.add(new BasicNameValuePair("screen-height", SystemUtil.getScreenHeight() + ""));

        addHeader("platform", "1");
        signHeaders.add(new BasicNameValuePair("platform", "1"));

        addHeader("os-version", SystemUtil.getOSVersion());
        signHeaders.add(new BasicNameValuePair("os-version", SystemUtil.getOSVersion()));

        addHeader("model", SystemUtil.getDeviceModel());
        signHeaders.add(new BasicNameValuePair("model", SystemUtil.getDeviceModel()));

        ApplicationInfo appInfo = null;
        String channel;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            channel = appInfo.metaData.getString("MTA_CHANNEL");
        } catch (Exception e) {
            channel = "channel";
            e.printStackTrace();
        }
        addHeader("channel", channel);
        signHeaders.add(new BasicNameValuePair("channel", channel));

        mRequestId = UUID.randomUUID().toString();
        addHeader("request-id", mRequestId);

        mRequestTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ").format(new Date(System.currentTimeMillis()));
        addHeader("client-request-time", mRequestTime);

        if (!TextUtils.isEmpty(BaseResponse.sTimeDiff)) {
            addHeader("time-diff", BaseResponse.sTimeDiff);
        }
        */
    }


/*    // 生成签名
    private void genSign() {
        try {
            StringBuilder sb = new StringBuilder();
            // 签名开始
            sb.append(SIGN_HEAD);
            // 拼接请求唯一参数
            if (TextUtils.isEmpty(BaseResponse.sTimeDiff)) {
                sb.append(String.format("request-id=%s@client-request-time=%s\n", mRequestId, mRequestTime));
            } else {
                sb.append(String.format("request-id=%s@client-request-time=%s+time-diff=%s\n", mRequestId, mRequestTime, BaseResponse.sTimeDiff));
            }

            // 拼接url和params
            if (!TextUtils.isEmpty(path)) {
                Uri uri = Uri.parse(path);
                String path = uri.getPath();
                if (path != null) {
                    int index = path.indexOf("/api");
                    if (index > 0) {
                        path = path.substring(index);
                    }
                    sb.append(path.toLowerCase());
                }
            }
            sb.append("+");
            // 拼接header
            Collections.sort(signHeaders, new Comparator<BasicNameValuePair>() {
                @Override
                public int compare(BasicNameValuePair t0, BasicNameValuePair t1) {
                    return t0.getName().compareTo(t1.getName());
                }
            });
            StringBuilder paramsSb = new StringBuilder();
            for (BasicNameValuePair item : signHeaders) {
                String value = TextUtils.isEmpty(item.getValue()) ? "" : item.getValue();
                paramsSb.append(item.getName()).append("=").append(value).append("&");
            }
            if (paramsSb.toString().endsWith("&")) {
                paramsSb.deleteCharAt(paramsSb.length() - 1);
            }
            sb.append(paramsSb);
            sb.append("+");
            // 拼接query
            sb.append(getQueryString(true));
            // 拼接body
            if (TYPE_MULTI_PART.equals(contentType)) {
                Collections.sort(bodyParams, new Comparator<NameValuePair>() {
                    @Override
                    public int compare(NameValuePair t0, NameValuePair t1) {
                        return t0.getName().compareTo(t1.getName());
                    }
                });
                for (NameValuePair item : bodyParams) {
                    if (!TextUtils.isEmpty(item.getValue())) {
                        sb.append(item.getName()).append("=").append(item.getValue()).append("&");
                    }
                }
                if (sb.toString().endsWith("&")) {
                    sb.deleteCharAt(sb.length() - 1);
                }
            } else {
                buildBodyJsonText();
                String bodyStr = bodyText;
                if (!TextUtils.isEmpty(bodyStr)) {
                    sb.append(bodyStr);
                }
            }

            // 拼接key
            sb.append("\n");
            sb.append(APP_SECRET);

            String userKey = TextUtils.isEmpty(UserManager.getInstance().getUser().key) ? "" : UserManager.getInstance().getUser().key;
            String secret = TextUtils.isEmpty(userKey) ? APP_KEY : userKey;
            byte[] signatureByte = StringUtil.calculateRFC2104HMAC(sb.toString(), secret);
            String signature = new BASE64Encoder().encode(signatureByte);
//            LogUtils.d("bacy->" + sb.toString() + ",sign->" + signature + ",secret->" + secret);
            addHeader("signature", signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public Request toRequest() {
        //genSign();
        return super.toRequest();
    }
/*
    public void formatRequest() {
        Uri uri = Uri.parse(path);
        Set<String> paramSet = UrlUtil.getQueryParameterNames(Uri.parse(path));
        if (paramSet != null) {
            Iterator<String> iterator = paramSet.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = uri.getQueryParameter(key);
                if (!TextUtils.isEmpty(value)) {
                    addQueryStringParameter(key, value);
                }
            }
        }
    }*/

    public ArrayList<BasicNameValuePair> getSignHeaders() {
        //formatRequest();
        Request request = toRequest();
        Headers headers = request.headers();
        ArrayList<BasicNameValuePair> pairs = new ArrayList<>();
        for (int i = 0, size = headers.size(); i < size; i++) {
            String name = headers.name(i).toLowerCase(Locale.US);
            pairs.add(new BasicNameValuePair(name, headers.value(i)));
        }
        return pairs;
    }

}
