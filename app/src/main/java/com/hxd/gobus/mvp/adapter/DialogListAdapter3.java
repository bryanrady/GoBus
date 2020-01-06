package com.hxd.gobus.mvp.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.bean.StringInfo;

import java.util.HashMap;
import java.util.List;

/**
 *  对话框中的列表适配器  单选
 * Created by Administrator on 2018/3/19.
 */

public class DialogListAdapter3 extends ArrayAdapter<StringInfo>{

    private LayoutInflater inflater;
    private Context mContext;
    private List<StringInfo> list;
    private HodlerView mHodlerView;

    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;

    public DialogListAdapter3(Context context, List<StringInfo> list){
        super(context, R.layout.dialog_listview_item3, list);
        this.mContext = context;
        this.list = list;
        this.inflater = LayoutInflater.from(mContext);

        isSelected = new HashMap<Integer, Boolean>();
        initData();

    }

    private void initData() {
        for (int i = 0; i < list.size() + 1; i++) {
            getIsSelected().put(i, false);
        }
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        isSelected = isSelected;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mHodlerView = new HodlerView();
            convertView = this.inflater.inflate(R.layout.dialog_listview_item3, null);
            mHodlerView.dialog_checkBox = (CheckBox) convertView.findViewById(R.id.dialog_checkBox_item);
            mHodlerView.tv_dialog_title = (TextView) convertView.findViewById(R.id.tv_dialog_listview_item);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (HodlerView) convertView.getTag();
        }
        // 根据isSelected来设置checkbox的选中状况
        mHodlerView.dialog_checkBox.setChecked(getIsSelected().get(position));
        mHodlerView.tv_dialog_title.setText(list.get(position).getTitle());

        return convertView;
    }

    public HodlerView getHolderView(){
        return mHodlerView;
    }

    public class HodlerView {

        public CheckBox dialog_checkBox;

        public TextView tv_dialog_title;

    }
}
