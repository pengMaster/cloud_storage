package king.cloud.camara.act;

import android.content.pm.ActivityInfo;
import android.view.View;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import king.cloud.camara.R;
import king.cloud.camara.base.BaseActivity;
import king.cloud.camara.utils.LogUtils;


/**
 * Created by Administrator on 2018/7/24.
 */

public class VideoPlayActivity extends BaseActivity {

    @BindView(R.id.video_player)
    StandardGSYVideoPlayer videoPlayer;
    OrientationUtils orientationUtils;

    private String videourl = "";

    @Override
    public int layoutId() {
        return R.layout.activity_video_play;
    }

    @Override
    public void initData() {
        LogUtils.e("");
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        videourl = getIntent().getStringExtra("path");
        String name = getIntent().getStringExtra("name");
        videoPlayer.setUp(videourl, true, name);

        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        videoPlayer.startPlayLogic();
        videoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onAutoComplete(String url, Object... objects) {
                //广告结束，释放
                videoPlayer.onVideoReset();
                videoPlayer.startPlayLogic();
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {

            }

        });
    }

    @Override
    public void start() {

    }


    @Override
    public void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }

}
