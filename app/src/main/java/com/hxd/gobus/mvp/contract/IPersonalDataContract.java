package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.bean.User;
import com.hxd.gobus.mvp.IBaseView;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * @author: wangqingbin
 * @date: 2019/8/8 16:53
 */

public interface IPersonalDataContract {

    interface View extends IBaseView {
        void showToolbarTitle(String title);
        void showHeadPortrait(String imageUrl);
        void showUserInfo(User user);
        void showContact(Contact contact);
        void setSendLayoutVisible(boolean flag);
        void intoChatActivity(UserInfo info);
    }

    interface Model{
        Contact getContact(String targetId);
    }

}
