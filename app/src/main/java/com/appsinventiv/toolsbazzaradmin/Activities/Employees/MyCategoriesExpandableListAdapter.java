package com.appsinventiv.toolsbazzaradmin.Activities.Employees;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zerones on 04-Oct-17.
 */

public class MyCategoriesExpandableListAdapter extends BaseExpandableListAdapter {

    public static ArrayList<ArrayList<HashMap<String, String>>> childItems;
    public static ArrayList<HashMap<String, String>> parentItems;
    //    private final ArrayList<HashMap<String, String>> childItems;
    private LayoutInflater inflater;
    private Activity activity;
    private HashMap<String, String> child;
    private int count = 0;
    private boolean isFromMyCategoriesFragment;

    public MyCategoriesExpandableListAdapter(Activity activity, ArrayList<HashMap<String, String>> parentItems,
                                             ArrayList<ArrayList<HashMap<String, String>>> childItems,boolean isFromMyCategoriesFragment) {

        this.parentItems = parentItems;
        this.childItems = childItems;
        this.activity = activity;
        this.isFromMyCategoriesFragment = isFromMyCategoriesFragment;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (childItems.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean b, View convertView, ViewGroup viewGroup) {
         final ViewHolderParent viewHolderParent;
        if (convertView == null) {

            if(isFromMyCategoriesFragment) {
                convertView = inflater.inflate(R.layout.group_list_layout_my_categories, null);
            }else {
                convertView = inflater.inflate(R.layout.group_list_layout_choose_categories, null);
            }
            viewHolderParent = new ViewHolderParent();

            viewHolderParent.tvMainCategoryName = convertView.findViewById(R.id.tvMainCategoryName);
            viewHolderParent.cbMainCategory = convertView.findViewById(R.id.cbMainCategory);
            viewHolderParent.ivCategory = convertView.findViewById(R.id.ivCategory);
            convertView.setTag(viewHolderParent);
        } else {
            viewHolderParent = (ViewHolderParent) convertView.getTag();
        }

        if (parentItems.get(groupPosition).get(ConstantManager.Parameter.IS_CHECKED).equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {
            viewHolderParent.cbMainCategory.setChecked(true);
            notifyDataSetChanged();

        } else {
            viewHolderParent.cbMainCategory.setChecked(false);
            notifyDataSetChanged();
        }

        viewHolderParent.cbMainCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolderParent.cbMainCategory.isChecked()) {
                    parentItems.get(groupPosition).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_TRUE);

                    for (int i = 0; i < childItems.get(groupPosition).size(); i++) {
                        childItems.get(groupPosition).get(i).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_TRUE);
                    }
                    notifyDataSetChanged();

                }
                else {
                    parentItems.get(groupPosition).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_FALSE);
                    for (int i = 0; i < childItems.get(groupPosition).size(); i++) {
                        childItems.get(groupPosition).get(i).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_FALSE);
                    }
                    notifyDataSetChanged();
                }
            }
        });

        ConstantManager.childItems = childItems;
        ConstantManager.parentItems = parentItems;

        viewHolderParent.tvMainCategoryName.setText(parentItems.get(groupPosition).get(ConstantManager.Parameter.CATEGORY_NAME));

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean b, View convertView, ViewGroup viewGroup) {

        final ViewHolderChild viewHolderChild;
        child = childItems.get(groupPosition).get(childPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_list_layout_choose_category, null);
            viewHolderChild = new ViewHolderChild();

            viewHolderChild.tvSubCategoryName = convertView.findViewById(R.id.tvSubCategoryName);
            viewHolderChild.cbSubCategory = convertView.findViewById(R.id.cbSubCategory);
            viewHolderChild.viewDivider = convertView.findViewById(R.id.viewDivider);
            convertView.setTag(viewHolderChild);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }

        if (childItems.get(groupPosition).get(childPosition).get(ConstantManager.Parameter.IS_CHECKED).equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {
            viewHolderChild.cbSubCategory.setChecked(true);
            notifyDataSetChanged();
        } else {
            viewHolderChild.cbSubCategory.setChecked(false);
            notifyDataSetChanged();
        }

        viewHolderChild.tvSubCategoryName.setText(child.get(ConstantManager.Parameter.SUB_CATEGORY_NAME));
        viewHolderChild.cbSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolderChild.cbSubCategory.isChecked()) {
                    count = 0;
                    childItems.get(groupPosition).get(childPosition).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_TRUE);
                    notifyDataSetChanged();
                } else {
                    count = 0;
                    childItems.get(groupPosition).get(childPosition).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_FALSE);
                    notifyDataSetChanged();
                }

                for (int i = 0; i < childItems.get(groupPosition).size(); i++) {
                    if (childItems.get(groupPosition).get(i).get(ConstantManager.Parameter.IS_CHECKED).equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {
                        count++;
                    }
                }
                if (count == childItems.get(groupPosition).size()) {
                    parentItems.get(groupPosition).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_TRUE);
                    notifyDataSetChanged();
                } else {
                    parentItems.get(groupPosition).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_FALSE);
                    notifyDataSetChanged();
                }


                ConstantManager.childItems = childItems;
                ConstantManager.parentItems = parentItems;
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    private class ViewHolderParent {

        TextView tvMainCategoryName;
        CheckBox cbMainCategory;
        ImageView ivCategory;
    }

    private class ViewHolderChild {

        TextView tvSubCategoryName;
        CheckBox cbSubCategory;
        View viewDivider;
    }


}
