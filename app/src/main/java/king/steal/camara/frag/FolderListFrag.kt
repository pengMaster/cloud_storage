package king.steal.camara.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import king.steal.camara.R
import king.steal.camara.adapter.FileListAdapter
import king.steal.camara.base.BaseFragment
import king.steal.camara.bean.CloudFileParser
import king.steal.camara.net.Api
import king.steal.camara.net.NetWorkUtilsK
import king.steal.camara.utils.SpUtil
import king.steal.camara.utils.ToastUtils
import king.steal.camara.utils.UIUtils
import king.steal.marrykotlin.iface.OnRequestListener
import kotlinx.android.synthetic.main.frag_folder.*


/**
 * <pre>
 * author : Wp
 * e-mail : 18141924293@163.com
 * time   : 2018/12/20
 * desc   : 文件列表
 * version: 1.0
</pre> *
 */
class FolderListFrag : BaseFragment() {

    override fun initView(inflater: LayoutInflater?): View {
        return UIUtils.inflate(R.layout.frag_folder)
    }

    override fun initData(savedInstanceState: Bundle?) {
        getCloudFileList()
        ll_top.apply {
            findViewById<ImageView>(R.id.iv_topbar_cancel).visibility = View.GONE
        }
    }

    private fun getCloudFileList() {
        val map = java.util.HashMap<String, String>()
        val imei = SpUtil.getInstance().getString("imei")
        map["imei"] = imei
        NetWorkUtilsK.doPostJson(Api.baseUrl, map, Api.getCloudFileList, object : OnRequestListener {
            override fun onSuccess(t: String) {
                val bean = Gson().fromJson(t, CloudFileParser::class.java)
                if (null != bean && bean.result != null) {
                    val adapter = FileListAdapter(mListView, bean.result, activity)
                    mListView.adapter = adapter
                }
            }

            override fun onError(errorMsg: String) {
                ToastUtils.showToast("网络错误，请重启应用")
            }
        })
    }

}
