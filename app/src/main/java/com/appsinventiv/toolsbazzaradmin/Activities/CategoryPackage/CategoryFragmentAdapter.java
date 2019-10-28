package com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.appsinventiv.toolsbazzaradmin.Activities.Accounts.SODetails.NewSOFragment;


public class CategoryFragmentAdapter extends FragmentPagerAdapter {

    Context mContext;

    public CategoryFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new ProductCategoryFragment();
        } else {
            return new AttributesFragment();
        }

    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        if (position == 0) {
            return "Product Category";
        } else {
            return "Attributes";
        }
    }

}



