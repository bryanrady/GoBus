package com.hxd.gobus.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hxd.gobus.R;

/**
 * @author: wangqingbin
 * @date: 2019/7/22 10:20
 */

public class ConvertUtils {

    /**
     * dp值转换成px值
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, final float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px值转换成dp值
     *
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(Context context, final float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp值转换成px值
     *
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px(Context context, final float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px值转换成sp值
     *
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp(Context context, final float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

//    /**
//     * 动态设置dp与sp
//     */
//    public void dynamicSet(View view) {
//
//        /**
//         * 注意：
//         * getDimension()方法并不是直接拿到dimens.xml文件中的dp或sp值
//         * 而是將dimens.xml文件中的dp或sp值乘以屏幕密度（density）来换算成px值
//         * 所以拿到该值后还需要换算成对应的dp或sp
//         */
//
//        /*获取sp值*/
//        float pxValue = getResources().getDimension(R.dimen.sp_20);//获取对应资源文件下的sp值
//        int spValue = ConvertUtils.px2sp(this, pxValue);//将px值转换成sp值
//
//        /*获取dp值*/
//        float pxValue2 = getResources().getDimension(R.dimen.dp_180);//获取对应资源文件下的dp值
//        int dpValue = ConvertUtils.px2dp(this, pxValue2);//将px值转换成dp值
//
//        Log.d(TAG, "pxValue= " + pxValue);
//        Log.d(TAG, "spValue= " + spValue);
//        Log.d(TAG, "pxValue2= " + pxValue2);
//        Log.d(TAG, "dpValue= " + dpValue);
//
//        /*动态设置文字大小与控件大小*/
//        mTvShowParams.setTextSize(spValue);
//        mBtnTestModule.setLayoutParams(new LinearLayout.LayoutParams((int) pxValue2, ViewGroup.LayoutParams.WRAP_CONTENT));
//    }


}
