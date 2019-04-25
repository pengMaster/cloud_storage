package king.cloud.camara.act;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

import java.util.List;

import king.cloud.camara.AppConstants;
import king.cloud.camara.Constants;
import king.cloud.camara.R;
import king.cloud.camara.base.BaseActivity;
import king.cloud.camara.base.CommLockInfoManager;
import king.cloud.camara.utils.LockPatternUtils;
import king.cloud.camara.utils.LogUtils;
import king.cloud.camara.utils.SpUtil;
import king.cloud.camara.widget.LockPatternView;
import king.cloud.camara.widget.LockPatternViewPattern;

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
                    fetchSplashAD(GestureSelfUnlockActivity.this, unlock_layout,
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

    /** 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
