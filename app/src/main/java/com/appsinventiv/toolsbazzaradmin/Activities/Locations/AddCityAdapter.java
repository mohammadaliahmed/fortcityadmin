package com.appsinventiv.toolsbazzaradmin.Activities.Locations;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.R;

import java.util.ArrayList;

/**
 * Created by AliAh on 27/11/2018.
 */

public class AddCityAdapter extends RecyclerView.Adapter<AddCityAdapter.ViewHolder> {
    Context context;
    ArrayList<CityDeliveryChargesModel> list;

    public AddCityAdapter(Context context, ArrayList<CityDeliveryChargesModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.city_list_item, parent, false);
        AddCityAdapter.ViewHolder viewHolder = new AddCityAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CityDeliveryChargesModel model = list.get(position);
        holder.cityName.setText(model.getCityName());
        holder.halfKg.setText(model.getHalfKg());
        holder.oneKg.setText(model.getOneKg());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityName, oneKg, halfKg;


        public ViewHolder(View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.cityName);
            oneKg = itemView.findViewById(R.id.oneKg);
            halfKg = itemView.findViewById(R.id.halfKg);
        }
    }

}
