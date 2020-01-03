package com.hxd.gobus.mvp.model;

import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IPersonalDataContract;
import com.hxd.gobus.utils.ContactUtils;

import javax.inject.Inject;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:45
 */
@ActivityScope
public class PersonalDataModel extends BaseModel implements IPersonalDataContract.Model {

    @Inject
    public PersonalDataModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Contact getContact(String targetId) {
        Contact contact = ContactUtils.getInstance().queryContactFromDatabase(targetId);
        return contact;
    }
}
