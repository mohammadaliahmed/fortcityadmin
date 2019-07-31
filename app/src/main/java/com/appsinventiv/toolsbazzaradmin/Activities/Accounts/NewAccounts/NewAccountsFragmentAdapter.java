package com.appsinventiv.toolsbazzaradmin.Activities.Accounts.NewAccounts;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.appsinventiv.toolsbazzaradmin.Activities.Orders.NewOrder.NewOrdersFragment;

import java.util.ArrayList;


/**
 * Created by AliAh on 02/03/2018.
 */

public class NewAccountsFragmentAdapter extends FragmentPagerAdapter {

    Context mContext;
    ArrayList<String> arrayList;

    public NewAccountsFragmentAdapter(Context context, ArrayList<String> arrayList, FragmentManager fm) {
        super(fm);
        this.mContext = context;
        this.arrayList = arrayList;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {

        return new NewAccountsFragment(arrayList.get(position));

    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return arrayList.size();
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position

        return arrayList.get(position);
    }

}
