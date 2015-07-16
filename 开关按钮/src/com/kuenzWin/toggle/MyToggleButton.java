package com.kuenzWin.toggle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

/*
 * view ������ʾ����Ļ�ϣ��м�����Ҫ���裺
 * 1�����췽�� ���� ����
 * 2������view�Ĵ�С��	onMeasure(int,int);
 * 3��ȷ��view��λ�� ��view������һЩ����Ȩ������Ȩ�� ��view���С�  onLayout();
 * 4������ view ������ �� onDraw(Canvas)
 */

public class MyToggleButton extends View implements OnClickListener {

	/**
	 * ��Ϊ������ͼƬ
	 */
	private Bitmap backgroundBitmap;
	/**
	 * ���Ի�����ͼƬ
	 */
	private Bitmap slideBtn;
	/**
	 * ������ť����߽�
	 */
	private Paint paint;

	/**
	 * ������ť����߽�
	 */
	private float slideBtn_left;

	private boolean isChecked = true;

	public boolean isChecked() {
		return isChecked;
	}

	public OnClickCallBack onClickCallBack;

	public void setOnClickCallBack(OnClickCallBack onClickCallBack) {
		this.onClickCallBack = onClickCallBack;
	}

	/**
	 * �ڴ������洴�������ʱ��ʹ�ô˹��췽��
	 */
	public MyToggleButton(Context context) {
		super(context);
	}

	/**
	 * �ڲ����ļ���������view������ʱ��ϵͳ�Զ����á�
	 * 
	 * @param context
	 *            �����Ķ���
	 * @param attrs
	 *            ���Լ�
	 */
	public MyToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initView();
	}

	/**
	 * ��ʼ��ͼƬ
	 */
	private void initView() {
		backgroundBitmap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.switch_background);
		slideBtn = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.slide_button);
		this.initSlidingLeft();
		paint = new Paint();
		// �򿪿����
		paint.setAntiAlias(true);

		this.setOnClickListener(this);
	}

	/**
	 * ���ÿɻ�����ť����߾�
	 */
	private void initSlidingLeft() {
		if (isChecked)
			slideBtn_left = backgroundBitmap.getWidth() - slideBtn.getWidth();
		else
			slideBtn_left = 0;
	}

	/**
	 * �����ߴ�ʱ�Ļص�����
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		/**
		 * ���õ�ǰview�Ĵ�С width :view�Ŀ�� height :view�ĸ߶� ����λ�����أ�
		 */
		setMeasuredDimension(backgroundBitmap.getWidth(),
				backgroundBitmap.getHeight());

	}

	// ȷ��λ�õ�ʱ����ô˷���
	// �Զ���view��ʱ�����ò���
	// @Override
	// protected void onLayout(boolean changed, int left, int top, int right,
	// int bottom) {
	// super.onLayout(changed, left, top, right, bottom);
	// }

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// ���� ����
		/*
		 * backgroundBitmap Ҫ���Ƶ�ͼƬ left ͼƬ����߽� top ͼƬ���ϱ߽� paint ����ͼƬҪʹ�õĻ���
		 */
		canvas.drawBitmap(backgroundBitmap, 0, 0, paint);

		// ���� �ɻ����İ�ť
		canvas.drawBitmap(slideBtn, slideBtn_left, 0, paint);
	}

	@Override
	public void onClick(View v) {
		this.flushState();
		onClickCallBack.onClick();
	}

	public interface OnClickCallBack {
		public void onClick();
	}

	/**
	 * �ж��Ƿ����϶��� ����϶��ˣ��Ͳ�����Ӧ onclick �¼�
	 * 
	 */
	private boolean isDrag = false;

	/**
	 * down �¼�ʱ��xֵ
	 */
	private int firstX;
	/**
	 * touch �¼�����һ��xֵ
	 */
	private int lastX;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			firstX = lastX = (int) event.getX();
			isDrag = false;

			break;
		case MotionEvent.ACTION_MOVE:

			// �ж��Ƿ����϶�
			if (Math.abs(event.getX() - firstX) > 5) {
				isDrag = true;
			}

			// ���� ��ָ����Ļ���ƶ��ľ���
			int dis = (int) (event.getX() - lastX);

			// �����ε�λ�� ���ø�lastX
			lastX = (int) event.getX();

			// ������ָ�ƶ��ľ��룬�ı�slideBtn_left ��ֵ
			slideBtn_left = slideBtn_left + dis;
			break;
		case MotionEvent.ACTION_UP:

			// �ڷ����϶�������£���������λ�ã��жϵ�ǰ���ص�״̬
			if (isDrag) {

				int maxLeft = backgroundBitmap.getWidth() - slideBtn.getWidth(); // slideBtn
																					// ��߽����ֵ
				/*
				 * ���� slideBtn_left �жϣ���ǰӦ��ʲô״̬
				 */
				if (slideBtn_left > maxLeft / 2) { // ��ʱӦΪ �򿪵�״̬
					isChecked = true;
				} else {
					isChecked = false;
				}

				flushState();
			}
			break;
		}

		flushView();

		return true;
	}

	/**
	 * ˢ�µ�ǰ״̬
	 */
	private void flushState() {
		isChecked = !isChecked;
		if (isChecked) {
			slideBtn_left = backgroundBitmap.getWidth() - slideBtn.getWidth();
		} else {
			slideBtn_left = 0;
		}

		flushView();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (isChecked) {
				slideBtn_left -= 10;
			} else {
				slideBtn_left -= 10;
			}
		}
	};

	/**
	 * ˢ�µ�ǰ����
	 */
	private void flushView() {
		/*
		 * �� slideBtn_left ��ֵ�����ж� ��ȷ�����ں����λ�� �� 0<=slideBtn_left <= maxLeft
		 */

		int maxLeft = backgroundBitmap.getWidth() - slideBtn.getWidth(); // slideBtn
																			// ��߽����ֵ

		// ȷ�� slideBtn_left >= 0
		slideBtn_left = (slideBtn_left > 0) ? slideBtn_left : 0;

		// ȷ�� slideBtn_left <=maxLeft
		slideBtn_left = (slideBtn_left < maxLeft) ? slideBtn_left : maxLeft;

		/*
		 * ˢ�µ�ǰ��ͼ ���� ִ��onDrawִ��
		 */
		invalidate();
	}

}
