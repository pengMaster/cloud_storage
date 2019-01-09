package king.steal.camara.act

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import com.google.gson.Gson
import king.steal.camara.R
import king.steal.camara.base.BaseActivity
import king.steal.camara.bean.CloudFileBean
import kotlinx.android.synthetic.main.act_file_detail.*
import com.dmcbig.mediapicker.PickerConfig
import com.dmcbig.mediapicker.PickerActivity
import com.dmcbig.mediapicker.entity.Media
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import king.steal.camara.adapter.FileDetailAdapter
import king.steal.camara.iface.OnLongClick
import king.steal.camara.net.Api
import king.steal.camara.utils.*
import king.steal.tool.StealUtils
import okhttp3.Call
import org.jetbrains.anko.async
import java.io.File
import java.lang.Exception


/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2018/12/14
 *     desc   : 文件详情
 *     version: 1.0
 * </pre>
 */
class FileImageDetailAct : BaseActivity() {

    var select: ArrayList<Media>? = ArrayList()
    var exists: ArrayList<Media>? = ArrayList()
    var selectsUpload: ArrayList<Media>? = ArrayList()
    lateinit var itemBean: CloudFileBean
    lateinit var adapter: FileDetailAdapter

    override fun layoutId(): Int {
        return R.layout.act_file_detail
    }

