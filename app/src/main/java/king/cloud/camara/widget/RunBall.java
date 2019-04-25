package king.cloud.camara.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

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
public class RunBall extends View {
    private ValueAnimator mAnimator;//时间流

    private List<Ball> mBalls;//小球对象
    private Paint mPaint;//主画笔
    private Paint mHelpPaint;//辅助线画笔
    private Point mCoo;//坐标系

    private float defaultR = 80;//默认小球半径
    private int defaultColor = Color.BLUE;//默认小球颜色
    private float defaultVX = 10;//默认小球x方向速度
    private float defaultF = 0.95f;//碰撞损耗
    private float defaultVY = 0;//默认小球y方向速度
    private float defaultAY = 0.5f;//默认小球加速度

    private float mMaxX = 600;//X最大值
    private float mMinX = -200;//X最小值
    private float mMaxY = 300;//Y最大值
    private float mMinY = -100;//Y最小值

    public RunBall(Context context) {
        this(context, null);
    }

    public RunBall(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mCoo = new Point(500, 500);
        //初始画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBalls = new ArrayList<>();
        initBalls();

        mHelpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHelpPaint.setColor(Color.BLACK);
        mHelpPaint.setStyle(Paint.Style.FILL);
        mHelpPaint.setStrokeWidth(3);

        //初始化时间流ValueAnimator
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setRepeatCount(-1);
        mAnimator.setDuration(2000);
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                updateBall();//更新小球位置
                invalidate();
            }
        });
        mAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(mCoo.x, mCoo.y);
        drawBalls(canvas, mBalls);
        canvas.restore();
    }

    /**
     * 绘制小球集合
     *
     * @param canvas
     * @param balls  小球集合
     */
    private void drawBalls(Canvas canvas, List<Ball> balls) {
        for (Ball ball : balls) {
            mPaint.setColor(ball.color);
            canvas.drawCircle(ball.x, ball.y, ball.r, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                mAnimator.start();
                break;
            case MotionEvent.ACTION_UP:
//                mAnimator.pause();
                break;
        }
        return true;
    }

    //准备两个球
    private void initBalls() {
        for (int i = 0; i < 2; i++) {
            Ball mBall = new Ball();
            mBall.color = Color.parseColor("#E68276");
            mBall.r = 80;
            mBall.vX = (float) (Math.pow(-1, Math.ceil(Math.random() * 1000)) * 20 * Math.random());
            mBall.vY = rangeInt(-15, 35);
            mBall.aY = 0.98f;
            mBalls.add(mBall);
        }
        mBalls.get(1).x = 300;
        mBalls.get(1).y = 300;
        mBalls.get(1).color = Color.parseColor("#2DAC7C");
    }

    /**
     * 两点间距离函数
     */
    public static float disPos2d(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    /**
     * 更新小球
     */
    private void updateBall() {
        Ball redBall = mBalls.get(0);
        Ball blueBall = mBalls.get(1);
        //校验两个小球的距离
        if (disPos2d(redBall.x, redBall.y, blueBall.x, blueBall.y) < 80 * 2) {
            redBall.vX = -redBall.vX;
            redBall.vY = -redBall.vY;
            blueBall.vX = -blueBall.vX;
            blueBall.vY = -blueBall.vY;
        }
        for (int i = 0; i < mBalls.size(); i++) {
            Ball ball = mBalls.get(i);
            ball.x += ball.vX;
            ball.y += ball.vY;
            ball.vY += ball.aY;
            ball.vX += ball.aX;
            if (ball.x > mMaxX) {
                ball.x = mMaxX;
                ball.vX = -ball.vX * defaultF;
            }
            if (ball.x < mMinX) {
                ball.x = mMinX;
                ball.vX = -ball.vX * defaultF;
            }
            if (ball.y > mMaxY) {
                ball.y = mMaxY;
                ball.vY = -ball.vY * defaultF;
            }
            if (ball.y < mMinY) {
                ball.y = mMinY;
                ball.vY = -ball.vY * defaultF;
            }
        }
    }

    /**
     * 获取范围随机整数：如 rangeInt(1,9)
     *
     * @param s 前数(包括)
     * @param e 后数(包括)
     * @return 范围随机整数
     */
    public static int rangeInt(int s, int e) {
        int max = Math.max(s, e);
        int min = Math.min(s, e) - 1;
        return (int) (min + Math.ceil(Math.random() * (max - min)));
    }
}
