package king.steal.camara.frag

import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import com.google.gson.Gson
import king.steal.camara.R
import king.steal.camara.adapter.FileListAdapter
import king.steal.camara.base.BaseFragment
import king.steal.camara.bean.CloudFileBean
import king.steal.camara.bean.CloudFileParser
import king.steal.camara.iface.OnSelectItem
import king.steal.camara.net.Api
import king.steal.camara.net.NetWorkUtilsK
import king.steal.camara.utils.SpUtil
import king.steal.camara.utils.ToastUtils
import king.steal.camara.utils.UIUtils
import king.steal.camara.widget.CustomPopWindow
import king.steal.marrykotlin.iface.OnRequestListener
import kotlinx.android.synthetic.main.frag_folder.*
import org.jetbrains.anko.find


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

    private var fileType = "image"

    override fun initView(inflater: LayoutInflater?): View {
        return UIUtils.inflate(R.layout.frag_folder)
    }

    override fun initData(savedInstanceState: Bundle?) {
        getCloudFileList()
        initEvent()
    }

    private fun initEvent() {
        ll_top.apply {
            findViewById<ImageView>(R.id.iv_topbar_cancel).visibility = View.GONE
            find<ImageView>(R.id.iv_add).apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    showPopWindow()
                }
            }
        }
    }

    private fun showPopWindow() {
        val dialog = UIUtils.inflate(R.layout.ppw_add_folder_user)
        val ppw = CustomPopWindow.PopupWindowBuilder(activity)
                .setView(dialog)
                .setBgDarkAlpha(0.6f) // 控制亮度
                .enableBackDissmiss(false)
                .create()
                .showAtLocation(ll_top, Gravity.CENTER, 0, 0)
        dialog.apply {
            //类型切换
            find<RadioGroup>(R.id.mRgpType).setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
                override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                    when (p1) {
                        R.id.mBtnImage -> {
                            fileType = "image"
                        }
                        R.id.mBtnVideo -> {
                            fileType = "video"
                        }
                    }
                }

            })
            val folderName = find<EditText>(R.id.mEtFolderName).text
            //确定
            find<TextView>(R.id.tv_sure).setOnClickListener {
                if (null == folderName  || ""==folderName.toString()) {
                    ToastUtils.showToast("请填写文件夹名称")
                } else {
                    upLoadFolderInfo(folderName.toString(), ppw)
                }

            }
            //取消
            find<TextView>(R.id.tv_cancel).setOnClickListener {
                ppw.dissmiss()
            }
        }

    }

    /**
     * 上传文件夹信息
     */
    private fun upLoadFolderInfo(folderName: String, ppw: CustomPopWindow) {
        val map = HashMap<String, String>()
        val imei = SpUtil.getInstance().getString("imei")
        map["imei"] = imei
        map["folderType"] = fileType
        map["folderName"] = folderName
        NetWorkUtilsK.doPostJson(Api.baseUrl, map, Api.saveFolder, object : OnRequestListener {
            override fun onSuccess(t: String) {
                getCloudFileList()
                ppw.dissmiss()
            }

            override fun onError(errorMsg: String) {
                ToastUtils.showToast(errorMsg)
            }
        })
    }

    private fun getCloudFileList() {
        val map = java.util.HashMap<String, String>()
        val imei = SpUtil.getInstance().getString("imei")
        map["imei"] = imei
        NetWorkUtilsK.doPostJson(Api.baseUrl, map, Api.getCloudFileList, object : OnRequestListener {
            override fun onSuccess(t: String) {
                val bean = Gson().fromJson(t, CloudFileParser::class.java)
                if (null != bean && bean.result != null) {
                    val adapter = FileListAdapter(mListView, bean.result, activity, object : OnSelectItem {
                        override fun onSelect(bean: CloudFileBean) {
                            editFolder(bean)
                        }
                    })
                    mListView.adapter = adapter
                }
            }

            override fun onError(errorMsg: String) {
                ToastUtils.showToast("网络错误，请重启应用")
            }
        })
    }

    /**
     * 修改文件夹信息
     */
    private fun editFolder(bean: CloudFileBean) {
        ll_bottom.apply {
            visibility = View.VISIBLE
            //重命名
            find<LinearLayout>(R.id.ll_rename).setOnClickListener {
                showEditPopWindow(bean)
            }
            //删除
            find<LinearLayout>(R.id.ll_delete).setOnClickListener {
                editFolderNet(bean, "", "1", null)
            }
        }

        val animal = AnimationUtils.loadAnimation(activity,R.anim.ppw_show_anim)
        ll_bottom.animation = animal
        animal.start()
    }

    /**
     * 修改文件夹书上传
     */
    private fun editFolderNet(bean: CloudFileBean, folderName: String, isDelete: String, ppw: CustomPopWindow?) {
        val map = java.util.HashMap<String, String>()
        map["id"] = bean.id
        map["folderName"] = folderName
        map["isDelete"] = isDelete
        NetWorkUtilsK.doPostJson(Api.baseUrl, map, Api.editFolder, object : OnRequestListener {
            override fun onSuccess(t: String) {
                if (t == "success") {
                    getCloudFileList()
                }
                ppw?.dissmiss()
                ll_bottom.visibility = View.GONE
                val animal = AnimationUtils.loadAnimation(activity,R.anim.ppw_hidden_anim)
                ll_bottom.animation = animal
                animal.start()
            }

            override fun onError(errorMsg: String) {
                ToastUtils.showToast(errorMsg)
                ppw?.dissmiss()
                ll_bottom.visibility = View.GONE
                val animal = AnimationUtils.loadAnimation(activity,R.anim.ppw_hidden_anim)
                ll_bottom.animation = animal
                animal.start()
            }
        })
    }


    private fun showEditPopWindow(bean: CloudFileBean) {
        val dialog = UIUtils.inflate(R.layout.ppw_add_folder_user)
        val ppw = CustomPopWindow.PopupWindowBuilder(activity)
                .setView(dialog)
                .setBgDarkAlpha(0.6f) // 控制亮度
                .enableBackDissmiss(false)
                .create()
                .showAtLocation(ll_top, Gravity.CENTER, 0, 0)
        dialog.apply {
            //类型切换
            find<RadioButton>(R.id.mBtnImage).isEnabled = false
            find<RadioButton>(R.id.mBtnVideo).isEnabled = false
            when (bean.type) {
                "image" -> {
                    find<RadioButton>(R.id.mBtnImage).apply {
                        isChecked = true
                    }
                }
                "video" -> {
                    find<RadioButton>(R.id.mBtnVideo).apply {
                        isChecked = true
                    }
                }
            }
            val mEtFolderName = find<EditText>(R.id.mEtFolderName)
            mEtFolderName.setText(bean.name)
            val folderName = mEtFolderName.text
            //确定
            find<TextView>(R.id.tv_sure).setOnClickListener {
                if (null == folderName || ""==folderName.toString()) {
                    ToastUtils.showToast("请填写文件夹名称")
                } else {
                    editFolderNet(bean, folderName.toString(), "", ppw)
                }

            }
            //取消
            find<TextView>(R.id.tv_cancel).setOnClickListener {
                ppw.dissmiss()
            }
        }

    }


    override fun onResume() {
        super.onResume()
        getView()!!.isFocusableInTouchMode = true;
        getView()!!.requestFocus();
        getView()!!.setOnKeyListener(View.OnKeyListener { p0, p1, p2 ->
            if (p2!!.action == KeyEvent.ACTION_DOWN && p1 == KeyEvent.KEYCODE_BACK) {
                if (0 == ll_bottom.visibility) {
                    ll_bottom.visibility = View.GONE
                    val animal = AnimationUtils.loadAnimation(activity,R.anim.ppw_hidden_anim)
                    ll_bottom.animation = animal
                    animal.start()
                    return@OnKeyListener true
                } else {
                    return@OnKeyListener false
                }
            }
            false
        })
    }
}
