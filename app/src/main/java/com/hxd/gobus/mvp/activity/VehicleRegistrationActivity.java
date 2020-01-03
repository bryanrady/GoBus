package com.hxd.gobus.mvp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bryanrady.lib_permission.annotation.NeedPermission;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.bean.Driver;
import com.hxd.gobus.bean.KeyCode;
import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.bean.VehicleLicense;
import com.hxd.gobus.config.PictureSelectorConfig;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.mvp.adapter.AttachAddRecyclerAdapter;
import com.hxd.gobus.mvp.contract.IVehicleRegistrationContract;
import com.hxd.gobus.mvp.presenter.VehicleRegistrationPresenter;
import com.hxd.gobus.utils.DateUtils;
import com.hxd.gobus.utils.FileUtil;
import com.hxd.gobus.views.BottomPopupDialog;
import com.hxd.gobus.views.ChooseDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用车登记页面
 * @author: wangqingbin
 * @date: 2019/8/15 12:04
 */

public class VehicleRegistrationActivity extends BaseActivity<VehicleRegistrationPresenter> implements IVehicleRegistrationContract.View,
        AttachAddRecyclerAdapter.OnItemClickListener,BottomPopupDialog.OnDialogListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.tv_vehicle_registration_apply_name)
    TextView tv_vehicle_registration_apply_name;

    @BindView(R.id.tv_vehicle_registration_apply_time)
    TextView tv_vehicle_registration_apply_time;

    @BindView(R.id.tv_vehicle_registration_destination)
    TextView tv_vehicle_registration_destination;

    @BindView(R.id.tv_vehicle_registration_vehicle_type)
    TextView tv_vehicle_registration_vehicle_type;

    @BindView(R.id.tv_vehicle_registration_vehicle_range)
    TextView tv_vehicle_registration_vehicle_range;

    @BindView(R.id.tv_vehicle_registration_vehicle_reason)
    TextView tv_vehicle_registration_vehicle_reason;

    @BindView(R.id.rl_vehicle_registration_designated_vehicle)
    RelativeLayout rl_vehicle_registration_designated_vehicle;

    @BindView(R.id.tv_vehicle_registration_designated_vehicle)
    TextView tv_vehicle_registration_designated_vehicle;

    @BindView(R.id.tv_vehicle_registration_designated_vehicle_tip)
    TextView tv_vehicle_registration_designated_vehicle_tip;

    @BindView(R.id.tv_vehicle_registration_predict_start_time)
    TextView tv_vehicle_registration_predict_start_time;

    @BindView(R.id.tv_vehicle_registration_predict_end_time)
    TextView tv_vehicle_registration_predict_end_time;

    @BindView(R.id.rl_vehicle_registration_designated_driver)
    RelativeLayout rl_vehicle_registration_designated_driver;

    @BindView(R.id.tv_vehicle_registration_designated_driver)
    TextView tv_vehicle_registration_designated_driver;

    @BindView(R.id.tv_vehicle_registration_designated_driver_tip)
    TextView tv_vehicle_registration_designated_driver_tip;

    @BindView(R.id.tv_vehicle_registration_accompany_people)
    TextView tv_vehicle_registration_accompany_people;


    @BindView(R.id.rl_vehicle_registration_is_use)
    RelativeLayout rl_vehicle_registration_is_use;

    @BindView(R.id.tv_vehicle_registration_is_use)
    TextView tv_vehicle_registration_is_use;

    @BindView(R.id.tv_vehicle_registration_is_use_tip)
    TextView tv_vehicle_registration_is_use_tip;

    @BindView(R.id.rl_vehicle_registration_get_car)
    RelativeLayout rl_vehicle_registration_get_car;

    @BindView(R.id.tv_vehicle_registration_get_car)
    TextView tv_vehicle_registration_get_car;

    @BindView(R.id.tv_vehicle_registration_get_car_tip)
    TextView tv_vehicle_registration_get_car_tip;

    @BindView(R.id.tv_vehicle_registration_get_car_time)
    TextView tv_vehicle_registration_get_car_time;

    @BindView(R.id.et_vehicle_registration_remark)
    EditText et_vehicle_registration_remark;

    @BindView(R.id.attach_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.btn_vehicle_registration_save)
    Button btn_vehicle_registration_save;

    @OnClick({R.id.ll_toolbar_back,R.id.tv_toolbar_right_title,R.id.rl_vehicle_registration_designated_vehicle,
            R.id.rl_vehicle_registration_designated_driver,R.id.rl_vehicle_registration_is_use,
            R.id.rl_vehicle_registration_get_car, R.id.btn_vehicle_registration_save})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.tv_toolbar_right_title:
                break;
            case R.id.rl_vehicle_registration_designated_vehicle:
                mPresenter.queryAvailableVehicle();
                break;
            case R.id.rl_vehicle_registration_designated_driver:
                mPresenter.queryDesignatedDriver();
                break;
            case R.id.rl_vehicle_registration_is_use:
                mPresenter.queryIsUseTheCar();
                break;
            case R.id.rl_vehicle_registration_get_car:
                intoChooseContactPage();
                break;
            case R.id.btn_vehicle_registration_save:
                checkSaveParams();
                break;
        }
    }

    private Integer mAssignDriver;
    private Integer mDesignatedVehicle;
    private Integer mGetCarPeople;
    private String mIsUseTheCar;
    private Vehicle mUpdateVehicle;
    private BottomPopupDialog mPopupDialog;
    private AttachAddRecyclerAdapter mAdapter;
    public static final int REQUEST_OPEN_SYSTEM_FOLDER = 10005;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopupDialog != null && mPopupDialog.isShowing()){
            mPopupDialog.dismiss();
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_vehicle_registration;
    }

    @Override
    protected void initView() {
        initToolbar();

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setText("用车登记");
        mTvRightTitle.setVisibility(View.GONE);
        mTvRightTitle.setText("附件管理");
    }

    @Override
    protected void doBusiness() {
        mPresenter.queryVehicleRegistrationDetail(getIntent());
    }

    @Override
    public void initVehicleData(Vehicle vehicle) {
        mUpdateVehicle = vehicle;

        mDesignatedVehicle = vehicle.getInfoManageId();
        mAssignDriver = vehicle.getAssignDriver();

        tv_vehicle_registration_apply_name.setText(vehicle.getApplyPersonName());
        if (vehicle.getApplyDate() != null){
            if (vehicle.getApplyDate().contains(" ")){
                String[] split = vehicle.getApplyDate().split(" ");
                tv_vehicle_registration_apply_time.setText(split[0]);
            }else{
                tv_vehicle_registration_apply_time.setText(vehicle.getApplyDate());
            }
        }
        tv_vehicle_registration_destination.setText(vehicle.getDestination());
        tv_vehicle_registration_vehicle_type.setText(vehicle.getUseCarTypeName());
        tv_vehicle_registration_vehicle_range.setText(vehicle.getUseCarRangeName());
        tv_vehicle_registration_vehicle_reason.setText(vehicle.getUseReason());
        if (vehicle.getInfoManageId() != null){
            tv_vehicle_registration_designated_vehicle.setText(vehicle.getCarNumber());
            tv_vehicle_registration_designated_vehicle_tip.setText("");
        }
        tv_vehicle_registration_accompany_people.setText(vehicle.getTravelPartner());
        if (vehicle.getAssignDriver() != null){
            tv_vehicle_registration_designated_driver.setText(vehicle.getAssignDriverName());
            tv_vehicle_registration_designated_driver_tip.setText("");
        }
        tv_vehicle_registration_predict_start_time.setText(vehicle.getPredictStartDate());
        tv_vehicle_registration_predict_end_time.setText(vehicle.getPredictDurationName());

        tv_vehicle_registration_get_car_time.setText(DateUtils.getCurTimeFormat(DateUtils.FORMAT_1));

        mIsUseTheCar = "0";
        tv_vehicle_registration_is_use.setText("使用");
        tv_vehicle_registration_is_use_tip.setText("");
    }

    @Override
    public void showVehicleLicenseDialog(List<VehicleLicense> list) {
        List<StringInfo> stringInfoList = mPresenter.transferLicenseList(list);
        if (stringInfoList != null && stringInfoList.size() > 0){
            ChooseDialog.Builder builder = new ChooseDialog.Builder(this,stringInfoList);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setOnDataChooseDialogListener(new ChooseDialog.Builder.OnChooseDialogListener() {

                @Override
                public String onChoose(List<StringInfo> list) {
                    for(StringInfo stringInfo : list){
                        tv_vehicle_registration_designated_vehicle.setText(stringInfo.getTitle());
                        tv_vehicle_registration_designated_vehicle_tip.setText("");
                        mDesignatedVehicle = stringInfo.getId();
                    }
                    return null;
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    @Override
    public void showDesignatedDriverDialog(List<Driver> list) {
        List<StringInfo> stringInfoList = mPresenter.transferDriverList(list);
        if (stringInfoList != null && stringInfoList.size() > 0){
            ChooseDialog.Builder builder = new ChooseDialog.Builder(this,stringInfoList);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setOnDataChooseDialogListener(new ChooseDialog.Builder.OnChooseDialogListener() {

                @Override
                public String onChoose(List<StringInfo> list) {
                    for(StringInfo stringInfo : list){
                        tv_vehicle_registration_designated_driver.setText(stringInfo.getTitle());
                        tv_vehicle_registration_designated_driver_tip.setText("");
                        mAssignDriver = stringInfo.getId();
                    }
                    return null;
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    @Override
    public void showIsUseTheCarDialog(List<KeyCode> list) {
        List<StringInfo> stringInfoList = mPresenter.transferKeyCodeList(list);
        if (stringInfoList != null && stringInfoList.size() > 0){
            ChooseDialog.Builder builder = new ChooseDialog.Builder(this,stringInfoList);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setOnDataChooseDialogListener(new ChooseDialog.Builder.OnChooseDialogListener() {

                @Override
                public String onChoose(List<StringInfo> list) {
                    for(StringInfo stringInfo : list){
                        tv_vehicle_registration_is_use.setText(stringInfo.getTitle());
                        tv_vehicle_registration_is_use_tip.setText("");
                        mIsUseTheCar = stringInfo.getCodeType();
                    }
                    return null;
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    @Override
    public void saveVehicleRegistrationSuccess() {
        defaultFinish();
    }

    private void intoChooseContactPage() {
        Intent intent = new Intent(this,ChooseContactActivity.class);
        intent.putExtra("choose_title","选择领车人");
        startActivityForResult(intent, VehicleConfig.REQUEST_CODE_INTO_CHOOSE_CONTACT);
    }

    private void checkSaveParams(){
        if (mDesignatedVehicle == null){
            showShortToast("指定车辆不能为空!");
            return;
        }
        if (TextUtils.isEmpty(mIsUseTheCar)){
            showShortToast("是否用车不能为空!");
            return;
        }
        if (mGetCarPeople == null){
            showShortToast("领车人不能为空!");
            return;
        }
        String getCarTime = tv_vehicle_registration_get_car_time.getText().toString().trim();

        if(mUpdateVehicle != null){
            mUpdateVehicle.setInfoManageId(mDesignatedVehicle);
            if (mAssignDriver != null){
                mUpdateVehicle.setAssignDriver(mAssignDriver);
            }
            mUpdateVehicle.setIsUseCar(mIsUseTheCar);
            mUpdateVehicle.setReceiveCarPerson(mGetCarPeople);
            if (!TextUtils.isEmpty(getCarTime)){
                mUpdateVehicle.setReceiveCarDate(getCarTime);
            }
            mUpdateVehicle.setRemareks(et_vehicle_registration_remark.getText().toString().trim());
            mUpdateVehicle.setUseCarStatus(VehicleConfig.VEHICLE_ALREADY_REGISTRATION);

            mPresenter.updateVehicleRegistration(mUpdateVehicle);
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
                    mGetCarPeople = contact.getPersonId();
                    tv_vehicle_registration_get_car.setText(contact.getName());
                    tv_vehicle_registration_get_car_tip.setText("");
                    break;
            }
        }
    }
}
