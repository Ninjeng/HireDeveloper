package com.theakatsuki.hiredevelopers.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theakatsuki.hiredevelopers.Model.Hire;
import com.theakatsuki.hiredevelopers.R;

import java.util.HashMap;

public class HireActivity extends AppCompatActivity {

    EditText etBudget, etDescription;
    Button btnBack, btnSubmit;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire);
        getSupportActionBar().setTitle("Hire");
        Intent intent = getIntent();
        final String userId = intent.getStringExtra("userId");
        etBudget= findViewById(R.id.etBudget);
        etDescription= findViewById(R.id.etDescribe);
        btnBack= findViewById(R.id.GoProfileButton);
        btnSubmit= findViewById(R.id.btnSubmit);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                intent.putExtra("UID",userId);
                startActivity(intent);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
                HireFunction(userId);
            }
        });
    }

    private void validation()
    {
        if(TextUtils.isEmpty(etBudget.getText().toString()))
        {
            etBudget.setError("Place a budget for the project");
            etBudget.requestFocus();
        }

        if(TextUtils.isEmpty(etDescription.getText().toString()))
        {
            etBudget.setError("Place a describe the project");
            etBudget.requestFocus();
        }
    }

    private void HireFunction(final String userId)
    {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hire");
        String hireId = reference.push().getKey();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("userId",firebaseUser.getUid());
        hashMap.put("hireId",hireId);
        hashMap.put("hiringUserId",userId);
        hashMap.put("rejection","No");
        hashMap.put("invitation",true);
        hashMap.put("description",etDescription.getText().toString());
        hashMap.put("budget",etBudget.getText().toString());
        reference.child(hireId).setValue(hashMap);
        Toast.makeText(this, "Invitation has been send", Toast.LENGTH_SHORT).show();
        etBudget.setText("");
        etDescription.setText("");
    }
}