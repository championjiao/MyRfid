package com.jxb.myrfid.series.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MyFragmentManagerAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> fragmentList;
	
	public MyFragmentManagerAdapter(android.support.v4.app.FragmentManager fm) {
		super(fm);
		
	}
	
	public MyFragmentManagerAdapter(android.support.v4.app.FragmentManager fm,ArrayList<Fragment> fragmentList){
		super(fm);
		this.fragmentList = fragmentList;
	}
	@Override
	public Fragment getItem(int index) {
		return fragmentList.get(index);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

	@Override
	public int getItemPosition(Object object){
		return super.getItemPosition(object);
	}
}
