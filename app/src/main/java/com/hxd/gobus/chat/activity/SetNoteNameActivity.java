package com.hxd.gobus.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.activeandroid.query.Update;
import com.hxd.gobus.R;
import com.hxd.gobus.chat.database.FriendEntry;
import com.hxd.gobus.chat.utils.ToastUtil;
import com.hxd.gobus.chat.utils.pinyin.HanyuPinyin;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by ${chenyn} on 2017/5/7.
 */

public class SetNoteNameActivity extends JBaseActivity {

    private EditText mNote_name;
    private Button mJmui_commit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_name);

        initTitle(true, true, "备注名", "", true, "完成");
        mNote_name = (EditText) findViewById(R.id.note_name);
        mJmui_commit_btn = (Button) findViewById(R.id.jmui_commit_btn);
        mNote_name.setText(getIntent().getStringExtra("note"));
        final String userName = getIntent().getStringExtra("user");
        mJmui_commit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = mNote_name.getText().toString();

                JMessageClient.getUserInfo(userName, new GetUserInfoCallback() {
                    @Override
                    public void gotResult(int responseCode, String responseMessage, final UserInfo info) {
                        if (responseCode == 0) {
                            info.updateNoteName(name, new BasicCallback() {
                                @Override
                                public void gotResult(int responseCode, String responseMessage) {
                                    if (responseCode == 0) {
                                        Intent intent = new Intent();
                                        intent.putExtra("updateName", name);
                                        setResult(1, intent);
                                        ToastUtil.shortToast(SetNoteNameActivity.this, "更新成功");
                                        //更新备注名时候同时更新数据库中的名字和letter
                                        new Update(FriendEntry.class).set("DisplayName=?", name).where("Username=?", userName).execute();
                                        new Update(FriendEntry.class).set("NoteName=?", name).where("Username=?", userName).execute();
                                        String newNote = HanyuPinyin.getInstance().getStringPinYin(name.substring(0, 1));
                                        new Update(FriendEntry.class).set("Letter=?", newNote.toUpperCase()).where("Username=?", userName).execute();
                                        finish();
                                    } else {
                                        ToastUtil.shortToast(SetNoteNameActivity.this, "更新失败" + responseMessage);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

}
