package king.steal.camara.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

import java.util.List;
import king.steal.camara.AppConstants;
import king.steal.camara.Constants;
import king.steal.camara.R;
import king.steal.camara.base.BaseActivity;
import king.steal.camara.bean.LockStage;
import king.steal.camara.mvp.contract.GestureCreateContract;
import king.steal.camara.mvp.p.GestureCreatePresenter;
import king.steal.camara.utils.LockPatternUtils;
import king.steal.camara.utils.LogUtils;
import king.steal.camara.utils.SpUtil;
import king.steal.camara.utils.SystemBarHelper;
import king.steal.camara.utils.ToastUtils;
import king.steal.camara.widget.LockPatternView;
import king.steal.camara.widget.LockPatternViewPattern;

/**
 * 设置密码锁页面
 */

public class CreatePwdActivity extends BaseActivity implements View.OnClickListener,
        GestureCreateContract.View {

    private TextView mLockTip;
    private LockPatternView mLockPatternView;
    private TextView mBtnReset;
    private LinearLayout llLayout;
    //图案锁相关
    private LockStage mUiStage = LockStage.Introduction;
    protected List<LockPatternView.Cell> mChosenPattern = null; //密码
    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;
    private GestureCreatePresenter mGestureCreatePresenter;
    private RelativeLayout mTopLayout;

    @Override
    public int layoutId() {
        return R.layout.act_password;
    }

    @Override
    public void initView() {
        mLockPatternView = (LockPatternView) findViewById(R.id.lock_pattern_view);
        mLockTip = (TextView) findViewById(R.id.lock_tip);
        llLayout = (LinearLayout) findViewById(R.id.llLayout);
        mBtnReset = (TextView) findViewById(R.id.btn_reset);
        mTopLayout = (RelativeLayout) findViewById(R.id.top_layout);
//        mTopLayout.setPadding(0, SystemBarHelper.getStatusBarHeight(this), 0, 0);

    }

    @Override
    public void initData() {
        mGestureCreatePresenter = new GestureCreatePresenter(this, this);
        initLockPatternView();
    }

    @Override
    public void start() {
        mBtnReset.setOnClickListener(this);
    }


    /**
     * 初始化锁屏控件
     */
    private void initLockPatternView() {
        mLockPatternUtils = new LockPatternUtils(this);
        mPatternViewPattern = new LockPatternViewPattern(mLockPatternView);
        mPatternViewPattern.setPatternListener(new LockPatternViewPattern.onPatternListener() {
            @Override
            public void onPatternDetected(List<LockPatternView.Cell> pattern) {
                mGestureCreatePresenter.onPatternDetected(pattern, mChosenPattern, mUiStage);
            }
        });
        mLockPatternView.setOnPatternListener(mPatternViewPattern);
        mLockPatternView.setTactileFeedbackEnabled(true);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset:
                setStepOne();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    /**
     * 恢复到第一步
     */
    private void setStepOne() {
        mGestureCreatePresenter.updateStage(LockStage.Introduction);
        mLockTip.setText(getString(R.string.lock_recording_intro_header));
    }

    private void gotoLockMainActivity() {
        boolean isSplash = getIntent().getBooleanExtra("isSplash", false);
        if (isSplash) {
            SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, true); //开启应用锁开关
            SpUtil.getInstance().putBoolean(AppConstants.LOCK_IS_FIRST_LOCK, false);
            fetchSplashAD(CreatePwdActivity.this, llLayout,
                    Constants.APPID, Constants.SplashPosID, new SplashADListener() {
                        @Override
                        public void onADDismissed() {
                            LogUtils.e("onADDismissed");
                            intoMainActivity();
                        }

                        @Override
                        public void onNoAD(AdError adError) {
                            LogUtils.e("onNoAD：", adError.getErrorCode() + "___" + adError.getErrorMsg());
                            intoMainActivity();
                        }

                        @Override
                        public void onADPresent() {
                            LogUtils.e("onADPresent");
                        }

                        @Override
                        public void onADClicked() {
                            LogUtils.e("onADClicked");

                        }

                        @Override
                        public void onADTick(long l) {
                            LogUtils.e("onADTick");
                            if (l == 0)
                                intoMainActivity();
                        }

                        @Override
                        public void onADExposure() {
                            LogUtils.e("onADExposure");

                        }
                    });

        } else {
            ToastUtils.showToast("重置成功");
            finish();
        }
    }
    /**
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     *
     * @param activity    展示广告的 activity
     * @param adContainer 展示广告的大容器
     * @param appId       应用 ID
     * @param posId       广告位 ID
     * @param adListener  广告状态监听器
     */
    private void fetchSplashAD(Activity activity, ViewGroup adContainer,
                               String appId, String posId, SplashADListener adListener) {
        SplashAD splashAD = new SplashAD(activity, adContainer, appId, posId, adListener);
    }


    private void intoMainActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    /**
     * 更新当前锁的状态
     */
    @Override
    public void updateUiStage(LockStage stage) {
        mUiStage = stage;
    }

    /**
     * 更新当前密码
     */
    @Override
    public void updateChosenPattern(List<LockPatternView.Cell> mChosenPattern) {
        this.mChosenPattern = mChosenPattern;
    }

    /**
     * 更新提示信息
     */
    @Override
    public void updateLockTip(String text, boolean isToast) {
        mLockTip.setText(text);
    }

    /**
     * 更新提示信息
     */
    @Override
    public void setHeaderMessage(int headerMessage) {
        mLockTip.setText(headerMessage);
    }

    /**
     * LockPatternView的一些配置
     */
    @Override
    public void lockPatternViewConfiguration(boolean patternEnabled, LockPatternView.DisplayMode displayMode) {
        if (patternEnabled) {
            mLockPatternView.enableInput();
        } else {
            mLockPatternView.disableInput();
        }
        mLockPatternView.setDisplayMode(displayMode);
    }

    /**
     * 初始化
     */
    @Override
    public void Introduction() {
        clearPattern();
    }

    @Override
    public void HelpScreen() {

    }

    /**
     * 路径太短
     */
    @Override
    public void ChoiceTooShort() {
        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);  //路径太短
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
    }

    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
        }
    };

    /**
     * 画完第一步转到第二步
     */
    @Override
    public void moveToStatusTwo() {

    }

    /**
     * 清空控件路径
     */
    @Override
    public void clearPattern() {
        mLockPatternView.clearPattern();
    }

    /**
     * 第一次和第二次画得不一样
     */
    @Override
    public void ConfirmWrong() {
        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);  //路径太短
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
    }

    /**
     * 画成功了
     */
    @Override
    public void ChoiceConfirmed() {
        mLockPatternUtils.saveLockPattern(mChosenPattern); //保存密码
        clearPattern();
        gotoLockMainActivity();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGestureCreatePresenter.onDestroy();
    }


}
