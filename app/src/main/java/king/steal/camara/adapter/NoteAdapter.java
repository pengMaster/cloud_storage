package king.steal.camara.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import king.steal.camara.R;
import king.steal.camara.act.NoteDetailAct;
import king.steal.camara.base.BaseHolder;
import king.steal.camara.base.DefaultAdapter;
import king.steal.camara.bean.CloudNoteBean;
import king.steal.camara.iface.OnDeleteNote;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2018/12/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class NoteAdapter extends DefaultAdapter {

    Activity mActivity;
    OnDeleteNote mDeleteNote;

    public NoteAdapter(List infos, Activity activity, OnDeleteNote deleteNote) {
        super(infos);
        mActivity = activity;
        mDeleteNote = deleteNote;
    }

    @Override
    public BaseHolder getHolder(View v, int viewType) {
        return new NoteHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_note;
    }

    class NoteHolder extends BaseHolder {

        @BindView(R.id.iv_file)
        ImageView ivFile;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;
        @BindView(R.id.rl_layout)
        RelativeLayout rlLayout;

        public NoteHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(Object data, int position) {

            final CloudNoteBean bean = (CloudNoteBean)data;

            rlLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("item", new Gson().toJson(bean));
                    intent.setClass(mActivity, NoteDetailAct.class);
                    mActivity.startActivity(intent);
                }
            });


            tvName.setText(bean.getContent());
            tvTime.setText(bean.getUpdateTime());

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDeleteNote.delete(bean.getId(),bean.getContent());
                }
            });
        }
    }


}
