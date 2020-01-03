package com.hxd.gobus.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 15:54
 */

public class ViewHolderUtils {

    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (null == viewHolder) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (null == childView) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

}
