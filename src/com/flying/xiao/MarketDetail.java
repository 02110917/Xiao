package com.flying.xiao;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flying.xiao.app.AppContext;
import com.flying.xiao.common.StringUtils;
import com.flying.xiao.common.UIHelper;
import com.flying.xiao.common.URLs;
import com.flying.xiao.constant.Constant;
import com.flying.xiao.control.NetControl;
import com.flying.xiao.entity.XContent;
import com.flying.xiao.entity.XContentDetail;
import com.flying.xiao.entity.XImage;
import com.flying.xiao.entity.XMarketDetail;
import com.flying.xiao.util.ImageManager2;


/**
 * market详情
 * @author zhangmin
 *
 */
public class MarketDetail extends BaseActivity{
	
	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	private final static int DATA_LOAD_FAIL = 0x003;
	
	/**
	 * view
	 */
	private TextView mHeadText ;
	private ImageView mBack; // 返回按钮
	private ImageView mFavorite;// 收藏按钮
	private ProgressBar mProgressbar; // 进度条
	private TextView mTitle;
	private TextView mPubTime;
	private TextView mScanTimes ;// 浏览次数
	private TextView mPrice ;
	private TextView mDetailInfo;
	private ProgressBar progressBar;
	private ViewPager mImagePage ;
	/**
	 * footer
	 */
	private TextView mPubname ;  //联系人
	private TextView mPhone ;  //电话号码
	private TextView mCallPhone ; //电话
	private TextView mSendMsg ; //短信
	private TextView mWriteWords ; //留言
	private TextView indexView;
	private XContent con;
	private Handler mCommentHandler;
	private List<XImage>imageUrls;
	private List<View> images ;
	private XContentDetail conDetail ;
	private Resources resources ;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.market_detail);
		resources=this.getResources();
		this.initView();
		this.initData();
		
	}
	
	private void initView()
	{
		int index=getIntent().getIntExtra("content", 0);
		con = appContext.listManager.getMarketContentList().get(index);
		imageUrls=con.getImages();
		images=new ArrayList<View>();
		if(imageUrls!=null&&imageUrls.size()>0){
			for(XImage image:imageUrls){
				String url=URLs.HOST+image.getImageUrl();
				ImageView imageView=new ImageView(this) ;
//				imageView.setScaleType(ScaleType.FIT_XY);
				ImageManager2.from(this).displayImage(imageView, url, -1);
				imageView.setTag(url);
				images.add(imageView);
			}
		}
		mHeadText=(TextView)findViewById(R.id.content_detail_head_title);
		mHeadText.setText("详情");
		indexView=(TextView)findViewById(R.id.showpagerindex);
		indexView.setText("1/"+images.size());
		mBack = (ImageView) findViewById(R.id.content_detail_back);
		mBack.setOnClickListener(UIHelper.finish(this));
		mFavorite = (ImageView) findViewById(R.id.content_detail_refresh);
		if(con.isMeCollecte())
			mFavorite.setImageDrawable(resources.getDrawable(R.drawable.head_favorite_y));
		else
			mFavorite.setImageDrawable(resources.getDrawable(R.drawable.head_favorite));
		mFavorite.setOnClickListener(collectClickListener);
		mProgressbar = (ProgressBar) findViewById(R.id.content_detail_head_progress);
		mTitle=(TextView)findViewById(R.id.market_detail_title);
		mPubTime=(TextView)findViewById(R.id.market_detail_pub_time);
		mScanTimes=(TextView)findViewById(R.id.market_detail_scantimes);
		mPrice=(TextView)findViewById(R.id.market_detail_price);
		mDetailInfo=(TextView)findViewById(R.id.market_detail_info);
		mImagePage=(ViewPager)findViewById(R.id.market_detail_image_page);
		mPhone=(TextView)findViewById(R.id.market_detail_footer_phone);
		/**
		 * 底部按钮
		 */
		mPubname=(TextView)findViewById(R.id.market_detail_footer_pub_name);
		mCallPhone=(TextView)findViewById(R.id.market_detail_footer_btn_call);
		mSendMsg=(TextView)findViewById(R.id.market_detail_footer_btn_msg);
		mWriteWords=(TextView)findViewById(R.id.market_detail_footer_phone);
		mCallPhone.setOnClickListener(listener);
		mSendMsg.setOnClickListener(listener);
		mWriteWords.setOnClickListener(listener);
		progressBar = (ProgressBar)findViewById(R.id.listview_foot_progress);
		mTitle.setText(con.getConTitle());
		mPubTime.setText(StringUtils.friendly_time(con.getConPubTime().toString()));
		mScanTimes.setText(con.getConHot()+"");
		mPrice.setText(con.getPrice()+"元");
		mPubname.setText(con.getUserRealNama());
		MypaperAdapter adapter=new MypaperAdapter();
		mImagePage.setAdapter(adapter);
		mImagePage.setOnPageChangeListener(pageChangeListener);
	}
	
	private void initData()
	{
		NetControl.getShare(MarketDetail.this).getContentDetail(Constant.ContentType.CONTENT_TYPE_MARKET, con.getId());
		headButtonSwitch(DATA_LOAD_ING);
		
		mHandler=new Handler(){

			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				switch (msg.what)
				{
				case Constant.HandlerMessageCode.CONTENT_DETAIL_LOAD_DATA_ERROR:
					headButtonSwitch(DATA_LOAD_FAIL);
					UIHelper.ToastMessage(MarketDetail.this, R.string.msg_load_is_null);
					break;
				case Constant.HandlerMessageCode.CONTENT_DETAIL_LOAD_DATA_SUCCESS:
					XMarketDetail xmarketDetail = (XMarketDetail) msg.obj;
					headButtonSwitch(DATA_LOAD_COMPLETE);
					mDetailInfo.setText(xmarketDetail.getEsMiaoshu());
					mPhone.setText(xmarketDetail.getEsPhone());
					mPubname.setText(xmarketDetail.getEsName());
					break ;
				case Constant.HandlerMessageCode.COLLECTION_OPERATE_FAIL:
					UIHelper.ToastMessage(MarketDetail.this, "操作失败...");
					break ;
				case Constant.HandlerMessageCode.COLLECTION_OPERATE_SUCCESS:
					con.setMeCollecte(!con.isMeCollecte());
					if(con.isMeCollecte())
						mFavorite.setImageDrawable(resources.getDrawable(R.drawable.head_favorite_y));
					else
						mFavorite.setImageDrawable(resources.getDrawable(R.drawable.head_favorite));
					break ;
				case Constant.HandlerMessageCode.USER_NOT_LOGIN:
					UIHelper.ToastMessage(MarketDetail.this, R.string.user_login_out_of_date);
					UIHelper.showLoginDialog(MarketDetail.this);
					break;
				default:
					break;
				}
			}};
	}
	
	private OnClickListener listener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			
		}
	};
	/**
	 * 头部按钮展示
	 * 
	 * @param type
	 */
	private void headButtonSwitch(int type)
	{
		switch (type)
		{
		case DATA_LOAD_ING:
			mProgressbar.setVisibility(View.VISIBLE);
			mFavorite.setVisibility(View.GONE);
			break;
		case DATA_LOAD_COMPLETE:
			mProgressbar.setVisibility(View.GONE);
			mFavorite.setVisibility(View.VISIBLE);
			break;
		case DATA_LOAD_FAIL:
			mProgressbar.setVisibility(View.GONE);
			mFavorite.setVisibility(View.VISIBLE);
			break;
		}
	}
	public class MypaperAdapter extends PagerAdapter{

		@Override
		public int getCount()
		{
			return images.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			 container.removeView(images.get(position)); 
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			 container.addView(images.get(position));  
			return images.get(position);  
		}
		
		
	}
	/**
	 * 收藏操作 按钮监听
	 */
	private View.OnClickListener collectClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			NetControl.getShare(MarketDetail.this).collectOperate(con.getId(), con.isMeCollecte());
//			NetControl.getShare(MarketDetail.this).getContentDetail(Constant.ContentType.CONTENT_TYPE_MARKET, con.getId());
//			headButtonSwitch(DATA_LOAD_ING);
		}
	};
	private OnPageChangeListener pageChangeListener=new OnPageChangeListener()
	{
		
		@Override
		public void onPageSelected(int arg0)
		{
			indexView.setText((arg0+1)+"/"+images.size());
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0)
		{
		}
	};
}
