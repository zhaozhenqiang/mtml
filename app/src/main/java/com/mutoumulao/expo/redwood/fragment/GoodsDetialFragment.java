package com.mutoumulao.expo.redwood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.mutoumulao.expo.redwood.MainActivity;
import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.activity.ConfirmOrderdActivity;
import com.mutoumulao.expo.redwood.activity.StoreDetialActivity;
import com.mutoumulao.expo.redwood.adapter.GoodsDetialSpecAdapter;
import com.mutoumulao.expo.redwood.base.BaseFragment;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.GoodsDetialEntity;
import com.mutoumulao.expo.redwood.entity.ShoppingEntity;
import com.mutoumulao.expo.redwood.entity.StoreEntity;
import com.mutoumulao.expo.redwood.entity.custom_interface.SKUInterface;
import com.mutoumulao.expo.redwood.entity.event.ShoppingCarEvent;
import com.mutoumulao.expo.redwood.hx.ChatActivity;
import com.mutoumulao.expo.redwood.logic.ShoppingManager;
import com.mutoumulao.expo.redwood.util.BitmapTools;
import com.mutoumulao.expo.redwood.util.DisplayUtil;
import com.mutoumulao.expo.redwood.util.GlideImageLoader;
import com.mutoumulao.expo.redwood.util.SharedPutils;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.view.AmountView;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzy on 2018/8/8.
 */

public class GoodsDetialFragment extends BaseFragment implements View.OnClickListener {

    Banner mBanner;
    TextView mTvName;
    TextView mTvPrice;
    TextView mTvOther;
    TextView mTvLocation;
    TextView mTvTypeName;
    LinearLayout mLlTypeName;
    ImageView mIvStoreHead;
    TextView mTvStoreName;
    TextView mTvAllNumber;
    TextView mTvNewNumber;
    TextView mTvDescScore;
    TextView mTvDescEvaluate;
    TextView mTvServeiceScore;
    TextView mTvServeiceEvaluate;
    TextView mTvLogisticsScore;
    TextView mTvLogisticsEvaluate;
    RelativeLayout mRlServiceMan;
    RelativeLayout mRlShoppingCar;
    TextView mTvAddCar;
    TextView mTvBuy;
    private BitmapTools mBitmapTools;

    private String good_num = "1";
    private String sku_id = "";
    private String max_good_num = "1";

    private GoodsDetialEntity mEntity;
    private GoodsDetialSpecAdapter mAdapter;
    private TextView mTvFeight;
    private int pos = -1;
    private RelativeLayout mRl_all_goods;
    private RelativeLayout mRl_new_goods;
    private List<LocalMedia> medias = new ArrayList<>();

