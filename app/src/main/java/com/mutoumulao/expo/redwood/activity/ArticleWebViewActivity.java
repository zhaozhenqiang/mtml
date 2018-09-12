package com.mutoumulao.expo.redwood.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.logic.HomeManager;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzy on 2018/9/3.
 */

public class ArticleWebViewActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout mRlBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    private WebView webView;
    private String url = "";
    private EditText mEtMessage;
    private String mId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_webview);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webview);
        mEtMessage = (EditText) findViewById(R.id.et_message);
        mTvTitle.setText("文章详细");
        mId = getIntent().getStringExtra("id");
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();

        // 得到设置属性的对象
        WebSettings webSettings = webView.getSettings();
        // 使能JavaScript
        webSettings.setJavaScriptEnabled(true);
        // 支持中文，否则页面中中文显示乱码
        webSettings.setDefaultTextEncodingName("GBK");
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // 限制在WebView中打开网页，而不用默认浏览器
        webView.setWebViewClient(new WebViewClient());

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
//                    dismissDialog();
                    UIUtil.hideProgressBar(ArticleWebViewActivity.this);
                } else {
                    UIUtil.showProgressBar(ArticleWebViewActivity.this);
//                    showWaitDialog("", false, null);
                }
            }

        });

        // 如果不设置这个，JS代码中的按钮会显示，但是按下去却不弹出对话框r
        // Sets the chrome handler. This is an implementation of WebChromeClient
        // for use in handling JavaScript dialogs, favicons, titles, and the
        // progress. This will replace the current handler.


        load();

        mEtMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String content = mEtMessage.getText().toString().trim();
                    if (!TextUtils.isEmpty(content)) {
                        sendMessage(mId, content);
                    } else {
                        UIUtil.toastShort(ArticleWebViewActivity.this, "请输入评论内容");
                    }
                }
                return false;
            }
        });
    }

    private void sendMessage(final String id, final String content) {
        HomeManager.getInstance().SendComment(id, content, "", new ResponseCallback() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                mEtMessage.setText("");
                webView.reload();

            }

        });
    }

    private void load(){
        url = "http://mutoumulao.com:8888/api/article/detail?id=" + mId;/*CommonConstants.H5_SHOP + "user_id=" + user_id + "&" + "company_id=" + company_id
                + "&" + "one=" + time + "two=" + Md5Util.md5HexString(SignUtil.encryptToSHA(time));*/
        webView.addJavascriptInterface(new WebAppInterface(this), "myInterfaceName");
        webView.loadUrl(url);
    }

    @OnClick(R.id.rl_back)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
        }
    }


    /**
     * 自定义的Android代码和JavaScript代码之间的桥梁类
     *
     * @author 1
     */
    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        // 如果target 大于等于API 17，则需要加上如下注解
        @JavascriptInterface
        public void call() {
          /*  Intent intent = new Intent();
            intent.setClass(this, CoinPayActivity.class);
            startActivity(intent);*/
        }

        @JavascriptInterface
        public void call1() {
            finish();
        }
    }

    @Override
    protected synchronized void onDestroy() {

        if (webView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }

            webView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();

            try {
                webView.destroy();
            } catch (Throwable ex) {

            }
        }
        super.onDestroy();
    }
}
