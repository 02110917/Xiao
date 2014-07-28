package com.flying.xiao;

import greendroid.widget.MyQuickAction;
import greendroid.widget.QuickActionGrid;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.flying.xiao.app.AppContext;
import com.flying.xiao.common.UIHelper;
import com.flying.xiao.constant.Constant;
import com.flying.xiao.fragment.MainContentFragment;
import com.flying.xiao.fragment.MainDiary;
import com.flying.xiao.fragment.MainMarket;

public class MainActivity extends BaseActivity
{
	public static final int QUICKACTION_LOGIN_OR_LOGOUT = 0;
	public static final int QUICKACTION_USERINFO = 1;
	public static final int QUICKACTION_SOFTWARE = 2;
	public static final int QUICKACTION_SEARCH = 3;
	public static final int QUICKACTION_SETTING = 4;
	public static final int QUICKACTION_EXIT = 5;
	
	private Button mBtnNews;
	private Button mBtnLost;
	private Button mBtnDiary;
	private Button mBtnMarket;
	private Button mBtnAsk;
	private ViewPager mVp;
	private int mCurIndex ; 
	private List<Fragment> mainFragmentList ;
	
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
	private void initFootBar() {

		fbSetting = (ImageView) findViewById(R.id.main_footbar_pub);
		fbSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 展示快捷栏&判断是否登录&是否加载文章图片
				UIHelper.showSettingLoginOrLogout(MainActivity.this,
						mGrid.getQuickAction(0));
				mGrid.show(v);
			}
		});
	}
	
	private void initMain(){
		mBtnNews=(Button)findViewById(R.id.frame_btn_main_news);
		mBtnLost=(Button)findViewById(R.id.frame_btn_main_lost);
		mBtnDiary=(Button)findViewById(R.id.frame_btn_main_diary);
		mBtnAsk=(Button)findViewById(R.id.frame_btn_main_ask);
		mBtnMarket=(Button)findViewById(R.id.frame_btn_main_market);
		mCurIndex=0;
		mBtnNews.setEnabled(false);
		mBtnNews.setOnClickListener(new BtnClickListener(0));
		mBtnLost.setOnClickListener(new BtnClickListener(1));
		mBtnDiary.setOnClickListener(new BtnClickListener(2));
		mBtnMarket.setOnClickListener(new BtnClickListener(3));
		mBtnAsk.setOnClickListener(new BtnClickListener(4));
		mBtnNews.setTag(0);
		mBtnLost.setTag(1);
		mBtnDiary.setTag(2);
		mBtnMarket.setTag(3);
		mBtnAsk.setTag(4);
		mVp=(ViewPager)findViewById(R.id.viewpager);
		mainFragmentList=new ArrayList<Fragment>();
		Fragment mainNews=new MainContentFragment() ;
		((MainContentFragment) mainNews).setConType(Constant.ContentType.CONTENT_TYPE_NEWS);
		Fragment mainLost=new MainContentFragment() ;
		((MainContentFragment) mainLost).setConType(Constant.ContentType.CONTENT_TYPE_LOST);
		Fragment mainDiary=new MainDiary() ;
		Fragment mainAsk=new MainContentFragment() ;
		((MainContentFragment) mainAsk).setConType(Constant.ContentType.CONTENT_TYPE_ASK);
		Fragment mainMarket=new MainMarket() ;
		mainFragmentList.add(mainNews);
		mainFragmentList.add(mainLost);
		mainFragmentList.add(mainDiary);
		mainFragmentList.add(mainMarket);
		mainFragmentList.add(mainAsk);
		MyFragmentPaperAdapter adapter=new MyFragmentPaperAdapter(getSupportFragmentManager());
		mVp.setAdapter(adapter);
		mVp.setCurrentItem(mCurIndex);
		mVp.setOnPageChangeListener(new OnPageChangeListener()
		{
			
			@Override
			public void onPageSelected(int arg0)
			{
				mCurIndex=arg0;
				setBtnEnabled(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0)
			{
			}
		});
	}
	private class BtnClickListener implements OnClickListener{
		int index=0 ;
		public BtnClickListener(int i){
			index=i;
		}
		@Override
		public void onClick(View v)
		{
			setBtnEnabled((Integer)v.getTag());
			mVp.setCurrentItem(index);
		}
		
	}
	private void setBtnEnabled(int tag){
		if(tag==0){
			mBtnNews.setEnabled(false);
		}else{
			mBtnNews.setEnabled(true);
		}
		if(tag==1){
			mBtnLost.setEnabled(false);
		}else{
			mBtnLost.setEnabled(true);
		}
		if(tag==2){
			mBtnDiary.setEnabled(false);
		}else{
			mBtnDiary.setEnabled(true);
		}
		if(tag==3){
			mBtnMarket.setEnabled(false);
		}else{
			mBtnMarket.setEnabled(true);
		}
		if(tag==4){
			mBtnAsk.setEnabled(false);
		}else{
			mBtnAsk.setEnabled(true);
		}
	}
	private class MyFragmentPaperAdapter extends FragmentPagerAdapter{

		public MyFragmentPaperAdapter(FragmentManager fm)
		{
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0)
		{
			// TODO Auto-generated method stub
			return mainFragmentList.get(arg0);
		}

		@Override
		public int getCount()
		{
			return mainFragmentList.size();
		}
		
	}
	
	/**
	 * 初始化快捷栏
	 */
	private void initQuickActionGrid() {
		mGrid = new QuickActionGrid(this);
		mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_login,
				R.string.main_menu_login));
		mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_myinfo,
				R.string.main_menu_myinfo));
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.ic_menu_software, R.string.main_menu_software));
		mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_search,
				R.string.main_menu_search));
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.ic_menu_setting, R.string.main_menu_setting));
		mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_exit,
				R.string.main_menu_exit));

		mGrid.setOnQuickActionClickListener(mActionListener);
	}
	/**
	 * 快捷栏item点击事件
	 */
	private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
		@Override
		public void onQuickActionClicked(QuickActionWidget widget, int position) {
			switch (position) {
			case QUICKACTION_LOGIN_OR_LOGOUT:// 用户登录-注销
				UIHelper.loginOrLogout(MainActivity.this);
				break;
//			case QUICKACTION_USERINFO:// 我的资料
//				UIHelper.showUserInfo(MainActivity.this);
//				break;
//			case QUICKACTION_SOFTWARE:// 开源软件
//				UIHelper.showSoftware(MainActivity.this);
//				break;
//			case QUICKACTION_SEARCH:// 搜索
//				UIHelper.showSearch(MainActivity.this);
//				break;
//			case QUICKACTION_SETTING:// 设置
//				UIHelper.showSetting(MainActivity.this);
//				break;
//			case QUICKACTION_EXIT:// 退出
//				UIHelper.Exit(MainActivity.this);
//				break;
			}
		}
	};
}