    @Override
    protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goods_detial, null);
    }

    @Override
    protected void initViews() {
        mBitmapTools = new BitmapTools(mBaseActivity);
        mBanner = (Banner) findViewById(R.id.banner);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvPrice = (TextView) findViewById(R.id.tv_price);
        mTvOther = (TextView) findViewById(R.id.tv_other);
        mTvFeight = (TextView) findViewById(R.id.tv_feight);
        mTvLocation = (TextView) findViewById(R.id.tv_location);
        mTvTypeName = (TextView) findViewById(R.id.tv_type_name);
        mLlTypeName = (LinearLayout) findViewById(R.id.ll_type_name);
        mIvStoreHead = (ImageView) findViewById(R.id.iv_store_head);
        mTvStoreName = (TextView) findViewById(R.id.tv_store_name);
        mTvAllNumber = (TextView) findViewById(R.id.tv_all_number);
        mTvNewNumber = (TextView) findViewById(R.id.tv_new_number);
        mTvDescScore = (TextView) findViewById(R.id.tv_desc_score);
        mTvDescEvaluate = (TextView) findViewById(R.id.tv_desc_evaluate);
        mTvServeiceScore = (TextView) findViewById(R.id.tv_serveice_score);
        mTvServeiceEvaluate = (TextView) findViewById(R.id.tv_serveice_evaluate);
        mTvLogisticsScore = (TextView) findViewById(R.id.tv_logistics_score);
        mTvLogisticsEvaluate = (TextView) findViewById(R.id.tv_logistics_evaluate);
        mRlServiceMan = (RelativeLayout) findViewById(R.id.rl_service_man);
        mRlShoppingCar = (RelativeLayout) findViewById(R.id.rl_shopping_car);
        mRl_all_goods = (RelativeLayout) findViewById(R.id.rl_all_goods);
        mRl_new_goods = (RelativeLayout) findViewById(R.id.rl_new_goods);
        mTvAddCar = (TextView) findViewById(R.id.tv_add_car);
        mTvBuy = (TextView) findViewById(R.id.tv_buy);

        mLlTypeName.setOnClickListener(this);
        mRlServiceMan.setOnClickListener(this);
        mRlShoppingCar.setOnClickListener(this);
        mTvAddCar.setOnClickListener(this);
        mTvBuy.setOnClickListener(this);


    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle mBundle = getArguments();
        mEntity = (GoodsDetialEntity) mBundle.getSerializable("storeManagerListEntity");
        final List<String> images = new ArrayList<>();//图片集合
        if (mEntity != null) {
            for (int i = 0; i < mEntity.goods_image.size(); i++) {
                images.add(UrlConst.IMAGE_URL + mEntity.goods_image.get(i));
            }
            mBanner.setImageLoader(new GlideImageLoader());//设置图片加载器
            mBanner.setImages(images);//设置图片集合
            mBanner.setBannerAnimation(Transformer.Default);//设置banner动画效果
            mBanner.isAutoPlay(true);//设置自动轮播，默认为true
            mBanner.setDelayTime(5000); //设置轮播时间
            mBanner.setIndicatorGravity(BannerConfig.CENTER); //设置指示器位置（当banner模式中有指示器时）
            mBanner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    medias.clear();
                    for (int i = 0; i < images.size(); i++) {
                        LocalMedia localMedia = new LocalMedia();
                        localMedia.setPath(images.get(i));
                        medias.add(localMedia);
//                        PictureSelector.create(mBaseActivity).externalPicturePreview(position, medias);
                    }
                    PictureSelector.create(mBaseActivity).themeStyle(R.style.picture_default_style).openExternalPreview(position, medias);
                }
            });
            mBanner.start();//banner设置方法全部调用完毕时最后调用

            mTvName.setText(mEntity.goods_name);
            mTvPrice.setText(mEntity.price);
            String sale_num = TextUtils.isEmpty(mEntity.sale_num) ? "0" : mEntity.sale_num;
            mTvOther.setText("月销量　" + sale_num + "件");
            mTvLocation.setText("发货地" + mEntity.send_address);
            mTvFeight.setText("快递: ¥" + mEntity.freight + "元");

            if (mEntity.guiges != null) {
                mAdapter = new GoodsDetialSpecAdapter(mBaseActivity, mEntity.guiges);
            }

            mTvStoreName.setText(mEntity.business_name);
            mBitmapTools.display(mIvStoreHead, UrlConst.IMAGE_URL + mEntity.avatar);
            mTvServeiceScore.setText(mEntity.fuwu_star);
            if (4.5 <= Double.parseDouble(mEntity.fuwu_star)) {
                mTvServeiceEvaluate.setText("高");
                mTvServeiceEvaluate.setBackground(getResources().getDrawable(R.drawable.shape_red_fill_rect));
            } else {
                mTvServeiceEvaluate.setText("低");
                mTvServeiceEvaluate.setBackground(getResources().getDrawable(R.drawable.shape_green_fill_rect));

            }
            mTvDescScore.setText(mEntity.desc_star);
            if (4.5 <= Double.parseDouble(mEntity.desc_star)) {
                mTvDescEvaluate.setText("高");
                mTvDescEvaluate.setBackground(getResources().getDrawable(R.drawable.shape_red_fill_rect));
            } else {
                mTvDescEvaluate.setText("低");
                mTvDescEvaluate.setBackground(getResources().getDrawable(R.drawable.shape_green_fill_rect));

            }
            mTvLogisticsScore.setText(mEntity.wuliu_star);
            if (4.5 <= Double.parseDouble(mEntity.wuliu_star)) {
                mTvLogisticsEvaluate.setText("高");
                mTvLogisticsEvaluate.setBackground(getResources().getDrawable(R.drawable.shape_red_fill_rect));
            } else {
                mTvLogisticsEvaluate.setText("低");
                mTvLogisticsEvaluate.setBackground(getResources().getDrawable(R.drawable.shape_green_fill_rect));
            }
            if (!TextUtils.isEmpty(mEntity.goods_count)) {
                mTvAllNumber.setText(mEntity.goods_count);
            }
            mRl_all_goods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StoreEntity entity = new StoreEntity();
                    entity.avatar = mEntity.avatar;
                    entity.phone = mEntity.business_phone;
                    entity.address = mEntity.send_address;
                    entity.business_name = mEntity.business_name;
                    entity.author_id = mEntity.author_id;
                    entity.id = mEntity.business_id;

                    Intent intent = new Intent(mBaseActivity, StoreDetialActivity.class);
                    intent.putExtra("storeEntity", entity);
