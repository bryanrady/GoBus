package com.hxd.gobus.mvp.model;

import com.hxd.gobus.di.scopes.FragmentScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IContactContract;

import javax.inject.Inject;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:45
 */
@FragmentScope
public class ContactModel extends BaseModel implements IContactContract.Model {

    @Inject
    public ContactModel(){

    }

    @Override
    public void onDestroy() {

    }

}
