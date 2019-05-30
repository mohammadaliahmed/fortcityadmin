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

import com.appsinventiv.toolsbazzaradmin.Activities.Products.AddSubCategories;
import com.appsinventiv.toolsbazzaradmin.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by AliAh on 27/11/2018.
 */

public class AddCountryAdapter extends RecyclerView.Adapter<AddCountryAdapter.ViewHolder> {
    Context context;
    ArrayList<CountryModel> list;

    public AddCountryAdapter(Context context, ArrayList<CountryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.country_item_layout, parent, false);
        AddCountryAdapter.ViewHolder viewHolder = new AddCountryAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CountryModel model = list.get(position);
        holder.title.setText(model.getCountryName());
        holder.currency.setText("Currency: " + model.getCurrencySymbol());
        holder.countryCode.setText(model.getMobileCode());
        Glide.with(context).load(model.getImageUrl()).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AddProvinces.class);
                i.putExtra("country", model.getCountryName());
                i.putExtra("type", model.isShippingCountry());
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, countryCode, currency;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            currency = itemView.findViewById(R.id.currency);
            countryCode = itemView.findViewById(R.id.countryCode);
            image = itemView.findViewById(R.id.image);
        }
    }

}
