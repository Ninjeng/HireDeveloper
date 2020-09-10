package com.theakatsuki.hiredevelopers.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theakatsuki.hiredevelopers.Adapter.BidAdapter;
import com.theakatsuki.hiredevelopers.Adapter.JobAdapter;
import com.theakatsuki.hiredevelopers.Model.Bid;
import com.theakatsuki.hiredevelopers.Model.Job;
import com.theakatsuki.hiredevelopers.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SingleJobActivity extends AppCompatActivity {

    TextView title, description, price, averagePrice,showBids;
    Button btnBid,btnUpdate;
    FirebaseUser firebaseUser;
    String jobId;
    String category = "";
    String jobTitleText = "";
    String access = "";
    EditText jobTitle,jobDescription;
    CheckBox chkDesign, chkData,chkContent,chkWebsite,chkMobile,chkMarketing;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    TextView dateTimePicker;
    Spinner spinner;
    String date;
    List<Bid> bidList;
    RecyclerView bidRecyclerView;
    LinearLayout editLayout, detailsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_job);
        getSupportActionBar().setTitle("Job details");
        title = findViewById(R.id.show_job_title);
        description = findViewById(R.id.show_job_description);
        price = findViewById(R.id.show_job_price);
        averagePrice = findViewById(R.id.showAverageBid);
        btnBid = findViewById(R.id.btnBid);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = getIntent();
        jobId = intent.getStringExtra("JobId");
        category = intent.getStringExtra("category");

        bidRecyclerView= findViewById(R.id.recyclerViewBid);
        showBids= findViewById(R.id.show_Bids);
        bidRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        editLayout= findViewById(R.id.editJobDetails);
        detailsLayout= findViewById(R.id.JobDetails);
        jobTitle = findViewById(R.id.pro_job_title1);
        spinner = findViewById(R.id.pro_job_price1);
        jobDescription = findViewById(R.id.pro_job_description1);
        dateTimePicker = findViewById(R.id.tvDatetimePicker1);
        chkContent= findViewById(R.id.chkContentDev2);
        chkData= findViewById(R.id.chkDataDev2);
        chkMobile=findViewById(R.id.chkMobileDev2);
        chkMarketing= findViewById(R.id.chkMarketingDev2);
        chkWebsite= findViewById(R.id.chkWebsiteDev2);
        chkDesign= findViewById(R.id.chkDesignDev2);
        btnUpdate = findViewById(R.id.btnUpdateJob);
        editLayout.setVisibility(View.GONE);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateJob(jobId);
            }
        });
        dateTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SingleJobActivity.this,R.style.Theme_AppCompat_Light_Dialog,onDateSetListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                date=month+"/"+day+"/"+year;
                dateTimePicker.setText(date);
            }
        };
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("$ 10-30");
        arrayList.add("$ 30-250");
        arrayList.add("$ 250-750");
        arrayList.add("$ 750-1500");
        arrayList.add("$ 1500-3000");
        arrayList.add("$ 3000-5000");
        arrayList.add("$ 5000+");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddBidActivity.class);
                intent.putExtra("jobId",jobId);
                intent.putExtra("jobTitle",jobTitleText);
                startActivity(intent);
            }
        });
        showJobDetails(jobId);
        checkUserAccess(jobId);
        showBids(jobId);
    }
    public void deleteJob(String jobId)
    {

        if(category.equals("null"))
        {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.putExtra("UID",firebaseUser.getUid());
            FirebaseDatabase.getInstance().getReference("Jobs").child(jobId).removeValue();
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(getApplicationContext(), JobsActivity.class);
            intent.putExtra("category",category);
            FirebaseDatabase.getInstance().getReference("Jobs").child(jobId).removeValue();
            startActivity(intent);
            finish();
        }

    }
    public void updateJob(String jobId)
    {
        List<String> list = new ArrayList<>();
        if(chkContent.isChecked())
        {
            list.add("Content");
        }
        if(chkData.isChecked())
        {
            list.add("Data");
        }
        if(chkDesign.isChecked())
        {
            list.add("Design");
        }
        if(chkMobile.isChecked())
        {
            list.add("Mobile");
        }
        if(chkWebsite.isChecked())
        {
            list.add("Website");
        }
        if(chkMarketing.isChecked())
        {
            list.add("Marketing");
        }
        if(list.size()==0)
        {
            chkContent.setError("Please select a requirement");
            chkContent.requestFocus();
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Jobs").child(jobId);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("title",jobTitle.getText().toString());
        hashMap.put("description",jobDescription.getText().toString());
        hashMap.put("price",spinner.getSelectedItem().toString());
        hashMap.put("requirement",list);
        hashMap.put("date",dateTimePicker.getText().toString());
        databaseReference.updateChildren(hashMap);
        Toast.makeText(this, "Job update", Toast.LENGTH_SHORT).show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(access.equals("OK"))
        {
            getMenuInflater().inflate(R.menu.drop_down_button_menu,menu);
        }

        return true;
    }

    private void checkUserAccess(String jobId)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Jobs").child(jobId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Job job = dataSnapshot.getValue(Job.class);
                if(job.getUserId().equals(firebaseUser.getUid()))
                {
                    access="OK";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id =item.getItemId();
        switch (id){
            case R.id.deleteJob:
                deleteJob(jobId);
                break;
            case R.id.updateJob:
                detailsLayout.setVisibility(View.GONE);
                editLayout.setVisibility(View.VISIBLE);
        }
        return true;
    }

    private void showJobDetails(final String jobId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Jobs").child(jobId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Job job= dataSnapshot.getValue(Job.class);
                    title.setText(job.getTitle());
                    jobTitleText=job.getTitle();
                    description.setText(job.getDescription());
                    String job_price = job.getPrice();
                    price.setText(job_price);
                    jobTitle.setText(job.getTitle());
                    jobDescription.setText(job.getDescription());
                    dateTimePicker.setText(job.getDate());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final List<Integer> bidsList= new ArrayList<>();

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Bids");

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Bid bid = snapshot.getValue(Bid.class);
                    if(bid.getJobId().equals(jobId))
                    {
                        bidsList.add(Integer.parseInt(bid.getPayment()));
                    }
                }
                float i =0f;
                for(float pay : bidsList)
                {
                    i=i+pay;
                }
                float result = i/bidsList.size();
                averagePrice.setText("$ "+result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showBids(final String jobId)
    {
        bidList= new ArrayList<>();
        bidList.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bids");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Bid bid = snapshot.getValue(Bid.class);
                    if(bid.getJobId().equals(jobId))
                    {
                        bidList.add(bid);
                    }
                }
                if(bidList.size()==0)
                {
                    showBids.setVisibility(View.GONE);
                }
                Collections.reverse(bidList);
                BidAdapter bidAdapter = new BidAdapter(getApplicationContext(),bidList);
                bidRecyclerView.setAdapter(bidAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}