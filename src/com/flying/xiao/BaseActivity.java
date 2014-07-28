package com.flying.xiao;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.flying.xiao.app.AppContext;
import com.flying.xiao.app.AppManager;

/**
 * Ӧ�ó���Activity�Ļ���
 */
public class BaseActivity extends SwipeBackActivity {

	// �Ƿ�����ȫ��
	private boolean allowFullScreen = true;

	// �Ƿ���������
	private boolean allowDestroy = true;

	private View view;

	public Handler mHandler ;
	protected AppContext appContext ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		allowFullScreen = true;
		AppContext.baseActivity=this ;
		appContext=(AppContext) getApplication();
		// ���Activity����ջ
		AppManager.getAppManager().addActivity(this);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		AppContext.baseActivity=this ;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// ����Activity&�Ӷ�ջ���Ƴ�
		AppManager.getAppManager().finishActivity(this);
	}

	public boolean isAllowFullScreen() {
		return allowFullScreen;
	}

	/**
	 * �����Ƿ����ȫ��
	 * 
	 * @param allowFullScreen
	 */
	public void setAllowFullScreen(boolean allowFullScreen) {
		this.allowFullScreen = allowFullScreen;
	}

	public void setAllowDestroy(boolean allowDestroy) {
		this.allowDestroy = allowDestroy;
	}

	public void setAllowDestroy(boolean allowDestroy, View view) {
		this.allowDestroy = allowDestroy;
		this.view = view;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && view != null) {
			view.onKeyDown(keyCode, event);
			if (!allowDestroy) {
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.base_slide_right_out);
	}
}
