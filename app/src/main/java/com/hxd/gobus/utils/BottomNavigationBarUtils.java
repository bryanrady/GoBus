package com.hxd.gobus.utils;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.hxd.gobus.R;

import java.lang.reflect.Field;

/**
 * @author: wangqingbin
 * @date: 2019/7/30 15:27
 */

public class BottomNavigationBarUtils {

    /**
     * @param bottomNavigationBar 需要修改的 BottomNavigationBar
     * @param imgLen 图片大小
     * @param textSize 文字大小
     *      使用方法：直接调用setBottomNavigationItem(bottomNavigationBar, 6, 26, 10);
     *      代表将bottomNavigationBar的文字大小设置为10dp，图片大小为26dp，二者间间距为6dp
     */
    public static void setBottomNavigationItem(Context context, BottomNavigationBar bottomNavigationBar, float imgLen, float textSize){
        Class barClass = bottomNavigationBar.getClass();
        Field[] fields = barClass.getDeclaredFields();
        for(int i = 0; i < fields.length; i++){
            Field field = fields[i];
            field.setAccessible(true);
            if(field.getName().equals("mTabContainer")){
                try{
                    //反射得到 mTabContainer
                    LinearLayout mTabContainer = (LinearLayout) field.get(bottomNavigationBar);
                    for(int j = 0; j < mTabContainer.getChildCount(); j++){
                        //获取到容器内的各个Tab
                        FrameLayout container = (FrameLayout) mTabContainer.getChildAt(j);

                        //获取到Tab内的各个显示控件
                        FrameLayout frameLayout = container.findViewById(R.id.fixed_bottom_navigation_container);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                        frameLayout.setLayoutParams(params);
                        frameLayout.setPadding(0, 0, 0, 0);

                        //获取到Tab内的图像控件
                        ImageView iconView = frameLayout.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
                        iconView.setMaxWidth(ConvertUtils.px2dp(context,imgLen));
                        iconView.setMaxHeight(ConvertUtils.px2dp(context,imgLen));

                        //获取到Tab内的文字控件
                        TextView labelView = frameLayout.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title);
                        labelView.setTextSize(ConvertUtils.px2sp(context,textSize));
                        labelView.setIncludeFontPadding(true);
                    }
                } catch (IllegalAccessException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