    override fun initData() {
        val item = intent.getStringExtra("item")
        itemBean = Gson().fromJson(item, CloudFileBean::class.java)
        val outBasePath = FileUtils.getPackagePath(applicationContext)
        //文件夹创建
        val fileDir = File("$outBasePath/${itemBean.id}")
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }
        exists = FileUtils.getImagesByPath("$outBasePath/${itemBean.id}")
        adapter = FileDetailAdapter(mGridView, exists, this@FileImageDetailAct, false, object : OnLongClick {
            override fun onLongClick(path: String, name: String) {
                itemLongHandle(path, name)
            }
        })
        mGridView.adapter = adapter
    }

    /**
     * 删除 还原 操作
     */
    @SuppressLint("RestrictedApi")
    private fun itemLongHandle(path: String, name: String) {
        ll_bottom.apply {
            visibility = View.VISIBLE
            findViewById<LinearLayout>(R.id.ll_restore).setOnClickListener {
                val imageParentPath = SpUtil.getInstance().getString("imageParentPath")
                if ("" == imageParentPath) {
                    ToastUtils.showToast("请先加密文件")
                    return@setOnClickListener
                }
                FileUtils.CopySdcardFile(path, imageParentPath + name)
                val file = File(path)
                if (file.isFile) {
                    //通知相册更新
                    file.delete()
                    for (index in 0 until exists!!.size) {
                        if (index < exists!!.size && exists!![index].path == path)
                            exists!!.remove(exists!![index])
                    }

                    adapter = FileDetailAdapter(mGridView, exists, this@FileImageDetailAct, false, object : OnLongClick {
                        @SuppressLint("RestrictedApi")
                        override fun onLongClick(path: String, name: String) {
                            itemLongHandle(path, name)
                        }
                    })
                    mGridView.adapter = adapter

                    mButton.visibility = View.VISIBLE
                    visibility = View.GONE

                    notifyFileUpdate(imageParentPath + name, name)

                    ToastUtils.showToast("还原成功")
                }
            }
            findViewById<LinearLayout>(R.id.ll_delete).setOnClickListener {
                val file = File(path)
                if (file.isFile) {
                    //通知相册更新
                    file.delete()
                    for (index in 0 until exists!!.size) {
                        if (index < exists!!.size && exists!![index].path == path)
                            exists!!.remove(exists!![index])
                    }

                    adapter = FileDetailAdapter(mGridView, exists, this@FileImageDetailAct, false, object : OnLongClick {
                        @SuppressLint("RestrictedApi")
                        override fun onLongClick(path: String, name: String) {
                            itemLongHandle(path, name)
                        }
                    })
                    mGridView.adapter = adapter

                    mButton.visibility = View.VISIBLE
                    visibility = View.GONE

                    ToastUtils.showToast("删除成功")
                }
            }
        }
        mButton.visibility = View.GONE
        val animal = AnimationUtils.loadAnimation(this@FileImageDetailAct,R.anim.ppw_show_anim)
        ll_bottom.animation = animal
        animal.start()
    }

    @SuppressLint("RestrictedApi")
    override fun initView() {
        top_layout.findViewById<Button>(R.id.btnBack).setOnClickListener {
            if (0 == ll_bottom.visibility) {
                ll_bottom.visibility = View.GONE
                mButton.visibility = View.VISIBLE
                val animal = AnimationUtils.loadAnimation(this@FileImageDetailAct,R.anim.ppw_hidden_anim)
                ll_bottom.animation = animal
                animal.start()
            } else {
                finish()
            }
        }
    }

    override fun start() {
        LogUtils.e("")
    }


    /**
     * 点击FloatingActionButton时旋转
     */
    fun rotate(view: View) {
        val intent = Intent(this@FileImageDetailAct, PickerActivity::class.java)
        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE)//default image and video (Optional)
        val maxSize = 10874368L//long long long
        intent.putExtra(PickerConfig.MAX_SELECT_SIZE, maxSize) //default 10MB (Optional)
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 8)  //default 40 (Optional)
        //intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST, select) // (Optional)
        startActivityForResult(intent, 200)
    }

    /**
     * 选择后更新布局
     */
    private fun setView(select: ArrayList<Media>?) {

        for (index in 0 until exists!!.size) {
            val media = exists!![index]
            for (position in 0 until select!!.size) {
                if (position < select.size) {
                    val selectItem = select[position]
                    if (selectItem.name == media.name) {
                        select.remove(selectItem)
                    }
                }
            }
        }

        select!!.forEach {
            val outBasePath = FileUtils.getPackagePath(applicationContext)
            val outPath = "$outBasePath/${itemBean.id}/${it.name}"
            val outFile = File(outPath)
            if (!outFile.exists()) {

                val media = Media(outPath, it.name, it.time, it.mediaType, it.size, it.id, it.parentDir)
                exists!!.add(media)
                selectsUpload!!.add(media)

                val isSuccess = FileUtils.CopySdcardFile(it.path, outPath)
                val file = File(it.path)
                if (file.isFile) {
                    //通知相册更新
                    file.delete()
                    updateFileFromDatabase(applicationContext, it.path)
                }

                if (!isSuccess) {
                    ToastUtils.showToast("${it.name}文件加密失败，请重试")
                }
            } else {
                ToastUtils.showToast("${it.name}文件已加密")
            }
        }

        adapter = FileDetailAdapter(mGridView, exists, this@FileImageDetailAct, false, object : OnLongClick {
            @SuppressLint("RestrictedApi")
            override fun onLongClick(path: String, name: String) {
                itemLongHandle(path, name)
            }
        })
        mGridView.adapter = adapter

        if (selectsUpload!!.size > 0) {
            upLoadPics(selectsUpload)
        }

    }

    /**
     * 批量上传图片
     */
    private fun upLoadPics(select: ArrayList<Media>?) {
        val imei = SpUtil.getInstance().getString("imei")
        val files = HashMap<String, File>()
        for (index in 0 until select!!.size) {
            files[select[index].name] = File(select[index].path)
        }
        OkHttpUtils.post().addParams("dir", imei)
                .files("file", files)
                .url(Api.baseUrl)
                .addHeader("method", Api.upLoadCloudPic)
                .build().execute(object : StringCallback() {
                    override fun onResponse(p0: String?, p1: Int) {
                        LogUtils.e(p0)
                        selectsUpload!!.clear()
                    }

                    override fun onError(p0: Call?, p1: Exception?, p2: Int) {
                        selectsUpload!!.clear()
                        if (p1 != null) {
                            LogUtils.e(p1.message)
                        }
                    }
                })

    }

    /**
     * 删除文件后更新数据库
     */
    fun updateFileFromDatabase(context: Context, filepath: String) {
        val where = MediaStore.Images.Media.DATA + " like \"" + filepath + "%" + "\""
        context.contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, where, null)

    }

    /**
     * 添加文件后更新数据库
     */
    fun notifyFileUpdate(filePath: String, fileName: String) {
        AlbumNotifyHelper.insertVideoToMediaStore(this@FileImageDetailAct, filePath, System.currentTimeMillis(), 500)
        AlbumNotifyHelper.insertImageToMediaStore(this@FileImageDetailAct, filePath, System.currentTimeMillis())
        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://$filePath")))
        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(filePath)))
        val localUri = Uri.fromFile(File(filePath + fileName))
        val localIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri)
        sendBroadcast(localIntent)
        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(filePath))))
    }

    @SuppressLint("RestrictedApi")
    override fun onBackPressed() {
        if (0 == ll_bottom.visibility) {
            ll_bottom.visibility = View.GONE
            val animal = AnimationUtils.loadAnimation(this@FileImageDetailAct,R.anim.ppw_hidden_anim)
            ll_bottom.animation = animal
            animal.start()
            mButton.visibility = View.VISIBLE
        } else {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == PickerConfig.RESULT_CODE) {
            select = data!!.getParcelableArrayListExtra<Media>(PickerConfig.EXTRA_RESULT)

            if (null == select || select!!.size == 0) {
                return
            }
            val imageParentPath = SpUtil.getInstance().getString("imageParentPath")
            if ("" == imageParentPath) {
                val path = select!![0].path.split(select!![0].name)[0]
                SpUtil.getInstance().putString("imageParentPath", path)
            }
            setView(select)
        }
    }
}