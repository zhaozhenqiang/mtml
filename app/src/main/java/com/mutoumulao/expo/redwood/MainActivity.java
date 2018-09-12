package com.mutoumulao.expo.redwood;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMClientListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMMultiDeviceListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.EMLog;
import com.luck.picture.lib.tools.Constant;
import com.mutoumulao.expo.redwood.activity.LoginActivity;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.entity.event.AccountChangeEvent;
import com.mutoumulao.expo.redwood.fragment.HomeFragment;
import com.mutoumulao.expo.redwood.fragment.MessageFragment;
import com.mutoumulao.expo.redwood.fragment.MineFragment;
import com.mutoumulao.expo.redwood.fragment.ShoppingFragment;
import com.mutoumulao.expo.redwood.hx.ChatActivity;
import com.mutoumulao.expo.redwood.hx.DemoHelper;
import com.mutoumulao.expo.redwood.hx.PermissionsManager;
import com.mutoumulao.expo.redwood.hx.PermissionsResultAction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private static final String EXTRA_TAG = "tag";
    public static final String EXTRA_POS = "position";

    @BindView(R.id.divider_tab_host)
    View mDividerTabHost;
    @BindView(R.id.tab_message)
    TextView mTabMessage;
    @BindView(R.id.tab_home)
    TextView mTabHome;
    @BindView(R.id.tab_shop)
    TextView mTabShop;
    @BindView(R.id.tab_mine)
    TextView mTabMine;
    @BindView(R.id.rg_main_tab)
    LinearLayout mRgMainTab;
    @BindView(R.id.unread_msg_number)
    TextView unreadLabel;
    @BindView(R.id.fl_main)
    FrameLayout mFlMain;

    private HashMap<Integer, String> mTags;
    private int mCurrentPosition = 1;
    private long mExitTime = 0;
    private FragmentTransaction ft;

    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;
    private android.app.AlertDialog.Builder exceptionBuilder;
    private boolean isExceptionDialogShow =  false;
    public boolean isConflict = false;
    private boolean isCurrentAccountRemoved = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null &&
                (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) ||
                        getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                        getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false))) {
            DemoHelper.getInstance().logout(false,null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (getIntent() != null && getIntent().getBooleanExtra("isConflict", false)) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData(savedInstanceState);
        EventBus.getDefault().register(this);
        requestPermissions();
        showExceptionDialogFromIntent(getIntent());
        //register broadcast receiver to receive the change of group from DemoHelper
        registerBroadcastReceiver();


        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        EMClient.getInstance().addClientListener(clientListener);
        EMClient.getInstance().addMultiDeviceListener(new MyMultiDeviceListener());
    }

    EMClientListener clientListener = new EMClientListener() {
        @Override
        public void onMigrate2x(boolean success) {
            Toast.makeText(MainActivity.this, "onUpgradeFrom 2.x to 3.x " + (success ? "success" : "fail"), Toast.LENGTH_LONG).show();
            if (success) {
                refreshUIWithMessage();
            }
        }
    };


    private void initData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            onClick(mTabHome);
        }
        if (this.savedInstanceState != null) {
            mTags = (HashMap<Integer, String>) this.savedInstanceState.getSerializable(EXTRA_TAG);
            mCurrentPosition = this.savedInstanceState.getInt(EXTRA_POS);

        } else {
            mTags = new HashMap<Integer, String>(2);
            mCurrentPosition = getIntent().getIntExtra(EXTRA_POS, 0);

        }

