package com.flying.xiao.fragment;

import java.util.ArrayList;
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
import com.flying.xiao.common.UIHelper;
import com.flying.xiao.constant.Constant;
import com.flying.xiao.control.NetControl;
import com.flying.xiao.entity.XContent;
import com.flying.xiao.widget.PullDownListView;

@SuppressLint("ValidFragment")
public class MainContentFragment extends Fragment
{
	private static final String TAG = "MainNews";
	private PullDownListView mPullDownListview;
	private ListView mListView;
	private List<XContent> mContentList;
	private ListViewMainContentAdapter mAdapter;
	private Handler mHandler;
	private int mCurPage = 0;

	private int conType;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		mContentList = new ArrayList<XContent>();
		super.onCreate(savedInstanceState);
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
			mAdapter= new ListViewMainContentAdapter(getActivity(), mContentList, R.layout.main_fragment_market_listitem,true);
		else
			mAdapter = new ListViewMainContentAdapter(getActivity(), mContentList, R.layout.main_fragment_news_listitem);
		mListView.setAdapter(mAdapter);
		mHandler = new Handler()
		{

			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				switch (msg.what)
				{
				case Constant.HandlerMessageCode.MAIN_LOAD_DATA_ERROR:
					UIHelper.ToastMessage(getActivity(), R.string.main_fragment_load_data_error);
					break;

				case Constant.HandlerMessageCode.MAIN_LOAD_DATA_SUCCESS:

					mContentList.clear();
					mContentList.addAll((List<XContent>) msg.obj);
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
				UIHelper.showContentInfo(getActivity(),mContentList.get(position-1),conType);
			}
		});
	}
	public void setConType(int conType)
	{
		this.conType = conType;
	}
}
