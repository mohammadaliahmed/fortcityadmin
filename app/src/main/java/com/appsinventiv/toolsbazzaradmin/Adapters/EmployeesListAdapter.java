package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Employees.ViewEmployee;
import com.appsinventiv.toolsbazzaradmin.Models.Employee;
import com.appsinventiv.toolsbazzaradmin.Models.VendorModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AliAh on 10/09/2018.
 */

public class EmployeesListAdapter extends RecyclerView.Adapter<EmployeesListAdapter.ViewHolder> {
    Context context;
    ArrayList<Employee> itemList;
    EmployeeCallbacks callbacks;

    public EmployeesListAdapter(Context context, ArrayList<Employee> itemList, EmployeeCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.employee_item_list, parent, false);
        EmployeesListAdapter.ViewHolder viewHolder = new EmployeesListAdapter.ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Employee model = itemList.get(position);


        holder.name.setText("" + model.getName());
        holder.phone.setText("Phone: " + model.getPhone() + "\n" + "" + CommonUtils.rolesList[model.getRole()]);

        if (model.getPicUrl() != null) {
            Glide.with(context).load(model.getPicUrl()).into(holder.image);

        } else {
            Glide.with(context).load(R.drawable.ic_profile_placeholder).into(holder.image);
        }
        if (model.isApproved()) {
            holder.approvedText.setText("Approved");
            holder.approvedText.setTextColor(context.getResources().getColor(R.color.colorBlack));
        } else {
            holder.approvedText.setText("No Approved");
            holder.approvedText.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View layout = layoutInflater.inflate(R.layout.popup_menu_design, null);

                dialog.setContentView(layout);

                TextView title = layout.findViewById(R.id.title);
                LinearLayout call = layout.findViewById(R.id.call);
                LinearLayout whatsapp = layout.findViewById(R.id.whatsapp);
                Switch approveSwitch = layout.findViewById(R.id.approveSwitch);

                title.setText("Choose option for " + model.getName());


                whatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = "https://api.whatsapp.com/send?phone=" + model.getPhone();
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(i);
                    }
                });
                if (model.isApproved()) {
                    approveSwitch.setChecked(true);
                }
                approveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (compoundButton.isPressed()) {
//                            callbacks.onChangeStatus(model, b);
                            callbacks.onApprove(model, b);
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


//        holder.menu.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onClick(View view) {
//                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View layout = layoutInflater.inflate(R.layout.popup_menu_design, null);
//                LinearLayout call = layout.findViewById(R.id.call);
//                LinearLayout whatsapp = layout.findViewById(R.id.whatsapp);
//                Switch approveSwitch = layout.findViewById(R.id.approveSwitch);
//
//
//                whatsapp.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        String url = "https://api.whatsapp.com/send?phone=" + model.getPhone();
//                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        context.startActivity(i);
//                    }
//                });
//                if (model.isApproved()) {
//                    approveSwitch.setChecked(true);
//                }
//                approveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                        if (compoundButton.isPressed()) {
////                            callbacks.onChangeStatus(model, b);
//                            callbacks.onApprove(model,b);
//                        }
//                    }
//                });
//                call.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + model.getPhone()));
//                        context.startActivity(i);
//                    }
//                });
//                // Creating the PopupWindow
//                PopupWindow changeStatusPopUp = new PopupWindow(context);
//                changeStatusPopUp.setContentView(layout);
//                changeStatusPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
//                changeStatusPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
//                changeStatusPopUp.setFocusable(true);
//
//                changeStatusPopUp.setBackgroundDrawable(new BitmapDrawable());
//                int ivHeight = holder.menu.getHeight() - 20;
//
////                changeStatusPopUp.showAsDropDown(layout, 200, -layout.getHeight() + changeStatusPopUp.getHeight());
//                changeStatusPopUp.showAsDropDown(layout, 200, -layout.getHeight() ,Gravity.END);
//
//
//
//            }
//        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ViewEmployee.class);
                i.putExtra("username", model.getUsername());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, approvedText;
        ImageView menu;
        CircleImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            image = itemView.findViewById(R.id.image);

            menu = itemView.findViewById(R.id.menu);
            approvedText = itemView.findViewById(R.id.approvedText);
        }
    }

    public interface EmployeeCallbacks {

        public void onChangeStatus(Employee model, boolean abc);

        public void onApprove(Employee model, boolean value);
    }
}
