package com.flying.xiao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ViewSwitcher;

import com.flying.xiao.app.AppContext;
import com.flying.xiao.app.AppException;
import com.flying.xiao.common.StringUtils;
import com.flying.xiao.common.UIHelper;
import com.flying.xiao.constant.Constant;
import com.flying.xiao.control.NetControl;
import com.flying.xiao.entity.XUserInfo;
import com.flying.xiao.http.HttpUtil;

/**
 * 用户登录对话框
 */
public class LoginDialog extends BaseActivity
{
	private ViewSwitcher mViewSwitcher;
	private ImageButton btn_close;
	private Button btn_login;
	private AutoCompleteTextView mAccount;
	private EditText mPwd;
	private AnimationDrawable loadingAnimation;
	private View loginLoading;
	private CheckBox chb_rememberMe;
	private int curLoginType;
	private InputMethodManager imm;

	public final static int LOGIN_OTHER = 0x00;
	public final static int LOGIN_MAIN = 0x01;
	public final static int LOGIN_SETTING = 0x02;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_dialog);

		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		curLoginType = getIntent().getIntExtra("LOGINTYPE", LOGIN_OTHER);

		mViewSwitcher = (ViewSwitcher) findViewById(R.id.logindialog_view_switcher);
		loginLoading = findViewById(R.id.login_loading);
		mAccount = (AutoCompleteTextView) findViewById(R.id.login_account);
		mPwd = (EditText) findViewById(R.id.login_password);
		chb_rememberMe = (CheckBox) findViewById(R.id.login_checkbox_rememberMe);
		initUserInput();
		btn_close = (ImageButton) findViewById(R.id.login_close_button);
		btn_close.setOnClickListener(UIHelper.finish(this));
		btn_login = (Button) findViewById(R.id.login_btn_login);
		btn_login.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// 隐藏软键盘
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

				String account = mAccount.getText().toString();
				String pwd = mPwd.getText().toString();
				boolean isRememberMe = chb_rememberMe.isChecked();
				// 判断输入
				if (StringUtils.isEmpty(account))
				{
					UIHelper.ToastMessage(v.getContext(), getString(R.string.msg_login_email_null));
					return;
				}
				if (StringUtils.isEmpty(pwd))
				{
					UIHelper.ToastMessage(v.getContext(), getString(R.string.msg_login_pwd_null));
					return;
				}

				btn_close.setVisibility(View.GONE);
				loadingAnimation = (AnimationDrawable) loginLoading.getBackground();
				loadingAnimation.start();
				mViewSwitcher.showNext();

				login(account, pwd, isRememberMe);
			}
		});

	}

	private void initUserInput()
	{
		SharedPreferences share = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		if (share != null)
		{
			String user = share.getString("user_name", "");
			String psd = share.getString("user_psd", "");
			mAccount.setText(user);
			mPwd.setText(psd);
		}

	}

	// 登录验证
	private void login(String account, String pwd, final boolean isRememberMe)
	{
		NetControl.getShare(this).login(account, pwd);
		mHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what == Constant.HandlerMessageCode.LOGIN_SUCCESS)
				{
					XUserInfo user = (XUserInfo) msg.obj;
					if (user != null)
					{
						// 清空原先cookie
						HttpUtil.cleanCookie();
						// 提示登陆成功
						UIHelper.ToastMessage(LoginDialog.this, R.string.msg_login_success);
						if (curLoginType == LOGIN_MAIN)
						{
							// 跳转--加载用户动态
							Intent intent = new Intent(LoginDialog.this, MainActivity.class);
							intent.putExtra("LOGIN", true);
							startActivity(intent);
							appContext.initLoginInfo();
							
						}
						// else if(curLoginType == LOGIN_SETTING){
						// //跳转--用户设置页面
						// Intent intent = new Intent(LoginDialog.this,
						// Setting.class);
						// intent.putExtra("LOGIN", true);
						// startActivity(intent);
						// }
						finish();
					}
				} else if (msg.what == Constant.HandlerMessageCode.LOGIN_FAILD)
				{
					mViewSwitcher.showPrevious();
					btn_close.setVisibility(View.VISIBLE);
					UIHelper.ToastMessage(LoginDialog.this, getString(R.string.msg_login_fail));
				} else if (msg.what == Constant.HandlerMessageCode.NET_THROW_EXCEPTION)
				{
					mViewSwitcher.showPrevious();
					btn_close.setVisibility(View.VISIBLE);
					((AppException) msg.obj).makeToast(LoginDialog.this);
				}
			}
		};
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			this.onDestroy();
		}
		return super.onKeyDown(keyCode, event);
	}
}
