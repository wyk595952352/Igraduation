package hello.wyk.graduation.activity;

import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nineoldandroids.view.ViewHelper;

import org.wyk.api.ApiConfig;
import org.wyk.core.LoginRegisterController;
import org.wyk.core.util.Common;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import hello.wyk.graduation.R;
import hello.wyk.graduation.adapter.LeftMenuAdapter;
import hello.wyk.graduation.fragment.ErrorQuestionFragment;
import hello.wyk.graduation.fragment.GroupFragment;
import hello.wyk.graduation.fragment.MainFragment;
import hello.wyk.graduation.fragment.RandomQuestionFragment;
import hello.wyk.graduation.util.DialogUtils;
import hello.wyk.graduation.util.ItemDataUtils;
import hello.wyk.graduation.widget.DragLayout;
import hello.wyk.graduation.widget.RoundAngleImageView;


public class MainActivity extends BaseActivity implements View.OnClickListener, LoginRegisterController.LoginCallBack, LoginRegisterController.RegisterCallBack {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.iv_icon)
    RoundAngleImageView ivIcon;
    @BindView(R.id.dl)
    DragLayout dl;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.iv_bottom)
    RoundAngleImageView ivHead;
    @BindView(R.id.text_nickname)
    TextView textNickname;
    @BindView(R.id.text_phone)
    TextView textPhone;
    @BindView(R.id.text_note)
    TextView textNote;

    LeftMenuAdapter leftMenuAdapter;
    LoginRegisterController loginRegisterController;
    FragmentTransaction transaction;
    public long lastClickBack = 0;


    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void refreshView() {
        loginRegisterController = new LoginRegisterController(this);
        setStatusBar();
        leftMenuAdapter = new LeftMenuAdapter(this, ItemDataUtils.getItemBeans());
        lv.setAdapter(leftMenuAdapter);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(flContent.getId(), new MainFragment()).commit();
    }

    @Override
    public void addEvent() {
        initDragLayout();
    }

    private void initDragLayout() {
        dl.setDragListener(new DragLayout.DragListener() {
            //界面打开的时候
            @Override
            public void onOpen() {
            }

            //界面关闭的时候
            @Override
            public void onClose() {
            }

            //界面滑动的时候
            @Override
            public void onDrag(float percent) {
                ViewHelper.setAlpha(ivIcon, 1 - percent);
            }
        });
    }

    private void refresh() {
        if (Common.userObj != null) {
            textNickname.setText(Common.userObj.getNickname());
            textPhone.setText(Common.userObj.getPhone());
            textNote.setText(Common.userObj.getIntroduction());
            Glide.with(this).load(ApiConfig.baseUrl + "/wyk/head/" + Common.userObj.getImghead()).into(ivIcon);
            Glide.with(this).load(ApiConfig.baseUrl + "/wyk/head/" + Common.userObj.getImghead()).into(ivHead);
        } else {
            textNickname.setText("未登录");
            textPhone.setText("");
            textNote.setText("");
            Glide.with(this).load(R.mipmap.none).into(ivIcon);
            Glide.with(this).load(R.mipmap.none).into(ivHead);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - lastClickBack > 2000) {
                showToast("再次点击退出应用");
            } else {
                moveTaskToBack(true);
            }
            lastClickBack = System.currentTimeMillis();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.layout_head, R.id.iv_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_head:
                if (Common.userObj == null)
                    login();
                else
                    goActivity(UserInfoActivity.class);
                break;
            case R.id.iv_icon:
                dl.open();
        }
    }


    @OnItemClick(R.id.lv)
    void OnItemClick(int position) {
        if (Common.userObj == null && position != 0) {
            login();
            return;
        }
        switch (position) {
            case 0:
                textTitle.setText("首页");
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(flContent.getId(), new MainFragment()).commit();
                dl.close();
                break;
            case 1:
                textTitle.setText("随机练习");
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(flContent.getId(), new RandomQuestionFragment()).commit();
                dl.close();
//                goActivity(QuestionActivity.class);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        dl.close();
//                    }
//                }, 500);
                break;
            case 2:
                textTitle.setText("分组练习");
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(flContent.getId(), new GroupFragment()).commit();
                dl.close();
                break;
            case 3:
                textTitle.setText("错题");
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(flContent.getId(), new ErrorQuestionFragment()).commit();
                dl.close();
                break;
            case 4:
                logout();
                break;
        }
    }

    private void logout() {
        Common.userObj = null;
        refresh();
        leftMenuAdapter.refresh();
        textTitle.setText("首页");
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(flContent.getId(), new MainFragment()).commit();
        dl.close();
    }

    private void login() {
        DialogUtils.loginDialog(this, new DialogUtils.DialogCallBack<HashMap<String, String>>() {
            @Override
            public void onPositiveButton(HashMap<String, String> map) {
                super.onPositiveButton(map);
                loginRegisterController.login(map.get("username"), map.get("password"));
            }

            @Override
            public void onNegativeButton(Object o) {
                super.onNegativeButton(o);
                register();
            }
        });
    }

    private void register() {
        DialogUtils.registerDialog(MainActivity.this, new DialogUtils.DialogCallBack<HashMap<String, String>>() {
            @Override
            public void onPositiveButton(HashMap<String, String> map) {
                super.onPositiveButton(map);
                loginRegisterController.register(map.get("username"), map.get("password"),map.get("nickname"));
            }
        });
    }

    @Override
    public void loginSuccess() {
        Snackbar.make(flContent, "欢迎回来，" + Common.userObj.getNickname(), Snackbar.LENGTH_LONG)
                .show();
        refresh();
        leftMenuAdapter.refresh();
    }

    @Override
    public void loginFailure(String s) {
        Snackbar.make(flContent, s, Snackbar.LENGTH_LONG)
                .setAction("重新登录", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        login();
                    }
                })
                .show();
    }

    @Override
    public void registerSuccess() {
        Snackbar.make(flContent, "注册成功，" + Common.userObj.getNickname(), Snackbar.LENGTH_LONG)
                .show();
        refresh();
        leftMenuAdapter.refresh();
    }

    @Override
    public void registerFailure(String s) {
        Snackbar.make(flContent, s, Snackbar.LENGTH_LONG)
                .setAction("重新注册", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        register();
                    }
                })
                .show();
    }
}
