package com.hxd.gobus.views.indicator;

import android.os.Handler;
import android.os.Message;
/**
 * @author 作者:pengyu 
 * @version 版本号:1.0 
 * @date 创建时间:2015年5月4日 上午11:15:29 
 * @description 类描述:自动轮播图片管理器
 */
public class AutoPlayManager {
	/**
	 * 自动播放标志位，默认播放
	 */
	private boolean broadcastEnable = false;
	/**
	 * 自动播放启动默认时间
	 */
	private static final long DEFAULT_STARTMILS = 2 * 1000;
	/**
	 * 自动播放间隔默认时间
	 */
	private static final long DEFAULT_INTEVALMILS = 3 * 1000;
	/**
	 * 启动时间ms
	 */
	private long startMils = DEFAULT_STARTMILS;
	/**
	 * 间隔ms
	 */
	private long intevalMils = DEFAULT_INTEVALMILS;
	/**
	 * 向右
	 */
	private final static int RIGHT = 0;
	/**
	 * 向左
	 */
	private final static int LEFT = 1;

	/**
	 * 当前方向
	 */
	private int direction = RIGHT;

	/**
	 * 自动播放默认次数（无限）
	 */
	private static final int DEFAULT_TIMES = -1;

	/**
	 * 自动播放次数
	 */
	private int broadcastTimes = DEFAULT_TIMES;

	/**
	 * 自动播放次数记数
	 */
	private int timesCount = 0;

	/**
	 * 循环播放
	 */
	private Handler broadcastHandler = null;

	/**
	 * target ImageIndicatorView
	 */
	private IndicatorView mIndicatorView = null;

	public AutoPlayManager(IndicatorView indicatorView) {
		this.mIndicatorView = indicatorView;
		this.broadcastHandler = new BroadcastHandler(AutoPlayManager.this);
	}

	/**
	 * 设置自动播放启动时间和间隔
	 * 
	 * @param startMils
	 *            启动时间ms(>2, 默认为8s)
	 * @param intevelMils
	 *            间隔ms(默认为3s)
	 */
	public void setBroadcastTimeIntevel(long startMils, long intevelMils) {
		this.startMils = startMils;
		this.intevalMils = intevelMils;
	}

	/**
	 * 设置自动播放开关
	 * 
	 * @param flag
	 *            打开或关闭
	 */
	public void setBroadcastEnable(boolean flag) {
		this.broadcastEnable = flag;
	}

	/**
	 * 设置循环播放次数
	 * 
	 * @param times
	 *            循环播放次数
	 */
	public void setBroadCastTimes(int times) {
		this.broadcastTimes = times;
	}

	/**
	 * 启动循环播放
	 */
	public void loop() {
		if (broadcastEnable) {
			broadcastHandler.sendEmptyMessageDelayed(0, this.startMils);
		}
	}

	protected void handleMessage(Message msg) {
		if (broadcastEnable) {
			if (System.currentTimeMillis()
					- mIndicatorView.getRefreshTime() < 2 * 1000) {// 最近一次划动间隔小于2s
				return;
			}
			if ((broadcastTimes != DEFAULT_TIMES)
					&& (timesCount > broadcastTimes)) {// 循环次数用完
				return;
			}

			if (direction == RIGHT) {// roll right
				if (mIndicatorView.getCurrentIndex() < mIndicatorView
						.getTotalCount()) {
					if (mIndicatorView.getCurrentIndex() == mIndicatorView
							.getTotalCount() - 1) {
						timesCount++;// 循环次数次数加1
						direction = LEFT;
					} else {
						mIndicatorView
								.getViewPager()
								.setCurrentItem(
										mIndicatorView.getCurrentIndex() + 1,
										true);
					}
				}
			} else {// roll left
				if (mIndicatorView.getCurrentIndex() >= 0) {
					if (mIndicatorView.getCurrentIndex() == 0) {
						direction = RIGHT;
					} else {
						mIndicatorView
								.getViewPager()
								.setCurrentItem(
										mIndicatorView.getCurrentIndex() - 1,
										true);
					}
				}
			}

			broadcastHandler.sendEmptyMessageDelayed(1, this.intevalMils);
		}
	}

	static class BroadcastHandler extends Handler {
		private AutoPlayManager autoBrocastManager;

		public BroadcastHandler(AutoPlayManager autoBrocastManager) {
			this.autoBrocastManager = autoBrocastManager;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (this.autoBrocastManager != null) {
				autoBrocastManager.handleMessage(msg);
			}
		}
	}
}
