package com.hxd.gobus.mvp.presenter;

import android.content.Intent;
import android.view.View;

import com.hxd.gobus.BusApp;
import com.hxd.gobus.R;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.bean.User;
import com.hxd.gobus.chat.entity.Event;
import com.hxd.gobus.chat.entity.EventType;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IPersonalDataContract;
import com.hxd.gobus.mvp.model.PersonalDataModel;

import javax.inject.Inject;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.eventbus.EventBus;

/**
 * @author: wangqingbin
 * @date: 2019/7/25 10:26
 */
@ActivityScope
public class PersonalDataPresenter extends BasePresenter<IPersonalDataContract.View,PersonalDataModel> {

    private String mTargetId;
    private String mTargetAppKey;

    @Inject
    public PersonalDataPresenter(){

    }

    public void initData(Intent intent){
        String tag = intent.getStringExtra("TAG");
        String imageName = null;
        if (tag.equals("MyFragment")) {
            getView().showToolbarTitle("个人资料");
            User user = (User) intent.getSerializableExtra("user");
            if (user != null) {
                getView().showUserInfo(user);
                imageName = user.getPhotoUrl();
            }
        } else if (tag.equals("single")) {
            mTargetId = intent.getStringExtra(BaseConfig.TARGET_ID);
            Contact contact = (Contact) intent.getSerializableExtra("contacts");
            if(contact != null){
                if (User.getInstance().getName().equals(contact.getName())){
                    getView().showToolbarTitle("个人资料");
                }else{
                    getView().showToolbarTitle("详细资料");
                    getView().setSendLayoutVisible(true);
                }
                getView().showContact(contact);
                imageName = contact.getPhotoUrl();
            }

        } else if(tag.equals("MessageAlreadyReadFragment") || tag.equals("MessageNotReadFragment")) {
            mTargetId = intent.getStringExtra(BaseConfig.TARGET_ID);
            mTargetAppKey = intent.getStringExtra(BaseConfig.TARGET_APP_KEY);
            if (mTargetAppKey == null) {
                mTargetAppKey = JMessageClient.getMyInfo().getAppKey();
            }

            Contact contact = getModel().getContact(mTargetId);
            if(contact != null){
                if (User.getInstance().getName().equals(contact.getName())){
                    getView().showToolbarTitle("个人资料");
                }else{
                    getView().showToolbarTitle("详细资料");
                    getView().setSendLayoutVisible(true);
                }
                getView().showContact(contact);
                imageName = contact.getPhotoUrl();
            }

        } else if (tag.equals("group_gridview_group") || tag.equals("group_gridview")) {    //从群组信息或更多群组信息过来
            mTargetId = intent.getStringExtra(BaseConfig.TARGET_ID);
            mTargetAppKey = intent.getStringExtra(BaseConfig.TARGET_APP_KEY);
            if (mTargetAppKey == null) {
                mTargetAppKey = JMessageClient.getMyInfo().getAppKey();
            }
            Contact contact = getModel().getContact(mTargetId);
            if(contact != null){
                if (User.getInstance().getName().equals(contact.getName())){
                    getView().showToolbarTitle("个人资料");
                }else{
                    getView().showToolbarTitle("详细资料");
                    getView().setSendLayoutVisible(true);
                }
                getView().showContact(contact);
                imageName = contact.getPhotoUrl();
            }

        } else if (tag.equals("chattingList")) {   //从聊天界面点击头像过来的
            mTargetId = intent.getStringExtra(BaseConfig.TARGET_ID);
            mTargetAppKey = intent.getStringExtra(BaseConfig.TARGET_APP_KEY);
            String tag2 = intent.getStringExtra("TAG2");
            if (mTargetAppKey == null) {
                mTargetAppKey = JMessageClient.getMyInfo().getAppKey();
            }
            Contact contact = getModel().getContact(mTargetId);
            if(contact != null){
                if("single".equals(tag2)){
                    if (User.getInstance().getName().equals(contact.getName())){
                        getView().showToolbarTitle("个人资料");
                    }else{
                        getView().showToolbarTitle("详细资料");
                    }
                }else{
                    if (User.getInstance().getName().equals(contact.getName())){
                        getView().showToolbarTitle("个人资料");
                    }else{
                        getView().showToolbarTitle("详细资料");
                        getView().setSendLayoutVisible(true);
                    }
                }
                getView().showContact(contact);
                imageName = contact.getPhotoUrl();
            }

        } else if (tag.equals("from_businesscard")) {  //从名片点击进来
            mTargetId = intent.getStringExtra(BaseConfig.TARGET_ID);
            mTargetAppKey = intent.getStringExtra(BaseConfig.TARGET_APP_KEY);
            if (mTargetAppKey == null) {
                mTargetAppKey = JMessageClient.getMyInfo().getAppKey();
            }
            Contact contact = getModel().getContact(mTargetId);
            if(contact != null){
                if (User.getInstance().getName().equals(contact.getName())){
                    getView().showToolbarTitle("个人资料");
                }else{
                    getView().showToolbarTitle("详细资料");
                    getView().setSendLayoutVisible(true);
                }
                getView().showContact(contact);
                imageName = contact.getPhotoUrl();
            }

        }else if(tag.equals("group_gridview_single")){  //从聊天设置过来
            mTargetId = intent.getStringExtra(BaseConfig.TARGET_ID);
            mTargetAppKey = intent.getStringExtra(BaseConfig.TARGET_APP_KEY);
            if (mTargetAppKey == null) {
                mTargetAppKey = JMessageClient.getMyInfo().getAppKey();
            }
            Contact contact = getModel().getContact(mTargetId);
            if(contact != null){
                if (User.getInstance().getName().equals(contact.getName())){
                    getView().showToolbarTitle("个人资料");
                }else{
                    getView().showToolbarTitle("详细资料");
                    getView().setSendLayoutVisible(true);
                }
                getView().showContact(contact);
                imageName = contact.getPhotoUrl();
            }

        } else{ //从首页通讯录中进来
            Contact contact = (Contact) intent.getSerializableExtra("contacts");
            if(contact != null){
                if (User.getInstance().getName().equals(contact.getName())){
                    getView().showToolbarTitle("个人资料");
                }else{
                    getView().showToolbarTitle("详细资料");
                    getView().setSendLayoutVisible(true);
                }
                getView().showContact(contact);
                imageName = contact.getPhotoUrl();

                mTargetId = contact.getWorkNo();
            }
        }

        //加载头像
        if(imageName != null){
            String imageUrl = Constant.HEAD_PICTURE_DOWNLOAD_URL + imageName;
            getView().showHeadPortrait(imageUrl);
        }

    }

    public void sendMessage(){
        if (mTargetId != null){
            JMessageClient.getUserInfo(mTargetId, new GetUserInfoCallback() {
                @Override
                public void gotResult(int responseCode, String responseMessage, UserInfo info) {
                    if (responseCode == 0) {
                        if (info == null){
                            getView().showToast("无法获取用户信息,请检查网络连接!");
                            return;
                        }
                        //创建会话
                        Conversation conv = JMessageClient.getSingleConversation(info.getUserName(), info.getAppKey());
                        //如果会话为空，使用EventBus通知会话列表添加新会话
                        if (conv == null) {
                            conv = Conversation.createSingleConversation(info.getUserName(), info.getAppKey());
                            EventBus.getDefault().post(new Event.Builder()
                                    .setType(EventType.createConversation)
                                    .setConversation(conv)
                                    .build());
                        }

                        getView().intoChatActivity(info);
                    }
                }
            });
        }
    }
}
