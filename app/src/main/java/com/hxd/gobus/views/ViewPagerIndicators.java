package com.hxd.gobus.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.utils.ScreenUtils;

import java.util.List;

/**
 * Created by pengyu520 on 2016/7/21.
 */
public class ViewPagerIndicators extends LinearLayout{
    private static final int COLOR_TEXT_NORMAL=0XFFBEBEBE;
    private static final int COLOR_TEXT_HIGHLIGHT=0Xff33b5e5;
    private Paint mPaint;//画笔
    private Path mPath;//画线
    private int mTriangleWidth;//三角形的宽度
    private int mTriangleHeight;//三角形的高度
    private static final float RADIO_TRIANGLE_WIDTH=1/6F;//宽度比例
    private int screenWidth= ScreenUtils.getScreenWidth(getContext());//屏幕的宽度
    /**
     * 三角形底边的最大宽度
     */
    private final int DIMENSION_TRIANGLE_WIDTH_MAX=(int)(screenWidth/3*RADIO_TRIANGLE_WIDTH);
    private int mInitTranslationX;//初始化的偏移位置
    private int mTranslationX;//移动的偏移位置
    private int mTabVisibleCount;
    private static final int COUNT_DEFAULT_TAB=4;
    private List<String> mTitles;

    private ViewPager mViewPager;
    private PageOnchangeListener mListener;

    public ViewPagerIndicators(Context context) {
        this(context,null);

    }

    public ViewPagerIndicators(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取可见tab的数量
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mTabVisibleCount=array.getInt(R.styleable.ViewPagerIndicator_visible_tab_count,COUNT_DEFAULT_TAB);
        if (mTabVisibleCount<0){
            mTabVisibleCount=COUNT_DEFAULT_TAB;
        }
        array.recycle();

        //初始化画笔
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
//        mPaint.setColor(Color.parseColor("#ffffffff"));
        mPaint.setColor(Color.parseColor("#ff33b5e5"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth=(int)(w/mTabVisibleCount*RADIO_TRIANGLE_WIDTH);
        mTriangleWidth=Math.min(mTriangleWidth,DIMENSION_TRIANGLE_WIDTH_MAX);
        mInitTranslationX=w/mTabVisibleCount/2-mTriangleWidth/2;
        initTriangle();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mInitTranslationX+mTranslationX,getHeight()+2);
        canvas.drawPath(mPath,mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int cCount=getChildCount();
        if (cCount==0)return;
        for (int i=0;i<cCount;i++){
            View view=getChildAt(i);
            LayoutParams lp= (LayoutParams) view.getLayoutParams();
            lp.weight=0;
            lp.width= screenWidth/mTabVisibleCount;
            view.setLayoutParams(lp);
        }
        setItemClickEvent();
    }

    /**
     * 初始化三角形
     */
    private void initTriangle() {
        mTriangleHeight=mTriangleWidth/2;
        mPath=new Path();
        mPath.moveTo(0,0);
        mPath.lineTo(mTriangleWidth,0);
        mPath.lineTo(mTriangleWidth/2,-mTriangleHeight);
        mPath.close();
    }

    /**
     * 指示器跟随手指进行滚动
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset) {
        int tabWidth=getWidth()/mTabVisibleCount;
        mTranslationX=(int)(tabWidth*(offset+position));
        //容器移动,当tab处于移动至最后一个时
        if (position>=(mTabVisibleCount-1)&&offset>0&&getChildCount()>mTabVisibleCount){
            if (mTabVisibleCount!=0){
                int x=(position-(mTabVisibleCount-1))*tabWidth+(int)(tabWidth*offset);
                this.scrollTo(x,0);
            }else {
                int x=position*tabWidth+(int)(tabWidth*offset);
                this.scrollTo(x,0);
            }
        }
        invalidate();
    }

    /**
     * 设置可见的Tab数量
     * @param count
     */
    public void setVisibleTabCount(int count){
        mTabVisibleCount=count;
    }
    public void setTabItemTitle(List<String> titles){
        if (titles!=null&&titles.size()>0){
            this.removeAllViews();
            mTitles=titles;
            for (String title:mTitles){
                addView(getTextView(title));
            }
            setItemClickEvent();
        }
    }


    /**
     * 根据title创建Tab
     * @param title
     * @return
     */
    private View getTextView(String title) {
        TextView tv=new TextView(getContext());
        LayoutParams lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        lp.width=screenWidth/mTabVisibleCount;
        tv.setText(title);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        tv.setTextColor(COLOR_TEXT_NORMAL);
        tv.setLayoutParams(lp);
        return tv;
    }

    /**
     * 高亮某个tab的文本
     * @param pos
     */
    private void highLihgtTextView(int pos){
        resetTextViewColor();
        View view=getChildAt(pos);
        if (view instanceof TextView){
            ((TextView)view).setTextColor(COLOR_TEXT_HIGHLIGHT);
        }
    }
    private void resetTextViewColor(){
        for (int i=0;i<getChildCount();i++){
            View view=getChildAt(i);
            if (view instanceof TextView){
                ((TextView)view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }

    /**
     * 设置tab的点击事件
     */
    private void setItemClickEvent(){
        int cCount=getChildCount();
        for (int i=0;i<cCount;i++){
            View view = getChildAt(i);
            final int j=i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }


    /**
     * 设置关联的ViewPager
     * @param viewPager
     * @param pos
     */
    public void setViewPager(ViewPager viewPager,int pos){
        mViewPager=viewPager;
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position,positionOffset);
                if (mListener!=null){
                    mListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (mListener!=null){
                    mListener.onPageSelected(position);
                }
                highLihgtTextView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mListener!=null){
                    mListener.onPageScrollStateChanged(state);
                }
            }
        });
        mViewPager.setCurrentItem(pos);
        highLihgtTextView(pos);
    }


    public void setOnPageChangeListener(PageOnchangeListener listener){
        this.mListener=listener;
    }
    public interface PageOnchangeListener{
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        void onPageSelected(int position);
        void onPageScrollStateChanged(int state);
    }
}
