package king.steal.camara.adapter;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import king.steal.camara.R;
import king.steal.camara.base.BaseAppAdapter;
import king.steal.camara.base.BaseHolderL;
import king.steal.camara.bean.CloudIssueBean;
import king.steal.camara.utils.SpUtil;
import king.steal.camara.utils.UIUtils;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2019/01/09
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class IssueAdapter extends BaseAppAdapter {

    public IssueAdapter(AbsListView listView, List datas) {
        super(listView, datas);
    }

    @Override
    protected BaseHolderL getHolder() {
        return new IssueHolder();
    }

    class IssueHolder extends BaseHolderL {

        @BindView(R.id.mIvAvatar)
        ImageView mIvAvatar;
        @BindView(R.id.mTvName)
        TextView mTvName;
        @BindView(R.id.mTvContent)
        TextView mTvContent;
        @BindView(R.id.mTvTime)
        TextView mTvTime;

        @Override
        protected View initView() {
            return UIUtils.inflate(R.layout.item_issue);
        }

        @Override
        public void refreshView() {

            CloudIssueBean data = (CloudIssueBean) getData();
            mTvContent.setText(data.getContent());
            mTvName.setText(data.getUserId());
            mTvTime.setText(data.getCreateTime());
        }
    }

}
