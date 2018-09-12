package com.mutoumulao.expo.redwood.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 刘会成 on 2017/3/19.
 */

public class SharedPutils {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private static SharedPutils sharedPutils;
    private Context context;

    private SharedPutils(Context context) {
        this.context = context.getApplicationContext();
        mPref = context.getSharedPreferences("OFFICE", 0);
        mEditor = mPref.edit();
    }

    public static SharedPutils getPreferences(Context context) {

        if (sharedPutils == null) {
            sharedPutils = new SharedPutils(context);
        }
        return sharedPutils;
    }



    //头像
    public void setAvatar(String Avatar) { mEditor.putString("avatar", Avatar).commit();}
    public String getAvatar() {
        return mPref.getString("avatar", "");
    }


    //头像
    public void setNoPreHeadPic(String Avatar) { mEditor.putString("headNoPrePic", Avatar).commit();}
    public String getNoPreHeadPic() {
        return mPref.getString("headNoPrePic", "");
    }




    /**
     * 用戶id
     */
    public  void setUserID(String str) { mEditor.putString("userID", str).commit();}
    public String getUserID() {
        return mPref.getString("userID", "");
    }

    /**
     * 公司id
     */
    public  void setCompany_id(String str) { mEditor.putString("company_id", str).commit();}
    public String getCompany_id() {
        return mPref.getString("company_id", "");
    }

    /**
     * 公司name
     */
    public  void setCompanyname(String str) { mEditor.putString("companyname", str).commit();}
    public String getCompanyname() {
        return mPref.getString("companyname", "");
    }


    /**
     * 是否是商户 1 是商户  0 普通用户
     */
    public  void setIs_business(String str) { mEditor.putString("is_business", str).commit();}

    public String getIs_business() {
        return mPref.getString("is_business", "");
    }

    /**
     * 商铺ID
     */
    public  void setBusinessId(String str) { mEditor.putString("BusinessId", str).commit();}
    public String getBusinessId() {
        return mPref.getString("BusinessId", "");
    }

    /**
     * 用戶名称
     */
    public  void setUserName(String str) { mEditor.putString("userName", str).commit();}
    public String getUserName() {
        return mPref.getString("userName", "");
    }

    /**
     * 用戶手机号
     */
    public void setPhone(String str) {
        mEditor.putString("phone", str).commit();
    }

    public String getPhone() {
        return mPref.getString("phone", "");
    }

    /**
     * 用戶密码
     */
    public  void setPwd(String str) {
        mEditor.putString("pwd", str).commit();
    }

    public String getPwd() {
        return mPref.getString("pwd", "");
    }

    /**
     * 店铺是否营业
     */
    public void setIsClose(String str) {
        mEditor.putString("IsClose", str).commit();
    }

    public String getIsClose() {
        return mPref.getString("IsClose", "");
    }


    /**
     * 是否是领导
     */
    public void setState(String str) {
        mEditor.putString("state", str).commit();
    }

    public String getState() {
        return mPref.getString("state", "");
    }


    /**
     * 是否可以修改公司名称
     */
    public void setis_open_email(String str) {
        mEditor.putString("is_open_email", str).commit();
    }

    public String getis_open_email() {
        return mPref.getString("is_open_email", "");
    }

    /**
     * 审核状态
     */
    public void setIsCheck(String str) {
        mEditor.putString("IsCheck", str).commit();
    }

    public String getIsCheck() {
        return mPref.getString("IsCheck", "");
    }


    /**
     * 用户工作岗位
     */
    public void setjob(String str) {
        mEditor.putString("job", str).commit();
    }

    public String getjob() {
        return mPref.getString("job", "");
    }

    /**
     * 应用手机端布局
     */
    public void setWorkMoudular(String str) {
        mEditor.putString("workMoudular", str).commit();
    }

    public String getWorkMoudular() {
        return mPref.getString("workMoudular", "");
    }

    /**
     * 审批流权限
     */
    public void setisFlowUser(String str) {
        mEditor.putString("isFlowUser", str).commit();
    }

    public String getisFlowUser() {
        return mPref.getString("isFlowUser", "");
    }


    /**
     * 发布通知权限
     */
    public void setis_pubnoticer(String str) {
        mEditor.putString("is_pubnotice", str).commit();
    }

    public String getis_pubnotice() {
        return mPref.getString("is_pubnotice", "");
    }

    /**
     * 发送考勤报告的权限
     */
    public void setis_kaoqin(String str) {
        mEditor.putString("is_kaoqin", str).commit();
    }

    public String getis_kaoqin() {
        return mPref.getString("is_kaoqin", "");
    }


    /**
     * 保存第三方登录的类型
     */
    public  void setLogintype(String str) {
        mEditor.putString("oauth_type", str).commit();
    }

    public String getLogintype() {
        return mPref.getString("oauth_type", "");
    }


    /**
     * 保存第三方微博登录的uid
     */
    public  void setWbuid(String str) {
        mEditor.putString("wbuid", str).commit();
    }

    public String getWbuid() {
        return mPref.getString("wbuid", "");
    }

    /**
     * 保存第三方微信登录的uid
     */
    public  void setWxuid(String str) {
        mEditor.putString("wxuid", str).commit();
    }

    public String getWxuid() {
        return mPref.getString("wxuid", "");
    }

    /**
     * 保存第三方qq登录的uid
     */
    public  void setqquid(String str) {
        mEditor.putString("qquid", str).commit();
    }

    public String getqquid() {
        return mPref.getString("qquid", "");
    }


    /**
     * 清除所有数据
     */
    public void clearData() {
        mEditor.clear();
        mEditor.commit();
    }

}
