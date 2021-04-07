package com.example.authentication.Activity.Walkthrough;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SlideFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    public SlideFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return SlideListFragment.newInstance(position);
    }
}
