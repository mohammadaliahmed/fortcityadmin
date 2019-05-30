package com.appsinventiv.toolsbazzaradmin.Adapters;

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

import com.appsinventiv.toolsbazzaradmin.Activities.Vendors.AddVendors;
import com.appsinventiv.toolsbazzaradmin.Models.VendorModel;
import com.appsinventiv.toolsbazzaradmin.R;

import java.util.ArrayList;

/**
 * Created by AliAh on 24/08/2018.
 */

public class VendorsListAdapter extends RecyclerView.Adapter<VendorsListAdapter.ViewHolder> {
    Context context;
    ArrayList<VendorModel> itemList;
    DeleteVendor deleteVendor;

    public VendorsListAdapter(Context context, ArrayList<VendorModel> itemList, DeleteVendor deleteVendor) {
        this.context = context;
        this.itemList = itemList;
        this.deleteVendor = deleteVendor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vendor_item_layout, parent, false);
        VendorsListAdapter.ViewHolder viewHolder = new VendorsListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final VendorModel model = itemList.get(position);
        holder.name.setText("" + model.getVendorName());
        holder.phone.setText("Phone: " + model.getVendorPhone()+"\n"+"Address: " + model.getVendorAddress()
                +"\n"+"Email: " +( model.getEmail()==null?"Email: ":model.getEmail()));

        if (model.getIsActive().equalsIgnoreCase("yes")) {
            holder.switchh.setChecked(true);
        } else if (model.getIsActive().equalsIgnoreCase("no")) {
            holder.switchh.setChecked(false);
        }




        holder.dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + model.getVendorPhone()));
                context.startActivity(i);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AddVendors.class);
                i.putExtra("vendorId", model.getVendorId());
                context.startActivity(i);
            }
        });
        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone=" + model.getVendorPhone();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(i);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteVendor.onDelete(model);
            }
        });



        holder.switchh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isPressed()){
                    deleteVendor.onChangeStatus(model, b);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, address, email;
        ImageView dial, whatsapp, delete;
        Switch switchh;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            dial = itemView.findViewById(R.id.phone_dial);
            whatsapp = itemView.findViewById(R.id.whatsapp);
            delete = itemView.findViewById(R.id.delete);
            switchh = itemView.findViewById(R.id.switchh);

        }
    }

    public interface DeleteVendor {
        public void onDelete(VendorModel model);

        public void onChangeStatus(VendorModel model, boolean abc);
    }
}
