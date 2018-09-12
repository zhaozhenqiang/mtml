package com.mutoumulao.expo.redwood.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mutoumulao.expo.redwood.R;

import java.lang.ref.WeakReference;

/**
 * Created by lzy on 2018/7/25.
 */

public class UIUtil {

    private static final int ID_LOADING_VIEW = 0xfffff0;
    private static final int ID_EMPTY_VIEW = 0xfffff1;
    private static final int ID_ERROR_VIEW = 0xfffff2;

    private static WeakReference<Toast> sToastRef = null;
    private static WeakReference<Dialog> sDialogRef;
    /**
     * 弹出短时间的吐司
     * toastShort
     * @param context
     * @param msg
     * @since 1.0
     */
    public static void toastShort(Context context, String msg) {
        toast(context, msg, Toast.LENGTH_SHORT);
    }

    /**
     * 弹出短时间的吐司
     * toastShort
     * @param context
     * @param resId
     * @since 1.0
     */
    public static void toastShort(Context context, int resId) {
        toast(context, context.getString(resId), Toast.LENGTH_SHORT);
    }


    private static void toast(Context context, String msg, int duration) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast t = null;
        if (sToastRef == null || sToastRef.get() == null) {
            t = Toast.makeText(context, msg, duration);
            sToastRef = new WeakReference<Toast>(t);
        } else {
            t = sToastRef.get();
            t.setText(msg);
            t.setDuration(duration);
        }
        t.show();
    }

    /**
     * 弹出长时间的吐司
     * toastLong
     * @param context
     * @param msg
     * @since 1.0
     */
    public static void toastLong(Context context, String msg) {
        toast(context, msg, Toast.LENGTH_LONG);
    }

    /**
     * 弹出长时间的吐司
     * toastLong
     * @param context
     * @param resId
     * @since 1.0
     */
    public static void toastLong(Context context, int resId) {
        toast(context, context.getString(resId), Toast.LENGTH_LONG);
    }

    /**
     * 设置touch效果
     *
     * @return
     */
    public static void setTouchEffect(View view) {
        if (view == null)
            return;
        view.setOnTouchListener(buildLayTouchListener(R.color.tc_black9, view.getContext()));
    }

    /**
     * 设置touch效果
     *
     * @return
     */
    public static void setTouchEffect(View view, int colorRes) {
        if (view == null)
            return;
        view.setOnTouchListener(buildLayTouchListener(colorRes, view.getContext()));
    }

    /**
     * 自定义按钮点击效果
     *
     * @param colorRes
     * @param context
     * @return
     */
    public static View.OnTouchListener buildLayTouchListener(final int colorRes, final Context context) {
        return new View.OnTouchListener() {

            @Override
            public boolean onTouch(final View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        setSelected(view, true);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        setSelected(view, false);
                        break;
                    default:
                        break;
                }
                return false;
            }

            private void setSelected(final View view, boolean isSelected) {
                Drawable drawable = null;
                if (view instanceof ImageView) {
                    drawable = ((ImageView) view).getDrawable();
                } else if (view instanceof TextView) {
                    Drawable[] drawables = ((TextView) view).getCompoundDrawables();
                    for (int i = 0; i < drawables.length; i++) {
                        if (drawables[i] != null) {
                            drawable = drawables[i];
                            break;
                        }
                    }
                }
                if (drawable == null) {
                    drawable = view.getBackground();
                }
                if (drawable == null) {
                    return;
                }
                if (isSelected && view.isSelected()) {
                    drawable.setColorFilter(context.getResources().getColor(colorRes), PorterDuff.Mode.MULTIPLY);
                    view.invalidate();
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setSelected(view, false);
                        }
                    }, 120);
                } else {
                    drawable.clearColorFilter();
                    view.invalidate();
                }
            }
        };
    }

    /**
     * 显示加载弹窗
     * @param activity
     */
    public static Dialog showProgressBar(Activity activity) {
        return showProgressBar(activity, null);
    }

    /**
     * 显示加载弹窗
     * @param activity
     * @param msg
     */
    public static Dialog showProgressBar(Activity activity, String msg) {
        if (activity == null || activity.isFinishing()) return null;
        hideProgressBar(activity);
        Dialog dialog = new Dialog(activity, R.style.FullScreenDialog);
        sDialogRef = new WeakReference<Dialog>(dialog);
        LayoutInflater mLayoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = mLayoutInflater.inflate(R.layout.dialog_loading, null);
        // 显示宽度为屏幕的3/5
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(SystemUtil.getScreenWidth() / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tvMsg = (TextView) layout.findViewById(R.id.txtv_loading_text);

        if (TextUtils.isEmpty(msg)) {
            tvMsg.setText(R.string.dialog_loading);
        } else {
            tvMsg.setText(msg);
        }
        dialog.setContentView(layout, params);
        dialog.show();
        return dialog;
    }
    /**
     * 显示加载弹窗 点击屏幕不允许被关闭
     * @param activity
     * @param msg
     */
    public static void showProgressBar(Activity activity, String msg,boolean canceledOnTouchOutside) {
        if (activity == null || activity.isFinishing()) return;
        hideProgressBar(activity);
        Dialog dialog = new Dialog(activity, R.style.FullScreenDialog);
        sDialogRef = new WeakReference<Dialog>(dialog);
        LayoutInflater mLayoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = mLayoutInflater.inflate(R.layout.dialog_loading, null);
        // 显示宽度为屏幕的4/5
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(SystemUtil.getScreenWidth() / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tvMsg = (TextView) layout.findViewById(R.id.txtv_loading_text);

        if (TextUtils.isEmpty(msg)) {
            tvMsg.setText(R.string.dialog_loading);
        } else {
            tvMsg.setText(msg);
        }
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        dialog.setContentView(layout, params);
        dialog.show();
    }
    /**
     * 隐藏进度条
     * hideProgressBar
     * @param activity
     * @since 1.0
     */
    public static void hideProgressBar(Activity activity) {
        if (activity == null || activity.isFinishing()) return;
        if (sDialogRef != null && sDialogRef.get() != null) {
            try {
                sDialogRef.get().dismiss();
            } catch (Exception e) {
            } finally {
                sDialogRef.clear();
                sDialogRef = null;
            }
        }
    }

    /**
     * 显示加载中提示界面
     * showLoadingView
     * @param parent
     * @since 1.0
     */
    public static void showLoadingView(ViewGroup parent) {
        showLoadingView(parent, "");
    }

    /**
     * 显示加载中提示界面
     * showLoadingView
     * @param parent
     * @param tipStr
     * @since 1.0
     */
    public static void showLoadingView(ViewGroup parent, String tipStr) {
        //TODO
       /* if (parent == null) return;
        AnimationDrawable drawable = (AnimationDrawable) parent.getResources().getDrawable(R.drawable.ic_page_load_anim);
        View loadingView = getNoticeView(parent, drawable, tipStr, ID_LOADING_VIEW);

        drawable.start();
        showLoadingView(parent, loadingView);*/
    }

    /**
     * 显示加载中提示界面
     * showLoadingView
     * @param parent 父控件
     * @param loadingView 加载中控件
     * @since 1.0
     */
    public static void showLoadingView(ViewGroup parent, View loadingView) {
        removeNoticeView(parent, ID_LOADING_VIEW);
        removeNoticeView(parent, ID_EMPTY_VIEW);
        removeNoticeView(parent, ID_ERROR_VIEW);
        hideChildrenView(parent);
        addNoticeView(parent, loadingView, ID_LOADING_VIEW, null);
    }

    public static void showEmptyView(ViewGroup parent, View.OnClickListener onClickListener) {
        showEmptyView(parent, "", onClickListener);
    }

    public static void showEmptyView(ViewGroup parent, String emptyStr, View.OnClickListener onClickListener) {
        showEmptyView(parent, emptyStr, 0, onClickListener);
    }

    public static void showEmptyView(ViewGroup parent, String emptyStr, int resId, View.OnClickListener onClickListener) {
        View emptyView = getNoticeView(parent, parent.getResources().getDrawable(resId == 0 ? R.drawable.ic_no_data : resId), emptyStr, ID_EMPTY_VIEW);
        showEmptyView(parent, emptyView, onClickListener);
    }

    /**
     * 显示无数据时候提示界面
     * showEmptyView
     * @param parent 父控件
     * @param emptyView 加载中控件
     * @param onClickListener 控件点击事件
     * @since 1.0
     */
    public static void showEmptyView(ViewGroup parent, View emptyView, View.OnClickListener onClickListener) {
        if (parent == null) return;
        hideAllNoticeView(parent);
        hideChildrenView(parent);
        addNoticeView(parent, emptyView, ID_EMPTY_VIEW, onClickListener);
    }

    public static void showErrorView(ViewGroup parent, View.OnClickListener onClickListener) {
        showErrorView(parent, "", onClickListener);
    }

    public static void showErrorView(ViewGroup parent, String emptyStr, View.OnClickListener onClickListener) {
        showErrorView(parent, emptyStr, 0, onClickListener);
    }

    public static void showErrorView(ViewGroup parent, String emptyStr, int resId, View.OnClickListener onClickListener) {
        Drawable drawable  = parent.getResources().getDrawable(resId == 0 ? R.drawable.ic_network_failed : resId);
        View errorView = getNoticeView(parent, drawable, emptyStr, ID_ERROR_VIEW);
        showErrorView(parent, errorView, onClickListener);
    }

    /**
     * 显示加载失败时的提示界面
     * showErrorView
     * @param parent 父控件
     * @param errorView 加载失败控件
     * @param onClickListener 控件点击事件
     * @since 1.0
     */
    public static void showErrorView(ViewGroup parent, View errorView, View.OnClickListener onClickListener) {
        if (parent == null) return;
        hideAllNoticeView(parent);
        hideChildrenView(parent);
        addNoticeView(parent, errorView, ID_ERROR_VIEW, onClickListener);
    }

    /**
     * 隐藏所有的提示控件
     * hideAllNoticeView
     * @param parent 父控件
     * @since 1.0
     */
    public static void hideAllNoticeView(ViewGroup parent) {
        if (parent == null) return;
        boolean result = false;
        result |= removeNoticeView(parent, ID_LOADING_VIEW);
        result |= removeNoticeView(parent, ID_EMPTY_VIEW);
        result |= removeNoticeView(parent, ID_ERROR_VIEW);
        if (result) {
            showChildrenView(parent);
        }
    }

    // 隐藏parent下所有的子view
    public static void hideChildrenView(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View childAt = parent.getChildAt(i);
            if (childAt.getVisibility() != View.GONE) {
                childAt.setVisibility(View.GONE);
            }
        }
    }

    // 显示parent下所有的子view
    public static void showChildrenView(final ViewGroup parent) {
        boolean hasViewChange = false;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View childAt = parent.getChildAt(i);
            if (childAt.getVisibility() != View.VISIBLE) {
                childAt.setVisibility(View.VISIBLE);
                hasViewChange = true;
            }
        }
    }

    private static boolean removeNoticeView(final ViewGroup parent, int tag) {
        View view = parent.findViewWithTag(tag + parent.hashCode());

        if (view != null) {
            parent.removeView(view);
            view.setTag(null);
            return true;
        }
        return false;
    }

    // 获取默认的加载界面
    private static View getNoticeView(ViewGroup parent, Drawable srcDrawable, String tipStr, int tag) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notice, null);
        TextView tipText = (TextView) view.findViewById(R.id.text_notice_tip);
        tipText.setPadding(UIUtil.dip2px(10), 0, UIUtil.dip2px(10), 0);
        tipText.setGravity(Gravity.CENTER);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_notice);
        if (TextUtils.isEmpty(tipStr)) {
            if (tag == ID_EMPTY_VIEW) {
                tipText.setText(R.string.notice_no_data);
            } else if (tag == ID_ERROR_VIEW) {
                tipText.setText(R.string.notice_load_error);
            } else {
                tipText.setText(R.string.loading);
            }
        } else {
            tipText.setText(tipStr);
        }
        imageView.setImageDrawable(srcDrawable);
        return view;
    }

    // 在parent中添加子view，并且监听子view点击事件
    private static void addNoticeView(ViewGroup parent, View child, int tag, View.OnClickListener listener) {
        hideAllNoticeView(parent);
        hideChildrenView(parent);
        child.setTag(tag + parent.hashCode());
        parent.addView(child, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (listener != null) {
            child.setOnClickListener(listener);
        } else {
            // 防止key事件穿透
            child.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
    }

    /**
     * dip转换为px
     * dip2px
     * @param dpValue
     * @return
     * @since 1.0
     */

    public static int dip2px(int dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转换为dip
     * px2dip
     * @param pxValue
     * @return
     * @since 1.0
     */
    public static int px2dip(int pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 弹出confirm对话框
     * showConfirm
     * @param context
     * @param msg 内容
     * @param okListener
     * @since 1.0
     */
    public static Dialog showConfirm(Context context, String msg, final View.OnClickListener okListener) {
        return showConfirm(context, null, msg, null, okListener, null, null);
    }

    /**
     * 弹出confirm对话框
     * showConfirm
     * @param context
     * @param title 标题
     * @param msg 内容
     * @param okStr 确定按钮字符串
     * @param okListener 确定点击事件监听
     * @param cancelStr 取消按钮字符串
     * @param cancelListener 取消点击事件监听
     * @since 1.0
     */
    public static Dialog showConfirm(final Context context, String title, String msg, String okStr,
                                     final View.OnClickListener okListener, String cancelStr, final View.OnClickListener cancelListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_confirm, null);
        final Dialog dialog = showAlert(context, view);
        TextView titleText = (TextView) view.findViewById(R.id.tv_confirm_title);
        TextView msgText = (TextView) view.findViewById(R.id.tv_confirm_msg);
        Button leftBtn = (Button) view.findViewById(R.id.btn_confirm_left);
        Button rightBtn = (Button) view.findViewById(R.id.btn_confirm_right);
        titleText.setText(TextUtils.isEmpty(title) ? context.getString(R.string.notice) : title);
        msgText.setText(msg);
        leftBtn.setText(TextUtils.isEmpty(cancelStr) ? context.getString(R.string.cancel) : cancelStr);
        rightBtn.setText(TextUtils.isEmpty(okStr) ? context.getString(R.string.ok) : okStr);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (cancelListener != null) {
                    cancelListener.onClick(view);
                }
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (okListener != null) {
                    okListener.onClick(view);
                }
            }
        });
        return dialog;
    }


    /**
     * 显示自定义弹窗
     * @param context
     * @param view
     * @return
     */
    public static Dialog showAlert(final Context context, View view) {
        return showAlert(context, view, 0);
    }

    public static Dialog showAlert(final Context context, View view, int width) {
        Dialog dialog = new Dialog(context, R.style.FullScreenDialog);
        // 显示宽度为屏幕的5/6
        if(width == 0){
            width = SystemUtil.getScreenWidth() * 5 / 6;
        }
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(view, params);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);

        if (context instanceof Activity) {// 判断状态
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {
                dialog.show();
            }
        }
        return dialog;
    }

    public static Dialog showAlert(final Context context, String msg, View.OnClickListener listener) {
        return showAlert(context, "提示", msg, "确定", listener);
    }

    public static Dialog showAlert(final Context context, String title, String msg) {
        return showAlert(context, title, msg, null, null);
    }

    public static Dialog showAlert(final Context context, String title, String msg, String okStr,
                                   final View.OnClickListener okListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_confirm, null);
        final Dialog dialog = showAlert(context, view);
        TextView titleText = (TextView) view.findViewById(R.id.tv_confirm_title);
        TextView msgText = (TextView) view.findViewById(R.id.tv_confirm_msg);
        Button leftBtn = (Button) view.findViewById(R.id.btn_confirm_left);
        Button rightBtn = (Button) view.findViewById(R.id.btn_confirm_right);
        if (TextUtils.isEmpty(title)) {
            titleText.setVisibility(View.GONE);
        } else {
            titleText.setText(title);
        }
        msgText.setText(msg);
        setTouchEffect(rightBtn);
        leftBtn.setVisibility(View.GONE);
        view.findViewById(R.id.di_confirm_line).setVisibility(View.GONE);
        rightBtn.setText(TextUtils.isEmpty(okStr) ? context.getString(R.string.ok) : okStr);
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (okListener != null) {
                    okListener.onClick(view);
                }
            }
        });
        return dialog;
    }

    public static void hideSoftInput(Context context, View view) {
        //隐藏键盘时取消对etcommenter的监听;
        //  mEt_sendmessage.removeTextChangedListener(new MyTextWatcher(ivCommentSend));
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
