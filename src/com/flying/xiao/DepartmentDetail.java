package com.flying.xiao;

import com.flying.xiao.common.URLs;
import com.flying.xiao.entity.XUserInfo;
import com.flying.xiao.util.ImageManager2;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DepartmentDetail extends BaseActivity{

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
		initData();
	}
	private void initView(){
		mHeadImage=(ImageView)findViewById(R.id.department_detail_head_image);
		mName=(TextView)findViewById(R.id.department_name);
		mDescribe=(TextView)findViewById(R.id.department_describe);
		mAddfriend=(Button)findViewById(R.id.department_add_friend);
		mInfo=(WebView)findViewById(R.id.department_detail_webview);
		
		int index=getIntent().getIntExtra("index", 0);
		userInfo=appContext.listManager.getDepartmentList().get(index);
		String url=URLs.HOST+userInfo.getUserHeadImageUrl();
		mHeadImage.setTag(url);
		ImageManager2.from(this).displayImage(mHeadImage, url, -1);
		mName.setText(userInfo.getUserRealName());
		mDescribe.setText( userInfo.getUserGerenshuoming());
		mAddfriend.setOnClickListener(clickListener);
	}
	private void initData(){
		
	}
	
	/**
	 * add friend
	 */
	private OnClickListener clickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
		}
	};
}
