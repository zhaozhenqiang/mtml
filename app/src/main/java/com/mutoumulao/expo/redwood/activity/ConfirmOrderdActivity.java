package com.mutoumulao.expo.redwood.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.ConfirmOrderdAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.AddressEntity;
import com.mutoumulao.expo.redwood.entity.PayEntity;
import com.mutoumulao.expo.redwood.entity.ShoppingEntity;
import com.mutoumulao.expo.redwood.entity.WXModel;
import com.mutoumulao.expo.redwood.entity.WxSignEntity;
import com.mutoumulao.expo.redwood.entity.event.BackConfireOrderEvent;
import com.mutoumulao.expo.redwood.logic.OrderManager;
import com.mutoumulao.expo.redwood.logic.ShoppingManager;
import com.mutoumulao.expo.redwood.util.StringUtil;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.alipay.OrderInfoUtil2_0;
import com.mutoumulao.expo.redwood.util.alipay.PayResult;
import com.mutoumulao.expo.redwood.util.http.FinalResponseCallback;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;
import com.mutoumulao.expo.redwood.view.ToggleButton;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzy on 2018/8/19.
 */

public class ConfirmOrderdActivity extends BaseActivity {
    private static final int SELECT_ADDRESS = 102;

    @BindView(R.id.rl_back)
    RelativeLayout mRlBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.rl_right)
    RelativeLayout mRlRight;
    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.rl_address)
    LinearLayout mLlAddress;
    @BindView(R.id.iv_goods)
    BaseRecyclerView mRvGoods;
    @BindView(R.id.switch_notify)
    ToggleButton mSwitchNotify;
    @BindView(R.id.tv_all_price)
    TextView mTvAllPrice;
    @BindView(R.id.tv_transport_price)
    TextView mTvTransportPrice;
    @BindView(R.id.tv_order_price)
    TextView mTvOrderPrice;
    @BindView(R.id.tv_notice)
    TextView mTvNotice;
    @BindView(R.id.rl_distribution_type)
    LinearLayout mLlDistributionType;
    @BindView(R.id.iv_image1)
    ImageView mIvImage1;
    @BindView(R.id.ll_weixin)
    LinearLayout mLlWeixin;
    @BindView(R.id.iv_image2)
    ImageView mIvImage2;
    @BindView(R.id.ll_zhifubao)
    LinearLayout mLlZhifubao;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    private String mAddress_id;
    private List<ShoppingEntity> mGood_list = new ArrayList<>();
    private String isDaigou = "1";//(0:否，1:是)
    private String paytype = "wx";
    String orderCode = "";//订单编号
    String total_amount = "";//商品总价
    private IWXAPI api;

    public static final String APP_ID_ZFB = "2018080760936380";

    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(ConfirmOrderdActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new BackConfireOrderEvent());
                        finish();

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ConfirmOrderdActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(ConfirmOrderdActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
    private boolean mIs_single;
    private List<ShoppingEntity> mGoodList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_orderd);
        ButterKnife.bind(this);
        mGoodList = (List<ShoppingEntity>) getIntent().getSerializableExtra("good_list");
        mIs_single = getIntent().getBooleanExtra("is_single", false);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        mTvTitle.setText("确认订单");
        mSwitchNotify.setToggleOn();
        mTvAddress.setVisibility(View.GONE);
        mTvName.setText("请选择地址");
        try {
            bindData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSwitchNotify.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                try {
                    bindData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        ShoppingManager.getInstance().addressList(new FinalResponseCallback<AddressEntity>(this) {
            @Override
            public void onSuccess(List<AddressEntity> t) {
                super.onSuccess(t);
                for (int i = 0; i < t.size(); i++) {
                    String is_default = t.get(i).is_default;
                    if ("0".equals(is_default)) {
                        mAddress_id = t.get(i).id;
                        String user_name = t.get(i).name;
                        String address = t.get(i).address;
                        String phone = t.get(i).phone;
                        mTvAddress.setText("收货地址:" + address);
                        mTvAddress.setVisibility(View.VISIBLE);
                        mTvName.setText("收货人:" + user_name + "   " + phone);
                    }
                }

            }

            @Override
            public boolean isShowNotice() {
                return false;
            }
        });

    }

    private void bindData() {
        DecimalFormat format = new DecimalFormat("##.##");
        double prices = 0.00;
        double freight = 0.00;
        if (mGoodList != null) {
            mGood_list.clear();
            mGood_list.addAll(mGoodList);
            ConfirmOrderdAdapter adapter = new ConfirmOrderdAdapter(this, mGood_list);
            mRvGoods.setAdapter(adapter);
            for (int i = 0; i < mGood_list.size(); i++) {
                for (int j = 0; j < mGood_list.get(i).list.size(); j++) {
                    String num = TextUtils.isEmpty(mGood_list.get(i).list.get(j).num) ? "1" : mGood_list.get(i).list.get(j).num;
                    prices = prices + Double.parseDouble(num) * Double.parseDouble(mGood_list.get(i).list.get(j).price);
                    freight = freight + Double.parseDouble(mGood_list.get(i).list.get(j).freight);
                }
            }

//            mTvPrice.setText("¥" + prices + "");
            mTvAllPrice.setText("¥" + prices + "");
            mTvTransportPrice.setText("¥" + freight + "");
            double old_price = prices + freight;
            double dai_gou = prices * 1.03 + freight;
            if (mSwitchNotify.isToggleOn()) {
                isDaigou = "1";
                mTvOrderPrice.setText(format.format(dai_gou));
                mTvPrice.setText("¥" + format.format(dai_gou));
            } else {
                isDaigou = "0";
                mTvOrderPrice.setText(format.format(old_price));
                mTvPrice.setText("¥" + format.format(old_price));
            }
        }
    }

    @OnClick({R.id.rl_back, R.id.rl_address, R.id.ll_weixin, R.id.ll_zhifubao, R.id.tv_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;

            case R.id.rl_address:
                Intent intent = new Intent(this, AddressListActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
                break;
            case R.id.ll_weixin:
                mIvImage1.setVisibility(View.VISIBLE);
                mIvImage2.setVisibility(View.GONE);
                paytype = "wx";
                break;
            case R.id.ll_zhifubao:
                mIvImage1.setVisibility(View.GONE);
                mIvImage2.setVisibility(View.VISIBLE);
                paytype = "zfb";
                break;
            case R.id.tv_submit:
                if (TextUtils.isEmpty(mAddress_id)) {
                    UIUtil.toastShort(this, "请选择地址");
                    return;
                }
                if (mIs_single) {
                    String sku_id = "";
                    String num = "";
                    for (int i = 0; i < mGood_list.size(); i++) {
                        for (int j = 0; j < mGood_list.get(i).getList().size(); j++) {
                            sku_id = mGood_list.get(i).getList().get(j).sku_id;
                            num = mGood_list.get(i).getList().get(j).num;
                        }
                    }
                    immediatelyBuy(sku_id, num, isDaigou, mAddress_id);
                } else {
                    String goods = JSON.toJSONString(mGood_list);
                    shopCarBuy(goods, isDaigou, mAddress_id);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case SELECT_ADDRESS:
                mAddress_id = data.getStringExtra("address_id");
                String user_name = data.getStringExtra("user_name");
                String address = data.getStringExtra("address");
                String phone = data.getStringExtra("phone");
                mTvAddress.setText("收货地址:" + address);
                mTvAddress.setVisibility(View.VISIBLE);
                mTvName.setText("收货人:" + user_name + "   " + phone);
                break;

            default:
                break;
        }
    }

    //购物车结算
    private void shopCarBuy(String item, String is_daigou, String ship_address_id) {
        OrderManager.getInstance().shoppongCarBuy(item, is_daigou, ship_address_id, new ResponseCallback<PayEntity>() {
            @Override
            public void onSuccess(PayEntity payEntity) {
                super.onSuccess(payEntity);
                orderCode = payEntity.pay_order;
                total_amount = payEntity.total_amount;
                if ("zfb".equals(paytype)) {
                    payV2();
                } else if ("wx".equals(paytype)) {
                    payWx();
                }
            }

            @Override
            public boolean isShowNotice() {
                return false;
            }

            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(ConfirmOrderdActivity.this);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(ConfirmOrderdActivity.this);
            }
        });
    }

    private void payWx() {
        api = WXAPIFactory.createWXAPI(ConfirmOrderdActivity.this, "wx84bd33fbc16c10b7");
        OrderManager.getInstance().getWxSign(orderCode, new ResponseCallback<WxSignEntity>() {
            @Override
            public void onSuccess(WxSignEntity wxSignEntity) {
                super.onSuccess(wxSignEntity);
                try {
                    PayReq req = new PayReq();
                    List<WXModel> list = new LinkedList<>();
                    list.add(new WXModel("appid", wxSignEntity.appid));
                    list.add(new WXModel("noncestr", wxSignEntity.nonce_str));
                    list.add(new WXModel("package", wxSignEntity.packages));
                    list.add(new WXModel("partnerid", wxSignEntity.partnerid));
                    list.add(new WXModel("prepayid", wxSignEntity.prepay_id));
                    list.add(new WXModel("timestamp", wxSignEntity.timestamp));
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < list.size(); i++) {
                        sb.append(list.get(i).key);
                        sb.append('=');
                        sb.append(list.get(i).value);
                        sb.append('&');
                    }
                    sb.append("key=");
                    sb.append("039e097e658b7e9787a23aeefb0464f8");
                    String appSign = StringUtil.MD5Encode(sb.toString(), "utf-8").toUpperCase();

                    req.appId = wxSignEntity.appid;
                    req.partnerId = wxSignEntity.partnerid;
                    req.prepayId = wxSignEntity.prepay_id;
                    req.nonceStr = wxSignEntity.nonce_str;
                    req.timeStamp = wxSignEntity.timestamp;
                    req.packageValue = wxSignEntity.packages;
                    req.sign = appSign;
                    api.sendReq(req);
                } catch (Exception e) {

                    Toast.makeText(ConfirmOrderdActivity.this, "支付异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public boolean isShowNotice() {
                return false;
            }

            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(ConfirmOrderdActivity.this);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(ConfirmOrderdActivity.this);
            }
        });
    }

    //立即购买

    private void immediatelyBuy(String sku_id, String num, String is_daigou, String ship_address_id) {
        OrderManager.getInstance().immediatelyBuy(sku_id, num, is_daigou, ship_address_id, new ResponseCallback<PayEntity>() {
            @Override
            public void onSuccess(PayEntity payEntity) {
                super.onSuccess(payEntity);
                orderCode = payEntity.pay_order;
                total_amount = payEntity.total_amount;
                if ("zfb".equals(paytype)) {
                    payV2();
                } else if ("wx".equals(paytype)) {
                    payWx();
                }
            }

            @Override
            public boolean isShowNotice() {
                return false;
            }

            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(ConfirmOrderdActivity.this);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(ConfirmOrderdActivity.this);
            }
        });
    }

    /**
     * 支付宝支付业务
     *
     * @param
     */
    public void payV2() {
        if (TextUtils.isEmpty(APP_ID_ZFB) || (TextUtils.isEmpty(UrlConst.RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (UrlConst.RSA_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APP_ID_ZFB, orderCode, total_amount, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = /*rsa2 ? RSA2_PRIVATE : RSA_PRIVATE*/UrlConst.RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ConfirmOrderdActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Subscribe
    public void onEvent(BackConfireOrderEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
