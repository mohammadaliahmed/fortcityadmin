package com.appsinventiv.toolsbazzaradmin.Activities.Customers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class SellerListAdapter extends RecyclerView.Adapter<SellerListAdapter.ViewHolder> {
    Context context;
    ArrayList<SellerModel> itemList;
    SellerListCallbacks callbacks;
    HashMap<String, Boolean> map = new HashMap<>();

    public void setUserStatus(HashMap<String, Boolean> map) {
        this.map = map;
        notifyDataSetChanged();
    }

    public SellerListAdapter(Context context, ArrayList<SellerModel> itemList, SellerListCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.seller_item_layout, parent, false);
        SellerListAdapter.ViewHolder viewHolder = new SellerListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SellerModel model = itemList.get(position);
        if (map != null && map.size() > 0) {
            if (map.get(model.getUsername()) != null) {
                if (map.get(model.getUsername())) {
                    holder.onlineStatus.setVisibility(View.VISIBLE);
                } else {
                    holder.onlineStatus.setVisibility(View.GONE);

                }
            }
        }
        holder.name.setText(model.getStoreName());
        holder.info.setText("Phone: " + model.getPhone());
        holder.status.setText(model.isApproved() ? "Approved" : "Unapproved");
        if(model.getPicUrl()!=null){
            Glide.with(context).load(model.getPicUrl()).into(holder.imageView2);
        }

        if (model.isStatus()) {
            holder.switchh.setChecked(true);
        } else {
            holder.switchh.setChecked(false);
        }
        holder.switchh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isPressed()) {
                    callbacks.onStatusChanged(model, b);
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ViewSeller.class);
                i.putExtra("sellerId", model.getUsername());
                context.startActivity(i);
            }
        });
        holder.dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + model.getPhone()));
                context.startActivity(i);
            }
        });
        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone=" + model.getPhone();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, info, status;
        Switch switchh;
        ImageView whatsapp, dial;
        ImageView onlineStatus;
        CircleImageView imageView2;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            info = itemView.findViewById(R.id.info);
            switchh = itemView.findViewById(R.id.switchh);
            status = itemView.findViewById(R.id.status);
            dial = itemView.findViewById(R.id.dial);
            whatsapp = itemView.findViewById(R.id.whatsapp);
            onlineStatus = itemView.findViewById(R.id.onlineStatus);
            imageView2 = itemView.findViewById(R.id.imageView2);

        }
    }

    public interface SellerListCallbacks {
        public void onStatusChanged(SellerModel sellerModel, boolean status);

    }
}
