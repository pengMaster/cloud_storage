package king.cloud.camara.iface

import android.view.View

/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2018/08/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface OnRecyclerViewItemClickListener<T> {

    abstract fun onItemClick(view: View, viewType: Int, data: T, position: Int)


}