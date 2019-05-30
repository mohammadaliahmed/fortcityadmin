package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.appsinventiv.toolsbazzaradmin.Activities.Orders.OrdersFragment;
import com.appsinventiv.toolsbazzaradmin.Activities.Purchases.CompletedPurchasesFragment;
import com.appsinventiv.toolsbazzaradmin.Activities.Purchases.ListOfPosFragment;
import com.appsinventiv.toolsbazzaradmin.Activities.Purchases.PendingFragment;


/**
 * Created by AliAh on 02/03/2018.
 */

public class PurchasesFragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public PurchasesFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new PendingFragment();
        } else if (position == 1) {
            return new CompletedPurchasesFragment();
        }

//        }else if (position == 2) {
//            return new CompletedPurchasesFragment();
//
//        }
        else {
            return null;
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
        switch (position) {
            case 0:
                return "Pending";
            case 1:
                return "Completed";
//            case 2:
//                return "Completed";


            default:
                return null;
        }
    }

}
