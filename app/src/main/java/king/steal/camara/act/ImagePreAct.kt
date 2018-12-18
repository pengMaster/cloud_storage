package king.steal.camara.act

import android.view.Gravity
import android.widget.TextView
import com.bumptech.glide.Glide
import king.steal.camara.R
import king.steal.camara.base.BaseActivity
import king.steal.camara.utils.LogUtils
import king.steal.camara.utils.StatusBarCompat
import king.steal.camara.utils.UIUtils
import king.steal.camara.widget.CustomPopWindow
import kotlinx.android.synthetic.main.act_image_pre.*

/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2018/12/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class ImagePreAct : BaseActivity() {

    override fun layoutId(): Int {
        return R.layout.act_image_pre
    }

    override fun initData() {
        val path = intent.getStringExtra("path")
        val name = intent.getStringExtra("name")
        Glide.with(applicationContext)
                .load(path)
                .into(mImagePre)
        mTvTitle.text = name
    }

    override fun initView() {
        StatusBarCompat.compat(this, resources.getColor(R.color.transparent_23))
        mImageBack.setOnClickListener {
            finish()
        }
        mImageRestore.setOnClickListener {
            showVerifyPop()
        }
    }

    override fun start() {
        LogUtils.e("")
    }


    private fun showVerifyPop() {
        val dialog = UIUtils.inflate(R.layout.ppw_customer)
        val ppw = CustomPopWindow.PopupWindowBuilder(this@ImagePreAct)
                .setView(dialog)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .create()
                .showAtLocation(mImagePre, Gravity.CENTER, 0, 0)

        dialog.apply {
            findViewById<TextView>(R.id.tv_sure).setOnClickListener {

            }
            findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
                ppw.dissmiss()
            }
        }
    }
}