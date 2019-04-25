package king.cloud.camara.act

import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import king.cloud.camara.R
import king.cloud.camara.adapter.IssueAdapter
import king.cloud.camara.base.BaseActivity
import king.cloud.camara.bean.CloudIssueParser
import king.cloud.camara.net.Api
import king.cloud.camara.net.NetWorkUtilsK
import king.cloud.camara.utils.SpUtil
import king.cloud.camara.utils.ToastUtils
import king.cloud.marrykotlin.iface.OnRequestListener
import kotlinx.android.synthetic.main.act_issue.*
import org.jetbrains.anko.find

/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2019/01/09
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class IssueUploadAct : BaseActivity() {

    override fun layoutId(): Int {
        return R.layout.act_issue
    }

    override fun initData() {
        initEvent()
    }

    private fun initEvent() {
        mBtnSave.setOnClickListener {
            if ("" == mEditText.text.toString()) {
                ToastUtils.showToast("请填写意见")
                return@setOnClickListener
            }
            saveIssue()
        }
    }

    override fun initView() {
        ll_top.apply {
            find<TextView>(R.id.tv_topbar_title).text = "意见反馈"
            find<Button>(R.id.btnBack).setOnClickListener {
                finish()
            }
        }
    }

    override fun start() {
        getIssues()    }

    /**
     * getIssues
     */
    private fun getIssues() {
        val map = HashMap<String, String>()
        val imei = SpUtil.getInstance().getString("imei")
        map["imei"] = imei
        NetWorkUtilsK.doPostJson(Api.baseUrl, map, Api.getIssues, object : OnRequestListener {
            override fun onSuccess(t: String) {
                val lists = Gson().fromJson(t, CloudIssueParser::class.java)
                if (null != lists && null != lists.result) {
                    val adapter = IssueAdapter(mListView, lists.result)
                    mListView.adapter = adapter
                }
            }

            override fun onError(errorMsg: String) {
                ToastUtils.showToast(errorMsg)
            }
        })
    }

    /**
     * saveIssue
     */
    private fun saveIssue() {
        val map = HashMap<String, String>()
        val imei = SpUtil.getInstance().getString("imei")
        map["imei"] = imei
        map["content"] = mEditText.text.toString()
        NetWorkUtilsK.doPostJson(Api.baseUrl, map, Api.saveIssue, object : OnRequestListener {
            override fun onSuccess(t: String) {
                if (t == "success") {
                    getIssues()
                    ToastUtils.showToast("保存成功")
                    mEditText.setText("")
                } else {
                    ToastUtils.showToast("保存失败")
                }
            }

            override fun onError(errorMsg: String) {
                ToastUtils.showToast(errorMsg)
            }
        })
    }
}