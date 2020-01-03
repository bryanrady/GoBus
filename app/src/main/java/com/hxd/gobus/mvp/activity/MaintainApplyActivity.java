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
import com.hxd.gobus.bean.Maintain;
import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.bean.User;
import com.hxd.gobus.bean.VehicleUpkeep;
import com.hxd.gobus.config.PictureSelectorConfig;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.mvp.adapter.AttachAddRecyclerAdapter;
import com.hxd.gobus.mvp.contract.IMaintainApplyContract;
import com.hxd.gobus.mvp.presenter.MaintainApplyPresenter;
import com.hxd.gobus.utils.DateUtils;
import com.hxd.gobus.utils.FileUtil;
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
 * 保养申请页面
 * @author: wangqingbin
 * @date: 2019/8/15 12:04
 */

public class MaintainApplyActivity extends BaseActivity<MaintainApplyPresenter> implements IMaintainApplyContract.View,
        AttachAddRecyclerAdapter.OnItemClickListener,BottomPopupDialog.OnDialogListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.tv_maintain_apply_record_number)
    TextView tv_maintain_apply_record_number;

    @BindView(R.id.tv_maintain_apply_name)
    TextView tv_maintain_apply_name;

    @BindView(R.id.tv_maintain_apply_time)
    TextView tv_maintain_apply_time;

    @BindView(R.id.rl_maintain_apply_vehicle_name)
    RelativeLayout rl_maintain_apply_vehicle_name;

    @BindView(R.id.tv_maintain_apply_vehicle_name)
    TextView tv_maintain_apply_vehicle_name;

    @BindView(R.id.tv_maintain_apply_vehicle_name_tip)
    TextView tv_maintain_apply_vehicle_name_tip;

    @BindView(R.id.tv_maintain_apply_vehicle_license)
    TextView tv_maintain_apply_vehicle_license;

    @BindView(R.id.tv_maintain_apply_estimated_cost)
    TextView tv_maintain_apply_estimated_cost;

    @BindView(R.id.et_maintain_apply_cost_bearing)
    EditText et_maintain_apply_cost_bearing;

    @BindView(R.id.et_maintain_apply_vehicle_description)
    EditText et_maintain_apply_vehicle_description;

    @BindView(R.id.et_maintain_apply_remark)
    EditText et_maintain_apply_remark;

    @BindView(R.id.attach_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.btn_maintain_apply_save)
    Button btn_maintain_apply_save;

    @BindView(R.id.btn_maintain_apply_commit)
    Button btn_maintain_apply_commit;

    @OnClick({R.id.ll_toolbar_back, R.id.tv_toolbar_right_title, R.id.rl_maintain_apply_vehicle_name,
            R.id.btn_maintain_apply_save, R.id.btn_maintain_apply_commit})
    void doClick(View view) {
        switch (view.getId()) {
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.tv_toolbar_right_title:
                break;
            case R.id.rl_maintain_apply_vehicle_name:
                mPresenter.queryVehicleUpKeepList();
                break;
            case R.id.btn_maintain_apply_save:
                checkAddParamsAndCommit("0");
                break;
            case R.id.btn_maintain_apply_commit:
                checkAddParamsAndCommit("1");
                break;
        }
    }

    private Maintain mUpdateMaintain;
    private Integer mUpkeepId;
    private BottomPopupDialog mPopupDialog;
    private AttachAddRecyclerAdapter mAdapter;
    private List<Attach> mAttachList;
    public static final int REQUEST_OPEN_SYSTEM_FOLDER = 10002;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopupDialog != null && mPopupDialog.isShowing()){
            mPopupDialog.dismiss();
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_maintain_apply;
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
        mTvCenterTitle.setText("保养申请");
        mTvRightTitle.setVisibility(View.GONE);
        mTvRightTitle.setText("附件管理");
    }

    @Override
    protected void doBusiness() {
        tv_maintain_apply_name.setText(User.getInstance().getName());
        tv_maintain_apply_time.setText(DateUtils.getCurTimeFormat(DateUtils.FORMAT_3));
        mPresenter.getMaintainData(getIntent());
    }

    @Override
    public void initMaintainData(Maintain maintain) {
        mUpdateMaintain = maintain;
        mUpkeepId = maintain.getUpkeepId();

        tv_maintain_apply_record_number.setText(maintain.getRecordNumber());
        tv_maintain_apply_name.setText(maintain.getApplyPersonName());
        if (maintain.getApplyDate() != null){
            if (maintain.getApplyDate().contains(" ")){
                String[] split = maintain.getApplyDate().split(" ");
                tv_maintain_apply_time.setText(split[0]);
            }else{
                tv_maintain_apply_time.setText(maintain.getApplyDate());
            }
        }
        if (maintain.getVehicleName() != null) {
            tv_maintain_apply_vehicle_name.setText(maintain.getVehicleName());
            tv_maintain_apply_vehicle_name_tip.setText("");
        }
        tv_maintain_apply_vehicle_license.setText(maintain.getVehicleMark());
        if (maintain.getPlanUpkeepCost() != null){
            tv_maintain_apply_estimated_cost.setText(maintain.getPlanUpkeepCost().toString());
        }
        et_maintain_apply_cost_bearing.setText(maintain.getCostPayer());
        et_maintain_apply_vehicle_description.setText(maintain.getVehicleDescription());
        et_maintain_apply_remark.setText(maintain.getRemarks());
    }

    @Override
    public void showVehicleUpKeepDialog(List<VehicleUpkeep> list) {
        List<StringInfo> stringInfoList = mPresenter.transferUpKeepList(list);
        if (stringInfoList != null && stringInfoList.size() > 0) {
            ChooseDialog.Builder builder = new ChooseDialog.Builder(this, stringInfoList);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setOnDataChooseDialogListener(new ChooseDialog.Builder.OnChooseDialogListener() {

                @Override
                public String onChoose(List<StringInfo> list) {
                    for (StringInfo stringInfo : list) {
                        mUpkeepId = stringInfo.getId();
                        tv_maintain_apply_vehicle_name.setText(stringInfo.getRemark());
                        tv_maintain_apply_vehicle_name_tip.setText("");
                        tv_maintain_apply_vehicle_license.setText(stringInfo.getTitle());
                        if (stringInfo.getPlanUpkeepCost() != null){
                            tv_maintain_apply_estimated_cost.setText(stringInfo.getPlanUpkeepCost().toString());
                        }
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

    private void checkAddParamsAndCommit(String approvalStatus) {
        String applyTime = tv_maintain_apply_time.getText().toString().trim();
        String vehicleName = tv_maintain_apply_vehicle_license.getText().toString().trim();
        if (TextUtils.isEmpty(vehicleName)) {
            showShortToast("车辆名称不能为空!");
            return;
        }
        String vehicleLicense = tv_maintain_apply_vehicle_license.getText().toString().trim();
        if (TextUtils.isEmpty(vehicleLicense)) {
            showShortToast("车牌号不能为空!");
            return;
        }
        String estimatedCost = tv_maintain_apply_estimated_cost.getText().toString().trim();
        if (TextUtils.isEmpty(estimatedCost)) {
            showShortToast("本次预计费用不能为空!");
            return;
        }
        String costBearing = et_maintain_apply_cost_bearing.getText().toString().trim();
        String vehicleDescription = et_maintain_apply_vehicle_description.getText().toString().trim();
        String remark = et_maintain_apply_remark.getText().toString().trim();

        String dataStatus = mPresenter.getDataStatus();
        if (dataStatus != null) {
            if (VehicleConfig.APPLY_ADD.equals(dataStatus)) {
                Maintain maintain = new Maintain();
                if (mAttachList != null && mAttachList.size() > 0){
                    if (!TextUtils.isEmpty(mPresenter.getDataId())){
                        maintain.setUuid(mPresenter.getDataId());
                    }
                }
                maintain.setApplyPerson(User.getInstance().getPersonId());
                maintain.setApplyDate(applyTime);
                maintain.setUpkeepId(mUpkeepId);
                if (!TextUtils.isEmpty(estimatedCost)) {
                    maintain.setPlanUpkeepCost(new BigDecimal(estimatedCost));
                }
                if (!TextUtils.isEmpty(costBearing)) {
                    maintain.setCostPayer(costBearing);
                }
                if (!TextUtils.isEmpty(vehicleDescription)) {
                    maintain.setVehicleDescription(vehicleDescription);
                }
                if (!TextUtils.isEmpty(remark)) {
                    maintain.setRemarks(remark);
                }
                maintain.setApprovalStatus(approvalStatus);

                mPresenter.addMaintainApply(maintain);
            } else {
                if (mUpdateMaintain != null) {
                    mUpdateMaintain.setApplyPerson(User.getInstance().getPersonId());
                    mUpdateMaintain.setApplyDate(applyTime);
                    mUpdateMaintain.setApplyDate(applyTime);
                    mUpdateMaintain.setUpkeepId(mUpkeepId);
                    if (!TextUtils.isEmpty(estimatedCost)) {
                        mUpdateMaintain.setPlanUpkeepCost(new BigDecimal(estimatedCost));
                    }
                    if (!TextUtils.isEmpty(costBearing)) {
                        mUpdateMaintain.setCostPayer(costBearing);
                    }
                    if (!TextUtils.isEmpty(vehicleDescription)) {
                        mUpdateMaintain.setVehicleDescription(vehicleDescription);
                    }
                    if (!TextUtils.isEmpty(remark)) {
                        mUpdateMaintain.setRemarks(remark);
                    }
                    mUpdateMaintain.setApprovalStatus(approvalStatus);

                    mPresenter.updateMaintainApply(mUpdateMaintain);
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

    @NeedPermission({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void openCamera(){
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @NeedPermission({Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void openAlbum(){
        PictureSelectorConfig.initSingleConfig(this);
    }

    @NeedPermission({Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
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
