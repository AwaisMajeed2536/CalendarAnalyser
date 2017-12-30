package com.example.bisma.calendar_analyzer.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.bisma.calendar_analyzer.fragment.CalendarViewFragment;

import java.util.Date;

/**
 * Created by Awais Majeed on 21/10/2017.
 */

public class CalendarViewPagerAdapter extends FragmentStatePagerAdapter{

    private Context context;

    public CalendarViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Date date = new Date();
        date.setMonth(date.getMonth()+(position - 1000/2));
        return CalendarViewFragment.newInstance(date);
    }

    @Override
    public int getCount() {
        return 1000;
    }
}
