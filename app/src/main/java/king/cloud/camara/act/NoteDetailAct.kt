package king.cloud.camara.act

import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import king.cloud.camara.R
import king.cloud.camara.base.BaseActivity
import king.cloud.camara.bean.CloudNoteBean
import king.cloud.camara.net.Api
import king.cloud.camara.net.NetWorkUtilsK
import king.cloud.camara.utils.LogUtils
import king.cloud.camara.utils.SpUtil
import king.cloud.camara.utils.ToastUtils
import king.cloud.marrykotlin.iface.OnRequestListener
import kotlinx.android.synthetic.main.act_note_detail.*
import java.util.*

/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2018/12/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class NoteDetailAct : BaseActivity() {

    lateinit var itemBean: CloudNoteBean
    private var isAdd: Boolean = false

    override fun layoutId(): Int {
        return R.layout.act_note_detail
    }

    override fun initData() {
        val item = intent.getStringExtra("item")
        isAdd = intent.getBooleanExtra("isAdd", false)
        if (!isAdd) {
            itemBean = Gson().fromJson(item, CloudNoteBean::class.java)
            mEditText.setText(itemBean.content)
        }
    }


    override fun initView() {
        ll_top.apply {
            findViewById<Button>(R.id.btnBack).setOnClickListener {
                finish()
            }
            findViewById<TextView>(R.id.tv_topbar_title).text = "笔记内容"
        }
    }

    override fun start() {
        LogUtils.e("")
    }

    override fun onBackPressed() {
        if ("" != mEditText.text.toString())
            saveNote()
        else
            finish()
    }

    private fun saveNote() {
        val map = java.util.HashMap<String, String>()
        val imei = SpUtil.getInstance().getString("imei")
        map["imei"] = imei
        map["content"] = mEditText.text.toString()
        if (isAdd) {
            val uuid = UUID.randomUUID()
            map["id"] = uuid!!.toString().replace("-", "")
            map["isDelete"] = "0"
        } else {
            map["id"] = itemBean.id
            map["isDelete"] = itemBean.isDelete
        }
        NetWorkUtilsK.doPostJson(Api.baseUrl, map, Api.saveNote, object : OnRequestListener {
            override fun onSuccess(t: String) {
                ToastUtils.showToast("保存成功")
                finish()
            }

            override fun onError(errorMsg: String) {
                ToastUtils.showToast(errorMsg)
            }
        })
    }

}