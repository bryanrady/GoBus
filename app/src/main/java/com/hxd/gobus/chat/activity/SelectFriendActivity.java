package com.hxd.gobus.chat.activity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.HorizontalScrollView;

import com.hxd.gobus.R;
import com.hxd.gobus.chat.adapter.CreateGroupAdapter;
import com.hxd.gobus.chat.controller.SelectFriendController;
import com.hxd.gobus.chat.utils.SoftKeyBoardStateHelper;
import com.hxd.gobus.chat.view.SelectFriendView;

public class SelectFriendActivity extends JBaseActivity{

    private SelectFriendView mView;
    private SelectFriendController mController;

    private HorizontalScrollView scrollViewSelected;
    private GridView imageSelectedGridView;
    private CreateGroupAdapter mGroupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        mView = (SelectFriendView) findViewById(R.id.select_friend_view);

        mGroupAdapter = new CreateGroupAdapter(SelectFriendActivity.this);
        scrollViewSelected = (HorizontalScrollView) findViewById(R.id.contact_select_area);
        imageSelectedGridView = (GridView) findViewById(R.id.contact_select_area_grid);
        imageSelectedGridView.setAdapter(mGroupAdapter);
        mView.initModule(mRatio, mDensity);

        mController = new SelectFriendController(mView, this, getIntent().getLongExtra("add_friend_group_id", 0), scrollViewSelected, mGroupAdapter, imageSelectedGridView);
        mView.setListeners(mController);
        mView.setSideBarTouchListener(mController);
        mView.setTextWatcher(mController);
        mView.setGoneSideBar(false);


        SoftKeyBoardStateHelper helper = new SoftKeyBoardStateHelper(findViewById(R.id.select_friend_view));
        helper.addSoftKeyboardStateListener(new SoftKeyBoardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                mView.setGoneSideBar(true);
            }

            @Override
            public void onSoftKeyboardClosed() {
                mView.setGoneSideBar(false);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mController.delNotFriend();
    }
}