//                    intent.putExtra("form", "goods");
                    intent.putExtra("store_id", mEntity.business_id);
                    intent.putExtra("id", mEntity.business_id);
                    intent.putExtra("author_id", mEntity.author_id);
                    startActivity(intent);

                }
            });
            mRl_new_goods.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_type_name:
                showPopWindows();
                break;
            case R.id.rl_service_man:
                // 去环信
                EMClient.getInstance().chatManager().loadAllConversations();
                EMClient.getInstance().groupManager().loadAllGroups();
                if (SharedPutils.getPreferences(mBaseActivity).getPhone().equals(mEntity.business_phone)) {
                    UIUtil.toastShort(mBaseActivity, "不能和自己聊天");
                } else {
                    intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, mEntity.business_phone);
                    startActivity(intent);
                }
                break;
            case R.id.rl_shopping_car:
                intent = new Intent(mBaseActivity, MainActivity.class);
                intent.putExtra("shop_position", 2);
                startActivity(intent);
                break;
            case R.id.tv_add_car:
                if (TextUtils.isEmpty(sku_id)) {
                    showPopWindows();
                } else {
                    addShopCar();
                }
                break;
            case R.id.tv_buy:
                if (TextUtils.isEmpty(sku_id)) {
                    showPopWindows();
                } else {
                    intent = new Intent(mBaseActivity, ConfirmOrderdActivity.class);
                    List<ShoppingEntity> good_list = assemble();
                    intent.putExtra("good_list", (Serializable) good_list);
                    intent.putExtra("is_single", true);
                    startActivity(intent);
                }
                break;

            default:
                break;
        }
    }

    private void addShopCar() {
        if (TextUtils.isEmpty(sku_id)) {
            UIUtil.toastShort(mBaseActivity, "请选择规格");
            return;
        }
        ShoppingManager.getInstance().getShopCarAdd(sku_id, good_num, new ResponseCallback() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                EventBus.getDefault().post(new ShoppingCarEvent());
            }
        });
    }


    private void showPopWindows() {
        final AlertDialog dialog = new AlertDialog.Builder(mBaseActivity, R.style.dialog_style).create();
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.gravity = Gravity.BOTTOM;
        params.width = DisplayUtil.getScreenWidth(mBaseActivity);
        window.setAttributes(params);
        window.setContentView(R.layout.dialog_goods_spec);
        ImageView iv_store_head = window.findViewById(R.id.iv_store_head);
        final TextView tv_price = window.findViewById(R.id.tv_price);
        final TextView tv_inventory = window.findViewById(R.id.tv_inventory);
        final TextView tv_select_spec = window.findViewById(R.id.tv_select_spec);
        BaseRecyclerView rv_goods_spec = window.findViewById(R.id.rv_goods_spec);
        final AmountView av_add_subtract = window.findViewById(R.id.av_add_subtract);
        TextView tv_add_car = window.findViewById(R.id.tv_diaadd_car);
        TextView tv_buy = window.findViewById(R.id.tv_diabuy);
        tv_inventory.setVisibility(View.GONE);
        if (mEntity.goods_image.size() > 0) {
            mBitmapTools.display(iv_store_head, UrlConst.IMAGE_URL + mEntity.goods_image.get(0));
        }
        av_add_subtract.setGoods_storage(Integer.parseInt(max_good_num));
        av_add_subtract.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                good_num = amount + "";

            }
        });

        rv_goods_spec.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


        tv_add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                addShopCar();
            }
        });
        tv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(sku_id)) {
                    UIUtil.toastShort(mBaseActivity, "请选择规格");
                    return;
                }
                dialog.dismiss();
                Intent intent = new Intent(mBaseActivity, ConfirmOrderdActivity.class);
                List<ShoppingEntity> good_list = assemble();
                intent.putExtra("good_list", (Serializable) good_list);
                intent.putExtra("is_single", true);
                startActivity(intent);

            }
        });


        mAdapter.setSKUInterface(new SKUInterface() {
            @Override
            public void selectedAttribute(String[] attr) {
                String str = "";
                String ss = "";
                String spec = "";
                for (int i = 0; i < attr.length; i++) {
                    str += " " + mEntity.guiges.get(i).title + "：";
                    ss = TextUtils.isEmpty(attr[i]) ? "无" : attr[i];
                    str += ss + "";
                    spec = spec + attr[i] + ":";

                }
                tv_select_spec.setText(str);
                for (int j = 0; j < mEntity.skuList.size(); j++) {
                    if (spec.contains(mEntity.skuList.get(j).spec)) {
                        sku_id = mEntity.skuList.get(j).sku_id;
                        pos = j;
                        max_good_num = mEntity.skuList.get(j).stock;
                        tv_inventory.setVisibility(View.VISIBLE);
                        tv_inventory.setText("库存" + max_good_num);
                        av_add_subtract.setGoods_storage(Integer.parseInt(max_good_num));
                        tv_price.setText(mEntity.skuList.get(j).price);
                        return;
                    } else {
                        sku_id = "";
                    }

                }



            }

            @Override
            public void uncheckAttribute(String[] attr) {
                String str = "";
                String ss = "";
                String spec = "";
                for (int i = 0; i < attr.length; i++) {
                    str += " " + mEntity.guiges.get(i).title + "：";
                    ss = TextUtils.isEmpty(attr[i]) ? "无" : attr[i];
                    str += ss + " ";
                    spec = spec + attr[i] + ":";
                }

                for (int j = 0; j < mEntity.skuList.size(); j++) {
                    if (spec.contains(mEntity.skuList.get(j).spec)) {
                        sku_id = mEntity.skuList.get(j).sku_id;
                        max_good_num = mEntity.skuList.get(j).stock;
//                        tv_inventory.setVisibility(View.GONE);
                        return;
                    } else {
                        av_add_subtract.setGoods_storage(Integer.parseInt("1"));
                        tv_inventory.setVisibility(View.GONE);
                        sku_id = "";
                    }

                }
                tv_select_spec.setText(str);
            }
        });
    }

    private List<ShoppingEntity> assemble() {
        List<ShoppingEntity> good_list = new ArrayList<>();
        ShoppingEntity entity = new ShoppingEntity();
        entity.avatar = mEntity.avatar;
        entity.business_name = mEntity.business_name;
        ShoppingEntity.ShoppingCarEntity carEntity = new ShoppingEntity.ShoppingCarEntity();
        carEntity.num = good_num;
        carEntity.stock = max_good_num;
        carEntity.author_id = mEntity.author_id;
        carEntity.business_id = mEntity.id;
        carEntity.category = mEntity.category;
        carEntity.category_type = mEntity.category_type;
        carEntity.freight = mEntity.freight;
        carEntity.goods_name = mEntity.goods_name;
        carEntity.goods_image = mEntity.goods_image;

        GoodsDetialEntity.SkuListEntity skuListEntity = mEntity.skuList.get(pos);
        carEntity.spec = skuListEntity.spec;
        carEntity.price = skuListEntity.price;
        carEntity.sku_id = sku_id;

        List<ShoppingEntity.ShoppingCarEntity> list_goods = new ArrayList<>();
        list_goods.add(carEntity);
        entity.setList(list_goods);

        good_list.add(entity);
        return good_list;
    }
}
