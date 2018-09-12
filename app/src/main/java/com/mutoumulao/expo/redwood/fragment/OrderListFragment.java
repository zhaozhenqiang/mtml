package com.mutoumulao.expo.redwood.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.activity.EvaluateActivity;
import com.mutoumulao.expo.redwood.activity.OrderdetailActivity;
import com.mutoumulao.expo.redwood.activity.ShipCompanyActivity;
import com.mutoumulao.expo.redwood.adapter.OrderListAdapter;
import com.mutoumulao.expo.redwood.adapter.WuliuAdapter;
import com.mutoumulao.expo.redwood.base.BaseFragment;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.OrderEntity;
import com.mutoumulao.expo.redwood.entity.WXModel;
import com.mutoumulao.expo.redwood.entity.WuliuEntity;
import com.mutoumulao.expo.redwood.entity.WxSignEntity;
import com.mutoumulao.expo.redwood.entity.custom_interface.OrderListLeftRightListener;
import com.mutoumulao.expo.redwood.entity.event.OrderEvent;
import com.mutoumulao.expo.redwood.logic.OrderManager;
import com.mutoumulao.expo.redwood.logic.ShoppingManager;
import com.mutoumulao.expo.redwood.util.DisplayUtil;
import com.mutoumulao.expo.redwood.util.StringUtil;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.alipay.OrderInfoUtil2_0;
import com.mutoumulao.expo.redwood.util.alipay.PayResult;
import com.mutoumulao.expo.redwood.util.http.FinalResponseCallback;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;
import com.mutoumulao.expo.redwood.view.PullToRefreshView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mutoumulao.expo.redwood.util.UIUtil.showAlert;


/**
 * Created by lzy on 2018/8/22.
 */

public class OrderListFragment extends BaseFragment implements OrderListLeftRightListener {

    private BaseRecyclerView mRv_message;
    private int pages = 1;
    private List<OrderEntity> mList = new ArrayList<>();

