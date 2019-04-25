package king.cloud.camara.bean;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2018/11/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Ball implements Cloneable {
    public float aX;//加速度
    public float aY;//加速度Y
    public float vX;//速度X
    public float vY;//速度Y
    public float x;//点位X
    public float y;//点位Y
    public int color;//颜色
    public float r;//半径

    public Ball clone() {
        Ball clone = null;
        try {
            clone = (Ball) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
