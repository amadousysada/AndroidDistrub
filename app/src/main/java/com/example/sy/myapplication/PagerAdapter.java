package com.example.sy.myapplication; /**
 * Created by sy on 27/04/18.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

// cette classe est un adapter pour notre System de tab.
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabHome tab1 = new TabHome();
                return tab1;
            case 1:
                TabPlay tab2 = new TabPlay();
                return tab2;
            case 2:
                TabAbout tab3 = new TabAbout();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