//        changeTab(mCurrentPosition);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showExceptionDialogFromIntent(intent);
        int shop_position = intent.getIntExtra("shop_position", 0);
        if (2 == shop_position) {
            onClick(mTabShop);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
//            updateUnreadAddressLable();
        }

        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message: messages) {
                DemoHelper.getInstance().getNotifier().vibrateAndPlayTone(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {}
    };

    public void changeTab(int item) {
        switch (item) {
            case 0:
                mTabMessage.setSelected(false);
                mTabMessage.setTextColor(getResources().getColor(R.color.tc_black6));

                mTabHome.setSelected(true);
                mTabHome.setTextColor(getResources().getColor(R.color.tc_red_price));

                mTabShop.setSelected(false);
                mTabShop.setTextColor(getResources().getColor(R.color.tc_black6));

                mTabMine.setSelected(false);
                mTabMine.setTextColor(getResources().getColor(R.color.tc_black6));
                break;

            case 1:
                mTabMessage.setSelected(true);
                mTabMessage.setTextColor(getResources().getColor(R.color.tc_red_price));

                mTabHome.setSelected(false);
                mTabHome.setTextColor(getResources().getColor(R.color.tc_black6));

                mTabShop.setSelected(false);
                mTabShop.setTextColor(getResources().getColor(R.color.tc_black6));

                mTabMine.setSelected(false);
                mTabMine.setTextColor(getResources().getColor(R.color.tc_black6));

                break;
            case 2:
                mTabMessage.setSelected(false);
                mTabMessage.setTextColor(getResources().getColor(R.color.tc_black6));

                mTabHome.setSelected(false);
                mTabHome.setTextColor(getResources().getColor(R.color.tc_black6));

                mTabShop.setSelected(true);
                mTabShop.setTextColor(getResources().getColor(R.color.tc_red_price));

                mTabMine.setSelected(false);
                mTabMine.setTextColor(getResources().getColor(R.color.tc_black6));

                break;
            case 3:

                mTabMessage.setSelected(false);
                mTabMessage.setTextColor(getResources().getColor(R.color.tc_black6));

                mTabHome.setSelected(false);
                mTabHome.setTextColor(getResources().getColor(R.color.tc_black6));

                mTabShop.setSelected(false);
                mTabShop.setTextColor(getResources().getColor(R.color.tc_black6));

                mTabMine.setSelected(true);
                mTabMine.setTextColor(getResources().getColor(R.color.tc_red_price));
                break;
            default:
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        EMClient.getInstance().removeClientListener(clientListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);
    }


    @OnClick({R.id.tab_message, R.id.tab_home, R.id.tab_shop, R.id.tab_mine})
    public void onClick(View v) {
        ft = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.tab_message:
                if (mCurrentPosition != 1) {
                    mCurrentPosition = 1;
                    changeTab(1);
                    ft.replace(R.id.fl_main, MessageFragment.instantiate(MainActivity.this, MessageFragment.class.getName(), null), "MessageFragment");
                    updateUnreadLabel();
                }

                break;
            case R.id.tab_home:
                if (mCurrentPosition != 0) {
                    mCurrentPosition = 0;
                    changeTab(0);
                    ft.replace(R.id.fl_main, HomeFragment.instantiate(MainActivity.this, HomeFragment.class.getName(), null), "HomeFragment");
                }
                break;
            case R.id.tab_shop:
                if (mCurrentPosition != 2) {
                    mCurrentPosition = 2;
                    changeTab(2);
                    ft.replace(R.id.fl_main, ShoppingFragment.instantiate(MainActivity.this, ShoppingFragment.class.getName(), null), "ShoppingFragment");
                }
                break;
            case R.id.tab_mine:
                if (mCurrentPosition != 3) {
                    mCurrentPosition = 3;
                    changeTab(3);
                    ft.replace(R.id.fl_main, MineFragment.instantiate(MainActivity.this, MineFragment.class.getName(), null), "MineFragment");
                }
                break;

            default:
                break;
        }
        ft.commit();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_TAG, mTags);
        outState.putInt(EXTRA_POS, mCurrentPosition);
    }


    @Subscribe
    public void onEvent(AccountChangeEvent event) {
        //退出登录后，关闭主页
        if (!event.isLogin) {
            finish();
        }
    }

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            unreadLabel.setText(String.valueOf(count));
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    public int getUnreadMsgCountTotal() {
        return EMClient.getInstance().chatManager().getUnreadMsgsCount();
    }


    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
//                moveTaskToBack(false);
                finish();
            }
            return true;
        }


        return super.onKeyDown(keyCode, event);
    }

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // refresh unread count
                updateUnreadLabel();

            }
        });
    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
//                updateUnreadAddressLable();
//                String action = intent.getAction();

            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
        }

        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
//            updateUnreadAddressLable();
        }

        @Override
        public void onContactInvited(String username, String reason) {
        }

        @Override
        public void onFriendRequestAccepted(String username) {
        }

        @Override
        public void onFriendRequestDeclined(String username) {
        }
    }

    public class MyMultiDeviceListener implements EMMultiDeviceListener {

        @Override
        public void onContactEvent(int event, String target, String ext) {

        }

        @Override
        public void onGroupEvent(int event, String target, final List<String> username) {
            switch (event) {
                case EMMultiDeviceListener.GROUP_LEAVE:
                    ChatActivity.activityInstance.finish();
                    break;
                default:
                    break;
            }
        }
    }

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private int getExceptionMessageId(String exceptionType) {
        if(exceptionType.equals(Constant.ACCOUNT_CONFLICT)) {
            return R.string.connect_conflict;
        } else if (exceptionType.equals(Constant.ACCOUNT_REMOVED)) {
            return R.string.em_user_remove;
        } else if (exceptionType.equals(Constant.ACCOUNT_FORBIDDEN)) {
            return R.string.user_forbidden;
        }
        return R.string.Network_error;
    }

    private void showExceptionDialog(String exceptionType) {
        isExceptionDialogShow = true;
        DemoHelper.getInstance().logout(false,null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (exceptionBuilder == null)
                    exceptionBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                exceptionBuilder.setTitle(st);
                exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));
                exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exceptionBuilder = null;
                        isExceptionDialogShow = false;
                        finish();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                exceptionBuilder.setCancelable(false);
                exceptionBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e("11111111", "---------color conflictBuilder error" + e.getMessage());
            }
        }
    }

    private void showExceptionDialogFromIntent(Intent intent) {
//        EMLog.e(TAG, "showExceptionDialogFromIntent");
        if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
            showExceptionDialog(Constant.ACCOUNT_CONFLICT);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
            showExceptionDialog(Constant.ACCOUNT_REMOVED);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false)) {
            showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false)) {
            this.finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (exceptionBuilder != null) {
            exceptionBuilder.create().dismiss();
            exceptionBuilder = null;
            isExceptionDialogShow = false;
        }
        unregisterBroadcastReceiver();

        /*try {
            unregisterReceiver(internalDebugReceiver);
        } catch (Exception e) {
        }*/

    }
}
