package com.hxd.gobus.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.hxd.gobus.R;

/**
 *  Created by pengyu520 on 2016/6/7.
 *	@fn:自定义主页面底部使用的TabView
 */
public class TabView extends View {
	private static final String INSTANCE_STATUS = "instance_status";
	private static final String STATUS_ALPHA = "status_alpha";
	private int mColor = 0xFF8a8a8a;// 默认的颜色值
	private Bitmap mIconBitmap;// 需要绘制的Bitmap图标
	private String mText = "首页";// 默认的字体内容(无关要紧的)
	// 默认的字体大小
	private int mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());

	// 内存中进行绘图的工具
	private Canvas mCanvas;// 画布
	private Bitmap mBitmap;// 位图
	private Paint mPaint;// 画笔

	private float mAlpha;// 变色

	private Rect mIconRect;
	private Rect mTextBound;
	private Paint mTextPaint;

	public TabView(Context context) {
		this(context, null);// 一个参数的方法调用两个参数的方法
	}

	public TabView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);// 两个参数的方法调用三个参数的方法
	}

	/**
	 * 获取自定义属性的值
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// 通过TypedArray获取自定义属性的值
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabView);
		int n = a.getIndexCount();
		// 循环遍历获取所有自定义属性的值
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.TabView_tabview_icon:
				// 获取自定义的图标
				BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(attr);
				mIconBitmap = drawable.getBitmap();
				break;
			case R.styleable.TabView_tabview_color:
				// 获取自定义的颜色值
				mColor = a.getColor(attr, 0xFF8a8a8a);// 默认值
				break;
			case R.styleable.TabView_tabview_text:
				// 获取自定义的文本
				mText = a.getString(attr);
				break;
			case R.styleable.TabView_tabview_text_size:
				// 获取自定义的文本的大小
				mTextSize = (int) a.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
				break;
			}

		}
		a.recycle();// 回收资源

		mTextBound = new Rect();
		mTextPaint = new Paint();
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setColor(0Xff8a8a8a);// 设置图片的颜色(灰色)
		mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);// 绘制字的范围
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 图标可用的宽度
		int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
		// 图标可用的高度
		int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - mTextBound.height();
		// 图标的宽度(取宽度与高度的最小值),正方形图标的宽高是一样的
		int iconWidth = Math.min(width, height);
		// 横向居中的一个位置
		int left = getMeasuredWidth() / 2 - iconWidth / 2;
		// 纵向居中的一个位置
		int top = getMeasuredHeight() / 2 - (mTextBound.height() + iconWidth) / 2;
		// 绘制图标的范围
		mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 绘制图标
		canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
		// 可变的颜色取值
		int alpha = (int) Math.ceil(255 * mAlpha);
		// 内存去准备mBitmap , setAlpha , 纯色 ，xfermode ， 图标
		setupTargetBitmap(alpha);
		// 1、绘制原文本 ；
		drawSourceText(canvas, alpha);
		// 2、绘制变色的文本
		drawTargetText(canvas, alpha);
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}

	/**
	 * 在内存中绘制可变色的Icon图标
	 */
	private void setupTargetBitmap(int alpha) {
		mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mPaint = new Paint();
		mPaint.setColor(mColor);// 设置颜色
		mPaint.setAntiAlias(true);// 去锯齿
		mPaint.setDither(true);
		mPaint.setAlpha(alpha);
		mCanvas.drawRect(mIconRect, mPaint);// 在图标的范围内设置纯色
		// 设置xfermode的DST_IN
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		mPaint.setAlpha(255);
		// 绘制可变色的图标
		mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
	}

	/**
	 * 绘制原文本
	 * @param canvas
	 * @param alpha
	 */
	private void drawSourceText(Canvas canvas, int alpha) {
		mTextPaint.setColor(0Xff8a8a8a);// 设置默认的颜色(灰色)
		// mTextPaint.setFakeBoldText(true);//设置文本为粗体
		mTextPaint.setAlpha(255 - alpha);
		int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
		int y = mIconRect.bottom + mTextBound.height() + 6;
		canvas.drawText(mText, x, y, mTextPaint);
	}

	/**
	 * 绘制变色的文本
	 * @param canvas
	 * @param alpha
	 */
	private void drawTargetText(Canvas canvas, int alpha) {
		mTextPaint.setColor(mColor);
		mTextPaint.setAlpha(alpha);
		int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
		int y = mIconRect.bottom + mTextBound.height() + 6;
		canvas.drawText(mText, x, y, mTextPaint);
	}

	// 提供给外界设置图标颜色的方法
	public void setIconAlpha(float alpha) {
		this.mAlpha = alpha;
		invalidateView();// 重绘图标
	}

	/**
	 * 重绘
	 */
	private void invalidateView() {
		if (Looper.getMainLooper() == Looper.myLooper()) {
			invalidate();// UI线程
		} else {
			postInvalidate();// 非UI线程
		}
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
		bundle.putFloat(STATUS_ALPHA, mAlpha);
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			mAlpha = bundle.getFloat(STATUS_ALPHA);
			super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
			return;
		}
		super.onRestoreInstanceState(state);
	}
}
