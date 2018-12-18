package king.steal.camara.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class ListViewToScrollView extends ListView {

	public ListViewToScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ListViewToScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ListViewToScrollView(Context context, AttributeSet attrs,
                                int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
		            return true; // 禁止滑动
		        }
		        return super.dispatchTouchEvent(ev);

		}

}
