package com.hxd.gobus.chat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.hxd.gobus.R;
import com.hxd.gobus.chat.adapter.AppsAdapter;
import com.hxd.gobus.chat.model.AppBean;

import java.util.ArrayList;

public class SimpleAppsGridView extends RelativeLayout {

    protected View view;

    public SimpleAppsGridView(Context context) {
        this(context, null);
    }

    public SimpleAppsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_apps, this);
        init();
    }

    protected void init() {
        GridView gv_apps = (GridView) view.findViewById(R.id.gv_apps);
        ArrayList<AppBean> mAppBeanList = new ArrayList<>();
        mAppBeanList.add(new AppBean(R.mipmap.icon_photo, "图片"));
        mAppBeanList.add(new AppBean(R.mipmap.icon_camera, "拍摄"));
        mAppBeanList.add(new AppBean(R.mipmap.icon_file, "文件"));
        mAppBeanList.add(new AppBean(R.mipmap.icon_loaction, "位置"));
        mAppBeanList.add(new AppBean(R.mipmap.businesscard, "名片"));
//        mAppBeanList.add(new AppBean(R.mipmap.icon_audio, "视频"));
//        mAppBeanList.add(new AppBean(R.mipmap.icon_voice, "语音"));
        AppsAdapter adapter = new AppsAdapter(getContext(), mAppBeanList);
        gv_apps.setAdapter(adapter);
    }
}
