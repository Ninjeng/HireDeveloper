package com.theakatsuki.hiredevelopers.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theakatsuki.hiredevelopers.Model.Bid;
import com.theakatsuki.hiredevelopers.R;

public class AddBidActivity extends AppCompatActivity {

    TextView tvTitle;
    EditText payment, deliver,description;
    Button btnAddBid, btnBack;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bid);
        getSupportActionBar().setTitle("Add bid");

        tvTitle= findViewById(R.id.tvJobTitle);
        payment= findViewById(R.id.bidPayment);
        deliver= findViewById(R.id.bidDelivery);
        description= findViewById(R.id.bidDescription);
        btnAddBid= findViewById(R.id.btnPlaceBid);
        btnBack= findViewById(R.id.btnBack);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        final Intent intent = getIntent();
        final String jobId =intent.getStringExtra("jobId");
        String jobTitle =intent.getStringExtra("jobTitle");
        tvTitle.setText(jobTitle);
        btnAddBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(payment.getText().toString()))
                {
                    payment.setError("Enter a bid");
                    payment.requestFocus();
                }
                else if(TextUtils.isEmpty(deliver.getText().toString()))
                {
                    deliver.setError("Enter a delivery date in days");
                    deliver.requestFocus();
                }
                else if(TextUtils.isEmpty(description.getText().toString()))
                {
                    description.setError("Say something about yourself");
                    description.requestFocus();
                }
                else
                {
                    Bid bid = new Bid(jobId,payment.getText().toString(),deliver.getText().toString(),description.getText().toString(),firebaseUser.getUid());
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bids");
                    databaseReference.child(databaseReference.push().getKey()).setValue(bid);
                    Toast.makeText(AddBidActivity.this, "Bid has been placed", Toast.LENGTH_SHORT).show();
                    payment.setText("");
                    deliver.setText("");
                    description.setText("");
                }
                
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),SingleJobActivity.class);
                intent1.putExtra("JobId",jobId);
                startActivity(intent1);
            }
        });


    }


}