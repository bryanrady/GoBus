package com.hxd.gobus.mvp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseLazyFragment;
import com.hxd.gobus.bean.Todo;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.mvp.activity.MaintainApprovalActivity;
import com.hxd.gobus.mvp.activity.RepairApprovalActivity;
import com.hxd.gobus.mvp.activity.VehicleApprovalActivity;
import com.hxd.gobus.mvp.adapter.TodoRecyclerAdapter;
import com.hxd.gobus.mvp.contract.IApprovalTabContract;
import com.hxd.gobus.mvp.presenter.ApprovalTabPresenter;
import com.hxd.gobus.views.RecycleViewDivider;

import java.util.List;

import butterknife.BindView;

/**
 * Created by yifeng on 16/8/3.
 *
 */
public class ApprovalTabFragment extends BaseLazyFragment<ApprovalTabPresenter> implements IApprovalTabContract.View,
        BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv_todo)
    RecyclerView mRecyclerView;

    private static final String EXTRA_CONTENT = "content";
    private String mPositionArguments;

    public static ApprovalTabFragment newInstance(String position){
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_CONTENT, position);
        ApprovalTabFragment approvalTabFragment = new ApprovalTabFragment();
        approvalTabFragment.setArguments(arguments);
        return approvalTabFragment;
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_tab_content;
    }

    @Override
    protected void initView(View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, R.drawable.recycleview_divider));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initData() {
        mPositionArguments = getArguments().getString(EXTRA_CONTENT);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPositionArguments != null){
            mPresenter.queryList(mPositionArguments);
        }
    }

    @Override
    protected void lazyLoad(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void showList(List<Todo> list) {
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
        intent.putExtra("positionFlag",mPositionArguments);
        intent.putExtra("todo",todo);
        startActivity(intent);
    }
}
