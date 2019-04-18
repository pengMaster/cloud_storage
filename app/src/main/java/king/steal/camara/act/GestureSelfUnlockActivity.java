package king.steal.camara.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import java.util.List;

import cdc.sed.yff.nm.sp.SplashViewSettings;
import cdc.sed.yff.nm.sp.SpotListener;
import cdc.sed.yff.nm.sp.SpotManager;
import king.steal.camara.AppConstants;
import king.steal.camara.R;
import king.steal.camara.base.BaseActivity;
import king.steal.camara.base.CommLockInfoManager;
import king.steal.camara.utils.LockPatternUtils;
import king.steal.camara.utils.SpUtil;
import king.steal.camara.utils.SystemBarHelper;
import king.steal.camara.widget.LockPatternView;
import king.steal.camara.widget.LockPatternViewPattern;

/**
 * 解锁页面
 */

public class GestureSelfUnlockActivity extends BaseActivity {

    private LockPatternView mLockPatternView;
    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;
    private int mFailedPatternAttemptsSinceLastTimeout = 0;
    private String actionFrom;//按返回键的操作
    private String pkgName; //解锁应用的包名
    private CommLockInfoManager mManager;
    private RelativeLayout mTopLayout;
    private RelativeLayout unlock_layout;

    private TextureView mTextureView;

    private ImageView btn_back;

    @Override
    public int layoutId() {
        return R.layout.activity_gesture_self_unlock;
    }

    @Override
    public void initView() {
        mLockPatternView = (LockPatternView) findViewById(R.id.unlock_lock_view);
        mTopLayout = (RelativeLayout) findViewById(R.id.top_layout);
        mTextureView = (TextureView) findViewById(R.id.texture_view);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        unlock_layout = (RelativeLayout) findViewById(R.id.unlock_layout);
    }

    @Override
    public void start() {

    }

    @Override
    public void initData() {
        mManager = new CommLockInfoManager(this);
        //获取解锁应用的包名
        pkgName = getIntent().getStringExtra(AppConstants.LOCK_PACKAGE_NAME);
        //获取按返回键的操作
        actionFrom = getIntent().getStringExtra(AppConstants.LOCK_FROM);

        initLockPatternView();
        initAction();

    }

    private void initAction() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 初始化解锁控件
     */
    private void initLockPatternView() {
        mLockPatternUtils = new LockPatternUtils(this);
        mPatternViewPattern = new LockPatternViewPattern(mLockPatternView);
        mPatternViewPattern.setPatternListener(new LockPatternViewPattern.onPatternListener() {
            @Override
            public void onPatternDetected(List<LockPatternView.Cell> pattern) {
                if (mLockPatternUtils.checkPattern(pattern)) { //解锁成功,更改数据库状态
                    mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                    SplashViewSettings splashViewSettings = new SplashViewSettings();
                    splashViewSettings.setTargetClass(MainActivity.class);
                    splashViewSettings.setAutoJumpToTargetWhenShowFailed(true);
                    splashViewSettings.setSplashViewContainer(unlock_layout);
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
                    mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                    if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
                        mFailedPatternAttemptsSinceLastTimeout++;
                        int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailedPatternAttemptsSinceLastTimeout;
                        if (retry >= 0) {
                        }
                    } else {
                    }
                    if (mFailedPatternAttemptsSinceLastTimeout >= 3) { //失败次数大于3次
                        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_RECORD_PIC, false)) {

                        }
                    }
                    if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) { //失败次数大于阻止用户前的最大错误尝试次数

                    } else {
                        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
                    }
                }
            }
        });
        mLockPatternView.setOnPatternListener(mPatternViewPattern);
        mLockPatternView.setTactileFeedbackEnabled(true);
    }

    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
        }
    };
    @Override
    public void onBackPressed() {
        // 如果有需要，可以点击后退关闭插播广告。
        if (SpotManager.getInstance(getApplicationContext()).isSpotShowing()) {
            SpotManager.getInstance(getApplicationContext()).hideSpot();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 插屏广告
        SpotManager.getInstance(getApplicationContext()).onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 插屏广告
        SpotManager.getInstance(getApplicationContext()).onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 开屏展示界面的 onDestroy() 回调方法中调用
        SpotManager.getInstance(getApplicationContext()).onDestroy();
    }
}
