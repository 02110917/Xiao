package com.flying.xiao;

import com.flying.xiao.common.UIHelper;
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

	private ImageView mBack ;
	private ImageView mHeadImage ; // ����ͷ��
	private TextView mName ; //��������
	private TextView mDescribe ; //����
	private Button mAddfriend ; //��ӹ�ע
	private WebView mInfo ; //��ϸ����
	private XUserInfo userInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.department_detail);
		initView();
		initData();
	}
	private void initView(){
		mBack = (ImageView) findViewById(R.id.content_detail_back);
		mHeadImage=(ImageView)findViewById(R.id.department_head_image);
		mName=(TextView)findViewById(R.id.department_name);
		mDescribe=(TextView)findViewById(R.id.department_describe);
		mAddfriend=(Button)findViewById(R.id.department_add_friend);
		mInfo=(WebView)findViewById(R.id.department_detail_webview);
		
		int index=getIntent().getIntExtra("index", 0);
		userInfo=appContext.listManager.getDepartmentList().get(index);
		String url=URLs.HOST+userInfo.getUserHeadImageUrl();
		ImageManager2.from(this).displayImage(mHeadImage, url, -1);
		mHeadImage.setTag(url);
		mName.setText(userInfo.getUserRealName());
		mDescribe.setText( userInfo.getUserGerenshuoming());
		String body = UIHelper.WEB_STYLE + userInfo.getUserInfoDetail()
				+ "<div style=\"margin-bottom: 80px\" />";
		body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
		body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
		//
		mInfo.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);
		mAddfriend.setOnClickListener(clickListener);
		mBack.setOnClickListener(UIHelper.finish(this));
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
