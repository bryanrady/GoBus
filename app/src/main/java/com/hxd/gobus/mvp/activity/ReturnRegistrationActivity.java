package com.hxd.gobus.mvp.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bryanrady.lib_permission.annotation.NeedPermission;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.config.PictureSelectorConfig;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.mvp.adapter.AttachAddRecyclerAdapter;
import com.hxd.gobus.mvp.contract.IReturnRegistrationContract;
import com.hxd.gobus.mvp.presenter.ReturnRegistrationPresenter;
import com.hxd.gobus.utils.DateUtil;
import com.hxd.gobus.utils.FileUtil;
import com.hxd.gobus.views.BottomPopupDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 还车登记页面
 * @author: wangqingbin
 * @date: 2019/8/15 12:04
 */

public class ReturnRegistrationActivity extends BaseActivity<ReturnRegistrationPresenter> implements IReturnRegistrationContract.View,
        AttachAddRecyclerAdapter.OnItemClickListener,BottomPopupDialog.OnDialogListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.tv_return_registration_apply_name)
    TextView tv_return_registration_apply_name;

    @BindView(R.id.tv_return_registration_apply_time)
    TextView tv_return_registration_apply_time;

    @BindView(R.id.tv_return_registration_destination)
    TextView tv_return_registration_destination;

    @BindView(R.id.tv_return_registration_vehicle_type)
    TextView tv_return_registration_vehicle_type;

    @BindView(R.id.tv_return_registration_vehicle_range)
    TextView tv_return_registration_vehicle_range;

    @BindView(R.id.tv_return_registration_vehicle_reason)
    TextView tv_return_registration_vehicle_reason;

    @BindView(R.id.tv_return_registration_designated_vehicle)
    TextView tv_return_registration_designated_vehicle;

    @BindView(R.id.tv_return_registration_is_use_car)
    TextView tv_return_registration_is_use_car;

    @BindView(R.id.tv_return_registration_predict_start_time)
    TextView tv_return_registration_predict_start_time;

    @BindView(R.id.tv_return_registration_predict_end_time)
    TextView tv_return_registration_predict_end_time;


    @BindView(R.id.tv_return_registration_accompany_people)
    TextView tv_return_registration_accompany_people;

    @BindView(R.id.tv_return_registration_designated_driver)
    TextView tv_return_registration_designated_driver;

    @BindView(R.id.tv_return_registration_get_car)
    TextView tv_return_registration_get_car;

    @BindView(R.id.tv_return_registration_get_car_time)
    TextView tv_return_registration_get_car_time;


    @BindView(R.id.rl_return_registration_return_car)
    RelativeLayout rl_return_registration_return_car;

    @BindView(R.id.tv_return_registration_return_car)
    TextView tv_return_registration_return_car;

    @BindView(R.id.tv_return_registration_return_car_tip)
    TextView tv_return_registration_return_car_tip;

    @BindView(R.id.rl_return_registration_return_car_time)
    RelativeLayout rl_return_registration_return_car_time;

    @BindView(R.id.tv_return_registration_return_car_time)
    TextView tv_return_registration_return_car_time;

    @BindView(R.id.tv_return_registration_return_car_time_tip)
    TextView tv_return_registration_return_car_time_tip;

    @BindView(R.id.et_return_registration_remark)
    EditText et_return_registration_remark;

    @BindView(R.id.attach_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.btn_return_registration_save)
    Button btn_return_registration_save;

    @OnClick({R.id.ll_toolbar_back,R.id.tv_toolbar_right_title,R.id.rl_return_registration_return_car,
            R.id.rl_return_registration_return_car_time, R.id.btn_return_registration_save})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.tv_toolbar_right_title:
                break;
            case R.id.rl_return_registration_return_car:
                intoChooseContactPage();
                break;
            case R.id.rl_return_registration_return_car_time:
                mPvTime.show();
                break;
            case R.id.btn_return_registration_save:
                checkSaveParams();
                break;
        }
    }

    private Integer mReturnCarPeople;
    private Vehicle mUpdateVehicle;
    private BottomPopupDialog mPopupDialog;
    private AttachAddRecyclerAdapter mAdapter;
    public static final int REQUEST_OPEN_SYSTEM_FOLDER = 10006;
    private TimePickerView mPvTime;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopupDialog != null && mPopupDialog.isShowing()){
            mPopupDialog.dismiss();
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_return_registration;
    }

    @Override
    protected void initView() {
        initToolbar();

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        initTimePicker();
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setText("还车登记");
        mTvRightTitle.setVisibility(View.GONE);
        mTvRightTitle.setText("附件管理");
    }

    private void initTimePicker() {//Dialog 模式下，在底部弹出
        mPvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String time = DateUtil.strByDate(date, DateUtil.FORMAT_1);
                tv_return_registration_return_car_time.setText(time);
                tv_return_registration_return_car_time_tip.setText("");
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {
                Log.i("pvTime", "onTimeSelectChanged");
            }
        })
                .setType(new boolean[]{true, true, true, true, true, true})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .build();

        Dialog mDialog = mPvTime.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);
            params.leftMargin = 0;
            params.rightMargin = 0;
            mPvTime.getDialogContainerLayout().setLayoutParams(params);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }

    @Override
    protected void doBusiness() {
        mPresenter.queryVehicleRegistrationDetail(getIntent());
    }

    private void intoChooseContactPage() {
        Intent intent = new Intent(this,ChooseContactActivity.class);
        intent.putExtra("choose_title","选择还车人");
        startActivityForResult(intent, VehicleConfig.REQUEST_CODE_INTO_CHOOSE_CONTACT);
    }

    @Override
    public void initVehicleData(Vehicle vehicle) {
        mUpdateVehicle = vehicle;

        tv_return_registration_apply_name.setText(vehicle.getApplyPersonName());
        if (vehicle.getApplyDate() != null){
            if (vehicle.getApplyDate().contains(" ")){
                String[] split = vehicle.getApplyDate().split(" ");
                tv_return_registration_apply_time.setText(split[0]);
            }else{
                tv_return_registration_apply_time.setText(vehicle.getApplyDate());
            }
        }
        tv_return_registration_destination.setText(vehicle.getDestination());
        tv_return_registration_vehicle_type.setText(vehicle.getUseCarTypeName());
        tv_return_registration_vehicle_range.setText(vehicle.getUseCarRangeName());
        tv_return_registration_vehicle_reason.setText(vehicle.getUseReason());
        tv_return_registration_designated_vehicle.setText(vehicle.getCarNumber());
        tv_return_registration_is_use_car.setText(vehicle.getIsUseCarName());
        tv_return_registration_predict_start_time.setText(vehicle.getPredictStartDate());
        tv_return_registration_predict_end_time.setText(vehicle.getPredictDurationName());

        tv_return_registration_accompany_people.setText(vehicle.getTravelPartner());
        tv_return_registration_designated_driver.setText(vehicle.getAssignDriverName());
        tv_return_registration_get_car.setText(vehicle.getReceiveCarPersonName());
        tv_return_registration_get_car_time.setText(vehicle.getReceiveCarDate());
    }

    @Override
    public void saveReturnRegistrationSuccess() {
        defaultFinish();
    }

    private void checkSaveParams(){
        if (mReturnCarPeople == null){
            showShortToast("还车人不能为空!");
            return;
        }
        String returnCarTime = tv_return_registration_return_car_time.getText().toString().trim();
        if (TextUtils.isEmpty(returnCarTime)){
            showShortToast("还车时间不能为空!");
            return;
        }

        if(mUpdateVehicle != null){

            mUpdateVehicle.setBackCarPerson(mReturnCarPeople);
            mUpdateVehicle.setBackCarDate(returnCarTime);
            mUpdateVehicle.setRemareks(et_return_registration_remark.getText().toString().trim());

            mUpdateVehicle.setUseCarStatus(VehicleConfig.VEHICLE_ALREADY_RETURN);

            mPresenter.updateReturnRegistration(mUpdateVehicle);
        }

    }

    @Override
    public void showAttachList(List<Attach> list) {
        mAdapter = new AttachAddRecyclerAdapter(this,list);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemDelete(int position, Attach item) {
        mPresenter.deleteAttach(item.getAttachId(),position);
    }

    @Override
    public void onItemAdd() {
        mPopupDialog = new BottomPopupDialog(this);
        mPopupDialog.setOnDialogListener(this);
        mPopupDialog.show();
    }

    @Override
    public void onItemImageClick(int position, Attach item) {
        Intent intent = new Intent(this,ImagePreviewActivity.class);
        intent.putExtra("imageName",item.getAttachName());
        intent.putExtra("nativePath",item.getNativePath());
        intent.putExtra("imageUrl",item.getAttachAddress());
        startActivity(intent);
    }

    @Override
    public void onItemFileClick(int position, Attach item) {
        Intent intent = new Intent(this,FilePreviewActivity.class);
        intent.putExtra("fileName",item.getAttachName());
        intent.putExtra("nativePath",item.getNativePath());
        intent.putExtra("fileUrl",item.getAttachAddress());
        startActivity(intent);
    }

    @Override
    public void onDialogClick(int type) {
        switch (type){
            case BottomPopupDialog.TYPE_SHOOTING:
                openCamera();
                break;
            case BottomPopupDialog.TYPE_FROM_ALBUM:
                openAlbum();
                break;
            case BottomPopupDialog.TYPE_FROM_FILE:
                openSystemFolder();
                break;
        }
    }

    @NeedPermission({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void openCamera(){
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @NeedPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    private void openAlbum(){
        PictureSelectorConfig.initSingleConfig(this);
    }

    @NeedPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    private void openSystemFolder(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult( intent,REQUEST_OPEN_SYSTEM_FOLDER);
        } catch (android.content.ActivityNotFoundException ex) {
            showShortToast("Please install a File Manager.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    images = PictureSelector.obtainMultipleResult(data);
                    String localMediaPath;
                    if (images != null){
                        for (LocalMedia localMedia : images){
                            localMediaPath = localMedia.getPath();
                            if (!TextUtils.isEmpty(localMediaPath)){
                                if(!FileUtil.isCanUpload(localMediaPath)){
                                    showShortToast("上传的文件大小不能超过10M");
                                }else{
                                    mPresenter.uploadAttach(localMediaPath);
                                }
                            }else{
                                showShortToast("文件路径找不到!");
                                return;
                            }
                        }
                    }
//                    selectList.addAll(images);
//
////                    selectList = PictureSelector.obtainMultipleResult(data);
//                    // 例如 LocalMedia 里面返回三种path
//                    // 1.media.getPath(); 为原图path
//                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
//                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
//                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
//                    adapter.setList(selectList);
//                    adapter.notifyDataSetChanged();
                    break;
                case REQUEST_OPEN_SYSTEM_FOLDER:
                    Uri uri = data.getData();
                    String localFilePath;
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        localFilePath = FileUtil.getPath(this, uri);
                    } else {//4.4以下下系统调用方法
                        localFilePath = FileUtil.getRealPathFromURI(this, uri);
                    }
                    if(TextUtils.isEmpty(localFilePath)){
                        showShortToast("文件路径找不到!");
                        return;
                    }
                    final File file = new File(localFilePath);
                    if (!file.exists()) {
                        showShortToast("权限拒绝或找不到文件!");
                        return;
                    }
                    if(!FileUtil.isCanUpload(localFilePath)){
                        showShortToast("上传的文件大小不能超过10M");
                        return;
                    }else{
                        mPresenter.uploadAttach(localFilePath);
                    }
                    break;
                case VehicleConfig.REQUEST_CODE_INTO_CHOOSE_CONTACT:
                    Contact contact = (Contact) data.getSerializableExtra("contact");
                    mReturnCarPeople = contact.getPersonId();
                    tv_return_registration_return_car.setText(contact.getName());
                    tv_return_registration_return_car_tip.setText("");
                    break;
            }
        }
    }
}
