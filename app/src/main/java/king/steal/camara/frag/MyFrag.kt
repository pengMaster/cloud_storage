package king.steal.camara.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import king.steal.camara.R
import king.steal.camara.base.BaseFragment
import king.steal.camara.utils.LogUtils
import king.steal.camara.utils.SpUtil
import king.steal.camara.utils.UIUtils
import kotlinx.android.synthetic.main.frag_my.*

/**
 * <pre>
 * author : Wp
 * e-mail : 18141924293@163.com
 * time   : 2018/12/20
 * desc   :
 * version: 1.0
</pre> *
 */
class MyFrag : BaseFragment() {

    override fun initView(inflater: LayoutInflater?): View {
        return UIUtils.inflate(R.layout.frag_my)
    }

    override fun initData(savedInstanceState: Bundle?) {
        val imei = SpUtil.getInstance().getString("imei")
        mTvUserName.text = imei
    }

}
