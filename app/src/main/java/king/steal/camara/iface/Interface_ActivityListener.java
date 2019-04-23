package king.steal.camara.iface;

import android.content.Context;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2019/4/19 11:40 AM
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface Interface_ActivityListener {
    /**
     * 全屏积分墙Activity 调用onDestory的时候回调，执行在ui线程中
     */
    public void onActivityDestroy(Context context);
}
