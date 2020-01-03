package com.hxd.gobus.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.bean.User;
import com.hxd.gobus.chat.adapter.ContactsGroupAdapter;
import com.hxd.gobus.chat.utils.ToastUtil;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.mvp.adapter.ContactsAdapter;
import com.hxd.gobus.utils.CharacterParser;
import com.hxd.gobus.utils.PinyinUtil;
import com.hxd.gobus.utils.SqliteUtils;
import com.hxd.gobus.views.RecycleViewDivider;
import com.hxd.gobus.views.SearchBox;
import com.hxd.gobus.views.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * 删除群成员
 * Created by Administrator on 2018/6/4.
 */

public class DeleteGroupPersonActivity extends BaseActivity implements ContactsAdapter.OnItemClickListener, SideBar.OnSideBarListener,View.OnClickListener{

    private Context mContext;
    private Toolbar mToolbar;
    private LinearLayout llBack;//返回
    private TextView tvToolbarTitle;//标题
    private TextView tvRightTitle;
    private SearchBox mSearchBox;
    private TextView tvLetterTip;//字母提示
    private RecyclerView rvContacts;
    private SideBar mSideBar;
    private List<Contact> mList;
    private ContactsGroupAdapter mAdapter;
    private CharacterParser mParser;
    private PinyinUtil pinyinUtil;

    private long mGroupId;
    private GroupInfo mGroupInfo;
    private List<UserInfo> mMemberInfoList = new ArrayList<UserInfo>();

    @Override
    public int bindLayout() {
        return R.layout.aty_contacts;
    }

    @Override
    public void initView() {
        mToolbar = findViewById(R.id.toolbar);
        llBack = findViewById(R.id.ll_toolbar_back);
        tvToolbarTitle = findViewById(R.id.tv_toolbar_center_title);
        tvRightTitle = findViewById(R.id.tv_toolbar_right_title);
        llBack.setVisibility(View.VISIBLE);
        tvRightTitle.setVisibility(View.VISIBLE);
        tvRightTitle.setText("删除");
        tvToolbarTitle.setText("删除成员");
        mToolbar.setTitle("");// 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(mToolbar);
        llBack.setOnClickListener(this);
        tvRightTitle.setOnClickListener(this);
        mSearchBox = findViewById(R.id.searchBox_contacts);
        rvContacts = findViewById(R.id.rv_contacts);
        tvLetterTip = findViewById(R.id.tv_contacts_letter_tip);
        mSideBar = findViewById(R.id.sidebar_contacts);
        mSideBar.setTextView(tvLetterTip);
        mSideBar.setOnSideBarListener(this);
    }

    @Override
    public void doBusiness() {
        Intent intent=getIntent();
        mGroupId = intent.getLongExtra(BaseConfig.GROUP_ID,0);

        mList = new ArrayList<>();
        mParser = CharacterParser.getInstance();
        pinyinUtil = new PinyinUtil();
        //设置RecycleView的布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvContacts.setLayoutManager(layoutManager);
        //设置RecycleView的Item间的分割线
        rvContacts.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, R.drawable.recycleview_divider));
        // 设置RecycleView的动画ItemAnimator
        rvContacts.setItemAnimator(new DefaultItemAnimator());

        //获得群组基本信息：群主ID、群组名、群组人数
        Conversation conv = JMessageClient.getGroupConversation(mGroupId);
        mGroupInfo = (GroupInfo) conv.getTargetInfo();
        mMemberInfoList = mGroupInfo.getGroupMembers();
        for(UserInfo userInfo : mMemberInfoList){
            Contact contacts = SqliteUtils.getDaoByWorkNo(mContext,userInfo.getUserName());
            if(!contacts.getWorkNo().equals(User.getInstance().getWorkNo())){
                mList.add(contacts);
            }
        }
        getContacts(mList);
        mAdapter = new ContactsGroupAdapter(mList,getApplicationContext());
        rvContacts.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        Collections.sort(mList, pinyinUtil);// 根据a-z进行排序源数据
        mAdapter.updateData(mList);

        //根据输入框输入值的改变来过滤搜索
        mSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getContacts(List<Contact> list) {
        for (int i = 0; i < list.size(); i++) {
            //汉字转换成拼音
            String pinyin = mParser.getSelling(list.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                list.get(i).setFirstLetter(sortString.toUpperCase());
            } else {
                list.get(i).setFirstLetter("#");
            }
        }
    }
    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<Contact> filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mList;
        } else {
            filterDateList.clear();
            for (Contact sortModel : mList) {
                String name = sortModel.getName();
                String dept = sortModel.getUnitName();
                if (name.indexOf(filterStr.toString()) != -1 || mParser.getSelling(name).startsWith(filterStr.toString())
                        || dept.indexOf(filterStr.toString()) != -1 || mParser.getSelling(dept).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }
        Collections.sort(filterDateList, pinyinUtil); // 根据a-z进行排序
        mAdapter.updateData(filterDateList);
    }

    @Override
    public void onItemClick(View view, int position) {
        ContactsGroupAdapter.ContactsViewHolder holder = mAdapter.getContactsViewHolder();
        boolean checked = !mAdapter.getIsCheckBoxSelected().get(mList.get(position).getPersonId());
        holder.checkBox.setChecked(checked);
        mAdapter.getIsCheckBoxSelected().put(mList.get(position).getPersonId(), checked);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onLetterChanged(String letter) {
        int position = mAdapter.getPositionForSection(letter.charAt(0));
        if (position != -1) {
            rvContacts.scrollToPosition(position);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.tv_toolbar_right_title:   //删除
                List<String> deleteList = mAdapter.getSelectedList();
                if (deleteList.size() > 0) {
                    JMessageClient.removeGroupMembers(mGroupId, deleteList, new BasicCallback() {
                        @Override
                        public void gotResult(int status, String desc) {
                            if (status == 0) {
                                Intent intent = new Intent();
                                setResult(BaseConfig.RESULT_CODE_ALL_MEMBER, intent);
                                finish();
                            } else {
                                Toast.makeText(DeleteGroupPersonActivity.this, "删除失败" + desc, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    ToastUtil.shortToast(DeleteGroupPersonActivity.this, "请至少选择一个成员");
                }
                break;
        }
    }
}
