package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Models.LocationAndChargesModel;
import com.appsinventiv.toolsbazzaradmin.R;

import java.util.ArrayList;

/**
 * Created by AliAh on 12/09/2018.
 */

public class CountriesListAdapter extends RecyclerView.Adapter<CountriesListAdapter.ViewHolder> {
    Context context;
    ArrayList<LocationAndChargesModel> itemList;

    public CountriesListAdapter(Context context, ArrayList<LocationAndChargesModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public CountriesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.countries_list_item_layout,parent,false);
        CountriesListAdapter.ViewHolder viewHolder=new CountriesListAdapter.ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull CountriesListAdapter.ViewHolder holder, int position) {
        LocationAndChargesModel model=itemList.get(position);
        holder.country.setText("Country: "+model.getCountryName());
        holder.city.setText("Citie: "+model.getCities());
        holder.charges.setText("Delivery Charges: "+model.getCurrency()+" "+model.getDeliveryCharges());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView country, city,charges;
        public ViewHolder(View itemView) {
            super(itemView);

            country=itemView.findViewById(R.id.country);
            city=itemView.findViewById(R.id.city);
            charges=itemView.findViewById(R.id.charges);

        }
    }
}
