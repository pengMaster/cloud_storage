package king.cloud.camara.widget;

import android.animation.TypeEvaluator;

import king.cloud.camara.bean.Ball;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2018/11/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SinEvaluator implements TypeEvaluator {

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {

        //初始点
        Ball startPos = (Ball) startValue;
        //结束点
        Ball endPos = (Ball) endValue;
        //计算每次更新时的x坐标

        Ball clone = startPos.clone();
        clone.x = startPos.x + fraction * (endPos.x - startPos.x);
        //将y坐标进行联动
        clone.y = (float) (Math.sin(clone.x * Math.PI / 180) * 100);
        //返回更新后的点
        return clone;
    }
}