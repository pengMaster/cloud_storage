package king.cloud.camara.mvp.contract;

import android.content.Context;



import java.util.List;

import king.cloud.camara.base.BasePresenter;
import king.cloud.camara.base.BaseView;
import king.cloud.camara.bean.CommLockInfo;

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
