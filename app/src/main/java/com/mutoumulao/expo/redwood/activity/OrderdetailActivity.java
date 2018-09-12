package com.mutoumulao.expo.redwood.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.OrderGoodsAdapter;
import com.mutoumulao.expo.redwood.adapter.WuliuAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.OrderEntity;
import com.mutoumulao.expo.redwood.entity.WXModel;
import com.mutoumulao.expo.redwood.entity.WuliuEntity;
import com.mutoumulao.expo.redwood.entity.WxSignEntity;
import com.mutoumulao.expo.redwood.entity.event.OrderEvent;
import com.mutoumulao.expo.redwood.logic.OrderManager;
import com.mutoumulao.expo.redwood.logic.ShoppingManager;
import com.mutoumulao.expo.redwood.util.BitmapTools;
import com.mutoumulao.expo.redwood.util.DisplayUtil;
import com.mutoumulao.expo.redwood.util.StringUtil;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.alipay.OrderInfoUtil2_0;
import com.mutoumulao.expo.redwood.util.alipay.PayResult;
import com.mutoumulao.expo.redwood.util.http.FinalResponseCallback;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mutoumulao.expo.redwood.util.UIUtil.showAlert;

/**
 * Created by lzy on 2018/8/13.
 * 订单详情
 */

public class OrderdetailActivity extends BaseActivity {

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
    LinearLayout mRlAddress;
    @BindView(R.id.iv_store_head)
    ImageView mIvStoreHead;
    @BindView(R.id.tv_store_name)
    TextView mTvStoreName;
    @BindView(R.id.rv_goods)
    BaseRecyclerView mRvGoods;
    @BindView(R.id.tv_all_price)
    TextView mTvAllPrice;
    @BindView(R.id.tv_daigou_price)
    TextView mTvDaigouPrice;
    @BindView(R.id.tv_transport_price)
    TextView mTvTransportPrice;
    @BindView(R.id.tv_order_price)
    TextView mTvOrderPrice;
    @BindView(R.id.tv_button_left)
    TextView mTvButtonLeft;
    @BindView(R.id.tv_button_right)
    TextView mTvButtonRight;
    @BindView(R.id.ll_button)
    LinearLayout mLlButton;
    @BindView(R.id.tv_status)
    TextView mTvState;
    @BindView(R.id.tv_order_number)
    TextView mTvOrderNumber;
    @BindView(R.id.tv_order_time)
    TextView mTvOrderTime;
    @BindView(R.id.tv_pay_time)
    TextView mTvPayTime;
    @BindView(R.id.tv_real_money)
    TextView mTvRealMoney;
    @BindView(R.id.tv_order_type)
    TextView mTvOrderType;
    @BindView(R.id.ll_to_store)
    LinearLayout mLlToStore;

    private String mId;
    public OrderGoodsAdapter mAdapter;
    private BitmapTools mBitmapTools;
    private String mIs_business;
    private OrderEntity mEntity;


