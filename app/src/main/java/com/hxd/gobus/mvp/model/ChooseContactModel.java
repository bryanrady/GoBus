package com.hxd.gobus.mvp.model;

import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.di.scopes.FragmentScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IChooseContactContract;
import com.hxd.gobus.mvp.contract.IContactContract;

import javax.inject.Inject;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:45
 */
@ActivityScope
public class ChooseContactModel extends BaseModel implements IChooseContactContract.Model {

    @Inject
    public ChooseContactModel(){

    }

    @Override
    public void onDestroy() {

    }

}
