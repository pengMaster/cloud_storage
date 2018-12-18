package king.steal.camara.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dmcbig.mediapicker.entity.Media;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import king.steal.camara.MyApplication;
import king.steal.camara.R;
import king.steal.camara.act.ImagePreAct;
import king.steal.camara.act.VideoPlayActivity;
import king.steal.camara.base.BaseAppAdapter;
import king.steal.camara.base.BaseHolder;
import king.steal.camara.base.BaseHolderL;
import king.steal.camara.base.DefaultAdapter;
import king.steal.camara.iface.OnLongClick;
import king.steal.camara.utils.FileUtils;
import king.steal.camara.utils.ToastUtils;
import king.steal.camara.utils.UIUtils;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2018/12/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class FileDetailAdapter extends BaseAppAdapter {

    private Activity mActivity;
    private boolean mIsVideo;
    private OnLongClick mOnLongClick;


    public FileDetailAdapter(AbsListView listView, List datas, Activity activity
            , boolean isVideo,OnLongClick onLongClick) {
        super(listView, datas);
        mActivity = activity;
        mIsVideo = isVideo;
        mOnLongClick = onLongClick;
    }


    @Override
    protected BaseHolderL getHolder() {
        return new FileDetailHolder();
    }

    class FileDetailHolder extends BaseHolderL {

        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_name)
        TextView tvName;

        @Override
        protected View initView() {
            return UIUtils.inflate(R.layout.item_file_detail);
        }

        @Override
        public void refreshView() {
            final Media data = (Media) getData();
            Glide.with(mActivity)                             //配置上下文
                    .load(Uri.fromFile(new File(data.path)))  //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                    .into(ivImage);

            tvName.setText(data.name);

            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    if (!mIsVideo) {
                        intent.setClass(mActivity, ImagePreAct.class);
                    } else {
                        intent.setClass(mActivity, VideoPlayActivity.class);
                    }
                    intent.putExtra("path", data.path);
                    intent.putExtra("name", data.name);
                    mActivity.startActivity(intent);
                }
            });

            ivImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnLongClick.onLongClick(data.path,data.name);
                    return true;
                }
            });

        }
    }
}
