package example.hp.com.myclient.Tools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import example.hp.com.myclient.MainActivity;


public class TabAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragmentList;
	public TabAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		fragmentList=list;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO: 2015/10/21 如果加一个列表？

		return fragmentList.get(position);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return MainActivity.TITLES[position];
	}

	@Override
	public int getCount() {
		// TODO: 2015/10/20 可以加一个+号，自定义功能模块

		return MainActivity.TITLES.length;
	}

}
