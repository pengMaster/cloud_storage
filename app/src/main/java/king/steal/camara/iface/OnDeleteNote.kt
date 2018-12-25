package king.steal.camara.iface

/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2018/12/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface OnDeleteNote {
    fun delete(id: String, content: String)
}