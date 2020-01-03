package com.hxd.gobus.mvp.contract;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:37
 */

public interface IContactContract {

    interface View extends IBaseView {
        void showContacts(List<Contact> list);
    }

    interface Model{

    }

}
