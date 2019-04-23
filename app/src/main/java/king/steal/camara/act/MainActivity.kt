package king.steal.camara.act

import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
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
import king.steal.camara.base.AppManager
import king.steal.camara.base.BaseActivity
import king.steal.camara.base.BaseFragmentAdapter
import king.steal.camara.base.FragmentFactory
import king.steal.camara.bean.CloudFileParser
import king.steal.camara.iface.FragmentConstant
import king.steal.camara.iface.OnLongClick
import king.steal.camara.utils.LogUtils
import king.steal.camara.utils.SpUtil
import king.steal.camara.utils.ToastUtils
import king.steal.marrykotlin.iface.OnRequestListener
import kotlinx.android.synthetic.main.act_main.*
import okhttp3.Call
import java.lang.Exception


class MainActivity : BaseActivity() {


    private var mExitTime: Long? = 0L


    override fun layoutId(): Int {
        return R.layout.act_main
    }

    override fun initData() {

        val fragList = listOf<Fragment>(
                FragmentFactory.getInstanceByIndex(FragmentConstant.frag_file_dir),
                FragmentFactory.getInstanceByIndex(FragmentConstant.frag_note),
                FragmentFactory.getInstanceByIndex(FragmentConstant.frag_my)

        )
        val fragAdapter = BaseFragmentAdapter(supportFragmentManager, fragList)
        mViewPager.adapter = fragAdapter

        initEvent()

    }

    private fun initEvent() {

        mLlFolder.setOnClickListener {
            mViewPager.currentItem = 0
        }
        mLlNote.setOnClickListener {
            mViewPager.currentItem = 1
        }
        mLlMy.setOnClickListener {
            mViewPager.currentItem = 2
        }
        mViewPager.setOnPageChangeListener(mPageListener)
    }

    override fun initView() {
    }

    override fun start() {
    }

    /**
     * viewPagerOnPageChangeListener
     */
    private val mPageListener =  object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(p0: Int) {
        }

        override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
        }

        override fun onPageSelected(p0: Int) {

            mIvFolder.setImageResource(R.drawable.ic_folder_gray_24dp)
            mTvFolder.setTextColor(resources.getColor(R.color.white_96))
            mTvNote.setTextColor(resources.getColor(R.color.white_96))
            mIvNote.setImageResource(R.drawable.ic_insert_comment_gray_24dp)
            mIvMy.setImageResource(R.drawable.ic_person_gray_24dp)
            mTvMy.setTextColor(resources.getColor(R.color.white_96))

            when (p0) {
                0 -> {
                    mIvFolder.setImageResource(R.drawable.ic_folder_green_24dp)
                    mTvFolder.setTextColor(resources.getColor(R.color.green))
                }
                1 -> {
                    mIvNote.setImageResource(R.drawable.ic_insert_comment_green_24dp)
                    mTvNote.setTextColor(resources.getColor(R.color.green))
                }
                2 -> {
                    mIvMy.setImageResource(R.drawable.ic_person_green_24dp)
                    mTvMy.setTextColor(resources.getColor(R.color.green))
                }
            }
        }

    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - this!!.mExitTime!! > 2000) {
                Toast.makeText(applicationContext, "再按一次退出应用", Toast.LENGTH_SHORT).show()
                mExitTime = System.currentTimeMillis()

            } else {
                AppManager.getAppManager().finishAllActivity()
                System.exit(0)
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


}