    private String payType = "zfb";

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
                        Toast.makeText(OrderdetailActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new OrderEvent());
                        loadData();

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(OrderdetailActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(OrderdetailActivity.this, "支付失败",
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        initView();
        loadData();
    }


    private void initView() {
        mTvTitle.setText("订单详情");
        mId = getIntent().getStringExtra("id");
        mBitmapTools = new BitmapTools(this);
        mIs_business = getIntent().getStringExtra("is_business");

    }

    private void loadData() {
        OrderManager.getInstance().getOrderDetial(mId, new ResponseCallback<OrderEntity>() {

            @Override
            public void onSuccess(final OrderEntity orderEntity) {
                super.onSuccess(orderEntity);
                bindData(orderEntity);
                DecimalFormat format = new DecimalFormat("##.##");
                mEntity = orderEntity;
                try {
                    if (orderEntity.ship_info != null) {
                        mTvAddress.setText("收货人：" + orderEntity.ship_info.address + "  " + orderEntity.ship_info.phone);
                        mTvName.setText("收货人地址：" + orderEntity.ship_info.name);
                    } else {
                        mTvAddress.setText("收货人：" + "null");
                        mTvName.setText("收货人地址：" + "null");
                    }


                    mTvStoreName.setText(orderEntity.business_name);
                    mBitmapTools.display(mIvStoreHead, UrlConst.IMAGE_URL + orderEntity.avatar);
                    mTvAllPrice.setText("¥" + orderEntity.goods_price);
                    mTvDaigouPrice.setText("¥" + format.format(Double.parseDouble(orderEntity.daigou_money)));
                    mTvTransportPrice.setText("¥" + orderEntity.freight);

                    String all_price = orderEntity.amount;/* format.format(orderEntity.amountDouble.parseDouble(orderEntity.amount) + Double.parseDouble(orderEntity.freight) + Double.parseDouble(orderEntity.daigou_money));*/
                    mTvOrderPrice.setText("¥" + all_price);
                    mTvOrderTime.setText(orderEntity.created_at);
                    mTvOrderNumber.setText(orderEntity.pay_order);
                    mTvOrderType.setText(orderEntity.pay_typ);
                    mTvPayTime.setText(orderEntity.pay_time);
                    mTvRealMoney.setText(/*orderEntity.pay_money */all_price + "元");

                    if (orderEntity.list != null) {
                        mAdapter = new OrderGoodsAdapter(OrderdetailActivity.this, orderEntity.list);
                        mRvGoods.setAdapter(mAdapter);
                        mRvGoods.setOnItemClickListener(new BaseRecyclerView.OnItemClickListener() {
                            @Override
                            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                                Intent intent = new Intent(OrderdetailActivity.this, GoodsDetialActivity.class);
                                intent.putExtra("id", orderEntity.list.get(position).goods_id);
                                startActivity(intent);

                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean isShowNotice() {
                return false;
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void bindData(OrderEntity orderEntity) {
        if ("0".equals(orderEntity.status)) {
            mTvState.setText("待付款");
            if ("0".equals(mIs_business)) {
                mLlButton.setVisibility(View.VISIBLE);
                mTvButtonLeft.setVisibility(View.VISIBLE);
                mTvButtonRight.setVisibility(View.VISIBLE);
                mTvButtonLeft.setText("取消");
                mTvButtonRight.setText("支付");
            } else {
                mLlButton.setVisibility(View.GONE);
            }

        } else if ("1".equals(orderEntity.status)) {
            mTvState.setText("待发货" /*+ "\n" + orderEntity.auto_receive_time*/);
            if ("0".equals(mIs_business)) {
                mLlButton.setVisibility(View.GONE);
            } else {
                mLlButton.setVisibility(View.VISIBLE);
                mTvButtonLeft.setVisibility(View.GONE);
                mTvButtonRight.setVisibility(View.VISIBLE);
                mTvButtonRight.setText("填单发货");
            }
        } else if ("4".equals(orderEntity.status)) {
            mTvState.setText("已发货" + "\n" + orderEntity.auto_receive_time + "自动收货");
            if ("0".equals(mIs_business)) {
                mLlButton.setVisibility(View.VISIBLE);
                mTvButtonLeft.setVisibility(View.VISIBLE);
                mTvButtonRight.setVisibility(View.VISIBLE);
                mTvButtonLeft.setText("确认收货");
                mTvButtonRight.setText("查看物流");
            } else {
                mLlButton.setVisibility(View.VISIBLE);
                mTvButtonLeft.setVisibility(View.GONE);
                mTvButtonRight.setVisibility(View.VISIBLE);
                mTvButtonRight.setText("查看物流");
            }
        } else if ("5".equals(orderEntity.status)) {
            mTvState.setText("已签收");
            if ("0".equals(mIs_business)) {
                mLlButton.setVisibility(View.VISIBLE);
                mTvButtonLeft.setVisibility(View.GONE);
                mTvButtonRight.setVisibility(View.VISIBLE);
                mTvButtonRight.setText("评价订单");
            } else {
                mLlButton.setVisibility(View.GONE);
            }

        } else if ("7".equals(orderEntity.status)) {
            mTvState.setText("已完成");
            mLlButton.setVisibility(View.GONE);

        } else if ("8".equals(orderEntity.status)) {
            mTvState.setText("已取消");
            mLlButton.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.rl_back, R.id.tv_button_left, R.id.tv_button_right, R.id.ll_to_store})
    public void onClick(View v) {
        Intent intent;
        String type = mEntity.status;
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_button_left:
                if ("0".equals(mIs_business)) {
                    if ("0".equals(type)) {
                        //取消订单
                        UIUtil.showConfirm(this, "是否取消收货？", new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                cancelOrder(mEntity.id);
                            }
                        });


                    }
                    if ("4".equals(type)) {
                        //确认收货
                        UIUtil.showConfirm(this, "是否确认收货？", new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                shouhuo(mEntity.id);
                            }
                        });

                    }
                }
                break;
            case R.id.tv_button_right:

                if ("0".equals(mIs_business)) {
                    if ("0".equals(type)) {
                        //支付
                        showPayDialog(mEntity);
                    }
                    if ("4".equals(type)) {
                        shoWuliuDialog(mEntity.ship_com_code, mEntity.ship_order_num);
                    }
                    if ("5".equals(type)) {
                        //评价订单
                        intent = new Intent(this, EvaluateActivity.class);
                        intent.putExtra("order_id", mEntity.id);
                        if (mEntity.goods_image != null && mEntity.goods_image.size() > 0) {
                            intent.putExtra("order_avater", mEntity.goods_image.get(0));
                        } else {
                            intent.putExtra("order_avater", mEntity.avatar);
                        }
                        startActivity(intent);
                    }
                } else {
                    if ("1".equals(type)) {
                        //填单发货
                        intent = new Intent(this, ShipCompanyActivity.class);
                        intent.putExtra("order_id", mEntity.id);
                        startActivity(intent);
                    }
                    if ("4".equals(type)) {
                        //查看物流
                        shoWuliuDialog(mEntity.ship_com_code, mEntity.ship_order_num);
                    }
                }
                break;
            case R.id.ll_to_store:

                intent = new Intent(OrderdetailActivity.this, StoreDetialActivity.class);
                intent.putExtra("store_id", mEntity.business_id);
                intent.putExtra("id", mEntity.business_id);
                intent.putExtra("author_id", mEntity.author_id);
                startActivity(intent);

                break;
            default:
                break;

        }
    }

    private void shouhuo(String id) {
        ShoppingManager.getInstance().getReceiveShip(id, new ResponseCallback() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                loadData();
                EventBus.getDefault().post(new OrderEvent());
            }
        });
    }


    private void showPayDialog(final OrderEntity entity) {
        final AlertDialog dialog = new AlertDialog.Builder(this, R.style.dialog_style).create();
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.gravity = Gravity.BOTTOM;
        params.width = DisplayUtil.getScreenWidth(this);
        window.setAttributes(params);
        window.setContentView(R.layout.dialog_pay);
        TextView tv_price = window.findViewById(R.id.tv_price);
        ImageView iv_close = window.findViewById(R.id.iv_close);
        LinearLayout ll_weixin = window.findViewById(R.id.ll_weixin);
        final ImageView iv_image1 = window.findViewById(R.id.iv_image1);
        LinearLayout ll_zhifubao = window.findViewById(R.id.ll_zhifubao);
        final ImageView iv_image2 = window.findViewById(R.id.iv_image2);
        Button btn_go = window.findViewById(R.id.btn_go);

        ll_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_image1.setVisibility(View.VISIBLE);
                iv_image2.setVisibility(View.GONE);
                payType = "wx";
            }
        });

        ll_zhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_image1.setVisibility(View.GONE);
                iv_image2.setVisibility(View.VISIBLE);
                payType = "zfb";
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_price.setText("支付金额: ¥" + entity.pay_money);

        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payPrice(entity);
                dialog.dismiss();
            }
        });
    }

    private void payPrice(OrderEntity entity) {
        orderCode = entity.pay_order;
        total_amount = entity.pay_money;
        if ("zfb".equals(payType)) {
            payV2();
        } else if ("wx".equals(payType)) {
            payWx();
        }
    }


    private void cancelOrder(String id) {
        OrderManager.getInstance().getOrderCancel(id, new ResponseCallback() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                loadData();

            }

            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(OrderdetailActivity.this);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(OrderdetailActivity.this);
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
            new android.app.AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {

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
                PayTask alipay = new PayTask(OrderdetailActivity.this);
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


    private void payWx() {
        api = WXAPIFactory.createWXAPI(this, "wx84bd33fbc16c10b7");
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

                    Toast.makeText(OrderdetailActivity.this, "支付异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public boolean isShowNotice() {
                return false;
            }

            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(OrderdetailActivity.this);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(OrderdetailActivity.this);
            }
        });
    }

    private List<WuliuEntity> mWuliuEntityList;

    private void shoWuliuDialog(String ship_com_code, String ship_order_num) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_wuliu, null);
        final Dialog dialog = showAlert(this, view);
        final BaseRecyclerView rv_wuliu = (BaseRecyclerView) view.findViewById(R.id.rv_wuliu);
        final TextView tv_empty = (TextView) view.findViewById(R.id.tv_empty);
        mWuliuEntityList = new ArrayList<>();
        ShoppingManager.getInstance().getQueryShip(ship_com_code, ship_order_num, new FinalResponseCallback<WuliuEntity>(rv_wuliu) {
            @Override
            public void onSuccess(List<WuliuEntity> t) {
                super.onSuccess(t);
                mWuliuEntityList.clear();
                mWuliuEntityList.addAll(t);
                WuliuAdapter adapter = new WuliuAdapter(OrderdetailActivity.this, mWuliuEntityList);
                rv_wuliu.setAdapter(adapter);
                if (t.size() == 0) {
                    tv_empty.setVisibility(View.VISIBLE);
                    rv_wuliu.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Exception error) {
                super.onFailure(error);
                tv_empty.setVisibility(View.VISIBLE);
                rv_wuliu.setVisibility(View.GONE);
            }
        });
        Button btn_confirm_right = (Button) view.findViewById(R.id.btn_confirm_right);

        btn_confirm_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
