package com.flying.xiao;

import greendroid.widget.MyQuickAction;
import greendroid.widget.QuickActionGrid;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.flying.xiao.adapter.FragmentTabAdapter;
import com.flying.xiao.app.AppContext;
import com.flying.xiao.common.UIHelper;
import com.flying.xiao.fragment.CommunityFragment;
import com.flying.xiao.fragment.LifeFragment;
import com.flying.xiao.fragment.MainFragment;
import com.flying.xiao.fragment.MeFragment;

public class MainActivity extends BaseActivity
{
	public static final int QUICKACTION_LOGIN_OR_LOGOUT = 0;
	public static final int QUICKACTION_USERINFO = 1;
	public static final int QUICKACTION_SOFTWARE = 2;
	public static final int QUICKACTION_SEARCH = 3;
	public static final int QUICKACTION_SETTING = 4;
	public static final int QUICKACTION_EXIT = 5;

	private Fragment mainFragment;
	private Fragment lifeFragment;
	private Fragment communityFragment; // 社区
	private Fragment meFragment;
	private RadioGroup mRadioGroup;
	public List<Fragment> fragments = new ArrayList<Fragment>();
	private ImageView fbSetting;

	private QuickActionWidget mGrid;// 快捷栏控件

	private AppContext appContext;// 全局Context

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		appContext = (AppContext) getApplication();
		// 网络连接判断
		if (!appContext.isNetworkConnected())
			UIHelper.ToastMessage(this, R.string.network_not_connected);
		initFootBar();
		initQuickActionGrid();
		initMain();
		appContext.initLoginInfo();
	}

	/**
	 * 初始化底部栏
	 */
	private void initFootBar()
	{

		fbSetting = (ImageView) findViewById(R.id.main_footbar_pub);
		fbSetting.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// 展示快捷栏&判断是否登录&是否加载文章图片
				UIHelper.showSettingLoginOrLogout(MainActivity.this, mGrid.getQuickAction(0));
				mGrid.show(v);
			}
		});
	}

	private void initMain()
	{
		mRadioGroup = (RadioGroup) findViewById(R.id.footbargroup);
		mainFragment = new MainFragment();
		lifeFragment = new LifeFragment();
		communityFragment = new CommunityFragment();
		meFragment = new MeFragment();
		fragments.add(mainFragment);
		fragments.add(lifeFragment);
		fragments.add(communityFragment);
		fragments.add(meFragment);
		FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments, R.id.main_scrolllayout,
				mRadioGroup);
	}

	/**
	 * 初始化快捷栏
	 */
	private void initQuickActionGrid()
	{
		mGrid = new QuickActionGrid(this);
		mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_login, R.string.main_menu_login));
		mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_myinfo, R.string.main_menu_myinfo));
		mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_software, R.string.main_menu_software));
		mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_search, R.string.main_menu_search));
		mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_setting, R.string.main_menu_setting));
		mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_exit, R.string.main_menu_exit));

		mGrid.setOnQuickActionClickListener(mActionListener);
	}

	/**
	 * 快捷栏item点击事件
	 */
	private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener()
	{
		@Override
		public void onQuickActionClicked(QuickActionWidget widget, int position)
		{
			switch (position)
			{
			case QUICKACTION_LOGIN_OR_LOGOUT:// 用户登录-注销
				UIHelper.loginOrLogout(MainActivity.this);
				break;
			// case QUICKACTION_USERINFO:// 我的资料
			// UIHelper.showUserInfo(MainActivity.this);
			// break;
			// case QUICKACTION_SOFTWARE:// 开源软件
			// UIHelper.showSoftware(MainActivity.this);
			// break;
			// case QUICKACTION_SEARCH:// 搜索
			// UIHelper.showSearch(MainActivity.this);
			// break;
			// case QUICKACTION_SETTING:// 设置
			// UIHelper.showSetting(MainActivity.this);
			// break;
			// case QUICKACTION_EXIT:// 退出
			// UIHelper.Exit(MainActivity.this);
			// break;
			}
		}
	};

	/**
	 * 监听返回--是否退出程序
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		boolean flag = true;
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			// 是否退出应用
			UIHelper.Exit(this);
		} else
		{
			flag = super.onKeyDown(keyCode, event);
		}
		return flag;
	}

}
