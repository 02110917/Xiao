package com.flying.xiao.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.flying.xiao.R;
import com.flying.xiao.adapter.ListViewMainContentAdapter;
import com.flying.xiao.app.AppContext;
import com.flying.xiao.common.UIHelper;
import com.flying.xiao.constant.Constant;
import com.flying.xiao.control.NetControl;
import com.flying.xiao.entity.XContent;
import com.flying.xiao.widget.PullDownListView;

@SuppressLint("ValidFragment")
public class MainContentFragment extends Fragment implements PullDownListView.OnRefreshListioner
{
	private static final String TAG = "MainNews";
	private PullDownListView mPullDownListview;
	private ListView mListView;
	private ListViewMainContentAdapter mAdapter;
	private Handler mHandler;
	private AppContext appContext ;
	private int mCurPage = 0;
	private int conType;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		appContext=(AppContext) getActivity().getApplication();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		System.out.println(conType + "----onCreateView-----------");
		View view = inflater.inflate(R.layout.main_fragment_news, null);
		initView(view);
		return view;
	}

	private void initView(View v)
	{
		mPullDownListview = (PullDownListView) v.findViewById(R.id.main_fragment_list_view_news);
		mListView = mPullDownListview.mListView;
		
		if(conType==Constant.ContentType.CONTENT_TYPE_MARKET)
			mAdapter= new ListViewMainContentAdapter(getActivity(), appContext.contentManager.getContentListByType(conType), R.layout.main_fragment_market_listitem,true);
		else
			mAdapter = new ListViewMainContentAdapter(getActivity(), appContext.contentManager.getContentListByType(conType), R.layout.main_fragment_news_listitem);
		mListView.setAdapter(mAdapter);
		mPullDownListview.setRefreshListioner(this);
		mHandler = new Handler()
		{

			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				switch (msg.what)
				{
				case Constant.HandlerMessageCode.MAIN_LOAD_DATA_ERROR:
					mPullDownListview.onRefreshComplete();
					mPullDownListview.onLoadMoreComplete();
					UIHelper.ToastMessage(getActivity(), R.string.main_fragment_load_data_error);
					break;

				case Constant.HandlerMessageCode.MAIN_LOAD_DATA_SUCCESS:
					mPullDownListview.onRefreshComplete();
					mPullDownListview.onLoadMoreComplete();
					if(mCurPage==0) //如果是刷新获得重新加载  则清楚之前的数据
						appContext.contentManager.getContentListByType(conType).clear();
					List<XContent> list=(List<XContent>) msg.obj;
					if(list.size()==Constant.MAX_PAGE_COUNT)
						mPullDownListview.setMore(true);
					else if(list.size()<Constant.MAX_PAGE_COUNT)
						mPullDownListview.setMore(false);
					appContext.contentManager.getContentListByType(conType).addAll(list);
					mAdapter.notifyDataSetChanged();

					break;
				default:
					break;
				}
			}
		};
		NetControl.getShare(getActivity()).getContentData(conType, 0, mHandler);
		mListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				UIHelper.showContentInfo(getActivity(),position-1,conType);
			}
		});
	}
	public void setConType(int conType)
	{
		this.conType = conType;
	}

	@Override
	public void onRefresh()
	{
		mCurPage=0;
		NetControl.getShare(getActivity()).getContentData(conType, 0, mHandler);
	}

	@Override
	public void onLoadMore()
	{
		mCurPage++;
		NetControl.getShare(getActivity()).getContentData(conType, mCurPage, mHandler);
	}
}
