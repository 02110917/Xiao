package com.flying.xiao;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
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
	private ImageView mBack; // 返回按钮
	private ImageView mRefresh;// 刷新按钮
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
	private XContent con;
	private AppContext appContext;
	private Handler mCommentHandler;
	private List<XImage>imageUrls;
	private List<View> images ;
	private XContentDetail conDetail ;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.market_detail);
		this.initView();
		this.initData();
		appContext = (AppContext) this.getApplication();
		
	}
	
	private void initView()
	{
		con = (XContent) getIntent().getSerializableExtra("content");
		imageUrls=con.getImages();
		images=new ArrayList<View>();
		if(imageUrls!=null&&imageUrls.size()>0){
			for(XImage image:imageUrls){
				String url=URLs.HOST+image.getImageUrl();
				ImageView imageView=new ImageView(this) ;
				imageView.setScaleType(ScaleType.FIT_XY);
				ImageManager2.from(this).displayImage(imageView, url, R.drawable.widget_dface);
				imageView.setTag(url);
				images.add(imageView);
			}
		}
		mBack = (ImageView) findViewById(R.id.content_detail_back);
		mBack.setOnClickListener(UIHelper.finish(this));
		mRefresh = (ImageView) findViewById(R.id.content_detail_refresh);
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
					break ;
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
			mRefresh.setVisibility(View.GONE);
			break;
		case DATA_LOAD_COMPLETE:
			mProgressbar.setVisibility(View.GONE);
			mRefresh.setVisibility(View.VISIBLE);
			break;
		case DATA_LOAD_FAIL:
			mProgressbar.setVisibility(View.GONE);
			mRefresh.setVisibility(View.VISIBLE);
			break;
		}
	}
	public class MypaperAdapter extends PagerAdapter{

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return images.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			// TODO Auto-generated method stub
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
}
