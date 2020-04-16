package com.example.a15632.poetrydemo;


import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import java.util.List;

public class CommunityFragment extends Fragment{
    //选项卡中的按钮
    private Button btn_addpoem=null;
    private Button btn_addtalk=null;

    private View cursor;
    private int offset = 0;
    private  int screenWidth = 0;
    private int screenl_3;
    private LinearLayout.LayoutParams lp;


    private  ViewPager mViewPager;
    private View fragment;
    FragmentManager fragmentManager;
    ViewPagerFragmentAdapter mViewPagerFragmentAdapter;

    List<Fragment> mFragmentList = new ArrayList<Fragment>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragment=inflater.inflate(R.layout.tab_community_layout,container,false);
        //code begin
        findViews();
        MyListener myListener=new MyListener();
        btn_addpoem.setOnClickListener(myListener);
        btn_addtalk.setOnClickListener(myListener);

        //页面切换
        fragmentManager =getChildFragmentManager();//定义fragment管理器
        initFragmetList();//初始化fragment的列表
        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(fragmentManager,mFragmentList);//设置viewpager的适配器
        initViewPager();//初始化viewpager        //code end
        ViewGroup p=(ViewGroup)fragment.getParent();

        //获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenl_3 = screenWidth/2;  //裁剪2分之1
        lp = (LinearLayout.LayoutParams)cursor.getLayoutParams();




        if(p!=null){
            p.removeView(fragment);
        }

        return fragment;
    }

    public void findViews(){
        btn_addpoem=fragment.findViewById(R.id.btn_addpoem);
        btn_addtalk=fragment.findViewById(R.id.btn_addtalk);
        cursor = fragment.findViewById(R.id.cursor);
        mViewPager = (ViewPager)fragment.findViewById(R.id.viewpager);
    }


    public void initViewPager() {
        mViewPager.addOnPageChangeListener(new ViewPagetOnPagerChangedLisenter());
        mViewPager.setAdapter(mViewPagerFragmentAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(2);



    }
    public void initFragmetList() {
        mFragmentList.clear();
        Fragment addPoemFragment = new AddPoemFragment();
        Fragment addTalkFragment=new AddTalkFragment();
        mFragmentList.add(addPoemFragment);
        mFragmentList.add(addTalkFragment);

    }
    private class ViewPagetOnPagerChangedLisenter implements ViewPager.OnPageChangeListener {



        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            offset = (screenl_3-cursor.getLayoutParams().width)/2;
            Log.d("TAG", "111----"+position + "--" + positionOffset + "--"
                    + positionOffsetPixels);
            final float scale = getResources().getDisplayMetrics().density;
            if (position == 0){
                lp.leftMargin = (int)(positionOffsetPixels/2)+offset;
            }else if(position ==1){
                lp.leftMargin = (int)(positionOffsetPixels/2)+screenl_3+offset;
            }
            cursor.setLayoutParams(lp);
            upTextcolor(position);

        }
        @Override
        public void onPageSelected(int position) {
            Log.d("CHANGE","onPageSelected");
        }
        @Override
        public void onPageScrollStateChanged(int state) { }
    }

    private void upTextcolor(int position){
        if (position==0){
            btn_addpoem.setTextColor(getResources().getColor(R.color.colorTheme));
            btn_addtalk.setTextColor(getResources().getColor(R.color.colorText));
        }else if(position==1){
            btn_addpoem.setTextColor(getResources().getColor(R.color.colorText));
            btn_addtalk.setTextColor(getResources().getColor(R.color.colorTheme));
        }else if(position==2){
            btn_addpoem.setTextColor(getResources().getColor(R.color.colorTheme));
            btn_addtalk.setTextColor(getResources().getColor(R.color.colorText));
        }
    }
    private class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_addpoem:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.btn_addtalk:
                    mViewPager.setCurrentItem(1);
                    break;
            }
        }
    }





}
