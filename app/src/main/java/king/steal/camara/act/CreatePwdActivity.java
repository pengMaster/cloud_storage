package king.steal.camara.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.List;

import cdc.sed.yff.nm.sp.SplashViewSettings;
import cdc.sed.yff.nm.sp.SpotListener;
import cdc.sed.yff.nm.sp.SpotManager;
import king.steal.camara.AppConstants;
import king.steal.camara.R;
import king.steal.camara.base.BaseActivity;
import king.steal.camara.bean.LockStage;
import king.steal.camara.mvp.contract.GestureCreateContract;
import king.steal.camara.mvp.p.GestureCreatePresenter;
import king.steal.camara.utils.LockPatternUtils;
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
            SplashViewSettings splashViewSettings = new SplashViewSettings();
            splashViewSettings.setTargetClass(MainActivity.class);
            splashViewSettings.setAutoJumpToTargetWhenShowFailed(true);
            splashViewSettings.setSplashViewContainer(llLayout);
            SpotManager.getInstance(getApplicationContext()).showSplash(getApplicationContext(),
                    splashViewSettings, new SpotListener() {
                        @Override
                        public void onShowSuccess() {
                            finish();
                        }

                        @Override
                        public void onShowFailed(int i) {
                            finish();
                        }

                        @Override
                        public void onSpotClosed() {
                            finish();
                        }

                        @Override
                        public void onSpotClicked(boolean b) {
                            finish();
                        }
                    });

        } else {
            ToastUtils.showToast("重置成功");
            finish();
        }
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
