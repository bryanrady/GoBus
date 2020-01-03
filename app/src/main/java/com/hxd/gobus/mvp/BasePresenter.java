package com.hxd.gobus.mvp;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 基本业务逻辑基类
 * @author: wangqingbin
 * @date: 2019/7/16 15:07
 */

public abstract class BasePresenter<V extends IBaseView,M extends BaseModel> {

    private WeakReference<V> mRefView;
    private M mModel;
    private CompositeDisposable mCompositeDisposable;

    /**
     * @description 这里使用@Inject作用在方法上，其实就是内部通过attach（）赋值而已
     * @author wangqingbin
     * @time 2019/7/16 16:29
     * @param
     * @return
     */
    @Inject
    public void attach(V view,M model) {
        this.mRefView = new WeakReference<V>(view);
        this.mModel = model;
    }

    public void onDestroy() {
        unDispose();
        onDestroyView();
        onDestroyModel();
    }

    protected V getView(){
        return mRefView != null ? mRefView.get() : null;
    }

    protected M getModel(){
        return mModel != null ? mModel : null;
    }

    /**
     * @description 将Disposable添加到CompositeDisposable中统一管理
     * @author wangqingbin
     * @time 2019/7/16 15:26
     * @param
     * @return
     */
    protected void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    /**
     * @description 保证 Activity 结束时取消所有正在执行的订阅
     * @author wangqingbin
     * @time 2019/7/16 15:26
     * @param
     * @return
     */
    private void unDispose() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
    }

    private void onDestroyView() {
        if (mRefView != null) {
            mRefView.clear();
            mRefView = null;
        }
    }

    private void onDestroyModel() {
        if (mModel != null){
            mModel.onDestroy();
            this.mModel = null;
        }
    }

}
