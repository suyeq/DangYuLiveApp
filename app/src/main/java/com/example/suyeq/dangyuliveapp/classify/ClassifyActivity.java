package com.example.suyeq.dangyuliveapp.classify;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.suyeq.dangyuliveapp.R;

public class ClassifyActivity extends AppCompatActivity {
    private TabLayout tabLayout = null;

    private ViewPager viewPager;

    private Fragment[] mFragmentArrays = new Fragment[3];

    private String[] mTabTitles = new String[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        initView();
        setTarbar();
    }

    private void setTarbar() {
        Toolbar titlebar = (Toolbar) findViewById(R.id.titlebar);
        titlebar.setTitle("直播分类");
        //titlebar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(titlebar);
    }

    private void initView() {
        mTabTitles[0] = "游戏";
        mTabTitles[1] = "娱乐";
        mTabTitles[2] = "户外";
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //设置tablayout距离上下左右的距离
        //tab_title.setPadding(20,20,20,20);
        mFragmentArrays[0] = ClassifyFragment.newInstance();
        mFragmentArrays[1] = ClassifyFragment.newInstance();
        mFragmentArrays[2] = ClassifyFragment.newInstance();
        PagerAdapter pagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        //将ViewPager和TabLayout绑定
        tabLayout.setupWithViewPager(viewPager);
    }

    final class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentArrays[position];
        }


        @Override
        public int getCount() {
            return mFragmentArrays.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];

        }
    }
}
