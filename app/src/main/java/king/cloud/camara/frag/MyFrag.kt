package king.cloud.camara.frag

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tencent.bugly.beta.Beta
import king.cloud.camara.R
import king.cloud.camara.act.CreatePwdActivity
import king.cloud.camara.act.IconAct
import king.cloud.camara.act.IssueUploadAct
import king.cloud.camara.base.BaseFragment
import king.cloud.camara.utils.SpUtil
import king.cloud.camara.utils.UIUtils
import kotlinx.android.synthetic.main.frag_my.*

/**
 * <pre>
 * author : Wp
 * e-mail : 18141924293@163.com
 * time   : 2018/12/20
 * desc   :
 * version: 1.0
</pre> *
 */
class MyFrag : BaseFragment() {

    override fun initView(inflater: LayoutInflater?): View {
        return UIUtils.inflate(R.layout.frag_my)
    }

    override fun initData(savedInstanceState: Bundle?) {
        val imei = SpUtil.getInstance().getString("imei")
        mTvUserName.text = imei

        initEvent()
    }

    private fun initEvent() {
        //版本更新
        mRlVersion.setOnClickListener {
            Beta.checkUpgrade()
        }
        //密码重置
        mRlPwd.setOnClickListener {
            val intent = Intent()
            intent.putExtra("isSplash", false)
            intent.setClass(activity, CreatePwdActivity::class.java)
            startActivity(intent)
        }

        mRlIssue.setOnClickListener {
            val intent = Intent()
            intent.setClass(activity, IssueUploadAct::class.java)
            startActivity(intent)
        }
        mRlChangeIcon.setOnClickListener {
            val intent = Intent()
            intent.setClass(activity, IconAct::class.java)
            startActivity(intent)
        }
    }

}
