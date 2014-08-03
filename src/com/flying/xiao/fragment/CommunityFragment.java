package com.flying.xiao.fragment;

import com.flying.xiao.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * ÉçÇø
 * @author zhangmin
 *
 */
public class CommunityFragment extends Fragment
{
	private ViewPager mViewPaper;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v=inflater.inflate(R.layout.frame_community, null);
		
		return v;
	}

	

}
