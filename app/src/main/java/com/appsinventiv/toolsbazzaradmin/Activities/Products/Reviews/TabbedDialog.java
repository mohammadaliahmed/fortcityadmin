package com.appsinventiv.toolsbazzaradmin.Activities.Products.Reviews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsinventiv.toolsbazzaradmin.R;

public class TabbedDialog extends DialogFragment {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.reject_reason_dialog, container, false);
        tabLayout = (TabLayout) rootview.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) rootview.findViewById(R.id.masterViewPager);
        CustomAdapter adapter = new CustomAdapter(getChildFragmentManager());
        adapter.addFragment("Rejected", CustomFragment.createInstance("Rejected"));
        adapter.addFragment("Prohibited Item", CustomFragment.createInstance("Prohibited"));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return rootview;
    }
}
