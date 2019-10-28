package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Vendors.AddVendors;
import com.appsinventiv.toolsbazzaradmin.Models.VendorModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AliAh on 24/08/2018.
 */

public class VendorsListAdapter extends RecyclerView.Adapter<VendorsListAdapter.ViewHolder> {
    Context context;
    ArrayList<VendorModel> itemList;
    VendorListCallBacks callBacks;

    public VendorsListAdapter(Context context, ArrayList<VendorModel> itemList, VendorListCallBacks callBacks) {
        this.context = context;
        this.itemList = itemList;
        this.callBacks = callBacks;
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
        holder.phone.setText("Phone: " + model.getVendorPhone() + "\n" + "Address: " + model.getVendorAddress()
                + "\n" + "Email: " + (model.getEmail() == null ? "Email: " : model.getEmail()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AddVendors.class);
                i.putExtra("vendorId", model.getVendorId());
                context.startActivity(i);
            }
        });
        if (model.getPicUrl() != null) {
            Glide.with(context).load(model.getPicUrl()).into(holder.image);

        } else {
            Glide.with(context).load(R.drawable.ic_profile_placeholder).into(holder.image);
        }


        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                View layout = layoutInflater.inflate(R.layout.popup_vendor_menu_design, null);

                dialog.setContentView(layout);

                TextView title = layout.findViewById(R.id.title);
                LinearLayout call = layout.findViewById(R.id.call);
                LinearLayout whatsapp = layout.findViewById(R.id.whatsapp);
                Switch approveSwitch = layout.findViewById(R.id.approveSwitch);

                CircleImageView picture = layout.findViewById(R.id.picture);


                if (model.getPicUrl() != null) {
                    Glide.with(context).load(model.getPicUrl()).into(picture);
                }

                title.setText(model.getVendorName());


                whatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = "https://api.whatsapp.com/send?phone=" + model.getPhone();
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(i);
                    }
                });
                if (model.isActive()) {
                    approveSwitch.setChecked(true);
                }
                approveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (compoundButton.isPressed()) {
//                            callbacks.onChangeStatus(model, b);
                            callBacks.onChangeStatus(model, b);
                        }
                    }
                });
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + model.getPhone()));
                        context.startActivity(i);
                    }
                });

                dialog.show();


            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone;
        ImageView menu;
        CircleImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            image = itemView.findViewById(R.id.image);

            menu = itemView.findViewById(R.id.menu);


        }
    }

    public interface VendorListCallBacks {
        public void onChangeStatus(VendorModel model, boolean abc);
    }
}
