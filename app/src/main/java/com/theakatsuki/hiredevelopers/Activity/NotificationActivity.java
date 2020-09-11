package com.theakatsuki.hiredevelopers.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theakatsuki.hiredevelopers.Model.Hire;
import com.theakatsuki.hiredevelopers.R;

import java.util.HashMap;

public class NotificationActivity extends AppCompatActivity {

    TextView budget,description;
    Button btnAccept, btnDecline;
    String userId="";
    GridLayout gridLayout;
    FirebaseUser firebaseUser;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().setTitle("Notification");

        final Intent intent = getIntent();
        final String hireId = intent.getStringExtra("hireId");
        final String hiringUserId = intent.getStringExtra("hiringUserId");
        budget = findViewById(R.id.tvHireBudget);
        description = findViewById(R.id.tvHireDescription);
        btnAccept = findViewById(R.id.btnAccept);
        btnDecline = findViewById(R.id.btnReject);
        gridLayout = findViewById(R.id.gridsButtons);
        imageView = findViewById(R.id.NotificationImageView);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hire").child(hireId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Hire hire =  dataSnapshot.getValue(Hire.class);
                userId = hire.getUserId();
                if(hire.getRejection().equals("Yes"))
                {
                    budget.setVisibility(View.GONE);
                    description.setText(hire.getDescription());
                    imageView.setVisibility(View.VISIBLE);
                    gridLayout.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.rejected);
                }
                else
                {
                    if(hire.getInvitation().equals(true))
                    {
                        budget.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.INVISIBLE);
                        budget.setText("$ "+(hire.getBudget()));
                        description.setText(hire.getDescription());
                        gridLayout.setVisibility(View.VISIBLE);
                        if (hire.getRejection().equals("Accepted"))
                        {
                            btnAccept.setText("Accepted");
                            btnAccept.setClickable(false);
                        }
                    }
                    else
                    {
                        budget.setVisibility(View.GONE);
                        description.setText(hire.getDescription());
                        imageView.setVisibility(View.VISIBLE);
                        gridLayout.setVisibility(View.GONE);
                        imageView.setImageResource(R.drawable.hurray);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hire");
                String hireId = reference.push().getKey();
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("userId",firebaseUser.getUid());
                hashMap.put("hireId",hireId);
                hashMap.put("hiringUserId",hiringUserId);
                hashMap.put("rejection","Yes");
                hashMap.put("invitation",false);
                hashMap.put("description","Your invitation has been rejected");
                reference.child(hireId).setValue(hashMap);

                Intent intent1 =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent1);
                FirebaseDatabase.getInstance().getReference("Hire").child(hireId).removeValue();
                finish();
            }
        });
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hire").child(hireId);
                final HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("rejection","Accepted");
                reference.updateChildren(hashMap);


                final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Follow").child(firebaseUser.getUid()).child("Following").child(userId);
                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists())
                        {
                            reference1.child("id").setValue(userId);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Hire");
                HashMap<String,Object> hashMap2 = new HashMap<>();
                String hireId = reference2.push().getKey();
                hashMap2.put("userId",firebaseUser.getUid());
                hashMap2.put("hiringUserId",hiringUserId);
                hashMap2.put("rejection","No");
                hashMap2.put("invitation",false);
                hashMap2.put("hireId",hireId);
                hashMap2.put("description","Your invitation has been Accepted");
                reference2.child(hireId).setValue(hashMap2);

                Toast.makeText(NotificationActivity.this, "Invitation accepted", Toast.LENGTH_SHORT).show();
                btnAccept.setText("Accepted");
                btnAccept.setClickable(false);

            }
        });
    }
}