package com.hxd.gobus.mvp.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.mvp.adapter.SearchAddressAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: wangqingbin
 * @date: 2019/9/25 15:05
 */

public class SearchLocationActivity extends BaseActivity implements TextWatcher,
        SearchAddressAdapter.OnItemClickListener,PoiSearch.OnPoiSearchListener{

    @BindView(R.id.iv_search_back)
    ImageView iv_search_back;

    @BindView(R.id.et_search_search)
    EditText et_search_search;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @OnClick({R.id.iv_search_back})
    void doClick(View view){
        switch (view.getId()){
            case R.id.iv_search_back:
                defaultFinish();
                break;
        }
    }

    private List<PoiItem> mList;
    private SearchAddressAdapter mSearchAddressAdapter;
    private PoiSearch mPoiSearch;
    private PoiSearch.Query mQuery;
    private AMapLocation mLocation;

    @Override
    protected int bindLayout() {
        return R.layout.activity_search_location;
    }

    @Override
    protected void initView() {
        et_search_search.addTextChangedListener(this);
    }

    @Override
    protected void doBusiness() {
        if (mLocation == null){
            mLocation = getIntent().getParcelableExtra("locationInfo");
        }
        mList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSearchAddressAdapter = new SearchAddressAdapter(this, mList);
        mRecyclerView.setAdapter(mSearchAddressAdapter);
        mSearchAddressAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mPoiSearch) {
            mPoiSearch = null;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (null != editable) {
            if (0 == editable.length()) {//没有输入则清空搜索记录
                mList.clear();
                mSearchAddressAdapter.setList(mList, "");
            } else {
                if (null != mLocation) {
                    doSearchQuery(editable.toString(), mLocation.getCity(), new LatLonPoint(mLocation.getLatitude(), mLocation.getLongitude()));
                } else {
                    doSearchQuery(editable.toString(), "", null);
                }
                doSearchQuery(editable.toString(), "", null);
            }
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String keyWord, String city, LatLonPoint lpTemp) {
        mQuery = new PoiSearch.Query(keyWord, city, "");//第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        mQuery.setPageSize(20);// 设置每页最多返回多少条poiitem
        mQuery.setPageNum(0);// 设置查第一页

        mPoiSearch = new PoiSearch(this, mQuery);
        mPoiSearch.setOnPoiSearchListener(this);
        if (lpTemp != null) {
            mPoiSearch.setBound(new PoiSearch.SearchBound(lpTemp, 10000, true));//该范围的中心点-----半径，单位：米-----是否按照距离排序
        }
        mPoiSearch.searchPOIAsyn();// 异步搜索
    }

    @Override
    public void onItemClick(int position) {
        PoiItem poiItem = mList.get(position);
        if (null != poiItem) {//获取信息并回传上一页面
            Intent intent = new Intent();
            intent.putExtra("searchInfo", poiItem);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onPoiSearched(PoiResult result, int i) {
        if (i == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(mQuery)) {// 是否是同一条
                    if (null != mList) {
                        mList.clear();
                    }
                    List<PoiItem> poiItems = result.getPois();
                    mList.addAll(poiItems);
                    if (null != mSearchAddressAdapter) {
                        mSearchAddressAdapter.setList(mList, et_search_search.getText().toString().trim());
                        mRecyclerView.smoothScrollToPosition(0);
                    }
                }
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
