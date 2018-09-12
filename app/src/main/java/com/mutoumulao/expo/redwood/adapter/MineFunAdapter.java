package com.mutoumulao.expo.redwood.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.activity.AboutActivity;
import com.mutoumulao.expo.redwood.activity.CreateStoreActivity;
import com.mutoumulao.expo.redwood.activity.MyBrowseHistory;
import com.mutoumulao.expo.redwood.activity.PublishRecruitmentListActivity;
import com.mutoumulao.expo.redwood.activity.StoreManagerActivity;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.entity.MineIconEntity;
import com.mutoumulao.expo.redwood.fragment.MineFragment;
import com.mutoumulao.expo.redwood.logic.MineFuncManager;
import com.mutoumulao.expo.redwood.util.BitmapTools;
import com.mutoumulao.expo.redwood.util.SharedPutils;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;

import java.util.List;

/**
 * Created by lzy on 2018/8/1.
 */

public class MineFunAdapter extends BaseAdapter<MineIconEntity, MineFunAdapter.MineFunHolder> {

    private BitmapTools mBitmapTools;
    private final SharedPutils mPutils;

    public MineFunAdapter(Context context, List list) {
        super(context, list);
        mBitmapTools = new BitmapTools(context);
        mPutils = SharedPutils.getPreferences(context);
    }

    @Override
    public MineFunAdapter.MineFunHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MineFunHolder(mInflater.inflate(R.layout.item_mine_fun, null));
    }

    @Override
    public void onBindViewHolder(MineFunHolder holder, int position) {
        final MineIconEntity item = getItem(position);
        if (item.name.equals("暂停营业")) {
            if ("1".equals(mPutils.getIsClose())) {
                item.name = "暂停营业";
            } else {
                item.name = "正常营业";
            }

        }
        holder.mTv_func_name.setText(item.name);

        mBitmapTools.display(holder.mIv_func_image, item.drawable);
        holder.mLl_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (item.name) {
                    case "服务管理":
                        intent = new Intent(mContext, PublishRecruitmentListActivity.class);
                        intent.putExtra(MineFragment.EXTECL_NAME, item.name);
                        intent.putExtra(MineFragment.EXTECL_TYPE, "0");
                        mContext.startActivity(intent);
                        break;
                    case "招聘管理":
                        intent = new Intent(mContext, PublishRecruitmentListActivity.class);
                        intent.putExtra(MineFragment.EXTECL_NAME, item.name);
                        intent.putExtra(MineFragment.EXTECL_TYPE, "1");
                        mContext.startActivity(intent);
                        break;
                    case "求职管理":
                        intent = new Intent(mContext, PublishRecruitmentListActivity.class);
                        intent.putExtra(MineFragment.EXTECL_NAME, item.name);
                        intent.putExtra(MineFragment.EXTECL_TYPE, "2");
                        mContext.startActivity(intent);
                        break;

                    case "商铺管理":
                        intent = new Intent(mContext, StoreManagerActivity.class);
                        intent.putExtra(MineFragment.EXTECL_NAME, item.name);
                        mContext.startActivity(intent);
                        break;

                    case "我的足迹":
                        intent = new Intent(mContext, MyBrowseHistory.class);
                        intent.putExtra(MineFragment.EXTECL_NAME, item.name);
                        mContext.startActivity(intent);
                        break;

                    case "商家入驻":
                        intent = new Intent(mContext, CreateStoreActivity.class);
                        intent.putExtra("is_edit", false);
                        mContext.startActivity(intent);
                        break;

                    case "暂停营业":

                        UIUtil.showConfirm(mContext, "是否确定暂停营业？", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MineFuncManager.getInstance().getStoreClose("0",new ResponseCallback() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        super.onSuccess(o);
                                        mPutils.setIsClose("0");
                                        item.name = "暂停营业";

                                        notifyDataSetChanged();
                                    }

                                });
                            }
                        });
                        break;
                    case "正常营业":
                        UIUtil.showConfirm(mContext, "是否确定正常营业？", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MineFuncManager.getInstance().getStoreClose("1",new ResponseCallback() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        super.onSuccess(o);
                                        mPutils.setIsClose("1");
                                        item.name = "正常营业";
                                        notifyDataSetChanged();
                                    }

                                });
                            }
                        });
                        break;

                    case "关于我们":
                        intent = new Intent(mContext, AboutActivity.class);
                        intent.putExtra("is_edit", false);
                        mContext.startActivity(intent);
                        break;

                    case "联系客服"://4009150969
                        intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + "4009150969");
                        intent.setData(data);
                        mContext.startActivity(intent);
                        break;

                    default:
                        break;
                }

            }
        });

    }

    static class MineFunHolder extends RecyclerView.ViewHolder {

        private final TextView mTv_func_name;
        private final ImageView mIv_func_image;
        private final LinearLayout mLl_content;

        public MineFunHolder(View itemView) {
            super(itemView);
            mIv_func_image = (ImageView) itemView.findViewById(R.id.iv_func_image);
            mTv_func_name = (TextView) itemView.findViewById(R.id.tv_func_name);
            mLl_content = (LinearLayout) itemView.findViewById(R.id.ll_content);

        }
    }
}
