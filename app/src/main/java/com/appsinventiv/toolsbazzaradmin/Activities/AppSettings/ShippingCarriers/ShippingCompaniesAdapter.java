package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings.ShippingCarriers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Models.LocationAndChargesModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import it.sephiroth.android.library.easing.Circ;

/**
 * Created by AliAh on 12/09/2018.
 */

public class ShippingCompaniesAdapter extends RecyclerView.Adapter<ShippingCompaniesAdapter.ViewHolder> {
    Context context;
    ArrayList<ShippingCompanyModel> itemList;

    public ShippingCompaniesAdapter(Context context, ArrayList<ShippingCompanyModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ShippingCompaniesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.shipping_list_item_layout,parent,false);
        ShippingCompaniesAdapter.ViewHolder viewHolder=new ShippingCompaniesAdapter.ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ShippingCompaniesAdapter.ViewHolder holder, int position) {
        final ShippingCompanyModel model=itemList.get(position);
        holder.name.setText(model.getName());
        holder.phone.setText(model.getTelephone());
        Glide.with(context).load(model.getPicUrl()).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,AddShippingCarriers.class);
                i.putExtra("shippingId",model.getId());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView phone, name;
        CircleImageView image;
        public ViewHolder(View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.image);
            phone=itemView.findViewById(R.id.phone);
            name=itemView.findViewById(R.id.name);

        }
    }
}
