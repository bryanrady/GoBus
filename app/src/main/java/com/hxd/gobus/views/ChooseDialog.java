package com.hxd.gobus.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.mvp.adapter.DialogListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  自定义一个单选、多选对话框
 * Created by wangqingbin on 2018/3/16.
 */

public class ChooseDialog extends Dialog {

    private static final int SINGLE_SELECT = 0;
    private static final int MULTI_SELECT = 1;

    public ChooseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{
        private Context context;
        private LinearLayout llTitleLayout;
        private TextView tvTitle;
        private MaxListView listView;
        private Button positiveButton;
        private Button negativeButton;
        private List<StringInfo> list;  //数据集合
        private DialogListAdapter dialogListAdapter;
        private int choiceType;  //用来标识是单选还是多选  0表示单选 1表示多选
        private List<StringInfo> mSelectList = new ArrayList<>();    //被选中的集合列表
        private List<Integer> mCheckedList;  //已经被选中的列表 为了第二次进来做选中初始化的

        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        private boolean titleVisible = false;
        private String title;

        public Builder(Context context,List<StringInfo> list){
            this.context = context;
            this.choiceType = SINGLE_SELECT;
            this.list = list;
        }

        public Builder(Context context,int choiceType,List<StringInfo> list){
            this.context = context;
            this.choiceType = choiceType;
            this.list = list;
        }

        public Builder(Context context,int choiceType,List<StringInfo> list,String checked){
            this.context = context;
            this.choiceType = choiceType;
            this.list = list;
            mCheckedList = new ArrayList<>();
            String[] split = checked.split(",");
            for(int i=0;i<split.length;i++){
                mCheckedList.add(Integer.parseInt(split[i]));
            }
        }

        public interface OnChooseDialogListener{
            String onChoose(List<StringInfo> list);
        }

        private OnChooseDialogListener chooseDialogListener;

        public void setOnDataChooseDialogListener(OnChooseDialogListener chooseDialogListener){
            this.chooseDialogListener = chooseDialogListener;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setTitleLayoutVisible(boolean titleVisible) {
            this.titleVisible = titleVisible;
            return this;
        }

        public Builder setTitleText(String title) {
            this.title = title;
            return this;
        }

        public ChooseDialog create(){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ChooseDialog dialog = new ChooseDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.choose_dialog, null);

            llTitleLayout = (LinearLayout) layout.findViewById(R.id.ll_dialog_title);
            tvTitle = (TextView) layout.findViewById(R.id.tv_dialog_title);

            listView = (MaxListView) layout.findViewById(R.id.dialog_listview);
            listView.setListViewHeight(800);
            positiveButton = (Button) layout.findViewById(R.id.btn_dialog_ok);
            negativeButton = (Button) layout.findViewById(R.id.btn_dialog_cancel);

            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            if (titleVisible){
                llTitleLayout.setVisibility(View.VISIBLE);
                tvTitle.setText(title);
            }else{
                llTitleLayout.setVisibility(View.GONE);
            }

            mSelectList.clear();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(choiceType == SINGLE_SELECT){
                        setRadioChoice(view,position);
                    }else {
                        setMultiSelect(view,position);
                    }
                }
            });

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //多选
                    if(choiceType==MULTI_SELECT){
                        HashMap<Integer, Boolean> isSelected = dialogListAdapter.getIsSelected();
                        for(int j = 0;j < list.size(); j++) {
                            if(isSelected.get(j)) {      //如果被选中
                                list.get(j).setPosition(j);
                                mSelectList.add(list.get(j));
                            }
                        }
                    }

                    Set<StringInfo> set = new HashSet<>(mSelectList);
                    mSelectList.clear();
                    mSelectList.addAll(set);

                    if(chooseDialogListener != null){
                        chooseDialogListener.onChoose(mSelectList);
                    }
                    positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                }
            });

            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                }
            });

            dialogListAdapter = new DialogListAdapter(context, list);

            if (dialogListAdapter != null) {
                View listItem = dialogListAdapter.getView(0, null, listView);
                listItem.measure(0, 0);
                listItem.getMeasuredHeight();

                if(mCheckedList != null){
                    DialogListAdapter.HodlerView hodlerView = dialogListAdapter.getHolderView();
                    hodlerView.dialog_checkBox.toggle();
                    for(int i=0;i<mCheckedList.size();i++){
                        for(int j = 0;j < list.size(); j++) {
                            if(mCheckedList.get(i) == list.get(j).getId()){
                                dialogListAdapter.getIsSelected().put(j,true);
                                dialogListAdapter.getIsSelected().put(j, hodlerView.dialog_checkBox.isChecked());
                            }
                        }
                    }
                }
                listView.setAdapter(dialogListAdapter);
                dialogListAdapter.notifyDataSetChanged();
            }

            dialog.setContentView(layout);
            return dialog;
        }

        /**
         * 实现单选功能
         */
        private void setRadioChoice(View view, int position){
            DialogListAdapter.HodlerView holder = (DialogListAdapter.HodlerView) view.getTag();
            dialogListAdapter.getIsSelected().put(position,true);
            holder.dialog_checkBox.toggle();
            dialogListAdapter.getIsSelected().put(position, holder.dialog_checkBox.isChecked());

            //使用checkbox实现单选功能
            for (int i = 0; i < list.size(); i++) {
                dialogListAdapter.getIsSelected().put(i, false);
                dialogListAdapter.notifyDataSetChanged();
            }
            dialogListAdapter.getIsSelected().put(position,true);
            dialogListAdapter.getItem(position).setPosition(position);
            mSelectList.clear();
            mSelectList.add(dialogListAdapter.getItem(position));
            dialogListAdapter.notifyDataSetChanged();
        }

        /**
         * 实现多选
         */
        private void setMultiSelect (View view, int position) {
            DialogListAdapter.HodlerView holder = (DialogListAdapter.HodlerView) view.getTag();
            dialogListAdapter.getIsSelected().put(position, true);
            holder.dialog_checkBox.toggle();
            dialogListAdapter.getIsSelected().put(position, holder.dialog_checkBox.isChecked());
        }

    }

}
