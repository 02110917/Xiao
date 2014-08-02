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
	private Fragment communityFragment; // ����
	private Fragment meFragment;
	private RadioGroup mRadioGroup;
	public List<Fragment> fragments = new ArrayList<Fragment>();
	private ImageView fbSetting;

	private QuickActionWidget mGrid;// ������ؼ�

	private AppContext appContext;// ȫ��Context

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		appContext = (AppContext) getApplication();
		// ���������ж�
		if (!appContext.isNetworkConnected())
			UIHelper.ToastMessage(this, R.string.network_not_connected);
		initFootBar();
		initQuickActionGrid();
		initMain();
		appContext.initLoginInfo();
	}

	/**
	 * ��ʼ���ײ���
	 */
	private void initFootBar()
	{

		fbSetting = (ImageView) findViewById(R.id.main_footbar_pub);
		fbSetting.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// չʾ�����&�ж��Ƿ��¼&�Ƿ��������ͼƬ
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
	 * ��ʼ�������
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
	 * �����item����¼�
	 */
	private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener()
	{
		@Override
		public void onQuickActionClicked(QuickActionWidget widget, int position)
		{
			switch (position)
			{
			case QUICKACTION_LOGIN_OR_LOGOUT:// �û���¼-ע��
				UIHelper.loginOrLogout(MainActivity.this);
				break;
			// case QUICKACTION_USERINFO:// �ҵ�����
			// UIHelper.showUserInfo(MainActivity.this);
			// break;
			// case QUICKACTION_SOFTWARE:// ��Դ���
			// UIHelper.showSoftware(MainActivity.this);
			// break;
			// case QUICKACTION_SEARCH:// ����
			// UIHelper.showSearch(MainActivity.this);
			// break;
			// case QUICKACTION_SETTING:// ����
			// UIHelper.showSetting(MainActivity.this);
			// break;
			// case QUICKACTION_EXIT:// �˳�
			// UIHelper.Exit(MainActivity.this);
			// break;
			}
		}
	};

	/**
	 * ��������--�Ƿ��˳�����
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		boolean flag = true;
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			// �Ƿ��˳�Ӧ��
			UIHelper.Exit(this);
		} else
		{
			flag = super.onKeyDown(keyCode, event);
		}
		return flag;
	}

}
