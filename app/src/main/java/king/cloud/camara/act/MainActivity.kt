package king.cloud.camara.act

import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.widget.Toast
import king.cloud.camara.R
import king.cloud.camara.base.AppManager
import king.cloud.camara.base.BaseActivity
import king.cloud.camara.base.BaseFragmentAdapter
import king.cloud.camara.base.FragmentFactory
import king.cloud.camara.iface.FragmentConstant
import kotlinx.android.synthetic.main.act_main.*


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
