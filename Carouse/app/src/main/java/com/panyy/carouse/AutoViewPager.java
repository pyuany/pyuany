package com.panyy.carouse;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：panyuanyuan
 * 时间：16/5/1 20:32
 * 邮箱：pyy6663@gmail.com
 * 版本：1.0
 */
public class AutoViewPager extends RelativeLayout implements ViewPager.OnPageChangeListener {
    ViewPagerAdapter mPagerAdapter;
    private List<ImageView> listviews;
    private List<String> mList = new ArrayList<>();
    private ViewPager mViewPager;
    private int mViewListSize;
    private final int CONSTANT_ONE = 1;
    private final int CONSTANT_TWO = 2;
    private final int CONSTANT_THREE = 3;
    private int CONSTANT_DATE_TIME = 3000;
    private final int CONSTANT_ZERO = 0;
    private    int pageIndex ;
    private OnAutoItemClickListener onAutoItemClickListener;

    public AutoViewPager(Context context) {
        this(context, null);
    }

    public AutoViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //添加任意View视图
    public void setViews(final List<String> mList1) {
        if (mList1.size() < CONSTANT_THREE) {
            return;
        }
        mViewListSize = mList1.size();
        //初始化pager
        mViewPager = new ViewPager(getContext());

        //添加viewpager到SliderLayout
        addView(mViewPager);
        //设置缓存数量
        //设置间隔
        mViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.ui_5_dip));
        //监听数据刷新
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(getResources().getDimensionPixelSize(R.dimen.ui_40_dip), 0, getResources().getDimensionPixelSize(R.dimen.ui_40_dip), 0);
        listviews = new ArrayList<>();
        // 添加viewpager多出的两个view
        int length = mList1.size() + CONSTANT_TWO;
        for (int i = CONSTANT_ZERO; i < length; i++) {
            if (i == CONSTANT_ZERO) {
                this.mList.add(mList1.get(mList1.size() - CONSTANT_ONE));
            } else if (i == mList1.size() + CONSTANT_ONE) {
                this.mList.add(mList1.get(CONSTANT_ZERO));
            } else {
                this.mList.add(mList1.get(i - CONSTANT_ONE));
            }
        }
        for (int i = CONSTANT_ZERO; i < this.mList.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            ViewGroup.LayoutParams viewPagerImageViewParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(viewPagerImageViewParams);
            Glide.with(getContext())
                    .load(this.mList.get(i))
                    .centerCrop()
                    .placeholder(R.drawable.img1)
                    .crossFade()
                    .into(imageView);
            listviews.add(imageView);

        }
        mPagerAdapter = new ViewPagerAdapter();
        mViewPager.setOffscreenPageLimit(listviews.size());

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        // 设置viewpager在第二个视图显示
        mViewPager.setCurrentItem(CONSTANT_ONE);
        handler.postDelayed(r, CONSTANT_DATE_TIME);
    }

    public void setDateTime(int dateTime) {
        if (CONSTANT_ZERO != dateTime) {
            CONSTANT_DATE_TIME = dateTime;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int i) {
        if (this != null) {
            this.invalidate();
        }
         pageIndex = i;
        if (i == CONSTANT_ZERO) {
            // 当视图在第一个时，将页面号设置为图片的最后一张。
            pageIndex = mViewListSize;
        } else if (i == mViewListSize + CONSTANT_ONE) {
            // 当视图在最后一个是,将页面号设置为图片的第一张。
            pageIndex = CONSTANT_ONE;
        }
        if (i != pageIndex) {
            mViewPager.setCurrentItem(pageIndex, false);
            return;
        }
    }

    public void setOnAutoItemClickListener(OnAutoItemClickListener onAutoItemClickListener) {
        this.onAutoItemClickListener = onAutoItemClickListener;
    }

    public interface OnAutoItemClickListener {
        void onItemClick(int position);
    }


    class ViewPagerAdapter extends PagerAdapter  {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView view = listviews.get(position);
            container.removeView(view);
            view.setImageBitmap(null);
        }

        @Override
        public Object instantiateItem(ViewGroup container,final int i) {
            View view=listviews.get(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onAutoItemClickListener!=null){
                        onAutoItemClickListener.onItemClick(i);
                    }
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return listviews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }
    }

    /**
     * 停止自动轮播
     */
    public void stopAutoPlay() {
        handler.removeCallbacks(r);
    }


    /**
     * 开始自动轮播
     */
    public void startAutoPlay() {
        handler.postDelayed(r, CONSTANT_DATE_TIME);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopAutoPlay();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                startAutoPlay();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            int index = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(++index);
            handler.postDelayed(r, CONSTANT_DATE_TIME);
        }
    };

    Handler handler = new Handler();

}
