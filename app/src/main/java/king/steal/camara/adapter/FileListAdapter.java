package king.steal.camara.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import king.steal.camara.MyApplication;
import king.steal.camara.R;
import king.steal.camara.act.FileImageDetailAct;
import king.steal.camara.act.FileVideoDetailAct;
import king.steal.camara.base.BaseAppAdapter;
import king.steal.camara.base.BaseHolderL;
import king.steal.camara.bean.CloudFileBean;
import king.steal.camara.iface.OnSelectItem;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2018/12/05
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class FileListAdapter extends BaseAppAdapter {

    private Activity mActivity;
    private OnSelectItem mOnSelectItem;

    public FileListAdapter(AbsListView listView, List datas, Activity activity, OnSelectItem onSelectItem) {
        super(listView, datas);
        mActivity = activity;
        mOnSelectItem = onSelectItem;
    }

    @Override
    protected BaseHolderL getHolder() {
        return new Holder();
    }

    class Holder extends BaseHolderL {


        @BindView(R.id.iv_file)
        ImageView ivFile;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.iv_menu)
        ImageView ivMenu;
        @BindView(R.id.rl_layout)
        RelativeLayout rlLayout;

        @Override
        protected View initView() {
            return LayoutInflater.from(MyApplication.Companion.getContext()).inflate(R.layout.item_record, null);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void refreshView() {
            final CloudFileBean data = (CloudFileBean)getData();
            tvName.setText(data.getName());
            tvTime.setText(data.getUpdateTime());
            if ("1".equals(data.getIsCanEdit())){
                ivMenu.setVisibility(View.VISIBLE);
            }

            rlLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    if (data.getType().equals("image")) {
                        intent.setClass(mActivity, FileImageDetailAct.class);
                    }
                    if (data.getType().equals("video")) {
                        intent.setClass(mActivity, FileVideoDetailAct.class);
                    }
                    intent.putExtra("item", new Gson().toJson(data));
                    mActivity.startActivity(intent);
                }
            });

            ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnSelectItem.onSelect(data);
                }
            });
        }
    }
}
