// COMP4521     WAN Yuxuan  20493150    ywanaf@connect.ust.hk
// COMP4521     REN Jiming  20493019    jrenaf@connect.ust.hk
// COMP4521     YIN Yue     20493368    yyinai@connect.ust.hk
package com.example.officeathome;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {

    private static final int NUM_PAGES = 5;
    public ScreenSlidePagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new ExecutivesFragment();
            case 1: return new RDFragment();
            case 2: return new HRFragment();
            case 3: return new MarketingFragment();
            case 4: return new PurchasingFragment();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