    private PullToRefreshView mPull_view;
    private OrderListAdapter mAdapter;
    private String mIs_business;
    private int mStatus;

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
                        Toast.makeText(mBaseActivity, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new OrderEvent());

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(mBaseActivity, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(mBaseActivity, "支付失败",
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
    private List<WuliuEntity> mWuliuEntityList;


    @Override
    protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_home, null);
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
//        mIs_business = SharedPutils.getPreferences(mBaseActivity).getIs_business();

        mRv_message = (BaseRecyclerView) findViewById(R.id.rv_message);
        mPull_view = (PullToRefreshView) findViewById(R.id.pull_view);

        mRv_message.setOnItemClickListener(new BaseRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                OrderEntity orderEntity = mList.get(position);
                Intent intent = new Intent(mBaseActivity, OrderdetailActivity.class);
                intent.putExtra("id", orderEntity.id);
                intent.putExtra("is_business", mIs_business);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle mBundle = getArguments();
        String position = mBundle.getString("arg");
        mIs_business = mBundle.getString("is_business");

        mAdapter = new OrderListAdapter(mBaseActivity, mList, mIs_business);
        mRv_message.setAdapter(mAdapter);
        String s = "";
        if ("0".equals(position)) {
            s = "-1";
        } else if ("1".equals(position)) {
            s = "0";  //商家    普通： 取消,支付
        } else if ("2".equals(position)) {
            s = "1";   //商家：填单发货   普通：
        } else if ("3".equals(position)) {
            s = "4";  //商家 查看物流   普通：查看物流 确认收货
        } else if ("4".equals(position)) {
            s = "5";   // 商家    普通： 评价
        } else if ("5".equals(position)) {
            s = "7";
        } else if ("6".equals(position)) {
            s = "8";
        }
        mStatus = Integer.parseInt(s);
        loadData(1, mStatus);

        mPull_view.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                pages = 1;
                loadData(1, mStatus);
            }
        });

        mPull_view.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                loadData(pages, mStatus);
            }
        });

        mAdapter.setLeftRightClickListener(this);
    }

    private void loadData(final int page, final int status) {
        if ("0".equals(mIs_business)) {
            OrderManager.getInstance().getOrderList(page, status, new FinalResponseCallback<OrderEntity>(mPull_view) {
                @Override
                public void onStart() {
                    super.onStart();
                    if (1 == page) {
                        UIUtil.showProgressBar(mBaseActivity);
                    }
                }

                @Override
                public void onSuccess(List<OrderEntity> t) {
                    super.onSuccess(t);
                    mPull_view.onHeaderRefreshComplete();
                    if (1 == page) {
                        mList.clear();
                        if (0 == t.size()) {
                            setIsEmpty(true);
                        } else {
                            mList.addAll(t);
                        }
                    } else {
                        mList.addAll(t);
                        mPull_view.setLoadMoreEnable(true);
                        mPull_view.onFooterRefreshComplete();
                    }
                    pages++;
//                    mRv_message.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    if(t.size() <10){
                        mPull_view.setLoadMoreEnable(false);
                    }
                }

                @Override
                public boolean isShowNotice() {
                    return false;
                }

                @Override
                public void onReload() {
                    super.onReload();
                    loadData(1, status);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(mBaseActivity);
                    if (1 == page) {
                        mPull_view.onHeaderRefreshComplete();
                    } else {
                        mPull_view.onFooterRefreshComplete();
                    }
                }
            });

        } else {
            OrderManager.getInstance().getOrderBuinessList(page, status, new FinalResponseCallback<OrderEntity>(mPull_view) {
                @Override
                public void onStart() {
                    super.onStart();
                    if (1 == page) {
                        UIUtil.showProgressBar(mBaseActivity);
                    }
                }

                @Override
                public void onSuccess(List<OrderEntity> t) {
                    super.onSuccess(t);
                    mPull_view.onHeaderRefreshComplete();
                    if (1 == page) {
                        mList.clear();
                        if (0 == t.size()) {
                            setIsEmpty(true);
                        } else {
                            mList.addAll(t);
                        }
                    } else {
                        mList.addAll(t);
                        mPull_view.setLoadMoreEnable(true);
                        mPull_view.onFooterRefreshComplete();
                    }
                    pages++;
//                    mRv_message.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public boolean isShowNotice() {
                    return false;
                }

                @Override
                public void onReload() {
                    super.onReload();
                    loadData(1, status);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(mBaseActivity);
                    if (1 == page) {
                        mPull_view.onHeaderRefreshComplete();
                    } else {
                        mPull_view.onFooterRefreshComplete();
                    }
                }
            });
        }
    }

    @Override
    public void rightClick(int position, String type) {
        Intent intent;
        OrderEntity entity = mList.get(position);
        if ("0".equals(mIs_business)) {
            if ("0".equals(type)) {
                //支付
                showPayDialog(entity, position);
            }
            if ("4".equals(type)) {
                shoWuliuDialog(entity.ship_com_code, entity.ship_order_num);
            }
            if ("5".equals(type)) {
                //评价订单
                intent = new Intent(mBaseActivity, EvaluateActivity.class);
                intent.putExtra("order_id", entity.id);
                if (entity.goods_image.size() > 0) {
                    intent.putExtra("order_avater", entity.goods_image.get(0));
                }else {
                    intent.putExtra("order_avater",entity.avatar );
                }
                mBaseActivity.startActivity(intent);
            }
        } else {
            if ("1".equals(type)) {
                //填单发货
                intent = new Intent(mBaseActivity, ShipCompanyActivity.class);
                intent.putExtra("order_id", entity.id);
                mBaseActivity.startActivity(intent);
            }
            if ("4".equals(type)) {
                //查看物流
                shoWuliuDialog(entity.ship_com_code, entity.ship_order_num);
            }
        }
    }

    private void shoWuliuDialog(String ship_com_code, String ship_order_num) {
        View view = LayoutInflater.from(mBaseActivity).inflate(R.layout.layout_wuliu, null);
        final Dialog dialog = showAlert(mBaseActivity, view);
        final BaseRecyclerView rv_wuliu = (BaseRecyclerView) view.findViewById(R.id.rv_wuliu);
        final TextView tv_empty = (TextView) view.findViewById(R.id.tv_empty);
        mWuliuEntityList = new ArrayList<>();
        ShoppingManager.getInstance().getQueryShip(ship_com_code, ship_order_num, new FinalResponseCallback<WuliuEntity>(rv_wuliu) {
            @Override
            public void onSuccess(List<WuliuEntity> t) {
                super.onSuccess(t);
                mWuliuEntityList.clear();
                mWuliuEntityList.addAll(t);
                WuliuAdapter adapter = new WuliuAdapter(mBaseActivity, mWuliuEntityList);
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


    @Override
    public void leftClick(final int position, String type) {
        if ("0".equals(mIs_business)) {
            final OrderEntity entity = mList.get(position);
            if ("0".equals(type)) {
                //取消订单
                UIUtil.showConfirm(mBaseActivity, "是否取消订单？", new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        cancelOrder(entity.id, position);
                    }
                });


            }
            if ("4".equals(type)) {
                //确认收货
                UIUtil.showConfirm(mBaseActivity, "是否确认收货？", new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        shouhuo(entity.id);
                    }
                });

            }
        }
    }

    private void shouhuo(String id) {
        ShoppingManager.getInstance().getReceiveShip(id, new ResponseCallback() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                loadData(1, mStatus);
            }
        });
    }


    private void showPayDialog(final OrderEntity entity, final int position) {
        final AlertDialog dialog = new AlertDialog.Builder(mBaseActivity, R.style.dialog_style).create();
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.gravity = Gravity.BOTTOM;
        params.width = DisplayUtil.getScreenWidth(mBaseActivity);
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
        tv_price.setText("支付金额: ¥" + entity.amount);

        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payPrice(entity, position);
                dialog.dismiss();
            }
        });
    }

    private void payPrice(OrderEntity entity, int position) {
        orderCode = entity.pay_order;
        total_amount = entity.amount;
        if ("zfb".equals(payType)) {
            payV2();
        } else if ("wx".equals(payType)) {
            payWx();
        }
    }


    private void cancelOrder(String id, final int position) {
        OrderManager.getInstance().getOrderCancel(id, new ResponseCallback() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                mList.remove(position);
                loadData(1, mStatus);
            }

            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(mBaseActivity);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(mBaseActivity);
            }
        });
    }

    @Subscribe
    public void OnEvent(OrderEvent event) {
        loadData(1, mStatus);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 支付宝支付业务
     *
     * @param
     */
    public void payV2() {
        if (TextUtils.isEmpty(APP_ID_ZFB) || (TextUtils.isEmpty(UrlConst.RSA_PRIVATE))) {
            new android.app.AlertDialog.Builder(mBaseActivity).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
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
                PayTask alipay = new PayTask(mBaseActivity);
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
        api = WXAPIFactory.createWXAPI(mBaseActivity, "wx84bd33fbc16c10b7");
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

                    Toast.makeText(mBaseActivity, "支付异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public boolean isShowNotice() {
                return false;
            }

            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(mBaseActivity);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(mBaseActivity);
            }
        });
    }
}
