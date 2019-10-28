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
import com.appsinventiv.toolsbazzaradmin.Activities.Vendors.AddVendors;
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
        holder.phone.setText("Phone: " + model.getPhone() + "\n" + "" +model.getRole());

        if (model.getPicUrl() != null) {
            Glide.with(context).load(model.getPicUrl()).into(holder.image);

        } else {
            Glide.with(context).load(R.drawable.ic_profile_placeholder).into(holder.image);
        }
        if (model.isBlocked()) {
            holder.approvedText.setText("Blocked");
            holder.approvedText.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            holder.approvedText.setText("Not Blocked");
            holder.approvedText.setTextColor(context.getResources().getColor(R.color.colorBlack));
        }

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View layout = layoutInflater.inflate(R.layout.popup_employee_menu_design, null);

                dialog.setContentView(layout);

                TextView title = layout.findViewById(R.id.title);
                LinearLayout call = layout.findViewById(R.id.call);
                LinearLayout whatsapp = layout.findViewById(R.id.whatsapp);
                CheckBox blockAccount = layout.findViewById(R.id.blockAccount);
                CircleImageView picture = layout.findViewById(R.id.picture);

                if (model.getPicUrl() != null) {
                    try {
                        Glide.with(context).load(model.getPicUrl()).into(picture);
                    } catch (Exception e) {

                    }
                }

                title.setText("" + model.getName());


                whatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = "https://api.whatsapp.com/send?phone=" + model.getPhone();
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(i);
                    }
                });
                if (model.isBlocked()) {
                    blockAccount.setChecked(true);
                }
                blockAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (compoundButton.isPressed()) {
                            dialog.dismiss();
//                            callbacks.onChangeStatus(model, b);
                            callbacks.onBlock(model, b);
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


//

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

        public void onBlock(Employee model, boolean value);
    }
}
