package com.hxd.gobus.mvp.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.Todo;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.mvp.adapter.TodoRecyclerAdapter;
import com.hxd.gobus.mvp.contract.ITodoContract;
import com.hxd.gobus.mvp.presenter.TodoPresenter;
import com.hxd.gobus.views.RecycleViewDivider;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: wangqingbin
 * @date: 2019/8/22 19:06
 */

public class TodoActivity extends BaseActivity<TodoPresenter> implements ITodoContract.View,BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.rv_todo)
    RecyclerView mRecyclerView;

    @OnClick({R.id.ll_toolbar_back,R.id.tv_toolbar_right_title})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.tv_toolbar_right_title:
                startActivity(AlreadyDoActivity.class);
                break;
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_todo;
    }

    @Override
    protected void initView() {
        initToolbar();

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, R.drawable.recycleview_divider));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setText("待办事项");
        mTvRightTitle.setVisibility(View.VISIBLE);
        mTvRightTitle.setText("已办事项");
    }

    @Override
    protected void doBusiness() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getTodoList();
    }

    @Override
    public void showTodoList(List<Todo> list) {
        TodoRecyclerAdapter adapter = new TodoRecyclerAdapter(R.layout.recycler_todo_list_item, list);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Todo todo = (Todo) adapter.getItem(position);
        Intent intent = null;
        if (BaseConfig.DATUMTYPE_VEHICLE.equals(todo.getDatumType())){
            intent = new Intent(mContext, VehicleApprovalActivity.class);
        }else if(BaseConfig.DATUMTYPE_REPAIR.equals(todo.getDatumType())){
            intent = new Intent(mContext, RepairApprovalActivity.class);
        }else if(BaseConfig.DATUMTYPE_MAINTAIN.equals(todo.getDatumType())){
            intent = new Intent(mContext, MaintainApprovalActivity.class);
        }
        intent.putExtra("source",BaseConfig.SOURCE_TODO);
        intent.putExtra("positionFlag",BaseConfig.SIGN_TO_DO);
        intent.putExtra("todo",todo);
        startActivity(intent);
    }
}
