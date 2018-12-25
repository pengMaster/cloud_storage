package king.steal.camara.frag

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import king.steal.camara.R
import king.steal.camara.act.NoteDetailAct
import king.steal.camara.adapter.NoteAdapter
import king.steal.camara.base.BaseFragment
import king.steal.camara.bean.CloudFileParser
import king.steal.camara.bean.CloudNoteParser
import king.steal.camara.iface.OnDeleteNote
import king.steal.camara.net.Api
import king.steal.camara.net.NetWorkUtilsK
import king.steal.camara.utils.LogUtils
import king.steal.camara.utils.SpUtil
import king.steal.camara.utils.ToastUtils
import king.steal.camara.utils.UIUtils
import king.steal.marrykotlin.iface.OnRequestListener
import kotlinx.android.synthetic.main.frag_note.*

/**
 * <pre>
 * author : Wp
 * e-mail : 18141924293@163.com
 * time   : 2018/12/20
 * desc   :
 * version: 1.0
</pre> *
 */
class NoteListFrag : BaseFragment() {

    override fun initView(inflater: LayoutInflater?): View {
        return UIUtils.inflate(R.layout.frag_note)
    }

    override fun initData(savedInstanceState: Bundle?) {
        downLoadNote()
        ll_top.apply {
            findViewById<ImageView>(R.id.iv_topbar_cancel).visibility = View.GONE
            findViewById<ImageView>(R.id.iv_add).apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    val intent = Intent()
                    intent.putExtra("isAdd",true)
                    intent.setClass(activity, NoteDetailAct::class.java)
                    startActivityForResult(intent,200)
                }
            }
            findViewById<TextView>(R.id.tv_topbar_title).text = "记事本"
        }
    }

    private fun downLoadNote() {
        val map = HashMap<String, String>()
        val imei = SpUtil.getInstance().getString("imei")
        map["imei"] = imei
        NetWorkUtilsK.doPostJson(Api.baseUrl, map, Api.getNoteList, object : OnRequestListener {
            override fun onSuccess(t: String) {
                val bean = Gson().fromJson(t, CloudNoteParser::class.java)
                if (null != bean.result) {
                    mRecyclerView.visibility = View.VISIBLE
                    val adapter = NoteAdapter(bean.result, activity, object : OnDeleteNote {
                        override fun delete(id: String, content: String) {
                            saveNote(id, "1", content)
                        }
                    })
                    mRecyclerView.layoutManager = LinearLayoutManager(context)
                    mRecyclerView.itemAnimator = DefaultItemAnimator() as RecyclerView.ItemAnimator?
                    mRecyclerView.adapter = adapter
                } else {
                    mRecyclerView.visibility = View.GONE
                }

            }

            override fun onError(errorMsg: String) {
            }
        })
    }


    private fun saveNote(id: String, isDelete: String, content: String) {
        val map = java.util.HashMap<String, String>()
        val imei = SpUtil.getInstance().getString("imei")
        map["id"] = id
        map["imei"] = imei
        map["isDelete"] = isDelete
        map["content"] = content
        NetWorkUtilsK.doPostJson(Api.baseUrl, map, Api.saveNote, object : OnRequestListener {
            override fun onSuccess(t: String) {
                downLoadNote()
            }

            override fun onError(errorMsg: String) {
                ToastUtils.showToast(errorMsg)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        downLoadNote()
    }
}
