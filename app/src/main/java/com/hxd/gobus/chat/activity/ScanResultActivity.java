package com.hxd.gobus.chat.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.hxd.gobus.R;

/**
 * Created by ${chenyn} on 2017/8/16.
 */

public class ScanResultActivity extends JBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan_result);
        initTitle(true,true, "扫描结果", "", false,"");

        TextView tv_scanResult = (TextView) findViewById(R.id.tv_scanResult);

        String result = getIntent().getStringExtra("result");

        tv_scanResult.setText(result);
    }
}
