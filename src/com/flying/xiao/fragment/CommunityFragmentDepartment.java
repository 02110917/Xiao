package com.flying.xiao.fragment;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.flying.xiao.R;
import com.flying.xiao.adapter.ListViewCommunityAdapter;
import com.flying.xiao.common.UIHelper;
import com.flying.xiao.constant.Constant;
import com.flying.xiao.control.NetControl;
import com.flying.xiao.entity.XUserInfo;
import com.flying.xiao.manager.ListManager;
import com.flying.xiao.widget.PullDownListView;
/**
 * 社区 
 * @author zhangmin
 *
 */
public class CommunityFragmentDepartment extends Fragment
{
	private PullDownListView mPullDownListView ;
	private ListView mListView ;
	private ListViewCommunityAdapter adapter ;
	private List<XUserInfo> userInfolist;
	private ListManager manager;
	private int page=0;
	private Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
			case Constant.HandlerMessageCode.GET_USERINFOS_DEPARTMENT_SUCCESS:
				userInfolist.clear();
				List<XUserInfo> xusrs=(List<XUserInfo>) msg.obj;
				userInfolist.addAll(xusrs);
				adapter.notifyDataSetChanged();
				break;
			case Constant.HandlerMessageCode.GET_USERINFOS_FAIL:
				UIHelper.ToastMessage(getActivity(), "获取数据出错");
				break;
			default:
				break;
			}
		}};
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		manager=ListManager.getContentMangerShare();
		userInfolist=manager.getDepartmentList();
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v=inflater.inflate(R.layout.community_fragment, null);
		initView(v);
		initData();
		return v;
	}

	private void initView(View v){
		mPullDownListView=(PullDownListView)v.findViewById(R.id.fragment_list_view);
		mListView=mPullDownListView.mListView;
		adapter=new ListViewCommunityAdapter(getActivity(), userInfolist, R.layout.community_fragment_listitem);
		mListView.setAdapter(adapter);
	}

	private void initData(){
		
			NetControl.getShare(getActivity()).getUserInfos(Constant.UserType.User_TYPE_DEPARTMENT, page, mHandler);
	}

	

}
