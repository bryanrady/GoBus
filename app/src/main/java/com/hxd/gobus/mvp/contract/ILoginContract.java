package com.hxd.gobus.mvp.contract;
import com.hxd.gobus.bean.User;
import com.hxd.gobus.mvp.IBaseView;

import cn.jpush.im.android.api.model.UserInfo;
import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:37
 */

public interface ILoginContract {

    interface View extends IBaseView {
        void loginSuccess();

        void isCheck(boolean isCheck);

        void showUserInfo(User user);

        void showUsername(User user);
    }

    interface Model{
        Observable<String> verifyUser(String loginName, String loginPassword);

        void setAlreadyLogin();

        void saveCheckStatus(boolean isChecked);

        boolean getCheckStatus();

        void saveUser(User user);

        void setCachedPsw(String password);

        User getUser();
    }

}
