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
import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.bean.User;
import com.hxd.gobus.bean.VehicleLicense;
import com.hxd.gobus.config.PictureSelectorConfig;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.mvp.adapter.AttachAddRecyclerAdapter;
import com.hxd.gobus.mvp.contract.IRepairApplyContract;
import com.hxd.gobus.mvp.presenter.RepairApplyPresenter;
import com.hxd.gobus.utils.DateUtils;
import com.hxd.gobus.utils.FileUtil;
import com.hxd.gobus.utils.NumberValidationUtils;
import com.hxd.gobus.views.BottomPopupDialog;
import com.hxd.gobus.views.ChooseDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 维修申请页面
 * @author: wangqingbin
 * @date: 2019/8/15 12:04
 */

public class RepairApplyActivity extends BaseActivity<RepairApplyPresenter> implements IRepairApplyContract.View,
        AttachAddRecyclerAdapter.OnItemClickListener,BottomPopupDialog.OnDialogListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.tv_repair_apply_record_number)
    TextView tv_repair_apply_record_number;

    @BindView(R.id.tv_repair_apply_name)
    TextView tv_repair_apply_name;

    @BindView(R.id.tv_repair_apply_time)
    TextView tv_repair_apply_time;

    @BindView(R.id.rl_repair_apply_vehicle_name)
    RelativeLayout rl_repair_apply_vehicle_name;

    @BindView(R.id.tv_repair_apply_vehicle_name)
    TextView tv_repair_apply_vehicle_name;

    @BindView(R.id.tv_repair_apply_vehicle_name_tip)
    TextView tv_repair_apply_vehicle_name_tip;

    @BindView(R.id.tv_repair_apply_vehicle_license)
    TextView tv_repair_apply_vehicle_license;

    @BindView(R.id.et_repair_apply_estimated_cost)
    EditText et_repair_apply_estimated_cost;

    @BindView(R.id.et_repair_apply_handle_person)
    EditText et_repair_apply_handle_person;

    @BindView(R.id.et_repair_apply_cost_bearing)
    EditText et_repair_apply_cost_bearing;

    @BindView(R.id.et_repair_apply_situation_description)
    EditText et_repair_apply_situation_description;

    @BindView(R.id.et_repair_apply_remark)
    EditText et_repair_apply_remark;

    @BindView(R.id.attach_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.btn_repair_apply_save)
    Button btn_repair_apply_save;

    @BindView(R.id.btn_repair_apply_commit)
    Button btn_repair_apply_commit;

    @OnClick({R.id.ll_toolbar_back,R.id.tv_toolbar_right_title,R.id.rl_repair_apply_vehicle_name,
            R.id.btn_repair_apply_save,R.id.btn_repair_apply_commit})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.tv_toolbar_right_title:
                break;
            case R.id.rl_repair_apply_vehicle_name:
                mPresenter.queryAvailableVehicle();
                break;
            case R.id.btn_repair_apply_save:
                checkAddParamsAndCommit("0");
                break;
            case R.id.btn_repair_apply_commit:
                checkAddParamsAndCommit("1");
                break;
        }
    }

    private Integer mInfoManageId;
    private Repair mUpdateRepair;
    private AttachAddRecyclerAdapter mAdapter;
    private List<Attach> mAttachList;
    private BottomPopupDialog mPopupDialog;
    public static final int REQUEST_OPEN_SYSTEM_FOLDER = 10001;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopupDialog != null && mPopupDialog.isShowing()){
            mPopupDialog.dismiss();
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_repair_apply;
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
        mTvCenterTitle.setText("维修申请");
        mTvRightTitle.setVisibility(View.GONE);
        mTvRightTitle.setText("附件管理");
    }

    @Override
    protected void doBusiness() {
        tv_repair_apply_name.setText(User.getInstance().getName());
        tv_repair_apply_time.setText(DateUtils.getCurTimeFormat(DateUtils.FORMAT_3));
        mPresenter.getVehicleData(getIntent());
    }

    @Override
    public void initRepairData(Repair repair) {
        mUpdateRepair = repair;

        mInfoManageId = repair.getInfoManageId();

        tv_repair_apply_record_number.setText(repair.getRecordNumber());
        tv_repair_apply_name.setText(repair.getApplyPersonName());
        if (repair.getApplyDate() != null){
            if (repair.getApplyDate().contains(" ")){
                String[] split = repair.getApplyDate().split(" ");
                tv_repair_apply_time.setText(split[0]);
            }else{
                tv_repair_apply_time.setText(repair.getApplyDate());
            }
        }
        if (repair.getInfoManageId() != null){
            tv_repair_apply_vehicle_name.setText(repair.getVehicleName());
            tv_repair_apply_vehicle_name_tip.setText("");
        }
        if (repair.getEstimatedCosts() != null){
            et_repair_apply_estimated_cost.setText(repair.getEstimatedCosts().toString());
        }
        tv_repair_apply_vehicle_license.setText(repair.getVehicleMark());
        et_repair_apply_handle_person.setText(repair.getHandlerName());
        et_repair_apply_cost_bearing.setText(repair.getCostPayer());
        et_repair_apply_situation_description.setText(repair.getRepairDetail());
        et_repair_apply_remark.setText(repair.getRemarks());
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
                        tv_repair_apply_vehicle_name.setText(stringInfo.getRemark());
                        tv_repair_apply_vehicle_name_tip.setText("");
                        tv_repair_apply_vehicle_license.setText(stringInfo.getTitle());
                        mInfoManageId = stringInfo.getId();
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
    public void saveCommitSuccess() {
        defaultFinish();
    }

    private void checkAddParamsAndCommit(String approvalStatus){
        String applyTime = tv_repair_apply_time.getText().toString().trim();
        if (mInfoManageId == null){
            showShortToast("车辆名称不能为空!");
            return;
        }
        String license = tv_repair_apply_vehicle_license.getText().toString().trim();
        if (TextUtils.isEmpty(license)){
            showShortToast("车牌号不能为空!");
            return;
        }
        String estimatedCost = et_repair_apply_estimated_cost.getText().toString().trim();
        if (!TextUtils.isEmpty(estimatedCost)) {
            if(NumberValidationUtils.isNumeric(estimatedCost)){
                if(estimatedCost.length() > 9 || estimatedCost.length() < 1){
                    showShortToast("本次预计费用整数部分必须是1-9位的数!");
                    return;
                }
            }else if(NumberValidationUtils.isDecimal(estimatedCost)){
                String s1 = estimatedCost.substring(0, estimatedCost.indexOf("."));
                String s2 = estimatedCost.substring(estimatedCost.indexOf(".")+1, estimatedCost.length());
                if(s1.length() > 6 || s1.length() < 1){
                    showShortToast("本次预计费用整数部分必须是1-6位的数!");
                    return;
                }
                if(s2.length() > 2 || s2.length() < 1){
                    showShortToast("本次预计费用小数部分必须是1-2位的数!");
                    return;
                }
            }else{
                showShortToast("请输入正确的金额");
                return;
            }
        }

        String handlePerson = et_repair_apply_handle_person.getText().toString().trim();
        String costBearing = et_repair_apply_cost_bearing.getText().toString().trim();
        String situationDescription = et_repair_apply_situation_description.getText().toString().trim();
        String remark = et_repair_apply_remark.getText().toString().trim();

        String dataStatus = mPresenter.getDataStatus();
        if (dataStatus != null){
            if (VehicleConfig.APPLY_ADD.equals(dataStatus)){
                Repair repair = new Repair();
                if (mAttachList != null && mAttachList.size() > 0){
                    if (!TextUtils.isEmpty(mPresenter.getDataId())){
                        repair.setUuid(mPresenter.getDataId());
                    }
                }
                repair.setApplyPerson(User.getInstance().getPersonId());
                repair.setApplyDate(applyTime);
                repair.setInfoManageId(mInfoManageId);
                if (!TextUtils.isEmpty(estimatedCost)) {
                    repair.setEstimatedCosts(new BigDecimal(estimatedCost));
                }
                if (!TextUtils.isEmpty(handlePerson)){
                    repair.setHandler(handlePerson);
                }
                if (!TextUtils.isEmpty(costBearing)){
                    repair.setCostPayer(costBearing);
                }
                if (!TextUtils.isEmpty(situationDescription)){
                    repair.setRepairDetail(situationDescription);
                }
                if (!TextUtils.isEmpty(remark)){
                    repair.setRemarks(remark);
                }
                repair.setApprovalStatus(approvalStatus);

                mPresenter.addRepairApply(repair);
            }else{
                if(mUpdateRepair != null){
                    mUpdateRepair.setApplyPerson(User.getInstance().getPersonId());
                    mUpdateRepair.setApplyDate(applyTime);
                    mUpdateRepair.setInfoManageId(mInfoManageId);
                    if (!TextUtils.isEmpty(estimatedCost)) {
                        mUpdateRepair.setEstimatedCosts(new BigDecimal(estimatedCost));
                    }
                    if (!TextUtils.isEmpty(handlePerson)){
                        mUpdateRepair.setHandler(handlePerson);
                    }
                    if (!TextUtils.isEmpty(costBearing)){
                        mUpdateRepair.setCostPayer(costBearing);
                    }
                    if (!TextUtils.isEmpty(situationDescription)){
                        mUpdateRepair.setRepairDetail(situationDescription);
                    }
                    if (!TextUtils.isEmpty(remark)){
                        mUpdateRepair.setRemarks(remark);
                    }
                    mUpdateRepair.setApprovalStatus(approvalStatus);

                    mPresenter.updateRepairApply(mUpdateRepair);
                }
            }
        }
    }

    @Override
    public void showAttachList(List<Attach> list) {
        mAttachList = list;
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

            }
        }
    }

}
