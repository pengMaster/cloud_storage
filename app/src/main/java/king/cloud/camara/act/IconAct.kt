package king.cloud.camara.act

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.CountDownTimer
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import king.cloud.camara.R
import king.cloud.camara.base.BaseActivity
import king.cloud.camara.utils.LogUtils
import king.cloud.camara.utils.SpUtil
import king.cloud.camara.utils.UIUtils
import king.cloud.camara.widget.CustomPopWindow
import kotlinx.android.synthetic.main.act_icon.*
import org.jetbrains.anko.find

/**
 * <pre>
 * author : Wp
 * e-mail : 18141924293@163.com
 * time   : 2019/01/11
 * desc   : 图标切换功能
 * version: 1.0
</pre> *
 */
class IconAct : BaseActivity() {


    private var defaultComponent: ComponentName? = null
    private var calculatorComponent: ComponentName? = null
    private var calendarComponent: ComponentName? = null
    private var weatherComponent: ComponentName? = null
    private lateinit var timer: CountDownTime
    private lateinit var ppw: CustomPopWindow
    private lateinit var timeText: TextView


    override fun layoutId(): Int {
        return R.layout.act_icon
    }

    override fun initData() {
        //拿到我们注册的MainActivity组件
        defaultComponent = ComponentName(baseContext, "king.steal.camara.act.SplashAct")  //拿到默认的组件
        //计算器
        calculatorComponent = ComponentName(baseContext, "king.steal.camara.act.calculator")
        //日历
        calendarComponent = ComponentName(baseContext, "king.steal.camara.act.calendar")
        //天气
        weatherComponent = ComponentName(baseContext, "king.steal.camara.act.weather")
        initEvent()
        timer = CountDownTime(10000, 1000)
    }

    private fun initEvent() {
        mLlDefault.setOnClickListener {
            enableComponent(defaultComponent)
            disableComponent(calculatorComponent)
            disableComponent(calendarComponent)
            disableComponent(weatherComponent)

            mLlDefault.setBackgroundResource(R.color.green_hint)
            mLlCalculator.setBackgroundResource(R.color.white)
            mLlCalendar.setBackgroundResource(R.color.white)
            mLlWeather.setBackgroundResource(R.color.white)

            SpUtil.getInstance().putString(SpUtil.icon_style, "default")

            showPopWindow(R.mipmap.ic_space, "私密云盘")

        }
        //计算器
        mLlCalculator.setOnClickListener {
            disableComponent(defaultComponent)
            disableComponent(calendarComponent)
            disableComponent(weatherComponent)
            enableComponent(calculatorComponent)

            mLlCalculator.setBackgroundResource(R.color.green_hint)
            mLlDefault.setBackgroundResource(R.color.white)
            mLlCalendar.setBackgroundResource(R.color.white)
            mLlWeather.setBackgroundResource(R.color.white)

            SpUtil.getInstance().putString(SpUtil.icon_style, "calculator")

            showPopWindow(R.mipmap.ic_calculator, "计算器")

        }
        //日历
        mLlCalendar.setOnClickListener {
            disableComponent(defaultComponent)
            disableComponent(calculatorComponent)
            disableComponent(weatherComponent)
            enableComponent(calendarComponent)

            mLlCalendar.setBackgroundResource(R.color.green_hint)
            mLlDefault.setBackgroundResource(R.color.white)
            mLlCalculator.setBackgroundResource(R.color.white)
            mLlWeather.setBackgroundResource(R.color.white)

            SpUtil.getInstance().putString(SpUtil.icon_style, "calendar")

            showPopWindow(R.mipmap.ic_calendar, "日历")
        }
        //天气
        mLlWeather.setOnClickListener {
            enableComponent(weatherComponent)
            disableComponent(defaultComponent)
            disableComponent(calculatorComponent)
            disableComponent(calendarComponent)

            mLlWeather.setBackgroundResource(R.color.green_hint)
            mLlDefault.setBackgroundResource(R.color.white)
            mLlCalculator.setBackgroundResource(R.color.white)
            mLlCalendar.setBackgroundResource(R.color.white)

            SpUtil.getInstance().putString(SpUtil.icon_style, "weather")

            showPopWindow(R.mipmap.ic_weather, "天气")

        }
    }

    override fun initView() {
        //默认值
        val iconStyle = SpUtil.getInstance().getString(SpUtil.icon_style)
        when (iconStyle) {
            "default" -> {
                mLlDefault.setBackgroundResource(R.color.green_hint)
                mLlCalculator.setBackgroundResource(R.color.white)
                mLlCalendar.setBackgroundResource(R.color.white)
                mLlWeather.setBackgroundResource(R.color.white)
            }
            "calculator" -> {
                mLlCalculator.setBackgroundResource(R.color.green_hint)
                mLlDefault.setBackgroundResource(R.color.white)
                mLlCalendar.setBackgroundResource(R.color.white)
                mLlWeather.setBackgroundResource(R.color.white)
            }
            "calendar" -> {
                mLlCalendar.setBackgroundResource(R.color.green_hint)
                mLlDefault.setBackgroundResource(R.color.white)
                mLlCalculator.setBackgroundResource(R.color.white)
                mLlWeather.setBackgroundResource(R.color.white)
            }
            "weather" -> {
                mLlWeather.setBackgroundResource(R.color.green_hint)
                mLlDefault.setBackgroundResource(R.color.white)
                mLlCalculator.setBackgroundResource(R.color.white)
                mLlCalendar.setBackgroundResource(R.color.white)
            }
        }
        mLlHeader.apply {
            find<Button>(R.id.btnBack).setOnClickListener {
                finish()
            }
            find<TextView>(R.id.tv_topbar_title).text = "更换图标"
        }
    }

    override fun start() {
        LogUtils.e("")
    }

    private fun showPopWindow(picId: Int, name: String) {
        val dialog = UIUtils.inflate(R.layout.ppw_change_icon_success)
        ppw = CustomPopWindow.PopupWindowBuilder(this@IconAct)
                .setView(dialog)
                .setBgDarkAlpha(0.6f) // 控制亮度
                .enableOutsideTouchableDissmiss(false)
                .enableBackDissmiss(false)
                .create()
                .showAtLocation(mLlWeather, Gravity.CENTER, 0, 0)

        timer.start()

        dialog.apply {
            find<TextView>(R.id.mTvAppName).text = name
            find<ImageView>(R.id.mIvIcon).setImageResource(picId)
            timeText = find<TextView>(R.id.tv_pay_result)
        }
    }

    /**
     * 启用组件
     *
     * @param componentName
     */
    private fun enableComponent(componentName: ComponentName?) {
        val state = packageManager!!.getComponentEnabledSetting(componentName)
        if (state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            //已经启用
            return
        }
        packageManager!!.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)
    }

    /**
     * 禁用组件
     *
     * @param componentName
     */
    private fun disableComponent(componentName: ComponentName?) {
        val state = packageManager!!.getComponentEnabledSetting(componentName)
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            //已经禁用
            return
        }
        packageManager!!.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)
    }

    /**
     * 第一种方法 使用android封装好的 CountDownTimer
     * 创建一个类继承 CountDownTimer
     */
    internal inner class CountDownTime//构造函数  第一个参数代表总的计时时长  第二个参数代表计时间隔  单位都是毫秒
    (millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        @SuppressLint("SetTextI18n")
        override fun onTick(l: Long) { //每计时一次回调一次该方法
            timeText.text = "${l / 1000} s"
        }

        override fun onFinish() { //计时结束回调该方法
            ppw.dissmiss()
        }
    }
}
