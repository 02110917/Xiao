package com.flying.xiao;

import com.flying.xiao.entity.XUserInfo;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BusinessDetail extends BaseActivity{

	private ImageView mHeadImage ; // 部门头像
	private TextView mName ; //部门名称
	private TextView mDescribe ; //描述
	private Button mAddfriend ; //添加关注
	private WebView mInfo ; //详细介绍
	private XUserInfo userInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.department_detail);
		initView();
	}
	private void initView(){
		mHeadImage=(ImageView)findViewById(R.id.department_detail_head_image);
		mName=(TextView)findViewById(R.id.department_name);
		mDescribe=(TextView)findViewById(R.id.department_describe);
		mAddfriend=(Button)findViewById(R.id.department_add_friend);
		mInfo=(WebView)findViewById(R.id.department_detail_webview);
	}
	private void initData(){
		userInfo=(XUserInfo) getIntent().getSerializableExtra("userinfo");
	}
}
