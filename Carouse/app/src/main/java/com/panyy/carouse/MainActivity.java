package com.panyy.carouse;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private AutoViewPager viewPager;
    private List<String> mList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (AutoViewPager) findViewById(R.id.viewpager);
        mList1 = new ArrayList<>();
        mList1.add("http://img3.imgtn.bdimg.com/it/u=2546610023,3120506294&fm=11&gp=0.jpg");
        mList1.add("http://pic32.nipic.com/20130829/12906030_124355855000_2.png");
        mList1.add("http://img05.tooopen.com/images/20140604/sy_62331342149.jpg");
        mList1.add("http://img02.tooopen.com/images/20150507/tooopen_sy_122395947985.jpg");
        viewPager.setViews(mList1);
        viewPager.setOnAutoItemClickListener(new AutoViewPager.OnAutoItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
}