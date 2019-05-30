package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Models.AdminTermsModel;
import com.appsinventiv.toolsbazzaradmin.Models.CompanyDetailsModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ViewTerms extends AppCompatActivity {

    DatabaseReference mDatabase;
    private LinearLayout abc;

    TextView storeAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_terms);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        abc = findViewById(R.id.abc);
        storeAddress = findViewById(R.id.storeAddress);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }



        mDatabase.child("Settings").child("AdminTerms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    AdminTermsModel model = dataSnapshot.getValue(AdminTermsModel.class);
                    if (model != null) {
                        onAddField("Terms of offer and Conditions", model.getTermsofofferandConditions());
                        onAddField("Job title", model.getJobtitle());
                        onAddField("Working schedule", model.getWorkingschedule());
                        onAddField("Employment Relationship", model.getEmploymentRelationship());
                        onAddField("Cash Compensation", model.getCashCompensation());
                        onAddField("Salary", model.getSalary());
                        onAddField("Tax withholding EPF / ETF", model.getTaxwithholdingEPFETF());
                        onAddField("Tax advice EPF/ ETF", model.getTaxadviceEPFETF());
                        onAddField("Bonus (or commission) potential", model.getBonusorcommissionpotential());
                        onAddField("Employee benefits", model.getEmployeebenefits());
                        onAddField("Privacy and Confidentiality Agreements", model.getPrivacyandConfidentialityAgreements());
                        onAddField("Conflict of Interest policy", model.getConflictofInterestpolicy());
                        onAddField("Termination Conditions", model.getTerminationConditions());
                        onAddField("Interpretation, Amendment and Enforcement", model.getInterpretationAmendmentandEnforcement());
                        onAddField("Cookies", model.getCookies());
                        onAddField("License", model.getLicense());
                        onAddField("Hyperlinking to our Content", model.getHyperlinkingtoourContent());
                        onAddField("iFrames", model.getiFrames());
                        onAddField("Content Liability", model.getContentLiability());
                        onAddField("Reservation of Rights", model.getReservationofRights());
                        onAddField("Removal of links from our Application", model.getRemovaloflinksfromourApplication());
                        onAddField("Disclaimer", model.getDisclaimer());
                        onAddField("Other", model.getOther());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getAddressFromDb();
    }
    private void getAddressFromDb() {
        mDatabase.child("Settings").child("CompanyDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    CompanyDetailsModel model=dataSnapshot.getValue(CompanyDetailsModel.class);
                    storeAddress.setText(model.getAddress()+" "+model.getPhone()+" "+model.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void onAddField(String t1, String t2) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        TextView title = rowView.findViewById(R.id.title);
        TextView text = rowView.findViewById(R.id.text);

        title.setText(t1);
        text.setText(t2);

        // Add the new row before the add field button.
        abc.addView(rowView, abc.getChildCount() );
    }
}
