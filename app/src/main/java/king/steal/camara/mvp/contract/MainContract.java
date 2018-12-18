package king.steal.camara.mvp.contract;

import android.content.Context;



import java.util.List;

import king.steal.camara.base.BasePresenter;
import king.steal.camara.base.BaseView;
import king.steal.camara.bean.CommLockInfo;

/**
 * Created by xian on 2017/2/17.
 */

public interface MainContract {
    interface View extends BaseView<Presenter> {
        void loadAppInfoSuccess(List<CommLockInfo> list);
    }

    interface Presenter extends BasePresenter {
        void loadAppInfo(Context context, boolean isSort);

        void loadLockAppInfo(Context context);

        void onDestroy();
    }
}
