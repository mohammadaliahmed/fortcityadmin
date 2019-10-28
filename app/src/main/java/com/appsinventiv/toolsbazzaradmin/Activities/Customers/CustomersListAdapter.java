package com.appsinventiv.toolsbazzaradmin.Activities.Customers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Models.Customer;
import com.appsinventiv.toolsbazzaradmin.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AliAh on 28/11/2018.
 */

public class CustomersListAdapter extends RecyclerView.Adapter<CustomersListAdapter.ViewHolder> {
    Context context;
    ArrayList<Customer> itemList;
    CustomerListCallback callback;
    HashMap<String, Boolean> map = new HashMap<>();


    public CustomersListAdapter(Context context, ArrayList<Customer> itemList, CustomerListCallback callback) {
        this.context = context;
        this.itemList = itemList;
        this.callback = callback;
    }

    public void setUserStatus(HashMap<String, Boolean> map) {
        this.map = map;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_item_layout, parent, false);
        CustomersListAdapter.ViewHolder viewHolder = new CustomersListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Customer model = itemList.get(position);
        if (map != null && map.size() > 0) {
            if (map.get(model.getUsername()) != null) {
                if (map.get(model.getUsername())) {
                    holder.onlineStatus.setVisibility(View.VISIBLE);
                } else {
                    holder.onlineStatus.setVisibility(View.GONE);

                }
            }
        }
        holder.name.setText(model.getName());
        holder.info.setText("Phone: " + model.getPhone());

        if (model.isStatus()) {
            holder.switchh.setChecked(true);
        } else {
            holder.switchh.setChecked(false);
        }

        holder.switchh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isPressed()) {
                    callback.onStatusChanged(model, b);
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ViewCustomer.class);
                i.putExtra("customerId", model.getUsername());
                context.startActivity(i);
            }
        });

        if (model.getPicUrl() != null) {
            Glide.with(context).load(model.getPicUrl()).into(holder.imageView2);
        } else {
            Glide.with(context).load(R.drawable.ic_profile_placeholder).into(holder.imageView2);

        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, info;
        Switch switchh;
        ImageView onlineStatus;
        CircleImageView imageView2;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            info = itemView.findViewById(R.id.info);
            switchh = itemView.findViewById(R.id.switchh);
            onlineStatus = itemView.findViewById(R.id.onlineStatus);
            imageView2 = itemView.findViewById(R.id.imageView2);

        }
    }

    public interface CustomerListCallback {
        public void onStatusChanged(Customer customers, boolean status);
    }
}
