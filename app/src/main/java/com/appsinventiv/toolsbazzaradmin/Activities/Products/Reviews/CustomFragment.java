package com.appsinventiv.toolsbazzaradmin.Activities.Products.Reviews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.Constants;

import static android.support.constraint.Constraints.TAG;

public class CustomFragment extends Fragment {
    private String mText = "";
    RejectCallbacks callbacks;

    public static CustomFragment createInstance(String txt) {
        CustomFragment fragment = new CustomFragment();
        fragment.mText = txt;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        callbacks = (RejectCallbacks) getActivity();

        View v = inflater.inflate(R.layout.fragment_sample, container, false);
        final String[] mobileArray;
        if (mText.equalsIgnoreCase("Rejected")) {
            mobileArray = new String[]{"Attributes Missing", "Product Images are not clear", "Product Images are too Big",
                    "Product Images are too Small", "Product Images Inappropriate", "Second Hand products"};
        } else {
//            mobileArray = new String[]{"Alcoholic Beverages", "Animals", "Antiques", "Artwork",
//                    "Casinos and Gambling Equipment", "Corrosives", "Counterfeit Goods", "Dangerous Goods",
//                    "Drugs", "Electronics", "Explosives", "Fake Items", "Fake/ Dummy Games", "Flammable liquids"
//                    , "Flammable solids", "Fragile", "Gambling", "Gases compressed, liquefied or dissolved under pressure"
//                    , "Government Issued Documents", "Healthcare and Medicine", "Immovable Property", "Live Animals",
//                    "Minerals", "Miscellaneous", "Negotiable Currency", "Oxidizing substances and organic peroxides solids",
//                    "Packaging", "Perishables", "Plants", "Pornography", "Radioactive material"
//                    , "Remains", "Sharp Tools/Weapons", "Tobacco Products", "Toxic and infectious substances", "Traffic Devices", "Vehicles"};
            mobileArray = new String[]{"Alcoholic Beverages", "Animals", "Antiques", "Artwork", "Corrosives", "Counterfeit Goods",
                    "Drugs & Tobacco Products", "Electronics", "Explosives or Dangerous Goods", "Flammable liquids", "Flammable solids",
                    "Fragile", "Gambling", "Gases compressed, liquefied", "Government Issued Documents",
                    "Healthcare & Medicine", "Immovable Property", "Minerals", "Miscellaneous", "Negotiable Currency ",
                    "Oxidizing Substances & Organic Peroxides ", "Packaging", "Perishables", "Plants", "Pornography", "Radioactive Material  ",
                    "Sharp Tools or Weapons", "Toxic & Infectious Substances", "Vehicles"};
        }


        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, mobileArray);
        ListView listView = v.findViewById(R.id.ListView);

//        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String abc = mobileArray[i];

                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment prev = manager.findFragmentByTag("dialog");
                if (prev != null) {
                    manager.beginTransaction().remove(prev).commit();
                    callbacks.onOptionSelected(abc);
                }

            }
        });

        return v;
    }
}
