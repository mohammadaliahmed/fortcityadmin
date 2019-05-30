package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Employees.ViewEmployee;
import com.appsinventiv.toolsbazzaradmin.Models.Employee;
import com.appsinventiv.toolsbazzaradmin.Models.VendorModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by AliAh on 10/09/2018.
 */

public class EmployeesListAdapter extends RecyclerView.Adapter<EmployeesListAdapter.ViewHolder> {
    Context context;
    ArrayList<Employee> itemList;
    EmployeeCallbacks callbacks;
    public EmployeesListAdapter(Context context, ArrayList<Employee> itemList,EmployeeCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks=callbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.employee_item_list, parent, false);
        EmployeesListAdapter.ViewHolder viewHolder = new EmployeesListAdapter.ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Employee model = itemList.get(position);
        if(model.isActive()){
            holder.switchh.setChecked(true);
        }else{
            holder.switchh.setChecked(false);
        }

        if(model.isApproved()){
            holder.approve.setChecked(true);
        }else{
            holder.approve.setChecked(false);
        }


        holder.name.setText("" + model.getName());
        holder.phone.setText("Phone: " + model.getPhone() + "\n" + "Designation: " + CommonUtils.rolesList[model.getRole()]);

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


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ViewEmployee.class);
                i.putExtra("username", model.getUsername());
                context.startActivity(i);
            }
        });
        holder.switchh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isPressed()){
                    callbacks.onChangeStatus(model,b);
                }
            }
        });
        holder.approve.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isPressed()){
                    callbacks.onApprove(model,b);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, address, designation;
        ImageView dial, whatsapp;
        Switch switchh;
        CheckBox approve;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            dial = itemView.findViewById(R.id.phone_dial);
            whatsapp = itemView.findViewById(R.id.whatsapp);
            switchh = itemView.findViewById(R.id.switchh);
            approve = itemView.findViewById(R.id.approve);
        }
    }
    public interface EmployeeCallbacks{

        public void onChangeStatus(Employee model, boolean abc);
        public void onApprove(Employee model, boolean value);
    }
}
