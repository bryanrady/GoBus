package com.hxd.gobus.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.hxd.gobus.R;

/**
 * @author: wangqingbin
 * @date: 2019/9/12 11:04
 */

public class SideSlipMenuItemView extends HorizontalScrollView {

    /**
     * 滑动后出现的滑动布局
     */
    private LinearLayout mSlideLayout;

    /**
     * 水平滚动条可以滑动的范围，即右侧按钮的宽度
     */
    private int mScrollWidth;

    /**
     * 判断是否显示右边的滑动布局
     */
    private boolean mIsOpenSlideLayout;

    /**
     * 在onMeasure中只执行一次的判断
     */
    private boolean mOnce = false;

    private OnSlidingLayoutListener mOnSlidingLayoutListener;

    public SideSlipMenuItemView(Context context) {
        this(context, null);
    }

    public SideSlipMenuItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideSlipMenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (!mOnce) {
//            mSlideLayout = findViewById(R.id.layout_right);
//        }
//        mOnce = true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed) {
            this.scrollTo(0, 0);
            mSlideLayout = findViewById(R.id.layout_right);
            mScrollWidth = mSlideLayout.getWidth();
        }
    }

    /**
     * 滚动发生改变   滚动多少 右边布局就显示多少
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mSlideLayout.setTranslationX(l - mScrollWidth);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if(mOnSlidingLayoutListener != null){
                    mOnSlidingLayoutListener.onDownOrMove(this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(mOnSlidingLayoutListener != null){
                    mOnSlidingLayoutListener.onDownOrMove(this);
                }
                break;
            case MotionEvent.ACTION_UP:
                changeScrollX();
                return true;
            case MotionEvent.ACTION_CANCEL:
                changeScrollX();
                return true;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 按滚动条被拖动距离判断 关闭或打开右边布局
     *      判断条件：被拖动的距离有没有隐藏或显示控件的一半以上
     */
    private void changeScrollX(){
        //如果拖动距离超过了控件的一半
        if(getScrollX() >= mScrollWidth/2){
            //推动了一半以上就打开
            this.smoothScrollTo(mScrollWidth, 0);
            mIsOpenSlideLayout = true;
            if(mOnSlidingLayoutListener != null){
                mOnSlidingLayoutListener.onMenuIsOpen(this);
            }
        }else{
            //没有一半以上就关上
            this.smoothScrollTo(0, 0);
            mIsOpenSlideLayout = false;
        }
    }

    /**
     * 判断滑动菜单是否是打开的
     * @return
     */
    public boolean slideLayoutIsOpen(){
        return mIsOpenSlideLayout;
    }

    /**
     * 关闭右边滑动菜单
     */
    public void closeSlideLayout(){
        if (!mIsOpenSlideLayout){
            return;
        }
        this.smoothScrollTo(0, 0);
        mIsOpenSlideLayout = false;
    }

    public void setOnSlidingLayoutListener(OnSlidingLayoutListener listener){
        mOnSlidingLayoutListener = listener;
    }

    /**
     * 滑动菜单布局的监听
     */
    public interface OnSlidingLayoutListener{
        void onMenuIsOpen(SideSlipMenuItemView recyclerItemView);//判断菜单是否打开
        void onDownOrMove(SideSlipMenuItemView recyclerItemView);
    }

}