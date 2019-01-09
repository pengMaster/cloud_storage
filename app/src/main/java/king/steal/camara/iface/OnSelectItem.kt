package king.steal.camara.iface

import king.steal.camara.bean.CloudFileBean

/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2018/12/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface OnSelectItem {

    fun onSelect(bean: CloudFileBean)
}