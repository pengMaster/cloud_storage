package king.steal.camara

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.https.HttpsUtils
import king.steal.camara.act.MainActivity
import king.steal.camara.utils.SpUtil
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates


/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2018/09/19
 *     desc   : Application
 *     version: 1.0
 * </pre>
 */
class MyApplication : Application(){

    companion object {

        private val TAG = "MyApplication"

        var context: Context by Delegates.notNull()
            private set


    }

    override fun onCreate() {

        super.onCreate()
        context = applicationContext

        SpUtil.getInstance().init(context)

        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)

        val sslParams = HttpsUtils.getSslSocketFactory(null, null, null)
        val okHttpClient: OkHttpClient
        okHttpClient = OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .build()
        OkHttpUtils.initClient(okHttpClient)

        initBugly()
    }

    private fun initBugly() {
        Beta.autoInit = true
        Beta.showInterruptedStrategy = true
        Beta.initDelay = (5 * 1000).toLong()
        val appId = "ac1400d910"
        Beta.autoCheckUpgrade = true
        Beta.canShowUpgradeActs.add(MainActivity::class.java)
        Beta.upgradeCheckPeriod = (1 * 1000).toLong()
        Bugly.init(applicationContext, appId, false)

    }
    private val mActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Log.d(TAG, "onCreated: " + activity.componentName.className)
        }

        override fun onActivityStarted(activity: Activity) {
            Log.d(TAG, "onStart: " + activity.componentName.className)
        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            Log.d(TAG, "onDestroy: " + activity.componentName.className)
        }
    }


}
