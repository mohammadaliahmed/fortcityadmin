package com.appsinventiv.toolsbazzaradmin.Activities.Products;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.appsinventiv.toolsbazzaradmin.Activities.Orders.InvoiceListFragment;
import com.appsinventiv.toolsbazzaradmin.Activities.Orders.OrdersFragment;
import com.appsinventiv.toolsbazzaradmin.Interfaces.TabCountCallbacks;

import java.util.ArrayList;


/**
 * Created by AliAh on 02/03/2018.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    Context mContext;
    ArrayList<String> arrayList;
    TabCountCallbacks callbacks;

    public FragmentAdapter(Context context, ArrayList<String> arrayList, FragmentManager fm) {
        super(fm);
        this.mContext = context;
        this.arrayList = arrayList;
    }


    public void setListener(TabCountCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(final int position) {

        if (position == 0) {
            return new ProductsListFragment();
        } else {
            return new VendorProductsListFragment(new TabCountCallbacks() {
                @Override
                public void newCount(int count,int po) {
                    callbacks.newCount(count,position);
                }
            });

        }

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
