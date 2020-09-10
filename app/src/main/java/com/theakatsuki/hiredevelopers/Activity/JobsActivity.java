package com.theakatsuki.hiredevelopers.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theakatsuki.hiredevelopers.Adapter.DeveloperAdapter;
import com.theakatsuki.hiredevelopers.Adapter.JobAdapter;
import com.theakatsuki.hiredevelopers.Adapter.UserAdapter;
import com.theakatsuki.hiredevelopers.Model.Following;
import com.theakatsuki.hiredevelopers.Model.Job;
import com.theakatsuki.hiredevelopers.Model.User;
import com.theakatsuki.hiredevelopers.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JobsActivity extends AppCompatActivity {

    Button job, dev;
    RecyclerView jobRecyclerView;
    List<User> userList ;
    List<Job> jobList ;
    String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        getSupportActionBar().setTitle("Jobs");
        job = findViewById(R.id.btnShowJobs);
        dev = findViewById(R.id.btnShowDevelopers);
        jobRecyclerView = findViewById(R.id.recyclerViewJobActivity);
        jobRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Intent intent = getIntent();
        category =intent.getStringExtra("category");
        userList= new ArrayList<>();
        jobList= new ArrayList<>();
        dev.setBackgroundColor(getResources().getColor(R.color.clickColor));
        job.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        showUser(category);
        dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dev.setBackgroundColor(getResources().getColor(R.color.clickColor));
                job.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                showUser(category);
            }
        });
        job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                job.setBackgroundColor(getResources().getColor(R.color.clickColor));
                dev.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                showJobs(category);
            }
        });

    }

    private void showUser(final String category) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                jobList.clear();
                for(DataSnapshot snapshot :  dataSnapshot.getChildren())
                {
                    User user = snapshot.getValue(User.class);
                    List<String> programming= user.getProgramming();
                    for(String program:programming)
                    {
                        if(program.equals(category))
                        {
                            userList.add(user);
                        }
                    }

                }
                DeveloperAdapter userAdapter = new DeveloperAdapter(getApplicationContext(),userList,true);
                jobRecyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void showJobs(final String category) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Jobs");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jobList.clear();
                userList.clear();
                for(DataSnapshot snapshot :  dataSnapshot.getChildren())
                {
                    Job job = snapshot.getValue(Job.class);
                    List<String> programming= job.getRequirement();
                    for(String program:programming)
                    {
                        if(program.equals(category))
                        {
                            jobList.add(job);
                        }
                    }

                }
                Collections.reverse(jobList);
                JobAdapter jobAdapter = new JobAdapter(getApplicationContext(),jobList,category);
                jobRecyclerView.setAdapter(jobAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}